<!--
	This page simply posts the passed params to new XPEDXOciServlet.java
  -->

<html>
<head>
<%@ page import="java.util.*"%>
<script type="text/javascript">

function callSterling(){

	document.SterlingOCI.submit();
}
</script>
</head>
<body onload="callSterling()">
<form name="SterlingOCI"  method="POST" action="/swc/interop/xoci">

<%
	Enumeration parameterNames = request.getParameterNames();
    while (parameterNames.hasMoreElements()){
    	 String parameterName = (String) parameterNames.nextElement();
         String parameterValue = request.getParameter(parameterName);
    	%>
    	<input type="hidden" name="<%=parameterName%>" 
    	id="<%=parameterName%>" value="<%=parameterValue %>">
    	<% 
    }

%>

</form>
</body>
</html>