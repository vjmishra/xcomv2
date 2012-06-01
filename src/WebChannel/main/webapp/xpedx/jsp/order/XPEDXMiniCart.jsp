<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs’. 
    This is to avoid a defect in Struts that’s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts’ OGNL statements. --%>
 
    
<s:set name='_action' value='[0]'/>

<s:bean name="com.sterlingcommerce.webchannel.utilities.UtilBean" id="util" />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shopping-cart.css" />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil'
	id='priceUtil' />
<s:bean name='org.apache.commons.lang.StringUtils' id='strUtil' /> <!-- added for 2769jira -->

<div class="xpedx-light-box" id="mini-cart" style="display:block;">
<%-- <h2>Mini Cart<span style="font-size:10px;font-family:Arial;"> (Last five items)</span></h2> --%>
<h2><s:text name='MSG.SWC.CART.MINICART.GENERIC.DLGTITLE' /> <span style="font-size:10px;font-family:Arial;"> <s:text name='MSG.SWC.CART.MINICART.INFO.LAST5ITEMS' /> </span></h2>
<div class="clearall">&nbsp;</div>

<s:set name='sdoc' value='outputDocument'/>
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name='priceInfo' value='#util.getElement(#sdoc, "/Order/PriceInfo")' />
<s:set name='currencyCode' value='#priceInfo.getAttribute("Currency")'/>
<s:set name='isProcurementUser' value='wCContext.isProcurementUser()'/>
<s:set name='isProcurementInspectMode' value='#util.isProcurementInspectMode(wCContext)'/>
<s:set name='isReadOnly' value='#isProcurementInspectMode'/>
<s:url id='cartDetailURL' action='draftOrderDetails.action'>
    <s:param name="orderHeaderKey" value='orderHeaderKey'/>
    <s:param name='draft' value='"Y"'/>
</s:url>

<s:if test='!#isProcurementUser'>
    <s:url id='checkoutURL' action='miniCartCheckout' namespace="/order" escapeAmp="false">
        <s:param name="OrderHeaderKey" value='orderHeaderKey'/>
        <s:param name="draft" value="%{'Y'}"/>
    </s:url>
</s:if>
<s:else>
    <s:if test='#isProcurementInspectMode'>
        <s:set name='checkoutButtonText' value='%{#_action.getText("ProcurementCancelAndReturn")}'/>
        <s:url id='checkoutURL' action='procurementPunchOut' namespace="/order" escapeAmp="false">
            <s:param name='mode' value='"cancel"'/>
            <s:param name='draft' value='"Y"'/>
        </s:url>
        <s:url id="returnURL" action="draftOrderDetails" escapeAmp="false">
            <s:param name='OrderHeaderKey' value='orderHeaderKey'/>
            <s:param name='draft' value='"Y"'/>
        </s:url>
    </s:if>
    <s:else>
        <s:set name='checkoutButtonText' value='%{#_action.getText("ProcurementSaveAndReturn")}'/>
        <s:url id='checkoutURL' action='procurementMiniCartSaveAndReturn' namespace="/order" escapeAmp="false">
            <s:param name='mode' value='"save"'/>
            <s:param name='draft' value='"Y"'/>
        </s:url>
    </s:else>
</s:else>

<s:url id='updateURL' action='miniCartUpdate' namespace="/order" escapeAmp="false"/>
 <div>
<s:form action="miniCartUpdate" id="miniCartData" name="miniCartData" namespace="/order" method="POST" validate="true">
<s:hidden name='#action.name' id='validationActionName' value='miniCartUpdate'/>
<s:hidden name='#action.namespace' value='/order'/>
<s:hidden name='updateURL' value='%{#updateURL}'/>
<s:hidden name='checkoutURL' value='%{#checkoutURL}'/>
<s:hidden name='fullBackURL' value='%{#cartDetailURL}'/>
<%--Added  OrderLinesCount for Validating Jira 3481--%>
<s:hidden id="OrderLinesCount" name="OrderLinesCount" value='%{majorLineElements.size()}' />
<!-- cart rows -->
<div class="mini-cart-rows">


