<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%request.setAttribute("isMergedCSSJS","true");%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<s:set name="wcCtx" value="WCContext" />
<s:set name="RememberMeRule"
	value="#wcCtx.getWCAttribute('RememberMeRule')" />
<s:set name='sfid' value='wCContext.storefrontId'/>
<head>
<!-- This needs to be at the top to ensure the 'Sign In' link is never seen. -->
<script type="text/javascript">
$('#signIn').hide();
</script>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
 <link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />  	 
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
						tabindex="1" onkeypress="javascript:loginSubmit(this,event)" />
				
					<p class="login-lbl">Password</p>
						<s:password id="Password" name="Password" cssClass="x-input"
							tabindex="2" onkeypress="javascript:loginSubmit(this,event)" >
						</s:password>
					
					<p><a class="underlink" href="<s:url action="forgotPwd" namespace="/home" />"><s:text name="login.forgotPwd"/></a></p>
					
					<div class="button-row">
					<!-- added for JIRA 3936 -->
						<a href="javascript:signIn()" 
							class="green-ui-btn"><span>Sign In</span></a> 
							<a href="javascript:(function(){document.homePageNewUserRegistration.submit();})();" class="underlink">Register</a>
					</div>

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
					<%	}
					%>
					
					<s:hidden name="EnterpriseCode" value="%{#wcCtx.getStorefrontId()}" />
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

<s:form name='homePageNewUserRegistration' namespace='/profile/user' action='XPEDXRegisterUser'>
</s:form>
<script>
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