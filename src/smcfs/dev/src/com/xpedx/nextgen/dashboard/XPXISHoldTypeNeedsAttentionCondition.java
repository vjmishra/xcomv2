package com.xpedx.nextgen.dashboard;

import java.util.Map;

import org.w3c.dom.Document;

import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXISHoldTypeNeedsAttentionCondition implements
		YCPDynamicConditionEx {

	private static YFCLogCategory log;
	private static YIFApi api = null;
	
	static {
		log = (YFCLogCategory) YFCLogCategory
				.getLogger("com.xpedx.nextgen.log");

		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean evaluateCondition(YFSEnvironment env, String arg1, Map arg2,
			Document getOrderDetailsOutput) {

		boolean isOrderOnNeedsAttentionHold = false;
		YFCDocument yfcOrderDetailDoc = YFCDocument.getDocumentFor(getOrderDetailsOutput);
		YFCElement rootElem = yfcOrderDetailDoc.getDocumentElement();
		YFCElement orderHoldTypesElem = rootElem
				.getChildElement(XPXLiterals.E_ORDER_HOLD_TYPES);
		if (orderHoldTypesElem != null) {
			YFCIterable<YFCElement> holdTypeItr = orderHoldTypesElem.getChildren(XPXLiterals.E_ORDER_HOLD_TYPE);
			while (holdTypeItr.hasNext()) {
				YFCElement orderHoldTypeElem = holdTypeItr.next();
				if (orderHoldTypeElem != null) {
					String holdType = orderHoldTypeElem
							.getAttribute(XPXLiterals.A_HOLD_TYPE);
					String status = orderHoldTypeElem
							.getAttribute(XPXLiterals.A_STATUS);

					if ((holdType.equalsIgnoreCase(XPXLiterals.NEEDS_ATTENTION_HOLD)  && 
						 status.equalsIgnoreCase(XPXLiterals.HOLD_CREATED_STATUS_ID)) || 
						(rootElem.getAttribute(XPXLiterals.A_HAS_ERROR).equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_Y))) 
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