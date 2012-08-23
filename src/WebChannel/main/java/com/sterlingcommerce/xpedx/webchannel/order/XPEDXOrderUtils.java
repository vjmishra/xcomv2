/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.order;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.comergent.appservices.configuredItem.XMLUtils;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.webchannel.utilities.BusinessRuleUtil;
import com.sterlingcommerce.webchannel.utilities.OrderHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;


import com.sterlingcommerce.xpedx.webchannel.common.XpedxSortUOMListByConvFactor;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author Rajendra
 * @Manohar
 * 
 */
public class XPEDXOrderUtils {
	private static final Logger log = Logger.getLogger(XPEDXOrderUtils.class);
	private final static String CREATE_MASHUP_ID = "draftOrderCreate";
	private final static String DEFAULT_CARRIER_SERVICE_RULE = "SWC_DEFAULT_CARRIER_SERVICE_FOR_NEW_DRAFT_ORDERS";
	private final static String DOCUMENT_TYPE = "0001";
	private final static String NEW_CART_NAME = "DEFAULT_CART";
	
	public final static String REPLACEMENT_ITEMS_KEY = "REP";
	public final static String ALTERNATE_ITEMS_KEY = "ALT";
	public final static String COMPLEMENTARY_ITEMS_KEY = "COMP";
	public final static String UPGRADE_ITEMS_KEY = "UPG";
	public final static String CROSS_SELL_ITEMS_KEY = "CROSS-SELL";
	public final static String UP_SELL_ITEMS_KEY = "UP-SELL";
	public final static String ITEM_EXTN_KEY = "ITEM_EXTN";
	public final static String ITEM_LIST_KEY = "ITEM_LIST_KEY";
	public static ArrayList<String> itemList = new ArrayList<String>();
	
