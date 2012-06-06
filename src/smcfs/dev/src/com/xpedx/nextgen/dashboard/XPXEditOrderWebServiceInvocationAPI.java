package com.xpedx.nextgen.dashboard;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrder;
import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrderE;
import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrderResponseE;
import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.WsIpaperPlaceOrderStub;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXEditOrderWebServiceInvocationAPI implements YIFCustomApi{

	private static Properties props;
	private static final YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	
	public Document invokeWebService(YFSEnvironment env, Document inputXML) throws Exception
	{
			
			log.debug("OrderEditInterface_InputXML:" + SCXmlUtil.getString(inputXML));	
            Document orderEditResponseDoc = null;
            String endPointURL = null;
            
            endPointURL = YFSSystem.getProperty("OrderPlaceWSDL");
            if(endPointURL ==  null || endPointURL.trim().length()<=0)
            {//No customer overrides entry so property value is retrieved from the SDF
                     endPointURL = props.getProperty("ENDPOINT_URL");
            }
            
            WsIpaperPlaceOrderStub testStub = new WsIpaperPlaceOrderStub(endPointURL);
            //Setting the time out for the Order placement and Edit order based on the email from pawan dated July 15th 2011
			Integer timeoutInMilliSecs = getTimeoutInMilliSecondsForWebService();
			testStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(timeoutInMilliSecs);
			//End of Setting the time out
            FPlaceOrderE inputOrderXml = new FPlaceOrderE();
            FPlaceOrder inputPlaceOrder = new FPlaceOrder();
            String inputXMLString = SCXmlUtil.getString(inputXML);
            log.debug("Input xml for OrderPlace to Legacy: "+inputXMLString);
            inputPlaceOrder.setWsIpaperPlaceOrderInput(inputXMLString);
            inputOrderXml.setFPlaceOrder(inputPlaceOrder);
            FPlaceOrderResponseE orderResponse=null;
			try {
				orderResponse = testStub.fPlaceOrder(inputOrderXml);
			
			}catch (Exception oeException){
				oeException = new Exception("Transaction has failed when trying to invoke Edit Order webservice."); 				
 				prepareErrorObject(oeException, "Order Edit", XPXLiterals.YFE_ERROR_CLASS, env, inputXML);
 				/*Begin - Changes made by Mitesh Parikh for JIRA 3045*/
 				env.setTxnObject("OrderEditTransactionFailure", "System Error - Please try after sometime.");
 				/*End - Changes made by Mitesh Parikh for JIRA 3045*/
				throw oeException;
				
			}

            orderEditResponseDoc = YFCDocument.getDocumentFor(orderResponse.getFPlaceOrderResponse().getWsIpaperPlaceOrderOutput()).getDocument();
             
            log.debug("OrderEditInterface_LegacyResponse:" + SCXmlUtil.getString(orderEditResponseDoc));
            // Throw error to revert the transaction if there a transaction failure message from legacy.
            Element orderElement = orderEditResponseDoc.getDocumentElement(); 
            NodeList tranStatusList = (NodeList)orderElement.getElementsByTagName("TransactionStatus");
     		if(tranStatusList.getLength() > 0){
     			Node tranStatusNode = (Node)tranStatusList.item(0);
     			String tranStatus = tranStatusNode.getTextContent();
     			// Transaction status has been checked for Failure.
     			if(tranStatus != null && tranStatus.equalsIgnoreCase("F")){
     				// Error logged in CENT and thrown to revert the Order changes made in sterling database.
     				log.debug("Order Edit - Transaction has failed when trying to modify order in Legacy.");	
     				Exception oeException = new Exception("Transaction has failed when trying to modify order in Legacy.");
     				// setErrorDescription("Transaction has failed when trying to modify order in Legacy.");
     				prepareErrorObject(oeException, "Order Edit", XPXLiterals.YFE_ERROR_CLASS, env, inputXML);
     				/*Begin - Changes made by Mitesh Parikh for JIRA 3045*/
     				env.setTxnObject("OrderEditTransactionFailure", "System Error - Please try after sometime.");
     				/*End - Changes made by Mitesh Parikh for JIRA 3045*/
     				throw oeException;
     			}
     		}
               
            if(null!=orderEditResponseDoc){
            	  // Flag set to identify order edit interface when doing order update.
            	  orderEditResponseDoc.getDocumentElement().setAttribute("IsOrderEdit", "Y");
	              String outputXMLString = SCXmlUtil.getString(orderEditResponseDoc);
	              log.debug("Output xml for OrderPlace from Legacy: "+outputXMLString);
             } else {
	              log.error("Output from Legacy for Order placement is empty");
             }
             
             return orderEditResponseDoc;

}
	
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML){
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();	
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		this.props = arg0;
	}
	
	private Integer getTimeoutInMilliSecondsForWebService(){
		//setting the time out for the order placement
		String timeout = null;
		Integer timeoutInSecs;
		
		timeout = YFSSystem.getProperty("EditOrderWebServiceTimeOut");
		if(timeout == null || timeout.trim().length() <= 0) 
		{// setting the default to 300 sec
			timeout = "300";
		}
		try {
			timeoutInSecs= Integer.parseInt(timeout);
		}
		catch (NumberFormatException e) {
			log.error("Exception: " + e.getStackTrace());
			timeoutInSecs = 300;
		}
		Integer timeoutInMilliSecs = timeoutInSecs*1000;
		return timeoutInMilliSecs;
	}

	

}
