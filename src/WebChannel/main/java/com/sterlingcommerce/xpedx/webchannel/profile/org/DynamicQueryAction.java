package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.profile.user.XPEDXUserGeneralInfo;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DynamicQueryAction extends WCMashupAction {

	private static final long serialVersionUID = 7756731378709085664L;
	private static final Logger log = Logger
			.getLogger(DynamicQueryAction.class);

	private String PrntChildComb = null;
	private String customerContactId = null;
	private String customerID = null;
	private List<String> assignedCustomerList = null;

	public String execute() {

		String output = "None";
		String customerContactId;
		try {

			getAuthorizedLocation(wcContext);
			getMsapCustomerAndAssignedCustomers(wcContext);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SUCCESS;

	}

	public void getAuthorizedLocation(IWCContext wcContext) throws Exception {
		Document outDoc = null;
		Object obj = null;
		Boolean isCustomerIDFromContext;
		try {
			String rootCustomerKey = (String) wcContext
					.getWCAttribute(XPEDXWCUtils.LOGGED_IN_CUSTOMER_KEY);

			customerContactId = wcContext.getSCUIContext().getRequest()
					.getParameter("customerContactId");
			customerID = wcContext.getSCUIContext().getRequest()
					.getParameter("customerId");
			if (log.isDebugEnabled()) {
				log.debug("customerContactID from Request=" + customerContactId);
			}
			if (customerContactId == null) {
				customerContactId = getWCContext().getCustomerContactId(); // m.balkhi

			}
			if (customerID == null) {

				customerID = wcContext.getCustomerId();
			}

			assignedCustomerList = (ArrayList<String>) XPEDXWCUtils
					.getAssignedCustomers(customerContactId);

			XPEDXWCUtils.setObectInCache("AUTHORIZED_LOCATIONS",
					assignedCustomerList);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void getMsapCustomerAndAssignedCustomers(IWCContext wcContext)
			throws Exception {

		Document outDoc = null;
		Object obj = null;
		assignedCustomerList = (List) XPEDXWCUtils
				.getObjectFromCache("AUTHORIZED_LOCATIONS");
		String rootCustomerKey = (String) wcContext
				.getWCAttribute(XPEDXWCUtils.LOGGED_IN_CUSTOMER_KEY);

		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/CustomerContact/@CustomerKey", rootCustomerKey);
		Element input = null;
		try {
			input = WCMashupHelper.getMashupInput("XPXCustomerInfo", valueMap,
					wcContext.getSCUIContext());
		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = WCMashupHelper.invokeMashup("XPXCustomerInfo", input,
				wcContext.getSCUIContext());

		if (obj != null) {
			outDoc = ((Element) obj).getOwnerDocument();
			log.debug("XPXCustomerInfo" + SCXmlUtil.getString(outDoc));
		}
		String MsapId = SCXmlUtil.getXpathAttribute(
				outDoc.getDocumentElement(), "//Customer/@CustomerID");
		String grpVal = getCustomerInfo(wcContext, MsapId);
		if (assignedCustomerList != null
				&& assignedCustomerList.contains(MsapId)) {
			grpVal = grpVal + "^^^" + "checked";
		}
		PrntChildComb = MsapId + "##" + grpVal;
		try {
			getChildCustomers(wcContext, MsapId);
		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getChildCustomers(IWCContext wcContext, String sParentID)
			throws CannotBuildInputException {

		Map<String, String> valueMap = new HashMap<String, String>();
		assignedCustomerList = (List) XPEDXWCUtils
				.getObjectFromCache("AUTHORIZED_LOCATIONS");
		Document outDoc = null;
		Object obj = null;
		Element input;
		getCustomerInfo(wcContext, sParentID);

		valueMap.put("/Customer/@CustomerID", sParentID);
		valueMap.put("/Customer/@OrganizationCode", "xpedx");

		input = WCMashupHelper.getMashupInput("XPEDXMyItemsGetCustomersList",
				valueMap, wcContext.getSCUIContext());

		obj = WCMashupHelper.invokeMashup("XPEDXMyItemsGetCustomersList",
				input, wcContext.getSCUIContext());

		if (obj != null) {

			outDoc = ((Element) obj).getOwnerDocument();
			log.debug("XPEDXMyItemsGetCustomersList"
					+ SCXmlUtil.getString(outDoc));

			NodeList nlCustomerList = outDoc
					.getElementsByTagName("CustomerList");

			if (nlCustomerList != null && nlCustomerList.getLength() > 1) {
				int i = 1;
				Element eleCustomerList = (Element) nlCustomerList.item(i);
				NodeList nlCustomerListchild = eleCustomerList
						.getElementsByTagName("Customer");

				if (nlCustomerListchild != null
						&& nlCustomerListchild.getLength() > 0) {

					for (i = 0; i < nlCustomerListchild.getLength(); i++) {

						Element eleChildCustomer = (Element) nlCustomerListchild
								.item(i);
						String EleCustID = SCXmlUtil.getAttribute(
								eleChildCustomer, "CustomerID");

						String grpVal = getCustomerInfo(wcContext, EleCustID);
						if (assignedCustomerList != null
								&& assignedCustomerList.contains(EleCustID)) {
							grpVal = grpVal + "^^^" + "checked";
						}

						String tempValue = EleCustID + "##" + grpVal;
						PrntChildComb = PrntChildComb + "|" + tempValue;

					}
				}

			}
		} 

		return PrntChildComb;

	}

	public String getCustomerInfo(IWCContext wcContext, String sCustID)
			throws CannotBuildInputException {

		Map<String, String> valueMap = new HashMap<String, String>();
		Document outDoc = null;
		Object obj = null;
		Element input = null;
		String CmbString = null;
		String AddressLine1 = null;
		String City = null;
		String state = null;
		String zipcode = null;
		String country = null;
		valueMap.put("/Customer/@CustomerID", sCustID);
		valueMap.put("/Customer/@OrganizationCode", "xpedx");

		input = WCMashupHelper.getMashupInput("XPEDXGetCustomerInfo", valueMap,
				wcContext.getSCUIContext());

		obj = WCMashupHelper.invokeMashup("XPEDXGetCustomerInfo", input,
				wcContext.getSCUIContext());

		if (obj != null) {

			outDoc = ((Element) obj).getOwnerDocument();
			log.debug("XPEDXGetCustomerInfo"
					+ SCXmlUtil.getString(outDoc));

			String OrgName = SCXmlUtil.getXpathAttribute(
					outDoc.getDocumentElement(),
					"//Customer/BuyerOrganization/@OrganizationName");

			String OrgCode = SCXmlUtil.getXpathAttribute(
					outDoc.getDocumentElement(),
					"//Customer/BuyerOrganization/@OrganizationCode");

			
			if (OrgCode.endsWith("S") || OrgCode.endsWith("B")) {

				AddressLine1 = SCXmlUtil
						.getXpathAttribute(
								outDoc.getDocumentElement(),
								"//Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@AddressLine1");

				City = SCXmlUtil
						.getXpathAttribute(
								outDoc.getDocumentElement(),
								"//Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@City");

				state = SCXmlUtil
						.getXpathAttribute(
								outDoc.getDocumentElement(),
								"//Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@State");

				zipcode = SCXmlUtil
						.getXpathAttribute(
								outDoc.getDocumentElement(),
								"//Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@ZipCode");

				country = SCXmlUtil
						.getXpathAttribute(
								outDoc.getDocumentElement(),
								"//Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@Country");

				OrgCode = XPEDXWCUtils.formatBillToShipToCustomer(OrgCode);

				CmbString = OrgName + "(" + OrgCode + ")" + AddressLine1 + ","
						+ City + "," + state + "," + zipcode + "," + country;
			} else {
				CmbString = OrgName + "(" + OrgCode + ")";
			}

		}
		return CmbString;

	}

	public void setPrntChildComb(String p_sPrntChildComb) {

		this.PrntChildComb = p_sPrntChildComb;
	}

	public void setAssignedCustomerList(List p_lAssignedCustomerList) {
		this.assignedCustomerList = p_lAssignedCustomerList;

	}

	public List getAssignedCustomerList() {

		return assignedCustomerList;
	}

	public String getPrntChildComb() {

		return PrntChildComb;
	}

	public static void main(String[] args) throws JSONException {

	

	}

}

