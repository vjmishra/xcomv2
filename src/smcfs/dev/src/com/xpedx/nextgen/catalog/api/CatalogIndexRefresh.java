package com.xpedx.nextgen.catalog.api;

import java.util.Iterator;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.api.common.XpedxYIFCustomApi;
import com.xpedx.common.XpedxUtil;
import com.xpedx.constants.XpedxConstants;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * Service Class for JIRA 3185
 * 
 * @author lahotip
 * 
 */

public class CatalogIndexRefresh extends XpedxYIFCustomApi implements XpedxConstants {
	private static final String STATUS_COMPLETE = "03";
	private static final String STATUS_ACTIVE = "04";
	// private static final Logger LOG = new
	// Logger(StoreSearchIndexFilesAPI.class.getName());
	private static final YFCLogCategory LOG = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	private static YIFApi api = null;

	static {
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}

	/**
	 * CallBack Function called as per configuration invoke
	 * 
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document invoke(YFSEnvironment env, Document inXML) throws Exception {

		LOG.info("Inside Invoke operation of CatalogIndexRefresh " + SCXmlUtil.getString(inXML));
		try {
			// Need to get Second latest Completed element
			YFCElement searchIndexTriggerSecondLatestElement = getSecondLatestSearchIndex(env);
			//flag for verification of Activating Index Operation successfully completed
			boolean activate = false;
			if (searchIndexTriggerSecondLatestElement != null) {
				String searchIndexSecondLatestTriggerKey = searchIndexTriggerSecondLatestElement.getAttribute("SearchIndexTriggerKey");

				activate = activateSearchIndex(env, searchIndexSecondLatestTriggerKey);

			} else {
				activate = true;
			}
			LOG.info("After Getting value of Second Active Index in CatalogIndexRefresh Java File");
			if (activate == true) {
				// Need to get first latest completed element to re-activate it again
				YFCElement searchIndexTriggerLatestElement = getLatestSearchIndex(env);
				String searchIndexLatestTriggerKey = searchIndexTriggerLatestElement.getAttribute("SearchIndexTriggerKey");
				activate = activateSearchIndex(env, searchIndexLatestTriggerKey);
				LOG.info("After Getting value of First Active Index in CatalogIndexRefresh Java File");	

			}
		} catch (Exception exception) {
			LOG.error("Error in Invoke Operation of CatalogIndexRefresh", exception);
			throw new Exception("Error from CatalogIndexRefresh Service");
		}
		return null;
	}

	/**
	 * purpose of this operation is to get latest YFCElement
	 * getLatestSearchIndex
	 * 
	 * @param env
	 * @return YFCElement
	 * @throws Exception
	 */
	private YFCElement getLatestSearchIndex(YFSEnvironment env) throws Exception {
		YFCDocument inputDoc = YFCDocument.createDocument("SearchIndexTrigger");
		YFCElement inputDocElement = inputDoc.getDocumentElement();
		YFCElement inputAttributeElement = inputDocElement.createChild("OrderBy").createChild("Attribute");
		inputAttributeElement.setAttribute("Name", "TriggerTimestamp");
		inputAttributeElement.setAttribute("Desc", "Y");
		Document output = XpedxUtil.invokeAPI(env, "getSearchIndexTriggerList", inputDoc.getDocument());
		YFCElement searchIndexTriggerElement = null;
		if (output != null) {
			YFCDocument yfcOutput = YFCDocument.getDocumentFor(output);
			if (yfcOutput.getDocumentElement() != null) {
				YFCNodeList<YFCElement> list = yfcOutput.getDocumentElement().getElementsByTagName("SearchIndexTrigger");
				if (list != null && list.getLength() > 0) {
					Iterator<YFCElement> iterator = list.iterator();
					while (iterator.hasNext()) {
						searchIndexTriggerElement = iterator.next();
						String status = searchIndexTriggerElement.getAttribute("Status");
						if (status.equalsIgnoreCase(STATUS_COMPLETE)) {
							return searchIndexTriggerElement;
						}
						if (status.equalsIgnoreCase(STATUS_ACTIVE)) {
							return null;
						}
					}
				}
			}
		}
		return searchIndexTriggerElement;

	}

	/**
	 * This operation will return second latest search Index whose status needs
	 * to be completed getSecondLatestSearchIndex
	 * 
	 * @param env
	 * @return
	 * @throws Exception
	 */
	private YFCElement getSecondLatestSearchIndex(YFSEnvironment env) throws Exception {
		YFCDocument inputDoc = YFCDocument.createDocument("SearchIndexTrigger");
		YFCElement inputDocElement = inputDoc.getDocumentElement();
		YFCElement inputAttributeElement = inputDocElement.createChild("OrderBy").createChild("Attribute");
		inputAttributeElement.setAttribute("Name", "TriggerTimestamp");
		inputAttributeElement.setAttribute("Desc", "Y");
		Document output = XpedxUtil.invokeAPI(env, "getSearchIndexTriggerList", inputDoc.getDocument());
		YFCElement searchIndexTriggerElement = null;
		if (output != null) {
			YFCDocument yfcOutput = YFCDocument.getDocumentFor(output);
			if (yfcOutput.getDocumentElement() != null) {
				YFCNodeList<YFCElement> list = yfcOutput.getDocumentElement().getElementsByTagName("SearchIndexTrigger");
				if (list != null && list.getLength() > 0) {
					for (int i = 1; i < list.getLength(); i++) {
						searchIndexTriggerElement = (YFCElement) list.item(i);
						String status = searchIndexTriggerElement.getAttribute("Status");
						if (status.equalsIgnoreCase(STATUS_ACTIVE)) {
							return null;
						}
						if (status.equalsIgnoreCase(STATUS_COMPLETE)) {
							searchIndexTriggerElement = (YFCElement) list.item(i);
							return searchIndexTriggerElement;
						}
					}
				}
			}
		}
		return searchIndexTriggerElement;

	}

	/**
	 * This Operation will activate Search Index based on Search Index Trigger
	 * Key activateSearchIndex
	 * 
	 * @param env
	 * @param searchIndexTriggerKey
	 * @throws Exception
	 */
	private static boolean activateSearchIndex(YFSEnvironment env, String searchIndexTriggerKey) throws Exception {
		try {
			YFCDocument inputDoc = YFCDocument.createDocument("SearchIndexTrigger");
			YFCElement inputDocElement = inputDoc.getDocumentElement();
			inputDocElement.setAttribute("OrganizationCode", "xpedx");//This is Mandatory Field as per Input XML
			inputDocElement.setAttribute("Operation", "Modify");
			inputDocElement.setAttribute("Status", STATUS_ACTIVE);
			inputDocElement.setAttribute("SearchIndexTriggerKey", searchIndexTriggerKey);
			inputDocElement.setAttribute("CategoryDomain", YFSSystem.getProperty("xpedx.searhindex.categoryDomain"));
			Document output = api.invoke(env, "manageSearchIndexTrigger", inputDoc.getDocument());
			LOG.info("Sucessfully activated search index" + searchIndexTriggerKey);
			LOG.info("Output: " + SCXmlUtil.getString(output));
			if (output != null) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			LOG.error("Error while activating the search index", e);
			e.printStackTrace();
			throw new Exception("Error while activating the search index: " + e.getMessage());
		}
	}
}
