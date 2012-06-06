package com.xpedx.nextgen.dashboard;

import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfs.japi.YFSUserExitException;

/**
 * Description: This class is used to add the parameters like WebConfirmationNumber and WebLineNumber to the confirmed draft order
 *              just before the creation of the chained order(s). This also stamps the LineType of each orderline after checking the
 *              respective item details. If the ExtnItemType of the Item has no set value, then the default value used will be STOCK_ORDER.
 *                  
 * @author Prasanth Kumar M.
 *
 */
public class XPXAddParametersAPI implements YIFCustomApi
{
	private static YFCLogCategory log;
	private static YIFApi api = null;

	String template = "global/template/api/getItemList.CreateCustomerOrderService.xml";
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXCreateChainedOrderService.xml";

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

	public Document beforeChangeOrder(YFSEnvironment env, Document inputXML) throws YFSUserExitException, RemoteException
	{

		log.beginTimer("XPXAddParametersAPI.beforeChangeOrder");

		

		Element inputDocRoot =  null;
		Element orderLines = null;
		Element item = null;
		Element itemListDocRoot = null;
		Element itemElement = null;
		Element itemExtnElement = null;
		NodeList orderLineList = null;

		Document getItemListInputDoc = null;
		Document outputCustomerDoc = null;
		Document getCustomerListInputDoc = null;
		Document getCustomerListOutputDoc = null;

		String itemId = null;
		String itemTypeValue = null;

		String webConfirmationNumber = null;
		String webLineNumber = null;
		String orderHeaderKey = null;
		String orderLineKey = null;
		String willCallFlag = null;
		String webHoldFlag = null;
		String billToId = null;
	    String orderUpdateFlag = null;
	    String legacyCustomerNo = null;
	    String entryType = null;
	    String sourceType = null;
	    String lineSubTotal = null;
	    String extendedPrice = null;
	    String customer_branch = null;
	    String environment_id =  null;
	    String company_code = null;
	    String customerItem = null;
	    String orderLineShipNode = null;
	    String inventoryIndicator = null;
	    String masterCustomerName  = null;
	    String awardAmount = null;
	    String buyerOrgCode = null;
	    String shipToName = null;
	    String billToName = null;
	    String billToSuffix = null;
	    String shipToSuffix = null;
	    String lineType = null;
	    String customerEnvtId = null;
	    String carrierServiceCode = null;
	    String customerOrderingDivision = null;
	    String customerDivision = null;
	    String envtCode = null;
	    String compCode = null;
	    String customerContactID = null;
	    String orderedByName = null;
	    
	    NodeList apiOutputList = null;
	    Element customerListOutputElement = null;
	    boolean setWebHoldFlag = false;

        log.debug("The input xml to ADDParametersAPI is: "+SCXmlUtil.getString(inputXML));
        
        /***********************************************/
        
        //invoke a get orderList
        /*Document inputOrderDoc = YFCDocument.createDocument("Order").getDocument();
        Element inputOrderElement = inputOrderDoc.getDocumentElement();
        inputOrderElement.setAttribute("OrderHeaderKey", inputXML.getDocumentElement().getAttribute("OrderHeaderKey"));
        Document orderListTemplateDoc = setOrderListTemplate(env);
        env.setApiTemplate("getOrderList", orderListTemplateDoc);
        Document orderListDocument = api.invoke(env, "getOrderList", inputOrderDoc);
        env.clearApiTemplate("getOrderList");
        NodeList orderList = orderListDocument.getElementsByTagName("Order");
        Element orderElement = (Element)orderList.item(0);
        
       inputDocRoot = orderElement;*/
        
        inputDocRoot =  inputXML.getDocumentElement();
        
        //log.debug("inputDocRoot"+SCXmlUtil.getString(inputDocRoot));
        
        /*********************************************/
         

		orderHeaderKey = inputDocRoot.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
		log.debug("The order header key of the customer order is: "+orderHeaderKey);

		customerContactID = inputDocRoot.getAttribute("CustomerContactID");
		
		/*********Fix done on 14/09/10 so that default Shipping method is always passed during order confirmation*********/
		carrierServiceCode = inputDocRoot.getAttribute("CarrierServiceCode");
		log.debug("The carrier service code is: "+carrierServiceCode);
		
		if(carrierServiceCode == null || carrierServiceCode.trim().length()==0)
		{
			carrierServiceCode = "Standard Shipping";
		}
		/*****************************************************************************************************/
		
		inputDocRoot.setAttribute(XPXLiterals.A_ORDER_TYPE, XPXLiterals.CUSTOMER);

		Element orderExtn = (Element) inputDocRoot.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		
		if(orderExtn!=null)
		{
			willCallFlag =  orderExtn.getAttribute(XPXLiterals.A_EXTN_WILL_CALL_FLAG);
			
		}
		
		entryType = inputDocRoot.getAttribute(XPXLiterals.A_ENTRY_TYPE);
		
		/************** Fix for Bug # 11649 *******************************************************/
		
		if(entryType.equals(XPXLiterals.SOURCE_COM))
		{
            //input from COM
			customer_branch = inputDocRoot.getAttribute(XPXLiterals.A_SHIP_NODE);
			if(SCUtil.isVoid(customer_branch)){
				
				customer_branch = getShipNodeFromCustomer(env,inputXML);
			}
			//inputXML.getDocumentElement().setAttribute(XPXLiterals.A_SHIP_NODE, customer_branch);
			
		}
		else
		{
		
		/***************************************************************************/
		customer_branch = getShipNodeFromCustomer(env,inputXML);
		}
		
		/**************Added by Prasanth Kumar M. as fix for Bug #11643***************************/
		
		buyerOrgCode = inputDocRoot.getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
		//log.debug("The buyerOrganizationCode is: "+buyerOrgCode);
		
		if(buyerOrgCode!=null || buyerOrgCode.trim().length()!=0)
		{
			/*String tempBuyerOrgCode = "30-057520-200-DEV-MM";
			String[] splitArrayOnBuyerOrgCode = tempBuyerOrgCode.split("-");*/
			
			String[] splitArrayOnBuyerOrgCode =  buyerOrgCode.split("-");
            
            for(int i=0; i<splitArrayOnBuyerOrgCode.length; i++)
            {
           	 if(i==2)
           	 {
           		 shipToSuffix = splitArrayOnBuyerOrgCode[i];
           		log.debug("The shipToSuffix is: "+shipToSuffix);
           	 }
           	 if(i==3)
           	 {
           		envtCode = splitArrayOnBuyerOrgCode[i];
           		log.debug("The envt code is: "+envtCode);
           	 }
           	if(i==4)
          	 {
          		compCode = splitArrayOnBuyerOrgCode[i];
          		log.debug("The comp code is: "+compCode);
          	 }
           	 
            }
		}
		
		
		/*****************************************************************************************/
		
		Element orderOverallTotals = (Element) inputDocRoot.getElementsByTagName(XPXLiterals.E_OVERALL_TOTALS).item(0);
		if(orderOverallTotals!=null)
		{
		lineSubTotal = orderOverallTotals.getAttribute(XPXLiterals.A_LINE_SUB_TOTAL);
		}
		if(entryType != null && entryType.trim().length() !=0)
		{
				/* Code changed for jira 1426 ****/
			
		        if(entryType.equalsIgnoreCase(XPXLiterals.SOURCE_EDI)||entryType.equalsIgnoreCase(XPXLiterals.SOURCE_TYPE_B2B))
		        {
		        	//sourceType = XPXLiterals.SOURCE_TYPE_B2B;
		        	sourceType = "1";
		        }
		        if(entryType.equalsIgnoreCase(XPXLiterals.SOURCE_WEB))
		        {
		        	//sourceType = XPXLiterals.SOURCE_TYPE_EXTERNAL;
		        	sourceType = "3";      
		        }
		        if(entryType.equalsIgnoreCase(XPXLiterals.SOURCE_COM))
		        {
		        	//sourceType = XPXLiterals.SOURCE_TYPE_INTERNAL;
		        	  sourceType = "4";
		        }
		        /*if(entryType.equalsIgnoreCase("B2B"))
		        {          
		        	
		        	  sourceType = "2";
		        }*/
		        
		}
		
		// BillToId changed to BuyerOrgCode.Changed as part of Bug fix for #11643
		if(buyerOrgCode != null && buyerOrgCode.trim().length() !=0)
		{
			getCustomerListOutputDoc = (Document)env.getTxnObject("ShipToCustomerProfile");
						
			if(getCustomerListOutputDoc == null)
			{	
				try {
		              getCustomerListInputDoc = createGetCustomerListInput(env,buyerOrgCode);
		         	   log.debug("The input to getCustomerList for shipTo is: "+SCXmlUtil.getString(getCustomerListInputDoc));	
		              /**********************getCustomerList call changed to MultiAPI call as part of fix for bug#11643*********/
			          getCustomerListOutputDoc = api.invoke(env,XPXLiterals.MULTI_API, getCustomerListInputDoc);
					log.debug("The output of multiApi getCustomerList is: "+SCXmlUtil.getString(getCustomerListOutputDoc));
			         /************************************************************************************************************/
		           } 
				catch (Exception e) {
			
			         log.error("The exception is: "+e.getMessage());
				}
		   }
		    
		
		Element getCustomerListOutputRoot = getCustomerListOutputDoc.getDocumentElement();
		
		if(getCustomerListOutputRoot.getNodeName().equalsIgnoreCase(XPXLiterals.E_MULTI_API))
		{
			apiOutputList = getCustomerListOutputRoot.getElementsByTagName(XPXLiterals.E_API);
		    
		    if(apiOutputList!=null && apiOutputList.getLength()>0)
			{
			
				for(int i=0; i<apiOutputList.getLength(); i++)
				{
					Element apiElement = (Element)apiOutputList.item(i);
					Element outputElement = (Element) apiElement.getElementsByTagName(XPXLiterals.E_OUTPUT).item(0);
					Element customerListElement = (Element) outputElement.getElementsByTagName(XPXLiterals.E_CUSTOMER_LIST).item(0);
					Element customerElement = (Element) customerListElement.getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
					
					if(customerElement!=null)
					{
					String customerID=customerElement.getAttribute(XPXLiterals.A_CUSTOMER_ID);
					
					if(customerID.equalsIgnoreCase(buyerOrgCode))
					{
						Element customerExtnElement = (Element) customerElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
						
						orderUpdateFlag = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_ORDER_UPDATE_FLAG);
						legacyCustomerNo = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_LEGACY_CUST_NO);
						masterCustomerName  = customerExtnElement.getAttribute(XPXLiterals.A_SAP_PARENT_NAME);
						
						customerEnvtId = customerExtnElement.getAttribute("ExtnOrigEnvironmentCode");
						customerOrderingDivision = customerExtnElement.getAttribute("ExtnCustOrderBranch")+"_"+envtCode;
						//log.debug("The customer ordering division is: "+customerOrderingDivision);
						customerDivision = customerExtnElement.getAttribute("ExtnCustomerDivision")+"_"+envtCode;
						//log.debug("The customer division is: "+customerDivision);
						
						Element customerBuyerOrgElement  = (Element)customerElement.getElementsByTagName(XPXLiterals.E_BUYER_ORGANIZATION).item(0);
						shipToName = customerBuyerOrgElement.getAttribute(XPXLiterals.A_ORGANIZATION_NAME);
						//log.debug("The shipTo name is: "+shipToName);
						
						Element customerParentElement = (Element) customerElement.getElementsByTagName(XPXLiterals.E_PARENT_CUSTOMER).item(0);
						billToId = customerParentElement.getAttribute(XPXLiterals.A_CUSTOMER_ID);
						log.debug("The billTo customer id is: "+billToId);
						
						if(billToId!=null || billToId.trim().length()!=0)
						{
							/*String tempBillToId = "30-057520-000-DEV-MM";
							String[] splitArrayOnBillToId = tempBillToId.split("-");*/
							
							String[] splitArrayOnBillToId = billToId.split("-");
				            
				            for(int c=0; c<splitArrayOnBillToId.length; c++)
				            {
				           	 if(c==2)
				           	 {
				           		 billToSuffix = splitArrayOnBillToId[c];
				           		 
				           		 log.debug("The billToSuffix is: "+billToSuffix);
				           	 }
				            }
						}
					}
					
									
					}
					
				}
			
			}
		}
		else if(getCustomerListOutputRoot.getNodeName().equalsIgnoreCase("CustomerList"))
		{
			customerListOutputElement = (Element) getCustomerListOutputRoot.getElementsByTagName("Customer").item(0);
			
			if(customerListOutputElement!=null)
			{

					Element customerExtnElement = (Element) customerListOutputElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
					
					orderUpdateFlag = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_ORDER_UPDATE_FLAG);
					legacyCustomerNo = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_LEGACY_CUST_NO);
					masterCustomerName  = customerExtnElement.getAttribute(XPXLiterals.A_SAP_PARENT_NAME);
					
