<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ taglib prefix="xpedx" uri="/WEB-INF/xpedx.tld"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->
	<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
	

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/home<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/portalhome<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/narrowBy<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/styles<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/theme-xpedx_v1.2<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil-new<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-forms<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-quick-add<s:property value='#wcUtil.xpedxBuildKey' />.css"/>

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/prod-details<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
 



<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- carousel scripts css  -->

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/theme<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/skin<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

 <script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
 
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript">
	
</script>


<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>

<title><s:property value="wCContext.storefrontId" /> - Imposition Calculator</title>
</head>

<s:set name='_action' value='[0]' />
<s:set name='shtSizeW' value='#_action.getSheetSizeW()' />
<s:set name='shtSizeH' value='#_action.getSheetSizeH()' />
<s:set name='trmSizeW' value='#_action.getTrimSizeW()' />
<s:set name='trmSizeH' value='#_action.getTrimSizeH()' />
<s:set name='grpWidth' value='#_action.getGripperWidth()' />
<s:set name='clrBarWidth' value='#_action.getColorBarWidth()' />
<s:set name='sideGde' value='#_action.getSideGuide()' />
<s:set name='gtr' value='#_action.getGutter()' />
<s:set name='numoutWasteList' value='#_action.getNumWasteList()'/>


<body class="ext-gecko ext-gecko3">
    <div id="main-container">
        <div id="main">
        	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
        
			<div class="container"> 
                <!-- breadcrumb -->
                <div id="searchBreadcrumb">
                	<!-- <a href="<s:url action="home" namespace="/home" includeParams='none'/>">Home</a> / <a href="<s:url action="XPEDXTools" namespace="/xpedx/tools" includeParams='none'/>">Tools</a>  / <span class="page-title">Imposition Calculater</span> Commented For Jira 1538 --> 
                </div>                               
                
                <div id="mid-col-mil">
                
                
                
                
                <div class="clearview"><div class="clearview">&nbsp;</div><h2>Possible Layouts:</h2></div><div class="clearview">&nbsp;</div>
