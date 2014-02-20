<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<%--
	***** REMINDER TO DEVELOPER ABOUT DUPLICATE CODE *****
	When making changes to this page you will likely need to make changes to XPEDXPriceDisplayForMultipleItems.
	This is because the MIL page calls separate actions for single vs multiple Price and Availability (see EB-4767 for details).
--%>

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
    This is to avoid a defect in Struts that's creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts OGNL statements. --%>
<!-- Web Trends tag start -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Web Trends tag end  -->

<!-- <table width="100%" style='margin-top: -2px;border:0px;' class="mil-my-price-availability" border="0"> -->


<s:set name='_action' value='[0]' />
<s:set name='itemId' value='pnaItemId' />
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
<s:set name="isReqCustomerUOM" value="%{#_action.getIsPnaReqCustomerUOM()}"></s:set>
<s:set name="reqCustomerUOM" value="%{#_action.getPnaReqCustomerUOM()}"></s:set>
<s:set name="customerUOM" value="#pnALineErrorMessage.get(#itemId)"></s:set>
<s:set name="pnaErrorStatusMsg" value="#_action.getAjaxLineStatusCodeMsg()"/>
<s:hidden name="pnaErrorStatusMsg" id="pnaErrorStatusMsg" value="%{#pnaErrorStatusMsg}"/>
<s:set name="orderMultipleQtyFromSrc" value='sourcingOrderMultipleForItems.get(#itemId)' />
<s:hidden name="orderMultipleQtyFromSrc" id="orderMultipleQtyFromSrc_%{#id}" value="%{#orderMultipleQtyFromSrc}"/>
<s:set name="qtyTxtBox" value='#_action.getQtyTextBox()' />
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


