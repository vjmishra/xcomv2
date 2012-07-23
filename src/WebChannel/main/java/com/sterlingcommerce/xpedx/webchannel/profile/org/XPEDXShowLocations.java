package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.util.ArrayUtil;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfs.japi.YFSEnvironment;

@SuppressWarnings("serial")
public class XPEDXShowLocations extends WCAction {
	private String customerID;
	protected String shownCustomerId;
	private Document outDoc;
	private Document profileDocument;
	private Document countriesListDoc;
	protected String organizationCode;
	protected Document shownCustomerDoc;
	protected Document allChildCustomersDoc;
	protected ArrayList<Element> childCustomers = new ArrayList<Element>();
	protected Map<String,String> displayChildCustomersMap = new HashMap<String, String>();
	ArrayList<Element> billToCustomers = new ArrayList<Element>();
	protected Map<String,ArrayList<String>> msapAndSapCustomersMap = new HashMap<String, ArrayList<String>>();
	protected Map<String,ArrayList<String>> sapAndBillToCustomersMap = new HashMap<String, ArrayList<String>>();
	protected Map<String,ArrayList<String>> billToAndShipToCustomersMap = new HashMap<String, ArrayList<String>>();
	private final static Logger log = Logger.getLogger(XPEDXShowLocations.class);
	private String shownCustomerSuffixType;
	protected String mSapName;
	protected String buyrOrgName;
	protected Integer pageNumber = 1;
	private String pageSetToken;
	private int shipTosSize = 0;
	private Boolean isFirstPage = Boolean.FALSE;
	private String totalNumOfRecords;
	private Map<String,String> suffixTypeMap=new HashMap<String,String>();
	private String suffixType;
	private String sapCustomerDisplay;
    public String getTotalNumOfRecords() {
		return totalNumOfRecords;
	}

	public void setTotalNumOfRecords(String totalNumOfRecords) {
		this.totalNumOfRecords = totalNumOfRecords;
	}

