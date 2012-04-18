/*
 * Created on April 17,2012
 *
 */
package com.xpedx.sterling.rcp.pca.orderhistory.screen;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.orderhistory.editor.XPXOrderHistoryEditor;
import com.xpedx.sterling.rcp.pca.orderhistory.screen.XPXOrderHistoryPanel;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXOrderHistoryPanelBehavior extends YRCBehavior {

	
	private XPXOrderHistoryPanel page ;
	private String defaultOrgCode ;
	private String refOrderHdrKey;

	public XPXOrderHistoryPanelBehavior(Composite ownerComposite, String formId, Object inputObject) {
        super(ownerComposite, formId,inputObject);
        this.page = (XPXOrderHistoryPanel)getOwnerForm();
        this.defaultOrgCode = "";
//        init();
        createSearchBy();
        createOrderType();
        createOrderStatus();
    }
	
	@Override
	protected void init() {
		String api[] = {
	            "getOrganizationList"
	        };
		Document doc[] = {
				XPXUtils.getOrganizationListInput(YRCPlatformUI.getUserId()).getOwnerDocument()
		};
	    callApis(api, doc);
		super.init();
	}
	
	public void createSearchBy(){
		
		Element elemModel = YRCXmlUtils.createDocument("SearchBy").getDocumentElement();
		
		Element attrElemComplex1 = YRCXmlUtils.createChild(elemModel, "Search");
		attrElemComplex1.setAttribute("SearchByValue", "WebConf");
		attrElemComplex1.setAttribute("SearchByDescription", "Web Confirmation");

		Element attrElemComplex2 = YRCXmlUtils.createChild(elemModel, "Search");
		attrElemComplex2.setAttribute("SearchByValue", "OrdNum");
		attrElemComplex2.setAttribute("SearchByDescription", "Order Number");

		Element attrElemComplex3 = YRCXmlUtils.createChild(elemModel, "Search");
		attrElemComplex3.setAttribute("SearchByValue", "ItemNum");
		attrElemComplex3.setAttribute("SearchByDescription", "Item Number");
							
		setModel("SearchBy",elemModel);
	}
	
	public void createOrderType(){
		
		Element elemModel = YRCXmlUtils.createDocument("OrderType").getDocumentElement();
		
		
		Element attrElemComplex1 = YRCXmlUtils.createChild(elemModel, "OrdType");
		attrElemComplex1.setAttribute("OrderTypeValue", "AO");
		attrElemComplex1.setAttribute("OrderTypeDescription", "All Orders");
		
		
		Element attrElemComplex2 = YRCXmlUtils.createChild(elemModel, "OrdType");
		attrElemComplex2.setAttribute("OrderTypeValue", "FO");
		attrElemComplex2.setAttribute("OrderTypeDescription", "Fulfillment Orders");

		Element attrElemComplex3 = YRCXmlUtils.createChild(elemModel, "OrdType");
		attrElemComplex3.setAttribute("OrderTypeValue", "CO");
		attrElemComplex3.setAttribute("OrderTypeDescription", "Customer Orders");
							
		setModel("OrderType",elemModel);
	}
	
	public void createOrderStatus(){
		
		Element elemModel = YRCXmlUtils.createDocument("OrderStatus").getDocumentElement();
		
		Element attrElemComplex1 = YRCXmlUtils.createChild(elemModel, "OrdStatus");
		attrElemComplex1.setAttribute("OrderStatusValue", "OpenOrd");
		attrElemComplex1.setAttribute("OrderStatusDescription", "Open Orders");
		
		Element attrElemComplex2 = YRCXmlUtils.createChild(elemModel, "OrdStatus");
		attrElemComplex2.setAttribute("OrderStatusValue", "BackOrd");
		attrElemComplex2.setAttribute("OrderStatusDescription", "Backordered");

		Element attrElemComplex3 = YRCXmlUtils.createChild(elemModel, "OrdStatus");
		attrElemComplex3.setAttribute("OrderStatusValue", "OrdNum");
		attrElemComplex3.setAttribute("OrderStatusDescription", "Shipped");
							
		setModel("OrderStatus",elemModel);
		
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
        	if(YRCPlatformUI.equals(ctx.getApiName(), "getOrganizationList"))
        		this.setOrganizationList(ctx);
	        else if(YRCPlatformUI.equals(ctx.getApiName(), "getXPXReferenceOrderListService"))
	        {
	        	setModel(ctx.getOutputXml().getDocumentElement());
	        }
	        else if(YRCPlatformUI.equals(ctx.getApiName(), "changeXPXRefOrderHdr"))
	        {
	        	Element outXml=ctx.getOutputXml().getDocumentElement();
	        	//refresh search results
	        	search();
	        }        	
	        else if(YRCPlatformUI.equals(ctx.getApiName(), "XPXReprocessReferenceOrderService"))
	        {
	        	Element outXml=ctx.getOutputXml().getDocumentElement();
	        }        	
	        
        }
		
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
            elemModel = YRCXmlUtils.createDocument("XPXRefOrderHdr").getDocumentElement();
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
			openMultipleOrders(tblItems);
        }
	}

	public void openMultipleOrders(TableItem[] tblItems) {
		
		if(tblItems.length > 0)
        	for(int i = 0; i < tblItems.length; i++){
        		Element orderElement = (Element)tblItems[i].getData();
        		
        		orderElement = YRCXmlUtils.getCopy(orderElement, false);
                if(!YRCPlatformUI.isVoid(tblItems[i]) && !tblItems[i].isDisposed())
                {
                	String currRefOrderHdrKey = orderElement.getAttribute("RefOrderHdrKey");

                    boolean isRefOrderEditorFound = false;
                    IEditorReference editorReferences[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
                    for(int j = 0; j < editorReferences.length; j++){
                        try
                        {
                            org.eclipse.ui.IEditorInput tEditorInput = editorReferences[j].getEditorInput();
                            org.eclipse.ui.IEditorPart tEditor = editorReferences[j].getEditor(false);
                            if(tEditor == null || !(tEditor instanceof XPXOrderHistoryEditor))
                                continue;
                            Element orderEntryEditorInput = ((YRCEditorInput)tEditorInput).getXml();
                            if(YRCPlatformUI.isVoid(orderEntryEditorInput) || !YRCPlatformUI.equals(currRefOrderHdrKey, orderEntryEditorInput.getAttribute("RefOrderHdrKey")))
                                continue;
                            YRCPlatformUI.openEditor("com.xpedx.sterling.rcp.pca.orderhistory.editor.XPXOrderHistoryEditor", (YRCEditorInput)tEditorInput);
                            isRefOrderEditorFound = true;
                            break;
                        }
                        catch(Exception e)
                        {
                            YRCPlatformUI.trace("Exception in getOpenDraftOrderList : ", e);
                        }
                    }
                    if(!isRefOrderEditorFound){
                    	String[] arrayEditorComparisonAttrs = new String[] {
                                "RefOrderHdrKey"
                            };
                        YRCPlatformUI.openEditor(XPXOrderHistoryEditor.ID_EDITOR, new YRCEditorInput(orderElement, arrayEditorComparisonAttrs, ""));
                    }
                }
        	}
	}
	public void search() {
		Element elemModel = getTargetModel("SearchCriteria");
		
		// if 'elemModel' is null then create a dummy element with root element name as 'XPXRefOrderHdr'.
        if(YRCPlatformUI.isVoid(elemModel)){
            elemModel = YRCXmlUtils.createDocument("XPXRefOrderHdr").getDocumentElement();
        }

        // Set Order Date Query Type to DATERANGE in case attribute(FromOrderDate or ToOrderDate) values are not blank.
        if(!YRCPlatformUI.isVoid(elemModel.getAttribute("FromOrderDate")) 
        		|| !YRCPlatformUI.isVoid(elemModel.getAttribute("ToOrderDate"))){
        	elemModel.setAttribute("OrderDateQryType", "DATERANGE");
        }
        // Set e-trading ID Query Type to LIKE in case attribute(EtradingID) value is not blank.
        if(!YRCPlatformUI.isVoid(elemModel.getAttribute("EtradingID")))
        	elemModel.setAttribute("EtradingIDQryType", "LIKE");
        
        // Append Order By RefOrderHdrKey in Ascending order.
        Element orderByElem = YRCXmlUtils.getChildElement(elemModel, "OrderBy");
        if(YRCPlatformUI.isVoid(orderByElem))
            orderByElem = YRCXmlUtils.createChild(elemModel, "OrderBy");
        Element attrElem = YRCXmlUtils.getChildElement(orderByElem, "Attribute");
        if(YRCPlatformUI.isVoid(attrElem))
            attrElem = YRCXmlUtils.createChild(orderByElem, "Attribute");
        attrElem.setAttribute("Name", "RefOrderHdrKey");
        attrElem.setAttribute("Desc", "Y");
		
        // query for only reprocessible reference orders..
        elemModel.setAttribute("IsReprocessibleFlag", "Y");
        
        // Invoke Command: getXPXReferenceOrderListService 
        YRCApiContext context = new YRCApiContext();
	    context.setApiName("getXPXReferenceOrderListService");
	    context.setFormId(getFormId());
	    context.setInputXml(elemModel.getOwnerDocument());
	    callApi(context);
    }

	public void reset() {
		setModel("SearchCriteria", YRCXmlUtils.createDocument("XPXRefOrderHdr").getDocumentElement());
		setDefaultEnterpriseCode();
	}
	
	public void reprocessRefOrder(Element eleTableItem)
	{
		refOrderHdrKey = eleTableItem.getAttribute("RefOrderHdrKey");
		String api[] = {"XPXReprocessReferenceOrderService"};
		Document[] docInput = {
				YRCXmlUtils.createFromString("<XPXRefOrderHdr RefOrderHdrKey='" + refOrderHdrKey + "' />")
		};
	    callApis(api, docInput);		
		
	}
	
	public void markRefOrderComplete(Element eleTableItem)
	{
		refOrderHdrKey = eleTableItem.getAttribute("RefOrderHdrKey");
		String api[] = {"changeXPXRefOrderHdr"};
		Document[] docInput = {
				YRCXmlUtils.createFromString("<XPXRefOrderHdr RefOrderHdrKey='" + refOrderHdrKey + "'  IsMarkOdrCompleteFlag='Y'/>")
		};
	    callApis(api, docInput);	
		
	}

}
