package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.yantra.yfc.util.YFCDate;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsDeleteAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsDetailsDeleteAction.class);
	private String listKey		= "";
	private String listName		= "";
	private String listDesc	= "";
	private String[] itemKeys;
	private String[] itemIdFNs;
	private String key = "";
	private String modifiedDate;
	private boolean editMode = false;
	private String modifyUserName;
	
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
			YFCDate modifiedYFCDate = new YFCDate();
			modifiedDate = modifiedYFCDate.getString();
			String isSalesRep = (String) getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP");
			if(isSalesRep!=null && isSalesRep.equalsIgnoreCase("true")){
				String salesreploggedInUserName = (String)getWCContext().getSCUIContext().getSession().getAttribute("loggedInUserName");
				modifyUserName = salesreploggedInUserName;
			} else {
				modifyUserName	 = getWCContext().getLoggedInUserName();
			}
			
			 Element out;
			 Element updateListEle;
			for (int i = 0; i < itemKeys.length; i++) {
				setKey(itemKeys[i]);
				out = prepareAndInvokeMashup("XPEDXMyItemsDetailsDelete");
				updateListEle = prepareAndInvokeMashup("XPEDXMyItemsListChange");
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

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifyUserName() {
		return modifyUserName;
	}

	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}

}
