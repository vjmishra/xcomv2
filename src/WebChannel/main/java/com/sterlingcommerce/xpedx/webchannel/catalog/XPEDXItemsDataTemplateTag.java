/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;
import org.w3c.dom.Element;

import com.opensymphony.xwork2.util.ValueStack;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;

/**
 * @author vgovindan
 * This class takes care of building JS objects with the items data for the templates in Catalog search results pages.
 * 
 */
public class XPEDXItemsDataTemplateTag extends ComponentTagSupport {

	/* (non-Javadoc)
	 * @see org.apache.struts2.views.jsp.ComponentTagSupport#getBean(com.opensymphony.xwork2.util.ValueStack, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Component getBean(ValueStack vstack, HttpServletRequest request, HttpServletResponse response) {
		this.wcContext = (IWCContext) request.getAttribute("Tag_WCContext");
		this.ctx = wcContext.getSCUIContext();
		orderMultipleString = (String)request.getAttribute("Tag_orderMultipleString");
		qtyString = (String)request.getAttribute("Tag_qtyString");
		
		itemToItemBranchBeanMap = (HashMap<String, XPEDXItemBranchInfoBean>) wcContext.getWCAttribute("itemToItemBranchBeanMap", WCAttributeScope.SESSION);
		itemToItemBranchBeanMap = itemToItemBranchBeanMap == null ? new HashMap<String, XPEDXItemBranchInfoBean>() : itemToItemBranchBeanMap;
		itemMap =  (HashMap<String, HashMap<String, String>>) wcContext.getWCAttribute("itemMap", WCAttributeScope.REQUEST);
		itemMap = itemMap == null ? new HashMap<String, HashMap<String, String>>() : itemMap;		
		orderMultipleMap =  (HashMap<String, String>) wcContext.getWCAttribute("orderMultipleMap", WCAttributeScope.REQUEST);
		orderMultipleMap = orderMultipleMap == null ? new HashMap<String, String>() : orderMultipleMap;
		defaultShowUOMMap =  (HashMap<String, String>) wcContext.getWCAttribute("defaultShowUOMMap", WCAttributeScope.REQUEST);
		defaultShowUOMMap = defaultShowUOMMap == null ? new HashMap<String, String>() : defaultShowUOMMap;
		//Added for EB-225 - Itemids with customer UOM as value if exist
		itemCustomerUomMap =  (LinkedHashMap<String, String>) wcContext.getWCAttribute("itemCustomerUomMap", WCAttributeScope.REQUEST);
		itemCustomerUomMap = itemCustomerUomMap == null ? new LinkedHashMap<String, String>() : itemCustomerUomMap;
		
		itemUomHashMap = (HashMap<String, HashMap<String, String>>) wcContext.getWCAttribute("itemUomHashMap", WCAttributeScope.REQUEST);
		itemUomHashMap = itemUomHashMap == null ? new HashMap<String, HashMap<String, String>>() : itemUomHashMap;
		PLLineMap = (HashMap<String, List<Element>>) wcContext.getWCAttribute("PLLineMap", WCAttributeScope.REQUEST);
		PLLineMap = PLLineMap == null ? new HashMap<String, List<Element>>() : PLLineMap;
		this.component = new XPEDXItemsDataTemplateComponent(vstack, request, response, this);
		return component;
	}
	
	public Map<String, String> getDefaultShowUOMMap() {
		return defaultShowUOMMap;
	}

	public void setDefaultShowUOMMap(Map<String, String> defaultShowUOMMap) {
		this.defaultShowUOMMap = defaultShowUOMMap;
	}

	public HashMap<String, String> getOrderMultipleMap() {
		return orderMultipleMap;
	}

	public void setOrderMultipleMap(HashMap<String, String> orderMultipleMap) {
		this.orderMultipleMap = orderMultipleMap;
	}

	public IWCContext getWcContext() {
		return wcContext;
	}

	public void setWcContext(IWCContext wcContext) {
		this.wcContext = wcContext;
	}

	public String getQtyString() {
		return qtyString;
	}

	public void setQtyString(String qtyString) {
		this.qtyString = qtyString;
	}

	public String getOrderMultipleString() {
		return orderMultipleString;
	}

	public void setOrderMultipleString(String orderMultipleString) {
		this.orderMultipleString = orderMultipleString;
	}

	public String getCurrency() {
		return currency;
	}

	public HashMap<String, List<Element>> getPLLineMap() {
		return PLLineMap;
	}

	public void setPLLineMap(HashMap<String, List<Element>> pLLineMap) {
		PLLineMap = pLLineMap;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public SCUIContext getCtx() {
		return ctx;
	}

	public void setCtx(SCUIContext ctx) {
		this.ctx = ctx;
	}

	public HashMap<String, XPEDXItemBranchInfoBean> getItemToItemBranchBeanMap() {
		return itemToItemBranchBeanMap;
	}

	public void setItemToItemBranchBeanMap(HashMap<String, XPEDXItemBranchInfoBean> itemToItemBranchBeanMap) {
		this.itemToItemBranchBeanMap = itemToItemBranchBeanMap;
	}

	public HashMap<String, HashMap<String, String>> getItemMap() {
		return itemMap;
	}

	public void setItemMap(HashMap<String, HashMap<String, String>> itemMap) {
		this.itemMap = itemMap;
	}

	public HashMap<String, HashMap<String, String>> getItemUomHashMap() {
		return itemUomHashMap;
	}

	public void setItemUomHashMap(HashMap<String, HashMap<String, String>> itemUomHashMap) {
		this.itemUomHashMap = itemUomHashMap;
	}

	public String getItemElement() {
		return itemElement;
	}

	public void setItemElement(String itemElement) {
		this.itemElement = itemElement;
	}

	//Start of EB-225
	public LinkedHashMap<String, String> getItemCustomerUomMap() {
		return itemCustomerUomMap;
	}

	public void setItemCustomerUomMap(
			LinkedHashMap<String, String> itemCustomerUomMap) {
		this.itemCustomerUomMap = itemCustomerUomMap;
	}
	private LinkedHashMap<String, String> itemCustomerUomMap = new LinkedHashMap<String, String>();
	//End of EB-225
	private String itemElement;
	private IWCContext wcContext;
	private HashMap<String, HashMap<String, String>> itemUomHashMap;
	private HashMap<String, HashMap<String, String>> itemMap;
	private HashMap<String, XPEDXItemBranchInfoBean> itemToItemBranchBeanMap;
	private HashMap<String, String> orderMultipleMap;
	private Map <String, String>defaultShowUOMMap;
	private SCUIContext ctx;
	private String currency;
	private HashMap <String, List<Element>> PLLineMap;
	private String orderMultipleString = "Must be ordered in units of ";
	private String qtyString = "Qty should be greater than 0.";

}
