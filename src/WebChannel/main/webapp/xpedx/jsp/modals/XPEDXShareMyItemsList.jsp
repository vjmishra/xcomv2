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
	<div id="dlgShareList" class="share-modal xpedx-light-box"  style="width:740px!important; height:385px">
		<h2>Share My Items List</h2>
		<p>Name</p>
		<div class="clear"></div>	
		<span style="width: 421px; float: left;" class="grey"><s:property value="listName" /></span>
		<p>Description</p>
		<div class="clear"></div>
		
		<span style="width: 100%; float: left;" class="grey"><s:property value="listDesc" /></span>
		<div class="clear"></div>
	
		<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
		<s:set name="isEstUser" value='%{#xpedxCustomerContactInfoBean.isEstimator()}' />
		<s:form id="XPEDXMyItemsDetailsChangeShareList" action="MyItemsDetailsChangeShareList" method="post" enctype="multipart/form-data" >
			<s:hidden name="listKey" value="%{listKey}"></s:hidden>
			<s:hidden name="listName" value="%{listName}"></s:hidden>
			<s:hidden name="listDesc" value="%{listDesc}"></s:hidden>
			<s:hidden name="editMode" value="%{editMode}"></s:hidden>
			<s:hidden name="itemCount" value="%{itemCount}"></s:hidden>
			<s:hidden name="shareAdminOnly1" value="%{shareAdminOnly}"></s:hidden>
			<s:hidden name="sharePrivateFlag" value="%{getSharePrivateField().trim()}"></s:hidden>
		
			
			<s:set name="rbPermissionShared" value="%{''}" />
			<s:set name="rbPermissionPrivate" value="%{''}" />
	
			<s:if test="%{(#isUserAdmin) || (#isEstUser) }">
				<s:set name="rbPermissionShared" value="%{' checked '}" />
			</s:if>
			<s:else>
				<s:set name="rbPermissionPrivate" value="%{' checked '}" />
			</s:else>
			
			
			<s:if test='%{getSharePrivateField()!= null && getSharePrivateField().trim() != ""}'>
				<s:hidden name="test11" value="%{getSharePrivateField().trim()}"></s:hidden>
				<s:set name="rbPermissionShared" value="%{''}" />
				<s:set name="rbPermissionPrivate" value="%{' checked '}" />
			</s:if>
			<s:else>
				<s:set name="rbPermissionPrivate" value="" />
				<s:set name="rbPermissionShared" value="%{' checked '}" />
			</s:else>
			
			<s:set name="saCV" value="%{''}" />
			<s:if test='%{shareAdminOnly == "Y"}'>
				<s:set name="saCV" value="%{' checked '}" />
			</s:if>
			<s:if test='%{shareAdminOnly1 == "Y"}'>
				<s:set name="saCV" value="%{' checked '}" />
			</s:if>
		
			<%-- START - Saved hidden data Fields --%>
			<s:iterator id="item" value='savedSharedList'>
				<s:set name='customerID' value='#item.getAttribute("CustomerID")' />
				<s:set name='customerPath' value='#item.getAttribute("CustomerPath")' />
		
				<s:hidden name="sslNames" value="%{#customerID}"></s:hidden>
				<s:hidden name="sslValues" value="%{#customerPath}"></s:hidden>
			</s:iterator>
			<%-- END - Saved hidden data Fields --%>
		
			<p>
				List Type:
				<input id="rbPermissionPrivate" <s:property value="#rbPermissionPrivate"/>
						type="radio" name="sharePermissionLevel"
						value="<s:property value="wCContext.loggedInUserId"/>"
						onclick="hideSharedListForm()"
						/>&nbsp;Personal (only you will be able to view and edit this list)  
			</p>
				
			<s:if test="%{(!#isEstUser) && (!#isUserAdmin)}">			
				<div style="display: none;">
					<input style="margin-left:53px; margin-top:5px; margin-bottom:10px;" 
							onchange="" id="rbPermissionShared"
							<s:property value="#rbPermissionShared"/>
							type="radio" name="sharePermissionLevel" value=" "
							onclick="showSharedListForm()"
							/>&nbsp;Shared &nbsp;&nbsp;&nbsp;
				</div>
			</s:if>	
			<s:else> <%-- is admin or est user: show extra options --%>
				<input style="margin-left:53px; margin-top:5px; margin-bottom:10px;" 
						onchange="" id="rbPermissionShared"
						<s:property value="#rbPermissionShared"/>
						type="radio" name="sharePermissionLevel" value=" "
						onclick="showSharedListForm()"
						/>&nbsp;Shared &nbsp;&nbsp;&nbsp;
			</s:else>
			<s:set name="displayStyle" value="%{''}" />
		
			<s:if test="%{((!#isUserAdmin) && (!#isEstUser)) || (getSharePrivateField() != '' && getSharePrivateField() != null)}">
				<s:set name="displayStyle" value="%{'display: none;'}"/>
			</s:if>
			
			<s:if test="%{#isUserAdmin }">
				<span style="<s:property value="#displayStyle"/>" id="shareAdminOnlyShared">
					<input type="checkbox" <s:property value="#saCV"/>
							name="shareAdminOnly" id="shareAdminOnly" value="Y" onchange="javascript:setAdminFlag(this);"/>
					Edit by Admin users only
					<br/>
				</span>
			</s:if>
			<s:else>
				<span style="<s:property value="#displayStyle"/>" id="shareAdminOnlyShared" >
					<input type="hidden" 
							name="shareAdminOnly" id="shareAdminOnly" value="" />
				</span>
			</s:else>
			
			<div style="<s:property value="#displayStyle"/>" id="dynamiccontent">
				<%-- Placeholder for the dynamic content --%>
				<s:div id="dlgShareListShared">	
					<div id="divMainShareList" class="grey-msg x-corners" style="height:116px; width:722px; overflow:auto;">
						<div id="itemZCountId"></div>
				
						<script type="text/javascript">
							function shareSelectAll(checked_status){
								var checkboxes = Ext.query('input[name*=customerPaths]');
								Ext.each(checkboxes, function(obj_item){
									obj_item.checked = !checked_status;
									obj_item.click();
								});
							}
						</script>
					</div>	
				</s:div>
			</div> <%-- / dynamiccontent --%>
			
			<script type="text/javascript">
				function setAdminFlag(obj){
					if(obj.checked == true)
						obj.value="Y";
					else
						obj.value="N";
					return;
				}
				
				function submitSL(){
					var customerPaths=document.getElementsByName("customerPaths");
					var custID;
					for(var i=0;i<customerPaths.length;i++)
					{
						var customerPath=customerPaths[i].value;
						var isSelected=customerPaths[i].checked;
						if(isSelected)
						{
							if(customerPath != null)
							{
								var custPath = customerPath.split("|");
								var custPathlen = custPath.length;
								if(custPathlen > 1)
								{
									custID = custPath[custPath.length-1];
									custID = custID.replace(/-/g, '_');
								}
								else
								{
									custID = custPath[0];
									custID = custID.replace(/-/g, '_');
								}
								selectNode(custID, true);
							}
						}
					}
					
					Ext.get('XPEDXMyItemsDetailsChangeShareList').dom.submit();
				}
			</script>
		
			
			<div class="button-container addpadtop15">
				<input class="btn-gradient floatright addmarginright10" type="submit" value="Save" onclick="submitSL(); return false;" />
				<input class="btn-neutral floatright addmarginright10" type="submit" value="Cancel" onclick="$.fancybox.close(); return false;" />
			</div>
		</s:form>
	</div> <%-- / dlgShareList --%>
</div> <%-- / hidden div --%>