<table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="399" class="no-border table-header-bar-left "><span class="white txt-small"> Layout Method (click link to view layout)</span></td>
                                <td width="82" align="left" class="no-border"><span class="white txt-small">Number out </span></td>
                                <td align="left" class="no-border-right table-header-bar-right"><span class="white txt-small">Waste % </span> </td>
                      </tr>
                      
                      <s:iterator status="stat" value="numoutWasteList" id="numWastMap">
                      	<s:if test="#stat.index == 0">
                      		<s:url id="option1" namespace="/xpedx/tools" action="xpedxImpCalculationPage">
                      			<s:param name="option" value="1"/>
                      			<s:param name="sheetSizeW" value="#shtSizeW"/>
                      			<s:param name="sheetSizeH" value="#shtSizeH"/>
                      			<s:param name="trimSizeW" value="#trmSizeW"/>
                      			<s:param name="trimSizeH" value="#trmSizeH"/>
                      			<s:param name="gripperWidth" value="#grpWidth"/>
                      			<s:param name="colorBarWidth" value="#clrBarWidth"/>
                      			<s:param name="sideGuide" value="#sideGde"/>
                      			<s:param name="gutter" value="#gtr"/>
                      		</s:url>                      
                            <tr class="odd">
                                <td><s:a href="%{#option1}">1. Sheet Long Side / Trim Long Side</s:a></td>
                                <s:set name="key1" value="#numWastMap.keySet().iterator().next()"/>
                                <s:set name="value1" value="#numWastMap.values().iterator().next()"/>
                              <td valign="top"><s:property value="#key1"/></td>
                              <td width="103" valign="top"><s:property value="#value1"/> %</td>
                            </tr>
                        </s:if>
                        <s:if test="#stat.index == 1">
                        	<s:url id="option2" namespace="/xpedx/tools" action="xpedxImpCalculationPage">
                      			<s:param name="option" value="2"/>
                      			<s:param name="sheetSizeW" value="#shtSizeW"/>
                      			<s:param name="sheetSizeH" value="#shtSizeH"/>
                      			<s:param name="trimSizeW" value="#trmSizeW"/>
                      			<s:param name="trimSizeH" value="#trmSizeH"/>
                      			<s:param name="gripperWidth" value="#grpWidth"/>
                      			<s:param name="colorBarWidth" value="#clrBarWidth"/>
                      			<s:param name="sideGuide" value="#sideGde"/>
                      			<s:param name="gutter" value="#gtr"/>
                      		</s:url>
                            <tr>
                              <td><s:a href="%{#option2}">2. Sheet Long Side / Trim Short Side</s:a></td>
                              <s:set name="key2" value="#numWastMap.keySet().iterator().next()"/>
                              <s:set name="value2" value="#numWastMap.values().iterator().next()"/>
                              <td valign="top"><s:property value="#key2"/></td>
                              <td valign="top"><s:property value="#value2"/> %</td>
                            </tr>
                        </s:if>
                        <s:if test="#stat.index == 2">
                        	<s:url id="option3" namespace="/xpedx/tools" action="xpedxImpCalculationPage">
                      			<s:param name="option" value="3"/>
                      			<s:param name="sheetSizeW" value="#shtSizeW"/>
                      			<s:param name="sheetSizeH" value="#shtSizeH"/>
                      			<s:param name="trimSizeW" value="#trmSizeW"/>
                      			<s:param name="trimSizeH" value="#trmSizeH"/>
                      			<s:param name="gripperWidth" value="#grpWidth"/>
                      			<s:param name="colorBarWidth" value="#clrBarWidth"/>
                      			<s:param name="sideGuide" value="#sideGde"/>
                      			<s:param name="gutter" value="#gtr"/>
                      		</s:url>
                            <tr class="odd">
                              <td><s:a href="%{#option3}">3. Sheet Long Side / Trim Long Side use waste if possible</s:a></td>
                              <s:set name="key3" value="#numWastMap.keySet().iterator().next()"/>
                              <s:set name="value3" value="#numWastMap.values().iterator().next()"/>
                              <td valign="top"><s:property value="#key3"/></td>
                              <td valign="top"><s:property value="#value3"/> %</td>
                            </tr>
                        </s:if>
                        <s:if test="#stat.index == 3">
                        	<s:url id="option4" namespace="/xpedx/tools" action="xpedxImpCalculationPage">
                      			<s:param name="option" value="4"/>
                      			<s:param name="sheetSizeW" value="#shtSizeW"/>
                      			<s:param name="sheetSizeH" value="#shtSizeH"/>
                      			<s:param name="trimSizeW" value="#trmSizeW"/>
                      			<s:param name="trimSizeH" value="#trmSizeH"/>
                      			<s:param name="gripperWidth" value="#grpWidth"/>
                      			<s:param name="colorBarWidth" value="#clrBarWidth"/>
                      			<s:param name="sideGuide" value="#sideGde"/>
                      			<s:param name="gutter" value="#gtr"/>
                      		</s:url>
                            <tr>
                              <td><s:a href="%{#option4}">4. Sheet Long Side / Trim Short Side use waste if possible</s:a></td>
                              <s:set name="key4" value="#numWastMap.keySet().iterator().next()"/>
                              <s:set name="value4" value="#numWastMap.values().iterator().next()"/>
                              <td valign="top"><s:property value="#key4"/></td>
                              <td valign="top"><s:property value="#value4"/> %</td>
                            </tr>
                      	</s:if>      
                      </s:iterator>
                            
                            
                            
                            
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
<div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
</div>
                    <div class="clearview">&nbsp;</div> 
                     <table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="187" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Parameters</span></td>
                                <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Sheet Size:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{shtSizeW}' /> x <s:property value='%{shtSizeH}' /></td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Trim Size:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{trmSizeW}' /> x <s:property value='%{trmSizeH}' /></td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Gripper Width:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{grpWidth}' /></td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Color Bar Width:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{clrBarWidth}' /></td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Side Guide Width:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{sideGde}' /></td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Gutter Width:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{gtr}' /></td>
                            </tr>
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
            <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>  <div class="clearview">&nbsp; </div> 
                
                            
                <div class="bot-margin"></div>
			</div>
        </div>
        
        </div>
	</div>
 
	<!-- end main  -->
    	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />



	<!-- end container  -->
</body>
</html>