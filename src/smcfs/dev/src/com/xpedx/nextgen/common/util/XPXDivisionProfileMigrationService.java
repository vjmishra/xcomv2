/**
 * 
 */
package com.xpedx.nextgen.common.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author asekhar-tw
 * 
 */
public class XPXDivisionProfileMigrationService {

	private static YIFApi api = null;
	private static YFCLogCategory yfcLogCatalog;

	static {
		try {
			api = YIFClientFactory.getInstance().getLocalApi();
		} catch (YIFClientCreationException e) {
			e.printStackTrace();
		}
		yfcLogCatalog = YFCLogCategory
				.instance(XPXDivisionProfileMigrationService.class);
	}

	public Document isNewOrExistingOrganization(YFSEnvironment env,
			Document inXML) throws Exception {

		yfcLogCatalog
				.info("***************************************************************************");
		yfcLogCatalog
				.info("Inside XPXDivisionProfileMigrationService :: isNewOrExistingOrganization()");
		yfcLogCatalog.info("Input to the API: " + SCXmlUtil.getString(inXML));
		yfcLogCatalog
				.info("***************************************************************************");

		int orgCount = 0;
		String orgCode = null;
		boolean isAnExistingOrg = false;

		Document getOrganizationListInput = null;
		Document getOrganizationListOutput = null;
		Document manageOrgHierarchyOutput = null;
		Element inputElement =  null;
		try {
			inputElement = inXML.getDocumentElement();
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

			yfcLogCatalog.info("getOrganizationListInput: "
					+ SCXmlUtil.getString(getOrganizationListInput));

			getOrganizationListOutput = api.invoke(env,
					XPXLiterals.GET_ORGANIZATION_LIST_API,
					getOrganizationListInput);

			yfcLogCatalog.info("getOrganizationListOutput: "
					+ SCXmlUtil.getString(getOrganizationListOutput));

			NodeList organizationList = getOrganizationListOutput
					.getDocumentElement().getElementsByTagName(
							XPXLiterals.ORGANIZATION);

			orgCount = organizationList.getLength();
			yfcLogCatalog.info("orgCount: " + orgCount);

			if (orgCount > 0) {
				yfcLogCatalog.info("This is an existing Organization...");
				isAnExistingOrg = true;
				yfcLogCatalog.info("isAnExistingOrg: " + isAnExistingOrg);
			} else {
				yfcLogCatalog.info("This is a new Organization...");
				isAnExistingOrg = false;
				yfcLogCatalog.info("isAnExistingOrg: " + isAnExistingOrg);
			}
			if (isAnExistingOrg) {
				manageOrgHierarchyOutput = api.invoke(env,
						XPXLiterals.MANAGE_ORGANIZATION_HIERARCHY, inXML);
				yfcLogCatalog
						.info("manageOrganizationHierarchy call successfully done the update for the existing Node...");
			} else {
				Element eleCorpPersonInfo = SCXmlUtil.createChild(
						inXML.getDocumentElement(), "CorporatePersonInfo");
				// inXML.appendChild(eleCorpPersonInfo);
				yfcLogCatalog
						.info("Input XML after appending CorporatePersonInfo element: "
								+ SCXmlUtil.getString(inXML));
				manageOrgHierarchyOutput = api.invoke(env,
						XPXLiterals.MANAGE_ORGANIZATION_HIERARCHY, inXML);
				yfcLogCatalog
						.info("manageOrganizationHierarchy call successfully created the new Node...");
			}
		} catch (NullPointerException ne) {
			yfcLogCatalog.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			yfcLogCatalog.error(SCXmlUtil.getString(inputElement));
			yfcLogCatalog.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			yfcLogCatalog.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.DIVISION_PROFILE_MIGRATION,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);

			/** Need to confirm if we need to throw or not **/
			/* throw ne; */
		} catch (YFSException yfe) {
			yfcLogCatalog.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			yfcLogCatalog.error(SCXmlUtil.getString(inputElement));
			yfcLogCatalog.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			yfcLogCatalog.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.DIVISION_PROFILE_MIGRATION,
					XPXLiterals.YFE_ERROR_CLASS, env, inXML);

			/** Need to confirm if we need to throw or not **/
			/* throw yfe; */
		} catch (Exception e) {
			yfcLogCatalog.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			yfcLogCatalog.error(SCXmlUtil.getString(inputElement));
			yfcLogCatalog.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			yfcLogCatalog.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.DIVISION_PROFILE_MIGRATION,
					XPXLiterals.E_ERROR_CLASS, env, inXML);

			/** Need to confirm if we need to throw or not **/
			/* throw e; */
		}
		return manageOrgHierarchyOutput;
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

}
