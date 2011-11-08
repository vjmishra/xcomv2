<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

	<s:set name="json" value='pnaHoverMap.get(#itemId)'/>
	<s:hidden name="itemPrice_%{itemId}" value="itemId"></s:hidden>
	<s:property value="itemId" />