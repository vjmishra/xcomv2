package com.xpedx.nextgen.dashboard;

import java.util.Map;

import org.w3c.dom.Document;

import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXCheckIfOrderOnHoldCondition implements YCPDynamicConditionEx {

	private static YFCLogCategory log;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}
	
	public boolean evaluateCondition(YFSEnvironment arg0, String arg1, Map arg2, Document inXML) {
		
		boolean isOrderOnHold = false;
		
		YFCDocument yfcDoc = YFCDocument.getDocumentFor(inXML);
		if (log.isDebugEnabled()) {
			log.debug("XPXCheckIfOrderOnHoldCondition-InXML: "+yfcDoc.getString());
		}
		
		YFCElement rootElem = yfcDoc.getDocumentElement();
		YFCElement orderHoldTypesElem = rootElem.getChildElement(XPXLiterals.E_ORDER_HOLD_TYPES);
		if (orderHoldTypesElem != null) {
			YFCIterable<YFCElement> holdTypeItr = orderHoldTypesElem.getChildren(XPXLiterals.E_ORDER_HOLD_TYPE);
			while (holdTypeItr.hasNext()) {
				YFCElement holdTypeElem = holdTypeItr.next();
				String holdType = holdTypeElem.getAttribute(XPXLiterals.A_HOLD_TYPE);
	        	String status = holdTypeElem.getAttribute(XPXLiterals.A_STATUS);
	        	if (log.isDebugEnabled()) {
	        		log.debug("holdType: "+holdType);
		        	log.debug("holdStatus: "+status);
	        	}
	        	
	        	if((holdType.equalsIgnoreCase(XPXLiterals.NEEDS_ATTENTION_HOLD) && status.equalsIgnoreCase(XPXLiterals.HOLD_CREATED_STATUS_ID))
	        			|| ("Y".equalsIgnoreCase(rootElem.getAttribute(XPXLiterals.A_HAS_ERROR)))
	        			//Added as part of JIRA # 1627
	        				|| (holdType.equalsIgnoreCase("ORDER_LIMIT_APPROVAL") && status.equalsIgnoreCase(XPXLiterals.HOLD_CREATED_STATUS_ID))) {
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
