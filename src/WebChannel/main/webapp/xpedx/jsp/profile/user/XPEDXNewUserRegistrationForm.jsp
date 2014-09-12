<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%
	request.setAttribute("isMergedCSSJS", "true");
%>
<s:bean
	name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils"
	id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en"
	xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />

<!-- styles -->

<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MISC<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->

<!-- javascript -->


<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]> 
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-ie-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" /> 
<![endif]-->

<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css"
	media="screen" />

<!-- jQuery -->
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Lightbox/Modal Window -->
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>




<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.maskedinput-1.3<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		var xpedxVideoSRC = "";
		$(document).pngFix();
		$('.phone-numeric').numeric(false);
		$("#newUserPhone").mask("999 999-9999");
		$("#selectVideoToPlay").fancybox({
			'autoDimensions' : false,
			'width' : 700,
			'height' : 394,
			'scrolling' : 'no',
			'padding' : 10,
			'onComplete' : function() {
				xpedxVideoSRC = $('#viewVideoDlg iframe').attr('src');
			},
			'onClosed' : function() {
				$('#viewVideoDlg iframe').attr('src', xpedxVideoSRC);
			},
			'onCleanup' : function() {
				var myContent = this.href;
				$(myContent).unwrap();
			}
		});
		$("#cancelId").click(function() {
			var url = $('#homePageId').val();
			window.location.href = url;
			return false;
		});
	});
</script>
<script type="text/javascript">
	function userRegSubmit() {
		//var errorDiv = document.getElementById("errorMsg");
		var errorDiv = document.getElementById("errorMsgForMandatoryFields");
		document.getElementById('newUserName').value = document
				.getElementById('newUserFirstName').value;

		if (document.getElementById('newUserAccountNumber').value.trim().length == 0
				||document.getElementById('newUserFirstName').value.trim().length == 0
				|| document.getElementById('newUserLastName').value.trim().length == 0
				|| document.getElementById('newUserCompanyName').value.trim().length == 0
				|| document.getElementById('newUserEmail').value.trim().length == 0
				|| document.getElementById('newUserPhone').value.trim().length == 0
				|| document.getElementById('newUserAddress1').value.trim().length == 0
				|| document.getElementById('newUserState').value.trim().length == 0
				|| document.getElementById('newUserCity').value.trim().length == 0
				|| document.getElementById('newUserZipCode').value.trim().length == 0) {
			//alert("Fields can't be empty, Input required");
			//errorDiv.innerHTML = "<s:text name='MSG.SWC.MISC.REGISTER.GENERIC.INVALIDFIELDS' />";

			errorDiv.innerHTML = "<s:text name='MSG.SWC.MISC.REGISTER.GENERIC.INVALIDFIELDS' />";
			//usernameField.style.borderColor="#FF0000";
			errorDiv.style.display = 'inline';
			return;
		}
		//added for email validation
		if (document.getElementById("newUserEmail") != null) {
			var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[(2([0-4]\d|5[0-5])|1?\d{1,2})(\.(2([0-4]\d|5[0-5])|1?\d{1,2})){3} \])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			var email = document.getElementById("newUserEmail").value;
			if (!re.test(email)) {

				alert("Please enter valid Email address!");

				//document.getElementById("errorMsgFor_validemailId").style.display = "inline";
				return;
			}

			len = email.length;
			var no = 0;
			for (var i = 0; i < len; i++) {
				if (email.charAt(i) == "@" || email.charAt(i) == ",") {
					no = no + 1;
				}
			}

			if (email.indexOf("@") < 0) {

				alert("Please enter valid Email address!");
				document.getElementById("newUserEmail").focus();
				return;
			}
			// Email Checker
			else if (email.indexOf(".") < 0) {

				alert("Incorrect email address. Please re-enter!");
				document.getElementById("newUserEmail").focus();
				return;
			}
			// Email Checker
			else if (email.indexOf(" ") >= 0) {
				//new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
				alert("Incorrect email address. Please re-enter!");
				document.getElementById("newUserEmail").focus();
				return;
			} else if (len > 50) {
				//length1=email.length;
				//alert(length1);
				// new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
				alert("Email should not exceed more than 50 characters!");
				document.getElementById("newUserEmail").focus();
				return;
			} else if (no >= 2) {
				//length1=email.length;
				//alert(length1);
				//new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
				alert("Please do not enter more than one email address!");
				document.getElementById("newUserEmail").focus();
				return;
			}
		}
		//alert("Submitting registrationForm ..");
		document.registrationForm.submit();

	}

	function textCounter(field, cntfield, maxlimit) {
		if (field.value.length > maxlimit) // if too long...trim it!
			field.value = field.value.substring(0, maxlimit);
		// otherwise, update 'characters left' counter
		else
			cntfield.value = maxlimit - field.value.length;
	}
