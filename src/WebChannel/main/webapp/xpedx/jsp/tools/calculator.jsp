<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/home/home.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/home/portalhome.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/catalog/narrowBy.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/catalog/catalogExt.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/global/ext-all.css" />

<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/xpedx-mil-new.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/xpedx-quick-add.css"/>

<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/prod-details.css"/>
 

<!-- javascript -->

<script type="text/javascript" src="../../xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="../../xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="../../xpedx/js/catalog/catalogExt.js"></script>

<!-- carousel scripts css  -->

<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/js/jcarousel/skins/xpedx/theme.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/js/jcarousel/skins/xpedx/skin.css" />

<!-- carousel scripts js   -->

<script type="text/javascript" src="../../xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript" src="../../xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../../xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="../../xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="../../xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>

<script type="text/javascript" src="../../xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="../../xpedx/js/quick-add/quick-add.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="../../xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<script src="../../xpedx/js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="../../xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="../../xpedx/js/xpedx-new-ui.js" language="javascript">
	
</script>


<script type="text/javascript" src="/swc/xpedx/js/jquery-ui.min.js"></script>

<title><s:text name="tools.papercalculator.title" /></title>
<!-- Webtrend tag starts -->
<meta name="WT.ti" content="Paper Calculator">
<!-- Webtrend tag stops --> 
</head>
<body class="ext-gecko ext-gecko3">

    <div id="main-container">
      <div id="main">
        <s:action name="xpedxHeader" executeResult="true" namespace="/common" />
        <div class="container">
          <!-- breadcrumb -->
          <div id="searchBreadcrumb">
	    	  	<s:url id='toolsLink' namespace='/xpedx/tools' action='XPEDXTools'>
			  		<s:param name="xpedxSelectedHeaderTab">ToolsTab</s:param>
				</s:url>
          		<!-- <a href="<s:url action="home" namespace="/home" includeParams='none'/>"><s:text name="home.title" /></a> / <s:a href="%{toolsLink}"><s:text name="tools.title" /></s:a> / <span class="breadcrumb-inactive"><s:text name="tools.papercalculator.title" /></span> Commented for jira 1538-->
          		 </div>
	        <div id="mid-col-mil">
            <div style=" width: 600px;">
               <div class="clearview">&nbsp;</div>
              <h2>Paper Calculator</h2>
              <div id="requestform">
                <div class="clearview">&nbsp;</div>
                
                <form name="calculator">
                  <table style="width:600px;" class="form">
                    <tbody>
                      <tr>
                        <td>Page width: </td>
                        <td><input name="pagew" type="text" style="width:80px;" onkeyup="calcPhone('Value',calculator.pagew);" class="x-input align-right" id="pagew" /></td>
                      </tr>
                      <tr>
                        <td>Page height: </td>
                        <td><input name="pageh" type="text" style="width:80px;" class="x-input align-right" id="pageh" onkeyup="calcPhone('Value',calculator.pageh);"/></td>
                      </tr>
                      <tr>
                        <td>Number of pages: </td>
                        <td><input name="pages" type="text" style="width:80px;" class="x-input align-right" id="pages" onkeyup="calcPhone('Value',calculator.pages);"/></td>
                      </tr>
                      <tr>
                        <td>Number of pieces: </td>
                        <td><input name="pieces" type="text" style="width:80px;" class="x-input align-right" id="pieces" onkeyup="calcPhone('Value',calculator.pieces);"/></td>
                      </tr>
                      <tr>
                        <td>Waste: </td>
                        <td><input name="waste" type="text" style="width:80px; text-align:right" class="x-input align-right" onkeyup="calcPhone('Value',calculator.waste);" id="waste" />
                          <strong>% </strong></td>
                      </tr>
                      <tr>
                        <td>Basis weight: </td>
                        <td><input name="weight" type="text" style="width:80px;" class="x-input align-right" id="weight" onkeyup="calcPhone('Value',calculator.weight);" /></td>
                      </tr>
                      <tr>
                        <td>Basis size: </td>
                        <td><span class="no-border-left">
                          <select style="width:80px;" 
																																	name="size" id="size">
                            <option  selected="selected" value="">Select size</option>
                            <option value="374">17x22</option>
                            <option value="520">20x26</option>
                            <option value="641.25">22.5x28.5</option>
                            <option value="864">24x36</option>
                            <option value="726.75">25.5x28.5</option>
                            <option value="777.75">25.5x30.5</option>
                            <option value="950" >25x38</option>
                          </select>
                          <input type="hidden" id="txtstran" name="txtstran" />
                        </span></td>
                      </tr>
                      <tr>
                        <td>&nbsp;</td>
                        <td><ul id="cart-actions">
                          <li><a href="#" onclick="javascript:calc();" class="green-ui-btn"><span>Calculate</span></a></li>
                          <li><a class="grey-ui-btn" href="#" onClick="javascript:document.calculator.reset()"><span>Clear</span></a></li>
                        </ul></td>
                      </tr>
                      <tr >
                        <td width="268"><strong>Paper Estimate: </strong></td>
                        <td width="320"><div id="Layer1" style="FONT-WEIGHT: bold; WIDTH: 239px; COLOR: #ff0000; HEIGHT: 19px">Paper 
                          Estimate: 0 pounds </div></td>
                      </tr>
                    </tbody>
                  </table>
                </form>
                <div class="clearview">&nbsp;</div>
              </div>
              <div class="clearview">&nbsp;</div>
              <div class="x-corners">
                <div ><strong>Note:</strong><br />
                  The results of the Interactive Calculations System are estimates and are not guaranteed by International Paper.<br />
                  <br />
                </div>
              </div>
            </div>
            <div class="bot-margin"></div>
          </div>
        </div>
        <s:action name="xpedxFooter" executeResult="true" namespace="/common" />
        <!-- end main  -->
        
        <!-- end container  -->
      </div>
    </div>
    <script>  

