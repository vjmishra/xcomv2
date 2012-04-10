package com.xpedx.sterling.rcp.pca.myitems.screen;

import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.xml.xpath.XPathConstants;

import org.eclipse.jface.viewers.deferred.SetModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.xpedx.sterling.rcp.pca.customerDetails.extn.CustomerDetailsWizardExtnBehavior;
import com.xpedx.sterling.rcp.pca.myitems.editor.XPXManageMyItemsListEditor;
import com.xpedx.sterling.rcp.pca.tasks.myitems.screen.XPXGetCompleteChildCustomerTreeBehaviour;
import com.xpedx.sterling.rcp.pca.util.XPXCacheManager;
import com.xpedx.sterling.rcp.pca.util.XPXPaginationBehavior;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCDialog;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;
import com.yantra.yfc.rcp.internal.YRCCommand;
import com.yantra.yfc.rcp.internal.YRCCommandRepository;



/**
 * @author Manasa Mahapatra
 *
 * My Items List Search and List Panel Behavior
 */
public class XPXMyItemsSearchListScreenBehavior extends XPXPaginationBehavior {
	
	private XPXMyItemsSearchListScreen page ;
	private String defaultOrgCode ;
	public static final String ACTION_EDIT = "EDIT";
	public static final String ACTION_DELETE = "DELETE";
	//private static final String COMMAND_GET_ORGANIZATION_LIST="getOrganizationList";
	StringBuffer st = new StringBuffer();
	StringBuffer sap = new StringBuffer();
	StringBuffer msap= new StringBuffer();
	StringBuffer bt = new StringBuffer();
	private static final String COMMAND_GET_LIST_OF_MY_ITEMS_LISTS="XPXGetBothMyItemsList";
	private static final String COMMAND_DELETE_MY_ITEMS_LIST="deleteXPEDXMyItemsList";
	
	
	private String enterpriseKey;
	private String userId;
	private String customerId;
	private static Element elemSearchModel;
	private static boolean isShared;
	private static boolean isPersonal;
	private static boolean isBoth;
	public static final String PAGINATION_STRATEGY_FOR_MIL_SEARCH = NEXTPAGE_PAGINATION_STRATEGY;
	private static Element eleOutput;
	public String OrgName;
	public String createUserId;
	public String strRootCustomerKey;
	public String customerName;
	public Document BothList;
	
	public static final String PAGINATION_STRATEGY_FOR_SEARCH = NEXTPAGE_PAGINATION_STRATEGY;
	//private static final String COMMAND_GET_LIST_OF_SHIPTO="XPXGetListOfAssignedShipTosForAUserService";
	

	public XPXMyItemsSearchListScreenBehavior(Composite ownerComposite, String formId, Object inputObject) {
        super(ownerComposite, formId,inputObject);
        this.page = (XPXMyItemsSearchListScreen)getOwnerForm();
        this.defaultOrgCode = "";
        Document docActions = YRCXmlUtils.createFromString("<Actions><Action Id='"+ACTION_EDIT+"' DisplayName='Edit'/><Action Id='"+ACTION_DELETE+"' DisplayName='Delete'/></Actions>");
        setModel(docActions.getDocumentElement());
        //Called For Creating Item List type Model
        prepareItemListTypeDropdownModel();
        //Set static pagination data for the MIL Search Behavior
        this.getXpxPaginationData().setPaginationStrategy(PAGINATION_STRATEGY_FOR_MIL_SEARCH);
        
		
		this.getXpxPaginationData().setSortColumn("Name");
		
		YRCCommand command = YRCCommandRepository.getCommand((new StringBuilder()).append(getFormId()).append(COMMAND_GET_LIST_OF_MY_ITEMS_LISTS).toString());
		this.getXpxPaginationData().setApiName(command.getCommandAPIName());//getXPEDX_MyItemsList_List_Hdr service
		this.getXpxPaginationData().setIsFlow("Y");
		
		//Set the model data. This is a pre-requisite before calling the handlePaginationOutput method
		setSrcModelName("XpedxMilBothLstList");
		setRootListElemName("XpedxMilBothLstList");
		setRepeatingElemName("XpedxMilBothLst");
		
		//Load the cache/Fetch data from cache for organization list for the logged in user
		this.setOrganizationList(XPXUtils.orgList);
		//CustomerDetailsWizardExtnBehavior customer = new CustomerDetailsWizardExtnBehavior();
		//String custName = CustomerDetailsWizardExtnBehavior.getCustomerID();
		//getExtensionBehavior().getFieldValue("extn_dummytext");
	//	String myvalue = CustomerDetailsWizardExtnBehavior.getFieldValue("extn_dummytext");
		
		
    }
	
	
	
	

