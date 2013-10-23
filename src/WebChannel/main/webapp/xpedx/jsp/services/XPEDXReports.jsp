<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%request.setAttribute("isMergedCSSJS","true");%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript"></script>
 







<script type="text/javascript">
	$(document).ready(function() 
{$(document).pngFix();
$("#varous1").fancybox();
$("#various2").fancybox();
$("#various3").fancybox();
$("#various4").fancybox(); 
});

<!-- Facy Box (expand and collpse Modal Window -->

<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.MISC.REPT.GENERIC.TABTITLE" /> </title> 
<!-- Webtrends tag starts -->
<Meta name="WT.ti" Content='<s:property value="wCContext.storefrontId" /> : <s:text name="Resources" /> /  Reporting'>
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
			<strong><s:text name="MSG.SWC.MISC.REPT.GENERIC.PGTITLE" /> </strong>
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
          	<div class="inf"> Reports can be saved and printed using Adobe PDF or Microsoft Excel.<br/>
          	If you do not have Adobe Reader, click the link below and follow the instructions to download and install this free software.</div>
          	<br/>
          	<a href="http://www.adobe.com/reader" class="underlink">
          		<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/reader.png"/>	
       		</a>

         
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