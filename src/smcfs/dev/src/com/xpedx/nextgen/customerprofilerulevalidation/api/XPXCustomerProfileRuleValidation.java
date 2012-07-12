package com.xpedx.nextgen.customerprofilerulevalidation.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.customerprofilerulevalidation.api.XPXCustomerProfileRuleConstant.CustomerProfileRuleID;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * @author VChandra-tw
 * 
 */
public class XPXCustomerProfileRuleValidation implements YIFCustomApi {
	private String entryType = null;
	private static YIFApi api = null;
	private static YFCLogCategory log;
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXCreateChainedOrderService.xml";
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
	}
	static {
		try 
		{
			log = YFCLogCategory.instance(XPXCustomerProfileRuleValidation.class);
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {
			
			e1.printStackTrace();
		}
		
	}
	/**
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document invokeRuleValidation(YFSEnvironment env, Document inXML)
			throws Exception {
		log.beginTimer("XPXCustomerProfileRuleValidation.invokeRuleValidation");
		Element orderElement = inXML.getDocumentElement();		
		//String customerID = orderElement.getAttribute("BillToID");
		String customerID = orderElement.getAttribute("BuyerOrganizationCode");
		String enterpriseCode = orderElement.getAttribute("EnterpriseCode");
		entryType = orderElement.getAttribute("EntryType");
		YFCDocument customeListDoc = YFCDocument.createDocument("Customer");
		YFCElement customerElement = customeListDoc.getDocumentElement();
		customerElement.setAttribute("CustomerID", customerID);
		customerElement.setAttribute("OrganizationCode", enterpriseCode);

		HashMap<String, HashMap<String, String>> ruleMap = new HashMap<String, HashMap<String, String>>();
		XPXCustomerProfileRuleUtil util = new XPXCustomerProfileRuleUtil(inXML,ruleMap);
		getCustomeProfileRules(env, customeListDoc.getDocument(), ruleMap,orderElement);
		if (ruleMap.size() > 0) {
			if(ruleMap.containsKey("AcceptPriceOverRide") ||ruleMap.containsKey("PreventBackOrder")||ruleMap.containsKey("PriceDiscrepency")
					||ruleMap.containsKey("GrossTradingMargin")||ruleMap.containsKey("ItemNotAvailableForNextDayShipment")){
			callPnA(env,inXML,util);
			}
			validateCustomerProfileRules(env, inXML, ruleMap,util);
		}
		checkErrorAndStampHasErrorInOrderXml(inXML);
		log.endTimer("XPXCustomerProfileRuleValidation.invokeRuleValidation");
		return inXML;
	}
	
	private void callPnA(YFSEnvironment env, Document inXML, XPXCustomerProfileRuleUtil util) {
		//Document inXML = getOrderXML();
		SCXmlUtil.getString(inXML);
		util.callPAndAService(env,inXML);
	}

	/**
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document getCustomerRulesForOrder(YFSEnvironment env, Document inXML)
			throws Exception {
		log.beginTimer("getCustomerRulesForOrder");
		Element orderElement = inXML.getDocumentElement();	
		String customerIDs[] = null;
		String custID = null;
		//String customerID = orderElement.getAttribute("BillToID");..
		String customerID = orderElement.getAttribute("BuyerOrganizationCode");
		String enterpriseCode = orderElement.getAttribute("EnterpriseCode");
		String billToId = orderElement.getAttribute("BillToID");
		String sapId = orderElement.getAttribute("SAPCustomerID");
		entryType = orderElement.getAttribute("EntryType");
		YFCDocument customeListDoc = YFCDocument.createDocument("Customer");
		YFCElement customerElement = customeListDoc.getDocumentElement();
		customerElement.setAttribute("CustomerID", customerID);
		customerElement.setAttribute("OrganizationCode", enterpriseCode);

		HashMap<String, HashMap<String, String>> ruleMap = new HashMap<String, HashMap<String, String>>();
		if((billToId != null && !billToId.trim().equals("")) && (sapId != null && !sapId.trim().equals(""))){
			custID = billToId + ","+ sapId;
			customerIDs = custID.split(",");
			Document custContInputDoc = YFCDocument.createDocument("Customer").getDocument();
			Element inputElement = custContInputDoc.getDocumentElement();
			Element custElement = custContInputDoc.createElement("Customer");
			custElement.setAttribute("OrganizationCode", enterpriseCode );
			Element complexQuery = custContInputDoc.createElement("ComplexQuery");
			
			Element OrElem = custContInputDoc.createElement("Or"); 
			
			for(String contactId : customerIDs) {
				Element exp = custContInputDoc.createElement("Exp");
				exp.setAttribute("Name", "CustomerID");
				exp.setAttribute("Value", contactId);
				SCXmlUtil.importElement(OrElem, exp);
			}
			complexQuery.appendChild(OrElem);
			inputElement.appendChild(complexQuery);
			inputElement.appendChild(custElement);
			
			getCustomeProfileRulesParent(env, custContInputDoc, ruleMap, orderElement);
		}
		else
		{
			getCustomeProfileRules(env, customeListDoc.getDocument(), ruleMap, orderElement);
		}
		YFCDocument rulesDoc = YFCDocument.createDocument("Rules");
		YFCElement rulesElement = rulesDoc.getDocumentElement();
		if (ruleMap.size() > 0) {
			rulesElement.setAttribute("CustomerID", customerID);
			Set keySet = ruleMap.keySet();
			Iterator<String> keyIter =  keySet.iterator();
			while(keyIter.hasNext())
			{
				String ruleId = keyIter.next();
				if(!YFCCommon.isVoid(ruleId))
				{
					YFCElement ruleElem = rulesElement.createChild("Rule");
					ruleElem.setAttribute("RuleId", ruleId);
				}
			}
		}
		log.endTimer("getCustomerRulesForOrder");
		return rulesDoc.getDocument();
	}
	
	/* Gets Rule Ids of customer as well as Customer's Parent */
	
	private void getCustomeProfileRulesParent(YFSEnvironment env, Document custListXML,
			HashMap<String, HashMap<String, String>> ruleMap, Element orderElement) throws Exception {
		
		Document outputCustListDoc = null;
		   //Meaning it is not invoked in Order Place flow..need to update ship to customer profile details in environment object for non OP flow.
			
			  //These are calls for other customers in the hierarchy apart from the ShipTo 
		      Document template = createTemplateForCustomerList();
		      env.setApiTemplate("getCustomerList", template);
		      outputCustListDoc = api.invoke(env, "getCustomerList",custListXML);
		      env.clearApiTemplate("getCustomerList");
		ArrayList<Element> customers=SCXmlUtil.getElements(outputCustListDoc.getDocumentElement(), "Customer");
		for(int i=0;i<customers.size();i++)
		{
			saveRuleKeys(customers.get(i), ruleMap);
		}
		
		return;
	}
	
	private void getCustomeProfileRules(YFSEnvironment env, Document custListXML,
			HashMap<String, HashMap<String, String>> ruleMap, Element orderElement) throws Exception {
		
		Document outputCustListDoc = null;
		String customerId = null;
				
		//Retrive ship to customer details from env object if its in OP flow
		if("Y".equalsIgnoreCase(orderElement.getAttribute("OrderConfirmationFlow")))
		{		
		    outputCustListDoc = (Document)env.getTxnObject("ShipToCustomerProfile");
		    log.debug("The outputCustListDoc in XPXCustomerProfileRulesValidaton java class is: "+SCXmlUtil.getString(outputCustListDoc));
		}
		
		else if(outputCustListDoc == null)
		{
		   //Meaning it is not invoked in Order Place flow..need to update ship to customer profile details in environment object for non OP flow.
			
			if(custListXML!=null)
			{
				customerId = custListXML.getDocumentElement().getAttribute("CustomerID");
			}
		
		   if(customerId.equalsIgnoreCase(orderElement.getAttribute("BuyerOrganizationCode")))
		   {
			   outputCustListDoc = createGetCustomerListInput(env,customerId);
			   env.setTxnObject("ShipToCustomerProfile", outputCustListDoc);
		   }
		   else
		   {
			  //These are calls for other customers in the hierarchy apart from the ShipTo 
		      Document template = createTemplateForCustomerList();
		      env.setApiTemplate("getCustomerList", template);
		      outputCustListDoc = api.invoke(env, "getCustomerList",custListXML);
		      env.clearApiTemplate("getCustomerList");
		   }
		}
		Document parentCustListXML = saveRuleKeys(outputCustListDoc.getDocumentElement(), ruleMap);
		/*After Review , If customer has some rules no need to check for parents, This will help in suppressing some rules form parent
		 * 
		 */
		if (parentCustListXML != null){
			//Set the attribute to N in subsequent calls to the same method.
			if("Y".equalsIgnoreCase(orderElement.getAttribute("OrderConfirmationFlow")))
			{	
			   orderElement.setAttribute("OrderConfirmationFlow", "N");
			}
			getCustomeProfileRules(env, parentCustListXML, ruleMap, orderElement);
		}
		return;
	}
	private Document createGetCustomerListInput(YFSEnvironment env, String buyerOrgCode) throws Exception
	{
		YFCDocument getCustomerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
        getCustomerDetailsInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, buyerOrgCode);
                
        env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
		Document getCustomerDetailsOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerDetailsInputDoc.getDocument());
		
		log.debug("GETCustomerDetailsOutputDoc::"+SCXmlUtil.getString(getCustomerDetailsOutputDoc));
		  
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
        
        
		return getCustomerDetailsOutputDoc;
	}

	/* Tempalate to get Customer Profile Rule List and RuleID */
	private Document createTemplateForCustomerList() {
		YFCDocument customeListTemplateDoc = YFCDocument
				.createDocument("CustomerList");
		YFCElement customerListTemplateElement = customeListTemplateDoc
				.getDocumentElement();
		YFCElement customerList = customerListTemplateElement
				.createChild("Customer");
		customerList.setAttribute("ParentCustomerKey", "");
		customerList.setAttribute("OrganizationCode", "");
		customerList.setAttribute("CustomerID", "");
		customerList.setAttribute("CustomerKey", "");
		YFCElement extnList = customerList.createChild("Extn");
		extnList.setAttribute("ExtnSuffixType","");
	
		YFCElement customerRulesProfileList = extnList
				.createChild("XPXCustomerRulesProfileList");
		YFCElement customerRulesProfile = customerRulesProfileList
				.createChild("XPXCustomerRulesProfile");
		customerRulesProfile.setAttribute("RuleKey", "");
		customerRulesProfile.setAttribute("Param1", "");
		customerRulesProfile.setAttribute("Param2", "");
		customerRulesProfile.setAttribute("Param3", "");
		YFCElement XPXRuleDefnChild = customerRulesProfile
				.createChild("XPXRuleDefn");
		XPXRuleDefnChild.setAttribute("RuleKey", "");
		XPXRuleDefnChild.setAttribute("RuleID", "");
		XPXRuleDefnChild.setAttribute("B2BFlag", "");
		XPXRuleDefnChild.setAttribute("InteractiveFlag", "");
		customerRulesProfile.appendChild(XPXRuleDefnChild);
		customerRulesProfileList.appendChild(customerRulesProfile);
		extnList.appendChild(customerRulesProfileList);
		customerList.appendChild(extnList);
		return customeListTemplateDoc.getDocument();
	}
	/* Saves Rule IDs and param in a hashmap */
	private Document saveRuleKeys(Element custListDoc,
			HashMap<String, HashMap<String, String>> ruleMap) {
		Document parentCustListXML = null;
		Element custElement =null;
		Element organizationElement =custListDoc;
		int custLength = 1;
		NodeList customerList =null;
		if( organizationElement !=null && organizationElement.getNodeName().equals("Customer"))
			custElement=organizationElement;
		else
		{
			customerList = organizationElement
					.getElementsByTagName("Customer");
			custLength = customerList.getLength();
		}
		if (custLength > 0) {
			if(custElement ==null)
				custElement = (Element) customerList.item(0);
			String parentCustomerKey = custElement
					.getAttribute("ParentCustomerKey");
			
			if (!YFCCommon.isVoid(parentCustomerKey)) {
				parentCustListXML = customerListInputXMLForCustomerKey(parentCustomerKey);
			}
			NodeList extnList = custElement.getElementsByTagName("Extn");

			int extnLength = extnList.getLength();
			
			if (extnLength > 0) {
				Element extnElement = (Element) extnList.item(0);
				
				NodeList customerRulesProfileList = extnElement
						.getElementsByTagName("XPXCustomerRulesProfileList");
				Element customerRulesProfileNodeElement = (Element) customerRulesProfileList
						.item(0);
				NodeList customerRulesList = customerRulesProfileNodeElement
						.getElementsByTagName("XPXCustomerRulesProfile");
				int length = customerRulesList.getLength();
				
				if (length > 0) {
					for (int brandCounter = 0; brandCounter < length; brandCounter++) {
						Element customerRuleElement = (Element) customerRulesList
								.item(brandCounter);			
						
						HashMap<String, String> paramHm = new HashMap<String, String>();
						paramHm.put("Param1", customerRuleElement
								.getAttribute("Param1"));
						paramHm.put("Param2", customerRuleElement
								.getAttribute("Param2"));
						paramHm.put("Param3", customerRuleElement
								.getAttribute("Param3"));
						String ruleID = SCXmlUtil.getXpathAttribute(
								customerRuleElement, "XPXRuleDefn/@RuleID");
						/*Rules set on child customer will preempt parent customer rules*/
						if( ruleMap.containsKey(ruleID))
							continue;
						if(ruleApplicableForOrderEntryType(customerRuleElement))
							ruleMap.put(ruleID, paramHm);
					}
				}
			}
		}
		return parentCustListXML;
	}
	private Document customerListInputXMLForCustomerKey(String CustomerKey) {
		YFCDocument customeListDoc = YFCDocument.createDocument("Customer");
		YFCElement customerElement = customeListDoc.getDocumentElement();
		customerElement.setAttribute("CustomerKey", CustomerKey);
		return customeListDoc.getDocument();
	}
	/* validate all the customer profile rules against order XML */
	private void validateCustomerProfileRules(YFSEnvironment env, Document inXML,
			HashMap<String, HashMap<String, String>> ruleMap, XPXCustomerProfileRuleUtil util) throws Exception {
		HashMap<String, HashMap<String,String>> lineRulesMap = new HashMap<String, HashMap<String,String>>();
		
		//CLEAR ALL THE ERRORS FROM PREVIOUS RULE VALIDATION
		clearPreviousErrorElements(inXML);
		for(Entry<String,HashMap<String,String>> entry:ruleMap.entrySet()){
			CustomerProfileRuleID ruleid = CustomerProfileRuleID.valueOf(entry.getKey());
			switch (ruleid) {
			/* Order Line Level */
			case AcceptPriceOverRide:
				util.validateAcceptPriceOverRide(env,entry.getValue());
				break;
			/* Order Level */
			case PreventAutoPlace:
				util.validatePeventAutoPlace(env);
				break;
			/* Order Level */
			case HeaderCommentByCustomer:
				util.validateHeaderCommentByCustomer(env);
				break;
				//Added By Manas
				/* Order Level */
			case PlaceOrderOnWBHold:
				util.validateWebHoldCustomer(env);
				break;
			case DuplicatePO:
				util.validateDuplicatePO(env);
				break;
			case NonStandardShipMethod:
				util.validateNonStatndardShipMethod(env);
				break;
			case CustomerSelectedShipComplete:
				util.validateCustomerSelecteShipComplete(env);
				lineRulesMap.put(entry.getKey(), entry.getValue());
				break;
			case ValidShiptoZipCode:
				util.validateShipToZipCode(env);
				break;
			case ShipDateNotNextBusinessDay:
				util.validateShipDateNotNextBusinessDay(env);
				break;
			/*case IncorrectBuyerID:
				util.validateIncorrectBuyerId(env);
				break;*/
			case IncorrectETradingID:
				util.validateIncorrectEtradingId(env);
				break;
			case RequireCustomerPO:
				util.validateRequiredCustomerPO(env);
				break;	
			case PreventBackOrder:
				util.validatePreventBackOrder(env);
				break;				
			/* Order Line Level */
			case LineCommentsByCustomer:
			case RequiredCustomerLinePO:
			case RequireCustomerLineField1:
			case RequireCustomerLineField2:
			case RequireCustomerLineField3:
			case AllDeliveryDatesDoNotMatch:
//			case PreventBackOrder:
			case ItemNotAvailableForNextDayShipment:
			/*case LineLevelCodeByCustomer:
			case PreventPriceBelowCost:*/
			case PriceDiscrepency:
			/*case RequiredCustomerLineSequenceNo:
			case RequiredCustomerLineAccountNo:	
			case GrossTradingMargin:*/				
			/*case PartNumberValidation:*/
				lineRulesMap.put(entry.getKey(),entry.getValue());				
				break;
			default:
			}
		}
		util.validateOrderLines(env,lineRulesMap);
	}
	private void clearPreviousErrorElements(Document inXML) {
		Element ele = inXML.getDocumentElement();
		NodeList nl = ele.getElementsByTagName("Error");
		int length = nl.getLength();
		if (length > 0) {
			for (int counter = 0; counter < length; counter++) {
				ele.removeChild( nl.item(counter));
			}
		}
	}
	/* Add Attribute HasError in case error is found */
	private void checkErrorAndStampHasErrorInOrderXml(Document inXML) {
		NodeList nodeList = inXML.getElementsByTagName("Error");
		
		if (nodeList.getLength() > 0) {
			inXML.getDocumentElement().setAttribute("HasError", "Y");
		} else {
			inXML.getDocumentElement().setAttribute("HasError", "N");
		}
	}
	private boolean ruleApplicableForOrderEntryType(Element customerRuleElement){
		String interactiveFlag = SCXmlUtil.getXpathAttribute(
				customerRuleElement, "XPXRuleDefn/@InteractiveFlag");
		String b2bFlag = SCXmlUtil.getXpathAttribute(
				customerRuleElement, "XPXRuleDefn/@B2BFlag");
		boolean isEdiOrder= false;
		if(XPXCustomerProfileRuleConstant.XPX_EDI_ORDER.equalsIgnoreCase(entryType)){
			isEdiOrder = true;
		}
		if("Y".equalsIgnoreCase(b2bFlag) && isEdiOrder){
			return true;
		} 
		else if("Y".equalsIgnoreCase(interactiveFlag) && !isEdiOrder){
			return true;
		}
		return false;
	}
}