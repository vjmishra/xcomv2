package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.validators.WCValidationUtils;
import com.sterlingcommerce.webchannel.order.DraftOrderAddOrderLinesAction;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.util.YFCCommon;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsAddToCartAction extends
		DraftOrderAddOrderLinesAction {
	/*
	 * protected ArrayList<String> orderedProductIDs = new ArrayList<String>();
	 * protected ArrayList<String> orderedQuantities = new ArrayList<String>();
	 * protected ArrayList<String> orderedProductUOMs = new ArrayList<String>();
	 * protected ArrayList<String> orderedProductClasses = new
	 * ArrayList<String>();
	 */
	 protected ArrayList<String> orderedLineTypeList = new ArrayList<String>();
	 protected ArrayList<String> entereditemTypeList = new ArrayList<String>();
	
	private static final Logger LOG = Logger
			.getLogger(XPEDXMyItemsDetailsAddToCartAction.class);
	private String productID;
	protected String requestedItemID;
	protected String requestedQty;
	protected String requestedUOM;
	protected String requestedItemType;
	protected String requestedOrderHeaderKey;
	protected String requestedCustomerFields;
	protected String ExtnCustLineAccNo;
	protected String ExtnCustLineField1;
	protected String ExtnCustLineField2;
	protected String ExtnCustLineField3;
	protected String ExtnCustomerPONo;
	protected String ExtnCustomerLinePONo;
	protected ArrayList<String> enteredUOMs;
	protected ArrayList<String> enteredCustLineField1;
	protected ArrayList<String> enteredCustLineField2;
	protected ArrayList<String> enteredCustLineField3;
	protected ArrayList<String> enteredCustLineAccNo;
	protected ArrayList<String> enteredCustomerPONo;
	protected ArrayList<String> enteredCustomerLinePONo;
	protected ArrayList<String> orderedCustLineField1 = new ArrayList<String>();
	protected ArrayList<String> orderedCustLineField2 = new ArrayList<String>();
	protected ArrayList<String> orderedCustLineField3 = new ArrayList<String>();
	protected ArrayList<String> orderedCustLineAccNo = new ArrayList<String>();
	protected ArrayList<String> orderedCustomerPONo = new ArrayList<String>();
	protected ArrayList<String> orderedCustomerLinePONo = new ArrayList<String>();
	protected ArrayList<String> transactionalUOMs = new ArrayList<String>();
	protected String addToCartError;
	protected boolean addingSingleItem = false;
	protected ArrayList<String> isEditNewline=new ArrayList<String>();

	@Override
	public String execute() {
		try {
			// If a currency wasn't passed, use the effective currency.
			if (currency == null) {
				currency = getWCContext().getEffectiveCurrency();
			}

			// Process the product list.

			String editedOrderHeaderKey=XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
			isEditNewline.clear();
			if(!YFCCommon.isVoid(editedOrderHeaderKey))
			{
				orderHeaderKey=editedOrderHeaderKey;
			}
			if (getProductInformationForEnteredProducts()) {
				organizeProductInformationResults();
				if (orderedProductIDs.size() > 0
						&& !getProductID().equals("INVALID_ITEM")) {
					Element changeOrderOutput = null;
					if(orderHeaderKey.equals("_CREATE_NEW_")|| "".equalsIgnoreCase(orderHeaderKey))
					{
						String tempOrderHeaderKey=(String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");//XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext());
						if(tempOrderHeaderKey !=null && !"".equalsIgnoreCase(tempOrderHeaderKey))
						{
							orderHeaderKey	=(String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");//XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext());
						}
					}
					if((orderHeaderKey==null || orderHeaderKey.equals("") || orderHeaderKey.equals("_CREATE_NEW_")) && XPEDXOrderUtils.isCartOnBehalfOf(getWCContext())){
						 XPEDXOrderUtils.createNewCartInContext(getWCContext());
						 //orderHeaderKey =getWCContext().getWCAttribute("CommerceContextObject")
							
						 orderHeaderKey=(String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");//XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext());
					}

					try {
						XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext());
						changeOrderOutput = prepareAndInvokeMashup(MASHUP_DO_ADD_ORDER_LINES);
					} catch (Exception dle) {
						if (dle != null && dle.toString() != null
								&& dle.toString().contains("YFC0101")) {
							LOG.debug("Databse is locked, hence continuing to "
									+ "call draft order details............");
						}
					}
					XPEDXWCUtils.releaseEnv(wcContext);
					changeOrderOutputDoc = getDocFromOutput(changeOrderOutput);
					if(YFCCommon.isVoid(editedOrderHeaderKey))
					{
						XPEDXOrderUtils.refreshMiniCart(getWCContext(),changeOrderOutputDoc.getDocumentElement(),true,false,XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
					}
		            //XPEDXWCUtils.setMiniCartDataInToCache(changeOrderOutputDoc.getDocumentElement(), wcContext);
					// at this point there should not be any more errors
					// how to handle them if there are?
					//refreshCartInContext(orderHeaderKey);
				}
			}
		} catch (Exception dle) {
			LOG.debug(dle.getStackTrace());
			return "error";
		}

		if (!quickAddErrorList.isEmpty()) {
			getWCContext().setWCAttribute(getQuickAddErrorListSessionKey(),
					quickAddErrorList, WCAttributeScope.SESSION);
		}

		//return SUCCESS;
		return null;
	}

	
	/*private void setYFSEnvironmentVariables() {
		if (orderHeaderKey != null && !"".equals(orderHeaderKey))
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("isPnACall", "true");
			map.put("isDiscountCalculate", "true");
			XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext(), map);
		}
	}*/
	
	private boolean getProductInformationForEnteredProducts() {
		// enteredProductIDs should never be null in this situation - if it is,
		// then we log an error and return to the page
		if (this.enteredProductIDs == null || this.enteredQuantities == null) {
			LOG
					.error("DraftOrderAddOrderLinesAction called with a null list of product IDs");
			return false;
		}
		try {
			// call the draftOrderGetCompleteItemList mashup once for each
			// product ID in the list
			Document entitledItemsDoc = XPEDXOrderUtils.getXpedxEntitledItemDetails(enteredProductIDs, wcContext.getCustomerId(), wcContext.getStorefrontId(), wcContext);
			Iterator productIDIter = enteredProductIDs.iterator();
			while (productIDIter.hasNext()) {
				productID = (String) productIDIter.next();
				// See if we we already have verification output for this
				// product ID (would happen if the user entered the same SKU
				// more than once).
				if (verificationOutputMap.get(productID) == null
						&& !productID.equals("")) {
					//Element productInfoOutput = prepareAndInvokeMashup(MASHUP_DO_GET_COMPLETE_ITEM_LIST);
					//Document productInfoDoc = getDocFromOutput(productInfoOutput);
					Element itemElement = null;
					if(entitledItemsDoc!=null) {
						itemElement = SCXmlUtil.getElementByAttribute(entitledItemsDoc.getDocumentElement(), "/Item", "ItemID", productID);
					}
					if(itemElement!=null) {
						verificationOutputMap.put(productID, itemElement);
					}
					/*LOG
							.debug(MASHUP_DO_GET_COMPLETE_ITEM_LIST
									+ " output XML is: "
									+ XMLUtilities
											.getXMLDocString(getDocFromOutput(productInfoOutput)));*/
				}
			}
		} catch (Exception e) {
			LOG.error("Unable to retrieve "
					+ "information about the entered products");
			LOG.error(e.getStackTrace());
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private void organizeProductInformationResults()
			throws XPathExpressionException {
		// Loop through each product ID that was entered.
		for (int i = 0; i < this.enteredProductIDs.size(); i++) {
			String itemID = enteredProductIDs.get(i);
			if (itemID == null || itemID.trim().length() == 0) {
				continue;
			}
			String enteredQtyStr = this.enteredQuantities.get(i);
			if(enteredQtyStr == null || enteredQtyStr.equalsIgnoreCase("") || enteredQtyStr.equalsIgnoreCase("0")){
				continue;
			}
			String editedOrderHeaderKey=XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
			if(!YFCCommon.isVoid(editedOrderHeaderKey))
			{
				isEditNewline.add("Y");
			}
			else
			{
				isEditNewline.add("N");
			}
			String enteredUOMStr = (String)this.enteredUOMs.get(i);
			String itemType=entereditemTypeList.get(i);
			if("99.00".equals(itemType) || "99".equals(itemType) || "0.00".equals(itemType) || "0".equals(itemType))
			{
				orderedLineTypeList.add("C");
			}
			else{
				orderedLineTypeList.add("P");
			}
			
			// Fix the Long to integer problem. -PN
			enteredQtyStr = StringUtils.replace(enteredQtyStr, ".00", "");
			enteredQtyStr = StringUtils.replace(enteredQtyStr, ",", "");

			ArrayList errorStringArgs = new ArrayList();
			errorStringArgs.add(itemID); // The first arg to any error message
			// is always the product ID.
			String errorString = null;

			// Start by insuring a valid entered quantity.
			String platformValidationErrorMessage = WCValidationUtils
					.validateFieldValue("qaQuantity", enteredQtyStr,
							"OrderedQty", getWCContext());
			if (platformValidationErrorMessage != null) {
				// the UI isn't verifying quantities; handle the error and go on
				// to the next product
				LOG.error("Invalid ordered quantity of " + enteredQtyStr
						+ " for " + itemID + "("
						+ platformValidationErrorMessage + ")");
				errorStringArgs.add(enteredQtyStr);
				errorStringArgs
						.add(getText(
								platformValidationErrorMessage,
								getText(WCValidationUtils.DEFAULT_VALIDATION_ERROR_MESSAGE)));
				errorString = getText("QAInvalidQuantity", errorStringArgs);
				quickAddErrorList.add(errorString);
				continue;
			}

			// Retrieve the associated output document.
			Element itemListEl = (Element) this.verificationOutputMap
					.get(itemID);

			ItemValidationResult result = processGetCompleteItemListResult(
					itemID, itemListEl);
			String message = result.getMessage();
			if (message != null) {
				quickAddErrorList.add(message);
			}
//			if(!(enteredUOMStr!=null && enteredUOMStr.trim().length()>0)){
//				enteredUOMStr = result.getUOM();
//			}

			// if(result.isValid())
			// Add the product only if the qty is > 0
			try {
				if (Double.parseDouble(enteredQtyStr) > 0) {
					orderedProductIDs.add(result.getItemID());
					orderedQuantities.add(enteredQtyStr);
					orderedProductUOMs.add(result.getUOM());
					if(enteredUOMStr!=null && enteredUOMStr.trim().length()>0)
					{
						
						transactionalUOMs.add(enteredUOMStr);
					
					}
					else
					{
						transactionalUOMs.add(result.getUOM());
					}
					orderedProductClasses.add(result.getDefaultProductClass());
					setCustomerFieldsForOrderedItems(i);
				}
			} catch (Exception e) {
				LOG.error(e.getStackTrace());
			}
		}

	}
	
	protected ItemValidationResult processGetCompleteItemListResult(String itemID, Element itemEl)
    throws XPathExpressionException {
		
	    ItemValidationResult result = new ItemValidationResult();
	    result.setItemID(itemID);
	    ArrayList messageArgs = new ArrayList();
	    messageArgs.add(itemID);
	    String message = null;
	    if(itemEl == null)
	    {
	        message = getText("QAProductInvalid", messageArgs);
	        result.setMessage(message);
	        return result;
	    }
	    String uom = itemEl.getAttribute("UnitOfMeasure");
	    result.setUOM(uom);
	    Element primaryInfoEl = XMLUtilities.getElement(itemEl, "PrimaryInformation");
	    if(primaryInfoEl == null)
	    {
	        LOG.error((new StringBuilder()).append("getCompleteItemList failed to return a PrimaryInformation element for product ").append(itemID).toString());
	        message = getText("QAProductMayBeInvalid", messageArgs);
	        result.setMessage(message);
	        return result;
	    }
	    String isSoldSeparatelyStr = primaryInfoEl.getAttribute("IsSoldSeparately");
	    if("N".equals(isSoldSeparatelyStr))
	    {
	        message = getText("QANotSoldSeparately", messageArgs);
	        result.setMessage(message);
	        return result;
	    }
	    String isModelItem = primaryInfoEl.getAttribute("IsModelItem");
	    if("Y".equals(isModelItem))
	    {
	        message = getText("QAVariantItem", messageArgs);
	        result.setMessage(message);
	        return result;
	    }
	    String defaultProductClass = primaryInfoEl.getAttribute("DefaultProductClass");
	    result.setDefaultProductClass(defaultProductClass);
	    String minOrderQuantity = primaryInfoEl.getAttribute("MinOrderQuantity");
	    result.setMinOrderQuantity(minOrderQuantity);
	    String isValidStr = primaryInfoEl.getAttribute("IsValid");
	    if("N".equals(isValidStr))
	    {
	        String isSupersededStr = itemEl.getAttribute("IsItemSuperseded");
	        if("Y".equals(isSupersededStr))
	        {
	            Element supersedingItemEl = XMLUtilities.getElement(itemEl, "AssociationTypeList/AssociationType/AssociationList/Association/Item");
	            if(supersedingItemEl != null)
	            {
	                String supersedingProductID = supersedingItemEl.getAttribute("ItemID");
	                if(supersedingProductID == null || "".equals(supersedingProductID))
	                {
	                    LOG.error((new StringBuilder()).append(itemID).append(" has IsValid=N and IsItemSuperseded=Y, but association of type Substitutions has no ItemID").toString());
	                    message = getText("QAProductMayBeInvalid", messageArgs);
	                    result.setMessage(message);
	                    return result;
	                }
	                String supersedingUOM = supersedingItemEl.getAttribute("UnitOfMeasure");
	                String autoSubDuringSched = primaryInfoEl.getAttribute("IsSubOnOrderAllowed");
	                if("Y".equals(autoSubDuringSched))
	                {
	                    LOG.debug((new StringBuilder()).append("IsSubOnOrderAllowed flag of ").append(itemID).append(" is ").append(autoSubDuringSched).toString());
	                } else
	                {
	                    messageArgs.add(uom);
	                    messageArgs.add(supersedingProductID);
	                    messageArgs.add(supersedingUOM);
	                    message = getText("QAProductAutoSubstituted", messageArgs);
	                    result.setMessage(message);
	                    result.setItemID(supersedingProductID);
	                    result.setUOM(supersedingUOM);
	                }
	            } else
	            {
	                LOG.error((new StringBuilder()).append(itemID).append(" has IsValid=N and IsItemSuperseded=Y, but has no association of type Substitutions").toString());
	                message = getText("QAProductMayBeInvalid", messageArgs);
	                result.setMessage(message);
	                return result;
	            }
	        } else
	        {
	            message = getText("QAProductInvalid", messageArgs);
	            result.setMessage(message);
	            return result;
	        }
	    }
	    result.setValid(true);
	    return result;
	}
	
	private void setCustomerFieldsForOrderedItems(int index) 
	{
		
			String customerFieldVal = null;
			customerFieldVal = this.enteredCustLineAccNo!=null?this.enteredCustLineAccNo.get(index):"";
			if(customerFieldVal!=null && customerFieldVal.length()>0)
				orderedCustLineAccNo.add(customerFieldVal);
			
			customerFieldVal = this.enteredCustLineField1!=null?this.enteredCustLineField1.get(index):"";
			if(customerFieldVal!=null && customerFieldVal.length()>0)
				orderedCustLineField1.add(customerFieldVal);
			
			customerFieldVal = this.enteredCustLineField2!=null?this.enteredCustLineField2.get(index):"";
			if(customerFieldVal!=null && customerFieldVal.length()>0)
				orderedCustLineField2.add(customerFieldVal);
			
			customerFieldVal = this.enteredCustLineField3!=null?this.enteredCustLineField3.get(index):"";
			if(customerFieldVal!=null && customerFieldVal.length()>0)
				orderedCustLineField3.add(customerFieldVal);
			
			customerFieldVal = this.enteredCustomerPONo!=null?this.enteredCustomerPONo.get(index):"";
			if(customerFieldVal!=null && customerFieldVal.length()>0)
				orderedCustomerPONo.add(customerFieldVal);
			
			customerFieldVal = this.enteredCustomerLinePONo!=null?this.enteredCustomerLinePONo.get(index):"";
			if(customerFieldVal!=null && customerFieldVal.length()>0)
				orderedCustomerLinePONo.add(customerFieldVal);
	}
	
	public String addMyItemToCart(){
		enteredProductIDs = new ArrayList<String>();
		enteredQuantities = new ArrayList<String>();
		entereditemTypeList = new ArrayList<String>();
		enteredUOMs = new ArrayList<String>();
		enteredProductIDs.add(requestedItemID);
		enteredQuantities.add(requestedQty.trim().length()>0?requestedQty:"1");
		entereditemTypeList.add(requestedItemType);
		enteredUOMs.add(requestedUOM);
		orderHeaderKey = requestedOrderHeaderKey;
		String editedOrderHeaderKey=XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
		if(!YFCCommon.isVoid(editedOrderHeaderKey))
		{
			orderHeaderKey=editedOrderHeaderKey;
		}
		setAddingSingleItem(true);
		setCustomerDisplayFields(requestedCustomerFields);
		String returnVal = execute();
		if(returnVal!=null && returnVal.equalsIgnoreCase(ERROR))
			addToCartError = "Error while adding item to cart";
		return returnVal;
	}
	
	protected Document getsapCustExtnFieldsFromSession(){
		/*HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
        Document sapCustExtnFields = (Document)localSession.getAttribute("sapCustExtnFields");*/
		XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
		Document sapCustExtnFields = (Document)XPEDXWCUtils.getObjectFromCache("sapCustExtnFields");
        return sapCustExtnFields;
	}
	
	private void setCustomerDisplayFields(String customerFieldsString) {
		if(customerFieldsString!=null && customerFieldsString.trim().length()>0) {
			try {
				String[] customerFields = customerFieldsString.split(",");
				if(customerFields!=null && customerFields.length>0)
				{
					for(int i =0;i<customerFields.length;i++)
					{
						String currentCustFieldData = customerFields[i];
						String[] custField = currentCustFieldData.split("@");
						if(custField!=null && custField.length == 2)
						{
							String custFieldLabel = custField[0];
							String custFieldVal = custField[1];
							if(custFieldLabel.equalsIgnoreCase("ExtnCustLineAccNo"))
								ExtnCustLineAccNo = custFieldVal;
							else if(custFieldLabel.equalsIgnoreCase("ExtnCustomerPONo"))
								ExtnCustomerPONo = custFieldVal;
							else if(custFieldLabel.equalsIgnoreCase("ExtnCustomerLinePONo"))
								ExtnCustomerLinePONo = custFieldVal;
							else if(custFieldLabel.equalsIgnoreCase("ExtnCustLineField1"))
								ExtnCustLineField1 = custFieldVal;
							else if(custFieldLabel.equalsIgnoreCase("ExtnCustLineField2"))
								ExtnCustLineField2 = custFieldVal;
							else if(custFieldLabel.equalsIgnoreCase("ExtnCustLineField3"))
								ExtnCustLineField3 = custFieldVal;
						}
					}
				}
	
			} catch (Exception e) {
				LOG.error(e.getStackTrace());
			}
		}
	}
	
	/*@Override
	protected void manipulateMashupInputs(Map<String, Element> mashupInputs)
			throws CannotBuildInputException {
		Element input = mashupInputs.get(MASHUP_DO_ADD_ORDER_LINES);
		if(input!=null && addingSingleItem) {
			System.out.println(SCXmlUtil.getString(input));
			Element transactionalElem = input.getOwnerDocument().createElement("OrderLineTranQuantity");
			transactionalElem.setAttribute("TransactionalUOM", requestedUOM);
			requestedQty = StringUtils.replace(requestedQty, ".00", "");
			transactionalElem.setAttribute("OrderedQty", requestedQty.trim().length()>0?requestedQty:"1");
			Element ItemElem = SCXmlUtil.getElementByAttribute(input, "/OrderLines/OrderLine/Item", "ItemID", requestedItemID);
			Element orderLineElem = (Element)ItemElem.getParentNode();
			orderLineElem.appendChild(transactionalElem);
		}
		Element input1 = mashupInputs.get(MASHUP_DO_GET_COMPLETE_ITEM_LIST);
		if(input1!=null && addingSingleItem) {
			System.out.println(SCXmlUtil.getString(input1));
		}
		// TODO Auto-generated method stub
		super.manipulateMashupInputs(mashupInputs);
	}*/
	
	@Override
	protected void manipulateMashupInputs(Map<String, Element> mashupInputs)
			throws CannotBuildInputException {
		Element input = mashupInputs.get(MASHUP_DO_ADD_ORDER_LINES);
		if (!YFCCommon.isVoid(input)){
			YFCElement inp = YFCDocument.getDocumentFor(input.getOwnerDocument()).getDocumentElement();
			YFCElement orderLines = inp.getChildElement("OrderLines");
			Iterator orderLineIter = orderLines.getChildren();
			List remElems = new ArrayList();
			while (orderLineIter.hasNext())
			{
				YFCElement orderLineEle = (YFCElement)orderLineIter.next();
				YFCElement items = orderLineEle.getChildElement("Item");
				if (!YFCCommon.isVoid(items)){
					String itemId = items.getAttribute("ItemID");
					if (YFCCommon.isVoid(itemId)){
						remElems.add(orderLineEle);
					}
				}
				else {
					remElems.add(orderLineEle);					
				}
			}
			for (int i=0; i < remElems.size(); i++){
				orderLines.removeChild((YFCElement)remElems.get(i));
			}
		
			input = inp.getOwnerDocument().getDocument().getDocumentElement();
		}
		/*
		if(input!=null && addingSingleItem) {
			System.out.println(SCXmlUtil.getString(input));
			Element transactionalElem = input.getOwnerDocument().createElement("OrderLineTranQuantity");
			transactionalElem.setAttribute("TransactionalUOM", requestedUOM);
			requestedQty = StringUtils.replace(requestedQty, ".00", "");
			transactionalElem.setAttribute("OrderedQty", requestedQty.trim().length()>0?requestedQty:"1");
			Element ItemElem = SCXmlUtil.getElementByAttribute(input, "/OrderLines/OrderLine/Item", "ItemID", requestedItemID);
			Element orderLineElem = (Element)ItemElem.getParentNode();
			orderLineElem.appendChild(transactionalElem);
		}*/
		Element input1 = mashupInputs.get(MASHUP_DO_GET_COMPLETE_ITEM_LIST);
		if(input1!=null && addingSingleItem) {
			System.out.println(SCXmlUtil.getString(input1));
		}
		// TODO Auto-generated method stub
		super.manipulateMashupInputs(mashupInputs);
	}
	
	public String getProductID() {
		String res = productID;
		try {
			if (res == null) {
				res = "INVALID_ITEM";
			} else {
				if (res.trim().length() == 0) {
					res = "INVALID_ITEM";
				}
			}
		} catch (Exception e) {
			res = "ERROR_INVALID_ITEM";
		}
		return res;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public ArrayList<String> getOrderedLineTypeList() {
		return orderedLineTypeList;
	}

	public void setOrderedLineTypeList(ArrayList<String> orderedLineTypeList) {
		this.orderedLineTypeList = orderedLineTypeList;
	}

	public ArrayList<String> getEntereditemTypeList() {
		return entereditemTypeList;
	}

	public void setEntereditemTypeList(ArrayList<String> entereditemTypeList) {
		this.entereditemTypeList = entereditemTypeList;
	}

	public String getRequestedItemID() {
		return requestedItemID;
	}

	public void setRequestedItemID(String requestedItemID) {
		this.requestedItemID = requestedItemID;
	}

	public String getRequestedQty() {
		return requestedQty;
	}

	public void setRequestedQty(String requestedQty) {
		this.requestedQty = requestedQty;
	}

	public String getRequestedUOM() {
		return requestedUOM;
	}

	public void setRequestedUOM(String requestedUOM) {
		this.requestedUOM = requestedUOM;
	}

	public String getRequestedOrderHeaderKey() {
		return requestedOrderHeaderKey;
	}

	public void setRequestedOrderHeaderKey(String requestedOrderHeaderKey) {
		this.requestedOrderHeaderKey = requestedOrderHeaderKey;
	}

	public String getRequestedItemType() {
		return requestedItemType;
	}

	public void setRequestedItemType(String requestedItemType) {
		this.requestedItemType = requestedItemType;
	}

	public String getRequestedCustomerFields() {
		return requestedCustomerFields;
	}

	public void setRequestedCustomerFields(String requestedCustomerFields) {
		this.requestedCustomerFields = requestedCustomerFields;
	}
	
	public String getExtnCustLineAccNo() {
		return ExtnCustLineAccNo;
	}

	public void setExtnCustLineAccNo(String extnCustLineAccNo) {
		ExtnCustLineAccNo = extnCustLineAccNo;
	}

	public String getExtnCustLineField1() {
		return ExtnCustLineField1;
	}

	public void setExtnCustLineField1(String extnCustLineField1) {
		ExtnCustLineField1 = extnCustLineField1;
	}

	public String getExtnCustLineField2() {
		return ExtnCustLineField2;
	}

	public void setExtnCustLineField2(String extnCustLineField2) {
		ExtnCustLineField2 = extnCustLineField2;
	}

	public String getExtnCustLineField3() {
		return ExtnCustLineField3;
	}

	public void setExtnCustLineField3(String extnCustLineField3) {
		ExtnCustLineField3 = extnCustLineField3;
	}
	

	public String getExtnCustomerPONo() {
		return ExtnCustomerPONo;
	}

	public void setExtnCustomerPONo(String extnCustomerPONo) {
		ExtnCustomerPONo = extnCustomerPONo;
	}

	public String getExtnCustomerLinePONo() {
		return ExtnCustomerLinePONo;
	}

	public void setExtnCustomerLinePONo(String extnCustomerLinePONo) {
		ExtnCustomerLinePONo = extnCustomerLinePONo;
	}

	public boolean isAddingSingleItem() {
		return addingSingleItem;
	}

	public void setAddingSingleItem(boolean addingSingleItem) {
		this.addingSingleItem = addingSingleItem;
	}

	public ArrayList getEnteredUOMs() {
		return enteredUOMs;
	}

	public void setEnteredUOMs(ArrayList enteredUOMs) {
		this.enteredUOMs = enteredUOMs;
	}

	public ArrayList<String> getEnteredCustLineField1() {
		return enteredCustLineField1;
	}

	public void setEnteredCustLineField1(ArrayList<String> enteredCustLineField1) {
		this.enteredCustLineField1 = enteredCustLineField1;
	}

	public ArrayList<String> getEnteredCustLineField2() {
		return enteredCustLineField2;
	}

	public void setEnteredCustLineField2(ArrayList<String> enteredCustLineField2) {
		this.enteredCustLineField2 = enteredCustLineField2;
	}

	public ArrayList<String> getEnteredCustLineField3() {
		return enteredCustLineField3;
	}

	public void setEnteredCustLineField3(ArrayList<String> enteredCustLineField3) {
		this.enteredCustLineField3 = enteredCustLineField3;
	}

	public ArrayList<String> getEnteredCustLineAccNo() {
		return enteredCustLineAccNo;
	}

	public void setEnteredCustLineAccNo(ArrayList<String> enteredCustLineAccNo) {
		this.enteredCustLineAccNo = enteredCustLineAccNo;
	}

	public ArrayList<String> getEnteredCustomerPONo() {
		return enteredCustomerPONo;
	}

	public void setEnteredCustomerPONo(ArrayList<String> enteredCustomerPONo) {
		this.enteredCustomerPONo = enteredCustomerPONo;
	}

	public ArrayList<String> getEnteredCustomerLinePONo() {
		return enteredCustomerLinePONo;
	}

	public void setEnteredCustomerLinePONo(ArrayList<String> enteredCustomerLinePONo) {
		this.enteredCustomerLinePONo = enteredCustomerLinePONo;
	}

	public ArrayList<String> getOrderedCustLineField1() {
		return orderedCustLineField1;
	}

	public void setOrderedCustLineField1(ArrayList<String> orderedCustLineField1) {
		this.orderedCustLineField1 = orderedCustLineField1;
	}

	public ArrayList<String> getOrderedCustLineField2() {
		return orderedCustLineField2;
	}

	public void setOrderedCustLineField2(ArrayList<String> orderedCustLineField2) {
		this.orderedCustLineField2 = orderedCustLineField2;
	}

	public ArrayList<String> getOrderedCustLineField3() {
		return orderedCustLineField3;
	}

	public void setOrderedCustLineField3(ArrayList<String> orderedCustLineField3) {
		this.orderedCustLineField3 = orderedCustLineField3;
	}

	public ArrayList<String> getOrderedCustLineAccNo() {
		return orderedCustLineAccNo;
	}

	public void setOrderedCustLineAccNo(ArrayList<String> orderedCustLineAccNo) {
		this.orderedCustLineAccNo = orderedCustLineAccNo;
	}

	public ArrayList<String> getOrderedCustomerPONo() {
		return orderedCustomerPONo;
	}

	public void setOrderedCustomerPONo(ArrayList<String> orderedCustomerPONo) {
		this.orderedCustomerPONo = orderedCustomerPONo;
	}

	public ArrayList<String> getOrderedCustomerLinePONo() {
		return orderedCustomerLinePONo;
	}

	public void setOrderedCustomerLinePONo(ArrayList<String> orderedCustomerLinePONo) {
		this.orderedCustomerLinePONo = orderedCustomerLinePONo;
	}


	public ArrayList<String> getTransactionalUOMs() {
		return transactionalUOMs;
	}


	public void setTransactionalUOMs(ArrayList<String> transactionalUOMs) {
		this.transactionalUOMs = transactionalUOMs;
	}


	public ArrayList<String> getIsEditNewline() {
		return isEditNewline;
	}


	public void setIsEditNewline(ArrayList<String> isEditNewline) {
		this.isEditNewline = isEditNewline;
	}
	
	

}
