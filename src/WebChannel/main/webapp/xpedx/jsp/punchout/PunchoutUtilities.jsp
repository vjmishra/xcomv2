<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<s:bean	name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils'	id='wcUtil' />
<s:set name='_action' value='[0]' />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=8" /> 
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />	
	<%
		request.setAttribute("isMergedCSSJS","true");
	%>
	<title>Punchout Utilities</title>
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<!--[if IE]> 
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-ie-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" /> 
	<![endif]--> 
	<!--[if IE]>
		<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ie-hacks<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->
	<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/punchout/PunchoutUtilities<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	
	<style>
		#ajax-output {
			margin-top: 20px;
		}
	</style>

	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<s:include value="../order/XPEDXRefreshMiniCart.jsp"/>	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/punchout/PunchoutUtilities<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
</head>
<body class="ext-gecko ext-gecko3">
	<s:url id='punchoutCreateDataParamURLid' namespace='/punchout' action='ajaxCreatePunchoutOciDataParam' escapeAmp="false" />
	<s:hidden id="punchoutCreateDataParamURL" value="%{#punchoutCreateDataParamURLid}" />
	
	<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
	
	<div>
	     <div class="loading-icon" style="display:none;"></div>
	</div> 
	<div id="main-container">
		<div id="main">
			<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
			<div class="container">
				<s:url id="getAssignedShipTosForChangeShipToURLid" namespace="/common" action="getAssignedShipToCustomers" escapeAmp="false">
					<s:param name="status">30</s:param>
				</s:url>
				<s:hidden id="getAssignedShipTosForChangeShipToURL" value="%{#getAssignedShipTosForChangeShipToURLid}" /> 
				<div class="content-container">
					<h1>Encrypt OCI User ID and Password</h1>
					
					<s:if test="isUserAdmin">
						<form id="dataForm" onsubmit="encryptUserIdAndPassword(); return false;">
							<div class="utility-row">
								<label>User ID:</label>
								<input id="userId" type="text" />
							</div>
							<div class="utility-row">
								<label>Password:</label>
								<input id="password" type="password" />
							</div>
							<div class="utility-row">
								<input value="Generate URL" type="submit" class="btn-neutral" />
							</div>
						</form>
		      			
		      			<div id="ajax-output">
		      				<%-- dynamically populated by javascript --%>
		      			</div>
					</s:if>
					<s:else>
						This tool is only available to admin users.
					</s:else>
				</div>
			</div> <%-- / container --%>
		</div> <%-- main --%>
	</div> <%-- / main-container --%>
	<div class="loading-wrap"  style="display:none;">
         <div class="load-modal" ></div>
    </div>
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" /> 
</body>
</html>
