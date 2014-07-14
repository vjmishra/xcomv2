package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**@author asekhar-tw on 10-Jan-2011
 * Using the literals used for CENT tool logging 
 */
import com.xpedx.nextgen.common.util.XPXLiterals;

public class XPXSendInvoiceMessage {
	private static YIFApi api = null;

	private Properties arg0;

	private static YFCLogCategory log;

	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}

	public void setProperties(Properties arg0) throws Exception {
		this.arg0 = arg0;
		// TODO Auto-generated method stub

	}

	XPXUpdateInvoiceDetailsAPI invoiceObject = new XPXUpdateInvoiceDetailsAPI();

	public Document formInvoiceMessage(YFSEnvironment env, Document inXML)
			throws Exception {
		/**
		 * @author asekhar-tw on 04-Jan-2011 Added try-catch
		 */
		Document outputInvoiceDocument = null;
		try {
			api = YIFClientFactory.getInstance().getApi();
			String customerID = "";
			// first set the processed flag to Y
			Element inputElement = inXML.getDocumentElement();
			//inputElement.setAttribute("ProcessedFlag", "Y");
			String strInvoiceHeaderKey = inputElement.getAttribute("InvoiceHeaderKey");
			if(log.isDebugEnabled()){
				log.debug("Invoice HeaderKey :" + strInvoiceHeaderKey);
			}
			//Set in txn object to invoke changeXPXInvoiceHdrService after invoking the web service 
			//successfully in XPXB2BInvoiceWebServiceInvocationAPI
			env.setTxnObject("strInvoiceHeaderKey", strInvoiceHeaderKey);
			if(log.isDebugEnabled()){
				log.debug("changeXPXInvoiceHdrService_InputElement : " + SCXmlUtil.getString(inputElement));
			}
			//Document processedDoc = api.executeFlow(env,
				//	"changeXPXInvoiceHdrService", inXML);
			// get the list of invoice header details
			// form the template
			Document templateDoc = YFCDocument.createDocument("XPXInvoiceHdr")
					.getDocument();
			Element templateElement = templateDoc.getDocumentElement();
			Element invoiceLineListElement = templateDoc
					.createElement("XPXInvoiceLineList");
			templateElement.appendChild(invoiceLineListElement);
			Element invoiceLineElement = templateDoc
					.createElement("XPXInvoiceLine");
			invoiceLineListElement.appendChild(invoiceLineElement);
			env.setApiTemplate("getXPXGetInvoiceHdrListService", templateDoc);
			// Form the input xml
			Document inputHeaderDoc = YFCDocument.createDocument(
					"XPXInvoiceHdr").getDocument();
			Element inputHeaderElement = inputHeaderDoc.getDocumentElement();
			inputHeaderElement.setAttribute("InvoiceHeaderKey", inputElement
					.getAttribute("InvoiceHeaderKey"));
			if(log.isDebugEnabled())
			{
				log.debug("The input XML to getXPXInvoiceHdrListService in XPXSendInvoiceMessage is "+ SCXmlUtil.getString(inputHeaderDoc ));
			}
			Document outputInvoiceListDocument = api.executeFlow(env,
					"getXPXInvoiceHdrListService", inputHeaderDoc);
			NodeList invoiceList = outputInvoiceListDocument
					.getElementsByTagName("XPXInvoiceHdr");
			Element invoiceElement = (Element) invoiceList.item(0);
			outputInvoiceDocument = YFCDocument.createDocument().getDocument();
			outputInvoiceDocument.appendChild(outputInvoiceDocument.importNode(
					invoiceElement, true));
			outputInvoiceDocument.renameNode(outputInvoiceDocument
					.getDocumentElement(), outputInvoiceDocument
					.getNamespaceURI(), "XPXInvoiceHdr");
			customerID = getCustomerID(env, outputInvoiceDocument);
			invoiceObject.stampCustomerInfo(env, outputInvoiceDocument
					.getDocumentElement(), customerID);			
			invoiceObject.stampInvoiceLineLevelAttribute(env, outputInvoiceDocument.getDocumentElement());
		} catch (NullPointerException ne) {
			log.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.B2B_INV_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			/** Call the method to send the failure message * */
			prepareFailureMsg();
			throw ne;
		} catch (YFSException yfe) {
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.B2B_INV_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			/** Call the method to send the failure message * */
			prepareFailureMsg();
			throw yfe;
		} catch (Exception e) {
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.B2B_INV_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			/** Call the method to send the failure message * */
			prepareFailureMsg();
			throw e;
		}
		return outputInvoiceDocument;
	}

	/**
	 * @author asekhar-tw on 10-Jan-2011 This method prepares the error object
	 *         with the exception details which in turn will be used to log into
	 *         CENT
	 */
	private void prepareErrorObject(Exception e, String transType,
			String errorClass, YFSEnvironment env, Document inXML) {
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}

	/**
	 * @author asekhar-tw on 11-Jan-2011 This method sends a failure message to
	 *         indicate failure
	 */
	private Document prepareFailureMsg() {
		Document failureMsg = YFCDocument.createDocument("ErrorMessage")
				.getDocument();
		Element failureMsgElement = failureMsg.getDocumentElement();
		failureMsgElement.setAttribute("ErrorFlag", "Y");
		if(log.isDebugEnabled()){
			log.debug("Failure Message : " + SCXmlUtil.getString(failureMsg));
		}
		return failureMsg;
	}

	private String getCustomerID(YFSEnvironment env, Document invoiceDoc)
			throws YFSException, RemoteException {
		String customerID = "";
		Document inputOrderDoc = SCXmlUtil.createDocument("Order");
		inputOrderDoc.getDocumentElement().setAttribute("OrderHeaderKey",
				invoiceDoc.getDocumentElement().getAttribute("OrderHeaderKey"));
		if(log.isDebugEnabled()){
			log.debug("The input document for getOrderList in XPXSendInvoiceMessage is  "+SCXmlUtil.getString(inputOrderDoc));
		}
		Document orderListDoc = api.invoke(env, "getOrderList", inputOrderDoc);
		NodeList orderNodeList = orderListDoc.getElementsByTagName("Order");
		int orderLength = orderNodeList.getLength();
		if (orderLength != 0) {
			Element orderElement = (Element) orderNodeList.item(0);
			customerID = orderElement.getAttribute("BuyerOrganizationCode");
		}
		return customerID;
	}

}
