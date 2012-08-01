<table class="catalog-my-price-availability">

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
    This is to avoid a defect in Struts that's creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts OGNL statements. --%>
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
<s:set name="pnALineErrorMessage" value="#_action.getPnALineErrorMessage()" />
<s:set name="lineStatusCodeMsg" value="#pnALineErrorMessage.get(#itemId)"></s:set>
<s:set name="pnaErrorStatusMsg" value="#_action.getAjaxLineStatusCodeMsg()"/>
<s:hidden name="pnaErrorStatusMsg" id="pnaErrorStatusMsg" value="%{#pnaErrorStatusMsg}"/>
<s:property value="#addToCartError"/>
<s:if test="isPnAAvailable == 'true'">
<s:if test="%{pnaHoverMap.containsKey(#itemId)}">


	<tbody>
		<tr class="my-headings" style="border-top: 0px none; background:url('<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/global/dot-gray<s:property value='#wcUtil.xpedxBuildKey' />.gif') repeat-x scroll left center;">
			<td colspan="3"><span><i>Availability</i></span></td>
			<s:if test="%{#_action.getValidateOM() == 'true'}">
			<td colspan="3" class="left">
			<s:if test="%{#_action.getCatagory() == 'Paper'}">
			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'> 
			<s:if test="isBracketPricing == 'true'"><span><i>My Bracket Pricing (<s:property value='%{priceCurrencyCode}'/>)</i></span></s:if>
			</s:if>
			</s:if>
			<s:else>
			<span>&nbsp;</span>
			</s:else></td>
			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'> 
			<td colspan="3"><span><i>Price (<s:property value='%{priceCurrencyCode}'/>)</i></span></td>
			</s:if>
			</s:if>
			
		</tr>
		<tr>
			<td colspan="3" width="36%" valign="top">

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
							<s:set name="jsonPricingUOM"
								value="#json.get('PricingUOM')" />

				<TABLE cellpadding="0" cellspacing="0" border="0">
				<TR>
					<td class="leftmost my-available"><strong>Total Available:</strong></td>
					<td class="my-number">
					<s:if test='%{#jsonTotal == null}'>
						<s:set name="jsonTotal" value="%{'0'}" />
					</s:if>
					<s:set name="jsonTotal" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTotal)"/>
					<s:property value="#xpedxutil.formatQuantityForCommas(#jsonTotal)" />
					
						<!--<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTotal)" />-->
					</td>
					<td class="my-uom"><s:property value="#jsonUOMDesc" /></td>
				</TR>
				<TR>
					<td class="my-timeframe">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Next Day:</td>
					<td class="my-number">
					<s:if test='%{#jsonNextDay == null}'>
						<s:set name="jsonNextDay" value="%{'0'}" />
					</s:if>
				<!-- 	<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonNextDay)" />  -->
					<s:set name="jsonNextDay" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonNextDay)"/>
					<s:property value="#xpedxutil.formatQuantityForCommas(#jsonNextDay)" />
				
				
					</td>
					<td class="my-uom"><%--<s:property value="#jsonUOMDesc" /> --%></td>
				</TR>
				<TR>
					<td class="my-timeframe">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2+ Days:</td>
					<td class="my-number">
					<s:if test='%{#jsonTwoPlus == null}'>
						<s:set name="jsonTwoPlus" value="%{'0'}"></s:set>
					</s:if>
				<!-- 	<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTwoPlus)" />  -->
					
					<s:set name="jsonTwoPlus" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTwoPlus)"/>
					<s:property value="#xpedxutil.formatQuantityForCommas(#jsonTwoPlus)" />
					</td>
					<td class="my-uom"><%--<s:property value="#jsonUOMDesc" /> --%></td>
				</TR>
				<TR>
					<td class="leftmost my-local-availability" colspan="3">
					<s:if test='%{#jsonImmediate == null}'>
						<s:set name="jsonImmediate" value="%{'0'}" /> 
					</s:if>
					<s:set name="jsonImmediate" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonImmediate)"/>
					<s:property value="#xpedxutil.formatQuantityForCommas(#jsonImmediate)" />&nbsp;<s:property value="#jsonUOMDesc" /> available today at <s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDivisionName()" /></td>
				<%-- 	<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonImmediate)" /> --%>
					
					
				</TR>
				</TABLE>

						</s:if>
					</s:if>
				</s:if>

			</td>
			<s:if test="%{#_action.getValidateOM() == 'true'}">
			<s:if test="%{#_action.getCatagory() == 'Paper'}">
			<td colspan="3" width='35%'>
			<%--	Using CustomerContactBean object from session
			<s:if test='%{#session.viewPricesFlag == "Y"}'>		
			--%>
			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
			<s:if test="isBracketPricing == 'true'">
				<s:if test="displayPriceForUoms.size()>0" >
					<s:iterator value='displayPriceForUoms' id='disUOM' status='disUOMStatus'>
						<s:if test="#disUOMStatus.last">
							<s:set name="bracketPriceForUOM" value="bracketPrice" />
							<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
							<s:if test="%{#bracketPriceForUOM != #priceWithCurrencyTemp}">
								<TABLE  width="100%" style="align:center;">
									<s:iterator value='bracketsPricingList' id='bracket'
											status='bracketStatus'>
					
											<s:set name='currency' value='priceCurrencyCode'/>
												<s:set name="bracketPriceForUOM" value="bracketPrice" />
												<s:set name='formattedBracketpriceForUom'
													value='#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(#scuicontext,#currency,#bracketPriceForUOM,#showCurrencySymbol)' />
												<s:set name="bracketUOMDesc" value="bracketUOM" />
												<s:set name='formattedbracketUOM'
													value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#bracketUOMDesc)' />
												<s:set name='formattedPricingUOM'
														value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#jsonPricingUOM)' />
									<TR>
										<td style="text-align:left; width='35%'; "><s:property value="bracketQTY" /> <s:property value="%{#formattedbracketUOM}" /> - <s:property value='%{#formattedBracketpriceForUom}' />
											/ <s:property value="%{#formattedPricingUOM}" />
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
			</s:if>	
			<s:else>
			<td colspan="3" width='35%'><span>&nbsp;</span></td>
			</s:else>		
			<td colspan="3" width="39%;">
			<%--	Using CustomerContactBean object from session
			<s:if test='%{#session.viewPricesFlag == "Y"}'>	
			--%>
			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
			<s:if test="displayPriceForUoms.size()>0" >
				<TABLE  width="100%">
				<s:set name="break" value="false"></s:set>
				<s:if test='%{#lineStatusCodeMsg != null}'>
				<tbody class="mil-priceDiv-visibility" style="valign:right;">
				<tr>
					<td width="auto" class="left" colspan="3"><b>My Price:</b></td>
					<td class="left" width="auto" colspan="3"><span class="red bold"> <s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' /> </span> </td>
				</tr>
				<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr>
					<td width="auto" class="left" colspan="3"><strong>Extended Price:</strong></td>
					<td class="left" width="auto" colspan="3"><span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD'/> </span> </td>
				</tr>
				</tbody>
				</s:if>	
				<s:else>
				<s:iterator value='displayPriceForUoms' id='disUOM' status='disUOMStatus'>
				<s:set name="bracketPriceForUOM" value="bracketPrice" />
				<s:set name="bracketUOMDesc" value="bracketUOM" />
				<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
				<s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
				<s:if test="#disUOMStatus.last">	
				<TR>
					<td class="left" width="50%"><strong>Extended Price:</strong></td>
					<s:if test="%{#bracketPriceForUOM == #priceWithCurrencyTemp}">
					<td class="left" colspan="2">
						<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span> </td>
					</s:if>
					<s:else>
						<td class="left" width="50%"><s:property value="#bracketPriceForUOM" /></td><td>&nbsp;</td>
					</s:else>
					
					<%-- <td>&nbsp;/&nbsp;<s:property value="#bracketUOMDesc" /></td>--%>
					
				</TR>

				</s:if>
				<s:else>
				<s:if test="%{#break == false}">
					<TR>
						<td width="45%" class="my-price left"><s:if test="#disUOMStatus.first"><strong>My Price:</strong></s:if></td>
						
						<s:if test="%{#bracketPriceForUOM == #priceWithCurrencyTemp1}">
							<td class="left" width="55%"><span class="red bold"><s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' /></span></td>
							<s:set name="break" value="true"></s:set>
						</s:if>
						<s:else>
						<td class="left" width="55%"><s:property value="#bracketPriceForUOM" />&nbsp;/&nbsp;<s:property value="#bracketUOMDesc" /></td>
						</s:else>
					</TR>
				</s:if>
				</s:else>				
				</s:iterator>			
				</s:else>	
				</TABLE>
				</s:if>
				</s:if>
			</td>
			</s:if>
		</tr>	
		
</tbody>
<s:if test='%{#lineStatusCodeMsg != ""}'>
	<tbody><tr><td colspan="9" width="100%" align="center"><b><font color="red"><s:property value="%{#lineStatusCodeMsg}"/></font></b></td></tr>
	</tbody>
</s:if>	
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
<%-- start of jira 2885 --%><tbody>
<tr >
	<td width="100%">
			<s:if test='pnaErrorStatusMsg !=null || pnaErrorStatusMsg != "" '>
				<h5 align="center"><b><font color="red"><s:property value="pnaErrorStatusMsg" /></font></b></h5><br/>
			</s:if>		
    	
    		<%--	<s:if test='%{#lineStatusCodeMsg != null}'>
				<h5 align="center"><b><font color="red"><s:property value="%{#lineStatusCodeMsg}"/></font></b></h5>
			</s:if> --%>
	</td>
</tr>	
<%-- end of jira 2885 --%>    
</s:else>

	</tbody>
</table>