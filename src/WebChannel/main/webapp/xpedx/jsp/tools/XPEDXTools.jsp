<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />



<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />


<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- jQuery -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
    <!-- Lightbox/Modal Window -->
    <%-- <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
    <link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
     --%>
     
      <script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
 
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
     
 <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript"></script> 
        
        <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
        
<!--   This is Line Separator  -->
<!-- Web Trends tag start -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Web Trends tag end  -->
<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript"></script> 



<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
</script>
<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.MISC.TOOLS.GENERIC.PGTITLE" /></title>
<!--Webtrends tag starts-->
<meta name="WT.ti" Content='<s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.MISC.TOOLS.GENERIC.PGTITLE" />'>
<!--Webtrends tag stops-->
</head>

<s:set name='_action' value='[0]' />
<body class="ext-gecko ext-gecko3">
    <div id="main-container">
        <div id="main">

		<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
		



            <div class="container"> 
                <!-- breadcrumb -->
               
                <div id="mid-col-mil">
				<br/>
				
				
				 <div  class="padding-bottom3" >
                	  <span class="page-title"><s:text name="MSG.SWC.MISC.TOOLS.GENERIC.PGTITLE" /></span><!--  > Commented for jira 1538-->
                </div>
				
               	<div class="underlines x-input"> 
            <ul class="tools-page">

            <!---->
            	<s:url  id='newsMaintenanceLink' namespace="/profile/user" action='MyNewsMaintenance'></s:url>
				<s:url id='calculator' namespace="/tools" action='calculator' />	
				<s:url id='ReamWeights' namespace="/tools" action='Ream-Weights' />
				<s:url id='MWeight' namespace="/tools" action='M-Weight' />
				<s:url id='roolWeight' namespace="/tools" action='rool-Weight' />	
				<s:url id='LinearFootageCalculator' namespace="/tools" action='Linear-Footage-Calculator' />	
				<s:url id='basisWeightConversionChart' namespace="/tools" action='basis-Weight-Conversion-Chart' />	
				<s:url id='impositionCalculatorLink' namespace="/tools" action='MyImpositionCalculator' />																		
				<s:url id='GSMCalculator' namespace="/tools" action='GSMCalculator' />	
				<s:url id='PagesInchcalculator' namespace="/tools" action='PagesInch-calculator' />	
				<s:url id='Weightofoddnumofsheet' namespace="/tools" action='Weight-of-odd-num-of-sheet' />	
				<s:url id='SheetDensity' namespace="/tools" action='SheetDensity' />	
				<s:url id='Equivalentweight' namespace="/tools" action='Equivalent-weight' />	
				
				<!---->
						      
                <li><span class="underlink"> <s:a href="%{calculator}"><s:text name="tools.papercalculator.title" /></s:a> </span></li>
                <li><span class="underlink"> <s:a href="%{ReamWeights}"><s:text name="tools.reamweight.title" /></s:a> </span></li>
                <li><span class="underlink"> <s:a href="%{MWeight}"><s:text name="tools.mweight.title" /></s:a> </span></li>
                <li><span class="underlink"> <s:a href="%{roolWeight}"><s:text name="tools.rollweight.title" /></s:a> </span></li>
                <li><span class="underlink"> <s:a href="%{LinearFootageCalculator}"><s:text name="tools.linearfootagecalculator.title" /></s:a> </span></li>

                <li><span class="underlink"> <s:a href="%{basisWeightConversionChart}"><s:text name="tools.basiswtconvchart.title" /></s:a> </span></li>
                <li><span class="underlink"> <s:a href="%{impositionCalculatorLink}"><s:text name="tools.impositioncalculator.title" /></s:a> </span></li>
                <li><span class="underlink"> <s:a href="%{GSMCalculator}"><s:text name="tools.gsmcalculator.title" /></s:a> </span></li>
                <li><span class="underlink"> <s:a href="%{PagesInchcalculator}"><s:text name="tools.pagesinchcalculator.title" /></s:a> </span></li>
                <li><span class="underlink"> <s:a href="%{Weightofoddnumofsheet}"><s:text name="tools.wtofoddnoofsheets.title" /></s:a> </span></li>
                <li><span class="underlink"> <s:a href="%{SheetDensity}"><s:text name="tools.sheetdensity.title" /></s:a> </span></li>
                <li><span class="underlink"><s:a href="%{Equivalentweight}"><s:text name="tools.eqbasiswtchart.title" /></s:a> </span></li>
                <!-- <li><span class="underlink"> <a href="http://dbcalc.usps.gov">Postal Calculator</a> </span></li> Changes done for jira 1538-->
                <li><span class="underlink"> <a target="_blank" href="http://dbcalc.usps.gov">Postal Calculator</a> </span></li>
                <li><span class="underlink"> <a target="_blank" href="http://www.canadapost.ca" onclick="javascript: writeMetaTag('WT.ti', 'Postes Canada - Canada Post');">Canada Post Tools</a> </span></li><!--add _blank for jira 1538  -->
                <!-- <li><span class="underlink"> <a href="#" onclick="javascript: writeMetaTag('WT.ti', 'Postes Canada - Canada Post');">Electronic Product Guide</a> </span></li> Commented for jira 1538-->
               <%-- <li><span class="underlink"> <a target="_blank" href="http://xpedx.edviser.com/" onclick="javascript: writeMetaTag('WT.ti', 'Graphics Edviser');">Printer Knowledge Center</a> </span></li>--%><!--Commented for XB-438  -->
          </ul>
          			</div>
                </div>
			</div>
        </div>
    </div>
	<!-- end main  -->

	

	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	<!-- end container  -->
</body>
</html>