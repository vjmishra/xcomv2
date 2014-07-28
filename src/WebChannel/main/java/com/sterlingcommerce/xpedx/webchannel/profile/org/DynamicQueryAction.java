package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

import org.apache.log4j.Logger;
import org.json.JSONException;
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

	@Override
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
		
		
		NodeList customerNodeList=(NodeList)XPEDXWCUtils.getObjectFromCache("MASTER_CUSTOMER_ELEMENT");
		XPEDXWCUtils.removeObectFromCache("MASTER_CUSTOMER_ELEMENT");		
		Element customerElement=(Element)customerNodeList.item(0);
		String MsapId=customerElement.getAttribute("CustomerID");
		Element buyerOrganizationElement=(Element)customerElement.getElementsByTagName("BuyerOrganization").item(0);
		String grpVal = getCustomerInfo(buyerOrganizationElement);
		//Commented for EB-309 perormance fix
		/*String rootCustomerKey = (String) wcContext
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
				outDoc.getDocumentElement(), "//Customer/@CustomerID");*/
		
		if (assignedCustomerList != null
				&& assignedCustomerList.contains(MsapId)) {
			grpVal = grpVal + "^^^" + "checked";
		}
		//Added for EB-4152 - Fixed the issue For single quote in Account name, throwing javascript error and not showing any locations in Profile page
		if(grpVal!=null){
			grpVal.replace("'", "\\'");
		}
		String status="30";
		//EB 4152 : replacing single quote, since its giving javascript error for displaying authorized locations
		if(grpVal!=null){
			grpVal.replace("'", "\\'");
		}
		PrntChildComb = MsapId + "##" + grpVal+"##"+status;
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
		//Commented for EB-309 perormance fix
		//getCustomerInfo(wcContext, sParentID);

		valueMap.put("/Customer/@ParentCustomerID", sParentID);
		//valueMap.put("/Customer/@OrganizationCode", "xpedx");
		valueMap.put("/Customer/@EnterpriseCode", wcContext.getStorefrontId());
		input = WCMashupHelper.getMashupInput("xpedxgetAllChildCustomers",
				valueMap, wcContext.getSCUIContext());

		obj = WCMashupHelper.invokeMashup("xpedxgetAllChildCustomers",
				input, wcContext.getSCUIContext());

		if (obj != null) {

			outDoc = ((Element) obj).getOwnerDocument();
			log.debug("XPEDXMyItemsGetCustomersList"
					+ SCXmlUtil.getString(outDoc));

			NodeList nlCustomerList = outDoc
					.getElementsByTagName("XPXChildCustomersView");

			for (int i = 0; nlCustomerList != null && nlCustomerList.getLength() > 0 && i < nlCustomerList.getLength() ; i++) 
			{
					
						Element eleChildCustomer = (Element) nlCustomerList.item(i);
						String EleCustID = SCXmlUtil.getAttribute(
								eleChildCustomer, "CustomerID");
						if(PrntChildComb != null && PrntChildComb.contains(EleCustID))
							continue;
						String grpVal = getCustomerInfo(eleChildCustomer); //getCustomerInfo(wcContext, EleCustID);
						if (assignedCustomerList != null
								&& assignedCustomerList.contains(EleCustID)) {
							grpVal = grpVal + "^^^" + "checked";
						}
						//EB 4152 : replacing single quote, since its giving javascript error for displaying authorized locations
						if(grpVal!=null)
						{
							grpVal =grpVal.replace("'", "\\'");
						}
						if(grpVal!=null)
						{
							grpVal =grpVal.replace("'", "\\'");
						}
						String status="30";
						String tempValue = EleCustID + "##" + grpVal+"##"+status;
						//EB 4152 : replacing single quote, since its giving javascript error for displaying authorized locations

						PrntChildComb = PrntChildComb + "|" + tempValue;

					}
		} 

		return PrntChildComb;

	}
	private String getCustomerInfo(Element eleChildCustomer)
	{
		
		String CmbString = null;
		String AddressLine1 = null;
		String City = null;
		String state = null;
		String zipcode = null;
		String country = null;
		String suspendedString = "";
		
		
		if(eleChildCustomer == null)
			return "";

			String OrgName = eleChildCustomer.getAttribute("OrganizationName");

			String OrgCode = eleChildCustomer.getAttribute("OrganizationCode");
			String extnSuffixType=eleChildCustomer.getAttribute("ChildExtnSuffixType");
			
			if ("S".equals(extnSuffixType) || "B".equals(extnSuffixType) || OrgCode.endsWith("S") || OrgCode.endsWith("B")) {

				AddressLine1 = eleChildCustomer.getAttribute("AddressLine1");

				City =eleChildCustomer.getAttribute("City");

				state = eleChildCustomer.getAttribute("State");

				zipcode = eleChildCustomer.getAttribute("ZipCode");

				country = eleChildCustomer.getAttribute("Country");
				
				//suspendedString = SCXmlUtil.getXpathAttribute(outDoc.getDocumentElement(),"//Customer/@Status")=="30"?"(Suspended)":"";
				String statusCode = eleChildCustomer.getAttribute("Status");
				if(statusCode!=null && statusCode.trim().length() > 0 && statusCode.trim().equals("30")){
				suspendedString = "(Suspended)";
				
		}

				OrgCode = XPEDXWCUtils.formatBillToShipToCustomer(OrgCode);

				CmbString = suspendedString+OrgName + "(" + OrgCode + ")" + AddressLine1 + ","
						+ City + "," + state + "," + zipcode + "," + country;
			} else {
				CmbString = OrgName + "(" + OrgCode + ")";
			}
		return CmbString;
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
		String suspendedString = "";
		valueMap.put("/Customer/@CustomerID", sCustID);
		//valueMap.put("/Customer/@OrganizationCode", "xpedx");
		valueMap.put("/Customer/@OrganizationCode", wcContext.getStorefrontId());
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

				//suspendedString = SCXmlUtil.getXpathAttribute(outDoc.getDocumentElement(),"//Customer/@Status")=="30"?"(Suspended)":"";
				String statusCode = SCXmlUtil.getXpathAttribute(outDoc.getDocumentElement(),"//Customer/@Status");
				if(statusCode!=null && statusCode.trim().length() > 0 && statusCode.trim().equals("30")){
				suspendedString = "(Suspended)";

				}

				OrgCode = XPEDXWCUtils.formatBillToShipToCustomer(OrgCode);

				zipcode = XPEDXWCUtils.getFormattedZipCode(zipcode);

				CmbString = suspendedString+OrgName + "(" + OrgCode + ")" + AddressLine1 + ","
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

