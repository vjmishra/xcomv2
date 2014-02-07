<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>
<% request.setAttribute("isMergedCSSJS","true"); %>

<s:set name='_action' value='[0]' />
<s:set name="xutil" value="XMLUtils" />
<s:set name="articleElement" value="articleElement" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='dateUtilBean'/>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils' id='util' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<%-- <title><s:property value="wCContext.storefrontId" /> - News Article</title> --%>
<title><s:property value="wCContext.storefrontId" /> - <s:text name='MSG.SWC.NEWSARTL.READ.GENERIC.TABTITLE' /> </title>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />

<!-- begin styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/ADMIN.css" />
<!-- end styles -->
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE.css" />
<![endif]-->

<!-- jQuery Base & jQuery UI -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/draftOrderList<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.datepicker<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
	$(function() {
		$(".datepicker").datepicker({
			showOn: 'button',
						numberOfMonths: 1,
	
			buttonImage: '<s:property value='#util.staticFileLocation' />/images/theme/theme-1/calendar-icon.png',
			buttonImageOnly: true
		});
	});
</script>

</head>
<%-- <swc:extDateFieldComponentSetup/> --%>
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
							src="<s:property value='#util.staticFileLocation' />/xpedx/images/common/print-icon.gif" width="16" height="15"
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