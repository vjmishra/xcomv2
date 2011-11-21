<table class="catalog-my-price-availability">

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
    This is to avoid a defect in Struts that�s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>
<s:set name='_action' value='[0]' />
<s:set name='pnaHoverMap' value='pnaJson'/>
<s:set name='itemId' value='pnaItemId' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:set name='scuicontext' value="%{#_action.getWCContext().getSCUIContext()}" />
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name='showCurrencySymbol' value='true' />
<s:set name='id' value='myItemsKey' />
<s:set name='addToCartError' value= "addToCartError" />
<s:property value="#addToCartError"/>
<s:if test="isPnAAvailable == 'true'">
<s:if test="%{pnaHoverMap.containsKey(#itemId)}">


	<tbody>
		<tr class="my-headings">
			<td colspan="3" class="leftmost my-availability"><span><i>Availability</i></span></td>
			<td colspan="3" class="left"><span><i>My Bracket Pricing (<s:property value='%{priceCurrencyCode}'/>)</i></span></td>
			<td colspan="3" class="my-pricing"><span><i>Price (<s:property value='%{priceCurrencyCode}'/>)</i></span></td>
		</tr>
		<tr>
			<td colspan="3">

			<s:if test="%{pnaHoverMap != null}">
					<s:if test="%{#itemId != ''}">
						<s:if test="%{pnaHoverMap.containsKey(#itemId)}">
							<s:set name="json" value='pnaHoverMap.get(#itemId)' />
							<s:set name="jsonUOM" value="#json.get('UOM')" />
							<s:set name="jsonUOMDesc"
								value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#jsonUOM)" />
							<s:set name="jsonImmediate" value="#json.get('Immediate')" />
							<s:set name="jsonNextDay" value="#json.get('NextDay')" />
							<s:set name="jsonTwoPlus" value="#json.get('TwoPlusDays')" />
							<s:set name="jsonAvailability" value="#json.get('Availability')" />
							<s:set name="jsonTotal" value="#json.get('Total')" />
							<s:set name="jsonMyPrice"
								value="#json.get('PricingUOMUnitPrice')" />
							<s:set name="jsonMyPriceExtended"
								value="#json.get('ExtendedPrice')" />
							<s:set name="currencyCode" value="#json.get('currencyCode')" />

				<TABLE width="100%">
				<TR>
					<td class="leftmost my-available"><strong>Total Available:</strong></td>
					<td class="my-number"><strong>
					<s:if test='%{#jsonTotal == null}'>
						<s:set name="jsonTotal" value="%{'0'}" />
					</s:if>
						<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTotal)" />
					</strong></td>
					<td class="my-uom"><strong><s:property value="#jsonUOMDesc" /></strong></td>
				</TR>
				<TR>
					<td class="leftmost my-timeframe">Next Day:</td>
					<td class="my-number">
					<s:if test='%{#jsonNextDay == null}'>
						<s:set name="jsonNextDay" value="%{'0'}" />
					</s:if>
					<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonNextDay)" /> 
					</td>
					<td class="my-uom"><s:property value="#jsonUOMDesc" /></td>
				</TR>
				<TR>
					<td class="leftmost my-timeframe">2+ Days: </td>
					<td class="my-number">
					<s:if test='%{#jsonTwoPlus == null}'>
						<s:set name="jsonTwoPlus" value="%{'0'}"></s:set>
					</s:if>
					<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTwoPlus)" /> 
					</td>
					<td class="my-uom"><s:property value="#jsonUOMDesc" /></td>
				</TR>
				<TR>
					<td class="leftmost my-local-availability" colspan="3">
					<s:if test='%{#jsonImmediate == null}'>
						<s:set name="jsonImmediate" value="%{'0'}" /> 
					</s:if>
					<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonImmediate)" />&nbsp;<s:property value="#jsonUOMDesc" />
					&nbsp;available today at <s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDivisionName()" /></td>
					
				</TR>
				</TABLE>

						</s:if>
					</s:if>
				</s:if>

			</td>
			<td colspan="3">
			<%--	Using CustomerContactBean object from session
			<s:if test='%{#session.viewPricesFlag == "Y"}'>		
			--%>
			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
			<s:if test="isBracketPricing == 'true'">
				<s:if test="displayPriceForUoms.size()>0" >
					<s:iterator value='displayPriceForUoms' id='disUOM' status='disUOMStatus'>
						<s:if test="#disUOMStatus.last">
							<s:set name="bracketPriceForUOM" value="bracketPrice" />
							<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
							<s:if test="%{#bracketPriceForUOM != #priceWithCurrencyTemp}">
								<TABLE  width="100%">
									<s:iterator value='bracketsPricingList' id='bracket'
											status='bracketStatus'>
					
											<s:set name='currency' value='priceCurrencyCode'/>
												<s:set name="bracketPriceForUOM" value="bracketPrice" />
												<s:set name='formattedBracketpriceForUom'
													value='#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(#scuicontext,#currency,#bracketPriceForUOM,#showCurrencySymbol)' />
												<s:set name="bracketUOMDesc" value="bracketUOM" />
												<s:set name='formattedbracketUOM'
													value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#bracketUOMDesc)' />
									<TR>
										<td><s:property value="bracketQTY" /> <s:property value="%{#formattedbracketUOM}" /> - <s:property value='%{#formattedBracketpriceForUom}' />
											/ <s:property value="%{#formattedbracketUOM}" />
										</td>
										<td></td>
										<td></td>
									</TR>					
									</s:iterator>
								</TABLE>							
							</s:if>
						</s:if>
					</s:iterator>
				</s:if>
				</s:if>
				</s:if>
			</td>
			<td colspan="3">
			<%--	Using CustomerContactBean object from session
			<s:if test='%{#session.viewPricesFlag == "Y"}'>	
			--%>
			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
			<s:if test="displayPriceForUoms.size()>0" >
				<TABLE  width="100%">
				<s:set name="break" value="false"></s:set>
				<s:iterator value='displayPriceForUoms' id='disUOM' status='disUOMStatus'>
				<s:set name="bracketPriceForUOM" value="bracketPrice" />
				<s:set name="bracketUOMDesc" value="bracketUOM" />
				<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
				<s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
				
				<s:if test="#disUOMStatus.last">				

				<TR>
					<td class="my-ext-price">Extended Price:</td>
					<s:if test="%{#bracketPriceForUOM == #priceWithCurrencyTemp}">
						<td><span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span></td>
					</s:if>
					<s:else>
						<td class="my-number"><s:property value="#bracketPriceForUOM" /></td>
					</s:else>
					
					<%-- <td>&nbsp;/&nbsp;<s:property value="#bracketUOMDesc" /></td>--%>
					<td>&nbsp;</td>
				</TR>

				</s:if>
				<s:else>
				<s:if test="%{#break == false}">
					<TR>
						<td class="my-price"><s:if test="#disUOMStatus.first">My Price:</s:if></td>
						
						<s:if test="%{#bracketPriceForUOM == #priceWithCurrencyTemp1}">
							<td><span class="red bold"><s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' /></span></td>
							<s:set name="break" value="true"></s:set>
						</s:if>
						<s:else>
						<td class="my-number"><s:property value="#bracketPriceForUOM" /></td>
						<td class="my-uom">/&nbsp;<s:property value="#bracketUOMDesc" /></td>
						</s:else>
					</TR>
				</s:if>
				</s:else>							
				</s:iterator>			
				
				</TABLE>
				</s:if>
				</s:if>
			</td>
		</tr>	
		


</s:if>
<s:else>
		<tr >
			<td width="100%">
			<h5 align="center"><b><font color="red">Could not get the pricing details for this Particular Item at the moment. Please try again Later</font></b></h5>
			</td>
		</tr>
</s:else>
</s:if>
<s:else>
		<tr >
			<td width="100%">
				<h5 align="center"><b><font color="red"><s:property value="ajaxLineStatusCodeMsg" /></font></b></h5>
			</td>
		</tr>
</s:else>

	</tbody>
</table>