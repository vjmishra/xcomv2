<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en"
	xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>


<!-- styles -->
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/home.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/portalhome.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/narrowBy.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/catalogExt.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all.css" />
<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-forms.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-quick-add.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil-new.css" />

	
<!-- Test -->
<!-- javascript -->
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-returns-ui.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>

<!-- carousel scripts css  -->
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/theme.css" />

<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/skin.css" />

<!-- carousel scripts js   -->
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js"
	type="text/javascript" charset="utf-8"></script>
<script
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bgiframe.min.js"
	type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min.js"
	type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
		$('#Availability_Hover').bt({
			ajaxPath: '../tool-tips/cart-availability-hover.html div#tool-tip-content',
			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
			fill: '#ebebeb',
			cssStyles: {color: 'black'},
			padding: 20,
			spikeLength: 10,
  			spikeGirth: 15,
			cornerRadius: 6,
			shadow: true,
			shadowOffsetX: 0,
			shadowOffsetY: 3,
			shadowBlur: 3,
			shadowColor: 'rgba(0,0,0,.4)',
			shadowOverlap: false,
			strokeWidth: 1,
  			strokeStyle: '#FFFFFF',
			noShadowOpts:     {strokeStyle: '#969696'},
			positions: ['top']
		});

	});
</script>

<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min.js"></script>
	
	
<s:set name='_action' value='[0]' />
<s:set name='customerID' value="wCContext.customerId" />
<s:set name='orgCode' value="wCContext.storefrontId" />
<s:url id='createCustQL' namespace='/profile/user' action='xpedxCustomerQuickLink'>
	<s:param name='createSelected' value='true'/>
</s:url>

<s:url id='userProfile' namespace='/profile/user' action='xpedxUserProfile' />
<s:url id='custProfile' namespace='/profile/org' action='xpedxGetCustomerInfo' />
<s:url id='shipTo' namespace='/profile/org' action='xpedxGetShipToInfo' />
<s:url id='billTo' namespace='/profile/org' action='xpedxGetBillToInfo' />


<!-- <title>User Profile</title> -->
<title> <s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.ADMN.PRF.GENERIC.TABTITLE"/> </title>

</head>
<!-- END swc:head -->
<body class="  ext-gecko ext-gecko3">
<div id="main-container">
<div id="main"><s:action name="xpedxHeader" executeResult="true"
	namespace="/common" />


<div class="container"><!-- breadcrumb -->

<div id="searchBreadcrumb" class="page-title"><a
	<%-- href="<s:url action="home" namespace="/home" includeParams='none'/>">Admin</a> --%>
	href="<s:url action="home" namespace="/home" includeParams='none'/>"> <s:text name="MSG.SWC.ADMN.PRF.GENERIC.PGTITLE"/> </a>
<%-- - <span class="breadcrumb-inactive">User Profile</span> <a href="javascript:window.print()"><span --%>
 - <span class="breadcrumb-inactive"> <s:text name="MSG.SWC.ADMN.PRFU.GENERIC.USERPROFILE"/> </span> <a href="javascript:window.print()"><span 
	class="print-ico-xpedx"><img
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon.gif" width="16"
	height="15" alt="Print Page" />Print Page</span></a></div>


<div id="mid-col-mil">
<div class="mil-edit"></div>
	<s:hidden name="customerId" value="%{#customerID}" id="customerId"/>
	<s:hidden name='organizationCode' value='%{#orgCode}' id="organizationCode"/>
<table>
	<tr>
		<td>
			<%-- <s:a href="%{custProfile}">User Profile</s:a> --%>
			<s:a href="%{userProfile}"> <s:text name="MSG.SWC.ADMN.PRF.GENERIC.USER.PGTITLE"/> </s:a>
		</td>
		<td>
			<%-- <s:a href="%{custProfile}">Customer Profile</s:a> --%>
			<s:a href="%{custProfile}"><s:text name="MSG.SWC.ADMN.PRF.GENERIC.CUST.PGTITLE"/></s:a>
		</td>
		<td>
			<s:a href="%{shipTo}">Ship To</s:a>
		</td>
		<td>
			<s:a href="%{billTo}">Bill To</s:a>
		</td>
	</tr>
</table>


<div class="clearview">&nbsp;</div>
</div>
<!-- End Pricing --> <br />
<br />
</div>
</div>
</div>

<!-- end main  -->
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
<!-- end container  -->
</body>
</html>

