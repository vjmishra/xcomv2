package com.xpedx.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.Security;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.xpedx.api.common.XpedxPropertyValueHelper;
//import com.labcorp.logger.Logger;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.utils.SCUIUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.ui.backend.YCPBackendUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.japi.IYFSApplicationSecurityContextAware;
import com.yantra.yfs.japi.YFSEnvironment;

public class XpedxUtil {

	public static final String AT_CYPHER_KEY = "Sterl1ng";

	/**
	 * Logger Instance.
	 */
//	private static Logger logger = Logger
//	.getLogger(XpedxUtil.class.getName());
	private static Logger logger = Logger.getLogger(XpedxUtil.class);

	// Utility Class - Mask Constructor
	private XpedxUtil() {
	}

	/**
	 * Instance of YIFApi used to invoke Yantra APIs or services.
	 */
	private static YIFApi api = null;

	static {
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (Exception e) {
			logger.error("IOM_UTIL_0001", e);
		}
	}

	/**
	 * Stores the object in the environment under a certain key.
	 *
	 * @param env
	 *            Yantra Environment Context.
	 * @param key
	 *            Key to identify object in environment.
	 * @param value
	 *            Object to be stored in the environment under the given key.
	 * @return Previous object stored in the environment with the same key (if
	 *         present).
	 */
	public static Object setContextObject(YFSEnvironment env, String key,
			Object value) {
		Object oldValue = null;
		Map map = env.getTxnObjectMap();
		if (map != null) {
			oldValue = map.get(key);
		}
		env.setTxnObject(key, value);
		return oldValue;
	}

	/**
	 * Retrieves the object stored in the environment under a certain key.
	 *
	 * @param env
	 *            Yantra Environment Context.
	 * @param key
	 *            Key to identify object in environment.
	 * @return Object retrieved from the environment under the given key.
	 */
	public static Object getContextObject(YFSEnvironment env, String key) {
		return env.getTxnObject(key);
	}

	/**
	 * Retrieves the property stored in the environment under a certain key.
	 *
	 * @param env
	 *            Yantra Environment Context.
	 * @param key
	 *            Key to identify object in environment.
	 * @return Poperty retrieved from the environment under the given key.
	 */
	public static String getContextProperty(YFSEnvironment env, String key) {
		String value = null;
		Object obj = env.getTxnObject(key);
		if (obj != null) {
			value = obj.toString();
		}
		return value;
	}

	/**
	 * Removes an object from the environment.
	 *
	 * @param env
	 *            Yantra Environment Context.
	 * @param key
	 *            Key to identify object in environment.
	 * @return The object stored in the environment under the specified key (if
	 *         any).
	 */
	public static Object removeContextObject(YFSEnvironment env, String key) {
		Object oldValue = null;
		Map map = env.getTxnObjectMap();
		if (map != null) {
			oldValue = map.remove(key);
		}
		return oldValue;
	}

	/**
	 * Clears the environment of any user objects stored.
	 *
	 * @param env
	 *            Yantra Environment Context.
	 */
	public static void clearContextProperties(YFSEnvironment env) {
		Map map = env.getTxnObjectMap();
		if (map != null) {
			map.clear();
		}
	}

	/**
	 * Determines if the input document is a SOAP Fault message.
	 *
	 * @param doc
	 *            Input Document to check.
	 * @return true if SOAP Fault, false otherwise.
	 */
	/*
	 * public static boolean isFaultMessage(Document doc) { return
	 * isFaultMessage(doc.getDocumentElement()); }
	 */

	/**
	 * Invokes a Yantra API.
	 *
	 * @param env
	 *            Yantra Environment Context.
	 * @param apiName
	 *            Name of API to invoke.
	 * @param inDoc
	 *            Input Document to be passed to the API.
	 * @throws java.lang.Exception
	 *             Exception thrown by the API.
	 * @return Output of the API.
	 */
	public static Document invokeAPI(YFSEnvironment env, String apiName,
			Document inDoc) throws Exception {
		Calendar startTime = Calendar.getInstance();
		Document outDoc = null;

		try {
			outDoc = api.invoke(env, apiName, inDoc);
		} catch (Exception ex) {
			//logger.logEntry(env, inDoc, apiName);
			logger.error("Error while invoking API :: " + apiName + " :: ", ex);
			throw ex;
		}
		//logger.logExit(env, apiName, inDoc, outDoc, startTime);
		return outDoc;
	}

