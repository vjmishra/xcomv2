<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>



<div id="divSpecialItemContent_<s:property value="#uId"/>"  class="add-non-catalog-container" style="display: none">
	
	<div class="xpedx-light-box" id="inline1" style="height: 305px; width: 550px;">
	<!-- <h2>Add Non-Catalog Item</h2> -->
	 <h2><s:text name="MSG.SWC.CART.NONCAT.GENERIC.DLGTITLE" /></h2> 
	<br />

	<span class="margin-top-none"><strong>HINT:</strong> Use this
	form to add items you can't find in the online catalog, or would like to
	special order. Items added here may be deleted from your shopping cart
	at any time</span> <br />

	<div>
		<p><span id="catalog-descr">Description:</span> 
		<s:textarea name="siUIDesc_%{#uId}"  cssClass="margin-left-10" rows="3" cols="50" onkeyup="restrictTextareaMaxLength(this,'255');" theme="simple"/>
		</p>
	</div>

	<div>
		<div class="add-non-catalog-labels">
		<span class="add-non-catalog-qty-label">Qty:</span> 
		<s:textfield tabindex="12" cssClass="input-label add-non-catalog-qty-input" 
			name="siUIQty_%{#uId}" value="%{qty}" maxlength="7"
			onkeyup="javascript:isValidQtyRemoveAlpha(this);" theme="simple" /> 
			
		<span class="bold">UOM:</span>
	
		<s:textfield tabindex="12" cssClass="input-label add-non-catalog-UOM-input" 
		 name="siUIUom_%{#uId}" value=" " maxlength="16" theme="simple" /></div>
	</div>
	<s:hidden name="siUIJobId_%{#uId}" value="%{jobId}" /> 
	<%-- <input type="hidden" name="siUIJobId_%{#uId}" value="%{jobId}" /> --%>

	<ul class="tool-bar-bottom-right" id="tool-bar">
		<li><a href="javascript:$.fancybox.close();"
			class="grey-ui-btn button-margin"> <span>Cancel</span> </a></li>
		<li><a
			href="javascript:addSpecialItem('<s:property value="#uId"/>');"
			class="green-ui-btn"> <span>Add to Quick List</span> </a></li>
	</ul>
</div>
</div>

