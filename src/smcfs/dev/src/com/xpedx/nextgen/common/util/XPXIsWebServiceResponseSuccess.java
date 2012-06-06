package com.xpedx.nextgen.common.util;

/**
 * Description: WebService call transaction has been checked for Success/Failure. 
 * Condition has been called in Order edit and Order place flow after web service invocation. 
 * 
 * @author Stanislaus Joseph John
 *
 */

import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.dashboard.XPXEditOrderWebServiceInvocationAPI;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXIsWebServiceResponseSuccess implements YCPDynamicConditionEx {
	
	private static final Logger log = Logger.getLogger(XPXEditOrderWebServiceInvocationAPI.class);

	@Override
	public boolean evaluateCondition(YFSEnvironment env, String arg1,
			Map arg2, Document inputDoc) throws YFSException {
		
		String orderEditFlag = "";		
		Element orderElement = inputDoc.getDocumentElement();	
		
		// To check whether the condition is called in Order Edit flow.
		if(orderElement.hasAttribute("IsOrderEdit")){
		orderEditFlag = orderElement.getAttribute("IsOrderEdit");
		}
		
		NodeList tranStatusList = (NodeList)orderElement.getElementsByTagName("TransactionStatus");
		if(tranStatusList.getLength() > 0){
		Node tranStatusNode = (Node)tranStatusList.item(0);
		String tranStatus = tranStatusNode.getTextContent();		
			// Transaction status has been checked for Failure.
			if(tranStatus != null && tranStatus.equalsIgnoreCase("F")){
				if(orderEditFlag.equalsIgnoreCase("Y")){
					// Order Edit Flow - Error logged in CENT and thrown to revert the Order changes made in sterling database.
					log.debug("Order Edit - Transaction has failed when trying to modify order in Legacy.");	
					YFSException oeException = new YFSException();
					oeException.setErrorDescription("Transaction has failed when trying to modify order in Legacy.");				
					prepareErrorObject(oeException, "Order Edit", XPXLiterals.YFE_ERROR_CLASS, env, inputDoc);
					/*Begin - Changes made by Mitesh Parikh for JIRA 3045*/
	 				env.setTxnObject("OrderEditTransactionFailure", "System Error - Please try after sometime.");
	 				/*End - Changes made by Mitesh Parikh for JIRA 3045*/
					throw oeException;
				} else {
					// Order Place flow.
					log.debug("Order Place - Return false.");
				return false; 
				}
			}
		}
		log.debug("Return true as the transaction is success.");
		return true;
	}
	
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML){
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();	
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}

	@Override
	public void setProperties(Map arg0) {
		// TODO Auto-generated method stub
		
	}

	

	

}
