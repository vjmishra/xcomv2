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
try
{
_logger.info("-->processPurge.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "");
    DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
    DataProviders dps = doc.getDataProviders();    
    if(strDPIndex.equals("")) 
    {
_logger.info(" purge all ");
dps.purgeAll();
    }
    else
    {
int iDPIndex = Integer.parseInt(strDPIndex);
_logger.info(" purge dp index = "+iDPIndex);
DataProvider dp  = dps.getItem(iDPIndex);
if (dp!=null) dp.purge();
    }      
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
%>
<jsp:forward page="report.jsp"/>
<%
_logger.info("<--processPurge.jsp");
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_DEFAULT",true, out, session);
}
%>
<%!
void updateArrayOfDPName(JspWriter out, DataProviders dps)
throws IOException
{
int count  = dps.getCount();
String dpName="";
int dpRows=0;
boolean editable = true;
DataProvider dp=null; 
out.println("with(par) {");
out.println("arrDPs.length=0");
for(int i=0;i<count;i++)
{
dp = dps.getItem(i);
dpName = dp.getName();
dpRows = dp.getRowCount();
try
{
editable = !dp.hasCombinedQueries();
}
catch(Exception e)
{
editable=false;
}
out.println("arrDPs["+i+"]=newDPInfo(\""+ViewerTools.escapeQuotes(dpName)+"\","+editable+","+dpRows+")");
}
out.println("}");
}
void updateStorageToken(JspWriter out,DocumentInstance doc)
throws IOException
{
String sEntry = doc.getStorageToken();
out.println("par.changeEntry('"+sEntry+"')");
}
%>