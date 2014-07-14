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
--><%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive)
{
objUtils.invalidSessionDialog(out);
return;
}
String strEntry = null;
_logger.info("--> processTrackDataChanges.jsp");
try {
strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry = " + strEntry);
String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
String iReport     = requestWrapper.getQueryParameter("iReport", true, "0");
 int nReportIndex = Integer.parseInt(iReport);
String strEnableTDC = requestWrapper.getQueryParameter("sEnableTdc", false, "");
String strAutoUpdateChanges = requestWrapper.getQueryParameter("bAutoUpdateChanges",false,"false");
String strRefreshNow = requestWrapper.getQueryParameter("bRefreshNow",false,"false");
String strShowChanges = requestWrapper.getQueryParameter("sShowChanges", false, "");
String strShowCurrentReportChanges = requestWrapper.getQueryParameter("sShowCurrentReportChanges", false, "");
String strSetAsRef = requestWrapper.getQueryParameter("sSetAsRef", false, "");
String sTdcOptions = requestWrapper.getQueryParameter("sTdcOptions", false, "");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
TrackData tdc = doc.getTrackData();
if (tdc != null) {
if (!strEnableTDC.equals("")) {
boolean bEnableTDC = Boolean.valueOf(strEnableTDC).booleanValue();
_logger.info("bEnableTDC = " + bEnableTDC);
boolean bAutoUpdateChanges = Boolean.valueOf(strAutoUpdateChanges).booleanValue();
_logger.info("bAutoUpdateChanges=" + bAutoUpdateChanges);
tdc.setTrackDataMode(bEnableTDC?(bAutoUpdateChanges?ReferenceUpdateMode.AUTO:ReferenceUpdateMode.USER_DEFINED):ReferenceUpdateMode.DISABLED);
} 
if (strSetAsRef.equals("true")) {
_logger.info("setCurrentAsReference ");
tdc.setCurrentAsReference();
} 
if (sTdcOptions.equals("true")) {
_logger.info("set TDC Options ");
String strTDCFormat = requestWrapper.getQueryParameter("TDCFormat", false, "false");
boolean bTDCFormat = Boolean.valueOf(strTDCFormat).booleanValue();
_logger.info("Format TDC : " + bTDCFormat);
TrackDataOptions tdcOptions = tdc.getTrackDataOptions();
TrackDataOption option = null;
String insertionCheck = requestWrapper.getQueryParameter("insertionCheck", false, "");
boolean bInsertionCheck = Boolean.valueOf(insertionCheck).booleanValue();
option = tdcOptions.getInsertedOption();
option.showChanges(bInsertionCheck);
_logger.info("\nbInsertionCheck=" + bInsertionCheck);
String fgColor;
String bgColor;
String fntStyle;
if (bTDCFormat) {
fgColor = requestWrapper.getQueryParameter("insertionFgColor", false, "");
bgColor = requestWrapper.getQueryParameter("insertionBgColor", false, "");
fntStyle = requestWrapper.getQueryParameter("insertionFntStyle", false, "");
_logger.info("InsertionfntStyle=" + fntStyle);
setFontProperties(_logger, option.getDecoration(), fgColor, bgColor, fntStyle);
}
String deletionCheck = requestWrapper.getQueryParameter("deletionCheck", false, "");
boolean bDeletionCheck = Boolean.valueOf(deletionCheck).booleanValue();
option = tdcOptions.getDeletedOption();
option.showChanges(bDeletionCheck);
_logger.info("\nbDeletionCheck=" + bDeletionCheck);
if (bTDCFormat) {
fgColor = requestWrapper.getQueryParameter("deletionFgColor", false, "");
bgColor = requestWrapper.getQueryParameter("deletionBgColor", false, "");
fntStyle = requestWrapper.getQueryParameter("deletionFntStyle", false, "");
_logger.info("DeletionfntStyle=" + fntStyle);
setFontProperties(_logger, option.getDecoration(), fgColor, bgColor, fntStyle);
}
String changeCheck = requestWrapper.getQueryParameter("changeCheck", false, "");
boolean bChangeCheck = Boolean.valueOf(changeCheck).booleanValue();
option = tdcOptions.getUpdatedOption();
option.showChanges(bChangeCheck);
_logger.info("\nbChangeCheck=" + bChangeCheck);
if (bTDCFormat) {
fgColor = requestWrapper.getQueryParameter("changeFgColor", false, "");
bgColor = requestWrapper.getQueryParameter("changeBgColor", false, "");
fntStyle = requestWrapper.getQueryParameter("changeFntStyle", false, "");
_logger.info("ChangefntStyle=" + fntStyle);
setFontProperties(_logger, option.getDecoration(), fgColor, bgColor, fntStyle);
}
TrackNumericDataOption numOption = null;
String increasedCheck = requestWrapper.getQueryParameter("increasedCheck", false, "");
boolean bIncreasedCheck = Boolean.valueOf(increasedCheck).booleanValue();
numOption = tdcOptions.getIncreasedOption();
numOption.showChanges(bIncreasedCheck);
_logger.info("\nbIncreasedCheck=" + bIncreasedCheck);
if (bTDCFormat) {
fgColor = requestWrapper.getQueryParameter("increasedFgColor", false, "");
bgColor = requestWrapper.getQueryParameter("increasedBgColor", false, "");
fntStyle = requestWrapper.getQueryParameter("increasedFntStyle", false, "");
_logger.info("IncreasefntStyle=" + fntStyle);
setFontProperties(_logger, numOption.getDecoration(), fgColor, bgColor, fntStyle);
}
String greaterEqualIncreasedCheck = requestWrapper.getQueryParameter("greaterEqualIncreasedCheck", false, "");
boolean bGreaterEqualIncreasedCheck = Boolean.valueOf(greaterEqualIncreasedCheck).booleanValue();
numOption.applyThreshold(bGreaterEqualIncreasedCheck);
_logger.info("bGreaterEqualIncreasedCheck=" + bGreaterEqualIncreasedCheck);
String thresholdIncreasedField = requestWrapper.getQueryParameter("thresholdIncreasedField", false, "0");
numOption.setThreshold(Double.parseDouble(thresholdIncreasedField));
String decreasedCheck = requestWrapper.getQueryParameter("decreasedCheck", false, "");
boolean bDecreasedCheck = Boolean.valueOf(decreasedCheck).booleanValue();
numOption = tdcOptions.getDecreasedOption();
numOption.showChanges(bDecreasedCheck);
_logger.info("\nbDecreasedCheck=" + bDecreasedCheck);
if (bTDCFormat) {
fgColor = requestWrapper.getQueryParameter("decreasedFgColor", false, "");
bgColor = requestWrapper.getQueryParameter("decreasedBgColor", false, "");
fntStyle = requestWrapper.getQueryParameter("decreasedFntStyle", false, "");
_logger.info("DecreasefntStyle=" + fntStyle);
setFontProperties(_logger, numOption.getDecoration(), fgColor, bgColor, fntStyle);
}
String greaterEqualDecreasedCheck = requestWrapper.getQueryParameter("greaterEqualDecreasedCheck", false, "");
boolean bGreaterEqualDecreasedCheck = Boolean.valueOf(greaterEqualDecreasedCheck).booleanValue();
numOption.applyThreshold(bGreaterEqualDecreasedCheck);
_logger.info("bGreaterEqualDecreasedCheck=" + bGreaterEqualDecreasedCheck);
String thresholdDecreasedField = requestWrapper.getQueryParameter("thresholdDecreasedField", false, "0");
numOption.setThreshold(Double.parseDouble(thresholdDecreasedField));
if (bTDCFormat) {
tdc.update();
}
_logger.info("tdc is UPDATED.");
}
if (!strShowCurrentReportChanges.equals("")) {
boolean bShowChanges = Boolean.valueOf(strShowCurrentReportChanges).booleanValue();
TrackDataInfo tdi = (doc.getReports().getItem(nReportIndex)).getTrackDataInfo();
_logger.info("showChanges =" + bShowChanges + " on report idx=" + nReportIndex);
tdi.showChanges(bShowChanges);
} 
if (!strShowChanges.equals("")) {
boolean bShowChanges = Boolean.valueOf(strShowChanges).booleanValue();
_logger.info("showChanges =" + bShowChanges + " on ALL reports.");
tdc.showChanges(bShowChanges);
} 
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strQueryString = requestWrapper.getQueryString();
int iPos = strQueryString.indexOf("&sBid");
if (iPos>=0)
  strQueryString = strQueryString.substring(0, iPos);
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
requestWrapper.setQueryString(strQueryString);
}
boolean bRefreshNow = Boolean.valueOf(strRefreshNow).booleanValue();
_logger.info("tdc RefreshNow: " + bRefreshNow);
String redirectUrl = bRefreshNow?"refreshDocument.jsp":"report.jsp";
_logger.info("Redirect to :" + redirectUrl);
_logger.info("<-- processTrackDataChanges.jsp");
%>
<jsp:forward page="<%=redirectUrl%>"/>
<%
} catch(Exception e) {
objUtils.displayErrorMsg(e, "_ERR_PROCESSING_TDC", true, out, session);
} 
%>
<%!
void setFontProperties(DHTMLLogger _logger, Decoration deco, String fgColor, String bgColor, String fntStyleList)
{
    Font font = deco.getFont();
String [] fntStyle = fntStyleList.split(",");
   int style = 0;
_logger.info("before font.getStyle()=" + font.getStyle());
    if (fntStyle[0].equals("1")) {
    _logger.info("BOLD");
    style |= StyleType.BOLD;
    } 
    if (fntStyle[1].equals("1")) {
    _logger.info("ITALIC");
    style |= StyleType.ITALIC;
    } 
    if (fntStyle[2].equals("1")) {
    _logger.info("UNDERLINE");
    style |= StyleType.UNDERLINE;
    } 
    if (fntStyle[3].equals("1")) {
    _logger.info("STRIKETHROUGH");
    style |= StyleType.STRIKETHROUGH;
    } 
    font.setStyle(style);
    _logger.info("after font.getStyle()=" + font.getStyle());
    Attributes atts = deco.getAttributes();
    _logger.info("fgColor=" + fgColor);
    if (!fgColor.equals("")){
Color fg = fgColor.equals("-1,-1,-1") ? null : ViewerTools.toColor(ViewerTools.split(fgColor, ","));
    atts.setForeground(fg);
    }
_logger.info("bgColor=" + bgColor);
    if (!bgColor.equals("")) {
Color bg = bgColor.equals("-1,-1,-1") ? null : ViewerTools.toColor(ViewerTools.split(bgColor, ","));
    atts.setBackground(bg);
    }
}    
 %>