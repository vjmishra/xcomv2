<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

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
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/swc.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/theme-xpedx_v1.2.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" />.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-mil-new.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-quick-add.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/prod-details.css"/>
 <s:include value="../common/XPEDXStaticInclude.jsp"/>

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

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.fancybox-1.3.1.js"></script>

<link rel="stylesheet" type="text/css" href="/swc/xpedx/js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/swc/xpedx/js/fancybox/jquery.fancybox-1.3.1.css" media="screen" />
<script type="text/javascript" src="/swc/xpedx/js/xpedx-new-ui.js" language="javascript"></script>


<link rel="stylesheet" type="text/css" href="/swc/xpedx/css/modals/checkboxtree/demo.css"/>
<link rel="stylesheet" type="text/css" href="/swc/xpedx/css/modals/checkboxtree/jquery.checkboxtree.css"/>
<script type="text/javascript" src="/swc/xpedx/css/modals/checkboxtree/jquery.checkboxtree.js"></script>


<!-- jQuery -->
<link type="text/css" href="/swc/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all.css" rel="stylesheet" />



<script type="text/javascript">
	$(document).ready(function() 
{$(document).pngFix();
$("#varous1").fancybox();
$("#various2").fancybox();
$("#various3").fancybox();
$("#various4").fancybox(); 
});
	
	
	
</script>

 

<!-- Facy Box (expand and collpse Modal Window -->

<title><s:property value="wCContext.storefrontId" /> : <s:text name="Resources" /> /  Estimating Files</title> 
<style>
.ui-datepicker-trigger {
	margin-left:470px;
	margin-top:-25px; } 
	
	div.demo {
	padding:0px;
	margin-left:370px;
	}

.container {
	min-height: 770px;
}  




</style>

</head>
<!-- END swc:head -->
<s:set name='_action' value='[0]' />

<body class="ext-gecko ext-gecko3">
<div id="main-container">
  <div id="main">
  
  
     <s:action name="xpedxHeader" executeResult="true" namespace="/common" />
	
	
    <form id="contact_form" name="contact_form" method="post" action="#">

	
		<div class="container">
		<div id="mid-col-mil"><br/>
        <div class="page-title"> 
			Printable Catalogs & Estimating Files
		</div>
    <div class="x-input margin-top2">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" >
           
          	<tr>
           		 <td >
           		 <%-- Print Estimator for <s:property value='%{organizationName}'/> - <s:property value='%{pricingWareHouse}'/><br /> --%>
					<div class="estimators">
	                     <s:iterator id='CatalogExp' value='%{CatalogExp}'>
						   <ul>
								<%-- 			                     
								<li>
				                     <span><a href="<s:property value='#CatalogExp.value'/>">
				                     <img height="12" width="12" border="0" alt="Download" title="Download" src="/swc/xpedx/images/download_12x12_grey.png"></a></span>
				                     <span><s:property value='#CatalogExp.key'/></span>			                     
				                 </li> 
			                     --%>
			                     
			                      <li>
			                          <span><a href="<s:property value='#CatalogExp.key'/>">
			                          <img height="12" width="12" border="0" alt="Download" title="Download" src="/swc/xpedx/images/download_12x12_grey.png"></a></span>
			                          <span><s:property value='#CatalogExp.value'/></span>			                     
			                     </li>
			                   
		                     </ul>				
					   </s:iterator> 
					   			  

				   </div> 
			   </td>
			   <td>
			   		<s:if test="%{CatalogExp.size()} <= 0 ">
					   	<span>No Printable Catalogs & Estimating Files available </span>
					 </s:if>
			   </td>
          </tr>
        </table> 
 
</div>
        
 
          
           <div class="clearview">&nbsp;</div>
        
        <!-- Pricing -->
         
        <div class="clearview">&nbsp;</div>
         
      </div>
	   </div>
    </form>
    <!-- End Pricing -->
    <br />
  </div>
</div>
</div>
     
    <!-- end main  -->

 <s:action name="xpedxFooter" executeResult="true" namespace="/common" />
<!-- end container  -->
</body>
</html>