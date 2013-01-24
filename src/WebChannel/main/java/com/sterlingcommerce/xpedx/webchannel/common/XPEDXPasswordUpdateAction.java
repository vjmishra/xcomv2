package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCConstants;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.profile.user.XPEDXSaveUserInfo;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXClientPasswordValidator;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.ycp.passwordpolicy.result.PasswordPolicyResult;
import com.yantra.ycp.passwordpolicy.result.PasswordPolicyResult.ResultType;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCException;

public class XPEDXPasswordUpdateAction extends WCMashupAction{
	public String userPwdToValidate;
	private static final Logger log = Logger.getLogger(XPEDXPasswordUpdateAction.class);
	private Map<String, String> pwdValidationResultMap = null;
	public String errorPwd = "errorPwd";
	private static final String YES = "Y";
	private String enterpriseCode="";
	private String passwordUpdate;
	public String getPasswordUpdate() {
		return passwordUpdate;
	}
	public void setPasswordUpdate(String passwordUpdate) {
		this.passwordUpdate = passwordUpdate;
	}
	public Map<String, String> getPwdValidationResultMap() {
		return pwdValidationResultMap;
	}
	public void setPwdValidationResultMap(Map<String, String> pwdValidationResultMap) {
		this.pwdValidationResultMap = pwdValidationResultMap;
	}
	public String getUserPwdToValidate() {
		return userPwdToValidate;
	}
	public void setUserPwdToValidate(String userPwdToValidate) {
		this.userPwdToValidate = userPwdToValidate;
	}
	public String getMaskedPasswordString() {
		return WCConstants.MASKED_PASSWORD_STRING;
	}
	private boolean checkIfPasswordChanged() {
		String passwordParam = getUserPwdToValidate();
		if (YFCCommon.isVoid(passwordParam)
				|| passwordParam.equals(WCConstants.MASKED_PASSWORD_STRING)) {
			return false;
		}
		return true;
	}
	public String execute() {
		return "success"; 
		
	}
	
	public String passwordUpdateValidation(){
		
		HashMap<String, String> userInfoMap = new HashMap<String, String>();
		userInfoMap.put("newPassword", getUserPwdToValidate());
		userInfoMap.put("loginId", wcContext.getLoggedInUserId());
		String newPassword="";
		String useLoginID="";
		if (checkIfPasswordChanged()) {
			newPassword = getUserPwdToValidate();
		useLoginID = wcContext.getCustomerContactId();
		}
		String returnStr = SUCCESS;
		PasswordPolicyResult pwdValidationResult;
		Map<String, String> resultMap = null;
		try {
			// validate client password
			pwdValidationResult = XPEDXClientPasswordValidator.validateClientPassword(userInfoMap);
			if(ResultType.FAILURE.equals(pwdValidationResult.getResultType())){
				String errorMsg = (String)pwdValidationResult.getResultMap().get("ErrorMsg");
				if(errorMsg!=null){
					resultMap = new HashMap<String, String>();
					resultMap.put("ErrorMsg", errorMsg);
					setPwdValidationResultMap(resultMap);
				}
			}
			if (checkIfPasswordChanged()) {
				List<String> strErrorMessage= new ArrayList<String>();
				if(newPassword.length()<8){

					strErrorMessage.add("The password must contain at least 8 characters.");
				}
				
					char[] newPwdChar = newPassword.toCharArray();
					int pwdCharLength = newPwdChar.length;
					int iNoOfNumericChars = 0;
					int iNoOfUpperCaseChars = 0;
					int iNoOfAlphabeticalChars = 0;
					boolean exceededMaxRepeatedChars = this.checkIfExceedsMaxRepeatedChars(newPassword);
					for(int i=0;i<pwdCharLength;i++){
						char c = newPwdChar[i];
						/*if (newPwdChar[i] == 33){
							strErrorMessage.add("The password must not contain the character '"+newPwdChar[i]+"'");
						}
						
						if (newPwdChar[i] == 36){
							strErrorMessage.add("The password must not contain the character '"+newPwdChar[i]+"'");
						}
						
						if (newPwdChar[i] == 63){
							strErrorMessage.add("The password must not contain the character '"+newPwdChar[i]+"'");
						}*/
						if(Character.isUpperCase(c) || Character.isLowerCase(c)){
		                	iNoOfAlphabeticalChars++;
		                }
						if(Character.isDigit(c)){
		                	iNoOfNumericChars++;
		                }
						if(Character.isUpperCase(c)){
		                	iNoOfUpperCaseChars++;
		                }
					}
					if(newPassword.toUpperCase().contains(useLoginID.toUpperCase())){
						strErrorMessage.add("The password must not contain the user's login ID. ");
					}
					/*if(iNoOfAlphabeticalChars < 2){
						strErrorMessage.add("The password must contain at least 2 alpha characters.");
					}*/
					if(iNoOfNumericChars < 1){
						strErrorMessage.add("The password must contain at least 1 numeric character. ");
					}
					if(iNoOfUpperCaseChars < 1){
						strErrorMessage.add("The password must contain at least 1 uppercase character.");
					}
					if(exceededMaxRepeatedChars){
						strErrorMessage.add("The password must not exceed more than 2 consecutive repeated characters.");
					}
					
					if(strErrorMessage.size()>0){
						request.getSession().setAttribute("errorNote",strErrorMessage);
						for(String errorDesc:strErrorMessage)
						{
							if(pwdValidationResultMap == null)
								pwdValidationResultMap = new HashMap<String, String>();
							pwdValidationResultMap.put(errorDesc, errorDesc);
						}
							
						return errorPwd;
					}
			}
		} 
		catch (YFCException passexp) {
			// This exception is put here to handle the password validation exceptions.
			if(log.isDebugEnabled()){
			log.debug("GOT EXCEPTION");
			}
			pwdValidationResultMap = new HashMap<String, String>();
			pwdValidationResultMap.put(((YFCException) passexp).getAttribute("ErrorCode"), ((YFCException) passexp).getAttribute("ErrorDescription"));
			setPwdValidationResultMap(pwdValidationResultMap);
			passexp.printStackTrace();
			return ERROR;
		}
		catch (Exception e) {
			if(e instanceof YFCException){
				pwdValidationResultMap = new HashMap<String, String>();
				pwdValidationResultMap.put(((YFCException) e).getAttribute("ErrorCode"), ((YFCException) e).getAttribute("ErrorDescription"));
			}
			e.printStackTrace();
		}
		
		return returnStr;
	}
	
