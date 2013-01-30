<%@ page language="java" contentType="application/json"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="pwdValidationResultMap!=null">
<div id="pwdErrorDiv" class="error">
			<s:iterator value="pwdValidationResultMap" id="pwdValidationResultMap" status="status">
			
			<s:set name="errorDesc" value="value" />
			<s:set name="errorCode" value="key" />	
			<s:property value="#errorDesc"/>
				<br/>		
			
		</s:iterator>
		Please revise and try again.
	</div>
			
</s:if>
