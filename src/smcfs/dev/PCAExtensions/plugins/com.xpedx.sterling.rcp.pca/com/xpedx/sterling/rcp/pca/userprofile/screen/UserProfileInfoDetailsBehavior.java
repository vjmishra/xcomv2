package com.xpedx.sterling.rcp.pca.userprofile.screen;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.userprofile.editor.XPXUserProfileEditor;
import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCButtonBindingData;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;
import com.yantra.yfc.rcp.YRCValidationResponse;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class UserProfileInfoDetailsBehavior extends YRCBehavior {

	private Element inputElement;
	private UserProfileInfoDetails page;
	private String orgCode;
	private String userID;
	private String customerKey ;
	private String customerContactID;
	private Document docPOList;
	public static String defaultShipTo;
	private String billToExtnCustomerClass;
	private String b2bCatalogView;
	private String extnDefaultShipTo;
	private String modifyTs;
	private String modifyUserId;
	private boolean isSalesRep=false;
	Element multiAPIDocElement=null;
	Element deleteCCDocElement=null;
	
	public UserProfileInfoDetailsBehavior(
			UserProfileInfoDetails userProfileInfoDetails,
			Object inputObject,Element customerContactEle) {
		
		
		super((Composite) userProfileInfoDetails, userProfileInfoDetails.getFormId(), inputObject);
		this.page = userProfileInfoDetails;
		this.inputElement=((YRCEditorInput) inputObject).getXml();
		orgCode= (String)YRCXmlUtils.getAttribute(this.inputElement, "OrganizationCode");
		userID= (String)YRCXmlUtils.getAttribute(this.inputElement, "UserID");
		customerKey = (String)YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey");
		customerContactID =(String)YRCXmlUtils.getAttribute(this.inputElement, "CustomerContactID");
		this.reInitPage();
		initPage();
		getCurrencyType();
		
	}
	/* Start- For Jira 3264  */
	public void getCurrencyType(){
		Element elemModel = YRCXmlUtils.createDocument("GetCurrenyType").getDocumentElement();
		
		Element attrElemComplex1 = YRCXmlUtils.createChild(elemModel, "CurrencyType");
		
		attrElemComplex1.setAttribute("CurrencyType", "US Dollar");
		attrElemComplex1.setAttribute("CurrencyValue", "USD");

		Element attrElemComplex2 = YRCXmlUtils.createChild(elemModel, "CurrencyType");;

		attrElemComplex2.setAttribute("CurrencyType", "Canadian Dollar");
		attrElemComplex2.setAttribute("CurrencyValue", "CAD");
		
		setModel("GetCurrencyType",elemModel);
	}
	
	//Spending limit and currency type fields are made editable for all user irrespective of their role, according to the comment of Andrea. #JIRA 3409.
	/*public void getApprover(){
		
		Element eleApprover = getModel("XPXCustomerContactIn");
		Element eleBuyerApprover = YRCXmlUtils.getXPathElement(eleApprover, "/CustomerContact/User/UserGroupLists");
		String buyerApprover = eleBuyerApprover.getAttribute("BuyerApprover");
		if("Y".equalsIgnoreCase(buyerApprover)&& buyerApprover!=null){
			getControl("comboCurrencyType").setEnabled(true);
			getControl("txtSpendingLimit").setEnabled(true);
		}
		else{
			getControl("comboCurrencyType").setEnabled(false);
			getControl("txtSpendingLimit").setEnabled(false);
		}
	}*/
	/*End- For Jira 3264 */
	public void getUserGroupInfo(){
		customerKey = (String)YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey");
		String apinames = "getCustomerContactList";
		if(!YRCPlatformUI.isVoid(customerKey)){
			Document docInput = YRCXmlUtils.createFromString("<CustomerContact CustomerKey='"+customerKey+"'/>");
			YRCApiContext ctx = new YRCApiContext();
			ctx.setFormId("com.xpedx.sterling.rcp.pca.userprofile.screen.UserProfileInfoDetails");
			ctx.setApiName(apinames);
			ctx.setInputXml(docInput);
			if (!page.isDisposed())
				callApi(ctx, page);
		}
	}
	
	private void setCoustomerContactModel(Element customerContactElement) {
		this.updateAdditionalAttributes(customerContactElement);
		String viewInvoices =  YRCXmlUtils.getAttributeValue(customerContactElement, "/CustomerContact/Extn/@ExtnViewInvoices");
		if("T".equalsIgnoreCase(viewInvoices)){
			YRCXmlUtils.setAttributeValue(customerContactElement, "/CustomerContact/Extn/@ExtnViewInvoices","Y");
		}
		setModel("XPXCustomerContactIn", customerContactElement);
		setSpendingLmtTextBoxBehaviorOnLoad(YRCXmlUtils.getAttributeValue(customerContactElement, "/CustomerContact/Extn/@ExtnOrderApprovalFlag"));		
		defaultShipTo = getFieldValue("txtCustomerID");
		this.createModelForInvoiceEmails();
		this.getDefaultShipToAddress();
		//Commented for 3409		
		//this.getApprover();
	}

	private void getDefaultShipToAddress() {
		Element eleCustomerContactInfo = getModel("XPXCustomerContactIn");
		String strExtnDefaultShipTo = YRCXmlUtils.getAttributeValue(eleCustomerContactInfo, "/CustomerContact/Extn/@ExtnDefaultShipTo");
		
		if(!YRCPlatformUI.isVoid(strExtnDefaultShipTo)){
			
			
			if(isSalesRep){
				 if(!"".equalsIgnoreCase(this.modifyUserId.trim()))
				 {
					 String[] apinames = {"getDefaultShipToList","getUserList"};
					 Document[] docInput = {
								
								YRCXmlUtils.createFromString("<Customer CustomerID='"+ strExtnDefaultShipTo +"'/>") , 
								YRCXmlUtils.createFromString("<User><Extn ExtnEmployeeId ='"+ this.modifyUserId +"'/></User>") , 
						}; 
					 YRCApiContext ctx = new YRCApiContext();
						ctx.setFormId(getFormId());
						ctx.setApiNames(apinames);
						ctx.setInputXmls(docInput);
						if (!page.isDisposed())
							callApi(ctx, page);
				 }
				 else
				 {		
					 String[] apinames = {"getDefaultShipToList"};
					Document[] docInput = {
							
							YRCXmlUtils.createFromString("<Customer CustomerID='"+ strExtnDefaultShipTo +"'/>"), 
					};
					YRCApiContext ctx = new YRCApiContext();
					ctx.setFormId(getFormId());
					ctx.setApiNames(apinames);
					ctx.setInputXmls(docInput);
					if (!page.isDisposed())
						callApi(ctx, page);
				 }
				
				
				
			}else{
				 if("".equalsIgnoreCase(this.modifyUserId.trim()))
				 {	
					 String[] apinames = {"getDefaultShipToList"};
					Document[] docInput = {
							
							YRCXmlUtils.createFromString("<Customer CustomerID='"+ strExtnDefaultShipTo +"'/>")
					};
					
					YRCApiContext ctx = new YRCApiContext();
					ctx.setFormId(getFormId());
					ctx.setApiNames(apinames);
					ctx.setInputXmls(docInput);
					if (!page.isDisposed())
						callApi(ctx, page);
				 }
				 else
				 {
					 String[] apinames = {"getDefaultShipToList","getUserList"};
					 Document[] docInput = {
								
								YRCXmlUtils.createFromString("<Customer CustomerID='"+ strExtnDefaultShipTo +"'/>") , 
								YRCXmlUtils.createFromString("<User Loginid='"+ this.modifyUserId +"'/>") , 
						};
						
						YRCApiContext ctx = new YRCApiContext();
						ctx.setFormId(getFormId());
						ctx.setApiNames(apinames);
						ctx.setInputXmls(docInput);
						if (!page.isDisposed())
							callApi(ctx, page);
						}
				 }
			
			
			
		}else{

			String[] apinames = {"getUserList"};
			if(isSalesRep){
				Document[] docInput = {
						
					
						YRCXmlUtils.createFromString("<User><Extn ExtnEmployeeId ='"+ this.modifyUserId +"'/></User>") , 
				};
				
				YRCApiContext ctx = new YRCApiContext();
				ctx.setFormId(getFormId());
				ctx.setApiNames(apinames);
				ctx.setInputXmls(docInput);
				if (!page.isDisposed() && "".equalsIgnoreCase(this.modifyUserId.trim()))
					callApi(ctx, page);
				
			}else{
				
			Document[] docInput = {
					
					YRCXmlUtils.createFromString("<User Loginid='"+ this.modifyUserId +"'/>") , 
			};
			
			YRCApiContext ctx = new YRCApiContext();
			ctx.setFormId(getFormId());
			ctx.setApiNames(apinames);
			ctx.setInputXmls(docInput);
			if (!page.isDisposed() && "".equalsIgnoreCase(this.modifyUserId.trim()))
				callApi(ctx, page);
			}
		}
		
	}
	@Override
	public void init() {
		
	}

	public void initPage() {
		invokeSearchCatalogIndexAPI();
//		invokeGetShiptoCustomerListAPI();
		invokeCustomerPOListAPI();
		getUserGroupInfo();

	}	
	private void invokeCustomerPOListAPI() {
		

		String[] apinames = {"getXPXCustContExtn"};
		Document inputXML=YRCXmlUtils.createFromString("<XPXCustomerContactExtn CustomerKey='"+ customerKey +"' />");
		inputXML.getDocumentElement().setAttribute("CustomerContactID", customerContactID);
		Document[] docInput = {
				
				//YRCXmlUtils.createFromString("<XPXCustomerContactExtn CustomerKey='"+ customerKey +"' CustomerContactID='"+customerContactID+"'/>")
				inputXML
		};
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(getFormId());
		ctx.setApiNames(apinames);
		ctx.setInputXmls(docInput);
		if (!page.isDisposed())
			callApi(ctx, page);		
	
		
	}

	private void invokeSearchCatalogIndexAPI() {

		YRCApiContext apiContext = new YRCApiContext();
		apiContext.setApiName("searchCatalogIndex");
		
		/*Begin - Changes made for EB-1680. There would be just one active catalog and it will be of 'xpedx' as its the parent enterprise.*/
		apiContext.setInputXml(YRCXmlUtils.createFromString("<SearchCatalogIndex  CategoryDepth='1' CallingOrganizationCode='xpedx' />"));
		//apiContext.setInputXml(YRCXmlUtils.createFromString("<SearchCatalogIndex  CategoryDepth='1' CallingOrganizationCode='"+orgCode+"' />"));
		/*End - Changes made for EB-1680*/
		
		apiContext.setFormId(getFormId());
		callApi(apiContext);
	}
	
	public void addDefaultShipToAddress(String fieldName){
		if("defaultShipto".equalsIgnoreCase(fieldName)){
			
			// Create Input XML
    		Document docInput = YRCXmlUtils.createDocument("User");
    		
    		Element eleInput = docInput.getDocumentElement();
    		eleInput.setAttribute("UserID", userID);
			if(YRCPlatformUI.isTraceEnabled()){
    			YRCPlatformUI.trace("Shared Task: Launching with Input XML - "+YRCXmlUtils.getString(docInput));
    		}
			
			Element customerContactElement =  getModel("XPXCustomerContactIn");
			
			String b2bCatView = YRCXmlUtils.getAttributeValue(customerContactElement, "/CustomerContact/Extn/@ExtnB2BCatalogView");
			setB2bCatalogView(b2bCatView);
			String defaultShipTo = YRCXmlUtils.getAttributeValue(customerContactElement, "/CustomerContact/Extn/@ExtnDefaultShipTo");
			setExtnDefaultShipTo(defaultShipTo);

			//Launch Shared Task
    		YRCSharedTaskOutput output = YRCPlatformUI.launchSharedTask("com.xpedx.sterling.rcp.pca.sharedTasks.XPXShipToLookupSharedTask", eleInput);
    		
    		// Get the response 
    		Element eleContactPersonInfo = output.getOutput();
    		if(!YRCPlatformUI.isVoid(eleContactPersonInfo)){
    			setModel("DefaultShipToAddress", eleContactPersonInfo);
    			
    			if(YRCPlatformUI.isTraceEnabled()){
					YRCPlatformUI.trace("Shared Task: Output from XPXShipToLookupSharedTask - "+YRCXmlUtils.getString(eleContactPersonInfo));
				}
	    		if(eleContactPersonInfo.getAttribute("CustomerID") != null ) {
	    			setFieldValue("txtCustomerID", eleContactPersonInfo.getAttribute("CustomerID"));
	    			
	    			if(YRCPlatformUI.isVoid(defaultShipTo) && YRCPlatformUI.isVoid(b2bCatView)){
		    			String selectedBilltoCustID=eleContactPersonInfo.getAttribute("BillToCustomerID");
		    			invokeCustomerListAPI(selectedBilltoCustID);
	    			}
	    			
				} else {
					setFieldValue("txtCustomerID", "");
				}
			}
		}

	}
	private void reInitPage() {
		String[] apinames = {"getCustomerDetails"};
		Document[] docInput = {
				YRCXmlUtils.createFromString("<Customer CustomerKey='" + customerKey + "' />")						
		};
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId("com.yantra.pca.ycd.rcp.tasks.customerContactDetails.wizards.YCDCustomerContactWizard");
		ctx.setApiNames(apinames);
		ctx.setInputXmls(docInput);
		if (!page.isDisposed())
			callApi(ctx, page);
	}
	private void callApis(String apinames[], Document inputXmls[]) {
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(page.getFormId());
		ctx.setApiNames(apinames);
		ctx.setInputXmls(inputXmls);
		if (!page.isDisposed())
			callApi(ctx, page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yantra.yfc.rcp.YRCBaseBehavior#handleApiCompletion(com.yantra.yfc.rcp.YRCApiContext)
	 */
	@Override
	public void handleApiCompletion(YRCApiContext ctx) {
		if (ctx.getInvokeAPIStatus() > 0){
			if (page.isDisposed()) {
				YRCPlatformUI.trace("Page is Disposed");
			} else {
				String[] apinames = ctx.getApiNames();
				for (int i = 0; i < apinames.length; i++) {
					String apiname = apinames[i];
					/*if ("getCustomerAssignmentList".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						setModel("XPXCustomerAssignmentList",outXml);						
						
					}*/
					if ("searchCatalogIndex".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						setModel("XPXSearchCatalogIndex",outXml);
						repopulateModel("XPXCustomerContactIn");
					}
					else if ("getUserList".equals(apiname)){
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						String firstName = YRCXmlUtils.getAttributeValue(outXml,"/UserList/User/ContactPersonInfo/@FirstName" );
						//(Element) YRCXPathUtils.evaluate(ctx.getOutputXmls()[i].getDocumentElement(), "UserList/User/ContactPersonInfo",XPathConstants.NODE);
						String lastName =  YRCXmlUtils.getAttributeValue(outXml, "/UserList/User/ContactPersonInfo/@LastName");
						
						YRCXmlUtils.setAttribute(outXml, "ContactModifiedDate", this.modifyTs);						
						//System.out.println(YRCXmlUtils.getString(outXml));
						setModel("UserList" , outXml);
					}
					else if ("getCustomerDetails".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();	
						//customerContactID=customerContactID.replace("'", "\'");
						Element customerContactElement =null;
						ArrayList<Element> customerContactsEleme=YRCXmlUtils.getChildren(YRCXmlUtils.getChildElement(outXml, "CustomerContactList"), "CustomerContact");
						for(Element customerContactEleme :customerContactsEleme)
						{
							if(customerContactEleme.getAttribute("CustomerContactID").equals(customerContactID))
								customerContactElement=customerContactEleme;
						}
							//Element customerContactElement = (Element)YRCXPathUtils.evaluate(outXml, "/Customer/CustomerContactList/CustomerContact[@CustomerContactID='"+customerContactID+"']", XPathConstants.NODE);
						this.setCoustomerContactModel(customerContactElement);
						customerContactID=customerContactID.replace("\'", "'");
					}
					else if("getDefaultShipToList".equals(apiname)){
						Element eleOutput = ctx.getOutputXmls()[i].getDocumentElement();
						Element eleCustomer = (Element) YRCXPathUtils.evaluate(eleOutput, "/CustomerList/Customer", XPathConstants.NODE);
						Document docAddress = YRCXmlUtils.createDocument("ShipTo");
						Element eleAddress = docAddress.getDocumentElement();
						if(!YRCPlatformUI.isVoid(eleCustomer)){
							eleAddress.setAttribute("CustomerID", eleCustomer.getAttribute("CustomerID"));
							eleAddress.setAttribute("OrganizationCode", eleCustomer.getAttribute("OrganizationCode"));
							eleAddress.setAttribute("BuyerOrganizationCode", eleCustomer.getAttribute("CustomerID"));
							Element eleContactPersonInfo = (Element) YRCXPathUtils.evaluate(eleCustomer, "./CustomerAdditionalAddressList/CustomerAdditionalAddress[@IsShipTo='Y' and @IsDefaultShipTo='Y']/PersonInfo", XPathConstants.NODE);
							if(!YRCPlatformUI.isVoid(eleContactPersonInfo))
								YRCXmlUtils.mergeElement(eleContactPersonInfo, eleAddress, false);
						}
						setModel("DefaultShipToAddress",eleAddress);
					}
					else if ("createXPXCustContExtn".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						
					}
					else if ("changeXPXCustContExtn".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						
					}					
					else if ("getXPXCustContExtn".equals(apiname)) {
						this.docPOList=ctx.getOutputXmls()[i];
						Element outXml =null;
						if(!YRCPlatformUI.isVoid(docPOList)){
							outXml=docPOList.getDocumentElement();
							setModel("XPXCustomercontactExtn",outXml);
						
						}
						else{
							Element outXMElement=YRCXmlUtils.createFromString("<XPXCustomercontactExtn/>").getDocumentElement();
							setModel("XPXCustomercontactExtn",outXMElement);
						}
						if(outXml !=null){
						modifyUserId=YRCXmlUtils.getAttributeValue(outXml,"/XPXCustomercontactExtn/@Modifyuserid");
						if(modifyUserId.contains("@CD-")){
							List<String> words = Arrays.asList(this.modifyUserId.split("@"));
							System.out.println(words);
							isSalesRep = true;
							this.modifyUserId = words.get(0);
						}
							modifyTs = YRCXmlUtils.getAttributeValue(outXml,"/XPXCustomercontactExtn/@Modifyts");
						}
						this.createModelForPOList();
						this.createModelForAdditionalEmails();
					}
					else if ("getCustomerContactList".equals(ctx.getApiName())) {
						Element eleCustomerContactList = ctx.getOutputXml().getDocumentElement();
						adminCustomerContact(eleCustomerContactList);						
						this.UpdateCustomer();
						
					}
					else if ("getBilltoExtnCustomerClass".equals(ctx.getApiName())) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();			
						String billToCustomerClass=YRCXmlUtils.getAttributeValue(outXml,"/CustomerList/Customer/Extn/@ExtnCustomerClass" );
						Combo combo = (Combo)getControl("comboPrefferedCatalog");
						
						if("CA".equals(billToCustomerClass)) {
							Button radMatrix = (Button)getControl("radMatrix");
							Button radItem1 = (Button)getControl("radItem1");
							Button radItem2 = (Button)getControl("radItem2");
							Button radItem4 = (Button)getControl("radItem4");							
							radMatrix.setSelection(true);
							radItem1.setSelection(false);
							radItem2.setSelection(false);
							radItem4.setSelection(false);

							YRCButtonBindingData radItemMatrixButtonBindingData= (YRCButtonBindingData)radMatrix.getData("YRCButtonBindingDefination");
							radItemMatrixButtonBindingData.setSourceBinding("3");
							radItemMatrixButtonBindingData.setTargetBinding("3");
							radItemMatrixButtonBindingData.setCheckedBinding("3");
							radMatrix.setData("YRCButtonBindingDefination",radItemMatrixButtonBindingData);
							
							combo.select(3);		
						
						} else {
							Button radMatrix = (Button)getControl("radMatrix");
							Button radItem1 = (Button)getControl("radItem1");
							Button radItem2 = (Button)getControl("radItem2");
							Button radItem4 = (Button)getControl("radItem4");
							radMatrix.setSelection(false);
							radItem1.setSelection(true);
							radItem2.setSelection(false);
							radItem4.setSelection(false);
							
							YRCButtonBindingData radItem1ButtonBindingData= (YRCButtonBindingData)radItem1.getData("YRCButtonBindingDefination");
							radItem1ButtonBindingData.setSourceBinding("0");
							radItem1ButtonBindingData.setTargetBinding("0");
							radItem1ButtonBindingData.setCheckedBinding("0");
							
							if ("CJ".equals(billToCustomerClass)) {
								combo.select(1);
							
							} else if ("CU".equals(billToCustomerClass)){
								combo.select(2);
							
							} else if ("CG".equals(billToCustomerClass)){
								combo.select(4);
							
							}
						
						}
						setBillToExtnCustomerClass(billToCustomerClass);
					}
					 
				}
				if (ctx.getApiName().equals("manageCustomer")) {
					Element outXml = ctx.getOutputXml().getDocumentElement();
					callUpdatePOListAPI();
					this.reInitPage();
					((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(false);
				}
				
			}
		}
		//In case of Invoke API failure
		else if(ctx.getInvokeAPIStatus()==-1){
			Element outXml = ctx.getOutputXml().getDocumentElement();
			if("Errors".equals(outXml.getNodeName())){
					Element errorEle = (Element) outXml.getElementsByTagName(
							"Error").item(0);
					if (!YRCPlatformUI.isVoid(errorEle)) {
						YRCPlatformUI.trace(errorEle
								.getAttribute("ErrorDescription"), outXml);
						YRCPlatformUI.showError("Failed!", errorEle
								.getAttribute("ErrorDescription"));
					}
				}			
		}
		
		super.handleApiCompletion(ctx);
		
	}
	private void adminCustomerContact(Element eleCustomerContactList){
		Element elemModel = YRCXmlUtils.createDocument("CustomerContactList").getDocumentElement();
		NodeList nodCustContact=eleCustomerContactList.getElementsByTagName("CustomerContact");
		ArrayList CustomerContactIdList = new ArrayList();
		for(int i=0;i<nodCustContact.getLength();i++){
			Element eleCust=(Element) nodCustContact.item(i);
			String customerID = eleCust.getAttribute("CustomerContactID");
			System.out.println(customerID);
			if(this.customerContactID.equals(customerID)){
			//	Modifyts="2013-12-05T13:58:34-05:00" Modifyuserid="dave@bh.com">
				this.modifyTs = eleCust.getAttribute("Modifyts");
				if(!"admin".equalsIgnoreCase(eleCust.getAttribute("Modifyuserid"))){
					this.modifyUserId=eleCust.getAttribute("Modifyuserid");
				}
				
				if(modifyUserId!= null && modifyUserId.contains("@CD-")){
					List<String> words = Arrays.asList(this.modifyUserId.split("@"));
					System.out.println("here " + words);
					this.modifyUserId = words.get(0);
					isSalesRep = true;
				}
				
			}
				NodeList nodGroupLists=eleCust.getElementsByTagName("UserGroupLists");			
				for(int j=0;j<nodGroupLists.getLength();j++){
					Element eleGroup=(Element) nodGroupLists.item(j);
					NodeList nodGroupList=eleGroup.getElementsByTagName("UserGroupList");
					for(int k=0;k<nodGroupList.getLength();k++){
						Element eleGroupName=(Element) nodGroupList.item(k);
						String groupId = eleGroupName.getAttribute("UsergroupKey");
						if ("BUYER-APPROVER".equalsIgnoreCase(groupId)){
							CustomerContactIdList.add(customerID);
							k = nodGroupList.getLength();
						}
					}
					
				}
		}
		//Element inputElement = getModel("XPXCustomerContactIn");
		String logINCustomerContactID = this.customerContactID;
		//String logINCustomerContactID = inputElement.getAttribute("CustomerContactID");
		if(CustomerContactIdList.contains(logINCustomerContactID)){
			CustomerContactIdList.remove(logINCustomerContactID);
		}
		Document docInput = YRCXmlUtils.createDocument("AdminCustomerList");
		Element adminCustomerList = docInput.getDocumentElement();
		for (int i=0; i<CustomerContactIdList.size() ;i++){
			Element customerContactList = YRCXmlUtils.createChild(adminCustomerList, "CustomerContact");
			customerContactList.setAttribute("CustomerContactID",(String)CustomerContactIdList.get(i) );
			}
	/*	NodeList nodeCustContact=eleCustomerContactList.getElementsByTagName("CustomerContact");
		for(int i=0;i<nodeCustContact.getLength();i++){
			Element elementCust=(Element) nodCustContact.item(i);
			String customerID = elementCust.getAttribute("CustomerContactID");
			if (!CustomerContactIdList.contains(customerID)){
				eleCustomerContactList.removeChild(elementCust);
				i--;
			}
			
		}*/
		setModel("CustomerContactDetails",adminCustomerList);
	}
	
public void UpdateCustomer(){
	Document inputDoc = YRCXmlUtils.createDocument("UserList");
	System.out.println("here");
	Element inputRestPasswordElement = inputDoc.getDocumentElement();
	//inputRestPasswordElement.setAttribute("ResetType", "Email");
	Element userElement = inputDoc.createElement("User");
	inputRestPasswordElement.appendChild(userElement);
	userElement.setAttribute("Loginid", this.modifyUserId);
   
	String[] apinames = {"getUserList"};
    if(isSalesRep){
		Document[] docInput = {
				
			
				YRCXmlUtils.createFromString("<User><Extn ExtnEmployeeId ='"+ this.modifyUserId +"'/></User>") , 
		};
		
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(getFormId());
		ctx.setApiNames(apinames);
		ctx.setInputXmls(docInput);
		if (!page.isDisposed() && "".equalsIgnoreCase(this.modifyUserId.trim()))
			callApi(ctx, page);
		
	}else{
		
	Document[] docInput = {
			
			YRCXmlUtils.createFromString("<User Loginid='"+ this.modifyUserId +"'/>") , 
	};
	
	YRCApiContext ctx = new YRCApiContext();
	ctx.setFormId(getFormId());
	ctx.setApiNames(apinames);
	ctx.setInputXmls(docInput);
	if (!page.isDisposed() && "".equalsIgnoreCase(this.modifyUserId.trim()))
		callApi(ctx, page);
	}
}
    
  
	


	private void updateAdditionalAttributes(Element customerContactElement) {
		Element eleUserGroupsList = (Element) YRCXPathUtils.evaluate(customerContactElement, "./User/UserGroupLists", XPathConstants.NODE);
		ArrayList listOfAssignedUserGroups = YRCXmlUtils.getChildren(eleUserGroupsList, "UserGroupList");
		for (Object assignedUserGroup : listOfAssignedUserGroups) {
			Element eleAssignedUserGroup = (Element) assignedUserGroup; 
			String strUserGroupKey = eleAssignedUserGroup.getAttribute("UsergroupKey");
			if(YRCPlatformUI.equals("BUYER-USER", strUserGroupKey)){
				eleUserGroupsList.setAttribute("BuyerUser", "Y");
			} else if(YRCPlatformUI.equals("BUYER-ADMIN", strUserGroupKey)){
				eleUserGroupsList.setAttribute("BuyerAdmin", "Y");
			} else if(YRCPlatformUI.equals("BUYER-APPROVER", strUserGroupKey)){
				eleUserGroupsList.setAttribute("BuyerApprover", "Y");
			} else if (YRCPlatformUI.equals("PROCUREMENT-USER", strUserGroupKey)){
				eleUserGroupsList.setAttribute("ProcurementUser", "Y");
			}
		}
		
		Element statusElement = YRCXmlUtils.getXPathElement(customerContactElement, "/CustomerContact/User");
		String stausflag=statusElement.getAttribute("Activateflag");
		if("Y".equals(stausflag))
			 YRCXmlUtils.setAttributeValue(customerContactElement,"/CustomerContact/@Status", "10");
		 else
			 YRCXmlUtils.setAttributeValue(customerContactElement,"/CustomerContact/@Status", "30");
	}

	private void createModelForPOList(){
		Element optionListDoc = YRCXmlUtils.createFromString("<POList/>").getDocumentElement();
		String strPOList=(String)YRCXPathUtils.evaluate(getModel("XPXCustomercontactExtn"), "./@POList", XPathConstants.STRING);
		if(!YRCPlatformUI.isVoid(strPOList)){
		String[] poListSplit = strPOList.split(",");
		for (int i = 0; i < poListSplit.length; i++) {
			Element optionEle = YRCXmlUtils.createChild(optionListDoc, "PO");
			optionEle.setAttribute("PONumber", poListSplit[i]);			
		}
		}
		setModel("XPXPOList",optionListDoc);
	}
	private void createModelForAdditionalEmails(){
		Element optionListDoc = YRCXmlUtils.createFromString("<EmailsList/>").getDocumentElement();
		String strEmailList =(String)YRCXPathUtils.evaluate(getModel("XPXCustomercontactExtn"), "./@AddnlEmailAddrs", XPathConstants.STRING);
		if(!YRCPlatformUI.isVoid(strEmailList)){
		String[] poListSplit = strEmailList.split(",");
		for (int i = 0; i < poListSplit.length; i++) {
			Element optionEle = YRCXmlUtils.createChild(optionListDoc, "Email");
			optionEle.setAttribute("Address", poListSplit[i]);			
		}
		}
		setModel("XPXEmailList",optionListDoc);
	}
	private void createModelForInvoiceEmails() {
		Element optionListDoc = YRCXmlUtils.createFromString("<InvoiceEmailList/>").getDocumentElement();
		String strInvoiceEmailList = (String)YRCXPathUtils.evaluate(getModel("XPXCustomerContactIn"), "./Extn/@ExtnInvoiceEMailID", XPathConstants.STRING);
		if(!YRCPlatformUI.isVoid(strInvoiceEmailList)){
		String[] invoiceListSplit = strInvoiceEmailList.split(",");
		for (int i = 0; i < invoiceListSplit.length; i++) {
			Element optionEle = YRCXmlUtils.createChild(optionListDoc, "InvoiceEmail");
			optionEle.setAttribute("InvoiceAddress", invoiceListSplit[i]);			
		}
		}
		setModel("XPXInvoiceEmailList",optionListDoc);
	}
	private void callApi(String apinames, Document inputXml) {
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(page.getFormId());
		ctx.setApiName(apinames);
		ctx.setInputXml(inputXml);
		if (!page.isDisposed())
			callApi(ctx, page);
	}	
	public void removePOs(){
		Element tempPOList = getModel("XPXPOList");
		Table temp2 = page.itemPOList;
        for(int i = temp2.getSelectionCount() - 1; i >= 0; i--)
        {
            TableItem ti = temp2.getItem(temp2.getSelectionIndices()[i]);
            if(!YRCPlatformUI.isVoid(ti))
            {
                Element poEle = (Element)ti.getData();
                tempPOList.removeChild(poEle);
                
            }
        }
        setModel("XPXPOList", tempPOList);        
	}
	public void addPONumber(){
		Element tempPOList = getModel("XPXPOList");
		String enteredPO = getFieldValue("txtPOList");
		if(!YRCPlatformUI.isVoid(enteredPO)){
			Element optionEle = YRCXmlUtils.createChild(tempPOList, "PO");
			optionEle.setAttribute("PONumber", enteredPO);
			setFieldValue("txtPOList", "");
		}
		setModel("XPXPOList", tempPOList);        
	}
	public void removeEmails(){
		Element tempEmailList = getModel("XPXEmailList");
		Table temp2 = page.orderConfirmList;
		
        for(int i = temp2.getSelectionCount() - 1; i >= 0; i--)
        {
            TableItem ti = temp2.getItem(temp2.getSelectionIndices()[i]);
            if(!YRCPlatformUI.isVoid(ti))
            {
                Element emailEle = (Element)ti.getData();
                tempEmailList.removeChild(emailEle);
                
            }
        }
        setModel("XPXEmailList", tempEmailList);        
	}
	public void addEmail(){
		String enteredEmail = getFieldValue("txtOrderConfirmationList");
		Element tempEmailList = getModel("XPXEmailList");
		NodeList nodeList = tempEmailList.getElementsByTagName("Email");
		String strAddressAttrName = "Address";
		if(XPXUtils.validateEmail(enteredEmail))
        {
			if(this.doesEmailExistAlready(enteredEmail, nodeList, strAddressAttrName)){
				setFieldInError("txtOrderConfirmationList", new YRCValidationResponse(YRCValidationResponse.YRC_VALIDATION_ERROR,"Email Already Exists."));
	        	String exception = "This Email Already Exists.";
	        	YRCPlatformUI.showError("Error", exception);
	        	System.out.println("Email Match Exists: "+strAddressAttrName+" Yes");
	        	return ;
			} else{
				if(!YRCPlatformUI.isVoid(enteredEmail)){
					Element optionEle = YRCXmlUtils.createChild(tempEmailList, "Email");
					optionEle.setAttribute("Address", enteredEmail);
					setFieldValue("txtOrderConfirmationList", "");
				}
				setModel("XPXEmailList", tempEmailList);
				setFieldInError("txtOrderConfirmationList", new YRCValidationResponse(YRCValidationResponse.YRC_VALIDATION_ERROR,"Email ID added."));
			}
	    }
        else
        {	
        	setFieldInError("txtOrderConfirmationList", new YRCValidationResponse(YRCValidationResponse.YRC_VALIDATION_ERROR,"Invalid Email ID"));
        	String exception = "Invalid Email Format.";
        	YRCPlatformUI.showError("Error", exception);
        }
    }

	private boolean doesEmailExistAlready(String enteredEmail,
		NodeList nodeList, String strAddressAttrName) {
		int length = nodeList.getLength();
		int count = 0;
		boolean emailMatchExists = false;
		for (count = 0; count < length; count++) {
			Element assignElement = (Element) nodeList.item(count);
			String currentString = assignElement
					.getAttribute(strAddressAttrName);
			if (enteredEmail.equals(currentString)) {
				emailMatchExists = true;
			}
		}
		return emailMatchExists;
	}
	
	public void removeInvoiceEmails(){
		Element tempEmailList = getModel("XPXInvoiceEmailList");
		Table temp2 = page.invoiceList;
		
        for(int i = temp2.getSelectionCount() - 1; i >= 0; i--)
        {
            TableItem ti = temp2.getItem(temp2.getSelectionIndices()[i]);
            if(!YRCPlatformUI.isVoid(ti))
            {
                Element emailEle = (Element)ti.getData();
                tempEmailList.removeChild(emailEle);
                
            }
        }
        setModel("XPXInvoiceEmailList", tempEmailList);        
	}
	public void addInvoiceEmail() {
		
		Element tempEmailList1 = getModel("XPXInvoiceEmailList");
		String enteredEmail = getFieldValue("txtInvoiceEmailId");
		NodeList nodeList = tempEmailList1.getElementsByTagName("InvoiceEmail");
		String strAddressAttrName = "InvoiceAddress";
		if(XPXUtils.validateEmail(enteredEmail))
        {
			if(this.doesEmailExistAlready(enteredEmail, nodeList, strAddressAttrName)){
				setFieldInError("txtInvoiceEmailId", new YRCValidationResponse(YRCValidationResponse.YRC_VALIDATION_ERROR,"Email Already Exists."));
	        	String exception = "This Email Already Exists.";
	        	YRCPlatformUI.showError("Error", exception);
	        	return ;
			} else{
				if(!YRCPlatformUI.isVoid(enteredEmail)){
					Element optionEle = YRCXmlUtils.createChild(tempEmailList1, "InvoiceEmail");
					optionEle.setAttribute("InvoiceAddress", enteredEmail);
					setFieldValue("txtInvoiceEmailId", "");
				}
				setModel("XPXInvoiceEmailList", tempEmailList1);      
			setFieldInError("txtInvoiceEmailId", new YRCValidationResponse(YRCValidationResponse.YRC_VALIDATION_ERROR,"Email ID added."));
			}
	    }
        else
        {	
       	String exception = "Invalid Email Format.";
        	
        	YRCPlatformUI.showError("Error", exception);
        	
        	setFieldInError("txtInvoiceEmailId", new YRCValidationResponse(YRCValidationResponse.YRC_VALIDATION_ERROR,"Invalid Email ID"));
        	       }
		
	}
	
	public void resetCustomPassword()
	{
		//form the input Element
		Document inputDoc = YRCXmlUtils.createDocument("ResetPassword");
		Element inputRestPasswordElement = inputDoc.getDocumentElement();
		inputRestPasswordElement.setAttribute("ResetType", "Email");
		Element userElement = inputDoc.createElement("User");
		inputRestPasswordElement.appendChild(userElement);
		Element ExistingoutPut= getTargetModel("XPXResultOut");
		String loginID = ExistingoutPut.getAttribute("UserID");
		userElement.setAttribute("Loginid", loginID);
		String newUserStatus = ExistingoutPut.getAttribute("Status");
		Element inputElement = getModel("XPXCustomerContactIn");		
		String origUserStatus = YRCXmlUtils.getAttributeValue(inputElement, "/CustomerContact/@Status");
		if((newUserStatus!=null && newUserStatus.equals("30")) || (origUserStatus!=null && origUserStatus.equals("30"))){
			YRCPlatformUI.showError("Reset Password",YRCPlatformUI.getString("Reset_Password_For_Susp_User_Error"));
		}else{
			YRCApiContext context = new YRCApiContext();
		    context.setApiName("requestPasswordReset");
		    context.setFormId(getFormId());
		    context.setInputXml(inputDoc);
		    callApi(context);
		}
	}
	public Element addNonBindedAttribute(){
		Element ExistingoutPut= getTargetModel("XPXResultOut");
		Document outputDocument = formNewInput(ExistingoutPut);
		Element outPut = outputDocument.getDocumentElement();
		Element elePOList = getModel("XPXPOList");
		NodeList nl= elePOList.getElementsByTagName("PO");
		String strPOList="";
		String strPO="";
		for(int counter=0;counter < nl.getLength(); counter ++){
			Element POElement = (Element)nl.item(counter);
			//Modified for EB 1178 to set CC & WC PO data comma seperated
			strPO =POElement.getAttribute("PONumber");
			if(!YRCPlatformUI.isVoid(strPOList)){
				strPOList=strPOList+strPO+",";
			}
			else{
				strPOList=strPO+",";
			}
			//strPOList =strPOList+POElement.getAttribute("PONumber");
		}
		Element eleEmailList = getModel("XPXEmailList");
		NodeList nl1= eleEmailList.getElementsByTagName("Email");
		String strEmailList="";
		for(int counter=0;counter < nl1.getLength(); counter ++){
			Element POElement = (Element)nl1.item(counter);
			if(!YRCPlatformUI.isVoid(strEmailList))
				strEmailList=strEmailList+",";
			strEmailList =strEmailList+POElement.getAttribute("Address");
		}
		Element eleInvoiceEmailList = getModel("XPXInvoiceEmailList");
		NodeList invoiceList= eleInvoiceEmailList.getElementsByTagName("InvoiceEmail");
		String strInvoiceEmailList="";
		for(int counter=0;counter < invoiceList.getLength(); counter ++){
			Element InvoiceElement = (Element)invoiceList.item(counter);
			if(!YRCPlatformUI.isVoid(strInvoiceEmailList))
				strInvoiceEmailList=strInvoiceEmailList+",";
			strInvoiceEmailList =strInvoiceEmailList+InvoiceElement.getAttribute("InvoiceAddress");
		}
		String status=ExistingoutPut.getAttribute("Status");
		if("10".equals(status)){
			YRCXmlUtils.setAttributeValue(outPut, "/CustomerContact/User/@Activateflag","Y");
		}
		else{
			YRCXmlUtils.setAttributeValue(outPut, "/CustomerContact//User/@Activateflag","N");
		}
	//	YRCXmlUtils.setAttributeValue(outPut, "/CustomerContact/Extn/@ExtnPOList",strPOList);
	//	YRCXmlUtils.setAttributeValue(outPut, "/CustomerContact/Extn/@ExtnAddnlEmailAddrs",strEmailList);
		YRCXmlUtils.setAttributeValue(outPut, "/CustomerContact/Extn/@ExtnInvoiceEMailID",strInvoiceEmailList);
		return outPut;
		
	}
	
	private Document formNewInput(Element outputElement)
	{
		String inputstring = YRCXmlUtils.getString(outputElement);
		Document inputDoc = YRCXmlUtils.createFromString(inputstring);
		NodeList userList = inputDoc.getElementsByTagName("UserGroupLists");
		int userLength = userList.getLength();
		if(userLength != 0 ){
			Element userGroupListsElement = (Element)userList.item(0);
			userGroupListsElement.setAttribute("Reset", "Y");
			if(userGroupListsElement.hasAttribute("BuyerUser"))
			{
				if(userGroupListsElement.getAttribute("BuyerUser").equals("Y"))
				{
					Element userGroupElement = inputDoc.createElement("UserGroupList");
					userGroupElement.setAttribute("UsergroupKey", "BUYER-USER");
					userGroupListsElement.appendChild(userGroupElement);
				}
				
			}
			if(userGroupListsElement.hasAttribute("BuyerAdmin"))
			{
				if(userGroupListsElement.getAttribute("BuyerAdmin").equals("Y"))
				{
					Element userGroupElement = inputDoc.createElement("UserGroupList");
					userGroupElement.setAttribute("UsergroupKey", "BUYER-ADMIN");
					userGroupListsElement.appendChild(userGroupElement);
				}
				
			}
			if(userGroupListsElement.hasAttribute("BuyerApprover"))
			{
				if(userGroupListsElement.getAttribute("BuyerApprover").equals("Y"))
				{
					Element userGroupElement = inputDoc.createElement("UserGroupList");
					userGroupElement.setAttribute("UsergroupKey", "BUYER-APPROVER");
					userGroupListsElement.appendChild(userGroupElement);
				}
				
			}
			if(userGroupListsElement.hasAttribute("ProcurementUser"))
			{
				if(userGroupListsElement.getAttribute("ProcurementUser").equals("Y"))
				{
					Element userGroupElement = inputDoc.createElement("UserGroupList");
					userGroupElement.setAttribute("UsergroupKey", "PROCUREMENT-USER");
					userGroupListsElement.appendChild(userGroupElement);
				}
				
			}
		}
		return inputDoc;
	}
	public void updateAction(){
		Element inputElement = getModel("XPXCustomerContactIn");
		String oldMailId=inputElement.getAttribute("EmailID");
		String changedMailId=getFieldValue("txtEmailAddress");
		if(!oldMailId.equalsIgnoreCase(changedMailId)){
			UpdateEMailAddress(oldMailId,changedMailId); //--function used to call API for MailId change
		}
		
		Element targetModel = addNonBindedAttribute();
		//invoking hasValidInputData()  method to validate input Fields on User profile Info Screen 
		if(!hasError() && hasValidInputData()){
			if(!YRCPlatformUI.isVoid(customerContactID)){
				targetModel.setAttribute("CustomerContactID", customerContactID);
				targetModel.setAttribute("ApproverUserId", getFieldValue("comboPrimApprover"));
				targetModel.setAttribute("ApproverProxyUserId", getFieldValue("comboAlterApprover"));
				targetModel.setAttribute("SpendingLimit", getFieldValue("txtSpendingLimit"));
				targetModel.setAttribute("SpendingLimitCurrency", getFieldValue("comboCurrencyType"));		
				Element eleUser = YRCXmlUtils.getXPathElement(targetModel, "/CustomerContact/User");
				//Contact person info added under user element to update values at user label
				Element eleContactPersonInfo = YRCXmlUtils.createChild(eleUser, "ContactPersonInfo");
				eleContactPersonInfo.setAttribute("EMailID", getFieldValue("txtEmailAddress"));
				eleContactPersonInfo.setAttribute("DayFaxNo", getFieldValue("txtFaxNumber"));
				eleContactPersonInfo.setAttribute("DayPhone", getFieldValue("txtPhone"));
				eleContactPersonInfo.setAttribute("FirstName", getFieldValue("txtFirstName"));
				eleContactPersonInfo.setAttribute("LastName", getFieldValue("txtLastName"));
				YRCApiContext ctx = new YRCApiContext();
				ctx.setApiName("manageCustomer");
				ctx.setInputXml(createManageCustomerOutputXml(targetModel).getOwnerDocument());
				ctx.setFormId(page.getFormId());
				ctx.setShowError(false);
				ctx.setUserData("isRefreshReqd", String.valueOf(false));
				callApi(ctx, page);
				((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
				
			}
		} else {
			showError();
		}
//		}
	}
	
	public void deleteAction(){
		
		//GetCustomer Assignment List
		ArrayList assignedList = null;
		assignedList =XPXUtils.getAssignedCustomerList();
		//Delete Customer Assignment List
		createManageAssignmentInput(assignedList);
		String message = "Do you want to delete user "+this.userID+" ?";
		if(YRCPlatformUI.getConfirmation("Warning", message )){
		try{
		YRCApiContext ctx = new YRCApiContext();
		String[] apinames ={"XPXManageCustomerAndAssignmentAPIService","manageCustomer"/*,"deleteUserHierarchy"*/};
		ctx.setApiNames(apinames);
		
		Document[] docInput = {
				multiAPIDocElement.getOwnerDocument(),
				YRCXmlUtils.createFromString("<Customer CustomerID='"+XPXUtils.getMsapCustomerOrgID()+"' OrganizationCode='"+orgCode+"'><CustomerContactList><CustomerContact CustomerContactID='"+userID+"' Operation ='Delete'></CustomerContact></CustomerContactList></Customer> "),
				//YRCXmlUtils.createFromString("<User DisplayUserID='"+userID+"'  Loginid='"+userID+"' />  "),	
			//	YRCXmlUtils.createFromString("<CustomerAssignment UserId='" + userID + "'/>")
		};

		ctx.setInputXmls(docInput);
		ctx.setFormId(page.getFormId());
		ctx.setShowError(true);
		ctx.setUserData("isRefreshReqd", String.valueOf(true));
		//callApi(ctx, page);
		callApis(apinames, docInput);
		multiAPIDocElement = null;
		apinames=null;
		docInput=null;
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor((YRCEditorPart)YRCDesktopUI.getCurrentPart(), true);	
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor((YRCEditorPart)YRCDesktopUI.getCurrentPart(), true);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor((YRCEditorPart)YRCDesktopUI.getCurrentPart(), true);
	//	PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor((YRCEditorPart)YRCDesktopUI.getCurrentPart(), true);
		}catch(Exception e){
			System.out.println("Exception" + e);
		}
		}else{
			
		}
	}

	
	
	private void saveChanges(List<String> wList, String operation,Element customerAssignmentListElem) {
		
		for (int index = 0; index < wList.size(); index++) {
			try {
				if(wList.get(index) != null && wList.get(index).trim().length() ==0 )
					continue;
				Element customerAssignmentElem=YRCXmlUtils.createChild(customerAssignmentListElem, "CustomerAssignment");
				customerAssignmentElem.setAttribute("CustomerID", wList.get(index));
				customerAssignmentElem.setAttribute("OrganizationCode", XPXUtils.getMsapCustomerOrgID());
				customerAssignmentElem.setAttribute("UserId", userID);
				customerAssignmentElem.setAttribute("Operation", operation);
				} catch (Exception ex) {
				System.out.println("***********Record already exists");
			}
		}
	}
	
	public void createManageAssignmentInput(List<String> wList){
		if(null ==multiAPIDocElement){
		multiAPIDocElement = YRCXmlUtils.createDocument("ManageCustomerAndAssignment").getDocumentElement();
		
		}
		multiAPIDocElement.setAttribute("IgnoreOrdering", "Y");
		Element custAssignmentele=(Element)multiAPIDocElement.getElementsByTagName("CustomerAssignmentList").item(0);
		if(custAssignmentele == null)
		{
			custAssignmentele= YRCXmlUtils.createChild(multiAPIDocElement, "CustomerAssignmentList");
		}
		
		saveChanges(wList, "Delete",custAssignmentele);
		System.out.println("Final XML : " + YRCXmlUtils.getString(multiAPIDocElement));
	}	
	
	
	
	 //including this method to validate input fields like MinOrdAmt/MaxOrdAmt
	private boolean hasValidInputData()
	{  
		boolean hasValidData=true;
		Element targetModel =  getTargetModel("XPXResultOut");

		//Declations
		String minAmount="0.0";
		String maxAmount="0.0";
		BigDecimal minAmountDecimal,maxAmountDecimal,checkOverallTotal;
		
		// Initializations
		minAmount = YRCXmlUtils.getAttributeValue(targetModel, "/CustomerContact/Extn/@ExtnMinOrderAmount");
		maxAmount = YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(targetModel, "/CustomerContact/Extn"), "ExtnMaxOrderAmount");
		if(YRCPlatformUI.isVoid(minAmount)){
			minAmount="0.0";
		}
		if(YRCPlatformUI.isVoid(maxAmount)||maxAmount.equals("0")||maxAmount.equals("0.00")){
			maxAmount="0.0";
		}
		try{
			minAmountDecimal = new BigDecimal(minAmount);
		}
		catch(Exception e)
		{
			YRCPlatformUI.showWarning("warning", "INVALID DATA !!");
	    	getControl("txtMinOrderAmount").setFocus();
		    return false;	
		}
		try{
			maxAmountDecimal = new BigDecimal(maxAmount);
		}
		catch(Exception e)
		{
			YRCPlatformUI.showWarning("warning", "INVALID DATA !!");
	    	getControl("txtMaxOrderAmount").setFocus();
		    return false;	
		}

		if((!"0.0".equals(minAmount)) && (!"0.0".equals(maxAmount))){
	    	if(maxAmountDecimal.compareTo(minAmountDecimal)<0 )
		    {
		    	YRCPlatformUI.showWarning("warning", "Max. Order Amount should be GREATER THAN Min. Order Amount.");
		    	getControl("txtMaxOrderAmount").setFocus();
			    return false;
		    }
	    }
		/*Start- For Jira 3264 */
		int spendLimit;
		String spendingLimit=getFieldValue("txtSpendingLimit");
		if(spendingLimit != null && !spendingLimit.equalsIgnoreCase("")){
			try
			{
			  double d = new Double(spendingLimit);
			  spendLimit = (int)d;
			  if(spendLimit<1){
					YRCPlatformUI.showWarning("SpendingLimit", "Minimum Spending Limit is 1");
					getControl("txtSpendingLimit").setFocus();
					return false;
				}
				else if(spendLimit>999999){
					YRCPlatformUI.showWarning("SpendingLimit", "Maximum Spending Limit is 999999");
					getControl("txtSpendingLimit").setFocus();
					return false;
				}
			}
			catch(NumberFormatException e)
			{
				YRCPlatformUI.showWarning("warning", "Please Enter Spending Limit in Numerical between 1 to 999999");
				getControl("txtSpendingLimit").setFocus();
				return false;
			}
		}
		/*End- For Jira 3264 */
		
		/*Start- For Jira XB-691 */
		String chkApproveOrders=getFieldValue("chkApproveOrders");
		if("Y".equals(chkApproveOrders))
		{
			String primaryApprover=getFieldValue("comboPrimApprover");
			if(YRCPlatformUI.isVoid(primaryApprover))
			{
				YRCPlatformUI.showWarning("PrimaryApprover", "Please select a Primary Approver");
				getControl("comboPrimApprover").setFocus();
				return false;
			}			
		}		
		/*End- For Jira XB-691*/
		
	    return hasValidData;
	}


	
	private void callUpdatePOListAPI() {
		Element inputElement = getModel("XPXCustomerContactIn");
		String customerContactId=inputElement.getAttribute("CustomerContactID");
		
		String[] apinames = null;
		Document[] docInput = null;
		String strPOList="";
		String strPO = "";
		Element elePOList = getModel("XPXPOList");
		NodeList nl= elePOList.getElementsByTagName("PO");
		for(int counter=0;counter < nl.getLength(); counter ++){
			Element POElement = (Element)nl.item(counter);
			//Modified for EB 1178 to set CC & WC PO data comma seperated
			strPO =POElement.getAttribute("PONumber");
			if(!YRCPlatformUI.isVoid(strPOList)){
				strPOList=strPOList+strPO+",";
			}
			else{
				strPOList=strPO+",";
			}
			//strPOList =strPOList+POElement.getAttribute("PONumber");
		}
		
		Element eleEmailList = getModel("XPXEmailList");
		NodeList nl1= eleEmailList.getElementsByTagName("Email");
		String strEmailList="";
		for(int counter=0;counter < nl1.getLength(); counter ++){
			Element POElement = (Element)nl1.item(counter);
			if(!YRCPlatformUI.isVoid(strEmailList))
				strEmailList=strEmailList+",";
			strEmailList =strEmailList+POElement.getAttribute("Address");
		}
		
		Document docPOListInput=YRCXmlUtils.createFromString("<XPXCustomerContactExtn />");

		docPOListInput.getDocumentElement().setAttribute("CustomerKey", customerKey);
    	docPOListInput.getDocumentElement().setAttribute("CustomerContactID", customerContactId);
    	docPOListInput.getDocumentElement().setAttribute("POList", strPOList);
    	docPOListInput.getDocumentElement().setAttribute("AddnlEmailAddrs", strEmailList);
    	
    	if(YRCPlatformUI.isVoid(docPOList))
    		apinames = new String[]{"createXPXCustContExtn"};
    	else
    		apinames = new String[]{"changeXPXCustContExtn"};
    	
    	docInput = new Document[]{docPOListInput};
    	callApis(apinames, docInput);
		
	}
	/** --function to call API for mailId change-- **/
	private void UpdateEMailAddress(String oldMailId, String changedMailId) {
		String[] apinames = null;
		Document[] docInput = null;
		String strEnterpriseCode = "";
		strEnterpriseCode = getModel("UserNameSpace").getAttribute("EnterpriseCode");
		String entryType=XPXConstants.ENTRY_TYPE_EMAIL_UPDATE;
		Document docLDAPSearchInputQry=YRCXmlUtils.createFromString("<UserUpdateEmail />");
/* EB-2805 Call Center user updates Saalfeld profile and user want to trigger the Saalfeld user profile updated email template */
    	docLDAPSearchInputQry.getDocumentElement().setAttribute("BrandName", orgCode);
    	docLDAPSearchInputQry.getDocumentElement().setAttribute("EntryType", entryType);
    	docLDAPSearchInputQry.getDocumentElement().setAttribute("OldEmailID", oldMailId);
    	docLDAPSearchInputQry.getDocumentElement().setAttribute("newEmailID", changedMailId);
    	docLDAPSearchInputQry.getDocumentElement().setAttribute("SellerOrganizationCode", orgCode);
    	apinames = new String[]{"XPXSendUserUpdateEmailService"};
    	docInput = new Document[]{docLDAPSearchInputQry};
    	callApis(apinames, docInput);
		
	}

	private Element createManageCustomerOutputXml(Element results){
		
		Element targetDoc = YRCXmlUtils.createFromString("<Customer CustomerKey='" + customerKey + "'/>").getDocumentElement();
		Element targetContactList = YRCXmlUtils.createChild(targetDoc, "CustomerContactList");
		YRCXmlUtils.importElement(targetContactList, results);
		return targetDoc;
	}
	
	public void setSpendingLmtTextBoxBehaviorOnLoad(String extnOrderApprovalFlag){
		
		Control controlSpendingLimit=getControl("txtSpendingLimit");
		
		if(!YRCPlatformUI.isVoid(controlSpendingLimit)){			
			Text txtSpendingLimit = (Text)controlSpendingLimit;			
			if("Y".equalsIgnoreCase(extnOrderApprovalFlag)){
				txtSpendingLimit.setText("");
				txtSpendingLimit.setEnabled(false);
			
			} else{
				txtSpendingLimit.setEnabled(true);
				
			}			
		}		
	}	
	
//	private void createModelForRecieveOrderConfirmationEmails(){
//	Element optionListDoc = YRCXmlUtils.createFromString("<OptionList/>").getDocumentElement();
//	Element optionEle = YRCXmlUtils.createChild(optionListDoc, "Option");
//	optionEle.setAttribute("OptionType", "None");
//	optionEle = YRCXmlUtils.createChild(optionListDoc, "Option");
//	optionEle.setAttribute("OptionType", "HTML");
//	optionEle = YRCXmlUtils.createChild(optionListDoc, "Option");
//	optionEle.setAttribute("OptionType", "Text");
//	setModel("XPXRecieveOrderConfirmationEmails",optionListDoc);
//}

	private void invokeCustomerListAPI(String billtoCustID ) {
		String[] apinames = { "getBilltoExtnCustomerClass" };
		Document[] docInput = { YRCXmlUtils.createFromString("<Customer CustomerID='" + billtoCustID+ "'/>") };
		
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(getFormId());
		ctx.setApiNames(apinames);
		ctx.setInputXmls(docInput);
		if (!page.isDisposed())
			callApi(ctx, page);

	}
	
	public String getBillToExtnCustomerClass() {
		return billToExtnCustomerClass;
	}
	
	public void setBillToExtnCustomerClass(String billToExtnCustomerClass) {
		this.billToExtnCustomerClass = billToExtnCustomerClass;
	}
	
	public String getB2bCatalogView() {
		return b2bCatalogView;
	}
	public void setB2bCatalogView(String catalogView) {
		b2bCatalogView = catalogView;
	}
	public String getExtnDefaultShipTo() {
		return extnDefaultShipTo;
	}
	public void setExtnDefaultShipTo(String extnDefaultShipTo) {
		this.extnDefaultShipTo = extnDefaultShipTo;
	}

}