	public Boolean getIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(Boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public Boolean getIsLastPage() {
		return isLastPage;
	}

	public void setIsLastPage(Boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public Boolean getIsValidPage() {
		return isValidPage;
	}

	public void setIsValidPage(Boolean isValidPage) {
		this.isValidPage = isValidPage;
	}

	private Boolean isLastPage = Boolean.FALSE;
    private Boolean isValidPage = Boolean.FALSE;
    private Integer totalNumberOfPages = new Integer(1);
	public Integer getTotalNumberOfPages() {
		return totalNumberOfPages;
	}

	public void setTotalNumberOfPages(Integer totalNumberOfPages) {
		this.totalNumberOfPages = totalNumberOfPages;
	}

	public String getPageSetToken() {
		return pageSetToken;
	}

	public void setPageSetToken(String pageSetToken) {
		this.pageSetToken = pageSetToken;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String generateRamdomId() {
		return UUID.randomUUID().toString().replace("-", "_");
	}
	protected Integer pageSize = 15;
	
	public String getPaginatedCustomerList()
	{
		String resultType="page";
		String pageNo = pageNumber.toString();
		String size = pageSize.toString();
		if(!(shownCustomerId!=null && shownCustomerId.trim().length()>0)) { // if the shownCustomerID is not passed taking cusotmerId from the context
			if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext)) { // checking if a ship to is selected
				shownCustomerId = wcContext.getCustomerId();// if the shownCustomerId is not passed taking the selected ship to
			}
			else {	// no ship to is selected, so selecting the master customer and displaying all the child customers under it
				shownCustomerId = wcContext.getCustomerId();
			}
		}
		if(!(organizationCode!=null && organizationCode.trim().length()>0)) { // if the organization code is not passed taking org code from the context
			organizationCode = wcContext.getStorefrontId();
		}
		try {
			if(shownCustomerSuffixType == null ||shownCustomerSuffixType.trim().length()==0)
			{
				resultType=SUCCESS;
				shownCustomerDoc = XPEDXWCUtils.getCustomerDetails(shownCustomerId, organizationCode, XPEDXConstants.MASHUP_SHOW_LOCATIONS);
				Element custElem = shownCustomerDoc.getDocumentElement();
				Element ExtnElem = SCXmlUtil.getChildElement(custElem, "Extn");
				shownCustomerSuffixType = SCXmlUtil.getAttribute(ExtnElem, "ExtnSuffixType");
				Element buyrOrgElement = SCXmlUtil.getChildElement(custElem, "BuyerOrganization");
				buyrOrgName=SCXmlUtil.getAttribute(buyrOrgElement,"OrganizationName");

			}
			YFCDocument orgListDoucment = YFCDocument
			.createDocument("OrganizationList");
			YFCElement orgListElement = orgListDoucment.getDocumentElement();
			if("C".equals(shownCustomerSuffixType) || shownCustomerSuffixType== null)
				getChildOrgList(orgListDoucment, orgListElement, organizationCode, shownCustomerSuffixType,false,pageNo,size);
			else
				getChildOrgList(orgListDoucment, orgListElement, organizationCode, shownCustomerSuffixType,true,pageNo,size);
			
			if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.SAP_CUSTOMER_SUFFIX_TYPE)) {
				//shown customer is a sap customer. So we have to show all the billto's and shipto's under the sap customer
				customerID = shownCustomerId;
				//String customerKey = custElem.getAttribute("CustomerKey");
				allChildCustomersDoc =orgListDoucment.getDocument();// getAllChildCustomers(customerKey,customerID);
				if(allChildCustomersDoc!=null) {
					childCustomers = SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");
				}
				setChildsHierarchyMap();
				setAllChildCustomerInfoDisplayFormat();
			}
			else if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE)) {
				// shown customer is Bill to. We have to show all the billto's and ship to's under the parent of bill to(SAP) except the shown bill to customer
				//getting the parent customer. i.e., SAP and getting all the child's of SAP
				String customerID = shownCustomerId;
				String customerKey = null;
				allChildCustomersDoc =orgListDoucment.getDocument();
				if(allChildCustomersDoc!=null) {
					childCustomers = SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");
//					removeShownCustomer(); Instead of removing, now we are preselecting the customer
				}
				setChildsHierarchyMap();
				setAllChildCustomerInfoDisplayFormat();
				
				profileDocument = countriesListDoc;
				parsePageInfo(profileDocument.getDocumentElement(), true);
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return resultType;
	}
	
	/**
	 * @param env
	 * @param orgListDoucment
	 * @param orgListElement
	 * @param customerKey
	 * @throws RemoteException
	 *             Method to form the Organization element(this method gets
	 *             called recursively to fetch all the child organizations)
	 */
	private void getChildOrgList(
			YFCDocument orgListDoucment, YFCElement orgListElement,String organizationCode,String suffixType,boolean isGetPageAPI, String pageNumber,String pageSize) throws Exception {
		// Form the input to fetch the child org list
		if(suffixType== null ||suffixType.trim().length() ==0)
			suffixType="C";
		
		
			YFCDocument inputCustomerDocument = YFCDocument
				.createDocument("XPXCustomerHierarchyView");
		YFCElement inputCustomerElement = inputCustomerDocument
				.getDocumentElement();
		YFCElement complexQueryElement = inputCustomerDocument
		.createElement("ComplexQuery");
		YFCElement sort = inputCustomerDocument.createElement("OrderBy");
		YFCElement attribute = inputCustomerDocument.createElement("Attribute");
		sort.appendChild(attribute);
		
		YFCElement orElement = inputCustomerDocument
		.createElement("Or");
		complexQueryElement.appendChild(orElement);
		YFCElement expElement = inputCustomerDocument
		.createElement("Exp");
		if(suffixType.equals(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE))
		{
			expElement.setAttribute("Name", "BillToCustomerID");
			expElement.setAttribute("Value", shownCustomerId);
			expElement.setAttribute("QryType", "FLIKE");
			attribute.setAttribute("Name", "ShipToCustomerID");
			attribute.setAttribute("Desc", "N");
			sort.appendChild(attribute);
			orElement.appendChild(expElement);
		}
		if(suffixType.equals(XPEDXConstants.SAP_CUSTOMER_SUFFIX_TYPE))
		{
			expElement.setAttribute("Name", "SAPCustomerID");
			expElement.setAttribute("Value", shownCustomerId);
			expElement.setAttribute("QryType", "FLIKE");
			attribute.setAttribute("Name", "BillToCustomerID");
			attribute.setAttribute("Desc", "N");
			sort.appendChild(attribute);
			orElement.appendChild(expElement);
		}
		inputCustomerElement.appendChild(complexQueryElement);
		inputCustomerElement.appendChild(sort);
		Element outputCustomerElement =null;
		if(isGetPageAPI)
		{
				Integer pageNumberToCheck,pageSizeToCheck;
				pageNumberToCheck = Integer.parseInt(pageNumber);
				pageSizeToCheck = Integer.parseInt(pageSize);
				if(!(pageNumber!=null && pageNumber.trim().length()>0 && pageNumberToCheck>0))
					pageNumber="1";
				if(!(pageSize!=null && pageSize.trim().length()>0 && pageSizeToCheck>0))
					pageSize="25";
			
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("/Page/@PageNumber", pageNumber);
				valueMap.put("/Page/@PageSize", pageSize);
				valueMap.put("/Page/@PageSetToken", pageSetToken);

				try{
				Element input = WCMashupHelper.getMashupInput("xpedx-getPaginatedAssignedShipTosView-Profile", valueMap, wcContext.getSCUIContext());
				Element pageElement=(Element)input.getElementsByTagName("Input").item(0);
				pageElement.appendChild(input.getOwnerDocument().importNode(inputCustomerDocument.getDocument().getDocumentElement(), true));
				Object obj = WCMashupHelper.invokeMashup("xpedx-getPaginatedAssignedShipTosView-Profile", input, wcContext.getSCUIContext());
				
				countriesListDoc = ((Element) obj).getOwnerDocument();
				outputCustomerElement=(Element)countriesListDoc.getElementsByTagName("XPXCustHierarchyViewList").item(0);
			} catch (CannotBuildInputException e) {
				// Error in invoking mashup
			}
			}
		else
		{
			YFCDocument outputCustomerListTemplateDoc = YFCDocument
					.createDocument("XPXCustHierarchyViewList");
			YFCElement outputCustomerListTemplateElement = outputCustomerListTemplateDoc
					.getDocumentElement();
			outputCustomerListTemplateElement.setAttribute("TotalNumberOfRecords",
					"");
			YFCElement outputCustomerTemplateElement = outputCustomerListTemplateDoc
					.createElement("XPXCustHierarchyView");
			outputCustomerListTemplateElement
					.appendChild(outputCustomerTemplateElement);
			ISCUITransactionContext scuiTransactionContext = getWCContext().getSCUIContext().getTransactionContext(true);
			YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
			.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
			env.setApiTemplate("XPXCustomerHierarchyViewService", outputCustomerListTemplateDoc
					.getDocument());
			YIFApi api = YIFClientFactory.getInstance().getApi();
			Document outputCustomerDocument = api.executeFlow(env, "XPXCustomerHierarchyViewService",
					inputCustomerDocument.getDocument());
			env.clearApiTemplate("XPXCustomerHierarchyViewService");
			outputCustomerElement = outputCustomerDocument
					.getDocumentElement();
		}
		createOrganization(outputCustomerElement,orgListElement,
		orgListDoucment,organizationCode,suffixType);
	}
	
	private void createOrganization(Element outputCustomerElement,YFCElement orgListElement,
			YFCDocument orgListDoucment,String organizationCode,String suffixType)
	{
		ArrayList<Element> childElementList=SCXmlUtil.getElements(outputCustomerElement, "XPXCustHierarchyView");
		int root=0;
		
		if(childElementList != null && childElementList.size()>0)
		{
			if(suffixType.equals("C"))
				getCustomer(childElementList,orgListElement,orgListDoucment,organizationCode,root,"BillTo");
			if(suffixType.equals("B"))
				getCustomer(childElementList,orgListElement,orgListDoucment,organizationCode,root,"ShipTo");
		}
		
	}
	
	private void getCustomer(ArrayList<Element> childElementList,YFCElement orgListElement,YFCDocument orgListDoucment,
			String organizationCode,int root,String suffixType)
	{
		//String customersArr[]={"ShipTo","BillTo","SAP","MSAP"};	
		ArrayList<String> addedCustomer=new ArrayList<String>();
		for(int i=0;i<childElementList.size();i++)
		{
			Element customerElement=childElementList.get(i);
				if(!addedCustomer.contains(customerElement.getAttribute(suffixType+"CustomerID")))
				{
						YFCElement orgElement = orgListDoucment.createElement("Organization");
						orgElement.setAttribute("OrganizationCode", organizationCode);
						orgElement.setAttribute("CustomerID", customerElement.getAttribute(suffixType+"CustomerID"));
						addedCustomer.add(customerElement.getAttribute(suffixType+"CustomerID"));
						orgElement.setAttribute("ParentCustomerID", shownCustomerId);
						if("BillTo".equals(suffixType))
							orgElement.setAttribute("CustomerSuffixType", "B");
						if("ShipTo".equals(suffixType))
							orgElement.setAttribute("CustomerSuffixType", "S");
						orgElement.setAttribute("CustomerName", customerElement.getAttribute(suffixType+"CustomerName"));
						orgElement.setAttribute("SAPParentAccNo", customerElement.getAttribute(suffixType+"SAPParentAccNo"));
						orgElement.setAttribute("SAPNumber", customerElement.getAttribute(suffixType+"SAPNumber"));
						orgListElement.appendChild(orgElement);
				}
			}
		//To get The parent Customer
		if(childElementList.size() >0)
		{
			Element customerElement=childElementList.get(0);
			if(suffixType.equals("BillTo"))
				suffixType="SAP";
			if(suffixType.equals("ShipTo"))
				suffixType="BillTo";
			if(!addedCustomer.contains(customerElement.getAttribute(suffixType+"CustomerID")))
			{
					YFCElement orgElement = orgListDoucment.createElement("Organization");
					orgElement.setAttribute("OrganizationCode", organizationCode);
					orgElement.setAttribute("CustomerID", customerElement.getAttribute(suffixType+"CustomerID"));
					addedCustomer.add(customerElement.getAttribute(suffixType+"CustomerID"));
					orgElement.setAttribute("ParentCustomerID",shownCustomerId);
					if("BillTo".equals(suffixType))
						orgElement.setAttribute("CustomerSuffixType", "B");
					if("SAP".equals(suffixType))
						orgElement.setAttribute("CustomerSuffixType", "C");
					orgElement.setAttribute("CustomerName", customerElement.getAttribute(suffixType+"CustomerName"));
					orgElement.setAttribute("SAPParentAccNo", customerElement.getAttribute(suffixType+"SAPParentAccNo"));
					orgElement.setAttribute("SAPNumber", customerElement.getAttribute(suffixType+"SAPNumber"));
					orgListElement.appendChild(orgElement);
			}
		}
	}
	
	public String execute() {
		if(!(shownCustomerId!=null && shownCustomerId.trim().length()>0)) { // if the shownCustomerID is not passed taking cusotmerId from the context
			if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext)) { // checking if a ship to is selected
				shownCustomerId = wcContext.getCustomerId();// if the shownCustomerId is not passed taking the selected ship to
			}
			else {	// no ship to is selected, so selecting the master customer and displaying all the child customers under it
				shownCustomerId = wcContext.getCustomerId();
			}
		}
		if(!(organizationCode!=null && organizationCode.trim().length()>0)) { // if the organization code is not passed taking org code from the context
			organizationCode = wcContext.getStorefrontId();
		}
		try {
			shownCustomerDoc = XPEDXWCUtils.getCustomerDetails(shownCustomerId, organizationCode, XPEDXConstants.MASHUP_SHOW_LOCATIONS);
			Element custElem = shownCustomerDoc.getDocumentElement();
			Element ExtnElem = SCXmlUtil.getChildElement(custElem, "Extn");
			shownCustomerSuffixType = SCXmlUtil.getAttribute(ExtnElem, "ExtnSuffixType");
			
			//added for jira 2769
			Element buyrOrgElement = SCXmlUtil.getChildElement(custElem, "BuyerOrganization");
			buyrOrgName=SCXmlUtil.getAttribute(buyrOrgElement,"OrganizationName");


			
			if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.MASTER_CUSTOMER_SUFFIX_TYPE)) {
				//shown customer is Master customer. This is not a scenario at all, but if in case, the shown customer is MSAP, we will only show him SAP
				String customerID = custElem.getAttribute("CustomerID");
				String customerKey = custElem.getAttribute("CustomerKey");
				allChildCustomersDoc = getAllChildCustomers(customerKey,customerID);
				//getting only the SAP customers
				childCustomers = SCXmlUtil.getElementsByAttribute(
						allChildCustomersDoc.getDocumentElement(),
						"Organization", "CustomerSuffixType",
						XPEDXConstants.SAP_CUSTOMER_SUFFIX_TYPE);
				prepareHierachyMapsForMSAP();
				setAllChildCustomerInfoDisplayFormat();
			}
			else if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.SAP_CUSTOMER_SUFFIX_TYPE)) {
				//shown customer is a sap customer. So we have to show all the billto's and shipto's under the sap customer
				String customerID = custElem.getAttribute("CustomerID");
				String customerKey = custElem.getAttribute("CustomerKey");
				allChildCustomersDoc = getAllChildCustomers(customerKey,customerID);
				if(allChildCustomersDoc!=null) {
					childCustomers = SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");
				}
				setChildsHierarchyMap();
				setAllChildCustomerInfoDisplayFormat();
			}
			else if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE)) {
				// shown customer is Bill to. We have to show all the billto's and ship to's under the parent of bill to(SAP) except the shown bill to customer
				//getting the parent customer. i.e., SAP and getting all the child's of SAP
				Element parentCustomer = SCXmlUtil.getChildElement(custElem, "ParentCustomer");
				String customerID = parentCustomer.getAttribute("CustomerID");
				String customerKey = parentCustomer.getAttribute("CustomerKey");
				allChildCustomersDoc = getAllChildCustomers(customerKey,customerID);
				if(allChildCustomersDoc!=null) {
					childCustomers = SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");
//					removeShownCustomer(); Instead of removing, now we are preselecting the customer
				}
				setChildsHierarchyMap();
				setAllChildCustomerInfoDisplayFormat();
			}
			else if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.SHIP_TO_CUSTOMER_SUFFIX_TYPE)) {
				// shown customer is Ship to. We have to show all the billto's and ship to's under the parent of bill to(SAP) except the shown bill to customer
				Element parentCustomer1 = SCXmlUtil.getChildElement(custElem, "ParentCustomer");// getting the bill to customer
				Element parentCustomer2= SCXmlUtil.getChildElement(parentCustomer1, "ParentCustomer");// getting the ship to customer
				String customerID = parentCustomer2.getAttribute("CustomerID");
				String customerKey = parentCustomer2.getAttribute("CustomerKey");
				allChildCustomersDoc = getAllChildCustomers(customerKey,customerID);
				if(allChildCustomersDoc!=null) {
					childCustomers = SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");
//					Changed as part of new UI that came
//					removeShownCustomer(); Instead of removing, now we are preselecting the customer
				}
				setChildsHierarchyMap();
				setAllChildCustomerInfoDisplayFormat();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	
		private void parsePageInfo(Element ele, boolean paginated) throws Exception {

	        UtilBean util = new UtilBean();
	        Element page = util.getElement(ele, "//Page");

	        setIsLastPage(Boolean.FALSE);
	        if ((paginated) && (page != null)) {
	        	setIsLastPage(SCXmlUtil.getBooleanAttribute(page, "IsLastPage", getIsLastPage()));
	        }

	        setIsFirstPage(Boolean.FALSE);
	        if ((paginated) && (page != null)) {
	            setIsFirstPage(SCXmlUtil.getBooleanAttribute(page, "IsFirstPage", getIsFirstPage()));
	        }

	        setIsValidPage(Boolean.FALSE);
	        if ((paginated) && (page != null)) {
	        	setIsValidPage(SCXmlUtil.getBooleanAttribute(page, "IsValidPage", getIsValidPage()));
	        }

	        if ((paginated) && (page != null)) {
	            setPageNumber(getIntegerAttribute(page, "PageNumber", getPageNumber()));
	        }
	        
	        if ((paginated) && (page != null)) {
	        	setPageSetToken(page.getAttribute("PageSetToken"));
	        }

	        setTotalNumberOfPages(new Integer(0));
	        if ((paginated) && (page != null)) {
	        	setTotalNumberOfPages(getIntegerAttribute(page,
	                    "TotalNumberOfPages", getTotalNumberOfPages()));
	        }
	    }
		
		private Integer getIntegerAttribute(Element page, String name,
	            Integer defaultValue) {
	        Integer value = defaultValue;
	        String str = page.getAttribute(name);
	        try {
	            value = Integer.valueOf(str);
	        } catch (NumberFormatException e) {
	            value = defaultValue;
	        }
	        return value;
	    }
		
		
	private void setAllChildCustomerInfoDisplayFormat() {
		ArrayList<String> allChildCustomerIds = new ArrayList<String>();
		ArrayList<String> sapCustomersIds = new ArrayList<String>();
		childCustomers = SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");
		Iterator<Element> iterator = childCustomers.iterator();
		while(iterator.hasNext()) {
			Element childOrgElem = iterator.next();
			String childCustomerID = childOrgElem.getAttribute("CustomerID");
			if(!allChildCustomerIds.contains(childCustomerID)) {
				allChildCustomerIds.add(childCustomerID);
			}
		}
		/*ArrayList<Element> allBillTos = SCXmlUtil.getElementsByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization","CustomerSuffixType",XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE);
		Iterator<Element> allBillTosIterator = allBillTos.iterator();
		while(allBillTosIterator.hasNext()) {
			Element childBillToOrgElem = allBillTosIterator.next();
			String sapCustomerID = childBillToOrgElem.getAttribute("ParentCustomerID");
			if(!sapCustomersIds.contains(sapCustomerID)) {
				sapCustomersIds.add(sapCustomerID);
			}
		}
		allChildCustomerIds.addAll(sapCustomersIds);
*/		if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext))
			allChildCustomerIds.add(XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext));
		else
			allChildCustomerIds.add(wcContext.getCustomerId());
		displayChildCustomersMap = XPEDXWCUtils.custFullAddresses(allChildCustomerIds, organizationCode);
		if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.SAP_CUSTOMER_SUFFIX_TYPE)) {
			sapCustomerDisplay=displayChildCustomersMap.get(shownCustomerId);
		}
		// TODO Auto-generated method stub		
	}

	private Document getAllChildCustomers(String customerKey, String customerID) throws CannotBuildInputException {
		Document allAssignedCustomerDoc = null;

		if (null == customerID && null == customerKey) {
			log.debug("getAllChildCustomersDoc: customerID is a required field. Returning a empty Document");
			return allAssignedCustomerDoc;
		}

		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Customer/@CustomerID", customerID);
		valueMap.put("/Customer/@OrganizationCode", context.getStorefrontId());
		valueMap.put("/Customer/@CustomerKey", customerKey);

		Element input = WCMashupHelper.getMashupInput(
				"xpedxGetAllChilds", valueMap, wSCUIContext);
		String inputXml = SCXmlUtil.getString(input);
		log.debug("xpedxGetAllChilds: Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxGetAllChilds",
				input, wSCUIContext);
		allAssignedCustomerDoc = ((Element) obj).getOwnerDocument();
		if (null != allAssignedCustomerDoc) {
			log.debug("xpedxGetAllChilds: Output XML: "
					+ SCXmlUtil.getString((Element) obj));
		}
		return allAssignedCustomerDoc;
	}
	
	private void removeShownCustomer() {
		childCustomers = SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");
		Iterator<Element> iterator = childCustomers.iterator();
		while(iterator.hasNext()) {
			Element childOrgElem = iterator.next();
			String childCustomerID = childOrgElem.getAttribute("CustomerID");
			if(childCustomerID.equalsIgnoreCase(shownCustomerId)) {
				iterator.remove();
				break;
			}
		}
	}
	
	private void setChildsHierarchyMap() {
		/*if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.MASTER_CUSTOMER_SUFFIX_TYPE)) {
			prepareHierachyMapsForMSAP();
		}*/
		billToCustomers =SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");// SCXmlUtil.getElementsByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization", "ParentCustomerID", shownCustomerId);
		//prepareHierachyMaps();
		ArrayList<String> sapCustomerIDs = new ArrayList<String>();
		for(int i=0;i<billToCustomers.size();i++)
		{
			
			//Element extnElement=(Element)billToCustomers.get(i).getElementsByTagName("Extn").item(0);
			if(!"C".equals(billToCustomers.get(i).getAttribute("CustomerSuffixType")) )
			{
				if(!("B".equals(billToCustomers.get(i).getAttribute("CustomerSuffixType")) && "B".equals(shownCustomerSuffixType)))
				{
					sapCustomerIDs.add(billToCustomers.get(i).getAttribute("CustomerID"));
					suffixTypeMap.put(billToCustomers.get(i).getAttribute("CustomerID"),billToCustomers.get(i).getAttribute("CustomerSuffixType"));
				}
			}
		}
		if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext))
			msapAndSapCustomersMap.put(XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext), sapCustomerIDs);
		else
			msapAndSapCustomersMap.put(wcContext.getCustomerId(), sapCustomerIDs);
		
	}
	
	private void prepareHierachyMaps() {
		ArrayList<String> billToCustomersIDs = new ArrayList<String>();
		String sapCustomerID = null;
		if(billToCustomers!=null && billToCustomers.size()>0) {
			Iterator<Element> iterator = billToCustomers.iterator();
			while(iterator.hasNext()) {
				Element billToCustomerElem = iterator.next();
				String billToCustomerID = billToCustomerElem.getAttribute("CustomerID");
				sapCustomerID = billToCustomerElem.getAttribute("ParentCustomerID");
				if(!(billToCustomersIDs.contains(billToCustomerID))) {
					billToCustomersIDs.add(billToCustomerID);
				}
				ArrayList<Element> shipToCustomers = SCXmlUtil.getElementsByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization", "ParentCustomerID", billToCustomerID);
				Iterator<Element> shipToIterator = shipToCustomers.iterator();
				ArrayList<String> shipToCustomersIDs = new ArrayList<String>();
				while(shipToIterator.hasNext()) {
					Element shipToElement = shipToIterator.next();
					String shipToCustomerID = shipToElement.getAttribute("CustomerID");
					if(!(shipToCustomersIDs.contains(shipToCustomerID))) {
						shipToCustomersIDs.add(shipToCustomerID);
					}
				}
				billToAndShipToCustomersMap.put(billToCustomerID, shipToCustomersIDs);
			}
		}
		sapAndBillToCustomersMap.put(sapCustomerID, billToCustomersIDs);
	}
	
	private void prepareHierachyMapsForMSAP() {
		//Came here as the shown customer is MSAP customer
		ArrayList<String> sapCustomersIDs = new ArrayList<String>();
		ArrayList<Element> sapCustomers = SCXmlUtil.getElementsByAttribute(allChildCustomersDoc.getDocumentElement(),"Organization","ParentCustomerID",shownCustomerId);
		Iterator<Element> sapCustomersIterator = sapCustomers.iterator();
		while(sapCustomersIterator.hasNext()) {
			Element sapCustomerElement = sapCustomersIterator.next();
			String sapCustomerID = sapCustomerElement.getAttribute("CustomerID");
			billToCustomers = SCXmlUtil.getElementsByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization", "ParentCustomerID",sapCustomerID);
			prepareHierachyMaps();
			if(!(sapCustomersIDs.contains(sapCustomerID))) {
				sapCustomersIDs.add(sapCustomerID);
			}
		}
		//msapAndSapCustomersMap.put(shownCustomerId, sapCustomersIDs);		
	}

	public String getShownCustomerId() {
		return shownCustomerId;
	}

	public void setShownCustomerId(String shownCustomerId) {
		this.shownCustomerId = shownCustomerId;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public Document getShownCustomerDoc() {
		return shownCustomerDoc;
	}

	public void setShownCustomerDoc(Document shownCustomerDoc) {
		this.shownCustomerDoc = shownCustomerDoc;
	}

	public Document getAllChildCustomersDoc() {
		return allChildCustomersDoc;
	}

	public void setAllChildCustomersDoc(Document allChildCustomersDoc) {
		this.allChildCustomersDoc = allChildCustomersDoc;
	}

	public ArrayList<Element> getChildCustomers() {
		return childCustomers;
	}

	public void setChildCustomers(ArrayList<Element> childCustomers) {
		this.childCustomers = childCustomers;
	}

	public Map<String, String> getDisplayChildCustomersMap() {
		return displayChildCustomersMap;
	}

	public void setDisplayChildCustomersMap(
			Map<String, String> displayChildCustomersMap) {
		this.displayChildCustomersMap = displayChildCustomersMap;
	}

	public Map<String, ArrayList<String>> getMsapAndSapCustomersMap() {
		return msapAndSapCustomersMap;
	}

	public void setMsapAndSapCustomersMap(
			Map<String, ArrayList<String>> msapAndSapCustomersMap) {
		this.msapAndSapCustomersMap = msapAndSapCustomersMap;
	}

	public Map<String, ArrayList<String>> getSapAndBillToCustomersMap() {
		return sapAndBillToCustomersMap;
	}

	public void setSapAndBillToCustomersMap(
			Map<String, ArrayList<String>> sapAndBillToCustomersMap) {
		this.sapAndBillToCustomersMap = sapAndBillToCustomersMap;
	}

	public Map<String, ArrayList<String>> getBillToAndShipToCustomersMap() {
		return billToAndShipToCustomersMap;
	}

	public void setBillToAndShipToCustomersMap(
			Map<String, ArrayList<String>> billToAndShipToCustomersMap) {
		this.billToAndShipToCustomersMap = billToAndShipToCustomersMap;
	}
	
	public String getmSapName() {
		return mSapName;
	}

	public void setmSapName(String mSapName) {
		this.mSapName = mSapName;
	}
	
	public String getBuyrOrgName() {
		return buyrOrgName;
	}

	public void setBuyrOrgName(String buyrOrgName) {
		this.buyrOrgName = buyrOrgName;
	}

	public Map<String, String> getSuffixTypeMap() {
		return suffixTypeMap;
	}

	public void setSuffixTypeMap(Map<String, String> suffixTypeMap) {
		this.suffixTypeMap = suffixTypeMap;
	}

	public String getShownCustomerSuffixType() {
		return shownCustomerSuffixType;
	}

	public void setShownCustomerSuffixType(String shownCustomerSuffixType) {
		this.shownCustomerSuffixType = shownCustomerSuffixType;
	}

	public String getSapCustomerDisplay() {
		return sapCustomerDisplay;
	}

	public void setSapCustomerDisplay(String sapCustomerDisplay) {
		this.sapCustomerDisplay = sapCustomerDisplay;
	}
	
	

}
