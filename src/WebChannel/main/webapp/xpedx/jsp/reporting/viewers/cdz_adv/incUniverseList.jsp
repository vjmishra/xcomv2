<%@ include file="incStartpage.jsp" %>
<%@ page import="com.businessobjects.sdk.plugin.desktop.universe.CeUniverseRightID,
com.crystaldecisions.sdk.occa.pluginmgr.IPluginInfo,
com.crystaldecisions.sdk.occa.pluginmgr.IPluginMgr,
com.crystaldecisions.sdk.occa.security.internal.SecurityIDs,
com.crystaldecisions.sdk.plugin.CeKind"%>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
IInfoStore infoStore = (IInfoStore)session.getAttribute("cdzInfoStore");
if(infoStore == null)
{
infoStore = (IInfoStore)entSession.getService("InfoStore");
session.setAttribute("cdzInfoStore", infoStore);
}
IInfoObjects objs = infoStore.query("SELECT SI_ID FROM CI_APPOBJECTS WHERE SI_PARENTID=95 AND SI_NAME='Universes'");
IInfoObject unvFolder = (IInfoObject)objs.get(0);
int userId = entSession.getUserInfo().getUserID();
int rightId = CeUniverseRightID.SEC_ID_Universe_CreateOrEditQuery;
IPluginMgr pluginMgr = infoStore.getPluginMgr();
IPluginInfo pluginInfo = pluginMgr.getPluginInfo(CeKind.UNIVERSE);
short universeType = pluginInfo.getType();
int realRightID = rightId;
if (universeType > 0) {
realRightID = SecurityIDs.getAuthenticRightID(rightId, universeType);
}
IInfoObjects universes = infoStore.query("SELECT SI_ID, SI_NAME, SI_SHORTNAME, SI_CUID , SI_DESCRIPTION FROM CI_APPOBJECTS WHERE SI_ANCESTOR=" + unvFolder.getID() + "AND IsAllowed( "+ userId +","+ realRightID +" ) AND SI_KIND='Universe' ORDER BY SI_NAME");
for (int i = 0; i < universes.size(); i++)
{
com.businessobjects.sdk.plugin.desktop.universe.IUniverse unv = (com.businessobjects.sdk.plugin.desktop.universe.IUniverse) universes.get(i);
String strUnvConnectionString = unv.buildUniverseIdString();
out.println("newBOUnvItem('"+ViewerTools.escapeQuotes(unv.getTitle())+"','" + ViewerTools.escapeQuotes(strUnvConnectionString)+"', '','"+ViewerTools.escapeQuotes(unv.getDescription())+"');");
}
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>