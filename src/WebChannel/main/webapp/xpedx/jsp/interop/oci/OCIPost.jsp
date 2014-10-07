<%@ page import="java.util.Enumeration"%>

<%--
	This page simply posts the passed params to new XPEDXOciServlet.java
	This is necessary due to an implementation of the authentication layer, otherwise we could have customer use '/swc/interop/xoci' directly.
--%>

<html>
<head>
<script type="text/javascript">
	function submitForm(){
		document.SterlingOCI.submit();
	}
</script>
</head>
<body onload="submitForm()">
	<form name="SterlingOCI"  method="POST" action="/swc/interop/xoci">
<%
		Enumeration<String> parameterNames = request.getParameterNames();
	    while (parameterNames.hasMoreElements()){
	    	 String parameterName = parameterNames.nextElement();
	         String parameterValue = request.getParameter(parameterName);
%>
	    	<input type="hidden" id="<%=parameterName%>" name="<%=parameterName%>" value="<%=parameterValue %>">
<% 
	    }
	
%>
	</form>
</body>
</html>
