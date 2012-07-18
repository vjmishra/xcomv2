package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.util.HashMap;
import java.util.Map;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.catalog.helper.CatalogContextHelper;
import com.sterlingcommerce.webchannel.core.WCMashupAction;

public class XPEDXMainCategoriesAction  extends WCMashupAction{

	private static final String SEARCH_CATALOG_INDEX_MASHUP_ID = "MainCategories";
	private static final Logger log = Logger.getLogger(XPEDXMainCategoriesAction.class);
	protected Element mainCatsDoc;
	protected String categoryDepth = "2";

	public String execute(){

        Document outDoc = null;
        //outDoc = CatalogContextHelper.getCategoryFromCache(wcContext, "1");
        if(outDoc==null){
            try {
            	if (wcContext.isGuestUser()) { 
            		Map<String, String> valueMap1 = new HashMap<String, String>();
            		valueMap1.put("/SearchCatalogIndex/@CallingOrganizationCode", getWCContext().getStorefrontId());
            		valueMap1.put("/SearchCatalogIndex/@OrganizationCode", "");
            		valueMap1.put("/SearchCatalogIndex/@CategoryDomain", "");
            		valueMap1.put("/SearchCatalogIndex/@DisplayLocalizedFieldInLocale", getWCContext().getSCUIContext().getUserPreferences().getLocale().getLocaleCode());
            		valueMap1.put("/SearchCatalogIndex/Item/CustomerInformation/@CustomerID", "");
            		valueMap1.put("/SearchCatalogIndex/@CategoryDepth", getCategoryDepth());
            		Element input1 = null;
            		input1 = WCMashupHelper.getMashupInput(SEARCH_CATALOG_INDEX_MASHUP_ID,
            		valueMap1, wcContext.getSCUIContext());
            		Object obj1 = WCMashupHelper.invokeMashup(SEARCH_CATALOG_INDEX_MASHUP_ID,
            		input1, wcContext.getSCUIContext());
            		outDoc = ((Element) obj1).getOwnerDocument();
            	} else {
            		outDoc = prepareAndInvokeMashup(SEARCH_CATALOG_INDEX_MASHUP_ID).getOwnerDocument();
            	}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
        
        if(outDoc == null){
            log.error("Exception in reading the Categories  ");
            return ERROR;
        }
        this.mainCatsDoc = outDoc.getDocumentElement();
        return SUCCESS;
    }

	public Element getMainCatsDoc()
    {
        return this.mainCatsDoc;
    }

    public String getCategoryDepth() {
		return categoryDepth;
	}

	public void setCategoryDepth(String categoryDepth) {
		this.categoryDepth = categoryDepth;
	}


}
