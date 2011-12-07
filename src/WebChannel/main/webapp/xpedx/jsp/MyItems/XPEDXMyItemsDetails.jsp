<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>

<s:set name="CurrentCustomerId"
	value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />
<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
<s:set name='currentCartInContextOHK' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("OrderHeaderInContext")'/>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean" id="xpedxUtilBean" />

<%-- 
<s:if test="#currentCartInContextOHK == null ">
	<s:set name='currentCartInContextOHK' value='@com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper@getCartInContextOrderHeaderKey(wCContext)'/>
</s:if>
--%>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils' id='xpedxSCXmlUtil' />
<s:set name='outDoc2' value='%{outDoc.documentElement}' />
<script type="text/javascript">
	var isUserAdmin = <s:property value="#isUserAdmin"/>;
</script>

<!-- Version 1.1 Updated 8-18-10 -->
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/ext-all.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/swc.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/catalog/my-price-and-availability.css" />

<s:include value="../common/XPEDXStaticInclude.jsp"/>

<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-quick-add.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/theme-xpedx_v1.2.css"/>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="/swc/xpedx/js/global/ext-base.js"></script>

<script type="text/javascript" src="/swc/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/catalog/catalogExt.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/swc.js"></script>
<!-- Web Trends tag start -->
<script type="text/javascript" src="/swc/xpedx/js/webtrends/displayWebTag.js"></script>
<!-- Web Trends tag end  -->

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<!-- carousel scripts css  -->
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/js/jcarousel/skins/xpedx/theme.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/js/jcarousel/skins/xpedx/skin.css" />
<!-- carousel scripts js   -->
<script type="text/javascript" src="/swc/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/modals/checkboxtree/demo.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/modals/checkboxtree/jquery.checkboxtree.css"/>
<script type="text/javascript" src="/swc/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>

<script type="text/javascript" src="/swc/xpedx/css/modals/checkboxtree/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/quick-add/jquery.form.js"></script>
<!-- script type="text/javascript" src="/swc/xpedx/js/quick-add/quick-add.js"></script -->

<script type="text/javascript" src="/swc/xpedx/js/jqdialog/jqdialog.js"></script>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/js/jqdialog/jqdialog.css" />

<script type="text/javascript" src="/swc/xpedx/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/pseudofocus.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global-xpedx-functions.js"></script>

<!-- Lightbox/Modal Window --> 
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>
<link rel="stylesheet" type="text/css" href="/swc/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

<!-- Page Calls -->
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/my-items.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/catalog/narrowBy.css" />

<!-- page specific JS includes -->
<script type="text/javascript" src="<s:url value='/swc/js/MyItems/XPEDXMyItemsDetails.js'/>"></script>
<script type="text/javascript" src="<s:url value='/swc/js/MyItems/XPEDXAddToCartAndAvailability.js'/>"></script>
<script type="text/javascript" src="<s:url value='/swc/js/common/XPEDXUtils.js'/>"></script>

<script type="text/javascript" src="/swc/xpedx/js/common/ajaxValidation.js"></script>
<s:set name="customerPONoFlag" value='%{customerFieldsMap.get("CustomerPONo")}'></s:set>
<s:set name="jobIdFlag" value='%{customerFieldsMap.get("CustLineAccNo")}'></s:set>

<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/mil-quick-add-large.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/theme-xpedx_v1.2.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" />.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/banner.css"/>

<!-- For the number formatting -->
<script type="text/javascript"
	src="../js/jquery.numberformatter-1.1.0.js"></script>
<script type="text/javascript" src="../js/jquery.numeric.js"></script>
<script type="text/javascript">
function maxLength(field,maxlimit) {
	if (field.value.length > maxlimit) // if too long...trim it!
		alert(field.title + ' should not exceed '+maxlimit+ ' characters');
		field.value = field.value.substring(0, maxlimit);
		return false;
	}

function hideSharedListForm(){
	document.getElementById("dynamiccontent").style.display = "none";
	if(document.getElementById("shareAdminOnlyShared")!=null)
	document.getElementById("shareAdminOnlyShared").style.display = "none";
}
	
function showSharedListForm(){
	var dlgForm 		= document.getElementById("dynamiccontent");
	if (dlgForm){
		dlgForm.style.display = "block";
		if(document.getElementById("shareAdminOnlyShared")!=null)
		document.getElementById("shareAdminOnlyShared").style.display = "";
	}
}
</script>
<s:if test='editMode == true'>
	<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.MIL.MYITEMLISTS.GENERIC.TABTITLE" /> / <s:text name="myitemsdetails.editable.title" /></title>
	<meta name="DCSext.w_x_list_edit" content="1" />
</s:if>
<s:else>
	<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.MIL.MYITEMLISTS.GENERIC.TABTITLE" /> / <s:text name="myitemsdetails.noneditable.title" /></title>
</s:else>

<!-- Web Trends tag start -->

<s:if test="%{updatePAMetaTag}">
<meta name="DCSext.w_x_sc" content="1" /> 
<meta name="DCSext.w_x_scr" content="<s:property value='%{strItemIds}'/>" />
</s:if>

<s:if test='%{#session.metatagName != null}'>		
	<meta name ="<s:property value='%{#session.metatagName}' />" content="<s:property value='%{#session.metatagValue}' />" />
	<s:set name="metatagName" value="<s:property value=null />" scope="session"/> 
	<s:set name="metatagValue" value="<s:property value=null />" scope="session"/>
</s:if>
<!-- Web Trends tag end -->

<!-- CODE_END - Global Vars -PN -->

<style type="text/css">
.dlgForm {
	display: none;
	border-color: black;
	border-width: 10px;
	padding: 20px;
	background-color: white;
	position: absolute;
	left: 25%;
	top: 40%;
	z-index: 9999;
	width: auto;
	height: 50%;
	overflow: auto;
}

.dlgFormShareList {
	display: none;
	border-color: black;
	border-width: 10px;
	padding: 20px;
	background-color: white;
	position: absolute;
	left: 25%;
	top: 40%;
	z-index: 9999;
	width: 400px;
	height: 200px;
	overflow: auto;
	border-style: solid;
	border-width: 1px;
	border-color: black;
}

#wwgrp_listOfCarts.wwgrp,#wwctrl_listOfCarts.wwctrl {
	display: inline;
}

.xpedx-light-box p {
}
</style>
<script type="text/javascript">
	var availabilityURL = '<s:property value="#availabilityURL"/>';
	var addToCartURL = '<s:property value="#addToCartURL"/>';

	function hideSharedListFormIfPrivate() {
		var radioBtns = document.XPEDXMyItemsDetailsChangeShareList.sharePermissionLevel;
		var div = document.getElementById("dynamiccontent");
		if(radioBtns[0].checked) {
			div.style.display = "none";
		}
	}
