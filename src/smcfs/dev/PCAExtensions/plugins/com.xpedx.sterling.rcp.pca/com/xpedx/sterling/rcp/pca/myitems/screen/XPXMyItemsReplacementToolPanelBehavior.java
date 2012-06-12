/*
 * Created on Dec 03,2010
 *
 */
package com.xpedx.sterling.rcp.pca.myitems.screen;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.internal.win32.DOCINFO;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.util.XPXCacheManager;
import com.xpedx.sterling.rcp.pca.util.XPXPaginationBehavior;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;
import com.yantra.yfc.rcp.internal.YRCCommand;
import com.yantra.yfc.rcp.internal.YRCCommandRepository;

/**
 * @author Administrator
 *
 * Generated by MTCE
 */
public class XPXMyItemsReplacementToolPanelBehavior extends XPXPaginationBehavior {
	
	
	private XPXMyItemsReplacementToolPanel page;
	private static final String COMMAND_GET_TEAM_LIST="getTeamList";
	public static final String PAGINATION_STRATEGY_FOR_MIL_REPTOOL_SEARCH = GENERIC_PAGINATION_STRATEGY;
	private static final String COMMAND_GET_LIST_OF_MY_ITEMS_LISTS="XPXGetBothMyItemsList";
	private String defaultOrgCode ;
	public ArrayList parentcustomer = new ArrayList();
	public ArrayList arrListKey = new ArrayList();
	public int numberOfCustomer;
	public Element elemreplaceModel;
	public XPXMyItemsReplacementToolPanelBehavior(Composite ownerComposite, String formId) {
        super(ownerComposite, formId);
        this.page=(XPXMyItemsReplacementToolPanel) ownerComposite;
        //Set static pagination data for the MIL Replacement Tool Behavior
        this.getXpxPaginationData().setPaginationStrategy(PAGINATION_STRATEGY_FOR_MIL_REPTOOL_SEARCH);
		this.getXpxPaginationData().setSortColumn("Name");
		this.defaultOrgCode = "";
		this.setOrganizationList(XPXUtils.orgList);
		XPXCacheManager.getInstance().getDivisionList(getModel("UserNameSpace").getAttribute("DataSecurityGroupId"), this);
		YRCCommand command = YRCCommandRepository.getCommand((new StringBuilder()).append(getFormId()).append(COMMAND_GET_LIST_OF_MY_ITEMS_LISTS).toString());
		//System.out.println("Command's API name is "+command.getCommandAPIName());
		this.getXpxPaginationData().setApiName(command.getCommandAPIName());//getXPEDX_MyItemsList_List_Hdr service
		this.getXpxPaginationData().setIsFlow("Y");
		
		//Set the model data. This is a pre-requisite before calling the handlePaginationOutput method
		setSrcModelName("XpedxMilBothLstList");
		setRootListElemName("XpedxMilBothLstList");
		setRepeatingElemName("XpedxMilBothLst");
		
		initBehavior();
	}
	
	@Override
	protected void init() {
		getTeamList();
		
	}
    
	private void initBehavior() {
		
	    loadIntialDataAndSetModel();
	    this.getCustomers("MC");
	    
	    //getTeamList();
	}
	
	private void getTeamList() {
		Document docTeamInput = YRCXmlUtils.createDocument("Team");
		docTeamInput.getDocumentElement().setAttribute("TeamId", getModel("UserNameSpace").getAttribute("DataSecurityGroupId"));
		String api[] = {COMMAND_GET_TEAM_LIST,
	           
	        };
		Document doc[] = {docTeamInput
		};
	    callApis(api, doc);
		super.init();
		
	}

	
	void callApis(String names[], Document inputXmls[])
    {
        YRCApiContext context = new YRCApiContext();
        context.setApiNames(names);
        context.setFormId("com.xpedx.sterling.rcp.pca.myitems.screen.XPXMyItemsSearchListScreen");
        context.setInputXmls(inputXmls);
        callApi(context);
    }
	
	public void loadIntialDataAndSetModel() {
		// TODO: write code for search panel initialization
		// If there are widgets within the search panale (e.g. combo box) that has source binding
		// call one or more commands and load the output XMLs using setModel.
		// Must avoid multiple roundtrips by using multi-command call as much as possible.
		// Setting the model during initialization will populate the widgets with data before
		// displaying the screen to user.
		// 
	    // NOTE: Api call is asynchronous. when finished handleApiCompletion will be called
	    // with the same context.
		
		this.getCustomers("MC");
		
    }

    public void getCustomers(String strExtnSuffixType) {
		getCustomers(null, strExtnSuffixType, null);
	}
    
	public void search() {
		
		Element eleXPEDXMyItemsList = elemreplaceModel;	
    	
    	
    	
    //	String strDivisionID = YRCXmlUtils.getAttributeValue(eleXPEDXMyItemsList, "XPEDXMyItemsList/XPEDXMyItemsListShareList/XPEDXMyItemsListShare/@DivisionID");
    	
    		// If DivisionID is void Remove Share List element.
    	/*	Element eleXPEDXMyItemsListShareList = YRCXmlUtils.getChildElement(eleXPEDXMyItemsList, "XPEDXMyItemsListShareList");
    		if(!YRCPlatformUI.isVoid(eleXPEDXMyItemsListShareList)){
    			eleXPEDXMyItemsListShareList.getParentNode().removeChild(eleXPEDXMyItemsListShareList);
    		}*/
    	
   // 	setCustomerPathAttribute(eleXPEDXMyItemsList);

		this.getXpxPaginationData().setInputXml(eleXPEDXMyItemsList.getOwnerDocument());
		super.search();
		
		//callApi("getListOfXPEDXMyItemsLists",eleXPEDXMyItemsList.getOwnerDocument());
    }
    
