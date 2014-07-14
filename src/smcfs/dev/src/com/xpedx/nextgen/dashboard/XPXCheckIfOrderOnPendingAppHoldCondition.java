package com.xpedx.nextgen.dashboard;

import java.util.Map;

import org.w3c.dom.Document;

import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXCheckIfOrderOnPendingAppHoldCondition implements YCPDynamicConditionEx{

	private static YFCLogCategory log;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}
	
	public boolean evaluateCondition(YFSEnvironment arg0, String arg1, Map arg2, Document getOrderDetailsOutput) {
		
		boolean isOrderOnHold = false;
		
		YFCDocument yfcDoc = YFCDocument.getDocumentFor(getOrderDetailsOutput);
		if (log.isDebugEnabled()) {
			log.debug("XPXCheckIfOrderOnPendingAppHoldCondition-InXML: " + yfcDoc.getString());
		}
		
		YFCElement rootElem = yfcDoc.getDocumentElement();
		YFCElement orderHoldTypesElem = rootElem.getChildElement(XPXLiterals.E_ORDER_HOLD_TYPES);
		if (orderHoldTypesElem != null) {
			YFCIterable<YFCElement> orderHoldItr = orderHoldTypesElem.getChildren(XPXLiterals.E_ORDER_HOLD_TYPE);
			while (orderHoldItr.hasNext()) {
				YFCElement orderHoldTypeElem = orderHoldItr.next();
				String holdType = orderHoldTypeElem.getAttribute(XPXLiterals.A_HOLD_TYPE);
	        	String holdStatus = orderHoldTypeElem.getAttribute(XPXLiterals.A_STATUS);
	        	if (log.isDebugEnabled()) {
	        		log.debug("holdType: "+holdType);
	        		log.debug("holdStatus: "+holdStatus);
				}	
	        	
	        	if(!YFCObject.isNull(holdType) && holdType.equalsIgnoreCase("ORDER_LIMIT_APPROVAL") 
	        			&& !YFCObject.isNull(holdStatus) && holdStatus.equalsIgnoreCase(XPXLiterals.HOLD_CREATED_STATUS_ID)) {
    		              isOrderOnHold = true;
    		              break;
    			}
			}
		}
        
		if (log.isDebugEnabled()) {
			log.debug("Is Order On Hold: " + isOrderOnHold);
		}
		return isOrderOnHold;
	}

	public void setProperties(Map arg0) {
		// TODO Auto-generated method stub
	}
}