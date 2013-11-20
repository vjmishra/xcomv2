package com.sterlingcommerce.xpedx.webchannel.order;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.webchannel.order.OrderSaveBaseAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXUpdateMiniCartAction extends OrderSaveBaseAction{
	
	 private static final Logger LOG = Logger.getLogger(XPEDXUpdateMiniCartAction.class);
	 protected String cicRefreshingMashupOrderHeaderKey = "";
	 private static final String UPDATE_MINI_CART_MASHUP="updateOrderForMiniCart";
	 private static final String DELETE_MINI_CART_MASHUP="deleteLineForMiniCart";
	 private static final String CHANGE_ORDEROUTPUT_CHECKOUT_SESSION_OBJ = "changeOrderAPIOutputForCheckout";
	 public static final String CART_ERROR = "CartError";
	 private static final String CHECKOUT_MINI_CART_MASHUP="checkoutOrderForMiniCart";
	 public ArrayList<String> allItemID = new ArrayList<String>();//Added for Jira 3523
	 XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean;
	 //XBT- 248 & 252
	 public String draftOrderFlag;
	 public String draftOrderError;
	 public String draftOrderFailure="draftOrderFailure";

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
	//End of XBT- 248 & 252
	public String execute()
	 {
		XPEDXWCUtils.setYFSEnvironmentVariables(wcContext);
		 String returnValue = ERROR;
		 Element orderLelement=null;
		 xpedxCustomerContactInfoBean=(XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
		//start of XBT 252 & 248
			String editedOrderHeaderKey = XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
			if(YFCCommon.isVoid(editedOrderHeaderKey)){
				draftOrderFlag="Y";	
			}
			else {
				draftOrderFlag="N";	
			}
			//end of XBT 252 & 248   
	        try
	        {     	
	        	Map<String, Element> outputMap=prepareAndInvokeMashups();
	        	if(getMashupIds().contains(CHECKOUT_MINI_CART_MASHUP))
	        	{
	        		orderLelement=outputMap.get(CHECKOUT_MINI_CART_MASHUP);
	        		
	        	//Added for Jira 3523
	        		ArrayList<Element> itemList =SCXmlUtil.getElements(orderLelement,"OrderLines/OrderLine");
	        		Iterator<Element> it=itemList.iterator();
	        		while(it.hasNext())
	        		{
	        			Element orderLineElem=it.next();						
	        			Element itemElement = (Element)orderLineElem.getElementsByTagName("Item").item(0);
	        			String itemId = itemElement.getAttribute("ItemID");
        				//Element lineType=(Element)orderLineElem.getElementsByTagName("OrderLine").item(0);
        				String linetype = orderLineElem.getAttribute("LineType");
        				//For Charge line, we will not be checking for entitlements.
        				if(!"M".equals(linetype) &&  !"C".equals(linetype) && !allItemID.contains(itemId))
        				{        				
        					allItemID.add(itemId);
        				}
	        		}        		
	        		boolean entitleCheck = XPEDXOrderUtils.checkforNonEntitlement(allItemID , wcContext);
	        		// If there is even a single item in mini cart which is non entitled, we will show error and not allow user to checkout.
					if(entitleCheck){
	        			XPEDXOrderUtils.refreshMiniCart(wcContext, orderLelement, true, XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
	        			XPEDXWCUtils.releaseEnv(wcContext);
	        			return CART_ERROR;
					}
        		//End of Jira 3523
	        		//check for cart errors to redirect it to cart page instead of checkout
	        		boolean minOrderError = checkMinOrderFee(orderLelement);
	        		if(minOrderError){
	        			XPEDXOrderUtils.refreshMiniCart(wcContext, orderLelement, true, XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
	        			XPEDXWCUtils.releaseEnv(wcContext);
	        			return CART_ERROR;
	        		}
	        		boolean maxOrderError = checkMaxOrderFee(orderLelement);
	        		if(maxOrderError){
	        			XPEDXOrderUtils.refreshMiniCart(wcContext, orderLelement, true, XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
	        			XPEDXWCUtils.releaseEnv(wcContext);
	        			return CART_ERROR;
	        		}
	        		
	        		getWCContext().getSCUIContext().getSession().setAttribute(CHANGE_ORDEROUTPUT_CHECKOUT_SESSION_OBJ, orderLelement.getOwnerDocument());
	        		
	        	}
	        	else if(getMashupIds().contains(UPDATE_MINI_CART_MASHUP))	        		
	        	{
	        		orderLelement=outputMap.get(UPDATE_MINI_CART_MASHUP);
	        		
	        		//check for cart errors to redirect it to cart page instead of checkout
	        		boolean minOrderError = checkMinOrderFee(orderLelement);
	        		if(minOrderError){
	        			XPEDXOrderUtils.refreshMiniCart(wcContext, orderLelement, true, XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
	        			XPEDXWCUtils.releaseEnv(wcContext);
	        			return CART_ERROR;
	        		}	        		
	        		boolean maxOrderError = checkMaxOrderFee(orderLelement);
	        		if(maxOrderError){
	        			XPEDXOrderUtils.refreshMiniCart(wcContext, orderLelement, true, XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
	        			XPEDXWCUtils.releaseEnv(wcContext);
	        			return CART_ERROR;
	        		}
	        	}
	        	else if(getMashupIds().contains(DELETE_MINI_CART_MASHUP))
	        	{
	        		orderLelement=outputMap.get(DELETE_MINI_CART_MASHUP);
	        	}
	        	
	        	XPEDXOrderUtils.refreshMiniCart(wcContext, orderLelement, true, XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
	            returnValue=SUCCESS;
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
                                    draftOrderError = "true";
                                    break;
                              }
                        }
                  }
                  return draftOrderFailure;
            }

	        catch(Exception e)
	        {
	        	
	        	e.printStackTrace();
	            LOG.error(e);
	            return ERROR;
	        }
	        XPEDXWCUtils.releaseEnv(wcContext);
	        return returnValue;
	}
	 
	 private boolean checkMaxOrderFee(Element orderLelement){
		 boolean maxOrderError = false;
		 String maxOrderAmountStr=xpedxCustomerContactInfoBean.getExtnmaxOrderAmount();
		 float maxOrderAmt = 0;
		 float orderTotal = 0;
		 if(maxOrderAmountStr != null  && !"".equals(maxOrderAmountStr.trim()) &&
					Float.parseFloat(maxOrderAmountStr)>0)
			 maxOrderAmt = Float.parseFloat(maxOrderAmountStr);
		 if(maxOrderAmt==0)
		 {
			 XPEDXShipToCustomer billToElement = shipToCustomer.getBillTo();
				if(billToElement != null )
				{
					maxOrderAmountStr=billToElement.getExtnMaxOrderAmount();
					if(maxOrderAmountStr != null && (!("".equals(maxOrderAmountStr.trim()))) &&
							Float.parseFloat(maxOrderAmountStr)>0)
					{
						maxOrderAmt = Float.parseFloat(maxOrderAmountStr);
					}
				}
		 }
		 if(orderLelement != null){
				//find the order total without tax
				String orderTotalStr = SCXmlUtil.getXpathAttribute(orderLelement, "//Order/Extn/@ExtnTotOrdValWithoutTaxes"); 
				
				if (orderTotalStr != null && (!"".equals(orderTotalStr))) {
					orderTotal = Float.parseFloat(orderTotalStr);
				}
				
		}
		if(maxOrderAmt != 0 && maxOrderAmt < orderTotal){
				maxOrderError = true;
			}
		 return maxOrderError;
	 }
	
	private boolean checkMinOrderFee(Element orderLelement) throws RemoteException {
		boolean minOrderError = false;
		//if the order total is less than min order, then redirect to cart page so that the user can correct it.
		getCustomerDetails();
		if(orderLelement != null){
			//find the order total without tax
			String orderTotalStr = SCXmlUtil.getXpathAttribute(orderLelement, "//Order/Extn/@ExtnTotOrdValWithoutTaxes"); 
			float orderTotal = 0;
			if (orderTotalStr != null && (!"".equals(orderTotalStr))) {
				orderTotal = Float.parseFloat(orderTotalStr);
			}
			if(minOrderAmount > orderTotal){
				minOrderError = true;
			}
		}
		return minOrderError;
	}
	
	
	public void setOrderHeaderKey(String orderHeaderKey)
    {
        cicRefreshingMashupOrderHeaderKey = orderHeaderKey;
    }
	
	private void getCustomerDetails() throws RemoteException {
		ISCUITransactionContext scuiTransactionContext = null;
		try {
			shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			if (wcContext.getCustomerId() != null) {
				if (shipToCustomer == null) {
					LOG.error("shipToCustomer object from session is null... Creating the Object and Putting it in the session");
					XPEDXWCUtils.setCustomerObjectInCache(XPEDXWCUtils.getCustomerDetails(getWCContext().getCustomerId(),getWCContext().getStorefrontId()).getDocumentElement());
					shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
				}
				String minOrderAmountStr = shipToCustomer.getExtnMinOrderAmount();
				String chargeAmountStr = shipToCustomer.getExtnMinChargeAmount();
				
				if (minOrderAmountStr != null && (!("".equals(minOrderAmountStr))) && (!"0".equals(minOrderAmountStr)) && (!"0.00".equals(minOrderAmountStr))) {
					minOrderAmount = Float.parseFloat(minOrderAmountStr);
					if (chargeAmountStr != null && (!"".equals(chargeAmountStr))) {
						chargeAmount = Float.parseFloat(chargeAmountStr);
					}
				} else {
					if (chargeAmountStr != null && (!"".equals(chargeAmountStr))) {
						chargeAmount = Float.parseFloat(chargeAmountStr);
					}
					XPEDXShipToCustomer parentElement = shipToCustomer .getBillTo();
					if (parentElement != null) {
						minOrderAmountStr = parentElement.getExtnMinOrderAmount();
						chargeAmountStr = parentElement.getExtnMinChargeAmount();
						if (minOrderAmountStr != null && (!("".equals(minOrderAmountStr))) && (!"0".equals(minOrderAmountStr)) && (!"0.00".equals(minOrderAmountStr))) {
							minOrderAmount = Float.parseFloat(minOrderAmountStr);
							if (chargeAmount <= 0 && chargeAmountStr != null && (!"".equals(chargeAmountStr))) {
								chargeAmount = Float.parseFloat(chargeAmountStr);
							}
						} else {
							if (chargeAmountStr != null && (!"".equals(chargeAmountStr)) && chargeAmount <= 0) {
								chargeAmount = Float .parseFloat(chargeAmountStr);
							}
							if (shipToCustomer.getShipToOrgExtnMinOrderAmt() == null 
									&& shipToCustomer .getShipToOrgExtnSmallOrderFee() == null 
									&& shipToCustomer.getShipToOrgOrganizationName() == null
									&& shipToCustomer.getShipToOrgCorporatePersonInfoState() == null) {
								shipFromDivision = shipToCustomer.getExtnShipFromBranch();
								String envCode = shipToCustomer.getExtnEnvironmentCode();
								Map<String, String> valueMap = new HashMap<String, String>();
								if (envCode != null && envCode.trim().length() > 0) {
									valueMap.put("/Organization/@OrganizationCode",shipFromDivision + "_" + envCode);
								} else {
									LOG.error("EnvCode is NULL. Returning back to the caller.");
									return;
								}
								try {
									Element input = WCMashupHelper.getMashupInput("XPEDXGetShipOrgNodeDetails",valueMap, getWCContext().getSCUIContext());
									Object obj = WCMashupHelper.invokeMashup("XPEDXGetShipOrgNodeDetails",input, getWCContext().getSCUIContext());
									Document outputDoc = ((Element) obj).getOwnerDocument();

									if (YFCCommon.isVoid(outputDoc)) {
										LOG.error("No DB record exist for Node "+ shipFromDivision+ "_" + envCode + ". ");
										return;
									}
									shipToCustomer.setShipToOrgExtnMinOrderAmt(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(),"/OrganizationList/Organization/Extn/@ExtnMinOrderAmt"));
									shipToCustomer.setShipToOrgExtnSmallOrderFee(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(),"/OrganizationList/Organization/Extn/@ExtnSmallOrderFee"));
									shipToCustomer.setShipToOrgOrganizationName(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(),"/OrganizationList/Organization/@OrganizationName"));
									shipToCustomer.setShipToOrgCorporatePersonInfoState(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(),"/OrganizationList/Organization/CorporatePersonInfo/@State"));
									shipToCustomer.setShipToDivDeliveryCutOffTime(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(),"/OrganizationList/Organization/Extn/@ExtnDeliveryCutOffTime"));
									//Added For Jira 3465
									shipToCustomer.setShipToDivdeliveryInfo(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(),"/OrganizationList/Organization/Extn/@ExtnDeliveryInfo"));
									shipToCustomer.setShipToDivdeliveryInfoSaal(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(),"/OrganizationList/Organization/Extn/@ExtnDeliveryInfoSaal"));//EB-3624
									
									XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER,shipToCustomer);
								} catch (CannotBuildInputException e) {
									LOG.error("Unable to get XPEDXGetShipOrgNodeDetails for "+ shipFromDivision + "_"+ envCode + ". ", e);
									return;
								}
							}
							minOrderAmountStr = shipToCustomer.getShipToOrgExtnMinOrderAmt();
							chargeAmountStr = shipToCustomer.getShipToOrgExtnSmallOrderFee();
							if (minOrderAmountStr != null && (!("".equals(minOrderAmountStr)))&& (!"0".equals(minOrderAmountStr))&& (!"0.00".equals(minOrderAmountStr))) {
								minOrderAmount = Float.parseFloat(minOrderAmountStr);
								if (chargeAmount <= 0 && chargeAmountStr != null && (!"".equals(chargeAmountStr))) {
									chargeAmount = Float.parseFloat(chargeAmountStr);
								}
							}
						}
					}
				}
			}// if customerId is not null
		} catch (Exception ex) {
			scuiTransactionContext.rollback();
		} finally {
			if (scuiTransactionContext != null) {SCUITransactionContextHelper.releaseTransactionContext(scuiTransactionContext, wcContext.getSCUIContext());
			}
		}

	}
	
	protected XPEDXShipToCustomer shipToCustomer;	
	private float chargeAmount;
	private float minOrderAmount;
	protected String shipFromDivision;
}