	private void setCustomerPathAttribute(Element eleXPEDXMyItemsList)
	{
		Element searchCriteriaElem = getTargetModel("SearchCriteria");
    	//if(YRCPlatformUI.isVoid(searchCriteriaElem)){
    		//Prepare customer Path
    		String strCustomerPath = null;
    		String strSeparator = "|";
    		
    		if(numberOfCustomer==3){
    		String strMasterCustomerID = (String) parentcustomer.get(2);
    		String strCustomerID = (String) parentcustomer.get(1);
    		String strBillToCustomerID = (String) parentcustomer.get(0);
       		
          		if (strMasterCustomerID.isEmpty()) {
    			// CustomerPath is null
    			System.out.println("customer path is null");

    		} else {
    			// CustomerPath is not null ignore DivisionId
    			// CustomerPath =
    			// MasterCustomerID|CustomerID|BillToCustomerID|ShipToCustomerID
  				strCustomerPath = strMasterCustomerID + strSeparator + strCustomerID + strSeparator + strBillToCustomerID ;
    						
    			}
    		}
    		else if(numberOfCustomer==4){
        		String strMasterCustomerID = (String) parentcustomer.get(3);
        		String strCustomerID = (String) parentcustomer.get(2);
        		String strBillToCustomerID = (String) parentcustomer.get(1);
           		String strShipToCustomerID = (String) parentcustomer.get(0);
           		
               		if (strMasterCustomerID ==null) {
        			// CustomerPath is null
        			System.out.println("customer path is null");

        		} else {
        			// CustomerPath is not null ignore DivisionId
        			// CustomerPath =
        			// MasterCustomerID|CustomerID|BillToCustomerID|ShipToCustomerID
      				strCustomerPath = strMasterCustomerID + strSeparator + strCustomerID + strSeparator + strBillToCustomerID + strSeparator + strShipToCustomerID;
        						
        			}
        		}
    		else if(numberOfCustomer==2){
        		String strMasterCustomerID = (String) parentcustomer.get(1);
        		String strCustomerID = (String) parentcustomer.get(0);
 
               		if (strMasterCustomerID.isEmpty()) {
        			// CustomerPath is null
        			System.out.println("customer path is null");

        		} else {
        			// CustomerPath is not null ignore DivisionId
        			// CustomerPath =
        			// MasterCustomerID|CustomerID|BillToCustomerID|ShipToCustomerID
      				strCustomerPath = strMasterCustomerID + strSeparator + strCustomerID;
        						
        			}
        		}
    		else if(numberOfCustomer==1){
        		String strMasterCustomerID = (String) parentcustomer.get(0);
        		
           		
        		
               		if (strMasterCustomerID ==null) {
        			// CustomerPath is null
        			System.out.println("customer path is null");

        		} else {
        			// CustomerPath is not null ignore DivisionId
        			// CustomerPath =
        			// MasterCustomerID|CustomerID|BillToCustomerID|ShipToCustomerID
      				strCustomerPath = strMasterCustomerID;
        						
        			}
        		}

    		
    		if (null != strCustomerPath) {
    			// prepare input xml with customer path
    			/**
    			 * <XPEDXMyItemsList> <XPEDXMyItemsListShareList>
    			 * <XPEDXMyItemsListShare
    			 * CustomerPath="30-0000145087-000-N3-12-MSAP|30-00001450"
    			 * CustomerPathQryType="FLIKE" /> </XPEDXMyItemsListShareList>
    			 * <XPEDXMyItemsItemsList> <XPEDXMyItemsItems
    			 * ItemId="XPEDXMyItemsList@LegacyProductCode" />
    			 * </XPEDXMyItemsItemsList> </XPEDXMyItemsList>
    			 **/

    			Element elemXPEDXMyItemsListShareList = YRCXmlUtils.createChild(eleXPEDXMyItemsList, "XPEDXMyItemsListShareList");

    			Element elemXPEDXMyItemsListShare = YRCXmlUtils.createChild(elemXPEDXMyItemsListShareList, "XPEDXMyItemsListShare");
    			
    			elemXPEDXMyItemsListShare.setAttribute("CustomerPathQryType",
    					"FLIKE");
    			elemXPEDXMyItemsListShare.setAttribute("CustomerPath",
    					strCustomerPath);
    			System.out.println("*******************************eleXPEDXMyItemsList Input XML :: "
    					+ YRCXmlUtils.getString(eleXPEDXMyItemsList));

    		
    	}
    	parentcustomer.clear();
	}

