<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:set name='_action' value='[0]'/>
<s:set name="xutil" value="#_action.getXMLUtils()"/>
<!--<s:set name='orderLines' value='#xutil.getChildElement(row, "OrderLines")' />
<s:set name='orderDesc' value='#xutil.getChildElement(row, "Extn")' />


--><s:url id="draftOrderDetailsURL" action="draftOrderMakeCartInContextDetails">
  <s:param name="OrderHeaderKey" value='%{row.getAttribute("OrderHeaderKey")}'/>
  <s:param name='draft' value='"Y"'/>
</s:url>
<s:url id="draftOrderDetailsMakeCartInContext" action="draftOrderMakeCartInContext">
  <s:param name="OrderHeaderKey" value='%{row.getAttribute("OrderHeaderKey")}'/>
</s:url>
<s:if test='%{(row.getAttribute("OrderHeaderKey") != cartInContextOrderHeaderKey)}'>
    <s:set name='makeCartInContextLabel' value='#_action.getText("MakeActiveCart")'/>
    <s:a href="%{draftOrderDetailsMakeCartInContext}" title="%{makeCartInContextLabel}" >  
 		<img height="20" width="20" align="left" title="Make active cart" alt="Cart Icon" src="/swc/xpedx/images/theme/theme-1/20x20_white_cart_off.png" style="margin-right: 4px; margin-top:4px;"> 
<!--     	&nbsp;&nbsp; -->
    </s:a>
    <s:a href="%{draftOrderDetailsURL}" cssClass="underlink">
      <s:property value='%{value}'/> (<s:property  value="%{row.getAttribute('TotalNumberOfItems')}"/>)
    </s:a>
</s:if>
<s:else>
	<s:a href="%{draftOrderDetailsURL}" cssClass="underlink">
	   <img height="20" width="20" align="left" title="Active cart" alt="active cart" src="/swc/xpedx/images/theme/theme-1/20x20_blue_cart_on.png" style="margin-right: 4px; margin-top:4px;"> 
<!-- 	  &nbsp;&nbsp; -->
      <s:property value='%{value}'/> (<s:property  value="%{row.getAttribute('TotalNumberOfItems')}"/>)
    </s:a>
</s:else>

</div>
<p class="grey-mil"><s:property  value="%{row.getAttribute('ExtnOrderDesc')}"/></p>