package com.xpedx.nextgen.passwordpolicy.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import com.yantra.shared.dbi.YFS_User;
import com.yantra.ycp.passwordpolicy.YCPPasswordPolicyUtil;
import com.yantra.ycp.passwordpolicy.factory.IPasswordPolicyForPasswordChange;
import com.yantra.ycp.passwordpolicy.result.PasswordPolicyResult;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dblayer.YFCDBContext;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;

/**
 * On Password change validate's following conditions:<br>
 * Password must contain at least two (2) alphabetical characters.<br>
 * Password must require at least one (1) numeric character.<br>
 * Password must require at least one (1) Upper Case character.<br>
 * Password must require at least one punctuation character or special character #@%^&*<br>
 * Password must not contain the characters '!', '$', '?'.<br>
 * Maximum repeated characters = 2.<br>
 * Password must not allow the user’s first name.<br>
 * Password must not allow the user’s last name.<br>
 * Password must not allow the user’s Login Name.<br>
 *  
 * @author sdodda
 *
 */
public class XPXPasswordPolicyValidator implements IPasswordPolicyForPasswordChange {

	private static String className;
    private static YFCLogCategory cat;
    int minNoOfAlphabeticalChars;
    int minNoOfNumericChars;
    int minNoOfUpperCaseChars;
    int minNoOfSplChars;
    char[] allowedSplChars;
    char[] disallowedChars;
    String enteredDisallowedChars="";
    int maxRepeatedChars;
    boolean disallowUserFirstName = false;
    boolean disallowUserLastName = false;
    boolean disallowUserLoginName = false;
    
    private String lastName;
    private String firstName;
    private String loginId;
	private int minNoOfLowerCaseChars;

    static 
    {
        className = XPXPasswordPolicyValidator.class.getName();
        cat = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
    }
    /**
     * Default
     */
    public XPXPasswordPolicyValidator(){}
    
	/**
	 * For Client side validation
	 */
	public XPXPasswordPolicyValidator(YFCElement pwdRulesElement, HashMap<String, String> userInfoMap) {
		loadPasswordPolicy(pwdRulesElement);
		setAdditionalFields(userInfoMap);
	}

	/**
	 * Extract the information from the params and set it into the class variables.
	 * @param pwdRulesElement
	 */
	private void loadPasswordPolicy(YFCElement pwdRulesElement) {
		// TODO call setParams method after extracting from the params
		YFCIterable<YFCElement> paramsElemsList = pwdRulesElement.getChildren("PasswordRuleCfg");
		String paramsXmlStr;
		YFCDocument paramDoc;
		YFCIterable<YFCElement> paramElemList;
		HashMap<String, String> pwdAttribsMap = new HashMap<String, String>();
		
		for(YFCElement pwdRuleConfigElem : paramsElemsList){
			paramsXmlStr = pwdRuleConfigElem.getAttribute("Params");
			
			paramDoc = YFCDocument.getDocumentFor(paramsXmlStr);
			paramElemList = paramDoc.getDocumentElement().getChildren("Param");
			for(YFCElement param : paramElemList){
				pwdAttribsMap.put(param.getAttribute("Name"), param.getAttribute("Value"));
			}
		}
		
		setParams(pwdAttribsMap);
		
	}

	private void setAdditionalFields(HashMap<String, String> userInfoMap) {
		firstName = userInfoMap.get("firstName");
		loginId = userInfoMap.get("loginId");
		lastName = userInfoMap.get("lastName");
	}

	@Override
	public PasswordPolicyResult onPasswordChange(YFCDBContext yfcdbcontext, YFS_User yfs_user, String newPassword, String oldPassword) {
		// save the user information coming in into member  variable.
		if(yfs_user!=null && !yfs_user.isNewRecord()){
			if(yfs_user.getContactPersonInfo()!=null){
	            firstName = yfs_user.getContactPersonInfo().getFirst_Name();
	            lastName =  yfs_user.getContactPersonInfo().getLast_Name();
	            loginId = yfs_user.getLoginid();
			}
		}else if(yfs_user.isNewRecord()){
			return PasswordPolicyResult.SUCCESS();
		}
		
		return onPasswordChange(newPassword, oldPassword);
		
	}

