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
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MISC<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
</head>
<!-- END swc:head -->

<s:set name='orgQuestionList' value='%{getQuestionListForOrg()}' />
<s:set name='maskedPasswordString' value='%{getMaskedPasswordString()}' />

<body class="ext-gecko ext-gecko3" onLoad="javascript:document.getElementById('answer').focus();">


    	
    	<div >
      
			
			<s:form id="passwordUpdateForm" name="passwordUpdateForm"  namespace='/common' action='xpedxPasswordUpdate'  validate="true" method="POST">
			
			<table class="full-width">
			<tbody style="width: 721px">
			<tr><td align ="left"><div class="xpedx-light-box" style="width: 718px"><h2><b>Change Password</b></h2></div></td></tr>
			<tr><td></td></tr>
			</tbody>
			</table>
			<table width="95%" style="margin-left:15px;">
				<tbody>
					<tr>
						<td colspan="4" class="underlines no-border-right-user" align="center" width="80%">
						 <b>Please change your password.</b></br></br></td>
						 </tr>
						 <tr>
						 <td colspan="4" class="underlines no-border-right-user" align="center">
						<b>Password Policy: minimum of 8 characters, maximum of 14 characters, at least 2 
							</br>
						 alpha characters, 1 uppercase character, 1 numeric character, maximum of</br>
						 consecutive repeated characters=2, the following characters cannot be used $, ?, !</b></br>							
						 </td>
					</tr>
					<tr>
					<td width="23%" >&nbsp;</td>
					<td width="71%"></td>
					<td width="2%"></td>
					<td width="4%"></td>
				</tr>
					
		<tr>
			<td>&nbsp;</td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td class="no-border-right-user">
			<b>Password:</b></td>
			
			<td  class="no-border-right-user">
			<s:url id="ValidatePasswordURL" action="validatePassword"/><s:password
				tabindex="22" name="userpassword" id="userpassword" 
				cssClass="x-input" cssStyle="width: 400px;"
				value="" showPassword="true" size="8"
				maxlength="14" 
			/></td>
		</tr>
		<tr>
			<td >&nbsp;</td>
			<td ></td>
			<td ></td>
			<td ></td>
		</tr>
		
		<tr>
			<td class="no-border-right-user">
			<b>Confirm Password:</b></td>
			<td class="no-border-right-user"><s:password
				tabindex="23" name="confirmpassword" id="confirmpassword" onkeyup="" 
			    cssClass="x-input" cssStyle="width: 400px;"
				value="" showPassword="true" size="8"
				maxlength="14" /></td>
		</tr>		
		<tr>
			<td>&nbsp;</td>
			<td></td>
			<td></td>
			<td></td>
		</tr>		
				</tbody>
			</table>
			
			<table width="83%">
			<tbody>
			<tr>
			<td colspan="1" width="83%"></td>
			<td colspan="2">
			<div>
			<ul class="float-right">
			<li >
			<a href="javascript:savePassword();"  class="orange-ui-btn" tabindex="24"><span>Submit</span>					
			</a>
			</li>
			</ul>
			</div></td>
			<td width="5%"></td>
			</tr>
			
			<tr>
				<td class="grey  no-border-right-user" colspan="3" >
				</br>
					<div class="error"  style="float:right; margin-right: 1px;display:none;" id="errorMsgForAnswer" ></div>
					<div style="float:right; margin-right: 1px;display:none;" id="pwdValidationDiv" ></div>
			  </td>
						<td class="grey  no-border-right-user" colspan="1">
						</td>
			  </tr>
			</tr>
			</tbody>
			</table>
			</s:form>
			 <s:form id='passwordSubmit' namespace='/common' action='XPEDXPasswordSubmit' name='passwordSubmit'>
					<s:hidden name='newPassword' id='newPassword' value='%{#userpassword}'></s:hidden> 
					<s:hidden name='userPwdToValidate' id='userPwdToValidate' value='%{#userpassword}'></s:hidden> 
				  </s:form>
		
			</div> 
      		<div class="error" id="errorMsgFor_userpassword" style="display : none; margin-left: 140px ;"/></div>
      		<div class="error" id="errorMsgFor_blank" style="display : none; margin-left: 300px ;"/></div>
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
<script type="text/javascript">



</script>

<!-- end main  -->
    
<%--s:action name="xpedxFooter" executeResult="true" namespace="/common" /--%>
	
<!-- end container  -->
</body>
</html>