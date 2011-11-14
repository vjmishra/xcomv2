<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set name="wcCtx" value="WCContext"/>

<s:iterator value="quickLinkBeanArray">	
<s:if test='(showQuickLink == "Y")'>
	<li>					
		<a href="<s:property value='quickLinkURL'/>" class="underlink" target="_blank"><s:property value="urlName"/></a>
	</li>	
	</s:if>		
</s:iterator>	
	
