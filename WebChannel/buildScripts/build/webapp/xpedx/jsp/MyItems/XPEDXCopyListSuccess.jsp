<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<s:set name='_action' value='[0]' />
<s:hidden id="listKey" name="listKey" value="%{#_action.getListKey()}" />
<s:hidden id="listName" name="listName" value="%{#_action.getListName()}" />
<s:hidden id="listDesc" name="listDesc" value="%{#_action.getListDesc()}" />