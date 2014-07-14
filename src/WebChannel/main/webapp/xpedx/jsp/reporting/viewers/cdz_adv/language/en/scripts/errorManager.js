/*
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
*/_ERR_DEFAULT="Error";
_ERR_REFRESH_DATA="Refreshing Data";
_ERR_RUN_QUERY="Running Query";
_ERR_VIEW_SQL="View SQL";
_ERR_EDIT_QUERY="Edit Query";
_ERR_PROMPTS="Prompts";
_ERR_CONTEXTS="Contexts";
_ERR_LOV="List of Values";
_ERR_DRILL="Drill";
_ERR_FORMULA_VARIABLE="Formulas and Variables";
_ERR_QUERY_QUICK_FILTER="User Prompt Input";
_ERR_REPORT_MAP="Navigation Map";
_ERR_REPORT="Report";
_ERR_INVALID_SESSION_TITLE="Web Intelligence";
_ERR_INVALID_SESSION_MSG="Invalid session. Please close your browser and log on again. (WIH 00013)";
_ERR_OK_BUTTON_CAPTION="OK";
_ERR_OPEN_DOCUMENT="Open document";
_ERR_DOCUMENT_HAS_NO_REPORT="This document does not contain any reports.";
_ERR_INSERT_CALCULATION="Calculation";
_ERR_FILTER_BY="Filter";
_ERR_EDIT_FILTER="Filter Editor";
_ERR_FILTER_MAP="Document Structure and Filters";
_ERR_REMOVE="Remove";
_ERR_SETSECTION="Set as section";
_ERR_RESIZE="Resize";
_ERR_SORT="Sort";
_ERR_SWAP="Swap axis";
_ERR_ADD_CELL="Insert";
_ERR_ADD_OR_REPLACE="Add or replace";
_ERR_ALERTER="Alerters";
_ERR_BREAK="Break";
_ERR_DOC_PROPERTIES="Document Properties";
_ERR_DUPLICATE="Duplicate";
_ERR_QUICK_FILTER="Filter Editor";
_ERR_FORMAT="Format";
_ERR_INSERT_CELL="Cell";
_ERR_SAVE="Save";
_ERR_TURN_TO="Turn to";
_ERR_PREFERENCES="Preferences";
_ERR_DOWNLOAD_AS="Save to my computer as";
_ERR_EDIT_DOC="Edit";
_ERR_FETCH_DATA="Retrieving Data";
_ERR_PLUGIN_PDF_NOT_INSTALLED="Cannot view the document in PDF format. Do you still want to download the document?";
_ERR_VIEWER_PDFMODE="PDF mode";
_ERR_CANCEL="Cancel";
_ERR_CUSTOM_SORT="Cannot modify the custom sort.";
_ERR_DRILL_OUTOF_SCOPE_RIGHTS="You do not have rights to drill outside the scope of analysis.";
_ERR_USE_QUERY_DRILL_SCOPE_NOT_EMPTY="Scope of analysis must be undefined before setting the query drill option";
_ERR_COPY_PASTE_ELEMENT="You cannot cut or paste this element here";
_ERR_HYPERLINK="Hyperlink";
_ERR_MERGE_DIM_CREATION="Creating a merge dimension error.";
_ERR_LAYERING="Layering error";
_ERR_LAYERING_NO_BLOCK="Layering error : no block";
_ERR_PROCESSING_TDC="Error settings the data tracking options";
_ERR_ADDING_BLOCK="An error occured when you tried to insert a block in the report";
_ERR_REPORT_PART_PICKER="Report Part Picker";
_ERR_REPORT_PART_NOT_VALID="The reference of this report part is no longer valid. Please edit the document and select the report part again.";
_ERR_HYPERLINK_DOC_NOT_FOUND="Cannot find the document.";
_ERR_HYPERLINK_DOC_NOT_FOUND_RECOVERY="Cannot find the target document. Do you want to link to another document?";
_ERR_UNIVERSE="Universe not found";
_ERR_MAP_OBJECT="Error while mapping object";
_ERR_CHANGE_SOURCE="Error while changing source";
_ERR_ALIGNMENT_00="Error while processing alignment";
_ERR_ALIGNMENT_01="At least two objects must be selected to process alignment";
_ERR_ALIGNMENT_02="Alignment feature not applied if report elements are attached";
_ERR_OPENDOC_DOESNOT_MATCH_INSTANCE="No document instances match the parameter values";
_ERR_OPENDOC_HAS_NO_INSTANCE="The document has no instances";
_ERR_UPLOADFILE="File upload error";
_ERR_UPLOADFILE_INCORRECT_SIZE="The selected file is too big.";
_ERR_UPLOADFILE_INCORRECT_EXTENSION="The file extension is not correct.";
_ERR_CANCEL_QUERY="Cancel Query";
_ERR_GET_IMAGE="Getting Image";
_ERR_CLOSE_DOCUMENT="Closing Document";
_ERR_MERGEORSPLIT="Merge Split";
_ERR_MOVE_ELEMENT="Move Element";
_ERR_NEW_DOCUMENT="New Document";
_ERR_SAVE_QUERY="Save Query";
_VIEWER_ERR_DEFAULT="An unexpected error occurred. Contact your administrator with details of the action you performed before the error occurred (WIH 444444)";
_ERR_KEYDATE_PROPERTIES="Keydate Properties";
_ERR_UPLOADFILE_INCORRECT_FILENAME="The  file does not exist.";
_ERR_SECURITY_SET_PREFERENCES="An error occurred when trying to set preferences. Please contact your Business Objects administrator.";
_viewerErrorMsgPrefix="VIEWER:";
function displayErrorMsgDlg(strMsg,idTitle,okCB)
{
if (typeof(hideBlockWhileWaitWidget)!="undefined")
hideBlockWhileWaitWidget();
if (typeof(hideWaitDlg)!="undefined")
hideWaitDlg();
showAlertDialog(strMsg,idTitle,2,okCB);
}
function advDisplayErrorMsgDlg(strMsg,idTitle,okCB)
{
var topfs = getTopViewerFrameset();
if(topfs==null)
return
topfs.hideBlockWhileWaitWidget();
topfs.hideWaitDlg();
topfs.showAlertDialog(strMsg,idTitle,2,okCB);
}
function advDisplayViewerErrorMsgDlg(strMsg,idTitle,okCB,isTop)
{
var isTop=(typeof(isTop)=="undefined")?false:isTop;
if (startsWithIgnoreCase(strMsg,_viewerErrorMsgPrefix))
{
strMsg=strMsg.substring(_viewerErrorMsgPrefix.length);
strMsg=trim(strMsg);
var iPos=strMsg.indexOf(' ');
if (iPos>=0)
{
var strVar=strMsg.substring(iPos);
strMsg= strMsg.substring(0,iPos) + '+"' + strVar.replace(/"/g,"\\"+"\"") + '"';
}
if (isTop)
eval('displayErrorMsgDlg('+strMsg+',idTitle,okCB)');
else
eval('advDisplayErrorMsgDlg('+strMsg+',idTitle,okCB)');
}
else
{
if (isTop)
displayErrorMsgDlg(strMsg,idTitle,okCB)
else
advDisplayErrorMsgDlg(strMsg,idTitle,okCB);
}
}
function getLocalizedMessageString(msg)
{
var sRet = '';
if (msg)
{
var prefix = 'VIEWER:';
var iPos = msg.indexOf(prefix);
if (iPos >= 0)
{
msg = msg.substring(prefix.length);
iPos = msg.indexOf(' ');
if (iPos >= 0)
msg = msg.substring(0, iPos);
try
{
sRet = eval(msg);
}
catch(err)
{
sRet = msg;
}
}
else
sRet = msg;
}
return sRet;
}
