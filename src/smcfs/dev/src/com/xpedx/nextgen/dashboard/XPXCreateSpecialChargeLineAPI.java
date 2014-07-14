package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXCreateSpecialChargeLineAPI {
	
	private static YIFApi api = null;
	private Properties arg0;
	private static YFCLogCategory log;
	static
	{
	log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}

	public void setProperties(Properties arg0) throws Exception {
		this.arg0 = arg0;
		// TODO Auto-generated method stub
		
		

	}
	public Document createSpecialOrderLine(YFSEnvironment env,Document inputXML) throws Exception{
		{
			api = YIFClientFactory.getInstance().getApi();
			Document outDoc =null;
			//get the customerID(BillToID)
			Element inputElement = inputXML.getDocumentElement();
			String customerId = inputElement.getAttribute("ShipToID");
			String enterpriseCode=inputElement.getAttribute("EnterpriseCode");
			com.sterlingcommerce.baseutil.SCXmlUtil.getString(inputXML);
			//inputElement.setAttribute("Action", "MODIFY");
			//create a new doc
			Document changeOrderDoc = YFCDocument.createDocument("Order").getDocument();
			Element changeOrderElement = changeOrderDoc.getDocumentElement();
			changeOrderElement.setAttribute("OrderHeaderKey", inputElement.getAttribute("OrderHeaderKey"));
			
			ArrayList<String> customerDetailsList = new ArrayList<String>();
			//get the customer list to find the maxOrderTotal and the MinOrderTotal
			customerDetailsList = getCustomerList(env, customerId,enterpriseCode);
			
			float maxOrderTotal = Float.parseFloat(customerDetailsList.get(0));
			float minOrderTotal = Float.parseFloat(customerDetailsList.get(1));
			float chargeAmount = Float.parseFloat(customerDetailsList.get(2));
			
			if(minOrderTotal != 0)
			{
				//check whether special charge line needs to be created
				//get the order total
				String orderTotal = SCXmlUtil.getXpathAttribute(inputElement, "./Extn/@ExtnTotalOrderValue");
				//String orderTotal = inputElement.getAttribute("OriginalTotalAmount");
				
				float totalAmount = Float.parseFloat(orderTotal);
				if(totalAmount <minOrderTotal)
				{
					//get the orderlines element
					Element orderExtnElem=SCXmlUtil.getChildElement(inputElement, "Extn");
					double reTotalAmount=totalAmount+ new Float(chargeAmount);
					
					String extnOrderSubTotal=orderExtnElem.getAttribute("ExtnOrderSubTotal");
					float extnSubtotal =0;
					
					if(extnOrderSubTotal != null && !"".equals(extnOrderSubTotal))
					{
						extnSubtotal = Float.parseFloat(extnOrderSubTotal)+new Float(chargeAmount);
					}
					
					Element orderExtnElement = changeOrderDoc.createElement("Extn");
					orderExtnElement.setAttribute("ExtnTotOrdValWithoutTaxes", ""+reTotalAmount);
					orderExtnElement.setAttribute("ExtnTotalOrderValue", ""+reTotalAmount) ;
					orderExtnElement.setAttribute("ExtnOrderSubTotal", ""+extnSubtotal) ;
					changeOrderElement.appendChild(orderExtnElement);
					Element orderLinesElement = changeOrderDoc.createElement("OrderLines");
					//create a special charge line
					Element specialElement = changeOrderDoc.createElement("OrderLine");
					specialElement.setAttribute("OrderedQty", "1");
					specialElement.setAttribute("ValidateItem", "N");
					specialElement.setAttribute("LineType", "M");
					Element itemElement = changeOrderDoc.createElement("Item");
					itemElement.setAttribute("ItemID", arg0.getProperty("ItemID"));
					//itemElement.setAttribute("UnitOfMeasure", "EACH");
										
					Element extnElement = changeOrderDoc.createElement("Extn");
					extnElement.setAttribute("ExtnLineOrderedTotal", new Float(chargeAmount).toString());
					extnElement.setAttribute("ExtnReqUOMUnitPrice",new Float(chargeAmount).toString());
					extnElement.setAttribute("ExtnUnitPriceDiscount", "0");
					extnElement.setAttribute("ExtnUnitPrice", new Float(chargeAmount).toString());
					extnElement.setAttribute("ExtnExtendedPrice", new Float(chargeAmount).toString());
					extnElement.setAttribute("ExtnAdjUOMUnitPrice", new Float(chargeAmount).toString());
					extnElement.setAttribute("ExtnAdjUnitPrice", new Float(chargeAmount).toString());					
					specialElement.appendChild(extnElement);
					
					specialElement.appendChild(itemElement);
					Element linePriceInfoElement = changeOrderDoc.createElement("LinePriceInfo");
					linePriceInfoElement.setAttribute("UnitPrice", new Float(chargeAmount).toString());
					linePriceInfoElement.setAttribute("IsPriceLocked", "Y");
					specialElement.appendChild(linePriceInfoElement);
					orderLinesElement.appendChild(specialElement);
					changeOrderElement.appendChild(orderLinesElement);
					setProgressYFSEnvironmentVariables(env);
					outDoc = api.invoke(env, "changeOrder", changeOrderDoc);
				}
			}
			
			
			return inputXML;
		}
			
		
	}
	
	private void setProgressYFSEnvironmentVariables(YFSEnvironment env) {
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				envVariablesmap.put("isSpecialItem", "true");
				envVariablesmap.put("specialItemID", arg0.getProperty("ItemID"));
				
			}
			
			clientVersionSupport.setClientProperties(envVariablesmap);
		}
	}
	
	private ArrayList<String> getCustomerList(YFSEnvironment env, String customerId)
			throws RemoteException {
		ArrayList<String> customerArrayList = new ArrayList<String>();
		//Form the input
		Document inputCustomerDoc = YFCDocument.createDocument("Customer").getDocument();
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerID", customerId);
		String maxTotalOrderAmount = "";
		String minTotalOrderAmount = "";
		String chargeAmount = "";
		//form the template
		Document customerListTemplate = YFCDocument.createDocument("CustomerList").getDocument();
		Element customerListTemplateElement = customerListTemplate.getDocumentElement();
		Element customerTemplateElement = customerListTemplate.createElement("Customer");
		customerListTemplateElement.appendChild(customerTemplateElement);
		Element extnTemplateElement = customerListTemplate.createElement("Extn");
		customerTemplateElement.appendChild(extnTemplateElement);
		//invoke a getCustomerList
		env.setApiTemplate("getCustomerList", customerListTemplate);
		Document customerList = api.invoke(env, "getCustomerList", inputCustomerDoc);
		env.clearApiTemplate("getCustomerList");
		NodeList customerNodeList = customerList.getElementsByTagName("Customer");
		int customerLength = customerNodeList.getLength();
		if(customerLength != 0)
		{
			Element customerElement = (Element)customerNodeList.item(0);
			maxTotalOrderAmount = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnMaxOrderAmount");
			minTotalOrderAmount = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnMinOrderAmount");
			chargeAmount = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnMinChargeAmount");
		}
		customerArrayList.add(maxTotalOrderAmount);
		customerArrayList.add(minTotalOrderAmount);
		customerArrayList.add(chargeAmount);
		
		return customerArrayList;
	}

	private ArrayList<String>  getCustomerList(YFSEnvironment env, String customerId,String enterpriseCode)
	{
		ArrayList<String> customerArrayList = new ArrayList<String>();
		try
		{
			
			float minOrderAmount=0;
			float chargeAmount=0;
			String maxOrderAmount="0";
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
			YFCElement customerList=YFCDocument.getDocumentFor(api.invoke(env, "getCustomerList", inputCustomerDoc.getDocument())).getDocumentElement();
			/*scuiTransactionContext  = wcContext.getSCUIContext().getTransactionContext(true);
			YFCElement customerList=SCUIPlatformUtils.invokeXAPI(
					"getCustomerList", inputCustomerDoc.getDocumentElement(), customerListTemplate
							.getDocumentElement(), wcContext.getSCUIContext());*/
			//Document customerList = api.invoke(env, "getCustomerList", inputCustomerDoc);
			//env.clearApiTemplate("getCustomerList");
			
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
							/*env
							.setApiTemplate(
									"getCustomerDetails",
									SCXmlUtil
											.createFromString(""
													+ "<Customer CustomerID=\"\" OrganizationCode=\"\" >" 
													+ "<Extn></Extn>"
													+ "</Customer>"));
							inputCustomerElement.setAttribute("OrganizationCode", enterpriseCode);
							
							Element custDetails=api.invoke(env, "getCustomerDetails", inputCustomerDoc.getDocument()).getDocumentElement();							
							String shipFromDivision =SCXmlUtil.getXpathAttribute(custDetails, "/Customer/Extn/@ExtnShipFromBranch");
							
							
							String envCode =SCXmlUtil.getXpathAttribute(custDetails, "/Customer/Extn/@ExtnEnvironmentCode");*/
							
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
}
