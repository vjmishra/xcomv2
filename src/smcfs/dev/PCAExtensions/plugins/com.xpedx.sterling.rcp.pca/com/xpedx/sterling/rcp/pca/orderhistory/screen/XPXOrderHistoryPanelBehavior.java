/*
 * Created on April 17,2012
 *
 */
package com.xpedx.sterling.rcp.pca.orderhistory.screen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.xml.xpath.XPathConstants;

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
//import com.yantra.pca.ycd.rcp.exposed.YCDExtensionUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXOrderHistoryPanelBehavior extends YRCBehavior {

	
	private XPXOrderHistoryPanel page ;
	private String defaultOrgCode ;
	private String refOrderHdrKey;
	public String masterCustomer;
	public static final HashMap statusList = new HashMap<String, String>();
	public XPXOrderHistoryPanelBehavior(Composite ownerComposite, String formId, Object inputObject) {
        super(ownerComposite, formId,inputObject);
        this.page = (XPXOrderHistoryPanel)getOwnerForm();
        this.defaultOrgCode = "";
//        init();
        getStatusList();
        masterCustomer = XPXUtils.masterCustomerID;
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
		attrElemComplex2.setAttribute("OrderTypeValue", "0002");
		attrElemComplex2.setAttribute("OrderTypeDescription", "Fulfillment Orders");

		Element attrElemComplex3 = YRCXmlUtils.createChild(elemModel, "OrdType");
		attrElemComplex3.setAttribute("OrderTypeValue", "0001");
		attrElemComplex3.setAttribute("OrderTypeDescription", "Customer Orders");
							
		setModel("OrderType",elemModel);
	}
	
	public void createOrderStatus(){
		
		Element elemModel = YRCXmlUtils.createDocument("OrderStatus").getDocumentElement();
		
		Element attrElemComplex1 = YRCXmlUtils.createChild(elemModel, "OrdStatus");
		attrElemComplex1.setAttribute("OrderStatusValue", "1100.5250");
		attrElemComplex1.setAttribute("OrderStatusDescription", "Open Orders");
		
		Element attrElemComplex2 = YRCXmlUtils.createChild(elemModel, "OrdStatus");
		attrElemComplex2.setAttribute("OrderStatusValue", "1100.5100");
		attrElemComplex2.setAttribute("OrderStatusDescription", "Backordered");

		Element attrElemComplex3 = YRCXmlUtils.createChild(elemModel, "OrdStatus");
		attrElemComplex3.setAttribute("OrderStatusValue", "1100.5550");
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
        	//Calling Child Customer List Servie
	        else if(YRCPlatformUI.equals(ctx.getApiName(), "getOrderList"))
	        {
	        	Element orderListXml=ctx.getOutputXml().getDocumentElement();
	        	//Adding Status Column
	        	NodeList listOrderList = orderListXml.getElementsByTagName("Order");
	        	for (int k = 0; k < listOrderList.getLength(); k++) {
					Element eleOrderList = (Element) listOrderList.item(k);
					Element extn = YRCXmlUtils.getChildElement(eleOrderList, "Extn");
					String extnOrderStatus = extn.getAttribute("ExtnOrderStatus");
					
					
						eleOrderList.setAttribute("Status", (String) statusList.get(extnOrderStatus));
						
					
					
				}
	        	/*********************************************/
	        	setModel("OrderListModel",orderListXml);
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
                	String currRefOrderHdrKey = orderElement.getAttribute("OrderHeaderKey");
                	String OrderNo=orderElement.getAttribute("OrderNo");

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
                            if(YRCPlatformUI.isVoid(orderEntryEditorInput) || !YRCPlatformUI.equals(currRefOrderHdrKey, orderEntryEditorInput.getAttribute("OrderHeaderKey")))
                                continue;
                            YRCPlatformUI.openEditor("com.yantra.pca.ycd.rcp.editors.YCDOrderEditor", (YRCEditorInput)tEditorInput);
                            isRefOrderEditorFound = true;
                            break;
                        }
                        catch(Exception e)
                        {
                            YRCPlatformUI.trace("Exception in getOpenDraftOrderList : ", e);
                        }
                    }
                    if(!isRefOrderEditorFound){
                    	//YCDExtensionUtils.launchTaskInEditor("YCD_TASK_VIEW_ORDER_SUMMARY", orderElement);

                        
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
		setFieldValue("txtCompany", "");
		setFieldValue("txtAccount", "");
		setFieldValue("txtSuffix", "");
		setFieldValue("txtShipFrom", "");
		setFieldValue("txtSearchBy", "");
		setFieldValue("cmbSearchBy", "");
		setFieldValue("cmbOrderStatus", "");
		setFieldValue("cmbOrderType", "");
		setFieldValue("txtFromDate", "");
		setFieldValue("txtToDate", "");
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
	//Method created for fetcching child customers
	public void getOrderList()
	{
		YRCApiContext apiCtx = new YRCApiContext();
		Element elemModel = YRCXmlUtils.createDocument("Order").getDocumentElement();
		elemModel.setAttribute("BillToID", masterCustomer);
		elemModel.setAttribute("ReadFromHistory","N");
		boolean isExtnChildCreated = false;
		Element attrElemComplex2 =null;
		//Condition For Item Number
		if((getFieldValue("cmbSearchBy")!= null) && (getFieldValue("cmbSearchBy").equalsIgnoreCase("ItemNum"))){
			Element attrElemComplex3 = YRCXmlUtils.createChild(elemModel, "OrderLines");
			Element attrElemComplex4 = YRCXmlUtils.createChild(attrElemComplex3, "OrderLine");
			Element attrElemComplex5 = YRCXmlUtils.createChild(attrElemComplex4, "Item");
			attrElemComplex5.setAttribute("ItemID", getFieldValue("txtSearchBy"));
		}
					//Condition For Order Search By
		if((getFieldValue("cmbSearchBy")!= null) && (getFieldValue("cmbSearchBy").equalsIgnoreCase("WebConf")|| getFieldValue("cmbSearchBy").equalsIgnoreCase("OrdNum"))){
			attrElemComplex2 = YRCXmlUtils.createChild(elemModel, "Extn");
			isExtnChildCreated = true;
			/*attrElemComplex2.setAttribute("MasterCustomer", "CD-0000163615-M-XPED-CC");
			attrElemComplex2.setAttribute("Division", "12_M");
			attrElemComplex2.setAttribute("ShipToID", "");*/
			if(getFieldValue("cmbSearchBy").equalsIgnoreCase("WebConf") && ((getFieldValue("txtSearchBy")) !=null || (getFieldValue("txtSearchBy")) != "")){
				attrElemComplex2.setAttribute("ExtnWebConfNum", getFieldValue("txtSearchBy"));
			}
			else if(getFieldValue("cmbSearchBy").equalsIgnoreCase("OrdNum") && ((getFieldValue("txtSearchBy")) !=null || (getFieldValue("txtSearchBy")) != "")){
				attrElemComplex2.setAttribute("ExtnLegacyOrderNo", getFieldValue("txtSearchBy"));
			}
			
		}
		if(getFieldValue("txtSuffix")!= null || getFieldValue("txtCompany")!= null || getFieldValue("txtShipFrom")!= null || getFieldValue("txtAccount")!= null){
			if(isExtnChildCreated){
				attrElemComplex2.setAttribute("ExtnShipToSuffix", getFieldValue("txtSuffix"));
				attrElemComplex2.setAttribute("ExtnSAPParentName", getFieldValue("txtCompany"));
				attrElemComplex2.setAttribute("ExtnCustomerDivision", getFieldValue("txtShipFrom"));
				attrElemComplex2.setAttribute("ExtnCustomerNo", getFieldValue("txtAccount"));
			}
			else{
				attrElemComplex2 = YRCXmlUtils.createChild(elemModel, "Extn");
				attrElemComplex2.setAttribute("ExtnShipToSuffix", getFieldValue("txtSuffix"));
				attrElemComplex2.setAttribute("ExtnSAPParentName", getFieldValue("txtCompany"));
				attrElemComplex2.setAttribute("ExtnCustomerDivision", getFieldValue("txtShipFrom"));
				attrElemComplex2.setAttribute("ExtnCustomerNo", getFieldValue("txtAccount"));
			}
			isExtnChildCreated = false;
			/*attrElemComplex2.setAttribute("MasterCustomer", "CD-0000163615-M-XPED-CC");
			attrElemComplex2.setAttribute("Division", "12_M");
			attrElemComplex2.setAttribute("ShipToID", "");*/
			
			
		}
		/*else if((getFieldValue("cmbSearchBy")!= null) && (getFieldValue("cmbSearchBy").equalsIgnoreCase("Item Number"))){
			
		}*/
		//Condition For OrderType
		if((getFieldValue("cmbOrderType")!= null) && (getFieldValue("cmbOrderType").equalsIgnoreCase("0001"))){
			elemModel.setAttribute("OrderType", "Customer");
		}
		 if((getFieldValue("cmbOrderType")!= null) && (getFieldValue("cmbOrderType").equalsIgnoreCase("0002"))){
			elemModel.setAttribute("OrderType","STOCK_ORDER");
		}

		//Condition For Order Status
		 if((getFieldValue("cmbOrderStatus")!= null) && (getFieldValue("cmbOrderStatus").equalsIgnoreCase("1100.5250"))){
			elemModel.setAttribute("Status", "1100.5250");
		}
		if((getFieldValue("cmbOrderStatus")!= null) && (getFieldValue("cmbOrderStatus").equalsIgnoreCase("1100.5100"))){
			elemModel.setAttribute("Status", "1100.5100");
		}
		 if((getFieldValue("cmbOrderStatus")!= null) && (getFieldValue("cmbOrderStatus").equalsIgnoreCase("1100.5550"))){
			elemModel.setAttribute("Status", "1100.5550");
		}

		//Condition For DateRange
		
		 if(getFieldValue("txtFromDate")!= null && getFieldValue("txtFromDate")!= ""){
			 String datefrom=getFieldValue("txtFromDate");
			    SimpleDateFormat sdfSource = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
			    Date date=new Date();
			   try {
					date = sdfSource.parse(datefrom);
					} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			    String fromdateReturn=sdfDestination.format(date);
			    elemModel.setAttribute("FromOrderDate", fromdateReturn);
			elemModel.setAttribute("OrderDateQryType","DATERANGE");
		}
		if(getFieldValue("txtToDate")!= null && getFieldValue("txtToDate")!= ""){
			String dateTo=getFieldValue("txtToDate");
		    SimpleDateFormat sdfSource = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		    Date date=new Date();
		   try {
				date = sdfSource.parse(dateTo);
				} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		    String todateReturn=sdfDestination.format(date);
			elemModel.setAttribute("ToOrderDate", todateReturn);
		}
		//Condition for Company
		

			apiCtx.setApiName("getOrderList");
			apiCtx.setInputXml(elemModel.getOwnerDocument());
		
		apiCtx.setFormId(getFormId());
		callApi(apiCtx);
		
	}
	
	private HashMap getStatusList(){
		statusList.put("1100.0100", "Submitted");
		statusList.put("1100.5100", "Backorder");
		statusList.put("1100.5250", "Open");
		statusList.put("1100.5300", "Direct from Manufacturer");
		statusList.put("1100.5350", "Customer Hold");
		statusList.put("1100.5400", "System Hold");
		statusList.put("1100.5450", "Web Hold");
		statusList.put("1100.5500", "Released for Fullfillment");
		statusList.put("1100.5550", "Shipped");
		statusList.put("1100.5700", "Invoiced");
		statusList.put("1100.5750", "Return");
		statusList.put("1100.5900", "Quote");
		statusList.put("1100.5950", "Invoice Only");
		statusList.put("9000", "Cancelled");
		return statusList;
		
		
	}

}
