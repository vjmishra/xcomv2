<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.StringTokenizer" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>

   <% 
 String tag = "";
 String content = "";
 int cnt=0;
 String noOfTags = "";
 tag = (String)request.getParameter("tag");
 content = (String)request.getParameter("content"); 
 if(request.getParameter("noOfTags")!= null)
 {
	 noOfTags = (String)request.getParameter("noOfTags"); 
 }  
 
 if((noOfTags.equalsIgnoreCase("undefined")== false)&& (noOfTags.isEmpty()==false)) {
	 cnt = Integer.parseInt(noOfTags);
	 StringTokenizer stTag = new StringTokenizer(tag,","); 
	 StringTokenizer stContent = new StringTokenizer(content,",");
 	for (int i=0; i<cnt; i++){
 		tag =stTag.nextElement().toString();
 		content = stContent.nextElement().toString();
 %> 
 <meta name ="<%=tag%>" content="<%=content%>" />
  <%} } else{ %> 
  <meta name ="<%=tag%>" content="<%=content%>" />
  <%} %>
 
 </head>
 <body >
 
 </body>
</html>