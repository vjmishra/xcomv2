<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>

<s:form action="%{#formAction}" namespace="%{#formNamespace}"  validate="true" method="POST">


<s:if test="%{#request.errornote!= null}">
	 <div class="error" style="display : inline; align:center">
	 <font color="red"><b><s:property escape="false" value="%{#request.errornote}"/>  </b></font><br>
	</div>
  	
	
	
</s:if>
    
<s:if test="%{#request.sessiondata!= null}">
	 <div id="errorNote1" class="error" style="display : inline; float: right">
	 <font color="blue"><b>Session Data</b></font><br>
		<s:iterator value="%{#request.sessiondata}" id="sessiondata"  >
        	<s:property escape="false" value="%{#sessiondata}"/>  
        	<br>
  		</s:iterator>
  	</div>
  	
	
	
</s:if>

<s:if test="%{#request.reqdata!= null}">
	 <div id="errorNote2" class="error" style="display : inline; float: right">
	 <br><br><font color="blue"><b>Request Object Data</b></font><br>
		<s:iterator value="%{#request.reqdata}" id="reqdata"  >
        	<s:property escape="false" value="%{#reqdata}"/>  
        	<br>
  		</s:iterator>
  	</div>
  	
	
	
</s:if>

<!--  
<s:if test="%{#request.reqparamdata!= null}">
	 <div id="errorNote3" class="error" style="display : inline; float: right">
	<br><br> <font color="blue"><b>Request Param Data</b></font><br>
		<s:iterator value="%{#request.reqparamdata}" id="reqparamdata"  >
        	<s:property escape="false" value="%{#reqparamdata}"/>  
        	<br>
  		</s:iterator>
  	</div>
  	
	
	
</s:if>
-->

</s:form>

