<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page import="com.businessobjects.adv_ivcdzview.*,
 com.crystaldecisions.sdk.framework.IEnterpriseSession,
 com.crystaldecisions.sdk.occa.managedreports.*,
 com.crystaldecisions.sdk.occa.report.application.*,
 com.crystaldecisions.sdk.occa.report.data.*,
 java.util.Locale" %>
<jsp:useBean id="requestWrapper" class="com.businessobjects.adv_ivcdzview.RequestWrapper" scope="page">
<%
requestWrapper.onStart(request);
requestWrapper.setCharacterEncoding("UTF-8");
%>
</jsp:useBean>
<%
String strSession = (String)session.getAttribute(ViewerTools.SessionEntSessionName);
IEnterpriseSession entSession = (IEnterpriseSession) session.getAttribute(strSession);
DHTMLLogger _logger = Utils.getLogger("com.businessobjects.dhtml." + requestWrapper.getCurrentPageName());
_logger.info("--> incCrystalParams.jsp");
response.setDateHeader("expires", 0);
String strID = requestWrapper.getQueryParameter("id", true);
String strParent = requestWrapper.getQueryParameter("sParent", false, "");
Locale viewerLocale = new Locale(requestWrapper.getUserLocale());
IReportAppFactory reportAppFactory = (IReportAppFactory) entSession.getService("RASReportFactory");
ReportClientDocument clientDoc = reportAppFactory.openDocument(Integer.parseInt(strID),0,viewerLocale);
DataDefController dataCtrler = clientDoc.getDataDefController();
IDataDefinition iDataDef = dataCtrler.getDataDefinition();
Fields parameters = iDataDef.getParameterFields();
int nbParams = parameters.size();
_logger.info("document has " + Integer.toString(nbParams) + " parameters.");
for (int i=0; i<nbParams; i++)
{
IParameterField parameter = (IParameterField)parameters.getField(i);
String strIndex = Integer.toString(i);
out.println(strParent + "doc.arrPrompts[" + strIndex + "]=new " + strParent + "newPromptInfo();");
out.println(strParent + "doc.arrPrompts[" + strIndex + "].name=\"" + ViewerTools.escapeQuotes(parameter.getName()) + "\";");
out.println(strParent + "doc.arrPrompts[" + strIndex + "].id=\"" + ViewerTools.escapeQuotes(parameter.getName()) + "\";");
if (parameter.getValueRangeKind().value() == 0)
out.println(strParent + "doc.arrPrompts[" + strIndex + "].type='R';");
else if (parameter.getAllowMultiValue())
out.println(strParent + "doc.arrPrompts[" + strIndex + "].type='M';");
else
out.println(strParent + "doc.arrPrompts[" + strIndex + "].type='S';");
out.println(strParent + "doc.arrPrompts[" + strIndex + "].isOptional=false;");
out.println(strParent + "doc.arrPrompts[" + strIndex + "].isSelected=false;");
}
viewerLocale = null;
parameters = null;
iDataDef = null;
dataCtrler = null;
clientDoc = null;
reportAppFactory = null;
_logger.info("<-- incCrystalParams.jsp");
%>
