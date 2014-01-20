<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Punchout Landing Page</title>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/punchout/po-xpedx.css"/>
</head>

<body>
<div class="ui-po-wrap">
  <div class="ui-po-brand"></div>
  <div class="ui-po-content">
    <div class="ui-po-search"> <span>Search for Ship To Location</span>
      <form action="" method="get" >
        <div class="ui-po-searchinput">
          <input type="text" class="ui-searchbox" />
        </div>
        <div class="ui-po-floatbtn">
          <div class="magnify-icon"><img width="16" height="15" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/punchout/ui-po-magnifying.png"/></div>
          <input name="search" type="button" class="po-search-btn" />
        </div>
      </form>
    </div>
    <div class="ui-po-errormsg">Error message can go here.</div>
    <div class="clearfix" ></div>
    <div class="ul-po-results">Search Results for ""</div>
    <ul class="ui-po-list">
      <li><a href="#">Search Result 1</a></li>
      <li><a href="#">Search Result 2</a></li>
    </ul>
  </div>
</div>
</body>
</html>
