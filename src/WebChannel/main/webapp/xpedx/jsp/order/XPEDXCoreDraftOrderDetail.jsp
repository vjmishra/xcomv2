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
	<input type="hidden" value='<s:property value="%{maxOrderAmount}" />' name="maxOrderAmount" />
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
	
    <table class="standard-table width100-percent">
	   <tr>
		<th class="text-right addpadright10"> Price (<s:property value='#currencyCode'/>) </th>
		<th width="145"> Extended Price (<s:property value='#currencyCode'/>) &nbsp;</th>
	   </tr>
	</table>
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
		<s:set name="customerUOM" value="#_action.getItemAndCustomerUomWithConvHashMap().get(#item.getAttribute('ItemID'))" />
		<s:hidden name="customerUOMConvFactors" id="customerUOMConvFactor_%{#orderLineKey}" value="%{#customerUOM}" />
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
		<%--EB-3840 --%>
		<s:set name="displayUomMapconv" value='itemIdsUOMsDescMap[#itemIDVal]' />
		<s:iterator value="displayUomMapconv" id="displayUomMapconv" >
			<s:set name="disValue" value="value" />
			<s:set name="disUom" value="key" />
			<s:set name="disItemIDVal" value="#itemIDVal" />
			<s:hidden name='DisplayUomItem_%{#disItemIDVal}' value="%{#disValue}"  id='%{#disUom}' /> 
		</s:iterator>	
		<%--EB-3840 --%>	 
		
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
	    	<div class="cart-wrap" onmouseleave="$(this).removeClass('green-background');" onmouseover="$(this).addClass('green-background');">
	    	
                <!--check box   -->
                <div class="cart-desc-wrap">
                <div class="cart-checkbox-wrap">
                     <%-- <s:checkbox name='selectedLineItem' id='selectedLineItem_%{#orderLineKey}' fieldValue='%{#orderLineKey}' disabled='%{!#canCancel}' tabindex="%{#tabIndex}" /> --%>                    
                   <s:checkbox name='selectedLineItem' id='selectedLineItem_%{#orderLineKey}' cssStyle="display:none" onclick="checkHiddenCheckboxAndDeleteItem(this, 'selectedLineItem_%{#orderLineKey}')" fieldValue='%{#orderLineKey}' disabled='%{!#canCancel}' tabindex="%{#tabIndex}" />
                   <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
                   	<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_red_x.png" onclick="javascript:checkHiddenCheckboxAndDeleteItem(this,&#39;<s:text name='selectedLineItem_%{#orderLineKey}'/>&#39; );" title="Remove" alt="RemoveIcon" />
                   </s:if> 
                 </div>
                <!-- end check box   -->
                        
                <!-- begin description  -->
                <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
               <s:a href="javascript:processDetail('%{#item.getAttribute('ItemID')}', '%{#item.getAttribute('UnitOfMeasure')}')" >
							<h3><span class="short-description">
							<s:if test='#item.getAttribute("ItemShortDesc") == ""'>
								<s:property escape='false'	value='%{#item.getAttribute("ItemDesc")}' />
							</s:if>
							<s:else>
								<s:property escape='false'	value='%{#item.getAttribute("ItemShortDesc")}' />
							</s:else></span></h3>
							<div class="clearfix"></div>
					
		                <div class="float-left brand-info">
							<s:if test='#item.getAttribute("ItemDesc") != ""'>
								<ul class="prodlist">
									<s:property escape='false'	value='%{#item.getAttribute("ItemDesc")}' />
								</ul>
							</s:if>							
		               </div>
				 </s:a>
				</s:if>
				<s:else>
							<h3><span class="short-description_M">
							<s:if test='#item.getAttribute("ItemShortDesc") == ""'>
								<s:property escape='false'	value='%{#item.getAttribute("ItemDesc")}' />
							</s:if>
							<s:else>
								<s:property escape='false'	value='%{#item.getAttribute("ItemShortDesc")}' />
							</s:else></span></h3>
						<div class="clearfix"></div>
		              <div class="float-left brand-info">
		                    <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
								<s:if test='#item.getAttribute("ItemDesc") != ""'>
									<ul class="prodlist">
										<s:property escape='false'	value='%{#item.getAttribute("ItemDesc")}' />
									</ul>
								</s:if>
							</s:if>
		                </div>
				</s:else>
			    	<div class="float-left brand-info">
			    		<s:set name="itemID" value='#item.getAttribute("ItemID")' />
			    		<p><b><s:property value="wCContext.storefrontId" /> <s:property value="#xpedxItemLabel" />: <s:property value='#item.getAttribute("ItemID")' /></b>
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
												<p>
												<span class="fields-padding"><s:property value="#manufacturerItemLabel" />: <s:property value='#mfgItemVal' /></span></p>
											 </s:if>
											<s:if test='customerItemFlag != null && customerItemFlag=="Y"'>
												<p>
													<span class="fields-padding"><s:property value="#customerItemLabel" />: <s:property value='#partItemVal' /></span></p>
											</s:if>		
							</s:if>	
			    </div>
			</div>
				<!-- Disable the fields for line type C -->
				<s:if test='(#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" 
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
				<div class="cart-availability-wrap">
	            	<div class="cart-quantity">
								<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
									<label style="font-size:12px">Qty: </label>
								</s:if>
								<s:if test='#isReadOnly'>
								  <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
									<s:textfield name='tempOrderLineQuantities'
									theme="simple" id="tempOrderLineQuantities_%{#orderLineKey}" size='1'
									cssClass="cart-qty-label x-input" value='%{#qty}'
									disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" maxlength="7"/>
								 </s:if>
									<s:hidden name="orderLineQuantities" id="orderLineQuantities_%{#orderLineKey}" value='%{#qty}' />
								</s:if>
								<s:else>
								  <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
									<s:textfield name='orderLineQuantities'
									theme="simple" id="orderLineQuantities_%{#orderLineKey}" size='1'
									cssClass="cart-qty-label x-input" value='%{#qty}'
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
											cssClass="xpedx_select_sm width-140 x-input"  onchange="javascript:setUOMValue(this.id,'%{#_action.getJsonStringForMap(#itemuomMap)}')" 
											list="#displayUomMap" listKey="key" listValue='value'
											disabled="#isUOMAndInstructions" value='%{#uom}' tabindex="%{#tabIndex}" theme="simple"/>
											<s:hidden id="custUOM_%{#orderLineKey}" name="custUOM" value="%{#customerUOM}" />
									</s:if>
									</div>
									<s:if test='#isUOMAndInstructions'>
										<s:hidden name="itemUOMsSelect" id="itemUOMsSelect_%{#orderLineKey}" value='%{#uom}' />
									</s:if>	 		
						 			<s:if test='%{#customerUOM==#MultiUom}'>
											<s:set name='customerUomWithoutM' value='%{#customerUOM.substring(2, #customerUOM.length())}' />				
											<s:set name="multiUomDesc" value="#customerUomWithoutM" />										
									</s:if>
									<s:else>
											<s:set name="multiUomDesc" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#MultiUom)" />
									</s:else>					 			
						 			<s:if test='(useOrderMultipleMapFromSourcing.containsKey(#jsonKey))'>
						 				<s:set id="OrderMultipleUom" name="OrderMultipleUom"  value="%{#OrderMultipleQtyFromSrc.substring(#OrderMultipleQtyFromSrc.indexOf('|')+1,#OrderMultipleQtyFromSrc.lastIndexOf('|'))}"/>
						 				<s:if test='%{#customerUOM==#OrderMultipleUom}'>
											<s:set name='customerUomWithoutM' value='%{#customerUOM.substring(2, #customerUOM.length())}' />				
											<s:set name="orderMultipleUomDesc" value="#customerUomWithoutM" />										
										</s:if>
										<s:else>
											<s:set name="orderMultipleUomDesc" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#OrderMultipleUom)" />
										</s:else>
															 				
                						<s:if test='%{#OrderMultipleQtyFromSrc !=null && #OrderMultipleQtyFromSrc !=""}'>						 			
						 						<div class="error" id="errorDiv_orderLineQuantities_<s:property value='%{#orderLineKey}' />" ><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#xpedxutil.formatQuantityForCommas(#OrderMultipleQtyFromSrc.substring(0,#OrderMultipleQtyFromSrc.indexOf('|')))}"></s:property>&nbsp;<s:property value="#orderMultipleUomDesc"></s:property></div>
						 				</s:if>
						 				<s:else>
						 						<div class="error" id="errorDiv_orderLineQuantities_<s:property value='%{#orderLineKey}' />" ><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#xpedxutil.formatQuantityForCommas(#mulVal)}"></s:property>&nbsp;<s:property value="#multiUomDesc"></s:property></div>
						 				</s:else>
                					</s:if>
                					<s:elseif test='%{#OrderMultipleQtyFromSrc !=null && #OrderMultipleQtyFromSrc !=""}' >
                						<div class="notice" id="errorDiv_orderLineQuantities_<s:property value='%{#orderLineKey}' />" ><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#xpedxutil.formatQuantityForCommas(#OrderMultipleQtyFromSrc.substring(0,#OrderMultipleQtyFromSrc.indexOf('|')))}"></s:property>&nbsp;<s:property value="#multiUomDesc"></s:property></div>
                					</s:elseif>
                					<s:else>
						 		<s:if test='%{#mulVal >"1" && #mulVal !=null}'>
						 			<div class="notice" id="errorDiv_orderLineQuantities_<s:property value='%{#orderLineKey}' />" ><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#xpedxutil.formatQuantityForCommas(#mulVal)}"></s:property>&nbsp;<s:property value="#multiUomDesc"></s:property><br/></div>
						 		</s:if>
						 			</s:else>
						<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
						<s:set name="calculatedLineTotal" value='{0}' />
					</s:if>
					<s:else>
							<s:set name="calculatedLineTotal" value='#priceUtil.getLineTotal(#json.get("UnitPricePerRequestedUOM"),#qty,#lineTotals.getAttribute("DisplayLineAdjustments"))' />
							<s:set name='deliveryMethod' value='#orderLine.getAttribute("DeliveryMethod")' />
							<s:set name='deliveryMethods' value='deliveryMethodHelper.getDeliveryMethodsForLine(#orderLine)' />
							<s:if test="(!(#canChangeOrderDate && #canChangeShipNode && #canChangeDeliveryCode && #canChangeLineOrderDate && #canChangeLineReqShipDate)) || (#deliveryMethods.size() == 0) || (#isProcurementUser)">
								<s:hidden name="selectedDeliveryMethods" id="selectedDeliveryMethods_%{#orderLineKey}" value="%{#deliveryMethod}" />
							</s:if> 
							<s:hidden name="originalDeliveryMethods" id="originalDeliveryMethods_%{#orderLineKey}" value="%{#deliveryMethod}" />
							<s:hidden name="canChangeStore" id="canChangeStore_%{#orderLineKey}" value="%{(#canChangeOrderDate && #canChangeShipNode && #canChangeLineOrderDate && #canChangeLineReqShipDate)}" />
					</s:else>
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
					<s:set name="jsonAvailabilityMessage" value="#json.get('AvailabilityMessage')" />
					<s:set name="jsonAvailabilityMessageColor" value="#json.get('AvailabilityMessageColor')" />
					<s:set name="jsonAvailabilityBalance" value="#json.get('AvailabilityBalance')" />
				     	<div class="pa-avail">
							<div class="avail-wrap">
				     			 <div id="errorDiv_orderLineQuantities_<s:property value='%{#orderLineKey}' />"></div> 
				     	 			<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
									<div class="pa-row pa-status ready-to-ship addpadtop10" style="color:${jsonAvailabilityMessageColor}">${jsonAvailabilityMessage}</div>
									<div class="pa-row">
										<div class="col-1 bold">Next Day: </div>
										<div class="col-2 bold">${jsonFmtNextDay}</div>
						    		</div>
						    		<div class="pa-row">
										<div class="col-1">2+ Days: </div>
										<div class="col-2">${jsonFmtTwoPlus} </div>
										
						    		</div>
						    		<div class="pa-row">
										<div class="col-1">Total Available: </div>
										<div class="col-2">${jsonFmtTotal} </div>
										<div class="col-3">&nbsp;${jsonUOMDesc}</div>
						    	</div>
						    		<s:if test='%{#jsonAvailabilityBalance != null}'>
						    			<div class="pa-row">
										<div class="warning-icon">
											<img width="12" height="12" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/warning.png"/>
										</div>
										<s:set name="jsonAvailabilityBalance" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonAvailabilityBalance)"/>
										<div class="qty-unavailable"><s:property value="#xpedxUtilBean.formatQuantityForCommas(#jsonAvailabilityBalance)"/> 
											<s:property value='%{#jsonUOMDesc}'/> currently unavailable
										</div>
										</div>
									</s:if>
										<div class="pa-row pa-location">${jsonCommaFmtImmediate} &nbsp;${jsonUOMDesc}&nbsp;available today at ${DivisionName}</div>
					    	</s:if>
					    	<s:set name="lineStatusCodeMsg" value="#pnALineErrorMessage.get(#itemID)"></s:set>
    							<s:if test='%{#lineStatusCodeMsg != null || #lineStatusCodeMsg != ""}'>
									<h5 class="suspended-item addpadleft5"><s:property value="%{#lineStatusCodeMsg}" /></h5>
								</s:if>
					</div>
					<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
				    	<s:iterator value="displayInventoryMap" id="displayInventoryMap" status="status" >
							<s:set name="inventoryChk" value="value" />
							<s:set name="itemId" value="key" />
							<s:if test='#item.getAttribute("ItemID") == #itemId'>
								<s:if test='%{#inventoryChk=="I"}'>
									<div class="non-stock-item-shorter">
										<div class="stock-icon">
											<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/icon-stock.png" width="25" height="25" title="Contact Customer Service to confirm pricing and any additional charges" />
										</div>
											Not a Stocked Item
									</div>
								</s:if>
								<s:if test='%{#inventoryChk=="M"}'>
									<div class="non-stock-item-shorter addmarginleft130">
										<div class="stock-icon">
											<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/icon-manufacturing.png" width="25" height="25" title="Contact Customer Service to confirm pricing and any additional charges" />
										</div>
											Ships Directly from Mfr
									</div>
								</s:if>
								<s:if test='%{#inventoryChk=="W"}'>
								</s:if>
							</s:if>	
						</s:iterator>
					</s:if>
				 </div>
			</div>
				 <s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
					 <div class="cart-price-wrap">
				 	<s:if test="#rowStatus.first == true ">
				 	</s:if>
				 	<s:else>
				 	</s:else>
				 	<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
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
											<div class="cart-price-row">
											<s:if test="%{#break == false}">
								  	  			<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
									 			</s:if>
									 			<s:else>
									 			  <s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
									 			  <s:if test="%{#bracketPriceForUOM == #priceWithCurrencyTemp1}">
									 			    	<s:set name="myPriceValue" value="%{'true'}" />
														<div class="col-1"><span class="red bold"> <s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' /></span></div> 
														<s:set name="break" value="true"></s:set>
												  </s:if>
												  <s:else>
												  <s:if test="%{#bracketPriceForUOM != #priceWithCurrencyTemp1}">
												    <div class="col-2"><s:property value="#bracketPriceForUOM" /><span>
													per&nbsp;<s:property value="#bracketUOMDesc" /></span></div></s:if>
												  </s:else>
												</s:else>
				                            	</s:if>
				                            	<div class="col-2">
					                            	<s:set name= 'extendedPrice'  value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#lineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
					                            	<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
					                            	<s:if test='#orderLine.getAttribute("LineType")=="C"'>
													</s:if>
													<s:else>											  
											 			  <s:if test="%{#extendedPrice == #priceWithCurrencyTemp}">											 			  
																<span class="red bold"><s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
																<s:set name="isOrderTBD" value="%{#isOrderTBD+1}" /> 
														  </s:if>
														  <s:else>
														   		<s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#lineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
														  </s:else>
													</s:else>
													</div>
				                        	<div class="clearfix"></div>
				                        	</div>									
				                    	 </s:if>
				                    	 <s:else>
					                        	<div class="cart-price-row">
					                        	<div class="col-1">
					                        		<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
										 				&nbsp;
										 			</s:if>
										 			<s:else>
										 				<s:if test="%{#break == false}">
										 			    	<s:property	value='#bracketPriceForUOM' />
													    </s:if>
										 			</s:else>
			            	            	<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
								 				&nbsp;
								 			</s:if>
								 			<s:else>						 			
												<s:if test="%{#break == false}"><span>
													per&nbsp;<s:property value="#bracketUOMDesc" /></span>
												</s:if>							
											</s:else>
										</div>
										<div class="clearfix"></div>
			                    	   		</div>					
				                    	</s:else>
		                    	    </s:if>
					 			</s:iterator>
					 		</s:if>
					 		</div>
				 </s:if>
			    	<s:if test='(xpedxItemIDUOMToComplementaryListMap.containsKey(#itemIDUOM))'>
						<a href='javascript:showXPEDXComplimentaryItems("<s:property value="#itemIDUOM"/>", "<s:property value="#orderLineKey"/>", "<s:property value="#orderLine.getAttribute('OrderedQty')"/>");'
							tabindex="100"> Complimentary </a>
					</s:if>
					<s:if test='(xpedxItemIDUOMToAlternativeListMap.containsKey(#itemIDUOM))'>
						<a href='javascript:showXPEDXAlternateItems("<s:property value="#itemIDUOM"/>", "<s:property value="#orderLineKey"/>", "<s:property value="#orderLine.getAttribute('OrderedQty')"/>");'
							tabindex="100"> Alternate </a>
					</s:if>
				 <div class="red float-left">
						<s:set name='linelineoverallTotals' value='#util.getElement(#orderLine, "LineOverallTotals")'/>
						<s:set name='adjustment' value='%{0.00}' />
						<s:if test='%{#adjustment != 0.00}'>
							<p>A Discount of <a id='tip_<s:property value="#orderLineKey"/>' href="javascript:displayLineAdjustments('adjustmentsLightBox','<s:property value='#orderLineKey'/>')">
							<s:property value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#adjustment)'/></a> has applied to this line.</p>
						</s:if>
				    	<br/>
			    	</div>	
			    	<div>
						<s:if test='(xpedxItemIDUOMToReplacementListMap.containsKey(#itemID) && xpedxItemIDUOMToReplacementListMap.get(#itemID) != null)'>
			    			<a href='javascript:showXPEDXReplacementItems("<s:property value="#itemID"/>", "<s:property value="#orderLineKey"/>", "<s:property value="#orderLine.getAttribute('OrderedQty')"/>");' ><p class="cart-replaced red line-spacing floatright addmarginright10">This item will be replaced once inventory is depleted.</p></a>
			    		</s:if>
			    	</div>
    			<div class="cart-instructions-wrap">
					<div class="cust-defined-wrap">
				    			<s:set name='tabIndex' value='%{#tabIndex + 1}' />
				    			<s:if test="%{requiredCustFieldsErrorMap!=null && requiredCustFieldsErrorMap.size>0}" >
                					<s:set name="requiredFieldsForOLK" value="%{requiredCustFieldsErrorMap.get(#orderLineKey)}" />
                				</s:if>
								<s:iterator value='customerFieldsMap'>
									<div class="cust-defined-fields">
									<s:set name='FieldLabel' value='key' />
									<s:set name='FieldValue' value='value' />
									<s:set name='customLbl' value='%{"Extn" + #FieldLabel}' />
		                                	<s:if test='(#orderLineType =="P" || #orderLineType =="S")'>
		                                		<div class="col-1"><label>
		                                			<s:text name="%{#FieldValue}" />:
		                                		</label></div>
		                                		<s:if test=' (#FieldLabel == "CustomerPONo") || (#FieldLabel == "CustomerLinePONo") '>
													<%--Added if-else condn for giving border-color when CustomerPONo and CustomerLinePONo are blank - Jira 3966 --%>
													<s:if test="%{#requiredFieldsForOLK!=null && #requiredFieldsForOLK.contains(#FieldLabel)}" >
													<div class="col-2"><s:textfield name='orderLine%{#FieldLabel}' theme="simple"
														id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="%{#orderLine.getAttribute(#FieldLabel)}"
														disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" maxlength="22" cssStyle="border-color:#FF0000" cssClass="x-input"/></div>
													</s:if>
													<s:else>
													<div class="col-2"><s:textfield name='orderLine%{#FieldLabel}' theme="simple"
														id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="%{#orderLine.getAttribute(#FieldLabel)}"
														disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" maxlength="22" cssClass="x-input" /></div>
													</s:else>
												</s:if>
												<s:else>
												<%--Added if-else condn for giving border-color when Line Acc# and PO# are blank - Jira 3966 --%>
												<%-- For 24 Characters EB -449 --%>
													<s:if test="%{#requiredFieldsForOLK!=null && #requiredFieldsForOLK.contains(#FieldLabel)}" >
														<div class="col-2"><s:textfield name='orderLine%{#FieldLabel}' theme="simple"
														cssClass="x-input bottom-mill-info-avail"
														id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="%{#lineExtn.getAttribute(#customLbl)}"
														disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" maxlength="24" cssStyle="border-color:#FF0000"/></div>
													</s:if>
													<s:else>
														<div class="col-2"><s:textfield name='orderLine%{#FieldLabel}' theme="simple"
														cssClass="x-input bottom-mill-info-avail"
														id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="%{#lineExtn.getAttribute(#customLbl)}"
														disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" maxlength="24" /></div>
													</s:else>
												</s:else>
												<%-- Show error message against each required customer field --%>
												<%--   EB 771 for I should not see extra  spacing between the entry fields & error messages, 
														so that the page is more compact & easier to read.  --%>
												<s:if test="%{#requiredFieldsForOLK!=null && #requiredFieldsForOLK.contains(#FieldLabel)}" >
													<div style="padding: 10px 0px 10px 0px;">		
														<span class="error">Required fields missing. Please review and try again.</span>
													</div>	
												</s:if>
												<s:set name='tabIndex' value='%{#tabIndex + 1}' />
											</s:if>
											<s:else> 
												<s:hidden name='orderLine%{#FieldLabel}' id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="" />
											</s:else>
									</div>	
                                </s:iterator>
				</div>
			    	<div class="cart-special-instructions">
					<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
		    				<p>Special Instructions:</p>
				    			<s:set name='lineNoteText' value='#lineNotes.getAttribute("InstructionText")' />
								<s:hidden name="lineNotesKey" id="lineNotesKey_%{#orderLineKey}" value='%{#lineNotes.getAttribute("InstructionDetailKey")}' />				    														
								
								<s:textarea name="orderLineNote" rows="4" cols="90" onkeyup="javascript:restrictTextareaMaxLength(this,250);"
									id="orderLineNote_%{#orderLineKey}" value='%{#lineNotes.getAttribute("InstructionText")}'
									 tabindex="%{#tabIndex}" theme="simple" disabled="%{#isUOMAndInstructions}"/>									
						
							<s:if test='#isUOMAndInstructions'>
								<s:hidden name="orderLineNote" id="orderLineNote_%{#orderLineKey}" value='%{#lineNotes.getAttribute("InstructionText")}'/>
							</s:if>	
		    		</s:if>
		    			<s:else>
		    				<s:hidden name="orderLineNote" id="orderLineNote_%{#orderLineKey}" value='%{#lineNotes.getAttribute("InstructionText")}'/>
		    				<s:hidden name="lineNotesKey" id="lineNotesKey_%{#orderLineKey}" value='' />
		    			</s:else>										
					</div>
				</div>
				<div class="clearfix"></div> 
		</div>
		<s:set name='isReadOnly' value='%{#tempIsReadOnly}' />
</s:iterator>     <!-- end iterator -->
</s:form>