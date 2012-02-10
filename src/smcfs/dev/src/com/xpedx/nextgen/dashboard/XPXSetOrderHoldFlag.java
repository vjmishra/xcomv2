package com.xpedx.nextgen.dashboard;

import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXSetOrderHoldFlag implements YIFCustomApi
{
	private static YFCLogCategory log;
	private static YIFApi api = null;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}
	
	
	public Document setOrderHoldFlag(YFSEnvironment env, Document inputXML)
	{
		boolean isOrderOnHold = false;
        Element getOrderDetailsOutputRoot = inputXML.getDocumentElement();   
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
        		              isOrderOnHold = true;
        		              break;
        			}
		        }        
	        }
        }  
        log.debug("The order is on hold: "+isOrderOnHold);
		
        // To set the order hold flag in transaction object.
        if(isOrderOnHold){
        	getOrderDetailsOutputRoot.setAttribute("OrderHoldFlag", "Y");
        } else {
        	getOrderDetailsOutputRoot.setAttribute("OrderHoldFlag", "N");
        }
        
		return inputXML;
	}


	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}	

}