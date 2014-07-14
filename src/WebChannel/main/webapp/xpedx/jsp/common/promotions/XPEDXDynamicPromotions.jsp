<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ page import="com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants" %>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>

<%
String url = (String)request.getAttribute(XPEDXConstants.REQUEST_ATTR_PROMO_PAGE_URL);
 %>
<c:set var="urlVar" value="<%=url%>" scope="page"/>
<c:import url="${urlVar}"/> 
