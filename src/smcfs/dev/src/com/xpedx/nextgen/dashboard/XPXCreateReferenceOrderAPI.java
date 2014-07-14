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
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXCreateReferenceOrderAPI implements YIFCustomApi{
	
	private static YIFApi api = null;
	private static YFCLogCategory log;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");		
	}

	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXB2BDraftOrderCreationService.xml";
	public Document createReferenceOrder(YFSEnvironment env,Document inXML)
	{
		try
		{
		api = YIFClientFactory.getInstance().getApi();
		ArrayList<String> customerArray = new ArrayList<String>();
		Element inputElement = inXML.getDocumentElement();
		String buyerOrganizationCode = "";
		String billToID = "";
		String customerNo = "";
		String customerName = "";
		String customerDivision = "";
		
        Document getSAPCustomerDetailsOutputDoc = null;
		
		getSAPCustomerDetailsOutputDoc = (Document) env.getTxnObject("SAPCustomerProfile");
		if(getSAPCustomerDetailsOutputDoc==null)
		{
		   String buyerID = inputElement.getAttribute("BuyerID");
		   getSAPCustomerDetailsOutputDoc = getSAPCustomerDetailsOutput(env,buyerID);
		}
		Element sapCustomerElement = (Element) getSAPCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
		String sapCustOrgCode = sapCustomerElement.getAttribute(XPXLiterals.A_ORGANIZATION_CODE);	
		
		String etradingID = inputElement.getAttribute("EtradingID");
		if(etradingID!=null && etradingID.trim().length()>0)
		{	
			
		       customerArray = getCustomerDetails(env,etradingID,sapCustOrgCode);
		       if(!customerArray.isEmpty())
		       {
		    	   
		    	  if(!customerArray.get(0).isEmpty()) 
		    	  {
		    		  
		             buyerOrganizationCode = customerArray.get(0);
		    	  }
		    	  if(!customerArray.get(1).isEmpty())
		    	  {
		    		  
		              billToID = customerArray.get(1);
		    	  }
		    	  if(!customerArray.get(2).isEmpty())
		    	  {
		    	      customerNo = customerArray.get(2);
		    	  }
		    	  if(!customerArray.get(3).isEmpty())
		    	  {
		  		      customerName = customerArray.get(3);
		    	  }
		    	  if(!customerArray.get(4).isEmpty())
		    	  {
		    		  customerDivision = customerArray.get(4);
		    	  }
		          inputElement.setAttribute("BuyerOrganizationCode", buyerOrganizationCode);
		          inputElement.setAttribute("BillToId", billToID);
		          inputElement.setAttribute("CustomerNo", customerNo);
		  		  inputElement.setAttribute("CustomerName", customerName);
		  		  inputElement.setAttribute("CustomerDivision", customerDivision);
		       }  
		}
		
		
		inputElement.setAttribute("EnterpriseKey", sapCustOrgCode);
		
		Document referenceOrderHeaderOutput = api.executeFlow(env, "createXPXRefOrderHdr", inXML);

		// Changes made for CR - 850
		// To set the RefOrdHdrKey and RefOdrLineKey in transaction object.
		setRefDetailsInTransation(env,referenceOrderHeaderOutput);

		}
		catch(YFSException yfe)
		{
			com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
			
		      errorObject.setTransType("B2B-PO");
		      errorObject.setErrorClass("Application");
		      errorObject.setInputDoc(inXML);
		      		      
		      errorObject.setException(yfe);
		
		      ErrorLogger.log(errorObject, env);
		
            return inXML;
		}
		catch(Exception ex)
		{
			com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
			
		      errorObject.setTransType("B2B-PO");
		      errorObject.setErrorClass("Application");
		      errorObject.setInputDoc(inXML);
		      		      
		      errorObject.setException(ex);
		
		      ErrorLogger.log(errorObject, env);
		
            return inXML;
		}
		
		return inXML;
	}
	
	private Document getSAPCustomerDetailsOutput(YFSEnvironment env, String buyerID)
	{
        Document getCustomerDetailsOutputDoc = null;
		
        YFCDocument getCustomerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
        YFCElement extnElement = getCustomerDetailsInputDoc.createElement(XPXLiterals.E_EXTN);
        extnElement.setAttribute("ExtnBuyerID", buyerID);
        
        getCustomerDetailsInputDoc.getDocumentElement().appendChild(extnElement);

         
		   try {
			   //env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
			getCustomerDetailsOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerDetailsInputDoc.getDocument());
			//env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getCustomerDetailsOutputDoc;
	}

	private ArrayList<String> getCustomerDetails(YFSEnvironment env, String etradingID, String sapCustOrgCode) throws YFSException, RemoteException
	{
		String buyerOrgCode = "";
		String billToID = "";
		String customerNo = "";
		String customerName = "";
		String customerDivision = "";
		ArrayList<String> customerArray = new ArrayList<String>();
		//form input customer doc
		Document inputCustomerDoc = YFCDocument.createDocument("Customer").getDocument();
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE, sapCustOrgCode);
		Element extnCustomerElement = inputCustomerDoc.createElement("Extn");
		extnCustomerElement.setAttribute("ExtnETradingID", etradingID);
		inputCustomerElement.appendChild(extnCustomerElement);
		env.setApiTemplate("getCustomerList", getCustomerListTemplate);
		Document outputCustomerListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		env.clearApiTemplate("getCustomerList");
		NodeList customerList = outputCustomerListDoc.getElementsByTagName("Customer");
		int customerLength = customerList.getLength();
		if(customerLength != 0){
			Element customerElement = (Element)customerList.item(0);
			buyerOrgCode = customerElement.getAttribute("CustomerID");
			billToID = SCXmlUtil.getXpathAttribute(customerElement, "./ParentCustomer/@CustomerID");
			customerNo = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnLegacyCustNumber");
			customerName = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnCustomerName");
			customerDivision = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnCustomerDivision");
			customerArray.add(buyerOrgCode);
			customerArray.add(billToID);
			customerArray.add(customerNo);
			customerArray.add(customerName);
			customerArray.add(customerDivision);
		}
		
		return customerArray;
	}
	
	
	public static void setRefDetailsInTransation(YFSEnvironment env, Document inputXML)
	{		
		String refOrderLineKey = "";
		String msgLineId = "";
		Map refOrderLineMap = new HashMap();
		try{
		NodeList refOrderLineList = inputXML.getDocumentElement().getElementsByTagName("XPXRefOrderLine");
		int refOrderLineListSize = refOrderLineList.getLength();
		for(int lineCounter=0;lineCounter<refOrderLineListSize;lineCounter++){
		Element refOrderLineElement = (Element) refOrderLineList.item(lineCounter);
		log.debug("refOrderLineElement :" + SCXmlUtil.getString(refOrderLineElement));
		refOrderLineKey = refOrderLineElement.getAttribute("RefOrderLineKey");
		log.debug("refOrderLineKey :" + refOrderLineKey);
		msgLineId = refOrderLineElement.getAttribute("MsgLineId");
		log.debug("msgLineId:" + msgLineId);
		refOrderLineMap.put(msgLineId,refOrderLineKey);
		}		
		env.setTxnObject("refOrderLineMap", refOrderLineMap);						
		
        // Pass the refOrderKey to the environmnent's TransactionObject		
		String refOrderHeaderKey = inputXML.getDocumentElement().getAttribute("RefOrderHdrKey");
		log.debug("refOrderHeaderKey:" + refOrderHeaderKey);
		env.setTxnObject("ReferenceOrderHeaderKey", refOrderHeaderKey);
		}catch(Exception e){
			// Error logged in CENT tool.
			YFSException exceptionMessage = new YFSException();
			exceptionMessage.setErrorDescription("Exception occured in XPXCreateReferenceOrderAPI.java");		
			log.error("Exception occured in XPXCreateReferenceOrderAPI.java");				
			throw exceptionMessage;
		}				
				
	}

}
