<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />

<!-- styles -->
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"  href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"  href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/theme-xpedx_v1.2<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<script type="text/javascript">
	
	function getPreviousPageUrl(){	
		window.location.href = "<s:property value='%{itemDtlBackPageURL.substring(itemDtlBackPageURL.indexOf(xpedxSwcContext))}' escape='false'/>";
	}
</script>
<title><s:property value="wCContext.storefrontId" /> - <s:text name='system.error.title' /> </title>
<!-- Start of Webtrends -->
 <Meta name="WT.ti" Content='<s:property value="wCContext.storefrontId" /> - Order Confirmation: Error'>
<!-- End of webtrends -->
</head>
<body class="ext-gecko ext-gecko3">

    <div id="main-container">
        <div id="main">
             <!--s:action name="xpedxHeader" executeResult="true" namespace="/common" /-->
			<s:url id ='homeLink' action='home' namespace='/home' /> 
			<div class="t1-header commonHeader signOnHeader" id="headerContainer" >
				<div class="logo">
					<s:a href="%{homeLink}">&nbsp;</s:a>
				</div> 
        	</div>
            <div class="container"> 
                <!-- breadcrumb -->
                <div id="searchBreadcrumb" >
                <br/>
                	
                	<br/>
                	<br/>
                </div>
                <div id="mid-col-mil">
					<div style="border: 1px solid #CCC; width: auto; padding: 20px;" class="x-corners">
                        <br />
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span> <s:text name='search.produced.system.error.firstline' /></span>
                        <br />
                        <br />
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span> <s:text name='search.produced.system.error.secondline' /></span>
                        <br />
                        <ul>
                        </ul>	
                        <br />
                        <br />
                        <br />
                    </div>

                     <ul class="tool-bar-bottom float-right" id="tool-bar">
	                    	<li><a href="javascript:history.go(-1);" class="grey-ui-btn"><span>Return to Previous Page</span></a></li>
	                  </ul>
                    <div class="clearall"></div>
                    <br />
                    <br />
                    <br />
                    <br />

                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />

                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />

                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                </div>

			</div>
        </div>
    </div>
	<!-- end main  -->
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />   
	
	

	<!-- end container  -->
</body>
</html>