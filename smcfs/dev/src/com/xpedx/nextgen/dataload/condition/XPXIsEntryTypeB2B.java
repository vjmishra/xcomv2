package com.xpedx.nextgen.dataload.condition;

/**
 * Description: Entry type has been checked for value 'B2B'.
 * 
 * @author Stanislaus Joseph John
 *
 */

import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXIsEntryTypeB2B implements YCPDynamicConditionEx {		

	@Override
	public boolean evaluateCondition(YFSEnvironment arg0, String arg1,
			Map arg2, Document inputDoc) {
				
		Element orderElement = inputDoc.getDocumentElement();		
		NodeList sourceIndicatorList = (NodeList)orderElement.getElementsByTagName("SourceIndicator");
		Node sourceIndicatorNode = (Node)sourceIndicatorList.item(0);
		String sourceIndicator = sourceIndicatorNode.getTextContent();				
		// Source Indicator has been checked for B2B (Source Indicator == 2)
		if(sourceIndicator != null && sourceIndicator.equals(XPXLiterals.SOURCE_INDICATOR_B2B)){			
			return true;			
		}
		return false;
	}

	@Override
	public void setProperties(Map arg0) {
		// TODO Auto-generated method stub
		
	}

	

	

}
