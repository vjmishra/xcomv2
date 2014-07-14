<%@ include file="incStartpage.jsp" %>
<jsp:useBean id="objPromptsBean" class="com.businessobjects.adv_ivcdzview.Prompts" scope="page">
<%
objPromptsBean.onStart(requestWrapper);
%>
</jsp:useBean>
<%
response.setDateHeader("expires", 0);
if (!isAlive)
{
return;
}
try
{
_logger.info("--> incQuickPrompts.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("sEntry IN: " + strEntry);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
Prompts arrPrompts = doc.getPrompts();
int iNbPrompts = arrPrompts.getCount();
String[][] arrPromptNames = new String[iNbPrompts][2];
for (int i=0; i<iNbPrompts; i++)
{
arrPromptNames[i][0] = arrPrompts.getItem(i).getName();
arrPromptNames[i][1] = "false";
for (int j=0; j<i; j++) {
if (arrPromptNames[i][0].equalsIgnoreCase(arrPromptNames[j][0]))
{
arrPromptNames[i][1] = "true";  
arrPromptNames[j][1] = "true";
break;
}
}
}
String strDefaultInputFormat = null;
String strDocType = doc.getProperties().getProperty(PropertiesType.DOCUMENT_TYPE) ;
if (strDocType.equalsIgnoreCase("WID"))
{
ReportEngine objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
if(objReportEngine!=null)
{
FormatNumber defaultFormat = doc.getDefaultFormatNumber(FormatNumberType.DATE_TIME);
if((defaultFormat!= null) && (defaultFormat instanceof DefaultDateTimeFormatNumber)) 
strDefaultInputFormat = ((DefaultDateTimeFormatNumber) defaultFormat).getDateFormatting().getPositive(); 
}
}
String strDocID = doc.getProperties().getProperty(PropertiesType.DOCUMENT_ID);
String strUserRefreshLov = objUserSettings.getUserDocRight("REFRESH_LISTS_OF_VALUES", strDocID);
out.println("strUserRefreshLov=\"" + strUserRefreshLov.toLowerCase() + "\";");
String strUseLov = objUserSettings.getUserDocRight("USE_LOV", strDocID);
out.println("strUseLov=\"" + strUseLov.toLowerCase() + "\";");
out.println("iNbPrompts=" + Integer.toString(iNbPrompts) + ";");
out.println("arrPrompts=new Array();");
out.println("arrNbLovItems=new Array();");
out.println("arrSelectedValues=new Array();");
out.println("arrLovTexts=new Array();");
out.println("arrLovValues=new Array();");
out.println("arrLovIndexes=new Array();");
out.println("arrNbBatchLovItems=new Array();");
out.println("arrLovTitle=new Array();");
out.println("iLovSortType=new Array();");
out.println("iLovSortedColumnIndex=new Array();");
for (int i=1; i<=iNbPrompts; i++)
{
Prompt objPrompt = arrPrompts.getItem(i-1);
String strIndex = Integer.toString(i-1);
if (arrPromptNames[i-1][1].equals("true"))
{
try
{
DataProvider[] arrDataProviders = objPrompt.getDataProviders();
String strDPName = " (";
for (int j=0; j<arrDataProviders.length; j++)
{
if (j == 0) strDPName += arrDataProviders[j].getName();
else strDPName += ", " + arrDataProviders[j].getName();
}
strDPName += ")";
arrPromptNames[i-1][0] += strDPName;
}
catch (Exception e1)
{
_logger.info("Cannot get Dataproviders from prompts");
e1.printStackTrace();
}
}
String strEscapedPromptName = ViewerTools.convStr(arrPromptNames[i-1][0]);
String strInputFormat = objPrompt.getInputFormat();
if (null == strInputFormat) strInputFormat = "";
boolean blnRequired = objPrompt.requireAnswer();
boolean blnDateType = false;
if (objPrompt.getObjectType() == ObjectType.DATE)
{
blnDateType = true;
if(strDefaultInputFormat != null)
strInputFormat=strDefaultInputFormat;
}
String strParamName = "PV" + Integer.toString(i);
String strParamIndexName = "PI" + Integer.toString(i);
String strParamValue = "";
String[] arrSeletedValues = requestWrapper.getQueryParameterValues(strParamName);
if (arrSeletedValues == null)
{
ValueFromLov[] arrTemp = null;
if (objPrompt.getCurrentValuesFromLov() != null && objPrompt.getCurrentValuesFromLov().length > 0)
{
arrTemp = objPrompt.getCurrentValuesFromLov();
}
else if (objPrompt.getPreviousValuesFromLov() != null && objPrompt.getPreviousValuesFromLov().length > 0)
{
arrTemp = objPrompt.getPreviousValuesFromLov();
}
else if (!objPrompt.isOptional() && objPrompt.getDefaultValuesFromLov() != null && objPrompt.getDefaultValuesFromLov().length > 0)
{
arrTemp = objPrompt.getDefaultValuesFromLov();
}
if (arrTemp != null)
{
for (int j=0; j<arrTemp.length; j++)
{
if (j > 0) strParamValue += ";";
String strValueIndex = ViewerTools.encodeIndex(arrTemp[j].getRowIndex());
if (strValueIndex == null) strValueIndex = "";
strParamValue += strValueIndex + "_" + arrTemp[j].getValue();
}
}
}
else
{
if (arrSeletedValues.length == 1)
arrSeletedValues = ViewerTools.split(arrSeletedValues[0], ";");
String[] arrSeletedIndexes = requestWrapper.getQueryParameterValues(strParamIndexName);
if (arrSeletedIndexes == null)
arrSeletedIndexes = new String[arrSeletedValues.length];
else if (arrSeletedIndexes.length == 1)
arrSeletedIndexes = ViewerTools.split(arrSeletedIndexes[0], ";");
for (int j=0; j<arrSeletedValues.length; j++)
{
if (j > 0) strParamValue += ";";
if (arrSeletedIndexes[j] != null)
strParamValue += arrSeletedIndexes[j] + "_" + arrSeletedValues[j];
else
strParamValue += "_" + arrSeletedValues[j];
}
}
Lov objLOV = null;
String strLovID = "";
boolean blnLOV = false;
if (strUseLov.equals("full"))
{
blnLOV = objPrompt.hasLOV();
if (blnLOV && objPrompt.isConstrained())
{
try
{
objLOV = objPrompt.getLOV();
if (objLOV != null)
strLovID = objLOV.getID();
}
catch(Exception e)
{
_logger.info("Internal error: Cannot get LOV object");
e.printStackTrace();
blnLOV = false;
}
}
}
out.println("p=arrPrompts[" + strIndex + "]=new Array(13);");
out.println("p[0]=\"" + ViewerTools.escapeQuotes(strEscapedPromptName) + "\";");
out.println("p[1]=\"" + strParamName + "\";");
out.println("p[2]=\"" + strInputFormat + "\";");
out.println("p[3]=" + ((blnDateType)?"true":"false") + ";");
if (objPrompt.getType() == PromptType.Mono)
    out.println("p[4]=\"Mono\";");
else if (objPrompt.getType() == PromptType.Multi)
out.println("p[4]=\"Multi\";");
out.println("p[5]=" + ((blnLOV)?"true":"false") + ";");
out.println("p[6]=" + ((objPrompt.isConstrained())?"true":"false") + ";");
out.println("p[7]=false;");
out.println("p[8]=encodeEmptyValue(\"" + ViewerTools.escapeQuotes(strParamValue) + "\",true);");
out.println("p[9]=" + ((blnRequired)?"true":"false") + ";");
out.println("p[10]=\"" + ViewerTools.escapeQuotes(arrPromptNames[i-1][0]) + "\";");
out.println("p[11]=\"" + strLovID + "\";");
out.println("p[12]=" + ((objPrompt.isOptional())?"true":"false") + ";");
out.println("arrNbBatchLovItems[" + strIndex + "]=0;");
out.println("arrNbLovItems[" + strIndex + "]=0;");
out.println("arrLovTitle[" + strIndex + "]=\"\";");
}
arrPrompts = null;
doc = null;
_logger.info("<-- incQuickPrompts.jsp");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>
