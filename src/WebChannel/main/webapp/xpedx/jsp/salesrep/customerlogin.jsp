<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>Auto Login Inprogress...</title>
</head>

<body onload="document.singForm.submit();">
	<div>
  		<s:form action="login" namespace="/common" method="post" name="singForm" id="singForm" cssStyle="margin: 15px 0 0 50px;">
			<h2>Please wait... the home page is loading ...</h2>
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