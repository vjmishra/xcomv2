<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
    This is to avoid a defect in Struts that's creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts OGNL statements. --%>
<!-- Web Trends tag start -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag.js"></script>
<!-- Web Trends tag end  -->

<table width="100%" style='margin-top: -2px;border:0px;' class="mil-my-price-availability" border="0">


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
<s:set name="pnaErrorStatusMsg" value="#_action.getAjaxLineStatusCodeMsg()"/>
<s:hidden name="pnaErrorStatusMsg" id="pnaErrorStatusMsg" value="%{#pnaErrorStatusMsg}"/>
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
								
<tbody>
		<tr style="border-top: 0px none; background:url('<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/global/dot-gray.gif') repeat-x scroll left center;">
			<td width="3%">&nbsp;</td>
			<td colspan="3" width="32%"><i><span>Availability</i></span></td>
			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'> 
			<s:if test="%{#_action.getValidateOM() == 'true'}">
			<s:if test="%{#_action.getCatagory() == 'Paper'}">
			<td class="left" colspan="3" width="32%"><i><s:if test="#isBracketPricing == 'true'"><span>My Bracket Pricing (<s:property value='%{priceCurrencyCode}'/>)</span></s:if></i></td>
			</s:if></s:if></s:if>
			<s:else>
			<td class="left" colspan="3" width="32%"><span>&nbsp;</span></td>
			</s:else>
			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'> 
			<td colspan="3" width="32%"><i><span> Price (<s:property value='%{priceCurrencyCode}'/>)</i></span></td>
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
							<s:set name="jsonUOMDesc"
								value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#jsonUOM)" />
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

				<table cellpadding="0" cellspacing="0" border="0" >
					<tr>
						<td width="45%"><strong>Total Available:</strong></td>
						<td width="20%" class="right"><strong>
						<s:if test='%{#jsonTotal != null}'>
						<!-- 	<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTotal)" />  -->
							<s:set name="jsonTotal" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTotal)"/>
							<s:property value="#xpedxutil.formatQuantityForCommas(#jsonTotal)" />
						</s:if>
						<s:else>
							<s:set name="jsonTotal" value="%{'0'}"></s:set>
							<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTotal)" />
						</s:else>
						</strong></td>
						<td class="left" width="30%" ><strong><s:property value="#jsonUOMDesc" /></strong></td>
					</tr>
					<tr>
						<td>Next Day:</td>
						<td class="right"> 
						<s:if test='%{#jsonNextDay != null}'>
						<!-- 	<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonNextDay)" />  -->
							<s:set name="jsonNextDay" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonNextDay)"/>
							<s:property value="#xpedxutil.formatQuantityForCommas(#jsonNextDay)" />
						</s:if>
						<s:else>
							<s:set name="jsonNextDay" value="%{'0'}"></s:set>
							<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonNextDay)" />
						</s:else>
						</td>
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
						<!-- Web Trends tag start -->  
						<meta name="DCSext.w_x_sc" content="1"></meta><meta name="DCSext.w_x_scr" content="<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#jsonTotal)' />"></meta>                          	
                            	                          	
                          <!-- Web Trends tag End -->			
						</s:if>
						</s:if>
					</s:if>
				</s:div>

			</td>
			
			<s:if test="%{#_action.getValidateOM() == 'true'}">
			<s:if test="%{#_action.getCatagory() == 'Paper'}">
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
			<td colspan="3" width="28%" valign="top"><span>&nbsp;</span></td>
			</s:else>
			<td colspan="3" width="28%" valign="top">
				<%--	Using CustomerContactBean object from session
				<s:if test='%{#session.viewPricesFlag == "Y"}'>	
				--%>
				<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
				<s:if test="#displayPriceForUoms.size()>0" >
				<s:div id="myPrice_%{#id}" cssStyle="border-bottom:none;">
				<table cellpadding="0" cellspacing="0" border="0" width="100%">
				<s:set name="break" value="false"></s:set>
					<s:iterator value='#displayPriceForUoms' id='disUOM' status='disUOMStatus'>
					<s:set name="bracketPriceForUOM" value="bracketPrice" />
					<s:set name="bracketUOMDesc" value="bracketUOM" />
					<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
					<s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
						<s:if test="#disUOMStatus.last">
						<tr>
							<td><strong>Extended Price: </strong></td>
							<td class="left" width="39%">
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
								<td width="40%"><strong><s:if test="#disUOMStatus.first">My Price:</s:if></strong></td>
								<td class="left" width="60%">
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
					
				</table>
				</s:div>
				</s:if>
				</s:if>
			</td>
			</s:if>
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
			<h5 align="center"><b><font color="red">Your request could not be completed at this time, please try again.</font></b></h5>
			</td>
		</tr>
		<tr style="border-bottom: 1px solid rgb(204, 204, 204);">
			<td></td>
		</tr>
</tbody>

</s:else>
</s:if>
<s:else>

<tbody>
<%-- start of jira 2885 --%>
<tr >
	<td width="100%">
		<s:if test='pnaErrorStatusMsg !=null || pnaErrorStatusMsg != "" '>
				<h5 align="center"><b><font color="red"><s:property value="pnaErrorStatusMsg" /></font></b></h5><br/>
		</s:if>		
    		<s:if test='%{#lineStatusCodeMsg != null}'>
				<h5 align="center"><b><font color="red"><s:property value="%{#lineStatusCodeMsg}"/></font></b></h5>
		</s:if>
	</td>
</tr>	    				
<%-- end of jira 2885 --%>
<tr style="border-bottom: 1px solid rgb(204, 204, 204);">
	<td></td>
</tr>
		
		
</tbody>

</s:else>

</table>
 <s:include value="../../htmls/webtrends/webtrends.html"/>