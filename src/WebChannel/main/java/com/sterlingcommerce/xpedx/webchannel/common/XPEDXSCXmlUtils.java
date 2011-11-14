package com.sterlingcommerce.xpedx.webchannel.common;

import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.compat.SCXmlUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class XPEDXSCXmlUtils extends SCXmlUtils {

	@Override
	public String getAttribute(Element element, String attributeName) {
		String attributeValue = super.getAttribute(element, attributeName);
		boolean isImageOrContentLocation = "ContentLocation"
				.equals(attributeName)
				|| "ImageLocation".equals(attributeName);
		if (isImageOrContentLocation
				&& attributeValue != null
				&& (attributeValue.equals(XPEDXConstants.IMAGE_SERVER)
						|| attributeValue.equals(XPEDXConstants.CONTENT_SERVER_MSDS) || attributeValue
						.equals(XPEDXConstants.CONTENT_SERVER) || attributeValue
						.equals(XPEDXConstants.CONTENT_SERVER_FSC) || attributeValue
						.equals(XPEDXConstants.CONTENT_SERVER_PEFC) || attributeValue
						.equals(XPEDXConstants.CONTENT_SERVER_SFI))) {
			return XPEDXWCUtils.getServerLocation(attributeValue);
		}
		return attributeValue;
	}
	

}
