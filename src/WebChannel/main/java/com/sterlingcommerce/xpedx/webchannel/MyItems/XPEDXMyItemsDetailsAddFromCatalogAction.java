package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCMashupAction;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsAddFromCatalogAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsDetailsAddFromCatalogAction.class);
	
	public static final String COMMAND_VALIDATE_ITEM 	= "validate_item";
	private Document outDoc;
	private String prodId = "";
	private String listKey = ""; 
	private String listName = "";
	private String listDesc	= "";
	
	private String name 	= "";
	private String desc 	= "";
	private String qty 		= "";
	private String order	= "";
	private String itemCount = "";
	private String jobId 	= "";
	private String itemId 	= "";
	private String itemType = "";
	private String command 	= "";
	
	
	
	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
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
		this.desc = desc;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	private void validateItem() {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public String execute() {
		try {
			
			if (getCommand().equals(COMMAND_VALIDATE_ITEM)){
				validateItem();
				return SUCCESS;
			}
			
			Map<String, Element> out;
			setName(getProdId());
			setDesc(getProdId());
			setItemId(getProdId());
			
			//update the order
			order = ( Integer.parseInt(itemCount) + 1) + "";

			out = prepareAndInvokeMashups();
			outDoc = (Document)out.values().iterator().next().getOwnerDocument();
			
			//Populate all the fields
			Element itemEle 		= getXMLUtils().getChildElement(outDoc.getDocumentElement(), "Item");
			Element primaryInfoEle 	= getXMLUtils().getChildElement(itemEle, "PrimaryInformation");
			setName(primaryInfoEle.getAttribute("DisplayItemDescription"));
			setDesc(primaryInfoEle.getAttribute("Description"));
			
		} catch (Exception e) {
			setItemType("99");
			LOG.error("No Item found '"+getProdId()+"'. Adding product not from the catalog.");
			return SUCCESS;
		}
		return SUCCESS;
	}

	public Document getOutDoc() {
		return outDoc;
	}

	public void setOutDoc(Document outDoc) {
		this.outDoc = outDoc;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
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

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getListDesc() {
		return listDesc;
	}

	public void setListDesc(String listDesc) {
		this.listDesc = listDesc;
	}

}
