/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.utilities;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.xpedx.nextgen.passwordpolicy.impl.XPXPasswordPolicyValidator;
import com.yantra.ycp.passwordpolicy.result.PasswordPolicyResult;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;

/**
 * @author srao
 *
 */
public class XPEDXClientPasswordValidator {

	// Logger
	private static final Logger log = Logger.getLogger(XPEDXClientPasswordValidator.class);
	
	public static PasswordPolicyResult validateClientPassword(HashMap<String, String> userInfoMap)throws Exception{
		
		// invoke the Custom service which fetches the password policy key
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		
		Element res = null;
		XPXPasswordPolicyValidator policyValidator;
		PasswordPolicyResult result = PasswordPolicyResult.SUCCESS();
		Element input =  null;
		YFCElement resElem = null;
		try {
			HashMap<String, String> valueMap =  new HashMap<String, String>();
			valueMap.put("/PasswordPolicy/@OrganizationCode", wcContext.getStorefrontId());
			input = WCMashupHelper.getMashupInput("XpxPasswordPoliciesCfg", valueMap, wcContext.getSCUIContext());
			 
			res = (Element)WCMashupHelper.invokeMashup("XpxPasswordPoliciesCfg", input , wcContext.getSCUIContext());
			resElem = YFCDocument.getDocumentFor(res.getOwnerDocument()).getDocumentElement();
			
			policyValidator = new XPXPasswordPolicyValidator(resElem, userInfoMap);
			result = policyValidator.onPasswordChange(userInfoMap.get("newPassword"), userInfoMap.get("oldPassword"));
		} catch (Exception e) {
			log.error(" Error during Password Validation : " +e.getMessage(), e);
			throw e;
		}
		// using the op xml call XPXPasswordPolicyValidator constructor passing op xml and map with user info
		return result;
	}
	
}
