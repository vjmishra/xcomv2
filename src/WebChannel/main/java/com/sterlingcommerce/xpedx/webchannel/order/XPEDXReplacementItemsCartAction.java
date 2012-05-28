/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.order;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.sterlingcommerce.webchannel.order.OrderSaveBaseAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

/**
 * @author Administrator
 *
 */
public class XPEDXReplacementItemsCartAction extends OrderSaveBaseAction{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(XPEDXReplacementItemsCartAction.class);
	protected String orderHeaderKey;
	protected String key;
	private String itemId;
	private static String associationTypesOfInterest[] = { "Substitutions" };
	protected String currency;
	protected String originalQuantity;
	protected String qty;
	protected String uomId;
	protected String orderedProductDefaultUOM;
	protected String orderedProductClass;
	protected String desc;
	protected String jobId;
	protected String itemType;
	protected String recordPendingChange;

	private String addToList = null;
	
	public JSONObject productsJson;
	public String prodValdAjaxResp = null;
	public static final String MASHUP_DO_ADD_ORDER_LINES = "xpedx_me_draftOrderAddOrderLines";
	public static final String MASHUP_DO_DELETE_ORDER_LINES = "xpedx_me_draftOrderReplaceOrderLines";
	private String orderedLineType;	

	public String execute() {
		try {
			setDefaultvaluesIfNull();
			if("true".equalsIgnoreCase(getAddToList())){// invoke only add mashup
														// Add to cart
				
				LOG.debug(" XPEDXReplacementItemsCartAction :: execute :: Adding the replacement item:  "+ getItemId() +" available to cart");
				prepareAndInvokeMashup(MASHUP_DO_ADD_ORDER_LINES);
			}else{ // Replace item in cart
				// invoke add and then delete.
				LOG.debug(" XPEDXReplacementItemsCartAction :: execute :: Replacing the cart with the selected item:  "+ getItemId() +" available to cart");
				prepareAndInvokeMashup(MASHUP_DO_ADD_ORDER_LINES);
				prepareAndInvokeMashup(MASHUP_DO_DELETE_ORDER_LINES);
			}
		} catch (XMLExceptionWrapper xmle) {
			LOG.error("Exception in XPEDXReplacementItemsCartAction :: execute :: "+xmle.getMessage() , xmle);
			return "error";
		} catch (CannotBuildInputException cbie) {
			LOG.error("Exception in XPEDXReplacementItemsCartAction :: execute :: "+cbie.getMessage() , cbie);
			return "error";
		}
		return "success";
	}

	private void setDefaultvaluesIfNull() {
		if(YFCUtils.isVoid(getQty())){
			setQty("1");
		}
		setOrderedLineType("P");
	}

	/**
	 * @return the orderHeaderKey
	 */
	public String getOrderHeaderKey() {
		return orderHeaderKey;
	}

	/**
	 * @param orderHeaderKey the orderHeaderKey to set
	 */
	public void setOrderHeaderKey(String orderHeaderKey) {
		this.orderHeaderKey = orderHeaderKey;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the associationTypesOfInterest
	 */
	public static String[] getAssociationTypesOfInterest() {
		return associationTypesOfInterest;
	}

	/**
	 * @param associationTypesOfInterest the associationTypesOfInterest to set
	 */
	public static void setAssociationTypesOfInterest(
			String[] associationTypesOfInterest) {
		XPEDXReplacementItemsCartAction.associationTypesOfInterest = associationTypesOfInterest;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the qty
	 */
	public String getQty() {
		return qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(String qty) {
		this.qty = qty;
	}

	/**
	 * @return the uomId
	 */
	public String getUomId() {
		return uomId;
	}

	/**
	 * @param uomId the uomId to set
	 */
	public void setUomId(String uomId) {
		this.uomId = uomId;
	}

	/**
	 * @return the orderedProductDefaultUOM
	 */
	public String getOrderedProductDefaultUOM() {
		return orderedProductDefaultUOM;
	}

	/**
	 * @param orderedProductDefaultUOM the orderedProductDefaultUOM to set
	 */
	public void setOrderedProductDefaultUOM(String orderedProductDefaultUOM) {
		this.orderedProductDefaultUOM = orderedProductDefaultUOM;
	}

	/**
	 * @return the orderedProductClass
	 */
	public String getOrderedProductClass() {
		return orderedProductClass;
	}

	/**
	 * @param orderedProductClass the orderedProductClass to set
	 */
	public void setOrderedProductClass(String orderedProductClass) {
		this.orderedProductClass = orderedProductClass;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the jobId
	 */
	public String getJobId() {
		return jobId;
	}

	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * @return the recordPendingChange
	 */
	public String getRecordPendingChange() {
		return recordPendingChange;
	}

	/**
	 * @param recordPendingChange the recordPendingChange to set
	 */
	public void setRecordPendingChange(String recordPendingChange) {
		this.recordPendingChange = recordPendingChange;
	}

	/**
	 * @return the addToList
	 */
	public String getAddToList() {
		return addToList;
	}

	/**
	 * @param addToList the addToList to set
	 */
	public void setAddToList(String addToList) {
		this.addToList = addToList;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	public String getOriginalQuantity() {
		return originalQuantity;
	}

	public void setOriginalQuantity(String originalQuantity) {
		this.originalQuantity = originalQuantity;
	}	

	public String getOrderedLineType() {
		return orderedLineType;
	}

	public void setOrderedLineType(String orderedLineType) {
		this.orderedLineType = orderedLineType;
	}
}
