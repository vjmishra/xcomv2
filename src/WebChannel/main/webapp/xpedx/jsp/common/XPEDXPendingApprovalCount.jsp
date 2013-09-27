<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:set name='_action' value='[0]' />
<s:set name="selectedHeaderTab" value="#_action.getSelectedHeaderTab()"/>
 <s:set name="penAppOrderCount" value="#_action.getPendingOrderRecords()"></s:set>
   <s:if test="%{#penAppOrderCount !=0}">
		           	 	<s:url id='homeLink' namespace='/order' action='approvalList.action'>
							<s:param name="sfId"><s:property value="wCContext.storefrontId" /></s:param>
							<s:param name="pageNumber">1</s:param>
							<s:param name='scFlag'>Y</s:param>
						</s:url>
		            	
		            	<s:if test='#selectedHeaderTab=="OrdersAwaitingApproval"'>
		            		<li class="lighter active">
			            	<s:a href="%{homeLink}" cssClass="active"><s:property value="#penAppOrderCount"/> Orders Awaiting Approval</s:a>
		            	</s:if>
		            	<s:else>
		            		<li class="lighter">
		            		<s:a href="%{homeLink}"> <s:property value="#penAppOrderCount"/> Orders Awaiting Approval</s:a>
		            	</s:else>
  </s:if>