<s:if test="isPnAAvailable == 'true'">
	<s:if test="%{pnaHoverMap.containsKey(#jsonKey)}">
		<s:set name='currency' value='#priceCurrencyCode'/>
		<s:set name="json" value='pnaHoverMap.get(#jsonKey)' />
		<s:set name="jsonUOM" value="#json.get('UOM')" />
		<s:if test="%{#customerUom == #jsonUOM}">
			<s:set name='customerUomWithoutM' value='%{#jsonUOM.substring(2, #jsonUOM.length())}' />
			<s:set name="jsonUOMDesc" value="#customerUomWithoutM" />
		</s:if>
		<s:else>
			<s:set name="jsonUOMDesc" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#jsonUOM)" />
		</s:else>
		<s:set name="jsonAvailabilityMessageColor" value="#json.get('AvailabilityMessageColor')" />
		<s:set name="jsonAvailabilityBalance" value="#json.get('AvailabilityBalance')" />
		
		<s:if test='%{#lineStatusCodeMsg == "" && #_action.getIsOMError() != "true"}'>
			 <s:set name="showPaBracket" value='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y" && #_action.getCatagory().trim().equals("Paper") && #_action.getValidateOM() == "true"}' />
			<s:set name="showPaPrices" value='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y" && #displayPriceForUoms.size() > 0}' />
			<%-- since the availability/bracket/pricing columns may be hidden, we indicate whether the P&A section is 1, 2, or 3 columns. this allows css specificity to customize layout --%>
			<s:if test="%{#showPaBracket && #showPaPrices}">
				<s:set name="milPaWrapClass" value="%{'three-col'}" />
			</s:if>
			<s:elseif test="%{#showPaBracket || #showPaPrices}">
				<s:set name="milPaWrapClass" value="%{'two-col'}" />
			</s:elseif>
			<s:else>
				<s:set name="milPaWrapClass" value="%{'one-col'}" />
			</s:else>
			<s:div cssClass="mil-pa-wrap %{#milPaWrapClass}">
				<s:if test='%{#lineStatusCodeMsg == "" && #_action.getIsOMError() != "true"}'>
					<s:if test='%{#qtyTxtBox != null && #qtyTxtBox != 0 && #jsonAvailabilityBalance != null}'>
						<s:div cssStyle="color:%{#jsonAvailabilityMessageColor}; font-size:13px; padding-left:30px; line-height:22px;">
							<s:set name="jsonAvailabilityBalance" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonAvailabilityBalance)"/>
							<s:property value="#xpedxutil.formatQuantityForCommas(#jsonAvailabilityBalance)"/> <s:property value='%{#jsonUOMDesc}'/> not available
						</s:div>
					</s:if>
					
					<div class="mil-pa-avail">
						<h4>Availability</h4>
						<s:div id="availability_%{#id}" cssClass="addpadleft20">
							<s:if test="%{pnaHoverMap != null && #jsonKey != '' && pnaHoverMap.containsKey(#jsonKey)}">
								<s:set name="json" value='pnaHoverMap.get(#jsonKey)' />
								<s:set name="jsonUOM" value="#json.get('UOM')" />
								<s:if test='%{#reqCustomerUOM==#jsonUOM}'>
									<s:set name='customerUomWithoutM' value='%{#reqCustomerUOM.substring(2, #reqCustomerUOM.length())}' />
									<s:set name="jsonUOMDesc" value="#customerUomWithoutM" />
								</s:if>
								<s:else>
									<s:set name="jsonUOMDesc" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#jsonUOM)" />
								</s:else>
								<s:set name="jsonImmediate" value="#json.get('Immediate')" />
								<s:set name="jsonNextDay" value="#json.get('NextDay')" />
								<s:set name="jsonTwoPlus" value="#json.get('TwoPlusDays')" />
								<s:set name="jsonAvailability" value="#json.get('Availability')" />
								<s:set name="jsonTotal" value="#json.get('Total')" />
								<s:set name="jsonMyPrice" value="#json.get('PricingUOMUnitPrice')" />
								<s:set name="jsonPricingUOM" value="#json.get('PricingUOM')" />
								<s:set name="jsonMyPriceExtended" value="#json.get('ExtendedPrice')" />
								<s:set name="currencyCode" value="#json.get('currencyCode')" />
								<s:set name="jsonAvailabilityMessage" value="#json.get('AvailabilityMessage')" />
								
								<s:hidden name="price_%{#id}" value="%{#jsonMyPrice}" />
								<s:hidden name='pricingUom_%{#id}' value="%{#jsonPricingUOM}" />
								<s:hidden name="avail_%{#id}" value='%{#jsonAvailability}' />
								
								<!-- Web Trends tag start -->  
								<meta name="DCSext.w_x_sc" content="1" />
								<meta name="DCSext.w_x_scr" content="<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTotal)' />" />                          	
								<!-- Web Trends tag End -->
								
								<table class="addpad3">
									<tbody>
										<tr>
											<td colspan="3" align="left" style="color:<s:property value='%{#jsonAvailabilityMessageColor}'/>;font-size:13px;">
												<b>
													<s:property value="%{#jsonAvailabilityMessage}"/>
												</b>
											</td>
										</tr>
										<tr>
											<td align="left" class="boldtype">Next Day:</td>
											<td class="right boldtype">
												<s:if test='%{#jsonNextDay != null}'>
													<s:set name="jsonNextDay" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonNextDay)"/>
													<s:property value="#xpedxutil.formatQuantityForCommas(#jsonNextDay)" />
												</s:if>
												<s:else>
													<s:set name="jsonNextDay" value="%{'0'}"></s:set>
													<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonNextDay)" />
												</s:else>
											</td>
											<td class="left"></td>
										</tr>
										<tr>
											<td align="left">2+ Days: </td>
											<td class="right">
												<s:if test='%{#jsonTwoPlus != null}'>
													<s:set name="jsonTwoPlus" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTwoPlus)"/>
													<s:property value="#xpedxutil.formatQuantityForCommas(#jsonTwoPlus)" />
												</s:if>
												<s:else>
													<s:set name="jsonTwoPlus" value="%{'0'}"></s:set>
													<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTwoPlus)" />
												</s:else>
											</td>
											<td class="left"></td>
										</tr>
										<tr>
											<td width="33%" align="left">Total Available:</td>
											<td class="right" width="22%">
												<s:if test='%{#jsonTotal != null}'>
													<s:set name="jsonTotal" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTotal)"/>
													<s:property value="#xpedxutil.formatQuantityForCommas(#jsonTotal)" />
												</s:if>
												<s:else>
													<s:set name="jsonTotal" value="%{'0'}"></s:set>
													<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTotal)" />
												</s:else>
											</td>
											<td class="left" width="45%" style="padding-left: 4px;">
												<s:property value="#jsonUOMDesc" />
											</td>
										</tr>
									</tbody>
								</table>
								<table class="addpad3">
									<tbody>
										<tr>
											<td colspan="3" class="addpadtop5">
												<i>
													<s:if test='%{#jsonImmediate != null}'>
														<s:set name="jsonImmediate" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonImmediate)" />
														<s:property value="#xpedxutil.formatQuantityForCommas(#jsonImmediate)" />
													</s:if>
													<s:else>
														<s:set name="jsonImmediate" value="%{'0'}"></s:set>
														<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonImmediate)" />
													</s:else>
													<s:property value="#jsonUOMDesc" />
													&nbsp;
													available today at 
													<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDivisionName()" />
												</i>
											</td>
										</tr>
									</tbody>
								</table>
							</s:if>
						</s:div> <%-- / availability_ --%>
					</div> <%-- / mil-pa-avail --%>
					
					<s:if test='%{#showPaBracket}'>
						<div class="mil-pa-bracket">
								<h4>
									Bracket Pricing (<s:property value='%{priceCurrencyCode}'/>)
								</h4>
								<s:div id="bracketPricing_%{#id}" cssClass="addpadleft20">
									<table width="260px;" class="addpad3">
										<tbody>
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
													<s:set name='formattedBracketpriceForUom' value='#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(#scuicontext,#currency,#bracketPriceForUOM,#showCurrencySymbol)' />
													<s:set name="temp" value="bracketUOM" />
													<s:set name="reqCustomerUOMDesc" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#reqCustomerUOM)"/>
													<s:if test='%{#reqCustomerUOMDesc==#temp}'>
														<s:set name='customerUomWithoutM' value='%{#reqCustomerUOM.substring(2, #reqCustomerUOM.length())}' />
														<s:set name="bracketUOMDesc" value="#customerUomWithoutM" />
													</s:if>
													<s:else>
														<s:set name="bracketUOMDesc" value="bracketUOM" />
													</s:else>
													<s:set name='formattedbracketUOM' value='#bracketUOMDesc' />
													<s:if test='%{#reqCustomerUOM==#jsonPricingUOM}'>
														<s:set name='customerUomWithoutM' value='%{#reqCustomerUOM.substring(2, #reqCustomerUOM.length())}' />
														<s:set name="formattedPricingUOM" value="#customerUomWithoutM" />
													</s:if>
													<s:else>
														<s:set name='formattedPricingUOM' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#jsonPricingUOM)' />
													</s:else>
													<tr>
														<td align="right">
															<s:property value="bracketQTY" />&nbsp;<s:property value="%{#formattedbracketUOM}" />
														</td>
														<td>
															-&nbsp;<s:property value='%{#formattedBracketpriceForUom}' /> / <s:property value="%{#formattedPricingUOM}" />
														</td>
													</tr>
												</s:iterator>
											</s:if>
										</tbody>
									</table>
								</s:div> <%-- / bracketPricing_ --%>
						</div> <%-- / mil-pa-bracket --%>
					</s:if>
					
					<s:if test='%{#showPaPrices}'>
						<div class="mil-pa-price">
							<h4>
								Price (<s:property value='%{priceCurrencyCode}'/>)
							</h4>
							<s:div id="myPrice_%{#id}" cssClass="addpadleft20">
								<table class="mil-priceDiv-visibility" width="auto" cellspacing="0" cellpadding="0" border="0">
									<tbody>
										<s:set name="break" value="false"></s:set>
										<s:iterator value='#displayPriceForUoms' id='disUOM' status='disUOMStatus'>
											<s:set name="bracketPriceForUOM" value="bracketPrice" />
											<s:set name="temp" value="bracketUOM" />
											<s:set name="reqCustomerUOMDesc" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#reqCustomerUOM)"/>
											<s:if test='%{#reqCustomerUOMDesc==#temp}'>
												<s:set name='customerUomWithoutM' value='%{#reqCustomerUOM.substring(2, #reqCustomerUOM.length())}' />
												<s:set name="bracketUOMDesc" value="#customerUomWithoutM" />
											</s:if>
											<s:else>
												<s:set name="bracketUOMDesc" value="bracketUOM" />
											</s:else>
											<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
											<s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
											<s:if test="#disUOMStatus.last">
												<tr>
													<td width="auto" class="addpadtop5 addpadright5"><strong>Extended Price: </strong></td>
													<td class="left addpadtop5" width="auto">
														<s:if test="%{#bracketPriceForUOM==#priceWithCurrencyTemp}">
															<s:set name="isMyPriceZero" value="%{'true'}" />
															<span class="red bold">
																<s:text name='MSG.SWC.ORDR.OM.INFO.TBD' />
															</span>
														</s:if>
														<s:else>
															<s:property value="#bracketPriceForUOM" />
														</s:else>
													</td>
													<td></td>
												</tr>
											</s:if>
											<s:elseif test="%{#break == false}">
												<tr>
													<td width="auto" class="addpadright5">
														<s:if test="#disUOMStatus.first">
															<strong>My Price: </strong>
														</s:if>
													</td>
													<td class="left" width="auto">
														<s:if test="%{#bracketPriceForUOM == #priceWithCurrencyTemp1}">
															<s:set name="isMyPriceZero" value="%{'true'}" />
															<span class="red bold">
																<s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' />
															</span>
															<s:set name="break" value="true"></s:set>
														</s:if>
														<s:else>
															<s:property value="#bracketPriceForUOM" />
															/ 
															<s:property value="#bracketUOMDesc" />
														</s:else>
													</td>
												</tr>
											</s:elseif>
										</s:iterator>
									</tbody>
								</table>
							</s:div>
						</div> <%-- / mil-pa-price --%>
					</s:if>
				</s:if>
			</s:div> <%-- / mil-pa-wrap --%>
		</s:if>
		<s:elseif test='%{#lineStatusCodeMsg != "" && #_action.getIsOMError() != "true"}'>
			<div class="mil-pa-wrap">
				<h5 align="center"><b><font color="red"><s:property value="%{#lineStatusCodeMsg}"/></font></b></h5>
			</div>
		</s:elseif>
	</s:if>
	<s:else>
		<div class="mil-pa-wrap">
			<h5 align="center"><b><font color="red">Your request could not be completed at this time, please try again.</font></b></h5>
		</div>
	</s:else>
</s:if>
<s:else>
	<div class="mil-pa-wrap">
		<h5 align="center"><b><font color="red"><s:property value="pnaErrorStatusMsg" /></font></b></h5>
	</div>
</s:else>

<s:include value="../../htmls/webtrends/webtrends.html"/>
