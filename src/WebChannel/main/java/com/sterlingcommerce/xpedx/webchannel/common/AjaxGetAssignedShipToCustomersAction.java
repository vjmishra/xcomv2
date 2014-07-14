package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.util.YFCCommon;

/**
 * This ajax class implemented for Getting All the assigned Ship-To customers for Change Ship-To/Select Ship-To Pages/Modals
 * 
 * @param customerContactId Get all assigned ship-tos for this customer contact id(User id).
 * @param includeShoppingForAndDefaultShipTo This is a flag for to get Default/Shopping for ship-to values or not.
 * @param status. status of shipto customer. We need to get in some scenarios only active, in some other scenarios  all , including suspended ones.
 * 
 * @param shipToList  This is return ship-to list contains All the  assigned Ship-To customers for the corresponding input customer contact id(user id).
 * @param defaultShipToCustomer This is Default ship to customer information for the corresponding input customer contact id(user id).
 * @param shoppingForShipToCustomer This is Shopping-for ship to customer information for the corresponding input customer contact id(user id).
 * 
 */
@SuppressWarnings("serial")
public class AjaxGetAssignedShipToCustomersAction extends WCAction {

	// input fields
	private String customerContactId;
	private boolean includeShoppingForAndDefaultShipTo;
	private String status;
	
	// output fields
	private List<ShipTo> shipToList;
	private XPEDXShipToCustomer defaultShipToCustomer;
	private XPEDXShipToCustomer shoppingForShipToCustomer;
	
	
	

	public void setStatus(String status) {
		this.status = status;
	}
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
	public List<ShipTo> getShipToList() {
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
			shipToList = getAllShipToList();
			if(isIncludeShoppingForAndDefaultShipTo() && shipToList!=null && shipToList.size() >0){
				initShoppingForAndDefaultShipTo();
			}
		}
		else{
			throw new IllegalArgumentException("Missing parameter: customerContactId");
		}
		return SUCCESS;
	}
	
	/**
	 * getting all Assigned Ship-to customers for the corresponding customer contact id(user id)
	 */
	private List<ShipTo> getAllShipToList(){
		List<ShipTo> returnshipToList = null;
		Document inputDoc = SCXmlUtil.createDocument("XPXCustomerAssignmentView");
		inputDoc.getDocumentElement().setAttribute("UserId", customerContactId);
		if(status!=null && status.length()>0 ) {
			inputDoc.getDocumentElement().setAttribute("Status", status);
		}
		Element custAssignedEle = (Element)WCMashupHelper.invokeMashup("XPEDXGetAllShipToList-ShipTo",inputDoc.getDocumentElement(), wcContext.getSCUIContext());
		List<Element> assignedCustElems = SCXmlUtil.getElements(custAssignedEle, "XPXCustomerAssignmentView");

		if (assignedCustElems!=null && assignedCustElems.size() > 0) {
			returnshipToList =  new ArrayList<ShipTo>(assignedCustElems.size());
			for (Element assignedCustElem : assignedCustElems) {
				ShipTo shipTo = createShipToFromElement(assignedCustElem);
				returnshipToList.add(shipTo);
			}
			Collections.sort(returnshipToList,ShipTo.COMPARATOR_DISPLAY);
		}
		return returnshipToList;
	}
	
	/**
	 * 
	 * @param customer
	 * @return
	 * creating the shipTo object from customer Element
	 */
	private ShipTo createShipToFromElement(Element customer){
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
		shipTo.setShipToMultiRowDisplay(shipToCustomer +"\n"+ shipTo.getShipToCustomerName() +"\n"+  shipTo.getAddressLine1()  +"\n"+  shipTo.getCity()+ ", " +shipTo.getState()+", "+ formattedZip + ", " + shipTo.getCountry() +"\n");
		String ShipToDisplayString = " ("+ shipToCustomer + ") " + shipTo.getShipToCustomerName()+" " + shipTo.getAddressLine1() + ", " + shipTo.getCity()+ ", " +shipTo.getState()+", "+ formattedZip + ", " + shipTo.getCountry();
		if(!YFCCommon.isVoid(shipTo.getAddressLine2()))
			ShipToDisplayString = " ("+ shipToCustomer + ") " + shipTo.getShipToCustomerName()+" " + shipTo.getAddressLine1() + ", " + shipTo.getAddressLine2()+ ", " + shipTo.getCity()+ ", " +shipTo.getState()+", "+ formattedZip + ", " + shipTo.getCountry();
		
		shipTo.setShipToDisplayString(ShipToDisplayString);
		return shipTo;
	}
	
	/**
	 * This method will initialize Shopping for and Default Ship To customers. 
	 * If the customer contact Id matches to the logged in user then we are getting this info from Cache instead of calling an API
	 */
	private void initShoppingForAndDefaultShipTo(){
		if(customerContactId.equals(getWCContext().getLoggedInUserId().trim())){
			shoppingForShipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		}else{
			//TODO need to implement for customer contact was different.
		}
		if(shoppingForShipToCustomer!=null){
			shoppingForShipToCustomer.setZipCode(getFormattedZip(shoppingForShipToCustomer.getZipCode()));
			defaultShipToCustomer = shoppingForShipToCustomer.getDefaultShipToCustomer();
			if(defaultShipToCustomer!=null){
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
		 if (zipCode.indexOf("-") > -1) {
			 zipCode = zipCode.replaceAll("-", "");
		 }
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
