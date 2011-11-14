<%@ page language="java" contentType="text/html;charset=UTF-8" errorPage="errorPage.jsp"%>
<%@ page import="com.businessobjects.rebean.wi.Prompts,
 com.businessobjects.rebean.wi.*,
 com.businessobjects.adv_ivcdzview.*,
 java.awt.Color,
 java.io.*,
 java.util.*,
 javax.xml.parsers.*,
 com.crystaldecisions.sdk.exception.SDKException,
 com.crystaldecisions.sdk.framework.IEnterpriseSession,
 com.crystaldecisions.sdk.occa.infostore.IInfoStore,
 com.crystaldecisions.sdk.occa.infostore.IInfoObjects,
 com.crystaldecisions.sdk.occa.infostore.IInfoObject,
 com.crystaldecisions.sdk.occa.security.*"%>
<jsp:useBean id="objUtils" class="com.businessobjects.adv_ivcdzview.Utils" scope="application" />
<jsp:useBean id="objUserSettings" class="com.businessobjects.adv_ivcdzview.UserSettings" scope="session" />
<jsp:useBean id="requestWrapper" class="com.businessobjects.adv_ivcdzview.RequestWrapper" scope="page">
<%
requestWrapper.onStart(request);
requestWrapper.setCharacterEncoding("UTF-8");
%>
</jsp:useBean>
<%
DHTMLLogger _logger = Utils.getLogger("com.businessobjects.dhtml." + requestWrapper.getCurrentPageName());
boolean isAlive = (session.getAttribute(ViewerTools.SessionAlive) == null)?false:true;
IEnterpriseSession entSession = null;
ReportEngines reportEngines = null;
if (isAlive)
{
String strSession = requestWrapper.getQueryParameter("entSession");
if (strSession == null || strSession.equals(""))
{
strSession = (String)session.getAttribute(ViewerTools.SessionEntSessionName);
if (strSession == null)
{
strSession = "CE_ENTERPRISESESSION";
session.setAttribute(ViewerTools.SessionEntSessionName, strSession);
}
}
else
session.setAttribute(ViewerTools.SessionEntSessionName, strSession);
entSession = (IEnterpriseSession) session.getAttribute(strSession);
if (entSession != null)
{
reportEngines = objUtils.getReportEngines(request, entSession);
if (reportEngines == null)
{
_logger.error("reportEngines object is null");
throw new Exception("Internal error: cannot get ReportEngines");
}
}
else
{
_logger.info("Invalid session: enterprise session is null.");
isAlive = false;
}
}
InstanceManager instanceManager = (InstanceManager)session.getAttribute(ViewerTools.SessionInstanceManager);
if (instanceManager == null)
{
instanceManager = new InstanceManager();
session.setAttribute(ViewerTools.SessionInstanceManager, instanceManager);
}
String strLanguage = requestWrapper.getQueryParameter("lang", false, "");
if (!strLanguage.equals(""))
{
if (!objUtils.isInstalledLanguage(strLanguage))
{
_logger.warn("language " + strLanguage + " is not installed. Fallback to english");
strLanguage = "en";
}
if (strLanguage.equals("zh_SG"))
strLanguage = "zh_CN";
else if (strLanguage.equals("zh_HK"))
strLanguage = "zh_TW";
else if (strLanguage.equals("zh_MO"))
strLanguage = "zh_TW";
session.setAttribute(ViewerTools.SessionLanguage, strLanguage);
}
else
{
strLanguage = (String)session.getAttribute(ViewerTools.SessionLanguage);
if (strLanguage == null) strLanguage = "";
}
if (!isAlive)
{
_logger.info("Invalid session: SessionAlive variable is not set");
out.println("_isSessionInvalid=true");
}
%>
