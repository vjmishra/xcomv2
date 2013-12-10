package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.comergent.appservices.configuredItem.XMLUtils;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.order.AddToCartAction;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceAndAvailability;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;

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
		 this.reqProductQuantity=this.quantity;
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
	         		
         		if(YFCCommon.isVoid(editedOrderHeaderKey)){
         			draftOrderflag="Y";

         		}
         		else{
         			draftOrderflag="N";
         		}
		         	organizeProductInformationResults();
		         	// Added for addtocart base UOM issue, due to performance fixes
		         	HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
		         	productUOM = httpRequest.getParameter("baseUnitOfMeasure");
		         	
		         	
		         if(YFCCommon.isVoid(errorText) && !YFCCommon.isVoid(productID))
		         	{
		
		         		Element changeOrderOutput = performChangeOrder();
		         			try
		         			{
				         		Integer maxItemLineNum = null;
				         		Integer maxLineNum = null;
				         		String responseXML = null;
				         		Element additionalAttrXmlList = SCXmlUtil.getChildElement(changeOrderOutput, "Extn");
				         		Element additionalAttrXml = XMLUtilities.getElement(additionalAttrXmlList,"XPXUeAdditionalAttrXmlList/XPXUeAdditionalAttrXml");
				         		if(additionalAttrXml != null){
					         	    responseXML = additionalAttrXml.getAttribute("ResponseXML");
					         		Document attrXml =SCXmlUtil.createFromString(responseXML);
									Element priceAvailXml = attrXml.getDocumentElement();
					         		
									Element itemPrice = XMLUtilities.getElement(priceAvailXml, "Items/Item");
									Document doc  = itemPrice.getOwnerDocument();
									YFCElement rootEle = YFCDocument.getDocumentFor(doc).getDocumentElement();
									
									if (rootEle.hasChildNodes()) {
										YFCElement lineItem =  rootEle.getChildElement("Items");
										YFCIterable<YFCElement> yfcItr = (YFCIterable<YFCElement>) lineItem.getChildren("Item");
										while (yfcItr.hasNext()) {
											YFCElement lineElem = (YFCElement) yfcItr.next();
											YFCElement lineNumberElem = lineElem.getChildElement("LineNumber");
											YFCElement itemIdElem = lineElem.getChildElement("LegacyProductCode");
											YFCElement orderMultipleQtyElem = lineElem.getChildElement("OrderMultipleQty");
											YFCElement lineStatusCode = lineElem.getChildElement("LineStatusCode");
											YFCElement requestedQtyElem = lineElem.getChildElement("RequestedQty"); 
											YFCElement requestedUomElem = lineElem.getChildElement("RequestedQtyUOM");//EB-3651
											String legacyProductCode1 = itemIdElem.getNodeValue();
											String requestedQty = requestedQtyElem.getNodeValue();
											String requestedUom = requestedUomElem.getNodeValue();
											if(this.reqProductUOM!= null && 
													(this.reqProductUOM.equals("") || this.reqProductUOM.equals("M_"))){											
												System.out.println("XPEDXAddToCartAction:requestedUom::::::"+requestedUom);//EB-3651
												System.out.println("XPEDXAddToCartAction:this.reqProductUOM::::::"+this.reqProductUOM);//EB-3651
											}
											if(lineStatusCode.getNodeValue()!= null && lineStatusCode.getNodeValue().equalsIgnoreCase("14") 
													&& legacyProductCode1.equalsIgnoreCase(this.productID) && requestedQty.equalsIgnoreCase(this.reqProductQuantity) && requestedUom.equalsIgnoreCase(this.reqProductUOM)){								
												orderMultipleErrorItems.add(legacyProductCode1);
											}
											lineElem.getLastChild();
											String str = lineNumberElem.getNodeValue();
											String legacyProductCode=itemIdElem.getNodeValue();
											if(maxLineNum == null && str != null && str.length()>0 )
												maxLineNum=Integer.valueOf(str);
											if(maxItemLineNum == null && str != null && str.length()>0 && 
													legacyProductCode != null && productID.equals(legacyProductCode))
											{
												Integer currentLine=Integer.valueOf(str);
												maxItemLineNum =currentLine;												
												if(currentLine >= maxLineNum)
												{
													maxLineNum=currentLine;
												}
											}
											else if(str != null && str.length()>0 )
											{
												Integer currentLine=Integer.valueOf(str);
												if(productID.equals(legacyProductCode) && currentLine > maxItemLineNum)
												{
													maxItemLineNum=currentLine;
												}
												if(currentLine > maxLineNum)
												{
													maxLineNum=currentLine;
												}
											}
										}
										yfcItr = (YFCIterable<YFCElement>) lineItem.getChildren("Item");
										while (yfcItr.hasNext()) {
											YFCElement lineElem = (YFCElement) yfcItr.next();
											YFCElement lineStatusCodeElem = lineElem.getChildElement("LineNumber");
											lineElem.getLastChild();
											String str = lineStatusCodeElem.getNodeValue();
											int lineNumber=Integer.parseInt(str);
											if(maxItemLineNum != null && lineNumber == maxItemLineNum && maxItemLineNum == maxLineNum)
												continue;
											else
												lineItem.removeChild(lineElem);
										}
										
									}
									if(maxItemLineNum == maxLineNum)
										XPEDXWCUtils.setObectInCache("PNA_RESPONSE_FOR_ITEM",doc) ;
				         		}
		         			}
		         			catch(Exception e)
		         			{
		         				LOG.error("Exception while getting item from PnA Response "+e.getMessage());
		         			}
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
		         		//orderMultipleErrorItems = XPEDXPriceandAvailabilityUtil.processPNAResponseForOrderMultiple(changeOrderOutputDoc);
		         		if(orderMultipleErrorItems.contains(productID))
		         			return "MaxError";
		         		//refreshCartInContext(orderHeaderKey);
		         		XPEDXWCUtils.releaseEnv(wcContext);
		         		return SUCCESS;
		
		         	}
	           }
	         XPEDXWCUtils.releaseEnv(wcContext);
	     }
		 catch(XMLExceptionWrapper e)
		 {
			 YFCElement errorXML=e.getXML();
			 YFCElement errorElement=(YFCElement)errorXML.getElementsByTagName("Error").item(0);
			 String errorDeasc=errorElement.getAttribute("ErrorDescription");
			 if(errorDeasc.contains("Key Fields cannot be modified."))
			 {
				 YFCNodeList listAttribute=errorElement.getElementsByTagName("Attribute");
				 for(int i=0;i<listAttribute.getLength();i++)
				 {
					 YFCElement attributeELement=(YFCElement)listAttribute.item(i);
					 String value=attributeELement.getAttribute("Value");
					 if("DraftOrderFlag".equals(value))
					 {
						 draftError = "true";
					 }
				 }
			 }
			 if(errorDeasc.contains("value larger than specified precision allowed for this column"))
			 {
				 quantitydraftError = "true";
			 }
			 YFCNodeList<YFCElement> errorNodeList=errorXML.getElementsByTagName("Error");
			boolean isOUErrorPage=false;
 			for(YFCElement errorEle:errorNodeList)
 			{
 				String errorCode=errorEle.getAttribute("ErrorCode");
 				if(XPEDXConstants.UE_ERROR_CODE.equalsIgnoreCase(errorCode) || XPEDXConstants.UE_ERROR_CODE1.equalsIgnoreCase(errorCode))
 				{
 					isOUErrorPage=true;
 					break;
 				}
 			}
 			if(isOUErrorPage)
 			{
 				ouErrorMessage=XPEDXConstants.UE_ERROR_CODE;
 				return "OUErrorPage"; 
 			}
			 return draftErrorFlag;
		 }
	     catch (Exception e)
	     {
	         // cause of error should have been logged by the throwing method
	         e.printStackTrace();
			 XPEDXWCUtils.logExceptionIntoCent(e);  //JIRA 4289
	         System.out.println(" Add to Cart Undefined : " +e.getMessage());
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
			XPEDXWCUtils.setYFSEnvironmentVariables(wcContext);
		    
			/* Begin - Changes made by Mitesh Parikh
			 * Service call to getItemListForOrdering API commented to improve performance of addToCart action. 
			 * Task of checking the entitlement of an item is accomplished by changing the value of attribute ValidateItem (of changeOrder API) to 'Y' 
			 * */
			/*String itemID = productID;
		           
		    ArrayList errorStringArgs = new ArrayList();
		    errorStringArgs.add(productID); // The first arg to any error message is always the product ID.
		    String errorString = null;*/
		    
		    /*ItemValidationResult result = processGetCompleteItemListResult(productID, verificationElem);
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
		    } */
		    /* End - Changes made by Mitesh Parikh */
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
	        /*try
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
	        }*/
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

	protected List<String> orderMultipleErrorItems = new ArrayList<String>();
	public List<String> getOrderMultipleErrorItems() {
		return orderMultipleErrorItems;
	}

	public void setOrderMultipleErrorItems(List<String> orderMultipleErrorItems) {
		this.orderMultipleErrorItems = orderMultipleErrorItems;
	}

	protected String reqProductUOM;
	protected String reqProductQuantity;
	public String getReqProductQuantity() {
		return reqProductQuantity;
	}

	public void setReqProductQuantity(String reqProductQuantity) {
		this.reqProductQuantity = reqProductQuantity;
	}

	protected String reqJobId;
	protected String reqCustomer;
	protected String lineType;
	protected String customerPONo="";
	protected String isEditNewline="N";
	public String draftOrderflag;
	public String draftErrorFlag="DraftError";
	private String draftError= "false";
	private String quantitydraftError="false";

	public void setQuantitydraftError(String quantitydraftError) {
		this.quantitydraftError = quantitydraftError;
	}
	
	public String getQuantitydraftError() {
		return quantitydraftError;
	}


	private String ouErrorMessage;


	public String getDraftError() {
		return draftError;
	}

	public void setDraftError(String draftError) {
		this.draftError = draftError;
	}

	public String getOuErrorMessage() {
		return ouErrorMessage;
	}

	public void setOuErrorMessage(String ouErrorMessage) {
		this.ouErrorMessage = ouErrorMessage;
	}

	
}
