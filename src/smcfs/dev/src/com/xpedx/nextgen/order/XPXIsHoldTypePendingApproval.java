package com.xpedx.nextgen.order;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXIsHoldTypePendingApproval implements YCPDynamicConditionEx {

	@Override
	public boolean evaluateCondition(YFSEnvironment env, String name,
			Map mapdata, Document doc) {
		boolean isHoldTypeApproval = false;
		String pendingApprovalHoldType = (String)_properties.get(HOLD_TYPE_PROP);// This returns the Hold type of Pending Approval
		if(!SCUtil.isVoid(pendingApprovalHoldType) && !SCUtil.isVoid(doc)) {
			Element orderHoldType = doc.getDocumentElement();
			String holdTypeOnOrder = orderHoldType.getAttribute(XPXLiterals.A_HOLD_TYPE);
			String holdStatusOnOrder = orderHoldType.getAttribute(XPXLiterals.A_STATUS);
			if(!"1300".equals(holdStatusOnOrder)) //Order Approved Email won't be sent on changeOrder API call as its a part of Order Approval Service now.
			{
				if(!SCUtil.isVoid(holdTypeOnOrder)) {
					if(SCUtil.equals(holdTypeOnOrder, pendingApprovalHoldType))
						isHoldTypeApproval = true;
				}
			}
		}
		return isHoldTypeApproval;
	}

	@Override
	public void setProperties(Map props) {
		_properties = props;

	}
	public static final String HOLD_TYPE_PROP = "PendingApprovalHoldType";
	private Map _properties;

}
