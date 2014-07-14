/**
 * 
 */
package com.xpedx.sterling.rcp.pca.util;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.myitems.screen.XPXMyItemsSearchListScreenBehavior;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;

/**
 * @author Krithika S
 *
 */
public abstract class XPXPaginationBehavior extends YRCBehavior {
	
	public static final String NEXTPAGE_PAGINATION_STRATEGY = "NEXTPAGE";
	public static final String GENERIC_PAGINATION_STRATEGY = "GENERIC";
	
	protected XPXPaginationBehavior(Composite ownerComposite, String formId, Object inputObject) {
		super(ownerComposite, formId,inputObject);
		xpxPaginationData = new XPXPaginationData();
	}
	
	protected XPXPaginationBehavior(Composite ownerComposite, String formId) {
		super(ownerComposite, formId);
		xpxPaginationData = new XPXPaginationData();
	}
	
	//The pagination data object
	private XPXPaginationData xpxPaginationData;
	
	//The setter method to set the following variables have been made abstract to force implementation in subclass.
	protected String srcModelName;
	protected String rootListElemName;
	protected String repeatingElemName;

	/**
	 * This method prepares the I/P XML to getPage API depending on the attributes set in XPXPaginationData object
	 * @return
	 */
	private Document formInputXml()
	{
		Document docInput = YRCXmlUtils.createDocument("Page");
		Element pageElem = docInput.getDocumentElement();
		
		//Get the pagination strategy
		String paginationStrategy =  getXpxPaginationData().getPaginationStrategy();
		
		pageElem.setAttribute("PaginationStrategy", paginationStrategy);
		pageElem.setAttribute("Refresh", "Y");
		pageElem.setAttribute("PageSize", getXpxPaginationData().getPageSize());
		
		//If the pagination strategy is not NEXTPAGE, set the PageNumber attribute in the Page element
		if(!paginationStrategy.equals(NEXTPAGE_PAGINATION_STRATEGY))
			pageElem.setAttribute("PageNumber", getXpxPaginationData().getPageNumber());
		
		//Create the API Element and set the Input and OrderBy elements
		Element apiElem = YRCXmlUtils.createChild(pageElem, "API");
		apiElem.setAttribute("Name", getXpxPaginationData().getApiName());
		apiElem.setAttribute("IsFlow", getXpxPaginationData().getIsFlow());
		
		Element inputXmlElem = getXpxPaginationData().getInputXml().getDocumentElement();
		Element customerOrderelem = YRCXmlUtils.getChildElement(inputXmlElem, "customerOrder");
		if(customerOrderelem != null){
			String isCustomerOrderPageCheck = customerOrderelem.getAttribute("isCustomerOrderPage");	
			if("Y".equalsIgnoreCase(isCustomerOrderPageCheck)){
				createSearchOrderXML(apiElem);
				
			}
		}
		
		if(!YRCPlatformUI.isVoid(getXpxPaginationData().getSortColumn())){
			Element orderByElem = YRCXmlUtils.createChild(inputXmlElem, "OrderBy");
			Element attributeElem = YRCXmlUtils.createChild(orderByElem, "Attribute");
			attributeElem.setAttribute("Desc", getXpxPaginationData().getSortOrderDesc());
			attributeElem.setAttribute("Name", getXpxPaginationData().getSortColumn());
		}
		
		Element inputElem = YRCXmlUtils.createChild(apiElem, "Input");
		YRCXmlUtils.importElement(inputElem, inputXmlElem); 
		//Create the PreviousPage element and set the PageNumber attribute if the pagination strategy is NEXTPAGE
		//and set the previous page's last record as a child to the PreviousPage element
		if(!YRCPlatformUI.isVoid(getXpxPaginationData().getPreviousPageElem()) && paginationStrategy.equals(NEXTPAGE_PAGINATION_STRATEGY)){
			Element prevPageElem = YRCXmlUtils.createChild(pageElem, "PreviousPage");
			prevPageElem.setAttribute("PageNumber", getXpxPaginationData().getPageNumber());
			YRCXmlUtils.importElement(prevPageElem, getXpxPaginationData().getPreviousPageElem());
		}
		return docInput;
	}
	
