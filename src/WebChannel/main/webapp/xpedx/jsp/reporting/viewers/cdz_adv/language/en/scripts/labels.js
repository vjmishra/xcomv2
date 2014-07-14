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
*/_drillbar_allValues="All values"
_drillbar_remove="Remove"
_drillbar_noValue="No value"
_drillbar_combo_tooltip="Drill filter on"
_drillbar_title="Analysis context:"
_drillbar_icon_tooltip="Add drill filter"
_defaultDocName="New Web Intelligence Document"
_defaultRepName="Report"
_defaultRepTitle="Report Title"
_wait_dialogbox_title="Please Wait"
_prompts_title="Prompts"
_view_in_pdf_format="View in PDF Format, please wait"
_viewSQLLabel = 'View SQL'
_selectSQLLabel = 'Select'
_pageHeader = 'Page Header'
_pageBody = 'Page Body'
_pageFooter = 'Page Footer'
_localeDateFormat = "M/d/yyyy h:mm:ss aa"
_localeTimeAM = "AM"
_localeTimePM = "PM"
_noRefreshYet="Data is not refreshed."
_schedulePromptsButton="Modify values"
_wait_opendoc_title="Opening Document"
_wait_opendoc_msg="Please wait while the document is being processed"
_keydateUsedLabel = "Keydate used:";
_lastAvailableLabel = "Last available";
_DOCMAP_IMG=0
_FORMULA_IMG=1
_QUALIF_IMG=2
function getIconAlt(imgID,idx)
{
var iconAlt=''
switch(imgID)
{
case _DOCMAP_IMG:
switch(idx)
{
case 0:
iconAlt="Report"
break
case 1:
iconAlt="Section"
break
default:
iconAlt="Navigation Map"
break
}
break;
case _FORMULA_IMG:
switch(idx)
{
case 1:
iconAlt="Cancel"
break
case 2:
iconAlt="Validate"
break
}
break;
case _QUALIF_IMG:
switch(idx)
{
case 0:
iconAlt="Universe"
break
case 1:
case 9:
iconAlt="Class"
break
case 2:
iconAlt="Dimension"
break
case 3:
iconAlt="Measure"
break
case 4:
iconAlt="Detail"
break
case 5:
case 6:
case 7:
iconAlt="Filter"
break
case 8:
iconAlt="Hierarchy"
break
}
break;
}
return iconAlt;
}
