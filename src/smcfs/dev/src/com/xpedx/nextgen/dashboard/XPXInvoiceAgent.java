package com.xpedx.nextgen.dashboard;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXInvoiceAgent extends YCPBaseAgent {
	
	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory
	.getLogger("com.xpedx.nextgen.log");

	// private static Set<String> invoiceHeaderKeys = new HashSet<String>();
	
	public List getJobs(YFSEnvironment env, Document criteria,Document lastMessageCreated) throws Exception {
		// get YIFApi instance.
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		//form the document as input for invoice header
		Document inputInvoiceHeaderDoc = YFCDocument.createDocument("XPXInvoiceHdr").getDocument();
		Element inputInvoiceHeaderElement = inputInvoiceHeaderDoc.getDocumentElement();
		inputInvoiceHeaderElement.setAttribute("ProcessedFlag", "Y");
		inputInvoiceHeaderElement.setAttribute("ProcessedFlagQryType", "NE");
		Element orderByElement = inputInvoiceHeaderDoc.createElement("OrderBy");
		inputInvoiceHeaderElement.appendChild(orderByElement);
		Element attributeElement = inputInvoiceHeaderDoc.createElement("Attribute");
		attributeElement.setAttribute("Name", "InvoiceHeaderKey");
		attributeElement.setAttribute("Desc", "N");
		orderByElement.appendChild(attributeElement);
		String totalNumberOfRecords = criteria.getDocumentElement().getAttribute("NumRecordsToBuffer");
		inputInvoiceHeaderElement.setAttribute("MaximumRecords", totalNumberOfRecords);
		
		if(lastMessageCreated != null){			
			//get the orders which have failed
			Element complexQueryElement = inputInvoiceHeaderDoc.createElement("ComplexQuery");
			inputInvoiceHeaderElement.appendChild(complexQueryElement);
//			Element orElement = inputInvoiceHeaderDoc.createElement("Or");
//			complexQueryElement.appendChild(orElement);
//			for (String invoiceHeaderKey : invoiceHeaderKeys) {
//			Element expElement = inputInvoiceHeaderDoc.createElement("Exp");
//			expElement.setAttribute("Name", "InvoiceHeaderKey");
//			expElement.setAttribute("Value", invoiceHeaderKey);
//			orElement.appendChild(expElement);
//			}
			
			
			String orderHeaderKeyInLastCreatedMessage = lastMessageCreated.getDocumentElement().getAttribute("InvoiceHeaderKey");
			
			Element expNextElement = inputInvoiceHeaderDoc.createElement("Exp");
			expNextElement.setAttribute("Name", "InvoiceHeaderKey");
			expNextElement.setAttribute("Value", orderHeaderKeyInLastCreatedMessage);
			expNextElement.setAttribute("QryType", "GT");
			complexQueryElement.appendChild(expNextElement);
	
		}
		
				
		//get the invoice list
		Document outputInvoiceHeaderListDoc = api.executeFlow(env, "getXPXInvoiceHdrListService", inputInvoiceHeaderDoc);
//		NodeList invoiceHdrNodeList = outputInvoiceHeaderListDoc.getElementsByTagName("XPXInvoiceHdr");
//		int invoiceHdrLength = invoiceHdrNodeList.getLength();
		log.debug("The output from the invoice agent service getXPXInvoiceHdrListService is :" + SCXmlUtil.getString(outputInvoiceHeaderListDoc));
		List listOrders = SCXmlUtil.getChildrenList(outputInvoiceHeaderListDoc.getDocumentElement());
		List listOfJobs = new ArrayList();
		for(int counter = 0;counter<listOrders.size();counter++)
		{
			Element invoiceElement = (Element)listOrders.get(counter);
			Document newInputDoc1 = YFCDocument.createDocument().getDocument();
			newInputDoc1.appendChild(newInputDoc1.importNode(invoiceElement, true));
			newInputDoc1.renameNode(newInputDoc1.getDocumentElement(), newInputDoc1.getNamespaceURI(), "XPXInvoiceHdr");
			
			listOfJobs.add(newInputDoc1);
			
		}

	return listOfJobs;
	}
	
	
	@Override
	public void executeJob(YFSEnvironment env, Document inputDoc) throws Exception {
		// TODO Auto-generated method stub
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		String strTransType = "B2B-Inv";
		log.debug("The input to the XPXInvoiceProcessMessage service is : "+ inputDoc);
		try {
			api.executeFlow(env, "XPXInvoiceProcessMessage", inputDoc);
		/**old exception catch block replaced with CENT Tool
		 * } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			invoiceHeaderKeys.add(SCXmlUtil.getAttribute(inputDoc.getDocumentElement(), "InvoiceHeaderKey"));
			**/
		} catch (NullPointerException ne) {
			if(log.isDebugEnabled()){
				log.debug("Invoice Agent execute job strTransType : " + strTransType );
			}
			log.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, strTransType,
					XPXLiterals.NE_ERROR_CLASS, env, inputDoc);
			// invoiceHeaderKeys.add(SCXmlUtil.getAttribute(inputDoc.getDocumentElement(), "InvoiceHeaderKey"));
			/** Call the method to send the failure message * */
			
			throw ne;
		} catch (YFSException yfe) {
			log.error("YFSException: " + yfe.getStackTrace());
			if(log.isDebugEnabled()){
				log.debug("Invoice Agent execute job strTransType : " + strTransType );
			}
			prepareErrorObject(yfe, strTransType,
					XPXLiterals.NE_ERROR_CLASS, env, inputDoc);
			// invoiceHeaderKeys.add(SCXmlUtil.getAttribute(inputDoc.getDocumentElement(), "InvoiceHeaderKey"));
			/** Call the method to send the failure message * */
			
			throw yfe;
		} catch (Exception e) {
			if(log.isDebugEnabled()){
				log.debug("Invoice Agent execute job strTransType : " + strTransType );
			}
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, strTransType,
					XPXLiterals.NE_ERROR_CLASS, env, inputDoc);
			// invoiceHeaderKeys.add(SCXmlUtil.getAttribute(inputDoc.getDocumentElement(), "InvoiceHeaderKey"));
			/** Call the method to send the failure message * */
			
			throw e;
		}
		
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

	

}
