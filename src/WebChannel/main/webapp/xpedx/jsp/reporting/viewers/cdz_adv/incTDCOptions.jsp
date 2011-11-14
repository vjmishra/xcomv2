<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incTDCOptions.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("strEntry = " + strEntry );
TrackData tdc = doc.getTrackData();
if (tdc != null) {
TrackDataOptions tdcOptions = tdc.getTrackDataOptions();
TrackDataOption option = null;
Decoration deco = null;
Attributes atts = null;
TrackNumericDataOption numOption = null;
option = tdcOptions.getInsertedOption();
out.println("bInsertionCheck = " + option.isActive() +";");
deco = option.getDecoration();
_logger.info("insertionFntStyle = \"" + getFontStyle(deco.getFont()) +"\"");
out.println("insertionFntStyle = \"" + getFontStyle(deco.getFont()) +"\";");
atts = deco.getAttributes();
out.println("insertionFgColor = " + getColorString(atts.getForeground()) +";");
out.println("insertionBgColor = " + getColorString(atts.getBackground()) +";");
option = tdcOptions.getDeletedOption();
out.println("bDeletionCheck = " + option.isActive() +";");
deco = option.getDecoration();
_logger.info("deletionFntStyle = \"" + getFontStyle(deco.getFont()) +"\"");
out.println("deletionFntStyle = \"" + getFontStyle(deco.getFont()) +"\";");
atts = deco.getAttributes();
out.println("deletionFgColor = " + getColorString(atts.getForeground()) +";");
out.println("deletionBgColor = " + getColorString(atts.getBackground()) +";");
option = tdcOptions.getUpdatedOption();
out.println("bChangeCheck = " + option.isActive() +";");
deco = option.getDecoration();
_logger.info("changeFntStyle = \"" + getFontStyle(deco.getFont()) +"\"");
out.println("changeFntStyle = \"" + getFontStyle(deco.getFont()) +"\";");
atts = deco.getAttributes();
out.println("changeFgColor = " + getColorString(atts.getForeground()) +";");
out.println("changeBgColor = " + getColorString(atts.getBackground()) +";");
numOption = tdcOptions.getIncreasedOption();
out.println("bIncreasedCheck = " + numOption.isActive() +";");
deco = numOption.getDecoration();
_logger.info("increasedFntStyle = \"" + getFontStyle(deco.getFont()) +"\"");
out.println("increasedFntStyle = \"" + getFontStyle(deco.getFont()) +"\";");
atts = deco.getAttributes();
out.println("increasedFgColor = " + getColorString(atts.getForeground()) +";");
out.println("increasedBgColor = " + getColorString(atts.getBackground()) +";");
out.println("increasedThresholdActive= " +  numOption.isThresholdApplied() +";");
out.println("increasedThreshold= " +  numOption.getThreshold() +";");
numOption = tdcOptions.getDecreasedOption();
out.println("bDecreasedCheck = " + numOption.isActive() +";");
deco = numOption.getDecoration();
_logger.info("decreasedFntStyle = \"" + getFontStyle(deco.getFont()) +"\"");
out.println("decreasedFntStyle = \"" + getFontStyle(deco.getFont()) +"\";");
atts = deco.getAttributes();
out.println("decreasedFgColor = " + getColorString(atts.getForeground()) +";");
out.println("decreasedBgColor = " + getColorString(atts.getBackground()) +";");
out.println("decreasedThresholdActive= " +  numOption.isThresholdApplied() +";");
out.println("decreasedThreshold= " + numOption.getThreshold() +";");
out.println("var bIncTDCOptionsOK = true;");
} else {
out.println("var bIncTDCOptionsOK = false;");
}
_logger.info("<--incTDCOptions.jsp");
}
catch(Exception e)
{
out.println("var bIncTDCOptionsOK = false");
objUtils.incErrorMsg(e, out);
}
%>
<%!
String getColorString(java.awt.Color color)
{
if (color != null) {
            return "\"" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "\"";
        } else {
            return "\"-1,-1,-1\"";
        }
}
String getFontStyle(Font font)
throws IOException
{
if (font != null) {
    StringBuffer s = new StringBuffer((((font.getStyle() & StyleType.BOLD)==StyleType.BOLD)?"1":"0"));
    s.append("," + (((font.getStyle() & StyleType.ITALIC)==StyleType.ITALIC)?"1":"0"));
    s.append("," + (((font.getStyle() & StyleType.UNDERLINE)==StyleType.UNDERLINE)?"1":"0"));
    s.append("," + (((font.getStyle() & StyleType.STRIKETHROUGH)==StyleType.STRIKETHROUGH)?"1":"0"));
    return s.toString();
    } else {
    return "0,0,0,0";
    }
}
%>