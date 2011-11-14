<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>

<s:set name='_action' value='[0]' />
<s:set name="xutil" value="XMLUtils" />
<s:set name="articleElement" value="articleElement" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='dateUtilBean'/>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils' id='util' />

<style type="text/css">

.feildsets {
	-moz-border-radius:4px 4px 4px 4px;
	border:1px solid #D3D3D3;
	padding-left:5px;
}
.feildsets legend {
	color:#999999;
	font-size:11px;
	font-style:italic;
	margin-left:7px;
	padding:0 3px;
}
.feild-service {
	-moz-border-radius:4px 4px 4px 4px;
	border:1px solid #D3D3D3;
	margin-right:15px;
	padding-left:5px;
}
.feild-service legend {
	color:#003366;
	font-size:14px;
	font-weight:bold;
	margin-left:7px;
	padding:0 3px;
}
</style>
<%-- <title><s:property value="wCContext.storefrontId" /> - News Article</title> --%>
<title><s:property value="wCContext.storefrontId" /> - <s:text name='MSG.SWC.NEWSARTL.READ.GENERIC.TABTITLE' /> </title>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />


<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/home/home.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/home/portalhome.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/catalog/narrowBy.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/catalog/catalogExt.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/ext-all.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/global/swc.css" />

<s:include value="../common/XPEDXStaticInclude.jsp"/>

<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-quick-add.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/prod-details.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-mil-new.css" />

<!-- jQuery Base & jQuery UI -->
<script type="text/javascript" src="/swc/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/swc.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/order/draftOrderList.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>

<link type="text/css" href="/swc/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all.css" rel="stylesheet" />
<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="/swc/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<script src="/swc/xpedx/js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="/swc/xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>
<link rel="stylesheet" type="text/css" href="/swc/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/swc/xpedx/js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom.css" media="screen" />

<script type="text/javascript" src="/swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery.ui.datepicker.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
	$(function() {
		$(".datepicker").datepicker({
			showOn: 'button',
						numberOfMonths: 1,
	
			buttonImage: '/swc/xpedx/images/theme/theme-1/calendar-icon.png',
			buttonImageOnly: true
		});
	});
</script>

</head>
<swc:extDateFieldComponentSetup/>
<script type="text/javascript">
    function swc_validateForm_addArticle() {
    var errors = false;
        
        form = document.getElementById("addArticle");

        
        // validator name: swcajax
        var isAjaxValidated = swc_validateForm("addArticle", false); //calling global js-function for ajax-validation defined in ajaxValidation.js
        if(!isAjaxValidated)
           errors = true;
    if(errors){
    	try{
            svg_classhandlers_decoratePage();
        }catch (err){
            alert("There is an error in Svg-apply: "+err.message);
        }
    }
	    return !errors;
    }
</script>


<body class="ext-gecko ext-gecko3">
	<div id="main-container">
		<div id="main"><s:action name="xpedxHeader" executeResult="true"
				namespace="/common" /> <!-- // header end -->
		<div class="container"><!-- breadcrumb -->
			<div id="mid-col-mil"><br />

				<div>
					
					<div class="float-right clearview padding-bottom3" ><a href="javascript:window.print()"><span
							class="print-ico-xpedx underlink"><img
							src="/swc/xpedx/images/common/print-icon.gif" width="16" height="15"
							alt="Print This Page" />Print Page</span></a></div>
					
					<div>
					<h2></h2>
				
				<table class="form margin-15 underlines" style="margin-bottom: 10px;">
					<tr>
						<td class="no-padding no-border-right-user underlines" width="100%">
	
						<%-- <span class="page-title">News Article</span> --%>
						<span class="page-title"> <s:text name='MSG.SWC.NEWSARTL.READ.GENERIC.PGTITLE' /> </span>
						<br/><br/><span class="bold"><s:property value='#xutil.getAttribute(#articleElement,"ArticleName")' /> </span><br/>
						<s:property value='%{#dateUtilBean.formatDate(#xutil.getAttribute(#articleElement,"Createts"),wCContext)}' />
						</td>
					</tr>
				</table>
				
						<!-- <div class="table-top-bar" style="width: 100%;">
						<div class="table-top-bar-L"></div>
						<div class="table-top-bar-R"></div>
						</div> -->
	
						<table id="news-article-table" class="form" width="100%" style="width: 100%">
							<tr>
								<td valign="top"><s:property escape='false'
									value='#xutil.getAttribute(#articleElement,"Article")' /></td>
							</tr>
						</table>
						
					<!--  <div id="table-bottom-bar" style="width: 100%">
						<div id="table-bottom-bar-L"></div>
						<div id="table-bottom-bar-R"></div>
					</div> -->
					<br />
					<div class="clearview"></div>
				</div>
</div>
<!-- End Pricing -->
</div>
</div>
</div>
</div>


<!-- end container  --> <s:action name="xpedxFooter"
	executeResult="true" namespace="/common" />
</body>
</html>