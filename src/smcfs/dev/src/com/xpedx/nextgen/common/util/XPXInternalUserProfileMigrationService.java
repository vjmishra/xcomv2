package com.xpedx.nextgen.common.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.yantra.interop.japi.YIFApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFClientFactory;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFClientCreationException;

/**
 * @author asekhar-tw on 15-March-2011. This class will be invoked by the
 *         service XPXExternalUserProfileMigration
 */

public class XPXInternalUserProfileMigrationService {

	private static YIFApi api = null;
	private static YFCLogCategory yfcLogCatalog;

	static {
		try {
			api = YIFClientFactory.getInstance().getLocalApi();
		} catch (YIFClientCreationException e) {
			e.printStackTrace();
		}
		yfcLogCatalog = YFCLogCategory
				.instance(XPXInternalUserProfileMigrationService.class);
	}

	public Document isNewOrExistingUser(YFSEnvironment env, Document inXML)
			throws Exception {

		yfcLogCatalog.debug("----- isNewOrExistingUser -----");
		int userCount = 0;

		Document getUserListInput = null;
		Document userHierarchyInput = null;
		Document getUserListOutput = null;
		Document userHierarchyOutput = null;

		try {
			/*
			 * getUserListInput = YFCDocument.parse(SCXmlUtil.getString(inXML))
			 * .getDocument();
			 * getUserListInput.getDocumentElement().removeAttribute
			 * ("Password");
			 * 
			 * yfcLogCatalog.info("--- getUserListInput after Password removal: "
			 * + SCXmlUtil.getString(getUserListInput));
			 */

			yfcLogCatalog.info("inXML: " + SCXmlUtil.getString(inXML));

			String displayUserID = inXML.getDocumentElement().getAttribute(
					"DisplayUserID");

			yfcLogCatalog.info("--- DisplayUserID: " + displayUserID);

			getUserListInput = YFCDocument.createDocument("User").getDocument();
			Element userHeaderElement = getUserListInput.getDocumentElement();
			userHeaderElement.setAttribute("DisplayUserID", displayUserID);

			yfcLogCatalog.info("--- getUserListInput: "
					+ SCXmlUtil.getString(getUserListInput));

			getUserListOutput = api.invoke(env, XPXLiterals.GET_USER_LIST_API,
					getUserListInput);

			NodeList userList = getUserListOutput.getDocumentElement()
					.getElementsByTagName(XPXLiterals.USER);

			userCount = userList.getLength();
			yfcLogCatalog.info("userCount: " + userCount);

			userHierarchyInput = YFCDocument.parse(SCXmlUtil.getString(inXML))
					.getDocument();

			if (userCount > 0) {
				yfcLogCatalog.info("Calling modifyUserHierarchy...");
				userHierarchyOutput = api.invoke(env,
						XPXLiterals.MODIFY_USER_HIERARCHY_API,
						userHierarchyInput);
				yfcLogCatalog.info("modifyUserHierarchy call successful...");
			} else {
				yfcLogCatalog.info("Calling createUserHierarchy...");
				userHierarchyInput.getDocumentElement().setAttribute("Loginid",
						"");
				yfcLogCatalog
						.info("createUserHierarchy Input after Loginid rest: "
								+ SCXmlUtil.getString(userHierarchyInput));
				userHierarchyOutput = api.invoke(env,
						XPXLiterals.CREATE_USER_HIERARCHY_API,
						userHierarchyInput);
				yfcLogCatalog.info("createUserHierarchy call successful...");
			}

		} catch (NullPointerException ne) {
			//	yfcLogCatalog.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.USER_PROFILE_MIGRATION,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);

			/** Need to confirm if we need to throw or not **/
			 throw ne; 
		} catch (YFSException yfe) {
			//yfcLogCatalog.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.USER_PROFILE_MIGRATION,
					XPXLiterals.YFE_ERROR_CLASS, env, inXML);

			/** Need to confirm if we need to throw or not **/
			 throw yfe; 
		} catch (Exception e) {
			//yfcLogCatalog.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.USER_PROFILE_MIGRATION,
					XPXLiterals.E_ERROR_CLASS, env, inXML);

			
			/** Need to confirm if we need to throw or not **/
			 throw e; 
		}

		return userHierarchyOutput;
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
