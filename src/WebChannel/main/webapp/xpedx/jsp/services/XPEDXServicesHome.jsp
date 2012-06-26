<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en"
	xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />



<!-- styles -->

<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc.min<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-dan<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/nav<s:property value='#wcUtil.xpedxBuildKey' />.css" />


<!-- jQuery -->
<link type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all<s:property value='#wcUtil.xpedxBuildKey' />.css" rel="stylesheet" />



<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- // XNGTP-524 : for Adding Calendar icon for Order Date and validating Order Date -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.datepicker<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
	$(function() {
		$(".datepicker").datepicker({
			showOn: 'button',
						numberOfMonths: 1,

			buttonImage: '<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/calendar-icon.png',
			buttonImageOnly: true
		});
	});
</script>
<script type="text/javascript">
	function submitForm() {
		document.getElementById("returnItemComplaintForm").submit();
		return true;	
	}
	// XNGTP-524 : for validating Order Date
	function ValidateDate(){
		var dt=document.returnItemComplaintForm.orderDate;
				
		if (dt.value!= null && dt.value.length>0){
			dt.value = (dt.value).replace(/^\s*|\s*$/g,'');
			if (dt.value.length>0 && isDate(dt.value)==false){				
				document.getElementById("orderDate").focus();
				return false;
			}
		}		
	    return true;
	 }
</script>

<title><s:property value="wCContext.storefrontId" /> - <s:text name="Resources" /></title>
<meta name="WT.ti" Content='<s:text name="Resources" />'>
<style>
.ui-datepicker-trigger {
	margin-left:470px;
	margin-top:-25px; } 
	
	div.demo {
	padding:0px;
	margin-left:370px;
	}

</style>
</head>

<s:set name='_action' value='[0]' />
<body class="ext-gecko ext-gecko3">
<div id="main-container">
<div id="main"><s:action name="xpedxHeader" executeResult="true"
	namespace="/common" />




<div id="tools" class="container"><!-- breadcrumb -->
<div id="searchBreadcrumb"><a href="<s:url action="home" namespace="/home" includeParams='none'/>">Home</a> / <span class="page-title"><s:text name="Resources" /></span></div>

<div id="mid-col-mil">


<div style="width: 475px; margin-right: 20px;" class="float-left">
<h2 class="table-hdr"><s:text name="Resources" /></h2>
<table style="height: 360px;">
	<tbody>
		<tr class="table-header-bar">
			<td width="95%" align="left"
				class="no-border table-header-bar-left reg-no-border-right"><span
				class="white">Reporting</span></td>
			<td align="left" class="no-border-right table-header-bar-right"></td>
		</tr>
		<tr>

			<td valign="top" class="reg-no-border-right">
			<ul>
				<li><a href="#">Customer Standard Reports</a></li>
				<li><a href="#">Order Summary</a></li>
				<li><a href="#">Order Detail</a></li>
			</ul>
			<br>

			<ul>
				<li><a href="#">Item Summary</a></li>
				<li><a href="#">Item Summary - No Order Detail</a></li>
				<li><a href="#">Item Summary - by Manufactures</a></li>
				<li><a href="#">Item Summary - by Manufacturer no Freight</a></li>
				<li><a href="#">Item Summary - Data export</a></li>

				<li><a href="#">Item Summary - My Items Data</a></li>
			</ul>
			<br>
			<ul>
				<li><a href="#">Certification - COC - Order Detail</a></li>
				<li><a href="#">Certification - COC - Item Summary - by
				Manufacturer no Freight</a></li>
				<li><a href="#">Certification - LEED</a></li>

				<li><a href="#">Certification - Green - Order Detail</a></li>
				<li><a href="#">Certification - Green - Item Summary - by
				Manufacturer</a></li>
			</ul>
			</td>
			<td class="reg-no-border-left"></td>
		</tr>
	</tbody>
</table>

<div id="table-bottom-bar">
<div id="table-bottom-bar-L"></div>
<div id="table-bottom-bar-R"></div>
</div>
</div>
<div style="width: 455px; margin-bottom: 20px;" class="float-left">
<table>
	<tbody>
		<tr class="table-header-bar">

			<td width="95%" align="left"
				class="no-border table-header-bar-left reg-no-border-right"><span
				class="white">Samples</span></td>
			<td align="left" class="no-border-right table-header-bar-right"></td>
		</tr>
		<tr>
			<td valign="top" class="reg-no-border-right"><s:set
				name="canRequestProductSample"
				value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@canRequestSamples(wCContext.customerId, wCContext.storefrontId)" />

			<s:if
				test='#canRequestProductSample == "Y" || #canRequestProductSample == "y" '>
				<s:url id='RequestProdSampleLink' namespace='/xpedx/services'
					action='XPEDXServices'></s:url>
				<s:a href="%{RequestProdSampleLink}">Request Product Sample</s:a>
			</s:if></td>
			<td class="reg-no-border-left"></td>

		</tr>
	</tbody>
