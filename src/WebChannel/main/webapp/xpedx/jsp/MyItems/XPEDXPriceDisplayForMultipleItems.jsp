<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<s:set name='_action' value='[0]' />
<s:set name="lineNumber" value="%{1}" />
<s:set name='itemOrderSeq' value="%{1}" />	
<meta name="DCSext.w_x_sc" content="1"></meta>	
<meta name ="DCSext.w_x_scr" content='<s:property value="#webtrendTotalQty" />' />
<s:set name='webtrendTotalQty' value="#_action.buildwebtrendTagForAll()" />
<s:iterator value='#_action.getCheckItemKeys()' id="item" status="status" >
                   <input type="hidden" name="availabilityRowsHide" id="hidden_availabilityRow_<s:property value='#item'/>" />               
</s:iterator> 
<s:set name="chkItemKeys1" value="%{#_action.getCheckItemKeys()}" />
<s:hidden id='chkItemKeys' name="chkItemKeys" value='%{#chkItemKeys1}'/>

<s:iterator status="status" id="item" value='#_action.getListOfItemsFromsession()'> 
					<s:set name='id' value='#item.getAttribute("MyItemsKey")' />
					<s:set name='name' value='#item.getAttribute("Name")' />
					<s:set name='itemId' value='#item.getAttribute("ItemId")+"" ' />
					<s:set name='tmpItemOrder' value='%{#_action.getItemOrderMap().get(#itemId+":"+#itemOrderSeq)}' />
					<s:if test='%{#tmpItemOrder==null}'>
						<s:hidden id="itemOrder" name="itemOrder" value="%{#tmpItemOrder}"/>
					</s:if>
					<s:else>
						<s:hidden id="itemOrder" name="itemOrder" value="%{#tmpItemOrder.substring(#tmpItemOrder.indexOf(':'))}" /> 
					</s:else>
					<s:set name="itemOrder" value='%{#_action.getItemOrderMap().get(#itemId+":"+#itemOrderSeq)}' />	
					<s:set name="customerUom" value='%{#_action.getItemCustomerUomMap().get(#itemId+":"+#itemOrderSeq)}' />	
					<s:set name="validateOrderMul111" value="%{#_action.getValidateCheck().get(#itemId+':'+#itemOrderSeq)}" />
					<s:set name="category" value="%{#_action.getCatMap().get(#itemId+':'+#itemOrderSeq)}" />
					<s:hidden id="validateOrderMul111" name="validateOrderMul" value="%{#_action.getValidateCheck().get(#itemId+':'+#itemOrderSeq)}" />
								<s:set name="jsonKey" value='%{#itemId+"_"+#itemOrder}' />
								<div id="availabilityRow_<s:property value='#id'/>"  <s:if test='%{pnaHoverMap.containsKey(#jsonKey)}'></s:if><s:else>style="display:none; background-color:#fafafa;"</s:else> >   
                                <%--Fix For Jira 4144 --%>
                                <s:set name="itemKEY" value='#id' />
                            <!-- end prefs  -->
                         
                              
	                        	<table width="100%" style='margin-top: -2px;border:0px;' class="mil-my-price-availability" border="0">

<%-- itemId will be null if update price and availability is called to check PnA for multiple items --%>
<s:if test='%{#itemId==null || #itemId == "" }'>
	<s:set name='itemId' value='#itemID' />
</s:if>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:set name='scuicontext' value="%{#_action.getWCContext().getSCUIContext()}" />
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name='showCurrencySymbol' value='true' />
<s:set name='id' value='myItemsKey' />
<s:set name="pnALineErrorMessage" value="#_action.getPnALineErrorMessage()" />
<s:set name="lineStatusCodeMsg" value="#pnALineErrorMessage.get(#itemId)"></s:set>
<s:hidden name="lineStatusCodeMsg" id="lineStatusCodeMsg_%{#itemOrder}" value="%{#lineStatusCodeMsg}"/>
<s:set name="pnaErrorStatusMsg" value="#_action.getAjaxLineStatusCodeMsg()"/>
<s:hidden name="pnaErrorStatusMsg" id="pnaErrorStatusMsg" value="%{#pnaErrorStatusMsg}"/>
<s:hidden name="validateOrderMul" value="%{#_action.getValidateOM()}" />
<s:hidden name="pnaListitem" value="%{#_action.isPnaCall()}" />
<s:set name="orderMultipleQtyFromSrc" value='sourcingOrderMultipleForItems.get(#itemId+"_"+#itemOrder)' />
<s:hidden name="orderMultipleQtyFromSrc" id="orderMultipleQtyFromSrc_%{#itemOrder}" value="%{#orderMultipleQtyFromSrc}"/>

