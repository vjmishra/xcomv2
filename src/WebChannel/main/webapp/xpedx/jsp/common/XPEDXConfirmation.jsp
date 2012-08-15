<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%request.setAttribute("isMergedCSSJS","true");%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<Meta name="DCSext.w_x_req_s" Content="1">

<!-- styles -->

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MISC.css" />
<s:set name='wcContext' value="wCContext" />
<s:set name='isGuestUser' value="wCContext.guestUser" />
<s:if test="#isGuestUser != true">		

	
<!-- javascript -->

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />

<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/IE.css" />
	<![endif]-->
	
</s:if>
<s:else>
<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/IE.css" />
	<![endif]-->

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />


</s:else>	

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header.js"></script>		
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder.js"></script>
				

<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

			


<!-- jQuery -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.widget.js"></script>


	

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.maskedinput-1.3.js"></script>

<%-- <title><s:text name='myitemslists.title' /></title> --%>
<title><s:property value="wCContext.storefrontId" /> / <s:text name='MSG.SWC.MISC.CONFIRMATION.GENERIC.TABTITLE' /></title>

</head>
<!-- END swc:head -->


<!-- CODE_START - Global Vars -PN -->

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
   	This is to avoid a defect in Struts that?s creating contention under load. 
   	The explicit call style will also help the performance in evaluating Struts? OGNL statements. 
   	--%>
<s:set name='_action' value='[0]' />

<!-- CODE_END - Global Vars -PN -->

<body class="  ext-gecko ext-gecko3">
<div id="main-container">
	<div id="main" class="anon-pages">
	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
    	<div class="container">
      	<!-- breadcrumb -->
      		<div id="mid-col-mil"> 
		    	<div>
      				<div class="padding-top3  page-title black"><strong class="black"><s:text name="MSG.SWC.MISC.CONFIRMATION.GENERIC.PGTITLE"/></strong></div>
				</div>
				<div class=" padding-bottom clearview"> </div>
				<s:set name="messageType" value='%{#_action.getMessageType()}'/>
				<s:if test="%{#messageType == 'NewUser'}">
					<p> <s:text name='MSG.SWC.MISC.NEWUSERCONFIRMATION.GENERIC.GENINFO' /> </p> 
				</s:if>
				<s:elseif test="%{#messageType == 'sampleRequest'}">
					<p> <s:text name='MSG.SWC.MISC.SAMPLEREQUESTCONFIRMATION.GENERIC.GENINFO' /> </p>       
				</s:elseif>
				<s:elseif test="%{#messageType == 'returnItemsRequest'}">
					<p> <s:text name='MSG.SWC.MISC.RETURNREQUESTCONFIRMATION.GENERIC.GENINFO' /> </p>       
				</s:elseif>
				
			</div>
		</div>
	</div>
</div>
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
</body>
</html>
