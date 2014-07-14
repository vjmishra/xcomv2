
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:set name='_action' value='[0]' />
<s:set name='addToCartError' value= "addToCartError" />
<s:set name='orderMultipleErrorItems' value= "orderMultipleErrorItems" />
<s:hidden name="addToCartErrors" id="addToCartErrors" value="%{#addToCartError}"/>
<s:hidden name="orderMultipleErrorItemsList" id="orderMultipleErrorItemsList" value="%{#orderMultipleErrorItems}"/>
