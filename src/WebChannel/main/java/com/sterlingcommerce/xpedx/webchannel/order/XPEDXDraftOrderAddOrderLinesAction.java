package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.xpath.XPathExpressionException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.validators.WCValidationUtils;
import com.sterlingcommerce.webchannel.order.OrderItemValidationBaseAction;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXDraftOrderAddOrderLinesAction extends
		OrderItemValidationBaseAction {

	public XPEDXDraftOrderAddOrderLinesAction() {
		verificationOutputMap = new HashMap();
		quickAddErrorList = new ArrayList();
		orderedJobIDs = new ArrayList();
		orderedPONos = new ArrayList();
		orderedLineTypes = new ArrayList();
		orderedProductIDs = new ArrayList();
		orderedQuantities = new ArrayList();
		orderedTranQuantities = new ArrayList();
		orderedProductDescs = new ArrayList();
		orderedProductUOMs = new ArrayList();
		orderedProductDefaultUOMs = new ArrayList();
		quickAddOrderMultiple =  new ArrayList();
		orderedProductClasses = new ArrayList();
		isEditNewline=new ArrayList();
		changeOrderOutputDoc = null;
		orderHeaderKey = null;
		quickAddErrorListSessionKey = null;
	}

	public String execute() {
		try {
			if (currency == null)
				currency = getWCContext().getEffectiveCurrency();
			
			boolean detailsOfProds = getProductInformationForEnteredProducts();
			isEditNewline.clear();
			if (detailsOfProds) {
				XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext());
				organizeProductInformationResults();
				
				if (orderedProductIDs.size() > 0) {
					//start of XBT 252 & 248
					String editedOrderHeaderKey = XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
					if(YFCCommon.isVoid(editedOrderHeaderKey)){
						draftOrderFlag="Y";	
					}
					else {
						draftOrderFlag="N";	
					}
					//end of XBT 252 & 248
					Element changeOrderOutput = prepareAndInvokeMashup(MASHUP_DO_ADD_ORDER_LINES);
					changeOrderOutputDoc = getDocFromOutput(changeOrderOutput);
					getWCContext().getSCUIContext().getSession().setAttribute(CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ, changeOrderOutputDoc);
					refreshCartInContext(orderHeaderKey);
				}
			}
		} 
		//start of XBT 252 & 248
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
                                draftOrderError = "true";
                                break;
                          }
                    }
              }
        }
		//end of XBT 252 & 248
		catch (Exception databaseLockException) {
			if (databaseLockException != null
					&& databaseLockException.toString() != null
					&& databaseLockException.toString().contains("YFC0101")) {
				LOG.debug("Databse is locked, hence continuing to "
						+ "call draft order details............");
				return "success";
			}
			LOG.debug(databaseLockException);
			XPEDXWCUtils.releaseEnv(wcContext);
			return "error";
		}
		if (!quickAddErrorList.isEmpty())
			getWCContext().setWCAttribute(getQuickAddErrorListSessionKey(),
					quickAddErrorList, WCAttributeScope.SESSION);
		XPEDXWCUtils.releaseEnv(wcContext);
		return "success";
	}

	@SuppressWarnings("unchecked")
	public String validateProduct() {
		try {
			if (currency == null)
				currency = getWCContext().getEffectiveCurrency();
			// productsJson = new JSONObject();
			String items = request.getParameter("itemList");
			String itemTypes = request.getParameter("itemTypeList");
			//Modified for JIRA 2995
			StringTokenizer itemsStringToken = new StringTokenizer(items, "*");
			StringTokenizer itemTypeStringToken = new StringTokenizer(
					itemTypes, "*");
			StringBuilder sb = new StringBuilder();
			Map<String, String> itemIdsOrderMultipleMap = new HashMap<String, String>();
			while (itemsStringToken.hasMoreTokens()) {
				List<String> productList = new ArrayList<String>();
				productID = itemsStringToken.nextToken();
				String bufferProductID = productID;
				String itemType = itemTypeStringToken.nextToken();
				Document productInfoDoc = null;
				if ("1".equals(itemType)) { //"1 = xpedx Item #" 
					Element productInfoOutput = prepareAndInvokeMashup("addToCartGetCompleteItemList");
					productInfoDoc = getDocFromOutput(productInfoOutput); 
				} else if ("2".equals(itemType))  { //Manufacturer # 
					Element productInfoOutput = prepareAndInvokeMashup("xpedxAddToCartGetCompleteItemList");
					productInfoDoc = getDocFromOutput(productInfoOutput);
					if(productInfoDoc!=null)
					{
						ArrayList<Element> itemElem = getXMLUtils().getElements(productInfoOutput, "Item");
						if(itemElem!=null){
							for(Iterator<Element> iterator = itemElem.iterator(); iterator.hasNext();){
								Element itemList = (Element)iterator.next();						
								productID = itemList.getAttribute("ItemID");
							} // end of for
						} // end of if(itemElem!=null)              
					}
				} else if("3".equals(itemType)) //Customer Part #
				{
					String customerPartNo = productID;
					Map itemAttr = new HashMap();
					itemAttr.put("CustomerItemNumber", customerPartNo);
					Element itemCustXrefEle = XPEDXWCUtils.getItemCustXrefInfo(itemAttr);
					if(itemCustXrefEle != null)
					{
						Element custXrefEle = XMLUtilities.getElement(itemCustXrefEle,"XPXItemcustXref");
						String itemId = SCXmlUtils.getAttribute(custXrefEle, "LegacyItemNumber");
						if(itemId!=null)
						{
							productID = itemId;
							Element productInfoOutput = prepareAndInvokeMashup("addToCartGetCompleteItemList");
							productInfoDoc = getDocFromOutput(productInfoOutput);
						}
					}
				} else if("4".equals(itemType)) //4 = MPC Code
				{
					/*String mpc = productID;
					Map itemAttr = new HashMap();
					itemAttr.put("MPC", mpc);
					Element itemCustXrefEle = XPEDXWCUtils.getItemCustXrefInfo(itemAttr);
					if(itemCustXrefEle != null)
					{
						Element custXrefEle = XMLUtilities.getElement(itemCustXrefEle,"XPXItemcustXref");
						String itemId = SCXmlUtils.getAttribute(custXrefEle, "LegacyItemNumber");
						if(itemId!=null)
						{
							productID = itemId;
							Element productInfoOutput = prepareAndInvokeMashup("addToCartGetCompleteItemList");
							productInfoDoc = getDocFromOutput(productInfoOutput);
						}
					}*/
					Element productInfoOutput = prepareAndInvokeMashup("xpedxGetCompleteItemListWithMpc");
					productInfoDoc = getDocFromOutput(productInfoOutput);
					if(productInfoDoc!=null)
					{
						Element itemElem = getXMLUtils().getChildElement(productInfoOutput, "Item");
						if(itemElem!=null)
							productID = itemElem.getAttribute("ItemID");
					}
				}
				
				Element itemListEl = null;
				Element itemEl = null;
				if(productInfoDoc!=null)
				{
					itemListEl = productInfoDoc.getDocumentElement();
					itemEl = XMLUtilities.getElement(itemListEl, "Item");
				}
				

				if (itemEl != null) {
					OrderItemValidationBaseAction.ItemValidationResult result = processGetCompleteItemListResult(
							productID, itemListEl);
					productList.add(productID);
					itemIdsOrderMultipleMap = XPEDXOrderUtils.getOrderMultipleForItems(productList);
					//Commented since GetCompleteItemList also validate the item Entitlement
					if (result.isValid() /*&& validateItemEntitlement(productID)*/) {
						sb.append(true);
						Map uomMap = getUOMlist(productID);
						if (!uomMap.isEmpty()) {
							sb.append("*");
							Set keys = uomMap.keySet();
							Iterator keyIter = keys.iterator();
							while (keyIter.hasNext()) {
								String uom = (String) keyIter.next();
//								String uomDesc = XPEDXWCUtils.getUOMDescription(uom);
								String uomConvfactr = (String) uomMap.get(uom);
								sb.append(uom);
								sb.append(":");
								sb.append(uomConvfactr);
								if (keyIter.hasNext()) {
									sb.append("!");
								}
							}
						}
						sb.append("|");
						sb.append(itemIdsOrderMultipleMap.get(productID));
						sb.append(",");
					} else {
						sb.append(false);
						sb.append(",");
					}
				} else {
					// productsJson.put(productID, false);
					sb.append(false);
					sb.append(",");
				}

			}
			int index = sb.lastIndexOf(",");
			if (index != -1) {
				sb.deleteCharAt(index);
			}
			prodValdAjaxResp = sb.toString();

		} catch (Exception e) {
			LOG
					.error("Unable to retrieve information about the entered products");
		}
		return "success";
	}

	private boolean getProductInformationForEnteredProducts() {
		if (enteredProductIDs == null || enteredQuantities == null) {
			LOG
					.error("XPEDXDraftOrderAddOrderLinesAction called with a null list of product IDs");
			return false;
		}
		try {
			Iterator productIDIter = enteredProductIDs.iterator();
			Iterator itemTypeIter = enteredItemTypes.iterator();
			do {
				if (!productIDIter.hasNext())
					break;
				String itemType = (String) itemTypeIter.next();

				if ("1".equals(itemType)) {
					productID = (String) productIDIter.next();
					if (verificationOutputMap.get(productID) == null) {
						Element productInfoOutput = prepareAndInvokeMashup("addToCartGetCompleteItemList");
						Document productInfoDoc = getDocFromOutput(productInfoOutput);
						verificationOutputMap.put(productID, productInfoDoc
								.getDocumentElement());
						LOG
								.debug((new StringBuilder())
										.append(
												"addToCartGetCompleteItemList output XML is: ")
										.append(
												XMLUtilities
														.getXMLDocString(getDocFromOutput(productInfoOutput)))
										.toString());
					}
				} else if ("2".equals(itemType)) {
					productID = (String) productIDIter.next();
					if (verificationOutputMap.get(productID) == null) {
						Element productInfoOutput = prepareAndInvokeMashup("xpedxAddToCartGetCompleteItemList");
						Document productInfoDoc = getDocFromOutput(productInfoOutput);
						verificationOutputMap.put(productID, productInfoDoc
								.getDocumentElement());
						LOG
								.debug((new StringBuilder())
										.append(
												"xpedxAddToCartGetCompleteItemList output XML is: ")
										.append(
												XMLUtilities
														.getXMLDocString(getDocFromOutput(productInfoOutput)))
										.toString());
					}
				}else if("3".equals(itemType))
				{
					productID = (String) productIDIter.next();
					if (verificationOutputMap.get(productID) == null) {
						String customerPartNo = productID;
						Map itemAttr = new HashMap();
						itemAttr.put("CustomerItemNumber", customerPartNo);
						Element itemCustXrefEle = XPEDXWCUtils.getItemCustXrefInfo(itemAttr);
						if(itemCustXrefEle != null)
						{
							Element custXrefEle = XMLUtilities.getElement(itemCustXrefEle,"XPXItemcustXref");
							String itemId = SCXmlUtils.getAttribute(custXrefEle, "LegacyItemNumber");
							if(itemId!=null)
							{
								productID = itemId;
								Element productInfoOutput = prepareAndInvokeMashup("addToCartGetCompleteItemList");
								Document productInfoDoc = getDocFromOutput(productInfoOutput);
								verificationOutputMap.put(customerPartNo, productInfoDoc
										.getDocumentElement());
								LOG
										.debug((new StringBuilder())
												.append(
														"xpedxAddToCartGetCompleteItemList output XML is: ")
												.append(
														XMLUtilities
																.getXMLDocString(getDocFromOutput(productInfoOutput)))
												.toString());
							}
						}
					}
				}else if("4".equals(itemType))
				{
					productID = (String) productIDIter.next();
					if (verificationOutputMap.get(productID) == null) {
						String mpc = productID;
						/*Map itemAttr = new HashMap();
						itemAttr.put("MPC", mpc);*/
						
						Element productInfoOutput = prepareAndInvokeMashup("xpedxGetCompleteItemListWithMpc");
						Document productInfoDoc = getDocFromOutput(productInfoOutput);
						if(productInfoDoc!=null)
						{
							Element itemElem = getXMLUtils().getChildElement(productInfoOutput, "Item");
							if(itemElem!=null)
								productID = itemElem.getAttribute("ItemID");
							verificationOutputMap.put(mpc, productInfoDoc
									.getDocumentElement());
						}
						/*Element itemCustXrefEle = XPEDXWCUtils.getItemCustXrefInfo(itemAttr);
						if(itemCustXrefEle != null)
						{
							Element custXrefEle = XMLUtilities.getElement(itemCustXrefEle,"XPXItemcustXref");
							String itemId = SCXmlUtils.getAttribute(custXrefEle, "LegacyItemNumber");
							if(itemId!=null)
							{
								productID = itemId;
								Element productInfoOutput = prepareAndInvokeMashup("addToCartGetCompleteItemList");
								Document productInfoDoc = getDocFromOutput(productInfoOutput);
								verificationOutputMap.put(mpc, productInfoDoc
										.getDocumentElement());
								LOG
										.debug((new StringBuilder())
												.append(
														"xpedxAddToCartGetCompleteItemList output XML is: ")
												.append(
														XMLUtilities
																.getXMLDocString(getDocFromOutput(productInfoOutput)))
												.toString());
							}
						}*/
					}
				}
				
			} while (true);
		} catch (Exception e) {
			LOG
					.error("Unable to retrieve information about the entered products");
		}
		return true;
	}

	private void organizeProductInformationResults()
			throws XPathExpressionException {
		//Getting itemMap from Session For Minicart Jira 3481
		HashMap<String,String> itemMapObj = (HashMap<String, String>) XPEDXWCUtils.getObjectFromCache("itemMap");
		for (int i = 0; i < enteredProductIDs.size(); i++) {
			String jobId = "";
			if(enteredJobIDs != null)
				jobId = (String) enteredJobIDs.get(i);
			String PONo = "";
			if(enteredPONos != null)
				PONo = (String) enteredPONos.get(i);
			String itemID = (String) enteredProductIDs.get(i);
			String enteredQtyStr = (String) enteredQuantities.get(i);
			String enteredProdDesc = (String) enteredProductDescs.get(i);
			String enteredUOM = (String) enteredUOMs.get(i);
			if(enteredUOM != null)
				enteredUOM=enteredUOM.trim();
			ArrayList errorStringArgs = new ArrayList();
			errorStringArgs.add(itemID);
			String errorString = null;
			String platformValidationErrorMessage = WCValidationUtils
					.validateFieldValue("qaQuantity", enteredQtyStr,
							"OrderedQty", getWCContext());
			if (platformValidationErrorMessage != null) {
				LOG.error((new StringBuilder()).append(
						"Invalid ordered quantity of ").append(enteredQtyStr)
						.append(" for ").append(itemID).append("(").append(
								platformValidationErrorMessage).append(")")
						.toString());
				errorStringArgs.add(enteredQtyStr);
				errorStringArgs.add(getText(platformValidationErrorMessage,
						getText("default.Invalid")));
				errorString = getText("QAInvalidQuantity", errorStringArgs);
				quickAddErrorList.add(errorString);
				continue;
			}
			Element itemListEl = (Element) verificationOutputMap.get(itemID);
			String originalItemId = itemID;
			if(itemListEl != null)
			{
				Element itemEl = XMLUtilities.getElement(itemListEl, "Item");
				if(itemEl != null)
				{
					originalItemId = itemEl.getAttribute("ItemID");
					//Added For Jira 3481 orderMultiple variable
					String orderMultiple = (String)getQuickAddOrderMultiple().get(i);
					//Checking condition For Jira 3481
					if(itemMapObj !=null )
					{
						itemMapObj.put(originalItemId, orderMultiple);
					}
					//End Checking condition For Jira 3481
				}
			}
			OrderItemValidationBaseAction.ItemValidationResult result = processGetCompleteItemListResult(
					originalItemId, itemListEl);
			String message = result.getMessage();
			if (message != null
					&& (enteredSpecialItems != null && !enteredSpecialItems
							.contains(itemID)))
				quickAddErrorList.add(message);
			if (result.isValid()
					|| (enteredSpecialItems != null && enteredSpecialItems
							.contains(itemID))) {
				orderedJobIDs.add(jobId);
				orderedPONos.add(PONo);
				orderedProductIDs.add(result.getItemID());
				orderedQuantities.add(enteredQtyStr);
				orderedTranQuantities.add(enteredQtyStr);
				orderedProductUOMs.add(result.getUOM());
				// orderedProductDefaultUOMs.add(getDefaultUOM(result.getItemID()));
				orderedProductDefaultUOMs.add(getUOM(enteredUOM,itemID));
				orderedProductClasses.add(result.getDefaultProductClass());
				if (enteredSpecialItems != null
						&& enteredSpecialItems.contains(itemID)) {
					orderedLineTypes.add("C");
					orderedProductDescs.add(enteredProdDesc);
				} else {
					orderedLineTypes.add("P");
					orderedProductDescs.add("");
				}
				if("true".equals(isEditOrder))
				{
					isEditNewline.add("Y");
				}
				else
				{
					isEditNewline.add("N");
				}
			}
		}
		//Set itemMap MAP again in session Jira 3481
		XPEDXWCUtils.setObectInCache("itemMap",itemMapObj);
		//set a itemsUOMMap in Session for ConvFactor
		XPEDXWCUtils.setObectInCache("itemsUOMMap",XPEDXOrderUtils.getXpedxUOMList(wcContext.getCustomerId(), getEnteredProductIDs(), wcContext.getStorefrontId()));

	}

	private String getUOM(String enteredUOM, String itemID) {
		if(enteredUOM!=null && enteredUOM.trim().length()>0) {
			enteredUOM=enteredUOM.trim();
			if(enteredUOM.indexOf('(')!= -1 && enteredUOM.indexOf(')')!= -1) {
				String UOMDesc = enteredUOM.substring(0,enteredUOM.indexOf('('));
				String convFactor = enteredUOM.substring(enteredUOM.indexOf("(")+1, enteredUOM.lastIndexOf(")"));
				enteredUOM = UOMDesc.trim()+"("+convFactor+")";
				// This Map contains UOMDesc(ConvFactor) as key and UOM Code as Value. Example {Case(10.00)=CASE, Each(1)=EACH}
				Map UOMDescMap = getUOMDesciptionMap(itemID);
				String UOM = (String) UOMDescMap.get(enteredUOM);
				if(UOM != null && UOM.trim().length()>0)
					return UOM;
			}
		}
		return enteredUOM;
	}

	private Map getUOMDesciptionMap(String itemID) {
		Map<String,String> UOMDescMap = new HashMap<String, String>();
		ArrayList<String> Uoms = new ArrayList<String>();
		Map UOMMap = getUOMlist(itemID);
		Uoms.addAll(UOMMap.keySet());
		for(int i=0;i<Uoms.size();i++) {
			try {
				String uomDesc = XPEDXWCUtils.getUOMDescription(Uoms.get(i));
				UOMDescMap.put(uomDesc+"("+UOMMap.get(Uoms.get(i))+")", Uoms.get(i));
			}
			catch (Exception e) {
				LOG.debug(e.getMessage());
			}
			
		}
		if(UOMDescMap != null)
			return UOMDescMap;
		return null;
	}

	/*
	 * protected ItemValidationResult processGetCompleteItemListResult(String
	 * itemID, Element itemListEl) throws XPathExpressionException {
	 * 
	 * if(itemListEl == null) { ItemValidationResult result = new
	 * ItemValidationResult(); result.setItemID(itemID); result.setValid(true);
	 * } return super.processGetCompleteItemListResult(itemID, itemListEl); }
	 */

	protected Map getUOMlist(String itemID) {
		String uom = null;
		Map<String, String> uoms = null;
		String customerId = wcContext.getCustomerId();
		String organizationCode = wcContext.getStorefrontId();
		uoms = XPEDXOrderUtils.getXpedxUOMList(customerId, itemID,
				organizationCode);
		return uoms;
	}

	protected Document getDocFromOutput(Element outputEl) {
		Node retVal = outputEl;
		if (null != retVal)
			return retVal.getOwnerDocument();
		try {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean validateItemEntitlement(String itemId) {
		boolean res = false;
		try {
			//Populate all the fields
			if(itemId ==null || itemId.trim().length()==0)
				return res;
			Element tmpRes 	= prepareAndInvokeMashup("XPEDXValidateItemEntitlement");
			Element itemEle = getXMLUtils().getChildElement(tmpRes, "Item");
			
			if (itemEle!=null && itemEle.getAttribute("ItemID")!=null && itemEle.getAttribute("ItemID").equals(itemId)){
				res = true;
			}
			
		} catch (Exception e) {
			res = false;
			LOG.error(e.getStackTrace());
		}
		
		return res;
	}

	/*private void setYFSEnvironmentVariables() {
		if (orderHeaderKey != null && !"".equals(orderHeaderKey))
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("CartOrderHeaderKey", orderHeaderKey);
			map.put("isAddItemToCart","true");
			map.put("isPnACall","true");
			map.put("isDiscountCalculate", "true");
			XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext(), map);
		}
	}*/
	
	public String getOrderHeaderKey() {
		return orderHeaderKey;
	}

	public void setOrderHeaderKey(String orderHeaderKey) {
		this.orderHeaderKey = orderHeaderKey;
	}

	public void setEnteredProductIDs(ArrayList enteredIDs) {
		enteredProductIDs = enteredIDs;
	}

	public ArrayList getEnteredProductIDs() {
		return enteredProductIDs;
	}

	public void setEnteredQuantities(ArrayList enteredQuantities) {
		this.enteredQuantities = enteredQuantities;
	}

	public ArrayList getEnteredQuantities() {
		return enteredQuantities;
	}

	public String getProductID() {
		return productID;
	}
	
	public String getMpcId() {
		return mpcId;
	}

	public void setMpcId(String mpcId) {
		this.mpcId = mpcId;
	}

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String[] getAssociationTypesOfInterest() {
		return associationTypesOfInterest;
	}

	public ArrayList getOrderedProductIDs() {
		return orderedProductIDs;
	}

	public ArrayList getOrderedQuantities() {
		return orderedQuantities;
	}

	public ArrayList getOrderedProductUOMs() {
		return orderedProductUOMs;
	}

	public ArrayList getOrderedProductClasses() {
		return orderedProductClasses;
	}

	public List getQuickAddErrorList() {
		return quickAddErrorList;
	}

	public String getQuickAddErrorListSessionKey() {
		if (quickAddErrorListSessionKey == null)
			quickAddErrorListSessionKey = (new StringBuilder()).append(
					"_temp_QuickAddErrorListSessionKey_").append(
					System.currentTimeMillis()).toString();
		return quickAddErrorListSessionKey;
	}

	public ArrayList getOrderedJobIDs() {
		return orderedJobIDs;
	}

	public void setOrderedJobIDs(ArrayList orderedJobIDs) {
		this.orderedJobIDs = orderedJobIDs;
	}

	public ArrayList getEnteredJobIDs() {
		return enteredJobIDs;
	}

	public void setEnteredJobIDs(ArrayList enteredJobIDs) {
		this.enteredJobIDs = enteredJobIDs;
	}

	public ArrayList getEnteredItemTypes() {
		return enteredItemTypes;
	}

	public void setEnteredItemTypes(ArrayList enteredItemTypes) {
		this.enteredItemTypes = enteredItemTypes;
	}

	public ArrayList getOrderedProductDefaultUOMs() {
		return orderedProductDefaultUOMs;
	}

	public void setOrderedProductDefaultUOMs(ArrayList orderedProductDefaultUOMs) {
		this.orderedProductDefaultUOMs = orderedProductDefaultUOMs;
	}

	public JSONObject getProductsJson() {
		return productsJson;
	}

	public void setProductsJson(JSONObject productsJson) {
		this.productsJson = productsJson;
	}

	public String getProdValdAjaxResp() {
		return prodValdAjaxResp;
	}

	public void setProdValdAjaxResp(String prodValdAjaxResp) {
		this.prodValdAjaxResp = prodValdAjaxResp;
	}

	public ArrayList getEnteredSpecialItems() {
		return enteredSpecialItems;
	}

	public void setEnteredSpecialItems(ArrayList enteredSpecialItems) {
		this.enteredSpecialItems = enteredSpecialItems;
	}

	public ArrayList getOrderedLineTypes() {
		return orderedLineTypes;
	}

	public void setOrderedLineTypes(ArrayList orderedLineTypes) {
		this.orderedLineTypes = orderedLineTypes;
	}

	public ArrayList getOrderedTranQuantities() {
		return orderedTranQuantities;
	}

	public void setOrderedTranQuantities(ArrayList orderedTranQuantities) {
		this.orderedTranQuantities = orderedTranQuantities;
	}

	public ArrayList getOrderedProductDescs() {
		return orderedProductDescs;
	}

	public void setOrderedProductDescs(ArrayList orderedProductDescs) {
		this.orderedProductDescs = orderedProductDescs;
	}

	public ArrayList getEnteredProductDescs() {
		return enteredProductDescs;
	}

	public void setEnteredProductDescs(ArrayList enteredProductDescs) {
		this.enteredProductDescs = enteredProductDescs;
	}

	public ArrayList getEnteredUOMs() {
		return enteredUOMs;
	}

	public void setEnteredUOMs(ArrayList enteredUOMs) {
		this.enteredUOMs = enteredUOMs;
	}
	
	public ArrayList getOrderedPONos() {
		return orderedPONos;
	}

	public void setOrderedPONos(ArrayList orderedPONos) {
		this.orderedPONos = orderedPONos;
	}

	public ArrayList getEnteredPONos() {
		return enteredPONos;
	}

	public void setEnteredPONos(ArrayList enteredPONos) {
		this.enteredPONos = enteredPONos;
	}

	public String getIsEditOrder() {
		return isEditOrder;
	}

	public void setIsEditOrder(String isEditOrder) {
		this.isEditOrder = isEditOrder;
	}

	public ArrayList getIsEditNewline() {
		return isEditNewline;
	}

	public void setIsEditNewline(ArrayList isEditNewline) {
		this.isEditNewline = isEditNewline;
	}



	public JSONObject productsJson;
	public String prodValdAjaxResp = null;
	public static final String MASHUP_DO_ADD_ORDER_LINES = "xpedx_me_draftOrderAddOrderLines";
	private static final String QUICK_ADD_ERROR_LIST_SESSION_KEY_PREFIX = "_temp_QuickAddErrorListSessionKey_";
	private static final Logger LOG = Logger
			.getLogger(XPEDXDraftOrderAddOrderLinesAction.class);
	protected HashMap verificationOutputMap;
	protected List quickAddErrorList;
	protected ArrayList orderedProductIDs;
	protected ArrayList orderedJobIDs;
	protected ArrayList orderedPONos;
	protected ArrayList orderedLineTypes;
	protected ArrayList orderedQuantities;
	protected ArrayList orderedTranQuantities;
	protected ArrayList orderedProductDescs;
	protected String currency;
	protected ArrayList orderedProductUOMs;
	protected ArrayList orderedProductDefaultUOMs;
	protected ArrayList orderedProductClasses;
	protected Document changeOrderOutputDoc;
	protected String orderHeaderKey;
	protected ArrayList enteredJobIDs;
	protected ArrayList enteredPONos;
	protected ArrayList enteredProductIDs;
	protected ArrayList enteredQuantities;
	protected ArrayList enteredProductDescs;
	protected ArrayList enteredItemTypes;
	protected ArrayList enteredSpecialItems;
	protected ArrayList enteredUOMs;
	protected String quickAddErrorListSessionKey;
	protected ArrayList errorList;
	protected String isEditOrder="false";
	protected ArrayList isEditNewline;
	public static final String CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ = "changeOrderAPIOutputForOrderLinesModification";
	
	//Adding ArrayList quickAddOrderMultiple FOr Jira 3481
	protected ArrayList quickAddOrderMultiple;
	
	//XBT 282 & 252
	public String draftOrderFlag;
	public String draftOrderError;
	
	public String getDraftOrderError() {
		return draftOrderError;
	}

	public void setDraftOrderError(String draftOrderError) {
		this.draftOrderError = draftOrderError;
	}

	public String getDraftOrderFlag() {
		return draftOrderFlag;
	}

	public void setDraftOrderFlag(String draftOrderFlag) {
		this.draftOrderFlag = draftOrderFlag;
	}


	public ArrayList getQuickAddOrderMultiple() {
		return quickAddOrderMultiple;
	}

	public void setQuickAddOrderMultiple(ArrayList quickAddOrderMultiple) {
		this.quickAddOrderMultiple = quickAddOrderMultiple;
	}

	/**
	 * @return the errorList
	 */
	public ArrayList getErrorList() {
		return errorList;
	}

	/**
	 * @param errorList the errorList to set
	 */
	public void setErrorList(ArrayList errorList) {
		this.errorList = errorList;
	}

	private String productID;
	private String mpcId;
	private String manufacturerId;
	private static String associationTypesOfInterest[] = { "Substitutions" };
	
}
