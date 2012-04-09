<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<!-- styles -->

<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/RESOURCES.css" />

<!--[if IE]> --> <!-- UI changes done  -->

<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/IE.css" />
<link rel="stylesheet" type="text/css"
	href="../../xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

<!-- [endif] -->


<!-- javascript -->

<script type="text/javascript" src="/swc/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/catalog/catalogExt.js"></script>

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
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>

<script type="text/javascript" src="/swc/xpedx/js/xpedx-new-ui.js" language="javascript"></script>
<script type="text/javascript" src="/swc/xpedx/css/modals/checkboxtree/jquery.checkboxtree.js"></script>

<script type="text/javascript">
	$(document).ready(function() 
{$(document).pngFix();
$("#varous1").fancybox();
$("#various2").fancybox();
$("#various3").fancybox();
$("#various4").fancybox(); 
});
</script>

<%-- <title><s:property value="wCContext.storefrontId" /> : <s:text name="Resources" /> /  Estimating Files</title>  --%>
<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.MISC.ESTFS.GENERIC.TABTITLE" /> </title> 

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
			<!-- Printable Catalogs & Estimating Files -->
			<s:text name="MSG.SWC.MISC.ESTFS.GENERIC.PGTITLE" />
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
			                          <span><s:property value='#CatalogExp.value'/></span></a>			                     
			                     </li>
			                   
		                     </ul>				
					   </s:iterator> 
					   			  

				   </div> 
			   </td>
			   <td>
			   		<s:if test="%{CatalogExp.size()} <= 0 ">
					   	<%-- <span>No Printable Catalogs & Estimating Files available </span> --%>
					   	<span> <s:text name="MSG.SWC.MISC.ESTFS.ERROR.NOFILESAVAILABLE" /> </span>
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