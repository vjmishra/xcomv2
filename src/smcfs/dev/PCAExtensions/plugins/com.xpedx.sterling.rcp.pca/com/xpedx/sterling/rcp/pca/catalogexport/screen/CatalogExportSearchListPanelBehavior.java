/*
 * Created on Jul 08,2010
 *
 */
package com.xpedx.sterling.rcp.pca.catalogexport.screen;


import java.util.ArrayList;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;

/**
 * @author vchandra
 *
 * Articles Search and List Panel Behavior
 */
public class CatalogExportSearchListPanelBehavior extends YRCBehavior {
	
	private CatalogExportSearchListPanel page ;
	private Element eleBrands=null;
	private String strPricingWarehouse = null;
	private boolean canEdit = true;
	public CatalogExportSearchListPanelBehavior(Composite ownerComposite, String formId, Object inputObject) {
        super(ownerComposite, formId,inputObject);
        this.page = (CatalogExportSearchListPanel)getOwnerForm();
        eleBrands=(Element) ((YRCEditorInput) inputObject).getInputObject();
        setEleBrands(eleBrands);

        // Get the Pricing Warehouse Code.
        Element screenInput = ((YRCEditorInput) inputObject).getXml();
        Element eleOrgExtn = YRCXmlUtils.getChildElement(screenInput, "Extn");
        if(!YRCPlatformUI.isVoid(eleOrgExtn.getAttribute("ExtnPriceWarehouse")) 
        		&& !YRCPlatformUI.isVoid(eleOrgExtn.getAttribute("ExtnEnvtId"))){
        	strPricingWarehouse = eleOrgExtn.getAttribute("ExtnPriceWarehouse")+"_"+eleOrgExtn.getAttribute("ExtnEnvtId");
        }
        if(!YRCPlatformUI.equals(screenInput.getAttribute("OrganizationCode"),strPricingWarehouse)){
        	page.getBtnCreate().setVisible(false);
        	canEdit = false;
        }
        init();
    }
    
	public void init() {
	    loadIntialDataAndSetModel();
	    YRCApiContext context = new YRCApiContext();
	    context.setApiName("getQueryTypeList");
	    context.setFormId(getFormId());
	    context.setInputXml(YRCXmlUtils.createDocument("QueryType"));
	    callApi(context);	    
	}
	
	/**
	 * Initially reset the Search Criteria to blank
	 */
	public void loadIntialDataAndSetModel() {
		Element eDummy = YRCXmlUtils.createDocument("XPXCatalogExp").getDocumentElement();
        setModel("SearchCriteria", eDummy);
        setModel("BrandCodesList", eleBrands);
        
    }

    /**
     * Invokes get Article List Service
     */
    public void search() {
    	if(!YRCPlatformUI.isVoid(strPricingWarehouse)){
	        YRCApiContext context = new YRCApiContext();
		    context.setApiName("XPXGetCatalogExportListService");
		    context.setFormId(getFormId());
		    context.setInputXml(prepareInputXMLToSearch());
		    callApi(context);
    	}
    }
    
    private Document prepareInputXMLToSearch() {
		Element eleInput = (Element)((YRCEditorInput)getInputObject()).getXml();
		Element eleSearchCriteria = getTargetModel("SearchCriteria");
		eleSearchCriteria.setAttribute("OrganizationKey", strPricingWarehouse);
		ArrayList divisionArray = new ArrayList();
		divisionArray = XPXUtils.getRefDiv();
		
		
		Element e4 = YRCXmlUtils.createChild(eleSearchCriteria, "SearchCriteria");
		Element attrElemComplex = YRCXmlUtils.createChild(e4, "ComplexQuery");
		Element attrOr = YRCXmlUtils.createChild(attrElemComplex, "Or");
		for ( int i=0; i<divisionArray.size();i++) {
	          String division = (String) divisionArray.get(i);
	          if (division != null && division != " ") {
	                Element attrName = YRCXmlUtils.createChild(attrOr, "Exp");
	                attrName.setAttribute("Name", "DivisionID");
	                attrName.setAttribute("Value", division);
	                attrOr.appendChild(attrName);
	          }

	    }
	    if(!YRCPlatformUI.isVoid(YRCXmlUtils.getAttributeValue(eleSearchCriteria, "XPXCatalogExp/@BrandCode"))){
	    	
	    	YRCXmlUtils.setAttributeValue(eleSearchCriteria, "XPXCatalogExp/@BrandCodeQryType", "LIKE");
	    }
	    
		return eleSearchCriteria.getOwnerDocument();
    }
    
