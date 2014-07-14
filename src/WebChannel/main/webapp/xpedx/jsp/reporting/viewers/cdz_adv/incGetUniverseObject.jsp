<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incGetUniverseObjects.jsp");
String univId = requestWrapper.getQueryParameter("unvid", true);
String objId = requestWrapper.getQueryParameter("sNewVarIds", true);
ReportEngine engine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
engine.setLocale(requestWrapper.getUserLocale());
    DocumentInstance doc = engine.newDocument(univId); 
DataProviders dps = doc.getDataProviders();
DataProvider dp = dps.getItem(0);
DataSource ds = dp.getDataSource();
DataSourceObjects dsos = ds.getClasses();
DataSourceObject dso = dsos.getChild(objId);
String desc = dso.getDescription();
if (desc == null)
desc = "";
String longUnivame = ViewerTools.escapeQuotes(dso.getDataSource().getLongName());
out.println("_currentUnv=newBOUnv('" + longUnivame + "','" + univId + "');");
out.println("cls0=_currentUnv.root;");
out.println("_obj = cls0.add('" + ViewerTools.escapeQuotes(dso.getName()) + "','" + 
ViewerTools.escapeQuotes(dso.getID()) + "',"+ ViewerTools.getWomType(dso.getQualification()) + ","+ 
ViewerTools.getWomDataType(dso.getType()) + ",'"+ ViewerTools.escapeQuotes(desc) + "'," +
(dso.hasLOV() ? "true" : "false") + "," + "null" + "," +
"'','','','" + longUnivame + "',false);");
    String strEntry = doc.getStorageToken();
    _logger.info("strEntry = " + strEntry );
out.println("strEntry ='"+strEntry+"';");
FormatNumber localeFormat = null;
FormatNumber defaultFormat = doc.getDefaultFormatNumber(FormatNumberType.DATE_TIME);
if((defaultFormat!= null) && (defaultFormat instanceof DefaultDateTimeFormatNumber)) 
localeFormat = ((DefaultDateTimeFormatNumber) defaultFormat).getDateFormatting();   
if (localeFormat != null) {
out.println("_inputFormatDate=\""+ViewerTools.escapeQuotes(localeFormat.getPositive())+"\";");
} else {
out.println("_inputFormatDate= \"M/d/yyyy\";");
}
out.println("_topfs._inputFormatDate=_inputFormatDate");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>
