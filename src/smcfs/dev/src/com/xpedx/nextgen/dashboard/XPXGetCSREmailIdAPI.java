package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXGetCSREmailIdAPI implements YIFCustomApi
{
	
	private static YIFApi api = null;

	private static YFCLogCategory log;

	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
            //Getting the YIF api handle
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}

	String getOrganizationListTemplate = "global/template/api/getOrganizationList.XPXSendCSREmailService.xml";
	String changeOrderTemplate = "global/template/api/changeOrder.XPXSendCSREmailService.xml";
	
	Document changeOrderOutputDoc = null;
	
	public Document getCSREmailId(YFSEnvironment env,Document getOrderDetailsOutput) throws Exception
	{
		
	    boolean needsAttentionHoldIsSet =false;	
	           
	    String webConfirmationNumber = null;
	    String csrEmailId = null;
	    if(log.isDebugEnabled()){
	    	log.debug("The input to XPXGetCSREmailIDAPI is: "+SCXmlUtil.getString(getOrderDetailsOutput));
	    }
		Element getOrderDetailsOutputRoot = getOrderDetailsOutput.getDocumentElement();
		
		
        String shipNode = getOrderDetailsOutputRoot.getAttribute(XPXLiterals.A_SHIP_NODE);
        String orderHeaderKey = getOrderDetailsOutputRoot.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
      	  
        /***********This is put in if the ShipNode is empty in the incoming order*******************/
      	  if(shipNode ==null || shipNode.trim().length()==0)
      	  {      		        		  
      		  shipNode = getShipNodeFromCustomer(env,getOrderDetailsOutput);
      		  
      	  }
      	/*************************************************************************************************/
      	  
      	      	  
      	Document getOrganizationListInputDoc = createDocument(XPXLiterals.E_ORGANIZATION);
      	  
      	getOrganizationListInputDoc.getDocumentElement().setAttribute("IsNode","Y");  
      	getOrganizationListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, shipNode);
      	getOrganizationListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_PARENT_ORGANIZATION_CODE,"xpedx");
      	
      	     	
      	  try
      	  {
      		  
      		env.setApiTemplate(XPXLiterals.GET_ORGANIZATION_LIST_API, getOrganizationListTemplate);	  
      	    Document getOrganizationListOutputDoc = api.invoke(env, XPXLiterals.GET_ORGANIZATION_LIST_API,getOrganizationListInputDoc);
      	    if(log.isDebugEnabled()){
      	    	log.debug("The getOrgList o/p is: "+SCXmlUtil.getString(getOrganizationListOutputDoc));
      	    }
      	    env.clearApiTemplate(XPXLiterals.GET_ORGANIZATION_LIST_API);  
      	  
      	     Element extnElem = (Element)getOrderDetailsOutputRoot.getElementsByTagName("Extn").item(0);
    	    webConfirmationNumber = extnElem.getAttribute(XPXLiterals.A_WEB_CONF_NUMBER);
    	    if(log.isDebugEnabled()){
    	    	log.debug("webConfirmationNumber for CSR Email Id: " + webConfirmationNumber);
    	    }
    	    if(webConfirmationNumber == null || webConfirmationNumber.trim().length()<=0)
    	    {
    	    	 webConfirmationNumber = generateWebConfirmationNumber(orderHeaderKey,getOrderDetailsOutputRoot);
    	    }
    	    
      	    if(getOrganizationListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_ORGANIZATION).getLength() > 0)
      	    {
      	    Element organizationElement = (Element)getOrganizationListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_ORGANIZATION).item(0);
      	    /*Element organizationExtnElement = (Element)organizationElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
      	    String csrEmailId = organizationExtnElement.getAttribute(XPXLiterals.A_EXTN_DIVISION_EMAIL_ID);*/
      	    
      	    Element corporatePersonInfoElement = (Element)organizationElement.getElementsByTagName(XPXLiterals.E_CORPROATE_PERSON_INFO).item(0);
      	    if(corporatePersonInfoElement!=null)
      	    {
      	       csrEmailId =  corporatePersonInfoElement.getAttribute(XPXLiterals.A_EMAIL_ID);    
      	    }
      	        	    
      	          	  
      	    Document changeOrderInputDoc = createChangeOrderInputDoc(env,orderHeaderKey,webConfirmationNumber);
      	    if(log.isDebugEnabled()){
      	    	log.debug("The changeOrder input doc is: "+SCXmlUtil.getString(changeOrderInputDoc));
      	    }
      	    env.setApiTemplate(XPXLiterals.CHANGE_ORDER_API, changeOrderTemplate);	  
    	    changeOrderOutputDoc = api.invoke(env, XPXLiterals.CHANGE_ORDER_API,changeOrderInputDoc);
    	    env.clearApiTemplate(XPXLiterals.CHANGE_ORDER_API);     
    	    if(log.isDebugEnabled()){
      	    	log.debug("The changeOrder Output doc is: "+SCXmlUtil.getString(changeOrderOutputDoc));
    	    }
      	      if(csrEmailId!=null && csrEmailId.trim().length() > 0) 
      	      {
    	         changeOrderOutputDoc.getDocumentElement().setAttribute("CSREmailId", csrEmailId);
    	         api.executeFlow(env, "XPXSendNeedsAttentionEmailService", changeOrderOutputDoc);
      	      }
      	      else
      	      {
      	    	if(log.isDebugEnabled()){
      	    		log.debug("There is no csr email id and hence no email will be sent.");
      	    	}
      	    	Document centErrorLoggerXmlInput = YFCDocument.createDocument("Order").getDocument();
      	    	Element extnCentErrorLogger = centErrorLoggerXmlInput.createElement("Extn");
      	    	extnCentErrorLogger.setAttribute("ExtnWebConfNum", webConfirmationNumber);
      	    	centErrorLoggerXmlInput.getDocumentElement().appendChild(extnCentErrorLogger);
      	    	
      	    	YFSException exceptionMessage = new YFSException();
      	    	exceptionMessage.setErrorDescription("There is no csr email id and hence no email will be sent");
      	    	com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
    			
		         errorObject.setTransType("OP");
		         errorObject.setErrorClass("Application");
		         errorObject.setInputDoc(centErrorLoggerXmlInput);
		      	 errorObject.setException(exceptionMessage);
		
		         ErrorLogger.log(errorObject, env);
      	      }
      	      
      	    }
      	    else
      	    {
      	    	Document centErrorLoggerXmlInput = YFCDocument.createDocument("Order").getDocument();
      	    	Element extnCentErrorLogger = centErrorLoggerXmlInput.createElement("Extn");
      	    	extnCentErrorLogger.setAttribute("ExtnWebConfNum", webConfirmationNumber);
      	    	centErrorLoggerXmlInput.getDocumentElement().appendChild(extnCentErrorLogger);
      	    	
      	    	YFSException exceptionMessage = new YFSException();
      	    	exceptionMessage.setErrorDescription("There is no such ship node: "+shipNode+" in the system");
      	    	com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
    			
		         errorObject.setTransType("OP");
		         errorObject.setErrorClass("Application");
		         errorObject.setInputDoc(centErrorLoggerXmlInput);
		      	 errorObject.setException(exceptionMessage);
		
		         ErrorLogger.log(errorObject, env);
      	    }
      	        	  
		  } 
      	  catch (YFSException e)
      	  {
      		 
      		     com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
			
		         errorObject.setTransType("OP");
		         errorObject.setErrorClass("Application");
		         errorObject.setInputDoc(getOrderDetailsOutput);
		      	 errorObject.setException(e);
		
		         ErrorLogger.log(errorObject, env);
		         
		         return getOrderDetailsOutput;
		  }
      	  catch (RemoteException e)
		    {
      		
			     com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
				
		         errorObject.setTransType("OP");
		         errorObject.setErrorClass("Application");
		         errorObject.setInputDoc(getOrderDetailsOutput);
		      	 errorObject.setException(e);
		
		         ErrorLogger.log(errorObject, env);
		         return getOrderDetailsOutput;
			}
      	catch (Exception ex)
    	  {
      		
    		     com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
			
		         errorObject.setTransType("OP");
		         errorObject.setErrorClass("Unknown Error");
		         errorObject.setInputDoc(getOrderDetailsOutput);
		      	 errorObject.setException(ex);
		
		         ErrorLogger.log(errorObject, env);
		         
		         return getOrderDetailsOutput;
		  }
        
	    
		return changeOrderOutputDoc;
		
       } 
	
	
	private String getShipNodeFromCustomer(YFSEnvironment env, Document inputDoc) throws RemoteException {
		String shipNode = "";
		YFCDocument getCustomerListTemplate = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER_LIST);
		YFCElement eCustomerListTemp = getCustomerListTemplate.getDocumentElement();
		YFCElement eCustomerTemp = getCustomerListTemplate.createElement(XPXLiterals.E_CUSTOMER);
		eCustomerListTemp.appendChild(eCustomerTemp);
		YFCElement eExtnTemp = getCustomerListTemplate.createElement(XPXLiterals.E_EXTN);
		eCustomerTemp.appendChild(eExtnTemp);
		//eExtnTemp.setAttribute(XPXLiterals.A_EXTN_SERVICE_DIVISION, "");
		eExtnTemp.setAttribute(XPXLiterals.A_EXTN_CUSTOMER_ORDER_BRANCH, "");
		eExtnTemp.setAttribute(XPXLiterals.A_EXTN_ENVIRONMENT_CODE, "");
		eExtnTemp.setAttribute("ExtnShipFromBranch", "");
		
		String shipToID = inputDoc.getDocumentElement().getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
		//form the input xml
		Document inputCustomerDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER).getDocument();
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, shipToID);
		
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
			if(log.isDebugEnabled()){
				log.debug("The ship from branch is : "+shipNode);
			}
			if(shipNode.equals(""))
			{
				shipNode = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnCustOrderBranch");
			}
			String strEnvtId = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnEnvironmentCode");
			shipNode = XPXUtils.updateNodeSyntax(strEnvtId, shipNode);
		}
		return shipNode;
	}


	private Document createChangeOrderInputDoc(YFSEnvironment env, String orderHeaderKey, String webConfirmationNumber) {


		         Document changeOrderInputDoc = createDocument(XPXLiterals.E_ORDER);
		         changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, orderHeaderKey);
		         changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTERPRISE_CODE, "xpedx");
		         changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_DOCUMENT_TYPE, "0001");
		         changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ACTION, XPXLiterals.MODIFY);
		         
		         
		         Element extnElement = changeOrderInputDoc.createElement(XPXLiterals.E_EXTN);
		         extnElement.setAttribute(XPXLiterals.A_WEB_CONF_NUMBER,webConfirmationNumber);
		         changeOrderInputDoc.getDocumentElement().appendChild(extnElement);
		         
		         
		return changeOrderInputDoc;
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
	
	private String generateWebConfirmationNumber(String orderHeaderKey, Element inputDocRoot) {

		String webConfirmationNumber = "";
		String uniqueSequence = "";
		String year = ""; 
		String month = "";
		String day = "";
		int uniqueSequenceLength = 7;
		int orderHeaderKeylength = orderHeaderKey.trim().length();
		
		YFCDate currentSystemDate = new YFCDate();
		String currentSystemDateString = currentSystemDate.toString();
		if(log.isDebugEnabled()){
			log.debug("The current systemDate is : "+currentSystemDateString);
		}
		year = currentSystemDateString.substring(2,4);
		month = currentSystemDateString.substring(4,6);
		day = currentSystemDateString.substring(6,8);
		
		if (orderHeaderKey != null && orderHeaderKeylength != 0 
				&& orderHeaderKeylength > 8)
		{
			

			int startIndex = orderHeaderKeylength-uniqueSequenceLength;
			uniqueSequence = orderHeaderKey.substring(startIndex);

		}	

		String entryType = inputDocRoot.getAttribute(XPXLiterals.A_ENTRY_TYPE);
		log.debug("Entry type = " + entryType);
		Element extnElem = (Element) inputDocRoot.getElementsByTagName("Extn").item(0);
		String envtCode = extnElem.getAttribute(XPXLiterals.A_EXTN_ENVIRONMENT_CODE);
		/* Changes made to fix issue 926 
		IF order is placed from B2B,WEB or COM  Environment ID is Changed to constant('E') */
		if(entryType != null && (XPXLiterals.SOURCE_TYPE_B2B.equals(entryType) || XPXLiterals.SOURCE_WEB.equals(entryType) 
				|| XPXLiterals.SOURCE_COM.equals(entryType))){
			envtCode = XPXLiterals.SYSTEM_SPECIFIER_WEB;
		}
		
		webConfirmationNumber = year+month+day+envtCode+uniqueSequence;
		if(log.isDebugEnabled()){
			log.debug("The web confirmation number is: "+webConfirmationNumber);
		}

		return webConfirmationNumber;
	}
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
