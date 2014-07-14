package com.xpedx.sterling.rcp.pca.user.internal.screen;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.WorkbenchPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.user.internal.editor.XPXCreateUserEditor;
import com.xpedx.sterling.rcp.pca.userprofile.editor.XPXUserProfileEditor;
import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXCreateInternalUserPanelBehavior extends YRCBehavior {
	private XPXCreateInternalUserPanel page;
	private Element getUserGroupListOutXml;
	private Element inputElement;
	String employeeID = null;
	String userKey;
	String loginID;
	boolean count = false;
	ArrayList salesRepKey = new ArrayList();
	
	
	public XPXCreateInternalUserPanelBehavior(Composite ownerComposite, String formId) {
        super(ownerComposite, formId);
        this.page = (XPXCreateInternalUserPanel) ownerComposite;
        inputElement = page.getPageInput();
        this.loadIntialDataAndSetModel();
        
    }
	
	public void loadIntialDataAndSetModel() {
		// panel initialization
		// If there are widgets within the search panale (e.g. combo box) that has source binding
		// call one or more commands and load the output XMLs using setModel.
		// Must avoid multiple roundtrips by using multi-command call as much as possible.
		// Setting the model during initialization will populate the widgets with data before
		// displaying the screen to user.
		// 
	    // NOTE: Api call is asynchronous. when finished handleApiCompletion will be called
	    // with the same context.
//		

		Document docUserInputQry = null;
		String strEnterpriseCode = "";
		String[] apinames = null;
		Document[] docInput = null;
		
		Document docTeamInputQry = YRCXmlUtils.createFromString("<Team />");
		Document docUserGroupInputQry = YRCXmlUtils.createFromString("<UserGroup UsergroupId='XPX' UsergroupIdQryType='FLIKE'/>");
		
		if (!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(), "UserKey"))){
			docUserInputQry = page.getPageInput().getOwnerDocument();
			strEnterpriseCode = YRCXmlUtils.getAttribute(docUserInputQry.getDocumentElement(), "EnterpriseCode");
			docTeamInputQry.getDocumentElement().setAttribute("OrganizationCode", strEnterpriseCode);
			this.setControlState();

			apinames = new String[]{"getTeamList","getUserHierarchy","getUserGroupList"};
			docInput = new Document[]{ docTeamInputQry, docUserInputQry, docUserGroupInputQry};
		} else {
			strEnterpriseCode = YRCPlatformUI.getUserElement().getAttribute("EnterpriseCode");
			docTeamInputQry.getDocumentElement().setAttribute("OrganizationCode", strEnterpriseCode);
		
			apinames = new String[]{"getTeamList","getUserGroupList"};
			docInput = new Document[]{ docTeamInputQry, docUserGroupInputQry};
		}
		callApis(apinames, docInput);
    }

	public void create() {
    	Element inputElement = getTargetModel("XPXInternalUser");
    	if (!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(),"UserKey"))) {
    		inputElement.setAttribute("UserKey", YRCXmlUtils.getAttribute(page.getPageInput(), "UserKey"));
		}
    	
    	String[] apinames = null;
		Document[] docInput = null;
		if (!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(),"UserKey"))||!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(inputElement,"UserKey"))) {
			//Added for JIRA 4147
			Element userDetails = getModel("User_Details");
			Element personInfoInputxml = YRCXmlUtils.getXPathElement(userDetails, "/User/ContactPersonInfo");
			Element contactPersonInfo =  YRCXmlUtils.getXPathElement(inputElement, "/User/ContactPersonInfo");
			if(!YRCPlatformUI.isVoid(personInfoInputxml)){
				contactPersonInfo.setAttribute("AddressLine4",personInfoInputxml.getAttribute("AddressLine4"));
				contactPersonInfo.setAttribute("AddressLine5",personInfoInputxml.getAttribute("AddressLine5"));
				contactPersonInfo.setAttribute("AddressLine6",personInfoInputxml.getAttribute("AddressLine6"));
				contactPersonInfo.setAttribute("AlternateEmailID",personInfoInputxml.getAttribute("AlternateEmailID"));
				contactPersonInfo.setAttribute("Beeper",personInfoInputxml.getAttribute("Beeper"));
				contactPersonInfo.setAttribute("DayFaxNo",personInfoInputxml.getAttribute("DayFaxNo"));
				contactPersonInfo.setAttribute("Department",personInfoInputxml.getAttribute("Department"));
				contactPersonInfo.setAttribute("ErrorTxt",personInfoInputxml.getAttribute("ErrorTxt"));
				contactPersonInfo.setAttribute("EveningFaxNo",personInfoInputxml.getAttribute("EveningFaxNo"));
				contactPersonInfo.setAttribute("EveningPhone",personInfoInputxml.getAttribute("EveningPhone"));
				contactPersonInfo.setAttribute("FirstName",personInfoInputxml.getAttribute("FirstName"));
				contactPersonInfo.setAttribute("HttpUrl",personInfoInputxml.getAttribute("HttpUrl"));
				contactPersonInfo.setAttribute("JobTitle",personInfoInputxml.getAttribute("JobTitle"));
				contactPersonInfo.setAttribute("LastName",personInfoInputxml.getAttribute("LastName"));
				contactPersonInfo.setAttribute("MiddleName",personInfoInputxml.getAttribute("MiddleName"));
				contactPersonInfo.setAttribute("MobilePhone",personInfoInputxml.getAttribute("MobilePhone"));
				contactPersonInfo.setAttribute("OtherPhone",personInfoInputxml.getAttribute("OtherPhone"));
				contactPersonInfo.setAttribute("PersonID",personInfoInputxml.getAttribute("PersonID"));
				contactPersonInfo.setAttribute("PreferredShipAddress",personInfoInputxml.getAttribute("PreferredShipAddress"));
				contactPersonInfo.setAttribute("Suffix",personInfoInputxml.getAttribute("Suffix"));
				contactPersonInfo.setAttribute("Title",personInfoInputxml.getAttribute("Title"));
				contactPersonInfo.setAttribute("UseCount",personInfoInputxml.getAttribute("UseCount"));
				contactPersonInfo.setAttribute("VerificationStatus",personInfoInputxml.getAttribute("VerificationStatus"));				
			}
			Element groupListsElement = YRCXmlUtils.getChildElement(inputElement, "UserGroupLists");
			groupListsElement.setAttribute("Reset", "Y");
			ArrayList<Element> listOfAssignedGroups = YRCXmlUtils.getChildren(groupListsElement, "UserGroupList");
			for (Element group : listOfAssignedGroups) {
				if(group.getAttribute("Checked").equals("N"))
				{
					groupListsElement.removeChild(group);
				}
			}
			
			apinames = new String[]{"modifyUserHierarchy"};
			inputElement.setAttribute("Usertype", XPXConstants.DEFAULT_CSR_USER_TYPE);
			docInput = new Document[]{
					inputElement.getOwnerDocument()
			};
		} else {
			//
			inputElement.setAttribute("Localecode", "en_US_EST");
			inputElement.setAttribute("GeneratePassword", "true");
			inputElement.setAttribute("Activateflag", "Y");
			inputElement.setAttribute("MenuId", XPXConstants.DEFAULT_MENU_ID);
			inputElement.setAttribute("Theme", XPXConstants.DEFAULT_THEME);
			inputElement.setAttribute("Usertype", XPXConstants.DEFAULT_CSR_USER_TYPE);
			// Defaulting the storefront ID as xpedx, as internal users will be created at xpedx level only
			inputElement.setAttribute("OrganizationKey", XPXConstants.DEFAULT_SFID);
			
			// stamping Users's Company name as International Papers when a Internal user is created.
			Element contactElement = YRCXmlUtils.getChildElement(inputElement, "ContactPersonInfo");
			contactElement.setAttribute("Company", XPXConstants.DEFAULT_CSR_USER_COMPANY);

			// stamping ExtnUserType=INTERNAL when an internal user is created.
			Element extnuser = YRCXmlUtils.getChildElement(inputElement,"Extn");
			extnuser.setAttribute(XPXConstants.EXTN_USER_TYPE , XPXConstants.DEFAULT_CSR_USER_TYPE);

			// stamping FirstName and Last Name from User's LDAP details when an internal user is created.
			Element eleLDAPDetails = getModel("XPXLDAPSearchDetails_output");
			if(null != eleLDAPDetails){
				String strGivenName = eleLDAPDetails.getAttribute("EmployeeName");
				if(YRCPlatformUI.isVoid(strGivenName)){
					strGivenName = "";
				}
				
				String strLastName = eleLDAPDetails.getAttribute("EmployeeLastName");
				if(YRCPlatformUI.isVoid(strLastName)){
					strLastName = "";
				}
				
				Element eleContactPersonInfo = YRCXmlUtils.getChildElement(inputElement,"ContactPersonInfo");
				eleContactPersonInfo.setAttribute("FirstName" , strGivenName);
				eleContactPersonInfo.setAttribute("LastName" , strLastName);
			}
			
			// Retaining the User Groups which are associated by the user.
			Element groupListsElement = YRCXmlUtils.getChildElement		(inputElement, "UserGroupLists");
			ArrayList<Element> listOfAssignedGroups = YRCXmlUtils.getChildren(groupListsElement, "UserGroupList");
			for (Element group : listOfAssignedGroups) {
				if(group.getAttribute("Checked").equals("N"))
				{
					groupListsElement.removeChild(group);
				}
			}
			
			
			apinames = new String[]{"createUserHierarchy"};
			docInput = new Document[]{
					inputElement.getOwnerDocument()
			};
		}
    	
		YRCApiContext context = new YRCApiContext();
		context.setApiNames(apinames);
		context.setFormId("com.xpedx.sterling.rcp.pca.user.internal.screen.XPXCreateInternalUserPanel");
		context.setInputXmls(docInput);
		callApi(context);

    }
    
   public void handleApiCompletion(YRCApiContext ctx) {
	   	Element eleOutput = null;
    	if (ctx.getInvokeAPIStatus() < 0) { // api call failed
    		//TODO: show exception message
    	} else {
    		
    		String[] apinames = ctx.getApiNames();
    		for (int i = 0; i < apinames.length; i++) {
				String apiname = apinames[i];
		    	if ("getUserHierarchy".equals(apiname)||"createUserHierarchy".equals(apiname)||"modifyUserHierarchy".equals(apiname)) {
		    		if(!"modifyUserHierarchy".equals(apiname) && !"createUserHierarchy".equals(apiname))
		    		{
		    		 eleOutput = ctx.getOutputXmls()[i].getDocumentElement(); 
		    		 setModel(eleOutput);
		    		}
		    		if("getUserHierarchy".equals(apiname))
		    		{
		    			eleOutput = ctx.getOutputXmls()[i].getDocumentElement(); 
		    			Element groupListsElement = YRCXmlUtils.getChildElement(eleOutput, "UserGroupLists");
		    			//Element groupListElement = YRCXmlUtils.getChildElement(groupListsElement, "UserGroupList");
		    			ArrayList<Element> listOfAssignedGroups = YRCXmlUtils.getChildren(groupListsElement, "/UserGroupList/UserGroup");
						for (Element group : listOfAssignedGroups) {
							group.setAttribute("Checked", "Y");
						}
						setModel("User_Details",eleOutput);
						
						YRCPlatformUI.openEditor("com.xpedx.sterling.rcp.pca.user.internal.editor.XPXCreateUserEditor", new YRCEditorInput(eleOutput, new String[] {"UserKey"}, eleOutput.getAttribute("UserKey")));
						
		    		}
		    		
		    		if("createUserHierarchy".equals(apiname)||"modifyUserHierarchy".equals(apiname))
		    		{
			    		page.setPageInput(eleOutput);
			    		Element inputElement = getTargetModel("XPXInternalUser");
			    		Element input = YRCXmlUtils.createDocument("User").getDocumentElement();
						input.setAttribute("Loginid", inputElement.getAttribute("Loginid"));
						getUserDetails(input);
						YRCPlatformUI.showInformation("Success","Success");
						//Created to assign extn_user_type field as INTERNAL when the user is modified
						inputElement.setAttribute("Usertype", XPXConstants.DEFAULT_CSR_USER_TYPE);
						Element extnuser = YRCXmlUtils.getChildElement(inputElement,"Extn");
						extnuser.setAttribute(XPXConstants.EXTN_USER_TYPE , XPXConstants.DEFAULT_CSR_USER_TYPE);
						
						if("createUserHierarchy".equals(apiname))
						{
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor((YRCEditorPart)YRCDesktopUI.getCurrentPart(), false);
							YRCPlatformUI.openEditor("com.xpedx.sterling.rcp.pca.user.internal.editor.XPXCreateUserEditor", new YRCEditorInput(eleOutput, new String[] {"UserKey"}, eleOutput.getAttribute("UserKey")));
						}
					
		    		}
		    		
		    		XPXCreateUserEditor part = (XPXCreateUserEditor)YRCDesktopUI.getCurrentPart();
		    		part.getTitleKey("TITLE_Modify_User", eleOutput.getAttribute("Username"));
		    		Element userdetail = getModel("User_Details");
		    		if(userdetail !=null && userdetail.getAttribute("UserKey")!=null){
						userKey = userdetail.getAttribute("UserKey");
						loginID = userdetail.getAttribute("Loginid");
						getSalesRepDetails();
					}
		    						
		    	} else if (apiname.equals("getUserGroupList")) {
		    		Element checkElement = getModel("User_Details");
		    		String userGroupKey = "";
		    		ArrayList userGroupKeyList = new ArrayList();
		    		if(!YRCPlatformUI.isVoid(checkElement))
		    		{
		    			Element groupListsElement = YRCXmlUtils.getChildElement(checkElement, "UserGroupLists");
		    			ArrayList<Element> groupList = YRCXmlUtils.getChildren(groupListsElement, "UserGroupList");
						for (Element group : groupList) {
							userGroupKey = group.getAttribute("UsergroupKey");
							userGroupKeyList.add(userGroupKey);
						}
		    		}
					getUserGroupListOutXml = ctx.getOutputXmls()[i].getDocumentElement();
					ArrayList<Element> listOfGroups = YRCXmlUtils.getChildren(getUserGroupListOutXml, "UserGroup");
					for (Element object : listOfGroups) {
						if(userGroupKeyList.contains(object.getAttribute("UsergroupKey")))
							object.setAttribute("Checked", "Y");
						else
							object.setAttribute("Checked", "N");
					}
					setModel("getUserGroupLists_output",ctx.getOutputXmls()[i].getDocumentElement());
					
				} else if("getTeamList".equals(apiname))
	    		{
	    			eleOutput = ctx.getOutputXmls()[i].getDocumentElement(); 
	    			setModel("getTeamList_output",eleOutput);
	    		}
				//Added for Jira 4216
				 else if("XPXGetSalesRepCustomersService".equals(apiname))
		    		{
		    			eleOutput = ctx.getOutputXmls()[i].getDocumentElement(); 
		    			updateSalesRepDetails(eleOutput);
		    		}
				 else if("changeSalesRepDetails".equals(apiname))
		    		{
					 ((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
					 	eleOutput = ctx.getOutputXmls()[i].getDocumentElement();
		    			((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(false);
		    		}
		    	//--- function changed by sukumar-tw for New LDAP service to fetch user details-
				else if("XPXLDAPSearchDetails".equals(apiname)){

					eleOutput = ctx.getOutputXmls()[i].getDocumentElement(); 
					setModel("XPXLDAPSearchDetails_output",eleOutput);
					if(eleOutput.hasAttribute("Exception")){
						String exception=eleOutput.getAttribute("Exception");
						YRCPlatformUI.showError("Error", exception);
						clearData();
					
					}
					else{
						String strGivenName = YRCXmlUtils.getXPathElement(eleOutput, "/User").getAttribute("EmployeeName");
						String strLastName = YRCXmlUtils.getXPathElement(eleOutput, "/User").getAttribute("EmployeeLastName");
						if( YRCPlatformUI.isVoid(strGivenName)){
							strGivenName = "";
						}
						if( YRCPlatformUI.isVoid(strLastName)){
							strLastName = "";
						} else {
							if (!strLastName.equalsIgnoreCase(strLastName)) {
								strLastName += strLastName;
							}
						}
						setFieldValue("txtName", strGivenName +" "+ strLastName);
						
						String strEmployeeID = YRCXmlUtils.getXPathElement(eleOutput, "/User").getAttribute("EmployeeID");
						if(YRCPlatformUI.isVoid(strEmployeeID)){
							strEmployeeID = "";
						}
						setFieldValue("txtEmployeeId", strEmployeeID);
						
						String strEmailAddress = YRCXmlUtils.getXPathElement(eleOutput, "/User").getAttribute("EmailAddress");
						if(YRCPlatformUI.isVoid(strEmailAddress)){
							strEmailAddress = "";
						}
						setFieldValue("txtEmailAddr", strEmailAddress);
						
						String strTelephoneNumber = YRCXmlUtils.getXPathElement(eleOutput, "/User").getAttribute("TelephoneNumber");
						if(YRCPlatformUI.isVoid(strEmailAddress)){
							strTelephoneNumber = "";
						}
						setFieldValue("txtPhoneNumber", strTelephoneNumber);

					}
				//---End of  function changed by sukumar-tw for New LDAP service to fetch user details-
	    			
		    	}
		    	
		    	
	    	}
    	}
    	
    }

	// Added for Jira 4216
   public void getSalesRepDetails()
   {
	   Element userDetails = getModel("User_Details");
	   if (userDetails != null) {
			Element extnUser = YRCXmlUtils.getChildElement(userDetails, "Extn");
			employeeID = extnUser.getAttribute("ExtnEmployeeId");
			if (!YRCPlatformUI.isVoid(employeeID)) {
				YRCApiContext apiCtx = new YRCApiContext();
				String[] apinames = { "XPXGetSalesRepCustomersService" };
				Document[] docInput = { YRCXmlUtils
						.createFromString("<XPEDXSalesRep SalesRepId='"
								+ employeeID + "' />") };
				apiCtx.setApiNames(apinames);
				apiCtx.setInputXmls(docInput);
				apiCtx.setFormId(getFormId());
				callApi(apiCtx);
			}
	   }
   }
   
   public void updateSalesRepDetails(Element salesRepDetails){
	   String salesUserKey = null;
	   String networkID = null;
	   String salesRPKey = null;
	   if(salesRepDetails != null){
		   
		   NodeList salesRepList = salesRepDetails.getElementsByTagName("XPEDXSalesRep");
		   for(int i=0; i<salesRepList.getLength(); i++){
			   Element salesRep = (Element) salesRepList.item(i);
			   networkID = salesRep.getAttribute("NetworkID");
			   salesUserKey = salesRep.getAttribute("SalesUserKey");
			   salesRPKey = salesRep.getAttribute("SalesRPKey");
			   if((salesUserKey == null || salesUserKey == "") || (networkID == null || networkID == "")){
				   salesRepKey.add(salesRPKey);
				   count = true;
			   }
		   }
		   if(count){
			   Document doc = null;
				for (int i = 0; i < salesRepKey.size(); i++) {
					doc = YRCXmlUtils.createFromString("<XPEDXSalesRep SalesRPKey='" + salesRepKey.get(i) + "'  SalesUserKey='" + userKey + "'  NetworkID='" + loginID + "'  />");
					YRCApiContext apiCtx = new YRCApiContext();
					apiCtx.setApiNames(new String[]{"changeSalesRepDetails"});
					apiCtx.setInputXml(doc);
					apiCtx.setFormId(getFormId());
					callApi(apiCtx);
				}
					((XPXUserProfileEditor) YRCDesktopUI.getCurrentPart())
							.showBusy(true);
		   }
	   }
   }
   //End for Jira 4216
   private void setControlState() {
		setControlEditable("txtNetworkId", false);
		setFieldValue("btnUpdate", YRCPlatformUI.getString("Update"));
	}
	
	private void getUserDetails(Element pageInput) {

		this.setControlState();

		//Element eleInput = YRCXmlUtils.createDocument("User").getDocumentElement();
		//eleInput.setAttribute("UserKey", YRCXmlUtils.getAttribute(page.getPageInput(), "UserKey"));
		//eleInput.setAttribute("Loginid", YRCXmlUtils.getAttribute(page.getPageInput(), "Loginid"));
		String[] apinames = {"getUserHierarchy"};
		//Document[] docInput = {eleInput.getOwnerDocument()};			
		Document[] docInput = {pageInput.getOwnerDocument()};
		callApis(apinames, docInput);
	}
	
    public void reset() {
        loadIntialDataAndSetModel();
    }

    public void proceed() {
        //TODO: write proceed code for a selected list item
    }

    public void modify() {
        //TODO: write modify code for a selected list item
    }

    public Element getInputElement(){
		return this.inputElement;
	}
  //--- function changed by sukumar-tw for New LDAP service to fetch user details-
    public void loadData(){
    	String[] apinames = null;
		Document[] docInput = null;
		String strLoginId="";
    	Document docLDAPSearchInputQry=YRCXmlUtils.createFromString("<User />");

    	strLoginId=getTargetModel("XPXInternalUser").getAttribute("Loginid");
       	docLDAPSearchInputQry.getDocumentElement().setAttribute("LoginID", strLoginId);	
    			
    	apinames = new String[]{"XPXLDAPSearchDetails"};
    	docInput = new Document[]{docLDAPSearchInputQry};

    	callApis(apinames, docInput);

       	
		
    /*	ArrayList<String> dataList = new ArrayList<String>();
    	dataList.add("EmployeeId");
    	dataList.add("Name");
    	dataList.add("name@gmail.com");
    	return dataList;*/
    }
  //--- End of function changed by sukumar-tw for New LDAP service to fetch user details-
 
 //--function Added by sukumar-tw for restting user data fields   
    public void clearData(){
		setFieldValue("txtName","");
		setFieldValue("txtEmployeeId", "");
		setFieldValue("txtEmailAddr","");
    }
  //--End of function Added by sukumar-tw for restting user data fields 
    private void callApis(String apinames[], Document inputXmls[]) {
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(getFormId());
		ctx.setApiNames(apinames);
		ctx.setInputXmls(inputXmls);
		if (!page.isDisposed())
			callApi(ctx, page);
	}
    
}
