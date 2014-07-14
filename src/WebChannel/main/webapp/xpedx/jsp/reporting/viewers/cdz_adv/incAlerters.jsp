<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incAlerter.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("strEntry = " + strEntry );
String sBids = requestWrapper.getQueryParameter("bids", false);
    String[] bids = ViewerTools.split(sBids, ",");
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
ReportStructure reports = doc.getStructure();
AlertersDictionary docAlerters=reports.getAlerters();
int nbDocAlerter=docAlerters.getCount();
_logger.info("nbDocAlerter = " + nbDocAlerter );
StringBuffer sb = new StringBuffer(1024);
sb.append("var arrAlerter = new Array;");
if (nbDocAlerter != 0)
{
for (int i = 0; i < nbDocAlerter; i++)
{
Alerter docAlerter = docAlerters.getAlerter(i);
sb.append("arrAlerter[" + i + "]= new AlerterElement(");
String alrtID = docAlerter.getID();
sb.append( alrtID );
sb.append(",");
sb.append("'" + ViewerTools.escapeQuotes(docAlerter.getName()) + "'");
int state = -1;
for (int k = 0; k < bids.length; k++) 
{
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bids[k]);
if (cellInfo.m_tableCell == null) break;
_logger.info( "CurrentCellInfo with BID = " + bids[k] );
Alerters cellAlerters = cellInfo.m_tableCell.getAlerters();
int nbCellAlerter = cellAlerters.getCount();
int temp = 0;
for (int j = 0; j < nbCellAlerter; j++)
{
if (cellAlerters.getAlerter(j).getID().equals(alrtID))
{
temp=1;
break;
}
}
if ( k == 0 )
state = temp;
else
state = (state == temp)? temp : -1;
}
sb.append(",");
sb.append(state);
sb.append(",");
sb.append("'" + ViewerTools.escapeQuotes(docAlerter.getDescription()) + "'");
sb.append(");\n");
_logger.info("Alerter id <"+ alrtID +"> - name <"+ docAlerter.getName() +"> - state <"+ state +">");
}
}
out.println(sb + "var incAlertersOK = true;");
_logger.info("<--incAlerter.jsp");
}
catch(Exception e)
{
out.println("var incAlertersOK = false;");
objUtils.incErrorMsg(e, out);
}
%>