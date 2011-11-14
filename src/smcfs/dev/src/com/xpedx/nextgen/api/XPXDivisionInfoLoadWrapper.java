/**
 * 
 */
package com.xpedx.nextgen.api;

import java.rmi.RemoteException;
import org.w3c.dom.Document;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author asekhar-tw on 27-Jan-2010. This class will be invoked by the
 *         XPXDivisionInfoLoadWrapper service
 */
public class XPXDivisionInfoLoadWrapper {

	/** API object. */
	private static YIFApi api = null;

	/** YFCLogCategory object */
	private static YFCLogCategory yfcLogCatalog;

	static {
		yfcLogCatalog = YFCLogCategory
				.instance(XPXDivisionInfoLoadWrapper.class);
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException yifce) {
			yifce.printStackTrace();
		}
	}

	public Document invokeXPXDivisionInfoLoad(YFSEnvironment env, Document inXML)
			throws Exception {
		Document outXML = null;
		try {
			outXML = api.executeFlow(env, "XPXDivisionInfoLoad", inXML);
		} catch (RemoteException re) {
			yfcLogCatalog.error("NullPointerException: " + re.getStackTrace());
			prepareErrorObject(re, XPXLiterals.DIV_B_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			throw re;
		} catch (NullPointerException ne) {
			yfcLogCatalog.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.DIV_B_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			throw ne;
		} catch (YFSException yfe) {
			yfcLogCatalog.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.DIV_B_TRANS_TYPE,
					XPXLiterals.YFE_ERROR_CLASS, env, inXML);
			throw yfe;
		} catch (Exception e) {
			yfcLogCatalog.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.DIV_B_TRANS_TYPE,
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
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}
}
