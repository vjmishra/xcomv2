<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<meta http-equiv="X-UA-Compatible" content="IE=8" /> 

<s:form action="draftOrderDetails" cssClass="" name="OrderDetailsForm"
		id="OrderDetailsForm" namespace="/order" method="POST" validate="true">

	<s:hidden name='#action.name' id='validationActionName'
		value='draftOrderDetails' />
	<s:hidden name='#action.namespace' value='/order' />
	<s:hidden name="orderHeaderKey" value='%{#orderHeaderKey}' />
	<s:hidden name="draft" value="%{#draftOrderFlag}" />
	<s:hidden name='Currency' value='%{#currencyCode}' />
	<s:hidden name='mode' value='%{#mode}' />
	<s:hidden name='fullBackURL' value='%{#returnURL}' />
	<s:hidden name="orderLineKeyForNote" id="orderLineKeyForNote" value="" />
	<s:hidden name="listKey" id="listKey" value="" />
	<s:hidden name="orderLineItemOrders" value="" />
	<s:hidden id="orderName" name="orderName" value="" />
	<s:hidden id="orderDesc" name="orderDesc" value="" />
	<s:hidden id="OrderLinesCount" name="OrderLinesCount" value='%{majorLineElements.size()}' />
	<s:hidden id="zeroOrderLines" name="zeroOrderLines" value='false' />
	<s:hidden id="isPNACallOnLoad" name="isPNACallOnLoad" value='false' />
	<%-- Removing changeOrder call for Performance improvement, while checkout --%>
	<s:hidden id="isComingFromCheckout" name="isComingFromCheckout" value='false' />
	<s:hidden id="modifyOrderLines" name="modifyOrderLines" value='false' />
	<input type="hidden" value='<s:property value="%{chargeAmount}" />' name="chargeAmount" />
	<input type="hidden" value='<s:property value="%{minOrderAmount}" />' name="minOrderAmount" />
	<%-- 
	I don't see this variable used in this jsp so removing it . In case if any one wants to use getUOMDescriptions method please use
	XPEDXWCUtils.getUOMDescription .
	<s:set name="uomMap" value='#util.getUOMDescriptions(#wcContext,true)' />
	 --%>
	
	<s:set name="xpedxItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_ITEM_LABEL"/>
	<s:set name="customerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUSTOMER_ITEM_LABEL"/>
	<s:set name="manufacturerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MANUFACTURER_ITEM_LABEL"/>
	<s:set name="mpcItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MPC_ITEM_LABEL"/>
	<s:set name="myPriceValue" value="%{'false'}" />
	
    <table class="mil-top-border" border="0px solid red" class="float-right">
	   <tr  class="table-header-bar">
		<td class="text-right white table-header-bar-left" > My Price (<s:property value='#currencyCode'/>) </td>
		<td class="text-right white pricing-border mill-container-extended-pricing table-header-bar-right" > Extended Price (<s:property value='#currencyCode'/>) &nbsp;</td>
	   </tr>
	</table>
	
	<%--<s:set name="subTotal" value='%{0.00}' /> --%>
	<input type="hidden" name="isEditOrder" value="<s:property	value='%{(#_action.getIsEditOrder())}' escape="false" />"/>
	<%--jira 3788 --%>  
	<s:set name="isOrderTBD" value="%{0}" />  
	<s:set name="itemIdCustomerUomMap" value="#_action.getItemAndCustomerUomHashMap()" />      
	<s:iterator value='majorLineElements' id='orderLine' status="rowStatus"  >
		<s:set name='lineTotals' value='#util.getElement(#orderLine, "LineOverallTotals")' />
		<s:set name='item' value='#util.getElement(#orderLine, "Item")' />
		<s:set name='lineTran' value='#util.getElement(#orderLine, "OrderLineTranQuantity")' />
		<s:set name='itemDetails' value='#util.getElement(#orderLine, "ItemDetails")' />
		<s:set name='lineNotes' value='#util.getElement(#orderLine, "Instructions/Instruction")' />
		<s:set name='primaryInfo' value='#util.getElement(#itemDetails, "PrimaryInformation")' />
		<s:set name='itemExtnEle' value='#util.getElement(#itemDetails, "Extn")' />
		<s:set name="imageLocation" value="#xpedxSCXmlUtil.getAttribute(#primaryInfo,'ImageLocation')" />
		<s:set name='imageID' value='#primaryInfo.getAttribute("ImageID")' />
		<s:set name='certFlag' value='#itemExtnEle.getAttribute("ExtnCert")' />
		<s:set name='orderLineKey' value='#orderLine.getAttribute("OrderLineKey")' />
		<s:set name='orderLineType' value='#orderLine.getAttribute("LineType")' />
		<s:set name='kitLines' value='#util.getElement(#orderLine, "KitLines")' />
		<s:set name='lineExtn' value='#util.getElement(#orderLine, "Extn")' />
		<s:set name='itemIDUOM' value='#_action.getIDUOM(#item.getAttribute("ItemID"), #item.getAttribute("UnitOfMeasure"))' />
		<s:set name='canCancel' value='#_action.isOrderLineModificationAllowed(#orderLine, "CANCEL")' />
		<s:set name='canChangeShipNode' value='#_action.isOrderLineModificationAllowed(#orderLine, "SHIP_NODE")' />
		<s:set name='canChangeDeliveryCode' value='#_action.isOrderLineModificationAllowed(#orderLine, "DELIVERY_CODE")' />
		<s:set name='canChangeLineOrderDate' value='#_action.isOrderLineModificationAllowed(#orderLine, "CHANGE_ORDER_DATE")' />
		<s:set name='canChangeLineReqShipDate' value='#_action.isOrderLineModificationAllowed(#orderLine, "REQ_SHIP_DATE")' />
		<s:set name='canChangeBundleDefinition' value='#_action.isOrderLineModificationAllowed(#orderLine, "CHANGE_BUNDLE_DEFINITION")' />
		<s:set name='isSpecialLine' value='#_action.isSpecialLine(#orderLine)' />
		<s:set name="jsonKey" value='%{#item.getAttribute("ItemID")+"_"+#orderLine.getAttribute("PrimeLineNo")}' />
		<s:set name="json" value='pnaHoverMap.get(#jsonKey)' />
		<s:set name="OrderMultipleQtyFromSrc" value='orderMultipleMapFromSourcing.get(#jsonKey)' />
		<s:set name="displayPriceForUoms" value='%{""}' />
		<s:set name="priceInfo" value='priceHoverMap.get(#jsonKey)' />
		<s:if test="%{#priceInfo!=null}" >
			<s:set name="displayPriceForUoms" value='%{#priceInfo.getDisplayPriceForUoms()}' />
		</s:if>
		<s:include value='XPEDXOrderLineTotalAdjustments.jsp' />
		<s:hidden name="orderLineKeys" id="orderLineKeys_%{#orderLineKey}" value="%{#orderLineKey}" />
		<s:hidden name="orderLineItemIDs" id="orderLineItemIDs_%{#orderLineKey}" value='%{#item.getAttribute("ItemID")}' />
		<s:hidden name="orderLineItemNames" id="orderLineItemNames_%{#orderLineKey}" value='%{#item.getAttribute("ItemShortDesc")}' />
		<s:hidden name="orderLineItemDesc" id="orderLineItemDesc_%{#orderLineKey}" value='%{#item.getAttribute("ItemDesc")}' />
		<s:set name='catalogItem' value='#item' />
		<s:include value="XPEDXCatalogDetailURL.jsp" />
		<s:set name='itemIDVal' value='%{#item.getAttribute("ItemID")}' />
		<s:set name='customerUOM' value='#itemIdCustomerUomMap.get(#itemIDVal)' />
		<s:set name="mulVal" value='itemOrderMultipleMap[#itemIDVal]' />		
		<s:if test='%{#OrderMultipleQtyFromSrc !=null && #OrderMultipleQtyFromSrc !=""}'>	
			<s:hidden name="orderLineOrderMultiple" id="orderLineOrderMultiple_%{#orderLineKey}" value="%{#OrderMultipleQtyFromSrc.substring(0,#OrderMultipleQtyFromSrc.indexOf('|'))}" />
		</s:if>
		<s:else>
			<s:hidden name="orderLineOrderMultiple" id="orderLineOrderMultiple_%{#orderLineKey}" value="%{#mulVal}" />
		</s:else>	
		<s:hidden name="minLineQuantity" id="minLineQuantity_%{#orderLineKey}" value="%{#_action.getMinimumLineQuantity(#orderLine)}" />
		<s:hidden name="maxLineQuantity" id="maxLineQuantity_%{#orderLineKey}" value="%{#_action.getMaximumLineQuantity(#orderLine)}" />
		<s:set name="uom" value='%{#lineTran.getAttribute("TransactionalUOM")}' /> 
				<s:set name="MultiUom" value='%{#item.getAttribute("UnitOfMeasure")}' />
				<s:set name="BaseUOMs" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#MultiUom)' /> 
				<s:hidden name="BaseUOMs" id="BaseUOMs_%{#orderLineKey}" value='%{#BaseUOMs}' /> 
				
		<s:hidden name="selectedCustomerContactId" id="selectedCustomerContactId" value="" />
		
		<%-- 
		<s:set name="itemuomMap" value='itemUOMsMap[#itemIDUOM]' /> 
		<s:set name="displayUomsMap" value='#_action.getDisplayItemUOMsMap()' />
		<s:set name="displayUomMap" value='#displayUomsMap[#itemIDUOM]' />
		<s:hidden name="itemUOMs" id="itemUOMs_%{#orderLineKey}" value='%{#uom}' /> 
		<s:set name="convF" value='#itemuomMap[#uom]' />
		<s:hidden name="UOMconversion" id="UOMconversion_%{#orderLineKey}" value="%{#convF}" />
		--%>
		
		<s:set name="editOrderOrderExtn" value='%{""}' />
		<s:set name="editOrderOrderLineExtn" value='%{""}' />
		<s:if test='%{#editOrderFlag == "true" || #editOrderFlag.contains("true")}'>
			<s:set name="editOrderOrder" value='%{#_action.getEditOrderOrderMap().get(#orderHeaderKey)}' />
			<s:set name="editOrderOrderExtn" value='#util.getElement(#editOrderOrder, "Extn")' />
			<s:set name="editOrderOrderLine" value='%{#_action.getEditOrderOrderLineMap().get(#orderLineKey)}' />
			<s:set name="editOrderOrderLineExtn" value='#util.getElement(#editOrderOrderLine, "Extn")' />
		</s:if>
		 
		<s:set name="itemuomMap" value='itemIdConVUOMMap[#itemIDVal]' />
		<s:set name="displayUomMap" value='itemIdsUOMsDescMap[#itemIDVal]' />
		<s:hidden name="itemUOMs" id="itemUOMs_%{#orderLineKey}" value='%{#uom}' /> 
		<s:set name="convF" value='#itemuomMap[#uom]' />
		<s:hidden name="UOMconversion" id="UOMconversion_%{#orderLineKey}" value="%{#convF}" />
		<s:if test='%{#editOrderFlag == "true" || #editOrderFlag.contains("true")}'>
			<s:if test='%{#lineExtn.getAttribute("ExtnEditOrderFlag") == "Y" || #lineExtn.getAttribute("ExtnEditOrderFlag") =="true"}'>
				<s:set name="isUOMAndInstructions" value="%{false}" />
			</s:if>
			<s:else>
				<s:set name="isUOMAndInstructions" value="%{true}" />
			</s:else>
		</s:if>
		<s:else>
			<s:set name="isUOMAndInstructions" value="%{#isReadOnly}" />
		</s:else>

		
		<!-- begin iterator -->       
		<s:if test="#rowStatus.last == true ">
	    	<div class="mil-wrap last" onmouseout="$(this).removeClass('green-background');" onmouseover="$(this).addClass('green-background');">
	    </s:if>
	    <s:else>
	    	 <div class="mil-wrap" onmouseout="$(this).removeClass('green-background');" onmouseover="$(this).addClass('green-background');">
	    </s:else>
         	<div class="mil-container" >
                <!--checkbox   -->
                <div class="mil-checkbox-wrap">
                     <%-- <s:checkbox name='selectedLineItem' id='selectedLineItem_%{#orderLineKey}' fieldValue='%{#orderLineKey}' disabled='%{!#canCancel}' tabindex="%{#tabIndex}" /> --%>                    
                   <s:checkbox name='selectedLineItem' id='selectedLineItem_%{#orderLineKey}' cssStyle="display:none" onclick="checkHiddenCheckboxAndDeleteItem(this, 'selectedLineItem_%{#orderLineKey}')" fieldValue='%{#orderLineKey}' disabled='%{!#canCancel}' tabindex="%{#tabIndex}" />
                   <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
                   	<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_red_x.png" onclick="javascript:checkHiddenCheckboxAndDeleteItem(this,&#39;<s:text name='selectedLineItem_%{#orderLineKey}'/>&#39; );" title="Remove" alt="RemoveIcon" />
                   </s:if> 
                  </div>
                <!-- end checkbox   -->
                        
                <!-- begin description  -->
                <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
                <s:a href="javascript:processDetail('%{#item.getAttribute('ItemID')}', '%{#item.getAttribute('UnitOfMeasure')}')" >
	                <div class="mil-desc-wrap">
	                    <div class="mil-wrap-condensed-desc item-short-desc" style="max-height:59px; height: auto;"> 
	
							<span class="short-description">
							<s:if test='#item.getAttribute("ItemShortDesc") == ""'>
								<s:property escape='false'	value='%{#item.getAttribute("ItemDesc")}' />
							</s:if>
							<s:else>
								<s:property escape='false'	value='%{#item.getAttribute("ItemShortDesc")}' />
							</s:else></span>
						</div>
		                <div class="mil-attr-wrap">
							<s:if test='#item.getAttribute("ItemDesc") != ""'>
								<ul class="prodlist">
									<s:property escape='false'	value='%{#item.getAttribute("ItemDesc")}' />
								</ul>
							</s:if>							
		                </div>
					</div>
				 </s:a>
				</s:if>
				<s:else>
	                <div class="mil-desc-wrap">
	                    <div class="mil-wrap-condensed-desc item-short-desc" style="max-height:59px; height: auto;"> 
	
							<span class="short-description_M">
							<s:if test='#item.getAttribute("ItemShortDesc") == ""'>
								<s:property escape='false'	value='%{#item.getAttribute("ItemDesc")}' />
							</s:if>
							<s:else>
								<s:property escape='false'	value='%{#item.getAttribute("ItemShortDesc")}' />
							</s:else></span>
						</div>
		                <div class="mil-attr-wrap">
		                    <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
								<s:if test='#item.getAttribute("ItemDesc") != ""'>
									<ul class="prodlist">
										<s:property escape='false'	value='%{#item.getAttribute("ItemDesc")}' />
									</ul>
								</s:if>
							</s:if>
		                </div>
					</div>
				</s:else>
				
				<!-- Disable the fields for line type C -->
				<s:if test='(#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" 
						<%-- || (!#_action.isDraftOrder() && (!#_action.getIsEditOrder().contains("true") ))--%>
					)'>
					<s:set name="disblForLnTypCOrNonDrftOdr" value="%{true}"></s:set>
				</s:if>
				<s:else>
					<s:set name="disblForLnTypCOrNonDrftOdr" value="%{false}"></s:set>
				</s:else>
				<!-- end description -->
                <s:if test="!#isReadOnly">
                	<s:set name="isReadOnly" value="#disblForLnTypCOrNonDrftOdr"></s:set>
                </s:if>
                <!-- Disable the fields for line type C -->
                
                <s:set name='qty' value='#lineTran.getAttribute("OrderedQty")' />
				<s:set name='qty' value='%{#strUtil.replace(#qty, ".00", "")}' />
				<div class="cart-availability-section">
	            	<table width="100%" cellspacing="0" cellpadding="0" border="0px solid red" class="mil-config availability-table">
	                	<tbody>
	                    	<tr>
								<td class="text-right" style="padding:0px;">
								<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
									<label style="font-size:12px">Qty: </label>
								</s:if>
								<s:if test='#isReadOnly'>
								  <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
									<s:textfield name='tempOrderLineQuantities'
									theme="simple" id="tempOrderLineQuantities_%{#orderLineKey}" size='1'
									cssClass="mil-action-list-wrap-qty-label" value='%{#qty}'
									disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" maxlength="7"/>
								 </s:if>
									<s:hidden name="orderLineQuantities" id="orderLineQuantities_%{#orderLineKey}" value='%{#qty}' />
								</s:if>
								<s:else>
								  <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
									<s:textfield name='orderLineQuantities'
									theme="simple" id="orderLineQuantities_%{#orderLineKey}" size='1'
									cssClass="mil-action-list-wrap-qty-label" value='%{#qty}'
									disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" maxlength="7"/>
								  </s:if>
								  <s:else>
								  		<s:hidden name="orderLineQuantities" id="orderLineQuantities_%{#orderLineKey}" value='%{#qty}' />
								  </s:else>
								</s:else>
									<s:set name='tabIndex' value='%{#tabIndex + 1}' />
									<s:hidden name="#qaQuantity.type" value="OrderedQty" />
									<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
										  <s:select name="itemUOMsSelect" id="itemUOMsSelect_%{#orderLineKey}"
											cssClass="xpedx_select_sm mil-action-list-wrap-select" onchange="javascript:setUOMValue(this.id,'%{#_action.getJsonStringForMap(#itemuomMap)}')" 
											list="#displayUomMap" listKey="key" listValue='value'
											disabled="#isUOMAndInstructions" value='%{#uom}' tabindex="%{#tabIndex}" theme="simple"/>
											<s:hidden id="custUOM_%{#orderLineKey}" name="custUOM" value="%{#customerUOM}" />
									</s:if>
									<s:if test='#isUOMAndInstructions'>
										<s:hidden name="itemUOMsSelect" id="itemUOMsSelect_%{#orderLineKey}" value='%{#uom}' />
									</s:if>
                         		</td>
						 	</tr>
						 	<tr>
						 		<td>
						 		<br/>
						 						 		
						 						 			
						 			<s:if test='(useOrderMultipleMapFromSourcing.containsKey(#jsonKey))'>
						 				<s:set id="OrderMultipleUom" name="OrderMultipleUom"  value="%{#OrderMultipleQtyFromSrc.substring(#OrderMultipleQtyFromSrc.indexOf('|')+1,#OrderMultipleQtyFromSrc.length())}"/>						 				
                						<s:if test='%{#OrderMultipleQtyFromSrc !=null && #OrderMultipleQtyFromSrc !=""}'>						 			
						 						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div class="error" id="errorDiv_orderLineQuantities_<s:property value='%{#orderLineKey}' />" style="display : inline;position:absolute;font-size:12px;"><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#xpedxutil.formatQuantityForCommas(#OrderMultipleQtyFromSrc.substring(0,#OrderMultipleQtyFromSrc.indexOf('|')))}"></s:property>&nbsp;<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#OrderMultipleUom)'></s:property><br/>
						 				</s:if>
						 				<s:else>
						 						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div class="error" id="errorDiv_orderLineQuantities_<s:property value='%{#orderLineKey}' />" style="display : inline;position:absolute;font-size:12px;"><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#xpedxutil.formatQuantityForCommas(#mulVal)}"></s:property>&nbsp;<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#MultiUom)'></s:property><br/>
						 				</s:else>
                					</s:if>
                					<s:elseif test='%{#OrderMultipleQtyFromSrc !=null && #OrderMultipleQtyFromSrc !=""}' >
                						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div class="notice" id="errorDiv_orderLineQuantities_<s:property value='%{#orderLineKey}' />" style="display : inline;position:absolute;font-size:12px;"><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#xpedxutil.formatQuantityForCommas(#OrderMultipleQtyFromSrc.substring(0,#OrderMultipleQtyFromSrc.indexOf('|')))}"></s:property>&nbsp;<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#MultiUom)'></s:property><br/>
                					</s:elseif>
                					<s:else>
						 		<s:if test='%{#mulVal >"1" && #mulVal !=null}'>
						 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div class="notice" id="errorDiv_orderLineQuantities_<s:property value='%{#orderLineKey}' />" style="display : inline;position:absolute;font-size:12px;"><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#xpedxutil.formatQuantityForCommas(#mulVal)}"></s:property>&nbsp;<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#MultiUom)'></s:property><br/></div>
						 		</s:if>
						 			</s:else>
						 		
						 		</div>
						 		</td>
						 	</tr>
					 	</tbody>
				 	</table>
				 <%--	Using CustomerContactBean object from session
				 <s:if test='%{#session.viewPricesFlag == "Y"}'>
				 --%>
				 <s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
				 	<s:if test="#rowStatus.first == true ">
				 		<table class="float-right pricing-table" style="font-size:12px">
				 	</s:if>
				 	<s:else>
				 		<table class="float-right pricing-table" style="font-size:12px">
				 	</s:else>
				 	<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
				 	
						<tbody>
							<%-- 
				  	  		<tr>
				  	  			<td class="text-right" width="147">
				  	  			<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
					 				TBD
					 			</s:if>
					 			<s:else>
									<s:property value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #json.get("UnitPricePerRequestedUOM"))' />
								</s:else>
								</td>
                            	<td class="text-right" width="147"><span class="mil-action-list-wrap-num-span">
                            	<s:if test='#orderLine.getAttribute("LineType")=="C"'>
									TBD
								</s:if>
								<s:else>
									<s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#json.get("ExtendedPrice"),"1","0"))' />
								</s:else>
								</span></td>
                        	</tr>
                        	<tr>
            	            	<td class="text-right">
            	            	<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
					 				&nbsp;
					 			</s:if>
					 			<s:else>
									/&nbsp;<s:property value="#XPEDXWCUtils.getUOMDescription(#json.get('UOM'))" />
								</s:else>
								</td>
                    	    </tr>
                        	<s:if test='#orderLine.getAttribute("LineType") !="C"'>
					 			<s:if test="#displayPriceForUoms.size()>0" >
					 			<s:set name="pricingUOM" value="#json.get('PricingUOM')" />
					 			
					 			<s:set name="displayIndex" value="1" />
					 			<s:if test="#pricingUOM == 'A_CWT' || #pricingUOM == 'M_CWT' || #pricingUOM == 'M_M' || #pricingUOM == 'A_M' ">
					 				<s:set name="displayIndex" value="2" />
					 			</s:if>
					 			<s:iterator value='displayPriceForUoms' id='disUOM' status='disUOMStatus'>
					 				<s:set name="bracketPriceForUOM" value="bracketPrice" />
									<s:set name="bracketUOMDesc" value="bracketUOM" />
									
					 				<s:if test='%{#disUOMStatus.index < #displayIndex && (#XPEDXWCUtils.getUOMDescription(#json.get("UOM")) != #bracketUOMDesc) }' >
									
										<tr>
				                        	<td class="text-right">
												<s:property	value='#bracketPriceForUOM' />
											</td>
		    	                    	</tr>
			        	                <tr>
			            	            	<td class="text-right">
												/&nbsp;<s:property value="#bracketUOMDesc" />
											</td>
			                	            <td></td>
			                    	    </tr>
			                    	    </s:if>
								</s:iterator>
								
	                    	    </s:if>
					 		</s:if> 
					 		--%>
					 		
					 		<s:set name="isMyPriceZero" value="%{'false'}" />
					 	
					 		<s:set name="break" value="false"></s:set>
					 		<s:if test="#displayPriceForUoms!=null && #displayPriceForUoms.size()>0" >
					 			<s:iterator value='#displayPriceForUoms' id='disUOM' status='disUOMStatus'>
					 				<s:set name="bracketPriceForUOM" value="bracketPrice" />
					 				
					 				<s:set name="temp" value="bracketUOM" />
									<s:set name="customerUOMDesc" value="#XPEDXWCUtils.getUOMDescription(#customerUOM)"/>
									<s:if test='%{#customerUOMDesc==#temp}'>	
										<s:set name='customerUomWithoutM' value='%{#customerUOM.substring(2, #customerUOM.length())}' />
										<s:set name="bracketUOMDesc" value="#customerUomWithoutM" />
									</s:if>
									<s:else>
										<s:set name="bracketUOMDesc" value="bracketUOM" />
									</s:else>		
									
									
									<s:if test='%{!#disUOMStatus.last}' >
										<s:if test='%{#disUOMStatus.first}' >
											<tr>
											<s:if test="%{#break == false}">
								  	  			<td class="text-right" width="130">
								  	  			<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
									 				
									 			</s:if>
									 			<s:else>
									 			
									 			  <s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
									 			  <s:if test="%{#bracketPriceForUOM == #priceWithCurrencyTemp1}">
									 			    	<s:set name="myPriceValue" value="%{'true'}" />
									 			    	
														<span class="red bold"> <s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' /></span>  
														<s:set name="break" value="true"></s:set>
												  </s:if>
												  <s:else>
												  <s:if test="%{#bracketPriceForUOM != #priceWithCurrencyTemp1}">
												    <s:property value="#bracketPriceForUOM" /><br/>
													per&nbsp;<s:property value="#bracketUOMDesc" /></s:if>
												  </s:else>
												</s:else>
												</td></s:if>
				                            	<td class="text-right" width="147" valign="top">
					                            	<span class="mil-action-list-wrap-num-span">
					                            	<s:set name= 'extendedPrice'  value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#lineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
					                            	<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
					                            	<s:if test='#orderLine.getAttribute("LineType")=="C"'>
														
													</s:if>
													<s:else>
														<%-- <s:if test='%{#editOrderFlag == "true"}'>
															<s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#editOrderOrderLineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
														</s:if>
														<s:else> --%>														  
											 			  <s:if test="%{#extendedPrice == #priceWithCurrencyTemp}">											 			  
																<span class="red bold"><s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
																<s:set name="isOrderTBD" value="%{#isOrderTBD+1}" /> 
														  </s:if>
														  <s:else>
														   		<s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#lineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
														  </s:else>
														<%-- 	
														</s:else>
														--%>
													</s:else>
													</span>
												</td>
				                        	</tr>
				                        	<tr><td>&nbsp;</td></tr>											
				                    	 </s:if>
				                    	 <s:else>										
				                    	 	<tr>
					                        	<td class="text-right">
					                        		<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
										 				&nbsp;
										 			</s:if>
										 			<s:else>
										 				<s:if test="%{#break == false}">
										 			    	<s:property	value='#bracketPriceForUOM' />
													    </s:if>
										 			</s:else>					                        														  
												</td>
			    	                    	</tr>
				                    	 <tr>
			            	            	<td class="text-right">
			            	            	<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
								 				&nbsp;
								 			</s:if>
								 			<s:else>									 			
												<s:if test="%{#break == false}">
													per&nbsp;<s:property value="#bracketUOMDesc" />
												</s:if>											
											</s:else>
											</td>											 
			                    	    </tr>
			                    	    <!--  Add empty space between each price / UOM  -->
			                    	    <tr><td>&nbsp;</td></tr>										
				                    	</s:else>
		                    	    </s:if>
			                    	   
					 			</s:iterator>
					 		</s:if>
						</tbody>
				 	</table>
				 </s:if>
				 	<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
						<s:set name="calculatedLineTotal" value='{0}' />
					</s:if>
					<s:else>
							<s:set name="calculatedLineTotal" value='#priceUtil.getLineTotal(#json.get("UnitPricePerRequestedUOM"),#qty,#lineTotals.getAttribute("DisplayLineAdjustments"))' />
							<s:set name='deliveryMethod' value='#orderLine.getAttribute("DeliveryMethod")' />
							<s:set name='deliveryMethods' value='deliveryMethodHelper.getDeliveryMethodsForLine(#orderLine)' />
							
							<!-- commenting delivery methods code - to decide where to put this on the screen -->
							<%-- <br />
							<s:if test='!#isProcurementUser'>
								<s:if test='#deliveryMethods.size() > 0'>
									<s:select name="selectedDeliveryMethods"
										id="selectedDeliveryMethods_%{#orderLineKey}"
										list="deliveryMethods"
										disabled="%{!(#canChangeOrderDate && #canChangeShipNode && #canChangeDeliveryCode && #canChangeLineOrderDate && #canChangeLineReqShipDate)}"
										value='%{#deliveryMethod}'
										tabindex="%{#tabIndexForDeliveryMethod}" />
								</s:if>
								<s:else>
									<s:property value='deliveryMethodHelper.allDeliveryMethods[#deliveryMethod]' />
								</s:else>
							</s:if>--%>
							<s:if test="(!(#canChangeOrderDate && #canChangeShipNode && #canChangeDeliveryCode && #canChangeLineOrderDate && #canChangeLineReqShipDate)) || (#deliveryMethods.size() == 0) || (#isProcurementUser)">
								<s:hidden name="selectedDeliveryMethods" id="selectedDeliveryMethods_%{#orderLineKey}" value="%{#deliveryMethod}" />
							</s:if> 
							<s:hidden name="originalDeliveryMethods" id="originalDeliveryMethods_%{#orderLineKey}" value="%{#deliveryMethod}" />
							<s:hidden name="canChangeStore" id="canChangeStore_%{#orderLineKey}" value="%{(#canChangeOrderDate && #canChangeShipNode && #canChangeLineOrderDate && #canChangeLineReqShipDate)}" />
					</s:else>
				 	<%-- 
				 	<s:if test='( #json.get("UnitPricePerRequestedUOM") != null && #json.get("UnitPricePerRequestedUOM") != "")'> 
                           <s:set name="subTotal" value='%{#subTotal+#calculatedLineTotal}' />
                 	</s:if>
				 	--%>
				 	<s:if test="(#json.get('Immediate') == null)">
						<s:set name="jsonImmediate" value="'0'" />
					</s:if>
					<s:else>
						<s:set name="jsonImmediate" value="#json.get('Immediate')" />
					</s:else>
					
					<s:set name="jsonImmediate" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonImmediate)"/>
					<s:set name="jsonCommaFmtImmediate" value='#xpedxUtilBean.formatQuantityForCommas( #jsonImmediate )' />
 
					
					<s:if test="(#json.get('NextDay') == null)">
						<s:set name="jsonNextDay" value="'0'" />
					</s:if>
					<s:else>
						<s:set name="jsonNextDay" value="#json.get('NextDay')" />
					</s:else>
					
					<s:set name="jsonNextDay" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonNextDay)"/>
					<s:set name="jsonFmtNextDay" value='#xpedxUtilBean.formatQuantityForCommas( #jsonNextDay )' />
					
					<s:if test="(#json.get('TwoPlusDays') == null)">
						<s:set name="jsonTwoPlus" value="'0'" />
					</s:if>
					<s:else>
						<s:set name="jsonTwoPlus" value="#json.get('TwoPlusDays')" />
					</s:else>
					
					<s:set name="jsonTwoPlus" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTwoPlus)"/>
					<s:set name="jsonFmtTwoPlus" value='#xpedxUtilBean.formatQuantityForCommas( #jsonTwoPlus )' />
					
					<s:set name="jsonUOM" value="#json.get('UOM')" />
					<s:if test='%{#customerUOM==#jsonUOM}'>
						<s:set name='customerUomWithoutM' value='%{#customerUOM.substring(2, #customerUOM.length())}' />				
						<s:set name="jsonUOMDesc" value="#customerUomWithoutM" />										
					</s:if>
					<s:else>
						<s:set name="jsonUOMDesc" value="#XPEDXWCUtils.getUOMDescription(#jsonUOM)" />
					</s:else>	
					
					<s:set name="jsonAvailability" value="#json.get('Availability')" />
					<s:set name="jsonTotal" value="#json.get('Total')" />
					<s:set name="jsonImage1" value="#XPEDXWCUtils.getImage('Immediate')" />
					<s:set name="jsonImage3" value="#XPEDXWCUtils.getImage('TwoPlusDays')" />
					<s:set name="jsonImage2" value="#XPEDXWCUtils.getImage('NextDay')" />
					<s:set name="divName" value="#_action.getDivisionName()" />
					<s:set name="stateCode" value="#_action.getState()" />
					<s:if test="(#stateCode == '')">
						<s:set name='StateCode' value="'&nbsp;'" />
					</s:if>
					<s:else>
						<s:set name='StateCode' value='#stateCode' />
					</s:else>
					<s:if test="(#divName == '')">
						<s:set name='DivisionName' value="'&nbsp;'" />
					</s:if>
					<s:else>
						<s:set name='DivisionName' value='#divName' />
					</s:else>
					<s:set name="jsonTotal" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTotal)"/>
					<s:set name="jsonFmtTotal" value='#xpedxUtilBean.formatQuantityForCommas( #jsonTotal )' />
					
					
					<%-- <s:if test="(#jsonTotal != null)"> --%>
					
				     	<div class="cart-availability text-left">
				     	 <div id="errorDiv_orderLineQuantities_<s:property value='%{#orderLineKey}' />"></div> 
				     	 <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
					 		<table  cellspacing="0" cellpadding="0" border="0px solid red" class="mil-config" style="font-size:12px">
						    	<tbody>
						    		<tr>
										<td><strong><p class="bold left" style="width:110px">Total Available: </p></strong></td>
										<td class="text-right"><strong>${jsonFmtTotal} </strong></td>
										<td class="text-left"><strong>&nbsp;${jsonUOMDesc}</strong></td>
						    		</tr>
						    		<tr>
										<td><p class="availability-indent">Next Day: </p></td>
										<td class="text-right"><p> ${jsonFmtNextDay} </p></td>
										<td class="text-left">&nbsp;<%-- ${jsonUOMDesc} --%></td>									
						    		</tr>
						    		<tr>
										<td><p class="availability-indent">2+ Days: </p></td>
										<td class="text-right"><p> ${jsonFmtTwoPlus} </p></td>
										<td class="text-left">&nbsp;<%-- ${jsonUOMDesc} --%></td>
						    		</tr>
						    		<%-- <s:if test="(#divName != null)"> --%>
						    		<tr>
										<%-- <td colspan="3"><p class="italic">${jsonImmediate} available today at ${DivisionName}</p></td> --%>
										<td colspan="3"><p class="italic">${jsonCommaFmtImmediate} &nbsp;${jsonUOMDesc}&nbsp;available today at ${DivisionName}</p></td> 
								    </tr>
								    <%-- </s:if> --%>
							    </tbody>
					    	</table>
					    	</s:if>
				    	</div>
			    	
			    	
			    	<s:if test='(xpedxItemIDUOMToComplementaryListMap.containsKey(#itemIDUOM))'>
						<a href='javascript:showXPEDXComplimentaryItems("<s:property value="#itemIDUOM"/>", "<s:property value="#orderLineKey"/>", "<s:property value="#orderLine.getAttribute('OrderedQty')"/>");'
							tabindex="100"> Complimentary </a>
					</s:if>
					<s:if test='(xpedxItemIDUOMToAlternativeListMap.containsKey(#itemIDUOM))'>
						<a href='javascript:showXPEDXAlternateItems("<s:property value="#itemIDUOM"/>", "<s:property value="#orderLineKey"/>", "<s:property value="#orderLine.getAttribute('OrderedQty')"/>");'
							tabindex="100"> Alternate </a>
					</s:if>
					<%-- Commenting the Replacement link as 'This item has been replaced' is the link to replacement items.
					<s:if test='(xpedxItemIDUOMToReplacementListMap.containsKey(#itemID) && xpedxItemIDUOMToReplacementListMap.get(#itemID).size()>0)'>
						<a href='javascript:showXPEDXReplacementItems("<s:property value="#itemID"/>", "<s:property value="#orderLineKey"/>", "<s:property value="#orderLine.getAttribute('OrderedQty')"/>");'
							tabindex="100"> Replacement </a>
					</s:if>
					--%>
				<%-- </s:if> --%>
				<br/> 
				<div class="clearall">&nbsp; </div>
			    	<div class="red float-left">
			    		<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
				    	<s:iterator value="inventoryMap" id="inventoryMap" status="status" >
							<s:set name="inventoryChk" value="value" />
							<s:set name="itemId" value="key" />
							<s:if test='#item.getAttribute("ItemID") == #itemId'>
								<s:if test='%{#inventoryChk !="Y"}'>								
									<p class="red">Mill / Mfg. Item - Additional charges may apply</p>
								</s:if>
							</s:if>	
						</s:iterator>
						</s:if>
						<s:set name='linelineoverallTotals' value='#util.getElement(#orderLine, "LineOverallTotals")'/>
						<s:set name='adjustment' value='%{0.00}' />
						<%--<s:if test='%{#editOrderFlag == "true"}'>
								<s:set name='adjustment' value='#xutil.getDoubleAttribute(#editOrderOrderLineExtn,"ExtnAdjDollarAmt")' />
						</s:if>
						<s:else>
						     --%><s:set name='adjustment' value='#xutil.getDoubleAttribute(#lineExtn,"ExtnLegOrderLineAdjustments")' />
						<%--</s:else>
				    	
						--%><s:if test='%{#adjustment != 0.00}'>
							<p>A Discount of <a id='tip_<s:property value="#orderLineKey"/>' href="javascript:displayLineAdjustments('adjustmentsLightBox','<s:property value='#orderLineKey'/>')">
							<s:property value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#adjustment)'/></a> has applied to this line.</p>
						</s:if>
				    	<br/>
			    	</div>
				</div>
			
				<div class="clearall">&nbsp;</div>
			    
			    <div class="bottom-mil-info">
			    	<div class="float-left brand-info">
			    		<s:set name="itemID" value='#item.getAttribute("ItemID")' />
			    		<p><s:property value="wCContext.storefrontId" /> <s:property value="#xpedxItemLabel" />: <s:property value='#item.getAttribute("ItemID")' />
				    		<s:if test='#certFlag=="Y"'>
							 	<img border="none"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/catalog/green-e-logo_small.png" alt="" style="margin-left:0px; display: inline;"/>
							 </s:if>
			    		</p>
			    		<s:if test='#orderLine.getAttribute("LineType") != "M"'>
						<s:if test='skuMap!=null && skuMap.size()>0'>
										<s:set name='itemSkuMap' value='%{skuMap.get(#item.getAttribute("ItemID"))}'/> 
										<s:set name='mfgItemVal' value='%{#itemSkuMap.get(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MFG_ITEM_NUMBER)}'/>
										<s:set name='partItemVal' value='%{#itemSkuMap.get(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUST_PART_NUMBER)}'/>
									</s:if>
										 	<s:if test='mfgItemFlag != null && mfgItemFlag=="Y"'> 
											<p class="fields-padding">
												<s:property value="#manufacturerItemLabel" />: <s:property value='#mfgItemVal' /></p>
											 </s:if>
											<s:if test='customerItemFlag != null && customerItemFlag=="Y"'>
											<p class="fields-padding">
												<s:property value="#customerItemLabel" />: <s:property value='#partItemVal' /></p>
											</s:if>		
							</s:if>
						
						<s:if test='(xpedxItemIDUOMToReplacementListMap.containsKey(#itemID) && xpedxItemIDUOMToReplacementListMap.get(#itemID) != null)'>
			    		<a href='javascript:showXPEDXReplacementItems("<s:property value="#itemID"/>", "<s:property value="#orderLineKey"/>", "<s:property value="#orderLine.getAttribute('OrderedQty')"/>");' ><p class="cart-replaced red line-spacing">This item has been replaced<img class="replacement-img" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_i.png" title="View Replacement Item"/></p></a>
			    		</s:if>					    		
			    	</div>			    	
    
			    	<div class="cust-defined-fields">
			    		<table style="font-size:12px">
			    			<tbody>
				    			<s:set name='tabIndex' value='%{#tabIndex + 1}' />
				    			<s:if test="%{requiredCustFieldsErrorMap!=null && requiredCustFieldsErrorMap.size>0}" >
                					<s:set name="requiredFieldsForOLK" value="%{requiredCustFieldsErrorMap.get(#orderLineKey)}" />
                				</s:if>
								<s:iterator value='customerFieldsMap'>
									<s:set name='FieldLabel' value='key' />
									<s:set name='FieldValue' value='value' />
									<s:set name='customLbl' value='%{"Extn" + #FieldLabel}' />
										<tr>
		                                	<td class="float-right" colspan="2">
		                                	<s:if test='(#orderLineType =="P" || #orderLineType =="S")'>
		                                		<label>
		                                			<s:text name="%{#FieldValue}" />:
		                                		</label>
		                                		<s:if test=' (#FieldLabel == "CustomerPONo") || (#FieldLabel == "CustomerLinePONo") '>
													<%--Added if-else condn for giving border-color when CustomerPONo and CustomerLinePONo are blank - Jira 3966 --%>
													<s:if test="%{#requiredFieldsForOLK!=null && #requiredFieldsForOLK.contains(#FieldLabel)}" >
													<s:textfield name='orderLine%{#FieldLabel}' theme="simple"
														cssClass="x-input bottom-mill-info-avail"
														id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="%{#orderLine.getAttribute(#FieldLabel)}"
														disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" maxlength="22" cssStyle="border-color:#FF0000"/>
													</s:if>
													<s:else>
													<s:textfield name='orderLine%{#FieldLabel}' theme="simple"
														cssClass="x-input bottom-mill-info-avail"
														id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="%{#orderLine.getAttribute(#FieldLabel)}"
														disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" maxlength="22"  />
													</s:else>
												</s:if>
												<s:else>
												<%--Added if-else condn for giving border-color when Line Acc# and PO# are blank - Jira 3966 --%>
													<s:if test="%{#requiredFieldsForOLK!=null && #requiredFieldsForOLK.contains(#FieldLabel)}" >
														<s:textfield name='orderLine%{#FieldLabel}' theme="simple"
														cssClass="x-input bottom-mill-info-avail"
														id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="%{#lineExtn.getAttribute(#customLbl)}"
														disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" maxlength="22" cssStyle="border-color:#FF0000"/>
													</s:if>
													<s:else>
														<s:textfield name='orderLine%{#FieldLabel}' theme="simple"
														cssClass="x-input bottom-mill-info-avail"
														id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="%{#lineExtn.getAttribute(#customLbl)}"
														disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" maxlength="22" />
													
													</s:else>
													
												</s:else>
												<%-- Show error message against each required customer field --%>
												<s:if test="%{#requiredFieldsForOLK!=null && #requiredFieldsForOLK.contains(#FieldLabel)}" >
													
													<br/><br/><span class="error">Required fields missing. Please review and try again.</span> <br/><br/>
	
												</s:if>
												
												<%-- --%>
												<s:set name='tabIndex' value='%{#tabIndex + 1}' />
											</s:if>
											<s:else> 
												<s:hidden name='orderLine%{#FieldLabel}' id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="" />
											
											</s:else>
											</td>
										</tr>
                                </s:iterator>
                            </tbody>
                        </table>
					</div>				
	
					<div class="clearall">&nbsp; </div>
					<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
		    				<p style='MARGIN-LEFT: 25px;' class="line-spacing">Special Instructions:</p>
				    		<s:set name='lineNoteText' value='#lineNotes.getAttribute("InstructionText")' />
							<s:hidden name="lineNotesKey" id="lineNotesKey_%{#orderLineKey}" value='%{#lineNotes.getAttribute("InstructionDetailKey")}' />				    														
							<P style='MARGIN-LEFT: 140px; MARGIN-TOP: -25px;'>	
								<s:textarea name="orderLineNote" rows="4" cols="90" onkeyup="javascript:restrictTextareaMaxLength(this,250);"
									id="orderLineNote_%{#orderLineKey}" value='%{#lineNotes.getAttribute("InstructionText")}'
									cssClass="special-instructions-input" tabindex="%{#tabIndex}" theme="simple" disabled="%{#isUOMAndInstructions}"/>									
							</P>	
							<s:if test='#isUOMAndInstructions'>
								<s:hidden name="orderLineNote" id="orderLineNote_%{#orderLineKey}" value='%{#lineNotes.getAttribute("InstructionText")}'/>
							</s:if>	
		    		</s:if>
		    		<s:else>
		    			<s:hidden name="orderLineNote" id="orderLineNote_%{#orderLineKey}" value='%{#lineNotes.getAttribute("InstructionText")}'/>
		    			<s:hidden name="lineNotesKey" id="lineNotesKey_%{#orderLineKey}" value='' />
		    		</s:else>										
				</div>
				<%--jira 2885 --%>
    				<s:set name="lineStatusCodeMsg" value="#pnALineErrorMessage.get(#itemID)"></s:set>
    				<s:if test='%{#lineStatusCodeMsg != null || #lineStatusCodeMsg != ""}'>
						<div id="mid-col-mil">
							<h5 align="center"><b><font color="red"><s:property value="%{#lineStatusCodeMsg}" /></font></b></h5>
						</div>
					</s:if>
    				<%-- end of it 2885 --%>
                <br/>
				<div class="clear"></div>
			</div>
		</div>
		<s:set name='isReadOnly' value='%{#tempIsReadOnly}' />
</s:iterator>     <!-- end iterator -->
</s:form>