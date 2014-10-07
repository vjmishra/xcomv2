<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<html>
<head>
<title>Auto Login In Progress...</title>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' /><s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' /><s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/punchout/po-xpedx<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
</head>

<body onload="document.singForm.submit();">
<script>
$(document).ready(function() {
    $(".loading-wrap").css('display','block');
	$(".loading-icon").css('display','block');
	$("body").css("overflow", "hidden");
      
});

</script> 

	<div>
  		<s:form action="login" namespace="/common" method="post" name="singForm" id="singForm" cssStyle="margin: 15px 0 0 50px;">
			<s:hidden name="DisplayUserID" value="%{#request.dum_username}"/>
			<s:hidden name="Password" value="%{#request.dum_password}"/>
			<s:hidden name="payLoadID" value="%{#request.payLoadID}"/>
			<s:hidden name="operation" value="%{#request.operation}"/>
			<s:hidden name="returnURL" value="%{#request.returnURL}"/>
			<s:hidden name="orderHeaderKey" value="%{#request.orderHeaderKey}"/>			
			<s:hidden name="selectedCategory" value="%{#request.selectedCategory}"/>
			<s:hidden name="selectedItem" value="%{#request.selectedItem}"/>
			<s:hidden name="selectedItemUOM" value="%{#request.selectedItemUOM}"/>
			<s:hidden name="buyerCookie" value="%{#request.buyerCookie}"/>
			<s:hidden name="fromIdentity" value="%{#request.fromIdentity}"/>
			<s:hidden name="toIdentity" value="%{#request.toIdentity}"/>
			<s:hidden name="amiProcurementUser" value="%{#request.isProcurementUser}"/>
			<s:hidden name="EnterpriseCode" value="%{#request.selected_storefrontId}"/>

			<a href="javascript:(function(){document.singForm.submit();})();"><span></span></a>
		</s:form>
	</div>
	<div class="loading-wrap"  style="display:none;">
         <div class="load-modal" ><div class="loading-icon" style="display:none;"></div></div>
    </div>
</body>
</html>