    public void handleApiCompletion(YRCApiContext ctx) {
    	if (ctx.getInvokeAPIStatus() < 0) { // api call failed
    		//TODO: show exception message
    	}
    	else {
    		String[] apinames= ctx.getApiNames();
        	for(int i=0; i< apinames.length;i++){
        		String apiname = apinames[i]; 
        		if ( YRCPlatformUI.equals(apiname, "getListOfXPEDXMyItemsLists")) {
        			Document docOutput = ctx.getOutputXmls()[i];
			    	Element eleOutput = docOutput.getDocumentElement();
			    	handleSearchApiCompletion(ctx.getOutputXmls()[i].getDocumentElement());
			     	}
		    	else if(YRCPlatformUI.equals(apiname, "getPage")){
        			Document docOutput = ctx.getOutputXmls()[i];
					Element eleOutput = docOutput.getDocumentElement();
					if (eleOutput != null) {
						//Get the repeating elements and set their Action attribute
						NodeList XpedxMilBothLst = (NodeList) YRCXPathUtils
								.evaluate(eleOutput, "./Output/"
										+ getRootListElemName() + "/"
										+ getRepeatingElemName(),
										XPathConstants.NODESET);
						if (XpedxMilBothLst != null
								&& XpedxMilBothLst.getLength() > 0) {

							for (int k = 0; k < XpedxMilBothLst.getLength(); k++) {
								Element eleMyItemsList = (Element) XpedxMilBothLst
										.item(k);
								eleMyItemsList.setAttribute("Replace", "Y");
								String sharePrivate = eleMyItemsList.getAttribute("SharePrivate");
								String createUserId = eleMyItemsList.getAttribute("Createuserid");
								if(sharePrivate != ""){
									eleMyItemsList.setAttribute("ListType", createUserId);
									
								}
								else{
									eleMyItemsList.setAttribute("ListType", "Shared");
								}
							}
						}
						//This is an inherited method which sets/replaces the model used to display the Paginated results.
						handlePaginationOutput(eleOutput);
					}
        		}
		    	else if( YRCPlatformUI.equals(apiname, "XPXItemReplacementToolForMyItemsService")) {
		    		search();
		    	} /*else if( YRCPlatformUI.equals(apiname, "getCustomerList")){
		    		Document docCustomerList = ctx.getOutputXmls()[i];
		    		YRCXmlUtils.getString(docCustomerList);
		    		populateCustomerList(ctx, docCustomerList);
	    		}*/
		    	else if ("XPXGetParentCustomerListService".equals(apiname)) {
					Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
					System.out.println("*********"+YRCXmlUtils.getString(outXml));
					updateModelWithParentInfo(outXml);
					
					
					
				}	
		    	else if(YRCPlatformUI.equals(apiname, COMMAND_GET_TEAM_LIST)){
        			Document docTeamList = ctx.getOutputXmls()[i];
        			// env.clearApiTemplate(XPXLiterals.API_GET_TEAM_LIST);
        			Node nodeTeamNodesList = (Node) YRCXPathUtils.evaluate(docTeamList.getDocumentElement(), "./Team/TeamNodesList", XPathConstants.NODE);
        			setModel((Element)nodeTeamNodesList);
        			setDivisionsList(getModel("TeamNodesList"));
        		//	setModel("TeamNodesList",(Element)nodeTeamNodesList);

        		}
		    	
		    	else if(YRCPlatformUI.equals(apiname, "XPXMILItemReplacementToolForDivisionsService")){
		    		
		    		Document docTeamList = ctx.getOutputXmls()[i];
		    		if(!YRCPlatformUI.isVoid(docTeamList)){
		    		YRCXmlUtils.getString(docTeamList);
		    		Element resultElement=docTeamList.getDocumentElement();
		    		String resSuccessValue=resultElement.getAttribute("TotalNumberOfRecords");
		    		String messageKey=resSuccessValue+" My Item Lists Updated.";
		    		YRCPlatformUI.showInformation("Information", messageKey);
		    		}
		    		
		    	}
		    	else if("XPXGetDivisionsManagedByTeam".equals(apiname)){
					Element eleOutput = ctx.getOutputXmls()[i].getDocumentElement(); 
					
					setModel("Divisions",eleOutput);
	    		}
		    	else if("XPXGetBothMyItemsList".equals(apiname)){
					Element eleOutput = ctx.getOutputXmls()[i].getDocumentElement(); 
					
					setModel("XpedxMilBothLstList",eleOutput);
	    		}
        	}
    	}
    }

	/**
	 * @param ctx
	 * @param docCustomerList
	 */
/*	private void populateCustomerList(YRCApiContext ctx, Document docCustomerList) {
		String strModelName = null;
		String[] strClearModels = null;
		String strUserDataKey = (String) ctx.getUserData("CustomerLevel");
		if(YRCPlatformUI.equals(strUserDataKey, "MC")){
			strModelName = "MSAPCustomerList";
			strClearModels = new String[]{"SAPCustomerList", "BillToCustomerList", "ShipToCustomerList"};
		} else if(YRCPlatformUI.equals(strUserDataKey, "C")){
			strModelName = "SAPCustomerList";
			strClearModels = new String[]{"BillToCustomerList", "ShipToCustomerList"};
		} else if(YRCPlatformUI.equals(strUserDataKey, "B")){
			strModelName = "BillToCustomerList";
			strClearModels = new String[]{"ShipToCustomerList"};
		} else if(YRCPlatformUI.equals(strUserDataKey, "S")){
			strModelName = "ShipToCustomerList";
			strClearModels = new String[0];
		}
		for (String strClearModel : strClearModels) {
			setModel(strClearModel, YRCXmlUtils.createDocument("CustomerList").getDocumentElement());
		}
		setModel(strModelName, docCustomerList.getDocumentElement());
		
	}*/

