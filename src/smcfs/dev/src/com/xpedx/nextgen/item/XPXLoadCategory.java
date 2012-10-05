package com.xpedx.nextgen.item;

import java.util.HashMap;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.dashboard.XPXInvoiceAgent;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXLoadCategory implements YIFCustomApi {

	private YIFApi api = null;
	private static String _ATTR_GROUP_ID = "xpedx";
	private static String _ORG_CODE = "xpedx";

	/** YFCLogCategory Added by Arun.Sekhar on 21-Jan-2011 for  logging
	 */
	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public Document invoke(YFSEnvironment env, Document inXML)
	throws Exception
	{
		/**try-catch added by Arun.Sekhar on 21-Jan-2011
		 */
		Element eCategory = null;
		try{
			/** Modified from getApi() to getLocalApi() by Arun.Sekhar on 21-Jan-2011 for  logging
			 */
			api = YIFClientFactory.getInstance().getLocalApi();
			if(inXML != null){
				log.debug("Input XML for XPXLoadCategory : " + SCXmlUtil.getString(inXML));
			}
			
			Element eCategoryList = inXML.getDocumentElement();
			
			NodeList nlCategoryList = eCategoryList.getElementsByTagName("Category");
			
			if(nlCategoryList != null)
			{
				for(int j=0; j<nlCategoryList.getLength(); j++)
				{
					eCategory = (Element)nlCategoryList.item(j);
					NodeList nlAddnlAttrList = eCategory.getElementsByTagName("AdditionalAttributeList");
					if(nlAddnlAttrList != null && nlAddnlAttrList.getLength() > 0)
					{
						//the valid values for the attribute needs to be updated
						Element eAddnlAttrList = (Element)nlAddnlAttrList.item(0);
						NodeList nlAddnlAttrs = eAddnlAttrList.getElementsByTagName("AdditionalAttribute");
						
						for(int i=0; i<nlAddnlAttrs.getLength(); i++)
						{
							Element eAddnlAttr = (Element)nlAddnlAttrs.item(i);
							String strOp = eAddnlAttr.getAttribute("Operation");
							if("Delete".equalsIgnoreCase(strOp))
							{
								//for delete we donot need to update valid values
								continue;
							}
							
							String strAttrName = eAddnlAttr.getAttribute("Name");
							String strAttrValue = eAddnlAttr.getAttribute("Value");
							
							updateValidValueForAttribute(env, strAttrName, strAttrValue);
						}
					}
				}
				//CALL THE manageCategory API here and remove it from the XPXCategoryLoad service configuration
				inXML = api.invoke(env, "manageCategory", inXML);
			}
		}catch (NullPointerException ne) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(eCategory));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("NullPointerException: " + ne.getStackTrace());	
			prepareErrorObject(ne, XPXLiterals.CAT_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inXML);
			throw ne;
		} catch (YFSException yfe) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(eCategory));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.CAT_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inXML);	
			throw yfe;
		} catch (Exception e) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(eCategory));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.CAT_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inXML);
			throw e;
		}	
		return inXML;
	}
	
	/**@author asekhar-tw on 21-Jan-2011
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
	
	private void updateValidValueForAttribute(YFSEnvironment env, String sAttrName, String sItemAttrValue) throws Exception
	{
	
		HashMap hmValidAttrValue = new HashMap();
		if(log.isDebugEnabled()){
			log.debug("Setting " + sItemAttrValue + " as valid value for " + sAttrName);
		}
		YFCDocument getAttrListInDoc = YFCDocument.createDocument("Attribute");
		YFCElement eAttributeIn = getAttrListInDoc.getDocumentElement();
		eAttributeIn.setAttribute("AttributeDomainID", "ItemAttribute");
		eAttributeIn.setAttribute("AttributeGroupID", _ATTR_GROUP_ID);
		eAttributeIn.setAttribute("AttributeID", sAttrName);
		eAttributeIn.setAttribute("CallingOrganizationCode", _ORG_CODE);
		if(log.isDebugEnabled()){
			log.debug("Get Attribute List Input Document is :" + getAttrListInDoc.toString());
		}
		//set template for getAttributeList
		YFCDocument getAttrListTemp = YFCDocument.createDocument("AttributeList");
		YFCElement eAttrListTemp = getAttrListTemp.getDocumentElement();
		YFCElement eAttrTemp = getAttrListTemp.createElement("Attribute");
		eAttrListTemp.appendChild(eAttrTemp);
		
		eAttrTemp.setAttribute("AttributeID", "");
		eAttrTemp.setAttribute("AttributeKey", "");
		
		YFCElement eAttAllowedValueListTemp = getAttrListTemp.createElement("AttributeAllowedValueList");
		eAttrTemp.appendChild(eAttAllowedValueListTemp);
		
		YFCElement eAttAllowedValueTemp = getAttrListTemp.createElement("AttributeAllowedValue");
		eAttAllowedValueListTemp.appendChild(eAttAllowedValueTemp);
		
		eAttAllowedValueTemp.setAttribute("Value", "");
		eAttAllowedValueTemp.setAttribute("ShortDescription", "");
		if(log.isDebugEnabled()){
			log.debug("Document for attribute list is :" + getAttrListTemp.toString()); 
		}
		env.setApiTemplate("getAttributeList", getAttrListTemp.getDocument());
		
		Document docAttrList = api.invoke(env, "getAttributeList", getAttrListInDoc.getDocument());
		
		env.clearApiTemplate("getAttributeList");
		
		/*
		if("Y".equals(sVerbose))
		{*/
			//log.debug("docAttrList:" + YFCDocument.getDocumentFor(docAttrList));
		/*}*/
		
		Element eAttrListOut = docAttrList.getDocumentElement();
		Element eAttrOut = (Element)eAttrListOut.getElementsByTagName("Attribute").item(0);
		String sAttrKey = eAttrOut.getAttribute("AttributeKey");
		
		NodeList nlAttrValues = eAttrListOut.getElementsByTagName("AttributeAllowedValue");
		for(int i=0; i<nlAttrValues.getLength(); i++)
		{
			Element eAttrValueOut = (Element)nlAttrValues.item(i);
			String sAttrValue = eAttrValueOut.getAttribute("Value");
			String sAttrShortDesc = eAttrValueOut.getAttribute("ShortDescription");
			hmValidAttrValue.put(sAttrValue, sAttrShortDesc);
		}
		
		/*
		if("Y".equals(sVerbose))
		{*/
			//log.debug("hmValidAttrValue:" + hmValidAttrValue);
		/*}*/
		
		if(!hmValidAttrValue.containsKey(sItemAttrValue))
		{
			if(log.isDebugEnabled()){
				log.debug("Adding item attribute value : " + sItemAttrValue);
			}
			//update the attribute to add the valid value
			YFCDocument manageAttributeIn = YFCDocument.createDocument("AttributeList");
			YFCElement eAttributeListIn = manageAttributeIn.getDocumentElement();
			YFCElement eManageAttributeIn = manageAttributeIn.createElement("Attribute");
			eAttributeListIn.appendChild(eManageAttributeIn);
			eManageAttributeIn.setAttribute("AttributeKey", sAttrKey);
			
			YFCElement eAttrAllowedValueList = manageAttributeIn.createElement("AttributeAllowedValueList");
			eManageAttributeIn.appendChild(eAttrAllowedValueList);
			
			YFCElement eAttrAllowedValue = manageAttributeIn.createElement("AttributeAllowedValue");
			eAttrAllowedValueList.appendChild(eAttrAllowedValue);
			
			eAttrAllowedValue.setAttribute("LongDescription", sItemAttrValue);
			eAttrAllowedValue.setAttribute("ShortDescription", sItemAttrValue);
			eAttrAllowedValue.setAttribute("Value", sItemAttrValue);
			eAttrAllowedValue.setAttribute("Operation", "Manage");
			
			/*
			if("Y".equals(sVerbose))
			{*/
				//log.debug("manageAttributeIn:" + manageAttributeIn);
			/*}
			if("Y".equals(sExecuteAPI))
			{*/
				api.invoke(env, "manageAttribute", manageAttributeIn.getDocument());
			/*}*/
			
			
		}
	}
}