function calc()


{  
	if (isNaN(document.calculator.pagew.value))
		{
			width = 0;
			document.calculator.pagew.value = '';
		}
	else
		{
			width = document.calculator.pagew.value;
		}
	if (isNaN(document.calculator.pageh.value))
		{
			height = 0;
			document.calculator.pageh.value = '';
		}
	else
		{
			height = document.calculator.pageh.value;
		}
	if (isNaN(document.calculator.weight.value))
		{
			weight = 0;
			document.calculator.weight.value = '';
		}
	else
		{
			weight = document.calculator.weight.value;
		}
	if (isNaN(document.calculator.pages.value))
		{
			pages = 0;
			document.calculator.pages.value = '';
		}
	else
		{
			pages = document.calculator.pages.value;
		}
	if (isNaN(document.calculator.pieces.value))
		{
			pieces = 0;
			document.calculator.pieces.value = '';
		}
	else
		{
			pieces = document.calculator.pieces.value;
		}
	if (isNaN(document.calculator.waste.value))
		{
			waste = 0;
			document.calculator.waste.value = '';
		}
	else
		{
			waste = 1 + (document.calculator.waste.value / 100);
		}
	basis_size = document.calculator.size.value
	result = ((width * height)/ basis_size ) * (weight / 1000) * pages * pieces * waste;
	result = Math.ceil(result * 100);
	result = result / 100
	//usd-3634239 Prashant Pandey/EDS
	//if ((obj=MM_findObj('Layer1'))!=null) with (obj)

	//innerHTML = unescape('Paper Estimate: ' + result + ' pounds');
	document.getElementById('Layer1').innerHTML='Paper Estimate: ' + result + ' pounds';
	

}



function MM_findObj(n, d) { //v3.0

  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {

    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}

  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];

  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document); return x;

}



function MM_setTextOfLayer(objName,x,newText) { //v3.0

  if ((obj=MM_findObj(objName))!=null) with (obj)

   // if (navigator.appName=='Netscape') {document.write(unescape(newText)); document.close();}

    //else 

	 innerHTML = unescape(newText);

}

</script>
</body>
</html>