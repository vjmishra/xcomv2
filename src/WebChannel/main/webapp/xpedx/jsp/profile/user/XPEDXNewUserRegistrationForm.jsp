<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en"
	xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />

<s:include value="../../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/home/home.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/home/portalhome.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/catalog/narrowBy.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/catalog/catalogExt.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/global/ext-all.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/theme-xpedx_v1.2.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" />.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/xpedx-mil-new.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/xpedx-quick-add.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/prod-details.css"/>
 
<!-- jQuery -->
<link type="text/css" href="/swc/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all.css" rel="stylesheet" />

<script type="text/javascript" src="/swc/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../../xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/ext-all.js"></script>

<script type="text/javascript" src="/swc/xpedx/js/jquery.ui.core.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery.ui.widget.js"></script>

<script type="text/javascript" src="<s:url value="/xpedx/js/fancybox/jquery.fancybox-1.3.4.js" />"></script>
<!-- Web Trends tag start -->
<script type="text/javascript" src="/swc/xpedx/js/webtrends/displayWebTag.js"></script>
<!-- Web Trends tag end  -->

<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});	
</script>
<script type="text/javascript">
    function userRegSubmit(){
    	//var errorDiv = document.getElementById("errorMsg");
    	var errorDiv = document.getElementById("errorMsgForMandatoryFields");
     	document.getElementById('newUserName').value = document.getElementById('newUserFirstName').value ;
           	
     	if( document.getElementById('newUserFirstName').value.trim().length == 0 ||  document.getElementById('newUserCompanyName').value.trim().length == 0
    			||  document.getElementById('newUserEmail').value.trim().length == 0 ||  document.getElementById('newUserPhone').value.trim().length == 0)
   		{
     		//alert("Fields can't be empty, Input required");
    		//errorDiv.innerHTML = "<s:text name='MSG.SWC.MISC.REGISTER.GENERIC.INVALIDFIELDS' />";
    		
    		 errorDiv.innerHTML = "<s:text name='MSG.SWC.MISC.REGISTER.GENERIC.INVALIDFIELDS' />";
    	     //usernameField.style.borderColor="#FF0000";
    	     errorDiv.style.display = 'inline';
    		//return false;
   		}     	
    	else
    	{
	    	//alert("Submitting registrationForm ..");
	    	document.registrationForm.submit();
	    	return true;
    	} 
    }

</script>

<title><s:property value="wCContext.storefrontId" /> / <s:text name='MSG.SWC.MISC.REGISTER.GENERIC.TABTITLE' /></title>
</head>

<body class="ext-gecko ext-gecko3">
<div id="main-container">
<div id="main" class="anon-pages">

<s:action name="xpedxHeader" executeResult="true" namespace="/common" />

<div class="container">
<!-- breadcrumb -->
       
<div id="mid-col-mil"> 
    
<s:form name="registrationForm" namespace='/profile/user'
	action='XPEDXNewUserRegistrationAction' validate="true">
<!-- Webtrends Start  -->
<meta name="DCSext.w_x_reg" content="1" /> 
<!-- Webtrends End  -->
<s:hidden name='newUserName' id='newUserName' value="" />

<s:url id='homePage' namespace='/home' action='home' />

<div class="error" id="errorMsg" style="display : none"></div>



<div>
	<div class="padding-top3 black page-title"><strong class="black"><s:text name='MSG.SWC.MISC.REGISTER.GENERIC.PGTITLE' /></strong></div>
</div>
<div class=" padding-bottom clearview"> </div>
    <!-- <p> Please fill out the information below and a customer service representative will follow up with you to get your ID set up. </p>  -->
    <p> <s:text name='MSG.SWC.MISC.REGISTER.GENERIC.GENINFO' />  </p> 
	<br>
<table class="full-width">
	<tbody>
		<tr>
    		<td style="width:180px;" class="underlines no-border-right-user">Name: </td>
			<td style="width:278px;" class="underlines no-border-right-user" ><s:textfield name="newUserFirstName" id="newUserFirstName" cssClass="x-input width-250px" maxlength="50"/></td>
		</tr>

		<tr>
  			<td width="160" class="underlines no-border-right-user">Company:</td>
  			<td class="underlines no-border-right-user"><s:textfield id="newUserCompanyName" name="newUserCompanyName" theme="simple" cssClass="x-input width-250px" maxlength="35"/></td>
  			<td class="underlines no-border-right-user">&nbsp;</td>
  			<td class="underlines no-border-right-user">&nbsp;</td>
		</tr>
		
		<tr>
  			<td width="160" class="underlines no-border-right-user">Email Address:</td>
  			<td class="underlines no-border-right-user"><s:textfield id="newUserEmail" name="newUserEmail" theme="simple" cssClass="x-input width-250px" maxlength="150"/></td>
			<td class="underlines no-border-right-user">&nbsp;</td>
			<td class="underlines no-border-right-user">&nbsp;</td>
		</tr>
		
		<tr>
			<td width="160" class="underlines no-border-right-user">Phone (Include area code):</td>
			<td class="underlines no-border-right-user"><s:textfield id="newUserPhone" name="newUserPhone" theme="simple" cssClass="x-input width-250px" maxlength="10"/></td>
  			<td class="underlines no-border-right-user">&nbsp;</td>
  			<td class="underlines no-border-right-user">&nbsp;</td>
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
              <td colspan="3" class="no-border-right-user"><div style="width: 263px;">
                <ul class="float-right  padding-left3 margin-right-10">
                	<li class="float-left margin-10"><s:a href='%{homePage}' cssClass="grey-ui-btn"><span>Cancel</span></s:a></li>
					<li class="float-right"><a href="javascript:userRegSubmit();" class="orange-ui-btn"><span>Submit</span></a></li>
                </ul>
              </div></td>
              
		   <td class="underlines no-border-right-user">&nbsp;</td>
              <td class="underlines no-border-right-user">&nbsp;</td>
            </tr>
            
            
             <tr>            
              <td colspan="4" class="grey  no-border-right-user"><div id="errorMsgForMandatoryFields" style="float:right; margin-right: 12px;display:none; margin-right: 110px;" class="error" ></div></td>
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

</div>
<!-- end main  -->

<s:action name="xpedxFooter" executeResult="true" namespace="/common" />

<!-- end container  -->
</body>

</html>