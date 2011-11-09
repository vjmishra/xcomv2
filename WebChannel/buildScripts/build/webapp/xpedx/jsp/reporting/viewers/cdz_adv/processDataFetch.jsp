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
_logger.info("-->processDataFetch.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);     
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "");
String strShowNoFetchDataMsg = requestWrapper.getQueryParameter("bNoFetchDataMsg", false, "false");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
DataProviders dps = doc.getDataProviders();
DataProvider dp=null;
int nDP=dps.getCount(), nRows=0;
String sDPList="",sDPRows="";
for(int j=0;j<nDP; j++)
{
dp= dps.getItem(j);
nRows = dp.getRowCount();
if(j>0)
sDPRows+=";";
sDPRows+=nRows;
_logger.debug("dp"+j+" has "+nRows+" lines");
}
if(strShowNoFetchDataMsg.equals("true"))
{
if(!strDPIndex.equals(""))
{
int iDPIndex = Integer.parseInt(strDPIndex);
dp = dps.getItem(iDPIndex);
nRows = dp.getRowCount();
if(nRows<1) 
sDPList+="\\n    "+dp.getName(); 
}
else 
{
for(int j=0;j< nDP; j++)
{
dp= dps.getItem(j);
nRows = dp.getRowCount();
if(nRows<1)
sDPList+="\\n    "+dp.getName();
}
}
}
%>
<html>
<head>
<script language="javascript">
function loadCB()
{
var sDPList="<%=sDPList%>";
var sDPRows = "<%=sDPRows%>", arrDPRows = sDPRows.split(";");
if(sDPList!="")
{
parent.displayNoDataToFetch(sDPList)
}
parent.updateDPsRows(arrDPRows);
}
</script>
</head>
<body onload="loadCB()">
</body>
</html>
<%
_logger.info("<--processDataFetch.jsp");
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_FETCH_DATA", true, out, session);
}
%>