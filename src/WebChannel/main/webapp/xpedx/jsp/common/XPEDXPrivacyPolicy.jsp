<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<s:set name='isGuestUser' value="wCContext.guestUser" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
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
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/ADMIN<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->

<s:if test="#isGuestUser == true">
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
</s:if>


<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />

<!-- jQuery -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

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
  
<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.MISC.PRPY.GENERIC.TABTITLE" /></title>

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
								<a href="javascript:window.print()"><span class="print-ico-xpedx underlink"><img
										height="15" width="16" alt="Print This Page"
										src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif">Print Page
								</span>
								</a>
							</div>
							<div class="padding-top3 black">
							</div>

						</div>
						<br/>
						<br/>
						
						<table class="full-widths">
							<tbody>
								<s:if test="wCContext.storefrontId == 'xpedx'">
									<jsp:include page="../common/sharedPrivacyPolicy_xpedx.jsp" />
								</s:if>
								<s:if test="wCContext.storefrontId == 'Saalfeld'">
									<jsp:include page="../common/sharedPrivacyPolicy_Saalfeld.jsp" />
								</s:if>
							</tbody>
						</table>

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
