<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.HashMap"%>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="xpedx" uri="xpedx" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>

<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	<meta name="format-detection" content="telephone=no"/>
	
	<title>Sales Representative Search</title> 
	
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/salesrep/style<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/salesrep/sales-rep<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/salesrep/lib<s:property value='#wcUtil.xpedxBuildKey' />.js">
	</script>
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/salesrep/SpryTooltip<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/salesrep/salesRepCustomers<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/scroll-startstop.events.jquery<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/processingIcon<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/navArrows<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<style>
		div a.underlink { text-decoration: none; }
		div a.underlink:hover { text-decoration: underline;}
		#logout-text { float: right; margin-right: 25px; margin-top: 40px;}
	</style>
	
	<script type="text/javascript">
		function clearTextField(field){
			if (field.defaultValue == field.value) {
				field.value = '';
			} else if (field.value == '') { 
				field.value = field.defaultValue;
			}
		}
		var data="false";
	</script>

</head>
<body class="loading">
 	
	 <div id="main">
		<div id="container" class="container content-container">
			
			<div id="scroll-up-down">
				<div style="display: none;" class="nav_up" id="nav_up"></div>
				<div style="display: none;" class="nav_down" id="nav_down"></div>
			</div>
			
			<s:url id='selectedCustURL' namespace='/common'
				action='salesRepCustomerLogin'></s:url>
			<s:hidden id="selectedCustURL" value="%{#selectedCustURL}" />
			<s:url id="getSalesRepCustomerListid" namespace="/common"
				action="getSalesProCustomersList" escapeAmp="false">
				<s:param name="status">30</s:param>
			</s:url>
			<s:hidden id="getSalesRepCustomerURL" value="%{#getSalesRepCustomerListid}" />
	
			<s:url id='logoutURL' namespace='/home' action='saleslogout' includeParams='none' />
			<p class="logout">
				<s:a href="%{#logoutURL}">Log Out</s:a>
			</p>
			
			<h1>Select an Account</h1>
			<p class="addpadbottom20">
				<strong>Note:</strong> Please ensure correct Ship-To has been
				selected once you are logged in. You can view/change Ship-To by
				clicking on customer name on top right corner.
			</p>
			
			<s:set name='_action' value='[0]' />
			<s:set name='wcContext' value="#_action.getWCContext()" />

			<div class="search">
				<div class="ship-search-input-wrap">
					<form class="filterform" action="#">
						<input id="searchTerm" class="filterinput input-watermark-color" type="text" data-watermark="Search" />
						<span class="resetlink">
							<a href="#">Reset</a>
						</span>
					</form>
				</div>
			</div>
			
			<div class="table" id="listOfCustomers">
				<%-- dynamically populate data here with salesRepCustomers javascript  --%>
			</div>
			
			<br/>
			<div id="errorMsgForCustomerData" align="center" style="display:none;">
				<font color="red">No customer records found with the above criteria</font>
			</div>
			
		</div> <%-- / container --%>
	</div> <%-- / main --%>
	
	<div class="loading-wrap"  style="display:none;">
		 <div class="load-modal" ><div class="loading-icon" style="display:none;"></div></div>
	</div>

	<div id="strdiv2"></div>
	
	<script type="text/javascript">
		if(data == "true") {
			document.getElementById("errorMsgForCustomerData").style.display ='block';
		} else {
			document.getElementById("errorMsgForCustomerData").style.display ="none";
		}
	</script>
	
</body>
</html>
