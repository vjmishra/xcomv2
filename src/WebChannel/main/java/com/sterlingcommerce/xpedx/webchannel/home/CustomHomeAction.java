package com.sterlingcommerce.xpedx.webchannel.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.common.CookieUtil;
import com.sterlingcommerce.xpedx.webchannel.punchout.DivisionBean;
import com.sterlingcommerce.xpedx.webchannel.punchout.ShipToCustomerBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class CustomHomeAction extends WCAction {
	
	public String execute() throws Exception
	{
		super.execute();
		
		
		String aribaFlag = (String) wcContext.getSCUIContext().getSession(false).getAttribute("aribaFlag");
		
		String sfId = (String) wcContext.getSCUIContext().getSession(false).getAttribute("EnterpriseCode");
		
		if (sfId != null) {
			// create or update cookie with this storefront id
			Cookie cookie = CookieUtil.getCookie(wcContext.getSCUIContext().getRequest(), CookieUtil.STOREFRONT_ID);
			if (cookie == null) {
				cookie = new Cookie(CookieUtil.STOREFRONT_ID, sfId);
				cookie.setMaxAge(-1); // until user closes browser
			} else {
				cookie.setValue(sfId);
			}
			wcContext.getSCUIContext().getResponse().addCookie(cookie);
		}
		
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
					wcContext.setWCAttribute("isTACheckReqForPunchoutUser", "Y",WCAttributeScope.LOCAL_SESSION);
						if(divisionBeanList.size()==1 && shipToCustomers.size()==1 ){
							wcContext.getSCUIContext().getSession(false).removeAttribute("aribaFlag");
							prefferedShipToCustomer=shipToCustomerBean.getShipToCustomerID();						
							return "changePreferredShip";
						}
				}
			} 
			
			
			
			wcContext.getSCUIContext().getSession(false).removeAttribute("aribaFlag");
			
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
		
		String ShipToDisplayString = shipToCustomerBean.getShipToCustomerName() + "("+ shipToCustomer + ")" + shipToCustomerBean.getAddressLine1() + "," + shipToCustomerBean.getCity()+ "," + shipToCustomerBean.getState() + "," + shipToCustomerBean.getZipCode() + "," + shipToCustomerBean.getCountry();
		
		shipToCustomerBean.setShipToDisplayString(ShipToDisplayString);
		return shipToCustomerBean;
	}
}

