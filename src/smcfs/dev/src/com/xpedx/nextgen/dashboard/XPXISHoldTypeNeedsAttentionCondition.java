package com.xpedx.nextgen.dashboard;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXISHoldTypeNeedsAttentionCondition implements YCPDynamicConditionEx{

	private static YFCLogCategory log;
	private static YIFApi api = null;
	
	
	static {
		log = YFCLogCategory.instance(XPXCheckIfOrderOnHoldCondition.class);
		
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean evaluateCondition(YFSEnvironment env, String arg1, Map arg2, Document getOrderDetailsOutput)
	{
		
		boolean isOrderOnNeedsAttentionHold = false;

        Element getOrderDetailsOutputRoot = getOrderDetailsOutput.getDocumentElement();
        
        Element getOrderDetailsOutputOrderHoldTypes = (Element)getOrderDetailsOutputRoot.getElementsByTagName(XPXLiterals.E_ORDER_HOLD_TYPES).item(0);
        
        if(getOrderDetailsOutputOrderHoldTypes != null)
        {	
        
        NodeList getOrderDetailsOutputOrderHoldTypeList = getOrderDetailsOutputOrderHoldTypes.getElementsByTagName(XPXLiterals.E_ORDER_HOLD_TYPE);
        
        if(getOrderDetailsOutputOrderHoldTypeList.getLength() > 0)
        {
        
        for(int i=0; i < getOrderDetailsOutputOrderHoldTypeList.getLength(); i++)
        {
        	Element orderHoldType = (Element)getOrderDetailsOutputOrderHoldTypeList.item(i);
        	
        	String holdType = orderHoldType.getAttribute(XPXLiterals.A_HOLD_TYPE);
        	
        	String status = orderHoldType.getAttribute(XPXLiterals.A_STATUS);
        	
        	if((holdType.equalsIgnoreCase(XPXLiterals.NEEDS_ATTENTION_HOLD) && status.equalsIgnoreCase(XPXLiterals.HOLD_CREATED_STATUS_ID))
        			|| (getOrderDetailsOutputRoot.getAttribute(XPXLiterals.A_HAS_ERROR).equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_Y)))
        			{
        		              log.debug("The order is on hold or has an error---No chained order will be created");
        		              isOrderOnNeedsAttentionHold = true;  		              
        		              
        		              break;
        			}
        	

         }
        }

        }
        
        return isOrderOnNeedsAttentionHold;
	}
	public void setProperties(Map arg0) {
		// TODO Auto-generated method stub
		
	}
	
}