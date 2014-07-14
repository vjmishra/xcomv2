package com.sterlingcommerce.simplifieddataaccess;


import java.util.Date;

import org.w3c.dom.Document;

import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.ui.web.framework.utils.SCUIUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.IYFSApplicationSecurityContextAware;
import com.yantra.yfs.japi.YFSEnvironment;

public class QuickQuery {

	private static final YFCLogCategory LOG = YFCLogCategory.instance(QuickQuery.class.getName());


	/**
	 * Instance of YIFApi used to invoke Yantra APIs or services.
	 */
	private static YIFApi api = null;

	static {
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (Exception e) {
			LOG.error("Unable to get api instance");
			e.printStackTrace();
		}
	}

	public static SimpleXML executeApi(SCUIContext uiContext, String apiName, SimpleXML query, SimpleXML template) throws Exception {
		boolean transactionExists = true;
		ISCUITransactionContext transactionContext = uiContext.getTransactionContext(false);
		if (transactionContext==null) {
			transactionExists = false;
			transactionContext = startNonMashUpTransaction(uiContext);
		}
		YFSEnvironment env = (YFSEnvironment)getEnvironmentFromUIContext(uiContext);
		SimpleXML result;
		try {
			result = executeApi(env, apiName, query, template);
			if (!transactionExists) {
				transactionContext.commit();
				transactionContext.end();
				SCUITransactionContextHelper.releaseTransactionContext(transactionContext, uiContext);
				uiContext.removeTransactionContext();
			}
		} catch (Exception e) {
			if (!transactionExists) {
				transactionContext.rollback();
				transactionContext.end();
				SCUITransactionContextHelper.releaseTransactionContext(transactionContext, uiContext);
				uiContext.removeTransactionContext();
			}
			throw e;
		}
		return result;
	}

	public static SimpleXML executeApi(YFSEnvironment env, String apiName, SimpleXML query, SimpleXML template) throws Exception {
		Date start = new Date();
		LOG.debug("Execution API: "+apiName+" Query: "+query+" Template: "+template+" Start: "+(start));
		SimpleXML result = null;
		try {
			if (template!=null)
				env.setApiTemplate(apiName, template.getDOMDocument());
			
			Document resultingDoc = api.invoke(env, apiName, query.getDOMDocument());
			if (resultingDoc!=null)
				result = new SimpleXML(resultingDoc);

			LOG.debug("Query Result:\n"+result);
		} catch (Exception e) {
			throw e;
		}
		Date end = new Date();
		LOG.debug("Execution API: "+apiName+" Query: "+query+" End: "+(end));
		LOG.debug("Execution API: "+apiName+" Took: "+(end.getTime()-start.getTime())+" ms");
		return result;
	}

	public static SimpleXML executeFlow(SCUIContext uiContext, String flowName, SimpleXML query, SimpleXML template) throws Exception {
		boolean transactionExists = true;
		ISCUITransactionContext transactionContext = uiContext.getTransactionContext(false);
		if (transactionContext==null) {
			transactionExists = false;
			transactionContext = startNonMashUpTransaction(uiContext);
		}
		YFSEnvironment env = (YFSEnvironment)getEnvironmentFromUIContext(uiContext);
		SimpleXML result;
		try {
			result = executeFlow(env, flowName, query, template);
			if (!transactionExists) {
				transactionContext.commit();
				transactionContext.end();
				SCUITransactionContextHelper.releaseTransactionContext(transactionContext, uiContext);
				uiContext.removeTransactionContext();
			}
		} catch (Exception e) {
			if (!transactionExists) {
				transactionContext.rollback();
				transactionContext.end();
				SCUITransactionContextHelper.releaseTransactionContext(transactionContext, uiContext);
				uiContext.removeTransactionContext();
			}
			throw e;
		}
		return result;
	}

	public static SimpleXML executeFlow(YFSEnvironment env, String flowName, SimpleXML query, SimpleXML template) throws Exception {
		Date start = new Date();
		LOG.debug("Execution Flow: "+flowName+" Query: "+query+" Start: "+(start));
		SimpleXML result = null;
		try {
			if (template!=null)
				env.setApiTemplate(flowName, template.getDOMDocument());
			Document resultingDoc = api.executeFlow(env, flowName, query.getDOMDocument());
			if (resultingDoc!=null)
				result = new SimpleXML(resultingDoc);
			LOG.debug("Query Result:\n"+result);
		} catch (Exception e) {
			throw e;
		}
		Date end = new Date();
		LOG.debug("Execution Flow: "+flowName+" Query: "+query+" End: "+(end));
		LOG.debug("Execution Flow: "+flowName+" Took: "+(end.getTime()-start.getTime())+" ms");
		return result;
	}

	public static SimpleXML executeDBApi(SCUIContext uiContext, String apiName, SimpleXML query, SimpleXML template) throws Exception {
		boolean transactionExists = true;
		ISCUITransactionContext transactionContext = uiContext.getTransactionContext(false);
		if (transactionContext==null) {
			transactionExists = false;
			transactionContext = startNonMashUpTransaction(uiContext);
		}
		YFSEnvironment env = (YFSEnvironment)getEnvironmentFromUIContext(uiContext);
		SimpleXML result;
		try {
			result = executeDBApi(env, apiName, query, template);
			if (!transactionExists) {
				transactionContext.commit();
				transactionContext.end();
				SCUITransactionContextHelper.releaseTransactionContext(transactionContext, uiContext);
				uiContext.removeTransactionContext();
			}
		} catch (Exception e) {
			if (!transactionExists) {
				transactionContext.rollback();
				transactionContext.end();
				SCUITransactionContextHelper.releaseTransactionContext(transactionContext, uiContext);
				uiContext.removeTransactionContext();
			}
			throw e;
		}
		return result;
	}

	public static SimpleXML executeDBApi(YFSEnvironment env, String apiName, SimpleXML query, SimpleXML template) throws Exception {
		Date start = new Date();
		LOG.debug("Execution API: "+apiName+" Query: "+query+" Template: "+template+" Start: "+(start));
		SimpleXML result = null;
		try {
			
			//Enclosing the call with LCExtendedDb Flow
//			SimpleXML extendedDBInput = SimpleXML.getInstance("(LCExtendedDb (API (Input))(Template))");
			SimpleXML extendedDBInput = SimpleXML.getInstance("(XpedxExtendedDb (API (Input))(Template))");
			extendedDBInput.getChild("API").setAttribute("Name", apiName);
			extendedDBInput.getChild("API").getChild("Input").addChild(query);
			if (template!=null)
				extendedDBInput.getChild("Template").addChild(template);
			
			result = QuickQuery.executeFlow(env, "XpedxExtendedDb", extendedDBInput, null);
			LOG.debug("Query Result:\n"+result);
		} catch (Exception e) {
			throw e;
		}
		Date end = new Date();
		LOG.debug("Execution API: "+apiName+" Query: "+query+" End: "+(end));
		LOG.debug("Execution API: "+apiName+" Took: "+(end.getTime()-start.getTime())+" ms");
		return result;
	}

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
			LOG.debug("Creating a new transaction context.");
			transactionContext = uiContext.getTransactionContext(true);
			transactionContext.begin();
		}
		return transactionContext;
	}
}

