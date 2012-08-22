package com.xpedx.nextgen.customerprofilerulevalidation.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.customerprofilerulevalidation.api.XPXCustomerProfileRuleConstant.CustomerProfileRuleID;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.core.YFCObject;
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
	
	static {
		try {
			log = YFCLogCategory.instance(XPXCustomerProfileRuleValidation.class);
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {
			e1.printStackTrace();
		}	
	}
	
	/**
	 * 
	 * Applies Business Rules On The Order.
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	
	public Document invokeRuleValidation(YFSEnvironment env, Document inXML) throws Exception {
		
		Element orderElement = inXML.getDocumentElement();		
		String customerID = orderElement.getAttribute("BuyerOrganizationCode");
		String enterpriseCode = orderElement.getAttribute("EnterpriseCode");
		entryType = orderElement.getAttribute("EntryType");
		
		YFCDocument customeListDoc = YFCDocument.createDocument("Customer");
		YFCElement customerElement = customeListDoc.getDocumentElement();
		customerElement.setAttribute("CustomerID", customerID);
		customerElement.setAttribute("OrganizationCode", enterpriseCode);

		HashMap<String, HashMap<String, String>> ruleMap = new HashMap<String, HashMap<String, String>>();
		XPXCustomerProfileRuleUtil util = new XPXCustomerProfileRuleUtil(inXML,ruleMap);
		
		Document rulesDoc = getRulesDocFromEnv(env); 
		if (rulesDoc != null) {
			YFCDocument rulesDocYfc = YFCDocument.getDocumentFor(rulesDoc);
			getCustomerProfileRules(rulesDocYfc, ruleMap);
		} else {
			getCustomeProfileRules(env, customeListDoc.getDocument(), ruleMap, orderElement);
		}
		
		if (ruleMap.size() > 0) {
			if(ruleMap.containsKey("AcceptPriceOverRide") || ruleMap.containsKey("PreventBackOrder") || ruleMap.containsKey("PriceDiscrepency")
					|| ruleMap.containsKey("GrossTradingMargin") || ruleMap.containsKey("ItemNotAvailableForNextDayShipment")) {
				callPnA(env,inXML,util);
			}
			validateCustomerProfileRules(env, inXML, ruleMap, util);
		}
		checkErrorAndStampHasErrorInOrderXml(inXML);
		
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
		//String customerID = orderElement.getAttribute("BillToID");
		String customerID = orderElement.getAttribute("BuyerOrganizationCode");
		String enterpriseCode = orderElement.getAttribute("EnterpriseCode");
		entryType = orderElement.getAttribute("EntryType");
		YFCDocument customeListDoc = YFCDocument.createDocument("Customer");
		YFCElement customerElement = customeListDoc.getDocumentElement();
		customerElement.setAttribute("CustomerID", customerID);
		customerElement.setAttribute("OrganizationCode", enterpriseCode);

		HashMap<String, HashMap<String, String>> ruleMap = new HashMap<String, HashMap<String, String>>();
		getCustomeProfileRules(env, customeListDoc.getDocument(), ruleMap, orderElement);
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
					
					// To Get The Parameters
					HashMap<String, String> paramMap = new HashMap<String, String>();
					paramMap = ruleMap.get(ruleId);
					if (paramMap != null && paramMap.size() > 0) {
						String param1 = paramMap.get("Param1");
						String param2 = paramMap.get("Param2");
						String param3 = paramMap.get("Param3");
						if (!YFCObject.isNull(param1) && !YFCObject.isVoid(param1)) {
							ruleElem.setAttribute("Param1", param1);
						}
						if (!YFCObject.isNull(param2) && !YFCObject.isVoid(param2)) {
							ruleElem.setAttribute("Param2", param2);
						}
						if (!YFCObject.isNull(param3) && !YFCObject.isVoid(param3)) {
							ruleElem.setAttribute("Param3", param3);
						}
					}
				}
			}
		}
		log.endTimer("getCustomerRulesForOrder");
		return rulesDoc.getDocument();
	}
	
	/* Gets Rule Ids of customer as well as Customer's Parent */
	private void getCustomeProfileRules(YFSEnvironment env, Document custListXML,
			HashMap<String, HashMap<String, String>> ruleMap, Element orderElement) throws Exception {
		
		Document outputCustListDoc = null;
		String customerId = null;
		String orderConfirmationFlow = "";
		String getParentCustomer = "";
		
		if (orderElement.hasAttribute("OrderConfirmationFlow")) {
			orderConfirmationFlow = orderElement.getAttribute("OrderConfirmationFlow");
		}
		
		if (orderElement.hasAttribute("GetParentCustomer")) {
			getParentCustomer = orderElement.getAttribute("GetParentCustomer");
		}
			
		if(!"Y".equalsIgnoreCase(getParentCustomer) && "Y".equalsIgnoreCase(orderConfirmationFlow)) {	
			// Retrieve Ship To Customer Details From Environment In OP flow.
			outputCustListDoc = (Document)env.getTxnObject("ShipToCustomerProfile");
		    log.debug("XPXCustomerProfileRulesValidaton-ShipToCustomerProfile: " + SCXmlUtil.getString(outputCustListDoc));
		} else if (outputCustListDoc == null) {
			// Non OP Flow.
			if(custListXML!=null) {
				customerId = custListXML.getDocumentElement().getAttribute("CustomerID");
			}
			
			// Below Condition is required as this method is recursive.
		    if(!YFCObject.isNull(customerId) && customerId.equalsIgnoreCase(orderElement.getAttribute("BuyerOrganizationCode"))) {
			   outputCustListDoc = createGetCustomerListInput(env,customerId);
			   env.setTxnObject("ShipToCustomerProfile", outputCustListDoc);
		    } else {
			   // These are calls for other customers in the hierarchy apart from the ShipTo 
		       env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
		       outputCustListDoc = api.invoke(env, "getCustomerList",custListXML);
		       env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		    }  
		}
		
		// Prepares Rule Map and returns the parent Customer InXML.
		Document parentCustListXML = saveRuleKeys(outputCustListDoc, ruleMap);

		if (parentCustListXML != null) {
			orderElement.setAttribute("GetParentCustomer", "Y");
			getCustomeProfileRules(env, parentCustListXML, ruleMap, orderElement);
		} 
	}
	
	private Document createGetCustomerListInput(YFSEnvironment env, String buyerOrgCode) throws Exception {
		
		YFCDocument getCustomerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
        getCustomerDetailsInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, buyerOrgCode);
                
        env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
		Document getCustomerListOutDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerDetailsInputDoc.getDocument());
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		
		log.debug("XPXCustomerProfileRuleValidation-customerOutDoc:" + SCXmlUtil.getString(getCustomerListOutDoc));
		return getCustomerListOutDoc;
	}
	
	/* Saves Rule IDs and param in a hashmap */
	private Document saveRuleKeys(Document custListDoc, HashMap<String, HashMap<String, String>> ruleMap) {
		
		Document parentCustListXML = null;
		
		YFCDocument yfcDoc = YFCDocument.getDocumentFor(custListDoc);
		YFCElement customerListElem = yfcDoc.getDocumentElement();
		
		YFCElement customerElem = customerListElem.getChildElement("Customer");
		if (customerElem != null) {
			
			String parentCustomerKey = customerElem.getAttribute("ParentCustomerKey");
			if (!YFCCommon.isVoid(parentCustomerKey)) {
				parentCustListXML = customerListInputXMLForCustomerKey(parentCustomerKey);
			}
			YFCElement custExtnElem = customerElem.getChildElement("Extn");
			if (custExtnElem != null) {
				YFCElement custRulesProfileListElem = custExtnElem.getChildElement("XPXCustomerRulesProfileList");
				if (custRulesProfileListElem != null) {
					YFCIterable<YFCElement> custRulesProfileItr = custRulesProfileListElem.getChildren("XPXCustomerRulesProfile");
					while (custRulesProfileItr.hasNext()) {
						
						String ruleID = "";
						YFCElement custRulesProfileElem = custRulesProfileItr.next();
						
						HashMap<String, String> paramMap = new HashMap<String, String>();
						paramMap.put("Param1", custRulesProfileElem.getAttribute("Param1"));
						paramMap.put("Param2", custRulesProfileElem.getAttribute("Param2"));
						paramMap.put("Param3", custRulesProfileElem.getAttribute("Param3"));
						YFCElement ruleDefnElem = custRulesProfileElem.getChildElement("XPXRuleDefn");						
						if (ruleDefnElem != null) {
							ruleID = ruleDefnElem.getAttribute("RuleID");
						}

						// Rules Set On Child Customer Will Preempt Parent Customer Rules.
						if( YFCObject.isNull(ruleID) || YFCObject.isVoid(ruleID) || ruleMap.containsKey(ruleID))
							continue;
						if(ruleApplicableForOrderEntryType(ruleDefnElem))
							ruleMap.put(ruleID, paramMap);
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
		
		// CLEAR ALL THE ERRORS FROM PREVIOUS RULE VALIDATION
		clearPreviousErrorElements(inXML);
		for(Entry<String,HashMap<String,String>> entry:ruleMap.entrySet()) {
			CustomerProfileRuleID ruleid = CustomerProfileRuleID.valueOf(entry.getKey());
			switch (ruleid) {
			/* Order Line Level */
			case AcceptPriceOverRide:
				util.validateAcceptPriceOverRide(env,entry.getValue());
				break;
			/* Order Level */
			/*case PreventAutoPlace:
				util.validatePeventAutoPlace(env);
				break;*/
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
			/*case NonStandardShipMethod:
				util.validateNonStatndardShipMethod(env);
				break;*/
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
	
	private boolean ruleApplicableForOrderEntryType(YFCElement ruleDefnElem) {
		
		String interactiveFlag = null;
		String b2bFlag = null;
		
		if (ruleDefnElem.hasAttribute("InteractiveFlag")) {
			interactiveFlag = ruleDefnElem.getAttribute("InteractiveFlag");
		}
		if (ruleDefnElem.hasAttribute("B2BFlag")) {
			b2bFlag = ruleDefnElem.getAttribute("B2BFlag");
		}
		
		boolean isEDIOrder = false;
		if(XPXCustomerProfileRuleConstant.XPX_EDI_ORDER.equalsIgnoreCase(entryType)) {
			isEDIOrder = true;
		}
		if((!YFCObject.isNull(b2bFlag) && "Y".equalsIgnoreCase(b2bFlag)) && isEDIOrder) {
			return true;
		} else if((!YFCObject.isNull(interactiveFlag) && "Y".equalsIgnoreCase(interactiveFlag)) && !isEDIOrder) {
			return true;
		} else {
			return false;
		}
	}
	
	private void getCustomerProfileRules (YFCDocument rulesDoc, HashMap<String, HashMap<String, String>>  ruleMap) {
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		YFCElement rulesElement = rulesDoc.getDocumentElement();
		YFCIterable<YFCElement> ruleItr = rulesElement.getChildren("Rule");
		while (ruleItr.hasNext()) {
			
			String ruleID = null;
			YFCElement ruleElem = ruleItr.next();
			if (ruleElem.hasAttribute("Param1")) {
				String param1 = ruleElem.getAttribute("Param1");
				paramMap.put("Param1", param1);
			}
			if (ruleElem.hasAttribute("Param2")) {
				String param2 = ruleElem.getAttribute("Param2");
				paramMap.put("Param2", param2);
			}
			if (ruleElem.hasAttribute("Param3")) {
				String param3 = ruleElem.getAttribute("Param3");
				paramMap.put("Param3", param3);
			}
			
			if (ruleElem.hasAttribute("RuleId")) {
				ruleID = ruleElem.getAttribute("RuleId");
			}
			
			if (!YFCObject.isNull(ruleID) && !YFCObject.isVoid(ruleID) && paramMap != null && !ruleMap.containsKey(ruleID)) {
				ruleMap.put(ruleID, paramMap);
			}
		}		
	}
	
	private Document getRulesDocFromEnv (YFSEnvironment env) {
		
		Document rulesDoc = null;
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				// Order Placed From Web. Ship To Customer Profile Has Been Set In Environment By WC Application.
				rulesDoc = (Document) envVariablesmap.get("CustomerRulesDoc");
			} 
		}
		return rulesDoc;
	}
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
	}
}