package com.sterlingcommerce.xpedx.webchannel.common;

import com.sterlingcommerce.webchannel.core.WCAction;

public class XPEDXPunchoutCustomerLoginAction extends WCAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2929835801646530741L;

	private String _loginID;
	private String _password;
	private String _sfId;
	
	private String _payloadID;
	private String _operation;
	private String _orderHeaderKey;
	private String _returnURL;
	private String _selectedCategory;
	private String _selectedItem;
	private String _selectedItemUOM;
	private String _buyerCookie;
	private String _fromIdentity;
	private String _toIdentity;
	private String _preferredShipTo;

	public String getCustomersForPunchout() {

		_loginID = (String) request.getParameter("u");
		_password = (String) request.getParameter("p");
		_sfId = request.getParameter("sfId");
		
		_payloadID = (String) request.getParameter("payLoadID");
		_operation = (String) request.getParameter("operation");
		_orderHeaderKey = (String) request.getParameter("orderHeaderKey");
		_returnURL = (String) request.getParameter("returnURL");
		_selectedCategory = (String) request.getParameter("selectedCategory");
		_selectedItem = (String) request.getParameter("selectedItem");
		_selectedItemUOM = (String) request.getParameter("selectedItemUOM");
		_buyerCookie = (String) request.getParameter("buyerCookie");
		_fromIdentity = (String) request.getParameter("fromIdentity");
		_toIdentity = (String) request.getParameter("toIdentity");
		_preferredShipTo = (String) request.getParameter("preferredShipTo");

		request.setAttribute("dum_username", _loginID);
		request.setAttribute("dum_password", _password);
		request.setAttribute("selected_storefrontId", _sfId);
		
		request.setAttribute("_payloadID", _payloadID);
		request.setAttribute("_operation", _operation);
		request.setAttribute("_orderHeaderKey", _orderHeaderKey);
		request.setAttribute("_returnURL", _returnURL);
		request.setAttribute("_selectedCategory", _selectedCategory);
		request.setAttribute("_selectedItem", _selectedItem);
		request.setAttribute("_selectedItemUOM", _selectedItemUOM);
		request.setAttribute("_buyerCookie", _buyerCookie);
		request.setAttribute("_fromIdentity", _fromIdentity);
		request.setAttribute("_toIdentity", _toIdentity);
		request.setAttribute("_isProcurementUser", "Y");
		request.setAttribute("_preferredShipTo", _preferredShipTo);

		
		return WCAction.SUCCESS;
	}	
}