<s:if test='!minimalMiniCart'>
    <s:if test="majorLineElements.size() > 0">
    
    <!--gray header -->
	<div class="mini-cart-header">
		<table border="0px solid red">
			<tbody>
				<tr>
					<td class="mini-cart-header-sect1">&nbsp;</td>
					<td class="mini-cart-header-sect2">Item #</td>
					<td style="width:100px;"class="mini-cart-header-sect3">Qty</td>
					<td	align="right" style="width:200px" class="mini-cart-header-sect4 mini-cart-extended-price text-right">Extended Price(USD)</td>
				</tr>
			</tbody>
		</table>
	</div>
 	<div class="mini-cart-rows">
	  	<table width="100%" border="0px solid red" >
		<tbody>  
	    	<s:set name="counter" value="0" />			
	        <s:iterator value='majorLineElements' id='orderLine'>
	        	<s:set name="counter" value="#counter + 1"/>
	        	<s:if test='#counter < 6'> 
	            <s:set name='item' value='#util.getElement(#orderLine, "Item")'/>
	            <s:set name="orderLineTran" value="#util.getElement(#orderLine, 'OrderLineTranQuantity')" />
	            <s:set name='orderLineExtn' value='#util.getElement(#orderLine, "Extn")'/>
	            <s:set name='lineTotals' value='#util.getElement(#orderLine, "LineOverallTotals")'/>
	            <s:set name='orderLineKey' value='#orderLine.getAttribute("OrderLineKey")'/>
	            <s:set name="itemUOMs" value='%{#item.getAttribute("UnitOfMeasure")}'/>
	            <s:set name="BaseUOMs" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#itemUOMs)' />
				<s:hidden name="BaseUOMs" id="BaseUOMs_%{#orderLineKey}" value='%{#BaseUOMs}' /> 
	            <s:if test='#isProcurementInspectMode'>
	                <s:url id='detailURL' namespace='/catalog' action='itemDetails.action'>
	                    <s:param name='itemID'><s:property value='#item.getAttribute("ItemID")'/></s:param>
	                    <s:param name='unitOfMeasure'><s:property value='#item.getAttribute("UnitOfMeasure")'/></s:param>
	                    <s:param name='goBackFlag'>true</s:param>
	                    <s:param name='_r_url_' value='%{returnURL}'/>
	                </s:url>
	            </s:if>
	            <s:else>
	                <s:url id='detailURL' namespace='/catalog' action='itemDetails.action'>
	                    <s:param name='itemID'><s:property value='#item.getAttribute("ItemID")'/></s:param>
	                    <s:param name='unitOfMeasure'><s:property value='#item.getAttribute("UnitOfMeasure")'/></s:param>
	                    <s:param name='goBackFlag'>true</s:param>
	                </s:url>
	            </s:else>
	        <tr>
	            <td  style="width: 17px;">
	                <s:if test='!#isReadOnly'>
	                    <s:a href="javascript:deleteLine('%{orderHeaderKey}', '%{#orderLineKey}')" id="delete_%{#orderLineKey}">
	                    <img title="Remove" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_red_x.png" title="Remove"></s:a>
	                </s:if>
	            </td>
	            <td class="mini-cart-qty">
	                  <s:if test='#item.getAttribute("ItemShortDesc") == ""'>
							<s:set name='showDesc' value='#item.getAttribute("ItemDesc")'/>
					</s:if>
					<s:else>
						<s:set name='showDesc' value='#item.getAttribute("ItemShortDesc")'/>
					</s:else>
	                <span class="underlink" style="color:#2970A6" ><s:a href="%{#detailURL}" title="%{#showDesc}"
	                ><s:property value='#item.getAttribute("ItemID")'/></s:a></span>
	                <s:hidden name="lineKeys" id="lineKeys" value="%{#orderLineKey}" />
	            </td>
	            <td  style="width: 78px;">
	            <s:set name='qty' value='#orderLineTran.getAttribute("OrderedQty")' />
	            <s:set name='qty' value='%{#strUtil.replace(#qty, ".00", "")}' />
	          <!--<s:textfield theme="simple" cssClass="mini-cart-row-input-length" maxlength="7" name="orderLineQtys" id="orderLineQtys_%{#orderLineKey}" size="5" value='%{#util.formatQuantity(wCContext, #orderLineTran.getAttribute("OrderedQty"))}' onchange="isValidQuantity(this)" onkeypress="return onEnter(event)" disabled='%{#isReadOnly}' />  -->  
	              <s:textfield theme="simple" cssClass="mini-cart-row-input-length" maxlength="7" name="orderLineQtys" id="orderLineQtys_%{#orderLineKey}" size="5" value='%{#qty}' onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" onkeypress="return onEnter(event)" disabled='%{#isReadOnly}' />
	             
 				</td>
	            <td>
	            	<s:hidden name="orderLineReqUOMs" id="orderLineReqUOMs_%{#orderLineKey}" value="%{#orderLineTran.getAttribute('TransactionalUOM')}" />       
	            	<s:property value='#wcUtil.getUOMDescription(#orderLineTran.getAttribute("TransactionalUOM"))'/>
				</td>
				<%--	Using CustomerContactBean object from session
				<s:if test='%{#session.viewPricesFlag == "Y"}'>
				--%>
				<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
		            <td  class="text-right">
		            <s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
		            <s:set name="theMyPrice" value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#orderLineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
		            	<s:if test="%{#theMyPrice=='$0.00'}">
								<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
                    	 </s:if>
                         <s:else>
		              	<%-- <s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #orderLineExtn.getAttribute("ExtnLineOrderedTotal"))'/> --%>
		              			<s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#orderLineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
		              	</s:else>
		            </td>
		        </s:if>
		        <s:else>
		        	<td  class="text-right">&nbsp;&nbsp;&nbsp;&nbsp;</td>
		        </s:else>
	        </tr>
	       
	        <%-- Added errorDiv for Jira 3366 --%>
	        <tr>
	        <td colspan='5'>
	        <s:set name="itemMap" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("itemMap")' />
	       <s:set name='orderMultiple' value='%{#itemMap.get(#item.getAttribute("ItemID"))}'/>
	       <s:hidden name='orderLineOrderMultipleMiniCart' value='%{#itemMap.get(#item.getAttribute("ItemID"))}'/>
	       <s:hidden name="orderLineItemIDsMiniCart" id="orderLineItemIDsMiniCart_%{#orderLineKey}" value='%{#item.getAttribute("ItemID")}' />
	       <s:hidden name='orderMultipleMiniCart' value='%{#orderMultiple}'/>
	      <s:if test='%{#orderMultiple >"1" && #orderMultiple !=null}'>
				<div  class="notice" id="errorDiv_orderLineQtys_<s:property value='%{#orderLineKey}' />" style="display:inline-block"> <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /><s:property value="%{#xpedxutil.formatQuantityForCommas(#orderMultiple)}"></s:property>&nbsp;<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#itemUOMs)'></s:property><br/></div>
			</s:if> 
	       <div  class="error" id="errorDiv_orderLineQtys_<s:property value='%{#orderLineKey}' />" style="display:none"></div> 
	      
	      </td>
	     </tr>
	        </s:if>
	        <%--Code For Fetching the map for ConvFactor  Jira 3481--%>
	         <s:set name="getitemsUOMMap" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("itemsUOMMap")' />
	        <s:if test='%{#getitemsUOMMap !=null}'>
	        <s:set name="uomMap" value='%{#getitemsUOMMap.get(#item.getAttribute("ItemID"))}' />
	        <s:set name="orderLineReqUOMs" id="orderLineReqUOMs_%{#orderLineKey}" value="%{#orderLineTran.getAttribute('TransactionalUOM')}" /> 
	       <s:set name="convF" value='#uomMap[#orderLineReqUOMs]' />
			<s:hidden name="UOMconversionMiniCart" id="UOMconversionMiniCart_%{#orderLineKey}" value="%{#convF}" />
	        <%--Code For Fetching the map for ConvFactor  Jira 3481--%>
	       	</s:if>
	        </s:iterator>
	         <!--
	        <s:if test='#counter > 5'>
	            <tr>
	                <td class="textAlignRight" colspan="4">
	                    <s:a href='%{cartDetailURL}'>
	                        <s:text name='More'/>
	                    </s:a>
	                </td>
	                <td class="text-right"></td>
	            </tr>
	        </s:if> -->
	         <tr class="no-border">
			  	<td class="text-right bold" colspan="4"></td>
			  	<td class="text-right"></td>
			  </tr> 
	  	</tbody>
		</table>   
	</div>	   
        
    </s:if>
    <s:else>
    <div class="textAlignCenter">
    <table width="100%" class="no-border-tbl">
        <tr>
            <td class="textAlignCenter" colspan="4"><s:text name="YourCartIsEmpty"/></td>
        </tr>
     </table>
    </div>
    </s:else>
