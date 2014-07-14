package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;

public class XPEDXPendingApprovalCountAction extends WCMashupAction {

	public XPEDXPendingApprovalCountAction() {
		
		outputDoc = null;
		
		resourceId = "";
		oUserList = new ArrayList();
		nameExp = new ArrayList();
		isApprover = false;
		approvalHoldType = "";
		userList = new ArrayList();
		userNameExp = new ArrayList();
		
	}
	
	public String execute() {
		
//		approvalHoldType = BusinessRuleUtil.getBusinessRule(
//				"HOLD_TO_BE_APPLIED_FOR_ORDER_APPROVAL", wcContext);
		approvalHoldType = XPEDXConstants.HOLD_TYPE_FOR_PENDING_APPROVAL;
		Document outDoc = null;
		outDoc = null;
		try{
			outDoc = prepareAndInvokeMashup("userListForApproval").getOwnerDocument();
			if (outDoc == null)
				return "error";
			if (outDoc != null)
				setOutputForCustomerContactList(outDoc);
			
			isApprover = true;
			outDoc = null;
			outDoc = prepareAndInvokeMashup("xpedxPendingApprovalCount").getOwnerDocument();
			//outEle = (Element)prepareAndInvokeMashup("xpedxPendingApprovalCount");
		}catch (Exception ex) {
			log.error(ex);
		}	
		 
		if (outDoc != null) {
			try {
				setOutputDocument(outDoc);
			} catch (Exception e) {
				log.debug("XPathExpressionException", e);
			}
			return "success";
		} else {
			return "error";
		}
		
	}
	
	private void setOutputForCustomerContactList(Document outDoc) {
		Element customerContactList = outDoc.getDocumentElement();
		ArrayList nodeCustomerList = SCXmlUtils.getChildren(
				customerContactList, "CustomerContact");
		Iterator iter = nodeCustomerList.iterator();
		do {
			if (!iter.hasNext())
				break;
			Element oNode = (Element) iter.next();
			String proxyApprover = oNode.getAttribute("ApproverProxyUserId");
			if (wcContext.getCustomerContactId().equals(proxyApprover)) {
				oUserList.add(oNode.getAttribute("CustomerContactID"));
				nameExp.add("ResolverUserId");
			}
		} while (true);
		oUserList.add(wcContext.getCustomerContactId());
		nameExp.add("ResolverUserId");
	}
	
	public Document getOutputDocument() {
		return outputDoc;
	}

	public void setOutputDocument(Document outputDocument) {
		outputDoc = outputDocument;
	}
	
	public List getOUserList() {
		return oUserList;
	}

	public List getNameExp() {
		return nameExp;
	}


	public boolean getIsApprover() {
		return isApprover;
	}

	public void setIsApprover(boolean isApprover) {
		this.isApprover = isApprover;
	}

	public String getApprovalHoldType() {
		return approvalHoldType;
	}

	public void setApprovalHoldType(String approvalHoldType) {
		this.approvalHoldType = approvalHoldType;
	}
	
	public List getUserList() {
		userList.add(wcContext.getCustomerContactId());
		return userList;
	}

	public List getUserNameExp() {
		userNameExp.add("ApproverProxyUserId");
		return userNameExp;
	}
	


	private static final Logger log = Logger.getLogger(XPEDXPendingApprovalCountAction.class);
	protected Document outputDoc;

	
	public List oUserList;
	public List nameExp;
	private String approvalHoldType;
	private boolean isApprover;
	public List userList;
	public List userNameExp;
	
	
	
}
