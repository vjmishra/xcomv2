package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.Map;

import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.order.DraftOrderCopyAction;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXDraftOrderCopyAction extends DraftOrderCopyAction {
	private String copyCartName;
	private String copyCartDescription;
	private String newOrderHeaderKey;
	public String draftFlagError="draftFlagError";
	public String execute(){
		 try {

				//Remove itemMap from Session, when cart change in context,  For Minicart Jira 3481
				XPEDXWCUtils.removeObectFromCache("itemMap");
	            Element orderOutput = prepareAndInvokeMashup(MASHUP_GET_ORDER_NAME);
	            String copyOrderID = orderOutput.getAttribute("OrderNo");
	            String draftOrderFlag = orderOutput.getAttribute("DraftOrderFlag");
	            String copyText = getText("CopyOfPrefix");
	            String[] newCartName = {copyText, copyOrderID};
	            setOrderName(getText("CopyOfCartName", newCartName));
	            String editedOrderHeaderKey = XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
				if(YFCCommon.isVoid(editedOrderHeaderKey) && "N".equals(draftOrderFlag)){
					
					return draftFlagError;	
				}
	            orderOutput = prepareAndInvokeMashup(MASHUP_COPY_ORDER);
	            newOrderHeaderKey=orderOutput.getAttribute("OrderHeaderKey");
	            CommerceContextHelper.flushCartInContextCache(getWCContext());

	        }catch(Exception ex){
	            log.error(ex);
	            return ERROR;
	        }
	        return SUCCESS; 
	}

	@Override
	protected void manipulateMashupInputs(Map<String, Element> mashupInputs)
			throws CannotBuildInputException {
		Element orderElem = mashupInputs.get(MASHUP_COPY_ORDER);
		if(orderElem != null )
		{
			String billToCustomerId = XPEDXWCUtils
					.getLoggedInCustomerFromSession(wcContext);
			String customerId = (billToCustomerId != null && billToCustomerId
								.trim().length() > 0) ? billToCustomerId : wcContext.getCustomerId();
			orderElem.setAttribute("BillToID", customerId);
			if(copyCartName!=null && copyCartName.trim().length()>0){
				orderElem.setAttribute("OrderName",copyCartName);
			}
			
			Element extnOrderElem=null;
			if(copyCartDescription!=null && copyCartDescription.trim().length()>0) {
				extnOrderElem = orderElem.getOwnerDocument().createElement("Extn");
				extnOrderElem.setAttribute("ExtnOrderDesc", copyCartDescription);				
			}
			
			XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			if(shipToCustomer!=null) {				
				if(extnOrderElem==null) {
					extnOrderElem = orderElem.getOwnerDocument().createElement("Extn");
				}
				extnOrderElem.setAttribute("ExtnShipToName", shipToCustomer.getExtnCustomerName());
				XPEDXShipToCustomer billToCustomer=shipToCustomer.getBillTo();
				if(billToCustomer != null)	{
					extnOrderElem.setAttribute("ExtnBillToName", billToCustomer.getExtnCustomerName());
				}
			}
			
			if(extnOrderElem!=null) {
				orderElem.appendChild(extnOrderElem);
			}
		}
		super.manipulateMashupInputs(mashupInputs);
	}

	public String getCopyCartDescription() {
		return copyCartDescription;
	}

	public void setCopyCartDescription(String copyCartDescription) {
		this.copyCartDescription = copyCartDescription;
	}

	public String getCopyCartName() {
		return copyCartName;
	}

	public void setCopyCartName(String copyCartName) {
		this.copyCartName = copyCartName;
	}

	public String getNewOrderHeaderKey() {
		return newOrderHeaderKey;
	}

	public void setNewOrderHeaderKey(String newOrderHeaderKey) {
		this.newOrderHeaderKey = newOrderHeaderKey;
	}

}
