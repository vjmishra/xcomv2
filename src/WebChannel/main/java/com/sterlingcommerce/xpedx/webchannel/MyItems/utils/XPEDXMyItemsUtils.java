package com.sterlingcommerce.xpedx.webchannel.MyItems.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.ui.web.platform.utils.SCUIPlatformUtils;
import com.sterlingcommerce.webchannel.compat.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.context.WCContext;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;

public class XPEDXMyItemsUtils {

	private final static Logger log = Logger.getLogger(XPEDXMyItemsUtils.class);
	
	public static ArrayList<String> getCustomerPath(IWCContext context) throws CannotBuildInputException{
		return getCustomerPath(context.getSCUIContext(), context.getCustomerId(), context.getStorefrontId());
	}
	
    public static String formatEscapeCharacters(String str){
 	    str = str.replaceAll("\r\n", " ");
 	    str = str.replaceAll("\n", " ");
 	    str = str.replaceAll("\r", " ");
 	    str = str.replaceAll("\"", "%22");// replacing double quot unicode
        str = str.replaceAll("\'", "%27");// replacing single quot unicode
       return str;
    }
    
    public static String formatEscapeCharactersHtml(String str){
 	    str = str.replaceAll("\r\n", " ");
 	    str = str.replaceAll("\n", " ");
 	    str = str.replaceAll("\r", " ");
 	    str = str.replaceAll("\"", "&quot;");// replacing double quot unicode
   //     str = str.replaceAll("\'", "%27");// replacing single quot unicode
       return str;
    }

	
	public static String encodeStringForCSV(String data){
		String res = null;
		try {
			res = StringUtils.replace(data, "\"", "\"\"");
			res = StringUtils.replace(res, "'", "");
		} catch (Exception e) {
			log.error(e.toString());
		}
		return res;
	}
	
	public static String getCustomerPartNumber(String itemId){
		String res = null;
		try {
			Map itemAttr = new HashMap();
			itemAttr.put("LegacyItemNumber", itemId);
			Element itemCustXrefEle = XPEDXWCUtils.getItemCustXrefInfo(itemAttr);
			if(itemCustXrefEle != null){
				Element custXrefEle = XMLUtilities.getElement(itemCustXrefEle,"XPXItemcustXref");
				res 				= SCXmlUtil.getAttribute(custXrefEle, "CustomerItemNumber");
			}
			
			if (res == null){
				res = "";
			}
		} catch (Exception e) {
			res = "";
			log.error(e.toString());
		}
		
		return res;
	}
	
	public static String getItemIdByCustomerPartNumber(String itemId){
		String res = null;
		try {
			Map itemAttr = new HashMap();
			itemAttr.put("CustomerItemNumber", itemId);
			Element itemCustXrefEle = XPEDXWCUtils.getItemCustXrefInfo(itemAttr);
			if(itemCustXrefEle != null){
				Element custXrefEle = XMLUtilities.getElement(itemCustXrefEle,"XPXItemcustXref");
				res 				= SCXmlUtil.getAttribute(custXrefEle, "LegacyItemNumber");
			}
			
			if (res == null){
				res = "";
			}
		} catch (Exception e) {
			res = "";
			log.error(e.toString());
		}
		
		return res;
	}

	public static String getCurrentCustomerId(IWCContext cntx){
		String res = null;
		try {
			res = XPEDXWCUtils.getLoggedInCustomerFromSession(cntx);
			if (res == null){
				res = cntx.getCustomerId();
			}
		} catch (Exception e) {
			log.error(e.toString());
		}
		
		return res;
	}

	public static boolean isCurrentUserAdmin(WCContext cntx){
		boolean res = false;
		try {
			if	(ResourceAccessAuthorizer.getInstance().isAuthorized(
					"/swc/profile/ManageUserList", cntx)
			) {
	              res = true;
	        } else {
	              res = false;
	        }
		} catch (Exception e) {
			log.error(e.toString());
		}
		
		return res;
	}
	
