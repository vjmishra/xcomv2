<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<div class="listTableContainer2"">
<table>
	<tr>
		<td>
		<h5><b><font color="red"><s:property
			value="ajaxLineStatusCodeMsg" /></font></b></h5>
		<h1><s:property value='%{ItemExtendedDescription}' /></h1>
		</td>
		<td></td>
	</tr>
	<tr>
		<td>
		<h5><s:property value='%{ItemShortDescription}' /></h5>
		</td>
		<td>
		    <s:select name="UOM" id="UOM" list="displayItemUOMsMap" listKey="value" listValue="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(key)" disabled="%{#isReadOnly}" tabindex="%{#tabIndex}" theme="simple" />	
		</td>
	</tr>
	<tr>
		<td>
		<h6><s:property value='%{ItemDescription}' /></h6>
		<s:hidden id="ITemId" name="ITemId" value="%{ItemExtendedDescription}" />
		</td>
		<td></td>
	</tr>
	<tr>
		<td><s:text name="Qty"></s:text></td>
		<td><s:textfield name="Qty" id="Qty" onkeyup="javascript:isValidQtyRemoveAlpha(this);"/></td>
	</tr>
	<tr>
		<td><s:text name="Job">JOB#</s:text></td>
		<td><s:textfield name="Job" id="Job" /></td>
	</tr>
	<tr>
		<td><s:text name="customer">customer#</s:text></td>
		<td><s:textfield name="customer" id="customer" /></td>
	</tr>
	<tr>
		<td>Currency Code</td>
		<td><s:property value="priceCurrencyCode" /></td>
	</tr>
	<tr>
		<td><s:iterator id='abc1' value="%{displayPriceForUoms}">
			<h6><s:property value="abc1" /><br>
			</h6>
		</s:iterator></td>
		<td></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="button" id="dialogCart" value="Add to Cart"
			class="submitBtnBg1" onClick="javascript:openData();" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<s:if test='%{isBracketPricing =="true" }'>
			<td><input type="button" id="btntPricingDialog"
			value="BracketPricing" class="submitBtnBg1"
			onClick="javascript:openDialog();" /></td>
		</s:if>
	</tr>
	<tr colspan='2'>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<h6><s:property value='%{Details}' /></h6>
		</td>
		<td></td>
	</tr>
	<tr id="<s:property escape="false" value="pnaJson" />">
	</tr>
	<tr colspan='2'>
		<td>&nbsp;</td>
	</tr>
</table>

</div>