	public PasswordPolicyResult onPasswordChange(String newPassword,
			String oldPassword) {
		
		if(!YFCObject.isVoid(newPassword))
        {
            int length = newPassword.length();
            int iNoOfAlphabeticalChars = 0;
            int iNoOfNumericChars = 0;
            int iNoOfUpperCaseChars = 0;
            int iNoOfAllowedSplChars = 0;
            int iNoOfDisallowedChars = 0;
            for(int i = 0; i < length; i++)
            {
                char c = newPassword.charAt(i);
                if(Character.isUpperCase(c) || Character.isLowerCase(c)){
                	iNoOfAlphabeticalChars++;
                }
                if(Character.isDigit(c)){
                	iNoOfNumericChars++;
                }
                if(Character.isUpperCase(c)){
                	iNoOfUpperCaseChars++;
                }
                for (char allowedSplChar : allowedSplChars) {
					if(allowedSplChar==c){
						iNoOfAllowedSplChars++;
						break;
					}
				}
                
                int intDisChar =0;
                
                for (char disallowedChar : disallowedChars) {
					if(disallowedChar==c){
						iNoOfDisallowedChars++;
						if(enteredDisallowedChars.length()>0 && enteredDisallowedChars.contains(Character.toString(c)))
							continue;
						else
							enteredDisallowedChars = enteredDisallowedChars+ Character.toString(c);
						//break;
					}
				}
                
            }
            boolean exceededMaxRepeatedChars = this.checkIfExceedsMaxRepeatedChars(newPassword);
            if(iNoOfAlphabeticalChars < minNoOfAlphabeticalChars)
            {
            	/*YFCException ex = new YFCException("XPX0001");
                ex.setErrorDescription("The password must have atleast "+Integer.toString(minNoOfAlphabeticalChars)+" Alphabetical Characters.");
                throw ex;*/
            	return setPasswordPolicyResult("EXTNXPX0001", "The password must have atleast "+Integer.toString(minNoOfAlphabeticalChars)+" Alphabetical Characters.");
            }
            if(iNoOfNumericChars < minNoOfNumericChars)
            {
            	/*YFCException ex = new YFCException("EXTNXPX0002");
                ex.setErrorDescription("The password must have atleast "+Integer.toString(minNoOfNumericChars)+" Numerical Characters.");
                throw ex;*/
            	 return setPasswordPolicyResult("EXTNXPX0002", "The password must have atleast "+Integer.toString(minNoOfNumericChars)+" Numerical Characters.");
            }
            if(iNoOfUpperCaseChars < minNoOfUpperCaseChars)
            {
            	/*YFCException ex = new YFCException("EXTNXPX0003");
				ex.setErrorDescription("The password must have atleast "+Integer.toString(minNoOfUpperCaseChars)+" Upper case Characters.");
				throw ex;*/
                return setPasswordPolicyResult("EXTNXPX0003", "The password must have atleast "+Integer.toString(minNoOfUpperCaseChars)+" Upper case Characters.");
            }
            /* COmmented for Jira 1454 - as per DDD
            if(lastName!=null && newPassword.toUpperCase().contains(lastName.toUpperCase())){
            	//return PasswordPolicyResult.FAILURE("EXTNXPX0005");
            	return setPasswordPolicyResult("EXTNXPX0005", "The password must not contain the users last name");
            }*/
            
            if(loginId!=null && newPassword.toUpperCase().contains(loginId.toUpperCase())){
            	//return PasswordPolicyResult.FAILURE("EXTNXPX0006");
            	return setPasswordPolicyResult("EXTNXPX0006", "The password must not be the same as your login ID");
            }
            /* COmmented for Jira 1454 - as per DDD
            if(iNoOfAllowedSplChars < minNoOfSplChars){
            	//return PasswordPolicyResult.FAILURE("EXTNXPX0007");
            	return setPasswordPolicyResult("EXTNXPX0007", "The password must contain " +Integer.toString(minNoOfSplChars) +" Minumum number of Special Characters");
            }*/
            
            if(iNoOfDisallowedChars > 0){
            	//return PasswordPolicyResult.FAILURE("EXTNXPX0008");
            	return setPasswordPolicyResult("EXTNXPX0008", "The password must NOT contain " +Arrays.toString(disallowedChars) +" Characters");
            }
            
            if(exceededMaxRepeatedChars){
            	//return PasswordPolicyResult.FAILURE("EXTNXPX0009");
            	return setPasswordPolicyResult("EXTNXPX0009", "The password has exceeded Maximum number "+maxRepeatedChars+" of repeated Characters");
            }
            /* COmmented for Jira 1454 - as per DDD
            if(firstName!=null && newPassword.toUpperCase().contains(firstName.toUpperCase())){
            	//return PasswordPolicyResult.FAILURE("EXTNXPX0005");
            	return setPasswordPolicyResult("EXTNXPX0010", "The password must not contain the users first name");
            }*/
        }
		return PasswordPolicyResult.SUCCESS();
	}
//Adding a new method for jira 3992 with return type as HashMap
	public HashMap onResetPassword(String newPassword,
			String oldPassword) {
		HashMap errorMap = new HashMap();
		if(!YFCObject.isVoid(newPassword))
        {
            int length = newPassword.length();
            int iNoOfAlphabeticalChars = 0;
            int iNoOfNumericChars = 0;
            int iNoOfUpperCaseChars = 0;
            int iNoOfAllowedSplChars = 0;
            int iNoOfDisallowedChars = 0;
            for(int i = 0; i < length; i++)
            {
                char c = newPassword.charAt(i);
                if(Character.isUpperCase(c) || Character.isLowerCase(c)){
                	iNoOfAlphabeticalChars++;
                }
                if(Character.isDigit(c)){
                	iNoOfNumericChars++;
                }
                if(Character.isUpperCase(c)){
                	iNoOfUpperCaseChars++;
                }
                for (char allowedSplChar : allowedSplChars) {
					if(allowedSplChar==c){
						iNoOfAllowedSplChars++;
						break;
					}
				}
                
                int intDisChar =0;
                for (char disallowedChar : disallowedChars) {
					if(disallowedChar==c){
						iNoOfDisallowedChars++;
						if(enteredDisallowedChars!=null && enteredDisallowedChars.length()>0 && enteredDisallowedChars.contains(Character.toString(c)))
							continue;
						else
							enteredDisallowedChars = enteredDisallowedChars + Character.toString(c);
						//break;
					}
				}
                
            }
            boolean exceededMaxRepeatedChars = this.checkIfExceedsMaxRepeatedChars(newPassword);
            if(iNoOfAlphabeticalChars < minNoOfAlphabeticalChars)
            {
               	errorMap.put("EXTNXPX0001", "The password must contain at least "+Integer.toString(minNoOfAlphabeticalChars)+" alphabetical characters");
            }
            if(iNoOfNumericChars < minNoOfNumericChars)
            { 
              	errorMap.put("EXTNXPX0002", "The password must contain at least "+Integer.toString(minNoOfNumericChars)+" numeric character");
            }
            if(iNoOfUpperCaseChars < minNoOfUpperCaseChars)
            { 
            	errorMap.put("EXTNXPX0003", "The password must contain at least "+Integer.toString(minNoOfUpperCaseChars)+" uppercase character");
            }
            
            if(loginId!=null && newPassword.toUpperCase().contains(loginId.toUpperCase())){
            	//return PasswordPolicyResult.FAILURE("EXTNXPX0006");
            	errorMap.put("EXTNXPX0006", "The password must not be the same as your login ID");
            }
            
            if(iNoOfDisallowedChars > 0){
            	String messageString = "";
            	try
            	{
	            	for(int i = 0; i < enteredDisallowedChars.length() ; i++)
	            	{
	            		messageString = "The password must not contain the character '" + enteredDisallowedChars.charAt(i) + "'\n";
	                	errorMap.put("EXTNXPX000" + (12 + i), messageString);
	            	}
            	}
            	catch(Exception ex)
            	{
            	}
            	//return PasswordPolicyResult.FAILURE("EXTNXPX0008");
            }
            
            if(exceededMaxRepeatedChars){
            	//return PasswordPolicyResult.FAILURE("EXTNXPX0009");   
            	errorMap.put("EXTNXPX0009", "The password must not exceed more than "+maxRepeatedChars+" consecutive repeated characters");
            }
            if(length < 8){
            	//return PasswordPolicyResult.FAILURE("EXTNXPX0009");
            	errorMap.put("EXTNXPX0010", " The password must contain at least 8 characters");
            }
            if(length >14 ){
            	//return PasswordPolicyResult.FAILURE("EXTNXPX0009");
            	errorMap.put("EXTNXPX0011", " The password has a maximum length of 14 characters");
            }
           
        }
		return errorMap;
	}

