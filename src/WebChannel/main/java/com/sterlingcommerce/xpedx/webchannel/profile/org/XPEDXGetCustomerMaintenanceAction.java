/*
 * Action Class - Customer Maintenance Data.
 * 
 * @author - adsouza
 */

package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCDate;

public class XPEDXGetCustomerMaintenanceAction extends WCMashupAction {

	/**
     * 
     */
	private static final long serialVersionUID = 8047692617071059295L;

	public XPEDXGetCustomerMaintenanceAction() {
		// empList = new LinkedHashMap();
		empList = new ArrayList();
		empNames = new ArrayList();
		otherSalesRepList = new ArrayList();
		;

		// viewPrices = false;
		// canOrder = false;
		// canViewInv = false;
		// useCustSKU = false;
		// useOrderMulUOM = false;
		// sampleRequestFlag = false;

		// emailViewInvoices = false;
		previewInvoices = false;
		// acceptBackOrders = false;
		shipToOverrideFlag = false;
		orderUpdateFlag = false;
		shipComplete = false;

		CustLineAccNoFlag = false;
		CustLineSeqNoFlag = false;
		CustLinePONoFlag = false;
		CustLineField1Flag = false;
		CustLineField2Flag = false;
		CustLineField3Flag = false;

		myItemsLink = false;

		countriesMap = new HashMap<String, String>();

	}

	@Override
	public String execute() {
		try {
			outputDoc = prepareAndInvokeMashup("xpedx_getCustMaintenance");
			if (log.isDebugEnabled()) {
				log.debug("getCustomerDetails Output"
						+ SCXmlUtil.getString(outputDoc));
			}
			// added for 2769
			String lastModifiedDateStr = outputDoc.getAttribute("Modifyts");
			setCustContactId(outputDoc.getAttribute("Modifyuserid"));			
			Element custContactListEle = prepareAndInvokeMashup("getCustomerContactDetailsForAll");
			
			Element custContactEle = SCXmlUtil.getChildElement(custContactListEle, "CustomerContact");
			String firstName = custContactEle.getAttribute("FirstName");
			String lastName = custContactEle.getAttribute("LastName");		
			if (firstName != null && !("").equals(firstName)) {				
				setContactFirstName(firstName);
				setContactLastName(lastName);
			} else {
				Element userEle = SCXmlUtil.getChildElement(custContactEle, "User");
				setContactFirstName(userEle.getAttribute("Username"));
			}
			
			if (lastModifiedDateStr != null) {
				setLastModifiedDateString(lastModifiedDateStr);
				setLastModifiedDate(YFCDate.getYFCDate(lastModifiedDateStr));
			}
		} catch (Exception ex) {
			log.error(ex);
		}
		setCustMaintenanceFlags();
		createEmpList();
		setCustAddressToDisp();

		try {
			empNameDoc = prepareAndInvokeMashup("xpedx-cust-GetEmployeeNames");
			if (log.isDebugEnabled()) {
				log.debug("GetEmployeeNames Output"
						+ SCXmlUtil.getString(empNameDoc));
			}
		} catch (Exception ex) {
			log.error(ex);
		}

		createEmpNames();
		setCountryCodesList();

		// empIDs = getEmpIDList();
		//setLastModifiedUser();
		return "success";
	}