</script>
<script type="text/javascript">

	var selectedItemId;
	var sharePrivateVar = '<s:property value="getSharePrivateField()" />';
		
	$(document).ready(function() {
		$("#varous1").fancybox();
		$("#various2").fancybox({
			'autoDimensions'	: false,
			'width' 			: 440,
			'height' 			: 340  
		});
		
		$("#dlgShareListLink1").fancybox({
			'onStart' 	: function(){
			if (isUserAdmin){
				//Calling AJAX function to fetch 'Ship-To' locations only when user is an Admin
				showShareList('<s:property value="#CurrentCustomerId"/>', true);
				hideSharedListFormIfPrivate();
				}
			},
			'onClosed':function() {
				document.XPEDXMyItemsDetailsChangeShareList.shareAdminOnly.checked=false;
				var radioBtns = document.XPEDXMyItemsDetailsChangeShareList.sharePermissionLevel;
				var div = document.getElementById("dynamiccontent");
				if(!isUserAdmin)
				{
					//Check Private radio button
					radioBtns[0].checked = true;
					//Hide Ship To Locations
					div.style.display = "none";
				}
				else
				{
					// share private variable will be populated if it is a private list
					//based on that, the private is selected in the pop up and the locations are not shown
					if(sharePrivateVar!=null && sharePrivateVar != "") {
						radioBtns[0].checked = true;
						//Hide Ship To Locations
						div.style.display = "none";
					}
					else {							
					//Check Shared radio button
					radioBtns[1].checked = true;
					//Display Ship To Locations
					div.style.display = "block";
					}
				}
				shareSelectAll(false);
			}
			
		});

		$("#dlgShareListLink2").fancybox({
			'onStart' 	: function(){
			if (isUserAdmin){
				//Calling AJAX function to fetch 'Ship-To' locations only when user is an Admin
				showShareList('<s:property value="#CurrentCustomerId"/>', true);
				hideSharedListFormIfPrivate();
				}
			},
			'onClosed':function() {
				document.XPEDXMyItemsDetailsChangeShareList.shareAdminOnly.checked=false;
				var radioBtns = document.XPEDXMyItemsDetailsChangeShareList.sharePermissionLevel;
				var div = document.getElementById("dynamiccontent");
				if(!isUserAdmin)
				{
					//Check Private radio button
					radioBtns[0].checked = true;
					//Hide Ship To Locations
					div.style.display = "none";
				}
				else
				{
					// share private variable will be populated if it is a private list
					//based on that, the private is selected in the pop up and the locations are not shown
					if(sharePrivateVar!=null && sharePrivateVar != "") {
						radioBtns[0].checked = true;
						//Hide Ship To Locations
						div.style.display = "none";
					}
					else {							
					//Check Shared radio button
					radioBtns[1].checked = true;
					//Display Ship To Locations
					div.style.display = "block";
					}
				}
				shareSelectAll(false);
			}
			
		});

		$("#various4").fancybox();
		$("#various5,#various5_1").fancybox({
			/*'autoDimensions'	: false,
			'width'				: 640,
			'height'			: 458*/
			'onClosed' : function(){
			document.getElementById("errorMsgForBrowsePath").style.display = "none";
			document.getElementById("errorMsgForRequiredField").innerHTML = "";
			document.getElementById("errorMsgForRequiredField").style.display = "none";
			if(document.getElementById("File"))
				document.getElementById("File").value = "";
			},
			'autoDimensions'	:false,
			'width'				: 620,
			'height'			: 465
		});
		
		$("#linkToReplacement").fancybox({
			'onStart' 	: function(){
				showShareList('<s:property value="#CurrentCustomerId"/>');
			},
			'autoDimensions'	: false,
			'width' 			: 760,
			'height' 			: 405  
		});
		$("#addToCartFancybox").fancybox({
			'onStart' 	: function(){			
				populateAddToCartDlg('<s:property value="#availabilityURL" escape="false"/>');
			},
			'onClosed' 	: function(){			
				clearAddToCartDiv();
			},
			'autoDimensions'	: false,
			'width' 			: 700,
			'height' 			: 600  
		});
		$("#bracketPricing").fancybox({
			'autoDimensions'	: false,
			'width' 			: '100',
			'height' 			: '100'  
		});
		
		/*
		$("#formItemIds_qtys").blur(function(){
		    $(this).format({format:"####", locale:"us"});
		});
		*/	
	});	
	

     //<!--
     <%
     /*
         $(document).ready(function() {
             $('#tree').checkboxTree();
             $('#collapseAllButtonsTree').checkboxTree({
                 collapseAllButton: 'Collapse all',
                 expandAllButton: 'Expand all'
             });
         });
     */
     %>
     //-->
     
     	function checkAvailability(itemId,myItemsKey) {
     		
     		clearPreviousDisplayMsg()
     		
     		if(itemId == null || itemId =="") {
    			alert("Item ID cannot be null to make a PnA call");
    		}
    		var url = document.getElementById("checkAvailabilityURLHidden");
    		if(url != null) {
        		var qty = document.getElementById('QTY_'+myItemsKey).value;
        		var uom = document.getElementById('UOM_'+myItemsKey).value;
    			displayAvailability(itemId,qty,uom,myItemsKey,url.value);
    		}        		
     	}
		
		function importItems(msgImportMyItemsError){
			
			
			//Clears previous messages if any
			clearPreviousDisplayMsg()
     		
			document.getElementById("errorMsgTop").innerHTML = msgImportMyItemsError ;
            document.getElementById("errorMsgTop").style.display = "inline"; 
            
            document.getElementById("errorMsgBottom").innerHTML = msgImportMyItemsError ;
            document.getElementById("errorMsgBottom").style.display = "inline"; 
		}
		
		function deleteItems(){
			
			//Clear previous messages if any
			clearPreviousDisplayMsg()
			
			var formItemKeys = document.getElementsByName("itemKeys");
			var isAnyItemSelected = false;
			for(var i = 0; i < formItemKeys.length; i++){
				if(formItemKeys[i].checked == true){
					isAnyItemSelected = true;
					break;
				}
			}

			if(isAnyItemSelected){
				var formItemIds = document.getElementById("formItemIds");

				<s:url id='deleteListLink' action='XPEDXMyItemsDetailsDelete.action' >
				</s:url>
				
						
				if (formItemIds){
					formItemIds.action = "<s:property value='%{deleteListLink}' escape='false'/>";
					 formItemIds.listName.value = Ext.get("listName").dom.value;
				     formItemIds.listDesc.value = Ext.get("listDesc").dom.value;
	
					formItemIds.submit();
				} else {
					alert("There is a problem in this page. Form formItemIds is missing.");
				}
			}
			else{
				
					
			  // var msgSelectItemFirst = "You have not selected any items to be deleted. Please select an item and try again";
			   var msgSelectItemFirst = "<s:text name='MSG.SWC.MIL.NOITEMSELECT.ERROR.SELECTFORDELETE' />";
				document.getElementById("errorMsgTop").innerHTML = msgSelectItemFirst ;
                document.getElementById("errorMsgTop").style.display = "inline";
            
				document.getElementById("errorMsgBottom").innerHTML = msgSelectItemFirst ;
                document.getElementById("errorMsgBottom").style.display = "inline";
            
				return;	
			}
		}
		
		
		
		//Resets the Messages and calls the actual javascript function
		function myUpdateSelectedPAA( ){
			
			//Clear previous messages if any
			clearPreviousDisplayMsg();
			javascript:updateSelectedPAA( );
		}
		
		

		
		
		//Resets the Messages and calls the actual javascript function
		function myAddItemToCart(itemId, id){
			
			//Clear previous messages if any
			clearPreviousDisplayMsg();
			javascript:addItemToCart(itemId, id );
		}
		
		function add2List(){
			var itemCount = "<s:property value='XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems").size()'/>";
			var itemCountNum = Number(itemCount);
			if(itemCount<=200){
				if((itemCountNum+document.getElementById('qaTable').rows.length)<=200){
					var formItemIds = document.getElementById("formAdd2List");
					if (formItemIds){
						//form already submitted.
						if (Ext.get("btnQLAdd2Cart").dom.disabled){
							return;
						}
					
						formItemIds.listName.value 	= Ext.get("listName").dom.value;
						formItemIds.listDesc.value 	= Ext.get("listDesc").dom.value;
						
						Ext.get("btnQLAdd2Cart").dom.disabled = true;
						formItemIds.submit();
						
					} else {
						alert("There is a problem in this page. Form formAdd2List is missing.");
					}
				}
				else{
					//alert("Your list may contain a maximum of 200 items. Please delete some items and try again.");
					alert( "<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGT200' /> ");
				}
			}
			else{
				//alert("Your list may contain a maximum of 200 items. Please delete some items and try again.");
				alert( "<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGT200' />");
				return false;
			}
			
		}

		String.prototype.trim = function() {
			return this.replace(/^\s+|\s+$/g,"");
		}
		
		function saveAllItemsNew(docDivId, ignoreDivIds){
			try{
				if (mandatoryFieldValidation(docDivId, ignoreDivIds) != "")
					return;
			}catch(err){
			}

			//Check for maxlength description
			if (document.getElementById("listDesc").value.length > 255){
				document.getElementById("mandatoryFieldCheckFlag_"+docDivId).value = "true";
				
					//var jasonErrorMess = "The description can contain a maximum of "+ 255 +" characters, please revise and try again.";
					var jasonErrorMess = "<s:text name='MSG.SWC.MIL.DESC.ERROR.LENGTHGT255' />";
					document.getElementById("errorMsgForMandatoryFields_"+docDivId).innerHTML = jasonErrorMess;
					document.getElementById("errorMsgForMandatoryFields_"+docDivId).style.display = "inline";
					return;
			}
			else{
				document.getElementById("errorMsgForMandatoryFields_"+docDivId).style.display = "none";
				document.getElementById("mandatoryFieldCheckFlag_"+docDivId).value = "false";
			}

			
			saveAllItems();
		}
		
		function saveAllItems(){
			
			var formItemIds = document.getElementById("formItemIds");
			
			<s:url id='saveAllLink' action='XPEDXMyItemsDetailsChange.action' escapeAmp='false' >
			</s:url>
			
			if (formItemIds){
				formItemIds.action = "<s:property value='%{saveAllLink}' escape='false'/>";
				formItemIds.editMode.value = "false";
				formItemIds.listName.value = Ext.get("listName").dom.value;
				formItemIds.listDesc.value = Ext.get("listDesc").dom.value;
				try{ console.log("Action: " + formItemIds.action); }catch(ee){}
				try{ console.log("Key: " 	+ formItemIds.listKey.value); }catch(ee){}
				try{ console.log("Desc: " 	+ formItemIds.listDesc.value); }catch(ee){}
				formItemIds.submit();
			} else {
				alert("There is a problem in this page. Form formItemIds is missing.");
			}
		}

		function listNameCheck(component, docDivId){
			var mandatoryFieldCheckFlag = document.getElementById("mandatoryFieldCheckFlag_"+docDivId).value;
			if(mandatoryFieldCheckFlag=="true"){
				if(component.value==""){
	            	component.style.borderColor="#FF0000";
	        	}
	        	else{
	            	component.style.borderColor="";
	    	    }
	    	}
	    }
	    
		
		/*function getShareList(customerId, divId, showRoot){
	           //Init vars
	           <s:url id='getShareList' includeParams='none' action='XPEDXMyItemsDetailsGetShareList' escapeAmp="false"/>
	           if (showRoot == null){ showRoot = false; }

	           var isCustomerSelected = false;
	           //Replace all '-' with '_'
	           var controlId = customerId.replace(/-/g, '_');
	           //Get the current checkbox selection of the customer
			   if(document.getElementById('customerPaths_'+controlId)!=null){
	               isCustomerSelected = document.getElementById('customerIds_'+controlId).checked;
			   }
	           
	           var url = "<s:property value='#getShareList'/>";
	           //url = ReplaceAll(url,"&amp;",'&');
	           
	           //Show the waiting box
	           var x = document.getElementById(divId);
	           x.innerHTML = "Loading data... please wait!";
	           
	           
	           //Execute the call
	           document.body.style.cursor = 'wait';
	           //alert("Customer id is: " + customerId + ", showRoot: " + showRoot);
	           if(true){
	                 Ext.Ajax.request({
	                   url: url,
	                   params: {
	                             customerId: customerId,
	                             showRoot: showRoot
	                   },
	                   method: 'POST',
	                   success: function (response, request){
	                       document.body.style.cursor = 'default';
	                       //alert('setAndExecuteForShareList');
	                       setAndExecuteForShareList(divId, response.responseText, isCustomerSelected);
	                       
	                   },
	                   failure: function (response, request){
	                       var x = document.getElementById(divId);
	                       x.innerHTML = "";
	                       alert('Failed to fecth the share list');
	                       document.body.style.cursor = 'default';                                                  
	                   }
	               });     
	           }
	       	document.body.style.cursor = 'default';
	
		}*/
		
		function getAvail(itemId, uom, divId){
			
				//Init vars
	           <s:url id='getPandA' includeParams='none' action='XPEDXMyItemsDetailsGetAvail' />
	           
	           var url = "<s:property value='#getPandA'/>";
	           url = ReplaceAll(url,"&amp;",'&');
	           //Show the waiting box
	           var x = document.getElementById(divId);
	           x.innerHTML = "Loading data...<br/> please wait!";
	           
	           
	           //Execute the call
	           document.body.style.cursor = 'wait';
	           //alert("Customer id is: " + customerId + ", showRoot: " + showRoot);
	           if(true){
	                 Ext.Ajax.request({
	                   url: url,
	                   params: {
	                             "itemId": itemId,
	                             "itemUom": uom
	                   },
	                   method: 'POST',
	                   success: function (response, request){
	                       document.body.style.cursor = 'default';
	                       setAndExecute(divId, response.responseText);
	                   },
	                   failure: function (response, request){
	                       var x = document.getElementById(divId);
	                       x.innerHTML = "";
	                       alert('Failed to update price and availability');
	                       document.body.style.cursor = 'default';                                                  
	                   }
	               });     
	           }
	       	document.body.style.cursor = 'default';
	
		}
				
						function getPrice(itemId, uom, divId){
	           //Init vars
	           <s:url id='getPandA' includeParams='none' action='XPEDXMyItemsDetailsGetPrice' />
	           
	           var url = "<s:property value='#getPandA'/>";
	           url = ReplaceAll(url,"&amp;",'&');
	           //Show the waiting box
	           var x = document.getElementById(divId);
	           x.innerHTML = "Loading data...<br/> please wait!";
	           
	           
	           //Execute the call
	           document.body.style.cursor = 'wait';
	           //alert("Customer id is: " + customerId + ", showRoot: " + showRoot);
	           if(true){
	                 Ext.Ajax.request({
	                   url: url,
	                   params: {
	                             "itemId": itemId,
	                             "itemUom": uom
	                   },
	                   method: 'POST',
	                   success: function (response, request){
	                       document.body.style.cursor = 'default';
	                       setAndExecute(divId, response.responseText);
	                   },
	                   failure: function (response, request){
	                       var x = document.getElementById(divId);
	                       x.innerHTML = "";
	                       alert('Failed to update price and availability');
	                       document.body.style.cursor = 'default';                                                  
	                   }
	               });     
	           }
	       	document.body.style.cursor = 'default';
	
		}
					
		function qaValidateItem(divId, jobId, qty, itemId, itemType, purchaseOrder, itemTypeText){
	           //Init vars
	           <s:url id='qaAddItem' includeParams='none' action='XPEDXMyItemsDetailsQuickAdd' />
	           var flag = false;
	           var jobIdFlag = false;
	           var jobIdStr = "<s:property value='%{customerFieldsMap.get("CustLineAccNo")}'/>";
	           if(jobIdStr != null && jobIdStr != "")
		           jobIdFlag = true;
	           var customPOFlagstr = "<s:property value='%{customerFieldsMap.get("CustomerPONo")}'/>";
	           
	           if(customPOFlagstr != null && customPOFlagstr == "Line PO #")
	           	flag = true;
	           var url = "<s:property value='#qaAddItem'/>";
	           url = ReplaceAll(url,"&amp;",'&');
	           //Show the waiting box
	           var x = document.getElementById(divId);
	           x.innerHTML = "Validating item " + itemId + "...please wait!";
	           
	           //Execute the call
	           document.body.style.cursor = 'wait';
	           //alert("Customer id is: " + customerId + ", showRoot: " + showRoot);
	           if(true){
	                 Ext.Ajax.request({
	                   url: url,
	                   params: {
		                   		"jobIdFlag":	  jobIdFlag,
	                             "jobId": 	  	  jobId,
	                             "qty": 	  	  qty,
	                             "itemId":   	  itemId,
	                             "itemType": 	  itemType,
	                             "itemTypeText": itemTypeText,
	                             "purchaseOrder": purchaseOrder,
	                             "customerPOFlag":		  flag,
	                             "command": 	  "validate_item"
	                   },
	                   method: 'POST',
	                   success: function (response, request){
	                       document.body.style.cursor = 'default';	                       
	                       setAndExecute(divId, response.responseText);
	                   },
	                   failure: function (response, request){
	                       var x = document.getElementById(divId);
	                       x.innerHTML = "";
	                       alert('Failed to add this item. Please try again later.');
	                       document.body.style.cursor = 'default';                                                  
	                   }
	               });     
	           }
	           
	       	document.body.style.cursor = 'default';
	
		}
	
		function addToCart(){

			clearPreviousDisplayMsg();
			
			resetQuantityError();
			 if(validateOrderMultiple(false,null) == false)
			 {
					return;
			 }
			var formItemIds 	= document.getElementById("formItemIds");
			//var selCart 		= document.getElementById("draftOrders");
			
			try{
			//selCart = selCart.options[selCart.selectedIndex].value;
			var selCart = '<s:property value="#currentCartInContextOHK" />';
			}catch(ee){
				selCart = "_CREATE_NEW_";
			}
			
		    <s:url id='addToCartLink' action='XPEDXMyItemsDetailsAddToCart.action'>
		    </s:url>
		
			if (formItemIds){
				formItemIds.action = "<s:property value='%{addToCartLink}' escape='false'/>";
				formItemIds.orderHeaderKey.value = selCart;
				//formItemIds.submit();
				//START - Submit the form via ajax
	            //Added For Jira 2903
				Ext.Msg.wait("Processing...");      
				//Ext.Msg.wait("Adding items to cart...Please wait!");
	                     //xpedx_working_start();
                         //setTimeout(xpedx_working_stop, 3000);
	                 Ext.Ajax.request({
	                   url: '<s:property value='%{addToCartLink}' escape='false'/>',
	                   form: 'formItemIds',
	                   method: 'POST',
	                   success: function (response, request){
	                      Ext.MessageBox.hide();
	                      refreshMiniCartLink();
	                   },
	                   failure: function (response, request){
						  Ext.MessageBox.hide();
						  alert("There is a problem adding the items to the cart. Please try again or contact your administrator.");	                   }
	               });     
				
				//END - Submit the form via ajax
				
				
			} else {
				alert("There is a problem in this page. Form formItemIds is missing.");
			}
		}
		function resetQuantityError(uId)
		{
			var divId = 'errorDiv_qtys_'+uId;
			var divVal=document.getElementById(divId);
			if(divVal!=null)
				divVal.innerHTML='';
		}
		
		function resetQuantityError()
		{
			var arrQty = new Array();
			arrQty = document.getElementsByName("qtys");
			for(var i = 0; i < arrQty.length; i++)
			{
				var divId='errorDiv_'+	arrQty[i].id;
				var divVal=document.getElementById(divId);
				divVal.innerHTML='';
			}
		}
		
		/*function validateOrderMultiple(isOnlyOneItem,listId)
		{
			
			var arrQty = new Array();
			var arrUOM = new Array();
			var arrItemID = new Array();
			var arrOrdMul = new Array();
			if(isOnlyOneItem != undefined && isOnlyOneItem == true)
			{
				arrQty[0]=document.getElementById("qtys_"+listId);
				arrUOM[0]=document.getElementById("UOMconversion_"+listId);
				arrItemID[0]=document.getElementById("orderLineItemIDs_"+listId);
				arrOrdMul[0]=document.getElementById("orderLineOrderMultiple_"+listId);
			}
			else
			{
				arrQty = document.getElementsByName("qtys");
				arrUOM = document.getElementsByName("UOMconversion");
				arrItemID = document.getElementsByName("orderLineItemIDs");
				arrOrdMul =  document.getElementsByName("orderLineOrderMultiple");
			}

			var errorflag=true;
			for(var i = 0; i < arrItemID.length; i++)
			{
				var divId='errorDiv_'+	arrQty[i].id;
				var divVal=document.getElementById(divId);
				if(isOnlyOneItem == true && arrQty[i].value == ''){
					divVal.innerHTML='No quantities are defined for the item. Add at least 1 in the quantity field and click \'Add to Cart\'';
					errorflag= false;
					return errorflag;
				}
				var totalQty = arrUOM[i].value * arrQty[i].value;
				if(arrOrdMul[i].value == undefined || arrOrdMul[i].value.replace(/^\s*|\s*$/g,"") =='' || arrOrdMul[i].value == null)
				{
					arrOrdMul[i].value=1;
				}
				var ordMul = totalQty % arrOrdMul[i].value;*/
				
				/*var divId='errorDiv_'+	arrQty[i].id;
				var divVal=document.getElementById(divId);
				if(arrQty[i].value == '' )
				{
					divVal.innerHTML='No quantities are defined for the item. Add at least 1 in the quantity field and click \'Add Items with Qty to Cart\'';
					errorflag= false;
				}
				else*/
				 
				/*if(ordMul!= 0||  (isOnlyOneItem == true && arrQty[i].value == '0'))
				{
					divVal.innerHTML="Order Quantity for " + arrItemID[i].value + " must be a multiple of " + arrOrdMul[i].value;
					//divVal.innerHTML="You must order in units of "+ arrOrdMul[i].value+", please review your entry and try again.";
					errorflag= false;
				}
			}
			return errorflag;
		}*/

		function validateOrderMultiple(isOnlyOneItem,listId)
		{
			
			
			var arrQty = new Array();
			var arrUOM = new Array();
			var arrItemID = new Array();
			var arrOrdMul = new Array();
			if(isOnlyOneItem != undefined && isOnlyOneItem == true)
			{
				arrQty[0]=document.getElementById("qtys_"+listId);
				arrUOM[0]=document.getElementById("UOMconversion_"+listId);
				arrItemID[0]=document.getElementById("orderLineItemIDs_"+listId);
				arrOrdMul[0]=document.getElementById("orderLineOrderMultiple_"+listId);
			}
			else
			{
				arrQty = document.getElementsByName("qtys");
				arrUOM = document.getElementsByName("UOMconversion");
				arrItemID = document.getElementsByName("orderLineItemIDs");
				arrOrdMul =  document.getElementsByName("orderLineOrderMultiple");
			}

			var errorflag=true;
			for(var i = 0; i < arrItemID.length; i++)
			{
				var divId='errorDiv_'+	arrQty[i].id;
				var divVal=document.getElementById(divId);

				var quantity = arrQty[i].value;
				quantity = ReplaceAll(quantity,",","");
				divVal.setAttribute("class", "error");
				
				if((quantity == '0' || quantity== '' ) && isOnlyOneItem == true)
				{
					if((arrOrdMul[i].value!=null || arrOrdMul[i].value!='') && arrOrdMul[i].value>1)
					{
						//divVal.innerHTML="You must order in units of "+ arrOrdMul[i].value+", please review your entry and try again.";
						divVal.innerHTML= " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + arrOrdMul[i].value;
						divVal.style.display = 'block';
						errorflag= false;
					}
					if(quantity == '0' || quantity== '' ){
						//Display Generic Message at Header level first then Update Line Level message.
						displayMsgHdrLevelForLineLevelError ();
						/* divVal.innerHTML='Qty Should be greater than 0'; */
						divVal.innerHTML="<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGTZERO' />";
						divVal.style.display = 'block';
						
						errorflag= false;
					}
				}
				else if(quantity>0){
					var totalQty = arrUOM[i].value * quantity;
					if(arrOrdMul[i].value == undefined || arrOrdMul[i].value.replace(/^\s*|\s*$/g,"") =='' || arrOrdMul[i].value == null)
					{
						arrOrdMul[i].value=1;
					}
					var ordMul = totalQty % arrOrdMul[i].value;
					if(ordMul!= 0)
					{
						//divVal.innerHTML="You must order in units of "+ arrOrdMul[i].value+", please review your entry and try again.";
						divVal.innerHTML="<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + arrOrdMul[i].value ;
						divVal.style.display = 'block';
						errorflag= false;
					}
				}
			}
			return errorflag;
		}
		
		function addSpecialItem(uId){
			
			//Get all the values
			var siUIDesc 	= Ext.get("siUIDesc_"  + uId);
			var siUIQty 	= Ext.get("siUIQty_"   + uId);
			var siUIUom 	= Ext.get("siUIUom_"   + uId);
			var siUIJobId 	= Ext.get("siUIJobId_" + uId);
			
			var siName 			= Ext.get("siName_"  		+ uId);
			var siDesc 			= Ext.get("siDesc_"  		+ uId);
			var siQty 			= Ext.get("siQty_"   		+ uId);
			var siUom 			= Ext.get("siUom_"   		+ uId);
			var siJobId 		= Ext.get("siJobId_" 		+ uId);
			var siItemId 		= Ext.get("siItemId_" 		+ uId);
			var siItemType 		= Ext.get("siItemType_" 	+ uId);
			var siItemTypeText 	= Ext.get("siItemTypeText_" + uId);
			
			//Transfer the values for the records
			//Id is already transfer by the jsp page
			
			var desc = siUIDesc.dom.value;
			if (desc.length > 200){
				alert(" The description you have entered is longer than the maximum limit 200. Please reenter.");
				return;
			}
			//desc = desc.substring(0, 200);
			
			siName.dom.value 		= siItemId.dom.value;
			siDesc.dom.value 		= desc;
			siQty.dom.value 		= siUIQty.dom.value;
			siUom.dom.value 		= siUIUom.dom.value;
			if(siJobId != null)
			{
				siJobId.dom.value 		= siUIJobId.dom.value;
			}
			siItemType.dom.value	= "99";
			siItemTypeText.update("Non-Catalog Item") ;
			
							
			//Show the current record
			var el = Ext.get("itemUI_" + uId);
			el.setVisibilityMode(Ext.Element.DISPLAY);
			el.show();
			
			//hide the message
			var el = Ext.get("invalidItemMessage_" + uId);
			el.setVisibilityMode(Ext.Element.DISPLAY);
			el.hide();
			
			//-- Web Trends tag start --
			writeMetaTag('DCSext.w_x_qa_special','1');
			//-- Web Trends tag End --
			
			//Hide the window
			$.fancybox.close();
		}
		
		function showDialog(title, contentId, okText, onOk){
		  Ext.onReady(function() {
		    var w = null;
		    
		    w = Ext.WindowMgr.get("win_" + contentId);
		    
		    if (w != undefined && w != null ){
		    	w.show(this);
		    	return;
		    }
		    
		    w = new Ext.Window({
		      autoScroll: true,
		      id: 'win_' + contentId,
		      closeAction: 'hide',
		      contentEl: contentId,
		      hidden: true,
		      modal: true,
		      width: 'auto',
		      height: 'auto',
		      resizable: true,
		      shadow: 'drop',
		      shadowOffset: 10,
		      title: title,
			  buttons: [{
				text: 'Close',
				handler: function(){
					w.hide();
				}
			  },{
			  	text: okText,
			  	handler: onOk
			  }]
		    });
		    w.show(this);
		  });
		}
		
		function quickAddCopyAndPaste(data){
			//Clean up the data
			Ext.get('dlgCopyAndPasteText').dom.value = '';
			$.fancybox.close();
			
			//console.debug("data: ", data);
			var itemsString = data;
			var char = '\n';
			var itemLines = itemsString.split(char);
						
			for(var i=0;i < itemLines.length; i++)
			{
				var itemQty = null;
				var itemSku = null;
				var jobId = "";
				var itemLine = itemLines[i].split('\t');
				
				if(itemLine.length > 1 )
				{
					itemQty = itemLine[0];
					itemSku = itemLine[1];
				}
				itemLine = itemLines[i].split(',');
				if(itemLine.length > 1 )
				{
					itemQty = itemLine[0];
					itemSku = itemLine[1];
				}
				
				itemSku = Ext.util.Format.trim(itemSku);
				itemQty = Ext.util.Format.trim(itemQty);
				
				document.getElementById("prodId").value= itemSku;
				document.getElementById("qty").value= itemQty;
				qaAddItem(jobId, itemQty, itemSku, '1','', 'xpedx #' ); 
			}
			
			//var w = Ext.WindowMgr.get("dlgCopyAndPaste");
			//w.hide();
		}
		
		function exportList(){
			//Wait for 5 sec and them take the dialog off 
			var task = new Ext.util.DelayedTask(function(){
			    Ext.MessageBox.hide();
			});
			task.delay(5000);
			
			document.getElementById('formItemIds').command.value = 'export_list'; 
			document.getElementById('formItemIds').submit();

			//-- Web Trends tag start --
			writeMetaTag('DCSext.w_x_list_export', '1');
			//-- Web Trends tag end --
			
			//Ext.Msg.wait("Exporting list...Please wait!");
			    xpedx_working_start();
                setTimeout(xpedx_working_stop, 3000);
		}

		$(document).ready(function() {
			$("#selAll1").click(function() {
				if($("#selAll1").attr('checked')) {
					selectAll();
					$("#selAll2").attr('checked', true);
				} else {
					deSelectAll();
					$("#selAll2").attr('checked', false);
				}
			});
			$("#selAll2").click(function() {
				if($("#selAll2").attr('checked')) {
					selectAll();
					$("#selAll1").attr('checked', true);
				} else {
					deSelectAll();
					$("#selAll1").attr('checked', false);
				}
			});

		});
		
		function selectAll(){
			var checkboxes = Ext.query('input[id*=itemKeys]');
			Ext.each(checkboxes, function(obj_item){
				obj_item.checked = true;
			});
			
			var checkboxes = Ext.query('input[id*=checkItemKeys]');
			Ext.each(checkboxes, function(obj_item){
				obj_item.checked = true;
			});
			
		}
		


		function displayMsgHdrLevelForLineLevelError() {
			//var msgGenericForLineLevelErrors = "Error occurred in One of the Line, Please Correct";
			var msgGenericForLineLevelErrors = "<s:text name='MSG.SWC.MIL.GENHDRLEVELMSG.ERROR.LINELEVELERROS' />";
			document.getElementById("errorMsgTop").innerHTML = msgGenericForLineLevelErrors ;
            document.getElementById("errorMsgTop").style.display = "inline";

			document.getElementById("errorMsgBottom").innerHTML = msgGenericForLineLevelErrors ;
            document.getElementById("errorMsgBottom").style.display = "inline";
			
		}
		
		
		function updateSelectedPAA() {
			 //alert("updateSelectedPAA" );

			var checkboxes = Ext.query('input[id*=checkItemKeys]');
			var nLineSelected = 0;
			
			var cnt =0;
			var singleSelectedKey;
			Ext.each(checkboxes, function(obj_item){
				if(obj_item.checked) {
					singleSelectedKey = obj_item.value;
					cnt++;
				}
			});

			if(cnt == 1){
				var itemId = document.getElementById("enteredProductIDs_"+singleSelectedKey).value;
				checkAvailability(itemId,singleSelectedKey);
			}
			else if(cnt>0)
			{
				if(cnt == checkboxes.length)
					document.getElementById('formItemIds').command.value = 'stock_check_all'; 
				else {
					document.getElementById('formItemIds').command.value = 'stock_check_sel';
				} 
				document.getElementById('formItemIds').submit();
			}

			if(cnt <=0 ){
				
				//alert("Please select at least one item for Price Check : " + cnt );
				//var msgSelectItemFirst = "You have not selected any items for Price Check. Please select an item and try again";
				var msgSelectItemFirst = "<s:text name='MSG.SWC.MIL.NOITEMSELECT.ERROR.SELECTFORPNA' />";
				document.getElementById("errorMsgTop").innerHTML = msgSelectItemFirst ;
                document.getElementById("errorMsgTop").style.display = "inline";

				document.getElementById("errorMsgBottom").innerHTML = msgSelectItemFirst ;
                document.getElementById("errorMsgBottom").style.display = "inline";
				
				//Ext.MessageBox.alert('Alert','Please select at least one item for Price Check.');
			}
			/*Ext.each(checkboxes, function(obj_item){
				if(obj_item.checked) {
					nLineSelected = nLineSelected.value + 1;
					$('#PAAClick_'+obj_item.value).trigger('click');
				}
			});

			if(nLineSelected <=0 ){
				var msgSelectItemFirst = "You have not selected any items for Price Check. Please select an item and try again";
				document.getElementById("errorMsgTop").innerHTML = msgSelectItemFirst ;
                document.getElementById("errorMsgTop").style.display = "inline";

				document.getElementById("errorMsgBottom").innerHTML = msgSelectItemFirst ;
                document.getElementById("errorMsgBottom").style.display = "inline";
				
				//Ext.MessageBox.alert('Alert','Please select at least one item for Price Check.');
			}*/
		}

		function deSelectAll(){
			var checkboxes = Ext.query('input[id*=itemKeys]');
			Ext.each(checkboxes, function(obj_item){
				obj_item.checked = false;
			});
			
			var checkboxes = Ext.query('input[id*=checkItemKeys]');
			Ext.each(checkboxes, function(obj_item){
				obj_item.checked = false;
			});
			
		}
		
		function selectNone(){
			var checkboxes = Ext.query('input[id*=state_]');
			Ext.each(checkboxes, function(obj_item){
				obj_item.checked = false;
			});
			
			//Ext.getDom('update_button').disabled = true;
		}
			
		function openAddToCart(val){
			if(val != null) {
				selectedItemId = val;
				$('#addToCartFancybox').trigger('click');
			}
			else {				
				return false;
			}
		}

		function processDetail(itemid, uom) {
			<s:url id='detailURL' namespace='/catalog' action='itemDetails.action'>
			</s:url>
			window.location.href="<s:property value='%{detailURL}' escape='false'/>" + "&itemID=" + itemid + "&unitOfMeasure=" + uom;
		}
		
		//Place holder to indicate the current id
		var selReplacementId = "";

		function setUId(uId)
		{
			selReplacementId = uId;
		}
		
		var defaultMsg = "";
		
		function clearPreviousDisplayMsg( )
		{
			//alert("clearPreviousDisplayMsg ... Called " ); 
			document.getElementById("errorMsgTop").innerHTML = defaultMsg ;
			document.getElementById("errorMsgTop").style.display = "none";
			
			document.getElementById("errorMsgBottom").innerHTML = defaultMsg ; 
            document.getElementById("errorMsgBottom").style.display = "none";
			    

            
		}
		
	</script>
 