<s:set name="orderMulErrorCode" value="useOrderMultipleMapFromSourcing.get(#itemId+'_'+#itemOrder)" />
<s:hidden name="orderMulErrorCode" id="orderMulErrorCode_%{#itemOrder}" value="%{#orderMulErrorCode}"/>

<%-- id will be null if update price and availability is called to check PnA for multiple items --%>
<s:if test='%{#id==null || #id == ""}'>
	<s:set name='id' value='#itemKEY' />
</s:if>

<s:if test='#itemOrder == null' >
		<s:set name="jsonKey" value='%{#itemId}' />
</s:if>
<s:else>
		<s:set name="jsonKey" value='%{#itemId+"_"+#itemOrder}' />
</s:else>

<s:if test='%{priceHoverMap!=null && priceHoverMap.get(#jsonKey)!=null}' >
	<s:set name="itemPriceInfo" value='priceHoverMap.get(#jsonKey)' />
	<s:set name="displayPriceForUoms" value='%{#itemPriceInfo.getDisplayPriceForUoms()}' />
	<s:set name="bracketsPricingList" value='%{#itemPriceInfo.getBracketsPricingList()}' />
	<s:set name="isBracketPricing" value='%{#itemPriceInfo.getIsBracketPricing()}' />
	<s:set name="priceCurrencyCode" value='%{#itemPriceInfo.getPriceCurrencyCode()}' />
</s:if>
<s:else>
	<s:set name="displayPriceForUoms" value='%{displayPriceForUoms}' />
	<s:set name="bracketsPricingList" value='%{bracketsPricingList}' />
	<s:set name="isBracketPricing" value='%{isBracketPricing}' />
	<s:set name="priceCurrencyCode" value='%{priceCurrencyCode}' />
</s:else>

<s:if test="#displayPriceForUoms.size()>0" >
<s:set name="pricingUOM" value='%{#displayPriceForUoms.get(0)}' />
</s:if>
<s:else>
<s:set name="pricingUOM" value='' />
</s:else>


								

