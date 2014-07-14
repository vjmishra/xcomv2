

package com.sterlingcommerce.xpedx.webchannel.common.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.sterlingcommerce.webchannel.core.AbstractWCAction;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;

public class XPEDXGetAddressBookAction extends AbstractWCAction {

	public XPEDXGetAddressBookAction() {
		addressType = null;
		targetForm = "";
		targetDiv = "";
		searchTextTerm = "";
		callBackFunctionForUseAddress = "";
		addressArrayName = "addressBookData";
		addressBook = new ArrayList();
		numbCols = 2;
		numbInitialRows = 2;
		maxAddresses = 2147483647;
		skipChangeEventsOnAddressSelection = false;
		addressBooktabStartIndex = 0;
	}

	public String execute() {
		try {
			if(searchTextTerm != ""){
				searchAddressBook();
			}else{
				buildAddressBook();
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return "success";
	}
	
	protected void searchAddressBook() throws Exception{
		IWCContext context = getWCContext();
		ArrayList alAddrBook = (ArrayList)context.getWCAttribute("ShipToAddrList");
		if(alAddrBook!=null){
			for(int n=0; n<alAddrBook.size();n++){
				Element elAddress = (Element)alAddrBook.get(n);
				Element elPersonInfo = XMLUtilities.getChildElementByName(elAddress, "PersonInfo");
				Boolean addAddrFlg = false;
				NamedNodeMap AttrMap = elPersonInfo.getAttributes();
				for(int i=0; i<AttrMap.getLength(); i++){
					Node wNode = AttrMap.item(i);
					
					if(wNode.getTextContent().contains(searchTextTerm)){
						addAddrFlg = true;
					}
				}
				if(addAddrFlg){
					addressBook.add(elAddress);
				}
			}
			
			
			/*String addressEl = el.toString();
			addressEl.con
			addressBook.add(el);
			el = (Element)alAddrBook.get(1);
			addressBook.add(el);*/
			
			
		}
	}

	protected void buildAddressBook() throws Exception {
		String addressTypeFilter = null;
		if ("SoldTo".equals(addressType) || "Unfiltered".equals(addressType))
			addressTypeFilter = "IsSoldTo";
		else if ("ShipTo".equals(addressType))
			addressTypeFilter = "IsShipTo";
		else if ("BillTo".equals(addressType))
			addressTypeFilter = "IsBillTo";
		else
			throw new Exception((new StringBuilder()).append(
					"Received unrecognized AddressType of ")
					.append(addressType).toString());
		Map valueMap = buildValueMap();
		Element apiOutput = prepareAndInvokeMashup(getMashupId(), valueMap);
		String xpathToCustomerContactAddressList = (new StringBuilder())
				.append(
						"//Customer/CustomerContactList/CustomerContact[@CustomerContactID='")
				.append(getWCContext().getCustomerContactId()).append(
						"']/CustomerAdditionalAddressList").toString();
		addAddressesOfType(apiOutput, xpathToCustomerContactAddressList,
				addressTypeFilter);
		addAddressesOfType(apiOutput,
				"//Customer/CustomerAdditionalAddressList", addressTypeFilter);
	}

	protected void addAddressesOfType(Element baseElement, String listPath,
			String addressTypeFilter) throws Exception {
		Element addressListElement = XMLUtilities.getElement(baseElement,
				listPath);
		if (addressListElement == null) {
			LOG.error((new StringBuilder()).append("Found null element for ")
					.append(listPath).toString());
			return;
		}
		List addressList = XMLUtilities.getChildElements(addressListElement,
				"CustomerAdditionalAddress");
		if (addressList != null && !addressList.isEmpty()) {
			for (int i = 0; i < addressList.size(); i++) {
				if (addressBook.size() >= maxAddresses)
					return;
				Element address = (Element) addressList.get(i);
				String value = address.getAttribute(addressTypeFilter);
				if ("Y".equals(value) || "Unfiltered".equals(addressType))
					addressBook.add(address);
			}

		}
		IWCContext context = getWCContext();
		context.setWCAttribute("ShipToAddrList", addressBook, WCAttributeScope.SESSION);
		
	}

	protected Map buildValueMap() throws Exception {
		Map valueMap = new HashMap();
		IWCContext context = getWCContext();
		String customerContactID = context.getCustomerContactId();
		if (customerContactID == null)
			throw new Exception("customerContactID was null");
		valueMap.put("/Customer/CustomerContact/@CustomerContactID",
				customerContactID);
		String storefrontID = context.getStorefrontId();
		if (storefrontID == null)
			throw new Exception("storefrontID was null");
		valueMap.put("/Customer/@OrganizationCode", storefrontID);
		String customerID = context.getCustomerId();
		if (customerID == null) {
			throw new Exception("customerID was null");
		} else {
			valueMap.put("/Customer/@CustomerID", customerID);
			return valueMap;
		}
	}

	public ArrayList getAddressBook() {
		return addressBook;
	}

	public void setAddressType(String parameter) {
		addressType = parameter;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setTargetForm(String parameter) {
		targetForm = parameter;
	}

	public String getTargetForm() {
		return targetForm;
	}

	public void setTargetDiv(String parameter) {
		targetDiv = parameter;
	}

	public String getTargetDiv() {
		return targetDiv;
	}
	
	
	public void setSearchTextTerm(String parameter) {
		searchTextTerm = parameter;
	}


	public void setCallBackFunctionForUseAddress(String parameter) {
		callBackFunctionForUseAddress = parameter;
	}

	public String getCallBackFunctionForUseAddress() {
		return callBackFunctionForUseAddress;
	}

	public String getAddressArrayName() {
		return addressArrayName;
	}

	public void setAddressArrayName(String parameter) {
		addressArrayName = parameter;
	}

	public void setNumberOfColumns(int parameter) {
		if (parameter >= 1)
			numbCols = parameter;
		else
			LOG.error((new StringBuilder()).append(
					"setNumberOfColumns received an invalid value of ").append(
					parameter).append("; using ").append(numbCols).append(
					" instead").toString());
	}

	public int getNumberOfColumns() {
		return numbCols;
	}

	public void setNumberOfInitialRows(int parameter) {
		if (parameter >= 1)
			numbInitialRows = parameter;
		else
			LOG.error((new StringBuilder()).append(
					"setNumberOfInitialRows received an invalid value of ")
					.append(parameter).append("; using ").append(
							numbInitialRows).append(" instead").toString());
	}

	public int getNumberOfInitialRows() {
		return numbInitialRows;
	}

	public void setMaximumNumberOfAddresses(int parameter) {
		if (parameter >= 0)
			maxAddresses = parameter;
		else
			LOG
					.error((new StringBuilder())
							.append(
									"setMaximumNumberOfAddresses received an invalid value of ")
							.append(parameter).append("; using ").append(
									maxAddresses).append(" instead").toString());
	}

	public int getMaximumNumberOfAddresses() {
		return maxAddresses;
	}

	public void setSkipChangeEventsOnAddressSelection(boolean parameter) {
		skipChangeEventsOnAddressSelection = parameter;
	}

	public boolean getSkipChangeEventsOnAddressSelection() {
		return skipChangeEventsOnAddressSelection;
	}

	public int getAddressBooktabStartIndex() {
		return addressBooktabStartIndex;
	}

	public void setAddressBooktabStartIndex(int addressBooktabStartIndex) {
		this.addressBooktabStartIndex = addressBooktabStartIndex;
	}

	public static final String SOLD_TO = "SoldTo";
	public static final String SHIP_TO = "ShipTo";
	public static final String BILL_TO = "BillTo";
	public static final String UNFILTERED = "Unfiltered";
	public static final String NOT_REQUESTED = "";
	public static final String DEFAULT_ADDRESS_ARRAY_NAME = "addressBookData";
	protected static final String XPATH_CUSTOMER_CONTACT_ID = "/Customer/CustomerContact/@CustomerContactID";
	protected static final String XPATH_ORGANIZATION_CODE = "/Customer/@OrganizationCode";
	protected static final String XPATH_CUSTOMER_ID = "/Customer/@CustomerID";
	protected static final String XPATH_CUSTOMER_ADDRESS_LIST = "//Customer/CustomerAdditionalAddressList";
	protected static final String CUSTOMER_ADDITIONAL_ADDRESS = "CustomerAdditionalAddress";
	protected static final String IS_SOLD_TO = "IsSoldTo";
	protected static final String IS_SHIP_TO = "IsShipTo";
	protected static final String IS_BILL_TO = "IsBillTo";
	private static final Logger LOG = Logger.getLogger(XPEDXGetAddressBookAction.class);
	protected String addressType;
	protected String targetForm;
	protected String targetDiv;
	protected String callBackFunctionForUseAddress;
	protected String addressArrayName;
	protected String searchTextTerm;
	protected ArrayList addressBook;
	protected int numbCols;
	protected int numbInitialRows;
	protected int maxAddresses;
	protected boolean skipChangeEventsOnAddressSelection;
	protected int addressBooktabStartIndex;

}

