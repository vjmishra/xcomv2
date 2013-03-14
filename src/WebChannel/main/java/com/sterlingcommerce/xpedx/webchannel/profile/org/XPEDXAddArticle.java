/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;

/**
 * @author rugrani
 * 
 */
@SuppressWarnings("all")
public class XPEDXAddArticle extends WCMashupAction {

	private static final Logger log = Logger.getLogger(XPEDXAddArticle.class);

	public Document articleOutputDoc = null;

	protected String customerId;

	public String execute() {
		try {
			Element listInput = WCMashupHelper.getMashupAPI("XPEDXAddArticle",
					getWCContext().getSCUIContext());
			prepareAndInvokeMashups();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	public Document getArticleOutputDoc() {
		return articleOutputDoc;
	}

	public String getShipToCustomerCustomerID() {
		String customerCustomerId = null;
		try {

			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/Customer/@OrganizationCode",
					wcContext.getStorefrontId());
			valueMap.put("/Customer/@CustomerID", wcContext.getCustomerId());

			Element input = WCMashupHelper.getMashupInput(
					"XPEDXParentCustomerList", valueMap, getWCContext()
							.getSCUIContext());

			Object obj = WCMashupHelper.invokeMashup("XPEDXParentCustomerList",
					input, getWCContext().getSCUIContext());

			Document outputDoc = ((Element) obj).getOwnerDocument();

			String xml = SCXmlUtil.getString(outputDoc);

			NodeList nlCustomer = outputDoc.getElementsByTagName("Customer");
			Element customer = null;
			int length = nlCustomer.getLength();
			for (int i = 0; i < length; i++) {
				customer = (Element) nlCustomer.item(i);
				String custID = SCXmlUtil.getAttribute(customer, "CustomerID");
				if (i == 2) {
					customerCustomerId = custID;
				}
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (customerCustomerId == null) {
			log.error("There is no customer's customer id is associated for ship to id "
					+ customerId + " setting creating article for ship to id");
			customerCustomerId = getWCContext().getCustomerId();
		}
		return customerCustomerId;
	}

	public String getCustomerId() {
		// Document
		// sapCustomerDoc=((Document)wcContext.getSCUIContext().getRequest().getSession().getAttribute("sapCustExtnFields"));
		XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
		Document sapCustomerDoc = (Document) XPEDXWCUtils
				.getObjectFromCache("sapCustExtnFields");
		if (sapCustomerDoc != null) {
			customerId = sapCustomerDoc.getDocumentElement().getAttribute(
					"CustomerID");

		}// getShipToCustomerCustomerID();
		if (YFCCommon.isVoid(customerId)) {
			customerId = wcContext.getCustomerId();
		}
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
