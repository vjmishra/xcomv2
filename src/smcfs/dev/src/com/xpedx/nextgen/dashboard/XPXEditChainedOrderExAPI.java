package com.xpedx.nextgen.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.xpedx.nextgen.customerprofilerulevalidation.api.XPXCustomerProfileRuleConstant;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;

import com.yantra.shared.ycp.YFSContext;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXEditChainedOrderExAPI implements YIFCustomApi {
	
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
	
	public Document editChainedOrder(YFSEnvironment env, Document inXML) throws Exception {
		try {						
			if(inXML != null) {
				
				List<String> webLineNos = new ArrayList<String>();
				
				String cOrdHeaderKey = null;
				String fOrdHeaderKey = null;
				String webConfNum = null;
				
				YFCElement editOrdEle = YFCDocument.getDocumentFor(inXML).getDocumentElement();
					
				log.debug("XPXEditChainedOrderAPI-InXML:"+editOrdEle.getString());
				
				String ordType = null;
				if(editOrdEle.hasAttribute("OrderType")) {
					ordType = editOrdEle.getAttribute("OrderType");
					if(YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
						throw new Exception("Attribute OrderType Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute OrderType Not Available in Edit Order InXML!");
				}
				
				if(ordType.equalsIgnoreCase("Customer")) {
					if(editOrdEle.hasAttribute("OrderHeaderKey")) {
						cOrdHeaderKey = editOrdEle.getAttribute("OrderHeaderKey");
						if(YFCObject.isNull(cOrdHeaderKey) || YFCObject.isVoid(cOrdHeaderKey)) {
							throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void in Edit Order InXML!");
						}
					} else {
						throw new Exception("Attribute OrderHeaderKey Not Available in Edit Order InXML!");
					}
				} else {
					if(editOrdEle.hasAttribute("OrderHeaderKey")) {
						fOrdHeaderKey = editOrdEle.getAttribute("OrderHeaderKey");
						if(YFCObject.isNull(fOrdHeaderKey) || YFCObject.isVoid(fOrdHeaderKey)) {
							throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void in Edit Order InXML!");
						}
					} else {
						throw new Exception("Attribute OrderHeaderKey Not Available in Edit Order InXML!");
					}
				}
				
				YFCElement editOrdExtnEle = editOrdEle.getChildElement("Extn");
				if(editOrdExtnEle != null) {
					editOrdExtnEle.setAttribute("ExtnOrderLockFlag", "Y");
					if(editOrdExtnEle.hasAttribute("ExtnWebConfNum")) {
						webConfNum = editOrdExtnEle.getAttribute("ExtnWebConfNum");
						if(YFCObject.isNull(webConfNum) || YFCObject.isVoid(webConfNum)) {
							throw new Exception("Attribute ExtnWebConfNum Cannot be NULL or Void in Edit Order InXML!");
						}
					} else {
						throw new Exception("Attribute ExtnWebConfNum Not Available in Edit Order InXML!");
					}
				} else {
					throw new Exception("Element Extn Not Available in Edit Order InXML!");
				}
				
				YFCElement editOrdLinesEle = editOrdEle.getChildElement("OrderLines");
				if(editOrdLinesEle != null) {
					YFCIterable<YFCElement> yfcItr = editOrdLinesEle.getChildren("OrderLine");
					while(yfcItr.hasNext()) {
						YFCElement editOrdLineEle = (YFCElement) yfcItr.next();
						
						String action = null;
						if(editOrdLineEle.hasAttribute("Action")) {
							action = editOrdLineEle.getAttribute("Action");
						}
						String editOrdLineKey = null;
						if(editOrdLineEle.hasAttribute("OrderLineKey")) {
							editOrdLineKey = editOrdLineEle.getAttribute("OrderLineKey");
						} 
						
						String webLineNo = null;
						YFCElement editOrdLineExtnEle = editOrdLineEle.getChildElement("Extn");
						if(editOrdLineExtnEle != null) {
							if(editOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
								webLineNo = editOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
							}
						} 
						
						if(!YFCObject.isNull(action) && !YFCObject.isVoid(action)) {
							if(!action.equalsIgnoreCase("CREATE")) {
								if(!YFCObject.isNull(editOrdLineKey) && !YFCObject.isVoid(editOrdLineKey)) {
									// Do Nothing
								} else if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo) && ordType.equalsIgnoreCase("Customer")) {
									// Do Nothing
								} else if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo) && !ordType.equalsIgnoreCase("Customer")) {
									// Do Nothing
								} else {
									throw new Exception("One or More Mandatory Attributes[Action='CREATE', OrderLineKey, WebLineNo, OrderType] Not Availble in Incoming Edit Message!");
								}
							}
						} else if(!YFCObject.isNull(editOrdLineKey) && !YFCObject.isVoid(editOrdLineKey)) {
							// Do Nothing
						} else if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo) && ordType.equalsIgnoreCase("Customer")) {
							// Do Nothing
						} else if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo) && !ordType.equalsIgnoreCase("Customer")) {
							// Do Nothing
						} else {
							throw new Exception("One or More Mandatory Attributes[Action='CREATE', OrderLineKey, WebLineNo, OrderType] Not Availble in Incoming Edit Message!");
						}
						
						if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo)) {
							webLineNos.add(webLineNo);
						}
						
						String lineType = null;
						if(editOrdLineEle.hasAttribute("LineType")) {
							lineType = editOrdLineEle.getAttribute("LineType");
							if(YFCObject.isNull(lineType) || YFCObject.isVoid(lineType)) {
								throw new Exception("Attribute LineType Cannot be NULL or Void in Incoming Edit Message!");
							}
						} else {
							throw new Exception("Attribute LineType Not Available in Incoming Edit Message!");
						}
						
						YFCElement editOrdLineTranQtyEle = editOrdLineEle.getChildElement("OrderLineTranQuantity");
						if(editOrdLineTranQtyEle != null) {
							if(editOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
								String editOrdLineTranQty = editOrdLineTranQtyEle.getAttribute("OrderedQty");
								if(YFCObject.isNull(editOrdLineTranQty) || YFCObject.isVoid(editOrdLineTranQty)) {
									throw new Exception("Attribute OrderLineTranQuantity/@OrderedQty Cannot be NULL or Void in Edit Order InXML!");
								}
							} else {
								throw new Exception("Attribute OrderLineTranQuantity/@OrderedQty Not Available in Edit Order InXML!");
							}
							if(!lineType.equalsIgnoreCase("M")) {
								if(editOrdLineTranQtyEle.hasAttribute("TransactionalUOM")) {
									String tuom = editOrdLineTranQtyEle.getAttribute("TransactionalUOM");
									if(YFCObject.isNull(tuom) || YFCObject.isVoid(tuom)) {
										throw new Exception("Attribute OrderLineTranQuantity/@TransactionalUOM Cannot be NULL or Void in Edit Order InXML!");
									}
								} else {
									throw new Exception("Attribute OrderLineTranQuantity/@TransactionalUOM Not Available in Edit Order InXML!");
								}
							}
						}
					}                     
				}
				
				Set<String> uniqueWebLineNos = new TreeSet<String>(webLineNos);
				if(webLineNos.size() > uniqueWebLineNos.size()) {
					throw new Exception("Duplicate ExtnWebLineNumber Found in Edit Order XML!");
				}
				
				YFCElement cAndfOrdListEle = getcOrderAndfOrderList(env, webConfNum);
				processOrderEdit(env, ordType, webConfNum, cOrdHeaderKey, fOrdHeaderKey, editOrdEle, cAndfOrdListEle);
				
			} else {
				throw new Exception("Edit Order InXML Cannot be NULL!");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			((YFSContext)env).rollback();
			prepareErrorObject(ex, "OrderEdit", XPXLiterals.E_ERROR_CLASS, env, inXML);
			inXML.getDocumentElement().setAttribute("TransactionMessage",ex.getMessage());			
			return inXML;
		}
		return inXML;
	}
	
	private List<String> getAlreadyDeletedLines(YFCElement editOrdEle, YFCElement cOrderEle) throws Exception {
		List<String> alrdyDelLines = new ArrayList<String>();
		YFCElement cOrderLinesEle = cOrderEle.getChildElement("OrderLines");
		YFCIterable<YFCElement> yfcItr = cOrderLinesEle.getChildren("OrderLine");
		while(yfcItr.hasNext()) {
			YFCElement cOrderLineEle = (YFCElement) yfcItr.next(); 
			String cOrdLineKey = cOrderLineEle.getAttribute("OrderLineKey");
			if(this.IsCancelledLine(cOrderLineEle)) {
				if(!YFCObject.isNull(cOrdLineKey) && !YFCObject.isVoid(cOrdLineKey)) {
					alrdyDelLines.add(cOrdLineKey);
				}
			}
		}
		return alrdyDelLines;
	}
	
	private Document processOrderEdit(YFSEnvironment env, String ordType, 
			String webConfNum, String cOrdHeaderKey, String fOrdHeaderKey, 
					YFCElement editOrdEle, YFCElement cAndfOrdListEle) throws Exception {
		
		try {
			Map<String, YFCElement> chngfOrders = null;
			List<String> alrdyDelLines = new ArrayList<String>();
			YFCElement fOrderEle = null;
			YFCElement buyerOrgCustEle = null;
			
			YFCElement cOrderEle = getcOrderEle(webConfNum, cAndfOrdListEle);
			if(cOrderEle == null) {
				throw new Exception("No Customer Order With ExtnWebConfNum["+webConfNum+"] Found!");
			}
			
			if(ordType.equalsIgnoreCase("Customer")) {
				alrdyDelLines = getAlreadyDeletedLines(editOrdEle, cOrderEle);
				stampMandatoryAttributes(env, editOrdEle, cOrderEle, buyerOrgCustEle);
				chngfOrders = prepareChngfOrderFromcOrder(env, editOrdEle, cOrderEle, cAndfOrdListEle, buyerOrgCustEle);
			} else {
				fOrderEle = getfOrderEle(fOrdHeaderKey, cAndfOrdListEle);
				if(fOrderEle == null) {
					throw new Exception("No Fulfillment Order With OrderHeaderKey["+fOrdHeaderKey+"] Found!");
				}
				alrdyDelLines = getAlreadyDeletedLines(editOrdEle, fOrderEle);
				stampMandatoryAttributes(env, editOrdEle, fOrderEle, buyerOrgCustEle);
				chngfOrders = prepareChngfOrderFromfOrder(env, editOrdEle, fOrderEle, cAndfOrdListEle, buyerOrgCustEle);
			}
		
			if (log.isDebugEnabled()) {
				log.debug("chngfOrders:"+chngfOrders);
			}
				
			String chngfOrdersArray[] = { "CC", "CA", "AA" };
			for(int i=0; i < chngfOrdersArray.length; i++) {
				boolean postEditMsgToLegacy = false;
				YFCElement postToLegacyOrdEle = null;
				
				YFCElement chngfOrdEle = null;				
				if(chngfOrdersArray[i].equalsIgnoreCase("CC") || chngfOrdersArray[i].equalsIgnoreCase("CA")) {
					String processCode = chngfOrdersArray[i];
					if(processCode.equalsIgnoreCase("CC")) {
						chngfOrdEle = (YFCElement)chngfOrders.get("CC");
					} else {
						chngfOrdEle = (YFCElement) chngfOrders.get("CA");
						YFCElement _fOrderEle = (YFCElement) chngfOrders.get("FO");
						if(_fOrderEle != null) {
							fOrderEle = _fOrderEle;
						}
					}
					if(chngfOrdEle != null) {
						validateWebHoldFlag(chngfOrdEle, buyerOrgCustEle);	
						if(ordType.equalsIgnoreCase("Customer")) {
							if(fOrderEle == null) {
								if(chngfOrdEle.hasAttribute("OrderHeaderKey")) {
									fOrdHeaderKey = chngfOrdEle.getAttribute("OrderHeaderKey");
									if(YFCObject.isNull(fOrdHeaderKey) || YFCObject.isVoid(fOrdHeaderKey)) {
										throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
									}
									fOrderEle = getfOrderEle(fOrdHeaderKey, cAndfOrdListEle);
									if(fOrderEle == null) {
										throw new Exception("No Fulfillment Order With OrderHeaderKey["+fOrdHeaderKey+"] Found!");
									}
								} else {
									throw new Exception("Attribute OrderHeaderKey Not Available in changeOrder API Call!");
								}
							}
						}
						Map<String, YFCElement> retDocMap = processOrderEditC(env, chngfOrdEle, fOrderEle, cOrderEle);
						if(retDocMap != null) {
							if(retDocMap.size() > 0) {
								YFCElement foDocEle = (YFCElement)retDocMap.get("FO");
								if(foDocEle != null) {
									postToLegacyOrdEle = foDocEle;
									if(isOPRProcessed(postToLegacyOrdEle)) {
										setDefaultPC(postToLegacyOrdEle, alrdyDelLines, "C");
									} else {
										setDefaultPC(postToLegacyOrdEle, alrdyDelLines, "A");
									}
									postEditMsgToLegacy = true;
									
									if (log.isDebugEnabled()) {
										log.debug("PostToLegacyOrdEle:"+postToLegacyOrdEle.getString());
									}
								}
								YFCElement coDocEle = (YFCElement)retDocMap.get("CO");
								if(coDocEle != null) {
									cOrderEle = coDocEle;
								}
							}
						}
					}
				}
				
				if(chngfOrdersArray[i].equalsIgnoreCase("AA")) {
					chngfOrdEle = (YFCElement)chngfOrders.get("AA");
					if(chngfOrdEle != null) {
						validateWebHoldFlag(chngfOrdEle, buyerOrgCustEle);
						Map<String, YFCElement> retDocMap = processOrderEditA(env, chngfOrdEle, fOrderEle, cOrderEle, buyerOrgCustEle);
						if(retDocMap != null) {
							if(retDocMap.size() > 0) {
								YFCElement foDocEle = (YFCElement)retDocMap.get("FO");
								if(foDocEle != null) {
									postToLegacyOrdEle = foDocEle;
									setDefaultPC(postToLegacyOrdEle, alrdyDelLines, "A");
									postEditMsgToLegacy = true;
									
									if (log.isDebugEnabled()) {
										log.debug("PostToLegacyOrdEle:"+postToLegacyOrdEle.getString());
									}
								}
								YFCElement coDocEle = (YFCElement)retDocMap.get("CO");
								if(coDocEle != null) {
									cOrderEle = coDocEle;
								}
							}
						}
					}
				}
				
				// Post Edit Order Message To Legacy
				if(postEditMsgToLegacy) {
					filterCancelLines(postToLegacyOrdEle);
					if(postToLegacyOrdEle.hasAttribute("IsValidOrder")) {
						
						// Business rule validation for Fulfillment Order.
						Document businessRuleOutputDoc = api.executeFlow(env, "XPXBusinessRuleValidationFOService", postToLegacyOrdEle.getOwnerDocument().getDocument());																		
						updateWebHold(businessRuleOutputDoc,postToLegacyOrdEle);				
							
						log.debug("XPXPostEditOrderToLegacy-InXML:"+postToLegacyOrdEle.getString());
						
						Document editOrdMsgFromLegacy = XPXEditChainedOrderExAPI.api.executeFlow(env, "XPXPostEditOrderToLegacy", postToLegacyOrdEle.getOwnerDocument().getDocument());
						if(editOrdMsgFromLegacy != null) {
							
							log.debug("XPXPostEditOrderToLegacy-OutXML:"+YFCDocument.getDocumentFor(editOrdMsgFromLegacy).getString());
							
							YFCElement editOrdMsgFromLegacyEle = YFCDocument.getDocumentFor(editOrdMsgFromLegacy).getDocumentElement();
							YFCNodeList<YFCElement> nodeListEle = editOrdMsgFromLegacyEle.getElementsByTagName("TransactionStatus");
							if(nodeListEle.getLength() != 0) {
								YFCNode tranStatusNode = nodeListEle.item(0);
								String tranStatus = tranStatusNode.getDOMNode().getTextContent();
								if(YFCObject.isNull(tranStatus) || YFCObject.isVoid(tranStatus)) {
									throw new Exception("Element TransactionStatus Cannot be NULL or Void in Legacy Message!");
								}
								if(tranStatus.equalsIgnoreCase("F")) {
									throw new Exception("Edit Order Failed In Legacy!");
								}
								Document ordUpdateDoc = XPXEditChainedOrderExAPI.api.executeFlow(env, "XPXLegacyOrderUpdateService", editOrdMsgFromLegacy);
								if(ordUpdateDoc != null) {
									
									log.debug("XPXLegacyOrderUpdateService-OutXML:"+YFCDocument.getDocumentFor(ordUpdateDoc).getString());
									
									YFCElement ordUpdateEle = YFCDocument.getDocumentFor(ordUpdateDoc).getDocumentElement();
									YFCNodeList<YFCElement> _nodeListEle = ordUpdateEle.getElementsByTagName("TransactionStatus");
									if(_nodeListEle.getLength() != 0) {
										YFCNode _tranStatusNode = _nodeListEle.item(0);
										String _tranStatus = _tranStatusNode.getDOMNode().getTextContent();
										if(YFCObject.isNull(_tranStatus) || YFCObject.isVoid(_tranStatus)) {
											throw new Exception("Element TransactionStatus Cannot be NULL or Void in Legacy Message!");
										}
										if(_tranStatus.equalsIgnoreCase("F")) {
											throw new Exception("Order Update Failed in Sterling!");
										}
										((YFSContext)env).commit();
									} else {
										throw new Exception("Element TransactionStatus Not Returned During Order Update!");
									}
								} else {
									throw new Exception("Service XPXLegacyOrderUpdateService Failed!");
								}
							} else {
								throw new Exception("Element TransactionStatus Not Available in Legacy Message!");
							}
						} else {
							throw new Exception("Service XPXPostEditOrderToLegacy Failed!");
						}
					} else {
						((YFSContext)env).commit();
					}
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			((YFSContext)env).rollback();
			prepareErrorObject(ex, "OrderEdit", ex.getMessage(), env, editOrdEle.getOwnerDocument().getDocument());
			/*Begin - Changes made by Mitesh Parikh for JIRA 3045*/
			if(env.getTxnObject("OrderEditTransactionFailure") == null)
				editOrdEle.getOwnerDocument().getDocumentElement().setAttribute("TransactionMessage",ex.getMessage());
			else
				editOrdEle.getOwnerDocument().getDocumentElement().setAttribute("TransactionMessage",env.getTxnObject("OrderEditTransactionFailure").toString());
			
			log.debug("Order Edit Transaction failure XML : "+SCXmlUtil.getString(editOrdEle.getOwnerDocument().getDocument()));			
			/*End - Changes made by Mitesh Parikh for JIRA 3045*/
			
			return editOrdEle.getOwnerDocument().getDocument();
		}
		return editOrdEle.getOwnerDocument().getDocument();
	}
	
	private boolean isOPRProcessed(YFCElement fOrderEle) {
		YFCElement fOrderExtnEle = fOrderEle.getChildElement("Extn");
		if(fOrderExtnEle != null) {
			String legacyOrdNo = fOrderExtnEle.getAttribute("ExtnLegacyOrderNo");
			String genNo = fOrderExtnEle.getAttribute("ExtnGenerationNo");
			String isReprocessibleFlag = fOrderExtnEle.getAttribute("ExtnIsReprocessibleFlag");
			
			if (log.isDebugEnabled()) {
				log.debug("legacyOrderNo:"+legacyOrdNo);
				log.debug("genNo:"+genNo);
				log.debug("isReprocessibleFlag:"+isReprocessibleFlag);
				log.debug("IsOPRProcessed:"+(!YFCObject.isNull(legacyOrdNo) && !YFCObject.isVoid(legacyOrdNo) && 
					!YFCObject.isNull(genNo) && !YFCObject.isVoid(genNo) && !YFCObject.isNull(isReprocessibleFlag) && 
						!YFCObject.isVoid(isReprocessibleFlag) && isReprocessibleFlag.equalsIgnoreCase("N")));
			}
			
			
			if(!YFCObject.isNull(legacyOrdNo) && !YFCObject.isVoid(legacyOrdNo) && 
				!YFCObject.isNull(genNo) && !YFCObject.isVoid(genNo) && !YFCObject.isNull(isReprocessibleFlag) && 
					!YFCObject.isVoid(isReprocessibleFlag) && isReprocessibleFlag.equalsIgnoreCase("N")) {
				return true;
			}
		}
		
		return false;
	}
	
	private void filterCancelLines(YFCElement legacyOrdEle) throws Exception {
		YFCElement legacyOrdLinesEle = legacyOrdEle.getChildElement("OrderLines");
		if(legacyOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = legacyOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement legacyOrdLineEle = (YFCElement) yfcItr.next();
				if(legacyOrdLineEle != null) {
					String lpc = legacyOrdLineEle.getAttribute("LineProcessCode");
					if(YFCObject.isNull(lpc) || YFCObject.isVoid(lpc)) {
						throw new Exception("Attribute LineProcessCode Cannot be NULL or Void!");
					}
					boolean isCancelLine = false;
					String ordQty = null;
					YFCElement ordLineTranQtyEle = legacyOrdLineEle.getChildElement("OrderLineTranQuantity");
					if(ordLineTranQtyEle != null) {
						if(ordLineTranQtyEle.hasAttribute("OrderedQty")) {
							ordQty = ordLineTranQtyEle.getAttribute("OrderedQty");
							if(YFCObject.isNull(ordQty) || YFCObject.isVoid(ordQty)) {
								throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
							}
							if(Float.parseFloat(ordQty) == 0) {
								isCancelLine = true;
							}
						} else {
							throw new Exception("Attribute OrderedQty Not Available in OrderList Template!");
						}
					} else {
						throw new Exception("Element OrderLineTranQuantity Not Available in GetOrderList Template!");
					}
					if(lpc.equalsIgnoreCase("_D") && isCancelLine) {
						legacyOrdLinesEle.removeChild(legacyOrdLineEle);
						yfcItr = legacyOrdLinesEle.getChildren("OrderLine");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in the OrderList Template!");
				}
			}
			yfcItr = legacyOrdLinesEle.getChildren("OrderLine");
			if(yfcItr.hasNext()) {
				legacyOrdEle.setAttribute("IsValidOrder", "Y");
			}
		} else {
			throw new Exception("OrderLines Element Not Available in the OrderList Template!");
		}
	}
	
	private void stampMandatoryAttributes(YFSEnvironment env, YFCElement editOrdEle, YFCElement fOrderEle, YFCElement buyerOrgCustEle) throws Exception {
		String entryType = null;
		if(fOrderEle.hasAttribute("EntryType")) {
			entryType = fOrderEle.getAttribute("EntryType");
			if(YFCObject.isNull(entryType) || YFCObject.isVoid(entryType)) {
				throw new Exception("Attribute EntryType Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute EntryType Not Available in getOrderList Template!");
		}
		
		String enterpriseCode = null;
		if(fOrderEle.hasAttribute("EnterpriseCode")) {
			enterpriseCode = fOrderEle.getAttribute("EnterpriseCode");
			if(YFCObject.isNull(enterpriseCode) || YFCObject.isVoid(enterpriseCode)) {
				throw new Exception("Attribute EnterpriseCode Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute EnterpriseCode Not Available in getOrderList Template!");
		}
		
		String buyerOrgCode = null;
		if(fOrderEle.hasAttribute("BuyerOrganizationCode")) {
			buyerOrgCode = fOrderEle.getAttribute("BuyerOrganizationCode");
			if(YFCObject.isNull(buyerOrgCode) || YFCObject.isVoid(buyerOrgCode)) {
				throw new Exception("Attribute BuyerOrganizationCode Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute BuyerOrganizationCode Not Available in getOrderList Template!");
		}
		
		String shipNode = null;
		if(fOrderEle.hasAttribute("ShipNode")) {
			shipNode = fOrderEle.getAttribute("ShipNode");
		}
		
		String custNo = null;
		String envId = null;
		String compId = null;
		String billtoSuffix = null;
		String shipToSuffix = null;
		YFCElement fOrderExtnEle = fOrderEle.getChildElement("Extn");
		if(fOrderExtnEle != null) {
			if(fOrderExtnEle.hasAttribute("ExtnCustomerNo")) {
				custNo = fOrderExtnEle.getAttribute("ExtnCustomerNo");
				if(YFCObject.isNull(custNo) || YFCObject.isVoid(custNo)) {
					throw new Exception("Attribute ExtnCustomerNo Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ExtnCustomerNo Not Available in getOrderList Template!");
			}
			
			if(fOrderExtnEle.hasAttribute("ExtnEnvtId")) {
				envId = fOrderExtnEle.getAttribute("ExtnEnvtId");
				if(YFCObject.isNull(envId) || YFCObject.isVoid(envId)) {
					throw new Exception("Attribute ExtnEnvtId Cannot be NULL or Void!");
				}
				
			} else {
				throw new Exception("Attribute ExtnEnvtId Cannot be NULL or Void!");
			}
			
			if(fOrderExtnEle.hasAttribute("ExtnCompanyId")) {
				compId = fOrderExtnEle.getAttribute("ExtnCompanyId");
				if(YFCObject.isNull(compId) || YFCObject.isVoid(compId)) {
					throw new Exception("Attribute ExtnCompanyId Cannot be NULL or Void!");
				}
				
			} else {
				throw new Exception("Attribute ExtnCompanyId Not Available in getOrderList Template!");
			}
			
			if(fOrderExtnEle.hasAttribute("ExtnBillToSuffix")) {
				billtoSuffix = fOrderExtnEle.getAttribute("ExtnBillToSuffix");
				if(YFCObject.isNull(billtoSuffix) || YFCObject.isVoid(billtoSuffix)) {
					throw new Exception("Attribute ExtnBillToSuffix Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ExtnBillToSuffix Not Available in Incoming Legacy Message!");
			}
			
			if(fOrderExtnEle.hasAttribute("ExtnShipToSuffix")) {
				shipToSuffix = fOrderExtnEle.getAttribute("ExtnShipToSuffix");
				if(YFCObject.isNull(shipToSuffix) || YFCObject.isVoid(shipToSuffix)) {
					throw new Exception("Attribute ExtnShipToSuffix Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute ExtnBillToSuffix Not Available in Incoming Legacy Message!");
			}
			
		} else {
			throw new Exception("Element Order/Extn Not Available in getOrderList Template!");
		}
		
		YFCElement editOrdExtnEle = editOrdEle.getChildElement("Extn");
		if(editOrdExtnEle != null) {
			if(editOrdExtnEle.hasAttribute("ExtnCustomerNo")) {
				String _custNo = editOrdExtnEle.getAttribute("ExtnCustomerNo");
				if(YFCObject.isNull(_custNo) || YFCObject.isVoid(_custNo)) {
					editOrdExtnEle.setAttribute("ExtnCustomerNo", custNo);
				}
			} else {
				editOrdExtnEle.setAttribute("ExtnCustomerNo", custNo);
			}
			
			if(editOrdExtnEle.hasAttribute("ExtnEnvtId")) {
				String _envId = editOrdExtnEle.getAttribute("ExtnEnvtId");
				if(YFCObject.isNull(_envId) || YFCObject.isVoid(_envId)) {
					editOrdExtnEle.setAttribute("ExtnEnvtId", envId);
				}
			} else {
				editOrdExtnEle.setAttribute("ExtnEnvtId", envId);
			}
			
			if(editOrdExtnEle.hasAttribute("ExtnCompanyId")) {
				String _compId = editOrdExtnEle.getAttribute("ExtnCompanyId");
				if(YFCObject.isNull(_compId) || YFCObject.isVoid(_compId)) {
					editOrdExtnEle.setAttribute("ExtnCompanyId", compId);
				}
			} else {
				editOrdExtnEle.setAttribute("ExtnCompanyId", compId);
			}
			
			if(editOrdExtnEle.hasAttribute("ExtnBillToSuffix")) {
				String _billToSuffix = editOrdExtnEle.getAttribute("ExtnBillToSuffix");
				if(YFCObject.isNull(_billToSuffix) || YFCObject.isVoid(_billToSuffix)) {
					editOrdExtnEle.setAttribute("ExtnBillToSuffix", billtoSuffix);
				}
			} else {
				editOrdExtnEle.setAttribute("ExtnBillToSuffix", billtoSuffix);
			}
			
			if(editOrdExtnEle.hasAttribute("ExtnShipToSuffix")) {
				String _shipToSuffix = editOrdExtnEle.getAttribute("ExtnShipToSuffix");
				if(YFCObject.isNull(_shipToSuffix) || YFCObject.isVoid(_shipToSuffix)) {
					editOrdExtnEle.setAttribute("ExtnShipToSuffix", shipToSuffix);
				}
			} else {
				editOrdExtnEle.setAttribute("ExtnShipToSuffix", shipToSuffix);
			}
		}
		if(editOrdEle.hasAttribute("EnterpriseCode")) {
			String _enterpriseCode = editOrdEle.getAttribute("EnterpriseCode");
			if(YFCObject.isNull(_enterpriseCode) || YFCObject.isVoid(_enterpriseCode)) {
				editOrdEle.setAttribute("EnterpriseCode", enterpriseCode);
			}
		} else {
			editOrdEle.setAttribute("EnterpriseCode", enterpriseCode);
		}
		
		if(editOrdEle.hasAttribute("EntryType")) {
			String _entryType = editOrdEle.getAttribute("EntryType");
			if(YFCObject.isNull(_entryType) || YFCObject.isVoid(_entryType)) {
				editOrdEle.setAttribute("EntryType", entryType);
			}
		} else {
			editOrdEle.setAttribute("EntryType", entryType);
		}
		
		if(YFCObject.isNull(shipNode) || YFCObject.isVoid(shipNode)) {
			YFCElement custInfoListEle = getCustomerInfo(env, buyerOrgCode);
			YFCElement custInfoEle = custInfoListEle.getChildElement("Customer");
			YFCElement custInfoExtnEle = custInfoEle.getChildElement("Extn");
			String _shipNode = null;
			String _envtId = null;
			if(custInfoExtnEle != null) {
				if(custInfoExtnEle.hasAttribute("ExtnShipFromBranch")) {
					_shipNode = custInfoExtnEle.getAttribute("ExtnShipFromBranch");
					if(YFCObject.isNull(_shipNode) || YFCObject.isVoid(_shipNode)) {
						if(custInfoExtnEle.hasAttribute("ExtnCustOrderBranch")) {
							_shipNode = custInfoExtnEle.getAttribute("ExtnCustOrderBranch");
							if(YFCObject.isNull(_shipNode) || YFCObject.isVoid(_shipNode)) {
								throw new Exception("Attribute ExtnCustOrderBranch Not Available in getCustomerList Template!;");
							}
						} else {
							throw new Exception("Attribute ExtnCustOrderBranch Not Available in getCustomerList Template!");
						}
					}
				}
				if(custInfoExtnEle.hasAttribute("ExtnEnvironmentCode")) {
					_envtId = custInfoExtnEle.getAttribute("ExtnEnvironmentCode");
					if(YFCObject.isNull(_envtId) || YFCObject.isVoid(_envtId)) {
						throw new Exception("Attribute ExtnEnvironmentCode Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute ExtnEnvironmentCode Not Available in getCustomerList Template!");
				}
				shipNode = XPXUtils.updateNodeSyntax(_envtId, _shipNode);
				editOrdEle.setAttribute("ShipNode", shipNode);
			} else {
				throw new Exception("Element Customer/Extn Not Available in getCustomerList Template!");
			}
			buyerOrgCustEle = custInfoEle;
		} else {
			editOrdEle.setAttribute("ShipNode", shipNode);
		}
		
		// To Stamp Web Hold Flag.
		if (editOrdEle.hasAttribute("MaxOrderStatus")) {
			String status = editOrdEle.getAttribute("MaxOrderStatus");
			if (!YFCObject.isNull(status) && !YFCObject.isVoid(status) && status.equalsIgnoreCase("1100.5450")) {
				editOrdExtnEle.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_FLAG, "Y");
				if (editOrdExtnEle.hasAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON)) {
					String webHoldReason = editOrdExtnEle.getAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON);
					if (YFCObject.isNull(webHoldReason) || YFCObject.isVoid(webHoldReason)) {
						editOrdExtnEle.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON, "Order Is On Status Web Hold.");
					}
				} else {
					editOrdExtnEle.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON, "Order Is On Status Web Hold.");
				}
			}
		}
		
		log.debug("");
		log.debug("After Stamping Mandatory Attributes:" + editOrdEle.getString());
	}
	
	private void validateWebHoldFlag(YFCElement chngfOrdEle, YFCElement buyerOrgCustEle) {
		String willCall = null;
		if(chngfOrdEle.hasAttribute("ExtnWillCall")) {
			willCall = chngfOrdEle.getAttribute("ExtnWillCall");
		}
		String ordUpdateFlag = null;
		if(buyerOrgCustEle != null) {
			YFCElement buyerOrgCustExtnEle = buyerOrgCustEle.getChildElement("Extn");
			if(buyerOrgCustExtnEle != null) {
				if(buyerOrgCustExtnEle.hasAttribute("ExtnOrderUpdateFlag")) {
					ordUpdateFlag = buyerOrgCustExtnEle.getAttribute("ExtnOrderUpdateFlag");
				}
			}
		}
		YFCElement chngfOrdLinesEle = chngfOrdEle.getChildElement("OrderLines");
		if(chngfOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = chngfOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement fOrdLineEle = (YFCElement)yfcItr.next();
				String lpc = fOrdLineEle.getAttribute("LineProcessCode");
				if (lpc.equalsIgnoreCase("A") || lpc.equalsIgnoreCase("C")) {
					YFCElement chngfOrdExtnEle = chngfOrdEle.getChildElement("Extn");
					if(chngfOrdExtnEle != null) {
						YFCNodeList<YFCElement> instructionList = fOrdLineEle.getElementsByTagName("Instruction");
						Iterator<YFCElement> itr = instructionList.iterator();
						while(itr.hasNext()) {
							YFCElement instructionEle = (YFCElement) itr.next();
							if(instructionEle.hasAttribute("InstructionType")) {
								String instType = instructionEle.getAttribute("InstructionType");
								if(!YFCObject.isNull(instType) && !YFCObject.isVoid(instType)) {
									if(instType.equalsIgnoreCase("LINE")) {
							
										chngfOrdExtnEle.setAttribute("ExtnWebHoldFlag", "Y");
										chngfOrdExtnEle.setAttribute("ExtnWebHoldReason", "Edit Order Message Had Order/Line Instructions of InstructionType-HEADER/LINE!");
										if(log.isDebugEnabled()){
											log.debug("IN EDIT CHAINED ORDER");
										}
										break;
									}
								}
							}
						}
						
						if(chngfOrdExtnEle.hasAttribute("ExtnWebHoldFlag")) {
							String webHoldFlag = chngfOrdExtnEle.getAttribute("ExtnWebHoldFlag");
							if(YFCObject.isNull(webHoldFlag) || YFCObject.isVoid(webHoldFlag)) {
								if(!YFCObject.isNull(willCall) && !YFCObject.isVoid(willCall) && 
										!YFCObject.isNull(ordUpdateFlag) && !YFCObject.isVoid(ordUpdateFlag)) {
									if(willCall.equalsIgnoreCase("P") && !ordUpdateFlag.equalsIgnoreCase("N")) {
										chngfOrdExtnEle.setAttribute("ExtnWebHoldReason", "Will Call requested");										
									} else if(!willCall.equalsIgnoreCase("P") && ordUpdateFlag.equalsIgnoreCase("N")){
										chngfOrdExtnEle.setAttribute("ExtnWebHoldReason", "New Customer Setup");
									} else if(willCall.equalsIgnoreCase("P") && ordUpdateFlag.equalsIgnoreCase("N")) {
										chngfOrdExtnEle.setAttribute("ExtnWebHoldReason", "Will Call requested & New Customer Setup");
									} else {
										chngfOrdExtnEle.setAttribute("ExtnWebHoldReason", "Order Is On Web Hold For Unknown Reason");
									}
									break;
								}
							} else {
								if(webHoldFlag.equalsIgnoreCase("N")) {
									if(!YFCObject.isNull(willCall) && !YFCObject.isVoid(willCall) && 
											!YFCObject.isNull(ordUpdateFlag) && !YFCObject.isVoid(ordUpdateFlag)) {
										if(willCall.equalsIgnoreCase("P") && !ordUpdateFlag.equalsIgnoreCase("N")) {
											chngfOrdExtnEle.setAttribute("ExtnWebHoldReason", "Will Call requested");
										} else if(!willCall.equalsIgnoreCase("P") && ordUpdateFlag.equalsIgnoreCase("N")){
											chngfOrdExtnEle.setAttribute("ExtnWebHoldReason", "New Customer Setup");
										} else if(willCall.equalsIgnoreCase("P") && ordUpdateFlag.equalsIgnoreCase("N")) {
											chngfOrdExtnEle.setAttribute("ExtnWebHoldReason", "Will Call requested & New Customer Setup");
										} else {
											chngfOrdExtnEle.setAttribute("ExtnWebHoldReason", "Order Is On Web Hold For Unknown Reason");
										}
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML) {
		
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();	
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		errorObject.setExceptionMessage(e.getMessage());
		ErrorLogger.log(errorObject, env);
	}
	
	private void setDefaultPC(YFCElement fOrderEle, List<String> alrdyDelLines, String pc) throws Exception {
		
		YFCElement fOrdLinesEle = fOrderEle.getChildElement("OrderLines");
		if(fOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = fOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement fOrdLineEle = (YFCElement)yfcItr.next();
				String ordLineKey = fOrdLineEle.getAttribute("OrderLineKey");
				if(pc.equalsIgnoreCase("A")) {
					fOrdLineEle.setAttribute("LineProcessCode", "A");
				}
				if(pc.equalsIgnoreCase("C")) {
				
					if(this.IsCancelledLine(fOrdLineEle)) {
						if(!YFCObject.isNull(ordLineKey) && !YFCObject.isVoid(ordLineKey)) {
							if(alrdyDelLines.contains(ordLineKey)) {
								fOrdLineEle.setAttribute("LineProcessCode", "_D");
							} else {
								fOrdLineEle.setAttribute("LineProcessCode", "D");
							}
						} else {
							throw new Exception("Attribute OrderLineKey Cannot be NULL or Void!");
						}
					} else if (hasLegacyLineNum(fOrdLineEle)){
						fOrdLineEle.setAttribute("LineProcessCode", "C");
					} else {
						fOrdLineEle.setAttribute("LineProcessCode", "A");
					}
				}
			}
			setHeaderProcessCode(fOrderEle);
		} else {
			throw new Exception("Element OrderLines Not Available in getOrderList Template!");
		}
	}
	
	private void setHeaderProcessCode(YFCElement fOrderEle) throws Exception {
		
		boolean hasLineA = false;
		boolean hasLineC = false;
		boolean hasLineD = false;
		
		YFCElement fOrdLinesEle = fOrderEle.getChildElement("OrderLines");
		if(fOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = fOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement fOrdLineEle = (YFCElement)yfcItr.next();
				if(fOrdLineEle.hasAttribute("LineProcessCode")) {
					String lpc = fOrdLineEle.getAttribute("LineProcessCode");
					if(YFCObject.isNull(lpc) || YFCObject.isVoid(lpc)) {
						throw new Exception("Attribute LineProcessCode Cannot be NULL or Void!");
					}
					if(lpc.equalsIgnoreCase("A")) {
						hasLineA = true;
					} else if(lpc.equalsIgnoreCase("C")) {
						hasLineC = true;
					} else if(lpc.equalsIgnoreCase("D")) {
						hasLineD = true;
					} else if(lpc.equalsIgnoreCase("_D")){
						// Do Nothing
					} else {
						throw new Exception("Invalid LineProcessCode Stamped!");
					}
				} else {
					throw new Exception("Could Not Determine HeaderProcessCode Since Line Has No LineProcessCode Stamped!");
				}
			}
			if(hasLineA && !hasLineC && !hasLineD) {
				fOrderEle.setAttribute("HeaderProcessCode", "A");
			} else if(!hasLineA && !hasLineC && hasLineD) {
				fOrderEle.setAttribute("HeaderProcessCode", "D");
			} else {
				fOrderEle.setAttribute("HeaderProcessCode", "C");
			}
		} else {
			throw new Exception("Element OrderLines Not Available in getOrderList Template!");
		}
	}
	
	private Map<String, YFCElement> processOrderEditC(YFSEnvironment env, 
			YFCElement editOrderEle, YFCElement fOrderEle, YFCElement cOrderEle) throws Exception {
		
		// Build input document to call ChangeOrder API.
		Document chngcOrdDoc = YFCDocument.createDocument().getDocument();
		chngcOrdDoc.appendChild(chngcOrdDoc.importNode(editOrderEle.getDOMNode(), true));
		chngcOrdDoc.renameNode(chngcOrdDoc.getDocumentElement(), chngcOrdDoc.getNamespaceURI(), "Order");
		YFCElement chngcOrderEle0 = YFCDocument.getDocumentFor(chngcOrdDoc).getDocumentElement();
		chngcOrderEle0.removeAttribute("OrderHeaderKey");

		// To remove order lines element from the document.
		YFCElement remEle = chngcOrderEle0.getChildElement("OrderLines");
		if(remEle != null) {
			chngcOrderEle0.removeChild(remEle);
		} 
		
		YFCElement chngcOOrdLinesEle0 = chngcOrderEle0.getOwnerDocument().createElement("OrderLines");
		chngcOrderEle0.appendChild(chngcOOrdLinesEle0);
		
		YFCElement chngcOrderEle1 = YFCDocument.getDocumentFor("<Order><OrderLines/></Order>").getDocumentElement();
		
		String tranId0 = null;
		if(this._prop != null) {
			tranId0 = this._prop.getProperty("XPX_CHN_CRT_STATUS_TXN");
			if(YFCObject.isNull(tranId0) || YFCObject.isVoid(tranId0)) {
				throw new Exception("Transaction Not Configured in API Arguments!");
			}
		} else {
			throw new Exception("Arguments Not Configured For API-XPXPerformLegacyOrderUpdateAPI!");
		}
		
		YFCDocument chngcOrdStatusInXML0 = YFCDocument.getDocumentFor("<OrderStatusChange/>");
		YFCElement chngcOrdStatusEle0 = chngcOrdStatusInXML0.getDocumentElement();
		chngcOrdStatusEle0.setAttribute("IgnoreTransactionDependencies", "Y");
		chngcOrdStatusEle0.setAttribute("TransactionId", tranId0);
		chngcOrdStatusEle0.setAttribute("BaseDropStatus", "1100.0100");
		
		YFCElement chngCOStatusLinesEle0 = chngcOrdStatusEle0.getOwnerDocument().createElement("OrderLines");
		chngcOrdStatusEle0.appendChild(chngCOStatusLinesEle0);
		
		// To set the transaction Id.
		String tranId1 = null;
		if(this._prop != null) {
			tranId1 = this._prop.getProperty("XPX_CHN_CNL_STATUS_TXN");
			if(YFCObject.isNull(tranId1) || YFCObject.isVoid(tranId1)) {
				throw new Exception("TransactionID Not Configured in API Arguments!");
			}
		} else {
			throw new Exception("Arguments Not Configured For API-XPXPerformLegacyOrderUpdateAPI!");
		}
		
		// Build input document to call ChangeOrderStatus API.
		YFCDocument chngcOrdStatusInXML1 = YFCDocument.getDocumentFor("<OrderStatusChange/>");
		YFCElement chngcOrdStatusEle1 = chngcOrdStatusInXML1.getDocumentElement();
		chngcOrdStatusEle1.setAttribute("IgnoreTransactionDependencies", "Y");
		chngcOrdStatusEle1.setAttribute("TransactionId", tranId1);
		chngcOrdStatusEle1.setAttribute("BaseDropStatus", "1100.0010");
		
		YFCElement chngCOStatusLinesEle1 = chngcOrdStatusEle1.getOwnerDocument().createElement("OrderLines");
		chngcOrdStatusEle1.appendChild(chngCOStatusLinesEle1);
		
		if (log.isDebugEnabled()) {
			log.debug("fOrderEle_OrderType:"+fOrderEle.getAttribute("OrderType"));
			log.debug("cOrderEle_OrderType:"+cOrderEle.getAttribute("OrderType"));
		}
		
		preparefOChange(env, chngcOrderEle0, chngcOrderEle1, chngcOrdStatusEle0, chngcOrdStatusEle1, editOrderEle, fOrderEle, cOrderEle);
		Map<String, String> chnOrdCrtMap = this.getChainedOrderCreatedQtyOnCO(chngcOrdStatusEle0);
		this.adjustChainedOrderCreatedQtyOnCO(chngcOrderEle1, chnOrdCrtMap);
		
		// Updates customer and fulfillment order.
		Map<String, YFCElement> retDocMap = updatefOrder(env, chngcOrderEle0, chngcOrdStatusEle0, chngcOrdStatusEle1, editOrderEle, chngcOrderEle1, cOrderEle, fOrderEle);
		
		return retDocMap; 

	}
	
	private Map<String, String> getChainedOrderCreatedQtyOnCO(YFCElement chngcOrdStatusEle0) throws Exception {
		Map<String, String> newQtyMap = new HashMap<String, String>();
		if(chngcOrdStatusEle0.hasAttribute("OrderHeaderKey")) {
			YFCElement chngcOrdLinesStatusEle0 = chngcOrdStatusEle0.getChildElement("OrderLines");
			YFCIterable<YFCElement> yfcItr = chngcOrdLinesStatusEle0.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement chngcOrdLineStatusEle0 = (YFCElement)yfcItr.next();
				String ordLineKey = null;
				String qty = null;
				if(chngcOrdLineStatusEle0.hasAttribute("OrderLineKey")) {
					ordLineKey = chngcOrdLineStatusEle0.getAttribute("OrderLineKey");
					if(YFCObject.isNull(ordLineKey) || YFCObject.isVoid(ordLineKey)) {
						throw new Exception("Attribute OrderLineKey Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute OrderLineKey Not Available in changeOrderStatus API!");
				}
				YFCElement chngcOrdLineStatusTranQtyEle0 = chngcOrdLineStatusEle0.getChildElement("OrderLineTranQuantity");
				if(chngcOrdLineStatusTranQtyEle0 != null) {
					if(chngcOrdLineStatusTranQtyEle0.hasAttribute("Quantity")) {
						qty = chngcOrdLineStatusTranQtyEle0.getAttribute("Quantity");
						if(YFCObject.isNull(qty) || YFCObject.isVoid(qty)) {
							throw new Exception("Attribute Quantity Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute Quantity Not Available in changeOrderStatus API!");
					}
				}
				newQtyMap.put(ordLineKey, qty);
			}
		}
		return newQtyMap;
	}

	private void adjustChainedOrderCreatedQtyOnCO(YFCElement chngcOrderEle1, Map<String, String> chnOrdCrtMap) throws Exception {
		if(chngcOrderEle1.hasAttribute("OrderHeaderKey")) {
			YFCElement chngcOrdLinesEle1 = chngcOrderEle1.getChildElement("OrderLines");
			YFCIterable<YFCElement> yfcItr = chngcOrdLinesEle1.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement chngcOrdLineEle1 = (YFCElement)yfcItr.next();
				String ordLineKey = null;
				if(chngcOrdLineEle1.hasAttribute("OrderLineKey")) {
					ordLineKey = chngcOrdLineEle1.getAttribute("OrderLineKey");
					if(YFCObject.isNull(ordLineKey) || YFCObject.isVoid(ordLineKey)) {
						throw new Exception("Attribute OrderLineKey Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute OrderLineKey Not Available in changeOrderStatus API!");
				}
				String qty = null;
				YFCElement chngcOrdLineTranQtyEle1 = chngcOrdLineEle1.getChildElement("OrderLineTranQuantity");
				if(chngcOrdLineTranQtyEle1 != null) {
					if(chngcOrdLineTranQtyEle1.hasAttribute("OrderedQty")) {
						qty = chngcOrdLineTranQtyEle1.getAttribute("OrderedQty");
						if(YFCObject.isNull(qty) || YFCObject.isVoid(qty)) {
							throw new Exception("Attribute OrderLineTranQuantity/@OrderedQty Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute OrderLineTranQuantity/@OrderedQty Not Available in changeOrderStatus API!");
					}
					String newQty = (String)chnOrdCrtMap.get(ordLineKey);
					if(!YFCObject.isNull(newQty) && !YFCObject.isNull(newQty)) {
						Float finalQty = Float.parseFloat(qty) + Float.parseFloat(newQty);
						chngcOrdLineTranQtyEle1.setAttribute("OrderedQty", finalQty.toString());
					}
				}
			}
		}
	}

	private Map<String, YFCElement> processOrderEditA(YFSEnvironment env, YFCElement editOrderEle, 
			YFCElement fOrderEle, YFCElement cOrderEle, YFCElement buyerOrgCustEle) throws Exception {
		Map<String, YFCElement> retDocMap = new HashMap<String, YFCElement>();
		
		// To set the order header attributes of the customer order to the legacy input xml. 
		this.setOrderHeaderAttributes(env, editOrderEle, cOrderEle, buyerOrgCustEle);
		
		Document chngcOrderDoc = YFCDocument.createDocument().getDocument();
		chngcOrderDoc.appendChild(chngcOrderDoc.importNode(editOrderEle.getDOMNode(), true));
		chngcOrderDoc.renameNode(chngcOrderDoc.getDocumentElement(), chngcOrderDoc.getNamespaceURI(), "Order");
		YFCElement chngcOrderEle = YFCDocument.getDocumentFor(chngcOrderDoc).getDocumentElement();
		
		if(cOrderEle != null) {
			// Updates the customer order.
			cOrderEle = updateCustomerOrder(env, chngcOrderEle, cOrderEle).getDocumentElement();
			retDocMap.put("CO", cOrderEle);
		} else {
			throw new Exception("No Customer Order Details Returned!");
		}
		// To create fulfillment order.
		YFCDocument postToLegacyOrdDoc = createFulfillmentOrder(env, editOrderEle, cOrderEle);
		retDocMap.put("FO", postToLegacyOrdDoc.getDocumentElement());
		
		return retDocMap;
	}
	
	private Map<String, YFCElement> prepareChngfOrderFromcOrder(YFSEnvironment env, YFCElement editOrdEle, 
			YFCElement cOrderEle, YFCElement cAndfOrdListEle, YFCElement buyerOrgCustEle) throws Exception {
		
		Map<String, YFCElement> chngfOrders = new HashMap<String, YFCElement>();
		Map<String, YFCElement> tempMap = new HashMap<String, YFCElement>();
		
		String cShipToId = "";
		if(cOrderEle.hasAttribute("ShipToID")) {
			cShipToId = cOrderEle.getAttribute("ShipToID");
			if(YFCObject.isNull(cShipToId) || YFCObject.isVoid(cShipToId)) {
				throw new Exception("Attribute ShipToID Cannot be NULL or Void in Fulfillment Order!");
			}
		} else {
			throw new Exception("Attribute ShipToID Not Available in getOrderList Template!");
		}
		
		String entryType = null;
		if(editOrdEle.hasAttribute("EntryType")) {
			entryType = editOrdEle.getAttribute("EntryType");
			if(YFCObject.isNull(entryType) || YFCObject.isVoid(entryType)) {
				throw new Exception("Attribute EntryType Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute EntryType Not Available in Edit Message!");
		}
		
		String shipNode = "";
		if(editOrdEle.hasAttribute("ShipNode")) {
			shipNode = editOrdEle.getAttribute("ShipNode");
		}
		
		String envtId = null;
		YFCElement editOrdExtnEle = editOrdEle.getChildElement("Extn");
		if(editOrdExtnEle != null) {
			if(editOrdExtnEle.hasAttribute("ExtnEnvtId")) {
				envtId = editOrdExtnEle.getAttribute("ExtnEnvtId");
				if(YFCObject.isNull(envtId) || YFCObject.isVoid(envtId)) {
					throw new Exception("Attribute ExtnEnvtId Cannot be NULL or Void in Edit Order InXML!");
				}
			} else {
				throw new Exception("Attribute ExtnEnvtId Not Available in Edit Order InXML!");
			}
		} else {
			throw new Exception("Element Extn Not Available in Edit Order InXML!");
		}
		
		YFCElement editOrdLinesEle = editOrdEle.getChildElement("OrderLines");
		YFCIterable<YFCElement> yfcItr = editOrdLinesEle.getChildren("OrderLine");
		while(yfcItr.hasNext()) {
			YFCElement editOrdLineEle = (YFCElement)yfcItr.next();
			String action = null;
			String cOrdLineKey = null;
			if(editOrdLineEle.hasAttribute("Action")) {
				action = editOrdLineEle.getAttribute("Action");
				cOrdLineKey = editOrdLineEle.getAttribute("OrderLineKey");
				if(!YFCObject.isNull(action) && !YFCObject.isVoid(action)) {
					if(action.equalsIgnoreCase("CREATE")) {
						YFCElement editOrdLineExtnEle = editOrdLineEle.getChildElement("Extn");
						if(editOrdLineExtnEle != null) {
							if(editOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
								String _webLineNo = editOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
								if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
									String webLineNo = this.setWebLineNo(env, entryType, envtId);
									editOrdLineExtnEle.setAttribute("ExtnWebLineNumber", webLineNo);
								}
							} else {
								String webLineNo = this.setWebLineNo(env, entryType, envtId);
								editOrdLineExtnEle.setAttribute("ExtnWebLineNumber", webLineNo);
							}
						} else {
							throw new Exception("Element OrderLine/Extn Not Available in Edit Order Message!");
						}
					}
					if(action.equalsIgnoreCase("CREATE") && 
							(YFCObject.isNull(cOrdLineKey) || YFCObject.isVoid(cOrdLineKey))) {
						String lineType = null;
						if(editOrdLineEle.hasAttribute("LineType")) {
							lineType = editOrdLineEle.getAttribute("LineType");
							if(YFCObject.isNull(lineType) || YFCObject.isVoid(lineType)) {
								throw new Exception("Attribute LineType Cannot be NULL or Void in Edit Order InXML!");
							}
						} else {
							throw new Exception("Attribute LineType Not Available in Edit Order InXML!");
						}
						
						String itemId = null;
						YFCElement editOrdLineItemEle = editOrdLineEle.getChildElement("Item");
						if(editOrdLineItemEle != null) {
							if(editOrdLineItemEle.hasAttribute("ItemID")) {
								itemId = editOrdLineItemEle.getAttribute("ItemID");
								if(YFCObject.isNull(itemId) || YFCObject.isVoid(itemId)) {
									throw new Exception("Attribute ItemID Cannot be NULL or Void in Edit Order InXML!");
								}
							} else {
								throw new Exception("Attribute ItemID Not Available in Edit Order InXML!");
							}
						} else {
							throw new Exception("Element OrderLine/Item Not Available in Edit Order InXML!");
						}
						
						String allowDirectOrderFlag = null;
						if(buyerOrgCustEle != null) {
							YFCElement buyerOrgCustExtnEle = buyerOrgCustEle.getChildElement("Extn");
							if(buyerOrgCustExtnEle != null) {
								if(buyerOrgCustExtnEle.hasAttribute("ExtnAllowDirectOrderFlag")) {
									allowDirectOrderFlag = buyerOrgCustExtnEle.getAttribute("ExtnAllowDirectOrderFlag");
									if(YFCObject.isNull(allowDirectOrderFlag) || YFCObject.isVoid(allowDirectOrderFlag)) {
										allowDirectOrderFlag = "N";
									}
								} else {
									throw new Exception("Attribute ExtnAllowDirectOrderFlag Not Available in getCustomerList Template!");
								}
							} else {
								throw new Exception("Element Customer/Extn Not Available in getCustomerList Template!");
							}
						} else {
							allowDirectOrderFlag = XPXUtils.getAllowDirectOrderForShipToCust(env, cShipToId);
							if(YFCObject.isNull(allowDirectOrderFlag) || YFCObject.isVoid(allowDirectOrderFlag)) {
								allowDirectOrderFlag = "N";
							}
						}
						
						String stockType = null;
						String _ordType = null;
						YFCElement _fOrderEle = null;
						if(allowDirectOrderFlag.equalsIgnoreCase("Y")) {
							stockType = getInventoryIndicator(env, itemId, shipNode, envtId, lineType);
							if(YFCObject.isNull(stockType) || YFCObject.isVoid(stockType)) {
								throw new Exception("Inventory Indicator For Parameters["+itemId+","+shipNode+","+envtId+","+lineType+"] Returns NULL or Void!");
							}
							_ordType = stockType+"_ORDER";
							_fOrderEle = getfOrderEleByOrderType(_ordType, cAndfOrdListEle);
						} else {
							_fOrderEle = this.getfOrderEleFromList(cAndfOrdListEle);
							_ordType = _fOrderEle.getAttribute("OrderType");
							if(YFCObject.isNull(_ordType) || YFCObject.isVoid(_ordType)) {
								throw new Exception("Attribute OrderType Cannot be NULL or Void!");
							}
							stockType = _ordType.substring(0, _ordType.indexOf("_"));
						}
						Set<String> keys = tempMap.keySet();
						if(_fOrderEle != null) {
							String fOrdHeaderKey = this.getOrdHeaderKey(_fOrderEle);
							if(YFCObject.isNull(fOrdHeaderKey) || YFCObject.isVoid(fOrdHeaderKey)) {
								throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
							}
							String fLegacyOrdNo = this.getLegacyOrderNo(_fOrderEle);
							if(keys.contains(_ordType) || keys.contains(fOrdHeaderKey)) {
								YFCElement chngfOrdEle = null;
								if(keys.contains(fOrdHeaderKey)) {
									chngfOrdEle = (YFCElement)tempMap.get(fOrdHeaderKey);
								} else {
									chngfOrdEle = (YFCElement)tempMap.get(_ordType);
								}
								YFCElement chngfOrdLinesEle = chngfOrdEle.getChildElement("OrderLines");
								YFCElement chngfOrdLineEle = chngfOrdEle.getOwnerDocument().importNode(editOrdLineEle, true);
								chngfOrdLinesEle.appendChild(chngfOrdLineEle);
								chngfOrdLineEle.setAttribute("LineProcessCode", "A");
							} else {
								YFCElement chngfOrdEle = getOrderHeaderInfo(editOrdEle);
								chngfOrdEle.setAttribute("OrderHeaderKey", fOrdHeaderKey);
								chngfOrdEle.setAttribute("HeaderProcessCode", "A");
								chngfOrdEle.setAttribute("OrderType", _ordType);
								
								YFCElement chngfOrdExtnEle = chngfOrdEle.getChildElement("Extn");
								if(chngfOrdExtnEle != null) {
									chngfOrdExtnEle.setAttribute("ExtnLegacyOrderNo", fLegacyOrdNo);
								}
								
								YFCElement chngfOrdLinesEle = chngfOrdEle.getChildElement("OrderLines");
								YFCElement chngfOrdLineEle = chngfOrdEle.getOwnerDocument().importNode(editOrdLineEle, true);
								chngfOrdLinesEle.appendChild(chngfOrdLineEle);
								chngfOrdLineEle.setAttribute("LineProcessCode", "A");
								
								if(keys.contains(fOrdHeaderKey)) {
									tempMap.put(fOrdHeaderKey, chngfOrdEle);
								} else {
									tempMap.put(_ordType, chngfOrdEle);
								}
								chngfOrders.put("CC", chngfOrdEle);
							}
						} else {
							if(keys.contains(_ordType)) {
								YFCElement chngfOrdEle = (YFCElement)tempMap.get(_ordType);
								
								YFCElement chngfOrdLinesEle = chngfOrdEle.getChildElement("OrderLines");
								YFCElement chngfOrdLineEle = chngfOrdEle.getOwnerDocument().importNode(editOrdLineEle, true);
								chngfOrdLinesEle.appendChild(chngfOrdLineEle);
								chngfOrdLineEle.setAttribute("LineProcessCode", "A");
								
								YFCElement chngfOrdLineExtnEle = chngfOrdLineEle.getChildElement("Extn");
								if(chngfOrdLineExtnEle != null) {
									chngfOrdLineExtnEle.setAttribute("ExtnLineType", stockType);
								}
								
							} else {
								YFCElement chngfOrdEle = getOrderHeaderInfo(editOrdEle);
								chngfOrdEle.setAttribute("HeaderProcessCode", "A");
								chngfOrdEle.setAttribute("OrderType", _ordType);
								
								YFCElement chngfOrdLinesEle = chngfOrdEle.getChildElement("OrderLines");
								YFCElement chngfOrdLineEle = chngfOrdEle.getOwnerDocument().importNode(editOrdLineEle, true);
								chngfOrdLinesEle.appendChild(chngfOrdLineEle);
								chngfOrdLineEle.setAttribute("LineProcessCode", "A");
								
								YFCElement chngfOrdLineExtnEle = chngfOrdLineEle.getChildElement("Extn");
								if(chngfOrdLineExtnEle != null) {
									chngfOrdLineExtnEle.setAttribute("ExtnLineType", stockType);
								}
								
								tempMap.put(_ordType, chngfOrdEle);
								chngfOrders.put("AA", chngfOrdEle);
							}
						}
					} else {
						// Not a New Line
						this.preparefOrders(editOrdEle, editOrdLineEle, cAndfOrdListEle, tempMap, chngfOrders);
					}
				} else {
					// Not a New Line
					this.preparefOrders(editOrdEle, editOrdLineEle, cAndfOrdListEle, tempMap, chngfOrders);
				}
			} else {
				// Not a New Line
				this.preparefOrders(editOrdEle, editOrdLineEle, cAndfOrdListEle, tempMap, chngfOrders);
			}
		}
		
		return chngfOrders;
	}
	
	private void preparefOrders(YFCElement editOrdEle, YFCElement editOrdLineEle, 
			YFCElement cAndfOrdListEle, Map<String, YFCElement> tempMap, Map<String, YFCElement> chngfOrders) throws Exception {
		
		String ordLineKey = null;
		String webLineNo = null;
		if(editOrdLineEle.hasAttribute("OrderLineKey")) {
			ordLineKey = editOrdLineEle.getAttribute("OrderLineKey");
			if(YFCObject.isNull(ordLineKey) || YFCObject.isVoid(ordLineKey)) {
				YFCElement editOrdLineExtnEle = editOrdLineEle.getChildElement("Extn");
				if(editOrdLineExtnEle != null) {
					if(editOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
						webLineNo = editOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
						if(YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
							throw new Exception("One or More Mandatory Attributes[OrderLineKey, WebLineNo] Not Availble in Incoming Edit Message!");
						}
					}
				} else {
					throw new Exception("Element Extn Not Available in Incoming Edit Message!");
				}
			}
		}
		
		String ordType = null;
		String fOrdHeaderKey = null;
		String fLegacyOrdNo = null;
		String fOrdLineKey = null;
		String fLegacyLineNo = null;
		if(!YFCObject.isNull(ordLineKey) && !YFCObject.isVoid(ordLineKey)) {
			String chnFromKeys = this.getChainedFromOrderKeys(ordLineKey, cAndfOrdListEle);
			if(YFCObject.isNull(chnFromKeys) || YFCObject.isVoid(chnFromKeys)) {
				throw new Exception("Incoming Edit Line [OrderLineKey:"+ordLineKey+" | WebLineNo:"+webLineNo+"] Not Chained To any Fulfillment Order Line!");
			}
			StringTokenizer st = new StringTokenizer(chnFromKeys,"|");
			while(st.hasMoreElements()) {
				ordType = (String)st.nextToken();
				fOrdHeaderKey = (String)st.nextToken();
				fLegacyOrdNo = (String)st.nextToken();
				fOrdLineKey = (String)st.nextToken();
				fLegacyLineNo = (String)st.nextToken();
			}
		} else {
			String chnFromKeys = this.getChainedFromOrderKeysForWebLineNo(webLineNo, cAndfOrdListEle);
			if(YFCObject.isNull(chnFromKeys) || YFCObject.isVoid(chnFromKeys)) {
				throw new Exception("Incoming Edit Line [OrderLineKey:"+ordLineKey+" | WebLineNo:"+webLineNo+"] Not Chained To any Fulfillment Order Line!");
			}
			StringTokenizer st = new StringTokenizer(chnFromKeys,"|");
			while(st.hasMoreElements()) {
				ordType = (String)st.nextToken();
				fOrdHeaderKey = (String)st.nextToken();
				fLegacyOrdNo = (String)st.nextToken();
				fOrdLineKey = (String)st.nextToken();
				fLegacyLineNo = (String)st.nextToken();
			}
		}
		
		
		Set<String> keys = tempMap.keySet();
		if(keys.contains(fOrdHeaderKey)) {
			YFCElement chngfOrdEle = (YFCElement)tempMap.get(fOrdHeaderKey);
			YFCElement chngfOrdLinesEle = chngfOrdEle.getChildElement("OrderLines");
			YFCElement chngfOrdLineEle = chngfOrdEle.getOwnerDocument().importNode(editOrdLineEle, true);
			chngfOrdLinesEle.appendChild(chngfOrdLineEle);
			chngfOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);
			chngfOrdLineEle.setAttribute("LineProcessCode", "C");
			
			YFCElement chngfOrdLineExtnEle = chngfOrdLineEle.getChildElement("Extn");
			if(chngfOrdLineExtnEle != null) {
				chngfOrdLineExtnEle.setAttribute("ExtnLegacyLineNumber", fLegacyLineNo);
			}
			
		} else {
			YFCElement chngfOrdEle = getOrderHeaderInfo(editOrdEle);
			chngfOrdEle.setAttribute("OrderHeaderKey", fOrdHeaderKey);
			chngfOrdEle.setAttribute("HeaderProcessCode", "C");
			chngfOrdEle.setAttribute("OrderType", ordType);
			
			YFCElement chngfOrdExtnEle = chngfOrdEle.getChildElement("Extn");
			if(chngfOrdExtnEle != null) {
				chngfOrdExtnEle.setAttribute("ExtnLegacyOrderNo", fLegacyOrdNo);
			}
			
			YFCElement chngfOrdLinesEle = chngfOrdEle.getChildElement("OrderLines");
			YFCElement chngfOrdLineEle = chngfOrdEle.getOwnerDocument().importNode(editOrdLineEle, true);
			chngfOrdLinesEle.appendChild(chngfOrdLineEle);
			chngfOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);
			chngfOrdLineEle.setAttribute("LineProcessCode", "C");
			
			YFCElement chngfOrdLineExtnEle = chngfOrdLineEle.getChildElement("Extn");
			if(chngfOrdLineExtnEle != null) {
				chngfOrdLineExtnEle.setAttribute("ExtnLegacyLineNumber", fLegacyLineNo);
			}
			
			tempMap.put(fOrdHeaderKey, chngfOrdEle);
			chngfOrders.put("CC", chngfOrdEle);
		}
	}
	
	private YFCElement getOrderHeaderInfo(YFCElement editOrdEle) {
		
		Document chngcOrderDoc = YFCDocument.createDocument().getDocument();
		chngcOrderDoc.appendChild(chngcOrderDoc.importNode(editOrdEle.getDOMNode(), true));
		chngcOrderDoc.renameNode(chngcOrderDoc.getDocumentElement(), chngcOrderDoc.getNamespaceURI(), "Order");
		YFCElement chngcOrderEle = YFCDocument.getDocumentFor(chngcOrderDoc).getDocumentElement();
		YFCElement chngcOrdLinesEle = chngcOrderEle.getChildElement("OrderLines");
		if(chngcOrdLinesEle != null) {
			chngcOrderEle.removeChild(chngcOrdLinesEle);
		}
		chngcOrdLinesEle = chngcOrderEle.getOwnerDocument().createElement("OrderLines");
		chngcOrderEle.appendChild(chngcOrdLinesEle);
		
		return chngcOrderEle;
	}
	
	private Map<String, YFCElement> prepareChngfOrderFromfOrder(YFSEnvironment env, YFCElement editOrdEle, 
			YFCElement fOrderEle, YFCElement cAndfOrdListEle, YFCElement buyerOrgCustEle) throws Exception {
		
		Map<String, YFCElement> chngfOrders = new HashMap<String, YFCElement>();
		
		String fShipToId = "";
		if(fOrderEle.hasAttribute("ShipToID")) {
			fShipToId = fOrderEle.getAttribute("ShipToID");
			if(YFCObject.isNull(fShipToId) || YFCObject.isVoid(fShipToId)) {
				throw new Exception("Attribute ShipToID Cannot be NULL or Void in Fulfillment Order!");
			}
		} else {
			throw new Exception("Attribute ShipToID Not Available in getOrderList Template!");
		}
		
		Document chngfOrder0 = YFCDocument.createDocument().getDocument();
		chngfOrder0.appendChild(chngfOrder0.importNode(editOrdEle.getDOMNode(), true));
		chngfOrder0.renameNode(chngfOrder0.getDocumentElement(), chngfOrder0.getNamespaceURI(), "Order");
		YFCElement chngfOrdEle0 = YFCDocument.getDocumentFor(chngfOrder0).getDocumentElement();
		chngfOrdEle0.setAttribute("HeaderProcessCode", "C");
		
		YFCElement chngfOrdEle1 = YFCDocument.getDocumentFor("<Order><Extn ExtnLegacyOrderNo='' ExtnWebConfNum=''/><OrderLines/></Order>").getDocumentElement();
		chngfOrdEle1.setAttribute("HeaderProcessCode", "C");
		YFCElement chngfOrdLinesEle1 = chngfOrdEle1.getChildElement("OrderLines");
		
		Document createfOrd0 = YFCDocument.createDocument().getDocument();
		createfOrd0.appendChild(createfOrd0.importNode(editOrdEle.getDOMNode(), true));
		createfOrd0.renameNode(createfOrd0.getDocumentElement(), createfOrd0.getNamespaceURI(), "Order");
		YFCElement createfOrdEle0 = YFCDocument.getDocumentFor(createfOrd0).getDocumentElement();
		createfOrdEle0.setAttribute("HeaderProcessCode", "A");
		
		YFCElement createfOrdLinesEle0 = createfOrdEle0.getChildElement("OrderLines");
		createfOrdEle0.removeChild(createfOrdLinesEle0);
		createfOrdLinesEle0 = createfOrdEle0.getOwnerDocument().createElement("OrderLines");
		createfOrdEle0.appendChild(createfOrdLinesEle0);
		
		String ordType = null;
		if(editOrdEle.hasAttribute("OrderType")) {
			ordType = editOrdEle.getAttribute("OrderType");
			if(YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
				throw new Exception("Attribute OrderType Canno be NULL or Void in Edit Order InXML!");
			}
		} else {
			throw new Exception("Attribute OrderType Not Available in Edit Order InXML!");
		}
		
		String shipNode = "";
		if(editOrdEle.hasAttribute("ShipNode")) {
			shipNode = editOrdEle.getAttribute("ShipNode");
			if(YFCObject.isNull(shipNode) || YFCObject.isVoid(shipNode)) {
				shipNode = "";
			}
		}
		
		String envtId = null;
		YFCElement chngfOrdExtnEle0 = chngfOrdEle0.getChildElement("Extn");
		if(chngfOrdExtnEle0 != null) {
			if(chngfOrdExtnEle0.hasAttribute("ExtnEnvtId")) {
				envtId = chngfOrdExtnEle0.getAttribute("ExtnEnvtId");
				if(YFCObject.isNull(envtId) || YFCObject.isVoid(envtId)) {
					throw new Exception("Attribute ExtnEnvtId Cannot be NULL or Void in Edit Order InXML!");
				}
			} else {
				throw new Exception("Attribute ExtnEnvtId Not Available in Edit Order InXML!");
			}
		} else {
			throw new Exception("Element Extn Not Available in Edit Order InXML!");
		}
		
		String entryType = null;
		if(chngfOrdEle0.hasAttribute("EntryType")) {
			entryType = chngfOrdEle0.getAttribute("EntryType");
			if(YFCObject.isNull(entryType) || YFCObject.isVoid(entryType)) {
				throw new Exception("Attribute EntryType Cannot be NULL or Void!");
			}
		} else {
			throw new Exception("Attribute EntryType Not Available in Edit Message!");
		}
		
		YFCElement chngfOrdLinesEle0 = chngfOrdEle0.getChildElement("OrderLines");
		if(chngfOrdLinesEle0 != null) {
			YFCIterable<YFCElement> yfcItr = chngfOrdLinesEle0.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				String lineType = null;
				YFCElement chngfOrdLineEle0 = (YFCElement)yfcItr.next();
				if(chngfOrdLineEle0.hasAttribute("LineType")) {
					lineType = chngfOrdLineEle0.getAttribute("LineType");
					if(YFCObject.isNull(lineType) || YFCObject.isVoid(lineType)) {
						throw new Exception("Attribute LineType Cannot be NULL or Void in Edit Order InXML!");
					}
				} else {
					throw new Exception("Attribute LineType Not Available in Edit Order InXML!");
				}
				
				String itemId = null;
				YFCElement chngfOrdLineItemEle = chngfOrdLineEle0.getChildElement("Item");
				if(chngfOrdLineItemEle != null) {
					if(chngfOrdLineItemEle.hasAttribute("ItemID")) {
						itemId = chngfOrdLineItemEle.getAttribute("ItemID");
						if(YFCObject.isNull(itemId) || YFCObject.isVoid(itemId)) {
							throw new Exception("Attribute ItemID Cannot be NULL or Void in Edit Order InXML!");
						}
					} else {
						throw new Exception("Attribute ItemID Not Available in Edit Order InXML!");
					}
				} else {
					throw new Exception("Element OrderLine/Item Not Available in Edit Order InXML!");
				}
				
				String action = null;
				String fOrdLineKey = null;
				if(chngfOrdLineEle0.hasAttribute("Action")) {
					action = chngfOrdLineEle0.getAttribute("Action");
					fOrdLineKey = chngfOrdLineEle0.getAttribute("OrderLineKey");
					if(!YFCObject.isNull(action) && !YFCObject.isVoid(action)) {
						if(action.equalsIgnoreCase("CREATE")) {
							YFCElement chngfOrdLineExtnEle0 = chngfOrdLineEle0.getChildElement("Extn");
							if(chngfOrdLineExtnEle0 != null) {
								if(chngfOrdLineExtnEle0.hasAttribute("ExtnWebLineNumber")) {
									String _webLineNo = chngfOrdLineExtnEle0.getAttribute("ExtnWebLineNumber");
									if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
										String webLineNo = this.setWebLineNo(env, entryType, envtId);
										chngfOrdLineExtnEle0.setAttribute("ExtnWebLineNumber", webLineNo);
									}
								} else {
									String webLineNo = this.setWebLineNo(env, entryType, envtId);
									chngfOrdLineExtnEle0.setAttribute("ExtnWebLineNumber", webLineNo);
								}
							} else {
								throw new Exception("Element OrderLine/Extn Not Available in Edit Order Message!");
							}
						}
						if(action.equalsIgnoreCase("CREATE") && 
								(YFCObject.isNull(fOrdLineKey) || YFCObject.isVoid(fOrdLineKey))) {
							String allowDirectOrderFlag = null;
							if(buyerOrgCustEle != null) {
								YFCElement buyerOrgCustExtnEle = buyerOrgCustEle.getChildElement("Extn");
								if(buyerOrgCustExtnEle != null) {
									if(buyerOrgCustExtnEle.hasAttribute("ExtnAllowDirectOrderFlag")) {
										allowDirectOrderFlag = buyerOrgCustExtnEle.getAttribute("ExtnAllowDirectOrderFlag");
										if(YFCObject.isNull(allowDirectOrderFlag) || YFCObject.isVoid(allowDirectOrderFlag)) {
											allowDirectOrderFlag = "N";
										}
									} else {
										throw new Exception("Attribute ExtnAllowDirectOrderFlag Not Available in getCustomerList Template!");
									}
								} else {
									throw new Exception("Element Customer/Extn Not Available in getCustomerList Template!");
								}
							} else {
								allowDirectOrderFlag = XPXUtils.getAllowDirectOrderForShipToCust(env, fShipToId);
								if(YFCObject.isNull(allowDirectOrderFlag) || YFCObject.isVoid(allowDirectOrderFlag)) {
									allowDirectOrderFlag = "N";
								}
							}
							
							if (log.isDebugEnabled()) {
								log.debug("AllowDirectOrderFlag:"+allowDirectOrderFlag);
							}
							
							if(allowDirectOrderFlag.equalsIgnoreCase("Y")) {
								String stockType = getInventoryIndicator(env, itemId, shipNode, envtId, lineType);
								if(YFCObject.isNull(stockType) || YFCObject.isVoid(stockType)) {
									throw new Exception("Inventory Indicator For Parameters" +
											"["+itemId+","+shipNode+","+envtId+","+lineType+"] Returns NULL or Void!");
								}
								String _ordType = stockType+"_ORDER";
								
								if (log.isDebugEnabled()) {
									log.debug("_ordType:"+_ordType);
									log.debug("ordType:"+ordType);
									log.debug("Boolean:"+!_ordType.equalsIgnoreCase(ordType));
								}
								
								if(!_ordType.equalsIgnoreCase(ordType)) {
									YFCElement _fOrderEle = getfOrderEleByOrderType(_ordType, cAndfOrdListEle);
									if(_fOrderEle != null) {
										// changeOrder is for different FO
										chngfOrders.put("FO", _fOrderEle);
										String ordHeaderKey = this.getOrdHeaderKey(_fOrderEle);
										if(YFCObject.isNull(ordHeaderKey) || YFCObject.isVoid(ordHeaderKey)) {
											throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
										}
										
										YFCElement fOrdExtnEle = _fOrderEle.getChildElement("Extn");
										String webConfNum = null;
										if(fOrdExtnEle != null) {
											if(fOrdExtnEle.hasAttribute("ExtnWebConfNum")) {
												webConfNum = fOrdExtnEle.getAttribute("ExtnWebConfNum");
												if(YFCObject.isNull(webConfNum) || YFCObject.isVoid(webConfNum)) {
													throw new Exception("Attribute ExtnWebConfNum Cannot be NULL or Void!");
												}
											} else {
												throw new Exception("Attribute ExtnWebConfNum Not Available in getOrderList Template!");
											}
										}
										
										String legOrdNum = null;
										if(fOrdExtnEle != null) {
											if(fOrdExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
												legOrdNum = fOrdExtnEle.getAttribute("ExtnLegacyOrderNo");
												if(YFCObject.isNull(legOrdNum) || YFCObject.isVoid(legOrdNum)) {
													legOrdNum = "";
												}
											} else {
												legOrdNum = "";
											}
										}
										
										chngfOrdEle1.setAttribute("IsValidChangeOrder", "Y");
										chngfOrdEle1.setAttribute("OrderHeaderKey", ordHeaderKey);
										YFCElement chngfOrdExtnEle1 = chngfOrdEle1.getChildElement("Extn");
										if(chngfOrdExtnEle1 != null) {
											chngfOrdExtnEle1.setAttribute("ExtnWebConfNum", webConfNum);
											chngfOrdExtnEle1.setAttribute("ExtnLegacyOrderNo", legOrdNum);
										}
										YFCElement impEle = chngfOrdEle1.getOwnerDocument().importNode(chngfOrdLineEle0, true);
										impEle.setAttribute("LineProcessCode", "A");
										chngfOrdLinesEle1.appendChild(impEle);
										
										chngfOrdLinesEle0.removeChild(chngfOrdLineEle0);
										yfcItr = chngfOrdLinesEle0.getChildren("OrderLine");
									} else {
										// changeOrder is for new FO
										createfOrdEle0.setAttribute("IsValidCreateOrder", "Y");
										YFCElement impEle = createfOrdEle0.getOwnerDocument().importNode(chngfOrdLineEle0, true);
										impEle.setAttribute("LineProcessCode", "A");
										YFCElement impExtnEle = impEle.getChildElement("Extn");
										if(impExtnEle != null) {
											impExtnEle.setAttribute("ExtnLineType", stockType);
										} else {
											throw new Exception("Element OrderLine/Extn Not Available in Edit Order Message!");
										}
										createfOrdLinesEle0.appendChild(impEle);
										
										chngfOrdLinesEle0.removeChild(chngfOrdLineEle0);
										yfcItr = chngfOrdLinesEle0.getChildren("OrderLine");
									}
								} else {
									// changeOrder for the same FO
									chngfOrdEle0.setAttribute("IsValidChangeOrder", "Y");
									chngfOrdLineEle0.setAttribute("LineProcessCode", this.findLineProcessCode(chngfOrdLineEle0));
								}
							} else {
								// changeOrder for the same FO
								chngfOrdEle0.setAttribute("IsValidChangeOrder", "Y");
								chngfOrdLineEle0.setAttribute("LineProcessCode", this.findLineProcessCode(chngfOrdLineEle0));
							}
						} else {
							// changeOrder for the same FO
							chngfOrdEle0.setAttribute("IsValidChangeOrder", "Y");
							chngfOrdLineEle0.setAttribute("LineProcessCode", this.findLineProcessCode(chngfOrdLineEle0));
						}
					} else {
						// changeOrder for the same FO
						chngfOrdEle0.setAttribute("IsValidChangeOrder", "Y");
						chngfOrdLineEle0.setAttribute("LineProcessCode", this.findLineProcessCode(chngfOrdLineEle0));
					}
				} else {
					// changeOrder for the same FO
					chngfOrdEle0.setAttribute("IsValidChangeOrder", "Y");
					chngfOrdLineEle0.setAttribute("LineProcessCode", this.findLineProcessCode(chngfOrdLineEle0));
				}
			}
		}
		
		if(chngfOrdEle0.hasAttribute("IsValidChangeOrder")) {
			chngfOrders.put("CC", chngfOrdEle0);
		}
		if(chngfOrdEle1.hasAttribute("IsValidChangeOrder")) {
			chngfOrders.put("CA", chngfOrdEle1);
		}
		if(createfOrdEle0.hasAttribute("IsValidCreateOrder")) {
			chngfOrders.put("AA", createfOrdEle0);
		}
		
		return chngfOrders;
	}
	
	private String findLineProcessCode(YFCElement ordLineEle) throws Exception {
		if(ordLineEle.hasAttribute("Action")) {
			String action = ordLineEle.getAttribute("Action");
			if(!YFCObject.isNull(action) && !YFCObject.isVoid(action)) {
				if(action.equalsIgnoreCase("CREATE")) {
					return "A";
				}
			}
		} 
		return "C";
	}

	private String getInventoryIndicator(YFSEnvironment env, String itemId, String shipNode, String envtId, String lineType) throws Exception {
		
		if (log.isDebugEnabled()) {
			log.debug("**************************************GetInventoryIndicator*******************************************");
			
			log.debug("ItemID:"+itemId);
			log.debug("ShipNode:"+shipNode);
			log.debug("EnvtId:"+envtId);
			log.debug("LineType:"+lineType);
		}
		
		boolean callItemList = false;
		String invIndicator = null;
		String stockType = null;
		
		YFCDocument getItemExtnListInXML = YFCDocument.getDocumentFor("<XPXItemExtn/>");
		YFCElement getItemExtnListEle = getItemExtnListInXML.getDocumentElement();
		getItemExtnListEle.setAttribute("ItemID", itemId);		
		getItemExtnListEle.setAttribute("EnvironmentID", envtId);
		
		/* Start - changes made to fix Issue 1501 */
		if(shipNode.contains("_")){
			// Need to remove the environment id from ship node.
			String[] splitArrayOnUom = shipNode.split("_");			
			if(splitArrayOnUom.length > 0){
			shipNode = splitArrayOnUom[0];
			}
		}		
		getItemExtnListEle.setAttribute("XPXDivision", shipNode);
		/* End - changes made to fix Issue 1501 */
		
		if (log.isDebugEnabled()) {
			log.debug("getXPXItemBranchListService-InXML:"+getItemExtnListInXML.getString());
		}
		
		YFCDocument getItemExtnListOutXML = null;
		Document tempDoc = XPXEditChainedOrderExAPI.api.executeFlow(env, "getXPXItemBranchListService", getItemExtnListInXML.getDocument());
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
			
			if (log.isDebugEnabled()) {
				log.debug("XPXGetItemList-InXML:"+getItemListInXML.getString());
			}
			tempDoc = XPXEditChainedOrderExAPI.api.executeFlow(env, "XPXGetItemList", getItemListInXML.getDocument());
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
		
		if (log.isDebugEnabled()) {
			log.debug("ExtnLineType:"+stockType);
			
			log.debug("*****************************************************************************************************");
		}
		
		return stockType;
	}
	
	private String getOrdHeaderKey(YFCElement ordEle) {
		String ordHeaderKey = "";
		if(ordEle.hasAttribute("OrderHeaderKey")) {
			ordHeaderKey = ordEle.getAttribute("OrderHeaderKey");
		}
		return ordHeaderKey;
	}
	
	private String getLegacyOrderNo(YFCElement ordEle) {
		String legacyOrdNo = "";
		YFCElement ordExtnEle = ordEle.getChildElement("Extn");
		if(ordExtnEle != null) {
			if(ordExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
				legacyOrdNo = ordExtnEle.getAttribute("ExtnLegacyOrderNo");
			}
		}
		return legacyOrdNo;
	}
	
	private YFCElement getfOrderEle(String ordHeaderKey, YFCElement cAndfOrdListEle) throws Exception {
		
		YFCIterable<YFCElement> yfcItr = cAndfOrdListEle.getChildren("Order");
		while(yfcItr.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr.next();
			if(ordEle.hasAttribute("OrderHeaderKey")) {
			
				String _ordHeaderKey = ordEle.getAttribute("OrderHeaderKey");
				if(YFCObject.isNull(_ordHeaderKey) || YFCObject.isVoid(_ordHeaderKey)) {
					throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
				}
				
				if(ordHeaderKey.equalsIgnoreCase(_ordHeaderKey)) {
					return ordEle;
				}
			} else {
				throw new Exception("Attribute OrderHeaderKey Not Available in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
			}
		}
		
		return null;
	}

	private YFCElement getfOrderEleByOrderType(String ordType, YFCElement cAndfOrdListEle) throws Exception {
		
		YFCIterable<YFCElement> yfcItr = cAndfOrdListEle.getChildren("Order");
		while(yfcItr.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr.next();
			if(ordEle.hasAttribute("OrderType")) {
				String _ordType = ordEle.getAttribute("OrderType");
				if(YFCObject.isNull(_ordType) || YFCObject.isVoid(_ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
				}
				
				if(_ordType.equalsIgnoreCase(ordType)) {
					return ordEle;
				}
			} else {
				throw new Exception("Attribute OrderType Not Available in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
			}
		}
		
		return null;
	}
	
	private YFCElement getfOrderEleFromList(YFCElement cAndfOrdListEle) throws Exception {
		YFCIterable<YFCElement> yfcItr = cAndfOrdListEle.getChildren("Order");
		while(yfcItr.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr.next();
			if(ordEle.hasAttribute("OrderType")) {
				String _ordType = ordEle.getAttribute("OrderType");
				if(YFCObject.isNull(_ordType) || YFCObject.isVoid(_ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
				}
				
				if(!_ordType.equalsIgnoreCase("Customer")) {
					return ordEle;
				}
			} else {
				throw new Exception("Attribute OrderType Not Available in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
			}
		}
		return null;
	}
	
	private YFCElement getcOrderEle(String webConfNum, YFCElement cAndfOrdListEle) throws Exception {
		
		YFCIterable<YFCElement> yfcItr = cAndfOrdListEle.getChildren("Order");
		while(yfcItr.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr.next();
			if(ordEle.hasAttribute("OrderType")) {
			
				String ordType = ordEle.getAttribute("OrderType");
				if(YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
				}
				
				String _webConfNum = null;
				YFCElement extnEle = ordEle.getChildElement("Extn");
				if(extnEle != null) {
					if(extnEle.hasAttribute("ExtnWebConfNum")) {
						_webConfNum = extnEle.getAttribute("ExtnWebConfNum");
					} else {
						throw new Exception("Attribute ExtnWebConfNum Not Available in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
					}
				} else {
					throw new Exception("Element Order/Extn Not Available in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
				}
				if(ordType.equalsIgnoreCase("Customer") && (webConfNum.equalsIgnoreCase(_webConfNum))) {
					return ordEle;
				}
			} else {
				throw new Exception("Attribute OrderType Not Available in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
			}
		}
		
		return null;
	}
	
	private YFCElement getcOrderAndfOrderList(YFSEnvironment env, String webConfNum) throws Exception {
		
		YFCDocument getOrdListInDoc = YFCDocument.getDocumentFor("<Order/>");
		YFCElement ordListInEle = getOrdListInDoc.getDocumentElement();
		
		YFCElement extnEle = getOrdListInDoc.createElement("Extn");
		ordListInEle.appendChild(extnEle);
		extnEle.setAttribute("ExtnWebConfNum", webConfNum);
		
		if (log.isDebugEnabled()) {
			log.debug("XPXEditChainedOrderAPI.getCustomerOrderAndFulfillmentOrderList()-InXML:"+getOrdListInDoc.getString());
		}
		
		YFCDocument getOrdListOutDoc = null;
		Document tempDoc = api.executeFlow(env, "XPXGetOrderListForLegacyOrderUpdate", getOrdListInDoc.getDocument());
		if(tempDoc != null) {
			getOrdListOutDoc = YFCDocument.getDocumentFor(tempDoc);
			
			log.debug("XPXEditChainedOrderAPI.getCustomerOrderAndFulfillmentOrderList()-OutXML:"+getOrdListOutDoc.getString());
			
		}
		
		if(getOrdListOutDoc != null) {
			YFCElement ordListOutEle = getOrdListOutDoc.getDocumentElement();
			if(ordListOutEle != null) {
				if(ordListOutEle.hasAttribute("TotalOrderList")) {
					String totalOrdList = ordListOutEle.getAttribute("TotalOrderList");
					if(YFCObject.isNull(totalOrdList) || YFCObject.isVoid(totalOrdList)) {
						throw new Exception("Attribute TotalOrderList Cannot be NULL or Void in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
					}
					if(totalOrdList.equalsIgnoreCase("0")) {
						throw new Exception("Neither Customer Order/Fulfillment Order With ExtnWebConfNum["+webConfNum+"] Found!");
					} else {
						return getOrdListOutDoc.getDocumentElement();
					}
				} else {
					throw new Exception("Attribute TotalOrderList Not Available in XPXEditChainedOrderAPI.XPXGetOrderListForLegacyOrderUpdate Template!");
				}
			} else {
				throw new Exception("Neither Customer Order/Fulfillment Order With ExtnWebConfNum["+webConfNum+"] Found!");
			}
		} else {
			throw new Exception("Neither Customer Order/Fulfillment Order With ExtnWebConfNum["+webConfNum+"] Found!");
		}
	}

	private void setOrderHeaderAttributes(YFSEnvironment env, YFCElement editOrdEle, YFCElement cOrderEle, YFCElement buyerOrgCustEle) throws Exception {
		
		String _envId = null;
		String _compId = null;
		String _custNo = null;				
		
		// To retrieve header attributes from a customer order.
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
		
		if (log.isDebugEnabled()) {
			log.debug("CO_ExtnCustomerNo:"+_custNo);
			log.debug("CO_ExtnEnvtId:"+_envId);
			log.debug("CO_ExtnCompanyId:"+_compId);
		}
		
		String envId = null;
		String compId = null;
		String custNo = null;
		
		// To retrieve header attributes from legacy xml.
		YFCElement editOrdExtnEle = editOrdEle.getChildElement("Extn");
		if(editOrdExtnEle != null) {
			if(editOrdExtnEle.hasAttribute("ExtnCustomerNo")) {
				custNo = editOrdExtnEle.getAttribute("ExtnCustomerNo");
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
			
			if(editOrdExtnEle.hasAttribute("ExtnEnvtId")) {
				envId = editOrdExtnEle.getAttribute("ExtnEnvtId");
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
			
			if(editOrdExtnEle.hasAttribute("ExtnCompanyId")) {
				compId = editOrdExtnEle.getAttribute("ExtnCompanyId");
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
			if(editOrdExtnEle.hasAttribute("ExtnBillToName")) {
				String extnBillToName = editOrdExtnEle.getAttribute("ExtnBillToName");
				if(YFCObject.isNull(extnBillToName) || YFCObject.isVoid(extnBillToName)) {
					// Suffix type "MC" stands for Bill To. 
					custInfoEle = getCustomerInformation(env, editOrdEle, "MC", null);
					if(custInfoEle == null) {
						throw new Exception("No Customer Information Found in Sterling!");
					}
					editOrdExtnEle.setAttribute("ExtnBillToName", getBillToName(custInfoEle));
				}
			} else {
				custInfoEle = getCustomerInformation(env, editOrdEle, "MC", null);
				if(custInfoEle == null) {
					throw new Exception("No Customer Information Found in Sterling!");
				}
				editOrdExtnEle.setAttribute("ExtnBillToName", getBillToName(custInfoEle));
			}
			
			if(editOrdEle.hasAttribute("BillToID")) {
				String billToID = editOrdEle.getAttribute("BillToID");
				if(YFCObject.isNull(billToID) || YFCObject.isVoid(billToID)) {
					if(custInfoEle == null) {
						custInfoEle = getCustomerInformation(env, editOrdEle, "MC", null);
					}
					if(custInfoEle == null) {
						throw new Exception("No Customer Information Found in Sterling!");
					}
					editOrdEle.setAttribute("BillToID", getBillToID(custInfoEle));
				}
			} else {
				if(custInfoEle == null) {
					custInfoEle = getCustomerInformation(env, editOrdEle, "MC", null);
				}
				if(custInfoEle == null) {
					throw new Exception("No Customer Information Found in Sterling!");
				}
				editOrdEle.setAttribute("BillToID", getBillToID(custInfoEle));
			}
			
			// Customer information element has been made null to fetch the Ship To customer details.
			custInfoEle = null;
			if(editOrdExtnEle.hasAttribute("ExtnSAPParentName")) {
				String sapParentName = editOrdExtnEle.getAttribute("ExtnSAPParentName");
				if(YFCObject.isNull(sapParentName) || YFCObject.isVoid(sapParentName)) {
					// Suffix type "S" stands for Ship To.
					custInfoEle = getCustomerInformation(env, editOrdEle, "S", buyerOrgCustEle);
					if(custInfoEle == null) {
						throw new Exception("No Customer Information Found in Sterling!");
					}
					editOrdExtnEle.setAttribute("ExtnSAPParentName", getSAPParentName(custInfoEle));
				}
			} else {
				custInfoEle = getCustomerInformation(env, editOrdEle, "S", buyerOrgCustEle);
				if(custInfoEle == null) {
					throw new Exception("No Customer Information Found in Sterling!");
				}
				editOrdExtnEle.setAttribute("ExtnSAPParentName", getSAPParentName(custInfoEle));
			}
			
			if(editOrdExtnEle.hasAttribute("ExtnOrigEnvironmentCode")) {
				String orgEnvCode = editOrdExtnEle.getAttribute("ExtnOrigEnvironmentCode");
				if(YFCObject.isNull(orgEnvCode) || YFCObject.isVoid(orgEnvCode)) {
					if(custInfoEle == null) {
						custInfoEle = getCustomerInformation(env, editOrdEle, "S", buyerOrgCustEle);
					}
					if(custInfoEle == null) {
						throw new Exception("No Customer Information Found in Sterling!");
					}
					editOrdExtnEle.setAttribute("ExtnOrigEnvironmentCode", getOrigEnvCode(custInfoEle));
				}
			} else {
				if(custInfoEle == null) {
					custInfoEle = getCustomerInformation(env, editOrdEle, "S", buyerOrgCustEle);
				}
				if(custInfoEle == null) {
					throw new Exception("No Customer Information Found in Sterling!");
				}
				editOrdExtnEle.setAttribute("ExtnOrigEnvironmentCode", getOrigEnvCode(custInfoEle));
			}
			
			if(editOrdEle.hasAttribute("BuyerOrganizationCode")) {
				String buyerOrgCode = editOrdEle.getAttribute("BuyerOrganizationCode");
				if(YFCObject.isNull(buyerOrgCode) || YFCObject.isVoid(buyerOrgCode)) {
					if(custInfoEle != null) {
						custInfoEle = getCustomerInformation(env, editOrdEle, "S", buyerOrgCustEle);
					}
					if(custInfoEle == null) {
						throw new Exception("No Customer Information Found in Sterling!");
					}
					editOrdEle.setAttribute("BuyerOrganizationCode", this.getBuyerOrgCode(custInfoEle));
				}
			} else {
				if(custInfoEle == null) {
					custInfoEle = getCustomerInformation(env, editOrdEle, "S", buyerOrgCustEle);
				}
				if(custInfoEle == null) {
					throw new Exception("No Customer Information Found in Sterling!");
				}
				editOrdEle.setAttribute("BuyerOrganizationCode", this.getBuyerOrgCode(custInfoEle));
			}
			
			if(editOrdEle.hasAttribute("ShipToID")) {
				String shipToID = editOrdEle.getAttribute("ShipToID");
				if(YFCObject.isNull(shipToID) || YFCObject.isVoid(shipToID)) {
					if(custInfoEle == null) {
						custInfoEle = getCustomerInformation(env, editOrdEle, "S", buyerOrgCustEle);
					}
					if(custInfoEle == null) {
						throw new Exception("No Customer Information Found in Sterling!");
					}
					editOrdEle.setAttribute("ShipToID", this.getShipToID(custInfoEle));
				}
			} else {
				if(custInfoEle == null) {
					custInfoEle = getCustomerInformation(env, editOrdEle, "S", buyerOrgCustEle);
				}
				if(custInfoEle == null) {
					throw new Exception("No Customer Information Found in Sterling!");
				}
				editOrdEle.setAttribute("ShipToID", this.getShipToID(custInfoEle));
			}				
		} else {
			throw new Exception("OrderLine/Extn Element Not Available in Incoming Edit Message!");
		}
	}
	
	private Map<String, YFCElement> updatefOrder(YFSEnvironment env, YFCElement chngcOrderEle0, 
			YFCElement chngcOrdStatusEle0, YFCElement chngcOrdStatusEle1, YFCElement chngfOrderEle, 
				YFCElement chngcOrderEle1, YFCElement cOrderEle, YFCElement fOrderEle) throws Exception {
		Map<String, YFCElement> retDocMap = new HashMap<String, YFCElement>();
		Document tempDoc = null;
		
		if(chngcOrderEle0.hasAttribute("OrderHeaderKey")) {
			removeInstructionsElement(chngcOrderEle0);
			this.filterAttributes(chngcOrderEle0, true);
			
			if (log.isDebugEnabled()) {
				log.debug("XPXChangeOrder_CO-InXML:"+chngcOrderEle0.getString());
			}
			
			tempDoc = XPXEditChainedOrderExAPI.api.executeFlow(env, "XPXChangeOrder", chngcOrderEle0.getOwnerDocument().getDocument());
			if(tempDoc != null) {
				cOrderEle = YFCDocument.getDocumentFor(tempDoc).getDocumentElement();
			} else {
				throw new Exception("Service XPXChangeOrder Failed!");
			}	
			retDocMap.put("CO", cOrderEle);
		}
		
		boolean isChngOrdReq = chaincAndfOrder(chngfOrderEle, cOrderEle);
		if(isChngOrdReq) {
			this.filterAttributes(chngfOrderEle, false);
			
			if (log.isDebugEnabled()) {
				log.debug("XPXChangeOrder_FO-InXML:"+chngfOrderEle.getString());
			}
			
			tempDoc = XPXEditChainedOrderExAPI.api.executeFlow(env, "XPXChangeOrder", chngfOrderEle.getOwnerDocument().getDocument());
			if(tempDoc != null) {
				fOrderEle = YFCDocument.getDocumentFor(tempDoc).getDocumentElement();
			} else {
				throw new Exception("Service XPXChangeOrder Failed!");
			}
			retDocMap.put("FO", fOrderEle);
		}
		
		if(chngcOrdStatusEle0.hasAttribute("OrderHeaderKey")) {
			
			if (log.isDebugEnabled()) {
				log.debug("XPXChangeOrderStatus_CO-InXML:"+chngcOrdStatusEle0.getString());
			}
			
			tempDoc = XPXEditChainedOrderExAPI.api.executeFlow(env, "XPXChangeOrderStatus", chngcOrdStatusEle0.getOwnerDocument().getDocument());
			if(tempDoc == null) {
				throw new Exception("Service XPXChangeOrderStatus Failed!");
			}
		}
		
		if(chngcOrdStatusEle1.hasAttribute("OrderHeaderKey")) {
			
			if (log.isDebugEnabled()) {
				log.debug("XPXChangeOrderStatus_CO-InXML:"+chngcOrdStatusEle1.getString());
			}
			
			tempDoc = XPXEditChainedOrderExAPI.api.executeFlow(env, "XPXChangeOrderStatus", chngcOrdStatusEle1.getOwnerDocument().getDocument());
			if(tempDoc == null) {
				throw new Exception("Service XPXChangeOrderStatus Failed!");
			}
		}
		
		if(chngcOrderEle1.hasAttribute("OrderHeaderKey")) {
			
			if (log.isDebugEnabled()) {
				log.debug("XPXChangeOrder_CO-InXML:"+chngcOrderEle1.getString());
			}
			
			tempDoc = XPXEditChainedOrderExAPI.api.executeFlow(env, "XPXChangeOrder", chngcOrderEle1.getOwnerDocument().getDocument());
			if(tempDoc != null) {
				cOrderEle = YFCDocument.getDocumentFor(tempDoc).getDocumentElement();
			} else {
				throw new Exception("Service XPXChangeOrder Failed!");
			}	
			retDocMap.put("CO", cOrderEle);
		}
		
		return retDocMap;
	}
		
	private boolean chaincAndfOrder(YFCElement fOrderEle, YFCElement cOrderEle) throws Exception {
		
		boolean isChngOrdReq = true;
		
		YFCElement fOrderLinesEle = fOrderEle.getChildElement("OrderLines");
		if(fOrderLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = fOrderLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement fOrdLineEle = (YFCElement)yfcItr.next();
				if(fOrdLineEle != null) {
					if(fOrdLineEle.hasAttribute("IngoreLineFromFOChange")) {
						String ignoreLineFromFOChange = fOrdLineEle.getAttribute("IngoreLineFromFOChange");
						if(!YFCObject.isNull(ignoreLineFromFOChange) && !YFCObject.isVoid(ignoreLineFromFOChange)) {
							fOrderLinesEle.removeChild(fOrdLineEle);
							continue;
						}
					}
				} else {
					throw new Exception("OrderLine Element Not Available in Incoming Edit Message!");
				}
			}
		}
		
		fOrderLinesEle = fOrderEle.getChildElement("OrderLines");
		if(fOrderLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = fOrderLinesEle.getChildren("OrderLine");
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
							String cOrdHeaderKey = this.getOrderHeaderKeyForWebLineNo(_webLineNo, cOrderEle);
							String cOrdLineKey = this.getOrderLineKeyForWebLineNo(_webLineNo, cOrderEle);
							
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
							throw new Exception("Attribute ExtnWebLineNumber Not Available in Incoming Edit Message!");
						}
					} else {
						throw new Exception("Extn Element Not Available in Incoming Edit Message!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in Incoming Edit Message!");
				}
			}
		}
		return isChngOrdReq;
	}
	
	private void preparefOChange(YFSEnvironment env, YFCElement chngcOrderEle0, 
			YFCElement chngcOrderEle1, YFCElement chngcOrdStatusEle0, YFCElement chngcOrdStatusEle1, 
					YFCElement editOrdEle, YFCElement fOrderEle, YFCElement cOrderEle) throws Exception {
		
		String fOrdHeaderKey = null;
		if(fOrderEle.hasAttribute("OrderHeaderKey")) {
			fOrdHeaderKey = fOrderEle.getAttribute("OrderHeaderKey");
			if(!YFCObject.isNull(fOrdHeaderKey) && !YFCObject.isVoid(fOrdHeaderKey)) {
				editOrdEle.setAttribute("OrderHeaderKey", fOrdHeaderKey);
				editOrdEle.setAttribute("Action", "MODIFY");
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
		
		if (log.isDebugEnabled()) {
			log.debug("fOrdHeaderKey:"+fOrdHeaderKey);
			log.debug("cOrdHeaderKey;"+cOrdHeaderKey);
		}
		
		YFCElement chngcOrdLinesEle0 = chngcOrderEle0.getChildElement("OrderLines");
		YFCElement editOrdLinesEle = editOrdEle.getChildElement("OrderLines");
		if(editOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = editOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement editOrdLineEle = (YFCElement) yfcItr.next();
				if(editOrdLineEle != null) {
					String action = null;
					String editOrdLineKey = null;
					String webLineNo = null;
					if(editOrdLineEle.hasAttribute("LineProcessCode")) {
						String lineProcessCode = editOrdLineEle.getAttribute("LineProcessCode");
						if(YFCObject.isNull(lineProcessCode) || YFCObject.isVoid(lineProcessCode)) {
							throw new Exception("Attribute LineProcessCode Cannot be NULL or Void!");
						}
						
						if(editOrdLineEle.hasAttribute("Action")) {
							action = editOrdLineEle.getAttribute("Action");
						}
						
						if(editOrdLineEle.hasAttribute("OrderLineKey")) {
							editOrdLineKey = editOrdLineEle.getAttribute("OrderLineKey");
						}
						
						YFCElement editOrdLineExtnEle = editOrdLineEle.getChildElement("Extn");
						if(editOrdLineExtnEle != null) {
							if(editOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
								webLineNo = editOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
							}
						} else {
							throw new Exception("Extn Element Not Available in Incoming Edit Message!");
						}
						
						String fOrdLineKey = null;
						if(!YFCObject.isNull(action) && !YFCObject.isVoid(action)) {
							if(!action.equalsIgnoreCase("CREATE")) {
								if(!YFCObject.isNull(editOrdLineKey) && !YFCObject.isVoid(editOrdLineKey)) {
									fOrdLineKey = editOrdLineKey;
								} else if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo)) {
									fOrdLineKey = getOrderLineKeyForWebLineNo(webLineNo, fOrderEle);
								} else {
									throw new Exception("One or More Mandatory Attributes[Action='CREATE', OrderLineKey, WebLineNo] Not Availble in Incoming Edit Message!");
								}
							}
						} else if(!YFCObject.isNull(editOrdLineKey) && !YFCObject.isVoid(editOrdLineKey)) {
							fOrdLineKey = editOrdLineKey;
						}  else if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo)) {
							fOrdLineKey = getOrderLineKeyForWebLineNo(webLineNo, fOrderEle);
						} else {
							throw new Exception("One or More Mandatory Attributes[Action='CREATE', OrderLineKey, (WebLineNo,LegacyLineNo)] Not Availble in Incoming Edit Message!");
						}
						 
						String cOrdLineKey = getOrderLineKeyForWebLineNo(webLineNo, cOrderEle);
						
						if (log.isDebugEnabled()) {
							log.debug("fOrdLineKey:"+fOrdLineKey);
							log.debug("cOrdLineKey:"+cOrdLineKey);
						}
						
						if(lineProcessCode.equalsIgnoreCase("A")) {						
							if(YFCObject.isNull(fOrdLineKey) || YFCObject.isVoid(fOrdLineKey)) {
								if(YFCObject.isNull(cOrdLineKey) || YFCObject.isVoid(cOrdLineKey)) {
									// New Line Not Exists In Customer Order
									chngcOrderEle0.setAttribute("OrderHeaderKey", cOrdHeaderKey);
									
									YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(editOrdLineEle, true);
									chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
									chngcOrdLineEle0.setAttribute("Action", "CREATE");
									
									editOrdLineEle.setAttribute("Action","CREATE");
									editOrdLineEle.setAttribute("IsNewLine", "Y");
									
								} else {
									// New Line Already Exists In Customer Order
									this.handleLineProcessCodeCandA(cOrdHeaderKey, cOrdLineKey, action, fOrdLineKey, webLineNo, editOrdLineEle, fOrderEle, cOrderEle,	chngcOrdStatusEle0, chngcOrdStatusEle1, chngcOrderEle0, chngcOrderEle1);
									
									editOrdLineEle.setAttribute("Action","CREATE");
									editOrdLineEle.setAttribute("IsNewLine", "Y");
									
								}
							} else {
								// WebLineNo Already Exists In Fulfillment Order
								this.handleLineProcessCodeCandA(cOrdHeaderKey, cOrdLineKey, action, fOrdLineKey, webLineNo, editOrdLineEle, fOrderEle, cOrderEle,	chngcOrdStatusEle0, chngcOrdStatusEle1, chngcOrderEle0, chngcOrderEle1);
								
								editOrdLineEle.setAttribute("Action","MODIFY");
								editOrdLineEle.setAttribute("IsNewLine", "N");
								editOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);
								
							}
						} else if(lineProcessCode.equalsIgnoreCase("C")) {
							if(YFCObject.isNull(fOrdLineKey) || YFCObject.isVoid(fOrdLineKey)) {
								throw new Exception("ExtnWebLineNumber Does Not Exist in FO. Line Process Code Cannot be C!");
							}
							if(YFCObject.isNull(cOrdLineKey) || YFCObject.isVoid(cOrdLineKey)) {
								throw new Exception("ExtnWebLineNumber Does Not Exist in CO. Line Process Code Cannot be C!"); 
							}
							
							this.handleLineProcessCodeCandA(cOrdHeaderKey, cOrdLineKey, action, fOrdLineKey, webLineNo, 
									editOrdLineEle, fOrderEle, cOrderEle,	chngcOrdStatusEle0, chngcOrdStatusEle1, 
									chngcOrderEle0, chngcOrderEle1);
							
							editOrdLineEle.setAttribute("Action","MODIFY");
							editOrdLineEle.setAttribute("IsNewLine", "N");
							editOrdLineEle.setAttribute("OrderLineKey", fOrdLineKey);
							
						} 
						else if(lineProcessCode.equalsIgnoreCase("D")) {
							throw new Exception("LineProcessCode=D Not Allowed Order Edit!");			
						} else {
							throw new Exception("Invalid LineProcessCode in the Incoming Edit Message!");
						}
					} else {
						throw new Exception("Attribute LineProcessCode Not Available in the Incoming Edit Message!");
					}
				} else {
					throw new Exception("OrderLine Element Not Available in the Incoming Edit Message!");
				}
			}
		}	
	}
	
	private void handleLineProcessCodeCandA(String cOrdHeaderKey, String cOrdLineKey, String action, 
			String fOrdLineKey, String webLineNo, YFCElement editOrdLineEle, YFCElement fOrderEle, YFCElement cOrderEle, 
			YFCElement chngcOrdStatusEle0, YFCElement chngcOrdStatusEle1, YFCElement chngcOrderEle0, YFCElement chngcOrderEle1) throws Exception {
		
		Float _ordLineQtyInXML = 0f;
		Float _ordLineQtyInCO = 0f;
		Float _ordLineQtyInFO = 0f;
		
		YFCElement chngcOrdStatusLinesEle0 = chngcOrdStatusEle0.getChildElement("OrderLines");
		YFCElement chngcOrdStatusLinesEle1 = chngcOrdStatusEle1.getChildElement("OrderLines");
		YFCElement chngcOrdLinesEle0 = chngcOrderEle0.getChildElement("OrderLines");
		YFCElement chngcOrdLinesEle1 = chngcOrderEle1.getChildElement("OrderLines");
		
		boolean isNewFOLine = true;
		if(!YFCObject.isNull(action) && !YFCObject.isVoid(action)) {
			if(!action.equalsIgnoreCase("CREATE")) {
				if(!YFCObject.isNull(fOrdLineKey) && !YFCObject.isVoid(fOrdLineKey)) {
					List<String> ordLineKeys = this.getOrdLineKeysToArray(fOrderEle);
					if(ordLineKeys.contains(fOrdLineKey)) {
						isNewFOLine = false;
					}
				} else if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo)) {
					fOrdLineKey = getOrderLineKeyForWebLineNo(webLineNo, fOrderEle);
					if(!YFCObject.isNull(fOrdLineKey) && !YFCObject.isVoid(fOrdLineKey)) {
						isNewFOLine = false;
					}
				} else {
					throw new Exception("One or More Mandatory Attributes[Action='CREATE', OrderLineKey, (WebLineNo,LegacyLineNo)] Not Availble in Incoming Edit Message!");
				}
			}
		} else if(!YFCObject.isNull(fOrdLineKey) && !YFCObject.isVoid(fOrdLineKey)) {
			List<String> ordLineKeys = this.getOrdLineKeysToArray(fOrderEle);
			if(ordLineKeys.contains(fOrdLineKey)) {
				isNewFOLine = false;
			}
		}  else if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo)) {
			fOrdLineKey = getOrderLineKeyForWebLineNo(webLineNo, fOrderEle);
			if(!YFCObject.isNull(fOrdLineKey) && !YFCObject.isVoid(fOrdLineKey)) {
				isNewFOLine = false;
			}
		} else {
			throw new Exception("One or More Mandatory Attributes[Action='CREATE', OrderLineKey, (WebLineNo,LegacyLineNo)] Not Availble in Incoming Edit Message!");
		}
		
		if (log.isDebugEnabled()) {
			log.debug("action:"+action);
			log.debug("fOrdLineKey:"+fOrdLineKey);
			log.debug("webLineNo:"+webLineNo);
			log.debug("isNewFOLine:"+isNewFOLine);
		}
		
		YFCElement editOrdLineTranQtyEle = editOrdLineEle.getChildElement("OrderLineTranQuantity");
		String ordQty = null;
		String tuom = null;
		if(editOrdLineTranQtyEle != null) {
			if(editOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
				ordQty = editOrdLineTranQtyEle.getAttribute("OrderedQty");
				if(YFCObject.isNull(ordQty) || YFCObject.isVoid(ordQty)) {
					throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute OrderedQty Not Available in Incoming Edit Message!");
			}
			if(editOrdLineTranQtyEle.hasAttribute("TransactionalUOM")) {
				tuom = editOrdLineTranQtyEle.getAttribute("TransactionalUOM");
			}
		} else {
			throw new Exception("Element OrderLineTranQuantity Not Available in Incoming Edit Message!");
		}
		
		_ordLineQtyInXML = Float.parseFloat(ordQty);
		_ordLineQtyInCO = Float.parseFloat(this.getOrderedQtyOnOrderLine(webLineNo, cOrderEle));
		if(!isNewFOLine) {
			if(!YFCObject.isNull(fOrdLineKey) && !YFCObject.isVoid(fOrdLineKey)) {
				String tempQty = this.getOrderedQtyOnOrderLineByLineKey(fOrdLineKey, fOrderEle);
				if(YFCObject.isNull(tempQty) || YFCObject.isVoid(tempQty)) {
					throw new Exception("OrderedQty on OrderLine Cannot be NULL or Void!");
				}
				_ordLineQtyInFO = Float.parseFloat(tempQty);
			} else {
				String tempQty = this.getOrderedQtyOnOrderLine(webLineNo, fOrderEle);
				if(YFCObject.isNull(tempQty) || YFCObject.isVoid(tempQty)) {
					throw new Exception("OrderedQty on OrderLine Cannot be NULL or Void!");
				}
				_ordLineQtyInFO = Float.parseFloat(tempQty);
			}
		} else {
			_ordLineQtyInFO = _ordLineQtyInXML;
		}
		
		if (log.isDebugEnabled()) {
			log.debug("_ordLineQtyInXML:"+_ordLineQtyInXML);
			log.debug("_ordLineQtyInCO:"+_ordLineQtyInCO);
			log.debug("_ordLineQtyInFO:"+_ordLineQtyInFO);
		}
		
		if(_ordLineQtyInXML > _ordLineQtyInFO) {
			YFCElement _chngcOrdStatusLineEle0 = this.getOrderLineFromChngcOrdStatusEle(chngcOrdStatusEle0, cOrdLineKey);
			if(_chngcOrdStatusLineEle0 == null) {
				chngcOrdStatusEle0.setAttribute("OrderHeaderKey", cOrdHeaderKey);
				YFCElement chngcOrdStatusLineEle0 = chngcOrdStatusLinesEle0.getOwnerDocument().createElement("OrderLine");
				chngcOrdStatusLinesEle0.appendChild(chngcOrdStatusLineEle0);
				chngcOrdStatusLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
				YFCElement chngcOrdStatusLineTranQtyEle0 = chngcOrdStatusLineEle0.getOwnerDocument().createElement("OrderLineTranQuantity");
				chngcOrdStatusLineEle0.appendChild(chngcOrdStatusLineTranQtyEle0);
				chngcOrdStatusLineTranQtyEle0.setAttribute("Quantity", _ordLineQtyInXML-_ordLineQtyInFO);
				chngcOrdStatusLineTranQtyEle0.setAttribute("TransactionalUOM", tuom);
			} else {
				String existQty = null;
				YFCElement _chngcOrdStatusLineTranQtyEle0 = _chngcOrdStatusLineEle0.getChildElement("OrderLineTranQuantity");
				if(_chngcOrdStatusLineTranQtyEle0 != null) {
					if(_chngcOrdStatusLineTranQtyEle0.hasAttribute("Quantity")) {
						existQty = _chngcOrdStatusLineTranQtyEle0.getAttribute("Quantity");
						if(YFCObject.isNull(existQty) || YFCObject.isVoid(existQty)) {
							throw new Exception("Attribute OrderLineTranQuantity/@Quantity Cannot be NULL or Void!");
						}
						_chngcOrdStatusLineTranQtyEle0.setAttribute("Quantity", new Float(existQty) + (_ordLineQtyInXML-_ordLineQtyInFO));
					} else {
						throw new Exception("Attribute Quantity Not Available in ChangeOrder InXML!");
					}
				}
			}
		} else {
			if(_ordLineQtyInXML < _ordLineQtyInFO) {
				YFCElement _chngcOrdStatusLineEle1 = this.getOrderLineFromChngcOrdStatusEle(chngcOrdStatusEle1, cOrdLineKey);
				if(_chngcOrdStatusLineEle1 == null) {
					chngcOrdStatusEle1.setAttribute("OrderHeaderKey", cOrdHeaderKey);
					YFCElement chngcOrdStatusLineEle1 = chngcOrdStatusLinesEle1.getOwnerDocument().createElement("OrderLine");
					chngcOrdStatusLinesEle1.appendChild(chngcOrdStatusLineEle1);
					chngcOrdStatusLineEle1.setAttribute("OrderLineKey", cOrdLineKey);
					YFCElement chngcOrdStatusLineTranQtyEle1 = chngcOrdStatusLineEle1.getOwnerDocument().createElement("OrderLineTranQuantity");
					chngcOrdStatusLineEle1.appendChild(chngcOrdStatusLineTranQtyEle1);
					chngcOrdStatusLineTranQtyEle1.setAttribute("Quantity", (_ordLineQtyInFO - _ordLineQtyInXML));
					chngcOrdStatusLineTranQtyEle1.setAttribute("TransactionalUOM", tuom);
				} else {
					String existQty = null;
					YFCElement _chngcOrdStatusLineTranQtyEle1 = _chngcOrdStatusLineEle1.getChildElement("OrderLineTranQuantity");
					if(_chngcOrdStatusLineTranQtyEle1 != null) {
						if(_chngcOrdStatusLineTranQtyEle1.hasAttribute("Quantity")) {
							existQty = _chngcOrdStatusLineTranQtyEle1.getAttribute("Quantity");
							if(YFCObject.isNull(existQty) || YFCObject.isVoid(existQty)) {
								throw new Exception("Attribute OrderLineTranQuantity/@Quantity Cannot be NULL or Void!");
							}
							_chngcOrdStatusLineTranQtyEle1.setAttribute("Quantity", new Float(existQty) + (_ordLineQtyInFO - _ordLineQtyInXML));
						} else {
							throw new Exception("Attribute Quantity Not Available in ChangeOrder InXML!");
						}
					}
				}
				YFCElement _chngcOrdLineEle1 = this.getOrderLineFromChngcOrdEle(chngcOrderEle1, cOrdLineKey);
				if(_chngcOrdLineEle1 == null) {
					chngcOrderEle1.setAttribute("OrderHeaderKey", cOrdHeaderKey);
					
					YFCElement chngcOrdLineEle1 = chngcOrdLinesEle1.getOwnerDocument().createElement("OrderLine");
					chngcOrdLinesEle1.appendChild(chngcOrdLineEle1);
					chngcOrdLineEle1.setAttribute("Action", "MODIFY");
					chngcOrdLineEle1.setAttribute("OrderLineKey", cOrdLineKey);
					
					YFCElement chngcOrdLineTranQtyEle1 = chngcOrdLineEle1.getOwnerDocument().createElement("OrderLineTranQuantity");
					chngcOrdLineEle1.appendChild(chngcOrdLineTranQtyEle1);
					if(chngcOrdLineTranQtyEle1 != null) {
						chngcOrdLineTranQtyEle1.setAttribute("OrderedQty", _ordLineQtyInCO - (_ordLineQtyInFO - _ordLineQtyInXML));
						chngcOrdLineTranQtyEle1.setAttribute("TransactionalUOM", tuom);
					} else {
						throw new Exception("Element OrderLineTranQuantity Not Available in Incoming Edit Message!");
					}
				} else {
					YFCElement _chngcOrdLineTranQtyEle1 = _chngcOrdLineEle1.getChildElement("OrderLineTranQuantity");
					if(_chngcOrdLineTranQtyEle1 != null) {
						String existQty = null;
						if(_chngcOrdLineTranQtyEle1.hasAttribute("OrderedQty")) {
							existQty = _chngcOrdLineTranQtyEle1.getAttribute("OrderedQty");
							if(YFCObject.isNull(existQty) || YFCObject.isVoid(existQty)) {
								throw new Exception("Attribute OrderLineTranQuantity/@OrderedQty Cannot be NULL or Void!");
							}
						} else {
							throw new Exception("Attribute OrderLineTranQuantity/@OrderedQty Not Available in ChangeOrder InXML!");
						}
						_chngcOrdLineTranQtyEle1.setAttribute("OrderedQty", new Float(existQty) - (_ordLineQtyInFO - _ordLineQtyInXML));
					} else {
						throw new Exception("Element OrderLineTranQuantity Not Available in Incoming Edit Message!");
					}
				}
			}
		}
		
		if(isNewFOLine) {
			YFCElement _chngcOrdLineEle0 = this.getOrderLineFromChngcOrdEle(chngcOrderEle0, cOrdLineKey);
			if(_chngcOrdLineEle0 == null) {
				chngcOrderEle0.setAttribute("OrderHeaderKey", cOrdHeaderKey);
				
				YFCElement chngcOrdLineEle0 = chngcOrdLinesEle0.getOwnerDocument().importNode(editOrdLineEle, true);
				chngcOrdLinesEle0.appendChild(chngcOrdLineEle0);
				chngcOrdLineEle0.setAttribute("Action", "MODIFY");
				chngcOrdLineEle0.setAttribute("OrderLineKey", cOrdLineKey);
				YFCElement chngcOrdLineTranQtyEle0 = chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
				if(chngcOrdLineTranQtyEle0 != null) {
					if(!isNewFOLine) {
						chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", _ordLineQtyInCO);
					} else{
						chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", _ordLineQtyInCO + _ordLineQtyInXML);
					}
				} else {
					throw new Exception("Element OrderLineTranQuantity Not Available in Incoming Edit Message!");
				}
			} else {
				YFCElement _chngcOrdLineTranQtyEle0 = _chngcOrdLineEle0.getChildElement("OrderLineTranQuantity");
				if(_chngcOrdLineTranQtyEle0 != null) {
					String existQty = null;
					if(_chngcOrdLineTranQtyEle0.hasAttribute("OrderedQty")) {
						existQty = _chngcOrdLineTranQtyEle0.getAttribute("OrderedQty");
						if(YFCObject.isNull(existQty) || YFCObject.isVoid(existQty)) {
							throw new Exception("Attribute OrderLineTranQuantity/@OrderedQty Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute OrderLineTranQuantity/@OrderedQty Not Available in ChangeOrder InXML!");
					}
					_chngcOrdLineTranQtyEle0.setAttribute("OrderedQty", new Float(existQty) + _ordLineQtyInXML);
				} else {
					throw new Exception("Element OrderLineTranQuantity Not Available in Incoming Edit Message!");
				}
			}
		}
	}

	private YFCElement getOrderLineFromChngcOrdStatusEle(YFCElement chngcOrdStatusEle, String cOrdLineKey) {
		if(chngcOrdStatusEle.hasAttribute("OrderHeaderKey")) {
			YFCElement chngcOrdLinesStatusEle = chngcOrdStatusEle.getChildElement("OrderLines");
			if(chngcOrdLinesStatusEle != null) {
				YFCIterable<YFCElement> yfcItr = chngcOrdLinesStatusEle.getChildren("OrderLine");
				while(yfcItr.hasNext()) {
					YFCElement chngcOrdLineStatusEle = (YFCElement)yfcItr.next();
					String _cOrdLineKey = null;
					if(chngcOrdLineStatusEle.hasAttribute("OrderLineKey")) {
						_cOrdLineKey = chngcOrdLineStatusEle.getAttribute("OrderLineKey");
						if(!YFCObject.isNull(_cOrdLineKey) && !YFCObject.isVoid(_cOrdLineKey)) {
							if(_cOrdLineKey.equalsIgnoreCase(cOrdLineKey)) {
								return chngcOrdLineStatusEle;
							}
						}
					}
				}
			}
		}
		return null;
	}

	private YFCElement getOrderLineFromChngcOrdEle(YFCElement chngcOrderEle, String cOrdLineKey) {
		if(chngcOrderEle.hasAttribute("OrderHeaderKey")) {
			YFCElement chngcOrdLinesEle = chngcOrderEle.getChildElement("OrderLines");
			if(chngcOrdLinesEle != null) {
				YFCIterable<YFCElement> yfcItr = chngcOrdLinesEle.getChildren("OrderLine");
				while(yfcItr.hasNext()) {
					YFCElement chngcOrdLineEle = (YFCElement)yfcItr.next();
					String _cOrdLineKey = null;
					if(chngcOrdLineEle.hasAttribute("OrderLineKey")) {
						_cOrdLineKey = chngcOrdLineEle.getAttribute("OrderLineKey");
						if(!YFCObject.isNull(_cOrdLineKey) && !YFCObject.isVoid(_cOrdLineKey)) {
							if(_cOrdLineKey.equalsIgnoreCase(cOrdLineKey)) {
								return chngcOrdLineEle;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private YFCElement getCustomerInfo(YFSEnvironment env, String buyerOrgCode) throws Exception {
		YFCElement getCustomerListEle = YFCDocument.getDocumentFor("<Customer BuyerOrganizationCode=''/>").getDocumentElement();
		getCustomerListEle.setAttribute("CustomerID", buyerOrgCode);
		
		if (log.isDebugEnabled()) {
			log.debug("getCustomerList-InXML:"+getCustomerListEle.getString());
		}
		
		Document tempDoc = api.executeFlow(env, "XPXGetCustomerList", getCustomerListEle.getOwnerDocument().getDocument());
		if(tempDoc == null) {
			throw new Exception("XPXGetCustomerList Flow Returned ZERO Customers!");
		}
		
		YFCDocument getCustListOutXML = YFCDocument.getDocumentFor(tempDoc);
		YFCElement custListOutXMLEle = getCustListOutXML.getDocumentElement();
		YFCIterable<YFCElement> yfcItr = custListOutXMLEle.getChildren("Customer");
		if(!yfcItr.hasNext()) {
			throw new Exception("XPXGetCustomerList Flow Returned ZERO Customers!");
		} else {
			return custListOutXMLEle;
		}
	}
	
	private YFCElement getCustomerInformation(YFSEnvironment env, YFCElement editOrdEle, String suffixType, YFCElement buyerOrgCustEle) throws Exception {
		
		if(suffixType.equalsIgnoreCase("S")) {
			if(buyerOrgCustEle != null) {
				return buyerOrgCustEle;
			}
		}
		
		YFCElement extneditOrdEle = editOrdEle.getChildElement("Extn");
		
		YFCDocument getCustListInXML = YFCDocument.getDocumentFor("<Customer/>");
		YFCElement custInXMLEle = getCustListInXML.getDocumentElement();
		if(editOrdEle.hasAttribute("EnterpriseCode")) {
			String orgCode = editOrdEle.getAttribute("EnterpriseCode");
			if(YFCObject.isNull(orgCode) || YFCObject.isNull(orgCode)) {
				throw new Exception("Attribute EnterpriseCode cannot be NULL or Void!");
			}
			custInXMLEle.setAttribute("OrganizationCode", orgCode);
		} else {
			throw new Exception("Attribute EnterpriseCode Not Available in Incoming Edit Message!");
		}
		
		YFCElement extnCustInXMLEle = getCustListInXML.createElement("Extn");
		custInXMLEle.appendChild(extnCustInXMLEle);
		
		if(extneditOrdEle.hasAttribute("ExtnCustomerNo")) {
			String legacyCustNo = extneditOrdEle.getAttribute("ExtnCustomerNo");
			if(YFCObject.isNull(legacyCustNo) || YFCObject.isVoid(legacyCustNo)) {
				throw new Exception("Attribute ExtnCustomerNo Cannot be NULL or Void!");
			}
			extnCustInXMLEle.setAttribute("ExtnLegacyCustNumber", legacyCustNo);
		} else {
			throw new Exception("Attribute ExtnLegacyCustNumber Not Available in Incoming Edit Message!");
		}
		
		if(YFCObject.isNull(suffixType) || YFCObject.isVoid(suffixType)) {
			throw new Exception("Customer SuffixType Cannot be NULL or Void!");
		}
		
		// To set the attribute based on suffix type.
		if(suffixType.equalsIgnoreCase("MC")) {
			if(extneditOrdEle.hasAttribute("ExtnBillToSuffix")) {
				String buillToSuffix = extneditOrdEle.getAttribute("ExtnBillToSuffix");
				if(YFCObject.isNull(buillToSuffix) || YFCObject.isVoid(buillToSuffix)) {
					throw new Exception("Attribute ExtnBillToSuffix Cannot be NULL or Void!");
				}
				extnCustInXMLEle.setAttribute("ExtnBillToSuffix", buillToSuffix);
			} else {
				throw new Exception("Attribute ExtnBillToSuffix Not Available in Incoming Edit Message!");
			}
		} else if(suffixType.equalsIgnoreCase("S")) {
			if(extneditOrdEle.hasAttribute("ExtnShipToSuffix")) {
				String shipToSuffix = extneditOrdEle.getAttribute("ExtnShipToSuffix");
				if(YFCObject.isNull(shipToSuffix) || YFCObject.isVoid(shipToSuffix)) {
					throw new Exception("Attribute ExtnShipToSuffix Cannot be NULL or Void!");
				}
				extnCustInXMLEle.setAttribute("ExtnShipToSuffix", shipToSuffix);
			} else {
				throw new Exception("Attribute ExtnBillToSuffix Not Available in Incoming Edit Message!");
			}
		} else {
			throw new Exception("Customer SuffixType is invalid in Incoming Edit Message.");
		}
		if(suffixType.trim().equalsIgnoreCase("MC")) {
			extnCustInXMLEle.setAttribute("ExtnSuffixType", "B");
		} else {
			extnCustInXMLEle.setAttribute("ExtnSuffixType", suffixType.trim());
		}
		
		if (log.isDebugEnabled()) {
			log.debug("XPXGetCustomerList-InXML:"+getCustListInXML.getString());
		}
		Document tempDoc = api.executeFlow(env, "XPXGetCustomerList", getCustListInXML.getDocument());
		if(tempDoc == null) {
			throw new Exception("XPXGetCustomerList Flow Returned ZERO Customers!");
		}
		
		if (log.isDebugEnabled()) {
			log.debug("XPXGetCustomerList-OutXML:"+YFCDocument.getDocumentFor(tempDoc).getString());
		}
		
		YFCElement custEle = null;
		YFCDocument getCustListOutXML = YFCDocument.getDocumentFor(tempDoc);
		YFCElement custListOutXMLEle = getCustListOutXML.getDocumentElement();
		YFCIterable<YFCElement> yfcItr = custListOutXMLEle.getChildren("Customer");
		if(!yfcItr.hasNext()) {
			throw new Exception("XPXGetCustomerList Flow Returned ZERO Customers!");
		} else {
			if(suffixType.equalsIgnoreCase("MC")) {
				String editOrdCustKey = null;
				YFCElement billToCustEle = (YFCElement) yfcItr.next();
				if(billToCustEle.hasAttribute("RootCustomerKey")) {
					editOrdCustKey = billToCustEle.getAttribute("RootCustomerKey");
					if(YFCObject.isNull(editOrdCustKey) || YFCObject.isVoid(editOrdCustKey)) {
						throw new Exception("Attribute RootCustomerKey Cannot be NULL or Void!");
					}
				} else {
					throw new Exception("Attribute RootCustomerKey Not Available in getCustomerList Template!");
				}
				
				YFCDocument geteditOrdCustListInXML = YFCDocument.getDocumentFor("<Customer/>");
				YFCElement editOrdCustInXMLEle = geteditOrdCustListInXML.getDocumentElement();
				editOrdCustInXMLEle.setAttribute("CustomerKey", editOrdCustKey);
				editOrdCustInXMLEle.setAttribute("ExtnSuffixType", suffixType.trim());
				
				if (log.isDebugEnabled()) {
					log.debug("XPXGetCustomerList-InXML:"+geteditOrdCustListInXML.getString());
				}
				
				tempDoc = api.executeFlow(env, "XPXGetCustomerList", geteditOrdCustListInXML.getDocument());
				if(tempDoc == null) {
					throw new Exception("XPXGetCustomerList - MSAP Customer Not Found!");
				}
				
				if (log.isDebugEnabled()) {
					log.debug("XPXGetCustomerList-OutXML:"+YFCDocument.getDocumentFor(tempDoc).getString());
				}
				
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
	
	private YFCDocument updateCustomerOrder(YFSEnvironment env, YFCElement chngcOrdEle, YFCElement cOrderEle) throws Exception {
		String _ordHeaderKey = null;
		if(cOrderEle.hasAttribute("OrderHeaderKey")) {
			_ordHeaderKey = cOrderEle.getAttribute("OrderHeaderKey");
			if(YFCObject.isNull(_ordHeaderKey) || YFCObject.isVoid(_ordHeaderKey)) {
				throw new Exception("Attribute OrderHeader Cannot be NULL or Void!");
			}
			chngcOrdEle.setAttribute("OrderHeaderKey", _ordHeaderKey);
		} else {
			throw new Exception("Attribute OrderHeaderKey Not Available in OrderList Template!");
		}
		
		// Forms list of web line numbers of the existing web line numbers in the customer order.
		List<String> webLineNosExistingCO = getWebLineNosToArray(cOrderEle);
		if(webLineNosExistingCO.size() == 0) {
			throw new Exception("No WebLineNumber Available in the Existing Customer Order!");
		}
		
		List<String> alreadyModifiedWebLineNoList = new ArrayList<String>();
		YFCElement chngcOrdLinesEle = chngcOrdEle.getChildElement("OrderLines");
		if(chngcOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = chngcOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement chngcOrdLineEle = (YFCElement)yfcItr.next();
				YFCElement chngcOrdLineExtnEle = chngcOrdLineEle.getChildElement("Extn");
				if(chngcOrdLineExtnEle != null) {
					if(chngcOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
						String webLineNo = chngcOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
						if(YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
							throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
						}
						if(webLineNosExistingCO.contains(webLineNo)) {
							String ordQtyInXML = null;
							YFCElement chngcOrdLineTranQtyEle = chngcOrdLineEle.getChildElement("OrderLineTranQuantity");
							if(chngcOrdLineTranQtyEle != null) {
								if(chngcOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
									ordQtyInXML = chngcOrdLineTranQtyEle.getAttribute("OrderedQty");
									if(YFCObject.isNull(ordQtyInXML) || YFCObject.isVoid(ordQtyInXML)) {
										throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
									}
								} else {
									throw new Exception("Attribute OrderedQty Not Available in Incoming Edit Message!");
								}
							}
							if(!alreadyModifiedWebLineNoList.contains(webLineNo)) {
								String cOrdLineKey = this.getOrderLineKeyForWebLineNo(webLineNo, cOrderEle);
								if(YFCObject.isNull(cOrdLineKey) || YFCObject.isVoid(cOrdLineKey)) {
									throw new Exception("Attribute OrderLineKey Cannot be NULL or Void!");
								}
								
								String ordQtyInCO = getOrderedQtyOnOrderLine(webLineNo, cOrderEle);
								if(YFCObject.isNull(ordQtyInCO) || YFCObject.isVoid(ordQtyInCO)) {
									ordQtyInCO = "0.0";
								}
								
								chngcOrdLineEle.setAttribute("IsNewLine", "N");
								chngcOrdLineEle.setAttribute("Action", "MODIFY");
								chngcOrdLineEle.setAttribute("OrderLineKey", cOrdLineKey);
								
								chngcOrdLineTranQtyEle.setAttribute("OrderedQty", Float.parseFloat(ordQtyInCO) + Float.parseFloat(ordQtyInXML));
								
								alreadyModifiedWebLineNoList.add(webLineNo);
							} else {
								chngcOrdLineEle.setAttribute("DeleteIt", "Y");
								String cOrdLineKey = this.getOrderLineKeyForWebLineNo(webLineNo, cOrderEle);
								if(YFCObject.isNull(cOrdLineKey) || YFCObject.isVoid(cOrdLineKey)) {
									throw new Exception("Attribute OrderLineKey Cannot be NULL or Void!");
								}
								YFCElement _chngcOrdLineEle = this.getOrderLineFromChngcOrdEle(chngcOrdEle, cOrdLineKey);
								if(_chngcOrdLineEle != null) {
									YFCElement _chngcOrdLineTranQtyEle = _chngcOrdLineEle.getChildElement("OrderLineTranQuantity");
									if(_chngcOrdLineTranQtyEle != null) {
										String existQty = null;
										if(_chngcOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
											existQty = _chngcOrdLineTranQtyEle.getAttribute("OrderedQty");
											if(YFCObject.isNull(ordQtyInXML) || YFCObject.isVoid(ordQtyInXML)) {
												throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
											}
											_chngcOrdLineTranQtyEle.setAttribute("OrderedQty", Float.parseFloat(existQty) + Float.parseFloat(ordQtyInXML));
										} else {
											throw new Exception("Attribute OrderedQty Not Available in Incoming Edit Message!");
										}
									} 
								}
							}
						} else {
							chngcOrdLineEle.setAttribute("IsNewLine", "Y");
							chngcOrdLineEle.setAttribute("Action", "CREATE");
						}
					} else {
						throw new Exception("Attribute ExtnWebLineNumber Not Available in Incoming Edit Message!");
					}
				}
			}
		}
		
		chngcOrdLinesEle = chngcOrdEle.getChildElement("OrderLines");
		if(chngcOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = chngcOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement chngcOrdLineEle = (YFCElement)yfcItr.next();
				if(chngcOrdLineEle.hasAttribute("DeleteIt")) {
					chngcOrdLinesEle.removeChild(chngcOrdLineEle);
					yfcItr = chngcOrdLinesEle.getChildren("OrderLine");
				}
			}
		}
		
		this.removeInstructionsElement(chngcOrdEle);
		this.filterAttributes(chngcOrdEle, true);
		
		if (log.isDebugEnabled()) {
			log.debug("XPXChangeOrder_CO-InXML:"+chngcOrdEle.getString());
		}
		
		// Change order call to update the customer order.
		Document tempDoc = XPXEditChainedOrderExAPI.api.executeFlow(env, "XPXChangeOrder", chngcOrdEle.getOwnerDocument().getDocument());
		if(tempDoc != null) {
			return YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("XPXChangeOrder Service Failed!");
		}

	}
	
	private boolean IsCancelledLine(YFCElement ordLineEle) throws Exception {
		if(ordLineEle.hasAttribute("OrderedQty")) {
			String ordQty = ordLineEle.getAttribute("OrderedQty");
			if(YFCObject.isNull(ordQty) || YFCObject.isVoid(ordQty)) {
				throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
			}
			double dOrdQty = Double.parseDouble(ordQty);
			if(dOrdQty == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new Exception("Attribute OrderedQty Not Available in getOrderList Template!");
		}
	}
	
	private String getOrderedQtyOnOrderLineByLineKey(String ordLineKey, YFCElement orderEle) throws Exception {
		YFCElement ordLinesEle = orderEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
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
						throw new Exception("Attribute OrderLineKey Not Available in getOrderList Template!");
					}
					if(ordLineKey.equalsIgnoreCase(_ordLineKey)) {
						String ordQty = null;
						YFCElement ordLineTranQtyEle = ordLineEle.getChildElement("OrderLineTranQuantity");
						if(ordLineTranQtyEle != null) {
							if(ordLineTranQtyEle.hasAttribute("OrderedQty")) {
								ordQty = ordLineTranQtyEle.getAttribute("OrderedQty");
								if(YFCObject.isNull(ordQty) || YFCObject.isVoid(ordQty)) {
									throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
								}
								return ordQty;
							} else {
								throw new Exception("Attribute OrderedQty Not Available in OrderList Template!");
							}
						} else {
							throw new Exception("Element OrderLineTranQuantity Not Available in GetOrderList Template!");
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

	private String getOrderedQtyOnOrderLine(String webLineNo, YFCElement orderEle) throws Exception {
		
		YFCElement ordLinesEle = orderEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						String _webLineNo = null;
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							if(_webLineNo.equalsIgnoreCase(webLineNo)) {
								String ordQty = null;
								YFCElement ordLineTranQtyEle = ordLineEle.getChildElement("OrderLineTranQuantity");
								if(ordLineTranQtyEle != null) {
									if(ordLineTranQtyEle.hasAttribute("OrderedQty")) {
										ordQty = ordLineTranQtyEle.getAttribute("OrderedQty");
										if(YFCObject.isNull(ordQty) || YFCObject.isVoid(ordQty)) {
											throw new Exception("Attribute OrderedQty Cannot be NULL or Void!");
										}
										return ordQty;
									} else {
										throw new Exception("Attribute OrderedQty Not Available in OrderList Template!");
									}
								} else {
									throw new Exception("Element OrderLineTranQuantity Not Available in GetOrderList Template!");
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
	
	private YFCDocument createFulfillmentOrder(YFSEnvironment env, YFCElement editOrdEle, YFCElement cOrderEle) throws Exception {
		
		editOrdEle.setAttribute("Action", "CREATE");
		
		YFCElement editOrdLinesEle = editOrdEle.getChildElement("OrderLines");
		if(editOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = editOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement editOrdLineEle = (YFCElement)yfcItr.next();
				editOrdLineEle.setAttribute("Action", "CREATE");

				String webLineNo = null;
				YFCElement editOrdLineExtnEle = editOrdLineEle.getChildElement("Extn");
				if(editOrdLineExtnEle != null) {
					if(editOrdLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
						webLineNo = editOrdLineExtnEle.getAttribute("ExtnWebLineNumber");
						if(YFCObject.isNull(webLineNo) || YFCObject.isVoid(webLineNo)) {
							throw new Exception("Extn/@ExtnWebLineNumber Cannot be NULL or Void!");
						}
					} else {
						throw new Exception("Attribute Extn/@ExtnWebLineNumber Not Available in Incoming Edit Message!");
					}
					
					// Logic to set the order type. ie., STOCK or DIRECT
					if(editOrdLineExtnEle.hasAttribute("ExtnLineType")) {
						String extnLineType = editOrdLineExtnEle.getAttribute("ExtnLineType");
						if(YFCObject.isNull(extnLineType) || YFCObject.isVoid(extnLineType)) {
							throw new Exception("ExtnLineType Cannot be NULL or Void!");
						}
						if(extnLineType.equalsIgnoreCase("DIRECT")) {
							editOrdEle.setAttribute("OrderType", "DIRECT_ORDER");
						} else if(extnLineType.equalsIgnoreCase("STOCK")) {
							editOrdEle.setAttribute("OrderType", "STOCK_ORDER");
						} else {
							throw new Exception("Invalid ExtnLineType in Fulfillment Order InXML!");
						}
						
					} else {
						throw new Exception("Attribute Order/OrderLine/Extn/@ExtnLineType Not Available in Fulfillment Order InXML!");
					}
				} else {
					throw new Exception("OrderLine/Extn Element Not Available in Incoming Edit Message!");
				}
				
				// Mapping the customer order details with fulfillment order details.
				YFCElement chainedFromNode = editOrdEle.getOwnerDocument().createElement("ChainedFrom");
				editOrdLineEle.appendChild(chainedFromNode);
				
				String custOrdHeaderKey = cOrderEle.getAttribute("OrderHeaderKey");
				if(!YFCObject.isVoid(custOrdHeaderKey) && !YFCObject.isNull(custOrdHeaderKey)) {
					chainedFromNode.setAttribute("OrderHeaderKey", custOrdHeaderKey);
				} else {
					throw new Exception("OrderHeaderKey Cannot be NULL or Void!");
				}
				
				// To get the corresponding customer order line key based on web line number.
				String custOrdLineKey = getOrderLineKeyForWebLineNo(webLineNo, cOrderEle);
				if(!YFCObject.isVoid(custOrdLineKey) && !YFCObject.isNull(custOrdLineKey)) {
					chainedFromNode.setAttribute("OrderLineKey", custOrdLineKey);
				} else {
					throw new Exception("OrderLineKey Cannot be NULL or Void!");
				}
			}
		} else {
			throw new Exception("Order/OrderLines Element Not Available in Incoming Edit Message!");
		}
		
		filterAttributes(editOrdEle, false);
		
		if (log.isDebugEnabled()) {
			log.debug("XPXCreateOrder_FO-InXML:"+editOrdEle.getString());
		}
		
		// To create fulfillment order.
		Document tempDoc = XPXEditChainedOrderExAPI.api.executeFlow(env, "XPXCreateOrder", editOrdEle.getOwnerDocument().getDocument());
		if(tempDoc != null) {
			return YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("XPXCreateOrder Service Failed!");
		}
	}	
	
	private void filterAttributes(YFCElement chngOrdEle, boolean isCustOrder) throws Exception {
		if(chngOrdEle.hasAttribute("Action")) {
			String action = chngOrdEle.getAttribute("Action");
			if(!YFCObject.isNull(action) && !YFCObject.isVoid(action)) {
				if(action.equalsIgnoreCase("CREATE")) {
					if(chngOrdEle.hasAttribute("OrderHeaderKey")) {
						chngOrdEle.removeAttribute("OrderHeaderKey");
					}
				}
			}
		}
		if(chngOrdEle.hasAttribute("OrderNo")) {
			chngOrdEle.removeAttribute("OrderNo");
		}
		if(chngOrdEle.hasAttribute("APIName")) {
			chngOrdEle.removeAttribute("APIName");
		}
		if(chngOrdEle.hasAttribute("OrderType")) {
			if(isCustOrder) {
				chngOrdEle.removeAttribute("OrderType");
			}
		}
		YFCElement chngOrdExtnEle = chngOrdEle.getChildElement("Extn");
		if(chngOrdExtnEle != null) {
			if(chngOrdExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
				if(isCustOrder) {
					chngOrdExtnEle.removeAttribute("ExtnLegacyOrderNo");
				} else {
					if(chngOrdEle.hasAttribute("Action")) {
						String action = chngOrdEle.getAttribute("Action");
						if(!YFCObject.isNull(action) && !YFCObject.isVoid(action)) {
							if(action.equalsIgnoreCase("CREATE")) {
								chngOrdExtnEle.removeAttribute("ExtnLegacyOrderNo");
							}
						}
					}
				}
			}
		}
		YFCElement chngOrdLinesEle = chngOrdEle.getChildElement("OrderLines");
		YFCIterable<YFCElement> yfcItr = chngOrdLinesEle.getChildren("OrderLine");
		while(yfcItr.hasNext()) {
			YFCElement chngOrdLineEle = (YFCElement) yfcItr.next();
			if(chngOrdLineEle.hasAttribute("PrimeLineNo")) {
				chngOrdLineEle.removeAttribute("PrimeLineNo");
			}
			if(chngOrdLineEle.hasAttribute("SubLineNo")) {
				chngOrdLineEle.removeAttribute("SubLineNo");
			}
			YFCElement chngOrdLinePriceInfoEle = chngOrdLineEle.getChildElement("LinePriceInfo");
			if(chngOrdLinePriceInfoEle != null) {
				if(chngOrdLinePriceInfoEle.hasAttribute("PricingUOM")) {
					chngOrdLinePriceInfoEle.removeAttribute("PricingUOM");
				}
			}
			YFCElement chngOrdLineTranQtyEle = chngOrdLineEle.getChildElement("OrderLineTranQuantity");
			if(chngOrdLineTranQtyEle != null) {
				if(chngOrdLineTranQtyEle.hasAttribute("OrderedQty")) {
					String ordQty = chngOrdLineTranQtyEle.getAttribute("OrderedQty");
					if(YFCObject.isNull(ordQty) || YFCObject.isVoid(ordQty)) {
						throw new Exception("Attribute OrderLineTranQuantity/@OrderedQty Cannot be NULL or Void!");
					}
					chngOrdLineEle.setAttribute("OrderedQty", ordQty);
				} else {
					throw new Exception("Attribute OrderLineTranQuantity/@OrderedQty Not Available in Incoming Edit Message!");
				}
			} else {
				throw new Exception("Element OrderLineTranQuantity Not Available in Incoming Edit Message!");
			}
			
			if(chngOrdLineEle.hasAttribute("StatusQuantity")) {
				chngOrdLineEle.removeAttribute("StatusQuantity");
			}
			YFCElement chngOrdLineExtnEle = chngOrdLineEle.getChildElement("Extn");
			if(chngOrdLineEle.hasAttribute("Action")) {
				String action = chngOrdLineEle.getAttribute("Action");
				if(!YFCObject.isNull(action) && !YFCObject.isVoid(action)) {
					if(action.equalsIgnoreCase("CREATE")) {
						if(chngOrdLineEle.hasAttribute("OrderLineKey")) {
							chngOrdLineEle.removeAttribute("OrderLineKey");
						}
						if(chngOrdLineExtnEle != null) {
							if(chngOrdLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
								chngOrdLineExtnEle.removeAttribute("ExtnLegacyLineNumber");
							}
						}
					} else {
						YFCElement chngOrdLineItemEle = chngOrdLineEle.getChildElement("Item");
						if(chngOrdLineItemEle != null) {
							chngOrdLineEle.removeChild(chngOrdLineItemEle);
						}
					}
				}
			}
			
			if(isCustOrder) {
				if(chngOrdLineExtnEle != null) {
					if(chngOrdLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
						chngOrdLineExtnEle.removeAttribute("ExtnLegacyLineNumber");
					}
				}
			}
		}
	}
	
	private String getOrderHeaderKeyForWebLineNo(String webLineNo, YFCElement ordEle) throws Exception {
		
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					YFCElement extnEle = ordLineEle.getChildElement("Extn");
					if(extnEle != null) {
						String _webLineNo = null;
						if(extnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
							if(_webLineNo.equalsIgnoreCase(webLineNo)) {
								
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
	
	private String getOrderLineKeyForWebLineNo(String webLineNo, YFCElement ordEle) throws Exception {
		
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
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

	private List<String> getWebLineNosToArray(YFCElement editOrdEle) throws Exception {
		
		List<String> weblineNosArray = new ArrayList<String>();
		YFCElement ordLinesEle = editOrdEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					if(!this.IsCancelledLine(ordLineEle)) {
						YFCElement extnEle = ordLineEle.getChildElement("Extn");
						if(extnEle != null) {
							if(extnEle.hasAttribute("ExtnWebLineNumber")) {
								String webLineNo = extnEle.getAttribute("ExtnWebLineNumber");
								if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo)) {
									weblineNosArray.add(webLineNo);
								}
							} else {
								throw new Exception("Attribute ExtnWebLineNumber Not Available!");
							}
						} else {
							throw new Exception("OrderLine/Extn Element Not Available!");
						}
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
	
	private List<String> getOrdLineKeysToArray(YFCElement ordEle) throws Exception {
		
		List<String> ordLineKeyArray = new ArrayList<String>();
		YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
		if(ordLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement ordLineEle = (YFCElement) yfcItr.next();
				if(ordLineEle != null) {
					if(ordLineEle.hasAttribute("OrderLineKey")) {
						String ordLineKey = ordLineEle.getAttribute("OrderLineKey");
						if(!YFCObject.isNull(ordLineKey) && !YFCObject.isVoid(ordLineKey)) {
							ordLineKeyArray.add(ordLineKey);
						}
					}
				} else {
					throw new Exception("OrderLine Element Not Available!");
				}
			}
		} else {
			throw new Exception("OrderLines Element Not Available!");
		}
		
		return ordLineKeyArray;
	}

	private YFCElement removeInstructionsElement(YFCElement editOrdEle) throws Exception {
		
		if(editOrdEle.getChildElement("Instructions") != null) {
			editOrdEle.removeChild(editOrdEle.getChildElement("Instructions"));
		}
		
		YFCElement editOrdLinesEle = editOrdEle.getChildElement("OrderLines");
		if(editOrdLinesEle != null) {
			YFCIterable<YFCElement> yfcItr = editOrdLinesEle.getChildren("OrderLine");
			while(yfcItr.hasNext()) {
				YFCElement editOrdLineEle = (YFCElement) yfcItr.next();
				if(editOrdLineEle.getChildElement("Instructions") != null) {
					editOrdLineEle.removeChild(editOrdLineEle.getChildElement("Instructions"));
				}
			}
		}
		
		return editOrdEle;
	}
	
	private String setWebLineNo(YFSEnvironment env, String entryType, String envtId) {
		long nextSeqNo = CallDBSequence.getNextDBSequenceNo(env, "SEQ_XPEDX_WEBLINE");
		return XPXAddParametersAPI.generateWebLineNumber(entryType, nextSeqNo, envtId);
	}
	
	private void updateWebHold(Document businessRuleOutputDoc,YFCElement postToLegacyOrdEle){
		String orderHoldFlag = "";
		if(businessRuleOutputDoc != null && businessRuleOutputDoc.getDocumentElement().hasAttribute("OrderHoldFlag")){
			orderHoldFlag = businessRuleOutputDoc.getDocumentElement().getAttribute("OrderHoldFlag");
		}
		if(!YFCObject.isNull(orderHoldFlag) && orderHoldFlag.equalsIgnoreCase("Y")) {
			// To add ExtnWebHoldFlag to 'Y'
			YFCElement extnElem = postToLegacyOrdEle.getChildElement("Extn");
			if(extnElem != null) {
				extnElem.setAttribute("ExtnWebHoldFlag", "Y");
				extnElem.setAttribute("ExtnWebHoldReason", XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_DESC);		
			}
		}
	}
	
	private String getChainedFromOrderKeys(String ordLineKey, YFCElement cAndfOrdListEle) throws Exception {
		
		YFCIterable<YFCElement> yfcItr0 = cAndfOrdListEle.getChildren("Order");
		while(yfcItr0.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr0.next();
			
			String ordType = null;
			if(ordEle.hasAttribute("OrderType")) {
				ordType = ordEle.getAttribute("OrderType");
				if(YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute OrderType Not Available in getOrderList Template!");
			}
			
			String ordHeaderKey = null;
			if(ordEle.hasAttribute("OrderHeaderKey")) {
				ordHeaderKey = ordEle.getAttribute("OrderHeaderKey");
				if(YFCObject.isNull(ordHeaderKey) || YFCObject.isVoid(ordHeaderKey)) {
					throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute OrderHeaderKey Not Available in getOrderList Template!");
			}
			
			String legacyOrdNo = "";
			YFCElement ordExtnEle = ordEle.getChildElement("Extn");
			if(ordExtnEle != null) {
				if(ordExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
					legacyOrdNo = ordExtnEle.getAttribute("ExtnLegacyOrderNo");
					if(YFCObject.isNull(legacyOrdNo) || YFCObject.isVoid(legacyOrdNo)) {
						legacyOrdNo = " ";
					}
				} else {
					legacyOrdNo = " ";
				}
			}
			
			YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
			if(ordLinesEle != null) {
				YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
				while(yfcItr.hasNext()) {
					YFCElement ordLineEle = (YFCElement) yfcItr.next();
					String chnFromOrdLineKey = null;
					if(ordLineEle.hasAttribute("ChainedFromOrderLineKey")) {
						chnFromOrdLineKey = ordLineEle.getAttribute("ChainedFromOrderLineKey");
						if(!YFCObject.isNull(chnFromOrdLineKey) && !YFCObject.isVoid(chnFromOrdLineKey)) {
							if(chnFromOrdLineKey.equalsIgnoreCase(ordLineKey) && !ordType.equalsIgnoreCase("Customer")) {
								String _ordLineKey = null;
								if(ordLineEle.hasAttribute("OrderLineKey")) {
									_ordLineKey = ordLineEle.getAttribute("OrderLineKey");
									if(YFCObject.isNull(ordHeaderKey) || YFCObject.isVoid(ordHeaderKey)) {
										throw new Exception("Attribute OrderLineKey Cannot be NULL or Void!");
									}
								} else {
									throw new Exception("Attribute OrderLineKey Not Available in getOrderList Template!");
								}
								
								String legacyLineNo = "";
								YFCElement ordLineExtnEle = ordLineEle.getChildElement("Extn");
								if(ordLineExtnEle != null) {
									if(ordLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
										legacyLineNo = ordLineExtnEle.getAttribute("ExtnLegacyLineNumber");
										if(YFCObject.isNull(legacyLineNo) || YFCObject.isVoid(legacyLineNo)) {
											legacyLineNo = " ";
										}
									} else {
										legacyLineNo = " ";
									}
								}
								return ordType+"|"+ordHeaderKey+"|"+legacyOrdNo+"|"+_ordLineKey+"|"+legacyLineNo;
							}
						}
					}
				}
			} else {
				throw new Exception("OrderLines Element Not Available in the OrderList Template!");
			}
		}
		return null;
	}
	
	private String getChainedFromOrderKeysForWebLineNo(String webLineNo, YFCElement cAndfOrdListEle) throws Exception {
		
		YFCIterable<YFCElement> yfcItr0 = cAndfOrdListEle.getChildren("Order");
		while(yfcItr0.hasNext()) {
			YFCElement ordEle = (YFCElement) yfcItr0.next();
			
			String ordType = null;
			if(ordEle.hasAttribute("OrderType")) {
				ordType = ordEle.getAttribute("OrderType");
				if(YFCObject.isNull(ordType) || YFCObject.isVoid(ordType)) {
					throw new Exception("Attribute OrderType Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute OrderType Not Available in getOrderList Template!");
			}
			
			String ordHeaderKey = null;
			if(ordEle.hasAttribute("OrderHeaderKey")) {
				ordHeaderKey = ordEle.getAttribute("OrderHeaderKey");
				if(YFCObject.isNull(ordHeaderKey) || YFCObject.isVoid(ordHeaderKey)) {
					throw new Exception("Attribute OrderHeaderKey Cannot be NULL or Void!");
				}
			} else {
				throw new Exception("Attribute OrderHeaderKey Not Available in getOrderList Template!");
			}
			
			String legacyOrdNo = "";
			YFCElement ordExtnEle = ordEle.getChildElement("Extn");
			if(ordExtnEle != null) {
				if(ordExtnEle.hasAttribute("ExtnLegacyOrderNo")) {
					legacyOrdNo = ordExtnEle.getAttribute("ExtnLegacyOrderNo");
					if(YFCObject.isNull(legacyOrdNo) || YFCObject.isVoid(legacyOrdNo)) {
						legacyOrdNo = " ";
					}
				} else {
					legacyOrdNo = " ";
				}
			}
			
			YFCElement ordLinesEle = ordEle.getChildElement("OrderLines");
			if(ordLinesEle != null) {
				YFCIterable<YFCElement> yfcItr = ordLinesEle.getChildren("OrderLine");
				while(yfcItr.hasNext()) {
					YFCElement ordLineEle = (YFCElement) yfcItr.next();
					YFCElement ordLineExtnEle = ordLineEle.getChildElement("Extn");
					if(ordLineExtnEle != null) {
						String _webLineNo = null;
						if(ordLineExtnEle.hasAttribute("ExtnWebLineNumber")) {
							_webLineNo = ordLineExtnEle.getAttribute("ExtnWebLineNumber");
							if(YFCObject.isNull(_webLineNo) || YFCObject.isVoid(_webLineNo)) {
								throw new Exception("Attribute ExtnWebLineNumber Cannot be NULL or Void!");
							}
						} else {
							throw new Exception("Attribute ExtnWebLineNumber Not Available in the OrderList Template!");
						}
						
						if(_webLineNo.equalsIgnoreCase(webLineNo) && !ordType.equalsIgnoreCase("Customer")) {
							String _ordLineKey = null;
							if(ordLineEle.hasAttribute("OrderLineKey")) {
								_ordLineKey = ordLineEle.getAttribute("OrderLineKey");
								if(YFCObject.isNull(ordHeaderKey) || YFCObject.isVoid(ordHeaderKey)) {
									throw new Exception("Attribute OrderLineKey Cannot be NULL or Void!");
								}
							} else {
								throw new Exception("Attribute OrderLineKey Not Available in getOrderList Template!");
							}
							
							String legacyLineNo = "";
							if(ordLineExtnEle.hasAttribute("ExtnLegacyLineNumber")) {
								legacyLineNo = ordLineExtnEle.getAttribute("ExtnLegacyLineNumber");
								if(YFCObject.isNull(legacyLineNo) || YFCObject.isVoid(legacyLineNo)) {
									legacyLineNo = " ";
								}
							} else {
								legacyLineNo = " ";
							}
							return ordType+"|"+ordHeaderKey+"|"+legacyOrdNo+"|"+_ordLineKey+"|"+legacyLineNo;
						}
						
					} else {
						throw new Exception("OrderLine Element Not Available in the OrderList Template!");
					}
				}
			} else {
				throw new Exception("OrderLines Element Not Available in the OrderList Template!");
			}
		}
		return null;
	}
	
	private boolean hasLegacyLineNum(YFCElement fOrdLineElem) {
		
		YFCElement extnElem = fOrdLineElem.getChildElement("Extn");
		if (extnElem != null && extnElem.hasAttribute("ExtnLegacyLineNumber")) {
			String legacyLineNo = extnElem.getAttribute("ExtnLegacyLineNumber");
			if (!YFCObject.isNull(legacyLineNo) && !YFCObject.isVoid(legacyLineNo)) {
				return true;
			}
		}
		return false;
	}
	
	public void setProperties(Properties _prop) throws Exception {
		this._prop = _prop;
	}
}
