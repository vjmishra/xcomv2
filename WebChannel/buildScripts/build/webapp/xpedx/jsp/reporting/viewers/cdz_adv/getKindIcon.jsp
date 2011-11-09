<%@ page language="java" import="java.io.*,com.businessobjects.rebean.wi.*,com.businessobjects.adv_ivcdzview.*,java.util.*,com.crystaldecisions.sdk.framework.IEnterpriseSession,com.crystaldecisions.sdk.occa.pluginmgr.IPluginInfo"%><jsp:useBean id="requestWrapper" class="com.businessobjects.adv_ivcdzview.RequestWrapper" scope="page" /><%
requestWrapper.onStart(request);
DHTMLLogger _logger = Utils.getLogger("com.businessobjects.dhtml." + requestWrapper.getCurrentPageName());
try
{
response.reset();
requestWrapper.setCharacterEncoding("UTF-8");
boolean isAlive = (session.getAttribute(ViewerTools.SessionAlive) == null)?false:true;
if (!isAlive) return;
String strSession = (String)session.getAttribute(ViewerTools.SessionEntSessionName);
if (strSession == null)
{
strSession = "CE_ENTERPRISESESSION";
session.setAttribute(ViewerTools.SessionEntSessionName, strSession);
}
IEnterpriseSession entSession = (IEnterpriseSession) session.getAttribute(strSession);
String progID = requestWrapper.getQueryParameter("progID");
if (progID == null)
throw new Exception("Internal Error: Missing progID parameter.");
IPluginInfo info = entSession.getPluginManager().getPluginInfo(progID);
InputStream stream = info.getPicture();
if (stream == null) {
_logger.error("IPluginInfo=" + info);
_logger.error("stream == null");
        return;
}
ServletOutputStream os = response.getOutputStream();
byte[] buffer = new byte[512];
while (-1 != stream.read(buffer)) {
    os.write(buffer);
}
os.close();
}
catch(Exception e)
{
_logger.error(e.getMessage());
}
%>