	public static List<String> getMyItemList_disabled(String customerContactId) {
		
		ISCUITransactionContext scuiTransactionContext 	= null;
		List<String> listMIL 							= new ArrayList<String>();
		SCUIContext wSCUIContext 						= null;
		
		try {
			YFCDocument inputDocument = YFCDocument.createDocument("CustomerAssignment");
			YFCElement documentElement = inputDocument.getDocumentElement();

			IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
			wSCUIContext = context.getSCUIContext();

			if (customerContactId != null && customerContactId.trim().length() > 0) {
				documentElement.setAttribute("UserId", customerContactId);
			} else {
				documentElement.setAttribute("UserId", context.getLoggedInUserId());
			}
			
			documentElement.setAttribute("OrganizationCode", context.getBuyerOrgCode());
			
			YFCDocument template = YFCDocument.getDocumentFor("" +
					"<XPEDXMyItemsList SharePrivate=\" \">" +
					"	<XPEDXMyItemsListShareList>" +
					"		<XPEDXMyItemsListShare>" +
					"			<ComplexQuery>" +
					"				<Or>" +
					"					<Exp Name=\"\" Value=\"\" />" +
					"				</Or>" +
					"			</ComplexQuery>" +
					"		</XPEDXMyItemsListShare>" +
					"	</XPEDXMyItemsListShareList>" +
					"</XPEDXMyItemsList>" +
			"");
			
			scuiTransactionContext = wSCUIContext
					.getTransactionContext(true);					
			YFCElement yfcElement = SCUIPlatformUtils.invokeXAPI(
					"getCustomerAssignmentList", inputDocument
							.getDocumentElement(), template
							.getDocumentElement(), wSCUIContext);
			YFCIterable<YFCElement> iteartor = yfcElement.getChildren();
			YFCElement wElement = null;
			while (iteartor.hasNext()) {
				wElement = iteartor.next();
				wElement = wElement.getFirstChildElement();
				String customer = wElement.getAttribute("CustomerID");
				if (!listMIL.contains(customer)) {
					listMIL.add(customer);
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			scuiTransactionContext.rollback();
		} finally {
			if (scuiTransactionContext != null) {
				SCUITransactionContextHelper.releaseTransactionContext(
						scuiTransactionContext, wSCUIContext);
				scuiTransactionContext=null;
			}
		}
		return listMIL;
	}
	
	public static ArrayList<String> getMyItemList(SCUIContext context,String sharePrivate,ArrayList<String> sharedListOrNames, ArrayList<String> sharedListOrValues) throws CannotBuildInputException{
    	ArrayList<String> parentCustomerList = new ArrayList<String>();
    	try {
    		
    		Map<String, String> valueMap = new HashMap<String, String> ();
            valueMap.put("/XPEDXMyItemsList/@SharePrivate", sharePrivate);
            //valueMap.put("/XPEDXMyItemsList/XPEDXMyItemsListShareList/XPEDXMyItemsListShare/ComplexQuery/Or/Exp[]/@Name", 	sharedListOrNames);
            //valueMap.put("/XPEDXMyItemsList/XPEDXMyItemsListShareList/XPEDXMyItemsListShare/ComplexQuery/Or/Exp[]/@Value",	sharedListOrValues);
    		
            Element input = WCMashupHelper.getMashupInput("XPEDXMyItemsList", valueMap, context);
			
    		Object obj = WCMashupHelper.invokeMashup("XPEDXMyItemsList", input, context);
			
    		Document outputDoc =((Element)obj).getOwnerDocument();
			
    		String xml = SCXmlUtil.getString(outputDoc);
    		
    		NodeList nlCustomer = outputDoc.getElementsByTagName("Customer");
    		Element customer = null;
    		int length = nlCustomer.getLength();
    		for(int i= length-1; i>=0 ;i--){
    			customer = (Element) nlCustomer.item(i);
    			String custID = SCXmlUtil.getAttribute(customer, "CustomerID");
    			parentCustomerList.add(custID);
    		}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
			
    	return parentCustomerList;
	}
	
	public static boolean validateItemEntitlement(String itemId, IWCContext cntx) {
		boolean res = false;
		try {
			//Populate all the fields
			Map<String, String> valueMap = new HashMap<String, String>();
			try {
				if (itemId==null || itemId.trim().equals("")){
					itemId = "INVALID_ITEM";
				}
			} catch (Exception e) {
				itemId = "INVALID_ITEM";
			}
			//Avoid calling the mashup if mandatory parameters are missing
			if(itemId.equals("INVALID_ITEM") || cntx.getStorefrontId()==null || cntx.getCustomerId()==null)
				return res;
			
			valueMap.put("/Item/@ItemID", 					itemId);
			valueMap.put("/Item/@CallingOrganizationCode", 	cntx.getStorefrontId());
			valueMap.put("/Item/CustomerInformation/@CustomerID", 	cntx.getCustomerId());

			Element input = WCMashupHelper.getMashupInput("XPEDXMyItemsListValidateItem", valueMap, cntx.getSCUIContext());
			//String inputXml = SCXmlUtil.getString(input);
			//log.debug("Input XML: " + inputXml);
			Object obj = WCMashupHelper.invokeMashup("XPEDXMyItemsListValidateItem", input, cntx.getSCUIContext());
			Element tmpRes  = ((Element) obj).getOwnerDocument().getDocumentElement();
			
			Element itemEle = SCXmlUtils.getInstance().getChildElement(tmpRes, "Item");
			
			if (itemEle!=null && itemId.equals(itemEle.getAttribute("ItemID"))){
				res = true;
			}
			
			
		} catch (Exception e) {
			res = false;
			log.error(e.toString());
		}
		
		return res;
	}
	
	public static Document getItemDetails(String itemID, String custID, String callingOrgCode, IWCContext wcContext) throws Exception {
		Document outputDoc = null;
		
		if (null == itemID) {
			log.debug("getItemDetails: Item ID is a required field. Returning a empty document");
			return outputDoc;
		}

		Map<String, String> valueMap = new HashMap<String, String>();
		try {
			if (itemID.trim().equals("")){
				itemID = "INVALID_ITEM";
			}
		} catch (Exception e) {
			itemID = "INVALID_ITEM";
		}
		
		if(itemID.equals("INVALID_ITEM"))
			return outputDoc;
		
		valueMap.put("/Item/@CallingOrganizationCode", callingOrgCode);
		valueMap.put("/Item/@ItemID", itemID);
		valueMap.put("/Item/CustomerInformation/@CustomerID", custID);

		Element input = WCMashupHelper.getMashupInput("xpedxItemDetailsMIL", valueMap, wcContext.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		log.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxItemDetailsMIL", input, wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}
		return outputDoc;
	}
	
	public static Document getItemDetailsForRelatedItems(String itemID, String custID, String callingOrgCode, IWCContext wcContext) throws Exception {
		Document outputDoc = null;
		
		if (null == itemID) {
			log.debug("getItemDetails: Item ID is a required field. Returning a empty document");
			return outputDoc;
		}

		Map<String, String> valueMap = new HashMap<String, String>();
		try {
			if (itemID.trim().equals("")){
				itemID = "INVALID_ITEM";
			}
		} catch (Exception e) {
			itemID = "INVALID_ITEM";
		}
		
		if(itemID.equals("INVALID_ITEM"))
			return outputDoc;
		
		valueMap.put("/Item/@CallingOrganizationCode", callingOrgCode);
		valueMap.put("/Item/@ItemID", itemID);
		valueMap.put("/Item/CustomerInformation/@CustomerID", custID);

		Element input = WCMashupHelper.getMashupInput("xpedxItemDetailsMILRelatedItems", valueMap, wcContext.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		log.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxItemDetailsMILRelatedItems", input, wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}
		return outputDoc;
	}
	

	@SuppressWarnings("unchecked")
	/*
	 * Returns a list of related items including the custom associated items
	 *  - HashMap
	 *  	"All" 			= All related items together
	 *  	"CrossSell"		= Only cross sell items
	 *      "UpSell"		= Only up sell items
	 *      "Complementary"	= Only complementary items
	 *      "Upgrade"		= Only upgrade items
	 *      "Replacement"	= Only replacement items
	 */
	
	public static final String GRI_KEY_ALL 				= "All";  
	public static final String GRI_KEY_CROSS_SELL 		= "CrossSell";
	public static final String GRI_KEY_UP_SELL 			= "UpSell";
	public static final String GRI_KEY_COMPLEMENTARY 	= "UpSell";
	public static final String GRI_KEY_UPGRADE 			= "Upgrade";
	public static final String GRI_KEY_REPLACEMENT 		= "Replacement";
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, ArrayList<Object>> getRelatedItems(String itemId, boolean includetAssocItems, String customerId, String orgId, IWCContext wcContext){
		HashMap<String, ArrayList<Object>> res = new HashMap<String, ArrayList<Object>>();
		ArrayList relatedItems = new ArrayList();
		
		try {
			Document docItemDetails = XPEDXMyItemsUtils.getItemDetailsForRelatedItems(itemId, customerId, orgId, wcContext);
			
			SCXmlUtil.getString(docItemDetails);
			
			if (docItemDetails!=null && docItemDetails.getDocumentElement() != null) {
				Document itemDoc 					= docItemDetails;
				Element associationTypeListElem 	= XMLUtilities.getElement(itemDoc, "ItemList/Item/AssociationTypeList");
				Element xpxAssociationTypeListElem 	= XMLUtilities.getElement(itemDoc, "ItemList/Item/AssociationTypeList");
				
				if (associationTypeListElem != null) {
					List<Element> crossSellElements = XMLUtilities.getElements(associationTypeListElem, "AssociationType[@Type='CrossSell']");
					List<Element> upSellElements 	= XMLUtilities.getElements(associationTypeListElem, "AssociationType[@Type='UpSell']");
					ArrayList crossSellItems 		= new ArrayList();
					ArrayList upSellItems 			= new ArrayList();
					
					
					for (int i = 0; i < crossSellElements.size(); i++) {
						Element associationTypeElem = crossSellElements.get(i);
						Element associationListElem = XMLUtilities.getElement(associationTypeElem, "AssociationList");
						List associationList 		= XMLUtilities.getChildElements(associationListElem, "Association");
						
						if (associationList != null && !associationList.isEmpty()) {
							Iterator associationIter = associationList.iterator();
							while (associationIter.hasNext()) {
								Element association = (Element) associationIter.next();
								Element itemEl 		= XMLUtilities.getElement(association, "Item");
								
								SCXmlUtil.getString(itemEl);
								crossSellItems.add(itemEl);
							}
						}
					}
					
					for (int i = 0; i < upSellElements.size(); i++) {
						Element associationTypeElem = upSellElements.get(i);
						Element associationListElem = XMLUtilities.getElement(associationTypeElem, "AssociationList");
						List associationList = XMLUtilities.getChildElements(associationListElem, "Association");
						
						if (associationList != null && !associationList.isEmpty()) {
							Iterator associationIter = associationList.iterator();
							while (associationIter.hasNext()) {
								Element association = (Element) associationIter.next();
								Element itemEl = XMLUtilities.getElement(association, "Item");
								
								SCXmlUtil.getString(itemEl);
								upSellItems.add(itemEl);
							}
						}
					}
					
					relatedItems.addAll(crossSellItems);
					relatedItems.addAll(upSellItems);
					
					res.put(GRI_KEY_CROSS_SELL, crossSellItems);
					res.put(GRI_KEY_UP_SELL, 	upSellItems);
				}

				if (includetAssocItems){
					Element result = null;
					
					Document outputDoc = XPEDXOrderUtils.getXPEDXItemAssociation(customerId, XPEDXWCUtils.getCustomerShipFromDivision(customerId, orgId), itemId, wcContext);

					NodeList nlItemAssociationsList = outputDoc.getElementsByTagName("XPXItemAssociations");
					Element itemAssociation = null;
					int length = nlItemAssociationsList.getLength();
					ArrayList upgradeAssociatedItems = new ArrayList();
					ArrayList complimentAssociatedItems = new ArrayList();
					ArrayList replacementAssociatedItems = new ArrayList();
					
					for (int i = 0; i < length; i++) {
						itemAssociation = (Element) nlItemAssociationsList.item(i);
						String associatedItemID = SCXmlUtil.getAttribute(itemAssociation, "AssociatedItemID");
						String associationType = SCXmlUtil.getAttribute(itemAssociation, "AssociationType");
						// get the Item details for each item and put it in the
						// appropriate MAP according to the association type
						Document relItemDoc = XPEDXOrderUtils.getItemDetails(associatedItemID, customerId, orgId, wcContext);
						Element relItemoutputEl = relItemDoc.getDocumentElement();
						Element itemEl = XMLUtilities.getElement(relItemoutputEl,"Item");
						String itemDetailXML = SCXmlUtil.getString(itemEl);
	
						if (associationType.equalsIgnoreCase("C")) {
							complimentAssociatedItems.add(itemEl);
						}
						if (associationType.equalsIgnoreCase("U")) {
							upgradeAssociatedItems.add(itemEl);
						}
						if (associationType.equalsIgnoreCase("R")) {
							replacementAssociatedItems.add(itemEl);
						}
						SCXmlUtil.getString(itemEl);
					}
	
					relatedItems.addAll(complimentAssociatedItems);
					relatedItems.addAll(upgradeAssociatedItems);
					relatedItems.addAll(replacementAssociatedItems);
					
					res.put(GRI_KEY_COMPLEMENTARY, 	complimentAssociatedItems);
					res.put(GRI_KEY_UPGRADE, 		upgradeAssociatedItems);
					res.put(GRI_KEY_REPLACEMENT, 	replacementAssociatedItems);
				}
				
				res.put(GRI_KEY_ALL, 		relatedItems);
			}
			
		} catch (Exception e) {
			log.error(e);
		}
		
		return res;
	}
	
	public static HashMap<String, ArrayList<Object>> getRelatedItems(String itemId, String customerId, String orgId, IWCContext wcContext){
		return getRelatedItems(itemId, true, customerId, orgId, wcContext);
	}
	
	public static ArrayList<String> getCustomerPath(SCUIContext context,String customerId,String orgCode) throws CannotBuildInputException{
    	ArrayList<String> parentCustomerList = new ArrayList<String>();
    	try {
    		
    		Map<String, String> valueMap = new HashMap<String, String> ();
            valueMap.put("/Customer/@OrganizationCode", orgCode);
            valueMap.put("/Customer/@CustomerID", customerId);
    		
    		Element input = WCMashupHelper.getMashupInput("XPEDXParentCustomerList", valueMap, context);
			
    		Object obj = WCMashupHelper.invokeMashup("XPEDXParentCustomerList", input, context);
			
    		Document outputDoc =((Element)obj).getOwnerDocument();
			
    		String xml = SCXmlUtil.getString(outputDoc);
    		
    		NodeList nlCustomer = outputDoc.getElementsByTagName("Customer");
    		Element customer = null;
    		int length = nlCustomer.getLength();
    		for(int i= length-1; i>=0 ;i--){
    			customer = (Element) nlCustomer.item(i);
    			String custID = SCXmlUtil.getAttribute(customer, "CustomerID");
    			parentCustomerList.add(custID);
    		}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
			
    	return parentCustomerList;
	}
	
	public static String getCustomerPathAsHRY(IWCContext context) throws Exception{
		return getCustomerPathAsHRY(context.getSCUIContext(), getCurrentCustomerId(context), context.getStorefrontId());
	}
	
	public static String getCustomerPathAsHRY(SCUIContext context,String customerId,String orgCode) throws Exception{
		StringBuilder res = new StringBuilder();
		
		ArrayList<String> hry = getCustomerPath(context, customerId, orgCode);
		
		for (int i = 0; i < hry.size(); i++) {
			String path = hry.get(i);
			if (i==0){
				res.append(path);
			} else {
				res.append("|").append(path);
			}
		}
		
		return res.toString();
	}

	
	public static ArrayList<String> getEntitledItem(IWCContext wcContext,ArrayList items) {
		Document outputDoc = null;
		ArrayList<String> outList=new ArrayList<String>();
		try {
			
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/Item/@CallingOrganizationCode", wcContext.getCustomerMstrOrg());
			valueMap.put("/Item/CustomerInformation/@CustomerID", wcContext.getCustomerId());
			
			Element input = WCMashupHelper.getMashupInput("XPEDXMyAllItemsDetails", valueMap,
					wcContext.getSCUIContext());
			Document inputDoc = input.getOwnerDocument();
			NodeList inputNodeList = input.getElementsByTagName("Or");
			Element inputNodeListElemt = (Element) inputNodeList.item(0);
			for (Object item :items) {
				Document expDoc = YFCDocument.createDocument("Exp").getDocument();
				Element expElement = expDoc.getDocumentElement();
				if(item instanceof String){
					expElement.setAttribute("Name", "ItemID");
					expElement.setAttribute("Value", item.toString());
				}
				else{
					Element itemEle=(Element)item;
					expElement.setAttribute("Name", "ItemID");
					expElement.setAttribute("Value", itemEle.getAttribute("ItemId"));
					
				}
				inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
			}
			log.debug("Input XML: " + SCXmlUtil.getString(input));
			Object obj = WCMashupHelper.invokeMashup(
					"XPEDXMyAllItemsDetails", input, wcContext
							.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			List<Element> itemList=com.sterlingcommerce.framework.utils.SCXmlUtils.getElements(outputDoc.getDocumentElement(), "/Item");
			for(Element elem : itemList){
				outList.add(elem.getAttribute("ItemID"));
			}
			if (null != outputDoc) {
				log.debug("Output XML getXpedxEntitledItemDetails: " + SCXmlUtil.getString((Element) obj));
			}
		} catch (Exception e) {
			
			log.error(e.getStackTrace());
		}
		
		return outList;
	}
	
	public static Document getEntitledItemsDocument(IWCContext wcContext,ArrayList items) {
		Document outputDoc = null;
		ArrayList<String> outList=new ArrayList<String>();
		try {
			
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/Item/@CallingOrganizationCode", wcContext.getCustomerMstrOrg());
			valueMap.put("/Item/CustomerInformation/@CustomerID", wcContext.getCustomerId());
			
			Element input = WCMashupHelper.getMashupInput("XPEDXMyAllItemsDetails", valueMap,
					wcContext.getSCUIContext());
			Document inputDoc = input.getOwnerDocument();
			NodeList inputNodeList = input.getElementsByTagName("Or");
			Element inputNodeListElemt = (Element) inputNodeList.item(0);
			for (Object item :items) {
				Document expDoc = YFCDocument.createDocument("Exp").getDocument();
				Element expElement = expDoc.getDocumentElement();
				if(item instanceof String){
					expElement.setAttribute("Name", "ItemID");
					expElement.setAttribute("Value", item.toString());
				}
				else{
					Element itemEle=(Element)item;
					expElement.setAttribute("Name", "ItemID");
					expElement.setAttribute("Value", itemEle.getAttribute("ItemId"));
					
				}
				inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
			}
			log.debug("Input XML: " + SCXmlUtil.getString(input));
			Object obj = WCMashupHelper.invokeMashup(
					"XPEDXMyAllItemsDetails", input, wcContext
							.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			if (null != outputDoc) {
				log.debug("Output XML getXpedxEntitledItemDetails: " + SCXmlUtil.getString((Element) obj));
			}
		} catch (Exception e) {
			
			log.error(e.getStackTrace());
		}
		
		return outputDoc;
	} 
	public static String getReplacedValue(String CustomerField){		
		CustomerField=CustomerField.replaceAll("'","");
		// Blank space removed for JIRA 3693
		return CustomerField.trim(); 
		}
}
