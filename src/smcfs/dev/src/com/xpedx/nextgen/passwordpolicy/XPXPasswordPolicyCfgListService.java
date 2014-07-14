package com.xpedx.nextgen.passwordpolicy;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * Description: This class is used to fetch the password policies defined for different storeFronts
 * @author srao
 *
 */

public class XPXPasswordPolicyCfgListService implements YIFCustomApi {
	
	private static YIFApi api = null;
	private static YFCLogCategory log;
	
	static {
		
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException yifEx) {
			yifEx.printStackTrace();
		}
	}

	public Document getPasswordPoliciesList(YFSEnvironment env,Document inputXML)  {
		
		Document pwdRulesElemDoc = null;
		YFCDocument opDoc = null;
		try {
			
			/**
			 * 1. call getPasswordPolicyList
			 * 		I/p: <PasswordPolicy OrganizationCode="xpedx"/>
			 * 		o/p: <PasswordPolicy Description="XPXPasswordPolicy" Name="XPXPasswordPolicy" OrganizationCode="xpedx" PasswordPolicyKey="2010092120425558488" Status="1"/>
			 * 
			 * 2. using the PasswordPolicyKey, call getPasswordRuleCfgList
			 * 		i/p: <PasswordRuleCfg PasswordPolicyKey="2010092120425558488"/>
			 * 		o/p: <PasswordRuleCfgList><PasswordRuleCfg Params="<?xml version="1.0" encoding="UTF-8"?>
			 *				<ParamList>
			 *			    <Param Name="ResetType" Value="Email"/>
			 *				    <Param Name="CheckIntervalMinutes" Value="60"/>
			 *				</ParamList>
			 *				" PasswordPolicyKey="2010092120425558488" PasswordRuleCfgKey="2010092120425558489" PasswordRuleDefnKey="2010092120055658444"/>
			 */
			if(inputXML != null)
			{
				log.debug("Input XML for the getPasswordPolicyList is " + SCXmlUtil.getString(inputXML));
			}
			// invoke getPasswordPolicyDetails API
			
			Document pwdPolicyDetailDoc = api.invoke(env, "getPasswordPolicyList", inputXML);
			if(log.isDebugEnabled()){
				log.debug("Output document of getPasswordPolicyList is: " + SCXmlUtil.getString(pwdPolicyDetailDoc));
			}
			String pwdPolicyKey = null;
			if(null!=pwdPolicyDetailDoc){
				NodeList policyElemsList = pwdPolicyDetailDoc.getElementsByTagName("PasswordPolicy");
				Element policyElem;
				for(int j=0; policyElemsList!=null && j<policyElemsList.getLength(); j++){
					policyElem = (Element)policyElemsList.item(j);
					pwdPolicyKey = policyElem.getAttribute("PasswordPolicyKey");
					break;
				}
			}
			if(log.isDebugEnabled()){
				log.debug("Password policy key is: " +pwdPolicyKey);
			}
			if(!YFCUtils.isVoid(pwdPolicyKey)){
				
				Document inputPwdKeyDoc = SCXmlUtil.createDocument("PasswordRuleCfg");
				Element inputPwdElement = inputPwdKeyDoc.getDocumentElement();
				inputPwdElement.setAttribute("PasswordPolicyKey", pwdPolicyKey);
				
				// using pwdPolicyKey, get the rules configured for this
				pwdRulesElemDoc = api.invoke(env, "getPasswordRuleCfgList", inputPwdKeyDoc);
				env.clearApiTemplate("getPasswordRuleCfgList");
				
			}
			if(pwdRulesElemDoc!=null)opDoc = YFCDocument.getDocumentFor(pwdRulesElemDoc);
			if(log.isDebugEnabled()){
				log.debug("Password Policies Document output: "+opDoc.toString());
			}
			log.verbose("Password Policies Document:  "+opDoc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Error while fetching the password policies " + e.getMessage(), e);
		}
		
		return pwdRulesElemDoc;
	}

	@Override
	public void setProperties(Properties properties) throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}