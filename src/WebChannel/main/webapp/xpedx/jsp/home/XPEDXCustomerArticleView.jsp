<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>


<s:set name='_action' value='[0]' />
<s:set name="xutil" value="XMLUtils" />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/home/home.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/home/portalhome.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/catalog/narrowBy.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/catalog/catalogExt.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/ext-all.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/swc.css" />

<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-mil-new.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-quick-add.css"/>
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

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="/swc/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<script src="/swc/xpedx/js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="/swc/xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="/swc/xpedx/js/xpedx-new-ui.js" language="javascript">
	
</script>


<script type="text/javascript" src="/swc/xpedx/js/jquery-tool-tip/jquery-ui.min.js"></script>

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.fancybox-1.3.1.js"></script>

<link rel="stylesheet" type="text/css" href="/swc/xpedx/js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/swc/xpedx/js/fancybox/jquery.fancybox-1.3.1.css" media="screen" />

<script src="/swc/xpedx/js/SpryTabbedPanels.js" type="text/javascript"></script>
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
<%--  <title><s:property value="wCContext.storefrontId" /> /  View All Articles</title> --%>
 <title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.NEWSARTL.LISTALL.GENERIC.TABTITLE" /> </title>

</head>
<!-- END swc:head -->
<body class="ext-gecko ext-gecko3">
<div id="main-container">
  <div id="main">

	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
    
    <div class="container">
      <!-- breadcrumb -->
	  
      <div id="mid-col-mil">
	    <div> <div class="float-right padding-right8 padding-top3 underlines"><a href="#">View Apple News</a></div>
     	<div class="padding-top3 black"><strong class="black"><s:property value="wCContext.storefrontId" /> News</strong></div>
    	</div> <div class=" padding-bottom clearview"> </div>
                  
        <table style="width:100%;">
          <tbody>
				<s:iterator value='articleLines' id='articleLine' status="articleLineCount">
					<s:set name="ArticleKey" value='#xutil.getAttribute(#articleLine,"ArticleKey")' />
					<s:set name="ArticleName" value='#xutil.getAttribute(#articleLine,"ArticleName")' />
					<s:set name="Article" value='#xutil.getAttribute(#articleLine,"Article")' />
					<s:set name="Createts" value='%{#util.formatDate(#xutil.getAttribute(#articleLine,"Createts"),wCContext)}' />
				<tr>
                <td  class="underlines no-border-right-user">
                <a href="<s:url namespace='/profile/user' action='xpedxReadArticle'><s:param name="articleKey" 
                	value='%{#ArticleKey}' /></s:url>" class="underlines" >
                		<strong><s:property value='%{#ArticleName}'/></strong></a><br />
                		<s:property value='%{#Createts}'/><br />
                		<s:property escape='false' value='%{#Article}'/><br />
                	<div class=" padding-bottom4clearview"> </div>
                		<a href="<s:url namespace='/profile/user' action='xpedxReadArticle'><s:param name="articleKey" 
                			value='%{#ArticleKey}' /></s:url>" class="underlines">
                				Read More </a>
                	</td>
              </tr>  
			  </s:iterator>
			  
              
          </tbody>
        </table>

	  <div class=" bottomBorder">&nbsp; </div>
	  <!--  Need to add cutomer specific news  -->


        <div class=" bot-margin"> &nbsp; </div>
        <div class=" bot-margin"> &nbsp;</div>
        </div>  
		
      
</div>
      <!-- End Pricing -->
      <br />
    </div>

    
  </div>

    <s:action name="xpedxFooter" executeResult="true" namespace="/common" />

</body>
</html>