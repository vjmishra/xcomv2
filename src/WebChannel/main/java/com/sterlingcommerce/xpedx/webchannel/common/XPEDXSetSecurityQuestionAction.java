package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.ui.backend.util.Util;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

public class XPEDXSetSecurityQuestionAction extends WCMashupAction {
	private static final String GET_ORG_QUESTION_LIST_MASHUP = "getOrgSecretQuestionList";
	private Map<String, String> questionListForOrg = new LinkedHashMap<String, String>();
	private String secretAnswer;
	private String confirmAnswer;
	private String secretQuestion;
	private String organizationCode; 
	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getSecretQuestion() {
		return secretQuestion;
	}

	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}

	public String getSecretAnswer() {
		return secretAnswer;
	}

	public void setSecretAnswer(String secretAnswer) {
		this.secretAnswer = secretAnswer;
	}
	public void setConfirmAnswer(String confirmAnswer) {
		this.confirmAnswer = confirmAnswer;
	}

	public String getConfirmAnswer() {
		return confirmAnswer;
	}
	

	
	public Map<String, String> getQuestionListForOrg() {
		return questionListForOrg;
	}

	public void setQuestionListForOrg(Map<String, String> questionListForOrg) {
		this.questionListForOrg = questionListForOrg;
	}



	public String execute() {
		this.organizationCode=wcContext.getStorefrontId();
		setAuthQuestionListForOrg();
		return "success";
	}
	
	public String saveAnswer() {
		saveSecretAnswer();
		return "success";
	}
	
	/**
	 * Method to set the Secret Question List of the Current Org/Enterprise
	 *
	 * @param
	 * @return
	 */

	private void setAuthQuestionListForOrg() {
		Element questionListForOrgElem = null;
		try {
			Map<String, Element> map = prepareAndInvokeMashups();
			questionListForOrgElem=map.get(GET_ORG_QUESTION_LIST_MASHUP);
		} catch (XMLExceptionWrapper e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.questionListForOrg = new LinkedHashMap<String, String>();
		if (null != questionListForOrgElem) {
			ArrayList<Element> nodeQuestionList = SCXmlUtils.getChildren(
					questionListForOrgElem, "AuthQuestion");
			for (Iterator<Element> iter = nodeQuestionList.iterator(); iter
					.hasNext();) {
				Element questionElem = (Element) iter.next();
				String authQuestionKey = questionElem
						.getAttribute("AuthQuestionKey");
				String questionText = questionElem.getAttribute("QuestionText");
				questionText = Util.getInstance().getDBString(
						getWCContext().getSCUIContext().getRequest(),
						questionText);
				this.questionListForOrg.put(authQuestionKey, questionText);
			}
		}

	}

	
	/**
	 * Method to set the Secret Question of the user
	 *
	 * @param
	 * @return
	 */

	public  Document saveSecretAnswer() {
		Document outDoc = null;
		Element input=null;
	
		HashMap<String,String> valueMap = new HashMap<String, String>();
		valueMap.put("/Customer/@CustomerID",XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext));
		valueMap.put("/Customer/@OrganizationCode",wcContext.getStorefrontId() );
		valueMap.put("/Customer/CustomerContactList/CustomerContact/@CustomerContactID",wcContext.getCustomerContactId() );
		valueMap.put("/Customer/CustomerContactList/CustomerContact/User/@Loginid",wcContext.getCustomerContactId());
		valueMap.put("/Customer/CustomerContactList/CustomerContact/User/AuthAnswerList/AuthAnswer/@Answer",this.secretAnswer);
		valueMap.put("/Customer/CustomerContactList/CustomerContact/User/AuthAnswerList/AuthAnswer/@AuthQuestionKey",this.secretQuestion );
		try {
			input = WCMashupHelper.getMashupInput("SetSecretQuestion", valueMap, wcContext);
			Object obj = WCMashupHelper.invokeMashup("SetSecretQuestion", input, wcContext.getSCUIContext());
			if(obj!= null){
				outDoc = ((Element)obj).getOwnerDocument();
				getWCContext().setWCAttribute("setSecretQuestion", "Y", WCAttributeScope.LOCAL_SESSION);//JIRA 3487
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return outDoc;
		}
		return outDoc;
		
	}


}
