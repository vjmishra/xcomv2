package com.xpedx.nextgen.common.util;


import java.util.ArrayList;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfs.japi.YFSEnvironment;


public class XPXIsInternalUser implements YCPDynamicConditionEx {
	
	@Override
	public boolean evaluateCondition(YFSEnvironment arg0, String arg1,
			Map arg2, Document inputDoc) {
				
		Element orderElement = inputDoc.getDocumentElement();
		String userType = orderElement.getAttribute("Usertype");
		String extnUserType = null;
		ArrayList<Element> userElements=SCXmlUtil.getElements(orderElement, "Extn");
		if(userElements != null && userElements.size() > 0)
		{
			extnUserType=userElements.get(0).getAttribute("ExtnUserType");
		}			
		// Source Indicator has been checked for B2B (Source Indicator == 2)
		if((userType != null && userType.equals("INTERNAL")) || (extnUserType != null && extnUserType.equals("INTERNAL"))){		
			//Added to for debugging the log if the API call is being redirected here for INTERNAL USER creation
			System.out.println("Inside usertype and extnusertype ");
			return true;			
		}
		return false;
	}

	@Override
	public void setProperties(Map arg0) {
		// TODO Auto-generated method stub
		
	}

	

	

}
