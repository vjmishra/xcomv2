<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<html>
<head>
<title>Auto Login Inprogress...</title>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>

</head>

<body onload="document.singForm.submit();">

<script type="text/javascript">
Ext.onReady(function() {    
	//Added For Jira 2903
	Ext.Msg.wait("Processing..."); 
   });
</script> 

	<div>
  		<s:form action="login" namespace="/common" method="post" name="singForm" id="singForm" cssStyle="margin: 15px 0 0 50px;">
			<s:hidden name="DisplayUserID" value="%{#request.dum_username}"/>
			<s:hidden name="Password" value="%{#request.dum_password}"/>
			<s:hidden name ="loggedInUserName" value="%{#session.loggedInUserName}"/>
			<s:hidden name ="loggedInUserId" value="%{#session.loggedInUserId}"/>
			<s:hidden name ="SRSalesRepEmailID" value="%{#session.SRSalesRepEmailID}"/>
			<s:hidden name="EnterpriseCode" value="%{#request.selected_storefrontId}"/>
			<s:hidden name="isSWCReq" value="true"/>
			<a href="javascript:(function(){document.singForm.submit();})();"><span></span></a>
		</s:form>
	</div>
</body>
</html>