</table>
<div id="table-bottom-bar">
<div id="table-bottom-bar-L"></div>
<div id="table-bottom-bar-R"></div>
</div>
</div>
<div style="width: 455px; margin-bottom: 20px;" class="float-left">

<table>
	<tbody>
		<tr class="table-header-bar">
			<td width="95%" align="left"
				class="no-border table-header-bar-left reg-no-border-right"><span
				class="white">Product File Extract</span></td>
			<td align="left" class="no-border-right table-header-bar-right"></td>
		</tr>
		<tr>
			<td valign="top" class="reg-no-border-right"><a href="#">View
			Product File Extract</a></td>
			<td class="reg-no-border-left"></td>
		</tr>
	</tbody>
</table>
<div id="table-bottom-bar">
<div id="table-bottom-bar-L"></div>

<div id="table-bottom-bar-R"></div>
</div>
</div>
<div style="width: 455px; margin-bottom: 20px;" class="float-left">
<table>
	<tbody>
		<tr class="table-header-bar">
			<td width="95%" align="left"
				class="no-border table-header-bar-left reg-no-border-right"><span
				class="white">Printable Catalogs (PDF)</span></td>

			<td align="left" class="no-border-right table-header-bar-right"></td>
		</tr>
		<tr>
			<td valign="top" class="reg-no-border-right">
			<ul>
				<li><a href="#">Category 1</a></li>
				<li><a href="#">Category 2</a></li>
				<li><a href="#">Category 3</a></li>

				<li><a href="#">Category 4</a></li>
				<li><a href="#">Category 5</a></li>
				<li><a href="#">Category 6</a></li>
			</ul>
			</td>
			<td class="reg-no-border-left"></td>
		</tr>

	</tbody>
</table>
<div id="table-bottom-bar">
<div id="table-bottom-bar-L"></div>
<div id="table-bottom-bar-R"></div>
</div>
</div>
<div class="clearall"></div>
<div><s:form id="returnItemComplaintForm"
	name="returnItemComplaintForm" namespace="/xpedx/services"
	method="post" action="returnItemsComplaintRequest"
	cssClass="return-center">
	<table id="x-tbl-cmmn">
		<tbody>
			<tr class="table-header-bar">
				<td width="900" align="left"
					class="no-border table-header-bar-left reg-no-border-right"><span
					class="white"><span class="white">Return Center</span></span></td>
				<td align="left"
					class="no-border-right reg-no-border-left table-header-bar-right"
					colspan="2"></td>
			</tr>
			<tr>
				<td valign="top" class="reg-no-border-right">

				<p><b>Indicate whether this incident is a return request or
				a complaint about product quality.</b></p>
				<br />
				<s:radio list="{' Return',' Complaint'}" name="requestType"
					id="requestType"></s:radio>

				<div
					style="border-bottom: 1px solid #cccccc; width: 860px; margin-top: 20px;"></div>
				<br />
				<div>
				<ul>
					<li>
					<p>Item Number:</p>
					<s:textfield name="itemNumber" id="itemNumber"
						cssStyle="width: 125px;" cssClass="x-input" /></li>
					<li>

					<p>Description:</p>
					<s:textfield name="description" id="description" cssClass="x-input"
						cssStyle="width: 325px;" /></li>
					<li>
					<p>Quantity</p>
					<s:textfield name="itemQTY" id="itemQTY" cssStyle="width: 50px;"
						cssClass="x-input" /></li>
					<li>

					<p>Unit of Measure</p>
					<s:textfield name="itemUOM" id="itemUOM" cssStyle="width: 125px;"
						cssClass="x-input" /></li>
					<li>
					<p>Reason for Return</p>
					<s:autocompleter cssStyle="width: 129px;" name="reasonForReturn"
						id="reasonForReturn" headerValue="Select One" headerKey="1"
						list="{'Damaged during Delivery','Others'}" theme="simple" /></li>
				</ul>
				<div class="clearall"></div>
				</div>
				<div class="float-left" style="margin-right: 20px;">
				<p>Reason for Return or Complaint:</p>
				<s:textarea name="txtReason" id="txtReason"
					cssStyle="width: 576px; height: 110px;" cssClass="x-input" /></div>
				<p>Order Number:</p>
				<s:textfield name="orderNumber" id="orderNumber"
					cssStyle="width: 215px;" cssClass="x-input" />
				<p>Order Date</p>
				<div class="demo">
					<s:textfield name="orderDate" id="orderDate" onBlur="return ValidateDate()"
					cssStyle="width: 215px;" cssClass='x-input datepicker' /> <br />
				</div>
				<br />
				<a href="javascript:submitForm();" class="grey-ui-btn"><span>Request
				Return/Send Complaint</span></a></td>
				<td valign="top" align="left" style="padding: 0pt;"
					class="reg-no-border-left">&nbsp;</td>
			</tr>
		</tbody>
	</table>
</s:form>
<div id="table-bottom-bar">

<div id="table-bottom-bar-L"></div>
<div id="table-bottom-bar-R"></div>
</div>
</div>
</div>
</div>
</div>
</div>
<!-- end main  -->

<!-- end main  -->
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
<!-- end container  -->
</body>
</html>