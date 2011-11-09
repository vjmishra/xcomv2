<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>


<style>
.share-modal {
	
}

.indent-tree {
	margin-left: 15px;
}

.indent-tree-act {
	margin-left: 25px;
}
</style>

<div style="display: none">
<div id="dlgShareList" class="share-modal xpedx-light-box"  style="width:740px!important;height:385px">
	<h2>Share My Items List</h2>
	<p>Name</p>
	<div class="clear"></div>	
	<span style="width: 421px; float: left;" class="grey"><s:property value="listName" /></span>   
	<p>Description</p>
	<div class="clear"></div>
	
	<span style="width: 100%; float: left;" class="grey"><s:property value="listDesc" /></span>   
	<div class="clear"></div>

<!-- CODE_START MIL - PN --> 

	<s:form id="XPEDXMyItemsDetailsChangeShareList"
			action="XPEDXMyItemsDetailsChangeShareList" method="post"
			enctype="multipart/form-data" >
    
			<s:hidden name="listKey" value="%{listKey}"></s:hidden>
			<s:hidden name="listName" value="%{listName}"></s:hidden>
			<s:hidden name="listDesc" value="%{listDesc}"></s:hidden>
			<s:hidden name="editMode" value="%{editMode}"></s:hidden>
			<s:hidden name="itemCount" value="%{itemCount}"></s:hidden>
			
			<s:set name="rbPermissionShared" value="%{''}" />
			<s:set name="rbPermissionPrivate" value="%{''}" />

	<s:if test="%{#isUserAdmin}">
		<s:set name="rbPermissionShared" value="%{' checked '}" />
	</s:if>
	<s:else>
		<s:set name="rbPermissionPrivate" value="%{' checked '}" />
	</s:else>
	<s:if test="getSharePrivateField() != '' && getSharePrivateField() != null" >
		<s:set name="rbPermissionShared" value="%{''}" />
		<s:set name="rbPermissionPrivate" value="%{' checked '}" />
	</s:if>
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
	<p>
		List Type:
		<input onchange=""
			id="rbPermissionPrivate" <s:property value="#rbPermissionPrivate"/>
			type="radio" name="sharePermissionLevel"
			value="<s:property value="wCContext.loggedInUserId"/>"
			onclick="hideSharedListForm()" />&nbsp;Personal (only you will be able to view and edit this list)  
		</p>
		<s:if test="%{!#isUserAdmin}">
			<div style="display: none;">
		</s:if>
		
		<input style="margin-left:53px; margin-top:5px; margin-bottom:10px;" 
			onchange="" id="rbPermissionShared"
			<s:property value="#rbPermissionShared"/> 
			type="radio" name="sharePermissionLevel" value=" "
			onclick="showSharedListForm()" />&nbsp;Shared &nbsp;&nbsp;&nbsp;
		
		<s:if test="%{!#isUserAdmin}">
			</div>
		</s:if>
	
		<s:set name="displayStyle" value="%{''}" />
	
		<s:if test="%{(!#isUserAdmin) || (getSharePrivateField() != '' && getSharePrivateField() != null)}">
			<s:set name="displayStyle" value="%{'display: none;'}"/>
		</s:if>
		
		<span style="<s:property value="#displayStyle"/>" id="shareAdminOnlyShared" >
		<input type="checkbox" <s:property value="#saCV"/>
			name="shareAdminOnly" id="shareAdminOnly" value="Y" /> Edit by Admin users only<br>
		</span>
			
	<div style="<s:property value="#displayStyle"/>" id="dynamiccontent" >
	<!-- Placeholder for the dynamic content -->
	
	<s:div id="dlgShareListShared">	
		<div id="divMainShareList" class="grey-msg x-corners" style="height:116px; width:722px; overflow:auto;">
			<div id="itemZCountId"></div>
	
			<script type="text/javascript">
				function shareSelectAll(checked_status){
					//var checked_status = this.checked;
					var checkboxes = Ext.query('input[name*=customerPaths]');
					Ext.each(checkboxes, function(obj_item){
					obj_item.checked = !checked_status;
					obj_item.click();
					//obj_item.fireEvent('click');
					});
				}
			</script>
		</div>	
	</s:div>
	</div>
	
	<SCRIPT type="text/javascript">
		/*if ("<s:property value="rbPermissionPrivate"/>" != ""){
			Ext.get('dlgShareListShared').hide();
		}*/
		
		function submitSL(){
			Ext.get('XPEDXMyItemsDetailsChangeShareList').dom.submit();
		} 
	</SCRIPT>

	
	</br> <div class="clear"></div>
	<ul id="tool-bar" class="tool-bar-bottom">
		<li style="float: right;"><a class="green-ui-btn" href="javascript:submitSL();"> 
			<span>Save</span></a></li>		
		<li class="cancel-float-right"><a class="grey-ui-btn" href="javascript:$.fancybox.close()"><span>Cancel</span></a></li>	
	</ul>
</s:form> <!-- CODE_END MIL - PN -->
</div>
</div>