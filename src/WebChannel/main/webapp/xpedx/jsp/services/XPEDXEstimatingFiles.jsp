<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->

<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- jQuery -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
    
    <link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
     
      <script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
 
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript"></script> 
 
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
			 <!-- Added for EB-1614 Remove any associations to estimating files Starts -->
				<s:set name='storefrontId' value="wCContext.storefrontId" />
				  <s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storefrontId)}'>
				<s:text name="MSG.SWC.MISC.ESTFS.GENERIC.PGTITLE" />
				</s:if>
				<s:elseif test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>
						<s:text name="MSG.SWC.MISC.ESTFS.GENERIC.PGTITLE_SAALFELD" />
				</s:elseif> 
			  <!--  EB-1614 End -->
		</div>
    <div class="x-input margin-top2">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" >
           
          	<tr>
          	<div class="estimators">
           		 <td>
           		 <%-- Print Estimator for <s:property value='%{organizationName}'/> - <s:property value='%{pricingWareHouse}'/><br /> --%>
					
	                     <s:iterator id='CatalogExp' value='%{CatalogExp}'>
						   <ul>
								<%-- 			                     
								<li>
				                     <span><a href="<s:property value='#CatalogExp.value'/>">
				                     <img height="12" width="12" border="0" alt="Download" title="Download" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/download_12x12_grey.png"></a></span>
				                     <span><s:property value='#CatalogExp.key'/></span>			                     
				                 </li> 
			                     --%>
			                     <%-- jira 3709--%>
			                      <li>
			                          <span><a href="<s:property value='#CatalogExp.key'/>" target="_blank"></span>
			                          <span><s:property value='#CatalogExp.value'/></span></a>			                     
			                     </li>
			                   
		                     </ul>	<ul>&nbsp;</ul>			
					   </s:iterator> 
					   			  
			   </td>
			   </div> 
			   <td>
			   		<s:if test="%{CatalogExp.size() <= 0 }">
					   	<%-- <span>No Printable Catalogs & Estimating Files available </span> --%>
					  <!-- Added for EB-1614 Remove any associations to estimating files Starts -->
						<s:set name='storefrontId' value="wCContext.storefrontId" />
				 		 <s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storefrontId)}'>
						   	<span> <s:text name="MSG.SWC.MISC.ESTFS.ERROR.NOFILESAVAILABLE" /> </span>
						</s:if>
						<s:elseif test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>
						<span> <s:text name="MSG.SWC.MISC.ESTFS.ERROR.NOFILESAVAILABLE_SAALFELD" /> </span>
						</s:elseif> 
					 <!--  EB-1614 End -->	
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