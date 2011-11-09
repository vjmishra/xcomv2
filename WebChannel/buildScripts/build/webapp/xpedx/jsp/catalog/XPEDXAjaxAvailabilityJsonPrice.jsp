<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<s:set name='_action' value='[0]' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:set name='catDoc' value='%{outDoc.documentElement}' />
<s:set name='itemList' value='XMLUtils.getChildElement(#catDoc, "ItemList")'/>
<s:set name='scuicontext' value="#_action.getWCContext().getSCUIContext()" />
<s:set name='currency' value='#itemList.getAttribute("Currency")' />
<s:set name='showCurrencySymbol' value='true' />
<div>
	<table>
		<tr><td>Currency Code</td><td><s:property value="priceCurrencyCode" /></td></tr>		
		<tr>
			<td width="30%">UOM</td>
			<td width="30%">QTY</td>
			<td width="30%">PRICE</td>
		</tr>
		<s:iterator value='bracketsPricingList' id='bracket' status='bracketStatus'>
			<tr>
				<s:set name="bracketPriceForUOM" value="bracketPrice" />
				<s:set name="bracketUOMDesc" value="bracketUOM" />
				<s:set name='formattedBracketpriceForUom' value='#xpedxutil.formatPriceWithCurrencySymbol(#scuicontext,#currency,#bracketPriceForUOM,#showCurrencySymbol)' />
				<s:set name='formattedbracketUOM' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#bracketUOMDesc)' />

				<td><s:property value="%{#formattedbracketUOM}" /></td>
				<td><s:property value="bracketQTY" /></td>
				<td><s:property value='%{#formattedBracketpriceForUom}' /></td>
			</tr>
		</s:iterator>
	</table>
	</div>	