<script type="text/javascript" src="/swc/xpedx/js/jquery.blockUI.js"></script>
 

</head>
<!-- Hemantha -->
<body class="  ext-gecko ext-gecko3">

<!--  Shopping for banner -->
<div id="main-container"><!-- CODE_START - Global HTML -PN -->

<!-- END swc:head -->

<!-- CODE_START - Global Vars -PN -->

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
			    This is to avoid a defect in Struts that?s creating contention under load. 
			    The explicit call style will also help the performance in evaluating Struts? OGNL statements. --%>
<s:set name='_action' value='[0]' />
<s:set name="xutil" value="XMLUtils" />
<s:set name='categoryListElem' value="categoryListElement" />
<s:set name='childCategoryListElem' value="childCategoryListElement" />
<s:set name='fieldListElem'
	value="searchableIndexFieldListOutPutElement" />
<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>

<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
<s:bean name='org.apache.commons.lang.StringUtils' id='strUtil' />
<s:a href="#addToCartDlg" id="addToCartFancybox" cssStyle="display: none;"></s:a>
<s:a href="#Price_Grid" id="bracketPricing" cssStyle="display: none;"></s:a>
<s:url id='availabilityURL' action='ajaxAvailabilityJson' namespace='/catalog' />
<s:url id='checkAvailabilityURL' action='getItemAvailabilty' namespace="/xpedx/myItems" />
<input type="hidden" value="<s:property value='%{checkAvailabilityURL}'/>" id="checkAvailabilityURLHidden"/>
<s:url id='addToCartURL' namespace='/order' action='addToCart' />
<s:url id='addToCartURLId' namespace="/xpedx/myItems" action='addMyItemToCart' />
<s:hidden id='updatePandAURL' value='%{#addToCartURLId}' />
<s:hidden id='currentCartInContextOHKVal' value='%{#currentCartInContextOHK}' />

