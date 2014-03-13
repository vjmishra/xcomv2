<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Receiver</title>

<script>
	function PostData() {
		document.getElementById("Form1").submit()
	}
</script>
</head>

<body onload="PostData();">
	<form method="post"  action='<s:property value="returnUrl"/>' id="Form1" name="Form1" enctype="application/x-www-form-urlencoded">
		<s:set name="cxml" value="cxmlData"/>
		<s:if test='(cxmlData != null) && !cxmlData.isEmpty()'>
			<input type="hidden" name="cxml-urlencoded" value='<s:property value="cxml"/>'/>
		</s:if>
		<s:else>
			<s:property value="ociData" escape="false"/>
		</s:else>
	</form>
	
	<%
	response.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader( "Expires", 0 );
	session.invalidate();%>
	
</body>
</html>
