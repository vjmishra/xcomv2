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
try
{
_logger.info("-->processAddCell.jsp");
String strEntry    = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strReport = requestWrapper.getQueryParameter("iReport", false, "0");
String bid         = requestWrapper.getQueryParameter("sBid", true);
String sNewCellType = requestWrapper.getQueryParameter("sNewCellType", true);
String sUnitIsInch = requestWrapper.getQueryParameter("sUnitIsInch", true);
String sX = requestWrapper.getQueryParameter("sX", true);
String sY = requestWrapper.getQueryParameter("sY", true);
String sRow = requestWrapper.getQueryParameter("sRow", false, "0");
int nReportIndex = Integer.parseInt(strReport);
int row = Integer.parseInt(sRow);
DocumentInstance doc     = reportEngines.getDocumentFromStorageToken(strEntry);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
ReportElement container = cellInfo.getReportElementByID(bid);
_logger.info("original container =" + container + ", bid=" + bid);
boolean prefUnitIsInch =  Boolean.valueOf(sUnitIsInch).booleanValue();
UnitType prefUnit = prefUnitIsInch ? UnitType.INCH : UnitType.MILLIMETER;
_logger.info("prefUnit=" + prefUnit);
double contX = 0;
double contY = 0;
if (container instanceof Cell)
{
_logger.info("get Cell parent = " + container + container.getID());
((Cell) container).setUnit(prefUnit);
contX = ((Cell) container).getX();
contY = ((Cell) container).getY();
_logger.info("contX = " + contX + ", contY=" + contY);
container = container.getFather();
} 
int nNewCellType = Integer.parseInt(sNewCellType);
_logger.info("nNewCellType = " + nNewCellType + ", bid=" + bid + ", sUnitIsInch=" + sUnitIsInch + ", sX=" + sX + ", sY=" + sY + ", row=" + row);
    int fctID;
    FormulaExpression formula = null;
    ReportStructure reportStruct = doc.getStructure();
    Function funct = null;
    String fName = null;
    String formulaStr = "";
    String followBid="";
switch (nNewCellType)
    {
    case 0:
              break;
          case 1:
            fctID = AllAvailableFunctionsIdentifiers.ID_DRILLFILTERS;
                  funct = reportStruct.getFunctionByID(fctID);
                  fName = funct.getName();
                  formulaStr = "=" + fName + "()";
              break;
          case 2:
          fctID = AllAvailableFunctionsIdentifiers.ID_LASTEXECUTIONDATE;
                  funct = reportStruct.getFunctionByID(fctID);
                  fName = funct.getName();
                  formulaStr = "=" + fName + "()";
          break;
          case 3:
          fctID = AllAvailableFunctionsIdentifiers.ID_DOCUMENTNAME;
                  funct = reportStruct.getFunctionByID(fctID);
                  fName = funct.getName();
                  formulaStr = "=" + fName + "()";
              break;
          case 4:
          fctID = AllAvailableFunctionsIdentifiers.ID_PAGE;
                  funct = reportStruct.getFunctionByID(fctID);
                  fName = funct.getName();
                  formulaStr = "=" + fName + "()";
          break;
          case 5:
          fctID = AllAvailableFunctionsIdentifiers.ID_PAGE;
                  funct = reportStruct.getFunctionByID(fctID);
                  fName = funct.getName();
                  formulaStr = "=" + fName + "()";
            fctID = AllAvailableFunctionsIdentifiers.ID_NUMBEROFPAGES;
                  funct = reportStruct.getFunctionByID(fctID);
                  fName = funct.getName();
                  formulaStr = formulaStr + "+ \"/\" + " + fName + "()";
          break;
          case 6:
          fctID = AllAvailableFunctionsIdentifiers.ID_NUMBEROFPAGES;
                  funct = reportStruct.getFunctionByID(fctID);
                  fName = funct.getName();
                  formulaStr = "=" + fName + "()";
          break;
       }
         _logger.info("processAddCell > formulaStr = " + formulaStr);
        ReportDictionary dic = doc.getDictionary();
        if(formulaStr.equals("") == false)
formula = dic.createFormula(formulaStr);
         _logger.info("processAddCell > formula = " + formula);
_logger.info("processAddCell > container = " + container + ", row=" + row);
double adjY = CurrentCellInfo.getYPositionFromRow((ReportElement) container, prefUnit, row, _logger);
        if (formula != null)
        {
        ReportCell cell = null;
        if (container instanceof ReportBody)
{
        cell = ((ReportBody) container).createReportCell(formula);
              _logger.info("add to ReportBody = " + container + container.getID());
        }
else if (container instanceof SectionContainer)
{
                cell = ((SectionContainer) container).createReportCell(formula);
                _logger.info("add to SectionContainer = " + container + container.getID());
        }
else if (container instanceof PageHeaderFooter)
{
                cell = ((PageHeaderFooter) container).createReportCell(formula);
                _logger.info("add to PageHeaderFooter = " + container + container.getID());
        }
            cell.setUnit(prefUnit);
cell.deleteAttachment();
      cell.setX(ViewerTools.toServerUnit(sX, prefUnit) + contX);
      cell.setY(ViewerTools.toServerUnit(sY, prefUnit) + adjY + contY);
      _logger.info("ViewerTools.toServerUnit(sX, prefUnit) = " + ViewerTools.toServerUnit(sX, prefUnit) + ", ViewerTools.toServerUnit(sY, prefUnit)="+ViewerTools.toServerUnit(sY, prefUnit) + ", adjY= " + adjY);
            cell.setAutoFitHeight(false);
            cell.setAutoFitWidth(false);
            followBid=cell.getID();
        }
else
{
        FreeCell cell = null;
        if (container instanceof ReportBody)
{
        cell = ((ReportBody) container).createFreeCell("");
                _logger.info("add to ReportBody = " + container + container.getID());
        }
else if (container instanceof SectionContainer)
{
                cell = ((SectionContainer) container).createFreeCell("");
            _logger.info("add to SectionContainer = " + container + container.getID());
        }
else if (container instanceof PageHeaderFooter)
{
                cell = ((PageHeaderFooter) container).createFreeCell("");
                _logger.info("add to PageHeaderFooter = " + container + container.getID());
        }
            cell.setUnit(prefUnit);
cell.deleteAttachment();
      cell.setX(ViewerTools.toServerUnit(sX, prefUnit) + contX);
      cell.setY(ViewerTools.toServerUnit(sY, prefUnit) + adjY + contY);
      _logger.info("ViewerTools.toServerUnit(sX, prefUnit) = " + ViewerTools.toServerUnit(sX, prefUnit) + ", ViewerTools.toServerUnit(sY, prefUnit)="+ViewerTools.toServerUnit(sY, prefUnit) + ", adjY= " + adjY);
cell.setAutoFitHeight(false);
            cell.setAutoFitWidth(false);
            followBid=cell.getID();
       }
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
_logger.info("new strEntry = " + strEntry);
String strQueryString = requestWrapper.getQueryString();
int iPos = strQueryString.indexOf("&sBid");
if (iPos >= 0)
  strQueryString = strQueryString.substring(0, iPos);
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
requestWrapper.setQueryString(strQueryString);
requestWrapper.setQueryParameter("sFollowBid", followBid);
_logger.info("<--processAddCell.jsp\n");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_ADD_CELL", true, out, session);
}
%>
