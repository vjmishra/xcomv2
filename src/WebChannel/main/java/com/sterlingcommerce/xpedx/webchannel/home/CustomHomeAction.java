package com.sterlingcommerce.xpedx.webchannel.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.common.CookieUtil;
import com.sterlingcommerce.xpedx.webchannel.punchout.DivisionBean;
import com.sterlingcommerce.xpedx.webchannel.punchout.ShipToCustomerBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class CustomHomeAction extends WCAction {

	@Override
	public String execute() throws Exception
	{
		super.execute();

		String aribaFlag = (String) wcContext.getSCUIContext().getSession(false).getAttribute("aribaFlag");

		if (aribaFlag!=null && aribaFlag.equals("Y"))
		{

			getAllAssignedShiptosWithDivisionsForAUser();
			if (divisionBeanList != null && divisionBeanList.size() >0) {
				DivisionBean divisionBean = divisionBeanList.get(0);
				ArrayList<ShipToCustomerBean>  shipToCustomers = divisionBean.getShipToCustomrs();

				if(shipToCustomers!=null && shipToCustomers.size()>0){
					ShipToCustomerBean shipToCustomerBean = shipToCustomers.get(0);
					XPEDXWCUtils.setCurrentCustomerIntoContext(shipToCustomerBean.getMSAPCustomerID(),wcContext);

					wcContext.setWCAttribute("isPunchoutUser", "true",WCAttributeScope.LOCAL_SESSION);

					// eb-4953: also create a cookie so we can properly handle a session timeout
					SCUIContext scuiContext = getWCContext().getSCUIContext();
					CookieUtil.setCookie(scuiContext.getRequest(), scuiContext.getResponse(), CookieUtil.PUNCHOUT, "true");

					wcContext.setWCAttribute("isTACheckReqForPunchoutUser", "Y",WCAttributeScope.LOCAL_SESSION);
					if(divisionBeanList.size()==1 && shipToCustomers.size()==1 ){
						wcContext.getSCUIContext().getSession(false).removeAttribute("aribaFlag");
						prefferedShipToCustomer=shipToCustomerBean.getShipToCustomerID();
						return "changePreferredShip";
					}
				}
			}



			wcContext.getSCUIContext().getSession(false).removeAttribute("aribaFlag");

			setPunchoutMessage(XPEDXWCUtils.getCustomerPunchoutMessage(wcContext));

			// Remove data from session also
			return "punchout";

		} else {
			return "main";
		}
	}

	private void getAllAssignedShiptosWithDivisionsForAUser() {

		Map<String, DivisionBean> divisionBeanMap = new HashMap<String, DivisionBean>();
		Document inputDoc = SCXmlUtil.createDocument("XPXCustomerAssignmentView");
		inputDoc.getDocumentElement().setAttribute("UserId", wcContext.getLoggedInUserId());

		Object obj = WCMashupHelper.invokeMashup("XPEDXGetAllShipToList-Punchout",inputDoc.getDocumentElement(), wcContext.getSCUIContext());
		Element custAssignedEle = (Element) obj;
		ArrayList<Element> assignedCustElems = SCXmlUtil.getElements(custAssignedEle, "XPXCustomerAssignmentView");
		ArrayList<ShipToCustomerBean> assignedShipToCustomersBean;
		DivisionBean divisionBean;
		if (assignedCustElems.size() > 0) {
			for (int i = 0; i < assignedCustElems.size(); i++) {
				Element customer = assignedCustElems.get(i);
				String division_id = SCXmlUtil.getAttribute(customer,"ExtnCustomerDivisionID");
				divisionBean = divisionBeanMap.get(division_id);
				if (divisionBean == null) {
					divisionBean = new DivisionBean();
					divisionBean.setDivisionId(SCXmlUtil.getAttribute(customer,"ExtnCustomerDivisionID"));
					divisionBean.setDivisionName(SCXmlUtil.getAttribute(customer,"DivisionName"));
				}
				assignedShipToCustomersBean = divisionBean.getShipToCustomrs();
				if (assignedShipToCustomersBean == null) {
					assignedShipToCustomersBean = new ArrayList<ShipToCustomerBean>();
				}
				assignedShipToCustomersBean.add(setShipToCustomer(customer));
				Collections.sort(assignedShipToCustomersBean);
				divisionBean.setShipToCustomrs(assignedShipToCustomersBean);
				divisionBeanMap.put(division_id, divisionBean);
			}
		}

		if(divisionBeanMap!=null){
			divisionBeanList = new ArrayList<DivisionBean>(divisionBeanMap.values());
			Collections.sort(divisionBeanList);
			XPEDXWCUtils.setObectInCache("divisionBeanList",divisionBeanList);
		}

	}

	//	/**
	//	 * Performs API call to get customer's punchout message.
	//	 * @return
	//	 * @throws Exception
	//	 */
	//	private String getCustomerPunchoutMessage() throws Exception {
	//		IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
	//		SCUIContext wSCUIContext = context.getSCUIContext();
	//		ISCUITransactionContext scuiTransactionContext = wSCUIContext.getTransactionContext(true);
	//		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
	//		YIFApi api = YIFClientFactory.getInstance().getApi();
	//
	//		String masterCustomerId = (String) wcContext.getSCUIContext().getSession(false).getAttribute("loggedInCustomerID");
	//
	//		// TODO ideally we wouldn't have to make an api call just to get this specific data. is the master customer stored in the session?
	//					// if so, can we tweak the code that puts it there to also fetch the ExtnPunchOutComments attribute?
	//
	//		Document outputTemplate = SCXmlUtil.createFromString(""
	//				+ "<Customer CustomerID=\"\">"
	//				+ "  <Extn ExtnPunchOutComments=\"\" />"
	//				+ "</Customer>");
	//		env.setApiTemplate("getCustomerDetails", outputTemplate);
	//
	//		Document getCustomerDetailsInputDoc = YFCDocument.createDocument("Customer").getDocument();
	//		getCustomerDetailsInputDoc.getDocumentElement().setAttribute("OrganizationCode", context.getStorefrontId());
	//		getCustomerDetailsInputDoc.getDocumentElement().setAttribute("CustomerID", masterCustomerId);
	//
	//		Document getCustomerDetailsOutputDoc = api.invoke(env, "getCustomerDetails", getCustomerDetailsInputDoc);
	//
	//		env.clearApiTemplate("getCustomerDetails");
	//
	//		Element extnElem = SCXmlUtil.getElements(getCustomerDetailsOutputDoc.getDocumentElement(), "Extn").get(0);
	//		return extnElem.getAttribute("ExtnPunchOutComments");
	//	}

	private ArrayList<DivisionBean> divisionBeanList;

	public ArrayList<DivisionBean> getDivisionBeanList() {
		return divisionBeanList;
	}

	public void setDivisionBeanList(ArrayList<DivisionBean> divisionBeanList) {
		this.divisionBeanList = divisionBeanList;
	}
	private String prefferedShipToCustomer;

	public String getPrefferedShipToCustomer() {
		return prefferedShipToCustomer;
	}

	public void setPrefferedShipToCustomer(String prefferedShipToCustomer) {
		this.prefferedShipToCustomer = prefferedShipToCustomer;
	}

	private ShipToCustomerBean setShipToCustomer(Element customer){

		ShipToCustomerBean shipToCustomerBean = new ShipToCustomerBean();
		shipToCustomerBean.setUserId(SCXmlUtil.getAttribute(customer,"UserId"));
		shipToCustomerBean.setShipToCustomerID(SCXmlUtil.getAttribute(customer,"ShipToCustomerID"));
		shipToCustomerBean.setEnterpriseCode(SCXmlUtil.getAttribute(customer,"EnterpriseCode"));
		shipToCustomerBean.setFirstName(SCXmlUtil.getAttribute(customer,"FirstName"));
		shipToCustomerBean.setLastName(SCXmlUtil.getAttribute(customer,"LastName"));
		shipToCustomerBean.setBillToCustomerID(SCXmlUtil.getAttribute(customer,"BillToCustomerID"));
		shipToCustomerBean.setSAPCustomerID(SCXmlUtil.getAttribute(customer,"SAPCustomerID"));
		shipToCustomerBean.setMSAPCustomerID(SCXmlUtil.getAttribute(customer,"MSAPCustomerID"));
		shipToCustomerBean.setShipToExtnCustStoreNo(SCXmlUtil.getAttribute(customer,"ShipToExtnCustStoreNo"));
		shipToCustomerBean.setAddressLine1(SCXmlUtil.getAttribute(customer,"AddressLine1"));
		shipToCustomerBean.setAddressLine2(SCXmlUtil.getAttribute(customer,"AddressLine2"));
		shipToCustomerBean.setAddressLine3(SCXmlUtil.getAttribute(customer,"AddressLine3"));
		shipToCustomerBean.setCity(SCXmlUtil.getAttribute(customer,"City"));
		shipToCustomerBean.setState(SCXmlUtil.getAttribute(customer,"State"));
		shipToCustomerBean.setCountry(SCXmlUtil.getAttribute(customer,"Country"));
		shipToCustomerBean.setZipCode(SCXmlUtil.getAttribute(customer,"ZipCode"));
		shipToCustomerBean.setEmailID(SCXmlUtil.getAttribute(customer,"EMailID"));
		shipToCustomerBean.setShipToCustomerName(SCXmlUtil.getAttribute(customer,"ShipToCustomerName"));
		shipToCustomerBean.setBillToCustomerName(SCXmlUtil.getAttribute(customer,"BillToCustomerName"));
		shipToCustomerBean.setShipToAddressString(SCXmlUtil.getAttribute(customer,"ShipToAddressString"));
		shipToCustomerBean.setStatus(SCXmlUtil.getAttribute(customer,"Status"));

		String shipToCustomerID = shipToCustomerBean.getShipToCustomerID();
		String shipToCustomer = shipToCustomerID.substring(0,shipToCustomerID.lastIndexOf("-M-XX-S"));

		String zipCode=shipToCustomerBean.getZipCode();
		String firstZip=zipCode;
		String lastZip="";

		if(zipCode!=null && zipCode.length()>5){
		    firstZip=zipCode.substring(0, 5);
		    lastZip="-"+zipCode.substring(5);
		  }


		String ShipToDisplayString = shipToCustomerBean.getShipToCustomerName() + " ("+ shipToCustomer + ") " + shipToCustomerBean.getAddressLine1() + ", " + shipToCustomerBean.getCity()+ ", " +shipToCustomerBean.getState()+", "+ firstZip + lastZip + ", " + shipToCustomerBean.getCountry();

		shipToCustomerBean.setShipToDisplayString(ShipToDisplayString);
		return shipToCustomerBean;
	}

	private String punchoutMessage;

	public String getPunchoutMessage() {
		return punchoutMessage;
	}

	public void setPunchoutMessage(String punchoutMessage) {
		this.punchoutMessage = punchoutMessage;
	}

}

