/*
 * Created on Jul 08,2010
 *
 */
package com.xpedx.sterling.rcp.pca.articles.screen;


import java.util.HashMap;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.yantra.yfc.rcp.YRCDesktopUI;

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
	private Element searchResultsElement = null;
	private  HashMap<String, String> customersMap = null;

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
    	searchResultsElement = null;
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
	    		YRCDesktopUI.getCurrentPart().showBusy(true);
	    		handleSearchApiCompletion(ctx);
	    		YRCDesktopUI.getCurrentPart().showBusy(false);
	    	}
	    	if ("getCustomerList".equals(ctx.getApiName())) {	    		     		  
	    		handleCustomerListApiCompletion(ctx);
	    	}
    	}
    }

	private void handleSearchApiCompletion(YRCApiContext ctx) {
		//EB-1088 show the actual Customer Name and Account number
		searchResultsElement = ctx.getOutputXml().getDocumentElement();
		getCustomerListDetails(searchResultsElement);
	}
	
	//EB-1088 show the actual Customer Name and Account number
	private void handleCustomerListApiCompletion(YRCApiContext ctx) {
		System.out.println(YRCXmlUtils.getString(ctx.getOutputXml()));
		Element eleCustomerListOutput = ctx.getOutputXml().getDocumentElement();
		NodeList nodeList	=	eleCustomerListOutput.getElementsByTagName("Customer");
		customersMap = new HashMap<String, String>();
		for(int i=0;i<nodeList.getLength();i++){
			Element  eleCustomer=(Element) nodeList.item(i);
			String customerID = eleCustomer.getAttribute("CustomerID");
			NodeList buyerOrganization	=	eleCustomer.getElementsByTagName("BuyerOrganization");
			String customerName = ((Element)buyerOrganization.item(0)).getAttribute("OrganizationName");			 
			customersMap.put(customerID, customerName);
		}
		setModel(ctx.getOutputXml().getDocumentElement()); 
		if(searchResultsElement!=null){
			setModel(searchResultsElement);
		}
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
	//EB-1088 show the actual Customer Name and Account number
	public void getCustomerListDetails(Element element) {
		Element eleXPXArticleList = element;
		if(eleXPXArticleList!=null){
			YRCApiContext context = new YRCApiContext();
			context.setApiName("getCustomerList");
			context.setFormId(getFormId());	
			YRCXmlUtils.getString(eleXPXArticleList.getOwnerDocument());			
			Document docOutput = eleXPXArticleList.getOwnerDocument();
			Element eleXPXArticleList_output = docOutput.getDocumentElement();
			NodeList nodeList	=	eleXPXArticleList_output.getElementsByTagName("XPXArticle");
			Document docCustomerList = YRCXmlUtils.createDocument("Customer");
			Element eleCustomerListInput = docCustomerList.getDocumentElement();
			Element eleCustomerComplexQuery =  YRCXmlUtils.createChild(eleCustomerListInput, "ComplexQuery");
			eleCustomerComplexQuery.setAttribute("Operator", "OR");
			Element eleCustomerComplexQueryOR =  YRCXmlUtils.createChild(eleCustomerComplexQuery, "Or");
			for(int i=0;i<nodeList.getLength();i++){
				Element  eleArticle=(Element) nodeList.item(i);
				String customerID = eleArticle.getAttribute("CustomerID");				
				if ((customerID!=null) && (!customerID.isEmpty()) && (!customerID.equalsIgnoreCase("N/A")) ){
					Element eleExpCustomer =  YRCXmlUtils.createChild(eleCustomerComplexQueryOR, "Exp");
					eleExpCustomer.setAttribute("Name", "CustomerID");
					eleExpCustomer.setAttribute("Value", customerID);
				}				
			}
			context.setInputXml(docCustomerList);
			System.out.println(YRCXmlUtils.getString(context.getInputXml()));

			callApi(context);			
		}
	}
	//EB-1088 show the actual Customer Name and Account number
	public String getCustomerName(String customerID)
	{
		if(customersMap!=null && customersMap!=null)
			return customersMap.get(customerID);	

		return "";	

	}
}
