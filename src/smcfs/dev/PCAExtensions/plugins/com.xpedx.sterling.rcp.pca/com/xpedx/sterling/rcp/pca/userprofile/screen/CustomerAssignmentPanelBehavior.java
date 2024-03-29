package com.xpedx.sterling.rcp.pca.userprofile.screen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import org.eclipse.swt.SWT;
import org.eclipse.jface.viewers.deferred.SetModel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.userprofile.editor.XPXUserProfileEditor;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;
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
	private long startTimeCustomerService;
	private long endTime,timespent;
	public String custMsapId;
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
		//user
		Document inputXML=YRCXmlUtils.createFromString("<CustomerAssignment />");
		inputXML.getDocumentElement().setAttribute("UserId", userID);
		Document[] docInput = {
				inputXML,
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
						XPXUtils.setAssignedCustomerList(assignedList);
						if(null == assignedList){
							assignedList = new ArrayList();
						}
						for (Object eleCustAssignment : list) {
							String strCustID = YRCXmlUtils.getAttributeValue((Element)eleCustAssignment, "CustomerAssignment/Customer/@CustomerID");
							assignedList.add(strCustID);
							custMsapId = YRCXmlUtils.getAttributeValue((Element)eleCustAssignment, "CustomerAssignment/@OrganizationCode");
						}
						XPXUtils.setMsapCustomerOrgID(custMsapId);
						XPXUtils.setAssignedCustomerList(assignedList);
						page.resetTreeAssignedValues(assignedList);
						setModel("XPXGetCustomerAssignmentList",outXml);
						getShipToID();
						
					} else if ("getCustomerList".equals(apiname)) {
						System.out.println("its inside the api call-->>");
					
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						setModel("XPXGetImmediateChildCustomerListService",outXml);
						getChildList();    //--function used to set the values of child nodes in Tree structure
					} else if ("XPXManageCustomerAndAssignmentAPIService".equals(apiname)) {
						 getCustomerAssignmentsAfterUpdate();
						((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(false);
					}
					  else if ("manageCustomer".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(false);
					}
					  else if ("XPXGetListOfAssignedShipTosForAUserService".equals(apiname)) {
							Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
							setModel("AssignedShipTos",outXml);
							getCustomerAssignment();
						}
					  else if ("XPXCustomerAssignmentViewService".equals(apiname)) {
							Element outXml1 = ctx.getOutputXmls()[i].getDocumentElement();
							//setModel("AssignedShipTos",outXml);
							//getCustomerAssignment();
							ArrayList list = /*null;
							Element ele =*/ YRCXmlUtils.getChildren(outXml1,"XPXCustomerAssignmentView");
							assignedList = null;
							XPXUtils.setAssignedCustomerList(assignedList);
							if(null == assignedList){
								assignedList = new ArrayList();
							}
							for (Object eleCustAssignment : list) {
								String strCustID = YRCXmlUtils.getAttributeValue((Element)eleCustAssignment, "CustomerAssignment/Customer/@CustomerID");
								assignedList.add(strCustID);
							}
							XPXUtils.setAssignedCustomerList(assignedList);
						//	page.resetTreeAssignedValues(assignedList);
							//setModel("XPXGetCustomerAssignmentList",outXml);
							System.out.println("XML --" + YRCXmlUtils.getString(outXml1));
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
		//Added to check performance of XB-638		
		endTime=System.currentTimeMillis();
		timespent=(endTime-startTimeCustomerService);
		System.out.println("Final Time Taken by XPXManageCustomerAndAssignmentAPIService:"+ timespent);
	}	
	
	


	private void getCustomerAssignmentsAfterUpdate()
	{
		String[] apinames = {"getCustomerAssignmentList"};
		Document inputXML=YRCXmlUtils.createFromString("<CustomerAssignment />");
		inputXML.getDocumentElement().setAttribute("UserId", userID);
		Document[] docInput = {
				//YRCXmlUtils.createFromString("<CustomerAssignment UserId='" + userID + "'/>"),
				inputXML,
		};
		callApis(apinames, docInput);
	
	}
	
	public void getShipToID(){
		String[] apinames = {"XPXGetListOfAssignedShipTosForAUserService"};
		Document inputXML=YRCXmlUtils.createFromString("<XPXCustomerAssignmentView />");
		inputXML.getDocumentElement().setAttribute("UserId", userID);
		Document[] docInput = {
				//YRCXmlUtils.createFromString("<XPXCustomerAssignmentView UserId='" + userID + "'/>"),
				inputXML,
		};
		callApis(apinames, docInput);
	}
	
	
	public void callManageCustomer()
	{
		Element targetModel = getModel("XPXCustomerContactIn");
		if(!YRCPlatformUI.isVoid(customerContactID)){
		targetModel.setAttribute("CustomerContactID", customerContactID);
		String shipTo = "";
		Document docInput =
			YRCXmlUtils.createFromString("<CustomerContact CustomerContactID='" + customerContactID + "'>" + "<Extn ExtnDefaultShipTo ='" + shipTo + "'/>" + "</CustomerContact>");
		Element inputXML = docInput.getDocumentElement();
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(page.getFormId());
		ctx.setApiName("manageCustomer");
		ctx.setInputXml(createManageCustomerOutputXml(inputXML).getOwnerDocument());
		if (!page.isDisposed())
			callApi(ctx, page);
		}
	}
	
	public void getCustomerAssignment(){
		String defaultShipTo = UserProfileInfoDetailsBehavior.defaultShipTo;
		Element customerAssignment = getModel("AssignedShipTos");
		NodeList nodCustAssign=customerAssignment.getElementsByTagName("XPXCustomerAssignmentView");
		ArrayList assignedCustomerShipTo = new ArrayList();
		for(int i=0;i<nodCustAssign.getLength();i++){
			Element eleCust=(Element) nodCustAssign.item(i);
				String assignedShipTo = eleCust.getAttribute("ShipToCustomerID");
				assignedCustomerShipTo.add(assignedShipTo);
			}
		if(!YRCPlatformUI.isVoid(defaultShipTo)){
			if (!(assignedCustomerShipTo.contains((defaultShipTo)) || YRCPlatformUI.isVoid(assignedCustomerShipTo))) {

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
	/*	Commented below code as new service was written for XB-638
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
		
	}*/
	
	// Added below method for XB-638
	private void saveChanges(List<String> wList, String operation,Element customerAssignmentListElem) {
		
		for (int index = 0; index < wList.size(); index++) {
			try {
				if(wList.get(index) != null && wList.get(index).trim().length() ==0 )
					continue;
				Element customerAssignmentElem=YRCXmlUtils.createChild(customerAssignmentListElem, "CustomerAssignment");
				customerAssignmentElem.setAttribute("CustomerID", wList.get(index));
				customerAssignmentElem.setAttribute("OrganizationCode", UserOrgCode);
				customerAssignmentElem.setAttribute("UserId", userID);
				customerAssignmentElem.setAttribute("Operation", operation);
				} catch (Exception ex) {
				System.out.println("***********Record already exists");
			}
		}
	}
	
	public void createManageAssignmentInput(List<String> wList, boolean action){
		if(null ==multiAPIDocElement){
		multiAPIDocElement = YRCXmlUtils.createDocument("ManageCustomerAndAssignment").getDocumentElement();
		
		}
		multiAPIDocElement.setAttribute("IgnoreOrdering", "Y");
		Element custAssignmentele=(Element)multiAPIDocElement.getElementsByTagName("CustomerAssignmentList").item(0);
		if(custAssignmentele == null)
		{
			custAssignmentele= YRCXmlUtils.createChild(multiAPIDocElement, "CustomerAssignmentList");
		}
		if(!action)
		saveChanges(wList, "Delete",custAssignmentele);
		else
			saveChanges(wList, "Create",custAssignmentele);
		System.out.println("Final XML : " + YRCXmlUtils.getString(multiAPIDocElement));
	}	
	
	
	public void updateAction(){
		startTimeCustomerService=System.currentTimeMillis(); 
		page.getTargetModelForUpdateAssignments();
		YRCApiContext ctx = new YRCApiContext();
		ctx.setApiName("XPXManageCustomerAndAssignmentAPIService");
		if(multiAPIDocElement == null)
			return;
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
	public void showAuthLocations(){
		Document docInput = YRCXmlUtils.createDocument("User");		
		Element eleInput = docInput.getDocumentElement();
		eleInput.setAttribute("UserID", userID);
		if(YRCPlatformUI.isTraceEnabled()){
			YRCPlatformUI.trace("Shared Task: Launching with Input XML - "+YRCXmlUtils.getString(docInput));
		}
		//Launch Shared Task
		YRCSharedTaskOutput output = YRCPlatformUI.launchSharedTask("com.xpedx.sterling.rcp.pca.sharedTasks.XPXAuthorizedLocationsSharedTask", eleInput);
	}
	public void exportAuthLocations(){
		Element authorizedShipTosElement = getModel("AssignedShipTos");
		if(authorizedShipTosElement!=null){
			exportToFile();
		}else{
			getShipToID();
			exportToFile();
		}		
	}
	private void exportToFile(){
		StringBuilder sbCSV = new StringBuilder();		
		HashSet<String> set = new HashSet<String>();
		Element eleOutput = getModel("AssignedShipTos");							
		NodeList customerList = (NodeList) eleOutput.getElementsByTagName("XPXCustomerAssignmentView");
		if (customerList != null && customerList.getLength()>0) {
			sbCSV.append("User ID,MSAP,Customer Number,Bill-To Number,Ship-To Number,Name,Status,Address");
			sbCSV.append("\n");
			for(int j=0;j<customerList.getLength();j++){
				Element eleCustomer = (Element)customerList.item(j);
				sbCSV.append("\"").append(userID).append("\"").append(",");
				sbCSV.append("\"").append(eleCustomer.getAttribute("MSAPCustomerID")).append("\"").append(",");
				sbCSV.append("\"").append(eleCustomer.getAttribute("SAPCustomerID")).append("\"").append(",");

				set.add(eleCustomer.getAttribute("BillToCustomerID"));
				String billToCustomerID  = eleCustomer.getAttribute("BillToCustomerID");					
				sbCSV.append("\"").append(
						(!YRCPlatformUI.isVoid(billToCustomerID)&& billToCustomerID.lastIndexOf("-M-XX-B")!=-1)?billToCustomerID.substring(0,billToCustomerID.lastIndexOf("-M-XX-B")):"").append("\"").append(",");

				String shipToCustomerID  = eleCustomer.getAttribute("ShipToCustomerID");					
				sbCSV.append("\"").append(
						(!YRCPlatformUI.isVoid(shipToCustomerID)&& shipToCustomerID.lastIndexOf("-M-XX-S")!=-1)?shipToCustomerID.substring(0,shipToCustomerID.lastIndexOf("-M-XX-S")):"").append("\"").append(",");

				sbCSV.append("\"").append(eleCustomer.getAttribute("ShipToCustomerName")).append("\"").append(",");
				String status = eleCustomer.getAttribute("Status");
				sbCSV.append("\"").append(!YRCPlatformUI.isVoid(status) && "30".equalsIgnoreCase(status.trim())?"[Suspended] do not use":"").append("\"").append(",");
				sbCSV.append("\"").append(getAddressColumnText(eleCustomer)).append("\"");
				sbCSV.append("\n");
			}
			org.eclipse.swt.widgets.FileDialog fileDialog = new FileDialog(YRCPlatformUI.getShell(), SWT.SAVE);
			fileDialog.setFileName(userID+"_Authorized_Locations" +"_" + System.currentTimeMillis()+".csv");
			fileDialog.setText("Save");
			String filePath = fileDialog.open();
			if(!YRCPlatformUI.isVoid(filePath))
				saveFile(filePath,sbCSV);
		}else{
			YRCPlatformUI.showWarning("Export Authorized Locations", "There are no Ship-To Locations assigned to export.");
		}

	}
	private String getAddressColumnText(Element eleCustomer) {			
		String add1 = eleCustomer.getAttribute("AddressLine1");
		String add2 = eleCustomer.getAttribute("AddressLine2");				
		String city = eleCustomer.getAttribute("City");
		String state = eleCustomer.getAttribute("State");
		String country = eleCustomer.getAttribute("Country");
		String zipCode = eleCustomer.getAttribute("ZipCode");
		String firstZip=zipCode;
		String lastZip="";			
		if(!YRCPlatformUI.isVoid(zipCode) && zipCode.length()>5){
			firstZip=zipCode.substring(0, 5);
			lastZip="-"+zipCode.substring(5);
		}
		StringBuffer addressString = new StringBuffer();			 
		if(!YRCPlatformUI.isVoid(add1))
			addressString.append(add1);
		if(!YRCPlatformUI.isVoid(add2))
			addressString.append(", "+add2);
		if(!YRCPlatformUI.isVoid(city))
			addressString.append(", "+city);				
		if(!YRCPlatformUI.isVoid(state))
			addressString.append(", "+state);
		if(!YRCPlatformUI.isVoid(firstZip))
			addressString.append(", "+firstZip+lastZip);
		if(!YRCPlatformUI.isVoid(country))
			addressString.append(", "+country);
		return addressString.toString();
	}
	private void saveFile(String filePath, StringBuilder output) {
		if(YRCPlatformUI.isVoid(filePath))
			return;
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(filePath)); 				
			out.write(output.toString());
		} catch (IOException e){
			e.printStackTrace();
		}
		finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}