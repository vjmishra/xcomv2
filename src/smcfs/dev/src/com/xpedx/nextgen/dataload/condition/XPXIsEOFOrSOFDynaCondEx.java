package com.xpedx.nextgen.dataload.condition;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXIsEOFOrSOFDynaCondEx implements YCPDynamicConditionEx {

	private static YFCLogCategory cat = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	private Properties _properties  = null;
	private static YIFApi api = null;
	
	/**
	 * Evaluates if the incoming message root element name is SOF/EOF or any thing else.
	 * In case of SOF invokes ManageFeedsService, which in turn RESUME or SUSPEND related services.
	 * 
	 * (non-Javadoc)
	 * @see com.yantra.ycp.japi.YCPDynamicConditionEx#evaluateCondition(com.yantra.yfs.japi.YFSEnvironment, java.lang.String, java.util.Map, org.w3c.dom.Document)
	 * 
	 */
	public boolean evaluateCondition(YFSEnvironment env, String name,
			Map map, Document inElem) {
		
		/** try-catch restructured and updated by Arun Sekhar on 25-Jan-2011 **/
		try{		
	        String strRoot = inElem.getDocumentElement().getNodeName();
	        if(cat.isDebugEnabled()){
	        	cat.debug("XPXIsEOFOrSOFDynaCondEx.evaluateCondition(...): Input Message:" + SCXmlUtil.getString(inElem)); 
	        }
	        String strElementNames = _properties.getProperty("ElementNames");
	        String[] strNames = strElementNames.split(",");
	        boolean isRootElementMatches=false;
	        for (String strElementName : strNames) {
	        	
				if(strRoot.equals(strElementName)){
					isRootElementMatches = true;
					break;
				}
			}
	        
	        String environment = _properties.getProperty("EnvironmentID");
	        	        
	        if(isRootElementMatches){
	        	
	        	// This is done to handle Start Of Feed message to disable/suspend dependent feed services.
	        	if(SCUtil.equals(strRoot, "SOF"))
	        	{
	        			if(cat.isDebugEnabled()){
	        	        	cat.debug("XPXIsEOFOrSOFDynaCondEx.evaluateCondition(...): Invoke ManageFeedService for SOF"); 
	        	        }
						api = YIFClientFactory.getInstance().getApi();
						
						
						if("Acc".equalsIgnoreCase(environment))
						{	
						   api.executeFlow(env, "AccManageFeedsService", inElem);
						}
						else if("Max".equalsIgnoreCase(environment))
						{
						   api.executeFlow(env, "MaxManageFeedsService", inElem);
						}
					
	        	}
	            
	        	return true;
	        }
		}catch (NullPointerException ne) {
			cat.error("NullPointerException: " + ne.getStackTrace());	
			prepareErrorObject(ne, XPXLiterals.PB_B_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inElem);	
			throw ne;
		}catch (YFSException yfe) {
			cat.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.PB_B_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inElem);
			throw yfe;
		}catch (RemoteException re) {
			cat.error("YFSException: " + re.getStackTrace());
			prepareErrorObject(re, XPXLiterals.PB_B_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inElem);
			try {
				throw re;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch (YIFClientCreationException yifce) {
			cat.error("YIFClientCreationException: " + yifce.getStackTrace());
			prepareErrorObject(yifce, XPXLiterals.PB_B_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inElem);
			try {
				throw yifce;
			} catch (YIFClientCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch (Exception e) {
			cat.fatal("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.PB_B_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inElem);
			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		/** Commented by Arun Sekhar as this is not required **/
/*        try
        {
            return false;
        }
        catch(Exception e)
        {
            cat.fatal(e);
        }*/
        return false;
	}

	/**@author asekhar-tw on 25-Jan-2011
	 * This method prepares the error object with the exception details which in turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML){
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();	
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}
	
	public void setProperties(Map map) {
		_properties = new Properties();
		_properties.putAll(map);

	}

}
