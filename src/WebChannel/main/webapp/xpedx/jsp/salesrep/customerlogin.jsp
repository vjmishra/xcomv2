<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>Auto Login Inprogress...</title>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/ext-all.css" />
<script type="text/javascript" src="/swc/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/ext-all.js"></script>

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
			<s:hidden name="EnterpriseCode" value="%{#request.selected_storefrontId}"/>
			<a href="javascript:(function(){document.singForm.submit();})();"><span></span></a>
		</s:form>
	</div>
</body>
</html>