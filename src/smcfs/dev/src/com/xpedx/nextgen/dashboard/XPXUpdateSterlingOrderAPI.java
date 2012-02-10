package com.xpedx.nextgen.dashboard;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXUpdateSterlingOrderAPI implements YIFCustomApi {

	private static YIFApi api = null;

	private Properties arg0;

	private static YFCLogCategory log;

	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXCreateChainedOrderService.xml";

	String template = "global/template/api/getItemList.CreateCustomerOrderService.xml";

	String billToName = "";

	String sapParentName = "";

	String origEnvtId = "";
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		// log.getLogger(XPXUpdateSterlingOrderAPI.class);
		// log = YFCLogCategory.getLogger(XPXUpdateSterlingOrderAPI.class);

	}

	// static Logger log = Logger.getLogger(XPXUpdateSterlingOrderAPI.class);

	XPXUtils divisionObject = new XPXUtils();

	public void setProperties(Properties arg0) throws Exception {
		this.arg0 = arg0;
		// TODO Auto-generated method stub

	}

	// for the input order xml store webline no and OrderLineElement
	Hashtable<String, Element> htCurrentOrder = new Hashtable<String, Element>();

	Hashtable<String, Element> htCurrentCustomerOrder = new Hashtable<String, Element>();

	// to store orderHeaderKey and ChangeOrder xml
	Hashtable<String, Document> htChangeOrder = new Hashtable<String, Document>();

	Hashtable<String, Float> htPrice = new Hashtable<String, Float>();

	// store orderlines of the customer order
	Hashtable<String, Element> htCustomerOrder = new Hashtable<String, Element>();

	Hashtable<String, Document> htCustomerChangeOrderStatus = new Hashtable<String, Document>();

	Hashtable<String, Document> htCustomerChangeOrder = new Hashtable<String, Document>();

	// for status change

	HashSet<String> hsWebline = new HashSet<String>();

	// determine the sterling fulfillment
	ArrayList<String> fulfillmentOrderDetails = new ArrayList<String>();

	ArrayList<String> customerOrderDetails = new ArrayList<String>();

	public Document updateSterlingOrder(YFSEnvironment env, Document inXML)
			throws Exception

	{

		log.debug("The input xml to the LegacyOrderUpdate flow is: "
				+ SCXmlUtil.getString(inXML));
		log.beginTimer("XPXUpdateSterlingOrderAPI.updateSterlingOrder");

		StringBuffer output = null;
		String transactionStatusCode = "";
		api = YIFClientFactory.getInstance().getApi();
		Element inputElement = inXML.getDocumentElement();
		String processCode = inputElement.getAttribute("HeaderProcessCode");
		Document outputChangeOrderDoc = null;
		Document returnLegacyDoc = null;
		/* String sHdrStsCode = arg0.getProperty("HDR_PROCESS_CODE_SUCCESS"); */
		String sHeaderStatusCode = "";
		String enterpriseCode = "";

		try {

			Document newInputDoc1 = YFCDocument.createDocument().getDocument();
			newInputDoc1.appendChild(newInputDoc1
					.importNode(inputElement, true));
			newInputDoc1.renameNode(newInputDoc1.getDocumentElement(),
					newInputDoc1.getNamespaceURI(), "Order");
			ArrayList<String> billToCustomerArray = getBillToID(env,
					newInputDoc1);
			String billToID = billToCustomerArray.get(0);
			if (!billToCustomerArray.get(1).isEmpty()) {
				billToName = (String) billToCustomerArray.get(1);
			}
			newInputDoc1.getDocumentElement()
					.setAttribute("BillToID", billToID);
			ArrayList<String> shipToCustomerArray = getShipToID(env,
					newInputDoc1);
			String shipToID = shipToCustomerArray.get(0);
			if (!shipToCustomerArray.get(1).isEmpty()) {
				sapParentName = shipToCustomerArray.get(1);
			}
			if (!shipToCustomerArray.get(2).isEmpty()) {
				origEnvtId = shipToCustomerArray.get(2);
			}
			newInputDoc1.getDocumentElement().setAttribute(
					"BuyerOrganizationCode", shipToID);
			newInputDoc1.getDocumentElement()
					.setAttribute("ShipToID", shipToID);
			Document inputOrderStatusChangeDoc = null;
			Document changeFulfillmentOrderDoc = null;
			Document changeOrderStatusDoc = null;
			boolean bStatusUpdateFlag = false;

			String webConfirmationNumber = SCXmlUtil.getXpathAttribute(
					inputElement, "./Extn/@ExtnWebConfNum");
			log.debug("webConfirmationNumber: " + webConfirmationNumber);

			if (processCode.equals("C")) {
				log.debug("processCode.equals('C')");
				fulfillmentOrderDetails = getSterlingFulfillmentOrderNo(env,
						inputElement);
				log.debug("Fulfillment orderNo="
						+ fulfillmentOrderDetails.get(0));
				log.debug("Fulfillment orderHeaderKey="
						+ fulfillmentOrderDetails.get(1));

				customerOrderDetails = getCustomerOrderNo(env, inputElement);
				String customerOrderNo = customerOrderDetails.get(1);
				String customerOrderHeaderKey = customerOrderDetails.get(0);
				log.debug("customerOrderNo: " + customerOrderNo);
				log.debug("customerOrderNo: " + customerOrderHeaderKey);

				// form input xml
				// get the webline numbers from input element
				ArrayList<String> weblineArray = new ArrayList<String>();
				weblineArray = populateWeblineArray(inputElement, weblineArray);
				// get the fulfillment orderlist
				Document formedInputXml = formNewInputXML(env, newInputDoc1,
						weblineArray);
				log.debug("Formed Input xml:"
						+ SCXmlUtil.getString(formedInputXml));

				htCurrentOrder = populateHTCurrentOrder(env,
						fulfillmentOrderDetails.get(0), htCurrentOrder);
				log.debug("htCurrentOrder size: " + htCurrentOrder.size());

				htCurrentCustomerOrder = populateHTCurrentCustomerOrder(env,
						customerOrderDetails.get(1), htCurrentCustomerOrder);
				log.debug("htCurrentCustomerOrder size: "
						+ htCurrentCustomerOrder.size());

				populateHTCustomerOrder(env, customerOrderNo);

				// loop thru the orderlines in the input xml

				NodeList inputOrderLinesList = formedInputXml
						.getElementsByTagName("OrderLines");
				int inputOrderLinesListLength = inputOrderLinesList.getLength();
				if (inputOrderLinesListLength != 0) {
					Element inputOrderLinesElement = (Element) inputOrderLinesList
							.item(0);

					NodeList inputOrderLineList = inputOrderLinesElement
							.getElementsByTagName("OrderLine");
					int inputOrderLineLength = inputOrderLineList.getLength();
					if (inputOrderLineLength != 0) {
						for (int inputOrderLineCounter = 0; inputOrderLineCounter < inputOrderLineLength; inputOrderLineCounter++) {
							log.debug("formedInputXml Iteration: "
									+ inputOrderLineCounter);
							Element inputOrderLineELement = (Element) inputOrderLineList
									.item(inputOrderLineCounter);
							// get the line on the customer order using
							// ExtnWebLineNo

							// newly added
							String webLineNumber = getWebLineKey(env,
									inputOrderLineELement);
							String webLine = SCXmlUtil.getXpathAttribute(
									inputOrderLineELement,
									"./Extn/@ExtnWebLineNumber");
							String lineProcessCode = inputOrderLineELement
									.getAttribute("LineProcessCode");
							log.debug("LineProcessCode: " + lineProcessCode);
							Element orderLineElement1 = htCurrentOrder
									.get(webLineNumber);
							if (orderLineElement1 != null) {
								log.debug("orderLineElement"
										+ SCXmlUtil
												.getString(orderLineElement1));
								log.debug("orderLineElement"
										+ SCXmlUtil
												.getString(orderLineElement1));
								log
										.debug(orderLineElement1
												.getAttribute("ChainedFromOrderLineKey"));
								log
										.debug(orderLineElement1
												.getAttribute("ChainedFromOrderHeaderKey"));
								inputOrderLineELement
										.setAttribute(
												"ChainedFromOrderLineKey",
												orderLineElement1
														.getAttribute("ChainedFromOrderLineKey"));
								inputOrderLineELement
										.setAttribute(
												"ChainedFromOrderHeaderKey",
												orderLineElement1
														.getAttribute("ChainedFromOrderHeaderKey"));
								inputOrderLineELement.setAttribute(
										"PrimeLineNo", orderLineElement1
												.getAttribute("PrimeLineNo"));
								inputOrderLineELement.setAttribute("SubLineNo",
										orderLineElement1
												.getAttribute("SubLineNo"));
							}
							if (lineProcessCode.equals("A")) {

								log
										.debug("inputOrderLineELement"
												+ SCXmlUtil
														.getString(inputOrderLineELement));
								// create customer change order xml
								// find the line type of the orderline and check
								// if the chained order already exists.
								htChangeOrder = formInputxmlForCreatingChainedOrderForLineA(
										env, inputElement,
										customerOrderHeaderKey,
										inputOrderLineELement, webLineNumber,
										webLine);
							}

							if (lineProcessCode.equals("C")) {

								log.debug("lineProcessCode.equals('C')");

								/** ************************************ */
								// check if the c is a
								// get the line processCode
								String statusCode = "";
								// check if the line exists
								log.debug("webLineNumber: " + webLineNumber);
								if (weblineArray.contains(webLine)) {
									log.debug("weblineArray.contains(webLine)");
									// check if the line is cancelled
									// get the status
									log.debug("Fulfillment order no: "
											+ fulfillmentOrderDetails.get(0));
									Document statusDocument = getSterlingOrderList(
											env, fulfillmentOrderDetails.get(0));
									NodeList orderList = statusDocument
											.getElementsByTagName("Order");
									Element orderStatusElement = (Element) orderList
											.item(0);
									NodeList orderStatusLinesList = orderStatusElement
											.getElementsByTagName("OrderLines");
									Element orderStatusLineElement = (Element) orderStatusLinesList
											.item(0);
									NodeList orderStatusLineList = orderStatusLineElement
											.getElementsByTagName("OrderLine");
									for (int i = 0; i < orderStatusLineList
											.getLength(); i++) {
										Element lineElement = (Element) orderStatusLineList
												.item(i);
										String web = SCXmlUtil
												.getXpathAttribute(lineElement,
														"./Extn/@ExtnWebLineNumber");
										log.debug("ExtnWebLineNumber(web): "
												+ web);
										log
												.debug("ExtnWebLineNumber(webLine): "
														+ webLine);
										if (webLine.equals(web)) {
											statusCode = SCXmlUtil
													.getXpathAttribute(
															lineElement,
															"./OrderStatuses/OrderStatus/@Status");
										}
									}

									log.debug("statusCode" + statusCode);
									if (statusCode.equals("9000")) {
										log.debug("statusCode.equals('9000')");
										htChangeOrder = formInputxmlForCreatingChainedOrderForLineA(
												env, inputElement,
												customerOrderHeaderKey,
												inputOrderLineELement,
												webLineNumber, webLine);
									}

									else {
										log.debug("This is a quantity change");
										/** ************************************* */

										// for qty change
										changeFulfillmentOrderDoc = invokeMethodForOrlineProcessCodeC(
												env, inputOrderLineELement,
												webLineNumber, inputElement);

										changeFulfillmentOrderDoc = htChangeOrder
												.get(fulfillmentOrderDetails
														.get(1));
										log
												.debug("changeFulfillmentOrderDoc: "
														+ SCXmlUtil
																.getString(changeFulfillmentOrderDoc));

									}
								}

							}

							if (lineProcessCode.equals("R")) {
								// deletion
								changeFulfillmentOrderDoc = htChangeOrder
										.get(fulfillmentOrderDetails.get(1));
								if (changeFulfillmentOrderDoc != null) {
									// append
									htChangeOrder = updateChangeOrderXMLInHTChangeOrderForR(
											inputOrderLineELement,
											changeFulfillmentOrderDoc,
											webLineNumber,
											inputOrderLineELement);
								} else {
									// create
									htChangeOrder = createChangeOrderXMLForCWhenHTChangeOrderisEmptyForR(
											env, webLineNumber,
											inputOrderLineELement);
								}

							}

							if (lineProcessCode.equals("D")) {
								// check for the line status code
								String lineStatusCode = SCXmlUtil
										.getXpathAttribute(inputElement,
												"Extn/@ExtnLineStatusCode");
								log.debug("lineStatusCode" + lineStatusCode);
								if (!lineStatusCode.equals("A0000")) {
									changeFulfillmentOrderDoc = htChangeOrder
											.get(fulfillmentOrderDetails.get(0));

									Document customerChangeOrderDoc1 = htCustomerChangeOrder
											.get(customerOrderDetails.get(1));
									customerChangeOrderDoc1 = createCustomerChangeOrderDocForLineProcessCodeD(
											env, inputOrderLineELement,
											customerChangeOrderDoc1);
									htCustomerChangeOrder.put(
											customerOrderDetails.get(1),
											customerChangeOrderDoc1);
								}
							}

						}
						log.debug("All the orderlines are iterated....");
					}
				}

				changeFulfillmentOrderDoc = htChangeOrder
						.get(fulfillmentOrderDetails.get(1));

				// change the fulfillment order
				if (changeFulfillmentOrderDoc != null) {

					/***********************************************************
					 * Added by Arun Sekhar on 16th Feb 2011 as changeOrder was
					 * failing due to the error-="YFS: Order cannot be modified
					 * in current status"
					 **********************************************************/
					changeFulfillmentOrderDoc.getDocumentElement()
							.setAttribute("Override", "Y");
					setOrderHeaderLevelAttributes(inputElement,
							changeFulfillmentOrderDoc.getDocumentElement());

					// get the extn element from input
					Element extnElement = null;
					NodeList extnNodeList = inputElement
							.getElementsByTagName("Extn");
					int extnLength = extnNodeList.getLength();
					if (extnLength != 0) {
						extnElement = (Element) extnNodeList.item(0);

					}

					Element extnFulElement = null;
					NodeList extnFulNodeList = changeFulfillmentOrderDoc
							.getElementsByTagName("Extn");
					int extnFulLength = extnFulNodeList.getLength();
					if (extnFulLength != 0) {
						extnFulElement = (Element) extnFulNodeList.item(0);
						changeFulfillmentOrderDoc.getDocumentElement()
								.removeChild(extnFulElement);
					}

					Node inputOrderLineNode = changeFulfillmentOrderDoc
							.importNode(extnElement, true);
					Element inputOrderLineNodeElement = (Element) inputOrderLineNode;

					changeFulfillmentOrderDoc.getDocumentElement().appendChild(
							inputOrderLineNodeElement);

					log.debug("changeOrder i/p for FulfillmentOrder: "
							+ SCXmlUtil.getString(changeFulfillmentOrderDoc));
					outputChangeOrderDoc = api.invoke(env, "changeOrder",
							changeFulfillmentOrderDoc);
					log.debug("changeOrder o/p for FulfillmentOrder: "
							+ SCXmlUtil.getString(outputChangeOrderDoc));
				}

				// for status change
				bStatusUpdateFlag = invokeOnStatusChange(env, inputElement,
						fulfillmentOrderDetails, bStatusUpdateFlag);
				Document changeCustomerDoc = htCustomerChangeOrder
						.get(customerOrderDetails.get(1));
				if (changeCustomerDoc != null) {

					formUpdateCustomerDocForPriceChange(env,
							customerOrderHeaderKey, changeCustomerDoc);
				} else {
					formCustomerDocForPriceChange(env, customerOrderNo,
							customerOrderHeaderKey);
				}

				/***************************************************************
				 * In case of an order status change, calling changeOrderStatus
				 * API Need to pextract it to a method separately
				 **************************************************************/

				changeOrderStatusToBaseDrop(env, inputElement);
			}

			// if(processCode.equals("S"))
			log.debug("bStatusUpdateFlag: " + bStatusUpdateFlag);
			if ((bStatusUpdateFlag) || (processCode.equals("S"))) {
				inputElement.setAttribute("BillToID", billToID);
				inputElement.setAttribute("BuyerOrganizationCode", shipToID);
				inputElement.setAttribute("ShipToID", shipToID);
				fulfillmentOrderDetails = getSterlingFulfillmentOrderNo(env,
						inputElement);

				invokeOnProcessCodeS(env, inputElement, fulfillmentOrderDetails);
			}

			if (processCode.equals("A")) {
				// fulfillmentOrderDetails = getSterlingFulfillmentOrderNo(env,
				// inputElement);
				// customerOrderDetails = getCustomerOrderNo(env, inputElement);
				inXML.getDocumentElement().setAttribute("BillToID", billToID);
				inXML.getDocumentElement().setAttribute(
						"BuyerOrganizationCode", shipToID);
				inXML.getDocumentElement().setAttribute("ShipToID", shipToID);
				outputChangeOrderDoc = invokeOnHeaderProcessCodeA(env, inXML,
						webConfirmationNumber);

			}

			if (processCode.equals("D")) {
				// check the line status code
				String headerStatusCode = SCXmlUtil.getXpathAttribute(
						inputElement, "./Extn/@ExtnHeaderStatusCode");
				log.debug("headerStatusCode" + headerStatusCode);
				if (!headerStatusCode.equals("A0000")) {
					fulfillmentOrderDetails = getSterlingFulfillmentOrderNo(
							env, inputElement);
					customerOrderDetails = getCustomerOrderNo(env, inputElement);
					inputElement.setAttribute("BillToID", billToID);
					inputElement
							.setAttribute("BuyerOrganizationCode", shipToID);
					inputElement.setAttribute("ShipToID", shipToID);
					outputChangeOrderDoc = invokeForOrderProcessCodeD(env,
							webConfirmationNumber, inputElement);
				}

			}

			/** ************************ */

			if (processCode.equals("I")) {
				inputElement.setAttribute("BillToID", billToID);
				inputElement.setAttribute("BuyerOrganizationCode", shipToID);
				inputElement.setAttribute("ShipToID", shipToID);
				fulfillmentOrderDetails = getSterlingFulfillmentOrderNo(env,
						inputElement);

				invokeOnProcessCodeS(env, inputElement, fulfillmentOrderDetails);

				// change order for stamping the invoice related attributes
				inputElement.setAttribute("DocumentType", "0001");
				// inputElement.setAttribute("EnterpriseCode", "xpedx");
				// new change for enterprise code
				inputElement.setAttribute("EnterpriseCode",
						fulfillmentOrderDetails.get(2));
				inputElement.setAttribute("Action", "Modify");
				inputElement.setAttribute("OrderNo", fulfillmentOrderDetails
						.get(0));
				// inputElement.setAttribute("Override", "Y");
				// change order api
				outputChangeOrderDoc = api.invoke(env, "changeOrder", inXML);
				log.debug("" + SCXmlUtil.getString(inXML));

			}

			/** *********************** */

			/* } */
		}
		/**
		 * New catch block added by Arun Sekhar on 24-Jan-2011 for CENT tool
		 * logging *
		 */
		/*
		 * catch (Exception e) { log.debug("Exception caught"); StringWriter in =
		 * new StringWriter ( ) ; PrintWriter ps = new PrintWriter ( in ) ; //
		 * Write the stack trace to the string e.printStackTrace ( ps ) ; // get
		 * the string output = in.getBuffer ( ) ; // show the string log.debug (
		 * output ) ; }
		 */
		catch (NullPointerException ne) {
			log.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.OU_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			throw ne;
		} catch (YFSException yfe) {
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.OU_TRANS_TYPE,
					XPXLiterals.YFE_ERROR_CLASS, env, inXML);
		} catch (Exception e) {
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.OU_TRANS_TYPE,
					XPXLiterals.E_ERROR_CLASS, env, inXML);
		} finally {
			/* if(sHeaderStatusCode.equals(sHdrStsCode)){ */
			if (!processCode.equals("A")) {
				returnLegacyDoc = outputChangeOrderDoc;
				if (returnLegacyDoc == null) {
					// form input
					Document inputdoc = YFCDocument.createDocument("Order")
							.getDocument();
					inputdoc.getDocumentElement().setAttribute("OrderNo",
							fulfillmentOrderDetails.get(0));
					inputdoc.getDocumentElement().setAttribute("DocumentType",
							"0001");
					// inputdoc.getDocumentElement().setAttribute("EnterpriseCode",
					// "xpedx");
					// new change for enterprise code
					inputdoc.getDocumentElement().setAttribute(
							"EnterpriseCode", fulfillmentOrderDetails.get(2));
					// returnLegacyDoc = api.invoke(env, "getOrderDetails",
					// inputdoc);
					returnLegacyDoc = getSterlingOrderDetails(env, inputdoc);
				}
				log.debug("returnLegacyDoc"
						+ SCXmlUtil.getString(returnLegacyDoc));
				if (output == null) {
					// get the details
					log.debug("outputChangeOrderDoc"
							+ SCXmlUtil.getString(outputChangeOrderDoc));
					if (outputChangeOrderDoc == null) {
						// form input
						Document inputdoc = YFCDocument.createDocument("Order")
								.getDocument();
						inputdoc.getDocumentElement().setAttribute("OrderNo",
								fulfillmentOrderDetails.get(0));
						inputdoc.getDocumentElement().setAttribute(
								"DocumentType", "0001");
						// inputdoc.getDocumentElement().setAttribute("EnterpriseCode",
						// "xpedx");
						// new change for enterprise Code
						inputdoc.getDocumentElement().setAttribute(
								"EnterpriseCode",
								fulfillmentOrderDetails.get(2));
						// returnLegacyDoc = api.invoke(env, "getOrderDetails",
						// inputdoc);
						returnLegacyDoc = getSterlingOrderDetails(env, inputdoc);
					} else {
						returnLegacyDoc = getSterlingOrderDetails(env,
								outputChangeOrderDoc);
					}

					transactionStatusCode = "SUCCESS";
					returnLegacyDoc.getDocumentElement().setAttribute(
							"TransactionMessage", "");
					returnLegacyDoc.getDocumentElement().setAttribute(
							"TransactionStatus", transactionStatusCode);
				} else {
					if (outputChangeOrderDoc == null) {
						// form input
						Document inputdoc = YFCDocument.createDocument("Order")
								.getDocument();
						inputdoc.getDocumentElement().setAttribute("OrderNo",
								fulfillmentOrderDetails.get(0));
						inputdoc.getDocumentElement().setAttribute(
								"DocumentType", "0001");
						// inputdoc.getDocumentElement().setAttribute("EnterpriseCode",
						// "xpedx");
						// new change enterprise code
						inputdoc.getDocumentElement().setAttribute(
								"EnterpriseCode",
								fulfillmentOrderDetails.get(2));
						// returnLegacyDoc = api.invoke(env, "getOrderDetails",
						// inputdoc);
						returnLegacyDoc = getSterlingOrderDetails(env, inputdoc);
					} else {
						returnLegacyDoc = getSterlingOrderDetails(env,
								outputChangeOrderDoc);
					}
					// get the details
					// returnLegacyDoc = getSterlingOrderDetails(env,
					// outputChangeOrderDoc);
					transactionStatusCode = "ERROR";
					returnLegacyDoc.getDocumentElement().setAttribute(
							"TransactionMessage", output.toString());
					returnLegacyDoc.getDocumentElement().setAttribute(
							"TransactionStatus", transactionStatusCode);
				}
			}
			// for A
			else {
				returnLegacyDoc = inXML;
				if (output == null) {
					transactionStatusCode = "SUCCESS";
					returnLegacyDoc.getDocumentElement().setAttribute(
							"TransactionMessage", "");
					returnLegacyDoc.getDocumentElement().setAttribute(
							"TransactionStatus", transactionStatusCode);
				} else {
					transactionStatusCode = "ERROR";
					returnLegacyDoc.getDocumentElement().setAttribute(
							"TransactionMessage", output.toString());
					returnLegacyDoc.getDocumentElement().setAttribute(
							"TransactionStatus", transactionStatusCode);
				}
			}

			/* } */
		}
		return returnLegacyDoc;
	}

	/**
	 * @author asekhar-tw on 11-Feb-2011 as a fix for the bug 1015 This method
	 *         will push the order to the base drop status in case of a
	 *         HeaderProcessCode "C" prepares the error object with the
	 *         exception details which in turn will be used to log into CENT
	 */
	private void changeOrderStatusToBaseDrop(YFSEnvironment env,
			Element inputElement) throws RemoteException {

		log.debug("---> changeOrderStatusToBaseDrop()");
		log.debug("inputElement: " + SCXmlUtil.getString(inputElement));
		Document inputChangeStatusDoc = null;

		/*
		 * String newStatus = SCXmlUtil.getXpathAttribute(inputElement,
		 * "./Extn/@ExtnHeaderStatusCode");
		 */
		String newStatus = inputElement.getAttribute("OrderStatus");
		log.debug("newStatus: " + newStatus);

		String transactionId = arg0.getProperty(newStatus);
		log.debug("transactionId: " + transactionId);

		// form the change status doc
		inputChangeStatusDoc = YFCDocument.createDocument("OrderStatusChange")
				.getDocument();
		Element inputChangeStatusElement = inputChangeStatusDoc
				.getDocumentElement();
		inputChangeStatusElement.setAttribute("DocumentType", "0001");

		// new enterprise code change
		inputChangeStatusElement.setAttribute("EnterpriseCode",
				fulfillmentOrderDetails.get(2));
		inputChangeStatusElement.setAttribute("IgnoreTransactionDependencies",
				"Y");
		inputChangeStatusElement.setAttribute("ChangeForAllAvailableQty", "Y");
		inputChangeStatusElement.setAttribute("ModificationReasonCode",
				"CHANGE_FULFILLMENT_TYPE");
		inputChangeStatusElement.setAttribute("ModificationReasonText",
				"Changed Fulfillment Type");
		inputChangeStatusElement.setAttribute("OrderNo",
				fulfillmentOrderDetails.get(0));
		inputChangeStatusElement.setAttribute("TransactionId", transactionId);
		inputChangeStatusElement.setAttribute("BaseDropStatus", newStatus);

		log.debug("changeOrderStatus API input doc: "
				+ SCXmlUtil.getString(inputChangeStatusDoc));
		Document output = api.invoke(env, "changeOrderStatus",
				inputChangeStatusDoc);
		log.debug("changeOrderStatus API output doc: "
				+ SCXmlUtil.getString(output));
		log.debug("<--- changeOrderStatusToBaseDrop()");
	}

	/**
	 * @author asekhar-tw on 24-Jan-2011 This method prepares the error object
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

	private ArrayList<String> getShipToID(YFSEnvironment env, Document inputXml)
			throws YFSException, RemoteException {
		log.debug("---> getShipToID()");
		log.debug("Input XML: " + SCXmlUtil.getString(inputXml));
		ArrayList<String> shipToCustomerArray = new ArrayList();
		Element inputElement = inputXml.getDocumentElement();
		// form the input
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("OrganizationCode", inputElement
				.getAttribute("EnterpriseCode"));
		Element extnElement = inputCustomerDoc.createElement("Extn");
		extnElement.setAttribute("ExtnLegacyCustNumber", SCXmlUtil
				.getXpathAttribute(inputElement, "./Extn/@ExtnCustomerNo"));
		extnElement.setAttribute("ExtnShipToSuffix", SCXmlUtil
				.getXpathAttribute(inputElement, "./Extn/@ExtnShipToSuffix"));
		extnElement.setAttribute("ExtnSuffixType", "S");
		inputCustomerElement.appendChild(extnElement);
		env.setApiTemplate("getCustomerList", getCustomerListTemplate);
		log.debug("The Shipto query input is: "
				+ SCXmlUtil.getString(inputCustomerDoc));
		Document outputCustomerListDoc = api.invoke(env, "getCustomerList",
				inputCustomerDoc);
		log.debug("The ship to query output is: "
				+ SCXmlUtil.getString(outputCustomerListDoc));
		env.clearApiTemplate("getCustomerList");
		NodeList customerNodeList = outputCustomerListDoc
				.getElementsByTagName("Customer");
		int customerLength = customerNodeList.getLength();
		if (customerLength != 0) {
			Element customerElement = (Element) customerNodeList.item(0);
			shipToCustomerArray.add(customerElement.getAttribute("CustomerID"));
			Element customerExtnElement = (Element) customerElement
					.getElementsByTagName("Extn").item(0);
			shipToCustomerArray.add(customerExtnElement
					.getAttribute("ExtnSAPParentName"));
			shipToCustomerArray.add(customerExtnElement
					.getAttribute("ExtnOrigEnvironmentCode"));
		}
		log.debug("<--- getShipToID()");
		return shipToCustomerArray;
	}

	private ArrayList<String> getBillToID(YFSEnvironment env, Document inputXml)
			throws YFSException, RemoteException {
		log.debug("Inside getBillToId");
		ArrayList billToCustomerArray = new ArrayList();
		Element inputElement = inputXml.getDocumentElement();
		// form the input
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("OrganizationCode", inputElement
				.getAttribute("EnterpriseCode"));
		Element extnElement = inputCustomerDoc.createElement("Extn");
		extnElement.setAttribute("ExtnLegacyCustNumber", SCXmlUtil
				.getXpathAttribute(inputElement, "./Extn/@ExtnCustomerNo"));
		extnElement.setAttribute("ExtnBillToSuffix", SCXmlUtil
				.getXpathAttribute(inputElement, "./Extn/@ExtnBillToSuffix"));
		extnElement.setAttribute("ExtnSuffixType", "B");
		inputCustomerElement.appendChild(extnElement);
		env.setApiTemplate("getCustomerList", getCustomerListTemplate);
		log.debug("The input to getBillTocustomer is : "
				+ SCXmlUtil.getString(inputCustomerDoc));
		Document outputCustomerListDoc = api.invoke(env, "getCustomerList",
				inputCustomerDoc);
		log.debug("The bill to  customer output is: "
				+ SCXmlUtil.getString(outputCustomerListDoc));
		env.clearApiTemplate("getCustomerList");
		NodeList customerNodeList = outputCustomerListDoc
				.getElementsByTagName("Customer");
		int customerLength = customerNodeList.getLength();
		if (customerLength != 0) {
			log.debug("Inside customerLength");
			Element customerElement = (Element) customerNodeList.item(0);
			billToCustomerArray.add(customerElement.getAttribute("CustomerID"));
			Element buyerOrgElement = (Element) customerElement
					.getElementsByTagName("BuyerOrganization").item(0);
			billToCustomerArray.add(buyerOrgElement
					.getAttribute("OrganizationName"));
		}
		log.debug("<--- getBillToID()");
		return billToCustomerArray;
	}

	private Document createCustomerChangeOrderDocForLineProcessCodeD(
			YFSEnvironment env, Element inputOrderLineELement,
			Document customerChangeOrderDoc1) throws RemoteException {
		if (customerChangeOrderDoc1 != null) {
			// update
			// String weblineNo =
			// SCXmlUtil.getXpathAttribute(inputOrderLineELement,
			// "./Extn/@ExtnWebLineNumber");
			NodeList linesList = customerChangeOrderDoc1
					.getElementsByTagName("OrderLines");
			Element orderLinesElement = (Element) linesList.item(0);
			NodeList lineList = orderLinesElement
					.getElementsByTagName("OrderLine");
			for (int linecounter = 0; linecounter < lineList.getLength(); linecounter++) {

				Element orderLineElement = (Element) lineList.item(linecounter);
				String inputWebLineNo = SCXmlUtil.getXpathAttribute(
						inputOrderLineELement, "./Extn/@ExtnWebLineNumber");
				log.debug("inputWebLineNo" + inputWebLineNo);
				String existingWebLineNo = SCXmlUtil.getXpathAttribute(
						orderLineElement, "./Extn/@ExtnWebLineNumber");
				log.debug("existingWebLineNo" + existingWebLineNo);
				if (inputWebLineNo.equals(existingWebLineNo)) {
					Float eQty = Float.parseFloat(orderLineElement
							.getAttribute("OrderedQty"));
					Float iQty = Float.parseFloat(inputOrderLineELement
							.getAttribute("OrderedQty"));
					Float uQty = eQty - iQty;
					orderLineElement.setAttribute("Action", "Modify");
					orderLineElement
							.setAttribute("OrderedQty", uQty.toString());
				}
			}

			htCustomerChangeOrder.put(customerOrderDetails.get(1),
					customerChangeOrderDoc1);

		} else {
			// create
			// get the list of customer order
			Document outputCustomerOrderList = getSterlingOrderList(env,
					customerOrderDetails.get(1));
			NodeList outputList = outputCustomerOrderList
					.getElementsByTagName("Order");
			Element orderElement = (Element) outputList.item(0);
			orderElement.setAttribute("Action", "Modify");
			customerChangeOrderDoc1 = YFCDocument.createDocument()
					.getDocument();
			customerChangeOrderDoc1.appendChild(customerChangeOrderDoc1
					.importNode(orderElement, true));
			customerChangeOrderDoc1.renameNode(customerChangeOrderDoc1
					.getDocumentElement(), customerChangeOrderDoc1
					.getNamespaceURI(), "Order");
			// String weblineNo =
			// SCXmlUtil.getXpathAttribute(inputOrderLineELement,
			// "./Extn/@ExtnWebLineNumber");
			NodeList linesList = customerChangeOrderDoc1
					.getElementsByTagName("OrderLines");
			Element orderLinesElement = (Element) linesList.item(0);
			NodeList lineList = orderLinesElement
					.getElementsByTagName("OrderLine");
			for (int linecounter = 0; linecounter < lineList.getLength(); linecounter++) {
				Element orderLineElement = (Element) lineList.item(linecounter);
				String inputWebLineNo = SCXmlUtil.getXpathAttribute(
						inputOrderLineELement, "./Extn/@ExtnWebLineNumber");
				log.debug("inputWebLineNo" + inputWebLineNo);
				String existingWebLineNo = SCXmlUtil.getXpathAttribute(
						orderLineElement, "./Extn/@ExtnWebLineNumber");
				log.debug("existingWebLineNo" + existingWebLineNo);
				if (inputWebLineNo.equals(existingWebLineNo)) {
					Float eQty = Float.parseFloat(orderLineElement
							.getAttribute("OrderedQty"));
					Float iQty = Float.parseFloat(inputOrderLineELement
							.getAttribute("OrderedQty"));
					Float uQty = eQty - iQty;
					orderLineElement.setAttribute("Action", "Modify");
					orderLineElement
							.setAttribute("OrderedQty", uQty.toString());
				}
			}
			htCustomerChangeOrder.put(customerOrderDetails.get(1),
					customerChangeOrderDoc1);
		}
		return customerChangeOrderDoc1;
	}

	private Document invokeOnHeaderProcessCodeA(YFSEnvironment env,
			Document inXML, String webConfirmationNumber)
			throws RemoteException, Exception {
		Document outputChangeOrderDoc;
		Element inputElement = inXML.getDocumentElement();
		String webConfirmationNumberA = SCXmlUtil.getXpathAttribute(
				inputElement, "./Extn/@ExtnWebConfNum");
		String legacyOrderNo = SCXmlUtil.getXpathAttribute(inputElement,
				"./Extn/@ExtnLegacyOrderNo");
		// for doc to check if the customer order exists
		Document customerDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element customerElement = customerDoc.getDocumentElement();
		Element extnElement = customerDoc.createElement("Extn");
		extnElement.setAttribute("ExtnWebConfNum", webConfirmationNumberA);
		customerElement.appendChild(extnElement);
		// get the list
		Document outCustomerDoc = api.invoke(env, "getOrderList", customerDoc);
		String customerTotalRecords = outCustomerDoc.getDocumentElement()
				.getAttribute("TotalOrderList");
		if (customerTotalRecords.equals("0")) {

			// if(customer order and legacy order dont exist)
			Document outputOrderDocument = invokeCreateCustomerOrderForProcessCodeA(
					env, inXML, inputElement, webConfirmationNumber);
			log.debug("outputOrderDocument"
					+ SCXmlUtil.getString(outputOrderDocument));
			// create chained order correspondingly
			outputChangeOrderDoc = invokeCreateChainedOrderForProcessCodeA(env,
					outputOrderDocument, legacyOrderNo);
		} else {
			// if customer order exists and fulfillment order does not exist
			// get the customer order doc
			// check if backordered quantity exists
			NodeList linesList = inputElement
					.getElementsByTagName("OrderLines");
			Element linesElement = (Element) linesList.item(0);
			NodeList lineList = linesElement.getElementsByTagName("OrderLine");
			Element lineElement = (Element) lineList.item(0);
			NodeList lineInfoList = lineElement.getElementsByTagName("Extn");
			Element lineInfoElement = (Element) lineInfoList.item(0);
			boolean bckExists = false;
			if (lineInfoElement.hasAttribute("ExtnReqBackOrdQty")
					&& lineInfoElement.getAttribute("ExtnReqBackOrdQty") != "") {
				if (!(Float.parseFloat(lineInfoElement
						.getAttribute("ExtnReqBackOrdQty")) == 0)) {
					bckExists = true;
				}
			}

			Document inputCustomerdoc = YFCDocument.createDocument("Order")
					.getDocument();
			Element inputCustomerElement = inputCustomerdoc
					.getDocumentElement();
			// inputCustomerElement.setAttribute("OrderType", "Customer");
			Element extnElement1 = inputCustomerdoc.createElement("Extn");
			extnElement1.setAttribute("ExtnWebConfNum", webConfirmationNumberA);
			inputCustomerElement.appendChild(extnElement1);
			// get the order list
			log.debug("inputCustomerdoc"
					+ SCXmlUtil.getString(inputCustomerdoc));
			Document templateDoc = setTemplate(env);
			env.setApiTemplate("getOrderList", templateDoc);
			Document outListDoc = api.invoke(env, "getOrderList",
					inputCustomerdoc);
			env.clearApiTemplate("getOrderList");
			log.debug("outListDoc" + SCXmlUtil.getString(outListDoc));
			NodeList orderList = outListDoc.getElementsByTagName("Order");
			Element orderElement = (Element) orderList.item(0);
			log.debug("" + SCXmlUtil.getString(orderElement));
			Document newInputDoc = YFCDocument.createDocument().getDocument();
			newInputDoc.appendChild(newInputDoc.importNode(orderElement, true));
			newInputDoc.renameNode(newInputDoc.getDocumentElement(),
					newInputDoc.getNamespaceURI(), "Order");
			// create chained order correspondingly
			outputChangeOrderDoc = invokeCreateChainedOrderForProcessCodeAForExistingCustomerOrder(
					env, newInputDoc, inputElement, bckExists);
			log.debug("outputChangeOrderDoc"
					+ SCXmlUtil.getString(outputChangeOrderDoc));
		}
		return outputChangeOrderDoc;
	}

	private void formUpdateCustomerDocForPriceChange(YFSEnvironment env,
			String customerOrderHeaderKey, Document changeCustomerDoc)
			throws Exception, RemoteException {
		// check if the htprice is populated
		if (htPrice.size() > 0) {
			Enumeration<String> Enumeration = htPrice.keys();
			while (Enumeration.hasMoreElements()) {
				String key = (String) Enumeration.nextElement();
				Float value = htPrice.get(key);
				NodeList linesList = changeCustomerDoc
						.getElementsByTagName("OrderLines");
				for (int i = 0; i < linesList.getLength(); i++) {
					Element lineElement = (Element) linesList.item(i);
					String weblineNumber = SCXmlUtil.getXpathAttribute(
							lineElement, "./Extn/@ExtnWebLineNumber");
					if (weblineNumber.equals(key)) {
						ArrayList<String> test = getPrimeLineNumberFromCustomerOrder(
								env, customerOrderHeaderKey, key);
						Float lineTotal = Float.parseFloat(test.get(3)) + value;
						Float unitPrice = lineTotal
								/ (Integer.parseInt(test.get(4)));
						Element lineInfoElement = changeCustomerDoc
								.createElement("LinePriceInfo");
						lineInfoElement.setAttribute("IsPriceLocked", "Y");
						lineInfoElement.setAttribute("UnitPrice", unitPrice
								.toString());
						lineElement.appendChild(lineInfoElement);
					} else {
						ArrayList<String> test = getPrimeLineNumberFromCustomerOrder(
								env, customerOrderHeaderKey, key);
						Float lineTotal = Float.parseFloat(test.get(3)) + value;
						Float unitPrice = lineTotal
								/ (Float.parseFloat(test.get(4)));
						Element lineInfoElement = changeCustomerDoc
								.createElement("OrderLine");
						lineInfoElement.setAttribute("IsPriceLocked", "Y");
						lineInfoElement.setAttribute("UnitPrice", unitPrice
								.toString());
						lineElement.appendChild(lineInfoElement);
					}
				}
			}
			log.debug("changeCustomerDoc"
					+ SCXmlUtil.getString(changeCustomerDoc));
		}
		log.debug("changeCustomerDoc" + SCXmlUtil.getString(changeCustomerDoc));
		Document outputChangeCustomerOrderDoc = api.invoke(env, "changeOrder",
				changeCustomerDoc);
	}

	private void formCustomerDocForPriceChange(YFSEnvironment env,
			String customerOrderNo, String customerOrderHeaderKey)
			throws Exception, RemoteException {
		log.debug("---> formCustomerDocForPriceChange()");
		Document changeCustomerDoc;
		changeCustomerDoc = null;
		if (htPrice.size() > 0) {
			// form the document
			changeCustomerDoc = YFCDocument.createDocument("Order")
					.getDocument();
			Element customerElement = changeCustomerDoc.getDocumentElement();
			customerElement.setAttribute("OrderNo", customerOrderNo);
			customerElement.setAttribute("DocumentType", "0001");
			// customerElement.setAttribute("EnterpriseCode", "xpedx");
			// new enterprise code change
			customerElement.setAttribute("EnterpriseCode", customerOrderDetails
					.get(2));

			Element orderLinesElement = changeCustomerDoc
					.createElement("OrderLines");
			customerElement.appendChild(orderLinesElement);
			Enumeration<String> Enumeration = htPrice.keys();
			while (Enumeration.hasMoreElements()) {
				String key = (String) Enumeration.nextElement();
				Float value = htPrice.get(key);
				// get prime line number
				ArrayList<String> test = getPrimeLineNumberFromCustomerOrder(
						env, customerOrderHeaderKey, key);
				String primeLine = test.get(0);
				Enumeration<String> Enumeration3 = htCurrentCustomerOrder
						.keys();
				while (Enumeration3.hasMoreElements()) {
					String key1 = (String) Enumeration3.nextElement();
					Element value1 = htCurrentCustomerOrder.get(key1);
				}
				Element inputChainedOrderLineElement = htCurrentCustomerOrder
						.get(test.get(5));
				String linetotal = SCXmlUtil.getXpathAttribute(
						inputChainedOrderLineElement,
						"./LinePriceInfo/@LineTotal");
				Float lineTotal = Float.parseFloat(linetotal) + value;
				Float unitPrice = lineTotal / (Float.parseFloat(test.get(4)));
				Element orderLineElement = changeCustomerDoc
						.createElement("OrderLine");
				orderLineElement.setAttribute("Action", "Modify");
				orderLineElement.setAttribute("PrimeLineNo", primeLine);
				orderLineElement.setAttribute("SubLineNo", "1");
				orderLinesElement.appendChild(orderLineElement);
				Element lineInfoElement = changeCustomerDoc
						.createElement("LinePriceInfo");
				lineInfoElement.setAttribute("IsPriceLocked", "Y");
				lineInfoElement.setAttribute("UnitPrice", unitPrice.toString());
				orderLineElement.appendChild(lineInfoElement);

			}
			/*******************************************************************
			 * Added by Arun Sekhar on 19-Feb-2011 - for a bug found out during
			 * the fix test of 1015 Without this, changeOrder was failing due to
			 * the error-="YFS: Order cannot be modified in current status"
			 * (Even after applying Modification rules)
			 ******************************************************************/
			changeCustomerDoc.getDocumentElement()
					.setAttribute("Override", "Y");
			/** ************************************************************************************* */

			log.debug("changeCustomerDoc"
					+ SCXmlUtil.getString(changeCustomerDoc));
			Document outputChangeCustomerOrderDoc = api.invoke(env,
					"changeOrder", changeCustomerDoc);
			log.debug("changeOrder o/p: "
					+ SCXmlUtil.getString(outputChangeCustomerOrderDoc));
		}
		log.debug("<--- formCustomerDocForPriceChange()");
	}

	/**
	 * csc stands for Convert Special Character. Change &, <, ", ' into XML
	 * acceptable. Because it could be used frequently, it is short-named to
	 * 'csc'. Usually when a string is used for XML values, the string should be
	 * parsed first.
	 * 
	 * @param str
	 *            the String to convert.
	 * @return converted String with & to &amp;amp;, < to &amp;lt;, " to
	 *         &amp;quot;, ' to &amp;apos;
	 */
	// public static String csc(String str) {
	// if (str == null || str.length() == 0)
	// return str;
	//    
	// StringBuffer buf = new StringBuffer(str);
	// int i = 0;
	// char c;
	//    
	// while (i < buf.length()) {
	// c = buf.charAt(i);
	// if (c == '&') {
	// buf.replace(i, i+1, "&amp;");
	// i += 5;
	// } else if (c == '<') {
	// buf.replace(i, i+1, "&lt;");
	// i += 4;
	// } else if (c == '"') {
	// buf.replace(i, i+1, "&quot;");
	// i += 6;
	// } else if (c == '\'') {
	// buf.replace(i, i+1, "&apos;");
	// i += 6;
	// } else if (c == '>') {
	// buf.replace(i, i+1, "&gt;");
	// i += 4;
	// } else
	// i++;
	// }
	//    
	// return buf.toString();
	// }
	private void setOrderHeaderLevelAttributes(Element inputElement,
			Element outputLineElement) {
		if (inputElement.hasAttribute("ShipNode")
				&& !YFCCommon.isVoid("ShipNode"))
			outputLineElement.setAttribute("ShipNode", inputElement
					.getAttribute("ShipNode"));
		if (inputElement.hasAttribute("CustomerPONo")
				&& !YFCCommon.isVoid("CustomerPONo"))
			outputLineElement.setAttribute("CustomerPONo", inputElement
					.getAttribute("CustomerPONo"));
		if (inputElement.hasAttribute("ReqDeliveryDate")
				&& !YFCCommon.isVoid("ReqDeliveryDate"))
			outputLineElement.setAttribute("ReqDeliveryDate", inputElement
					.getAttribute("ReqDeliveryDate"));
		if (inputElement.hasAttribute("OrderDate")
				&& !YFCCommon.isVoid("OrderDate"))
			outputLineElement.setAttribute("OrderDate", inputElement
					.getAttribute("OrderDate"));
		if (inputElement.hasAttribute("SourceType")
				&& !YFCCommon.isVoid("SourceType"))
			outputLineElement.setAttribute("SourceType", inputElement
					.getAttribute("SourceType"));
	}

	private Hashtable<String, Document> updateChangeOrderXMLInHTChangeOrderForDeletion(
			Element inputChainedOrderLineElement,
			Document changeFulfillmentOrderDoc, String webLineNumber,
			Element inputOrderLineELement) throws Exception {
		String eWebLineNumber = "";
		NodeList orderLinesList = changeFulfillmentOrderDoc
				.getElementsByTagName("OrderLines");
		int orderLinesLength = orderLinesList.getLength();
		if (orderLinesLength != 0) {
			Element orderLinesElement = (Element) orderLinesList.item(0);
			log.debug("orderLinesElement"
					+ SCXmlUtil.getString(orderLinesElement));

			NodeList orderLineList = orderLinesElement
					.getElementsByTagName("OrderLine");
			int orderLineLength = orderLineList.getLength();
			if (orderLineLength != 0) {
				for (int oCount = 0; oCount < orderLineLength; oCount++) {
					Element orderLineElement = (Element) orderLineList
							.item(oCount);
					// check if the line already exists then update the line
					eWebLineNumber = SCXmlUtil.getXpathAttribute(
							orderLineElement, "./Extn/@ExtnWebLineNumber");

					if (eWebLineNumber.equals(webLineNumber)) {
						Float eQty = Float.parseFloat(orderLineElement
								.getAttribute("OrderedQty"));
						Float iQty = Float.parseFloat(inputOrderLineELement
								.getAttribute("OrderedQty"));
						Float uQty = eQty - iQty;
						// update the orderline
						orderLinesElement.removeChild(orderLineElement);

						Node inputOrderLineNode = changeFulfillmentOrderDoc
								.importNode(inputOrderLineELement, true);
						Element inputOrderLineNodeElement = (Element) inputOrderLineNode;
						inputOrderLineNodeElement.setAttribute("Action",
								"MODIFY");
						inputOrderLineNodeElement.setAttribute("OrderedQty",
								uQty.toString());
						orderLinesElement
								.appendChild(inputOrderLineNodeElement);
					}
				}
			}
		}
		log.debug("changeFulfillmentOrderDoc"
				+ SCXmlUtil.getString(changeFulfillmentOrderDoc));
		htChangeOrder.put(fulfillmentOrderDetails.get(1),
				changeFulfillmentOrderDoc);
		return htChangeOrder;
	}

	private Hashtable<String, Document> createChangeOrderXMLForCWhenHTChangeOrderisEmptyForDeletion(
			YFSEnvironment env, String webLineNumber,
			Element inputOrderLineELement) throws RemoteException {
		Document newDoc = YFCDocument.createDocument().getDocument();
		String eWebLineNumber = "";
		// get the orderlinekey
		// form the input to get the fulfillment order
		Document inputFulfillmentOrderDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element inputFulfillmentOrderElement = inputFulfillmentOrderDoc
				.getDocumentElement();
		inputFulfillmentOrderElement.setAttribute("OrderHeaderKey",
				fulfillmentOrderDetails.get(1));
		// Document outputFulfillmentOrderDoc = api.invoke(env, "getOrderList",
		// inputFulfillmentOrderDoc);

		Document outputFulfillmentOrderDoc = getSterlingOrderList(env,
				fulfillmentOrderDetails.get(0));

		NodeList fOrderList = outputFulfillmentOrderDoc
				.getElementsByTagName("Order");
		// Document outputCustomerListDoc = null;
		int oLength = fOrderList.getLength();

		if (oLength != 0) {
			Element changeOrderElement = (Element) fOrderList.item(0);

			changeOrderElement.setAttribute("Action", "MODIFY");

			newDoc.appendChild(newDoc.importNode(changeOrderElement, true));
			newDoc.renameNode(newDoc.getDocumentElement(), newDoc
					.getNamespaceURI(), "Order");

			NodeList orderLinesList = newDoc.getElementsByTagName("OrderLines");
			int orderLinesLength = orderLinesList.getLength();

			if (orderLinesLength != 0) {
				Element orderLinesElement = (Element) orderLinesList.item(0);

				NodeList orderLineList = orderLinesElement
						.getElementsByTagName("OrderLine");
				int orderLineLength = orderLineList.getLength();
				if (orderLineLength != 0) {
					for (int oCount = 0; oCount < orderLineLength; oCount++) {
						Element orderLineElement = (Element) orderLineList
								.item(oCount);
						// check if the line already exists then update the line
						eWebLineNumber = SCXmlUtil.getXpathAttribute(
								orderLineElement, "./Extn/@ExtnWebLineNumber")
								+ "|"
								+ SCXmlUtil.getXpathAttribute(orderLineElement,
										"./Extn/@ExtnLegacyLineNumber");
						log.debug("eWebLineNumber" + eWebLineNumber);
						log.debug("webLineNumber" + webLineNumber);
						if (eWebLineNumber.equals(webLineNumber)) {
							Float eQty = Float.parseFloat(orderLineElement
									.getAttribute("OrderedQty"));
							Float iQty = Float.parseFloat(inputOrderLineELement
									.getAttribute("OrderedQty"));
							Float uQty = eQty - iQty;
							// String primelineNumber =
							// inputOrderLineELement.getAttribute("PrimeLineNo");
							// String subLineNumber =
							// inputOrderLineELement.getAttribute("SubLineNo");

							// //update the orderline
							orderLinesElement.removeChild(orderLineElement);

							Node inputOrderLineNode = newDoc.importNode(
									inputOrderLineELement, true);
							Element inputOrderLineNodeElement = (Element) inputOrderLineNode;
							inputOrderLineNodeElement.setAttribute("Action",
									"MODIFY");
							inputOrderLineNodeElement.setAttribute(
									"OrderedQty", uQty.toString());
							orderLinesElement
									.appendChild(inputOrderLineNodeElement);
						}

					}
				}
			}
			log.debug("the change order doc looks like"
					+ SCXmlUtil.getString(newDoc));
			htChangeOrder.put(fulfillmentOrderDetails.get(1), newDoc);

		}
		return htChangeOrder;
	}

	private Document formNewInputXML(YFSEnvironment env, Document newInputDoc1,
			ArrayList<String> weblineArray) throws RemoteException,
			YFSException, YIFClientCreationException {
		log.debug("---> formNewInputXML()");
		log.debug("weblineArray length: " + weblineArray.size());

		log.debug("Triggerring getOrderList for the fulfillmentOrder: "
				+ fulfillmentOrderDetails.get(0));
		Document fulfillmentOrderLisDoc = getSterlingOrderList(env,
				fulfillmentOrderDetails.get(0));
		log.debug("getOrderList o/p for the fulfillmentOrder"
				+ SCXmlUtil.getString(fulfillmentOrderLisDoc));
		NodeList fOrderLinesList = fulfillmentOrderLisDoc
				.getElementsByTagName("OrderLines");
		Element fOrderLinesElement = (Element) fOrderLinesList.item(0);
		NodeList fOrderLineList = fOrderLinesElement
				.getElementsByTagName("OrderLine");
		int fLineLength = fOrderLineList.getLength();
		log.debug("No. lines in the fulfillment order: " + fLineLength);
		for (int fCounter = 0; fCounter < fLineLength; fCounter++) {
			Element fOrderLineElement = (Element) fOrderLineList.item(fCounter);

			String status = SCXmlUtil.getXpathAttribute(fOrderLineElement,
					"./OrderStatuses/OrderStatus/@Status");

			if (!status.equals("9000")) {
				log.debug("!status.equals('9000')");
				String webLineNumber = SCXmlUtil.getXpathAttribute(
						fOrderLineElement, "./Extn/@ExtnWebLineNumber");
				/**
				 * Comment added by Arun Sekhar on 15th Feb 2011. The below line
				 * of code is wrong. The XML won't have the attribute
				 * 'ProcessCode' or 'LineProcessCode'. We need to read the
				 * LineProcessCode from the input XML's line level (if we need
				 * this attribute value)
				 */
				/** ************************************************************* */
				String lineProcessCode = fOrderLineElement
						.getAttribute("ProcessCode");
				/** ************************************************************* */
				log.debug("Line No:" + fCounter + "\nwebLineNumber: "
						+ webLineNumber + "\nlineProcessCode: "
						+ lineProcessCode);

				if (!weblineArray.contains(webLineNumber)
						&& !(lineProcessCode.equals("A"))) {
					log
							.debug("weblineArray does NOT contain(webLineNumber) and lineProcessCode NOT equals('A')");
					/**
					 * Modified by Arun Sekhar on 15th Feb 2011. The below line
					 * of code is wrong. The attribute to be set is
					 * LineProcessCode *
					 */
					/** ************************************************************* */
					/* fOrderLineElement.setAttribute("ProcessCode", "R"); */
					fOrderLineElement.setAttribute("LineProcessCode", "R");
					/** ************************************************************* */

					fOrderLineElement.setAttribute("OrderedQty", "0");
					log
							.debug("Reset the orderline with LineProcessCode = R and OrderedQty = 0");
					Node inputNode = newInputDoc1.importNode(fOrderLineElement,
							true);
					NodeList iOrderLinesList = newInputDoc1
							.getDocumentElement().getElementsByTagName(
									"OrderLines");
					Element iOrderLinesElement = (Element) iOrderLinesList
							.item(0);
					iOrderLinesElement.appendChild(inputNode);
				}
			}
		}
		log.debug("<--- formNewInputXML()");
		// stampDivisionInInputDoc(env,newInputDoc1);
		return newInputDoc1;
	}

	private void stampDivisionInInputDoc(YFSEnvironment env,
			Document newInputDoc) throws YFSException, RemoteException,
			YIFClientCreationException {
		String customerID = newInputDoc.getDocumentElement().getAttribute(
				"BillToID");
		String organizationCode = newInputDoc.getDocumentElement()
				.getAttribute("EnterpriseCode");
		String envCode = getDivisionCode(env, customerID, organizationCode);
		Element newInputElement = newInputDoc.getDocumentElement();
		if (newInputElement.hasAttribute("ShipNode")) {
			String division = newInputDoc.getDocumentElement().getAttribute(
					"ShipNode")
					+ "_" + envCode;
			// stamp the shipnode
			newInputDoc.getDocumentElement().setAttribute("ShipNode", division);
		}
		NodeList extnNodeList = newInputElement.getElementsByTagName("Extn");
		int extnLength = extnNodeList.getLength();
		if (extnLength != 0) {
			Element extnElement = (Element) extnNodeList.item(0);
			if (extnElement.hasAttribute("ExtnOrderDivision")
					&& !YFCCommon.isVoid(extnElement
							.getAttribute("ExtnOrderDivision"))) {
				String newOrderDivision = extnElement
						.getAttribute("ExtnOrderDivision")
						+ "_" + envCode;
				extnElement.setAttribute("ExtnOrderDivision", newOrderDivision);
			}
			if (extnElement.hasAttribute("ExtnCustomerDivision")
					&& !YFCCommon.isVoid(extnElement
							.getAttribute("ExtnCustomerDivision"))) {
				String newCustomerDivision = extnElement
						.getAttribute("ExtnCustomerDivision")
						+ "_" + envCode;
				extnElement.setAttribute("ExtnCustomerDivision",
						newCustomerDivision);
			}
		}

		// browse thru order lines
		NodeList orderLinesList = newInputDoc
				.getElementsByTagName("OrderLines");
		int linesLength = orderLinesList.getLength();
		if (linesLength != 0) {
			Element orderLinesElement = (Element) orderLinesList.item(0);
			NodeList orderLineList = orderLinesElement
					.getElementsByTagName("OrderLine");
			int lineLength = orderLineList.getLength();
			if (lineLength != 0) {
				for (int counter = 0; counter < lineLength; counter++) {
					Element orderLineElement = (Element) orderLineList
							.item(counter);
					if (orderLineElement.hasAttribute("ShipNode")
							&& !YFCCommon.isVoid(orderLineElement
									.getAttribute("ShipNode"))) {
						String newShipNode = orderLineElement
								.getAttribute("ShipNode")
								+ "_" + envCode;
						orderLineElement.setAttribute("ShipNode", newShipNode);
					}
				}
			}
		}
	}

	private String getDivisionCode(YFSEnvironment env, String customerID,
			String orgCode) throws YFSException, RemoteException {
		String envCode = "";
		// form the input
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerID", customerID);
		inputCustomerElement.setAttribute("OrganizationCode", orgCode);

		Document inputCustomerListTemplateDoc = SCXmlUtil
				.createDocument("CustomerList");
		Element inputcustomerListTemplateElement = inputCustomerListTemplateDoc
				.getDocumentElement();
		Element inputCustomerTemplateElement = inputCustomerListTemplateDoc
				.createElement("Customer");
		inputcustomerListTemplateElement
				.appendChild(inputCustomerTemplateElement);
		Element extnTemplateElement = inputCustomerListTemplateDoc
				.createElement("Extn");
		inputCustomerTemplateElement.appendChild(extnTemplateElement);

		env.setApiTemplate("getCustomerList", inputCustomerListTemplateDoc);
		Document outputCustomerDoc = api.invoke(env, "getCustomerList",
				inputCustomerDoc);
		env.clearApiTemplate("getCustomerList");
		NodeList customerList = outputCustomerDoc
				.getElementsByTagName("Customer");
		int customerLength = customerList.getLength();
		if (customerLength != 0) {
			Element customerElement = (Element) customerList.item(0);
			envCode = SCXmlUtil.getXpathAttribute(customerElement,
					"./Extn/@ExtnEnvironmentCode");
		}
		return envCode;
	}

	private ArrayList<String> populateWeblineArray(Element inputElement,
			ArrayList<String> weblineArray) throws Exception {
		log.debug("---> populateWeblineArray()");
		log.debug("Input Element: " + SCXmlUtil.getString(inputElement));
		NodeList inputLinesList = inputElement
				.getElementsByTagName("OrderLines");
		Element inputLinesElement = (Element) inputLinesList.item(0);
		NodeList inputLineList = inputLinesElement
				.getElementsByTagName("OrderLine");
		int lineLength = inputLineList.getLength();
		for (int lCounter = 0; lCounter < lineLength; lCounter++) {
			Element inputLineElement = (Element) inputLineList.item(lCounter);
			String webLine = SCXmlUtil.getXpathAttribute(inputLineElement,
					"./Extn/@ExtnWebLineNumber");
			log.debug("Line No. " + lCounter + ": ExtnWebLineNumber: "
					+ webLine);
			weblineArray.add(webLine);
		}
		log.debug("Size of weblineArray at the end of this method: "
				+ weblineArray.size());
		log.debug("<--- populateWeblineArray()");
		return weblineArray;
	}

	private Document invokeCreateChainedOrderForProcessCodeAForExistingCustomerOrder(
			YFSEnvironment env, Document outputOrderDocument,
			Element inputElement, boolean bckExists) throws Exception {
		String weblineNumber = "";
		ArrayList<String> primeArray = new ArrayList<String>();
		log.debug("outputOrderDocument"
				+ SCXmlUtil.getString(outputOrderDocument));
		log.debug("The newInputElement is: "
				+ SCXmlUtil.getString(inputElement));
		// get the orderheaderkey of the above created order
		Element outputOrderElement = outputOrderDocument.getDocumentElement();
		String orderHeaderKey = outputOrderElement
				.getAttribute("OrderHeaderKey");

		Document newInputDoc = YFCDocument.createDocument().getDocument();
		newInputDoc.appendChild(newInputDoc.importNode(inputElement, true));
		newInputDoc.renameNode(newInputDoc.getDocumentElement(), newInputDoc
				.getNamespaceURI(), "Order");
		log.debug("newInputDoc" + SCXmlUtil.getString(newInputDoc));
		Element newInputElement = newInputDoc.getDocumentElement();
		newInputElement.removeAttribute("OrderHeaderKey");
		newInputElement.removeAttribute("OrderNo");
		newInputElement.removeAttribute("PaymentStatus");
		newInputElement.setAttribute("Action", "CREATE");
		newInputElement.setAttribute("EntryType", "Web");
		newInputElement.setAttribute("CarrierServiceCode", "Standard Shipping");

		Element newInputExtnElement = (Element) newInputElement
				.getElementsByTagName("Extn").item(0);
		newInputExtnElement.setAttribute("ExtnBillToName", billToName);
		newInputExtnElement.setAttribute("ExtnSAPParentName", sapParentName);
		newInputExtnElement.setAttribute("ExtnOrigEnvironmentCode", origEnvtId);
		newInputExtnElement.setAttribute("ExtnSourceType", "1");

		NodeList inputOrderLinesList = newInputDoc
				.getElementsByTagName("OrderLines");
		int orderLinesLength = inputOrderLinesList.getLength();
		if (orderLinesLength != 0) {
			Element inputOrderLinesElement = (Element) inputOrderLinesList
					.item(0);
			NodeList inputOrderLineList = inputOrderLinesElement
					.getElementsByTagName("OrderLine");
			int orderLineLength = inputOrderLineList.getLength();
			if (orderLineLength != 0) {
				for (int oCounter = 0; oCounter < orderLineLength; oCounter++) {
					Integer oCounterInt = new Integer(oCounter) + 1;
					Element inputOrderLineElement = (Element) inputOrderLineList
							.item(oCounter);
					// for comment line
					String lineType = inputOrderLineElement
							.getAttribute("LineType");
					// if(lineType.equalsIgnoreCase("C")){
					if (!lineType.equalsIgnoreCase("S")
							&& !lineType.equalsIgnoreCase("P")) {
						// inputOrderLineElement.setAttribute("OrderedQty",
						// "1");
						inputOrderLineElement.setAttribute("ValidateItem", "N");
					}
					/***********************************************************
					 * Modified by Arun Sekhar on 15th Feb 2011. Fix for the bug
					 * 948
					 **********************************************************/
					/* if (lineType.equalsIgnoreCase("C")) { */
					if (lineType.equalsIgnoreCase("C")
							|| lineType.equalsIgnoreCase("M")) {
						inputOrderLineElement.setAttribute("OrderedQty", "1");
					}
					if (lineType.equalsIgnoreCase("S")) {
						String customerPartNo = SCXmlUtil.getXpathAttribute(
								inputElement, "./Item/@CustomerItem");
						String envtId = SCXmlUtil.getXpathAttribute(
								inputElement, "./Extn/@ExtnEnvtId");
						String companyCode = SCXmlUtil.getXpathAttribute(
								inputElement, "./Extn/@ExtnCompanyId");
						String legacyCustomerNumber = SCXmlUtil
								.getXpathAttribute(inputElement,
										"./Extn/@ExtnCustomerNo");
						String itemId = getLegacyItemNumberFromXref(env,
								customerPartNo, envtId, companyCode,
								legacyCustomerNumber);
						// inputOrderLineElement.setAttribute("ItemID", itemId);
						Element itemElement = (Element) inputOrderLineElement
								.getElementsByTagName("Item").item(0);
						if (itemElement != null) {
							itemElement.setAttribute("ItemID", itemId);
						}
					}
					weblineNumber = SCXmlUtil.getXpathAttribute(
							inputOrderLineElement, "./Extn/@ExtnWebLineNumber");
					log.debug("orderHeaderKey" + orderHeaderKey);

					primeArray = getPrimeLineNumberFromCustomerOrder(env,
							orderHeaderKey, weblineNumber);

					inputOrderLineElement.setAttribute(
							"ChainedFromOrderLineKey", primeArray.get(1));
					inputOrderLineElement.setAttribute(
							"ChainedFromOrderHeaderKey", orderHeaderKey);

					inputOrderLineElement.setAttribute("Action", "CREATE");
					Element chainedOrderElement = newInputDoc
							.createElement("ChainedFrom");
					chainedOrderElement.setAttribute("DocumentType", "0001");
					// chainedOrderElement.setAttribute("EnterpriseCode",
					// "xpedx");
					// new change for enterprise code
					// chainedOrderElement.setAttribute("EnterpriseCode",
					// customerOrderDetails.get(2));
					chainedOrderElement.setAttribute("EnterpriseCode", "xpedx");
					chainedOrderElement.setAttribute("OrderHeaderKey",
							orderHeaderKey);
					chainedOrderElement.setAttribute("PrimeLineNo", oCounterInt
							.toString());
					chainedOrderElement.setAttribute("SubLineNo", "1");
					inputOrderLineElement.appendChild(chainedOrderElement);

					Element inputOrderLineExtnElement = (Element) inputOrderLineElement
							.getElementsByTagName("Extn").item(0);
					if (!primeArray.get(6).isEmpty()) {
						inputOrderLineExtnElement.setAttribute("ExtnLineType",
								primeArray.get(6));
					}

					if (oCounter == 0) {
						String extnLineType = "";
						if (!primeArray.get(6).isEmpty()) {
							extnLineType = primeArray.get(6);
							if (extnLineType
									.equalsIgnoreCase(XPXLiterals.DIRECT)) {
								newInputElement.setAttribute("OrderType",
										"DIRECT_ORDER");
							} else if (extnLineType
									.equalsIgnoreCase(XPXLiterals.STOCK)) {
								newInputElement.setAttribute("OrderType",
										"STOCK_ORDER");
							}

						}

					}

					NodeList linePriceNodeList = inputOrderLineElement
							.getElementsByTagName("LinePriceInfo");
					int priceLength = linePriceNodeList.getLength();
					if (priceLength != 0) {
						Element linePriceElement = (Element) linePriceNodeList
								.item(0);
						if (linePriceElement.hasAttribute("UnitPrice")
								&& !YFCCommon.isVoid(linePriceElement
										.getAttribute("UnitPrice"))) {
							linePriceElement.setAttribute("IsPriceLocked", "Y");
						}
					}

				}
			}
		}
		log.debug("newInputDoc" + SCXmlUtil.getString(newInputDoc));
		Document outputChainedOrderDoc = api.invoke(env, "createOrder",
				newInputDoc);
		log.debug("outputChainedOrderDoc"
				+ SCXmlUtil.getString(outputChainedOrderDoc));

		Document templateDoc = setTemplate(env);
		env.setApiTemplate("getOrderList", templateDoc);
		Document listDoc = api.invoke(env, "getOrderList",
				outputChainedOrderDoc);
		env.clearApiTemplate("getOrderList");
		NodeList orderList = listDoc.getElementsByTagName("Order");
		Element orderElement = (Element) orderList.item(0);
		Document newInputDoc1 = YFCDocument.createDocument().getDocument();
		newInputDoc1.appendChild(newInputDoc1.importNode(orderElement, true));
		newInputDoc1.renameNode(newInputDoc1.getDocumentElement(), newInputDoc1
				.getNamespaceURI(), "Order");
		if (bckExists) {
			// change order status to backordered in case backorder quantity
			// exists
			Document changeStatusDocument = YFCDocument.createDocument("Order")
					.getDocument();
			Element changeStatusElement = changeStatusDocument
					.getDocumentElement();
			changeStatusElement.setAttribute("DocumentType", "0001");
			// changeStatusElement.setAttribute("EnterpriseCode", "xpedx");
			// new change for enterprise Code
			changeStatusElement.setAttribute("EnterpriseCode",
					fulfillmentOrderDetails.get(2));
			changeStatusElement.setAttribute("IgnoreTransactionDependencies",
					"Y");
			changeStatusElement.setAttribute("ModificationReasonCode",
					"CHANGE_FULFILLMENT_TYPE");
			changeStatusElement.setAttribute("ModificationReasonText",
					"Changed Fulfillment Type");
			changeStatusElement.setAttribute("OrderNo", newInputDoc1
					.getDocumentElement().getAttribute("OrderNo"));
			// changeStatusElement.setAttribute("TransactionId",
			// "XPX_BACK_ORDER_IN_LEGACY.0001.ex");
			changeStatusElement.setAttribute("TransactionId",
					"XPX_LEGACY_BACKORD.0001.ex");
			Element orderLinesElement = changeStatusDocument
					.createElement("OrderLines");
			changeStatusElement.appendChild(orderLinesElement);
			NodeList orderLinesList = newInputDoc1
					.getElementsByTagName("OrderLines");
			Element orderLinesElement1 = (Element) orderLinesList.item(0);
			NodeList orderLineList = orderLinesElement1
					.getElementsByTagName("OrderLine");
			for (int lCount = 0; lCount < orderLineList.getLength(); lCount++) {
				Element orderLineElement = (Element) orderLineList.item(lCount);
				Element orderLine1Element = changeStatusDocument
						.createElement("OrderLine");
				orderLine1Element.setAttribute("BaseDropStatus", "1100.5150");
				orderLine1Element.setAttribute("PrimeLineNo", orderLineElement
						.getAttribute("PrimeLineNo"));
				orderLine1Element.setAttribute("SubLineNo", orderLineElement
						.getAttribute("SubLineNo"));
				orderLinesElement.appendChild(orderLine1Element);
				// Node appendNode =
				// changeStatusDocument.importNode(orderLine1Element, true);
				// orderLinesElement.appendChild(appendNode);
				Element orderLineTranElement = changeStatusDocument
						.createElement("OrderLineTranQuantity");
				orderLineTranElement.setAttribute("Quantity", orderLineElement
						.getAttribute("OrderedQty"));
				orderLineTranElement.setAttribute("TransactionalUOM",
						orderLineElement.getAttribute("OrderingUOM"));
				orderLine1Element.appendChild(orderLineTranElement);
			}

			// invoke change order status
			Document statusDoc = api.invoke(env, "changeOrderStatus",
					changeStatusDocument);
		}
		return outputChainedOrderDoc;
	}

	private Document invokeCreateChainedOrderForProcessCodeA(
			YFSEnvironment env, Document outputOrderDocument,
			String legacyOrderNo) throws RemoteException {

		// get the orderheaderkey of the above created order
		Element outputOrderElement = outputOrderDocument.getDocumentElement();
		String orderHeaderKey = outputOrderElement
				.getAttribute("OrderHeaderKey");
		String orderNo = outputOrderElement.getAttribute("OrderNo");

		// form the input xml for creating a chained order
		// get the list
		Document inputChainedOrderDoc = getSterlingOrderList(env, orderNo);
		NodeList orderNodeList = inputChainedOrderDoc
				.getElementsByTagName("Order");
		// int chainLength = orderNodeList.getLength();
		Element inputChainedOrderElement = (Element) orderNodeList.item(0);
		Document newInputDoc = YFCDocument.createDocument().getDocument();
		newInputDoc.appendChild(newInputDoc.importNode(
				inputChainedOrderElement, true));
		newInputDoc.renameNode(newInputDoc.getDocumentElement(), newInputDoc
				.getNamespaceURI(), "Order");
		log.debug("newInputDoc" + SCXmlUtil.getString(newInputDoc));
		Element newInputElement = newInputDoc.getDocumentElement();
		newInputElement.removeAttribute("OrderHeaderKey");
		newInputElement.removeAttribute("OrderNo");
		newInputElement.removeAttribute("PaymentStatus");
		newInputElement.setAttribute("Action", "CREATE");
		// stamp legacyorder no
		NodeList extnList = newInputElement.getElementsByTagName("Extn");
		int extnLength = extnList.getLength();
		if (extnLength != 0) {
			Element extnElement = (Element) extnList.item(0);
			extnElement.setAttribute("ExtnLegacyOrderNo", legacyOrderNo);
		}
		NodeList inputOrderLinesList = newInputDoc
				.getElementsByTagName("OrderLines");
		int orderLinesLength = inputOrderLinesList.getLength();
		if (orderLinesLength != 0) {
			Element inputOrderLinesElement = (Element) inputOrderLinesList
					.item(0);
			NodeList inputOrderLineList = inputOrderLinesElement
					.getElementsByTagName("OrderLine");
			int orderLineLength = inputOrderLineList.getLength();
			if (orderLineLength != 0) {
				for (int oCounter = 0; oCounter < orderLineLength; oCounter++) {

					Integer oCounterInt = new Integer(oCounter) + 1;
					Element inputOrderLineElement = (Element) inputOrderLineList
							.item(oCounter);
					inputOrderLineElement.removeAttribute("OrderHeaderKey");
					inputOrderLineElement.removeAttribute("OrderLineKey");
					inputOrderLineElement.setAttribute("Action", "CREATE");
					Element chainedOrderElement = newInputDoc
							.createElement("ChainedFrom");
					chainedOrderElement.setAttribute("DocumentType", "0001");
					// chainedOrderElement.setAttribute("EnterpriseCode",
					// "xpedx");
					// new change for enterprise code
					// chainedOrderElement.setAttribute("EnterpriseCode",
					// customerOrderDetails.get(2));
					chainedOrderElement.setAttribute("EnterpriseCode", "xpedx");
					chainedOrderElement.setAttribute("OrderHeaderKey",
							orderHeaderKey);
					chainedOrderElement.setAttribute("PrimeLineNo", oCounterInt
							.toString());
					chainedOrderElement.setAttribute("SubLineNo", "1");
					inputOrderLineElement.appendChild(chainedOrderElement);

					// Setting order type of fulfillment order using extn line
					// type attribute of first order line element

					if (oCounter == 0) {
						Element inputOrderLineExtnElement = (Element) inputOrderLineElement
								.getElementsByTagName("Extn").item(0);
						if (inputOrderLineExtnElement != null) {
							String extnLineType = inputOrderLineExtnElement
									.getAttribute("ExtnLineType");
							if (extnLineType
									.equalsIgnoreCase(XPXLiterals.DIRECT)) {
								newInputElement.setAttribute("OrderType",
										"DIRECT_ORDER");
							} else if (extnLineType
									.equalsIgnoreCase(XPXLiterals.STOCK)) {
								newInputElement.setAttribute("OrderType",
										"STOCK_ORDER");
							}

						}
					}

					NodeList linePriceNodeList = inputOrderLineElement
							.getElementsByTagName("LinePriceInfo");
					int priceLength = linePriceNodeList.getLength();
					if (priceLength != 0) {
						Element linePriceElement = (Element) linePriceNodeList
								.item(0);
						if (linePriceElement.hasAttribute("UnitPrice")
								&& !YFCCommon.isVoid(linePriceElement
										.getAttribute("UnitPrice"))) {
							linePriceElement.setAttribute("IsPriceLocked", "Y");
						}
					}
				}
			}
		}

		log.debug("The input to fulfillment order creation is: "
				+ SCXmlUtil.getString(newInputDoc));

		Document outputChainedOrderDoc = api.invoke(env, "createOrder",
				newInputDoc);
		log.debug("outputChainedOrderDoc"
				+ SCXmlUtil.getString(outputChainedOrderDoc));
		return outputChainedOrderDoc;
	}

	private Document invokeCreateCustomerOrderForProcessCodeA(
			YFSEnvironment env, Document inXML, Element inputElement,
			String webConfirmationNumber) throws Exception, RemoteException {

		String envtCode = "";
		String extnLineType = "";
		// check if the customer order exists
		Document inputLineDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element inputLineElement = inputLineDoc.getDocumentElement();
		Element extnElement = inputLineDoc.createElement("Extn");
		extnElement.setAttribute("ExtnWebConfNum", webConfirmationNumber);
		inputLineElement.appendChild(extnElement);
		Document templateDoc = setTemplate(env);
		env.setApiTemplate("getOrderList", templateDoc);
		Document outputLineDoc = api.invoke(env, "getOrderList", inputLineDoc);
		env.clearApiTemplate("getOrderList");

		Element outputLineElement = outputLineDoc.getDocumentElement();

		setOrderHeaderLevelAttributes(inputElement, outputLineElement);
		String totalRecords = outputLineElement.getAttribute("TotalOrderList");
		log.debug("totalRecords" + totalRecords);
		if (totalRecords.equals("0")) {
			// create customer order
			inputElement.setAttribute("Action", "CREATE");
			inputElement.setAttribute("OrderType", "Customer");
			inputElement.setAttribute("EntryType", "Web");
			inputElement
					.setAttribute("CarrierServiceCode", "Standard Shipping");
			// stamp legacy order number empty
			NodeList extnNodeList = inputElement.getElementsByTagName("Extn");
			int extnLength = extnNodeList.getLength();
			if (extnLength != 0) {
				Element extnElement1 = (Element) extnNodeList.item(0);
				extnElement1.setAttribute("ExtnLegacyOrderNo", "");

				// Added by Prasanth to include all extn attributes
				extnElement1.setAttribute("ExtnBillToName", billToName);
				extnElement1.setAttribute("ExtnSAPParentName", sapParentName);
				extnElement1
						.setAttribute("ExtnOrigEnvironmentCode", origEnvtId);
				extnElement1.setAttribute("ExtnSourceType", "1");

				envtCode = extnElement1.getAttribute("ExtnEnvtId");
			}

			NodeList orderLinesList = inXML.getElementsByTagName("OrderLines");
			int linesLength = orderLinesList.getLength();
			if (linesLength != 0) {
				Element orderLinesElement = (Element) orderLinesList.item(0);
				NodeList orderLineList = orderLinesElement
						.getElementsByTagName("OrderLine");
				int lineLength = orderLineList.getLength();
				if (lineLength != 0) {
					for (int lCounter = 0; lCounter < lineLength; lCounter++) {
						Element orderLineElement = (Element) orderLineList
								.item(lCounter);
						orderLineElement.setAttribute("Action", "CREATE");
						// check for comment line
						String lineType = orderLineElement
								.getAttribute("LineType");
						// if(lineType.equalsIgnoreCase("C"))
						if (!lineType.equalsIgnoreCase("S")
								&& !lineType.equalsIgnoreCase("P")) {
							// orderLineElement.setAttribute("OrderedQty", "1");
							orderLineElement.setAttribute("ValidateItem", "N");
						}
						/*******************************************************
						 * Modified by Arun Sekhar on 15th Feb 2011. Fix for the
						 * bug 948
						 ******************************************************/
						/* if (lineType.equalsIgnoreCase("C")) { */
						if (lineType.equalsIgnoreCase("C")
								|| lineType.equalsIgnoreCase("M")) {
							orderLineElement.setAttribute("OrderedQty", "1");
						}
						if (lineType.equalsIgnoreCase("S")) {
							String customerPartNo = SCXmlUtil
									.getXpathAttribute(inputElement,
											"./Item/@CustomerItem");
							String envtId = SCXmlUtil.getXpathAttribute(
									inputElement, "./Extn/@ExtnEnvtId");
							String companyCode = SCXmlUtil.getXpathAttribute(
									inputElement, "./Extn/@ExtnCompanyId");
							String legacyCustomerNumber = SCXmlUtil
									.getXpathAttribute(inputElement,
											"./Extn/@ExtnCustomerNo");
							String itemId = getLegacyItemNumberFromXref(env,
									customerPartNo, envtId, companyCode,
									legacyCustomerNumber);

							Element itemElement = (Element) orderLineElement
									.getElementsByTagName("Item").item(0);
							if (itemElement != null) {
								itemElement.setAttribute("ItemID", itemId);
							}
						}
						Element itemElement = (Element) orderLineElement
								.getElementsByTagName("Item").item(0);
						if (itemElement != null) {
							String itemId = itemElement.getAttribute("ItemID");
							extnLineType = getInventoryIndicator(env, itemId,
									orderLineElement.getAttribute("ShipNode"),
									envtCode, lineType);
						}

						NodeList extnLineNodeList = orderLineElement
								.getElementsByTagName("Extn");
						int extnLineLength = extnLineNodeList.getLength();
						if (extnLineLength != 0) {
							Element extnLineElement = (Element) extnLineNodeList
									.item(0);
							// extnLineElement.setAttribute("ExtnLegacyLineNumber",
							// "");
							extnLineElement.setAttribute("ExtnLineType",
									extnLineType);
						}

						NodeList linePriceNodeList = orderLineElement
								.getElementsByTagName("LinePriceInfo");
						int priceLength = linePriceNodeList.getLength();
						if (priceLength != 0) {
							Element linePriceElement = (Element) linePriceNodeList
									.item(0);
							if (linePriceElement.hasAttribute("UnitPrice")
									&& !YFCCommon.isVoid(linePriceElement
											.getAttribute("UnitPrice"))) {
								linePriceElement.setAttribute("IsPriceLocked",
										"Y");
							}
						}

					}
				}
			}
			log.debug("inputElement" + SCXmlUtil.getString(inXML));
		}
		log.debug("The input to customer order creation is: "
				+ SCXmlUtil.getString(inXML));
		Document outputOrderDoc = api.invoke(env, "createOrder", inXML);
		return outputOrderDoc;
		// return inXML;
	}

	private String getInventoryIndicator(YFSEnvironment env, String itemId,
			String orderLineShipNode, String envtCode, String lineType)
			throws YFSException, RemoteException {

		String extnLineType = null;

		Document getXPXItemBranchListInputDoc = createXPXItemBranchListInputDoc(
				itemId, orderLineShipNode, envtCode);
		log.debug("The input to getXPXitemBranchExtnListService is: "
				+ SCXmlUtil.getString(getXPXItemBranchListInputDoc));
		Document getXPXItemBranchListOutputDoc = api.executeFlow(env,
				XPXLiterals.GET_XPX_ITEM_BRANCH_LIST_SERVICE,
				getXPXItemBranchListInputDoc);

		Element getXPXItemBranchListOutputDocRoot = getXPXItemBranchListOutputDoc
				.getDocumentElement();
		Element XPXItemExtnElement = (Element) getXPXItemBranchListOutputDocRoot
				.getElementsByTagName(XPXLiterals.E_XPX_ITEM_EXTN).item(0);

		if (XPXItemExtnElement != null) {
			String inventoryIndicator = XPXItemExtnElement
					.getAttribute(XPXLiterals.A_INVENTORY_INDICATOR);

			if ("W".equalsIgnoreCase(inventoryIndicator)) {

				extnLineType = "STOCK";
			}

			if ("M".equalsIgnoreCase(inventoryIndicator)) {
				// orderLine.setAttribute(XPXLiterals.A_LINE_TYPE, "N");
				extnLineType = "DIRECT";
			}
		}

		else {

			/** *Commented out as per Rajendra's comments on 11/03 *** */
			// Checking Item exists in YFS_ITEM
			Document getItemListInputDoc = createItemListInputDoc(itemId);
			env.setApiTemplate(XPXLiterals.GET_ITEM_LIST_API, template);
			Document outputCustomerDoc = api.invoke(env,
					XPXLiterals.GET_ITEM_LIST_API, getItemListInputDoc);
			env.clearApiTemplate(XPXLiterals.GET_ITEM_LIST_API);

			Element itemListDocRoot = outputCustomerDoc.getDocumentElement();

			if (itemListDocRoot.getElementsByTagName(XPXLiterals.E_ITEM)
					.getLength() > 0) {
				// Item exists in YFS_ITEM table
				// orderLine.setAttribute(XPXLiterals.A_LINE_TYPE,
				// XPXLiterals.STOCK);
				extnLineType = XPXLiterals.DIRECT;

			} else {
				if (lineType.equals("M")) {
					// Item does not exist in YFS_ITEM table and its a Special
					// charge line
					extnLineType = XPXLiterals.STOCK;

				}
				if (lineType.equalsIgnoreCase("C")) {
					// Item does not exist in YFS_ITEM table
					// orderLine.setAttribute(XPXLiterals.A_LINE_TYPE,
					// XPXLiterals.STOCK);
					extnLineType = XPXLiterals.STOCK;
				}
			}
		}
		return extnLineType;
	}

	private Document createItemListInputDoc(String itemId) {
		Document getItemListInputDoc = YFCDocument.createDocument(
				XPXLiterals.E_ITEM).getDocument();

		Element getItemListInputDocRoot = getItemListInputDoc
				.getDocumentElement();

		getItemListInputDocRoot.setAttribute(XPXLiterals.A_ITEM_ID, itemId);

		return getItemListInputDoc;
	}

	private Document createXPXItemBranchListInputDoc(String itemId,
			String orderLineShipNode, String envtCode) {
		Document getXPXItemBranchListInputDoc = YFCDocument.createDocument(
				XPXLiterals.E_XPX_ITEM_EXTN).getDocument();
		getXPXItemBranchListInputDoc.getDocumentElement().setAttribute(
				XPXLiterals.A_ITEM_ID, itemId);
		getXPXItemBranchListInputDoc.getDocumentElement().setAttribute(
				XPXLiterals.A_XPX_DIVISION, orderLineShipNode);
		getXPXItemBranchListInputDoc.getDocumentElement().setAttribute(
				"EnvironmentID", envtCode);

		return getXPXItemBranchListInputDoc;
	}

	private Document invokeForOrderProcessCodeD(YFSEnvironment env,
			String webConfirmationNumber, Element inputElement)
			throws Exception, RemoteException {
		Document outputChangeOrderDoc = null;
		// deletion
		// check if the customer order exists
		Document inputLineDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element inputLineElement = inputLineDoc.getDocumentElement();
		Element extnElement = inputLineDoc.createElement("Extn");
		extnElement.setAttribute("ExtnWebConfNum", webConfirmationNumber);
		inputLineElement.appendChild(extnElement);
		Document templateDoc = setTemplate(env);
		env.setApiTemplate("getOrderList", templateDoc);
		Document outputLineDoc = api.invoke(env, "getOrderList", inputLineDoc);
		env.clearApiTemplate("getOrderList");

		Element outputLineElement = outputLineDoc.getDocumentElement();
		setOrderHeaderLevelAttributes(inputElement, outputLineElement);
		String totalRecords = outputLineElement.getAttribute("TotalOrderList");
		log.debug("totalRecords" + totalRecords);
		if (totalRecords.equals("0")) {
			log.debug("the order does not exist");
		} else {
			// make the customer order qty zero
			NodeList customerOrderList = outputLineDoc
					.getElementsByTagName("Order");
			Element customerOrderElement = (Element) customerOrderList.item(0);
			// form a new document
			Document newDoc = YFCDocument.createDocument().getDocument();
			newDoc.appendChild(newDoc.importNode(customerOrderElement, true));
			newDoc.renameNode(newDoc.getDocumentElement(), newDoc
					.getNamespaceURI(), "Order");
			log.debug("newDoc" + SCXmlUtil.getString(newDoc));
			Element newOrderElement = newDoc.getDocumentElement();
			newOrderElement.setAttribute("Action", "MODIFY");
			newOrderElement.setAttribute("Override", "Y");
			NodeList orderLinesNodeList = newDoc
					.getElementsByTagName("OrderLines");
			int linesLength = orderLinesNodeList.getLength();
			if (linesLength != 0) {
				Element orderLinesElement = (Element) orderLinesNodeList
						.item(0);
				NodeList orderLineNodeList = orderLinesElement
						.getElementsByTagName("OrderLine");
				int lineLength = orderLineNodeList.getLength();
				if (lineLength != 0) {
					for (int lCounter = 0; lCounter < lineLength; lCounter++) {
						Element orderLineElement = (Element) orderLineNodeList
								.item(lCounter);
						orderLineElement.setAttribute("Action", "MODIFY");
						orderLineElement.setAttribute("OrderedQty", "0");
					}
				}
			}
			log.debug("newDoc" + SCXmlUtil.getString(newDoc));
			// change order to zero which automatically cancels the chained
			// order
			outputChangeOrderDoc = api.invoke(env, "changeOrder", newDoc);
		}
		return outputChangeOrderDoc;
	}

	private Hashtable<String, Document> formInputxmlForCreatingChainedOrderForLineA(
			YFSEnvironment env, Element inputElement,
			String customerOrderHeaderKey, Element inputOrderLineELement,
			String webLineNumber, String webLine) throws Exception,
			RemoteException {
		log.debug("---> formInputxmlForCreatingChainedOrderForLineA()");
		// String lineType = inputOrderLineELement.getAttribute("LineType");
		// form the input xml
		Document inputChangeCustomerOrder = YFCDocument.createDocument("Order")
				.getDocument();
		Element inputChangeCustomerOrderElement = inputChangeCustomerOrder
				.getDocumentElement();
		// Element orderLinesElement =
		// inputChangeCustomerOrder.createElement("OrderLines");
		// inputChangeCustomerOrderElement.appendChild(orderLinesElement);
		Element orderLineElement = inputChangeCustomerOrder
				.createElement("OrderLine");
		// orderLineElement.setAttribute("LineType", lineType);
		log.debug("customerOrderDetails.get(0)" + customerOrderDetails.get(0));
		orderLineElement.setAttribute("ChainedFromOrderHeaderKey",
				customerOrderDetails.get(0));
		inputChangeCustomerOrderElement.appendChild(orderLineElement);
		log.debug("inputChangeCustomerOrderElement"
				+ SCXmlUtil.getString(inputChangeCustomerOrderElement));
		Document templateDoc = setTemplate(env);
		env.setApiTemplate("getOrderList", templateDoc);
		// get the list
		Document outOrderListDocument = api.invoke(env, "getOrderList",
				inputChangeCustomerOrder);
		env.clearApiTemplate("getOrderList");
		log.debug("outOrderListDocument"
				+ SCXmlUtil.getString(outOrderListDocument));
		// Element outOrderListElement =
		// outOrderListDocument.getDocumentElement();
		// String records = outOrderListElement.getAttribute("TotalOrderList");
		String orderHeaderKey = customerOrderDetails.get(0);
		htChangeOrder = invokeAddOrderLineInFulfillmentOrder(env, inputElement,
				orderHeaderKey, inputOrderLineELement, outOrderListDocument,
				webLineNumber, webLine);
		log.debug("htCustomerChangeOrder" + htChangeOrder.size());

		return htChangeOrder;
	}

	private void invokeOnProcessCodeS(YFSEnvironment env, Element inputElement,
			ArrayList<String> fulfillmentOrderDetails) throws RemoteException {
		String eSatus;
		String newStatus = "";
		Document inputChangeStatusDoc = null;
		// NodeList statusList =
		// inputElement.getElementsByTagName("OrderStatus");
		// if(statusList.getLength()>0)
		// {
		// Element statusElement = (Element)statusList.item(0);
		// log.debug("statusElement"+SCXmlUtil.getString(statusElement));
		// newStatus = statusElement.getAttribute("Status");
		// }
		/*
		 * newStatus = SCXmlUtil.getXpathAttribute(inputElement,
		 * "./Extn/@ExtnHeaderStatusCode");
		 */
		newStatus = inputElement.getAttribute("OrderStatus");

		log.debug("newStatus" + newStatus);

		log.debug("fulfillmentOrderDetails.get(0)"
				+ fulfillmentOrderDetails.get(0));
		Document outputListDoc = getSterlingOrderList(env,
				fulfillmentOrderDetails.get(0));
		Element outputListElement = outputListDoc.getDocumentElement();
		log.debug("outputListElement" + SCXmlUtil.getString(outputListElement));
		NodeList statusOrderLinesNodeList = outputListElement
				.getElementsByTagName("OrderLines");
		int statusLinesLength = statusOrderLinesNodeList.getLength();
		if (statusLinesLength != 0) {
			Element statusOrderLinesElement = (Element) statusOrderLinesNodeList
					.item(0);
			NodeList stausOrderLineNodeList = statusOrderLinesElement
					.getElementsByTagName("OrderLine");
			int statusOrderLineLength = stausOrderLineNodeList.getLength();
			if (statusOrderLineLength != 0) {
				for (int sCounter = 0; sCounter < statusOrderLineLength; sCounter++) {
					Element statusOrderLineElement = (Element) stausOrderLineNodeList
							.item(0);
					eSatus = SCXmlUtil.getXpathAttribute(
							statusOrderLineElement,
							"./OrderStatuses/OrderStatus/@Status");
					log.debug("eSatus" + eSatus);
					if (newStatus.equals("3700")) {
						onStatusShipConfirm(env, statusOrderLineElement,
								fulfillmentOrderDetails);
						break;
					}

					if (!eSatus.equals(newStatus) && !newStatus.equals("")
							&& !newStatus.equals("3700")) {
						String transactionId = arg0.getProperty(newStatus);
						// form the change status doc
						inputChangeStatusDoc = YFCDocument.createDocument(
								"OrderStatusChange").getDocument();
						Element inputChangeStatusElement = inputChangeStatusDoc
								.getDocumentElement();
						inputChangeStatusElement.setAttribute("DocumentType",
								"0001");
						// inputChangeStatusElement.setAttribute("EnterpriseCode",
						// "xpedx");
						// new enterprise code change
						inputChangeStatusElement.setAttribute("EnterpriseCode",
								fulfillmentOrderDetails.get(2));
						inputChangeStatusElement.setAttribute(
								"IgnoreTransactionDependencies", "Y");
						inputChangeStatusElement.setAttribute(
								"ChangeForAllAvailableQty", "Y");
						inputChangeStatusElement.setAttribute(
								"ModificationReasonCode",
								"CHANGE_FULFILLMENT_TYPE");
						inputChangeStatusElement.setAttribute(
								"ModificationReasonText",
								"Changed Fulfillment Type");
						inputChangeStatusElement.setAttribute("OrderNo",
								fulfillmentOrderDetails.get(0));
						inputChangeStatusElement.setAttribute("TransactionId",
								transactionId);
						inputChangeStatusElement.setAttribute("BaseDropStatus",
								newStatus);
						break;
					}
				}

				if (!newStatus.equals("3700")) {
					log.debug("inputChangeStatusDoc"
							+ SCXmlUtil.getString(inputChangeStatusDoc));
					log.debug("inputChangeStatusDoc"
							+ SCXmlUtil.getString(inputChangeStatusDoc));
					api.invoke(env, "changeOrderStatus", inputChangeStatusDoc);
				}
			}
		}
	}

	private void onStatusShipConfirm(YFSEnvironment env,
			Element statusOrderLineElement,
			ArrayList<String> fulfillmentOrderDetails) throws RemoteException {
		// form ship confirm xml
		String fulfillmentOrderNo = fulfillmentOrderDetails.get(0);
		Document outputFulListDoc = getSterlingOrderList(env,
				fulfillmentOrderNo);
		NodeList listNode = outputFulListDoc.getElementsByTagName("Order");
		Element orderElement = (Element) listNode.item(0);
		// get the lines of full
		Document shipDoc = YFCDocument.createDocument("Shipment").getDocument();
		Element shipElement = shipDoc.getDocumentElement();
		// shipElement.setAttribute("Action", "CREATE");
		shipElement.setAttribute("SellerOrganizationCode", orderElement
				.getAttribute("SellerOrganizationCode"));
		shipElement.setAttribute("ShipNode", orderElement
				.getAttribute("ShipNode"));
		// shipElement.setAttribute("EnterpriseCode", "xpedx");
		// new enterprise code change
		shipElement.setAttribute("EnterpriseCode", fulfillmentOrderDetails
				.get(2));
		shipElement.setAttribute("BuyerOrganizationCode", orderElement
				.getAttribute("BuyerOrganizationCode"));
		shipElement.setAttribute("DocumentType", "0001");
		Element shipmentLinesElement = shipDoc.createElement("ShipmentLines");
		shipElement.appendChild(shipmentLinesElement);
		NodeList orderlinesList = orderElement
				.getElementsByTagName("OrderLines");
		int linesLength = orderlinesList.getLength();
		if (linesLength != 0) {
			Element linesElement = (Element) orderlinesList.item(0);
			NodeList lineList = linesElement.getElementsByTagName("OrderLine");
			int lineLength = lineList.getLength();
			for (int lCounter = 0; lCounter < lineLength; lCounter++) {
				Element lineElement = (Element) lineList.item(lCounter);
				Element shipmentLineElement = shipDoc
						.createElement("ShipmentLine");
				shipmentLineElement.setAttribute("OrderNo", fulfillmentOrderNo);
				shipmentLineElement.setAttribute("DocumentType", "0001");
				// shipmentLineElement.setAttribute("ProductClass", "Good");
				shipmentLineElement.setAttribute("ShipNode", lineElement
						.getAttribute("ShipNode"));
				shipmentLineElement.setAttribute("PrimeLineNo", lineElement
						.getAttribute("PrimeLineNo"));
				shipmentLineElement.setAttribute("SubLineNo", lineElement
						.getAttribute("SubLineNo"));
				shipmentLineElement.setAttribute("ItemID", SCXmlUtil
						.getXpathAttribute(lineElement, "./Item/@ItemID"));
				shipmentLineElement.setAttribute("UnitOfMeasure",
						SCXmlUtil.getXpathAttribute(lineElement,
								"./Item/@UnitOfMeasure"));
				shipmentLineElement.setAttribute("Quantity", lineElement
						.getAttribute("OrderedQty"));
				shipmentLinesElement.appendChild(shipmentLineElement);
			}
		}
		log.debug("shipDoc" + SCXmlUtil.getString(shipDoc));
		// api.invoke(env, "confirmShipment", shipDoc);
		api.invoke(env, "createShipment", shipDoc);
	}

	private Hashtable<String, Document> createChangeOrderXMLForCWhenHTChangeOrderisEmptyForR(
			YFSEnvironment env, String webLineNumber,
			Element inputOrderLineELement) throws RemoteException {
		Document newDoc = YFCDocument.createDocument().getDocument();
		String eWebLineNumber = "";
		// get the orderlinekey
		// form the input to get the fulfillment order
		Document inputFulfillmentOrderDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element inputFulfillmentOrderElement = inputFulfillmentOrderDoc
				.getDocumentElement();
		inputFulfillmentOrderElement.setAttribute("OrderHeaderKey",
				fulfillmentOrderDetails.get(1));
		// Document outputFulfillmentOrderDoc = api.invoke(env, "getOrderList",
		// inputFulfillmentOrderDoc);
		log.debug("fulfillmentOrderDetails.get(0)"
				+ fulfillmentOrderDetails.get(1));
		Document outputFulfillmentOrderDoc = getSterlingOrderList(env,
				fulfillmentOrderDetails.get(0));
		log.debug("outputFulfillmentOrderDoc"
				+ SCXmlUtil.getString(outputFulfillmentOrderDoc));
		NodeList fOrderList = outputFulfillmentOrderDoc
				.getElementsByTagName("Order");
		// Document outputCustomerListDoc = null;
		int oLength = fOrderList.getLength();

		if (oLength != 0) {
			Element changeOrderElement = (Element) fOrderList.item(0);
			log.debug("changeOrderElement" + changeOrderElement);
			changeOrderElement.setAttribute("Action", "MODIFY");

			newDoc.appendChild(newDoc.importNode(changeOrderElement, true));
			newDoc.renameNode(newDoc.getDocumentElement(), newDoc
					.getNamespaceURI(), "Order");

			NodeList orderLinesList = newDoc.getElementsByTagName("OrderLines");
			int orderLinesLength = orderLinesList.getLength();

			if (orderLinesLength != 0) {
				Element orderLinesElement = (Element) orderLinesList.item(0);
				log.debug("orderLinesElement"
						+ SCXmlUtil.getString(orderLinesElement));

				NodeList orderLineList = orderLinesElement
						.getElementsByTagName("OrderLine");
				int orderLineLength = orderLineList.getLength();
				if (orderLineLength != 0) {
					for (int oCount = 0; oCount < orderLineLength; oCount++) {
						Element orderLineElement = (Element) orderLineList
								.item(oCount);
						// check if the line already exists then update the line
						eWebLineNumber = SCXmlUtil.getXpathAttribute(
								orderLineElement, "./Extn/@ExtnWebLineNumber")
								+ "|"
								+ SCXmlUtil.getXpathAttribute(orderLineElement,
										"./Extn/@ExtnLegacyLineNumber");
						if (eWebLineNumber.equals(webLineNumber)) {

							// //update the orderline
							orderLinesElement.removeChild(orderLineElement);

							Node inputOrderLineNode = newDoc.importNode(
									inputOrderLineELement, true);
							Element inputOrderLineNodeElement = (Element) inputOrderLineNode;
							inputOrderLineNodeElement.setAttribute("Action",
									"MODIFY");
							inputOrderLineNodeElement.setAttribute(
									"OrderedQty", "0");
							orderLinesElement
									.appendChild(inputOrderLineNodeElement);
						}

					}
				}
			}
			log.debug("the change order doc looks like"
					+ SCXmlUtil.getString(newDoc));
			// htChangeOrder.put(customerOrderDetails.get(0), newDoc);
			htChangeOrder.put(fulfillmentOrderDetails.get(1), newDoc);
		}
		return htChangeOrder;
	}

	private Hashtable<String, Document> updateChangeOrderXMLInHTChangeOrderForR(

	Element inputChainedOrderLineElement, Document changeFulfillmentOrderDoc,
			String webLineNumber, Element inputOrderLineELement)
			throws Exception {
		String eWebLineNumber = "";
		changeFulfillmentOrderDoc = htChangeOrder.get(fulfillmentOrderDetails
				.get(1));
		log.debug("changeFulfillmentOrderDoc"
				+ SCXmlUtil.getString(changeFulfillmentOrderDoc));
		NodeList orderLinesList = changeFulfillmentOrderDoc
				.getElementsByTagName("OrderLines");
		int orderLinesLength = orderLinesList.getLength();
		if (orderLinesLength != 0) {
			Element orderLinesElement = (Element) orderLinesList.item(0);
			log.debug("orderLinesElement"
					+ SCXmlUtil.getString(orderLinesElement));

			NodeList orderLineList = orderLinesElement
					.getElementsByTagName("OrderLine");
			int orderLineLength = orderLineList.getLength();
			log.debug("orderLineLength" + orderLineLength);
			if (orderLineLength != 0) {
				for (int oCount = 0; oCount < orderLineLength; oCount++) {
					Element orderLineElement = (Element) orderLineList
							.item(oCount);
					log.debug("orderLineElement"
							+ SCXmlUtil.getString(orderLineElement));
					// check if the line already exists then update the line
					eWebLineNumber = SCXmlUtil.getXpathAttribute(
							orderLineElement, "./Extn/@ExtnWebLineNumber")
							+ "|"
							+ SCXmlUtil.getXpathAttribute(orderLineElement,
									"./Extn/@ExtnLegacyLineNumber");
					if (eWebLineNumber.equals(webLineNumber)) {
						// update the orderline
						String primeLine = orderLineElement
								.getAttribute("PrimeLineNo");
						String subLine = orderLineElement
								.getAttribute("SubLineNo");

						orderLinesElement.removeChild(orderLineElement);

						Element orderLineElement1 = changeFulfillmentOrderDoc
								.createElement("OrderLine");
						orderLineElement1.setAttribute("Action", "MODIFY");
						orderLineElement1.setAttribute("OrderedQty", "0");
						orderLineElement1
								.setAttribute("PrimeLineNo", primeLine);
						orderLineElement1.setAttribute("SubLineNo", subLine);
						Node inputNode = changeFulfillmentOrderDoc.importNode(
								orderLineElement1, true);
						orderLinesElement.appendChild(inputNode);
						log.debug("changeFulfillmentOrderDoc"
								+ SCXmlUtil
										.getString(changeFulfillmentOrderDoc));
					}
				}
			}
		}
		log.debug("changeFulfillmentOrderDoc"
				+ SCXmlUtil.getString(changeFulfillmentOrderDoc));
		htChangeOrder.put(fulfillmentOrderDetails.get(1),
				changeFulfillmentOrderDoc);

		return htChangeOrder;
	}

	private Hashtable<String, Document> invokeAddOrderLineInFulfillmentOrder(
			YFSEnvironment env, Element inputElement,
			String customerOrderHeaderKey, Element inputOrderLineELement,
			Document outOrderListDocument, String webLineNumber, String webLine)
			throws Exception {
		// append
		// get the prime line number and chained from orderline key
		ArrayList<String> primeArrayList = new ArrayList<String>();
		String primeLineNo = "";
		String chainedOrderLineKey = "";
		String chainedOrderHeaderKey = "";
		primeArrayList = getPrimeLineNumber(env, customerOrderHeaderKey,
				webLine);
		primeLineNo = primeArrayList.get(0);
		chainedOrderLineKey = primeArrayList.get(1);
		chainedOrderHeaderKey = primeArrayList.get(2);

		log.debug("fulfillmentDetails.get(1)" + fulfillmentOrderDetails.get(1));
		Document changeOrderDoc = htChangeOrder.get(fulfillmentOrderDetails
				.get(1));
		if (changeOrderDoc != null) {
			NodeList orderLinesList = changeOrderDoc
					.getElementsByTagName("OrderLines");
			int orderLinesLength = orderLinesList.getLength();
			if (orderLinesLength != 0) {

				inputOrderLineELement.removeAttribute("PrimeLineNo");
				log.debug("inputOrderLineELement"
						+ SCXmlUtil.getString(inputOrderLineELement));
				inputOrderLineELement.setAttribute("Action", "CREATE");
				// code for comment line
				String lineType = inputOrderLineELement
						.getAttribute("LineType");
				// if(lineType.equalsIgnoreCase("C"))
				if (!lineType.equalsIgnoreCase("S")
						&& !lineType.equalsIgnoreCase("P")) {
					// inputOrderLineELement.setAttribute("OrderedQty", "1");
					inputOrderLineELement.setAttribute("ValidateItem", "N");
				}
				// customer item stamping
				/***************************************************************
				 * Modified by Arun Sekhar on 15th Feb 2011. Fix for the bug 948
				 **************************************************************/
				/* if (lineType.equalsIgnoreCase("C")) { */
				if (lineType.equalsIgnoreCase("C")
						|| lineType.equalsIgnoreCase("M")) {
					inputOrderLineELement.setAttribute("OrderedQty", "1");
				}
				if (lineType.equalsIgnoreCase("S")) {
					String customerPartNo = SCXmlUtil.getXpathAttribute(
							inputElement, "./Item/@CustomerItem");
					String envtId = SCXmlUtil.getXpathAttribute(inputElement,
							"./Extn/@ExtnEnvtId");
					String companyCode = SCXmlUtil.getXpathAttribute(
							inputElement, "./Extn/@ExtnCompanyId");
					String legacyCustomerNumber = SCXmlUtil.getXpathAttribute(
							inputElement, "./Extn/@ExtnCustomerNo");
					String itemId = getLegacyItemNumberFromXref(env,
							customerPartNo, envtId, companyCode,
							legacyCustomerNumber);
					// inputOrderLineELement.setAttribute("ItemID", itemId);
					Element itemElement = (Element) inputOrderLineELement
							.getElementsByTagName("Item").item(0);
					if (itemElement != null) {
						itemElement.setAttribute("ItemID", itemId);
					}
				}
				inputOrderLineELement.setAttribute("ChainedFromOrderLineKey",
						chainedOrderLineKey);
				inputOrderLineELement.setAttribute("ChainedFromOrderHeaderKey",
						chainedOrderHeaderKey);

				// check for price
				NodeList linePriceNodeList = inputOrderLineELement
						.getElementsByTagName("LinePriceInfo");
				int priceLength = linePriceNodeList.getLength();
				if (priceLength != 0) {
					Element linePriceElement = (Element) linePriceNodeList
							.item(0);
					if (linePriceElement.hasAttribute("UnitPrice")
							&& !YFCCommon.isVoid(linePriceElement
									.getAttribute("UnitPrice"))) {
						linePriceElement.setAttribute("IsPriceLocked", "Y");
					}
				}

				Element orderLinesCustomerElement = (Element) orderLinesList
						.item(0);

				Node inputNode = changeOrderDoc.importNode(
						inputOrderLineELement, true);
				orderLinesCustomerElement.appendChild(inputNode);
				Element chainedFromElement = changeOrderDoc
						.createElement("ChainedFrom");
				chainedFromElement.setAttribute("DocumentType", "0001");
				// chainedFromElement.setAttribute("EnterpriseCode", "xpedx");
				// new change for enterprise code
				chainedFromElement.setAttribute("EnterpriseCode",
						customerOrderDetails.get(2));
				chainedFromElement.setAttribute("OrderHeaderKey",
						customerOrderHeaderKey);
				chainedFromElement.setAttribute("PrimeLineNo", primeLineNo);
				chainedFromElement.setAttribute("SubLineNo", "1");
				inputNode.appendChild(chainedFromElement);
			}
			htChangeOrder.put(fulfillmentOrderDetails.get(1), changeOrderDoc);
			log.debug("orderElement" + SCXmlUtil.getString(changeOrderDoc));
		} else {
			// changeOrderDoc = null;

			Document outputFulfillmentOrderDoc = getSterlingOrderList(env,
					fulfillmentOrderDetails.get(0));
			// NodeList fOrderList =
			// outputFulfillmentOrderDoc.getElementsByTagName("Order");
			// NodeList orderList =
			// outOrderListDocument.getElementsByTagName("Order");
			NodeList orderList = outputFulfillmentOrderDoc
					.getElementsByTagName("Order");
			int orderLength = orderList.getLength();
			if (orderLength != 0) {
				Element orderElement = (Element) orderList.item(0);

				setOrderHeaderLevelAttributes(inputElement, orderElement);
				changeOrderDoc = YFCDocument.createDocument().getDocument();
				changeOrderDoc.appendChild(changeOrderDoc.importNode(
						orderElement, true));
				changeOrderDoc.renameNode(changeOrderDoc.getDocumentElement(),
						changeOrderDoc.getNamespaceURI(), "Order");
				Element newInputElement = changeOrderDoc.getDocumentElement();
				newInputElement.setAttribute("Action", "MODIFY");
				// newInputElement.setAttribute("Override", "Y");
				NodeList orderLinesList = newInputElement
						.getElementsByTagName("OrderLines");
				int orderLinesLength = orderLinesList.getLength();
				if (orderLinesLength != 0) {

					inputOrderLineELement.setAttribute("Action", "CREATE");

					// check for price
					NodeList linePriceNodeList = inputOrderLineELement
							.getElementsByTagName("LinePriceInfo");
					int priceLength = linePriceNodeList.getLength();
					if (priceLength != 0) {
						Element linePriceElement = (Element) linePriceNodeList
								.item(0);
						if (linePriceElement.hasAttribute("UnitPrice")
								&& !YFCCommon.isVoid(linePriceElement
										.getAttribute("UnitPrice"))) {
							linePriceElement.setAttribute("IsPriceLocked", "Y");
						}
					}

					Element orderLinesCustomerElement = (Element) orderLinesList
							.item(0);
					log.debug("orderLinesCustomerElement"
							+ SCXmlUtil.getString(orderLinesCustomerElement));
					Node inputNode = changeOrderDoc.importNode(
							inputOrderLineELement, true);
					orderLinesCustomerElement.appendChild(inputNode);
					Element chainedFromElement = changeOrderDoc
							.createElement("ChainedFrom");
					chainedFromElement.setAttribute("DocumentType", "0001");
					// chainedFromElement.setAttribute("EnterpriseCode",
					// "xpedx");
					// new change for enterprise code
					chainedFromElement.setAttribute("EnterpriseCode",
							customerOrderDetails.get(2));
					chainedFromElement.setAttribute("OrderHeaderKey",
							customerOrderHeaderKey);
					// chainedFromElement.setAttribute("PrimeLineNo",
					// inputOrderLineELement.getAttribute("PrimeLineNo"));
					chainedFromElement.setAttribute("PrimeLineNo", primeLineNo);
					chainedFromElement.setAttribute("SubLineNo", "1");
					inputNode.appendChild(chainedFromElement);

				}
				htChangeOrder.put(fulfillmentOrderDetails.get(1),
						changeOrderDoc);
				log.debug("orderElement" + SCXmlUtil.getString(changeOrderDoc));
			}
		}
		return htChangeOrder;
		// Document changeDocument = api.invoke(env, "changeOrder",
		// outOrderListDocument);
	}

	private ArrayList<String> getPrimeLineNumberFromCustomerOrder(
			YFSEnvironment env, String customerOrderHeaderKey,
			String webLineNumber) throws Exception, RemoteException {
		log.debug("---> getPrimeLineNumberFromCustomerOrder()");
		log.debug("webLineNumber" + webLineNumber);
		ArrayList<String> primeArrayList = new ArrayList<String>();
		String primeLineNo = "";
		String chainedOrderLineKey = "";
		String chaninedFromOrderHeaderKey = "";
		String lineTotal = "";
		String quantity = "";
		// String webLine = "";
		// form the input
		Document inputPrimeDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element inputPrimeElement = inputPrimeDoc.getDocumentElement();
		inputPrimeElement
				.setAttribute("OrderHeaderKey", customerOrderHeaderKey);
		Document templateDoc = setTemplate(env);
		env.setApiTemplate("getOrderList", templateDoc);
		log.debug("inputPrimeDoc" + SCXmlUtil.getString(inputPrimeDoc));
		Document outputListDoc = api.invoke(env, "getOrderList", inputPrimeDoc);
		log.debug("outputListDoc" + SCXmlUtil.getString(outputListDoc));
		NodeList orderList1 = outputListDoc.getElementsByTagName("Order");
		Element orderElement1 = (Element) orderList1.item(0);
		NodeList orderLinesList1 = orderElement1
				.getElementsByTagName("OrderLines");
		int linesLength = orderLinesList1.getLength();
		if (linesLength != 0) {
			Element orderLinesElement1 = (Element) orderLinesList1.item(0);
			NodeList orderLineList1 = orderLinesElement1
					.getElementsByTagName("OrderLine");
			int lineLength1 = orderLineList1.getLength();
			if (lineLength1 != 0) {
				for (int lineCounter = 0; lineCounter < lineLength1; lineCounter++) {
					Element orderLineEement1 = (Element) orderLineList1
							.item(lineCounter);
					// String eWebLineNUmber = getWebLineKey(env,
					// orderLineEement1);
					String eWebLineNUmber = SCXmlUtil.getXpathAttribute(
							orderLineEement1, "./Extn/@ExtnWebLineNumber");
					if (eWebLineNUmber.equals(webLineNumber)) {
						primeLineNo = orderLineEement1
								.getAttribute("PrimeLineNo");
						chainedOrderLineKey = orderLineEement1
								.getAttribute("OrderLineKey");
						lineTotal = SCXmlUtil.getXpathAttribute(
								orderLineEement1, "./LinePriceInfo/@LineTotal");
						quantity = orderLineEement1.getAttribute("OrderedQty");
						// newly added
						// webLine = getWebLineKey(env, orderLineEement1);
						// webLine =
						// SCXmlUtil.getXpathAttribute(orderLineEement1,
						// "./Extn/@ExtnWebLineNumber");
						// chaninedFromOrderHeaderKey =
						// orderLineEement1.getAttribute("ChainedFromOrderHeaderKey");
						primeArrayList.add(primeLineNo);
						primeArrayList.add(chainedOrderLineKey);
						primeArrayList.add(chaninedFromOrderHeaderKey);
						primeArrayList.add(lineTotal);
						primeArrayList.add(quantity);
						primeArrayList.add(eWebLineNUmber);

						Element extnElement = (Element) orderLineEement1
								.getElementsByTagName("Extn").item(0);
						if (extnElement != null) {
							primeArrayList.add(extnElement
									.getAttribute("ExtnLineType"));
						}
					}
				}
			}
		}
		log.debug("<--- getPrimeLineNumberFromCustomerOrder()");
		return primeArrayList;
	}

	private ArrayList<String> getPrimeLineNumber(YFSEnvironment env,
			String customerOrderHeaderKey, String webLineNumber)
			throws Exception, RemoteException {
		ArrayList<String> primeArrayList = new ArrayList<String>();
		String primeLineNo = "";
		String chainedOrderLineKey = "";
		String chaninedFromOrderHeaderKey = "";
		// form the input
		Document inputPrimeDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element inputPrimeElement = inputPrimeDoc.getDocumentElement();
		inputPrimeElement
				.setAttribute("OrderHeaderKey", customerOrderHeaderKey);
		Document templateDoc = setTemplate(env);
		env.setApiTemplate("getOrderList", templateDoc);

		Document outputListDoc = api.invoke(env, "getOrderList", inputPrimeDoc);
		log.debug("outputListDoc" + SCXmlUtil.getString(outputListDoc));
		NodeList orderList1 = outputListDoc.getElementsByTagName("Order");
		Element orderElement1 = (Element) orderList1.item(0);
		NodeList orderLinesList1 = orderElement1
				.getElementsByTagName("OrderLines");
		int linesLength = orderLinesList1.getLength();
		if (linesLength != 0) {
			Element orderLinesElement1 = (Element) orderLinesList1.item(0);
			NodeList orderLineList1 = orderLinesElement1
					.getElementsByTagName("OrderLine");
			int lineLength1 = orderLineList1.getLength();
			if (lineLength1 != 0) {
				for (int lineCounter = 0; lineCounter < lineLength1; lineCounter++) {
					Element orderLineEement1 = (Element) orderLineList1
							.item(lineCounter);
					String eWebLineNUmber = SCXmlUtil.getXpathAttribute(
							orderLineEement1, "./Extn/@ExtnWebLineNumber");
					if (eWebLineNUmber.equals(webLineNumber)) {
						primeLineNo = orderLineEement1
								.getAttribute("PrimeLineNo");
						chainedOrderLineKey = orderLineEement1
								.getAttribute("ChainedFromOrderLineKey");
						chaninedFromOrderHeaderKey = orderLineEement1
								.getAttribute("ChainedFromOrderHeaderKey");
					}
				}
			}

		}
		primeArrayList.add(primeLineNo);
		primeArrayList.add(chainedOrderLineKey);
		primeArrayList.add(chaninedFromOrderHeaderKey);

		return primeArrayList;
	}

	private boolean invokeOnStatusChange(YFSEnvironment env,
			Element inputElement, ArrayList<String> fulfillmentOrderDetails,
			boolean bStatusUpdateFlag) throws RemoteException {
		log.debug("---> invokeOnStatusChange()");
		String inputStatus = "";
		int flag = 0;
		// get the input status code
		NodeList inputStatusNodeList = inputElement
				.getElementsByTagName("OrderStatus");
		int statusNodeLength = inputStatusNodeList.getLength();
		if (statusNodeLength != 0) {
			Element statusElement = (Element) inputStatusNodeList.item(0);
			inputStatus = statusElement.getAttribute("Status");

		}
		if (!inputStatus.equals("")) {
			// loop thru the fulfillment order for staus change check
			// form the input doc
			Document outputListDocument = getSterlingOrderList(env,
					fulfillmentOrderDetails.get(0));
			Element outputListElement = outputListDocument.getDocumentElement();
			NodeList orderLinesNodeList = outputListElement
					.getElementsByTagName("OrderLines");
			int orderLinesLength = orderLinesNodeList.getLength();
			if (orderLinesLength != 0) {
				Element orderLinesElement = (Element) orderLinesNodeList
						.item(0);
				NodeList orderLineList = orderLinesElement
						.getElementsByTagName("OrderLine");
				int orderLineLength = orderLineList.getLength();
				if (orderLineLength != 0) {
					for (int oCounter = 0; oCounter < orderLineLength; oCounter++) {
						Element orderLineElement = (Element) orderLineList
								.item(oCounter);
						String orderLineStatus = SCXmlUtil.getXpathAttribute(
								orderLineElement,
								"./OrderStatuses/OrderStatus/@Status");
						log.debug("orderLineStatus: " + orderLineStatus);
						if (!inputStatus.equals(orderLineStatus)) {
							flag = 1;
							break;
						}
					}
				}
			}
		}

		if (flag == 1) {
			bStatusUpdateFlag = true;
		}
		log.debug("<--- invokeOnStatusChange()");
		return bStatusUpdateFlag;
	}

	private Hashtable<String, Document> invokeCustomerChangeOrderStatus(
			YFSEnvironment env, Document inputOrderStatusChangeDoc,
			String customerOrderNo, String webLineNumber,
			Document changeOrderStatusDoc, Element inputOrderLineELement)
			throws Exception {
		log.debug("htCurrentOrder" + htCurrentOrder.size());

		inputOrderStatusChangeDoc = htCustomerChangeOrderStatus
				.get(customerOrderNo);
		if (inputOrderStatusChangeDoc != null) {

			// append it
			NodeList changeOrderNodeList = inputOrderStatusChangeDoc
					.getElementsByTagName("OrderLines");
			int orderLinesLength = changeOrderNodeList.getLength();
			if (orderLinesLength != 0) {
				String qty = inputOrderLineELement.getAttribute("OrderedQty");

				Element orderLinesElement = (Element) changeOrderNodeList
						.item(0);
				log.debug("processCode"
						+ inputOrderLineELement.getAttribute("ProcessCode"));
				Document orderLineDoc = invokeMethodForChangeOrderStatus(env,
						htCurrentOrder, htCustomerOrder,
						htCustomerChangeOrderStatus, orderLinesElement,
						webLineNumber, inputOrderLineELement, customerOrderNo);
				Element orderLineElement = orderLineDoc.getDocumentElement();
				Node orderLineNode = inputOrderStatusChangeDoc.importNode(
						orderLineElement, true);
				orderLinesElement.appendChild(orderLineNode);
			}

		} else {

			// create it
			// create the orderstatus change xml
			inputOrderStatusChangeDoc = YFCDocument.createDocument(
					"OrderStatusChange").getDocument();
			Element inputOrderStatusChangeElement = inputOrderStatusChangeDoc
					.getDocumentElement();
			inputOrderStatusChangeElement.setAttribute("DocumentType", "0001");
			// inputOrderStatusChangeElement.setAttribute("EnterpriseCode",
			// "xpedx");
			// new change for eneterprisecode
			inputOrderStatusChangeElement.setAttribute("EnterpriseCode",
					customerOrderDetails.get(2));
			inputOrderStatusChangeElement.setAttribute(
					"IgnoreTransactionDependencies", "Y");
			inputOrderStatusChangeElement.setAttribute(
					"ModificationReasonCode", "CHANGE_FULFILLMENT_TYPE");
			inputOrderStatusChangeElement.setAttribute(
					"ModificationReasonText", "Changed Fulfillment Type");
			inputOrderStatusChangeElement.setAttribute("OrderNo",
					customerOrderNo);
			// inputOrderStatusChangeElement.setAttribute("TransactionId",
			// "xpedxUPDATE_ORD_QTY.0001.ex");
			inputOrderStatusChangeElement.setAttribute("TransactionId",
					"XPX_UPDT_CUSTOMER_ORD.0001.ex");
			Element orderLinesElement = inputOrderStatusChangeDoc
					.createElement("OrderLines");
			inputOrderStatusChangeElement.appendChild(orderLinesElement);
			log.debug("processCode"
					+ inputOrderLineELement.getAttribute("ProcessCode"));
			String processCode = inputOrderLineELement
					.getAttribute("ProcessCode");
			Document orderLineDoc = null;
			if (processCode.equals("D")) {
				orderLineDoc = invokeMethodForChangeOrderStatusForD(env,
						htCurrentOrder, htCustomerOrder,
						htCustomerChangeOrderStatus, orderLinesElement,
						webLineNumber, inputOrderLineELement, customerOrderNo);
			} else {
				orderLineDoc = invokeMethodForChangeOrderStatus(env,
						htCurrentOrder, htCustomerOrder,
						htCustomerChangeOrderStatus, orderLinesElement,
						webLineNumber, inputOrderLineELement, customerOrderNo);
			}
			Element orderLineElement = orderLineDoc.getDocumentElement();
			Node orderLineNode = inputOrderStatusChangeDoc.importNode(
					orderLineElement, true);
			orderLinesElement.appendChild(orderLineNode);
			htCustomerChangeOrderStatus.put(customerOrderNo,
					inputOrderStatusChangeDoc);
		}
		return htCustomerChangeOrderStatus;
	}

	private Document invokeMethodForChangeOrderStatus(YFSEnvironment env,
			Hashtable<String, Element> htCurrentOrder,
			Hashtable<String, Element> htCustomerOrder,
			Hashtable<String, Document> htCustomerChangeOrderStatus,
			Element inputOrderLinesELement, String webLineNumber,
			Element inputOrderLineELement, String customerOrderNo)
			throws Exception {

		log.debug("---> invokeMethodForChangeOrderStatus()");
		Document orderLineStatusChangeDoc = YFCDocument.createDocument(
				"OrderLine").getDocument();
		log.debug("webLineNumber" + webLineNumber);
		Element inputChainedOrderLineElement = htCurrentOrder
				.get(webLineNumber);
		log.debug("inputChainedOrderLineElement"
				+ SCXmlUtil.getString(inputChainedOrderLineElement));
		String primeLineNo = inputChainedOrderLineElement
				.getAttribute("PrimeLineNo");
		log.debug("primeLineNo" + primeLineNo);
		// quantity change logic
		// compare the quantity on input line and chained order line
		Float iQty = Float.parseFloat(inputOrderLineELement
				.getAttribute("OrderedQty"));
		log.debug("iQty" + iQty);

		Float eQty = Float.parseFloat(inputChainedOrderLineElement
				.getAttribute("OrderedQty"));
		log.debug("eQty" + eQty);

		if (iQty > eQty) {
			String[] webArray = webLineNumber.split("\\|");
			webLineNumber = webArray[0];
			primeLineNo = getPrimeLineNumberForStatusChangeDoc(env,
					customerOrderNo, webLineNumber);
			Float dQty = iQty - eQty;
			Element orderLineStatusChangeElement = orderLineStatusChangeDoc
					.getDocumentElement();
			orderLineStatusChangeElement.setAttribute("BaseDropStatus",
					"1100.0100");
			orderLineStatusChangeElement.setAttribute("PrimeLineNo",
					primeLineNo);
			orderLineStatusChangeElement.setAttribute("SubLineNo", "1");
			Element orderLineTranQtyElement = orderLineStatusChangeDoc
					.createElement("OrderLineTranQuantity");
			orderLineTranQtyElement.setAttribute("Quantity", dQty.toString());
			orderLineStatusChangeElement.appendChild(orderLineTranQtyElement);
		}
		if ((iQty < eQty) && (iQty != 0)) {

			String[] webArray = webLineNumber.split("\\|");
			webLineNumber = webArray[0];
			primeLineNo = getPrimeLineNumberForStatusChangeDoc(env,
					customerOrderNo, webLineNumber);
			Float dQty = eQty - iQty;
			Element orderLineStatusChangeElement = orderLineStatusChangeDoc
					.getDocumentElement();
			orderLineStatusChangeElement.setAttribute("BaseDropStatus",
					"1100.0100");
			orderLineStatusChangeElement.setAttribute("PrimeLineNo",
					primeLineNo);
			orderLineStatusChangeElement.setAttribute("SubLineNo", "1");
			Element orderLineTranQtyElement = orderLineStatusChangeDoc
					.createElement("OrderLineTranQuantity");
			orderLineTranQtyElement.setAttribute("Quantity", dQty.toString());
			orderLineStatusChangeElement.appendChild(orderLineTranQtyElement);
		}
		if (iQty == 0) {
			String[] webArray = webLineNumber.split("\\|");
			webLineNumber = webArray[0];
			primeLineNo = getPrimeLineNumberForStatusChangeDoc(env,
					customerOrderNo, webLineNumber);
			Element orderLineStatusChangeElement = orderLineStatusChangeDoc
					.getDocumentElement();
			orderLineStatusChangeElement.setAttribute("BaseDropStatus", "1100");
			orderLineStatusChangeElement.setAttribute("PrimeLineNo",
					primeLineNo);
			orderLineStatusChangeElement.setAttribute("SubLineNo", "1");
			Element orderLineTranQtyElement = orderLineStatusChangeDoc
					.createElement("OrderLineTranQuantity");
			orderLineTranQtyElement.setAttribute("Quantity", eQty.toString());
			orderLineStatusChangeElement.appendChild(orderLineTranQtyElement);
		}
		return orderLineStatusChangeDoc;
	}

	private Document invokeMethodForChangeOrderStatusForD(YFSEnvironment env,
			Hashtable<String, Element> htCurrentOrder,
			Hashtable<String, Element> htCustomerOrder,
			Hashtable<String, Document> htCustomerChangeOrderStatus,
			Element inputOrderLinesELement, String webLineNumber,
			Element inputOrderLineELement, String customerOrderNo)
			throws Exception {

		Document orderLineStatusChangeDoc = YFCDocument.createDocument(
				"OrderLine").getDocument();
		log.debug("webLineNumber" + webLineNumber);
		Element inputChainedOrderLineElement = htCurrentOrder
				.get(webLineNumber);
		log.debug("inputChainedOrderLineElement"
				+ SCXmlUtil.getString(inputChainedOrderLineElement));
		String primeLineNo = inputChainedOrderLineElement
				.getAttribute("PrimeLineNo");
		log.debug("primeLineNo" + primeLineNo);
		// quantity change logic
		// compare the quantity on input line and chained order line
		Float iQty = Float.parseFloat(inputOrderLineELement
				.getAttribute("OrderedQty"));
		log.debug("iQty" + iQty);

		Float eQty = Float.parseFloat(inputChainedOrderLineElement
				.getAttribute("OrderedQty"));
		log.debug("eQty" + eQty);

		if (iQty > eQty) {
			String[] webArray = webLineNumber.split("\\|");
			webLineNumber = webArray[0];
			primeLineNo = getPrimeLineNumberForStatusChangeDoc(env,
					customerOrderNo, webLineNumber);
			Float dQty = iQty - eQty;
			Element orderLineStatusChangeElement = orderLineStatusChangeDoc
					.getDocumentElement();
			orderLineStatusChangeElement.setAttribute("BaseDropStatus",
					"1100.0100");
			orderLineStatusChangeElement.setAttribute("PrimeLineNo",
					primeLineNo);
			orderLineStatusChangeElement.setAttribute("SubLineNo", "1");
			Element orderLineTranQtyElement = orderLineStatusChangeDoc
					.createElement("OrderLineTranQuantity");
			orderLineTranQtyElement.setAttribute("Quantity", dQty.toString());
			orderLineStatusChangeElement.appendChild(orderLineTranQtyElement);
		}
		if ((iQty < eQty) && (iQty != 0)) {
			String[] webArray = webLineNumber.split("\\|");
			webLineNumber = webArray[0];
			primeLineNo = getPrimeLineNumberForStatusChangeDoc(env,
					customerOrderNo, webLineNumber);
			// Float dQty = eQty - iQty;
			Element orderLineStatusChangeElement = orderLineStatusChangeDoc
					.getDocumentElement();
			orderLineStatusChangeElement.setAttribute("BaseDropStatus",
					"1100.0100");
			orderLineStatusChangeElement.setAttribute("PrimeLineNo",
					primeLineNo);
			orderLineStatusChangeElement.setAttribute("SubLineNo", "1");
			Element orderLineTranQtyElement = orderLineStatusChangeDoc
					.createElement("OrderLineTranQuantity");
			orderLineTranQtyElement.setAttribute("Quantity", iQty.toString());
			orderLineStatusChangeElement.appendChild(orderLineTranQtyElement);
		}
		if (iQty == 0) {
			String[] webArray = webLineNumber.split("\\|");
			webLineNumber = webArray[0];
			primeLineNo = getPrimeLineNumberForStatusChangeDoc(env,
					customerOrderNo, webLineNumber);
			Element orderLineStatusChangeElement = orderLineStatusChangeDoc
					.getDocumentElement();
			orderLineStatusChangeElement.setAttribute("BaseDropStatus", "1100");
			orderLineStatusChangeElement.setAttribute("PrimeLineNo",
					primeLineNo);
			orderLineStatusChangeElement.setAttribute("SubLineNo", "1");
			Element orderLineTranQtyElement = orderLineStatusChangeDoc
					.createElement("OrderLineTranQuantity");
			orderLineTranQtyElement.setAttribute("Quantity", eQty.toString());
			orderLineStatusChangeElement.appendChild(orderLineTranQtyElement);
		}
		return orderLineStatusChangeDoc;
	}

	private String getPrimeLineNumberForStatusChangeDoc(YFSEnvironment env,
			String customerOrderNo, String webLineNumber) throws Exception,
			RemoteException {

		String primeLineNo = "";

		// form the input
		Document inputPrimeDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element inputPrimeElement = inputPrimeDoc.getDocumentElement();
		inputPrimeElement.setAttribute("OrderNo", customerOrderNo);
		Document templateDoc = setTemplate(env);
		env.setApiTemplate("getOrderList", templateDoc);
		Document outputListDoc = api.invoke(env, "getOrderList", inputPrimeDoc);
		NodeList orderList1 = outputListDoc.getElementsByTagName("Order");
		Element orderElement1 = (Element) orderList1.item(0);
		NodeList orderLinesList1 = orderElement1
				.getElementsByTagName("OrderLines");
		int linesLength = orderLinesList1.getLength();
		if (linesLength != 0) {
			Element orderLinesElement1 = (Element) orderLinesList1.item(0);
			NodeList orderLineList1 = orderLinesElement1
					.getElementsByTagName("OrderLine");
			int lineLength1 = orderLineList1.getLength();
			if (lineLength1 != 0) {
				for (int lineCounter = 0; lineCounter < lineLength1; lineCounter++) {
					Element orderLineEement1 = (Element) orderLineList1
							.item(lineCounter);
					String eWebLineNUmber = SCXmlUtil.getXpathAttribute(
							orderLineEement1, "./Extn/@ExtnWebLineNumber");
					if (eWebLineNUmber.equals(webLineNumber)) {
						primeLineNo = orderLineEement1
								.getAttribute("PrimeLineNo");
					}
				}
			}

		}
		return primeLineNo;
	}

	private Document invokeMethodForOrlineProcessCodeC(YFSEnvironment env,
			Element inputOrderLineELement, String webLineNumber,
			Element inputElement) throws Exception {
		log.debug("---> invokeMethodForOrlineProcessCodeC()");
		Document changeFulfillmentOrderDoc = null;
		log.debug("webLineNumber" + webLineNumber);
		Element inputChainedOrderLineElement = htCurrentOrder
				.get(webLineNumber);
		log.debug("inputChainedOrderLineElement: "
				+ SCXmlUtil.getString(inputChainedOrderLineElement));
		// quantity change logic
		Float iQty = 0.0f;

		/** ********************** */
		// check if the shipped quantity exists and is changed.
		boolean shipQtyExists = false;
		NodeList extnShipList = inputOrderLineELement
				.getElementsByTagName("Extn");
		int extnShipLength = extnShipList.getLength();
		if (extnShipLength > 0) {
			Element extnShipElement = (Element) extnShipList.item(0);
			Float shipQty = 0f;
			if (extnShipElement.hasAttribute("ExtnReqShipOrdQty")
					&& (shipQty != 0)) {
				shipQtyExists = true;
				shipQty = Float.parseFloat(SCXmlUtil.getXpathAttribute(
						inputOrderLineELement, "./Extn/@ExtnReqShipOrdQty"));
			}
		}
		// check if the requested shipped qty is changed
		Float sQty = 0f;
		Float esQty = 0f;
		if (shipQtyExists) {
			// get the existing shipped qty
			sQty = Float.parseFloat(SCXmlUtil.getXpathAttribute(
					inputOrderLineELement, "./Extn/@ExtnReqShipOrdQty"));
			esQty = Float.parseFloat(SCXmlUtil.getXpathAttribute(
					inputChainedOrderLineElement, "./Extn/@ExtnReqShipOrdQty"));
			if (!sQty.equals(esQty)) {
				// need to invoke the change shipment
				log.debug("change shipment invoked");

				String shipmentKey = "";
				// form ship confirm xml
				String fulfillmentOrderNo = fulfillmentOrderDetails.get(0);

				Document outputFulListDoc = getSterlingOrderList(env,
						fulfillmentOrderNo);
				NodeList listNode = outputFulListDoc
						.getElementsByTagName("Order");
				Element orderElement = (Element) listNode.item(0);

				// get the lines of full
				Document shipDoc = YFCDocument.createDocument("Shipment")
						.getDocument();
				Element shipElement = shipDoc.getDocumentElement();
				shipElement.setAttribute("Action", "Modify");
				shipElement.setAttribute("SellerOrganizationCode", orderElement
						.getAttribute("SellerOrganizationCode"));
				shipElement.setAttribute("ShipNode", orderElement
						.getAttribute("ShipNode"));
				// shipElement.setAttribute("EnterpriseCode", "xpedx");
				// new enterprise code change
				shipElement.setAttribute("EnterpriseCode",
						fulfillmentOrderDetails.get(2));
				shipElement.setAttribute("BuyerOrganizationCode", orderElement
						.getAttribute("BuyerOrganizationCode"));
				shipElement.setAttribute("DocumentType", "0001");
				Element shipmentLinesElement = shipDoc
						.createElement("ShipmentLines");
				shipElement.appendChild(shipmentLinesElement);
				NodeList orderlinesList = orderElement
						.getElementsByTagName("OrderLines");
				int linesLength = orderlinesList.getLength();
				if (linesLength != 0) {
					Element linesElement = (Element) orderlinesList.item(0);
					NodeList lineList = linesElement
							.getElementsByTagName("OrderLine");
					int lineLength = lineList.getLength();
					for (int lCounter = 0; lCounter < lineLength; lCounter++) {
						Element lineElement = (Element) lineList.item(lCounter);
						String newWeblineNumber = SCXmlUtil.getXpathAttribute(
								lineElement, "./Extn/@ExtnWebLineNumber")
								+ "|"
								+ SCXmlUtil.getXpathAttribute(lineElement,
										"./Extn/@ExtnLegacyLineNumber");
						if (webLineNumber.equals(newWeblineNumber)) {
							// get shipment key
							Document inputShipmentListDoc = YFCDocument
									.createDocument("ShipmentLine")
									.getDocument();
							Element inputShipmentListElement = inputShipmentListDoc
									.getDocumentElement();
							inputShipmentListElement.setAttribute(
									"DocumentType", "0001");
							inputShipmentListElement.setAttribute("OrderNo",
									fulfillmentOrderNo);
							inputShipmentListElement.setAttribute(
									"PrimeLineNo", lineElement
											.getAttribute("PrimeLineNo"));
							inputShipmentListElement.setAttribute("SubLineNo",
									lineElement.getAttribute("SubLineNo"));
							inputShipmentListElement.setAttribute("ShipNode",
									lineElement.getAttribute("ShipNode"));
							inputShipmentListElement.setAttribute("ItemID",
									SCXmlUtil.getXpathAttribute(lineElement,
											"./Item/@ItemID"));
							Document outputShipmentListDoc = api
									.invoke(env, "getShipmentLineList",
											inputShipmentListDoc);
							NodeList shipmentList = outputShipmentListDoc
									.getElementsByTagName("ShipmentLine");
							Element outputShipmentLineElement = (Element) shipmentList
									.item(0);
							shipmentKey = outputShipmentLineElement
									.getAttribute("ShipmentKey");

							Element shipmentLineElement = shipDoc
									.createElement("ShipmentLine");
							shipmentLineElement.setAttribute("OrderNo",
									fulfillmentOrderNo);
							shipmentLineElement.setAttribute("DocumentType",
									"0001");
							shipmentLineElement.setAttribute("ShipmentKey",
									shipmentKey);
							// shipmentLineElement.setAttribute("ProductClass",
							// "Good");
							shipmentLineElement.setAttribute("ShipNode",
									lineElement.getAttribute("ShipNode"));
							shipmentLineElement.setAttribute("PrimeLineNo",
									lineElement.getAttribute("PrimeLineNo"));
							shipmentLineElement.setAttribute("SubLineNo",
									lineElement.getAttribute("SubLineNo"));
							shipmentLineElement.setAttribute("ItemID",
									SCXmlUtil.getXpathAttribute(lineElement,
											"./Item/@ItemID"));
							shipmentLineElement.setAttribute("UnitOfMeasure",
									SCXmlUtil.getXpathAttribute(lineElement,
											"./Item/@UnitOfMeasure"));
							shipmentLineElement.setAttribute("Quantity", sQty
									.toString());
							shipmentLinesElement
									.appendChild(shipmentLineElement);
						}
					}
				}
				log.debug("changeShipment i/p doc: "
						+ SCXmlUtil.getString(shipDoc));
				shipDoc.getDocumentElement().setAttribute("ShipmentKey",
						shipmentKey);
				Document outShipdoc = api
						.invoke(env, "changeShipment", shipDoc);
				log.debug("changeShipment o/p doc: "
						+ SCXmlUtil.getString(outShipdoc));
			}
			log.debug("<--- invokeMethodForOrlineProcessCodeC()");
		}

		/** ************************ */
		// check if the input line has backordered qty
		boolean bckExists = false;
		NodeList extnList = inputOrderLineELement.getElementsByTagName("Extn");
		int extnLength = extnList.getLength();
		if (extnLength > 0) {
			Element extnElement = (Element) extnList.item(0);
			if (extnElement.hasAttribute("ExtnReqBackOrdQty")) {
				/** Commented on 19th Feb 2011 * */
				// if (!YFCCommon.isVoid(extnElement
				// .getAttribute("ExtnReqBackOrdQty"))) {
				if (!extnElement.getAttribute("ExtnReqBackOrdQty").equals("0")) {
					bckExists = true;
				}
			}
		}
		Float bQty = 0f;
		if (bckExists) {
			bQty = Float.parseFloat(SCXmlUtil.getXpathAttribute(
					inputOrderLineELement, "./Extn/@ExtnReqBackOrdQty"));
			if ((bQty != 0)) {
				// compare the quantity on input line and chained order line
				boolean ordQtyexists = false;
				if (inputOrderLineELement.hasAttribute("OrderedQty")) {
					ordQtyexists = true;
				}
				if (ordQtyexists) {
					iQty = Float.parseFloat(inputOrderLineELement
							.getAttribute("OrderedQty"))
							- bQty;
				}
				log.debug("iQty" + iQty);
				inputOrderLineELement.setAttribute("OrderedQty", iQty
						.toString());
			}
		} else {
			iQty = Float.parseFloat(inputOrderLineELement
					.getAttribute("OrderedQty"));
		}
		Float eQty = Float.parseFloat(inputChainedOrderLineElement
				.getAttribute("OrderedQty"));
		log.debug("eQty" + eQty);
		// get the webline number form order line
		// get the orderline for corresponding webline number from the
		// htCurrentOrder

		// check for line price info
		boolean priceExists = false;
		NodeList lineNode = inputOrderLineELement
				.getElementsByTagName("LinePriceInfo");
		int lineLength = lineNode.getLength();
		if (lineLength > 0) {
			Element lineElement = (Element) lineNode.item(0);
			if (lineElement.hasAttribute("UnitPrice")) {
				priceExists = true;
			}
		}
		Float iPrice = 0f;
		if (priceExists) {
			iPrice = Float.parseFloat(SCXmlUtil.getXpathAttribute(
					inputOrderLineELement, "./LinePriceInfo/@UnitPrice"));
		}
		Float ePrice = Float.parseFloat(SCXmlUtil.getXpathAttribute(
				inputChainedOrderLineElement, "./LinePriceInfo/@UnitPrice"));
		// get the existing price info
		Float eTotal = Float.parseFloat(SCXmlUtil.getXpathAttribute(
				inputChainedOrderLineElement, "./LinePriceInfo/@LineTotal"));
		if (iPrice.equals(ePrice)) {
			log.debug("there is no change in price");
		} else {
			changeFulfillmentOrderDoc = checkForPriceChange(env,
					inputOrderLineELement, webLineNumber,
					changeFulfillmentOrderDoc, iPrice, eTotal);
		}

		if (iQty > eQty) {
			String checkFlag = "Y";

			// get change order xml or fulfillment order using order header key
			// from htchangeOrder
			changeFulfillmentOrderDoc = htChangeOrder
					.get(fulfillmentOrderDetails.get(1));
			if (changeFulfillmentOrderDoc != null) {
				// update the existing change order xml
				updateChangeOrderXMLInHTChangeOrder(env,
						inputChainedOrderLineElement,
						changeFulfillmentOrderDoc, webLineNumber,
						inputOrderLineELement, checkFlag);

			} else {
				// create the change order xml including this line
				createChangeOrderXMLForCWhenHTChangeOrderisEmpty(env,
						webLineNumber, inputOrderLineELement, checkFlag,
						inputElement);
			}
		}
		if (iQty < eQty) {
			String checkFlag = "Y";
			// if the input qty is less than the existing

			changeFulfillmentOrderDoc = htChangeOrder
					.get(fulfillmentOrderDetails.get(1));
			if (changeFulfillmentOrderDoc != null) {
				// create the change order doc
				updateChangeOrderXMLInHTChangeOrder(env,
						inputChainedOrderLineElement,
						changeFulfillmentOrderDoc, webLineNumber,
						inputOrderLineELement, checkFlag);
			} else {
				// create the change order xml including this line
				createChangeOrderXMLForCWhenHTChangeOrderisEmpty(env,
						webLineNumber, inputOrderLineELement, checkFlag,
						inputElement);
			}
		}
		// else
		boolean qsame = (iQty.equals(eQty));
		log.debug("(iQty == eQty)" + qsame);
		boolean check = (!inputOrderLineELement.hasAttribute("OrderedQty") || (iQty
				.equals(eQty)));
		log.debug("check" + check);
		if (!inputOrderLineELement.hasAttribute("OrderedQty")
				|| (iQty.equals(eQty))) {
			String checkFlag = "N";

			changeFulfillmentOrderDoc = htChangeOrder
					.get(fulfillmentOrderDetails.get(1));

			if (changeFulfillmentOrderDoc != null) {
				// create the change order doc
				updateChangeOrderXMLInHTChangeOrder(env,
						inputChainedOrderLineElement,
						changeFulfillmentOrderDoc, webLineNumber,
						inputOrderLineELement, checkFlag);
			} else {
				// create the change order xml including this line
				createChangeOrderXMLForCWhenHTChangeOrderisEmpty(env,
						webLineNumber, inputOrderLineELement, checkFlag,
						inputElement);
			}
		}
		// check if there is a price change
		return changeFulfillmentOrderDoc;
	}

	private Document checkForPriceChange(YFSEnvironment env,
			Element inputOrderLineELement, String webLineNumber,
			Document changeFulfillmentOrderDoc, Float iPrice, Float eTotal)
			throws RemoteException, Exception {
		Document changeLineDoc = htChangeOrder.get(fulfillmentOrderDetails
				.get(1));
		if (changeLineDoc == null) {
			createInputForPriceChange(env, inputOrderLineELement,
					webLineNumber, iPrice, eTotal);

		}

		else {
			changeFulfillmentOrderDoc = updateXmlForPriceChange(env,
					inputOrderLineELement, webLineNumber, iPrice, eTotal);
		}
		return changeFulfillmentOrderDoc;
	}

	private Document updateXmlForPriceChange(YFSEnvironment env,
			Element inputOrderLineELement, String webLineNumber, Float iPrice,
			Float eTotal) throws Exception {
		log.debug("---> updateXmlForPriceChange()");
		Document changeFulfillmentOrderDoc;

		String eWebLineNumber = "";
		// log.debug("444444444444444444444444444");
		log.debug("inputOrderLineELement"
				+ SCXmlUtil.getString(inputOrderLineELement));
		changeFulfillmentOrderDoc = htChangeOrder.get(fulfillmentOrderDetails
				.get(1));
		NodeList orderLinesList = changeFulfillmentOrderDoc
				.getElementsByTagName("OrderLines");
		// changeFulfillmentOrderDoc.getDocumentElement().setAttribute("ChangeInQty",
		// checkFlag);
		int orderLinesLength = orderLinesList.getLength();
		if (orderLinesLength != 0) {
			Element orderLinesElement = (Element) orderLinesList.item(0);
			log.debug("orderLinesElement"
					+ SCXmlUtil.getString(orderLinesElement));

			NodeList orderLineList = orderLinesElement
					.getElementsByTagName("OrderLine");
			int orderLineLength = orderLineList.getLength();
			if (orderLineLength != 0) {
				for (int oCount = 0; oCount < orderLineLength; oCount++) {
					Element orderLineElement = (Element) orderLineList
							.item(oCount);
					// check if the line already exists then update the line
					// newly added
					eWebLineNumber = getWebLineKey(env, orderLineElement);
					String eWebLineNumber1 = SCXmlUtil.getXpathAttribute(
							orderLineElement, "./Extn/@ExtnWebLineNumber");
					if (eWebLineNumber.equals(webLineNumber)) {
						// calculate line total
						Float newTotal = iPrice
								* Float.parseFloat(inputOrderLineELement
										.getAttribute("OrderedQty"));
						Float updateTotal = newTotal - eTotal;
						if (htPrice.get(eWebLineNumber1) == null) {
							htPrice.put(eWebLineNumber1, updateTotal);

						} else {
							Float newUpdateTotal = htPrice.get(eWebLineNumber1)
									+ updateTotal;
							htPrice.put(eWebLineNumber1, newUpdateTotal);

						}
						// update the orderline
						// orderLinesElement.removeChild(orderLineElement);

						Node inputOrderLineNode = changeFulfillmentOrderDoc
								.importNode(inputOrderLineELement, true);
						Element inputOrderLineNodeElement = (Element) inputOrderLineNode;
						// include IsPriceLocked="Y"
						NodeList linePriceNodeList = inputOrderLineNodeElement
								.getElementsByTagName("LinePriceInfo");
						int priceLength = linePriceNodeList.getLength();
						if (priceLength != 0) {
							Element linePriceElement = (Element) linePriceNodeList
									.item(0);
							linePriceElement.setAttribute("IsPriceLocked", "Y");
						}
						inputOrderLineNodeElement.setAttribute("Action",
								"MODIFY");
						inputOrderLineNodeElement.setAttribute("PrimeLineNo",
								inputOrderLineELement
										.getAttribute("PrimeLineNo"));
						inputOrderLineNodeElement
								.setAttribute("SubLineNo",
										inputOrderLineELement
												.getAttribute("SubLineNo"));
						// orderLinesElement.appendChild(inputOrderLineNodeElement);
						orderLinesElement.replaceChild(
								inputOrderLineNodeElement, orderLineElement);
					}

				}
			}
		}
		return changeFulfillmentOrderDoc;
	}

	private void createInputForPriceChange(YFSEnvironment env,
			Element inputOrderLineELement, String webLineNumber, Float iPrice,
			Float eTotal) throws RemoteException, Exception {
		String eWebLineNumber = "";
		// get the orderlinekey
		// form the input to get the fulfillment order
		Document inputFulfillmentOrderDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element inputFulfillmentOrderElement = inputFulfillmentOrderDoc
				.getDocumentElement();
		inputFulfillmentOrderElement.setAttribute("OrderHeaderKey",
				fulfillmentOrderDetails.get(1));
		Document outputFulfillmentOrderDoc = getSterlingOrderList(env,
				fulfillmentOrderDetails.get(0));
		NodeList fOrderList = outputFulfillmentOrderDoc
				.getElementsByTagName("Order");
		int oLength = fOrderList.getLength();
		if (oLength != 0) {
			Element changeOrderElement = (Element) fOrderList.item(0);
			changeOrderElement.setAttribute("Action", "MODIFY");

			// changeOrderElement.setAttribute("ChangeInQty", checkFlag);
			Document newDoc = YFCDocument.createDocument().getDocument();
			newDoc.appendChild(newDoc.importNode(changeOrderElement, true));
			newDoc.renameNode(newDoc.getDocumentElement(), newDoc
					.getNamespaceURI(), "Order");

			NodeList orderLinesList = newDoc.getElementsByTagName("OrderLines");
			int orderLinesLength = orderLinesList.getLength();
			if (orderLinesLength != 0) {
				Element orderLinesElement = (Element) orderLinesList.item(0);
				log.debug("orderLinesElement"
						+ SCXmlUtil.getString(orderLinesElement));

				NodeList orderLineList = orderLinesElement
						.getElementsByTagName("OrderLine");
				int orderLineLength = orderLineList.getLength();
				if (orderLineLength != 0) {
					for (int oCount = 0; oCount < orderLineLength; oCount++) {
						Element orderLineElement = (Element) orderLineList
								.item(oCount);
						// check if the line already exists then update the line
						// newly added
						eWebLineNumber = getWebLineKey(env, orderLineElement);
						String eWebLineNumber1 = SCXmlUtil.getXpathAttribute(
								orderLineElement, "./Extn/@ExtnWebLineNumber");
						if (eWebLineNumber.equals(webLineNumber)) {
							// calculate line total
							Float newTotal = iPrice
									* Float.parseFloat(inputOrderLineELement
											.getAttribute("OrderedQty"));
							Float updateTotal = newTotal - eTotal;
							if (htPrice.get(eWebLineNumber1) == null) {
								htPrice.put(eWebLineNumber1, updateTotal);
							} else {
								Float newUpdateTotal = htPrice
										.get(eWebLineNumber1)
										+ updateTotal;
								htPrice.put(eWebLineNumber1, newUpdateTotal);

							}
							// update the orderline

							// orderLinesElement.removeChild(orderLineElement);
							// remove the item node

							Node inputOrderLineNode = newDoc.importNode(
									inputOrderLineELement, true);
							Element inputOrderLineNodeElement = (Element) inputOrderLineNode;
							// include IsPriceLocked="Y"
							NodeList linePriceNodeList = inputOrderLineNodeElement
									.getElementsByTagName("LinePriceInfo");
							int priceLength = linePriceNodeList.getLength();
							if (priceLength != 0) {
								Element linePriceElement = (Element) linePriceNodeList
										.item(0);
								linePriceElement.setAttribute("IsPriceLocked",
										"Y");
							}
							inputOrderLineNodeElement.setAttribute("Action",
									"MODIFY");
							inputOrderLineNodeElement.setAttribute(
									"PrimeLineNo", orderLineElement
											.getAttribute("PrimeLineNo"));
							inputOrderLineNodeElement.setAttribute("SubLineNo",
									orderLineElement.getAttribute("SubLineNo"));
							// orderLinesElement.appendChild(inputOrderLineNodeElement);
							// Node replaceNode =
							// orderLinesElement.replaceChild(orderLineElement,
							// inputOrderLineNodeElement);
							// newDoc.importNode(orderLinesElement.replaceChild(orderLineElement,
							// inputOrderLineNodeElement), true);
							orderLinesElement
									.replaceChild(inputOrderLineNodeElement,
											orderLineElement);

							// orderLinesElement.replaceChild(inputOrderLineELement,
							// orderLineElement);
						}

					}
				}
			}
			newDoc.getDocumentElement().setAttribute("QtyChange", "true");
			log.debug("the change order doc looks like"
					+ SCXmlUtil.getString(newDoc));
			htChangeOrder.put(fulfillmentOrderDetails.get(1), newDoc);
		}
	}

	private void updateChangeOrderXMLInHTChangeOrder(YFSEnvironment env,
			Element inputChainedOrderLineElement,
			Document changeFulfillmentOrderDoc, String webLineNumber,
			Element inputOrderLineELement, String checkFlag) throws Exception {
		String eWebLineNumber = "";
		// log.debug("444444444444444444444444444");
		log.debug("inputOrderLineELement"
				+ SCXmlUtil.getString(inputOrderLineELement));
		NodeList orderLinesList = changeFulfillmentOrderDoc
				.getElementsByTagName("OrderLines");
		changeFulfillmentOrderDoc.getDocumentElement().setAttribute(
				"ChangeInQty", checkFlag);
		int orderLinesLength = orderLinesList.getLength();
		if (orderLinesLength != 0) {
			Element orderLinesElement = (Element) orderLinesList.item(0);
			log.debug("orderLinesElement"
					+ SCXmlUtil.getString(orderLinesElement));

			NodeList orderLineList = orderLinesElement
					.getElementsByTagName("OrderLine");
			int orderLineLength = orderLineList.getLength();
			if (orderLineLength != 0) {
				for (int oCount = 0; oCount < orderLineLength; oCount++) {
					Element orderLineElement = (Element) orderLineList
							.item(oCount);
					// check if the line already exists then update the line
					// newly added
					eWebLineNumber = getWebLineKey(env, orderLineElement);
					// eWebLineNumber =
					// SCXmlUtil.getXpathAttribute(orderLineElement,
					// "./Extn/@ExtnWebLineNumber");
					if (eWebLineNumber.equals(webLineNumber)) {
						// check if it has modify
						if (orderLineElement.hasAttribute("Action")) {
							orderLineElement.setAttribute("OrderedQty",
									inputOrderLineELement
											.getAttribute("OrderedQty"));
						} else {
							// update the orderline
							orderLinesElement.removeChild(orderLineElement);

							Node inputOrderLineNode = changeFulfillmentOrderDoc
									.importNode(inputOrderLineELement, true);
							Element inputOrderLineNodeElement = (Element) inputOrderLineNode;
							inputOrderLineNodeElement.setAttribute("Action",
									"MODIFY");
							inputOrderLineNodeElement.setAttribute(
									"PrimeLineNo", inputOrderLineELement
											.getAttribute("PrimeLineNo"));
							inputOrderLineNodeElement.setAttribute("SubLineNo",
									inputOrderLineELement
											.getAttribute("SubLineNo"));
							orderLinesElement
									.appendChild(inputOrderLineNodeElement);
						}
					}

				}
			}
		}
	}

	private void createChangeOrderXMLForCWhenHTChangeOrderisEmpty(
			YFSEnvironment env, String webLineNumber,
			Element inputOrderLineELement, String checkFlag,
			Element inputElement) throws Exception {
		String eWebLineNumber = "";
		// get the orderlinekey
		// form the input to get the fulfillment order
		Document inputFulfillmentOrderDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element inputFulfillmentOrderElement = inputFulfillmentOrderDoc
				.getDocumentElement();
		inputFulfillmentOrderElement.setAttribute("OrderHeaderKey",
				fulfillmentOrderDetails.get(1));
		Document outputFulfillmentOrderDoc = getSterlingOrderList(env,
				fulfillmentOrderDetails.get(0));
		NodeList fOrderList = outputFulfillmentOrderDoc
				.getElementsByTagName("Order");
		int oLength = fOrderList.getLength();
		if (oLength != 0) {
			Element changeOrderElement = (Element) fOrderList.item(0);
			changeOrderElement.setAttribute("Action", "MODIFY");
			NodeList extnChangeOrderList = changeOrderElement
					.getElementsByTagName("Extn");
			Element extnChangeOrderElement = (Element) extnChangeOrderList
					.item(0);

			// set the extn fields
			NodeList extnNodeList = inputElement.getElementsByTagName("Extn");
			Element extnElement = (Element) extnNodeList.item(0);
			Node newExtnNode = outputFulfillmentOrderDoc.importNode(
					extnElement, true);
			Element newExtnElement = (Element) newExtnNode;
			changeOrderElement.replaceChild(newExtnElement,
					extnChangeOrderElement);
			log.debug("changeOrderElement"
					+ SCXmlUtil.getString(changeOrderElement));

			changeOrderElement.setAttribute("ChangeInQty", checkFlag);
			Document newDoc = YFCDocument.createDocument().getDocument();
			newDoc.appendChild(newDoc.importNode(changeOrderElement, true));
			newDoc.renameNode(newDoc.getDocumentElement(), newDoc
					.getNamespaceURI(), "Order");

			NodeList orderLinesList = newDoc.getElementsByTagName("OrderLines");
			int orderLinesLength = orderLinesList.getLength();
			if (orderLinesLength != 0) {
				Element orderLinesElement = (Element) orderLinesList.item(0);
				log.debug("orderLinesElement"
						+ SCXmlUtil.getString(orderLinesElement));

				NodeList orderLineList = orderLinesElement
						.getElementsByTagName("OrderLine");
				int orderLineLength = orderLineList.getLength();
				if (orderLineLength != 0) {
					for (int oCount = 0; oCount < orderLineLength; oCount++) {
						Element orderLineElement = (Element) orderLineList
								.item(oCount);
						// check if the line already exists then update the line
						// newly added
						eWebLineNumber = getWebLineKey(env, orderLineElement);
						// eWebLineNumber =
						// SCXmlUtil.getXpathAttribute(orderLineElement,
						// "./Extn/@ExtnWebLineNumber");
						if (eWebLineNumber.equals(webLineNumber)) {
							// update the orderline
							orderLinesElement.removeChild(orderLineElement);

							Node inputOrderLineNode = newDoc.importNode(
									inputOrderLineELement, true);
							Element inputOrderLineNodeElement = (Element) inputOrderLineNode;
							inputOrderLineNodeElement.setAttribute("Action",
									"MODIFY");
							inputOrderLineNodeElement.setAttribute(
									"PrimeLineNo", orderLineElement
											.getAttribute("PrimeLineNo"));
							inputOrderLineNodeElement.setAttribute("SubLineNo",
									orderLineElement.getAttribute("SubLineNo"));
							orderLinesElement
									.appendChild(inputOrderLineNodeElement);
						}

					}
				}
			}
			newDoc.getDocumentElement().setAttribute("QtyChange", "true");
			log.debug("the change order doc looks like"
					+ SCXmlUtil.getString(newDoc));
			htChangeOrder.put(fulfillmentOrderDetails.get(1), newDoc);
		}
	}

	private void populateHTCustomerOrder(YFSEnvironment env,
			String customerOrderNo) throws Exception {
		log.debug("---> populateHTCustomerOrder()");
		// form the input xml for cusotmer order
		Document inputCustomerOrderDocument = YFCDocument.createDocument(
				"Order").getDocument();
		Element inputCustomerOrderElement = inputCustomerOrderDocument
				.getDocumentElement();
		inputCustomerOrderElement.setAttribute("OrderNo", customerOrderNo);
		Document templateDoc = setTemplate(env);
		env.setApiTemplate("getOrderList", templateDoc);
		log.debug("Triggering getOrderList API for the customer Order : "
				+ customerOrderNo);
		Document outputCustomerOrderListDoc = api.invoke(env, "getOrderList",
				inputCustomerOrderDocument);
		env.clearApiTemplate("getOrderList");
		NodeList customerOrderNodeList = outputCustomerOrderListDoc
				.getElementsByTagName("Order");
		int customerOrderLength = customerOrderNodeList.getLength();
		if (customerOrderLength != 0) {
			Element customerOrderElement = (Element) customerOrderNodeList
					.item(0);
			NodeList customerOrderLinesList = customerOrderElement
					.getElementsByTagName("OrderLines");
			int customerOrderLinesListLength = customerOrderLinesList
					.getLength();
			if (customerOrderLinesListLength != 0) {
				Element customerOrderLinesElement = (Element) customerOrderLinesList
						.item(0);
				NodeList customerOrderLineList = customerOrderLinesElement
						.getElementsByTagName("OrderLine");
				int customerOrderLineListLength = customerOrderLineList
						.getLength();
				if (customerOrderLineListLength != 0) {
					for (int cCounter = 0; cCounter < customerOrderLineListLength; cCounter++) {
						Element customerOrderLineElement = (Element) customerOrderLineList
								.item(cCounter);
						String webLineNumber = SCXmlUtil.getXpathAttribute(
								customerOrderLineElement,
								"/Extn/@ExtnWebLineNumber");
						htCustomerOrder.put(webLineNumber,
								customerOrderLineElement);
					}
				}
			}
		}
		log.debug("<--- populateHTCustomerOrder()");
	}

	/**
	 * @param env
	 * @param inputElement
	 * @return
	 * @throws Exception
	 */
	private ArrayList<String> getCustomerOrderNo(YFSEnvironment env,
			Element inputElement) throws Exception {
		log.debug("---> getCustomerOrderNo()");
		ArrayList<String> customerOrderDetails = new ArrayList<String>();
		String customerOrderNo = "";
		String customerOrderHeaderKey = "";
		String enterpriseCode = "";
		NodeList extnNode = inputElement.getElementsByTagName("Extn");
		Element extnInputElement = (Element) extnNode.item(0);
		String legacyOrderNo = extnInputElement
				.getAttribute("ExtnLegacyOrderNo");
		String webConfNum = extnInputElement.getAttribute("ExtnWebConfNum");
		// Form the input xml
		YFCDocument inputOrderDoc = YFCDocument.createDocument("Order");
		YFCElement inputOrderElement = inputOrderDoc.getDocumentElement();
		YFCElement extnElement = inputOrderDoc.createElement("Extn");
		extnElement.setAttribute("ExtnLegacyOrderNo", legacyOrderNo);
		extnElement.setAttribute("ExtnWebConfNum", webConfNum);
		inputOrderElement.appendChild(extnElement);

		Document templateDoc = setTemplate(env);
		env.setApiTemplate("getOrderList", templateDoc);
		// invoke the orderList
		Document outOrderListDoc = api.invoke(env, "getOrderList",
				inputOrderDoc.getDocument());
		env.clearApiTemplate("getOrderList");

		Element outOrderListElement = outOrderListDoc.getDocumentElement();
		String totalNumber = outOrderListElement.getAttribute("TotalOrderList");
		if (!totalNumber.equals("0")) {
			log
					.debug("getOrderList output has order(s) : Fulfillment order exists...");
			NodeList orderNode = outOrderListDoc.getElementsByTagName("Order");
			Element orderElement = (Element) orderNode.item(0);
			/** ************************************ */
			setOrderHeaderLevelAttributes(inputElement, orderElement);
			NodeList orderLinesNodeList = orderElement
					.getElementsByTagName("OrderLines");
			int orderLinesLength = orderLinesNodeList.getLength();
			if (orderLinesLength != 0) {
				Element orderLinesElement = (Element) orderLinesNodeList
						.item(0);

				NodeList orderLineNodeList = orderLinesElement
						.getElementsByTagName("OrderLine");
				int orderLineLength = orderLineNodeList.getLength();
				if (orderLineLength != 0) {
					Element orderLineElement = (Element) orderLineNodeList
							.item(0);

					customerOrderHeaderKey = orderLineElement
							.getAttribute("ChainedFromOrderHeaderKey");
				}
			}

		} else {
			log.debug("fulfillment order does not exist");
		}

		// get the customer order number
		// form the input
		Document inputCustomerOrderDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element inputCustomerOrderElement = inputCustomerOrderDoc
				.getDocumentElement();
		inputCustomerOrderElement.setAttribute("OrderHeaderKey",
				customerOrderHeaderKey);

		Document outputCustomerOrderDoc = api.invoke(env, "getOrderDetails",
				inputCustomerOrderDoc);
		Element outputCustomerOrderElement = outputCustomerOrderDoc
				.getDocumentElement();
		customerOrderNo = outputCustomerOrderElement.getAttribute("OrderNo");
		enterpriseCode = outputCustomerOrderElement
				.getAttribute("EnterpriseCode");
		log.debug("customerOrderNo: " + customerOrderNo);
		log.debug("customerOrderHeaderKey: " + customerOrderHeaderKey);
		log.debug("EnterpriseCode: " + enterpriseCode);
		customerOrderDetails.add(customerOrderHeaderKey);
		customerOrderDetails.add(customerOrderNo);
		customerOrderDetails.add(enterpriseCode);

		log.debug("<--- getCustomerOrderNo()");
		return customerOrderDetails;
	}

	/**
	 * @param env
	 * @param inputElement
	 * @return
	 * @throws Exception
	 */
	private ArrayList<String> getSterlingFulfillmentOrderNo(YFSEnvironment env,
			Element inputElement) throws Exception {
		log.debug("---> getSterlingFulfillmentOrderNo()");
		ArrayList<String> fulfillmentOrderDetails = new ArrayList<String>();
		String sFulfillmentOrderNo = "";
		String sFulfillmentOrderHeaderKey = "";
		String enterpriseCode = "";
		// get the legacy order number and web confirmation number from the
		// input
		NodeList extnNode = inputElement.getElementsByTagName("Extn");
		Element extnInputElement = (Element) extnNode.item(0);
		String legacyOrderNo = extnInputElement
				.getAttribute("ExtnLegacyOrderNo");
		String webConfNum = extnInputElement.getAttribute("ExtnWebConfNum");

		log.debug("ExtnLegacyOrderNo: " + legacyOrderNo + ",  ExtnWebConfNum: "
				+ webConfNum);
		// Determine the sterling legacy order
		// Form the input xml
		YFCDocument inputOrderDoc = YFCDocument.createDocument("Order");
		YFCElement inputOrderElement = inputOrderDoc.getDocumentElement();
		YFCElement extnElement = inputOrderDoc.createElement("Extn");
		extnElement.setAttribute("ExtnLegacyOrderNo", legacyOrderNo);
		extnElement.setAttribute("ExtnWebConfNum", webConfNum);
		inputOrderElement.appendChild(extnElement);

		// invoke the orderList
		Document templateDoc = setTemplate(env);
		env.setApiTemplate("getOrderList", templateDoc);
		Document outOrderListDoc = api.invoke(env, "getOrderList",
				inputOrderDoc.getDocument());
		env.clearApiTemplate("getOrderList");
		Element outOrderListElement = outOrderListDoc.getDocumentElement();
		String totalNumber = outOrderListElement.getAttribute("TotalOrderList");

		if (!totalNumber.equals("0")) {
			log.debug("Fulfillment order exists...");
			NodeList orderNode = outOrderListDoc.getElementsByTagName("Order");
			Element orderElement = (Element) orderNode.item(0);
			sFulfillmentOrderNo = orderElement.getAttribute("OrderNo");
			sFulfillmentOrderHeaderKey = orderElement
					.getAttribute("OrderHeaderKey");
			enterpriseCode = orderElement.getAttribute("EnterpriseCode");

			fulfillmentOrderDetails.add(sFulfillmentOrderNo);
			fulfillmentOrderDetails.add(sFulfillmentOrderHeaderKey);
			// newly added for enterprise
			fulfillmentOrderDetails.add(enterpriseCode);
		} else {
			log.debug("Fulfillment order does not exist");
		}
		log.debug("<--- getSterlingFulfillmentOrderNo()");
		// return sFulfillmentOrderNo;
		return fulfillmentOrderDetails;
	}

	/**
	 * @param env
	 * @param fulfillmentOrderNo
	 * @param htCurrentOrder
	 * @throws Exception
	 */
	private Hashtable<String, Element> populateHTCurrentOrder(
			YFSEnvironment env, String fulfillmentOrderNo,
			Hashtable<String, Element> htCurrentOrder) throws Exception {

		log.debug("---> populateHTCurrentOrder()");
		// form the input and invoke getorderlist for this slegacy number
		Document sOrderListDoc = getSterlingOrderList(env, fulfillmentOrderNo);

		// populate HashTable
		// Element sOrderListElement = sOrderListDoc.getDocumentElement();
		NodeList orderNode = sOrderListDoc.getElementsByTagName("OrderLines");
		int orderLineLength = orderNode.getLength();

		if (orderLineLength != 0) {
			Element orderElement = (Element) orderNode.item(0);
			NodeList oList = orderElement.getElementsByTagName("OrderLine");
			int oLength = oList.getLength();

			if (oLength != 0) {
				for (int oCounter = 0; oCounter < oLength; oCounter++) {
					Element oElement = (Element) oList.item(oCounter);

					// newly added
					String webLineNo = getWebLineKey(env, oElement);
					// String webLineNo = SCXmlUtil.getXpathAttribute(oElement,
					// "./Extn/@ExtnWebLineNumber");
					log.debug("Line No. : " + oCounter);
					log.debug("webLine Key: " + webLineNo);
					htCurrentOrder.put(webLineNo, oElement);
				}
				log.debug("<--- getWebLineKey()");
			}

		}
		log.debug("<--- populateHTCurrentOrder()");
		return htCurrentOrder;
	}

	private Hashtable<String, Element> populateHTCurrentCustomerOrder(
			YFSEnvironment env, String customerOrderNo,
			Hashtable<String, Element> htCurrentCustomerOrder) throws Exception {
		log.debug("---> populateHTCurrentCustomerOrder()");
		// form the input and invoke getorderlist for this slegacy number
		Document sOrderListDoc = getSterlingOrderList(env, customerOrderNo);

		// populate HashTable
		// Element sOrderListElement = sOrderListDoc.getDocumentElement();
		NodeList orderNode = sOrderListDoc.getElementsByTagName("OrderLines");
		int orderLineLength = orderNode.getLength();
		if (orderLineLength != 0) {
			Element orderElement = (Element) orderNode.item(0);
			NodeList oList = orderElement.getElementsByTagName("OrderLine");
			int oLength = oList.getLength();
			if (oLength != 0) {
				for (int oCounter = 0; oCounter < oLength; oCounter++) {
					Element oElement = (Element) oList.item(oCounter);

					// String webLineNo = getWebLineKey(env, oElement);
					String webLineNo = SCXmlUtil.getXpathAttribute(oElement,
							"./Extn/@ExtnWebLineNumber");
					log.debug("Line No. : " + oCounter);
					log.debug("ExtnWebLineNumber: " + webLineNo);
					htCurrentCustomerOrder.put(webLineNo, oElement);
				}
			}
		}
		log.debug("<--- populateHTCurrentCustomerOrder()");
		return htCurrentCustomerOrder;
	}

	/**
	 * @param env
	 * @param fulfillmentOrderNo
	 * @return
	 * @throws RemoteException
	 *             method to fetch the Order List
	 */
	private Document getSterlingOrderList(YFSEnvironment env,
			String fulfillmentOrderNo) throws RemoteException {

		Document inputOrderDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element inputOrderElement = inputOrderDoc.getDocumentElement();
		inputOrderElement.setAttribute("OrderNo", fulfillmentOrderNo);
		// set output template
		Document orderTemplateDoc = YFCDocument.createDocument("OrderList")
				.getDocument();
		Element orderTemplateElement = orderTemplateDoc.getDocumentElement();
		Element orderElement = orderTemplateDoc.createElement("Order");
		orderElement.setAttribute("OrderNo", "");
		orderElement.setAttribute("BillToID", "");
		orderElement.setAttribute("OrderHeaderKey", "");
		orderElement.setAttribute("BuyerOrganizationCode", "");
		orderElement.setAttribute("ShipToID", "");
		orderElement.setAttribute("DocumentType", "");
		orderElement.setAttribute("ShipNode", "");
		orderElement.setAttribute("SellerOrganizationCode", "");
		orderElement.setAttribute("EnterpriseCode", "");
		orderElement.setAttribute("CustomerPONo", "");
		orderElement.setAttribute("OrderDate", "");
		orderElement.setAttribute("ReqDeliveryDate", "");
		orderElement.setAttribute("SourceType", "");
		orderTemplateElement.appendChild(orderElement);
		Element orderExtnElement = orderTemplateDoc.createElement("Extn");
		orderElement.appendChild(orderExtnElement);
		Element orderPriceInfo = orderTemplateDoc.createElement("PriceInfo");
		orderElement.appendChild(orderPriceInfo);
		/*
		 * Element orderInstructions =
		 * orderTemplateDoc.createElement("Instructions");
		 * orderElement.appendChild(orderInstructions);
		 */
		Element oLinesElement = orderTemplateDoc.createElement("OrderLines");
		orderElement.appendChild(oLinesElement);
		Element oLineElement = orderTemplateDoc.createElement("OrderLine");
		oLineElement.setAttribute("ChainedFromOrderHeaderKey", "");
		oLineElement.setAttribute("ChainedFromOrderLineKey", "");
		oLineElement.setAttribute("OrderedQty", "");
		oLineElement.setAttribute("LineType", "");
		oLineElement.setAttribute("OrderingUOM", "");
		oLineElement.setAttribute("ShipToID", "");
		oLineElement.setAttribute("ShipNode", "");
		oLineElement.setAttribute("PrimeLineNo", "");
		oLineElement.setAttribute("SubLineNo", "");
		oLinesElement.appendChild(oLineElement);
		Element itemElement = orderTemplateDoc.createElement("Item");
		oLineElement.appendChild(itemElement);
		Element linePriceElement = orderTemplateDoc
				.createElement("LinePriceInfo");
		oLineElement.appendChild(linePriceElement);
		Element extnElement = orderTemplateDoc.createElement("Extn");
		oLineElement.appendChild(extnElement);
		/*
		 * Element orderLineInstructions =
		 * orderTemplateDoc.createElement("Instructions");
		 * oLineElement.appendChild(orderLineInstructions);
		 */
		Element orderStatusesElement = orderTemplateDoc
				.createElement("OrderStatuses");
		oLineElement.appendChild(orderStatusesElement);
		Element orderStatus = orderTemplateDoc.createElement("OrderStatus");
		orderStatusesElement.appendChild(orderStatus);
		env.setApiTemplate("getOrderList", orderTemplateDoc);
		Document outOrderListDoc = api.invoke(env, "getOrderList",
				inputOrderDoc);

		env.clearApiTemplate("getOrderList");
		return outOrderListDoc;
	}

	private Document setTemplate(YFSEnvironment env) throws Exception {
		Document templateDoc = YFCDocument.createDocument("OrderList")
				.getDocument();
		Element templateElement = templateDoc.getDocumentElement();
		Element orderElement = templateDoc.createElement("Order");
		orderElement.setAttribute("OrderNo", "");
		orderElement.setAttribute("OrderHeaderKey", "");
		orderElement.setAttribute("BuyerOrganizationCode", "");
		orderElement.setAttribute("BillToID", "");
		orderElement.setAttribute("ShipToID", "");
		orderElement.setAttribute("DocumentType", "");
		orderElement.setAttribute("EnterpriseCode", "");
		orderElement.setAttribute("ShipNode", "");
		orderElement.setAttribute("SellerOrganizationCode", "");
		orderElement.setAttribute("EnterpriseCode", "");
		orderElement.setAttribute("CustomerPONo", "");
		orderElement.setAttribute("OrderDate", "");
		orderElement.setAttribute("ReqDeliveryDate", "");
		orderElement.setAttribute("SourceType", "");
		templateElement.appendChild(orderElement);
		Element orderExtnElement = templateDoc.createElement("Extn");
		orderElement.appendChild(orderExtnElement);
		Element orderPriceInfoElement = templateDoc.createElement("PriceInfo");
		orderElement.appendChild(orderPriceInfoElement);
		Element oLinesElement = templateDoc.createElement("OrderLines");
		orderElement.appendChild(oLinesElement);
		Element oLineElement = templateDoc.createElement("OrderLine");
		oLineElement.setAttribute("ChainedFromOrderHeaderKey", "");
		oLineElement.setAttribute("ChainedFromOrderLineKey", "");
		oLineElement.setAttribute("OrderedQty", "");
		oLineElement.setAttribute("LineType", "");
		oLineElement.setAttribute("OrderingUOM", "");
		oLineElement.setAttribute("ShipToID", "");
		oLineElement.setAttribute("ShipNode", "");
		oLineElement.setAttribute("PrimeLineNo", "");
		oLineElement.setAttribute("SubLineNo", "");
		oLinesElement.appendChild(oLineElement);
		Element itemElement = templateDoc.createElement("Item");
		oLineElement.appendChild(itemElement);
		Element linePriceElement = templateDoc.createElement("LinePriceInfo");
		oLineElement.appendChild(linePriceElement);
		Element extnElement = templateDoc.createElement("Extn");
		oLineElement.appendChild(extnElement);
		Element orderStatusesElement = templateDoc
				.createElement("OrderStatuses");
		oLineElement.appendChild(orderStatusesElement);
		Element orderStatus = templateDoc.createElement("OrderStatus");
		orderStatusesElement.appendChild(orderStatus);

		return templateDoc;
	}

	private Document getSterlingOrderDetails(YFSEnvironment env,
			Document inputDoc) throws RemoteException {
		// set output template
		Document orderTemplateDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element orderElement = orderTemplateDoc.getDocumentElement();
		orderElement.setAttribute("OrderNo", "");
		orderElement.setAttribute("OrderHeaderKey", "");
		orderElement.setAttribute("BuyerOrganizationCode", "");
		orderElement.setAttribute("ShipToID", "");
		orderElement.setAttribute("DocumentType", "");
		orderElement.setAttribute("ShipNode", "");
		orderElement.setAttribute("SellerOrganizationCode", "");
		orderElement.setAttribute("EnterpriseCode", "");
		Element orderExtnElement = orderTemplateDoc.createElement("Extn");
		orderElement.appendChild(orderExtnElement);
		Element oLinesElement = orderTemplateDoc.createElement("OrderLines");
		orderElement.appendChild(oLinesElement);
		Element oLineElement = orderTemplateDoc.createElement("OrderLine");
		oLineElement.setAttribute("ChainedFromOrderHeaderKey", "");
		oLineElement.setAttribute("ChainedFromOrderLineKey", "");
		oLineElement.setAttribute("OrderedQty", "");
		oLineElement.setAttribute("LineType", "");
		oLineElement.setAttribute("OrderingUOM", "");
		oLineElement.setAttribute("ShipToID", "");
		oLineElement.setAttribute("ShipNode", "");
		oLineElement.setAttribute("PrimeLineNo", "");
		oLineElement.setAttribute("SubLineNo", "");
		oLinesElement.appendChild(oLineElement);
		Element itemElement = orderTemplateDoc.createElement("Item");
		oLineElement.appendChild(itemElement);
		Element linePriceElement = orderTemplateDoc
				.createElement("LinePriceInfo");
		oLineElement.appendChild(linePriceElement);
		Element extnElement = orderTemplateDoc.createElement("Extn");
		oLineElement.appendChild(extnElement);
		Element orderStatusesElement = orderTemplateDoc
				.createElement("OrderStatuses");
		oLineElement.appendChild(orderStatusesElement);
		Element orderStatus = orderTemplateDoc.createElement("OrderStatus");
		orderStatusesElement.appendChild(orderStatus);
		env.setApiTemplate("getOrderList", orderTemplateDoc);
		log.debug("7777777777777input" + SCXmlUtil.getString(inputDoc));
		Document outOrderListDoc = api.invoke(env, "getOrderDetails", inputDoc);
		env.clearApiTemplate("getOrderList");

		return outOrderListDoc;
	}

	private String getWebLineKey(YFSEnvironment env, Element inputLineElement)
			throws Exception {
		log.debug("---> getWebLineKey()");
		String key = "";
		String webLineNumber = "";
		String legacyLineNumber = "";
		// String itemID = "";
		// String shipNode = "";
		webLineNumber = SCXmlUtil.getXpathAttribute(inputLineElement,
				"./Extn/@ExtnWebLineNumber");
		// itemID = SCXmlUtil.getXpathAttribute(inputLineElement,
		// "./Item/@ItemID");
		// shipNode = inputLineElement.getAttribute("ShipNode");
		legacyLineNumber = SCXmlUtil.getXpathAttribute(inputLineElement,
				"./Extn/@ExtnLegacyLineNumber");
		// key = webLineNumber+"|"+itemID+"|"+shipNode;
		key = webLineNumber + "|" + legacyLineNumber;

		log.debug("ExtnWebLineNumber: " + webLineNumber
				+ ",  ExtnLegacyLineNumber: " + legacyLineNumber
				+ ", WebLineKey: " + key);
		log.debug("<--- getWebLineKey()");
		return key;
	}

	private Document setCustomerListTemplate() throws Exception {
		Document templateEmailDoc = YFCDocument.createDocument("CustomerList")
				.getDocument();
		Element templateElement = templateEmailDoc.getDocumentElement();
		Element CustElement = templateEmailDoc.createElement("Customer");
		templateElement.appendChild(CustElement);
		CustElement.setAttribute("CustomerID", "");
		Element ExtnElement = templateEmailDoc.createElement("Extn");
		ExtnElement.setAttribute("ExtnECsr1EMailID", "");
		ExtnElement.setAttribute("ExtnECsr2EMailID", "");
		CustElement.appendChild(ExtnElement);
		log.debug("templateEmailDoc: "
				+ YFCDocument.getDocumentFor(templateEmailDoc));

		return templateEmailDoc;
	}

	private Document setOrganizationListTemplate() throws Exception {
		Document templateEmailDoc = YFCDocument.createDocument(
				"OrganizationList").getDocument();
		Element templateElement = templateEmailDoc.getDocumentElement();

		Element OrgElement = templateEmailDoc.createElement("Organization");
		OrgElement.setAttribute("OrganizationCode", "");
		templateElement.appendChild(OrgElement);
		Element crpPrsnInfoElement = templateEmailDoc
				.createElement("CorporatePersonInfo");
		crpPrsnInfoElement.setAttribute("EMailID", "");
		OrgElement.appendChild(crpPrsnInfoElement);

		return templateEmailDoc;
	}

	private String getLegacyItemNumberFromXref(YFSEnvironment env,
			String customerPartNo, String envtId, String companyCode,
			String legacyCustomerNumber) {

		Document getItemXRefListOutputDoc = null;
		String legacyItemNumber = null;

		Document getItemXRefListDoc = YFCDocument.createDocument(
				"XPXItemcustXref").getDocument();

		getItemXRefListDoc.getDocumentElement().setAttribute(
				XPXLiterals.A_COMPANY_CODE, companyCode);
		getItemXRefListDoc.getDocumentElement().setAttribute("EnvironmentCode",
				envtId);
		getItemXRefListDoc.getDocumentElement().setAttribute("CustomerNumber",
				legacyCustomerNumber);
		getItemXRefListDoc.getDocumentElement().setAttribute(
				"CustomerItemNumber", customerPartNo);

		try {
			getItemXRefListOutputDoc = api.executeFlow(env,
					XPXLiterals.GET_XREF_LIST, getItemXRefListDoc);
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (getItemXRefListOutputDoc != null) {
			Element XREFOutputDocRoot = getItemXRefListOutputDoc
					.getDocumentElement();

			NodeList XREFCustElementList = XREFOutputDocRoot
					.getElementsByTagName(XPXLiterals.E_XPX_ITEM_CUST_XREF);

			if (XREFCustElementList.getLength() > 0) {
				for (int k = 0; k < XREFCustElementList.getLength(); k++) {
					Element XREFElement = (Element) XREFCustElementList.item(k);

					legacyItemNumber = XREFElement
							.getAttribute("LegacyItemNumber");
					log.debug("The legacy item number is: " + legacyItemNumber);
				}
			}
		}
		return legacyItemNumber;
	}
}