<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%request.setAttribute("isMergedCSSJS","true");%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<s:set name="wcCtx" value="WCContext" />
<s:set name="RememberMeRule"
	value="#wcCtx.getWCAttribute('RememberMeRule')" />
<s:set name='sfid' value='wCContext.storefrontId'/>
<s:url id="MyRegisterUserURL" namespace='/profile/user' action='MyRegisterUser' />

<head>
<!-- This needs to be at the top to ensure the 'Sign In' link is never seen. -->
<script type="text/javascript">
//$('#signIn').hide();
var sign = document.getElementById("signIn");
sign.innerHTML="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
</script>
<%-- <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
     <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
 --%> <link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" /> 
</head>
<table id="signon-table">
		<tr>
			<td class="promo-cell" rowspan="2"  colspan="3">
				<div id="signon-image-rotation">
				 					
				<%-- 
					<div class="slideshow">
						<s:action name="xpedxDynamicPromotions" executeResult="true" namespace="/common" >
							<s:param name="callerPage">signinpage</s:param>
						</s:action>
					</div> 
					--%>
					
					<div class="slideshow">
						<s:action name="xpedxDynamicPromotionsAction" executeResult="true" namespace="/common" >
							<s:param name="callerPage">SignInPageTop</s:param>
						</s:action>
					</div>
					
					<s:if test="#request['imageCounter'] > 1" >
						<div id="home-image-rotation-nav">
							<div class="img-navi-left"></div>
	
							<div id="home-image-rotation-nav-inner"></div>
							<div class="img-navi-right"></div>
						</div>
					</s:if>
					
				</div>
			</td>
			<td class="form-cell">	
				<s:form
					action="login" namespace="/common" method="post" name="singForm" id="singForm" >
		<%-- 			<s:set name="displayError" value="N" />
					<% if(null != request.getParameter("fromRegisterPage") && !request.getParameter("fromRegisterPage").equals("Y")) { %>
					<s:if
						test="%{(#request.ERROR_MESSAGE != '') && (#request.ERROR_MESSAGE != null) }">
						<div style="color: #FF0000; font-size: 1.1em; text-align: left;">
						<s:text name="login.loginError" /></div>
					</s:if>
					<% } else if(null ==  request.getParameter("fromRegisterPage"))
					{ 
					%>
					<s:if
						test="%{(#request.ERROR_MESSAGE != '') && (#request.ERROR_MESSAGE != null) }">
						<div style="color: #FF0000; font-size: 1.1em; text-align: left;">
						<s:text name="login.loginError" /></div>
					</s:if>
					<%	}
					%> --%>
					
					<!-- Webtrands Start -->
					<% if(null != request.getParameter("fromRegisterPage") && request.getParameter("fromRegisterPage").equals("Y")) { %>
					<meta name="DCSext.w_x_reg_c" content="1" />
					<%} %>
					<!-- Wbtrands End -->
					
					<p class="login-lbl">Username</p>
					<s:textfield id="DisplayUserID" name="DisplayUserID"
						value="%{#wcCtx.getRememberedDisplayUserId()}" cssClass="x-input"
						tabindex="1" />
				
					<p class="login-lbl">Password</p>
						<s:password id="Password" name="Password" cssClass="x-input"
							tabindex="2" >
						</s:password>
					
					<p><a class="underlink" href="<s:url action="forgotPwd" namespace="/home" />"><s:text name="login.forgotPwd"/></a></p>
					
					<div class="button-row">
						<a href="#" id="loginFormSignInLink" class="orange-ui-btn"><span>Sign In</span></a> 
						<a href="<s:property value='MyRegisterUserURL' />" class="underlink">Register</a>
					</div>
					
					<%-- eb-2749: in order for IE to remember the password, we must submit the form using a real button --%>
					<input type="submit" id="loginFormSubmitButton" name="submitForm" value="Submit" style="position:absolute;left:-9999px" />

					<s:if test='%{#RememberMeRule=="Y"}'>
						<p> <input type="checkbox" id="remember.me" name="RememberMe"
							checked="checked" tabindex="3">
						<label class="txt-sml-gry">&nbsp;Remember Me</label>
						<p/>
					</s:if>
								
				 <s:set name="displayError" value="N" />
					<% if(null != request.getParameter("fromRegisterPage") && !request.getParameter("fromRegisterPage").equals("Y")) { %>
					<s:if
						test="%{(#request.ERROR_MESSAGE != '') && (#request.ERROR_MESSAGE != null) }">
						<div style="color: #FF0000; font-size: 1.1em; text-align: left; margin-right: 10px;" class="error">
						<s:text name="login.loginError" /></div>
					</s:if>
					<% } else if(null ==  request.getParameter("fromRegisterPage"))
					{ 
					%>
					<s:if
						test="%{(#request.ERROR_MESSAGE != '') && (#request.ERROR_MESSAGE != null) }">
						<div style="color:#ff0000; font-size: 1.1em; text-align: left;margin-right: 10px;" class="error">
						<s:text name="login.loginError" /></div>
					</s:if>
					<!-- Added for EB 560 on session timeout display the error msg-->
					<% if(null != request.getParameter("error")){ %>
						<div style="color:#ff0000; font-size: 1.1em; text-align: left;margin-right: 10px;" class="error">
						<p>Your session has expired</p>
						</div>
					<%}
					//Added for EB 560
					%>
					<%	}
					%>
					
					<s:hidden name="EnterpriseCode" value="%{#wcCtx.getStorefrontId()}" />						
				<p/>
				<div>
					<s:action name="xpedxDynamicPromotionsAction" executeResult="true" namespace="/common" >
							<s:param name="callerPage">SignInPageSide</s:param>
						</s:action>
				</div>
					</s:form>	
			</td>
		</tr>
		<tr>
		
		<%-- start of Fix : JIRA - 3385 --%>
			<!--  td class="stores-cell">
				<s:if test="#sfid == 'xpedx' ">
					<a href="http://xpedxstores.com" target="new">
						<img src="http://content.ipaper.com/storefront/xpedxSignIn.png" />	
					<a>
				</s:if>
				
				<s:if test="#sfid == 'xpedxCanada' ">
						<img src="http://content.ipaper.com/storefront/xpedxCanadaSignIn.png" />	
				</s:if>
								
				<s:if test="#sfid == 'BulkleyDunton' ">
						<img src="http://content.ipaper.com/storefront/BulkleyDuntonSignIn.png" />	
				</s:if>
								
				<s:if test="#sfid == 'Saalfeld' ">
						<img src="http://content.ipaper.com/storefront/SaalfeldSignIn.png" />	
				</s:if>
			</td-->
			<%-- End of Fix : JIRA - 3385 --%>
		</tr>
		
		<s:action name="xpedxMainCategoriesHomeAction" executeResult="true" namespace="/catalog" />
		
</table>

<script>
// sign in link submits the form. note that it's important to click the real submit button (hidden) rather than $(form).submit() - see eb-2749 for details
$('#loginFormSignInLink').click(function(event) {
	$('#loginFormSubmitButton').click();
	event.stopPropagation();
	return false;
});

$(document).ready(function() {
	var RememberMeUser="<s:property value='#wcCtx.getRememberedUserId()'/>";
	if(RememberMeUser==null || RememberMeUser=="")
	{
		$('[name="RememberMe"]').attr('checked', false);
	}
	else
	{
		$('[name="RememberMe"]').attr('checked', true);
	}
	});
</script>
