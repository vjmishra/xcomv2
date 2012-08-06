package com.xpedx.nextgen.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.shared.ycp.YFSContext;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * Description: Updates/Creates/Deletes an order in Sterling based on the Order
 * updates from Legacy system. Type of Order updates are identified by the
 * process codes(A,C,D and S)
 * 
 * A - Add/create a new order. C - Change in an order. D - Delete an order. S -
 * Status change of an order.
 * 
 * @author mgandhi, jstaney.
 * 
 */

public class XPXPerformLegacyOrderUpdateExAPI implements YIFCustomApi {

	private static YIFApi api = null;
	private Properties _prop;
	private static YFCLogCategory log;
	boolean centExempt=false;

	static {

		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException yifEx) {
			yifEx.printStackTrace();
		}
	}

	/**
	 * Updates the customer and fulfillment order based on legacy order update.
	 * 
	 * The logic initially checks the Header Code of the received in Legacy XML  and 
	 * for each HeaderCode it has logic to process the Order lines using lineProcessCode.  
	 * 
	 * @param env  Environment object.
	 * @param inXML  Order document thats been received from legacy system.
	 * @return Output xml document.
	 * 
	 */

	public Document performLegacyOrderUpdate(YFSEnvironment env, Document inXML) throws Exception {

		boolean isAPISuccess = false;
		boolean isExceptionScenario = false;
		YFCDocument returnToLegacyDoc = null;
		Exception APIException = null;
		YFCElement cAndfOrderEle = null;
		// Fulfillment Order Element.
		YFCElement fOrderEle = null;
		// Customer Order Element.
		YFCElement cOrderEle = null;
		String headerProcessCode = null;
		String isOrderPlaceFlag = null;
		String hdrStatusCode = null;

		YFCDocument yfcDoc = YFCDocument.getDocumentFor(inXML);
		
		//rootEle contains the Legacy XML document. 
		YFCElement rootEle = yfcDoc.getDocumentElement();

		try {
			if(log.isDebugEnabled()){
			log.debug("XPXPerformLegacyOrderUpdateAPI-InXML:" + YFCDocument.getDocumentFor(inXML).getString());
			}
			// Retrieve the header process code from the input doc.
			if (rootEle.hasAttribute("HeaderProcessCode")) {
				headerProcessCode = rootEle.getAttribute("HeaderProcessCode");
				if (YFCObject.isNull(headerProcessCode) || YFCObject.isVoid(headerProcessCode)) {
					throw new Exception("Attribute HeaderProcessCode Cannot be NULL or Void!");
				} else {
					// Header process code "S" is changed to "C" when the order has Order Lines 
					if (headerProcessCode.equalsIgnoreCase("S")) {
						YFCElement orderLinesEle = rootEle.getChildElement("OrderLines");
						if (orderLinesEle != null) {
							YFCElement orderLineEle = orderLinesEle.getChildElement("OrderLine");
							if (orderLineEle != null) {
								rootEle.setAttribute("HeaderProcessCode", "C");
							} 
						}
					}
				}
			} else {
				throw new Exception("Attribute HeaderProcessCode Not Available in Incoming Legace Message!");
			}

			validateInXML(rootEle,headerProcessCode);

			if(log.isDebugEnabled()){
				log.debug("InXML After Validation:" + YFCDocument.getDocumentFor(inXML).getString());
			}

			// Added by Prasanth Kumar M. to prevent rollback of the orders if this code is invoked in OPResponse flow
			if (rootEle.hasAttribute("IsOrderPlace")) {
				isOrderPlaceFlag = rootEle.getAttribute("IsOrderPlace");
				if (YFCObject.isNull(isOrderPlaceFlag) || YFCObject.isVoid(isOrderPlaceFlag)) {
					isOrderPlaceFlag = "N";
				}
			} else {
				isOrderPlaceFlag = "N";
			}

			// To set environment variables which are required for XPEDXOverrideGetOrderPriceUE.java
			setProgressYFSEnvironmentVariables(env);

			Document inDoc = YFCDocument.createDocument().getDocument();
			inDoc.appendChild(inDoc.importNode(rootEle.getDOMNode(), true));
			inDoc.renameNode(inDoc.getDocumentElement(), inDoc.getNamespaceURI(), "Order");
			YFCElement inXMLEle = YFCDocument.getDocumentFor(inDoc).getDocumentElement();

			// Validations done for the Xml received from legacy system.
			if (cAndfOrderEle == null) {
				// To get customer and fulfillment order details if exist.
				cAndfOrderEle = getCustomerOrderAndFulfillmentOrderList(env, rootEle, headerProcessCode);
				if (cAndfOrderEle != null) {
					// To get fulfillment order details.
					String fOrdHeaderKey = (String) env.getTxnObject("FOKEY");
					if (YFCObject.isNull(fOrdHeaderKey) || YFCObject.isVoid(fOrdHeaderKey)) {
						fOrdHeaderKey = rootEle.getAttribute("OrderHeaderKey");
					}

					if(log.isDebugEnabled()){
						log.debug("Fulfillment OrderHeaderKey:" + fOrdHeaderKey);
					}
					if (YFCObject.isNull(fOrdHeaderKey) || YFCObject.isVoid(fOrdHeaderKey)) {
						fOrderEle = getFulfillmentOrderEle(headerProcessCode, rootEle, cAndfOrderEle);
					} else {
						fOrderEle = getFulfillmentOrderEle(headerProcessCode, fOrdHeaderKey, cAndfOrderEle);
					}

					if (fOrderEle == null) {
						if (!headerProcessCode.equalsIgnoreCase("A")) {
							throw new Exception("Fulfillment Order Does Not Exist in the System!");
						}
					} else {
						if (headerProcessCode.equalsIgnoreCase("A")) {
							if (!this.isCancelledOrder(fOrderEle)) {
								throw new Exception("Fulfillment Order Already Exists in the System!");
							} else {
								// A Cancelled Fulfillment order exist.
								headerProcessCode = "C";
								rootEle.setAttribute("HeaderProcessCode", "C");
							}
						}
						// To remove the lines from InXML which is already been deleted from the system.
						removeDeletedLines(rootEle,fOrderEle);
					}

					// To get Customer order details.
					cOrderEle = getCustomerOrderEle(env, headerProcessCode, rootEle, cAndfOrderEle);
					if (cOrderEle == null) {
						if (!headerProcessCode.equalsIgnoreCase("A")) {
							throw new Exception("Customer Order Does Not Exist in the System!");
						}
					} else {
						if (headerProcessCode.equalsIgnoreCase("A")) {
							this.getOrderExtendedPriceInfo(rootEle);
						}
					}

				} else {
					if (!headerProcessCode.equalsIgnoreCase("A")) {
						throw new Exception("Either Customer Order Or Fulfillment Order Not Exists!");
					} else {
						this.getOrderExtendedPriceInfo(rootEle);
					}
				}
			}
			
			// OPR Exception Scenario
			String isOrderUpdate = rootEle.getAttribute("IsOrderUpdate");
			if (!YFCObject.isNull(isOrderUpdate) && !YFCObject.isVoid(isOrderUpdate) && isOrderUpdate.equalsIgnoreCase("Y") && !headerProcessCode.equalsIgnoreCase("A")) {
				if (!isOPRProcessed(fOrderEle)) {
					isExceptionScenario = true;
					// To set the order lock flag to Y.
					YFCElement orderExtnElem = rootEle.getChildElement("Extn");
					if (orderExtnElem != null) {
						orderExtnElem.setAttribute("ExtnOrderLockFlag", "Y");
					}

					// To apply Order exception hold on the order.
					YFCElement ordHoldTypesEle = rootEle.getOwnerDocument().createElement("OrderHoldTypes");
					rootEle.appendChild(ordHoldTypesEle);
					YFCElement ordHoldTypeEle = ordHoldTypesEle.getOwnerDocument().createElement("OrderHoldType");
					ordHoldTypesEle.appendChild(ordHoldTypeEle);
					ordHoldTypeEle.setAttribute("HoldType", "ORDER_EXCEPTION_HOLD");
					ordHoldTypeEle.setAttribute("ReasonText", "Order Exception Hold");
					ordHoldTypeEle.setAttribute("Status", "1100");
				}
			}

			String fStatusCode = "1100.0100";
			if (fOrderEle != null) {
				fStatusCode = fOrderEle.getAttribute("MaxOrderStatus");
				if (YFCObject.isNull(fStatusCode) || YFCObject.isVoid(fStatusCode)) {
					throw new Exception("Attribute MaxOrderStatus Cannot be NULL or Void!");
				}
				if (fStatusCode.equalsIgnoreCase("9000")) {
					fStatusCode = "1100.0100";
				}
			}

			hdrStatusCode = fStatusCode;
			if (rootEle.hasAttribute("OrderStatus")) {
				hdrStatusCode = rootEle.getAttribute("OrderStatus");
				if (YFCObject.isNull(hdrStatusCode) || YFCObject.isVoid(hdrStatusCode)) {
					hdrStatusCode = fStatusCode;
				} else {
					if (hdrStatusCode.equalsIgnoreCase("9000")) {
						hdrStatusCode = fStatusCode;
					}
				}
			}

			// If Header process code from input xml is A
			if (headerProcessCode.equalsIgnoreCase("A")) {
				this.setReqUOMPrice(env, rootEle);

				// To set the order header attributes of the customer order to the legacy input xml.
				this.setOrderHeaderAttributes(env, rootEle, cOrderEle);

				Document cOrderInXML = YFCDocument.createDocument().getDocument();
				cOrderInXML.appendChild(cOrderInXML.importNode(rootEle.getDOMNode(), true));
				cOrderInXML.renameNode(cOrderInXML.getDocumentElement(), cOrderInXML.getNamespaceURI(), "Order");
				YFCElement cOrderInXMLEle = YFCDocument.getDocumentFor(cOrderInXML).getDocumentElement();

				// Set the Extended Price Info for FO.
				this.setExtendedPriceInfo(env, rootEle, false);

				YFCElement custOrderEle = null;
				if (cOrderEle != null) {
					this.setExtendedPriceInfoCO(cOrderInXMLEle, rootEle, cAndfOrderEle,headerProcessCode);
					// Updates the customer order.
					custOrderEle = updateCustomerOrder(env, cOrderInXMLEle, cOrderEle).getDocumentElement();
				} else {
					this.setExtendedPriceInfo(env, cOrderInXMLEle, true);
					// To Create a new customer order.
					custOrderEle = createCustomerOrder(env, cOrderInXMLEle).getDocumentElement();
				}

				// To create fulfillment order.
				returnToLegacyDoc = createFulfillmentOrder(env, rootEle, custOrderEle, inXMLEle);
				this.performOrderStatusChange(env, cAndfOrderEle, custOrderEle, returnToLegacyDoc.getDocumentElement(), hdrStatusCode, "A");
				isAPISuccess = true;

			} else if (headerProcessCode.equalsIgnoreCase("C")) {
				this.setReqUOMPrice(env, rootEle);

				// Build input document to call ChangeOrder API.
				Document chngOrdDoc = YFCDocument.createDocument().getDocument();
				chngOrdDoc.appendChild(chngOrdDoc.importNode(rootEle.getDOMNode(), true));
				chngOrdDoc.renameNode(chngOrdDoc.getDocumentElement(), chngOrdDoc.getNamespaceURI(), "Order");
				YFCElement chngcOrderEle0 = YFCDocument.getDocumentFor(chngOrdDoc).getDocumentElement();
				chngcOrderEle0.removeAttribute("OrderHeaderKey");
				// To remove order lines element from the document.
				YFCElement remEle = chngcOrderEle0.getChildElement("OrderLines");
				if (remEle != null) {
					chngcOrderEle0.removeChild(remEle);
				}

				YFCElement chngcOOrdLinesEle0 = chngcOrderEle0.getOwnerDocument().createElement("OrderLines");
				chngcOrderEle0.appendChild(chngcOOrdLinesEle0);

				// To set the transaction Id.
				String tranId0 = null;
				if (this._prop != null) {
					tranId0 = this._prop.getProperty("XPX_CHN_CNL_STATUS_TXN");
					if (YFCObject.isNull(tranId0) || YFCObject.isVoid(tranId0)) {
						throw new Exception("TransactionID Not Configured in API Arguments!");
					}
				} else {
					throw new Exception("Arguments Not Configured For API-XPXPerformLegacyOrderUpdateAPI!");
				}

				// Build input document to call ChangeOrderStatus API.
				YFCDocument chngcOrdStatusInXML0 = YFCDocument.getDocumentFor("<OrderStatusChange/>");
				YFCElement chngcOrdStatusEle0 = chngcOrdStatusInXML0.getDocumentElement();
				chngcOrdStatusEle0.setAttribute("IgnoreTransactionDependencies", "Y");
				chngcOrdStatusEle0.setAttribute("TransactionId", tranId0);
				chngcOrdStatusEle0.setAttribute("BaseDropStatus", "1100.0010");

				YFCElement chngCOStatusLinesEle0 = chngcOrdStatusEle0.getOwnerDocument().createElement("OrderLines");
				chngcOrdStatusEle0.appendChild(chngCOStatusLinesEle0);

				String tranId = null;
				if (this._prop != null) {
					tranId = this._prop.getProperty("XPX_CHN_CRT_STATUS_TXN");
					if (YFCObject.isNull(tranId) || YFCObject.isVoid(tranId)) {
						throw new Exception("Transaction Not Configured in API Arguments!");
					}
				} else {
					throw new Exception("Arguments Not Configured For API-XPXPerformLegacyOrderUpdateAPI!");
				}

				YFCDocument chngcOrdStatusInXML = YFCDocument.getDocumentFor("<OrderStatusChange/>");
				YFCElement chngcOrdStatusEle = chngcOrdStatusInXML.getDocumentElement();
				chngcOrdStatusEle.setAttribute("IgnoreTransactionDependencies", "Y");
				chngcOrdStatusEle.setAttribute("TransactionId", tranId);
				chngcOrdStatusEle.setAttribute("BaseDropStatus", "1100.0100");

				YFCElement chngCOStatusLinesEle = chngcOrdStatusEle.getOwnerDocument().createElement("OrderLines");
				chngcOrdStatusEle.appendChild(chngCOStatusLinesEle);

				YFCDocument chngcOrdStatusInXML1 = YFCDocument.getDocumentFor("<OrderStatusChange/>");
				YFCElement chngcOrdStatusEle1 = chngcOrdStatusInXML1.getDocumentElement();
				chngcOrdStatusEle1.setAttribute("IgnoreTransactionDependencies", "Y");
				chngcOrdStatusEle1.setAttribute("TransactionId", tranId0);
				chngcOrdStatusEle1.setAttribute("BaseDropStatus", "1100.0010");

				YFCElement chngCOStatusLinesEle1 = chngcOrdStatusEle1.getOwnerDocument().createElement("OrderLines");
				chngcOrdStatusEle1.appendChild(chngCOStatusLinesEle1);

				// Build input document to call ChangeOrder API.
				Document chngOrdDoc1 = YFCDocument.createDocument().getDocument();
				chngOrdDoc1.appendChild(chngOrdDoc1.importNode(rootEle.getDOMNode(), true));
				chngOrdDoc1.renameNode(chngOrdDoc1.getDocumentElement(), chngOrdDoc1.getNamespaceURI(), "Order");
				YFCElement chngcOrderEle1 = YFCDocument.getDocumentFor(chngOrdDoc1).getDocumentElement();
				chngcOrderEle1.removeAttribute("OrderHeaderKey");
				// To remove order lines element from the document.
				YFCElement remEle1 = chngcOrderEle1.getChildElement("OrderLines");
				if (remEle1 != null) {
					chngcOrderEle1.removeChild(remEle1);
				}

				YFCElement chngcOOrdLinesEle1 = chngcOrderEle1.getOwnerDocument().createElement("OrderLines");
				chngcOrderEle1.appendChild(chngcOOrdLinesEle1);

				if(log.isDebugEnabled()){
					log.debug("fOrderEle_OrderType:" + fOrderEle.getAttribute("OrderType"));
					log.debug("cOrderEle_OrderType:" + cOrderEle.getAttribute("OrderType"));
				}

				if (!isOrderPlaceFlag.equalsIgnoreCase("Y")) {
					processFOHold(rootEle, fOrderEle);
				}

				preparefOChange(env, chngcOrderEle0, chngcOrdStatusEle0, chngcOrdStatusEle, rootEle, chngcOrdStatusEle1, chngcOrderEle1, fOrderEle, cOrderEle);
				
				// Updates customer and fulfillment order.
				returnToLegacyDoc = updatefOrder(env, chngcOrderEle0, chngcOrdStatusEle0, chngcOrdStatusEle, rootEle, chngcOrdStatusEle1, chngcOrderEle1, cOrderEle, fOrderEle, cAndfOrderEle);
				this.performOrderStatusChange(env, cAndfOrderEle, cOrderEle, fOrderEle, hdrStatusCode, "C");
				isAPISuccess = true;

			} else if (headerProcessCode.equalsIgnoreCase("D")) {

				this.handleHeaderProcessCodeD(rootEle, fOrderEle);
				if (rootEle.hasAttribute("OrderHeaderKey") && !YFCObject.isNull(rootEle.getAttribute("OrderHeaderKey")) && !YFCObject.isVoid(rootEle.getAttribute("OrderHeaderKey"))) {
					// To Set Instruction Detail Key To Update / Remove The Instruction Text For The Order.
					setInstructionKeys(rootEle, fOrderEle);
					// To Calculate the Price Information For The FO.
					setExtendedPriceInfo(env, rootEle, false);
					filterAttributes(rootEle, false);					
					
					if(log.isDebugEnabled()){
						log.debug("XPXChangeOrder_FO[HeaderProcessCode:D]-InXML:" + rootEle.getString());
					}
					Document tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrder", rootEle.getOwnerDocument().getDocument());
					if (tempDoc != null) {
						returnToLegacyDoc = YFCDocument.getDocumentFor(tempDoc);
					} else {
						throw new Exception("Service XPXChangeOrder Failed!");
					}
				}

				// To set the transaction Id.
				String tranId0 = null;
				if (this._prop != null) {
					tranId0 = this._prop.getProperty("XPX_CHN_CNL_STATUS_TXN");
					if (YFCObject.isNull(tranId0) || YFCObject.isVoid(tranId0)) {
						throw new Exception("TransactionID Not Configured in API Arguments!");
					}
				} else {
					throw new Exception("Arguments Not Configured For API-XPXPerformLegacyOrderUpdateAPI!");
				}

				YFCDocument chngcOrdStatusInXML = YFCDocument.getDocumentFor("<OrderStatusChange/>");
				YFCElement chngcOrdStatusEle = chngcOrdStatusInXML.getDocumentElement();
				chngcOrdStatusEle.setAttribute("IgnoreTransactionDependencies", "Y");
				chngcOrdStatusEle.setAttribute("TransactionId", tranId0);
				chngcOrdStatusEle.setAttribute("BaseDropStatus", "1100.0010");

				YFCElement chngCOStatusLinesEle = chngcOrdStatusEle.getOwnerDocument().createElement("OrderLines");
				chngcOrdStatusEle.appendChild(chngCOStatusLinesEle);

				Document chngcOrderInXML0 = YFCDocument.createDocument().getDocument();
				chngcOrderInXML0.appendChild(chngcOrderInXML0.importNode(rootEle.getDOMNode(), true));
				chngcOrderInXML0.renameNode(chngcOrderInXML0.getDocumentElement(), chngcOrderInXML0.getNamespaceURI(), "Order");
				YFCElement chngcOrderEle0 = YFCDocument.getDocumentFor(chngcOrderInXML0).getDocumentElement();

				this.handleHeaderProcessCodeD(chngcOrderEle0, chngcOrdStatusEle, fOrderEle, cOrderEle);
				if (chngcOrdStatusEle.hasAttribute("OrderHeaderKey")) {

					if(log.isDebugEnabled()){
						log.debug("XPXChangeOrderStatus_CO[HeaderProcessCode:D]-InXML:" + chngcOrdStatusEle.getString());
					}
					Document tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrderStatus", chngcOrdStatusEle.getOwnerDocument().getDocument());
					if (tempDoc != null) {
						// Do Nothing
					} else {
						throw new Exception("Service XPXChangeOrder Failed!");
					}
				}

				if (chngcOrderEle0.hasAttribute("OrderHeaderKey")) {
					
					setExtendedPriceInfoCO(chngcOrderEle0, returnToLegacyDoc.getDocumentElement(), cAndfOrderEle, headerProcessCode);
					filterAttributes(chngcOrderEle0, true);

					if(log.isDebugEnabled()){
						log.debug("XPXChangeOrder_CO[HeaderProcessCode:D]-InXML:" + chngcOrderEle0.getString());
					}
					Document tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrderEx", chngcOrderEle0.getOwnerDocument().getDocument());
					if (tempDoc == null) {
						throw new Exception("Service XPXChangeOrder Failed to Update Customer Order!");
					} 
				}
				if (returnToLegacyDoc == null) {
					throw new Exception("Could Not Perform Fulfillment Order Cancel!");
				}
				isAPISuccess = true;

			} else if (headerProcessCode.equalsIgnoreCase("S")) {

				// To update the status of the order.
				this.performOrderStatusChange(env, cAndfOrderEle, cOrderEle, fOrderEle, hdrStatusCode, "S");

				// Input xml has been passed as the output after updating the status.
				returnToLegacyDoc = YFCDocument.getDocumentFor(inXML);
				isAPISuccess = true;

			} else {
				throw new Exception("Invalid HeaderProcessCode in the Incoming Legacy Message!");
			}
		} catch (Exception ex) {
			
			if(log.isDebugEnabled()){
				log.debug("Exception Description:" + ex.getMessage());
			}
			ex.printStackTrace();
			APIException = ex;
			if(!centExempt){
				prepareErrorObject(ex, XPXLiterals.OU_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inXML);
			}
			// Added by Prasanth Kumar M. to prevent rollback of the orders if this code is invoked in OPResponse flow
			if(log.isDebugEnabled()){
				log.debug("IsOrderPlaceFlag:" + isOrderPlaceFlag);
				log.debug("Boolean:" + (!"Y".equalsIgnoreCase(isOrderPlaceFlag)));
			}
			if (!"Y".equalsIgnoreCase(isOrderPlaceFlag)) {
				((YFSContext) env).rollback();
				try {
					// Try to update only the order header information on exception case.
					if (isExceptionScenario) {
						updateOrderHeaderDetails(env, cAndfOrderEle, cOrderEle, fOrderEle, rootEle, headerProcessCode, hdrStatusCode);
					} else {
						updateOrderKeys(env, rootEle, fOrderEle);
					}
				} catch (Exception ex1) {
					ex1.printStackTrace();
				}
			}
		} finally {
			if (isAPISuccess) {
				YFCElement returnToLegacyEle = returnToLegacyDoc.getDocumentElement();
				returnToLegacyEle.setAttribute("TransactionMessage", "");
				// Transaction status 'P' refers to Pass.
				returnToLegacyEle.setAttribute("TransactionStatus", "P");
			} else {
				returnToLegacyDoc = YFCDocument.getDocumentFor(inXML);
				YFCElement returnToLegacyEle = returnToLegacyDoc.getDocumentElement();
				if (!YFCObject.isNull(APIException)) {
					returnToLegacyEle.setAttribute("TransactionMessage", APIException.toString());
				} else {
					returnToLegacyEle.setAttribute("TransactionMessage", "");
				}
				returnToLegacyEle.setAttribute("TransactionStatus", "F");
			}
		}

		if(log.isDebugEnabled()){
			log.debug("ReturnToLegacyDoc:" + returnToLegacyDoc);
		}
		return returnToLegacyDoc.getDocument();
	}

	private boolean isOPRProcessed(YFCElement fOrderEle) {
		YFCElement fOrderExtnEle = fOrderEle.getChildElement("Extn");
		if (fOrderExtnEle != null) {
			String legacyOrdNo = fOrderExtnEle.getAttribute("ExtnLegacyOrderNo");
			String genNo = fOrderExtnEle.getAttribute("ExtnGenerationNo");
			String isReprocessibleFlag = fOrderExtnEle.getAttribute("ExtnIsReprocessibleFlag");

			if(log.isDebugEnabled()){
				log.debug("legacyOrderNo:" + legacyOrdNo);
				log.debug("genNo:" + genNo);
				log.debug("isReprocessibleFlag:" + isReprocessibleFlag);
				log.debug("IsOPRProcessed:"
					+ (!YFCObject.isNull(legacyOrdNo) && !YFCObject.isVoid(legacyOrdNo) && !YFCObject.isNull(genNo) && !YFCObject.isVoid(genNo) && !YFCObject.isNull(isReprocessibleFlag)
							&& !YFCObject.isVoid(isReprocessibleFlag) && isReprocessibleFlag.equalsIgnoreCase("N")));
			}

			if (!YFCObject.isNull(legacyOrdNo) && !YFCObject.isVoid(legacyOrdNo) && !YFCObject.isNull(genNo) && !YFCObject.isVoid(genNo) && !YFCObject.isNull(isReprocessibleFlag)
					&& !YFCObject.isVoid(isReprocessibleFlag) && isReprocessibleFlag.equalsIgnoreCase("N")) {
				return true;
			}
		}

		return false;
	}

	private void updateOrderHeaderDetails(YFSEnvironment env, YFCElement cAndfOrderEle, YFCElement cOrderEle, YFCElement fOrderEle, YFCElement inXMLEle, String headerProcessCode, String hdrStatusCode) {

		YFCElement remEle = null;

		try {
			if (!YFCObject.isNull(headerProcessCode) && !YFCObject.isVoid(headerProcessCode) && !headerProcessCode.equalsIgnoreCase("A")) {

				// To remove the line level information.
				remEle = inXMLEle.getChildElement("OrderLines");
				if (remEle != null) {
					inXMLEle.removeChild(remEle);
				}
				// To update Order and line level instructions.
				YFCElement instElement = inXMLEle.getChildElement("Instructions");
				if (instElement != null) {
					setInstructionKeysOnException(inXMLEle, fOrderEle);
				}

				XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrder", inXMLEle.getOwnerDocument().getDocument());

				// To update the status of the order.
				this.performOrderStatusChange(env, cAndfOrderEle, cOrderEle, fOrderEle, hdrStatusCode, headerProcessCode);
			}
		} catch (Exception ex) {

			if (remEle != null) {
				inXMLEle.appendChild(remEle);
			}
			// To update key fields such as legacy order number,legacy line
			// number and generation number.
			updateOrderKeys(env, inXMLEle, fOrderEle);
		}
	}

	private void updateOrderKeys(YFSEnvironment env, YFCElement inXMLEle, YFCElement fOrderEle) {
		if (fOrderEle != null) {
			YFCElement chngOrderEle = YFCDocument.getDocumentFor("<Order><Extn/></Order>").getDocumentElement();
			YFCElement chngOrdExtnEle = chngOrderEle.getChildElement("Extn");
			try {
				String inXMLOrdHeaderKey = null;
				String fOrdHeaderKey = null;
				String inXMLWebConfNum = null;
				String inXMLLegacyOrdNo = null;
				String inXMLGenNo = null;
				String fOrderWebConfNum = null;
				String fOrderLegacyOrdNo = null;
				String fOrderGenNo = null;

				inXMLOrdHeaderKey = inXMLEle.getAttribute("OrderHeaderKey");
				fOrdHeaderKey = fOrderEle.getAttribute("OrderHeaderKey");
				YFCElement inXMLExtnEle = inXMLEle.getChildElement("Extn");
				YFCElement fOrderExtnEle = fOrderEle.getChildElement("Extn");
				if (inXMLExtnEle != null && fOrderExtnEle != null) {
					inXMLWebConfNum = inXMLExtnEle.getAttribute("ExtnWebConfNum");
					inXMLLegacyOrdNo = inXMLExtnEle.getAttribute("ExtnLegacyOrderNo");
					inXMLGenNo = inXMLExtnEle.getAttribute("ExtnGenerationNo");
					fOrderWebConfNum = fOrderExtnEle.getAttribute("ExtnWebConfNum");
					fOrderLegacyOrdNo = fOrderExtnEle.getAttribute("ExtnLegacyOrderNo");
					fOrderGenNo = fOrderExtnEle.getAttribute("ExtnGenerationNo");
				}

				String headerProcessCode = null;
				if (inXMLEle.hasAttribute("HeaderProcessCode")) {
					headerProcessCode = inXMLEle.getAttribute("HeaderProcessCode");
					if (YFCObject.isNull(headerProcessCode) || YFCObject.isVoid(headerProcessCode)) {
						throw new Exception("Attribute HeaderProcessCode Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute HeaderProcessCode Not Available in Incoming Legace Message!");
				}

				if(log.isDebugEnabled()){
					log.debug("inXMLOrdHeaderKey:" + inXMLOrdHeaderKey);
					log.debug("fOrdHeaderKey:" + fOrdHeaderKey);
					log.debug("inXMLWebConfNum:" + inXMLWebConfNum);
					log.debug("inXMLLegacyOrdNo:" + inXMLLegacyOrdNo);
					log.debug("inXMLGenNo:" + inXMLGenNo);
					log.debug("fOrderWebConfNum:" + fOrderWebConfNum);
					log.debug("fOrderLegacyOrdNo:" + fOrderLegacyOrdNo);
					log.debug("fOrderGenNo:" + fOrderGenNo);
					log.debug("Boolean1:" + (!YFCObject.isNull(fOrdHeaderKey) && !YFCObject.isVoid(fOrdHeaderKey)));
					log.debug("Boolean2:"
						+ (!YFCObject.isNull(inXMLWebConfNum) && !YFCObject.isVoid(inXMLWebConfNum) && !YFCObject.isNull(inXMLLegacyOrdNo) && !YFCObject.isVoid(inXMLLegacyOrdNo)
								&& !YFCObject.isNull(fOrderWebConfNum) && !YFCObject.isVoid(fOrderWebConfNum) && !YFCObject.isNull(fOrderLegacyOrdNo) && !YFCObject.isVoid(fOrderLegacyOrdNo)));
				}
				if (!YFCObject.isNull(fOrdHeaderKey)
						&& !YFCObject.isVoid(fOrdHeaderKey)
						|| (!YFCObject.isNull(inXMLWebConfNum) && !YFCObject.isVoid(inXMLWebConfNum) && !YFCObject.isNull(inXMLLegacyOrdNo) && !YFCObject.isVoid(inXMLLegacyOrdNo)
								&& !YFCObject.isNull(fOrderWebConfNum) && !YFCObject.isVoid(fOrderWebConfNum) && !YFCObject.isNull(fOrderLegacyOrdNo) && !YFCObject.isVoid(fOrderLegacyOrdNo))
						&& !YFCObject.isNull(inXMLGenNo) && !YFCObject.isVoid(inXMLGenNo) && !YFCObject.isNull(fOrderGenNo) && !YFCObject.isVoid(fOrderGenNo)) {

					if (YFCObject.isNull(inXMLOrdHeaderKey) || YFCObject.isVoid(inXMLOrdHeaderKey)) {
						inXMLOrdHeaderKey = this.getOrdHeaderKeyForWebConfNumAndLegOrdNo(inXMLWebConfNum, inXMLLegacyOrdNo, fOrderEle);
						if (YFCObject.isNull(inXMLOrdHeaderKey) || YFCObject.isVoid(inXMLOrdHeaderKey)) {
							if(log.isDebugEnabled()){
								log.debug("Keys Fields Missing. Returning Without Updating Order keys!");
							}
							return;
						}
					}
					if (!inXMLOrdHeaderKey.equalsIgnoreCase(fOrdHeaderKey)) {
						if(log.isDebugEnabled()){
							log.debug("Keys Fields Missing. Returning Without Updating Order keys!");
						}
						return;
					}

					chngOrderEle.setAttribute("OrderHeaderKey", inXMLOrdHeaderKey);
					chngOrdExtnEle.setAttribute("ExtnWebConfNum", inXMLWebConfNum);
					chngOrdExtnEle.setAttribute("ExtnLegacyOrderNo", inXMLLegacyOrdNo);
					chngOrdExtnEle.setAttribute("ExtnIsReprocessibleFlag", "N");

					String isOrderUpdate = inXMLEle.getAttribute("IsOrderUpdate");
					if (!YFCObject.isNull(isOrderUpdate) && !YFCObject.isVoid(isOrderUpdate) && isOrderUpdate.equalsIgnoreCase("Y") && !headerProcessCode.equalsIgnoreCase("A")) {
						if (!isOPRProcessed(fOrderEle)) {
							YFCElement ordHoldTypesEle = chngOrderEle.getOwnerDocument().createElement("OrderHoldTypes");
							chngOrderEle.appendChild(ordHoldTypesEle);
							YFCElement ordHoldTypeEle = ordHoldTypesEle.getOwnerDocument().createElement("OrderHoldType");
							ordHoldTypesEle.appendChild(ordHoldTypeEle);
							ordHoldTypeEle.setAttribute("HoldType", "ORDER_EXCEPTION_HOLD");
							ordHoldTypeEle.setAttribute("ReasonText", "Order Exception Hold");
							ordHoldTypeEle.setAttribute("Status", "1100");
						}
					}

					YFCElement inXMLOrdLinesEle = inXMLEle.getChildElement("OrderLines");
					if (inXMLOrdLinesEle != null) {
						YFCIterable<YFCElement> yfcItr = inXMLOrdLinesEle.getChildren("OrderLine");
						while (yfcItr.hasNext()) {
							String fOrdLineKey = null;
							String inXMLWebLineNo = null;
							String inXMLLegacyLineNo = null;
							YFCElement inXMLOrdLineEle = (YFCElement) yfcItr.next();
							YFCElement inXMLOrdLineExtnEle = inXMLOrdLineEle.getChildElement("Extn");
							if (inXMLOrdLineExtnEle != null) {
								inXMLWebLineNo = inXMLOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
								inXMLLegacyLineNo = inXMLOrdLineExtnEle.getAttribute("ExtnLegacyLineNumber");
								if (!YFCObject.isNull(inXMLWebLineNo) || !YFCObject.isVoid(inXMLWebLineNo)) {
									fOrdLineKey = this.getOrderLineKeyForWebLineNo(inXMLWebLineNo, fOrderEle);
									if (YFCObject.isNull(fOrdLineKey) && YFCObject.isVoid(fOrdLineKey)) {
										continue;
									}
								} else {
									continue;
								}
							}
							YFCElement chngOrdLinesEle = chngOrderEle.getChildElement("OrderLines");
							if (chngOrdLinesEle != null) {
								YFCElement chngOrdLineEle = chngOrdLinesEle.getOwnerDocument().createElement("OrderLine");
								chngOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);
								YFCElement chngOrdLineExtnEle = chngOrdLineEle.getOwnerDocument().createElement("Extn");
								chngOrdLineExtnEle.setAttribute("ExtnWebLineNumber", inXMLWebLineNo);
								chngOrdLineExtnEle.setAttribute("ExtnLegacyLineNumber", inXMLLegacyLineNo);
								chngOrdLineEle.appendChild(chngOrdLineExtnEle);
								chngOrdLinesEle.appendChild(chngOrdLineEle);

							} else {
								chngOrdLinesEle = chngOrderEle.getOwnerDocument().createElement("OrderLines");
								YFCElement chngOrdLineEle = chngOrdLinesEle.getOwnerDocument().createElement("OrderLine");
								chngOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);
								YFCElement chngOrdLineExtnEle = chngOrdLineEle.getOwnerDocument().createElement("Extn");
								chngOrdLineExtnEle.setAttribute("ExtnWebLineNumber", inXMLWebLineNo);
								chngOrdLineExtnEle.setAttribute("ExtnLegacyLineNumber", inXMLLegacyLineNo);
								chngOrdLineEle.appendChild(chngOrdLineExtnEle);
								chngOrdLinesEle.appendChild(chngOrdLineEle);
								chngOrderEle.appendChild(chngOrdLinesEle);
							}
						}
					}
					// call changeOrder API
					if (chngOrderEle.hasAttribute("OrderHeaderKey")) {

						if(log.isDebugEnabled()){
							log.debug("XPXChangeOrder[Update Order Keys]-InXML:" + chngOrderEle.getString());
						}
						XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrder", chngOrderEle.getOwnerDocument().getDocument());
					}
				}
			} catch (Exception ex) {
				log.error("Warning: Updating Order Keys during Exception Failed. Failed changeOrder API-" + chngOrderEle.getString());
			}
		}
	}

	private String getOrdHeaderKeyForWebConfNumAndLegOrdNo(String webConfNum, String legacyOrdNo, YFCElement ordEle) {
		YFCElement ordExtnEle = ordEle.getChildElement("Extn");
		if (ordExtnEle != null) {
			String _webConfNum = ordExtnEle.getAttribute("ExtnWebConfNum");
			String _legacyOrdNo = ordExtnEle.getAttribute("ExtnLegacyOrderNo");
			if (!YFCObject.isNull(_webConfNum) && !YFCObject.isVoid(_legacyOrdNo)) {
				if (webConfNum.equalsIgnoreCase(_webConfNum) && legacyOrdNo.equalsIgnoreCase(_legacyOrdNo)) {
					return ordEle.getAttribute("OrderHeaderKey");
				}
			}
		}
		return null;
	}

	/**
	 * This method prepares the error object with the exception details which in
	 * turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML) {
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		errorObject.setExceptionMessage(e.getMessage());
		ErrorLogger.log(errorObject, env);
	}

	private void setOrderHeaderAttributes(YFSEnvironment env, YFCElement rootEle, YFCElement cOrderEle) throws Exception {

		String _envId = null;
		String _compId = null;
		String _custNo = null;

		// To retreive header attributes from a customer order.
		if (cOrderEle != null) {
			YFCElement custExtnEle = cOrderEle.getChildElement("Extn");
			if (custExtnEle != null) {
				if (custExtnEle.hasAttribute("ExtnCustomerNo")) {
					_custNo = custExtnEle.getAttribute("ExtnCustomerNo");
					if (YFCObject.isNull(_custNo) || YFCObject.isVoid(_custNo)) {
						throw new Exception("Attribute OrderLine/Extn/@ExtnCustomerNo Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute ExtnCustomerNo Not Available In OrderList Template!");
				}

				if (custExtnEle.hasAttribute("ExtnEnvtId")) {
					_envId = custExtnEle.getAttribute("ExtnEnvtId");
					if (YFCObject.isNull(_envId) || YFCObject.isVoid(_envId)) {
						throw new Exception("Attribute OrderLine/Extn/@ExtnEnvtId Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute ExtnEnvId Not Available In OrderList Template!!");
				}

				if (custExtnEle.hasAttribute("ExtnCompanyId")) {
					_compId = custExtnEle.getAttribute("ExtnCompanyId");
					if (YFCObject.isNull(_compId) || YFCObject.isVoid(_compId)) {
						throw new Exception("Attribute OrderLine/Extn/@ExtnCompanyId Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute ExtnCompanyId Not Available In OrderList Template!");
				}
			} else {
				throw new Exception("OrderLine/Extn Element Not Available In OrderList Template!");
			}
		}

		if(log.isDebugEnabled()){
			log.debug("CO_ExtnCustomerNo:" + _custNo);
			log.debug("CO_ExtnEnvtId:" + _envId);
			log.debug("CO_ExtnCompanyId:" + _compId);
		}

		String envId = null;
		String compId = null;
		String custNo = null;

		// To retrieve header attributes from legacy xml.
		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		if (rootExtnEle != null) {
			if (rootExtnEle.hasAttribute("ExtnCustomerNo")) {
				custNo = rootExtnEle.getAttribute("ExtnCustomerNo");
				if (YFCObject.isNull(custNo) || YFCObject.isVoid(custNo)) {
					if (YFCObject.isNull(_custNo) || YFCObject.isVoid(_custNo)) {
						throw new Exception("Attribute OrderLine/Extn/@ExtnCustomerNo Cannot be NULL or Void!");
					}
					custNo = _custNo;
				}

			} else {
				if (YFCObject.isNull(_custNo) || YFCObject.isVoid(_custNo)) {
					throw new Exception("Attribute OrderLine/Extn/@ExtnCustomerNo Cannot be NULL or Void!");
				}
				custNo = _custNo;
			}

			if (rootExtnEle.hasAttribute("ExtnEnvtId")) {
				envId = rootExtnEle.getAttribute("ExtnEnvtId");
				if (YFCObject.isNull(envId) || YFCObject.isVoid(envId)) {
					if (YFCObject.isNull(_envId) || YFCObject.isVoid(_envId)) {
						throw new Exception("Attribute OrderLine/Extn/@ExtnEnvtId Cannot be NULL or Void!");
					}
					envId = _envId;
				}

			} else {
				if (YFCObject.isNull(_envId) || YFCObject.isVoid(_envId)) {
					throw new Exception("Attribute OrderLine/Extn/@ExtnEnvtId Cannot be NULL or Void!");
				}
				envId = _envId;
			}

			if (rootExtnEle.hasAttribute("ExtnCompanyId")) {
				compId = rootExtnEle.getAttribute("ExtnCompanyId");
				if (YFCObject.isNull(compId) || YFCObject.isVoid(compId)) {
					if (YFCObject.isNull(_compId) || YFCObject.isVoid(_compId)) {
						throw new Exception("Attribute OrderLine/Extn/@ExtnCompanyId Cannot be NULL or Void!");
					}
					compId = _compId;
				}

			} else {
				if (YFCObject.isNull(_compId) || YFCObject.isVoid(_compId)) {
					throw new Exception("Attribute OrderLine/Extn/@ExtnCompanyId Cannot be NULL or Void!");
				}
				compId = _compId;
			}
			
			// To get ShipTo Customer Information.
			YFCElement shipToCustElem = getShipToCustomerInformation(env, rootEle);
			
			// To get MSAP Customer Key.
			String rootCustKey = null;
			if (shipToCustElem.hasAttribute("RootCustomerKey")) {
				rootCustKey = shipToCustElem.getAttribute("RootCustomerKey");
				if (YFCObject.isNull(rootCustKey) || YFCObject.isVoid(rootCustKey)) {
					throw new Exception("Attribute RootCustomerKey Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute RootCustomerKey Not Available in getCustomerList Template!");
			}
			
			// To get MSAP Customer Details.
			YFCElement msapCustElem = getMSAPCustomerInformation(env, rootCustKey);		
						
			// To Set MSAP Customer Information.
			if (rootExtnEle.hasAttribute("ExtnBillToName")) {
				String extnBillToName = rootExtnEle.getAttribute("ExtnBillToName");
				if (YFCObject.isNull(extnBillToName) || YFCObject.isVoid(extnBillToName)) {
					rootExtnEle.setAttribute("ExtnBillToName", getBillToName(msapCustElem));
				}
			} else {
				rootExtnEle.setAttribute("ExtnBillToName", getBillToName(msapCustElem));
			}

			if (rootEle.hasAttribute("BillToID")) {
				String billToID = rootEle.getAttribute("BillToID");
				if (YFCObject.isNull(billToID) || YFCObject.isVoid(billToID)) {	
					rootEle.setAttribute("BillToID", getBillToID(msapCustElem));
				}
			} else {
				rootEle.setAttribute("BillToID", getBillToID(msapCustElem));
			}

			// To Set ShipTo Customer Information.
			if (rootExtnEle.hasAttribute("ExtnSAPParentName")) {
				String sapParentName = rootExtnEle.getAttribute("ExtnSAPParentName");
				if (YFCObject.isNull(sapParentName) || YFCObject.isVoid(sapParentName)) {
					rootExtnEle.setAttribute("ExtnSAPParentName", getSAPParentName(shipToCustElem));
				}
			} else {
				rootExtnEle.setAttribute("ExtnSAPParentName", getSAPParentName(shipToCustElem));
			}

			if (rootExtnEle.hasAttribute("ExtnOrigEnvironmentCode")) {
				String orgEnvCode = rootExtnEle.getAttribute("ExtnOrigEnvironmentCode");
				if (YFCObject.isNull(orgEnvCode) || YFCObject.isVoid(orgEnvCode)) {
					rootExtnEle.setAttribute("ExtnOrigEnvironmentCode", getOrigEnvCode(shipToCustElem));
				}
			} else {
				rootExtnEle.setAttribute("ExtnOrigEnvironmentCode", getOrigEnvCode(shipToCustElem));
			}

			if (rootEle.hasAttribute("BuyerOrganizationCode")) {
				String buyerOrgCode = rootEle.getAttribute("BuyerOrganizationCode");
				if (YFCObject.isNull(buyerOrgCode) || YFCObject.isVoid(buyerOrgCode)) {
					rootEle.setAttribute("BuyerOrganizationCode", this.getBuyerOrgCode(shipToCustElem));
				}
			} else {
				rootEle.setAttribute("BuyerOrganizationCode", this.getBuyerOrgCode(shipToCustElem));
			}

			if (rootEle.hasAttribute("ShipToID")) {
				String shipToID = rootEle.getAttribute("ShipToID");
				if (YFCObject.isNull(shipToID) || YFCObject.isVoid(shipToID)) {
					rootEle.setAttribute("ShipToID", this.getShipToID(shipToCustElem));
				}
			} else {
				rootEle.setAttribute("ShipToID", this.getShipToID(shipToCustElem));
			}

			if (!rootEle.hasAttribute("EntryType")) {
				rootEle.setAttribute("EntryType", "Web");
			}

			if (!rootEle.hasAttribute("CarrierServiceCode")) {
				rootEle.setAttribute("CarrierServiceCode", "Standard Shipping");
			}
		} else {
			throw new Exception("OrderLine/Extn Element Not Available in Incoming Legacy Message!");
		}
		
		// To Get The List Of Customer Part Numbers From Every Order Line.
		Set<String> custPartNoSet = new HashSet<String>();
		HashMap<String,String> legacyItemNoMap = new HashMap<String,String>();
		HashMap<String,YFCDocument> itemDetailsMap = new HashMap<String,YFCDocument>();
		YFCElement rootOrdLinesEle_ = rootEle.getChildElement("OrderLines");
		if (rootOrdLinesEle_ != null) {
			YFCIterable<YFCElement> yfcItr_ = rootOrdLinesEle_.getChildren("OrderLine");
			while (yfcItr_.hasNext()) {	
				YFCElement rootOrdLineEle_ = (YFCElement) yfcItr_.next();
				YFCElement rootOrdLineItemEle = rootOrdLineEle_.getChildElement("Item");
				if (rootOrdLineItemEle != null) {
					if (rootOrdLineItemEle.hasAttribute("CustomerItem")) {
						String custPartNo = rootOrdLineItemEle.getAttribute("CustomerItem");
						if (!YFCObject.isNull(custPartNo) && !YFCObject.isVoid(custPartNo)) {
							custPartNoSet.add(custPartNo);
						}
					}
				}
			}
		}
		
		// To Get The Legacy Item Number Map For The Customer Part Number.
		if ( custPartNoSet != null && !custPartNoSet.isEmpty()) {
			legacyItemNoMap = getLegacyItemNoFromXRef(env, compId, envId, custNo, custPartNoSet);
		}
		
		// To Get Line Type Of An Item From Inventory Indicator.
		itemDetailsMap = getItemDetails(env, rootEle, envId);

		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if (rootOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				String isNewLine = null;
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				if (rootOrdLineEle.hasAttribute("IsNewLine")) {
					isNewLine = rootOrdLineEle.getAttribute("IsNewLine");
					if (YFCObject.isNull(isNewLine) || YFCObject.isVoid(isNewLine)) {
						isNewLine = "Y";
					}
				} else {
					isNewLine = "Y";
				}
				if (isNewLine.equalsIgnoreCase("Y")) {
					setOrderLineAttributes(env, rootOrdLineEle, compId, envId, custNo, legacyItemNoMap, itemDetailsMap);
				}
			}
		} else {
			throw new Exception("Element OrderLines Not Available In Incoming Legacy Message!");
		}
	}

	private void setOrderLineAttributes(YFSEnvironment env, YFCElement rootOrdLineEle, String compId, String envId, 
			String custNo, HashMap<String,String> legacyItemNoMap, HashMap<String,YFCDocument> itemDetailsMap ) throws Exception {

		String lineType = null;
		String legacyItemNo = null;
		String itemId = null;
		String shipNode = null;

		if (rootOrdLineEle.hasAttribute("LineType")) {
			lineType = rootOrdLineEle.getAttribute("LineType");
			if (YFCObject.isNull(lineType) || YFCObject.isVoid(lineType)) {
				throw new Exception("Attribute OrderLine/@LineType Cannot be NULL or Void!");
			}
			if (lineType.equalsIgnoreCase("C") || lineType.equalsIgnoreCase("M")) {
				rootOrdLineEle.setAttribute("ValidateItem", "N");
			} else {
				rootOrdLineEle.setAttribute("ValidateItem", "N");
			}
			if (lineType.equalsIgnoreCase("S")) {
				String custPartNo = null;
				YFCElement rootOrdLineItemEle = rootOrdLineEle.getChildElement("Item");
				if (rootOrdLineItemEle != null) {
					if (rootOrdLineItemEle.hasAttribute("CustomerItem")) {
						custPartNo = rootOrdLineItemEle.getAttribute("CustomerItem");
						if (!YFCObject.isNull(custPartNo) && !YFCObject.isVoid(custPartNo) && legacyItemNoMap != null) {
							legacyItemNo = legacyItemNoMap.get(custPartNo);							
							if (!YFCObject.isNull(legacyItemNo) && !YFCObject.isVoid(legacyItemNo)) {
								rootOrdLineItemEle.setAttribute("ItemID", legacyItemNo);
							}
						}
					}
				} else {
					throw new Exception("Element OrderLine/Item Not Available in Incoming Legacy Message!");
				}
			}
		} else {
			throw new Exception("Attribute OrderLine/@LineType Not Available in Incoming Legacy Message!");
		}
		
		itemId = getItemID(rootOrdLineEle);
		shipNode = getShipNode(rootOrdLineEle);
		
		YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
		if (rootOrdLineExtnEle != null) {
				String extnLineType = getStockType(env, itemId, shipNode, lineType, itemDetailsMap);
				if (!YFCObject.isNull(extnLineType) && !YFCObject.isVoid(extnLineType)) {
					rootOrdLineExtnEle.setAttribute("ExtnLineType", extnLineType);
				} else {
					throw new Exception("Item# "+itemId+" Doesn't Exist In Web.");
				}
		} else {
			throw new Exception("OrderLine/Extn Element Not Available in Incoming Legacy Message!");
		}
	}
	
	private void setOrderLineAttributes(YFSEnvironment env, YFCElement rootOrdLineEle, String compId, String envId, String custNo) throws Exception {

		String lineType = null;
		String legacyItemNo = null;
		String shipNode = null;
		String itemId = null;

		if (rootOrdLineEle.hasAttribute("LineType")) {
			lineType = rootOrdLineEle.getAttribute("LineType");
			if (YFCObject.isNull(lineType) || YFCObject.isVoid(lineType)) {
				throw new Exception("Attribute OrderLine/@LineType Cannot be NULL or Void!");
			}
			if (lineType.equalsIgnoreCase("C") || lineType.equalsIgnoreCase("M")) {
				rootOrdLineEle.setAttribute("ValidateItem", "N");
			} else {
				rootOrdLineEle.setAttribute("ValidateItem", "N");
			}
			if (lineType.equalsIgnoreCase("S")) {
				String custPartNo = null;
				YFCElement rootOrdLineItemEle = rootOrdLineEle.getChildElement("Item");
				if (rootOrdLineItemEle != null) {
					if (rootOrdLineItemEle.hasAttribute("CustomerItem")) {
						custPartNo = rootOrdLineItemEle.getAttribute("CustomerItem");
						if (!YFCObject.isNull(custPartNo) && !YFCObject.isVoid(custPartNo)) {
							legacyItemNo = getLegacyItemNoFromXRef(env, compId, envId, custNo, custPartNo);
							if (!YFCObject.isNull(legacyItemNo) && !YFCObject.isVoid(legacyItemNo)) {
								rootOrdLineItemEle.setAttribute("ItemID", legacyItemNo);
							}
						}
					}
				} else {
					throw new Exception("Element OrderLine/Item Not Available in Incoming Legacy Message!");
				}
			}
		} else {
			throw new Exception("Attribute OrderLine/@LineType Not Available in Incoming Legacy Message!");
		}

		if (rootOrdLineEle.hasAttribute("ShipNode")) {
			shipNode = rootOrdLineEle.getAttribute("ShipNode");
			if (YFCObject.isNull(shipNode) || YFCObject.isVoid(shipNode)) {
				throw new Exception("Attribute OrderLine/@ShipNode Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute OrderLine/@ShipNode Not Available in Incoming Legacy Message!");
		}

		YFCElement rootOrdLineItemEle = rootOrdLineEle.getChildElement("Item");
		if (rootOrdLineItemEle != null) {
			if (rootOrdLineItemEle.hasAttribute("ItemID")) {
				itemId = rootOrdLineItemEle.getAttribute("ItemID");
				if (YFCObject.isNull(itemId) || YFCObject.isVoid(itemId)) {
					throw new Exception("Attribute ItemID Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ItemID Not Available in Incoming Legacy Message!");
			}
		} else {
			throw new Exception("Element OrderLine/Item Not Available in Incoming Legacy Message!");
		}

		YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
		if (rootOrdLineExtnEle != null) {
			String extnLineType = getInventoryIndicator(env, itemId, shipNode, envId, lineType);
			if (!YFCObject.isNull(extnLineType) && !YFCObject.isVoid(extnLineType)) {
				rootOrdLineExtnEle.setAttribute("ExtnLineType", extnLineType);
			} else {
				throw new Exception("Item# "+itemId+" Doesn't Exist In Web.");
			}

		} else {
			throw new Exception("OrderLine/Extn Element Not Available in Incoming Legacy Message!");
		}
	}

	private void handleHeaderProcessCodeD(YFCElement rootEle, YFCElement fOrderEle) throws Exception {

		String isOrdPlace = "N";
		if (rootEle.hasAttribute("IsOrderPlace")) {
			isOrdPlace = rootEle.getAttribute("IsOrderPlace");
			if (YFCObject.isNull(isOrdPlace) || YFCObject.isVoid(isOrdPlace)) {
				isOrdPlace = "N";
			}
		}

		if (fOrderEle.hasAttribute("OrderHeaderKey")) {
			String ordHeaderKey = fOrderEle.getAttribute("OrderHeaderKey");
			if (YFCObject.isNull(ordHeaderKey) || YFCObject.isVoid(ordHeaderKey)) {
				throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
			}
			rootEle.setAttribute("OrderHeaderKey", ordHeaderKey);
			rootEle.setAttribute("Override", "Y");

			if (isOrdPlace.equalsIgnoreCase("Y")) {
				YFCElement ordHoldTypesEle = rootEle.getOwnerDocument().createElement("OrderHoldTypes");
				rootEle.appendChild(ordHoldTypesEle);
				YFCElement ordHoldTypeEle = ordHoldTypesEle.getOwnerDocument().createElement("OrderHoldType");
				ordHoldTypesEle.appendChild(ordHoldTypeEle);
				ordHoldTypeEle.setAttribute("HoldType", "LEGACY_CNCL_ORD_HOLD");
				ordHoldTypeEle.setAttribute("ReasonText", "Legacy Request For Order Cancel");
				ordHoldTypeEle.setAttribute("Status", "1100");
			}
		} else {
			throw new Exception("Attribute OrderHeaderKey Not Available in OrderList Template!");
		}

		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if (rootOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			// If we don't receive order line information from Legacy.
			if (!yfcItr.hasNext()) {
				
				// Appending the order lines from database as there were no order lines sent from legacy.			
				YFCElement fOrderLinesElem = fOrderEle.getChildElement("OrderLines");
				if ( fOrderLinesElem == null) {
					throw new Exception("OrderLines Element Not Available in Incoming Legacy Message or in Next Gen!");
				}
				
				// Add order lines from the database.
				yfcItr = fOrderLinesElem.getChildren("OrderLine");
				while (yfcItr.hasNext()) {
					String fOrderLineKey = null;
					String webLineNo = null;
					String tranUOM = null;
					
					YFCElement fOrderLineElem = (YFCElement) yfcItr.next();
					if (fOrderLineElem.hasAttribute("OrderLineKey")) {
						fOrderLineKey = fOrderLineElem.getAttribute("OrderLineKey");
					}
					
					// To get Transactional UOM.
					YFCElement orderLineTranQtyElem = fOrderLineElem.getChildElement("OrderLineTranQuantity");
					if (orderLineTranQtyElem != null && orderLineTranQtyElem.hasAttribute("TransactionalUOM")) {
						tranUOM = orderLineTranQtyElem.getAttribute("TransactionalUOM");
					}
					
					YFCElement fOrderLineExtnElem = fOrderLineElem.getChildElement("Extn");
					if (fOrderLineExtnElem != null) {
						if (fOrderLineExtnElem.hasAttribute("ExtnWebLineNumber")) {
							webLineNo = fOrderLineExtnElem.getAttribute("ExtnWebLineNumber");
						}
					}
					
					YFCElement _fOrderLineElem = rootOrdLinesEle.getOwnerDocument().createElement("OrderLine");
					_fOrderLineElem.setAttribute("OrderLineKey", fOrderLineKey);
					
					YFCElement _fOrdLineTranQtyEle = _fOrderLineElem.createChild("OrderLineTranQuantity");
					if (!isOrdPlace.equalsIgnoreCase("Y")) {		
						_fOrdLineTranQtyEle.setAttribute("OrderedQty", Float.parseFloat("0"));
						if (!YFCObject.isNull(tranUOM) && !YFCObject.isVoid(tranUOM)) {
							_fOrdLineTranQtyEle.setAttribute("TransactionalUOM", tranUOM);
						}
					}
					
					YFCElement _fOrdExtnElem = _fOrderLineElem.createChild("Extn");
					_fOrdExtnElem.setAttribute("ExtnWebLineNumber", webLineNo);
					
					// Reset the Price Information Of the Order To Zero.
					resetPriceToZero(_fOrdExtnElem);
					rootOrdLinesEle.appendChild(_fOrderLineElem);	
				}
			} else {
							
				while (yfcItr.hasNext()) {
					String webLineNo = null;
					YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
					YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
					if (rootOrdLineExtnEle != null) {
						if (rootOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
							webLineNo = rootOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
						}
					}
	
					if(log.isDebugEnabled()){
						log.debug("ExtnWebLineNumber:" + webLineNo);
					}
	
					if (!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo)) {
						String fOrdLineKey = this.getOrderLineKeyForWebLineNo(webLineNo, fOrderEle);
						if (YFCObject.isNull(fOrdLineKey) || YFCObject.isVoid(fOrdLineKey)) {
							rootOrdLinesEle.removeChild(rootOrdLineEle);
						}
						rootOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);
						YFCElement rootOrdLineTranQtyEle = rootOrdLineEle.getChildElement("OrderLineTranQuantity");
						if (!isOrdPlace.equalsIgnoreCase("Y")) {		
							rootOrdLineTranQtyEle.setAttribute("OrderedQty", Float.parseFloat("0"));
						}
					} else {
						rootOrdLinesEle.removeChild(rootOrdLineEle);
					}
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available in Incoming Legacy Message!");
		}
	}

	private void handleHeaderProcessCodeD(YFCElement chngcOrderEle, YFCElement chngcOrdStatusEle, YFCElement fOrderEle, YFCElement cOrderEle) throws Exception {

		String isOrdPlace = "N";
		if (chngcOrderEle.hasAttribute("IsOrderPlace")) {
			isOrdPlace = chngcOrderEle.getAttribute("IsOrderPlace");
			if (YFCObject.isNull(isOrdPlace) || YFCObject.isVoid(isOrdPlace)) {
				isOrdPlace = "N";
			}
		}

		String cOrdHeaderKey = null;
		if (cOrderEle.hasAttribute("OrderHeaderKey")) {
			cOrdHeaderKey = cOrderEle.getAttribute("OrderHeaderKey");
			if (YFCObject.isNull(cOrdHeaderKey) || YFCObject.isVoid(cOrdHeaderKey)) {
				throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
			}
			chngcOrderEle.setAttribute("OrderHeaderKey", cOrdHeaderKey);
			chngcOrderEle.setAttribute("Override", "Y");
			chngcOrderEle.removeAttribute("OrderType");
		} else {
			throw new Exception("Attribute OrderHeaderKey Not Available in OrderList Template!");
		}

		YFCElement chngcOrderLinesEle = chngcOrderEle.getChildElement("OrderLines");
		if (chngcOrderLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = chngcOrderLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement chngcOrderLineEle = (YFCElement) yfcItr.next();
				YFCElement chngcOrderLineExtnEle = chngcOrderLineEle.getChildElement("Extn");
				if (chngcOrderLineExtnEle != null) {
					String cOrdLineKey = null;
					String webLineNo = null;
					if (chngcOrderLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
						webLineNo = chngcOrderLineExtnEle.getAttribute("ExtnWebLineNumber");
					}
					if (!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo)) {
						cOrdLineKey = this.getOrderLineKeyForWebLineNo(webLineNo, cOrderEle);
						if (!YFCObject.isNull(cOrdLineKey) && !YFCObject.isVoid(cOrdLineKey)) {
							chngcOrderLineEle.setAttribute("OrderLineKey", cOrdLineKey);
							chngcOrderLineExtnEle.removeAttribute("ExtnLegacyLineNumber");
							if (isOrdPlace.equalsIgnoreCase("Y")) {
								chngcOrderLineEle.removeAttribute("OrderedQty");
								YFCElement chngcOrderLineTranQtyEle = chngcOrderLineEle.getChildElement("OrderLineTranQuantity");
								chngcOrderLineEle.removeChild(chngcOrderLineTranQtyEle);
							} else {
								String cOrdQty = this.getOrderedQtyForWebLineNo(webLineNo, cOrderEle);
								if (YFCObject.isNull(cOrdQty) || YFCObject.isVoid(cOrdQty)) {
									throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
								}
								String fOrdQty = this.getOrderedQtyForWebLineNo(webLineNo, fOrderEle);
								if (YFCObject.isNull(fOrdQty) || YFCObject.isVoid(fOrdQty)) {
									throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
								}
								YFCElement chngcOrderLineTranQtyEle = chngcOrderLineEle.getChildElement("OrderLineTranQuantity");
								chngcOrderLineTranQtyEle.setAttribute("OrderedQty", Float.parseFloat(cOrdQty) - Float.parseFloat(fOrdQty));
								String tuom = chngcOrderLineTranQtyEle.getAttribute("TransactionalUOM");

								chngcOrdStatusEle.setAttribute("OrderHeaderKey", cOrdHeaderKey);
								YFCElement chngcOrdStatusLinesEle = chngcOrdStatusEle.getChildElement("OrderLines");
										
								if ( !fOrdQty.equalsIgnoreCase("0") && !fOrdQty.equalsIgnoreCase("0.0") && !fOrdQty.equalsIgnoreCase("0.00")) {
									YFCElement chngcOrdStatusLineEle = chngcOrdStatusLinesEle.getOwnerDocument().createElement("OrderLine");
									chngcOrdStatusLinesEle.appendChild(chngcOrdStatusLineEle);
									chngcOrdStatusLineEle.setAttribute("OrderLineKey", cOrdLineKey);
	
									YFCElement chngcOrdStatusLineTranQtyEle = chngcOrdStatusLineEle.getOwnerDocument().createElement("OrderLineTranQuantity");
									chngcOrdStatusLineEle.appendChild(chngcOrdStatusLineTranQtyEle);
									chngcOrdStatusLineTranQtyEle.setAttribute("Quantity", Float.parseFloat(fOrdQty));
									if (!YFCObject.isNull(tuom) && !YFCObject.isVoid(tuom)) {
										chngcOrdStatusLineTranQtyEle.setAttribute("TransactionalUOM", tuom);
									}
								}
							}
						} else {
							chngcOrderLinesEle.removeChild(chngcOrderLineEle);
						}
					} else {
						chngcOrderLinesEle.removeChild(chngcOrderLineEle);
					}
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available in Incoming Legacy Message!");
		}
	}

	private YFCDocument updatefOrder(YFSEnvironment env, YFCElement chngcOrderEle, YFCElement chngcOrdStatusEle0, YFCElement chngcOrdStatusEle, YFCElement chngfOrderEle,
			YFCElement chngcOrdStatusEle1, YFCElement chngcOrderEle1, YFCElement cOrderEle, YFCElement fOrderEle, YFCElement cAndfOrderEle) throws Exception {

		YFCDocument retDoc = null;
		Document tempDoc = null;
		
		// Calculate Price Information.
		if(hasOrderLines(chngfOrderEle)) {
			// Fulfillment Order
			setExtendedPriceInfo(env, chngfOrderEle, false);
			// Customer Order.
			/* Both The Change Order Document Needs To Be Checked As There Can be Change Order Call For Customer Order Twice 
			 * When Request From Legacy Has Line Process Codes C And D 
			 * */
			if (chngcOrderEle.hasAttribute("OrderHeaderKey") && !YFCObject.isNull(chngcOrderEle.getAttribute("OrderHeaderKey")) && !YFCObject.isVoid(chngcOrderEle.getAttribute("OrderHeaderKey"))) {
				setExtendedPriceInfoCO(chngcOrderEle, chngfOrderEle, cAndfOrderEle,"C");
			} 
			if (chngcOrderEle1.hasAttribute("OrderHeaderKey") && !YFCObject.isNull(chngcOrderEle1.getAttribute("OrderHeaderKey")) && !YFCObject.isVoid(chngcOrderEle1.getAttribute("OrderHeaderKey"))) {
				setExtendedPriceInfoCO(chngcOrderEle1, chngfOrderEle, cAndfOrderEle,"C");
			}
		} else {
			// Remove the price attributes from the changeOrder XML as price shouldn't be calculated/stamped if line level information is not available.
			if (chngcOrderEle.hasAttribute("OrderHeaderKey") && !YFCObject.isNull(chngcOrderEle.getAttribute("OrderHeaderKey")) && !YFCObject.isVoid(chngcOrderEle.getAttribute("OrderHeaderKey"))) {
				removeExtendedPriceInfoCO(chngcOrderEle);
			} else {
				removeExtendedPriceInfoCO(chngcOrderEle1);
			}
		}

		if (chngcOrdStatusEle0.hasAttribute("OrderHeaderKey") && !YFCObject.isNull(chngcOrdStatusEle0.getAttribute("OrderHeaderKey"))
				&& !YFCObject.isVoid(chngcOrdStatusEle0.getAttribute("OrderHeaderKey"))) {
			if(log.isDebugEnabled()){
			log.debug("XPXChangeOrderStatus_CO-InXML:" + chngcOrdStatusEle0.getString());
			}
			tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrderStatus", chngcOrdStatusEle0.getOwnerDocument().getDocument());
			if (tempDoc == null) {
				throw new Exception("Service XPXChangeOrderStatus Failed!");
			}
		}

		if (chngcOrderEle.hasAttribute("OrderHeaderKey") && !YFCObject.isNull(chngcOrderEle.getAttribute("OrderHeaderKey")) && !YFCObject.isVoid(chngcOrderEle.getAttribute("OrderHeaderKey"))) {
			
			filterAttributes(chngcOrderEle, true);
			if(log.isDebugEnabled()){
			log.debug("XPXChangeOrder_CO-InXML:" + chngcOrderEle.getString());
			}
			tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrder", chngcOrderEle.getOwnerDocument().getDocument());
			if (tempDoc != null) {
				if(log.isDebugEnabled()){
				log.debug("XPXChangeOrderCO[Output]:" + SCXmlUtil.getString(tempDoc));
				}
				cOrderEle = YFCDocument.getDocumentFor(tempDoc).getDocumentElement();
			} else {
				throw new Exception("Service XPXChangeOrder Failed!");
			}
		}

		boolean isChngOrdReq = chaincAndfOrder(chngfOrderEle, cOrderEle);

		if (isChngOrdReq) {
			setInstructionKeys(chngfOrderEle, fOrderEle);			
			filterAttributes(chngfOrderEle, false);
			if(log.isDebugEnabled()){
			log.debug("XPXChangeOrder_FO-InXML:" + chngfOrderEle.getString());
			}
			tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrder", chngfOrderEle.getOwnerDocument().getDocument());
			if (tempDoc != null) {
				fOrderEle = YFCDocument.getDocumentFor(tempDoc).getDocumentElement();
			} else {
				throw new Exception("Service XPXChangeOrder Failed!");
			}
			retDoc = fOrderEle.getOwnerDocument();
		} else {
			retDoc = chngfOrderEle.getOwnerDocument();
		}

		if (chngcOrdStatusEle.hasAttribute("OrderHeaderKey") && !YFCObject.isNull(chngcOrdStatusEle.getAttribute("OrderHeaderKey"))
				&& !YFCObject.isVoid(chngcOrdStatusEle.getAttribute("OrderHeaderKey"))) {

			if(log.isDebugEnabled()){
			log.debug("XPXChangeOrderStatus_CO-InXML:" + chngcOrdStatusEle.getString());
			}
			tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrderStatus", chngcOrdStatusEle.getOwnerDocument().getDocument());
			if (tempDoc == null) {
				throw new Exception("Service XPXChangeOrderStatus Failed!");
			}
		}

		if (chngcOrdStatusEle1.hasAttribute("OrderHeaderKey") && !YFCObject.isNull(chngcOrdStatusEle1.getAttribute("OrderHeaderKey"))
				&& !YFCObject.isVoid(chngcOrdStatusEle1.getAttribute("OrderHeaderKey"))) {
			if(log.isDebugEnabled()){
			log.debug("XPXChangeOrderStatus_CO[LPC:D]-InXML:" + chngcOrdStatusEle1.getString());
			}
			tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrderStatus", chngcOrdStatusEle1.getOwnerDocument().getDocument());
			if (tempDoc == null) {
				throw new Exception("Service XPXChangeOrderStatus Failed!");
			}
		}

		if (chngcOrderEle1.hasAttribute("OrderHeaderKey") && !YFCObject.isNull(chngcOrderEle1.getAttribute("OrderHeaderKey")) && !YFCObject.isVoid(chngcOrderEle1.getAttribute("OrderHeaderKey"))) {
			
			filterAttributes(chngcOrderEle1, true);
			if(log.isDebugEnabled()){
				log.debug("XPXChangeOrder_CO[LPC:D]-InXML:" + chngcOrderEle1.getString());
			}
			tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrder", chngcOrderEle1.getOwnerDocument().getDocument());
			if (tempDoc != null) {
				cOrderEle = YFCDocument.getDocumentFor(tempDoc).getDocumentElement();
			} else {
				throw new Exception("Service XPXChangeOrder Failed!");
			}
		}

		return retDoc;
	}

	private void setExtendedPriceInfo(YFSEnvironment env, YFCElement ordEle, boolean isCustOrder) throws Exception {
		
		String fillAndKill = "";
		if (ordEle.hasAttribute("FillAndKill")) {
			fillAndKill = ordEle.getAttribute("FillAndKill");
			if (YFCObject.isNull(fillAndKill)) {
				fillAndKill = "";
			}
		}								
		
		YFCElement ordExtnEle = ordEle.getChildElement("Extn");
		if (ordExtnEle != null) {
	
			String extnTotOrdVal = null;
			if (ordExtnEle.hasAttribute("ExtnTotalOrderValue")) {
				extnTotOrdVal = ordExtnEle.getAttribute("ExtnTotalOrderValue");
				if (YFCObject.isNull(extnTotOrdVal) || YFCObject.isVoid(extnTotOrdVal)) {
					extnTotOrdVal = "0.0";
				}
			} else {
				extnTotOrdVal = "0.0";
			}

			String extnOrdTax = null;
			if (ordExtnEle.hasAttribute("ExtnOrderTax")) {
				extnOrdTax = ordExtnEle.getAttribute("ExtnOrderTax");
				if (YFCObject.isNull(extnOrdTax) || YFCObject.isVoid(extnOrdTax)) {
					extnOrdTax = "0.0";
				}
			} else {
				extnOrdTax = "0.0";
			}

			String extnTotOrdFreight = null;
			if (ordExtnEle.hasAttribute("ExtnTotalOrderFreight")) {
				extnTotOrdFreight = ordExtnEle.getAttribute("ExtnTotalOrderFreight");
				if (YFCObject.isNull(extnTotOrdFreight) || YFCObject.isVoid(extnTotOrdFreight)) {
					extnTotOrdFreight = "0.0";
				}
			} else {
				extnTotOrdFreight = "0.0";
			}
			double dExtnTotOrdValWithoutTax = Double.parseDouble(extnTotOrdVal) - (Double.parseDouble(extnOrdTax) + Double.parseDouble(extnTotOrdFreight));
			ordExtnEle.setAttribute("ExtnTotOrdValWithoutTaxes", new Double(dExtnTotOrdValWithoutTax).toString());
		}

		double extnOrdSubTotal = 0.0;
		YFCElement ordLineExtnEle = null;
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");			
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				String lineProcessCode = "";
				
				// To retrieve Line Process Code.
				
				if (ordLineEle.hasAttribute("LineProcessCode")) {
					lineProcessCode = ordLineEle.getAttribute("LineProcessCode");
					if (YFCObject.isNull(lineProcessCode) || YFCObject.isVoid(lineProcessCode)) {							
						lineProcessCode = "";
					}
				} else {
					lineProcessCode = "";						
				}
				
				ordLineExtnEle = ordLineEle.getChildElement("Extn");
				if (ordLineExtnEle != null) {
				
					String extnLineOrdTotal = null;
					if (ordLineExtnEle.hasAttribute("ExtnLineOrderedTotal")) {
						extnLineOrdTotal = ordLineExtnEle.getAttribute("ExtnLineOrderedTotal");
						if (YFCObject.isNull(extnLineOrdTotal) || YFCObject.isVoid(extnLineOrdTotal)) {
							extnLineOrdTotal = "0.0";
						}
					} else {
						extnLineOrdTotal = "0.0";
					}
					
					// Add the line total to Order sub total if the line is not being cancelled.
					if ((!lineProcessCode.equalsIgnoreCase("D") && !lineProcessCode.equalsIgnoreCase("_D")) || fillAndKill.equalsIgnoreCase("Y")) {
						extnOrdSubTotal = extnOrdSubTotal + Double.parseDouble(extnLineOrdTotal);
					}

					String extnLegOrdLineAdj = null;
					if (ordLineExtnEle.hasAttribute("ExtnLegOrderLineAdjustments")) {
						extnLegOrdLineAdj = ordLineExtnEle.getAttribute("ExtnLegOrderLineAdjustments");
						if (YFCObject.isNull(extnLegOrdLineAdj) || YFCObject.isVoid(extnLegOrdLineAdj)) {
							extnLegOrdLineAdj = "0.0";
						}
					} else {
						extnLegOrdLineAdj = "0.0";
					}
					ordLineExtnEle.setAttribute("ExtnExtendedPrice", new Double(Double.parseDouble(extnLineOrdTotal) + Double.parseDouble(extnLegOrdLineAdj)).toString());
					ordLineExtnEle.setAttribute("ExtnLegOrderLineAdjustments", extnLegOrdLineAdj);
				}
			}
			
			if (ordExtnEle != null && ! new Double(extnOrdSubTotal).toString().equalsIgnoreCase("0.0")) {
				ordExtnEle.setAttribute("ExtnOrderSubTotal", new Double(extnOrdSubTotal).toString());
			}
			
			if (isCustOrder && ordExtnEle != null) {	
				ordExtnEle.setAttribute("ExtnLegacyOrderNo", "");
				ordExtnEle.setAttribute("ExtnGenerationNo", "");
			}
		}
	}

	private void processFOHold(YFCElement chngOrdEle, YFCElement fOrderEle) {

		YFCElement fOrdHoldTypesEle = fOrderEle.getChildElement("OrderHoldTypes");
		if (fOrdHoldTypesEle != null) {
			YFCIterable<YFCElement> fOrdHoldTypeList = fOrdHoldTypesEle.getChildren("OrderHoldType");
			while (fOrdHoldTypeList.hasNext()) {
				YFCElement fOrdHoldTypeEle = (YFCElement) fOrdHoldTypeList.next();
				if (fOrdHoldTypeEle != null) {
					if (fOrdHoldTypeEle.hasAttribute("HoldType")) {
						String fHoldType = fOrdHoldTypeEle.getAttribute("HoldType");
						if (!(YFCObject.isNull(fHoldType) || YFCObject.isVoid(fHoldType))) {
							if (fHoldType.equalsIgnoreCase("NEEDS_ATTENTION")) {
								String fHoldStatus = fOrdHoldTypeEle.getAttribute("Status");
								if (!(YFCObject.isNull(fHoldStatus) || YFCObject.isVoid(fHoldStatus))) {
									if (fHoldStatus.equalsIgnoreCase("1100")) {
										YFCElement extnOrdEle = chngOrdEle.getChildElement("Extn");
										if (extnOrdEle != null) {
											if (extnOrdEle.hasAttribute("ExtnWebHoldFlag")) {
												String extnWebHold = extnOrdEle.getAttribute("ExtnWebHoldFlag");
												if (!(YFCObject.isNull(extnWebHold) || YFCObject.isVoid(extnWebHold))) {
													if (extnWebHold.equalsIgnoreCase("N")) {
														YFCElement ordHoldTypesEle = chngOrdEle.getOwnerDocument().createElement("OrderHoldTypes");
														chngOrdEle.appendChild(ordHoldTypesEle);
														YFCElement ordHoldTypeEle = chngOrdEle.getOwnerDocument().createElement("OrderHoldType");
														ordHoldTypesEle.appendChild(ordHoldTypeEle);
														ordHoldTypeEle.setAttribute("HoldType", "NEEDS_ATTENTION");
														ordHoldTypeEle.setAttribute("ReasonText", "Hold Resolved by Legacy");
														ordHoldTypeEle.setAttribute("Status", "1300");
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean chaincAndfOrder(YFCElement fOrderEle, YFCElement cOrderEle) throws Exception {
		boolean isChngOrdReq = true;
		YFCElement fOrderLinesEle = fOrderEle.getChildElement("OrderLines");
		if (fOrderLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = fOrderLinesEle.getChildren("OrderLine");
			if (!yfcItr.hasNext()) {
				return true;
			}
			while (yfcItr.hasNext()) {
				YFCElement fOrdLineEle = (YFCElement) yfcItr.next();
				if (fOrdLineEle != null) {
					if (fOrdLineEle.hasAttribute("IngoreLineFromFOChange")) {
						String ignoreLineFromFOChange = fOrdLineEle.getAttribute("IngoreLineFromFOChange");
						if (!YFCObject.isNull(ignoreLineFromFOChange) && !YFCObject.isVoid(ignoreLineFromFOChange)) {
							if (ignoreLineFromFOChange.equalsIgnoreCase("Y")) {
								fOrderLinesEle.removeChild(fOrdLineEle);
								continue;
							}
						}
					}
				} else {
					throw new Exception("OrderLine Element Not Available in Incoming Legacy Message!");
				}
			}
		} else {
			return true;
		}

		fOrderLinesEle = fOrderEle.getChildElement("OrderLines");
		if (fOrderLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = fOrderLinesEle.getChildren("OrderLine");
			if (!yfcItr.hasNext()) {
				return false;
			}
			while (yfcItr.hasNext()) {
				YFCElement fOrdLineEle = (YFCElement) yfcItr.next();
				if (fOrdLineEle != null) {
					YFCElement fOrdLineExtnEle = fOrdLineEle.getChildElement("Extn");
					if (fOrdLineExtnEle != null) {
						if (fOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
							String _webLineNo = fOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							String cOrdHeaderKey = this.getOrderHeaderKeyForWebLineNo(_webLineNo, cOrderEle);
							String cOrdLineKey = this.getOrderLineKeyForWebLineNo(_webLineNo, cOrderEle);
							if (YFCObject.isNull(cOrdHeaderKey) || YFCObject.isVoid(cOrdHeaderKey) || YFCObject.isNull(cOrdLineKey) || YFCObject.isVoid(cOrdLineKey)) {
								throw new Exception("CO OrderHeaderKey or OrderLineKey Cannot be NULL or Void!");
							}
							if (fOrdLineEle.hasAttribute("IsNewLine")) {
								String isNewLine = fOrdLineEle.getAttribute("IsNewLine");
								if (YFCObject.isNull(isNewLine) || YFCObject.isVoid(isNewLine)) {
									throw new Exception("Attribute IsNewLine Cannot be NULL or Void!");
								}
								if (isNewLine.equalsIgnoreCase("Y")) {

									YFCElement fChainedFromEle = fOrderEle.getOwnerDocument().createElement("ChainedFrom");
									fOrdLineEle.appendChild(fChainedFromEle);
									fChainedFromEle.setAttribute("OrderHeaderKey", cOrdHeaderKey);
									fChainedFromEle.setAttribute("OrderLineKey", cOrdLineKey);
								} else {

									fOrdLineEle.setAttribute("ChainedFromOrderHeaderKey", cOrdHeaderKey);
									fOrdLineEle.setAttribute("ChainedFromOrderLineKey", cOrdLineKey);
								}
							} else {
								throw new Exception("Attribute IsNewLine Not Set in ChangeOrder XML!");
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in Incoming Legacy Message!");
						}
					} else {
						throw new Exception("Extn Element Not Available in Incoming Legacy Message!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in Incoming Legacy Message!");
				}
			}
		}
		return isChngOrdReq;
	}

	private boolean preparefOChange(YFSEnvironment env, YFCElement chngcOrderEle0, YFCElement chngcOrdStatusEle0, YFCElement chngcOrdStatusEle, YFCElement rootEle, YFCElement chngcOrdStatusEle1,
			YFCElement chngcOrderEle1, YFCElement fOrderEle, YFCElement cOrderEle ) throws Exception {

		boolean hasLineA = false;
		boolean hasLineC = false;
		boolean hasLineD = false;
		String fillAndKill = null;

		String fOrdHeaderKey = null;
		if (fOrderEle.hasAttribute("OrderHeaderKey")) {
			fOrdHeaderKey = fOrderEle.getAttribute("OrderHeaderKey");
			if (!YFCObject.isNull(fOrdHeaderKey) && !YFCObject.isVoid(fOrdHeaderKey)) {
				rootEle.setAttribute("OrderHeaderKey", fOrdHeaderKey);
				rootEle.setAttribute("Action", "MODIFY");
				rootEle.setAttribute("Override", "Y");
			} else {
				throw new Exception("Attribute OrderHeaderKey is NULL Or Empty in the FO Details!");
			}
		} else {
			throw new Exception("Attribute OrderHeaderKey is Missing in the OrderList Template!");
		}

		String cOrdHeaderKey = null;
		if (cOrderEle.hasAttribute("OrderHeaderKey")) {
			cOrdHeaderKey = cOrderEle.getAttribute("OrderHeaderKey");
			if (YFCObject.isNull(cOrdHeaderKey) || YFCObject.isVoid(cOrdHeaderKey)) {
				throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute OrderHeaderKey Not Available in OrderList Template!");
		}

		YFCElement chngcOrdLinesEle0 = chngcOrderEle0.getChildElement("OrderLines");

		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if (rootOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			if (!yfcItr.hasNext()) {
				chngcOrderEle0.setAttribute("OrderHeaderKey", cOrdHeaderKey);
				chngcOrderEle0.setAttribute("Override", "Y");
			}
			while (yfcItr.hasNext()) {
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				if (rootOrdLineEle != null) {
					if (rootOrdLineEle.hasAttribute("LineProcessCode")) {
						String lineProcessCode = rootOrdLineEle.getAttribute("LineProcessCode");
						if (YFCObject.isNull(lineProcessCode) || YFCObject.isVoid(lineProcessCode)) {
							throw new Exception("Attribute LineProcessCode Cannot be NULL or Void!");
						}

						String webLineNo = null;
						YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
						if (rootOrdLineExtnEle != null) {
							if (rootOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
								webLineNo = rootOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
								if (YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
									throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
								}
							} else {
								throw new Exception("Attribute ExtnWebLineNumber Not Available in Incoming Legacy Message!");
							}
						} else {
							throw new Exception("Extn Element Not Available in Incoming Legacy Message!");
						}

						String fOrdLineKey = getOrderLineKeyForWebLineNo(webLineNo, fOrderEle);
						String cOrdLineKey = getOrderLineKeyForWebLineNo(webLineNo, cOrderEle);

						if(log.isDebugEnabled()){
							log.debug("preparefoChange_webLineNo:" + webLineNo);
							log.debug("preparefoChange_fOrdLineKey:" + fOrdLineKey);
							log.debug("preparefoChange_cOrdLineKey:" + cOrdLineKey);
						}

						if (lineProcessCode.equalsIgnoreCase("A")) {
							hasLineA = true;
							List<String> webLineNos = this.getWebLineNosToArray(fOrderEle);
							if (webLineNos.contains(webLineNo)) {
								if (!this.IsCancelledLine(webLineNo, fOrderEle)) {
									throw new Exception("Cannot Add New Line To Fulfillment Order With Same ExtnLegacyLineNumber!");
								}
							}

							if (YFCObject.isNull(fOrdLineKey) || YFCObject.isVoid(fOrdLineKey)) {
								if (YFCObject.isNull(cOrdLineKey) || YFCObject.isVoid(cOrdLineKey)) {
									// New Line Not Exists In Customer Order
									chngcOrderEle0.setAttribute("OrderHeaderKey", cOrdHeaderKey);
									chngcOrderEle0.setAttribute("Override", "Y");

									YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
									chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
									chngcOrdLineEle0.setAttribute("Action", "CREATE");

									YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
									if (chngcOrdLineExtnEle0 != null) {
										if (chngcOrdLineExtnEle0.hasAttribute("ExtnLegacyLineNumber")) {
											chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
										}
									}
									this.setExtnLineTypeToOrderLine(env, chngcOrdLineEle0, cOrderEle);

									rootOrdLineEle.setAttribute("Action", "CREATE");
									rootOrdLineEle.setAttribute("IsNewLine", "Y");

								} else {
									// New Line Already Exists In Customer Order
									this.handleLineProcessCodeCandA(env, cOrdHeaderKey, webLineNo, cOrdLineKey, rootOrdLineEle, fOrderEle, cOrderEle, chngcOrdStatusEle0, chngcOrdStatusEle,
											chngcOrderEle0);

									rootOrdLineEle.setAttribute("Action", "CREATE");
									rootOrdLineEle.setAttribute("IsNewLine", "Y");

								}
							} else {
								// WebLineNo Already Exists In Fulfillment Order
								this.handleLineProcessCodeCandA(env, cOrdHeaderKey, webLineNo, cOrdLineKey, rootOrdLineEle, fOrderEle, cOrderEle, chngcOrdStatusEle0, chngcOrdStatusEle, chngcOrderEle0);

								rootOrdLineEle.setAttribute("Action", "MODIFY");
								rootOrdLineEle.setAttribute("IsNewLine", "N");
								rootOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);

							}
						} else if (lineProcessCode.equalsIgnoreCase("C")) {
							hasLineC = true;
							if (YFCObject.isNull(fOrdLineKey) || YFCObject.isVoid(fOrdLineKey)) {
								throw new Exception("ExtnWebLineNumber Does Not Exist in FO. Line Process Code Cannot be C!");
							}
							if (YFCObject.isNull(cOrdLineKey) || YFCObject.isVoid(cOrdLineKey)) {
								throw new Exception("ExtnWebLineNumber Does Not Exist in CO. Line Process Code Cannot be C!");
							}

							this.handleLineProcessCodeCandA(env, cOrdHeaderKey, webLineNo, cOrdLineKey, rootOrdLineEle, fOrderEle, cOrderEle, chngcOrdStatusEle0, chngcOrdStatusEle, chngcOrderEle0);

							rootOrdLineEle.setAttribute("Action", "MODIFY");
							rootOrdLineEle.setAttribute("IsNewLine", "N");
							rootOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);

						} else if (lineProcessCode.equalsIgnoreCase("D")) {
							hasLineD = true;

							rootEle.setAttribute("OrderHeaderKey", fOrdHeaderKey);
							rootOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);
							rootOrdLineEle.setAttribute("IsNewLine", "N");

							String isOrdPlace = "N";
							if (rootEle.hasAttribute("IsOrderPlace")) {
								isOrdPlace = rootEle.getAttribute("IsOrderPlace");
								if (YFCObject.isNull(isOrdPlace) || YFCObject.isVoid(isOrdPlace)) {
									isOrdPlace = "N";
								}
							}

							if (isOrdPlace.equalsIgnoreCase("Y")) {

								rootOrdLineEle.removeAttribute("OrderedQty");
								rootOrdLineEle.setAttribute("IngoreLineFromFOChange", "N");
								YFCElement rootOrdLineTranQtyEle = rootOrdLineEle.getChildElement("OrderLineTranQuantity");
								if (rootOrdLineTranQtyEle != null) {
									rootOrdLineEle.removeChild(rootOrdLineTranQtyEle);
								}

								chngcOrderEle0.setAttribute("OrderHeaderKey", cOrdHeaderKey);
								chngcOrderEle0.setAttribute("Override", "Y");

								YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
								chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
								chngcOrdLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
								YFCElement chngcOrdLineExtnEle1 = chngcOrdLineEle0.getChildElement("Extn");
								if (chngcOrdLineExtnEle1 != null) {
									chngcOrdLineExtnEle1.setAttribute("ExtnLegacyLineNumber", "");
								}
								chngcOrdLineEle0.removeAttribute("OrderedQty");
								YFCElement chngcOrdLineTranQtyEle = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
								if (chngcOrdLineTranQtyEle != null) {
									chngcOrdLineEle0.removeChild(chngcOrdLineTranQtyEle);
								}

								YFCElement ordHoldTypesEle0 = rootOrdLineEle.getOwnerDocument().createElement("OrderHoldTypes");
								rootOrdLineEle.appendChild(ordHoldTypesEle0);
								YFCElement ordHoldTypeEle0 = ordHoldTypesEle0.getOwnerDocument().createElement("OrderHoldType");
								ordHoldTypesEle0.appendChild(ordHoldTypeEle0);
								ordHoldTypeEle0.setAttribute("HoldType", "LEGACY_CNCL_LNE_HOLD");
								ordHoldTypeEle0.setAttribute("ReasonText", "Legacy Request For Order Line Cancel");
								ordHoldTypeEle0.setAttribute("Status", "1100");

							} else {
								chngcOrderEle1.setAttribute("OrderHeaderKey", cOrdHeaderKey);
								chngcOrderEle1.setAttribute("Override", "Y");

								YFCElement chngcOrdLinesEle1 = chngcOrderEle1.getChildElement("OrderLines");
								YFCElement chngcOrdLineEle1 = chngcOrdLinesEle1.getOwnerDocument().importNode(rootOrdLineEle, true);
								chngcOrdLinesEle1.appendChild(chngcOrdLineEle1);
								chngcOrdLineEle1.setAttribute("OrderLineKey", cOrdLineKey);
								YFCElement chngcOrdLineExtnEle1 = chngcOrdLineEle1.getChildElement("Extn");
								if (chngcOrdLineExtnEle1 != null) {
									chngcOrdLineExtnEle1.setAttribute("ExtnLegacyLineNumber", "");
									chngcOrdLineExtnEle1.removeAttribute("ExtnUnitPrice");
								}

								// To get the ordered quantity from the customer
								// order line.
								String cOrdQty = this.getOrderedQtyOnOrderLine(webLineNo, cOrderEle);
								if (YFCObject.isNull(cOrdQty) || YFCObject.isVoid(cOrdQty)) {
									throw new Exception("ExtnWebLineNumber Does Not Exist in Customer Order!");
								}
								// To get the ordered quantity from the
								// Fulfillment order line.
								String fOrdQty = this.getOrderedQtyForWebLineNo(webLineNo, fOrderEle);
								if (YFCObject.isNull(fOrdQty) || YFCObject.isVoid(fOrdQty)) {
									throw new Exception("ExtnWebLineNumber Does Not Exist in Fulfillment Order!");
								}

								Float _ordLineQtyInCO = Float.parseFloat(cOrdQty);
								Float _ordLineQtyInFO = Float.parseFloat(fOrdQty);

								String ordQty = null;
								String tuom = null;
								YFCElement chngcOrdLineTranQtyEle1 = chngcOrdLineEle1.getChildElement("OrderLineTranQuantity");
								if (chngcOrdLineTranQtyEle1 != null) {
									if (chngcOrdLineTranQtyEle1.hasAttribute("OrderedQty")) {
										ordQty = chngcOrdLineTranQtyEle1.getAttribute("OrderedQty");
										if (YFCObject.isNull(ordQty) || YFCObject.isVoid(ordQty)) {
											ordQty = _ordLineQtyInFO.toString();
										}
									} else {
										ordQty = _ordLineQtyInFO.toString();
									}
									if (chngcOrdLineTranQtyEle1.hasAttribute("TransactionalUOM")) {
										tuom = chngcOrdLineTranQtyEle1.getAttribute("TransactionalUOM");
									}
								}

								Float _ordLineQtyInXML = Float.parseFloat(ordQty);
								if(log.isDebugEnabled()){
									log.debug("_ordLineQtyInXML:" + _ordLineQtyInXML);
									log.debug("_ordLineQtyInCO:" + _ordLineQtyInCO);
									log.debug("_ordLineQtyInFO:" + _ordLineQtyInFO);
								}
								if (_ordLineQtyInXML > _ordLineQtyInFO) {
									ordQty = _ordLineQtyInFO.toString();
								} else {
									ordQty = new Float(_ordLineQtyInCO - (_ordLineQtyInFO - _ordLineQtyInXML)).toString();
								}

								chngcOrdLineTranQtyEle1.setAttribute("OrderedQty", ordQty);
								chngcOrdLineTranQtyEle1.setAttribute("TransactionalUOM", tuom);

								chngcOrdStatusEle1.setAttribute("OrderHeaderKey", cOrdHeaderKey);
								YFCElement chngcOrdStatusLinesEle1 = chngcOrdStatusEle1.getChildElement("OrderLines");
								YFCElement chngcOrdStatusLineEle1 = chngcOrdStatusLinesEle1.getOwnerDocument().createElement("OrderLine");
								chngcOrdStatusLinesEle1.appendChild(chngcOrdStatusLineEle1);
								chngcOrdStatusLineEle1.setAttribute("OrderLineKey", cOrdLineKey);

								YFCElement chngcOrdStatusLineTranQtyEle1 = chngcOrdStatusLineEle1.getOwnerDocument().createElement("OrderLineTranQuantity");
								chngcOrdStatusLineEle1.appendChild(chngcOrdStatusLineTranQtyEle1);
								
								if (rootEle.hasAttribute("FillAndKill")) {
									fillAndKill = rootEle.getAttribute("FillAndKill");
								}								
								if (!YFCObject.isNull(fillAndKill) && !YFCObject.isVoid(fillAndKill) 
										&& fillAndKill.equalsIgnoreCase("Y")) {
									// For Fill and Kill scenario.
									Float fillAndKillQuantity = 0.0f;
									fillAndKillQuantity = _ordLineQtyInFO - _ordLineQtyInXML;
									chngcOrdStatusLineTranQtyEle1.setAttribute("Quantity", fillAndKillQuantity);
									if (!YFCObject.isNull(fillAndKillQuantity) && fillAndKillQuantity.equals(0.0f)) {
										// Not required to change the status for the line as quantity to be changed is 0.
										chngcOrdStatusLinesEle1.removeChild(chngcOrdStatusLineEle1);										
									}	
								} else {
									chngcOrdStatusLineTranQtyEle1.setAttribute("Quantity", _ordLineQtyInFO.toString());
								}
								chngcOrdStatusLineTranQtyEle1.setAttribute("TransactionalUOM", tuom);
							}
						} else if (lineProcessCode.equalsIgnoreCase("_D")) {
							prepareFOLineCancel(rootOrdLineEle);
						} else {
							throw new Exception("Invalid LineProcessCode in the Incoming Legacy Message!");
						}
					} else {
						throw new Exception("Attribute LineProcessCode Not Available in the Incoming Legacy Message!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in the Incoming Legacy Message!");
				}
			}
		} else {
			chngcOrderEle0.setAttribute("OrderHeaderKey", cOrdHeaderKey);
			chngcOrderEle0.setAttribute("Override", "Y");
		}

		if(log.isDebugEnabled()){
			log.debug("hasLineA:" + hasLineA);
			log.debug("hasLineC:" + hasLineC);
			log.debug("hasLineD:" + hasLineD);
			log.debug("Boolean:" + (!hasLineA && !hasLineC && hasLineD));
		}

		return (!hasLineA && !hasLineC && hasLineD);
	}

	private void handleLineProcessCodeCandA(YFSEnvironment env, String ordHeaderKey, String webLineNo, String cOrdLineKey, YFCElement rootOrdLineEle, YFCElement fOrderEle, YFCElement cOrderEle,
			YFCElement chngcOrdStatusEle0, YFCElement chngcOrdStatusEle, YFCElement chngcOrderEle0) throws Exception {

		Float _ordLineQtyInXML = 0f;
		Float _unSchLineQtyInCO = 0f;
		Float _ordLineQtyInCO = 0f;
		Float _ordLineQtyInFO = 0f;
		boolean hasUOMChanged = false;

		YFCElement chngcOrdStatusLinesEle0 = chngcOrdStatusEle0.getChildElement("OrderLines");
		YFCElement chngcOrdStatusLinesEle = chngcOrdStatusEle.getChildElement("OrderLines");
		YFCElement chngcOrdLinesEle0 = chngcOrderEle0.getChildElement("OrderLines");

		List<String> webLineNos = this.getWebLineNosToArray(fOrderEle);
		boolean webLineNoExists = webLineNos.contains(webLineNo);

		String ordQty = null;
		String tuom = null;
		String lineType = null;

		if (rootOrdLineEle.hasAttribute("LineType")) {
			lineType = rootOrdLineEle.getAttribute("LineType");
			if (YFCObject.isNull(lineType) || YFCObject.isVoid(lineType)) {
				throw new Exception("Attribute LineType Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute LineType Not Available in Incoming Legacy Message!");
		}

		YFCElement rootOrdLineTranQtyEle = rootOrdLineEle.getChildElement("OrderLineTranQuantity");
		if (rootOrdLineTranQtyEle != null) {
			if (rootOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
				ordQty = rootOrdLineTranQtyEle.getAttribute("OrderedQty");
				if (YFCObject.isNull(ordQty) || YFCObject.isVoid(ordQty)) {
					throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute OrderedQty Not Available in Incoming Legacy Message!");
			}
			if (rootOrdLineTranQtyEle.hasAttribute("TransactionalUOM")) {
				tuom = rootOrdLineTranQtyEle.getAttribute("TransactionalUOM");
				if ((YFCObject.isNull(tuom) || YFCObject.isVoid(tuom)) && !lineType.equalsIgnoreCase("M")) {
					throw new Exception("Attribute TransactionalUOM Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute TransactionalUOM Not Available in Incoming Legacy Message!");
			}
		}

		_ordLineQtyInXML = Float.parseFloat(ordQty);
		_unSchLineQtyInCO = getUnScheduledQtyOnCOLine(webLineNo, cOrderEle);
		_ordLineQtyInCO = Float.parseFloat(this.getOrderedQtyOnOrderLine(webLineNo, cOrderEle));
		if (webLineNoExists) {
			_ordLineQtyInFO = Float.parseFloat(this.getOrderedQtyOnOrderLine(webLineNo, fOrderEle));
		} else {
			_ordLineQtyInFO = _ordLineQtyInXML;
		}
		
		// To check change in UOM.
		if (!lineType.equalsIgnoreCase("M")) {
			// As special item need not be compared for UOM. UOM will be empty.
			hasUOMChanged = findUOMchange(webLineNo,webLineNoExists,fOrderEle,rootOrdLineTranQtyEle);
		}

		if(log.isDebugEnabled()){
			log.debug("_ordLineQtyInXML:" + _ordLineQtyInXML);
			log.debug("_unSchLineQtyInCO:" + _unSchLineQtyInCO);
			log.debug("_ordLineQtyInCO:" + _ordLineQtyInCO);
			log.debug("_ordLineQtyInFO:" + _ordLineQtyInFO);
			log.debug("hasUOMChanged:" + hasUOMChanged);
		}

		if (_unSchLineQtyInCO > 0) {
			Float tempQty = _ordLineQtyInXML - _unSchLineQtyInCO;
			if (tempQty > 0) {
				if (_ordLineQtyInXML > _ordLineQtyInFO) {

					chngcOrdStatusEle0.setAttribute("OrderHeaderKey", ordHeaderKey);
					YFCElement chngcOrdStatusLineEle0 = chngcOrdStatusLinesEle0.getOwnerDocument().createElement("OrderLine");
					chngcOrdStatusLinesEle0.appendChild(chngcOrdStatusLineEle0);
					chngcOrdStatusLineEle0.setAttribute("OrderLineKey", cOrdLineKey);

					YFCElement chngcOrdStatusLineTranQtyEle0 = chngcOrdStatusLineEle0.getOwnerDocument().createElement("OrderLineTranQuantity");
					chngcOrdStatusLineEle0.appendChild(chngcOrdStatusLineTranQtyEle0);
					chngcOrdStatusLineTranQtyEle0.setAttribute("Quantity", _unSchLineQtyInCO.toString());
					chngcOrdStatusLineTranQtyEle0.setAttribute("TransactionalUOM", tuom);

					chngcOrderEle0.setAttribute("OrderHeaderKey", ordHeaderKey);
					chngcOrderEle0.setAttribute("Override", "Y");
					YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
					chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
					chngcOrdLineEle0.setAttribute("Action", "MODIFY");
					chngcOrdLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
					YFCElement chngcOrdLineTranQtyEle0 = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
					if (chngcOrdLineTranQtyEle0 != null) {
						chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", new Float(_ordLineQtyInCO - _unSchLineQtyInCO).toString());
					}
					YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
					if (chngcOrdLineExtnEle0 != null) {
						if (chngcOrdLineExtnEle0.hasAttribute("ExtnLegacyLineNumber")) {
							chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
						}
					}

					chngcOrdStatusEle.setAttribute("OrderHeaderKey", ordHeaderKey);
					YFCElement chngcOrdStatusLineEle = chngcOrdStatusLinesEle.getOwnerDocument().createElement("OrderLine");
					chngcOrdStatusLinesEle.appendChild(chngcOrdStatusLineEle);
					chngcOrdStatusLineEle.setAttribute("OrderLineKey", cOrdLineKey);

					YFCElement chngcOrdStatusLineTranQtyEle = chngcOrdStatusLineEle.getOwnerDocument().createElement("OrderLineTranQuantity");
					chngcOrdStatusLineEle.appendChild(chngcOrdStatusLineTranQtyEle);
					chngcOrdStatusLineTranQtyEle.setAttribute("Quantity", _ordLineQtyInXML - _ordLineQtyInFO);
					chngcOrdStatusLineTranQtyEle.setAttribute("TransactionalUOM", tuom);

				} else {

					chngcOrderEle0.setAttribute("OrderHeaderKey", ordHeaderKey);
					chngcOrderEle0.setAttribute("Override", "Y");
					YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
					chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
					chngcOrdLineEle0.setAttribute("Action", "MODIFY");
					chngcOrdLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
					YFCElement chngcOrdLineTranQtyEle0 = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
					if (chngcOrdLineTranQtyEle0 != null) {
						if (webLineNoExists) {
							chngcOrdLineEle0.removeAttribute("OrderedQty");
							chngcOrdLineEle0.removeChild(chngcOrdLineTranQtyEle0);
						} else {
							chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", _ordLineQtyInCO + _ordLineQtyInXML);
						}
					}
					YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
					if (chngcOrdLineExtnEle0 != null) {
						if (chngcOrdLineExtnEle0.hasAttribute("ExtnLegacyLineNumber")) {
							chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
						}
					}
				}
			} else {
				if (_ordLineQtyInXML > _ordLineQtyInFO) {
					chngcOrdStatusEle0.setAttribute("OrderHeaderKey", ordHeaderKey);
					YFCElement chngcOrdStatusLineEle0 = chngcOrdStatusLinesEle0.getOwnerDocument().createElement("OrderLine");
					chngcOrdStatusLinesEle0.appendChild(chngcOrdStatusLineEle0);
					chngcOrdStatusLineEle0.setAttribute("OrderLineKey", cOrdLineKey);

					YFCElement chngcOrdStatusLineTranQtyEle0 = chngcOrdStatusLineEle0.getOwnerDocument().createElement("OrderLineTranQuantity");
					chngcOrdStatusLineEle0.appendChild(chngcOrdStatusLineTranQtyEle0);
					chngcOrdStatusLineTranQtyEle0.setAttribute("Quantity", _ordLineQtyInXML - _ordLineQtyInFO);
					chngcOrdStatusLineTranQtyEle0.setAttribute("TransactionalUOM", tuom);

					chngcOrderEle0.setAttribute("OrderHeaderKey", ordHeaderKey);
					chngcOrderEle0.setAttribute("Override", "Y");
					YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
					chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
					chngcOrdLineEle0.setAttribute("Action", "MODIFY");
					chngcOrdLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
					YFCElement chngcOrdLineTranQtyEle0 = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
					if (chngcOrdLineTranQtyEle0 != null) {
						chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", new Float(_ordLineQtyInCO - (_ordLineQtyInXML - _ordLineQtyInFO)).toString());
					}
					YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
					if (chngcOrdLineExtnEle0 != null) {
						if (chngcOrdLineExtnEle0.hasAttribute("ExtnLegacyLineNumber")) {
							chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
						}
					}

					chngcOrdStatusEle.setAttribute("OrderHeaderKey", ordHeaderKey);
					YFCElement chngcOrdStatusLineEle = chngcOrdStatusLinesEle.getOwnerDocument().createElement("OrderLine");
					chngcOrdStatusLinesEle.appendChild(chngcOrdStatusLineEle);
					chngcOrdStatusLineEle.setAttribute("OrderLineKey", cOrdLineKey);

					YFCElement chngcOrdStatusLineTranQtyEle = chngcOrdStatusLineEle.getOwnerDocument().createElement("OrderLineTranQuantity");
					chngcOrdStatusLineEle.appendChild(chngcOrdStatusLineTranQtyEle);
					chngcOrdStatusLineTranQtyEle.setAttribute("Quantity", _ordLineQtyInXML - _ordLineQtyInFO);
					chngcOrdStatusLineTranQtyEle.setAttribute("TransactionalUOM", tuom);
				} else {

					chngcOrderEle0.setAttribute("OrderHeaderKey", ordHeaderKey);
					chngcOrderEle0.setAttribute("Override", "Y");
					YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
					chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
					chngcOrdLineEle0.setAttribute("Action", "MODIFY");
					chngcOrdLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
					YFCElement chngcOrdLineTranQtyEle0 = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
					if (chngcOrdLineTranQtyEle0 != null) {
						if (webLineNoExists) {
							chngcOrdLineEle0.removeAttribute("OrderedQty");
							chngcOrdLineEle0.removeChild(chngcOrdLineTranQtyEle0);
						} else {
							chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", _ordLineQtyInCO + _ordLineQtyInXML);
						}
					}
					YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
					if (chngcOrdLineExtnEle0 != null) {
						if (chngcOrdLineExtnEle0.hasAttribute("ExtnLegacyLineNumber")) {
							chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
						}
					}
				}
			}
		} else {
			if (_ordLineQtyInXML > _ordLineQtyInFO) {
				chngcOrdStatusEle.setAttribute("OrderHeaderKey", ordHeaderKey);
				YFCElement chngcOrdStatusLineEle = chngcOrdStatusLinesEle.getOwnerDocument().createElement("OrderLine");
				chngcOrdStatusLinesEle.appendChild(chngcOrdStatusLineEle);
				chngcOrdStatusLineEle.setAttribute("OrderLineKey", cOrdLineKey);

				YFCElement chngcOrdStatusLineTranQtyEle = chngcOrdStatusLineEle.getOwnerDocument().createElement("OrderLineTranQuantity");
				chngcOrdStatusLineEle.appendChild(chngcOrdStatusLineTranQtyEle);
				chngcOrdStatusLineTranQtyEle.setAttribute("Quantity", _ordLineQtyInXML - _ordLineQtyInFO);
				chngcOrdStatusLineTranQtyEle.setAttribute("TransactionalUOM", tuom);
			}

			chngcOrderEle0.setAttribute("OrderHeaderKey", ordHeaderKey);
			chngcOrderEle0.setAttribute("Override", "Y");
			YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
			chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
			chngcOrdLineEle0.setAttribute("Action", "MODIFY");
			chngcOrdLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
			YFCElement chngcOrdLineTranQtyEle0 = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
			if (chngcOrdLineTranQtyEle0 != null) {
				if (webLineNoExists) {
					// Line Process Code C
					chngcOrdLineEle0.removeAttribute("OrderedQty");
					chngcOrdLineEle0.removeChild(chngcOrdLineTranQtyEle0);
				} else {
					// Line Process Code A
					chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", _ordLineQtyInCO + _ordLineQtyInXML);
				}
			}
			YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
			if (chngcOrdLineExtnEle0 != null) {
				if (chngcOrdLineExtnEle0.hasAttribute("ExtnLegacyLineNumber")) {
					chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
				}
			}
		}
		
		// If there is a Change in UOM for the fulfillment order. This moves the customer order quantities to Submitted status.
		if (hasUOMChanged) {
			chngcOrdStatusEle.setAttribute("OrderHeaderKey", ordHeaderKey);
			chngcOrdStatusEle.setAttribute("ChangeForAllAvailableQty", "Y");
			YFCElement chngcOrdStatusLineEle = chngcOrdStatusLinesEle.getOwnerDocument().createElement("OrderLine");
			chngcOrdStatusLinesEle.appendChild(chngcOrdStatusLineEle);
			chngcOrdStatusLineEle.setAttribute("OrderLineKey", cOrdLineKey);

			YFCElement chngcOrdStatusLineTranQtyEle = chngcOrdStatusLineEle.getOwnerDocument().createElement("OrderLineTranQuantity");
			chngcOrdStatusLineEle.appendChild(chngcOrdStatusLineTranQtyEle);
			chngcOrdStatusLineTranQtyEle.setAttribute("TransactionalUOM", tuom);
			// Attribute has been removed as all the quantity needs to be increased in case of UOM conversion.
			chngcOrdStatusLineTranQtyEle.removeAttribute("Quantity");
		}	
	}
	
	
	
	private YFCElement getMSAPCustomerInformation(YFSEnvironment env, String rootCustKey) throws Exception {
		
		YFCDocument getRootCustListInXML = YFCDocument.getDocumentFor("<Customer/>");
		YFCElement rootCustInXMLEle = getRootCustListInXML.getDocumentElement();
		rootCustInXMLEle.setAttribute("CustomerKey", rootCustKey);
		YFCElement extnCustInXmlEle = rootCustInXMLEle.createChild("Extn");
		extnCustInXmlEle.setAttribute("ExtnSuffixType", XPXLiterals.MSAP_SUFFIX_TYPE);

		if(log.isDebugEnabled()){
			log.debug("XPXGetCustomerList-InXML:" + getRootCustListInXML.getString());
		}
		Document tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXGetCustomerList", getRootCustListInXML.getDocument());
		if (tempDoc == null || !tempDoc.getDocumentElement().hasChildNodes()) {
			throw new Exception("XPXGetCustomerList - MSAP Customer Not Found!");
		}

		if(log.isDebugEnabled()){
			log.debug("XPXGetCustomerList-OutXML:" + YFCDocument.getDocumentFor(tempDoc).getString());
		}
		
		YFCDocument msapCustDoc = YFCDocument.getDocumentFor(tempDoc);
		return msapCustDoc.getDocumentElement().getChildElement("Customer");
	}
	
	private YFCElement getShipToCustomerInformation(YFSEnvironment env, YFCElement rootEle) throws Exception {
		
		String legacyCustNo = null;
		String shipToSuffix = null;
		YFCElement extnRootEle = rootEle.getChildElement("Extn");

		YFCDocument getCustListInXML = YFCDocument.getDocumentFor("<Customer/>");
		YFCElement custInXMLEle = getCustListInXML.getDocumentElement();
		if (rootEle.hasAttribute("EnterpriseCode")) {
			String orgCode = rootEle.getAttribute("EnterpriseCode");
			if (YFCObject.isNull(orgCode) || YFCObject.isNull(orgCode)) {
				throw new Exception("Attribute EnterpriseCode cannot be NULL or Void!");
			}
			custInXMLEle.setAttribute("OrganizationCode", orgCode);
		} else {
			throw new Exception("Attribute EnterpriseCode Not Available in Incoming Legacy Message!");
		}

		YFCElement extnCustInXMLEle = getCustListInXML.createElement("Extn");
		custInXMLEle.appendChild(extnCustInXMLEle);

		if (extnRootEle.hasAttribute("ExtnCustomerNo")) {
			legacyCustNo = extnRootEle.getAttribute("ExtnCustomerNo");
			if (YFCObject.isNull(legacyCustNo) || YFCObject.isVoid(legacyCustNo)) {
				throw new Exception("Attribute ExtnCustomerNo Cannot be NULL or Void!");
			}
			extnCustInXMLEle.setAttribute("ExtnLegacyCustNumber", legacyCustNo);
		} else {
			throw new Exception("Attribute ExtnLegacyCustNumber Not Available in Incoming Legacy Message!");
		}
 
		if (extnRootEle.hasAttribute("ExtnShipToSuffix")) {
			shipToSuffix = extnRootEle.getAttribute("ExtnShipToSuffix");
			if (YFCObject.isNull(shipToSuffix) || YFCObject.isVoid(shipToSuffix)) {
				throw new Exception("Attribute ExtnShipToSuffix Cannot be NULL or Void!");
			}
			extnCustInXMLEle.setAttribute("ExtnShipToSuffix", shipToSuffix);
		} else {
			throw new Exception("Attribute ExtnBillToSuffix Not Available in Incoming Legacy Message!");
		}
		
		extnCustInXMLEle.setAttribute("ExtnSuffixType", XPXLiterals.CHAR_S);
		
		if(log.isDebugEnabled()){
			log.debug("XPXGetCustomerList-InXML:" + getCustListInXML.getString());
		}
		
		Document tempDoc = api.executeFlow(env, "XPXGetCustomerList", getCustListInXML.getDocument());
		if (tempDoc == null || !tempDoc.getDocumentElement().hasChildNodes()) {
			centExempt = true;
			throw new Exception("Customer Doesn't Exist In Web. [Customer No:"+legacyCustNo+", Suffix:"+shipToSuffix+" ]");
		}

		if(log.isDebugEnabled()){
			log.debug("XPXGetCustomerList-OutXML:" + YFCDocument.getDocumentFor(tempDoc).getString());
		}
		
		YFCDocument shipToCustDoc = YFCDocument.getDocumentFor(tempDoc);
		return shipToCustDoc.getDocumentElement().getChildElement("Customer");
	}

	/**
	 * Gets Bill To name from customer information element.
	 * 
	 * @param custInfoEle
	 *            Element contains the customer information.
	 * @return Bill To Name of the customer.
	 */
	private String getBillToName(YFCElement custInfoEle) throws Exception {

		YFCElement buyerOrgEle = custInfoEle.getChildElement("BuyerOrganization");
		if (buyerOrgEle == null) {
			throw new Exception("Customer/BuyerOrganization Element Not Available in GetCustomerList Template!");
		}
		String orgName = null;
		if (buyerOrgEle.hasAttribute("OrganizationName")) {
			orgName = buyerOrgEle.getAttribute("OrganizationName");
			if (YFCObject.isNull(orgName) || YFCObject.isVoid(orgName)) {
				throw new Exception("Attribute OrganizationName Cannot be NULL or Void!");
			}
		}

		return orgName;
	}

	/**
	 * Gets Bill To ID from customer information element.
	 * 
	 * @param custInfoEle
	 *            Element contains the customer information.
	 * @return Bill To ID of the customer.
	 */
	private String getBillToID(YFCElement custInfoEle) throws Exception {

		String billToID = null;
		if (custInfoEle.hasAttribute("CustomerID")) {
			billToID = custInfoEle.getAttribute("CustomerID");
			if (YFCObject.isNull(billToID) || YFCObject.isVoid(billToID)) {
				throw new Exception("Attribute CustomerID Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute CustomerID Not Available in GetCustomerList Template!");
		}

		return billToID;
	}

	/**
	 * Gets SAP Parent name from customer information element.
	 * 
	 * @param custInfoEle
	 *            Element contains the customer information.
	 * @return SAP Parent Name of the customer.
	 */
	private String getSAPParentName(YFCElement custInfoEle) throws Exception {

		YFCElement extnEle = custInfoEle.getChildElement("Extn");
		if (extnEle == null) {
			throw new Exception("Customer/Extn Element Not Available in GetCustomerList Template!");
		}
		String sapParentName = null;
		if (extnEle.hasAttribute("ExtnSAPParentName")) {
			sapParentName = extnEle.getAttribute("ExtnSAPParentName");
			if (YFCObject.isNull(sapParentName) || YFCObject.isVoid(sapParentName)) {
				throw new Exception("Attribute ExtnSAPParentName Cannot be NULL or Void!");
			}
		}

		return sapParentName;
	}

	/**
	 * Gets Buyer organization code from customer information element.
	 * 
	 * @param custInfoEle
	 *            Element contains the customer information.
	 * @return Buyer Organization code of the customer.
	 */
	private String getBuyerOrgCode(YFCElement custInfoEle) throws Exception {

		String buyerOrgCode = null;
		if (custInfoEle.hasAttribute("CustomerID")) {
			buyerOrgCode = custInfoEle.getAttribute("CustomerID");
			if (YFCObject.isNull(buyerOrgCode) || YFCObject.isVoid(buyerOrgCode)) {
				throw new Exception("Attribute CustomerID Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute CustomerID Not Available in GetCustomerList Template!");
		}

		return buyerOrgCode;
	}

	/**
	 * Gets Ship To ID from customer information element.
	 * 
	 * @param custInfoEle
	 *            Element contains the customer information.
	 * @return Ship To ID of the customer.
	 */
	private String getShipToID(YFCElement custInfoEle) throws Exception {

		String shipToID = null;
		if (custInfoEle.hasAttribute("CustomerID")) {
			shipToID = custInfoEle.getAttribute("CustomerID");
			if (YFCObject.isNull(shipToID) || YFCObject.isVoid(shipToID)) {
				throw new Exception("Attribute CustomerID Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute CustomerID Not Available in GetCustomerList Template!");
		}

		return shipToID;
	}

	/**
	 * Gets Environment code from customer information element.
	 * 
	 * @param custInfoEle
	 *            Element contains the customer information.
	 * @return Environment code.
	 */
	private String getOrigEnvCode(YFCElement custInfoEle) throws Exception {

		YFCElement extnEle = custInfoEle.getChildElement("Extn");
		if (extnEle == null) {
			throw new Exception("Customer/Extn Element Not Available in GetCustomerList Template!");
		}
		String orgEnvCode = null;
		if (extnEle.hasAttribute("ExtnOrigEnvironmentCode")) {
			orgEnvCode = extnEle.getAttribute("ExtnOrigEnvironmentCode");
			if (YFCObject.isNull(orgEnvCode) || YFCObject.isVoid(orgEnvCode)) {
				throw new Exception("Attribute ExtnOrigEnvironmentCode Cannot be NULL or Void!");
			}
		}

		return orgEnvCode;
	}

	/**
	 * Updates the customer order.
	 * 
	 * @param env
	 *            Environment variable.
	 * @param rootEle
	 *            Input xml from legacy system
	 * @param cOrderEle
	 *            Element contains the customer information.
	 * @return tempDoc Output document of the change order API.
	 */

	private YFCDocument updateCustomerOrder(YFSEnvironment env, YFCElement rootEle, YFCElement cOrderEle) throws Exception {

		String _ordHeaderKey = null;
		if (cOrderEle.hasAttribute("OrderHeaderKey")) {
			_ordHeaderKey = cOrderEle.getAttribute("OrderHeaderKey");
			if (YFCObject.isNull(_ordHeaderKey) || YFCObject.isVoid(_ordHeaderKey)) {
				throw new Exception("Attribute OrderHeader Cannot be NULL or Void!");
			}
			rootEle.setAttribute("OrderHeaderKey", _ordHeaderKey);
			rootEle.setAttribute("Override", "Y");
			if (rootEle.hasAttribute("ShipNode")) {
				rootEle.removeAttribute("ShipNode");
			}
		} else {
			throw new Exception("Attribute OrderHeaderKey Not Available in OrderList Template!");
		}

		YFCElement rootOrdExtnEle = rootEle.getChildElement("Extn");
		if (rootOrdExtnEle != null) {
			if (rootOrdExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
				rootOrdExtnEle.setAttribute("ExtnLegacyOrderNo", "");
			}
			if (rootOrdExtnEle.hasAttribute("ExtnGenerationNo")) {
				rootOrdExtnEle.setAttribute("ExtnGenerationNo", "");
			}
		}

		// Forms list of web line numbers of the new lines to be added.
		List<String> webLineNosInXML = getWebLineNosToArray(rootEle);
		if (webLineNosInXML.size() == 0) {
			throw new Exception("No WebLineNumber Available in the Incoming Legacy Message!");
		}
		// Forms list of web line numbers of the existing web line numbers in
		// the customer order.
		List<String> webLineNosExistingCO = getWebLineNosToArray(cOrderEle);
		if (webLineNosExistingCO.size() == 0) {
			throw new Exception("No WebLineNumber Available in the Existing Customer Order!");
		}

		List<String> _newLinesList = new ArrayList<String>(webLineNosInXML);
		List<String> _existingLinesList = new ArrayList<String>(webLineNosExistingCO);

		// To remove all the existing web line numbers from the new line list
		// that needs to be created.
		_newLinesList.removeAll(webLineNosExistingCO);
		_existingLinesList.retainAll(webLineNosInXML);
		if(log.isDebugEnabled()){
			log.debug("_NewLinesList:" + _newLinesList);
			log.debug("_ExistingLinesList:" + _existingLinesList);
		}
		if (_newLinesList.size() > 0 || _existingLinesList.size() > 0) {
			// Add New Lines to CO
			YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
			if (rootOrdLinesEle != null) {
				YFCIterable<YFCElement> yfcItr = rootOrdLinesEle.getChildren("OrderLine");
				while (yfcItr.hasNext()) {
					YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
					YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
					if (rootOrdLineExtnEle != null) {
						if (rootOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
							String webLineNo = rootOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							// Check if the webline no in the input xml is there
							// in the new lines list that needs to be created.
							// Else remove the particular line.
							if (!_newLinesList.contains(webLineNo) && !_existingLinesList.contains(webLineNo)) {
								rootOrdLinesEle.removeChild(rootOrdLineEle);
								yfcItr = rootOrdLinesEle.getChildren("OrderLine");
							} else {
								if (_newLinesList.contains(webLineNo)) {
									rootOrdLineEle.setAttribute("IsNewLine", "Y");
									rootOrdLineEle.setAttribute("Action", "CREATE");
								} else {
									rootOrdLineEle.setAttribute("IsNewLine", "N");
								}
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in Incoming Legacy Message!");
						}
						if (rootOrdLineEle.hasAttribute("ShipNode")) {
							rootOrdLineEle.removeAttribute("ShipNode");
						}
						if (rootOrdLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
							rootOrdLineExtnEle.setAttribute("ExtnLegacyLineNumber", "");
						} else {
							throw new Exception("Attribute Extn/@ExtnLegacyLineNumber Not Available in Incoming Legacy Message!");
						}
					} else {
						throw new Exception("OrderLine/Extn Element Not Available in Incoming in Legacy Message!");
					}
				}
			} else {
				throw new Exception("Order/OrderLines Element Not Available in Incoming Legacy Message!");
			}
		}
		// To change the customer order quantity
		adjustCustomerOrderQty(env, rootEle, cOrderEle);

		Document tempDoc = YFCDocument.createDocument().getDocument();
		tempDoc.appendChild(tempDoc.importNode(rootEle.getDOMNode(), true));
		tempDoc.renameNode(tempDoc.getDocumentElement(), tempDoc.getNamespaceURI(), "Order");
		YFCElement chngcOrdEle = YFCDocument.getDocumentFor(tempDoc).getDocumentElement();

		this.filterAttributes(chngcOrdEle, true);
		if(log.isDebugEnabled()){
		log.debug("XPXChangeOrder_CO-InXML:" + chngcOrdEle.getString());
		}
		// Change order call to update the customer order.
		tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrder", chngcOrdEle.getOwnerDocument().getDocument());
		if (tempDoc != null) {
			return YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("XPXChangeOrder Service Failed!");
		}

	}

	private boolean isCancelledOrder(YFCElement orderEle) {

		String minOrdStatus = null;
		if (orderEle.hasAttribute("MinOrderStatus")) {
			minOrdStatus = orderEle.getAttribute("MinOrderStatus");
			if (YFCObject.isNull(minOrdStatus) || YFCObject.isVoid(minOrdStatus)) {
				return false;
			}
		} else {
			return false;
		}

		String maxOrdStatus = null;
		if (orderEle.hasAttribute("MaxOrderStatus")) {
			maxOrdStatus = orderEle.getAttribute("MaxOrderStatus");
			if (YFCObject.isNull(maxOrdStatus) || YFCObject.isVoid(maxOrdStatus)) {
				return false;
			}
		} else {
			return false;
		}

		if (minOrdStatus.equalsIgnoreCase("9000") && maxOrdStatus.equalsIgnoreCase("9000")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean IsCancelledLine(String webLineNo, YFCElement orderEle) throws Exception {

		YFCElement ordLinesEle = orderEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if (ordLineEle != null) {
					String ordQty = null;
					if (ordLineEle.hasAttribute("OrderedQty")) {
						ordQty = ordLineEle.getAttribute("OrderedQty");
						if (YFCObject.isNull(ordQty) || YFCObject.isVoid(ordQty)) {
							throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute OrderedQty Not Available in OrderList Template!");
					}

					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if (extnEle != null) {
						String _webLineNo = null;
						if (extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							if (_webLineNo.equalsIgnoreCase(webLineNo)) {
								Float ordLineQty = Float.parseFloat(ordQty);
								if (ordLineQty == 0) {
									return true;
								} else {
									return false;
								}
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in OrderList Template!");
						}
					} else {
						throw new Exception("OrderLine/Extn Element Not Available in the OrderList Template!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in the OrderList Template!");
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available in the OrderList Template!");
		}
		return false;
	}

	/**
	 * Gets unscheduled quantity of the customer order line.
	 * 
	 * @param webLineNo
	 *            Web line number.
	 * @param cOrderEle
	 *            Element contains the customer information.
	 * @return unSchLineQty Unscheduled quantity.
	 */

	private Float getUnScheduledQtyOnCOLine(String webLineNo, YFCElement cOrderEle) throws Exception {

		Float unSchLineQty = 0f;

		YFCElement ordLinesEle = cOrderEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if (ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if (extnEle != null) {
						if (extnEle.hasAttribute("ExtnWebLineNumber")) {
							String _webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							if (_webLineNo.equalsIgnoreCase(webLineNo)) {
								String status = null;
								String statusQty = null;
								YFCElement ordLineStatusesEle = ordLineEle.getChildElement("OrderStatuses");
								if (ordLineStatusesEle != null) {
									YFCIterable<YFCElement> yfcItr1 = ordLineStatusesEle.getChildren("OrderStatus");
									while (yfcItr1.hasNext()) {
										YFCElement ordLineStatusEle = (YFCElement) yfcItr1.next();
										if (ordLineStatusEle.hasAttribute("Status")) {
											status = ordLineStatusEle.getAttribute("Status");
											if (YFCObject.isNull(status) || YFCObject.isVoid(status)) {
												throw new Exception("Attribute OrderLine/OrderStatuses/OrderStatus/@Status Cannot be NULL or Void!");
											}
										} else {
											throw new Exception("Attribute OrderLine/OrderStatuses/OrderStatus/@Status Not Available in OrderList Template!");
										}

										if (ordLineStatusEle.hasAttribute("StatusQty")) {
											statusQty = ordLineStatusEle.getAttribute("StatusQty");
											if (YFCObject.isNull(statusQty) || YFCObject.isVoid(statusQty)) {
												throw new Exception("Attribute OrderLine/OrderStatuses/OrderStatus/@StatusQty Cannot be NULL or Void!");
											}
										} else {
											throw new Exception("Attribute OrderLine/OrderStatuses/OrderStatus/@StatusQty Not Available in OrderList Template!");
										}
										// Fetch the status quantity where
										// status = 'Unscheduled'
										if (status.equalsIgnoreCase("1310")) {
											unSchLineQty = unSchLineQty + Float.parseFloat(statusQty);
										}
									}
								} else {
									throw new Exception("OrderStatuses Element Not Available in OrderList Template!");
								}
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in the OrderList Template!");
						}
					} else {
						throw new Exception("OrderLine/Extn Element Not Available in the OrderList Template!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in the OrderList Template!");
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available in the OrderList Template!");
		}

		return unSchLineQty;

	}

	/**
	 * Returns ordered quantity on customer order.
	 * 
	 * @param env
	 *            Environment variable.
	 * @param rootEle
	 *            Input xml from legacy system
	 * @param cOrderEle
	 *            Element contains the customer information.
	 * @return tempDoc Output document of the change order API.
	 */

	private String getOrderedQtyOnOrderLine(String webLineNo, YFCElement orderEle) throws Exception {

		YFCElement ordLinesEle = orderEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if (ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if (extnEle != null) {
						String _webLineNo = null;
						if (extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							if (_webLineNo.equalsIgnoreCase(webLineNo)) {
								YFCElement ordLineTranQtyEle = ordLineEle.getChildElement("OrderLineTranQuantity");
								if (ordLineTranQtyEle != null) {
									if (ordLineTranQtyEle.hasAttribute("OrderedQty")) {
										String _ordQty = ordLineTranQtyEle.getAttribute("OrderedQty");
										if (YFCObject.isNull(_ordQty) || YFCObject.isVoid(_ordQty)) {
											throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
										}
										return _ordQty;
									} else {
										throw new Exception("Attribute OrderedQty Not Available in the OrderList Template!");
									}
								}
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in the OrderList Template!");
						}
					} else {
						throw new Exception("OrderLine/Extn Element Not Available in the OrderList Template!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in the OrderList Template!");
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available in the OrderList Template!");
		}

		return null;
	}

	private void adjustCustomerOrderQty(YFSEnvironment env, YFCElement rootEle, YFCElement cOrderEle) throws Exception {

		boolean isChngCOStatusReq = false;

		Float ordQtyInXML = 0f;
		Float ordQtyInCO = 0f;
		Float ordQtyInCOUnSch = 0f;

		String _ordHeaderKey = null;

		YFCDocument chngCOStatusInXML = YFCDocument.getDocumentFor("<OrderStatusChange/>");
		if (rootEle.hasAttribute("OrderHeaderKey")) {
			_ordHeaderKey = rootEle.getAttribute("OrderHeaderKey");
			if (YFCObject.isNull(_ordHeaderKey) || YFCObject.isVoid(_ordHeaderKey)) {
				throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute OrderHeaderKey Not Available in Incoming Legacy Message!");
		}
		// To set 'XPX Chain FO Cancel Status' transaction.
		String tranId = null;
		if (this._prop != null) {
			tranId = this._prop.getProperty("XPX_CHN_CNL_STATUS_TXN");
			if (YFCObject.isNull(tranId) || YFCObject.isVoid(tranId)) {
				throw new Exception("TranstionID Not Configured in API Arguments!");
			}
		} else {
			throw new Exception("Arguments Not Configured For API-XPXPerformLegacyOrderUpdateAPI!");
		}
		// To build input document to call changeOrderStatus API.
		YFCElement chngCOStatusEle = chngCOStatusInXML.getDocumentElement();
		chngCOStatusEle.setAttribute("OrderHeaderKey", _ordHeaderKey);
		chngCOStatusEle.setAttribute("IgnoreTransactionDependencies", "Y");
		chngCOStatusEle.setAttribute("TransactionId", tranId);
		chngCOStatusEle.setAttribute("BaseDropStatus", "1100.0100");

		YFCElement chngCOStatusLinesEle = chngCOStatusEle.getOwnerDocument().createElement("OrderLines");
		chngCOStatusEle.appendChild(chngCOStatusLinesEle);

		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if (rootOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {

				String isNewLine = null;
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				if (rootOrdLineEle.hasAttribute("IsNewLine")) {
					isNewLine = rootOrdLineEle.getAttribute("IsNewLine");
					if (YFCObject.isNull(isNewLine) || YFCObject.isVoid(isNewLine)) {
						isNewLine = "Y";
					} else {
						if ("Y".equalsIgnoreCase(isNewLine)) {
							isNewLine = "Y";
						} else {
							isNewLine = "N";
						}
					}
				} else {
					isNewLine = "Y";
				}
				// To retrieve the order quantity from input xml.
				if (isNewLine.equalsIgnoreCase("N")) {
					String _ordLineKey = null;
					String _tuom = null;
					String _lineType = null;

					if (rootOrdLineEle.hasAttribute("LineType")) {
						_lineType = rootOrdLineEle.getAttribute("LineType");
						if (YFCObject.isNull(_lineType) || YFCObject.isVoid(_lineType)) {
							throw new Exception("Attribute LineType Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute LineType Not Available in Incoming Legacy Message!");
					}

					YFCElement rootOrdLineTranQtyEle = rootOrdLineEle.getChildElement("OrderLineTranQuantity");
					if (rootOrdLineTranQtyEle != null) {
						if (rootOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
							String _ordQtyInXML = rootOrdLineTranQtyEle.getAttribute("OrderedQty");
							if (YFCObject.isNull(ordQtyInXML) || YFCObject.isVoid(ordQtyInXML)) {
								throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
							}
							ordQtyInXML = Float.parseFloat(_ordQtyInXML);
						} else {
							throw new Exception("Attribute OrderedQty Not Available in Incoming Legacy Message!");
						}
						if (rootOrdLineTranQtyEle.hasAttribute("TransactionalUOM")) {
							_tuom = rootOrdLineTranQtyEle.getAttribute("TransactionalUOM");
							if ((YFCObject.isNull(_tuom) || YFCObject.isVoid(_tuom)) && !_lineType.equalsIgnoreCase("M")) {
								throw new Exception("Attribute TransactionalUOM Cannot be NULL or Void!");
							}
						} else {
							throw new Exception("Attribute TransactionalUOM Not Available in Incoming Legacy Message!");
						}
					}
					YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
					if (rootOrdLineExtnEle != null) {
						if (rootOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
							String webLineNo = rootOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							_ordLineKey = this.getOrderLineKeyForWebLineNo(webLineNo, cOrderEle);
							ordQtyInCO = Float.parseFloat(getOrderedQtyOnOrderLine(webLineNo, cOrderEle));
							ordQtyInCOUnSch = getUnScheduledQtyOnCOLine(webLineNo, cOrderEle);
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in Incoming Legacy Message!");
						}
					} else {
						throw new Exception("OrderLine/Extn Element Not Available in Incoming Legacy Message!");
					}

					rootOrdLineEle.setAttribute("OrderLineKey", _ordLineKey);

					if(log.isDebugEnabled()){
						log.debug("Customer Order Update...");
						log.debug("ordQtyInCO:" + ordQtyInCO);
						log.debug("ordQtyInCOUnSch:" + ordQtyInCOUnSch);
						log.debug("ordQtyInXML:" + ordQtyInXML);
					}

					if (ordQtyInCOUnSch > 0) {
						Float tempQty = ordQtyInXML - ordQtyInCOUnSch;
						if (tempQty > 0) {
							isChngCOStatusReq = true;
							YFCElement chngCOStatusLineEle = chngCOStatusLinesEle.getOwnerDocument().createElement("OrderLine");
							chngCOStatusLinesEle.appendChild(chngCOStatusLineEle);
							chngCOStatusLineEle.setAttribute("OrderLineKey", _ordLineKey);

							YFCElement chngCOStatusLineTranQtyEle = chngCOStatusLineEle.getOwnerDocument().createElement("OrderLineTranQuantity");
							chngCOStatusLineEle.appendChild(chngCOStatusLineTranQtyEle);
							chngCOStatusLineTranQtyEle.setAttribute("Quantity", ordQtyInCOUnSch);
							chngCOStatusLineTranQtyEle.setAttribute("TransactionalUOM", _tuom);

							if (rootOrdLineTranQtyEle != null) {
								rootOrdLineTranQtyEle.setAttribute("OrderedQty", new Float(tempQty + ordQtyInCO).toString());
							}
						} else {
							isChngCOStatusReq = true;
							YFCElement chngCOStatusLineEle = chngCOStatusLinesEle.getOwnerDocument().createElement("OrderLine");
							chngCOStatusLinesEle.appendChild(chngCOStatusLineEle);
							chngCOStatusLineEle.setAttribute("OrderLineKey", _ordLineKey);

							YFCElement chngCOStatusLineTranQtyEle = chngCOStatusLineEle.getOwnerDocument().createElement("OrderLineTranQuantity");
							chngCOStatusLineEle.appendChild(chngCOStatusLineTranQtyEle);
							chngCOStatusLineTranQtyEle.setAttribute("Quantity", ordQtyInXML);
							chngCOStatusLineTranQtyEle.setAttribute("TransactionalUOM", _tuom);

							if (rootOrdLineTranQtyEle != null) {
								rootOrdLineTranQtyEle.setAttribute("OrderedQty", new Float(ordQtyInCO).toString());
							}
						}
					} else {
						if (rootOrdLineTranQtyEle != null) {
							rootOrdLineTranQtyEle.setAttribute("OrderedQty", new Float(ordQtyInXML + ordQtyInCO).toString());
						}
					}
				}
			}

			if (isChngCOStatusReq) {
				if(log.isDebugEnabled()){
				log.debug("XPXChangeOrderStatus_CO-InXML:" + chngCOStatusInXML.getString());
				}
				XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrderStatus", chngCOStatusInXML.getDocument());
			}
		} else {
			throw new Exception("OrderLines Element Not Available in Incoming Legacy Message!");
		}
	}

	private void setExtnLineTypeToOrderLine(YFSEnvironment env, YFCElement rootOrdLineEle, YFCElement cOrderEle) throws Exception {

		String envId = null;
		String compId = null;
		String custNo = null;

		YFCElement custExtnEle = cOrderEle.getChildElement("Extn");
		if (custExtnEle != null) {

			if (custExtnEle.hasAttribute("ExtnCustomerNo")) {
				custNo = custExtnEle.getAttribute("ExtnCustomerNo");
				if (YFCObject.isNull(custNo) || YFCObject.isVoid(custNo)) {
					throw new Exception("Attribute OrderLine/Extn/@ExtnCustomerNo Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ExtnCustomerNo Not Available in Incoming Legacy Message!");
			}

			if (custExtnEle.hasAttribute("ExtnEnvtId")) {
				envId = custExtnEle.getAttribute("ExtnEnvtId");
				if (YFCObject.isNull(envId) || YFCObject.isVoid(envId)) {
					throw new Exception("Attribute OrderLine/Extn/@ExtnEnvtId Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ExtnEnvId Not Available in Incoming Legacy Message!");
			}

			if (custExtnEle.hasAttribute("ExtnCompanyId")) {
				compId = custExtnEle.getAttribute("ExtnCompanyId");
				if (YFCObject.isNull(compId) || YFCObject.isVoid(compId)) {
					throw new Exception("Attribute OrderLine/Extn/@ExtnCompanyId Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ExtnCompanyId Not Available in Incoming Legacy Message!");
			}
		} else {
			throw new Exception("OrderLine/Extn Element Not Available in Incoming Legacy Message!");
		}

		rootOrdLineEle.setAttribute("Action", "CREATE");
		this.setOrderLineAttributes(env, rootOrdLineEle, compId, envId, custNo);

	}

	/**
	 * Creates a new Customer Order.
	 * 
	 * @param env
	 *            Environment variable.
	 * @param rootEle
	 *            Input xml from legacy system
	 * @return tempDoc Output document of the Create order API.
	 */

	private YFCDocument createCustomerOrder(YFSEnvironment env, YFCElement rootEle) throws Exception {

		rootEle.setAttribute("Action", "CREATE");
		rootEle.setAttribute("OrderType", "Customer");

		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		if (rootExtnEle != null) {

			rootExtnEle.setAttribute("ExtnLegacyOrderNo", "");
			rootExtnEle.setAttribute("ExtnGenerationNo", "");

			YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
			YFCIterable<YFCElement> yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {

				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				rootOrdLineEle.setAttribute("Action", "CREATE");

				YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
				if (rootOrdLineExtnEle != null) {
					if (rootOrdLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
						rootOrdLineExtnEle.setAttribute("ExtnLegacyLineNumber", "");
					} else {
						throw new Exception("Attribute Extn/@ExtnLegacyLineNumber Not Available in Incoming Legacy Message!");
					}
				} else {
					throw new Exception("OrderLine/Extn Element Not Available in Incoming Legacy Message!");
				}
			}
		}

		filterAttributes(rootEle, true);
		if(log.isDebugEnabled()){
		log.debug("XPXCreateOrder_CO-InXML:" + rootEle.getString());
		}
		Document tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXCreateOrder", rootEle.getOwnerDocument().getDocument());
		if (tempDoc != null) {
			return YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("XPXCreateOrder Service Failed!");
		}

	}
	
	private String getLineType(YFCElement rootOrdLineEle) throws Exception {
		
		String lineType = null;
		if (rootOrdLineEle.hasAttribute("LineType")) {
			lineType = rootOrdLineEle.getAttribute("LineType");
			if (YFCObject.isNull(lineType) || YFCObject.isVoid(lineType)) {
				throw new Exception("Attribute OrderLine/@LineType Cannot be NULL or Void!");
			}	
		} else {
			throw new Exception("Attribute OrderLine/@LineType Not Available in Incoming Legacy Message!");
		}
		return lineType;
	}
	
	private String getShipNode(YFCElement rootOrdLineEle) throws Exception {
		
		String shipNode = null;
		if (rootOrdLineEle.hasAttribute("ShipNode")) {
			shipNode = rootOrdLineEle.getAttribute("ShipNode");
			if (YFCObject.isNull(shipNode) || YFCObject.isVoid(shipNode)) {
				throw new Exception("Attribute OrderLine/@ShipNode Cannot be NULL or Void!");
			}
			
			if (shipNode.contains("_")) {
				// Need to remove the environment id from ship node.
				String[] splitArrayOnUom = shipNode.split("_");
				if (splitArrayOnUom.length > 0) {
					shipNode = splitArrayOnUom[0];
				}
			}
		} else {
			throw new Exception("Attribute OrderLine/@ShipNode Not Available in Incoming Legacy Message!");
		}
		return shipNode;
	}
	
	private String getItemID(YFCElement rootOrdLineEle) throws Exception {
		
		String itemID = null;
		YFCElement rootOrdLineItemEle = rootOrdLineEle.getChildElement("Item");
		if (rootOrdLineItemEle != null) {
			if (rootOrdLineItemEle.hasAttribute("ItemID")) {
				itemID = rootOrdLineItemEle.getAttribute("ItemID");
				if (YFCObject.isNull(itemID) || YFCObject.isVoid(itemID)) {
					throw new Exception("Attribute ItemID Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ItemID Not Available in Incoming Legacy Message!");
			}
		} else {
			throw new Exception("Element OrderLine/Item Not Available in Incoming Legacy Message!");
		}	
		return itemID;
	}
	
	private HashMap<String,YFCDocument> getItemDetails(YFSEnvironment env, YFCElement rootEle, String envID) throws Exception {
		
		YFCDocument getItemExtnListOutXML = null;
		YFCDocument getItemListOutXML = null;
		HashMap<String,YFCDocument> itemDetailsMap = new HashMap<String,YFCDocument>();
		YFCDocument getItemExtnListInXML = YFCDocument.getDocumentFor("<XPXItemExtn/>");
		YFCElement getItemExtnListEle = getItemExtnListInXML.getDocumentElement();
		
		YFCDocument getItemListInXML = YFCDocument.getDocumentFor("<Item/>");
		YFCElement itemListEle = getItemListInXML.getDocumentElement();
		
		// To Form Input Document With Complex Query.
		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if (rootOrdLinesEle != null) {
			
			getItemExtnListEle.setAttribute("EnvironmentID", envID);
			YFCElement complexQryElem = getItemExtnListEle.createChild("ComplexQuery");
			YFCElement orElement = complexQryElem.createChild("Or");
			
			YFCElement complexQryElem_ = itemListEle.createChild("ComplexQuery");
			YFCElement orElement_ = complexQryElem_.createChild("Or"); 
			
			YFCIterable<YFCElement> yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				String lineType = getLineType(rootOrdLineEle);
				String shipNode = getShipNode(rootOrdLineEle);
				String itemID = getItemID(rootOrdLineEle);
				
				if(log.isDebugEnabled()){
					log.debug("************************************** GetStockType *******************************************");
					log.debug("ItemID:" + itemID);
					log.debug("ShipNode:" + shipNode);
					log.debug("EnvtId:" + envID);
					log.debug("lineType:" + lineType);
				}
				
				YFCElement andElement = orElement.createChild("And");
				// To Build Complex Query.
				YFCElement expEle = andElement.createChild("Exp");		
				expEle.setAttribute("Name", "XPXDivision");
				expEle.setAttribute("Value", shipNode);
				expEle.setAttribute("QryType", "EQ");
				
				YFCElement expEle_ = andElement.createChild("Exp");		
				expEle_.setAttribute("Name", "ItemID");
				expEle_.setAttribute("Value", itemID);
				expEle_.setAttribute("QryType", "EQ");
				
				YFCElement expEle0 = orElement_.createChild("Exp");		
				expEle0.setAttribute("Name", "ItemID");
				expEle0.setAttribute("Value", itemID);
				expEle0.setAttribute("QryType", "EQ");
			}
		} else {
			throw new Exception("Element OrderLines Not Available In Incoming Legacy Message!");
		}
		
		if(log.isDebugEnabled()){
			log.debug("getXPXItemBranchListService-InXML:" + getItemExtnListInXML.getString());
			log.debug("XPXGetItemList-InXML:" + getItemListInXML.getString());
		}
		
		// 1. API Call - getXPXItemBranchList
		Document tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXItemBranchListService", getItemExtnListInXML.getDocument());
		if (tempDoc != null) {
			getItemExtnListOutXML = YFCDocument.getDocumentFor(tempDoc);
			if(log.isDebugEnabled()){
				log.debug("getXPXItemBranchListService-OutXML:" + getItemExtnListOutXML.getString());
			}
			itemDetailsMap.put("XPXItemExtn", getItemExtnListOutXML);
		}
		
		// 2. API Call - getItemList
		Document tempDocItem = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXGetItemList", getItemListInXML.getDocument());
		if (tempDocItem != null) {
			getItemListOutXML = YFCDocument.getDocumentFor(tempDocItem);
			if(log.isDebugEnabled()){
				log.debug("XPXGetItemList-OutXML:" + getItemListOutXML.getString());
			}
			itemDetailsMap.put("Item", getItemListOutXML);
		}
		return itemDetailsMap;
	}
	
	private String getStockType(YFSEnvironment env, String itemID, String shipNode, String lineType, HashMap<String,YFCDocument> itemDetailsMap) throws Exception {
		if(log.isDebugEnabled()){
			log.debug("**************************************GetInventoryIndicator*******************************************");
			log.debug("ItemID:" + itemID);
			log.debug("ShipNode:" + shipNode);
			log.debug("LineType:" + lineType);
		}

		boolean callItemList = false;
		String invIndicator = null;
		String stockType = null;
		
		if (itemDetailsMap != null) {
			YFCDocument getItemExtnListOutXML = itemDetailsMap.get("XPXItemExtn");
			if (getItemExtnListOutXML != null) {
				if(log.isDebugEnabled()){
					log.debug("getItemExtnList-OutXML:" + getItemExtnListOutXML.getString());
				}
				YFCElement getItemExtnListOutEle = getItemExtnListOutXML.getDocumentElement();
				YFCIterable<YFCElement> yfcItr = getItemExtnListOutEle.getChildren("XPXItemExtn");
				if (!yfcItr.hasNext()) {
					callItemList = true;
				}
				while (yfcItr.hasNext()) {
					
					String shipNode_ = null;
					String itemID_ = null;
					YFCElement itemExtnEle = yfcItr.next();
					if (itemExtnEle != null) {
						
						if (itemExtnEle.hasAttribute("XPXDivision")) {
							shipNode_ = itemExtnEle.getAttribute("XPXDivision");
							if (YFCObject.isNull(shipNode_) || YFCObject.isVoid(shipNode_)) {
								throw new Exception("Attribute XPXDivision Cannot Be Null or Void.");
							}
						} else {
							throw new Exception("Attribute XPXDivision Not Available In XPXItemExtn Output Template.");
						}
						
						if (itemExtnEle.hasAttribute("ItemID")) {
							itemID_ = itemExtnEle.getAttribute("ItemID");
							if (YFCObject.isNull(itemID_) || YFCObject.isVoid(itemID_)) {
								throw new Exception("Attribute ItemID Cannot Be Null or Void.");
							}
						} else {
							throw new Exception("Attribute ItemID Not Available In XPXItemExtn Output Template.");
						}
						
						if (shipNode_.equalsIgnoreCase(shipNode) && itemID_.equalsIgnoreCase(itemID)) {
							callItemList = false;
							if (itemExtnEle.hasAttribute("InventoryIndicator")) {
								invIndicator = itemExtnEle.getAttribute("InventoryIndicator");
								if (!YFCObject.isNull(invIndicator) && !YFCObject.isVoid(invIndicator)) {
									if ("W".equalsIgnoreCase(invIndicator) || "I".equalsIgnoreCase(invIndicator)) {
										stockType = "STOCK";
									} else if ("M".equalsIgnoreCase(invIndicator)) {
										stockType = "DIRECT";
									} else {
										// Do Nothing.
									}
									break;
								} else {
									callItemList = true;
								}
							} else {
								throw new Exception("Attribute InventoryIndicator Not Available In XPXItemExtn Output Template.");
							}
						} else {
							callItemList = true;
						}
					}  else {
						callItemList = true;
					}
				} 
			} else {
				callItemList = true;
			}
			
			if (callItemList) {
				
				YFCDocument getItemListOutXML = itemDetailsMap.get("Item");
				if(log.isDebugEnabled()){
					log.debug("getItemList-OutXML:" + getItemListOutXML.getString());
				}
				
				if (getItemListOutXML != null) {
					YFCElement _itemListEle = getItemListOutXML.getDocumentElement();
					YFCIterable<YFCElement> yfcItr_ = _itemListEle.getChildren("Item");
					if (!yfcItr_.hasNext()) {
						if (lineType.equalsIgnoreCase("M") || lineType.equalsIgnoreCase("C")) {
							stockType = "STOCK";
						} else {
							// Item Doesn't Exist in DB.
							if (log.isDebugEnabled()) {
								log.debug("Item# "+itemID+" Doesn't Exist In Web/Xpedx.com");
							}
							throw new Exception("Item# "+itemID+" Doesn't Exist In Web.");
						}
					}
					while (yfcItr_.hasNext()) {	
						
						String itemID_ = null;
						YFCElement itemElem = (YFCElement) yfcItr_.next();
						if (itemElem.hasAttribute("ItemID")) {
							itemID_ = itemElem.getAttribute("ItemID");
							if (YFCObject.isNull(itemID_) || YFCObject.isVoid(itemID_)) {
								throw new Exception("Attribute ItemID Cannot be NULL or Void!");
							}
						} else {
							throw new Exception("Attribute ItemID Not Available In Output Template!");
						}
						
						if (itemID_.equalsIgnoreCase(itemID)) {
							stockType = "DIRECT";
							break;
						} else {
							if (lineType.equalsIgnoreCase("M") || lineType.equalsIgnoreCase("C")) {
								stockType = "STOCK";
							}
						}
					}
				}
			}
		}
		if(log.isDebugEnabled()){
			log.debug("ExtnLineType:" + stockType);
			log.debug("*****************************************************************************************************");
		}
		
		return stockType;
	}

	private String getInventoryIndicator(YFSEnvironment env, String itemId, String shipNode, String envtId, String lineType) throws Exception {
		if(log.isDebugEnabled()){
			log.debug("**************************************GetInventoryIndicator*******************************************");
		
			log.debug("ItemID:" + itemId);
			log.debug("ShipNode:" + shipNode);
			log.debug("EnvtId:" + envtId);
			log.debug("LineType:" + lineType);
		}

		boolean callItemList = false;
		String invIndicator = null;
		String stockType = null;

		YFCDocument getItemExtnListInXML = YFCDocument.getDocumentFor("<XPXItemExtn/>");
		YFCElement getItemExtnListEle = getItemExtnListInXML.getDocumentElement();
		getItemExtnListEle.setAttribute("ItemID", itemId);
		getItemExtnListEle.setAttribute("EnvironmentID", envtId);

		/* start - changes made to fix Issue 1501 */
		if (shipNode.contains("_")) {
			// Need to remove the environment id from ship node.
			String[] splitArrayOnUom = shipNode.split("_");
			if (splitArrayOnUom.length > 0) {
				shipNode = splitArrayOnUom[0];
			}
		}
		getItemExtnListEle.setAttribute("XPXDivision", shipNode);
		/* End - changes made to fix Issue 1501 */

		if(log.isDebugEnabled()){
			log.debug("XPXItemBranchListService-InXML:" + getItemExtnListInXML.getString());
		}
		YFCDocument getItemExtnListOutXML = null;
		Document tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXItemBranchListService", getItemExtnListInXML.getDocument());
		if (tempDoc != null) {
			getItemExtnListOutXML = YFCDocument.getDocumentFor(tempDoc);
			YFCElement getItemExtnListOutEle = getItemExtnListOutXML.getDocumentElement();
			YFCElement itemExtnEle = getItemExtnListOutEle.getChildElement("XPXItemExtn");
			if (itemExtnEle != null) {
				if (itemExtnEle.hasAttribute("InventoryIndicator")) {
					invIndicator = itemExtnEle.getAttribute("InventoryIndicator");
					if (YFCObject.isNull(invIndicator) || YFCObject.isVoid(invIndicator)) {
						callItemList = true;
					} else {
						/*
						 * changes made to fix Issue 1501 - Indicator 'I' has
						 * been added for Line type Stock.
						 */
						if ("W".equalsIgnoreCase(invIndicator) || "I".equalsIgnoreCase(invIndicator)) {
							stockType = "STOCK";
						} else if ("M".equalsIgnoreCase(invIndicator)) {
							stockType = "DIRECT";
						}
					}
				} else {
					callItemList = true;
				}
			} else {
				callItemList = true;
			}
		} else {
			callItemList = true;
		}

		if (callItemList) {
			YFCDocument getItemListOutXML = null;

			YFCDocument getItemListInXML = YFCDocument.getDocumentFor("<Item/>");
			YFCElement itemListEle = getItemListInXML.getDocumentElement();
			itemListEle.setAttribute("ItemID", itemId);

			if(log.isDebugEnabled()){
				log.debug("XPXGetItemList-InXML:" + getItemListInXML.getString());
			}
			tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXGetItemList", getItemListInXML.getDocument());
			if (tempDoc != null) {
				getItemListOutXML = YFCDocument.getDocumentFor(tempDoc);
				YFCElement _itemListEle = getItemListOutXML.getDocumentElement();
				YFCElement itemEle = _itemListEle.getChildElement("Item");
				if (itemEle != null) {
					stockType = "DIRECT";
				} else {
					if (lineType.equalsIgnoreCase("M") || lineType.equalsIgnoreCase("C")) {
						stockType = "STOCK";
					} else {
						// Item Doesn't Exist in DB.
						if (log.isDebugEnabled()) {
							log.debug("Item# "+itemId+" Doesn't Exist In Web/Xpedx.com");
						}
						throw new Exception("Item# "+itemId+" Doesn't Exist In Web.");
					}
				}
			}
		}

		if(log.isDebugEnabled()){
			log.debug("ExtnLineType:" + stockType);
			log.debug("*****************************************************************************************************");
		}
		return stockType;
	}

	private HashMap<String,String> getLegacyItemNoFromXRef(YFSEnvironment env, String companyCode, String envtId, String custNo, Set<String> custPartNoSet) throws Exception {

		HashMap<String,String> legacyItemNoMap = new HashMap<String,String>();
		
		YFCDocument getItemXRefListInXML = YFCDocument.getDocumentFor("<XPXItemcustXref/>");
		YFCElement getItemXRefListEle = getItemXRefListInXML.getDocumentElement();
		getItemXRefListEle.setAttribute("CompanyCode", companyCode);
		getItemXRefListEle.setAttribute("EnvironmentCode", envtId);
		getItemXRefListEle.setAttribute("CustomerNumber", custNo);
		// To Build Complex Query.
		YFCElement complexQueryElem = getItemXRefListEle.createChild("ComplexQuery"); 
		YFCElement orElement = complexQueryElem.createChild("Or");
		Iterator<String> itr = custPartNoSet.iterator();
		while (itr.hasNext()) {
			String custPartNo = (String) itr.next();
			YFCElement expEle = orElement.createChild("Exp");		
			expEle.setAttribute("Name", "CustomerItemNumber");
			expEle.setAttribute("Value", custPartNo);
			expEle.setAttribute("QryType", "EQ");
		}
		
		if (log.isDebugEnabled()) {
			log.debug("getItemXRefListInXML:" + getItemXRefListInXML.getString());
		}

		YFCDocument getItemXRefListOutXML = null;
		Document tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "getXrefList", getItemXRefListInXML.getDocument());
		if (tempDoc != null) {
			getItemXRefListOutXML = YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("getXrefList Service Returned NULL!");
		}
		
		if (log.isDebugEnabled()) {
			log.debug("getItemXRefListOutXML:" + getItemXRefListOutXML.getString());
		}

		YFCElement getItemXRefListOutEle = getItemXRefListOutXML.getDocumentElement();
		YFCIterable<YFCElement> yfcItr = getItemXRefListOutEle.getChildren("XPXItemcustXref");
		while (yfcItr.hasNext()) {	
			YFCElement itemCustXrefElem = (YFCElement) yfcItr.next();
			if (itemCustXrefElem != null) {
				String custItemNum = null;
				String legacyItemNum = null;
				if (itemCustXrefElem.hasAttribute("CustomerItemNumber")) {
					custItemNum = itemCustXrefElem.getAttribute("CustomerItemNumber");
				} else {
					throw new Exception("Attribute CustomerItemNumber Not Available in getXrefList Template!");
				}
				
				if (itemCustXrefElem.hasAttribute("LegacyItemNumber")) {
					legacyItemNum = itemCustXrefElem.getAttribute("LegacyItemNumber");
					if (YFCObject.isNull(legacyItemNum) || YFCObject.isVoid(legacyItemNum)) {
						throw new Exception("Attribute XPXItemcustXref/@LegacyItemNumber Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute LegacyItemNumber Not Available in getXrefList Template!");
				}
				legacyItemNoMap.put(custItemNum, legacyItemNum);
			}
		}
		
		return legacyItemNoMap;
	}
	
	private String getLegacyItemNoFromXRef(YFSEnvironment env, String companyCode, String envtId, String custNo, String custPartNo) throws Exception {

		String legacyCustNo = null;

		YFCDocument getItemXRefListInXML = YFCDocument.getDocumentFor("<XPXItemcustXref/>");
		YFCElement getItemXRefListEle = getItemXRefListInXML.getDocumentElement();
		getItemXRefListEle.setAttribute("CompanyCode", companyCode);
		getItemXRefListEle.setAttribute("EnvironmentCode", envtId);
		getItemXRefListEle.setAttribute("CustomerNumber", custNo);
		getItemXRefListEle.setAttribute("CustomerItemNumber", custPartNo);

		YFCDocument getItemXRefListOutXML = null;
		Document tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "getXrefList", getItemXRefListInXML.getDocument());
		if (tempDoc != null) {
			getItemXRefListOutXML = YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("getXrefList Service Returned NULL!");
		}

		YFCElement getItemXRefListOutEle = getItemXRefListOutXML.getDocumentElement();
		YFCElement getItemXRefEle = getItemXRefListOutEle.getChildElement("XPXItemcustXref");
		if (getItemXRefEle.hasAttribute("LegacyItemNumber")) {
			legacyCustNo = getItemXRefEle.getAttribute("LegacyItemNumber");
			if (YFCObject.isNull(legacyCustNo) || YFCObject.isVoid(legacyCustNo)) {
				throw new Exception("Attribute XPXItemcustXref/@LegacyItemNumber Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute LegacyItemNumber Not Available in getXrefList Template!");
		}

		return legacyCustNo;

	}

	/**
	 * Creates Fulfillment Order.
	 * 
	 * @param env
	 *            Environment variable.
	 * @param rootEle
	 *            Input xml from legacy system
	 * @param cOrderEle
	 *            Element contains the customer information.
	 * @return tempDoc Output document of the Create order API.
	 */
	private YFCDocument createFulfillmentOrder(YFSEnvironment env, YFCElement rootEle, YFCElement cOrderEle, YFCElement inXMLEle) throws Exception {

		rootEle.setAttribute("Action", "CREATE");
		if (rootEle.hasAttribute("OrderHeaderKey")) {
			rootEle.removeAttribute("OrderHeaderKey");
		}
		if (rootEle.hasAttribute("APIName")) {
			rootEle.removeAttribute("APIName");
		}
		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		if (rootExtnEle != null) {
			rootExtnEle.setAttribute("ExtnLegacyOrderNo", this.getLegacyOrderNo(inXMLEle));
			rootExtnEle.setAttribute("ExtnGenerationNo", this.getGenerationNo(inXMLEle));
		} else {
			throw new Exception("Order/Extn Element Not Available in Incoming Legacy Message!");
		}

		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if (rootOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				rootOrdLineEle.setAttribute("Action", "CREATE");
				if (rootOrdLineEle.hasAttribute("OrderLineKey")) {
					rootOrdLineEle.removeAttribute("OrderLineKey");
				}

				String webLineNo = null;
				YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
				if (rootOrdLineExtnEle != null) {
					if (rootOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
						webLineNo = rootOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
						if (YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
							throw new Exception("Extn/@ExtnWebLineNumber Cannot be NULL or Void!");
						}
						String legacyLineNo = this.getLegacyLineNoForWebLineNo(webLineNo, inXMLEle);
						rootOrdLineExtnEle.setAttribute("ExtnLegacyLineNumber", legacyLineNo);
						YFCElement rootOrdLineTranQtyEle = rootOrdLineEle.getChildElement("OrderLineTranQuantity");
						if (rootOrdLineTranQtyEle != null) {
							rootOrdLineTranQtyEle.setAttribute("OrderedQty", this.getOrderedQtyForWebLineNo(webLineNo, inXMLEle));
						}
					} else {
						throw new Exception("Attribute Extn/@ExtnWebLineNumber Not Available in Incoming Legacy Message!");
					}

					// Logic to set the order type. ie., STOCK or DIRECT
					if (rootOrdLineExtnEle.hasAttribute("ExtnLineType")) {
						String extnLineType = rootOrdLineExtnEle.getAttribute("ExtnLineType");
						if (YFCObject.isNull(extnLineType) || YFCObject.isVoid(extnLineType)) {
							throw new Exception("ExtnLineType Cannot be NULL or Void!");
						}
						if (extnLineType.equalsIgnoreCase("DIRECT")) {
							rootEle.setAttribute("OrderType", "DIRECT_ORDER");
						} else if (extnLineType.equalsIgnoreCase("STOCK")) {
							rootEle.setAttribute("OrderType", "STOCK_ORDER");
						} else {
							throw new Exception("Invalid ExtnLineType in Fulfillment Order InXML!");
						}

					} else {
						throw new Exception("Attribute Order/OrderLine/Extn/@ExtnLineType Not Available in Fulfillment Order InXML!");
					}
				} else {
					throw new Exception("OrderLine/Extn Element Not Available in Incoming Legacy Message!");
				}

				// Mapping the customer order details with fulfillment order
				// details.
				YFCElement chainedFromNode = rootEle.getOwnerDocument().createElement("ChainedFrom");
				rootOrdLineEle.appendChild(chainedFromNode);

				String custOrdHeaderKey = cOrderEle.getAttribute("OrderHeaderKey");
				if (!YFCObject.isVoid(custOrdHeaderKey) && !YFCObject.isNull(custOrdHeaderKey)) {
					chainedFromNode.setAttribute("OrderHeaderKey", custOrdHeaderKey);
				} else {
					throw new Exception("OrderHeaderKey Cannot be NULL or Void!");
				}

				// To get the corresponding customer order line key based on web
				// line number.
				String custOrdLineKey = getOrderLineKeyForWebLineNo(webLineNo, cOrderEle);
				if (!YFCObject.isVoid(custOrdLineKey) && !YFCObject.isNull(custOrdLineKey)) {
					chainedFromNode.setAttribute("OrderLineKey", custOrdLineKey);
				} else {
					throw new Exception("OrderLineKey Cannot be NULL or Void!");
				}
			}
		} else {
			throw new Exception("Order/OrderLines Element Not Available in Incoming Legacy Message!");
		}

		filterAttributes(rootEle, false);
		if(log.isDebugEnabled()){
		log.debug("XPXCreateOrder_FO-InXML:" + rootEle.getString());
		}
		// To create fulfillment order.
		Document tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXCreateOrder", rootEle.getOwnerDocument().getDocument());
		if (tempDoc != null) {
			return YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("XPXCreateOrder Service Failed!");
		}
	}

	private YFCElement getChngOrdStatusInXML(String ordHeaderKey, String tranId, String toStatus) {

		YFCDocument chngOrdStatusInXML = YFCDocument.getDocumentFor("<OrderStatusChange/>");
		YFCElement chngOrdStatusEle = chngOrdStatusInXML.getDocumentElement();
		chngOrdStatusEle.setAttribute("OrderHeaderKey", ordHeaderKey);
		chngOrdStatusEle.setAttribute("IgnoreTransactionDependencies", "Y");
		chngOrdStatusEle.setAttribute("ChangeForAllAvailableQty", "Y");
		chngOrdStatusEle.setAttribute("TransactionId", tranId);
		chngOrdStatusEle.setAttribute("BaseDropStatus", toStatus);

		return chngOrdStatusEle;
	}

	private void performOrderStatusChange(YFSEnvironment env, YFCElement cAndfOrderEle, YFCElement cOrderEle, YFCElement fOrderEle, String toStatus, String hpc) throws Exception {

		TreeSet<String> chngfOrdStatusList1 = new TreeSet<String>();
		TreeSet<String> chngfOrdStatusList2 = new TreeSet<String>();

		YFCElement chngcOrdStatusEle = null;

		String fOrdHdrKey = fOrderEle.getAttribute("OrderHeaderKey");
		if (YFCObject.isNull(fOrdHdrKey) || YFCObject.isVoid(fOrdHdrKey)) {
			throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
		}

		String tranId0 = null;
		if (this._prop != null) {
			tranId0 = this._prop.getProperty("XPX_CHNED_ORD_STATUS_TXN");
			if (YFCObject.isNull(tranId0) || YFCObject.isVoid(tranId0)) {
				throw new Exception("TransactionID Not Configured in API Arguments!");
			}
		} else {
			throw new Exception("Arguments Not Configured For API-XPXPerformLegacyOrderUpdateAPI!");
		}

		String tranId1 = null;
		if (this._prop != null) {
			tranId1 = this._prop.getProperty("XPX_CHNG_ORD_STATUS_TXN");
			if (YFCObject.isNull(tranId1) || YFCObject.isVoid(tranId1)) {
				throw new Exception("TranstionID Not Configured in API Arguments!");
			}
		} else {
			throw new Exception("Arguments Not Configured For API-XPXPerformLegacyOrderUpdateAPI!");
		}

		if (hpc.equalsIgnoreCase("A")) {
			if (cAndfOrderEle != null) {
				YFCElement newfOrdEle = cAndfOrderEle.getOwnerDocument().importNode(fOrderEle, true);
				cAndfOrderEle.appendChild(newfOrdEle);
			} else {
				cAndfOrderEle = YFCDocument.getDocumentFor("<OrderList/>").getDocumentElement();
				YFCElement newcOrdEle = cAndfOrderEle.getOwnerDocument().importNode(cOrderEle, true);
				cAndfOrderEle.appendChild(newcOrdEle);
				YFCElement newfOrdEle = cAndfOrderEle.getOwnerDocument().importNode(fOrderEle, true);
				cAndfOrderEle.appendChild(newfOrdEle);
			}
		}

		YFCIterable<YFCElement> yfcItr = cAndfOrderEle.getChildren("Order");
		while (yfcItr.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr.next();
			if (ordEle.hasAttribute("OrderType")) {
				String ordType = ordEle.getAttribute("OrderType");
				if (YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void!");
				}

				String ordHdrKey = null;
				if (ordEle.hasAttribute("OrderHeaderKey")) {
					ordHdrKey = ordEle.getAttribute("OrderHeaderKey");
					if (YFCObject.isNull(ordHdrKey) || YFCObject.isVoid(ordHdrKey)) {
						throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute OrderHeaderKey Not Available in getOrderList Template!");
				}

				String maxOrdStatus = null;
				if (ordEle.hasAttribute("MaxOrderStatus")) {
					maxOrdStatus = ordEle.getAttribute("MaxOrderStatus");
					if (YFCObject.isNull(maxOrdStatus) || YFCObject.isVoid(maxOrdStatus)) {
						throw new Exception("Attribute MaxOrderStatus Cannot be NULL or Void!");
					}
					if (maxOrdStatus.equalsIgnoreCase("9000")) {
						maxOrdStatus = "1100.0100";
					}
				} else {
					throw new Exception("Attribute MaxOrderStatus Not Available in getOrderList Template!");
				}
				if (ordType.equalsIgnoreCase("Customer")) {
					chngcOrdStatusEle = getChngOrdStatusInXML(ordHdrKey, tranId0, "1100.0100");
				} else {
					YFCElement chngfOrdStatusEle = getChngOrdStatusInXML(ordHdrKey, tranId1, "1100.0100");
					chngfOrdStatusList1.add(chngfOrdStatusEle.getString());

					if (fOrdHdrKey.equalsIgnoreCase(ordHdrKey)) {
						YFCElement chngfOrdStatusEle1 = getChngOrdStatusInXML(ordHdrKey, tranId1, toStatus);
						chngfOrdStatusList2.add(chngfOrdStatusEle1.getString());
					} else {
						YFCElement chngfOrdStatusEle1 = getChngOrdStatusInXML(ordHdrKey, tranId1, maxOrdStatus);
						chngfOrdStatusList2.add(chngfOrdStatusEle1.getString());
					}
				}
			} else {
				throw new Exception("Attribute OrderType Not Available in OrderList Template!");
			}
		}
		if(log.isDebugEnabled()){
		log.debug("ChangeCOStatus-InXML:" + chngcOrdStatusEle.getString());
		}
		// To call changeOrderStatus API.
		Document tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrderStatus", chngcOrdStatusEle.getOwnerDocument().getDocument());
		if (tempDoc != null) {
			YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("Service XPXChangeOrderStatus Failed!");
		}

		Iterator<String> itr = chngfOrdStatusList1.iterator();
		while (itr.hasNext()) {
			String chngfOrdStatusStr = (String) itr.next();
			YFCElement chngfOrdStatusEle = YFCDocument.getDocumentFor(chngfOrdStatusStr).getDocumentElement();
			if(log.isDebugEnabled()){
			log.debug("ChangeFOStatus-InXML:" + chngfOrdStatusEle.getString());
			}
			// To call changeOrderStatus API.
			tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrderStatus", chngfOrdStatusEle.getOwnerDocument().getDocument());
			if (tempDoc != null) {
				YFCDocument.getDocumentFor(tempDoc);
			} else {
				throw new Exception("Service XPXChangeOrderStatus Failed!");
			}
		}

		itr = chngfOrdStatusList2.descendingIterator();
		while (itr.hasNext()) {
			String chngfOrdStatusStr = (String) itr.next();
			YFCElement chngfOrdStatusEle = YFCDocument.getDocumentFor(chngfOrdStatusStr).getDocumentElement();
			if(log.isDebugEnabled()){
			log.debug("ChangeFOStatus-InXML:" + chngfOrdStatusEle.getString());
			}
			// To call changeOrderStatus API.
			tempDoc = XPXPerformLegacyOrderUpdateExAPI.api.executeFlow(env, "XPXChangeOrderStatus", chngfOrdStatusEle.getOwnerDocument().getDocument());
			if (tempDoc != null) {
				YFCDocument.getDocumentFor(tempDoc);
			} else {
				throw new Exception("Service XPXChangeOrderStatus Failed!");
			}
		}
	}

	private String getOrderHeaderKeyForWebLineNo(String webLineNo, YFCElement ordEle) throws Exception {

		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if (ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if (extnEle != null) {
						String _webLineNo = null;
						if (extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							if (_webLineNo.equalsIgnoreCase(webLineNo)) {

								if(log.isDebugEnabled()){
									log.debug("webLineNo:" + webLineNo);
									log.debug("_webLineNo:" + _webLineNo);
									log.debug("OrderHeaderKeyCheck_Boolean:" + (_webLineNo.equalsIgnoreCase(webLineNo)));
								}

								if (ordLineEle.hasAttribute("OrderHeaderKey")) {
									String _ordHeaderKey = ordLineEle.getAttribute("OrderHeaderKey");
									if (YFCObject.isNull(_ordHeaderKey) || YFCObject.isVoid(_ordHeaderKey)) {
										throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
									}
									return _ordHeaderKey;
								} else {
									throw new Exception("Attribute OrderHeaderKey Not Available in OrderList Template!");
								}
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in the OrderList Template!");
						}
					} else {
						throw new Exception("OrderLine/Extn Element Not Available in the OrderList Template!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in the OrderList Template!");
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available in the OrderList Template!");
		}

		return null;
	}

	private String getGenerationNo(YFCElement ordEle) throws Exception {
		YFCElement ordExtnEle = ordEle.getChildElement("Extn");
		if (ordExtnEle != null) {
			String genNo = ordExtnEle.getAttribute("ExtnGenerationNo");
			if (YFCObject.isNull(genNo) || YFCObject.isVoid(genNo)) {
				throw new Exception("Attribute ExtnGenerationNo Cannot be NULL or Void!");
			}
			return genNo;
		} else {
			throw new Exception("Attribute ExtnGenerationNo Not Available In Incoming Legacy Message!");
		}
	}

	private String getLegacyOrderNo(YFCElement ordEle) throws Exception {
		YFCElement ordExtnEle = ordEle.getChildElement("Extn");
		if (ordExtnEle != null) {
			String legacyOrdNo = ordExtnEle.getAttribute("ExtnLegacyOrderNo");
			if (YFCObject.isNull(legacyOrdNo) || YFCObject.isVoid(legacyOrdNo)) {
				throw new Exception("Attribute ExtnLegacyOrderNo Cannot be NULL or Void!");
			}
			return legacyOrdNo;
		} else {
			throw new Exception("Attribute ExtnLegacyOrderNo Not Available In Incoming Legacy Message!");
		}
	}

	private String getLegacyLineNoForWebLineNo(String webLineNo, YFCElement ordEle) throws Exception {

		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if (ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if (extnEle != null) {
						if (extnEle.hasAttribute("ExtnWebLineNumber")) {
							String _webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtWebLineNumber Cannot be NULL or Void!");
							}
							if (_webLineNo.equalsIgnoreCase(webLineNo)) {
								if (extnEle.hasAttribute("ExtnLegacyLineNumber")) {
									String _extnLegacyLineNo = extnEle.getAttribute("ExtnLegacyLineNumber");
									if (YFCObject.isNull(_extnLegacyLineNo) || YFCObject.isVoid(_extnLegacyLineNo)) {
										throw new Exception("Attribute ExtnLegacyLineNumber Cannot be NULL or Void!");
									}
									return _extnLegacyLineNo;
								} else {
									throw new Exception("Attribute ExtnLegacyLineNumber Not Available in the OrderList Template!");
								}
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in the OrderList Template!");
						}
					} else {
						throw new Exception("OrderLine/Extn Element Not Available in the OrderList Template!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in the OrderList Template!");
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available in the OrderList Template!");
		}
		return null;
	}

	private String getOrderedQtyForWebLineNo(String webLineNo, YFCElement ordEle) throws Exception {

		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if (ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if (extnEle != null) {
						String _webLineNo = null;
						if (extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtWebLineNumber Cannot be NULL or Void!");
							}
							if (_webLineNo.equalsIgnoreCase(webLineNo)) {
								YFCElement ordLineTranQtyEle = ordLineEle.getChildElement("OrderLineTranQuantity");
								if (ordLineTranQtyEle != null) {
									if (ordLineTranQtyEle.hasAttribute("OrderedQty")) {
										String _ordQty = ordLineTranQtyEle.getAttribute("OrderedQty");
										if (YFCObject.isNull(_ordQty) || YFCObject.isVoid(_ordQty)) {
											throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
										}
										return _ordQty;
									} else {
										throw new Exception("Attribute OrderLineKey Not Available in the OrderList Template!");
									}
								}
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in the OrderList Template!");
						}
					} else {
						throw new Exception("OrderLine/Extn Element Not Available in the OrderList Template!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in the OrderList Template!");
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available in the OrderList Template!");
		}
		return null;
	}

	/**
	 * Gets the customer order line key to chain with the fulfillment order
	 * based on web line number.
	 * 
	 * Logic : Customer order xml is iterated and matched with the web line
	 * number & Legacy line number in the input xml(legacy xml). Customer order
	 * line key has been returned when there is a match.
	 * 
	 * @param webLineNo
	 *            Web Line number.
	 * @param legacyLineNo
	 *            Legacy Line Number.
	 * @param ordEle
	 *            Element contains the customer information.
	 * @return _ordLineKey Customer Order line key which needs to be chained
	 *         with the fulfillment order.
	 */

	private String getOrderLineKeyForWebLineNo(String webLineNo, YFCElement ordEle) throws Exception {

		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if (ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if (extnEle != null) {
						String _webLineNo = null;
						if (extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtWebLineNumber Cannot be NULL or Void!");
							}
							if (_webLineNo.equalsIgnoreCase(webLineNo)) {

								if(log.isDebugEnabled()){
									log.debug("webLineNo:" + webLineNo);
									log.debug("_webLineNo:" + _webLineNo);
									log.debug("OrderLineKeyCheck_Boolean:" + (_webLineNo.equalsIgnoreCase(webLineNo)));
								}

								if (ordLineEle.hasAttribute("OrderLineKey")) {
									String _ordLineKey = ordLineEle.getAttribute("OrderLineKey");
									if (YFCObject.isNull(_ordLineKey) || YFCObject.isVoid(_ordLineKey)) {
										throw new Exception("Attribute OrderLineKey Cannot be NULL or Void!");
									}
									return _ordLineKey;
								} else {
									throw new Exception("Attribute OrderLineKey Not Available in the OrderList Template!");
								}
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in the OrderList Template!");
						}
					} else {
						throw new Exception("OrderLine/Extn Element Not Available in the OrderList Template!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in the OrderList Template!");
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available in the OrderList Template!");
		}

		return null;
	}

	private YFCElement getOrderLineForWebLineNo(String webLineNo, YFCElement ordEle) throws Exception {

		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if (ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if (extnEle != null) {
						String _webLineNo = null;
						if (extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtWebLineNumber Cannot be NULL or Void!");
							}

							if (_webLineNo.equalsIgnoreCase(webLineNo)) {

								if(log.isDebugEnabled()){
									log.debug("webLineNo:" + webLineNo);
									log.debug("_webLineNo:" + _webLineNo);
									log.debug("OrderLineKeyCheck_Boolean:" + (_webLineNo.equalsIgnoreCase(webLineNo)));
								}

								return ordLineEle;
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in the OrderList Template!");
						}
					} else {
						throw new Exception("OrderLine/Extn Element Not Available in the OrderList Template!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in the OrderList Template!");
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available in the OrderList Template!");
		}

		return null;
	}

	private YFCElement prepareFOLineCancel(YFCElement ordLineEle) throws Exception {

		String ordLineKey = null;
		Map<String, String> tempMap = ordLineEle.getAttributes();
		Set<String> keySet = tempMap.keySet();
		Iterator<String> itr = keySet.iterator();
		while (itr.hasNext()) {
			String attr = (String) itr.next();
			if (attr.equalsIgnoreCase("OrderLineKey")) {
				ordLineKey = ordLineEle.getAttribute("OrderLineKey");
				if (YFCObject.isNull(ordLineKey) || YFCObject.isVoid(ordLineKey)) {
					throw new Exception("OrderLineKey Cannot be NULL or Void!");
				}
			}
			ordLineEle.removeAttribute(attr);
		}
		ordLineEle.setAttribute("Action", "CANCEL");
		ordLineEle.setAttribute("OrderLineKey", ordLineKey);
		ordLineEle.setAttribute("IsNewLine", "N");

		YFCIterable<YFCElement> yfcItr = ordLineEle.getChildren();
		while (yfcItr.hasNext()) {
			YFCElement yfcElement = (YFCElement) yfcItr.next();
			if (yfcElement.getNodeName().equalsIgnoreCase("Extn")) {
				String webLineNo = null;
				tempMap = yfcElement.getAttributes();
				keySet = tempMap.keySet();
				itr = keySet.iterator();
				while (itr.hasNext()) {
					String attr = (String) itr.next();
					if (attr.equalsIgnoreCase("ExtnWebLineNumber")) {
						webLineNo = yfcElement.getAttribute("ExtnWebLineNumber");
						if (YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
							throw new Exception("ExtnWebLineNumber Cannot be NULL or Void!");
						}
					}
					yfcElement.removeAttribute(attr);
				}
				yfcElement.setAttribute("ExtnWebLineNumber", webLineNo);
			}
			if (!yfcElement.getNodeName().equalsIgnoreCase("OrderLineTranQuantity") && !yfcElement.getNodeName().equalsIgnoreCase("Extn")) {
				ordLineEle.removeChild(yfcElement);
			}
		}

		YFCElement ordLineTranQty = ordLineEle.getChildElement("OrderLineTranQuantity");
		ordLineTranQty.setAttribute("OrderedQty", "0.0");

		return ordLineEle;
	}

	private List<String> getWebLineNosToArray(YFCElement rootEle) throws Exception {

		List<String> weblineNosArray = new ArrayList<String>();
		YFCElement ordLinesEle = rootEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if (ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if (extnEle != null) {
						if (extnEle.hasAttribute("ExtnWebLineNumber")) {
							weblineNosArray.add(extnEle.getAttribute("ExtnWebLineNumber"));
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available!");
						}
					} else {
						throw new Exception("OrderLine/Extn Element Not Available!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available!");
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available!");
		}

		return weblineNosArray;
	}

	private YFCElement getFulfillmentOrderEle(String hpc, String fOrdHeaderKey, YFCElement cAndfOrderEle) throws Exception {

		YFCIterable<YFCElement> yfcItr = cAndfOrderEle.getChildren("Order");
		while (yfcItr.hasNext()) {

			String ordType = null;
			String ordHeaderKey = null;

			YFCElement ordEle = (YFCElement) yfcItr.next();
			if (ordEle.hasAttribute("OrderType")) {
				ordType = ordEle.getAttribute("OrderType");
				if (YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute OrderType Not Available in OrderList Template!");
			}

			if (ordEle.hasAttribute("OrderHeaderKey")) 
			{
				ordHeaderKey = ordEle.getAttribute("OrderHeaderKey");
				if (YFCObject.isNull(ordHeaderKey) || YFCObject.isVoid(ordHeaderKey)) 
				{
					throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
				}
			} 
			else 
			{
				throw new Exception("Attribute OrderHeaderKey Not Available in OrderList Template!");
			}

			if (ordHeaderKey.equalsIgnoreCase(fOrdHeaderKey) && !ordType.equalsIgnoreCase("Customer")) {
				if ((!this.isCancelledOrder(ordEle) || hpc.equalsIgnoreCase("C"))) {
					return ordEle;
				} else if (this.isCancelledOrder(ordEle) && hpc.equalsIgnoreCase("A")) {
					// If we receive HeaderProcessCode 'A' for a Cancelled
					// Order.
					return ordEle;
				}
				else if (this.isCancelledOrder(ordEle) && hpc.equalsIgnoreCase("D")) 
				{
					return ordEle;
				}				
			}
		}

		return null;
	}

	private YFCElement getFulfillmentOrderEle(String hpc, YFCElement rootEle, YFCElement cAndfOrderEle) throws Exception {

		String isOrderEdit = "N";
		if (rootEle.hasAttribute("IsOrderEdit")) {
			isOrderEdit = rootEle.getAttribute("IsOrderEdit");
			if (YFCObject.isNull(isOrderEdit) || YFCObject.isVoid(isOrderEdit)) {
				isOrderEdit = "N";
			}
		}

		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		String extnWebConfNum = null;
		if (rootExtnEle.hasAttribute("ExtnWebConfNum")) {
			extnWebConfNum = rootExtnEle.getAttribute("ExtnWebConfNum");
			if (YFCObject.isNull(extnWebConfNum) || YFCObject.isVoid(extnWebConfNum)) {
				throw new Exception("Attribute ExtnWebConfNum Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute ExtnWebConfNum Not Available in Incoming Legacy Message!");
		}

		String extnLegacyOrdNum = null;
		if (rootExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
			extnLegacyOrdNum = rootExtnEle.getAttribute("ExtnLegacyOrderNo");
			if (YFCObject.isNull(extnLegacyOrdNum) || YFCObject.isVoid(extnLegacyOrdNum)) {
				throw new Exception("Attribute ExtnLegacyOrderNo Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute ExtnLegacyOrderNo Not Available in Incoming Legacy Message!");
		}

		String extnGenNum = null;
		if (rootExtnEle.hasAttribute("ExtnGenerationNo")) {
			extnGenNum = rootExtnEle.getAttribute("ExtnGenerationNo");
			if (YFCObject.isNull(extnGenNum) || YFCObject.isVoid(extnGenNum)) {
				throw new Exception("Attribute ExtnGenerationNo Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute ExtnGenerationNo Not Available in Incoming Legacy Message!");
		}

		YFCIterable<YFCElement> yfcItr = cAndfOrderEle.getChildren("Order");
		while (yfcItr.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr.next();
			if (ordEle.hasAttribute("OrderType")) {
				String webConfNum = null;
				String legacyOrdNum = null;
				String genNum = null;
				String ordType = ordEle.getAttribute("OrderType");
				if (YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void!");
				}
				YFCElement extnEle = ordEle.getChildElement("Extn");
				if (extnEle != null) {
					if (extnEle.hasAttribute("ExtnWebConfNum")) {
						webConfNum = extnEle.getAttribute("ExtnWebConfNum");
						if (YFCObject.isNull(webConfNum) || YFCObject.isVoid(webConfNum)) {
							throw new Exception("Attribute ExtnWebConfNum Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute ExtnWebConfNum Not Available in OrderList Template!");
					}

					if (extnEle.hasAttribute("ExtnLegacyOrderNo")) {
						legacyOrdNum = extnEle.getAttribute("ExtnLegacyOrderNo");
						if (YFCObject.isNull(legacyOrdNum) || YFCObject.isVoid(legacyOrdNum)) {
							legacyOrdNum = "DUMMY";
						}
					} else {
						legacyOrdNum = "DUMMY";
					}

					if (extnEle.hasAttribute("ExtnGenerationNo")) {
						genNum = extnEle.getAttribute("ExtnGenerationNo");
						if (YFCObject.isNull(genNum) || YFCObject.isVoid(genNum)) {
							genNum = "DUMMY";
						}
					} else {
						genNum = "DUMMY";
					}
				} else {
					throw new Exception("OrderLine/Extn Element Not Available in OrderList Template!");
				}

				if(log.isDebugEnabled()){
					log.debug("webConfNum:" + webConfNum);
					log.debug("extnWebConfNum:" + extnWebConfNum);
					log.debug("extnLegacyOrdNum:" + extnLegacyOrdNum);
					log.debug("legacyOrdNum:" + legacyOrdNum);
					log.debug("genNum:" + genNum);
					log.debug("extnGenNum:" + extnGenNum);
					log.debug("Boolean1:"
						+ (!ordType.equalsIgnoreCase("Customer") && (webConfNum.equalsIgnoreCase(extnWebConfNum)) && (extnLegacyOrdNum.equalsIgnoreCase(legacyOrdNum)) && (genNum
								.equalsIgnoreCase(extnGenNum))));
					log.debug("Boolean2:" + (!this.isCancelledOrder(ordEle) || hpc.equalsIgnoreCase("C")));
				}

				if (!ordType.equalsIgnoreCase("Customer") && (webConfNum.equalsIgnoreCase(extnWebConfNum)) && (extnLegacyOrdNum.equalsIgnoreCase(legacyOrdNum))
						&& (genNum.equalsIgnoreCase(extnGenNum))) {
					if (isOrderEdit.equalsIgnoreCase("N")) {
						if (!this.isCancelledOrder(ordEle) || hpc.equalsIgnoreCase("C")) {
							return ordEle;
						} 
						else if (this.isCancelledOrder(ordEle) && hpc.equalsIgnoreCase("A")) 
						{
							// If we receive HeaderProcessCode 'A' for a
							// Cancelled Order.
							return ordEle;
						}
						else if (this.isCancelledOrder(ordEle) && hpc.equalsIgnoreCase("D")) 
						{
							return ordEle;
						}					
					} 
					else 
					{
						return ordEle;
					}
				}
			} else {
				throw new Exception("Attribute OrderType Not Available in OrderList Template!");
			}
		}

		return null;
	}

	private YFCElement getCustomerOrderEle(YFSEnvironment env, String hpc, YFCElement rootEle, YFCElement cAndfOrderEle) throws Exception {
		YFCElement retOrdEle = null;

		String isOrderEdit = "N";
		if (rootEle.hasAttribute("IsOrderEdit")) {
			isOrderEdit = rootEle.getAttribute("IsOrderEdit");
			if (YFCObject.isNull(isOrderEdit) || YFCObject.isVoid(isOrderEdit)) {
				isOrderEdit = "N";
			}
		}

		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		String extnWebConfNum = null;
		if (rootExtnEle.hasAttribute("ExtnWebConfNum")) {
			extnWebConfNum = rootExtnEle.getAttribute("ExtnWebConfNum");
		} else {
			throw new Exception("Attribute ExtnWebConfNum Not Available in Incoming Legacy Message!");
		}

		YFCIterable<YFCElement> yfcItr = cAndfOrderEle.getChildren("Order");
		while (yfcItr.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr.next();
			if (ordEle.hasAttribute("OrderType")) {
				String webConfNum = null;

				String ordType = ordEle.getAttribute("OrderType");
				if (YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void!");
				}

				YFCElement extnEle = ordEle.getChildElement("Extn");
				if (extnEle != null) {
					if (extnEle.hasAttribute("ExtnWebConfNum")) {
						webConfNum = extnEle.getAttribute("ExtnWebConfNum");
					} else {
						throw new Exception("Attribute ExtnWebConfNum Not Available in OrderList Template!");
					}
				} else {
					throw new Exception("OrderLine/Extn Element Not Available in OrderList Template!");
				}
				if (ordType.equalsIgnoreCase("Customer") && (webConfNum.equalsIgnoreCase(extnWebConfNum))) {
					if (isOrderEdit.equalsIgnoreCase("N")) {
						if (!this.isCancelledOrder(ordEle) || hpc.equalsIgnoreCase("C")) {
							retOrdEle = ordEle;
						}
					} else {
						retOrdEle = ordEle;
					}
				} else {
					// Stamp Extended Price Info
					this.getOrderExtendedPriceInfo(rootEle);
				}
			} else {
				throw new Exception("Attribute OrderType Not Available in OrderList Template!");
			}
		}
		return retOrdEle;
	}

	private void getOrderExtendedPriceInfo(YFCElement rootEle) throws Exception {

		YFCElement rootExtnEle = rootEle.getChildElement("Extn");

		String extnLegOrdTotAdj = null;
		if (rootExtnEle.hasAttribute("ExtnLegTotOrderAdjustments")) {
			extnLegOrdTotAdj = rootExtnEle.getAttribute("ExtnLegTotOrderAdjustments");
			if (YFCObject.isNull(extnLegOrdTotAdj) || YFCObject.isVoid(extnLegOrdTotAdj)) {
				extnLegOrdTotAdj = "0.0";
				rootExtnEle.setAttribute("ExtnLegTotOrderAdjustments", extnLegOrdTotAdj);
			}
		} else {
			extnLegOrdTotAdj = "0.0";
			rootExtnEle.setAttribute("ExtnLegTotOrderAdjustments", extnLegOrdTotAdj);
		}		

		String extnTotOrdFreight = null;
		if (rootExtnEle.hasAttribute("ExtnTotalOrderFreight")) {
			extnTotOrdFreight = rootExtnEle.getAttribute("ExtnTotalOrderFreight");
			if (YFCObject.isNull(extnTotOrdFreight) || YFCObject.isVoid(extnTotOrdFreight)) {
				extnTotOrdFreight = "0.0";
				rootExtnEle.setAttribute("ExtnTotalOrderFreight", extnTotOrdFreight);
			}
		} else {
			extnTotOrdFreight = "0.0";
			rootExtnEle.setAttribute("ExtnTotalOrderFreight", extnTotOrdFreight);
		}

		String extnOrdTax = null;
		if (rootExtnEle.hasAttribute("ExtnOrderTax")) {
			extnOrdTax = rootExtnEle.getAttribute("ExtnOrderTax");
			if (YFCObject.isNull(extnOrdTax) || YFCObject.isVoid(extnOrdTax)) {
				extnOrdTax = "0.0";
				rootExtnEle.setAttribute("ExtnOrderTax", extnOrdTax);
			}
		} else {
			extnOrdTax = "0.0";
			rootExtnEle.setAttribute("ExtnOrderTax", extnOrdTax);
		}
		
		String extnTotOrdValue = null;
		if (rootExtnEle.hasAttribute("ExtnTotalOrderValue")) {
			extnTotOrdValue = rootExtnEle.getAttribute("ExtnTotalOrderValue");
			if (YFCObject.isNull(extnTotOrdValue) || YFCObject.isVoid(extnTotOrdValue)) {
				extnTotOrdValue = "0.0";
				rootExtnEle.setAttribute("ExtnTotalOrderValue", extnTotOrdValue);
			}
		} else {
			extnTotOrdValue = "0.0";
			rootExtnEle.setAttribute("ExtnTotalOrderValue", extnTotOrdValue);
		}
		
		double dExtnOrdTotWithoutTax = Double.parseDouble(extnTotOrdValue) - (Double.parseDouble(extnOrdTax) + Double.parseDouble(extnTotOrdFreight));
		rootExtnEle.setAttribute("ExtnTotOrdValWithoutTaxes", new Double(dExtnOrdTotWithoutTax).toString());

		String subOrdTot = this.getOrderLineExtendedPriceInfo(rootEle);
		rootExtnEle.setAttribute("ExtnOrderSubTotal", subOrdTot);
	}

	private String getOrderLineExtendedPriceInfo(YFCElement rootEle) throws Exception {
			
		double dExtnOrdSubTotal = 0.0;
		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if (rootOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
				if (rootOrdLineExtnEle != null) {
					
					String extnLegTotOrdLineAdj = null;
					if (rootOrdLineExtnEle.hasAttribute("ExtnLegOrderLineAdjustments")) {
						extnLegTotOrdLineAdj = rootOrdLineExtnEle.getAttribute("ExtnLegOrderLineAdjustments");
						if (YFCObject.isNull(extnLegTotOrdLineAdj) || YFCObject.isVoid(extnLegTotOrdLineAdj)) {
							extnLegTotOrdLineAdj = "0.0";
							rootOrdLineExtnEle.setAttribute("ExtnLegOrderLineAdjustments", extnLegTotOrdLineAdj);
						}
					} else {
						extnLegTotOrdLineAdj = "0.0";
						rootOrdLineExtnEle.setAttribute("ExtnLegOrderLineAdjustments", extnLegTotOrdLineAdj);
					}
						
					String extnLineOrdTotal = null;
					if (rootOrdLineExtnEle.hasAttribute("ExtnLineOrderedTotal")) {
						extnLineOrdTotal = rootOrdLineExtnEle.getAttribute("ExtnLineOrderedTotal");
						if (YFCObject.isNull(extnLineOrdTotal) || YFCObject.isVoid(extnLineOrdTotal)) {
							extnLineOrdTotal = "0.0";
							rootOrdLineExtnEle.setAttribute("ExtnLineOrderedTotal", extnLineOrdTotal);
						}
					} else {
						extnLineOrdTotal = "0.0";
						rootOrdLineExtnEle.setAttribute("ExtnLineOrderedTotal", extnLineOrdTotal);
					}
					
					// To Set Extended Price.
					double dExtnExtendedPrice = Double.parseDouble(extnLineOrdTotal) + Double.parseDouble(extnLegTotOrdLineAdj);
					rootOrdLineExtnEle.setAttribute("ExtnExtendedPrice", new Double(dExtnExtendedPrice).toString());
				    // To Calculate Order Sub Total.
					dExtnOrdSubTotal = dExtnOrdSubTotal + Double.parseDouble(extnLineOrdTotal);
				}
			}
		}
		return new Double(dExtnOrdSubTotal).toString();
	}

	private YFCElement setInstructionKeysOnException(YFCElement rootEle, YFCElement ordEle) throws Exception {

		String orderHeaderKey = null;

		if (rootEle.hasAttribute("OrderHeaderKey")) {
			orderHeaderKey = rootEle.getAttribute("OrderHeaderKey");
		}

		YFCElement instructionsEle = rootEle.getChildElement("Instructions");
		if (instructionsEle != null) {
			YFCIterable<YFCElement> yfcItr = instructionsEle.getChildren("Instruction");
			while (yfcItr.hasNext()) {
				YFCElement instructionEle = (YFCElement) yfcItr.next();
				if (instructionEle.hasAttribute("InstructionType")) {
					String instType = instructionEle.getAttribute("InstructionType");
					if (!YFCObject.isNull(instType) && !YFCObject.isVoid(instType)) {
						if (instType.equalsIgnoreCase("HEADER")) {
							if (!YFCObject.isNull(orderHeaderKey) && !YFCObject.isVoid(orderHeaderKey)) {
								String instDtlKey = getOrderInstructionKeyOnException(orderHeaderKey, ordEle);
								if (!YFCObject.isNull(instDtlKey) && !YFCObject.isVoid(instDtlKey)) {
									instructionEle.setAttribute("InstructionDetailKey", instDtlKey);
								}
							}
						}
					}
				}
			}
		}

		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if (rootOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				YFCElement rootLineInstructionsEle = rootOrdLineEle.getChildElement("Instructions");
				if (rootLineInstructionsEle != null) {
					YFCIterable<YFCElement> yfcItr1 = rootLineInstructionsEle.getChildren("Instruction");
					while (yfcItr1.hasNext()) {
						YFCElement rootLineInstructionEle = (YFCElement) yfcItr1.next();
						if (rootLineInstructionEle.hasAttribute("InstructionType")) {
							String lineInstType = rootLineInstructionEle.getAttribute("InstructionType");
							if (!YFCObject.isNull(lineInstType) && !YFCObject.isVoid(lineInstType)) {
								if (lineInstType.equalsIgnoreCase("LINE")) {
									YFCElement rootLineExtnEle = rootOrdLineEle.getChildElement("Extn");
									if (rootLineExtnEle != null) {
										String extnWebLineNum = null;
										if (rootLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
											extnWebLineNum = rootLineExtnEle.getAttribute("ExtnWebLineNumber");
										}

										if (!YFCObject.isNull(extnWebLineNum) && !YFCObject.isVoid(extnWebLineNum)) {
											YFCElement _ordLineEle = this.getOrderLineForWebLineNo(extnWebLineNum, ordEle);
											if (_ordLineEle != null) {
												String instDtlKey = getOrderLineInstructionKey(extnWebLineNum, _ordLineEle);
												if (!YFCObject.isNull(instDtlKey) && !YFCObject.isVoid(instDtlKey)) {
													rootLineInstructionEle.setAttribute("InstructionDetailKey", instDtlKey);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return rootEle;
	}

	private YFCElement setInstructionKeys(YFCElement rootEle, YFCElement ordEle) throws Exception {

		YFCElement instructionsEle = rootEle.getChildElement("Instructions");
		if (instructionsEle != null) {
			YFCIterable<YFCElement> yfcItr = instructionsEle.getChildren("Instruction");
			while (yfcItr.hasNext()) {
				YFCElement instructionEle = (YFCElement) yfcItr.next();
				if (instructionEle.hasAttribute("InstructionType")) {
					String instType = instructionEle.getAttribute("InstructionType");
					if (!YFCObject.isNull(instType) && !YFCObject.isVoid(instType)) {
						if (instType.equalsIgnoreCase("HEADER")) {
							YFCElement rootExtnEle = rootEle.getChildElement("Extn");
							if (rootExtnEle != null) {
								String extnWebConfNum = null;
								if (rootExtnEle.hasAttribute("ExtnWebConfNum")) {
									extnWebConfNum = rootExtnEle.getAttribute("ExtnWebConfNum");
								}

								String extnLegacyOrderNo = null;
								if (rootExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
									extnLegacyOrderNo = rootExtnEle.getAttribute("ExtnLegacyOrderNo");
								}

								String genNo = null;
								if (rootExtnEle.hasAttribute("ExtnGenerationNo")) {
									genNo = rootExtnEle.getAttribute("ExtnGenerationNo");
								}

								if (!YFCObject.isNull(extnWebConfNum) && !YFCObject.isVoid(extnWebConfNum) && !YFCObject.isNull(extnLegacyOrderNo) && !YFCObject.isVoid(extnLegacyOrderNo)
										&& !YFCObject.isNull(genNo) && !YFCObject.isVoid(genNo)) {
									String instDtlKey = getOrderInstructionKey(extnWebConfNum, extnLegacyOrderNo, genNo, ordEle);
									if (!YFCObject.isNull(instDtlKey) && !YFCObject.isVoid(instDtlKey)) {
										instructionEle.setAttribute("InstructionDetailKey", instDtlKey);
										/*Begin - Changes made by Mitesh Parikh for JIRA 3248*/
										String headerProcessCode=rootEle.getAttribute("HeaderProcessCode");
										String newInstructionText=instructionEle.getAttribute("InstructionText");
										boolean isNewInstructionTextVoid=YFCObject.isVoid(newInstructionText);
										if(("D".equalsIgnoreCase(headerProcessCode) || "C".equalsIgnoreCase(headerProcessCode)) && (isNewInstructionTextVoid))
										{
											String instructionTextInDB = SCXmlUtil.getXpathAttribute((Element)ordEle.getDOMNode(),	"./Instructions/Instruction/@InstructionText");											
											instructionEle.setAttribute("InstructionText", instructionTextInDB);
											instructionEle.setAttribute("Action", "REMOVE");
										}
										/*End - Changes made by Mitesh Parikh for JIRA 3248*/
									}
								}
							}
						}
					}
				}
			}
		}

		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if (rootOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				YFCElement rootLineInstructionsEle = rootOrdLineEle.getChildElement("Instructions");
				if (rootLineInstructionsEle != null) {
					YFCIterable<YFCElement> yfcItr1 = rootLineInstructionsEle.getChildren("Instruction");
					while (yfcItr1.hasNext()) {
						YFCElement rootLineInstructionEle = (YFCElement) yfcItr1.next();
						if (rootLineInstructionEle.hasAttribute("InstructionType")) {
							String lineInstType = rootLineInstructionEle.getAttribute("InstructionType");
							if (!YFCObject.isNull(lineInstType) && !YFCObject.isVoid(lineInstType)) {
								if (lineInstType.equalsIgnoreCase("LINE")) {
									YFCElement rootLineExtnEle = rootOrdLineEle.getChildElement("Extn");
									if (rootLineExtnEle != null) {
										String extnWebLineNum = null;
										if (rootLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
											extnWebLineNum = rootLineExtnEle.getAttribute("ExtnWebLineNumber");
										}

										if (!YFCObject.isNull(extnWebLineNum) && !YFCObject.isVoid(extnWebLineNum)) {
											YFCElement _ordLineEle = this.getOrderLineForWebLineNo(extnWebLineNum, ordEle);
											if (_ordLineEle != null) {
												String instDtlKey = getOrderLineInstructionKey(extnWebLineNum, _ordLineEle);
												if (!YFCObject.isNull(instDtlKey) && !YFCObject.isVoid(instDtlKey)) {
													rootLineInstructionEle.setAttribute("InstructionDetailKey", instDtlKey);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return rootEle;
	}

	private String getOrderLineInstructionKey(String webLineNum, YFCElement ordLineEle) throws Exception {

		YFCElement ordLineExtnEle = ordLineEle.getChildElement("Extn");
		String _webLineNum = null;
		if (ordLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
			_webLineNum = ordLineExtnEle.getAttribute("ExtnWebLineNumber");
			if (YFCObject.isNull(_webLineNum) || YFCObject.isVoid(_webLineNum)) {
				throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute ExtnWebLineNumber Not Available in getOrderList Template!");
		}

		if (webLineNum.equalsIgnoreCase(_webLineNum)) {
			YFCElement instructionsEle = ordLineEle.getChildElement("Instructions");
			if (instructionsEle != null) {
				YFCIterable<YFCElement> yfcItr = instructionsEle.getChildren("Instruction");
				while (yfcItr.hasNext()) {
					YFCElement instructionEle = (YFCElement) yfcItr.next();
					if (instructionEle.hasAttribute("InstructionType")) {
						String instType = instructionEle.getAttribute("InstructionType");
						if (!YFCObject.isNull(instType) && !YFCObject.isVoid(instType)) {
							if (instType.equalsIgnoreCase("LINE")) {
								if (instructionEle.hasAttribute("InstructionDetailKey")) {
									String instDtlKey = instructionEle.getAttribute("InstructionDetailKey");
									if (!YFCObject.isNull(instDtlKey) && !YFCObject.isVoid(instDtlKey)) {
										return instDtlKey;
									}
								} else {
									throw new Exception("Attribute InstructionDetailKey Not Available in getOrderList Template!");
								}
							}
						}
					}
				}
			}
		}

		return null;
	}

	private String getOrderInstructionKeyOnException(String orderHeaderKey, YFCElement ordEle) throws Exception {

		String _orderHeaderKey = null;

		if (ordEle.hasAttribute("OrderHeaderKey")) {
			_orderHeaderKey = ordEle.getAttribute("OrderHeaderKey");
		}

		if (!YFCObject.isNull(_orderHeaderKey) && !YFCObject.isVoid(_orderHeaderKey) && orderHeaderKey.equalsIgnoreCase(_orderHeaderKey)) {
			YFCElement instructionsEle = ordEle.getChildElement("Instructions");
			if (instructionsEle != null) {
				YFCIterable<YFCElement> yfcItr = instructionsEle.getChildren("Instruction");
				while (yfcItr.hasNext()) {
					YFCElement instructionEle = (YFCElement) yfcItr.next();
					if (instructionEle.hasAttribute("InstructionType")) {
						String instType = instructionEle.getAttribute("InstructionType");
						if (!YFCObject.isNull(instType) && !YFCObject.isVoid(instType)) {
							if (instType.equalsIgnoreCase("HEADER")) {
								if (instructionEle.hasAttribute("InstructionDetailKey")) {
									String instDtlKey = instructionEle.getAttribute("InstructionDetailKey");
									if (!YFCObject.isNull(instDtlKey) && !YFCObject.isVoid(instDtlKey)) {
										return instDtlKey;
									}
								} else {
									throw new Exception("Attribute InstructionDetailKey Not Available in getOrderList Template!");
								}
							}
						}
					}
				}
			}
		}

		return null;
	}

	private String getOrderInstructionKey(String webConfNum, String legacyOrderNo, String genNo, YFCElement ordEle) throws Exception {

		YFCElement ordExtnEle = ordEle.getChildElement("Extn");
		String _WebConfNum = null;
		if (ordExtnEle.hasAttribute("ExtnWebConfNum")) {
			_WebConfNum = ordExtnEle.getAttribute("ExtnWebConfNum");
			if (YFCObject.isNull(_WebConfNum) || YFCObject.isVoid(_WebConfNum)) {
				throw new Exception("Attribute ExtnWebConfNum Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute ExtnWebConfNum Not Available in getOrderList Template!");
		}

		if (YFCObject.isNull(legacyOrderNo) || YFCObject.isVoid(legacyOrderNo)) {
			return null;
		}

		String _legacyOrderNo = "DUMMY";
		if (ordExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
			_legacyOrderNo = ordExtnEle.getAttribute("ExtnLegacyOrderNo");
			if (YFCObject.isNull(_legacyOrderNo) || YFCObject.isVoid(_legacyOrderNo)) {
				return null;
			}
		} else {
			return null;
		}

		if (YFCObject.isNull(genNo) || YFCObject.isVoid(genNo)) {
			return null;
		}

		String _genNo = "DUMMY";
		if (ordExtnEle.hasAttribute("ExtnGenerationNo")) {
			_genNo = ordExtnEle.getAttribute("ExtnGenerationNo");
			if (YFCObject.isNull(_genNo) || YFCObject.isVoid(_genNo)) {
				return null;
			}
		} else {
			return null;
		}

		if (webConfNum.equalsIgnoreCase(_WebConfNum) && legacyOrderNo.equalsIgnoreCase(_legacyOrderNo) && genNo.equalsIgnoreCase(_genNo)) {
			YFCElement instructionsEle = ordEle.getChildElement("Instructions");
			if (instructionsEle != null) {
				YFCIterable<YFCElement> yfcItr = instructionsEle.getChildren("Instruction");
				while (yfcItr.hasNext()) {
					YFCElement instructionEle = (YFCElement) yfcItr.next();
					if (instructionEle.hasAttribute("InstructionType")) {
						String instType = instructionEle.getAttribute("InstructionType");
						if (!YFCObject.isNull(instType) && !YFCObject.isVoid(instType)) {
							if (instType.equalsIgnoreCase("HEADER")) {
								if (instructionEle.hasAttribute("InstructionDetailKey")) {
									String instDtlKey = instructionEle.getAttribute("InstructionDetailKey");
									if (!YFCObject.isNull(instDtlKey) && !YFCObject.isVoid(instDtlKey)) {
										return instDtlKey;
									}
								} else {
									throw new Exception("Attribute InstructionDetailKey Not Available in getOrderList Template!");
								}
							}
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Gets the customer and fulfillment order details using web confirmation
	 * number.
	 * 
	 * @param env
	 *            Environment object.
	 * @param rootEle
	 *            Parent document element of the doc received from legacy.
	 * @param headerProcessCode
	 *            Code defined to do specific actions(A,C,D and S).
	 * @return Element with CO and FO details.
	 * 
	 */

	private YFCElement getCustomerOrderAndFulfillmentOrderList(YFSEnvironment env, YFCElement rootEle, String headerProcessCode) throws Exception {

		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		String extnWebConfNum = null;
		if (rootExtnEle != null) {
			if (rootExtnEle.hasAttribute("ExtnWebConfNum")) {
				extnWebConfNum = rootExtnEle.getAttribute("ExtnWebConfNum");
				if (YFCObject.isNull(extnWebConfNum) || YFCObject.isVoid(extnWebConfNum)) {
					throw new Exception("Attribute ExtnWebConfNum Cannot be NULL or Void in Incoming Legacy Message!");
				}
			} else {
				throw new Exception("Attribute ExtnWebConfNum Not Available in Incoming Legacy Message!");
			}
		} else {
			throw new Exception("Element Extn Not Available in Incoming Legacy Message!");
		}

		YFCDocument getOrdListInDoc = YFCDocument.getDocumentFor("<Order/>");
		YFCElement ordListInEle = getOrdListInDoc.getDocumentElement();

		YFCElement extnEle = getOrdListInDoc.createElement("Extn");
		ordListInEle.appendChild(extnEle);
		extnEle.setAttribute("ExtnWebConfNum", extnWebConfNum);
		if(log.isDebugEnabled()){
		log.debug("XPXPerformLegacyOrderUpdateAPI.getCustomerOrderAndFulfillmentOrderList()-InXML:" + getOrdListInDoc.getString());
		}

		YFCDocument getOrdListOutDoc = null;
		Document tempDoc = api.executeFlow(env, "XPXGetOrderListForLegacyOrderUpdateEx", getOrdListInDoc.getDocument());
		if (tempDoc != null) {
			getOrdListOutDoc = YFCDocument.getDocumentFor(tempDoc);
		if(log.isDebugEnabled()){
		log.debug("XPXPerformLegacyOrderUpdateAPI.getCustomerOrderAndFulfillmentOrderList()-OutXML:" + getOrdListOutDoc.getString());
		}	

		}

		if (getOrdListOutDoc != null) {
			YFCElement ordListOutEle = getOrdListOutDoc.getDocumentElement();
			if (ordListOutEle != null) {
				if (ordListOutEle.hasAttribute("TotalOrderList")) 
				{
					String totalOrdList = ordListOutEle.getAttribute("TotalOrderList");
					if (totalOrdList.equalsIgnoreCase("0")) {
						if (!headerProcessCode.equalsIgnoreCase("A")) {
							throw new Exception("Fulfillment Order Does Not Exist!");
						}
					} else {
						return getOrdListOutDoc.getDocumentElement();
					}
				}
			}
		}

		return null;
	}

	private void setProgressYFSEnvironmentVariables(YFSEnvironment env) {
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap<String, String> envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				envVariablesmap.put("isDiscountCalculate", "false");
				envVariablesmap.put("isPnACall", "false");
			} else {
				envVariablesmap = new HashMap<String, String>();
				envVariablesmap.put("isDiscountCalculate", "false");
				envVariablesmap.put("isPnACall", "false");
			}

			clientVersionSupport.setClientProperties(envVariablesmap);
		}
	}
	
	private Document setItemUOMListTemplate(YFSEnvironment env)
	{
		Document orderTemplateDoc = SCXmlUtil.createFromString(new StringBuffer()
		.append("<ItemUOMList>")
		.append(" <ItemUOM Quantity='' UnitOfMeasure='' >")
		.append("<Item ItemID='' OrganizationCode='' UnitOfMeasure=''/>")
		.append("</ItemUOM>")
		.append("</ItemUOMList>").toString());
		return orderTemplateDoc;
	}

	public void setReqUOMPrice(YFSEnvironment env, YFCElement rootEle) throws Exception {
		YFCElement itemUOMListEle = null;

		Set<String> itemIds = this.getItemIds(rootEle);
		if (itemIds.size() > 0) {
			YFCElement itemEle = this.getItemUOMListInXML(itemIds);

			if(log.isDebugEnabled()){
				log.debug("getItemUOMList-InXML:" + itemEle.getString());
			}
			Document getItemUOMListDoc = setItemUOMListTemplate(env);
			env.setApiTemplate("getItemUOMList", getItemUOMListDoc);
			Document temp = XPXPerformLegacyOrderUpdateExAPI.api.invoke(env, "getItemUOMList", itemEle.getOwnerDocument().getDocument());
			env.clearApiTemplate("getItemUOMList");
			if (temp != null) {
				itemUOMListEle = YFCDocument.getDocumentFor(temp).getDocumentElement();

				if(log.isDebugEnabled()){
					log.debug("getItemUOMList-OutXML:" + itemUOMListEle.getString());
				}
			} else {
				return;
			}
		} else {
			return;
		}

		YFCElement ordLinesEle = rootEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if (ordLineEle != null) {
					String lpc = null;
					if (ordLineEle.hasAttribute("LineProcessCode")) {
						lpc = ordLineEle.getAttribute("LineProcessCode");
						if (YFCObject.isNull(lpc) || YFCObject.isVoid(lpc)) {
							throw new Exception("Attribute LineProcessCode Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute LineProcessCode Not Available in Incoming Legacy Message!");
					}
					if (lpc.equalsIgnoreCase("A") || lpc.equalsIgnoreCase("C")) {
						String reqUOM = null;
						YFCElement ordLineTranQtyEle = ordLineEle.getChildElement("OrderLineTranQuantity");
						if (ordLineTranQtyEle != null) {
							if (ordLineTranQtyEle.hasAttribute("TransactionalUOM")) {
								reqUOM = ordLineTranQtyEle.getAttribute("TransactionalUOM");
							}
						}

						String prcUOM = null;
						String unitPrc = null;
						YFCElement ordLineExtnEle = ordLineEle.getChildElement("Extn");
						if (ordLineExtnEle != null) {
							if (ordLineExtnEle.hasAttribute("ExtnPricingUOM")) {
								prcUOM = ordLineExtnEle.getAttribute("ExtnPricingUOM");
							}
							if (ordLineExtnEle.hasAttribute("ExtnAdjUnitPrice")) {
								unitPrc = ordLineExtnEle.getAttribute("ExtnAdjUnitPrice");
							}
						}

						String itemId = null;
						YFCElement ordLineItemEle = ordLineEle.getChildElement("Item");
						if (ordLineItemEle != null) {
							if (ordLineItemEle.hasAttribute("ItemID")) {
								itemId = ordLineItemEle.getAttribute("ItemID");
							}
						}
						double reqUOMPrice = getReqUOMPrice(itemId, reqUOM, prcUOM, unitPrc, itemUOMListEle);
						if (ordLineExtnEle != null) {
							if (reqUOMPrice != 0) {
								ordLineExtnEle.setAttribute("ExtnReqUOMUnitPrice", new Double(reqUOMPrice).toString());
							} else {
								ordLineExtnEle.setAttribute("ExtnReqUOMUnitPrice", unitPrc);
							}
						}
					}
				}
			}
		}
	}

	private double getReqUOMPrice(String itemId, String reqUOM, String prcUOM, String unitPrice, YFCElement itemUOMListEle) throws Exception {

		String cnvrFctrPUOM = null;
		String cnvrFctrRUOM = null;

		YFCIterable<YFCElement> yfcItr = itemUOMListEle.getChildren("ItemUOM");
		while (yfcItr.hasNext()) {
			String uom = null;
			YFCElement itemUOMEle = (YFCElement) yfcItr.next();
			if (itemUOMEle != null) {
				YFCElement itemEle = itemUOMEle.getChildElement("Item");
				if (itemEle != null) {
					String uomItemId = null;
					if (itemEle.hasAttribute("ItemID")) {
						uomItemId = itemEle.getAttribute("ItemID");
						if (YFCObject.isNull(uomItemId) || YFCObject.isVoid(uomItemId)) {
							throw new Exception("Attribute ItemID Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute ItemID Not Available in getItemUOMList Template!");
					}
					if (uomItemId.equalsIgnoreCase(itemId)) {
						if (itemUOMEle.hasAttribute("UnitOfMeasure")) {
							uom = itemUOMEle.getAttribute("UnitOfMeasure");
							if (!YFCObject.isNull(uom) && !YFCObject.isVoid(uom)) {
								// PUOM - Conversion factor.
								if (uom.equalsIgnoreCase(prcUOM)) {
									cnvrFctrPUOM = itemUOMEle.getAttribute("Quantity");
								}
								// RUOM - Conversion factor.
								if (uom.equalsIgnoreCase(reqUOM)) {
									cnvrFctrRUOM = itemUOMEle.getAttribute("Quantity");
								}
							}
						} else {
							throw new Exception("Attribute UnitOfMeasure Not Available in getItemUOMList Template!");
						}
					}
				}
			}
		}

		if(log.isDebugEnabled()){
			log.debug("itemId:" + itemId);
			log.debug("reqUOM:" + reqUOM);
			log.debug("prcUOM:" + prcUOM);
			log.debug("unitPrice:" + unitPrice);
			log.debug("cnvrFctrPUOM:" + cnvrFctrPUOM);
			log.debug("cnvrFctrRUOM:" + cnvrFctrRUOM);
		}

		double dReqUOMPrice = 0.0;
		if (!YFCObject.isNull(unitPrice) && !YFCObject.isVoid(unitPrice) && !YFCObject.isNull(cnvrFctrPUOM) && !YFCObject.isVoid(cnvrFctrPUOM) && !YFCObject.isNull(cnvrFctrRUOM)
				&& !YFCObject.isVoid(cnvrFctrRUOM)) {
			dReqUOMPrice = (Double.parseDouble(unitPrice) / Double.parseDouble(cnvrFctrPUOM)) * Double.parseDouble(cnvrFctrRUOM);
		}
		if(log.isDebugEnabled()){
			log.debug("dReqUOMPrice:" + dReqUOMPrice);
		}

		return dReqUOMPrice;
	}

	private Set<String> getItemIds(YFCElement ordEle) {
		Set<String> set = new HashSet<String>();
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				String itemId = null;
				YFCElement ordLineItemEle = ordLineEle.getChildElement("Item");
				if (ordLineItemEle != null) {
					if (ordLineItemEle.hasAttribute("ItemID")) {
						itemId = ordLineItemEle.getAttribute("ItemID");
						if (!YFCObject.isNull(itemId) && !YFCObject.isVoid(itemId)) {
							set.add(itemId);
						}
					}
				}
			}
		}
		return set;
	}

	private YFCElement getItemUOMListInXML(Set<String> itemIds) {
		if (itemIds.size() > 0) {
			YFCElement itemEle = YFCDocument.getDocumentFor("<Item><ComplexQuery><Or/></ComplexQuery></Item>").getDocumentElement();
			YFCElement complexQryEle = itemEle.getChildElement("ComplexQuery");
			YFCElement OrEle = complexQryEle.getChildElement("Or");
			Iterator<String> itr = itemIds.iterator();
			while (itr.hasNext()) {
				String itemId = (String) itr.next();
				YFCElement expEle = OrEle.getOwnerDocument().createElement("Exp");
				expEle.setAttribute("Name", "ItemID");
				expEle.setAttribute("Value", itemId);
				expEle.setAttribute("QryType", "EQ");
				OrEle.appendChild(expEle);
			}
			return itemEle;
		}
		return null;
	}


	private void filterAttributes(YFCElement chngOrdEle, boolean isCustOrder) throws Exception {
		boolean isOrderDelete = false;
		boolean hasOtherLines = false;
		if (chngOrdEle.hasAttribute("HeaderProcessCode")) {
			String hpc = chngOrdEle.getAttribute("HeaderProcessCode");
			if (!YFCObject.isNull(hpc) && !YFCObject.isVoid(hpc)) {
				isOrderDelete = hpc.equalsIgnoreCase("D");
			}
		}
		if (isCustOrder) {
			if (chngOrdEle.getChildElement("Instructions") != null) {
				chngOrdEle.removeChild(chngOrdEle.getChildElement("Instructions"));
			}
		} else {
			
			// Fulfillment Order - Remove the Instructions element if instruction text is empty.
			if (chngOrdEle.getChildElement("Instructions") != null) {
				YFCElement instructionsElem = chngOrdEle.getChildElement("Instructions");
				YFCIterable<YFCElement> yfcItr = instructionsElem.getChildren("Instruction");
				while (yfcItr.hasNext()) {
					YFCElement instrElem = (YFCElement) yfcItr.next();
					if (instrElem.hasAttribute("InstructionText")) {
						String instrText = instrElem.getAttribute("InstructionText");
						if (YFCObject.isNull(instrText) || YFCObject.isVoid(instrText)) {
							instructionsElem.removeChild(instrElem);
						}
					}
				}
				
			}
		}
		
		YFCElement chngOrdLinesEle = chngOrdEle.getChildElement("OrderLines");
		YFCIterable<YFCElement> yfcItr = chngOrdLinesEle.getChildren("OrderLine");
		while (yfcItr.hasNext()) {
			YFCElement chngOrdLineEle = (YFCElement) yfcItr.next();
			if (chngOrdLineEle.hasAttribute("PrimeLineNo")) {
				chngOrdLineEle.removeAttribute("PrimeLineNo");
			}
			if (chngOrdLineEle.hasAttribute("SubLineNo")) {
				chngOrdLineEle.removeAttribute("SubLineNo");
			}
			
			if (chngOrdLineEle.hasAttribute("OrderedQty")) {
				chngOrdLineEle.removeAttribute("OrderedQty");
			}
			
			if (chngOrdLineEle.hasAttribute("Action")) {
				String action = chngOrdLineEle.getAttribute("Action");
				if (!YFCObject.isNull(action) && !YFCObject.isVoid(action)) {
					if (action.equalsIgnoreCase("CREATE") && !isCustOrder) {
						setOrdLineScheduleQty(chngOrdLineEle);
					}
				}
			}
			if (isCustOrder) {
				if (chngOrdLineEle.getChildElement("Instructions") != null) {
					chngOrdLineEle.removeChild(chngOrdLineEle.getChildElement("Instructions"));
				}
			}
			String lpc = null;
			if (chngOrdLineEle.hasAttribute("LineProcessCode")) {
				lpc = chngOrdLineEle.getAttribute("LineProcessCode");
			}
			
			if (!YFCObject.isNull(lpc) && !YFCObject.isVoid(lpc)) {
				if (!lpc.equalsIgnoreCase("D")) {
					// Do Nothing
					hasOtherLines = true;
				}
			}
		}
		if(log.isDebugEnabled()){
			log.debug("hasOtherLines:" + hasOtherLines);
			log.debug("isOrderDelete:" + isOrderDelete);
		}
		
		if (!hasOtherLines || isOrderDelete) {
			if (chngOrdEle.hasAttribute("ReqDeliveryDate")) {
				chngOrdEle.removeAttribute("ReqDeliveryDate");
			}
			YFCElement shipTo = chngOrdEle.getChildElement("PersonInfoShipTo");
			if (shipTo != null) {
				chngOrdEle.removeChild(shipTo);
			}
			YFCElement billTo = chngOrdEle.getChildElement("PersonInfoBillTo");
			if (billTo != null) {
				chngOrdEle.removeChild(billTo);
			}
		}
		
		// To Validate Legacy Order Number and Generation Number are not stored for Customer Order.
		if (isCustOrder) {
			YFCElement extnElement = chngOrdEle.getChildElement("Extn");
			if (extnElement != null) {
				extnElement.setAttribute("ExtnLegacyOrderNo","");
				extnElement.setAttribute("ExtnGenerationNo", "");
			}
		}
	}
	private void setOrdLineScheduleQty(YFCElement chngOrdLineEle) throws Exception {
		YFCElement chngOrdLineTranQtyEle = chngOrdLineEle.getChildElement("OrderLineTranQuantity");
		if (chngOrdLineTranQtyEle != null) {
			String lineType = null;
			if (chngOrdLineEle.hasAttribute("LineType")) {
				lineType = chngOrdLineEle.getAttribute("LineType");
				if (YFCObject.isNull(lineType) || YFCObject.isVoid(lineType)) {
					throw new Exception("Attribute LineType Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute LineType Not Available in Incoming Legacy Message!");
			}

			String ordQty = null;
			if (chngOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
				ordQty = chngOrdLineTranQtyEle.getAttribute("OrderedQty");
			}

			String tuom = null;
			if (chngOrdLineTranQtyEle.hasAttribute("TransactionalUOM")) {
				tuom = chngOrdLineTranQtyEle.getAttribute("TransactionalUOM");
			}
			if (!YFCObject.isNull(ordQty) && !YFCObject.isVoid(ordQty) && ((!YFCObject.isNull(tuom) && !YFCObject.isVoid(tuom)) || lineType.equalsIgnoreCase("M"))) {
				YFCElement chngOrdLineSchsEle = chngOrdLineEle.getOwnerDocument().createElement("Schedules");
				chngOrdLineEle.appendChild(chngOrdLineSchsEle);

				YFCElement chngOrdLineSchEle = chngOrdLineSchsEle.getOwnerDocument().createElement("Schedule");
				chngOrdLineSchsEle.appendChild(chngOrdLineSchEle);

				YFCElement chngOrdLineSchTranQtyEle = chngOrdLineSchEle.getOwnerDocument().createElement("ScheduleTranQuantity");
				chngOrdLineSchEle.appendChild(chngOrdLineSchTranQtyEle);
				chngOrdLineSchTranQtyEle.setAttribute("ChangeInQuantity", ordQty);
				chngOrdLineSchTranQtyEle.setAttribute("TransactionalUOM", tuom);
			}
		}
	}
	
	private void removeDeletedLines(YFCElement rootEle,YFCElement fOrderEle) throws Exception {
		
		String webLineNo = null;
		String _webLineNo = null;
		List delWebLineNoList = new ArrayList();
		
		// To get the web line number of the cancelled lines in the database.
		YFCElement _orderLinesEle = fOrderEle.getChildElement("OrderLines");
		if (_orderLinesEle != null) {
			YFCIterable<YFCElement> _yfcItr = _orderLinesEle.getChildren("OrderLine");
			while(_yfcItr.hasNext()){
				YFCElement _orderLineElem = _yfcItr.next();
				YFCElement orderLineTranQtyElement = _orderLineElem.getChildElement("OrderLineTranQuantity");
				if(orderLineTranQtyElement.hasAttribute("OrderedQty")){
					String orderedTranQty = orderLineTranQtyElement.getAttribute("OrderedQty");
					if (YFCObject.isNull(orderedTranQty) || YFCObject.isVoid(orderedTranQty)) {
						throw new Exception("Attribute OrderedQty in OrderLineTranQuantity Cannot be NULL or Void!");
					} else {
						if (orderedTranQty.equalsIgnoreCase("0") 
								|| orderedTranQty.equalsIgnoreCase("0.0") 
									|| orderedTranQty.equalsIgnoreCase("0.00")) {							
							YFCElement _extnLineElem = _orderLineElem.getChildElement("Extn");
							if (_extnLineElem != null && _extnLineElem.hasAttribute("ExtnWebLineNumber")) {
								_webLineNo = _extnLineElem.getAttribute("ExtnWebLineNumber");	
							}
							delWebLineNoList.add(_webLineNo);
						}
					}
				}
			}
		}
		
		if (delWebLineNoList != null && !delWebLineNoList.isEmpty()) {
			
			// To remove the lines from Input XML with line process code 'D' which has already been deleted.
			YFCElement orderLinesEle = rootEle.getChildElement("OrderLines");
			if (orderLinesEle != null) {
				YFCIterable<YFCElement> yfcItr = orderLinesEle.getChildren("OrderLine");
				while(yfcItr.hasNext()){
					YFCElement orderLineElem = yfcItr.next();
					if(orderLineElem.hasAttribute("LineProcessCode")){
						String lpc = orderLineElem.getAttribute("LineProcessCode");
						if (YFCObject.isNull(lpc) || YFCObject.isVoid(lpc)) {
							throw new Exception("Attribute LineProcessCode Cannot be NULL or Void!");
						} else {
							if (lpc.equalsIgnoreCase("D")) {
								YFCElement extnLineElem = orderLineElem.getChildElement("Extn");
								if (extnLineElem != null && extnLineElem.hasAttribute("ExtnWebLineNumber")) {
									webLineNo = extnLineElem.getAttribute("ExtnWebLineNumber");
									if (delWebLineNoList.contains(webLineNo)) {
										orderLinesEle.removeChild(orderLineElem);
									}				
								}
							}
						}
					}
				}
			}
		}
	}

	private void validateInXML(YFCElement rootEle, String headerProcessCode) throws Exception {

		List<String> webLineNos = new ArrayList<String>();
		String isOrdPlace = rootEle.getAttribute("IsOrderPlace");
		String isOrdEdit = rootEle.getAttribute("IsOrderEdit");
		if ((YFCObject.isNull(isOrdPlace) || YFCObject.isVoid(isOrdPlace)) 
				&& (YFCObject.isNull(isOrdEdit) || YFCObject.isVoid(isOrdEdit))) {
			rootEle.setAttribute("IsOrderUpdate", "Y");
		}

		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		if (rootExtnEle != null) {
			rootExtnEle.setAttribute("ExtnIsReprocessibleFlag", "N");
		}

		String noBoSplit = rootEle.getAttribute("NoBoSplit");
		String ordStatus = rootEle.getAttribute("OrderStatus");

		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if (rootOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			if(!yfcItr.hasNext())
			{
				if(headerProcessCode.equalsIgnoreCase("A")){
					centExempt = true;
					throw new Exception("OrderLines Not Available In Incoming Legacy Message!");
				}	
			}
			
			while (yfcItr.hasNext()) {
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				String lpc = rootOrdLineEle.getAttribute("LineProcessCode");
				if (YFCObject.isNull(lpc) || YFCObject.isVoid(lpc)) {
					throw new Exception("Attribute LineProcessCode Cannot be NULL or Void!");
				}
				YFCElement rootOrdLineTranQtyEle = rootOrdLineEle.getChildElement("OrderLineTranQuantity");
				String orderedQuantity = rootOrdLineTranQtyEle.getAttribute("OrderedQty");
				YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
				if (rootOrdLineTranQtyEle != null && rootOrdLineExtnEle != null) {
					if (rootOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
						String webLineNo = rootOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
						if (YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo) && !lpc.equalsIgnoreCase("D")) {
							throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
						}
						webLineNos.add(webLineNo);
					} else {
						if (!lpc.equalsIgnoreCase("D")) {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in Incoming Legacy Message!");
						}
					}
					String reqShipOrdQty = rootOrdLineExtnEle.getAttribute("ExtnReqShipOrdQty");
					if (!YFCObject.isNull(ordStatus) && !YFCObject.isVoid(ordStatus) && !YFCObject.isNull(reqShipOrdQty) && !YFCObject.isVoid(reqShipOrdQty)) {
						if (ordStatus.equalsIgnoreCase("1100.5700")) {
							if (YFCObject.isNull(noBoSplit) || YFCObject.isVoid(noBoSplit) || noBoSplit.equalsIgnoreCase("N")) {
								// Back order Scenario
								Float fReqShipOrdQty = Float.parseFloat(reqShipOrdQty);
								rootOrdLineTranQtyEle.setAttribute("OrderedQty", fReqShipOrdQty.toString());
							} else {
								// Fill & Kill Scenario
								if (noBoSplit.equalsIgnoreCase("Y")) {
									rootEle.setAttribute("FillAndKill", "Y");
									Float fReqShipOrdQty = Float.parseFloat(reqShipOrdQty);
									Float fOrderedQuantity = Float.parseFloat(orderedQuantity);
									rootOrdLineTranQtyEle.setAttribute("OrderedQty", fReqShipOrdQty.toString());
									if (fOrderedQuantity > fReqShipOrdQty) {
										rootOrdLineEle.setAttribute("LineProcessCode", "D");
									}									
								}
							}
						}
					}

					if (!YFCObject.isNull(lpc) && !YFCObject.isVoid(lpc)) {
						if (lpc.equalsIgnoreCase("D") && (YFCObject.isNull(isOrdPlace) || !isOrdPlace.equalsIgnoreCase("Y"))) {
								rootOrdLineTranQtyEle.setAttribute("OrderedQty", Float.parseFloat("0"));
						}
					}
				}
			}
		} else {
			if (headerProcessCode.equalsIgnoreCase("A")) {
				centExempt = true;
				throw new Exception("OrderLines Not Available In Incoming Legacy Message!");
			}
		}

		Set<String> uniqueWebLineNos = new TreeSet<String>(webLineNos);
		if (webLineNos.size() > uniqueWebLineNos.size()) {
			throw new Exception("Duplicate ExtnWebLineNumber Found in Edit Order XML!");
		}
	}
	
	private boolean findUOMchange(String webLineNo,boolean webLineNoExists, YFCElement fOrderEle, YFCElement rootOrdLineTranQtyEle) throws Exception{
		
		String uom = null;
		String _uom = null;
		
		if(rootOrdLineTranQtyEle!= null && rootOrdLineTranQtyEle.hasAttribute("TransactionalUOM")){
			_uom = rootOrdLineTranQtyEle.getAttribute("TransactionalUOM");
		}
		
		if(webLineNoExists) {
			uom = getUOMForOrderLine(webLineNo,fOrderEle);
		} else {
			return false;
		}
		if(log.isDebugEnabled()){
			log.debug("");
			log.debug("UOM_InXML:" + _uom);
			log.debug("UOM_InFO:" + uom);
			log.debug("");
		}
		if(!YFCObject.isNull(uom) && !YFCObject.isNull(_uom) && 
				!YFCObject.isVoid(uom) && !YFCObject.isVoid(_uom) && !uom.equalsIgnoreCase(_uom)){
			return true;
		}
		return false;
	}
	
	private String getUOMForOrderLine(String webLineNo, YFCElement fOrderEle) throws Exception {
		
		YFCElement ordLinesEle = fOrderEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if (ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if (extnEle != null) {
						String _webLineNo = null;
						if (extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							if (_webLineNo.equalsIgnoreCase(webLineNo)) {
								YFCElement ordLineTranQtyEle = ordLineEle.getChildElement("OrderLineTranQuantity");
								if (ordLineTranQtyEle != null) {
									if (ordLineTranQtyEle.hasAttribute("TransactionalUOM")) {
										String uom = ordLineTranQtyEle.getAttribute("TransactionalUOM");
										if (YFCObject.isNull(uom) || YFCObject.isVoid(uom)) {
											throw new Exception("Attribute UOM Cannot be NULL or Void!");
										}
										return uom;
									} else {
										throw new Exception("Attribute TransactionalUOM Not Available in the OrderList Template!");
									}
								}
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in the OrderList Template!");
						}
					} else {
						throw new Exception("OrderLine/Extn Element Not Available in the OrderList Template!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in the OrderList Template!");
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available in the OrderList Template!");
		}

		return null;	
	}
	
	private void setExtendedPriceInfoCO(YFCElement chngcOrderEle, YFCElement chngfOrderEle, YFCElement cAndfOrderEle, String headerProcessCode) throws Exception {
			
		String _ordSubTotal = null;
		String ordSubTotal = null;
		String _orderHeaderKey = null;
		String orderHeaderKey = null;
		String _orderTotalAdj = null;
		String orderTotalAdj = null;
		String _orderTax = null;
		String orderTax = null;
		String _orderFreightCharge = null;
		String orderFreightCharge = null;
		
		// To get the modified informations from legacy.
		YFCElement orderExtnElement = chngfOrderEle.getChildElement("Extn");
		if (orderExtnElement != null) {
			// Order Sub Total from Legacy.
			if (orderExtnElement.hasAttribute("ExtnOrderSubTotal")) {
				_ordSubTotal = orderExtnElement.getAttribute("ExtnOrderSubTotal");
				if (YFCObject.isNull(_ordSubTotal) || YFCObject.isVoid(_ordSubTotal)) {
					_ordSubTotal = "0.0";
				}
			} else {				
				_ordSubTotal = "0.0";
				
			}
			// Order Adjustment from legacy.
			if (orderExtnElement.hasAttribute("ExtnLegTotOrderAdjustments")) {
				_orderTotalAdj = orderExtnElement.getAttribute("ExtnLegTotOrderAdjustments");
				if (YFCObject.isNull(_orderTotalAdj) || YFCObject.isVoid(_orderTotalAdj)) {
					_orderTotalAdj = "0.0";
				}
			} else {
				_orderTotalAdj = "0.0";
			}
			
			// Order Tax from legacy.
			if (orderExtnElement.hasAttribute("ExtnOrderTax")) {
				_orderTax = orderExtnElement.getAttribute("ExtnOrderTax");
				if (YFCObject.isNull(_orderTax) || YFCObject.isVoid(_orderTax)) {
					_orderTax = "0.0";
				}
			} else {
				_orderTax = "0.0";
			}
			
			// Order Freight Charge from Legacy.
			if (orderExtnElement.hasAttribute("ExtnTotalOrderFreight")) {
				_orderFreightCharge = orderExtnElement.getAttribute("ExtnTotalOrderFreight");
				if (YFCObject.isNull(_orderFreightCharge) || YFCObject.isVoid(_orderFreightCharge)) {
					_orderFreightCharge = "0.0";
				}
			} else {
				_orderFreightCharge = "0.0";
			}
		}
		
		// To get the FO Order Header Key.
		if (chngfOrderEle.hasAttribute("OrderHeaderKey")) {
			_orderHeaderKey = chngfOrderEle.getAttribute("OrderHeaderKey");
			if (YFCObject.isNull(_orderHeaderKey) || YFCObject.isVoid(_orderHeaderKey)) {
				if (!headerProcessCode.equalsIgnoreCase("A")) {
					throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
				} else {
					// OrderHeaderKey set as empty as there won't be OrderHeaderKey attribute for HeaderProcessCode A.
					_orderHeaderKey = "";
				}
			}
		} else if (headerProcessCode.equalsIgnoreCase("A")) {
			_orderHeaderKey = "";
		} else {
			throw new Exception("Attribute OrderHeaderKey Not Available In ChangeOrder XML!");
		}
		
		double cOrdSubTotal = 0.0;
		double cTotalOrdAdj = 0.0;
		double cOrderTax = 0.0;
		double cOrderFreightCharge = 0.0;
		double cTotalOrderValue = 0.0;
		double cTotalOrderValueWithoutTax = 0.0;
		YFCIterable<YFCElement> orderItr = cAndfOrderEle.getChildren("Order");
		while (orderItr.hasNext()) {
			YFCElement orderElem = (YFCElement) orderItr.next();
			
			if (orderElem.hasAttribute("OrderHeaderKey")) {
				orderHeaderKey = orderElem.getAttribute("OrderHeaderKey");
				if (YFCObject.isNull(orderHeaderKey) || YFCObject.isVoid(orderHeaderKey)) {
					throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute OrderHeaderKey Not Available In The Change Order XML!");
			}
			
			if (orderElem.hasAttribute("OrderType")) {
				String ordType = orderElem.getAttribute("OrderType");
				if (YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void!");
				}
							
				if(!ordType.equalsIgnoreCase("Customer") && !isCancelledOrder(orderElem)) {
					
					YFCElement fOrderExtnElement = orderElem.getChildElement("Extn");
					if (fOrderExtnElement != null) {
						if (fOrderExtnElement.hasAttribute("ExtnOrderSubTotal")) {
							ordSubTotal = fOrderExtnElement.getAttribute("ExtnOrderSubTotal");
							if (YFCObject.isNull(ordSubTotal) || YFCObject.isVoid(ordSubTotal)) {
								ordSubTotal = "0.0";
							}
						} else {
							ordSubTotal = "0.0";
						}
						
						if (fOrderExtnElement.hasAttribute("ExtnLegTotOrderAdjustments")) {
							orderTotalAdj = fOrderExtnElement.getAttribute("ExtnLegTotOrderAdjustments");
							if (YFCObject.isNull(orderTotalAdj) || YFCObject.isVoid(orderTotalAdj)) {
								orderTotalAdj = "0.0";
							}
						} else {
							orderTotalAdj = "0.0";
						}
						
						if (fOrderExtnElement.hasAttribute("ExtnOrderTax")) {
							orderTax = fOrderExtnElement.getAttribute("ExtnOrderTax");
							if (YFCObject.isNull(orderTax) || YFCObject.isVoid(orderTax)) {
								orderTax = "0.0";
							}
						} else {
							orderTax = "0.0";
						}
						
						if (fOrderExtnElement.hasAttribute("ExtnTotalOrderFreight")) {
							orderFreightCharge = fOrderExtnElement.getAttribute("ExtnTotalOrderFreight");
							if (YFCObject.isNull(orderFreightCharge) || YFCObject.isVoid(orderFreightCharge)) {
								orderFreightCharge = "0.0";
							}
						} else {
							orderFreightCharge = "0.0";
						}
					}
							
					if (orderHeaderKey.equalsIgnoreCase(_orderHeaderKey)) {
						if (!headerProcessCode.equalsIgnoreCase("D")) {
							cOrdSubTotal = cOrdSubTotal + Double.parseDouble(_ordSubTotal);
							cTotalOrdAdj = cTotalOrdAdj + Double.parseDouble(_orderTotalAdj);
							cOrderTax = cOrderTax + Double.parseDouble(_orderTax);
							cOrderFreightCharge = cOrderFreightCharge + Double.parseDouble(_orderFreightCharge);
						}
					} else {
						cOrdSubTotal = cOrdSubTotal + Double.parseDouble(ordSubTotal);
						cTotalOrdAdj = cTotalOrdAdj + Double.parseDouble(orderTotalAdj);
						cOrderTax = cOrderTax + Double.parseDouble(orderTax);
						cOrderFreightCharge = cOrderFreightCharge + Double.parseDouble(orderFreightCharge);
					}
				}
			}	
		}
		
		if (headerProcessCode.equalsIgnoreCase("A")) {
			cOrdSubTotal = cOrdSubTotal + Double.parseDouble(_ordSubTotal);
			cTotalOrdAdj = cTotalOrdAdj + Double.parseDouble(_orderTotalAdj);
			cOrderTax = cOrderTax + Double.parseDouble(_orderTax);
			cOrderFreightCharge = cOrderFreightCharge + Double.parseDouble(_orderFreightCharge);
		}
		
		cTotalOrderValueWithoutTax = (cOrdSubTotal - cTotalOrdAdj);
		cTotalOrderValue = (cOrdSubTotal - cTotalOrdAdj) + cOrderTax + cOrderFreightCharge;
		if(log.isDebugEnabled()){
			log.debug("");
			log.debug("ExtnOrderSubTotal:" + cOrdSubTotal);
			log.debug("ExtnLegTotOrderAdjustments:" + cTotalOrdAdj);
			log.debug("ExtnOrderTax:" + cOrderTax);
			log.debug("ExtnTotalOrderFreight:" + cOrderFreightCharge);
			log.debug("ExtnTotalOrderValue:" + cTotalOrderValue);
		}
		YFCElement ordExtnEle = chngcOrderEle.getChildElement("Extn");
		if (ordExtnEle != null && ! new Double(cTotalOrderValue).toString().equalsIgnoreCase("0.0")) {
			ordExtnEle.setAttribute("ExtnOrderSubTotal", new Double(cOrdSubTotal).toString());
			ordExtnEle.setAttribute("ExtnLegTotOrderAdjustments", new Double(cTotalOrdAdj).toString());
			ordExtnEle.setAttribute("ExtnOrderTax", new Double(cOrderTax).toString());
			ordExtnEle.setAttribute("ExtnTotalOrderFreight", new Double(cOrderFreightCharge).toString());
			ordExtnEle.setAttribute("ExtnTotalOrderValue", new Double(cTotalOrderValue).toString());
			ordExtnEle.setAttribute("ExtnTotOrdValWithoutTaxes", new Double(cTotalOrderValueWithoutTax).toString());
		}
		
		YFCElement ordLinesEle = chngcOrderEle.getChildElement("OrderLines");
		if (ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				String webLineNo = null;
				String orderLineTotal = null;
				String orderLineAdj = null;
				String orderLineExtPrice = null;
				HashMap<String,String> lineInfoMap = new HashMap<String,String>();
				
				// To Retrieve WebLineNo
				YFCElement ordLineExtnEle = ordLineEle.getChildElement("Extn");
				if (ordLineExtnEle != null) {
					if (ordLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
						webLineNo = ordLineExtnEle.getAttribute("ExtnWebLineNumber");
					} else {
						throw new Exception("Attribute ExtnWebLineNumber Not Available!");
					}
				} else {
					throw new Exception("OrderLine/Extn Element Not Available!");
				}
				
				// To get the order line details(Order Line Total, Order Line Adjustment and Line Extended Price) for the customer order line.
				lineInfoMap = getOrderLineDetailsForWebLineNo(webLineNo, chngfOrderEle, cAndfOrderEle, headerProcessCode);
				orderLineTotal = lineInfoMap.get("OrderLineTotal");
				if (YFCObject.isNull(orderLineTotal) || YFCObject.isVoid(orderLineTotal)) {					
					orderLineTotal = "0.0";
				}
				orderLineAdj = lineInfoMap.get("OrderLineAdj");
				if (YFCObject.isNull(orderLineAdj) || YFCObject.isVoid(orderLineAdj)) {					
					orderLineAdj = "0.0";
				}
				orderLineExtPrice = lineInfoMap.get("OrdLineExtPrice");
				if (YFCObject.isNull(orderLineExtPrice) || YFCObject.isVoid(orderLineExtPrice)) {					
					orderLineExtPrice = "0.0";
				}

				if (ordLineExtnEle != null) {
					ordLineExtnEle.setAttribute("ExtnLineOrderedTotal", orderLineTotal);
					ordLineExtnEle.setAttribute("ExtnExtendedPrice", orderLineExtPrice);
					ordLineExtnEle.setAttribute("ExtnLegOrderLineAdjustments", orderLineAdj);
				}
			}
					
			if (ordExtnEle != null) {
				ordExtnEle.setAttribute("ExtnLegacyOrderNo", "");
				ordExtnEle.setAttribute("ExtnGenerationNo", "");
			}
		}	
	}
	
	/**
	 * 	Gets the order line total for the web line number in Customer order line. 
	 *  The line total is calculated from the fulfillment order stored in database and the XML sent by legacy.
	 * 
	 * @param webLineNo
	 *            Web Line Number to identify the lines
	 * @param chngfOrderEle
	 *            Parent document element of the doc received from legacy.
	 * @param cAndfOrderEle
	 *            Parent document element of the list of orders stored in database for the Web Confirmation Number.
	 * @param headerProcessCode
	 *            Code defined to do specific actions(A,C,D and S).
	 * @return Order Line Total.
	 * 
	 */
	
	public HashMap<String, String> getOrderLineDetailsForWebLineNo(String webLineNo, YFCElement chngfOrderEle, YFCElement cAndfOrderEle, String headerProcessCode) throws Exception {
		
		HashMap<String,String> lineInfoMap = new HashMap<String,String>();
		String _webLineNo = null;
		double ordLineTotalDB = 0.0;
		double ordLineTotalLeg = 0.0;
		double ordLineAdjDB = 0.0;
		double ordLineAdjLeg = 0.0;
		double ordLineExtPrice = 0.0;
		String orderLineKey = "";
		String _orderLineKey = "";
		String lineProcessCode = null;
				
		// To get the line order total from the XML sent by Legacy.
		YFCElement orderLinesElem = chngfOrderEle.getChildElement("OrderLines");
		if (orderLinesElem != null) {
			YFCIterable<YFCElement> yfcItr = orderLinesElem.getChildren("OrderLine");
			while (yfcItr.hasNext()) {
				YFCElement orderLineElem = (YFCElement) yfcItr.next();
				
				YFCElement orderLineExtnElem = orderLineElem.getChildElement("Extn");
				if (orderLineExtnElem != null) {
					_webLineNo = orderLineExtnElem.getAttribute("ExtnWebLineNumber");
					if (YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
						throw new Exception("Attribute WebLineNo Cannot be Null or Void!");
					}
					
					if(webLineNo.equalsIgnoreCase(_webLineNo)) {
						
						// To Retrieve Line Process Code from XML sent by Legacy.
						if (orderLineElem.hasAttribute("LineProcessCode")) {
							lineProcessCode = orderLineElem.getAttribute("LineProcessCode");
							if (YFCObject.isNull(lineProcessCode) || YFCObject.isVoid(lineProcessCode)) {
								lineProcessCode = "";
							}
						} else {
							lineProcessCode = "";
						}
						
						// To retrieve Order Line Key from XML sent by Legacy.
						if(orderLineElem.hasAttribute("OrderLineKey")){
							orderLineKey = orderLineElem.getAttribute("OrderLineKey");
							if (YFCObject.isNull(orderLineKey) || YFCObject.isVoid(orderLineKey)) {
								if (!lineProcessCode.equalsIgnoreCase("A")) {
									throw new Exception("Attribute OrderLineKey Cannot be Null or Void!");
								} else {
									orderLineKey = "";
								}
							}
						} else if (lineProcessCode.equalsIgnoreCase("A")) { 
							orderLineKey = "";
						} else {
							throw new Exception("Attribute OrderLineKey Not Available In The ChangeOrder XML! ");
						}
						
						// Calculate order line total.
						String extnLineOrdTotal = orderLineExtnElem.getAttribute("ExtnLineOrderedTotal");
						if (!YFCObject.isNull(extnLineOrdTotal) && !YFCObject.isVoid(extnLineOrdTotal)) {
							ordLineTotalLeg = ordLineTotalLeg + Double.parseDouble(extnLineOrdTotal);
						}
						
						// Calculate order line Adjustment.
						String extnOrderLineAdj = orderLineExtnElem.getAttribute("ExtnLegOrderLineAdjustments");
						if (!YFCObject.isNull(extnOrderLineAdj) && !YFCObject.isVoid(extnOrderLineAdj)) {
							ordLineAdjLeg = ordLineAdjLeg + Double.parseDouble(extnOrderLineAdj);
						} 
						break;
					}					
				} 	
			}
		}
		
		// To get the line order total from the FO in database.
		YFCIterable<YFCElement> yfcItr = cAndfOrderEle.getChildren("Order");
		while (yfcItr.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr.next();
			if (ordEle.hasAttribute("OrderType")) {
				
				// To retrieve Order Type.
				String ordType = ordEle.getAttribute("OrderType");
				if (YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void!");
				}
				
				if (!ordType.equalsIgnoreCase("Customer") && !isCancelledOrder(ordEle)) {
					YFCElement _orderLinesElem = (YFCElement) ordEle.getChildElement("OrderLines");
					YFCIterable<YFCElement> _yfcItr = _orderLinesElem.getChildren("OrderLine");
					while (_yfcItr.hasNext()) {
						YFCElement _orderLineElem = (YFCElement) _yfcItr.next();
						YFCElement _orderLineExtnElem = _orderLineElem.getChildElement("Extn");
						if (_orderLineExtnElem != null) {
							
							// To retrieve web line number from the line in DB.
							_webLineNo = _orderLineExtnElem.getAttribute("ExtnWebLineNumber");
							if (YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute WebLineNo Cannot be Null or Void!");
							}
							
							if (webLineNo.equalsIgnoreCase(_webLineNo)) {
								// To retrieve order line key of the line stored in DB.
								if(_orderLineElem.hasAttribute("OrderLineKey")) {
									_orderLineKey = _orderLineElem.getAttribute("OrderLineKey");
									if (YFCObject.isNull(_orderLineKey) || YFCObject.isVoid(_orderLineKey)) {
										throw new Exception("Attribute OrderLineKey Cannot be Null or Void!");
									}
								} else {
									throw new Exception("Attribute OrderLineKey Not Available In The ChangeOrder XML! ");
								}
								
								String extnLineOrdTotal = _orderLineExtnElem.getAttribute("ExtnLineOrderedTotal");
								String extnLegTotalLineAdj = _orderLineExtnElem.getAttribute("ExtnLegOrderLineAdjustments");
								
								if (orderLineKey.equalsIgnoreCase(_orderLineKey) || (lineProcessCode.equalsIgnoreCase("A") && headerProcessCode.equalsIgnoreCase("C"))) {
									// Header Process Code is compared instead of Line Process Code as Legacy might not send Line information to delete an Order.
									if (!headerProcessCode.equalsIgnoreCase("D")) {
										ordLineTotalDB = ordLineTotalDB + ordLineTotalLeg;
										ordLineAdjDB = ordLineAdjDB + ordLineAdjLeg;
									}
								} else {
									ordLineTotalDB = ordLineTotalDB + Double.parseDouble(extnLineOrdTotal);
									ordLineAdjDB = ordLineAdjDB + Double.parseDouble(extnLegTotalLineAdj);
								}
							}
						}
					}
				}
			}
		}
		
		if (headerProcessCode.equalsIgnoreCase("A")) {
			ordLineTotalDB = ordLineTotalDB + ordLineTotalLeg;
			ordLineAdjDB = ordLineAdjDB + ordLineAdjLeg;
		}
		
		ordLineExtPrice = ordLineTotalDB + ordLineAdjDB;
			
		if(log.isDebugEnabled()){	
			log.debug("");
			log.debug("WebLineNo:" + webLineNo);
			log.debug("ordLineTotal:" + ordLineTotalDB);
			log.debug("ordLineAdj:" + ordLineAdjDB);
			log.debug("ordLineExtPrice:" + ordLineExtPrice);
		}
		
		lineInfoMap.put("OrderLineTotal", new Double(ordLineTotalDB).toString());
		lineInfoMap.put("OrderLineAdj", new Double(ordLineAdjDB).toString());
		lineInfoMap.put("OrdLineExtPrice", new Double(ordLineExtPrice).toString());
		return lineInfoMap;
	}
	
	private boolean hasOrderLines(YFCElement chngfOrderEle) {
	
		YFCElement orderLinesElem = chngfOrderEle.getChildElement("OrderLines");
		if ( orderLinesElem != null) {
			YFCIterable<YFCElement> yfcItr = orderLinesElem.getChildren("OrderLine");
			if (yfcItr.hasNext()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private void removeExtendedPriceInfoCO(YFCElement chngcOrderEle) {
		
		YFCElement extnElement = chngcOrderEle.getChildElement("Extn");
		if (extnElement != null && extnElement.hasAttribute("ExtnTotalOrderValue") ) {
			extnElement.removeAttribute("ExtnTotalOrderValue");
		}
	}
	
	private void resetPriceToZero(YFCElement fOrdExtnElem) {
		fOrdExtnElem.setAttribute("ExtnLegOrderLineAdjustments", "0.0");
		fOrdExtnElem.setAttribute("ExtnLineOrderedTotal", "0.0");
		fOrdExtnElem.setAttribute("ExtnLineShippableTotal", "0.0");
		fOrdExtnElem.setAttribute("ExtnExtendedPrice", "0.0");
	}

	public void setProperties(Properties _prop) throws Exception {
		this._prop = _prop;
	}
}