package com.xpedx.sterling.rcp.pca.tasks.myitems.screen;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.myitems.editor.XPXManageMyItemsListEditor;
import com.xpedx.sterling.rcp.pca.userprofile.editor.XPXUserProfileEditor;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXCreateNewMyItemsListPanelBehavior  extends YRCBehavior {

	private XPXCreateNewMyItemsListPopupPanel page;
	Element multiAPIDocElement=null;
	private String myItemsListKey;
	private String formId;
	private String strCustomerPathPrefix = "";
	private Element eleSaveList = null;
	public static String  oldCustomerID = " ";
	/**
	 * Constructor for the behavior class.
	 */

	public XPXCreateNewMyItemsListPanelBehavior(Composite ownerComposite,
			String formId) {
		super(ownerComposite, formId);
		this.formId = formId;
		this.page = (XPXCreateNewMyItemsListPopupPanel) ownerComposite;
	}

	/** Will be triggered on click of get Share List or getChilds */
	public void getSharedList() {
		
		if (YRCPlatformUI.isVoid(getFieldValue("txtCustomerIDSelected"))) {

			YRCPlatformUI.showError("Message", "Please enter any one of the search fields and click Search button.");
			getControl("txtCustomerId").setFocus();
		}
		else
		{
			
			if(getFieldValue("txtCustomerIDSelected")== " " || oldCustomerID.equalsIgnoreCase(getFieldValue("txtCustomerIDSelected")) ){
				
			}else{
				oldCustomerID = getFieldValue("txtCustomerIDSelected");
			Element outXml = getTargetModel("SaveMyItemsList");
			String selectedCustomer = YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(outXml, "/XPEDXMyItemsList"), "CustomerID");

			YRCApiContext apiCtx = new YRCApiContext();
			if(!YRCPlatformUI.isVoid(selectedCustomer)) {
				String[] apinames = {"getCustomerList"};
				Document[] docInput = {
						YRCXmlUtils.createFromString("<Customer CustomerID='"+selectedCustomer+"' />")										
				};
				apiCtx.setApiNames(apinames);
				apiCtx.setInputXmls(docInput);
			}
			apiCtx.setFormId(getFormId());
			callApi(apiCtx);
			}
		}
	}
	
	
	private void callApis(String apinames[], Document inputXmls[]) {
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(formId);
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
					if ("getCustomerList".equals(apiname)) {
						
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						setModel("XPXGetImmediateChildCustomerListService",outXml);
						
						String strCustomerKey = YRCXmlUtils.getAttributeValue(outXml, "/CustomerList/Customer/@CustomerKey");
						String strCustomerID = YRCXmlUtils.getAttributeValue(outXml, "/CustomerList/Customer/@CustomerID");
						String strOrgCode = YRCXmlUtils.getAttributeValue(outXml, "/CustomerList/Customer/@OrganizationCode");
						String strSuffixType = YRCXmlUtils.getAttributeValue(outXml, "/CustomerList/Customer/Extn/@ExtnSuffixType");
						if(YRCPlatformUI.isVoid(strSuffixType) || !"MC".equals(strSuffixType)){
							YRCApiContext apiCtx = new YRCApiContext();
							String cmdName = "XPXGetParentCustomerListService";
							Document docInput = YRCXmlUtils.createFromString("<Customer CustomerID='"+strCustomerID+"' OrganizationCode='"+strOrgCode+"'/>");
							apiCtx.setApiName(cmdName);
							apiCtx.setInputXml(docInput);
							apiCtx.setFormId(getFormId());
							callApi(apiCtx);
						} else {
							this.getChildList();    //--function used to set the values of child nodes in Tree structure
						}
					}
					else if("XPXGetParentCustomerListService".equals(apiname)){
						// Set the Global Variable CustomerPath Prefix
						// XPXGetParentCustomerListService List output will be from Queried_Customer(lower) to Master_Customer(top) 
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						setModel("XPXGetParentCustomerListService", outXml);
						boolean isFirstCustElement = true;
						if(outXml != null && outXml.hasChildNodes())
				        {
				            NodeList childList = outXml.getChildNodes();
				            for(int j = 0; j < childList.getLength(); j++){
			            		if((childList.item(j) instanceof Element) && "Customer".equals(childList.item(j).getNodeName())){
			            			Element eleCustomer = (Element) childList.item(j); 
			            			if(!isFirstCustElement){
			            				if(!YRCPlatformUI.isVoid(strCustomerPathPrefix)){
			            					strCustomerPathPrefix = "|" + strCustomerPathPrefix; 
			            				}
			            				strCustomerPathPrefix = eleCustomer.getAttribute("CustomerID") + strCustomerPathPrefix;
			            			} else {
			            				// This eats up the first Customer Element in the loop
			            				isFirstCustElement = false;
			            			}
			            		}
			            	}
				        }
						
						this.getChildList();    //--function used to set the values of child nodes in Tree structure
					} else if ("createXPEDX_MyItemsList".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						myItemsListKey = outXml.getAttribute("MyItemsListKey");
						setModel("XPEDXMyItemsList",outXml);
						
						openMyItemsListDetailScreen();
						((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(false);
					} else if ("XPXGetCustomerListService".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						setModel("XPXGetCustomerListService",outXml);
						Element CustomerElement = YRCXmlUtils.getXPathElement(outXml, "/CustomerList/Customer");
						String customerIDSelected = CustomerElement.getAttribute("CustomerID");
						setFieldValue("txtCustomerIDSelected", customerIDSelected);
					}
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
	
	public void getChildList(){
		Element childCustomerList = getModel("XPXGetImmediateChildCustomerListService");
		Element customerElement = YRCXmlUtils.getChildElement(childCustomerList, "Customer");
		ArrayList<Element> arrlst=new ArrayList<Element>();
		arrlst.add(customerElement);
		
		page.setTreeValues(null, arrlst); //--function used to set the values of child nodes in Tree structure
	}


	/** Utility to Open My Itmes List Detail screen. */
	private void openMyItemsListDetailScreen() {
		page.getParent().getShell().close();
		Element element = getModel("XPEDXMyItemsList");
        Element eleEditorInput = YRCXmlUtils.getCopy(element, true);
        eleEditorInput.getOwnerDocument().renameNode(eleEditorInput, eleEditorInput.getNamespaceURI(), "XPEDXMyItemsListTmp");
        YRCPlatformUI.openEditor(XPXManageMyItemsListEditor.ID_EDITOR, new YRCEditorInput(eleEditorInput, eleEditorInput, new String[]{"MyItemsListKey"}, ""));
	}

	/** Will be triggred on click of Create button from My Items List */
	public void createMyItemsList() {
		if (YRCPlatformUI.isVoid(getFieldValue("txtCustomerIDSelected"))||YRCPlatformUI.isVoid(getFieldValue("txtListName"))) {

			YRCPlatformUI.showError("MANDATORY_MYITEMS_LIST_FIELDS",
					YRCPlatformUI
							.getString("MANDATORY_MYITEMS_LIST_FIELDS"));
		}
		else
		{
			Element targetModel = getTargetModel("SaveMyItemsList");
			eleSaveList = YRCXmlUtils.getCopy(targetModel);
			page.getTargetModelForUpdateAssignments();
			
			// Validate if MIL is shared to atleast one Customer
			if(this.isMILSharedToAtleastOneCustomer()){
				YRCPlatformUI.showError("SHOULD_BE_SHARED_ATLEAST_TO_ONE_LOCATION_TITLE",
						YRCPlatformUI.getString("SHOULD_BE_SHARED_ATLEAST_TO_ONE_LOCATION_MSG"));
//				getControl("txtCustomerId").setFocus();
				oldCustomerID = " ";
			} else {
        	
				String Createuserid=YRCPlatformUI.getUserElement().getAttribute("Loginid");//--XIRAId-1143
				String strCreatedByUsername = YRCPlatformUI.getUserElement().getAttribute("Username");
				String strProgid = ".YRCApp";
				if(YRCPlatformUI.isVoid(Createuserid)){
					Createuserid = "";
				}
				if(YRCPlatformUI.isVoid(strCreatedByUsername)){
					strCreatedByUsername = "";
				}
				eleSaveList.setAttribute("Createuserid",Createuserid);
				eleSaveList.setAttribute("Createusername", strCreatedByUsername);
				eleSaveList.setAttribute("Createprogid", strProgid);
				eleSaveList.setAttribute("Modifyuserid",Createuserid);
				eleSaveList.setAttribute("Modifyprogid", strProgid);
				
				YRCXmlUtils.getString(eleSaveList);
				YRCApiContext ctx = new YRCApiContext();
				ctx.setFormId(page.getFormId());
				ctx.setApiName("createXPEDX_MyItemsList");
				ctx.setInputXml(eleSaveList.getOwnerDocument());
				if (!page.isDisposed())
					callApi(ctx, page);
			}
		}

	}

	private boolean isMILSharedToAtleastOneCustomer() {
		Element eleMILShareList = null;
		if(null != eleSaveList){
			eleMILShareList = YRCXmlUtils.getChildElement(eleSaveList, "XPEDXMyItemsListShareList", true);
		}
		ArrayList sharedList = YRCXmlUtils.getChildren(eleMILShareList, "XPEDXMyItemsListShare");
		return (0 == sharedList.size());
	}
	
	public void createManageAssignmentInput(String strCustID, String strCustPath, String strDivisionID, boolean action){
		Element eleMILShareList = null;
		if(null != eleSaveList){
			eleMILShareList = YRCXmlUtils.getChildElement(eleSaveList, "XPEDXMyItemsListShareList", true);
		}
		
		Element lineEle = YRCXmlUtils.createFromString("<XPEDXMyItemsListShare CustomerID='"+strCustID+"'/>").getDocumentElement();
		lineEle.setAttribute("CustomerPath", strCustPath);
		if(!YRCPlatformUI.isVoid(strDivisionID))
			strDivisionID = strDivisionID.concat("_M");
			lineEle.setAttribute("DivisionID", strDivisionID);
		
		YRCXmlUtils.importElement(eleMILShareList, lineEle);
	}
	
	/** Search for given Customer Id for which MIL is being created. **/
	public void searchCustomers()
	{
		String[] apinames = {"XPXGetCustomerListService"};
		String customerId = getFieldValue("txtCustomerId");
		String customerName = getFieldValue("txtCustomerName");
		String customerNumber = getFieldValue("txtCustomerNumber");
		if ("".equalsIgnoreCase(customerId) && "".equalsIgnoreCase(customerName) && "".equalsIgnoreCase(customerNumber)) {
			YRCPlatformUI.showError("Message", "Please enter any one of the search fields and click Search button.");
			getControl("txtCustomerId").setFocus();
		}
		/*else
		{
			Document[] docInput = {
					YRCXmlUtils.createFromString("<Customer CustomerID='"+customerId+"' CustomerIDQryType='FLIKE'/>")										
			};
			callApis(apinames, docInput);			
		}*/
		if ((!"".equalsIgnoreCase(customerId)&&!"".equalsIgnoreCase(customerName))|| (!"".equalsIgnoreCase(customerId)&&!"".equalsIgnoreCase(customerNumber))||(!"".equalsIgnoreCase(customerName)&&!"".equalsIgnoreCase(customerNumber))) {
			YRCPlatformUI.showError("Message", "Please enter data into only one of the three search fields.");
			return;
			}
		if (!"".equalsIgnoreCase(customerId)) {
			Document[] docInput = {
					YRCXmlUtils.createFromString("<Customer CustomerID='"+customerId+"' CustomerIDQryType='FLIKE'/>")										
			};
			callApis(apinames, docInput);	
		}
		if (!"".equalsIgnoreCase(customerName)) {
			Element elemModelCustomerName = YRCXmlUtils.createDocument("Customer").getDocumentElement();
			elemModelCustomerName.setAttribute("CallingOrganizationCode", "xpedx");
			elemModelCustomerName.setAttribute("CustomerType", "01");
			Element e1 = YRCXmlUtils.createChild(elemModelCustomerName, "BuyerOrganization");
			e1.setAttribute("OrganizationName", customerName);
			e1.setAttribute("OrganizationNameQryType", "FLIKE");
			Element e2 = YRCXmlUtils.createChild(elemModelCustomerName, "Extn");
			e2.setAttribute("ExtnSuffixType", "MC");
			YRCSharedTaskOutput output = YRCPlatformUI.launchSharedTask("com.xpedx.sterling.rcp.pca.sharedTasks.XPXCustomerSearchSharedTask",elemModelCustomerName);
			Element eleUOMInfo = output.getOutput();
			String customerIdSelected = eleUOMInfo.getAttribute("CustomerIdSelected");
			setFieldValue("txtCustomerIDSelected", customerIdSelected);

		}
		if (!"".equalsIgnoreCase(customerNumber)) {
			Element elemModelCustomerNumber = YRCXmlUtils.createDocument("Customer").getDocumentElement();
			elemModelCustomerNumber.setAttribute("CallingOrganizationCode", "xpedx");
			elemModelCustomerNumber.setAttribute("CustomerType", "01");
			Element BuyerOrganizationElem = YRCXmlUtils.createChild(elemModelCustomerNumber, "BuyerOrganization");
			Element complexQueryElem = YRCXmlUtils.createChild(BuyerOrganizationElem, "ComplexQuery");
			complexQueryElem.setAttribute("Operation", "OR");
			Element orElem = YRCXmlUtils.createChild(complexQueryElem, "Or");
			Element ExpElem = YRCXmlUtils.createChild(orElem, "Exp");
			ExpElem.setAttribute("Name", "OrganizationCode");
			ExpElem.setAttribute("QryType", "LIKE");
			ExpElem.setAttribute("Value", customerNumber);
			Element ExtnElem = YRCXmlUtils.createChild(elemModelCustomerNumber, "Extn");
			ExtnElem.setAttribute("ExtnSuffixType", "MC");
			YRCSharedTaskOutput output = YRCPlatformUI.launchSharedTask("com.xpedx.sterling.rcp.pca.sharedTasks.XPXCustomerSearchSharedTask",elemModelCustomerNumber);
			Element eleUOMInfo = output.getOutput();
			String customerIdSelected = eleUOMInfo.getAttribute("CustomerIdSelected");
			setFieldValue("txtCustomerIDSelected", customerIdSelected);

		}
	}
	
	public String getCustomerPathPrefix() {
		return strCustomerPathPrefix;
	}
}
