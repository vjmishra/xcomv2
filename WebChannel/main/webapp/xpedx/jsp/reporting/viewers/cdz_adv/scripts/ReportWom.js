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
*/function ac(parent,className,bid)
{
    var ch=parent.children,o=ch[ch.length]=new Object
    o.className=className
    o.bid=bid
    o.parent=parent
    o.children=new Array
    return o
}
function addGenericChild(parent,className,id)
{
    var ch=parent.children,o=ch[ch.length]=new Object
    o.className=className
    o.id=id
    o.parent=parent
    o.children=new Array
    return o
}
function ax(block, index)
{
    if (block.axis==null)
        block.axis = new Array(new Object,new Object,new Object,new Object,new Object,new Object)
    var o=block.axis[index],args=ax.arguments,len=args.length
    o.vars=new Array(len-2)
    for (var i=2; i < len; i++)
o.vars[i-2]=args[i]
}
function axn(block, index, arrNames)
{
if (arrNames&&(arrNames.length>0))
{
var o=block.axis[index]
o.names=arrNames
}
}
function crdr(block, index, crdr)
{
if (crdr&&(crdr.length>0))
{
var o=block.axis[index]
o.crdr=crdr
}
}
function srt(block, index, s)
{
    if (s != "")
    {
        var o=block.axis[index]
        var splitted=s.split(","),count=splitted.length
        o.sorts=new Array
        var j=0
        for (var i=0;i<count;i+=2)
        {
            var srt=o.sorts[j++]=new Object
            srt.id=splitted[i]
            srt.kind=parseInt(splitted[i+1])
        }
    }
}
function fm(o,formula,qualification,dType,type,definition,isAggregate,nDType,linkedDim,aggregateFct)
{
if (formula!=null) o.formula=formula
if (qualification!=null) o.qualification=qualification
if (dType!=null) o.dType=dType
if (type!=null) o.type=type
if (definition!=null) o.definition=definition
if (isAggregate!=null) o.isAggregate=isAggregate
if (nDType!=null) o.nDType=nDType
if (linkedDim!=null) o.linkedDim=linkedDim
if (aggregateFct!=null) o.aggregateFct=aggregateFct
}
function brk(block, index, s)
{
    if (s != "")
    {
        var o=block.axis[index]
        var splitted=s.split(","),count=splitted.length
        o.brks=new Array
        var j=0
        for (var i=0;i<count;i++)
        {
            var brk=o.brks[j++]=new Object
            brk.id=splitted[i]
        }
    }
}
function fnt(o,name,size,bold,italic,under,strike)
{
    var f=o.font=new Object
    f.name=name
    f.size=size
    f.bold=bold
    f.italic=italic
    f.under=under
    f.strike=strike    
}
function cl(r,g,b)
{
   var o=new Object
   o.rgb=""+r+","+g+","+b
   return o
}
function align(o,h,v,wrap)
{
    var a=o.align=new Object
    a.h=h
    a.v=v
    a.wrap=wrap
}
function pad(o,h,v)
{
    var p=o.pad=new Object
    p.h=h   
    p.v=v    
}
function bd(o,sz, col)
{
    var b=o.border=new Object
    b.size=sz
    b.color=col
}
function bs(o, tsz, tcol, bsz, bcol, lsz, lcol, rsz, rcol)
{
    var b=o.borders=new Object
    b.topSize=tsz
    b.topColor=tcol
    b.bottomSize=bsz
    b.bottomColor=bcol
    b.leftSize=lsz
    b.leftColor=lcol
    b.rightSize=rsz
    b.rightColor=rcol
}
function attach(o, toH, h, toV, v)
{
    var a=o.attach=new Object
    a.toH=toH
    a.h=h
    a.toV=toV
    a.v=v
}
function block(o, nm, bt, ndd, nmes, dr, snp, roep, apb, u, ph, pv, se)
{
    var b=o.block=new Object
b.name=nm
b.blockType=bt
b.nDimDetails=ndd
b.nMeasures=nmes
b.dupRow=dr?dr:null
b.startNewPage=snp?snp:null
b.repeatOnEveryPage=roep?roep:null
b.avoidPageBreak=apb?apb:null
b.unitIsInch=u?u:null
b.posH=ph?ph:null
b.posV=pv?pv:null
    b.showWhenEmpty=se?se:null
}
function graph(o, unit, w, h)
{
var g=o.graph=new Object;
g.unitIsInch=unit;
g.width=w;
g.height=h;
}
function cell(o, r, u, ph, pv, pb)
{
var c=o.cell=new Object
c.repeat=r
    c.unitIsInch=u
    c.posH=ph
    c.posV=pv    
    c.pageBreak=pb
}
function fcell(o, fo)
{
var fc=o.fcell=new Object
fc.formula=fo    
}
function cs(o, u, sw, sh, w, h, ph, pv)
{
var s=o.cellsize=new Object
    s.unitIsInch=u
    s.specW=sw
    s.specH=sh
    s.width=w
    s.height=h
}
function fReport(o, nm, sh, uh, hh, hc, sf, uf, fh, fc,  bgc, sk, iu, dm, ha, va, hlc, hlvc, ps, po, pu, ph, pw, um, mt, ml, mb, mr, pm, rm, nbp, sp, il, embimg, hdsk, hdiu, hdembimg, hddm, hdha, hdva, ftsk, ftiu, ftembimg, ftdm, ftha, ftva)
{
var r=o.report=new Object
r.name=nm
r.showHeader=sh
r.headerUnitIsInch=uh
r.headerHeight=hh
r.headerBGColor=hc
r.showFooter=sf
r.footerUnitIsInch=uf
r.footerHeight=fh
r.footerBGColor=fc
r.bgColor=bgc
r.skin=sk
r.imgURL=iu
r.embimg=embimg
r.dispMode=dm
r.HAlign=ha
r.VAlign=va
r.hyperLinkColor=hlc
r.hyperLinkVisitedColor=hlvc
r.paperSize=ps
r.paperOrientation=po
r.pageUnit=pu
r.pageHeight=ph
r.pageWidth=pw
r.marginUnitIsInch=um
r.marginTop=mt
r.marginLeft=ml
r.marginBottom=mb
r.marginRight=mr
r.pageMode=pm
r.repMode=rm
r.nbPage=nbp
r.selPage=sp
r.isLeaf=il
r.hdskin=hdsk
r.hdimgURL=hdiu
r.hdembimg=hdembimg
r.hddispMode=hddm
r.hdHAlign=hdha
r.hdVAlign=hdva
r.ftskin=ftsk
r.ftimgURL=ftiu
r.ftembimg=ftembimg
r.ftdispMode=ftdm
r.ftHAlign=ftha
r.ftVAlign=ftva
r.vertRecords=100
r.horiRecords=20
}
function AlerterElement(id,s,st,desc)
{
var o=this;
o.id=id;
o.name=s;
o.state=st;
o.description=desc;
return o;
};
