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
	
		<!-- <h2>Import My Items List</h2> -->
		<h2> <s:text name="MSG.SWC.ITEM.LISTIMPORT.ERROR.PGTITLE" /> </h2>
	<div  id="importItems">
		<s:form name="formImport" id="formImport" action="XPEDXMyItemsDetailsImportPrepare" method="post"
			enctype="multipart/form-data">
		  
		<div id="errorMsgForRequiredField" class="error" style="display: none;" >
		</div> 
		
		<div>
            <p>Items can be imported from a file into new or existing My Items Lists. Files must be saved as a comma separated value (CSV) file. A sample file is provided. Delete the item contents and enter your own data. </p>
            <ul id="tool-bar" class="tool-bar-bottom">
                <li><a class="grey-ui-btn" href="../sample/SampleImport.csv"><span>Download Sample</span></a></li>

            </ul>
            <p>Please note that an individual My Items List cannot exceed 200 items. </p>
            <p>To import the file, locate the file on your hard drive by using the 'Browse' button or by typing the file path in the box below. After the file is located, click 'Open' and then click the 'Import' button. </p>
            <br />
            
            
			<s:file name="upload" size="50" id="File"/>
			<!-- 
			<div id="errorMsgForBrowsePath" class="error" style="display: none;" >
			Required Field
			</div> -->
						<center><div id="errorMsgForBrowsePath" class="error error-msg-btm" style="display: none;" >
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
		
	</div>
	<div id="importWarning">
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
			<!--  <div id="errorMsgForBrowsePath" class="error" style="display: none;" ></div> -->
			<center><div id="errorMsgForRequiredField" class="error" style="display: none;" ></div></center>
			<!-- div for he errors are Required as there are JS issues on close -->
		</s:form>
	</div>
	</div>
</div>