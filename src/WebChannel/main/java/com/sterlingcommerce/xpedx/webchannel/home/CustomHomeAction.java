package com.sterlingcommerce.xpedx.webchannel.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.common.CookieUtil;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.util.YFCUtils;

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
		
		if (aribaFlag!=null && aribaFlag.equals("Y") ) 
		{
			ArrayList<Map> divisionsWithShipToInfo = getAllAssignedShiptosWithDivisionsForAUser(wcContext.getLoggedInUserId(), wcContext);
			if (divisionsWithShipToInfo != null
					&& divisionsWithShipToInfo.size() == 2) {
				divisonDetails = divisionsWithShipToInfo.get(0);
				divisionWithShipTo = divisionsWithShipToInfo.get(1);
				if(divisonDetails!=null && divisionWithShipTo!=null ){
				Set keySet = divisionWithShipTo.keySet();
				ArrayList<Element> customers = divisionWithShipTo.get(keySet.iterator().next());
				Element customer = customers.get(0);
				String selectedCustomer = customer.getAttribute("MSAPCustomerID");	
				XPEDXWCUtils.setCurrentCustomerIntoContext(selectedCustomer,wcContext);
				wcContext.setWCAttribute("isPunchoutUser", "true",WCAttributeScope.LOCAL_SESSION);
					wcContext.setWCAttribute("isTACheckReqForPunchoutUser", "Y",WCAttributeScope.LOCAL_SESSION);
					if(divisonDetails.size()==1 && customers!=null && customers.size()==1 ){
						wcContext.getSCUIContext().getSession(false).removeAttribute("aribaFlag");
						prefferedShipToCustomer=customer.getAttribute("ShipToCustomerID");						
						return "changePreferredShip";
					}
				}
			} 
			
			
			
			wcContext.getSCUIContext().getSession(false).removeAttribute("aribaFlag");
			return "punchout";
			
		} else {
			return "main";
		}
	}
	
	public static ArrayList<Map> getAllAssignedShiptosWithDivisionsForAUser(
			String UserId, IWCContext context) {

		Map<String, ArrayList<Element>> divisionWithShipTo = new HashMap<String, ArrayList<Element>>();
		Map<String, String> divisonDetails = new HashMap<String, String>();
		ArrayList<Map> divisionsWithShipToInfo = new ArrayList<Map>();
		if (YFCUtils.isVoid(UserId)) {
			return divisionsWithShipToInfo;
		}

		if (context == null) {
			return divisionsWithShipToInfo;
		}

		Document inputDoc = SCXmlUtil
				.createDocument("XPXCustomerAssignmentView");
		inputDoc.getDocumentElement().setAttribute("UserId", UserId);
		try {
			Object obj = WCMashupHelper.invokeMashup("XPEDXGetAllShipToList-Punchout",inputDoc.getDocumentElement(), context.getSCUIContext());
			Element custAssignedEle = (Element) obj;
			ArrayList<Element> assignedCustElems = SCXmlUtil.getElements(custAssignedEle, "XPXCustomerAssignmentView");
			ArrayList<Element> assignedShipTosForDivision;

			if (assignedCustElems.size() > 0) {
				for (int i = 0; i < assignedCustElems.size(); i++) {
					Element customer = assignedCustElems.get(i);
					String division_id = SCXmlUtil.getAttribute(customer,
							"ExtnCustomerDivisionID");
					String division_Name = SCXmlUtil.getAttribute(customer,
							"DivisionName");
					String shipToCustomerID = SCXmlUtil.getAttribute(customer,
							"ShipToCustomerID");
					String shipToCustomer = shipToCustomerID.substring(0,
							shipToCustomerID.lastIndexOf("-M-XX-S"));
					String shipToCustomerName = SCXmlUtil.getAttribute(
							customer, "ShipToCustomerName");
					String addressLine1 = SCXmlUtil.getAttribute(customer,
							"AddressLine1");
					String city = SCXmlUtil.getAttribute(customer, "City");
					String state = SCXmlUtil.getAttribute(customer, "State");
					String zipcode = SCXmlUtil
							.getAttribute(customer, "ZipCode");
					String country = SCXmlUtil
							.getAttribute(customer, "Country");
					System.out.println(SCXmlUtil.getString(customer));
					System.out.println("division_id:" + division_id
							+ " DivisionName:" + division_Name);
					String ShipToDisplayString = shipToCustomerName + "("
							+ shipToCustomer + ")" + addressLine1 + "," + city
							+ "," + state + "," + zipcode + "," + country;
					customer.setAttribute("ShipToDisplayString",
							ShipToDisplayString);
					assignedShipTosForDivision = divisionWithShipTo
							.get(division_id);
					if (assignedShipTosForDivision == null) {
						assignedShipTosForDivision = new ArrayList<Element>();
					}
					assignedShipTosForDivision.add(customer);
					divisonDetails.put(division_id, division_Name);
					divisionWithShipTo.put(division_id,
							assignedShipTosForDivision);
				}
				divisionsWithShipToInfo.add(divisonDetails);
				divisionsWithShipToInfo.add(divisionWithShipTo);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return divisionsWithShipToInfo;
		}

		return divisionsWithShipToInfo;
	}

	private ArrayList<Map> divisionsWithShipToInfo = new ArrayList<Map>();

	public ArrayList<Map> getDivisionsWithShipToInfo() {
		return divisionsWithShipToInfo;
	}

	public void setDivisionsWithShipToInfo(
			ArrayList<Map> divisionsWithShipToInfo) {
		this.divisionsWithShipToInfo = divisionsWithShipToInfo;
	}

	private Map<String, String> divisonDetails = new HashMap<String, String>();

	public Map<String, String> getDivisonDetails() {
		return divisonDetails;
	}

	public void setDivisonDetails(Map<String, String> divisonDetails) {
		this.divisonDetails = divisonDetails;
	}

	private Map<String, ArrayList<Element>> divisionWithShipTo = new HashMap<String, ArrayList<Element>>();

	public Map<String, ArrayList<Element>> getDivisionWithShipTo() {
		return divisionWithShipTo;
	}

	public void setDivisionWithShipTo(
			Map<String, ArrayList<Element>> divisionWithShipTo) {
		this.divisionWithShipTo = divisionWithShipTo;
	}
	private String prefferedShipToCustomer;

	public String getPrefferedShipToCustomer() {
		return prefferedShipToCustomer;
	}

	public void setPrefferedShipToCustomer(String prefferedShipToCustomer) {
		this.prefferedShipToCustomer = prefferedShipToCustomer;
	}
}
