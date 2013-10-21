<%@ page language="java" contentType="text/html; charset=UTF-8"
			pageEncoding="UTF-8" %>
<%@ page import="com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>

<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<s:set name="storefront" value='wCContext.storefrontId'/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta content='IE=8' http-equiv='X-UA-Compatible' />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value='wCContext.storefrontId' />/css/sfskin-<s:property value='wCContext.storefrontId' /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
	
	<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/IE.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value='wCContext.storefrontId' />/css/sfskin-ie-<s:property value='wCContext.storefrontId' /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->
	
	<title><s:property value="#storefront"></s:property></title>
	<!-- Webtrends Start -->
	<meta name="WT.ti" content="Terms of Access Modal - <s:property value='#storefront' />" />
	<!-- Webtrends End -->
</head>
<body class="ext-gecko ext-gecko3 toaModalBody">
	<div id="main-container">
		<div id="main">
		<div class="container">
			<!-- breadcrumb -->
			<div id="mid-col-mil">
				<div>
					<div class="padding-top3 black bold" style= 'text-align:center'>
					</div>
				</div>
				
				<s:form name="toaform" namespace="/common" action="XPEDXRegisterUserTOA" onsubmit="return false;">
				<s:hidden name="toaChecked" id="toaChecked" value="Y" />
					<s:if test="#storefront == 'xpedx'">
						<jsp:include page="../common/sharedTermsOfAccess_xpedx.jsp">
							<jsp:param name="modal" value="true" />
						</jsp:include>
					</s:if>
					<s:if test="#storefront == 'Saalfeld'">
						<jsp:include page="../common/sharedTermsOfAccess_Saalfeld.jsp">
							<jsp:param name="modal" value="true" />
						</jsp:include>
					</s:if>

				</s:form>
				
			</div>
			<!-- End Pricing -->
			<br />
		</div>
		<!-- end main --> 
		</div>
	</div>
	<!-- end container -->
	
	<s:include value="../../htmls/webtrends/webtrends.html"/><!--EB-519-->
</body>
</html> 
