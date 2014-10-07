<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<a href="#xpedxDeleteCartDialog" id="xpedxDeleteCartDialog1" style="display:none"></a>
<div style="display: none;">
<div class="xpedx-light-box dialogue-box-adjustment" id="xpedxDeleteCartDialog">
<h1> <s:text name="MSG.SWC.CART.DELCART.GENERIC.DLGTITLE" /> </h1>
<p class="addmargintop0"><s:text name="MSG.SWC.CART.DELCART.GENERIC.CONFIRM" /></p>
<s:form name="delOrder" id="delOrder" method="post" validate="true">
	<s:hidden name="OrderHeaderKey" id="OrderHeaderKey" value="" />
	<s:hidden name="draft" id="draft" value="Y" />
	<div class="clearBoth"></div>
	<ul id="tool-bar" class="tool-bar-bottom float-right">
		<li><a class="btn-neutral"
			onclick="$('#fancybox-close').click();return false;" href="javascript:void();"><span>No</span></a></li>
		<li class="float-right"
			onclick="javascript:deleteCart(document.getElementById('delOrder')); ">
		<a class="btn-gradient" href="javascript:void();"><span>Yes</span></a></li>
	</ul>
</s:form></div>
</div>