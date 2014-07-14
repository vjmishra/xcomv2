package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCMashupAction;

@SuppressWarnings("serial")
public class XPEDXMyItemsListDeleteAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsListDeleteAction.class);
	
	private String listKey				= "";
	private String filterBySelectedListChk 		= "";
	private String filterByMyListChk 	= "";
	private String[] customerIds;
	private String[] customerPaths;
	private boolean deleteClicked;
	
	@Override
	@SuppressWarnings("unused")
	public String execute() {
		try {
			
			
			Map<String, Element> out;
			out = prepareAndInvokeMashups();
			if(customerIds!=null && customerIds.length >0){
				generateCustomerIdArray();
			}
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			return ERROR;
		}
		return SUCCESS;
	}

	private void generateCustomerIdArray() {
		String customerId;
		if(getCustomerIds()!=null && getCustomerIds().length>0)
		for(int i=0; i< getCustomerIds().length; ){// there is only on value with comma separated customerIds
			customerId = getCustomerIds()[i];
			setCustomerIds(customerId.split(","));
			break;
		}
		//setDeleteClicked(true);
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}

	public String getFilterByMyListChk() {
		return filterByMyListChk;
	}

	public void setFilterByMyListChk(String filterByMyListChk) {
		this.filterByMyListChk = filterByMyListChk;
	}

	/**
	 * @param filterBySelectedListChk the filterBySelectedListChk to set
	 */
	public void setFilterBySelectedListChk(String filterBySelectedListChk) {
		this.filterBySelectedListChk = filterBySelectedListChk;
	}

	/**
	 * @return the filterBySelectedListChk
	 */
	public String getFilterBySelectedListChk() {
		return filterBySelectedListChk;
	}

	/**
	 * @param customerIds the customerIds to set
	 */
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	/**
	 * @return the customerIds
	 */
	public String[] getCustomerIds() {
		return customerIds;
	}

	/**
	 * @param deleteClicked the deleteClicked to set
	 */
	public void setDeleteClicked(boolean deleteClicked) {
		this.deleteClicked = deleteClicked;
	}

	/**
	 * @return the deleteClicked
	 */
	public boolean getDeleteClicked() {
		return deleteClicked;
	}

	/**
	 * @param customerPaths the customerPaths to set
	 */
	public void setCustomerPaths(String[] customerPaths) {
		this.customerPaths = customerPaths;
	}

	/**
	 * @return the customerPaths
	 */
	public String[] getCustomerPaths() {
		return customerPaths;
	}
}
