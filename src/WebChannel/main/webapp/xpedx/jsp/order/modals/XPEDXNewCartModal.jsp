<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<div style="display: none;">
	<div id='createNewCartDlg' style="width:305px;height:280px;overflow:auto;">
		 <s:form name="createOrder" id="createOrder" method="post" action="draftOrderCreate" validate="true">
     <s:hidden id="action_namespace" name="#action.namespace" value="/order"/>
     <s:hidden id="actionName" name="#action.name" value="draftOrderCreate"/>

<div class="xpedx-light-box" id="new-cart">
     <!--  <h2>New Cart</h2> --> 
      <h2><s:text name="MSG.SWC.CART.NEWCART.GENERIC.DLGTITLE" /></h2> 
<!-- <div id="cr-cart-err-msg1" style="display: none;"><h5 align="left"><b><font color="red">Error: Please enter a Cart Description.</font></b></h5></div> for Jira2919 -->
      <p>Name</p>
      <div class="clear"></div>
      
      <input value="" name="OrderName" id="newcartname" class="x-input text copy-cart-field-length" maxlength="35"/> 
      <p>Description</p>
      <div class="clear"></div>
      
      <textarea name="orderDescription" id="orderDescription" rows="3" class="x-input text copy-cart-field-length" maxlength="255" onkeyup="javascript:restrictTextareaMaxLength(this,255);"></textarea>
      </br> <div class="clear"></div>
      
    	<ul id="tool-bar" class="tool-bar-bottom-right">
    	<li><a class="grey-ui-btn button-margin"  href="#" onclick="$('#fancybox-close').click();return false;"><span>Cancel</span></a></li>
      	<li><a class="green-ui-btn" href="javascript:saveNewCart();"><span>Save</span></a></li>
      </ul>
 	</div> 
	  <!--3098  -->
    <div id="cr-cart-err-msg" style="display: none;"><h5 align="right" style="position:relative;bottom:20px;"><b><font color="red"> <s:text name='MSG.SWC.CART.VERIFY.ERROR.NONAME' /> </font></b></h5></div>
      <!--3098  -->
 </s:form>
	</div> 
</div>
	     