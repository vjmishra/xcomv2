package com.xpedx.nextgen.dashboard;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXCheckIfOrderOnHoldCondition implements YCPDynamicConditionEx{

	private static YFCLogCategory log;
//	Removed API call object creation as it is not being used.
//	private static YIFApi api = null;
	
	
	static {
		log = YFCLogCategory.instance(XPXCheckIfOrderOnHoldCondition.class);
		
//		try {
//			api = YIFClientFactory.getInstance().getApi();
//		} catch (YIFClientCreationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	
	public boolean evaluateCondition(YFSEnvironment arg0, String arg1, Map arg2, Document getOrderDetailsOutput) {
		
		boolean isOrderOnHold = false;
		
		log.debug("The input to XPXCheckIfOrderOnHoldCondition is: "+SCXmlUtil.getString(getOrderDetailsOutput));

        Element getOrderDetailsOutputRoot = getOrderDetailsOutput.getDocumentElement();
        
        NodeList getOrderDetailsOutputOrderHoldTypes = getOrderDetailsOutputRoot.getElementsByTagName(XPXLiterals.E_ORDER_HOLD_TYPES);
	       for (int i=0; i<getOrderDetailsOutputOrderHoldTypes.getLength(); i++)
	       {
	    	   Element orderHoldTypesEle = (Element) getOrderDetailsOutputOrderHoldTypes.item(i);
	    	   
	    	   if(orderHoldTypesEle.getParentNode().getNodeName().equalsIgnoreCase("Order"))
	    	   {
	    		   NodeList orderHoldTypeList = orderHoldTypesEle.getElementsByTagName("OrderHoldType");
	    		   if(orderHoldTypeList.getLength() > 0)
			        {
			        
			           for(int j=0; j < orderHoldTypeList.getLength(); j++) 
			          {
			        	Element orderHoldType = (Element)orderHoldTypeList.item(j);
			        	
			        	String holdType = orderHoldType.getAttribute(XPXLiterals.A_HOLD_TYPE);
			        	log.debug("The hold type is: "+holdType);
			        	
			        	String status = orderHoldType.getAttribute(XPXLiterals.A_STATUS);
			        	log.debug("The hold status is: "+status);
			        	
			        	if((holdType.equalsIgnoreCase(XPXLiterals.NEEDS_ATTENTION_HOLD) && status.equalsIgnoreCase(XPXLiterals.HOLD_CREATED_STATUS_ID))
			        			|| (getOrderDetailsOutputRoot.getAttribute(XPXLiterals.A_HAS_ERROR).equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_Y))
			        			//Added as part of JIRA # 1627
			        			|| (holdType.equalsIgnoreCase("ORDER_LIMIT_APPROVAL") && status.equalsIgnoreCase(XPXLiterals.HOLD_CREATED_STATUS_ID)))
			        			{
			        		              log.debug("The order is on hold or has an error---No chained order will be created");
			        		              isOrderOnHold = true;
			        		              break;
			        			}
			        	
			        }
			        
			        }
	    	   }
	       }
        
        log.debug("The order is on hold: "+isOrderOnHold);
        //isOrderOnHold=true;
		return isOrderOnHold;
	}

	public void setProperties(Map arg0) {
		// TODO Auto-generated method stub
		
	}

}
