<%@page import="java.util.Date"%>
<%@page import="com.yantra.yfc.util.YFCDate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>
<% request.setAttribute("isMergedCSSJS","true"); %>

<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<!--Web trends tag start  -->
<meta name="DCSext.w_x_news_fa" content="1" />
<meta name="WT.ti" content="View Article">
<!--Web trends tag end  -->
 
<!-- begin styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/ADMIN.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE.css" />
<![endif]-->
<!-- end styles -->

<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script>

<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui.js" language="javascript">
	
</script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min.js"></script>

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>
<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/SpryTabbedPanels.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
		$("#varous1").fancybox();

$("#various2").fancybox();
$("#various3").fancybox();
$("#various4").fancybox();
$("#various5").fancybox(); 
});
	
	
	
</script> 

<title><s:property value="wCContext.storefrontId" /> - <s:text name='MSG.SWC.NEWSARTL.PREVIEW.GENERIC.TABTITLE' /></title>

</head>
<!-- END swc:head -->
<body class="ext-gecko ext-gecko3">

<s:set name='_action' value='[0]'/>
<s:set name='wcContext' value="#_action.getWCContext()"/>
<s:set name="ArticleName" value="#parameters['articleName']" />
<s:set name="Createts" value='%{#util.formatDate(<%= new java.util.Date()%>,#wcContext)}' />
 
<div id="main-container">
  <div id="main">

	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
    
    <div class="container">
      <!-- breadcrumb -->
	  
      <div id="mid-col-mil"><br />
      
      <div>
      
       <span class="page-title"><s:text name='MSG.SWC.NEWSARTL.PREVIEW.GENERIC.PGTITLE' /> </span> 
       
		<div class="float-right clearview padding-bottom3" ><a href="javascript:window.print()"><span
			class="print-ico-xpedx underlink"><img
			src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon.gif" width="16" height="15"
			alt="Print This Page" />Print Page</span></a></div>

		<div>
		
	
		

		 <div>
 			 <%-- <span class="dkcharcole"> Note: News article will not be saved until you click on the Publish button. </span> --%>
 			 <span class="dkcharcole"> <s:text name='MSG.SWC.NEWSARTL.PREVIEW.INFO.PREVIEWNOTE' /> </span>
		 </div>
		<br/>
			
		<hr class="lightgray"></hr>
		<br/>


			
		<table class="form margin-15 underlines" style="margin-bottom: 10px;">
			<tr>
				<td class="no-padding no-border-right-user underlines" width="100%">
				<%-- <a href="javascript:window.history.back();" class="underlines"> Back </a> / <strong><s:property value="#ArticleName"/> </strong> <br /> --%>
				  <span class="page-title"><s:property value="#ArticleName"/> </span> <br /> 
				<!--Web trends tag start  -->
				<meta name="DCSext.w_x_news_fa_ti" content="<s:property value='%{#ArticleName}' />"/>
				<!--Web trends tag end  -->
				<s:property value='%{#Createts}'/><br/>				
				</td>
			</tr>
		</table>
<!-- 
        <div class="table-top-bar" style="width:100%;">
        	<div class="table-top-bar-L"></div>
            <div class="table-top-bar-R"></div>
			        </div>
			
 -->			
			<table class="form" width="100%" style="width: 100%">
				<tbody>
				<tr>
					<td valign="top">
					<s:property escape='false' value='#parameters["articleBody"]' /><br />
					</td>
				</tr>
				</tbody>
			</table>

			<!-- 			
			<div id="table-bottom-bar" style="width: 100%">
				<div id="table-bottom-bar-L"></div>
				<div id="table-bottom-bar-R"></div>
			</div> 
			-->
			<br />
			<hr class="lightgray"></hr>
			<br></br>

			<div class="clearview">
			<!--removed, causing format issue when text is large : style="float:right;" -->
				<ul id="tool-bar" class="tool-bar-bottom" style="float: right;">
					<li><a class="grey-ui-btn" href="javascript:history.back();"><span>Cancel</span></a></li>
					<li ><a class="orange-ui-btn" href="javascript:document.newArticleForm.submit();"><span>Publish</span></a></li>
				</ul>
			</div>
		</div>				

<s:form name="newArticleForm" action="%{#parameters['nextActionName']}" namespace="/profile/user" method="POST">
		<input type="hidden" name="articleKey" value="<s:property value="#parameters['articleKey']"/>"/>
		<input type="hidden" name="articleName" value="<s:property value="#parameters['articleName']"/>"/>
		<input type="hidden" name="submittedTSFrom" value="<s:property value="#parameters['submittedTSFrom']"/>"/>
		<input type="hidden" name="submittedTSTo" value="<s:property value="#parameters['submittedTSTo']"/>"/>
		<input type="hidden" name="forcedMessage" value="<s:property value="#parameters['forcedMessage']"/>"/>
		<input type="hidden" name="articleBody" value="<s:property escape='true' value='#parameters["articleBody"]' />"/>
		</s:form>
      </div>
</div>
      <!-- End Pricing -->
      <br />
    </div>
  </div>
</div>
    
    <s:action name="xpedxFooter" executeResult="true" namespace="/common" />
</body>
</html>