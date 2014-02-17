<html>
<head>
</head>
<body onload="Form1.submit();">
  		<form action="%{#request.returnUrl}"  method="post" name="Form1">
			<hidden name="cxml-urlencoded" value="%{#request.cxml}"/>
		</form>
</body>
</html>