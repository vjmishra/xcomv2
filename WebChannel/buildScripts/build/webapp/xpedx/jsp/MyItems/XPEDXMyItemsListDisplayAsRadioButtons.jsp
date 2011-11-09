<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
<s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />

<script type="text/javascript">
		var isUserAdmin = <s:property value="#isUserAdmin"/>;
	</script>	

<div id="divAdd2ListRadio">

	<s:iterator id="listDetail" value="listOfItems" status="listIndex" >
		<input type="radio" name="itemListRadio" id="itemListRadio" value="<s:property value="#listDetail.getAttribute('MyItemsListKey')"/>"
		onclick='javascript:currentAadd2ItemList =  this; currentAadd2ItemListIndex = "<s:property value='#listIndex.index'/>";' />
		<s:property value="#listDetail.getAttribute('Name')"/>
		<br/><br/>
	</s:iterator>
	
	<s:iterator id="listSizeId" value="%{listSizeMap.keySet()}" status="listItemCountIndex" >
		<s:set name="itemCount" value="%{listSizeMap.get(#listSizeId)}" />
		<s:hidden name="itemCount_%{#listSizeId}" id="itemCount_%{#listSizeId}" value="%{#itemCount}" />
	</s:iterator>
	
</div>