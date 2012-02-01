<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/home/home.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/home/portalhome.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/catalog/narrowBy.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/catalog/catalogExt.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/ext-all.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/swc.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/theme-xpedx_v1.2.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" />.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/xpedx-mil-new.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/xpedx-quick-add.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/prod-details.css"/>
 

<!-- javascript -->

<script type="text/javascript" src="../xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="../xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="../xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="../xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="../xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="../xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="../xpedx/js/catalog/catalogExt.js"></script>

<!-- carousel scripts css  -->

<link media="all" type="text/css" rel="stylesheet" href="../xpedx/js/jcarousel/skins/xpedx/theme.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/js/jcarousel/skins/xpedx/skin.css" />

<!-- carousel scripts js   -->

<script type="text/javascript" src="../xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript" src="../xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="../xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="../xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="../xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="../xpedx/js/quick-add/quick-add.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="../xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<script src="../xpedx/js/jquery-tool-tip/jquery.bgiframe.min.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="../xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="../xpedx/js/xpedx-new-ui.js" language="javascript">
    
</script>


<script type="text/javascript" src="<s:url value='../xpedx/js/jquery-tool-tip/jquery-ui.min.js'/>"></script>

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="../xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="../xpedx/js/fancybox/jquery.fancybox-1.3.1.js"></script>

<link rel="stylesheet" type="text/css" href="../xpedx/js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../xpedx/js/fancybox/jquery.fancybox-1.3.1.css" media="screen" />

<script src="../xpedx/js/SpryTabbedPanels.js" type="text/javascript"></script>
 
<style>
#mil-list-new .form a:link{text-decoration:none;color:#000;}
#mil-list-new .form a:visited{text-decoration:none;color:#000;}
#mil-list-new .form a:hover{text-decoration:underline; color:#003366;}
#mil-list-new .form a:active{text-decoration:none; color:#000;}
</style>

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
        	errorDiv.innerHTML = "Please enter a value for "+ newPwd.title +" field.";
        	newPwd.style.borderColor="#FF0000";
        	errorDiv.style.display = 'inline';
			return;
		}
		if(confirmNewPwd.value.trim()=="")
		{
			errorDiv.innerHTML = "Please enter a value for "+ confirmNewPwd.title +" field.";
			confirmNewPwd.style.borderColor="#FF0000";
			errorDiv.style.display = 'inline';
			return;
		}
		if(newPwd.value!=confirmNewPwd.value)
		{
			errorDiv.innerHTML = "Please enter the same value in both password fields.";
			confirmNewPwd.style.borderColor="#FF0000";
			errorDiv.style.display = 'inline';
			return;
		}
    	document.forgotPwdSubmitForm.submit();
    	return false;
    }
</script>

<link media="all" type="text/css" rel="stylesheet" href="<s:url value='/swc/css/home/forgotPwd/forgotPassword.css'/>" />
<script type="text/javascript" src="<s:url value='/swc/js/home/forgotPassword.js'/>"></script>

<!-- <title>Forgot Password</title> -->
<title><s:text name="MSG.SWC.MISC.FORGOTPASSWORD.GENERIC.TABTITLE"/> </title>

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
			<set name='UserId' value='<%=request.getParameter("userID")%>'/>
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
			
			<s:form id="forgotPwdSubmitForm" name="forgotPwdSubmitForm" action="changePwdForgotPwd" validate="true" namespace="/home" method="POST">
	        <s:hidden name="#action.namespace" value="/home"/>
			<s:hidden id="actionName" name="#action.name" value="changePwdForgotPwd"/>
			<s:hidden id="validationField" name="validationField" value="true"/>
			<input type="hidden" id="requestId" name="requestId" value='<%=request.getParameter("requestId")%>'/>
			<input type="hidden" id="userID" name="userID" value='<%=request.getParameter("userID")%>'/>
			<input type="hidden" id="DisplayUserID" name="DisplayUserID" value='<%=request.getParameter("userID")%>'/>
			<input type="hidden" id="USER_IN_PROCESS" name="USER_IN_PROCESS" value="USER_IN_PROCESS" />
			<!-- <input type="hidden" id="loginId" name="loginId" value='<%=request.getParameter("userID")%>'/>  -->
			
			
			<s:url id='homePage' namespace='/home' action='home' />

			<table class="full-width">
				<tbody>
					<tr>
              			<td colspan="3" class="underlines no-border-right-user"><s:text name='password.enter'/></td>
            		</tr>
            		
            		<!-- <div class="error" id="errorMsgForPassword" style="display : none"></div> -->
            		
            		
					<tr>
						<td width="7%" class="underlines no-border-right-user"><s:text name="newPassword"/></td>
						<td colspan="2" class="underlines no-border-right-user"><span
							class=" noBorder-left"><s:password id="newPassword" name="newPassword" showPassword="true" cssClass="x-input width-250px" maxlength="14" title="New Password"/></span>
						</td>
					</tr>
					<tr>
						<td width="7%" class="underlines no-border-right-user"><s:text name="retypenewPassword"/></td>
						<td colspan="2" class="underlines no-border-right-user"><span
							class=" noBorder-left"><s:password id="confirmNewPassword" name="confirmNewPassword" showPassword="true" cssClass="x-input width-250px" maxlength="14" title="Confirm New Password"/></span>
						</td>
					</tr>
					<tr>
						<td class="grey  no-border-right-user">&nbsp;</td>
						<td width="38%" class="grey  no-border-right-user">
							<div class="fp-btn-container">
								<ul class="float-left  padding-left3">
									<li class="float-left margin-10"><a href="javascript:cancelForgotPasswordFlow();"
										class="grey-ui-btn"><span>Cancel</span>
									</a>
									</li>
									<li class="float-right"><a href="javascript:forgotPwdSubmit();"
										class="orange-ui-btn"><span>Submit</span>
									</a>
									</li>
								</ul>
							</div>
						</td>
						<td width="55%" class="grey  no-border-right-user">&nbsp;</td>
					</tr>
					
					<tr><td colspan="2">
					    <div class="error"  style="float:right; margin-right: 12px;display:none;" id="errorMsgForPassword" ></div>
					</td></tr>
					
					<tr>
              			<td colspan="3" class="grey  no-border-right-user"><s:text name="MSG.SWC.MISC.HELPDESK.GENERIC.CONTACT"/></td>
            		</tr>
				</tbody>
			</table>

			</s:form>
			
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
    
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	
<!-- end container  -->
</body>
</html>