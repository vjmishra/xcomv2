package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;

import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.order.ShippingOptionAction;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.profile.org.XPEDXOverriddenShipToAddress;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPEDXShippingOptionAction extends ShippingOptionAction {
	private final static Logger log = Logger
			.getLogger(XPEDXShippingOptionAction.class);

	XPEDXShipToCustomer shpCustomer;
	List arrAddress;

	public XPEDXShipToCustomer getShpCustomer() {
		return shpCustomer;
	}

	public void setShpCustomer(XPEDXShipToCustomer shpCustomer) {
		this.shpCustomer = shpCustomer;
	}

	String returnVal;
	XPEDXOverriddenShipToAddress shipToAddress = null;
	YFSEnvironment env;
	ISCUITransactionContext scuiTransactionContext;

	private String xpedxSTName = "";
	private String xpedxSTStreet = "";
	private String xpedxSTAddressLine2 = "";
	private String xpedxSTAddressLine3 = "";
	private String xpedxSTCity = "";
	private String xpedxSTState = "";
	private String xpedxSTZip = "";

	public String execute() {

		returnVal = super.execute();

		YFCDocument inputDocument = YFCDocument.createDocument("Order");
		YFCElement documentElement = inputDocument.getDocumentElement();
		documentElement.setAttribute("OrderHeaderKey", (String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext"));
		YFCElement eleElement = inputDocument.createElement("PersonInfoShipTo");
		shipToAddress = XPEDXWCUtils.getShipToOveriddenAddress(wcContext);
		if (shipToAddress == null) {
			try {
				shpCustomer = XPEDXWCUtils.getShipToAdress(wcContext
						.getCustomerId(), wcContext.getStorefrontId());
				if(log.isDebugEnabled()){
				log.debug(shpCustomer.getFirstName());
				}
				List arrAddress = shpCustomer.getAddressList();
				String sAddr = "AddressLine";
				if (arrAddress != null && arrAddress.size() > 0) {
					for (int i = 0; i < arrAddress.size(); i++) {
						eleElement.setAttribute(sAddr + i, arrAddress.get(i)
								.toString());
					}
				}

				else {
					eleElement.setAttribute("AddressLine1", "");
					eleElement.setAttribute("AddressLine2", "");
					eleElement.setAttribute("AddressLine3", "");
					eleElement.setAttribute("AddressLine4", "");
					eleElement.setAttribute("AddressLine5", "");
					eleElement.setAttribute("AddressLine6", "");
				}
				eleElement.setAttribute("AlternateEmailID", "");
				eleElement.setAttribute("Beeper", "");
				eleElement.setAttribute("City", shpCustomer.getCity());
				eleElement.setAttribute("Company", shpCustomer.getCompany());
				eleElement.setAttribute("Country", "");
				eleElement.setAttribute("DayFaxNo", "");
				eleElement.setAttribute("DayPhone", "");
				eleElement.setAttribute("Department", "");
				eleElement.setAttribute("EMailID", shpCustomer.getEMailID());
				eleElement.setAttribute("EveningFaxNo", "");
				eleElement.setAttribute("EveningPhone", "");
				eleElement
						.setAttribute("FirstName", shpCustomer.getFirstName());
				eleElement.setAttribute("IsCommercialAddress", "");
				eleElement.setAttribute("JobTitle", "");
				eleElement.setAttribute("LastName", shpCustomer.getLastName());
				eleElement.setAttribute("MiddleName", shpCustomer
						.getMiddleName());
				eleElement.setAttribute("MobilePhone", "");
				eleElement.setAttribute("OtherPhone", "");
				eleElement
						.setAttribute("PersonID", shpCustomer.getCustomerID());
				eleElement.setAttribute("State", "");
				eleElement.setAttribute("Suffix", "");
				eleElement.setAttribute("TaxGeoCode", "");
				eleElement.setAttribute("Title", "");
				eleElement.setAttribute("ZipCode", "");
				documentElement.appendChild(eleElement);
			} catch (CannotBuildInputException e) {
				e.printStackTrace();
			}
		} else {
			/**** ship to address !=null ***/
			eleElement.setAttribute("AddressLine1", "");
			eleElement.setAttribute("AddressLine2", shipToAddress
					.getXpedxSTAddressLine2());
			eleElement.setAttribute("AddressLine3", shipToAddress
					.getXpedxSTAddressLine3());
			eleElement.setAttribute("AddressLine4", "");
			eleElement.setAttribute("AddressLine5", "");
			eleElement.setAttribute("AddressLine6", "");

			eleElement.setAttribute("AlternateEmailID", "");
			eleElement.setAttribute("Beeper", "");
			eleElement.setAttribute("City", shipToAddress.getXpedxSTCity());
			eleElement.setAttribute("Company", "");
			eleElement.setAttribute("Country", "");
			eleElement.setAttribute("DayFaxNo", "");
			eleElement.setAttribute("DayPhone", "");
			eleElement.setAttribute("Department", "");
			eleElement.setAttribute("EMailID", "");
			eleElement.setAttribute("EveningFaxNo", "");
			eleElement.setAttribute("EveningPhone", "");
			eleElement
					.setAttribute("FirstName", shipToAddress.getXpedxSTName());
			eleElement.setAttribute("IsCommercialAddress", "");
			eleElement.setAttribute("JobTitle", "");
			eleElement.setAttribute("LastName", "");
			eleElement.setAttribute("MiddleName", "");
			eleElement.setAttribute("MobilePhone", "");
			eleElement.setAttribute("OtherPhone", "");
			eleElement.setAttribute("PersonID", "");
			eleElement.setAttribute("State", shipToAddress.getXpedxSTState());
			eleElement.setAttribute("Suffix", "");
			eleElement.setAttribute("TaxGeoCode", "");
			eleElement.setAttribute("Title", "");
			eleElement.setAttribute("ZipCode", shipToAddress.getXpedxSTZip());
			documentElement.appendChild(eleElement);
			if(log.isDebugEnabled()){
		     log.debug("shipToAddress" + shipToAddress.getXpedxSTName());
			}
		}
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		documentElement.setAttribute("Action", "MODIFY");
		scuiTransactionContext =wSCUIContext
				.getTransactionContext(true);
		env = (YFSEnvironment) scuiTransactionContext
				.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);

		try {
			YIFApi api = YIFClientFactory.getInstance().getApi();
			api.invoke(env, "changeOrder", inputDocument.getDocument());
		} catch (Exception e) {
			log.error(e);
		} finally {
			SCUITransactionContextHelper.releaseTransactionContext(
					scuiTransactionContext, wSCUIContext);
			scuiTransactionContext = null;
			env = null;
		}
		return returnVal;
	}

}
