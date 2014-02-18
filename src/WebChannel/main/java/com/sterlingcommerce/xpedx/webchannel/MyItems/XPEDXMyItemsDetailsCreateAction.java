package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCMashupAction;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsCreateAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsDetailsCreateAction.class);
	private Document outDoc;
	
	private String listKey		= ""; //For the listing page
	private String listName		= ""; //For the listing page
	private String listDesc	= "";
	
	private String name 		= "";
	private String desc 		= "";
	private String qty			= "";
	private String mfgItemNo	= "";
	
	private String order		= "-1";
	private String itemCount	= "-1";
	private String jobId		= "";
	private String createdBy 	= "";
	private String lastModified = "";
	private String itemId		= "";
	private String itemType		= "";
	private String uomId		= "";
	private String errorMsg		= "";
	private String editMode		= "";
	
	

	@Override
	public String execute() {
		try {
			Map<String, Element> out;
			
			//Enforce the item count at the api level
			if (Integer.parseInt(getItemCount()) >= XPEDXMyItemsDetailsAction.getItemMax()){
				throw new Exception("You have reached the maximu number of items which is " + XPEDXMyItemsDetailsAction.getItemMax() + ".");
			}
			
			out = prepareAndInvokeMashups();
			outDoc = (Document)out.values().iterator().next().getOwnerDocument();
			
			//Update the item count
			try {
				if (Integer.parseInt(order) > Integer.parseInt(itemCount)){
					itemCount = order;
				}
			} catch (Exception e) {
				LOG.error(e.getStackTrace());
			}
		
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			setErrorMsg("ITEM_ADD_ERROR");
			return ERROR;
		}
		return SUCCESS;
	}

	public Document getOutDoc() {
		return outDoc;
	}

	public void setOutDoc(Document outDoc) {
		this.outDoc = outDoc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		String desc2 = desc;
		
		/*
		try {
			desc2 = StringUtils.replace(desc2, "taglt", "<");
			desc2 = StringUtils.replace(desc2, "taggt", ">");
			desc2 = StringUtils.replace(desc2, "tagfs", "/");
			desc2 = StringUtils.replace(desc2, "<script", "");
			desc2 = StringUtils.replace(desc2, "<embed", "");
			desc2 = StringUtils.replace(desc2, "<object", "");
			
		} catch (Exception e) {
			desc2 = desc;
		}
		*/
		
		this.desc = desc2;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getItemId() {
		String res = itemId;
		try {
			if (res == null){
				res = "INVALID_ITEM";
			} else {
				if (res.trim().length() == 0){
					res = "INVALID_ITEM";
				}
			}
		} catch (Exception e) {
			res = "ERROR_INVALID_ITEM";
		}
		return res;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getMfgItemNo() {
		return mfgItemNo;
	}

	public void setMfgItemNo(String mfgItemNo) {
		this.mfgItemNo = mfgItemNo;
	}

	public String getUomId() {
		return uomId;
	}

	public void setUomId(String uomId) {
		this.uomId = uomId;
	}

	public String getListDesc() {
		return listDesc;
	}

	public void setListDesc(String listDesc) {
		this.listDesc = listDesc;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public String getEditMode() {
		return editMode;
	}

	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}
	
}