	/**
	 * This method is used to handle the pagination O/P and set/merge the model data.
	 * @param eleOutput
	 */
	public void handlePaginationOutput(Element eleOutput)
	{
			if(eleOutput!=null){
				Element xpedxMILListElem = (Element) YRCXPathUtils.evaluate(eleOutput, "./Output/"+getRootListElemName(), XPathConstants.NODE);
				NodeList listMyItemsList = (NodeList) YRCXPathUtils.evaluate(eleOutput, "./Output/"+getRootListElemName()+"/"+getRepeatingElemName(), XPathConstants.NODESET);
				boolean prevPageExists = false;
				
				//Get the data from the previous page
				Element prevPageMilItemsListElem = null;
				
				//If the pagination strategy is NEXTPAGE, merge the current API O/P with earlier model data and reset the model.
				if(getXpxPaginationData().getPaginationStrategy().equals(NEXTPAGE_PAGINATION_STRATEGY))
						prevPageMilItemsListElem = getModel(getSrcModelName());
				
				if(prevPageMilItemsListElem!=null)
				{
					NodeList prevMILItemsList = prevPageMilItemsListElem.getElementsByTagName(getRepeatingElemName());
					if(prevMILItemsList!=null && prevMILItemsList.getLength()>0){
						prevPageExists = true;
					}
				}
				if(listMyItemsList!=null && listMyItemsList.getLength()>0)
				{
    				int currentListSize = listMyItemsList.getLength();
					for(int k=0;k<currentListSize;k++)
    				{
    					Element eleMyItemsList = (Element)listMyItemsList.item(k);
    					if(k == (currentListSize-1) && getXpxPaginationData().getPaginationStrategy().equals(NEXTPAGE_PAGINATION_STRATEGY))
    					{
    						//If it is the last record, set it in previous page document
    						getXpxPaginationData().setPreviousPageElem(eleMyItemsList);
    					}
    					if(prevPageExists){
    						//If previous page exists, append the current result to the earlier one.
    						YRCXmlUtils.importElement(prevPageMilItemsListElem, eleMyItemsList);
    					}
    				}
				}
				
				if(prevPageExists)
					setModel(prevPageMilItemsListElem);
				else
					setModel(xpedxMILListElem);
				
				repopulateModel(getRootListElemName());
				
    			//Fetch the page attributes and display the 'previous','next' or 'get next page' buttons based on that
    			String isFirstPage = eleOutput.getAttribute("IsFirstPage");
    			String isLastPage = eleOutput.getAttribute("IsLastPage");
    			String pageNo = eleOutput.getAttribute("PageNumber");
    			String totalNoOfPages = eleOutput.getAttribute("TotalNumberOfPages");
    			getXpxPaginationData().setTotalNoOfPages(totalNoOfPages);
    			if(!getXpxPaginationData().getPaginationStrategy().equals(NEXTPAGE_PAGINATION_STRATEGY)){
	    			if(!YRCPlatformUI.isVoid(isFirstPage) && isFirstPage.equals("N"))
	    			{
	    				//Show Previous button if the current page is not the first page 
	    				((XPXPaginationComposite)getOwnerForm()).getLnkPrevious().setVisible(true);
	    			}
	    			if(!YRCPlatformUI.isVoid(isLastPage) && isLastPage.equals("N"))
	    			{
	    				//Show Next button if the current page is not the last page 
	    				((XPXPaginationComposite)getOwnerForm()).getLnkNext().setVisible(true);
	    			}
    			}
    			else if(!YRCPlatformUI.isVoid(isLastPage) && isLastPage.equals("N"))
    			{
    				//Show Get Next Page button if the current page is not the last page 
    				((XPXPaginationComposite)getOwnerForm()).getLnkGetNextPageRecords().setVisible(true);
    			}
    			getXpxPaginationData().setPageNumber(pageNo);
			}
		
	}
	
