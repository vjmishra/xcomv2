<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>
<%@ taglib prefix="scui" uri="scuiTag" %>
<s:set name="xutil" value="xMLUtils"/>
<s:set name='scuicontext' value="wCContext" />
<%--
            This JSP fragment can be used to display all PRICING ajustments (both header level and line level) in flyout.
			This JSP fragment expects the following inputs:
                orderDetails - the order element
                util - a UtilBean instance

--%>

<div class="allAdjustmentsLightBox hidden-data">
<s:if test="#orderDetails!= null">
<div class="maxHeight">
<div class="listTableContainer2">
<div class="padding-left3"><div class="boldText subHeaderText"><s:text name="All_adjustment_details"/></div></div>
<s:set name='pricingInfo' value='#xutil.getChildElement(#orderDetails, "PriceInfo")'/>
<s:set name='currency' value='#xutil.getAttribute(#pricingInfo, "Currency")'/>
<table class="listTableHeader2 adjustmentTableHeader">
<tr>
<td width="30%" class="padding-left1"><span class="backgroundText"><div class="boldText"><s:text name="Description"/></div></span></td>
<td width="20%" ><span class="backgroundText"><div class="boldText"><s:text name="CouponID"/></div></span></td>
<td width="35%" ><span class="backgroundText"><div class="boldText"><s:text name="Product"/></div></span></td>
<td width="15%" ><span class="backgroundText"><div class="boldText textAlignCenter"><s:text name="Amount"/></div></span></td>
</tr>

</table>
<table class="listTableBody2 adjustmentTableBody">
<s:set name="awardApplied" value="0"/>
<s:iterator value='#util.getElements(#orderDetails, "Awards/Award")' id='award' >
<s:set name='awardType' value='%{#xutil.getAttribute(#award,"AwardType")}' />
<s:set name='isPromotionOnOrder' value='%{#xutil.getAttribute(#award,"IsPromotionOnOrder")}' />

<s:if test='%{#awardType == "PRICING" || #awardType == "SHIPPING"}'>
<s:set name="awardAmount" value='#xutil.getDoubleAttribute(#award,"AwardAmount")' />
<s:if test='%{#awardAmount != "0.00"}'>
<tr>

<td width="30%" class="padding-left1" >
	<scui:i18nDB><s:property value='#award.getAttribute("Description")'/></scui:i18nDB>
</td>
<td width="20%" >
<s:if test='%{#isPromotionOnOrder == "Y"}'>
	<scui:i18nDB><s:property value='#award.getAttribute("AwardId")'/></scui:i18nDB>
</s:if>
</td>
<td width="35%" >
	<s:if test="%{#awardAmount < 0.00}">
		<s:text name="Order_Level_Discount"/>
	</s:if>
	<s:else>
		<s:text name="Order_Level_Surcharge"/>
	</s:else>
</td>
<td width="15%" >
	<s:set name="awardApplied" value='#awardApplied + #awardAmount'/>
	<div class="textAlignRight">
	<s:property value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#awardAmount)'/>
	</div>
	<s:set name="allAdjCounter" value="true"/>
</td>
</tr>
</s:if>
</s:if>
</s:iterator>
<s:iterator value='#util.getElements(#orderDetails, "HeaderCharges/HeaderCharge")' id='headerCharge' >
<s:set name='isManual' value='%{#xutil.getAttribute(#headerCharge,"IsManual")}' />
<s:set name='isShippingCharge' value='%{#xutil.getAttribute(#headerCharge,"IsShippingCharge")}' />
<s:set name="chargeAmount" value='#xutil.getDoubleAttribute(#headerCharge,"ChargeAmount")' />
<s:if test='%{#isManual == null || #isManual == "" || #isManual == "Y"}'>
<s:if test='%{#chargeAmount > 0}'>
<s:set name='isDiscount' value='%{#xutil.getAttribute(#headerCharge,"IsDiscount")}' />
<s:if test='%{#isDiscount == "Y"}'>
	<s:set name="chargeAmount" value='%{-#chargeAmount}' />
</s:if>
<tr>

<td width="30%" class="padding-left1" >
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
<td width="20%" >
	&nbsp;
</td>
<td width="35%" >
	<s:if test='%{#isDiscount == "Y"}'>
		<s:text name="Order_Level_Discount"/>
	</s:if>
	<s:else>
		<s:text name="Order_Level_Surcharge"/>
	</s:else>
</td>
<td width="15%" >
	<div class="textAlignRight">
	<s:set name="awardApplied" value='#awardApplied + #chargeAmount'/>
	<s:set name="individualChargeAmount" value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#chargeAmount)' />
	<s:property value='#individualChargeAmount'/>
	<s:set name="allAdjCounter" value="true"/>
	</div>
