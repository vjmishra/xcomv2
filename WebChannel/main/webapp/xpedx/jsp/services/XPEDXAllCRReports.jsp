<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ page import="java.util.* "%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />



<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/home/home.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/home/portalhome.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/catalog/narrowBy.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/catalog/catalogExt.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet" href="./swc/xpedx/css/global/ext-all.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/swc.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/theme-xpedx_v1.2.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-mil.css" /> 
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-mil-new.css" /> 
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-quick-add.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/ext-all.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-dan.css"/>

<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/swc.min.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/home/portalhome.css" />
<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/nav.css" />	

<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/prod-details.css"/>
 

<!-- javascript -->

<script type="text/javascript" src="/swc/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/catalog/catalogExt.js"></script>

<!-- carousel scripts css  -->

<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/js/jcarousel/skins/xpedx/theme.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/js/jcarousel/skins/xpedx/skin.css" />

<!-- carousel scripts js   -->

<script type="text/javascript" src="/swc/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery-1.4.2.min.js"></script>

<script type="text/javascript" src="/swc/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>

<script type="text/javascript" src="/swc/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/quick-add/quick-add.js"></script>

    <!-- Lightbox/Modal Window -->
    <script type="text/javascript" src="<s:url value="/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js" />"></script>
    <script type="text/javascript" src="<s:url value="/xpedx/js/fancybox/jquery.fancybox-1.3.4.js" />"></script>
<script type="text/javascript" src="/swc/xpedx/js/xpedx-new-ui.js" language="javascript"></script>
<link rel="stylesheet" type="text/css" href="/swc/xpedx/js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom.css" media="screen" />
<link rel="stylesheet" type="text/css" href="<s:url value="/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" />" media="screen" />
    
<!--   This is Line Separator  -->
<!-- Web Trends tag start -->
<script type="text/javascript" src="/swc/xpedx/js/webtrends/displayWebTag.js"></script>
<!-- Web Trends tag end  -->



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
<title>Services</title>
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
          </div>
 
 
     
   <s:action name="xpedxFooter" executeResult="true" namespace="/common" />
 
<!-- end container  -->
</body>
</html>