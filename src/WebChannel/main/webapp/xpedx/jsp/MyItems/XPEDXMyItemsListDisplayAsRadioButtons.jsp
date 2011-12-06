<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
<s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />

<script type="text/javascript">
		var isUserAdmin = <s:property value="#isUserAdmin"/>;
	</script>	

<div id="divAdd2ListRadio">
<table>
	<s:iterator id="listDetail" value="listOfItems" status="listIndex" >
	<tr>
	<td>
		<input type="radio" name="itemListRadio" id="itemListRadio" value="<s:property value="#listDetail.getAttribute('MyItemsListKey')"/>"
		onclick='javascript:currentAadd2ItemList =  this; currentAadd2ItemListIndex = "<s:property value='#listIndex.index'/>";' />
		</td><td>&nbsp;</td><td><s:if test="#listDetail.getAttribute('SharePrivate').length()>0">
		     <img src="<s:url value='/xpedx/images/theme/theme-1/20x20_personal_list.png'/>" alt="List Img" />
		</s:if>
		<s:else>
		    <img src="<s:url value='/xpedx/images/theme/theme-1/20x20_shared_list.png'/>" alt="List Img" />
		</s:else>
		</td><td>&nbsp;</td><td><s:property value="#listDetail.getAttribute('Name')"/></td>
		</tr>
	</s:iterator>
	</table>	
	<s:iterator id="listSizeId" value="%{listSizeMap.keySet()}" status="listItemCountIndex" >
		<s:set name="itemCount" value="%{listSizeMap.get(#listSizeId)}" />
		<s:hidden name="itemCount_%{#listSizeId}" id="itemCount_%{#listSizeId}" value="%{#itemCount}" />
	</s:iterator>
	
</div>