<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<html>
<head>
<title>Auto Login Inprogress...</title>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' /><s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' /><s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

</head>

<body onload="document.singForm.submit();">

<script type="text/javascript">
Ext.onReady(function() {    
	//Added For Jira 2903
	Ext.Msg.wait("Processing..."); 
   });
</script> 

	<div>
  		<s:form action="login" namespace="/common" method="post" name="singForm" id="singForm" cssStyle="margin: 15px 0 0 50px;">
			<s:hidden name="DisplayUserID" value="%{#request.dum_username}"/>
			<s:hidden name="Password" value="%{#request.dum_password}"/>
			
	
			
			<s:hidden name="_payloadID" value="%{#request._payloadID}"/>
			<s:hidden name="_operation" value="%{#request._operation}"/>
			<s:hidden name="_returnURL" value="%{#request._returnURL}"/>
			<s:hidden name="_selectedCategory" value="%{#request._selectedCategory}"/>			
			<s:hidden name="_selectedCategory" value="%{#request._selectedCategory}"/>
			<s:hidden name="_selectedItem" value="%{#request._selectedItem}"/>
			<s:hidden name="_selectedItemUOM" value="%{#request._selectedItemUOM}"/>
			<s:hidden name="_buyerCookie" value="%{#request._buyerCookie}"/>
			<s:hidden name="_fromIdentity" value="%{#request._fromIdentity}"/>
			<s:hidden name="_toIdentity" value="%{#request._toIdentity}"/>
			<s:hidden name="amiProcurementUser" value="Y"/>
			
			
			<s:hidden name="EnterpriseCode" value="%{#request.selected_storefrontId}"/>
			
			
		
			
			
			<!--  Add these parameters into this page -->
			<a href="javascript:(function(){document.singForm.submit();})();"><span></span></a>
		</s:form>
	</div>
</body>
</html>