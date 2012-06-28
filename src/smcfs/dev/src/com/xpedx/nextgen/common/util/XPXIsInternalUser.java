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
		
		System.out.println("Hello I am in evaluateCondition");
				
		Element orderElement = inputDoc.getDocumentElement();
		System.out.println("orderElement -----"+SCXmlUtil.getString(orderElement));
		String userType = orderElement.getAttribute("Usertype");
		System.out.println("userType ------------"+userType);
		String extnUserType = null;
		ArrayList<Element> userElements=SCXmlUtil.getElements(orderElement, "Extn");
		System.out.println("UserElements ---------"+userElements.size());
		if(userElements != null && userElements.size() > 0)
		{
			extnUserType=userElements.get(0).getAttribute("ExtnUserType");
		}			
		// Source Indicator has been checked for B2B (Source Indicator == 2)
		if((userType != null && userType.equals("INTERNAL")) || (extnUserType != null && extnUserType.equals("INTERNAL"))){		
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
