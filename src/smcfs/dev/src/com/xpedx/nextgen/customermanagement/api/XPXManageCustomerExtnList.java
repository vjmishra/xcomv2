package com.xpedx.nextgen.customermanagement.api;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXManageCustomerExtnList implements YIFCustomApi {
	private static YFCLogCategory log;
	private static YIFApi api = null;
	private Properties arg0;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {
			log.error("API initialization error");
		}
	}
	
	
	public Document manageCustomerExtnList(YFSEnvironment env, Document inXML) {
		if (log.isDebugEnabled()) {
			log.debug("XPXManageCustomerExtnList-InXML: "+ SCXmlUtil.getString(inXML));
		}
		System.out.println("XPXManageCustomerExtnList-InXML: "+ SCXmlUtil.getString(inXML));
		Document outXmlDoc=null;
		try { 
			YFCDocument yfcDoc = YFCDocument.getDocumentFor(inXML);
			YFCElement rootEle = yfcDoc.getDocumentElement();
			
			YFCIterable<YFCElement> yfcItr=rootEle.getChildren("XPXCustomerExtnList");
			while (yfcItr.hasNext()) {
				YFCElement emailListChildEle = (YFCElement)yfcItr.next();
				
				YFCDocument custExtnListInDoc = YFCDocument.getDocumentFor("<XPXCustomerExtnList/>");
				YFCElement custExtnListInEle=custExtnListInDoc.getDocumentElement();				
				
				String operation=emailListChildEle.getAttribute("Operation");
				
				if("Create".equals(operation)) {
					
					String emailAddress=emailListChildEle.getAttribute("ExtnListValue");					
					if(!YFCObject.isVoid(emailAddress)) {
						custExtnListInEle.setAttribute("CustomerKey", emailListChildEle.getAttribute("CustomerKey"));
						custExtnListInEle.setAttribute("ExtnListType", emailListChildEle.getAttribute("ExtnListType"));					
						custExtnListInEle.setAttribute("ExtnListValue", emailAddress);
						if (log.isDebugEnabled()) {
							log.debug("XPXCreateCustomerExtnList-InXML: "+ SCXmlUtil.getString(custExtnListInDoc.getDocument()));
						}
								
						outXmlDoc = api.executeFlow(env, "XPXCreateCustomerExtnList", custExtnListInDoc.getDocument());
								
						if (log.isDebugEnabled()) {
							log.debug("XPXCreateCustomerExtnList-OutXML: "+ SCXmlUtil.getString(outXmlDoc));
						}
					}
					//outXMLDoc = insertCustomerExtnList(env, rootEle, custExtnListInEle, emailList);									
					
				} else if ("Delete".equals(operation)) {
					custExtnListInEle.setAttribute("CustomerExtnListKey", emailListChildEle.getAttribute("CustomerExtnListKey"));
					if (log.isDebugEnabled()) {
						log.debug("XPXDeleteCustomerExtnList-IXML: "+ SCXmlUtil.getString(custExtnListInDoc.getDocument()));
					}
					
					outXmlDoc=api.executeFlow(env, "XPXDeleteCustomerExtnList", custExtnListInDoc.getDocument());
					
					if (log.isDebugEnabled()) {
						log.debug("XPXDeleteCustomerExtnList-OutXML: "+ SCXmlUtil.getString(outXmlDoc));
					}					
					
				}
			}
		}catch (YFSException yfe) {
    		yfe.printStackTrace();
    		prepareErrorObject(yfe, XPXLiterals.CUSTOMER_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inXML);
    		return inXML;
		} catch(Exception e) {
			e.printStackTrace();
			prepareErrorObject(e, XPXLiterals.CUSTOMER_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inXML);
	        return inXML;
		}
		
		if (log.isDebugEnabled()) {
			log.debug("XPXManageCustomerExtnList-OutXML: "+ SCXmlUtil.getString(outXmlDoc));
			
		}
		return outXmlDoc;
	
	}
	
	private Document insertCustomerExtnList(YFSEnvironment env, YFCElement rootEle, YFCElement custExtnListInEle, String emailList) throws YFSException, RemoteException{
		Document outXMLDoc1=null;
		
		custExtnListInEle.setAttribute("ExtnListType", rootEle.getAttribute("ExtnListType"));					
		
		String[] addnlEmailList = emailList.split(";");
		if(addnlEmailList!=null) {
			for (int i = 0; i < addnlEmailList.length; i++) {
				custExtnListInEle.setAttribute("ExtnListValue", addnlEmailList[i]);
				if (log.isDebugEnabled()) {
					log.debug("XPXCreateCustomerExtnList-InXML: "+ SCXmlUtil.getString(custExtnListInEle.getOwnerDocument().getDocument()));
				}
				
				outXMLDoc1 = api.executeFlow(env, "XPXCreateCustomerExtnList", custExtnListInEle.getOwnerDocument().getDocument());
				
				if (log.isDebugEnabled()) {
					log.debug("XPXCreateCustomerExtnList-OutXML: "+ SCXmlUtil.getString(outXMLDoc1));
				}
			}
		}
		
		return outXMLDoc1;
	}

	/**
	 * This method prepares the error object with the exception details which in
	 * turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML) {
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		errorObject.setExceptionMessage(e.getMessage());
		ErrorLogger.log(errorObject, env);
	}
	
	@Override
	public void setProperties(Properties arg0) throws Exception {
		this.arg0 = arg0;

	}
	
}