    public void handleApiCompletion(YRCApiContext ctx) {
    	if (ctx.getInvokeAPIStatus() < 0) { // api call failed
    		//TODO: show exception message
    	}
    	else {
	    	if ("XPXGetCatalogExportListService".equals(ctx.getApiName())) {
	    		handleSearchApiCompletion(ctx);
	    	}
	    	if ("getQueryTypeList".equals(ctx.getApiName())) {
	    		setModel("getQueryTypeList_output",ctx.getOutputXml().getDocumentElement());
	    	}	    	
    	}
    }

	private void handleSearchApiCompletion(YRCApiContext ctx) {
		setModel(ctx.getOutputXml().getDocumentElement());
	}
	
    /**
     * Reset the Search Criteria.
     */
    public void reset() {
        loadIntialDataAndSetModel();
    }

	public void mouseDoubleClick(MouseEvent e, String ctrlName) {
		if(YRCPlatformUI.equals(ctrlName, "tblSearchResults") && canEdit)
        {
            TableItem tblItems[] = page.tblSearchResults.getSelection();
            if(tblItems.length > 0)
            	for(int i = 0; i < tblItems.length; i++)
                    if(!YRCPlatformUI.isVoid(tblItems[i]) && !tblItems[i].isDisposed())
                    {
                        Element eleSelected = (Element)tblItems[i].getData();
                        //eleSelected.removeAttribute("Article");
                        YRCPlatformUI.launchSharedTask(page,"com.xpedx.sterling.rcp.pca.sharedTasks.XPXCreateCatalogExportSharedTask", eleSelected);
                    }
        }
	}

	/**
	 * Opens Create Article Pop-up by passing BuyerOrganizationCode and OrganizationCode(store front id)
	 */
	public void create() {
		Element eleCreateNew = YRCXmlUtils.createDocument("XPXCatalogExp").getDocumentElement();
		Element eleInput = (Element)((YRCEditorInput)getInputObject()).getXml();
		eleCreateNew.setAttribute("OrganizationKey", YRCXmlUtils.getAttribute(eleInput, "OrganizationKey"));
	    
		YRCPlatformUI.launchSharedTask(page,"com.xpedx.sterling.rcp.pca.sharedTasks.XPXCreateCatalogExportSharedTask", eleCreateNew);
	}
	
	public Element getEleBrands() {
		return eleBrands;
	}

	public void setEleBrands(Element eleBrands) {
		this.eleBrands = eleBrands;
	}
	//Get Brands Description
	public String getBrandDescription(Element eleTableItem) {
		String strBrands = eleTableItem.getAttribute("BrandCode");
		StringBuffer buffBrands = new StringBuffer();
		if(!YRCPlatformUI.isVoid(strBrands)) {
			String[] arrayBrand = strBrands.split(",");
			Element eleBrandCodesList = getModel("BrandCodesList");
			if(!YRCPlatformUI.isVoid(eleBrandCodesList)){
				for (String strBrand : arrayBrand) {
					Element eleBrand = (Element)YRCXPathUtils.evaluate(eleBrandCodesList, "./CommonCode[@CodeValue='"+strBrand+"']", XPathConstants.NODE);
					if(!YRCPlatformUI.isVoid(eleBrand)){
						if(!YRCPlatformUI.isVoid(eleBrand.getAttribute("CodeShortDescription"))){
							if(buffBrands.length()>0)
								buffBrands.append(",");
							buffBrands.append(eleBrand.getAttribute("CodeShortDescription"));
						}
					} else {
						if(buffBrands.length()>0)
							buffBrands.append(",");
						buffBrands.append(strBrand);
					}
				}
			}
		}
		return buffBrands.toString();
	}
	public String getUrl(Element eleTableItem){
		String strUrl = eleTableItem.getAttribute("Url");
		if(!YRCPlatformUI.isVoid(strUrl)){
			XPXUtils.accessURL(strUrl, "");
		}
		
		return strUrl;
	}
}
