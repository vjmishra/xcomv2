/**
 * 
 */
package com.xpedx.nextgen.api;

import java.rmi.RemoteException;
import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author asekhar-tw on 23-Jan-2010. This class will be invoked by the
 *         XPXManageAttributeWrapper service
 */
public class InvokeManageAttributeAPI {

	/** API object. */
	private static YIFApi api = null;

	/** YFCLogCategory object */
	private static YFCLogCategory yfcLogCatalog;

	static {
		yfcLogCatalog = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException yifce) {
			yifce.printStackTrace();
		}
	}

	public Document invokeManageAttribute(YFSEnvironment env, Document inXML)
			throws Exception {
		Document outXML = null;
		try {
			outXML = api.invoke(env, "manageAttribute", inXML);
		} catch (RemoteException re) {
			yfcLogCatalog.error("NullPointerException: " + re.getStackTrace());
			prepareErrorObject(re, XPXLiterals.ATTRB_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			throw re;
		} catch (NullPointerException ne) {
			yfcLogCatalog.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.ATTRB_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			throw ne;
		} catch (YFSException yfe) {
			yfcLogCatalog.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.ATTRB_TRANS_TYPE,
					XPXLiterals.YFE_ERROR_CLASS, env, inXML);
			throw yfe;
		} catch (Exception e) {
			yfcLogCatalog.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.ATTRB_TRANS_TYPE,
					XPXLiterals.E_ERROR_CLASS, env, inXML);
			throw e;
		}
		return outXML;
	}

	/**
	 * @This method prepares the error object with the exception details which
	 *       in turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType,
			String errorClass, YFSEnvironment env, Document inXML) {
		yfcLogCatalog.debug("CENT_Logging_ErrorObject:"+ SCXmlUtil.getString(inXML));
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}
}
