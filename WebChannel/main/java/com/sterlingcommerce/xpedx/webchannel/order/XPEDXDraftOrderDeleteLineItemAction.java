package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.sterlingcommerce.webchannel.order.CartInContextRefreshingWCMashupAction;
import com.sterlingcommerce.webchannel.order.DraftOrderDeleteLineItemAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class XPEDXDraftOrderDeleteLineItemAction extends CartInContextRefreshingWCMashupAction
{
	
	

    protected List<String> selectedLineItem = null;
    protected List<String> deleteActions = null;
    protected List<String> deleteQuantities = null;
    protected String errorMsg = null;
    
    protected static final String ERROR_CODE_CANCELLATION_CONFLICT = "YFS85_0020";
    
    protected static final String RETURN_CANCELLATION_CONFLICT = "pendingChangeConflict";
    
    public String execute()
    {
        String returnValue = ERROR;
        XPEDXWCUtils.setYFSEnvironmentVariablesForDiscounts(getWCContext());
        try
        {
            returnValue = super.execute();
            if(ERROR.equals(returnValue))
            {
                String errorString = getErrorCodeFromErrorXML(getExecuteErrorXML());
                if(ERROR_CODE_CANCELLATION_CONFLICT.equals(errorString))
                {
                    errorMsg = getText("YFS85_0020_error");
                    XPEDXWCUtils.releaseEnv(wcContext);
                    return RETURN_CANCELLATION_CONFLICT;
                }
            }
        }
        catch(Exception e)
        {
            LOG.error(e);
            XPEDXWCUtils.releaseEnv(wcContext);
            return ERROR;
        }
        finally
        {
        	XPEDXWCUtils.releaseEnv(wcContext);
        }

		
        return returnValue;
    }


    public List<String> getSelectedLineItem() {
        return selectedLineItem;
    }
    
    public void setSelectedLineItem(List<String> selectedLineItem) throws Exception {
        this.selectedLineItem = selectedLineItem;
        
        // Need to build a list of delete actions equal to the number
        // of items being deleted.
        String deleteAction = getOrderLineDeleteAction();
        deleteActions = new ArrayList(selectedLineItem.size());
        // Workaround for Bug 203977 - when in pending changes mode (i.e. for a confirmed
        // order) need to specify a quantity of "0" instead of an action of "CANCEL".
        String deleteQuantity = getOrderLineDeleteQuantity();
        deleteQuantities = new ArrayList(selectedLineItem.size());
        Iterator iter = selectedLineItem.iterator();
        while(iter.hasNext())
        {
            iter.next();
            deleteActions.add(deleteAction);
            deleteQuantities.add(deleteQuantity);
        }
    }
    
    public List<String> getDeleteActions() {
        return deleteActions;
    }
    
    public List<String> getDeleteQuantities() {
        return deleteQuantities;
    }

    public String getErrorMessage()
    {
        return errorMsg;
    }
	
/*	public String execute()
    {
       //Change method to set only discount calculate true since pna is not required while applying on coupons.
		XPEDXWCUtils.setYFSEnvironmentVariablesForDiscounts(getWCContext());
		
		String retVal=super.execute();
		XPEDXWCUtils.releaseEnv(wcContext);
		return retVal;
    }*/
	
	/*private void setYFSEnvironmentVariables() 
	{
		
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("isPnACall", "true");
			map.put("isDiscountCalculate", "true");
			XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext(), map);
	}*/
}
