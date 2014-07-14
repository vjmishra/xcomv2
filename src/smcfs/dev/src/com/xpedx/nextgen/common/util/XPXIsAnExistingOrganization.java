/**
 * 
 */
package com.xpedx.nextgen.common.util;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author Arun Sekhar
 * 
 */
public class XPXIsAnExistingOrganization implements YCPDynamicConditionEx {

	private static YIFApi api = null;
	private static YFCLogCategory yfcLogCatalog;

	static {
		try {
			api = YIFClientFactory.getInstance().getLocalApi();
		} catch (YIFClientCreationException e) {
			e.printStackTrace();
		}
		yfcLogCatalog = YFCLogCategory
				.instance(XPXIsAnExistingOrganization.class);
	}

	@Override
	public boolean evaluateCondition(YFSEnvironment env, String arg1, Map arg2,
			Document inXML) {

		yfcLogCatalog.info("The input to the XPXIsAnExistingOrganization: "+ SCXmlUtil.getString(inXML));
		int orgCount = 0;
		String orgCode = null;
		boolean isAnExistingOrg = false;

		Document getOrganizationListInput = null;
		Document getOrganizationListOutput = null;

		try {
			Element inputElement = inXML.getDocumentElement();
			orgCode = SCXmlUtil.getXpathAttribute(inputElement,
					"./@OrganizationCode");

			getOrganizationListInput = YFCDocument.createDocument(
					"Organization").getDocument();
			Element orgHeaderElement = getOrganizationListInput
					.getDocumentElement();

			orgHeaderElement.setAttribute("OrganizationCode", orgCode);
			/**
			 * OrganizationKey corresponds to the OrganizationCode Fact. So
			 * setting the same value as that of OrganizationCode
			 **/
			orgHeaderElement.setAttribute("OrganizationKey", orgCode);

			getOrganizationListOutput = api.invoke(env,
					XPXLiterals.GET_ORGANIZATION_LIST_API,
					getOrganizationListInput);
			if(yfcLogCatalog.isDebugEnabled()){
				yfcLogCatalog.debug("getOrganizationListOutput: "+ SCXmlUtil.getString(getOrganizationListOutput));
			}
			NodeList organizationList = getOrganizationListOutput
					.getDocumentElement().getElementsByTagName(
							XPXLiterals.ORGANIZATION);

			orgCount = organizationList.getLength();
			yfcLogCatalog.info("orgCount: " + orgCount);
			if(yfcLogCatalog.isDebugEnabled()){
				yfcLogCatalog.debug("orgCount:" + orgCount);
			}
			if (orgCount > 0) {
				yfcLogCatalog.info("This is an existing Organization...");
				isAnExistingOrg = true;
				if(yfcLogCatalog.isDebugEnabled()){
					yfcLogCatalog.debug("isAnExistingOrg: " + isAnExistingOrg);
				}
			} else {
				yfcLogCatalog.info("This is a new Organization...");
				isAnExistingOrg = false;
				if(yfcLogCatalog.isDebugEnabled()){
					yfcLogCatalog.debug("isAnExistingOrg: " + isAnExistingOrg);
				}
			}

		} catch (NullPointerException ne) {
			yfcLogCatalog.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.DIVISION_PROFILE_MIGRATION,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);

			/** Need to confirm if we need to throw or not **/
			/* throw ne; */
		} catch (YFSException yfe) {
			yfcLogCatalog.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.DIVISION_PROFILE_MIGRATION,
					XPXLiterals.YFE_ERROR_CLASS, env, inXML);

			/** Need to confirm if we need to throw or not **/
			/* throw yfe; */
		} catch (Exception e) {
			yfcLogCatalog.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.DIVISION_PROFILE_MIGRATION,
					XPXLiterals.E_ERROR_CLASS, env, inXML);

			/** Need to confirm if we need to throw or not **/
			/* throw e; */
		}
		return isAnExistingOrg;
	}

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
	public void setProperties(Map arg0) {

	}
}