<!-- HTML Dialogs -->
<div style="display: none;"><%-- <s:select id="listOfCarts"
	name="listOfCarts" headerKey="-1" list="listOfCarts"
	listValue="getAttribute('OrderName')"
	listKey="getAttribute('OrderHeaderKey')" emptyOption="false"
	value="orderHeaderKey" />
	Issue #1338 : To improve performance for a list of size 200 --%>

<div id="dlgNewForm" class="dlgForm">
<h3>New Item</h3>
<s:form action="XPEDXMyItemsDetailsCreate" method="post">
	<s:textfield label="Name" name="name"></s:textfield>
	<s:textfield label="Desc" name="desc"></s:textfield>
	<s:textfield label="Qty" name="qty"></s:textfield>
	<s:textfield label="Job Id" name="JobId"></s:textfield>
	<s:hidden name="listKey" value="%{listKey}"></s:hidden>
	<s:hidden name="listName" value="%{listName}"></s:hidden>
	<s:hidden name="listDesc" value="%{listDesc}"></s:hidden>

	<br /> <br /> <s:submit value="Create New Item" align="center"></s:submit>
	&nbsp;&nbsp; <input type="button" value="Cancel"
		onclick="hideForm('NewForm')" />
</s:form></div>

<s:include value="../modals/XPEDXItemImportModal.jsp" />

<s:include value="../modals/XPEDXShareMyItemsList.jsp" />

<div id="dlgCopyAndPaste" class="xpedx-light-box"
	style="width: 400px; height: 300px;">
<h2>Copy and Paste</h2>
<%-- <p>Copy and Paste the quantities and <s:property value="wCContext.storefrontId" /> item #'s from your file. --%>
<!-- Enter one item per line:<br /> -->
<!-- Qty. [Tab or Comma] Item#</p> -->
<p>Copy and Paste the quantities and <s:property value="wCContext.storefrontId" /> item #'s from your file.
Or enter manually with quantity and item #, separated by a comma, per line. Example:12,5002121 <br />
</p>
<br />
<form id="form1" name="form1" method="post" action=""><textarea
	name="dlgCopyAndPasteText" id="dlgCopyAndPasteText" cols="48" rows="5"></textarea>
<ul id="tool-bar" class="tool-bar-bottom" style="float:right";>
	<li><a class="grey-ui-btn" href="javascript:$.fancybox.close();"
		onclick="Ext.get('dlgCopyAndPasteText').dom.value = '';"><span>Cancel</span></a></li>
	<li style="float: right;"><a href="javascript: quickAddCopyAndPaste( document.form1.dlgCopyAndPasteText.value);" class="green-ui-btn" style="margin-left:5px;"><span>Add to Quick List</span></a></li>
	
	
</ul>
</form>
</div>


<div id="dlgEditForm" class="dlgForm">
<h3>Edit Record</h3>
<s:form id="formEdit" action="XPEDXMyItemsDetailsChange" method="post">
	<s:textfield label="Name" name="name"></s:textfield>
	<s:textfield label="Desc" name="desc"></s:textfield>
	<s:textfield label="Qty" name="qty"></s:textfield>
	<s:textfield label="Job Id" name="jobId"></s:textfield>
	<s:textfield label="Order" name="order"></s:textfield>

	<s:hidden name="key" value=""></s:hidden>
	<s:hidden name="listKey" value="%{listKey}"></s:hidden>
	<s:hidden name="listName" value="%{listName}"></s:hidden>
	<s:hidden name="listDesc" value="%{listDesc}" />
	<s:hidden name="itemCount" value="%{itemCount}"></s:hidden>
	<s:hidden name="editMode" value="%{editMode}"></s:hidden>

	<br /> <br /> 
	<s:submit value="Save" align="center"></s:submit>
	&nbsp;&nbsp; <input type="button" value="Cancel"
		onclick="hideForm('EditForm')" />
</s:form></div>
</div>
<s:form id="formEditMode">
	<s:hidden name="listKey" value="%{listKey}"></s:hidden>
	<s:hidden name="listName" value="%{listName}"></s:hidden>
	<s:hidden name="listDesc" value="%{listDesc}"></s:hidden>
	<s:hidden name="itemCount" value='%{itemCount}'></s:hidden>
	<s:hidden name="sharePermissionLevel" value="%{sharePermissionLevel}"></s:hidden>
	<s:hidden name="shareAdminOnly" value="%{shareAdminOnly}"></s:hidden>
	<s:hidden name="listOwner" value="%{listOwner}"></s:hidden>
	<s:hidden name="listCustomerId" value="%{listCustomerId}"></s:hidden>
	<s:hidden name="filterBySelectedListChk" value="%{#_action.isFilterBySelectedListChk()}"/>
	<s:hidden name="filterByMyListChk" value="%{#_action.isFilterByMyListChk()}"/>
	<s:hidden name="filterByAllChk" value="%{#_action.isFilterByAllChk()}"/>
	<s:hidden name='sharePrivateField' value='%{#_action.getSharePrivateField()}' />
	
	<s:hidden name="editMode" value="%{true}"></s:hidden>
</s:form> <!-- CODE_END - Global HTML -PN -->

    <div id="main">
        <!-- CODE_START Header - PN --> <s:action name="xpedxHeader" executeResult="true" namespace="/common" /> <!-- CODE_END Header - PN -->
	
	<s:if test='!#guestUser'>  	
		<s:action name="xpedxShiptoHeader" executeResult="true"
		namespace="/common" />
	</s:if>	
	
        <div class="container">
            <!-- breadcrumb -->
			<s:url action='home.action' namespace='/home' id='urlHome'
				includeParams='none' /> <s:url id='urlMIL' namespace='/xpedx/myItems'
				action='XPEDXMyItemsList.action' includeParams="get" escapeAmp="false">
				<s:param name="filterByAccChk" value="%{#_action.getFilterByAccChk()}" />
				<s:param name="filterByShipToChk" value="%{#_action.getFilterByShipToChk()}" />
				<s:param name="filterByMyListChk" value="%{#_action.getFilterByMyListChk()}" />
				<s:param name="filterByAccSel" value="%{#_action.getFilterByAccSel()}" />
				<s:param name="filterByShipToSel" value="%{#_action.getFilterByShipToSel()}" />
			</s:url>
			
			<div id="mid-col-mil">
			<h5 align="center"><b><font color="red"><s:property
				value="ajaxLineStatusCodeMsg" /></font></b></h5>
			</div>

			<s:set name="xpedxItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_ITEM_LABEL"/>
			<s:set name="customerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUSTOMER_ITEM_LABEL"/>
			<s:set name="manufacturerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MANUFACTURER_ITEM_LABEL"/>
			<s:set name="mpcItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MPC_ITEM_LABEL"/>

			<s:hidden id="mandatoryFieldCheckFlag_mil-edit" name="mandatoryFieldCheckFlag_mil-edit" value="%{false}"></s:hidden>

			<s:if test='itemDeleted == true'>			
<!-- 				<div   style="color:green" align="center"><h3><br/>Item(s) has been removed from list</h3></div> -->
			</s:if>
			
			
               
               
               	<%-- <div id="breadcumbs-list-name" > <span class="page-title"> My Items List:</span> &nbsp; <span><s:property value="listName" /> </span> --%>
               	<div id="breadcumbs-list-name" > 
               	<span class="page-title"> <s:text name='MSG.SWC.MIL.DETL.GENERIC.PGTITLE' />:</span> 
               	&nbsp; <span><s:property value="listName" /> </span>
				<a href="javascript:window.print()"><span class="print-ico-xpedx underlink" style="font-weight:normal; font-size:12px;">
				<img src="/swc/xpedx/images/common/print-icon.gif" width="16" height="15" alt="Print Page" />Print Page</span></a> </div>
		    
 		<%--             
 			<s:if test='editMode != true'>	 	
				<div id="breadcumbs-list-name" > <span class="page-title"> My Items List:</span> &nbsp; <span><s:property value="listName" /> </span>
				<a href="javascript:window.print()"><span class="print-ico-xpedx underlink" style="font-weight:normal; font-size:12px;">
				<img src="/swc/xpedx/images/common/print-icon.gif" width="16" height="15" alt="Print Page" />Print Page</span></a> </div>
			</s:if>
			<s:else>
				<div id="breadcumbs-list-name"><span class="page-title"> Edit My Items List: </span> &nbsp; <span><s:property value="listName" /> </span>
				<a href="javascript:window.print()"><span class="print-ico-xpedx underlink" style="font-weight:normal; font-size:12px;">
				<img src="/swc/xpedx/images/common/print-icon.gif" width="16" height="15" alt="Print Page" />Print Page</span></a> </div>
			</s:else> 
			--%>
			
			<s:if test='editMode != true'>				
				<s:if test="%{errorMsg!=null && errorMsg!= ''">
					<s:if test="%{errorMsg == 'ITEM_REPLACE_ERROR'}" >
					<h5 align="center"><b><font color="red">
					The selected replacement item could not be replaced in the list.
					</font></b></h5>
					<br />
					</s:if>
					<s:elseif test="%{errorMsg == 'ITEM_ADD_ERROR'}">
					<h5 align="center"><b><font color="red">The selected replacement item could not be added to the list
					</font></b></h5>
						<br />
					</s:elseif>
					<s:elseif test="%{errorMsg == 'ITEM_SAVE_ERROR'}">
					<h5 align="center"><b><font color="red">The changes could not be saved
					</font></b></h5>
						<br />
					</s:elseif>
					<s:else>
						<h3>The import must be in csv format, please try another import file.</h3>
						<br />
					</s:else>
				</s:if>
			</s:if>

            <div id="mid-col-mil">
           		 	<s:if test='editMode != true'> 
                        <div class="ad-float">
                        <div class="float-left smallBody">
						
						<img class="float-left" style="margin-top:5px; padding-right:5px;" src="/swc/xpedx/images/mil/ad-arrow.gif" width="7" height="4" alt="" />advertisement</div>
						
                        <div class="clear"></div>
                        <!-- Ad Juggler Tag Starts -->
                        <s:set name='ad_keyword' value='' />						
				<s:iterator status="status" id="item" value='XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems")'>
					<s:if test="%{#status.index == 0}">
							<s:set name='itemId1' value='#item.getAttribute("ItemId")+"" ' />
								<s:if test="#itemId1 != null" >									
									<s:set name="cat2Val" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getCatTwoDescFromItemId(#itemId1,wCContext.storefrontId)" />
									<s:if test="#cat2Val != null" >
										<s:set name='ad_keyword' value='#cat2Val' />
									</s:if>
								</s:if>
					</s:if>
				</s:iterator>
				<s:set name='storefrontId' value="wCContext.storefrontId" />
		<s:if test='%{#ad_keyword != null}' >
			<s:if test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = 'https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118165'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:if>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CANADA_STORE_FRONT}' >
								<script type="text/javascript" language="JavaScript">
				aj_server = 'https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118201'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@BULKLEY_DUNTON_STORE_FRONT}' >
									<script type="text/javascript" language="JavaScript">
				aj_server = 'https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118189'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif >
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = 'https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115717'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:else>
				<script type="text/javascript" language="JavaScript">
				aj_server = 'https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115717'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:else>			
		</s:if>	
		<s:else>
			<s:if test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = 'https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118165'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:if>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CANADA_STORE_FRONT}' >
								<script type="text/javascript" language="JavaScript">
				aj_server = 'https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118201'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@BULKLEY_DUNTON_STORE_FRONT}' >
									<script type="text/javascript" language="JavaScript">
				aj_server = 'https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118189'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif >
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = 'https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115717'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:else>
				<script type="text/javascript" language="JavaScript">
				aj_server = 'https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115717'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:else>			
		</s:else>
			<script type="text/javascript" language="JavaScript" src="https://img.hadj7.adjuggler.net/banners/ajtg.js"></script>
			<!-- Ad Juggler Tag Ends -->