<s:if test='%{#lineStatusCodeMsg == "" && #pnaErrorStatusMsg== ""}'>
<s:set name='currency' value='#priceCurrencyCode'/>
<tbody>
		<s:set name="isQtyTextBoxEmpty" value="%{'false'}" />
		<s:if test="%{qtyTextBoxMap != null}">
			<s:if test="%{#id != ''}">
				<s:if test="%{qtyTextBoxMap.containsKey(#id)}">
					<s:set name="isQtyTextBoxEmpty" value="%{'true'}" />
				</s:if>
			</s:if>
		</s:if>					
		
		<tr style="border-top: 0px none; background:url('<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/global/dot-gray<s:property value='#wcUtil.xpedxBuildKey' />.gif') repeat-x scroll left center;">
			<s:if test="%{pnaHoverMap != null}">
				<s:if test="%{#jsonKey != ''}">
					<s:if test="%{pnaHoverMap.containsKey(#jsonKey)}">
						<s:set name="json" value='pnaHoverMap.get(#jsonKey)' />
						<s:set name="jsonUOM" value="#json.get('UOM')" />
						<s:if test="%{#customerUom == #jsonUOM}">
							<s:set name='customerUomWithoutM' value='%{#jsonUOM.substring(2, #jsonUOM.length())}' />
							<s:set name="jsonUOMDesc" value="#customerUomWithoutM" />
						</s:if>
						<s:else>
							<s:set name="jsonUOMDesc"
							value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#jsonUOM)" />
						</s:else>
						<s:set name="jsonAvailabilityMessageColor" value="#json.get('AvailabilityMessageColor')" />
						<s:set name="jsonAvailabilityBalance" value="#json.get('AvailabilityBalance')" />
						
						<s:if test="%{#isQtyTextBoxEmpty == 'false' && #jsonAvailabilityBalance != null}">
							<s:set name="jsonAvailabilityBalance" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonAvailabilityBalance)"/>
							<tr>
								<td colspan="5" width="100%" style="color:<s:property value='%{#jsonAvailabilityMessageColor}'/>;font-size:13px;padding-left:30px"><b><s:property value="#xpedxutil.formatQuantityForCommas(#jsonAvailabilityBalance)"/> <s:property value='%{#jsonUOMDesc}'/> not available</b></td>
							</tr>
						</s:if>
					</s:if>
				</s:if>
			</s:if>
			<td width="3%">&nbsp;</td>
			<td colspan="3" width="33%"><i><span>Availability</i></span></td>
			<td class="left" colspan="3" width="33%"><i>
			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
			<s:if test="%{#_action.getCatMap().get(#itemId+':'+#itemOrderSeq) == 'Paper'}" >
			<s:if test="#isBracketPricing == 'true'"><span>My Bracket Pricing (<s:property value='%{priceCurrencyCode}'/>)</span></s:if></s:if>
			<s:else>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</s:else>
			</s:if></i></td>
			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
			<td colspan="3" align="center" width="34%"><i><span> Price (<s:property value='%{priceCurrencyCode}'/>)</i></span></td>
			</s:if>

		</tr>
		
	<tr>
			<td>&nbsp;</td>
			<td colspan="3" width="36%" valign="top">


			<s:div id='availability_%{#id}' cssStyle="border-bottom:none;">
				<s:if test="%{pnaHoverMap != null}">
					<s:if test="%{#jsonKey != ''}">
						<s:if test="%{pnaHoverMap.containsKey(#jsonKey)}">
							<s:set name="json" value='pnaHoverMap.get(#jsonKey)' />
							<s:set name="jsonUOM" value="#json.get('UOM')" />
							<s:if test="%{#customerUom == #jsonUOM}">
								<s:set name='customerUomWithoutM' value='%{#jsonUOM.substring(2, #jsonUOM.length())}' />
								<s:set name="jsonUOMDesc" value="#customerUomWithoutM" />
							</s:if>
							<s:else>
								<s:set name="jsonUOMDesc"
								value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#jsonUOM)" />
							</s:else>
							
							<s:set name="jsonImmediate" value="#json.get('Immediate')" />
							<s:set name="jsonNextDay" value="#json.get('NextDay')" />
							<s:set name="jsonTwoPlus" value="#json.get('TwoPlusDays')" />
							<s:set name="jsonAvailability" value="#json.get('Availability')" />
							<s:set name="jsonTotal" value="#json.get('Total')" />
							<s:set name="jsonMyPrice"
								value="#json.get('PricingUOMUnitPrice')" />
							<s:set name="jsonPricingUOM"
								value="#json.get('PricingUOM')" />
							<s:set name="jsonMyPriceExtended"
								value="#json.get('ExtendedPrice')" />
							<s:set name="currencyCode" value="#json.get('currencyCode')" />
							<s:set name="jsonAvailabilityMessage" value="#json.get('AvailabilityMessage')" />
							<s:set name="jsonAvailabilityMessageColor" value="#json.get('AvailabilityMessageColor')" />
				<table cellpadding="0" cellspacing="0" border="0">
					
					<s:if test="%{#isQtyTextBoxEmpty == 'false'}">
						<tr>
							<td width="150" align="left" style="color:<s:property value='%{#jsonAvailabilityMessageColor}'/>;font-size:13px;"><b><s:property value='%{#jsonAvailabilityMessage}' /></b></td>
						</tr>
					</s:if>		
					<tr>
						<td><strong>Next Day:</strong></td>
						<td class="right"><strong>
						<s:if test='%{#jsonNextDay != null}'>
						<!-- 	<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonNextDay)" />  -->
							<s:set name="jsonNextDay" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonNextDay)"/>
							<s:property value="#xpedxutil.formatQuantityForCommas(#jsonNextDay)" />
						</s:if>
						<s:else>
							<s:set name="jsonNextDay" value="%{'0'}"></s:set>
							<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonNextDay)" />
						</s:else>
						</strong></td>
						<td class="left" ><%--<s:property value="#jsonUOMDesc" />--%></td>
					</tr>
					<tr>
						<td>2+ Days: </td>
						<td class="right">
						<s:if test='%{#jsonTwoPlus != null}'>
						<!-- 	<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTwoPlus)" />  -->
							<s:set name="jsonTwoPlus" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTwoPlus)"/>
							<s:property value="#xpedxutil.formatQuantityForCommas(#jsonTwoPlus)" />
						</s:if>
						<s:else>
							<s:set name="jsonTwoPlus" value="%{'0'}"></s:set>
							<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTwoPlus)" />
						</s:else>
						</td>
						<td class="left" ><%--<s:property value="#jsonUOMDesc" />--%></td>
					</tr>
					<tr>
						<td width="50%">Total Available:</td>
						<td width="20%" class="right">
						<s:if test='%{#jsonTotal != null}'>
						<!-- 	<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTotal)" />  -->
							<s:set name="jsonTotal" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTotal)"/>
							<s:property value="#xpedxutil.formatQuantityForCommas(#jsonTotal)" />
						</s:if>
						<s:else>
							<s:set name="jsonTotal" value="%{'0'}"></s:set>
							<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTotal)" />
						</s:else>
						</td>
						<td class="left" width="30%">&nbsp;<s:property value="#jsonUOMDesc" /></td>
					</tr>
					<tr>
						<td colspan="3"><i>
						<s:if test='%{#jsonImmediate != null}'>
							<!--<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonImmediate)" /> -->
							<s:set name="jsonImmediate" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonImmediate)" /> 
							<s:property value="#xpedxutil.formatQuantityForCommas(#jsonImmediate)" />
						</s:if>
						<s:else>
							<s:set name="jsonImmediate" value="%{'0'}"></s:set>
							<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonImmediate)" />
						</s:else><s:property value="#jsonUOMDesc" />&nbsp;
						 available today at <s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDivisionName()" /></i></td>
					</tr>
					<s:hidden name="price_%{#id}" value="%{#jsonMyPrice}" />
					<s:hidden name='pricingUom_%{#id}' value="%{#jsonPricingUOM}" />
					<s:hidden name="avail_%{#id}" value='%{#jsonAvailability}' />
				</table>
							
						</s:if>
						</s:if>
					</s:if>
				</s:div>

			</td>
			<s:if test="%{#_action.getValidateCheck().get(#itemId+':'+#itemOrderSeq) != true}">
			<s:if test="%{#_action.getCatMap().get(#itemId+':'+#itemOrderSeq) == 'Paper'}" >
			<td colspan="3" width="33%" valign="top">
			<%--	Using CustomerContactBean object from session
			<s:if test='%{#session.viewPricesFlag == "Y"}'>	
			--%>
			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
			<s:if test="#isBracketPricing == 'true'">
			<s:div id="bracketPricing_%{#id}" cssStyle="border-bottom:none;">
				<table cellpadding="0" cellspacing="0" border="0" width="100%">
				<s:set name="isMyPriceZero" value="%{'false'}" />
				<s:iterator value='#displayPriceForUoms' id='disUOM' status='disUOMStatus'>
					<s:if test="#disUOMStatus.last">
						<s:set name="bracketPriceForUOM" value="bracketPrice" />
						<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
						<s:if test="%{#bracketPriceForUOM==#priceWithCurrencyTemp}">
							<s:set name="isMyPriceZero" value="%{'true'}" />
						</s:if>	
					</s:if>
				</s:iterator>
				<s:if test="%{#isMyPriceZero == 'false'}">
					<s:iterator value='#bracketsPricingList' id='bracket' status='bracketStatus'>
								<s:set name="bracketPriceForUOM" value="bracketPrice" />
								<s:set name='formattedBracketpriceForUom'
									value='#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(#scuicontext,#currency,#bracketPriceForUOM,#showCurrencySymbol)' />
								<s:set name="bracketUOMDesc" value="bracketUOM" />
								<s:set name='formattedbracketUOM'
									value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#bracketUOMDesc)' />
								<s:set name='formattedPricingUOM'
									value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#jsonPricingUOM)' />
	
						<tr>
							<td ><s:property value="bracketQTY" />&nbsp;<s:property value="%{#formattedbracketUOM}" /> -&nbsp;<s:property value='%{#formattedBracketpriceForUom}' /> / <s:property value="%{#formattedPricingUOM}" /></td>
							<td ></td>
							<td ></td>
						</tr>
	
					</s:iterator>
				</s:if>					
				</table>
				</s:div>
				</s:if>
				</s:if>
			</td>
			</s:if>
			<s:else>
			<td colspan="3" width="33%" valign="top"><span></span></td>
			</s:else>
