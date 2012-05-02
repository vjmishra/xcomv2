<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ page import="java.util.* "%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />



<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional/EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en"
	xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES.css" />

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE.css" />
<![endif]-->
		
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<!-- styles -->
<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/banner.css"/>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jQuery.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/ajaxValidation.js"></script>


<!-- carousel scripts css  -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/theme.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/skin.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<!-- carousel scripts js   -->
<%-- <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script> --%>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten.js"></script>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/demo.css"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree.css"/>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions.js"></script>


<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom.css" media="screen" />

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shopping-cart.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/om2.css" />


<!--  Change Location Light box -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/org/xpedxCustomerLocations.js"></script>

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>




<script type="text/javascript">
	$(document).ready(function() 
{$(document).pngFix();
$("#varous1").fancybox();
$("#various2").fancybox();
$("#various3").fancybox();
$("#various4").fancybox(); 
});
	

	
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
</script>
<style type="text/css">
p{margin-bottom:8px;}
 </style>
<title><s:property value="wCContext.storefrontId" /> - Reports</title>
<!--Webtrends tag starts-->
<meta name="WT.ti" Content='<s:text name="tools.title" />'>
<!--Webtrends tag stops-->
</head>

<s:set name='_action' value='[0]' />
<body class="ext-gecko ext-gecko3">
  <div id="main-container">
        <div id="main" class="min-height-fix">

		<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
		
      		<div class="container">
                <div id="mid-col-mil">
				
      <div class="clearview"> &nbsp;</div>
   <div class="padding-bottom3"> <span class="page-title">Reports</span>
       </div>
        <div>
               <div class="x-input">
                 <div class=" padding-all2" style="padding: 10px 10px 10px 0;"> 
                 <s:iterator value="stdreportlist" id='rId'>
	<s:url id='myreporting' namespace='/xpedx/services'
		action='promptInput'>
		<s:param name='id' value='#rId.id' />
		<s:param name='cuid' value='#rId.cuid' />
		<s:param name='kind' value='#rId.kind' />
		<s:param name="name" value="#rId.name"/>
	</s:url>

		
	<p>
	<h4><s:a href='%{#myreporting}'>
		<s:property value="%{#rId.name}" />
	</s:a></h4>

	<s:property value="%{#rId.desc}" />
	<div class="clearview">&nbsp;</div>
</s:iterator>


		<s:iterator value="vallist" id='cId'>
	<s:url id='mycustomreporting' namespace='/xpedx/services'
				action='promptInput'>
				<s:param name='id' value='#cId.id' />
				<s:param name='cuid' value='#cId.cuid' />
				<s:param name='kind' value='#cId.kind' />
				<s:param name='name' value= '#cId.name' />
			</s:url>
	<p><h4><s:a href='%{#mycustomreporting}'><s:property value="%{#cId.name}" /></s:a></h4>
<s:property value="%{#cId.desc}" />

</s:iterator>
                    </div>  </div> </div>
      <div class="clearview"> &nbsp;</div>
      <div class="clearview"> &nbsp;</div>   
      </div>
      </div>

			<div class="inf">
				Reports can be saved and printed using Adobe PDF or Microsoft Excel.<br />
				If you do not have Adobe Reader, click the link below and follow the
				instructions to download and install this free software.
			</div>
			<br /> <a href="http://www.adobe.com/reader" class="underlink"> <img
				src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/reader.png" /> </a>
          </div>
 
 
     
   <s:action name="xpedxFooter" executeResult="true" namespace="/common" />
 
<!-- end container  -->
</body>
</html>