	private void handleSearchApiCompletion(Element eleOutput) {
		ArrayList<Element> listParentCustomers = null;
		ArrayList myItemListKey = new ArrayList();
		listParentCustomers = YRCXmlUtils.getChildren(eleOutput, "XPEDXMyItemsList");
		if ((!YRCPlatformUI.isVoid(listParentCustomers))&& (listParentCustomers.size() > 0)) {		
					for (int y=0; y<listParentCustomers.size(); y++){
						Element customerEle = (Element)listParentCustomers.get(y);
						System.out.println("" + customerEle.getAttribute("MyItemsListKey"));
						String listKeyValue=customerEle.getAttribute("MyItemsListKey");		
						myItemListKey.add(listKeyValue);
						Element eleParentMasterCustomer = YRCXmlUtils.createChild(customerEle, "ParentMasterCustomer");
						YRCXmlUtils.importElement(eleParentMasterCustomer, (Element) listParentCustomers.get(0));
					}
					prepareInputXML(myItemListKey) ;
		}
			else{
				YRCPlatformUI.showInformation("Information", "There is no list containing the above item");
			}
	
		ArrayList<Element> list = YRCXmlUtils.getChildren(eleOutput, "XPEDXMyItemsList");
		for (Element eleXPEDXMyItemsList : list) {
			eleXPEDXMyItemsList.setAttribute("Replace", "Y");
		}
		setModel(eleOutput);
	}
	
    public void reset() {
       // loadIntialDataAndSetModel();
    	setFieldValue("DivisionID", "");
		setFieldValue("txtLPC", "");
		setFieldValue("txtReplaceLPC", "");
		setFieldValue("MasterCustomerId", "");
		setFieldValue("SAPId", "");
		setFieldValue("ShipToId", "");
		setFieldValue("BillToId", "");
    }

    public void proceed() {
    	
    	Element eleCriteria = getTargetModel("XPEDXMyItemsList");
    	if(!YRCPlatformUI.isVoid(eleCriteria)){
	    	
    		Element eleReplaceMyItems = getTargetModel("replace_input");
    		NodeList list = (NodeList) YRCXPathUtils.evaluate(eleReplaceMyItems, "/XPEDXMyItemsListList/XPEDXMyItemsList[@Checked='Y']", XPathConstants.NODESET);
    		int len = list.getLength();
    		if(YRCPlatformUI.isVoid(getFieldValue("txtLPC")) 
    				|| YRCPlatformUI.isVoid(getFieldValue("txtReplaceLPC"))){
    			YRCPlatformUI.showError("Message", "Replace with Legacy Product Code is mandatory.");
    			if(YRCPlatformUI.isVoid(getFieldValue("txtLPC"))){
    				getControl("txtLPC").setFocus();
    				return;
    			}
    			if(YRCPlatformUI.isVoid(getFieldValue("txtReplaceLPC"))){
    				getControl("txtReplaceLPC").setFocus();
    				return;
    			}
    		} else if(len<=0){ // Check if at least one MyItemList is selected for Item Replacement
    			YRCPlatformUI.showError("Message", "Select atleast one My Items List to Replace an Item.");
				((XPXMyItemsReplacementToolPanel)getOwnerForm()).tblSearchResults.setSelection(0);
				return;
			} else {	// Process Replace Item in selected MyItems
		    	Document docReplace = YRCXmlUtils.createDocument("XPEDXMyItemsListList");
		    	docReplace.getDocumentElement().setAttribute("LPC", 
		    			YRCXmlUtils.getAttributeValue(eleCriteria, "/XPEDXMyItemsList/XPEDXMyItemsItemsList/XPEDXMyItemsItems/@ItemId"));
		    	docReplace.getDocumentElement().setAttribute("ReplaceWithLPC", 
		    			eleCriteria.getAttribute("ReplaceWithLPC"));
		    	
		    	for( int i=0; i<len; i++){
		    		Element eleMyItemsList = (Element)list.item(i);
		    		Element eleReplaceMyItemsList = YRCXmlUtils.createChild(docReplace.getDocumentElement(), "XPEDXMyItemsList");
		    			eleReplaceMyItemsList.setAttribute("MyItemsListKey", eleMyItemsList.getAttribute("MyItemsListKey"));
		    	}
		    	callApi("XPXItemReplacementToolForMyItemsService",docReplace);
    		}
    	}
    }

    
    public void replaceItems() {
    	
		if(YRCPlatformUI.isVoid(getFieldValue("txtLPC")) 
				|| YRCPlatformUI.isVoid(getFieldValue("txtReplaceLPC"))){
			
			if(YRCPlatformUI.isVoid(getFieldValue("txtLPC"))){
				YRCPlatformUI.showError("Message", "Current Legacy Product Code is mandatory.");
				getControl("txtLPC").setFocus();
				return;
			}
			if(YRCPlatformUI.isVoid(getFieldValue("txtReplaceLPC"))){
				YRCPlatformUI.showError("Message", "Replace with Legacy Product Code is mandatory.");
				getControl("txtReplaceLPC").setFocus();
				return;
			}
		}
		
		String currLPC=getFieldValue("txtLPC");
		String replaceLPC=getFieldValue("txtReplaceLPC");
    	if(!YRCPlatformUI.isVoid(getFieldValue("txtLPC")) && !YRCPlatformUI.isVoid(getFieldValue("txtReplaceLPC")) && currLPC.equals(replaceLPC) ){
    		YRCPlatformUI.showError("Message", "Replace With Legacy Product Code is Same as Current Legacy Product Code");
    		getControl("txtReplaceLPC").setFocus();
			return;
    	}
		
    	Element eleXPEDXMyItemsList = getTargetModel("XPEDXMyItemsList");
    	String strDivisionID = YRCXmlUtils.getAttributeValue(eleXPEDXMyItemsList, "XPEDXMyItemsList/XPEDXMyItemsListShareList/XPEDXMyItemsListShare/@DivisionID");
    	if(!YRCPlatformUI.isVoid(strDivisionID)){
    		
    		callApi("XPXMILItemReplacementToolForDivisionsService",eleXPEDXMyItemsList.getOwnerDocument());
    	}
    	
		
	}
    void callApi(String name, Document inputXml){
    	System.out.println("--" + inputXml);
    	callApi(name, inputXml, null, null); 
    }
    
