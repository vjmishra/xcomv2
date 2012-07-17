package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.dom.YFCDocument;

@SuppressWarnings("serial")
public class XPEDXMyItemsListAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsListAction.class);
	private Document outDoc;
	private String userHRY			= "";
	private ArrayList<String> sharedListOrValues;
	private ArrayList<String> sharedListOrNames;
	private String sharePrivate     = "";
	private List<Element> listOfItems;
	private String customerId = "";
	private String[] customerPaths;
	private String[] customerIds;
	private String[] customerDivs;
	private String ShareList=null;
	protected Integer pageNumber = 1;
	//protected String pageNumber = "1";
    private Boolean isFirstPage = Boolean.FALSE;
    private Boolean isLastPage = Boolean.FALSE;
    private Boolean isValidPage = Boolean.FALSE;
    private Integer totalNumberOfPages = new Integer(1);
    protected Integer pageSize = 25;
    protected String orderByAttribute ="ListName";//Changed from modifyTS to Listname for JIRA 2982
    protected String orderDesc="N";//For Alphabetical sort for JIRA 2982
    private String modifyts;
    private String createUserId;
    private String modifyUserid;
    
    
	
	public String getModifyts() {
		return modifyts;
	}


	public void setModifyts(String modifyts) {
		this.modifyts = modifyts;
	}


	public String getCreateUserId() {
		return createUserId;
	}


	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}


	public String getModifyUserid() {
		return modifyUserid;
	}


	public void setModifyUserid(String modifyUserid) {
		this.modifyUserid = modifyUserid;
	}
	public String getOrderDesc() {
		return orderDesc;
	}


	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}


	public String getOrderByAttribute() {
		return orderByAttribute;
	}


	public void setOrderByAttribute(String orderByAttribute) {
		this.orderByAttribute = orderByAttribute;
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


	public Integer getTotalNumberOfPages() {
		return totalNumberOfPages;
	}


	public void setTotalNumberOfPages(Integer totalNumberOfPages) {
		this.totalNumberOfPages = totalNumberOfPages;
	}

	
	public String getShareList() {
		return ShareList;
	}


	public void setShareList(String shareList) {
		ShareList = shareList;
	}


	/**
	 * @return the customerPaths
	 */
	public String[] getCustomerPaths() {
		return customerPaths;
	}


	/**
	 * @param customerPaths the customerPaths to set
	 */
	public void setCustomerPaths(String[] customerPaths) {
		this.customerPaths = customerPaths;
	}


	/**
	 * @return the customerIds
	 */
	public String[] getCustomerIds() {
		return customerIds;
	}


	/**
	 * @param customerIds the customerIds to set
	 */
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}


	/**
	 * @return the customerDivs
	 */
	public String[] getCustomerDivs() {
		return customerDivs;
	}


	/**
	 * @param customerDivs the customerDivs to set
	 */
	public void setCustomerDivs(String[] customerDivs) {
		this.customerDivs = customerDivs;
	}

	private HashMap<String, HashMap<String,String>> assignedCustomersMap;
	private HashMap<String, String> listSizeMap = new HashMap<String, String>();
	private HashMap<String, String> listModifiedByMap = new HashMap<String, String>();
	
	public HashMap<String, String> getListSizeMap() {
		return listSizeMap;
	}


	public void setListSizeMap(HashMap<String, String> listSizeMap) {
		this.listSizeMap = listSizeMap;
	}

	private boolean filterByMyListChk 	= false;
	private String 	uniqueId 			= ""; 
	
	public static final String COMMAND_COPY_LIST 	= "copy_list";
	public static final String COMMAND_VIEW_LIST 	= "view_list";
	private String command = COMMAND_VIEW_LIST;
	private boolean displayAsSubMenu 		= false;
	private boolean displayAsDropdownList 	= false;
	private boolean displayAsRadioButton 	= false;
	private boolean canAddItem 		= true;
	private boolean canEditItem		= true;
	private boolean canSaveItem		= true;
	private boolean canDeleteItem	= true;
	private boolean canShare		= false;
	
	private String orderByLastModified = "asc";
	
	private String sharePermissionLevel = "";
	private String rbPermissionShared = "";
	private String rbPermissionPrivate = "";
	
	private boolean filterBySelectedListChk = false;
	private boolean filterByAllChk = false;
	private boolean filterBySharedLocations = false;
	private boolean deleteClicked = false;
	
	
	
	/**
	 * This method is used to get the Shared List or Names of the default shipTo or 
	 * while getting the lists of different locations
	 */
	private void generateSelectedLists(){
		try {
			
			setSharedListOrValues(new ArrayList<String>());
			setSharedListOrNames(new ArrayList<String>());
			if(ShareList!=null )
			{    
				if(ShareList.length()>0)
			     {
				filterByAllChk=true;
			     }
			}
			if ((!getFilterBySelectedListChk())){
				if(deleteClicked && getCustomerIds()!=null && getCustomerIds().length>0){// in case of delete, the value comes as comma seperated customerids
					for(int i=0; i< getCustomerIds().length; ){// there is only one value with comma separated customerIds
						customerId = getCustomerIds()[i];
						if(!YFCUtils.isVoid(customerId)){
							setCustomerIds(customerId.split(","));
						}else{
							setCustomerIds(null);
						}
						break;
					}
				}
				String[] customerList = getCustomerIds();
				/*
				 * If this customerPath is also included in the Query, Then it fetches all the shared lists shared with 
				 * all the customers below that particular customer. Say if Bill1 is selected, then if customer paths is included in the Query
				 * it fetches all the shared lists which are shared with all the ship to's under Bill1 also.
				 * Right Now When Bill1 is selected. It s
				 * if(customerPaths!=null && customerPaths.length>0) {
					for (int i=0; i< customerPaths.length; i++) {// admin
						String customerPath = customerPaths[i];
						getSharedListOrNames().add("CustomerPath");
						getSharedListOrValues().add(customerPath);
					}
				}*/
				if((ShareList!=null || customerList!=null) && customerPaths!=null)
				{
					if(customerList!=null){
						customerList = getAssignedCustomers(customerList);
						for (int i=0; i< customerList.length; i++) {
							String customerId = customerList[i];
							getSharedListOrNames().add("CustomerID");
							getSharedListOrValues().add(customerId);
						}
					}else{// buyer user customerIds are null
						getSharedListOrNames().add("CustomerID");
						getSharedListOrValues().add(getWCContext().getCustomerId());
					}
				}
				else
				{
					String customerPathOfCurrentShipTo = XPEDXMyItemsUtils.getCustomerPathAsHRY(getWCContext().getSCUIContext(), getWCContext().getCustomerId(), getWCContext().getStorefrontId());
					customerList = new String[] {getWCContext().getCustomerId()};
					if(customerPathOfCurrentShipTo!=null)
						customerList = customerPathOfCurrentShipTo.split("\\|");
					
					if(customerList!=null){
							for (int i=0; i< customerList.length; i++) {
								String customerId = customerList[i];
								getSharedListOrNames().add("CustomerID");
								getSharedListOrValues().add(customerId);
							}
						}
				}
			}else if(getFilterByAllChk()){// on landing page - home page
				
				/*getSharedListOrNames().add("CustomerID");
				getSharedListOrValues().add(getWCContext().getCustomerId());*/
				
				//Fetches all the assigned customers along with the ship-to
				//and gets the MILs shared with these customers. 
				String customerPathOfCurrentShipTo = XPEDXMyItemsUtils.getCustomerPathAsHRY(getWCContext().getSCUIContext(), getWCContext().getCustomerId(), getWCContext().getStorefrontId());
				String[] customerList = new String[] {getWCContext().getCustomerId()};
				if(customerPathOfCurrentShipTo!=null)
					customerList = customerPathOfCurrentShipTo.split("\\|");
				
				if(customerList!=null){
					for (int i=0; i< customerList.length; i++) {// admin
						String customerId = customerList[i];
						getSharedListOrNames().add("CustomerID");
						getSharedListOrValues().add(customerId);
					}
				}
			}
			
			//Add the current user as owner
			getSharedListOrNames().add("Createuserid");
			getSharedListOrValues().add(XPEDXMyItemsUtils.getCurrentCustomerId(getWCContext()));
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}
	private void generateSelectedLists(boolean flag){
		try {
			
			setSharedListOrValues(new ArrayList<String>());
			setSharedListOrNames(new ArrayList<String>());
			if(ShareList!=null )
			{    
				if(ShareList.length()>0)
			     {
				filterByAllChk=true;
			     }
			}
			if (flag){
				if(deleteClicked && getCustomerIds()!=null && getCustomerIds().length>0){// in case of delete, the value comes as comma seperated customerids
					for(int i=0; i< getCustomerIds().length; ){// there is only one value with comma separated customerIds
						customerId = getCustomerIds()[i];
						if(!YFCUtils.isVoid(customerId)){
							setCustomerIds(customerId.split(","));
						}else{
							setCustomerIds(null);
						}
						break;
					}
				}
				String[] customerList = getCustomerIds();
				/*
				 * If this customerPath is also included in the Query, Then it fetches all the shared lists shared with 
				 * all the customers below that particular customer. Say if Bill1 is selected, then if customer paths is included in the Query
				 * it fetches all the shared lists which are shared with all the ship to's under Bill1 also.
				 * Right Now When Bill1 is selected. It s
				 * if(customerPaths!=null && customerPaths.length>0) {
					for (int i=0; i< customerPaths.length; i++) {// admin
						String customerPath = customerPaths[i];
						getSharedListOrNames().add("CustomerPath");
						getSharedListOrValues().add(customerPath);
					}
				}*/
				if((ShareList!=null || customerList!=null) && customerPaths!=null)
				{
					if(customerList!=null){
						customerList = getAssignedCustomers(customerList);
						for (int i=0; i< customerList.length; i++) {// admin
							String customerId = customerList[i];
							getSharedListOrNames().add("CustomerID");
							getSharedListOrValues().add(customerId);
						}
					}else{// buyer user customerIds are null
						getSharedListOrNames().add("CustomerID");
						getSharedListOrValues().add(getWCContext().getCustomerId());
					}
				}
				else
				{
					String customerPathOfCurrentShipTo = XPEDXMyItemsUtils.getCustomerPathAsHRY(getWCContext().getSCUIContext(), getWCContext().getCustomerId(), getWCContext().getStorefrontId());
					customerList = new String[] {getWCContext().getCustomerId()};
					if(customerPathOfCurrentShipTo!=null)
						customerList = customerPathOfCurrentShipTo.split("\\|");
					
					if(customerList!=null){
							for (int i=0; i< customerList.length; i++) {// admin
								String customerId = customerList[i];
								getSharedListOrNames().add("CustomerID");
								getSharedListOrValues().add(customerId);
							}
						}
				}
			}else if(getFilterByAllChk()){// on landing page - home page
				
				/*getSharedListOrNames().add("CustomerID");
				getSharedListOrValues().add(getWCContext().getCustomerId());*/
				
				//Fetches all the assigned customers along with the ship-to
				//and gets the MILs shared with these customers. 
				String customerPathOfCurrentShipTo = XPEDXMyItemsUtils.getCustomerPathAsHRY(getWCContext().getSCUIContext(), getWCContext().getCustomerId(), getWCContext().getStorefrontId());
				String[] customerList = new String[] {getWCContext().getCustomerId()};
				if(customerPathOfCurrentShipTo!=null)
					customerList = customerPathOfCurrentShipTo.split("\\|");
				
				if(customerList!=null){
					for (int i=0; i< customerList.length; i++) {// admin
						String customerId = customerList[i];
						getSharedListOrNames().add("CustomerID");
						getSharedListOrValues().add(customerId);
					}
				}
			}
			
			//Add the current user as owner
			getSharedListOrNames().add("Createuserid");
			getSharedListOrValues().add(XPEDXMyItemsUtils.getCurrentCustomerId(getWCContext()));
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}

	/**
	 * This method gets the assigned customers
	 * @param customerList
	 * @return
	 */
	private String[] getAssignedCustomers(String[] customerList) {
		HashMap<String, ArrayList<String>> customerAssignmentsMap;
		try {
			
			String[] customersFromCustomerpaths;
			Set<String> both = new HashSet<String>();
			for(int i=0; getCustomerPaths()!=null && i< getCustomerPaths().length; i++){
				customersFromCustomerpaths = getCustomerPaths()[i].split("\\|");
				if(customersFromCustomerpaths!=null && customersFromCustomerpaths.length>0){
					//return customerList;
				    Collections.addAll(both, customersFromCustomerpaths);
				}
			}
			
			if(customerList.length>0){
				Collections.addAll(both, customerList);
			}
			return both.toArray(new String[both.size()]);
			
			/*customerAssignmentsMap = XPEDXWCUtils.getAssignedCustomersMap(
					XPEDXMyItemsUtils.getCurrentCustomerId(getWCContext()) , 
					getWCContext().getLoggedInUserId());
		
			Set<String> keys = customerAssignmentsMap.keySet();
			HashSet<String> customerSet = new HashSet<String>();// using set to make sure no duplicates are present
			if(keys!=null && !keys.isEmpty())
			{
				Iterator<String> keyIter = keys.iterator();
				while(keyIter.hasNext())
				{
					String currentKey = keyIter.next();
					ArrayList<String> customerIdList = customerAssignmentsMap.get(currentKey);
					//getAssignedCustomersMap().put(currentKey, XPEDXWCUtils.getHashMapFromList(customerIdList));
					for(String customerid : customerIdList){
						if(!YFCUtils.isVoid(customerid))
							customerSet.add(customerid);
					}
				}
			}
			
			for(String selectedCustomerId : customerList){
				if(!YFCUtils.isVoid(selectedCustomerId))
					customerSet.add(selectedCustomerId);
			}

			customerList = (String[]) customerSet.toArray(new String[customerSet.size()]);
*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return customerList;
	}


	public String getListKey(){
		String res = "";
		try {
			if (request.getParameter("listKey") !=null){
				res = request.getParameter("listKey");
			}
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
		
		return res;
	}
	
	/*private void setModifyMap() {
		Set<String> userNames = new HashSet<String>();
		if(listModifiedByMap!=null && listModifiedByMap.size()>0) {
			userNames.addAll(listModifiedByMap.values());
		}
		if(userNames.size()>0) {
			try {
				Element input = WCMashupHelper.getMashupInput("xpedxGetContactUserName", getWCContext());
				input.setAttribute("OrganizationCode", getWCContext().getStorefrontId());
				Element complexQuery = SCXmlUtil.getChildElement(input, "ComplexQuery");
				Element OrElem = SCXmlUtil.getChildElement(complexQuery, "Or");
				for(String userId : userNames) {
					Element exp = input.getOwnerDocument().createElement("Exp");
					exp.setAttribute("Name", "CustomerContactID");
					exp.setAttribute("Value", userId);
					SCXmlUtil.importElement(OrElem, exp);
				}
				System.out.println(SCXmlUtil.getString(input));
				Element output =(Element) WCMashupHelper.invokeMashup("xpedxGetContactUserName", input, getWCContext().getSCUIContext());
				System.out.println(SCXmlUtil.getString(output));
				if(output!=null){
					for(String listKey : listModifiedByMap.keySet()) {
						String modifyBy = listModifiedByMap.get(listKey);
						ArrayList<Element> modifyByElements = SCXmlUtil.getElementsByAttribute(output, "/CustomerContact", "CustomerContactID", modifyBy);
						Element modifyByElement = null;
						for(Element elem : modifyByElements) {
							String userId = elem.getAttribute("UserID");
							if(userId!=null && userId.equals(modifyBy)){
								modifyByElement = elem;
								break;
							}
						}
						if(modifyByElement!=null) {
							String name = "";
							String firstname = modifyByElement.getAttribute("FirstName");
							String lastname = modifyByElement.getAttribute("LastName");
							if(firstname!=null || lastname!=null)
								name = lastname +","+" "+firstname;
							else
								name = modifyBy;
							listModifiedByMap.put(listKey, name);
						}
					}
				}
			} catch (CannotBuildInputException e) {
				e.printStackTrace();
				LOG.error("Error fetching the Modify names for the list in method setModifyMap in action MyItemsListAction ");
			}
		}
	}
	*/
	@SuppressWarnings("unchecked")
	@Override
	public String execute() {
		try {
			Map<String, Element> out;
			
			//set some vars
			setCustomerId(XPEDXMyItemsUtils.getCurrentCustomerId(getWCContext()));
			
			//Generate the shared list "or" values
			setSharePrivate(getWCContext().getLoggedInUserId());
//			setUserHRY(XPEDXMyItemsUtils.getCustomerPathAsHRY(getWCContext()));// This is same as getting path for the Logged in customer which is MSAP
			setUserHRY(XPEDXMyItemsUtils.getCurrentCustomerId(getWCContext()));

			if(ShareList!=null )
			{  if(ShareList.length()>0)
			     {
					filterByMyListChk=false;
			     }
			}
			/**
			 * Changes Start for CR 2774 
			 * According to CR 2774 -Showing both persional and share records by default
			 * 
			 * */
				//generateSelectedLists();
				generateSelectedLists(true);
				
				Element inXML = formInputXML();
				
				Object obj = WCMashupHelper.invokeMashup(
						"XPEDXLandingMyItemsList", inXML, getWCContext()
								.getSCUIContext());
				outDoc = ((Element) obj).getOwnerDocument();
				parsePageInfo(outDoc.getDocumentElement(), true);
				
				List<Element> itemLists = SCXmlUtil.getElements(outDoc.getDocumentElement(), "/Output/XPEDXLandingMILList/XPEDXLandingMIL");
				setListOfItems(itemLists);
				for(Element elem : itemLists){
					//List<Element> items = getXMLUtils().getElements(elem, "XPEDXMyItemsItemsList");
					String itemCount="0";
					String modifyUserId = elem.getAttribute("UserName");
					this.modifyUserid=elem.getAttribute("Modifyuserid");
					this.createUserId=elem.getAttribute("Createuserid");
					this.modifyts=elem.getAttribute("Modifyts");
					String listKey = elem.getAttribute("MyItemsListKey");					
					//Element itemElem=items.get(0);
					itemCount=elem.getAttribute("NumberOFItems");
					if(itemCount.trim().equals("0.00")){
						itemCount = "0";
					}else{
					/*StringTokenizer st = new StringTokenizer(itemCount, ".00"); 
                    while(st.hasMoreTokens()){
                    	itemCount = st.nextToken();
                    }*/
						String splited[]=itemCount.split("\\.00");						
						if(splited[0] != null)
						itemCount=splited[0];

                    
                    }
					listSizeMap.put(listKey, itemCount);
					listModifiedByMap.put(listKey, modifyUserId);
				}
			
			
			if (filterByMyListChk || getFilterByAllChk()){
				Element resultsPrivate = prepareAndInvokeMashup("XPEDXMyItemsListPrivate");
				ArrayList<Element> tmpList = getXMLUtils().getElements(resultsPrivate, "XPEDXMyItemsList");
				if(getListOfItems()!=null){
					for (Iterator iterator = tmpList.iterator(); iterator.hasNext();) {
						Element tmpRecord = (Element) iterator.next();
						getListOfItems().add(tmpRecord);
					}
				}
				else setListOfItems(tmpList);
				for(Element elem : tmpList){
					List<Element> items = getXMLUtils().getElements(elem, "XPEDXMyItemsItemsList");
					String itemCount="0";
					String modifyUserId = elem.getAttribute("Modifyuserid");
					String listKey = elem.getAttribute("MyItemsListKey");
					if(items != null && items.size()>0){
						Element itemElem=items.get(0);
						itemCount=itemElem.getAttribute("TotalNumberOfRecords");
					}
					listSizeMap.put(elem.getAttribute("MyItemsListKey"), itemCount);
					listModifiedByMap.put(listKey, modifyUserId);
				}
			}
			
			//Sorting
			boolean asc = true;
			/*if (getOrderByLastModified().equals("desc")){ asc = false; }
			Collections.sort(listOfItems, new XPEDXMILSortByLastModified(asc));*/
			
			//setModifyMap();
			
			processPermissions();
			Set set = new HashSet();
			List newList = new ArrayList();
			
			for(Element elem : listOfItems){
				String MyItemsListKey = elem.getAttribute("MyItemsListKey");
				if (set.add(MyItemsListKey))
				      newList.add(elem);
				    }			
			 listOfItems.clear();
			 listOfItems.addAll(newList);
			if (displayAsSubMenu){
				return "displayAsSubMenu";
			}
			if (displayAsDropdownList){
				return "displayAsDropdownList";
			}
			if (displayAsRadioButton){	
				return "displayAsRadioButton";
			}
			
			//Get the list of customers and bills tos
			
			/*try {
				
				setAssignedCustomersMap(
					XPEDXWCUtils.getAssignedCustomersHashMap(
						XPEDXMyItemsUtils.getCurrentCustomerId(getWCContext()) , 
						getWCContext().getLoggedInUserId()
					)
				);
				
				HashMap<String, ArrayList<String>> customerAssignmentsMap = XPEDXWCUtils.getAssignedCustomersMap(
						XPEDXMyItemsUtils.getCurrentCustomerId(getWCContext()) , 
						getWCContext().getLoggedInUserId());
				Set<String> keys = customerAssignmentsMap.keySet();
				if(keys!=null && !keys.isEmpty())
				{
					Iterator<String> keyIter = keys.iterator();
					while(keyIter.hasNext())
					{
						String currentKey = keyIter.next();
						ArrayList<String> customerIdList = customerAssignmentsMap.get(currentKey);
						getAssignedCustomersMap().put(currentKey, XPEDXWCUtils.getHashMapFromList(customerIdList));
					}
				}
				
			} catch (Exception e) {
				LOG.error(e);
			}*/
			
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			return ERROR;
		}
		
		return SUCCESS;
	}

	private void processPermissions(){
		try {
			
			//New rules
			if (isCurrentUserAdmin()){
				canShare = true;
			}
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}
	
	private boolean isCurrentUserAdmin(){
		boolean res = false;
		try {
			if	(ResourceAccessAuthorizer.getInstance().isAuthorized(
					"/swc/profile/ManageUserList", getWCContext())
			) {
	              res = true;
	        } else {
	              res = false;
	        }
		} catch (Exception e) {
			LOG.error(e.toString());
		}
		
		return res;
	}
	
	public Document getOutDoc() {
		return outDoc;
	}

	public void setOutDoc(Document outDoc) {
		this.outDoc = outDoc;
	}


	public String getUserHRY() {
		return userHRY;
	}


	public void setUserHRY(String userHRY) {
		this.userHRY = userHRY;
	}


	public ArrayList<String> getSharedListOrValues() {
		return sharedListOrValues;
	}


	public void setSharedListOrValues(ArrayList<String> sharedListOrValues) {
		this.sharedListOrValues = sharedListOrValues;
	}


	public ArrayList<String> getSharedListOrNames() {
		return sharedListOrNames;
	}


	public void setSharedListOrNames(ArrayList<String> sharedListOrNames) {
		this.sharedListOrNames = sharedListOrNames;
	}


	public String getSharePrivate() {
		return sharePrivate;
	}


	public void setSharePrivate(String sharePrivate) {
		this.sharePrivate = sharePrivate;
	}


	public List<Element> getListOfItems() {
		return listOfItems;
	}


	public void setListOfItems(List<Element> listOfItems) {
		
		try {
			if (displayAsDropdownList){
				Element root = (Element)getOutDoc().createElement("root");
				root.setAttribute("Name", "Create new list");
				root.setAttribute("MyItemsListKey", "root");
				listOfItems.add(0, root);
			}
		} catch (Exception e) {
		}
		
		this.listOfItems = listOfItems;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public HashMap<String, HashMap<String,String>> getAssignedCustomersMap() {
		if (assignedCustomersMap == null){
			assignedCustomersMap = new HashMap();
		}
		return assignedCustomersMap;
	}


	public void setAssignedCustomersMap(
			HashMap<String, HashMap<String,String>> assignedCustomersMap) {
		this.assignedCustomersMap = assignedCustomersMap;
	}

	public boolean getFilterByMyListChk() {
		return filterByMyListChk;
	}


	public void setFilterByMyListChk(boolean filterByMyListChk) {
		this.filterByMyListChk = filterByMyListChk;
	}


	public String getUniqueId() {
		uniqueId = System.currentTimeMillis() + "";
		return uniqueId;
	}


	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}


	public boolean getDisplayAsSubMenu() {
		return displayAsSubMenu;
	}


	public void setDisplayAsSubMenu(boolean displayAsSubMenu) {
		this.displayAsSubMenu = displayAsSubMenu;
	}


	public boolean getDisplayAsDropdownList() {
		return displayAsDropdownList;
	}


	public void setDisplayAsDropdownList(boolean displayAsDropdownList) {
		this.displayAsDropdownList = displayAsDropdownList;
	}

	
	public boolean getDisplayAsRadioButton() {
		return displayAsRadioButton;
	}


	public void setDisplayAsRadioButton(boolean displayAsRadioButton) {
		this.displayAsRadioButton = displayAsRadioButton;
	}


	public boolean getCanAddItem() {
		return canAddItem;
	}


	public void setCanAddItem(boolean canAddItem) {
		this.canAddItem = canAddItem;
	}


	public boolean getCanEditItem() {
		return canEditItem;
	}


	public void setCanEditItem(boolean canEditItem) {
		this.canEditItem = canEditItem;
	}


	public boolean getCanSaveItem() {
		return canSaveItem;
	}


	public void setCanSaveItem(boolean canSaveItem) {
		this.canSaveItem = canSaveItem;
	}


	public boolean getCanDeleteItem() {
		return canDeleteItem;
	}


	public void setCanDeleteItem(boolean canDeleteItem) {
		this.canDeleteItem = canDeleteItem;
	}


	public boolean getCanShare() {
		return canShare;
	}


	public void setCanShare(boolean canShare) {
		this.canShare = canShare;
	}

	public String getOrderByLastModified() {
		return orderByLastModified;
	}


	public void setOrderByLastModified(String orderByLastModified) {
		this.orderByLastModified = orderByLastModified;
	}	
	private Element formInputXML() throws CannotBuildInputException
	{
		Element input = WCMashupHelper.getMashupInput(
				"XPEDXLandingMyItemsList", getWCContext()
						.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		Document inputDoc = input.getOwnerDocument();
		
		//NodeList milNodeList = input.getElementsByTagName("XPEDXMyItemsList");
		//Element milNodeListEle = (Element)milNodeList.item(0);
		
		Element milNodeListEle = inputDoc.getDocumentElement();
//		milNodeListEle.setAttribute("SharePrivate", getSharePrivate());
		
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element) inputNodeList.item(0);
		Element expElement = null;
		for (int i = 0; i < sharedListOrValues.size(); i++) {
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", sharedListOrNames.get(i));
			expElement
					.setAttribute("Value", sharedListOrValues.get(i));
			/*
			 * Removing this as we are not supposed to fetch the 
			 * Lists shared with the childs of the selected customer
			 * if(sharedListOrNames.get(i).equals("CustomerPath")){
				expElement.setAttribute("QryType", "FLIKE");
			}*/

			inputNodeListElemt.appendChild(inputDoc
					.importNode(expElement, true));
		}
		Document expDoc1 = YFCDocument.createDocument("Exp").getDocument();
		Element orderByElem=SCXmlUtil.createChild((Element)input.getElementsByTagName("XPEDXLandingMil").item(0), "OrderBy");
		Element attribElem=SCXmlUtil.createChild(orderByElem, "Attribute");
		attribElem.setAttribute("Desc",orderDesc);
		attribElem.setAttribute("Name",orderByAttribute);
		Element expElement1 = expDoc1.getDocumentElement();
		expElement1.setAttribute("Name","SharePrivate");
		expElement1.setAttribute("Value",getSharePrivate());
		inputNodeListElemt.appendChild(inputDoc
				.importNode(expElement1, true));
	
		input.setAttribute("PageNumber", getPageNumber().toString());
		input.setAttribute("PageSize", getPageSize().toString());
		input.setAttribute("PageSetToken", getPageSetToken());
		/* HashMap<String,String> valueMap = new HashMap<String, String>();
		valueMap.put("/Page/@PageNumber", "1");
		valueMap.put("/Page/@PageSize", "15");
		valueMap.put("input",input.toString());*/
		
		
		inputXml = SCXmlUtil.getString(input);
		LOG.debug("Input XML: " + inputXml);
		
		return input;
		
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


	/**
	 * @return the sharePermissionLevel
	 */
	public String getSharePermissionLevel() {
		return sharePermissionLevel;
	}


	/**
	 * @param sharePermissionLevel the sharePermissionLevel to set
	 */
	public void setSharePermissionLevel(String sharePermissionLevel) {
		this.sharePermissionLevel = sharePermissionLevel;
	}


	/**
	 * @return the rbPermissionShared
	 */
	public String getRbPermissionShared() {
		return rbPermissionShared;
	}


	/**
	 * @param rbPermissionShared the rbPermissionShared to set
	 */
	public void setRbPermissionShared(String rbPermissionShared) {
		this.rbPermissionShared = rbPermissionShared;
	}


	/**
	 * @return the rbPermissionPrivate
	 */
	public String getRbPermissionPrivate() {
		return rbPermissionPrivate;
	}


	/**
	 * @param rbPermissionPrivate the rbPermissionPrivate to set
	 */
	public void setRbPermissionPrivate(String rbPermissionPrivate) {
		this.rbPermissionPrivate = rbPermissionPrivate;
	}


	/**
	 * @param filterBySelectedListChk the filterBySelectedListChk to set
	 */
	public void setFilterBySelectedListChk(boolean filterBySelectedListChk) {
		this.filterBySelectedListChk = filterBySelectedListChk;
	}


	/**
	 * @return the filterBySelectedListChk
	 */
	public boolean getFilterBySelectedListChk() {
		return filterBySelectedListChk;
	}


	/**
	 * @param filterByAllChk the filterByAllChk to set
	 */
	public void setFilterByAllChk(boolean filterByAllChk) {
		this.filterByAllChk = filterByAllChk;
	}


	/**
	 * @return the filterByAllChk
	 */
	public boolean getFilterByAllChk() {
		return filterByAllChk;
	}


	/**
	 * @param filterBySharedLocations the filterBySharedLocations to set
	 */
	public void setFilterBySharedLocations(boolean filterBySharedLocations) {
		this.filterBySharedLocations = filterBySharedLocations;
	}


	/**
	 * @return the filterBySharedLocations
	 */
	public boolean getFilterBySharedLocations() {
		return filterBySharedLocations;
	}


	/**
	 * @param deleteClicked the deleteClicked to set
	 */
	public void setDeleteClicked(boolean deleteClicked) {
		this.deleteClicked = deleteClicked;
	}


	/**
	 * @return the deleteClicked
	 */
	public boolean getDeleteClicked() {
		return deleteClicked;
	}


	public HashMap<String, String> getListModifiedByMap() {
		return listModifiedByMap;
	}


	public void setListModifiedByMap(HashMap<String, String> listModifiedByMap) {
		this.listModifiedByMap = listModifiedByMap;
	}
	
	private String pageSetToken;	
	
	public String getPageSetToken() {
		return pageSetToken;
	}

	public void setPageSetToken(String pageSetToken) {
		this.pageSetToken = pageSetToken;
	}
	

}
