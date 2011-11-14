package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.order.AddToCartAction;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCI18NUtils;

public class XPEDXAddToCartAction extends AddToCartAction {

	 /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(XPEDXAddToCartAction.class);
	
	protected String getNameOfChangeOrderMashup()
    {
        return "xpedx_me_addToCartAddOrderLines";
    }
	
	 public String execute()
	 {
		 this.reqProductUOM = this.productUOM;	
		 String sOrderHeaderKey =(String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext"); //XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext());
		 if((sOrderHeaderKey==null || sOrderHeaderKey.equals("_CREATE_NEW_") )&& XPEDXOrderUtils.isCartOnBehalfOf(getWCContext())){
			 XPEDXOrderUtils.createNewCartInContext(getWCContext());
		 }
		 //set the line type to "P" since all the items coming from itemdetail/Catalog pages are Catalog Items - RUgrani
		 setLineType("P");
		 String editedOrderHeaderKey=XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
		 if(!YFCCommon.isVoid(editedOrderHeaderKey))
		 {
				orderHeaderKey=editedOrderHeaderKey;
				isEditNewline="Y";
		 }
		 XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext());
		 try
	     {
			// If a currency wasn't passed, use the effective currency.
	         if(currency == null)
	         {
	             currency = getWCContext().getEffectiveCurrency();
	         }
	         
	         // retrieve and store session information for later use
	         if (getProductInformationForEnteredProducts())
	         {
	         	if(YFCCommon.isVoid(orderHeaderKey)){
	         		
	         		String orderHeaderKeyTemp = (String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext"); //XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext(),true);
	         		if(YFCCommon.isVoid(orderHeaderKeyTemp)){
	         			return ERROR;
	         		}
	         		orderHeaderKey = orderHeaderKeyTemp;
	         		setDraft(DRAFT_Y);
	         	}
	         	else
	         	{
	         	    // Note:  only set draft to "N" if it is still null (the assumption being that we came from the
	         	    // catalog in that case).  This class is used for adding complementary and (via subclass) alternative
	         	    // items, and in those cases the draft value would already have been set properly based on whether
	         	    // we really ARE operating on a draft order or not.
	         	    // Actually, this whole "else" clause shouldn't have been necessary in the first place, since the
	         	    // draft variable should have been set via the flowID parameter when coming from the catalog during
	         	    // order change; but it looks like catalog isn't passing that parameter back to us.
	         	    if(getDraft() == null)
	         	    {
	                     setDraft(DRAFT_N);
	         	    }
		         }
		         	organizeProductInformationResults();
		         if(YFCCommon.isVoid(errorText) && !YFCCommon.isVoid(productID))
		         	{
		
		         		Element changeOrderOutput = performChangeOrder();
		         		changeOrderOutputDoc = getDocFromOutput(changeOrderOutput);
		         		 if(YFCCommon.isVoid(editedOrderHeaderKey))
		        		 {
		         			 XPEDXWCUtils.setMiniCartDataInToCache(changeOrderOutputDoc.getDocumentElement(), wcContext);
		        		 }
		         		// at this point there should not be any more errors
		         		// TODO how to handle them if there are?
		         		if(changeOrderOutput == null){
		         			return ERROR;
		         		}
		
		         		//refreshCartInContext(orderHeaderKey);
		
		         		return SUCCESS;
		
		         	}
	           }
	         XPEDXWCUtils.releaseEnv(wcContext);
	     }
	     catch (Exception e)
	     {
	         // cause of error should have been logged by the throwing method
	         e.printStackTrace();
	         return ERROR;
	     }
		 return SUCCESS;
	 }
	 

		/**
		 * Goes through product information. If product can't be added to order, stores product ID with error type.
		 * If product can be added, adds to data structures that will provide input for changeOrder.
		 * Note that the order of error detection matters, and that we only return the first error that we find
		 * If a product is valid, add it to the list of items that can be passed to the AddToCartAction mashup
		 */
		private void organizeProductInformationResults()
		throws XPathExpressionException
		{
		    String itemID = productID;
		           
		    ArrayList errorStringArgs = new ArrayList();
		    errorStringArgs.add(productID); // The first arg to any error message is always the product ID.
		    String errorString = null;
		    XPEDXWCUtils.setYFSEnvironmentVariables(wcContext);
		    ItemValidationResult result = processGetCompleteItemListResult(productID, verificationElem);
		    if(result.isValid())
		    {
		        productID = result.getItemID();
		        productUOM = result.getUOM();
		        productClass = result.getDefaultProductClass();
		    }
		    else
		    {
		        errorText = result.getMessage();
		    }
		
		    try
		    {
		    	if(YFCCommon.isVoid(quantity)){
		    		//quantity = WCDataFormatHelper.getFormattedString(WCDOMDataFieldNames.DOM_ORDERED_QUANTITY_XAPI_FIELDNAME ,result.getMinOrderQuantity(),getWCContext());
		    		quantity = result.getMinOrderQuantity();
		    	}        	
		    	if(YFCI18NUtils.getDeFormattedDouble(null, quantity) == 0)
		    	{
		    	    quantity = "1";
		    	}
		    	
		    }
		    catch (NumberFormatException e)
		    {
		        // the UI isn't verifying quantities correctly; handle the error and go on
		        // to the next product
		        LOG.error("Invalid ordered quantity " + quantity + " for " + itemID);
		        errorStringArgs.add(quantity);
		        errorString = this.getText("QAInvalidQuantity", errorStringArgs);
		        errorText = errorString; 
		        return;
		    } 
		}
		
		
		 /**
	     * Sends the requested product ID to MASHUP_DOD_GET_COMPLETE_ITEM_LIST mashup, 
	     * and saves the results
	     */
	    private boolean getProductInformationForEnteredProducts()
	    {
	        // enteredProductIDs should never be null in this situation - if it is,
	        // then we log an error and return to the page
	        if (this.productID == null)
	        {
	            LOG.error("AddToCartAction called without product ID");
	            return false;
	        }
	        try
	        {
	            // call the draftOrderGetCompleteItemList mashup once for each product ID in the list
	        	//XPEDXWCUtils.setYFSEnvironmentVariables(wcContext);      
	            Element productInfoOutput = prepareAndInvokeMashup(MASHUP_DO_GET_COMPLETE_ITEM_LIST);
	            verificationElem = productInfoOutput;
	            LOG.debug(MASHUP_DO_GET_COMPLETE_ITEM_LIST + " output XML is: " + XMLUtilities.getXMLDocString(getDocFromOutput(productInfoOutput)));
	             
	        }
	        catch (Exception e)
	        {
	            LOG.error("Unable to retrieve information about the entered products");
	            return false;
	        }
	       // XPEDXWCUtils.releaseEnv(wcContext);
	        return true;
	    }

	/* private void setYFSEnvironmentVariables() {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("isPnACall", "true");
				map.put("isDiscountCalculate", "true");
				XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext(), map);
		}*/
		
	 
	 public String getReqProductUOM() {
		return reqProductUOM;
	 }
	
	 public void setReqProductUOM(String reqProductUOM) {
		this.reqProductUOM = reqProductUOM;
	 }	 
	 public String getReqJobId() {
		return reqJobId;
	 }
	
	 public void setReqJobId(String reqJobId) {
		this.reqJobId = reqJobId;
	 }
	 public String getReqCustomer() {
		return reqCustomer;
	 }
	 public void setReqCustomer(String reqCustomer) {
		this.reqCustomer = reqCustomer;
	 }
	 
	 public String getLineType() {
		return lineType;
	}

	/**
	 * @param lineType the lineType to set
	 */
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}	


	public String getCustomerPONo() {
		return customerPONo;
	}

	public void setCustomerPONo(String customerPONo) {
		this.customerPONo = customerPONo;
	}


	public String getIsEditNewline() {
		return isEditNewline;
	}

	public void setIsEditNewline(String isEditNewline) {
		this.isEditNewline = isEditNewline;
	}


	protected String reqProductUOM;
	protected String reqJobId;
	protected String reqCustomer;
	protected String lineType;
	protected String customerPONo="";
	protected String isEditNewline="N";
}
