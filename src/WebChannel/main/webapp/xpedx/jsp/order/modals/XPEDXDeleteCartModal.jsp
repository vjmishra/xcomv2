<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<a href="#xpedxDeleteCartDialog" id="xpedxDeleteCartDialog1" style="display:none"></a>
<div style="display: none;">
<div class="xpedx-light-box" id="xpedxDeleteCartDialog">
<!-- <h2>Delete Cart</h2> -->
<h2> <s:text name="MSG.SWC.CART.DELCART.GENERIC.DLGTITLE" /> </h2>
<!-- <p>Are you sure you would like to delete this cart?</p> -->
<s:text name="MSG.SWC.CART.DELCART.GENERIC.CONFIRM" />
<br />

<s:form name="delOrder" id="delOrder" method="post" validate="true">
	<s:hidden name="OrderHeaderKey" id="OrderHeaderKey" value="" />
	<s:hidden name="draft" id="draft" value="Y" />
	<div class="clearBoth"></div>
	 
	<ul>
		<li><a class="grey-ui-btn"
			onclick="$('#fancybox-close').click();return false;" href="javascript:void();"><span>No</span></a></li>
		<li class="float-right"
			onclick="javascript:deleteCart(document.getElementById('delOrder')); ">
		<a class="green-ui-btn float-right" href="javascript:void();"><span>Yes</span></a></li>
	</ul>
	
</s:form></div>
</div>