	public static Document getXPEDXItemAssociation(String custID,
			String divNumber, String itemID, IWCContext wcContext)
			throws Exception {
		Document outputDoc = null;
		String environmentCode = null;
		XPEDXShipToCustomer customer = (XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		environmentCode = customer.getExtnEnvironmentCode();
		if(environmentCode == null || environmentCode.trim().length()==0) {
			LOG
				.debug("getXPEDXItemAssociation: Environment Code is null field. Returning a null document");
			return outputDoc;
		}			
		if (null == itemID) {
			LOG
					.debug("getXPEDXItemAssociation: Item ID is a required field. Returning a empty document");
			return outputDoc;
		}
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/XPXItemExtn/@CustomerID", custID);
		valueMap.put("/XPXItemExtn/@ItemID", itemID);
		valueMap.put("/XPXItemExtn/@XPXDivision", divNumber);
		valueMap.put("/XPXItemExtn/@EnvironmentID", environmentCode);

		Element input = WCMashupHelper.getMashupInput("xpedxItemAssociation",
				valueMap, wcContext.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		LOG.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxItemAssociation", input,
				wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}

		return outputDoc;
	}

	public static Map<String, String> getOrderMultipleForItems(
			List<String> items) throws YIFClientCreationException,
			YFSException, RemoteException {
		Map<String, String> hashMap = new HashMap<String, String>();
		if (items != null) {
			for (String item : items) {
				if(hashMap.containsKey(item))
					continue;
				String orderMultiple = getOrderMultipleForItem(item);
				if (orderMultiple != null) {
					hashMap.put(item, orderMultiple);
				}
			}
		}
		return hashMap;
	}

	public static String getOrderMultipleForItem(String itemID)
			throws YIFClientCreationException, YFSException, RemoteException {

		//String companyCode = "";
		String orderMultiple = "";
		//String customerDivision = "";
		String shipFromBranch = "";
		String enviCode = null;
		XPEDXShipToCustomer customer = (XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		if (customer != null)
			enviCode = customer.getExtnEnvironmentCode();

		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		ISCUITransactionContext scuiTransactionContext = wSCUIContext
				.getTransactionContext(true);
		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
				.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
		String customerID = context.getCustomerId();
		String storeFrontId = context.getStorefrontId();

		try {
			HashMap<String, String> customerDetails = getCustomerDetails(env,
					customerID, storeFrontId);

			/*companyCode = customerDetails.get("companyCode");
			customerDivision = customerDetails.get("customerDivision");*/
			shipFromBranch = customerDetails.get("shipFromBranch");
			String envCode = (String)context.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.ENVIRONMENT_CODE);
			
			if(itemID!=null && shipFromBranch!=null && enviCode!=null){
				YFCDocument inputDocument = YFCDocument.createDocument("XPXItemExtn");
				   YFCElement inputElement = inputDocument.getDocumentElement();
				   inputElement.setAttribute("ItemID", itemID);
				   inputElement.setAttribute("XPXDivision", shipFromBranch);
				   inputElement.setAttribute("EnvironmentID", enviCode);
				   
				   
				   YIFApi api = YIFClientFactory.getInstance().getApi();
				   
				   Document outputListDocument = api.executeFlow(env, "getXPXItemBranchListService",
				     inputDocument.getDocument());
				      
				   Element outputListElement = outputListDocument.getDocumentElement();
				   if(outputListElement!=null){
					   NodeList xpxItemExtnNL = outputListElement.getElementsByTagName("XPXItemExtn");
					   if(xpxItemExtnNL.getLength() > 0 ){
					    Element xpxItemExtnEle = (Element)xpxItemExtnNL.item(0);
					    if(xpxItemExtnEle!=null)
					    	orderMultiple = xpxItemExtnEle.getAttribute("OrderMultiple");
					   }
				   }
	//			YFCDocument inputDocument = YFCDocument.createDocument("Item");
	//			YFCElement inputElement = inputDocument.getDocumentElement();
	//			inputElement.setAttribute("ItemID", itemID);
	//			inputElement.setAttribute("OrganizationCode", storeFrontId);
	//			env.setApiTemplate("getItemList", SCXmlUtil.createFromString(""
	//					+ "<ItemList><Item><AlternateUOMList>" + "<AlternateUOM />"
	//					+ "</AlternateUOMList><Extn>"
	//					+ "<XPXItemExtnList><XPXItemExtn/>"
	//					+ "</XPXItemExtnList></Extn>" + "</Item></ItemList>"));
	//
	//			YIFApi api = YIFClientFactory.getInstance().getApi();
	//			Document outputListDocument = api.invoke(env, "getItemList",
	//					inputDocument.getDocument());
	//			Element outputListElement = outputListDocument.getDocumentElement();
	//			NodeList itemListNodes = outputListElement.getChildNodes();
	//			int length = itemListNodes.getLength();
	//			for (int i = 0; i < length; i++) {
	//				Node itemNode = itemListNodes.item(i);
	//				NodeList itemNodeChildren = itemNode.getChildNodes();
	//				int length1 = itemNodeChildren.getLength();
	//				for (int j = 0; j < length1; j++) {
	//					Node itemNodeChild = itemNodeChildren.item(j);
	//					if (itemNodeChild != null
	//							&& itemNodeChild.getNodeName().equals("Extn")) {
	//						NodeList extnChildList = itemNodeChild.getChildNodes();
	//						int length2 = extnChildList.getLength();
	//						for (int k = 0; k < length2; k++) {
	//							Node extnChild = extnChildList.item(k);
	//							if (extnChild != null
	//									&& extnChild.getNodeName().equals(
	//											"XPXItemExtnList")) {
	//								orderMultiple = getOrderMultipleValue(
	//										extnChild, companyCode, shipFromBranch);
	//								if (orderMultiple != null
	//										&& orderMultiple.trim().length() > 0) {
	//									break;
	//								}
	//							}
	//						}
	//					}
	//				}
	//			}
				} 
		}//if itemId, shipFromBranch and envCode are not null
		finally {
			scuiTransactionContext.end();
			env.clearApiTemplate("getItemList");
			SCUITransactionContextHelper.releaseTransactionContext(
					scuiTransactionContext, wSCUIContext);
			env = null;
		}
		if (orderMultiple == null || orderMultiple.trim().length() == 0) {
			orderMultiple = "1";
		}
		return orderMultiple;
	}

	/*private static String getOrderMultipleValue(Node extnChild,
			String companyCode, String customerDiv) {
		String orderMultiple = "";
		NodeList XpxItemExtnList = extnChild.getChildNodes();
		int length3 = XpxItemExtnList.getLength();
		for (int m = 0; m < length3; m++) {
			Node XpxItemExtn = XpxItemExtnList.item(m);
			NamedNodeMap XpxItemExtnAttributes = XpxItemExtn.getAttributes();
			Node companyCodeNode = XpxItemExtnAttributes
					.getNamedItem("CompanyCode");
			String companyCodeL = companyCodeNode.getTextContent();
			Node XPXDivisionNode = XpxItemExtnAttributes
					.getNamedItem("XPXDivision");
			String XPXDivision = XPXDivisionNode.getTextContent();
			if (companyCodeL.equals(companyCode)
					&& XPXDivision.equals(customerDiv)) {
				Node orderMultipleNode = XpxItemExtnAttributes
						.getNamedItem("OrderMultiple");
				orderMultiple = orderMultipleNode.getTextContent();
			}
		}
		return orderMultiple;
	}*/

	private static HashMap<String, String> getCustomerDetails(
			YFSEnvironment env, String customerID, String storeFrontId)
			throws YIFClientCreationException, YFSException, RemoteException {
		HashMap<String, String> customerDetails = new HashMap<String, String>();
		if(customerID!=null && storeFrontId!=null){
			YFCDocument inputDocument = YFCDocument.createDocument("Customer");
			YFCElement inputElement = inputDocument.getDocumentElement();
			inputElement.setAttribute("CustomerID", customerID);
			inputElement.setAttribute("OrganizationCode", storeFrontId);
	
			env.setApiTemplate("getCustomerList", SCXmlUtil.createFromString(""
					+ "<CustomerList><Customer><Extn>"
					+ "</Extn></Customer></CustomerList>"));
	
			YIFApi api = YIFClientFactory.getInstance().getApi();
	
			Document outputListDocument = api.invoke(env, "getCustomerList",
					inputDocument.getDocument());
			Element outputListElement = outputListDocument.getDocumentElement();
			NodeList wNodeList = outputListElement.getChildNodes();
			int length = wNodeList.getLength();
			for (int i = 0; i < length; i++) {
				Node customerNode = wNodeList.item(i);
				NodeList customerChildNodes = customerNode.getChildNodes();
				int length1 = customerChildNodes.getLength();
				for (int j = 0; j < length1; j++) {
					Node customerChildNode = customerChildNodes.item(j);
					String companyCode = "";
					String customerDivision = "";
					String useOrderMulUOMFlag = "";
					String shipFromBranch = "";
	
					if (customerChildNode.getNodeName().equals("Extn")) {
						NamedNodeMap extnAttributes = customerChildNode
								.getAttributes();
						Node ExtnCompanyCodeNode = extnAttributes
								.getNamedItem("ExtnCompanyCode");
						if (ExtnCompanyCodeNode != null) {
							companyCode = ExtnCompanyCodeNode.getTextContent();
							if (companyCode != null
									&& companyCode.trim().length() > 0) {
								customerDetails.put("companyCode", companyCode);
							}
						}
						Node ExtnCustomerDivisionNode = extnAttributes
								.getNamedItem("ExtnCustomerDivision");
						if (ExtnCustomerDivisionNode != null) {
							customerDivision = ExtnCustomerDivisionNode
									.getTextContent();
							if (customerDivision != null
									&& customerDivision.trim().length() > 0) {
								customerDetails.put("customerDivision",
										customerDivision);
							}
						}
						Node ExtnShipFromBranchNode = extnAttributes
								.getNamedItem("ExtnShipFromBranch");
						if (ExtnShipFromBranchNode != null) {
							shipFromBranch = ExtnShipFromBranchNode
									.getTextContent();
							if (shipFromBranch != null
									&& shipFromBranch.trim().length() > 0) {
								customerDetails.put("shipFromBranch",
										shipFromBranch);
							}
						}
						Node ExtnUseOrderMulUOMFlagNode = extnAttributes
								.getNamedItem("ExtnUseOrderMulUOMFlag");
						if (ExtnUseOrderMulUOMFlagNode != null) {
							useOrderMulUOMFlag = ExtnUseOrderMulUOMFlagNode
									.getTextContent();
							if (useOrderMulUOMFlag != null
									&& useOrderMulUOMFlag.trim().length() > 0) {
								customerDetails.put("useOrderMulUOMFlag",
										useOrderMulUOMFlag);
							}
						}
					}
				}
		}
		env.clearApiTemplate("getCustomerList");
		}
		return customerDetails;
	}

	public static Document getItemDetails(String itemID, String custID,
			String callingOrgCode, IWCContext wcContext) throws Exception {
		Document outputDoc = null;
		if (null == itemID || YFCCommon.isVoid(itemID)) {
			LOG
					.debug("getItemDetails: Item ID is a required field. Returning a empty document");
			return outputDoc;
		}

		try {
			if (itemID.trim().equals("")) {
				itemID = "INVALID_ITEM";
				return outputDoc;
			}
		} catch (Exception e) {
			itemID = "INVALID_ITEM";
			return outputDoc;
		}

		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Item/@CallingOrganizationCode", callingOrgCode);
		valueMap.put("/Item/@ItemID", itemID);
		valueMap.put("/Item/CustomerInformation/@CustomerID", custID);

		Element input = WCMashupHelper.getMashupInput("itemDetails", valueMap,
				wcContext.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		LOG.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxItemDetails", input,
				wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}
		return outputDoc;
	}
	
	public static Document getItemDetailsForPricingInfo(ArrayList<String> itemIDList, String custID,
			String callingOrgCode, IWCContext wcContext) throws Exception {
		Document outputDoc = null;
		if (YFCCommon.isVoid(itemIDList) || itemIDList.size() <=0) {
			LOG.error("Atleast one ItemID is required to evaluate the xpedxItemDetailsForPricingInfo mashup. Return back to the caller");
			return outputDoc;
		}

		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Item/@CallingOrganizationCode", callingOrgCode);
		valueMap.put("/Item/CustomerInformation/@CustomerID", custID);

		Element input = WCMashupHelper.getMashupInput("xpedxItemDetailsForPricingInfo", valueMap,
				wcContext.getSCUIContext());
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element) inputNodeList.item(0);
		for (int i = 0; i < itemIDList.size(); i++) {
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			Element expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", "ItemID");
			expElement.setAttribute("Value", itemIDList.get(i));
			inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
		}
		String inputXml = SCXmlUtil.getString(input);
		LOG.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxItemDetailsForPricingInfo", input,
				wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}
		return outputDoc;
	}
	
	public static Document getXpedxMinimalItemDetails(ArrayList<String> itemIDList, String custID,
			String callingOrgCode, IWCContext wcContext) throws Exception {
		Document outputDoc = null;
		if (YFCCommon.isVoid(itemIDList) || itemIDList.size() <=0) {
			LOG.error("Atleast one ItemID is required to evaluate the xpedxMinimalItemDetails mashup. Return back to the caller");
			return outputDoc;
		}

		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Item/@CallingOrganizationCode", callingOrgCode);
		valueMap.put("/Item/CustomerInformation/@CustomerID", custID);

		Element input = WCMashupHelper.getMashupInput("xpedxMinimalItemDetails", valueMap,
				wcContext.getSCUIContext());
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element) inputNodeList.item(0);
		for (int i = 0; i < itemIDList.size(); i++) {
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			Element expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", "ItemID");
			expElement.setAttribute("Value", itemIDList.get(i));
			inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
		}
		Object obj = WCMashupHelper.invokeMashup("xpedxMinimalItemDetails", input,wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("Output XML getXpedxMinimalItemDetails: " + SCXmlUtil.getString((Element) obj));
		}
		return outputDoc;
	}
	
	/**
	 * Get only the entitled items 
	 * @param itemIDList
	 * @param custID
	 * @param callingOrgCode
	 * @param wcContext
	 * @return
	 * @throws Exception
	 */
	public static Document getXpedxEntitledItemDetails(ArrayList<String> itemIDList, String custID,
			String callingOrgCode, IWCContext wcContext,String mashupId) throws Exception {
		Document outputDoc = null;
		if(mashupId == null || "".equals(mashupId))
			mashupId="xpedxEntitledItemDetails";
		if (YFCCommon.isVoid(itemIDList) || itemIDList.size() <=0) {
			LOG.error("Atleast one ItemID is required to evaluate the xpedxMinimalItemDetails mashup. Return back to the caller");
			return outputDoc;
		}

		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Item/@CallingOrganizationCode", callingOrgCode);
		valueMap.put("/Item/CustomerInformation/@CustomerID", custID);

		Element input = WCMashupHelper.getMashupInput(mashupId, valueMap,
				wcContext.getSCUIContext());
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element) inputNodeList.item(0);
		for (int i = 0; i < itemIDList.size(); i++) {
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			Element expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", "ItemID");
			expElement.setAttribute("Value", itemIDList.get(i));
			inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
		}
		Object obj = WCMashupHelper.invokeMashup(mashupId, input,wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("Output XML "+mashupId+": " + SCXmlUtil.getString((Element) obj));
			}
		return outputDoc;
	}
	
	/**
	 * Get only the entitled items 
	 * @param itemIDList
	 * @param custID
	 * @param callingOrgCode
	 * @param wcContext
	 * @param mashupId
	 * @return
	 * @throws Exception
	 */
	public static Document getXpedxEntitledItemDetails(ArrayList<String> itemIDList, String custID,
			String callingOrgCode, IWCContext wcContext) throws Exception {
		
		return getXpedxEntitledItemDetails(itemIDList,custID,callingOrgCode,wcContext,null);
	}
	
	public static Document getXpedxAssociationlItemDetails(ArrayList<String> itemIDList, String custID,
			String callingOrgCode, IWCContext wcContext) throws CannotBuildInputException{
		Document outputDoc = null;
		if (YFCCommon.isVoid(itemIDList) || itemIDList.size() <=0) {
			LOG.error("Atleast one ItemID is required to evaluate the xpedxItemDetailsForItemAssociation mashup. Return back to the caller");
			return outputDoc;
		}

		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Item/@CallingOrganizationCode", callingOrgCode);
		valueMap.put("/Item/CustomerInformation/@CustomerID", custID);

		Element input = WCMashupHelper.getMashupInput("xpedxItemDetailsForItemAssociation", valueMap,
				wcContext.getSCUIContext());
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element) inputNodeList.item(0);
		for (int i = 0; i < itemIDList.size(); i++) {
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			Element expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", "ItemID");
			expElement.setAttribute("Value", itemIDList.get(i));
			inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
		}
		Object obj = WCMashupHelper.invokeMashup("xpedxItemDetailsForItemAssociation", input,wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("Output XML for getXpedxAssociationlItemDetails: " + SCXmlUtil.getString((Element) obj));
		}
		return outputDoc;
	}
	
	public static Document getXpedxItemBranchDetails(ArrayList<String> itemIDList, String custID,
			String division, String envCode, IWCContext wcContext) throws Exception {
		Document outputDoc = null;
		if (YFCCommon.isVoid(itemIDList) || itemIDList.size() <=0) {
			LOG.error("Atleast one ItemID is required to evaluate the xpedxItemBranchDetails mashup. Return back to the caller");
			return outputDoc;
		}
		if(YFCCommon.isVoid(division)){
			LOG.error("Division is required to evaluate the xpedxItemBranchDetails mashup. Return back to the caller");
			return outputDoc;
		}
		if(YFCCommon.isVoid(envCode)){
			LOG.error("Environment Code is required to evaluate the xpedxItemBranchDetails mashup. Return back to the caller");
			return outputDoc;
		}
		String callingOrgCode= wcContext.getStorefrontId();
		Object obj = null;
		Document tempDoc = null;
		Document tempDoc2 = null;
		NodeList tempList;
		/*for (int i = 0; i < itemIDList.size(); i++) {
			 * Removed As this is causing a performance issue and calling the getCompleteItemList Api and from that getting the XPXItemExtn
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/XPXItemExtn/@CustomerID", custID);
			valueMap.put("/XPXItemExtn/@XPXDivision", division);
			valueMap.put("/XPXItemExtn/@EnvironmentID", envCode);
			valueMap.put("/XPXItemExtn/@ItemID", itemIDList.get(i));
			Element input = WCMashupHelper.getMashupInput("xpedxItemBranchDetails", valueMap,wcContext.getSCUIContext());
			
			if(obj==null){
				obj = WCMashupHelper.invokeMashup("xpedxItemBranchDetails", input,wcContext.getSCUIContext());
				tempDoc = ((Element) obj).getOwnerDocument();
			}else{// append document
				obj = WCMashupHelper.invokeMashup("xpedxItemBranchDetails", input,wcContext.getSCUIContext());
				tempDoc2 = ((Element) obj).getOwnerDocument();
				tempList = tempDoc2.getDocumentElement().getChildNodes();
				for(int j=0; j<tempList.getLength(); j++){
					SCXmlUtil.importElement(tempDoc.getDocumentElement(), (Element)tempList.item(j));
				}
			}
			 
		}*/
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Item/@CallingOrganizationCode", callingOrgCode);
		valueMap.put("/Item/CustomerInformation/@CustomerID", custID);

		Element input = WCMashupHelper.getMashupInput("xpedxItemExtnListMashup", valueMap,
				wcContext.getSCUIContext());
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element) inputNodeList.item(0);
		for (int i = 0; i < itemIDList.size(); i++) {
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			Element expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", "ItemID");
			expElement.setAttribute("Value", itemIDList.get(i));
			inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
		}
		obj = WCMashupHelper.invokeMashup("xpedxItemExtnListMashup", input,wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("Output XML for getXpxItemExtnList(Mashup is xpedxItemExtnListMashup): " + SCXmlUtil.getString((Element) obj));
		}
		if (null != tempDoc) {
			LOG.debug("Output XML for getXpxItemExtnList(Mashup is xpedxItemExtnListMashup): " + SCXmlUtil.getString(tempDoc));
		}
		return outputDoc;
	}
	
	public static Document getXpedxItemBranchItemAssociationDetails(ArrayList<String> itemIDList, String custID,
			String division, String envCode, IWCContext wcContext) throws Exception {
		Document outputDoc = null;
		if (YFCCommon.isVoid(itemIDList) || itemIDList.size() <=0) {
			LOG.error("Atleast one ItemID is required to evaluate the xpedxItemBranchDetailsForItemAssociation mashup. Return back to the caller");
			return outputDoc;
		}
		if(YFCCommon.isVoid(division)){
			LOG.error("Division is required to evaluate the xpedxItemBranchDetailsForItemAssociation mashup. Return back to the caller");
			return outputDoc;
		}

		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/XPXItemExtn/@CustomerID", custID);
		valueMap.put("/XPXItemExtn/@XPXDivision", division);
		valueMap.put("/XPXItemExtn/@EnvironmentID", envCode);

		Element input = WCMashupHelper.getMashupInput("xpedxItemBranchDetailsForItemAssociation", valueMap,wcContext.getSCUIContext());
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element) inputNodeList.item(0);
		for (int i = 0; i < itemIDList.size(); i++) {
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			Element expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", "ItemID");
			expElement.setAttribute("Value", itemIDList.get(i));
			inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
		}
		Object obj = WCMashupHelper.invokeMashup("xpedxItemBranchDetailsForItemAssociation", input,wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("Output XML for xpedxItemBranchDetailsForItemAssociation: " + SCXmlUtil.getString((Element) obj));
		}
		return outputDoc;
	}

	public static Map<String, String> getXpedxUOMList(String customerID,
			String ItemID, String StoreFrontID) {
		LinkedHashMap<String, String> wUOMsAndConFactors = new LinkedHashMap<String, String>();
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		ISCUITransactionContext scuiTransactionContext = wSCUIContext
				.getTransactionContext(true);
		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
				.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
		try {
			YFCDocument inputDocument = YFCDocument.createDocument("UOMList");
			YFCElement documentElement = inputDocument.getDocumentElement();

			documentElement.setAttribute("ItemID", ItemID);
			documentElement.setAttribute("CustomerID", customerID);
			documentElement.setAttribute("OrganizationCode", StoreFrontID);

			YIFApi api = YIFClientFactory.getInstance().getApi();
			env.setApiTemplate("XPXUOMListAPI", SCXmlUtil.createFromString(""
					+ "<UOMList><UOM>" + "</UOM></UOMList>"));
			Document outputListDocument = api.executeFlow(env, "XPXUOMListAPI",
					inputDocument.getDocument());
			Element wElement = outputListDocument.getDocumentElement();
			
			List<Element> listConv = SCXmlUtil.getChildrenList(wElement);
			
			//NodeList wNodeList = wElement.getChildNodes();
			
			String convStr;
			if (listConv != null) {
				//2964 start
				Collections.sort(listConv,new XpedxSortUOMListByConvFactor());
				
				//int length = listConv.size();
				for (Element eleUOM : listConv) {					
				//2964 end
					/*Node wNode = listConv.item(element);
					
					if (wNode != null) {
					*/	NamedNodeMap nodeAttributes = eleUOM.getAttributes();
						if (nodeAttributes != null) {
							Node UnitOfMeasure = nodeAttributes
									.getNamedItem("UnitOfMeasure");
							Node Conversion = nodeAttributes
									.getNamedItem("Conversion");
							if (UnitOfMeasure != null && Conversion != null) {
								convStr =  Conversion.getTextContent();
								if(!YFCUtils.isVoid(convStr)){
									long convFactorLong = Math.round(Double.parseDouble(convStr));
									wUOMsAndConFactors.put(UnitOfMeasure
										.getTextContent(), Long.toString(convFactorLong));
								}
							}
						}
					}
				}
			

		} catch (Exception ex) {
			log.error(ex.getMessage());
		} catch (Throwable t) {
			// Just adding this catch block to control the null pointer logging 
			// happening in staging.
			log.error(t.getMessage());
		}
		finally {
			scuiTransactionContext.end();
			env.clearApiTemplate("XPXUOMListAPI");
			SCUITransactionContextHelper.releaseTransactionContext(
					scuiTransactionContext, wSCUIContext);
			env = null;
		}
		return wUOMsAndConFactors;

	}
	
	/*
	 * This method is to include a complex query to get the Xpedx Uoms for multiple Items
	 * this will return a map of (itemId, map of UOMS)
	 */
	
	public static Map<String, Map<String,String>> getXpedxUOMList(String customerID,
			ArrayList<String> ItemID, String StoreFrontID) {
		
		LinkedHashMap<String, Map<String,String>> itemUomHashMap = new LinkedHashMap<String, Map<String,String>>();
		
		if(YFCCommon.isVoid(customerID)){
			log.warn("customerID is NULL. cannot call xpedxUOMList method for anonymous user.");
			return itemUomHashMap;
		}
		
		if(ItemID!=null && ItemID.size()>=1) {
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		ISCUITransactionContext scuiTransactionContext = wSCUIContext
				.getTransactionContext(true);
		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
				.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
		try {
			YFCDocument inputDocument = YFCDocument.createDocument("UOMList");
			YFCElement documentElement = inputDocument.getDocumentElement();

			documentElement.setAttribute("CustomerID", customerID);
			documentElement.setAttribute("OrganizationCode", StoreFrontID);
			
			YFCElement complexQueryElement = documentElement.createChild("ComplexQuery");
			YFCElement complexQueryOrElement = documentElement.createChild("Or");
			for (int i = 0; i < ItemID.size(); i++) {
				YFCElement expElement = documentElement.createChild("Exp");
				expElement.setAttribute("Name", "ItemID");
				expElement.setAttribute("Value", ItemID.get(i));
				complexQueryOrElement.appendChild((YFCNode)expElement);
			}
			complexQueryElement.setAttribute("Operator", "AND");
			complexQueryElement.appendChild(complexQueryOrElement);

			YIFApi api = YIFClientFactory.getInstance().getApi();
			env.setApiTemplate("XPXUOMListAPI", SCXmlUtil.createFromString(""
					+ "<ItemList><Item><UOMList><UOM>" + "</UOM></UOMList></Item></ItemList>"));
			Document outputListDocument = api.executeFlow(env, "XPXUOMListAPI",
					inputDocument.getDocument());
			Element wElement = outputListDocument.getDocumentElement();
			NodeList wNodeList = wElement.getChildNodes();
			if (wNodeList != null) {
				int length = wNodeList.getLength();
				String conversion;
				for (int i = 0; i < length; i++) {
					Node wNode = wNodeList.item(i);
					if (wNode != null) {
						NamedNodeMap nodeAttributes = wNode.getAttributes();
						if (nodeAttributes != null) {
							Node itemId = nodeAttributes
									.getNamedItem("ItemID");
							if(itemId!=null) {
								LinkedHashMap<String, String> wUOMsAndConFactors = new LinkedHashMap<String, String>();
								NodeList uomListNodeList =	wNode.getChildNodes();
								Node uomListNode = uomListNodeList.item(0);
								
								//2964 Start
								if (uomListNode != null) {
									List<Element> listOfUOMElements = SCXmlUtil.getChildrenList((Element) uomListNode);
								Collections.sort(listOfUOMElements, new XpedxSortUOMListByConvFactor());
									
									for (Element uomNode : listOfUOMElements) {
									
										if (uomNode != null) {
											NamedNodeMap uomAttributes = uomNode.getAttributes();
											//2964 end
											if (uomAttributes != null) {
												Node UnitOfMeasure = uomAttributes
														.getNamedItem("UnitOfMeasure");
												Node Conversion = uomAttributes
														.getNamedItem("Conversion");
												if (UnitOfMeasure != null && Conversion != null) {
													conversion = Conversion.getTextContent();
													if(!YFCUtils.isVoid(conversion)){
														long convFactor = Math.round(Double.parseDouble(conversion));
															wUOMsAndConFactors.put(UnitOfMeasure
																.getTextContent(), Long.toString(convFactor));
															
													}
												}
											}
										}
									}
								}
							
								itemUomHashMap.put(itemId.getTextContent(), wUOMsAndConFactors);
							}
						}
					}
				}
			}

		} catch (Exception ex) {
			log.error(ex.getMessage());
		} finally {
			scuiTransactionContext.end();
			env.clearApiTemplate("XPXUOMListAPI");
			SCUITransactionContextHelper.releaseTransactionContext(
					scuiTransactionContext, wSCUIContext);
			env = null;
		}
	}
		return itemUomHashMap;

	}

	public static HashMap<String, String> getSAPCustomerLineFieldMap(
			Element customerOrganizationExtnEle) throws CannotBuildInputException 
	{
		
		Element CustHierarchyView=(Element)(customerOrganizationExtnEle.getElementsByTagName("XPXCustHierarchyView")).item(0);
		if(CustHierarchyView ==null)
		{
			return new LinkedHashMap<String, String>();
		}
		return getCustomerLineFieldMap(CustHierarchyView,"SAP");
	}
	
	public static HashMap<String, String> getCustomerLineFieldMap(
			Document customerDoc) throws CannotBuildInputException {
		if (YFCCommon.isVoid(customerDoc)) {
			LOG
					.debug("getCustomerLineFieldMap(): customerDoc is empty. Returning a empty HashMap");
		}
		LOG.debug("Preparing a CustomerLineFieldMap for "
				+ SCXmlUtil.getAttribute(customerDoc.getDocumentElement(),
						"CustomerID"));
		Element customerOrganizationExtnEle = SCXmlUtil.getChildElement(
				customerDoc.getDocumentElement(), "Extn");
		return getCustomerLineFieldMap(customerOrganizationExtnEle,"");
	}
	
	/*
	 * getCustomerLineFieldMap Takes the CustomerDoc as the input parameter, and
	 * parses the Extn child tag, ad prepares a Map of all the Customer Line
	 * fields which has the Flag value set to 'Y'. The Map contains the
	 * LabelName and Label Value.
	 */
	public static HashMap<String, String> getCustomerLineFieldMap(
			Element customerOrganizationExtnEle,String prefix) throws CannotBuildInputException {
		//The customerFieldsMap is a LinkedHashMap as it should retain the sequence of customer fields
		HashMap<String, String> customerFieldsMap = new LinkedHashMap<String, String>();
		
		String custLineNoFlag = SCXmlUtil.getAttribute(
				customerOrganizationExtnEle, prefix+"ExtnCustLineAccNoFlag");
		String custPONoFlag = SCXmlUtil.getAttribute(
				customerOrganizationExtnEle, prefix+"ExtnCustLinePONoFlag");
		//Fix for not showing Seq Number as per Pawan's mail dated 17/3/2011
		/*String custSeqNoFlag = SCXmlUtil.getAttribute(
				customerOrganizationExtnEle, prefix+"ExtnCustLineSeqNoFlag");*/
		String custField1Flag = SCXmlUtil.getAttribute(
				customerOrganizationExtnEle, prefix+"ExtnCustLineField1Flag");
		String custField2Flag = SCXmlUtil.getAttribute(
				customerOrganizationExtnEle, prefix+"ExtnCustLineField2Flag");
		String custField3Flag = SCXmlUtil.getAttribute(
				customerOrganizationExtnEle, prefix+"ExtnCustLineField3Flag");
		
		if ("Y".equals(custLineNoFlag)) {
			//Reverted back to the earlier logic to read the label from customer profile
			//If no label found, Line Account# is used
			String custLineNoLbl = SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineAccLbl");
			if (custLineNoLbl != null && custLineNoLbl.trim().length() > 0)
				customerFieldsMap.put("CustLineAccNo", custLineNoLbl);
			else
				customerFieldsMap.put("CustLineAccNo", "Line Account#");
			//Fix for showing label as Line Account # as per Pawan's mail dated 17/3/2011
			//customerFieldsMap.put("CustLineAccNo", "Line Account#");
		}
		
		if ("Y".equals(custField1Flag)) {
			String custField1Lbl = SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineField1Label");
			if (custField1Lbl != null && custField1Lbl.trim().length() > 0)
				customerFieldsMap.put("CustLineField1", custField1Lbl);
			else
				customerFieldsMap.put("CustLineField1", "Customer Field 1");
		}
		if ("Y".equals(custField2Flag)) {
			String custField2Lbl = SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineField2Label");
			if (custField2Lbl != null && custField2Lbl.trim().length() > 0)
				customerFieldsMap.put("CustLineField2", custField2Lbl);
			else
				customerFieldsMap.put("CustLineField2", "Customer Field 2");
		}
		if ("Y".equals(custField3Flag)) {
			String custField3Lbl = SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineField3Label");
			if (custField3Lbl != null && custField3Lbl.trim().length() > 0)
				customerFieldsMap.put("CustLineField3", custField3Lbl);
			else
				customerFieldsMap.put("CustLineField3", "Customer Field 3");
		}
		if ("Y".equals(custPONoFlag)) {
			//Fix for showing label as Line PO # as per Pawan's mail dated 17/3/2011
			//customerFieldsMap.put("CustomerPONo", "Customer PO No");
			customerFieldsMap.put("CustomerPONo", "Line PO #");
			
		}
		//Fix for not showing Seq Number as per Pawan's mail dated 17/3/2011
		/*if ("Y".equals(custSeqNoFlag)) {
			customerFieldsMap.put("CustomerLinePONo", "Customer Seq No");
		}*/
		
		return customerFieldsMap;
	}
	
	public static Document getCustomerExtnFlagsDoc(IWCContext wcContext) {

		Document sapCustomer = null;
		Document billToCustomer = null;
		Document shipToCustomerDoc = null;
		String billToCustomerID = null;
		String sapCustomerID = null;
		if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext)){// if default ship to is available
			shipToCustomerDoc = XPEDXWCUtils.getCustomerDocument(wcContext.getCustomerId(), wcContext.getStorefrontId());// shipTo
			Element shipToCustomerElem = shipToCustomerDoc.getDocumentElement();
			Element billToCustElement = SCXmlUtil.getChildElement(shipToCustomerElem, "ParentCustomer");// parent of shipTo - billTo
			if(billToCustElement!=null)
				billToCustomerID = billToCustElement.getAttribute("CustomerID");// billTocustomerID
			if(!YFCUtils.isVoid(billToCustomerID)){
				billToCustomer = XPEDXWCUtils.getCustomerDocument(billToCustomerID, wcContext.getStorefrontId());// get billTo's parent
			}
			if(billToCustomer!=null){
				Element billToCustomerElem = billToCustomer.getDocumentElement();// billTo document
				Element sapCustElement = SCXmlUtil.getChildElement(billToCustomerElem, "ParentCustomer");// billTo's parent - SAP customerID
				if(sapCustElement!=null)
					sapCustomerID = sapCustElement.getAttribute("CustomerID");// SAPcustomerID
				if(!YFCUtils.isVoid(sapCustomerID)){
					sapCustomer = XPEDXWCUtils.getCustomerDocument(sapCustomerID, wcContext.getStorefrontId());// SAPCustomer EXTN details
				}
			}
		}

		return sapCustomer;
	}
	public static Document getSAPCustomerExtnFlagsDoc(IWCContext wcContext) {

		Document sapCustomer = null;
		if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext)){// if default ship to is available
			Map<String, String> valueMap1 = new HashMap<String, String>();
			valueMap1.put("/XPXCustomerHierarchyView/@ShipToCustomerID", wcContext.getCustomerId());
			Element input1;
			try {
				input1 = WCMashupHelper.getMashupInput("xpedx-getAssignedShipTos-View",
						valueMap1, wcContext.getSCUIContext());

				Object obj1 = WCMashupHelper.invokeMashup("xpedx-getAssignedShipTos-View",
						input1, wcContext.getSCUIContext());

				sapCustomer = ((Element) obj1).getOwnerDocument();

			} catch (CannotBuildInputException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return sapCustomer;
	}
	
	/**
	 * This method fetches the customer profile rules for order
	 * @param orderElement
	 * @param wcContext
	 * @return
	 * @throws Exception
	 */
	public static Document getValidationRulesForCustomer(Element orderElement, IWCContext wcContext) throws Exception
	{
		Document outputDoc = null;
		//Get the customer details from the order
		if(YFCCommon.isVoid(orderElement))
			return outputDoc;
		
		String customerID = orderElement.getAttribute("BuyerOrganizationCode");
		String enterpriseCode = orderElement.getAttribute("EnterpriseCode");
		String entryType = orderElement.getAttribute("EntryType");
		
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Order/@BuyerOrganizationCode", customerID);
		valueMap.put("/Order/@EnterpriseCode", enterpriseCode);
		valueMap.put("/Order/@EntryType", entryType);

		Element input = WCMashupHelper.getMashupInput("getCustomerRulesForOrder", valueMap,wcContext.getSCUIContext());
		outputDoc = ((Element)WCMashupHelper.invokeMashup("getCustomerRulesForOrder", input,wcContext.getSCUIContext())).getOwnerDocument();
		return outputDoc;
	}
	
	/**
	 * This method validates the customer field values for each order line as per the rules set at customer profile level.
	 * Customer Line Account#
	 * Customer Field 1
	 * Customer Field 2
	 * Customer Field 3
	 * Customer Line PO #
	 * @param orderElem
	 * @param rulesDoc
	 * @param wcContext
	 * @throws Exception
	 */
	public static void validateCustomerFieldValues(Element orderElem, Document rulesDoc, IWCContext wcContext)
    throws Exception
    {
	    HashMap<String, ArrayList<String>> requiredCustFieldsErrorMap = new HashMap<String, ArrayList<String>>();
		Element orderLinesElement = SCXmlUtil.getChildElement(orderElem, "OrderLines");
	    ArrayList<Element> orderLineElemList = SCXmlUtil.getElements(orderLinesElement, "OrderLine");
	  
	    
	    Element rulesElem = rulesDoc.getDocumentElement();
	    ArrayList<Element> ruleElems = SCXmlUtil.getChildren(rulesElem, "Rule");
	    if(orderLineElemList == null || orderLineElemList.size() == 0)
	    {
	    	if(ruleElems != null && ruleElems.size() > 0)
		    {
		        for(int i = 0; i < ruleElems.size(); i++)
		        {
		        	 String ruleId = ((Element)ruleElems.get(i)).getAttribute("RuleId");
		        	 validateCustomerPOBusinessRule(wcContext,ruleId);
		        	 break;
		        }
		        
		    }
	        return;
	    }
	    ArrayList<String> requiredCustFields = new ArrayList<String>();
	    if(ruleElems != null && ruleElems.size() > 0)
	    {
	        for(int i = 0; i < ruleElems.size(); i++)
	        {
	            String ruleId = ((Element)ruleElems.get(i)).getAttribute("RuleId");
	            if(log.isDebugEnabled()){
	            log.debug("RuleID:::"+ruleId);
	            }
	            if("RequiredCustomerLineAccountNo".equalsIgnoreCase(ruleId))
	                requiredCustFields.add("ExtnCustLineAccNo");
	            else
	            if("RequireCustomerLineField1".equalsIgnoreCase(ruleId))
	                requiredCustFields.add("ExtnCustLineField1");
	            else
	            if("RequireCustomerLineField2".equalsIgnoreCase(ruleId))
	                requiredCustFields.add("ExtnCustLineField2");
	            else
	            if("RequireCustomerLineField3".equalsIgnoreCase(ruleId))
	                requiredCustFields.add("ExtnCustLineField3");
	            else
		        if("RequiredCustomerLinePO".equalsIgnoreCase(ruleId))
		                requiredCustFields.add("CustomerPONo");
		        else
		        {
		        	validateCustomerPOBusinessRule(wcContext,ruleId);
		        }
	        }
	
	    }
	    
	    for(Iterator<Element> orderLineIter = orderLineElemList.iterator(); orderLineIter.hasNext();)
	    {
	    	ArrayList<String> missingReqFieldsForOLK = null;
	    	Element orderLineElem = (Element)orderLineIter.next();
	        if(orderLineElem != null)
	        {
	            String orderLineKey = SCXmlUtil.getAttribute(orderLineElem, "OrderLineKey");
	            String orderLineType = SCXmlUtil.getAttribute(orderLineElem, "LineType");
	            Element orderLineExtn = SCXmlUtil.getChildElement(orderLineElem, "Extn");
	          // Added for Jira 3853 - Rules are not applied to comment and charge line.  
	          if(orderLineType!=null && !orderLineType.equalsIgnoreCase("M")&& !orderLineType.equalsIgnoreCase("C")){
	            for(Iterator<String> requiredFieldsIter = requiredCustFields.iterator(); requiredFieldsIter.hasNext();)
	            {
	                String requiredCustField = (String)requiredFieldsIter.next();
	                if("CustomerPONo".equalsIgnoreCase(requiredCustField))
	                {
	                	if(YFCCommon.isVoid(orderLineElem.getAttribute(requiredCustField))) {
	                		if(missingReqFieldsForOLK==null)
	                			missingReqFieldsForOLK = new ArrayList<String>();
	                		missingReqFieldsForOLK.add(requiredCustField);
	                	}
	                }
	                else if(orderLineExtn!=null && YFCCommon.isVoid(orderLineExtn.getAttribute(requiredCustField))){
	                		if(missingReqFieldsForOLK==null)
	                			missingReqFieldsForOLK = new ArrayList<String>();
	                		//Remove the Extn string
	                		 missingReqFieldsForOLK.add(requiredCustField.substring(4, requiredCustField.length()));
	                }
	            }
	          }
	            if(missingReqFieldsForOLK!=null && !missingReqFieldsForOLK.isEmpty())
	            	requiredCustFieldsErrorMap.put(orderLineKey, missingReqFieldsForOLK);
	            
	        }
	    }
	    
	    if(requiredCustFieldsErrorMap!=null && !requiredCustFieldsErrorMap.isEmpty())
	    {
	    	HttpSession session = wcContext.getSCUIContext().getRequest().getSession();
			session.setAttribute("requiredCustFieldsErrorMap", requiredCustFieldsErrorMap);
	    }
    }

	private static void validateCustomerPOBusinessRule(IWCContext wcContext,String ruleId)
	{
		
		if("RequireCustomerPO".equalsIgnoreCase(ruleId))
		{
			HttpSession session = wcContext.getSCUIContext().getRequest().getSession();
			session.setAttribute(XPEDXConstants.REQUIRE_CUSTOMER_PO_NO_RULE, "true");
		}
	}



	public static boolean isCartOnBehalfOf(IWCContext wcContext) {
		String customerInSession = XPEDXWCUtils
				.getLoggedInCustomerFromSession(wcContext);
		if (customerInSession == null) {
			return false;
		}
		return true;
	}

	public static void createNewCartInContext(IWCContext wcContext) {
		try {
			Element orderOutput = XPEDXOrderUtils
					.createNewDraftOrderOnBehalfOf(wcContext, NEW_CART_NAME);
			if (orderOutput != null) {
				Element orderEl = XMLUtilities.getElement(((Node) orderOutput
						.getOwnerDocument()), "//Order");
				String orderHeaderKey = orderEl.getAttribute("OrderHeaderKey");
				CommerceContextHelper.overrideCartInContext(wcContext,
						orderHeaderKey);
				XPEDXWCUtils.setObectInCache("OrderHeaderInContext", orderHeaderKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Element createNewDraftOrderOnBehalfOf(IWCContext wcContext,
			String cartName) throws Exception {
		return createNewDraftOrderOnBehalfOf(wcContext,cartName,null);
	}
	
	public static Element createNewDraftOrderOnBehalfOf(IWCContext wcContext,
			String cartName,String orderDescription) throws Exception {
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Order/@EnterpriseCode", wcContext.getStorefrontId());
		valueMap.put("/Order/@BillToID", XPEDXWCUtils
				.getLoggedInCustomerFromSession(wcContext));
		valueMap.put("/Order/@ShipToID", wcContext.getCustomerId());
		valueMap.put("/Order/PriceInfo/@Currency", wcContext
				.getDefaultCurrency());
		valueMap.put("/Order/@OrderName", cartName);
		valueMap
				.put("/Order/@BuyerOrganizationCode", wcContext.getCustomerId());
		Document customerDetails = XPEDXWCUtils.getCustomerDetails(wcContext.getCustomerId(), wcContext.getStorefrontId(),"xpedx-customer-getCustomerAndParentAddressInformation");
		String billToCustomerID = SCXmlUtil.getXpathAttribute(customerDetails.getDocumentElement(), "//Customer/ParentCustomer/@CustomerID");
		String shipCompleteFlag = SCXmlUtil.getXpathAttribute(customerDetails.getDocumentElement(), "//Customer/Extn/@ExtnShipComplete");
		XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		if(shipToCustomer != null)
		{
			valueMap.put("/Order/Extn/@ExtnShipToName", shipToCustomer.getExtnCustomerName());
			XPEDXShipToCustomer billToCustomer=shipToCustomer.getBillTo();
			if(billToCustomer != null )
			{
				valueMap.put("/Order/Extn/@ExtnBillToName", billToCustomer.getExtnCustomerName());
			}
		}
		if(billToCustomerID !=null && billToCustomerID.trim().length()>0) {
			String billToKey;
			//Document billToCustomerDetails = XPEDXWCUtils.getCustomerDetails(billToCustomerID, wcContext.getStorefrontId(), "xpedx-customer-getCustomerAddressInfo");
			Element billToCustomerDetails =SCXmlUtil.getXpathElement(customerDetails.getDocumentElement(), "//Customer/ParentCustomer");
			Element billToAddressElement = SCXmlUtil.getElementByAttribute(billToCustomerDetails,"//CustomerAdditionalAddressList/CustomerAdditionalAddress", "IsDefaultBillTo", "Y");
			if(billToAddressElement==null) {
				billToAddressElement = SCXmlUtil.getXpathElement(billToCustomerDetails, "//BuyerOrganization/BillingPersonInfo");
				billToKey = SCXmlUtil.getAttribute(billToAddressElement, "PersonInfoKey");
			}
			else {
				//billToKey = SCXmlUtil.getXpathAttribute(billToAddressElement, "//CustomerAdditionalAddress/PersonInfo/@PersonInfoKey");				
				billToAddressElement = SCXmlUtil.getXpathElement(billToCustomerDetails, "//BuyerOrganization/BillingPersonInfo");
				billToKey = SCXmlUtil.getAttribute(billToAddressElement, "PersonInfoKey");
				if(log.isDebugEnabled()){
		            log.debug("INSIDE XPEDXOrderUtils.createNewDraftOrderOnBehalfOf, billToKey = " +  billToKey);
				}
			}
			if(billToKey != null && billToKey.trim().length()>0) {
				valueMap.put("/Order/@BillToKey", billToKey);
			}
		}
//		Element shipToCustAddress = SCXmlUtil.getElementByAttribute(customerDetails.getDocumentElement(), "//Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress", "IsDefaultShipTo", "Y");
//		Element shipToCustAddress = SCXmlUtil.getElementByAttribute(customerDetails.getDocumentElement(),"CustomerAdditionalAddressList/CustomerAdditionalAddress", "IsDefaultShipTo", "Y");
				
		List<Element> theShipToList = XMLUtilities.getElements(customerDetails.getDocumentElement(),"//Customer/CustomerAdditionalAddressList");
		if(log.isDebugEnabled()){
            log.debug("INSIDE XPEDXOrderUtils.createNewDraftOrderOnBehalfOf, theShipToList XML = " +  SCXmlUtil.getString(theShipToList.get(0)));
		}
		
		List<Element> shipToCustAddress = XMLUtilities.getElements (theShipToList.get(0),"//CustomerAdditionalAddress[@IsDefaultShipTo='Y']/PersonInfo");
		if(log.isDebugEnabled()){
            log.debug("INSIDE XPEDXOrderUtils.createNewDraftOrderOnBehalfOf, shipToCustAddress XML = " +  SCXmlUtil.getString(shipToCustAddress.get(0)));	
		}
						
								 
		if(shipToCustAddress!=null) {
			//String shipToKey = SCXmlUtil.getXpathAttribute(shipToCustAddress, "//CustomerAdditionalAddress/PersonInfo/@PersonInfoKey");
			String shipToKey = SCXmlUtil.getAttribute(shipToCustAddress.get(0), "PersonInfoKey");
			if(shipToKey!=null && shipToKey.trim().length()>0)
				if(log.isDebugEnabled()){
		            log.debug("INSIDE XPEDXOrderUtils.createNewDraftOrderOnBehalfOf, shipToKey IS NOT NULL = " +  shipToKey);
				}
				valueMap.put("/Order/@ShipToKey", shipToKey);
		}
		String defaultCarrierServiceCode = BusinessRuleUtil.getBusinessRule(
				DEFAULT_CARRIER_SERVICE_RULE, DOCUMENT_TYPE, wcContext);
		if (!(defaultCarrierServiceCode != null && defaultCarrierServiceCode
				.trim().length() > 0))
			defaultCarrierServiceCode = "Standard Shipping";
		valueMap.put("/Order/@CarrierServiceCode", defaultCarrierServiceCode);
		valueMap.put("/Order/@CustomerContactID", wcContext
				.getEffectiveUserId());
		valueMap.put("/Order/Extn/@ExtnShipComplete", shipCompleteFlag);
		valueMap.put("/Order/Extn/@ExtnBillToCustomerID", billToCustomerID);
		if(orderDescription != null)
			valueMap.put("/Order/Extn/@ExtnOrderDesc", orderDescription);
		
		Element input = WCMashupHelper.getMashupInput(CREATE_MASHUP_ID,
				valueMap, wcContext);
		Element output = (Element) WCMashupHelper.invokeMashup(
				CREATE_MASHUP_ID, input, wcContext.getSCUIContext());
		return output;
	}

	/**
	 * This method get the BillTo and ShipTo ID's of an order
	 * @param orderHeaderKey
	 * @param wcContext
	 * @return
	 */
	public HashMap<String, String> getBillToShipToFromOrder(String orderHeaderKey, IWCContext wcContext) {
			
			Element orderDocElement = SCXmlUtil.createDocument("Order").getDocumentElement();
			orderDocElement.setAttribute("OrderHeaderKey", orderHeaderKey);
			HashMap<String, String> billToShipToMap = new HashMap<String, String>();
			Document outputDoc;
			
			Object obj = WCMashupHelper.invokeMashup("xpedxOrderBillToShipToInfo", orderDocElement, wcContext.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			
			NodeList orderElementList = outputDoc.getElementsByTagName("Order");
			for(int i=0; orderElementList!=null && i<orderElementList.getLength(); ){
				Element orderElement = (Element)orderElementList.item(i);
				String shipToID = orderElement.getAttribute("ShipToID");
				/*Begin - Changes made by Mitesh Parikh for JIRA 3581*/
				//billToShipToMap.put("OrderBillToID", YFCUtils.isVoid(shipToID)?"":getBillToFromShipTo(shipToID, wcContext));
				billToShipToMap.put("OrderBillToID", YFCUtils.isVoid(shipToID)?"":getBillTofromOrderElement(orderElement));
				/*End - Changes made by Mitesh Parikh for JIRA 3581*/
				billToShipToMap.put("OrderShipToID", YFCUtils.isVoid(shipToID)?"":XPEDXWCUtils.formatBillToShipToCustomer(shipToID));
				break;
			}
			
			return billToShipToMap;
	}
	
	
	/**
	 * This method get the BillTo and ShipTo ID's of an order
	 * @param orderHeaderKey
	 * @param wcContext
	 * @return
	 */
	public HashMap<String, String> getBillToShipToFromOrder(Document outputDoc, String orderHeaderKey,IWCContext wcContext)
	{
		
			HashMap<String, String> billToShipToMap = new HashMap<String, String>();
			if(outputDoc == null)
			{
				return getBillToShipToFromOrder(orderHeaderKey,wcContext);
			}
			else
			{
				Element orderElement=outputDoc.getDocumentElement();
				String shipToID = orderElement.getAttribute("ShipToID");
				ArrayList<Element> orderExtn=SCXmlUtil.getElements(orderElement, "Extn");
				billToShipToMap.put("OrderShipToID", YFCUtils.isVoid(shipToID)?"":XPEDXWCUtils.formatBillToShipToCustomer(shipToID));
				XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
				String billToCustomerId="";
				if(orderExtn != null && orderExtn.size()>0)
				{
					billToCustomerId=orderExtn.get(0).getAttribute("ExtnBillToCustomerID");
					
				}

				if(!YFCCommon.isVoid(billToCustomerId))
				{
					billToShipToMap.put("OrderBillToID", YFCUtils.isVoid(shipToID)?"":XPEDXWCUtils.formatBillToShipToCustomer(billToCustomerId));
				}
				else if(shipToCustomer.getCustomerID().equals(shipToID))
				{
					XPEDXShipToCustomer billToCustomer=shipToCustomer.getBillTo();
					billToShipToMap.put("OrderBillToID", YFCUtils.isVoid(shipToID)?"":XPEDXWCUtils.formatBillToShipToCustomer(billToCustomer.getCustomerID()));
				}
				else
				{	
					/*Begin - Changes made by Mitesh Parikh for JIRA 3581*/
					//billToShipToMap.put("OrderBillToID", YFCUtils.isVoid(shipToID)?"":getBillToFromShipTo(shipToID, wcContext));
					billToShipToMap.put("OrderBillToID", YFCUtils.isVoid(shipToID)?"":getBillTofromOrderElement(orderElement));
					/*End - Changes made by Mitesh Parikh for JIRA 3581*/
				}
			}
			return billToShipToMap;
	}
	
	/**
	 * This method get the BillTo ID from a ShipTo
	 * @param shipToID
	 * @param wcContext
	 * @return
	 */
	private String getBillToFromShipTo(String shipToID, IWCContext wcContext) {
		Element custDocElement = SCXmlUtil.createDocument("Customer").getDocumentElement();
		String billToID = "";
		if(shipToID==null || shipToID.trim().length()==0)
			return billToID;
		
		custDocElement.setAttribute("CustomerID", shipToID);
		Document outputDoc;
		
		Object obj = WCMashupHelper.invokeMashup("xpedxCustListForBillToInfo", custDocElement, wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		
		
		NodeList parentCustList = outputDoc.getElementsByTagName("ParentCustomer");
		for(int i=0; parentCustList!=null && i<parentCustList.getLength(); ){
			Element parentCustElem = (Element)parentCustList.item(i);
			billToID = parentCustElem.getAttribute("CustomerID");
			return YFCUtils.isVoid(billToID)?"":XPEDXWCUtils.formatBillToShipToCustomer(billToID);
		}
		
		return billToID;
	}
	
	public static HashMap<String, HashMap<String,ArrayList<Element>>> getXpedxAssociationsForItems(ArrayList<String> itemIDList, 
			IWCContext wcContext, boolean editMode) throws XPathExpressionException
	{
		
		String custID = wcContext.getCustomerId();
		String callingOrgCode = wcContext.getStorefrontId();
		
		HashMap<String, HashMap<String,ArrayList<Element>>> allAssociatedItemsMap = new HashMap<String, HashMap<String,ArrayList<Element>>>(); 
		
		HashMap<String, ArrayList<Element>> replacementItemsMap = new HashMap<String, ArrayList<Element>>();
		HashMap<String, ArrayList<Element>> alternateItemsMap = new HashMap<String, ArrayList<Element>>();
		HashMap<String, ArrayList<Element>> complimentaryItemsMap = new HashMap<String, ArrayList<Element>>();
		HashMap<String, ArrayList<Element>> upgradeItemsMap = new HashMap<String, ArrayList<Element>>();
		HashMap<String, ArrayList<Element>> crosssellItemsMap = new HashMap<String, ArrayList<Element>>();
		HashMap<String, ArrayList<Element>> upsellItemsMap = new HashMap<String, ArrayList<Element>>();
		HashMap<String, ArrayList<Element>> itemExtnElemsMap = new HashMap<String, ArrayList<Element>>();
		HashMap<String, ArrayList<Element>> itemListElemsMap = new HashMap<String, ArrayList<Element>>();
		
		ArrayList<String> itemIDListForGetCompleteItemList = new ArrayList<String>();
		ArrayList<Element> itemElementList = new ArrayList<Element>();
		
		Document itemAssociationDoc = null;
		//get the getXpedxAssociationlItemDetails
		try {
			itemAssociationDoc = XPEDXOrderUtils.getXpedxAssociationlItemDetails(itemIDList, custID, callingOrgCode, wcContext);
		} catch (CannotBuildInputException e) {
			LOG.error("Error getting National Level item association.",e);
			return null;
		}
		if(null == itemAssociationDoc){
			LOG.error("No national level item association could be found");
			return null;
		}
		NodeList nItemList = itemAssociationDoc.getElementsByTagName("Item");
		int itemNodeListLength = nItemList.getLength();
		for (int i = 0; i < itemNodeListLength; i++) {
			Element itemElement = (Element)nItemList.item(i);
			itemElementList.add(itemElement);
			String itemID = SCXmlUtil.getAttribute(itemElement, "ItemID");

			ArrayList<Element> crossSellItemsList =  new ArrayList<Element>();
			ArrayList<Element> upSellItemsList =  new ArrayList<Element>();
			LOG.debug("Preparing national level association for item Id "+itemID);
			//ArrayList relatedItems = new ArrayList();
			Element associationTypeListElem = null;
			associationTypeListElem = XMLUtilities.getElement(itemElement, "AssociationTypeList");
			if (associationTypeListElem != null) {
				List<Element> crossSellElements = XMLUtilities.getElements(associationTypeListElem,"AssociationType[@Type='CrossSell']");
				List<Element> upSellElements = XMLUtilities.getElements(associationTypeListElem,"AssociationType[@Type='UpSell']");
				for (int j = 0; j < crossSellElements.size(); j++) {
					Element associationTypeElem = crossSellElements.get(j);
					Element associationListElem = XMLUtilities.getElement(associationTypeElem, "AssociationList");
					List associationList = XMLUtilities.getChildElements(associationListElem, "Association");
					if (associationList != null&& !associationList.isEmpty()) {
						Iterator associationIter = associationList.iterator();
						while (associationIter.hasNext()) {
							Element association = (Element) associationIter.next();
							Element associateditemEl = XMLUtilities.getElement(association, "Item");
							/*relatedItems.add(associateditemEl);*/
							crossSellItemsList.add(associateditemEl);
							String curritemid = XMLUtils.getAttributeValue(associateditemEl, "ItemID");
							if(!itemIDListForGetCompleteItemList.contains(curritemid)){
								itemIDListForGetCompleteItemList.add(curritemid);
							}
						}
					}
				}//for cross sell
				for (int k = 0; k < upSellElements.size(); k++) {
					Element associationTypeElem = upSellElements.get(k);
					Element associationListElem = XMLUtilities.getElement(associationTypeElem, "AssociationList");
					List associationList = XMLUtilities.getChildElements(associationListElem, "Association");
					if (associationList != null && !associationList.isEmpty()) {
						Iterator associationIter = associationList.iterator();
						while (associationIter.hasNext()) {
							Element association = (Element) associationIter.next();
							Element associateditemEl = XMLUtilities.getElement(association, "Item");
							/*relatedItems.add(associateditemEl);*/
							upSellItemsList.add(associateditemEl);
							String curritemid = XMLUtils.getAttributeValue(associateditemEl, "ItemID");
							if(!itemIDListForGetCompleteItemList.contains(curritemid)){
								itemIDListForGetCompleteItemList.add(curritemid);
							}
						}
					}
				}//for upsell
				crosssellItemsMap.put(itemID, crossSellItemsList);
				upsellItemsMap.put(itemID, upSellItemsList);
			}//if
		}
		
		allAssociatedItemsMap.put(CROSS_SELL_ITEMS_KEY, crosssellItemsMap);
		allAssociatedItemsMap.put(UP_SELL_ITEMS_KEY, upsellItemsMap);
		
		XPEDXWCUtils xPEDXWCUtils = new XPEDXWCUtils();
		XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		/*String customerDivision = (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH);
		String envCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.ENVIRONMENT_CODE);*/
		
		String customerDivision =shipToCustomer.getExtnShipFromBranch();
		String envCode = shipToCustomer.getExtnEnvironmentCode();
		if(envCode==null || envCode.trim().length()==0)
			envCode = XPEDXWCUtils.getEnvironmentCode(wcContext.getCustomerId());
		Document itemBranchItemAssociationDoc = null;
		try {
			itemBranchItemAssociationDoc = XPEDXOrderUtils.getXpedxItemBranchItemAssociationDetails(itemIDList, custID, customerDivision, envCode, wcContext);			
			
		} catch (Exception e) {
			LOG.error("Error getting item branch item association.",e);
			return null;
		}
		if(null == itemBranchItemAssociationDoc){
			LOG.error("No national level item association could be found");
			return null;
		}
		
		
		
		//loop through the itemIDList
		for(int i=0;i<itemIDList.size();i++){
			String itemID = itemIDList.get(i);
			List<Element> xPXItemExtnList = XMLUtilities.getElements(itemBranchItemAssociationDoc.getDocumentElement(), "XPXItemExtn[@ItemID='"+itemIDList.get(i)+"']");
			if(xPXItemExtnList == null || xPXItemExtnList.size() <= 0){
				continue;
			}
			
			ArrayList<Element> alternateAssItemIDList = null;
			ArrayList<Element> complementaryAssItemIDList = null;
			ArrayList<Element> upgradeAssItemIDList = null;
			ArrayList<Element> replacementAssItemIDList = null;
			
			//but take only one, since we dont want duplicate elements
			Element xPXItemExtnElement = xPXItemExtnList.get(0);
			//Start - Add the ItemExtn Element to the Map of itemExtnElemsMap with ItemID as the Key and put them as ITEM_EXTN in the allAssociatedItemsMap
			ArrayList<Element> itemExtnElemArray = new ArrayList<Element>();
			itemExtnElemArray.add(xPXItemExtnElement);
			itemExtnElemsMap.put(itemID, itemExtnElemArray);
			//End - Add ItemExtn Element to the Map itemExtnElemsMap
			Element xPXItemAssociationsListElement = SCXmlUtil.getChildElement(xPXItemExtnElement, "XPXItemAssociationsList");
			if(xPXItemAssociationsListElement == null)
				continue;
			//only replacement items will go to the xpedxItemIDUOMToReplacementListMap. Other items will go to the xpedxItemIDUOMToRelatedItemsListMap.
			List<Element> replacementList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='R']");
			List<Element> alternateList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='A']");
			List<Element> complementaryList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='C']");
			List<Element> upgradeList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='U']");
			
			//prepare the map for replacement
			if(null!=replacementList && replacementList.size() >=0){
				replacementAssItemIDList = new ArrayList<Element>();
				for(Element replacementItem:replacementList){
					String associatedItemID = SCXmlUtil.getAttribute(replacementItem, "AssociatedItemID");
					if(!YFCCommon.isVoid(associatedItemID)){
						replacementAssItemIDList.add(replacementItem);
						if(!itemIDListForGetCompleteItemList.contains(associatedItemID))
							itemIDListForGetCompleteItemList.add(associatedItemID);
					}
				}
				if(replacementAssItemIDList.size()>0){
					replacementItemsMap.put(itemID, replacementAssItemIDList);
				}
			}
			//prepare the map for alternate and complimentary
			//ArrayList<String> relatedAssItemIDList = new ArrayList<String>();
			//ArrayList<String> alternateAssItemIDList = new ArrayList<String>();
			//get the alternateList
			if(null!=alternateList && alternateList.size() >=0){
				alternateAssItemIDList = new ArrayList<Element>();
				for(Element alterItemItem:alternateList){
					String associatedItemID = SCXmlUtil.getAttribute(alterItemItem, "AssociatedItemID");
					if(!YFCCommon.isVoid(associatedItemID)){
						alternateAssItemIDList.add(alterItemItem);
						if(!itemIDListForGetCompleteItemList.contains(associatedItemID))
							itemIDListForGetCompleteItemList.add(associatedItemID);
					}
				}
				if(alternateAssItemIDList.size()>0){
					alternateItemsMap.put(itemID, alternateAssItemIDList);
				}
			}			
			//get the complementaryList
			//ArrayList<String> complementaryAssItemIDList = new ArrayList<String>();
			if(null!=complementaryList && complementaryList.size() >=0){
				complementaryAssItemIDList = new ArrayList<Element>();
				for(Element comItem:complementaryList){
					String associatedItemID = SCXmlUtil.getAttribute(comItem, "AssociatedItemID");
					if(!YFCCommon.isVoid(associatedItemID)){
						/*relatedAssItemIDList.add(associatedItemID);*/
						complementaryAssItemIDList.add(comItem);
						if(!itemIDListForGetCompleteItemList.contains(associatedItemID))
							itemIDListForGetCompleteItemList.add(associatedItemID);
					}
				}
				if(complementaryAssItemIDList.size()>0){
					complimentaryItemsMap.put(itemID, complementaryAssItemIDList);
				}
			}
			
			//get the upgraded items
			//ArrayList<String> upgradeAssItemIDList = new ArrayList<String>();
			if(null!=upgradeList && upgradeList.size() >=0){
				upgradeAssItemIDList = new ArrayList<Element>();
				for(Element upgradeItem:upgradeList){
					String associatedItemID = SCXmlUtil.getAttribute(upgradeItem, "AssociatedItemID");
					if(!YFCCommon.isVoid(associatedItemID)){
						/*relatedAssItemIDList.add(associatedItemID);*/
						upgradeAssItemIDList.add(upgradeItem);
						if(!itemIDListForGetCompleteItemList.contains(associatedItemID))
							itemIDListForGetCompleteItemList.add(associatedItemID);
					}
				}
				if(upgradeAssItemIDList.size()>0){
					upgradeItemsMap.put(itemID, upgradeAssItemIDList);
				}
			}
			
		}//End of for loop
		if(null==itemIDListForGetCompleteItemList || itemIDListForGetCompleteItemList.size()<=0){
			LOG.debug("No branch level associated items.");
		}
		//remove the duplicate items and call getCompleteItemList
		HashSet hs = new HashSet();
		hs.addAll(itemIDListForGetCompleteItemList);
		itemIDListForGetCompleteItemList.clear();
		itemIDListForGetCompleteItemList.addAll(hs);
		//call getCompleteItemList
		Document itemDetailsListDoc = null;
		try {
			// invoking a different function which will give onyl the entitiled items - 734
			itemDetailsListDoc = getXpedxEntitledItemDetails(itemIDListForGetCompleteItemList, custID, wcContext.getStorefrontId(), wcContext);
		} catch (Exception e) {
			LOG.error("Exception while getting item details for associated items",e);
			return null;
		}
		//prepare the xpedxItemIDUOMToReplacementListMap only when editMode is true
		Set replacementMapKeySet = replacementItemsMap.keySet();
		if(editMode ==  true && replacementMapKeySet!=null){
		Iterator<String> replacementIterator = replacementMapKeySet.iterator();
			while(replacementIterator.hasNext()){
				ArrayList replacementItemsElementList = new ArrayList();
				String itemID = replacementIterator.next();
				ArrayList<Element> asscociatedItemIDList = replacementItemsMap.get(itemID);
				for(Element assItem:asscociatedItemIDList){
					String assID = assItem.getAttribute("AssociatedItemID");
					List<Element> itemDetailsList = XMLUtilities.getElements(itemDetailsListDoc.getDocumentElement(),"Item[@ItemID='"+assID+"']");
					if(null==itemDetailsList || itemDetailsList.size()<=0)
						continue;
					replacementItemsElementList.add(itemDetailsList.get(0));
				}
				replacementItemsMap.put(itemID, replacementItemsElementList);
			}//end while loop
		}
		
		Set alternateMapKeySet = alternateItemsMap.keySet();
		if(alternateMapKeySet!=null && alternateMapKeySet.size()>0){
		Iterator<String> alternateIterator = alternateMapKeySet.iterator();
			while(alternateIterator.hasNext()){
				ArrayList alternateItemsElementList = new ArrayList();
				String itemID = alternateIterator.next();
				ArrayList<Element> asscociatedItemIDList = alternateItemsMap.get(itemID);
				for(Element assItem:asscociatedItemIDList){
					String assID = assItem.getAttribute("AssociatedItemID");
					List<Element> itemDetailsList = XMLUtilities.getElements(itemDetailsListDoc.getDocumentElement(),"Item[@ItemID='"+assID+"']");
					if(null==itemDetailsList || itemDetailsList.size()<=0)
						continue;
					alternateItemsElementList.add(itemDetailsList.get(0));
				}
				alternateItemsMap.put(itemID, alternateItemsElementList);
			}//end while loop
		}
		
		Set complimentaryMapKeySet = complimentaryItemsMap.keySet();
		if(complimentaryMapKeySet!=null && complimentaryMapKeySet.size()>0){
		Iterator<String> compIterator = complimentaryMapKeySet.iterator();
			while(compIterator.hasNext()){
				ArrayList compItemsElementList = new ArrayList();
				String itemID = compIterator.next();
				ArrayList<Element> asscociatedItemIDList = complimentaryItemsMap.get(itemID);
				for(Element assItem:asscociatedItemIDList){
					String assID = assItem.getAttribute("AssociatedItemID");
					List<Element> itemDetailsList = XMLUtilities.getElements(itemDetailsListDoc.getDocumentElement(),"Item[@ItemID='"+assID+"']");
					if(null==itemDetailsList || itemDetailsList.size()<=0)
						continue;
					compItemsElementList.add(itemDetailsList.get(0));
				}
				complimentaryItemsMap.put(itemID, compItemsElementList);
			}//end while loop
		}
		
		Set upgradeMapKeySet = upgradeItemsMap.keySet();
		if(upgradeMapKeySet!=null && upgradeMapKeySet.size()>0){
		Iterator<String> upgradeIterator = upgradeMapKeySet.iterator();
			while(upgradeIterator.hasNext()){
				ArrayList upgradeItemsElementList = new ArrayList();
				String itemID = upgradeIterator.next();
				ArrayList<Element> asscociatedItemIDList = upgradeItemsMap.get(itemID);
				for(Element assItem:asscociatedItemIDList){
					String assID = assItem.getAttribute("AssociatedItemID");
					List<Element> itemDetailsList = XMLUtilities.getElements(itemDetailsListDoc.getDocumentElement(),"Item[@ItemID='"+assID+"']");
					if(null==itemDetailsList || itemDetailsList.size()<=0)
						continue;
					upgradeItemsElementList.add(itemDetailsList.get(0));
				}
				upgradeItemsMap.put(itemID, upgradeItemsElementList);
			}//end while loop
		}
		
		if((upgradeMapKeySet!=null && upgradeMapKeySet.size()>0) || (complimentaryMapKeySet!=null && complimentaryMapKeySet.size()>0) 
				|| (alternateMapKeySet!=null && alternateMapKeySet.size()>0)  || (replacementMapKeySet!=null && replacementMapKeySet.size()>0)) {
			List<Element> itemDetailsList = XMLUtilities.getElements(itemDetailsListDoc.getDocumentElement(),"Item");
			itemElementList.addAll(itemDetailsList);
		}
		itemListElemsMap.put(ITEM_LIST_KEY, itemElementList);
		
		allAssociatedItemsMap.put(REPLACEMENT_ITEMS_KEY, replacementItemsMap);
		allAssociatedItemsMap.put(ALTERNATE_ITEMS_KEY, alternateItemsMap);
		allAssociatedItemsMap.put(COMPLEMENTARY_ITEMS_KEY, complimentaryItemsMap);
		allAssociatedItemsMap.put(UPGRADE_ITEMS_KEY, upgradeItemsMap);
		allAssociatedItemsMap.put(ITEM_EXTN_KEY, itemExtnElemsMap);
		allAssociatedItemsMap.put(ITEM_LIST_KEY, itemListElemsMap);
		
		return allAssociatedItemsMap;
	}
	
	public static String getFormattedDescription(String desc) {
		if (null == desc || desc.equals(""))
			return "";
		String newDesc[] = desc.split(",");
		if (newDesc.length > 5) {
			String temp = "";
			for (int i=0; i<newDesc.length; i++) {
				if (i<4) {
					temp = temp + newDesc[i] + ",";
				}
				if (i==4) {
					temp = temp + newDesc[i];
					break;
				}
			}
			return temp;
		} else {
			return desc;
		}
	}
	
	/**
	 * This method truncates the ShortDescription 70 characters.
	 * JIRA - 3895
	 * @param shortDesc - Description that should be truncated
	 * @return short Desc - Truncated short description
	 */
	public static String getFormattedShortDescription(String shortDesc){
		int textLength = 70;
		
		
		//Handle null values
		if(shortDesc == null){
			return XPEDXConstants.EMPTY_STRING;
		}
		
		if(log != null && log.isDebugEnabled()){
			log.debug("Unformatted Short Description"+shortDesc);
		}
		//Truncate the Short Description to 70 characters
		if(shortDesc.length() > textLength){
			shortDesc = shortDesc.substring(0, textLength);
			shortDesc = shortDesc+XPEDXConstants.TAIL_END;
		}
		
		if(log != null && log.isDebugEnabled()){
			log.debug("Formatted Short Description"+shortDesc);
		}
		
		return shortDesc;
	}
	
	/**
	 * This method truncates the Long Description in the WebConfirmation Detail page to 80% length of Short Description. 
	 * It also applies tool tip to display the full text and restricts the number of bullets to 5.
	 * JIRA - 3895
	 * @param longDesc - Description to be truncated
	 * @param shortDesc - Description whose length should be used to arrive at 80th percentile in space
	 * @return longDesc - Truncated Long Description with tool tip
	 */
	public static String getFormattedLongDescription(String longDesc, String shortDesc){
		String startLineIndicator = "<ul>";
		String endLineIndicator = "</ul>";
		String startRowIndicator = "<li>";
		String startRowIndicatorToolTip = "<li title=\"#\">";
		String endRowIndicator = "</li>";
		String strTrunc = "";
		int rowCounter = 0;
		int maxBulletCounter = 5;
		int shortDescContainerLength = 54;
		
		//Handle null values
		if(longDesc == null){
			return XPEDXConstants.EMPTY_STRING;
		}
		
		if(shortDesc == null){
			shortDesc = "";
		}
		
		StringBuffer strformattedDescription = new StringBuffer(startLineIndicator);
		int shortDescLength = shortDesc.length();
		if(shortDescLength > shortDescContainerLength){
			shortDescLength = shortDescContainerLength;
		}
		
		int textLength = shortDescLength*4/5;
		
		//Split the Long Description based on <li> delimiter
	if(longDesc.length()>0){
		String[] strArrDesc = longDesc.split(startRowIndicator);
		
		if(strArrDesc == null){
			return longDesc;
		}
		
		for(int index=0;index<strArrDesc.length;index++){
			String strTmp = strArrDesc[index];
			if(strTmp.equals(startLineIndicator)|| strTmp.equals(endLineIndicator)){
				continue;
			}
			
			//Restrict the Long Description bullet points to 5
			if(rowCounter >= maxBulletCounter){
				break;
			}
			rowCounter++;
			if(strTmp.endsWith(endRowIndicator)){
				strTmp = strTmp.substring(0, strTmp.indexOf(endRowIndicator));
			}
			
			//Apply truncation and add tool tip
			if(strTmp.length() > textLength){
				strTrunc = strTmp.substring(0,textLength);
				String toolTip = startRowIndicatorToolTip.replaceFirst("[#]", strTmp);
				String newString = toolTip+strTrunc+XPEDXConstants.TAIL_END+endRowIndicator;
				strformattedDescription.append(newString);
			}else{
				strformattedDescription.append(startRowIndicator+strTmp+endRowIndicator);
			}
		}
		strformattedDescription.append(endLineIndicator);
	}
		String formattedDescription = strformattedDescription.toString();
		
		if(log != null && log.isDebugEnabled()){
			log.debug("Unformatted Description"+longDesc);
			log.debug("Formatted Description"+formattedDescription);
		}
		 
		return formattedDescription;
	}
	
	public static String getFormattedOrderNumber(Element orderElement)
	{
		StringBuffer sb = new StringBuffer();
		if(orderElement!=null)
		{
			String orderBranch = orderElement.getAttribute("ExtnOrderDivision");
			String legacyOrderNum = orderElement.getAttribute("ExtnLegacyOrderNo");
			if(legacyOrderNum !=null && legacyOrderNum.length() >0)
			{
				String generationNum = orderElement.getAttribute("ExtnGenerationNo");
				return getFormattedOrderNumber(orderBranch,legacyOrderNum,generationNum);
			}
			else
				return "";
		}
		return sb.toString().replaceAll("_M","");
	}
	public static String getFormattedOrderNumber(String orderBranch,String legacyOrderNum,String generationNum)
	{
		StringBuffer sb = new StringBuffer();
		
			
			
			if(orderBranch!=null && orderBranch.length()>0)
			{
				sb.append(orderBranch);
				sb.append("-");
			}
			if(legacyOrderNum!=null && legacyOrderNum.length()>0)
			{				
				sb.append(legacyOrderNum);				
				sb.append("-");
			}
			if(generationNum!=null && generationNum.length()>0)
			{
				if(generationNum.trim().length()==1)
				{
					generationNum="0"+generationNum;
				}				
				sb.append(generationNum);
				
			}
		//as per data the order number which come from legacy will not contain any -,_ or any character .
		return sb.toString().replaceAll("_M","");
	}
	
	public static void refreshMiniCart(IWCContext webContext,Element output,boolean isGetCompleteOrder,int maxElements)
	{
		refreshMiniCart(webContext,output,isGetCompleteOrder,false,maxElements);
	}
	
	//Added for Jira 3523
	public static boolean checkforNonEntitlement(ArrayList<String> allItemIds,IWCContext wcContext){
		boolean errorFlag = false;	
		ArrayList<Element> itemlist = new ArrayList<Element>();
		Document entitledItemsDoc;
		try {
			//Check for entitlements of items list.			
			entitledItemsDoc = XPEDXOrderUtils.getXpedxEntitledItemDetails(allItemIds, wcContext.getCustomerId(), wcContext.getStorefrontId(), wcContext);
			if(entitledItemsDoc!=null) {
				 itemlist  = SCXmlUtil.getElements(entitledItemsDoc.getDocumentElement(), "//Item");
			}
			//If all items are entitled, return false
			if(itemlist.size()==allItemIds.size()){
				return false;
			}else{//even if single item in the list is non-entitled, return true
				return true;
			}
			
	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}	
		return errorFlag;
	}
	//End of JIRA 3523
	
	
	public static void refreshMiniCart(IWCContext webContext,Element output,boolean isGetCompleteOrder,boolean readOrderLinesFromStart,int maxElements)
	{
		 List<String> itemAndTotalList=new ArrayList<String>();
		Element orderElement=null;
		 if(output == null)
		 {
			 itemAndTotalList.add("0");
		   	 itemAndTotalList.add("0");
		   	 itemAndTotalList.add(webContext.getDefaultCurrency());
		   	 XPEDXWCUtils.setObectInCache("CommerceContextHelperOrderTotal", itemAndTotalList);
		   	 XPEDXWCUtils.setObectInCache("OrderLinesInContext",new ArrayList<Element>());
		   	 //refreshMiniCartDisplay(webContext,orderElement,readOrderLinesFromStart,maxElements,true);
		   	 return;
		 }
		if(isGetCompleteOrder == false) 
		{
			
			if(output != null)
			{
				orderElement=(Element)output.getElementsByTagName("Order").item(0);
			}
		}
		else
		{
			orderElement=output;
		}
		
		 String orderTotal= SCXmlUtil.getXpathAttribute(orderElement, "//Order/Extn/@ExtnTotalOrderValue"); 
		 Element orderLinesEleme=(Element)orderElement.getElementsByTagName("OrderLines").item(0);
	   	 itemAndTotalList.add(orderTotal);
	   	 itemAndTotalList.add(orderLinesEleme.getAttribute("TotalNumberOfRecords"));
	   	 try
	   	 {
		   	 String currency=SCXmlUtil.getXpathAttribute(orderElement, "//Order/PriceInfo/@Currency");
		   	 if(YFCCommon.isVoid(currency))
		   	 {
		   		currency=webContext.getDefaultCurrency();
		   	 }
		   	 itemAndTotalList.add(currency);
	   	 }
	   	 catch(Exception e)
	   	 {
	   		 
	   		 e.printStackTrace();
	   		 log.error("Error while adding currency for minicart");
	   	 }
	   	 
	   	 XPEDXWCUtils.setObectInCache("CommerceContextHelperOrderTotal", itemAndTotalList);
	   	 XPEDXWCUtils.setObectInCache("OrderHeaderInContext", orderElement.getAttribute("OrderHeaderKey"));
	   	refreshMiniCartDisplay(webContext,orderElement,readOrderLinesFromStart,maxElements,true);
	}
	public static void refreshMiniCartDisplay(IWCContext webContext,Element orderElement,boolean readOrderLinesFromStart,int maxElements,boolean isOrderByModifyTs)
	{
		try
		{
				ArrayList<Element> majorLineElements = new ArrayList<Element>();
				if(orderElement != null)
				{
	            NodeList orderLines = (NodeList) XMLUtilities.evaluate("//Order/OrderLines/OrderLine", orderElement, XPathConstants.NODESET);
	            int length = orderLines.getLength();
	            boolean hasMore = false;
	            if(isOrderByModifyTs)
	            {
	            	for (int i = 0; i < length; i++) {
	            		Element currNode = (Element) orderLines.item(i);
	                    String lineKey = currNode.getAttribute("OrderLineKey");
	                    if(log.isDebugEnabled()){
	                    log.debug("linekey-->"+lineKey);
	                    }
	                    NodeList bundleParentLines = currNode.getElementsByTagName("BundleParentLine");
	                    if ( (bundleParentLines.getLength() == 0) )
	                    {	                        
	                            // Haven't hit the max number yet, so go ahead and add it to the list.
	                            majorLineElements.add(currNode);
	                    }
	                }
	            	
	            	Collections.sort(majorLineElements, new XPEDXMiniCartComparator());
	            	for (int i = majorLineElements.size()-1; i >0 ;) {
	            		if(i>4)
	            			majorLineElements.remove(i);
	            		else
	            			break;
	            		i=majorLineElements.size()-1;
	            	}
	            }
	            else if(readOrderLinesFromStart){
	            	for (int i = 0; i < length; i++) {
	            		Element currNode = (Element) orderLines.item(i);
	                    String lineKey = currNode.getAttribute("OrderLineKey");
	                    if(log.isDebugEnabled()){
		                    log.debug("linekey-->"+lineKey);
	                    }
	                    NodeList bundleParentLines = currNode.getElementsByTagName("BundleParentLine");
	                    if ( (bundleParentLines.getLength() == 0) &&
	                         (!OrderHelper.isCancelledLine(currNode)) )
	                    {
	                    	if(log.isDebugEnabled()){
	    	                    log.debug(lineKey+ " it's major line");
	                    	}
	                        if(majorLineElements.size() == maxElements)
	                        {
	                            // Already have accumulated the maximum number of line items to display.
	                            // Set the "More..." indicator and break out of the loop.
	                            hasMore = true;
	                            break;
	                        }
	                        else
	                        {
	                            // Haven't hit the max number yet, so go ahead and add it to the list.
	                            majorLineElements.add(currNode);
	                        }
	                    }
	                }
	            }
	            else{
	            	for (int i = length-1; i > -1; i--) {
	                    Element currNode = (Element) orderLines.item(i);
	                    String lineKey = currNode.getAttribute("OrderLineKey");
	                    if(log.isDebugEnabled()){
		                    log.debug("linekey-->"+lineKey);
	                    }
	                    NodeList bundleParentLines = currNode.getElementsByTagName("BundleParentLine");
	                    if ( (bundleParentLines.getLength() == 0))
	                    {
	                        
	                        if(majorLineElements.size() == maxElements)
	                        {
	                            // Already have accumulated the maximum number of line items to display.
	                            // Set the "More..." indicator and break out of the loop.
	                            hasMore = true;
	                            break;
	                        }
	                        else
	                        {
	                            // Haven't hit the max number yet, so go ahead and add it to the list.
	                            majorLineElements.add(currNode);
	                        }
	                    }
	                }
	            }
	            XPEDXWCUtils.setObectInCache("OrderLinesInContext", majorLineElements);
	            
			}
        } catch (Exception e) {
        	log.error("++++++++ Got exception while refreshing mini cart display +++++++++++++");
            e.printStackTrace();
        }
	}
	/*Begin - Changes made by Mitesh Parikh for JIRA 3581*/
	public String getBillTofromOrderElement(Element orderElement)
	{
		String billToId=null;	
		
		Element orderExtn=SCXmlUtil.getChildElement(orderElement, "Extn");
		if(orderExtn != null)
		{
			String customerDivision=orderExtn.getAttribute("ExtnCustomerDivision");		
			customerDivision= customerDivision.split("_")[0];
			String legacyCustomerNumber=orderExtn.getAttribute("ExtnCustomerNo");
			String billToSuffix=orderExtn.getAttribute("ExtnBillToSuffix");
			String envtId=orderExtn.getAttribute("ExtnEnvtId");
			String companyCode=orderExtn.getAttribute("ExtnCompanyId");
			
			billToId=customerDivision+"-"+legacyCustomerNumber+"-"+billToSuffix;
		}
		
		return billToId;
		
	}
	/*End - Changes made by Mitesh Parikh for JIRA 3581*/
		
	private static final Logger LOG = Logger.getLogger(XPEDXOrderUtils.class);
}
