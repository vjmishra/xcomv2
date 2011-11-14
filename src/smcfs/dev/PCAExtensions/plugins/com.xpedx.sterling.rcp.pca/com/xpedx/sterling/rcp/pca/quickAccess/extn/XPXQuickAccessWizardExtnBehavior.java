	
package com.xpedx.sterling.rcp.pca.quickAccess.extn;

/**
 * Created on Jul 17,2010
 *
 */
 
import java.util.ArrayList;

import org.eclipse.swt.widgets.Link;

import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.YRCExtendedTableBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCValidationResponse;
import com.yantra.yfc.rcp.YRCWizardExtensionBehavior;
/**
 * @author jkotha
 * Copyright © 2005-2009 Sterling Commerce, Inc. All Rights Reserved.
 */
 public class XPXQuickAccessWizardExtnBehavior extends YRCWizardExtensionBehavior {

	/**
	 * This method initializes the behavior class.
	 */
	public void init() {

	}
 
 	
    public String getExtnNextPage(String currentPageId) {
		//TODO
		return null;
    }
    
    public IYRCComposite createPage(String pageIdToBeShown) {
		//TODO
		return null;
	}
    
    public void pageBeingDisposed(String pageToBeDisposed) {
		//TODO
    }

    /**
     * Called when a wizard page is about to be shown for the first time.
     *
     */
    public void initPage(String pageBeingShown) {
		if(!YRCPlatformUI
				.hasPermission(XPXConstants.RES_ID_DIVISION_SEARCH_ACTION))
		{
			disableField("extn_lnkDivisionSearch");
		}
		if(!YRCPlatformUI
				.hasPermission(XPXConstants.RES_ID_ARTICLES_SEARCH_ACTION))
		{
			disableField("extn_lnkManageArticles");
		}
		if(!YRCPlatformUI
				.hasPermission(XPXConstants.RES_ID_MANAGE_MY_ITEMS))
		{
			disableField("extn_lnkManageMyItems");
		}
		if(!YRCPlatformUI
				.hasPermission(XPXConstants.RES_ID_REF_ORDER_DEARCH))
		{
			disableField("extn_lnkRefOrderSearch");
		}
		
    }
    
	/**
	 * Method for validating the text box.
     */
    public YRCValidationResponse validateTextField(String fieldName, String fieldValue) {
    	// TODO Validation required for the following controls.
		
		// TODO Create and return a response.
		return super.validateTextField(fieldName, fieldValue);
	}
    
    /**
     * Method for validating the combo box entry.
     */
    public void validateComboField(String fieldName, String fieldValue) {
    	// TODO Validation required for the following controls.
		
		// TODO Create and return a response.
		super.validateComboField(fieldName, fieldValue);
    }
    
    /**
     * Method called when a button is clicked.
     */
    public YRCValidationResponse validateButtonClick(String fieldName) {
    	// TODO Validation required for the following controls.
		
		// TODO Create and return a response.
		return super.validateButtonClick(fieldName);
    }
    
    /**
     * Method called when a link is clicked.
     */
	public YRCValidationResponse validateLinkClick(String fieldName) {
		if ("extn_lnkDivisionSearch".equals(fieldName)){
			YRCPlatformUI.fireAction("com.xpedx.sterling.rcp.pca.divisionMaintainance.task.action.XPXDivisionMaintainanceAction");
		} else if ("extn_lnkManageArticles".equals(fieldName)){
			YRCPlatformUI.fireAction("com.xpedx.sterling.rcp.pca.articles.task.action.XPXArticleMaintainanceAction");
		} else if("extn_lnkRefOrderSearch".equals(fieldName)){
			YRCPlatformUI.fireAction("com.xpedx.sterling.rcp.pca.referenceorder.task.action.XPXReferenceOrderSearchUIAction");
		} else if("extn_lnkManageMyItems".equals(fieldName)){
				YRCPlatformUI.fireAction("com.xpedx.sterling.rcp.pca.myitems.task.action.XPXMyItemsSearchListUIAction");
		}
		return super.validateLinkClick(fieldName);
	}
	
	/**
	 * Create and return the binding data for advanced table columns added to the tables.
	 */
	 public YRCExtendedTableBindingData getExtendedTableBindingData(String tableName, ArrayList tableColumnNames) {
	 	// Create and return the binding data definition for the table.
		
	 	// The defualt super implementation does nothing.
	 	return super.getExtendedTableBindingData(tableName, tableColumnNames);
	 }

}
//TODO Validation required for a Link control: extn_lnkDivisionSearch