<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Thank you</title>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/punchout/po-xpedx<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
</head>
<body class="bodyclass">
	<div class="ui-po-wrap">
		<div class="ui-po-brand"></div>
		<div class="ui-po-content">
		    <div class="po-thanks"> <h2>Thank you.</h2>
		    	<p class="thanks-text">Your cart has been saved and can be accessed next time you log in.</p>      
		    </div>
		   	<div class="clearfix" ></div>   
		</div>
	</div>
</body>
</html>