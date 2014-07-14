<!-- This JSP file is use as view for order list widget. -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/order-list<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs’. 

    This is to avoid a defect in Struts that’s creating contention under load. 

    The explicit call style will also help the performance in evaluating Struts’ OGNL statements. --%>

<s:set name='_action' value='[0]'/>
<s:set name='sdoc' value="outputDoc"/>
 <s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
 <s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
 <s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='dateUtilBean'/>
<s:set name="xutil" value="XmlUtils"/>
 <s:url id="orderlistUrl" action="orderList">
 </s:url>
 <s:url id="orderListWidgetSortURL" action="portalHome" namespace="/home">
    <s:param name="orderByAttribute" value="'{0}'"/>
    <s:param name="orderDesc" value="'{1}'"/>
</s:url>
<s:set name='returnUrl' value='%{#orderlistUrl}'/> <!-- Use for return url on order detail page to order list page. -->

	<!-- // begin main-content -->
	    		            <!-- table list -->
		         
	<swc:sortctl sortField="%{orderByAttribute}"
                  sortDirection="%{orderDesc}" down="Y" up="N"
                  urlSpec="%{#orderListWidgetSortURL}">
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
		value="#util.getElements(#sdoc, '//Page/Output/OrderList/Order')" />

	<s:iterator id='parentOrder' status='rowStatus' value='parentOrderList'>
		<s:set name='OrderExtn'
			value='#xutil.getChildElement(#parentOrder,"Extn")' />
		<s:set name='webConfirmationNumber'
			value='#OrderExtn.getAttribute("ExtnWebConfNum")' />
		
	<tbody>
		<tr <s:if test="#rowStatus.odd == true">class="odd"</s:if> style="border-top: 1px solid #D7D7D7;">

			<td><s:url id="orderDetailsURL" action="orderDetail"
				escapeAmp="false">
				<s:param name="orderHeaderKey"
					value='#parentOrder.getAttribute("OrderHeaderKey")' />
				<s:param name="orderListReturnUrl" value='#returnUrl' />
			</s:url> <s:a href="%{orderDetailsURL}">
				<s:property value='#webConfirmationNumber' />
			</s:a></td>

			    		<s:set name="chainedOrderList" value='xpedxChainedOrderListMap.get(#parentOrder.getAttribute("OrderHeaderKey"))'/>
	            		<s:set name="chainedOrderListSize" value='#chainedOrderList.size()'/>
	            		<s:if test='xpedxChainedOrderListMap.containsKey(#parentOrder.getAttribute("OrderHeaderKey")) && #chainedOrderListSize > 1'>
	                    <td rowspan="1">
	                        <img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/XPEDX-linked-arrow-closed<s:property value='#wcUtil.xpedxBuildKey' />.gif" onclick="javascript:linkedRowToggle('<s:property value='#parentOrder.getAttribute("OrderHeaderKey")'/>');"/>
	            			Split Order
	            		</td>
						<td></td>
				    	<td></td>
				    	<td></td>
				    	<td></td>
				    	<td></td>
				    	<td></td>
            	
	                    </s:if>
	                    <s:else>
	                    	<s:set name="chainedOrderListSize" value='#chainedOrderList.size()'/>
	                    	<s:set name="chainedOrder" value='#chainedOrderList.get(0)'/>
	                    	<s:set name='ChainedOrderExtn' value='#xutil.getChildElement(#chainedOrder,"Extn")'/>
	                    	<s:set name="priceInfo" value='#chainedOrder.getElementsByTagName("PriceInfo")'/>
			        		<s:set name='currencyCode' value='%{#priceInfo.item(0).getAttribute("Currency")}'/>
			        		<s:set name='priceWithCurrency' value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #priceInfo.item(0).getAttribute("TotalAmount"))'/>
			        		<s:set name="orderDate" value='%{#dateUtilBean.formatDate(#chainedOrder.getAttribute("OrderDate"),wCContext)}'/>
				        	<s:set name='ChainedOrderExtn' value='#xutil.getChildElement(#chainedOrder,"Extn")'/>
							<s:set name='ChainedLegacyOrderNumber' value='#ChainedOrderExtn.getAttribute("ExtnLegacyOrderNo")'/>
							<s:set name='ChainedLegacyOrderBranch' value='#ChainedOrderExtn.getAttribute("ExtnOrderDivision")' />
							<s:set name='ChainedLegacyGenerationNumber' value='#ChainedOrderExtn.getAttribute("ExtnGenerationNo")' />
							
							<%-- Fetching the legacy order number, legacy order division, legacy generation number
							and show the fulfillment order link only when Legacy Order no is present --%>
							<s:set name="displayOrderNumber" value="%{''}" /> 
	                    	<s:if test='#ChainedLegacyOrderBranch != "" ' >
	                    		<s:set name="displayOrderNumber" value="#ChainedLegacyOrderBranch" />
	                    	</s:if>
	                    	<s:if test='#ChainedLegacyOrderNumber != ""' >
	                    		<s:if test='#displayOrderNumber!= ""' >
	                    			<s:set name="displayOrderNumber" value="%{#displayOrderNumber + '-' + #ChainedLegacyOrderNumber}" />
	                    		</s:if>
	                    		<s:else>
	                    			<s:set name="displayOrderNumber" value="%{#ChainedLegacyOrderNumber}" />
	                    		</s:else>
	                    	</s:if>
	                    	<s:if test='#ChainedLegacyGenerationNumber != ""' >
	                    		<s:if test='#displayOrderNumber!= ""' >
	                    			<s:set name="displayOrderNumber" value="%{#displayOrderNumber + '-' + #ChainedLegacyGenerationNumber}" />
	                    		</s:if>
	                    		<s:else>
	                    			<s:set name="displayOrderNumber" value="%{#ChainedLegacyGenerationNumber}" />
	                    		</s:else>
	                    	</s:if>
							
		                	<td>
                				<s:url id="chainedOrderDetailsURL" action="orderDetail" escapeAmp="false" >
									<s:param name="orderHeaderKey" value='#chainedOrder.getAttribute("OrderHeaderKey")'/>
									<s:param name="orderListReturnUrl" value='#returnUrl'/>  
								</s:url>
								<s:if test='#ChainedLegacyOrderNumber!=""'>
									<s:a href="%{chainedOrderDetailsURL}">
										<s:property value='#displayOrderNumber'/>
									</s:a>
								</s:if>
			    			</td>
		        
							<td><s:property value='#chainedOrder.getAttribute("CustomerPONo")'/></td>
	           	
							<td><s:property value='#orderDate'/></td>
					            	
						    <td><s:property value='#ChainedOrderExtn.getAttribute("ExtnOrderedByName")'/></td>
				            	
						    <td><s:property value='#ChainedOrderExtn.getAttribute("ExtnShipToName")'/></td>
				            	
						    <td><s:property value='#priceWithCurrency'/></td>
				            	
				    		<td><s:property value='#chainedOrder.getAttribute("Status")'/></td>
				     
	                    </s:else>
	            		
	            	</tr>
	            		</tbody>
		<tbody id="ChildOf_<s:property value='#parentOrder.getAttribute("OrderHeaderKey")'/>" style="display:none;">
            	<s:iterator value='xpedxChainedOrderListMap'>
            		<s:set name='chainedOrderListKey' value='key'/>
                	<s:set name='chainedOrderList' value='value'/>
                	<s:iterator value='#chainedOrderList' id='chainedOrder' status='iStatus'>
                		<s:if test='#chainedOrderListKey==#parentOrder.getAttribute("OrderHeaderKey")'>
                		<s:set name="priceInfo" value='#chainedOrder.getElementsByTagName("PriceInfo")'/>
		            	<s:set name='currencyCode' value='%{#priceInfo.item(0).getAttribute("Currency")}'/>
		            	<s:set name='priceWithCurrency' value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #priceInfo.item(0).getAttribute("TotalAmount"))'/>
		            	<s:set name="orderDate" value='%{#dateUtilBean.formatDate(#chainedOrder.getAttribute("OrderDate"),wCContext)}'/>
		            	<s:set name='ChainedOrderExtn' value='#xutil.getChildElement(#chainedOrder,"Extn")'/>
						<s:set name='ChainedLegacyOrderNumber' value='#ChainedOrderExtn.getAttribute("ExtnLegacyOrderNo")'/>
						<s:if test='#ChainedLegacyOrderNumber==""'>
							<s:set name='ChainedLegacyOrderNumber' value="''"/>
						</s:if>
            			<tr <s:if test="#rowStatus.odd == true">class="odd"</s:if> style="border-top: 1px solid #D7D7D7;">
                			<td>  &nbsp;</td>
			            	
			            	<td>
			            		<s:url id="chainedOrderDetailsURL" action="orderDetail" escapeAmp="false" >
								  <s:param name="orderHeaderKey" value='#chainedOrder.getAttribute("OrderHeaderKey")'/>
								  <s:param name="orderListReturnUrl" value='#returnUrl'/>  
								</s:url>
								<s:if test='#ChainedLegacyOrderNumber !=""'>
								<s:a href="%{chainedOrderDetailsURL}">
								  <s:property value='#ChainedLegacyOrderNumber'/>
								</s:a>
								</s:if>
			            	</td>
			            	
		            		<td><s:property value='#chainedOrder.getAttribute("CustomerPONo")'/></td>
           	
			            	<td><s:property value='#orderDate'/></td>
			            	
			            	<td><s:property value='#ChainedOrderExtn.getAttribute("ExtnOrderedByName")'/></td>
			            	
			            	<td><s:property value='#ChainedOrderExtn.getAttribute("ExtnShipToName")'/></td>
			            	
			            	<td><s:property value='#priceWithCurrency'/></td>
			            	
			            	<td><s:property value='#chainedOrder.getAttribute("Status")'/></td>
			            </tr>
			            </s:if>
            		</s:iterator>
            	</s:iterator>
                </tbody> 
            </s:iterator>
</table></swc:sortctl>
                	
                  
		


