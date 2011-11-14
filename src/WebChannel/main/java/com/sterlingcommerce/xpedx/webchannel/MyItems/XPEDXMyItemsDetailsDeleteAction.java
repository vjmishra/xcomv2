package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCMashupAction;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsDeleteAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsDetailsDeleteAction.class);
	private String listKey		= "";
	private String listName		= "";
	private String listDesc	= "";
	private String[] itemKeys;
	private String[] itemIdFNs;
	private String key = "";
	private boolean editMode = false;
	
	public String[] getItemKeys() {
		return itemKeys;
	}

	public void setItemKeys(String[] itemIds) {
		this.itemKeys = itemIds;
		
		try {
			itemIdFNs = new String[itemIds.length];
			for (int i = 0; i < itemIds.length; i++) {
				itemIdFNs[i] = "MyItemsKey";
			}
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}

	public String getKey(){
		
		String res = "";
		try {
			/*
			if (request.getParameter("key") !=null){
				res = request.getParameter("key");
			}
			*/
			res = this.key;
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
		
		return res;
	}

	@Override
	@SuppressWarnings("unused")
	public String execute() {
		try {
			Map<String, Element> out;
			for (int i = 0; i < itemKeys.length; i++) {
				setKey(itemKeys[i]);
				out = prepareAndInvokeMashups();
			}
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			return ERROR;
		}
		return SUCCESS;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String[] getItemIdFNs() {
		return itemIdFNs;
	}

	public void setItemIdFNs(String[] itemIdFNs) {
		this.itemIdFNs = itemIdFNs;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getListDesc() {
		return listDesc;
	}

	public void setListDesc(String listDesc) {
		this.listDesc = listDesc;
	}

	public boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

}
