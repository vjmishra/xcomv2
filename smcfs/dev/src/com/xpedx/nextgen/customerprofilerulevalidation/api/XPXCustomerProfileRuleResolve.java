package com.xpedx.nextgen.customerprofilerulevalidation.api;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXCustomerProfileRuleResolve {
	private static YIFApi api = null;
	private static YFCLogCategory log;
	String changeOrderTemplate = "global/template/api/changeOrder.XPXRulesHoldResolve.xml";
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
	}
	static {
		try 
		{
			log = YFCLogCategory.instance(XPXCustomerProfileRuleValidation.class);
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
	public Document invokeXPXCustomerProfileRuleResolve(YFSEnvironment env,
			Document inXML) throws Exception {
		log.beginTimer("XPXCustomerProfileRuleResolve.invokeXPXCustomerProfileRuleResolve");
		boolean updateWebHoldFlag = false;
		Document changeOrderOutputDoc = null;
		
		updateWebHoldFlag = Boolean
		.parseBoolean(SCXmlUtil
				.getXpathAttribute(
						inXML.getDocumentElement(),
						"/Order/Extn/@ExtnWebHoldFlag='"
								+ "Y"
								+ "'"));
		
		boolean orderOnHold = Boolean
				.parseBoolean(SCXmlUtil
						.getXpathAttribute(
								inXML.getDocumentElement(),
								"/Order/OrderHoldTypes/OrderHoldType[@HoldType='"
										+ XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_TYPE
										+ "']/@Status='"
										+ XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_ACTIVE_STATUS
										+ "'"));
		
		if (orderOnHold) {			
			changeOrderOutputDoc = callChangeOrder(env,inXML,updateWebHoldFlag,true);
			return changeOrderOutputDoc;
		}else if (updateWebHoldFlag){
			changeOrderOutputDoc = callChangeOrder(env,inXML,updateWebHoldFlag,false);
			return changeOrderOutputDoc;
		}
		log.endTimer("XPXCustomerProfileRuleResolve.invokeXPXCustomerProfileRuleResolve");
		
		log.debug("The output returned from changeOrder of XPXCustomerProfileRuleResolve is:"+SCXmlUtil.getString(changeOrderOutputDoc));
		return inXML;
	}

	public Document callChangeOrder(YFSEnvironment env, Document inXML,
			boolean updateWebHoldFlag, boolean updateNeedAttentionHold)
			throws Exception {

		Document changeOrderDoc = YFCDocument.createDocument("Order")
				.getDocument();
		Element changeOrderElement = changeOrderDoc.getDocumentElement();
		String orderHeaderKey = inXML.getDocumentElement().getAttribute(
				"OrderHeaderKey");
		changeOrderElement.setAttribute("OrderHeaderKey", orderHeaderKey);
		if (updateNeedAttentionHold) {
			
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
							XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_INACTIVE_STATUS);
		}
		if (updateWebHoldFlag) {
			Element extnElement = SCXmlUtil.createChild(changeOrderElement,
					"Extn");
			extnElement.setAttribute("ExtnWebHoldFlag", "Y");
		}
		
		env.setApiTemplate(XPXLiterals.CHANGE_ORDER_API, changeOrderTemplate);
		Document changeOrderOutputDoc = api.invoke(env, "changeOrder",
				changeOrderDoc);
		env.clearApiTemplate(XPXLiterals.CHANGE_ORDER_API);
		
		
		return changeOrderOutputDoc;

	}
}
