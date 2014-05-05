package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.ArrayList;
import java.util.Collections;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.util.YFCUtils;


@SuppressWarnings("serial")
public class AjaxGetAssignedShipToCustomersAction extends WCMashupAction {


	private String customerContactId;
	private ArrayList<ShipTo> shipToList;
	private XPEDXShipToCustomer defaultShipToCustomer;
	private XPEDXShipToCustomer shoppingForShipToCustomer;
	private boolean includeShoppingForAndDefaultShipTo = false;
	
	

	
	public boolean isIncludeShoppingForAndDefaultShipTo() {
		return includeShoppingForAndDefaultShipTo;
	}
	public void setIncludeShoppingForAndDefaultShipTo(
			boolean includeShoppingForAndDefaultShipTo) {
		this.includeShoppingForAndDefaultShipTo = includeShoppingForAndDefaultShipTo;
	}
	public XPEDXShipToCustomer getShoppingForShipToCustomer() {
		return shoppingForShipToCustomer;
	}
	public XPEDXShipToCustomer getDefaultShipToCustomer() {
		return defaultShipToCustomer;
	}
	public ArrayList<ShipTo> getShipToList() {
		return shipToList;
	}
	public String getCustomerContactId() {
		return customerContactId;
	}
	public void setCustomerContactId(String customerContactId) {
		this.customerContactId = customerContactId;
	}
	
	@Override
	public String execute() {
		if (!YFCUtils.isVoid(customerContactId)) {
			getAllShipToList();
			if(isIncludeShoppingForAndDefaultShipTo()){
				getShoppingForAndDefaultShipTo();
			}
		}
		else{
			throw new IllegalArgumentException("Missing parameter: customerContactId");
		}
		return SUCCESS;
	}
	
	/**
	 * getting all Assigned Ship-to customers
	 */
	private void getAllShipToList(){
		Document inputDoc = SCXmlUtil.createDocument("XPXCustomerAssignmentView");
		inputDoc.getDocumentElement().setAttribute("UserId", customerContactId);
		Element custAssignedEle = (Element)WCMashupHelper.invokeMashup("XPEDXGetAllShipToList-Punchout",inputDoc.getDocumentElement(), wcContext.getSCUIContext());
		ArrayList<Element> assignedCustElems = SCXmlUtil.getElements(custAssignedEle, "XPXCustomerAssignmentView");

		if (assignedCustElems!=null && assignedCustElems.size() > 0) {
			shipToList =  new ArrayList<ShipTo>();
			for (Element assignedCustElem : assignedCustElems) {
				ShipTo shipTo = setShipToValues(assignedCustElem);
				shipToList.add(shipTo);
			}
			Collections.sort(shipToList);
		}
	}
	
	/**
	 * 
	 * @param customer
	 * @return
	 * setting values to ShipTo Object from Element
	 */
	private ShipTo setShipToValues(Element customer){
		ShipTo shipTo = new ShipTo();
		shipTo.setUserId(SCXmlUtil.getAttribute(customer,"UserId"));
		shipTo.setShipToCustomerID(SCXmlUtil.getAttribute(customer,"ShipToCustomerID"));
		shipTo.setEnterpriseCode(SCXmlUtil.getAttribute(customer,"EnterpriseCode"));
		shipTo.setFirstName(SCXmlUtil.getAttribute(customer,"FirstName"));
		shipTo.setLastName(SCXmlUtil.getAttribute(customer,"LastName"));
		shipTo.setBillToCustomerID(SCXmlUtil.getAttribute(customer,"BillToCustomerID"));
		shipTo.setSAPCustomerID(SCXmlUtil.getAttribute(customer,"SAPCustomerID"));
		shipTo.setMSAPCustomerID(SCXmlUtil.getAttribute(customer,"MSAPCustomerID"));
		shipTo.setAddressLine1(SCXmlUtil.getAttribute(customer,"AddressLine1"));
		shipTo.setAddressLine2(SCXmlUtil.getAttribute(customer,"AddressLine2"));
		shipTo.setAddressLine3(SCXmlUtil.getAttribute(customer,"AddressLine3"));
		shipTo.setCity(SCXmlUtil.getAttribute(customer,"City"));
		shipTo.setState(SCXmlUtil.getAttribute(customer,"State"));
		shipTo.setCountry(SCXmlUtil.getAttribute(customer,"Country"));
		shipTo.setZipCode(SCXmlUtil.getAttribute(customer,"ZipCode"));
		shipTo.setShipToCustomerName(SCXmlUtil.getAttribute(customer,"ShipToCustomerName"));
		shipTo.setStatus(SCXmlUtil.getAttribute(customer,"Status"));
		String shipToCustomerID = shipTo.getShipToCustomerID();
		String shipToCustomer = shipToCustomerID.substring(0,shipToCustomerID.lastIndexOf("-M-XX-S"));
		String formattedZip = getFormattedZip(shipTo.getZipCode());
		
		String ShipToDisplayString = " ("+ shipToCustomer + ") " + shipTo.getShipToCustomerName()+" " + shipTo.getAddressLine1() + ", " + shipTo.getCity()+ ", " +shipTo.getState()+", "+ formattedZip + ", " + shipTo.getCountry();
      
		shipTo.setShipToDisplayString(ShipToDisplayString);
		return shipTo;
	}
	
	/**
	 * This method will get Shopping for and Default Ship To customers. 
	 * If the customer contact Id matches to the logged in user then we are getting this info from Cache instead of calling an API
	 */
	private void getShoppingForAndDefaultShipTo(){
		if(customerContactId.equals(getWCContext().getLoggedInUserId().trim())){
			shoppingForShipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		}else{
			//TODO need to implement for customer contact was different.
		}
		if(shoppingForShipToCustomer!=null){
			shoppingForShipToCustomer.setZipCode(getFormattedZip(shoppingForShipToCustomer.getZipCode()));
			defaultShipToCustomer = shoppingForShipToCustomer.getDefaultShipToCustomer();
			if(shoppingForShipToCustomer!=null){
				defaultShipToCustomer.setZipCode(getFormattedZip(defaultShipToCustomer.getZipCode()));
			}
		}
	}

/**
 * 	format the zipCode 
 * @param zipCode
 * @return
 */
   private String getFormattedZip(String zipCode){
	   if(zipCode!=null){
		String firstZip=zipCode;
		String lastZip="";
	
		if(zipCode!=null && zipCode.length()>5){
		    firstZip=zipCode.substring(0, 5);
		    lastZip="-"+zipCode.substring(5);
		  }
		return firstZip+lastZip;
	   }
	 return ""; 	
   }
}