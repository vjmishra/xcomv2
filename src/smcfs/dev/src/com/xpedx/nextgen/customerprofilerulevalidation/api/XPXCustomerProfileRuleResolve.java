package com.xpedx.nextgen.customerprofilerulevalidation.api;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXCustomerProfileRuleResolve {
	
	private static YIFApi api = null;
	private static YFCLogCategory log;
	String changeOrderTemplate = "global/template/api/changeOrder.XPXRulesHoldResolve.xml";
	
	static {
		try {
			log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {	
			e1.printStackTrace();
		}	
	}
	
	/**
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document invokeXPXCustomerProfileRuleResolve(YFSEnvironment env, Document inXML) throws Exception {
		
		boolean orderConfirmationFlow = false;
		boolean updateWebHoldFlag = false;
		boolean orderOnHold = false;
		Document changeOrderOutputDoc = null;
		
		if (log.isDebugEnabled()) {
			log.debug("XPXCustomerProfileRuleResolve-InXML:" + SCXmlUtil.getString(inXML));
		}
		
		Element rootElem = inXML.getDocumentElement();
		if (rootElem.hasAttribute("OrderConfirmationFlow")) {
			orderConfirmationFlow = true;
		}
		
		updateWebHoldFlag = Boolean.parseBoolean(SCXmlUtil.getXpathAttribute(rootElem,"/Order/Extn/@ExtnWebHoldFlag='"+ "Y"+ "'"));
		
		orderOnHold = Boolean.parseBoolean(SCXmlUtil.getXpathAttribute(rootElem,"/Order/OrderHoldTypes/OrderHoldType[@HoldType='"
										+ XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_TYPE
										+ "']/@Status='"
										+ XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_ACTIVE_STATUS
										+ "'"));
		
		if (orderOnHold) {			
			changeOrderOutputDoc = callChangeOrder(env,inXML,updateWebHoldFlag,true);
			return changeOrderOutputDoc;
		} else if (updateWebHoldFlag) {
			if (orderConfirmationFlow) {
				// Web Hold Flag Will Be Updated In The Later Part Of OP Flow In Service XPXCreateChainedOrderService.
				return inXML;
			} else {
				changeOrderOutputDoc = callChangeOrder(env,inXML,updateWebHoldFlag,false);
				return changeOrderOutputDoc;
			}
		}
		
		return inXML;
	}

	public Document callChangeOrder(YFSEnvironment env, Document inXML,
			boolean updateWebHoldFlag, boolean updateNeedAttentionHold)
			throws Exception {

		Document changeOrderDoc = YFCDocument.createDocument("Order").getDocument();
		Element changeOrderElement = changeOrderDoc.getDocumentElement();
		String orderHeaderKey = inXML.getDocumentElement().getAttribute("OrderHeaderKey");
		changeOrderElement.setAttribute("OrderHeaderKey", orderHeaderKey);
		
		if (updateNeedAttentionHold) {	
			Element orderHoldTypesElement = SCXmlUtil.createChild(changeOrderElement, "OrderHoldTypes");
			Element orderHoldTypeElement = SCXmlUtil.createChild(orderHoldTypesElement, "OrderHoldType");
			orderHoldTypeElement.setAttribute("HoldType", XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_TYPE);
			orderHoldTypeElement.setAttribute("ReasonText", XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_DESC);
			orderHoldTypeElement.setAttribute("Status", XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_INACTIVE_STATUS);
		}
		Element extnElement = SCXmlUtil.createChild(changeOrderElement, "Extn");
		if (updateWebHoldFlag) {			
			extnElement.setAttribute("ExtnWebHoldFlag", "Y");
		}
		clearOrderRuleIDsInChangeOrderDoc(changeOrderElement, inXML);
		
		env.setApiTemplate(XPXLiterals.CHANGE_ORDER_API, changeOrderTemplate);
		Document changeOrderOutputDoc = api.invoke(env, "changeOrder",changeOrderDoc);
		env.clearApiTemplate(XPXLiterals.CHANGE_ORDER_API);
		
		return changeOrderOutputDoc;
	}
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
	}
	
	//Added for JIRA 4321
	private void clearOrderRuleIDsInChangeOrderDoc(Element changeOrderElement, Document inXML){
		YFCDocument inXMLDoc= YFCDocument.getDocumentFor(inXML);
		YFCElement inXMLOrderExtn= inXMLDoc.getDocumentElement().getChildElement("Extn");
		if(inXMLOrderExtn!=null)
		{
			Element extnElement = (Element)changeOrderElement.getElementsByTagName("Extn").item(0);
			extnElement.setAttribute("ExtnOrdHdrLevelFailedRuleID", "");			
		}
		
		YFCElement inXMLOrderLines = inXMLDoc.getDocumentElement().getChildElement("OrderLines");
		if(inXMLOrderLines!=null)
		{
			Element changeOrderLinesElement = SCXmlUtil.createChild(changeOrderElement, "OrderLines");
			
			YFCIterable<YFCElement> inXMLOrderLineItr = inXMLOrderLines.getChildren("OrderLine");
			while (inXMLOrderLineItr.hasNext()) {
				YFCElement inXMLOrderLine = inXMLOrderLineItr.next();
				if (inXMLOrderLine != null) {
					String lineType=inXMLOrderLine.getAttribute("LineType");
					if(lineType==null || "".equals(lineType) || "M".equals(lineType) || "C".equals(lineType))
					{
						continue;
					}
					
					Element changeOrderLineElement = SCXmlUtil.createChild(changeOrderLinesElement, "OrderLine");					
					changeOrderLineElement.setAttribute("OrderLineKey", inXMLOrderLine.getAttribute("OrderLineKey"));
					
					YFCElement extnInXMLOrdLine = inXMLOrderLine.getChildElement("Extn");
					if(extnInXMLOrdLine!=null)
					{
						Element extnChangeOrderLineElement = SCXmlUtil.createChild(changeOrderLineElement, "Extn");
						extnChangeOrderLineElement.setAttribute("ExtnOrdLineLevelFailedRuleID", "");
						changeOrderLineElement.appendChild(extnChangeOrderLineElement);						
					}
					changeOrderLinesElement.appendChild(changeOrderLineElement);			
				}
			}
			changeOrderElement.appendChild(changeOrderLinesElement);
			
		}		
	}	
}
