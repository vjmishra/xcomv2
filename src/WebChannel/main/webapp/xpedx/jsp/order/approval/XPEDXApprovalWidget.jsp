<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
    This is to avoid a defect in Struts that�s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>
<s:set name='_action' value='[0]'/>
<s:set name='isApprover' value="#_action.getIsApprover()" />
<s:if test='#isApprover == true'>

 <s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
 <s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
 <s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='dateUtilBean'/>
 <s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
	<s:set name="xutil" value="#_action.getXMLUtils()"/>
	<!-- get the search result xml: getOutputDocument is an action method -->
	<s:set name='approvalWidgetListdoc' value="outputDocument" id='approvalWidgetListDoc'/>


	<!-- // begin main-content -->
	
	<table id="mil-list" width="951" border="0">
	<tr id="none" class="table-header-bar">
		<td class="no-border table-header-bar-left"><span class="white"> Web Confirmation</span> </td>
		<td class="no-border" align="center"><span class="white">Order Number</span></td>
		<td class="no-border" align="center"><span class="white">Customer Purchase Order</span></td>
		<td class="no-border" align="center"><span class="white">Order Date</span></td>
		<td class="no-border" align="center"><span class="white">Order Owner</span></td>
        <td class="no-border" align="center"><span class="white"> Ship To</span></td>
        <td class="no-border" align="center"><span class="white"> Amount</span></td>
        <td class="no-border-right table-header-bar-right" align="center" colspan="2"><span class="white"> Status </span></td>
	</tr>
	<s:set name="parentOrderList"
		value="#util.getElements(#approvalWidgetListdoc, '//Page/Output/OrderList/Order')" />

	<s:iterator id='parentOrder' status='rowStatus' value='parentOrderList'>
		<s:set name="priceInfo"
			value='#parentOrder.getElementsByTagName("PriceInfo")' />
		<s:set name='currencyCode'
			value='%{#priceInfo.item(0).getAttribute("Currency")}' />
		<s:set name='priceWithCurrency'
			value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #priceInfo.item(0).getAttribute("TotalAmount"))' />
		<s:set name="orderDate"
			value='%{#dateUtilBean.formatDate(#parentOrder.getAttribute("OrderDate"),wCContext)}' />
		<s:set name='OrderExtn'
			value='#xutil.getChildElement(#parentOrder,"Extn")' />
		<s:set name='webConfirmationNumber'
			value='#OrderExtn.getAttribute("ExtnWebConfNum")' />
		
		<s:if test="#rowStatus.odd == true">
			<tr class="odd">
		</s:if>
		<s:else>
			<tr>
		</s:else>
			<td><s:url id="orderDetailsURL" action="orderDetail"
				escapeAmp="false">
				<s:param name="orderHeaderKey"
					value='#parentOrder.getAttribute("OrderHeaderKey")' />
				<s:param name="orderListReturnUrl" value='#returnUrl' />
			</s:url> <s:a href="%{orderDetailsURL}">
				<s:property value='#webConfirmationNumber' />
			</s:a></td>
			
			<td><s:text name="Not given in OOTB code"></s:text></td>

			<td><s:property
				value='#parentOrder.getAttribute("CustomerPONo")' />
			</td>

			<td><s:property value='#orderDate' /></td>

			<td><s:property
				value='#OrderExtn.getAttribute("ExtnOrderedByName")' /></td>
				
			<td><s:text name="Ship to Not given in OOTB code"></s:text></td>

			<td>
				<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
					<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
					<s:if test="%{#priceWithCurrency == #priceWithCurrencyTemp}">
						<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
		                		</s:if>
		                        <s:else>
						(<s:property value='#currencyCode' />) <s:property value='#priceWithCurrency' /> 
					</s:else>
				</s:if>
			</td>

			<td><s:property value='#parentOrder.getAttribute("Status")' /></td>

		</tr>
            </s:iterator>
</table>
                     <!-- table end-->

   <!-- // table list end -->

</s:if>

