/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;

/**
 * @author Krithika S
 *
 */

/**
 * This class is being called on 'Cancel Order' action to make the corresponding order
 * line quantity as 0 and make the status of the Fulfillment Order as 'CANCELLED'.
 * The Customer Order can be in 'Placed' or 'Cancelled' status depending on the number of Fulfillment orders cancelled.
 */

public class XPEDXOrderCancelAction extends WCMashupAction {
	
	private String orderHeaderKey;
    private String orderListReturnUrl;
	private String orderNo;
	private String orderType;
	private String sellerOrganizationCode;
	private String webConfirmationNumber;
	
	protected final Logger log = Logger.getLogger(XPEDXOrderPlaceAction.class);
	protected String generatedErrorMessage = null;
	
	protected ArrayList<String> enteredItemIDs;
	protected ArrayList<String> enteredOrderLineKeys;
	protected ArrayList<String> enteredPrimeLineNumber;
	protected ArrayList<String> enteredSubLineNumber;
	protected ArrayList<String> enteredLegacyLineNumbers;
	protected ArrayList<String> enteredWebLineNumbers;
	protected String errorMsg;
	
	public String getErrorMsg() {
		return errorMsg;
	}



	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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
	 * @return the orderListReturnUrl
	 */
	public String getOrderListReturnUrl() {
		return orderListReturnUrl;
	}



