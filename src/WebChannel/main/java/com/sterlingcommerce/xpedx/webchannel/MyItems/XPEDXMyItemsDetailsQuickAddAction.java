package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsQuickAddAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsDetailsQuickAddAction.class);
	
	public static final String COMMAND_VALIDATE_ITEM 	= "validate_item";
	public static final String COMMAND_ADD_ITEMS 		= "add_items";
	
	private Document outDoc;
	private String prodId = "";
	private String listKey = "";
	private String listName = "";
	private String listDesc	= "";
	
	private String command 	= COMMAND_ADD_ITEMS;

	private String itemId	= "";
	private String jobId 	= "";
	private String qty 		= "";
	private String order	= "";
	private String uomId	= "";
	private String itemType = "";
	private String itemTypeText = "";
	private String name 	= "";
	private String desc		= "";
	private String purchaseOrder = "";
	private String itemManufacturerId 	= "";
	private String itemCustomerPartId 	= "";
	private String itemMpcId 			= "";

	private String[] names;
	private String[] descs;
	private String[] jobIds;
	private String[] qtys;
	private String[] uoms;
	private String[] purchaseOrders;
	private String[] itemIds;
	private String[] itemTypes;
	//Add only valid items
	private ArrayList<String> orderedNames;
	private ArrayList<String> orderedDescs;
	private ArrayList<String> orderedJobIds;
	private ArrayList<String> orderedQtys;
	private ArrayList<String> orderedUoms;
	private ArrayList<String> orderedPurchaseOrders;
	private ArrayList<String> orderedItemIds;
	private ArrayList<String> orderedItemTypes;
	
	private ArrayList<String> orders = new ArrayList<String>();
	private ArrayList<String> listKeyAsList = new ArrayList<String>();
	private ArrayList<String> createUserNameList = new ArrayList<String>();
	private ArrayList<String> createUserIdList = new ArrayList<String>();
	private boolean customerPOFlag = false;
	private boolean jobIdFlag = false;
	
	private String itemCount = "";
	private String editMode = "";
	private boolean isItemValid = false;
	private String validateItemId = "";
	protected Map displayItemUOMsMap;
	
	//-- Web Trends tag start --
	private boolean addToListMetaTag = false;
	
	public boolean isAddToListMetaTag() {
		return addToListMetaTag;
	}
	public void setAddToListMetaTag(boolean addToListMetaTag) {
		this.addToListMetaTag = addToListMetaTag;
	}
	//-- Web Trends tag end --

	public boolean isJobIdFlag() {
		return jobIdFlag;
	}

	public void setJobIdFlag(boolean jobIdFlag) {
		this.jobIdFlag = jobIdFlag;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	public String[] getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(String[] purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}
	
    public String getUniqueId(){
    	return System.currentTimeMillis() + "";
    }

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Map<String,String> getUOMList(String customerId, String itemID, String organizationCode){
		Map<String,String> uoms = null;
		try {
			//Map containing UOMCode as key and ConvFactor as value
			uoms = XPEDXOrderUtils.getXpedxUOMList(customerId, itemID, organizationCode);
			//displayItemUOMsMap = new HashMap();
			displayItemUOMsMap = uoms;
			/*for (Iterator it = uoms.keySet().iterator(); it.hasNext();){
				String uomDesc = (String) it.next();
				Object o = uoms.get(uomDesc);
				if("1".equals(o))
				{
					displayItemUOMsMap.put(XPEDXWCUtils.getUOMDescription(uomDesc), uomDesc);
				}
				else{
					displayItemUOMsMap.put(XPEDXWCUtils.getUOMDescription(uomDesc) + " (" + o + ")", uomDesc);
				}
				
			}*/
			for (Iterator it = uoms.keySet().iterator(); it.hasNext();) {
				String uomCode = (String) it.next();
				String convFact = (String)uoms.get(uomCode);
				double convFac = Double.parseDouble(convFact);
				//Map containing UOMCode as key and Desc ( ConvFactor ) as value
				if(new Double(convFac).intValue() == 1)
				{
					displayItemUOMsMap.put(uomCode, XPEDXWCUtils.getUOMDescription(uomCode));
				}
				else{
					displayItemUOMsMap.put(uomCode, XPEDXWCUtils.getUOMDescription(uomCode)
							+ " (" + convFact + ")");
				}
				
			}
			
			//Sample test data for lack of real data
			/*
			uoms = new HashMap<String, String>();
			uoms.put("uom1", "Carton (1000)");
			uoms.put("uom2", "Carton (500)");
			uoms.put("uom3", "Roll");
			*/
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
		return uoms;
	}
	
	private void validateItemForXPEDX() {
		try {
			
			//Populate all the fields
			Element res = prepareAndInvokeMashup("XPEDXMyItemsDetailsAddFromCatalog");
			Element itemEle 		= getXMLUtils().getChildElement(res, "Item");
			//Element primaryInfoEle 	= getXMLUtils().getChildElement(itemEle, "PrimaryInformation");
			//itemEle will be null if item is not entitled
			if (itemEle != null){
				Element primaryInfoEle 	= getXMLUtils().getChildElement(itemEle, "PrimaryInformation");
				//setName(primaryInfoEle.getAttribute("DisplayItemDescription"));
				/* 
				 * Arun 3/29
				 * setDesc changed to setDescMil to facilitate the li tags
				 * that are in the description information . If its changed
				 * back to setDesc the MIl edit - quick add will break
				 */
				if (primaryInfoEle != null){
					setName(primaryInfoEle.getAttribute("DisplayItemDescription"));
					setDescMil(primaryInfoEle.getAttribute("Description"));
				}
				//setDescMil(primaryInfoEle.getAttribute("Description"));
				setUomId( itemEle.getAttribute("UnitOfMeasure") );
				setIsItemValid(true);
				//Commenting the below method as the XPEDXMyItemsDetailsAddFromCatalog mashup returns only entitled items
				/*if ( validateItemEntitlement(getItemId()) ){
					setIsItemValid(true);
				}*/
			}
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void validateItemForManufacture() {
		try {
			//Populate all the fields
			setItemManufacturerId(getItemId());
			setItemId("");
			
			Element res = prepareAndInvokeMashup("XPEDXMyItemsDetailsAddFromCatalog");
			ArrayList<Element> itemEle = getXMLUtils().getElements(res, "Item");
			//Element primaryInfoEle 	= getXMLUtils().getChildElement(itemEle, "PrimaryInformation");
			//itemEle will be null if item is not entitled
			if (itemEle != null){
				for(Iterator<Element> iterator1 = itemEle.iterator(); iterator1.hasNext();){
					Element itemList = (Element)iterator1.next();
					Element primaryInfoEle 	= getXMLUtils().getChildElement(itemList, "PrimaryInformation");
				/* 
				 * Arun 3/29
				 * setDesc changed to setDescMil to facilitate the li tags
				 * that are in the description information . If its changed
				 * back to setDesc the MIl edit - quick add will break
				 */
				if (primaryInfoEle != null){
					setName(primaryInfoEle.getAttribute("DisplayItemDescription"));
					//setDesc(primaryInfoEle.getAttribute("Description"));
					setDescMil(primaryInfoEle.getAttribute("Description"));
					}
				setUomId( itemList.getAttribute("UnitOfMeasure") );
				setItemId(itemList.getAttribute("ItemID"));
				//setIsItemValid(true);
				//Validate the item passing the legacy item id
				if ( validateItemEntitlement(getItemId()) ){
					setIsItemValid(true);
					break;
				} else {
					setItemId(getItemManufacturerId());
				}
			}
			}
			else {
				setItemId(getItemManufacturerId());
			}
			
				
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}

	private void validateItemForCustomerPart() {
		try {
			//Populate all the fields
			setItemCustomerPartId(getItemId());
			setItemId("");
			
			Map<String, String> valueMap = new HashMap<String, String>();

	        /*
	         * Fetching the customer information from the customer object in the session.
	         */
	        
	        XPEDXShipToCustomer shipToCustomer =(XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
	        
	        String envCode = shipToCustomer.getExtnEnvironmentCode();
	        String customerNumber = shipToCustomer.getExtnLegacyCustNumber();
	        String customerDivision = shipToCustomer.getExtnCustomerDivision();
	        
	        if(envCode==null || /*companyCode==null*/ customerDivision == null || customerNumber ==null)
	        {
	        	Element out = prepareAndInvokeMashup("xpedx-customer-getExtnFieldsForCustomerPart");
				Element extnElement = getXMLUtils().getChildElement(out, "Extn");
				if(extnElement!=null)
				{
					customerNumber = extnElement.getAttribute("ExtnLegacyCustNumber");
					envCode = extnElement.getAttribute("ExtnEnvironmentCode");
					//companyCode = extnElement.getAttribute("ExtnCompanyCode");
					customerDivision = extnElement.getAttribute("ExtnCustomerDivision");
				}
				else 
				{
					setItemId(getItemCustomerPartId());
					return;
				}
	        }
			
			valueMap.put("/XPXItemcustXref/@EnvironmentCode", envCode);
			valueMap.put("/XPXItemcustXref/@CustomerDivision", customerDivision);
			valueMap.put("/XPXItemcustXref/@CustomerNumber", customerNumber);
			valueMap.put("/XPXItemcustXref/@CustomerItemNumber", getItemCustomerPartId());
			
			Element input = WCMashupHelper.getMashupInput("XPEDXMyItemsListGetCustomersPart", valueMap, wcContext.getSCUIContext());
			Element res = (Element)WCMashupHelper.invokeMashup("XPEDXMyItemsListGetCustomersPart",input , getWCContext().getSCUIContext());
		
			Element itemEle 		= getXMLUtils().getChildElement(res, "XPXItemcustXref");
		
			if (itemEle != null){
				setItemId(itemEle.getAttribute("LegacyItemNumber"));

			//Get this data from the product information
			//Populate all the fields
				Element res1 = prepareAndInvokeMashup("XPEDXMyItemsDetailsAddFromCatalog");
				//Element itemEle1 		= getXMLUtils().getChildElement(res1, "Item");
			
				//ArrayList<Element> itemEle1 = getXMLUtils().getElements(res, "Item");
				ArrayList<Element> itemEle1 = getXMLUtils().getElements(res1, "Item");/* Modified code for Jira 3294 ****/
				//Element primaryInfoEle 	= getXMLUtils().getChildElement(itemEle1, "PrimaryInformation");
				//itemEle will be null if item is not entitled
				if (itemEle1 != null){
					for(Iterator<Element> itr = itemEle1.iterator(); itr.hasNext();){
						Element itemListCust = (Element)itr.next();
						//Element primaryInfoEle 	= getXMLUtils().getChildElement(itemListMpc, "PrimaryInformation");
					Element primaryInfoEle 	= getXMLUtils().getChildElement(itemListCust, "PrimaryInformation");
					/* 
					 * Arun 3/29
					 * setDesc changed to setDescMil to facilitate the li tags
					 * that are in the description information . If its changed
					 * back to setDesc the MIl edit - quick add will break
					 */
					if (primaryInfoEle != null){
						setName(primaryInfoEle.getAttribute("DisplayItemDescription"));
						//setDesc(primaryInfoEle.getAttribute("Description"));
						setDescMil(primaryInfoEle.getAttribute("Description"));
					}
					setUomId( itemListCust.getAttribute("UnitOfMeasure") );
					setIsItemValid(true);
					//Commenting the below method as the XPEDXMyItemsDetailsAddFromCatalog mashup returns only entitled items
					/*if ( validateItemEntitlement(getItemId()) ){
						setIsItemValid(true);
					}*/
					}
				} else {
					//Item is not entitled
					setItemId(getItemCustomerPartId());
				}
			} else {
				//There is no valid item with the given customer part number
				setItemId(getItemCustomerPartId());
			}
			
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void validateItemForMpcCode() {
		try {
			//Populate all the fields
			setItemMpcId(getItemId());
			setItemId("");
			
			Element res = prepareAndInvokeMashup("XPEDXMyItemsDetailsAddFromCatalog");
			//Element primaryInfoEle 	= getXMLUtils().getChildElement(itemEle, "PrimaryInformation");
			//itemEle will be null if item is not entitled
			ArrayList<Element> itemElement = getXMLUtils().getElements(res, "Item");
			//Element primaryInfoEle 	= getXMLUtils().getChildElement(itemEle, "PrimaryInformation");
			//itemEle will be null if item is not entitled
			if (itemElement != null){
				for(Iterator<Element> iterator1 = itemElement.iterator(); iterator1.hasNext();){
					Element itemListMpc = (Element)iterator1.next();
					Element primaryInfoEle 	= getXMLUtils().getChildElement(itemListMpc, "PrimaryInformation");
				/* 
				 * Arun 3/29
				 * setDesc changed to setDescMil to facilitate the li tags
				 * that are in the description information . If its changed
				 * back to setDesc the MIl edit - quick add will break
				 */
				if (primaryInfoEle != null){
					setName(primaryInfoEle.getAttribute("DisplayItemDescription"));
					//setDesc(primaryInfoEle.getAttribute("Description"));
					setDescMil(primaryInfoEle.getAttribute("Description"));
					}
				setUomId( itemListMpc.getAttribute("UnitOfMeasure") );
				setItemId(itemListMpc.getAttribute("ItemID"));
				//setIsItemValid(true);
				//Validate the item passing the legacy item id
				if ( validateItemEntitlement(getItemId()) ){
					setIsItemValid(true);
					break;
				}
				}
			} else {
				setItemId(getItemMpcId());
			}
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}

	private void validateItem() {
		try {
			int it = Integer.parseInt(getItemType());
			
			switch (it) {
			
			case 1:
				validateItemForXPEDX();
				break;
			case 2:
				validateItemForManufacture();
				break;
			case 3:
				validateItemForCustomerPart();
				break;
			case 4:
				validateItemForMpcCode();
				break;

			default:
				break;
			}
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}
	
	private boolean validateItemEntitlement(String itemId) {
		boolean res = false;
		try {
			//Populate all the fields
			setValidateItemId(itemId);
			
			Element tmpRes 	= prepareAndInvokeMashup("XPEDXMyItemsListValidateItem");
			Element itemEle = getXMLUtils().getChildElement(tmpRes, "Item");
			
			if (itemEle.getAttribute("ItemID").equals(itemId)){
				res = true;
			}
			
		} catch (Exception e) {
			res = false;
			LOG.error(e.getStackTrace());
		}
		
		return res;
	}

	@Override
	public String execute() {
		
		try {
			//Some validation
			if (getItemCount().trim().length() == 0){
				setItemCount("0");
			}
			
			if (getCommand().equals(COMMAND_VALIDATE_ITEM)){
				validateItem();
				return "validate_item";
			}
			
			if (getCommand().equals(COMMAND_ADD_ITEMS)){
				
				orderedNames = new ArrayList<String>();
				orderedDescs = new ArrayList<String>();
				orderedJobIds = new ArrayList<String>();
				orderedQtys = new ArrayList<String>();
				orderedUoms = new ArrayList<String>();
				orderedPurchaseOrders = new ArrayList<String>();
				orderedItemIds = new ArrayList<String>();
				orderedItemTypes = new ArrayList<String>();
				
				//Init the counter
				int counter = 0;
				try { 
					counter = Integer.parseInt(getItemCount()); 
					if (counter < 0){ counter = 0; } 
				} catch (Exception e) {}
				
				//Add the items
				/*
				for (int i = 0; i < itemIds.length; i++) {
					setName(names[i]);
					setDesc(descs[i]);
					if(jobIds!=null && jobIds[i]!=null)
						setJobId(jobIds[i]);
					setQty(qtys[i]);
					setItemId(itemIds[i]);
					setItemType(itemTypes[i]);
					setUomId(uoms[i]);
					if(purchaseOrders!=null && purchaseOrders[i] != null)
						setPurchaseOrder(purchaseOrders[i]);
					counter++;
					setOrder(counter+"");
					WCMashupHelper.getMashupInput("XPEDXMyItemsDetailsCreate", wcContext);
					Element tmpResult = prepareAndInvokeMashup("XPEDXMyItemsDetailsCreate");
					if (tmpResult == null){
						LOG.error("The result of the item insert was null.");
						return ERROR;
					}
				} */
				//Adding Items using multi api 
				for (int i = 0; i < itemIds.length; i++) {
					String uom = uoms[i];
					//Uom will be null/empty for invalid item
					if(uom==null || uom.length()==0)
						continue;
					orderedNames.add(names[i]);
					orderedDescs.add(descs[i]);
					if(jobIds!=null && jobIds[i]!=null)
						orderedJobIds.add(jobIds[i]);
					orderedQtys.add(qtys[i]);
					orderedItemIds.add(itemIds[i]);
					orderedItemTypes.add(itemTypes[i]);
					orderedUoms.add(uoms[i]);
					if(purchaseOrders!=null && purchaseOrders[i] != null)
						orderedPurchaseOrders.add(purchaseOrders[i]);
					counter++;
					orders.add(counter+"");
					listKeyAsList.add(listKey);
					createUserNameList.add(wcContext.getLoggedInUserName());
					createUserIdList.add(wcContext.getLoggedInUserId());
				}
				//Call the mashup to add to list only when valid items size > 0
				if(orderedItemIds!=null && orderedItemIds.size()>0){
					Element tmpResult = prepareAndInvokeMashup("XPEDXMyItemsQuickAddMulti");
					if (tmpResult == null){
						LOG.error("The result of the item insert was null.");
						return ERROR;
					}
					
					//-- Web Trends tag start --
					addToListMetaTag = true;				
					request.getSession().setAttribute("metatagName","DCSext.w_x_list_additem");
					request.getSession().setAttribute("metatagValue","1");
					
					//-- Web Trends tag end --
					
					setItemCount(counter+"");
				}
			}
			
		} catch (Exception e) {
			LOG.error(e.toString());
			return SUCCESS;
		}
		
		return SUCCESS;
	}
	
	/*@Override
	protected void manipulateMashupInputs(Map<String, Element> mashupInputs)
			throws CannotBuildInputException {
		Element input = mashupInputs.get("XPEDXMyItemsQuickAddMulti");
		if (!YFCCommon.isVoid(input)){
			YFCElement inp = YFCDocument.getDocumentFor(input.getOwnerDocument()).getDocumentElement();
			YFCElement itemsList = inp.getChildElement("XPEDXMyItemsItemsList");
			Iterator itemIter = itemsList.getChildren();
			List remElems = new ArrayList();
			while (itemIter.hasNext())
			{
				YFCElement myItemsItemsEle = (YFCElement)itemIter.next();
				if(myItemsItemsEle!=null){
					String uom = myItemsItemsEle.getAttribute("UomId");
					if (YFCCommon.isVoid(uom)){
							remElems.add(myItemsItemsEle);
					}
					
				}
			}//while loop ends
			for (int i=0; i < remElems.size(); i++){
				itemsList.removeChild((YFCElement)remElems.get(i));
			}
			input = inp.getOwnerDocument().getDocument().getDocumentElement();
		}
		
		if(input!=null) {
			System.out.println(SCXmlUtil.getString(input));
		}
		// TODO Auto-generated method stub
		super.manipulateMashupInputs(mashupInputs);
	}*/
	

	public Document getOutDoc() {
		return outDoc;
	}

	public void setOutDoc(Document outDoc) {
		this.outDoc = outDoc;
	}

	public String getProdId() {
		String res = prodId;
		try {
			if (res == null){
				res = "INVALID_ITEM";
			} else {
				if (res.trim().length() == 0){
					res = "INVALID_ITEM";
				}
			}
		} catch (Exception e) {
			res = "ERROR_INVALID_ITEM";
		}
		return res;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean getIsItemValid() {
		return isItemValid;
	}

	public void setIsItemValid(boolean isItemValid) {
		this.isItemValid = isItemValid;
	}

	public String getItemTypeText() {
		return itemTypeText;
	}

	public void setItemTypeText(String itemTypeText) {
		this.itemTypeText = itemTypeText;
	}

	public String[] getJobIds() {
		return jobIds;
	}

	public void setJobIds(String[] jobIds) {
		this.jobIds = jobIds;
	}

	public String[] getQtys() {
		return qtys;
	}

	public void setQtys(String[] qtys) {
		this.qtys = qtys;
	}

	public String[] getItemIds() {
		return itemIds;
	}

	public void setItemIds(String[] itemIds) {
		this.itemIds = itemIds;
	}

	public String[] getItemTypes() {
		return itemTypes;
	}

	public void setItemTypes(String[] itemTypes) {
		this.itemTypes = itemTypes;
	}

	public String getEditMode() {
		return editMode;
	}

	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}

	public String getUomId() {
		return uomId;
	}

	public void setUomId(String uomId) {
		this.uomId = uomId;
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
		String desc2 = desc;
		try {
			desc2 = StringUtils.replace(desc2, "<", "taglt");
			desc2 = StringUtils.replace(desc2, ">", "taggt");
			desc2 = StringUtils.replace(desc2, "/", "tagfs");
			desc2 = StringUtils.replace(desc2, "<script", "");
			desc2 = StringUtils.replace(desc2, "<embed", "");
			desc2 = StringUtils.replace(desc2, "<object", "");
			
		} catch (Exception e) {
			desc2 = desc;
		}
		this.desc = desc2;
	}
	
	/*
	 * Arun 3/29 - Creating a new method as the description
	 * tags will comeas <li> desc </li> and need to be set 
	 * in the description information in li . These tags don't
	 * have to be edited for the MIL
	 */
	public void setDescMil(String desc) {
		String desc2 = desc;
		try {
//			desc2 = StringUtils.replace(desc2, "<", "taglt");
//			desc2 = StringUtils.replace(desc2, ">", "taggt");
//			desc2 = StringUtils.replace(desc2, "/", "tagfs");
		
			desc2 = StringUtils.replace(desc2, "<script", "");
			desc2 = StringUtils.replace(desc2, "<embed", "");
			desc2 = StringUtils.replace(desc2, "<object", "");
			
		} catch (Exception e) {
			desc2 = desc;
		}
		this.desc = desc2;
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public String[] getDescs() {
		return descs;
	}

	public void setDescs(String[] descs) {
		this.descs = descs;
	}

	public String[] getUoms() {
		return uoms;
	}

	public void setUoms(String[] uoms) {
		this.uoms = uoms;
	}
	
	public String getItemManufacturerId() {
		return itemManufacturerId;
	}

	public void setItemManufacturerId(String itemManufacturerId) {
		this.itemManufacturerId = itemManufacturerId;
	}

	public String getItemCustomerPartId() {
		return itemCustomerPartId;
	}

	public void setItemCustomerPartId(String itemCustomerPartId) {
		this.itemCustomerPartId = itemCustomerPartId;
	}

	public String getItemMpcId() {
		return itemMpcId;
	}

	public void setItemMpcId(String itemMpcId) {
		this.itemMpcId = itemMpcId;
	}

	public String getItemId() {
		String res = itemId;
		/*
		try {
			if (res == null){
				res = "INVALID_ITEM";
			} else {
				if (res.trim().length() == 0){
					res = "INVALID_ITEM";
				}
			}
		} catch (Exception e) {
			res = "ERROR_INVALID_ITEM";
		}
		*/
		return res;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getValidateItemId() {
		return validateItemId;
	}

	public void setValidateItemId(String validateItemId) {
		this.validateItemId = validateItemId;
	}

	public String getListDesc() {
		return listDesc;
	}

	public void setListDesc(String listDesc) {
		this.listDesc = listDesc;
	}
	
	public Map getDisplayItemUOMsMap() {
		return displayItemUOMsMap;
	}
	
	public Map getDisplayItemUOMsMap(String customerId, String itemID, String organizationCode) {
		getUOMList(customerId, itemID, organizationCode);
		return displayItemUOMsMap;
	}

	public void setDisplayItemUOMsMap(Map displayItemUOMsMap) {
		this.displayItemUOMsMap = displayItemUOMsMap;
	}

	public boolean isCustomerPOFlag() {
		return customerPOFlag;
	}

	public void setCustomerPOFlag(boolean customerPOFlag) {
		this.customerPOFlag = customerPOFlag;
	}
	public ArrayList<String> getOrders() {
		return orders;
	}
	public void setOrders(ArrayList<String> orders) {
		this.orders = orders;
	}
	public ArrayList<String> getListKeyAsList() {
		return listKeyAsList;
	}
	public void setListKeyAsList(ArrayList<String> listKeyAsList) {
		this.listKeyAsList = listKeyAsList;
	}
	public ArrayList<String> getCreateUserNameList() {
		return createUserNameList;
	}
	public void setCreateUserNameList(ArrayList<String> createUserNameList) {
		this.createUserNameList = createUserNameList;
	}
	public ArrayList<String> getCreateUserIdList() {
		return createUserIdList;
	}
	public void setCreateUserIdList(ArrayList<String> createUserIdList) {
		this.createUserIdList = createUserIdList;
	}
	public ArrayList<String> getOrderedNames() {
		return orderedNames;
	}
	public void setOrderedNames(ArrayList<String> orderedNames) {
		this.orderedNames = orderedNames;
	}
	public ArrayList<String> getOrderedDescs() {
		return orderedDescs;
	}
	public void setOrderedDescs(ArrayList<String> orderedDescs) {
		this.orderedDescs = orderedDescs;
	}
	public ArrayList<String> getOrderedJobIds() {
		return orderedJobIds;
	}
	public void setOrderedJobIds(ArrayList<String> orderedJobIds) {
		this.orderedJobIds = orderedJobIds;
	}
	public ArrayList<String> getOrderedQtys() {
		return orderedQtys;
	}
	public void setOrderedQtys(ArrayList<String> orderedQtys) {
		this.orderedQtys = orderedQtys;
	}
	public ArrayList<String> getOrderedUoms() {
		return orderedUoms;
	}
	public void setOrderedUoms(ArrayList<String> orderedUoms) {
		this.orderedUoms = orderedUoms;
	}
	public ArrayList<String> getOrderedPurchaseOrders() {
		return orderedPurchaseOrders;
	}
	public void setOrderedPurchaseOrders(ArrayList<String> orderedPurchaseOrders) {
		this.orderedPurchaseOrders = orderedPurchaseOrders;
	}
	public ArrayList<String> getOrderedItemIds() {
		return orderedItemIds;
	}
	public void setOrderedItemIds(ArrayList<String> orderedItemIds) {
		this.orderedItemIds = orderedItemIds;
	}
	public ArrayList<String> getOrderedItemTypes() {
		return orderedItemTypes;
	}
	public void setOrderedItemTypes(ArrayList<String> orderedItemTypes) {
		this.orderedItemTypes = orderedItemTypes;
	}
	
	
}