	private PasswordPolicyResult setPasswordPolicyResult(final String errorCode, final String errorMessage){

		PasswordPolicyResult status = new PasswordPolicyResult() {
				private PasswordPolicyResult tempObj = PasswordPolicyResult.FAILURE(errorCode);
				public ResultType getResultType() { 
					return tempObj.getResultType(); 
				}
				
				public String getErrorCode() { 
					return tempObj.getErrorCode(); 
				}
				
				public Map<String, String> getResultMap() {
					Map<String, String> myMap = tempObj.getResultMap();
					myMap.put("ErrorMsg", errorMessage);
					return myMap;
				}
			};
			return status;
		}
	/**
	 * @param newPassword
	 * @return
	 */
	/* commented for Jira 1454 : No more than 2 consecutive identical characters eg : AAAaBBBb1 aAaAaAaA1 should fail
	private boolean checkIfExceedsMaxRepeatedChars(String newPassword) {
		//Set<String> setOfRepeatedChars = new HashSet<String>();
		
		ArrayList<String> setOfRepeatedChars = new ArrayList<String>();
		newPassword = newPassword.toUpperCase();
		char[] cArray = newPassword.toCharArray();
		char c1, c2; 
		boolean exceededMaxRepeatedChars = false;
		for (int i=0; i<cArray.length ; i++) {
			c1 = cArray[i];
			log.debug("c1="+c1);
			if((i+1)<cArray.length)
				for(int j=i+1;j<cArray.length; j++){
					c2 = cArray[j];
					log.debug("c2="+c2);
					if(c1==c2){
						log.debug("added char in repeated");
						setOfRepeatedChars.add(String.valueOf(c1));
						log.debug("size="+setOfRepeatedChars.size());						
					}
				}
			if(setOfRepeatedChars.size()>maxRepeatedChars){
				exceededMaxRepeatedChars = true;
				log.debug("exceededMaxRepeatedChars=true");
				break;
			}
		}

		return exceededMaxRepeatedChars;
	}*/

