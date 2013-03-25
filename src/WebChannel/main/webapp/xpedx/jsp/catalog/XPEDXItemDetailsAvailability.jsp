<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<s:set name='_action' value='[0]' />
<s:set name='scuicontext' value="uiContext" />
<s:set name='showCurrencySymbol' value='true' />
<s:set name="itemID" value='#_action.getItemID()' />
<s:set name="json" value='pnaHoverMap.get(#itemID)' /> 
<s:set name="jsonUOM" value="#json.get('UOM')" /> 
<s:set name="jsonImmediate" value="#json.get('Immediate')" /> 
<s:set name="jsonNextDay" value="#json.get('NextDay')" /> 
<s:set name="jsonTwoPlus" value="#json.get('TwoPlusDays')" /> 
<s:set name="jsonAvailability" value="#json.get('Availability')" /> 
<s:set name="jsonTotalQty" value="#json.get('Total')" />
<s:set name="orderMul" value="#_action.getValidateOrderMul()" />
<s:hidden name="orderMul" value="#_action.getValidateOrderMul()" />
<s:set name="pnaErrorStatusMsg" value="#_action.getAjaxLineStatusCodeMsg()"/>
<s:hidden name="pnaErrorStatusMsg" id="pnaErrorStatusMsg" value="%{#pnaErrorStatusMsg}"/>
<s:set name="jsonUOMDesc" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#jsonUOM)" />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name="isSalesRep" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute('IS_SALES_REP')}"/>

<s:set name="OrderMultipleQtyFromSrc" value='orderMultipleMapFromSourcing.get(#itemID)' />
<s:hidden name="OrderMultipleQtyFromSrc" id="OrderMultipleQtyFromSrc" value="%{#OrderMultipleQtyFromSrc}"/>
 		 
