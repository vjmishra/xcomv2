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

<script type="text/javascript" src="../js/global/ext-base.js"></script>
<script type="text/javascript" src="../js/global/ext-all.js"></script>
<script type="text/javascript" src="../js/global/validation.js"></script>
<script type="text/javascript" src="../js/global/dojo.js"></script>
<script type="text/javascript" src="../js/global/dojoRequire.js"></script>
<script type="text/javascript" src="../js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="../js/catalog/catalogExt.js"></script>

<!-- carousel scripts css  -->

<link media="all" type="text/css" rel="stylesheet" href="../js/jcarousel/skins/xpedx/theme.css" />
<link media="all" type="text/css" rel="stylesheet" href="../js/jcarousel/skins/xpedx/skin.css" />

<!-- carousel scripts js   -->

<script type="text/javascript" src="../js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript" src="../js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="../js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="../js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="../js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="../js/quick-add/quick-add.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="../js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<script src="../js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="../js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="../../js/xpedx-new-ui.js" language="javascript">
	
</script>


<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>

<!-- Fancy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="../js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="../js/fancybox/jquery.fancybox-1.3.1.js"></script>

<link rel="stylesheet" type="text/css" href="../js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox-1.3.1.css" media="screen" />

<script src="../js/SpryTabbedPanels.js" type="text/javascript"></script>
 
<style>
#mil-list-new .form a:link{text-decoration:none;color:#000;}
#mil-list-new .form a:visited{text-decoration:none;color:#000;}
#mil-list-new .form a:hover{text-decoration:underline; color:#003366;}
#mil-list-new .form a:active{text-decoration:none; color:#000;}
</style>

<link media="all" type="text/css" rel="stylesheet" href="<s:url value='/swc/css/user/my-account.css'/>" />
<script type="text/javascript" src="<s:url value='/swc/js/home/forgotPassword.js'/>"></script>
<!-- <title>Forgot Password</title> -->
<% if(null != request.getParameter("requestId")){%>
	<title>Reset Password </title>
<%}else{ %>
  <title>Forgot Password </title>
<%}%>
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
      		<div id="mid-col-mil"> 
		    <div>
      			<% if(null != request.getParameter("requestId")){%>
		           <div class="padding-top3 page-title black"><strong class="black"> <s:text name="Reset.Password"/></strong></div>
		   	    <%}else{ %>
      		       <div class="padding-top3 page-title black"><strong class="black"> <s:text name="MSG.SWC.MISC.FORGOTPASSWORD.GENERIC.PGTITLE"/></strong></div>
      		     <%}%>
			</div>
			<div class=" padding-bottom clearview"> </div>
			
		<!-- begin progress bar -->
		<div id="second-navigation">
		<% if(null == request.getParameter("requestId")){%>
             <ul id="main-nav" class="dropdown">
				<li >
				     <span class="link" >Confirm your identity</span>
				</li>
				<li>
				     <span class="link">Answer your security question</span>
				</li>
				<li class="active">
				    <span class="link" >Password reset confirmation</span>
				 </li>
             </ul>
              <%}%>
		</div>
		<div class="clearall">&nbsp;</div>
		
		<!--  end progress bar -->
		
			<table class="full-width">
				<tbody>
					<tr>
						<s:set name="sucessmessage" value='%{#_action.getSuccessMessageType()}'/>
						<br/>
						<s:if test="%{#sucessmessage == 'passwordchange'}">
							<td style="border:0px; padding-left:0px;"><s:text name="success.pwdchange"/></td>
					    </s:if>
					    <s:elseif test="%{#sucessmessage == 'requestreset'}">
					    	<td style="border:0px; padding-left:0px;"><s:text name="request.pwdreset"/></td>
					    </s:elseif>
          			</tr>
          			<tr>
          			<% if(null != request.getParameter("requestId")){%>
						<td colspan="3" class="grey no-border-right-user"width= "75%" style="float:left; margin-right: 10px;">
              			<a href="<s:url action="login" namespace="/common"/>">Click Here to Login</a></td> <%}%>
              			
               		</tr>
					<tr>
              			<td colspan="3" class="grey no-border-right-user"><s:text name="MSG.SWC.MISC.HELPDESK.GENERIC.CONTACT"/></td>
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