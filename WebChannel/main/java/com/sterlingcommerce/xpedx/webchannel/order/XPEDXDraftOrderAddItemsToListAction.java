package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.sterlingcommerce.webchannel.common.Breadcrumb;
import com.sterlingcommerce.webchannel.common.BreadcrumbHelper;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCDate;

public class XPEDXDraftOrderAddItemsToListAction extends WCMashupAction
{
	protected String listKey;
	protected List selectedLineItem;
	protected List orderLineKeys;
	protected List orderLineItemIDs;
	protected List orderLineItemNames;
	protected List orderLineItemDesc;
	protected List orderLineQuantities;
	protected List orderLineItemOrders;
	protected List orderLineCustLineAccNo;
	protected List itemUOMs;
	protected List customerLinePONo;
	private String draft = "";
	private String url = "";
	private String itemId = "";
    protected String _bcs_ = null;
    protected List<Breadcrumb> _bcl_ = null;
	
	private String unitOfMeasure = "";
	private boolean sendToItemDetails = false;
	private String modifiedDate;
	
	private static final Logger LOG = Logger.getLogger(XPEDXDraftOrderAddItemsToListAction.class);

	@SuppressWarnings({ "unused", "unchecked", "deprecation" })
	public String execute() {
		try 
		{
			YFCDate modifiedYFCDate = new YFCDate();
			modifiedDate = modifiedYFCDate.getString();
			
			Document outputDoc = null;
			List<String> itemIds = new ArrayList<String>(); 
			List<String> itemNames = new ArrayList<String>();
			List<String> itemDescs = new ArrayList<String>();
			List itemQtys = new ArrayList();
			List itemUOMs = new ArrayList();
			List itemJobIds = new ArrayList();
			List itemTypes = new ArrayList();
			List itemOrders = new ArrayList();
			List itemPONumber = new ArrayList();
			String tempItemOrder = "";
			for(int i=0;i<selectedLineItem.size();i++)
			{
				if(orderLineKeys.contains(selectedLineItem.get(i)))
				{
					int index = orderLineKeys.indexOf(selectedLineItem.get(i));
					itemIds.add(orderLineItemIDs.get(index).toString());
					itemNames.add(orderLineItemNames.get(index).toString());
					itemDescs.add(orderLineItemDesc.get(index).toString());
					itemQtys.add(orderLineQuantities.get(index).toString());
					itemUOMs.add(this.itemUOMs.get(index).toString());
					if(this.customerLinePONo !=null)
					{
						if(this.customerLinePONo.get(index) != null)
						{
							itemPONumber.add(this.customerLinePONo.get(index).toString());
						}
					}
					if(orderLineCustLineAccNo!=null && index < orderLineCustLineAccNo.size()){
						itemJobIds.add(orderLineCustLineAccNo.get(index).toString());
					} else {
						itemJobIds.add(" ");
					}
					//hardcode for xpedx #
					
					//Set the item type tmp to XPEDX item 
					itemTypes.add("1");
					if(i==0){
						tempItemOrder = orderLineItemOrders.get(0).toString();
						itemOrders.add(tempItemOrder);
					}
					else{
						tempItemOrder = (Integer.parseInt(tempItemOrder)+1)+"";
						itemOrders.add(tempItemOrder);
					}
				}
				
			}
			outputDoc = XPEDXWCUtils.AddItemsToList(listKey,itemIds,itemNames,itemDescs,itemQtys,itemUOMs,itemJobIds,itemTypes,itemOrders,itemPONumber,modifiedDate);
			
			
		}
		catch (Exception ex) 
		{
			
		}
		
		//Send the user back if using the send back to item details page
		if (sendToItemDetails){
			//Remove the breadcrumb
			try {
				List<Breadcrumb> bcl = BreadcrumbHelper.preprocessBreadcrumb(this.get_bcs_());
				bcl.remove(0);
				set_bcs_(BreadcrumbHelper.serializeBreadcrumb(bcl)); 
			} catch (Exception e) {
			}
			//url = request.getHeader("referer");
			return "success2ItemDetails";
		}

		return SUCCESS;
	}
	
	
	
	public List getSelectedLineItem() {
		return selectedLineItem;
	}

	public void setSelectedLineItem(List selectedLineItem) {
		this.selectedLineItem = selectedLineItem;
	}

	public List getOrderLineKeys() {
		return orderLineKeys;
	}

	public void setOrderLineKeys(List orderLineKeys) {
		this.orderLineKeys = orderLineKeys;
	}

	public List getOrderLineItemIDs() {
		return orderLineItemIDs;
	}

	public void setOrderLineItemIDs(List orderLineItemIDs) {
		this.orderLineItemIDs = orderLineItemIDs;
	}

	public List getOrderLineQuantities() {
		return orderLineQuantities;
	}

	public List getOrderLineItemOrders() {
		return orderLineItemOrders;
	}

	public void setOrderLineItemOrders(List orderLineItemOrders) {
		this.orderLineItemOrders = orderLineItemOrders;
	}

	public void setOrderLineQuantities(List orderLineQuantities) {
		this.orderLineQuantities = orderLineQuantities;
	}

	public List getItemUOMs() {
		return itemUOMs;
	}

	public void setItemUOMs(List itemUOMs) {
		this.itemUOMs = itemUOMs;
	}

	public List getOrderLineItemNames() {
		return orderLineItemNames;
	}

	public void setOrderLineItemNames(List orderLineItemNames) {
		this.orderLineItemNames = orderLineItemNames;
	}

	public List getOrderLineItemDesc() {
		return orderLineItemDesc;
	}

	public void setOrderLineItemDesc(List orderLineItemDesc) {
		this.orderLineItemDesc = orderLineItemDesc;
	}

	
	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}
	
	public List getOrderLineCustLineAccNo() {
		return orderLineCustLineAccNo;
	}

	public void setOrderLineCustLineAccNo(List orderLineCustLineAccNo) {
		this.orderLineCustLineAccNo = orderLineCustLineAccNo;
	}


	public String getDraft() {
		return draft;
	}



	public void setDraft(String draft) {
		this.draft = draft;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public boolean getSendToItemDetails() {
		return sendToItemDetails;
	}



	public void setSendToItemDetails(boolean sendToItemDetails) {
		this.sendToItemDetails = sendToItemDetails;
	}



	public String getItemID() {
		return itemId;
	}



	public void setItemID(String itemID) {
		this.itemId = itemID;
	}


	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}



	public String get_bcs_() {
		return _bcs_;
	}



	public void set_bcs_(String _bcs_) {
		this._bcs_ = _bcs_;
	}



	public List<Breadcrumb> get_bcl_() {
		return _bcl_;
	}



	public void set_bcl_(List<Breadcrumb> _bcl_) {
		this._bcl_ = _bcl_;
	}



	public List getCustomerLinePONo() {
		return customerLinePONo;
	}



	public void setCustomerLinePONo(List customerLinePONo) {
		this.customerLinePONo = customerLinePONo;
	}
	
	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}