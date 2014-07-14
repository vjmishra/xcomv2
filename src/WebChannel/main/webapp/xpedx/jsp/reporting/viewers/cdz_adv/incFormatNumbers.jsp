<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
    _logger.info("-->incFormatNumbers.jsp");
    out.println("_formatNumbers = new Array()");
    for (int j = 0; j < 5; j++)
    {
            FormatNumberType formatType = FormatNumberType.fromInt(j);
            String constantName = "_" + formatType.toString();
            out.println("var " + constantName + " = " + j);
            ReportEngine objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
            FormatNumber[] allFormats = objReportEngine.getFormatNumbers(formatType);
            int count = allFormats.length;
            out.println("_formatNumbers["+ constantName + "] = new Array()");
            for (int i = 0; i < count; i++)
            {
                FormatNumber format = allFormats[i];
                String sample = format.getSample();
                if (sample == null) break;
                out.println("_formatNumbers["+ j + "][_formatNumbers["+ j + "].length] = \"" + ViewerTools.escapeQuotes(sample) + "\";");
            }
}
    _logger.info("<--incFormatNumbers.jsp");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>