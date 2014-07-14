package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXInvokeConfirmDraftOrderCreationAPI implements YIFCustomApi
{

	private static YIFApi api = null;
	private static YFCLogCategory log;
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
	
	public Document invokeConfirmDraftOrder(YFSEnvironment env, Document inputDoc) throws Exception
    {
		
		String orderHeaderKey = inputDoc.getDocumentElement().getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
    	try 
    	{
    		log.debug(SCXmlUtil.getString(inputDoc));
    		
    		//inputDoc.getDocumentElement().setAttribute("OrderHeaderKey", "123");
			Document outputDocument = api.invoke(env, "confirmDraftOrder", inputDoc);
			
			/****Commented out as part of fix for defect # 1952 where thsi loic is moved upfront in the flow to XPXUpdateReprocessFlag clas***/
			
			/*//Retrieve ref Order Header key from the environment object
			
			String referenceOrderHeaderKey= (String) env.getTxnObject("ReferenceOrderHeaderKey");
			log.debug("The reference order header key is: "+referenceOrderHeaderKey);
			
			Document changeRefOrderInputDocument = YFCDocument.createDocument("XPXRefOrderHdr").getDocument();
			changeRefOrderInputDocument.getDocumentElement().setAttribute("RefOrderHdrKey", referenceOrderHeaderKey);
			changeRefOrderInputDocument.getDocumentElement().setAttribute("IsReprocessibleFlag", "N");
			//changeRefOrderInputDocument.getDocumentElement().setAttribute("OrderProcessedFlag", "N");
			
			log.debug("The input to change XPX Reference order header is: "+SCXmlUtil.getString(changeRefOrderInputDocument));
			api.executeFlow(env,"changeXPXRefOrderHdr",changeRefOrderInputDocument);*/
			
			
		} 
    	
    	catch (Exception e)
    	{
			//Handle the confirmDraftOrderException case...invoke deleteOrderAPI to remove the draft order record
    		
    		Document deleteOrderInputDoc = YFCDocument.createDocument("Order").getDocument();
    		deleteOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, orderHeaderKey);
    		
    		try 
    		{
    			log.debug("The delete Order input doc is:"+SCXmlUtil.getString(deleteOrderInputDoc));
				api.invoke(env, "deleteOrder", deleteOrderInputDoc);
				
			} catch (YFSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
			throw e;
		}
    	return inputDoc;
    }
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
