package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
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

public class XPXGetCSREmailIDFromSAPCustomerAPI implements YIFCustomApi
{
	
	private static YIFApi api = null;
	private static YFCLogCategory log;
	
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXB2BDraftOrderCreationService.xml";

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

	
	public Document getCSREmailId(YFSEnvironment env,Document b2bInputXML) throws Exception
	{
		   Document emailInputDoc = null;
		   String eTradingId = null;
		   Element inputDocRoot = b2bInputXML.getDocumentElement();
		   
		   String buyerId = SCXmlUtil.getXpathElement(inputDocRoot, "./BuyerId").getTextContent();
		   if(log.isDebugEnabled()){
		   		log.debug("The buyerid retrieved is: "+buyerId);
		   }
		   Document getSAPCustomerDetailsOutputDoc = getSAPCustomerDetailsOutput(env,buyerId); 
	 	   Element sapCustomerElement = (Element) getSAPCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
	 	   Element customerExtnElement = (Element) sapCustomerElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
	 	   
	 	   String csrEmailId = customerExtnElement.getAttribute("ExtnCSREMailID");
	 	   
	 	   if(csrEmailId==null || csrEmailId.trim().length()==0)
	 	   {
	 		   log.debug("The csr email id is not available and hence no mail is sent");
	 	   }
	 	   else
	 	   {
	 		   //Create input doc for sending to email component
	 		   
	 		   emailInputDoc = YFCDocument.createDocument("Order").getDocument();
	 		   emailInputDoc.getDocumentElement().setAttribute("EmailID",csrEmailId);
	 		  emailInputDoc.getDocumentElement().setAttribute("BuyerID",buyerId);
	 		   
	 		   String headerMessageId = SCXmlUtil.getXpathElement(inputDocRoot, "./LiaisonMessageId").getTextContent();
	 		   emailInputDoc.getDocumentElement().setAttribute("LiaisonHeaderMessageID", headerMessageId);
	 		   
	 		   if(SCXmlUtil.getXpathElement(inputDocRoot, "./EtradingId")!=null)
	 		   {
	 		      eTradingId = SCXmlUtil.getXpathElement(inputDocRoot, "./EtradingId").getTextContent(); 
	 		   }
	 		   
	 		   if(eTradingId == null || eTradingId.trim().length()==0)
	 		   {
	 			  emailInputDoc.getDocumentElement().setAttribute("ETradingID","NA");
	 		   }
	 		   else
	 		   {
	 			  emailInputDoc.getDocumentElement().setAttribute("ETradingID",eTradingId); 
	 		   }
	 		   
	 	   }
	 	   
	 	   
	 	return  emailInputDoc;   
	}
	
	private Document getSAPCustomerDetailsOutput(YFSEnvironment env, String buyerId) {

		Document getCustomerDetailsOutputDoc = null;
		
        YFCDocument getCustomerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
        YFCElement extnElement = getCustomerDetailsInputDoc.createElement(XPXLiterals.E_EXTN);
        extnElement.setAttribute("ExtnBuyerID", buyerId);
        
        getCustomerDetailsInputDoc.getDocumentElement().appendChild(extnElement);

         
		   try {
			   env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
			getCustomerDetailsOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerDetailsInputDoc.getDocument());
			env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getCustomerDetailsOutputDoc;
		
		
	}

	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
