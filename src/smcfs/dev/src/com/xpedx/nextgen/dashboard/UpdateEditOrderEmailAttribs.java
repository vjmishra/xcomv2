package com.xpedx.nextgen.dashboard;

import java.util.List;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class UpdateEditOrderEmailAttribs implements YIFCustomApi {

	private static YFCLogCategory log;
	private static YIFApi api = null;
	Properties props;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	/* Updates the email attributes(ExtnLastOrderOperation & ExtnOrderConfirmationEmailSentFlag) for Order Edit Flow. 
	 * These attributes are considered while sending an order confirmation email. Check class XPXPerformLegacyOrderUpdateExAPI.java
	 * 
	 */
	public Document updateEditOrderEmailAttributes(YFSEnvironment env, Document editOrderInputDoc) throws Exception{
		try {
			if (log.isDebugEnabled()) {
				log.debug("UpdateEditOrderEmailAttribs_InputXML: "+ SCXmlUtil.getString(editOrderInputDoc));
			}
			
			String inputOrderType=editOrderInputDoc.getDocumentElement().getAttribute(XPXLiterals.A_ORDER_TYPE);			
			if("STOCK_ORDER".equals(inputOrderType) || "DIRECT_ORDER".equals(inputOrderType) || "THIRD_PARTY".equals(inputOrderType)) {				
				Element editOrderExtnInputElem = SCXmlUtil.getChildElement(editOrderInputDoc.getDocumentElement(),"Extn");
				if(editOrderExtnInputElem != null) {				
					String webConfNum=editOrderExtnInputElem.getAttribute(XPXLiterals.A_WEB_CONF_NUMBER);
					if(webConfNum!=null && webConfNum.trim().length()>0) {
						Document getOrderListInputDoc = SCXmlUtil.createDocument(XPXLiterals.E_ORDER);
						getOrderListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_TYPE, "Customer");
						getOrderListInputDoc.getDocumentElement().setAttribute("OrderTypeQryType", "EQ");
						Element getOrderListInputExtnElem = SCXmlUtil.createChild(getOrderListInputDoc.getDocumentElement(), XPXLiterals.E_EXTN);
						getOrderListInputExtnElem.setAttribute(XPXLiterals.A_WEB_CONF_NUMBER, webConfNum);					
						if (log.isDebugEnabled()) {
							log.debug("Inside updateEditOrderEmailAttributes method of UpdateEditOrderEmailAttribs class. getOrderList_InputXML: "+ SCXmlUtil.getString(getOrderListInputDoc));
						}
						StringBuilder getOrderListOutputTemplate= new StringBuilder("<OrderList>").append("<Order OrderHeaderKey=\"\" OrderType=\"\"/>").append("</OrderList>");
						env.setApiTemplate(XPXLiterals.GET_ORDER_LIST_API, SCXmlUtil.createFromString(getOrderListOutputTemplate.toString()));
						Document getOrderListOutputDoc = api.invoke(env, XPXLiterals.GET_ORDER_LIST_API, getOrderListInputDoc);
						env.clearApiTemplate(XPXLiterals.GET_ORDER_LIST_API);
						if (log.isDebugEnabled()) {
							log.debug("Inside updateEditOrderEmailAttributes method of UpdateEditOrderEmailAttribs class. getOrderList_OutputXML: "+ SCXmlUtil.getString(getOrderListOutputDoc));
						}
						if(getOrderListOutputDoc != null) {
							Element getOrderListOutputElem = SCXmlUtil.getChildElement(getOrderListOutputDoc.getDocumentElement(), XPXLiterals.E_ORDER);
							if(getOrderListOutputElem != null) {
								String customerOrderHeaderKey = getOrderListOutputElem.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
								Document changeOrderInputDoc = SCXmlUtil.createDocument(XPXLiterals.E_ORDER);
								changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, customerOrderHeaderKey);
								Element changeOrderInputExtnElem = SCXmlUtil.createChild(changeOrderInputDoc.getDocumentElement(), XPXLiterals.E_EXTN);
								changeOrderInputExtnElem.setAttribute("ExtnLastOrderOperation", "OrderEdit");
								changeOrderInputExtnElem.setAttribute("ExtnOrderConfirmationEmailSentFlag", "N");
								env.setTxnObject("CustomerOrderEditDetails", changeOrderInputDoc);								
								if (log.isDebugEnabled()) {
									log.debug("Inside updateEditOrderEmailAttributes method of UpdateEditOrderEmailAttribs class. changeOrder_InputXML: "+ SCXmlUtil.getString(changeOrderInputDoc));
								}
								Document changeOrderOutputDoc = api.invoke(env, XPXLiterals.CHANGE_ORDER_API, changeOrderInputDoc);
								if (log.isDebugEnabled()) {
									log.debug("Inside updateEditOrderEmailAttributes method of UpdateEditOrderEmailAttribs class. changeOrder_OutputXML: "+ SCXmlUtil.getString(changeOrderOutputDoc));
								}							
							}							
						}						
					}
				}
				
			} else if("Customer".equals(inputOrderType)) {				
				Element orderHoldTypesElem = SCXmlUtil.getChildElement(editOrderInputDoc.getDocumentElement(), XPXLiterals.E_ORDER_HOLD_TYPES);
				if (orderHoldTypesElem != null) {
					List<Element> orderHoldTypeList =  SCXmlUtil.getChildren(orderHoldTypesElem, XPXLiterals.E_ORDER_HOLD_TYPE);
					if(orderHoldTypeList!=null) {
						boolean isPendingApprovalHold=false;
						for(Element orderHoldTypeElem : orderHoldTypeList) {
							String holdType = orderHoldTypeElem.getAttribute(XPXLiterals.A_HOLD_TYPE);
				        	String holdStatus = orderHoldTypeElem.getAttribute(XPXLiterals.A_STATUS);			        	
				        	if(!YFCObject.isNull(holdType) && holdType.equalsIgnoreCase(XPXLiterals.PENDING_APPROVAL_HOLD) 
				        			&& !YFCObject.isNull(holdStatus) && holdStatus.equalsIgnoreCase(XPXLiterals.HOLD_CREATED_STATUS_ID)) {
			    		              isPendingApprovalHold = true;
			    		              break;
			    			}
						}
						
						if(!isPendingApprovalHold) {
							Element editOrderExtnInputElem = SCXmlUtil.getChildElement(editOrderInputDoc.getDocumentElement(),"Extn");
							if(editOrderExtnInputElem != null) {
								editOrderExtnInputElem.setAttribute("ExtnLastOrderOperation", "OrderEdit");
								editOrderExtnInputElem.setAttribute("ExtnOrderConfirmationEmailSentFlag", "N");
							}
						}						
					}
				}				
			}
			
			if (log.isDebugEnabled()) {
				log.debug("UpdateEditOrderEmailAttribs_OutputXML: "+ SCXmlUtil.getString(editOrderInputDoc));
			}

		} catch (Exception e) {
			log.error("Error while executing updateEditOrderEmailAttributes method in UpdateEditOrderEmailAttribs class: "+ e.getMessage());
			prepareErrorObject(e, "OrderEdit", e.getMessage(), env, editOrderInputDoc);
		}

		return editOrderInputDoc;
	}	

	@Override
	public void setProperties(Properties arg0) throws Exception {
		this.props = props;

	}
	
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
