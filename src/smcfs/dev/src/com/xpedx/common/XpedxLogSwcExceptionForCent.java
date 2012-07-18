package com.xpedx.common;
	import java.text.DateFormat;
	import java.text.SimpleDateFormat;
	import java.util.Properties;

	import org.w3c.dom.Document;
	import org.w3c.dom.Element;
	import org.w3c.dom.Node;
	import org.w3c.dom.NodeList;

	import com.sterlingcommerce.baseutil.SCXmlUtil;
	import com.xpedx.nextgen.common.cent.ErrorLogger;
	import com.xpedx.nextgen.common.util.XPXLiterals;
	import com.xpedx.nextgen.dashboard.CallDBSequence;
	import com.yantra.interop.japi.YIFApi;
	import com.yantra.interop.japi.YIFClientCreationException;
	import com.yantra.interop.japi.YIFClientFactory;
	import com.yantra.interop.japi.YIFCustomApi;
	import com.yantra.yfc.dom.YFCDocument;
	import com.yantra.yfc.log.YFCLogCategory;
	import com.yantra.yfs.japi.*;

import java.sql.*;


	public class XpedxLogSwcExceptionForCent implements YIFCustomApi 
	{
		private static YIFApi api = null;

		/** Added by Anil Agarwal on 17-feb-2012 for logging **/
		private static YFCLogCategory log;		
		static
		{
			log = YFCLogCategory.instance(XpedxLogSwcExceptionForCent.class);		
		}
		public void setProperties(Properties arg0) throws Exception {
			// TODO Auto-generated method stub

		}	
		static
		{
			try {
				api = YIFClientFactory.getInstance().getApi();
			} catch (YIFClientCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@SuppressWarnings("finally")
		public Document getExceptionLogged(YFSEnvironment env,Document inXML) throws Exception
	    {
			/** try-catch block added by Anil Agarwal on 17-feb-2012 for CENT tool logging **/
			try{
								
			
				throw new Exception(SCXmlUtil.getString(inXML));
				
		        }	       
			catch (NullPointerException ne) {
				log.error("NullPointerException: " + ne.getStackTrace());	
				prepareErrorObject(ne, XPXLiterals.SWC_ORDER_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inXML);	
				throw ne;
			} catch (YFSException yfe) {
				log.error("YFSException: " + yfe.getStackTrace());
				prepareErrorObject(yfe, XPXLiterals.SWC_ORDER_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inXML);	
				throw yfe;
			} catch (Exception e) {
				log.error("Exception: " + e.getStackTrace());
				prepareErrorObject(e, XPXLiterals.SWC_ORDER_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inXML);
				throw e;
			}
			finally
			{
				return inXML;
			}
				
	    }
		
		
		/**@author Aagarw1 on 17-feb-2012
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
		
		
		
	}

