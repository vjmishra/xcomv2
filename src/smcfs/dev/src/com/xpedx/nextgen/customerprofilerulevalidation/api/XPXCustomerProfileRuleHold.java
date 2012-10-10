package com.xpedx.nextgen.customerprofilerulevalidation.api;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXCustomerProfileRuleHold {
	private static YIFApi api = null;
	private static YFCLogCategory log;
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
	}
	static {
		try 
		{
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
	public Document invokeCustomerProfileRuleHold(YFSEnvironment env,
			Document inXML) throws Exception {
		
		String orderType="";
		log.beginTimer("XPXCustomerProfileRuleHold.invokeCustomerProfileRuleHold");
		boolean orderOnHold = Boolean
				.parseBoolean(SCXmlUtil
						.getXpathAttribute(
								inXML.getDocumentElement(),
								"/Order/OrderHoldTypes/OrderHoldType[@HoldType='"
										+ XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_TYPE
										+ "']/@Status='"
										+ XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_ACTIVE_STATUS
										+ "'"));
		if (!orderOnHold){
			Document changeOrderDoc = YFCDocument.createDocument("Order")
					.getDocument();
			Element changeOrderElement = changeOrderDoc.getDocumentElement();
			String orderHeaderKey = inXML.getDocumentElement().getAttribute(
					"OrderHeaderKey");
			changeOrderElement.setAttribute("OrderHeaderKey", orderHeaderKey);
			
			Element extnElement = SCXmlUtil.createChild(changeOrderElement, "Extn");			
			orderType = inXML.getDocumentElement().getAttribute("OrderType");			
			// To set the override flag to 'Y' for a fulfillment order.
			if(orderType != null && !orderType.equalsIgnoreCase("Customer")){
				changeOrderElement.setAttribute("Override", "Y");
				extnElement.setAttribute("ExtnWebHoldFlag", "Y");
				extnElement.setAttribute("ExtnWebHoldReason", XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_DESC);
			}
			
			applyOrderRuleIDsToChangeOrder(changeOrderElement, inXML);
			
			Element orderHoldTypesElement = SCXmlUtil.createChild(
					changeOrderElement, "OrderHoldTypes");
			Element orderHoldTypeElement = SCXmlUtil.createChild(
					orderHoldTypesElement, "OrderHoldType");
			orderHoldTypeElement.setAttribute("HoldType",
					XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_TYPE);
			orderHoldTypeElement.setAttribute("ReasonText",
					XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_DESC);
			orderHoldTypeElement
					.setAttribute(
							"Status",
							XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_ACTIVE_STATUS);
			
			Document changeOrderOutputDoc = api.invoke(env, "changeOrder",
					changeOrderDoc);
			addNeedAttentionHoldTypesinOrderXML(inXML);			
		}
		log.endTimer("XPXCustomerProfileRuleHold.invokeCustomerProfileRuleHold");
		return inXML;
	}
	private void addNeedAttentionHoldTypesinOrderXML(Document inXML){
		Element holdTypeElement= SCXmlUtil
		.getXpathElement(
				inXML.getDocumentElement(),
				"/Order/OrderHoldTypes");
		if(holdTypeElement==null){
				holdTypeElement = SCXmlUtil.createChild(
					inXML.getDocumentElement(), "OrderHoldTypes");		
		}
		Element orderHoldTypeElement = SCXmlUtil.createChild(
				holdTypeElement, "OrderHoldType");
		orderHoldTypeElement.setAttribute("HoldType",
				XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_TYPE);
		orderHoldTypeElement.setAttribute("ReasonText",
				XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_DESC);
		orderHoldTypeElement
				.setAttribute(
						"Status",
						XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_ACTIVE_STATUS);
	}
	//Added for JIRA 4321
	private void applyOrderRuleIDsToChangeOrder(Element changeOrderElement, Document inXML){
		YFCDocument inXMLDoc= YFCDocument.getDocumentFor(inXML);
		YFCElement inXMLOrderExtn= inXMLDoc.getDocumentElement().getChildElement("Extn");
		if(inXMLOrderExtn!=null)
		{
			Element extnElement = (Element)changeOrderElement.getElementsByTagName("Extn").item(0);			
			String ordHdrLevelFailedRuleID = inXMLOrderExtn.getAttribute("ExtnOrdHdrLevelFailedRuleID");	
			extnElement.setAttribute("ExtnOrdHdrLevelFailedRuleID", ordHdrLevelFailedRuleID);
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
					
					YFCElement	extnInXMLOrdLine = inXMLOrderLine.getChildElement("Extn");
					if(extnInXMLOrdLine!=null)
					{
						Element extnChangeOrderLineElement = SCXmlUtil.createChild(changeOrderLineElement, "Extn");
						extnChangeOrderLineElement.setAttribute("ExtnOrdLineLevelFailedRuleID", extnInXMLOrdLine.getAttribute("ExtnOrdLineLevelFailedRuleID"));
						changeOrderLineElement.appendChild(extnChangeOrderLineElement);
						
					}
					changeOrderLinesElement.appendChild(changeOrderLineElement);
			
				}
			}
			changeOrderElement.appendChild(changeOrderLinesElement);
			
		}
		
	}
}