	/**
	 * Invokes a Yantra API.
	 *
	 * @param env
	 *            Yantra Environment Context.
	 * @param apiName
	 *            Name of API to invoke.
	 * @param str
	 *            Input to be passed to the API. Should be a valid XML string.
	 * @throws java.lang.Exception
	 *             Exception thrown by the API.
	 * @return Output of the API.
	 */
	public static Document invokeAPI(YFSEnvironment env, String apiName,
			String str) throws Exception {
		return api.invoke(env, apiName, YFCDocument.parse(str).getDocument());
	}

	/**
	 * Invokes a Yantra Service.
	 *
	 * @param env
	 *            Yantra Environment Context.
	 * @param serviceName
	 *            Name of Service to invoke.
	 * @param inDoc
	 *            Input Document to be passed to the Service.
	 * @throws java.lang.Exception
	 *             Exception thrown by the Service.
	 * @return Output of the Service.
	 */
	public static Document invokeService(YFSEnvironment env,
			String serviceName, Document inDoc) throws Exception {

		Calendar startTime = Calendar.getInstance();
		Document outDoc = null;

		try {
			outDoc = api.executeFlow(env, serviceName, inDoc);
		} catch (Exception ex) {
			//logger.logEntry(env, inDoc, serviceName);
			logger.error("Error while invoking Service :: " + serviceName
					+ " :: ", ex);
			throw ex;
		}
		//logger.logExit(env, serviceName, inDoc, outDoc, startTime);
		return outDoc;
	}

	/**
	 * Invokes a Yantra Service.
	 *
	 * @param env
	 *            Yantra Environment Context.
	 * @param serviceName
	 *            Name of Service to invoke.
	 * @param str
	 *            Input to be passed to the Service. Should be a valid XML
	 *            String.
	 * @throws java.lang.Exception
	 *             Exception thrown by the Service.
	 * @return Output of the Service.
	 */
	public static Document invokeService(YFSEnvironment env,
			String serviceName, String str) throws Exception {
		return api.executeFlow(env, serviceName, YFCDocument.parse(str)
				.getDocument());
	}

	/**
	 * Invokes a Yantra Service.
	 *
	 * @param env
	 *            Yantra Environment Context.
	 * @param apiName
	 *            Name of Extended DB API to invoke.
	 * @param inDoc
	 *            Input Document to be passed to the Service.
	 * @throws java.lang.Exception
	 *             Exception thrown by the Service.
	 * @return Output of the Service.
	 */
	public static Document invokeExtendedDbAPI(YFSEnvironment env,
			String apiName, Document inDoc) throws Exception {
		String apiClassName = YCPBackendUtils.getExtnApiClassName(apiName);
		String apiMethodName = YCPBackendUtils.getExtnApiMethodName(apiName);

		Class c = Class.forName(apiClassName);
		Method m = c.getMethod(apiMethodName, new Class[] {
				com.yantra.yfs.japi.YFSEnvironment.class,
				org.w3c.dom.Document.class });

		Calendar startTime = Calendar.getInstance();
		Document outDoc = null;

		try {
			outDoc = (Document) m.invoke(c.newInstance(), new Object[] { env,
				inDoc });
		} catch (Exception ex) {
			//logger.logEntry(env, inDoc, apiName);
			logger.error("Error while invoking DB API :: " + apiName + " :: ",
					ex);
			
			throw (Exception) ex.getCause();
		}
		// enabled logger with memory check.
		//logger.logExit(env, apiName, inDoc, outDoc, startTime);

		return outDoc;
	}

	/**
	 * For encrypting and decrypting the req, abn and manifest keys for AT.
	 *
	 * @param keys
	 *            Keys.
	 * @throws java.lang.Exception
	 *             Exception thrown by the Service.
	 * @return cyphered keys.
	 */

