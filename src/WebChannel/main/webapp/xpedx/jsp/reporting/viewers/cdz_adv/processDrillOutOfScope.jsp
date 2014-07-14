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
<%
response.setDateHeader("expires", 0);
String strCancel = "";
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int iReportIndex = Integer.parseInt(iReport);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
Report objReport = doc.getReports().getItem(iReportIndex);
DrillInfo objDrillInfo = (DrillInfo) objReport.getNamedInterface("DrillInfo");
strCancel = requestWrapper.getQueryParameter("sCancel", false, "");
String[] objDimensionIDs = request.getParameterValues ("dimensions");
if (null != objDimensionIDs)
{
int ncIDs = objDimensionIDs.length;
if (ncIDs > 0)
{
DrillElements objDrillElements = null;
objDrillElements = objDrillInfo.extendScopeOfAnalysis();
for (int nIndex = 0; nIndex < ncIDs; nIndex++)
{
DrillElement objDrillElt = objDrillElements.add();
objDrillElt.setObjectID(objDimensionIDs [nIndex]);
}
}
}
String[] objFilterIDs = request.getParameterValues ("filtres_id");
if (null != objFilterIDs)
{
int ncFilterIDs = objFilterIDs.length;
if (ncFilterIDs > 0)
{
String[] objFilterVals  = request.getParameterValues ("filtres_val");
if (null != objFilterVals)
{
int ncFilterVals = objFilterVals.length;
int nIndex;
DrillElements objQFDrillElements = null;
objQFDrillElements = objDrillInfo.addQueryConditions();
for (nIndex = 0; nIndex < ncFilterIDs; nIndex++)
{
DrillFromElement objQFDrillFromElt = (DrillFromElement)objQFDrillElements.add();
objQFDrillFromElt.setObjectID(objFilterIDs[nIndex]);
objQFDrillFromElt.setFilter(objFilterVals[nIndex]);
}
}
}
}
String[] objNoRemFilterIDs  = request.getParameterValues ("rem_filtres_id_ck"); 
int ncNoRemFilterIDs = 0;
if (null != objNoRemFilterIDs) ncNoRemFilterIDs = objNoRemFilterIDs.length;
String[] objRemFilterIDs  = request.getParameterValues ("rem_filtres_id");
int ncRemFilterIDs = 0;
if (null != objRemFilterIDs) ncRemFilterIDs = objRemFilterIDs.length;
if (ncRemFilterIDs > 0)
{
if (ncNoRemFilterIDs < ncRemFilterIDs) 
{
String[] objRemFilterVals  = request.getParameterValues ("rem_filtres_val");
int nIndex;
DrillElements objQFDrillElements = null;
objQFDrillElements = objDrillInfo.removeQueryConditions();
for (nIndex = 0; nIndex < ncRemFilterIDs; nIndex++)
{
boolean isRemoveQueryCondition = true;
for (int i = 0; i < ncNoRemFilterIDs; i++)
{
if(objNoRemFilterIDs[i].equals(objRemFilterIDs[nIndex])) 
{
isRemoveQueryCondition = false;
break;
}
}
if(isRemoveQueryCondition) 
{
DrillFromElement objQFDrillElt = (DrillFromElement)objQFDrillElements.add();
objQFDrillElt.setObjectID(objRemFilterIDs[nIndex]); 
objQFDrillElt.setFilter(objRemFilterVals[nIndex]);  
}
else
isRemoveQueryCondition = true; 
}
}
}
objDrillInfo.executeDrill();
objDrillInfo.addQueryConditions().removeAll();
objDrillInfo.removeQueryConditions().removeAll();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
String strRelativeURL = "report.jsp";
if (doc.getMustFillContexts())
strRelativeURL = "getContextsInfos.jsp";
else if (doc.getMustFillPrompts())
strRelativeURL = "getPrompts.jsp";
session.setAttribute("CDZ." + strViewerID + ".DocInstance", doc);
out.clearBuffer();
%>
<jsp:forward page="<%=strRelativeURL%>"/>
<%
}
catch(Exception e)
{
if (!strCancel.equals(""))
{
out.println("<html><head><script language=\"javascript\">");
out.println("var previousURL=parent.allUseDictionary.get(\"" + strCancel + "\");");
out.println("function okCB() {parent.frameNav(\"Report\",previousURL);}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_DRILL", false, "okCB",out, session);
}
else
objUtils.displayErrorMsg(e, "_ERR_DRILL", true, out, session);
}
%>