<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
<s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />

<script type="text/javascript">
		var isUserAdmin = <s:property value="#isUserAdmin"/>;
	</script>	
<%--Added condition for Jira 3195 --%>
<div id="divAdd2ListRadio">
<s:if test="listOfItems == null || listOfItems.size()==0">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div class="error" style="margin-top:60px;">No lists have been created. Please create a new list. </div>
</s:if>
<table>

<s:iterator id="listDetail" value="listOfItems" status="listIndex" >
	<tr>
	<td>
		<input type="radio" name="itemListRadio" id="itemListRadio" value="<s:property value="#listDetail.getAttribute('MyItemsListKey')"/>"
		onclick='javascript:currentAadd2ItemList =  this; currentAadd2ItemListIndex = "<s:property value='#listIndex.index'/>";' />
		</td><td>&nbsp;</td><td><s:if test="#listDetail.getAttribute('SharePrivate').length()>0">
		     <img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/20x20_personal_list.png" alt="List Img" />
		</s:if>
		<s:else>
		    <img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/20x20_shared_list.png" alt="List Img" />
		</s:else>
		</td><td>&nbsp;</td><td><s:property value="#listDetail.getAttribute('ListName')"/></td>
		</tr>
	</s:iterator>
	</table>	
	<s:iterator id="listSizeId" value="%{listSizeMap.keySet()}" status="listItemCountIndex" >
		<s:set name="itemCount" value="%{listSizeMap.get(#listSizeId)}" />
		<s:hidden name="itemCount_%{#listSizeId}" id="itemCount_%{#listSizeId}" value="%{#itemCount}" />
	</s:iterator>
	<s:url id="MIListPaginationURL" action="MyItemsList">
    	 <s:param name="pageNumber" value="'{0}'"/>
    	 <s:param name="displayAsRadioButton" value="true"/>
 	</s:url>
	<div id="tool-bar-bottom" class="float-bottom">
  
 <div class="search-pagination-bottom">
                  <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;
                  	<xpedx:pagectl currentPage="%{getPageNumber()}" divId="divAdd2ListRadio" lastPage="%{totalNumberOfPages}" showFirstAndLast="False" urlSpec="%{#MIListPaginationURL}" isAjax="true"/>
                  	
 	</div>
</div>
	
</div>