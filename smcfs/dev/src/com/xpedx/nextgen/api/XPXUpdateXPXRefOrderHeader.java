package com.xpedx.nextgen.api;

import java.rmi.RemoteException;
import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author Stanislaus Joseph John.
 *  Updates the Is_Etrading_Invalid flag in the XPX_REF_ORDER_HDR table.
 */
public class XPXUpdateXPXRefOrderHeader implements YIFCustomApi {

	/** API object. */
	private static YIFApi api = null;

	/** YFCLogCategory object */
	private static YFCLogCategory yfcLogCatalog;

	static {
		yfcLogCatalog = YFCLogCategory
				.instance(XPXUpdateXPXRefOrderHeader.class);
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException yifce) {
			yifce.printStackTrace();
		}
	}

	public Document updateEtradingId(YFSEnvironment env, Document inXML)
			throws Exception {
		Document outXML = null;
		// Retrieving the Reference Order Header Key from the transaction object.
		// Transaction obect has been set in XPXCreateReferenceOrderAPI.java
		String refOrderHeaderKey = env.getTxnObject("ReferenceOrderHeaderKey").toString();
		if(refOrderHeaderKey != null){
		Document inputDoc = YFCDocument.createDocument("XPXRefOrderHdr").getDocument();
		Element xpxRefOrderHdrElement = inputDoc.getDocumentElement();
		xpxRefOrderHdrElement.setAttribute("RefOrderHdrKey", refOrderHeaderKey);
		xpxRefOrderHdrElement.setAttribute("IsInvalidETradingID", "Y");
		
		try {
			// To update the 'IsInvalidETradingID' flag in XPXRefOrderHdr table.
			outXML = api.executeFlow(env, "changeXPXRefOrderHdr", inputDoc);
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
		}else{
			// Error logged in CENT tool.
			YFSException exceptionMessage = new YFSException();
			exceptionMessage.setErrorDescription("Reference Order Header Key is empty in XPXUpdateXPXRefOrderHeader.java");		
			yfcLogCatalog.error("Reference Order Header Key is empty in XPXUpdateXPXRefOrderHeader.java");				
			throw exceptionMessage;				
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

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
