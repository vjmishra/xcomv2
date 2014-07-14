<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div class="topBorder width-400" style="margin:0px;" >
	<ul class="padding-bottom3">
	<s:if test='accountsMap!=null && accountsMap.size()>0' >
		<s:iterator id="accountDetail" value="accountsMap" status="accIndex" >
			<li>
				<input type="radio" name="customerId" id="customerId_<s:property value="key"/>" value="<s:property value="key"/>" />
				<s:property value="value"/>
			</li>
		<br/>
		</s:iterator>
	</s:if>
	<s:else>
		<li>
			<h5 align="center"><b><font color="red">Your request could not be completed at this time, please try again.</font></b></h5>	
		</li>
	</s:else>
	</ul>
</div>