<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:action name="xpedxDraftOrderListUserInfo" executeResult="false"
		namespace="/order" >
  <s:param name="CustomerContactId" value='%{row.getAttribute("Modifyuserid")}'/>
</s:action>
<s:set name='userName' value='#attr["USER_NAME"]' />

<s:if test='%{#userName!=null}'>
<s:property value='%{#userName}'/>
</s:if>
