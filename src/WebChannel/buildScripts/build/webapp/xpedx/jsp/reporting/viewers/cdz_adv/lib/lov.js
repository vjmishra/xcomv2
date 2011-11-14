_LOV_MULTI_COLH = 140
function newLovWidget(id,label,w,lines,showText,showRefresh,moveCB,refreshCB,searchCB,
dblClickCB,multi,enterCB,showDate,showTextOnly)
{
_arrowSize= 20
_refreshIcnSize= 28
_searchIcnSize= 40
_margin= 2
_textFieldH= 20;
_searchMinW = 20;
var comboW= showRefresh ? (w - (2*_arrowSize+_refreshIcnSize)) : (w - (2*_arrowSize))
var o=newWidget(id)
o.width=w
o.lines=lines
o.textField= newTextFieldWidget(id+"_textField",null,250,null,LovWidget_enterCB,true,_lovTextFieldLab,w);
o.textField.par= o;
o.dateField= newCalendarTextFieldButton(id+"_calendarText",null,null,null,null,LovWidget_enterCB,true,_lovCalendarLab,w);
o.dateField.par= o;
o.chunkLabel= newWidget(id+"_label");
o.prevChunkIcn= newIconWidget(id+"_prevChunk",_skin+'../lov.gif',LovWidget_prevCB,null,_lovPrevChunkLab,7,16,2*16,0,2*16,16);
o.prevChunkIcn.par= o;
o.chunkCombo= newComboWidget(id+"_chunk",LovWidget_comboCB,true,comboW,_lovComboChunkLab);
o.chunkCombo.par= o;
o.nextChunkIcn= newIconWidget(id+"_nextChunk",_skin+'../lov.gif',LovWidget_nextCB,null,_lovNextChunkLab,7,16,2*16+1*7,0,2*16+1*7,16);
o.nextChunkIcn.par= o;
o.refreshIcn= newIconWidget(id+"_refresh",_skin+'../lov.gif',LovWidget_refreshCB,_lovRefreshValuesLab,_lovRefreshLab,16,16,16,0);
o.refreshIcn.par= o;
o.refreshIcn.txtPosition="left";
o.hideChunk_n_Refresh= false;
var multiSelection= (multi != null) ? multi : false;
o.lovList=newMultiColumnList(id+"_lov",null,multiSelection,w,null,LovWidget_dblClickCB,null,false,LovWidget_ListKeyUpCB)
o.lovList.setLines(lines)
o.lovList.addColumn("",_LOV_MULTI_COLH,false)
o.lovList.par= o;
o.lovSearch= newSearchWidget(id+"_search",null,searchCB);
o.lovSearch.par= o;
o.showTextField= showText;
o.showRefreshIcn= showRefresh;
o.showTextOnly= showTextOnly==null?false:showTextOnly;
o.chunkText= label;
o.showDate= showDate==null?false:showDate;
o.lovMessage= '';
o.oldInit= o.init;
o.goToChunk= LovWidget_goToChunk;
o.setPrevNextIconState= LovWidget_setPrevNextIconState;
o.moveCB= moveCB;
o.refreshCB= refreshCB;
o.searchCB= searchCB;
o.dblClickCB= dblClickCB;
o.enterCB= enterCB;
o.init= LovWidget_init;
o.getHTML= LovWidget_getHTML;
o.resize= LovWidget_resize;
o.addChunkElem= LovWidget_addChunkElem;
o.addLOVElem= LovWidget_addLOVElem;
o.fillChunk= LovWidget_fillChunk;
o.fillLOV= LovWidget_fillLOV;
o.getSearchValue= LovWidget_getSearchValue;
o.setSearchValue= LovWidget_setSearchValue;
o.getTextValue= LovWidget_getTextValue;
o.setTextValue= LovWidget_setTextValue;
o.getDateValue= LovWidget_getDateValue;
o.setDateValue= LovWidget_setDateValue;
o.getChunkSelection= LovWidget_getChunkSelection;
o.getLOVSelection= LovWidget_getLOVSelection;
o.setTooltips= LovWidget_setTooltips;
o.change= LovWidget_change;
o.showTextFieldOnly= LovWidget_showTextFieldOnly;
o.setFormatDate= LovWidget_setFormatDate;
o.isCaseSensitive= LovWidget_isCaseSensitive;
o.setCaseSensitive= LovWidget_setCaseSensitive;
o.updateWidget= LovWidget_updateWidget;
o.showRefreshButton= LovWidget_showRefreshButton;
o.setLovMessage= LovWidget_setLovMessage;
o.showLovMessage= LovWidget_showLovMessage;
o.resizeLovMessageZone= LovWidget_resizeLovMessageZone;
o.setLovSearchIconDisabled=LovWidget_setLovSearchIconDisabled;
o.showPartialResult= LovWidget_showPartialResult;
o.showDelegateSearch= LovWidget_showDelegateSearch;
return o;
}
function LovWidget_getHTML()
{
var o=this, s =''
s='<table id="'+o.id+'" class="dialogzone" border="0" cellspacing="0" cellpadding="0"><tbody>'
s+= '<tr>' + 
'<td colspan="4">' + o.textField.getHTML() + '</td>' + 
'</tr>'
s+= '<tr>' + 
'<td colspan="4">' + o.dateField.getHTML() + '</td>' + 
'</tr>'
s+='<tr>' +
'<td colspan="4"><span id="' + o.chunkLabel.id + '"></span></td>' +
'</tr>'
s+='<tr' + (o.hideChunk_n_Refresh?' style="display:none">':'>') +
'<td align="center">' + o.prevChunkIcn.getHTML() + '</td>' +
'<td align="center">' + o.chunkCombo.getHTML() + '</td>' +
'<td align="center">' + o.nextChunkIcn.getHTML() + '</td>' +
'<td align="right">' +  
'<table class="dialogzone" border="0" cellspacing="0" cellpadding="0"><tbody>' +
'<tr>' + 
'<td>' + o.refreshIcn.getHTML() + '</td>' +
'</tr>' +
'</tbody></table>' +
'</td>' +
'</tr>'
s+= '<tr>' +
'<td colspan="4">' + 
'<table border="0" cellspacing="0" cellpadding="0">' +
'<tr>' +
'<td id="'+o.id+'_lovListContainer">' + o.lovList.getHTML() + '</td>' +
'<td id="'+o.id+'_lovMessageContainer" style="display:none" valign="top">' +
'<div id="'+o.id+'_lovMessageText" onselectstart="return false" class="lovMessage" style="overflow:auto;padding-left:4px">' +
o.lovMessage +
'<div>' +
'</td>' +
'</tr>' +
'</table>' +
'</td>' +
'</tr>'
s+='<tr>' +
'<td colspan="4" id="'+o.id+'_lovPartialResult" style="display:none">' +
'<table border="0" cellspacing="0" cellpadding="0">' +
'<tr>' +
'<td>' +
simpleImgOffset(_skin+'../lov.gif',16,16,53,15,o.id+"_lovPartialResulImg",null,_lovPartialResultLab) +
'</td>' +
'<td width="5"></td>' +
'<td class="iconText" valign="middle">' +
_lovPartialResultLab +
'</td>' +
'</tr>' +
'</table>' +
'</td>' +
'</tr>'
s+='<tr>' +
'<td colspan="4">' +
o.lovSearch.getHTML()+
'</td>' +
'</tr>' +
'</tbody></table>'
return s
}
function LovWidget_init()
{
var o=this
o.oldInit()
o.textField.init()
o.textField.setDisplay(o.showTextField)
o.textField.setHelpTxt(_lovTextFieldLab);
o.dateField.init()
o.dateField.setDisplay(o.showDate)
if(o.showDate) 
o.textField.setDisplay(false)
o.chunkLabel.init()
o.chunkLabel.setHTML(o.chunkText)
o.prevChunkIcn.init()
o.chunkCombo.init()
o.nextChunkIcn.init()
o.refreshIcn.init()
o.refreshIcn.setDisplay(o.showRefreshIcn)
o.lovList.init()
o.lovSearch.init()
o.showTextFieldOnly(o.showTextOnly)
o.resize(o.width,o.lines)
}
function LovWidget_resize(w,lines)
{
var o=this
var tmp= w - _searchIcnSize
var searchW= (tmp < _searchMinW)?_searchMinW:tmp
var newWidth= (tmp < _searchMinW)?((w+_searchMinW)-_margin):(w- _margin)
var textW= newWidth
var comboW= o.showRefreshIcn ? (newWidth - (2*_arrowSize+_refreshIcnSize)) : (newWidth - (2*_arrowSize))
if ( o.showTextField )
o.textField.resize(textW)
if ( o.showDate )
o.dateField.resize(newWidth)
o.chunkCombo.resize(comboW)
o.lovList.resize(newWidth)
o.lovList.setLines(lines);
o.resizeLovMessageZone();
o.lovSearch.resize(_searchIcnSize+searchW);
}
function LovWidget_addChunkElem(s,val,sel,id)
{
var o=this;
o.chunkCombo.add(s,val,sel,id);
}
function LovWidget_addLOVElem(s,val,sel,id)
{
var o=this;
o.lovList.add(s,val,sel,id);
}
function LovWidget_fillChunk(arrTxt,arrVal)
{
var o=this
o.chunkCombo.del()
o.setPrevNextIconState()
if ( arrTxt == null )
{
o.updateWidget()
return
}
if ( arrVal == null )
{
for (var i=0;i<arrTxt.length;i++)
o.chunkCombo.add(arrTxt[i],arrTxt[i],false,i)
}
else
{
for (var i=0;i<arrTxt.length;i++)
o.chunkCombo.add(arrTxt[i],arrVal[i],false,i)
}
o.chunkCombo.select(0)
o.setPrevNextIconState()
o.updateWidget()
}
function LovWidget_fillLOV(arrTxt,arrVal,arrTitle,sortType,sortColumnIndex)
{
var o=this;
o.lovList.freeze()
o.lovList.del();
o.lovList.resetColums();
if (arrTitle)
{
for (var l=0;l<arrTitle.length;l++)
o.lovList.addColumn(arrTitle[l],_LOV_MULTI_COLH,true,(l==sortColumnIndex?sortType:-1))
}
else
o.lovList.addColumn("",_LOV_MULTI_COLH,true,-1)
if ( arrTxt == null )
return;
if ( arrVal == null )
{
for (var i=0;i<arrTxt.length;i++)
o.lovList.add(arrTxt[i],arrTxt[i],false,i);
}
else
{
for (var i=0;i<arrTxt.length;i++)
o.lovList.add(arrTxt[i],arrVal[i],false,i);
}
o.lovList.rebuildHTML();
}
function LovWidget_getSearchValue()
{
var o=this;
return o.lovSearch.getSearchValue();
}
function LovWidget_setSearchValue(s)
{
var o=this;
o.lovSearch.setSearchValue(s);
}
function LovWidget_getTextValue()
{
var o=this;
return o.textField.getValue();
}
function LovWidget_setTextValue(s)
{
var o=this;
o.textField.setValue(s);
}
function LovWidget_getChunkSelection()
{
var o=this;
return o.chunkCombo.getSelection();
}
function LovWidget_getLOVSelection()
{
var o=this;
return o.lovList.getMultiSelection();
}
function LovWidget_getDateValue()
{
var o=this;
return o.dateField.getValue();
}
function LovWidget_setDateValue(s)
{
var o=this;
o.dateField.setValue(s);
}
function LovWidget_setTooltips(text,prev,next,refresh,search,chunk,lov)
{
var o=this;
if ( o.showTextField )
o.textField.setTooltip(text);
o.prevChunkIcn.setTooltip(prev);
o.nextChunkIcn.setTooltip(next);
if (o.showRefreshIcn)
o.refreshIcn.setTooltip(refresh);
o.lovSearch.searchIcn.setTooltip(search);
o.chunkCombo.setTooltip(chunk);
o.lovList.setTooltip(lov);
}
function LovWidget_goToChunk(step)
{
var o=this;
if ( o.chunkCombo.getSelection() == null )
return;
var curChunk = o.chunkCombo.getSelection().index;
if (step != null)
o.chunkCombo.select(curChunk+step);
o.setPrevNextIconState();
if (o.moveCB != null)
o.moveCB();
}
function LovWidget_setPrevNextIconState()
{
var o=this;
if(!o.chunkCombo.getSelection())
{
o.chunkCombo.setDisabled(true);
o.prevChunkIcn.setDisabled(true);
o.nextChunkIcn.setDisabled(true);
}
else
{
o.chunkCombo.setDisabled(false);
}
var curChunk = o.chunkCombo.getSelection().index;
if ( o.chunkCombo.getCount() == 1 )
{
o.prevChunkIcn.setDisabled(true);
o.nextChunkIcn.setDisabled(true);
return;
}
if ( curChunk == 0 )
{
o.prevChunkIcn.setDisabled(true);
o.nextChunkIcn.setDisabled(false);
return;
}
if ( (curChunk+1) == o.chunkCombo.getCount() )
{
o.prevChunkIcn.setDisabled(false);
o.nextChunkIcn.setDisabled(true);
return;
}
o.prevChunkIcn.setDisabled(false);
o.nextChunkIcn.setDisabled(false);
}
function LovWidget_change(label,w,lines,showText,showRefresh,moveCB,refreshCB,searchCB,
dblClickCB,multi,enterCB,showDate,showTextOnly)
{
var  o=this
if(showText!=null)
{
o.showTextField= showText;
o.textField.setDisplay(o.showTextField);
}
if(showRefresh!=null)
{
o.showRefreshIcn= showRefresh;
o.refreshIcn.setDisplay(o.showRefreshIcn);
o.updateWidget();
}
if(label!=null)
{
o.chunkText= label;
o.chunkLabel.setHTML(o.chunkText);
}
if(showDate!=null)
{
o.showDate= showDate;
o.dateField.setDisplay(o.showDate);
if(o.showDate)
o.textField.setDisplay(false);
}
if(moveCB!=null)
o.moveCB= moveCB;
if(refreshCB!=null)
o.refreshCB= refreshCB;
if(searchCB!=null)
o.searchCB= searchCB;
if(dblClickCB!=null)
o.dblClickCB= dblClickCB;
if(enterCB!=null)
o.enterCB= enterCB;
o.lovList.change(multi,lines)
o.showTextOnly= showTextOnly==null?false:showTextOnly;
o.showTextFieldOnly(o.showTextOnly)
if (o.showTextOnly) o.lovList.del() 
}
function LovWidget_isCaseSensitive()
{
return this.lovSearch.isCaseSensitive()
}
function LovWidget_setCaseSensitive(b)
{
var o=this
o.lovSearch.setCaseSensitive(b);
}
function LovWidget_prevCB()
{
this.par.goToChunk(-1);
}
function LovWidget_nextCB()
{
this.par.goToChunk(+1);
}
function LovWidget_comboCB()
{
this.par.goToChunk(null);
}
function LovWidget_refreshCB()
{
var p=this.par;
if (p.refreshCB != null) 
p.refreshCB();
}
function LovWidget_searchCB()
{
var p=this.par;
if (p.searchCB != null)
p.searchCB();
}
function LovWidget_enterCB(e)
{
var p=this.par;
if (p.enterCB != null)
p.enterCB(e);
}
function LovWidget_ListKeyUpCB(e)
{
var p=this.par;
var k=eventGetKey(e);
if ((eventGetKey(e) == 13)&& (p.enterCB != null))
{
p.enterCB(e);
eventCancelBubble(e);
}
}
function LovWidget_dblClickCB()
{
var p=this.par;
if (p.dblClickCB != null)
p.dblClickCB();
}
function LovWidget_clickCB()
{
}
function LovWidget_showTextFieldOnly(bVal)
{
var o=this;
o.showTextOnly=bVal
if(o.showTextOnly == true)
{
o.textField.setDisplay(!o.showDate);
o.dateField.setDisplay(o.showDate);
o.chunkLabel.setDisplay(false);
o.prevChunkIcn.setDisplay(false);
o.chunkCombo.setDisplay(false);
o.nextChunkIcn.setDisplay(false);
o.refreshIcn.setDisplay(false);
o.lovList.setDisplay(false);
o.lovSearch.setDisplay(false);
}
else
{
o.textField.setDisplay(o.showTextField);
o.dateField.setDisplay(o.showDate);
if(o.showDate) o.textField.setDisplay(false);
o.chunkLabel.setDisplay(true);
o.prevChunkIcn.setDisplay(true);
o.chunkCombo.setDisplay(true);
o.nextChunkIcn.setDisplay(true);
o.refreshIcn.setDisplay(o.showRefreshIcn);
o.lovList.setDisplay(true);
o.lovSearch.setDisplay(true);
o.updateWidget()
}
}
function LovWidget_setFormatDate(format,arrDays,arrMonth,AM,PM)
{
var o=this
o.dateField.setFormatInfo(format,arrDays,arrMonth,AM,PM);
}
function LovWidget_updateWidget()
{
var o=this
var b=(o.chunkCombo.getCount()>1)
o.chunkLabel.setDisplay(b)
o.prevChunkIcn.setDisplay(b)
o.chunkCombo.setDisplay(b)
o.nextChunkIcn.setDisplay(b)
if(!b && o.showRefreshIcn)
o.refreshIcn.changeText(_lovRefreshValuesLab);
else
o.refreshIcn.changeText("");
}
function LovWidget_showRefreshButton(b)
{
var o=this
o.refreshIcn.show(b);
}
function LovWidget_setLovMessage(m)
{
var o=this;
o.lovMessage=m;
getLayer(o.id+"_lovMessageText").innerHTML=m;
}
function LovWidget_showLovMessage(b)
{
var o=this;
var lovListContainer=getLayer(o.id+"_lovListContainer");
var lovMsgContainer=getLayer(o.id+"_lovMessageContainer");
lovMsgContainer.style.display=(b)?'':'none';
lovListContainer.style.display=(b)?'none':'';
}
function LovWidget_resizeLovMessageZone()
{
var o=this;
var css=o.lovList.css;
var lovMsgContainer=getLayer(o.id+"_lovMessageText");
lovMsgContainer.style.width=''+(o.lovList.w-(_dtd4?6:0))+'px';
lovMsgContainer.style.height=''+(o.lovList.h-(_dtd4?2:0))+'px';
}
function LovWidget_setLovSearchIconDisabled(b)
{
var o=this;
o.lovSearch.setSearchIconDisabled(((b)?true:false));
}
function LovWidget_showDelegateSearch(b)
{
var o=this;
o.showLovMessage(b);
o.setCaseSensitive(b);
o.lovSearch.normal.setDisabled(b)
o.showRefreshButton(!b);
}
function LovWidget_showPartialResult(b)
{
var o=this;
var lovPartialResult=getLayer(o.id+"_lovPartialResult");
lovPartialResult.style.display=(b)?'':'none';
}
