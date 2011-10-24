package com.sterlingcommerce.xpedx.webchannel.order;

import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;

import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.util.YFCCommon;

/**
 * XPEDXDraftOrderListUserInfoAction class fetches the user info needed by the draft order list use case
 *
 */
public class XPEDXDraftOrderListUserInfoAction extends WCMashupAction {

	private static final long serialVersionUID = 4617643776290992722L;
	private String customerContactId = null;

	public String execute() {
		try{
			getUserInfo();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return "error";
		}
		return "success";       
	}

	private void getUserInfo() {
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		String storeFrontId = context.getStorefrontId();
		String custContactId = getCustomerContactId();
		
		Document custOutDoc = XPEDXWCUtils.getUserInfo(custContactId,storeFrontId).getOwnerDocument();
		YFCDocument custDoc = YFCDocument.getDocumentFor(custOutDoc);
		YFCElement custContactListEle = custDoc.getDocumentElement();
		YFCIterable<YFCElement> custContactsEle = custContactListEle.getChildren("CustomerContact");
		YFCElement custContactEle = null;
		String name = "";
		for(;custContactsEle.hasNext();){
			custContactEle = (YFCElement)custContactsEle.next();
			String contactId = custContactEle.getAttribute("CustomerContactID");
			if (!contactId.equals(custContactId)){
				continue;
			}
			else{
				String lastName = custContactEle.getAttribute("LastName");
				String firstName = custContactEle.getAttribute("FirstName");
				
				if (!YFCCommon.isVoid(lastName)){
					name = lastName;
				}
				if (!YFCCommon.isVoid(firstName)){
					if (!YFCCommon.isVoid(lastName)){
//						name =  " "+name;
						name =  name+ " , ";
					}
//					name = firstName + name ;
					name =  name + firstName;
				}
				break;
			}
		}
		context.setWCAttribute("USER_NAME", name, WCAttributeScope.REQUEST);		
	}
	
	public String getCustomerContactId() {
		return customerContactId;
	}

	public void setCustomerContactId(String customerContactId) {
		this.customerContactId = customerContactId;
	}

}
