package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
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

import com.xpedx.nextgen.common.cent.ErrorLogger;
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
import com.yantra.yfs.japi.YFSException;

/**
 * Description: Updates/Creates/Deletes an order in Sterling based on the Order updates from Legacy system. 
 * 				Type of Order updates are identified by the process codes(A,C,D and S)
 * 
 * 				A - Add/create a new order.
 * 				C - Change in an order.
 * 				D - Delete an order.
 * 				S - Status change of an order.
 *              
 * @author mgandhi, jstaney.
 *
 */

public class XPXPerformLegacyOrderUpdateAPI implements YIFCustomApi {
	
	private static YIFApi api = null;
	private Properties _prop;
	private static YFCLogCategory log;
	
	static {
		
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException yifEx) {
			yifEx.printStackTrace();
		}
	}
	
	/**
	 *  Updates the customer and fulfillment order based on legacy order update.
	 *  	                                               
	 *	@param  env  Environment object.  	                        
	 *	@param  inXML  Order document thats been received from legacy system.  	                          
	 *	@return  Output xml document. 
	 *  
	 */
		
	public Document performLegacyOrderUpdate(YFSEnvironment env,Document inXML) throws Exception {
		
		boolean isAPISuccess = false;
		Exception APIException = null;
		YFCDocument returnToLegacyDoc = null;
		String headerProcessCode = null;
		String isOrderPlaceFlag = null;
		
		try {
			if(log.isDebugEnabled()){
			log.debug("**********************************************************************************************************************");
			log.debug("XPXPerformLegacyOrderUpdateAPI-InXML:"+YFCDocument.getDocumentFor(inXML).getString());
			}
			log.verbose("**********************************************************************************************************************");
			log.verbose("");
			log.verbose("XPXPerformLegacyOrderUpdateAPI-InXML:"+YFCDocument.getDocumentFor(inXML).getString());
			log.verbose("");

			YFCElement cAndfOrderEle = null;
			YFCElement fOrderEle = null;
			YFCElement cOrderEle = null;
						
			YFCDocument yfcDoc = YFCDocument.getDocumentFor(inXML);
			YFCElement rootEle = yfcDoc.getDocumentElement();
			
			// Retrieve the header process code from the input doc.
			if(rootEle.hasAttribute("HeaderProcessCode")) {
				headerProcessCode = rootEle.getAttribute("HeaderProcessCode");
				if(YFCObject.isNull(headerProcessCode) || YFCObject.isVoid(headerProcessCode)) {
					throw new Exception("Attribute HeaderProcessCode Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute HeaderProcessCode Not Available in Incoming Legace Message!");
			}
			
			validateInXML(rootEle);
			
			if(log.isDebugEnabled()){
			log.debug("InXML After Validation:"+YFCDocument.getDocumentFor(inXML).getString());
			}
			//Added by Prasanth Kumar M. to prevent rollback of the orders if this code is invoked in OPResponse flow
			if(rootEle.hasAttribute("IsOrderPlace")) {
				isOrderPlaceFlag = rootEle.getAttribute("IsOrderPlace");
				if(YFCObject.isNull(isOrderPlaceFlag) || YFCObject.isVoid(isOrderPlaceFlag)) {
					isOrderPlaceFlag = "N";
				}
			} else {
				isOrderPlaceFlag = "N";
			}
			
			log.verbose("HeaderProcessCode:"+headerProcessCode);
			
			// To set environment variables which are required for XPEDXOverrideGetOrderPriceUE.java
			setProgressYFSEnvironmentVariables(env);
			
			Document inDoc = YFCDocument.createDocument().getDocument();
			inDoc.appendChild(inDoc.importNode(rootEle.getDOMNode(), true));
			inDoc.renameNode(inDoc.getDocumentElement(), inDoc.getNamespaceURI(), "Order");
			YFCElement inXMLEle = YFCDocument.getDocumentFor(inDoc).getDocumentElement();
			
			// Validations done for the Xml received from legacy system. 
			if(cAndfOrderEle == null) {
				// To get customer and fulfillment order details if exist.
				cAndfOrderEle = getCustomerOrderAndFulfillmentOrderList(env, rootEle, headerProcessCode);
				if(cAndfOrderEle != null) {
					// To get Customer order details.
					cOrderEle = getCustomerOrderEle(env, headerProcessCode, rootEle, cAndfOrderEle);
					if(cOrderEle == null) {
						if(!headerProcessCode.equalsIgnoreCase("A")) {
							throw new Exception("Customer Order Does Not Exist in the System!");
						}
					} else {
						if(headerProcessCode.equalsIgnoreCase("A")) {
							this.getOrderExtendedPriceInfo(rootEle, null);
						}
					}
					
					// To get fulfillment order details.
					String fOrdHeaderKey = (String)env.getTxnObject("FOKEY");
					
					if(log.isDebugEnabled()){
					log.debug("Fulfillment OrderHeaderKey:"+fOrdHeaderKey);
					}
					log.verbose("");
					log.verbose("Fulfillment OrderHeaderKey:"+fOrdHeaderKey);
					
					if(YFCObject.isNull(fOrdHeaderKey) || YFCObject.isVoid(fOrdHeaderKey)) {
						fOrderEle = getFulfillmentOrderEle(headerProcessCode, rootEle, cAndfOrderEle);
					} else {
						fOrderEle = getFulfillmentOrderEle(headerProcessCode, fOrdHeaderKey, cAndfOrderEle);
					}
					
					if(fOrderEle == null) {
						if(!headerProcessCode.equalsIgnoreCase("A")) {
							throw new Exception("Fulfillment Order Does Not Exist in the System!");
						}
					} else {
						if(headerProcessCode.equalsIgnoreCase("A")) {
							if(!this.isCancelledOrder(fOrderEle)) {
								throw new Exception("Fulfillment Order Already Exists in the System!");
							}	
						}
					}
					
				} else {
					if(!headerProcessCode.equalsIgnoreCase("A")) {
						throw new Exception("Neither Customer Order Or Fulfillment Order Exists!");
					} else {
						this.getOrderExtendedPriceInfo(rootEle, null);
					}
				}
			}
			
			String fStatusCode = "1100.0100";
			if(fOrderEle != null) {
				fStatusCode = fOrderEle.getAttribute("MaxOrderStatus");
				if(YFCObject.isNull(fStatusCode) || YFCObject.isVoid(fStatusCode)) {
					throw new Exception("Attribute MaxOrderStatus Cannot be NULL or Void!");
				}
			}
			
			String hdrStatusCode = fStatusCode;
			if(rootEle.hasAttribute("OrderStatus")) {
				hdrStatusCode = rootEle.getAttribute("OrderStatus");
				if(YFCObject.isNull(hdrStatusCode) || YFCObject.isVoid(hdrStatusCode)) {
					hdrStatusCode = fStatusCode;
				} else {
					if(hdrStatusCode.equalsIgnoreCase("9000")) {
						hdrStatusCode = fStatusCode;
					}
				}
			} 
			
			log.verbose("OrderStatus:"+hdrStatusCode);
			
			// If Header process code from input xml is A
			if(headerProcessCode.equalsIgnoreCase("A")) {
				this.setReqUOMPrice(env, rootEle);
				
				// To set the order header attributes of the customer order to the legacy input xml. 
				this.setOrderHeaderAttributes(env, rootEle, cOrderEle);
				
				Document cOrderInXML = YFCDocument.createDocument().getDocument();
				cOrderInXML.appendChild(cOrderInXML.importNode(rootEle.getDOMNode(), true));
				cOrderInXML.renameNode(cOrderInXML.getDocumentElement(), cOrderInXML.getNamespaceURI(), "Order");
				YFCElement cOrderInXMLEle = YFCDocument.getDocumentFor(cOrderInXML).getDocumentElement();
				
				// Set the Extended Price Info
				this.setExtendedPriceInfo(env,cOrderInXMLEle, true);
				
				YFCElement custOrderEle = null;
				if(cOrderEle != null) {
					// Updates the customer order.
					custOrderEle = updateCustomerOrder(env, cOrderInXMLEle, cOrderEle).getDocumentElement();
				} else {
					// To Create a new customer order.
					custOrderEle = createCustomerOrder(env, cOrderInXMLEle).getDocumentElement();
				}
				
				// Set the Extended Price Info
				this.setExtendedPriceInfo(env,rootEle, false);
				
				// To create fulfillment order.
				returnToLegacyDoc = createFulfillmentOrder(env, rootEle, custOrderEle, inXMLEle);
				this.performOrderStatusChange(env, cAndfOrderEle, custOrderEle, returnToLegacyDoc.getDocumentElement(), hdrStatusCode, "A");
				isAPISuccess = true;
				
			} else if(headerProcessCode.equalsIgnoreCase("C")) {
				this.setReqUOMPrice(env, rootEle);
				
				// Build input document to call ChangeOrder API.
				Document chngOrdDoc = YFCDocument.createDocument().getDocument();
				chngOrdDoc.appendChild(chngOrdDoc.importNode(rootEle.getDOMNode(), true));
				chngOrdDoc.renameNode(chngOrdDoc.getDocumentElement(), chngOrdDoc.getNamespaceURI(), "Order");
				YFCElement chngcOrderEle0 = YFCDocument.getDocumentFor(chngOrdDoc).getDocumentElement();
				// To remove order lines element from the document.
				YFCElement remEle = chngcOrderEle0.getChildElement("OrderLines");
				if(remEle != null) {
					chngcOrderEle0.removeChild(remEle);
				} 
				
				YFCElement chngcOOrdLinesEle0 = chngcOrderEle0.getOwnerDocument().createElement("OrderLines");
				chngcOrderEle0.appendChild(chngcOOrdLinesEle0);
				
				// To set the transaction Id.
				String tranId0 = null;
				if(this._prop != null) {
					tranId0 = this._prop.getProperty("XPX_CHN_CNL_STATUS_TXN");
					if(YFCObject.isNull(tranId0) || YFCObject.isVoid(tranId0)) {
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
				if(this._prop != null) {
					tranId = this._prop.getProperty("XPX_CHN_CRT_STATUS_TXN");
					if(YFCObject.isNull(tranId) || YFCObject.isVoid(tranId)) {
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
				
				if(log.isDebugEnabled()){
				log.debug("fOrderEle_OrderType:"+fOrderEle.getAttribute("OrderType"));
				log.debug("cOrderEle_OrderType:"+cOrderEle.getAttribute("OrderType"));
				}
				
				log.verbose("");
				log.verbose("fOrderEle_OrderType:"+fOrderEle.getAttribute("OrderType"));
				log.verbose("cOrderEle_OrderType:"+cOrderEle.getAttribute("OrderType"));
				log.verbose("");
				
				if(!isOrderPlaceFlag.equalsIgnoreCase("Y")) {
					processFOHold(rootEle, fOrderEle);										
				}
				
				preparefOChange(env, chngcOrderEle0, chngcOrdStatusEle0, chngcOrdStatusEle, rootEle, fOrderEle, cOrderEle);
				
				// Updates customer and fulfillment order.
				returnToLegacyDoc = updatefOrder(env, chngcOrderEle0, chngcOrdStatusEle0, chngcOrdStatusEle, rootEle, cOrderEle, fOrderEle);
				this.performOrderStatusChange(env, cAndfOrderEle, cOrderEle, fOrderEle, hdrStatusCode, "C");
				isAPISuccess = true;
				
			} else if(headerProcessCode.equalsIgnoreCase("D")) {
				
				Document chngcOrderInXML0 = YFCDocument.createDocument().getDocument();
				chngcOrderInXML0.appendChild(chngcOrderInXML0.importNode(rootEle.getDOMNode(), true));
				chngcOrderInXML0.renameNode(chngcOrderInXML0.getDocumentElement(), chngcOrderInXML0.getNamespaceURI(), "Order");
				YFCElement chngcOrderEle0 = YFCDocument.getDocumentFor(chngcOrderInXML0).getDocumentElement();
				
				this.handleHeaderProcessCodeD(rootEle, fOrderEle);
				if(rootEle.hasAttribute("OrderHeaderKey")) {
					setInstructionKeys(rootEle, fOrderEle);
					setExtendedPriceInfo(env, rootEle, false);
					filterAttributes(rootEle, false);
					
					if(log.isDebugEnabled()){
					log.debug("XPXChangeOrder_FO[HeaderProcessCode:D]-InXML:"+rootEle.getString());
					}
					log.verbose("");
					log.verbose("XPXChangeOrder_FO[HeaderProcessCode:D]-InXML:"+rootEle.getString());
					
					Document tempDoc = this.api.executeFlow(env, "XPXChangeOrder", rootEle.getOwnerDocument().getDocument());
					if(tempDoc != null) {
						returnToLegacyDoc = YFCDocument.getDocumentFor(tempDoc);
					} else {
						throw new Exception("Service XPXChangeOrder Failed!");
					}
				}
				
				this.handleHeaderProcessCodeD(chngcOrderEle0, fOrderEle, cOrderEle);
				if(chngcOrderEle0.hasAttribute("OrderHeaderKey")) {
					setExtendedPriceInfo(env, chngcOrderEle0, true);
					filterAttributes(chngcOrderEle0, true);
					
					if(log.isDebugEnabled()){
					log.debug("XPXChangeOrder_CO[HeaderProcessCode:D]-InXML:"+chngcOrderEle0.getString());
					}
					
					log.verbose("");
					log.verbose("XPXChangeOrder_CO[HeaderProcessCode:D]-InXML:"+chngcOrderEle0.getString());
					
					Document tempDoc = this.api.executeFlow(env, "XPXChangeOrder", chngcOrderEle0.getOwnerDocument().getDocument());
					if(tempDoc != null) {
						returnToLegacyDoc = YFCDocument.getDocumentFor(tempDoc);
					} else {
						throw new Exception("Service XPXChangeOrder Failed!");
					}
				}
				if(returnToLegacyDoc == null) {
					throw new Exception("Could Not Perform Fulfillment Order Cancel!");
				}
				isAPISuccess = true;
				
			} else if(headerProcessCode.equalsIgnoreCase("S")) {
				
				this.performOrderStatusChange(env, cAndfOrderEle, cOrderEle, fOrderEle, hdrStatusCode, "S");
				
				// Input xml has been passed as the output after updating the status.				
				returnToLegacyDoc = YFCDocument.getDocumentFor(inXML);
				
			} else {
				throw new Exception("Invalid HeaderProcessCode in the Incoming Legacy Message!"); 
			}			
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			APIException = ex;
			
            //Added by Prasanth Kumar M. to prevent rollback of the orders if this code is invoked in OPResponse flow			
			if(!"Y".equalsIgnoreCase(isOrderPlaceFlag)) {
			   ((YFSContext)env).rollback();
			}
			prepareErrorObject(ex, "LegacyOrderUpdate", ex.getMessage(), env, inXML);
		} 
		finally {
			if(isAPISuccess) {
				YFCElement returnToLegacyEle = returnToLegacyDoc.getDocumentElement();
				returnToLegacyEle.setAttribute("TransactionMessage", "");
				// Transaction status 'P' refers to Pass.
				returnToLegacyEle.setAttribute("TransactionStatus", "P");
			} else {
				returnToLegacyDoc = YFCDocument.getDocumentFor(inXML);
				YFCElement returnToLegacyEle = returnToLegacyDoc.getDocumentElement();
				if(!YFCObject.isNull(APIException)) {
					returnToLegacyEle.setAttribute("TransactionMessage", APIException.toString());
				} else {
					returnToLegacyEle.setAttribute("TransactionMessage", "");	
				}
				returnToLegacyEle.setAttribute("TransactionStatus", "F");
			}
		}
		
		if(log.isDebugEnabled()){
		log.debug("ReturnToLegacyDoc:"+returnToLegacyDoc.toString());
		}
		log.verbose("");
		log.verbose("ReturnToLegacyDoc:"+returnToLegacyDoc.toString());
		
		return returnToLegacyDoc.getDocument();
	}
	
	/**
	 * This method prepares the error object with the exception details which in turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML){
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();	
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}
	
	private void setOrderHeaderAttributes(YFSEnvironment env, YFCElement rootEle, YFCElement cOrderEle) throws Exception {
		
		String _envId = null;
		String _compId = null;
		String _custNo = null;				
		
		// To retreive header attributes from a customer order.
		if(cOrderEle != null) {
			YFCElement custExtnEle = cOrderEle.getChildElement("Extn");
			if(custExtnEle != null) {
				if(custExtnEle.hasAttribute("ExtnCustomerNo")) {
					_custNo = custExtnEle.getAttribute("ExtnCustomerNo");
					if(YFCObject.isNull(_custNo) || YFCObject.isVoid(_custNo)) {
						throw new Exception("Attribute OrderLine/Extn/@ExtnCustomerNo Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute ExtnCustomerNo Not Available In OrderList Template!");
				}
				
				if(custExtnEle.hasAttribute("ExtnEnvtId")) {
					_envId = custExtnEle.getAttribute("ExtnEnvtId");
					if(YFCObject.isNull(_envId) || YFCObject.isVoid(_envId)) {
						throw new Exception("Attribute OrderLine/Extn/@ExtnEnvtId Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute ExtnEnvId Not Available In OrderList Template!!");
				}
				
				if(custExtnEle.hasAttribute("ExtnCompanyId")) {
					_compId = custExtnEle.getAttribute("ExtnCompanyId");
					if(YFCObject.isNull(_compId) || YFCObject.isVoid(_compId)) {
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
		log.debug("CO_ExtnCustomerNo:"+_custNo);
		log.debug("CO_ExtnEnvtId:"+_envId);
		log.debug("CO_ExtnCompanyId:"+_compId);
		}
		
		log.verbose("");
		log.verbose("CO_ExtnCustomerNo:"+_custNo);
		log.verbose("CO_ExtnEnvtId:"+_envId);
		log.verbose("CO_ExtnCompanyId:"+_compId);
		log.verbose("");
		
		
		String envId = null;
		String compId = null;
		String custNo = null;
		
		// To retrieve header attributes from legacy xml.
		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		if(rootExtnEle != null) {
			if(rootExtnEle.hasAttribute("ExtnCustomerNo")) {
				custNo = rootExtnEle.getAttribute("ExtnCustomerNo");
				if(YFCObject.isNull(custNo) || YFCObject.isVoid(custNo)) {
					if(YFCObject.isNull(_custNo) || YFCObject.isVoid(_custNo)) {
						throw new Exception("Attribute OrderLine/Extn/@ExtnCustomerNo Cannot be NULL or Void!");
					}
					custNo = _custNo;
				}
				
			} else {
				if(YFCObject.isNull(_custNo) || YFCObject.isVoid(_custNo)) {
					throw new Exception("Attribute OrderLine/Extn/@ExtnCustomerNo Cannot be NULL or Void!");
				}
				custNo = _custNo;
			}
			
			if(rootExtnEle.hasAttribute("ExtnEnvtId")) {
				envId = rootExtnEle.getAttribute("ExtnEnvtId");
				if(YFCObject.isNull(envId) || YFCObject.isVoid(envId)) {
					if(YFCObject.isNull(_envId) || YFCObject.isVoid(_envId)) {
						throw new Exception("Attribute OrderLine/Extn/@ExtnEnvtId Cannot be NULL or Void!");
					}
					envId = _envId;
				}
				
			} else {
				if(YFCObject.isNull(_envId) || YFCObject.isVoid(_envId)) {
					throw new Exception("Attribute OrderLine/Extn/@ExtnEnvtId Cannot be NULL or Void!");
				}
				envId = _envId;
			}
			
			if(rootExtnEle.hasAttribute("ExtnCompanyId")) {
				compId = rootExtnEle.getAttribute("ExtnCompanyId");
				if(YFCObject.isNull(compId) || YFCObject.isVoid(compId)) {
					if(YFCObject.isNull(_compId) || YFCObject.isVoid(_compId)) {
						throw new Exception("Attribute OrderLine/Extn/@ExtnCompanyId Cannot be NULL or Void!");
					}
					compId = _compId;
				}
				
			} else {
				if(YFCObject.isNull(_compId) || YFCObject.isVoid(_compId)) {
					throw new Exception("Attribute OrderLine/Extn/@ExtnCompanyId Cannot be NULL or Void!");
				}
				compId = _compId;
			}
			
			YFCElement custInfoEle = null;
			if(rootExtnEle.hasAttribute("ExtnBillToName")) {
				String extnBillToName = rootExtnEle.getAttribute("ExtnBillToName");
				if(YFCObject.isNull(extnBillToName) || YFCObject.isVoid(extnBillToName)) {
					// Suffix type "MC" stands for Bill To. 
					custInfoEle = getCustomerInformation(env, rootEle, "MC");
					if(custInfoEle == null) {
						throw new Exception("No Customer Information Found in Sterling!");
					}
					rootExtnEle.setAttribute("ExtnBillToName", getBillToName(custInfoEle));
				}
			} else {
				custInfoEle = getCustomerInformation(env, rootEle, "MC");
				if(custInfoEle == null) {
					throw new Exception("No Customer Information Found in Sterling!");
				}
				rootExtnEle.setAttribute("ExtnBillToName", getBillToName(custInfoEle));
			}
			
			if(rootEle.hasAttribute("BillToID")) {
				String billToID = rootEle.getAttribute("BillToID");
				if(YFCObject.isNull(billToID) || YFCObject.isVoid(billToID)) {
					if(custInfoEle == null) {
						custInfoEle = getCustomerInformation(env, rootEle, "MC");
					}
					if(custInfoEle == null) {
						throw new Exception("No Customer Information Found in Sterling!");
					}
					rootEle.setAttribute("BillToID", getBillToID(custInfoEle));
				}
			} else {
				if(custInfoEle == null) {
					custInfoEle = getCustomerInformation(env, rootEle, "MC");
				}
				if(custInfoEle == null) {
					throw new Exception("No Customer Information Found in Sterling!");
				}
				rootEle.setAttribute("BillToID", getBillToID(custInfoEle));
			}
			
			// Customer information element has been made null to fetch the Ship To customer details.
			custInfoEle = null;
			if(rootExtnEle.hasAttribute("ExtnSAPParentName")) {
				String sapParentName = rootExtnEle.getAttribute("ExtnSAPParentName");
				if(YFCObject.isNull(sapParentName) || YFCObject.isVoid(sapParentName)) {
					// Suffix type "S" stands for Ship To.
					custInfoEle = getCustomerInformation(env, rootEle, "S");
					if(custInfoEle == null) {
						throw new Exception("No Customer Information Found in Sterling!");
					}
					rootExtnEle.setAttribute("ExtnSAPParentName", getSAPParentName(custInfoEle));
				}
			} else {
				custInfoEle = getCustomerInformation(env, rootEle, "S");
				if(custInfoEle == null) {
					throw new Exception("No Customer Information Found in Sterling!");
				}
				rootExtnEle.setAttribute("ExtnSAPParentName", getSAPParentName(custInfoEle));
			}
			
			if(rootExtnEle.hasAttribute("ExtnOrigEnvironmentCode")) {
				String orgEnvCode = rootExtnEle.getAttribute("ExtnOrigEnvironmentCode");
				if(YFCObject.isNull(orgEnvCode) || YFCObject.isVoid(orgEnvCode)) {
					if(custInfoEle == null) {
						custInfoEle = getCustomerInformation(env, rootEle, "S");
					}
					if(custInfoEle == null) {
						throw new Exception("No Customer Information Found in Sterling!");
					}
					rootExtnEle.setAttribute("ExtnOrigEnvironmentCode", getOrigEnvCode(custInfoEle));
				}
			} else {
				if(custInfoEle == null) {
					custInfoEle = getCustomerInformation(env, rootEle, "S");
				}
				if(custInfoEle == null) {
					throw new Exception("No Customer Information Found in Sterling!");
				}
				rootExtnEle.setAttribute("ExtnOrigEnvironmentCode", getOrigEnvCode(custInfoEle));
			}
			
			if(rootEle.hasAttribute("BuyerOrganizationCode")) {
				String buyerOrgCode = rootEle.getAttribute("BuyerOrganizationCode");
				if(YFCObject.isNull(buyerOrgCode) || YFCObject.isVoid(buyerOrgCode)) {
					if(custInfoEle != null) {
						custInfoEle = getCustomerInformation(env, rootEle, "S");
					}
					if(custInfoEle == null) {
						throw new Exception("No Customer Information Found in Sterling!");
					}
					rootEle.setAttribute("BuyerOrganizationCode", this.getBuyerOrgCode(custInfoEle));
				}
			} else {
				if(custInfoEle == null) {
					custInfoEle = getCustomerInformation(env, rootEle, "S");
				}
				if(custInfoEle == null) {
					throw new Exception("No Customer Information Found in Sterling!");
				}
				rootEle.setAttribute("BuyerOrganizationCode", this.getBuyerOrgCode(custInfoEle));
			}
			
			if(rootEle.hasAttribute("ShipToID")) {
				String shipToID = rootEle.getAttribute("ShipToID");
				if(YFCObject.isNull(shipToID) || YFCObject.isVoid(shipToID)) {
					if(custInfoEle == null) {
						custInfoEle = getCustomerInformation(env, rootEle, "S");
					}
					if(custInfoEle == null) {
						throw new Exception("No Customer Information Found in Sterling!");
					}
					rootEle.setAttribute("ShipToID", this.getShipToID(custInfoEle));
				}
			} else {
				if(custInfoEle == null) {
					custInfoEle = getCustomerInformation(env, rootEle, "S");
				}
				if(custInfoEle == null) {
					throw new Exception("No Customer Information Found in Sterling!");
				}
				rootEle.setAttribute("ShipToID", this.getShipToID(custInfoEle));
			}			
			
			if(!rootEle.hasAttribute("EntryType")) {
				rootEle.setAttribute("EntryType", "Web");
			}

			if(!rootEle.hasAttribute("CarrierServiceCode")) {
				rootEle.setAttribute("CarrierServiceCode","Standard Shipping");
			}	
		} else {
			throw new Exception("OrderLine/Extn Element Not Available in Incoming Legacy Message!");
		}
		
		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if(rootOrdLinesEle != null) {
			YFCIterable yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				String isNewLine = null;
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				if(rootOrdLineEle.hasAttribute("IsNewLine")) {
					isNewLine = rootOrdLineEle.getAttribute("IsNewLine");
					if(YFCObject.isNull(isNewLine) || YFCObject.isVoid(isNewLine)) {
						isNewLine = "Y";
					}
				} else {
					isNewLine = "Y";
				}
				if(isNewLine.equalsIgnoreCase("Y")) {
					setOrderLineAttributes(env, rootOrdLineEle, compId, envId, custNo);
				}
			}
		} else {
			throw new Exception("Element OrderLines Not Available in Incoming Legacy Message!");
		}
	}
	
	private void setOrderLineAttributes(YFSEnvironment env, YFCElement rootOrdLineEle, String compId, String envId, String custNo) throws Exception {
		
		String lineType = null;
		String legacyItemNo = null;
		String shipNode = null;
		String itemId = null;
		
		if(rootOrdLineEle.hasAttribute("LineType")) {
			lineType = rootOrdLineEle.getAttribute("LineType");
			if(YFCObject.isNull(lineType) || YFCObject.isVoid(lineType)) {
				throw new Exception("Attribute OrderLine/@LineType Cannot be NULL or Void!");
			}
			if(lineType.equalsIgnoreCase("C") || lineType.equalsIgnoreCase("M")) {
				rootOrdLineEle.setAttribute("ValidateItem", "N");
			} else {
				rootOrdLineEle.setAttribute("ValidateItem", "N");
			}
			if(lineType.equalsIgnoreCase("S")) {
				String custPartNo = null;
				YFCElement rootOrdLineItemEle = rootOrdLineEle.getChildElement("Item");
				if(rootOrdLineItemEle != null) {
					if(rootOrdLineItemEle.hasAttribute("CustomerItem")) {
						custPartNo = rootOrdLineItemEle.getAttribute("CustomerItem");
						if(!YFCObject.isNull(custPartNo) && !YFCObject.isVoid(custPartNo)) {
							legacyItemNo = getLegacyItemNoFromXRef(env, compId, envId, custNo, custPartNo);
							if(!YFCObject.isNull(legacyItemNo) && !YFCObject.isVoid(legacyItemNo)) {
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
		
		if(rootOrdLineEle.hasAttribute("ShipNode")) {
			shipNode = rootOrdLineEle.getAttribute("ShipNode");
			if(YFCObject.isNull(shipNode) || YFCObject.isVoid(shipNode)) {
				throw new Exception("Attribute OrderLine/@ShipNode Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute OrderLine/@ShipNode Not Available in Incoming Legacy Message!");
		}
		
		YFCElement rootOrdLineItemEle = rootOrdLineEle.getChildElement("Item");
		if(rootOrdLineItemEle != null) {
			if(rootOrdLineItemEle.hasAttribute("ItemID")) {
				itemId = rootOrdLineItemEle.getAttribute("ItemID");
				if(YFCObject.isNull(itemId) || YFCObject.isVoid(itemId)) {
					throw new Exception("Attribute ItemID Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ItemID Not Available in Incoming Legacy Message!");
			}
		} else {
			throw new Exception("Element OrderLine/Item Not Available in Incoming Legacy Message!");
		}
		
		YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
		if(rootOrdLineExtnEle != null) {
			String extnLineType = getInventoryIndicator(env, itemId, shipNode, envId, lineType);
			if(!YFCObject.isNull(extnLineType) && !YFCObject.isVoid(extnLineType)) {
				rootOrdLineExtnEle.setAttribute("ExtnLineType", extnLineType);
			} else {
				throw new Exception("ExtnLineType Cannot be NULL or Void!");
			}
			
		} else {
			throw new Exception("OrderLine/Extn Element Not Available in Incoming Legacy Message!");
		}		
	}
	
	private void handleHeaderProcessCodeD(YFCElement rootEle, YFCElement fOrderEle) throws Exception {
		
		String isOrdPlace = "N";
		if(rootEle.hasAttribute("IsOrderPlace")) {
			isOrdPlace = rootEle.getAttribute("IsOrderPlace");
			if(YFCObject.isNull(isOrdPlace) || YFCObject.isVoid(isOrdPlace)) {
				isOrdPlace = "N";
			}
		}
		
		if(fOrderEle.hasAttribute("OrderHeaderKey")) {
			String ordHeaderKey = fOrderEle.getAttribute("OrderHeaderKey");
			if(YFCObject.isNull(ordHeaderKey) || YFCObject.isVoid(ordHeaderKey)) {
				throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
			}
			rootEle.setAttribute("OrderHeaderKey", ordHeaderKey);
			rootEle.setAttribute("Override", "Y");
			
			if(isOrdPlace.equalsIgnoreCase("Y")) {
				YFCElement ordHoldTypesEle = rootEle.getOwnerDocument().createElement("OrderHoldTypes");
				rootEle.appendChild(ordHoldTypesEle);
				YFCElement ordHoldTypeEle = ordHoldTypesEle.getOwnerDocument().createElement("OrderHoldType");
				ordHoldTypesEle.appendChild(ordHoldTypeEle);
				ordHoldTypeEle.setAttribute("HoldType","LEGACY_CNCL_ORD_HOLD");
				ordHoldTypeEle.setAttribute("ReasonText", "Legacy Request For Order Cancel");
				ordHoldTypeEle.setAttribute("Status", "1100");
			}
		} else {
			throw new Exception("Attribute OrderHeaderKey Not Available in OrderList Template!");
		}
		
		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if(rootOrdLinesEle != null) {
			YFCIterable yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				String webLineNo = null;
				String legacyLineNo = null;
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
				if(rootOrdLineExtnEle != null) {
					if(rootOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
						webLineNo = rootOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
					}
					if(rootOrdLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
						legacyLineNo = rootOrdLineExtnEle.getAttribute("ExtnLegacyLineNumber");
					}
				}
				
				if(log.isDebugEnabled()){
				log.debug("ExtnWebLineNumber:"+webLineNo);
				log.debug("ExtnLegacyLineNumber:"+legacyLineNo);
				}
				
				if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo)) {
					String fOrdLineKey = null;
					if(!YFCObject.isNull(legacyLineNo) && !YFCObject.isVoid(legacyLineNo)) {
						fOrdLineKey = this.getOrderLineKeyForWebLineNo(webLineNo, legacyLineNo, fOrderEle);
					} else {
						fOrdLineKey = this.getOrderLineKeyForWebLineNo(webLineNo, null, fOrderEle);
					}
					rootOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);
					rootOrdLineEle.removeAttribute("OrderedQty");
					YFCElement rootOrdLineTranQtyEle = rootOrdLineEle.getChildElement("OrderLineTranQuantity");
					if(rootOrdLineTranQtyEle != null) {
						rootOrdLineEle.removeChild(rootOrdLineTranQtyEle);
					}
				} else {
					rootOrdLinesEle.removeChild(rootOrdLineEle);
				}
			}
			yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			if(!yfcItr.hasNext()) {
				rootEle.removeAttribute("OrderHeaderKey");
			}
		} else {
			throw new Exception("OrderLines Element Not Available in Incoming Legacy Message!");
		}
	}
	
	private void handleHeaderProcessCodeD(YFCElement chngcOrderEle, YFCElement fOrderEle, YFCElement cOrderEle) throws Exception {
		
		String isOrdPlace = "N";
		if(chngcOrderEle.hasAttribute("IsOrderPlace")) {
			isOrdPlace = chngcOrderEle.getAttribute("IsOrderPlace");
			if(YFCObject.isNull(isOrdPlace) || YFCObject.isVoid(isOrdPlace)) {
				isOrdPlace = "N";
			}
		}
		
		if(cOrderEle.hasAttribute("OrderHeaderKey")) {
			String ordHeaderKey = cOrderEle.getAttribute("OrderHeaderKey");
			if(YFCObject.isNull(ordHeaderKey) || YFCObject.isVoid(ordHeaderKey)) {
				throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
			}
			chngcOrderEle.setAttribute("OrderHeaderKey", ordHeaderKey);
			chngcOrderEle.setAttribute("Override", "Y");
			chngcOrderEle.removeAttribute("OrderType");
		} else {
			throw new Exception("Attribute OrderHeaderKey Not Available in OrderList Template!");
		}
		
		YFCElement chngcOrderLinesEle = chngcOrderEle.getChildElement("OrderLines");
		if(chngcOrderLinesEle != null) {
			YFCIterable yfcItr = chngcOrderLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement chngcOrderLineEle = (YFCElement) yfcItr.next();
				YFCElement chngcOrderLineExtnEle = chngcOrderLineEle.getChildElement("Extn");
				if(chngcOrderLineExtnEle != null) {
					String cOrdLineKey = null;
					String webLineNo = null;
					if(chngcOrderLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
						webLineNo = chngcOrderLineExtnEle.getAttribute("ExtnWebLineNumber");
					}
					if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo)) {
						cOrdLineKey = this.getOrderLineKeyForWebLineNo(webLineNo, null, cOrderEle);
						if(!YFCObject.isNull(cOrdLineKey) && !YFCObject.isVoid(cOrdLineKey)) {
							chngcOrderLineEle.setAttribute("OrderLineKey", cOrdLineKey);
							chngcOrderLineExtnEle.removeAttribute("ExtnLegacyLineNumber");
							if(isOrdPlace.equalsIgnoreCase("Y")) {
								chngcOrderLineEle.removeAttribute("OrderedQty");
								YFCElement chngcOrderLineTranQtyEle = chngcOrderLineEle.getChildElement("OrderLineTranQuantity");
								chngcOrderLineEle.removeChild(chngcOrderLineTranQtyEle);
							} else {
								YFCElement chngcOrderLineTranQtyEle = chngcOrderLineEle.getChildElement("OrderLineTranQuantity");
								chngcOrderLineTranQtyEle.setAttribute("OrderedQty", "0.0");
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
	
	private YFCDocument updatefOrder(YFSEnvironment env, YFCElement chngcOrderEle, YFCElement chngcOrdStatusEle0, YFCElement chngcOrdStatusEle, YFCElement chngfOrderEle, YFCElement cOrderEle, YFCElement fOrderEle) throws Exception {
		
		YFCDocument retDoc = null;
		Document tempDoc = null;
		
		if(chngcOrdStatusEle0.hasAttribute("OrderHeaderKey")) {
			if(log.isDebugEnabled()){
			log.debug("XPXChangeOrderStatus_CO-InXML:"+chngcOrdStatusEle0.getString());
			}
			log.verbose("");
			log.verbose("XPXChangeOrderStatus_CO-InXML:"+chngcOrdStatusEle0.getString());
			
			tempDoc = this.api.executeFlow(env, "XPXChangeOrderStatus", chngcOrdStatusEle0.getOwnerDocument().getDocument());
			if(tempDoc == null) {
				throw new Exception("Service XPXChangeOrderStatus Failed!");
			}			
		}
		
		if(chngcOrderEle.hasAttribute("OrderHeaderKey")) {
			setExtendedPriceInfo(env,chngcOrderEle, true);
			filterAttributes(chngcOrderEle, true);
			
			if(log.isDebugEnabled()){
			log.debug("XPXChangeOrder_CO-InXML:"+chngcOrderEle.getString());
			}
			log.verbose("");
			log.verbose("XPXChangeOrder_CO-InXML:"+chngcOrderEle.getString());
			
			tempDoc = this.api.executeFlow(env, "XPXChangeOrder", chngcOrderEle.getOwnerDocument().getDocument());
			if(tempDoc != null) {
				cOrderEle = YFCDocument.getDocumentFor(tempDoc).getDocumentElement();
			} else {
				throw new Exception("Service XPXChangeOrder Failed!");
			}	
		}

		boolean isChngOrdReq = chaincAndfOrder(chngfOrderEle, cOrderEle);
		
		if(isChngOrdReq) {
			setInstructionKeys(chngfOrderEle, fOrderEle);
			setExtendedPriceInfo(env,chngfOrderEle, false);
			filterAttributes(chngfOrderEle, false);
			
			if(log.isDebugEnabled()){
			log.debug("XPXChangeOrder_FO-InXML:"+chngfOrderEle.getString());
			}
			log.verbose("");
			log.verbose("XPXChangeOrder_FO-InXML:"+chngfOrderEle.getString());
			
			tempDoc = this.api.executeFlow(env, "XPXChangeOrder", chngfOrderEle.getOwnerDocument().getDocument());
			if(tempDoc != null) {
				fOrderEle = YFCDocument.getDocumentFor(tempDoc).getDocumentElement();
			} else {
				throw new Exception("Service XPXChangeOrder Failed!");
			}
			retDoc = fOrderEle.getOwnerDocument();		
		} else {
			retDoc = chngfOrderEle.getOwnerDocument();
		}
		
		if(chngcOrdStatusEle.hasAttribute("OrderHeaderKey")) {

			if(log.isDebugEnabled()){
			log.debug("XPXChangeOrderStatus_CO-InXML:"+chngcOrdStatusEle.getString());
			}
			log.verbose("");
			log.verbose("XPXChangeOrderStatus_CO-InXML:"+chngcOrdStatusEle.getString());
			
			tempDoc = this.api.executeFlow(env, "XPXChangeOrderStatus", chngcOrdStatusEle.getOwnerDocument().getDocument());
			if(tempDoc == null) {
				throw new Exception("Service XPXChangeOrderStatus Failed!");
			}	
		}
		
		return retDoc;
	}
	
	private void setExtendedPriceInfo(YFSEnvironment env, YFCElement ordEle, boolean isCustOrder) throws YFSException, RemoteException {
		
		String legTotOrdAdj = null;
		
		if(ordEle.hasAttribute("LegTotOrderAdjustments")) {
			legTotOrdAdj = ordEle.getAttribute("LegTotOrderAdjustments");
			if(YFCObject.isNull(legTotOrdAdj) || YFCObject.isVoid(legTotOrdAdj)) {
				legTotOrdAdj = "0.0";
			}
		} else {
			legTotOrdAdj = "0.0";
		}
		
		String ordSubTotal = null;
		if(ordEle.hasAttribute("OrderSubTotal")) {
			ordSubTotal = ordEle.getAttribute("OrderSubTotal");
			if(YFCObject.isNull(ordSubTotal) || YFCObject.isVoid(ordSubTotal)) {
				ordSubTotal = "0.0";
			}
		} else {
			ordSubTotal = "0.0";
		}
		
		String totOrdValWithoutTax = null;
		if(ordEle.hasAttribute("TotOrdValWithoutTaxes")) {
			totOrdValWithoutTax = ordEle.getAttribute("TotOrdValWithoutTaxes");
			if(YFCObject.isNull(totOrdValWithoutTax) || YFCObject.isVoid(totOrdValWithoutTax)) {
				totOrdValWithoutTax = "0.0";
			}
		} else {
			totOrdValWithoutTax = "0.0";
		}
		
		YFCElement ordExtnEle = ordEle.getChildElement("Extn");
		if(ordExtnEle != null) {
			if(isCustOrder) {
				ordExtnEle.setAttribute("ExtnLegTotOrderAdjustments", legTotOrdAdj);
				ordExtnEle.setAttribute("ExtnOrderSubTotal", ordSubTotal);
				ordExtnEle.setAttribute("ExtnTotOrdValWithoutTaxes", totOrdValWithoutTax);
			} else {
				String extnTotOrdVal = null;
				if(ordExtnEle.hasAttribute("ExtnTotalOrderValue")) {
					extnTotOrdVal = ordExtnEle.getAttribute("ExtnTotalOrderValue");
					if(YFCObject.isNull(extnTotOrdVal) || YFCObject.isVoid(extnTotOrdVal)) {
						extnTotOrdVal = "0.0";
					}
				} else {
					extnTotOrdVal = "0.0";
				}
				
				String extnOrdTax = null;
				if(ordExtnEle.hasAttribute("ExtnOrderTax")) {
					extnOrdTax = ordExtnEle.getAttribute("ExtnOrderTax");
					if(YFCObject.isNull(extnOrdTax) || YFCObject.isVoid(extnOrdTax)) {
						extnOrdTax = "0.0";
					}
				} else {
					extnOrdTax = "0.0";
				}
				
				String extnTotOrdFreight = null;
				if(ordExtnEle.hasAttribute("ExtnTotalOrderFreight")) {
					extnTotOrdFreight = ordExtnEle.getAttribute("ExtnTotalOrderFreight");
					if(YFCObject.isNull(extnTotOrdFreight) || YFCObject.isVoid(extnTotOrdFreight)) {
						extnTotOrdFreight = "0.0";
					}
				} else {
					extnTotOrdFreight = "0.0";
				}
				double dExtnTotOrdValWithoutTax = Double.parseDouble(extnTotOrdVal) - (Double.parseDouble(extnOrdTax) + Double.parseDouble(extnTotOrdFreight));
				ordExtnEle.setAttribute("ExtnTotOrdValWithoutTaxes", new Double(dExtnTotOrdValWithoutTax).toString());
			}
		}
		
		double extnOrdSubTotal = 0.0;
		YFCElement ordLineExtnEle = null;
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement)yfcItr.next();
				String extPrice = null;
				if(ordLineEle.hasAttribute("ExtendedPrice")) {
					extPrice = ordLineEle.getAttribute("ExtendedPrice");
					if(YFCObject.isNull(extPrice) || YFCObject.isNull(extPrice)) {
						extPrice = "0.0";
					}
				} else {
					extPrice = "0.0";
				}
				
				String legOrdLineAdj = null;
				if(ordLineEle.hasAttribute("LegOrderLineAdjustments")) {
					legOrdLineAdj = ordLineEle.getAttribute("LegOrderLineAdjustments");
					if(YFCObject.isNull(legOrdLineAdj) || YFCObject.isNull(legOrdLineAdj)) {
						legOrdLineAdj = "0.0";
					}
				} else {
					legOrdLineAdj = "0.0";
				}
				
				ordLineExtnEle = ordLineEle.getChildElement("Extn");
				if(ordLineExtnEle != null) {
					if(isCustOrder) {
						ordLineExtnEle.setAttribute("ExtnExtendedPrice", extPrice);
						ordLineExtnEle.setAttribute("ExtnLegOrderLineAdjustments", legOrdLineAdj);
					} else {
						String extnLineOrdTotal = null;
						if(ordLineExtnEle.hasAttribute("ExtnLineOrderedTotal")) {
							extnLineOrdTotal = ordLineExtnEle.getAttribute("ExtnLineOrderedTotal");
							if(YFCObject.isNull(extnLineOrdTotal) || YFCObject.isVoid(extnLineOrdTotal)) {
								extnLineOrdTotal = "0.0";
							}
						} else {
							extnLineOrdTotal = "0.0";
						}
						extnOrdSubTotal = extnOrdSubTotal + Double.parseDouble(extnLineOrdTotal);
						
						String extnLegOrdLineAdj = null;
						if(ordLineExtnEle.hasAttribute("ExtnLegOrderLineAdjustments")) {
							extnLegOrdLineAdj = ordLineExtnEle.getAttribute("ExtnLegOrderLineAdjustments");
							if(YFCObject.isNull(extnLegOrdLineAdj) || YFCObject.isVoid(extnLegOrdLineAdj)) {
								extnLegOrdLineAdj = "0.0";
							}
						} else {
							extnLegOrdLineAdj = "0.0";
						}
						ordLineExtnEle.setAttribute("ExtnExtendedPrice", new Double(Double.parseDouble(extnLineOrdTotal) + Double.parseDouble(extnLegOrdLineAdj)).toString());
					}
				}
			}
			if(!isCustOrder) {
				if(ordExtnEle != null) {
					ordExtnEle.setAttribute("ExtnOrderSubTotal", new Double(extnOrdSubTotal).toString());
				}
			} else {
				if(ordExtnEle != null) {
					ordExtnEle.setAttribute("ExtnLegacyOrderNo", "");
					ordExtnEle.setAttribute("ExtnGenerationNo", "");
				}
			}
		}
	}
	private void processFOHold(YFCElement chngOrdEle, YFCElement fOrderEle) {
		
		YFCElement fOrdHoldTypesEle = fOrderEle.getChildElement("OrderHoldTypes");
		if(fOrdHoldTypesEle != null) {
			YFCIterable fOrdHoldTypeList = fOrdHoldTypesEle.getChildren("OrderHoldType");
			while(fOrdHoldTypeList.hasNext()) {
				YFCElement fOrdHoldTypeEle = (YFCElement)fOrdHoldTypeList.next();
				if(fOrdHoldTypeEle != null) {
					if(fOrdHoldTypeEle.hasAttribute("HoldType")) {
						String fHoldType = fOrdHoldTypeEle.getAttribute("HoldType");
						if(!(YFCObject.isNull(fHoldType) || YFCObject.isVoid(fHoldType))) {
							if(fHoldType.equalsIgnoreCase("NEEDS_ATTENTION")) {
								String fHoldStatus = fOrdHoldTypeEle.getAttribute("Status");
								if(!(YFCObject.isNull(fHoldStatus) || YFCObject.isVoid(fHoldStatus))) {
									if(fHoldStatus.equalsIgnoreCase("1100")) {
										YFCElement extnOrdEle = chngOrdEle.getChildElement("Extn");
										if(extnOrdEle != null) {
											if(extnOrdEle.hasAttribute("ExtnWebHoldFlag")) {
												String extnWebHold = extnOrdEle.getAttribute("ExtnWebHoldFlag");
												if(!(YFCObject.isNull(extnWebHold) || YFCObject.isVoid(extnWebHold))) {
													if(extnWebHold.equalsIgnoreCase("N")) {
														YFCElement ordHoldTypesEle = chngOrdEle.getOwnerDocument().createElement("OrderHoldTypes");
														chngOrdEle.appendChild(ordHoldTypesEle);
														YFCElement ordHoldTypeEle = chngOrdEle.getOwnerDocument().createElement("OrderHoldType");
														ordHoldTypesEle.appendChild(ordHoldTypeEle);
														ordHoldTypeEle.setAttribute("HoldType","NEEDS_ATTENTION");
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
		if(fOrderLinesEle != null) {
			YFCIterable yfcItr = fOrderLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement fOrdLineEle = (YFCElement)yfcItr.next();
				if(fOrdLineEle != null) {
					if(fOrdLineEle.hasAttribute("IngoreLineFromFOChange")) {
						String ignoreLineFromFOChange = fOrdLineEle.getAttribute("IngoreLineFromFOChange");
						if(!YFCObject.isNull(ignoreLineFromFOChange) && !YFCObject.isVoid(ignoreLineFromFOChange)) {
							if(ignoreLineFromFOChange.equalsIgnoreCase("Y")) {
								fOrderLinesEle.removeChild(fOrdLineEle);
								continue;
							}
						}
					}
				} else {
					throw new Exception("OrderLine Element Not Available in Incoming Legacy Message!");
				}
			}
		}
		
		fOrderLinesEle = fOrderEle.getChildElement("OrderLines");
		if(fOrderLinesEle != null) {
			YFCIterable yfcItr = fOrderLinesEle.getChildren("OrderLine");
			if(!yfcItr.hasNext()) {
				return false;
			}
			while(yfcItr.hasNext()) {
				YFCElement fOrdLineEle = (YFCElement)yfcItr.next();
				if(fOrdLineEle != null) {
					YFCElement fOrdLineExtnEle = fOrdLineEle.getChildElement("Extn");
					if(fOrdLineExtnEle != null) {
						if(fOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
							String _webLineNo = fOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							String cOrdHeaderKey = this.getOrderHeaderKeyForWebLineNo(_webLineNo, null, cOrderEle);
							String cOrdLineKey = this.getOrderLineKeyForWebLineNo(_webLineNo, null, cOrderEle);
							if(YFCObject.isNull(cOrdHeaderKey) || YFCObject.isVoid(cOrdHeaderKey) || 
												YFCObject.isNull(cOrdLineKey) || YFCObject.isVoid(cOrdLineKey)) {
								throw new Exception("CO OrderHeaderKey or OrderLineKey Cannot be NULL or Void!");
							}
							if(fOrdLineEle.hasAttribute("IsNewLine")) {
								String isNewLine = fOrdLineEle.getAttribute("IsNewLine");
								if(YFCObject.isNull(isNewLine) || YFCObject.isVoid(isNewLine)) {
									throw new Exception("Attribute IsNewLine Cannot be NULL or Void!");
								}
								if(isNewLine.equalsIgnoreCase("Y")) {
									
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
	
	private boolean preparefOChange(YFSEnvironment env, YFCElement chngcOrderEle0, YFCElement chngcOrdStatusEle0, 
			YFCElement chngcOrdStatusEle, YFCElement rootEle, YFCElement fOrderEle, YFCElement cOrderEle) throws Exception {
		
		boolean hasLineA = false;
		boolean hasLineC = false;
		boolean hasLineD = false;
		
		String fOrdHeaderKey = null;
		if(fOrderEle.hasAttribute("OrderHeaderKey")) {
			fOrdHeaderKey = fOrderEle.getAttribute("OrderHeaderKey");
			if(!YFCObject.isNull(fOrdHeaderKey) && !YFCObject.isVoid(fOrdHeaderKey)) {
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
		if(cOrderEle.hasAttribute("OrderHeaderKey")) {
			cOrdHeaderKey = cOrderEle.getAttribute("OrderHeaderKey");
			if(YFCObject.isNull(cOrdHeaderKey) || YFCObject.isVoid(cOrdHeaderKey)) {
				throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute OrderHeaderKey Not Available in OrderList Template!");
		}
		
		YFCElement chngcOrdLinesEle0 = chngcOrderEle0.getChildElement("OrderLines");
		
		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if(rootOrdLinesEle != null) {
			YFCIterable yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				if(rootOrdLineEle != null) {
					if(rootOrdLineEle.hasAttribute("LineProcessCode")) {
						String lineProcessCode = rootOrdLineEle.getAttribute("LineProcessCode");
						if(YFCObject.isNull(lineProcessCode) || YFCObject.isVoid(lineProcessCode)) {
							throw new Exception("Attribute LineProcessCode Cannot be NULL or Void!");
						}
						
						String legacyLineNo = null;
						String webLineNo = null;
						YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
						if(rootOrdLineExtnEle != null) {
							if(rootOrdLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
								legacyLineNo = rootOrdLineExtnEle.getAttribute("ExtnLegacyLineNumber");
								if(!lineProcessCode.equals("D") && (YFCObject.isNull(legacyLineNo) || YFCObject.isVoid(legacyLineNo))) {
									throw new Exception("Attribute ExtnLegacyLineNumber Cannot be NULL or Void!");
								}
							} else {
								throw new Exception("Attribute ExtnLegacyLineNumber Not Available in Incoming Legacy Message!");
							}
							if(rootOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
								webLineNo = rootOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
								if(YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
									throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
								}
							} else {
								throw new Exception("Attribute ExtnWebLineNumber Not Available in Incoming Legacy Message!");
							}
						} else {
							throw new Exception("Extn Element Not Available in Incoming Legacy Message!");
						}
						
						String fOrdLineKey = getOrderLineKeyForWebLineNo(webLineNo, legacyLineNo, fOrderEle);
						String cOrdLineKey = getOrderLineKeyForWebLineNo(webLineNo, null, cOrderEle);
						
						if(log.isDebugEnabled()){
						log.debug("preparefoChange_webLineNo:"+webLineNo);
						log.debug("preparefoChange_legacyLineNo:"+legacyLineNo);
						log.debug("preparefoChange_fOrdLineKey:"+fOrdLineKey);
						log.debug("preparefoChange_cOrdLineKey:"+cOrdLineKey);
						}
						
						log.verbose("");
						log.verbose("preparefoChange_webLineNo:"+webLineNo);
						log.verbose("preparefoChange_legacyLineNo:"+legacyLineNo);
						log.verbose("preparefoChange_fOrdLineKey:"+fOrdLineKey);
						log.verbose("preparefoChange_cOrdLineKey:"+cOrdLineKey);
						log.verbose("");
						
						if(lineProcessCode.equalsIgnoreCase("A")) {
							hasLineA = true;
							List legacyLineNos = this.getLegacyLineNosToArray(fOrderEle);
							if(legacyLineNos.contains(legacyLineNo)) {
								if(!this.IsCancelledLine(webLineNo, legacyLineNo, fOrderEle)) {
									throw new Exception("Cannot Add New Line To Fulfillment Order With Same ExtnLegacyLineNumber!");
								}
							}
							
							if(YFCObject.isNull(fOrdLineKey) || YFCObject.isVoid(fOrdLineKey)) {
								if(YFCObject.isNull(cOrdLineKey) || YFCObject.isVoid(cOrdLineKey)) {
									// New Line Not Exists In Customer Order
									chngcOrderEle0.setAttribute("OrderHeaderKey", cOrdHeaderKey);
									chngcOrderEle0.setAttribute("Override", "Y");
									
									YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
									chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
									chngcOrdLineEle0.setAttribute("Action", "CREATE");
									
									YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
									if(chngcOrdLineExtnEle0 != null) {
										if(chngcOrdLineExtnEle0.hasAttribute("ExtnLegacyLineNumber")) {
											chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
										}
									}
									this.setExtnLineTypeToOrderLine(env, chngcOrdLineEle0, cOrderEle);
									
									rootOrdLineEle.setAttribute("Action","CREATE");
									rootOrdLineEle.setAttribute("IsNewLine", "Y");
									
								} else {
									// New Line Already Exists In Customer Order
									this.handleLineProcessCodeCandA(env,cOrdHeaderKey, webLineNo, legacyLineNo, cOrdLineKey, rootOrdLineEle, fOrderEle, cOrderEle, 
											chngcOrdStatusEle0, chngcOrdStatusEle, chngcOrderEle0);
									
									rootOrdLineEle.setAttribute("Action","CREATE");
									rootOrdLineEle.setAttribute("IsNewLine", "Y");
									
								}
							} else {
								// WebLineNo Already Exists In Fulfillment Order
								this.handleLineProcessCodeCandA(env,cOrdHeaderKey, webLineNo, legacyLineNo, cOrdLineKey, rootOrdLineEle, fOrderEle, cOrderEle, 
										chngcOrdStatusEle0, chngcOrdStatusEle, chngcOrderEle0);
								
								rootOrdLineEle.setAttribute("Action","MODIFY");
								rootOrdLineEle.setAttribute("IsNewLine", "N");
								rootOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);
								
							}
						} else if(lineProcessCode.equalsIgnoreCase("C")) {
							hasLineC = true;
							if(YFCObject.isNull(fOrdLineKey) || YFCObject.isVoid(fOrdLineKey)) {
								throw new Exception("ExtnWebLineNumber Does Not Exist in FO. Line Process Code Cannot be C!");
							}
							if(YFCObject.isNull(cOrdLineKey) || YFCObject.isVoid(cOrdLineKey)) {
								throw new Exception("ExtnWebLineNumber Does Not Exist in CO. Line Process Code Cannot be C!"); 
							}
							
							this.handleLineProcessCodeCandA(env,cOrdHeaderKey, webLineNo, legacyLineNo, cOrdLineKey, rootOrdLineEle, fOrderEle, cOrderEle, 
									chngcOrdStatusEle0, chngcOrdStatusEle, chngcOrderEle0);
							
							rootOrdLineEle.setAttribute("Action","MODIFY");
							rootOrdLineEle.setAttribute("IsNewLine", "N");
							rootOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);
							
						} else if(lineProcessCode.equalsIgnoreCase("D")) {
							hasLineD = true;
							chngcOrderEle0.setAttribute("OrderHeaderKey", cOrdHeaderKey);
							chngcOrderEle0.setAttribute("Override", "Y");
							
							String isOrdPlace = "N";
							if(rootEle.hasAttribute("IsOrderPlace")) {
								isOrdPlace = rootEle.getAttribute("IsOrderPlace");
								if(YFCObject.isNull(isOrdPlace) || YFCObject.isVoid(isOrdPlace)) {
									isOrdPlace = "N";
								}
							}
							
							YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
							chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
							chngcOrdLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
							YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
							if(chngcOrdLineExtnEle0 != null) {
								chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
							}
							
							if(isOrdPlace.equalsIgnoreCase("Y")) {
								chngcOrdLineEle0.removeAttribute("OrderedQty");
								YFCElement chngcOrdLineTranQtyEle = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
								if(chngcOrdLineTranQtyEle != null) {
									chngcOrdLineEle0.removeChild(chngcOrdLineTranQtyEle);
								}
								
								YFCElement ordHoldTypesEle0 = rootOrdLineEle.getOwnerDocument().createElement("OrderHoldTypes");
								rootOrdLineEle.appendChild(ordHoldTypesEle0);
								YFCElement ordHoldTypeEle0 = ordHoldTypesEle0.getOwnerDocument().createElement("OrderHoldType");
								ordHoldTypesEle0.appendChild(ordHoldTypeEle0);
								ordHoldTypeEle0.setAttribute("HoldType","LEGACY_CNCL_LNE_HOLD");
								ordHoldTypeEle0.setAttribute("ReasonText", "Legacy Request For Order Line Cancel");
								ordHoldTypeEle0.setAttribute("Status", "1100");
								
							} else {
								// To get the ordered quantity from the customer order line.
								String cOrdQty = this.getOrderedQtyOnOrderLine(webLineNo, null, cOrderEle);
								if(YFCObject.isNull(cOrdQty) || YFCObject.isVoid(cOrdQty)){
									throw new Exception("ExtnWebLineNumber Does Not Exist in Customer Order!");
								}
								// To get the ordered quantity from the Fulfillment order line.
								String fOrdQty = this.getOrderedQtyForWebLineNo(webLineNo, legacyLineNo, fOrderEle);
								if(YFCObject.isNull(fOrdQty) || YFCObject.isVoid(fOrdQty)) {
									throw new Exception("ExtnWebLineNumber and ExtnLegacyLineNumber Combination Does Not Exist in Fulfillment Order!");
								}
								
								Float _ordLineQtyInCO = Float.parseFloat(cOrdQty);
								Float _ordLineQtyInFO = Float.parseFloat(fOrdQty);
								
								String ordQty = null;
								String tuom = null;
								YFCElement chngcOrdLineTranQtyEle = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
								if(chngcOrdLineTranQtyEle != null) {
									if(chngcOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
										ordQty = chngcOrdLineTranQtyEle.getAttribute("OrderedQty");
										if(YFCObject.isNull(ordQty) || YFCObject.isVoid(ordQty)) {
											ordQty = _ordLineQtyInFO.toString();
										}
									} else {
										ordQty = _ordLineQtyInFO.toString();
									}
									if(chngcOrdLineTranQtyEle.hasAttribute("TransactionalUOM")) {
										tuom = chngcOrdLineTranQtyEle.getAttribute("TransactionalUOM");
									}
								}
								
								Float _ordLineQtyInXML = Float.parseFloat(ordQty);
								if(log.isDebugEnabled()){
								log.debug("_ordLineQtyInXML:"+_ordLineQtyInXML);
								log.debug("_ordLineQtyInCO:"+_ordLineQtyInCO);
								log.debug("_ordLineQtyInFO:"+_ordLineQtyInFO);
								}
								log.verbose("_ordLineQtyInXML:"+_ordLineQtyInXML);
								log.verbose("_ordLineQtyInCO:"+_ordLineQtyInCO);
								log.verbose("_ordLineQtyInFO:"+_ordLineQtyInFO);
								
								if(_ordLineQtyInXML > _ordLineQtyInFO) {
									ordQty = _ordLineQtyInFO.toString();
								} else {
									ordQty = new Float(_ordLineQtyInCO-(_ordLineQtyInFO-_ordLineQtyInXML)).toString();
								}
								
								chngcOrdLineTranQtyEle.setAttribute("OrderedQty", ordQty);
								chngcOrdLineTranQtyEle.setAttribute("TransactionalUOM", tuom);
							}
							
							rootEle.setAttribute("OrderHeaderKey", fOrdHeaderKey);
							
							rootOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);
							rootOrdLineEle.removeAttribute("OrderedQty");
							rootOrdLineEle.setAttribute("IngoreLineFromFOChange", "N");
							rootOrdLineEle.setAttribute("IsNewLine", "N");
							YFCElement rootOrdLineTranQtyEle = rootOrdLineEle.getChildElement("OrderLineTranQuantity");
							if(rootOrdLineTranQtyEle != null) {
								rootOrdLineEle.removeChild(rootOrdLineTranQtyEle);
							}
							
						} else if(lineProcessCode.equalsIgnoreCase("_D")) {
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
		}	

		if(log.isDebugEnabled()){
		log.debug("hasLineA:"+hasLineA);
		log.debug("hasLineC:"+hasLineC);
		log.debug("hasLineD:"+hasLineD);
		log.debug("Boolean:"+(!hasLineA && !hasLineC && hasLineD));
		}
		
		return (!hasLineA && !hasLineC && hasLineD);
	}
	
	private void handleLineProcessCodeCandA(YFSEnvironment env,String ordHeaderKey, String webLineNo, String legacyLineNo, String cOrdLineKey, YFCElement rootOrdLineEle, 
			YFCElement fOrderEle, YFCElement cOrderEle, YFCElement chngcOrdStatusEle0, YFCElement chngcOrdStatusEle, YFCElement chngcOrderEle0) throws Exception {
		
		Float _ordLineQtyInXML = 0f;
		Float _unSchLineQtyInCO = 0f;
		Float _ordLineQtyInCO = 0f;
		Float _ordLineQtyInFO = 0f;
		
		YFCElement chngcOrdStatusLinesEle0 = chngcOrdStatusEle0.getChildElement("OrderLines");
		YFCElement chngcOrdStatusLinesEle = chngcOrdStatusEle.getChildElement("OrderLines");
		YFCElement chngcOrdLinesEle0 = chngcOrderEle0.getChildElement("OrderLines");
		
		List legacyLineNos = this.getLegacyLineNosToArray(fOrderEle);
		boolean legacyLineNoExists = legacyLineNos.contains(legacyLineNo);
		
		String ordQty = null;
		String tuom = null;
		String lineType = null;
		
		if(rootOrdLineEle.hasAttribute("LineType")) {
			lineType = rootOrdLineEle.getAttribute("LineType");
			if(YFCObject.isNull(lineType) || YFCObject.isVoid(lineType)) {
				throw new Exception("Attribute LineType Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute LineType Not Available in Incoming Legacy Message!");
		}
		
		YFCElement rootOrdLineTranQtyEle = rootOrdLineEle.getChildElement("OrderLineTranQuantity");
		if(rootOrdLineTranQtyEle != null) {
			if(rootOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
				ordQty = rootOrdLineTranQtyEle.getAttribute("OrderedQty");
				if(YFCObject.isNull(ordQty) || YFCObject.isVoid(ordQty)) {
					throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute OrderedQty Not Available in Incoming Legacy Message!");
			}
			if(rootOrdLineTranQtyEle.hasAttribute("TransactionalUOM")) {
				tuom = rootOrdLineTranQtyEle.getAttribute("TransactionalUOM");
				if((YFCObject.isNull(tuom) || YFCObject.isVoid(tuom)) && !lineType.equalsIgnoreCase("M")) {
					throw new Exception("Attribute TransactionalUOM Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute TransactionalUOM Not Available in Incoming Legacy Message!");
			}
		}
		
		_ordLineQtyInXML = Float.parseFloat(ordQty);
		_unSchLineQtyInCO = getUnScheduledQtyOnCOLine(webLineNo, cOrderEle);
		_ordLineQtyInCO = Float.parseFloat(this.getOrderedQtyOnOrderLine(webLineNo, null, cOrderEle));
		if(legacyLineNoExists) {
			_ordLineQtyInFO = Float.parseFloat(this.getOrderedQtyOnOrderLine(webLineNo, legacyLineNo, fOrderEle));
		} else {
			_ordLineQtyInFO = _ordLineQtyInXML;
		}
		
		if(log.isDebugEnabled()){
		log.debug("_ordLineQtyInXML:"+_ordLineQtyInXML);
		log.debug("_unSchLineQtyInCO:"+_unSchLineQtyInCO);
		log.debug("_ordLineQtyInCO:"+_ordLineQtyInCO);
		log.debug("_ordLineQtyInFO:"+_ordLineQtyInFO);
		}
		
		log.verbose("");
		log.verbose("_ordLineQtyInXML:"+_ordLineQtyInXML);
		log.verbose("_unSchLineQtyInCO:"+_unSchLineQtyInCO);
		log.verbose("_ordLineQtyInCO:"+_ordLineQtyInCO);
		log.verbose("_ordLineQtyInFO:"+_ordLineQtyInFO);
		log.verbose("");
		
		if(_unSchLineQtyInCO > 0) {
			Float tempQty = _ordLineQtyInXML - _unSchLineQtyInCO;
			if(tempQty > 0) {
				if(_ordLineQtyInXML > _ordLineQtyInFO) {
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
					if(chngcOrdLineTranQtyEle0 != null) {
						chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", new Float(_ordLineQtyInCO-_unSchLineQtyInCO).toString());
					}
					YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
					if(chngcOrdLineExtnEle0 != null) {
						if(chngcOrdLineExtnEle0.hasAttribute("ExtnLegacyLineNumber")) {
							chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
						}
					}
					
					chngcOrdStatusEle.setAttribute("OrderHeaderKey", ordHeaderKey);
					YFCElement chngcOrdStatusLineEle = chngcOrdStatusLinesEle.getOwnerDocument().createElement("OrderLine");
					chngcOrdStatusLinesEle.appendChild(chngcOrdStatusLineEle);
					chngcOrdStatusLineEle.setAttribute("OrderLineKey", cOrdLineKey);
					
					YFCElement chngcOrdStatusLineTranQtyEle = chngcOrdStatusLineEle.getOwnerDocument().createElement("OrderLineTranQuantity");
					chngcOrdStatusLineEle.appendChild(chngcOrdStatusLineTranQtyEle);
					chngcOrdStatusLineTranQtyEle.setAttribute("Quantity", _ordLineQtyInXML-_ordLineQtyInFO);
					chngcOrdStatusLineTranQtyEle.setAttribute("TransactionalUOM", tuom);
					
				} else {
					
					chngcOrderEle0.setAttribute("OrderHeaderKey", ordHeaderKey);
					chngcOrderEle0.setAttribute("Override", "Y");
					YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
					chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
					chngcOrdLineEle0.setAttribute("Action", "MODIFY");
					chngcOrdLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
					YFCElement chngcOrdLineTranQtyEle0 = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
					if(chngcOrdLineTranQtyEle0 != null) {
						if(legacyLineNoExists) {
							chngcOrdLineEle0.removeAttribute("OrderedQty");
							chngcOrdLineEle0.removeChild(chngcOrdLineTranQtyEle0);
						} else{
							chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", _ordLineQtyInCO + _ordLineQtyInXML);
						}
					}
					YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
					if(chngcOrdLineExtnEle0 != null) {
						if(chngcOrdLineExtnEle0.hasAttribute("ExtnLegacyLineNumber")) {
							chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
						}
					}
				}
			} else {
				if(_ordLineQtyInXML > _ordLineQtyInFO) {
					chngcOrdStatusEle0.setAttribute("OrderHeaderKey", ordHeaderKey);
					YFCElement chngcOrdStatusLineEle0 = chngcOrdStatusLinesEle0.getOwnerDocument().createElement("OrderLine");
					chngcOrdStatusLinesEle0.appendChild(chngcOrdStatusLineEle0);
					chngcOrdStatusLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
					
					YFCElement chngcOrdStatusLineTranQtyEle0 = chngcOrdStatusLineEle0.getOwnerDocument().createElement("OrderLineTranQuantity");
					chngcOrdStatusLineEle0.appendChild(chngcOrdStatusLineTranQtyEle0);
					chngcOrdStatusLineTranQtyEle0.setAttribute("Quantity", _ordLineQtyInXML-_ordLineQtyInFO);
					chngcOrdStatusLineTranQtyEle0.setAttribute("TransactionalUOM", tuom);
					
					chngcOrderEle0.setAttribute("OrderHeaderKey", ordHeaderKey);
					chngcOrderEle0.setAttribute("Override", "Y");
					YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
					chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
					chngcOrdLineEle0.setAttribute("Action", "MODIFY");
					chngcOrdLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
					YFCElement chngcOrdLineTranQtyEle0 = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
					if(chngcOrdLineTranQtyEle0 != null) {
						chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", new Float(_ordLineQtyInCO-(_ordLineQtyInXML-_ordLineQtyInFO)).toString());
					}
					YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
					if(chngcOrdLineExtnEle0 != null) {
						if(chngcOrdLineExtnEle0.hasAttribute("ExtnLegacyLineNumber")) {
							chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
						}
					}
					
					chngcOrdStatusEle.setAttribute("OrderHeaderKey", ordHeaderKey);
					YFCElement chngcOrdStatusLineEle = chngcOrdStatusLinesEle.getOwnerDocument().createElement("OrderLine");
					chngcOrdStatusLinesEle.appendChild(chngcOrdStatusLineEle);
					chngcOrdStatusLineEle.setAttribute("OrderLineKey", cOrdLineKey);
					
					YFCElement chngcOrdStatusLineTranQtyEle = chngcOrdStatusLineEle.getOwnerDocument().createElement("OrderLineTranQuantity");
					chngcOrdStatusLineEle.appendChild(chngcOrdStatusLineTranQtyEle);
					chngcOrdStatusLineTranQtyEle.setAttribute("Quantity", _ordLineQtyInXML-_ordLineQtyInFO);
					chngcOrdStatusLineTranQtyEle.setAttribute("TransactionalUOM", tuom);
				} else {
					
					chngcOrderEle0.setAttribute("OrderHeaderKey", ordHeaderKey);
					chngcOrderEle0.setAttribute("Override", "Y");
					YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
					chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
					chngcOrdLineEle0.setAttribute("Action", "MODIFY");
					chngcOrdLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
					YFCElement chngcOrdLineTranQtyEle0 = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
					if(chngcOrdLineTranQtyEle0 != null) {
						if(legacyLineNoExists) {
							chngcOrdLineEle0.removeAttribute("OrderedQty");
							chngcOrdLineEle0.removeChild(chngcOrdLineTranQtyEle0);
						} else{
							chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", _ordLineQtyInCO + _ordLineQtyInXML);
						}
					}
					YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
					if(chngcOrdLineExtnEle0 != null) {
						if(chngcOrdLineExtnEle0.hasAttribute("ExtnLegacyLineNumber")) {
							chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
						}
					}
				}
			}
		} else {
			if(_ordLineQtyInXML > _ordLineQtyInFO) {
				chngcOrdStatusEle.setAttribute("OrderHeaderKey", ordHeaderKey);
				YFCElement chngcOrdStatusLineEle = chngcOrdStatusLinesEle.getOwnerDocument().createElement("OrderLine");
				chngcOrdStatusLinesEle.appendChild(chngcOrdStatusLineEle);
				chngcOrdStatusLineEle.setAttribute("OrderLineKey", cOrdLineKey);
				
				YFCElement chngcOrdStatusLineTranQtyEle = chngcOrdStatusLineEle.getOwnerDocument().createElement("OrderLineTranQuantity");
				chngcOrdStatusLineEle.appendChild(chngcOrdStatusLineTranQtyEle);
				chngcOrdStatusLineTranQtyEle.setAttribute("Quantity", _ordLineQtyInXML-_ordLineQtyInFO);
				chngcOrdStatusLineTranQtyEle.setAttribute("TransactionalUOM", tuom);
			}
			
			chngcOrderEle0.setAttribute("OrderHeaderKey", ordHeaderKey);
			chngcOrderEle0.setAttribute("Override", "Y");
			YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(rootOrdLineEle, true);
			chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
			chngcOrdLineEle0.setAttribute("Action", "MODIFY");
			chngcOrdLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
			YFCElement chngcOrdLineTranQtyEle0 = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
			if(chngcOrdLineTranQtyEle0 != null) {
				if(legacyLineNoExists) {
					chngcOrdLineEle0.removeAttribute("OrderedQty");
					chngcOrdLineEle0.removeChild(chngcOrdLineTranQtyEle0);
				} else{
					chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", _ordLineQtyInCO + _ordLineQtyInXML);
				}
			}
			YFCElement chngcOrdLineExtnEle0 = chngcOrdLineEle0.getChildElement("Extn");
			if(chngcOrdLineExtnEle0 != null) {
				if(chngcOrdLineExtnEle0.hasAttribute("ExtnLegacyLineNumber")) {
					chngcOrdLineExtnEle0.setAttribute("ExtnLegacyLineNumber", "");
				}
			}
		}
	}
	
	private YFCElement getCustomerInformation(YFSEnvironment env, YFCElement rootEle, String suffixType) throws Exception {
		
		YFCElement extnRootEle = rootEle.getChildElement("Extn");
		
		YFCDocument getCustListInXML = YFCDocument.getDocumentFor("<Customer/>");
		YFCElement custInXMLEle = getCustListInXML.getDocumentElement();
		if(rootEle.hasAttribute("EnterpriseCode")) {
			String orgCode = rootEle.getAttribute("EnterpriseCode");
			if(YFCObject.isNull(orgCode) || YFCObject.isNull(orgCode)) {
				throw new Exception("Attribute EnterpriseCode cannot be NULL or Void!");
			}
			custInXMLEle.setAttribute("OrganizationCode", orgCode);
		} else {
			throw new Exception("Attribute EnterpriseCode Not Available in Incoming Legacy Message!");
		}
		
		YFCElement extnCustInXMLEle = getCustListInXML.createElement("Extn");
		custInXMLEle.appendChild(extnCustInXMLEle);
		
		if(extnRootEle.hasAttribute("ExtnCustomerNo")) {
			String legacyCustNo = extnRootEle.getAttribute("ExtnCustomerNo");
			if(YFCObject.isNull(legacyCustNo) || YFCObject.isVoid(legacyCustNo)) {
				throw new Exception("Attribute ExtnCustomerNo Cannot be NULL or Void!");
			}
			extnCustInXMLEle.setAttribute("ExtnLegacyCustNumber", legacyCustNo);
		} else {
			throw new Exception("Attribute ExtnLegacyCustNumber Not Available in Incoming Legacy Message!");
		}
		
		if(YFCObject.isNull(suffixType) || YFCObject.isVoid(suffixType)) {
			throw new Exception("Customer SuffixType Cannot be NULL or Void!");
		}
		
		// To set the attribute based on suffix type.
		if(suffixType.equalsIgnoreCase("MC")){
			if(extnRootEle.hasAttribute("ExtnBillToSuffix")) {
				String buillToSuffix = extnRootEle.getAttribute("ExtnBillToSuffix");
				if(YFCObject.isNull(buillToSuffix) || YFCObject.isVoid(buillToSuffix)) {
					throw new Exception("Attribute ExtnBillToSuffix Cannot be NULL or Void!");
				}
				extnCustInXMLEle.setAttribute("ExtnBillToSuffix", buillToSuffix);
			} else {
				throw new Exception("Attribute ExtnBillToSuffix Not Available in Incoming Legacy Message!");
			}
		} else if(suffixType.equalsIgnoreCase("S")) {
			if(extnRootEle.hasAttribute("ExtnShipToSuffix")) {
				String shipToSuffix = extnRootEle.getAttribute("ExtnShipToSuffix");
				if(YFCObject.isNull(shipToSuffix) || YFCObject.isVoid(shipToSuffix)) {
					throw new Exception("Attribute ExtnShipToSuffix Cannot be NULL or Void!");
				}
				extnCustInXMLEle.setAttribute("ExtnShipToSuffix", shipToSuffix);
			} else {
				throw new Exception("Attribute ExtnBillToSuffix Not Available in Incoming Legacy Message!");
			}
		} else {
			throw new Exception("Customer SuffixType is invalid in incoming legacy message.");
		}
		if(suffixType.trim().equalsIgnoreCase("MC")) {
			extnCustInXMLEle.setAttribute("ExtnSuffixType", "B");
		} else {
			extnCustInXMLEle.setAttribute("ExtnSuffixType", suffixType.trim());
		}
		
		if(log.isDebugEnabled()){
		log.debug("XPXGetCustomerList-InXML:"+getCustListInXML.getString());
		}
		log.verbose("");
		log.verbose("XPXGetCustomerList-InXML:"+getCustListInXML.getString());
		
		Document tempDoc = api.executeFlow(env, "XPXGetCustomerList", getCustListInXML.getDocument());
		if(tempDoc == null) {
			throw new Exception("XPXGetCustomerList Flow Returned ZERO Customers!");
		}
		
		if(log.isDebugEnabled()){
		log.debug("XPXGetCustomerList-OutXML:"+YFCDocument.getDocumentFor(tempDoc).getString());
		}
		log.verbose("");
		log.verbose("XPXGetCustomerList-OutXML:"+YFCDocument.getDocumentFor(tempDoc).getString());
		
		YFCElement custEle = null;
		YFCDocument getCustListOutXML = YFCDocument.getDocumentFor(tempDoc);
		YFCElement custListOutXMLEle = getCustListOutXML.getDocumentElement();
		YFCIterable yfcItr = custListOutXMLEle.getChildren("Customer");
		if(!yfcItr.hasNext()) {
			throw new Exception("XPXGetCustomerList Flow Returned ZERO Customers!");
		} else {
			if(suffixType.equalsIgnoreCase("MC")) {
				String rootCustKey = null;
				YFCElement billToCustEle = (YFCElement) yfcItr.next();
				if(billToCustEle.hasAttribute("RootCustomerKey")) {
					rootCustKey = billToCustEle.getAttribute("RootCustomerKey");
					if(YFCObject.isNull(rootCustKey) || YFCObject.isVoid(rootCustKey)) {
						throw new Exception("Attribute RootCustomerKey Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute RootCustomerKey Not Available in getCustomerList Template!");
				}
				
				YFCDocument getRootCustListInXML = YFCDocument.getDocumentFor("<Customer/>");
				YFCElement rootCustInXMLEle = getRootCustListInXML.getDocumentElement();
				rootCustInXMLEle.setAttribute("CustomerKey", rootCustKey);
				rootCustInXMLEle.setAttribute("ExtnSuffixType", suffixType.trim());
				
				if(log.isDebugEnabled()){
				log.debug("XPXGetCustomerList-InXML:"+getRootCustListInXML.getString());
				}
				log.verbose("");
				log.verbose("XPXGetCustomerList-InXML:"+getRootCustListInXML.getString());
				
				tempDoc = api.executeFlow(env, "XPXGetCustomerList", getRootCustListInXML.getDocument());
				if(tempDoc == null) {
					throw new Exception("XPXGetCustomerList - MSAP Customer Not Found!");
				}
				
				if(log.isDebugEnabled()){
				log.debug("XPXGetCustomerList-OutXML:"+YFCDocument.getDocumentFor(tempDoc).getString());
				}
				log.verbose("");
				log.verbose("XPXGetCustomerList-OutXML:"+YFCDocument.getDocumentFor(tempDoc).getString());
				
				getCustListOutXML = YFCDocument.getDocumentFor(tempDoc);
				custListOutXMLEle = getCustListOutXML.getDocumentElement();
				yfcItr = custListOutXMLEle.getChildren("Customer");
				if(!yfcItr.hasNext()) {
					throw new Exception("XPXGetCustomerList Flow Returned ZERO Customers!");
				} else {
					custEle = (YFCElement)yfcItr.next();
				}
				
			} else {
				custEle = (YFCElement)yfcItr.next();
			}
		}
		
		return custEle;
	}
	
	
	/**
	 *  Gets Bill To name from customer information element.
	 *  
	 *  @param  custInfoEle Element contains the customer information.	                          
	 *	@return  Bill To Name of the customer. 	                                               
	 */
	private String getBillToName(YFCElement custInfoEle) throws Exception {
		
		YFCElement buyerOrgEle = custInfoEle.getChildElement("BuyerOrganization");
		if(buyerOrgEle == null) {
			throw new Exception("Customer/BuyerOrganization Element Not Available in GetCustomerList Template!");
		} 
		String orgName = null;
		if(buyerOrgEle.hasAttribute("OrganizationName")) {
			orgName = buyerOrgEle.getAttribute("OrganizationName");
			if(YFCObject.isNull(orgName) || YFCObject.isVoid(orgName)) {
				throw new Exception("Attribute OrganizationName Cannot be NULL or Void!");
			}
		}
		
		return orgName;
	}
	
	/**
	 *  Gets Bill To ID from customer information element.
	 *  
	 *  @param  custInfoEle Element contains the customer information.	                          
	 *	@return  Bill To ID of the customer. 	                                               
	 */
	private String getBillToID(YFCElement custInfoEle) throws Exception {
		
		String billToID = null;
		if(custInfoEle.hasAttribute("CustomerID")) {
			billToID = custInfoEle.getAttribute("CustomerID");
			if(YFCObject.isNull(billToID) || YFCObject.isVoid(billToID)) {
				throw new Exception("Attribute CustomerID Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute CustomerID Not Available in GetCustomerList Template!");
		}
		
		return billToID;
	}
	
	/**
	 *  Gets SAP Parent name from customer information element.
	 *  
	 *  @param  custInfoEle Element contains the customer information.	                          
	 *	@return  SAP Parent Name of the customer. 	                                               
	 */
	private String getSAPParentName(YFCElement custInfoEle) throws Exception {
		
		YFCElement extnEle = custInfoEle.getChildElement("Extn");
		if(extnEle == null) {
			throw new Exception("Customer/Extn Element Not Available in GetCustomerList Template!");
		} 
		String sapParentName = null;
		if(extnEle.hasAttribute("ExtnSAPParentName")) {
			sapParentName = extnEle.getAttribute("ExtnSAPParentName");
			if(YFCObject.isNull(sapParentName) || YFCObject.isVoid(sapParentName)) {
				throw new Exception("Attribute ExtnSAPParentName Cannot be NULL or Void!");
			}
		}
		
		return sapParentName;
	}
	
	/**
	 *  Gets Buyer organization code from customer information element.
	 *  
	 *  @param  custInfoEle Element contains the customer information.	                          
	 *	@return  Buyer Organization code of the customer. 	                                               
	 */
	private String getBuyerOrgCode(YFCElement custInfoEle) throws Exception {
		
		String buyerOrgCode = null;
		if(custInfoEle.hasAttribute("CustomerID")) {
			buyerOrgCode = custInfoEle.getAttribute("CustomerID");
			if(YFCObject.isNull(buyerOrgCode) || YFCObject.isVoid(buyerOrgCode)) {
				throw new Exception("Attribute CustomerID Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute CustomerID Not Available in GetCustomerList Template!");
		}
		
		return buyerOrgCode;
	}
	
	/**
	 *  Gets Ship To ID from customer information element.
	 *  
	 *  @param  custInfoEle Element contains the customer information.	                          
	 *	@return  Ship To ID of the customer. 	                                               
	 */
	private String getShipToID(YFCElement custInfoEle) throws Exception {
		
		String shipToID = null;
		if(custInfoEle.hasAttribute("CustomerID")) {
			shipToID = custInfoEle.getAttribute("CustomerID");
			if(YFCObject.isNull(shipToID) || YFCObject.isVoid(shipToID)) {
				throw new Exception("Attribute CustomerID Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute CustomerID Not Available in GetCustomerList Template!");
		}
		
		return shipToID;
	}
	
	/**
	 *  Gets Environment code from customer information element.
	 *  
	 *  @param  custInfoEle Element contains the customer information.	                          
	 *	@return  Environment code. 	                                               
	 */
	private String getOrigEnvCode(YFCElement custInfoEle) throws Exception {
		
		YFCElement extnEle = custInfoEle.getChildElement("Extn");
		if(extnEle == null) {
			throw new Exception("Customer/Extn Element Not Available in GetCustomerList Template!");
		} 
		String orgEnvCode = null;
		if(extnEle.hasAttribute("ExtnOrigEnvironmentCode")) {
			orgEnvCode = extnEle.getAttribute("ExtnOrigEnvironmentCode");
			if(YFCObject.isNull(orgEnvCode) || YFCObject.isVoid(orgEnvCode)) {
				throw new Exception("Attribute ExtnOrigEnvironmentCode Cannot be NULL or Void!");
			}
		}
		
		return orgEnvCode;
	}
	
	/**
	 * 	Updates the customer order.
	 * 
	 *  @param env Environment variable.
	 *  @param rootEle Input xml from legacy system
	 *  @param cOrderEle Element contains the customer information.	                          
	 *	@return tempDoc Output document of the change order API. 	                                               
	 */
	
	private YFCDocument updateCustomerOrder(YFSEnvironment env, YFCElement rootEle, YFCElement cOrderEle) throws Exception {
		
		String _ordHeaderKey = null;
		if(cOrderEle.hasAttribute("OrderHeaderKey")) {
			_ordHeaderKey = cOrderEle.getAttribute("OrderHeaderKey");
			if(YFCObject.isNull(_ordHeaderKey) || YFCObject.isVoid(_ordHeaderKey)) {
				throw new Exception("Attribute OrderHeader Cannot be NULL or Void!");
			}
			rootEle.setAttribute("OrderHeaderKey", _ordHeaderKey);
			rootEle.setAttribute("Override", "Y");
			if(rootEle.hasAttribute("ShipNode")) {
				rootEle.removeAttribute("ShipNode");
			}
		} else {
			throw new Exception("Attribute OrderHeaderKey Not Available in OrderList Template!");
		}
		
		YFCElement rootOrdExtnEle = rootEle.getChildElement("Extn");
		if(rootOrdExtnEle != null) {
			if(rootOrdExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
				rootOrdExtnEle.setAttribute("ExtnLegacyOrderNo", "");
			}
			if(rootOrdExtnEle.hasAttribute("ExtnGenerationNo")) {
				rootOrdExtnEle.setAttribute("ExtnGenerationNo", "");
			}
		}
		
		// Forms list of web line numbers of the new lines to be added.
		List webLineNosInXML = getWebLineNosToArray(rootEle);
		if(webLineNosInXML.size() == 0) {
			throw new Exception("No WebLineNumber Available in the Incoming Legacy Message!");
		}
		// Forms list of web line numbers of the existing web line numbers in the customer order.
		List webLineNosExistingCO = getWebLineNosToArray(cOrderEle);
		if(webLineNosExistingCO.size() == 0) {
			throw new Exception("No WebLineNumber Available in the Existing Customer Order!");
		}
		
		List _newLinesList = new ArrayList(webLineNosInXML);
		List _existingLinesList = new ArrayList(webLineNosExistingCO);
		
		// To remove all the existing web line numbers from the new line list that needs to be created.
		_newLinesList.removeAll(webLineNosExistingCO);
		_existingLinesList.retainAll(webLineNosInXML);
		if(log.isDebugEnabled()){
		log.debug("_NewLinesList:"+_newLinesList);
		log.debug("_ExistingLinesList:"+_existingLinesList);
		}
		log.verbose("_NewLinesList:"+_newLinesList);
		log.verbose("_ExistingLinesList:"+_existingLinesList);
		
		if(_newLinesList.size() > 0 || _existingLinesList.size() > 0) {
			// Add New Lines to CO
			YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
			if(rootOrdLinesEle != null) {
				YFCIterable yfcItr = rootOrdLinesEle.getChildren("OrderLine");
				while(yfcItr.hasNext()) {
					YFCElement rootOrdLineEle = (YFCElement)yfcItr.next();
					YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
					if(rootOrdLineExtnEle != null) {
						if(rootOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
							String webLineNo = rootOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							// Check if the webline no in the input xml is there in the new lines list that needs to be created. 
							// Else remove the particular line.
							if(!_newLinesList.contains(webLineNo) && !_existingLinesList.contains(webLineNo)) {
								rootOrdLinesEle.removeChild(rootOrdLineEle);
								yfcItr = rootOrdLinesEle.getChildren("OrderLine");
							} else {
								if(_newLinesList.contains(webLineNo)) {
									rootOrdLineEle.setAttribute("IsNewLine", "Y");
									rootOrdLineEle.setAttribute("Action", "CREATE");
								} else {
									rootOrdLineEle.setAttribute("IsNewLine", "N");
								}
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in Incoming Legacy Message!");
						}
						if(rootOrdLineEle.hasAttribute("ShipNode")) {
							rootOrdLineEle.removeAttribute("ShipNode");
						}
						if(rootOrdLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
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
		
		this.setExtendedPriceInfo(env,chngcOrdEle, true);
		this.filterAttributes(chngcOrdEle, true);
		
		if(log.isDebugEnabled()){
		log.debug("XPXChangeOrder_CO-InXML:"+chngcOrdEle.getString());
		}
		log.verbose("");
		log.verbose("XPXChangeOrder_CO-InXML:"+chngcOrdEle.getString());
		// Change order call to update the customer order.
		tempDoc = this.api.executeFlow(env, "XPXChangeOrder", chngcOrdEle.getOwnerDocument().getDocument());
		if(tempDoc != null) {
			return YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("XPXChangeOrder Service Failed!");
		}
		
	}
	
	private boolean isCancelledOrder(YFCElement orderEle) {
		
		String minOrdStatus = null;
		if(orderEle.hasAttribute("MinOrderStatus")) {
			minOrdStatus = orderEle.getAttribute("MinOrderStatus");
			if(YFCObject.isNull(minOrdStatus) || YFCObject.isVoid(minOrdStatus)) {
				return false;
			}
		} else {
			return false;
		}
		
		String maxOrdStatus = null;
		if(orderEle.hasAttribute("MaxOrderStatus")) {
			maxOrdStatus = orderEle.getAttribute("MaxOrderStatus");
			if(YFCObject.isNull(maxOrdStatus) || YFCObject.isVoid(maxOrdStatus)) {
				return false;
			}
		} else {
			return false;
		}
		
		if(minOrdStatus.equalsIgnoreCase("9000") && maxOrdStatus.equalsIgnoreCase("9000")) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean IsCancelledLine(String webLineNo, String legacyLineNo, YFCElement orderEle) throws Exception {
		
		if(YFCObject.isNull(legacyLineNo) || YFCObject.isVoid(legacyLineNo)) {
			legacyLineNo = "DUMMY";
		}
		
		YFCElement ordLinesEle = orderEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					String ordQty = null;
					if(ordLineEle.hasAttribute("OrderedQty")) {
						ordQty = ordLineEle.getAttribute("OrderedQty");
						if(YFCObject.isNull(ordQty) || YFCObject.isVoid(ordQty)) {
							throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute OrderedQty Not Available in OrderList Template!");
					}
					
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						String _webLineNo = null;
						String _legacyLineNo = null;
						if(extnEle.hasAttribute("ExtnLegacyLineNumber")) {
							_legacyLineNo = extnEle.getAttribute("ExtnLegacyLineNumber");
							if(YFCObject.isNull(_legacyLineNo) || YFCObject.isVoid(_legacyLineNo)) {
								_legacyLineNo = "DUMMY";
							}
						} else {
							_legacyLineNo = "DUMMY";
						}
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							if(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)) {
								Float ordLineQty = Float.parseFloat(ordQty);
								if(ordLineQty == 0) {
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
	 * 	Gets unscheduled quantity of the customer order line.
	 * 
	 *  @param webLineNo Web line number.
	 *  @param cOrderEle Element contains the customer information.	                          
	 *	@return unSchLineQty Unscheduled quantity. 	                                               
	 */
	
	private Float getUnScheduledQtyOnCOLine(String webLineNo, YFCElement cOrderEle) throws Exception {
		
		Float unSchLineQty = 0f;
		
		YFCElement ordLinesEle = cOrderEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
							String _webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							if(_webLineNo.equalsIgnoreCase(webLineNo)) {
								String status = null;
								String statusQty = null;
								YFCElement ordLineStatusesEle = ordLineEle.getChildElement("OrderStatuses");
								if(ordLineStatusesEle != null) {
									YFCIterable yfcItr1 = ordLineStatusesEle.getChildren("OrderStatus");
									while(yfcItr1.hasNext()) {
										YFCElement ordLineStatusEle = (YFCElement) yfcItr1.next();
										if(ordLineStatusEle.hasAttribute("Status")) {
											status = ordLineStatusEle.getAttribute("Status");
											if(YFCObject.isNull(status) || YFCObject.isVoid(status)) {
												throw new Exception("Attribute OrderLine/OrderStatuses/OrderStatus/@Status Cannot be NULL or Void!");
											}
										} else {
											throw new Exception("Attribute OrderLine/OrderStatuses/OrderStatus/@Status Not Available in OrderList Template!");
										}
										
										if(ordLineStatusEle.hasAttribute("StatusQty")) {
											statusQty = ordLineStatusEle.getAttribute("StatusQty");
											if(YFCObject.isNull(statusQty) || YFCObject.isVoid(statusQty)) {
												throw new Exception("Attribute OrderLine/OrderStatuses/OrderStatus/@StatusQty Cannot be NULL or Void!");
											}
										} else {
											throw new Exception("Attribute OrderLine/OrderStatuses/OrderStatus/@StatusQty Not Available in OrderList Template!");
										}
										// Fetch the status quantity where status = 'Unscheduled'
										if(status.equalsIgnoreCase("1310")) {
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
	
	private String getUnitPriceOnOrderLine(String webLineNo, String legacyLineNo, YFCElement orderEle) throws Exception {
		
		if(YFCObject.isNull(legacyLineNo) || YFCObject.isVoid(legacyLineNo)) {
			legacyLineNo = "DUMMY";
		}
		
		YFCElement ordLinesEle = orderEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						String _webLineNo = null;
						String _legacyLineNo = null;
						if(extnEle.hasAttribute("ExtnLegacyLineNumber")) {
							_legacyLineNo = extnEle.getAttribute("ExtnLegacyLineNumber");
							if(YFCObject.isNull(_legacyLineNo) || YFCObject.isVoid(_legacyLineNo)) {
								_legacyLineNo = "DUMMY";
							}
						} else {
							_legacyLineNo = "DUMMY";
						}
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							if(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)) {
								YFCElement linePriceInfoEle = ordLineEle.getChildElement("LinePriceInfo");
								if(linePriceInfoEle != null) {
									if(linePriceInfoEle.hasAttribute("UnitPrice")) {
										String _unitPrice = linePriceInfoEle.getAttribute("UnitPrice");
										if(YFCObject.isNull(_unitPrice) || YFCObject.isVoid(_unitPrice)) {
											throw new Exception("Attribute LinePriceInfo/@UnitPrice Cannot be NULL or Void!");
										}
										return _unitPrice;
									} else {
										throw new Exception("Attribute LinePriceInfo/@UnitPrice Not Available in the OrderList Template!");
									}
								} else {
									throw new Exception("Element LinePriceInfo Not Available in the OrderList Template!");
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
	 * 	Returns ordered quantity on customer order.
	 * 
	 *  @param env Environment variable.
	 *  @param rootEle Input xml from legacy system
	 *  @param cOrderEle Element contains the customer information.	                          
	 *	@return tempDoc Output document of the change order API. 	                                               
	 */

	private String getOrderedQtyOnOrderLine(String webLineNo, String legacyLineNo, YFCElement orderEle) throws Exception {
		
		if(YFCObject.isNull(legacyLineNo) || YFCObject.isVoid(legacyLineNo)) {
			legacyLineNo = "DUMMY";
		}
		
		YFCElement ordLinesEle = orderEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						String _webLineNo = null;
						String _legacyLineNo = null;
						if(extnEle.hasAttribute("ExtnLegacyLineNumber")) {
							_legacyLineNo = extnEle.getAttribute("ExtnLegacyLineNumber");
							if(YFCObject.isNull(_legacyLineNo) || YFCObject.isVoid(_legacyLineNo)) {
								_legacyLineNo = "DUMMY";
							}
						} else {
							_legacyLineNo = "DUMMY";
						}
						
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							if(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)) {
								YFCElement ordLineTranQtyEle = ordLineEle.getChildElement("OrderLineTranQuantity");
								if(ordLineTranQtyEle != null) {
									if(ordLineTranQtyEle.hasAttribute("OrderedQty")) {
										String _ordQty = ordLineTranQtyEle.getAttribute("OrderedQty");
										if(YFCObject.isNull(_ordQty) || YFCObject.isVoid(_ordQty)) {
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
		if(rootEle.hasAttribute("OrderHeaderKey")) {
			_ordHeaderKey = rootEle.getAttribute("OrderHeaderKey");
			if(YFCObject.isNull(_ordHeaderKey) || YFCObject.isVoid(_ordHeaderKey)) {
				throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute OrderHeaderKey Not Available in Incoming Legacy Message!");
		}
		// To set 'XPX Chain FO Cancel Status' transaction.
		String tranId = null;
		if(this._prop != null) {
			tranId = this._prop.getProperty("XPX_CHN_CNL_STATUS_TXN");
			if(YFCObject.isNull(tranId) || YFCObject.isVoid(tranId)) {
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
		if(rootOrdLinesEle != null) {
			YFCIterable yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				
				String isNewLine = null;
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				if(rootOrdLineEle.hasAttribute("IsNewLine")) {
					isNewLine = rootOrdLineEle.getAttribute("IsNewLine");
					if(YFCObject.isNull(isNewLine) || YFCObject.isVoid(isNewLine)) {
						isNewLine = "Y";
					} else {
						if("Y".equalsIgnoreCase(isNewLine)) {
							isNewLine = "Y";
						} else {
							isNewLine = "N";
						}
					}
				} else {
					isNewLine = "Y";
				}
				// To retrieve the order quantity from input xml.
				if(isNewLine.equalsIgnoreCase("N")) {
					String _ordLineKey = null;
					String _tuom = null;
					String _lineType = null;
					
					if(rootOrdLineEle.hasAttribute("LineType")) {
						_lineType = rootOrdLineEle.getAttribute("LineType");
						if(YFCObject.isNull(_lineType) || YFCObject.isVoid(_lineType)) {
							throw new Exception("Attribute LineType Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute LineType Not Available in Incoming Legacy Message!");
					}

					YFCElement rootOrdLineTranQtyEle = rootOrdLineEle.getChildElement("OrderLineTranQuantity");
					if(rootOrdLineTranQtyEle != null) {
						if(rootOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
							String _ordQtyInXML = rootOrdLineTranQtyEle.getAttribute("OrderedQty");
							if(YFCObject.isNull(ordQtyInXML) || YFCObject.isVoid(ordQtyInXML)) {
								throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
							}
							ordQtyInXML = Float.parseFloat(_ordQtyInXML);
						} else {
							throw new Exception("Attribute OrderedQty Not Available in Incoming Legacy Message!");
						}
						if(rootOrdLineTranQtyEle.hasAttribute("TransactionalUOM")) {
							_tuom = rootOrdLineTranQtyEle.getAttribute("TransactionalUOM");
							if((YFCObject.isNull(_tuom) || YFCObject.isVoid(_tuom)) && !_lineType.equalsIgnoreCase("M")) {
								throw new Exception("Attribute TransactionalUOM Cannot be NULL or Void!");
							}
						} else {
							throw new Exception("Attribute TransactionalUOM Not Available in Incoming Legacy Message!");
						}
					}
					YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
					if(rootOrdLineExtnEle != null) {
						if(rootOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
							String webLineNo = rootOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							_ordLineKey = this.getOrderLineKeyForWebLineNo(webLineNo, null, cOrderEle);
							ordQtyInCO = Float.parseFloat(getOrderedQtyOnOrderLine(webLineNo, null, cOrderEle));
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
					log.debug("ordQtyInCO:"+ordQtyInCO);
					log.debug("ordQtyInCOUnSch:"+ordQtyInCOUnSch);
					log.debug("ordQtyInXML:"+ordQtyInXML);
					}
					
					if(ordQtyInCOUnSch > 0) {
						Float tempQty = ordQtyInXML - ordQtyInCOUnSch;
						if(tempQty > 0) {
							isChngCOStatusReq = true;
							YFCElement chngCOStatusLineEle = chngCOStatusLinesEle.getOwnerDocument().createElement("OrderLine");
							chngCOStatusLinesEle.appendChild(chngCOStatusLineEle);
							chngCOStatusLineEle.setAttribute("OrderLineKey", _ordLineKey);
							
							YFCElement chngCOStatusLineTranQtyEle = chngCOStatusLineEle.getOwnerDocument().createElement("OrderLineTranQuantity");
							chngCOStatusLineEle.appendChild(chngCOStatusLineTranQtyEle);
							chngCOStatusLineTranQtyEle.setAttribute("Quantity", ordQtyInCOUnSch);
							chngCOStatusLineTranQtyEle.setAttribute("TransactionalUOM", _tuom);
							
							if(rootOrdLineTranQtyEle != null) {
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
							
							if(rootOrdLineTranQtyEle != null) {
								rootOrdLineTranQtyEle.setAttribute("OrderedQty", new Float(ordQtyInCO).toString());
							}
						}
					} else {
						if(rootOrdLineTranQtyEle != null) {
							rootOrdLineTranQtyEle.setAttribute("OrderedQty", new Float(ordQtyInXML + ordQtyInCO).toString());
						}
					}
				}
			}
			
			if(isChngCOStatusReq) {
				
				if(log.isDebugEnabled()){
				log.debug("XPXChangeOrderStatus_CO-InXML:"+chngCOStatusInXML.getString());
				}
				log.verbose("");
				log.verbose("XPXChangeOrderStatus_CO-InXML:"+chngCOStatusInXML.getString());
				
				this.api.executeFlow(env, "XPXChangeOrderStatus", chngCOStatusInXML.getDocument());
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
		if(custExtnEle != null) {
			
			if(custExtnEle.hasAttribute("ExtnCustomerNo")) {
				custNo = custExtnEle.getAttribute("ExtnCustomerNo");
				if(YFCObject.isNull(custNo) || YFCObject.isVoid(custNo)) {
					throw new Exception("Attribute OrderLine/Extn/@ExtnCustomerNo Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ExtnCustomerNo Not Available in Incoming Legacy Message!");
			}
			
			if(custExtnEle.hasAttribute("ExtnEnvtId")) {
				envId = custExtnEle.getAttribute("ExtnEnvtId");
				if(YFCObject.isNull(envId) || YFCObject.isVoid(envId)) {
					throw new Exception("Attribute OrderLine/Extn/@ExtnEnvtId Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ExtnEnvId Not Available in Incoming Legacy Message!");
			}
			
			if(custExtnEle.hasAttribute("ExtnCompanyId")) {
				compId = custExtnEle.getAttribute("ExtnCompanyId");
				if(YFCObject.isNull(compId) || YFCObject.isVoid(compId)) {
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
	 * 	Creates a new Customer Order.
	 * 
	 *  @param env Environment variable.
	 *  @param rootEle Input xml from legacy system                          
	 *	@return tempDoc Output document of the Create order API. 	                                               
	 */
		
	private YFCDocument createCustomerOrder(YFSEnvironment env, YFCElement rootEle) throws Exception {
		
		rootEle.setAttribute("Action", "CREATE");
		rootEle.setAttribute("OrderType", "Customer");
		
		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		if(rootExtnEle != null) {
			
			rootExtnEle.setAttribute("ExtnLegacyOrderNo", "");
			rootExtnEle.setAttribute("ExtnGenerationNo", "");
			
			YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
			YFCIterable yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				rootOrdLineEle.setAttribute("Action", "CREATE");
				
				YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
				if(rootOrdLineExtnEle != null) {
					if(rootOrdLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
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
		log.debug("XPXCreateOrder_CO-InXML:"+rootEle.getString());
		}
		log.verbose("");
		log.verbose("XPXCreateOrder_CO-InXML:"+rootEle.getString());
		
		Document tempDoc = this.api.executeFlow(env, "XPXCreateOrder", rootEle.getOwnerDocument().getDocument());
		if(tempDoc != null) {
			return YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("XPXCreateOrder Service Failed!");
		}
		
	}
	
	private String getInventoryIndicator(YFSEnvironment env, String itemId, String shipNode, String envtId, String lineType) throws Exception {
		if(log.isDebugEnabled()){
		log.debug("**************************************GetInventoryIndicator*******************************************");
		
		log.debug("ItemID:"+itemId);
		log.debug("ShipNode:"+shipNode);
		log.debug("EnvtId:"+envtId);
		log.debug("LineType:"+lineType);
		}
		
		log.verbose("**************************************GetInventoryIndicator*******************************************");
		log.verbose("");
		log.verbose("ItemID:"+itemId);
		log.verbose("ShipNode:"+shipNode);
		log.verbose("EnvtId:"+envtId);
		log.verbose("LineType:"+lineType);
		log.verbose("");
		
		boolean callItemList = false;
		String invIndicator = null;
		String stockType = null;
		
		YFCDocument getItemExtnListInXML = YFCDocument.getDocumentFor("<XPXItemExtn/>");
		YFCElement getItemExtnListEle = getItemExtnListInXML.getDocumentElement();
		getItemExtnListEle.setAttribute("ItemID", itemId);		
		getItemExtnListEle.setAttribute("EnvironmentID", envtId);
		
		/* start - changes made to fix Issue 1501 */
		if(shipNode.contains("_")){
			// Need to remove the environment id from ship node.
			String[] splitArrayOnUom = shipNode.split("_");			
			if(splitArrayOnUom.length > 0){
			shipNode = splitArrayOnUom[0];
			}
		}		
		getItemExtnListEle.setAttribute("XPXDivision", shipNode);
		/* End - changes made to fix Issue 1501 */
		
		if(log.isDebugEnabled()){
		log.debug("getXPXItemBranchListService-InXML:"+getItemExtnListInXML.getString());
		}
		log.verbose("");
		log.verbose("getXPXItemBranchListService-InXML:"+getItemExtnListInXML.getString());
		
		YFCDocument getItemExtnListOutXML = null;
		Document tempDoc = this.api.executeFlow(env, "getXPXItemBranchListService", getItemExtnListInXML.getDocument());
		if(tempDoc != null) {
			getItemExtnListOutXML = YFCDocument.getDocumentFor(tempDoc);
			YFCElement getItemExtnListOutEle = getItemExtnListOutXML.getDocumentElement();
			YFCElement itemExtnEle = getItemExtnListOutEle.getChildElement("XPXItemExtn");
			if(itemExtnEle != null) {
				if(itemExtnEle.hasAttribute("InventoryIndicator")) {
					invIndicator = itemExtnEle.getAttribute("InventoryIndicator");
					if(YFCObject.isNull(invIndicator) || YFCObject.isVoid(invIndicator)) {
						callItemList = true;
					} else {
						/* changes made to fix Issue 1501  - Indicator 'I' has been added for Line type Stock.*/
						if("W".equalsIgnoreCase(invIndicator) || "I".equalsIgnoreCase(invIndicator)) {
							stockType = "STOCK";
						} else if("M".equalsIgnoreCase(invIndicator)) {
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
		
		if(callItemList) {
			YFCDocument getItemListOutXML = null;
			
			YFCDocument getItemListInXML = YFCDocument.getDocumentFor("<Item/>");
			YFCElement itemListEle = getItemListInXML.getDocumentElement();
			itemListEle.setAttribute("ItemID", itemId);
			
			if(log.isDebugEnabled()){
			log.debug("XPXGetItemList-InXML:"+getItemListInXML.getString());
			}
			log.verbose("");
			log.verbose("XPXGetItemList-InXML:"+getItemListInXML.getString());
			
			tempDoc = this.api.executeFlow(env, "XPXGetItemList", getItemListInXML.getDocument());
			if(tempDoc != null) {
				getItemListOutXML = YFCDocument.getDocumentFor(tempDoc);
				YFCElement _itemListEle = getItemListOutXML.getDocumentElement();
				YFCElement itemEle = _itemListEle.getChildElement("Item");
				if(itemEle != null) {
					stockType = "DIRECT";
				} else {
					if(lineType.equalsIgnoreCase("M") || lineType.equalsIgnoreCase("C")) {
						stockType = "STOCK";
					}
				}
			}
		}
		
		log.verbose("");
		log.verbose("ExtnLineType:"+stockType);
		log.verbose("");
		log.verbose("*****************************************************************************************************");
		
		if(log.isDebugEnabled()){
		log.debug("ExtnLineType:"+stockType);
		
		log.debug("*****************************************************************************************************");
		}
		return stockType;
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
		Document tempDoc = this.api.executeFlow(env, "getXrefList", getItemXRefListInXML.getDocument());
		if(tempDoc != null) {
			getItemXRefListOutXML = YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("getXrefList Service Returned NULL!");
		}
		
		YFCElement getItemXRefListOutEle = getItemXRefListOutXML.getDocumentElement();
		YFCElement getItemXRefEle = getItemXRefListOutEle.getChildElement("XPXItemcustXref");
		if(getItemXRefEle.hasAttribute("LegacyItemNumber")) {
			legacyCustNo = getItemXRefEle.getAttribute("LegacyItemNumber");
			if(YFCObject.isNull(legacyCustNo) || YFCObject.isVoid(legacyCustNo)) {
				throw new Exception("Attribute XPXItemcustXref/@LegacyItemNumber Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute LegacyItemNumber Not Available in getXrefList Template!");
		}
		
		return legacyCustNo;
		
	}
	
	
	/**
	 * 	Creates Fulfillment Order.
	 * 
	 *  @param env Environment variable.
	 *  @param rootEle Input xml from legacy system 
	 *  @param cOrderEle Element contains the customer information.	                         
	 *	@return tempDoc Output document of the Create order API. 	                                               
	 */
	private YFCDocument createFulfillmentOrder(YFSEnvironment env, YFCElement rootEle, YFCElement cOrderEle, YFCElement inXMLEle) throws Exception {
		
		rootEle.setAttribute("Action", "CREATE");
		if(rootEle.hasAttribute("OrderHeaderKey")) {
			rootEle.removeAttribute("OrderHeaderKey");
		}
		if(rootEle.hasAttribute("APIName")) {
			rootEle.removeAttribute("APIName");
		}
		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		if(rootExtnEle != null) {
			rootExtnEle.setAttribute("ExtnLegacyOrderNo", this.getLegacyOrderNo(inXMLEle));
			rootExtnEle.setAttribute("ExtnGenerationNo", this.getGenerationNo(inXMLEle));
		} else {
			throw new Exception("Order/Extn Element Not Available in Incoming Legacy Message!");
		}
		
		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if(rootOrdLinesEle != null) {
			YFCIterable yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement rootOrdLineEle = (YFCElement)yfcItr.next();
				rootOrdLineEle.setAttribute("Action", "CREATE");
				if(rootOrdLineEle.hasAttribute("OrderLineKey")) {
					rootOrdLineEle.removeAttribute("OrderLineKey");
				}
				
				String webLineNo = null;
				YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
				if(rootOrdLineExtnEle != null) {
					if(rootOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
						webLineNo = rootOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
						if(YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
							throw new Exception("Extn/@ExtnWebLineNumber Cannot be NULL or Void!");
						}
						String legacyLineNo = this.getLegacyLineNoForWebLineNo(webLineNo, inXMLEle);
						rootOrdLineExtnEle.setAttribute("ExtnLegacyLineNumber", legacyLineNo);
						YFCElement rootOrdLineTranQtyEle = rootOrdLineEle.getChildElement("OrderLineTranQuantity");
						if(rootOrdLineTranQtyEle != null) {
							rootOrdLineTranQtyEle.setAttribute("OrderedQty", this.getOrderedQtyForWebLineNo(webLineNo, legacyLineNo, inXMLEle));
						}
					} else {
						throw new Exception("Attribute Extn/@ExtnWebLineNumber Not Available in Incoming Legacy Message!");
					}
					
					// Logic to set the order type. ie., STOCK or DIRECT
					if(rootOrdLineExtnEle.hasAttribute("ExtnLineType")) {
						String extnLineType = rootOrdLineExtnEle.getAttribute("ExtnLineType");
						if(YFCObject.isNull(extnLineType) || YFCObject.isVoid(extnLineType)) {
							throw new Exception("ExtnLineType Cannot be NULL or Void!");
						}
						if(extnLineType.equalsIgnoreCase("DIRECT")) {
							rootEle.setAttribute("OrderType", "DIRECT_ORDER");
						} else if(extnLineType.equalsIgnoreCase("STOCK")) {
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
				
				// Mapping the customer order details with fulfillment order details.
				YFCElement chainedFromNode = rootEle.getOwnerDocument().createElement("ChainedFrom");
				rootOrdLineEle.appendChild(chainedFromNode);
				
				String custOrdHeaderKey = cOrderEle.getAttribute("OrderHeaderKey");
				if(!YFCObject.isVoid(custOrdHeaderKey) && !YFCObject.isNull(custOrdHeaderKey)) {
					chainedFromNode.setAttribute("OrderHeaderKey", custOrdHeaderKey);
				} else {
					throw new Exception("OrderHeaderKey Cannot be NULL or Void!");
				}
				
				// To get the corresponding customer order line key based on web line number.
				String custOrdLineKey = getOrderLineKeyForWebLineNo(webLineNo, null, cOrderEle);
				if(!YFCObject.isVoid(custOrdLineKey) && !YFCObject.isNull(custOrdLineKey)) {
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
		log.debug("XPXCreateOrder_FO-InXML:"+rootEle.getString());
		}
		log.verbose("");
		log.verbose("XPXCreateOrder_FO-InXML:"+rootEle.getString());
		// To create fulfillment order.
		Document tempDoc = this.api.executeFlow(env, "XPXCreateOrder", rootEle.getOwnerDocument().getDocument());
		if(tempDoc != null) {
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
		String[] sortAttr = { "MaxOrderStatus" };
		
		String fOrdHdrKey = fOrderEle.getAttribute("OrderHeaderKey");
		if(YFCObject.isNull(fOrdHdrKey) || YFCObject.isVoid(fOrdHdrKey)) {
			throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
		}
		
		String tranId0 = null;
		if(this._prop != null) {
			tranId0 = this._prop.getProperty("XPX_CHNED_ORD_STATUS_TXN");
			if(YFCObject.isNull(tranId0) || YFCObject.isVoid(tranId0)) {
				throw new Exception("TransactionID Not Configured in API Arguments!");
			}
		} else {
			throw new Exception("Arguments Not Configured For API-XPXPerformLegacyOrderUpdateAPI!");
		}
		
		String tranId1 = null;
		if(this._prop != null) {
			tranId1 = this._prop.getProperty("XPX_CHNG_ORD_STATUS_TXN");
			if(YFCObject.isNull(tranId1) || YFCObject.isVoid(tranId1)) {
				throw new Exception("TranstionID Not Configured in API Arguments!");
			}
		} else {
			throw new Exception("Arguments Not Configured For API-XPXPerformLegacyOrderUpdateAPI!");
		}
		
		if(hpc.equalsIgnoreCase("A")) {
			if(cAndfOrderEle != null) {
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
		
		YFCIterable yfcItr = cAndfOrderEle.getChildren("Order");
		while(yfcItr.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr.next();
			if(ordEle.hasAttribute("OrderType")) {
				String ordType = ordEle.getAttribute("OrderType");
				if(YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void!");
				}
				
				String ordHdrKey = null;
				if(ordEle.hasAttribute("OrderHeaderKey")) {
					ordHdrKey = ordEle.getAttribute("OrderHeaderKey");
					if(YFCObject.isNull(ordHdrKey) || YFCObject.isVoid(ordHdrKey)) {
						throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute OrderHeaderKey Not Available in getOrderList Template!");
				}
				
				String maxOrdStatus = null;
				if(ordEle.hasAttribute("MaxOrderStatus")) {
					maxOrdStatus = ordEle.getAttribute("MaxOrderStatus");
					if(YFCObject.isNull(maxOrdStatus) || YFCObject.isVoid(maxOrdStatus)) {
						throw new Exception("Attribute MaxOrderStatus Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute MaxOrderStatus Not Available in getOrderList Template!");
				}
				if(ordType.equalsIgnoreCase("Customer")) {
					chngcOrdStatusEle = getChngOrdStatusInXML(ordHdrKey, tranId0, "1100.0100");
				} else {
					YFCElement chngfOrdStatusEle = getChngOrdStatusInXML(ordHdrKey, tranId1, "1100.0100");
					chngfOrdStatusList1.add(chngfOrdStatusEle.getString());
					
					if(fOrdHdrKey.equalsIgnoreCase(ordHdrKey)) {
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
		log.debug("ChangeCOStatus-InXML:"+chngcOrdStatusEle.getString());
		}
		// To call changeOrderStatus API.
		Document tempDoc = this.api.executeFlow(env, "XPXChangeOrderStatus", chngcOrdStatusEle.getOwnerDocument().getDocument());
		if(tempDoc != null) {
			YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("Service XPXChangeOrderStatus Failed!");
		}
		
		Iterator itr = chngfOrdStatusList1.iterator();
		while(itr.hasNext()) {
			String chngfOrdStatusStr = (String)itr.next();
			YFCElement chngfOrdStatusEle = YFCDocument.getDocumentFor(chngfOrdStatusStr).getDocumentElement();
			
			if(log.isDebugEnabled()){
			log.debug("ChangeFOStatus-InXML:"+chngfOrdStatusEle.getString());
			}
			// To call changeOrderStatus API.
			tempDoc = this.api.executeFlow(env, "XPXChangeOrderStatus", chngfOrdStatusEle.getOwnerDocument().getDocument());
			if(tempDoc != null) {
				YFCDocument.getDocumentFor(tempDoc);
			} else {
				throw new Exception("Service XPXChangeOrderStatus Failed!");
			}
		}
		
		itr = chngfOrdStatusList2.descendingIterator();
		while(itr.hasNext()) {
			String chngfOrdStatusStr = (String)itr.next();
			YFCElement chngfOrdStatusEle = YFCDocument.getDocumentFor(chngfOrdStatusStr).getDocumentElement();
			
			if(log.isDebugEnabled()){
			log.debug("ChangeFOStatus-InXML:"+chngfOrdStatusEle.getString());
			}
			// To call changeOrderStatus API.
			tempDoc = this.api.executeFlow(env, "XPXChangeOrderStatus", chngfOrdStatusEle.getOwnerDocument().getDocument());
			if(tempDoc != null) {
				YFCDocument.getDocumentFor(tempDoc);
			} else {
				throw new Exception("Service XPXChangeOrderStatus Failed!");
			}
		}
	}
	
	private String getOrderHeaderKeyForWebLineNo(String webLineNo, String legacyLineNo, YFCElement ordEle) throws Exception {
		
		if(YFCObject.isNull(legacyLineNo) || YFCObject.isVoid(legacyLineNo)) {
			legacyLineNo = "DUMMY";
		}
		
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						String _webLineNo = null;
						String _legacyLineNo = null;
						if(extnEle.hasAttribute("ExtnLegacyLineNumber")) {
							_legacyLineNo = extnEle.getAttribute("ExtnLegacyLineNumber");
							if(YFCObject.isNull(_legacyLineNo) || YFCObject.isVoid(_legacyLineNo)) {
								_legacyLineNo = "DUMMY";
							}
						} else {
							_legacyLineNo = "DUMMY";
						}
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							if(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)) {
								
								if(log.isDebugEnabled()){
								log.debug("webLineNo:"+webLineNo);
								log.debug("_webLineNo:"+_webLineNo);
								log.debug("legacyLineNo:"+legacyLineNo);
								log.debug("_legacyLineNo:"+_legacyLineNo);
								log.debug("OrderHeaderKeyCheck_Boolean:"+(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)));
								}
								
								log.verbose("");
								log.verbose("webLineNo:"+webLineNo);
								log.verbose("_webLineNo:"+_webLineNo);
								log.verbose("legacyLineNo:"+legacyLineNo);
								log.verbose("_legacyLineNo:"+_legacyLineNo);
								log.verbose("OrderHeaderKeyCheck_Boolean:"+(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)));
								log.verbose("");
								
								if(ordLineEle.hasAttribute("OrderHeaderKey")) {
									String _ordHeaderKey = ordLineEle.getAttribute("OrderHeaderKey");
									if(YFCObject.isNull(_ordHeaderKey) || YFCObject.isVoid(_ordHeaderKey)) {
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
		if(ordExtnEle != null) {
			String genNo = ordExtnEle.getAttribute("ExtnGenerationNo");
			if(YFCObject.isNull(genNo) || YFCObject.isVoid(genNo)) {
				throw new Exception("Attribute ExtnGenerationNo Cannot be NULL or Void!");
			}
			return genNo;
		} else {
			throw new Exception("Attribute ExtnGenerationNo Not Available In Incoming Legacy Message!");
		}
	}
	
	private String getLegacyOrderNo(YFCElement ordEle) throws Exception {
		YFCElement ordExtnEle = ordEle.getChildElement("Extn");
		if(ordExtnEle != null) {
			String legacyOrdNo = ordExtnEle.getAttribute("ExtnLegacyOrderNo");
			if(YFCObject.isNull(legacyOrdNo) || YFCObject.isVoid(legacyOrdNo)) {
				throw new Exception("Attribute ExtnLegacyOrderNo Cannot be NULL or Void!");
			}
			return legacyOrdNo;
		} else {
			throw new Exception("Attribute ExtnLegacyOrderNo Not Available In Incoming Legacy Message!");
		}
	}
	
	private String getLegacyLineNoForWebLineNo(String webLineNo, YFCElement ordEle) throws Exception {
		
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
							String _webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtWebLineNumber Cannot be NULL or Void!");
							}
							if(_webLineNo.equalsIgnoreCase(webLineNo)) {
								if(extnEle.hasAttribute("ExtnLegacyLineNumber")) {
									String _extnLegacyLineNo = extnEle.getAttribute("ExtnLegacyLineNumber");
									if(YFCObject.isNull(_extnLegacyLineNo) || YFCObject.isVoid(_extnLegacyLineNo)) {
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

	private String getOrderedQtyForWebLineNo(String webLineNo, String legacyLineNo, YFCElement ordEle) throws Exception {
		
		if(YFCObject.isNull(legacyLineNo) || YFCObject.isVoid(legacyLineNo)) {
			legacyLineNo = "DUMMY";
		}
		
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						String _webLineNo = null;
						String _legacyLineNo = null;
						if(extnEle.hasAttribute("ExtnLegacyLineNumber")) {
							_legacyLineNo = extnEle.getAttribute("ExtnLegacyLineNumber");
							if(YFCObject.isNull(_legacyLineNo) || YFCObject.isVoid(_legacyLineNo)) {
								_legacyLineNo = "DUMMY";
							}
						} else {
							_legacyLineNo = "DUMMY";
						}
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtWebLineNumber Cannot be NULL or Void!");
							}
							if(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)) {
								YFCElement ordLineTranQtyEle = ordLineEle.getChildElement("OrderLineTranQuantity");
								if(ordLineTranQtyEle != null) {
									if(ordLineTranQtyEle.hasAttribute("OrderedQty")) {
										String _ordQty = ordLineTranQtyEle.getAttribute("OrderedQty");
										if(YFCObject.isNull(_ordQty) || YFCObject.isVoid(_ordQty)) {
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
		
	private String getChainedFromOrderLineKey(String ordLineKey, YFCElement ordEle) throws Exception {
		
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					String _ordLineKey = null;
					if(ordLineEle.hasAttribute("OrderLineKey")) {
						_ordLineKey = ordLineEle.getAttribute("OrderLineKey");
						if(YFCObject.isNull(_ordLineKey) || YFCObject.isVoid(_ordLineKey)) {
							throw new Exception("Attribute OrderLineKey Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute OrderLineKey Not Available in the OrderList Template!");
					}
					
					String chnFromOrdLineKey = null;
					if(_ordLineKey.equalsIgnoreCase(ordLineKey)) {
						if(ordLineEle.hasAttribute("ChainedFromOrderLineKey")) {
							chnFromOrdLineKey = ordLineEle.getAttribute("ChainedFromOrderLineKey");
							if(YFCObject.isNull(chnFromOrdLineKey) || YFCObject.isVoid(chnFromOrdLineKey)) {
								throw new Exception("Attribute ChainedFromOrderLineKey Cannot be NULL or Void!");
							}
							return chnFromOrdLineKey;
						} else {
							throw new Exception("Attribute ChainedFromOrderLineKey Not Available in the OrderList Template!");
						}
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
	 * 	Gets the customer order line key to chain with the fulfillment order based on web line number.
	 * 
	 * Logic : Customer order xml is iterated and matched with the web line number & Legacy line number in the input xml(legacy xml). 
	 * Customer order line key has been returned when there is a match.
	 * 
	 *  @param webLineNo Web Line number.
	 *  @param legacyLineNo Legacy Line Number.
	 *  @param ordEle Element contains the customer information.	                         
	 *	@return _ordLineKey Customer Order line key which needs to be chained with the fulfillment order. 	                                               
	 */
	
	private String getOrderLineKeyForWebLineNo(String webLineNo, String legacyLineNo, YFCElement ordEle) throws Exception {
		
		if(YFCObject.isNull(legacyLineNo) || YFCObject.isVoid(legacyLineNo)) {
			legacyLineNo = "DUMMY";
		}
		
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						String _webLineNo = null;
						String _legacyLineNo = null;
						if(extnEle.hasAttribute("ExtnLegacyLineNumber")) {
							_legacyLineNo = extnEle.getAttribute("ExtnLegacyLineNumber");
							if(YFCObject.isNull(_legacyLineNo) || YFCObject.isVoid(_legacyLineNo)) {
								_legacyLineNo = "DUMMY";
							}
						} else {
							_legacyLineNo = "DUMMY";
						}
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtWebLineNumber Cannot be NULL or Void!");
							}
							if(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)) {
								
								if(log.isDebugEnabled()){
								log.debug("webLineNo:"+webLineNo);
								log.debug("_webLineNo:"+_webLineNo);
								log.debug("legacyLineNo:"+legacyLineNo);
								log.debug("_legacyLineNo:"+_legacyLineNo);
								log.debug("OrderLineKeyCheck_Boolean:"+(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)));
								}
								
								log.verbose("");
								log.verbose("webLineNo:"+webLineNo);
								log.verbose("_webLineNo:"+_webLineNo);
								log.verbose("legacyLineNo:"+legacyLineNo);
								log.verbose("_legacyLineNo:"+_legacyLineNo);
								log.verbose("OrderLineKeyCheck_Boolean:"+(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)));
								log.verbose("");
								
								if(ordLineEle.hasAttribute("OrderLineKey")) {
									String _ordLineKey = ordLineEle.getAttribute("OrderLineKey");
									if(YFCObject.isNull(_ordLineKey) || YFCObject.isVoid(_ordLineKey)) {
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
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						String _webLineNo = null;
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtWebLineNumber Cannot be NULL or Void!");
							}
							
							if(_webLineNo.equalsIgnoreCase(webLineNo)) {
								
								if(log.isDebugEnabled()){
								log.debug("webLineNo:"+webLineNo);
								log.debug("_webLineNo:"+_webLineNo);
								log.debug("OrderLineKeyCheck_Boolean:"+(_webLineNo.equalsIgnoreCase(webLineNo)));
								}
								
								log.verbose("");
								log.verbose("webLineNo:"+webLineNo);
								log.verbose("_webLineNo:"+_webLineNo);
								log.verbose("OrderLineKeyCheck_Boolean:"+(_webLineNo.equalsIgnoreCase(webLineNo)));
								log.verbose("");
								
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

	private YFCElement getOrderLineForWebLineNo(String webLineNo, String legacyLineNo, YFCElement ordEle) throws Exception {
		
		if(YFCObject.isNull(legacyLineNo) || YFCObject.isVoid(legacyLineNo)) {
			legacyLineNo = "DUMMY";
		}
		
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						String _webLineNo = null;
						String _legacyLineNo = null;
						if(extnEle.hasAttribute("ExtnLegacyLineNumber")) {
							_legacyLineNo = extnEle.getAttribute("ExtnLegacyLineNumber");
							if(YFCObject.isNull(_legacyLineNo) || YFCObject.isVoid(_legacyLineNo)) {
								_legacyLineNo = "DUMMY";
							}
						} else {
							_legacyLineNo = "DUMMY";
						}
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtWebLineNumber Cannot be NULL or Void!");
							}
							
							if(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)) {
								
								if(log.isDebugEnabled()){
								log.debug("webLineNo:"+webLineNo);
								log.debug("_webLineNo:"+_webLineNo);
								log.debug("legacyLineNo:"+legacyLineNo);
								log.debug("_legacyLineNo:"+_legacyLineNo);
								log.debug("OrderLineKeyCheck_Boolean:"+(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)));
								}
								
								log.verbose("");
								log.verbose("webLineNo:"+webLineNo);
								log.verbose("_webLineNo:"+_webLineNo);
								log.verbose("legacyLineNo:"+legacyLineNo);
								log.verbose("_legacyLineNo:"+_legacyLineNo);
								log.verbose("OrderLineKeyCheck_Boolean:"+(_webLineNo.equalsIgnoreCase(webLineNo) && _legacyLineNo.equalsIgnoreCase(legacyLineNo)));
								log.verbose("");
								
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
		Map tempMap = ordLineEle.getAttributes();
		Set keySet = tempMap.keySet();
		Iterator itr = keySet.iterator();
		while(itr.hasNext()) {
			String attr = (String)itr.next();
			if(attr.equalsIgnoreCase("OrderLineKey")) {
				ordLineKey = ordLineEle.getAttribute("OrderLineKey");
				if(YFCObject.isNull(ordLineKey) || YFCObject.isVoid(ordLineKey)) {
					throw new Exception("OrderLineKey Cannot be NULL or Void!");
				}
			}
			ordLineEle.removeAttribute(attr);
		}
		ordLineEle.setAttribute("Action", "CANCEL");
		ordLineEle.setAttribute("OrderLineKey", ordLineKey);
		ordLineEle.setAttribute("IsNewLine", "N");
		
		YFCIterable yfcItr = ordLineEle.getChildren();
		while(yfcItr.hasNext()) {
			YFCElement yfcElement = (YFCElement)yfcItr.next();
			if(yfcElement.getNodeName().equalsIgnoreCase("Extn")) {
				String webLineNo = null;
				tempMap = yfcElement.getAttributes();
				keySet = tempMap.keySet();
				itr = keySet.iterator();
				while(itr.hasNext()) {
					String attr = (String)itr.next();
					if(attr.equalsIgnoreCase("ExtnWebLineNumber")) {
						webLineNo = yfcElement.getAttribute("ExtnWebLineNumber");
						if(YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
							throw new Exception("ExtnWebLineNumber Cannot be NULL or Void!");
						}
					}
					yfcElement.removeAttribute(attr);
				}
				yfcElement.setAttribute("ExtnWebLineNumber", webLineNo);
			}
			if(!yfcElement.getNodeName().equalsIgnoreCase("OrderLineTranQuantity") && !yfcElement.getNodeName().equalsIgnoreCase("Extn")) {
				ordLineEle.removeChild(yfcElement);
			}
		}
		
		YFCElement ordLineTranQty = ordLineEle.getChildElement("OrderLineTranQuantity");
		ordLineTranQty.setAttribute("OrderedQty", "0.0");
		
		return ordLineEle;
	}
	
	private List getWebLineNosToArray(YFCElement rootEle) throws Exception {
		
		List weblineNosArray = new ArrayList();
		YFCElement ordLinesEle = rootEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
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
	
	private List getLegacyLineNosToArray(YFCElement rootEle) throws Exception {
		
		List legacyLineNos = new ArrayList();
		YFCElement ordLinesEle = rootEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						if(extnEle.hasAttribute("ExtnLegacyLineNumber")) {
							legacyLineNos.add(extnEle.getAttribute("ExtnLegacyLineNumber"));
						} else {
							throw new Exception("Attribute ExtnLegacyLineNumber Not Available!");
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
		
		return legacyLineNos;
	}

	private YFCElement getFulfillmentOrderEle(String hpc, String fOrdHeaderKey, YFCElement cAndfOrderEle) throws Exception {
		
		YFCIterable yfcItr = cAndfOrderEle.getChildren("Order");
		while(yfcItr.hasNext()) {
			
			String ordType = null;
			String ordHeaderKey = null;
			
			YFCElement ordEle = (YFCElement) yfcItr.next();
			if(ordEle.hasAttribute("OrderType")) {
				ordType = ordEle.getAttribute("OrderType");
				if(YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute OrderType Not Available in OrderList Template!");
			}
			
			if(ordEle.hasAttribute("OrderHeaderKey")) {
				ordHeaderKey = ordEle.getAttribute("OrderHeaderKey");
				if(YFCObject.isNull(ordHeaderKey) || YFCObject.isVoid(ordHeaderKey)) {
					throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute OrderHeaderKey Not Available in OrderList Template!");
			}
			
			if((!this.isCancelledOrder(ordEle) || hpc.equalsIgnoreCase("C")) && 
					!ordType.equalsIgnoreCase("Customer") && ordHeaderKey.equalsIgnoreCase(fOrdHeaderKey)) {
				return ordEle;
			}
		}
		
		return null;
	}
	
	private YFCElement getFulfillmentOrderEle(String hpc, YFCElement rootEle, YFCElement cAndfOrderEle) throws Exception {
		
		String isOrderEdit = "N";
		if(rootEle.hasAttribute("IsOrderEdit")) {
			isOrderEdit = rootEle.getAttribute("IsOrderEdit");
			if(YFCObject.isNull(isOrderEdit) || YFCObject.isVoid(isOrderEdit)) {
				isOrderEdit = "N";
			}
		}
		
		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		String extnWebConfNum = null;
		if(rootExtnEle.hasAttribute("ExtnWebConfNum")) {
			extnWebConfNum = rootExtnEle.getAttribute("ExtnWebConfNum");
			if(YFCObject.isNull(extnWebConfNum) || YFCObject.isVoid(extnWebConfNum)) {
				throw new Exception("Attribute ExtnWebConfNum Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute ExtnWebConfNum Not Available in Incoming Legacy Message!"); 
		}
		
		String extnLegacyOrdNum = null;
		if(rootExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
			extnLegacyOrdNum = rootExtnEle.getAttribute("ExtnLegacyOrderNo");
			if(YFCObject.isNull(extnLegacyOrdNum) || YFCObject.isVoid(extnLegacyOrdNum)) {
				throw new Exception("Attribute ExtnLegacyOrderNo Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute ExtnLegacyOrderNo Not Available in Incoming Legacy Message!");
		}
		
		String extnGenNum = null;
		if(rootExtnEle.hasAttribute("ExtnGenerationNo")) {
			extnGenNum = rootExtnEle.getAttribute("ExtnGenerationNo");
			if(YFCObject.isNull(extnGenNum) || YFCObject.isVoid(extnGenNum)) {
				throw new Exception("Attribute ExtnGenerationNo Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute ExtnGenerationNo Not Available in Incoming Legacy Message!");
		}
		
		YFCIterable yfcItr = cAndfOrderEle.getChildren("Order");
		while(yfcItr.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr.next();
			if(ordEle.hasAttribute("OrderType")) {
				String webConfNum = null;
				String legacyOrdNum = null;
				String genNum = null;
				String ordType = ordEle.getAttribute("OrderType");
				if(YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void!");
				}
				YFCElement extnEle = ordEle.getChildElement("Extn");
				if(extnEle != null) {
					if(extnEle.hasAttribute("ExtnWebConfNum")) {
						webConfNum = extnEle.getAttribute("ExtnWebConfNum");
						if(YFCObject.isNull(webConfNum) || YFCObject.isVoid(webConfNum)) {
							throw new Exception("Attribute ExtnWebConfNum Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute ExtnWebConfNum Not Available in OrderList Template!");
					}
					
					if(extnEle.hasAttribute("ExtnLegacyOrderNo")) {
						legacyOrdNum = extnEle.getAttribute("ExtnLegacyOrderNo");
						if(YFCObject.isNull(legacyOrdNum) || YFCObject.isVoid(legacyOrdNum)) {
							if(!ordType.equalsIgnoreCase("Customer")) {
								throw new Exception("Attribute ExtnLegacyOrderNo Cannot be NULL or Void!");
							}
						}
					} else {
						if(!ordType.equalsIgnoreCase("Customer")) {
							throw new Exception("Attribute ExtnLegacyOrderNo Not Available in OrderList Template!");
						}
					}
					
					if(extnEle.hasAttribute("ExtnGenerationNo")) {
						genNum = extnEle.getAttribute("ExtnGenerationNo");
						if(YFCObject.isNull(genNum) || YFCObject.isVoid(genNum)) {
							if(!ordType.equalsIgnoreCase("Customer")) {
								throw new Exception("Attribute ExtnGenerationNo Cannot be NULL or Void!");
							}
						}
					} else {
						if(!ordType.equalsIgnoreCase("Customer")) {
							throw new Exception("Attribute ExtnGenerationNo Not Available in OrderList Template!");
						}
					}
				} else {
					throw new Exception("OrderLine/Extn Element Not Available in OrderList Template!");
				}
				
				if(!ordType.equalsIgnoreCase("Customer") && (webConfNum.equalsIgnoreCase(extnWebConfNum)) && 
						(extnLegacyOrdNum.equalsIgnoreCase(legacyOrdNum)) && (genNum.equalsIgnoreCase(extnGenNum))) {
					if(isOrderEdit.equalsIgnoreCase("N")) {
						if(!this.isCancelledOrder(ordEle) || hpc.equalsIgnoreCase("C")) {
							return ordEle;
						}
					} else {
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
		if(rootEle.hasAttribute("IsOrderEdit")) {
			isOrderEdit = rootEle.getAttribute("IsOrderEdit");
			if(YFCObject.isNull(isOrderEdit) || YFCObject.isVoid(isOrderEdit)) {
				isOrderEdit = "N";
			}
		}
		
		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		String extnWebConfNum = null;
		if(rootExtnEle.hasAttribute("ExtnWebConfNum")) {
			extnWebConfNum = rootExtnEle.getAttribute("ExtnWebConfNum");
		} else {
			throw new Exception("Attribute ExtnWebConfNum Not Available in Incoming Legacy Message!"); 
		}
		
		YFCIterable yfcItr = cAndfOrderEle.getChildren("Order");
		while(yfcItr.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr.next();
			if(ordEle.hasAttribute("OrderType")) {
				String webConfNum = null;
				
				String ordType = ordEle.getAttribute("OrderType");
				if(YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void!");
				}
				
				YFCElement extnEle = ordEle.getChildElement("Extn");
				if(extnEle != null) {
					if(extnEle.hasAttribute("ExtnWebConfNum")) {
						webConfNum = extnEle.getAttribute("ExtnWebConfNum");
					} else {
						throw new Exception("Attribute ExtnWebConfNum Not Available in OrderList Template!");
					}
				} else {
					throw new Exception("OrderLine/Extn Element Not Available in OrderList Template!");
				}
				if(ordType.equalsIgnoreCase("Customer") && (webConfNum.equalsIgnoreCase(extnWebConfNum))) {
					if(isOrderEdit.equalsIgnoreCase("N")) {
						if(!this.isCancelledOrder(ordEle) || hpc.equalsIgnoreCase("C")) {
							retOrdEle = ordEle;
						}
					} else {
						retOrdEle = ordEle;
					}
				} else {
					// Stamp Extended Price Info
					this.getOrderExtendedPriceInfo(rootEle, ordEle);
				}
			} else {
				throw new Exception("Attribute OrderType Not Available in OrderList Template!");
			}
		}
		return retOrdEle;
	}
	
	private void getOrderExtendedPriceInfo(YFCElement rootEle, YFCElement ordEle) throws Exception {
		
		String webConfNum = null;
		String legacyOrdNum = null;
		String genNo = null;
		String extnWebConfNum = null;
		String extnLegacyOrdNum = null;
		String extnGenNo = null;
		
		YFCElement extnEle = null;
		YFCElement ordExtnEle = null;
		
		if(ordEle != null) {
			ordExtnEle = ordEle.getChildElement("Extn");
			if(ordExtnEle != null) {
				if(ordExtnEle.hasAttribute("ExtnWebConfNum")) {
					webConfNum = ordExtnEle.getAttribute("ExtnWebConfNum");
					if(YFCObject.isNull(webConfNum) || YFCObject.isVoid(webConfNum)) {
						throw new Exception("Attribute ExtnWebConfNum Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute ExtnWebConfNum Not Available in getOrderList Template!"); 
				}
				
				if(ordExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
					legacyOrdNum = ordExtnEle.getAttribute("ExtnLegacyOrderNo");
					if(YFCObject.isNull(legacyOrdNum) || YFCObject.isVoid(legacyOrdNum)) {
						throw new Exception("Attribute ExtnLegacyOrderNo Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute ExtnLegacyOrderNo Not Available in getOrderList Template!");
				}
				
				if(ordExtnEle.hasAttribute("ExtnGenerationNo")) {
					genNo = ordExtnEle.getAttribute("ExtnGenerationNo");
					if(YFCObject.isNull(genNo) || YFCObject.isVoid(genNo)) {
						throw new Exception("Attribute ExtnGenerationNo Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute ExtnGenerationNo Not Available in getOrderList Template!");
				}

			}
		}
		
		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		if(rootExtnEle != null) {
			if(rootExtnEle.hasAttribute("ExtnWebConfNum")) {
				extnWebConfNum = rootExtnEle.getAttribute("ExtnWebConfNum");
				if(YFCObject.isNull(extnWebConfNum) || YFCObject.isVoid(extnWebConfNum)) {
					throw new Exception("Attribute ExtnWebConfNum Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ExtnWebConfNum Not Available in Incoming Legacy Message!"); 
			}
			
			if(rootExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
				extnLegacyOrdNum = rootExtnEle.getAttribute("ExtnLegacyOrderNo");
				if(YFCObject.isNull(extnLegacyOrdNum) || YFCObject.isVoid(extnLegacyOrdNum)) {
					throw new Exception("Attribute ExtnLegacyOrderNo Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ExtnLegacyOrderNo Not Available in Incoming Legacy Message!");
			}
			
			if(rootExtnEle.hasAttribute("ExtnGenerationNo")) {
				extnGenNo = rootExtnEle.getAttribute("ExtnGenerationNo");
				if(YFCObject.isNull(extnGenNo) || YFCObject.isVoid(extnGenNo)) {
					throw new Exception("Attribute ExtnGenerationNo Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ExtnGenerationNo Not Available in Incoming Legacy Message!");
			}
		}
		
		boolean toBeChangedOrder = false;
		if((ordEle == null) || (webConfNum.equalsIgnoreCase(extnWebConfNum) && legacyOrdNum.equalsIgnoreCase(extnLegacyOrdNum) && genNo.equalsIgnoreCase(extnGenNo))) {
			toBeChangedOrder = true;
			extnEle = rootExtnEle;
		} else {
			extnEle = ordExtnEle;
		}
		
		String extnLegOrdTotAdj = null;
		if(extnEle.hasAttribute("ExtnLegTotOrderAdjustments")) {
			extnLegOrdTotAdj = extnEle.getAttribute("ExtnLegTotOrderAdjustments");
			if(YFCObject.isNull(extnLegOrdTotAdj) || YFCObject.isVoid(extnLegOrdTotAdj)) {
				extnLegOrdTotAdj = "0.0";
				extnEle.setAttribute("ExtnLegTotOrderAdjustments", extnLegOrdTotAdj);
			}
		} else {
			extnLegOrdTotAdj = "0.0";
			extnEle.setAttribute("ExtnLegTotOrderAdjustments", extnLegOrdTotAdj);
		}
		if(rootEle.hasAttribute("LegTotOrderAdjustments")) {
			String _legOrdTotAdj = rootEle.getAttribute("LegTotOrderAdjustments");
			if(YFCObject.isNull(_legOrdTotAdj) || YFCObject.isVoid(_legOrdTotAdj)) {
				rootEle.setAttribute("LegTotOrderAdjustments", extnLegOrdTotAdj);
			} else {
				double _dLegOrdTotAdj = Double.parseDouble(_legOrdTotAdj);
				double _dExtnLegOrdTotAdj = Double.parseDouble(extnLegOrdTotAdj);
				_dLegOrdTotAdj = _dLegOrdTotAdj + _dExtnLegOrdTotAdj;
				rootEle.setAttribute("LegTotOrderAdjustments", new Double(_dLegOrdTotAdj).toString());
			}
		} else {
			rootEle.setAttribute("LegTotOrderAdjustments", extnLegOrdTotAdj);
		}
		
		String extnTotOrdFreight = null;
		if(extnEle.hasAttribute("ExtnTotalOrderFreight")) {
			extnTotOrdFreight = extnEle.getAttribute("ExtnTotalOrderFreight");
			if(YFCObject.isNull(extnTotOrdFreight) || YFCObject.isVoid(extnTotOrdFreight)) {
				extnTotOrdFreight = "0.0";
				extnEle.setAttribute("ExtnTotalOrderFreight", extnTotOrdFreight);
			}
		} else {
			extnTotOrdFreight = "0.0";
			extnEle.setAttribute("ExtnTotalOrderFreight", extnTotOrdFreight);
		}
		
		String extnOrdTax = null;
		if(extnEle.hasAttribute("ExtnOrderTax")) {
			extnOrdTax = extnEle.getAttribute("ExtnOrderTax");
			if(YFCObject.isNull(extnOrdTax) || YFCObject.isVoid(extnOrdTax)) {
				extnOrdTax = "0.0";
				extnEle.setAttribute("ExtnOrderTax", extnOrdTax);
			}
		} else {
			extnOrdTax = "0.0";
			extnEle.setAttribute("ExtnOrderTax", extnOrdTax);
		}
		String extnTotOrdValue = null;
		if(extnEle.hasAttribute("ExtnTotalOrderValue")) {
			extnTotOrdValue = extnEle.getAttribute("ExtnTotalOrderValue");
			if(YFCObject.isNull(extnTotOrdValue) || YFCObject.isVoid(extnTotOrdValue)) {
				extnTotOrdValue = "0.0";
				extnEle.setAttribute("ExtnTotalOrderValue", extnTotOrdValue);
			}
		} else {
			extnTotOrdValue = "0.0";
			extnEle.setAttribute("ExtnTotalOrderValue", extnTotOrdValue);
		}
		double dExtnOrdTotWithoutTax = Double.parseDouble(extnTotOrdValue) - (Double.parseDouble(extnOrdTax) + Double.parseDouble(extnTotOrdFreight));
		
		if(rootEle.hasAttribute("TotOrdValWithoutTaxes")) {
			String _ordTotWithOutTax = rootEle.getAttribute("TotOrdValWithoutTaxes");
			if(YFCObject.isNull(_ordTotWithOutTax) || YFCObject.isVoid(_ordTotWithOutTax)) {
				rootEle.setAttribute("TotOrdValWithoutTaxes", new Double(dExtnOrdTotWithoutTax).toString());
			} else {
				double _dOrdTotWithOutTax = Double.parseDouble(_ordTotWithOutTax);
				_dOrdTotWithOutTax = _dOrdTotWithOutTax + dExtnOrdTotWithoutTax;
				rootEle.setAttribute("TotOrdValWithoutTaxes", new Double(_dOrdTotWithOutTax).toString());
			}
		} else {
			rootEle.setAttribute("TotOrdValWithoutTaxes", new Double(dExtnOrdTotWithoutTax).toString());
		}
		
		String subOrdTot = this.getOrderLineExtendedPriceInfo(rootEle, ordEle, toBeChangedOrder);
		if(rootEle.hasAttribute("OrderSubTotal")) {
			String _subOrdTot = rootEle.getAttribute("OrderSubTotal");
			if(YFCObject.isNull(_subOrdTot) || YFCObject.isVoid(_subOrdTot)) {
				rootEle.setAttribute("OrderSubTotal", subOrdTot);
			} else {
				double _dSubOrdTot = Double.parseDouble(_subOrdTot);
				double dSubOrdTot = Double.parseDouble(subOrdTot);
				_dSubOrdTot = _dSubOrdTot + dSubOrdTot;
				rootEle.setAttribute("OrderSubTotal", new Double(_dSubOrdTot).toString());
			}
		} else {
			rootEle.setAttribute("OrderSubTotal", subOrdTot);
		}
	}
	
	private String getOrderLineExtendedPriceInfo(YFCElement rootEle, YFCElement ordEle, boolean toBeChangedOrder) throws Exception {
		double dExtnOrdSubTotal = 0.0;
		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if(rootOrdLinesEle != null) {
			YFCIterable yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement rootOrdLineEle = (YFCElement)yfcItr.next();
				YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
				if(rootOrdLineExtnEle != null) {
					if(toBeChangedOrder) {
						String extnLegTotOrdLineAdj = null;
						if(rootOrdLineExtnEle.hasAttribute("ExtnLegOrderLineAdjustments")) {
							extnLegTotOrdLineAdj = rootOrdLineExtnEle.getAttribute("ExtnLegOrderLineAdjustments");
							if(YFCObject.isNull(extnLegTotOrdLineAdj) || YFCObject.isVoid(extnLegTotOrdLineAdj)) {
								extnLegTotOrdLineAdj = "0.0";
								rootOrdLineExtnEle.setAttribute("ExtnLegOrderLineAdjustments", extnLegTotOrdLineAdj);
							}
						} else {
							extnLegTotOrdLineAdj = "0.0";
							rootOrdLineExtnEle.setAttribute("ExtnLegOrderLineAdjustments", extnLegTotOrdLineAdj);
						}
						if(rootOrdLineEle.hasAttribute("LegOrderLineAdjustments")) {
							String _legTotOrdLineAdj = rootOrdLineEle.getAttribute("LegOrderLineAdjustments");
							if(YFCObject.isNull(_legTotOrdLineAdj) || YFCObject.isVoid(_legTotOrdLineAdj)) {
								rootOrdLineEle.setAttribute("LegOrderLineAdjustments", extnLegTotOrdLineAdj);
							} else {
								double dLegTotOrdLineAdj = Double.parseDouble(_legTotOrdLineAdj);
								double dExtnLegTotOrdLineAdj = Double.parseDouble(extnLegTotOrdLineAdj);
								dLegTotOrdLineAdj = dLegTotOrdLineAdj + dExtnLegTotOrdLineAdj;
								rootOrdLineEle.setAttribute("LegOrderLineAdjustments", new Double(dLegTotOrdLineAdj).toString());
							}
						} else {
							rootOrdLineEle.setAttribute("LegOrderLineAdjustments", extnLegTotOrdLineAdj);
						}
						
						String extnLineOrdTot = null;
						if(rootOrdLineExtnEle.hasAttribute("ExtnLineOrderedTotal")) {
							extnLineOrdTot = rootOrdLineExtnEle.getAttribute("ExtnLineOrderedTotal");
							if(YFCObject.isNull(extnLineOrdTot) || YFCObject.isVoid(extnLineOrdTot)) {
								extnLineOrdTot = "0.0";
								rootOrdLineExtnEle.setAttribute("ExtnLineOrderedTotal", extnLineOrdTot);
							}
						} else {
							extnLineOrdTot = "0.0";
							rootOrdLineExtnEle.setAttribute("ExtnLineOrderedTotal", extnLineOrdTot);
						}
						if(rootOrdLineEle.hasAttribute("ExtendedPrice")) {
							String extPrice = rootOrdLineEle.getAttribute("ExtendedPrice");
							if(YFCObject.isNull(extPrice) || YFCObject.isVoid(extPrice)) {
								rootOrdLineEle.setAttribute("ExtendedPrice", new Double((Double.parseDouble(extnLineOrdTot)+Double.parseDouble(extnLegTotOrdLineAdj))).toString());
							} else {
								double dExtPrice = Double.parseDouble(extPrice);
								dExtPrice = dExtPrice + Double.parseDouble(extnLineOrdTot)+Double.parseDouble(extnLegTotOrdLineAdj);
								rootOrdLineEle.setAttribute("ExtendedPrice", dExtPrice);
							}
						} else {
							rootOrdLineEle.setAttribute("ExtendedPrice", new Double((Double.parseDouble(extnLineOrdTot)+Double.parseDouble(extnLegTotOrdLineAdj))).toString());
						}
					} else {
						String extnWebLineNo = null;
						if(rootOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
							extnWebLineNo = rootOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(extnWebLineNo) || YFCObject.isVoid(extnWebLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in Incoming Legacy Message!");
						}
						
						String extnLegacyLineNo = null;
						if(rootOrdLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
							extnLegacyLineNo = rootOrdLineExtnEle.getAttribute("ExtnLegacyLineNumber");
							if(YFCObject.isNull(extnLegacyLineNo) || YFCObject.isVoid(extnLegacyLineNo)) {
								throw new Exception("Attribute ExtnLegacyLineNumber Cannot be NULL or Void!");
							}
						} else {
							throw new Exception("Attribute ExtnLegacyLineNumber Not Available in Incoming Legacy Message!");
						}
						YFCElement _ordLineEle = this.getOrderLineForWebLineNo(extnWebLineNo, ordEle);
						if(_ordLineEle != null) {
							YFCElement _ordLineExtnEle = _ordLineEle.getChildElement("Extn");
							if(_ordLineExtnEle != null) {
								String legTotOrdLineAdj = null;
								if(_ordLineExtnEle.hasAttribute("ExtnLegOrderLineAdjustments")) {
									legTotOrdLineAdj = _ordLineExtnEle.getAttribute("ExtnLegOrderLineAdjustments");
									if(YFCObject.isNull(legTotOrdLineAdj) || YFCObject.isVoid(legTotOrdLineAdj)) {
										legTotOrdLineAdj = "0.0";
										_ordLineExtnEle.setAttribute("ExtnLegOrderLineAdjustments", legTotOrdLineAdj);
									}
								} else {
									legTotOrdLineAdj = "0.0";
									_ordLineExtnEle.setAttribute("ExtnLegOrderLineAdjustments", legTotOrdLineAdj);
								}
								if(rootOrdLineEle.hasAttribute("LegOrderLineAdjustments")) {
									String _legTotOrdLineAdj = rootOrdLineEle.getAttribute("LegOrderLineAdjustments");
									if(YFCObject.isNull(_legTotOrdLineAdj) || YFCObject.isVoid(_legTotOrdLineAdj)) {
										rootOrdLineEle.setAttribute("LegOrderLineAdjustments", legTotOrdLineAdj);
									} else {
										double dLegTotOrdLineAdj = Double.parseDouble(_legTotOrdLineAdj);
										double dExtnLegTotOrdLineAdj = Double.parseDouble(legTotOrdLineAdj);
										dLegTotOrdLineAdj = dLegTotOrdLineAdj + dExtnLegTotOrdLineAdj;
										rootOrdLineEle.setAttribute("LegOrderLineAdjustments", new Double(dLegTotOrdLineAdj).toString());
									}
								} else {
									rootOrdLineEle.setAttribute("LegOrderLineAdjustments", legTotOrdLineAdj);
								}
								
								String lineOrdTot = null;
								if(_ordLineExtnEle.hasAttribute("ExtnLineOrderedTotal")) {
									lineOrdTot = _ordLineExtnEle.getAttribute("ExtnLineOrderedTotal");
									if(YFCObject.isNull(lineOrdTot) || YFCObject.isVoid(lineOrdTot)) {
										lineOrdTot = "0.0";
										_ordLineExtnEle.setAttribute("ExtnLineOrderedTotal", lineOrdTot);
									}
								} else {
									lineOrdTot = "0.0";
									_ordLineExtnEle.setAttribute("ExtnLineOrderedTotal", lineOrdTot);
								}
								if(rootOrdLineEle.hasAttribute("ExtendedPrice")) {
									String extPrice = rootOrdLineEle.getAttribute("ExtendedPrice");
									if(YFCObject.isNull(extPrice) || YFCObject.isVoid(extPrice)) {
										rootOrdLineEle.setAttribute("ExtendedPrice", new Double((Double.parseDouble(lineOrdTot)+Double.parseDouble(legTotOrdLineAdj))).toString());
									} else {
										double dExtPrice = Double.parseDouble(extPrice);
										dExtPrice = dExtPrice + Double.parseDouble(lineOrdTot)+Double.parseDouble(legTotOrdLineAdj);
										rootOrdLineEle.setAttribute("ExtendedPrice", dExtPrice);
									}
								} else {
									rootOrdLineEle.setAttribute("ExtendedPrice", new Double((Double.parseDouble(lineOrdTot)+Double.parseDouble(legTotOrdLineAdj))).toString());
								}
							}
						} else {
							String extnLegTotOrdLineAdj = null;
							if(rootOrdLineExtnEle.hasAttribute("ExtnLegOrderLineAdjustments")) {
								extnLegTotOrdLineAdj = rootOrdLineExtnEle.getAttribute("ExtnLegOrderLineAdjustments");
								if(YFCObject.isNull(extnLegTotOrdLineAdj) || YFCObject.isVoid(extnLegTotOrdLineAdj)) {
									extnLegTotOrdLineAdj = "0.0";
									rootOrdLineExtnEle.setAttribute("ExtnLegOrderLineAdjustments", extnLegTotOrdLineAdj);
								}
							} else {
								extnLegTotOrdLineAdj = "0.0";
								rootOrdLineExtnEle.setAttribute("ExtnLegOrderLineAdjustments", extnLegTotOrdLineAdj);
							}
							if(rootOrdLineEle.hasAttribute("LegOrderLineAdjustments")) {
								String _legTotOrdLineAdj = rootOrdLineEle.getAttribute("LegOrderLineAdjustments");
								if(YFCObject.isNull(_legTotOrdLineAdj) || YFCObject.isVoid(_legTotOrdLineAdj)) {
									rootOrdLineEle.setAttribute("LegOrderLineAdjustments", extnLegTotOrdLineAdj);
								} else {
									double dLegTotOrdLineAdj = Double.parseDouble(_legTotOrdLineAdj);
									double dExtnLegTotOrdLineAdj = Double.parseDouble(extnLegTotOrdLineAdj);
									dLegTotOrdLineAdj = dLegTotOrdLineAdj + dExtnLegTotOrdLineAdj;
									rootOrdLineEle.setAttribute("LegOrderLineAdjustments", new Double(dLegTotOrdLineAdj).toString());
								}
							} else {
								rootOrdLineEle.setAttribute("LegOrderLineAdjustments", extnLegTotOrdLineAdj);
							}
							
							String extnLineOrdTot = null;
							if(rootOrdLineExtnEle.hasAttribute("ExtnLineOrderedTotal")) {
								extnLineOrdTot = rootOrdLineExtnEle.getAttribute("ExtnLineOrderedTotal");
								if(YFCObject.isNull(extnLineOrdTot) || YFCObject.isVoid(extnLineOrdTot)) {
									extnLineOrdTot = "0.0";
									rootOrdLineExtnEle.setAttribute("ExtnLineOrderedTotal", extnLineOrdTot);
								}
							} else {
								extnLineOrdTot = "0.0";
								rootOrdLineExtnEle.setAttribute("ExtnLineOrderedTotal", extnLineOrdTot);
							}
							if(rootOrdLineEle.hasAttribute("ExtendedPrice")) {
								String extPrice = rootOrdLineEle.getAttribute("ExtendedPrice");
								if(YFCObject.isNull(extPrice) || YFCObject.isVoid(extPrice)) {
									rootOrdLineEle.setAttribute("ExtendedPrice", new Double((Double.parseDouble(extnLineOrdTot)+Double.parseDouble(extnLegTotOrdLineAdj))).toString());
								} else {
									double dExtPrice = Double.parseDouble(extPrice);
									dExtPrice = dExtPrice + Double.parseDouble(extnLineOrdTot)+Double.parseDouble(extnLegTotOrdLineAdj);
									rootOrdLineEle.setAttribute("ExtendedPrice", dExtPrice);
								}
							} else {
								rootOrdLineEle.setAttribute("ExtendedPrice", new Double((Double.parseDouble(extnLineOrdTot)+Double.parseDouble(extnLegTotOrdLineAdj))).toString());
							}
						}
					}
					String extnLineOrdTotal = null;
					if(rootOrdLineExtnEle.hasAttribute("ExtnLineOrderedTotal")) {
						extnLineOrdTotal = rootOrdLineExtnEle.getAttribute("ExtnLineOrderedTotal");
						if(YFCObject.isNull(extnLineOrdTotal) || YFCObject.isVoid(extnLineOrdTotal)) {
							extnLineOrdTotal = "0.0";
							rootOrdLineExtnEle.setAttribute("ExtnLineOrderedTotal", extnLineOrdTotal);
						}
					} else {
						extnLineOrdTotal = "0.0";
						rootOrdLineExtnEle.setAttribute("ExtnLineOrderedTotal", extnLineOrdTotal);
					}
					dExtnOrdSubTotal = dExtnOrdSubTotal + Double.parseDouble(extnLineOrdTotal);
				}
			}
		}
		return new Double(dExtnOrdSubTotal).toString();
	}
	
	private YFCElement setInstructionKeys(YFCElement rootEle, YFCElement ordEle) throws Exception {
		
		YFCElement instructionsEle = rootEle.getChildElement("Instructions");
		if(instructionsEle != null) {
			YFCIterable yfcItr = instructionsEle.getChildren("Instruction");
			while(yfcItr.hasNext()) {
				YFCElement instructionEle = (YFCElement)yfcItr.next();
				if(instructionEle.hasAttribute("InstructionType")) {
					String instType = instructionEle.getAttribute("InstructionType");
					if(!YFCObject.isNull(instType) && !YFCObject.isVoid(instType)) {
						if(instType.equalsIgnoreCase("HEADER")) {
							YFCElement rootExtnEle = rootEle.getChildElement("Extn");
							if(rootExtnEle != null) {
								String extnWebConfNum = null;
								if(rootExtnEle.hasAttribute("ExtnWebConfNum")) {
									extnWebConfNum = rootExtnEle.getAttribute("ExtnWebConfNum");
								}
								
								String extnLegacyOrderNo = null;
								if(rootExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
									extnLegacyOrderNo = rootExtnEle.getAttribute("ExtnLegacyOrderNo");
								}
								
								String genNo = null;
								if(rootExtnEle.hasAttribute("ExtnGenerationNo")) {
									genNo = rootExtnEle.getAttribute("ExtnGenerationNo");
								}
								
								if(!YFCObject.isNull(extnWebConfNum) && !YFCObject.isVoid(extnWebConfNum) && 
										!YFCObject.isNull(extnLegacyOrderNo) && !YFCObject.isVoid(extnLegacyOrderNo) && !YFCObject.isNull(genNo) && !YFCObject.isVoid(genNo)) {
									String instDtlKey = getOrderInstructionKey(extnWebConfNum, extnLegacyOrderNo, genNo, ordEle);
									if(!YFCObject.isNull(instDtlKey) && !YFCObject.isVoid(instDtlKey)) {
										instructionEle.setAttribute("InstructionDetailKey", instDtlKey);
									}
								}
							}
						}
					}
				}
			}
		}
		
		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if(rootOrdLinesEle != null) {
			YFCIterable yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				YFCElement rootLineInstructionsEle = rootOrdLineEle.getChildElement("Instructions");
				if(rootLineInstructionsEle != null) {
					YFCIterable yfcItr1 = rootLineInstructionsEle.getChildren("Instruction");
					while(yfcItr1.hasNext()) {
						YFCElement rootLineInstructionEle = (YFCElement) yfcItr1.next();
						if(rootLineInstructionEle.hasAttribute("InstructionType")) {
							String lineInstType = rootLineInstructionEle.getAttribute("InstructionType");
							if(!YFCObject.isNull(lineInstType) && !YFCObject.isVoid(lineInstType)) {
								if(lineInstType.equalsIgnoreCase("LINE")) {
									YFCElement rootLineExtnEle = rootOrdLineEle.getChildElement("Extn");
									if(rootLineExtnEle != null) {
										String extnWebLineNum = null;
										if(rootLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
											extnWebLineNum = rootLineExtnEle.getAttribute("ExtnWebLineNumber");
										}
										
										String extnLegacyLineNo = null;
										if(rootLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
											extnLegacyLineNo = rootLineExtnEle.getAttribute("ExtnLegacyLineNumber");
										}
										
										if(!YFCObject.isNull(extnWebLineNum) && !YFCObject.isVoid(extnWebLineNum) 
												&& !YFCObject.isNull(extnLegacyLineNo) && !YFCObject.isVoid(extnLegacyLineNo)) {
											YFCElement _ordLineEle = this.getOrderLineForWebLineNo(extnWebLineNum, extnLegacyLineNo, ordEle);
											if(_ordLineEle != null) {
												String instDtlKey = getOrderLineInstructionKey(extnWebLineNum, extnLegacyLineNo, _ordLineEle);
												if(!YFCObject.isNull(instDtlKey) && !YFCObject.isVoid(instDtlKey)) {
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
	
	private String getOrderLineInstructionKey(String webLineNum, String legacyLineNo, YFCElement ordLineEle) throws Exception {
		
		YFCElement ordLineExtnEle = ordLineEle.getChildElement("Extn");
		String _webLineNum = null;
		if(ordLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
			_webLineNum = ordLineExtnEle.getAttribute("ExtnWebLineNumber");
			if(YFCObject.isNull(_webLineNum) || YFCObject.isVoid(_webLineNum)) {
				throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute ExtnWebLineNumber Not Available in getOrderList Template!"); 
		}
		
		if(YFCObject.isNull(legacyLineNo) || YFCObject.isVoid(legacyLineNo)) {
			legacyLineNo = "DUMMY";
		}
		
		String _legacyLineNo = "DUMMY";
		if(ordLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
			_legacyLineNo = ordLineExtnEle.getAttribute("ExtnLegacyLineNumber");
			if(YFCObject.isNull(_legacyLineNo) || YFCObject.isVoid(_legacyLineNo)) {
				_legacyLineNo = "DUMMY";
			}
		} else {
			throw new Exception("Attribute ExtnLegacyLineNumber Not Available in getOrderList Template!"); 
		}
		
		if(webLineNum.equalsIgnoreCase(_webLineNum) && legacyLineNo.equalsIgnoreCase(_legacyLineNo)) { 
			YFCElement instructionsEle = ordLineEle.getChildElement("Instructions");
			if(instructionsEle != null) {
				YFCIterable yfcItr = instructionsEle.getChildren("Instruction");
				while(yfcItr.hasNext()) {
					YFCElement instructionEle = (YFCElement)yfcItr.next();
					if(instructionEle.hasAttribute("InstructionType")) {
						String instType = instructionEle.getAttribute("InstructionType");
						if(!YFCObject.isNull(instType) && !YFCObject.isVoid(instType)) {
							if(instType.equalsIgnoreCase("LINE")) {
								if(instructionEle.hasAttribute("InstructionDetailKey")) {
									String instDtlKey = instructionEle.getAttribute("InstructionDetailKey");
									if(!YFCObject.isNull(instDtlKey) && !YFCObject.isVoid(instDtlKey)) {
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
		if(ordExtnEle.hasAttribute("ExtnWebConfNum")) {
			_WebConfNum = ordExtnEle.getAttribute("ExtnWebConfNum");
			if(YFCObject.isNull(_WebConfNum) || YFCObject.isVoid(_WebConfNum)) {
				throw new Exception("Attribute ExtnWebConfNum Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute ExtnWebConfNum Not Available in getOrderList Template!"); 
		}
		
		if(YFCObject.isNull(legacyOrderNo) || YFCObject.isVoid(legacyOrderNo)) {
			legacyOrderNo = "DUMMY";
		}
		
		String _legacyOrderNo = "DUMMY";
		if(ordExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
			_legacyOrderNo = ordExtnEle.getAttribute("ExtnLegacyOrderNo");
			if(YFCObject.isNull(_legacyOrderNo) || YFCObject.isVoid(_legacyOrderNo)) {
				_legacyOrderNo = "DUMMY";
			}
		} else {
			throw new Exception("Attribute ExtnLegacyOrderNo Not Available in getOrderList Template!"); 
		}
		
		if(YFCObject.isNull(genNo) || YFCObject.isVoid(genNo)) {
			genNo = "DUMMY";
		}
		
		String _genNo = "DUMMY";
		if(ordExtnEle.hasAttribute("ExtnGenerationNo")) {
			_genNo = ordExtnEle.getAttribute("ExtnGenerationNo");
			if(YFCObject.isNull(_genNo) || YFCObject.isVoid(_genNo)) {
				_genNo = "DUMMY";
			}
		} else {
			throw new Exception("Attribute ExtnGenerationNo Not Available in getOrderList Template!"); 
		}
		
		if(webConfNum.equalsIgnoreCase(webConfNum) && legacyOrderNo.equalsIgnoreCase(_legacyOrderNo) && genNo.equalsIgnoreCase(_genNo)) {
			YFCElement instructionsEle = ordEle.getChildElement("Instructions");
			if(instructionsEle != null) {
				YFCIterable yfcItr = instructionsEle.getChildren("Instruction");
				while(yfcItr.hasNext()) {
					YFCElement instructionEle = (YFCElement)yfcItr.next();
					if(instructionEle.hasAttribute("InstructionType")) {
						String instType = instructionEle.getAttribute("InstructionType");
						if(!YFCObject.isNull(instType) && !YFCObject.isVoid(instType)) {
							if(instType.equalsIgnoreCase("HEADER")) {
								if(instructionEle.hasAttribute("InstructionDetailKey")) {
									String instDtlKey = instructionEle.getAttribute("InstructionDetailKey");
									if(!YFCObject.isNull(instDtlKey) && !YFCObject.isVoid(instDtlKey)) {
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
	 *  Gets the customer and fulfillment order details using web confirmation number.
	 *  	                                               
		@param  env  Environment object.  	                        
		@param  rootEle  Parent document element of the doc received from legacy.  
		@param  headerProcessCode  Code defined to do specific actions(A,C,D and S). 	                          
		@return  Element with CO and FO details. 
	 *  
	 */
	
	private YFCElement getCustomerOrderAndFulfillmentOrderList(YFSEnvironment env, YFCElement rootEle, String headerProcessCode) throws Exception {
		
		YFCElement rootExtnEle = rootEle.getChildElement("Extn");
		String extnWebConfNum = null;
		if(rootExtnEle != null) {
			if(rootExtnEle.hasAttribute("ExtnWebConfNum")) {
				extnWebConfNum = rootExtnEle.getAttribute("ExtnWebConfNum");
				if(YFCObject.isNull(extnWebConfNum) || YFCObject.isVoid(extnWebConfNum)) {
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
		log.debug("XPXPerformLegacyOrderUpdateAPI.getCustomerOrderAndFulfillmentOrderList()-InXML:"+getOrdListInDoc.getString());
		}
		
		log.verbose("");
		log.verbose("XPXPerformLegacyOrderUpdateAPI.getCustomerOrderAndFulfillmentOrderList()-InXML:"+getOrdListInDoc.getString());
		log.verbose("");
		
		YFCDocument getOrdListOutDoc = null;
		Document tempDoc = api.executeFlow(env, "XPXGetOrderListForLegacyOrderUpdate", getOrdListInDoc.getDocument());
		if(tempDoc != null) {
			getOrdListOutDoc = YFCDocument.getDocumentFor(tempDoc);
			
			
			log.debug("XPXPerformLegacyOrderUpdateAPI.getCustomerOrderAndFulfillmentOrderList()-OutXML:"+getOrdListOutDoc.getString());
			
			
			log.verbose("");
			log.verbose("XPXPerformLegacyOrderUpdateAPI.getCustomerOrderAndFulfillmentOrderList()-OutXML:"+getOrdListOutDoc.getString());
			log.verbose("");
		}
		
		if(getOrdListOutDoc != null) {
			YFCElement ordListOutEle = getOrdListOutDoc.getDocumentElement();
			if(ordListOutEle != null) {
				if(ordListOutEle.hasAttribute("TotalOrderList")) {
					String totalOrdList = ordListOutEle.getAttribute("TotalOrderList");
					if(totalOrdList.equalsIgnoreCase("0")) {
						if(!headerProcessCode.equalsIgnoreCase("A")) {
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
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				envVariablesmap.put("isDiscountCalculate", "false");
				envVariablesmap.put("isPnACall", "false");
			}
			else{
				envVariablesmap=new HashMap();
				envVariablesmap.put("isDiscountCalculate", "false");
				envVariablesmap.put("isPnACall", "false");
			}
			
			clientVersionSupport.setClientProperties(envVariablesmap);
		}
	}
	
	public void setReqUOMPrice(YFSEnvironment env, YFCElement rootEle) throws Exception {
		YFCElement itemUOMListEle = null;
		
		Set itemIds = this.getItemIds(rootEle);
		YFCElement itemEle = this.getItemUOMListInXML(itemIds);
		
		if(log.isDebugEnabled()){
		log.debug("getItemUOMList-InXML:"+itemEle.getString());
		}
		Document temp = this.api.invoke(env, "getItemUOMList", itemEle.getOwnerDocument().getDocument());
		if(temp != null) {
			itemUOMListEle = YFCDocument.getDocumentFor(temp).getDocumentElement();
			
			if(log.isDebugEnabled()){
			log.debug("getItemUOMList-OutXML:"+itemUOMListEle.getString());
			}
		} else {
			return;
		}
		
		YFCElement ordLinesEle = rootEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					String lpc = null;
					if(ordLineEle.hasAttribute("LineProcessCode")) {
						lpc = ordLineEle.getAttribute("LineProcessCode");
						if(YFCObject.isNull(lpc) || YFCObject.isVoid(lpc)) {
							throw new Exception("Attribute LineProcessCode Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute LineProcessCode Not Available in Incoming Legacy Message!");
					}
					if(lpc.equalsIgnoreCase("A") || lpc.equalsIgnoreCase("C")) {
						String reqUOM = null;
						YFCElement ordLineTranQtyEle = ordLineEle.getChildElement("OrderLineTranQuantity");
						if(ordLineTranQtyEle != null) {
							if(ordLineTranQtyEle.hasAttribute("TransactionalUOM")) {
								reqUOM = ordLineTranQtyEle.getAttribute("TransactionalUOM");
							}
						}
						
						String prcUOM = null;
						String unitPrc = null;
						YFCElement ordLineExtnEle = ordLineEle.getChildElement("Extn");
						if(ordLineExtnEle != null) {
							if(ordLineExtnEle.hasAttribute("ExtnPricingUOM")) {
								prcUOM = ordLineExtnEle.getAttribute("ExtnPricingUOM");
							}
							if(ordLineExtnEle.hasAttribute("ExtnUnitPrice")) {
								unitPrc = ordLineExtnEle.getAttribute("ExtnUnitPrice");
							}
						}
						
						String itemId = null;
						YFCElement ordLineItemEle = ordLineEle.getChildElement("Item");
						if(ordLineItemEle != null) {
							if(ordLineItemEle.hasAttribute("ItemID")) {
								itemId = ordLineItemEle.getAttribute("ItemID");
							}
						}
						double reqUOMPrice = getReqUOMPrice(itemId, reqUOM, prcUOM, unitPrc, itemUOMListEle);
						if(ordLineExtnEle != null) {
							if(reqUOMPrice != 0) {
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
		
		YFCIterable yfcItr = itemUOMListEle.getChildren("ItemUOM");
		while(yfcItr.hasNext()) {
			String uom = null;
			YFCElement itemUOMEle = (YFCElement) yfcItr.next();
			if(itemUOMEle != null) {
				YFCElement itemEle = itemUOMEle.getChildElement("Item");
				if(itemEle != null) {
					String uomItemId = null;
					if(itemEle.hasAttribute("ItemID")) {
						uomItemId = itemEle.getAttribute("ItemID");
						if(YFCObject.isNull(uomItemId) || YFCObject.isVoid(uomItemId)) {
							throw new Exception("Attribute ItemID Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute ItemID Not Available in getItemUOMList Template!");
					}
					if(uomItemId.equalsIgnoreCase(itemId)) {
						if(itemUOMEle.hasAttribute("UnitOfMeasure")) {
							uom = itemUOMEle.getAttribute("UnitOfMeasure");
							if(!YFCObject.isNull(uom) && !YFCObject.isVoid(uom)) {
								// PUOM - Conversion factor.
								if(uom.equalsIgnoreCase(prcUOM)){
									cnvrFctrPUOM = itemUOMEle.getAttribute("Quantity");				
								}
								// RUOM - Conversion factor.
								if (uom.equalsIgnoreCase(reqUOM)){
									cnvrFctrRUOM = 	itemUOMEle.getAttribute("Quantity");
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
		log.debug("itemId:"+itemId);
		log.debug("reqUOM:"+reqUOM);
		log.debug("prcUOM:"+prcUOM);
		log.debug("unitPrice:"+unitPrice);
		log.debug("cnvrFctrPUOM:"+cnvrFctrPUOM);
		log.debug("cnvrFctrRUOM:"+cnvrFctrRUOM);
		}
		
		double dReqUOMPrice = 0.0;
		if(!YFCObject.isNull(unitPrice) && !YFCObject.isVoid(unitPrice)
			&& !YFCObject.isNull(cnvrFctrPUOM) && !YFCObject.isVoid(cnvrFctrPUOM)
				&& !YFCObject.isNull(cnvrFctrRUOM) && !YFCObject.isVoid(cnvrFctrRUOM)) {
			dReqUOMPrice = (Double.parseDouble(unitPrice)/Double.parseDouble(cnvrFctrPUOM)) * Double.parseDouble(cnvrFctrRUOM); 
		}
		if(log.isDebugEnabled()){
		log.debug("dReqUOMPrice:"+dReqUOMPrice);
		}
		
		return dReqUOMPrice;
	}
	
	private Set getItemIds(YFCElement ordEle) {
		Set set = new HashSet();
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				String itemId = null;
				YFCElement ordLineItemEle = ordLineEle.getChildElement("Item");
				if(ordLineItemEle != null) {
					if(ordLineItemEle.hasAttribute("ItemID")) {
						itemId = ordLineItemEle.getAttribute("ItemID");
						if(!YFCObject.isNull(itemId) && !YFCObject.isVoid(itemId)) {	
							set.add(itemId);
						}
					}
				}
			}
		}
		return set;
	}
	
	private YFCElement getItemUOMListInXML(Set itemIds) {
		if(itemIds.size() > 0) {
			YFCElement itemEle = YFCDocument.getDocumentFor("<Item><ComplexQuery><Or/></ComplexQuery></Item>").getDocumentElement();
			YFCElement complexQryEle = itemEle.getChildElement("ComplexQuery");
			YFCElement OrEle = complexQryEle.getChildElement("Or");
			Iterator itr = itemIds.iterator();
			while(itr.hasNext()) {
				String itemId = (String)itr.next();
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
		if(chngOrdEle.hasAttribute("HeaderProcessCode")) {
			String hpc = chngOrdEle.getAttribute("HeaderProcessCode");
			if(!YFCObject.isNull(hpc) && !YFCObject.isVoid(hpc)) {
				isOrderDelete = hpc.equalsIgnoreCase("D");
			}
		}
		if(isCustOrder) {
			if(chngOrdEle.getChildElement("Instructions") != null) {
				chngOrdEle.removeChild(chngOrdEle.getChildElement("Instructions"));
			}
		}
		
		YFCElement chngOrdLinesEle = chngOrdEle.getChildElement("OrderLines");
		YFCIterable yfcItr = chngOrdLinesEle.getChildren("OrderLine");
		while(yfcItr.hasNext()) {
			YFCElement chngOrdLineEle = (YFCElement) yfcItr.next();
			if(chngOrdLineEle.hasAttribute("PrimeLineNo")) {
				chngOrdLineEle.removeAttribute("PrimeLineNo");
			}
			if(chngOrdLineEle.hasAttribute("SubLineNo")) {
				chngOrdLineEle.removeAttribute("SubLineNo");
			}
			
			if(chngOrdLineEle.hasAttribute("OrderedQty")) {
				chngOrdLineEle.removeAttribute("OrderedQty");
			}
			
			if(chngOrdLineEle.hasAttribute("Action")) {
				String action = chngOrdLineEle.getAttribute("Action");
				if(!YFCObject.isNull(action) && !YFCObject.isVoid(action)) {
					if(action.equalsIgnoreCase("CREATE") && !isCustOrder) {
						setOrdLineScheduleQty(chngOrdLineEle);
					}
				}
			}
			if(isCustOrder) {
				if(chngOrdLineEle.getChildElement("Instructions") != null) {
					chngOrdLineEle.removeChild(chngOrdLineEle.getChildElement("Instructions"));
				}
			}
			String lpc = null;
			if(chngOrdLineEle.hasAttribute("LineProcessCode")) {
				lpc = chngOrdLineEle.getAttribute("LineProcessCode");
			}
			
			if(!YFCObject.isNull(lpc) && !YFCObject.isVoid(lpc)) {
				if(!lpc.equalsIgnoreCase("D")) {
					// Do Nothing
					hasOtherLines = true;
				}
			}
		}
		if(log.isDebugEnabled()){
		log.debug("hasOtherLines:"+hasOtherLines);
		log.debug("isOrderDelete:"+isOrderDelete);
		}
		
		if(!hasOtherLines || isOrderDelete) {
			if(chngOrdEle.hasAttribute("ReqDeliveryDate")) {
				chngOrdEle.removeAttribute("ReqDeliveryDate");
			}
			YFCElement shipTo = chngOrdEle.getChildElement("PersonInfoShipTo");
			if(shipTo != null) {
				chngOrdEle.removeChild(shipTo);
			}
			YFCElement billTo = chngOrdEle.getChildElement("PersonInfoBillTo");
			if(billTo != null) {
				chngOrdEle.removeChild(billTo);
			}
		}
	}
	
	private void setOrdLineScheduleQty(YFCElement chngOrdLineEle) throws Exception {
		YFCElement chngOrdLineTranQtyEle = chngOrdLineEle.getChildElement("OrderLineTranQuantity");
		if(chngOrdLineTranQtyEle != null) {
			String lineType = null;
			if(chngOrdLineEle.hasAttribute("LineType")) {
				lineType = chngOrdLineEle.getAttribute("LineType");
				if(YFCObject.isNull(lineType) || YFCObject.isVoid(lineType)) {
					throw new Exception("Attribute LineType Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute LineType Not Available in Incoming Legacy Message!");
			}
			
			String ordQty = null;
			if(chngOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
				ordQty = chngOrdLineTranQtyEle.getAttribute("OrderedQty");
			}
			
			String tuom = null;
			if(chngOrdLineTranQtyEle.hasAttribute("TransactionalUOM")) {
				tuom = chngOrdLineTranQtyEle.getAttribute("TransactionalUOM");
			}
			if(!YFCObject.isNull(ordQty) && !YFCObject.isVoid(ordQty) && 
					((!YFCObject.isNull(tuom) && !YFCObject.isVoid(tuom)) || lineType.equalsIgnoreCase("M"))) {
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
	
	private void validateInXML(YFCElement rootEle) {
		YFCElement rootOrdLinesEle = rootEle.getChildElement("OrderLines");
		if(rootOrdLinesEle != null) {
			YFCIterable yfcItr = rootOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				String lpc = null;
				YFCElement rootOrdLineEle = (YFCElement) yfcItr.next();
				if(rootOrdLineEle.hasAttribute("LineProcessCode")) {
					lpc = rootOrdLineEle.getAttribute("LineProcessCode");
				}
				YFCElement rootOrdLineTranQtyEle = rootOrdLineEle.getChildElement("OrderLineTranQuantity");
				YFCElement rootOrdLineExtnEle = rootOrdLineEle.getChildElement("Extn");
				if(rootOrdLineTranQtyEle != null && rootOrdLineExtnEle != null) {
					String tOrdQty = null;
					String reqShipOrdQty = null;
					String reqBackOrdQty = null;
					if(rootOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
						tOrdQty = rootOrdLineTranQtyEle.getAttribute("OrderedQty");
					}
					if(rootOrdLineExtnEle.hasAttribute("ExtnReqShipOrdQty")) {
						reqShipOrdQty = rootOrdLineExtnEle.getAttribute("ExtnReqShipOrdQty");
					}
					if(rootOrdLineExtnEle.hasAttribute("ExtnReqBackOrdQty")) {
						reqBackOrdQty = rootOrdLineExtnEle.getAttribute("ExtnReqBackOrdQty");
					}
					
					if(!YFCObject.isNull(tOrdQty) && !YFCObject.isVoid(tOrdQty) && 
									!YFCObject.isNull(reqShipOrdQty) && !YFCObject.isVoid(reqShipOrdQty) && 
													!YFCObject.isNull(reqBackOrdQty) && !YFCObject.isVoid(reqBackOrdQty) &&
																			!YFCObject.isNull(lpc) && !YFCObject.isVoid(lpc)) {
						if(lpc.equalsIgnoreCase("D")) {
							rootOrdLineTranQtyEle.setAttribute("OrderedQty",Float.parseFloat("0"));
						} else {
//							Float fTOrdQty = Float.parseFloat(tOrdQty);
//							Float fReqShipOrdQty = Float.parseFloat(reqShipOrdQty);
//							Float fReqBackOrdQty = Float.parseFloat(reqBackOrdQty);
//							if(fReqShipOrdQty != 0) {
//								rootOrdLineTranQtyEle.setAttribute("OrderedQty",fReqShipOrdQty.toString());
//								if((fReqBackOrdQty == 0) && (fTOrdQty > fReqShipOrdQty)) {
//									rootOrdLineEle.setAttribute("LineProcessCode", "D");
//								}
//							}
						}
					}
				}
			}
		}
	}
	
	public void setProperties(Properties _prop) throws Exception {
		this._prop = _prop;
	}
	
}