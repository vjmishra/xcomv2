package com.xpedx.nextgen.customerprofilerulevalidation.api;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
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
			
			// To set the override flag to 'Y' for a fulfillment order. 
			orderType = inXML.getDocumentElement().getAttribute("OrderType");
			if(orderType != null && !orderType.equalsIgnoreCase("Customer")){
				changeOrderElement.setAttribute("Override", "Y");
				Element extnElement = SCXmlUtil.createChild(changeOrderElement, "Extn");
				extnElement.setAttribute("ExtnWebHoldFlag", "Y");
				extnElement.setAttribute("ExtnWebHoldReason", XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_DESC);
			}
			
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
}