	public String passwordChange(){
		try{
		Map<String, String> valueMap = new HashMap<String, String>();
		String newPassword="";
		String useLoginID="";
			if (checkIfPasswordChanged()) {
				newPassword = getUserPwdToValidate();
			useLoginID = wcContext.getCustomerContactId();
			}
			
			if (checkIfPasswordChanged()) {
				
					valueMap.put("/Customer/@CustomerID",getWCContext().getCustomerId());
					valueMap.put("/Customer/@OrganizationCode",wcContext.getStorefrontId() );
					valueMap.put("/Customer/CustomerContactList/CustomerContact/@CustomerContactID",wcContext.getCustomerContactId() );
					valueMap.put("/Customer/CustomerContactList/CustomerContact/User/@Password",newPassword);
					valueMap.put("/Customer/CustomerContactList/CustomerContact/User/@GeneratePassword",YES);
					valueMap.put("/Customer/CustomerContactList/CustomerContact/User/@EnterpriseCode",wcContext.getStorefrontId() );
					valueMap.put("/Customer/CustomerContactList/CustomerContact/User/@DisplayUserID",wcContext.getCustomerContactId());
					valueMap.put("/Customer/CustomerContactList/CustomerContact/User/@OrganizationKey",getWCContext().getCustomerId());
					
			}
			Element userContactInput = WCMashupHelper.getMashupInput(
					"ManagePasswordUpdate", wcContext);
			populateMashupInput("ManagePasswordUpdate", valueMap,
					userContactInput);
			invokeMashup("ManagePasswordUpdate", userContactInput);
			//getWCContext().setWCAttribute("setPasswordUpdate", "N", WCAttributeScope.LOCAL_SESSION);
			Boolean sessionForUserProfile=  (Boolean) XPEDXWCUtils.getObjectFromCache("setPasswordUpdate");
			if(sessionForUserProfile == true){
					XPEDXWCUtils.removeObectFromCache("setPasswordUpdate");
			}
			return "success";
		}
		catch (YFCException passexp) {
			// This exception is put here to handle the password validation exceptions.
			if(log.isDebugEnabled()){
			log.debug("GOT EXCEPTION");
			}
			pwdValidationResultMap = new HashMap<String, String>();
			pwdValidationResultMap.put(((YFCException) passexp).getAttribute("ErrorCode"), ((YFCException) passexp).getAttribute("ErrorDescription"));
			setPwdValidationResultMap(pwdValidationResultMap);
			return ERROR;
		}
		catch (XMLExceptionWrapper xmlEx) {
			log.error("Error in saving the User/Contact Information", xmlEx
					.getCause());
			//Added For Jira-3106
			YFCElement errorMsgElement=xmlEx.getXML();
			
			boolean isPPMesage=false;
			if(errorMsgElement != null)
			{
				YFCNodeList<YFCElement> errors=errorMsgElement.getElementsByTagName("Error");			
				if(errors != null)
				{
					YFCElement errorMessages=errors.item(0);
					String errorMsg="";
					if( errorMessages!=null )
					{						
						errorMsg=errorMessages.getAttribute("ErrorDescription");
						String errorCode = errorMessages.getAttribute("ErrorCode");
						if( errorMsg!=null && errorMsg.trim().length()!=0)
						{
							//Added for JIRA 3553
							if(errorCode.equalsIgnoreCase("YCP0468") || errorMsg.equalsIgnoreCase("The new password matches one of the old password.")){
								return errorPwd;
							}else
							{
								isPPMesage=true;
								request.getSession().setAttribute("errorNote",errorMsg);
							}
						}
					}
					
				}
			}
			if(!isPPMesage)
			request.getSession().setAttribute("errorNote","The password is invalid. Please revise and try again.");
			//Fix End For Jira-3106
			return errorPwd;
		} catch (CannotBuildInputException e) {
			log.error("Error in creating the User/Contact Information", e
					.getCause());
			return errorPwd;
		}
	}
private boolean checkIfExceedsMaxRepeatedChars(String newPassword) {	
		
		ArrayList<String> setOfRepeatedChars = new ArrayList<String>();
	//Commented for JIRA 1454
		//newPassword = newPassword.toUpperCase();
		char[] cArray = newPassword.toCharArray();
		char c1, c2; 
		boolean exceededMaxRepeatedChars = false;
		for (int i=0; i<cArray.length ; i++) {
			c1 = cArray[i];
			
			if((i+1)<cArray.length){
				for(int j=i+1;j<cArray.length; j++){
					c2 = cArray[j];
					
					if(c1==c2){
						
						setOfRepeatedChars.add(String.valueOf(c1));
						setOfRepeatedChars.add(String.valueOf(c2));
							
						if(setOfRepeatedChars.size()>2){
							exceededMaxRepeatedChars = true;						
							break;
						}
						
					}
					else{					
						setOfRepeatedChars.clear();
						break;
					}
				}
			}
			if(setOfRepeatedChars.size()>2){
				exceededMaxRepeatedChars = true;
				break;
			}
		}

		return exceededMaxRepeatedChars;
	}
	/*public String submitPassword(){
		if (checkIfPasswordChanged()) {
			String newPassword = wcContext.getSCUIContext()
					.getRequest().getParameter("userpassword");
		String useLoginID = wcContext.getSCUIContext()
		.getRequest().getParameter("userName");
		}
		
		if (checkIfPasswordChanged()) {
			//XB - 319
			List<String> strErrorMessage= new ArrayList<String>();
			//Added For Jira-3106
			if(newPassword.length()<8){

				// This exception is put here to handle the password validation exceptions.
				//request.getSession().setAttribute("errorNote","The password must contain at least 8 characters. Please revise and try again.");
				strErrorMessage.add("The password must contain at least 8 characters.");
				setSuccess(false);
				setSaveAddUser(false);
				//return REDIRECT;
			}
			//else if(newPassword.length()>=8){
				char[] newPwdChar = newPassword.toCharArray();
				//int countSpclChar = 0;
				int pwdCharLength = newPwdChar.length;
				int iNoOfNumericChars = 0;
				int iNoOfUpperCaseChars = 0;
				int iNoOfAlphabeticalChars = 0;
				boolean exceededMaxRepeatedChars = this.checkIfExceedsMaxRepeatedChars(newPassword);
				for(int i=0;i<pwdCharLength;i++){
					char c = newPwdChar[i];
					if (newPwdChar[i] == 33){
						//XB - 319
						strErrorMessage.add("The password must not contain the character '"+newPwdChar[i]+"'");
						//countSpclChar++;
					}
					
					if (newPwdChar[i] == 36){
						//XB - 319
						strErrorMessage.add("The password must not contain the character '"+newPwdChar[i]+"'");
						//countSpclChar++;
					}
					
					if (newPwdChar[i] == 63){
						//XB - 319
						strErrorMessage.add("The password must not contain the character '"+newPwdChar[i]+"'");
						//countSpclChar++;
					}
					if(Character.isUpperCase(c) || Character.isLowerCase(c)){
	                	iNoOfAlphabeticalChars++;
	                }
					if(Character.isDigit(c)){
	                	iNoOfNumericChars++;
	                }
					if(Character.isUpperCase(c)){
	                	iNoOfUpperCaseChars++;
	                }
				}
				if(newPassword.toUpperCase().contains(useLoginID.toUpperCase())){
					//request.getSession().setAttribute("errorNote","The password cannot contain the user's login ID. Please revise and try again.");
					strErrorMessage.add("The password must not contain the user's login ID. ");
					setSuccess(false);
					setSaveAddUser(false);
					//return REDIRECT;
				}
				if(iNoOfAlphabeticalChars < 2){
					//request.getSession().setAttribute("errorNote","The password must contain at least 2 alpha characters. Please revise and try again.");
					strErrorMessage.add("The password must contain at least 2 alpha characters.");
					setSuccess(false);
					setSaveAddUser(false);
					//return REDIRECT;
				}
				if(iNoOfNumericChars < 1){
					//request.getSession().setAttribute("errorNote","The password must contain at least 1 numeric character. Please revise and try again.");
					strErrorMessage.add("The password must contain at least 1 numeric character. ");
					setSuccess(false);
					setSaveAddUser(false);
					//return REDIRECT;
				}
				if(iNoOfUpperCaseChars < 1){
					//request.getSession().setAttribute("errorNote","The password must contain at least 1 uppercase character. Please revise and try again.");
					strErrorMessage.add("The password must contain at least 1 uppercase character.");
					setSuccess(false);
					setSaveAddUser(false);
					//return REDIRECT;
				}
				if(exceededMaxRepeatedChars){
					//request.getSession().setAttribute("errorNote","The password cannot exceed more than 2 consecutive repeated characters. Please revise and try again.");
					strErrorMessage.add("The password must not exceed more than 2 consecutive repeated characters.");
					setSuccess(false);
					setSaveAddUser(false);
					//return REDIRECT;
				}
				//commented for XB - 319
				if(countSpclChar > 0){
					//request.getSession().setAttribute("errorNote","The password cannot contain $, ? or ! characters. Please revise and try again.");
					
					setSuccess(false);
					setSaveAddUser(false);
					//return REDIRECT;
				}
			//}
				//start XB - 319
				if(strErrorMessage.size()>0){
					strErrorMessage.add("Please revise and try again.");
					request.getSession().setAttribute("errorNote",strErrorMessage);
					return REDIRECT;
				}
				//end XB - 319
				
			//Fix End For Jira-3106
			valuemap
					.put("/Customer/CustomerContactList/CustomerContact/User/@Password",
							newPassword);
		}
		if (checkIfAnswerChanged()) {
			String secretQuestionKey = wcContext
					.getSCUIContext().getRequest()
					.getParameter("secretQuestion");
			String secretAnswer = wcContext.getSCUIContext()
					.getRequest().getParameter("secretAnswer");
			Element userElem = (Element) (SCXmlUtils
					.getElements(userContactInput,
							"/CustomerContactList/CustomerContact/User"))
					.get(0);
			Element authAnswerListElem = SCXmlUtils
					.createChild(userElem, "AuthAnswerList");
			authAnswerListElem.setAttribute("Reset", "Y");
			Element authAnswerElem = SCXmlUtils.createChild(
					authAnswerListElem, "AuthAnswer");
			authAnswerElem.setAttribute("AuthQuestionKey",
					secretQuestionKey);
			authAnswerElem.setAttribute("Answer", secretAnswer);
		}
		populateMashupInput(MANAGE_USER_MASHUP, valuemap,
				userContactInput);
		invokeMashup(MANAGE_USER_MASHUP, userContactInput);
	}
		return "success";
	}*/
}