 <%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<s:set name='_action' value='[0]' />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<s:if test='%{#_action.getQuantitydraftError() == "true"}'>
We were unable to add some items to your cart as there was an invalid quantity in your list. Please correct the qty and try again.
</s:if>
<s:else>
<h5 align='center'><b><font color=red>This cart has already been submitted, please refer to the Order Management page to review the order.</font></b></h5>
</s:else>
</body>
</html>