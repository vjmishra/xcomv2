package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCDate;
import java.util.Set;


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
	/*Updated for Jira 4134*/
	private String salesreploggedInUserName;
	
	public String getSalesreploggedInUserName() {
		return salesreploggedInUserName;
	}

	public void setSalesreploggedInUserName(String salesreploggedInUserName) {
		this.salesreploggedInUserName = salesreploggedInUserName;
	}
//end of Jira 4134
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
	private ArrayList<String> countCustomer;
	
	//Updated for JIRA 3920 - Ragini
	private boolean fromItemDetail;
	public boolean isFromItemDetail() {
		return fromItemDetail;
	}

	public void setFromItemDetail(boolean fromItemDetail) {
		this.fromItemDetail = fromItemDetail;
	}

	private String modifiedDate;
	private String orderLineItemIDs;
	private String orderLineItemOrders;
	private String customerLinePONo;
	private String orderLineCustLineAccNo;
	private String orderLineQuantities;
	private String itemUOMs;
	private String orderLineItemNames;
	private String orderLineItemDesc;
	private String itemOrders;
	private String itemType;
	//for jira 4221
	private String loggedInUserName;
	private String loggedInUserID;
	
	public String getLoggedInUserID() {
		return loggedInUserID;
	}

	public void setLoggedInUserID(String loggedInUserID) {
		this.loggedInUserID = loggedInUserID;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemOrders() {
		return itemOrders;
	}

	public void setItemOrders(String itemOrders) {
		this.itemOrders = itemOrders;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getOrderLineItemIDs() {
		return orderLineItemIDs;
	}

	public void setOrderLineItemIDs(String orderLineItemIDs) {
		this.orderLineItemIDs = orderLineItemIDs;
	}

	public String getOrderLineItemOrders() {
		return orderLineItemOrders;
	}

	public void setOrderLineItemOrders(String orderLineItemOrders) {
		this.orderLineItemOrders = orderLineItemOrders;
	}

	public String getCustomerLinePONo() {
		return customerLinePONo;
	}

	public void setCustomerLinePONo(String customerLinePONo) {
		this.customerLinePONo = customerLinePONo;
	}

	public String getOrderLineCustLineAccNo() {
		return orderLineCustLineAccNo;
	}

	public void setOrderLineCustLineAccNo(String orderLineCustLineAccNo) {
		this.orderLineCustLineAccNo = orderLineCustLineAccNo;
	}

	public String getOrderLineQuantities() {
		return orderLineQuantities;
	}

	public void setOrderLineQuantities(String orderLineQuantities) {
		this.orderLineQuantities = orderLineQuantities;
	}

	public String getItemUOMs() {
		return itemUOMs;
	}

	public void setItemUOMs(String itemUOMs) {
		this.itemUOMs = itemUOMs;
	}

	public String getOrderLineItemNames() {
		return orderLineItemNames;
	}

	public void setOrderLineItemNames(String orderLineItemNames) {
		this.orderLineItemNames = orderLineItemNames;
	}

	public String getOrderLineItemDesc() {
		return orderLineItemDesc;
	}

	public void setOrderLineItemDesc(String orderLineItemDesc) {
		this.orderLineItemDesc = orderLineItemDesc;
	}
//End of Jira 3920
	//Added for JIRA 4158
	public  Map<String,String> getDivisionMap() 
	{
		Map<String,String> divisionMap = new HashMap<String,String>();
		//Map which holds complete hierarchy in tree format starting from saps->billtos->shiptos
		Map<String,Map<String,Map<String,String>>> sapIds = new HashMap<String,Map<String,Map<String,String>>>();
		//getting customer hierarchy for MSAP customer
		String AttributeToQry = "MSAPCustomerID";
		Document inputDoc = SCXmlUtil.createDocument("XPXCustHierachyView");
		String customerID=(String)wcContext.getSCUIContext()
			.getSession().getAttribute(XPEDXWCUtils.LOGGED_IN_CUSTOMER_ID);
		inputDoc.getDocumentElement().setAttribute(AttributeToQry, customerID);	
		Object obj = WCMashupHelper.invokeMashup("xpedx-getAssignedShipTos-View", inputDoc.getDocumentElement(), wcContext.getSCUIContext());
		Element outputElement = ((Element)obj);
		NodeList customerHierView = outputElement.getElementsByTagName("XPXCustHierarchyView");
		for(int j=0;j<customerHierView.getLength();j++) 
		{
			Element customerHierViewElem = (Element)customerHierView.item(j);
			String extnShipFromBranch = customerHierViewElem.getAttribute("ExtnCustomerDivision");
			String shipToCustomerID = customerHierViewElem.getAttribute("ShipToCustomerID");			
			String billToCustomerID = customerHierViewElem.getAttribute("BillToCustomerID");
			String sapCustomerID = customerHierViewElem.getAttribute("SAPCustomerID");
			Set<String> sapIdSet=sapIds.keySet() ;
			//if already sap exist get the sap and add the bilto in it.
			if(sapIdSet != null && sapIdSet.contains(sapCustomerID))
			{
				Map<String,Map<String,String>>  billTos =sapIds.get(sapCustomerID);
				if(billTos != null)
				{
					Set<String> billToSet=billTos.keySet();
					//check if billto alreaady exist get the shipt to and add the ship to in it
					if(billToSet != null && billToSet.contains(billToCustomerID))
					{
						Map<String,String>  shipTos =billTos.get(billToCustomerID);
						shipTos.put(shipToCustomerID, extnShipFromBranch);
					}
					else
					{
						//create new buillto object if not already
						Map<String,String> shipTos = new HashMap<String,String>();
						shipTos.put(shipToCustomerID, extnShipFromBranch);
						billTos.put(billToCustomerID, shipTos);
						sapIds.put(sapCustomerID, billTos);
					}
				}
				else
				{
					// if there is no billto customer
					billTos=new HashMap<String,Map<String,String>>();
					Map<String,String> shipTos = new HashMap<String,String>();
					shipTos.put(shipToCustomerID, extnShipFromBranch);
					billTos.put(billToCustomerID, shipTos);
					sapIds.put(sapCustomerID, billTos);
				}
					
				
			}
			else
			{
				// if not already sap 
				Map<String,Map<String,String>> billTos=new HashMap<String,Map<String,String>>();
				Map<String,String> shipTos = new HashMap<String,String>();
				shipTos.put(shipToCustomerID, extnShipFromBranch);
				billTos.put(billToCustomerID, shipTos);
				sapIds.put(sapCustomerID, billTos);
			}
		}
		Set<String> msapSet=new HashSet<String>();
		String msapCustomerId=(String)wcContext.getSCUIContext().getRequest().getSession().getAttribute(XPEDXWCUtils.LOGGED_IN_CUSTOMER_ID);
		XPEDXShipToCustomer shipToCustomer = (XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		String environmentCode="";
		if(shipToCustomer != null)
			environmentCode=shipToCustomer.getExtnEnvironmentCode();
		// validate the all ship to's  and add in map for all customers .
		for(String sapId:sapIds.keySet())
		{
			Map<String,Map<String,String>> billtos=sapIds.get(sapId);
			
			Set<String> sapSet=new HashSet<String>();
			for(String billTo :billtos.keySet())
			{
				
				Map<String,String> shipTos=billtos.get(billTo);
				Set<String> shipFromBranchs=new HashSet<String>(shipTos.values());
				sapSet.addAll(shipFromBranchs);
				
					for(String shipTo:shipTos.keySet())
					{
						divisionMap.put(shipTo, shipTos.get(shipTo)+"_"+environmentCode);
					}
					if(shipFromBranchs.size() >1)
					{
						divisionMap.put(billTo, "");
					}
					else if(shipFromBranchs.size() == 1)
					{
						for(String shipFromBranch :shipFromBranchs )
						{
							divisionMap.put(billTo, shipFromBranch+"_"+environmentCode);
						}
					}
				
			}
			if(sapSet.size() ==1)
			{
				for(String shipFromBranch :sapSet )
				{
					divisionMap.put(sapId, shipFromBranch+"_"+environmentCode);
				}
			}
			else
			{
				divisionMap.put(sapId, "");
			}
			msapSet.addAll(sapSet);
		}
		if(msapSet.size() ==1)
		{
			for(String shipFromBranch :msapSet )
			{
				divisionMap.put(msapCustomerId, shipFromBranch+"_"+environmentCode);
			}
		}
		else
		{
			divisionMap.put(msapCustomerId, "");
		}
		return divisionMap;
	}
	
	private Element createMyItemsList(String mashupName)
		throws CannotBuildInputException, XMLExceptionWrapper
	{
		Set<String> mashupId=new HashSet<String>();
		mashupId.add(mashupName);
	    Map<String,Element> mashupInputs = prepareMashupInputs(mashupId);
	    Element mashupInput=mashupInputs.get(mashupName);
		if(getSharePermissionLevel() != null && getSharePermissionLevel().trim().length() >0)
		{
			Element milShareListElem=(Element)mashupInput.getElementsByTagName("XPEDXMyItemsListShareList").item(0);
			if(milShareListElem == null)
				milShareListElem=SCXmlUtil.createChild(mashupInput, "XPEDXMyItemsListShareList");
			 Element milShareElem=SCXmlUtil.createChild(milShareListElem, "XPEDXMyItemsListShare");
			    
		    XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		    if(shipToCustomer != null)
		    	milShareElem.setAttribute("DivisionID",shipToCustomer.getExtnShipFromBranch()+"_"+shipToCustomer.getExtnEnvironmentCode());
		}
		return invokeMashups(mashupInputs).get(mashupName);
	}
	@SuppressWarnings("unchecked")
	@Override
	public String execute() {
		try {
			boolean itemsAdded = false;
			Map<String, Element> out;
			//added for Jira 4158
			Map<String,String> divisionMap= getDivisionMap() ;
			//added for jira 4221
			loggedInUserID = wcContext.getLoggedInUserId();			
			String isSalesRep = (String) getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP");
			if(isSalesRep!=null && isSalesRep.equalsIgnoreCase("true"))
			{
				loggedInUserName = (String)getWCContext().getSCUIContext().getSession().getAttribute("loggedInUserName");
			}
			else
			{
				loggedInUserName = wcContext.getLoggedInUserName();
			}
			//end of jira 4221
			newList = false;
			//Check if this is a new list
			if(LOG.isDebugEnabled()){
			LOG.debug("Check 1: getListKey = " + getListKey());
			}
			if (getListKey().trim().equals("new")&& !fromItemDetail){
				newList = true;
				setName(getListName());
				setDesc(getListDesc());
				setSharePrivate(getSharePermissionLevel());
				setShareAdminOnly(shareAdminOnly); //JIRA 3377
				
				/*Updated for Jira 4134*/
				//String isSalesRep = (String) getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP");
				Element res1;
				if(isSalesRep!=null && isSalesRep.equalsIgnoreCase("true")){
					salesreploggedInUserName = (String)getWCContext().getSCUIContext().getSession().getAttribute("loggedInUserName");
					//Commented for Jira 4158
					 //res1=prepareAndInvokeMashup("XPEDXMyItemsListCreateForSalesRep");
					res1=createMyItemsList("XPEDXMyItemsListCreateForSalesRep");
				}else{
				    //Commented for Jira 4158
					 //res1 = prepareAndInvokeMashup("XPEDXMyItemsListCreate");
					res1=createMyItemsList("XPEDXMyItemsListCreate");
				}				
				setListKey(res1.getAttribute("MyItemsListKey")) ;
				if(LOG.isDebugEnabled()){
				LOG.debug("Check 1 - Done: getListKey = " + getListKey());
				}
			}
			//Added for Jira 3920 - Merging CreateNew list and add Item in list call for item detail page
			else if(getListKey().trim().contains("new") && fromItemDetail){
				
				newList = true;
				setName(getListName());
				setDesc(getListDesc());
				setSharePrivate(getSharePermissionLevel());
				setShareAdminOnly(shareAdminOnly); //JIRA 3377
				setItemOrders("1");
				YFCDate modifiedYFCDate = new YFCDate();
				modifiedDate = modifiedYFCDate.getString();
				/*Updated for Jira 4134*/
				Element res1;
				//String isSalesRep = (String) getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP");
				if(isSalesRep!=null && isSalesRep.equalsIgnoreCase("true")){
					salesreploggedInUserName = (String)getWCContext().getSCUIContext().getSession().getAttribute("loggedInUserName");
					//res1=prepareAndInvokeMashup("XPEDXMyItemsListCreateAndAddItemForSalesRep");
					res1=createMyItemsList("XPEDXMyItemsListCreateAndAddItemForSalesRep");
				}else{
					//res1 = prepareAndInvokeMashup("XPEDXMyItemsListCreateAndAddItem");
					res1=createMyItemsList("XPEDXMyItemsListCreateAndAddItem");
				}
				//end of 4134
				setListKey(res1.getAttribute("MyItemsListKey")) ;
				if(LOG.isDebugEnabled()){
				LOG.debug("Check 1 - Done: getListKey = " + getListKey());
				}
			}//end of Jira 3920
			/* JIRA-3745  WC - MIL - Lists of Lists List Count is not Accurate  */
			
			 Map<String ,Integer> customerIDCountMap=new HashMap<String ,Integer>();
			 if(customerIds != null && customerIds.length>0)
			 {
				 for(int i=0;i<countCustomer.size();i++)
				{
					String str=countCustomer.get(i);
					String customerIdCount[]=str.split("\\|");
					if(customerIdCount != null)
					{				
						String totalChildCuntStr= customerIdCount[1];
						if(totalChildCuntStr !=null && !"".equals(totalChildCuntStr.trim()))
							customerIDCountMap.put(customerIdCount[0],Integer.valueOf(totalChildCuntStr));
						else
							customerIDCountMap.put(customerIdCount[0],0);
							
						
					}	
					}
				
				Map<String,ArrayList<String>> parentCustmerMap=getChildCustomers();
				ArrayList<String> customerIdList=new ArrayList<String>();
				List<String> arrayList=Arrays.asList(customerIds);
				for(int i=0;i<customerIds.length;i++)
				{ 
					ArrayList<String> selectedList=parentCustmerMap.get(customerIds[i]);
					if(selectedList != null && parentCustmerMap.containsKey(customerIds[i]))
					{
						
						for(int j=0;j<selectedList.size();j++)
						{
							if(arrayList.contains(selectedList.get(j)))
							{
								customerIdList.add(selectedList.get(j));
							}
						}
					}
				}
				int actualLength=customerIds.length-new HashSet(customerIdList).size();
				String _customerIds[]=new String[actualLength];
				int idx=0;
				for(int i=0;i<customerIds.length;i++)
				{
					if(!customerIdList.contains(customerIds[i]))
					{
						_customerIds[idx]=customerIds[i];
						idx += 1;
					}
						
				}
				customerIds=_customerIds;
			 }
			//1 - Erase all the list
			Element result = null;
			if(LOG.isDebugEnabled()){
			LOG.debug("Check 2: Deleting the old data");
			}
			if (!newList){
				if(getSharePermissionLevel() != null && getSharePermissionLevel().trim().length() >0)
				{
					setSharePrivate(getSharePermissionLevel());
					result=createMyItemsList(MASHUPID_DELETE_SHARE_LIST_ITEMS);
					customerIds=new String[0];
				}
				else if(customerIds != null && customerIds.length>0)
				{
					setSharePrivate(" ");
					result = prepareAndInvokeMashup(MASHUPID_DELETE_SHARE_LIST_ITEMS);
				}
				//result = prepareAndInvokeMashup(MASHUPID_DELETE_SHARE_LIST_ITEMS);
				if(LOG.isDebugEnabled()){
				LOG.debug("Check 2: Deleting the old data - Done");
				}
			}
			
			if (result != null || newList){
			//1.5 Sync for deletes
				if(LOG.isDebugEnabled()){
				LOG.debug("Check 3: Sync for delete");
				}
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
					if(LOG.isDebugEnabled()){
					LOG.debug("Check 3: Sync for delete - Done");
					}
				} catch (Exception e) {
					LOG.error(e.getStackTrace());
				}
			try {
				//1.6 Sync for additions
				if(LOG.isDebugEnabled()){
				LOG.debug("Check 4: Sync for additions");
				}
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
				if(LOG.isDebugEnabled()){
				LOG.debug("Check 4: Sync for additions - Done");
				}
			} catch (Exception e) {
				LOG.error(e.toString());
			}
			
			//2 - Insert all the data into the system
				
				//If this list is not a private list then clean up the selection
				if (getSharePermissionLevel().trim().length() > 0){
					finalSSLNames = ArrayUtils.EMPTY_STRING_ARRAY;
					setShareAdminOnly("N");
					if(LOG.isDebugEnabled()){
					LOG.debug("Check 5: Cleaning up selection. Shared permission level: " + getSharePermissionLevel());
					}
				}
				
				for (int i = 0; i < finalSSLNames.length; i++) {
					setCustomerId(finalSSLNames[i]);
					setCustomerPath(finalSSLValues[i]);
					//Added for Jira 4158
					setCustomerDiv(divisionMap.get(finalSSLNames[i]));
					
					if (getCustomerId().trim().length()== 0){ continue; }
					if(LOG.isDebugEnabled()){
					LOG.debug("Check 6: Saving data: CustomerID = " + getCustomerId() + ", Customer path = " + getCustomerPath());
					}
					//out = prepareAndInvokeMashups();
					/*Updated for Jira 4134*/
					Element outElem;
					//String isSalesRep = (String) getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP");
					if(isSalesRep!=null && isSalesRep.equalsIgnoreCase("true")){
						salesreploggedInUserName = (String)getWCContext().getSCUIContext().getSession().getAttribute("loggedInUserName");
						outElem=prepareAndInvokeMashup("XPEDXMyItemsDetailsChangeShareListForSalesPro");
					}else{
						outElem = prepareAndInvokeMashup("XPEDXMyItemsDetailsChangeShareList");
					}				
					
					if(LOG.isDebugEnabled()){
					LOG.debug("Check 6: Saving data: CustomerID = " + getCustomerId() + ", Customer path = " + getCustomerPath() + " - Done!");
					}
					itemsAdded = true;
					/*if (out.get("XPEDXMyItemsDetailsChangeShareList") != null){
						outDoc = (Document)out.get("XPEDXMyItemsDetailsChangeShareList").getOwnerDocument();
					}*/
					//for jira 4221 for updating the lastModifiedby in change sharelist
					if (outElem != null){
						outDoc = outElem.getOwnerDocument();
					} else {
						LOG.error("No document as result");
						return ERROR;
					}
					//end of jira 4221
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
				if(LOG.isDebugEnabled()){
				LOG.debug("Check 8: Copy the items from the other list if specify");
				LOG.debug("Check 8: getClFromListId  = '" + getClFromListId() + "'");
				}
				if (getClFromListId().trim().length() > 0){
					//1 - Get all the items from this list
					Element elAllItems = prepareAndInvokeMashup("XPEDXMyItemsDetails");
					ArrayList<Element> alAllItems = getXMLUtils().getElements(elAllItems, "XPEDXMyItemsItems");
					
					Document myitemsList = SCXmlUtil.createDocument("XPEDXMyItemsList");
					Element myItemsItemsList = myitemsList.createElement("XPEDXMyItemsItemsList");
					myitemsList.getDocumentElement().appendChild(myItemsItemsList);
					//Added setItemCount() for Jira 3848
					setItemCount(String.valueOf(alAllItems.size()));
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
						myItemItems.setAttribute("ModifyUserName", clUserName);
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
					myitemsList.getDocumentElement().setAttribute("ModifyUserName",loggedInUserName);
					myitemsList.getDocumentElement().setAttribute("Modifyuserid",loggedInUserID);
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
		if(LOG.isDebugEnabled()){
		LOG.debug("Check 9: All done!");
		}
		/*Web Trends tag start */
		request.getSession().setAttribute("metatagName","DCSext.w_x_sharelist");
		request.getSession().setAttribute("metatagValue","1");
		/*Web Trends tag end */
		if(clAjax && newList)
			return "copy";
		else if(newList && fromItemDetail){
			return SUCCESS;
		}
		return SUCCESS;
	}
	

	/* JIRA-3745  WC - MIL - Lists of Lists List Count is not Accurate  */
	private Map<String,ArrayList<String>> getChildCustomers()
	{
		Map<String,ArrayList<String>> customersMap=new HashMap<String,ArrayList<String>>();
		 if(customerIds != null && customerIds.length>0)
		 {
		for(int i=0;i<customerIds.length;i++)
		{
			String []_customerIds=customerPaths[i].split("\\|");
			if(_customerIds !=null)
			{
				int length=_customerIds.length;
				int parentLength=length-2;
				int parentLength1=length-3;
				int parentLength2=length-4;
				if(parentLength >=0)
				{
					String parentCustomer=_customerIds[parentLength];
					
					
					ArrayList<String>  customers=customersMap.get(parentCustomer);
					if(customers == null)
						customers=new ArrayList<String>();
					customers.add(customerIds[i]);
					customersMap.put(parentCustomer, customers);
					if(parentLength1 >=0)
					{
						String parentCustomer1=_customerIds[parentLength1];
						ArrayList<String>  customers1=customersMap.get(parentCustomer1);
						if(customers1 == null)
							customers1=new ArrayList<String>();
						customers1.add(customerIds[i]);
						customers1.add(parentCustomer);
						customersMap.put(parentCustomer1, customers1);
						if(parentLength2 >=0)
						{
							String parentCustomer2=_customerIds[parentLength2];
							ArrayList<String>  customers2=customersMap.get(parentCustomer2);
							if(customers2 == null)
								customers2=new ArrayList<String>();
							customers2.add(customerIds[i]);
							customers2.add(parentCustomer1);
							customers1.add(parentCustomer);
							customersMap.put(parentCustomer2, customers2);
						}

					}
				}
			}
		}}
		return customersMap;
	}

	/**
	 * @return the countCustomer
	 */
	public ArrayList<String> getCountCustomer() {
		return countCustomer;
	}

	/**
	 * @param countCustomer the countCustomer to set
	 */
	public void setCountCustomer(ArrayList<String> countCustomer) {
		this.countCustomer = countCustomer;
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
	public String getLoggedInUserName() {
		return loggedInUserName;
	}

	public void setLoggedInUserName(String loggedInUserName) {
		this.loggedInUserName = loggedInUserName;
	}	
	
	
}
