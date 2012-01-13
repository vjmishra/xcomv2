<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%--
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/swc.css" />
 --%>
	<s:set name="isMergedCSSJS" value="(#request.isMergedCSSJS )"/>
  	  
  	<s:if test="(#isMergedCSSJS == null && #isMergedCSSJS != 'true')"> 
  		<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/swc.css" />
  	</s:if>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/theme-xpedx_v1.2.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" />.css" />
<%--
<link media="all" type="text/css" rel="stylesheet" href="/swc/<s:property value="wCContext.storefrontId" />/css/order/shopping-cart.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/<s:property value="wCContext.storefrontId" />/css/theme/xpedx-mil-new.css"/>
 --%>


