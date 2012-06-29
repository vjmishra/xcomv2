<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>


<s:set name='_action' value='[0]'/>
<s:set name='uId' value="%{uniqueId}"/>

	<div id="itemUI_<s:property value="#uId"/>">
		<s:hidden id="siName_%{#uId}" name="names" value="%{name}"></s:hidden>
		<s:hidden id="siDesc_%{#uId}" name="descs" value="%{desc}"></s:hidden>
		
		<table class="qaOTFTable" width="100%" >
			<tr>
				<td class="col-item" style="width: 62px; padding: 0px;">
					<s:div id="siItemTypeText_%{#uId}"><s:property value="itemTypeText"/></s:div>
					<s:hidden id="siItemType_%{#uId}" name="itemTypes" value="%{itemType}"></s:hidden>
				</td>
				<td class="col-item" style="width: 63px; padding: 0px;">
					&nbsp; 
					<s:if test='itemCustomerPartId!= ""'>
						<s:property value="itemCustomerPartId"/>
					</s:if>
					<s:elseif test='itemMpcId!= ""'>
						<s:property value="itemMpcId"/>
					</s:elseif>
					<s:elseif test='itemManufacturerId!= ""'>
						<s:property value="itemManufacturerId"/>
					</s:elseif>
					<s:else>
						<s:property value="itemId"/>
					</s:else>
					<s:hidden id="siItemId_%{#uId}" name="itemIds" value="%{itemId}"></s:hidden>
				</td>
				<td class="col-item" style="width: 39px; padding: 0px;">
					<s:textfield id="siQty_%{#uId}" cssClass="qty-field x-input" cssStyle="width: 63px;" name="qtys" value="%{qty}" maxlength="7"
					 onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" onchange="javascript:isValidQuantity(this,event);isValidQuantityRemoveAlpha(this,event);"></s:textfield>
				</td>
				<td class="col-item" style="width: 110px; padding: 0px;">
					<s:if test='%{isItemValid == true}'>
						<s:set name='uomList' value='%{#_action.getDisplayItemUOMsMap(wCContext.customerId, itemId, wCContext.storefrontId)}'/>
						<s:select cssStyle="width:110px;"
		                   	id="siUom_%{#uId}"  
		                   	name="uoms"
		                   	listKey="key"   
		                   	listValue="value"
		                   	list="#uomList" 
		                   	value='%{#_action.getRequestedDefaultUOM()}'
			            />
		            </s:if>
		            <s:else>
		            	<s:textfield id="siUom_%{#uId}" cssClass="qty-field x-input" name="uoms" value="%{itemUomId}" cssStyle="width: 63px;"></s:textfield>
		            </s:else>
				</td>
				<s:if test='jobIdFlag'>
					<td class="col-item" style="width: 53px;">
						<s:textfield id="siJobId_%{#uId}" cssClass="qty-field x-input" cssStyle="width: 154px;" name="jobIds" maxlength="24"  value="%{jobId}"></s:textfield>
					</td>
				</s:if>
				<s:if test="customerPOFlag">
				<td class="col-item" style="">
					<s:textfield id="siPurchaseOrder_%{#uId}" cssClass="qty-field x-input" cssStyle="width: 154px;" name="purchaseOrders" maxlength="22"  value="%{purchaseOrder}" ></s:textfield>
				</td>
				</s:if>
			</tr>
		</table>
	</div>
<s:if test='%{isItemValid == false}'>
	<s:div id="invalidItemMessage_%{#uId}">
	The item '<s:property value="itemId"/>' is not valid. Please review and try again or contact Customer Service.
	<%-- The item '<s:property value="itemId"/>' is not a valid item. Please review your entry and try again or <a href="javascript:showSpecialItem('divSpecialItemContent_<s:property value="#uId"/>'); " >add as a non-catalog item.</a>--%>
	</s:div>
<%-- Hemantha --%>
<s:include value="../order/modals/XPEDXNonCatalogItemModal.jsp" />
<%--
 	<div id="divSpecialItemContent_<s:property value="#uId"/>" style="height: 380px; display: none;">
      <div class="xpedx-light-box" id="inline1">
		<h2>Add Special or Non-Stocked Item</h2>
        <br>
        <p>
			<strong>HINT:</strong>
			Use this form to add items you can't find in the online catalog, or would like to special order. 
		</p>
		<p>Items added here may be deleted from your shopping cart at any time.</p>
        <br>
		<table width="100%" id="lightbox-table">
			<tbody>
				<tr class="table-header-bar" id="none">
					<td width="350" class="no-border table-header-bar-left">
                    	<span class="white">Product Description</span>
                    </td>
                    <td align="center" width="55" class="no-border">
                    	<span class="white">Quantity</span>
                    </td>
                    <td align="center" class="no-border">
                    	<span class="white">UOM</span>
                    </td>
                    <s:if test='jobIdFlag'>
	                    <td align="center" width="155" class="no-border-right table-header-bar-right">
	                    	<span class="white">Job # </span>
	                    </td>
                    </s:if>
				</tr>
				<tr>
                	<td valign="top">
						<s:textarea  name="siUIDesc_%{#uId}" rows="5" cols="40" tabindex="1"/>
                    </td>
                    <td valign="top">
                    	<s:textfield cssClass="input-label numeric" tabindex="2" cssStyle="text-align: right;" title="QTY" name="siUIQty_%{#uId}" value="%{qty}" size="10" onkeyup="javascript:isValidQuantity(this);"/> 
                    </td>
                    <td valign="top" class="createdby-lastmod">
						<s:textfield cssClass="input-label" tabindex="3" cssStyle="text-align: right;" title="UOM" name="siUIUom_%{#uId}"  value=" " size="12"/>
                    </td>
                    <s:if test='jobIdFlag'>
	                    <td align="center" valign="top">
	                    	
	                    	<s:textfield cssStyle="width: 125px;" title="Job#" tabindex="4" cssClass="input-label" name="siUIJobId_%{#uId}" value="%{jobId}" size="55"/>
	                    </td>
                    </s:if>
				</tr>
			</tbody>
		</table>
			
		<div id="table-bottom-bar">
			<div id="table-bottom-bar-L"></div>
			<div id="table-bottom-bar-R"></div>
		</div>
           <ul class="tool-bar-bottom" id="tool-bar">
           	<li>
               	<a href="javascript:$.fancybox.close();" class="grey-ui-btn" tabindex="5">
					<span>Cancel</span>
                   </a>
			</li>
               <li style="float: right;">
               	<a href="javascript:addSpecialItem('<s:property value="#uId"/>');" class="grey-ui-btn" tabindex="6">
                   	<span>Add to Cart</span>
				</a>
			</li>
		</ul>			
	  </div>
    </div>            
 --%>         
               
</s:if>

	<SCRIPT type="text/javascript">
		//Hide the item data if it is a custom item
		
		var el = Ext.get("itemUI_<s:property value="#uId"/>");
		el.setVisibilityMode(Ext.Element.DISPLAY);
		if (<s:property value="%{isItemValid}"/> == false){
			el.hide();
		}
		
		updateValidation();
	</SCRIPT>