	public static String encryptString(String keys) {
		String activeTreeFlag = XpedxPropertyValueHelper.getPropertyValue(
				"labcorp.USE_ACTIVE_TREE", "Y");
		if (activeTreeFlag != null
				&& (activeTreeFlag.equalsIgnoreCase("N") || activeTreeFlag
						.equalsIgnoreCase("false"))) {
			return keys;
		}
		try {
			byte[] input = keys.getBytes();
			byte[] keyBytes;

			keyBytes = getEncryptionKey();
			Security.addProvider(new BouncyCastleProvider());
			SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");

			// encryption pass
			cipher.init(Cipher.ENCRYPT_MODE, key);

			byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
			int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
			ctLength += cipher.doFinal(cipherText, ctLength);
			return new String(Base64.encode(cipherText));
		} catch (Exception e) {
			logger.error("Encryption logic failed, sending back the REQ ID: "
					+ e);
			return keys;
		}

	}

	/**
	 * @return
	 * @throws Exception
	 */
	private static byte[] getEncryptionKey() throws Exception {

		String keyString = XpedxPropertyValueHelper.getPropertyValue(
				"labcorp.REQUISTION_KEY_PASSWORD", "Sterl1ng");
		byte[] keyBytes = ("Labcorp" + keyString).getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		keyBytes = sha.digest(keyBytes);

		keyBytes = Arrays.copyOf(keyBytes, 16); // use only first 128 bit
		return keyBytes;
	}

	/**
	 * For encrypting and decrypting the req, abn and manifest keys for AT.
	 *
	 * @param keys
	 *            Keys.
	 * @throws java.lang.Exception
	 *             Exception thrown by the Service.
	 * @return cyphered keys.
	 */
	public static String decryptString(String encyptedString) {
		String activeTreeFlag = XpedxPropertyValueHelper.getPropertyValue(
				"labcorp.USE_ACTIVE_TREE", "Y");
		if (activeTreeFlag != null
				&& (activeTreeFlag.equalsIgnoreCase("N") || activeTreeFlag
						.equalsIgnoreCase("false"))) {
			return encyptedString;
		}
		byte[] input = Base64.decode(encyptedString);
		byte[] keyBytes;
		try {
			keyBytes = getEncryptionKey();

			Security.addProvider(new BouncyCastleProvider());

			SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");

			int ctLength = input.length;
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
			int ptLength = cipher.update(input, 0, ctLength, plainText, 0);
			ptLength += cipher.doFinal(plainText, ptLength);
			return new String(plainText);
		} catch (Exception e) {
			logger.error("Decrypting logic failed, sending back the REQ ID: "
					+ e);
			return encyptedString;
		}

	}

	/*
	 * This very cool method retrieves YFSEnvironment out of SCUIContext
	 */

	public static Object getEnvironmentFromUIContext(SCUIContext uiContext) {
		Object env = uiContext.getTransactionContext().getTransactionObject(
		"YFCTransactionObject");
		if (env instanceof IYFSApplicationSecurityContextAware) {
			IYFSApplicationSecurityContextAware appSecurityEnv = (IYFSApplicationSecurityContextAware) env;
			appSecurityEnv.setApplicationSecurityContext(uiContext
					.getApplicationSecurityContext());
		}
		String userTokenId = (String) uiContext.getSession().getAttribute(
		"UserToken");
		if ((env instanceof YFSEnvironment) && !SCUIUtils.isVoid(userTokenId)) {
			YFSEnvironment yfsEnv = (YFSEnvironment) env;
			yfsEnv.setTokenID(userTokenId);
		}
		uiContext.replaceAttribute("SCUI_XAPICALL_ATTR", "SCUI_XAPICALL");
		return env;
	}

	public static ISCUITransactionContext startNonMashUpTransaction(
			SCUIContext uiContext) {
		ISCUITransactionContext transactionContext = uiContext
		.getTransactionContext(false);
		if (transactionContext == null) {
			if (logger.isDebugEnabled())
				logger
				.debug("LabcorpUtil: Creating a new transaction context.");
			transactionContext = uiContext.getTransactionContext(true);
			transactionContext.begin();
		}
		return transactionContext;
	}

	public static String getRevision() {
		return XpedxPropertyValueHelper.getPropertyValue(
				"labcorp.ReleaseRevision", "55777");
	}

	public static Date getRoundDate(Date inputDate) {
		SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
		Date resultingDate = null;

		try {
			resultingDate = dateOnly.parse(dateOnly.format(inputDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultingDate;
	}

	public static String getStringFromException(Exception exception) {
		StringWriter traceWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(traceWriter, false);
		exception.printStackTrace(printWriter);
		printWriter.close();
		return traceWriter.getBuffer().toString();
	}

}

