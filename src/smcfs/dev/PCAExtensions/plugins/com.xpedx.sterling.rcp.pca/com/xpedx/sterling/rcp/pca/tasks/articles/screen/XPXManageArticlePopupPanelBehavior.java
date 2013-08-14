/*
 * Created on Jul 09,2010
 *
 */
package com.xpedx.sterling.rcp.pca.tasks.articles.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXCacheManager;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

/**
 * @author sdodda
 * 
 * Behavior for Manage Articles Panel
 */

public class XPXManageArticlePopupPanelBehavior extends YRCBehavior {

	private XPXManageArticlePopupPanel page;
   private String storeFront;
	/**
	 * Constructor for the behavior class.
	 */

	public XPXManageArticlePopupPanelBehavior(Composite ownerComposite,
			String formId) {
		super(ownerComposite, formId);
		this.page = (XPXManageArticlePopupPanel) ownerComposite;
		if (!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(),
				"ArticleKey")))
			getArticleDetails(page.getPageInput());
		initialize();
	}

	/**
	 * Constructor for the behavior class.
	 * 
	 * @param inputObject
	 */

	public XPXManageArticlePopupPanelBehavior(Composite ownerComposite,
			String formId, Object inputObject) {
		super(ownerComposite, formId);
		this.page = (XPXManageArticlePopupPanel) ownerComposite;
		if (!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(),
				"ArticleKey")))
			this.getArticleDetails(page.getPageInput());
		initialize();
		
	}

	/**
	 * This method initializes the behavior class.
	 */
	public void initialize() {
		if (YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(), "ArticleKey"))){
			// prepare input XML for getting Article Details
		/*Element eleInput = YRCXmlUtils.createDocument("Team").getDocumentElement();
			eleInput.setAttribute("RequestedByUsersTeamId", getModel("UserNameSpace").getAttribute("DataSecurityGroupId"));
			String[] apinames = {"XPXGetDivisionsManagedByTeam","getOrganizationList"};
			Document[] docInput = {(eleInput.getOwnerDocument()),
					YRCXmlUtils.createFromString("<Organization IsEnterprise='Y'/>"),
			};				
			YRCApiContext context = new YRCApiContext();
			context.setApiNames(apinames);
			context.setInputXmls(docInput);
			context.setFormId("com.xpedx.sterling.rcp.pca.tasks.articles.screen.XPXManageArticlePopupPanel");

			callApi(context);*/
			// JIRA 3622 - Performance Fix - Division Cache 
			//XPXCacheManager.getInstance().getDivisionList(getModel("UserNameSpace").getAttribute("DataSecurityGroupId"), this);
			
			String[] apinames = {"getOrganizationList" /*, "getOrganizationList"*/};
			Document[] docInput = {
					YRCXmlUtils.createFromString("<Organization IsEnterprise='Y'/>"),
				//	YRCXmlUtils.createFromString("<Organization  IsNode='Y'>  <Extn ExtnBrandCode='XPED' ExtnBrandCodeQryType='LIKE'/> </Organization>"),
			};				
			YRCApiContext context = new YRCApiContext();
			context.setApiNames(apinames);
			context.setInputXmls(docInput);
			context.setFormId("com.xpedx.sterling.rcp.pca.tasks.articles.screen.XPXManageArticlePopupPanel");

			callApi(context);
		}
	}

	private void getArticleDetails(Element pageInput) {

		this.setControlState();

		YRCApiContext context = new YRCApiContext();
		context.setFormId("com.xpedx.sterling.rcp.pca.tasks.articles.screen.XPXManageArticlePopupPanel");
//		context.setApiName("getXPXArticleDetailsService");

		// prepare input XML for getting Article Details
		Element eleInput = YRCXmlUtils.createDocument("XPXArticle").getDocumentElement();
		eleInput.setAttribute("ArticleKey", YRCXmlUtils.getAttribute(page.getPageInput(), "ArticleKey"));
		eleInput.setAttribute("ArticleType", YRCXmlUtils.getAttribute(page.getPageInput(), "ArticleType"));
		eleInput.setAttribute("RequestedByUsersTeamId", getModel("UserNameSpace").getAttribute("DataSecurityGroupId"));
		String[] apinames = {"getOrganizationList","getXPXArticleDetailsService"};
		Document[] docInput = {YRCXmlUtils.createFromString("<Organization IsEnterprise='Y'/>"),
				eleInput.getOwnerDocument()
		};			
		// Set the Input document
		context.setInputXmls(docInput);
		context.setApiNames(apinames);
		callApi(context);
	}

	public void create() {
		
		
		// prepare input XML Saving the Article Details
		Element eleInput = getTargetModel("SaveArticle");
		if (!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(),"ArticleKey"))) {
			eleInput.setAttribute("ArticleKey", YRCXmlUtils.getAttribute(page.getPageInput(), "ArticleKey"));
		}
		String articlelType = YRCXmlUtils.getAttribute(eleInput,"ArticleType");		
		
		if(!YRCPlatformUI.isVoid(articlelType)&& "S".equals(articlelType))
		{
			
		}
		String OrganizationCode = YRCXmlUtils.getAttribute(eleInput,"OrganizationCode");		
		
		if(!YRCPlatformUI.isVoid(articlelType) && "S".equals(articlelType))
		{
			if(OrganizationCode==null || OrganizationCode.trim().length()<=0)
			{
				YRCPlatformUI.showError("Create Article:Mandatory Parameter", "Please select Storefront ID");
				return ;
			}
		}
		
		if(!YRCPlatformUI.isVoid(articlelType)&& "D".equals(articlelType))
		{
			// Retrieve and stamp Divisions in eleInput
			
			
				if(OrganizationCode==null || OrganizationCode.trim().length()<=0)
				{
					YRCPlatformUI.showError("Create Article:Mandatory Parameter", "Please select Storefront ID");
					return ;
				}
			
			Element eleAssignToDivs = getModel("AssignedDivisions");
			ArrayList<Element> listAssignedDivs = YRCXmlUtils.getChildren(eleAssignToDivs, "Organization");
			StringBuffer divs = new StringBuffer();
			if(listAssignedDivs != null && listAssignedDivs.size()>0){
				divs.append("|");
				for (Element eleAssignedDiv : listAssignedDivs) {
					divs.append(eleAssignedDiv.getAttribute("OrganizationCode")).append("|");
				}
			}else{
				eleInput.setAttribute("XPXDivision", "N/A");
			}
			eleInput.setAttribute("XPXDivision", divs.toString());
			//eleInput.setAttribute("OrganizationCode", "");
		}
		
		
		if(!YRCPlatformUI.isVoid(articlelType)&& "D".equals(articlelType))
		{
			String DivisionId=eleInput.getAttribute("XPXDivision");
			if(DivisionId==null || DivisionId.trim().length()<=0)
			{
				YRCPlatformUI.showError("Create Article:Mandatory Parameter", "Please select Division");
				return ;
			}
		}
		
		

		
		String[] apinames = null;
		Document[] docInput = null;
		if (!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(),"ArticleKey"))) {
			apinames = new String[]{"changeXPXArticleService"};
			docInput = new Document[]{
				eleInput.getOwnerDocument()
			};
		} else {
			eleInput.setAttribute("CustomerID", "N/A");
			apinames = new String[]{"CreateXpedxArticle"};
			docInput = new Document[]{
					eleInput.getOwnerDocument()
			};
		}

		YRCApiContext context = new YRCApiContext();
		context.setFormId("com.xpedx.sterling.rcp.pca.tasks.articles.screen.XPXManageArticlePopupPanel");
		context.setApiNames(apinames);
		context.setInputXmls(docInput);
		callApi(context);
	}

	@Override
	public void handleApiCompletion(YRCApiContext ctx) {

		if (ctx.getInvokeAPIStatus() < 0) { // api call failed
			String[] apinames = ctx.getApiNames();
			for (int i = 0; i < apinames.length; i++) {
				String apiname = apinames[i];
				if ("CreateXpedxArticle".equals(apiname)
						|| "changeXPXArticleService".equals(apiname)) {
					Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
					if("Errors".equals(outXml.getNodeName())){
						YRCPlatformUI.trace("Failed To Save the Article: ", outXml);
						YRCPlatformUI.showError("Failed!", "Unable save the Article at this time.");
					}
				} else {
					YRCPlatformUI.showError("Failed!", "Unable to perform the Action.");
				}
			}
			
		} else {
			String[] apinames = ctx.getApiNames();
			for (int i = 0; i < apinames.length; i++) {
				String apiname = apinames[i];
				if ("getXPXArticleDetailsService".equals(apiname)
						|| "CreateXpedxArticle".equals(apiname)
						|| "changeXPXArticleService".equals(apiname)) {
					Element eleOutput = ctx.getOutputXmls()[i].getDocumentElement(); 
					setModel(eleOutput);
					
					setModel("Divisions",YRCXmlUtils.getXPathElement(eleOutput, "/XPXArticle/Divisions/OrganizationList"));
					setModel("AssignedDivisions", YRCXmlUtils.getXPathElement(eleOutput, "/XPXArticle/AssignedDivisions/OrganizationList"));
					setFieldValue("txtArticleDesc", eleOutput.getAttribute("Article"));
					setControlVisible("lblLastModifiedData", true);
					setControlVisible("txtLastModifiedData", true);
					setControlVisible("lblModifiedBy", true);
					setControlVisible("txtModifiedBy", true);
					String articlelType = YRCXmlUtils.getAttribute(eleOutput,"ArticleType");		
					if(!YRCPlatformUI.isVoid(articlelType)&& "S".equals(articlelType))
					{
						setControlVisible("lblStorefronCode", true);
						setControlVisible("comboStorefrontCode", true);
						setControlVisible("pnlDivisions", true);
					}
					else
					{
						setControlVisible("lblStorefronCode", true);
						setControlVisible("comboStorefrontCode", true);
						setControlVisible("pnlDivisions", true);
						
					}
					if ("CreateXpedxArticle".equals(apiname)
							|| "changeXPXArticleService".equals(apiname)) {
						page.setPageInput(eleOutput);
						this.setControlState();
						if(null != page.getInvokerPage())
							page.getInvokerPage().getMyBehavior().search();
						YRCPlatformUI.showInformation("Success","Successfully_Saved_Article");
					}
					
				} 
				
				else if("XPXGetDivisionsManagedByTeam".equals(apiname)){
					Element eleOutput = ctx.getOutputXmls()[i].getDocumentElement(); 
					
					setModel("Divisions",eleOutput);
					setModel("AssignedDivisions",YRCXmlUtils.createDocument("OrganizationList").getDocumentElement());
				}
				else if(("getOrganizationList".equals(apiname) && i==0) && storeFront ==null){
					Element eleOutput = ctx.getOutputXmls()[i].getDocumentElement(); 
					
					setModel("StoreFronts",eleOutput);
					
					
				}	
				else if("getOrganizationList".equals(apiname)){
					Element eleOutput = ctx.getOutputXmls()[i].getDocumentElement(); 
					if(eleOutput != null){
						setModel("Divisions", eleOutput);
					}
					else{
						eleOutput = null;
						setModel("Divisions", eleOutput);
					}
				}	
			}
			this.storeFront = null;
		}
	}

	/**
	 * while viewing Article, used to set ArticleName fields as Read only 
	 * and set the Create button Label as Update.
	 */
	private void setControlState() {
		setControlEditable("txtArticleName", false);
		setFieldValue("btnCreate", YRCPlatformUI.getString("Article_Update"));
	}

	public void addSelectedDivisions(SelectionEvent e) {
		Widget ctrl = e.widget;
        String ctrlName = (String)ctrl.getData("name");
        if(ctrlName != null)
        {
        	TableItem tblItems[] = page.getTblDivisions().getSelection();
            if(tblItems.length > 0){
            	Element eleAssignedDivisions = getModel("AssignedDivisions");
            	if(eleAssignedDivisions==null){
            		setModel("AssignedDivisions",YRCXmlUtils.createDocument("OrganizationList").getDocumentElement());
            		eleAssignedDivisions = getModel("AssignedDivisions");
            	}
            	Element eleDivisions = getModel("Divisions");
            	for(int i = 0; i < tblItems.length; i++)
                    if(!YRCPlatformUI.isVoid(tblItems[i]) && !tblItems[i].isDisposed())
                    {
                        Element eleSelected = (Element)tblItems[i].getData();
                        YRCXmlUtils.importElement(eleAssignedDivisions, eleSelected);
                        eleDivisions.removeChild(eleSelected);
                    }
            	repopulateModel("Divisions");
            	repopulateModel("AssignedDivisions");
            }
        }
		System.out.println("Manage Article: Add Selected Divisions");
		
	}

	public void removeSelectedDivisions(SelectionEvent e) {
		Widget ctrl = e.widget;
        String ctrlName = (String)ctrl.getData("name");
        if(ctrlName != null)
        {
        	TableItem tblItems[] = page.getTblAssignedToDivisions().getSelection();
            if(tblItems.length > 0){
            	Element eleDivisions = getModel("Divisions");
            	if(eleDivisions==null){
            		setModel("Divisions",YRCXmlUtils.createDocument("OrganizationList").getDocumentElement());
            		eleDivisions = getModel("Divisions");
            	}
            	Element eleAssignedDivisions = getModel("AssignedDivisions");
            	for(int i = 0; i < tblItems.length; i++){
            		if(!YRCPlatformUI.isVoid(tblItems[i]) && !tblItems[i].isDisposed())
                    {
                        Element eleSelected = (Element)tblItems[i].getData();
                        if(YRCPlatformUI.equals("N", eleSelected.getAttribute("AllowedToRemove"))){
                        	YRCPlatformUI.showInformation("Invalid", "Removing Division["+eleSelected.getAttribute("OrganizationCode")+"] assignment is not allowed, by this user.");
                        	break;
                        }
                        eleAssignedDivisions.removeChild(eleSelected);
                        YRCXmlUtils.importElement(eleDivisions, eleSelected);
                    }
            	}
            	repopulateModel("Divisions");
            	repopulateModel("AssignedDivisions");
            }
        }
		System.out.println("Manage Article: Remove Selected Assigned Divisions");
		
	}	

	
	 public void getDivisionList(String storeFront){
		 this.storeFront = storeFront;
			String apinames = "getOrganizationList";
			Document docInput = 
				YRCXmlUtils.createFromString("<Organization  IsNode='Y'>  <Extn ExtnBrandCode='"+storeFront+"' ExtnBrandCodeQryType='LIKE'/> </Organization>");
							
			YRCApiContext context = new YRCApiContext();
			context.setApiName(apinames);
			context.setInputXml(docInput);
			context.setFormId("com.xpedx.sterling.rcp.pca.tasks.articles.screen.XPXManageArticlePopupPanel");

			callApi(context);
	   }
}
