package com.sterlingcommerce.xpedx.webchannel.profile.user;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

@SuppressWarnings("serial")
public class XPEDXAnonymousQuickLinkAction extends WCMashupAction {
	private String codeType = null;
	private Map<String, String> quickLinkMap = null;

	@Override
	public String execute() {

		setCodeType("QLAnonymous");

		Element outputElement = null;
		try {
			outputElement = prepareAndInvokeMashup("xpedxGetCustomCommonCodesForQuickLinks");
			String inputXml = SCXmlUtil.getString(outputElement);
			LOG.debug("Input XML: " + inputXml);
		} catch (XMLExceptionWrapper e) {
			LOG.debug("Not able to retrieve Quick Link for Anonymous user :->"
					+ e.getMessage());
			return ERROR;
		} catch (CannotBuildInputException e) {
			LOG.debug("Not able to build Quick Link for Anonymous user:->"
					+ e.getMessage());
			return ERROR;
		}

		if (null != outputElement) {
			Map<String, String> map = getAnonymousQuickLink(outputElement);
			setQuickLinkMap(map);
		} else {
			return ERROR;
		}

		return SUCCESS;
	}

	private Map<String, String> getAnonymousQuickLink(Element outputElement) {
		Map<String, String> map = new HashMap<String, String>();
		NodeList list = outputElement.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Element ele = (Element) list.item(i);
			map.put(ele.getAttribute("CodeValue"),
					ele.getAttribute("CodeShortDescription"));
		}
		return map;
	}

	public String getCodeType() {
		return codeType;
	}

	public Map<String, String> getQuickLinkMap() {
		return quickLinkMap;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public void setQuickLinkMap(Map<String, String> quickLinkMap) {
		this.quickLinkMap = quickLinkMap;
	}
}