</td>
</tr>
</s:if>
</s:if>
</s:iterator>
<s:iterator value='#util.getElements(#orderDetails, "OrderLines/OrderLine")' id='orderLine' >
<s:set name='orderedQty' value='%{#xutil.getAttribute(#orderLine,"OrderedQty")}' />
<s:if test='%{#orderedQty > 0}'>
<s:set name='item' value='%{#xutil.getChildElement(#orderLine,"Item")}' />
<s:set name='isBundleParent' value='%{#xutil.getAttribute(#orderLine,"IsBundleParent")}' />
<s:if test='%{#isBundleParent == "Y"}'>
<s:set name='adjustmentElements' value='#xutil.getChildElement(#orderLine, "BundleOrderLineChargesAndAwards")'/>
</s:if>
<s:else>
<s:set name='adjustmentElements' value='#orderLine'/>
</s:else>
<s:set name='bundleParent' value='#xutil.getChildElement(#orderLine,"BundleParentLine")' />
<s:if test='%{#bundleParent == null}'>
<s:iterator value='#util.getElements(#adjustmentElements, "Awards/Award")' id='award' >
<s:set name='awardType' value='%{#xutil.getAttribute(#award,"AwardType")}' />
<s:set name='isPromotionOnOrder' value='%{#xutil.getAttribute(#award,"IsPromotionOnOrder")}' />
<s:set name="awardAmount" value='#xutil.getDoubleAttribute(#award,"AwardAmount")' />
<s:if test='%{#awardType == "PRICING" || #awardType == "SHIPPING"}'>
<s:if test='%{#awardAmount != "0.00"}'>
<tr>

<td width="30%" class="padding-left1" >
	<scui:i18nDB><s:property value='#award.getAttribute("Description")'/></scui:i18nDB>
</td>
<td width="20%" >
<s:if test='%{#isPromotionOnOrder == "Y"}'>
	<scui:i18nDB><s:property value='#award.getAttribute("AwardId")'/></scui:i18nDB>
</s:if>
</td>
<td width="35%" >
	<s:property value='#_action.getShortDescriptionForOrderLine(#orderLine)'/>
</td>
<td width="15%" >
	<div class="textAlignRight">
	<s:if test='%{#bundleParent == null}'>
		<s:set name="awardApplied" value='#awardApplied + #awardAmount'/>
	</s:if>
	<s:property value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#awardAmount)'/>
	<s:set name="allAdjCounter" value="true"/>
	</div>
</td>
</tr>
</s:if>
</s:if>
</s:iterator>
<s:iterator value='#util.getElements(#adjustmentElements, "LineCharges/LineCharge")' id='lineCharge' >
<s:set name='isManual' value='%{#xutil.getAttribute(#lineCharge,"IsManual")}' />
<s:set name='isShippingCharge' value='%{#xutil.getAttribute(#lineCharge,"IsShippingCharge")}' />
<s:set name="chargeAmount" value='#xutil.getDoubleAttribute(#lineCharge,"ChargeAmount")' />
<s:if test='%{#isManual == null || #isManual == "" || #isManual == "Y"}'>
<s:if test='%{#chargeAmount > 0}'>
<s:set name='isDiscount' value='%{#xutil.getAttribute(#lineCharge,"IsDiscount")}' />
<s:if test='%{#isDiscount == "Y"}'>
	<s:set name="chargeAmount" value='%{-#chargeAmount}' />
</s:if>
<tr >
<td width="30%" class="padding-left1" >
	<s:set name='chargeName' value='%{#lineCharge.getAttribute("ChargeName")}'/>
	<s:set name='chargeNameKey' value='%{#lineCharge.getAttribute("ChargeNameKey")}'/>
	<s:set name='chargeNameDescription' value='%{#chargeDescriptionMap.get(#chargeNameKey)}'/>
	<s:if test='%{#chargeNameDescription != null && #chargeNameDescription!=""}'>
		<scui:i18nDB><s:property value='%{#chargeNameDescription}'/></scui:i18nDB>
	</s:if>
	<s:else>
		<scui:i18nDB><s:property value='%{#chargeName}'/></scui:i18nDB>
	</s:else>
</td>
<td width="20%" >
	&nbsp;
</td>
<td width="35%" >
	<s:property value='#_action.getShortDescriptionForOrderLine(#orderLine)'/>
</td>
<td width="15%" >
	<div class="textAlignRight">
	<s:if test='%{#bundleParent == null}'>
		<s:set name="awardApplied" value='#awardApplied + #chargeAmount'/>
	</s:if>
	<s:set name="individualChargeAmount" value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#chargeAmount)' />
	<s:property value='#individualChargeAmount'/>
	<s:set name="allAdjCounter" value="true"/>
	</div>
</td>
</tr>

</s:if>
</s:if>
</s:iterator>
</s:if>
</s:if>
</s:iterator>
<tr><td colspan="4" id="adjustment-bottom-Border" ></td></tr>
</table>
<table class="listTableBody2 adjustmentTableBody">
<tr>
	<td width="50%"></td>
	<td width="35%" ><div class="boldText"><s:text name="Adjustments_total"/></div></td>
	<td width="15%" >
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