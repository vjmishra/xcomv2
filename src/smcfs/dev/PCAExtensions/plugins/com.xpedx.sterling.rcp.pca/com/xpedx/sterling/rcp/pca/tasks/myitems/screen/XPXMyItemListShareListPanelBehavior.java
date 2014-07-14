package com.xpedx.sterling.rcp.pca.tasks.myitems.screen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.myitems.editor.XPXManageMyItemsListEditor;
import com.xpedx.sterling.rcp.pca.userprofile.editor.XPXUserProfileEditor;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXMyItemListShareListPanelBehavior extends YRCBehavior {

	private XPXMyItemListShareListPanel page;
	private static final String IS_MODIFIED = "isModified";
	String organizationCode;
	public boolean updateAssignFlag = false;
	Element multiAPIDocElement=null;
	// Renamed UserOrgCode to milOrgCode
	private String strMILKey;
	private String strMILCustomerID;
	private HashMap<String, Element> assignedMap;
	private String strCustomerPathPrefix = "";
	public ArrayList parentcustomer = new ArrayList();
	public static HashSet divisionHashSet= new HashSet();
	public XPXMyItemListShareListPanelBehavior(
			XPXMyItemListShareListPanel myItemListShareListPanel,
			Element inputElement) {
		super((Composite) myItemListShareListPanel, myItemListShareListPanel.getFormId());
		this.page = myItemListShareListPanel;
		
		setModel("XPXMyItemListInput",inputElement);
		strMILKey = inputElement.getAttribute("MyItemsListKey");
		strMILCustomerID = inputElement.getAttribute("CustomerID");
		
		this.initPage();
	}
	
	public void initPage() {
		YRCApiContext apiCtx = new YRCApiContext();
		if(!YRCPlatformUI.isVoid(strMILCustomerID)) {
			String[] apinames = {"getListOfXPEDXMyItemsLists", "getCustomerList"};
			Document[] docInput = {
					YRCXmlUtils.createFromString("<XPEDXMyItemsList MyItemsListKey='"+strMILKey+"'/>"),
					YRCXmlUtils.createFromString("<Customer CustomerID='"+strMILCustomerID+"' />")										
			};
			apiCtx.setApiNames(apinames);
			apiCtx.setInputXmls(docInput);
			apiCtx.setUserData("isCustomerListQueried", true);
		} else {
			String[] apinames = {"getXPEDXMyItemsListDetail"};
			Document[] docInput = {
					YRCXmlUtils.createFromString("<XPEDXMyItemsList MyItemsListKey='"+strMILKey+"'/>")								
			};
			apiCtx.setApiNames(apinames);
			apiCtx.setInputXmls(docInput);
			apiCtx.setUserData("isCustomerListQueried", false);
		}
		apiCtx.setFormId(getFormId());
		callApi(apiCtx);
	}
	
	public void handleApiCompletion(YRCApiContext ctx) {
		if (ctx.getInvokeAPIStatus() > 0){
			if (page.isDisposed()) {
				YRCPlatformUI.trace("Page is Disposed");
			} else {
				String[] apinames = ctx.getApiNames();
				for (int i = 0; i < apinames.length; i++) {
					
					String apiname = apinames[i];
					if ("getListOfXPEDXMyItemsLists".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						Element eleMILDtls = YRCXmlUtils.getChildElement(outXml,"XPEDXMyItemsList");
						if(null != eleMILDtls){
							Element eleShareList = YRCXmlUtils.getChildElement(eleMILDtls, "XPEDXMyItemsListShareList");
							if(null != eleShareList) {
								ArrayList<Element> list = YRCXmlUtils.getChildren(eleShareList,"XPEDXMyItemsListShare");
								assignedMap = null;
								if(null == assignedMap){
									assignedMap = new HashMap<String, Element>();
								}
								for (Element eleCustAssignment : list) {
									String strCustID = eleCustAssignment.getAttribute("CustomerID");
									if(!YRCPlatformUI.isVoid(strCustID)){
										assignedMap.put(strCustID, eleCustAssignment);
									}
								}
								page.resetTreeAssignedValues(assignedMap);
							} // Share List
							setModel("XPEDXMyItemsList", eleMILDtls);
							strMILCustomerID = eleMILDtls.getAttribute("CustomerID");
							
							if(!YRCPlatformUI.isVoid(strMILCustomerID) && !(Boolean)ctx.getUserData("isCustomerListQueried")) {
								YRCApiContext apiCtx = new YRCApiContext();
								String cmdName = "getCustomerList";
								Document docInput = YRCXmlUtils.createFromString("<Customer CustomerID='"+strMILCustomerID+"' />");
								apiCtx.setApiName(cmdName);
								apiCtx.setInputXml(docInput);
								apiCtx.setFormId(getFormId());
								callApi(apiCtx);
							} else if(YRCPlatformUI.isVoid(strMILCustomerID)){
								YRCPlatformUI.showError("Error", "Unable to retrive the ");
							}
							
						}
					} else if ("getCustomerList".equals(apiname)) {
						System.out.println("its inside the api call-->>");
					
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
					} else if("XPXGetParentCustomerListService".equals(apiname)){
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
						ArrayList listParentCustomers = YRCXmlUtils.getChildren(outXml, "Customer");
						int numberOfCustomer = 0;
						if (!YRCPlatformUI.isVoid(listParentCustomers) ) {
						for (int k=0; k<listParentCustomers.size(); k++){
							Element customerEle = (Element)listParentCustomers.get(k);
							String CustomerIDValue=YRCXmlUtils.getAttribute(customerEle,"CustomerID");		
							parentcustomer.add(CustomerIDValue);
							numberOfCustomer = parentcustomer.size(); 
							}
						}
						if(numberOfCustomer==3){
				    		String strMasterCustomerID = (String) parentcustomer.get(2);
				    		YRCApiContext apiCtx = new YRCApiContext();
							String cmdName = "getCustomerList";
							Document docInput = YRCXmlUtils.createFromString("<Customer CustomerID='"+strMasterCustomerID+"' />");
							apiCtx.setApiName(cmdName);
							apiCtx.setInputXml(docInput);
							apiCtx.setFormId(getFormId());
							callApi(apiCtx);
						}
						else if(numberOfCustomer==4){
			        		String strMasterCustomerID = (String) parentcustomer.get(3);
			        		YRCApiContext apiCtx = new YRCApiContext();
							String cmdName = "getCustomerList";
							Document docInput = YRCXmlUtils.createFromString("<Customer CustomerID='"+strMasterCustomerID+"' />");
							apiCtx.setApiName(cmdName);
							apiCtx.setInputXml(docInput);
							apiCtx.setFormId(getFormId());
							callApi(apiCtx);
						}
						else if(numberOfCustomer==2){
			        		String strMasterCustomerID = (String) parentcustomer.get(1);
			        		YRCApiContext apiCtx = new YRCApiContext();
							String cmdName = "getCustomerList";
							Document docInput = YRCXmlUtils.createFromString("<Customer CustomerID='"+strMasterCustomerID+"' />");
							apiCtx.setApiName(cmdName);
							apiCtx.setInputXml(docInput);
							apiCtx.setFormId(getFormId());
							callApi(apiCtx);
						}
						//this.getChildList();    //--function used to set the values of child nodes in Tree structure
					} 
					
					
					else if ("multiApi".equals(apiname)) {
						updateAssignFlag = true;
						updateMyitemsList();
						((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(false);
					}
					//Called for JIRA 4157
					else if ("XPXCustomerHierarchyViewService".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						NodeList XPXCustHierarchyViewNodeList = outXml.getElementsByTagName("XPXCustHierarchyView");
						if (XPXCustHierarchyViewNodeList != null&& XPXCustHierarchyViewNodeList.getLength() > 0) {
							for (int k = 0; k < XPXCustHierarchyViewNodeList.getLength(); k++) {
								Element eleMyItemsList = (Element) XPXCustHierarchyViewNodeList.item(k);
								String strDivisionID = eleMyItemsList.getAttribute("ExtnCustomerDivision");
								if (strDivisionID != ""){
								divisionHashSet.add(strDivisionID);
								}
							}
						}
					}
					else if ("updateXPEDXMyItemsList".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						Element eleMyItemsList = YRCXmlUtils.getXPathElement(outXml, "/XPEDXMyItemsList");
						setModel("getXPEDXMyItemsListDetail",eleMyItemsList);
						this.getCustomerAssignmentsAfterUpdate();
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
	
	public void updateMyitemsList(){
		if(updateAssignFlag && !YRCPlatformUI.isVoid(strMILKey)){
			Element eleUpdateMyItemsListData = getModel("XPEDXMyItemsList");
			eleUpdateMyItemsListData.setAttribute("MyItemsListKey", strMILKey);
			NodeList nlItems = eleUpdateMyItemsListData.getElementsByTagName("XPEDXMyItemsItems");
			for(int i=0;i<nlItems.getLength();i++){
				Element eleItemData =(Element)nlItems.item(i);
				if(!(eleItemData.hasAttribute(IS_MODIFIED))){
					eleItemData.getParentNode().removeChild(eleItemData);
					i--;
				} 
			}
			YRCApiContext ctx = new YRCApiContext();
			ctx.setApiNames(new String[]{"updateXPEDXMyItemsList"});
			Document[] docInput = {createUpdateXPEDXMyItemsListInput(eleUpdateMyItemsListData)};
			ctx.setInputXmls(docInput);
			ctx.setFormId(getFormId());

			callApi(ctx, page);
			((XPXManageMyItemsListEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
		}
		
	}
	
	private Document createUpdateXPEDXMyItemsListInput(Element eleUpdateMyItemsListData) {
		
		Element updateXPEDXMyItemsListInput = YRCXmlUtils.createDocument("XPEDXMyItemsItemsList").getDocumentElement();
		String strCreatedByUsername = YRCPlatformUI.getUserElement().getAttribute("Username");
		String strModifyuserid = YRCPlatformUI.getUserElement().getAttribute("Loginid");
		updateXPEDXMyItemsListInput.setAttribute("MyItemsListKey", strMILKey);
		updateXPEDXMyItemsListInput.setAttribute("Name", eleUpdateMyItemsListData.getAttribute("Name"));
		updateXPEDXMyItemsListInput.setAttribute("Desc", eleUpdateMyItemsListData.getAttribute("Desc"));
		if(!YRCPlatformUI.isVoid(strCreatedByUsername))
		{
			updateXPEDXMyItemsListInput.setAttribute("ModifyUserName", strCreatedByUsername);
			updateXPEDXMyItemsListInput.setAttribute("Modifyuserid", strModifyuserid);
			
		}
		Element xPEDXMyItemsItemsList = YRCXmlUtils.createChild(updateXPEDXMyItemsListInput, "XPEDXMyItemsItemsList");
		NodeList nlItems = eleUpdateMyItemsListData.getElementsByTagName("XPEDXMyItemsItems");
		
			//System.out.println("The file is not updated");
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter=  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String dateNow = formatter.format(currentDate.getTime());
			updateXPEDXMyItemsListInput.setAttribute("Modifyts", dateNow);
		
		for(int i=0;i<nlItems.getLength();i++){
			
			Element tempElement =(Element)nlItems.item(i);
			Element xPEDXMyItemsItems= YRCXmlUtils.createChild(xPEDXMyItemsItemsList, "XPEDXMyItemsItems");
			xPEDXMyItemsItems.setAttribute("MyItemsKey", tempElement.getAttribute("MyItemsKey"));
			xPEDXMyItemsItems.setAttribute("MyItemsListKey", strMILKey);
			xPEDXMyItemsItems.setAttribute("Qty", tempElement.getAttribute("Qty"));
			xPEDXMyItemsItems.setAttribute("ItemPoNumber",tempElement.getAttribute("ItemPoNumber"));
			xPEDXMyItemsItems.setAttribute("JobId",tempElement.getAttribute("JobId"));
			xPEDXMyItemsItems.setAttribute("UomId",tempElement.getAttribute("UomId"));
			xPEDXMyItemsItems.setAttribute("ItemOrder",tempElement.getAttribute("ItemOrder"));
			
		}
		return updateXPEDXMyItemsListInput.getOwnerDocument();
	}
	
	private void getCustomerAssignmentsAfterUpdate()
	{
		YRCApiContext apiCtx = new YRCApiContext();
		String[] apinames = {"getXPEDXMyItemsListDetail"};
		Document[] docInput = {
				YRCXmlUtils.createFromString("<XPEDXMyItemsList MyItemsListKey='"+strMILKey+"'/>")								
		};
		apiCtx.setApiNames(apinames);
		apiCtx.setInputXmls(docInput);
		apiCtx.setUserData("isCustomerListQueried", true);
		apiCtx.setFormId(getFormId());
		callApi(apiCtx);
	}
	
	public void getChildList(){
		Element childCustomerList = getModel("XPXGetImmediateChildCustomerListService");
		String strMasterCustomerID = YRCXmlUtils.getAttributeValue(childCustomerList, "/CustomerList/Customer/@CustomerID");
		//Called for JIRA 4157
		getDivisionSet(strMasterCustomerID);
		Element customerElement = YRCXmlUtils.getChildElement(childCustomerList, "Customer");
		ArrayList<Element> arrlst=new ArrayList<Element>();
		arrlst.add(customerElement);
				
		page.setTreeValues(null, arrlst); //--function used to set the values of child nodes in Tree structure
	}

	public boolean isThisCustomerAssigned(Element eleCust) {
		return assignedMap.containsKey(eleCust.getAttribute("CustomerID"));
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
	
	//--createManageAssignmentInput function used to decide addition or deletion action of nodes
	public void createManageAssignmentInput(String strMILShareKey, String strCustID, String strCustPath, String strDivisionID, boolean action){
		if(null ==multiAPIDocElement){
			multiAPIDocElement = YRCXmlUtils.createDocument("MultiApi").getDocumentElement();
		}
		Element APIele= YRCXmlUtils.createChild(multiAPIDocElement, "API");
		if(!action)
			APIele.setAttribute("FlowName", "deleteXPEDX_MyItemsDetailsShareList");
		else
			APIele.setAttribute("FlowName", "createXPEDX_MyItemsDetailsShareList");
		
		Element inputEle= YRCXmlUtils.createChild(APIele, "Input");
		Element lineEle = YRCXmlUtils.createFromString("<XPEDXMyItemsListShare CustomerID='"+strCustID+"'/>").getDocumentElement();
		lineEle.setAttribute("CustomerPath", strCustPath);
		lineEle.setAttribute("MyItemsListKey",strMILKey );
		
		if(!YRCPlatformUI.isVoid(strMILShareKey))
			lineEle.setAttribute("MyItemsListShareKey", strMILShareKey);
		//Called for JIRA 4157
		if(!YRCPlatformUI.isVoid(strDivisionID)){
			strDivisionID = strDivisionID.concat("_M");
			lineEle.setAttribute("DivisionID", strDivisionID);
		}
		else{
			int divisionHashSetsize = divisionHashSet.size(); 
			if (divisionHashSetsize == 1){
				ArrayList<String> customerIDs = new ArrayList<String>();
				customerIDs.addAll(divisionHashSet);
				String strMasterDivisionID =customerIDs.get(0);
					if(!YRCPlatformUI.isVoid(strMasterDivisionID))
						strMasterDivisionID = strMasterDivisionID.concat("_M");
					lineEle.setAttribute("DivisionID", strMasterDivisionID);
					divisionHashSet = new HashSet();
			}
		}
			
		YRCXmlUtils.importElement(inputEle, lineEle);
		
		//InPut XML prepared for changeXPEDX_MyItemsList API
		Element eleXPEDXMyItemsList = YRCXmlUtils.createDocument("XPEDXMyItemsList").getDocumentElement();
		eleXPEDXMyItemsList.setAttribute("MyItemsListKey", strMILKey);
		eleXPEDXMyItemsList.setAttribute("SharePrivate", "");
		eleXPEDXMyItemsList.setAttribute("ShareAdminOnly", "");
		Element XPEDXMyItemsListShareListelem = YRCXmlUtils.createChild(eleXPEDXMyItemsList, "XPEDXMyItemsListShareList");
		XPEDXMyItemsListShareListelem.setAttribute("Reset","true");
		
		//changeXPEDX_MyItemsList API Called for removing the my items list from personal list when it got shared....
		YRCApiContext apiCtx = new YRCApiContext();
		String[] apinames = {"changeXPEDX_MyItemsList"};
		apiCtx.setApiNames(apinames);
		apiCtx.setInputXml(eleXPEDXMyItemsList.getOwnerDocument());
		apiCtx.setFormId(getFormId());
		callApi(apiCtx);
	}

	public HashMap<String, Element> getAssignedMap() {
		return assignedMap;
	}

	public String getCustomerPathPrefix() {
		return strCustomerPathPrefix;
	}
	//Called for JIRA 4157
	private void getDivisionSet(String strMasterCustomerID) {
		
		if(strMasterCustomerID != null ){
			Element XPXCustHierarchyViewelem = YRCXmlUtils.createDocument("XPXCustHierarchyView").getDocumentElement();
			XPXCustHierarchyViewelem.setAttribute("MSAPCustomerID", strMasterCustomerID);
			callApi("XPXCustomerHierarchyViewService",XPXCustHierarchyViewelem.getOwnerDocument());
		}
	}
	 void callApi(String name, Document inputXml){
	    	callApi(name, inputXml, null, null); 
	    }
	    void callApi(String name, Document inputXml, String strUserDataKey, String strUserData)
	    {
	        YRCApiContext context = new YRCApiContext();
	        context.setApiName(name);
	        context.setFormId(getFormId());
	        context.setInputXml(inputXml);
	        callApi(context);
	    }

}
