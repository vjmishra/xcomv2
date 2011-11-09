<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incDocumentProperties.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("strEntry = " + strEntry );
   Properties props = doc.getProperties();
out.print("var docProps = newDocProps(");
    String[] spropsToGet = { PropertiesType.NAME, PropertiesType.AUTHOR,
PropertiesType.DESCRIPTION, PropertiesType.KEYWORDS, PropertiesType.LAST_REFRESH_DATE,
PropertiesType.LAST_REFRESH_DURATION, PropertiesType.LOCALE, PropertiesType.TITLE,
PropertiesType.VERSION, PropertiesType.PREVIOUS_VERSION};
for (int i = 0; i < spropsToGet.length; i++)
{
        String s = props.getProperty(spropsToGet[i]);
_logger.info("spropsToGet["+i+"] = " + s);
        if (spropsToGet[i].equals(PropertiesType.LOCALE))
   {
        Locale oLocal = ViewerTools.getLocaleFromSDKLocaleString (s);
        Locale oFormatLocal = ViewerTools.getLocaleFromSDKLocaleString (requestWrapper.getUserLocale());
        s = oLocal.getDisplayName(oFormatLocal);
   }
        s = (s == null)?"":s;
        out.print( "\"" + ViewerTools.escapeQuotes(s) + "\"" );
        if (spropsToGet.length > 0)
            out.print(",");
    }
String[] bpropsToGet = { PropertiesType.ENHANCED_VIEWING, PropertiesType.REFRESH_ON_OPEN,
PropertiesType.DATABASE_CONNECTED_DRILL, PropertiesType.MERGE_DIMENSION };
for (int j = 0; j < bpropsToGet.length; j++)
{
        String s = props.getProperty(bpropsToGet[j]);
_logger.info("bpropsToGet["+j+"] = " + s);
        s = (s == null)?"":s;
        Boolean b = new Boolean(s);
        out.print( b );
        if ( bpropsToGet.length > 0)
            out.print(",");
    }
    if ( doc.getFormattingLocale() == DocumentLocaleType.BASELOCALE )
    {
out.print("true");
_logger.info("getFormattingLocale() = DocumentLocaleType.BASELOCALE");
}
else
{
out.print("false");
_logger.info("getFormattingLocale() = DocumentLocaleType.LOCALE");
}
out.print(")\n");
_logger.info("<--incDocumentProperties.jsp");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>