	private void setLastModifiedUser() {
		XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean) XPEDXWCUtils
				.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
		if (xpedxCustomerContactInfoBean != null) {
			setContactFirstName(xpedxCustomerContactInfoBean.getFirstName());
			setContactLastName(xpedxCustomerContactInfoBean.getLastName());
		}
	}

	protected void createEmpNames() {
		NodeList custContacts = empNameDoc
				.getElementsByTagName("CustomerContact");
		for (int i = 0; i < custContacts.getLength(); i++) {
			Element custContact = (Element) custContacts.item(i);
			Element custContactExtn = (Element) custContact
					.getElementsByTagName("Extn").item(0);
			String fname = custContact.getAttribute("FirstName");
			String lname = custContact.getAttribute("LastName");
			String name = lname + ", " + fname;
			if (PrimarySalesRepID.equals(custContactExtn
					.getAttribute("ExtnEmployeeID"))) {
				PrimarySalesRep = name;
			} else
				empNames.add(name);

		}
	}

	protected void createEmpList() {
		Element extnElem = XMLUtilities
				.getChildElementByName(outputDoc, "Extn");
		Element salesRepList = XMLUtilities.getChildElementByName(extnElem,
				"XPEDXSalesRepList");

		NodeList salesReps = salesRepList.getElementsByTagName("XPEDXSalesRep");
		String otherSalesRep = null;
		for (int i = 0; i < salesReps.getLength(); i++) {
			Element salesRep = (Element) salesReps.item(i);
			String SalesRepID = salesRep.getAttribute("SalesRepId");
			if ("Y".equalsIgnoreCase(salesRep
					.getAttribute("PrimarySalesRepFlag"))) {
				PrimarySalesRepID = SalesRepID;
			} else {
				otherSalesRep = SalesRepID;
				otherSalesRepList.add(otherSalesRep);
			}
			empList.add(SalesRepID);
		}
		if (log.isDebugEnabled()) {
			log.debug("eList:" + empList.toString());
		}
	}

	protected void setCustAddressToDisp() {
		Element custAddntlAddressList = XMLUtilities.getChildElementByName(
				outputDoc, "CustomerAdditionalAddressList");
		NodeList AddtnlAddressList = custAddntlAddressList
				.getElementsByTagName("CustomerAdditionalAddress");
		for (int i = 0; i < AddtnlAddressList.getLength(); i++) {
			Element AddtnlAddress = (Element) AddtnlAddressList.item(i);
			if ("Y".equalsIgnoreCase(AddtnlAddress.getAttribute("IsShipTo"))) {
				custAddressElem = AddtnlAddress;
			}
			if (custAddressElem == null) {
				if ("Y".equalsIgnoreCase(AddtnlAddress.getAttribute("IsBillTo"))) {
					custAddressElem = AddtnlAddress;
				}
			}
		}
	}

	public Element getCustAddressElem() {
		return custAddressElem;
	}

	public void setCustAddressElem(Element custAddressElem) {
		this.custAddressElem = custAddressElem;
	}

	public List getEmpNames() {
		return empNames;
	}

	public List getEmpList() {
		return empList;

	}

	public List getOtherSalesRepList() {
		return otherSalesRepList;

	}

	protected void setCustMaintenanceFlags() {

		Element extnElem = XMLUtilities
				.getChildElementByName(outputDoc, "Extn");
		SCXmlUtil.getAttribute(extnElem, "ExtnUseOrderMulUOMFlag");

		SCXmlUtil.getAttribute(extnElem, "ExtnEmailViewInvoicesFlag");
		String pInvoices = SCXmlUtil.getAttribute(extnElem,
				"ExtnPreviewInvoicesFlag");
		SCXmlUtil.getAttribute(extnElem, "ExtnAcceptBackOrdersFlag");
		String aShipToOR = SCXmlUtil.getAttribute(extnElem,
				"ExtnShipToOverrideFlag");

		// if ("Y".equals(vwPrices))
		// viewPrices = true;
		// if ("Y".equals(cnOrder))
		// canOrder = true;
		// if ("Y".equals(vwInv))
		// canViewInv = true;
		// if ("Y".equals(uCustSKU))
		// useCustSKU = true;
		// if ("Y".equals(uOMulUOM))
		// useOrderMulUOM = true;
		// if ("Y".equals(eViewInvoices))
		// emailViewInvoices = true;
		if ("Y".equals(pInvoices))
			previewInvoices = true;
		// if ("Y".equals(aBackOrders))
		// acceptBackOrders = true;
		if ("Y".equals(aShipToOR))
			shipToOverrideFlag = true;
		// if ("Y".equals(srFlag))
		// sampleRequestFlag = true;
		if ("Y".equals(SCXmlUtil.getAttribute(extnElem, "ExtnMyItemsLink")))
			myItemsLink = true;

		if ("Y".equals(SCXmlUtil.getAttribute(extnElem, "ExtnOrderUpdateFlag")))
			orderUpdateFlag = true;
		if ("Y".equals(SCXmlUtil.getAttribute(extnElem, "ExtnShipComplete")))
			shipComplete = true;

		if ("Y".equals(SCXmlUtil
				.getAttribute(extnElem, "ExtnCustLineAccNoFlag")))
			CustLineAccNoFlag = true;
		if ("Y".equals(SCXmlUtil
				.getAttribute(extnElem, "ExtnCustLineSeqNoFlag")))
			CustLineSeqNoFlag = true;
		if ("Y".equals(SCXmlUtil.getAttribute(extnElem, "ExtnCustLinePONoFlag")))
			CustLinePONoFlag = true;
		if ("Y".equals(SCXmlUtil.getAttribute(extnElem,
				"ExtnCustLineField1Flag")))
			CustLineField1Flag = true;
		if ("Y".equals(SCXmlUtil.getAttribute(extnElem,
				"ExtnCustLineField2Flag")))
			CustLineField2Flag = true;
		if ("Y".equals(SCXmlUtil.getAttribute(extnElem,
				"ExtnCustLineField3Flag")))
			CustLineField3Flag = true;

	}

	public String getPrimarySalesRep() {
		return PrimarySalesRep;
	}

	public String getPrimarySalesRepID() {
		return PrimarySalesRepID;
	}

	/*
	 * public boolean isViewPrices() { return viewPrices; }
	 * 
	 * public boolean isCanOrder() { return canOrder; }
	 * 
	 * public boolean isCanViewInv() { return canViewInv; }
	 * 
	 * public boolean isUseCustSKU() { return useCustSKU; }
	 */

	/*
	 * public boolean isUseOrderMulUOM() { return useOrderMulUOM; }
	 */

	/*
	 * public boolean isSampleRequest() { return sampleRequestFlag; }
	 */

	/*
	 * public boolean isEmailViewInvoices() { return emailViewInvoices; }
	 * 
	 * public boolean isPreviewInvoices() { return previewInvoices; }
	 * 
	 * public boolean isAcceptBackOrders() { return acceptBackOrders; }
	 */

	public boolean isPreviewInvoices() {
		return previewInvoices;
	}

	public boolean IsShipToOverrideFlag() {
		return shipToOverrideFlag;
	}

	public boolean IsMyItemsLink() {
		return myItemsLink;
	}

	public boolean isShipComplete() {
		return shipComplete;
	}

	public boolean isOrderUpdateFlag() {
		return orderUpdateFlag;
	}

	public boolean IsCustLineAccNoFlag() {
		return CustLineAccNoFlag;
	}

	public boolean IsCustLineSeqNoFlag() {
		return CustLineSeqNoFlag;
	}

	public boolean IsCustLinePONoFlag() {
		return CustLinePONoFlag;
	}

	public boolean IsCustLineField1Flag() {
		return CustLineField1Flag;
	}

	public boolean IsCustLineField2Flag() {
		return CustLineField2Flag;
	}

	public boolean IsCustLineField3Flag() {
		return CustLineField3Flag;
	}

	public Element getOutputDoc() {
		return outputDoc;
	}

	public void setOutputDoc(Element outputDocument) {
		outputDoc = outputDocument;
	}

	/**
	 * @return the countriesMap
	 */
	public Map<String, String> getCountriesMap() {
		return countriesMap;
	}

	/**
	 * @param countriesMap
	 *            the countriesMap to set
	 */
	public void setCountriesMap(Map<String, String> countriesMap) {
		this.countriesMap = countriesMap;
	}

	private void setCountryCodesList() {
		setCountriesMap(XPEDXWCUtils.getCountryCodeList());
	}

	private static final Logger log = Logger
			.getLogger(XPEDXGetCustomerMaintenanceAction.class);
	protected Element outputDoc;
	protected Element empNameDoc;
	protected Element custAddressElem;

	protected String custId;
	protected String orgCode;

	private List<String> empIDs = new ArrayList<String>();

	// protected boolean viewPrices;
	// protected boolean canOrder;
	// protected boolean canViewInv;
	// protected boolean useCustSKU;
	// protected boolean useOrderMulUOM;
	// protected boolean sampleRequestFlag;

	// protected boolean emailViewInvoices;
	protected boolean previewInvoices;
	// protected boolean acceptBackOrders;
	protected boolean shipToOverrideFlag;
	protected boolean orderUpdateFlag;
	protected boolean shipComplete;
	protected boolean myItemsLink;

	protected boolean CustLineAccNoFlag;
	protected boolean CustLineSeqNoFlag;
	protected boolean CustLinePONoFlag;
	protected boolean CustLineField1Flag;
	protected boolean CustLineField2Flag;
	protected boolean CustLineField3Flag;
	// protected String lastModifiedDate = "";
	protected String userCreatedDate = "";
	protected String contactFirstName = "";
	protected String contactLastName = "";

	protected YFCDate lastModifiedDate = new YFCDate();
	protected String lastModifiedDateString = "";

	protected String PrimarySalesRepID = "";
	protected String PrimarySalesRep = "";

	// protected Map empList;
	public List empList;
	public List otherSalesRepList;
	public List empNames;
	private boolean success;
	
	private String custContactId;

	public String getCustContactId() {
		return custContactId;
	}

	public void setCustContactId(String custContactId) {
		this.custContactId = custContactId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean isSuccess) {
		this.success = isSuccess;
	}

	protected Map<String, String> countriesMap;

	public String getUserCreatedDate() {
		return userCreatedDate;
	}

	public void setUserCreatedDate(String userCreatedDate) {
		this.userCreatedDate = userCreatedDate;
	}

	public String getContactFirstName() {
		return contactFirstName;
	}

	public void setContactFirstName(String contactFirstName) {
		this.contactFirstName = contactFirstName;
	}

	public String getContactLastName() {
		return contactLastName;
	}

	public void setContactLastName(String contactLastName) {
		this.contactLastName = contactLastName;
	}

	// added for 2769 jira for getting the lastmodified date and formatting
	public YFCDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(YFCDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedDateString() {
		return lastModifiedDateString;
	}

	public void setLastModifiedDateString(String lastModifiedDateString) {
		this.lastModifiedDateString = lastModifiedDateString;
	}

	public String getLastModifiedDateToDisplay() {
		UtilBean utilBean = new UtilBean();
		String dateToDisplay = "";
		if (lastModifiedDateString != null) {
			dateToDisplay = utilBean.formatDate(lastModifiedDateString,
					wcContext, null, "MM/dd/yyyy");
		}
		return dateToDisplay;
	}
}