</script>

<title><s:property value="wCContext.storefrontId" /> / <s:text
		name='MSG.SWC.MISC.REGISTER.GENERIC.TABTITLE' /></title>
</head>

<body class="ext-gecko ext-gecko3">
	<div id="main-container">
		<div id="main" class="anon-pages">

			<s:action name="xpedxHeader" executeResult="true" namespace="/common" />

			<div class="container content-container">
				<!-- breadcrumb -->


						
							
									<!-- EB-1554 As a Saalfeld user, user want to view a Saalfeld specific Registration graphic on the Register Page so that user see Saalfeld specific branding -->
									<s:set name='storefrontId' value="wCContext.storefrontId" />
									<s:if
										test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storefrontId)}'>
										<td><div class="float-left">
												<img border="0" alt="" width="505" height="229"
													src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/ster/images/register_main.jpg">
											</div></td>
										<td><div class="float-left">
												<a href="https://www.xpedx.com/locate-us.aspx"
													target="_blank"><img border="0" alt="" width="271"
													height="229"
													src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/registration/not-customer.jpg"></a>
											</div></td>
									</s:if>
									<s:elseif
										test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>
										<td><div class="float-left">
												<img border="0" alt="" width="505" height="229"
													src="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/images//ster/register_main_saalfeld.jpg">
											</div></td>
										<td><div class="float-left">
												<a href="http://saalfeldredistribution.com/ContactUs.aspx"
													target="_blank"><img border="0" alt="" width="271"
													height="229"
													src="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/images/registration/new-to-saalfeld.jpg"></a>
											</div></td>
									</s:elseif>
								
								<s:if
										test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>
									<div class="float-left">
											<table align="center" border="0" cellpadding="0"
												cellspacing="0" width="505" height="193">
												<tbody>
													<s:set name='storefrontId' value="wCContext.storefrontId" />
													
													<s:if
														test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>
														<tr style="background-color: #006a3a; font-weight: bold">
															<td align="left">&nbsp;<font color="white">Benefits
																	of saalfeldredistribution.com</font></td>
													</s:if>
													<td align="center"><font color="white">Unregistered
															User</font></td>
													<td align="center"><font color="white">Registered
															User</font></td>
													</tr>
													<tr>
														<td style="color: #4c4c4c">Robust catalog search and
															navigation</td>
														<td style="text-align: center"><a
															onmouseover="this.style.textDecoration='underline';"
															onmouseout="this.style.textDecoration='none';"
															href="/swc/catalog/navigate.action?sfId=<s:property value="wCContext.storefrontId" />&amp;scFlag=Y&amp;scGuestUser=Y&amp;_bcs_=_nb_">Browse
																limited catalogs</a></td>
														<td align="center"><img border="0" alt=""
															src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/ster/images/register_checkmark.png"></td>
													</tr>
													<tr>
														<td style="background-color: #efeff0; color: #4c4c4c">Check
															prices, real-time inventory status</td>
														<td
															style="background-color: #efeff0; text-align: center; color: #4c4c4c; border: solid 0 white; border-left-width: 2px; border-right-width: 2px">N/A</td>
														<td style="background-color: #efeff0" align="center"><img
															border="0" alt=""
															src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/ster/images/register_checkmark.png"></td>
													</tr>
													<tr>
														<td style="color: #4c4c4c">Manage invoices
															electronically</td>
														<td style="text-align: center; color: #4c4c4c">N/A</td>
														<td align="center"><img border="0" alt=""
															src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/ster/images/register_checkmark.png"></td>
													</tr>
													<tr>
														<td style="background-color: #efeff0; color: #4c4c4c">Ordering
															templates</td>
														<td
															style="background-color: #efeff0; text-align: center; color: #4c4c4c; border: solid 0 white; border-left-width: 2px; border-right-width: 2px">N/A</td>
														<td style="background-color: #efeff0" align="center"><img
															style="align: center" border="0" alt=""
															src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/ster/images/register_checkmark.png"></td>
													</tr>
													<tr>
														<td style="color: #4c4c4c">Purchasing history reports</td>
														<td style="text-align: center; color: #4c4c4c">N/A</td>
														<td align="center"><img border="0" alt=""
															src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/ster/images/register_checkmark.png"></td>
													</tr>
													<tr>
														<td style="background-color: #efeff0; color: #4c4c4c">Control
															spend with order approval</td>
														<td
															style="background-color: #efeff0; text-align: center; color: #4c4c4c; border: solid 0 white; border-left-width: 2px; border-right-width: 2px">N/A</td>
														<td style="background-color: #efeff0" align="center"><img
															src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/ster/images/register_checkmark.png"></td>
													</tr>
												</tbody>
											</table>
										</div>
										</s:if>
									<s:set name='storefrontId'
											value="wCContext.storefrontId" /> 
									<s:if
										test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>
										<div class="float-left">
											<a href="http://saalfeldredistribution.com/ContactUs.aspx"
												target="_blank"><img border="0" title="" width="271"
												height="193"
												src="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/images/ster/register_video_thumb_saalfeld.jpg"></a>
										</div>
										
									</s:if>
					
					
					<s:form name="registrationForm" namespace='/profile/user'
						action='XPEDXNewUserRegistrationAction' validate="true">
						<!-- Webtrends Start  -->
						<meta name="DCSext.w_x_reg" content="1" />
						<!-- Webtrends End  -->
						<s:hidden name='newUserName' id='newUserName' value="" />

						<s:url id='homePage' namespace='/home' action='home' />
						<s:hidden id="homePageId" value="%{#homePage}" />
						
						<div class="error" id="errorMsg" style="display: none"></div>


					<div class="clearfix"></div>
					<p class="black italic addpadtop5 addmarginleft230">Not a current customer? <a href="https://xpedx.com/locate-us.aspx">Locate a Customer Service Representative</a> in your area.</p>
					<div class ="clearfix"></div>
					<h1 class="addpadtop20 txt-large">Registration</h1>
					<h2>Registration allows current customers to shop our website.</h2>
					
					<p><s:text name='MSG.SWC.MISC.REGISTER.GENERIC.GENINFO' /></p>
					
					
					<table class="full-width">
						<tbody>
							<tr>
								<td style="width: 180px; color: red"
									class="underlines no-border-right-user"><div
										class="float-left" style="color: black">Account Number</div>&nbsp;*</td>
							</tr>
							<tr>
								<td  colspan="2"
									class="underlines no-border-right-user">
									<s:textfield
										name="newUserAccountNumber" id="newUserAccountNumber"
										cssClass="x-input width-250px" maxlength="50">
										</s:textfield>
										<div class="account-text">Need Account Number?<br />
											<a href="https://xpedx.com/locate-us.aspx" class="bright-link">Click to locate a Customer Serice Representative.
											</a>
										</div>
										</td>
										
							</tr>
							<tr>
								<td style="width: 180px; color: red"
									class="underlines no-border-right-user"><div
										class="float-left" style="color: black">First Name</div>&nbsp;*</td>
								<td style="width: 180px; color: red"
									class="underlines no-border-right-user"><div
										class="float-left" style="color: black">Last Name</div>&nbsp;*</td>

							</tr>

							<tr>
								<td style="width: 278px;"
									class="underlines no-border-right-user"><s:textfield
										name="newUserFirstName" id="newUserFirstName"
										cssClass="x-input width-250px" maxlength="50" /></td>
								<td style="width: 278px;"
									class="underlines no-border-right-user"><s:textfield
										name="newUserLastName" id="newUserLastName"
										cssClass="x-input width-250px" maxlength="50" /></td>
							</tr>

							<tr>
								<td style="width: 180px; color: red"
									class="underlines no-border-right-user"><div
										class="float-left" style="color: black">Company</div>&nbsp;*</td>
							</tr>

							<tr>
								<td colspan="3" class="underlines no-border-right-user"><s:textfield
										id="newUserCompanyName" name="newUserCompanyName"
										theme="simple" cssClass="x-input width-525px" maxlength="30" /></td>
							</tr>

							<tr>
								<td style="width: 180px; color: red"
									class="underlines no-border-right-user"><div
										class="float-left" style="color: black">Address</div>&nbsp;*</td>
							</tr>

							<tr>
								<td class="underlines no-border-right-user"><s:textfield
										id="newUserAddress1" name="newUserAddress1" theme="simple"
										cssClass="x-input width-250px" maxlength="150" /></td>
								<td class="underlines no-border-right-user"><s:textfield
										id="newUserAddress2" name="newUserAddress2" theme="simple"
										cssClass="x-input width-250px" maxlength="150" /></td>
							</tr>
							<tr>
								<td style="width: 180px; color: red"
									class="underlines no-border-right-user"><div
										class="float-left" style="color: black">City</div>&nbsp;*</td>
								<td style="width: 180px; color: red"
									class="underlines no-border-right-user"><div
										class="float-left" style="color: black">State / Province</div>&nbsp;*</td>
								<td style="width: 180px; color: red"
									class="underlines no-border-right-user"><div
										style="position: relative; left: -150px; color: black">
										&nbsp;Postal Code<span style="COLOR: red">&nbsp;*</span>
									</div></td>
							</tr>

							<tr>
								<td class="underlines no-border-right-user"><s:textfield
										id="newUserCity" name="newUserCity" theme="simple"
										cssClass="x-input width-250px" maxlength="150" /></td>
								<td class="underlines no-border-right-user"><s:textfield
										id="newUserState" name="newUserState" theme="simple"
										cssClass="x-input width-115px" maxlength="150" /></td>
								<td class="underlines no-border-right-user"><div
										style="position: relative; left: -150px">
										&nbsp;
										<s:textfield id="newUserZipCode" name="newUserZipCode"
											theme="simple" cssClass="x-input width-115px" maxlength="150" />
									</div></td>
							</tr>

							<tr>
								<td style="width: 180px; color: red"
									class="underlines no-border-right-user"><div
										class="float-left" style="color: black">Phone</div>&nbsp;*</td>
								<td style="width: 180px; color: red"
									class="underlines no-border-right-user"><div
										class="float-left" style="color: black">Email Address</div>&nbsp;*</td>
							</tr>

							<tr>
								<td class="underlines no-border-right-user"><s:textfield
										id="newUserPhone" name="newUserPhone" theme="simple"
										cssClass="x-input width-250px phone-numeric" maxlength="10" /></td>
								<td class="underlines no-border-right-user"><s:textfield
										id="newUserEmail" name="newUserEmail" theme="simple"
										cssClass="x-input width-250px" maxlength="150" /></td>
							</tr>

							<tr>
								<td style="width: 180px; color: red"
									class="underlines no-border-right-user"><div
										class="float-left" style="color: black">Comments</div></td>
							</tr>

							<tr>
								<td colspan="3" class="underlines no-border-right-user"><s:textarea
										id="newUserComments" name="newUserComments" theme="simple"
										cssClass="x-input width-525px" rows="6" cols="20"
										onkeydown="textCounter(document.registrationForm.newUserComments,document.registrationForm.remLen2,2000)"
										onkeyup="textCounter(document.registrationForm.newUserComments,document.registrationForm.remLen2,2000)" /></td>
							</tr>

							<tr>
								<td width="160" class="underlines no-border-right-user"><input
									readonly="readonly" type="text" name="remLen2" size="2" maxlength="2"
									class="float-left"
									style="position: relative; border: none"
									value="2000" /><span class="addmarginleft5">characters remaining</span></td>
							</tr>

							<tr>
								<td width="160"><div
										class="mandatory float-left addpadtop10 addmarginleft0">*required</div></td>

							</tr>

							<!-- 
		<tr>
  			<td width="160" class="grey  no-border-right-user">&nbsp;</td>
  			<td colspan="3" class="no-border-right-user"><div>
    			<ul class="float-right  padding-left3">
	      			 <li class="float-left margin-10"><s:a href='%{homePage}' cssClass="grey-ui-btn"><span>Cancel</span></s:a></li>
					<li class="float-right"><a href="javascript:userRegSubmit();" class="orange-ui-btn"><span>Submit</span></a></li> 
        		</ul>
      			</div></td>
    	</tr> 
    	
       	<tr>
      		<td colspan="4" class="grey no-border-right-user">&nbsp;</td>
    	</tr> -->

							<tr>
								<td class="grey  no-border-right-user">&nbsp;</td>
								<td colspan="3" class="no-border-right-user"><div
										style="width: 263px;">
										<ul class="float-right  padding-left3 margin-right-10">
											<li class="float-left margin-10"><input id="cancelId"
												class="btn-neutral" type="button" value="Cancel" /></li>
											<li class="float-right"><input type="button"
												onclick="javascript:userRegSubmit();" class="btn-gradient"
												value="Submit" /></li>
										</ul>
									</div></td>

								<td class="underlines no-border-right-user">&nbsp;</td>
								<td class="underlines no-border-right-user">&nbsp;</td>
							</tr>


							<tr>
								<td colspan="4" class="grey  no-border-right-user"><div
										id="errorMsgForMandatoryFields"
										style="float: right; margin-right: 12px; display: none; margin-right: 20px;"
										class="error"></div></td>
							</tr>

							<tr>
								<td colspan="4" class="grey  no-border-right-user">&nbsp;</td>
							</tr>

						</tbody>
					</table>

				</s:form>
			
			</div>

		</div>

	</div>
	<!-- end main  -->

	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />

	<!-- end container  -->
</body>

</html>