</div>
</s:if>
		 	<s:if test='editMode != true'> 
                <div class="mil-edit">

        
                    	   <span class="grey" style="width:421px; word-wrap:break-word; float:left;"><s:property value="listDesc" /></span></div>
                <!-- Close mil-edit -->
                <div class="clear"></div>
                <fieldset class="mil-non-edit-field">
                    <legend>For Selected Items:</legend>
                    <input class="forselected-input" type="checkbox" id="selAll1"/>
                   <%--  <a class="grey-ui-btn float-left" href="javascript:updateSelectedPAA()"><span>Update My Price &amp; Availability</span></a> --%>
                    <a class="grey-ui-btn float-left" href="javascript:myUpdateSelectedPAA()"><span>Update My Price &amp; Availability</span></a>
                </fieldset>

                <ul id="tool-bar" class="tool-bar-bottom" style="width:403px; float:left; padding-top:5px; margin-left:9px;">
                    <li><a class=" grey-ui-btn" href="javascript:exportList(); "><span>Export List</span></a></li>
					<s:if test="%{canEditItem}">
						<li><a class="grey-ui-btn" href="javascript:document.getElementById('formEditMode').submit();"><span>Edit This List</span></a></li>
					</s:if>
                    <!--     <li><a class="modal grey-ui-btn" href="../modals/new-copy-edit-cart-details-redesign.html"><span>New List</span></a></li>-->
                </ul>
                <ul id="tool-bar float-right" class="tool-bar-bottom" style="float:right; padding-top:5px; margin-right:5px;">
                    <li><a class="orange-ui-btn" href="javascript:addToCart();">
                    <s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
                    <span>Add Items with Qty to Cart</span>
                    </s:if>
                    <s:else>
                     <span>Add Items with Qty to Order</span>
                    </s:else>
                    </a></li>

                </ul>
			</s:if>
			<s:else>
				<div id="mil-edit" class="mil-edit" style="width:100%">
                    <div id="quick-add" class="quick-add float-right">
                        <div class="clear">&nbsp;</div>
						<s:hidden id="mandatoryFieldCheckFlag_quick-add" name="mandatoryFieldCheckFlag_quick-add" value="%{false}"></s:hidden>
                        <h2 style="float: left; margin-top:5px;">Quick Add</h2>
                        <p class="quick-add-aux-links underlink" style="color: orange;margin-top:5px; margin-right:5px;"> <a id="various2" class="modal" href="#dlgCopyAndPaste" onclick="javascript: writeMetaTag('DCSext.w_x_ord_quickadd_cp', '1');">Copy and Paste</a></p>

                        <div class="clear">&nbsp;</div>
                        <div class="quick-add-form-top">&nbsp;</div>
                        <div class="quick-add-form quick-add-form-mil">
							<form class="form selector" id="quick-add-selector" name="quickaddselector">
							<br/>
							
                                <ul class="hvv">
                                    <li>
                                        <label>Item Type</label>
										<s:select name="itemType" cssStyle="width:75px;" headerKey="1" list="skuTypeList" listKey="key" listValue="value"/>
                                    </li>
                                    <li>
                                        <label>Item #</label>
										<p class="p-word-wrap"><s:textfield maxlength="27" cssStyle="width:50px;" cssClass="text x-input" name="prodId" onkeyup="javascript:listNameCheck(this, 'quick-add');"	onmouseover="javascript:listNameCheck(this, 'quick-add');"></s:textfield></p>
                                    </li>
                                    <li>
                                        <label>Qty</label>
										<s:textfield maxlength="10" cssStyle="width:63px;" cssClass="qty-field text x-input" name="qty" value="" onkeyup="javascript:isValidQuantity(this);" onchange="javascript:this.value=addComma(this.value);"></s:textfield>
                                    </li>
                                    <s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>
	                                    <li>
	                                        <!-- label>Job Number:</label>  -->
	                                        <label><s:property value="#jobIdFlag" />:</label>
											<s:textfield maxlength="25" cssStyle="width:154px;" cssClass="text x-input" name="jobId" value=" "></s:textfield>
	                                    </li>
									</s:if>
									<!-- This condition check is also applied to the kind of css file that's been included. Refer in this page above in the <head> tag. -->
									<s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
										<li>
											<label>Line PO #</label>
											<s:textfield maxlength="25"  cssStyle="width:154px;" cssClass="text x-input" name="purchaseOrder" value=" "></s:textfield>
										</li>
									</s:if>
									<li>
										<label>&nbsp;</label>
										<input type="image"
											src="/swc/xpedx/images/theme/theme-1/quick-add/addtoquicklist.png"
											onclick="$('#prodId').focus();qaAddItem1(this.form); return false;" />
									</li>

                                </ul>
                            </form>

							<s:form id="formAdd2List" cssClass="form quick-add-to-cart-form" cssStyle="padding-right:21px;"
								action="XPEDXMyItemsDetailsQuickAdd" method="post">
								<s:hidden name="listKey" value="%{listKey}"></s:hidden>
								<s:hidden name="listName" value=""></s:hidden>
								<s:hidden name="listDesc" value=""></s:hidden>
								<s:hidden name="itemCount" value="%{#parameters.itemCount}"></s:hidden>
								<s:hidden name="editMode" value="%{editMode}"></s:hidden>

                                <table id="qaTableOld" cellspacing="0" cellpadding="0" 
									<s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>width="694"</s:if><s:else>width="524"</s:else>>
                                    <thead>
                                        <tr>
                                            <th class="del-col">&nbsp;</th>
                                            <th class="first-col-header col-header type-col">Item Type</th>

                                            <th class="col-header item-col">Item #</th>
                                            <th class="col-header qty-col">Qty</th>
                                            <th class="col-header uom-col">UOM</th>
                                            <s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>
                                            	<th class="col-header col-header job-col"><!-- Job Number  --><s:property value="#jobIdFlag" /></th>
                                            </s:if>
											<s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
												<th class="last-col-header col-header po-col">Line PO #</th>
											</s:if>
                                        </tr>

                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
								<table id="qaTable" cellspacing="0" cellpadding="0"  
									<s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>width="694"</s:if><s:else>width="524"</s:else>>
								</table>
								<div class="fFVVEM_wrap"><div style="display: none;" class="error" id="errorMsgForMandatoryFields_quick-add"></div></div> 
                                <input id="btnQLAdd2Cart" type="image" src="/swc/xpedx/images/theme/theme-1/quick-add/addtolist.png" 
								class="add-to-cart-btn" onclick="add2List();return false;" />
                            </s:form> <!-- CODE_END Quick Add - PN -->
							
                            <div class="clear">&nbsp;</div>
                            <div class="quick-add-form-bot"></div>
                        </div>

                    </div>
                    <div class="mil-edit-forms">