	//This method is used to trigger the pagination call
	public void search(){
		//Clear the links visibility before the pagination/search is triggered
		Link[] paginationLinks = ((XPXPaginationComposite)getOwnerForm()).getPaginationLinkControls(getXpxPaginationData().getPaginationStrategy());
		if(paginationLinks!=null && paginationLinks.length>0)
		{
			for(Link currentLink: paginationLinks)
			{
				currentLink.setVisible(false);
			}
		}
		
		//Call getPage API
		if(this.xpxPaginationData!=null){
			YRCApiContext context = new YRCApiContext();
	        context.setApiName("getPage");
	        context.setFormId(this.getFormId());
	        context.setInputXml(formInputXml());
	        callApi(context);
		}
	}
	
	/**
	 * This method is used to get the first page of search, it is triggered by clicking on Search button.
	 */
	public void getFirstPage()
	{
		if(getXpxPaginationData().getPaginationStrategy().equals(NEXTPAGE_PAGINATION_STRATEGY))
		{
			getXpxPaginationData().setPreviousPageElem(null);
			setModel(getSrcModelName(), YRCXmlUtils.createDocument(getRootListElemName()).getDocumentElement());
		}
		else
		{
			String previousPageNumber = XPXMyItemsSearchListScreenBehavior.TempPageNumber;
			if (previousPageNumber != null){
				if("1".equalsIgnoreCase("previousPageNumber") || "-1".equalsIgnoreCase("previousPageNumber")){
					getXpxPaginationData().setPageNumber("1");	
				}
				else{
					getXpxPaginationData().setPageNumber(previousPageNumber);
				}
			}
			else{
				getXpxPaginationData().setPageNumber("1");
			}
		}
		search();
	}
	
	public XPXPaginationData getXpxPaginationData() {
		return xpxPaginationData;
	}

	public void setXpxPaginationData(XPXPaginationData xpxPaginationData) {
		this.xpxPaginationData = xpxPaginationData;
	}

	public String getSrcModelName() {
		return srcModelName;
	}

	public abstract void setSrcModelName(String srcModelName);

	public String getRootListElemName() {
		return rootListElemName;
	}

	public abstract void setRootListElemName(String rootListElemName);

	public String getRepeatingElemName() {
		return repeatingElemName;
	}

	public abstract void setRepeatingElemName(String repeatingElemName);
	
