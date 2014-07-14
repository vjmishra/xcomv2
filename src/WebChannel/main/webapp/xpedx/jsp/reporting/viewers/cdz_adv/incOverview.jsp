<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incOverview");
    String strEntry      = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
_logger.info("incOverview \t strEntry:" + strEntry);
    String iReport     = requestWrapper.getQueryParameter("iReport", false, "0");
    int              nReportIndex = Integer.parseInt(iReport);
    DocumentInstance doc          = reportEngines.getDocumentFromStorageToken(strEntry);
    printInfos(out, _logger, doc, nReportIndex, requestWrapper);
printPrompts(out, _logger, doc);
out.println("var incOverviewOK = true;");
_logger.info("<--incOverview");
}
catch(Exception e)
{
out.println("var incOverviewOK = false;");
objUtils.incErrorMsg(e, out);
}
%>
<%!
void printInfos(JspWriter out, DHTMLLogger _logger, DocumentInstance doc, int nReportIndex, RequestWrapper requestWrapper)
throws IOException, java.text.ParseException
{
    Properties props = doc.getProperties();
    _logger.info("Properties:" + props);
    StringBuffer properties = new StringBuffer("var props = new Array(");
    String [] propsToGet = { "lastsavedby", PropertiesType.DESCRIPTION, PropertiesType.KEYWORDS, PropertiesType.LOCALE, "creationdate", "lastrefreshdate", PropertiesType.ENHANCED_VIEWING, PropertiesType.REFRESH_ON_OPEN,
PropertiesType.BASE_LOCALE, PropertiesType.DATABASE_CONNECTED_DRILL,  PropertiesType.MERGE_DIMENSION, PropertiesType.IS_TRACK_DATA_ACTIVATED, PropertiesType.IS_TRACK_DATA_MODE_AUTO_USED};
    for (int i = 0; i < propsToGet.length; i++) {
        String s = props.getProperty( propsToGet[i]);
        if (propsToGet[i].equals(PropertiesType.LOCALE) || propsToGet[i].equals(PropertiesType.BASE_LOCALE))
   {
        Locale oLocal = ViewerTools.getLocaleFromSDKLocaleString (s);
        Locale oFormatLocal = ViewerTools.getLocaleFromSDKLocaleString (requestWrapper.getUserLocale());
        s = oLocal.getDisplayName(oFormatLocal);
   }
        properties.append( "\"" + ViewerTools.escapeQuotes(s) + "\"");
        if (propsToGet.length > 0 && i < (propsToGet.length - 1)) {
            properties.append(",");
        }
    }
    properties.append(");");
    properties.append("\n");
    properties.append("var bPermanentRegionalFormatting = ");
    if (doc.getFormattingLocale() == DocumentLocaleType.BASELOCALE) {
properties.append("true");
} else {
properties.append("false");
}
properties.append(";");
    out.println(properties);
}
void printPrompts(JspWriter out, DHTMLLogger _logger, DocumentInstance doc)
throws IOException, java.text.ParseException
{
    Prompts prompts = doc.getPrompts();
    _logger.info("Prompts:" + prompts);
    out.println("var prompts = new Array();");
    int count = prompts.getCount();
    String[] values = null;
    for (int i = 0; i < count; i++) {
        out.print("var prompt = new Array(");
        Prompt p = prompts.getItem(i);
        out.print( "\"" + p.getName() + "\" ");
        if ((p.getCurrentValues() != null) && (p.getCurrentValues().length > 0))
        {
            values = p.getCurrentValues();
        }
        else if ((p.getPreviousValues() != null) && (p.getPreviousValues().length > 0))
        {
            values = p.getPreviousValues();
        }
        else if ((p.getDefaultValues() != null) && (p.getDefaultValues().length > 0))
        {
            values = p.getDefaultValues();
        }
        int valCount = (values != null) ? values.length : 0;
        if (valCount > 0) out.print(", ");
        for (int j = 0; j < valCount; j++) {
            out.print( "\"" + values[j] + "\"");
            if (j < valCount - 1) {
                out.print(", ");
            }
        }
        out.println(");");
        out.println("prompts[" + i + "]= prompt;");
    }    
}
%>