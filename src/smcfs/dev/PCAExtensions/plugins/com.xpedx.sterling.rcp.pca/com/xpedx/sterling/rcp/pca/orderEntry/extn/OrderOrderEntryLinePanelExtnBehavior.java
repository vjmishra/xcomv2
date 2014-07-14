	
package com.xpedx.sterling.rcp.pca.orderEntry.extn;

/**
 * Created on May 26,2010
 *
 */
 
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.xpedx.sterling.rcp.pca.orderlines.screen.OrderLinesPanel;
import com.xpedx.sterling.rcp.pca.orderlines.wizard.XPXOrderLinesWizard;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCExtendedTableBindingData;
import com.yantra.yfc.rcp.YRCValidationResponse;
import com.yantra.yfc.rcp.YRCWizard;
import com.yantra.yfc.rcp.YRCWizardContext;
import com.yantra.yfc.rcp.YRCWizardExtensionBehavior;
/**
 * @author sdodda
 * Copyright © 2005-2009 Sterling Commerce, Inc. All Rights Reserved.
 */
 public class OrderOrderEntryLinePanelExtnBehavior extends YRCWizardExtensionBehavior {

	/**
	 * This method initializes the behavior class.
	 */
	public void init() {
		//TODO: Write behavior init here.
	}
 
 	
    public String getExtnNextPage(String currentPageId) {
		//TODO
		return null;
    }
    public YRCWizard createChildWizard(String wizardPageFormId, Composite
    		pnlRoot, YRCWizardContext wizardContext){
    		
    		return new XPXOrderLinesWizard(this.getOwnerForm() , SWT.NONE, this.getInputObject());
    }
    
    public IYRCComposite createPage(String pageIdToBeShown) {
    	//OrderLinesPanel page = null;
    	IYRCComposite page= null;
    	//"com.xpedx.sterling.rcp.pca.orderlines.wizard.XPXOrderLinesWizard"
		if (pageIdToBeShown.equalsIgnoreCase(OrderLinesPanel.FORM_ID)) {
			/**/
			
			page = new OrderLinesPanel(this.getOwnerForm(), SWT.NONE, 
						new YRCEditorInput(getModel("OrderDetails"), this.getInputObject(), new String[] {}, ((YRCEditorInput)this.getInputObject()).getTaskName()), 
						pageIdToBeShown,3, this.getModel("getCustomerDetails_wizard"));
		}
		return page;
	}
    
    public void pageBeingDisposed(String pageToBeDisposed) {
		//TODO
    }

    /**
     * Called when a wizard page is about to be shown for the first time.
     *
     */
    public void initPage(String pageBeingShown) {
		//TODO
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
    	// TODO Validation required for the following controls.
		
		// TODO Create and return a response.
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
