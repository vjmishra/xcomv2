function newMultiColumnList(id,changeCB,multi,w,h,dblClickCB,sortCB,noHeader,keyDownCB)
{
var o=newWidget(id);
o.changeCB=changeCB;
o.multi=(multi==null?false:multi);
o.w=(w==null?100:w);
o.h=(h==null?400:h);
o.dblClickCB=dblClickCB;
o.sortCB=sortCB;
o.noHeader=(noHeader==null?false:noHeader)
o.keyDownCB=keyDownCB
o.borderW=1
o.bgClass='solidBorder'
o.innerW=Math.max(0,o.w-(2*(o.borderW)))
o.innerH=Math.max(0,o.h-(2*(o.borderW)))
o.getHTML=MultiColumnList_getHTML;
o.oldInit=o.init;
o.init=MultiColumnList_init;
o.rebuildHTML=MultiColumnList_rebuildHTML
o.oldResize=o.resize
o.resize=MultiColumnList_resize
o.resetColums=MultiColumnList_resetColums;
o.addColumn=MultiColumnList_addColum;
o.addRow=MultiColumnList_addRow;
o.add=MultiColumnList_add;
o.del=MultiColumnList_del
o.move=MultiColumnList_move
o.getColumsCount=MultiColumnList_getColumsCount
o.getCount=MultiColumnList_getCount
o.freeze=MultiColumnList_freeze
o.change=MultiColumnList_change
o.setMulti=MultiColumnList_setMulti
o.getSelection=MultiColumnList_getSelection
o.getMultiSelection=MultiColumnList_getMultiSelection
o.select=MultiColumnList_select
o.valueSelect=MultiColumnList_valueSelect
o.getValue=MultiColumnList_getValue
o.setLines=MultiColumnList_setLines
o.getLines=MultiColumnList_getLines
o.getRow=MultiColumnList_getRow
o.findByValue=MultiColumnList_findByValue
o.headH=o.noHeader?0:19;
o.chunkSize=50;
o.cols=new Array;
o.chunks=new Array;
o.chunks[0]=new Array
o.minWidth=36
o.defaultWidth=150
o.listBody=newScrolledZoneWidget("mlst_body"+o.id,0,0,o.innerW,o.innerH-o.headH);
o.header=null
o.body=null
o.selectionIndex=-1
o.oldSelectIdx=-1
o.frozen=true
o.fillBody=MultiColumnList_fillBody
o.fillHeader=MultiColumnList_fillHeader
o.fillChunk=MultiColumnList_fillChunk
o.generateRow=MultiColumnList_generateRow
o.getRowHTML=MultiColumnList_getRowHTML
o.invert=MultiColumnList_invert
o.setRowFocus=MultiColumnList_setRowFocus
o.setItemVisible=MultiColumnList_setItemVisible
o.internalSelect=MultiColumnList_internalSelect
o.internalUnselectAll=MultiColumnList_internalUnselectAll
o.internalSelectRange=MultiColumnList_internalSelectRange
o.getTR=MultiColumnList_getTR
o.getIndexFromY=MultiColumnList_getIndexFromY
o.getYPosFromEvent=MultiColumnList_getYPosFromEvent
o.getVisibleItemCount=MultiColumnList_getVisibleItemCount
o.rewriteLine=MultiColumnList_rewriteLine
return o
}
function MultiColumnList_getHTML()
{
var o=this,s=new Array,i=0;
if (o.cols.length==0)
{
o.resetColums()
o.addColumn("",50,false,0)
}
var w=""+(_dtd4?o.innerW:o.w)+"px"
var h=""+(_dtd4?o.innerH:o.h)+"px"
s[i++]='<div align="left" onselectstart="return false" class="' + o.bgClass + '" id="'+o.id+'" style="border-width:'+o.borderW+'px;padding:0px;'+sty("width",w)+sty("height",h)+'">'
s[i++]='<div id="mlst_header'+o.id+'" style="'+backImgOffset(_skin+'multih.gif',0,0)+sty("width",""+o.innerW+"px")+'overflow:hidden;height:'+o.headH+'px">'
o.fillHeader(s)
i=s.length
s[i++]='</div>'
s[i++]=o.listBody.beginHTML()
o.fillBody(s)
i=s.length
s[i++]=o.listBody.endHTML()
s[i++]='</div>'
return s.join("")
}
function MultiColumnList_rebuildHTML()
{
var o=this
if (o.layer)
{
if (o.cols.length==0)
{
o.resetColums()
o.addColumn("",50,false,0)
}
var s=new Array
o.fillHeader(s)
o.header.innerHTML=s.join('')
s.length=0
o.fillBody(s)
o.body.innerHTML=s.join('')
o.header.scrollLeft=0
o.body.scrollLeft=0
o.layer=null
o.init()
}
}
function MultiColumnList_resize(w,h)
{
var o=this
if (w!=null) o.w=w
if (h!=null) o.h=h
o.innerW=Math.max(0,o.w-(2*(o.borderW)))
o.innerH=Math.max(0,o.h-(2*(o.borderW)))
if (_dtd4)
o.oldResize(o.innerW,o.innerH)
else
o.oldResize(o.w,o.h)
o.listBody.resize(o.innerW, Math.max(0,o.innerH-o.headH))
if (o.layer)
{
o.header.style.width=""+o.innerW+"px"
o.header.style.height=""+o.headH+"px"
}
}
function MultiColumnList_init()
{
var o=this
o.oldInit()
o.listBody.init()
o.listBodyInner=o.listBody.layer.childNodes[0]
if (_ie)
{
o.listBody.layer.onscroll=MultiColumnList_ScrollTitle
}
else
{
o.scrollLeft=o.listBody.layer.scrollLeft
if (o.scrollTitleInterval!=null)
clearInterval(o.scrollTitleInterval)
o.scrollTitleInterval=setInterval('MultiColumnList_CheckScrollTitle('+o.widx+')',20)
}
var inner=o.listBodyInner
inner.onmousedown=MultiColumnList_mouseDown
inner.onkeydown=MultiColumnList_onkeydown
addDblClickCB(inner,MultiColumnList_ondblclick)
o.header=getLayer('mlst_header'+o.id)
o.body=o.listBody.layer
o.oldFocus=null
o.frozen=false
}
function MultiColumnList_resetColums()
{
var o=this
o.cols.length=0
o.chunks[0].length=0
o.oldSelectIdx=o.selectionIndex=-1
}
function MultiColumnList_addColum(name,width,sortable,sortIcn,tooltip)
{
var o=this,cols=o.cols,c=cols[cols.length]=new Object;
c.name=name
c.width=Math.max(width?width:o.defaultWidth,o.minWidth)
c.sortable=(sortable==null?false:sortable)
c.sortIcn=(sortIcn==null?-1:sortIcn)
c.tooltip=tooltip
}
function MultiColumnList_addRow(value,sel)
{
var o=this
if (o.cols.length==0)
{
o.resetColums()
o.addColumn("",50,false,0)
}
if (o.chunks[0]==null)
o.chunks[0]=new Array
var chunk=o.chunks[0]
var c=chunk[chunk.length]=new Object
var cols=c.cols=new Array
c.value=value
c.sel=sel
var args=MultiColumnList_addRow.arguments,argc=args.length
if (argc==3&&!(typeof(args[2])=="string"))
{
var arr=args[2]
for (var l in arr)
cols[cols.length]=arr[l]
}
else
{
for (var l=2;l<argc;l++)
cols[cols.length]=args[l]
}
if ((!o.frozen)&&o.layer)
{
var table=o.body.childNodes[0].childNodes[0],trParent=table.childNodes[0],tr=trParent.insertRow(-1)
tr.className=(c.sel?"mclS":"mcl")
o.generateRow(c,tr)
}
}
function MultiColumnList_add(s,value,sel)
{
this.addRow(value,sel,s)
}
function MultiColumnList_del(i)
{
var o=this,chunk=o.chunks[0],mustUpdate=false
if (i>=0&&i<chunk.length)
{
arrayRemove(o.chunks,0,i)
if ((!o.frozen)&&(o.layer))
o.body.childNodes[0].childNodes[0].childNodes[0].deleteRow(i)
}
else if (i==null)
{
o.chunks[0].length=0
if ((!o.frozen)&&(o.layer))
{
var s=new Array
o.fillBody(s)
o.body.innerHTML=s.join('')
o.init()
}
}
}
function MultiColumnList_move(delta)
{
  var o=this,i=o.selectionIndex,len=o.getCount()-1,newI=i+delta
  if ((i==-1)||(newI<0)||(newI>len))
      return false
var chunk=o.chunks[0],temp=chunk[i]
o.internalSelect()
chunk[i]=chunk[newI]
chunk[newI]=temp
o.rewriteLine(i)
o.rewriteLine(newI)
o.internalSelect(newI)
}
function MultiColumnList_rewriteLine(i)
{
var o=this,cols=o.chunks[0][i].cols,colsH=cols.length,tr=o.body.childNodes[0].childNodes[0].childNodes[0].childNodes[i]
for (var j=0;j<colsH;j++)
{
tr.childNodes[j].childNodes[0].innerHTML=convStr(cols[j])
}
}
function MultiColumnList_getColumsCount()
{
return this.cols.length
}
function MultiColumnList_getCount()
{
return (this.chunks[0]==null ? 0 : this.chunks[0].length)
}
function MultiColumnList_freeze()
{
if (this.layer)
this.frozen=true
}
function MultiColumnList_change(multi,lines)
{
var o=this
o.setMulti(multi)
if (lines!=null)
o.setLines(lines)
}
function MultiColumnList_setMulti(multi)
{
var o=this
if (multi!=o.multi)
{
if (!multi)
{
var sel=o.getSelection()
if (sel)
{
var idx=sel.index
o.select(null)
o.select(idx)
}
}
o.multi=multi
}
}
function MultiColumnList_getSelection()
{
var o=this,chunk=o.chunks[0]
if (chunk)
{
var idx=o.selectionIndex
if (idx>=0)
{
var row=chunk[idx]
if (row.sel)
{
var cols=row.cols,s=cols.length>1?cols:cols[0]
return { value:row.value, text:s, index: idx }
}
}
}
return null
}
function MultiColumnList_getMultiSelection()
{
var o=this,chunk=o.chunks[0],rets=new Array,len=chunk.length
for (var i=0;i<len;i++)
{
var row=chunk[i]
if (row.sel)
{
var cols=row.cols,ret=new Object;
ret.index=i;
ret.value=row.value;
ret.text=cols.length>1?cols:cols[0];
rets[rets.length]=ret
}
}
return rets
}
function MultiColumnList_select(i)
{
var o=this,len=o.getCount()
if (i==null)
o.internalSelect(null)
else
{
if (typeof(i)=="object")
{
o.internalUnselectAll(true)
var len=i.length
if (len > 0)
{
if (o.multi)
{
for (var j=0;j<len;j++)
o.internalSelect(i[j],true)
}
else
{
o.internalSelect(i[len-1])
}
}
}
else
{
if (i<0||i>=len)
o.internalSelect(len-1)
else
o.internalSelect(i)
}
}
}
function MultiColumnList_valueSelect(v)
{
var o=this,chunk=o.chunks[0],len=chunk.length
if (typeof(v)=="object")
{
var vlen=v.length
o.internalUnselectAll(true)
if (vlen>0)
{
for (var i=0;i<len;i++)
{
var chunkVal=chunk[i].value
if (o.multi)
{
for (var j=0;j<vlen;j++)
{
if (chunkVal==v[j])
{
o.internalSelect(i,true)
break
}
}
}
else
{
if (chunkVal==v[vlen-1])
{
o.select(i)
break
}
}
}
}
}
else
{
for (var i=0;i<len;i++)
{
if (chunk[i].value==v)
{
o.select(i)
break
}
}
}
}
function MultiColumnList_getValue(i)
{
var o=this,len=o.getCount()
if(i==null || i<0 || i>len)
return null;
else
return o.chunks[0][i].value
}
function MultiColumnList_setLines(lines)
{
var o=this
o.h=Math.max(0,(lines*14)+2*(o.borderW)) + o.headH
o.resize(null,o.h)
}
function MultiColumnList_getLines()
{
var o=this
return Math.floor(((o.h-o.headH)-(2*o.borderW))/14)
}
function MultiColumnList_getRow(i)
{
return this.chunks[0][i]
}
function MultiColumnList_findByValue(val)
{
var o=this,chunk=o.chunks[0],len=chunk.length
for (var i=0;i<len;i++)
{
if (chunk[i].value==val)
{
var ret=new Object;
ret.index=i;
ret.value=val;
return ret
}
}
return null
}
function MultiColumnList_getVisibleItemCount()
{
var o=this
if (o.layer)
{
var tr=o.getTR()
if (tr)
{
return Math.floor(o.body.offsetHeight/tr.offsetHeight)
}
}
return 0
}
function MultiColumnList_getYPosFromEvent(e)
{
return ypos(this.listBody.layer,e)
}
function MultiColumnList_getIndexFromY(y,isSafe)
{
var o=this,tr=o.getTR()
if (tr)
{
var trH=tr.childNodes[0].offsetHeight,idx=Math.floor(y/trH)
if (idx<0)
{
if (isSafe)
return 0
else
return -1
}
else if (idx>=o.chunks[0].length)
{
if (isSafe)
return o.chunks[0].length-1
else
return -1
}
else
return idx
}
return -1
}
function MultiColumnList_getTR(idx)
{
var o=this,lyr=o.listBody.layer
var tr=lyr?lyr.childNodes[0]:null
while (tr)
{
if (tr.tagName.toLowerCase()=='tr')
break
if (tr.hasChildNodes())
{
tr=tr.childNodes[0]
}
else
{
tr=null
break
}
}
if (idx)
return tr?tr.parentNode.childNodes[idx]:null
else
return tr
}
function MultiColumnList_internalSelect(idx,ctrl,shift)
{
var o=this
ctrl=(ctrl!=null?ctrl:false)
shift=(shift!=null?shift:false)
if (idx==null)
{
if (o.multi)
{
o.internalUnselectAll(true)
}
else
{
o.invert(o.oldSelectIdx)
o.oldSelectIdx=-1
o.selectionIndex=-1
}
}
else if (idx>=0)
{
if (o.multi)
{
if (ctrl)
{
var row=o.chunks[0][idx]
if (row)
o.invert(idx,!row.sel)
o.setRowFocus(idx)
}
else if (shift)
{
o.internalSelectRange(o.oldSelectIdx,idx)
o.setRowFocus(idx)
}
else
{
o.internalSelectRange(idx,idx)
o.setRowFocus(idx)
}
if (!shift)
{
o.oldSelectIdx=idx
}
o.selectionIndex=idx
}
else
{
o.invert(o.oldSelectIdx,false)
o.invert(idx,true)
o.setRowFocus(idx)
o.oldSelectIdx=idx
o.selectionIndex=idx
}
}
}
function MultiColumnList_internalUnselectAll()
{
var o=this,chunk=o.chunks[0],len=chunk.length
for (var i=0;i<len;i++)
{
o.invert(i,false)
}
}
function MultiColumnList_internalSelectRange(idx1,idx2)
{
var o=this,chunk=o.chunks[0],len=chunk.length,idxMin=-1,idxMax=-1
if (idx1>idx2)
{
idxMin=idx2
idxMax=idx1
}
else
{
idxMin=idx1
idxMax=idx2
}
for (var i=0;i<len;i++)
{
var select=(i>=idxMin)&&(i<=idxMax)
o.invert(i,select)
}
}
function MultiColumnList_invert(idx,select,setFocus)
{
if (idx==null||idx==-1)
return
var o=this,chunk=o.chunks[0]
if (chunk)
{
var col=chunk[idx],lenH=o.cols.length
if (col.sel!=select)
{
col.sel=select;
var tr=o.getTR(idx)
if (tr)
{
tr.className=(select?"mclS":"mcl")
}
}
}
}
function MultiColumnList_setRowFocus(idx)
{
if (idx==null||idx==-1)
return
var o=this,chunk=o.chunks[0]
if (idx>=chunk.length)
return
var tr=o.getTR(idx)
if (o.oldFocus)
o.oldFocus.tabIndex=-1
tr.tabIndex=0
o.oldFocus=tr
if (_webKit)
o.setItemVisible(idx)
safeSetFocus(tr)
}
function MultiColumnList_setItemVisible(idx)
{
var o=this,tr=o.getTR()
if (tr)
{
var lyr=o.listBody.layer
if (lyr)
{
var h=lyr.offsetHeight
var trH=tr.childNodes[0].offsetHeight
x1=trH*idx
x2=x1+trH
if (x1<lyr.scrollTop)
lyr.scrollTop=x1
else if (x2>(lyr.scrollTop+h))
lyr.scrollTop=x2-h
}
}
}
function MultiColumnList_CheckScrollTitle(widx)
{
var o=_widgets[widx]
if (o&&(o.cols.length>1))
{
var lyr=o.listBody.layer
if (lyr)
{
var scrollLeft=lyr.scrollLeft
if (o.scrollLeft!=scrollLeft)
{
o.scrollLeft=scrollLeft
MultiColumnList_ScrollTitle(lyr)
}
}
}
}
function MultiColumnList_ScrollTitle(lyr)
{
if (_ie)
lyr=this
var o=getWidget(lyr.parentNode)
if (o.cols.length>1)
o.header.scrollLeft=o.listBody.layer.scrollLeft
}
function MultiColumnList_getVisibleChunks()
{
var o=this
if (o.chunks.length==0)
return null;
if (o.layer)
{
}
else
{
return {start:0,end:0}
}
}
function mltSrtCB(lyr,col)
{
var o=getWidget(lyr)
if (o.sortCB)
{
o.sortCB(col)
}
}
function MultiColumnList_fillBody(s)
{
var o=this,len=o.chunks.length
for (var k=0; k<len;k++)
{
o.fillChunk(s,k)
}
}
function MultiColumnList_fillChunk(s,idx)
{
var o=this,i=s.length,tableW=0,colsH=o.cols,lenH=colsH.length,lastH=lenH-1
for (var j=0; j<lenH;j++)
{
tableW+=colsH[j].width
}
var chunk=o.chunks[idx]
if (chunk)
{
var len=chunk.length
s[i++]='<div style="width:'+(lenH>1?''+tableW+'px':'100%')+'">'
s[i++]='<table height="'+o.headH+'" '+ (lenH>1 ? 'width="'+tableW+'" ' : 'width="100%"') +'cellpadding="0" cellspacing="0" border="0" style="color:black"><tbody>'
var tabIndexNotWritten=true
for (var j=0;j<len;j++)
{
var row=chunk[j]
if (row.sel&&tabIndexNotWritten)
{
s[i++]='<tr tabindex="0" class="'+(row.sel?"mclS":"mcl")+'">'
tabIndexNotWritten=true
}
else
{
s[i++]='<tr>'
}
o.getRowHTML(row,s)
i=s.length
s[i++]='</tr>'
}
s[i++]='</tbody></table></div>'
}
}
function MultiColumnList_getRowHTML(row,s)
{
var o=this,colsH=o.cols,lenH=colsH.length,cols=row.cols,i=s.length
for (var k=0;k<lenH;k++)
{
if (lenH>1)
{
s[i++]='<td><div class="mclM" style="width:'+(colsH[k].width-(_dtd4?4:0))+'px">'
s[i++]=convStr(cols[k])+'</div></td>'
}
else
{
s[i++]='<td><div class="mclC">'+convStr(cols[k])+'</div></td>'
}
}
}
function MultiColumnList_generateRow(row,tr)
{
var o=this,colsH=o.cols,lenH=colsH.length,cols=row.cols
for (var k=0;k<lenH;k++)
{
var td=tr.insertCell(-1)
if (lenH>1)
{
td.innerHTML='<div class="mclM" style="width:'+(colsH[k].width-(_dtd4?4:0))+'px">'+convStr(cols[k])+'</div>'
}
else
{
td.innerHTML='<div class="mclC">'+convStr(cols[k])+'</div></td>'
}
}
}
function MultiColumnList_fillHeader(s)
{
var o=this,i=s.length,cols=o.cols,len=cols.length,tableW=0;
if (len==1)
{
tableW="100%"
}
else
{
for (var j=0; j<len;j++)
{
tableW+=cols[j].width
}
tableW+=300
}
s[i++]='<table '+(o.noHeader?'style="display:none" ':'')+'onmousedown="return '+(_webKit?"true":"false")+'" height="'+o.headH+'" width="'+tableW+'" cellpadding="0" cellspacing="0" border="0"><tbody><tr valign="middle" align="left">'
for (var j=0; j<len;j++)
{
var c=cols[j],tW=0
if (len==1)
tW=(o.w-(_dtd4?10:6))
else
tW=(c.width-(_dtd4?10:6))
if (c.sortIcn>=0)
tW-=15
var tip=""
if (c.tooltip)
tip=' title="'+convStr(c.tooltip)+'"'
s[i++]='<td'+ tip + (c.sortable?' onclick="'+_codeWinName+'.mltSrtCB(this,'+j+')"':'') +'><div class="mclH" style="'+(c.sortable?'':'cursor:default;')+'width:'+tW+'px">'+convStr(c.name)+'</div></td>'+
(c.sortIcn>=0?('<td onclick="'+_codeWinName+'.mltSrtCB(this,'+j+')" width="15" style="cursor:'+_hand+'">'+simpleImgOffset(_skin+'multis.gif',15,10,0,c.sortIcn*10)+'</td>'):'<td width="0"></td>')
if (len>1)
{
s[i++]='<td align="right" valign="middle" onmousedown="'+_codeWinName+'.MultiColumnList_titleMouseDown(this,event,'+j+');return '+(_webKit?"true":"false")+'" style="cursor:'+_resizeW+'" width="6">'+simpleImgOffset(_skin+"iconsep.gif",2,o.headH-7,2,3,' id="'+this.id+'" ')+'</td>'
}
}
if (len>1)
s[i++]='<td width="300"></td>'
s[i++]='</tr></tbody></table>'
}
function MultiColumnList_ondblclick(e)
{
if (_ie)
e=_curWin.event
var o=getWidget(this.parentNode.parentNode),idx=o.getIndexFromY(o.getYPosFromEvent(e),false)
if ((idx>=0)&&(o.dblClickCB))
o.dblClickCB()
return false
}
function MultiColumnList_onkeydown(e)
{
if (_ie)
e=_curWin.event
var k=eventGetKey(e),o=getWidget(this.parentNode.parentNode),i=o.selectionIndex
switch(k)
{
case 40: 
i++
break
case 36: 
i=0
break
case 38: 
i--
break
case 35: 
i=o.getCount()-1
break
case 34: 
i+=o.getVisibleItemCount()
break;
case 33: 
i-=o.getVisibleItemCount()
break;
case 46: 
return false
default:
if (o.keyDownCB)
return o.keyDownCB()
else
return true
}
i=Math.max(0,i)
i=Math.min(o.getCount()-1,i)
o.internalSelect(i,eventIsCtrl(e),e.shiftKey)
if (o.changeCB)
o.changeCB()
return false
}
function MultiColumnList_createCaptureLayer(idx,x,y,w,h)
{
var l=null
eval ("l=_curWin._captureMouseLayer"+idx+";")
if (l==null)
{
l=_curDoc.createElement("div")
eval("_curWin._captureMouseLayer"+idx+"=l;")
}
var st=l.style
st.position="absolute"
st.top=""+y+"px"
st.left=""+x+"px"
st.width=""+w+"px"
st.height=""+h+"px"
st.zIndex=10000
_curDoc.body.appendChild(l)
l.addEventListener("mousemove", MultiColumnList_mouseMove, true);
l.addEventListener("mouseup", MultiColumnList_mouseUp, true);
}
function MultiColumnList_releaseCaptureLayer(idx)
{
var l=null
eval ("l=_curWin._captureMouseLayer"+idx+";")
l.removeEventListener("mousemove", MultiColumnList_mouseMove, true);
l.removeEventListener("mouseup", MultiColumnList_mouseUp, true);
_curDoc.body.removeChild(l)
}
function MultiColumnList_mouseDown(e)
{
if (_ie)
e=_curWin.event
setTimeout("_curWin.focus()",1)
var o=getWidget(this.parentNode.parentNode)
if (o.timer!=null)
{
clearInterval(o.timer)
o.timer=null
}
if (e.button==_leftBtn)
{
var idx=o.getIndexFromY(o.getYPosFromEvent(e),false)
if (idx>=0)
{
var body=_curDoc.body
o.currentY=o.initialY=eventGetY(e)
if (_webKit)
{
var pos=getPos(o.layer)
var ww=winWidth(),wh=winHeight()
var dx=_curDoc.body.scrollLeft,dy=_curDoc.body.scrollTop
var x=eventGetX(e)-2+dx
var y=eventGetY(e)-2+dy
var w=4
var h=4
MultiColumnList_createCaptureLayer(0,dx,dy,Math.max(0,x-dx),wh)
MultiColumnList_createCaptureLayer(1,dx,dy,ww,Math.max(0,y-dy))
MultiColumnList_createCaptureLayer(2,x+w,dy,Math.max(0,ww-x-w+dx),wh)
MultiColumnList_createCaptureLayer(3,dx,y+h,ww,Math.max(0,wh-y-h+dy))
}
if (body.addEventListener)
{
body.addEventListener("mousemove", MultiColumnList_mouseMove, true);
body.addEventListener("mouseup", MultiColumnList_mouseUp, true);
}
else
{
o.oldMouseMove=body.onmousemove
o.oldMouseUp=body.onmouseup
body.onmousemove=MultiColumnList_mouseMove
body.onmouseup=MultiColumnList_mouseUp
if (body.setCapture)
body.setCapture(true)
}
_curWin.__curDraggingMultiList=o
o.internalSelect(idx,eventIsCtrl(e),e.shiftKey)
}
}
return _webKit?true:false;
}
function MultiColumnList_mouseMove(e)
{
if (_ie)
e=_curWin.event
if (eventIsCtrl(e))
return
if (e.button==_leftBtn)
{
var o=_curWin.__curDraggingMultiList
if (o)
{
o.currentY=eventGetY(e)
o.clientY=eventGetY(e)
var y=o.getYPosFromEvent(e)
var relY=ypos(o.layer,e)-o.headH
if ((relY>=0)&&(relY<=o.listBody.getHeight()))
{
o.internalSelect(o.getIndexFromY(y,true),false,true)
if (o.timer!=null)
{
clearInterval(o.timer)
o.timer=null
}
}
else
{
if (o.timer==null)
{
o.timer=setInterval("MultiColumnList_mouseMoveTimer()",50)
}
}
}
}
else
MultiColumnList_mouseUp(e)
return _webKit?true:false
}
function MultiColumnList_mouseMoveTimer()
{
var o=_curWin.__curDraggingMultiList
if (o)
{
o.internalSelect(o.getIndexFromY(o.clientY-getPosScrolled(o.listBody.layer).y+getScrollY(),true),false,true)
}
}
function MultiColumnList_mouseUp(e)
{
var o=_curWin.__curDraggingMultiList
if (o)
{
if (_ie)
e=_curWin.event
setTimeout('delayedMultiColumnList_mouseUp('+o.getYPosFromEvent(e)+')',1)
}
}
function delayedMultiColumnList_mouseUp(y)
{
var o=_curWin.__curDraggingMultiList
if (o)
{
if (o.timer!=null)
{
clearInterval(o.timer)
o.timer=null
}
var body=_curDoc.body
if (_webKit)
{
for (var i=0;i<4;i++)
MultiColumnList_releaseCaptureLayer(i)
}
if (body.removeEventListener)
{
body.removeEventListener("mousemove", MultiColumnList_mouseMove, true);
body.removeEventListener("mouseup", MultiColumnList_mouseUp, true);
}
else
{
body.onmouseup=o.oldMouseUp
body.onmousemove=o.oldMouseMove
if (body.releaseCapture)
body.releaseCapture()
}
_curWin.__curDraggingMultiList=null
if (o.changeCB)
o.changeCB()
o.setRowFocus(o.getIndexFromY(y,true))
}
return _webKit?true:false
}
function MultiColumnList_titleMouseDown(lyr,e,colIdx)
{
if (_ie)
e=_curWin.event
var o=getWidget(lyr.parentNode.parentNode)
var body=_curDoc.body
setTimeout("_curWin.focus()",1)
_curWin.__curDraggingMultiList=o
_curWin.__curDraggingColIdx=colIdx
_curWin.__curDraggingInitWidth=o.cols[colIdx].width
if (body.addEventListener)
{
body.addEventListener("mousemove", MultiColumnList_titleMouseMove, true);
body.addEventListener("mouseup", MultiColumnList_titleMouseUp, true);
}
else
{
o.oldMouseMove=body.onmousemove
o.oldMouseUp=body.onmouseup
body.onmousemove=MultiColumnList_titleMouseMove
body.onmouseup=MultiColumnList_titleMouseUp
if (body.setCapture)
body.setCapture(true)
}
o.initialX=eventGetX(e)
return _webKit?true:false
}
function MultiColumnList_titleMouseMove(e)
{
if (_ie)
e=_curWin.event
if (e.button==_leftBtn)
{
var o=_curWin.__curDraggingMultiList,colIdx=_curWin.__curDraggingColIdx
var c=o.cols[colIdx]
var newWidth=_curWin.__curDraggingInitWidth-o.initialX+eventGetX(e)
c.width=Math.max(newWidth,o.minWidth)
var tW=(c.width-(_dtd4?10:6))
if (c.sortIcn>=0)
tW-=15
var tableW=0,colsH=o.cols,lenH=colsH.length
for (var j=0; j<lenH;j++)
{
tableW+=colsH[j].width
}
tableW+=300
o.header.childNodes[0].style.width=""+tableW+"px"
var td=o.header.childNodes[0].childNodes[0].childNodes[0].childNodes[colIdx*3]
td.childNodes[0].style.width=""+tW+"px"
}
else
MultiColumnList_titleMouseUp(e)
}
function MultiColumnList_titleMouseUp()
{
setTimeout("delayedMultiColumnList_titleMouseUp()",1)
return _webKit?true:false
}
function delayedMultiColumnList_titleMouseUp()
{
var o=_curWin.__curDraggingMultiList,colIdx=_curWin.__curDraggingColIdx,body=_curDoc.body
var table=o.body.childNodes[0].childNodes[0]
var allTr=table.childNodes[0].childNodes,allTrLen=allTr.length
var w=o.cols[colIdx].width,innerW=_dtd4?w-4:w
var innerSty=""+innerW+"px"
var tableW=0,colsH=o.cols,lenH=colsH.length
for (var j=0; j<lenH;j++)
{
tableW+=colsH[j].width
}
table.parentNode.style.width=""+tableW+"px"
table.style.width=""+tableW+"px"
for (var i=0;i<allTrLen;i++)
{
var td=allTr[i].childNodes[colIdx]
td.childNodes[0].style.width=innerSty
}
o.header.scrollLeft=o.listBody.layer.scrollLeft
if (body.removeEventListener)
{
body.removeEventListener("mousemove", MultiColumnList_titleMouseMove, true);
body.removeEventListener("mouseup", MultiColumnList_titleMouseUp, true);
}
else
{
body.onmouseup=o.oldMouseUp
body.onmousemove=o.oldMouseMove
if (body.releaseCapture)
body.releaseCapture()
}
}
