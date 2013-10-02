<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>


<style>
.share-modal-forLocations { width:800px!important; height:210px !important;}         
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
 <div id="dlgSelectedList" class="share-modal-forLocations xpedx-light-box" style="width:722px; height:210px;">
<!-- <h2 id="smilTitle_2">Select My Items for Location(s)</h2> -->
<h2 id="smilTitle_2"><s:text name='MSG.SWC.MIL.MILLOC.GENERIC.DLGTITLE' /></h2>
  
<div class="clear"></div>


<s:form id="XPEDXMyItemsSelectedList" name="XPEDXMyItemsSelectedList" action="MyItemsList.action"
	method="post" enctype="multipart/form-data">

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
	
	
	<!-- START - Saved hidden data Fields -->
	<s:iterator id="item" value='savedSharedList'>
		<s:set name='customerID' value='#item.getAttribute("CustomerID")' />
		<s:set name='customerPath' value='#item.getAttribute("CustomerPath")' />

		<s:hidden name="sslNames" value="%{#customerID}"></s:hidden>
		<s:hidden name="sslValues" value="%{#customerPath}"></s:hidden>
	</s:iterator>
	<!-- END - Saved hidden data Fields -->

	<div id="dynamicContentInSelectedList" >
	<!-- Placeholder for the dynamic content -->
	<s:div id="dlgSelectedListShared" >
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
		<!-- START - BODY OF SHARE FORM -->
		<s:div id="divMainSelectedList" cssClass="grey-msg x-corners" cssStyle="height:110px; width:715px; overflow:auto;">
			<!-- CONTENT WILL GO HERE -->
		</s:div>
		<!-- END - BODY OF SHARE FORM -->
	</s:div>
	</div>

	<SCRIPT type="text/javascript">
					/*if ("<s:property value="rbPermissionPrivate"/>" != ""){
						hideForm('ShareListShared');
					}*/
					
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
		                return checked_status ; 
		            }
					
					
					function submitSelectedList(){
						
						var isLocSelected  = isLocationSelected( )
			        
						if( isLocSelected == true)
			            	submitNewSelectedlist();
			            else
			            {    
			               // var  validationErrorMsg="Please select at least one location.";
			                var  validationErrorMsg="<s:text name='MSG.SWC.MIL.MILLOC.ERROR.NOLOCATION' />";
			                //alert(validationErrorMsg);
			                document.getElementById("formFieldValidationErrorMsg").innerHTML = validationErrorMsg;
			                document.getElementById("formFieldValidationErrorMsg").style.display = "inline";
			              }
					} 
	</SCRIPT>
</s:form> 
	<div class="clear"></div>
	<div class="fFVVEM_wrap"><div style="display: none;" class="error" id="formFieldValidationErrorMsg"></div></div>
	<ul id="tool-bar" class="tool-bar-bottom">
	Click on "+" to expand the account hierarchy or "-" to collapse.</br>
    Selecting an account or location will display all lists authorized for that location.
	<li style="float: right;"><a class="green-ui-btn" href="javascript:submitSelectedList();"> <span>Apply</span> </a></li>
		<li  class="cancel-float-right"><a class="grey-ui-btn" href="javascript:$.fancybox.close(); resetclFromSelectedListId() ;"><span>Cancel</span></a></li>
	</ul>
</div>
</div>
