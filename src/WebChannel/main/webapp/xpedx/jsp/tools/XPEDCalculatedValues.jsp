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
<meta name="WT.ti" content='<s:property value="wCContext.storefrontId" /> - Imposition Calculator'>
<meta name="DCSext.w_x_tools_ti" content='<s:property value="wCContext.storefrontId" /> - Imposition Calculator'>
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->
	<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
	

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" />.css" />
<!--[if IE]> 
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-ie-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" /> 
<![endif]--> 
 



<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- carousel scripts css  -->


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

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript">
	
</script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/backlink<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min.js"></script>

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

			<div class="container content-container">
                        <p class="addmarginbottom15"><a class="back-resources">‹ Back</a></p>            
                        <h1>Imposition Calculator Results</h1>    
                        <h2>Possible Layouts:</h2>
					<div class="clearview">&nbsp;</div>
					<table style="width: 99%;" class="standard-table addpadtop10">
						
							<tr>

								<th width="399" > Layout Method (click link to
										view layout)</th>
								<th width="82">Number out </th>
								<th>Waste % </th>
							</tr>
						<tbody>
							<s:iterator status="stat" value="numoutWasteList" id="numWastMap">
								<s:if test="#stat.index == 0">
									<s:url id="option1" namespace="/tools"
										action="MyImpCalculationPage">
										<s:param name="option" value="1" />
										<s:param name="sheetSizeW" value="#shtSizeW" />
										<s:param name="sheetSizeH" value="#shtSizeH" />
										<s:param name="trimSizeW" value="#trmSizeW" />
										<s:param name="trimSizeH" value="#trmSizeH" />
										<s:param name="gripperWidth" value="#grpWidth" />
										<s:param name="colorBarWidth" value="#clrBarWidth" />
										<s:param name="sideGuide" value="#sideGde" />
										<s:param name="gutter" value="#gtr" />
									</s:url>
									<tr class="odd">
										<td style="border-right:none;"><s:a href="%{#option1}">1. Sheet Long Side / Trim Long Side</s:a></td>
										<s:set name="key1"
											value="#numWastMap.keySet().iterator().next()" />
										<s:set name="value1"
											value="#numWastMap.values().iterator().next()" />
										<td valign="top" style="border-right:none;"><s:property value="#key1" /></td>
										<td width="103" valign="top"><s:property value="#value1" />
											%</td>
									</tr>
								</s:if>
								<s:if test="#stat.index == 1">
									<s:url id="option2" namespace="/tools"
										action="MyImpCalculationPage">
										<s:param name="option" value="2" />
										<s:param name="sheetSizeW" value="#shtSizeW" />
										<s:param name="sheetSizeH" value="#shtSizeH" />
										<s:param name="trimSizeW" value="#trmSizeW" />
										<s:param name="trimSizeH" value="#trmSizeH" />
										<s:param name="gripperWidth" value="#grpWidth" />
										<s:param name="colorBarWidth" value="#clrBarWidth" />
										<s:param name="sideGuide" value="#sideGde" />
										<s:param name="gutter" value="#gtr" />
									</s:url>
									<tr>
										<td style="border-right:none;"><s:a href="%{#option2}">2. Sheet Long Side / Trim Short Side</s:a></td>
										<s:set name="key2"
											value="#numWastMap.keySet().iterator().next()" />
										<s:set name="value2"
											value="#numWastMap.values().iterator().next()" />
										<td valign="top" style="border-right:none;"><s:property value="#key2" /></td>
										<td valign="top"><s:property value="#value2" /> %</td>
									</tr>
								</s:if>
								<s:if test="#stat.index == 2">
									<s:url id="option3" namespace="/tools"
										action="MyImpCalculationPage">
										<s:param name="option" value="3" />
										<s:param name="sheetSizeW" value="#shtSizeW" />
										<s:param name="sheetSizeH" value="#shtSizeH" />
										<s:param name="trimSizeW" value="#trmSizeW" />
										<s:param name="trimSizeH" value="#trmSizeH" />
										<s:param name="gripperWidth" value="#grpWidth" />
										<s:param name="colorBarWidth" value="#clrBarWidth" />
										<s:param name="sideGuide" value="#sideGde" />
										<s:param name="gutter" value="#gtr" />
									</s:url>
									<tr class="odd">
										<td style="border-right:none;"><s:a href="%{#option3}">3. Sheet Long Side / Trim Long Side use waste if possible</s:a></td>
										<s:set name="key3"
											value="#numWastMap.keySet().iterator().next()" />
										<s:set name="value3"
											value="#numWastMap.values().iterator().next()" />
										<td valign="top" style="border-right:none;"><s:property value="#key3" /></td>
										<td valign="top"><s:property value="#value3" /> %</td>
									</tr>
								</s:if>
								<s:if test="#stat.index == 3">
									<s:url id="option4" namespace="/tools"
										action="MyImpCalculationPage">
										<s:param name="option" value="4" />
										<s:param name="sheetSizeW" value="#shtSizeW" />
										<s:param name="sheetSizeH" value="#shtSizeH" />
										<s:param name="trimSizeW" value="#trmSizeW" />
										<s:param name="trimSizeH" value="#trmSizeH" />
										<s:param name="gripperWidth" value="#grpWidth" />
										<s:param name="colorBarWidth" value="#clrBarWidth" />
										<s:param name="sideGuide" value="#sideGde" />
										<s:param name="gutter" value="#gtr" />
									</s:url>
									<tr>
										<td style="border-right:none;"> <s:a href="%{#option4}">4. Sheet Long Side / Trim Short Side use waste if possible</s:a></td>
										<s:set name="key4"
											value="#numWastMap.keySet().iterator().next()" />
										<s:set name="value4"
											value="#numWastMap.values().iterator().next()" />
										<td valign="top" style="border-right:none;"><s:property value="#key4" /></td>
										<td valign="top"><s:property value="#value4" /> %</td>
									</tr>
								</s:if>
							</s:iterator>




						</tbody>
					</table>
					
					<div class="clearview">&nbsp;</div>
					<table style="width: 99%;" class="standard-table">
						
							<tr>

								<th width="187">Parameters</th>
								<th colspan="2"></th>
							</tr>
							<tbody>
							<tr>
								<td style="border-right:none;">Sheet Size:</td>
								<td width="12" valign="middle" style="border-right:none;border-left:none;">
									:</td>
								<td width="376" valign="middle" style="border-left:none;"><s:property
										value='%{shtSizeW}' /> x <s:property value='%{shtSizeH}' /></td>
							</tr>
							<tr>
								<td style="border-right:none;">Trim Size:</td>
								<td width="12" valign="middle" style="border-right:none;border-left:none;">
									:</td>
								<td width="376" valign="middle" style="border-left:none;"><s:property
										value='%{trmSizeW}' /> x <s:property value='%{trmSizeH}' /></td>
							</tr>
							<tr>
								<td style="border-right:none;">Gripper Width:</td>
								<td width="12" valign="middle" style="border-right:none;border-left:none;">
									:</td>
								<td width="376" valign="middle" style="border-left:none;"><s:property
										value='%{grpWidth}' /></td>
							</tr>
							<tr>
								<td style="border-right:none;">Color Bar Width:</td>
								<td width="12" valign="middle" style="border-right:none;border-left:none;">
									:</td>
								<td width="376" valign="middle" style="border-left:none;"><s:property
										value='%{clrBarWidth}' /></td>
							</tr>
							<tr>
								<td style="border-right:none;">Side Guide Width:</td>
								<td width="12" valign="middle" style="border-right:none;border-left:none;">
									:</td>
								<td width="376" valign="middle" style="border-left:none;"><s:property
										value='%{sideGde}' /></td>
							</tr>
							<tr>
								<td style="border-right:none;">Gutter Width:</td>
								<td width="12" valign="middle" style="border-right:none; border-left:none;">
									:</td>
								<td width="376" valign="middle" style="border-left:none;"><s:property
										value='%{gtr}' /></td>
							</tr>
						</tbody>
					</table>
					
					<div class="clearview">&nbsp;</div>


					<div class="bot-margin"></div>
			</div>

		</div>
	</div>

	<!-- end main  -->
    	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />



	<!-- end container  -->
</body>
</html>