					customerEnvtId = customerExtnElement.getAttribute("ExtnOrigEnvironmentCode");
					customerOrderingDivision = customerExtnElement.getAttribute("ExtnCustOrderBranch")+"_"+envtCode;
					//log.debug("The customer ordering division is: "+customerOrderingDivision);
					customerDivision = customerExtnElement.getAttribute("ExtnCustomerDivision")+"_"+envtCode;
					//log.debug("The customer division is: "+customerDivision);
					
					Element customerBuyerOrgElement  = (Element)customerListOutputElement.getElementsByTagName(XPXLiterals.E_BUYER_ORGANIZATION).item(0);
					shipToName = customerBuyerOrgElement.getAttribute(XPXLiterals.A_ORGANIZATION_NAME);
					//log.debug("The shipTo name is: "+shipToName);
					
					Element customerParentElement = (Element) customerListOutputElement.getElementsByTagName(XPXLiterals.E_PARENT_CUSTOMER).item(0);
					billToId = customerParentElement.getAttribute(XPXLiterals.A_CUSTOMER_ID);
					log.debug("The billTo customer id is: "+billToId);
					
					if(billToId!=null || billToId.trim().length()!=0)
					{
						String[] splitArrayOnBillToId = billToId.split("-");
			            for(int c=0; c<splitArrayOnBillToId.length; c++)
			            {
			           	 if(c==2)
			           	 {
			           		 billToSuffix = splitArrayOnBillToId[c];
			           		 
			           		 log.debug("The billToSuffix is: "+billToSuffix);
			           	 }
			            }
					}				
				
			}
		}
		
		
		
		
		
		   //Retrieving the bill to customer's buyer org name to stamp as the bill to name
		
		    if(billToId!=null && billToId.trim().length()!=0) 
		    {
		    	getCustomerListInputDoc = createGetCustomerListInput(env,billToId);
		    	
		    	try 
		    	{
					
					
					Document getParentCustomerListOutputDoc = api.invoke(env,XPXLiterals.MULTI_API, getCustomerListInputDoc);
					
					log.debug("The output of multiApi getCustomerList is: "+SCXmlUtil.getString(getParentCustomerListOutputDoc));
					
					if(getParentCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).getLength() > 0)
					{
						//Customer exists
						
						Element customerElement  = (Element)getParentCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
						Element buyerOrgElement = (Element)customerElement.getElementsByTagName(XPXLiterals.E_BUYER_ORGANIZATION).item(0);
						
						billToName = buyerOrgElement.getAttribute(XPXLiterals.A_ORGANIZATION_NAME);
						
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("The exception is: "+e.getMessage());
				} 
		    }
		
		
		}
		
		
		/* Start - changes made for CR 1360 */
        NodeList instructionsList = inputDocRoot.getElementsByTagName("Instruction");
        int instructionListSize = instructionsList.getLength();
        log.debug("instructionListSize = " + instructionListSize);
        for(int i=0;i<instructionListSize;i++){
              Element instructionElement = (Element) instructionsList.item(i);              
              if(instructionElement.hasAttribute("InstructionType")){
                    String instructionType = instructionElement.getAttribute("InstructionType");
                    log.debug("instructionType = " + instructionType);
                    if(!YFCObject.isNull(instructionType) && !YFCObject.isVoid(instructionType) 
                                && (instructionType.equalsIgnoreCase("HEADER") ) ){
                    	setWebHoldFlag = true;
                          break;
                    }
              }
        }           
        /* End - changes made for CR 1360 */
		
		
		if((willCallFlag != null && willCallFlag.trim().length() != 0 && willCallFlag.equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_Y))
				|| (orderUpdateFlag != null && orderUpdateFlag.trim().length() != 0 && orderUpdateFlag.equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_N))
				|| (setWebHoldFlag == true))
		{
			log.debug("Setting webHoldFlag as Y");
			webHoldFlag=XPXLiterals.BOOLEAN_FLAG_Y;
		}
		
		/*if(webConfirmationNumber != null && webConfirmationNumber.trim().length() != 0)
		{*/
			//log.debug("The Web Confirmation Number is: "+ webConfirmationNumber);

			if(orderExtn != null)
			{		
				
				/*if(orderExtn.getAttribute(XPXLiterals.A_WEB_CONF_NUMBER) == null   || orderExtn.getAttribute(XPXLiterals.A_WEB_CONF_NUMBER).trim().length()==0)
				{
				       orderExtn.setAttribute(XPXLiterals.A_WEB_CONF_NUMBER, webConfirmationNumber);
				}*/
				if(webHoldFlag != null && webHoldFlag.trim().length() != 0 && webHoldFlag.equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_Y))
				{
					orderExtn.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_FLAG,webHoldFlag);
					
					if((XPXLiterals.CONSTANT_CHAR_P).equalsIgnoreCase(willCallFlag) &&
							!(XPXLiterals.BOOLEAN_FLAG_N).equalsIgnoreCase(orderUpdateFlag))
					{
						log.debug("Inside first if");
						orderExtn.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON,XPXLiterals.WEB_HOLD_FLAG_REASON_1);
					}
					
					else if((XPXLiterals.BOOLEAN_FLAG_N).equalsIgnoreCase(orderUpdateFlag) &&
							!(XPXLiterals.CONSTANT_CHAR_P).equalsIgnoreCase(willCallFlag))
					{
						log.debug("Inside second if");
						orderExtn.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON,XPXLiterals.WEB_HOLD_FLAG_REASON_2);
					}
					
					else if ((XPXLiterals.CONSTANT_CHAR_P).equalsIgnoreCase(willCallFlag) &&
							(XPXLiterals.BOOLEAN_FLAG_N).equalsIgnoreCase(orderUpdateFlag))
					{
						log.debug("Inside third if");
						orderExtn.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON,XPXLiterals.WEB_HOLD_FLAG_REASON_1_AND_2);
					}
						
					
				}
				
				if(orderExtn.getAttribute(XPXLiterals.A_EXTN_CUSTOMER_NO)==null || orderExtn.getAttribute(XPXLiterals.A_EXTN_CUSTOMER_NO).trim().length()<=0)
				{
				   orderExtn.setAttribute(XPXLiterals.A_EXTN_CUSTOMER_NO, legacyCustomerNo);
				}
				orderExtn.setAttribute(XPXLiterals.A_EXTN_SOURCE_TYPE, sourceType);
				//orderExtn.setAttribute(XPXLiterals.A_EXTN_TOTAL_ORDER_VALUE, lineSubTotal);
				orderExtn.setAttribute(XPXLiterals.A_SAP_PARENT_NAME, masterCustomerName);
				if(orderExtn.getAttribute(XPXLiterals.A_EXTN_SHIP_TO_NAME)==null || orderExtn.getAttribute(XPXLiterals.A_EXTN_SHIP_TO_NAME).trim().length()<=0)
				{
				   orderExtn.setAttribute(XPXLiterals.A_EXTN_SHIP_TO_NAME, shipToName);
				}
				orderExtn.setAttribute(XPXLiterals.A_EXTN_BILL_TO_NAME, billToName);
				if(orderExtn.getAttribute(XPXLiterals.A_EXTN_BILL_TO_SUFFIX)==null || orderExtn.getAttribute(XPXLiterals.A_EXTN_BILL_TO_SUFFIX).trim().length()<=0)
				{
				   orderExtn.setAttribute(XPXLiterals.A_EXTN_BILL_TO_SUFFIX, billToSuffix);
				}
				if(orderExtn.getAttribute(XPXLiterals.A_EXTN_SHIP_TO_SUFFIX)==null || orderExtn.getAttribute(XPXLiterals.A_EXTN_SHIP_TO_SUFFIX).trim().length()<=0)
				{
				   orderExtn.setAttribute(XPXLiterals.A_EXTN_SHIP_TO_SUFFIX, shipToSuffix);
				}
				orderExtn.setAttribute("ExtnOrigEnvironmentCode", customerEnvtId);
				orderExtn.setAttribute("ExtnOrderDivision", customerOrderingDivision);
				if(orderExtn.getAttribute("ExtnCustomerDivision")==null || orderExtn.getAttribute("ExtnCustomerDivision").trim().length()<=0)
				{
				   orderExtn.setAttribute("ExtnCustomerDivision", customerDivision);
				}
				environment_id = orderExtn.getAttribute(XPXLiterals.A_EXTN_ENVT_ID);
				if(environment_id == null || environment_id.trim().length()<=0)
				{
					environment_id = envtCode;
					orderExtn.setAttribute(XPXLiterals.A_EXTN_ENVT_ID,environment_id);
					log.debug("Stamping the environment id");
				}
				company_code = orderExtn.getAttribute(XPXLiterals.A_EXTN_COMPANY_CODE);
				if(company_code == null || company_code.trim().length()<=0)
				{
					company_code = compCode;
					orderExtn.setAttribute(XPXLiterals.A_EXTN_COMPANY_CODE,company_code);
					log.debug("Stamping the company code");
				}
				if(customerContactID != null && customerContactID .trim().length()>0)
				{
					//ExtnOrderedByName to be stamped only if entry type !=B2B
					//Getting the user name of the customerContact to stamp as OrderedByName
					
					String extnOrderedByName = orderExtn.getAttribute("ExtnOrderedByName");
					log.debug("The extnOrderedByName is: "+extnOrderedByName);
					
					if(extnOrderedByName == null || extnOrderedByName.trim().length()<=0)
					{
					   Document getCustomerContactListInputDoc = YFCDocument.createDocument("CustomerContact").getDocument();
					   getCustomerContactListInputDoc.getDocumentElement().setAttribute("CustomerContactID", customerContactID);
					   log.debug("The input to getCustomerContactList is: "+SCXmlUtil.getString(getCustomerContactListInputDoc));
					   Document getCustomerContactListOutputDoc = api.invoke(env, "getCustomerContactList", getCustomerContactListInputDoc);
					   log.debug("The output of getCustomerContactList is: "+SCXmlUtil.getString(getCustomerContactListOutputDoc));
					   if(getCustomerContactListOutputDoc.getDocumentElement().getElementsByTagName("CustomerContact").getLength()>0)
					    {
						Element customerContactElement = (Element) getCustomerContactListOutputDoc.getDocumentElement().getElementsByTagName("CustomerContact").item(0);
						String firstName = customerContactElement.getAttribute("FirstName");
						String lastName =  customerContactElement.getAttribute("LastName");
						orderedByName = firstName + "" + lastName;
						orderExtn.setAttribute("ExtnOrderedByName",orderedByName);
					     }
					}
				}
			}
			
		//}

			
			
		//Make complex query calls to ItemBranch, ItemCustXRef and to item catalog	
			
	    Document getComplexQueryOutputForXref = invokeComplexQueryForXref(env,inputXML,legacyCustomerNo,environment_id,company_code,customer_branch);
	    Document getComplexQueryOutputForItemBranch = invokeComplexQueryForItemBranch(env,inputXML,legacyCustomerNo,environment_id,company_code,customer_branch);
	    Document getComplexQueryOutputForItem = invokeComplexQueryForItem(env,inputXML,customer_branch);
			
		orderLines = (Element)inputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);

		orderLineList = orderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);	

		for(int i=0; i < orderLineList.getLength() ; i++)
		{
			Element orderLine = (Element)orderLineList.item(i);
			
		    String lineStatus = orderLine.getAttribute("Status");
			if(log.isDebugEnabled()){
			log.debug("The status of the line is: "+lineStatus);
			}
		    /***Added for CR # 2591 by Prasanth Kumar M.**************/
		    if(!"Cancelled".equalsIgnoreCase(lineStatus))
		    {	
			lineType = orderLine.getAttribute(XPXLiterals.A_LINE_TYPE);
			log.debug("lineType"+lineType);
			orderLineKey = orderLine.getAttribute(XPXLiterals.A_ORDER_LINE_KEY);
			orderLineShipNode = orderLine.getAttribute(XPXLiterals.A_SHIP_NODE);
			
			if(orderLineShipNode == null || orderLineShipNode.trim().length()<=0)
			{
				orderLineShipNode = customer_branch;
			}
			
			//Fix for defect # 804 and # 806 by Prasanth Kumar M. on 08/02/2011
			if(orderLineShipNode != null && orderLineShipNode.trim().length() >0)
			{
				String[] splitArrayOnorderLineShipNode =  orderLineShipNode.split("_");
	            
	            for(int j=0; j<splitArrayOnorderLineShipNode.length; j++)
	            {
	           	 if(j==0)
	           	 {
	           		orderLineShipNode = splitArrayOnorderLineShipNode[j];
	           		log.debug("The order line ship node after stripping off division code is: "+orderLineShipNode);
	           	 }
	            }
			}

			Element orderLineExtn = (Element) orderLine.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
			Element linePriceInfo = (Element) orderLine.getElementsByTagName(XPXLiterals.E_LINE_PRICE_INFO).item(0);
			
			/***********************Added as per new reqmt for switcing the unit price and extn discounted unit price********/
						
			if(orderLineExtn!=null && linePriceInfo!=null)
			{
			String extnDiscountedUnitPrice = orderLineExtn.getAttribute(XPXLiterals.A_EXTN_UNIT_PRICE_DISCOUNT);
			log.debug("The extn discount price before switch is: "+extnDiscountedUnitPrice);
			
			
			String unitPrice = linePriceInfo.getAttribute(XPXLiterals.A_UNIT_PRICE);
			log.debug("The unitPrice before switch is: "+unitPrice);
			
			
			//orderLineExtn.setAttribute(XPXLiterals.A_EXTN_UNIT_PRICE_DISCOUNT, unitPrice);
			//linePriceInfo.setAttribute(XPXLiterals.A_UNIT_PRICE, extnDiscountedUnitPrice);
			//linePriceInfo.setAttribute(XPXLiterals.A_IS_PRICE_LOCKED, XPXLiterals.BOOLEAN_FLAG_Y);
			}
			/*****************************************************************************************************************/
			
			
			Element lineOverallTotals = (Element) orderLine.getElementsByTagName(XPXLiterals.E_LINE_OVERALL_TOTALS).item(0);
			if(lineOverallTotals!=null)
			{
			extendedPrice = lineOverallTotals.getAttribute(XPXLiterals.A_EXTENDED_PRICE);
			}
			
			/*Element awardsElement = (Element) orderLine.getElementsByTagName(XPXLiterals.E_AWARDS).item(0);
			if(awardsElement!=null)
			{
				Element awardElement = (Element) orderLine.getElementsByTagName(XPXLiterals.E_AWARD).item(0);
				if(awardElement!=null)
				{
					awardAmount = awardElement.getAttribute(XPXLiterals.A_AWARD_AMOUNT);
				}
				
			}*/
			//Getting the next unique sequence number from the custom sequence created
			/*long uniqueSequenceNo = CallDBSequence.getNextDBSequenceNo(env, XPXLiterals.WEB_LINE_SEQUENCE);

			webLineNumber = generateWebLineNumber(uniqueSequenceNo);*/
			
		    /*//Stamping a unique values as shipNode to check is separate split orders are created
			
			if(i%3 == 0)
			{
			orderLine.setAttribute(XPXLiterals.A_SHIP_NODE, "TestNode1");
			}
			else if(i%3 == 1)
			{
				orderLine.setAttribute(XPXLiterals.A_SHIP_NODE, "TestNode2");	
			}
			else if(i%3 == 2)
			{
				orderLine.setAttribute(XPXLiterals.A_SHIP_NODE, "TestNode3");	
			}
			
			*//************Temporary code*************************************/
			
			item = (Element) orderLine.getElementsByTagName(XPXLiterals.E_ITEM).item(0);
			itemId = item.getAttribute(XPXLiterals.A_ITEM_ID);

			/*if(webLineNumber != null && webLineNumber.trim().length() != 0)
			{*/		

				if(orderLineExtn != null)
				{
					//orderLineExtn.setAttribute(XPXLiterals.A_WEB_LINE_NUMBER,webLineNumber);
					//orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_ORDERED_TOTAL, extendedPrice);
					//orderLineExtn.setAttribute(XPXLiterals.A_EXTN_ADJUST_DOLLAR_AMOUNT, awardAmount);
					
                     //Querying XPX_ITEMCUST_XREF to get the Customer Part No.
					
					log.debug("The environment id used to query XREF table is: "+environment_id);					
					/*Document XREFOutputDoc = invokeXREF(env,itemId,legacyCustomerNo,environment_id,company_code,customer_branch);
					if(XREFOutputDoc!=null)
					{
					     Element XREFOutputDocRoot = XREFOutputDoc.getDocumentElement();
					     NodeList XREFCustElementList = XREFOutputDocRoot.getElementsByTagName(XPXLiterals.E_XPX_ITEM_CUST_XREF);
					     if(XREFCustElementList.getLength() > 0)
					     {
						        Element XREFElement = (Element) XREFCustElementList.item(0);
						        customerItem = XREFElement.getAttribute("CustomerItemNumber");
						        log.debug("The customer part no is: "+customerItem);
						        item.setAttribute(XPXLiterals.A_CUSTOMER_ITEM, customerItem);
					     }					
				     }*/
					
					if(getComplexQueryOutputForXref!=null)
					{
						
						Element XREFOutputDocRoot = getComplexQueryOutputForXref.getDocumentElement();
						NodeList XREFCustElementList = XREFOutputDocRoot.getElementsByTagName(XPXLiterals.E_XPX_ITEM_CUST_XREF);
						if(XREFCustElementList.getLength() > 0 && !"M".equalsIgnoreCase(lineType) && !"C".equalsIgnoreCase(lineType))
					     {
							for(int custXRefCounter=0;custXRefCounter<XREFCustElementList.getLength();custXRefCounter++ )
							{	
							    Element XREFElement = (Element) XREFCustElementList.item(custXRefCounter);
							    String legacyItemNumber = XREFElement.getAttribute("LegacyItemNumber");
							    if(itemId.equalsIgnoreCase(legacyItemNumber))
							    {
								 customerItem = XREFElement.getAttribute("CustomerItemNumber");
								if(log.isDebugEnabled()){
								 log.debug("The customer item no is: "+customerItem);
								}
								 item.setAttribute(XPXLiterals.A_CUSTOMER_ITEM, customerItem);
								 break;
							    }
							}  
					     }
					}
					
					
					
				}
				
			//}

			try 
			{
				/*****************New logic added on 01/07/2010 for line type determination*********************/
				
				/*Document getXPXItemBranchListInputDoc = createXPXItemBranchListInputDoc(itemId,orderLineShipNode,environment_id);
				log.debug("The input to getXPXitemBranchExtnListService is: "+SCXmlUtil.getString(getXPXItemBranchListInputDoc));				
				Document getXPXItemBranchListOutputDoc = api.executeFlow(env,XPXLiterals.GET_XPX_ITEM_BRANCH_LIST_SERVICE, getXPXItemBranchListInputDoc);				
				Element getXPXItemBranchListOutputDocRoot = getXPXItemBranchListOutputDoc.getDocumentElement();
				Element XPXItemExtnElement = (Element)getXPXItemBranchListOutputDocRoot.getElementsByTagName(XPXLiterals.E_XPX_ITEM_EXTN).item(0);
				
				if(XPXItemExtnElement!=null)
				{
					inventoryIndicator = XPXItemExtnElement.getAttribute(XPXLiterals.A_INVENTORY_INDICATOR);
										
					*//**********Changing the logic as per Rajendra, the value of INVENTORY_INDICATOR 
					 * will be either 'W'(Stock)/ 'I'(Stock) or 'M'(Direct)****//*
					 Changes made for issue 1501 
					if("W".equalsIgnoreCase(inventoryIndicator) || "I".equalsIgnoreCase(inventoryIndicator))
					{	
						log.debug("Line type is a stock !!!");
						orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, "STOCK");
					}
					
					if("M".equalsIgnoreCase(inventoryIndicator))
					{
						//orderLine.setAttribute(XPXLiterals.A_LINE_TYPE, "N");
					    orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, "DIRECT");
					}
				}*/
				
				Element getXPXItemBranchListOutputDocRoot = getComplexQueryOutputForItemBranch.getDocumentElement();
				NodeList XPXItemExtnElementList = getXPXItemBranchListOutputDocRoot.getElementsByTagName(XPXLiterals.E_XPX_ITEM_EXTN);
				
				if(XPXItemExtnElementList.getLength()>0 && !"M".equalsIgnoreCase(lineType) && !"C".equalsIgnoreCase(lineType))
				{
				  for(int j=0; j<XPXItemExtnElementList.getLength(); j++)
				  {  
					Element XPXItemExtnElement = (Element) XPXItemExtnElementList.item(j);
					
					if(itemId.equalsIgnoreCase(XPXItemExtnElement.getAttribute(XPXLiterals.A_ITEM_ID)))
					{
					     inventoryIndicator = XPXItemExtnElement.getAttribute(XPXLiterals.A_INVENTORY_INDICATOR);
										
					     //**********Changing the logic as per Rajendra, the value of INVENTORY_INDICATOR 
					     // * will be either 'W'(Stock)/ 'I'(Stock) or 'M'(Direct)****//*
					     // Changes made for issue 1501 
					    if("W".equalsIgnoreCase(inventoryIndicator) || "I".equalsIgnoreCase(inventoryIndicator))
					    {	
					    	if(log.isDebugEnabled()){
					    		log.debug("Line type is a stock !!!");
					    	}
						   orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, "STOCK");
						   break;
					    }
					
					    else if("M".equalsIgnoreCase(inventoryIndicator))
					    {
						   orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, "DIRECT");
						   break;
					    }
					    
					    else
					    {
					    	//Meaning inventory indicator is empty..Pawan said on 25/08/2011 to treat it like a DIRECT item by checking catalog
					    	
					    	itemListDocRoot = getComplexQueryOutputForItem.getDocumentElement();
					    	NodeList ItemElementList = itemListDocRoot.getElementsByTagName(XPXLiterals.E_ITEM);
							
							if(ItemElementList.getLength() > 0 && !"M".equalsIgnoreCase(lineType) && !"C".equalsIgnoreCase(lineType))
							{
							  for(int itemIterator=0;itemIterator<ItemElementList.getLength();itemIterator++)
							  {	  
								  Element itemElem = (Element) ItemElementList.item(itemIterator);
							      if(itemId.equalsIgnoreCase(itemElem.getAttribute("ItemID")))
							      { 
								     orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, XPXLiterals.DIRECT);
								     break;
							      }	 
							  }   
							}
									
							
						  else
						    {
							   if(lineType.equals("M"))
							   {
								//Item does not exist in YFS_ITEM table and its a Special charge line
								orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, XPXLiterals.STOCK);
								break;
							   }
							   if(lineType.equalsIgnoreCase("C"))
							   {
								//Item does not exist in YFS_ITEM table
								//orderLine.setAttribute(XPXLiterals.A_LINE_TYPE, XPXLiterals.STOCK);
								orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, XPXLiterals.STOCK);
								break;
							    }
							
						     }
					    	
					    }
					   					    
					 } 
					else
					{
						//Meaning the item was not available in item branch output(where othere items are present) and so has to be checked in item catalog
						itemListDocRoot = getComplexQueryOutputForItem.getDocumentElement();
						
						NodeList ItemElementList = itemListDocRoot.getElementsByTagName(XPXLiterals.E_ITEM);
						
						if(ItemElementList.getLength() > 0 && !"M".equalsIgnoreCase(lineType) && !"C".equalsIgnoreCase(lineType))
						{
						  for(int itemIterator=0;itemIterator<ItemElementList.getLength();itemIterator++)
						  {	  
							  Element itemElem = (Element) ItemElementList.item(itemIterator);
						      if(itemId.equalsIgnoreCase(itemElem.getAttribute("ItemID")))
						      { 
						    	if(log.isDebugEnabled()){
						    	 log.debug("Setting item type as direct");
						    	}
							     orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, XPXLiterals.DIRECT);
							     break;
						      }	 
						  }   
						}
					}
				  }    
				}
				
				else
				{
					
					/***Commented out as per Rajendra's comments  on 11/03 ****/
				    //Checking Item exists in YFS_ITEM
					/*getItemListInputDoc = createItemListInputDoc(itemId);
				    env.setApiTemplate(XPXLiterals.GET_ITEM_LIST_API, template);
				    outputCustomerDoc = api.invoke(env,XPXLiterals.GET_ITEM_LIST_API, getItemListInputDoc);
				    env.clearApiTemplate(XPXLiterals.GET_ITEM_LIST_API);

				
				itemListDocRoot = outputCustomerDoc.getDocumentElement();
				
				if(itemListDocRoot.getElementsByTagName(XPXLiterals.E_ITEM).getLength() > 0 )
				{
				   //Item exists in YFS_ITEM table	
				  // orderLine.setAttribute(XPXLiterals.A_LINE_TYPE, XPXLiterals.STOCK);
					orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, XPXLiterals.DIRECT);
				
				}	*/
					if(log.isDebugEnabled()){
					log.debug("Into the else loop of no item branch record");
					}
					itemListDocRoot = getComplexQueryOutputForItem.getDocumentElement();
					
					NodeList ItemElementList = itemListDocRoot.getElementsByTagName(XPXLiterals.E_ITEM);
					
					if(ItemElementList.getLength() > 0 && !"M".equalsIgnoreCase(lineType) && !"C".equalsIgnoreCase(lineType))
					{
					  for(int itemIterator=0;itemIterator<ItemElementList.getLength();itemIterator++)
					  {	  
						  Element itemElem = (Element) ItemElementList.item(itemIterator);
					      if(itemId.equalsIgnoreCase(itemElem.getAttribute("ItemID")))
					      { 
					    	if(log.isDebugEnabled()){
					    	 log.debug("Setting item type as direct");
					    	  }
						     orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, XPXLiterals.DIRECT);
						     break;
					      }	 
					  }   
					}
					
				else
				{
					if(lineType.equals("M"))
					{
						//Item does not exist in YFS_ITEM table and its a Special charge line
						orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, XPXLiterals.STOCK);
						if(log.isDebugEnabled()){
						log.debug("Setting item type as stock");
						}
						
					}
					if(lineType.equalsIgnoreCase("C"))
					{
						//Item does not exist in YFS_ITEM table
						//orderLine.setAttribute(XPXLiterals.A_LINE_TYPE, XPXLiterals.STOCK);
						orderLineExtn.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, XPXLiterals.STOCK);
					}
					
				}
				
				
				}
				
				
			} 


			catch (Exception e)
			{				
				log.error("The exception is: "+e.getMessage());
			}
		  }
		}			

		log.endTimer("XPXAddParametersAPI.beforeChangeOrder");
		
		if(log.isDebugEnabled()){
		log.debug("The final input to changeOrder from XPXAddParameters API is: "+SCXmlUtil.getString(inputXML));
		}
		//this was changed for special chargeline requirement
		//log.debug("inputDocRoot"+SCXmlUtil.getString(inputDocRoot));
		/*Document newInputDoc = YFCDocument.createDocument().getDocument();
		newInputDoc.appendChild(newInputDoc.importNode(inputDocRoot, true));
		newInputDoc.renameNode(newInputDoc.getDocumentElement(), newInputDoc.getNamespaceURI(), "Order");*/
		return inputXML;
		/*log.debug("newInputDoc"+SCXmlUtil.getString(newInputDoc));
		return newInputDoc;*/
	}


	private Document invokeComplexQueryForItemBranch(YFSEnvironment env, Document inputXML, String legacyCustomerNo, String environment_id,
			String company_code, String customer_branch) 
	{
		Document getComplexQueryOutputForItemBranch = null;
		 try
		 {	
			Document getComplexQueryInputForItemBranch = YFCDocument.createDocument(XPXLiterals.E_XPX_ITEM_EXTN).getDocument();
			getComplexQueryInputForItemBranch.getDocumentElement().setAttribute("EnvironmentID",environment_id);
			
			Element complexQuery = getComplexQueryInputForItemBranch.createElement("ComplexQuery");
			complexQuery.setAttribute("Operator", "AND");
			getComplexQueryInputForItemBranch.getDocumentElement().appendChild(complexQuery);
			
			Element primaryOrElement = getComplexQueryInputForItemBranch.createElement("Or");
			complexQuery.appendChild(primaryOrElement);
			
			Element inputDocRoot = inputXML.getDocumentElement();
			Element orderLines = (Element)inputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);

			NodeList orderLineList = orderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);	
	        for(int i=0; i < orderLineList.getLength() ; i++)
			{
				Element orderLineElement = (Element) orderLineList.item(i);
				Element itemElement = (Element) orderLineElement.getElementsByTagName(XPXLiterals.E_ITEM).item(0);
				String itemID= itemElement.getAttribute(XPXLiterals.A_ITEM_ID);
	            String orderLineShipNode = orderLineElement.getAttribute(XPXLiterals.A_SHIP_NODE);
				
				if(orderLineShipNode == null || orderLineShipNode.trim().length()<=0)
				{
					orderLineShipNode = customer_branch;
				}

                 if(orderLineShipNode.contains("_"))
				{
					String[] splitArrayOnorderLineShipNode =  orderLineShipNode.split("_");
		            
		            for(int j=0; j<splitArrayOnorderLineShipNode.length; j++)
		            {
		           	 if(j==0)
		           	 {
		           		orderLineShipNode = splitArrayOnorderLineShipNode[j];
		           		
		           	 }
		            }
				}
				Element secondaryOrElement = getComplexQueryInputForItemBranch.createElement("Or");
				primaryOrElement.appendChild(secondaryOrElement);
				
				Element secondaryAndElement = getComplexQueryInputForItemBranch.createElement("And");
				secondaryOrElement.appendChild(secondaryAndElement);
																
				Element customerDivisionExpElement = getComplexQueryInputForItemBranch.createElement("Exp");
				customerDivisionExpElement.setAttribute("Name", "XPXDivision");
				
				customerDivisionExpElement.setAttribute("Value", orderLineShipNode);
				secondaryAndElement.appendChild(customerDivisionExpElement);
				
				Element legacyItemIdExpElement = getComplexQueryInputForItemBranch.createElement("Exp");
				legacyItemIdExpElement.setAttribute("Name", XPXLiterals.A_ITEM_ID);
				
				legacyItemIdExpElement.setAttribute("Value", itemID);
				secondaryAndElement.appendChild(legacyItemIdExpElement);
			}
	    	if(log.isDebugEnabled()){
	        log.debug("The complex query input to getItemBranchListService is: "+SCXmlUtil.getString(getComplexQueryInputForItemBranch));
	    	}
	        getComplexQueryOutputForItemBranch = api.executeFlow(env, "getItemBranchListForOPService", getComplexQueryInputForItemBranch);
	    	if(log.isDebugEnabled()){
	        log.debug("The complex query output of getItemBranchListService is: "+SCXmlUtil.getString(getComplexQueryOutputForItemBranch));
	    	}
		  }
		  catch(Exception e)
		  {
			  com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
				
		      errorObject.setTransType("OP");
		      errorObject.setErrorClass("Application");
		      errorObject.setInputDoc(inputXML);
		      		      
		      errorObject.setException(e);
		
		      ErrorLogger.log(errorObject, env);
		  }
			return getComplexQueryOutputForItemBranch;
	}


	private Document invokeComplexQueryForItem(YFSEnvironment env, Document inputXML, String customer_branch)
	{
		Document getComplexQueryOutputForItem = null;
		
		try
		{
			Document getComplexQueryInputForItem = YFCDocument.createDocument(XPXLiterals.E_ITEM).getDocument();
			getComplexQueryInputForItem.getDocumentElement().setAttribute("OrganizationCode",inputXML.getDocumentElement().getAttribute("EnterpriseCode"));
			
			Element complexQuery = getComplexQueryInputForItem.createElement("ComplexQuery");
			complexQuery.setAttribute("Operator", "AND");
			getComplexQueryInputForItem.getDocumentElement().appendChild(complexQuery);
			
			Element primaryAndElement = getComplexQueryInputForItem.createElement("And");
			complexQuery.appendChild(primaryAndElement);
			
			Element secondaryOrElement = getComplexQueryInputForItem.createElement("Or");
			primaryAndElement.appendChild(secondaryOrElement);
			
			Element inputDocRoot = inputXML.getDocumentElement();
			Element orderLines = (Element)inputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);

			NodeList orderLineList = orderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);	
	        for(int i=0; i < orderLineList.getLength() ; i++)
			{
				Element orderLineElement = (Element) orderLineList.item(i);
				Element itemElement = (Element) orderLineElement.getElementsByTagName(XPXLiterals.E_ITEM).item(0);
				String itemID= itemElement.getAttribute(XPXLiterals.A_ITEM_ID);
				
				Element itemIdExpElement = getComplexQueryInputForItem.createElement("Exp");
				itemIdExpElement.setAttribute("Name", "ItemID");
				itemIdExpElement.setAttribute("Value", itemID);
				secondaryOrElement.appendChild(itemIdExpElement);
				
			}
	    	
	        env.setApiTemplate(XPXLiterals.GET_ITEM_LIST_API, template);
	        getComplexQueryOutputForItem = api.invoke(env, "getItemList", getComplexQueryInputForItem);
	        env.clearApiTemplate(XPXLiterals.GET_ITEM_LIST_API);
	    	if(log.isDebugEnabled()){
	        log.debug("The complex query output of getItemListService is: "+SCXmlUtil.getString(getComplexQueryOutputForItem));
	    	}
			
			
		}
		catch(Exception e)
		{
			   com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
			
		      errorObject.setTransType("OP");
		      errorObject.setErrorClass("Application");
		      errorObject.setInputDoc(inputXML);
		      		      
		      errorObject.setException(e);
		
		      ErrorLogger.log(errorObject, env);
		}
		
		
		return getComplexQueryOutputForItem;
	}

	

	private Document invokeComplexQueryForXref(YFSEnvironment env, Document inputXML, String legacyCustomerNo, String environment_id,
			String company_code, String customer_branch)
	{
		Document getComplexQueryOutputForXref = null;
	 try
	 {	
		Document getComplexQueryInputForXref = YFCDocument.createDocument(XPXLiterals.E_XPX_ITEM_CUST_XREF).getDocument();
		getComplexQueryInputForXref.getDocumentElement().setAttribute(XPXLiterals.A_ENVT_CODE, environment_id);
		
		Element complexQuery = getComplexQueryInputForXref.createElement("ComplexQuery");
		complexQuery.setAttribute("Operator", "AND");
		getComplexQueryInputForXref.getDocumentElement().appendChild(complexQuery);
		
		Element primaryOrElement = getComplexQueryInputForXref.createElement("Or");
		complexQuery.appendChild(primaryOrElement);
		
		Element inputDocRoot = inputXML.getDocumentElement();
		Element orderLines = (Element)inputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);

		NodeList orderLineList = orderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);	
        for(int i=0; i < orderLineList.getLength() ; i++)
		{
			Element orderLineElement = (Element) orderLineList.item(i);
			Element itemElement = (Element) orderLineElement.getElementsByTagName(XPXLiterals.E_ITEM).item(0);
			String itemID= itemElement.getAttribute(XPXLiterals.A_ITEM_ID);
            String orderLineShipNode = orderLineElement.getAttribute(XPXLiterals.A_SHIP_NODE);
            
			if(orderLineShipNode == null || orderLineShipNode.trim().length()<=0)
			{
				orderLineShipNode = customer_branch;
			}
			
			if(orderLineShipNode.contains("_"))
			{
				String[] splitArrayOnorderLineShipNode =  orderLineShipNode.split("_");
	            
	            for(int j=0; j<splitArrayOnorderLineShipNode.length; j++)
	            {
	           	 if(j==0)
	           	 {
	           		orderLineShipNode = splitArrayOnorderLineShipNode[j];
	           		
	           	 }
	            }
			}
			
			Element secondaryOrElement = getComplexQueryInputForXref.createElement("Or");
			primaryOrElement.appendChild(secondaryOrElement);
			
			Element secondaryAndElement = getComplexQueryInputForXref.createElement("And");
			secondaryOrElement.appendChild(secondaryAndElement);
			
			Element customerNumberExpElement = getComplexQueryInputForXref.createElement("Exp");
			customerNumberExpElement.setAttribute("Name", XPXLiterals.A_CUSTOMER_NO);
			customerNumberExpElement.setAttribute("Value", legacyCustomerNo);
			secondaryAndElement.appendChild(customerNumberExpElement);
			
			/*Element envtCodeExpElement = getComplexQueryInputForXref.createElement("Exp");
			envtCodeExpElement.setAttribute("Name", XPXLiterals.A_ENVT_CODE);
			envtCodeExpElement.setAttribute("Value", environment_id);
			secondaryAndElement.appendChild(envtCodeExpElement);*/
			
			Element customerDivisionExpElement = getComplexQueryInputForXref.createElement("Exp");
			customerDivisionExpElement.setAttribute("Name", "CustomerDivision");
			customerDivisionExpElement.setAttribute("Value", orderLineShipNode);
			secondaryAndElement.appendChild(customerDivisionExpElement);
			
			Element legacyItemIdExpElement = getComplexQueryInputForXref.createElement("Exp");
			legacyItemIdExpElement.setAttribute("Name", XPXLiterals.A_LEGACY_ITEM_NO);
			legacyItemIdExpElement.setAttribute("Value", itemID);
			secondaryAndElement.appendChild(legacyItemIdExpElement);
		}
        
    	
        getComplexQueryOutputForXref = api.executeFlow(env, "getItemCustXrefListService", getComplexQueryInputForXref);
    	if(log.isDebugEnabled()){
        log.debug("The complex query output of getItemCustXrefListService is: "+SCXmlUtil.getString(getComplexQueryOutputForXref));
    	}
	  }
	  catch(Exception e)
	  {
		  com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
			
	      errorObject.setTransType("OP");
	      errorObject.setErrorClass("Application");
	      errorObject.setInputDoc(inputXML);
	      		      
	      errorObject.setException(e);
	
	      ErrorLogger.log(errorObject, env);
	  }
		return getComplexQueryOutputForXref;
	}


	private Document createXPXItemBranchListInputDoc(String itemId, String orderLineShipNode, String environment_id) {

                 Document getXPXItemBranchListInputDoc = createDocument(XPXLiterals.E_XPX_ITEM_EXTN);
                 getXPXItemBranchListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ITEM_ID, itemId);
                 getXPXItemBranchListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_XPX_DIVISION,orderLineShipNode);
                 getXPXItemBranchListInputDoc.getDocumentElement().setAttribute("EnvironmentID",environment_id);
		
		return getXPXItemBranchListInputDoc;
	}


	private Document invokeXREF(YFSEnvironment env, String itemId, String legacyCustomerNo, String environment_id, String company_code, String customer_branch) {

		
		/*<XPXItemcustXref BillToSuffix=" " CapsId=" " CompanyCode=" "
    ConvFactor=" " Createprogid=" " Createts=" " Createuserid=" "
    CustomerBranch=" " CustomerDecription=" " CustomerDivision=" "
    CustomerNumber=" " CustomerPartNumber=" " CustomerUnit=" "
    EnvironmentCode=" " IsCustUOMExcl=" " ItemcustRefKey=" "
    LegacyBase=" " LegacyItemNumber=" " Lockid=" " MPC=" "
    Modifyprogid=" " Modifyts=" " Modifyuserid=" " ShipToSuffix=" " SuffixType=" "/> */ 
		
		      Document XREFOutputDoc = null; 
		
              Document XREFInputDoc = createDocument(XPXLiterals.E_XPX_ITEM_CUST_XREF);
              XREFInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENVT_CODE, environment_id);
              XREFInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_COMPANY_CODE, company_code);
              XREFInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_NO,legacyCustomerNo);
              //XREFInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_BRANCH,customer_branch);
              XREFInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_LEGACY_ITEM_NO,itemId);
		
		      log.debug("The input to getXrefList is: "+SCXmlUtil.getString(XREFInputDoc));
              //<XPXItemcustXrefList />
              
              try {
            	  
            	  //log.debug("The input to getXRefList is: "+SCXmlUtil.getString(XREFInputDoc));
				XREFOutputDoc = api.executeFlow(env, XPXLiterals.GET_XREF_LIST, XREFInputDoc);
				
				log.debug("The output of getXrefList is: "+SCXmlUtil.getString(XREFOutputDoc));
			} catch (YFSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return XREFOutputDoc;
	}


	private Document createGetCustomerListInput(YFSEnvironment env, String customerId) {
		
		
		Element firstApiElement = null;
		Element secondApiElement = null;

		Document getCustomerListInputDoc = createDocument(XPXLiterals.E_MULTI_API);
        
		if(customerId!=null || customerId.trim().length()!=0)
		{
        /************First API Input and template****************************************/
        firstApiElement = getCustomerListInputDoc.createElement(XPXLiterals.E_API);
        firstApiElement.setAttribute(XPXLiterals.A_NAME, XPXLiterals.GET_CUSTOMER_LIST_API);
        
        Element firstInputElement = getCustomerListInputDoc.createElement(XPXLiterals.E_INPUT);
        
        Element firstInputCustomerElement = getCustomerListInputDoc.createElement(XPXLiterals.E_CUSTOMER);
        firstInputCustomerElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, customerId);
        firstInputElement.appendChild(firstInputCustomerElement);
        
        Element firstTemplateElement = getCustomerListInputDoc.createElement(XPXLiterals.E_TEMPLATE);
        
        Element firstOutputCustomerListElement = getCustomerListInputDoc.createElement(XPXLiterals.E_CUSTOMER_LIST);
        Element firstOutputCustomerElement = getCustomerListInputDoc.createElement(XPXLiterals.E_CUSTOMER);
        firstOutputCustomerElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, "");
        Element firstCustomerExtnElement = getCustomerListInputDoc.createElement(XPXLiterals.E_EXTN);
        firstCustomerExtnElement.setAttribute(XPXLiterals.A_EXTN_ORDER_UPDATE_FLAG, "");
        firstCustomerExtnElement.setAttribute(XPXLiterals.A_EXTN_LEGACY_CUST_NO,"");
        firstCustomerExtnElement.setAttribute(XPXLiterals.A_EXTN_SAP_PARENT_NAME,"");
        firstCustomerExtnElement.setAttribute("ExtnCustOrderBranch","");
        firstCustomerExtnElement.setAttribute("ExtnCustomerDivision","");
        
        firstCustomerExtnElement.setAttribute("ExtnOrigEnvironmentCode","");
        
        Element firstCustomerBuyerOrgElement = getCustomerListInputDoc.createElement(XPXLiterals.E_BUYER_ORGANIZATION);
        firstCustomerBuyerOrgElement.setAttribute(XPXLiterals.A_ORGANIZATION_NAME, "");
        firstOutputCustomerElement.appendChild(firstCustomerExtnElement);
        firstOutputCustomerElement.appendChild(firstCustomerBuyerOrgElement);
        
        
        //Adding the Parent customer element to the template so as to retrieve the ShipTo's BillTo customer id
        
        Element firstCustomerParentElement = getCustomerListInputDoc.createElement(XPXLiterals.E_PARENT_CUSTOMER);
        firstCustomerParentElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, "");
        firstOutputCustomerElement.appendChild(firstCustomerParentElement);
        firstOutputCustomerListElement.appendChild(firstOutputCustomerElement);
        
        firstTemplateElement.appendChild(firstOutputCustomerListElement);
        
        firstApiElement.appendChild(firstTemplateElement);
        firstApiElement.appendChild(firstInputElement);
       /************************************************************************************/
		}
        /******************Second API input and template************************************//*
        
		if(billToId!=null || billToId.trim().length()!=0)
		{
         secondApiElement = getCustomerListInputDoc.createElement(XPXLiterals.E_API);
        secondApiElement.setAttribute(XPXLiterals.A_NAME, XPXLiterals.GET_CUSTOMER_LIST_API);
        
        Element secondInputElement = getCustomerListInputDoc.createElement(XPXLiterals.E_INPUT);
        
        
        Element secondInputCustomerElement = getCustomerListInputDoc.createElement(XPXLiterals.E_CUSTOMER);
        secondInputCustomerElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, billToId);
        secondInputElement.appendChild(secondInputCustomerElement);
        
        Element secondTemplateElement = getCustomerListInputDoc.createElement(XPXLiterals.E_TEMPLATE);
        
        Element secondOutputCustomerListElement = getCustomerListInputDoc.createElement(XPXLiterals.E_CUSTOMER_LIST);
        Element secondOutputCustomerElement = getCustomerListInputDoc.createElement(XPXLiterals.E_CUSTOMER);
        secondOutputCustomerElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, "");
        Element secondCustomerExtnElement = getCustomerListInputDoc.createElement(XPXLiterals.E_EXTN);
        secondCustomerExtnElement.setAttribute(XPXLiterals.A_EXTN_ORDER_UPDATE_FLAG, "");
        secondCustomerExtnElement.setAttribute(XPXLiterals.A_EXTN_LEGACY_CUST_NO,"");
        secondCustomerExtnElement.setAttribute(XPXLiterals.A_EXTN_SAP_PARENT_NAME,"");
        secondCustomerExtnElement.setAttribute("ExtnCustOrderBranch","");
        secondCustomerExtnElement.setAttribute("ExtnCustomerDivision","");
        secondCustomerExtnElement.setAttribute("ExtnOrigEnvironmentCode","");
        
        Element secondCustomerBuyerOrgElement = getCustomerListInputDoc.createElement(XPXLiterals.E_BUYER_ORGANIZATION);
        secondCustomerBuyerOrgElement.setAttribute(XPXLiterals.A_ORGANIZATION_NAME, "");
        secondOutputCustomerElement.appendChild(secondCustomerExtnElement);
        secondOutputCustomerElement.appendChild(secondCustomerBuyerOrgElement);
        secondOutputCustomerListElement.appendChild(secondOutputCustomerElement);
        
        secondTemplateElement.appendChild(secondOutputCustomerListElement);
        
        secondApiElement.appendChild(secondTemplateElement);
        secondApiElement.appendChild(secondInputElement);
        
        *//*****************************************************************************************//*
		}*/
        getCustomerListInputDoc.getDocumentElement().appendChild(firstApiElement);
       // getCustomerListInputDoc.getDocumentElement().appendChild(secondApiElement);
             
             
             
             
             
            // getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, billToId);
		      
             return getCustomerListInputDoc;
	}


	public static String generateWebLineNumber(String entryType,long uniqueSequenceNo,String envtCode) {
		String webLineNumber = "";
		String uniqueSequence = "";
		String formatted = "";

		int uniqueSequenceLength = 8;
		int keyLength = String.valueOf(uniqueSequenceNo).length();
		log.debug("KeyLength = "+keyLength);
		if(keyLength < 8)
		{
			formatted = String.format("%08d", uniqueSequenceNo); 

			log.debug("Number with leading zeros: " + formatted); 
		}
		else if(keyLength > 8)
		{

			int startIndex = keyLength-uniqueSequenceLength;
			formatted = String.valueOf(uniqueSequenceNo).substring(startIndex);

		}
		else
		{
			formatted = String.valueOf(uniqueSequenceNo);
		}
		
		/* Changes made to fix issue 926 
		IF order is placed from B2B,WEB or COM instead of appending Environment ID Changed it to constant('E') */
		if(entryType != null && (XPXLiterals.SOURCE_TYPE_B2B.equals(entryType) || XPXLiterals.SOURCE_WEB.equals(entryType) 
				|| XPXLiterals.SOURCE_COM.equals(entryType))){
			envtCode = XPXLiterals.SYSTEM_SPECIFIER_WEB;
		}				
		
		webLineNumber = envtCode+formatted;

		return webLineNumber;
	}


	



