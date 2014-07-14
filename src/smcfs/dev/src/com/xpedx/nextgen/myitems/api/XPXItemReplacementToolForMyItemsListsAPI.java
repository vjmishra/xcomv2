package com.xpedx.nextgen.myitems.api;

import java.util.ArrayList;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.woodstock.util.frame.log.base.ISCILogger;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategoryFactory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXItemReplacementToolForMyItemsListsAPI  implements YIFCustomApi{

	/** API object. */
	private static YIFApi api = null;
	private static ISCILogger log = null;
	static {
		log = new YFCLogCategoryFactory().getLogger(XPXUtils.class.getCanonicalName());
	}
	private Properties _properties = null;

	public void setProperties(Properties properties) throws Exception {
		_properties = properties;
		
	}

	/**
	 * 
	 * Input XML Format:
	 * 
	 * <pre>
	 * &lt;XPEDXMyItemsListList LPC=&quot;LEGACY_PRODUCT_CODE&quot; ReplaceWithLPC=&quot;REPLACE_WITH_LEGACY_PRODUCT_CODE&quot;&gt;
	 * &lt;XPEDXMyItemsList MyItemsListKey=&quot;MY_ITEMS_LIST_KEY1&quot;/&gt;
	 * &lt;XPEDXMyItemsList MyItemsListKey=&quot;MY_ITEMS_LIST_KEY2&quot;/&gt;
	 * ...
	 * &lt;XPEDXMyItemsList MyItemsListKey=&quot;MY_ITEMS_LIST_KEYN&quot;/&gt;
	 * &lt;/XPEDXMyItemsListList&gt;
	 * 
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 * 
	 */
	public Document replaceItem(YFSEnvironment env, Document inXML) throws Exception {
		// check if the Environment/Input document passed is null, throw exception
		if (env == null) {
			// Throw new YFSException with the description
			throw new YFSException("YFSEnvironment cannot be null or invalid to XPXItemReplacementToolForMyItemsListsAPI.replaceItem()");
		}
		if (inXML == null) {
			// Throw new YFSException with the description
			throw new YFSException("Input XML document cannot be null or invalid to XPXItemReplacementToolForMyItemsListsAPI.replaceItem()");
		}

		Element eleInput = inXML.getDocumentElement();
		String strLPC = eleInput.getAttribute("LPC");
		String strReplaceLPC = eleInput.getAttribute("ReplaceWithLPC");
		String replaceItemUOM = null;
		if(SCUtil.isVoid(strLPC) 
				|| SCUtil.isVoid(strReplaceLPC)){
			throw new YFSException("LPC and ReplaceLPC attributes cannot be VOID.");
		}
		log.debug("XPXItemReplacementToolForMyItemsListsAPI.replaceItem()--> Input XML" + SCXmlUtil.getString(inXML));
		Document docSuccess = null;
		
		ArrayList<Element> replacibleList = SCXmlUtil.getChildren(eleInput, "XPEDXMyItemsList");
		if(replacibleList.size() > 0){
			api = YIFClientFactory.getInstance().getApi();
			
			//1.  Get list of My Items Items with ItemId=strLPC
			//a. prepare input
			Document docAPIInput = SCXmlUtil.createDocument("XPEDXMyItemsItems");
			docAPIInput.getDocumentElement().setAttribute("ItemId", strLPC);
			Element eleCQ = SCXmlUtil.createChild(docAPIInput.getDocumentElement(), "ComplexQuery");
			Element eleOr = SCXmlUtil.createChild(eleCQ, "Or");
			for (Element eleReplacibleMyItemList : replacibleList) {
				Element eleExp = SCXmlUtil.createChild(eleOr, "Exp");
				eleExp.setAttribute("Name", "MyItemsListKey");
				eleExp.setAttribute("Value", eleReplacibleMyItemList.getAttribute("MyItemsListKey"));
			}
			//b. invoke API and get the Items List
			Document docMyItemsItems = api.executeFlow(env, "getXPEDX_MyItemsItems_List", docAPIInput);
			
			// To Get UOM For The Item(LPC) To Be Replaced.
			Document getItemListInDoc = SCXmlUtil.createDocument("Item");
			getItemListInDoc.getDocumentElement().setAttribute("ItemID", strReplaceLPC);
			env.setApiTemplate("getItemList", SCXmlUtil.createFromString("<ItemList><Item UnitOfMeasure='' /></ItemList>"));
			Document getItemListOutDoc = api.invoke(env,"getItemList", getItemListInDoc);
			env.clearApiTemplate("getItemList");
			YFCDocument itemListOutDoc = YFCDocument.getDocumentFor(getItemListOutDoc);
			YFCElement rootListElem = itemListOutDoc.getDocumentElement();
			if (rootListElem.hasChildNodes()) {
				YFCElement itemElem = rootListElem.getChildElement("Item");
				if (itemElem != null && itemElem.hasAttribute("UnitOfMeasure")) {
					replaceItemUOM = itemElem.getAttribute("UnitOfMeasure");
				}
			}
			
			log.debug("Item To Be Replaced:" + strReplaceLPC);
			if (YFCObject.isNull(replaceItemUOM) || YFCObject.isVoid(replaceItemUOM)) {
				throw new Exception("Invalid Item: "+strReplaceLPC);
			}
			log.debug("Item Unit Of Measure To Be Replaced:" + replaceItemUOM);
			
			//2.  Prepare and invoke changeXPEDX_MyItemsDetails Service for each MyItemsItems
			ArrayList<Element> listOfReplacibleMyItems = SCXmlUtil.getChildren(docMyItemsItems.getDocumentElement(), "XPEDXMyItemsItems");
			if(listOfReplacibleMyItems.size()>0){
				Document docReplaceItem = SCXmlUtil.createDocument("XPEDXMyItemsItemsList");
				docReplaceItem.getDocumentElement().setAttribute("ServiceName", "changeXPEDX_MyItemsDetails");
				for (Element eleReplacibleMyIteme : listOfReplacibleMyItems) {
					Element eleItem = SCXmlUtil.createChild(docReplaceItem.getDocumentElement(), "XPEDXMyItemsItems");
					eleItem.setAttribute("ItemId", strReplaceLPC);
					eleItem.setAttribute("UomId", replaceItemUOM);
					eleItem.setAttribute("MyItemsKey", eleReplacibleMyIteme.getAttribute("MyItemsKey"));
				}
				Document docMultiApiOutput = new XPXUtils().loadDataUsingMutliApi(env, docReplaceItem);
				
				// return Success on no exceptions
				docSuccess = SCXmlUtil.createDocument("Success");
			}
		}
	
		log.debug("XPXItemReplacementToolForMyItemsListsAPI.replaceItem()--> Output XML" + SCXmlUtil.getString(docSuccess));
		api = null;
		if(SCUtil.isVoid(docSuccess)){
			docSuccess = SCXmlUtil.createDocument("Failure");
			docSuccess.getDocumentElement().setAttribute("Reason", "No matching My Items Lists for LPCs found:"+strLPC);
		}
		return docSuccess;
	}
}
