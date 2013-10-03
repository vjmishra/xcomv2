<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<%@page import="java.util.*" %>
<% request.setAttribute("isMergedCSSJS","true"); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<!-- begin styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MISC<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- carousel scripts css  -->

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/theme<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/skin<s:property value='#wcUtil.xpedxBuildKey' />.css" />

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

<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/SpryTabbedPanels<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript"></script>
 
<style>
#mil-list-new .form a:link{text-decoration:none;color:#000;}
#mil-list-new .form a:visited{text-decoration:none;color:#000;}
#mil-list-new .form a:hover{text-decoration:underline; color:#003366;}
#mil-list-new .form a:active{text-decoration:none; color:#000;}
</style>

<script type="text/javascript">
function usernameSubmit(){
    var usernameField = document.forgotPwdForm.UserId;
    var errorDiv = document.getElementById("errorMsgForUsername");
    var returnval = false;
    
    errorDiv.innerHTML = "";
    usernameField.style.borderColor="";
    errorDiv.style.display = "none";
    
    if(usernameField.value.trim().length == 0)
    {
       // errorDiv.innerHTML = "Please enter a value for "+ usernameField.title +" field.";
       // errorDiv.innerHTML = "Username is required.";
        errorDiv.innerHTML = "<s:text name='MSG.SWC.MISC.FORGOTPASSWORD.ERROR.USERNAMEREQ' />";
        usernameField.style.borderColor="#FF0000";
        errorDiv.style.display = 'inline';
        return returnval;
    }
    else{
    document.forgotPwdForm.submit();
    returnval = true;
    }
    return returnval;
}
</script>

<!-- <title>Forgot Password</title> -->
<title><s:text name="MSG.SWC.MISC.FORGOTPASSWORD.GENERIC.TABTITLE"/> </title>

</head>
<!-- END swc:head -->

<s:set name='_action' value='[0]' />

<body class="ext-gecko ext-gecko3">
<div id="main-container">
	<div id="main" class="anon-pages">
    	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
    	<script type="text/javascript">
			document.getElementById("newSearch").style.display = 'none';
		</script>
    	<div class="container">
      	<!-- breadcrumb -->
       		<s:set name='wcContext' value="#_action.getWCContext()"/>
      		<div id="mid-col-mil"> 
		    <div>
		   
		    
		    <% if(null != request.getParameter("requestId")){%>
		     <div class="padding-top3 page-title black"><strong class="black"> <s:text name="Reset.Password"/></strong></div>
		    <%}
		    else{ %>		    
      		<div class="padding-top3 page-title black"><strong class="black"> <s:text name="MSG.SWC.MISC.FORGOTPASSWORD.GENERIC.PGTITLE"/></strong></div>      		
      		<%} %>
			</div>
			<div class=" padding-bottom clearview"> </div>
				
		<!-- begin progress bar -->
			
		<div id="second-navigation">
             <ul id="main-nav" class="dropdown">
				<li class="active">
				     <span class="link" >Confirm your identity</span>
				</li>
				<li>
				     <span class="link">Answer your security question</span>
				</li>
				<li>
				    <span class="link" >Password reset confirmation</span>
				 </li>
             </ul>
		</div>
		<div class="clearall">&nbsp;</div>
		
		<!--  end progress bar -->
			
		<s:form id="forgotPwdForm" name="forgotPwdForm" action="usernameSubmit" validate="true" namespace="/home" method="POST">
	        <s:hidden name="#action.namespace" value="/home"/>
			<s:hidden id="actionName" name="#action.name" value="usernameSubmit"/>
			<s:hidden id="validationField" name="validationField" value="true"/>
			<input type="hidden" id="requestId" name="requestId" value='<%=request.getParameter("requestId")%>'/>
			
			
			<s:url id='homePage' namespace='/home' action='home' />
			
			<table class="full-width">
          		<tbody>
            		<tr>
              			<td colspan="3" class="underlines no-border-right-user"> Please enter your Username. <a href="#" class="txt-lnk-sml-1"></a></td>
            		</tr>
            		
            		<tr>
              			<td width="7%" class="underlines no-border-right-user">Username:</td>
              			 <% if(null != request.getParameter("requestId")){%>
		    				<td colspan="2" class="underlines no-border-right-user"><span class="noBorder-left">
<%--                 			<s:textfield id="UserId" name="UserId" value='<%=request.getParameter("UserId")%>' cssClass="x-input width-250px" maxlength="255" title="Username"/> --%>
								<% if(null != request.getParameter("userID")){%>
                					<input type="text" class="x-input width-250px" maxlength="255" title="Username" id="UserId" name="UserId" value='<%=request.getParameter("userID")%>'/>
                				<%} else{ %>
                					<input type="text" class="x-input width-250px" maxlength="255" title="Username" id="UserId" name="UserId" value=""/>
                				<%}%>
						</span></td>
		   				 <%} else{ %>
						<td colspan="2" class="underlines no-border-right-user"><span class="noBorder-left">
                			<s:textfield id="UserId" name="UserId" value="%{#wcContext.getRememberedDisplayUserId()}" cssClass="x-input width-250px" maxlength="255" title="Username"/>
						</span></td>
						<%}%>
            		</tr>
            		
  
            		
            		<tr>
              			<td class="grey  no-border-right-user">&nbsp;</td>
						<td width="24%" class="grey  no-border-right-user">
						<div class="fp-btn-container">
							<ul class="float-right margin-right-10">
								<li class="float-left margin-10">
									<s:a href='%{homePage}' cssClass="grey-ui-btn"><span>Cancel</span></s:a>
								</li>
								<li class="float-left">
									<s:a href="#" onclick="usernameSubmit();" cssClass="orange-ui-btn oub-fix"><span>Submit</span></s:a>
								</li>
							</ul>
              			</div></td>
              			<td width="55%" class="grey  no-border-right-user">&nbsp;</td>
            		</tr>

            		
            		<tr><td colspan="2">
            		<div class="error"  style="float:right; margin-right: 12px;display:none;" id="errorMsgForUsername" ></div>
            		</td></tr>
            		
          		</tbody>
        	</table>
        	</s:form>
        	<table class="full-width">
				<tbody>
					<tr>
						<s:set name="errormessage" value='%{#_action.getErrorMessageType()}'/>
					    <s:if test="%{#errormessage == 'ResetPasswordNotAllowed'}">
					       <td class="error" style="border:0px; padding-left:0px;display:inline;"><s:text name="username.error"/></td>
					    </s:if>
					    <s:elseif test="%{#errormessage == 'ResetPasswordRequestError'}">
					       <td class="error" style="border:0px; padding-left:0px;display:inline;"><s:text name="pwdresetrequest.error"/></td>
					    </s:elseif>
					    <s:elseif test="%{#errormessage == 'ResetIdInvalid'}">
					        <td class="error" style="border:0px; padding-left:0px;display:inline;"><s:text name="requestid.error"/></td>
					    </s:elseif>
					    <s:elseif test="%{#errormessage == 'MaximumAttemptsError'}">
					        <td class="error" style="border:0px; padding-left:0px;display:inline;"><s:text name="maxattempts.error"/></td>
					    </s:elseif>
          			</tr>
            		<tr>
              			<td colspan="3" class="grey  no-border-right-user" ><s:text name="MSG.SWC.MISC.HELPDESK.GENERIC.CONTACT"/></td>
            		</tr>
          		</tbody>
        	</table>
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