<%-- 						<s:if test="%{errorMsg == 'ItemsOverLoad'}"> --%>
<!-- 							<div style="color:red"><h3> -->
<!-- 							Your list may contain a maximum of 200 items, Please delete some items and try again. -->
<!-- 							</h3></div> -->
<!-- 							<br /> -->
<%-- 						</s:if> --%>

						<s:set name='isPrivateList' value="%{#_action.isFilterByMyListChk()}"/>
						<s:set name='isSelectedList' value="%{#_action.isFilterBySelectedListChk()}"/>
						<s:set name='isFilterByAll' value="%{#_action.isFilterByAllChk()}"/>
						<s:set name='sharePrivate' value="%{#_action.getSharePrivateField()}"/>
						<s:if test="(#isPrivateList)||(#isUserAdmin && #isSelectedList)">
							<s:set name='disableListNameAndDesc' value="%{false}" />
						</s:if><s:else>
							<s:set name='disableListNameAndDesc' value="%{true}" />
						</s:else>
						<s:if test="#sharePrivate != '' && #sharePrivate != null" >
							<s:set name='disableListNameAndDesc' value="%{false}" />
						</s:if>
						<s:else>
							<s:if test="#isUserAdmin">
								<s:set name='disableListNameAndDesc' value="%{false}" />
							</s:if>
							<s:else>
								<s:set name='disableListNameAndDesc' value="%{true}" />
							</s:else>
						</s:else>

                        
                          <p >Name</p> 
						<s:if test="%{#disableListNameAndDesc == true}">
							<input style="width:201px;"class="x-input text" id="listName" title="Name" maxlength="35" disabled="disabled"
							onkeyup="javascript:listNameCheck(this, 'mil-edit');" onmouseover="javascript:listNameCheck(this, 'mil-edit');" value="<s:property value="listName"/>"/>
						</s:if><s:else>
							<input style="width:201px;"class="x-input text" id="listName" title="Name" maxlength="35" 
							onkeyup="javascript:listNameCheck(this, 'mil-edit');" onmouseover="javascript:listNameCheck(this, 'mil-edit');" value="<s:property value="listName"/>"/>
						</s:else>
                        <br />
                        <br />
                          <p >Description</p>
						<s:if test="%{#disableListNameAndDesc == true}">
							<textarea class="x-input" id="listDesc" disabled="disabled" title="Description" rows="2" onkeyup="javascript:restrictTextareaMaxLength(this,255);" style="width:220px; height: 92px; word-wrap:break-word;"><s:property
							value="listDesc"   /></textarea>
						</s:if><s:else>
							<textarea class="x-input" id="listDesc" title="Description" rows="2" onkeyup="javascript:restrictTextareaMaxLength(this,255);" style="width:220px; height: 92px; word-wrap:break-word; "><s:property
							value="listDesc"  /></textarea>
						</s:else>
                    </div>
                    <div class="clear">&nbsp;</div>
                </div>
               
                <!-- Close mil-edit -->
				<div class="clear"></div>
                <br />
				<s:if test='XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems").size() > 0'>	
					<fieldset class="mil-edit-field">
	                    <legend>For Selected Items:</legend>
	
	                    <input class="forselected-input-edit" type="checkbox" id="selAll1" />
						<s:if test="%{canDeleteItem}">
							<a class="grey-ui-btn float-left" href="javascript:deleteItems();"><span>Remove Items</span></a>
						</s:if>                    
	                </fieldset>
	            </s:if>
                <ul id="tool-bar" class="tool-bar-bottom" style="width:503px; float:left; padding-top:5px; margin-left:9px;">
                	<s:if test="%{canShare}">
                    <li><a class="grey-ui-btn " id="dlgShareListLink1" href="#dlgShareList" ><span>Share List</span></a></li>
                    </s:if>
                    <li><a href="#dlgImportForm" id="various5"  class="grey-ui-btn"><span>Import Items</span></a></li>
                </ul>

                <ul id="tool-bar float-right" class="tool-bar-bottom" style="float:right; padding-top:5px; width:152px;">
                    <li><a style="margin-left:5px;" class="green-ui-btn float-right-imp" href="javascript:saveAllItemsNew('mil-edit', ['quick-add']);"><span>Save</span></a></li>
                    <li><a class="grey-ui-btn float-right-imp" href="javascript:saveAllItems();"><span>Cancel</span></a></li>
                </ul>
				
			</s:else>

			<!-- CODE_START List of Items - PN -->
			<s:form id="formItemIds" method="post">
				<s:hidden name="listKey" value="%{#parameters.listKey}" />
				<s:hidden name="listName" value="%{listName}" />
				<s:hidden name="listDesc" value="%{listDesc}" />
				<s:hidden name="orderHeaderKey" value="%{orderHeaderKey}" />
				<s:hidden name="draft" value="Y" />
				<s:hidden name="Currency" value="USD" />
				<s:hidden name="command" value="save_all" />
				<s:hidden name="itemCount" value="%{itemCount}"></s:hidden>
				<s:hidden name="editMode" value="%{editMode}"></s:hidden>
				<s:set name='itemOrder2' value='%{0}' />
				<s:set name='itemImagesMap' value="getItemImagesMap()" />
				<s:set name='itemDescMap' value="getItemDescMap()" />
				<s:hidden name="xpedxTimer" id="xpedxTimer" value="0" />
				
				<!-- /*
				<s:if test="%{errorMsg!=null && errorMsg!= '' && errorMsg.indexOf('ROW_PROCESSING_ERROR')>-1}">
						<s:set name='errIndex' value='%{errorMsg.indexOf("@")}' />
						<s:set name='rowNums' value='%{errorMsg.substring(#errIndex +1, errorMsg.length())}' />
						<div class="mil-wrap-condensed-container"  onmouseover="$(this).addClass('green-background');" onmouseout="$(this).removeClass('green-background');" ><h5 align="center"><b><font color="red">Row(s) <s:property value='#rowNums' /> failed to import.
						</font></b></h5></div>
						<br />
					
						<script type="text/javascript">
							importItems( );
						</script>
				</s:if>
				*/ -->
				

            
         
                 
               <ul style="float:center;text-align: center; background-color:white !important;">
                 <li style="margin-bottom: 5px;">   
                 <div class="clearall"></div>
    	
			 
			 		<div class="error" id="errorMsgTop" style="display:none;" ></div> 

	
	
			 <s:if test="%{errorMsg == 'ItemsOverLoad'}">
							<div style="color:red">
							      <!--   Your list may contain a maximum of 200 items. Please delete some items and try again. -->
							         <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGT200' />
							</div>
						</s:if>
					<br/>
                 </li> 
               </ul>
               
               
               
               
               
				<s:iterator status="status" id="item"
					value='XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems")'>
					<s:set name='id' value='#item.getAttribute("MyItemsKey")' />
					<s:set name='name' value='#item.getAttribute("Name")' />
					
					<s:set name='desc2' value='#item.getAttribute("Desc")' />
					<s:set name='desc' value='#item.getAttribute("Desc")' />
					<s:set name='qty1' value='#item.getAttribute("Qty")' />
					<s:set name='qty' value='%{#strUtil.replace(#qty1, ".00", "")}' />
					<s:set name='jobId' value='#item.getAttribute("JobId")' />
					<s:set name='listKey' value='#item.getAttribute("MyItemsListKey")' />
					<s:set name='itemId' value='#item.getAttribute("ItemId")+"" ' />
					<s:set name='desc1'  value="%{#itemDescMap.get(#itemId)}"  />
					<s:set name='itemType' value='#item.getAttribute("ItemType")' />
					<s:set name='itemTypeLabel' value="%{#xpedxItemLabel + '+:'}" />
					<s:set name='showItemType' value='%{true}' />
					<s:set name='itemUomId' value='#item.getAttribute("UomId")' />
					
					<s:set name="itemUOMsMap" value='itemIdConVUOMMap.get(#itemId)' />
					<s:set name="itemBaseUom"  value='%{#itemUomId}' />
					<s:set name="YFSItmePrimaryInfo" value='descriptionMap.get(#item.getAttribute("ItemId"))' />
					<s:set name="YFSItmeExtn" value='masterItemExtnMap.get(#item.getAttribute("ItemId"))' />
					<s:set name='certFlag' value='#YFSItmeExtn.getAttribute("ExtnCert")' />					
					<s:set name='desc' value='#YFSItmePrimaryInfo.getAttribute("Description")' />
					<s:set name='name' value='#YFSItmePrimaryInfo.getAttribute("ShortDescription")' />
					<s:if test='%{#itemUOMsMap!=null}' >
						<s:iterator value='#itemUOMsMap' status='uomIndex'>
							<s:if test="%{#uomIndex.index == 0}" >
								<s:set name='itemBaseUom' value='key' />
							</s:if>
						</s:iterator>
					</s:if>
						
					
					<s:set name='itemId1' value='#item.getAttribute("ItemId")' />

					<s:set name='itemOrder' value='#item.getAttribute("ItemOrder")' />
					
					<s:set name='itemOrder2' value='%{#itemOrder2 + 1}' />
					<s:hidden name="entereditemTypeList" id="entereditemTypeList_%{#id}" value="%{#itemType}"></s:hidden>
					<s:if test="%{#itemType == 2}">
						<s:set name='itemTypeLabel' value="%{#manufacturerItemLabel+':'}" />
					</s:if>
					<s:elseif test="%{#itemType == 0 || #itemType == 99}">
						<s:set name='showItemType' value='%{false}' />
					</s:elseif>
					<s:elseif test="%{#itemId.equals(' ')}">
						<s:set name='showItemType' value='%{false}' />
					</s:elseif>

					<s:url id='deleteListLink' action='XPEDXMyItemsDetailsDelete.action'>
						<s:param name="key" value="#id" />
						<s:param name="listKey" value="%{listKey}" />
						<s:param name="listName" value="%{listName}" />
						<s:param name="listDesc" value="%{listDesc}" />
					</s:url>
					<s:set name='uomList'
						value='itemIdsUOMsDescMap.get(#itemId1)' />

					<s:url id='itemDetailsLink' namespace="/catalog"
						action='itemDetails.action' includeParams='none' escapeAmp="false">
						<s:param name="itemID" value="#itemId" />
						<s:param name="sfId" value="#parameters.sfId" />
						<s:param name="unitOfMeasure" value="#itemBaseUom" />
					</s:url>


    
                   <div class="mil-wrap-condensed-container mil-only"  onmouseover="$(this).addClass('green-background');" onmouseout="$(this).removeClass('green-background');" >    
           
                    <div 
					<s:if test='%{#status.count == 1}'>class="mil-wrap-condensed"</s:if>
					<s:elseif test="!#status.last" >class="mil-wrap-condensed-mid"</s:elseif>
					<s:else>class="mil-wrap-condensed-bot"</s:else>>

                        <!-- begin image / checkbox   -->
                        <div class="mil-checkbox-wrap">
							<s:if test='editMode == true'>
								<s:checkbox name="itemKeys" fieldValue="%{#id}" />
							</s:if> <s:if test='editMode != true'>
								<s:checkbox name="checkItemKeys" fieldValue="%{#id}" />
							</s:if>
                            <!-- div class="mil-question-mark"> --> 
                            <a href='<s:property value="%{itemDetailsLink}" />'>
                            <img style="float:right; margin-left:3px;" src="<s:url value='%{#itemImagesMap.get(#itemId)}' includeParams='none' />" width="150" height="150" alt="" />
                            </a>
                            <s:hidden name="keys" value="%{#id}" /></div>
                        <!-- /div>  -->

                        <!-- end image / checkbox   -->
                        <!-- begin description  -->
						<s:hidden name="itemsName" value="%{#name}" /> 
						<s:hidden name="names" value="%{#name}" /> <s:hidden
						name="enteredProductIDs" id="enteredProductIDs_%{#id}" value="%{#itemId}" /> 
						
						<s:hidden
						name="itemIds" value="%{#itemId}" />
						
						<div class="mil-desc-wrap">
                            <div class="mil-wrap-condensed-desc" style="height:auto; max-height:59px;">  
								<s:if test="%{#itemType != 99}">
									<a class="short-description" href='<s:property value="%{itemDetailsLink}" />'>
									<s:property value="#name" />
									</a>
								</s:if>					
							</div>
                            <div class="mil-attr-wrap">
                                <ul class="prodlist">
									<s:if test="%{#itemType != 99}"> 
										<a href='<s:property value="%{itemDetailsLink}" />'> 
										<s:property value="#desc" escape="false"/>
										</a>
									</s:if> 
										<s:hidden name="itemsDesc" value="%{#desc}" /> 
										<s:hidden name="descs" value="%{#desc}" />
                                </ul>                             
				
								<s:if test="%{#showItemType}">
									<p ><s:property value="wCContext.storefrontId" /> <s:property value="#xpedxItemLabel" />: <s:property value="#itemId" />									
										<s:if test='#certFlag=="Y"'>
											<img src="/swc/xpedx/images/catalog/green-e-logo_small.png" style="margin-left:0px; display: inline;">
										</s:if>
									</p>
									
								<s:if test='editMode == true'>
								<%-- Show Replacement link only in Edit mode --%>
									<s:if test="(xpedxItemIDUOMToReplacementListMap.containsKey(#itemId) && xpedxItemIDUOMToReplacementListMap.get(#itemId) != null)">
										<s:set name='qty' value="#xpedxUtilBean.formatQuantityForCommas(#qty)"/>
										<p class="mil-replaced"><a href="#linkToReplacement" class="modal red" onclick='javascript:showXPEDXReplacementItems("<s:property value="#itemId"/>", "<s:property value="#id"/>", "<s:property value="#qty"/>");'>This Item has been replaced.</a></p>
									</s:if>
								</s:if>
								
									<s:if test='skuMap!=null && skuMap.size()>0 && customerSku!=null && customerSku!=""'>
										<s:set name='itemSkuMap' value='%{skuMap.get(#itemId)}'/>
										<s:set name='itemSkuVal' value='%{#itemSkuMap.get(customerSku)}'/>
										
											<p class="fields-padding"><s:if test='%{customerSku == "1"}' >
												<s:property value="#customerItemLabel" />:
											</s:if>
											<s:elseif test='%{customerSku == "2"}'>
												<s:property value="#manufacturerItemLabel" />:
											</s:elseif>
											<s:else>
												<s:property value="#mpcItemLabel" />:
											</s:else>
											
											<s:property value='#itemSkuVal' /></p>
										
									</s:if>
					
								</s:if>
								
							<div>
	                            <s:if test="%{#itemType != '99.00'}">
									<s:set name="isStocked" value="inventoryCheckForItemsMap.get(#itemId)"></s:set>
									<s:if test="#isStocked !=null">
										<s:if test='%{#isStocked !="Y"}'>
												<p class="red fields-padding" id="milltext">Mill / Mfg. Item - Additional charges may apply</p>
										</s:if>
									</s:if> 
									<s:else>
										<p class="red fields-padding" id="milltext">Mill / Mfg. Item - Additional charges may apply</p>
									</s:else>
								</s:if>
							</div>
							</div>

							<s:if test='(xpedxItemIDUOMToComplementaryListMap.containsKey(#itemIDUOM))'>
								<p class="mil-replaced"> <a class="modal red" href='javascript:showXPEDXComplimentaryItems("<s:property value="#itemIDUOM"/>", "<s:property value="#orderLineKey"/>", "<s:property  value="#orderLine.getAttribute('OrderedQty')"/>");'>Complimentary</a></p>
								<br />
							</s:if> 
							<s:if test='(xpedxItemIDUOMToAlternativeListMap.containsKey(#itemIDUOM))'>
								<p class="mil-replaced"> <a class="modal red" href='javascript:showXPEDXAlternateItems("<s:property value="#itemIDUOM"/>", "<s:property value="#orderLineKey"/>", "<s:property value="#orderLine.getAttribute('OrderedQty')"/>");'>Alternate</a></p>
								<br />
							</s:if>

							
                        </div>


						<div class="mil-action-list-wrap">

                            <table class="mil-config" width="330" border="0" cellspacing="0" cellpadding="0">
                                <s:if test='editMode == true'>
									<tr>

										<td align="right"></td>
										<td align="right"><label style="text-align: right;">Sequence: </label>
										
										<%--Hemantha
										<s:textfield name="orders" value="%{#itemOrder2}" id="itemOrder_%{#itemOrder2}" maxlength="3" 
											onkeyup="javascript:isValidQuantity(this);resetQuantityError('%{#id}');" 
											cssStyle="width: 50px;" theme="simple"/>
										--%>
									   <s:select cssClass="xpedx_select_sm" cssStyle="width: 50px; margin-right:16px;" name="orders"
											list="itemOrderList" value="itemOrder2" onfocus="this.oldvalue = this.value;" 
											onchange="onChangeItemOrder(this, this.oldvalue, this.value);"
											id="itemOrder_%{#itemOrder2}" headerKey="-1"  emptyOption="false" theme="simple"/>
										</td>
									</tr>
								</s:if>
								<tr>
                                    <td  valign="top" align="right" width="102"><label style="text-align:right;">Qty:</label></td>
                                    <td width="152" colspan="2">
                                    <s:set name='qty' value="#xpedxUtilBean.formatQuantityForCommas(#qty)"/>
                                    
										<!-- Qty --> <s:hidden
											name="itemQty" value="%{#qty}" /> <s:hidden
											id="enteredQuantities_%{#id}" name="enteredQuantities" value="%{#qty}" /> 

											<!-- UOM & Custom Fields -->
											<s:if test="%{#itemType != '99.00'}">
												<s:textfield
												title="QTY" cssClass="x-input" cssStyle="width:55px;" name="qtys" id="qtys_%{#id}"
												value="%{#qty}" onchange="javascript:isValidQuantity(this);resetQuantityError('%{#id}');updateHidden(this,'%{#id}');this.value=addComma(this.value);" theme="simple"></s:textfield>
												<s:hidden name='QTY_%{#id}' id='QTY_%{#id}' value='%{#qty}'/>
												<s:hidden
													id="enteredUOMs_%{#id}" name="enteredUOMs" value="%{#itemUomId}" />
													<s:if test="#uomList!=null" >
													<s:select cssClass="xpedx_select_sm" cssStyle="width:140px;" name="uoms" list="#uomList"
													listKey="key"
													listValue="value" 
													value="itemUomId" onchange="javascript:updateHidden(this,'%{#id}',0,'%{#_action.getJsonStringForMap(#itemUOMsMap)}');" theme="simple"/>
													</s:if>
												<s:hidden name='UOM_%{#id}' id='UOM_%{#id}' value="%{#itemUomId}"/>
											</s:if> <s:else>
												<s:textfield
												title="QTY" cssClass="x-input" cssStyle="width:55px;" name="qtys" id="qtys_%{#id}"
												value="%{#qty}" onchange="javascript:updateHidden(this,'%{#id}');isValidQuantity(this);resetQuantityError('%{#id}');this.value=addcomma(this);" theme="simple" readonly="true"></s:textfield>
												<s:hidden name='QTY_%{#id}' id='QTY_%{#id}' value='%{#qty}'/>
												<s:textfield cssClass="x-input" cssStyle="width:140px;" name="uoms" value="%{#itemUomId}" onchange="javascript:updateHidden(this,'%{#id}');" theme="simple" readonly="true"/>
												<s:hidden name='UOM_%{#id}' id='UOM_%{#id}' value=' '/>
											</s:else> 
											<%-- <div id="errorDiv_qtys_<s:property value='%{#id}' />" style="color:red;"> 
											</div> --%>
                                        </td>
                                </tr>
								
								<s:set name="mulVal" value='itemOrderMultipleMap.get(#itemId1)' />
									<%-- <s:set name="itemIdUOMsMap" value='itemIdsUOMsMap.get(#itemId1)' />
									 --%>
									 <s:set name="itemIdUOMsMap" value='itemIdConVUOMMap.get(#itemId1)' />
									 <s:iterator value='itemIdUOMsMap'>
										<s:set name='currentUomConvFact' value='value' />
										<s:hidden name='convF_%{#currentUomId}' id="convF_%{#currentUomId}" value="%{#currentUomConvFact}" />
									</s:iterator>
									
									<s:set name="defaultConvF" value='#itemIdUOMsMap.get(#itemUomId)' />
									<s:hidden name="orderLineOrderMultiple"	id="orderLineOrderMultiple_%{#id}" value="%{#mulVal}" />
									<s:hidden name="orderLineItemIDs" id="orderLineItemIDs_%{#id}" value='%{#itemId}' /> 
									<s:hidden name="UOMconversion" id="UOMconversion_%{#id}" value="%{#defaultConvF}" />
									
									<s:hidden name='customerFieldsSize_%{#id}' id='customerFieldsSize_%{#id}' value='%{#_action.getCustomerFieldsMap().size()}'/>
									<s:iterator value='customerFieldsMap' status='custFieldStatus'>
										<s:set name='FieldLabel' value='key' />
										<s:set name='FieldValue' value='value' />
										
										<s:set name='customKey'
											value='%{customerFieldsDBMap.get(#FieldLabel)}' />
										<s:set name='CustomFieldValue' value="%{' '}" />
										
										<s:if test="%{#item.getAttribute(#customKey)!=null && #item.getAttribute(#customKey)!=''}" >
										<s:set name='CustomFieldValue'
											value='%{#item.getAttribute(#customKey)}' />
										</s:if>
										<tr>
											<td align="right"><label style="text-align:right;"><s:property value="%{#FieldValue}" />:</label></td>
											<td>
												<%-- Creating text field with name as the Customer field name --%>
												<s:textfield cssStyle="width:173px;" cssClass="x-input" maxlength="25"
													name='customField%{#FieldLabel}s' id="customField%{#FieldLabel}s"
													size='10' value="%{@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getReplacedValue(#CustomFieldValue)}" 
													title="%{#FieldValue}" onchange="javascript:updateHidden(this,'%{#id}','%{#custFieldStatus.count}');"/>
												<s:hidden name='customerField_%{#custFieldStatus.count}_%{#id}' id='customerField_%{#custFieldStatus.count}_%{#id}' value='%{"Extn"+#FieldLabel+"@"+#CustomFieldValue}'/>
												<s:hidden id="entered%{#FieldLabel}_%{#id}" name="entered%{#FieldLabel}" value="%{#CustomFieldValue}" />
											</td>
										</tr>
									</s:iterator>
                                
                            </table>
                            <!--  TODO FXD2-11 Display error message  -->
                            <div class="clear"></div>
							<s:if test='editMode != true'>
                            <ul style="float: right; width: 281px; margin-right:9px;" class="tool-bar-bottom" id="tool-bar">
                                <li style="float: left; display: block; position: absolute; right: 144px; margin-right: 8px;"><a id="PAAClick_<s:property value="#id"/>" href="javascript:checkAvailability('<s:property value="#itemId"/>','<s:property value="#id"/>')" 
                                onclick="javascript:checkAvailability('<s:property value="#itemId"/>','<s:property value="#id"/>')" style="margin-left: 25px;"> 
								<span class="mil-mpna">My Price &amp; Availablity</span></a></li>
                                <%-- <li style="margin-left: 72px;"><a class="orange-ui-btn" href="javascript:addItemToCart('<s:property value="#itemId"/>','<s:property value="#id"/>')"><span>Add to Cart</span></a></li> --%>
                                <li style="margin-left: 172px;"><a class="orange-ui-btn" href="javascript:myAddItemToCart('<s:property value="#itemId"/>','<s:property value="#id"/>')"><span>Add to Cart</span></a></li>
                                 <s:if test='%{#mulVal >"1" && #mulVal !=null}'> 
	                               <li style="float: right; display: block; margin-right: 10px; margin-top: 3px; width: 225px;"> 
	                               <div class="notice" id="errorDiv_qtys_<s:property value='%{#id}' />" style="display : inline; float: right;">
	                               		<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#mulVal}"></s:property>&nbsp; 
	                               		<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#itemUomId)"></s:property>
	                               	</div>
	                               </li>
                                </s:if>
                                <li style="float: right; display: block; margin-right: 10px; width: 200px; margin-top: 5px;">
		                            <div class="error" style="display:none;" id="errorDiv_qtys_<s:property value='%{#id}' />" style="color:red"></div>
		                    	</li>
                               <br/> 
		
		                     </ul>
                            <div class="clearall"> &nbsp;</div><br></br>

                            
							</s:if>
                        </div>
                    </div>    

					<div <s:if test='%{#status.count == 1}'>class="show-hide-wrap"</s:if><s:else>class="show-hide-wrap"  style="background-color:#fafafa;"</s:else>>
                        <%-- <div id="availabilityRow_<s:property value='#id'/>"  <s:if test='%{#status.count == 1}'></s:if><s:else>style="display:none; background-color:#fafafa;"</s:else> >  --%>
                        	 <%-- Do not remove the following variables as it is used in the included jsp--%>
                        	<s:set name="itemID" value='#itemId' />
	                        <s:set name="itemKEY" value='#id' />
                        	<s:if test='#itemOrder == null' >
								<s:set name="jsonKey" value='%{#itemId}' />
							</s:if>
							<s:else>
								<s:set name="jsonKey" value='%{#itemId+"_"+#itemOrder}' />
							</s:else>
						 <div id="availabilityRow_<s:property value='#id'/>"  <s:if test='%{pnaHoverMap.containsKey(#jsonKey)}'></s:if><s:else>style="display:none; background-color:#fafafa;"</s:else> >   
                            <!-- end prefs  -->
                             <s:if test="%{pnaHoverMap.containsKey(#jsonKey)}">
	                        	<s:include value="../MyItems/XPEDXMyItemsDetailsItemAvailability.jsp"></s:include>
                        	</s:if>
                       
                        </div>
                       
                    </div>


				
                </div>
                </s:iterator>
                
             		</s:form>
			<!-- CODE_END List of Items - PN -->
             <!-- prod 1  -->

			<!-- This piece of code should be evaluated  start -->
			<div id="addToCartDlgWrapper" style="display: none;">
				<div title="Check Item Availability or Add to Cart" id="addToCartDlg">
					<s:form action="xxx" name="addToCart" id="addToCartForm"
						namespace="/xxx" method="POST">
						<table id="tableUOM">
							<tr>
								<td>
								<div id="add-to-cart-grid"></div>
								</td>
							</tr>
							<tr>
								<td><s:text name="Availability"></s:text>
								</td>
							</tr>
						</table>
						<div id="availibility-grid"></div>
						<ul id="tool-bar" class="tool-bar-bottom">
							<li><a class="grey-ui-btn" href="javascript:checkUpdateAvailability();">
							<span>Update Availability</span> </a></li>
						</ul>
						<s:hidden name='#action.name' id='xx' value='xx' />
						<s:hidden name='#action.namespace' value='/xx' />
					</s:form>
				</div>
			</div>

			<%
			/*
			<swc:dialogPanel title="Bracket Pricing" isModal="true"
				id="bracketPricingDialog" cssClass="my-class">

				<div id="Price_Grid"></div>

			</swc:dialogPanel>
			*/
			%>
			
			<!-- This piece of code should be evaluated  end -->

                    <s:if test='editMode != true'>
						<div class="clear"></div><br/>
						<fieldset class="mil-non-edit-field">
							<legend>For Selected Items:</legend>
							<input class="forselected-input" type="checkbox" id="selAll2"/>
							<%-- <a class="grey-ui-btn float-left" href="javascript:updateSelectedPAA()"><span>Update My Price &amp; Availability</span></a> --%>
							<a class="grey-ui-btn float-left" href="javascript:myUpdateSelectedPAA()"><span>Update My Price &amp; Availability</span></a>
						</fieldset>

						<ul id="tool-bar" class="tool-bar-bottom" style="width:403px; float:left; padding-top:5px; margin-left:9px;">
							<li><a class=" grey-ui-btn" href="javascript:exportList(); "><span>Export List</span></a></li>
							<s:if test="%{canEditItem}">
								<li><a class="grey-ui-btn" href="javascript:document.getElementById('formEditMode').submit();"><span>Edit This List</span></a></li>
							</s:if>
							<!--     <li><a class="modal grey-ui-btn" href="../modals/new-copy-edit-cart-details-redesign.html"><span>New List</span></a></li>-->
						</ul>
						<ul id="tool-bar float-right" class="tool-bar-bottom" style="float:right; padding-top:5px; margin-right:5px;">
							<li><a class="orange-ui-btn" href="javascript:addToCart();">
							<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
								<span>Add Items with Qty to Cart</span>
							</s:if>
							<s:else><span>Add Items with Qty to Order</span></s:else>
							</a></li>

						</ul>
					</s:if>    
					<s:else>
						<s:if test='XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems").size() > 0'>
							<fieldset class="mil-edit-field">
								<legend>For Selected Items:</legend>
	
								<input class="forselected-input-edit" type="checkbox" id="selAll2" />
								<s:if test="%{canDeleteItem}">
									<a class="grey-ui-btn float-left" href="javascript:deleteItems();"><span>Remove Items</span></a>
								</s:if>                    
							</fieldset>
							<ul id="tool-bar" class="tool-bar-bottom" style="width:503px; float:left; padding-top:5px; margin-left:9px;">
								<s:if test="%{canShare}">
								<li><a class="grey-ui-btn " href="#dlgShareList" id="dlgShareListLink2" ><span>Share List</span></a></li>
								</s:if>
								<li><a href="#dlgImportForm" id="various5"  class="grey-ui-btn"><span>Import Items</span></a></li>
							</ul>
	
							<ul id="tool-bar float-right" class="tool-bar-bottom" style="float:right; padding-top:5px; width:152px; ">
								<li><a  style="margin-left:5px;" class="green-ui-btn float-right-imp" href="javascript:saveAllItemsNew('mil-edit', ['quick-add']);"><span>Save</span></a></li>
								<li><a class="grey-ui-btn float-right-imp" href="javascript:saveAllItems();"><span>Cancel</span></a></li>																
							</ul>
						</s:if>

					</s:else>
			
                </div>
                            <div class="clearall"></div>
            <ul>
			<li style="text-align: center;">
                            
 
 
 			<div class="error" id="errorMsgBottom" style="display:none;" ></div> 
 
            
            </li>
			</ul>
            <div class="clearall"></div>
            
               
            <s:if test="%{errorMsg!=null && errorMsg!= '' && errorMsg.indexOf('ROW_PROCESSING_ERROR')>-1}">
						<s:set name='errIndex' value='%{errorMsg.indexOf("@")}' />
						<s:set name='rowNums' value='%{errorMsg.substring(#errIndex +1, errorMsg.length())}' />
						<br />
					
						<script type="text/javascript">
						//var milFileImportMsg = "Row(s) "+ '<s:property value="#rowNums" />' + " failed to import.";
						var milFileImportMsg = "<s:text name='MSG.SWC.ITEM.LISTIMPORT.ERROR.NUMROWSFAILED' /> " + '<s:property value="#rowNums" />' ;
							importItems(milFileImportMsg );
						</script>
				</s:if>
				
				
