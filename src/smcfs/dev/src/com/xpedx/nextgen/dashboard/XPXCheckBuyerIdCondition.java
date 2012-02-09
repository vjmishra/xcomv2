package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXCheckBuyerIdCondition implements YCPDynamicConditionEx
{
 
	private static YFCLogCategory log;
	private static YIFApi api = null;
	
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean evaluateCondition(YFSEnvironment env, String arg1, Map arg2, Document inputXML)
	{
		
		
		boolean isBuyerIdInavlidOrMissing = false;
		
		Element inputXMLRoot = inputXML.getDocumentElement();
		
		//String buyerId = SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderHeader/OrderParty/BuyerParty/Party/PartyID/Identifier/Ident").getTextContent();
		if(SCXmlUtil.getXpathElement(inputXMLRoot, "./BuyerId")!=null)
		{
		    String buyerId = SCXmlUtil.getXpathElement(inputXMLRoot, "./BuyerId").getTextContent();
		    
		    if(buyerId==null || buyerId.trim().length()<=0)
			{
				isBuyerIdInavlidOrMissing = true;
                com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
				
				errorObject.setTransType("B2B-PO");
				errorObject.setErrorClass("Unexpected / Invalid");
				errorObject.setInputDoc(inputXML);
				
				YFSException exceptionMessage = new YFSException();
				exceptionMessage.setErrorDescription("Empty value for BuyerId...thrown from XPXCheckBuyerIdCondition.java");
				
				errorObject.setException(exceptionMessage);
				
				ErrorLogger.log(errorObject, env);
				
			}
			else
			{
				Document getSAPCustomerDetailsOutputDoc = getSAPCustomerDetailsOutput(env,buyerId); 
				
				if(getSAPCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).getLength()==0)
				{
					isBuyerIdInavlidOrMissing = true;
                    com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
					
					errorObject.setTransType("B2B-PO");
					errorObject.setErrorClass("Unexpected / Invalid");
					errorObject.setInputDoc(inputXML);
					
					YFSException exceptionMessage = new YFSException();
					exceptionMessage.setErrorDescription("BuyerId sent in input xml is invalid...thrown from XPXCheckBuyerIdCondition.java");
					
					errorObject.setException(exceptionMessage);
					
					ErrorLogger.log(errorObject, env);
				}
				else
				{
					//SAP customer details exist in Sterling. Storing this in environment variable SAPCustomerProfile.
					env.setTxnObject("SAPCustomerProfile", getSAPCustomerDetailsOutputDoc);
				}
			}
		}
		else
		{
			//BuyerId element is not present
			isBuyerIdInavlidOrMissing = true;
             com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
			
			errorObject.setTransType("B2B-PO");
			errorObject.setErrorClass("Unexpected / Invalid");
			errorObject.setInputDoc(inputXML);
			
			YFSException exceptionMessage = new YFSException();
			exceptionMessage.setErrorDescription("BuyerId element is not sent in input xml...thrown from XPXCheckBuyerIdCondition.java");
			
			errorObject.setException(exceptionMessage);
			
			ErrorLogger.log(errorObject, env);
		}
		

		
		
		return isBuyerIdInavlidOrMissing;
	}

	private Document getSAPCustomerDetailsOutput(YFSEnvironment env, String buyerId)
	{
        Document getCustomerDetailsOutputDoc = null;
		
        YFCDocument getCustomerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
        YFCElement extnElement = getCustomerDetailsInputDoc.createElement(XPXLiterals.E_EXTN);
        extnElement.setAttribute("ExtnBuyerID", buyerId);
        
        getCustomerDetailsInputDoc.getDocumentElement().appendChild(extnElement);

         
		   try {
			   
			getCustomerDetailsOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerDetailsInputDoc.getDocument());
			
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getCustomerDetailsOutputDoc;
	}

	public void setProperties(Map arg0) {
		// TODO Auto-generated method stub
		
	}

}
