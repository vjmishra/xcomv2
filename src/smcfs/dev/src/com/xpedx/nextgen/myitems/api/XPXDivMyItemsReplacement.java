package com.xpedx.nextgen.myitems.api;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.woodstock.util.frame.log.base.ISCILogger;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategoryFactory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXDivMyItemsReplacement implements YIFCustomApi{

	private static YIFApi api = null;
	private static ISCILogger log = null;
	static {
		log = new YFCLogCategoryFactory().getLogger(XPXUtils.class.getCanonicalName());
		
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Properties _properties = null;

	public void setProperties(Properties properties) throws Exception {
		_properties = properties;
		
	}
	
	/** 
	 *  
	 *   Input XML Format:
	 *    <pre>
	 *  &lt;XPEDXMyItemsList ReplaceWithLPC=""&gt;
    		 &lt;XPEDXMyItemsItemsList &gt;
        		 &lt;XPEDXMyItemsItems ItemId=""/&gt;
    		 &lt;/XPEDXMyItemsItemsList &gt;
    		 &lt;XPEDXMyItemsListShareList>
        		&lt; XPEDXMyItemsListShare DivisionID=""/&gt;
    		 &lt;/XPEDXMyItemsListShareList &gt;
		 &lt;/XPEDXMyItemsList &gt;
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	
	public Document replaceDivItems(YFSEnvironment env, Document inXML) throws Exception {
		
		if (env == null) {
			// Throw new YFSException with the description
			throw new YFSException("YFSEnvironment cannot be null or invalid to XPXItemReplacementToolForMyItemsListsAPI.replaceItem()");
		}
		if (inXML == null) {
			// Throw new YFSException with the description
			throw new YFSException("Input XML document cannot be null or invalid to XPXItemReplacementToolForMyItemsListsAPI.replaceItem()");
		}
		
		Element eleInput = inXML.getDocumentElement();
		Element eleMyItemsItemsList=SCXmlUtils.getChildElement(eleInput, "XPEDXMyItemsItemsList");
		Element eleMyItemsItems=SCXmlUtils.getChildElement(eleMyItemsItemsList,"XPEDXMyItemsItems");
		
		
		Element eleItemsListShareList=SCXmlUtils.getChildElement(eleInput, "XPEDXMyItemsListShareList");
		Element eleItemsListShare=SCXmlUtils.getChildElement(eleItemsListShareList,"XPEDXMyItemsListShare");
		
		String strDivId=eleItemsListShare.getAttribute("DivisionID");
		String currItemId=eleMyItemsItems.getAttribute("ItemId");
		String replaceItem=eleInput.getAttribute("ReplaceWithLPC");
		Document returnDoc=null;
		Document divReturnDoc=getDivMyItemsList(currItemId,strDivId);
				
		Document docMyItemsItems = api.executeFlow(env, "getXPEDX_MyItemsItems_List_For_ReplacementTool_ByDivision", divReturnDoc);

		
		String numberOfRecords=docMyItemsItems.getDocumentElement().getAttribute("TotalNumberOfRecords");
		 int totalRecords = Integer.parseInt(numberOfRecords);
		 if(totalRecords!=0){
		 returnDoc=replaceItemsForDivisions(env,docMyItemsItems,replaceItem);
		 }
		 else{
			 
			 returnDoc = SCXmlUtil.createDocument("UpdatedList");
			 returnDoc.getDocumentElement().setAttribute("TotalNumberOfRecords", "0");
		 }
		return returnDoc;
		
	}
//Replace List Of MyItem List with new Item
	private Document replaceItemsForDivisions(YFSEnvironment env, Document docMyItemsItems, String replaceItem) throws Exception {
		
		Set<String> myItemListSet=new HashSet<String>();
		Element eleMyItemsListEle=docMyItemsItems.getDocumentElement();
		eleMyItemsListEle.setAttribute("ServiceName", "changeXPEDX_MyItemsDetails");
		Document docSuccess = null;
	//	SCXmlUtil.mergeAttributes(arg0, arg1, arg2)
		
		NamedNodeMap map = eleMyItemsListEle.getAttributes();
		NodeList nodeMyItemsList=eleMyItemsListEle.getElementsByTagName("XPEDXMyItemsItems");
		for(int i=0;i<nodeMyItemsList.getLength();i++){
			Element nl=(Element) nodeMyItemsList.item(i);
			nl.setAttribute("ItemId", replaceItem);
			String ItemListKey=nl.getAttribute("MyItemsListKey");
			myItemListSet.add(ItemListKey);
		}
		// TODO Auto-generated method stub
		
		Document docItemListToReplace=eleMyItemsListEle.getOwnerDocument();
		
		Document docMultiApiOutput = new XPXUtils().loadDataUsingMutliApi(env, docItemListToReplace);
		
		int Length=myItemListSet.size();
		// return Success on no exceptions
		docSuccess = SCXmlUtil.createDocument("UpdatedList");
		Element successEle=docSuccess.getDocumentElement();
		successEle.setAttribute("TotalNumberOfRecords",  Integer.toString(Length));


		return docSuccess;
	}

	private Document getDivMyItemsList(String currentItemId, String strDivId) {
		
		Document docItemsDocument=null;
		if(strDivId.contains(",")){
			
			String [] div= strDivId.split(",");
			String divDuery= getDivisionQuery(div);
			docItemsDocument=SCXmlUtils.createFromString("<XPEDXMyItemsItems MaximumRecords='2000' ItemId='"+currentItemId+"'>" +
					"<XPEDXMyItemsList><XPEDXMyItemsListShareList><XPEDXMyItemsListShare >" +
					"<ComplexQuery Operation='OR'><Or>"+divDuery+"</Or></ComplexQuery></XPEDXMyItemsListShare >" +
							"</XPEDXMyItemsListShareList>" +
							"</XPEDXMyItemsList></XPEDXMyItemsItems>");

		}
		
		else{
			docItemsDocument=SCXmlUtils.createFromString("<XPEDXMyItemsItems MaximumRecords='2000' ItemId='"+currentItemId+"'>"+
					"<XPEDXMyItemsList><XPEDXMyItemsListShareList><XPEDXMyItemsListShare DivisionID='"+strDivId+"' /></XPEDXMyItemsListShareList>" +
					"</XPEDXMyItemsList></XPEDXMyItemsItems>");
		}
		
		return docItemsDocument;
	}

	private String getDivisionQuery(String[] div) {
		String itemQuery = "";
		for(int i=0;i<div.length;i++){
			itemQuery=itemQuery.concat("<Exp Name='DivisionID' QryType='EQ' Value='"+div[i]+"' />");
		}
		return itemQuery;
	}

}
