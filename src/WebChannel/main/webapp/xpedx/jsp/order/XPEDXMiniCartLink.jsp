<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs’. 
    This is to avoid a defect in Struts that’s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts’ OGNL statements. --%>
<s:set name='_action' value='[0]'/>

<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name='orderHeaderKey' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("OrderHeaderInContext")'/>
<s:set name="loggedincustomer" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getLoggedInCustomerFromSession(wCContext) ' />
<s:if test='#orderHeaderKey == null && #loggedincustomer != null'>
	<s:set name='orderHeaderKey' value='@com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper@getCartInContextOrderHeaderKey(wCContext)'/>
</s:if>

<s:set name="orderTotalInCache" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("CommerceContextHelperOrderTotal")' />
<s:if test="(#orderTotalInCache != null) && (#orderTotalInCache.size()>0 )" >
	<s:set name="itemAndTotalList" value="#orderTotalInCache" />
</s:if>
<s:set name="orderTotal" value="#itemAndTotalList.get(0)" />
<s:set name="numbrOfItems" value="#itemAndTotalList.get(1)" />
<s:set name='currencyCode'  value='#itemAndTotalList.get(2)' />
<s:if test="(#numbrOfItems == null) || (#yfcCommon.isVoid(#numbrOfItems))">
	<s:set name="numbrOfItems1" value="0" />		
</s:if>
<s:else>
	<s:set name="numbrOfItems1" value="#numbrOfItems" />
		
</s:else>
<s:if test="(#orderTotal == null) || (#yfcCommon.isVoid(#orderTotal))">
	<s:set name="orderTotal1" value="0" />	
</s:if>
<s:else>
	<s:set name="orderTotal1" value="#orderTotal" />
</s:else>
<input type="hidden" name="carddetailOrderHeaderKey" id="carddetailOrderHeaderKey" value='<s:property value="#orderHeaderKey"/>' />

<s:property value='#numbrOfItems1'/> Items 
<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
<s:set name="theMyPrice" value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#orderTotal1)'/>
			<s:if test="%{#theMyPrice == '$0.00' && #numbrOfItems1 > 0}">
					<s:set name="isMyPriceZero" value="%{'true'}" />
					<span class="gray">&nbsp;&nbsp;&nbsp;TBD </span>  
             </s:if>
             <s:else>
             	&nbsp;&nbsp;&nbsp;
             	<s:if test='#currencyCode != null && #currencyCode != ""'>
             		(<s:property value='#currencyCode'/>)
             	</s:if>
					  <s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#orderTotal1)'/>
			 </s:else>
</s:if>