package com.sterlingcommerce.xpedx.webchannel.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.profile.user.XPEDXAssignedCustomersLocationsDisplayAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

public class XPEDXGetAssignedLocationsForReporting 
	extends WCMashupAction{
	
	private static final long serialVersionUID = 7861772398942028854L;

	private HashMap<String, String> accountsMap;
	private HashMap<String, String> billToMap;
	
	private ArrayList<String> childCustomerList = new ArrayList<String>();
	private ArrayList<String> billToList = new ArrayList<String>();
	private static String BILL_TO = "bill-to";
	
	public HashMap<String, String> getBillToMap() {
		return billToMap;
	}



	public void setBillToMap(HashMap<String, String> billToMap) {
		this.billToMap = billToMap;
	}



	public HashMap<String, String> getAccountsMap() {
		return accountsMap;
	}



	public void setAccountsMap(HashMap<String, String> accountsMap) {
		this.accountsMap = accountsMap;
	}


	private final static Logger log = Logger
	.getLogger(XPEDXGetAssignedLocationsForReporting.class);	
	
	

	
	 public String getAssignedLocations(){
		String processBillTo  = request.getParameter("processBillTo");
		try{
			
			String userId = wcContext.getLoggedInUserId();
			
			
			LOG.debug("Process bill to +++++++++++ " + processBillTo);
			Document outputDoc = null;
			if (null == userId) {
				log
						.debug("getAllAssignedLocations: customerID is a required field. Returning a empty Map");
				return ERROR;
			}
			
			outputDoc = getAllLocationsDoc(userId);

			if (null == outputDoc) {
				log
						.debug("getAllAssignedLocations: No data available for customerID. Returning a empty Map");
				return ERROR;
			}

			NodeList customerListElem = outputDoc.getElementsByTagName("Customer");

			
			if(customerListElem!=null && customerListElem.getLength()>0)
			{
				for(int i=0;i<customerListElem.getLength();i++)
				{
					Element customerElem = (Element) customerListElem.item(i);
					//Modified For Jira 3216
					Element extElement = (Element) customerElem.getFirstChild();
					String extSuffixType = SCXmlUtil.getAttribute(extElement, "ExtnSuffixType");
					
					if(customerElem!=null) {
						String strCustId = SCXmlUtil.getAttribute(customerElem, "CustomerID");
						if(extSuffixType.equalsIgnoreCase("C")){
						//if(strCustId.startsWith("CD") && strCustId.endsWith("CC")) {
							LOG.debug("---- " + strCustId);
							childCustomerList.add(strCustId);
							//Code Added for Bill To
							if(processBillTo.equalsIgnoreCase("true") == true)
								getAllBillToForSap(strCustId);
							//Code For Bill To
						}
						
						//Check if explicitly assigned bill to location
						else if(processBillTo.equalsIgnoreCase("true") == true){
							 //extElement = (Element) customerElem.getFirstChild();
							//extSuffixType = SCXmlUtil.getAttribute(extElement, "ExtnSuffixType");
							if(extSuffixType.equalsIgnoreCase("B") == true && ! billToList.contains(strCustId)){
								LOG.debug("Adding to bill to list ---- "+ strCustId);
								billToList.add(strCustId);
							}
						}
						
					}
				}
				
				if(childCustomerList!=null && childCustomerList.size()>0)
				{
					setAccountsMap((HashMap<String, String>) XPEDXWCUtils.custFullAddresses(childCustomerList, wcContext.getStorefrontId()));
				}			
				
				
				if(processBillTo.equalsIgnoreCase("true") == true && billToList!=null && billToList.size()>0)
				{
					setBillToMap((HashMap<String, String>) XPEDXWCUtils.custFullAddresses(billToList, wcContext.getStorefrontId()));
				}							
			}			
			
				

		} catch (XMLExceptionWrapper e) {
			e.printStackTrace();
			return ERROR;
		} catch (CannotBuildInputException e) {
			
			e.printStackTrace();
			return ERROR;
		}
		if(processBillTo.equalsIgnoreCase("true") == true)
			return BILL_TO;
		return SUCCESS;
	}
	 
	private Document getAllLocationsDoc(String userId)
			throws CannotBuildInputException {
		Document allAssignedCustomerDoc = null;

		if (null == userId) {
			log
					.debug("getAllLocationsDoc: customerID is a required field. Returning a empty Document");
			LOG.debug("getAllLocationsDoc: customerID is a required field. Returning a empty Document");
			return allAssignedCustomerDoc;
		}

		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/CustomerAssignment/@UserId", userId);
		
		Element input = WCMashupHelper.getMashupInput(
				"xpedx-getCustomerAssignments", valueMap, wSCUIContext);
		
		String inputXml = SCXmlUtil.getString(input);
		
		log.debug("getAllLocationsDoc: Input XML: " + inputXml);
		LOG.debug("getAllLocationsDoc: Input XML: " + inputXml);
		
		Object obj = WCMashupHelper.invokeMashup("xpedx-getCustomerAssignments",
				input, wSCUIContext);
		allAssignedCustomerDoc = ((Element) obj).getOwnerDocument();
		
		if (null != allAssignedCustomerDoc) {
			log.debug("getAllLocationsDoc: Output XML: "
					+ SCXmlUtil.getString((Element) obj));
			
			LOG.debug("Output document ----  " + SCXmlUtil.getString((Element) obj));
		}
		return allAssignedCustomerDoc;
	}
	
	private void getAllBillToForSap(String sapCustId){
		
		Document allBillTos = null;
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Customer/@CustomerID", sapCustId);
		valueMap.put("/Customer/@OrganizationCode", context.getStorefrontId());

		Element input;
		try {
			input = WCMashupHelper.getMashupInput(
					"xpedxCustomerAssignment", valueMap, wSCUIContext);
			
			String inputXml = SCXmlUtil.getString(input);
			LOG.debug("getAllChildCustomersDoc: Input XML: " + inputXml);
			Object obj = WCMashupHelper.invokeMashup("xpedxCustomerAssignment",
					input, wSCUIContext);
			allBillTos = ((Element) obj).getOwnerDocument();
			if (null != allBillTos) {
				LOG.debug("getAllChildCustomersDoc: Output XML: "
						+ SCXmlUtil.getString((Element) obj));
				NodeList orgListElements = allBillTos.getElementsByTagName("Organization");
				
				for(int counter=0; counter<orgListElements.getLength(); counter++){
					Element elemOrg = (Element) orgListElements.item(counter);
					String suffix= SCXmlUtil.getAttribute(elemOrg,"CustomerSuffixType");
					
					
					if(suffix.equalsIgnoreCase("B") == true){
						String billToCustId = SCXmlUtil.getAttribute(elemOrg,"CustomerID");
						if(! billToList.contains(billToCustId))
							LOG.debug("Adding to bill to list ---- "+ billToCustId);
							billToList.add(billToCustId);
						
					}
				}
			}
		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
