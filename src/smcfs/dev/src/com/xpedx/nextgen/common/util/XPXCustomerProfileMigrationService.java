/**
 * @author Arun Sekhar on 25-April-2011
 */
package com.xpedx.nextgen.common.util;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.rmi.RemoteException;
import com.yantra.interop.japi.YIFApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFClientFactory;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.yantra.interop.japi.YIFClientCreationException;

/**
 * 
 */
public class XPXCustomerProfileMigrationService {

	private static YIFApi api = null;
	private static YFCLogCategory yfcLogCatalog;

	static {
		try {
			api = YIFClientFactory.getInstance().getLocalApi();
		} catch (YIFClientCreationException e) {
			e.printStackTrace();
		}
		yfcLogCatalog = YFCLogCategory
				.instance(XPXCustomerProfileMigrationService.class);
	}

	/**
	 * This method will manage scenarios where the input XML is having an empty
	 * value for the RuleID attribute as well as when it is having a non-empty
	 * value for the same
	 **/
	public Document manageRuleID(YFSEnvironment env, Document inXML)
			throws Exception {

		yfcLogCatalog
				.info("***************************************************************************");
		yfcLogCatalog
				.info("Inside XPXCustomerProfileMigrationService :: manageRuleID()");
		yfcLogCatalog.debug("Input to the custom API: "
				+ SCXmlUtil.getString(inXML));
		yfcLogCatalog
				.info("***************************************************************************");

		String ruleID = null;
		String ruleKey = null;
		boolean ruleDefnExists = false;
		Document ruleDefinitionListOutput = null;

		try {
			Element inputElement = inXML.getDocumentElement();

			Element extnEle = (Element) inputElement.getElementsByTagName(
					"Extn").item(0);
			NodeList customerRulesProfileList = extnEle
					.getElementsByTagName("XPXCustomerRulesProfile");
			int rulesProfCount = customerRulesProfileList.getLength();

			for (int i = 0; i < rulesProfCount; i++) {
				Element custRulesProfileEle = (Element) customerRulesProfileList
						.item(i);
				ruleID = custRulesProfileEle.getAttribute("RuleID");
				
				if (ruleID == null || ruleID.trim().length() == 0) {
					yfcLogCatalog.debug("Input document doesn't have any value for the attribute 'RuleID'");
				} else {
					/** Call the service 'XPXGetRuleDefnList' **/
					yfcLogCatalog.debug("RuleID: " + ruleID);
					ruleDefinitionListOutput = getRuleDefinitionList(env,
							ruleID);

					/**
					 * Check if the RuleID definition exists in the
					 * XPX_RULE_DEFN table
					 **/
					ruleDefnExists = checkIfRuleDefExistsInDB(ruleDefinitionListOutput);
				}
				if (ruleDefnExists) {
					/**
					 * Get the RuleKey corresponding to the RuleID in the input
					 * XML and stamp it to the input doc
					 **/
					ruleKey = getRuleKey(ruleDefinitionListOutput);
					custRulesProfileEle.setAttribute("RuleKey", ruleKey);
				} else {
					// Do nothing
				}
			}
		} catch (NullPointerException ne) {
			yfcLogCatalog.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.CUSTOMER_PROFILE_MIGRATION,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);

			/** Need to confirm if we need to throw or not **/
			/* throw ne; */
		} catch (YFSException yfe) {
			yfcLogCatalog.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.CUSTOMER_PROFILE_MIGRATION,
					XPXLiterals.YFE_ERROR_CLASS, env, inXML);

			/** Need to confirm if we need to throw or not **/
			/* throw yfe; */
		} catch (Exception e) {
			yfcLogCatalog.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.CUSTOMER_PROFILE_MIGRATION,
					XPXLiterals.E_ERROR_CLASS, env, inXML);

			/** Need to confirm if we need to throw or not **/
			/* throw e; */
		}
		return inXML;
	}

	/**
	 * This method will return the rule definition document for the RuleID
	 * passed
	 **/
	public Document getRuleDefinitionList(YFSEnvironment env, String ruleID)
			throws YFSException, RemoteException {
		Document getRuleDefnInputDoc = YFCDocument
				.createDocument("XPXRuleDefn").getDocument();
		getRuleDefnInputDoc.getDocumentElement().setAttribute("RuleID", ruleID);
		yfcLogCatalog.debug("The input xml to XPXGetRuleDefnList is: " + SCXmlUtil.getString(getRuleDefnInputDoc));
		Document getRuleDefnOutputDoc = api.executeFlow(env,
				"XPXGetRuleDefnList", getRuleDefnInputDoc);
		yfcLogCatalog.debug("The output xml of XPXGetRuleDefnList is: "+ SCXmlUtil.getString(getRuleDefnOutputDoc));

		return getRuleDefnOutputDoc;
	}

	/**
	 * Validate the XPXGetRuleDefnList output to see if it has a valid
	 * definition element and will set the 'ruleDefExists' flag accordingly.
	 **/
	public boolean checkIfRuleDefExistsInDB(Document ruleDefinitionListOutput) {

		int ruleDefnCount = 0;
		boolean ruleDefExists = false;

		NodeList ruleDefnList = ruleDefinitionListOutput.getDocumentElement()
				.getElementsByTagName(XPXLiterals.RULE_DEFINITION);

		ruleDefnCount = ruleDefnList.getLength();
		
		if (ruleDefnCount > 0) {
			yfcLogCatalog.debug("ruleDefnCount: " + ruleDefnCount);
			ruleDefExists = true;
			yfcLogCatalog.debug("ruleDefExists: " + ruleDefExists);
		} else {
			yfcLogCatalog.debug("Rule definition for this RuleID does NOT exist in the DataBase.");
			ruleDefExists = false;
			yfcLogCatalog.debug("ruleDefExists: " + ruleDefExists);
		}
		return ruleDefExists;
	}

	/**
	 * This method will get the RuleKey corresponding to the RuleID of the
	 * current 'XPXRuleDefn' element
	 **/
	public String getRuleKey(Document ruleDefinitionListOutput) {

		String ruleKey = "";
		Element ruleDefnEle = (Element) ruleDefinitionListOutput
				.getDocumentElement()
				.getElementsByTagName(XPXLiterals.RULE_DEFINITION).item(0);
		ruleKey = ruleDefnEle.getAttribute("RuleKey");
		if(!YFCObject.isNull(ruleKey) && !YFCObject.isVoid(ruleKey)){
			yfcLogCatalog.debug("ruleKey: " + ruleKey);
		}
		return ruleKey;
	}

	/** Method for CENT tool integration **/
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
