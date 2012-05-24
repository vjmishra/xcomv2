<!-- This JSP file is use for drop down of draft Orders -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<s:set name='_action' value='[0]' />

<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/draft-order-list.css" />

<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/draftOrderList.js"></script>
<script type="text/javascript">
function loadCartDetails(){
	document.doddForm.OrderHeaderKey.value = document.doddForm.draftOrders.value;
   	document.doddForm.submit();
}
</script>

<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
<s:set name='sdoc' value="outputDoc" />
<s:set name='activeCartKey'
		value='@com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper@getCartInContextOrderHeaderKey(wCContext)' />
<s:form name="doddForm" namespace="/order" id="doddForm"
	action="draftOrderDetails">
	<s:hidden name="OrderHeaderKey" id="OrderHeaderKey" value="" />
	<s:hidden name="draft" id="draft" value="Y" />
	<p><b>Shopping For : </b></p>
	<div><select style="width: 100px; float: left" name="draftOrders"
		class="xpedx_select_sm" tabindex="81" id="draftOrders">
		<s:iterator id='cart' value='%{cartList}' status="rowStatus">
			<s:if test="#activeCartKey==#cart.key">
				<option selected="selected" value="<s:property value='#cart.key'/>"><s:property
					value='#cart.value' /></option>
			</s:if>
			<s:elseif test="#rowStatus.index == 1">
				<option selected="selected" value="<s:property value='#cart.key'/>"><s:property
					value='#cart.value' /></option>
			</s:elseif>
			<s:else>
				<option value="<s:property value='#cart.key'/>"><s:property
					value='#cart.value' /></option>
			</s:else>
		</s:iterator>
	</select>
	<div class="view_cart" style="margin-left: 5px; float: left"
		align="left">
	<p><a href="javascript:loadCartDetails();">View Cart</a></p>
	</div>
	</div>
</s:form>


