<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<% request.setAttribute("isMergedCSSJS","true"); %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

<!-- begin styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MISC<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
</head>
<!-- END swc:head -->

<s:set name='orgQuestionList' value='%{getQuestionListForOrg()}' />
<s:set name='maskedPasswordString' value='%{getMaskedPasswordString()}' />

<body class="ext-gecko ext-gecko3" onLoad="javascript:document.getElementById('answer').focus();">


    	
    	
      
			
			<div class="change-pw-wrap">
		<h1>Change Password</h1>
		<s:form cssclass="change-pw-form" id="passwordUpdateForm"
			name="passwordUpdateForm" namespace='/common'
			action='xpedxPasswordUpdate' validate="true" method="POST">

			<label> <span>New Password:</span> <input name="userpassword"
				id="userpassword" type="password" tabindex=1 class="float-right" { padding-bottom: 20px;}"/>
			</label>

			<label> <span>Confirm New Password:</span> <input
				name="confirmpassword" id="confirmpassword" type="password"
				tabindex=2 class="float-right" />
			</label>

			<div class="clearfix"></div>
			<input name="" type="button" tabindex=3 id="submitButton"
				class="btn-gradient floatright addmarginleft10 addmargintop10 addpadright20"
				onclick="javascript:savePassword();" value="Submit" />
		</s:form>
		<div class="password-policy">
			<h4>Password Policy:</h4>
			<ul>
				<li>8-14 characters required</li>
				<li>2 alpha characters minimum</li>
				<li>1 uppercase required</li>
				<li>1 number required</li>
				<li>2 character max in consecutive repeat</li>
				<li>Cannot use the following: $ ? !</li>
			</ul>
		</div>
		<div class="clearfix"></div>
			<s:url id="ValidatePasswordURL" action="validatePassword"/>
			<div style="float: left; margin-right: 12px; display: none;"
			id="pwdValidationDiv"></div>
			
			 <s:form id='passwordSubmit' namespace='/common' action='XPEDXPasswordSubmit' name='passwordSubmit'>
					<s:hidden name='newPassword' id='newPassword' value='%{#userpassword}'></s:hidden> 
					<s:hidden name='userPwdToValidate' id='userPwdToValidate' value='%{#userpassword}'></s:hidden> 
					<s:hidden name="preferredLocale" id="preferredLocale" value="%{'en_US_EST'}"/>
				  </s:form>
		
			
      		<div class="error" id="errorMsgFor_userpassword"
			style="display: none; margin-left: 12px;"></div>
		<div class="error" id="errorMsgFor_blank"
			style="display: none; margin-left: 12px;"></div>
      		<s:if test="%{#session.errorNote!= null}">
	 <div id="errorNote" class="error" style="display : inline; float: right">
		<s:iterator value="%{#session.errorNote}" id="error"  >
        	<s:property escape="false" value="%{#error}"/>  
        	<br>
  		</s:iterator>
  		<s:if test="pwdValidationResultMap!=null">
			<s:iterator value="pwdValidationResultMap" id="pwdValidationResultMap" status="status">
			<div id="pwdErrorDiv" class="error">
			<s:set name="errorDesc" value="value" />
			<s:set name="errorCode" value="key" />	
			<s:property value="#errorDesc"/>
			</div>			
		</s:iterator>
	
</s:if>
  	</div>
  	
	<s:set name="errorNote" value="<s:property value=null />" scope="session"/>
	
</s:if>
<div class="clearfix"></div>
	</div>
<script type="text/javascript">



</script>

<!-- end main  -->
    
<%--s:action name="xpedxFooter" executeResult="true" namespace="/common" /--%>
	
<!-- end container  -->
</body>
</html>