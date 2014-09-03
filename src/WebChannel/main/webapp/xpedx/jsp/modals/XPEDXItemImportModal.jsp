<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%@ page import="com.sterlingcommerce.xpedx.webchannel.common.DownloadSampleServlet"  %>
<script type="text/javascript">
	function importFile(){
		document.formImport[0].submit();
	}
	
</script>




<style>
	.share-modal { width:640px!important; height:414px;}		 
</style>

<div style="display: none;">
	<div class="xpedx-light-box" id="dlgImportForm">
		<s:if test='%{XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems").size < 200}'>
			<h1><s:text name="MSG.SWC.ITEM.LISTIMPORT.ERROR.PGTITLE" /></h1>
			<s:form name="formImport" id="formImport" action="MyItemsDetailsImportPrepare" method="post" enctype="multipart/form-data">
				<span id="errorMsgForRequiredField" style="display:none;"></span> 
				<%--  Don't delete this 'errorMsgForRequiredField' tag it needed while close button js --%>
				<div>
					<p class="addmargintop0">Items can be imported from a file into new or existing My Items Lists. Files must be saved as a comma separated value (CSV) file. </p>
					<p>A sample file is provided. Delete the item contents and enter your own data.</p>
					
					<input class="btn-neutral floatleft addmargintop10" type="submit" value="Download Sample" onclick="window.location.href='/swc/downloadsample'; return false;" />
		
					<p>&nbsp; Please note that an individual My Items List cannot exceed 200 items. </p>
					<p>To import the file, locate the file on your hard drive by using the 'Browse' button or by typing the file path in the box below. After the file is located, click 'Open' and then click the 'Import' button. </p>
					<br />
					<s:file name="upload" size="50" id="File"/>
			
					<center>
						<div id="errorMsgForBrowsePath" class="error error-msg-btm" style="display: none; width:120px;" >
							<%-- Please enter file path --%>
							<s:text name="MSG.SWC.ITEM.LISTIMPORT.ERROR.FILEPATH" />
						</div>
					</center>
					
					<p>
						Items are imported in the same order they are listed in the file, and added to the bottom of the My Items List.
					</p>
					
					<s:hidden name="listKey" value="%{listKey}" />
					<s:hidden name="editMode" value="%{true}" />
					<s:hidden name="listDesc" value="%{listDesc}" />
					<s:set name="itemCount" value='XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems").size' />
					<s:hidden name="itemCount" value="%{#itemCount}" />
					<s:hidden name="sharePermissionLevel" value="%{sharePermissionLevel}" />
					<s:hidden name="shareAdminOnly" value="%{shareAdminOnly}" />
					<s:hidden name="listOwner" value="%{listOwner}" />
					<s:hidden name="listCustomerId" value="%{listCustomerId}" />
					<s:hidden name="listName" value="%{listName}" />
					
					<script type="text/javascript">
						function submitImport() {
							submitImportForm("formImport");
						}
					</script>
			
					<div class="button-container addpadtop15">
						<input class="btn-gradient floatright addmarginright10" type="submit" value="Import" onclick="submitImport(); return false;" />
						<input class="btn-neutral floatright addmarginright10" type="submit" value="Cancel" onclick="$.fancybox.close(); return false;" />
					</div>
				</div>
			</s:form>
		</s:if>
		<s:else> <%-- XPEDXMyItemsItems.size >= 200 --%>
			<h3>Warning</h3>
			<br>
			<s:form>
				<%-- Maximum number of element in a list can only be 200. --%>
				<s:text name="MSG.SWC.CART.ADDTOCART.ERROR.QTYGT200" />
				<br/>
				Please try again with removing some items or create a new list.
				<br/>
				<br/>
				<input type="button" value="Cancel" onclick="$.fancybox.close(); return false;" />
				
				<center>
					<div id="errorMsgForRequiredField" class="error" style="display: none;" ></div>
				</center>
			</s:form>
		</s:else>
	</div> <%-- / dlgImportForm --%>
</div> <%-- / hidden div --%>
