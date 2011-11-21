package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsChangeShareListAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsDetailsChangeShareListAction.class);
	public final String MASHUPID_DELETE_SHARE_LIST_ITEMS = "XPEDXMyItemsDetailsDeleteShareListItems";
	
	private Document outDoc;
	
	private String itemCount 	= "-1";
	
	private String listKey 			= ""; //For the listing page
	private String listName 		= ""; //For the listing page
	private String listDesc 		= ""; //For the listing page
	
	private String name				= ""; //This are for the create new list functionality
	private String desc				= "";
	private String sharePrivate		= "";
	
	private String[] customerPaths;
	private String[] customerIds;
	private String[] customerDivs;
	private String[] sslNames;
	private String[] sslValues;
	private String customerId = "";
	private String customerPath = "";
	private String customerDiv = "";
	private String editMode = "";
	private String sharePermissionLevel = "";
	private String shareAdminOnly   = "";
	
	private String clFromListId		= ""; 
	private String clName		= "";
	private String clDesc		= "";
	private String clQty		= "";
	private String clJobId		= "";
	private String clOrder		= "";
	private String clListKey	= "";
	private String clItemId		= "";
	private String clItemType	= "";
	private String clUomId		= "";
	private String clUserId		= "";
	private String clUserName	= "";
	private String clCustField1 = "";
	private String clCustField2 = "";
	private String clCustField3 = "";
	private String clItemPO = "";
	private boolean clAjax = false;
	private boolean newList;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() {
		try {
			boolean itemsAdded = false;
			Map<String, Element> out;
			
			newList = false;
			//Check if this is a new list
			LOG.info("Check 1: getListKey = " + getListKey());
			if (getListKey().trim().equals("new")){
				newList = true;
				setName(getListName());
				setDesc(getListDesc());
				setSharePrivate(getSharePermissionLevel());
				Element res1 = prepareAndInvokeMashup("XPEDXMyItemsListCreate");
				setListKey(res1.getAttribute("MyItemsListKey")) ;
				LOG.info("Check 1 - Done: getListKey = " + getListKey());
			}
			
			//1 - Erase all the list
			Element result = null;
			
			LOG.info("Check 2: Deleting the old data");
			if (!newList){
				if(customerIds != null && customerIds.length>0)
				{
					setSharePrivate(" ");
				}
				else
				{
					setSharePrivate(getSharePermissionLevel());
				}
				result = prepareAndInvokeMashup(MASHUPID_DELETE_SHARE_LIST_ITEMS);
				LOG.info("Check 2: Deleting the old data - Done");
			}
			
			if (result != null || newList){
			//1.5 Sync for deletes
				LOG.info("Check 3: Sync for delete");
				if (sslNames == null){
					sslNames 	= new String[0];
					sslValues 	= new String[0];
				}
				
				String[] finalSSLNames 	= sslNames;
				String[] finalSSLValues	= sslValues;
				String[] finalSSLCDivs	= new String[sslValues.length];
				
				try {
					for (int i = 0; i < sslNames.length; i++) {
						String record = sslNames[i];
						if (ArrayUtils.indexOf(customerIds , record) == -1){
							//Delete this item from the ssl data
							/*
							finalSSLNames 	= (String[])ArrayUtils.remove(finalSSLNames, i);
							finalSSLValues 	= (String[])ArrayUtils.remove(finalSSLValues, i);
							finalSSLCDivs 	= (String[])ArrayUtils.remove(finalSSLCDivs, i);
							*/
							int newIdx = ArrayUtils.indexOf(finalSSLNames , record);
							finalSSLNames 	= (String[])ArrayUtils.remove(finalSSLNames, newIdx);
							finalSSLValues 	= (String[])ArrayUtils.remove(finalSSLValues, newIdx);
							finalSSLCDivs 	= (String[])ArrayUtils.remove(finalSSLCDivs, newIdx);
						}
					}
					LOG.info("Check 3: Sync for delete - Done");
				} catch (Exception e) {
					LOG.error(e.getStackTrace());
				}
			try {
				//1.6 Sync for additions
				LOG.info("Check 4: Sync for additions");
				if (customerIds == null) {
					customerIds = ArrayUtils.EMPTY_STRING_ARRAY;
				}
				for (int i = 0; i < customerIds.length; i++) {
					String record = customerIds[i];
					if (ArrayUtils.indexOf(sslNames, record) == -1) {
						//add the item from the final ssl data
						finalSSLNames = (String[]) ArrayUtils.add(finalSSLNames, record);
						finalSSLValues = (String[]) ArrayUtils.add(finalSSLValues, customerPaths[i]);
						finalSSLCDivs = (String[]) ArrayUtils.add(finalSSLCDivs, customerDivs[i]);
					}

				}
				LOG.info("Check 4: Sync for additions - Done");
			} catch (Exception e) {
				LOG.error(e.toString());
			}
			
			//2 - Insert all the data into the system
				
				//If this list is not a private list then clean up the selection
				if (getSharePermissionLevel().trim().length() > 0){
					finalSSLNames = ArrayUtils.EMPTY_STRING_ARRAY;
					setShareAdminOnly("N");
					LOG.info("Check 5: Cleaning up selection. Shared permission level: " + getSharePermissionLevel());
				}
				
				for (int i = 0; i < finalSSLNames.length; i++) {
					setCustomerId(finalSSLNames[i]);
					setCustomerPath(finalSSLValues[i]);
					setCustomerDiv(finalSSLCDivs[i]);
					
					if (getCustomerId().trim().length()== 0){ continue; }
					LOG.info("Check 6: Saving data: CustomerID = " + getCustomerId() + ", Customer path = " + getCustomerPath());
					out = prepareAndInvokeMashups();
					LOG.info("Check 6: Saving data: CustomerID = " + getCustomerId() + ", Customer path = " + getCustomerPath() + " - Done!");
					itemsAdded = true;
					if (out.get("XPEDXMyItemsDetailsChangeShareList") != null){
						outDoc = (Document)out.get("XPEDXMyItemsDetailsChangeShareList").getOwnerDocument();
					} else {
						LOG.error("No document as result");
						return ERROR;
					}
					
				}
			}
			
			//Save the current selection as to if this list of private or shared.
			/*
			 * Changing this using the Mashup below. Adding the items and making the changes at the same time
			 * LOG.info("Check 7: Saving data current selection");
			Element res = prepareAndInvokeMashup("XPEDXMyItemsListChange");
			LOG.info("Check 7: Saving data current selection - Done");
			*/
			//Copy the items from the other list if specify
			try {
				LOG.info("Check 8: Copy the items from the other list if specify");
				LOG.info("Check 8: getClFromListId  = '" + getClFromListId() + "'");
				if (getClFromListId().trim().length() > 0){
					//1 - Get all the items from this list
					Element elAllItems = prepareAndInvokeMashup("XPEDXMyItemsDetails");
					ArrayList<Element> alAllItems = getXMLUtils().getElements(elAllItems, "XPEDXMyItemsItems");
					
					Document myitemsList = SCXmlUtil.createDocument("XPEDXMyItemsList");
					Element myItemsItemsList = myitemsList.createElement("XPEDXMyItemsItemsList");
					myitemsList.getDocumentElement().appendChild(myItemsItemsList);
					
					//2 - Loop through the items and prepare the data
					for (Iterator iterator = alAllItems.iterator(); iterator.hasNext();) {
						Element tmpRecord = (Element) iterator.next();
						
						clName		 = tmpRecord.getAttribute("Name");
						clDesc		 = tmpRecord.getAttribute("Desc");
						clQty		 = tmpRecord.getAttribute("Qty");
						clCustField1 = tmpRecord.getAttribute("ItemCustomField1");
						clCustField2 = tmpRecord.getAttribute("ItemCustomField2");
						clCustField3 = tmpRecord.getAttribute("ItemCustomField3");
						clJobId		 = tmpRecord.getAttribute("JobId");
						clOrder		 = tmpRecord.getAttribute("ItemOrder");
						clListKey	 = getListKey();
						clItemId	 = tmpRecord.getAttribute("ItemId");
						clItemType	 = tmpRecord.getAttribute("ItemType");
						clUomId		 = tmpRecord.getAttribute("UomId");
						clUserId	 = getWCContext().getLoggedInUserId();
						clUserName	 = getWCContext().getLoggedInUserName();
						clItemPO     = tmpRecord.getAttribute("ItemPoNumber");
						
						//Start - Setting all the values into the my items element.
						Element myItemItems = myitemsList.createElement("XPEDXMyItemsItems");
						
						myItemItems.setAttribute("Name", clName);
						myItemItems.setAttribute("Desc", clDesc);
						myItemItems.setAttribute("Qty", clQty);
						myItemItems.setAttribute("ItemCustomField1", clCustField1);
						myItemItems.setAttribute("ItemCustomField2", clCustField2);
						myItemItems.setAttribute("ItemCustomField3", clCustField3);
						myItemItems.setAttribute("JobId", clJobId);
						myItemItems.setAttribute("ItemOrder", clOrder);
						myItemItems.setAttribute("MyItemsListKey", clListKey);
						myItemItems.setAttribute("ItemId", clItemId);
						myItemItems.setAttribute("ItemType", clItemType);
						myItemItems.setAttribute("UomId", clUomId);
						myItemItems.setAttribute("Createuserid", clUserId);
						myItemItems.setAttribute("Createusername", clUserName);
						myItemItems.setAttribute("ItemPoNumber", clItemPO);
						//End - Setting all the values into the my items element.
						
						//Start - Append the created Element to the MyItemsItemsList Element
						myItemsItemsList.appendChild(myItemItems);
						//End - Append the created Element to the MyItemsItemsList Element
						
						/*
						 * Instead of adding each item every time. 
						 * We are adding all items at the same time using the changeXPEDX_MyItemsList flow
						 * //3 - Add the items
						LOG.info("Check 8: Saving/Adding: List key = " + clListKey + ", Name: " + clName + ", desc: " + clDesc);
						Element elRes = prepareAndInvokeMashup("XPEDXMyItemsDetailsCreate");
						LOG.info("Check 8: Saving/Adding: List key = " + clListKey + ", Name: " + clName + ", desc: " + clDesc + " - Done");
						if (elRes != null){
							
						}*/
					}
					myitemsList.getDocumentElement().setAttribute("SharePrivate",sharePermissionLevel);
					myitemsList.getDocumentElement().setAttribute("ShareAdminOnly", shareAdminOnly);
					myitemsList.getDocumentElement().setAttribute("MyItemsListKey",listKey);
					WCMashupHelper.invokeMashup("XPEDXMyItemsListChange", myitemsList.getDocumentElement(),getWCContext().getSCUIContext());
					
				}
			} catch (Exception e) {
				LOG.error(e.getStackTrace());
				LOG.error("Error copying the Items into the New My Items list");
			}
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			return ERROR;
		}
		
		LOG.info("Check 9: All done!");
		/*Web Trends tag start */
		request.getSession().setAttribute("metatagName","DCSext.w_x_sharelist");
		request.getSession().setAttribute("metatagValue","1");
		/*Web Trends tag end */
		if(clAjax && newList)
			return "copy";
		return SUCCESS;
	}

	public Document getOutDoc() {
		return outDoc;
	}

	public void setOutDoc(Document outDoc) {
		this.outDoc = outDoc;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String[] getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	public String[] getCustomerPaths() {
		return customerPaths;
	}

	public void setCustomerPaths(String[] customerPaths) {
		this.customerPaths = customerPaths;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerPath() {
		return customerPath;
	}

	public void setCustomerPath(String customerPath) {
		this.customerPath = customerPath;
	}

	public String[] getSslNames() {
		return sslNames;
	}

	public void setSslNames(String[] sslNames) {
		this.sslNames = sslNames;
	}

	public String[] getSslValues() {
		return sslValues;
	}

	public void setSslValues(String[] sslValues) {
		this.sslValues = sslValues;
	}

	public String getEditMode() {
		return editMode;
	}

	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}

	public String getSharePermissionLevel() {
		return sharePermissionLevel;
	}

	public void setSharePermissionLevel(String sharePermissionLevel) {
		this.sharePermissionLevel = sharePermissionLevel;
	}

	public String getShareAdminOnly() {
		return shareAdminOnly;
	}

	public void setShareAdminOnly(String shareAdminOnly) {
		this.shareAdminOnly = shareAdminOnly;
	}

	public String getListDesc() {
		return listDesc;
	}

	public void setListDesc(String listDesc) {
		this.listDesc = listDesc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSharePrivate() {
		return sharePrivate;
	}

	public void setSharePrivate(String sharePrivate) {
		this.sharePrivate = sharePrivate;
	}

	public String getClFromListId() {
		return clFromListId;
	}

	public void setClFromListId(String clFromListId) {
		this.clFromListId = clFromListId;
	}

	public String getClName() {
		return clName;
	}

	public void setClName(String clName) {
		this.clName = clName;
	}

	public String getClDesc() {
		return clDesc;
	}

	public void setClDesc(String clDesc) {
		this.clDesc = clDesc;
	}

	public String getClQty() {
		return clQty;
	}

	public void setClQty(String clQty) {
		this.clQty = clQty;
	}

	public String getClJobId() {
		return clJobId;
	}

	public void setClJobId(String clJobId) {
		this.clJobId = clJobId;
	}

	public String getClOrder() {
		return clOrder;
	}

	public void setClOrder(String clOrder) {
		this.clOrder = clOrder;
	}

	public String getClListKey() {
		return clListKey;
	}

	public void setClListKey(String clListKey) {
		this.clListKey = clListKey;
	}

	public String getClItemId() {
		return clItemId;
	}

	public void setClItemId(String clItemId) {
		this.clItemId = clItemId;
	}

	public String getClItemType() {
		return clItemType;
	}

	public void setClItemType(String clItemType) {
		this.clItemType = clItemType;
	}

	public String getClUomId() {
		return clUomId;
	}

	public void setClUomId(String clUomId) {
		this.clUomId = clUomId;
	}

	public String getClUserId() {
		return clUserId;
	}

	public void setClUserId(String clUserId) {
		this.clUserId = clUserId;
	}

	public String getClUserName() {
		return clUserName;
	}

	public void setClUserName(String clUserName) {
		this.clUserName = clUserName;
	}

	public String[] getCustomerDivs() {
		return customerDivs;
	}

	public void setCustomerDivs(String[] customerDivs) {
		this.customerDivs = customerDivs;
	}

	public String getCustomerDiv() {
		return customerDiv;
	}

	public void setCustomerDiv(String customerDiv) {
		this.customerDiv = customerDiv;
	}

	
	public String getClCustField1() {
		return clCustField1;
	}

	public void setClCustField1(String clCustField1) {
		this.clCustField1 = clCustField1;
	}

	public String getClCustField2() {
		return clCustField2;
	}

	public void setClCustField2(String clCustField2) {
		this.clCustField2 = clCustField2;
	}

	public String getClCustField3() {
		return clCustField3;
	}

	public void setClCustField3(String clCustField3) {
		this.clCustField3 = clCustField3;
	}

	public String getClItemPO() {
		return clItemPO;
	}

	public void setClItemPO(String clItemPO) {
		this.clItemPO = clItemPO;
	}

	public boolean isClAjax() {
		return clAjax;
	}

	public void setClAjax(boolean clAjax) {
		this.clAjax = clAjax;
	}
}
