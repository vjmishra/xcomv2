<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<script type="text/javascript">
	function importFile(){
		document.formImport[0].submit();
		//alert("testing import modal")
		
		//var form = document.getElementById("formItemIds");
		
	}
</script>

<style>
.share-modal { width:640px!important; height:414px;}         
</style>

<div style="display: none;">

<div class="xpedx-light-box" id="dlgImportForm">
	<s:if test='%{XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems").size<200}'>
		<%-- <h2>Import My Items List</h2> --%>
		<h2> <s:text name="MSG.SWC.ITEM.LISTIMPORT.ERROR.PGTITLE" /> </h2>
		
		<s:form name="formImport" id="formImport" action="XPEDXMyItemsDetailsImportPrepare" method="post"
			enctype="multipart/form-data">
			
		  <span id="errorMsgForRequiredField" style="display:none;"></span> 
		  <%--  Don't delete this 'errorMsgForRequiredField' tag it needed while close button js --%>
		<div>
            <p>The import feature allows you to import a list of items into your My Items Lists. Lists can be imported as a new list or added to an existing My Items List. In order for the list to be imported, it first must be saved as a Comma Separated Values (CSV) file. </p>
            <p>Use the sample below as a template. Delete the contents and enter your own data.</p>
            <p><strong>Please note:</strong> An individual My Items List cannot exceed 200 items. </p>
            <ul id="tool-bar" class="tool-bar-bottom">
                <li><a class="grey-ui-btn" href="../sample/SampleImport.csv"><span>Download Sample</span></a></li>

            </ul>
            <p>To import the file, locate the file on your hard drive by using the 'Browse' button or by typing the file path in the box below. After the file is located, click 'Open' and then click the 'Import' button. </p>
            <br />
            
            
			<s:file name="upload" size="50" id="File"/>
	
						<center><div id="errorMsgForBrowsePath" class="error error-msg-btm" style="display: none; width:120px;" >
					<!-- Please enter file path -->
					<s:text name="MSG.SWC.ITEM.LISTIMPORT.ERROR.FILEPATH" />
				</div></center>
			<p>Items are imported in the same order they are listed in the file, and added to the bottom of the My Items List.</p>
        	
			<s:hidden name="listKey" value="%{listKey}"></s:hidden>
			<s:hidden name="listDesc" value="%{listDesc}"></s:hidden>
			<s:set name="itemCount" value='XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems").size'></s:set>
			<s:hidden name="itemCount" value="%{#itemCount}"></s:hidden>
			<s:hidden name="sharePermissionLevel" value="%{sharePermissionLevel}"></s:hidden>
			<s:hidden name="shareAdminOnly" value="%{shareAdminOnly}"></s:hidden>
			<s:hidden name="listOwner" value="%{listOwner}"></s:hidden>
			<s:hidden name="listCustomerId" value="%{listCustomerId}"></s:hidden>
			<s:hidden name="listName" value="%{listName}"></s:hidden>
			
			<script type="text/javascript">
		
			function submitImport() {
				submitImportForm("formImport");
			}
			</script>
			
	
	
		   <ul id="tool-bar" class="tool-bar-bottom">
           		<li style="float: right;"><a href="javascript:submitImport();" class="green-ui-btn"><span>Import</span></a></li>
        		<li style="float: right; margin-right:5px;"><a href="javascript:$.fancybox.close()" class="grey-ui-btn"><span>Cancel</span></a></li>
        	</ul>
		
		
		</div>
		</s:form>
		
	</s:if>
	<s:else>
		<h3>Warning</h3>
		<br>
		<s:form >
			<!-- Maximum number of element in a list can only be 200. -->
			<s:text name="MSG.SWC.CART.ADDTOCART.ERROR.QTYGT200" />
			<br>
			Please try again with removing some items or create a new list.
			<br>
			<br>
			<input type="button" value="Cancel" onclick="javascript:$.fancybox.close()" />
			
			<center><div id="errorMsgForRequiredField" class="error" style="display: none;" ></div></center>
			<!-- div for he errors are Required as there are JS issues on close -->
		</s:form>
	</s:else>
	</div>
</div>