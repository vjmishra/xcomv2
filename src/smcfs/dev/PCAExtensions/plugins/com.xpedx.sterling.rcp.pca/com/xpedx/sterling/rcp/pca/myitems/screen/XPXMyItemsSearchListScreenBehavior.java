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
	public static final String PAGINATION_STRATEGY_FOR_MIL_SEARCH = GENERIC_PAGINATION_STRATEGY;
	private static Element eleOutput;
	public String OrgName;
	public String createUserId;
	public String strRootCustomerKey;
	public String customerName;
	public Document BothList;
	public int count =1;
	
	public XPXMyItemsSearchListScreenBehavior(Composite ownerComposite, String formId, Object inputObject) {
       super(ownerComposite, formId,inputObject);
        this.page = (XPXMyItemsSearchListScreen)getOwnerForm();
        this.defaultOrgCode = "";
        Document docActions = YRCXmlUtils.createFromString("<Actions><Action Id='"+ACTION_EDIT+"' DisplayName='Edit'/><Action Id='"+ACTION_DELETE+"' DisplayName='Delete'/></Actions>");
        setModel(docActions.getDocumentElement());
       
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
		/*String customerID = getFieldValue("txtCustomer");
		YRCApiContext apiCtx = new YRCApiContext();
		String[] apinames = {"getCustomerList"};
		Document[] docInput = {
				YRCXmlUtils.createFromString("<Customer CustomerID='"+customerID+"' OrganizationCode='xpedx'/>")										
		};
		apiCtx.setApiNames(apinames);
		apiCtx.setInputXmls(docInput);
		apiCtx.setFormId(getFormId());*/
		//callApi(apiCtx);
    }
	
	
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
					strRootCustomerKey = YRCXmlUtils.getAttributeValue(outXml, "/CustomerList/Customer/@RootCustomerKey");
					BothList  = YRCXmlUtils.createFromString("<XpedxMilBothLst RootCustomerKey='"+strRootCustomerKey+"'/>");
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
								String CreateUserName = eleMyItemsList.getAttribute("Createusername");
								eleMyItemsList.setAttribute("Action", "");
								if(sharePrivate != ""){
									String personalListType = CreateUserName.concat("(").concat(sharePrivate).concat(")");
									eleMyItemsList.setAttribute("ListType", personalListType);
									
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
		String sharedSelect = getFieldValue("radIsShared");
		String bothSelect = getFieldValue("radIsBoth");
		String personalSelect = getFieldValue("radIsPersonal");
		BothList  = YRCXmlUtils.createFromString("<XpedxMilBothLst RootCustomerKey='"+strRootCustomerKey+"'/>");
		if(count==1){
			bothSelect = "B";
			//BothList  = YRCXmlUtils.createFromString("<XpedxMilBothLst RootCustomerKey='"+strRootCustomerKey+"'/>");
			count++;
		}
		
		//Condition for retrieving personal list
		if("P".equalsIgnoreCase(personalSelect)){
				Document personalList  = 	YRCXmlUtils.createFromString("<XpedxMilBothLst SharePrivate='"+createUserId+"'/>");										
				this.getXpxPaginationData().setInputXml(
		        		personalList);
		       
			}
		//COndition for retrieving Both List
			else if("B".equalsIgnoreCase(bothSelect)){
				this.getXpxPaginationData().setInputXml(BothList);
			}
		
		//Condition for retrieving shared list
		else if("S".equalsIgnoreCase(sharedSelect)){
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
	public void CreateSharedList() {
		//getControl("radIsShared").setRadioSelection(true);
		YRCPlatformUI.launchSharedTask("com.xpedx.sterling.rcp.pca.sharedTasks.XPEDXCreateSharedListTask");
		//getControl("radIsShared").setSelection(true);
		Element elemModel = XPXUtils.getElemModel();
		elemSearchModel = elemModel;
		isShared = true;
		getFirstPage();
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
	
	public  void selectShipToAddress() {
		
		Element elemModel = XPXUtils.getElemModel();
		elemSearchModel = elemModel;
		isShared = true;
		getFirstPage();
		
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

	public void callBothListService(int cnt) {
		String customerKey = XPXUtils.getCustomerKey();
		if(cnt ==1 || !YRCPlatformUI.isVoid(strRootCustomerKey)) {
			strRootCustomerKey = customerKey;
			getFirstPage();
		}
	}
	
	private void callPersonalUserItemList(Element eleContactPersonInfo) {
		YRCApiContext apiCtx = new YRCApiContext();
		createUserId = eleContactPersonInfo.getAttribute("Createuserid");
		getFirstPage();
	}

	
	public void selectCustomerContact() {
		strRootCustomerKey = XPXUtils.getCustomerKey();
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