	/*void callApis(String names[], Document inputXmls[])
    {
        YRCApiContext context = new YRCApiContext();
        context.setApiNames(names);
        context.setFormId(getFormId());
        context.setInputXmls(inputXmls);
        callApi(context);
    }*/
	
	@Override
	public void handleApiCompletion(YRCApiContext ctx) {
		
		if(ctx.getInvokeAPIStatus() < 1)
        {
            YRCPlatformUI.trace((new StringBuilder()).append("API exception in ").append(ctx.getFormId()).append(" page, ApiName ").append(ctx.getApiName()).append(",Exception : ").toString(), ctx.getException());
        } else if(page.isDisposed())
            YRCPlatformUI.trace((new StringBuilder()).append(ctx.getFormId()).append(" page is disposed, ApiName ").append(ctx.getApiName()).append(",Exception : ").toString());
        else {
        	String[] apinames= ctx.getApiNames();
        	
        	for(int i=0; i< apinames.length;i++){
        		String apiname = apinames[i]; 
			
        		
        		/*if(YRCPlatformUI.equals(apiname, COMMAND_GET_ORGANIZATION_LIST)){
        			Element eOrgList = ctx.getOutputXmls()[i].getDocumentElement();
        			this.setOrganizationList(eOrgList);
        		}
        		else if(YRCPlatformUI.equals(apiname, COMMAND_GET_LIST_OF_MY_ITEMS_LISTS)){
        			Document docOutput = ctx.getOutputXmls()[i];
        			eleOutput = docOutput.getDocumentElement();
        			ArrayList<Element> listMyItemsList = YRCXmlUtils.getChildren(eleOutput, "XpedxMilBothLst");
        			for (Element eleMyItemsList : listMyItemsList) {
        				eleMyItemsList.setAttribute("Action", "");
					}
        			setModel(eleOutput);
        			setModel("XpedxMilBothLstList",eleOutput);
        			repopulateModel("XpedxMilBothLstList");
        			
        		}*/
        	 if (YRCPlatformUI.equals(apiname, "XPXGetBothMyItemsList")) {
				Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
			if (outXml != null) {
				NodeList listBothItemsList = outXml.getElementsByTagName("XpedxMilBothLst");
				for (int k = 0; k < listBothItemsList.getLength(); k++) {
					Element eleBothItemsList = (Element) listBothItemsList.item(k);
					String sharePrivate = eleBothItemsList.getAttribute("SharePrivate");
					String CreateUserName = eleBothItemsList.getAttribute("Createusername");
					eleBothItemsList.setAttribute("Action", "");
					if(sharePrivate != ""){
						eleBothItemsList.setAttribute("ListType", CreateUserName);
						
					}
					else{
						eleBothItemsList.setAttribute("ListType", "Shared");
					}
				}
			}
			setModel("XpedxMilBothLstList",outXml);
    		}
        		else if (YRCPlatformUI.equals(apiname, "getCustomerList")) {
					
					Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
					setModel("CustomerList",outXml);
					OrgName= YRCXmlUtils.getAttributeValue(outXml, "/CustomerList/Customer/BuyerOrganization/@OrganizationName");
					String customerid= YRCXmlUtils.getAttributeValue(outXml, "/CustomerList/Customer/@CustomerID");
					String[] trimCustIDArray = customerid.split("-");
					String trimCustID = trimCustIDArray[1];
					customerName = OrgName.concat("(").concat(trimCustID).concat(")");
					strRootCustomerKey = YRCXmlUtils.getAttributeValue(outXml, "/CustomerList/Customer/@RootCustomerKey");
					BothList  = YRCXmlUtils.createFromString("<XpedxMilBothLst RootCustomerKey='"+strRootCustomerKey+"'/>");
					if("Both".equalsIgnoreCase(getFieldValue("cmbListType"))){
					if(!YRCPlatformUI.isVoid(strRootCustomerKey)){
						callBothListService(strRootCustomerKey);
						
					} 
					}
					
					else if("Personal".equalsIgnoreCase(getFieldValue("cmbListType"))){
						if(!YRCPlatformUI.isVoid(strRootCustomerKey)){
							selectCustomerContact(strRootCustomerKey);
							
						} 
						}
					
					
				}
        	
        			
        		else if(YRCPlatformUI.equals(apiname, "getPage")){
        			
        			Document docOutput = ctx.getOutputXmls()[i];
					eleOutput = docOutput.getDocumentElement();
					if (eleOutput != null) {
						//Get the repeating elements and set their Action attribute
						NodeList listMyItemsList = (NodeList) YRCXPathUtils
								.evaluate(eleOutput, "./Output/"
										+ getRootListElemName() + "/"
										+ getRepeatingElemName(),
										XPathConstants.NODESET);
						if (listMyItemsList != null
								&& listMyItemsList.getLength() > 0) {

							for (int k = 0; k < listMyItemsList.getLength(); k++) {
								Element eleMyItemsList = (Element) listMyItemsList
										.item(k);
								String sharePrivate = eleMyItemsList.getAttribute("SharePrivate");
								eleMyItemsList.setAttribute("Action", "");
								if(sharePrivate != ""){
									eleMyItemsList.setAttribute("ListType", sharePrivate);
									
								}
								else{
									eleMyItemsList.setAttribute("ListType", "Shared");
								}
							}
						}
						setModel("XpedxMilBothLstList",eleOutput);
						//This is an inherited method which sets/replaces the model used to display the Paginated results.
						handlePaginationOutput(eleOutput);
					}
        		}
        	}
	        
        }
		
		
	}
	