</s:if>
<s:elseif test='(minimalMiniCartMessage != null) && (minimalMiniCartMessage != "")'>
	<table width="100%" border="0px solid red">
        <tr>
            <td class="textAlignCenter" colspan="4"><s:property value='minimalMiniCartMessage'/></td>
        </tr>
         <tr class="no-border">
			  	<td class="text-right bold" colspan="4"></td>
			  	<td class="text-right"></td>
			  </tr>
    </table>
</s:elseif>


</div>
<%-- end mini-cart-rows --%>

<s:hidden name="orderHeaderKey" id="orderHeaderKey" value="%{orderHeaderKey}"/>
<s:set name='wcContext' value="wCContext" />
<s:set name="isEstimator" value='%{#xpedxCustomerContactInfoBean.isEstimator()}' />
<%--	Using CustomerContactBean object from session
<s:set name="isEstimator" value="%{#wcContext.getWCAttribute('isEstimator')}" />
--%>
	<s:if test="!#isEstimator">
		<s:if test="(majorLineElements.size() > 0) && !#isReadOnly">
		<s:if test="%{#_action.getCustomerStatus() != '30'}">
			<a class="orange-ui-btn" style="float:right;margin-right: -5px;"  href="javascript:miniCartCheckout();"><span>Checkout</span></a>
			</s:if>
		</s:if>
	</s:if>	

<%-- <div style="margin-top: 10px;"> --%>
	<s:if test="(majorLineElements.size() > 0) && !minimalMiniCart && !#isReadOnly">
		<a class="grey-ui-btn" style="float:right; margin-right:6px;"  href="javascript:updateLines()"><span>Update Cart</span></a>
	</s:if>
	
	        
	<s:if test='(majorLineElements.size() > 0) && (isCartManagementAllowed == true) && (!#isProcurementUser)'>
		<s:a cssClass='grey-ui-btn' cssStyle='float: right; margin-right: 6px;'  href='%{cartDetailURL}'><span><s:text name='View Cart'/></span></s:a>
	</s:if>
	
	 
<%-- </div> --%>

</s:form>

</div>
</div>
<%-- </div> --%>