    void callApi(String name, Document inputXml, String strUserDataKey, String strUserData)
    {
        YRCApiContext context = new YRCApiContext();
        context.setApiName(name);
        context.setFormId(getFormId());
        context.setInputXml(inputXml);
       /* if(!YRCPlatformUI.isVoid(strUserDataKey)){
        	context.setUserData(strUserDataKey, strUserData);
        }*/
        callApi(context);
    }

	public void getCustomers(String strParentCustomerID, String strExtnSuffixType, String strPreviousSuffixType) {
		Document docInput = YRCXmlUtils.createDocument("Customer");
		String enterPriseKey=getEnterPriseKey();
		docInput.getDocumentElement().setAttribute("CallingOrganizationCode",
				enterPriseKey);
		//	getParentCustomers(strPreviousSuffixType) ;
		Element eleExtn = YRCXmlUtils.createChild(docInput.getDocumentElement(), "Extn");
		eleExtn.setAttribute("ExtnSuffixType", strExtnSuffixType);
		if(!YRCPlatformUI.isVoid(strParentCustomerID)){
			Element eleCustomerList = null;
			Element eleParentCustomer = null;
			if(YRCPlatformUI.equals("MC", strPreviousSuffixType)){
				eleCustomerList = getModel("MSAPCustomerList");
			} else if(YRCPlatformUI.equals("C", strPreviousSuffixType)){
				eleCustomerList = getModel("SAPCustomerList");
			} else if(YRCPlatformUI.equals("B", strPreviousSuffixType)){
				eleCustomerList = getModel("BillToCustomerList");
			} else if(YRCPlatformUI.equals("S", strPreviousSuffixType)){
				eleCustomerList = getModel("ShipToCustomerList");
			}
			if(!YRCPlatformUI.isVoid(eleCustomerList)){
				eleParentCustomer = (Element) YRCXPathUtils.evaluate(eleCustomerList, "./Customer[@CustomerID='"+strParentCustomerID+"']", XPathConstants.NODE);
				if(null != eleParentCustomer){
					docInput.getDocumentElement().setAttribute("ParentCustomerKey", eleParentCustomer.getAttribute("CustomerKey"));
				}
			}
//			Element eleParentCustomer = YRCXmlUtils.createChild(docInput.getDocumentElement(), "ParentCustomer");
//			eleParentCustomer.setAttribute("CustomerID", strParentCustomerID);
		}
	/*	String selectedVal = getFieldValue("cmbBillToCustomer");
		selectedVal = "60-0006050662-000-M-XX-B(B)";
		
		YRCXmlUtils.createFromString("<Customer CustomerID='"+selectedVal+"' OrganizationCode='"+enterPriseKey+"'/>"); */
		YRCXmlUtils.getString(docInput);
		callApi("getCustomerList", docInput, "CustomerLevel", strExtnSuffixType);
	//	callApi("XPXGetParentCustomerListService" , YRCXmlUtils.createFromString("<Customer CustomerID='"+selectedVal+"' OrganizationCode='"+enterPriseKey+"'/>"));
	}

	public void setDivisionsList(Element model) {
		YRCXmlUtils.getString(model);
		setModel(model);
		
	}

	@Override
	public void setRepeatingElemName(String repeatingElemName) {
		this.repeatingElemName = repeatingElemName;
	}

	@Override
	public void setRootListElemName(String rootListElemName) {
		this.rootListElemName = rootListElemName;
		
	}

	@Override
	public void setSrcModelName(String srcModelName) {
		this.srcModelName = srcModelName;
	}
	
	private void setOrganizationList(Element eOrgList)
    {
        setModel("OrgList", eOrgList);
        NodeList nl = eOrgList.getElementsByTagName("Organization");
        if(null != nl && nl.getLength()>0)
        	defaultOrgCode = YRCPlatformUI.getUserElement().getAttribute("EnterpriseCode");
        
        this.setDefaultEnterpriseCode();
    }

