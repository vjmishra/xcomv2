package com.xpedx.nextgen.dashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * Description: OP - Updates the Customer order details with WebConfirmationNumber , Web Line Number and other information for the order and line level.
 * Creates a fulfillment order for the customer order and sends the information to Legacy System. 
 * 
 * @author JStaney.
 * 
 */

public class XPXInvokeOrderPlaceActions implements YIFCustomApi {
	
	private static YFCLogCategory log;
	private static YIFApi api = null;
	private Properties arg0;
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXCreateChainedOrderService.xml";
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {
			log.error("API initialization error");
		}
	}
	
	public Document invokeActions(YFSEnvironment env, Document inputXML) {
		
		log.debug("XPXInvokeOrderPlaceActions-InXML: "+ SCXmlUtil.getString(inputXML));		
		String entryType = null;
		Document getCustomerProfileDetailsDoc = null;
		boolean webOrder=false;
		try
		{	
		   YFCDocument yfcDoc = YFCDocument.getDocumentFor(inputXML);
		   YFCElement rootElem = yfcDoc.getDocumentElement();
		   
		   // Set Flag To Differentiate Order Confirmation Flow From Re-process Agent Flow.
		   rootElem.setAttribute("OrderConfirmationFlow","Y");
		   
		   // Retrieving Entry Type To Determine The Source Of Request.(Web or COM)
		   if (rootElem.hasAttribute("EntryType")) {
			   entryType = rootElem.getAttribute("EntryType");
			   if (!YFCObject.isNull(entryType) && entryType.equalsIgnoreCase(XPXLiterals.SOURCE_WEB)) {
				   webOrder= true;
			   } 
		   }
		   
		   // To Retrieve ShipTo Customer Information.
		   if (webOrder) {
			   setOPOrderDetailsFromEnv(env,rootElem);			   
			   // Commenting the code as extn Atrributes needs to be analyzed before picking it up from txn object.
			   // getCustomerProfileDetailsDoc = getCustomerProfileDocFromEnv(env);
		   } 
		   removeSpecialCharacters(env,rootElem);
		   
		   if (getCustomerProfileDetailsDoc == null) {
			   // To Pull ShipToProfile From DB For COM Or If WC Application Didn't Set It In Transaction.
			   if (log.isDebugEnabled()) {
				   log.debug("ShipToCustomerProfile Isn't Set In Environment. Need To Retrieve The Information From DB.");
			   }
			   String buyerOrgCode = rootElem.getAttribute("BuyerOrganizationCode");
			   if (!YFCObject.isNull(buyerOrgCode) && !YFCObject.isVoid(buyerOrgCode)) {
				   // Getting Ship To Customer Profile Details And Setting In As Transaction Object Which Will Be Used Throughout OP flow.
				   getCustomerProfileDetailsDoc = createGetCustomerListInput(env,buyerOrgCode);
				   env.setTxnObject("ShipToCustomerProfile", getCustomerProfileDetailsDoc);				   
			   }
		   } else {
			   // Do not Remove This Code as it will be required when we get the ShipToCustomerProfileDoc from transaction.
			   env.setTxnObject("ShipToCustomerProfile", getCustomerProfileDetailsDoc);
		   }
		   
		   if (log.isDebugEnabled()) {
		   		log.debug("ShipTo Customer Profile Doc: "+SCXmlUtil.getString(getCustomerProfileDetailsDoc));
		   }
		   
		   //Update Web Confirmation Number And Create Special Charge Line.
		   prepareChangeOrderDoc(env, rootElem, entryType, webOrder);
 		   
		   if (log.isDebugEnabled()) {
			   log.debug("XPXInvokeRulesEngineAndLegacyOrderCreationService-InXML:" + SCXmlUtil.getString(inputXML)); 
		   }
		   
		   // Invoke Business Rules And Send The Fulfillment Order To Legacy.
		   api.executeFlow(env, "XPXInvokeRulesEngineAndLegacyOrderCreationService", inputXML);
 
		} catch (NullPointerException ne) {	
			ne.printStackTrace();
			prepareErrorObject(ne, XPXLiterals.OP_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inputXML);
            return inputXML;
    	} catch (YFSException yfe) {
    		yfe.printStackTrace();
    		prepareErrorObject(yfe, XPXLiterals.OP_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inputXML);
    		return inputXML;
		} catch(Exception e) {
			e.printStackTrace();
			prepareErrorObject(e, XPXLiterals.OP_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inputXML);
	        return inputXML;
		}
		
		if (log.isDebugEnabled()) {
			   log.debug("XPXInvokeOrderPlaceActions-OutXML:" + SCXmlUtil.getString(inputXML)); 
		}
		return inputXML;
	}
	
	private void removeSpecialCharacters(YFSEnvironment env, YFCElement rootElem) {
		// TODO Auto-generated method stub
		int instructionListSize = 0;
		YFCNodeList<YFCElement> instructionsList = rootElem.getElementsByTagName("Instruction");
		if (instructionsList != null && instructionsList.getLength() > 0) {
		instructionListSize = instructionsList.getLength();
		}
	    for (int i=0;i<instructionListSize;i++) {
	    	YFCElement instructionElement = instructionsList.item(i);     
	        if (instructionElement.hasAttribute("InstructionType")) {
	        	String instructionType = instructionElement.getAttribute("InstructionType");           
	        	if(!YFCObject.isNull(instructionType) && (instructionType.equalsIgnoreCase("HEADER") || instructionType.equalsIgnoreCase("LINE"))) {
	        		String comments = instructionElement.getAttribute("InstructionText");
	        		if(comments != null) {
	        			if((comments.indexOf("\n") != -1 || comments.indexOf("\r\n") != -1))
	        			{
	        				comments = comments.replaceAll("\n|\r\n", " ");
	        			}
	        			comments = comments.replaceAll("[^\\x00-\\x7f]",""); // Replaces All Non Ascii Characters (EB-343)
	        			log.info("comments when placing the order"+comments);
	        			instructionElement.setAttribute("InstructionText", comments);
	        		}
	        	}
	        }
	    }
	}

	private Document createGetCustomerListInput(YFSEnvironment env, String buyerOrgCode) throws Exception {
		
		YFCDocument getCustomerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
        getCustomerDetailsInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, buyerOrgCode);
                
        env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
		Document getCustomerDetailsOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerDetailsInputDoc.getDocument());
	
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		return getCustomerDetailsOutputDoc;
	}
	
	private void prepareChangeOrderDoc(YFSEnvironment env, YFCElement rootElem, String entryType, boolean webOrder) {
		
		String webConfirmationNumber = null;
		String buyerOrganizationCode = null;
		String orderHeaderKey = null;
		String enterpriseCode = null;
		String envtCode = null;
		String[] custOrderAmountArray = new String[3];
		
		if (log.isDebugEnabled()) {
			log.debug("XPXInvokeOrderPlaceActions_prepareChangeOrderDoc()-InXML:" + rootElem.getString());
		}

		orderHeaderKey = rootElem.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
		buyerOrganizationCode = rootElem.getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
		enterpriseCode = rootElem.getAttribute(XPXLiterals.A_ENTERPRISE_CODE);
		if (!YFCObject.isNull(buyerOrganizationCode)
				&& !YFCObject.isVoid(buyerOrganizationCode)) {
			String[] splitArrayOnBuyerOrgCode = buyerOrganizationCode
					.split("-");
			if (splitArrayOnBuyerOrgCode.length > 2) {
				envtCode = splitArrayOnBuyerOrgCode[3];
			} else {
				envtCode = "";
			}
		}
		
		// To Generate Web confirmation Number.
		webConfirmationNumber = generateWebConfirmationNumber(orderHeaderKey, envtCode, entryType);
		
		// To Get Max Order Amount, Min Order Amount And Charge Amount.
		if (webOrder) {
			custOrderAmountArray = getCustOrderAmountListFromEnv(env, custOrderAmountArray, buyerOrganizationCode, enterpriseCode);
		} else {
			custOrderAmountArray = getCustOrderAmountList(env, buyerOrganizationCode, enterpriseCode);
		}
		
		updatePriceInformation(env, webConfirmationNumber, envtCode, rootElem, custOrderAmountArray, entryType, enterpriseCode);
	}
	
	public String generateWebConfirmationNumber(String orderHeaderKey, String envtCode, String entryType) {

		String webConfirmationNumber = "";
		String uniqueSequence = "";
		String year = ""; 
		String month = "";
		String day = "";
		int uniqueSequenceLength = 7;
		int orderHeaderKeylength = 0;
		
		if (!YFCObject.isNull(orderHeaderKey)) {
			orderHeaderKeylength = orderHeaderKey.trim().length();
		}
		
		YFCDate currentSystemDate = new YFCDate();
		String currentSystemDateString = currentSystemDate.toString();
		year = currentSystemDateString.substring(2,4);
		month = currentSystemDateString.substring(4,6);
		day = currentSystemDateString.substring(6,8);
		
		if (orderHeaderKeylength > 8) {
			int startIndex = orderHeaderKeylength-uniqueSequenceLength;
			uniqueSequence = orderHeaderKey.substring(startIndex);
		}	

		if (entryType != null
				&& (XPXLiterals.SOURCE_TYPE_B2B.equals(entryType)
						|| XPXLiterals.SOURCE_WEB.equals(entryType) || XPXLiterals.SOURCE_COM.equals(entryType))) {
			// Order Is Placed From B2B, WEB or COM.
			envtCode = XPXLiterals.SYSTEM_SPECIFIER_WEB;
		}
		
		webConfirmationNumber = year + month + day + envtCode + uniqueSequence;
		if (log.isDebugEnabled()) {
			log.debug("Web Confirmation Number: " + webConfirmationNumber);
		}
		return webConfirmationNumber;
	}
	
	private String[] getCustOrderAmountList(YFSEnvironment env, String customerId, String enterpriseCode) {
		
		String[] custOrderAmountArray = new String[2];
		try
		{			
			float minOrderAmount = 0;
			float chargeAmount = 0;
			Document shipToCustomerProfileDoc = null;
			YFCElement customerListElem = null;			
			String applyMinOrderBrands=null;
			
			shipToCustomerProfileDoc = (Document) env.getTxnObject("ShipToCustomerProfile");
			
			if(shipToCustomerProfileDoc != null) {
				customerListElem = YFCDocument.getDocumentFor(shipToCustomerProfileDoc).getDocumentElement();
			} else {
				//Form The Input
				YFCDocument inputCustomerDoc = YFCDocument.createDocument("Customer");
				YFCElement inputCustomerElement = inputCustomerDoc.getDocumentElement();
				inputCustomerElement.setAttribute("CustomerID", customerId);
				inputCustomerElement.setAttribute("OrganizationCode", enterpriseCode);
				//Form The Template
				YFCDocument customerListTemplate = YFCDocument.createDocument("CustomerList");
				YFCElement customerListTemplateElement = customerListTemplate.getDocumentElement();
				
				YFCElement customerTemplateElement = customerListTemplate.createElement("Customer");
				customerListTemplateElement.appendChild(customerTemplateElement);			
				YFCElement extnTemplateElement = customerListTemplate.createElement("Extn");			
				customerTemplateElement.appendChild(extnTemplateElement);
				
				YFCElement parentCustomerTemplateElement = customerListTemplate.createElement("ParentCustomer");
				YFCElement parentCustomerExtnTemplateElement = customerListTemplate.createElement("Extn");
				parentCustomerTemplateElement.appendChild(parentCustomerExtnTemplateElement);
				
				customerTemplateElement.appendChild(parentCustomerTemplateElement);
				env.setApiTemplate("getCustomerList", customerListTemplate.getDocument());
							
				customerListElem = YFCDocument.getDocumentFor(api.invoke(env, "getCustomerList", inputCustomerDoc.getDocument())).getDocumentElement();
			}
						
			YFCElement customerElem = customerListElem.getChildElement("Customer");

			if (customerElem != null) {
				
				YFCElement extnElement = customerElem.getChildElement("Extn");
				String minOrderAmountStr=extnElement.getAttribute("ExtnMinOrderAmount");
				String chargeAmountStr=extnElement.getAttribute("ExtnMinChargeAmount");
				String shipFromDivision =extnElement.getAttribute("ExtnShipFromBranch");
				String envCode =extnElement.getAttribute("ExtnEnvironmentCode");
				Document getOrganizationListInDoc=null;
				Document organizationListOutDoc=null;
				boolean isGetOrganizationListAPICalled=false;
				
				String applyMinOrderCharge_GlobalLevel=YFSSystem.getProperty("applyMinOrderCharge");//Property read from customer_overrides.properties file and it indicates whether minimum order charge should be added from NG or MAX.
				if("Y".equalsIgnoreCase(applyMinOrderCharge_GlobalLevel)) {
					isGetOrganizationListAPICalled=true;
					env.setApiTemplate("getOrganizationList",SCXmlUtil.createFromString(""
							+" <OrganizationList><Organization OrganizationName=\"\">"														
							+"<Extn ExtnMinOrderAmt=\"\" ExtnSmallOrderFee=\"\" ExtnApplyMinOrderBrands=\"\"/>"
						    +"</Organization>"
						    +"</OrganizationList>"));
	
					getOrganizationListInDoc = SCXmlUtil.createFromString(""+ "<Organization OrganizationCode=\""+shipFromDivision+"_"+envCode +"\"/> ");
					organizationListOutDoc = api.invoke(env, "getOrganizationList", getOrganizationListInDoc);				
					env.clearApiTemplate("getOrganizationList");					
				
					if(!YFCCommon.isVoid(organizationListOutDoc)) {
						applyMinOrderBrands = SCXmlUtil.getXpathAttribute(organizationListOutDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnApplyMinOrderBrands");
						
					}
				}
				
				if(minOrderAmountStr != null && !"".equals(minOrderAmountStr)  && !"0".equals(minOrderAmountStr) && !"0.00".equals(minOrderAmountStr)) {
					minOrderAmount = Float.parseFloat(minOrderAmountStr);	
					if(chargeAmountStr != null && !"".equals(chargeAmountStr)) {
						chargeAmount = Float.parseFloat(chargeAmountStr);
					}
				} else {
					if(chargeAmountStr != null && !"".equals(chargeAmountStr)){
						chargeAmount = Float.parseFloat(chargeAmountStr);
					}
					
					YFCElement parentElement = customerElem.getChildElement("ParentCustomer");
					if (parentElement != null) {
						YFCElement extnParentElement = parentElement.getChildElement("Extn");
						if (extnParentElement != null) {
							minOrderAmountStr=extnParentElement.getAttribute("ExtnMinOrderAmount");
							chargeAmountStr=extnParentElement.getAttribute("ExtnMinChargeAmount");
						}

						if (minOrderAmountStr != null && (!("".equals(minOrderAmountStr))) && (!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) )) {
							minOrderAmount = Float.parseFloat(minOrderAmountStr);	
							if(chargeAmount <= 0 && chargeAmountStr !=null && (!"".equals(chargeAmountStr))) {
								chargeAmount = Float.parseFloat(chargeAmountStr);
							}
						} else {
							if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)) && chargeAmount <=0) {
								chargeAmount = Float.parseFloat(chargeAmountStr);
							}
														
							try {
								if(!isGetOrganizationListAPICalled) {
									env.setApiTemplate("getOrganizationList",SCXmlUtil.createFromString(""
														+" <OrganizationList><Organization OrganizationName=\"\">"														
														+"<Extn ExtnMinOrderAmt=\"\" ExtnSmallOrderFee=\"\"/>"
													    +"</Organization>"
													    +"</OrganizationList>"));
								
									getOrganizationListInDoc = SCXmlUtil.createFromString(""+ "<Organization OrganizationCode=\""+shipFromDivision+"_"+envCode +"\"/> ");
									organizationListOutDoc = api.invoke(env, "getOrganizationList", getOrganizationListInDoc);	
									env.clearApiTemplate("getOrganizationList");
								}
								
								if(YFCCommon.isVoid(organizationListOutDoc)){
									log.error("Organization Details Doesn't Exist For Node: "+ shipFromDivision+"_"+envCode+". ");
									return custOrderAmountArray;
								}
								
								minOrderAmountStr = SCXmlUtil.getXpathAttribute(organizationListOutDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnMinOrderAmt");
								chargeAmountStr = SCXmlUtil.getXpathAttribute(organizationListOutDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnSmallOrderFee");								
								
								if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr))) && (!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) )) {
									minOrderAmount = Float.parseFloat(minOrderAmountStr);				
									if( chargeAmount <=0 && chargeAmountStr !=null && (!"".equals(chargeAmountStr))) {
											chargeAmount = Float.parseFloat(chargeAmountStr);
									}
								}								
							} catch (Exception e) {
								log.error("Organization Details Doesn't Exist For Node: "+ shipFromDivision+"_"+envCode+". ", e);
								return custOrderAmountArray;
							}
						}
					}
				}
			}
			custOrderAmountArray[0] = ""+minOrderAmount;
			custOrderAmountArray[1] = ""+chargeAmount;
			custOrderAmountArray[2] = (applyMinOrderBrands!=null ? applyMinOrderBrands : "");
			
	} catch (Exception ex) {
		log.error(ex.getMessage());
	}
		return 	custOrderAmountArray;
	}
	
	private void updatePriceInformation(YFSEnvironment env, String webConfirmationNumber, String envtCode, 
			YFCElement rootElem, String[] custOrderAmountArray, String entryType, String enterpriseCode) {
		
		if (log.isDebugEnabled()) {
			log.debug("XPXInvokeOrderPlaceActions_updatePriceInformation()-InXML:" + rootElem.getString());
		}
			
		String webLineNumber = null;
		String minOrderTotalStr = null;
		String chargeAmountStr = null;
		String applyMinOrderBrands_DivisionLevel = null;
		float minOrderTotal = 0;
		float chargeAmount = 0;
		float totalAmount = 0;
		XPXUtils utilObj = null;
		
		minOrderTotalStr = custOrderAmountArray[0];
		chargeAmountStr = custOrderAmountArray[1];
		applyMinOrderBrands_DivisionLevel = custOrderAmountArray[2];
		
		if (log.isDebugEnabled()) {
			log.debug("XPXInvokeOrderPlaceActions_updatePriceInformation() - Value of minOrderTotalStr, chargeAmountStr & applyMinOrderBrands_DivisionLevel are : ["+minOrderTotalStr+"], ["+ chargeAmountStr+"] and ["+applyMinOrderBrands_DivisionLevel+ "]");
		}
		
		if (!YFCObject.isNull(minOrderTotalStr) && !YFCObject.isVoid(minOrderTotalStr)) {
		     minOrderTotal = Float.parseFloat(minOrderTotalStr);
		}
		if (!YFCObject.isNull(chargeAmountStr) && !YFCObject.isVoid(chargeAmountStr)) {
		     chargeAmount = Float.parseFloat(chargeAmountStr);
		}
		
		// To Set Order Level Attributes.
		//rootElem.setAttribute(XPXLiterals.A_ENTERPRISE_CODE, XPXLiterals.A_ORGANIZATIONCODE_VALUE);
		rootElem.setAttribute(XPXLiterals.A_DOCUMENT_TYPE, "0001");
		rootElem.setAttribute(XPXLiterals.A_OVERRIDE, XPXLiterals.BOOLEAN_FLAG_Y);
		rootElem.setAttribute(XPXLiterals.A_ACTION, XPXLiterals.MODIFY);
		rootElem.setAttribute(XPXLiterals.A_ORDER_TYPE, XPXLiterals.CUSTOMER);
		
		// To Set Order Extended Attributes.
		YFCElement extnOrderElem = rootElem.getChildElement("Extn");
		extnOrderElem.setAttribute(XPXLiterals.A_WEB_CONF_NUMBER, webConfirmationNumber);
		extnOrderElem.setAttribute("ExtnOrderStatus", "1100.0100");
		extnOrderElem.setAttribute("ExtnOrderStatusPrefix", "");
		
		// Order Lines.
		YFCElement orderLinesElem = rootElem.getChildElement(XPXLiterals.E_ORDER_LINES);
		
		Document rootDoc = rootElem.getOwnerDocument().getDocument();     
		Element orderLines = (Element) rootDoc.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);
		
		ArrayList<Element> orderLinesListElements = SCXmlUtil.getChildren(orderLines, XPXLiterals.E_ORDER_LINE);		
		Collections.sort(orderLinesListElements, new XpedxPrimeLineNoComparator());		
		for (Iterator<Element> orderLineIterator = orderLinesListElements.iterator(); orderLineIterator.hasNext();) {
			
			Element orderLineElement = (Element) orderLineIterator.next();
			String orderLineKey = orderLineElement.getAttribute("OrderLineKey");
			webLineNumber = generateWebLineNumber(orderLineKey, envtCode, entryType);
			
			NodeList olExtnElemList = orderLineElement.getElementsByTagName("Extn");
			if (olExtnElemList != null && olExtnElemList.getLength() > 0) {
				Element orderLineExtnElem = (Element) olExtnElemList.item(0);
				orderLineExtnElem.setAttribute(XPXLiterals.A_WEB_LINE_NUMBER, webLineNumber);
			}
		}
		
		if (minOrderTotal != 0) {
			
			String orderTotal = null;
			if (extnOrderElem.hasAttribute("ExtnTotalOrderValue")) {
				orderTotal = extnOrderElem.getAttribute("ExtnTotalOrderValue");
				if (!YFCObject.isNull(orderTotal) && !YFCObject.isVoid(orderTotal)) {
					totalAmount = Float.parseFloat(orderTotal);
				}
			}
			String applyMinOrderCharge_GlobalLevel=YFSSystem.getProperty("applyMinOrderCharge");//Property read from customer_overrides.properties file and it indicates whether minimum order charge should be added from NG or MAX. 
			if(log.isDebugEnabled()) {
				log.debug("XPXInvokeOrderPlaceActions_updatePriceInformation() - Value of applyMinOrderCharge_GlobalLevel picked from customer_overrides property file is : ["+applyMinOrderCharge_GlobalLevel+"]");
				log.debug("XPXInvokeOrderPlaceActions_updatePriceInformation() - Value of applyMinOrderBrands_DivisionLevel picked from 'EXTN_APPLY_MIN_ORDER_BRANDS' column of YFS_ORGANIZATION table is : ["+applyMinOrderBrands_DivisionLevel+"]");
			}
		
			
			if (totalAmount < minOrderTotal) {
				if("Y".equalsIgnoreCase(applyMinOrderCharge_GlobalLevel)) {
					if(XPXUtils.isApplyMinimumOrderChargeForBrand(applyMinOrderBrands_DivisionLevel, enterpriseCode)) {						
						float extnSubtotal =0;
						utilObj = new XPXUtils();				
		
						double reTotalAmount = totalAmount + new Float(chargeAmount);
						extnOrderElem.setAttribute("ExtnTotOrdValWithoutTaxes", "" + reTotalAmount);
						extnOrderElem.setAttribute("ExtnTotalOrderValue", ""+reTotalAmount);
						
						String extnOrderSubTotal = extnOrderElem.getAttribute("ExtnOrderSubTotal");
						if(extnOrderSubTotal != null && !"".equals(extnOrderSubTotal)) {
							extnSubtotal = Float.parseFloat(extnOrderSubTotal)+ new Float(chargeAmount);
						}			
						extnOrderElem.setAttribute("ExtnOrderSubTotal", ""+extnSubtotal);
						
						// To Create A Special Charge Line.
						YFCElement specialLineElem = orderLinesElem.createChild("OrderLine");
						specialLineElem.setAttribute("OrderedQty", "1");
						specialLineElem.setAttribute("ValidateItem", "N");
						specialLineElem.setAttribute("LineType", "M");
						YFCElement itemElement = specialLineElem.createChild("Item");
						String itemID = arg0.getProperty("ItemID");
						if (YFCObject.isNull(itemID) || YFCObject.isVoid(itemID)) {
							itemID = "/05";
						}
						itemElement.setAttribute("ItemID", itemID);
						
						// Special Line Instruction Is Required Only When Order Has A Hold. In Other Case MAX Will Be Updating It Through OU Updates.
						if (utilObj.checkIfOrderOnPendingHold(rootDoc)) {
							if(YFSSystem.getProperty("ItemShortDesc") != null)
								itemElement.setAttribute("ItemShortDesc", YFSSystem.getProperty("ItemShortDesc"));			
						}
						
						YFCElement specialLineExtnElem = specialLineElem.createChild("Extn");					
						specialLineExtnElem.setAttribute("ExtnLineOrderedTotal", new Float(chargeAmount).toString());
						specialLineExtnElem.setAttribute("ExtnReqUOMUnitPrice",new Float(chargeAmount).toString());
						specialLineExtnElem.setAttribute("ExtnUnitPriceDiscount", "0");
						specialLineExtnElem.setAttribute("ExtnUnitPrice", new Float(chargeAmount).toString());
						specialLineExtnElem.setAttribute("ExtnExtendedPrice", new Float(chargeAmount).toString());
						specialLineExtnElem.setAttribute("ExtnAdjUOMUnitPrice", new Float(chargeAmount).toString());
						specialLineExtnElem.setAttribute("ExtnAdjUnitPrice", new Float(chargeAmount).toString());		
						specialLineExtnElem.setAttribute("ExtnPriceOverrideFlag", "Y"); 
						specialLineExtnElem.setAttribute("ExtnLineType", "STOCK");
						long uniqueSequenceNo = CallDBSequence.getNextDBSequenceNo(env, XPXLiterals.WEB_LINE_SEQUENCE);
						webLineNumber = generateWebLineNumberForSpecialCharge(entryType, uniqueSequenceNo,envtCode);
						specialLineExtnElem.setAttribute(XPXLiterals.A_WEB_LINE_NUMBER, webLineNumber);
						
						YFCElement linePriceInfoElem = specialLineElem.createChild("LinePriceInfo");
						linePriceInfoElem.setAttribute("UnitPrice", new Float(chargeAmount).toString());
						linePriceInfoElem.setAttribute("IsPriceLocked", "Y");
		
						setProgressYFSEnvironmentVariables(env);			
					}
				}
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("XPXInvokeOrderPlaceActions_updatePriceInformation()-OutXML:" + rootElem.getString());
		}
	}
	
	/**
	 * Gets the Min Order Amount and Charge amount from transaction object to form a array.
	 */
	
	private String[] getCustOrderAmountListFromEnv(YFSEnvironment env, String[] custOrderAmountArray, String buyerOrganizationCode, String enterpriseCode) {
		
		String minOrderAmount = null;
		String chargeAmount = null;
		String applyMinOrderBrands=null;
		
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				// Order Placed From Web. Ship To Customer Profile Has Been Set In Environment By WC Application.
				minOrderAmount = (String) envVariablesmap.get("ExtnMinOrderAmount");
				chargeAmount = (String) envVariablesmap.get("ExtnChargeAmount");
				
				applyMinOrderBrands = (String) envVariablesmap.get("ExtnApplyMinOrderBrands");
				
				if (log.isDebugEnabled()) {
					log.debug("MinOrderAmount:" + minOrderAmount);
					log.debug("ChargeAmount:" + chargeAmount);
					log.debug("applyMinOrderBrands_DivisionLevel:" + applyMinOrderBrands);
				}
			} 
		}
		
		if (!YFCObject.isNull(minOrderAmount) && !YFCObject.isNull(chargeAmount)) {
			custOrderAmountArray[0] = ""+minOrderAmount;
			custOrderAmountArray[1] = ""+chargeAmount;
			custOrderAmountArray[2] = (applyMinOrderBrands!=null ? applyMinOrderBrands : "");
		} else {
			custOrderAmountArray = getCustOrderAmountList(env, buyerOrganizationCode, enterpriseCode);
		}
		
		return custOrderAmountArray;
	}
	
	/**
	 * Generates Web Line Number for Special Charge Line with environment code and unique sequence number from database sequence.
	 */
	
	public static String generateWebLineNumberForSpecialCharge(String entryType,long uniqueSequenceNo,String envtCode) {
		
		String webLineNumber = "";
		String formatted = "";

		int uniqueSequenceLength = 8;
		int keyLength = String.valueOf(uniqueSequenceNo).length();
		if(keyLength < 8) {
			formatted = String.format("%08d", uniqueSequenceNo); 
		} else if(keyLength > 8) {
			int startIndex = keyLength-uniqueSequenceLength;
			formatted = String.valueOf(uniqueSequenceNo).substring(startIndex);
		} else {
			formatted = String.valueOf(uniqueSequenceNo);
		}

		if(entryType != null && (XPXLiterals.SOURCE_TYPE_B2B.equals(entryType) 
				|| XPXLiterals.SOURCE_WEB.equals(entryType) || XPXLiterals.SOURCE_COM.equals(entryType))) {
			envtCode = XPXLiterals.SYSTEM_SPECIFIER_WEB;
		}				
		
		webLineNumber = envtCode + formatted;
		return webLineNumber;
	}
	
	/**
	 * Generates Web Line Number for each line with environment code and subsequent 8 digits of Order Line Key.
	 * 
	 */
	
	public String generateWebLineNumber(String orderLineKey, String envtCode, String entryType) {

		String webLineNumber = "";
		String uniqueSequence = "";

		int uniqueSequenceLength = 8;
		int orderLineKeylength = 0;
		
		if (!YFCObject.isNull(orderLineKey)) {
			orderLineKeylength = orderLineKey.trim().length();
		}
		
		if (orderLineKeylength > 8) {
			int startIndex = orderLineKeylength-uniqueSequenceLength;
			uniqueSequence = orderLineKey.substring(startIndex);
		}	

		if(entryType != null && (XPXLiterals.SOURCE_TYPE_B2B.equals(entryType) 
				|| XPXLiterals.SOURCE_WEB.equals(entryType) || XPXLiterals.SOURCE_COM.equals(entryType))) {
			envtCode = XPXLiterals.SYSTEM_SPECIFIER_WEB;
		}
		
		webLineNumber = envtCode + uniqueSequence;
		if (log.isDebugEnabled()) {
			log.debug("Web Line Number: " + webLineNumber);
		}
		return webLineNumber;
	}
	
	/**
	 * Set the Flags in the environment object which will be used in beforeChangeOrderUE to determine whether price calculation is required.  
	 * 
	 */
	
	private void setProgressYFSEnvironmentVariables(YFSEnvironment env) {
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				envVariablesmap.put("isSpecialItem", "true");
				envVariablesmap.put("specialItemID", arg0.getProperty("ItemID"));
			}
			else{
				envVariablesmap=new HashMap();
				envVariablesmap.put("isSpecialItem", "true");
				envVariablesmap.put("specialItemID", arg0.getProperty("ItemID"));
			}
			
			clientVersionSupport.setClientProperties(envVariablesmap);
		}
	}
	
	private Document getCustomerProfileDocFromEnv (YFSEnvironment env) {
		
		Document getCustomerProfileDetailsDoc = null;
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				// Order Placed From Web. Ship To Customer Profile Has Been Set In Environment By WC Application.
				getCustomerProfileDetailsDoc = (Document) envVariablesmap.get("ShipToCustomerProfile");
				if (getCustomerProfileDetailsDoc != null) {
					envVariablesmap.remove("ShipToCustomerProfile");
					if (log.isDebugEnabled()) {
						log.debug("ShipToCustomerProfile Document From Environment:" + SCXmlUtil.getString(getCustomerProfileDetailsDoc));	
					}
				}
			} 
		}
		return getCustomerProfileDetailsDoc;
	}
	
	private void setOPOrderDetailsFromEnv(YFSEnvironment env, YFCElement rootElem) {
		
		HashMap orderDetailsMap = new HashMap();
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				// Order Placed From Web. Ship To Customer Profile Has Been Set In Environment By WC Application.
				orderDetailsMap = (HashMap) envVariablesmap.get("OPOrderDetailsMap");
				if (orderDetailsMap != null) {
					
					// Order Attributes.
					String custPONum = null;
					String reqDeliveryDate = null;
					// Order Extended Attributes.
					String deliveryHoldFlag = null;
					String rushOrderComments = null;
					String shipComplete = null;
					String willCall = null;
					String deliveryHoldDate = null;
					String deliveryHoldTime = null;
					String attentionName = null;
					String headerComments = null;
					String addnlEmailAddr = null;
					String rushOrderFlag = null;
					String webHoldFlag = null;
					String orderedByName = null;
					String shipNode = null;
					
					// Remove The Details From Environment As It is No Longer Required In The Environment.
					envVariablesmap.remove("OPOrderDetailsMap");
					if (log.isDebugEnabled()) {
						log.debug("OPOrderDetailsMap From SWC:" + orderDetailsMap);
					}
					
					// Order Atrributes.
					custPONum = (String) orderDetailsMap.get("CustomerPONo");
					reqDeliveryDate = (String) orderDetailsMap.get("ReqDeliveryDate");
					shipNode = (String) orderDetailsMap.get("ShipNode");
					
					if (!YFCObject.isNull(custPONum)) {
						rootElem.setAttribute("CustomerPONo", custPONum);
					}
					
					if (!YFCObject.isNull(reqDeliveryDate)) {
						rootElem.setAttribute("ReqDeliveryDate", reqDeliveryDate);
					}
					if (!YFCObject.isNull(shipNode)) {
						rootElem.setAttribute("ShipNode", shipNode);
					}
					//Order Hold Type element
					if(envVariablesmap.get("ApplyHoldonOrderDoc")!=null)
					{
						Element orderHoldTypesElement=(Element)envVariablesmap.get("ApplyHoldonOrderDoc");
						if(orderHoldTypesElement!=null){
							Document orderHoldTypesDoc=SCXmlUtil.createFromString(SCXmlUtil.getString(orderHoldTypesElement));
							YFCDocument yfsOrderHoldTypesDoc= YFCDocument.getDocumentFor(orderHoldTypesDoc);
							YFCElement existingOrderHldTypesElem=rootElem.getChildElement("OrderHoldTypes");
							if(existingOrderHldTypesElem !=null)
							{
								YFCElement orderHldTypeElem=yfsOrderHoldTypesDoc.getDocumentElement().getChildElement("OrderHoldType");
								existingOrderHldTypesElem.appendChild(existingOrderHldTypesElem.importNode(orderHldTypeElem));								
								
							}else {
								rootElem.appendChild(rootElem.importNode(yfsOrderHoldTypesDoc.getDocumentElement()));
							}
						}
					}
					// Order Instructions.
					headerComments = (String) orderDetailsMap.get("ExtnHeaderComments");
					if (!YFCObject.isNull(headerComments) && rootElem.getChildElement("Instructions") == null) {
						YFCElement instructionsElem = rootElem.createChild("Instructions");
						YFCElement instructionElem = instructionsElem.createChild("Instruction");	
						instructionElem.setAttribute("InstructionType", "HEADER");
						instructionElem.setAttribute("InstructionText", headerComments);
					} else if (!YFCObject.isNull(headerComments)) {
						// Instructions Element Is Already Created In The Document.
						YFCElement instructionsElem = rootElem.getChildElement("Instructions");
						YFCElement instructionElem = instructionsElem.createChild("Instruction");	
						instructionElem.setAttribute("InstructionType", "HEADER");
						instructionElem.setAttribute("InstructionText", headerComments);
					}
					
					// Order Extended Attributes.
					YFCElement orderExtnElem = rootElem.getChildElement("Extn");
					if (orderExtnElem != null) {
						
						// Retrieve The Data From The Map.
						deliveryHoldFlag = (String) orderDetailsMap.get("ExtnDeliveryHoldFlag");
						rushOrderComments = (String) orderDetailsMap.get("ExtnRushOrderComments");
						shipComplete = (String) orderDetailsMap.get("ExtnShipComplete");
						willCall = (String) orderDetailsMap.get("ExtnWillCall");
						deliveryHoldDate = (String) orderDetailsMap.get("ExtnDeliveryHoldDate");
						deliveryHoldTime = (String) orderDetailsMap.get("ExtnDeliveryHoldTime");
						attentionName = (String) orderDetailsMap.get("ExtnAttentionName");
						addnlEmailAddr = (String) orderDetailsMap.get("ExtnAddnlEmailAddr");
						rushOrderFlag = (String) orderDetailsMap.get("ExtnRushOrderFlag");
						webHoldFlag = (String) orderDetailsMap.get("ExtnWebHoldFlag");
						orderedByName = (String) orderDetailsMap.get("ExtnOrderedByName");
						
						// Set The Data In The Document.
						if (!YFCObject.isNull(deliveryHoldFlag)) {
							orderExtnElem.setAttribute("ExtnDeliveryHoldFlag", deliveryHoldFlag);
						}
						if (!YFCObject.isNull(rushOrderComments)) {
							orderExtnElem.setAttribute("ExtnRushOrderComments", rushOrderComments);
						}						
						if (!YFCObject.isNull(shipComplete)) {
							orderExtnElem.setAttribute("ExtnShipComplete", shipComplete);
						}					
						if (!YFCObject.isNull(willCall)) {
							orderExtnElem.setAttribute("ExtnWillCall", willCall);
						}					
						if (!YFCObject.isNull(deliveryHoldDate)) {
							orderExtnElem.setAttribute("ExtnDeliveryHoldDate", deliveryHoldDate);
						}					
						if (!YFCObject.isNull(deliveryHoldTime)) {
							orderExtnElem.setAttribute("ExtnDeliveryHoldTime", deliveryHoldTime);
						}				
						if (!YFCObject.isNull(attentionName)) {
							orderExtnElem.setAttribute("ExtnAttentionName", attentionName);
						}				
						if (!YFCObject.isNull(addnlEmailAddr)) {
							orderExtnElem.setAttribute("ExtnAddnlEmailAddr", addnlEmailAddr);
						}
						if (!YFCObject.isNull(rushOrderFlag)) {
							orderExtnElem.setAttribute("ExtnRushOrderFlag", rushOrderFlag);
						}
						if (!YFCObject.isNull(webHoldFlag)) {
							orderExtnElem.setAttribute("ExtnWebHoldFlag", webHoldFlag);
						}
						if (!YFCObject.isNull(orderedByName)) {
							orderExtnElem.setAttribute("ExtnOrderedByName", orderedByName);
						}
					}
				}
			} 
		}
	}
	
	/**
	 * This method prepares the error object with the exception details which in
	 * turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML) {
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		errorObject.setExceptionMessage(e.getMessage());
		ErrorLogger.log(errorObject, env);
	}

	public void setProperties(Properties arg0) throws Exception {
		this.arg0 = arg0;
	}
}