	/* Added for Jira 1454 : No more than 2 consecutive identical characters eg : AAAaBBBb1 aAaAaAaA1 should fail */
	private boolean checkIfExceedsMaxRepeatedChars(String newPassword) {	
	
	ArrayList<String> setOfRepeatedChars = new ArrayList<String>();
//Commented for JIRA 1454
	//newPassword = newPassword.toUpperCase();
	char[] cArray = newPassword.toCharArray();
	char c1, c2; 
	boolean exceededMaxRepeatedChars = false;
	for (int i=0; i<cArray.length ; i++) {
		c1 = cArray[i];
		
		if((i+1)<cArray.length)
			for(int j=i+1;j<cArray.length; j++){
				c2 = cArray[j];
				
				if(c1==c2){
					
					setOfRepeatedChars.add(String.valueOf(c1));
					setOfRepeatedChars.add(String.valueOf(c2));
						
					if(setOfRepeatedChars.size()>maxRepeatedChars){
						exceededMaxRepeatedChars = true;						
						break;
					}
					
				}
				else{					
					setOfRepeatedChars.clear();
					break;
				}
			}
		if(setOfRepeatedChars.size()>2){
			exceededMaxRepeatedChars = true;
			break;
		}
	}

	return exceededMaxRepeatedChars;
}	
	