	public Element createSearchOrderXML(Element elem){
	//	Element elemModel = YRCXmlUtils.createDocument("Template").getDocumentElement();
		Element elemModel = YRCXmlUtils.createChild(elem, "Template");
		Element OrderListelem = YRCXmlUtils.createChild(elemModel, "OrderList");
		OrderListelem.setAttribute("TotalNumberOfRecords", "");
		OrderListelem.setAttribute("TotalOrderList", "");
		Element Orderelem = YRCXmlUtils.createChild(OrderListelem, "Order");
		Orderelem.setAttribute("ActualPricingDate", "");
		Orderelem.setAttribute("AdjustmentInvoicePending", "");
		Orderelem.setAttribute("AllAddressesVerified", "");
		Orderelem.setAttribute("ApprovalCycle", "");
		Orderelem.setAttribute("AuthorizationExpirationDate", "");
		Orderelem.setAttribute("AuthorizedClient", "");
		Orderelem.setAttribute("BillToID", "");
		Orderelem.setAttribute("BillToKey", "");
		Orderelem.setAttribute("BuyerOrganizationCode", "");
		Orderelem.setAttribute("CarrierAccountNo", "");
		Orderelem.setAttribute("CarrierServiceCode", "");
		Orderelem.setAttribute("ChainType", "");
		Orderelem.setAttribute("ChargeActualFreightFlag", "");
		Orderelem.setAttribute("ComplimentaryGiftBoxQty", "");
		Orderelem.setAttribute("CreatedAtNode", "");
		Orderelem.setAttribute("Createprogid", "");
		Orderelem.setAttribute("Createts", "");
		Orderelem.setAttribute("Createuserid", "");
		Orderelem.setAttribute("CustCustPONo", "");
		Orderelem.setAttribute("CustomerContactID", "");		
		Orderelem.setAttribute("CustomerFirstName", "");
		Orderelem.setAttribute("CustomerLastName", "");
		Orderelem.setAttribute("CustomerPONo", "");
		Orderelem.setAttribute("CustomerPhoneNo", "");
		Orderelem.setAttribute("CustomerZipCode", "");
		Orderelem.setAttribute("DeliveryCode", "");
		Orderelem.setAttribute("Division", "");
		Orderelem.setAttribute("DoNotConsolidate", "");
		Orderelem.setAttribute("DocumentType", "");
		Orderelem.setAttribute("DraftOrderFlag", "");
		Orderelem.setAttribute("EnterpriseCode", "");
		Orderelem.setAttribute("EntryType", "");
		Orderelem.setAttribute("FormatedLegacyOrderNo", "");
		Orderelem.setAttribute("FreightTerms", "");
		Orderelem.setAttribute("HasDerivedChild", "");
		Orderelem.setAttribute("HasDerivedParent", "");
		Orderelem.setAttribute("HoldFlag", "");
		Orderelem.setAttribute("HoldReasonCode", "");
		Orderelem.setAttribute("InternalApp", "");
		Orderelem.setAttribute("InvoiceComplete", "");
		Orderelem.setAttribute("Lockid", "");
		Orderelem.setAttribute("Modifyprogid", "");
		Orderelem.setAttribute("Modifyts", "");
		Orderelem.setAttribute("Modifyuserid", "");
		Orderelem.setAttribute("NextAlertTs", "");
		Orderelem.setAttribute("NoOfAuthStrikes", "");
		Orderelem.setAttribute("NotifyAfterShipmentFlag", "");
		Orderelem.setAttribute("OrderComplete", "");
		Orderelem.setAttribute("OrderDate", "");
		Orderelem.setAttribute("OrderHeaderKey", "");
		Orderelem.setAttribute("OrderName", "");
		Orderelem.setAttribute("OrderNo", "");
		Orderelem.setAttribute("OrderType", "");
		Orderelem.setAttribute("OriginalTax", "");
		Orderelem.setAttribute("OriginalTotalAmount", "");
		Orderelem.setAttribute("OtherCharges", "");
		Orderelem.setAttribute("Override", "");
		Orderelem.setAttribute("PaymentStatus", "");
		Orderelem.setAttribute("PendingTransferIn", "");
		Orderelem.setAttribute("PersonalizeCode", "");
		Orderelem.setAttribute("PriceOrder", "");
		Orderelem.setAttribute("PriceProgramName", "");
		Orderelem.setAttribute("PriorityCode", "");
		Orderelem.setAttribute("PriorityNumber", "");
		Orderelem.setAttribute("PropagateCancellations", "");
		Orderelem.setAttribute("Purpose", "");
		Orderelem.setAttribute("ReqDeliveryDate", "");
		Orderelem.setAttribute("ReserveInventoryFlag", "");
		Orderelem.setAttribute("ReturnByGiftRecipient", "");
		Orderelem.setAttribute("SCAC", "");
		Orderelem.setAttribute("SaleVoided", "");
		Orderelem.setAttribute("SearchCriteria1", "");
		Orderelem.setAttribute("SearchCriteria2", "");
		Orderelem.setAttribute("SellerOrganizationCode", "");
		Orderelem.setAttribute("ShipNode", "");
		Orderelem.setAttribute("ShipToID", "");
		Orderelem.setAttribute("ShipToKey", "");
		Orderelem.setAttribute("SourceIPAddress", "");
		Orderelem.setAttribute("SourceType", "");
		Orderelem.setAttribute("Status", "");
		Orderelem.setAttribute("TaxExemptFlag", "");
		Orderelem.setAttribute("TaxExemptionCertificate", "");
		Orderelem.setAttribute("TaxJurisdiction", "");
		Orderelem.setAttribute("TaxPayerId", "");
		Orderelem.setAttribute("TermsCode", "");
		Orderelem.setAttribute("TotalAdjustmentAmount", "");
		Orderelem.setAttribute("isHistory", "");
		Element Extnelem = YRCXmlUtils.createChild(Orderelem, "Extn");
		
		Extnelem.setAttribute("ExtnAddnlEmailAddr", "");
		Extnelem.setAttribute("ExtnAttentionName", "");
		Extnelem.setAttribute("ExtnBillToCustomerID", "");
		Extnelem.setAttribute("ExtnBillToName", "");
		Extnelem.setAttribute("ExtnBillToSuffix", "");
		Extnelem.setAttribute("ExtnCompanyId", "");
		Extnelem.setAttribute("ExtnCurrencyCode", "");
		Extnelem.setAttribute("ExtnCustomerDivision", "");
		Extnelem.setAttribute("ExtnCustomerNo", "");
		Extnelem.setAttribute("ExtnDeliveryHoldFlag", "");
		Extnelem.setAttribute("ExtnDeliveryHoldTime", "");
		Extnelem.setAttribute("ExtnETradingID", "");
		Extnelem.setAttribute("ExtnEnvtId", "");
		Extnelem.setAttribute("ExtnGenerationNo", "");
		Extnelem.setAttribute("ExtnHeaderStatusCode", "");
		Extnelem.setAttribute("ExtnInvoiceNo", "");
		Extnelem.setAttribute("ExtnInvoiceTotal", "");
		Extnelem.setAttribute("ExtnIsProcessedFlag", "");
		Extnelem.setAttribute("ExtnLegTotOrderAdjustments", "");
		Extnelem.setAttribute("ExtnLegacyOrderNo", "");
		Extnelem.setAttribute("ExtnLegacyOrderType", "");
		Extnelem.setAttribute("ExtnMsgHeaderId", "");
		Extnelem.setAttribute("ExtnOrderCouponDiscount", "");
		Extnelem.setAttribute("ExtnOrderDesc", "");
		Extnelem.setAttribute("ExtnOrderDiscount", "");
		Extnelem.setAttribute("ExtnOrderDivision", "");
		Extnelem.setAttribute("ExtnOrderLockFlag", "");
		Extnelem.setAttribute("ExtnOrderSpecialCharges", "");
		Extnelem.setAttribute("ExtnOrderStatus", "");
		Extnelem.setAttribute("ExtnOrderSubTotal", "");
		Extnelem.setAttribute("ExtnOrderTax", "");
		Extnelem.setAttribute("ExtnOrderedByName", "");
		Extnelem.setAttribute("ExtnOrigEnvironmentCode", "");
		Extnelem.setAttribute("ExtnRushOrderComments", "");
		Extnelem.setAttribute("ExtnRushOrderFlag", "");
		Extnelem.setAttribute("ExtnSAPParentName", "");
		Extnelem.setAttribute("ExtnShipComplete", "");
		Extnelem.setAttribute("ExtnShipToName", "");
		Extnelem.setAttribute("ExtnShipToSuffix", "");
		Extnelem.setAttribute("ExtnSourceType", "");
		Extnelem.setAttribute("ExtnSystemIdentifier", "");
		Extnelem.setAttribute("ExtnTotOrdValWithoutTaxes", "");
		Extnelem.setAttribute("ExtnTotOrderAdjustments", "");
		Extnelem.setAttribute("ExtnTotalOrderFreight", "");
		Extnelem.setAttribute("ExtnTotalOrderValue", "");
		Extnelem.setAttribute("ExtnTotalShipValue", "");
		Extnelem.setAttribute("ExtnWebConfNum", "");
		Extnelem.setAttribute("ExtnWebHoldFlag", "");
		Extnelem.setAttribute("ExtnWebHoldReason", "");
		Extnelem.setAttribute("ExtnWillCall", "");
		Extnelem.setAttribute("ExtnOUFailureLockFlag", "");
		Element PriceInfoelem = YRCXmlUtils.createChild(Orderelem, "PriceInfo");
		PriceInfoelem.setAttribute("Currency", "");
		PriceInfoelem.setAttribute("EnterpriseCurrency", "");
		PriceInfoelem.setAttribute("TotalAmount", "");
		Element OrderHoldTypeselem = YRCXmlUtils.createChild(Orderelem, "OrderHoldTypes");
		Element OrderHoldTypeelem = YRCXmlUtils.createChild(OrderHoldTypeselem, "OrderHoldType");
		OrderHoldTypeelem.setAttribute("HoldType", "");
		OrderHoldTypeelem.setAttribute("ResolverUserId", "");
		OrderHoldTypeelem.setAttribute("Status", "");
		
		return elemModel;
	}
}
