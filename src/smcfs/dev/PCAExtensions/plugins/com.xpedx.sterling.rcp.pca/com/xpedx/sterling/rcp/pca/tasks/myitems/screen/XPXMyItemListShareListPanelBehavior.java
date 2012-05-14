package com.xpedx.sterling.rcp.pca.tasks.myitems.screen;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.userprofile.editor.XPXUserProfileEditor;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXMyItemListShareListPanelBehavior extends YRCBehavior {

	private XPXMyItemListShareListPanel page;
	String organizationCode;
	Element multiAPIDocElement=null;
	// Renamed UserOrgCode to milOrgCode
	private String strMILKey;
	private String strMILCustomerID;
	private HashMap<String, Element> assignedMap;
	private String strCustomerPathPrefix = "";
	public ArrayList parentcustomer = new ArrayList();
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
						this.getCustomerAssignmentsAfterUpdate();
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
		
		if(!YRCPlatformUI.isVoid(strDivisionID))
			lineEle.setAttribute("DivisionID", strDivisionID);
		
		
		YRCXmlUtils.importElement(inputEle, lineEle);
		
	}

	public HashMap<String, Element> getAssignedMap() {
		return assignedMap;
	}

	public String getCustomerPathPrefix() {
		return strCustomerPathPrefix;
	}

}
