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

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MISC<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bgiframe.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript">
    
</script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min.<s:property value='#wcUtil.xpedxBuildKey' />js"></script>

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/SpryTabbedPanels<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript"></script>
 
<script type="text/javascript">
    function forgotPwdSubmit(){
        var newPwd = document.forgotPwdSubmitForm.newPassword;
        var confirmNewPwd = document.forgotPwdSubmitForm.confirmNewPassword;
        var errorDiv = document.getElementById("errorMsgForPassword");
        
        errorDiv.innerHTML = "";
        newPwd.style.borderColor="";
        confirmNewPwd.style.borderColor="";
        errorDiv.style.display = "none";
        
        if(newPwd.value.trim()=="")
        {
        	errorDiv.innerHTML = "Please enter a new password";
        	newPwd.style.borderColor="#FF0000";
        	errorDiv.style.display = 'inline';
			return;
		}
		if(confirmNewPwd.value.trim()=="")
		{
			errorDiv.innerHTML = "Please confirm new password";
			confirmNewPwd.style.borderColor="#FF0000";
			errorDiv.style.display = 'inline';
			return;
		}
		if(newPwd.value!=confirmNewPwd.value)
		{
			errorDiv.innerHTML = "Please enter the same value in both password fields";
			confirmNewPwd.style.borderColor="#FF0000";
			errorDiv.style.display = 'inline';
			return;
		}
    	document.forgotPwdSubmitForm.submit();
    	return false;
    }
</script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/home/forgotPassword<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- <title>Forgot Password</title> -->
<title>Reset Password</title>

</head>
<!-- END swc:head -->

<s:set name='_action' value='[0]' />

<body class="ext-gecko ext-gecko3" onload="javascript:document.getElementById('newPassword').focus();">
<div id="main-container">
	<div id="main" class="anon-pages">
    	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
    	<script type="text/javascript">
			document.getElementById("newSearch").style.display = 'none';
		</script>
    	
    	<div class="container">
      	<!-- breadcrumb -->
       		<s:set name='wcContext' value="#_action.getWCContext()"/>
			<s:set name='error' value="#_action.getErrorMessageType()"/>
			<div class="errorMessage">
			<s:if test='%{#error=="OldPwdNotAllwdError"}'>
		    <s:text name="old.password.not.allowed"/>
			</s:if>
			<s:if test='%{#error=="PwdTooShortError"}'>
		    <s:text name="password.too.short"/>
			</s:if>
			<s:if test='%{#error=="PwdTooLongError"}'>
		    <s:text name="password.too.long"/>
			</s:if>
			<s:if test='%{#error=="SplCharRqdError"}'>
		    <s:text name="spl.char.rqd"/>
			</s:if>
			<s:if test='%{#error=="systemError"}'>
		    <s:text name="system.error.change.pwd"/>
			</s:if>
			</div>
      		<div id="mid-col-mil"> 
		    <div>
      		<%-- <div class="padding-top3 page-title black"><strong class="black"> Forgot Password</strong></div> --%>
      		<% if(null != request.getParameter("requestId")){%>
		     <div class="padding-top3 page-title black"><strong class="black"> <s:text name="Reset.Password"/></strong></div>
		    <%}
		    else{ %>
      		<div class="padding-top3 page-title black"><strong class="black"> <s:text name="MSG.SWC.MISC.FORGOTPASSWORD.GENERIC.PGTITLE"/></strong></div>
      		<%}%>
			</div>
			<div class=" padding-bottom clearview"> </div>
			<div class="reset-wrap"> <h3>Enter a new password</h3>
			 <div class="reset-left-col">
			
			<s:form id="forgotPwdSubmitForm" name="forgotPwdSubmitForm" action="changePwdForgotPwd" validate="true" namespace="/home" method="POST">
	        <s:hidden name="#action.namespace" value="/home"/>
			<s:hidden id="actionName" name="#action.name" value="changePwdForgotPwd"/>
			<s:hidden id="validationField" name="validationField" value="true"/>
			<input type="hidden" id="requestId" name="requestId" value='<%=request.getParameter("requestId")%>'/>
			<input type="hidden" id="loginid1" name="loginid1" value='<%=request.getParameter("UserId")%>'/>
		
		 	<!-- <s:url id='homePage' namespace='/home' action='home' /> --> 

			 <label class="reset-label"><span>New Password:</span>
             <input class="reset-input" id="newPassword" type="password"
		             name="newPassword" onchange="javaScript:clearErrorDiv();" tabindex=1 />
             </label>


	         <label class="reset-label"> <span>Confirm New Password:</span> 
	         <input class="reset-input" id="confirmNewPassword" type="password"
		            name="confirmNewPassword" onchange="javaScript:clearErrorDiv();"tabindex=2 /> 
		     </label>

	        <s:url id="ValidatePasswordURL" action="validateResetPassword"
		           namespace="/profile/user" />

	        <input type="button" class="btn-gradient floatright addmarginleft10 addmargintop10 addmarginright30"
		           value="Submit" onclick="javascript:validateResetPassword();" tabindex=3 />

	        <input type="button" class="btn-neutral floatright addmargintop10" 
	               value="Cancel" onclick="javascript:cancelForgotPasswordFlow();" />


            <div class="error"  style="float: left; margin-right: 12px; display:none; "
		         id="errorMsgForPassword"></div>

	        <div class="error addmargintop10" style="float:left; margin-right: 12px; display:none;" 
	             id="pwdErrorDiv"></div>

	        <p class="reset-questions"> <s:text
		       name="MSG.SWC.MISC.HELPDESK.GENERIC.CONTACT" /> </p>
		       
		    </s:form> 
			</div> 
			<div class="password-policy"> <h4>Password Policy:</h4> 
			<ul>
            <li>8-14 characters required</li> <li>2 alpha characters minimum</li> <li>1 uppercase required</li> <li>1 number required</li> 
            <li>2 character max in consective repeat</li> <li>Cannot use the following: $ ? !</li> </ul> 
            </div> 
            <div class="clearfix">
            </div> 
            </div>
			
			<s:form id="cancelforgotPwdForm" name="cancelforgotPwdForm" action="cancelForgotPassword" namespace="/home" method="POST">
        	</s:form>
          	<div class=" bot-margin"> &nbsp; </div>
        	<div class=" bot-margin"> &nbsp;</div>
  
			</div>
      		<br />
    	</div>
	</div>
