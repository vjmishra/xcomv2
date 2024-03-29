package com.xpedx.nextgen.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.shared.dbclasses.PLT_User_Login_FailDBHome;
import com.yantra.shared.ycp.YFSContext;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dblayer.PLTQueryBuilder;
import com.yantra.yfc.dblayer.PLTQueryBuilderHelper;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author sdodda
 *
 *         Holds Project Util methods
 *
 */
public class XPXUtils implements YIFCustomApi {

	private static final String GLOBAL_TEMPLATE_API_GET_ORGANIZATION_LIST_XPX_GET_ARTICLE_DETAILS_SERVICE = "global/template/api/getOrganizationList.XPXGetArticleDetailsService.xml";
	private static final String getCustomerListTemplate = "global/template/api/getCustomerList.XPXB2BDraftOrderCreationService.xml";
	private static final String propertyFile="/xpedx/sterling/Foundation/properties/centExempt.properties";

	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	private static final XPathFactory xPathFactory = XPathFactory.newInstance();

	private static YIFApi api = null;
	private static Map<String,String> uomIncomingXrefMap;
	private static Map<String,String> uomOutgoingXrefMap;
	static {
		try {
			api = YIFClientFactory.getInstance().getApi();

			uomIncomingXrefMap = new HashMap<String, String>();
			uomOutgoingXrefMap = new HashMap<String, String>();

		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Properties _properties = null;

	@Override
	public void setProperties(Properties properties) throws Exception {
		_properties = properties;
	}

	public Document postOrderManageMsgToLegacy(YFSEnvironment env,
			Document inXML) throws Exception {
		// check if the Environment/Input document passed is null, throw
		// exception
		if (env == null) {
			// Throw new YFSException with the description
			throw new YFSException("YFSEnvironment cannot be null or invalid to XPXCreateFulfillmentOrderAPI.invoke()");
		}
		if (inXML == null) {
			// Throw new YFSException with the description
			throw new YFSException("Input XML document cannot be null or invalid to XPXCreateFulfillmentOrderAPI.invoke()");
		}

		// api = YIFClientFactory.getInstance().getApi();
		log.debug("XPXUtils.postOrderManageMsgToLegacy() Input XML: " + SCXmlUtil.getString(inXML));
		if (true)
			throw new YFSException("");

		log.debug("XPXUtils.postOrderManageMsgToLegacy() Output XML: " + SCXmlUtil.getString(inXML));
		// api = null;
		return inXML;
	}

	/**
	 * Input XML: <br/>
	 *
	 * <pre>
	 * &lt;Root ServiceName='' ApiName=''&gt;
	 * &lt;Node ..../&gt;
	 * &lt;Node ..../&gt;
	 * &lt;Node ..../&gt;
	 * ...
	 * &lt;/Root&gt;
	 * </pre>
	 *
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document loadDataUsingMutliApi(YFSEnvironment env, Document inXML)
			throws Exception {

		if (env == null) {
			// Throw new YFSException with the description
			throw new YFSException(
					"YFSEnvironment cannot be null or invalid to XPXUtils.loadDataUsingMutliApi()");
		}
		if (inXML == null) {
			// Throw new YFSException with the description
			throw new YFSException(
					"Input XML document cannot be null or invalid to XPXUtils.loadDataUsingMutliApi()");
		}
		log.debug("XPXUtils.loadDataUsingMutliApi(): Input XML: - "
				+ SCXmlUtil.getString(inXML));

		String strServiceName = inXML.getDocumentElement().getAttribute(
				"ServiceName");
		String strApiName = inXML.getDocumentElement().getAttribute("ApiName");
		String strAPIAttrName = null;
		String strAPIAttrValue = null;
		if (SCUtil.isVoid(strApiName) && SCUtil.isVoid(strServiceName)) {
			throw new YFSException(
					"Invalid Input XML to XPXUtils.loadDataUsingMutliApi(): Either ApiName or ServiceName is required.");
		} else if (!SCUtil.isVoid(strApiName) && !SCUtil.isVoid(strServiceName)) {
			throw new YFSException(
					"Invalid Input XML to XPXUtils.loadDataUsingMutliApi(): In either ApiName or ServiceName Attributes only one is mandatory.");
		}
		if (!SCUtil.isVoid(strApiName)) {
			strAPIAttrName = "Name";
			strAPIAttrValue = strApiName;
		} else if (!SCUtil.isVoid(strServiceName)) {
			strAPIAttrName = "FlowName";
			strAPIAttrValue = strServiceName;
		}

		List<Element> nl = SCXmlUtil
				.getChildrenList(inXML.getDocumentElement());
		Document docMultiApi = SCXmlUtil.createDocument("MultiApi");
		for (Element eleChild : nl) {
			Element eleApi = SCXmlUtil.createChild(
					docMultiApi.getDocumentElement(), "API");
			eleApi.setAttribute(strAPIAttrName, strAPIAttrValue);
			Element eleInput = SCXmlUtil.createChild(eleApi, "Input");
			SCXmlUtil.importElement(eleInput, eleChild);
		}

		// api = YIFClientFactory.getInstance().getApi();
		log.debug("XPXUtils.loadDataUsingMutliApi(): Input XML for 'multiApi':- "
				+ SCXmlUtil.getString(docMultiApi));
		Document docMultiApiOutput = api.invoke(env, "multiApi", docMultiApi);
		log.debug("XPXUtils.loadDataUsingMutliApi(): Output XML:- "
				+ SCXmlUtil.getString(docMultiApiOutput));
		// api = null;
		return docMultiApiOutput;
	}

	/**
	 * In case of Start Of Feed, RESUME Starting FeedLoad, SUSPEND all the
	 * dependent Feeds.<br>
	 * In case of End Of Feed, RESUME dependent feeds \n <br>
	 * Service Arguments expected:- <br>
	 * IntegrationServerName:<ServerId> <br>
	 * SOFElementNames: SOF [If this is changed create a SOF Services Enable and
	 * Disable arguments too] <br>
	 * EOFElementNames: EOF,EOFDivisions,....,etc <br>
	 * In case of SOF:
	 * <SOFRootName>ServicesToEnable:<ServiceId1>,<ServiceId2>,...,<ServiceIdn>
	 * <SOFRootName>ServicesToDisable:<ServiceId1>,<ServiceId2>,...,<ServiceIdn> <br>
	 * In case of EOF:
	 * <EOFRootName>ServicesToEnable:<ServiceId1>,<ServiceId2>,...,<ServiceIdn>
	 * <EOFRootName>ServicesToDisable:<ServiceId1>,<ServiceId2>,...,<ServiceIdn>
	 *
	 *
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document manageFeedServices(YFSEnvironment env, Document inXML)
			throws Exception {

		/**
		 * try-catch block added and other modifications done by Arun Sekhar on
		 * 24-Jan-2011
		 */
		String messageAttrbs[] = null;
		String TRANS_TYPE = null;
		boolean rootElementMatches = false;
		try {
			/**
			 * @author asekhar-tw on 11-Jan-2011 @ modified by Prasanth Nair Log
			 *         the message into CENT showing that this is an EOF/SOF
			 *         message
			 *
			 *         Need to first validate which feed in coming in and them
			 *         pass the literals accordingly strRoot is used to identify
			 *         and switch Load Service.
			 */

			String strRoot = inXML.getDocumentElement().getNodeName();
			if(log.isDebugEnabled()){
				log.debug("The Root Node in manageFeedServices of XPXUtils is: " + strRoot);
			}
			messageAttrbs = validateIncomingMessage(env, inXML, strRoot);
			TRANS_TYPE = messageAttrbs[0];
			prepareEOFOrSOFMsgObject(TRANS_TYPE, messageAttrbs[1],
					messageAttrbs[2], env, inXML);
			// check if the Environment/Input document passed is null, throw
			// exception
			if (env == null) {
				// Throw new YFSException with the description
				throw new YFSException(
						"YFSEnvironment cannot be null or invalid to XPXCreateFulfillmentOrderAPI.invoke()");
			}
			if (inXML == null) {
				// Throw new YFSException with the description
				throw new YFSException(
						"Input XML document cannot be null or invalid to XPXCreateFulfillmentOrderAPI.invoke()");
			}
			log.debug("XPXUtils.postOrderManageMsgToLegacy()--> Input XML"
					+ SCXmlUtil.getString(inXML));

			/** Commented by Arun Sekhar as it is done above **/
			// String strRoot = inXML.getDocumentElement().getNodeName();
			if (null != _properties) {

				ArrayList<String> eofElementNames = new ArrayList<String>();
				eofElementNames.add(_properties.getProperty("EOFCustXref"));
				eofElementNames.add(_properties.getProperty("EOFCustomer"));
				eofElementNames.add(_properties.getProperty("EOFEntitlement"));
				eofElementNames.add(_properties.getProperty("EOFDivision"));
				eofElementNames.add(_properties.getProperty("EOFItemBranch"));
				eofElementNames.add(_properties.getProperty("EOFPriceBook"));
				eofElementNames.add(_properties.getProperty("EOFUom"));
				eofElementNames.add(_properties.getProperty("EOFMisc"));

				// String sofElementNames =
				// _properties.getProperty("SOFElementsNames");

				/*
				 * if (contains(arraySOFElementNames, strRoot) ||
				 * contains(arrayEOFElementNames, strRoot)) {
				 * this.processEOFOrSOFMessage(env, inXML); }
				 */

				if ("SOF".equals(strRoot)) {
					rootElementMatches = true;
				} else {
					for (int i = 0; i < eofElementNames.size(); i++) {
						String eofRootNames = eofElementNames.get(i);

						String[] eofRootNamesList = eofRootNames.split(",");
						for (String eofRootName : eofRootNamesList) {
							if (eofRootName.equals(strRoot)) {
								rootElementMatches = true;
								break;
							}
						}
					}
				}

				if (rootElementMatches) {
					this.processEOFOrSOFMessage(env, inXML);
				} else {
					log.debug("The root element does not match with the root names configured for the service");
				}

			}
			log.debug("XPXUtils.postOrderManageMsgToLegacy()--> Output XML"
					+ SCXmlUtil.getString(inXML));

		} catch (NullPointerException ne) {
			log.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env,
					inXML);
		} catch (YFSException yfe) {
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS,
					env, inXML);
		} catch (Exception e) {
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env,
					inXML);
		}
		return inXML;
	}

	/**
	 *
	 * @param env
	 * @param inXML
	 * @throws YIFClientCreationException
	 * @throws YFSException
	 * @throws RemoteException
	 */

	private void processEOFOrSOFMessage(YFSEnvironment env, Document inXML)
			throws YIFClientCreationException, YFSException, RemoteException {

		// api = YIFClientFactory.getInstance().getApi();
		// strRoot is used to identify and switch Load Service.
		String strRoot = inXML.getDocumentElement().getNodeName();

		String feedName = "";

		if (strRoot.equalsIgnoreCase("SOF")) {
			feedName = inXML.getDocumentElement().getAttribute("FeedName");
		} else {
			feedName = inXML.getDocumentElement().getAttribute(
					"YantraMessageGroupID");
		}
		if(log.isDebugEnabled()){
		log.debug("Processing " + strRoot + " Message:"
				+ SCXmlUtil.getString(inXML));
		}
		// Create a list of all the SOF and EOF feedNames

		/*
		 * ArrayList<String> sofServiceNames = new ArrayList<String>();
		 * sofServiceNames.add(_properties.getProperty("SOFCustomer"));
		 */

		String strServerName = _properties.getProperty("IntegrationServerName");
		String strServerId = null;
		if (!SCUtil.isVoid(strServerName)) {
			strServerId = this.getServerIdForName(env, strServerName);

			if (!SCUtil.isVoid(strServerId)) {

				// Prepare modifyServer API input XML.
				// Create Server Root element and stamp the ServerId
				Document docInput = SCXmlUtil.createDocument("Server");
				docInput.getDocumentElement().setAttribute("Id", strServerId);

				// Append Resume Services which are required to be marked as
				// Active

				if (strRoot.equalsIgnoreCase("SOF")) {

					String strServiceToEnable = _properties
							.getProperty(feedName);
					Element eleService = SCXmlUtil.createChild(
							docInput.getDocumentElement(), "Service");
					eleService.setAttribute("Action", "Resume");
					eleService.setAttribute("Name", strServiceToEnable);

					// Append Suspend all dependent Services on the starting
					// feeds

					String strFeedsToDisable = _properties
							.getProperty("ListOfFeeds");
					String[] arrayFeedsToDisable = strFeedsToDisable.split(",");
					for (String strFeedToDisable : arrayFeedsToDisable) {

						if (!strFeedToDisable.equalsIgnoreCase(feedName)) {
							String strServiceToDisable = _properties
									.getProperty(strFeedToDisable);
							if (strServiceToDisable != null
									&& strServiceToDisable.trim().length() > 0) {
								Element eleServiceToDisable = SCXmlUtil
										.createChild(
												docInput.getDocumentElement(),
												"Service");
								eleServiceToDisable.setAttribute("Action",
										"Suspend");
								eleServiceToDisable.setAttribute("Name",
										strServiceToDisable);
							}
						}
					}

				} else {
					// Resume all the services if its an EOF message
					String strFeedsToEnable = _properties
							.getProperty("ListOfFeeds");
					String[] arrayFeedsToEnable = strFeedsToEnable.split(",");
					for (String strFeedToEnable : arrayFeedsToEnable) {

						if (!strFeedToEnable.equalsIgnoreCase(feedName)) {

							String strServiceToEnable = _properties
									.getProperty(strFeedToEnable);
							if (strServiceToEnable != null
									&& strServiceToEnable.trim().length() > 0) {
								Element eleServiceToEnable = SCXmlUtil
										.createChild(
												docInput.getDocumentElement(),
												"Service");
								eleServiceToEnable.setAttribute("Action",
										"Resume");
								eleServiceToEnable.setAttribute("Name",
										strServiceToEnable);
							}
						}
					}

				}
				if(log.isDebugEnabled()){
					log.debug("ModifyServer API Input XML of XPXUtils is :" + SCXmlUtil.getString(docInput));
				}
				// Invoke modifyServer API
				if (docInput.getDocumentElement().hasChildNodes()) {
					api.invoke(env, "modifyServer", docInput);
				} else {
					log.debug("No service id found in Service Arguments to disable or enable: Root Element Name-"
							+ strRoot + "LoadIdentifier" + strRoot);
				}

				// Logic to apply modifyCache

				Document modifyCacheInput = YFCDocument.createDocument(
						"CachedGroups").getDocument();
				Element cachedGroup = modifyCacheInput
						.createElement("CachedGroup");
				cachedGroup.setAttribute("Name", "Database");
				modifyCacheInput.getDocumentElement().appendChild(cachedGroup);

				if ("SOF".equalsIgnoreCase(strRoot)) {
					if (feedName.equalsIgnoreCase("Customer")) {

						String customerFeedCachedObjects = _properties
								.getProperty("CustomerFeedCachedObjectsList");
						String[] cachedObjectsList = customerFeedCachedObjects
								.split(",");
						for (String cachedObjectProperty : cachedObjectsList) {
							Element cachedObject = modifyCacheInput
									.createElement("CachedObject");
							cachedObject.setAttribute("Action", "MODIFY");
							cachedObject.setAttribute("Enabled", "N");
							cachedObject.setAttribute("Class", _properties
									.getProperty(cachedObjectProperty));
							cachedGroup.appendChild(cachedObject);
						}

					}

					else if (feedName.equalsIgnoreCase("Division")) {
						String DivisionFeedCachedObjects = _properties
								.getProperty("DivisionFeedCachedObjectsList");
						String[] cachedObjectsList = DivisionFeedCachedObjects
								.split(",");
						for (String cachedObjectProperty : cachedObjectsList) {
							Element cachedObject = modifyCacheInput
									.createElement("CachedObject");
							cachedObject.setAttribute("Action", "MODIFY");
							cachedObject.setAttribute("Enabled", "N");
							cachedObject.setAttribute("Class", _properties
									.getProperty(cachedObjectProperty));
							cachedGroup.appendChild(cachedObject);
						}
					}

					else if (feedName.equalsIgnoreCase("Entitlement")) {
						String EntitlementFeedCachedObjects = _properties
								.getProperty("EntitlementFeedCachedObjectsList");
						String[] cachedObjectsList = EntitlementFeedCachedObjects
								.split(",");
						for (String cachedObjectProperty : cachedObjectsList) {
							Element cachedObject = modifyCacheInput
									.createElement("CachedObject");
							cachedObject.setAttribute("Action", "MODIFY");
							cachedObject.setAttribute("Enabled", "N");
							cachedObject.setAttribute("Class", _properties
									.getProperty(cachedObjectProperty));
							cachedGroup.appendChild(cachedObject);
						}
					}

					else if (feedName.equalsIgnoreCase("PriceBook")) {
						String PriceBookFeedCachedObjects = _properties
								.getProperty("PriceBookFeedCachedObjectsList");
						String[] cachedObjectsList = PriceBookFeedCachedObjects
								.split(",");
						for (String cachedObjectProperty : cachedObjectsList) {
							Element cachedObject = modifyCacheInput
									.createElement("CachedObject");
							cachedObject.setAttribute("Action", "MODIFY");
							cachedObject.setAttribute("Enabled", "N");
							cachedObject.setAttribute("Class", _properties
									.getProperty(cachedObjectProperty));
							cachedGroup.appendChild(cachedObject);
						}
					}

					else if (feedName.equalsIgnoreCase("Uom")) {
						String PriceBookFeedCachedObjects = _properties
								.getProperty("UomFeedCachedObjectsList");
						String[] cachedObjectsList = PriceBookFeedCachedObjects
								.split(",");
						for (String cachedObjectProperty : cachedObjectsList) {
							Element cachedObject = modifyCacheInput
									.createElement("CachedObject");
							cachedObject.setAttribute("Action", "MODIFY");
							cachedObject.setAttribute("Enabled", "N");
							cachedObject.setAttribute("Class", _properties
									.getProperty(cachedObjectProperty));
							cachedGroup.appendChild(cachedObject);
						}
					}

				} else {
					// Its an EOF message and so enable ="Y" for all the
					// disabled cached objects

					if (feedName.equalsIgnoreCase("Customer")) {

						String customerFeedCachedObjects = _properties
								.getProperty("CustomerFeedCachedObjectsList");
						String[] cachedObjectsList = customerFeedCachedObjects
								.split(",");
						for (String cachedObjectProperty : cachedObjectsList) {
							Element cachedObject = modifyCacheInput
									.createElement("CachedObject");
							cachedObject.setAttribute("Action", "MODIFY");
							cachedObject.setAttribute("Enabled", "Y");
							cachedObject.setAttribute("Class", _properties
									.getProperty(cachedObjectProperty));
							cachedGroup.appendChild(cachedObject);
						}

					}

					else if (feedName.equalsIgnoreCase("Division")) {
						String DivisionFeedCachedObjects = _properties
								.getProperty("DivisionFeedCachedObjectsList");
						String[] cachedObjectsList = DivisionFeedCachedObjects
								.split(",");
						for (String cachedObjectProperty : cachedObjectsList) {
							Element cachedObject = modifyCacheInput
									.createElement("CachedObject");
							cachedObject.setAttribute("Action", "MODIFY");
							cachedObject.setAttribute("Enabled", "Y");
							cachedObject.setAttribute("Class", _properties
									.getProperty(cachedObjectProperty));
							cachedGroup.appendChild(cachedObject);
						}
					}

					else if (feedName.equalsIgnoreCase("Entitlement")) {
						String EntitlementFeedCachedObjects = _properties
								.getProperty("EntitlementFeedCachedObjectsList");
						String[] cachedObjectsList = EntitlementFeedCachedObjects
								.split(",");
						for (String cachedObjectProperty : cachedObjectsList) {
							Element cachedObject = modifyCacheInput
									.createElement("CachedObject");
							cachedObject.setAttribute("Action", "MODIFY");
							cachedObject.setAttribute("Enabled", "Y");
							cachedObject.setAttribute("Class", _properties
									.getProperty(cachedObjectProperty));
							cachedGroup.appendChild(cachedObject);
						}
					}

					else if (feedName.equalsIgnoreCase("PriceBook")) {
						String PriceBookFeedCachedObjects = _properties
								.getProperty("PriceBookFeedCachedObjectsList");
						String[] cachedObjectsList = PriceBookFeedCachedObjects
								.split(",");
						for (String cachedObjectProperty : cachedObjectsList) {
							Element cachedObject = modifyCacheInput
									.createElement("CachedObject");
							cachedObject.setAttribute("Action", "MODIFY");
							cachedObject.setAttribute("Enabled", "Y");
							cachedObject.setAttribute("Class", _properties
									.getProperty(cachedObjectProperty));
							cachedGroup.appendChild(cachedObject);
						}
					} else if (feedName.equalsIgnoreCase("Uom")) {
						String PriceBookFeedCachedObjects = _properties
								.getProperty("UomFeedCachedObjectsList");
						String[] cachedObjectsList = PriceBookFeedCachedObjects
								.split(",");
						for (String cachedObjectProperty : cachedObjectsList) {
							Element cachedObject = modifyCacheInput
									.createElement("CachedObject");
							cachedObject.setAttribute("Action", "MODIFY");
							cachedObject.setAttribute("Enabled", "Y");
							cachedObject.setAttribute("Class", _properties
									.getProperty(cachedObjectProperty));
							cachedGroup.appendChild(cachedObject);
						}
					}
				}

				// Inoking modifyCache api
				if(log.isDebugEnabled()){
				log.debug("The modifyCache input is: "
						+ SCXmlUtil.getString(modifyCacheInput));
				}
				api.invoke(env, "modifyCache", modifyCacheInput);

			} else {
				log.debug("Unable to find the ServerId for given ServerName:"
						+ strServerName);
			}
		} else {
			log.debug("Argument 'IntegrationServerName' is either blank or not configured.");
		}

		// method to trigger catalog index build
		String isCatalogIndexBuildReqd = _properties
				.getProperty("CatalogIndexBuildReqd");
		if ("Y".equalsIgnoreCase(isCatalogIndexBuildReqd)) {
			triggerCatalogIndexBuild(env, strRoot);
		}
		// api = null;
	}

	/**
	 * @author asekhar-tw on 11-Jan-2011 This method prepares the error object
	 *         with the exception details which in turn will be used to log into
	 *         CENT
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

	/**
	 * @author asekhar-tw on 24-Jan-2011 This method prepares the message object
	 *         with the EOF message details which in turn will be used to log
	 *         into CENT
	 */
	private void prepareEOFOrSOFMsgObject(String transType, String errorClass,
			String errorDesc, YFSEnvironment env, Document inXML) {

		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setErrorDesc(errorDesc);
		/*
		 * YFSException yfe = new YFSException();
		 * yfe.setErrorDescription(errorDesc); errorObject.setException(yfe);
		 */
		ErrorLogger.log(errorObject, env);
	}

	/**
	 * @author asekhar-tw on 25-Jan-2011 This method validates the incoming XML
	 *         to figure out which feed has come in.
	 */
	private String[] validateIncomingMessage(YFSEnvironment env,
			Document inXML, String rootNode) {
		String messageAttributes[] = new String[3];
		String feedName = "";

		if ("SOF".equalsIgnoreCase(rootNode)) {
			/**
			 * Get the value of the root node attribute 'FeedName' of the input
			 * XML
			 **/
			feedName = inXML.getDocumentElement().getAttribute("FeedName");

		} else {
			feedName = inXML.getDocumentElement().getAttribute(
					"YantraMessageGroupID");
		}
		if(log.isDebugEnabled()){
			log.debug("The feedName in XPXUtils is : " + feedName);
		}
		/** Getting the value of the feed **/
		String TRANS_TYPE = _properties.getProperty(feedName + "TransType");
		messageAttributes[0] = TRANS_TYPE;
		if(log.isDebugEnabled()){
			log.debug("TRANS_TYPE: " + messageAttributes[0]);
		}
		/**
		 * Check if this is an EOF Feed Assumption: The Root Node of an EOF
		 * message will be 'EOFDivisions'
		 **/
		if (rootNode.startsWith("EOF")) {
			messageAttributes[1] = XPXLiterals.EOF_ERROR_CLASS;
			messageAttributes[2] = XPXLiterals.EOF_ERROR_DESC + " " + feedName;
			log.debug(messageAttributes[2]);
		}
		/** Check if this is an SOF Feed **/
		else if ("SOF".equalsIgnoreCase(rootNode)) {
			messageAttributes[1] = XPXLiterals.SOF_ERROR_CLASS;
			messageAttributes[2] = XPXLiterals.SOF_ERROR_DESC + " " + feedName;
			log.debug(messageAttributes[2]);
		}
		/** TRANS_TYPE represents the type of batch feed coming in **/
		return messageAttributes;
	}

	/**
	 * @param env
	 * @param strRoot
	 * @throws RemoteException
	 */
	private void triggerCatalogIndexBuild(YFSEnvironment env, String strRoot)
			throws RemoteException {
		String catalogIndexBuildParam = _properties
				.getProperty("CatalogIndexBuild");
		if (strRoot.equals(catalogIndexBuildParam)) {
			// form the input xml
			Document inputTriggerAgentDoc = YFCDocument.createDocument(
					"TriggerAgent").getDocument();
			Element inputTriggerAgentElement = inputTriggerAgentDoc
					.getDocumentElement();
			inputTriggerAgentElement.setAttribute("BaseTransactionId",
					"CATALOG_INDEX_BUILD");
			inputTriggerAgentElement.setAttribute("ProcessType", "GENERAL");
			inputTriggerAgentElement.setAttribute("CriteriaId",
					"CATALOG_INDEX_BUILD");
			api.invoke(env, "triggerAgent", inputTriggerAgentDoc);
		}
	}

	private String getServerIdForName(YFSEnvironment env, String strServerName)
			throws YFSException, RemoteException {

		Document docInput = SCXmlUtil.createDocument("Servers");
		docInput.getDocumentElement().setAttribute("Type",
				"IntegrationAgentServer");

		Document response = api.invoke(env, "getServerList", docInput);
		return SCXmlUtil.getXpathAttribute(response.getDocumentElement(),
				"./Server[@Name='" + strServerName + "']/@Id");
	}

	public boolean contains(String[] array, String str2checkinarray) {
		if (SCUtil.isVoid(array) || SCUtil.isVoid(str2checkinarray)) {
			return false;
		}
		for (String string : array) {
			if (SCUtil.equals(string, str2checkinarray)) {
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * Input XML format:
	 *
	 * <pre>
	 * &lt;XPXArticle ArticleKey='required' RequestedByUsersTeamId='required'/&gt;
	 * </pre>
	 *
	 * Output XML format:
	 *
	 * <pre>
	 * &lt;XPXArticle ...&gt;
	 * &lt;Divisions&gt;
	 * &lt;OrganizationList&gt;
	 * &lt;Organization OrganizationCode=&quot;&quot; OrganizationKey=&quot;&quot; OrganizationName=&quot;&quot;/&gt;
	 * &lt;/OrganizationList&gt;
	 * &lt;/Divisions&gt;
	 * &lt;AssignedDivisions&gt;
	 * &lt;OrganizationList&gt;
	 * &lt;Organization OrganizationCode=&quot;&quot; OrganizationKey=&quot;&quot; OrganizationName=&quot;&quot; AllowedToRemove=&quot;will be set to N in case this Division is not removable by the user&quot;/&gt;
	 * &lt;/OrganizationList&gt;
	 * &lt;/AssignedDivisions&gt;
	 * &lt;/XPXArticle&gt;
	 * </pre>
	 *
	 * @param env
	 * @param docInput
	 * @return
	 * @throws YIFClientCreationException
	 * @throws RemoteException
	 * @throws YFSException
	 */
	public Document getXPXArticleDetailsForCOM(YFSEnvironment env,
			Document docInput) throws YIFClientCreationException, YFSException,
			RemoteException {

		if (env == null) {
			// Throw new YFSException with the description
			throw new YFSException(
					"YFSEnvironment cannot be null or invalid to XPXUtils.getXPXArticleDetailsForCOM()");
		}
		if (docInput == null) {
			// Throw new YFSException with the description
			throw new YFSException(
					"Input XML document cannot be null or invalid to XPXUtils.getXPXArticleDetailsForCOM()");
		}
		if (SCUtil.isVoid(docInput.getDocumentElement().getAttribute(
				"ArticleKey"))
				|| SCUtil.isVoid(docInput.getDocumentElement().getAttribute(
						"RequestedByUsersTeamId"))) {
			throw new YFSException(
					"Invalid Input XPXUtils.getXPXArticleDetailsForCOM(): ArticleKey and RequestedByUsersTeamId are mandatory");
		}
		if(log.isDebugEnabled()){
			log.debug("getXPXArticleDetailsForCOM() Input XML:"+ SCXmlUtil.getString(docInput));
		}
		// api = YIFClientFactory.getInstance().getApi();

		// -->Get Article Details
		Document docResponse = api.executeFlow(env,
				XPXLiterals.DB_SERVICE_GET_ARTICLE_DETAILS, docInput);

		// Create child node's "Divisions","AssignedDivisions" under XPXArticle
		Element eleDivisions = SCXmlUtil.createChild(
				docResponse.getDocumentElement(), "Divisions");
		Element eleAssignedDivisions = SCXmlUtil.createChild(
				docResponse.getDocumentElement(), "AssignedDivisions");

		// -->Get Assignable Divisions List for the User's TeamId
		ArrayList<String> listDivsAllowedToAssign = getAllowedToAssignDivisionList(
				env, docInput);

		// -->Get the Array Of Assigned Divisions.
		String[] assignedDivisions = getArrayOfAssignedDivisions(docResponse);

		// -->Get the Organization list for 'Assigned Divisions' and 'Allowed To
		// Assign Division List'
		Document docOrganizationList = getOrganizationListForArticle(env,
				listDivsAllowedToAssign, assignedDivisions);

		// Update assigned Division OrganizationList to
		// XPXArticle/AssignedDivisions element.
		Element eleAssignedOrgList = SCXmlUtil.createChild(
				eleAssignedDivisions, "OrganizationList");
		NodeList nlOrgs = SCXmlUtil.getXpathNodes(
				docOrganizationList.getDocumentElement(), "./Organization");

		for (int i = 0; i < nlOrgs.getLength(); i++) {
			Element eleOrg = (Element) nlOrgs.item(i);
			String strOrgKey = eleOrg.getAttribute("OrganizationKey");
			// Set AllowedToRemove='N', in case of user is not allowed to remove
			// an assigned division to which he doesn't have permission
			if (contains(assignedDivisions, strOrgKey)
					&& !listDivsAllowedToAssign.contains(strOrgKey)) {
				eleOrg.setAttribute("AllowedToRemove", "N");
			}
			// in case Organization key is present in array[assigned divisions],
			// then
			// import Organization element from the OrganizationList to
			// assignedDivisons/OrganizationList and remove from its parent.
			if (contains(assignedDivisions, strOrgKey)) {
				SCXmlUtil.importElement(eleAssignedOrgList, eleOrg);
				docOrganizationList.getDocumentElement().removeChild(eleOrg);
			}
		}
		// import the Organization List element to XPXArticle/Divisions element
		SCXmlUtil.importElement(eleDivisions,
				docOrganizationList.getDocumentElement());
		if(log.isDebugEnabled()){
			log.debug("getXPXArticleDetailsForCOM() Output XML:" + SCXmlUtil.getString(docResponse));
		}
		// api = null;
		return docResponse;
	}

	/**
	 * @param docResponse
	 * @return
	 */
	private String[] getArrayOfAssignedDivisions(Document docResponse) {
		// Get Assigned Divisions from XPXArticle
		String strDivisions = docResponse.getDocumentElement().getAttribute(
				"XPXDivision");
		strDivisions = strDivisions.replace("||", "|");
		strDivisions = strDivisions.substring(strDivisions.indexOf("|") + 1);
		strDivisions = strDivisions.replace("|", ",");
		String[] assignedDivisions = strDivisions.split(",");
		return assignedDivisions;
	}

	/**
	 * @param env
	 * @param listDivsAllowedToAssign
	 * @param assignedDivisions
	 * @return
	 * @throws RemoteException
	 */
	private Document getOrganizationListForArticle(YFSEnvironment env,
			ArrayList<String> listDivsAllowedToAssign,
			String[] assignedDivisions) throws RemoteException {
		Document docOrgListInput = SCXmlUtil
				.createDocument(XPXLiterals.E_ORGANIZATION);
		Element eleCQuery = SCXmlUtil.createChild(
				docOrgListInput.getDocumentElement(), "ComplexQuery");
		Element eleOr = SCXmlUtil.createChild(eleCQuery, "Or");
		for (String strDivCode : assignedDivisions) {
			Element eleExp = SCXmlUtil.createChild(eleOr, "Exp");
			eleExp.setAttribute("Name", "OrganizationKey");
			eleExp.setAttribute("Value", strDivCode);
		}

		// If Team is associated with ShipNodes,
		// get the Organization Info List and append to 'Divisions' node for
		// displaying in COM UI.
		if (listDivsAllowedToAssign.size() > 0) {
			for (String strAllowedToAssignDiv : listDivsAllowedToAssign) {
				Element eleExp = SCXmlUtil.createChild(eleOr, "Exp");
				eleExp.setAttribute("Name", "OrganizationKey");
				eleExp.setAttribute("Value", strAllowedToAssignDiv);
			}
		} else {
			// SCXmlUtil.createChild(eleDivisions,
			// XPXLiterals.E_ORGANIZATION_LIST);
		}
		// GetOrganizationList
		env.setApiTemplate(XPXLiterals.GET_ORGANIZATION_LIST_API,
				GLOBAL_TEMPLATE_API_GET_ORGANIZATION_LIST_XPX_GET_ARTICLE_DETAILS_SERVICE);
		Document docOrganizationList = api.invoke(env,
				XPXLiterals.GET_ORGANIZATION_LIST_API, docOrgListInput);
		env.clearApiTemplate(XPXLiterals.GET_ORGANIZATION_LIST_API);
		return docOrganizationList;
	}

	/**
	 * @param env
	 * @param docInput
	 * @return
	 * @throws RemoteException
	 */
	private ArrayList<String> getAllowedToAssignDivisionList(
			YFSEnvironment env, Document docInput) throws RemoteException {
		Document docTeamInput = SCXmlUtil.createDocument("Team");
		docTeamInput.getDocumentElement().setAttribute(
				"TeamId",
				docInput.getDocumentElement().getAttribute(
						"RequestedByUsersTeamId"));

		// env.setApiTemplate(XPXLiterals.API_GET_TEAM_LIST,
		// "global/template/api/getTeamList.XPXGetArticleDetailsService.xml");
		Document docTeamList = api.invoke(env, XPXLiterals.GET_TEAM_LIST_API,
				docTeamInput);
		// env.clearApiTemplate(XPXLiterals.API_GET_TEAM_LIST);
		NodeList nlTeamNodes = SCXmlUtil.getXpathNodes(
				docTeamList.getDocumentElement(),
				"./Team/TeamNodesList/TeamNodes");

		ArrayList<String> listDivsAllowedToAssign = new ArrayList<String>();
		for (int i = 0; i < nlTeamNodes.getLength(); i++) {
			Element eleTeamNodes = (Element) nlTeamNodes.item(i);
			listDivsAllowedToAssign.add(eleTeamNodes
					.getAttribute("ShipnodeKey"));
		}
		return listDivsAllowedToAssign;
	}

	/**
	 *
	 * Input XML format:
	 *
	 * <pre>
	 * &lt;Team RequestedByUsersTeamId='required'/&gt;
	 * </pre>
	 *
	 * Output XML format:
	 *
	 * <pre>
	 *
	 * &lt;OrganizationList&gt;
	 * &lt;Organization OrganizationCode=&quot;&quot; OrganizationKey=&quot;&quot; OrganizationName=&quot;&quot;/&gt;
	 * &lt;/OrganizationList&gt;
	 * </pre>
	 *
	 * @param env
	 * @param docInput
	 * @return
	 * @throws YIFClientCreationException
	 * @throws RemoteException
	 * @throws YFSException
	 */
	public Document getDivisionsManagedByTeam(YFSEnvironment env,
			Document docInput) throws YIFClientCreationException, YFSException,
			RemoteException {

		if (env == null) {
			// Throw new YFSException with the description
			throw new YFSException(
					"YFSEnvironment cannot be null or invalid to XPXUtils.getDivisionsManagedByTeam()");
		}
		if (docInput == null) {
			// Throw new YFSException with the description
			throw new YFSException(
					"Input XML document cannot be null or invalid to XPXUtils.getDivisionsManagedByTeam()");
		}
		if (SCUtil.isVoid(docInput.getDocumentElement().getAttribute(
				"RequestedByUsersTeamId"))) {
			throw new YFSException(
					"Invalid Input XPXUtils.getDivisionsManagedByTeam(): RequestedByUsersTeamId is mandatory");
		}
		if(log.isDebugEnabled()){
			log.debug("getDivisionsManagedByTeam() Input XML:" + SCXmlUtil.getString(docInput));
		}
		// api = YIFClientFactory.getInstance().getApi();

		// -->Get Assignable Divisions List for the User's TeamId
		ArrayList<String> listDivsAllowedToAssign = getAllowedToAssignDivisionList(
				env, docInput);

		// -->Get the Organization list
		Document docOrganizationList = null;
		if (null != listDivsAllowedToAssign
				&& listDivsAllowedToAssign.size() > 0) {
			Document docOrgListInput = SCXmlUtil
					.createDocument(XPXLiterals.E_ORGANIZATION);
			Element eleCQuery = SCXmlUtil.createChild(
					docOrgListInput.getDocumentElement(), "ComplexQuery");
			Element eleOr = SCXmlUtil.createChild(eleCQuery, "Or");
			if (listDivsAllowedToAssign.size() > 0) {
				for (String strAllowedToAssignDiv : listDivsAllowedToAssign) {
					Element eleExp = SCXmlUtil.createChild(eleOr, "Exp");
					eleExp.setAttribute("Name", "OrganizationKey");
					eleExp.setAttribute("Value", strAllowedToAssignDiv);
				}
			}
			env.setApiTemplate(XPXLiterals.GET_ORGANIZATION_LIST_API,
					GLOBAL_TEMPLATE_API_GET_ORGANIZATION_LIST_XPX_GET_ARTICLE_DETAILS_SERVICE);
			docOrganizationList = api.invoke(env,
					XPXLiterals.GET_ORGANIZATION_LIST_API, docOrgListInput);
			env.clearApiTemplate(XPXLiterals.GET_ORGANIZATION_LIST_API);
		} else {
			docOrganizationList = SCXmlUtil.createDocument("OrganizationList");
		}
		if(log.isDebugEnabled()){
			log.debug("getDivisionsManagedByTeam() Output XML:" + SCXmlUtil.getString(docOrganizationList));
		}
		// api = null;
		return docOrganizationList;
	}

	public static HashMap getDivisionCode(YFSEnvironment env,
			String customerId, String organizationCode) throws YFSException,
			RemoteException, YIFClientCreationException {
		String customerDivision = null;
		String shipFromBranch = null;
		String customerOrderBranch = null;
		HashMap divisionCodes = new HashMap();
		// api = YIFClientFactory.getInstance().getApi();

		if (env == null) {
			// Throw new YFSException with the description
			throw new YFSException(
					"YFSEnvironment cannot be null or invalid to XPXUtils.getDivisionCode()");
		} else if (customerId == null || customerId.trim().length() <= 0) {
			// Throw new YFSException with the description
			throw new YFSException(
					"Customer ID cannot be null or empty to XPXUtils.getDivisionCode()");
		} else if (organizationCode == null
				|| organizationCode.trim().length() <= 0) {
			// Throw new YFSException with the description
			throw new YFSException(
					"OrganizationCode cannot be null or empty to XPXUtils.getDivisionCode()");
		}

		Document getCustomerListInputDoc = YFCDocument.createDocument(
				"Customer").getDocument();
		getCustomerListInputDoc.getDocumentElement().setAttribute("CustomerID",
				customerId);
		getCustomerListInputDoc.getDocumentElement().setAttribute(
				"OrganizationCode", organizationCode);

		Document getCustomerListTemplate = YFCDocument.createDocument(
				XPXLiterals.E_CUSTOMER_LIST).getDocument();
		Element eCustomerListTemp = getCustomerListTemplate
				.getDocumentElement();
		Element eCustomerTemp = getCustomerListTemplate
				.createElement(XPXLiterals.E_CUSTOMER);
		eCustomerTemp.setAttribute("CustomerID", "");
		eCustomerListTemp.appendChild(eCustomerTemp);
		Element eExtnTemp = getCustomerListTemplate
				.createElement(XPXLiterals.E_EXTN);
		eCustomerTemp.appendChild(eExtnTemp);
		eExtnTemp.setAttribute("ExtnCustomerDivision", "");
		eExtnTemp.setAttribute("ExtnCustOrderBranch", "");
		eExtnTemp.setAttribute("ExtnShipFromBranch", "");
		eExtnTemp.setAttribute("ExtnEnvironmentCode", "");

		env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API,
				getCustomerListTemplate);
		if(log.isDebugEnabled()){
			log.debug("The input doc in getDivisionCode is: " + SCXmlUtil.getString(getCustomerListInputDoc));
		}
		Document outputCustomerListDoc = api.invoke(env,
				XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc);
		if(log.isDebugEnabled()){
			log.debug("The output doc in getDivisionCode is: " + SCXmlUtil.getString(outputCustomerListDoc));
		}
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);

		if (outputCustomerListDoc.getDocumentElement()
				.getElementsByTagName("Customer").getLength() > 0) {
			Element outputCustomerElement = (Element) outputCustomerListDoc
					.getDocumentElement().getElementsByTagName("Customer")
					.item(0);
			Element outputExtnElement = (Element) outputCustomerElement
					.getElementsByTagName("Extn").item(0);
			customerDivision = outputExtnElement
					.getAttribute("ExtnCustomerDivision");
			shipFromBranch = outputExtnElement
					.getAttribute("ExtnShipFromBranch");
			customerOrderBranch = outputExtnElement
					.getAttribute("ExtnCustOrderBranch");

			//TODO remove this since always "M"?
			String envtCode = outputExtnElement.getAttribute("ExtnEnvironmentCode");
			if (envtCode == null || envtCode.trim().length() <= 0) {
				// Throw new YFSException with the description
				throw new YFSException("Environment code for the customer is null or empty");
			}

			if (customerDivision.trim().length() > 0) {

				customerDivision = updateNodeSyntax(envtCode, customerDivision);
				if(log.isDebugEnabled()){
					log.debug("The returned customer division code is: " + customerDivision);
				}
				divisionCodes.put("ExtnCustomerDivision", customerDivision);
			}
			if (shipFromBranch.trim().length() > 0) {

				shipFromBranch = updateNodeSyntax(envtCode, shipFromBranch);
				if(log.isDebugEnabled()){
					log.debug("The returned ship from branch code is: "
						+ shipFromBranch);
				}
				divisionCodes.put("ExtnShipFromBranch", shipFromBranch);
			}
			if (customerOrderBranch.trim().length() > 0) {

				customerOrderBranch = updateNodeSyntax(envtCode,
						customerOrderBranch);
				if(log.isDebugEnabled()){
					log.debug("The returned customer order branch is: "
						+ customerOrderBranch);
				}
				divisionCodes.put("ExtnCustOrderBranch", customerOrderBranch);
			}

		} else {
			// Throw new YFSException with the description
			throw new YFSException("No such customer: " + customerId
					+ "exists in system");
		}

		return divisionCodes;
	}


	// Keep this old signature as wrapper method though the one caller is hopefully not using it anymore
	public static String replaceOutgoingUOMFromLegacy(YFSEnvironment env,
			String incomingUOM, String itemID, String buyerID, String eTradingID)
			throws YFSException, RemoteException, YIFClientCreationException,
			NullPointerException, Exception {

		if (env == null) {
			// Throw new YFSException with the description
			throw new YFSException(
					"YFSEnvironment cannot be null or invalid to XPXUtils.replaceOutgoingUOMFromLegacy()");
		} else if (buyerID == null || buyerID.trim().length() <= 0) {
			// Throw new YFSException with the description
			throw new YFSException(
					"Customer ID cannot be null or empty to XPXUtils.replaceOutgoingUOMFromLegacy()");
		} else if (eTradingID == null || eTradingID.trim().length() <= 0) {
			// Throw new YFSException with the description
			throw new YFSException(
					"eTradingID cannot be null or empty to XPXUtils.replaceOutgoingUOMFromLegacy()");
		} else if (itemID == null || itemID.trim().length() <= 0) {
			// Throw new YFSException with the description
			throw new YFSException(
					"ItemID cannot be null or empty to XPXUtils.replaceOutgoingUOMFromLegacy()");
		} else if (incomingUOM == null || incomingUOM.trim().length() <= 0) {
			// Throw new YFSException with the description
			throw new YFSException(
					"IncomingUOM cannot be null or empty to XPXUtils.replaceOutgoingUOMFromLegacy)");
		}

		return replaceOutgoingUOMFromLegacy(env, incomingUOM, itemID);
	}

	public static String replaceOutgoingUOMFromLegacy(YFSEnvironment env,String incomingUOM, String itemID)
			throws YFSException, RemoteException, YIFClientCreationException, NullPointerException {

		// (removed cust lookup for env since always "M")
		String environment_id = "M";
		if (!incomingUOM.contains("_")) {
			incomingUOM = environment_id + "_" + incomingUOM;
		}

		String replacedUOM = null;

		if (uomOutgoingXrefMap.containsKey(incomingUOM)) {
			replacedUOM = uomOutgoingXrefMap.get(incomingUOM);
			log.info("Legacy UOM xref cache hit: " + incomingUOM + " -> "+replacedUOM); //TODO remove
		}
		else {
			// Lookup legacy UOM in xref table & cache
			Document legacyUomOutputDoc = null;
			Document legacyUomInputDoc = YFCDocument.createDocument("XPEDXLegacyUomXref").getDocument();
			legacyUomInputDoc.getDocumentElement().setAttribute("LegacyType", environment_id);
			legacyUomInputDoc.getDocumentElement().setAttribute("LegacyUOM", incomingUOM);

			log.info("The input to getLegacyUomXrefService is : " + SCXmlUtil.getString(legacyUomInputDoc)); //TODO debug
			legacyUomOutputDoc = api.executeFlow(env, "XPXGetLegacyUomXrefService", legacyUomInputDoc);
			log.info("The output of getLegacyUomXrefService is : " + SCXmlUtil.getString(legacyUomOutputDoc)); //TODO debug

			if (legacyUomOutputDoc != null) {
				Element legacyUomXrefElement = (Element) legacyUomOutputDoc.getDocumentElement()
						.getElementsByTagName("XPEDXLegacyUomXref").item(0);

				if (legacyUomXrefElement != null) {
					String ediUom = legacyUomXrefElement.getAttribute("UOM");

					if (ediUom != null && ediUom.trim().length() > 0) {
						replacedUOM = ediUom;
					} else {
						throw new YFSException("UOM Doesn't exist in XPEDX_Legacy_Uom_Xref table.");
					}
				} else {
					log.error("UOM Doesn't exist in XPEDX_Legacy_Uom_Xref table: " + incomingUOM);
					//throw new YFSException("UOM Doesn't exist in XPEDX_Legacy_Uom_Xref table.");
				}
			} else {
				throw new YFSException("UOM Doesn't exist in XPEDX_Legacy_Uom_Xref table.");
			}

			if (replacedUOM != null && replacedUOM.contains("_")) {
				// Means the envt id is appended to this uom....in this case, strip
				// the envt id and then send it to customer
				String[] splitArrayOnUom = replacedUOM.split("_");

				for (int i = 0; i < splitArrayOnUom.length; i++) {
					if (i == 1) {
						replacedUOM = splitArrayOnUom[i];
						log.info("The replacedUOM UOM after the split is: " + replacedUOM); //TODO debug
					}
				}
			}
			{
				log.info("The final replaced uom in the replaceOutgoing method is: "
					+ replacedUOM);
			}
			uomOutgoingXrefMap.put(incomingUOM, replacedUOM);
		}
		return replacedUOM;
	}


	// Keep this old signature as wrapper method though the one caller is hopefully not using it anymore
	public static String replaceIncomingUOMFromCustomer(YFSEnvironment env,
			String incomingUOM, String itemID, String buyerID, String eTradingID)
			throws YFSException, RemoteException, YIFClientCreationException {

		if (env == null) {
			throw new YFSException(
					"YFSEnvironment cannot be null or invalid to XPXUtils.replaceIncomingUOMFromCustomer()");
		} else if (buyerID == null || buyerID.trim().length() <= 0) {
			throw new YFSException(
					"Customer ID cannot be null or empty to XPXUtils.replaceIncomingUOMFromCustomer()");
		} else if (eTradingID == null || eTradingID.trim().length() <= 0) {
			throw new YFSException(
					"BuyerID cannot be null or empty to XPXUtils.replaceIncomingUOMFromCustomer()");
		} else if (itemID == null || itemID.trim().length() <= 0) {
			throw new YFSException(
					"ItemID cannot be null or empty to XPXUtils.replaceIncomingUOMFromCustomer()");
		} else if (incomingUOM == null || incomingUOM.trim().length() <= 0) {
			throw new YFSException(
					"IncomingUOM cannot be null or empty to XPXUtils.replaceIncomingUOMFromCustomer()");
		}

		Document getSAPCustomerDetailsOutputDoc = getSAPCustomerDetailsOutput(env, buyerID);

		return replaceIncomingUOMFromCustomer(env, incomingUOM, itemID, getSAPCustomerDetailsOutputDoc);
	}

	public static String replaceIncomingUOMFromCustomer(YFSEnvironment env,
			String incomingUOM, String itemID, Document getSAPCustomerDetailsOutputDoc)
			throws YFSException, RemoteException, YIFClientCreationException, NullPointerException {

		Element sapCustomerElement = (Element) getSAPCustomerDetailsOutputDoc
				.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
		String sapCustOrgCode = sapCustomerElement.getAttribute(XPXLiterals.A_ORGANIZATION_CODE);

		// (removed cust lookup for env since always "M")
		String environment_id = "M";

		/*** Added as of 30/03/2010 ******/
		incomingUOM = environment_id + "_" + incomingUOM;

		String replacedUOM;
		if (uomIncomingXrefMap.containsKey(incomingUOM)) {
			replacedUOM = uomIncomingXrefMap.get(incomingUOM);
			log.info("In Legacy UOM xref cache hit: " + incomingUOM + " -> "+replacedUOM); //TODO remove
		}
		else {
			// This is what actually converts the UOM from customer to legacy
			replacedUOM = checkCustomerProfileUom(env, incomingUOM, itemID);

			if (replacedUOM.contains("_")) {
				validateUOMWithItemUOMMaster(env, replacedUOM, sapCustOrgCode, itemID);
			} else {
				replacedUOM = environment_id + "_" + replacedUOM;
				{
					log.info("convertedUOM: " + replacedUOM);
				}
				validateUOMWithItemUOMMaster(env, replacedUOM, sapCustOrgCode,itemID);
			}
			uomIncomingXrefMap.put(incomingUOM, replacedUOM);
		}
		return replacedUOM;
	}

	private static Document getSAPCustomerDetailsOutput(YFSEnvironment env,
			String buyerId) throws YFSException, RemoteException {

		Document getCustomerDetailsOutputDoc = null;

		YFCDocument getCustomerDetailsInputDoc = YFCDocument
				.createDocument(XPXLiterals.E_CUSTOMER);
		YFCElement extnElement = getCustomerDetailsInputDoc
				.createElement(XPXLiterals.E_EXTN);
		extnElement.setAttribute("ExtnBuyerID", buyerId);

		getCustomerDetailsInputDoc.getDocumentElement()
				.appendChild(extnElement);

		env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API,
				getCustomerListTemplate);

		log.info("getCustomerList1: " + SCXmlUtil.getString(getCustomerDetailsInputDoc.getDocument())); //TODO debug
		getCustomerDetailsOutputDoc = api.invoke(env,
				XPXLiterals.GET_CUSTOMER_LIST_API,
				getCustomerDetailsInputDoc.getDocument());
		log.info("getCustomerList1 output: " + SCXmlUtil.getString(getCustomerDetailsOutputDoc)); //TODO debug

		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);

		return getCustomerDetailsOutputDoc;

	}

	private static String checkCustomerProfileUom(YFSEnvironment env, String incomingUOM, String itemID)
					throws YFSException, RemoteException, NullPointerException {

		String customerProfileUOM = null;
		Document legacyUomOutputDoc = null;

		// (removed cust lookup for env since always "M")
		String environment_id = "M";

		// TODO cache these? (template to reduce data?)
		Document legacyUomInputDoc = YFCDocument.createDocument("XPEDXLegacyUomXref").getDocument();
		legacyUomInputDoc.getDocumentElement().setAttribute("LegacyType", environment_id);
		legacyUomInputDoc.getDocumentElement().setAttribute("UOM", incomingUOM);

		{
			log.info("The i/p doc to getlegacyUomXref is: " + SCXmlUtil.getString(legacyUomInputDoc));
		}
		legacyUomOutputDoc = api.executeFlow(env, "XPXGetLegacyUomXrefService",
				legacyUomInputDoc);
		{
			log.info("The o/p doc o getlegacyUomXref is: " + SCXmlUtil.getString(legacyUomOutputDoc));
		}

		Element legacyUomXrefElement = (Element) legacyUomOutputDoc.getDocumentElement()
				.getElementsByTagName("XPEDXLegacyUomXref").item(0);
		if (legacyUomXrefElement != null) {
			String legacyUom = legacyUomXrefElement.getAttribute("LegacyUOM");
			if (legacyUom != null && legacyUom.trim().length() > 0) {
				if(log.isDebugEnabled()){
					log.debug("The legacy uom is: " + legacyUom);
				}
				customerProfileUOM = legacyUom;
			} else {
				throw new YFSException("No Legacy UOM available");
			}
		} else {
			throw new YFSException("No Legacy UOM XRef entry available");
		}
		return customerProfileUOM;
	}


	/**
	 * Appends EnvtId and Node in this format[if both are not null]:NODE_ENVTID
	 *
	 * @param strEnvtId
	 * @param strNode
	 * @return
	 */
	public static String updateNodeSyntax(String strEnvtId, String strNode) {
		if (SCUtil.isVoid(strNode) || SCUtil.isVoid(strEnvtId)) {
			strNode = "";
		} else {
			strNode = strNode + "_" + strEnvtId;
		}
		return strNode;
	}

	private static void validateUOMWithItemUOMMaster(YFSEnvironment env, String incomingUOM, String organizationCode, String itemID)
			throws YFSException, NullPointerException, RemoteException {
		if(log.isDebugEnabled()){
			log.debug("Validate UOM with the Item UOM master table.");
		}
		String itemUOMTemplate = "global/template/api/getItemUOMListTemplate.xml";

		// Validate incoming UOM based on Item UOM Master list.
		if (YFCObject.isNull(incomingUOM) || YFCObject.isVoid(incomingUOM)) {
			throw new IllegalArgumentException("UOM is empty");
		}
		if (YFCObject.isNull(organizationCode)
				|| YFCObject.isVoid(organizationCode)) {
			throw new IllegalArgumentException("Organization code is empty");
		}

		/* Input xml : <Item ItemID="" OrganizationCode="" /> */
		Document itemUOMInputDoc = YFCDocument.createDocument("Item")
				.getDocument();
		itemUOMInputDoc.getDocumentElement().setAttribute("ItemID", itemID);
		itemUOMInputDoc.getDocumentElement().setAttribute("OrganizationCode",
				organizationCode);

		env.clearApiTemplate("getItemUOMList");

		// To set API Template for getItemUOMList
		env.setApiTemplate("getItemUOMList", itemUOMTemplate);
		{
			log.info("itemUOMInputDoc = " + SCXmlUtil.getString(itemUOMInputDoc));
		}
		Document itemUOMOutputDoc = api.invoke(env, "getItemUOMList", itemUOMInputDoc);
		{
			log.info("itemUOMOutputDoc = " + SCXmlUtil.getString(itemUOMOutputDoc));
		}
		// To clear the api template.
		env.clearApiTemplate("getItemUOMList");

		boolean itemIDUOMExist = false;
		boolean validUOM = false;

		if (itemUOMOutputDoc != null) {
				NodeList itemUOMList = itemUOMOutputDoc.getDocumentElement().getElementsByTagName("ItemUOM");

			int itemUOMListSize = itemUOMList.getLength();
			for (int itemUOMCount = 0; itemUOMCount < itemUOMListSize; itemUOMCount++) {

				// Item UOM Exists for the item Id.
				Element itemUOMElement = (Element) itemUOMList.item(itemUOMCount);
				if (itemUOMElement != null&& itemUOMElement.hasAttribute("IsOrderingUOM")) {
					String itemUOM = itemUOMElement.getAttribute("UnitOfMeasure");

					// To check if incoming UOM is equal with the UOM in yfs_item_uom table.
					if (itemUOM != null && itemUOM.equalsIgnoreCase(incomingUOM)) {
						itemIDUOMExist = true;

						String isOrderingUOM = itemUOMElement.getAttribute("IsOrderingUOM");
						if (!YFCObject.isNull(isOrderingUOM)) {

							// To check if the UOM is an ordering UOM.
							if (isOrderingUOM.equalsIgnoreCase("Y")) {
								if(log.isDebugEnabled()){ log.debug("Valid UOM"); }
								validUOM = true;
								break;
							}
						}
					}
				}
			}

			// Throw the exception based on the flags set above.
			if (!itemIDUOMExist) {
				throw new IllegalArgumentException(
						"Invalid UOM as Item ID UOM combination isn't available with Item ID = "
								+ itemID + " and UOM = " + incomingUOM);
			}
			if (!validUOM) {
				throw new IllegalArgumentException(
						"Invalid UOM as Item UOM is not available for ordering.");
			}

		} else {
			throw new IllegalArgumentException(
					"Invalid UOM as Item ID UOM combination isn't available with Item ID = "
							+ itemID + " and UOM = " + incomingUOM);
		}
	}

	public Document stampBrandLogo(YFSEnvironment env, Document inputDocument) {

			String _imageName = "";
			String brandLogo = "";
			String imagesRootFolder = "";

			imagesRootFolder = YFSSystem.getProperty("ImagesRootFolder");
			if (null == imagesRootFolder || imagesRootFolder.trim().length() <= 0) {
				imagesRootFolder = _properties.getProperty("IMAGES_ROOT_FOLDER");
			}
			if (log.isDebugEnabled()) {
				log.debug("imagesRootFolder: " + imagesRootFolder);
			}

			String orgCode = inputDocument.getDocumentElement().getAttribute("EnterpriseCode");

			if (null != orgCode && null != imagesRootFolder) {
				_imageName = getLogoImageName(env, orgCode);
				brandLogo = imagesRootFolder.concat(_imageName);
				if (log.isDebugEnabled()) {
					log.debug("brandLogo: " + brandLogo);
				}
				inputDocument.getDocumentElement().setAttribute("BrandLogo",brandLogo);
			}

		return inputDocument;
	}

	public String getLogoImageName(YFSEnvironment env, String sellerOrgCode) {

		String _imageName = "";

		/**
		 * Comment added by Arun Sekhar on 26-April-2011
		 *
		 * NOTE: The image names are assigned based on the current naming
		 * convention followed for XPEDX logo(SellerOrgCode_r_rgb_lo.jpg).
		 *
		 * This needs to be revisited once the logo image names are confirmed.
		 **/
		if ("xpedx".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/logo-email.jpg";
		} else if ("BulkleyDunton".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/BulkleyDunton_r_rgb_lo.jpg";
		} else if ("CentralLewmar".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/CentralLewmar_r_rgb_lo.jpg";
		} else if ("CentralMarquardt".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/CentralMarquardt_r_rgb_lo.jpg";
		} else if ("Saalfeld".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/saalfeld_210x47px.jpg";
		} else if ("StrategicPaper".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/StrategicPaper_r_rgb_lo.jpg";
		} else if ("WesternPaper".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/WesternPaper_r_rgb_lo.jpg";
		} else if ("WhitemanTower".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/WhitemanTower_r_rgb_lo.jpg";
		} else if ("Zellerbach".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/Zellerbach_r_rgb_lo.jpg";
		} else if ("xpedxCanada".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/logo-email.jpg";
		}
		return _imageName;
	}

	/************** Added by Arun Sekhar on 26-April-2011 **************/
	/**
	 * All the four immediate below methods will prepare the Subject Line for
	 * the email corresponding to various email scenarios
	 */
	public String stampOrderSubjectLine(YFSEnvironment env,
			Document inputDocument, String orderOperation) throws Exception {

		String brand = inputDocument.getDocumentElement().getAttribute(
				"EnterpriseCode");
		String customerPO = inputDocument.getDocumentElement().getAttribute(
				"CustomerPONo");
		String orderNo = inputDocument.getDocumentElement().getAttribute(
				"OrderNo");
		StringBuilder _subjectLine = null;
		String emailStr = null;

		if("OrderPlacement".equals(orderOperation)) {
			emailStr="Order Submitted Notification";
		} else {
			emailStr="Order Edit Notification";
		}

		if("Saalfeld".equalsIgnoreCase(brand)){
			_subjectLine = new StringBuilder(brand.concat("redistribution.com ").concat(emailStr).concat(" "));
		}else{
			_subjectLine = new StringBuilder(brand.concat(".com ").concat(emailStr).concat(" "));
		}

		YFCDocument inDoc = YFCDocument.getDocumentFor(inputDocument);
		YFCElement orderElem = inDoc.getDocumentElement();

		if (orderElem != null)
		{
			if(!YFCObject.isVoid(customerPO))
			{
				_subjectLine.append("- PO # ").append(customerPO);
			}

			if (!YFCObject.isVoid(orderNo))
			{
				if(!YFCObject.isVoid(customerPO))
				{
				_subjectLine.append(", Order # ").append(orderNo);

				}else {
					_subjectLine.append("- Order # ").append(orderNo);
				}

			}
		}

		if(log.isDebugEnabled())
		{
			log.debug("Inside XPXUtils.stampOrderSubjectLine method - Order Confirmation email's subject Line : " + _subjectLine);
		}

		if(YFCObject.isVoid(_subjectLine)){
			log.error("Inside XPXUtils.stampOrderSubjectLine method - Order Confirmation email's subject Line is empty.");
		}
		inputDocument.getDocumentElement().setAttribute("Subject", _subjectLine.toString());

		return _subjectLine.toString();
	}

	public void stampOrderChangeStatusSubjectLine(YFSEnvironment env,
			Element orderElement, String holdStatus, String orderStatusSubjectLine) throws Exception {

		String brand = orderElement.getAttribute("EnterpriseCode");
		String customerPO = orderElement.getAttribute("CustomerPONo");
		String formattedOrderNo = orderElement.getAttribute("FormattedOrderNo");
		StringBuilder _subjectLine = null;
		if("Saalfeld".equalsIgnoreCase(brand)){
			_subjectLine = new StringBuilder(brand.concat("redistribution.com ").concat(orderStatusSubjectLine));
		}else{
			_subjectLine = new StringBuilder(brand.concat(".com ").concat(orderStatusSubjectLine));
		}

		if(!YFCObject.isVoid(customerPO))
		{
			_subjectLine.append(" - PO # ").append(customerPO);
		}
		if ("1300".equalsIgnoreCase(holdStatus) && (!YFCObject.isVoid(formattedOrderNo)))
		{
			if(!YFCObject.isVoid(customerPO))
			{
				_subjectLine.append(", Order # ").append(formattedOrderNo);

			}else {
				_subjectLine.append("- Order # ").append(formattedOrderNo);
			}
		}
		orderElement.setAttribute("Subject", _subjectLine.toString());

		log.debug("_subjectLine: " + _subjectLine);
	}

	public Document stampSubjectLine_UserProfChange(YFSEnvironment env,
			Document inputDocument) throws Exception {

		String _subjectLine = null;
		String imageUrl = "";
		Element rootElem = inputDocument.getDocumentElement();

		String brand = rootElem.getAttribute("SellerOrganizationCode");
		imageUrl = rootElem.getAttribute("ImageUrl");


		if("Saalfeld".equalsIgnoreCase(brand)){
			_subjectLine = brand.concat("redistribution.com").concat(" ").concat("User Profile Updated Notification"); //Start - Jira 3262
		}else{
			_subjectLine = brand.concat(".com").concat(" ").concat("User Profile Updated Notification"); //Start - Jira 3262
		}


		//_subjectLine = brand.concat(".com").concat(" ").concat("User Profile Updated Notification"); //Start - Jira 3262

		log.debug("brand:" + brand);
		log.debug("imageUrl:" + imageUrl);
		log.debug("_subjectLine:" + _subjectLine);

		if( !YFCObject.isNull(brand) && !YFCObject.isVoid(brand)
				&& (YFCObject.isNull(imageUrl) || YFCObject.isVoid(imageUrl)) ) {
				String imageName = getLogoImageName(env, brand);
				String imagesRootFolder = null;
				if("Saalfeld".equalsIgnoreCase(brand)){
					imagesRootFolder=YFSSystem.getProperty("SaalfeldImagesRootFolder");
				}else{
					imagesRootFolder=YFSSystem.getProperty("ImagesRootFolder");
				}
				if( !YFCObject.isNull(imagesRootFolder) && !YFCObject.isVoid(imagesRootFolder)
						&& !YFCObject.isNull(imageName) && !YFCObject.isVoid(imageName)){
					imageUrl = imagesRootFolder + imageName;
					rootElem.setAttribute("ImageUrl",imageUrl);
				}
		}

		rootElem.setAttribute("Subject", _subjectLine);

		if(log.isDebugEnabled()){
			log.debug("stampSubjectLine_UserProfChange()_OutXML: "+ SCXmlUtil.getString(inputDocument));
		}
		String inputXML=SCXmlUtil.getString(inputDocument);
		String emailType=XPXEmailUtil.USER_PROFILE_UPDATED_NOTIFICAON;
		String emailFrom = null;
		if("Saalfeld".equalsIgnoreCase(brand)){
			emailFrom=YFSSystem.getProperty("saalFeldEMailFromAddresses");  // new attribute defined in customer_overides properties….

		} else {
			emailFrom = YFSSystem.getProperty("EMailFromAddresses");
		}


		String emailOrgCode= (rootElem.getAttribute("SellerOrganizationCode")!=null?rootElem.getAttribute("SellerOrganizationCode"):"");
		String businessIdentifier = rootElem.getAttribute("UserName");
		XPXEmailUtil.insertEmailDetailsIntoDB(env,inputXML, emailType, _subjectLine, emailFrom, emailOrgCode,businessIdentifier);
		return inputDocument;
	}

	// Added for JIRA 1998 - Email Addr change on User Profile
	public Document stampSubjectLine_UserEmailAddrChange(YFSEnvironment env,
			Document inputDocument) throws Exception {
		String _subjectLine = null;

		String brand = inputDocument.getDocumentElement().getAttribute(
				"SellerOrganizationCode");
		_subjectLine = "Notification: User Profile Email Address Change on ".concat(brand)
				.concat(".com").concat(" account ");
		if(log.isDebugEnabled()){
			log.debug("_subjectLine: " + _subjectLine);
		}
		inputDocument.getDocumentElement()
				.setAttribute("Subject", _subjectLine);
		if(log.isDebugEnabled()){
		log.debug("inputDocument with SubjectLine: "
				+ SCXmlUtil.getString(inputDocument));
		}
		return inputDocument;
	}

	public Document stampSubjectLine_BuyerAdded(YFSEnvironment env,
			Document inputDocument) throws Exception {
		String _subjectLine = null;

		String brand = inputDocument.getDocumentElement().getAttribute(
				"SellerOrganizationCode");
		_subjectLine = "Notification: Buyer added to your ".concat(brand)
				.concat(".com").concat(" account.");
		if(log.isDebugEnabled()){
			log.debug("_subjectLine: " + _subjectLine);
		}
		inputDocument.getDocumentElement()
				.setAttribute("Subject", _subjectLine);
		if(log.isDebugEnabled()){
		log.debug("inputDocument with SubjectLine: "
				+ SCXmlUtil.getString(inputDocument));
		}
		return inputDocument;
	}

	public Document stampSubjectLine_BuyerRemoved(YFSEnvironment env,
			Document inputDocument) throws Exception {
		String _subjectLine = null;

		String brand = inputDocument.getDocumentElement().getAttribute(
				"SellerOrganizationCode");
		_subjectLine = "Notification: Buyer Removed from ".concat(brand)
				.concat(".com").concat(" account ");
		if(log.isDebugEnabled()){
			log.debug("_subjectLine: " + _subjectLine);
		}
		inputDocument.getDocumentElement()
				.setAttribute("Subject", _subjectLine);
		if(log.isDebugEnabled()){
		log.debug("inputDocument with SubjectLine: "
				+ SCXmlUtil.getString(inputDocument));
		}
		return inputDocument;
	}

	/*******************************************************************/
	/**
	 * @param env
	 * @param customerElement
	 * @return
	 * @throws YFSException
	 * @throws RemoteException
	 */
	public static String getSellerOrganizationCode(YFSEnvironment env,
			String brandName) throws YFSException, RemoteException {

		String organizationCode = "";
		YFCDocument inputDoc = YFCDocument.createDocument("CommonCode");
		YFCElement inputElement = inputDoc.getDocumentElement();
		inputElement.setAttribute("CodeType", "XPXBrandSF");
		inputElement.setAttribute("CodeValue", brandName);
		inputElement.setAttribute("OrganizationCode", "DEFAULT");
		if(log.isDebugEnabled()){
		log.debug("The input to getCommonCodeList is: "
				+ SCXmlUtil.getString(inputDoc.getDocument()));
		}
		Document outputListDoc = api.invoke(env, "getCommonCodeList",
				inputDoc.getDocument());
		if(log.isDebugEnabled()){
		log.debug("The output of getCommonCodeList is: "
				+ SCXmlUtil.getString(outputListDoc));
		}
		NodeList orgList = outputListDoc.getElementsByTagName("CommonCode");
		int orgLength = orgList.getLength();
		if (orgLength != 0) {
			Element listElement = (Element) orgList.item(0);
			organizationCode = listElement.getAttribute("CodeShortDescription");
		}
		if(log.isDebugEnabled()){
			log.debug("The organizationCode retruned from method to retrieve the OrgCode is: " + organizationCode);
		}
		return organizationCode;
	}
	/***************************** Arun's changes end here **************************************/

	/**
	 * @param env
	 * @param invIndicator - inventory indicator
	 * @param itemId
	 * @param lineType - Line type of the line.
	 * @return stockType - stock type of the line item.
	 *
	 * @throws YFSException
	 * @throws RemoteException
	 */
	public static String getLineTypeFromInventoryIndicator(YFSEnvironment env,
			String invIndicator, String itemId, String lineType) throws YFSException, RemoteException {

		String stockType = "";
		boolean callItemList = false;
		Document itemDoc;
		if(log.isDebugEnabled()){
			log.debug("Inventory Indicator : " + invIndicator);
		}
		if(YFCObject.isNull(invIndicator) || YFCObject.isVoid(invIndicator)) {
			callItemList = true;
		} else {
			if("W".equalsIgnoreCase(invIndicator) || "I".equalsIgnoreCase(invIndicator)) {
				stockType = "STOCK";
			} else if("M".equalsIgnoreCase(invIndicator)) {
				stockType = "DIRECT";
			}
		}

		if(callItemList) {
			YFCDocument getItemListOutXML = null;

			YFCDocument getItemListInXML = YFCDocument.getDocumentFor("<Item/>");
			YFCElement itemListEle = getItemListInXML.getDocumentElement();
			itemListEle.setAttribute("ItemID", itemId);

			if(log.isDebugEnabled()){
				log.debug("XPXUtils-XPXGetItemList-InXML:"+getItemListInXML.getString());
			}
			log.verbose("");
			log.verbose("XPXUtils-XPXGetItemList-InXML:"+getItemListInXML.getString());

			itemDoc = api.executeFlow(env, "XPXGetItemList", getItemListInXML.getDocument());
			if(itemDoc != null) {
				getItemListOutXML = YFCDocument.getDocumentFor(itemDoc);
				YFCElement _itemListEle = getItemListOutXML.getDocumentElement();
				YFCElement itemEle = _itemListEle.getChildElement("Item");
				if(itemEle != null) {
					stockType = "DIRECT";
				} else {
					if(lineType.equalsIgnoreCase("M") || lineType.equalsIgnoreCase("C")) {
						stockType = "STOCK";
					}
				}
			}
		}
		return stockType;
	}

	/**
	 * @param env
	 * @param shipToID - Ship To ID of the customer.
	 * @return allowDirectOrderFlag - returns the flag whether direct orders are allowed or not.
	 *
	 * @throws YFSException
	 * @throws RemoteException
	 */
	public static String getAllowDirectOrderForShipToCust(YFSEnvironment env,
			String shipToID) throws YFSException, RemoteException {

		String allowDirectOrderFlag = "";

		// To get the allow direct order flag from ship to customer.
		Document getCustListInDoc = YFCDocument.createDocument("Customer").getDocument();
		getCustListInDoc.getDocumentElement().setAttribute("CustomerID", shipToID);

		Document custListOutDoc = api.executeFlow(env, "XPXGetCustomerList", getCustListInDoc);

		// To retrieve allowDirectOrderFlag from the customer output document.
		if(custListOutDoc.hasChildNodes()){
			Element extnElement = (Element) custListOutDoc.getDocumentElement().getElementsByTagName("Extn").item(0);
			if(extnElement.hasAttribute("ExtnAllowDirectOrderFlag")){
				allowDirectOrderFlag = extnElement.getAttribute("ExtnAllowDirectOrderFlag");
			}
		}
		if(log.isDebugEnabled()){
			log.debug("");
			log.debug("allowDirectOrderFlag : " + allowDirectOrderFlag);
			log.debug("");
			log.debug("allowDirectOrderFlag = " + allowDirectOrderFlag);
		}
		return allowDirectOrderFlag;
	}

	public Document getAdditionalAttributes(YFSEnvironment env,
			Document inputDocument) throws Exception {

		if(log.isDebugEnabled()){
			log.debug("getAdditionalAttributes Start Method - inputDocument : "+SCXmlUtil.getString(inputDocument) );
		}
		String storeFrontId = SCXmlUtil.getAttribute(
				inputDocument.getDocumentElement(), "EnterpriseCode");
		StringBuffer sb = new StringBuffer();

		try{
			String emailSubject="";
			String emailType="";
			if(storeFrontId!=null && storeFrontId.length()>0)
			{
				String userName = YFSSystem.getProperty("fromAddress.username");
				String suffix = YFSSystem.getProperty("fromAddress.suffix");
				//EB-1723 As a Saalfeld product owner, I want to view the Saalfeld New User Email with correct Saalfeld branding
				if("Saalfeld".equalsIgnoreCase(storeFrontId)){
					sb.append(YFSSystem.getProperty("saalFeldEMailFromAddresses"));
				}
				else
				sb.append(YFSSystem.getProperty("EMailFromAddresses"));
				// EB-2447 As a Saalfeld Product owner, I want to view the Saalfeld Password Reset Request Notification email ...
				String resetSubString =null;
				if("Saalfeld".equalsIgnoreCase(storeFrontId)){
					resetSubString= storeFrontId + "redistribution.com" + " Password Reset Request Notification ";
				}else{
					resetSubString= storeFrontId + ".com" + " Password Reset Request Notification ";
					}
				Element resetSubject = SCXmlUtil.createChild(inputDocument.getDocumentElement(),
				"ResetPwdEmailSubject");
					resetSubject.setAttribute("Subject", resetSubString);

				String requestID =SCXmlUtil.getXpathAttribute(inputDocument.getDocumentElement(), "/User/User/@RequestId");
				String genPwd =SCXmlUtil.getXpathAttribute(inputDocument.getDocumentElement(), "/User/User/@GeneratedPassword");


				if(requestID!=null && !requestID.equalsIgnoreCase(""))
				{
					emailType=XPXEmailUtil.USER_RESET_PASSWORD_EMAIL_TYPE;
					// EB-2447 As a Saalfeld Product owner, I want to view the Saalfeld Password Reset Request Notification email ...
					if("Saalfeld".equalsIgnoreCase(storeFrontId)){
						emailSubject=storeFrontId + "redistribution.com" +" Password Reset Request Notification ";
					} else{
						emailSubject=storeFrontId + ".com" + " Password Reset Request Notification ";
					}
				}
				else if(genPwd != null && !genPwd.equalsIgnoreCase(""))
			    {
					emailType=XPXEmailUtil.USER_NOTIFICATION_EMAIL_TYPE;
					//EB-1723 As a Saalfeld product owner, I want to view the Saalfeld New User Email with correct Saalfeld branding...
					if("xpedx".equalsIgnoreCase(storeFrontId)){
					emailSubject = storeFrontId + ".com" + " User Creation Notification";
					}else if("Saalfeld".equalsIgnoreCase(storeFrontId)){
						emailSubject = storeFrontId + "redistribution.com" + " User Creation Notification";
					}
				}
				else if((null==requestID || requestID.equalsIgnoreCase("")) && (genPwd == null || genPwd.equalsIgnoreCase("")))
				{
					emailType=XPXEmailUtil.USER_CHANGE_PASSWORD_EMAIL_TYPE;
					//emailSubject=storeFrontId + ".com" + " User Password Change Notification ";
					//EB-2445 As a Saalfeld product owner, I want to view the Saalfeld User Password Change email with correct Saalfeld branding..
					if("xpedx".equalsIgnoreCase(storeFrontId)){
						emailSubject = storeFrontId + ".com" + " User Password Change Notification";
						}
						else if("Saalfeld".equalsIgnoreCase(storeFrontId)){
						emailSubject = storeFrontId + "redistribution.com" + " User Password Change Notification";
						}
				}
				Element notificationSubject = SCXmlUtil.createChild(inputDocument.getDocumentElement(),
					"NotificationPwdEmailSubject");
					notificationSubject.setAttribute("Subject", emailSubject);

					String readENV = YFSSystem.getProperty("environment");

					if(readENV!=null)
					{
						Element notificationEnvironment = SCXmlUtil.createChild(inputDocument.getDocumentElement(),
						"NotificationENV");
						if(readENV.trim().equalsIgnoreCase("DEVELOPMENT")){
							notificationEnvironment.setAttribute("environment", "dev.");

						}else if(readENV.trim().equalsIgnoreCase("STAGING")){
							notificationEnvironment.setAttribute("environment", "stg.");

						}
						else
						{
							notificationEnvironment.setAttribute("environment","");
						}
					}

				String imageName = getLogoImageName(env,storeFrontId);
				String imagesRootFolder = YFSSystem.getProperty("ImagesRootFolder");

				if ("Saalfeld".equals(storeFrontId)) {
					// replace host name, since we don't have a brand-specific setting in customer overrides props
					imagesRootFolder = imagesRootFolder.replace("xpedx.com", "saalfeldredistribution.com");
				}

				/**
				 * In case, value form the property file is not retrieve by any
				 * chance or there is no entry in the customer_overrides.properties,
				 * the property will be retrieved form the SDF
				 **/
				if (imagesRootFolder == null
						|| imagesRootFolder.trim().length() <= 0) {
					imagesRootFolder = _properties
							.getProperty("IMAGES_ROOT_FOLDER");
				}
				log.debug("imagesRootFolder: " + imagesRootFolder);

				String url = imagesRootFolder + imageName;
				Element brandImageUrl = SCXmlUtil.createChild(inputDocument.getDocumentElement(),
					"BrandImageURL");
				brandImageUrl.setAttribute("URL",url);

			}

			Element fromAddrElem = SCXmlUtil.createChild(inputDocument.getDocumentElement(),
					"FromAddr");
			fromAddrElem.setAttribute("Email", sb.toString());

			// fetching the server and port details for the email template.
			String url = null;
			String ipaddress = YFSSystem.getProperty("ipaddress");



			String portno = YFSSystem.getProperty("portnumber");
			String resetPasswordUrl = YFSSystem.getProperty("ResetPasswordUrl");

			if ("Saalfeld".equals(storeFrontId)) {
				resetPasswordUrl = resetPasswordUrl.replace("xpedx.com", "saalfeldredistribution.com");
			}

			if(!YFCUtils.isVoid(resetPasswordUrl)){
				resetPasswordUrl = resetPasswordUrl + "/swc/home/resetPassword.action?";
				Element urlElem = SCXmlUtil.createChild(inputDocument.getDocumentElement(),
				"URLInfo");
				urlElem.setAttribute("URL", resetPasswordUrl);
			} else {
				if(!(YFCUtils.isVoid(ipaddress)) && !(YFCUtils.isVoid(portno))){
					url = "http://" +ipaddress+":"+portno+"/swc/home/resetPassword.action?";
				}
				if(!YFCUtils.isVoid(url)){
					Element urlElem = SCXmlUtil.createChild(inputDocument.getDocumentElement(),
					"URLInfo");
					urlElem.setAttribute("URL", url);
				}
			}

			/*XB-461 : Begin - Sending email through Java Mail API now*/
			String emailXML=SCXmlUtil.getString(inputDocument);
			String businessIdentifier =SCXmlUtil.getXpathAttribute(inputDocument.getDocumentElement(), "/User/@Loginid");
	        XPXEmailUtil.insertEmailDetailsIntoDB(env, emailXML, emailType, emailSubject, sb.toString(), storeFrontId,businessIdentifier);
	        /*XB-461 : End - Sending email through Java Mail API now*/

		}catch(Exception e){
			e.printStackTrace();
			log.error("XPXUtils:getAdditionalAttributes" + e.getMessage());

		}

		if(log.isDebugEnabled()){
			log.debug("End - getAdditionalAttributes method - Email was sent successfully : "+SCXmlUtil.getString(inputDocument) );
		}

		return inputDocument;
	}

	public  void deleteFromPLTUserLoginFailAgent(YFSEnvironment env,Document inputDocument)throws Exception{
		try{
			String inputXML = SCXmlUtil.getString(inputDocument);
			String user_key =SCXmlUtil.getXpathAttribute(inputDocument.getDocumentElement(), "/User/User/@UserKey");
			System.out.println("**********Input for reset password in delete is : "+inputXML);
			System.out.println("******************************userkey="+user_key);
			PLTQueryBuilder pltQryBuilder = PLTQueryBuilderHelper.createPLTQueryBuilder();
			pltQryBuilder.setCurrentTable("PLT_USER_LOGIN_FAIL");
			pltQryBuilder.appendString("USER_KEY", "=", user_key);
			int loginFail = PLT_User_Login_FailDBHome.getInstance().deleteWithWhere((YFSContext)env, pltQryBuilder);

		}

	catch(Exception e){
		log.error("Exception: " + e.getStackTrace());

	}

	}
	public static XPath getXPathInstance()
	{
		synchronized (xPathFactory) {
	      return xPathFactory.newXPath();
	    }
	}

	public static final List<Element> getElements(Node node, String expression) throws XPathExpressionException
    {
	    XPathExpression xpr = getXPathInstance().compile(expression);
	    NodeList list=null;
	    if(xpr!=null)
	    	list = (NodeList)xpr.evaluate(node, XPathConstants.NODESET);

	    List toReturn = new ArrayList();
	    if(list!=null)
	    {
		    for (int i = 0; i < list.getLength(); i++)
		    {
		      Node n = list.item(i);
		      if (1 != n.getNodeType())
		        continue;
		      toReturn.add(n);
		    }
	    }
	    return toReturn;
	}

	public boolean checkIfOrderOnPendingHold(Document orderDocument) {

		YFCDocument yfcOrderDetailDoc = YFCDocument.getDocumentFor(orderDocument);

	    boolean isOrderOnHold = false;
	    if (log.isDebugEnabled()) {
	    	log.debug("XPXUtils_checkIfOrderOnPendingHold(): "+ yfcOrderDetailDoc.getString());
	    }

	    YFCElement rootElem = yfcOrderDetailDoc.getDocumentElement();
	    YFCElement orderHoldTypesElem = rootElem.getChildElement(XPXLiterals.E_ORDER_HOLD_TYPES);
	    if (orderHoldTypesElem != null) {
	    	YFCIterable<YFCElement> holdTypeItr = orderHoldTypesElem.getChildren("OrderHoldType");
	    	while (holdTypeItr.hasNext()) {
	        	   YFCElement orderHoldTypeElem = holdTypeItr.next();
	        	   if (orderHoldTypeElem != null) {
	        		   String holdType = orderHoldTypeElem.getAttribute(XPXLiterals.A_HOLD_TYPE);
			    	   String holdStatus = orderHoldTypeElem.getAttribute(XPXLiterals.A_STATUS);
			    	   if(!YFCObject.isNull(holdType)&& holdType.equalsIgnoreCase("ORDER_LIMIT_APPROVAL") && !YFCObject.isNull(holdStatus) && holdStatus.equalsIgnoreCase(XPXLiterals.HOLD_CREATED_STATUS_ID)) {
			    		   if (log.isDebugEnabled()) {
			    			   log.debug("Order Is On Pending Approval Hold. No Chained Order Will Be Created");
			    		   }
			               isOrderOnHold = true;
			               break;
			            }
	        	   }

	    	}
	    }
	    return isOrderOnHold;
	}

	public static String getFormattedOrderNumber(String orderBranch, String legacyOrderNum, String generationNum)
	{
		StringBuffer sb = new StringBuffer();

		if(orderBranch!=null && orderBranch.length()>0)
		{
			if(orderBranch.length()>2) {
		    	orderBranch = orderBranch.substring(0, orderBranch.length() - 2);
		    }
			sb.append(orderBranch);
			sb.append("-");
		}
		if(legacyOrderNum!=null && legacyOrderNum.length()>0)
		{
			sb.append(legacyOrderNum);
			sb.append("-");
		}
		if(generationNum!=null && generationNum.length()>0)
		{
			if(generationNum.trim().length()==1)
			{
				generationNum="0"+generationNum;
			}
			sb.append(generationNum);

		}

		return sb.toString();
	}

	public static HashMap<String, String> readCentPropertiesFile(){
		HashMap<String, String> map = null;
		try
	    {
			 map = XPXUtils.getProperties(propertyFile);
	    }
	    catch(Exception e)
	    {
	         log.error("Failed to read from " + propertyFile + " file.");
	    }
	    return map;
	}

	public static HashMap<String, String> getProperties(String infile) throws IOException {
        final int lhs = 0;
        final int rhs = 1;

        HashMap<String, String> map = new HashMap<String, String>();
        BufferedReader  bfr = new BufferedReader(new FileReader(new File(infile)));
        String line;
        while ((line = bfr.readLine()) != null) {
            if (!line.startsWith("#") && !line.isEmpty()) {
                String[] pair = line.trim().split("=");
                map.put(pair[lhs].trim(), pair[rhs].trim());
            }
        }
        bfr.close();
        return(map);
    }

	/**
	 * @param applyMinimumOrderBrands The comma-delimited list of brands that apply minimum order charge.
	 * @param storefrontId
	 * @return Returns true if minimum order charge should be applied; false otherwise.
	 */
	public static boolean isApplyMinimumOrderChargeForBrand(String applyMinimumOrderBrands, String storefrontId) {
		if (applyMinimumOrderBrands == null || storefrontId == null) {
			return false;
		}

		// database stores storefrontId as uppercase and truncated at 4 characters (eg, "XPED,SAAL")
		//		so we can uppercase and truncate the storefrontId
		String searchKey = storefrontId.length() > 4 ? storefrontId.substring(0, 4) : storefrontId;
		searchKey = searchKey.toUpperCase();
		return applyMinimumOrderBrands.contains(searchKey);
	}


	public String getOrderStatusSubjectLine(YFSEnvironment env,
			Element orderElement, String holdStatus, String orderStatusSubjectLine) throws Exception {

		String brand = orderElement.getAttribute("EnterpriseCode");
		String customerPO = orderElement.getAttribute("CustomerPONo");
		String formattedOrderNo = orderElement.getAttribute("FormattedOrderNo");
		StringBuilder _subjectLine = null;
		if("Saalfeld".equalsIgnoreCase(brand)){
			_subjectLine = new StringBuilder(brand.concat("redistribution.com ").concat(orderStatusSubjectLine));
		}else{
			_subjectLine = new StringBuilder(brand.concat(".com ").concat(orderStatusSubjectLine));
		}

		if(!YFCObject.isVoid(customerPO))
		{
			_subjectLine.append(" - PO # ").append(customerPO);
		}
		if ("1300".equalsIgnoreCase(holdStatus) && (!YFCObject.isVoid(formattedOrderNo)))
		{
			if(!YFCObject.isVoid(customerPO))
			{
				_subjectLine.append(", Order # ").append(formattedOrderNo);

			}else {
				_subjectLine.append("- Order # ").append(formattedOrderNo);
			}
		}
		//orderElement.setAttribute("Subject", _subjectLine.toString());
		return _subjectLine.toString();
		//log.debug("_subjectLine: " + _subjectLine);
	}

	public YFCDocument createOrderApprovedEmailInputDoc(YFCElement cOrderEle) {
		//	<OrderHoldType FromStatus='' HoldType='' OrderHeaderKey='' ReasonText='' ResolverUserId='' Status='' TransactionId=''>
		//		<Order BillToID='' CustomerContactID='' DocumentType='' EnterpriseCode='' OrderHeaderKey='' OrderNo=''/>
		//	</OrderHoldType>

		YFCElement cOrderHoldTypesElem = cOrderEle.getChildElement(XPXLiterals.E_ORDER_HOLD_TYPES);
		YFCElement cOrderHoldTypeElem  = cOrderHoldTypesElem.getChildElement(XPXLiterals.E_ORDER_HOLD_TYPE);

		YFCDocument orderHoldTypeDoc = YFCDocument.createDocument(XPXLiterals.E_ORDER_HOLD_TYPE);
		YFCElement orderHoldTypeEle = orderHoldTypeDoc.getDocumentElement();
		orderHoldTypeEle.setAttribute(XPXLiterals.A_HOLD_TYPE, cOrderHoldTypeElem.getAttribute(XPXLiterals.A_HOLD_TYPE));
		orderHoldTypeEle.setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, cOrderHoldTypeElem.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY));
		orderHoldTypeEle.setAttribute(XPXLiterals.HOLD_RELEASE_DESC, cOrderHoldTypeElem.getAttribute(XPXLiterals.HOLD_RELEASE_DESC));
		orderHoldTypeEle.setAttribute(XPXLiterals.RESOLVER_USER_ID, cOrderHoldTypeElem.getAttribute(XPXLiterals.RESOLVER_USER_ID));
		orderHoldTypeEle.setAttribute(XPXLiterals.A_STATUS, cOrderHoldTypeElem.getAttribute(XPXLiterals.A_STATUS));
		orderHoldTypeEle.setAttribute(XPXLiterals.A_TRANSACTION_ID, cOrderHoldTypeElem.getAttribute(XPXLiterals.A_TRANSACTION_ID));

		YFCElement orderEle = orderHoldTypeEle.createChild(XPXLiterals.E_ORDER);
		orderEle.setAttribute(XPXLiterals.A_BILL_TO_ID, cOrderEle.getAttribute(XPXLiterals.A_BILL_TO_ID));
		orderEle.setAttribute(XPXLiterals.CUSTOMER_CONTACT_ID, cOrderEle.getAttribute(XPXLiterals.CUSTOMER_CONTACT_ID));
		orderEle.setAttribute(XPXLiterals.A_DOCUMENT_TYPE, cOrderEle.getAttribute(XPXLiterals.A_DOCUMENT_TYPE));
		orderEle.setAttribute(XPXLiterals.A_ENTERPRISE_CODE, cOrderEle.getAttribute(XPXLiterals.A_ENTERPRISE_CODE));
		orderEle.setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, cOrderEle.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY));
		orderEle.setAttribute(XPXLiterals.A_ORDER_NO, cOrderEle.getAttribute(XPXLiterals.A_ORDER_NO));

		return orderHoldTypeDoc;
	}

	public void callChangeOrder(YFSEnvironment env, String cOrderHeaderKey, String orderConfirmationEmailSentFlag, String className) {
		YFCDocument changeOrderInputDoc=null;
		try {
			changeOrderInputDoc = YFCDocument.createDocument(XPXLiterals.E_ORDER);
			changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, cOrderHeaderKey);

			YFCElement extnOrderEle = changeOrderInputDoc.getDocumentElement().createChild("Extn");
			extnOrderEle.setAttribute(XPXLiterals.ORDER_CONFIRMATION_EMAIL_SENT_FLAG, orderConfirmationEmailSentFlag);

			if(log.isDebugEnabled()){
				log.debug("Inside callChangeOrder method of "+className+" class. changeOrder-InXML: " + changeOrderInputDoc.getString());
			}
			api.invoke(env, "changeOrder", changeOrderInputDoc.getDocument());

		}catch (Exception e) {
			e.printStackTrace();
			log.error("Inside callChangeOrder method of "+className+" class. changeOrder API call failed while updating ExtnOrderConfirmationEmailSentFlag value: ["
						+ orderConfirmationEmailSentFlag+"] for order: ["+cOrderHeaderKey+"]");

			prepareErrorObject(e, XPXLiterals.OU_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, changeOrderInputDoc.getDocument());

		}
	}

	public boolean updateEmailSentFlag(YFSEnvironment env, String cOrderHeaderKey, String orderConfirmationEmailSentFlag, String className) {
		YFCDocument changeOrderInputDoc=null;
		try {
			changeOrderInputDoc = YFCDocument.createDocument(XPXLiterals.E_ORDER);
			changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, cOrderHeaderKey);

			YFCElement extnOrderEle = changeOrderInputDoc.getDocumentElement().createChild("Extn");
			extnOrderEle.setAttribute(XPXLiterals.ORDER_CONFIRMATION_EMAIL_SENT_FLAG, orderConfirmationEmailSentFlag);

			if(log.isDebugEnabled()){
				log.debug("Inside callChangeOrder method of "+className+" class. changeOrder-InXML: " + changeOrderInputDoc.getString());
			}
			api.invoke(env, "changeOrder", changeOrderInputDoc.getDocument());
			return true;

		}catch (Exception e) {
			e.printStackTrace();
			log.error("Inside callChangeOrder method of "+className+" class. changeOrder API call failed while updating ExtnOrderConfirmationEmailSentFlag value: ["
						+ orderConfirmationEmailSentFlag+"] for order: ["+cOrderHeaderKey+"]");

			prepareErrorObject(e, XPXLiterals.OU_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, changeOrderInputDoc.getDocument());
	        return false;
		}
	}

}
