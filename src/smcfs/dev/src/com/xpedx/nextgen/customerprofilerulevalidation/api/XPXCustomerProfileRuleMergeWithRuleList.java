package com.xpedx.nextgen.customerprofilerulevalidation.api;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXCustomerProfileRuleMergeWithRuleList {
	private static YIFApi api = null;
	private static YFCLogCategory log;

	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
	}

	static {
		try {
			log = YFCLogCategory
					.instance(XPXCustomerProfileRuleValidation.class);
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
	public Document invokeMerge(YFSEnvironment env, Document inXML)
			throws Exception {

		Document ruleDefnXml = YFCDocument.createDocument("XPXRuleDefn")
				.getDocument();
		Document outputRuleDefnXml = api.executeFlow(env, "XPXGetRuleDefnList",
				ruleDefnXml);

		Document outputCustomerProfileRuleXml = api.executeFlow(env,
				"XPXGetCustomerProfileRuleList", inXML);
		if(log.isDebugEnabled()){
			log.debug("*****1****"
					+ SCXmlUtil.getString(outputRuleDefnXml));
			log.debug("*****2****"
					+ SCXmlUtil.getString(outputCustomerProfileRuleXml));
		}
		NodeList nodeList = outputCustomerProfileRuleXml
				.getElementsByTagName("XPXCustomerRulesProfile");
		int length = nodeList.getLength();
		for (int counter = 0; counter < length; counter++) {
			Element CustomerProfilerRuleElement = (Element) nodeList
					.item(counter);
			if(log.isDebugEnabled()){
				log.debug("*****counter****"+"i"
					+ SCXmlUtil.getString(CustomerProfilerRuleElement));
			}
			String RuleKey = CustomerProfilerRuleElement
					.getAttribute("RuleKey");
			Element ruleElement = SCXmlUtil.getXpathElement(outputRuleDefnXml
					.getDocumentElement(), "XPXRuleDefn[@RuleKey ='" + RuleKey
					+ "']");
			if(log.isDebugEnabled()){
				log.debug("*****1****"
					+ SCXmlUtil.getString(ruleElement));
			}
			if(!YFCCommon.isVoid(ruleElement))
			{
				ruleElement.setAttribute("Param1", CustomerProfilerRuleElement
						.getAttribute("Param1"));
				ruleElement.setAttribute("Param2", CustomerProfilerRuleElement
						.getAttribute("Param2"));
				ruleElement.setAttribute("Param3", CustomerProfilerRuleElement
						.getAttribute("Param3"));
				ruleElement.setAttribute("Selected", "Y");				
			}

		}
		if(log.isDebugEnabled()){
			log.debug("*****Last****"
				+ SCXmlUtil.getString(outputRuleDefnXml));
		}
		return outputRuleDefnXml;
	}
}
