package com.xpedx.nextgen.common.cent;

import org.w3c.dom.Document;

public class Error {
	
	private Document inputDoc = null;
	private Exception exception = null;
	private String exceptionMessage = null;
	private String errorClass = null;
	private String transType = null;
	/** Added by Arun Sekhar on 31-Jan-20100 for CENT tool logging of EOF/SOF messages **/
	private String errorDesc = null;
	
	public Document getInputDoc() {
		return inputDoc;
	}
	public void setInputDoc(Document inputDoc) {
		this.inputDoc = inputDoc;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	public String getErrorClass() {
		return errorClass;
	}
	public void setErrorClass(String errorClass) {
		this.errorClass = errorClass;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}