	/**
	 * @param orderListReturnUrl the orderListReturnUrl to set
	 */
	public void setOrderListReturnUrl(String orderListReturnUrl) {
		this.orderListReturnUrl = orderListReturnUrl;
	}



	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}



	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}



	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}



	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}



	/**
	 * @return the sellerOrganizationCode
	 */
	public String getSellerOrganizationCode() {
		return sellerOrganizationCode;
	}



	/**
	 * @param sellerOrganizationCode the sellerOrganizationCode to set
	 */
	public void setSellerOrganizationCode(String sellerOrganizationCode) {
		this.sellerOrganizationCode = sellerOrganizationCode;
	}
	
	/**
	 * @return the webConfirmationNumber
	 */
	public String getWebConfirmationNumber() {
		return webConfirmationNumber;
	}



	/**
	 * @param webConfirmationNumber the webConfirmationNumber to set
	 */
	public void setWebConfirmationNumber(String webConfirmationNumber) {
		this.webConfirmationNumber = webConfirmationNumber;
	}



	/**
	 * @return the enteredItemIDs
	 */
	public ArrayList<String> getEnteredItemIDs() {
		return enteredItemIDs;
	}



	/**
	 * @param enteredItemIDs the enteredItemIDs to set
	 */
	public void setEnteredItemIDs(ArrayList<String> enteredItemIDs) {
		this.enteredItemIDs = enteredItemIDs;
	}

	/**
	 * @return the enteredOrderLineKeys
	 */
	public ArrayList<String> getEnteredOrderLineKeys() {
		return enteredOrderLineKeys;
	}



	/**
	 * @param enteredOrderLineKeys the enteredOrderLineKeys to set
	 */
	public void setEnteredOrderLineKeys(ArrayList<String> enteredOrderLineKeys) {
		this.enteredOrderLineKeys = enteredOrderLineKeys;
	}


	/**
	 * @return the enteredPrimeLineNumber
	 */
	public ArrayList<String> getEnteredPrimeLineNumber() {
		return enteredPrimeLineNumber;
	}



	/**
	 * @param enteredPrimeLineNumber the enteredPrimeLineNumber to set
	 */
	public void setEnteredPrimeLineNumber(ArrayList<String> enteredPrimeLineNumber) {
		this.enteredPrimeLineNumber = enteredPrimeLineNumber;
	}



	/**
	 * @return the enteredSubLineNumber
	 */
	public ArrayList<String> getEnteredSubLineNumber() {
		return enteredSubLineNumber;
	}



	/**
	 * @param enteredSubLineNumber the enteredSubLineNumber to set
	 */
	public void setEnteredSubLineNumber(ArrayList<String> enteredSubLineNumber) {
		this.enteredSubLineNumber = enteredSubLineNumber;
	}



	/**
	 * @return the enteredLegacyLineNumbers
	 */
	public ArrayList<String> getEnteredLegacyLineNumbers() {
		return enteredLegacyLineNumbers;
	}



	/**
	 * @param enteredLegacyLineNumbers the enteredLegacyLineNumbers to set
	 */
	public void setEnteredLegacyLineNumbers(ArrayList<String> enteredLegacyLineNumbers) {
		this.enteredLegacyLineNumbers = enteredLegacyLineNumbers;
	}



	/**
	 * @return the enteredWebLineNumbers
	 */
	public ArrayList<String> getEnteredWebLineNumbers() {
		return enteredWebLineNumbers;
	}



	/**
	 * @param enteredWebLineNumbers the enteredWebLineNumbers to set
	 */
	public void setEnteredWebLineNumbers(ArrayList<String> enteredWebLineNumbers) {
		this.enteredWebLineNumbers = enteredWebLineNumbers;
	}



	@Override
	public String execute() {
		String result = "success";
		try {
			//get the order details
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/Order/@OrderHeaderKey", orderHeaderKey);
			Element input = WCMashupHelper.getMashupInput("XPEDXOrderDetailForOrderUpdate", valueMap, wcContext.getSCUIContext());
			Object obj;
			Document orderDetailDocument = null;
			try {
				obj = WCMashupHelper.invokeMashup("XPEDXOrderDetailForOrderUpdate", input, wcContext.getSCUIContext());
				if(!YFCCommon.isVoid(obj)){
					orderDetailDocument = ((Element) obj).getOwnerDocument();
					Element orderLinesElement = (Element) orderDetailDocument.getElementsByTagName("OrderLines").item(0);
					if(orderLinesElement!=null){
						NodeList orderLineElemList = orderLinesElement.getElementsByTagName("OrderLine");
						if(orderLineElemList!=null && orderLineElemList.getLength()>0)
			    		{
			    			for(int k =0;k<orderLineElemList.getLength();k++)
			    			{
			    				Element orderLineElement = (Element)orderLineElemList.item(k);
			    				orderLineElement.setAttribute("ValidateItem","N");
			    				Element orderLineTranQuantityElement = (Element) orderLineElement.getElementsByTagName("OrderLineTranQuantity").item(0);
			    				if(orderLineTranQuantityElement!=null){
			    					orderLineTranQuantityElement.setAttribute("OrderedQty", "0.0");
			    				}
			    			}
			    		}
					}
				}
			} catch (Exception e) {
				generatedErrorMessage = "Error getting the order details from database.Please contact system admin.";
				setErrorMsg("Your order cannot be cancelled at this moment. Please try again later.");
				log.error("Exception getting order detail xml for order update..\n",e);
				result = "error";
			}
			
			try {
				Element elementCancelOrder = (Element) WCMashupHelper.invokeMashup("xpedxChangeOrderDetailsOnCancel", orderDetailDocument.getDocumentElement(), wcContext.getSCUIContext());
				if(null != elementCancelOrder)
	            {
	                setOrderHeaderKey(SCXmlUtil.getAttribute(elementCancelOrder, "OrderHeaderKey"));
	            }
			} catch (XMLExceptionWrapper e) {
				setErrorMsg("Your order cannot be cancelled at this moment. Please try again later.");
				result = "error";
			}
			return result;
		} catch (Exception ex) {
			setErrorMsg("Your order cannot be cancelled at this moment. Please try again later.");
			log.error("Unexpected error while placing the order. "+ex.getMessage(), ex);
			generatedErrorMessage = "There was an error processing your last request. Please contact the Customer Support desk at 877 269-1784, eBusiness@ipaper.com";//Message changed - JIRA 3221
			return "error";
		}
	}



	public String getGeneratedErrorMessage() {
		return generatedErrorMessage;
	}

	public void setGeneratedErrorMessage(String generatedErrorMessage) {
		this.generatedErrorMessage = generatedErrorMessage;
	}

}
