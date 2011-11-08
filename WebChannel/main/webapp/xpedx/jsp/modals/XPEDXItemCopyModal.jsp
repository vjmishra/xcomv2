<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<style>
.share-modal { width:399px!important; height:370px;;}         
.indent-tree { margin-left:15px; }       
.indent-tree-act { margin-left:25px; } 
   
</style>

<script language="javascript" type="text/javascript">

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

<div id="dlgShareListHL" class="xpedx-light-box"
	style="width: 750px !important; height:450px">
<h2 id="smilTitleHL">Copy My Items List</h2>

<!-- CODE_START MIL - PN --> 

<s:form id="XPEDXMyItemsDetailsChangeShareListHL"
	name="XPEDXMyItemsDetailsChangeShareListHL"
	action="XPEDXMyItemsDetailsChangeShareList" namespace="/xpedx/myItems"
	method="post">

<!-- 	<div class="error" id="errorMsgForMandatoryFieldsHL" style="display: none" /></div> -->
	<p>Name</p>
	<div class="clear"></div>

	<input type="text" id="listNameHL" class="x-input text standard-textbox"
		 name="listName" value="" maxlength="35"
		onkeyup="javascript:listNameCheckHL(this);"
		onmouseover="javascript:listNameCheckHL(this);" />
	
	<p>Description</p>
	<div class="clear"></div>

	<textarea style="" rows="2" id="listDescHL" name="listDesc" value="" onkeyup="javascript:restrictTextareaMaxLength(this,255);" maxlength="255" title="Description"
		class="x-input standard-textarea"></textarea>

	<s:hidden name="listKey" value="new"></s:hidden>
	<s:hidden name="editMode" value="%{true}"></s:hidden>
	<s:hidden name="itemCount" value="%{0}"></s:hidden>
	<s:hidden id="clFromListId" name="clFromListId" value=""></s:hidden>
	<s:hidden id="clAjax" name="clAjax" value=""></s:hidden>
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

	<!-- START - Saved hidden data Fields -->
	<s:iterator id="item" value='savedSharedList'>
		<s:set name='customerID' value='#item.getAttribute("CustomerID")' />
		<s:set name='customerPath' value='#item.getAttribute("CustomerPath")' />

		<s:hidden name="sslNames" value="%{#customerID}"></s:hidden>
		<s:hidden name="sslValues" value="%{#customerPath}"></s:hidden>
	</s:iterator>
	<!-- END - Saved hidden data Fields -->

	<!-- Private and Shared are missing from the HTMLs -->
	<s:if test="%{!#isUserAdmin}">
		<div style="display: none;">
	</s:if>
	
	<p>
					List Type: 
	<input onclick="hideSharedListHLForm()" id="rbPermissionPrivate"
		<s:property value="#rbPermissionPrivate"/> type="radio"
		name="sharePermissionLevel"
		value="<s:property value="wCContext.loggedInUserId"/>" />&nbsp;Personal (only you will be able to view and edit this list)</p>
	
	<input style="margin-left: 54px; margin-top: 5px; margin-bottom: 10px;"
		onclick="showSharedListHLForm()" 
		id="rbPermissionShared" <s:property value="#rbPermissionShared"/>
		type="radio" name="sharePermissionLevel" value=" " />&nbsp;Shared &nbsp;&nbsp;&nbsp;

	<s:if test="%{!#isUserAdmin}">
		</div>
	</s:if>
	
	<s:set name="displayStyle" value="%{''}" />
	<s:if test="%{!#isUserAdmin}">
		<s:set name="displayStyle" value="%{'display: none;'}" />
	</s:if>
	
	<span style="<s:property value="#displayStyle"/>" id="shareAdminOnlyHL" >
		<input type="checkbox" <s:property value="#saCV"/> 
		name="shareAdminOnly" id="shareAdminOnly" value="Y" /> Edit by Admin users only
	</span>
	
	<div style="<s:property value="#displayStyle"/>" id="dynamiccontentHL">

	<!-- Placeholder for the dynamic content -->
	<s:div id="dlgShareListShared">
		<script type="text/javascript">
			function shareSelectAll(checked_status) {
				//var checked_status = this.checked;
				var checkboxes = Ext.query('input[name*=customerPaths]');
				Ext.each(checkboxes, function(obj_item) {
					obj_item.checked = !checked_status;
					obj_item.click();
					//obj_item.fireEvent('click');
				});
			}
		</script>
		<!-- START - BODY OF SHARE FORM -->
		<s:div id="divMainShareListHL" cssClass="grey-msg x-corners" cssStyle="height:110px; width:740px; overflow:auto;">
			<!-- CONTENT WILL GO HERE -->
		</s:div>
		<!-- END - BODY OF SHARE FORM -->
	
	</s:div>
	</div>
	
	<SCRIPT type="text/javascript">
		/*if ("<s:property value="rbPermissionPrivate"/>" != ""){
			hideForm('ShareListShared');
		}*/
	
		function submitHL() {
			submitNewlistHLNew("XPEDXMyItemsDetailsChangeShareListHL");
		}
	</SCRIPT>
<div class="fFVVEM_wrap"><div class="clear"></div>
		<div class="error" id="errorMsgForAddressFieldsHL"
		style="display: none" /></div>	
<div class="error" id="errorMsgForMandatoryFieldsHL"
		style="display: none" /></div>
		
		
		</div>		
	
		<ul id="tool-bar" class="tool-bar-bottom" style="float:right;">
			<li style="float: right;"><a href="javascript:submitHL();"
				class="green-ui-btn"><span>Save</span></a></li>
			<li class="cancel-float-right"><a class="grey-ui-btn"
				href="javascript:$.fancybox.close()"><span>Cancel</span></a></li>
		</ul>
		

</s:form> <!-- CODE_END MIL - PN -->
</div>
</div>
