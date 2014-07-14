/**
 * 
 */
package com.xpedx.sterling.rcp.pca.util;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;
import com.yantra.yfc.rcp.internal.YRCApiCaller;


/**
 * @author Administrator
 * @param <YCDCacheCallbackHandler>
 *
 */
public class XPXCacheManager {
	
	private HashMap xpxCache = new HashMap();
	public static HashMap xpxRuleIDCache = new HashMap();
	private static final String UOM_LIST_PREFIX = "UOM_LIST_";
	private static final String DIV_LIST_PREFIX = "DIV_LIST_"; // JIRA 3622 - Performance Fix - Division Cache
	
	private static final String LINE_TYPE_LIST_PREFIX = "LINE_TYPE_LIST_";
	private static final String ORDER_CHARGES_LIST_PREFIX = "ORDER_CHARGES_LIST_";
	private static XPXCacheManager cacheManager;
	private static final String CACHE_FORM_ID = "com.xpedx.sterling.rcp.pca.cache.XPXCache";
	
	private XPXCacheManager()
	{
		
	}
	
	public static XPXCacheManager getInstance()
    {
        if(cacheManager == null)
        	cacheManager = new XPXCacheManager();
        return cacheManager;
    }
 
	public void getUomList(String organizationCode, YRCBehavior inBehavior)
	{
		String key = UOM_LIST_PREFIX+organizationCode;
		Document inXML = YRCXmlUtils.createFromString("<ItemUOMMaster CallingOrganizationCode='"+organizationCode+"'/>");
		YRCApiContext ctx = new YRCApiContext();
		ctx.setApiName("XPXGetItemUOMMasterList");
        ctx.setInputXml(inXML);
        ctx.setFormId(CACHE_FORM_ID);
		if(isObjectInCache(key))
		{
				ctx.setInvokeAPIStatus(1);
	            try
	            {
	                ctx.setOutputXml((Document)getObjectFromCache(key));
	            }
	            catch(ClassCastException e)
	            {
	                Object obj = (Document)getObjectFromCache(key);
	                StringBuffer strBuff = new StringBuffer("The Cached Object is a: ");
	                strBuff.append(obj.getClass().getName());
	                strBuff.append(".  With a value of: ");
	                strBuff.append(obj.toString());
	                YRCPlatformUI.trace(strBuff.toString());
	                throw e;
	            }
		}
		else {
			YRCApiCaller syncapiCaller= new YRCApiCaller(ctx,true);
			syncapiCaller.invokeApi();
			//get the customer details
			Document outputXml = ctx.getOutputXml();
			addUOMList(outputXml, organizationCode);
		}
		inBehavior.handleApiCompletion(ctx);
	}
// JIRA 3622 - Performance Fix - Division Cache Starts
	public void getDivisionList(String dataSecurityGroupID, YRCBehavior inBehavior)
	{
		String key = DIV_LIST_PREFIX+dataSecurityGroupID;
		Element eleInput = YRCXmlUtils.createDocument("Team").getDocumentElement();
		eleInput.setAttribute("RequestedByUsersTeamId", dataSecurityGroupID);
		String[] apinames = {"XPXGetDivisionsManagedByTeam"};
		Document[] docInput = {(eleInput.getOwnerDocument())};				
		YRCApiContext ctx = new YRCApiContext();
		ctx.setApiNames(apinames);
		ctx.setInputXmls(docInput);
        ctx.setFormId(CACHE_FORM_ID);
		if(isObjectInCache(key))
		{
				ctx.setInvokeAPIStatus(1);
	            try
	            {
	                ctx.setOutputXml((Document)getObjectFromCache(key));
	            }
	            catch(ClassCastException e)
	            {
	                Object obj = (Document)getObjectFromCache(key);
	                StringBuffer strBuff = new StringBuffer("The Cached Object is a: ");
	                strBuff.append(obj.getClass().getName());
	                strBuff.append(".  With a value of: ");
	                strBuff.append(obj.toString());
	                YRCPlatformUI.trace(strBuff.toString());
	                throw e;
	            }
		}
		else {
			YRCApiCaller syncapiCaller= new YRCApiCaller(ctx,true);
			syncapiCaller.invokeApi();
			//get the customer details
			Document outputXml = ctx.getOutputXml();
			addDivList(outputXml, dataSecurityGroupID);
		}
		inBehavior.handleApiCompletion(ctx);
	}
// JIRA 3622 - Performance Fix - Division Cache Ends
	
