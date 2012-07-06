package com.xpedx.sterling.rcp.pca.userprofile.screen;

import java.util.ArrayList;

import javax.xml.xpath.XPathConstants;

import org.eclipse.jface.viewers.deferred.SetModel;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.userprofile.editor.XPXUserProfileEditor;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class CustomerAssignmentPanelBehavior extends YRCBehavior {
	private Element inputElement;
	private Element customerContactEle;
	private CustomerAssignmentPanel page;
	private String userID;
	String organizationCode;
	Element multiAPIDocElement=null;
	private String UserOrgCode;
	private String customerContactID;
	private String customerKey;
	private Object inputObject;
	private ArrayList assignedList;
	private boolean extnDefaultShipTo = false;
	public CustomerAssignmentPanelBehavior(
			CustomerAssignmentPanel customerAssignmentPanel,
			Object inputObject,Element customerContactEle) {
		
		super((Composite) customerAssignmentPanel, customerAssignmentPanel.getFormId(), inputObject);
		this.page = customerAssignmentPanel;
		this.inputObject = inputObject; 
		this.customerContactEle = customerContactEle;
		this.inputElement=((YRCEditorInput) inputObject).getXml();
		setModel("XPXCustomerContactIn",customerContactEle);
		userID= (String)YRCXmlUtils.getAttribute(this.inputElement, "UserID");
		organizationCode=(String)YRCXmlUtils.getAttribute(this.inputElement, "OrganizationCode");
		Element userElement = YRCXmlUtils.getXPathElement(getModel("XPXCustomerContactIn"),"/CustomerContact/User");
		UserOrgCode= (String)YRCXmlUtils.getAttribute(userElement, "OrganizationKey");
		customerContactID =(String)YRCXmlUtils.getAttribute(this.inputElement, "CustomerContactID");
		customerKey = (String)YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey");
		initPage();
	}

	public void initPage() {
		String[] apinames = {"getCustomerAssignmentList","getCustomerList"};
		Document[] docInput = {
				YRCXmlUtils.createFromString("<CustomerAssignment UserId='" + userID + "'/>"),
				YRCXmlUtils.createFromString("<Customer CustomerID='"+UserOrgCode+"' OrganizationCode='"+organizationCode+"'/>")										
		};
		callApis(apinames, docInput);
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
					if ("getCustomerAssignmentList".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						ArrayList list = YRCXmlUtils.getChildren(outXml,"CustomerAssignment");
						assignedList = null;
						if(null == assignedList){
							assignedList = new ArrayList();
						}
						for (Object eleCustAssignment : list) {
							String strCustID = YRCXmlUtils.getAttributeValue((Element)eleCustAssignment, "CustomerAssignment/Customer/@CustomerID");
							assignedList.add(strCustID);
						}
						page.resetTreeAssignedValues(assignedList);
						setModel("XPXGetCustomerAssignmentList",outXml);
						getCustomerAssignment();
						
					} else if ("getCustomerList".equals(apiname)) {
						System.out.println("its inside the api call-->>");
					
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						setModel("XPXGetImmediateChildCustomerListService",outXml);
						getChildList();    //--function used to set the values of child nodes in Tree structure
					} else if ("multiApi".equals(apiname)) {
						 getCustomerAssignmentsAfterUpdate();
						((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(false);
					}
					  else if ("manageCustomer".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(false);
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
	
	


	private void getCustomerAssignmentsAfterUpdate()
	{
		String[] apinames = {"getCustomerAssignmentList"};
		Document[] docInput = {
				YRCXmlUtils.createFromString("<CustomerAssignment UserId='" + userID + "'/>"),
		};
		callApis(apinames, docInput);
	
	}
	
	public void callManageCustomer()
	{
		Element targetModel = getModel("XPXCustomerContactIn");
		if(!YRCPlatformUI.isVoid(customerContactID)){
		targetModel.setAttribute("CustomerContactID", customerContactID);		
		if(extnDefaultShipTo){
			extnDefaultShipTo = false;
			Element eleExtn = YRCXmlUtils.getXPathElement(targetModel, "/CustomerContact/Extn");
			eleExtn.setAttribute("ExtnDefaultShipTo","");
		}
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(page.getFormId());
		ctx.setApiName("manageCustomer");
		ctx.setInputXml(createManageCustomerOutputXml(targetModel).getOwnerDocument());
		if (!page.isDisposed())
			callApi(ctx, page);
		}
	}
	
	public void getCustomerAssignment(){
		String defaultShipTo = UserProfileInfoDetailsBehavior.defaultShipTo;
		Element customerAssignment = getModel("XPXGetCustomerAssignmentList");
		NodeList nodCustAssign=customerAssignment.getElementsByTagName("CustomerAssignment");
		ArrayList assignedCustomerShipTo = new ArrayList();
		for(int i=0;i<nodCustAssign.getLength();i++){
			Element eleCust=(Element) nodCustAssign.item(i);
			NodeList nodCustLists=eleCust.getElementsByTagName("Customer");			
			for(int j=0;j<nodCustLists.getLength();j++){
				Element eleGroup=(Element) nodCustLists.item(j);
				String assignedShipTo = eleGroup.getAttribute("CustomerID");
				assignedCustomerShipTo.add(assignedShipTo);
			}
		}
		if(!YRCPlatformUI.isVoid(defaultShipTo)){
			if (!(assignedCustomerShipTo.contains(defaultShipTo)) || YRCPlatformUI.isVoid(assignedCustomerShipTo)) {

				extnDefaultShipTo = true;
				callManageCustomer();
			}
		}
	}
	
	private Element createManageCustomerOutputXml(Element results){
		
		Element targetDoc = YRCXmlUtils.createFromString("<Customer CustomerKey='" + customerKey + "'/>").getDocumentElement();
		Element targetContactList = YRCXmlUtils.createChild(targetDoc, "CustomerContactList");
		YRCXmlUtils.importElement(targetContactList, results);
		return targetDoc;
	}

	public void getChildList(){
		Element childCustomerList = getModel("XPXGetImmediateChildCustomerListService");
		Element customerElement = YRCXmlUtils.getChildElement(childCustomerList, "Customer");
		ArrayList<Element> arrlst=new ArrayList<Element>();
		arrlst.add(customerElement);
				
		page.setTreeValues(null, arrlst); //--function used to set the values of child nodes in Tree structure
	}
	//--createManageAssignmentInput function used to decide addtion or deletion action of nodes
	public void createManageAssignmentInput(String strCustID, boolean action){
		if(null ==multiAPIDocElement){
			multiAPIDocElement = YRCXmlUtils.createDocument("MultiApi").getDocumentElement();
		}
		Element APIele= YRCXmlUtils.createChild(multiAPIDocElement, "API");
		APIele.setAttribute("Name", "manageCustomerAssignment");
		Element inputEle= YRCXmlUtils.createChild(APIele, "Input");
		Element lineEle = YRCXmlUtils.createFromString("<CustomerAssignment CustomerID='"+strCustID+"'/>").getDocumentElement();
		lineEle.setAttribute("OrganizationCode", UserOrgCode);
		lineEle.setAttribute("UserId", userID);
		if(!action)
			lineEle.setAttribute("Operation", "Delete");
		else
			lineEle.setAttribute("Operation", "Create");
		
		YRCXmlUtils.importElement(inputEle, lineEle);
		
	}
	public void updateAction(){
		page.getTargetModelForUpdateAssignments();
		YRCApiContext ctx = new YRCApiContext();
		ctx.setApiName("multiApi");
		ctx.setInputXml(multiAPIDocElement.getOwnerDocument());
		ctx.setFormId(page.getFormId());
		ctx.setShowError(false);
		ctx.setUserData("isRefreshReqd", String.valueOf(false));
		callApi(ctx, page);
		multiAPIDocElement = null;
	    ((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
		
	}
	public boolean isThisCustomerAssigned(Element eleCust) {
		return assignedList.contains(eleCust.getAttribute("CustomerID"));
		
	}	
}