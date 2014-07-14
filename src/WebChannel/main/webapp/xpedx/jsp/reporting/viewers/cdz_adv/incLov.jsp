<%@ include file="incStartpage.jsp" %>
<jsp:useBean id="objPromptsBean" class="com.businessobjects.adv_ivcdzview.Prompts" scope="page" />
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incLov.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("strEntry = " + strEntry );
String bid = requestWrapper.getQueryParameter("sBid", false, "");
String sOid = requestWrapper.getQueryParameter("sOid", false, "");
String sDPid = requestWrapper.getQueryParameter("sDPid", false, "");
String sLovType = requestWrapper.getQueryParameter("sLovType", false, "");
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "");
DataProvider dp = null;
if(!strDPIndex.equals(""))
{
dp=doc.getDataProviders().getItem(Integer.parseInt(strDPIndex));
}
boolean isUniverseLOV=false;
String objID = null;
if (!bid.equals("")) 
{
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
objID = cellInfo.m_expr.getID();
}
else if (!sOid.equals("")) 
{
isUniverseLOV =true;
objID = sOid; 
}
else if (!sDPid.equals("")) 
{
ReportDictionary rd = doc.getDictionary();
ReportExpression re = rd.getChild(sDPid);
_logger.info("re:" + re);
objID = re.getID();
}
LovType lovType = LovType.CUBE;
if (sLovType.equals("CUBE"))
lovType = LovType.CUBE;
if (sLovType.equals("DATA_SOURCE"))
lovType = LovType.DATA_SOURCE;
if (sLovType.equals("LOV_DRILL"))
lovType = LovType.LOV_DRILL;
if (sLovType.equals("LOV_OBJECT"))
lovType = LovType.LOV_OBJECT;
_logger.info("objID:" + objID + ", lovType:" + lovType);
Lov objLOV = doc.getLOV( objID, lovType, dp );
if ( objLOV == null )
{
_logger.error("No LOV associated with this object");
return;
}
out.println("strLovID ='"+objLOV.getID()+"';");
boolean blnDelegatedSearch = objLOV.isDelegated();
out.println("isLovDelegatedSearch=" + ((blnDelegatedSearch)?"true":"false") + ";");
out.println("arrNbBatchLovItems=new Array;");
out.println("arrBatchLovTexts=new Array;");
out.println("arrBatchLovSelValue=new Array;");
out.println("arrLovTitle=new Array();");
out.println("iLovSortType=new Array();");
out.println("iLovSortedColumnIndex=new Array();");
out.println("arrNbLovItems=new Array;");
out.println("arrSelectedValues=new Array;");
out.println("arrLovTexts=new Array;");
out.println("arrLovValues=new Array;");
out.println("arrLovIndexes=new Array;");
objPromptsBean.onStart(requestWrapper);
if ( (isUniverseLOV == true ) && ((objLOV.mustFillNestedPrompts()) || (objLOV.getNestedPrompts().getCount()>0)))
{
_logger.info("LOV has nested prompts");
out.println("bFillNestedPrompts =true;");
boolean blnAny_Level = (objLOV.getLovDisplayType() == LovDisplayType.HIERARCHICAL_ANY_LEVEL);
objPromptsBean.blnGroupPrompts = (blnAny_Level || objLOV.getLovDisplayType() == LovDisplayType.HIERARCHICAL);
objPromptsBean.gIndex++;
String strDocType = doc.getProperties().getProperty(PropertiesType.DOCUMENT_TYPE);
String strInputFormat=null;
if (strDocType.equalsIgnoreCase("WID"))
{
ReportEngine objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
if(objReportEngine!=null)
{
FormatNumber defaultFormat = doc.getDefaultFormatNumber(FormatNumberType.DATE_TIME);
if((defaultFormat!= null) && (defaultFormat instanceof DefaultDateTimeFormatNumber)) 
{
strInputFormat = ((DefaultDateTimeFormatNumber) defaultFormat).getDateFormatting().getPositive(); 
}
}
}
objPromptsBean.setDateFormat(strInputFormat);
objPromptsBean.setDocType(strDocType) ;
objPromptsBean.getNestedPrompts(objLOV, "1", 1, out, session);
out.println("bGroupedPrompts ="+objPromptsBean.blnGroupPrompts+";");
}
else
{
_logger.info("LOV found");
if (!blnDelegatedSearch)
{
String searchVal = requestWrapper.getQueryParameter("searchVal", false, "");
if (!searchVal.equals(""))
{
objLOV.setSearchMode(true);
objLOV.setSearchPattern(searchVal);
}
else
{
objLOV.setSearchMode(false);
}
boolean blnHasBatchList = objPromptsBean.getBatchLOV(1, objLOV, out);
boolean blnHasLOVList = objPromptsBean.getLovValues(1, objLOV, out);
out.println("isLovParialResult=" + ((objLOV.isPartialResult())?"true":"false") + ";");
}
}
_logger.info("<--incLov.jsp");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>