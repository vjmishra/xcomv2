package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.order.CartInContextRefreshingWCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class XPEDXDraftOrderDeleteLineItemAction extends CartInContextRefreshingWCMashupAction
{
    protected List<String> selectedLineItem = null;
    protected List<String> deleteActions = null;
    protected List<String> deleteQuantities = null;
    protected String errorMsg = null;
    private static final Logger log = Logger.getLogger(WCMashupAction.class);
    protected static final String ERROR_CODE_CANCELLATION_CONFLICT = "YFS85_0020";
    public static final String CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ = "changeOrderAPIOutputForOrderLinesModification";
    protected static final String RETURN_CANCELLATION_CONFLICT = "pendingChangeConflict";
    
    public String execute()
    {
    	Document outputDocument;
    	String returnValue = ERROR;
        XPEDXWCUtils.setYFSEnvironmentVariablesForDiscounts(getWCContext());
        try
        {
        	System.out.println("ORDER HEADER KEY : "+cicRefreshingMashupOrderHeaderKey); 
        	try {
        		for(int i=0;i<selectedLineItem.size();i++){ 
        			System.out.println("selectd line items : "+selectedLineItem.get(i));
        		}
        		
        		Map<String, Element> out = prepareAndInvokeMashups();
     			/*Begin - Changes made by Mitesh Parikh for JIRA#3595*/
     			outputDocument = (Document)out.get("xpedx_me_draftOrderDeleteLineItems").getOwnerDocument();
     			getWCContext().getSCUIContext().getSession().setAttribute(CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ, outputDocument);
     			/*End - Changes made by Mitesh Parikh for JIRA#3595*/                 
                 returnValue = SUCCESS;
             }
             catch(Throwable t) {
                 log.error("Error in prepareAndInvokeMashups invocation", t);
                 executeException = t;
                 returnValue= ERROR;
             }
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
    
    protected void manipulateMashupInputs(Map mashupInputs)
    throws com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException
	{
    	Element inputOrderElem = (Element)mashupInputs.get("xpedx_me_draftOrderDeleteLineItems");
    	if(inputOrderElem == null)
	        throw new com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException("Cannot locate InputXML for xpedx_me_draftOrderDeleteLineItems mashup for manipulation");
    	
    	inputOrderElem.setAttribute("OrderHeaderKey", cicRefreshingMashupOrderHeaderKey);
    	Element orderLineEle=SCXmlUtil.getXpathElement(inputOrderElem, "OrderLines/OrderLine");
    	orderLineEle.setAttribute("Action", deleteActions.get(0));
    	orderLineEle.setAttribute("OrderLineKey", selectedLineItem.get(0));
	    
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
