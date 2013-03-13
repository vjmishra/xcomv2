/**
 * This is action-sepcific validation class for action saveNewUserInfo for
 * Create User functionality.
 */

package com.sterlingcommerce.xpedx.webchannel.profile.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.validators.AbstractWCFormFieldValidation;
import com.sterlingcommerce.webchannel.core.validators.ValidationEntry;
import com.sterlingcommerce.webchannel.core.validators.ValidationInfo;
import com.sterlingcommerce.webchannel.core.validators.WCValidationUtils;
import com.sterlingcommerce.webchannel.profile.user.UserProfileHelper;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXSaveNewUserInfoValidation extends
	AbstractWCFormFieldValidation {

    private static final String USER_NAME_UI_FIELD_NAME = "userName";
    // User resource package
    public static final String USER_RESOURCE_PACKAGE_STRING = "com.sterlingcommerce.xpedx.webchannel.profile.user.package";
    private Map parameters = new HashMap<String, String>();
    private List<String> reqdFields = new ArrayList<String>();
    IWCContext wcContext = null;

    /**
     * Implements validate method of interface IWCFormFieldValidation
     * 
     * @param parameters
     * @param jsonData
     * @param wcContext
     * 
     */

    @Override
    public void validate(Map parameters, ValidationInfo jsonData,
	    IWCContext wcContext) {
	this.parameters = parameters;
	this.wcContext = wcContext;
	// Add required fields to the list
	reqdFields.add(USER_NAME_UI_FIELD_NAME);
	reqdFields.add("preferredLocale");
	reqdFields.add("firstName");
	reqdFields.add("lastName");
	reqdFields.add("emailId");
	// Validate if Username entered
	String userIdValue = WCValidationUtils.getParameterValue(
		USER_NAME_UI_FIELD_NAME, parameters);
	for (String fieldName : reqdFields) {
	    validateRequired(parameters, jsonData, fieldName, wcContext);
	}
	if (YFCCommon.isVoid(userIdValue)) {
	    return;
	} else {
	    // Username entered. Hence validate if UserId already exists
	    boolean isUserIdValidated = UserProfileHelper
		    .displayUserIdAlreadyExists(userIdValue, wcContext);
	    if (!isUserIdValidated) {
		jsonData.add(new ValidationEntry(USER_NAME_UI_FIELD_NAME,
			getLocalizedErrorMessage("userId.exists",
				USER_RESOURCE_PACKAGE_STRING, wcContext)));
		return;
	    }
	    validateGenLoginid(userIdValue, jsonData);
	}

    }

    /**
     * Validate that the generated Loginid from the entered DisplayUserID is not
     * too long
     * 
     * @param displayUserId
     * @param jsonData
     * @return jsonData
     * 
     */

    private ValidationInfo validateGenLoginid(String displayUserId,
	    ValidationInfo jsonData) {
	if (displayUserId.length() <= 40) {
	    return jsonData;
	} else {
	    jsonData.add(new ValidationEntry(USER_NAME_UI_FIELD_NAME,
		    getLocalizedErrorMessage("too.Long",
			    USER_RESOURCE_PACKAGE_STRING, wcContext)));
	    return jsonData;
	}
	// For now not checking the length of Generated Loginid even if the
	// length check is passed, CustomerContactID length-check is not
	// happening
	// So, for now hard-coding the length-check to the DB-length of
	// CustomerContactID as it is much less than Db-length of Loginid minus
	// DB-Length of OrgSuffix
	// String maxLengthOrgSuffix =
	// ProfileUtility.getMaxLengthOrgSuffixForEnterprise(wcContext);
	// String genLoginid = displayUserId + "_" + maxLengthOrgSuffix;
	// String errorMessage =
	// WCValidationUtils.validateFieldValue("userName", genLoginid,
	// "Loginid", wcContext);
	// if(!(YFCCommon.isVoid(errorMessage))){
	// jsonData.add(new ValidationEntry("userName",
	// getLocalizedErrorMessage("too.Long", USER_RESOURCE_PACKAGE_STRING,
	// wcContext)));
	// }
	// return jsonData;
    }

}
