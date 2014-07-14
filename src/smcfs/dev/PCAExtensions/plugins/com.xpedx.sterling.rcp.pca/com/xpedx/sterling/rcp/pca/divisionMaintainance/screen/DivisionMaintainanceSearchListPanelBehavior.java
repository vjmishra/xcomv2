package com.xpedx.sterling.rcp.pca.divisionMaintainance.screen;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class DivisionMaintainanceSearchListPanelBehavior extends YRCBehavior {

	private DivisionMaintainanceSearchListPanel page;

	public DivisionMaintainanceSearchListPanelBehavior(Composite ownerComposite, String formId) {
		super(ownerComposite, formId);
		this.page = (DivisionMaintainanceSearchListPanel)getOwnerForm();
        init();
	}
	
	public void init() {
	    loadIntialDataAndSetModel();
	}
	
	public void loadIntialDataAndSetModel() {
		Element eDummy = YRCXmlUtils.createDocument("Organization").getDocumentElement();
        setModel("DivisionSearchCriteria", eDummy);
        
        // Get List Of Brands
        Document docGetBrandsIn = YRCXmlUtils.createDocument("CommonCode");
        docGetBrandsIn.getDocumentElement().setAttribute("CodeType", XPXConstants.COMMON_CODE_XPX_BRAND);
        /* commented because Brands are configured at DEFAULT Level
         * docGetBrandsIn.getDocumentElement().setAttribute("CallingOrganizationCode", YRCPlatformUI.getUserElement().getAttribute("EnterpriseCode"));
         */
        docGetBrandsIn.getDocumentElement().setAttribute("CallingOrganizationCode", XPXConstants.COMMON_CODE_XPX_BRAND_DEFAULT_ORG);
        
        YRCApiContext context = new YRCApiContext();
	    context.setApiName("XPXBrandCodesList");
	    context.setFormId(page.getFormId());
	    context.setInputXml(docGetBrandsIn);
	    callApi(context);
    }

	public void search() {
        YRCApiContext context = new YRCApiContext();
	    context.setApiName("getOrganizationList");
	    context.setFormId(page.getFormId());
	    context.setInputXml(prepareInputXMLToSearch());
	    callApi(context);
    }
	
	private Document prepareInputXMLToSearch() {
	    Element eleSearchCriteria = getTargetModel("DivisionSearchCriteria");
	    eleSearchCriteria.setAttribute("IsNode", "Y");
	    if(!YRCPlatformUI.isVoid(YRCXmlUtils.getAttributeValue(eleSearchCriteria, "Organization/Extn/@ExtnBrandCode"))){
	    	
	    	YRCXmlUtils.setAttributeValue(eleSearchCriteria, "Organization/Extn/@ExtnBrandCodeQryType", "LIKE");
	    }

	    String strDivision = eleSearchCriteria.getAttribute("OrganizationCodeOrName");
	    if(!YRCPlatformUI.isVoid(strDivision)) {
	    	Element eleCQ = YRCXmlUtils.createChild(eleSearchCriteria, "ComplexQuery");
	    	Element eleOr = YRCXmlUtils.createChild(eleCQ, "Or");
	    	
	    	Element eleExpCode = YRCXmlUtils.createChild(eleOr, "Exp");
	    	eleExpCode.setAttribute("Name", "OrganizationCode");
	    	eleExpCode.setAttribute("Value", strDivision);
	    	eleExpCode.setAttribute("QryType", "LIKE");
	    	
	    	Element eleExpName = YRCXmlUtils.createChild(eleOr, "Exp");
	    	eleExpName.setAttribute("Name", "OrganizationName");
	    	eleExpName.setAttribute("Value", strDivision);
	    	eleExpName.setAttribute("QryType", "LIKE");
	    	
	    } else {
	    	if(eleSearchCriteria.hasAttribute("OrganizationCodeOrName")){
	    		eleSearchCriteria.removeAttribute("OrganizationCodeOrName");
	    	}
	    }
	    
	    return eleSearchCriteria.getOwnerDocument();
    }
	
	public void handleApiCompletion(YRCApiContext ctx) {
    	if (ctx.getInvokeAPIStatus() < 0) { // api call failed
    		//TODO: show exception message
    	}
    	else {
	    	if ("getOrganizationList".equals(ctx.getApiName())) {
	    		handleSearchApiCompletion(ctx);
	    	} else if ("XPXBrandCodesList".equals(ctx.getApiName())) {
	    		setModel("BrandCodesList", ctx.getOutputXml().getDocumentElement());
	    	}
    	}
    	super.handleApiCompletion(ctx);
    }
	
	private void handleSearchApiCompletion(YRCApiContext ctx) {
		setModel(ctx.getOutputXml().getDocumentElement());
		page.tblResults.setFocus();
	}
	
	public void reset() {
        loadIntialDataAndSetModel();
    }
	
	public void mouseDoubleClick(MouseEvent e, String ctrlName) {
		if(YRCPlatformUI.equals(ctrlName, "tblDivisionSearchResults"))
        {
			TableItem tblItems[] = page.tblResults.getSelection();
			if(tblItems.length > 0)
				for(int i = 0; i < tblItems.length; i++)
	                if(!YRCPlatformUI.isVoid(tblItems[i]) && !tblItems[i].isDisposed())
	                {
	                    Element eleSelected = (Element)tblItems[i].getData();
	                    Element eleEditorInput = YRCXmlUtils.getCopy(eleSelected, true);
	                    eleEditorInput.getOwnerDocument().renameNode(eleEditorInput, eleEditorInput.getNamespaceURI(), "OrganizationTmp");
	                    Element eleBrandCodesList =getModel("BrandCodesList");
//	                    System.out.println("Opening XPXDivisionMaintainanceEditor with Input:" + YRCXmlUtils.getString(eleEditorInput));
	                    YRCPlatformUI.openEditor("com.xpedx.sterling.rcp.pca.divisionMaintainance.editor.XPXDivisionMaintainanceEditor", new YRCEditorInput(eleEditorInput,eleBrandCodesList, new String[] {""}, "DivisionMaintainance"));
	                }
        }
	}

	public String getBrandDescription(Element eleTableItem) {
		Element eleExtn = YRCXmlUtils.getChildElement(eleTableItem, "Extn");
		String strBrands = eleExtn.getAttribute("ExtnBrandCode");
		StringBuffer buffBrands = new StringBuffer("");
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

}