	@Override
	public void setParams(Map<String, String> paramsMap) {
		if(!YFCObject.isVoid(paramsMap.get("MinNoOfAlphabeticalChars")))
        {
            String sMinNoOfAlphabeticalChars = paramsMap.get("MinNoOfAlphabeticalChars");
            minNoOfAlphabeticalChars = YCPPasswordPolicyUtil.parseIntParameters("MinNoOfAlphabeticalChars", sMinNoOfAlphabeticalChars);
        }
        if(!YFCObject.isVoid(paramsMap.get("MinNoOfNumericChars")))
        {
            String sMinNoOfNumericChars = paramsMap.get("MinNoOfNumericChars");
            minNoOfNumericChars = YCPPasswordPolicyUtil.parseIntParameters("MinNoOfNumericChars", sMinNoOfNumericChars);
        }
        if(!YFCObject.isVoid(paramsMap.get("MinNoOfUpperCaseChars")))
        {
            String sMinNoOfUpperCaseChars = paramsMap.get("MinNoOfUpperCaseChars");
            minNoOfUpperCaseChars = YCPPasswordPolicyUtil.parseIntParameters("MinNoOfUpperCaseChars", sMinNoOfUpperCaseChars);
        }
        if(!YFCObject.isVoid(paramsMap.get("MinNoOfLowerCaseChars")))
        {
            String sMinNoOfUpperCaseChars = paramsMap.get("MinNoOfLowerCaseChars");
            minNoOfLowerCaseChars = YCPPasswordPolicyUtil.parseIntParameters("MinNoOfUpperCaseChars", sMinNoOfUpperCaseChars);
        }
        if(!YFCObject.isVoid(paramsMap.get("MaxRepeatedChars")))
        {
            String sMaxRepeatedChars = paramsMap.get("MaxRepeatedChars");
            maxRepeatedChars = YCPPasswordPolicyUtil.parseIntParameters("MaxRepeatedChars", sMaxRepeatedChars);
        }
        if(!YFCObject.isVoid(paramsMap.get("MinNoOfSplChars")))
        {
            String sMinNoOfSplChars = paramsMap.get("MinNoOfSplChars");
            minNoOfSplChars = YCPPasswordPolicyUtil.parseIntParameters("MinNoOfSplChars", sMinNoOfSplChars);
        }
        if(!YFCObject.isVoid(paramsMap.get("AllowedSplChars")))
        {
            String sAllowedSplChars = paramsMap.get("AllowedSplChars");
            allowedSplChars = sAllowedSplChars.toCharArray();
        }
        
        if(!YFCObject.isVoid(paramsMap.get("DisallowedChars")))
        {
            String sDisallowedChars = paramsMap.get("DisallowedChars");
            disallowedChars = sDisallowedChars.toCharArray();
        }
        if(!YFCObject.isVoid(paramsMap.get("DisallowUserFirstName"))){
        	String sDisallowUserFirstName = paramsMap.get("DisallowUserFirstName");
            disallowUserFirstName = Boolean.getBoolean(sDisallowUserFirstName);
        }
		if(!YFCObject.isVoid(paramsMap.get("DisallowUserLoginName"))){
			String sDisallowUserLoginName = paramsMap.get("DisallowUserLoginName");
            disallowUserLoginName =  Boolean.getBoolean(sDisallowUserLoginName);	
		}
		if(!YFCObject.isVoid(paramsMap.get("DisallowUserLastName"))){
			String sDisallowUserLastName = paramsMap.get("DisallowUserLastName");
            disallowUserLastName = Boolean.getBoolean(sDisallowUserLastName);
		}
        
        if(cat.isDebugEnabled()){
        cat.debug((new StringBuilder()).append("param values : minNoOfAlphabeticalChars").append(minNoOfAlphabeticalChars)
        		.append("minNoOfNumericChars").append(minNoOfNumericChars)
        		.append("minNoOfUpperCaseChars").append(minNoOfUpperCaseChars).toString());
        }

	}

}
