<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>


<style>
	.share-modal-forLocations { width:722px!important; height:210px !important;}
	.indent-tree { margin-left:15px; }
	.indent-tree-act { margin-left:25px; }
	 
	.xpedx-light-box h2 {
		border-bottom: 1px solid #CCCCCC;
		color: #002F75;
		font-size: 16px;
		font-weight: normal;
		margin: 0 0 0px;
		padding: 0 0 0px;
	}
</style>

<div style="display: none;">
	<div id="dlgSelectedList" class="xpedx-light-box">
		<h1 id="smilTitle_2"><s:text name='MSG.SWC.MIL.MILLOC.GENERIC.DLGTITLE'/></h1>
		
		<div class="clear"></div>
		
		<s:form id="XPEDXMyItemsSelectedList" name="XPEDXMyItemsSelectedList" action="MyItemsList.action" method="post" enctype="multipart/form-data">
			<s:hidden name="itemCount" value="%{0}"></s:hidden>
			<s:hidden id="clFromListId" name="clFromListId" value=""></s:hidden>
			<s:hidden id="clFromListIdTemp" name="clFromListIdTemp" value=""/>
			<s:hidden name="filterBySelectedListChk" value="%{#_action.getFilterBySelectedListChk()}"/>
			<s:hidden name="filterByMyListChk" value="%{#_action.getFilterByMyListChk()}"/>
			<s:hidden name="filterByAllChk" value="%{#_action.getFilterByAllChk()}"/>
			<s:hidden name="deleteClicked" value="%{false}"/>
			<s:if test="%{!#isUserAdmin}">
				<s:hidden name="customerIds" value="" />
			</s:if>
			
			
			<s:iterator id="item" value='savedSharedList'>
				<s:set name='customerID' value='#item.getAttribute("CustomerID")' />
				<s:set name='customerPath' value='#item.getAttribute("CustomerPath")' />
		
				<s:hidden name="sslNames" value="%{#customerID}"></s:hidden>
				<s:hidden name="sslValues" value="%{#customerPath}"></s:hidden>
			</s:iterator>
		
			<div id="dynamicContentInSelectedList" >
			<s:div id="dlgSelectedListShared">
				<br />
				
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
				
				<s:div id="divMainSelectedList" cssClass="grey-msg x-corners" cssStyle="height:110px; width:715px; overflow:auto;">
					<%-- CONTENT WILL GO HERE --%>
				</s:div>
			</s:div>
			</div>
			
			<script type="text/javascript">
				function isLocationSelected( ){
					var checked_status = false;
					//adding the already seleted as hidden checkboxes to the form 
					createHiddenInputsForSelectedCustomers('XPEDXMyItemsSelectedList');
					clearTheArrays();// clearing the arrays
					var checkboxes = Ext.query('input[name*=customerPaths]');
					Ext.each(checkboxes, function(obj_item){
						if (obj_item.checked == true)
							checked_status = true;
				
					});
					return checked_status; 
				}
				
				function submitSelectedList(){
					
					var isLocSelected  = isLocationSelected( )
				
					if( isLocSelected == true) {
						submitNewSelectedlist();
						
					} else {
						var  validationErrorMsg="<s:text name='MSG.SWC.MIL.MILLOC.ERROR.NOLOCATION' />";
						document.getElementById("formFieldValidationErrorMsg").innerHTML = validationErrorMsg;
						document.getElementById("formFieldValidationErrorMsg").style.display = "inline";
					}
				}
			</script>
		</s:form> 
		<div class="clear"></div>
		<div class="fFVVEM_wrap">
			<div style="display: none;" class="error" id="formFieldValidationErrorMsg"></div>
		</div>
		
		<div class="addpadtop5">
			Click on "+" to expand the account hierarchy or "-" to collapse.
			<br/>
			Selecting an account or location will display all lists authorized for that location.
		</div>
		<div class="button-container">
			<input class="btn-gradient floatright addmarginright5" type="submit" value="Apply" onclick="submitSelectedList(); return false;" />
			<input class="btn-neutral floatright addmarginright10" type="submit" value="Cancel" onclick="$.fancybox.close(); resetclFromSelectedListId(); return false;" />
		</div>
		<div class="clearfix"></div>
		
	</div> <%-- / dlgSelectedList --%>
</div> <%-- / hidden div --%>
