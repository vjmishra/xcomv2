package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXCheckCustomerOrderUpdateFlagCondition implements YCPDynamicConditionEx{

	private static YFCLogCategory log;
	private static YIFApi api = null;
	
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXCreateChainedOrderService.xml";
	
	Document getCustomerListInputDoc = null;
	Document getCustomerListOutputDoc = null;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public boolean evaluateCondition(YFSEnvironment env, String arg1, Map arg2, Document inputDoc)
	
	 {        
		
		   String orderUpdateFlag = null;
		   
		   boolean isOrderUpdateFlagSet = false;
	
		  Element inputDocRoot = inputDoc.getDocumentElement();
		  
		  
		  /*********Changed as part of fix for Bug # 11643*******************************/
		  //String billToId = inputDocRoot.getAttribute(XPXLiterals.A_BILL_TO_ID);
		  
		  String buyerOrgCode = inputDocRoot.getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
		  
		  if(buyerOrgCode != null && buyerOrgCode.trim().length() !=0)
			{
			 
			getCustomerListInputDoc = createGetCustomerListInput(env,buyerOrgCode);
			
			env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
			try {
				getCustomerListOutputDoc = api.invoke(env,XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc);
			} catch (YFSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
			
			// Null check not done as it is confirmed that there will always be a customer of the value retrived from BillToId
			
			Element getCustomerListOutputRoot = getCustomerListOutputDoc.getDocumentElement();
			
			Element customerElement = (Element)getCustomerListOutputRoot.getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
			
			Element customerExtnElement = (Element)customerElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
			
			orderUpdateFlag = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_ORDER_UPDATE_FLAG);
			
			log.debug("The order update flag is: "+orderUpdateFlag);
		  
		    if(orderUpdateFlag.equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_N))
		    {
		    	isOrderUpdateFlagSet = true;
		    	
		    }
		    
			}
		
		return isOrderUpdateFlagSet;
	}


	private Document createGetCustomerListInput(YFSEnvironment env, String buyerOrgCode) {
		Document getCustomerListInputDoc = createDocument(XPXLiterals.E_CUSTOMER);
        
        getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID,buyerOrgCode);
	
        return getCustomerListInputDoc;
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


	public void setProperties(Map arg0) {
		// TODO Auto-generated method stub
		
	}
}