	public void setDefaultEnterpriseCode()
    {
		//Update the Search Criteria
        Element elemModel = getModel("searchEnterprise");
        if(YRCPlatformUI.isVoid(elemModel))
            elemModel = YRCXmlUtils.createDocument("XPEDXMyItemsList").getDocumentElement();

     
        if(!YRCPlatformUI.isVoid(defaultOrgCode))
        {
            elemModel.setAttribute("EnterpriseKey", defaultOrgCode);
        }
        setModel("searchEnterprise",elemModel);
       
        
    }
	
public String  getEnterPriseKey(){
	
	String enterPriseKey=getFieldValue("cmbEnterprise");
	return enterPriseKey;
}

private void updateModelWithParentInfo(Element outXml) {

	//Element generalInfo = getModel("XPXCustomerIn");
	//String suffixType = YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(generalInfo, "/CustomerList/Customer/Extn"), "ExtnSuffixType");
	
	//ArrayList listCustomers = YRCXmlUtils.getChildren(generalInfo, "Customer");
	ArrayList listParentCustomers = YRCXmlUtils.getChildren(outXml, "Customer");
		if (!YRCPlatformUI.isVoid(listParentCustomers) ) {
		
		for (int i=0; i<listParentCustomers.size(); i++){
		Element customerEle = (Element)listParentCustomers.get(i);
		String CustomerIDValue=YRCXmlUtils.getAttribute(customerEle,"CustomerID");		
		parentcustomer.add(CustomerIDValue);
		numberOfCustomer = parentcustomer.size(); 
		Element eleParentMasterCustomer = YRCXmlUtils.createChild(customerEle, "ParentMasterCustomer");
		YRCXmlUtils.importElement(eleParentMasterCustomer, (Element) listParentCustomers.get(0));
		}
		CallReplacementService();
		//getFirstPage();
		
		/*if ("MC".equals(suffixType)) {
			Element eleParentMasterCustomer = YRCXmlUtils.createChild(customerEle, "ParentMasterCustomer");
			YRCXmlUtils.importElement(eleParentMasterCustomer, (Element) listParentCustomers.get(0));
		}			
		if ("C".equals(suffixType)) {
			Element eleParentMasterCustomer = YRCXmlUtils.createChild(customerEle, "ParentMasterCustomer");
			Element eleParentCustomer = YRCXmlUtils.createChild(customerEle, "ParentCustomer");
			YRCXmlUtils.importElement(eleParentCustomer, (Element) listParentCustomers.get(0));
			YRCXmlUtils.importElement(eleParentMasterCustomer, (Element) listParentCustomers.get(1));
		} else if ("B".equals(suffixType)) {
			Element eleParentCustomer = YRCXmlUtils.createChild(customerEle, "ParentCustomer");
			Element eleParentMasterCustomer = YRCXmlUtils.createChild(customerEle, "ParentMasterCustomer");
			YRCXmlUtils.importElement(eleParentCustomer, (Element) listParentCustomers.get(1));
			YRCXmlUtils.importElement(eleParentMasterCustomer, (Element) listParentCustomers.get(2));
		} else if ("S".equals(suffixType)) {
			Element eleBillToCustomer = YRCXmlUtils.createChild(customerEle, "ParentBillToCustomer");
			Element eleParentCustomer = YRCXmlUtils.createChild(customerEle, "ParentCustomer");
			Element eleParentMasterCustomer = YRCXmlUtils.createChild(customerEle, "ParentMasterCustomer");
			YRCXmlUtils.importElement(eleBillToCustomer, (Element) listParentCustomers.get(1));
			YRCXmlUtils.importElement(eleParentCustomer, (Element) listParentCustomers.get(2));
			YRCXmlUtils.importElement(eleParentMasterCustomer, (Element) listParentCustomers.get(3));
		}*/
	}
	//setModel("XPXCustomerIn", generalInfo);


}


public void getParentCustomers() {
	String BillToValue = getFieldValue("BillToId");
	String ShipToValue = getFieldValue("ShipToId");
	String SAPIdValue = getFieldValue("SAPId");
	String MasterCustomerValue = getFieldValue("MasterCustomerId");
	
	String enterPriseKey=getEnterPriseKey();
	
	//selectedVal = "12-0000100185-000-M-XX-B";
	if(BillToValue != null && BillToValue != ""){
	Document docInput = YRCXmlUtils.createFromString("<Customer CustomerID='"+BillToValue+"' OrganizationCode='"+enterPriseKey+"'/>");
	callApi("XPXGetParentCustomerListService" , docInput);
	}
	
	if(ShipToValue != null && ShipToValue != ""){
		Document docInput = YRCXmlUtils.createFromString("<Customer CustomerID='"+ShipToValue+"' OrganizationCode='"+enterPriseKey+"'/>");
		callApi("XPXGetParentCustomerListService" , docInput);
	}
	if(SAPIdValue != null && SAPIdValue != ""){
		Document docInput = YRCXmlUtils.createFromString("<Customer CustomerID='"+SAPIdValue+"' OrganizationCode='"+enterPriseKey+"'/>");
		callApi("XPXGetParentCustomerListService" , docInput);
	}
	if(MasterCustomerValue != null && MasterCustomerValue != ""){
		Document docInput = YRCXmlUtils.createFromString("<Customer CustomerID='"+MasterCustomerValue+"' OrganizationCode='"+enterPriseKey+"'/>");
		callApi("XPXGetParentCustomerListService" , docInput);
	}
	
	if((MasterCustomerValue == null || MasterCustomerValue == "") && (SAPIdValue == null || SAPIdValue == "") && (ShipToValue == null || ShipToValue == "") || (BillToValue == null && BillToValue == "")){
		CallReplacementServiceForDivision();	
	}
	
	
}

public void addSelectedDivisions(SelectionEvent e) {
	Widget ctrl = e.widget;
    String ctrlName = (String)ctrl.getData("name");
    if(ctrlName != null)
    {
    	TableItem tblItems[] = page.getTblDivisions().getSelection();
        if(tblItems.length > 0){
        	for(int i = 0; i < tblItems.length; i++){
        	Element eleDetailsInput = (Element)tblItems[i].getData();
        	System.out.println(eleDetailsInput.getAttribute("OrganizationCode"));
        	}
			}
        }
    
}
public void CallReplacementService(){
	Element eleXPEDXMyItemsList = getTargetModel("XPEDXMyItemsList");	
    		Element eleXPEDXMyItemsListShareList = YRCXmlUtils.getChildElement(eleXPEDXMyItemsList, "XPEDXMyItemsListShareList");
    		if(!YRCPlatformUI.isVoid(eleXPEDXMyItemsListShareList)){
    			eleXPEDXMyItemsListShareList.getParentNode().removeChild(eleXPEDXMyItemsListShareList);
    		}
    	
    	setCustomerPathAttribute(eleXPEDXMyItemsList);
callApi("getListOfXPEDXMyItemsLists",eleXPEDXMyItemsList.getOwnerDocument());
}
public void prepareInputXML(ArrayList arrListKey) {
      elemreplaceModel = YRCXmlUtils.createDocument("XpedxMilBothLst")
                .getDocumentElement();
    
    Element attrElemComplex = YRCXmlUtils.createChild(elemreplaceModel, "ComplexQuery");
    Element attrOr = YRCXmlUtils.createChild(attrElemComplex, "Or");
    
    for ( int i=0; i<arrListKey.size();i++) {
          String listKey = (String) arrListKey.get(i);
          if (listKey != null && listKey != " ") {
                Element attrName = YRCXmlUtils.createChild(attrOr, "Exp");
                attrName.setAttribute("Name", "MyItemsListKey");
                attrName.setAttribute("Value", listKey);
                attrOr.appendChild(attrName);
          }

    }

    Element attrName1 = YRCXmlUtils.createChild(attrOr, "Exp");
    setModel(elemreplaceModel);
    this.elemreplaceModel = elemreplaceModel;
    getFirstPage();
    
   }
public void CallReplacementServiceForDivision(){
	Element eleXPEDXMyItemsList = getTargetModel("XPEDXMyItemsList");	
    		String itemID = getFieldValue("txtLPC");
    		String replaceItemID = getFieldValue("txtReplaceLPC");
    		String strDivisionID = YRCXmlUtils.getAttributeValue(eleXPEDXMyItemsList, "XPEDXMyItemsList/XPEDXMyItemsListShareList/XPEDXMyItemsListShare/@DivisionID");
    		String[] divisionArray = strDivisionID.split(",");
    		
    		//Prepare the Input XML when Search By Division
    		Element elemModel = YRCXmlUtils.createDocument("XPEDXMyItemsList").getDocumentElement();
    		elemModel.setAttribute("ReplaceWithLPC", replaceItemID);
    		Element e1 = YRCXmlUtils.createChild(elemModel, "XPEDXMyItemsItemsList");
    		Element e2 = YRCXmlUtils.createChild(e1, "XPEDXMyItemsItems");
    		e2.setAttribute("ItemId", itemID);
    		
    		Element e3 = YRCXmlUtils.createChild(elemModel, "XPEDXMyItemsListShareList");
    		Element e4 = YRCXmlUtils.createChild(e3, "XPEDXMyItemsListShare");
    		Element attrElemComplex = YRCXmlUtils.createChild(e4, "ComplexQuery");
    		Element attrOr = YRCXmlUtils.createChild(attrElemComplex, "Or");
    		for ( int i=0; i<divisionArray.length;i++) {
    	          String division = (String) divisionArray[i];
    	          if (division != null && division != " ") {
    	                Element attrName = YRCXmlUtils.createChild(attrOr, "Exp");
    	                attrName.setAttribute("Name", "DivisionID");
    	                attrName.setAttribute("Value", division);
    	                attrOr.appendChild(attrName);
    	          }

    	    }
    	
    	callApi("getListOfXPEDXMyItemsLists",elemModel.getOwnerDocument());
}
public void searchCustomer(){
	String BillToValue = getFieldValue("BillToId");
	String ShipToValue = getFieldValue("ShipToId");
	String SAPIdValue = getFieldValue("SAPId");
	String MasterCustomerValue = getFieldValue("MasterCustomerId");
	String enterPriseKey=getEnterPriseKey();
	String customerIdSelected =  null;
	if(YRCPlatformUI.isVoid(getFieldValue("txtLPC")) 
			|| YRCPlatformUI.isVoid(getFieldValue("txtReplaceLPC"))){
		
		if(YRCPlatformUI.isVoid(getFieldValue("txtLPC"))){
			YRCPlatformUI.showError("Message", "Current Legacy Product Code is mandatory.");
			getControl("txtLPC").setFocus();
			return;
		}
		if(YRCPlatformUI.isVoid(getFieldValue("txtReplaceLPC"))){
			YRCPlatformUI.showError("Message", "Replace with Legacy Product Code is mandatory.");
			getControl("txtReplaceLPC").setFocus();
			return;
		}
	}
	
	String currLPC=getFieldValue("txtLPC");
	String replaceLPC=getFieldValue("txtReplaceLPC");
	if(!YRCPlatformUI.isVoid(getFieldValue("txtLPC")) && !YRCPlatformUI.isVoid(getFieldValue("txtReplaceLPC")) && currLPC.equals(replaceLPC) ){
		YRCPlatformUI.showError("Message", "Replace With Legacy Product Code is Same as Current Legacy Product Code");
		getControl("txtReplaceLPC").setFocus();
		return;
	}
	
	if((MasterCustomerValue == null || MasterCustomerValue == "") && (SAPIdValue == null || SAPIdValue == "") && (ShipToValue == null || ShipToValue == "") || (BillToValue == null && BillToValue == "")){
		YRCPlatformUI.showError("Information", "Please enter a customer name");	
		return;
	}
	
	if(BillToValue != null && BillToValue != ""){
		Element elemModel = YRCXmlUtils.createDocument("Customer").getDocumentElement();
		elemModel.setAttribute("CallingOrganizationCode", enterPriseKey);
		elemModel.setAttribute("CustomerType", "01");
		Element e1 = YRCXmlUtils.createChild(elemModel, "BuyerOrganization");
		e1.setAttribute("OrganizationName", BillToValue);
		e1.setAttribute("OrganizationNameQryType", "FLIKE");
		Element e2 = YRCXmlUtils.createChild(elemModel, "Extn");
		e2.setAttribute("ExtnSuffixType", "B");
		
		YRCSharedTaskOutput output = YRCPlatformUI.launchSharedTask("com.xpedx.sterling.rcp.pca.sharedTasks.XPXCustomerSearchSharedTask",elemModel);
		Element eleUOMInfo = output.getOutput();
		customerIdSelected = eleUOMInfo.getAttribute("CustomerIdSelected");
		String customerNameSelected = eleUOMInfo.getAttribute("CustomerNameSelected");
		if(customerNameSelected != null){
			setFieldValue("BillToId",customerNameSelected);
		}
		CallReplacementServiceForCustomerName(customerIdSelected);
	}
	
	if(ShipToValue != null && ShipToValue != ""){
		Element elemModel = YRCXmlUtils.createDocument("Customer").getDocumentElement();
		elemModel.setAttribute("CallingOrganizationCode", enterPriseKey);
		elemModel.setAttribute("CustomerType", "01");
		Element e1 = YRCXmlUtils.createChild(elemModel, "BuyerOrganization");
		e1.setAttribute("OrganizationName", ShipToValue);
		e1.setAttribute("OrganizationNameQryType", "FLIKE");
		Element e2 = YRCXmlUtils.createChild(elemModel, "Extn");
		e2.setAttribute("ExtnSuffixType", "S");
		
		YRCSharedTaskOutput output = YRCPlatformUI.launchSharedTask("com.xpedx.sterling.rcp.pca.sharedTasks.XPXCustomerSearchSharedTask",elemModel);
		Element eleUOMInfo = output.getOutput();
		customerIdSelected = eleUOMInfo.getAttribute("CustomerIdSelected");
		String customerNameSelected = eleUOMInfo.getAttribute("CustomerNameSelected");
		if(customerNameSelected != null){
			setFieldValue("ShipToId",customerNameSelected);
		}
		CallReplacementServiceForCustomerName(customerIdSelected);
	}
	if(SAPIdValue != null && SAPIdValue != ""){
		Element elemModel = YRCXmlUtils.createDocument("Customer").getDocumentElement();
		elemModel.setAttribute("CallingOrganizationCode", enterPriseKey);
		elemModel.setAttribute("CustomerType", "01");
		Element e1 = YRCXmlUtils.createChild(elemModel, "BuyerOrganization");
		e1.setAttribute("OrganizationName", SAPIdValue);
		e1.setAttribute("OrganizationNameQryType", "FLIKE");
		Element e2 = YRCXmlUtils.createChild(elemModel, "Extn");
		e2.setAttribute("ExtnSuffixType", "C");
		YRCSharedTaskOutput output = YRCPlatformUI.launchSharedTask("com.xpedx.sterling.rcp.pca.sharedTasks.XPXCustomerSearchSharedTask",elemModel);
		Element eleUOMInfo = output.getOutput();
		customerIdSelected = eleUOMInfo.getAttribute("CustomerIdSelected");
		String customerNameSelected = eleUOMInfo.getAttribute("CustomerNameSelected");
		if(customerNameSelected != null){
			setFieldValue("SAPId",customerNameSelected);
		}
		CallReplacementServiceForCustomerName(customerIdSelected);
	}
	if(MasterCustomerValue != null && MasterCustomerValue != ""){
		Element elemModel = YRCXmlUtils.createDocument("Customer").getDocumentElement();
		elemModel.setAttribute("CallingOrganizationCode", enterPriseKey);
		elemModel.setAttribute("CustomerType", "01");
		Element e1 = YRCXmlUtils.createChild(elemModel, "BuyerOrganization");
		e1.setAttribute("OrganizationName", MasterCustomerValue);
		e1.setAttribute("OrganizationNameQryType", "FLIKE");
		Element e2 = YRCXmlUtils.createChild(elemModel, "Extn");
		e2.setAttribute("ExtnSuffixType", "MC");
		YRCSharedTaskOutput output = YRCPlatformUI.launchSharedTask("com.xpedx.sterling.rcp.pca.sharedTasks.XPXCustomerSearchSharedTask",elemModel);
		Element eleUOMInfo = output.getOutput();
		customerIdSelected = eleUOMInfo.getAttribute("CustomerIdSelected");
		String customerNameSelected = eleUOMInfo.getAttribute("CustomerNameSelected");
		if(customerNameSelected != null){
			setFieldValue("MasterCustomerId",customerNameSelected);
		}
		CallReplacementServiceForCustomerName(customerIdSelected);
	}
	
	
	
	
}
public void CallReplacementServiceForCustomerName(String customerIdSelected){
	Element eleXPEDXMyItemsList = getTargetModel("XPEDXMyItemsList");	
	String itemID = getFieldValue("txtLPC");
	String replaceItemID = getFieldValue("txtReplaceLPC");
	//Prepare the Input XML when Search By CustomerName
	Element elemModel = YRCXmlUtils.createDocument("XPEDXMyItemsList").getDocumentElement();
	elemModel.setAttribute("ReplaceWithLPC", replaceItemID);
	Element e1 = YRCXmlUtils.createChild(elemModel, "XPEDXMyItemsItemsList");
	Element e2 = YRCXmlUtils.createChild(e1, "XPEDXMyItemsItems");
	e2.setAttribute("ItemId", itemID);
	Element e3 = YRCXmlUtils.createChild(elemModel, "XPEDXMyItemsListShareList");
	Element e4 = YRCXmlUtils.createChild(e3, "XPEDXMyItemsListShare");
	e4.setAttribute("CustomerID", customerIdSelected);

callApi("getListOfXPEDXMyItemsLists",elemModel.getOwnerDocument());
}
}
