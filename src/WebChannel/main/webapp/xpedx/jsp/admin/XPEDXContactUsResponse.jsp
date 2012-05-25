<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<s:set name='_action' value='[0]' />
<s:set name='isGuestUser' value="wCContext.guestUser" />
<s:set name="xutil" value="XMLUtils" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />


       
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-1.css" />
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc.min.css" />
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/home.css" />
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/portalhome.css" />

    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/narrowBy.css" />
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/catalogExt.css" />
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/styles.css" />
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all.css" />

		<s:include value="../common/XPEDXStaticInclude.jsp"/>

    <!-- jQuery -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>

    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
    <!-- Lightbox/Modal Window -->
    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>
    <link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
	
	<title><s:property value="wCContext.storefrontId" /> - Contact Us</title>
</head>

<body class="ext-gecko ext-gecko3">
	<div id="main-container">
		<div id="main" class="anon-pages">
		
			<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
			
			<h1>Contact Us</h1>
			
			
				<s:set name="contactUsResponse" value="contactUsResp" />
					
				 <div class="anon-home-top-left ">
		         	<br />
		         	<s:url id='contactUsLink' namespace="/common" action='xpedxContact'>					
					</s:url>
					<s:if test='#contactUsResponse != null'>
			        	<s:property value='%{#contactUsResponse}'/>
			   		</s:if>
	           		<s:a href="%{contactUsLink}" tabindex="2503">Return to Contact Us</s:a>
			     </div>
			
  		</div>
	</div>
	<!-- end container  -->
	
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />            

</body>
</html> 
	