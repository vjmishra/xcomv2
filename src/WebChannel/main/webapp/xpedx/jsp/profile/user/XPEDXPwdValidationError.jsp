<%@ page language="java" contentType="application/json"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="pwdValidationResultMap!=null">
			<s:iterator value="pwdValidationResultMap" id="pwdValidationResultMap" status="status">
			<div id="pwdErrorDiv" class="error">
			<s:set name="errorDesc" value="value" />
			<s:set name="errorCode" value="key" />	
			<s:property value="#errorDesc"/>
			</div>			
			<br/>
			<br/>
		</s:iterator>
	
</s:if>
