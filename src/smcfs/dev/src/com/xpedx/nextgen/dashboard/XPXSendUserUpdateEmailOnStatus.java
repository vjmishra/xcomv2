package com.xpedx.nextgen.dashboard;

/**
 * Description: Checks email is required for the order status
 *              
 * @author Stanislaus Joseph John
 */

import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXSendUserUpdateEmailOnStatus implements YIFCustomApi{

	private static YIFApi api = null;
	private static YFCLogCategory log;
	private String getOrderListTemplate = "global/template/api/getOrderList.XPXSendUserUpdateEmailOnStatus.xml";
	private String getcustContactListTemplate = "global/template/api/getCustomerContactList.XPXSendOrderUpdateEmail.xml";
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException yifEx) {
			yifEx.printStackTrace();
		}
	}
	
	public Document selectEmailTemplate(YFSEnvironment env,Document inXML) throws Exception {
						
		String orderStatus = "";
		String fOrderHeaderKey = "";
		String custContactID = "";	
		String emailRequired = "Y";
		String emailTemplatepath = "";
		String subject = "";	
		String brand = "";
		String orderNo = "";
		String custPONo = "";		
		String legacyOrdNo = "";
		String generationNo = "";
		String division = "";
		String extnOrderShipEmailFlag = "";
		String extnOrderCancelEmailFlag = "";
		String extnBackOrderEmailFlag = "";
		String orderType = "";
		Document custContactOutputDoc = null;
		Document fOrderOutputDoc = null;
		Element fOrderElement = null;
		
		if(inXML != null){
			log.debug("XPXSendUserUpdateEmailOnStatus_InXML:" + SCXmlUtil.getString(inXML));
		}
		
		Element rootElement = inXML.getDocumentElement();		
		if((rootElement.getOwnerDocument().getDocumentElement().getNodeName()).equalsIgnoreCase("OrderStatusChange")) {
			// For change order status.
			orderStatus = rootElement.getAttribute("BaseDropStatus");	
		} else if((rootElement.getOwnerDocument().getDocumentElement().getNodeName()).equalsIgnoreCase("Order")) {
			// For change order on cancellation.
			orderType = rootElement.getAttribute("OrderType");
			// To check whether all the quantities in the line has been cancelled.
			String maxOrderStatus = rootElement.getAttribute("MaxOrderStatus");
			String minOrderStatus = rootElement.getAttribute("MinOrderStatus");
			if(!orderType.equalsIgnoreCase("Customer") && minOrderStatus.equals(XPXLiterals.ORDER_CANCELLED_STATUS) 
					&& maxOrderStatus.equals(XPXLiterals.ORDER_CANCELLED_STATUS)) {
				// Order cancelled status is '9000'
				orderStatus = XPXLiterals.ORDER_CANCELLED_STATUS;
			}									
		} else {
			log.debug("Unknown Document Received To Send Email.");
			emailRequired = "N";
		}
		
		if(log.isDebugEnabled()){
			log.debug("");
			log.debug("Email Request For Order Status:" + orderStatus);
		}
				
		if(orderStatus != null && orderStatus.equalsIgnoreCase(XPXLiterals.ORDER_REJECTED_STATUS)) { 
			
			/* 
			 * This code has been commented as email functionality for the below status is not part of Phase I implementation. 
			 * Uncomment it for Phase II so that email will be sent for the below status.
			 **/
			//	|| orderStatus.equalsIgnoreCase(XPXLiterals.ORDER_SHIPPED_STATUS) 
			//		|| orderStatus.equalsIgnoreCase(XPXLiterals.ORDER_BACKORDERED_STATUS) 
			//			|| orderStatus.equalsIgnoreCase(XPXLiterals.ORDER_CANCELLED_STATUS)
			
			// To get the fulfillment order details that needs to be sent in email.	
			fOrderHeaderKey = rootElement.getAttribute("OrderHeaderKey");
			if(!YFCObject.isNull(fOrderHeaderKey) && !YFCObject.isVoid(fOrderHeaderKey)) {
				log.debug("Fulfillment Order Header Key: " + fOrderHeaderKey);
			}
			// To build input doc to call getOrderList API.
			Document fOrderInputDoc = YFCDocument.createDocument("Order").getDocument();
			fOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, fOrderHeaderKey);		
			// To set API template
			env.setApiTemplate("getOrderList", getOrderListTemplate);
			
			fOrderOutputDoc = api.invoke(env,"getOrderList", fOrderInputDoc);
			if(fOrderOutputDoc != null){
				log.debug("XPXSendUserUpdateEmailOnStatus_FulfillmentOrder: " + SCXmlUtil.getString(fOrderOutputDoc));
			}
			
			// To clear API template for getOrderList
			env.clearApiTemplate("getOrderList");
			
			NodeList orderList = fOrderOutputDoc.getElementsByTagName("Order");
			if(orderList != null && orderList.getLength() > 0) {
						
				fOrderElement = (Element)orderList.item(0);		
				// To get the details to prepare the subject for the email.
				custPONo = fOrderElement.getAttribute("CustomerPONo");
				brand = fOrderElement.getAttribute("EnterpriseCode");
				NodeList extnList = fOrderElement.getElementsByTagName("Extn");
				if(extnList != null && extnList.getLength() > 0){
					Element orderExtnElement = (Element)extnList.item(0);
					legacyOrdNo = orderExtnElement.getAttribute("ExtnLegacyOrderNo");
					generationNo = orderExtnElement.getAttribute("ExtnGenerationNo");
					division = orderExtnElement.getAttribute("ExtnCustomerDivision"); 
					// Order No format : CustomerDivision - Legacy Order No - Generation No
					if(!YFCObject.isNull(legacyOrdNo) && !YFCObject.isVoid(legacyOrdNo) && !YFCObject.isNull(generationNo) && !YFCObject.isVoid(generationNo)){
						orderNo = division + "-" + legacyOrdNo + "-" + generationNo;
					}
				}
									 				
				if(fOrderElement.hasAttribute("CustomerContactID")){
					custContactID = fOrderElement.getAttribute("CustomerContactID");					
				}
					
				// To check email needs to be sent based on the flags set in customer contact table.			
				if(!YFCObject.isNull(custContactID) && !YFCObject.isVoid(custContactID)){
					
					if (log.isDebugEnabled()) {
						log.debug("CustomerContactID: " + custContactID);
						log.debug("OrderNo: " + orderNo);
					}
					
					// To set the template for getCustomerContactList API.
					env.setApiTemplate("getCustomerContactList", getcustContactListTemplate);
					
					/* Input Doc Schema : <CustomerContact CustomerContactID="" /> */
					Document custContactInputDoc = YFCDocument.createDocument("CustomerContact").getDocument();
					custContactInputDoc.getDocumentElement().setAttribute("CustomerContactID", custContactID);
					if (log.isDebugEnabled()) {
						log.debug("getCustomerContactList_InXML: " + SCXmlUtil.getString(custContactInputDoc));
					}
					custContactOutputDoc = api.invoke(env,"getCustomerContactList", custContactInputDoc);
					// To clear API template.
					env.clearApiTemplate("getCustomerContactList");
					if(custContactOutputDoc != null && log.isDebugEnabled()){
						log.debug("getCustomerContactList_OutXML: " + SCXmlUtil.getString(custContactOutputDoc));
					}
					
					NodeList extnNodeList = custContactOutputDoc.getElementsByTagName("Extn");
					if(extnNodeList != null && extnNodeList.getLength() > 0){
						Element extnElement = (Element) extnNodeList.item(0);
						if(extnElement != null && extnElement.hasAttribute("ExtnOrderShipEmailFlag")){
						extnOrderShipEmailFlag = extnElement.getAttribute("ExtnOrderShipEmailFlag");
						}
						if(extnElement != null && extnElement.hasAttribute("ExtnOrderCancelEmailFlag")){
						extnOrderCancelEmailFlag = extnElement.getAttribute("ExtnOrderCancelEmailFlag");
						}
						if(extnElement != null && extnElement.hasAttribute("ExtnBackOrderEmailFlag")){
						extnBackOrderEmailFlag = extnElement.getAttribute("ExtnBackOrderEmailFlag");
						}
					}	
				} else {
					log.debug("Email can't be sent as MSAP Customer Contact ID doesn't exist.");
					emailRequired = "N";
				}
				
				// To check email required and set the email templates based on order status.
				if(orderStatus.equalsIgnoreCase(XPXLiterals.ORDER_SHIPPED_STATUS) && !extnOrderShipEmailFlag.equalsIgnoreCase("N")) {
					emailTemplatepath = "/global/template/email/XPXOrderShipped.xsl";
					subject = brand + " Order Shipped: PO " + custPONo +" Order " + orderNo;
				} else if(orderStatus.equalsIgnoreCase(XPXLiterals.ORDER_REJECTED_STATUS)) {
					emailTemplatepath = "/global/template/email/XPXOrderRejected.xsl";	
					subject = brand + ".com  Order Rejected: PO " + custPONo +" Order " + orderNo;
				} else if(orderStatus.equalsIgnoreCase(XPXLiterals.ORDER_BACKORDERED_STATUS) && !extnBackOrderEmailFlag.equalsIgnoreCase("N")) {
					emailTemplatepath = "/global/template/email/XPXBackOrdered.xsl";
					subject = brand + " Back Order Confirmation: PO " + custPONo +" Order " + orderNo;
				} else if(orderStatus.equalsIgnoreCase(XPXLiterals.ORDER_CANCELLED_STATUS) && !extnOrderCancelEmailFlag.equalsIgnoreCase("N")) {
					emailTemplatepath = "/global/template/email/XPXOrderCancelled.xsl";
					subject = brand + " Order Cancelled: PO " + custPONo +" Order " + orderNo;
				} else {
					log.debug(" Email Not Required.");
					emailRequired = "N";
				}	
				
				// To find the customer email address.
				if(emailRequired.equalsIgnoreCase("Y") && custContactOutputDoc.getDocumentElement().hasChildNodes()){			
					Document customerEmailDoc = getCustomerEmailIDsDoc(env,custContactOutputDoc);
					fOrderElement.setAttribute("CCEmailAddress", customerEmailDoc.getDocumentElement().getAttribute("cEmailAddress"));
					fOrderElement.setAttribute("ToEmailAddress", customerEmailDoc.getDocumentElement().getAttribute("strToEmailAddress"));
				}			
				
				fOrderElement.setAttribute("EmailTemplatepath", emailTemplatepath);
				fOrderElement.setAttribute("Subject", subject);
				
				// To set the Order no in the format requested by customer.
				fOrderElement.setAttribute("OrderNo", orderNo);
				
				// To get the full path of organization logo.
				Element orderElement = (Element) fOrderOutputDoc.getElementsByTagName("Order").item(0);
				Document inDoc = YFCDocument.createDocument("Order").getDocument();
				inDoc.getDocumentElement().setAttribute("SellerOrganizationCode", orderElement.getAttribute("SellerOrganizationCode"));
				XPXUtils xpxUtils = new XPXUtils();
				Document stampLogoDoc = xpxUtils.stampBrandLogo(env, inDoc);
				if(stampLogoDoc.getDocumentElement().hasAttribute("BrandLogo")){					
					fOrderElement.setAttribute("BrandLogo", stampLogoDoc.getDocumentElement().getAttribute("BrandLogo"));
				}
			} else {
				emailRequired = "N";
			}
		} else {
			log.debug("Email not required for the Order or Order Status.");
			emailRequired = "N";
		}
		
		// Document created to stamp the flag email not required.
		if(fOrderOutputDoc == null){
			fOrderOutputDoc = YFCDocument.createDocument("OrderList").getDocument();					
		} 				
		
		fOrderOutputDoc.getDocumentElement().setAttribute("EmailRequired", emailRequired);
		if(log.isDebugEnabled()) {
			log.debug("");
			log.debug("EmailRequired:" + emailRequired);
			log.debug("XPXSendUserUpdateEmailOnStatus_OutXML:" + SCXmlUtil.getString(fOrderOutputDoc));
		}
		return fOrderOutputDoc;
	}
		
	public Document getCustomerEmailIDsDoc(YFSEnvironment env,Document custContactOutputDoc) throws Exception
	{				
		String strcEmailAddress = "";
		String strToEmailAddress = "";
		String strUsergroupKey = "";
		String BUYER_ADMIN = "BUYER-ADMIN";
		String bAdminEmailAdr = "";
		Element tempCustomerContactElem = null;
		NodeList userGroupList = null;		
		Element tempUserGroupList = null;
		
		// To get the Customer email address.
		Element hdrCustomerContactElem = (Element) custContactOutputDoc.getElementsByTagName("CustomerContact").item(0);		
		strToEmailAddress = hdrCustomerContactElem.getAttribute("EmailID");
		
		NodeList CustomerContactList = custContactOutputDoc.getDocumentElement().getElementsByTagName("CustomerContact");		
		int customerContactLen = CustomerContactList.getLength();				
	
		for(int i=0;i<customerContactLen;i++)
		{
			tempCustomerContactElem = (Element) CustomerContactList.item(i);
			
			userGroupList = tempCustomerContactElem.getElementsByTagName("UserGroupList");
			int userGroupListLen = userGroupList.getLength();			
			for(int j=0;j<userGroupListLen;j++){
				tempUserGroupList = (Element) userGroupList.item(j);
				
				strUsergroupKey = tempUserGroupList.getAttribute("UsergroupKey");
				
				if(strUsergroupKey.equalsIgnoreCase(BUYER_ADMIN)){
					bAdminEmailAdr = tempCustomerContactElem.getAttribute("EmailID");
					if(strcEmailAddress.isEmpty()){					
						strcEmailAddress =   bAdminEmailAdr;
					}else{
						strcEmailAddress = strcEmailAddress + "," + bAdminEmailAdr;
					}					
				}
			}	
		}
		if(!YFCObject.isNull(strcEmailAddress) && !YFCObject.isVoid(strcEmailAddress)) {
			log.debug(" String email address : strcEmailAddress is " + strcEmailAddress);
		}
		if(!YFCObject.isNull(strToEmailAddress) && !YFCObject.isVoid(strToEmailAddress)) {
			log.debug(" String to email address is : strToEmailAddress is " + strToEmailAddress);
		}

		Document customerEmailDoc = YFCDocument.createDocument("Customer").getDocument();		
		customerEmailDoc.getDocumentElement().setAttribute("cEmailAddress", strcEmailAddress);
		customerEmailDoc.getDocumentElement().setAttribute("strToEmailAddress", strToEmailAddress);

		return customerEmailDoc;	
	}
	
	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
