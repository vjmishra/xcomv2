package com.xpedx.sterling.rcp.pca.user.internal.screen;


/*
 * Created on Jul 08,2010
 *
 */
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.user.internal.editor.XPXCreateUserEditor;
import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;


/**
 * @author sdodda
 *
 * Articles Search and List Panel Behavior
 */
public class XPXInternalUserSearchListBehaviour extends YRCBehavior {
	
	private XPXInternalUserSearchListScreen page ;
	private String defaultOrgCode ;

	public XPXInternalUserSearchListBehaviour(Composite ownerComposite, String formId, Object inputObject) {
        super(ownerComposite, formId,inputObject);
        this.page = (XPXInternalUserSearchListScreen)getOwnerForm();
        this.defaultOrgCode = "";
        init();
    }
	
	@Override
	protected void init() {
		String api[] = {
	            "getOrganizationList","getQueryTypeList"
	        };
		Document doc[] = {
				XPXUtils.getOrganizationListInput(YRCPlatformUI.getUserId()).getOwnerDocument(),YRCXmlUtils.createDocument("QueryType")
		};
	    callApis(api, doc);
		super.init();
	}

	void callApis(String names[], Document inputXmls[])
    {
        YRCApiContext context = new YRCApiContext();
        context.setApiNames(names);
        context.setFormId(getFormId());
        context.setInputXmls(inputXmls);
        callApi(context);
    }
	
	@Override
	public void handleApiCompletion(YRCApiContext ctx) {
		if(ctx.getInvokeAPIStatus() < 1)
        {
            YRCPlatformUI.trace((new StringBuilder()).append("API exception in ").append(ctx.getFormId()).append(" page, ApiName ").append(ctx.getApiName()).append(",Exception : ").toString(), ctx.getException());
        } else if(page.isDisposed())
            YRCPlatformUI.trace((new StringBuilder()).append(ctx.getFormId()).append(" page is disposed, ApiName ").append(ctx.getApiName()).append(",Exception : ").toString());
        else {
        	for(int i = 0; i < ctx.getApiNames().length; i++){
        	if(YRCPlatformUI.equals(ctx.getApiNames()[i], "getOrganizationList"))
        		this.setOrganizationList(ctx);
        	else if(YRCPlatformUI.equals(ctx.getApiNames()[i], "getQueryTypeList"))
        		
        		setModel("getQueryTypeList_output", ctx.getOutputXmls()[i].getDocumentElement());
	        else if(YRCPlatformUI.equals(ctx.getApiNames()[i], "getUserList"))
	        	setModel("UserList",ctx.getOutputXmls()[i].getDocumentElement());
        	    handleSearchApiCompletion(ctx);
        	}
	        
        }
		
	}
	private void handleSearchApiCompletion(YRCApiContext ctx) {
		//manipulateOutputXml(ctx.getOutputXml());
		setModel("User_Details",ctx.getOutputXml().getDocumentElement());
	}
	
	private void setOrganizationList(YRCApiContext ctx)
    {
        Element eOrgList = ctx.getOutputXml().getDocumentElement();
        setModel("OrgList", eOrgList);
        NodeList nl = eOrgList.getElementsByTagName("Organization");
        if(null != nl && nl.getLength()>0)
        	defaultOrgCode = YRCPlatformUI.getUserElement().getAttribute("EnterpriseCode");
        
        this.setDefaultEnterpriseCode();
    }
	
	
	
	public void setDefaultEnterpriseCode()
    {
		//Update the Search Criteria
        Element elemModel = getModel("SearchCriteria");
        if(YRCPlatformUI.isVoid(elemModel)){
            elemModel = YRCXmlUtils.createDocument("User").getDocumentElement();
        }
        
        if(!YRCPlatformUI.isVoid(defaultOrgCode))
        {
            elemModel.setAttribute("EnterpriseKey", defaultOrgCode);
        }
        setModel("SearchCriteria", elemModel);
    }

	public void mouseDoubleClick(MouseEvent e, String ctrlName) {
		if(YRCPlatformUI.equals(ctrlName, "tblSearchResults"))
        {
			TableItem tblItems[] = page.tblSearchResults.getSelection();
			//openMultipleOrders(tblItems);
			if(tblItems.length > 0)
				for(int i = 0; i < tblItems.length; i++)
	                if(!YRCPlatformUI.isVoid(tblItems[i]) && !tblItems[i].isDisposed())
	                {
	                    Element eleSelected = (Element)tblItems[i].getData();
	                    Element eleEditorInput = YRCXmlUtils.getCopy(eleSelected, true);
	                    YRCPlatformUI.openEditor("com.xpedx.sterling.rcp.pca.user.internal.editor.XPXCreateUserEditor", new YRCEditorInput(eleEditorInput, new String[] {"UserKey"}, eleSelected.getAttribute("UserKey")));
	                }
        }
	}
	
		public void search() {
		Element elemModel = getTargetModel("SearchCriteria");
		if(YRCPlatformUI.isVoid(elemModel)){
            elemModel = YRCXmlUtils.createDocument("User").getDocumentElement();
//            elemModel.setAttribute("Usertype", XPXConstants.DEFAULT_CSR_USER_TYPE);
        }
        if(!YRCPlatformUI.isVoid(elemModel.getAttribute("Loginid"))){
        	elemModel.setAttribute("LoginidQryType", "FLIKE");
        }
        Element orderByElem = YRCXmlUtils.getChildElement(elemModel, "OrderBy");
        if(YRCPlatformUI.isVoid(orderByElem))
            orderByElem = YRCXmlUtils.createChild(elemModel, "OrderBy");
        Element attrElem = YRCXmlUtils.getChildElement(orderByElem, "Attribute");
        if(YRCPlatformUI.isVoid(attrElem))
            attrElem = YRCXmlUtils.createChild(orderByElem, "Attribute");
        attrElem.setAttribute("Name", "UserKey");
        attrElem.setAttribute("Desc", "N");
        Element eleExtnSearchUser= YRCXmlUtils.createChild(elemModel, "Extn");
        eleExtnSearchUser.setAttribute("ExtnUserType", XPXConstants.DEFAULT_CSR_USER_TYPE);
     //   elemModel.setAttribute("Usertype", XPXConstants.DEFAULT_CSR_USER_TYPE);
        YRCApiContext context = new YRCApiContext();
	    context.setApiName("getUserList");
	    context.setFormId(getFormId());
	    context.setInputXml(elemModel.getOwnerDocument());
	    callApi(context);
    }

	
}