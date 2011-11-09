<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>
<s:set name="xutil" value="xMLUtils"/>
<s:set name='scuicontext' value="wCContext" />
<%@ taglib prefix="scui" uri="scuiTag" %>
<%--
            This JSP fragment can be used to display all SHIPPING ajustments (both header level and line level) in flyout.
			This JSP fragment expects the following inputs:
                orderDetails - the order element
                util - a UtilBean instance

  --%>
<div class="shippingAdjustmentsLightBox hidden-data">
<s:if test="#orderDetails!= null">
<div class="maxHeight">
<div class="listTableContainer2">
<div class="padding-left3"><div class="boldText subHeaderText"><s:text name="Shipping_adjustment_details"/></div></div>
<s:set name='pricingInfo' value='#xutil.getChildElement(#orderDetails, "PriceInfo")'/>
<s:set name='currency' value='#xutil.getAttribute(#pricingInfo, "Currency")'/>
<table class="listTableHeader2 adjustmentTableHeader">
<tr>
<td width="50%" class="padding-left1" ><span class="backgroundText"><div class="boldText"><s:text name="Description"/></div></span></td>
<td width="30%" ><span class="backgroundText"><div class="boldText"><s:text name="CouponID"/></div></span></td>
<td width="20%" ><span class="backgroundText"><div class="boldText textAlignCenter"><s:text name="Amount"/></div></span></td>
</tr>

</table>
<table class="listTableBody2 adjustmentTableBody">
<s:set name="awardApplied" value="0"/>
<s:iterator value='#util.getElements(#orderDetails, "Awards/Award")' id='award' >
<s:set name='awardType' value='%{#xutil.getAttribute(#award,"AwardType")}' />
<s:set name='isPromotionOnOrder' value='%{#xutil.getAttribute(#award,"IsPromotionOnOrder")}' />

<s:if test='%{#awardType == "SHIPPING"}'>
<s:set name="awardAmount" value='#xutil.getDoubleAttribute(#award,"AwardAmount")' />
<s:if test='%{#awardAmount != "0.00"}'>
<tr>

<td width="50%" class="padding-left1" >
	<scui:i18nDB><s:property value='#award.getAttribute("Description")'/></scui:i18nDB>
</td>
<td width="30%" >
<s:if test='%{#isPromotionOnOrder == "Y"}'>
	<scui:i18nDB><s:property value='#award.getAttribute("AwardId")'/></scui:i18nDB>
</s:if>
</td>
<td width="20%" >
	<div class="textAlignRight">
	<s:set name="awardApplied" value='#awardApplied + #awardAmount'/>
	<s:property value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#awardAmount)'/>
	<s:set name="shippingAdjCounter" value="true"/>
	</div>
</td>
</tr>
</s:if>
</s:if>
</s:iterator>
<s:iterator value='#util.getElements(#orderDetails, "HeaderCharges/HeaderCharge")' id='headerCharge' >
<s:set name='isManual' value='%{#xutil.getAttribute(#headerCharge,"IsManual")}' />
<s:set name='isShippingCharge' value='%{#xutil.getAttribute(#headerCharge,"IsShippingCharge")}' />
<s:set name="chargeAmount" value='#xutil.getDoubleAttribute(#headerCharge,"ChargeAmount")' />
<s:if test='%{#isManual == null || #isManual == "" ||#isManual == "Y"}'>
<s:if test='%{#isShippingCharge == "Y"}'>
<s:if test='%{#chargeAmount > 0}'>
<s:set name='isDiscount' value='%{#xutil.getAttribute(#headerCharge,"IsDiscount")}' />
<s:if test='%{#isDiscount == "Y"}'>
	<s:set name="chargeAmount" value='%{-#chargeAmount}' />
</s:if>
<tr>

<td width="50%" class="padding-left1" >
	<s:set name='chargeName' value='%{#headerCharge.getAttribute("ChargeName")}'/>
	<s:set name='chargeNameKey' value='%{#headerCharge.getAttribute("ChargeNameKey")}'/>
	<s:set name='chargeNameDescription' value='%{#chargeDescriptionMap.get(#chargeNameKey)}'/>
	<s:if test='%{#chargeNameDescription != null && #chargeNameDescription!=""}'>
		<scui:i18nDB><s:property value='%{#chargeNameDescription}'/></scui:i18nDB>
	</s:if>
	<s:else>
		<scui:i18nDB><s:property value='%{#chargeName}'/></scui:i18nDB>
	</s:else>	
</td>
<td width="30%" >
	&nbsp;
</td>
<td width="20%" >
	<div class="textAlignRight">
	<s:set name="awardApplied" value='#awardApplied + #chargeAmount'/>
	<s:set name="individualChargeAmount" value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#chargeAmount)' />
	<s:property value='#individualChargeAmount'/>
	<s:set name="shippingAdjCounter" value="true"/>
	</div>
</td>
</tr>
</s:if>
</s:if>
</s:if>
</s:iterator>
<tr><td colspan="3" id="adjustment-bottom-Border" ></td></tr>
</table>
<table class="listTableBody2 adjustmentTableBody">
<tr>
	<td width="30%"></td>
	<td width="40%" ><div class="boldText"><s:text name="Adjustments_total"/></div></td>
	<td width="30%" >
		<div class="textAlignRight">
		<s:property value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#awardApplied)'/>
		</div>
	</td>
</tr>
</table>
<BR/>
<div class="padding-left3"><a href="javascript:DialogPanel.hide('adjustmentsLightBox')" class="submitBtnBg1"><s:text name="Done" /></a>
</div>
</div>
<BR/>
</div>
</s:if>
</div>