package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsGetShareListAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsDetailsGetShareListAction.class);
	private Document outDoc;
	
	public static final String COMMAND_GET_SHARE_LIST 		= "get_share_list";
	public static final String MID_GET_CUSTOMER_LIST		= "XPEDXMyItemsGetCustomersList";
	
	private String command 		= "";
	private String itemCount 	= "-1";
	private String customerId 	= "";
	private String suffixType = "";
	private String parentCusotmerID="";
	public String getParentCusotmerID() {
		return parentCusotmerID;
	}

	public void setParentCusotmerID(String parentCusotmerID) {
		this.parentCusotmerID = parentCusotmerID;
	}

	public String getSuffixType() {
		return suffixType;
	}

	public void setSuffixType(String suffixType) {
		this.suffixType = suffixType;
	}

	private String showRoot 	= "";
	private String sfId			= "";
	
	private String listKey 	= ""; //For the listing page
	private String listName = ""; //For the listing page
	private String listDesc	= "";
	private String editMode = "";
	protected Integer pageNumber = 1;
	protected Integer pageSize = 15;
    private Boolean isFirstPage = Boolean.FALSE;
    private Boolean isLastPage = Boolean.FALSE;
    private Boolean isValidPage = Boolean.FALSE;
    private Integer totalNumberOfPages = new Integer(1);
    private String custSuffixType = null;
    private int shipTosSize = 0;
    private String countCustomer="0";
	

	//Added for JIRA 3589 : For paginating shipsTos
    private String billtosuffixtype;
	public String getBilltosuffixtype() {
		return billtosuffixtype;
	}

	public void setBilltosuffixtype(String billtosuffixtype) {
		this.billtosuffixtype = billtosuffixtype;
	}
	////end for JIRA 3589
	
	public int getShipTosSize() {
		return shipTosSize;
	}

	public void setShipTosSize(int shipTosSize) {
		this.shipTosSize = shipTosSize;
	}

	public String getCustSuffixType() {
		return custSuffixType;
	}

	public void setCustSuffixType(String custSuffixType) {
		this.custSuffixType = custSuffixType;
	}

	protected Map<String, String> displayCustomerMap = new HashMap<String, String>();
	HashMap<String, String> suffixTypeMap = new HashMap<String, String>();
	HashMap<String, String> shipFromDivisionMap = new HashMap<String, String>();
	HashMap<String, String> customerPathMap = new HashMap<String, String>();
	Set<String> customerIdSet = new HashSet<String>();
	Set<String> childCustomerIdSet = new HashSet<String>();
	private String totalNumOfRecords;
	private String pageSetToken;
	public String getPageSetToken() {
		return pageSetToken;
	}

	public void setPageSetToken(String pageSetToken) {
		this.pageSetToken = pageSetToken;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String execute() {
		try {
			Map<String, Element> out;
			
			//Get the current customer level information
			/*
			String xmlString;
			xmlString = "<?xml version = \"1.0\"?>" +
					"<CustomerList>" +
					"	<Customer name=\"Customer 1\" type=\"BillTo\" ID=\"c1\" OrganizationCode=\"xpedx\"> " +
					"		<CustomerList>" +
					"			<Customer name=\"Customer 2\"  type=\"ShipTo\" ID=\"c2\" OrganizationCode=\"xpedx\" /> " +
					"			<Customer name=\"Customer 3\" type=\"ShipTo\" ID=\"c3\" OrganizationCode=\"xpedx\" /> " +
					"			<Customer name=\"Customer 4\" type=\"ShipTo\" ID=\"c4\" OrganizationCode=\"xpedx\" /> " +
					"		</CustomerList>" +
					"	</Customer>" +
					"	<Customer name=\"Customer 5\" type=\"BillTo\" ID=\"c5\" OrganizationCode=\"xpedx\"> " +
					"		<CustomerList>" +
					"			<Customer name=\"Customer 6\" type=\"ShipTo\" ID=\"c6\" OrganizationCode=\"xpedx\" /> " +
					"			<Customer name=\"Customer 7\" type=\"ShipTo\" ID=\"c7\" OrganizationCode=\"xpedx\" /> " +
					"		</CustomerList>" +
					"	</Customer>" +
					"</CustomerList>" +
					"";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			       
			DocumentBuilder builder = factory.newDocumentBuilder();
			//outDoc = builder.parse(new InputSource(new StringReader(xmlString)));
			
			*/
			setSfId(getWCContext().getStorefrontId());
			/*Commented for Performance issue. JIRA 3589*/
			/*Document customerDoc = XPEDXWCUtils.getCustomerDetails(customerId, wcContext.getStorefrontId(), "xpedx-customer-getCustomExtnFieldsInformation");
			String custSuffixType = null;
			if(customerDoc!=null) {
				Element custExtnElem = SCXmlUtil.getChildElement(customerDoc.getDocumentElement(), "Extn");
				custSuffixType = SCXmlUtil.getAttribute(custExtnElem, "ExtnSuffixType");
			}*/		
			
        //Added for JIRA 3589
			billtosuffixtype = "B";
		custSuffixType = getWCContext().getSCUIContext().getRequest().getParameter("suffixtype");
		if(custSuffixType != null && custSuffixType.equals("")){
				XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
				custSuffixType = xpedxCustomerContactInfoBean.getExtnSuffixType();
		}
		if(custSuffixType!=null && custSuffixType.trim().length()>0 && custSuffixType.equalsIgnoreCase(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE)) {
				String pageNo = pageNumber.toString();
				String size = pageSize.toString();
				Document document = XPEDXWCUtils.getPaginatedShipTosForMIL(customerId, custSuffixType, pageNo, size, pageSetToken, getWCContext());
				parsePageInfo(document.getDocumentElement(), true);
				List<String> shipTos = parseForShiptoIds(document);
				shipTosSize = shipTos.size();
				if(LOG.isDebugEnabled()){
			  	LOG.debug(SCXmlUtil.getString(document));
				}
			if(shipTosSize>0){							
				Element custElem = null;
				Element custElem1 = null;
				Element temp = null;
				Document createdDoc = SCXmlUtil.createDocument("XPXCustHierarchyViewList");
				if(LOG.isDebugEnabled()){
				LOG.debug(SCXmlUtil.getString(custElem));
				}
				for(int i=0;i<shipTosSize;i++)
				{					
					NodeList customerNodeList=document.getElementsByTagName("XPXCustHierarchyView");
					if(customerNodeList !=null)
					{
						custElem=(Element)customerNodeList.item(i);
						
						if(custElem != null)
						{	
							if(custElem1 !=null){								
								 temp= createdDoc.createElement("XPXCustHierarchyView");
								 temp = custElem;
								 SCXmlUtil.importElement(createdDoc.getDocumentElement(), temp);
							}
							else {
								custElem1 = custElem;
							} 
					    if(LOG.isDebugEnabled()){
						LOG.debug(SCXmlUtil.getString(custElem1));
					    }
						}
					}
				}
				SCXmlUtil.importElement(createdDoc.getDocumentElement(), custElem1);
				if(LOG.isDebugEnabled()){
				LOG.debug(SCXmlUtil.getString(createdDoc));
				}
				outDoc = createdDoc;
				
				if(LOG.isDebugEnabled()){
					LOG.debug(SCXmlUtil.getString(outDoc));
				}
				
			}else{// if no shipTos are there under the selected Bill To
				
					Document createdDoc = SCXmlUtil.createDocument("CustomerList");
					Element createdElem = createdDoc.createElement("Customer");
					createdElem.setAttribute("CustomerID",customerId);
					createdElem.setAttribute("OrganizationCode", wcContext.getStorefrontId());
					Element createdElemChild = createdDoc.createElement("CustomerList");
					createdElemChild.setAttribute("TotalNumberOfRecords", "0");
					SCXmlUtil.importElement(createdElem, createdElemChild);
					SCXmlUtil.importElement(createdDoc.getDocumentElement(), createdElem);
					
					if(LOG.isDebugEnabled()){
						LOG.debug(SCXmlUtil.getString(createdDoc));
					}
					outDoc = createdDoc;				
			}
			setCustomerInformationMaps();
			/*Commented for Performance - JIRA 3589*/
				/*Document newDoc = XPEDXWCUtils.getCustomerDetails(shipTos, wcContext.getStorefrontId(),"XPEDXMyItemsPaginatedShipTosCustomerInfo");
				if(newDoc!=null) {
					 createdDoc = SCXmlUtil.createDocument("CustomerList");
					Element createdElem = createdDoc.createElement("Customer");
					createdElem.setAttribute("CustomerID",customerId);
					createdElem.setAttribute("OrganizationCode", wcContext.getStorefrontId());
					newDoc.getDocumentElement().setAttribute("TotalNumberOfRecords", totalNumOfRecords);
					SCXmlUtil.importElement(createdElem, newDoc.getDocumentElement());
					SCXmlUtil.importElement(createdDoc.getDocumentElement(), createdElem);
					outDoc = createdDoc;
				}
				else
				{
					 createdDoc = SCXmlUtil.createDocument("CustomerList");
					Element createdElem = createdDoc.createElement("Customer");
					createdElem.setAttribute("CustomerID",customerId);
					createdElem.setAttribute("OrganizationCode", wcContext.getStorefrontId());
					Element createdElemChild = createdDoc.createElement("CustomerList");
					createdElemChild.setAttribute("TotalNumberOfRecords", "0");
					SCXmlUtil.importElement(createdElem, createdElemChild);
					SCXmlUtil.importElement(createdDoc.getDocumentElement(), createdElem);					
					outDoc = createdDoc;
					
				} */
			}				
			else {
				Element listOfCustomer = prepareAndInvokeMashup(MID_GET_CUSTOMER_LIST);
				outDoc = (Document)listOfCustomer.getOwnerDocument();
				setCustomerInformationMaps();
			}

		/* JIRA-3745  WC - MIL - Lists of Lists List Count is not Accurate  */
		 ArrayList<Element> countCustomerList=SCXmlUtil.getElements(outDoc.getDocumentElement(), "Customer/CustomerList/Customer");
		 
		 if(countCustomerList!=null)
		 {
			 setCountCustomer(countCustomerList.size()+"");
	     }
			
			/*
			out = prepareAndInvokeMashups();
			if (out.values().iterator().next() != null){
				outDoc = (Document)out.values().iterator().next().getOwnerDocument();
			}
			*/
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
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
	
	private List<String> parseForShiptoIds(Document PaginatedDocument) {
		List<String> shiptoList = new ArrayList<String>();
		Set<String> shipTos = new HashSet<String>();
		if(PaginatedDocument!=null) {
			Element Page = PaginatedDocument.getDocumentElement();
			Element ouputElement = SCXmlUtil.getChildElement(Page, "Output");
			Element shipToListElement = SCXmlUtil.getChildElement(ouputElement, "XPXCustHierarchyViewList");
			totalNumOfRecords = shipToListElement.getAttribute("TotalNumberOfRecords");
			ArrayList<Element> assignedCustElems = SCXmlUtil.getElements(shipToListElement, "XPXCustHierarchyView");
			if(assignedCustElems.size()>0) {
				for(int i=0;i<assignedCustElems.size();i++) {
					Element customer = assignedCustElems.get(i);
					String shipToCustomerID =  customer.getAttribute("ShipToCustomerID");
					shipTos.add(shipToCustomerID);
				}
			}
			shiptoList.addAll(shipTos);
		}
		return shiptoList;
	}
	
	private void setCustomerInformationMaps() {
		// If Bill to is selected for expanding, then we need to show all the shipTos under it.
		if(custSuffixType!=null && custSuffixType.trim().length()>0 && custSuffixType.equalsIgnoreCase(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE) && shipTosSize>0) {
			setCustomerIdSetNew();
		}else{ // else MSAP or SAP is selected for expanding
			setCustomerIdSet();
		}
		if(customerIdSet.size()>0) {
			ArrayList<String> customerIdList = new ArrayList<String>(customerIdSet);
			setSuffixAndShipDivMaps(customerIdList);
			// A method to set the customer path map is to be written.
			setChildCustomerPaths(customerIdList);
		}
	}
	
	
	
	private void setSuffixAndShipDivMaps(ArrayList<String> customerIdList) {
		int totalShipsTos = 0;
		if(customerIdList.size()>0) {
			Document customerInfoDoc = null;
			try {
				customerInfoDoc = XPEDXWCUtils.getCustomerDetails(customerIdList, wcContext.getStorefrontId(), "xpedx-customer-getCustomListInformationForMILShareList");
				if(LOG.isDebugEnabled()){
					LOG.debug(SCXmlUtil.getString(customerInfoDoc));
				}
				if(customerInfoDoc!=null){
					if(custSuffixType!=null && custSuffixType.trim().length()>0 && custSuffixType.equalsIgnoreCase(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE)) {
						Document createdDoc = SCXmlUtil.createDocument("CustomerList");
						Element createdElem = createdDoc.createElement("Customer");
						createdElem.setAttribute("CustomerID",customerId);
						createdElem.setAttribute("OrganizationCode", wcContext.getStorefrontId());
						
						//For creating Customer List tag with totalnumberofRecords as a attribute 
						//For e.g : <CustomerList TotalNumberOfRecords="1">
						Element createdElem1 = createdDoc.createElement("CustomerList");
						createdElem1.setAttribute("TotalNumberOfRecords", totalNumOfRecords);
						
						/*if(totalNumOfRecords!=null && totalNumOfRecords.trim().length()>0)
						{
							totalShipsTos = Integer.parseInt(totalNumOfRecords)-1;
							customerInfoDoc.getDocumentElement().setAttribute("TotalNumberOfRecords", String.valueOf(totalShipsTos));
							//createdDoc.getDocumentElement().setAttribute("TotalNumberOfRecords", String.valueOf(totalShipsTos));
						}
						else
						{*/
							customerInfoDoc.getDocumentElement().setAttribute("TotalNumberOfRecords", totalNumOfRecords);
						//}
						Element customerListAll = customerInfoDoc.getDocumentElement();
						Iterator<Element> customerIterator = SCXmlUtil.getChildren(customerListAll);
						while(customerIterator.hasNext()) {
							Element customer = customerIterator.next();
							String CustomerIdNew = customer.getAttribute("CustomerID");
							if(CustomerIdNew!= null && !CustomerIdNew.trim().equalsIgnoreCase(customerId)){								
								if(custSuffixType!=null && custSuffixType.trim().length()>0 && custSuffixType.equalsIgnoreCase(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE)) {									
									SCXmlUtil.importElement(createdElem1, customer);
								
								/*	Element createdElemCustomerList = createdDoc.createElement("CustomerList");
									createdElem.appendChild(createdElemCustomerList);
									//createdElemCustomerList.setAttribute("TotalNumberOfRecords", String.valueOf(totalShipsTos));
									createdElemCustomerList.setAttribute("TotalNumberOfRecords", totalNumOfRecords);	*/								
									//SCXmlUtil.importElement(createdElem, createdElemCustomerList);	
								}
								
								
														
							}
						}
						
						SCXmlUtil.importElement(createdElem, createdElem1);						
						SCXmlUtil.importElement(createdDoc.getDocumentElement(), createdElem);
						if(LOG.isDebugEnabled()){
						LOG.debug(SCXmlUtil.getString(createdElem));  
						LOG.debug(SCXmlUtil.getString(createdDoc));
						}
						outDoc = createdDoc;
					}
					displayCustomerMap = XPEDXWCUtils.custFullAddresses(customerInfoDoc);
					Element customerList = customerInfoDoc.getDocumentElement();
					Iterator<Element> customerIterator = SCXmlUtil.getChildren(customerList);
					while(customerIterator.hasNext()) {
						Element customer = customerIterator.next();
						String CustomerIdNew = customer.getAttribute("CustomerID");
						Element Extn = SCXmlUtil.getChildElement(customer, "Extn");
						if(!SCUtil.isVoid(Extn)){
							String customerSuffixType = SCXmlUtil.getXpathAttribute(customer, "Extn/@ExtnSuffixType");
							String customerShipFromDivision = SCXmlUtil.getXpathAttribute(customer, "Extn/@ExtnShipFromBranch");
							suffixTypeMap.put(CustomerIdNew, customerSuffixType);
							shipFromDivisionMap.put(CustomerIdNew, customerShipFromDivision);
						}
					}
				}
			} catch (CannotBuildInputException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void setChildCustomerPaths(ArrayList<String> customerIdList) {
		if(customerIdList.size()>0) {
			try {
				String parentCustomerPath = XPEDXMyItemsUtils.getCustomerPathAsHRY(getWCContext().getSCUIContext(), customerId, getWCContext().getStorefrontId());
				if(!SCUtil.isVoid(parentCustomerPath)) {
					customerPathMap.put(customerId, parentCustomerPath);
					Iterator<String> childCustomerIterator = childCustomerIdSet.iterator();
					while(childCustomerIterator.hasNext()){
						String childCustomerID = childCustomerIterator.next();
						String childCustomerPath = parentCustomerPath+"|"+childCustomerID;
						customerPathMap.put(childCustomerID, childCustomerPath);
					}
				}
			} catch (Exception e) {
				LOG.error("Error getting the Customer Hierarchy Path for the Parent Customer with CustomerID"+ customerId);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	
	private void setCustomerIdSet() {
		ArrayList<String> customerIDs = new ArrayList<String>();
		Element immediateChildsDocElem = getOutDoc().getDocumentElement();
		ArrayList<Element> parentElems = SCXmlUtil.getElements(immediateChildsDocElem, "Customer");
		Iterator<Element> iterator = parentElems.iterator();
		while(iterator.hasNext()) {
			Element parentElem = iterator.next();
			String parentCusotmerID = parentElem.getAttribute("CustomerID");
			Element childListElem = SCXmlUtil.getChildElement(parentElem, "CustomerList");
			ArrayList<Element> immediateChilds = SCXmlUtil.getElements(childListElem, "Customer");
			Iterator<Element> immediateChildIterotor = immediateChilds.iterator();
			while(immediateChildIterotor.hasNext()) {
				Element  childElem = immediateChildIterotor.next();
				String customerID = childElem.getAttribute("CustomerID");
				customerIdSet.add(customerID);
				childCustomerIdSet.add(customerID);
			}
			customerIdSet.add(parentCusotmerID);
		}
		if(customerIdSet.size()>0 && customerIdSet!=null) {
			customerIDs.addAll(customerIdSet);
		}
		
	}

// Added for JIRA 3589 - When SUffix type is B that is Bill to, then we need list of SHip-Tos	
	private void setCustomerIdSetNew() {
		ArrayList<String> customerIDs = new ArrayList<String>();
		Element immediateChildsDocElem = getOutDoc().getDocumentElement();
		ArrayList<Element> parentElems = SCXmlUtil.getElements(immediateChildsDocElem, "XPXCustHierarchyView");
		Iterator<Element> iterator = parentElems.iterator();
		
		while(iterator.hasNext()) {
			Element parentElem = iterator.next();
			parentCusotmerID = parentElem.getAttribute("BillToCustomerID");
			SCXmlUtil.getString(parentElem);
			String customerID = parentElem.getAttribute("ShipToCustomerID");
			customerIdSet.add(customerID);
			childCustomerIdSet.add(customerID);			
		}
		customerIdSet.add(parentCusotmerID);
		if(customerIdSet.size()>0 && customerIdSet!=null) {
			customerIDs.addAll(customerIdSet);
		}
		
	}



	public Document getOutDoc() {
		return outDoc;
	}

	public void setOutDoc(Document outDoc) {
		this.outDoc = outDoc;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String generateRamdomId() {
		return UUID.randomUUID().toString().replace("-", "_");
	}

	public String getSfId() {
		return sfId;
	}

	public void setSfId(String sfId) {
		this.sfId = sfId;
	}

	public String getShowRoot() {
		return showRoot;
	}

	public void setShowRoot(String x) {
		showRoot = x;
	}

	public String getEditMode() {
		return editMode;
	}

	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}



	public String getListDesc() {
		return listDesc;
	}



	public void setListDesc(String listDesc) {
		this.listDesc = listDesc;
	}



	public Map<String, String> getDisplayCustomerMap() {
		return displayCustomerMap;
	}



	public void setDisplayCustomerMap(Map<String, String> displayCustomerMap) {
		this.displayCustomerMap = displayCustomerMap;
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

	public Integer getTotalNumberOfPages() {
		return totalNumberOfPages;
	}

	public void setTotalNumberOfPages(Integer totalNumberOfPages) {
		this.totalNumberOfPages = totalNumberOfPages;
	}

	public HashMap<String, String> getSuffixTypeMap() {
		return suffixTypeMap;
	}

	public void setSuffixTypeMap(HashMap<String, String> suffixTypeMap) {
		this.suffixTypeMap = suffixTypeMap;
	}

	public HashMap<String, String> getShipFromDivisionMap() {
		return shipFromDivisionMap;
	}

	public void setShipFromDivisionMap(HashMap<String, String> shipFromDivisionMap) {
		this.shipFromDivisionMap = shipFromDivisionMap;
	}

	public HashMap<String, String> getCustomerPathMap() {
		return customerPathMap;
	}

	public void setCustomerPathMap(HashMap<String, String> customerPathMap) {
		this.customerPathMap = customerPathMap;
	}
	/**
	 * @return the countCustomer
	 */
	public String getCountCustomer() {
		return countCustomer;
	}

	/**
	 * @param countCustomer the countCustomer to set
	 */
	public void setCountCustomer(String countCustomer) {
		this.countCustomer = countCustomer;
	}

}
