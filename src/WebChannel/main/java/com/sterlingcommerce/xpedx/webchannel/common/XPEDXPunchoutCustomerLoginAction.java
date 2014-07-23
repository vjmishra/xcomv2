package com.sterlingcommerce.xpedx.webchannel.common;

import com.sterlingcommerce.webchannel.core.WCAction;

/**
 * @deprecated Since R16. Use PunchoutCxmlLoginAction instead. This action will be removed in R17. We're keeping it in R16 strictly for backwards compatibility for OCI customers.
 */
@Deprecated
public class XPEDXPunchoutCustomerLoginAction extends WCAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -2929835801646530741L;

	private String _loginID;
	private String _password;
	private String _sfId;
	private String _payLoadID;
	private String _operation;
	private String _orderHeaderKey;
	private String _returnURL;
	private String _selectedCategory;
	private String _selectedItem;
	private String _selectedItemUOM;
	private String _buyerCookie;
	private String _fromIdentity;
	private String _toIdentity;


	public String getCustomersForPunchout() {

		_loginID = (String) request.getParameter("u");
		_password = (String) request.getParameter("p");
		_sfId = request.getParameter("sfId");
		_payLoadID = (String) request.getParameter("payLoadID");
		_operation = (String) request.getParameter("operation");
		_orderHeaderKey = (String) request.getParameter("orderHeaderKey");
		_returnURL = (String) request.getParameter("returnURL");
		_selectedCategory = (String) request.getParameter("selectedCategory");
		_selectedItem = (String) request.getParameter("selectedItem");
		_selectedItemUOM = (String) request.getParameter("selectedItemUOM");
		_buyerCookie = (String) request.getParameter("buyerCookie");
		_fromIdentity = (String) request.getParameter("fromIdentity");
		_toIdentity = (String) request.getParameter("toIdentity");


		request.setAttribute("dum_username", _loginID);
		request.setAttribute("dum_password", _password);
		request.setAttribute("selected_storefrontId", _sfId);
		request.setAttribute("payLoadID", _payLoadID);
		request.setAttribute("operation", _operation);
		request.setAttribute("orderHeaderKey", _orderHeaderKey);
		request.setAttribute("returnURL", _returnURL);
		request.setAttribute("selectedCategory", _selectedCategory);
		request.setAttribute("selectedItem", _selectedItem);
		request.setAttribute("selectedItemUOM", _selectedItemUOM);
		request.setAttribute("buyerCookie", _buyerCookie);
		request.setAttribute("fromIdentity", _fromIdentity);
		request.setAttribute("toIdentity", _toIdentity);
		request.setAttribute("isProcurementUser", "Y");



		return WCAction.SUCCESS;
	}
}
