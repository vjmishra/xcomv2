package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

@SuppressWarnings("all")
public class XPEDXDeleteArticle extends WCMashupAction {

	private String articleData;
	private String customerId;
	public List articleNames;
	public List divisions;
	public List customerIDs;
	public List orgCode;

	public XPEDXDeleteArticle() {
		super();
		articleNames = new ArrayList();
		divisions = new ArrayList();
		customerIDs = new ArrayList();
		orgCode = new ArrayList();
	}

	public String execute() {
		String[] articles = articleData.split("\\|");
		for (int i = 0; i < articles.length; i++) {
			if (articles[i] != null && !("").equals(articles[i])) {
				articleNames.add(articles[i]);
				divisions.add("N/A");
				customerIDs.add(customerId);
				orgCode.add(getWCContext().getStorefrontId());

			}
		}
		try {

			Element outputDoc = prepareAndInvokeMashup("xpedxDeleteArticle");
		} catch (XMLExceptionWrapper e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		}
		// AJAX call, so returning none on success.
		return NONE;
	}

	public String getArticleData() {
		return articleData;
	}

	public void setArticleData(String articleData) {
		this.articleData = articleData;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public List getArticleNames() {
		return articleNames;
	}

	public void setArticleNames(List articleNames) {
		this.articleNames = articleNames;
	}

	public List getDivisions() {
		return divisions;
	}

	public void setDivisions(List divisions) {
		this.divisions = divisions;
	}

	public List getCustomerIDs() {
		return customerIDs;
	}

	public void setCustomerIDs(List customerIDs) {
		this.customerIDs = customerIDs;
	}

	public List getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(List orgCode) {
		this.orgCode = orgCode;
	}

}