</s:if>
			<td colspan="3" width="28%" valign="top">
				<%--	Using CustomerContactBean object from session
				<s:if test='%{#session.viewPricesFlag == "Y"}'>	
				--%>
				<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
				<s:if test="#displayPriceForUoms.size()>0" >
				<s:div id="myPrice_%{#id}" cssStyle="border-bottom:none;">
				<table cellpadding="0" cellspacing="0" border="0" width="auto" class="mil-priceDiv-visibility">
				<s:set name="break" value="false"></s:set>
				
				<s:if test='%{#lineStatusCodeMsg != ""}'>
					<%-- <tbody class="mil-priceDiv-visibility" style="valign:top;">	--%>	
							<tr>
								<td width="auto" class="left">My Price:</td>
								<td class="right" width="auto"><span class="red bold"> <s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' /> </span> </td>
							</tr>
								<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
							<tr>
								<td class="left" width="auto">Extended Price:</td>
								<td class="right" width="auto"><span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span> </td>
							</tr><%--</tbody>--%>
				</s:if>
				<s:else>
					<s:iterator value='#displayPriceForUoms' id='disUOM' status='disUOMStatus'>
					<s:set name="bracketPriceForUOM" value="bracketPrice" />					
					
					<s:if test="%{#customerUom != ''}">
						<s:set name="temp" value="bracketUOM" />
						<s:set name="customerUOMDesc" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#customerUom)"/>
						<s:if test='%{#customerUOMDesc==#temp}'>	
							<s:set name='customerUomWithoutM' value='%{#customerUom.substring(2, #customerUom.length())}' />
							<s:set name="bracketUOMDesc" value="#customerUomWithoutM" />
						</s:if>
						<s:else>
							<s:set name="bracketUOMDesc" value="bracketUOM" />
						</s:else>			
					</s:if>
					<s:else>
						<s:set name="bracketUOMDesc" value="bracketUOM" />
					</s:else>
					
					<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
					<s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
					
						<s:if test="#disUOMStatus.last">
						<tr>
							<td width="auto" style="float:left;"><strong>Extended Price: </strong>&nbsp;</td>
							<td class="left" width="auto">
							<s:if test="%{#bracketPriceForUOM==#priceWithCurrencyTemp}">
								<s:set name="isMyPriceZero" value="%{'true'}" />
								<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>   
							</s:if>
							<s:else>
							<s:property value="#bracketPriceForUOM" />
							</s:else></td>
							<td>
							<%-- Hemantha: removed as per the new screen layout. 
							<s:if test="#bracketUOMDesc!=null"> / <s:property value="#bracketUOMDesc" /></s:if>
							 --%>
							</td>
						</tr>
						</s:if>
						<s:else>
						<s:if test="%{#break == false}">
							<tr>
								<td width="auto" style="float:left;"><strong><s:if test="#disUOMStatus.first">My Price: </s:if></strong>&nbsp;</td>
								<td class="left" width="auto">
								<s:if test="%{#bracketPriceForUOM == #priceWithCurrencyTemp1}">
										<s:set name="isMyPriceZero" value="%{'true'}" />
										<span class="red bold"> <s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' /> </span>
										<s:set name="break" value="true"></s:set>
							    </s:if>
							    <s:else>
										<s:property value="#bracketPriceForUOM" /> / <s:property value="#bracketUOMDesc" />
								</s:else></td>
							</tr>
						</s:if>
						</s:else>
												
					</s:iterator>
					</s:else>
					
				</table>
				</s:div>
				</s:if>
				</s:if>
			</td>
		</tr>		
		<tr style="border-bottom: 1px solid rgb(204, 204, 204);">
			<td colspan="10"></td>
		</tr>
</tbody>
</s:if>
<s:else>

<tbody>
		<tr >
			<td width="100%">
			<s:if test='%{#pnaErrorStatusMsg != ""}'>
				<h5 align="center"><b><font color="red"><s:property value="pnaErrorStatusMsg" /></font></b></h5>
			</s:if>		
			<s:elseif test='%{#lineStatusCodeMsg != ""}'>
				<h5 align="center"><b><font color="red"><s:property value="%{#lineStatusCodeMsg}"/></font></b></h5>
			</s:elseif>	
			<s:else>
				<h5 align="center"><b><font color="red">Your request could not be completed at this time, please try again.</font></b></h5>
			</s:else>
			</td>
		</tr>
		<tr style="border-bottom: 1px solid rgb(204, 204, 204);">
			<td></td>
		</tr>
		<tr style="border-bottom: 1px solid rgb(204, 204, 204);">
	<td></td>
</tr>
</tbody>
</s:else>
</table>
                        	
                        </div>
					<s:set name="lineNumber" value="%{#lineNumber+1}" />
					<s:set name='itemOrderSeq' value='%{#itemOrderSeq + 1}' />
					</s:iterator>
