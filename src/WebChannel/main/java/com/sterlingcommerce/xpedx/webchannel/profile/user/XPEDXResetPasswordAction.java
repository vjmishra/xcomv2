package com.sterlingcommerce.xpedx.webchannel.profile.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.profile.user.ResetPassword;
import com.sterlingcommerce.webchannel.profile.user.UserList;
import com.sterlingcommerce.webchannel.profile.user.UserProfileHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

public class XPEDXResetPasswordAction extends ResetPassword {

	// Logger
	private static final Logger log = Logger.getLogger(UserList.class);
	
	public String execute()
	{
		setResetType( UserProfileHelper.getResetTypeAllwdForUser(getUserId(), wcContext));
        if(getResetType() == null)
        {
            setResultStatus("error");
            return "success";
        }
        try
        {
        	System.out.println("*****  Login ID ********  "+ getUserId());
        	Map<String, String> valueMap1 = new HashMap<String, String>();
    		valueMap1.put("/ResetPassword/User/@Loginid", getUserId());
    		valueMap1.put("/ResetPassword/@ResetType",getResetType());
    		Element input1;
    		try {
    			input1 = WCMashupHelper.getMashupInput("ResetPassword",
    					valueMap1, wcContext.getSCUIContext());
    			System.out.println("*****  Input XML for reset Password is  ********  "+ SCXmlUtil.getString(input1));
    			Object obj1 = WCMashupHelper.invokeMashup("ResetPassword",
    					input1, wcContext.getSCUIContext());

    		} catch (CannotBuildInputException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

        }
        catch(com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper e)
        {
            log.error("Excpetion occurred while requesting for Password reset", e.getCause());
            setResultStatus("error");
            return "success";
        }
        catch(Exception e)
        {
        	log.error("Excpetion occurred while requesting for Password reset", e.getCause());
            setResultStatus("error");
            return "success";
        }
        setResultStatus("success");
        return "success";
	}
	
}
