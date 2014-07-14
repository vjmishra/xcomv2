package com.xpedx.nextgen.passwordpolicy.impl;

import java.util.HashMap;
import java.util.Map;

import com.yantra.shared.dbi.YFS_User;
import com.yantra.ycp.passwordpolicy.YCPPasswordPolicyUtil;
import com.yantra.ycp.passwordpolicy.defaultimpl.login.YCPLoginValidator;
import com.yantra.ycp.passwordpolicy.result.PasswordPolicyResult;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dblayer.YFCDBContext;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCDateUtils;

public class XPXPasswordExpireCheck extends YCPLoginValidator {
	private static String className;
    private static YFCLogCategory cat;
	int numDaysValid =0;
	public int getNumDaysValid() {
		return numDaysValid;
	}

	public void setNumDaysValid(int numDaysValid) {
		this.numDaysValid = numDaysValid;
	}

	static 
    {
        className = XPXPasswordExpireCheck.class.getName();
        cat = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
    }
    /**
     * Default
     */
    public XPXPasswordExpireCheck(){
    	super();
    }
    
	/**
	 * For password validation for expiry on login
	 */
	public XPXPasswordExpireCheck(YFCElement pwdRulesElement, HashMap<String, String> userInfoMap) {
		loadPasswordPolicy(pwdRulesElement);
		
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
	@Override
	public void setParams(Map paramsMap)
    {
        if(!YFCObject.isVoid(paramsMap.get("NumDaysValid")))
        {            String sNumDaysValid = (String)paramsMap.get("NumDaysValid");
            numDaysValid = YCPPasswordPolicyUtil.parseIntParameters("NumDaysValid", sNumDaysValid);
        }       
    }
	
	@Override
	public PasswordPolicyResult onLoginAttempt(YFCDBContext ctx, YFS_User user)
    {	
		if(user.getLoginid()!=null && user.getLoginid().indexOf("guest_")>-1){
			return PasswordPolicyResult.SUCCESS();
		}
		if(numDaysValid > 0)
        {
           com.yantra.yfc.util.YFCDate pwdLastChangedOn = user.getPwdlastchangedonDate();
           com.yantra.yfc.util.YFCDate currentDate = ctx.getDBDate().getYDate();
           int lapseTime = YFCDateUtils.diffDays(pwdLastChangedOn, currentDate);
            if(lapseTime >= 0)
            {
            	int daysUntilExpiration = numDaysValid - lapseTime;            
                if(daysUntilExpiration > 0)
                    return PasswordPolicyResult.SUCCESS(daysUntilExpiration);
              
            }            
            return PasswordPolicyResult.FAILURE("ERROR-YOUR PASSWORD EXPIRED");
        } else
        {            return PasswordPolicyResult.FAILURE("ERROR-YOUR PASSWORD EXPIRED");
        }
    }

}