<br/>
			<s:if test='editMode != true'>
            <!-- START Carousel -->
		
		<s:if test='xpedxYouMightConsiderItems.size() > 0'>
            <div class="mil-cart-bg mil" style="margin-top:0px;">
                <div id="cross-sell" class="float-left">
                    <span class="consider-text"> You might also consider...</span>
                    <ul id="footer-carousel-left" class="jcarousel-skin-xpedx">
					
					<s:if test='xpedxYouMightConsiderItems.size() > 0'>
						<s:iterator value='xpedxYouMightConsiderItems' id='reltItem' status='iStatus'>
							<s:set name="itemAssetList"
									value='#xutil.getElementsByAttribute(#reltItem,"AssetList/Asset","Type","ITEM_IMAGE_1" )' />
								<s:if test='#itemAssetList != null && #itemAssetList.size() > 0 '>
									<s:set name="itemAsset" value='#itemAssetList[0]' />
									<s:set name='imageLocation'
										value="#xutil.getAttribute(#itemAsset, 'ContentLocation')" />
									<s:set name='imageId'
										value="#xutil.getAttribute(#itemAsset, 'ContentID')" />
									<s:set name='imageLabel'
										value="#xutil.getAttribute(#itemAsset, 'Label')" />
									<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
									<s:if test='%{#imageURL=="/"}'>
											<s:set name='imageURL' value='%{"/swc/xpedx/images/INF_150x150.jpg"}' />
									</s:if>	

