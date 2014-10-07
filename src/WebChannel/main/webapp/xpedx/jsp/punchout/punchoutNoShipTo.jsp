<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Select Ship-To</title>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/punchout/po-xpedx<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/tablesorter/docs/js/chili/js-jquery/javascript<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
</head>
<body class="bodyclass">
<div class="ui-po-wrap">
  <div class="ui-po-brand"></div>
  
<!-- -----------New Content----------- --> 
    <div class="ship-to-message">
      <div><img src="../xpedx/images/punchout/ship-to-truck.png" width="59" height="37" alt="timeout" /></div>
      				
      <p>We're sorry, no ship-to locations have been assigned to your account. To continue, please contact eBusiness Customer Service.</p>

<p><strong>eBusiness Customer Support Desk: 1-877-269-1784 or <a href="mailto:ebusiness@veritivcorp.com">ebusiness@veritivcorp.com</a></strong></p>
    </div>
 
 <!-------------------------------------->   
    
    <div class="clearfix" ></div>
</div>
</body>
</html>