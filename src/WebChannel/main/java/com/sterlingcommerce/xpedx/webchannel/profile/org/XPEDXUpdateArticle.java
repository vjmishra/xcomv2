/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer;
import com.sterlingcommerce.webchannel.profile.ProfileUtility;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;

/**
 * @author vgovindan
 * 
 */
@SuppressWarnings("all")
public class XPEDXUpdateArticle extends WCMashupAction {

	private String customerId;
	private String customerContactId;

	// ResourceIds
	private static final String USER_LIST_RESOURCE_ID = "/swc/profile/ManageUserList";
	private static final String MANAGE_USER_PROFILE_RESOURCE_ID = "/swc/profile/ManageUserProfile";

	private static final Logger log = Logger
			.getLogger(XPEDXUpdateArticle.class);

	public XPEDXUpdateArticle() {
		super();
	}

	public String execute() {
		customerContactId = getWCContext().getCustomerContactId();
		String inputCustomerID = customerId;
		if (YFCCommon.isVoid(inputCustomerID)) {
			inputCustomerID = wcContext.getCustomerId();
		}
		if (!(ProfileUtility.isCustInCtxCustHierarchy(inputCustomerID,
				wcContext)))
			return ERROR;
		if (resourceId.equals(MANAGE_USER_PROFILE_RESOURCE_ID)) {
			try {
				if (!(ResourceAccessAuthorizer.getInstance().isAuthorized(
						USER_LIST_RESOURCE_ID, getWCContext()))) {
					if (!isSelfAdmin())
						return "";
				}
			} catch (Exception e1) {
				return ERROR;
			}
		}

		try {
			updateArticle();
		} catch (XMLExceptionWrapper e) {
			log.error(e);
			return ERROR;
		} catch (CannotBuildInputException e) {
			log.error(e);
			return ERROR;
		}
		return SUCCESS;

	}

	protected void updateArticle() throws XMLExceptionWrapper,
			CannotBuildInputException {
		Map outputMaps = prepareAndInvokeMashups();
		Element articleElement = (Element) outputMaps.get("XPEDXGetArticle");

	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public boolean isSelfAdmin() {
		String loggedInCustomerContactId = getWCContext()
				.getCustomerContactId();
		if (getCustomerContactId().compareTo(loggedInCustomerContactId) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getCustomerContactId() {
		return customerContactId;
	}

	public void setCustomerContactId(String customerContactId) {
		this.customerContactId = customerContactId;
	}

}
