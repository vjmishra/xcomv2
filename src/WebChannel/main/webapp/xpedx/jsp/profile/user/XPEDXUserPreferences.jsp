<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc" %>
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs’. 
    This is to avoid a defect in Struts that’s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts’ OGNL statements. --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<s:set name='_action' value='[0]'/>

	 <div style="position: relative; left: 50px; height: 250px; width:250px">
		<s:if test='%{#assetURL == null}'>
			<b><s:text name="Preferred Category:"></s:text></b>
		</s:if>
		<s:else>
			<b><s:text name="Preferred Category "></s:text></b>
			<br/>
		</s:else>
		<a href='<s:url action="navigate" namespace="/catalog" 
     			includeParams="all" >
					<s:param name="path" value="prefCategoryPath"/>
					<s:param name="cname" value="displayName"/>
			</s:url> '>
			<s:if test='%{#assetURL != null}'>
				<img src="<s:property value="assetURL" />" />
			</s:if>
			<s:else>
				<s:property value='displayName' />
			</s:else>
    	</a>    	 	         	
     </div>