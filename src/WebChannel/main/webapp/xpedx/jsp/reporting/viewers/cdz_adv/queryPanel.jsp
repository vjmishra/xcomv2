<!--
=============================================================
WebIntelligence(r) Report Panel
Copyright(c) 2001-2005 Business Objects S.A.
All rights reserved

Use and support of this software is governed by the terms
and conditions of the software license agreement and support
policy of Business Objects S.A. and/or its subsidiaries. 
The Business Objects products and technology are protected
by the US patent number 5,555,403 and 6,247,008

=============================================================
--><%@ include file="wistartpage.jsp" %>
<jsp:useBean id="oDS" class="com.businessobjects.adv_ivcdzview.DataSourceTools" scope="page" />
<jsp:useBean id="objQueries" class="com.businessobjects.adv_ivcdzview.ApplyQueries" scope="page" />
<%
response.setDateHeader("expires", 0);
try
{
_logger.info("-->queryPanel.jsp");
String strEntry      = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
    String strUnvID = requestWrapper.getQueryParameter("sParam1", false, "");
    String[] arrUnvID = ViewerTools.convertStringToArray(strUnvID);
    String strDPID = requestWrapper.getQueryParameter("sParam2", false, "");
    String[] arrDPID = ViewerTools.convertStringToArray(strDPID);
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "0");
int iDPIndex = Integer.parseInt(strDPIndex);
String mustFillTabsDP = requestWrapper.getQueryParameter("mustFillTabsDP", false, "true");
String strDuplicateDPName = requestWrapper.getQueryParameter("sParam3", false, "");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
DataProviders dps = doc.getDataProviders();
DataProvider  dp= dps.getItem(iDPIndex);
com.businessobjects.rebean.wi.Query query = null;
boolean unvLoaded =false, dpLoaded =false, stopLoading=false;
String sViewerFrameName = "_p";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Title zone</title>
<style type="text/css">
#universeContainer{position:absolute;top:5px;left:5px;}
#queryContainer{position:absolute;top:5px;left:250px;}
#filterContainer{position:absolute;top:250px;left:250px;}
#fop{width:190px}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript" src="lib/treeview.js"></script>
<script language="javascript" src="lib/bolist.js"></script>
<script language="javascript" src="lib/menu.js"></script>
<script language="javascript" src="lib/palette.js"></script>
<script language="javascript" src="lib/calendar.js"></script>
<script language="javascript" src="scripts/Utils.js"></script>
<script language="javascript">initDom(parent._skin,parent._lang)</script>
<script language="javascript">styleSheet()</script>
<script language="javascript">
var _p=parent;
var unvList = _p._universes;
var unvHrcList = _p._universesHrc;
var dpList = _p._queries;
var unvIndex = 0, dpIndex = 0;
var stopLoadingDP=false;
var stopLoadingReason=null;
var errorMsg="";
var _currentDP=null;
var keydateInfo=null;
<%
try
{
if(mustFillTabsDP.equals("true"))
{
oDS.updateArrayOfDPInfo(out,dps);
out.println("if(_p.mustFillTabsDP) _p.writeTabsDP();");
}
int dpDType = oDS.getDisabledDPType(dp);
if(dpDType!=-1) 
{
stopLoading = true;
out.println("stopLoadingDP=true");
out.println("stopLoadingReason="+dpDType);
}
if(!stopLoading)
{
DataSource ds = dp.getDataSource();
for(int i=0;i<arrUnvID.length ;i++)
{
if(arrUnvID[i].equals("")) break;
if (arrUnvID[i].equals(ds.getID()))
{
unvLoaded=true;
out.println("unvIndex="+i);
out.println("unv = unvList[unvIndex];_currentUnv = unvList[unvIndex];");
out.println("unvHrc = unvHrcList[unvIndex];_currentUnvHrc = unvHrcList[unvIndex];");
break;
}
}
if (!unvLoaded)
{
out.println();
oDS.printDataSource( out, ds, sViewerFrameName );
out.println("unvList[unvList.length]=unv;_currentUnv = unv;");
out.println("unvHrcList[unvHrcList.length]=unvHrc;_currentUnvHrc = unvHrc;");
}
for(int i=0;i<arrDPID.length ;i++)
{
if (arrDPID[i].compareTo(dp.getName())==0)
{
dpLoaded=true;
out.println("dpIndex="+i); 
out.println("_currentDP= dpList[dpIndex];_currentDP.alreadyLoaded=true;");
break;
}
}
if (!dpLoaded)
{
if(!strDuplicateDPName.equals(""))
{
out.println("for(var i in dpList){");
out.println("if (dpList[i].name == \""+ViewerTools.escapeQuotes(strDuplicateDPName)+"\" ){");
out.println("dp=dpList[i].getCopy();");
out.println("dp.name=\""+ViewerTools.escapeQuotes(dp.getName())+"\";");
out.println("dp.isModified = true;");
out.println("break;}}");
}
else
{
out.println();
query = (Query)((TreeNode)(dp.getCombinedQueries()).getChildAt(0));
printQuery(out, query, true, dp);
objQueries.printContexts(out,doc,iDPIndex);
out.println("dp.context=arrContexts;");
}
out.println("dpList[dpList.length]=dp;_currentDP = dp;");
out.println("_currentDP.alreadyLoaded=false;//use for the warning CustomSQL");
}
Locale loc = ViewerTools.convStrToLocale(requestWrapper.getUserLocale());
        DataSourceTools.printKeydateInfo(out, dp, loc);        
    out.println("_currentDP.keydateValue=keydateInfo;");
out.println("_p.usrPromptKeepValues=\"" + ViewerTools.escapeQuotes(objUtils.objConfig.getProperty("PROMPT_KEEP_LAST_VALUES")) + "\";");
}
}
catch(Exception e) 
{
stopLoading=true;
out.println("stopLoadingDP=true");
out.println("errorMsg='"+ViewerTools.escapeQuotes(e.getLocalizedMessage())+"';");
}
%>
</script>
<script language="javascript" src="scripts/CommonWom.js"></script>
<script language="javascript" src="language/<%=(String)session.getAttribute("CDZ.Language")%>/scripts/filterzone.js"></script>
<script language="javascript" src="language/<%=(String)session.getAttribute("CDZ.Language")%>/scripts/customTooltip.js"></script>
<script language="javascript" src="language/<%=(String)session.getAttribute("CDZ.Language")%>/scripts/querypanel.js"></script>
</head>
<script language="javascript"> 
if(stopLoadingDP) 
writeEmptyBody(errorMsg?errorMsg:"",stopLoadingReason)
else
writeBody() 
</script>
<script language="javascript"> stopLoadingDP?setTimeout("loadEmptyDPCB()",1):setTimeout("loadCB()",1)</script>
</html>
<%
_logger.info("<--queryPanel.jsp");
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_EDIT_QUERY", true, out, session);
}
%>
<%! 
void printQuery(JspWriter out, com.businessobjects.rebean.wi.Query query, boolean isEditing, DataProvider  dp)
throws Exception
{
out.println("with(_p){");
try
    {
    out.println("dp=_p.newBOProvider(\""+ViewerTools.escapeQuotes(dp.getName())+"\");");
    if (query == null) return;
    if (isEditing)
    {
    DataSource ds = dp.getDataSource();
    out.println("dp.unvID=\""+ViewerTools.escapeQuotes(ds.getID())+"\";");
    Scope  scope = query.getScope();
    ScopeLevel level = scope.getScopeLevel();
    int ScopeWom = 0;
    if (level == ScopeLevel.LEVEL_1) ScopeWom = 1;
    else if (level == ScopeLevel.LEVEL_2) ScopeWom = 2;
    else if (level == ScopeLevel.LEVEL_3) ScopeWom = 3;
    else ScopeWom = 0;
    out.println("dp.scope="+ScopeWom+";");
    int maxRows = query.getMaxRowsRetrieved();
    int maxTime = query.getMaxRetrievalTime();
    if (maxRows < 0)
    {
    out.println("dp.haveMaxRows=false;");
    maxRows = ds.getMaxRowsRetrieved();
    if (maxRows >= 0)
    out.println("dp.maxRows="+maxRows+";");
    }
    else
    {
    out.println("dp.haveMaxRows=true;");
    out.println("dp.maxRows="+maxRows+";");
    }
    if (maxTime < 0)
    {
    out.println("dp.haveMaxTime=false;");
    maxTime = ds.getMaxRetrievalTime();
    if (maxTime >= 0)
    out.println("dp.maxTime="+maxTime+";");
    }
    else
    {
    out.println("dp.haveMaxTime=true;");
    out.println("dp.maxTime="+maxTime+";");
    }
    if(dp instanceof SQLDataProvider )
    {
    out.println("dp.isCustomSQL=" + (((SQLDataProvider)dp).isCustomSQL() ? true : false) + ";");
    out.println("dp.duplicateRows=" + (((SQLDataProvider)dp).getDuplicatedRows() ? "true" : "false") + ";");
    SamplingMode mode = ((SQLDataProvider)dp).getSamplingMode();
    String sMode="none";
    if(mode == SamplingMode.FREE_RANDOM)
    sMode="free";
    else if (mode == SamplingMode.REPEATABLE_RANDOM)
    sMode="seeded";
    out.println("dp.samplingMode='" + sMode + "';");
    out.println("dp.samplingSize=" + (((SQLDataProvider)dp).getSamplingSize()) + ";");
    }
    out.println("dp.refreshContexts=" + (query.resetContexts() ? "true" : "false") + ";");
    if(!ds.isViewSQLSupported())
    out.print("dp.allowViewSQL=false;");
    if(!ds.isDuplicateRowSupported())
    out.print("dp.allowDuplicateRows=false;");
    if(!ds.isIsNullOperatorSupported())
    out.print("dp.allowIsNull=false;");
    if(!ds.isNotIsNullOperatorSupported())
    out.print("dp.allowNotIsNull=false;");
    if(!ds.isBothOperatorSupported())
    out.print("dp.allowBoth=false;");
    if(!ds.isExceptOperatorSupported())
    out.print("dp.allowExcept=false;");
    out.print("dp.allowSampling="+dp.isSupported(DataProviderFeature.SAMPLING_FREE_RANDOM)+";");
    out.print("dp.allowSeededSampling="+dp.isSupported(DataProviderFeature.SAMPLING_REPEATABLE_RANDOM)+";");
    int length = query.getResultObjectCount();
    for (int i=0; i < length; i++)
    {
    DataSourceObject obj = query.getResultObject(i);
    if (obj != null)
    out.println("dp.addObj(unv.getById('"+ViewerTools.escapeQuotes(obj.getID())+"'));");
    }
    if (level == ScopeLevel.CUSTOM)
    {
    int count = scope.getScopeObjectCount();
    for(int i=0; i<count; i++)
    {
    DataSourceObject obj =scope.getScopeObject(i);
    if (obj != null)
    out.println("dp.addObj(unv.getById('"+ViewerTools.escapeQuotes(obj.getID())+"'));");
    }
    }
    out.println("");
            String scriptFilterName = "flt";
            ConditionContainer conditions = query.getCondition();
            QueryFilterMaker filt = new QueryFilterMaker( scriptFilterName, conditions, true); 
            out.println(filt.toString());
            out.println("dp.filtersTree=" + scriptFilterName + "0");
            PromptOrder pOrder = query.getPromptOrder();
    int nbPrompt = pOrder.getCount();
    String question="";
    ConditionPrompt prompt = null;
    for(int j=0; j<nbPrompt;j++)
    {
    prompt = pOrder.getItem(j);
    if (prompt!=null)
    {
    question= prompt.getQuestion();
    out.println("dp.addPrompt('" + ViewerTools.escapeQuotes(question) + "');");
    }
    }
    }
}
catch(Exception e)
{
    out.println("}");
    throw(e);  
}
out.println("}");
}
%>