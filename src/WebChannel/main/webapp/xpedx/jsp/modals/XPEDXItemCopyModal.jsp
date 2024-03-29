<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<style>
	.share-modal { width:399px!important; height:370px;;}         
	.indent-tree { margin-left:15px; }       
	.indent-tree-act { margin-left:25px; } 
</style>

<script type="text/javascript">
	function restrictMaxLength(Object, maxLen){
		var txtTextArea = Object.value;
		
		if( txtTextArea.length > maxLen ) {	
			alert(Object.title + ' should not exceed '+ maxLen + ' characters');
			var txtTruckTextArea = txtTextArea.substring(0, maxLen);
			Object.value = txtTruckTextArea;
			return false;
		}
		return true;
	}
</script> 


<div style="display: none;">

	<div id="dlgShareListHL" class="xpedx-light-box">
		<h1 id="smilTitleHL"><s:text name="MSG.SWC.MIL.COPYMIL.GENERIC.DLGTITLE" /></h1>
		
		<!-- CODE_START MIL - PN --> 
		<s:set name="isEstUser" value='%{#xpedxCustomerContactInfoBean.isEstimator()}' />
		<s:form id="XPEDXMyItemsDetailsChangeShareListHL" name="XPEDXMyItemsDetailsChangeShareListHL" action="MyItemsDetailsChangeShareList" namespace="/myItems" method="post">
		
			<p class="addmargintop0">Name</p>
			<input type="text" id="listNameHL" class="x-input text standard-textbox"
				 name="listName" value="" maxlength="35"
				onkeyup="javascript:listNameCheckHL(this);"
				onmouseover="javascript:listNameCheckHL(this);" />
			
			<p>Description</p>
			<textarea style="" rows="2" id="listDescHL" name="listDesc" value="" onkeyup="javascript:restrictTextareaMaxLength(this,255);"
					maxlength="250" title="Description" class="x-input standard-textarea"></textarea>
			<s:set name='appFlowContext' value='#session.FlowContext' />
			<s:set name='isFlowInContext' value='#util.isFlowInContext(#appFlowContext)' />
			<s:set name='orderHeaderKey' value='%{#appFlowContext.key}' />
			<s:hidden name="orderHeaderKey" value='%{#orderHeaderKey}' />
			<s:hidden name="draft" value="%{#draftOrderFlag}" />
			<s:hidden name='Currency' value='%{#currencyCode}' />
			<s:hidden name='mode' value='%{#mode}' />
			<s:hidden name='fullBackURL' value='%{#returnURL}' />
			<s:hidden name="orderLineKeyForNote" id="orderLineKeyForNote" value="" />
			
			<s:hidden name="selectedLineItem" value="1" />
			<s:hidden name="orderLineKeys" value="1" />
			<s:hidden name="orderLineItemOrders" value="" />
			<s:hidden name="fromItemDetail" value="" />
			
			<s:hidden name="orderLineItemIDs" value="%{#itemID}" />
			
			<s:set name="shortDesc" value='%{#utilMIL.formatEscapeCharacters(#xutil.getAttribute(#primaryInfoElem, "ShortDescription"))}' />
			<s:set name="longDesc" value='%{#utilMIL.formatEscapeCharacters(#xutil.getAttribute(#primaryInfoElem, "Description"))}' />
			
			<s:hidden name="orderLineItemNames" value='%{#shortDesc}' />
			<s:hidden name="orderLineItemDesc" value='%{#longDesc}' />
			
			<s:hidden name="orderLineQuantities" value="%{#xutil.getAttribute(#primaryInfoElem, 'MinOrderQuantity')}" />
			<s:hidden name="orderLineCustLineAccNo" value=" " />
			<s:hidden name="itemUOMs" value=" " />
			<s:hidden name="sendToItemDetails" value="true" />
			
			<s:hidden name="itemID" value="%{#itemID}" />
			<s:hidden name="unitOfMeasure" value="%{#parameters.unitOfMeasure}" />
			<s:hidden name="customerLinePONo" value="" />		
			
			<s:hidden name="listKey" id="listKey" value="new" />
			<s:hidden name="editMode" value="%{true}" />
			<s:hidden name="itemCount" value="%{0}" />
			<s:hidden id="clFromListId" name="clFromListId" value="" />
			<s:hidden id="clAjax" name="clAjax" value="" />
			<s:set name="rbPermissionShared" value="%{''}" />
			<s:set name="rbPermissionPrivate" value="%{''}" />
			<s:if test="%{#isUserAdmin}">
				<s:set name="rbPermissionShared" value="%{' checked '}" />
			</s:if>
			<s:else>
				<s:set name="rbPermissionPrivate" value="%{' checked '}" />
			</s:else>
		
			<s:set name="saCV" value="%{''}" />
			<s:if test='%{shareAdminOnly == "Y"}'>
				<s:set name="saCV" value="%{' checked '}" />
			</s:if>
		
			<s:iterator id="item" value='savedSharedList'>
				<s:set name='customerID' value='#item.getAttribute("CustomerID")' />
				<s:set name='customerPath' value='#item.getAttribute("CustomerPath")' />
		
				<s:hidden name="sslNames" value="%{#customerID}" />
				<s:hidden name="sslValues" value="%{#customerPath}" />
			</s:iterator>
			
			<%-- hide the list type / share options for users without authorization --%>
			<s:div cssStyle="%{#isUserAdmin || #isEstUser ? '' : 'display:none;'}">
				<p>
					List Type: 
					<input onclick="hideSharedListHLForm()" id="rbPermissionPrivate" <s:property value="#rbPermissionPrivate"/> type="radio"
							name="sharePermissionLevel" value="<s:property value="wCContext.loggedInUserId"/>"
							/>&nbsp;Personal (only you will be able to view and edit this list)
				</p>
				
				<input style="margin-left: 54px; margin-top: 5px; margin-bottom: 10px;"
						onclick="showSharedListHLForm()" 
						id="rbPermissionShared" <s:property value="#rbPermissionShared"/>
						type="radio" name="sharePermissionLevel" value=" "
						/>&nbsp;Shared &nbsp;&nbsp;&nbsp;
			</s:div>
			
			<s:set name="displayStyle" value="%{''}" />
			<s:if test="%{!#isUserAdmin }">
				<s:if test="%{!#isEstUser}">
					<s:set name="displayStyle" value="%{'display: none;'}" />
				</s:if>
			</s:if>
			
			<s:if test="%{#isUserAdmin }">		
				<span style="<s:property value="#displayStyle"/>" id="shareAdminOnlyHL" >
					<input type="checkbox" <s:property value="#saCV"/> 
					name="shareAdminOnly" id="shareAdminOnly" value="Y" /> Edit by Admin users only  <s:property value="#saCV"/> 
				</span>
			</s:if>
			<s:else>		
				<span style="<s:property value="#displayStyle"/>" id="shareAdminOnlyHL" >
					<input type="hidden"  
					name="shareAdminOnly" id="shareAdminOnly" value="" />
				</span>
			</s:else>
			
			<s:div cssStyle="%{#displayStyle}" id="dynamiccontentHL">
				<s:div id="dlgShareListShared">
					<script type="text/javascript">
						function shareSelectAll(checked_status) {
							var checkboxes = Ext.query('input[name*=customerPaths]');
							Ext.each(checkboxes, function(obj_item) {
								obj_item.checked = !checked_status;
								obj_item.click();
							});
						}
					</script>
					<s:div id="divMainShareListHL" cssClass="grey-msg x-corners addmargintop10" cssStyle="height:110px; width:740px; overflow:auto;">
						<!-- CONTENT WILL GO HERE -->
					</s:div>
				
				</s:div>
			</s:div> <%-- / dynamiccontentHL --%>
			
			<script type="text/javascript">
				function submitHL() {
					submitNewlistHLNew("XPEDXMyItemsDetailsChangeShareListHL");
				}
			</script>
			
			<div class="fFVVEM_wrap">
				<div class="clear"></div>
				<div class="error" id="errorMsgForAddressFieldsHL" style="display: none" /></div>	
				<div class="error" id="errorMsgForMandatoryFieldsHL" style="display: none" /></div>
			</div>		
				
			<div class="button-container addpadtop15">
				<input class="btn-gradient floatright addmarginright10" type="submit" value="Save" onclick="submitHL(); return false;" />
				<input class="btn-neutral floatright addmarginright10" type="submit" value="Cancel" onclick="$.fancybox.close(); return false;" />
			</div>
					
			
		</s:form>
	</div> <%-- / dlgShareListHL --%>
	
</div> <%-- / hidden div --%>