<%-- 									<li> <a href="#"> <img src="<s:url value='%{#imageURL}' includeParams='none' />" width="91" height="94" alt="" />  --%>
<!-- 										<b></b><br /><br /><br /> -->
<!-- 										<br /> -->
<!-- 										</a> </li> -->

								</s:if> <s:else>
									<s:set name='imageURL' value='%{"/swc/xpedx/images/INF_150x150.jpg"}' />									
									<s:set name='info' value='XMLUtils.getChildElement(#reltItem, "PrimaryInformation")'/>
									<s:set name='shortDesc' value='#info.getAttribute("ShortDescription")'/>
									<li> 
									    <s:a cssClass="short-description" href="javascript:processDetail('%{#reltItem.getAttribute('ItemID')}', '%{#reltItem.getAttribute('UnitOfMeasure')}')"> <img src="<s:property value='%{#imageURL}'/>" title='<s:property value="%{#reltItem.getAttribute('ItemID')}"/>' width="91" height="94" alt="" /> <!-- <b><s:property value="%{#reltItem.getAttribute('ItemID')}"/></b> --><br />
										<s:property value="%{#shortDesc}"/>
											<br />
											<br />
											<br />
											</s:a> 
									</li>

								</s:else>
							
						</s:iterator>
					</s:if>

                        <!-- I will leave this piece of sampe html for reference -->
						<!-- li> <a href="#"> <img src="/swc/xpedx/images/catalog/carousel-demo-1.jpg" width="91" height="94" alt="" /> <b>Rubbermaid Ridget Can Liners</b><br />
                            <br />
                            Seemless lightweight and easy to clean.<br />
                            <br />

                            </a> </li -->
                        
                    </ul>
                </div>
            </div>
			</s:if>
        <!-- END carousel -->

			</s:if>
			            <s:set name='lastModifiedDateString' value="getLastModifiedDateToDisplay()" />
             <s:set name='lastModifiedUserId' value="lastModifiedUserId" />
             <s:set name='modifiedBy' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getLoginUserName(#lastModifiedUserId)' />
			<div class="last-modified-div">Last modified by <s:property value="#modifiedBy"/> on <s:property value="#lastModifiedDateString"/> </div> 
			

        </div>
    </div>
    <!-- end main  -->
    <!-- CODE_START Footer -PN -->
    
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	
	<!-- CODE_END Footer -PN -->

</div>
<!-- end container  -->


<div style="display: none;">

         <!-- Light Box -->
         <div style=" height:202px; width:600px; overflow:auto;">
         <s:if test='editMode == true'>
<!-- START of Hidden Layer -PN --> <!-- CODE_START Replacement items - PN -->
<!-- START: XPEDX Panel for Replacement items --> <s:set name='tabIndex'
	value='3001' /> <s:iterator value='xpedxItemIDUOMToReplacementListMap'>
	<s:set name='altItemList' value='value' />

	<div id="replacement_<s:property value='key'/>" class="xpedx-light-box">
	  <h2>Replacement Item(s) for <s:property value="wCContext.storefrontId" /> Item #: <s:property value='key'/> </h2>
         <!-- Light Box --><div style=" height:202px; width:580px; overflow:auto;border: 1px solid #CCC; border-radius: 6px;">
	       
		<s:iterator value='#altItemList' id='altItem' status='iStatus'>
		<s:if test="!#iStatus.last" >
			<div class="mil-wrap-condensed-container"  onmouseover="$(this).addClass('green-background');" onmouseout="$(this).removeClass('green-background');" >
		</s:if>
		<s:else>
			<div class="mil-wrap-condensed-container last"  onmouseover="$(this).addClass('green-background');" onmouseout="$(this).removeClass('green-background');" >
		</s:else>
            <div class="mil-wrap-condensed" style="min-height:200px;">
		
				<s:set name='uId' value='%{key + "_" +#altItem.getAttribute("ItemID")}' />
	
	<!-- begin image / checkbox   -->
                <div class="mil-checkbox-wrap">
                <s:set name='altItemIDUOM'
						value='#_action.getIDUOM(#altItem.getAttribute("ItemID"), #altItem.getAttribute("UnitOfMeasure"))' />
					<s:set name='altItemPrimaryInfo' value='#util.getElement(#altItem, "PrimaryInformation")' /> 
						<s:set name='name' value='%{#altItemPrimaryInfo.getAttribute("ShortDescription")}' /> 
						<s:set name='rItemID' value='%{#altItem.getAttribute("ItemID")}' /> 
						<s:set name='rdesc1'  value="%{#itemDescMap.get(rItemID)}"  />
						<s:set name='rdesc' value='%{#altItemPrimaryInfo.getAttribute("Description")}' />
						<s:url id='pImg' value='%{#_action.getImagePath(#altItemPrimaryInfo)}' />
						
						
						<s:set name='ritemUomId' value='#altItem.getAttribute("UomId")' />
						<s:set name='ritemType' value='#altItem.getAttribute("ItemType")' />
						
						<s:url id='ritemDetailsLink' namespace="/catalog"
							action='itemDetails.action' includeParams='none' escapeAmp="false">
							<s:param name="itemID" value="#rItemID" />
							<s:param name="sfId" value="#parameters.sfId" />
							<s:param name="unitOfMeasure" value="#ritemUomId" />
						</s:url>
                   <input name="relatedItems"
						onclick="javascript:setUId('<s:property value="#uId" />');"	type="radio" />
                    <div class="mil-question-mark"> 
                    <a href='<s:property value="%{itemDetailsLink}" />'>
                    <img src="<s:property value='%{#pImg}' />" width="150" height="150" alt="" />
                    </a>
                    </div>
                    <!--  image hardcoded  -->
                </div>
                <!-- end image / checkbox   -->
					<s:hidden name="replacement_%{uId}_itemid" value='%{#rItemID}' /> 
					<s:hidden name="replacement_%{uId}_name" value='%{#name}' /> 
					<s:hidden name="replacement_%{uId}_desc" value='%{#rdesc}' />
					
					<s:set name='altItemUomList' value='itemIdsUOMsDescMap.get(#rItemID)' />
					<s:set name="repItemUOM" value="" />
					<s:iterator value="altItemUomList" id="itemUOM" status="repItemUOMStatus">
						<s:if test="%{#repItemUOMStatus.first}" >
							<s:set name="repItemUOM" value="key" />
						</s:if>
					</s:iterator>
					<s:hidden id="replacement_%{uId}_uom" name="replacement_%{uId}_uom" value="%{#repItemUOM}" />

                <!-- begin description  -->
                <div class="mil-desc-wrap">
                    <div class="mil-wrap-condensed-desc replacement-items" style="height:auto; max-height:59px;"><s:if test="%{#ritemType != 99}">
								<a href='<s:property value="%{ritemDetailsLink}" />'>
								<s:property value="#name" />
								</a>	
							</s:if>
							</div>
                    <div class="mil-attr-wrap">
                        <ul class="mil-desc-attribute-list">                        
							<s:property value='#rdesc' escape='false'/>
					    </ul>
					    <%-- key contains the original itemId --%>
                       <%--  <p><s:property value="wCContext.storefrontId" /> Item #: <s:property value='key'/></p>
                        <p>Replacement Item #: <s:property value='rItemID' /></p> --%>
                        
<%--                         <s:if test="%{#rItemID}"> --%>
							 <p>xpedx Item #: <s:property value="#rItemID" /></p>
 
                             <p>Mfg. Item #: To be implemented </p>
<%-- 						</s:if> --%>
                        
                    </div>
                </div>
				<%-- Qty, UOM, PNA removed as per JIRA#1357--%>                
  			</div>
	</div>
  </s:iterator>
  </div>
  </div>
</s:iterator>
</s:if>
</div>
<s:form action="addComplementaryItemToCart"
	name="addReplacementItemToCartForm" id="addReplacementItemToCartForm"
	namespace="/order" method="POST">
<div id="replacementItems" style="height: 380px; display: none;">

	<s:hidden name='#action.name' id='validationActionName'
		value='addReplacementItemToCart' />
	<s:hidden name='#action.namespace' value='/order' />

	<div id="replacementItemBody"  class="xpedx-light-box"/>
	
</div> 

<!--  <div id="table-bottom-bar">-->
<!--<div id="table-bottom-bar-L"/>-->
<!--<div id="table-bottom-bar-R"/>-->
<!--</div>-->

 <ul class="tool-bar-bottom" id="tool-bar" style="margin-right:30px;float:right;">
	<li style="float: right;"><a href="javascript:replacementReplaceInList(selReplacementId);" class="orange-ui-btn modal"><span>Replace</span></a></li>
	<li style="float: right; margin-right:5px;"><a href="javascript:replacementAddToList(selReplacementId);" class="grey-ui-btn"><span>Add</span></a></li>
	<li style="float: right;"><a href="javascript:$.fancybox.close();" class="grey-ui-btn"><span>Cancel</span></a></li>
</ul>

</s:form> <s:form id="formRIAddToList" action="XPEDXMyItemsDetailsCreate"
	method="post">
	<s:hidden name="listKey" value="%{listKey}"></s:hidden>
	<s:hidden name="listName" value="%{listName}"></s:hidden>
	<s:hidden name="listDesc" value="%{listDesc}" />
	<s:hidden name="itemCount" value="%{itemCount}"></s:hidden>
	<s:hidden name="sharePermissionLevel" value="%{sharePermissionLevel}"></s:hidden>
	<s:hidden name="shareAdminOnly" value="%{shareAdminOnly}"></s:hidden>
	<s:hidden name="listOwner" value="%{listOwner}"></s:hidden>
	<s:hidden name="listCustomerId" value="%{listCustomerId}"></s:hidden>

	<s:hidden name="itemId" value="" />
	<s:hidden name="name" value="" />
	<s:hidden name="desc" value="" />
	<s:hidden name="qty" value="" />
	<s:hidden name="jobId" value=" " />
	<s:hidden name="itemType" value="1" />
	<s:hidden name="uomId" value="" />
	<s:hidden name="order" value="" />
</s:form> <s:form id="formRIReplaceInList" action="XPEDXMyItemsDetailsChange"
	method="post">
	<s:hidden name="listKey" value="%{listKey}"></s:hidden>
	<s:hidden name="listName" value="%{listName}"></s:hidden>
	<s:hidden name="listDesc" value="%{listDesc}" />
	<s:hidden name="itemCount" value="%{itemCount}"></s:hidden>
	<s:hidden name="sharePermissionLevel" value="%{sharePermissionLevel}"></s:hidden>
	<s:hidden name="shareAdminOnly" value="%{shareAdminOnly}"></s:hidden>
	<s:hidden name="listOwner" value="%{listOwner}"></s:hidden>
	<s:hidden name="listCustomerId" value="%{listCustomerId}"></s:hidden>

	<s:hidden name="key" value="" />
	<s:hidden name="itemId" value="" />
	<s:hidden name="name" value="" />
	<s:hidden name="desc" value="" />
	<s:hidden name="qty" value="" />
	<s:hidden name="jobId" value=" " />
	<s:hidden name="itemType" value="1" />
	<s:hidden name="uom" value="" />
	<!--<s:hidden name="order" 		value="" /> -->
</s:form></div>

<div class="hp-ad"><!-- iframe width="" height="" noresize
	scrolling=No frameborder=0 marginheight=0 marginwidth=0
	src="https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/52891/0/vh?z=xpedx&kw=&click="><script
	language=JavaScript
	src="https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/52891/0/vj?z=xpedx&kw=&click=&abr=$scriptiniframe"></script>
<noscript><a
	href="https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/52891/0/cc?z=xpedx"><img
	src="https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/52891/0/vc?z=xpedx&kw=&click=&abr=$imginiframe"
	width="" height="" border="0"></a></noscript>
</iframe --></div>
<!-- END: XPEDX Panel for Replacement items -->

<!-- CODE_END Replacement items - PN-->

<!-- END of Hidden Layer -PN -->
<script type="text/javascript">
		function updateValidation(){
			$(".numeric").numeric();	
		}
		updateValidation();
	</script>

</body>
</html>
