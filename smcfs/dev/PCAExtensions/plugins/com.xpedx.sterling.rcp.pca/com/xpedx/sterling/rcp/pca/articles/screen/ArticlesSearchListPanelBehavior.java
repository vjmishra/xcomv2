/*
 * Created on Jul 08,2010
 *
 */
package com.xpedx.sterling.rcp.pca.articles.screen;


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
import com.yantra.yfc.rcp.YRCXmlUtils;

/**
 * @author sdodda
 *
 * Articles Search and List Panel Behavior
 */
public class ArticlesSearchListPanelBehavior extends YRCBehavior {
	
	private ArticlesSearchListPanel page ;

	public ArticlesSearchListPanelBehavior(Composite ownerComposite, String formId, Object inputObject) {
        super(ownerComposite, formId,inputObject);
        this.page = (ArticlesSearchListPanel)getOwnerForm();
        init();
    }
    
	public void init() {
	    loadIntialDataAndSetModel();
	}
	
	/**
	 * Initially reset the Search Criteria to blank
	 */
	public void loadIntialDataAndSetModel() {
		Element eDummy = YRCXmlUtils.createDocument("XPXArticle").getDocumentElement();
        setModel("SearchCriteria", eDummy);

    }

    /**
     * Invokes get Article List Service
     */
    public void search() {
        YRCApiContext context = new YRCApiContext();
	    context.setApiName("getXPXArticleListService");
	    context.setFormId(getFormId());
	    context.setInputXml(prepareInputXMLToSearch());
	    callApi(context);
    }
    
    private Document prepareInputXMLToSearch() {
		Element eleSearchCriteria = getTargetModel("SearchCriteria");
		if(!YRCPlatformUI.isVoid(YRCXmlUtils.getAttribute(eleSearchCriteria, "XPXDivision"))){
			eleSearchCriteria.setAttribute("XPXDivision", "|"+YRCXmlUtils.getAttribute(eleSearchCriteria, "XPXDivision")+"|");
			eleSearchCriteria.setAttribute("XPXDivisionQryType", "LIKE");
		}
		return eleSearchCriteria.getOwnerDocument();
    }
    
    public void handleApiCompletion(YRCApiContext ctx) {
    	if (ctx.getInvokeAPIStatus() < 0) { // api call failed
    		//TODO: show exception message
    	}
    	else {
	    	if ("getXPXArticleListService".equals(ctx.getApiName())) {
	    		handleSearchApiCompletion(ctx);
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
		if(YRCPlatformUI.equals(ctrlName, "tblSearchResults"))
        {
            TableItem tblItems[] = page.tblSearchResults.getSelection();
            if(tblItems.length > 0)
            	for(int i = 0; i < tblItems.length; i++)
                    if(!YRCPlatformUI.isVoid(tblItems[i]) && !tblItems[i].isDisposed())
                    {
                        Element eleSelected = (Element)tblItems[i].getData();
                        eleSelected.removeAttribute("Article");
                        YRCPlatformUI.launchSharedTask(page,"com.xpedx.sterling.rcp.pca.sharedTasks.XPXCreateArticleSharedTask", eleSelected);
                    }
        }
	}

	/**
	 * Opens Create Article Pop-up by passing BuyerOrganizationCode and OrganizationCode(store front id)
	 */
	public void create() {
		Element eleCreateNew = YRCXmlUtils.createDocument("XPXArticle").getDocumentElement();
		Element eleInput = (Element)((YRCEditorInput)getInputObject()).getXml();
		eleCreateNew.setAttribute("OrganizationCode", (!YRCPlatformUI.equals("DEFAULT", getModel("UserNameSpace").getAttribute("EnterpriseCode"))?getModel("UserNameSpace").getAttribute("EnterpriseCode"):XPXConstants.DEFAULT_SFID));
		YRCPlatformUI.launchSharedTask(page,"com.xpedx.sterling.rcp.pca.sharedTasks.XPXCreateArticleSharedTask", eleCreateNew);
	}

}
