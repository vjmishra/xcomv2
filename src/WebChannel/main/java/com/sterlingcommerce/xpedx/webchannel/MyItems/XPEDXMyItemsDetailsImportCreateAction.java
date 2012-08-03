package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsImportCreateAction extends XPEDXMyItemsDetailsCreateAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsDetailsImportCreateAction.class);
	
	private String[] itemsIds;
	private String[] itemsName;
	private String[] itemsDesc;
	private String[] itemsQty;
	private String[] itemsUOM;
	private String[] itemsJobId;
	private String[] itemsOrder;
	private String errorMsg		= "";
	private ArrayList<String> errorRows = new ArrayList<String>();
	private boolean editMode = false;
	private HashMap<String, HashMap<String, String>> itemsCustomFields;
	private Document entitledItemsDoc;
	
	@Override
	public String execute() {
		String res = SUCCESS;
		try {
			//Get the data from the session
			
			setItemsIds((String[])request.getSession().getAttribute("itemsId"));
			setItemsName((String[])request.getSession().getAttribute("itemsName"));
			setItemsDesc((String[])request.getSession().getAttribute("itemsDesc"));
			setItemsQty((String[])request.getSession().getAttribute("itemsQty"));
			setItemsUOM((String[])request.getSession().getAttribute("itemsUOM"));
			setItemsJobId((String[])request.getSession().getAttribute("itemsJobId"));
			setItemsOrder((String[])request.getSession().getAttribute("itemsOrder"));
			setErrorMsg((String)request.getSession().getAttribute("errorMsg"));
			setItemsCustomFields( (HashMap<String, HashMap<String, String>>)request.getSession().getAttribute("itemsCustomFields") );
			
		} catch (Exception e) {
		}
		/*
		 * This is to set the item id's if the customer part number is used to import.
		 * We get the details using a complex query and get the entitled item details of all the item id's.
		 * We make a getCompleteItemList call here instead of the call in the import prepare action.
		 */
		ArrayList<String> custPartNumbers = new ArrayList<String>();
		HashMap<String, String> custpartNitemidMap = new LinkedHashMap<String, String>();
		if (itemsName != null && itemsName.length > 0 && getErrorMsg().trim().length() == 0) {
			for (int i = 0; i < itemsName.length; i++) {
				String tmpItemName = itemsName[i];
				String tmpItemId = itemsName[i];
				if(tmpItemName!=null && tmpItemName.trim().length()>0 && tmpItemId.equals("INVALID_ITEM")) {
					custPartNumbers.add(tmpItemName);
				}
			}
			Document itemCustXrefDoc = XPEDXWCUtils.getItemCustXrefDoc(getWCContext(), "CustomerItemNumber", custPartNumbers);
			if(itemCustXrefDoc!=null) {
				ArrayList<Element> itemcustXrefs = SCXmlUtil.getElements(itemCustXrefDoc.getDocumentElement(), "XPXItemcustXref");
				for(Element itemcustXref : itemcustXrefs) {
					String custpartNum = SCXmlUtil.getAttribute(itemcustXref,"CustomerItemNumber");
					String legacyItemNumber = SCXmlUtil.getAttribute(itemcustXref,"LegacyItemNumber");
					if(!(SCUtil.isVoid(custpartNum) && SCUtil.isVoid(legacyItemNumber)))
						custpartNitemidMap.put(custpartNum, legacyItemNumber);
				}
			}
			Set<String> itemIdset = new HashSet<String>();
			Collections.addAll(itemIdset,itemsIds);
			itemIdset.addAll(custpartNitemidMap.values());
			if(itemIdset != null && itemIdset.size()>0){
				entitledItemsDoc = XPEDXMyItemsUtils.getEntitledItemsDocument(getWCContext(),new ArrayList(itemIdset));
			}
		}
		
		
		try {
			/*
			 * We will create all the elements at the same time. 
			 * This way we can reduce the iterated DB calls to create items in list
			 */
			Document myitemsList = SCXmlUtil.createDocument("XPEDXMyItemsList");
			Element myItemsItemsList = myitemsList.createElement("XPEDXMyItemsItemsList");
			myitemsList.getDocumentElement().appendChild(myItemsItemsList);
			
			XPEDXShipToCustomer shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			String envCode =  shipToCustomer.getExtnEnvironmentCode();
			
			if (itemsName != null && itemsName.length > 0 && getErrorMsg().trim().length() == 0){
				for (int i = 0; i < itemsName.length; i++) {
					//Populate the current data
					setName(itemsName[i]);
					setDesc(itemsDesc[i]);
					setQty(itemsQty[i]);
					setUomId(itemsUOM[i]); 
					setJobId(itemsJobId[i]);
					setItemType("99"); //Custom item
					setItemId(itemsIds[i]);
					setOrder(itemsOrder[i]);
					
					
					//Load the item information from customer part number
					try {
						if (getItemId().equals("INVALID_ITEM") && getName().trim().length() > 0){
							String newItemId = custpartNitemidMap.get(getName());
							if (!newItemId.equals("")){
								setItemId(newItemId);
							} else {
								setItemId(getName());
							}
						}
					} catch (Exception e) {
						LOG.error(e.toString());
					}
					
					//Validate item
					Element itemElement = SCXmlUtil.getElementByAttribute(entitledItemsDoc.getDocumentElement(), "Item", "ItemID", getItemId());
					if (itemElement!=null){
						setItemType("1");
						
						//Pulling the item information
						try {
							/* Item Element is already there above.
							 * Element tmpRes = XPEDXMyItemsUtils.getItemDetails(
								getItemId(), 
								XPEDXMyItemsUtils.getCurrentCustomerId(getWCContext()), 
								getWCContext().getStorefrontId(), getWCContext()
							).getDocumentElement();*/
							
							if(itemElement!=null){
								Element itemEle 		= getXMLUtils().getChildElement(itemElement, "Item");
								Element primaryInfoEle 	= getXMLUtils().getChildElement(itemEle, "PrimaryInformation");
								
								if (primaryInfoEle != null){
									setName(primaryInfoEle.getAttribute("ShortDescription"));
									setDesc(primaryInfoEle.getAttribute("Description"));
								}
							}
							setUomId(envCode+"_"+getUomId());
							System.err.print("");
						} catch (Exception e) {
							LOG.error(e.toString());
						}
						
					} else {
						//It is an especial item
						//Commenting the below code as non-catalog items cannot be added
						//setName(getItemId());
						//New code to skip adding the non-catalog items
						errorRows.add(""+(i+1));
						continue;
					}
					
					Element myItemItems = myitemsList.createElement("XPEDXMyItemsItems");
					myItemItems.setAttribute("MyItemsListKey", getListKey());
					myItemItems.setAttribute("Name", getName());
					myItemItems.setAttribute("Desc", getDesc());
					myItemItems.setAttribute("Qty", getQty());
					myItemItems.setAttribute("JobId", getJobId());
					myItemItems.setAttribute("ItemId", getItemId());
					myItemItems.setAttribute("ItemType", getItemType());
					myItemItems.setAttribute("UomId", getUomId());
					myItemItems.setAttribute("ItemOrder", getOrder());
					
					HashMap<String, String> customFields = getItemsCustomFields().get(i+"");
					for (Iterator iterator = customFields.keySet().iterator(); iterator.hasNext();) {
						String key 		= (String) iterator.next();
						String value	= (String) customFields.get(key);
						myItemItems.setAttribute(key, value);
					}
					
					myItemsItemsList.appendChild(myItemItems);
										
					/*
					 * Creating all the items at the same time using change*My
					 * Document outputDoc = null;
					IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
					Map<String, String> valueMap = new HashMap<String, String>();
					valueMap.put("/XPEDXMyItemsItems/@MyItemsListKey", 		getListKey());
					valueMap.put("/XPEDXMyItemsItems/@Name", 		getName());
					valueMap.put("/XPEDXMyItemsItems/@Desc", 		getDesc());
					valueMap.put("/XPEDXMyItemsItems/@Qty", 		getQty());
					valueMap.put("/XPEDXMyItemsItems/@JobId", 		getJobId());
					valueMap.put("/XPEDXMyItemsItems/@ItemId", 		getItemId());
					valueMap.put("/XPEDXMyItemsItems/@ItemType", 	getItemType());
					valueMap.put("/XPEDXMyItemsItems/@UomId", 		getUomId());
					valueMap.put("/XPEDXMyItemsItems/@ItemOrder", 	getOrder());
					
					HashMap<String, String> customFields = getItemsCustomFields().get(i+"");
					for (Iterator iterator = customFields.keySet().iterator(); iterator.hasNext();) {
						String key 		= (String) iterator.next();
						String value	= (String) customFields.get(key);
						valueMap.put("/XPEDXMyItemsItems/@" + key, 		value);
					}

					Element input;
					try {
						input = WCMashupHelper.getMashupInput("XPEDXMyItemsDetailsCreate", valueMap, wcContext.getSCUIContext());
						String inputXml = SCXmlUtil.getString(input);
						Object obj = WCMashupHelper.invokeMashup("XPEDXMyItemsDetailsCreate", input, wcContext.getSCUIContext());
						outputDoc = ((Element) obj).getOwnerDocument();						
						if (null != outputDoc) {
							res = SUCCESS; 
							Web Trends tag start 
							request.getSession().setAttribute("metatagName","DCSext.w_x_importlist");
							request.getSession().setAttribute("metatagValue","1");
							Web Trends tag end 
						}
					} catch (CannotBuildInputException e) {
						LOG.error(e.toString());
						setErrorMsg("Level 2.1: " + e.toString());
					}*/
					
				}
				myitemsList.getDocumentElement().setAttribute("MyItemsListKey",getListKey());
				//added for jira 4134
				String isSalesRep = (String) getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP");
				if(isSalesRep!=null && isSalesRep.equalsIgnoreCase("true")){
					String salesreploggedInUserName = (String)getWCContext().getSCUIContext().getSession().getAttribute("loggedInUserName");
					myitemsList.getDocumentElement().setAttribute("ModifyUserName",salesreploggedInUserName);
				} else {
					myitemsList.getDocumentElement().setAttribute("ModifyUserName",getWCContext().getLoggedInUserName());	
				}
				//end of jira 4134
				WCMashupHelper.invokeMashup("XPEDXMyItemsListChange", myitemsList.getDocumentElement(),getWCContext().getSCUIContext());
			}
			//Display the row number which was not imported
			if(errorRows!=null && errorRows.size()>0){
				StringBuffer sb =  new StringBuffer();
				for(int k=0;k<errorRows.size();k++)
				{
					sb.append(errorRows.get(k));
					if(k < (errorRows.size()-1))
						sb.append(",");
				}
				setErrorMsg("ROW_PROCESSING_ERROR@"+sb.toString());
				editMode = true;
				return "failure";
			}
			
		} catch (Exception e) {
			LOG.error(e.toString());
			setErrorMsg("Level 2: " + e.toString());
		}		
		return res;
	}

	public String[] getItemsName() {
		return itemsName;
	}

	public void setItemsName(String[] itemsName) {
		this.itemsName = itemsName;
	}

	public String[] getItemsDesc() {
		return itemsDesc;
	}

	public void setItemsDesc(String[] itemsDesc) {
		this.itemsDesc = itemsDesc;
	}

	public String[] getItemsQty() {
		return itemsQty;
	}

	public void setItemsQty(String[] itemsQty) {
		this.itemsQty = itemsQty;
	}

	public String[] getItemsUOM() {
		return itemsUOM;
	}

	public void setItemsUOM(String[] itemsUOM) {
		this.itemsUOM = itemsUOM;
	}

	public String[] getItemsJobId() {
		return itemsJobId;
	}

	public void setItemsJobId(String[] itemsJobId) {
		this.itemsJobId = itemsJobId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public HashMap<String, HashMap<String, String>> getItemsCustomFields() {
		return itemsCustomFields;
	}

	public void setItemsCustomFields(
			HashMap<String, HashMap<String, String>> itemsCustomFields) {
		this.itemsCustomFields = itemsCustomFields;
	}

	public String[] getItemsIds() {
		return itemsIds;
	}

	public void setItemsIds(String[] itemsIds) {
		this.itemsIds = itemsIds;
	}

	public String[] getItemsOrder() {
		return itemsOrder;
	}

	public void setItemsOrder(String[] itemsOrder) {
		this.itemsOrder = itemsOrder;
	}

	public ArrayList<String> getErrorRows() {
		return errorRows;
	}

	public void setErrorRows(ArrayList<String> errorRows) {
		this.errorRows = errorRows;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
	

}
