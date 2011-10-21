/*
 * Created on Jul 09,2010
 *
 */
package com.xpedx.sterling.rcp.pca.tasks.catalogexport.screen;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

/**
 * @author vchandra
 * 
 * Behavior for Manage Articles Panel
 */

public class XPXManageCatalogExportPopupPanelBehavior extends YRCBehavior {

	private XPXManageCatalogExportPopupPanel page;

	/**
	 * Constructor for the behavior class.
	 */

	public XPXManageCatalogExportPopupPanelBehavior(Composite ownerComposite,
			String formId, Element eleBrandCodes) {
		super(ownerComposite, formId);
		this.page = (XPXManageCatalogExportPopupPanel) ownerComposite;
		setModel("BrandCodesList", eleBrandCodes);
		if (!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(),
				"CatalogExportKey")))
			getCatalogExportDetails(page.getPageInput());
		init();
	}

	/**
	 * Constructor for the behavior class.
	 * 
	 * @param inputObject
	 */

	public XPXManageCatalogExportPopupPanelBehavior(Composite ownerComposite,
			String formId, Object inputObject) {
		super(ownerComposite, formId);
		this.page = (XPXManageCatalogExportPopupPanel) ownerComposite;
		if (!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(),
				"Url")))
			this.getCatalogExportDetails(page.getPageInput());
		init();
	}

	/**
	 * This method initializes the behavior class.
	 */
	public void init() {
		// TODO Nothing....
	}

	private void getCatalogExportDetails(Element pageInput) {

		this.setControlState();
		setModel(pageInput);
		/*YRCApiContext context = new YRCApiContext();
		context.setFormId(page.getFormId());
		context.setApiName("XPXGetCatalogExportDetailsService");

		// prepare input XML for getting Article Details
		Element eleInput = YRCXmlUtils.createDocument("XPXCatalogExp").getDocumentElement();
		eleInput.setAttribute("Url", YRCXmlUtils.getAttribute(page.getPageInput(), "Url"));
		eleInput.setAttribute("Label", YRCXmlUtils.getAttribute(page.getPageInput(), "Label"));
			
		// Set the Input document
		context.setInputXml(eleInput.getOwnerDocument());
		callApi(context);*/
	}

	public void create() {
		
		YRCApiContext context = new YRCApiContext();
		context.setFormId(page.getFormId());
		
		// prepare input XML Saving the Article Details
		Element eleInput = getTargetModel("SaveArticle");
		if (!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(),"Url"))) {
			eleInput.setAttribute("Url", eleInput.getAttribute("Url"));
			eleInput.setAttribute("CatalogExportKey", YRCXmlUtils.getAttribute(page.getPageInput(), "CatalogExportKey"));
			context.setApiName("XPXChangeCatalogExportService");
		} else {
			context.setApiName("XPXCreateCatalogExport");
		}
		eleInput.setAttribute("OrganizationKey", YRCXmlUtils.getAttribute(page.getPageInput(), "OrganizationKey"));
				
		// Set the Input document
		context.setInputXml(eleInput.getOwnerDocument());
		callApi(context);
	}
	public void delete() {
		
		YRCApiContext context = new YRCApiContext();
		context.setFormId(page.getFormId());
		// prepare input XML Saving the Article Details
		Element eleInput = getTargetModel("SaveArticle");
		if (!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(),"Url")) 
				|| !YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(page.getPageInput(),"CatalogExportKey"))) {
			eleInput.setAttribute("CatalogExportKey", YRCXmlUtils.getAttribute(page.getPageInput(), "CatalogExportKey"));
			context.setApiName("XPXDeleteCatalogExportService");
		}						
		// Set the Input document
		context.setInputXml(eleInput.getOwnerDocument());
		callApi(context);
	}

	@Override
	public void handleApiCompletion(YRCApiContext ctx) {

		if (ctx.getInvokeAPIStatus() < 0) { // api call failed
			if ("XPXCreateCatalogExport".equals(ctx.getApiName())
					|| "XPXChangeCatalogExportService".equals(ctx.getApiName()) || "XPXDeleteCatalogExportService".equals(ctx.getApiName())) {
				Element outXml = ctx.getOutputXml().getDocumentElement();
				if("Errors".equals(outXml.getNodeName())){
					YRCPlatformUI.trace("Failed To Save the URL: ", outXml);
					YRCPlatformUI.showError("Failed!", "Unable save the URL at this time.");
				}
			} else {
				YRCPlatformUI.showError("Failed!", "Unable to perform the Action.");
			}
		} else {
			/*if ("XPXCreateCatalogExport".equals(ctx.getApiName())
					|| "XPXGetCatalogExportDetailsService".equals(ctx.getApiName())) {
				Element eleOutput = ctx.getOutputXml().getDocumentElement(); 
				setModel(eleOutput);
				setFieldValue("txtURL", eleOutput.getAttribute("Url"));
				setFieldValue("txtLabel", eleOutput.getAttribute("Label"));
				//setControlVisible("lblLastModifiedData", true);
				//setControlVisible("txtLastModifiedData", true);
				//setControlVisible("lblModifiedBy", true);
				//setControlVisible("txtModifiedBy", true);
			}*/
			if ("XPXCreateCatalogExport".equals(ctx.getApiName())
					|| "XPXChangeCatalogExportService".equals(ctx.getApiName())) {
				Element eleOutput = ctx.getOutputXml().getDocumentElement();
				setModel(eleOutput);
				page.setPageInput(eleOutput);
				this.setControlState();
				if(null != page.getInvokerPage())
				page.getInvokerPage().getMyBehavior().search();
				YRCPlatformUI.showInformation("Success","Successfully_Saved_Export");
			}
			if ("XPXDeleteCatalogExportService".equals(ctx.getApiName())) {
				//Element eleOutput = ctx.getOutputXml().getDocumentElement();
				if(null != page.getInvokerPage()){
					page.getInvokerPage().getMyBehavior().search();
				}
				this.page.getParent().getShell().close();
				YRCPlatformUI.showInformation("Success","Successfully_Deleted_Export");
			}
		}
	}

	/**
	 * while viewing Article, used to set ArticleName fields as Read only 
	 * and set the Create button Label as Update.
	 */
	private void setControlState() {
		//setControlEditable("txtURL", false);
		setControlEditable("txtLabel", false);
		setFieldValue("btnCreate", YRCPlatformUI.getString("Article_Update"));
		setFieldValue("btnCreate", YRCPlatformUI.getString("Article_Update"));
		getControl("btnDelete").setEnabled(true);
	}

}
