<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<!-- Show IE as IE8 -->
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 <title>Sales Professional Sign In</title>
<%-- <title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.SPRO.LGIN.GENERIC.TABTITLE"/> </title> --%>

<!-- styles 
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/theme-xpedx_v1.2.css" />
-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/salesrep/lib<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/salesrep/SpryTooltip<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/salesrep/style<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript">

function clearFields(field){
    	    document.salesRepForm.reset();
    		var eDiv = document.getElementById("errMsg");
    		eDiv.innerHTML = "";
}

function ReplaceAll(Source,stringToFind,stringToReplace){
	  var temp = Source;
	    var index = temp.indexOf(stringToFind);
	        while(index != -1){
	            temp = temp.replace(stringToFind,stringToReplace);
	            index = temp.indexOf(stringToFind);
	        }
	        return temp;
}

//Start- fix for 3108
function validateForm()
{	
	var rtn = true;
	var eDiv = document.getElementById("errMsg");
	if(document.salesRepForm.Password.value=="" && document.salesRepForm.DisplayUserID.value=="")
    {
      document.salesRepForm.DisplayUserID.focus();
      eDiv.innerHTML = "<div style=\"display: inline;color: #FF0000; font-size: 1.1em; text-align: left;\">Please enter Username and Password.</div>"
      rtn = false;
    }
	else if(document.salesRepForm.DisplayUserID.value=="")
    {
      document.salesRepForm.DisplayUserID.focus();
      rtn = false;
      eDiv.innerHTML = "<div style=\"display: inline;color: #FF0000; font-size: 1.1em; text-align: left;\">Please enter Username.</div>"
    }
    else if(document.salesRepForm.Password.value=="")
    {
      document.salesRepForm.Password.focus();
      eDiv.innerHTML = "<div style=\"display: inline;color: #FF0000; font-size: 1.1em; text-align: left;\">Please enter Password.</div>"
      rtn = false;
    }
	if (rtn == false){
		document.getElementById("InvalidCredentials").innerHTML = "";
		//commented for 3108
		//eDiv.innerHTML = "<div style=\"color: #FF0000; font-size: 1.1em; text-align: left;\">Please enter username and password.</div>"
	}
	else {
	document.salesRepForm.DisplayUserID.value=document.salesRepForm.DisplayUserID.value.toLowerCase(); //added for JIRA 3936
		document.salesRepForm.submit();
	}
    return rtn;
}
//End- fix for 3108

function validateOnEnter(e){

	if (e.keyCode == 13) 
	{ 
		validateForm();        
	  return ;     
	} 
}
function loadWindow(){
   <s:url id='alogoutURL' namespace='/common' action='salesrepLogout'></s:url>
        
    var url = "<s:property value='alogoutURL'/>";
    url = ReplaceAll(url,"&amp;",'&');
	Ext.Ajax.request({
		url: url,
		method: 'POST',
		params: { logoutMethod: "AJAX" },
		success: function (response, request){
		},
		failure: function (response, request){
		}
	});
}

</script>

<style type="text/css">
/* Green */
a.green-ui-btn {
	background: transparent
		url('<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/ui-buttons/orange-bg-btn-a.png')
		no-repeat scroll top right;
	color: #FFFFFF;
	display: block;
	float: left;
	font: bold 12px arial, sans-serif;
	height: 22px;
	margin-right: 0;
	padding-right: 18px;
	text-decoration: none;
	margin-top:10px;
	margin-bottom:10px;
}

a.green-ui-btn span {
	background: transparent
		url('<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/ui-buttons/orange-bg-btn-span.png')
		no-repeat;
	display: block;
	line-height: 12px;
	padding: 5px 0 5px 18px;
}

a.green-ui-btn:active {
	background-position: bottom right;
	color: #FFFFFF;
	outline: none;
}

a.green-ui-btn:active span {
	background-position: bottom left;
	padding: 5px 0 5px 18px;
	}
	
td p{margin-bottom: 0px; }
#login {     margin-left: 44%;
    margin-top: 50px;
    padding-top: 0;
}
.error {
	background-color: #F9E5E6;
	border: 1px solid #E8AAAD;
	color: #B50007;
	padding-left: 5px;
	padding-right: 5px;
}

span.forgot.underlink a:link{
	text-decoration: none;
		color: #003399;
}

span.forgot.underlink a:hover{
	text-decoration: underline;
	color: #003399; 
}

#footer
{
color:#cccccc;
background-image:none;
}

#container 
{
min-height: 700px;
}

p.access { margin-top:0px; }
h1 { margin-bottom: 0px; padding-top:200px; }
</style>



</head>

<body onload="clearFields();">
<div id="container">
<div id="login">
<div>
</div>
	<s:form action="/common/salesRepLogin.action" method="post" name="salesRepForm" id="salesRepForm" >
<div>
 <h1>Sign In</h1> 
<%-- <h1> <s:text name="MSG.SWC.SPRO.LGIN.GENERIC.PGTITLE"/></h1> --%>
<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="2">
		<p class="access"><strong>Access your customers</strong></p>
		</td>
	</tr>
	<tr>
		<td width="34%">
		<p><label for="DisplayUserID"><s:text name="Username"/></label></p>
		
		</td>
	</tr>
	<tr>
		<td width="66%">
			<s:textfield id="DisplayUserID" name="DisplayUserID"  cssClass="x-input" tabindex="1"/>
		</td>	
	</tr>
	<tr>
		<td>
		<p><label for="Password"><s:text name="Password"/></label></p>
		</td>
	</tr>
	<tr>
		<td style="text-align:left;">
			<input type="password" id="Password" name="Password" class="x-input" tabindex="2" onkeypress="return validateOnEnter(event);"/>
		</td>
	</tr>
	<tr>
		<td>
			<%--<span class="forgot underlink" style="float:left;"> <a href="#" title="Your Username/Password is the same as your Network ID/Password." id="sprytrigger1"> Forgot Password?</a></span> --%>
			<br/>
			<span style="float:left;width:400px;">Your Username/Password is the same as your Network ID/Password.</span>
		</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td style="text-align:left;">
		 <a name="button" id="button" class="green-ui-btn" href="#" onclick="return validateForm();"> <span>Sign In</span></a><br /> 
		
		<%-- <input name="button" type="image" id="button"  value="Submit" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/salesrep/signin.png" onClick="return validateForm();"/> <br /> --%>
		 
		</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
	<td colspan="2">
	<s:if test="%{(#session.ERROR_MESSAGE != '') && (#session.ERROR_MESSAGE != null)}">
		<div id="InvalidCredentials" style="display: inline; font-size: 1.1em; text-align: left;color: #FF0000";>
		<s:text name="Please enter a valid Username and Password." />
		</div>
	</s:if>
	
	<div id="errMsg"> </div>
	</td>
	</tr>
	 <tr>
		<td colspan="2">
			
		</td>	
	</tr> 
	<tr>
		<td colspan="2"><span>Questions: Contact the support desk at 877 269-1784</span></td>
		
	</tr>
</table>

</div>
   <s:hidden name="SALES_REP_LOGIN_PAGE" value="true"/>
   <s:hidden name="isSWCReq" value="true"/>
 
 <%
 	request.setAttribute("SCUI_USER_TRYING_TO_LOGIN", true);
 %>
</s:form>

<div id="footer">
	<p>© International Paper Company. All rights reserved.</p>
</div>

</div> <!-- End id="login" -->
</div> <!-- End id="container" -->

<%-- <script type="text/javascript">
 //var sprytooltip1 = new Spry.Widget.Tooltip("sprytooltip1", "#sprytrigger1");
 </script> --%>
 
</body>
</html>
