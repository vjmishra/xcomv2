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

public class XPXCheckETradingIdCondition implements YCPDynamicConditionEx
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
		boolean isEtradingIdInavlidOrMissing = false;
		String eTradingId = null;
		
		Element inputXMLRoot = inputXML.getDocumentElement();
		
		//String buyerId = SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderHeader/OrderParty/BuyerParty/Party/PartyID/Identifier/Ident").getTextContent();
		String buyerId = SCXmlUtil.getXpathElement(inputXMLRoot, "./BuyerId").getTextContent();
		
		Document getSAPCustomerDetailsOutputDoc = getSAPCustomerDetailsOutput(env,buyerId); 
		Element sapCustomerElement = (Element) getSAPCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
		String sapCustOrgCode = sapCustomerElement.getAttribute(XPXLiterals.A_ORGANIZATION_CODE);
		
		//String eTradingId = SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderHeader/OrderParty/ShipToParty/" +
		//"Party/PartyID/Identifier/Ident").getTextContent();
		if(SCXmlUtil.getXpathElement(inputXMLRoot, "./EtradingId")!=null)
		{
		  eTradingId = SCXmlUtil.getXpathElement(inputXMLRoot, "./EtradingId").getTextContent();
		  

			if(eTradingId==null || eTradingId.trim().length()<=0)
			{
				isEtradingIdInavlidOrMissing = true;	
				// Etrading Id is empty.
				inputXMLRoot.setAttribute("EtradingIDReason", "E");
			}
			else
			{
				Document getShipToCustomerDetailsOutputDoc = getCustomerDetailsOutput(env,eTradingId,sapCustOrgCode);
				
				if(getShipToCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).getLength()==0)
				{
					isEtradingIdInavlidOrMissing = true;
					// Etrading Id is Invalid.
					inputXMLRoot.setAttribute("EtradingIDReason", "I");
				}
			}
		}
		else
		{
			//No element for EtradingId itself
			isEtradingIdInavlidOrMissing = true;
			inputXMLRoot.setAttribute("EtradingIDReason", "NS");
		}
		

		
		
		return isEtradingIdInavlidOrMissing;
	}

	private Document getCustomerDetailsOutput(YFSEnvironment env, String eTradingId, String sapCustOrgCode) {
 
		   Document getCustomerDetailsOutputDoc = null;
				
           YFCDocument getCustomerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
           getCustomerDetailsInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, sapCustOrgCode);
           YFCElement extnElement = getCustomerDetailsInputDoc.createElement(XPXLiterals.E_EXTN);           
           extnElement.setAttribute("ExtnETradingID", eTradingId);
           
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
