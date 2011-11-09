<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incUserPref.jsp");
IUserInfo userInfo = entSession.getUserInfo();
String snapToGrid = userInfo.getProfileString("CDZ_VIEW_snapToGrid");
String showGrid = userInfo.getProfileString("CDZ_VIEW_showGrid");
String gridSpacing = userInfo.getProfileString("CDZ_VIEW_gridSpacing");
String showDesc = userInfo.getProfileString("CDZ_VIEW_showDesc");
String dispMenuHeaders = userInfo.getProfileString("CDZ_VIEW_dispMenuHeaders");
String ctxMenu = userInfo.getProfileString("CDZ_VIEW_ctxMenu");
String appFmt = userInfo.getProfileString("CDZ_VIEW_appFmt");
String addTableHeader = userInfo.getProfileString("CDZ_VIEW_addTableHeader");
String sortPriority = userInfo.getProfileString("CDZ_VIEW_sortPriority");
String drillCustomFmt = userInfo.getProfileString("CDZ_VIEW_drillCustomFmt");
String drillUnderlined = userInfo.getProfileString("CDZ_VIEW_drillUnderlined");
String drillBgColor = userInfo.getProfileString("CDZ_VIEW_drillBgColor");
String drillFgColor = userInfo.getProfileString("CDZ_VIEW_drillFgColor");
String defaultQueryFilter = userInfo.getProfileString("CDZ_VIEW_defaultQueryFilter");
String tmp = "var frmkP = new frmkPrefs(";
_logger.info("\t snapTogrid = " + snapToGrid );
if ( snapToGrid.equals("") )
tmp+="parent._snapToGrid";
else
tmp+=snapToGrid;
tmp += ",";
_logger.info("\t showGrid = " + showGrid );
if ( showGrid.equals("") )
tmp+="parent._showGrid";
else
tmp+=showGrid;
tmp += ",";
_logger.info("\t gridSpacing = " + gridSpacing );
if ( gridSpacing.equals("") )
tmp+="parent._gridSpacing";
else
tmp+=gridSpacing;
tmp += ",";
_logger.info("\t showDesc = " + showDesc );
if ( showDesc.equals("") )
tmp+="parent._showDesc";
else
tmp+=showDesc;
tmp += ",";
_logger.info("\t prefDispMenuHeaders = " + dispMenuHeaders );
if ( dispMenuHeaders.equals("") )
tmp+="parent._dispMenuHeaders";
else
tmp+=dispMenuHeaders;
tmp += ",";
_logger.info("\t ctxMenu = " + ctxMenu );
if ( ctxMenu.equals("") )
tmp+="parent._ctxMenu";
else
tmp+="\"" + ctxMenu + "\"";
tmp += ");";
out.println(tmp);
tmp = "var repP = new repRulesPrefs(";
_logger.info("\t appFmt = " + appFmt );
if ( appFmt.equals("") )
tmp+="parent._appFmt";
else
tmp+=appFmt;
tmp += ",";
_logger.info("\t addTableHeader = " + addTableHeader );
if ( addTableHeader.equals("") )
tmp+="parent._addTableHeader";
else
tmp+=addTableHeader;
tmp += ",";
_logger.info("\t sortPriority = " + sortPriority );
if ( sortPriority.equals("") )
tmp+="parent._sortPriority";
else
tmp+="\"" + sortPriority + "\"";
tmp += ",";
_logger.info("\t custFmt = " + drillCustomFmt );
if ( drillCustomFmt.equals("") )
tmp+="parent._drillCustomFmt";
else
tmp+=drillCustomFmt;
tmp += ",";
_logger.info("\t underlined = " + drillUnderlined );
if ( drillUnderlined.equals("") )
tmp+="parent._drillUnderlined";
else
tmp+=drillUnderlined;
tmp += ",";
_logger.info("\t bgColor = " + drillBgColor );
if ( drillBgColor.equals("") )
tmp+="parent._drillBgColor";
else
tmp+="\"" + drillBgColor + "\"";
tmp += ",";
_logger.info("\t fgColor = " + drillFgColor );
if ( drillFgColor.equals("") )
tmp+="parent._drillFgColor";
else
tmp+="\"" + drillFgColor + "\"";
tmp += ");";
out.println(tmp);
tmp = "var queryP = new queryRulesPrefs(";
_logger.info("\t queryFilter = " + defaultQueryFilter );
if ( defaultQueryFilter.equals("") )
tmp+="parent._defaultQueryFilter";
else
tmp+="\"" + defaultQueryFilter + "\"";
tmp += ");";
out.println(tmp);
_logger.info("<--incUserPref.jsp");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>