<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<div style="display: none;">
<div id='copyCartNameDlg' style="width:325px;height:300px;overflow:auto;">
 		 <s:form name="copyOrder" id="copyOrder" method="post" action="draftOrderCopy" validate="true">
     <s:hidden name="OrderHeaderKey" id="OrderHeaderKey" value=""/>

<div class="xpedx-light-box" id="copy-cart">
<!--     <h2>Copy Cart</h2> --> 
    <h2><s:text name="MSG.SWC.CART.COPYCART.GENERIC.DLGTITLE" /></h2> 
	
	<!-- <div id="cp-cart-err-msg1" style="display: none;"><h5 align="left"><b><font color="red">Error: Please enter a Cart Description.</font></b></h5></div>  for Jira2919  -->
     <p>Name</p>
     <div class="clear"></div>
     
     <input maxlength="35" value="" name="copyCartName" id="copyCartName" class="x-input text copy-cart-field-length"/> 
     <p>Description</p>
     <div class="clear"></div>
     
     <textarea name="copyCartDescription" id="copyCartDescription"  onKeyUp="return  maxNewLength(this,'255');" rows="3" class="x-input copy-cart-field-length"></textarea>
     </br>
     <div class="clear"></div>
     
     <ul id="tool-bar" class="tool-bar-bottom" style="float:right";>
     	<li><a class="grey-ui-btn" href="#" onclick="$('#fancybox-close').click();return false;"><span>Cancel</span></a></li>
     	<li><a class="green-ui-btn button-margin" href="javascript:copyNewCart();"><span>Save</span></a></li>
      </ul>
 	</div>
 	<!--3098  -->
 	<br/><br/><div id="cp-cart-err-msg" style="display: none;"><h5 align="right" style="position:relative;bottom:20px;"><b><font color="red"> <s:text name='MSG.SWC.CART.VERIFY.ERROR.NONAME' /> </font></b></h5></div> 
	<!--3098  -->
 </s:form> 
	</div> 
</div>
<a href="#copyCartNameDlg" id="various2" style="display:none"></a> 
