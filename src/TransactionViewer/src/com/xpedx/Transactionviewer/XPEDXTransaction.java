package com.xpedx.Transactionviewer;

import java.util.HashMap;
import java.util.Map.Entry;

public class XPEDXTransaction {

	private String busTimestamp;
	private String webConfNumber;
	private String transactionType;
	private HashMap<String,String> messageMap = new HashMap<String, String>();
	
	public void setBusinessTimestamp(String strBusTimestamp) {
		this.busTimestamp = strBusTimestamp;
	}
	public String getBusinessTimestamp() {
		return busTimestamp;
	}
	public void setWebConfNumber(String strWebConfNumber) {
		this.webConfNumber = strWebConfNumber;
	}
	public String getWebConfNumber() {
		return webConfNumber;
	}
	public void setTransactionType(String strTransactionType) {
		this.transactionType = strTransactionType;
	}
	public String getTransactionType() {
		return transactionType;
	}
	
	public void addMessage(String messageId, String message) {
		getMessageMap().put(messageId, message);
	}
	
	public void setMessageMap(HashMap<String,String> messageMap) {
		this.messageMap = messageMap;
	}
	
	public HashMap<String,String> getMessageMap() {
		return messageMap;
	}
	public String getAllMsgIds() {
		String msgIds = null;
		for (Entry<String, String> entry : messageMap.entrySet()) {
		    String msgType = entry.getKey();
		    String msgId = entry.getValue();
		    
		    if(msgIds == null)
		    	msgIds = msgId;
		    else
		    	msgIds = msgIds + "," + msgId;
		    
		}
		return msgIds;
	}		
	
}