	private void setOrganizationList(Element eOrgList)
    {
        setModel("OrgList", eOrgList);
        NodeList nl = eOrgList.getElementsByTagName("Organization");
        if(null != nl && nl.getLength()>0)
        	defaultOrgCode = YRCPlatformUI.getUserElement().getAttribute("EnterpriseCode");
        
       // this.setDefaultEnterpriseCode();
    }

	public void setDefaultEnterpriseCode()
    {
		//Update the Search Criteria
        Element elemModel = getModel("SearchCriteria");
        if(YRCPlatformUI.isVoid(elemModel))
            elemModel = YRCXmlUtils.createDocument("XpedxMilBothLst").getDocumentElement();

        /*Element orderByElem = YRCXmlUtils.getChildElement(elemModel, "OrderBy");
        if(YRCPlatformUI.isVoid(orderByElem))
            orderByElem = YRCXmlUtils.createChild(elemModel, "OrderBy");
        Element attrElem = YRCXmlUtils.getChildElement(orderByElem, "Attribute");
        if(YRCPlatformUI.isVoid(attrElem))
            attrElem = YRCXmlUtils.createChild(orderByElem, "Attribute");
        attrElem.setAttribute("Name", "Name");
        attrElem.setAttribute("Desc", "N");
        
        if(!YRCPlatformUI.isVoid(defaultOrgCode))
        {
            elemModel.setAttribute("EnterpriseKey", defaultOrgCode);
        }*/
        setModel("SearchCriteria",elemModel);
    }

	public void mouseDoubleClick(MouseEvent e, String ctrlName) {
		if(YRCPlatformUI.equals(ctrlName, "tblResults"))
        {
			TableItem tblItems[] = page.tblResults.getSelection();
			openMultipleEditors(tblItems);
        }
	}

