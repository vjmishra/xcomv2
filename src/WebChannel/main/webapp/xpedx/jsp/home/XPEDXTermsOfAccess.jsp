<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<s:set name='isGuestUser' value="wCContext.guestUser" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!-- styles -->
<s:if test="#isGuestUser == false">

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
</s:if>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MISC<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/IE.css" />
	<![endif]-->
	
<!-- javascript -->


					
<s:if test="#isGuestUser == true">
	
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
</s:if>
	
	
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

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

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


	

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.maskedinput-1.3<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- Lightbox/Modal Window -->


<script type="text/javascript">
$(document).ready(function() {
	$(document).pngFix();
	$("#video-tour").fancybox({
		'titleShow'			: false,
    	'transitionIn'		: 'fade',
		'transitionOut'		: 'fade'
	});
	$("#video-tour2").fancybox({
		'titleShow'			: false,
		'transitionIn'		: 'fade',
		'transitionOut'		: 'fade'
	});
});
</script>
  
<title><s:property value="wCContext.storefrontId" /> /  <s:text name="MSG.SWC.MISC.TERMS.GENERIC.TABTITLE"/> </title>
	<!--WebTrands Start -->
	<meta name="WT.ti" content="<s:property value="wCContext.storefrontId" /> / Terms of Access" /> 
	<!--WebTrands End -->
</head>
    <s:set name='isGuestUser' value="wCContext.guestUser" />
    <s:set name='_action' value='[0]' />
    
    <body class="ext-gecko ext-gecko3">
		<div id="main-container">
			<div id="main">
				
			<s:action name="xpedxHeader" executeResult="true" namespace="/common" />

			<div class="container">
				<!-- breadcrumb -->

				<div id="mid-col-mil">

					<div>
						<div class="padding-top2 float-right">
							<a href="javascript:window.print()" class="underlink"><span class="print-ico-xpedx underlink"><img
									height="15" width="16" alt="Print This Page"
									src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif">Print Page
							</span>
							</a>
						</div>
						<div class="padding-top3 black">
						</div>

					</div>
					
					<s:if test="wCContext.storefrontId == 'xpedx'">
						<jsp:include page="../common/sharedTermsOfAccess_xpedx.jsp">
							<jsp:param name="modal" value="false" />
						</jsp:include>
					</s:if>
					<s:if test="wCContext.storefrontId == 'Saalfeld'">
						<jsp:include page="../common/sharedTermsOfAccess_Saalfeld.jsp">
							<jsp:param name="modal" value="false" />
						</jsp:include>
					</s:if>
					
				</div> 
				<!-- End Pricing -->
				<br />
			</div>

			<!-- end main  --> 
			</div>
	
		</div>
		<!-- end container  -->  
          
		<s:action name="xpedxFooter" executeResult="true" namespace="/common" />            
	</body>
</html>
