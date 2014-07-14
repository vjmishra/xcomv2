<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set name="xutil" value="xMLUtils"/>

<s:set name='subtotalWithoutTaxes' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#overallTotals.getAttribute("SubtotalWithoutTaxes"))'/>
<s:set name='hdrShippingTotal' value='#xutil.getDoubleAttribute(#overallTotals,"HdrShippingTotal")'/>
<s:set name='hdrShippingBaseCharge' value='#xutil.getDoubleAttribute(#overallTotals,"HdrShippingBaseCharge")'/>
<s:set name='hdrAdjustmentWithoutShipping' value='#xutil.getDoubleAttribute(#overallTotals,"HeaderAdjustmentWithoutShipping")'/>
  
<s:set name='headerAdjustmentWithoutShipping' value ='%{#hdrAdjustmentWithoutShipping - #hdrShippingTotal + #hdrShippingBaseCharge}'/>
<s:set name='adjustedSubtotalWithoutTaxes' value ='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,(#overallTotals.getAttribute("AdjustedSubtotalWithoutTaxes") - #hdrShippingTotal + #hdrShippingBaseCharge))'/>
<s:set name='grandTax' value ='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#overallTotals.getAttribute("GrandTax"))'/>

<s:set name='grandTotal' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#overallTotals.getAttribute("GrandTotal"))'/>
<s:set name='shippingCharges' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#overallTotals.getAttribute("HdrShippingTotal"))'/>
<s:set name="shippingAdjCounter" value="false"/>
<s:set name="allAdjCounter" value="false"/>
<s:include value="XPEDXOrderTotalAdjustments.jsp"/>
<s:include value="XPEDXOrderShippingAdjustments.jsp"/>
<s:include value="XPEDXAllAdjustments.jsp"/>
<table  class="listSubTable" id="listSubTable1">
        <tr>
            <td class="textAlignRight boldText field-label"><s:text name='SubtotalOfItemsColon'/></td>
            <td class="cartSubTotal textAlignRight">
            	<label><s:property value='#subtotalWithoutTaxes'/></label>
     		</td>
        </tr>
        <tr>
            <td class="textAlignRight field-label">
            <s:if test ='%{#hideLink=="true"}'>
               <s:text name='OrderTotalAdjustmentsColon'/>
            </s:if>
            <s:elseif test='%{#headerAdjustmentWithoutShipping != "0.00" }'>
               <s:a id='hdrAdjShippingId'  href="javascript:displayLightbox('orderTotalAdjustmentLightBox')" tabindex='%{#orderShippingAndTotalStartTabIndex+1}'><s:text name='OrderTotalAdjustmentsColon'/></s:a>
			</s:elseif>
			<s:else>
				<s:text name='OrderTotalAdjustmentsColon'/>
            </s:else>
            </td>
            <td class="textAlignRight">
            <label>
			<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#headerAdjustmentWithoutShipping)'/></label>
            </td>
        </tr>
        <tr>
         <td class="textAlignRight field-label"><s:text name='AdjustedSubtotalColon'/></td>
         <td class="textAlignRight">
              <label><s:property value='#adjustedSubtotalWithoutTaxes'/></label>
            </td>
        </tr>
        <tr>
        	<td class="textAlignRight field-label"><s:text name='TaxColon'/></td>
            <td class="textAlignRight">
             <label><s:property value='#grandTax'/></label>
            </td>
        </tr>
        <tr>
        	 <td class="textAlignRight bottomBorder field-label">
             <s:if test ='%{#hideLink=="true"}'>
                <s:text name='ShippingCosts'/>
             </s:if>
            <s:elseif test="%{#shippingAdjCounter==true}">
 		       	 <s:a id='shipingAdjId' href="javascript:displayLightbox('shippingAdjustmentsLightBox')" tabindex='%{#orderShippingAndTotalStartTabIndex+2}'><s:text name='ShippingCosts'/></s:a>
            </s:elseif>
			<s:else>
				<s:text name='ShippingCosts'/>
			</s:else>
        	 </td>
            <!--<td class="textAlignRight bottomBorder"><s:property value='#util.formatPriceWithCurrencySymbol(getWCContext().getSCUIContext(),#currencyCode,#adjustedShippingTotal,#showCurrencySymbol)'/></td>-->
            <td class="shippingCharge textAlignRight bottomBorder">
              <label><s:property value='#shippingCharges'/></label>
            </td>
        </tr>
        <tr>
            <td class="textAlignRight subHeaderText bottomBorder">
			  <s:if test ="%{#allAdjCounter==true}">
			 <a id="totalPriceLinkId" href="javascript:displayLightbox('allAdjustmentsLightBox')" tabindex="<s:property value='%{#orderShippingAndTotalStartTabIndex+3}'/>"><s:text name='TotalPriceColon'/></a>
			 </s:if>
			 <s:else>
				<s:text name='TotalPriceColon'/>
			 </s:else>
			</td>
            <td class="cartGrandTotal textAlignRight bottomBorder">
             <label><s:property value='#grandTotal'/></label>


            </td>
        </tr>
  </table>
