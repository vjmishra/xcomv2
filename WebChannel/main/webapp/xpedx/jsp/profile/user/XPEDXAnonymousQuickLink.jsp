<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set name="wcCtx" value="WCContext"/>

	<div class="anon-hp-quick-links">
	    <h2>Quick Links</h2>
	    <ul>
		    <s:iterator value="quickLinkMap.entrySet()">							
				<li><a href="<s:property value='%{value}'/>"><s:property value="%{key}"/></a></li> 
			</s:iterator>                   
	    </ul>
	</div>