<s:set name="orderMultiple" value="#_action.getValidateOrderMul()" />
<s:hidden name="orderMultiple" value="%{#_action.getValidateOrderMul()}" />
<s:set name="pnALineErrorMessage" value="#_action.getPnALineErrorMessage()" />
<s:set name="lineStatusCodeMsg" value="#pnALineErrorMessage.get(#itemID)" />
<s:hidden name="lineStatusCodeMsg" id="lineStatusCodeMsg" value="%{#lineStatusCodeMsg}"/>

	<div id="jsonAvalabilityDiv">
		<table class="avail-tbl" width="325"  border="0" cellspacing="0" cellpadding="0" style="margin-left:-47px;"> 
					
			<tr>
			   <!-- Fix for Jira 2885 -->			 
			   <s:set name="orderMultiple" value="#_action.getValidateOrderMul()" />
				<s:hidden name="orderMultiple" value="%{#_action.getValidateOrderMul()}" />
			    <s:set name="pnALineErrorMessage" value="#_action.getPnALineErrorMessage()" />
			    <s:set name="lineStatusCodeMsg" value="#pnALineErrorMessage.get(#itemID)" />
			    <s:hidden name="lineStatusCodeMsg" id="lineStatusCodeMsg" value="%{#lineStatusCodeMsg}"/>
			   <!-- Fix for Jira 2885 -->
				<td class="table_center" width="163"><strong>Total Available:</strong></td>
				<td class="table_right"><strong>
			<s:if test='%{#jsonTotalQty == null}'>
				<s:set name="jsonTotalQty" value="%{'0'}"></s:set>
			</s:if>
			<!--<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTotalQty)" />-->
				<s:set name="jsonTotalQty" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTotalQty)"/>	
				<s:property value="#xpedxutil.formatQuantityForCommas(#jsonTotalQty)" />
				
			<meta name="DCSext.w_x_sc" content="1"></meta><meta name="DCSext.w_x_scr" content="<s:property value='#xpedxutil.formatQuantityForCommas(#jsonTotalQty)' />"></meta>
			
			</strong></td>
				<td width="114">&nbsp;<strong><s:property value='%{#jsonUOMDesc}' /></strong></td>
			</tr>
			<tr>
				<td class="table_center">Next Day:</td>
				<td class="table_right">
			<s:if test='%{#jsonNextDay == null}'>
				<s:set name="jsonNextDay" value="%{'0'}"></s:set>
			</s:if>
			<!--<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonNextDay)" />-->
				<s:set name="jsonNextDay" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonNextDay)"/>
				<s:property value="#xpedxutil.formatQuantityForCommas(#jsonNextDay)" />			
				</td>
				<td width="114">&nbsp;<%--<s:property value='%{#jsonUOMDesc}' /> --%></td>				
			</tr>
			<tr>
				<td class="table_center">2+ Days:</td>
				<td class="table_right">
			<s:if test='%{#jsonTwoPlus == null}'>
				<s:set name="jsonTwoPlus" value="%{'0'}"></s:set>
			</s:if>
			<!-- 
			<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTwoPlus)" /> -->
			<s:set name="jsonTwoPlus" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTwoPlus)"/>
			<s:property value="#xpedxutil.formatQuantityForCommas(#jsonTwoPlus)" />		
			</td>
			<td width="114">&nbsp;<%-- <s:property value='%{#jsonUOMDesc}' /> --%></td>				
			</tr>
		</table>
		<br/>
		<p id="avail_today"><i>
			<s:if test='%{#jsonImmediate == null}'>
				<s:set name="jsonImmediate" value="%{'0'}"></s:set>
			</s:if>
		<!-- 	<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonImmediate)" />   -->
			<s:set name="jsonImmediate" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonImmediate)" /> 
			<s:property value="#xpedxutil.formatQuantityForCommas(#jsonImmediate)" />
			<s:property value='%{#jsonUOMDesc}' /> 
			available today at <s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDivisionName()" /></i></p>
	</div>
	<div id="pricesDiv">
	<s:if test="%{#orderMultiple}">
	<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
		<table class="table_left" border="0" cellspacing="0" cellpadding="0" width="365" >
			<tbody>
				<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
				<s:if test="%{#lineStatusCodeMsg != ''}">
				<tr>
					<td class="bold">My Price (<s:property value='priceCurrencyCode'/>):</td>
					<td><span class="red bold"> <s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' /> </span></td>
				</tr>
					<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr>
					<td class="bold">Extended Price (<s:property value='priceCurrencyCode'/>):</td>
					<td >
						<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
					</td></tr>
				</s:if>
					<s:elseif test="displayPriceForUoms.size()>0" >
						<s:iterator value='displayUOMs'	id='disUOM' status='disUOMStatus'>
							
							<s:set name="unitPriceForUOM" value='%{displayPriceForUoms.get(#disUOMStatus.index)}' />
							<s:set name='formattedUnitpriceForUom'	value='#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(#scuicontext,#currency,#unitPriceForUOM,#showCurrencySymbol)' />
							<s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
							<s:if test='%{#disUOM !=" "}'>
							<s:if test="#disUOMStatus.first">
							<tr>
										<td class="bold">My Price (<s:property value='priceCurrencyCode'/>):</td>
										<td>
										 	<s:if test="%{#formattedUnitpriceForUom==#priceWithCurrencyTemp1}">
														<span class="red bold"> <s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' /> </span>  
										    </s:if>
										    <s:else>
										 				<s:property value='%{#formattedUnitpriceForUom}' /> / <s:property value="#disUOM" />
										 	</s:else>
										</td>
							</tr>
							</s:if>
							<s:else>
								<tr>
										<td ></td>
										<td  > 
										 <s:if test="%{#formattedUnitpriceForUom==#priceWithCurrencyTemp1}">
										 <td></td>
										 </s:if>
										 <s:else>
														<s:property value='%{#formattedUnitpriceForUom}' /> / <s:property value="#disUOM" />
										 </s:else>
										</td>
								</tr>
							</s:else>	
							</s:if>		
							<s:else>
							<tr >
								<td class="bold">&nbsp;</td>
								<td >&nbsp;</td>
							</tr>
							<s:set name='formattedExtpriceForUom'	value='#xpedxutil.formatPriceWithCurrencySymbol(#scuicontext,#currency,#unitPriceForUOM,#showCurrencySymbol)' />
							<tr >
								<td class="bold">Extended Price (<s:property value='priceCurrencyCode'/>):</td>
								<td >
								<s:if test="%{#formattedExtpriceForUom==#priceWithCurrencyTemp}">
										<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
								</s:if>
								<s:else>
									     <s:property value='%{#formattedExtpriceForUom}' />
								</s:else>
								</td>
							</tr>
							</s:else>	
							
							
						</s:iterator>			
					</s:if>
				</s:if>

				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<s:if test="%{#isSalesRep}">	
					<tr>
					<s:set name='UOM' value='%{getPricingUOM()}'/>
					<s:set name='UOMDesc' value='%{#UOM.substring(2,(#UOM.length()))}'/>
					<s:set name='cost' value='%{getItemCost()}' />
					<s:set name='formattedCost' value='#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(#scuicontext,#currency,#cost,#showCurrencySymbol)' />
					<s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
						<td class="bold">
						<s:if test="%{#formattedCost != ''}">
						Cost (<s:property value='priceCurrencyCode'/>):&nbsp;<a id="show-hide" onclick="javascript:return showCost(this);" href="#">[Show]</a></s:if></td>
						<td><div id="cost" style="display:none;"> 
							<s:if test="%{#formattedCost == #priceWithCurrencyTemp1}">
								<s:property value='%{#formattedCost}' />
							</s:if>
							<s:else>
								<s:property value='%{#formattedCost}' /> / <s:property value='UOMDesc'/>
							</s:else>
							</div></td>	
					</tr>
				</s:if>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>

				<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
					<s:if test="isBracketPricing == 'true'">
						<tr>
						<s:if test="%{#_action.getCategory() == 'Paper'}">
							<td colspan="1" class="bold" colspan="2">My Bracket Pricing (<s:property value='priceCurrencyCode'/>):</td>
							</s:if>
							<s:set name="isMyPriceZero" value="%{'false'}" />
						<s:if test="displayPriceForUoms.size()>0" >					
							<s:iterator value='displayUOMs'	id='disUOM' status='disUOMStatus'>
								<s:if test="#disUOMStatus.last">
									<s:set name="unitPriceForUOM" value='%{displayPriceForUoms.get(#disUOMStatus.index)}' />
									<s:set name='formattedExtpriceForUom'	value='#xpedxutil.formatPriceWithCurrencySymbol(#scuicontext,#currency,#unitPriceForUOM,#showCurrencySymbol)' />
									<s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
									<s:if test="%{#formattedExtpriceForUom != #priceWithCurrencyTemp}">
										<s:iterator value='bracketsPricingList' id='bracket' status='bracketStatus'>
											<s:set name='currency' value='priceCurrencyCode'/>
											<s:set name="bracketPriceForUOM" value="bracketPrice" />
											<s:set name='formattedBracketpriceForUom' value='#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(#scuicontext,#currency,#bracketPriceForUOM,#showCurrencySymbol)' />
											<s:set name="bracketUOMDesc" value="bracketUOM" />
											<s:set name='formattedbracketUOM'
													value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#bracketUOMDesc)' />
											<s:if test="#bracketStatus.first">
												<s:set name="isMyPriceZero" value="%{'true'}" />
												<s:if test="%{#_action.getCategory() == 'Paper'}">
												<td>
														<s:property value="bracketQTY" /> 
														<s:property value="%{#formattedbracketUOM}" /> 
														- 
														<s:property value='%{#formattedBracketpriceForUom}' />
														/
														<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#_action.getPricingUOM())' />
													</td>
													</s:if>
												</tr>											
											</s:if>
											<s:else>
												<tr>
													<td>&nbsp;</td>						
													<s:if test="%{#_action.getCategory() == 'Paper'}">				
													<td>
														<s:property value="bracketQTY" /> 
														<s:property value="%{#formattedbracketUOM}" /> 
														- 
														<s:property value='%{#formattedBracketpriceForUom}' />
														/
														<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#_action.getPricingUOM())' />
													</td>
													</s:if>
												</tr>
											</s:else>											
										</s:iterator>
									</s:if>	
								</s:if>
							</s:iterator>
						</s:if>
						<s:if test="%{#isMyPriceZero == 'false'}">
							</tr>
						</s:if>
					</s:if>
				</s:if>
				</tbody>
			</table>
			</s:if>
	</div>