//	private String generateWebConfirmationNumber(String orderHeaderKey) {
//
//		String webConfirmationNumber = "";
//		String uniqueSequence = "";
//		String year = ""; 
//		String month = "";
//		String day = "";
//		int uniqueSequenceLength = 7;
//		int orderHeaderKeylength = orderHeaderKey.trim().length();
//
//		if (orderHeaderKey != null && orderHeaderKeylength != 0 
//				&& orderHeaderKeylength > 8)
//		{
//			year = orderHeaderKey.substring(2,4);
//			month = orderHeaderKey.substring(4,6);
//			day = orderHeaderKey.substring(6,8);
//
//			int startIndex = orderHeaderKeylength-uniqueSequenceLength;
//			uniqueSequence = orderHeaderKey.substring(startIndex);
//
//		}	
//
//		webConfirmationNumber = year+month+day+"A"+uniqueSequence;
//
//		return webConfirmationNumber;
//	}


	private Document createItemListInputDoc(String itemId)  {

		Document getItemListInputDoc = createDocument(XPXLiterals.E_ITEM);

		Element getItemListInputDocRoot = getItemListInputDoc.getDocumentElement();

		getItemListInputDocRoot.setAttribute(XPXLiterals.A_ITEM_ID, itemId);

		return getItemListInputDoc;
	}

	private Document createDocument(String docElementTag) {

		Document doc = getNewDocument();
		Element ele = doc.createElement(docElementTag);
		doc.appendChild(ele);
		return doc;


	}

	public static Document getNewDocument() {
		return new DocumentImpl();
	}

	public static Document invokeAPI(YFSEnvironment env, String templateName,
			String apiName, Document inDoc) throws Exception {

		env.setApiTemplate(apiName, templateName);
		Document returnDoc = api.invoke(env, apiName, inDoc);
		env.clearApiTemplate(apiName);

		return returnDoc;
	}

	public static Element getFirstElementByName(Element ele, String tagName) {
		StringTokenizer st = new StringTokenizer(tagName, "/");
		Element curr = ele;
		Node node;
		String tag;

		while (st.hasMoreTokens()) {
			tag = st.nextToken();
			node = curr.getFirstChild();
			while (node != null) {
				if (node.getNodeType() == Node.ELEMENT_NODE
						&& tag.equals(node.getNodeName())) {
					break;
				}
				node = node.getNextSibling();
			}

			if (node != null)
				curr = (Element) node;
			else
				return null;
		}

		return curr;
	}
	
	private String getShipNodeFromCustomer(YFSEnvironment env, Document inputDoc) throws RemoteException {
		String shipNode = "";
		YFCDocument getCustomerListTemplate = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER_LIST);
		YFCElement eCustomerListTemp = getCustomerListTemplate.getDocumentElement();
		YFCElement eCustomerTemp = getCustomerListTemplate.createElement(XPXLiterals.E_CUSTOMER);
		eCustomerListTemp.appendChild(eCustomerTemp);
		YFCElement eExtnTemp = getCustomerListTemplate.createElement(XPXLiterals.E_EXTN);
		eCustomerTemp.appendChild(eExtnTemp);
		eExtnTemp.setAttribute(XPXLiterals.A_EXTN_SERVICE_DIVISION, "");
		eExtnTemp.setAttribute(XPXLiterals.A_EXTN_CUSTOMER_ORDER_BRANCH, "");
		eExtnTemp.setAttribute(XPXLiterals.A_EXTN_ENVIRONMENT_CODE, "");
		eExtnTemp.setAttribute("ExtnShipFromBranch", "");
		
		String billToID = inputDoc.getDocumentElement().getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
		//form the input xml
		Document inputCustomerDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER).getDocument();
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, billToID);
		
		env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate.getDocument());
		Document outputCustomerListDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, inputCustomerDoc);
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		Element outputCustomerListElement = outputCustomerListDoc.getDocumentElement();
		NodeList customerList = outputCustomerListElement.getElementsByTagName(XPXLiterals.E_CUSTOMER);
		int cLength = customerList.getLength();
		if(cLength > 0)
		{
			Element customerElement = (Element)customerList.item(0);
			shipNode = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnShipFromBranch");
			if(shipNode.equals(""))
			{
				shipNode = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnCustOrderBranch");
			}
			//String strEnvtId = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnEnvironmentCode");
			//shipNode = XPXUtils.updateNodeSyntax(strEnvtId, shipNode);
		}
		return shipNode;
	}
	
	private Document setOrderListTemplate(YFSEnvironment env)throws RemoteException
	{
		//set output template
		Document orderTemplateDoc = YFCDocument.createDocument("OrderList").getDocument();
		Element orderTemplateElement = orderTemplateDoc.getDocumentElement();
		Element orderElement = orderTemplateDoc.createElement("Order");
		orderTemplateElement.appendChild(orderElement);
		Element orderExtnElement = orderTemplateDoc.createElement("Extn");
		orderElement.appendChild(orderExtnElement);
		Element priceInfoEleemnt = orderTemplateDoc.createElement("PriceInfo");
		orderElement.appendChild(priceInfoEleemnt);
		Element oLinesElement = orderTemplateDoc.createElement("OrderLines");
		orderElement.appendChild(oLinesElement);
		Element oLineElement = orderTemplateDoc.createElement("OrderLine");
		oLinesElement.appendChild(oLineElement);
		Element orderLineExtnElement = orderTemplateDoc.createElement("Extn");
		oLineElement.appendChild(orderLineExtnElement);
		Element itemElement = orderTemplateDoc.createElement("Item");
		oLineElement.appendChild(itemElement);
		Element linePriceElement = orderTemplateDoc.createElement("LinePriceInfo");
		oLineElement.appendChild(linePriceElement);
		Element orderLineTranQuantityElement = orderTemplateDoc.createElement("OrderLineTranQuantity");
		oLineElement.appendChild(orderLineTranQuantityElement);
		Element personInfoShipToElement = orderTemplateDoc.createElement("PersonInfoShipTo");
		orderElement.appendChild(personInfoShipToElement);
		Element personInfoBillToElement = orderTemplateDoc.createElement("PersonInfoBillTo");
		orderElement.appendChild(personInfoBillToElement);
		return orderTemplateDoc;
	}

	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}






}