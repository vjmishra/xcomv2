package com.xpedx.nextgen.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

import java.util.Collections;

public class XPXWebConfAndSplChgLineUpdate implements YIFCustomApi
{

	private static YFCLogCategory log;
	private static YIFApi api = null;
	private Properties arg0;
	
	String changeOrderTemplate = "global/template/api/getSalesOrderDetails.XPXGetOrderDetailsService.xml";
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}
	
	public Document invokeActions(YFSEnvironment env, Document inputXML) throws Exception
	{
		String envtCode = null;
		String webConfirmationNumber = null;
		String buyerOrganizationCode = null;
		String orderHeaderKey = null;
		String enterpriseCode = null;
		
		orderHeaderKey = inputXML.getDocumentElement().getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
		
		buyerOrganizationCode = inputXML.getDocumentElement().getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
		
		enterpriseCode = inputXML.getDocumentElement().getAttribute(XPXLiterals.A_ENTERPRISE_CODE);
		
		if(buyerOrganizationCode!=null || buyerOrganizationCode.trim().length()!=0)
		{						
			String[] splitArrayOnBuyerOrgCode =  buyerOrganizationCode.split("-");
            
            for(int i=0; i<splitArrayOnBuyerOrgCode.length; i++)
            {
           	 
           	    if(i==3)
           	    {
           		envtCode = splitArrayOnBuyerOrgCode[i];
           		log.debug("The envt code is: "+envtCode);
           	    }         	
           	 
            }
		}
		
		webConfirmationNumber = generateWebConfirmationNumber(orderHeaderKey,envtCode, inputXML.getDocumentElement());
		if(!webConfirmationNumber.isEmpty())	
		{
			env.setTxnObject("WebConfirmationNumber", webConfirmationNumber);
		}
		
		ArrayList<String> customerDetailsList = getCustomerList(env, buyerOrganizationCode,enterpriseCode);
		
		Document changeOrderInputDoc = createChangeOrderForWebNumbers(env,orderHeaderKey,webConfirmationNumber,envtCode,inputXML.getDocumentElement(),customerDetailsList);
				
		env.setApiTemplate(XPXLiterals.CHANGE_ORDER_API, changeOrderTemplate);
		log.debug("Input to changeOrder input is: "+SCXmlUtil.getString(changeOrderInputDoc));
		Document changeOrderOutputDoc = api.invoke(env, "changeOrder", changeOrderInputDoc);
		env.clearApiTemplate(XPXLiterals.CHANGE_ORDER_API);
		log.debug("ChangeOrder output is: "+SCXmlUtil.getString(changeOrderOutputDoc));
		
		return changeOrderOutputDoc;
	}
	
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

	private Document createChangeOrderForWebNumbers(YFSEnvironment env, String orderHeaderKey, String webConfirmationNumber, String envtCode, 
			Element inputDocRoot, ArrayList<String> customerDetailsList) 
	{
		Document changeOrderInputDoc = null;
		String webLineNumber = null;	
		float maxOrderTotal = 0;
		float minOrderTotal = 0;
		float chargeAmount = 0;
		float totalAmount = 0;
		XPXUtils utilObj=null;
			
		String entryType = inputDocRoot.getAttribute(XPXLiterals.A_ENTRY_TYPE);
		
		//Max order total amount is not used as of now for special charge calculation but retaining it for any future change in reqmts.
		if(customerDetailsList.get(0)!=null && !"".equals(customerDetailsList.get(0)))
		{
		     maxOrderTotal = Float.parseFloat(customerDetailsList.get(0));
		}
		if(customerDetailsList.get(1)!=null && !"".equals(customerDetailsList.get(1)))
		{
		     minOrderTotal = Float.parseFloat(customerDetailsList.get(1));
		}
		if(customerDetailsList.get(2)!=null && !"".equals(customerDetailsList.get(2)))
		{
		     chargeAmount = Float.parseFloat(customerDetailsList.get(2));
		}		
		
		changeOrderInputDoc = YFCDocument.createDocument("Order").getDocument();
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, orderHeaderKey);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTERPRISE_CODE, XPXLiterals.A_ORGANIZATIONCODE_VALUE);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_DOCUMENT_TYPE, "0001");
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OVERRIDE, XPXLiterals.BOOLEAN_FLAG_Y);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ACTION, XPXLiterals.MODIFY);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_TYPE, XPXLiterals.CUSTOMER);
		
		Element orderExtn = changeOrderInputDoc.createElement(XPXLiterals.E_EXTN);
		orderExtn.setAttribute(XPXLiterals.A_WEB_CONF_NUMBER, webConfirmationNumber);
		orderExtn.setAttribute("ExtnOrderStatus", "1100.0100");
		orderExtn.setAttribute("ExtnOrderStatusPrefix", "");
		changeOrderInputDoc.getDocumentElement().appendChild(orderExtn);
				
		Element changeOrderLinesElement = changeOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINES);
		
		Element orderLines = (Element) inputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);
		
		/*Begin - Code changes made by Mitesh Parikh for JIRA 3372*/
		ArrayList<Element> orderLinesListElements = SCXmlUtil.getChildren(orderLines, XPXLiterals.E_ORDER_LINE);		
		Collections.sort(orderLinesListElements, new XpedxPrimeLineNoComparator());		
		for(int i=0; i<orderLinesListElements.size(); i++)
		{
			Element orderLineElement = (Element)orderLinesListElements.get(i);
		/*End - Code changes made by Mitesh Parikh for JIRA 3372*/
			String orderLineKey = orderLineElement.getAttribute(XPXLiterals.A_ORDER_LINE_KEY);
			
			Element changeOrderLineElement = changeOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINE);
			changeOrderLineElement.setAttribute(XPXLiterals.A_ORDER_LINE_KEY, orderLineKey);
            //Getting the next unique sequence number from the custom sequence created
			long uniqueSequenceNo = CallDBSequence.getNextDBSequenceNo(env, XPXLiterals.WEB_LINE_SEQUENCE);
			/* changes made to fix issue 926 */
			// webLineNumber = generateWebLineNumber(uniqueSequenceNo,envtCode);
			
			webLineNumber = XPXAddParametersAPI.generateWebLineNumber(entryType, uniqueSequenceNo,envtCode);
			log.debug("webLineNumber :::: " + webLineNumber);
			
			Element orderLineExtn = changeOrderInputDoc.createElement(XPXLiterals.E_EXTN);
			orderLineExtn.setAttribute(XPXLiterals.A_WEB_LINE_NUMBER, webLineNumber);
			changeOrderLineElement.appendChild(orderLineExtn);
			
			changeOrderLinesElement.appendChild(changeOrderLineElement);
		}
		
		changeOrderInputDoc.getDocumentElement().appendChild(changeOrderLinesElement);
		
		if(minOrderTotal != 0)
		{
			String orderTotal = SCXmlUtil.getXpathAttribute(inputDocRoot, "./Extn/@ExtnTotalOrderValue");
			if(orderTotal!=null && !"".equals(orderTotal))
			{
			    totalAmount = Float.parseFloat(orderTotal);
			} 			    
			
			if(totalAmount <minOrderTotal)
			{
				utilObj=new XPXUtils();				
				//get the orderlines element
				Element orderExtnElem=SCXmlUtil.getChildElement(inputDocRoot, "Extn");
				double reTotalAmount=totalAmount+ new Float(chargeAmount);
				
				String extnOrderSubTotal=orderExtnElem.getAttribute("ExtnOrderSubTotal");
				float extnSubtotal =0;
				
				if(extnOrderSubTotal != null && !"".equals(extnOrderSubTotal))
				{
					extnSubtotal = Float.parseFloat(extnOrderSubTotal)+new Float(chargeAmount);
				}			
				
				orderExtn.setAttribute("ExtnTotOrdValWithoutTaxes", ""+reTotalAmount);
				orderExtn.setAttribute("ExtnTotalOrderValue", ""+reTotalAmount) ;
				orderExtn.setAttribute("ExtnOrderSubTotal", ""+extnSubtotal) ;
				
				//create a special charge line
				
				Element specialElement = changeOrderInputDoc.createElement("OrderLine");
				specialElement.setAttribute("OrderedQty", "1");
				specialElement.setAttribute("ValidateItem", "N");
				specialElement.setAttribute("LineType", "M");
				Element itemElement = changeOrderInputDoc.createElement("Item");
				itemElement.setAttribute("ItemID", arg0.getProperty("ItemID"));
				if(utilObj.checkIfOrderOnPendingHold(inputDocRoot.getOwnerDocument())){
					if(YFSSystem.getProperty("ItemShortDesc") != null)
						itemElement.setAttribute("ItemShortDesc", YFSSystem.getProperty("ItemShortDesc"));			
				}
				//itemElement.setAttribute("UnitOfMeasure", "EACH");
									
				Element extnElement = changeOrderInputDoc.createElement("Extn");
				extnElement.setAttribute("ExtnLineOrderedTotal", new Float(chargeAmount).toString());
				extnElement.setAttribute("ExtnReqUOMUnitPrice",new Float(chargeAmount).toString());
				extnElement.setAttribute("ExtnUnitPriceDiscount", "0");
				extnElement.setAttribute("ExtnUnitPrice", new Float(chargeAmount).toString());
				extnElement.setAttribute("ExtnExtendedPrice", new Float(chargeAmount).toString());
				extnElement.setAttribute("ExtnAdjUOMUnitPrice", new Float(chargeAmount).toString());
				extnElement.setAttribute("ExtnAdjUnitPrice", new Float(chargeAmount).toString());		
				extnElement.setAttribute("ExtnPriceOverrideFlag", "Y"); 
				
				long uniqueSequenceNo = CallDBSequence.getNextDBSequenceNo(env, XPXLiterals.WEB_LINE_SEQUENCE);
				webLineNumber = XPXAddParametersAPI.generateWebLineNumber(entryType, uniqueSequenceNo,envtCode);
				extnElement.setAttribute(XPXLiterals.A_WEB_LINE_NUMBER, webLineNumber);
				
				specialElement.appendChild(extnElement);
				
				specialElement.appendChild(itemElement);
				Element linePriceInfoElement = changeOrderInputDoc.createElement("LinePriceInfo");
				linePriceInfoElement.setAttribute("UnitPrice", new Float(chargeAmount).toString());
				linePriceInfoElement.setAttribute("IsPriceLocked", "Y");
				specialElement.appendChild(linePriceInfoElement);
				changeOrderLinesElement.appendChild(specialElement);
				
				setProgressYFSEnvironmentVariables(env);
								
			}
			
		}	
		
		return changeOrderInputDoc;
	}

	private ArrayList<String>  getCustomerList(YFSEnvironment env, String customerId,String enterpriseCode)
	{
		ArrayList<String> customerArrayList = new ArrayList<String>();
		try
		{			
			float minOrderAmount=0;
			float chargeAmount=0;
			String maxOrderAmount="0";
			
			YFCElement customerList = null;			
			
			Document shipToCustomerProfileDoc = (Document)env.getTxnObject("ShipToCustomerProfile");
			if(shipToCustomerProfileDoc!=null)
			{
				log.debug("Retrieving the details from environment object");
				customerList = YFCDocument.getDocumentFor((Document)env.getTxnObject("ShipToCustomerProfile")).getDocumentElement();
			}
			else
			{
			//Form the input
			YFCDocument inputCustomerDoc = YFCDocument.createDocument("Customer");
			YFCElement inputCustomerElement = inputCustomerDoc.getDocumentElement();
			inputCustomerElement.setAttribute("CustomerID", customerId);
			inputCustomerElement.setAttribute("OrganizationCode", enterpriseCode);
			//form the template
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
			//invoke a getCustomerList
			//env.setApiTemplate("getCustomerList", customerListTemplate);
			//YFCElement inputCustDoc=inputCustomerDoc.getDocumentElement();
			env.setApiTemplate("getCustomerList", customerListTemplate.getDocument());
						
			customerList=YFCDocument.getDocumentFor(api.invoke(env, "getCustomerList", inputCustomerDoc.getDocument())).getDocumentElement();
			
			}
						
			YFCNodeList customerNodeList = customerList.getElementsByTagName("Customer");
			int customerLength = customerNodeList.getLength();
			if(customerLength > 0)
			{
				YFCElement customerElement = (YFCElement)customerNodeList.item(0);
				YFCElement extnElement = customerElement.getElementsByTagName("Extn").item(0);
				String minOrderAmountStr=extnElement.getAttribute("ExtnMinOrderAmount");
				String chargeAmountStr=extnElement.getAttribute("ExtnMinChargeAmount");
				maxOrderAmount=extnElement.getAttribute("ExtnMaxOrderAmount");
				String shipFromDivision =extnElement.getAttribute("ExtnShipFromBranch");
				String envCode =extnElement.getAttribute("ExtnEnvironmentCode");
				
				if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr)))  &&
						!("0".equals(minOrderAmountStr)) && (!"0.00".equals(minOrderAmountStr) ))
				{
					minOrderAmount = Float.parseFloat(minOrderAmountStr);	
					if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
					{
						chargeAmount = Float.parseFloat(chargeAmountStr);
					}
				}
				else
				{
					if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
					{
						chargeAmount = Float.parseFloat(chargeAmountStr);
					}
					YFCElement parentElement = customerElement.getElementsByTagName("ParentCustomer").item(0);
					if(parentElement != null )
					{
						YFCElement extnParentElement = parentElement.getElementsByTagName("Extn").item(0);
						minOrderAmountStr=extnParentElement.getAttribute("ExtnMinOrderAmount");
						chargeAmountStr=extnParentElement.getAttribute("ExtnMinChargeAmount");
						maxOrderAmount=extnElement.getAttribute("ExtnMaxOrderAmount");
						if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr))) &&
								(!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) ))
						{
							minOrderAmount = Float.parseFloat(minOrderAmountStr);	
							if(chargeAmount <=0 && chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
							{
									chargeAmount = Float.parseFloat(chargeAmountStr);
							}
						}
						else
						{
							if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)) && chargeAmount <=0)
							{
								chargeAmount = Float.parseFloat(chargeAmountStr);
							}
														
							try {
								
								env
								.setApiTemplate(
										"getOrganizationList",
										SCXmlUtil
												.createFromString(""
														+" <OrganizationList><Organization OrganizationName=\"\">"														
														+"<Extn ExtnMinOrderAmt=\"\" ExtnSmallOrderFee=\"\"/>"
													    +"</Organization>"
													    +"</OrganizationList>"));
								
								Document input =SCXmlUtil.createFromString(""+ "<Organization OrganizationCode=\""+shipFromDivision+"_"+envCode +"\"/> ");
								Object obj = api.invoke(env, "getOrganizationList", input).getDocumentElement();
								Document outputDoc = ((Element) obj).getOwnerDocument();
								
								if(YFCCommon.isVoid(outputDoc)){
									log.error("No DB record exist for Node "+ shipFromDivision+"_"+envCode+". ");
									return customerArrayList;
								}
								
								minOrderAmountStr = SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnMinOrderAmt");
								chargeAmountStr= SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnSmallOrderFee");
								if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr))) &&
										(!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) ))
								{
									minOrderAmount = Float.parseFloat(minOrderAmountStr);				
									if( chargeAmount <=0 && chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
									{
											chargeAmount = Float.parseFloat(chargeAmountStr);
									}
									maxOrderAmount=extnElement.getAttribute("ExtnMaxOrderAmount");
								}
								
							} catch (Exception e) {
								log.error("Unable to get XPEDXGetShipOrgNodeDetails for "+ shipFromDivision+"_"+envCode+". ",e);
								return customerArrayList;
							}
						}
					}
				}
			}
			customerArrayList.add(maxOrderAmount);
			customerArrayList.add(""+minOrderAmount);
			customerArrayList.add(""+chargeAmount);
	} catch (Exception ex) {
		log.error(ex.getMessage());
		
	}
		return 	customerArrayList;
	}

	public String generateWebConfirmationNumber(String orderHeaderKey, String envtCode, Element inputDocRoot) {

		String webConfirmationNumber = "";
		String uniqueSequence = "";
		String year = ""; 
		String month = "";
		String day = "";
		String entryType= "";
		int uniqueSequenceLength = 7;
		int orderHeaderKeylength = orderHeaderKey.trim().length();
		
		YFCDate currentSystemDate = new YFCDate();
		String currentSystemDateString = currentSystemDate.toString();
		log.debug("The current systemDate is : "+currentSystemDateString);
		year = currentSystemDateString.substring(2,4);
		month = currentSystemDateString.substring(4,6);
		day = currentSystemDateString.substring(6,8);
		
		if (orderHeaderKey != null && orderHeaderKeylength != 0 
				&& orderHeaderKeylength > 8)
		{
			int startIndex = orderHeaderKeylength-uniqueSequenceLength;
			uniqueSequence = orderHeaderKey.substring(startIndex);
		}	

		entryType = inputDocRoot.getAttribute(XPXLiterals.A_ENTRY_TYPE);
		log.debug("Entry type = " + entryType);
		/* Changes made to fix issue 926 
		IF order is placed from B2B,WEB or COM  Environment ID is Changed to constant('E') */
		if(entryType != null && (XPXLiterals.SOURCE_TYPE_B2B.equals(entryType) || XPXLiterals.SOURCE_WEB.equals(entryType) 
				|| XPXLiterals.SOURCE_COM.equals(entryType))){
			envtCode = XPXLiterals.SYSTEM_SPECIFIER_WEB;
		}
		
		webConfirmationNumber = year+month+day+envtCode+uniqueSequence;
		log.debug("The web confirmation number is: "+webConfirmationNumber);

		return webConfirmationNumber;
	}

	public void setProperties(Properties arg0) throws Exception {
		this.arg0 = arg0;
	}
	
}