</div>
<!-- end main  -->
<Script>
function validateResetPassword(){	
	var newPwd = document.forgotPwdSubmitForm.newPassword;
    var confirmNewPwd = document.forgotPwdSubmitForm.confirmNewPassword;
   // alert(confirmNewPwd.value);
    var errorDiv = document.getElementById("errorMsgForPassword");
    
    errorDiv.innerHTML = "";
    newPwd.style.borderColor="";
    confirmNewPwd.style.borderColor="";
    errorDiv.style.display = "none";
    
    if(newPwd.value.trim()=="")
    {
    	errorDiv.innerHTML = "Please enter a new password";
    	newPwd.style.borderColor="#FF0000";
    	errorDiv.style.display = 'inline';
		return;
	}
	if(confirmNewPwd.value.trim()=="")
	{
		errorDiv.innerHTML = "Please confirm new password";
		confirmNewPwd.style.borderColor="#FF0000";
		errorDiv.style.display = 'inline';
		return;
	}
	if(newPwd.value!=confirmNewPwd.value)
	{
		errorDiv.innerHTML = "Please enter the same value in both password fields";
		confirmNewPwd.style.borderColor="#FF0000";
		errorDiv.style.display = 'inline';
		return;
	}
	var url = '<s:property value="#ValidatePasswordURL" escape="false" />';
	
		Ext.Ajax.request({
	        url :url,
	        params:{loginId :document.getElementById("loginid1").value,
			userPwdToValidate : document.getElementById("newPassword").value},
	        method: 'POST',
	        success: function (response, request){
	           var responseText = response.responseText;
	           errorDiv = document.getElementById("pwdErrorDiv");
	           
	           if(errorDiv){     
		            	   
               		if(responseText.indexOf("error")>-1){                   		
	                    errorDiv.innerHTML = response.responseText;
	                    errorDiv.style.border = 'none';
	                    errorDiv.style.background = 'none';
	                    errorDiv.style.display = 'inline';
               		}
               		else {
               			errorDiv.innerHTML ="";
               			errorDiv.style.display = 'none';
               			forgotPwdSubmit();
               		}
               }
	   		},
	   		failure: function (response, request){
	   		   errorDiv = document.getElementById("pwdValidationDiv");
               if(errorDiv){
                errorDiv.style.display = 'none';
               }
	    	   alert("Error in service.");
	        }
	    });
}

function clearErrorDiv(){
		errorDiv = document.getElementById("pwdErrorDiv");
		errorDiv.innerHTML ="";
		errorDiv = document.getElementById("errorMsgForPassword");
		errorDiv.innerHTML ="";
		var newPwd = document.forgotPwdSubmitForm.newPassword;
	    var confirmNewPwd = document.forgotPwdSubmitForm.confirmNewPassword;
	    newPwd.style.borderColor="";
	    confirmNewPwd.style.borderColor="";
	    errorDiv.style.display = "none";
  }
</script>
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	
<!-- end container  -->
</body>
</html>