	public void getRuleIDErrorCode(YRCBehavior inBehavior)
	{
		Element eleInput = YRCXmlUtils.createDocument("CommonCode").getDocumentElement();
		eleInput.setAttribute("CodeType", "XPXRuleID");
		eleInput.setAttribute("OrganizationCode", "xpedx");
		String[] apinames = {"getCommonCodeList"};
		Document[] docInput = {(eleInput.getOwnerDocument())};				
		YRCApiContext ctx = new YRCApiContext();
		ctx.setApiNames(apinames);
		ctx.setInputXmls(docInput);
        ctx.setFormId(CACHE_FORM_ID);
		YRCApiCaller syncapiCaller= new YRCApiCaller(ctx,true);
		syncapiCaller.invokeApi();
		Document outputXml = ctx.getOutputXml();
		addRuleIDDescription(outputXml);
		inBehavior.handleApiCompletion(ctx);
	}
	public void getLineTypeList(String organizationCode, YRCBehavior inBehavior)
	{
		String key = LINE_TYPE_LIST_PREFIX+organizationCode;
		Document inXML = YRCXmlUtils.createFromString("<CommonCode CallingOrganizationCode='"+organizationCode+"' CodeType='LineType' />");
		YRCApiContext ctx = new YRCApiContext();
		ctx.setApiName("XPXGetLineTypeList");
        ctx.setInputXml(inXML);
        ctx.setFormId(CACHE_FORM_ID);
		if(isObjectInCache(key))
		{
				ctx.setInvokeAPIStatus(1);
	            try
	            {
	                ctx.setOutputXml((Document)getObjectFromCache(key));
	            }
	            catch(ClassCastException e)
	            {
	                Object obj = (Document)getObjectFromCache(key);
	                StringBuffer strBuff = new StringBuffer("The Cached Object is a: ");
	                strBuff.append(obj.getClass().getName());
	                strBuff.append(".  With a value of: ");
	                strBuff.append(obj.toString());
	                YRCPlatformUI.trace(strBuff.toString());
	                throw e;
	            }
		}
		else {
			YRCApiCaller syncapiCaller= new YRCApiCaller(ctx,true);
			syncapiCaller.invokeApi();
			//get the customer details
			Document outputXml = ctx.getOutputXml();
			addLineTypeList(outputXml, organizationCode);
		}
		inBehavior.handleApiCompletion(ctx);
	}
	
	public void getOrderChargesList(String organizationCode, YRCBehavior inBehavior)
	{
		String key = ORDER_CHARGES_LIST_PREFIX+organizationCode;
		Document inXML = YRCXmlUtils.createFromString("<CommonCode CallingOrganizationCode='"+organizationCode+"' CodeType='XPXChargeType' />");
		YRCApiContext ctx = new YRCApiContext();
		ctx.setApiName("XPXGetOrderChargesList");
        ctx.setInputXml(inXML);
        ctx.setFormId(CACHE_FORM_ID);
		if(isObjectInCache(key))
		{
				ctx.setInvokeAPIStatus(1);
	            try
	            {
	                ctx.setOutputXml((Document)getObjectFromCache(key));
	            }
	            catch(ClassCastException e)
	            {
	                Object obj = (Document)getObjectFromCache(key);
	                StringBuffer strBuff = new StringBuffer("The Cached Object is a: ");
	                strBuff.append(obj.getClass().getName());
	                strBuff.append(".  With a value of: ");
	                strBuff.append(obj.toString());
	                YRCPlatformUI.trace(strBuff.toString());
	                throw e;
	            }
		}
		else {
			YRCApiCaller syncapiCaller= new YRCApiCaller(ctx,true);
			syncapiCaller.invokeApi();
			//get the customer details
			Document outputXml = ctx.getOutputXml();
			addOrderChargesList(outputXml, organizationCode);
		}
		inBehavior.handleApiCompletion(ctx);
	}
	
	private boolean isObjectInCache(String key){
		if(YRCPlatformUI.isVoid(key))
			return false;
		if(xpxCache!=null && xpxCache.containsKey(key)){
			if(xpxCache.get(key)!=null)
				return true;
		}
		return false;
		
	}
	
	public Object getObjectFromCache(String key)
	{
		if(xpxCache!=null && xpxCache.containsKey(key)){
			return xpxCache.get(key);
		}
		return null;

	}
	
	public void setObjectInCache(String key, Object value)
	{
		xpxCache.put(key, value);
	}
	
	public void addUOMList(Document doc, String orgCode)
	{
		String key = UOM_LIST_PREFIX+orgCode;
		setObjectInCache(key, doc);
		
	}
	// JIRA 3622 - Performance Fix - Division Cache Starts
	public void addDivList(Document doc, String dataSecurityGroupID)
	{
		
		String key = DIV_LIST_PREFIX+dataSecurityGroupID;
		setObjectInCache(key, doc);
		
	}
	// JIRA 3622 - Performance Fix - Division Cache Ends
	public void addRuleIDDescription(Document commonCodeXML){
		Element eleCommonCodeList = commonCodeXML.getDocumentElement();
		NodeList eleCommonCode = eleCommonCodeList.getElementsByTagName("CommonCode");
		int listLength = eleCommonCode.getLength();
		for (int i=0;i<listLength; i++){
			Element commonCode = (Element)eleCommonCode.item(i);
			String RuleId = commonCode.getAttribute("CodeValue");
			String shortDescription = commonCode.getAttribute("CodeShortDescription");
			setRuleIDInCache(RuleId,shortDescription);
		}
		
	}
	public void addLineTypeList(Document doc, String orgCode)
	{
		String key = LINE_TYPE_LIST_PREFIX+orgCode;
		setObjectInCache(key, doc);
		
	}
	
	public void setRuleIDInCache(String key, String value)
	{
		xpxRuleIDCache.put(key, value);
	}
	public static String getsetRuleIDDescription(String key){
		String description = null;
		if(xpxRuleIDCache.containsKey(key))
		description = (String)xpxRuleIDCache.get(key);
		return description; 
		
	}
	public void addOrderChargesList(Document doc, String orgCode)
	{
		String key = ORDER_CHARGES_LIST_PREFIX+orgCode;
		setObjectInCache(key, doc);
		
	}
	

}