	private void openMultipleEditors(TableItem[] tblItems) {
		
		if(tblItems.length > 0)
        	for(int i = 0; i < tblItems.length; i++){
        		Element eleDetailsInput = (Element)tblItems[i].getData();
        		
        		eleDetailsInput = YRCXmlUtils.getCopy(eleDetailsInput);
                if(!YRCPlatformUI.isVoid(tblItems[i]) && !tblItems[i].isDisposed())
                {
                	String currMyItemsListKey = eleDetailsInput.getAttribute("MyItemsListKey");

                    boolean isMyItemsListDetailsEditorFound = false;
                    IEditorReference editorReferences[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
                    for(int j = 0; j < editorReferences.length; j++){
                        try
                        {
                            org.eclipse.ui.IEditorInput tEditorInput = editorReferences[j].getEditorInput();
                            org.eclipse.ui.IEditorPart tEditor = editorReferences[j].getEditor(false);
//                            if(tEditor == null || !(tEditor instanceof XPXMyItemsListDetailsEditor))
//                                continue;
                            Element orderEntryEditorInput = ((YRCEditorInput)tEditorInput).getXml();
                            if(YRCPlatformUI.isVoid(orderEntryEditorInput) || !YRCPlatformUI.equals(currMyItemsListKey, orderEntryEditorInput.getAttribute("MyItemsListKey")))
                                continue;
// TODO Open My Items List Details Editor                           YRCPlatformUI.openEditor("<MyItemsListDetail_Editor_ID>", (YRCEditorInput)tEditorInput);
                            YRCPlatformUI.openEditor("com.xpedx.sterling.rcp.pca.myitems.editor.XPXManageMyItemsListEditor", (YRCEditorInput)tEditorInput);
                            isMyItemsListDetailsEditorFound = true;
                            break;
                        }
                        catch(Exception e)
                        {
                            YRCPlatformUI.trace("Exception in while opening the My Items List Details Editor : ", e);
                        }
                    }
                    if(!isMyItemsListDetailsEditorFound){
                    	String[] arrayEditorComparisonAttrs = new String[] {
                                "MyItemsListKey"
                            };
//		TODO Open My Items List Details Editor                           YRCPlatformUI.openEditor("<MyItemsListDetail_Editor_ID>", new YRCEditorInput(eleDetailsInput, arrayEditorComparisonAttrs, ""));
                        YRCPlatformUI.openEditor("com.xpedx.sterling.rcp.pca.myitems.editor.XPXManageMyItemsListEditor", new YRCEditorInput(eleDetailsInput, arrayEditorComparisonAttrs, ""));

                    }
                }
        	}
	}
	
	/**
	 * This method is used to fetch the paginated My Items List.
	 * This method is invoked on click of Search button as well as on clicking of the pagination controls to navigate to other pages of the result. 
	 */
	public void search() {
		//Condition for retrieving personal list
		if(isShared == false && isPersonal == false && isBoth==false) {
			this.getXpxPaginationData().setInputXml(
					getTargetModel("SearchCriteria").getOwnerDocument());
			
			}else if(isPersonal == true){
				Document personalList  = 	YRCXmlUtils.createFromString("<XpedxMilBothLst Createuserid='"+createUserId+"'/>");										
				this.getXpxPaginationData().setInputXml(
		        		personalList);
		       
			}
		//COndition for retrieving Both List
			else if(isBoth == true){
				this.getXpxPaginationData().setInputXml(BothList);
			}
		
		//Condition for retrieving shared list
		else{
		setModel("ShareListModel",elemSearchModel);
		 Document finalelem = getModel("ShareListModel").getOwnerDocument();
		 if(YRCPlatformUI.isVoid(elemSearchModel))
           isShared = false;
        this.getXpxPaginationData().setInputXml(
				finalelem);
		}
    	super.search();
	}

	void callApi(String name, Document inputXml)
    {
        YRCApiContext context = new YRCApiContext();
        context.setApiName(name);
        context.setFormId(getFormId());
        context.setInputXml(inputXml);
        callApi(context);
    }

	public void delete(Element element) {
		Document docInput = YRCXmlUtils.createDocument("XPEDXMyItemsList");
		Element eleInput = docInput.getDocumentElement();
		eleInput.setAttribute("MyItemsListKey", element.getAttribute("MyItemsListKey"));
		callApi(COMMAND_DELETE_MY_ITEMS_LIST, docInput);
	}
	
	public void edit(Element element) throws PartInitException {
		
        Element eleEditorInput = YRCXmlUtils.getCopy(element, true);		
        //eleEditorInput.setAttribute("enterpriseKey", this.enterpriseKey); //JIRA 1155 - Code Starts to Add UOMDesc to UI
        eleEditorInput.getOwnerDocument().renameNode(eleEditorInput, eleEditorInput.getNamespaceURI(), "XPEDXMyItemsListTmp");
        YRCPlatformUI.openEditor(XPXManageMyItemsListEditor.ID_EDITOR, new YRCEditorInput(eleEditorInput, eleEditorInput, new String[]{"MyItemsListKey"}, ""));
	}	
	public void create() {
		YRCPlatformUI.launchSharedTask("com.xpedx.sterling.rcp.pca.sharedTasks.XPXCreateMyItemsListSharedTask");
	}
	public void create1() {
		YRCPlatformUI.launchSharedTask("com.xpedx.sterling.rcp.pca.sharedTasks.XPEDXCreateSharedListTask");
	}
	/**Added for XIRA ID-1176 for Reset functionality**/
	public void reset() {
		setFieldValue("txtCustomer", "");
		setFieldValue("txtUserId", "");
	    }


	public void openReplacementTool() {
		this.enterpriseKey = getTargetModel("SearchCriteria").getAttribute("EnterpriseKey"); 
		XPXMyItemsReplacementToolPanel popupReplacementTool  = new XPXMyItemsReplacementToolPanel(new Shell(Display.getDefault()), SWT.NONE,enterpriseKey);
	    YRCDialog oDialog = new YRCDialog(popupReplacementTool,700,600,"TITLE_KEY_My_Items_Replacement_Tool_UI",null);
	    oDialog.open();
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
	
	private void prepareItemListTypeDropdownModel() {
		Element elemModel = YRCXmlUtils.createDocument("GetListType").getDocumentElement();
		
		Element attrElemComplex1 = YRCXmlUtils.createChild(elemModel, "ListType");
		
		attrElemComplex1.setAttribute("ListType", "Personal");
		attrElemComplex1.setAttribute("ListValue", "Personal");

		Element attrElemComplex2 = YRCXmlUtils.createChild(elemModel, "ListType");;

		attrElemComplex2.setAttribute("ListType", "Shared");
		attrElemComplex2.setAttribute("ListValue", "Shared");
		
		Element attrElemComplex3 = YRCXmlUtils.createChild(elemModel, "ListType");;

		attrElemComplex3.setAttribute("ListType", "Both");
		attrElemComplex3.setAttribute("ListValue", "Both");
		
		setModel("GetListType",elemModel);
	}
	
	public static void selectShipToAddress(Element elemModel) {
		elemSearchModel = elemModel;
		isShared = true;
		}
	public void selectListType (){
		String listtype = getFieldValue("cmbListType");
		if ("Personal".equalsIgnoreCase(listtype)||"Shared".equalsIgnoreCase(listtype)){
			getCustomerKey();
			getFirstPage();
		}
		else if("Both".equalsIgnoreCase(listtype)){
			
			getCustomerKey();	
		}
		else{
			YRCPlatformUI.showError("ITEMLISTTYPE_FIELD_IS_MANDATORY",
					YRCPlatformUI
							.getString("ITEMLISTTYPE_FIELD1_LABEL_IS_MANDATORY"));
		}
	}


	void getCustomerKey() {
		String customerID = getFieldValue("txtCustomer");
		YRCApiContext apiCtx = new YRCApiContext();
		if(!YRCPlatformUI.isVoid(customerID)) {
			String[] apinames = {"getCustomerList"};
			Document[] docInput = {
					YRCXmlUtils.createFromString("<Customer CustomerID='"+customerID+"' OrganizationCode='xpedx'/>")										
			};
			apiCtx.setApiNames(apinames);
			apiCtx.setInputXmls(docInput);
		}
		apiCtx.setFormId(getFormId());
		callApi(apiCtx);
		
	}

	private void callBothListService(String strRootCustomerKey) {
		isPersonal = false;
		isBoth = true;
		YRCApiContext apiCtx = new YRCApiContext();
		if(!YRCPlatformUI.isVoid(strRootCustomerKey)) {
			getFirstPage();
		}
		

		
	}
	
	private void callPersonalUserItemList(Element eleContactPersonInfo) {
		isBoth = false;
		YRCApiContext apiCtx = new YRCApiContext();
		createUserId = eleContactPersonInfo.getAttribute("Createuserid");
		setFieldValue("txtUserId", createUserId);
		
			isPersonal = true;
			getFirstPage();
	}

	
	public void selectCustomerContact(String strRootCustomerKey) {
		Document docInput = YRCXmlUtils.createFromString("<CustomerContact CustomerKey='"+strRootCustomerKey+"'/>");
		//Launch Shared Task
		YRCSharedTaskOutput output = YRCPlatformUI
				.launchSharedTask(
						"com.xpedx.sterling.rcp.pca.sharedTasks.XPXUserLookUpSharedTask",
						docInput.getDocumentElement());
		
		Element eleContactPersonInfo = output.getOutput();
		callPersonalUserItemList(eleContactPersonInfo);
	}
}
