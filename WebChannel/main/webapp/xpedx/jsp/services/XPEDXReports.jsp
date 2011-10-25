<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/global-1.css" />
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
 







<script type="text/javascript">
	$(document).ready(function() 
{$(document).pngFix();
$("#varous1").fancybox();
$("#various2").fancybox();
$("#various3").fancybox();
$("#various4").fancybox(); 
});
	

	
</script>
<style type="text/css">
p{margin-bottom:8px;}

.container {
	min-height: 770px;
}
 </style>
 

<!-- Facy Box (expand and collpse Modal Window -->

<title><s:property value="wCContext.storefrontId" /> : <s:text name="Resources" /> /  Reporting</title> 
<!-- Webtrends tag starts -->
<Meta name="WT.ti" Content='xpedx : <s:text name="Resources" /> /  Reporting'>
<!-- Webtrends tag stops -->

</head>
<s:set name='_action' value='[0]' />
<!-- END swc:head -->
<body class="ext-gecko ext-gecko3">
<div id="main-container">

  <div id="main">
  
  
  
	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />

   
    <form id="contact_form" name="contact_form" method="post" action="#">
	<div class="container">
      <div id="mid-col-mil"> 
        <div>
        
     </div>
       
        <form id="request_form" name="request_form" method="post" action="#">

        
        <div class="clearview"> 
			&nbsp;
		</div>
		
		
		<div class="padding-bottom3"> 
			<strong>Reports </strong>
		</div>
		
        <div>
               <div class="x-input underlines  ">
                 <div class=" padding-all2"> 
                   <p><h4><a href="#">Order History - Details</a></h4>
                            Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Praesent aliquam, justo convallis 
                          <p><h4><a href="#">Customer Sales Metrics</a></h4>
                            Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Praesent aliquam, justo convallis luctus rutrum, lacus.</p>
                        <p><h4><a href="#">Purchase History</a></h4>
                            Lorem ipsum dolor sit amet, consectetuer adipiscing elit.  
                          <p><h4><a href="#">Customer Sales Metrics</a></h4>
                            Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</p>
                        <p><h4><a href="#">Purchase History - Details</a></h4>
                            Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Praesent aliquam, justo convallis luctus rutrum, erat nulla fermentum diam, lacus.</p>
                        <p><h4><a href="#">Purchase History by Ship-to Location</a></h4>
                          Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Praesent aliquam, justo convallis luctus rutrum, erat nulla fermentum diam, at nonummy</p> <p><h4><a href="#">Trended Purchase History </a></h4>
                            Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Praesent aliquam, justo convallis luctus rutrum, erat nulla fermentum diam, at nonummy</p> 
                            
                        <p><h4><a href="#">Invoice History</a></h4>
                            Dolor sit amet, consectetuer adipiscing elit. Praesent aliquam, justo convallis luctus rutrum, erat nulla fermentum diam, at nonummy</p> 
                            
                        <p><h4><a href="#">Certification</a></h4>
                            Adipiscing elit. Praesent aliquam, justo convallis luctus rutrum, erat nulla fermentum diam, at nonummy</p> 
                            
                          <p><h4><a href="#">Customer Report-1 </a></h4>
                        Consectetuer adipiscing elit. Praesent aliquam, justo convallis luctus rutrum, erat nulla fermentum diam, at nonummy</p> 
                    </div>  </div> </div>
        
      
          <div class="clearview"> &nbsp;</div>

         
        <div class="clearview"> &nbsp;</div>
           
         
                          
          
           </form>
        <!-- Pricing -->

         
        <div class="clearview">&nbsp;</div>
         
      </div>
	  </div>
    </form>
    <!-- End Pricing -->
    <br />
  </div>
 
 
     
    <!-- end main  -->

 <s:action name="xpedxFooter" executeResult="true" namespace="/common" />
 
<!-- end container  -->
</body>
</html>