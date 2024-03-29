<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean id="wcUtil" name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en"
	xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<%
	request.setAttribute("isMergedCSSJS", "true");
%>
<s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />
<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
<s:set name='currentCartInContextOHK' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("OrderHeaderInContext")' />
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name="isEstUser" value='%{#xpedxCustomerContactInfoBean.isEstimator()}' />
<s:url id='uomDescriptionURL' namespace="/common" action='getUOMDescription' />
<s:hidden name="uomDescriptionURL" value='%{#uomDescriptionURL}' />

<s:bean id="xpedxUtilBean" name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean" />
<s:bean id='xutil' name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils' />

<s:set name='ItemSize' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("listOfItemsSize")' />

<s:bean
	name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils'
	id='xpedxSCXmlUtil' />
<s:set name='outDoc2' value='%{outDoc.documentElement}' />
<script type="text/javascript">
	var isUserAdmin = <s:property value="#isUserAdmin"/>;
	var isEstUser = <s:property value="#isEstUser"/>;
</script>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<!-- carousel scripts js   -->

<s:set name="customerPONoFlag" value='%{customerFieldsMap.get("CustomerPONo")}'></s:set>
<s:set name="jobIdFlag" value='%{customerFieldsMap.get("CustLineAccNo")}'></s:set>
<s:set name="custPONo" value='%{customerFieldsMap.get("CustomerPONo")}'></s:set>

<!-- begin styles. These should be the only three styles. -->

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ie-hacks<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<!-- end styles -->

<!-- For the number formatting -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MIL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]> 
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-ie-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" /> 
<![endif]--> 

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<s:include value="../order/XPEDXRefreshMiniCart.jsp" />

<script type="text/javascript">
	// toggle thumbnails on/off
	$(document).ready(function() {
		var showHideImages = function(visible) {
			$main = $('#main');
			if (visible) {
				$main.removeClass('hide-item-thumbnails');
				
				// the first time we show the images we need to load the images
				if ($main.hasClass('lazy-load-thumbnails')) {
					$main.removeClass('lazy-load-thumbnails');
					
					setTimeout(function() {
						var lazyLoadImages = $('.item-thumbnail');
						for (var i = 0, len = lazyLoadImages.length; i < len; i++) {
							lazyLoadImages[i].src = lazyLoadImages[i].getAttribute('data-src');
						}
					}, 0);
				}
				
			} else {
				$main.addClass('hide-item-thumbnails');
			}
		};
		
		$viewHideImages = $('.view-hide-images');
		$viewHideImages.click(function() {
			$btn = $viewHideImages.find('.viewbtn');
			if ($btn.hasClass('hidebtn')) {
				$btn.removeClass('hidebtn');
				showHideImages(false);
			} else {
				$btn.addClass('hidebtn');
				showHideImages(true);
			}
			return false;
		});
		
		// price and availability links
		$('.paaLink').click(function() {
			myUpdateSelectedPAA();
			return false;
		});
		
		// remove item links
		$('.removeItemsLink').click(function() {
			deleteItems();
			return false;
		});
		
		// add items with qty to cart button
		$('.btn-add-items-qty-to-cart').click(function() {
			addToCart();
			return false;
		});
	});
		
</script>

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
	<title>
		<s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.MIL.MYITEMLISTS.GENERIC.TABTITLE" /> / <s:text name="myitemsdetails.editable.title" />
	</title>
	<meta name="DCSext.w_x_list_edit" content="1" />
</s:if>
<s:else>
	<title>
		<s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.MIL.MYITEMLISTS.GENERIC.TABTITLE" /> / <s:text name="myitemsdetails.noneditable.title" />
	</title>
</s:else>

<!-- Web Trends tag start -->

<s:if test="%{updatePAMetaTag}">
	<meta name="DCSext.w_x_sc" content="1" />
	<meta name="DCSext.w_x_scr" content="<s:property value='%{strItemIds}'/>" />
</s:if>

<s:if test='%{#session.metatagName != null}'>
	<meta name="<s:property value='%{#session.metatagName}' />" content="<s:property value='%{#session.metatagValue}' />" />
	<s:set name="metatagName" value="<s:property value=null />" scope="session" />
	<s:set name="metatagValue" value="<s:property value=null />" scope="session" />
</s:if>
<!-- Web Trends tag end -->

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

#errorMsgBottom {
	margin: 20px 30px 20px 20px;
}
</style>

<script type="text/javascript">
	var availabilityURL = '<s:property value="#availabilityURL"/>';
	var addToCartURL = '<s:property value="#addToCartURL"/>';
	var divId;
	var isGlobal;
	var errorflag;
	var addToCartFlag;
	var validAddtoCartItemsFlag  = new Array();
	// EB-3973 share private list is not displayed and  the locations are not shown if private list is checked.
	function hideSharedListFormIfPrivate() {
		 var shareprivateflg = document.getElementById("XPEDXMyItemsDetailsChangeShareList_sharePrivateFlag").value;		 
		//alert(test1.trim()!= "");
		if(shareprivateflg.trim()!= ""){			
			hideSharedListForm();
		}
		else{
			showSharedListForm();
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
		
		/* Begin Short desc. shortener 
		$('.short-description').each(function() {
			var html = $(this).html();
			var shortHTML = html.substring(0, 90);
			if( html.length > shortHTML.length )
			{
				$(this).html(shortHTML);
				$(this).append('...');	
				$(this).attr('title', html );
			}
		}); */
		
		/* EB-783 */
		$('.full-description-replacement-model').each(function() {
			var html = $(this).html();
			var shortHTML = html.substring(0, 175);
			if( html.length > shortHTML.length )
			{
				$(this).html(shortHTML);
				$(this).append('...');	
				$(this).attr('title', html );
			}
		});
		
		
		/* Begin long desc. shortener 
		$('.prodlist ul li, #prodlist ul li').each(function() {
			var html = $(this).html();
			var shortHTML = html.substring(0, 30);
			if( html.length > shortHTML.length )
			{
				$(this).html(shortHTML);
				$(this).append('...');	
				$(this).attr('title', html );
			}
		}); */
		
		<%-- fancybox workaround: fancybox only works with 'a' tag so programatically click it when the button is clicked --%>
		$('.btn-share-list').click(function() {
			$('#dlgShareListLink').click();
		});
		$("#dlgShareListLink").fancybox({
			'onStart' 	: function(){
			if (isUserAdmin || isEstUser){			
				//Calling AJAX function to fetch 'Ship-To' locations only when user is an Admin
				showShareList('<s:property value="#CurrentCustomerId"/>', true);
				hideSharedListFormIfPrivate();
				}
			},
			'onClosed':function() {				
				
				$("#XPEDXMyItemsDetailsChangeShareList #spShareAdminOnly").attr("checked",false);						
				 if($("#XPEDXMyItemsDetailsChangeShareList #rbPermissionPrivate").is(':checked')){
			  		document.getElementById("dynamiccontent").style.display = "none";
				 }
				else
				{
					// share private variable will be populated if it is a private list
					//based on that, the private is selected in the pop up and the locations are not shown
					if(sharePrivateVar!=null && sharePrivateVar != "") {
						document.getElementById("dynamiccontent").style.display = "none";
					}
					else {							
						document.getElementById("dynamiccontent").style.display = "block";
					}
				}
				shareSelectAll(false);
			}
		});
	
		<%-- fancybox workaround: fancybox only works with 'a' tag so programatically click it when the button is clicked --%>
		$('.btn-import-items').click(function() {
			$("#dlgImportFormLink").click();
		});
		$("#dlgImportFormLink").fancybox({
			'onClosed' : function(){
			document.getElementById("errorMsgForBrowsePath").style.display = "none";
			document.getElementById("errorMsgForRequiredField").innerHTML = "";
			document.getElementById("errorMsgForRequiredField").style.display = "none";
			if(document.getElementById("File"))
				document.getElementById("File").value = "";
			},
			'autoDimensions'	:false,
			'width'				: 620,
			'height'			: 375
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
	});	
	var priceCheck;
        	function checkAvailability(itemId,myItemsKey) {
     		priceCheck = true;
     		addToCartFlag = false;
     		//added for jira 3974
     		showProcessingIcon();
     		clearPreviousDisplayMsg()
     		
     		//Added for XB 224 - not to display availability when Item is not Entitled		
     		var erroMsg = '<s:property value='erroMsg' />';
     		var isItemEntitled=true;
			if(erroMsg != null && erroMsg != ""){
				var errorItemList = erroMsg.split(",");
				for (var j = 0; j < errorItemList.length; j++)
						{
						if(errorItemList[j] == itemId){
							isItemEntitled=false;							
							
						}	
						}
				
				}
			
     		var validateOM ; 
     		if(validateOrderMultiple(true,myItemsKey) == false)
   			 {
     				validateOM = false;
   			 }
     		else{
     				validateOM = true;
     			}
     		if(isItemEntitled){
     			if(itemId == null || itemId =="") {
    				alert("Item ID cannot be null to make a PnA call");
    			}
    			var url = document.getElementById("checkAvailabilityURLHidden");
    			var qty = document.getElementById('QTY_'+myItemsKey).value;
    		    var qtyTextBox = qty;
    			//XB 214 BR4
    			if(url != null && validateOM == true) {
    				//Added for EB 2034
    			        var uomConvFactor;
				var orderMul = document.getElementById("orderLineOrderMultiple_"+myItemsKey);
				var conversionFactor = document.getElementById("UOMconversion_"+myItemsKey);
				if(conversionFactor!=null && conversionFactor!=undefined){
				 	uomConvFactor = document.getElementById("UOMconversion_"+myItemsKey).value;
				}
    				if(qty == "" || qty == null || qty == "null"){
    					var uom = document.getElementById("enteredUOMs_"+myItemsKey).value;
    					if(orderMul != null && orderMul.value != 0 && uomConvFactor != 0 && conversionFactor != null ){
    						if(uomConvFactor == 1){
    							qty = document.getElementById("orderLineOrderMultiple_"+myItemsKey).value;
    						}
    						else if(uomConvFactor <= orderMul.value){
    								if((orderMul.value % uomConvFactor)==0){
    									qty = orderMul.value / uomConvFactor;
    								}
    								else{
    									qty = 1;
    								}
    					    }
    						else{//if conversionFactor is greater than OrderMul irrespective of the moduloOf(conversionFactor,OrderMul) is a whole number / decimal result we set the Qty to 1
        						qty = 1;
        					}
    					}
    				}// End of EB 2034
    				else{
        		 		qty = document.getElementById('QTY_'+myItemsKey).value;
        		 		var uom = document.getElementById('UOM_'+myItemsKey).value;
    				}
    				var customerUom = document.getElementById('custUOM_'+myItemsKey).value;
    	     		
    				displayAvailability(itemId,qty,uom,myItemsKey,url.value,validateOM,customerUom,qtyTextBox);
    			} 
    			else{
            		hideProcessingIcon();
         		}
     		}
     		else{
        		hideProcessingIcon();
     		}
     }
		
		function displayImportErrorMessage(msgImportMyItemsError){
			//Clears previous messages if any
			clearPreviousDisplayMsg()
     		
			$('#errorMsgTop,#errorMsgBottom').html(msgImportMyItemsError).show();
			//document.getElementById("errorMsgTop").innerHTML = msgImportMyItemsError ;
            //document.getElementById("errorMsgTop").style.display = "inline"; 
                        
            //document.getElementById("errorMsgBottom").innerHTML = msgImportMyItemsError ;
            //document.getElementById("errorMsgBottom").style.display = "inline"; 
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

				<s:url id='deleteListLink' action='MyItemsDetailsDelete.action' >
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
			updateSelectedPAA( );
		}
		
		//added for jira 3742
		function setFocus(component,e){
			var characterCode
			if(e && e.which){ // NN4 specific code
				e = e
				characterCode = e.which
			}
			else {
				e = event
				characterCode = e.keyCode // IE specific code
			}
			if(characterCode == 9){
		    		component.select();
		    		component.focus();
				return;
			}
			 return true;
		}	

		var addToCartFlag;
		//Resets the Messages and calls the actual javascript function
		function myAddItemToCart(itemId, id){
			//added for jira 3974
			showProcessingIcon();
			//Added isGlobal for Jira 3770
			isGlobal = true;
			addToCartFlag = true;
			//Clear previous messages if any
			clearPreviousDisplayMsg();
			addItemToCart(itemId, id );
		}
		var isAdd2List=false;
		function add2List(){
			var itemCount = "<s:property value='XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems").size()'/>";
			var itemCountNum = Number(itemCount);
			var storeFront='<s:property value="wCContext.storefrontId" />';
			if(itemCount<=200){
				if((itemCountNum+document.getElementById('qaTable').rows.length)<=200){
					//Added For Jira 3197
					if(document.getElementById('qaTable').rows.length==0){
						document.getElementById("errorMsgFor_QL").style.display = "inline";
						return false;
					}
					var formItemIds = document.getElementById("formAdd2List");
					if (formItemIds){
						//form already submitted.
						if (Ext.get("btnQLAdd2Cart").dom.disabled){
							return;
						}
					
						formItemIds.listName.value 	= Ext.get("listName").dom.value;
						formItemIds.listDesc.value 	= Ext.get("listDesc").dom.value;
						//Added for JIRA 3642
						formItemIds.itemCount.value=itemCount; 
						
						formItemIds.action="/swc/myItems/MyItemsDetailsQuickAdd.action?sfId="+storeFront+"&scFlag=Y";
						formItemIds.submit();
                        if(!isAdd2List && /Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent))

                        {
                                isAdd2List =true;
                                add2List();
                        }
                       
						
					} else {
						alert("There is a problem in this page. Form formAdd2List is missing.");
					}
				}
				else{
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
			
			<s:url id='saveAllLink' action='MyItemsDetailsChange.action' escapeAmp='false' >
			</s:url>
			
			if (formItemIds){
				formItemIds.action = "<s:property value='%{saveAllLink}' escape='false'/>";
				formItemIds.editMode.value = "true";
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

		//added for jira 3057
		function cancelChanges(){
			var formItemIds = document.getElementById("formItemIds");
			<s:url id='cancelchange' action='MyItemsDetails' escapeAmp='false' >
			</s:url>
			if (formItemIds){
				formItemIds.action = "<s:property value='%{cancelchange}' escape='false'/>";
				formItemIds.editMode.value = "false";
				formItemIds.listName.value = Ext.get("listName").dom.value;
				formItemIds.listDesc.value = Ext.get("listDesc").dom.value;
				try{ console.log("Action: " + formItemIds.action); }catch(ee){}
				try{ console.log("Key: " 	+ formItemIds.listKey.value); }catch(ee){}
				try{ console.log("Desc: " 	+ formItemIds.listDesc.value); }catch(ee){}
				formItemIds.submit();
			}	
		}
		//end of jira 3057
		
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
	           <s:url id='qaAddItem' includeParams='none' action='MyItemsDetailsQuickAdd' />
	           var flag = false;
	           var jobIdFlag = false;
	           var jobIdStr = "<s:property value='%{customerFieldsMap.get("CustLineAccNo")}'/>";
	           if(jobIdStr != null && jobIdStr != "")
		           jobIdFlag = true;
	           var customPOFlagstr = "<s:property value='%{customerFieldsMap.get("CustomerPONo")}'/>";
	           if(customPOFlagstr != null && customPOFlagstr != "")
	           	flag = true;
	           var url = "<s:property value='#qaAddItem'/>";
	           url = ReplaceAll(url,"&amp;",'&');
	           //Show the waiting box
	           var x = document.getElementById(divId);
	           x.innerHTML = "Validating item " + itemId;
	           
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
		//Added For Jira 3946
		function setMsgOnAddItemsWithQtyToCart(response){
				refreshMiniCartLink();
			if(response.responseText != null && response.responseText.indexOf('Error while adding to XPEDXMyItemsAdd To Cart ') !== -1 )
     	   {
     		   alert("There is a problem adding the items to the cart. Please try again or contact your administrator.");
     	   }
     	   else
     	   {
	         	   var addedItems = new Array();
	         	   var arrQty = new Array();
	         	  var arrUOM = new Array();
	         	 arrUOM =  document.getElementsByName("enteredUOMs");
	         	   addedItems = document.getElementsByName("enteredProductIDs");
	    				arrQty = document.getElementsByName("qtys");
	    				for(var i = 0; i < addedItems.length; i++){
	    					//alert("arrQty[i].value= "+ arrQty[i].value);
	    					if(validAddtoCartItemsFlag[i]== true ){
	    					divId='errorDiv_'+ arrQty[i].id; 
	    					//EB-44
	    					 var uid = arrQty[i].id;
	    					uid = uid.substring(5);   
	    					var enteredQty = document.getElementById('QTY_'+uid).value;
	    					var selectedUomDesc = document.getElementById('UOM_desc_'+uid).value;
	    					document.getElementById('qtys_' + uid).value = document.getElementById('initialQTY_' + uid).value;
							document.getElementById('QTY_'+uid).value = document.getElementById('initialQTY_' + uid).value;
							document.getElementById('enteredQuantities_'+uid).value = document.getElementById('initialQTY_' + uid).value; 						    					
	 						var divVal=document.getElementById(divId); 
	    					    					
	    					if(response.responseText.indexOf(addedItems[i].value+"_"+arrQty[i].value+"_"+arrUOM[i].value) !== -1){
	    						divVal.innerHTML = enteredQty+" "+selectedUomDesc+" "+" has been added to your cart. Please review the cart to update the item with a valid quantity." ;
	    						divVal.setAttribute("class", "error");
	    					}
	    					else{
	    						divVal.innerHTML =  enteredQty+" "+selectedUomDesc+" "+" has been added to cart." ;
	    						//divVal.innerHTML =  " It has been added to cart." ;
	    						 divVal.setAttribute("class", "success");
	    					}
							  divVal.style.display = "inline-block"; 
							  divVal.setAttribute("style", "margin-right:5px;float:right;");
							  
							  $('#uoms_'+ uid).val(document.getElementById('initialUOM_key_'+ uid).value).change();
							 
	    					
	    				}
	     	   }
			}
     	}
		var addItemsWithQty;
		function addToCart(){
			//added for jira 3974
			showProcessingIcon();
			addItemsWithQty = true;
			isGlobal = false;
			clearPreviousDisplayMsg();
			
			resetQuantityError();
			 if( validateOrderMultipleFromAddQtyToCart(false,null) == false)
			 {	
				 //Added displayMsgHdrLevelForLineLevelError() here for displaying error msg when Add Item with Qty To cart
				 //displayMsgHdrLevelForLineLevelError ();
				 if(addToCartFlag == false){
					 hideProcessingIcon();
			         return;
				 }
					
			 }
			var formItemIds 	= document.getElementById("formItemIds");
			
			try{
			var selCart = '<s:property value="#currentCartInContextOHK" />';
			}catch(ee){
				selCart = "_CREATE_NEW_";
			}
			
		    <s:url id='addToCartLink' action='XPEDXMyItemsDetailsAddToCart.action'>		   
		    </s:url>
		
			if (formItemIds){
				formItemIds.action = "<s:property value='%{addToCartLink}' escape='false'/>";
				formItemIds.orderHeaderKey.value = selCart;
				//START - Submit the form via ajax
                var URL = "<s:property value='%{addToCartLink}' escape='false'/>" + "&validItemFlagArray=" +validAddtoCartItemsFlag;
                if( isAddToCart == true)
                {
	                 Ext.Ajax.request({
	                   url: URL,
	                   form: 'formItemIds',
	                   method: 'POST',
	                   //Fix for Jira 3946
	                   success: function (response, request){
	   		        	var draftErr = response.responseText;
	   		            var draftErrDiv = document.getElementById("errorMessageDiv");
	   		            if(draftErr.indexOf("This cart has already been submitted, please refer to the Order Management page to review the order.") >-1)
	   		      		  {			
		            	    refreshWithNextOrNewCartInContext();
		                    draftErrDiv.innerHTML = "<h5 align='center'><b><font color=red>" + response.responseText + "</font></b></h5>";
		                    hideProcessingIcon();
		       			 }
	  		     	    else if(draftErr.indexOf("We were unable to add some items to your cart as there was an invalid quantity in your list. Please correct the qty and try again.") >-1)
	   		     		 {			
		            	    refreshWithNextOrNewCartInContext();
		                    draftErrDiv.innerHTML = "<h5 align='center'><b><font color=red>" + response.responseText + "</font></b></h5>";
		                    hideProcessingIcon();
		       			 }
	   		         	else if(draftErr.indexOf("Exception While Applying cheanges .Order Update was finished before you update") >-1)
		             	{
						 	var orderHeaderKey=document.getElementById("editOrderHeaderKey").value;
			        	 	var orderdetailsURL=document.getElementById('orderdetailsURLId').value+'&isErrorMessage=Y&orderHeaderKey='+orderHeaderKey;				        	 
			        		 orderdetailsURL = ReplaceAll(orderdetailsURL,"&amp;",'&');
			        	 	window.location=orderdetailsURL;//"orderDetail.action?sfId=<s:property value="wCContext.storefrontId" />&orderHeaderKey=<s:property value="#orderHeaderKey" />&scFlag=Y";
		            	 }
	   		            else{
	                	   setMsgOnAddItemsWithQtyToCart(response);  
	                	   draftErrDiv.innerHTML="";
	                	   hideProcessingIcon();
	   		         	}
	                   },
	                   failure: function (response, request){
	                	   setMsgOnAddItemsWithQtyToCart(response);
	                	   hideProcessingIcon();
	                	}
	               });    
	                } 
   	                
				//END - Submit the form via ajax
				
				
			} else {
				hideProcessingIcon();
				alert("There is a problem in this page. Form formItemIds is missing.");
			}
		}
		function resetQuantityError(uId)
		{
			var divId = 'errorDiv_qtys_'+uId;
			var divVal=document.getElementById(divId);
			if(divVal!=null)
				divVal.innerHTML='';
			divVal.style.display = 'none';
		}
		
		function resetQuantityError()
		{
			var arrQty = new Array();
			arrQty = document.getElementsByName("qtys");
			for(var i = 0; i < arrQty.length; i++)
			{
				document.getElementById(arrQty[i].id).style.borderColor="";
				
				var divId='errorDiv_'+	arrQty[i].id;
				var divVal=document.getElementById(divId);
				divVal.innerHTML='';
				divVal.style.display = 'none';
			}
		}
		

		//// Function Start - Jira 3770
		var isAddToCart=true;
		function validateOrderMultipleFromAddQtyToCart(isOnlyOneItem,listId){
			var itemCount = "<s:property value='XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems").size()'/>";
			//Adding this if block If MIL has 1 item
			if(itemCount == 1){
				isAddToCart=true;
				var arrQty;
				var arrUOM;
				var arrItemID;
				var arrOrdMul;
				var baseUOM;
				arrQty = document.getElementById("formItemIds").elements["qtys"];
				arrUOM = document.getElementById("formItemIds").elements["UOMconversion"];
				arrItemID = document.getElementById("formItemIds").elements["orderLineItemIDs"];
				arrOrdMul = document.getElementById("formItemIds").elements["orderLineOrderMultiple"];
				baseUOM = document.getElementById("formItemIds").elements["baseUOM"];
				
			errorflag=true;
			addToCartFlag=false;
			var isQuantityZero = true;
			var uomCheck = false ;
						
			validAddtoCartItemsFlag[0]=true;
			divId='errorDiv_'+	arrQty.id;
			var divVal=document.getElementById(divId);

			var quantity = arrQty.value;
			quantity = ReplaceAll(quantity,",","");
			// XB-224 start
			var erroMsg = '<s:property value='erroMsg' />';//Added for XB- 224
			if(erroMsg != null && erroMsg != ""){
			        document.getElementById("errorMsgTop").innerHTML = "Item # "+erroMsg+" is currently not valid. Please delete it from your list and contact Customer Service.";
			        document.getElementById("errorMsgTop").style.display = "inline";
						
			        document.getElementById("errorMsgBottom").innerHTML = "Item # "+erroMsg+" is currently not valid. Please delete it from your list and contact Customer Service.";
			        document.getElementById("errorMsgBottom").style.display = "inline";
					errorflag= true;
					isAddToCart=false;
					hideProcessingIcon();
			}
			//XB 224 end

			if (priceCheck == true && addItemsWithQty != true){
				if(quantity == '0'|| quantity == '')
				quantity = 1;
			}
			
			//Changed to || if((quantity == '0' || quantity== '' ) && isOnlyOneItem == true) JIRA 3197
			//alert("isGlobal==="+ isGlobal);
			if(isGlobal == true){
				
				if(quantity == '0' || quantity== '' ){
						//Display Generic Message at Header level first then Update Line Level message.
					
					/* divVal.innerHTML='Qty Should be greater than 0'; */
						divVal.innerHTML="<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGTZERO' />";
						divVal.setAttribute("class", "error");
						divVal.style.display = 'block';
						document.getElementById(arrQty.id).style.borderColor="#FF0000";
						hideProcessingIcon();
					}
				isQuantityZero = false;
			}
			if((quantity == '0' || quantity== '' ) )
			{  validAddtoCartItemsFlag[0]=false;
				if((arrOrdMul.value!=null || arrOrdMul.value!='') && arrOrdMul.value>1)
				{
					
					//alert("WE are in if block of quantity == '0'");
					//divVal.innerHTML="You must order in units of "+ arrOrdMul[i].value+", please review your entry and try again.";
					divVal.innerHTML= " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul.value) + " "+baseUOM.value;
					divVal.setAttribute("class", "notice");
					divVal.style.display = 'block';
					//document.getElementById(arrQty[i].id).style.borderColor="#FF0000";
					errorflag= false;						
				}
			}
			
			else if(quantity>0){
				var totalQty = arrUOM.value * quantity;
				if(arrOrdMul.value == undefined || arrOrdMul.value.replace(/^\s*|\s*$/g,"") =='' || arrOrdMul.value == null)
				{
					arrOrdMul.value=1;
				}
				var ordMul = totalQty % arrOrdMul.value;
				isQuantityZero = false;
				
				if(addItemsWithQty == true){
					addToCartFlag = true;
					validAddtoCartItemsFlag[0]=true;
					if(arrOrdMul.value > 1 && isAddToCart == true)
					{
						divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul.value) +" "+baseUOM.value ;
						divVal.setAttribute("class", "notice");
						divVal.style.display = 'block';
					}
					else if(arrOrdMul.value >1 && ordMul == 0)
					{
						divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul.value) +" "+baseUOM.value ;
						divVal.setAttribute("class", "notice");
						divVal.style.display = 'block';
						
					}
				}
				else if (arrOrdMul.value > 1 && priceCheck == true){
					
					if(priceCheck == true){
					divVal.innerHTML= " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul.value) + " "+baseUOM.value;
					divVal.setAttribute("class", "notice");
					divVal.style.display = 'block';
					}
					else {
						divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul.value) +" "+baseUOM.value ;
						divVal.setAttribute("class", "notice");
						divVal.style.display = 'block';
						
						}
					
					
				}	
			}
		}// End of If block
			else{
				isAddToCart=true;
				var arrQty = new Array();
				var arrUOM = new Array();
				var arrItemID = new Array();
				var arrOrdMul = new Array();
				var baseUOM = new Array();
				arrQty = document.getElementsByName("qtys");
					arrUOM = document.getElementsByName("UOMconversion");
					arrItemID = document.getElementById("formItemIds").elements["orderLineItemIDs"];
					arrOrdMul =  document.getElementsByName("orderLineOrderMultiple");
					baseUOM = document.getElementsByName("baseUOM");
				
				errorflag=true;
				addToCartFlag=false;
				var isValidItemError=false;
				var isQuantityZero = true;
				var uomCheck = false ;
				// XB-224 start
				var erroMsg = '<s:property value='erroMsg' />';
				var errorItemList = erroMsg.split(",");
				var unEntitledItemIDs = new Array();
				var n=0;
				for(var i = 0; i < arrItemID.length; i++)
				{	
					
					validAddtoCartItemsFlag[i]=true;
					divId='errorDiv_'+	arrQty[i].id;
					var divVal=document.getElementById(divId);
	
					var quantity = arrQty[i].value;
					quantity = ReplaceAll(quantity,",","");
					var itemID = arrItemID[i].value;
	
					if (priceCheck == true && addItemsWithQty != true){
						if(quantity == '0'|| quantity == '')
						quantity = 1;
					}
					
					if(isGlobal == true){
						
						if(quantity == '0' || quantity== '' ){
							divVal.innerHTML="<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGTZERO' />";
							divVal.setAttribute("class", "error");
							divVal.style.display = 'block';
							document.getElementById(arrQty[i].id).style.borderColor="#FF0000";
							//Ctrl.focus();
						}
						isQuantityZero = false;
					}
					if((quantity == '0' || quantity== '' ) )
					{  validAddtoCartItemsFlag[i]=false;
						if((arrOrdMul[i].value!=null || arrOrdMul[i].value!='') && arrOrdMul[i].value>1)
						{
							divVal.innerHTML= " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul[i].value) + " "+baseUOM[i].value;
							divVal.setAttribute("class", "notice");
							divVal.style.display = 'block';
							errorflag= false;						
						}
					}
					else if(quantity>0){
						//XB 224
						for (var j = 0; j < errorItemList.length; j++)
						{						
							if(errorItemList[j] == itemID){		
								n++;
								unEntitledItemIDs[j] = itemID;	
							}
						}
						var totalQty = arrUOM[i].value * quantity;
						if(arrOrdMul[i].value == undefined || arrOrdMul[i].value.replace(/^\s*|\s*$/g,"") =='' || arrOrdMul[i].value == null)
						{
							arrOrdMul[i].value=1;
						}
						var ordMul = totalQty % arrOrdMul[i].value;
						isQuantityZero = false;
						
						if(addItemsWithQty == true){
							addToCartFlag = true;
							validAddtoCartItemsFlag[i]=true;
							if(arrOrdMul[i].value > 1 && isAddToCart == true)
							{
								divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul[i].value) +" "+baseUOM[i].value ;
								divVal.setAttribute("class", "notice");
								divVal.style.display = 'block';
							}
							else if(arrOrdMul[i].value >1 && ordMul == 0)
							{
								divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul[i].value) +" "+baseUOM[i].value ;
								divVal.setAttribute("class", "notice");
								divVal.style.display = 'block';
								
							}
						}
						else if (arrOrdMul[i].value > 1 && priceCheck == true){
							
							if(priceCheck == true){
								divVal.innerHTML= " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul[i].value) + " "+baseUOM[i].value;
								divVal.setAttribute("class", "notice");
								divVal.style.display = 'block';
							}
							else {
								divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul[i].value) +" "+baseUOM[i].value ;
								divVal.setAttribute("class", "notice");
								divVal.style.display = 'block';
								
							}
						}	
					}	
				}
			
				if(unEntitledItemIDs.length == 0){
					
				}else if(unEntitledItemIDs.length != 0){
					if(n==1){
						var str = ""+unEntitledItemIDs;
						var str2 = ReplaceAll(str,",","");
						unEntitledItemIDs = str2;
					}
					document.getElementById("errorMsgTop").innerHTML = "Item # "+unEntitledItemIDs+" is currently not valid. Please delete it from your list and contact Customer Service.";
			        document.getElementById("errorMsgTop").style.display = "inline";
			        document.getElementById("errorMsgBottom").innerHTML = "Item # "+unEntitledItemIDs+" is currently not valid. Please delete it from your list and contact Customer Service.";
			        document.getElementById("errorMsgBottom").style.display = "inline";
					errorflag= false;
					isAddToCart=false;
					isValidItemError=true;
				
				}
			} // End of else . This else is for if itemCount==1
			
			if(uomCheck == true)
			{
				errorflag= false;
			}	
			if(isQuantityZero == true)
			{
				document.getElementById("errorMsgTop").innerHTML = "No items with quantity defined. Please review the list and try again." ;
	            document.getElementById("errorMsgTop").style.display = "inline";
	            
	            document.getElementById("errorMsgBottom").innerHTML = "No items with quantity defined. Please review the list and try again." ;
	            document.getElementById("errorMsgBottom").style.display = "inline";
				errorflag= false;
			}
			if(isValidItemError)
			{
				hideProcessingIcon();
			}
			return errorflag;
			
		}
		
		function validateOrderMultiple(isOnlyOneItem,listId)
		{
			var arrQty = new Array();
			var arrUOM = new Array();
			var arrItemID = new Array();
			var arrOrdMul = new Array();
			var baseUOM = new Array();
			var checkItemKeys = new Array();
			
			if(isOnlyOneItem != undefined && isOnlyOneItem == true)
			{	
				arrQty[0]=document.getElementById("qtys_"+listId);
				arrUOM[0]=document.getElementById("UOMconversion_"+listId);
				arrItemID[0]=document.getElementById("orderLineItemIDs_"+listId);
				arrOrdMul[0]=document.getElementById("orderLineOrderMultiple_"+listId);
				baseUOM[0]=document.getElementById("baseUOM_"+listId);
			}
			else
			{	
				arrQty = document.getElementsByName("qtys");
				arrUOM = document.getElementsByName("UOMconversion");
				arrItemID = document.getElementById("formItemIds").elements["orderLineItemIDs"];
				arrOrdMul =  document.getElementsByName("orderLineOrderMultiple");
				baseUOM = document.getElementsByName("baseUOM");
			}
			checkItemKeys = document.getElementsByName("checkItemKeys");
			var erroMsg = '<s:property value='erroMsg' />';
			var errorflag=true;
			var isQuantityZero = true;
			var uomCheck = false ;
			var count=0;
			for(var i = 0; i < arrItemID.length; i++)
			{	
			if(isOnlyOneItem || checkItemKeys[i].checked){	
				divId='errorDiv_'+	arrQty[i].id;
				var errorMsgFlag = false;
				var divVal=document.getElementById(divId);

				var quantity = arrQty[i].value;
				quantity = ReplaceAll(quantity,",","");
				var itemID = arrItemID[i].value;				
				if(erroMsg != null && erroMsg != "")
				{
					if(divId != null)
					{		
						var errorItemList = erroMsg.split(",");
						for (var j = 0; j < errorItemList.length; j++)
						{
							if(errorItemList[j] == itemID)
							{
								divVal.innerHTML="Item # "+itemID+" is currently not valid. Please delete it from your list and contact Customer Service.";
								divVal.setAttribute("class", "error");
								divVal.style.display = 'block';
								errorMsgFlag = true;
								if(isOnlyOneItem){
									errorflag= false;
								}
								else{
									errorflag= true;
								}
								isAddToCart=false;
							}
						}
					}
				}

				if (priceCheck == true && (addToCartFlag == false || addToCartFlag == undefined)){
					// added for XB 214 BR2
					if(quantity == ''){
						if(arrOrdMul[i].value!=null || arrOrdMul[i].value!='')
							quantity = arrOrdMul[i].value; 
					}
				}
				
				if(isGlobal == true){
					
					if(quantity == '0' || quantity== '' ){
						divVal.innerHTML="<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGTZERO' />";
						divVal.setAttribute("class", "error");
						divVal.style.display = 'block';
						document.getElementById(arrQty[i].id).style.borderColor="#FF0000";
						//Ctrl.focus();
						errorflag= false;
					}
					isQuantityZero = false;
				}
				if((quantity == '0') )
				{
					if((arrOrdMul[i].value!=null || arrOrdMul[i].value!='') && arrOrdMul[i].value>1)
					{
						//divVal.innerHTML="You must order in units of "+ arrOrdMul[i].value+", please review your entry and try again.";
						divVal.innerHTML= " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul[i].value) + " "+baseUOM[i].value;
						divVal.style.display = 'block';
					}
					//Display Generic Message at Header level first then Update Line Level message.
					
					divVal.innerHTML="Please enter a valid quantity and try again.";
					divVal.setAttribute("class", "error");
					divVal.style.display = 'block';
					document.getElementById(arrQty[i].id).style.borderColor="#FF0000";
					
					count++;
		            isQuantityZero = true;
		            if(isOnlyOneItem == true){
						errorflag= false;
					}
					else{
						errorflag= true;
					}
					
				}
				else if(quantity>0){
					var totalQty = arrUOM[i].value * quantity;
					if(arrOrdMul[i].value == undefined || arrOrdMul[i].value.replace(/^\s*|\s*$/g,"") =='' || arrOrdMul[i].value == null)
					{
						arrOrdMul[i].value=1;
					}
					var ordMul = totalQty % arrOrdMul[i].value;
					isQuantityZero = false;
					if (arrOrdMul[i].value > 1 && priceCheck == true && errorMsgFlag == false){
						if (priceCheck == true){
							divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' />" + addComma(arrOrdMul[i].value) +" "+baseUOM[i].value ;
							divVal.setAttribute("class", "notice");
							divVal.style.display = 'block';
							document.getElementById(arrQty[i].id).style.borderColor="";
						}
						else {
							divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul[i].value) +" "+baseUOM[i].value ;
							divVal.setAttribute("class", "notice");
							divVal.style.display = 'block';
							
							}
						}	
					}
				}				
			}
			if(uomCheck == true)
			{
				errorflag= false;
			}	
			if((isQuantityZero == true || count > 0) && !isOnlyOneItem)
			{
				document.getElementById("errorMsgTop").innerHTML = "An error has occurred with one or more of your items.  Please review any error messages on the lines below." ;
	            document.getElementById("errorMsgTop").style.display = "inline";
	            
	            document.getElementById("errorMsgBottom").innerHTML = "An error has occurred with one or more of your items.  Please review any error messages on the lines below." ;
	            document.getElementById("errorMsgBottom").style.display = "inline";
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
			var itemLineFlag = "false";
			var itemsString = data;
			var char = '\n';
			var flagTestCloseWindow = "false";	
			var itemLines = itemsString.split(char);
			if(itemsString == "")
			{
				document.getElementById("errorMsgCopyBottom").innerHTML = "Valid string is required. See instructions above." ;
		        document.getElementById("errorMsgCopyBottom").style.display = "inline";
			}
			else 
			{
				document.getElementById("errorMsgCopyBottom").innerHTML = "" ;
		        document.getElementById("errorMsgCopyBottom").style.display = "none";
			}
						
			for(var i=0;i < itemLines.length; i++)
			{
				var itemQty = null;
				var itemSku = null;
				var jobId = "";
				var itemLine = itemLines[i].split('\t');
				if(i == itemLines.length-1){
					if(itemLine == ""){
						break;
					}
				}
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
					if(itemSku == ""){
						itemLineFlag = "true";
						document.getElementById("errorMsgCopyBottom").innerHTML = "Valid string is required. See instructions above." ;
				        document.getElementById("errorMsgCopyBottom").style.display = "inline"; 
					}
				}
				else
				{
					itemLineFlag = "true";
					document.getElementById("errorMsgCopyBottom").innerHTML = "Valid string is required. See instructions above." ;
			        document.getElementById("errorMsgCopyBottom").style.display = "inline"; 
				}
			}
			if(itemLineFlag == "false")
			{
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
					
					 if(i == itemLines.length-1){
						if(itemLine == ""){
							flagTestCloseWindow = "true";
							
						}
					}
					if(flagTestCloseWindow == "false"){ 
						if(itemLine.length > 1 )
						{
							itemQty = itemLine[0];
							itemSku = itemLine[1];
						}					
						if((i+1) == itemLines.length && itemLineFlag == "false")
						{
							$.fancybox.close();
							Ext.get('dlgCopyAndPasteText').dom.value = '';
						}
						
						itemSku = Ext.util.Format.trim(itemSku);
						itemQty = Ext.util.Format.trim(itemQty);
						
						document.getElementById("prodId").value= itemSku;
						document.getElementById("qty").value= itemQty;
						
						var itemType = document.getElementById("itemType").value;
			            var itemTypeText = itemType;
		                var itemTypeSelElem = document.getElementById("itemType");
		                if(itemTypeSelElem!=null){
		                    itemType = itemTypeSelElem.options[itemTypeSelElem.selectedIndex].value;
		                    itemTypeText = itemTypeSelElem.options[itemTypeSelElem.selectedIndex].text;
		                }
						
						qaAddItem(jobId, itemQty, itemSku, itemType,'', itemTypeText); 
					}
					
					if(flagTestCloseWindow == "true"){ 
						$.fancybox.close();
						
						Ext.get('dlgCopyAndPasteText').dom.value = '';
				
					}
				}
			}	
		}
		
		function exportList(){
			//Wait for 5 sec and then hide the dialog 
			var task = new Ext.util.DelayedTask(function(){
			    Ext.MessageBox.hide();
			});
			task.delay(5000);
			
			var formItemIds = document.getElementById("formItemIds");
			<s:url id='exportList' action='MyItemsDetails' escapeAmp='false' >
			</s:url>
			if (formItemIds){
				formItemIds.action = "<s:property value='%{exportList}' escape='false'/>";
				formItemIds.command.value = 'export_list';			
				formItemIds.submit();
			}
			
			//-- Web Trends tag start --
			writeMetaTag('DCSext.w_x_list_export', '1');
			//-- Web Trends tag end --
			
		    xpedx_working_start();
            setTimeout(xpedx_working_stop, 3000);
		}
		
		<s:if test="editMode">
			function updateMilCountSelected() {
				// var numSelectedCheckboxes = $('.milCheckbox:checked').length;
				var numSelectedCheckboxes = $('.milCheckbox:checked').length;
				
				var $milCountPanel = $('.mil-count-selected');
				if (numSelectedCheckboxes == 0) {
					$milCountPanel.hide();
				} else {
					var totalCheckboxes = $('.milCheckbox').length;
					$milCountPanel.html('Items to be removed: ' + numSelectedCheckboxes + ' of ' + totalCheckboxes);
					$milCountPanel.show();
				}
			}
		</s:if>
		
		$(document).ready(function() {
			$('.toggleAllSelected').change(function(event) {
				var checkAll = $(this).attr('checked');
				
				var checkboxes = $('.milCheckbox, .toggleAllSelected');
				for (var i = 0, len = checkboxes.length; i < len; i++) {
					checkboxes[i].checked = checkAll;
				}
				
				if (typeof(updateMilCountSelected) === 'function') {
					setTimeout(updateMilCountSelected, 0);
				}
			});
			
			$('.milCheckbox').change(function(event) {
				if (typeof(updateMilCountSelected) === 'function') {
					setTimeout(updateMilCountSelected, 0);
				}
			});
		});


		function displayMsgHdrLevelForLineLevelError() {
			document.getElementById("errorMsgTop").innerHTML = "No items with quantity defined. Please review the list and try again." ;
            document.getElementById("errorMsgTop").style.display = "inline";
            
            document.getElementById("errorMsgBottom").innerHTML = "No items with quantity defined. Please review the list and try again." ;
            document.getElementById("errorMsgBottom").style.display = "inline";
		}
		
		function updateSelectedPAA(){
			//added for jira 3974
			showProcessingIcon();
			priceCheck = true;
			addToCartFlag = false;
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
			var formItemIds ;
			var validateOMForMultipleItems ; 
			if(validateOrderMultiple(false,null) == false)
			{
				validateOMForMultipleItems = false;
			}
			else{
					
				validateOMForMultipleItems = true;
			}


			if(cnt == 1){
				var itemId = document.getElementById("enteredProductIDs_"+singleSelectedKey).value;
				checkAvailability(itemId,singleSelectedKey);
			}
			else if(cnt>0)
			{
				if(cnt == checkboxes.length){
					formItemIds = document.getElementById('formItemIds').command.value = 'stock_check_all'; 
				}
				else{
					formItemIds = document.getElementById('formItemIds').command.value = 'stock_check_sel';
				} 
				formItemIds = document.getElementById('formItemIds');
			}

			<s:url id='testData' action='getItemAvailabiltyForSelected.action'>
		    </s:url>
		    writeMetaTag("DCSext.w_x_sc_count",cnt);
		    // if(validateOMForMultipleItems == true){
			if (formItemIds){
				var erroMsg = '<s:property value='erroMsg' />';
				formItemIds.action = "<s:property value='%{testData}' escape='false'/>";				
	                 Ext.Ajax.request({
	                   url: '<s:property value='%{testData}' escape='false'/>',
	                   params: {
	                	   validateOMForMultipleItems : validateOMForMultipleItems,
	                	   erroMsg : erroMsg,
		                   },
	                   form: 'formItemIds',
	                  method: 'POST',

	                   success: function (response, request){
			                var responseText = response.responseText;
		                    var availabilityRow = document.getElementById('priceDiv');		                   
		            		availabilityRow.innerHTML=responseText;
		            		assignAvailablity();

		            		var myitemskeys =document.getElementById("chkItemKeys").value;
		            		var trimMyitemskeys = myitemskeys.trim();
		            		var myitemskey=trimMyitemskeys.split(",");
		            		//var myitemskey = splitMyitemskey.trim();
		            		availabilityRow.innerHTML='';
		            		availabilityRow.style.display = '';
		            		//Added for XB 214 - BR requirements

		            		// omArray = document.getElementsByName("orderMultipleQtyFromSrc");
		            		var i,j,k=0;
		            		 for(i=0;i<checkboxes.length;i++){
		            			 j=(i+1);
		            			 var _omQtyUom = document.getElementById("orderMultipleQtyFromSrc_"+j);
		            			
		            			 if(_omQtyUom == null || _omQtyUom == undefined){
		            				 continue;
		            			 }
		            			 var omQtyUom = document.getElementById("orderMultipleQtyFromSrc_"+j).value;
		            			 
		            			if( omQtyUom =='' || omQtyUom == null  || myitemskey[k] == undefined){
		            					k++;
	                	 				continue;
	                			}
		            			
		            			 var _myItemKey=myitemskey[k].replace(/^\s+|\s+$/g,"");
		            			 var orderMultipleQtyUom = omQtyUom.split("|");
		            			 var orderMultipleQty = orderMultipleQtyUom[0];
		            			 var orderMultipleUom = orderMultipleQtyUom[2]; // uom desc now in response
		            			 var omError = document.getElementById("orderMulErrorCode_"+j).value;	
		            			 var qty = document.getElementById("QTY_"+_myItemKey);
		            			 var sourceOrderMulError = document.getElementById("errorDiv_qtys_"+_myItemKey);
		            			 var sourceOrderMulErrorInnerHTML = sourceOrderMulError.innerHTML;
		            			 document.getElementById("qtys_"+_myItemKey).style.borderColor="";
		            			 if(qty.value == '0' )
		            				{
		            					sourceOrderMulError.innerHTML = "Please enter a valid quantity and try again.";
		            					sourceOrderMulError.style.display = "inline-block"; 
		            					sourceOrderMulError.setAttribute("class", "error");
		            					document.getElementById("availabilityRow_"+_myItemKey).style.display ="none";
		            				}
		            			 else if(sourceOrderMulErrorInnerHTML.indexOf("is currently not valid. Please delete it from your list and contact Customer Service") > -1){
		            					 document.getElementById("availabilityRow_"+_myItemKey).style.display ="none";
		            			    }
		            			 else if(omError == 'true' && (qty.value > 0 || qty.value == ""))
		            				{
		            					sourceOrderMulError.innerHTML = "Must be ordered in units of " + addComma(orderMultipleQty) +" "+orderMultipleUom;
		            					sourceOrderMulError.style.display = "inline-block"; 
		            					sourceOrderMulError.setAttribute("class", "error");
		            					document.getElementById("availabilityRow_"+_myItemKey).style.display ="none";
		            					document.getElementById("qtys_"+_myItemKey).style.borderColor="#FF0000";
		            				}
		            			 else if(omError == 'true')
		            				{
		            					sourceOrderMulError.innerHTML = "Must be ordered in units of " + addComma(orderMultipleQty) +" "+orderMultipleUom;
		            					sourceOrderMulError.style.display = "inline-block"; 
		            					sourceOrderMulError.setAttribute("class", "notice");
		            					document.getElementById("availabilityRow_"+_myItemKey).style.display ="none";
		            				}
		            				else if(orderMultipleQty != null && orderMultipleQty != 0)
		            				{
		            					sourceOrderMulError.innerHTML = "Must be ordered in units of " + addComma(orderMultipleQty) +" "+orderMultipleUom;
		            					sourceOrderMulError.style.display = "inline-block"; 
		            					sourceOrderMulError.setAttribute("class", "notice");
		            					document.getElementById("availabilityRow_"+_myItemKey).style.display ="block";
		            				}
		            				else{
		            					document.getElementById("availabilityRow_"+_myItemKey).style.display ="block";
		            				}
		            			k++;
		            		 
		            		}
	                     	hideProcessingIcon();
				        //-- Web Trends tag start --
			            writeWebtrendTag(responseText);
			            //-- Web Trends tag end --
	                   },

	                   failure: function (response, request){						
						hideProcessingIcon();
						  alert("Your request could not be completed at this time, please try again.");	                   }
	               });     
		}
		if(cnt <=0 ){
				
				var msgSelectItemFirst = "<s:text name='MSG.SWC.MIL.NOITEMSELECT.ERROR.SELECTFORPNA' />";
				document.getElementById("errorMsgTop").innerHTML = msgSelectItemFirst ;
                		document.getElementById("errorMsgTop").style.display = "inline";

				document.getElementById("errorMsgBottom").innerHTML = msgSelectItemFirst ;
                		document.getElementById("errorMsgBottom").style.display = "inline";
                		hideProcessingIcon();
			}
		}

		function assignAvailablity()
		{
			var availabilityRows = document.getElementById('priceDiv').getElementsByTagName("div");
			var avilalablityDivs=document.getElementsByName('availabilityRowsHide');
			for(var j=0;j<avilalablityDivs.length;j++)
			{
				var avaliable = avilalablityDivs[j].id;
				for(var i=0;i<availabilityRows.length;i++)
				{
					var availableId = ReplaceAll(avaliable,"hidden_","");
					var availableIdDiv=document.getElementById(availableId);
					if(availabilityRows[i].id == availableId)
					{
						availableIdDiv.innerHTML=availabilityRows[i].innerHTML;
						availableIdDiv.style.display="block";
					}
				}
			}
		}
		
		function selectNone(){
			var checkboxes = Ext.query('input[id*=state_]');
			Ext.each(checkboxes, function(obj_item){
				obj_item.checked = false;
			});
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
			<s:if test='editMode != true'>			
				<s:set name="itemDtlBackPageURL" value="%{itemDtlBackPageURL}" scope="session"/>
			</s:if>
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
			document.getElementById("errorMsgTop").innerHTML = defaultMsg ;
			document.getElementById("errorMsgTop").style.display = "none";
			
			document.getElementById("errorMsgBottom").innerHTML = defaultMsg ; 
            document.getElementById("errorMsgBottom").style.display = "none";
			    

            
		}
		
	</script>
	<script type="text/javascript">
            var count=0;
		    var elementUniqueID = '';
		    var itemSize = <s:property value="#ItemSize"/>;
            var myArray2= new Array();
            var oldvalue = 0;	  
				  function populate(elemetnID)
				  {
					 
				  var combo = document.getElementById(elemetnID);
				  oldvalue = combo.value;
				  
				  if(myArray2.length == 0){
								
				  for (i=0; i<itemSize; i++)
					{ 
					 var j = i;
					++j;
					myArray2[i] = "itemOrder_"+j;
					}
					}              
					 if(combo.length < 2){
					 if(elementUniqueID == combo){

					 if(count == 0){
						elementUniqueID = combo;
						for (i=1; i<=itemSize; i++)
						{
						if(combo.value != i){ 
						
						var option = document.createElement("option");
							   option.text = i;
							   option.value = i;
							   combo.add(option);
							   ++count;
						}
									
					 }
					}
                      }             
				  else{
					  elementUniqueID = combo;
					  for (i=1; i<=itemSize; i++)
						{
						 if(combo.value != i){        
						 var option = document.createElement("option");
							   option.text = i;
							   option.value = i;
							   combo.add(option);
							   ++count;
								 }
									
							}
                                             
                           }
                          } 
                        }
				  function populateValue(targetElement,elemetnID) {
					   document.getElementById(elemetnID).value =
					   targetElement.options[targetElement.selectedIndex].value;
						   var idx =  document.getElementById(elemetnID).value;
						   var currIdx =  oldvalue;
							   	
						   var currValue = null;
							idx = parseInt(idx);
							currIdx = parseInt(currIdx);
							var currentSelElemName = targetElement.name;
							if (!targetElement) {
								targetElement = null;
							   }
							   	
							var selectElems = document.getElementsByName(currentSelElemName);
							if (selectElems == null)
								return;
							for ( var i = 0; i < selectElems.length; i++) {
								var currElem = selectElems[i];
								populate(currElem.id);
								currValue = parseInt(currElem.value);
								if (targetElement == currElem) {
									document.getElementById(targetElement.id).value = idx;
									continue;
							   }
							if (idx < currIdx) {
									if (currValue >= idx && currValue < currIdx) {
									    document.getElementById(currElem.id).value = currValue + 1;
										//currElem.selectedIndex = currElem.selectedIndex + 1;
						 }  
								}
							   else {
									if (currValue > currIdx && currValue <= idx) {
										document.getElementById(currElem.id).value = currValue - 1;
										//currElem.selectedIndex = currElem.selectedIndex - 1;
									}
								}
							}
                                     
                 }
				  
 function removeItem(itemKey){
	 
	 		$("#itemKeyForRemoveFromList").val(itemKey);
	 		$("#listKeyForRemove").val($("#listKey").val());
	 		$("#confirmDeleteItem").fancybox({				
			'onClosed' : function() {    			
				$("#itemKeyForRemoveFromList").val("");
				$("#listKeyForRemove").val("");
			},
			'autoDimensions'	: false,
			'width' 			: 300,
			'height' 			: 120  
				}).trigger('click');
	 }
	function fancyBoxCloseAndDelItem(){
		var itemIdForRemoveFromListForm = document.getElementById("itemIdForRemoveFromListForm");
		itemIdForRemoveFromListForm.listNameForRemove.value = Ext.get("listName").dom.value;
		itemIdForRemoveFromListForm.listDescForRemove.value = Ext.get("listDesc").dom.value;
		<s:url id='deleteListLink' action='MyItemsDetailsDelete.action' escapeAmp='false'></s:url>
		itemIdForRemoveFromListForm.action = "<s:property value='%{deleteListLink}'/>";
		$("#itemIdForRemoveFromListForm").submit();	
		$.fancybox.close();
	}		
    </script>

</head>
<!-- Hemantha -->
<body class="  ext-gecko ext-gecko3">

	<s:url id="ajaxAddItemsToMILURLid" namespace="/myItems" action="ajaxAddItemsToMIL" />
	<s:hidden id="ajaxAddItemsToMILURL" value="%{#ajaxAddItemsToMILURLid}" />
	
	<s:hidden id="listKey" value="%{listKey}"></s:hidden>
	<s:hidden id="itemCount" value="%{XMLUtils.getElements(#outDoc2, 'XPEDXMyItemsItems').size()}" />

<div >
<div class="loading-icon" style="display:none;"></div>
</div>

	<%-- fancybox workaround: fancybox only works with 'a' tag so programatically click it when the button is clicked --%>
	<a style="display: none;" id="dlgImportFormLink" href="#dlgImportForm" />
	<a style="display: none;" id="dlgShareListLink" href="#dlgShareList" />
	
	<!--  Shopping for banner -->
	<div id="main-container">
		<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
			    This is to avoid a defect in Struts that?s creating contention under load. 
			    The explicit call style will also help the performance in evaluating Struts? OGNL statements. --%>
		<s:set name='_action' value='[0]' />


		<s:set name='categoryListElem' value="categoryListElement" />
		<s:set name='childCategoryListElem' value="childCategoryListElement" />
		<s:set name='fieldListElem' value="searchableIndexFieldListOutPutElement" />
		<s:set name="isEditOrderHeaderKey" value="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}" />
		<s:set name="duplicateInfoMsg" value="#_action.getDuplicateInfoMsg()" />

		<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
		<s:bean name='org.apache.commons.lang.StringUtils' id='strUtil' />
		<s:a href="#addToCartDlg" id="addToCartFancybox" cssStyle="display: none;"></s:a>
		<s:a href="#Price_Grid" id="bracketPricing" cssStyle="display: none;"></s:a>
		<s:url id='availabilityURL' action='ajaxAvailabilityJson' namespace='/catalog' />
		<s:url id='checkAvailabilityURL' action='getItemAvailabilty' namespace="/myItems" />
		<input type="hidden" value="<s:property value='%{checkAvailabilityURL}'/>" id="checkAvailabilityURLHidden" />
		<s:url id='addToCartURL' namespace='/order' action='addToCart' />
		<s:url id='addToCartURLId' namespace="/myItems" action='addMyItemToCart' />
		<s:hidden id='updatePandAURL' value='%{#addToCartURLId}' />
		<s:hidden id='currentCartInContextOHKVal' value='%{#currentCartInContextOHK}' />
		<div>
		<a id="confirmDeleteItem" href="#deleteItemDiv"></a>
		<!-- HTML Dialogs -->
		<div style="display: none;">
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

					<br />
					<br />
					<s:submit value="Create New Item" align="center"></s:submit>
					&nbsp;&nbsp;
					<input type="button" value="Cancel" onclick="hideForm('NewForm')" />
				</s:form>
			</div>

			<s:include value="../modals/XPEDXItemImportModal.jsp" />

			<s:include value="../modals/XPEDXShareMyItemsList.jsp" />

			<div id="dlgCopyAndPaste" class="xpedx-light-box"
				style="width: 400px; height: 350px;">
				<h2>Copy and Paste</h2>
				<p>
					Copy and paste or type the quantities and
					<s:property value="wCContext.storefrontId" />
					item numbers or customer item numbers from your file in the
					following format: quantity,item number (no spaces). <br />Example:12,5002121
				</p>
				<p>
					To enter items without quantities, copy and paste or type a comma
					followed by the item number (no spaces).<br /> Example: ,5002121 <br />
				</p>
				<br />
				<form id="form1" name="form1" method="post" action="">
					<textarea name="dlgCopyAndPasteText" id="dlgCopyAndPasteText"
						cols="48" rows="5"></textarea>
					<ul id="tool-bar" class="tool-bar-bottom" style="float: right";>
						<li>
							<a class="grey-ui-btn" href="javascript:$.fancybox.close();"
									onclick="Ext.get('dlgCopyAndPasteText').dom.value = '';Ext.get('errorMsgCopyBottom').dom.innerHTML='';Ext.get('errorMsgCopyBottom').dom.style.display='none'">
								<span>Cancel</span>
							</a>
						</li>
						<li style="float: right;">
							<a href="javascript: quickAddCopyAndPaste( document.form1.dlgCopyAndPasteText.value);"
									class="green-ui-btn" style="margin-left: 5px;">
								<span>Add to Quick List</span></a>
						</li>
					</ul>
				</form>
				<br/>
				<br/>
				<br/>
				<div class="error" id="errorMsgCopyBottom" style="display: none; position: relative; left: 130px"></div>
			</div>


			<div id="dlgEditForm" class="dlgForm">
				<h3>Edit Record</h3>
				<s:form id="formEdit" action="MyItemsDetailsChange" method="post">
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

					<br />
					<br />
					<s:submit value="Save" align="center"></s:submit>
					&nbsp;&nbsp;
					<input type="button" value="Cancel" onclick="hideForm('EditForm')" />
				</s:form>
			</div>
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
			<s:hidden name="filterBySelectedListChk" value="%{#_action.isFilterBySelectedListChk()}" />
			<s:hidden name="filterByMyListChk" value="%{#_action.isFilterByMyListChk()}" />
			<s:hidden name="filterByAllChk" value="%{#_action.isFilterByAllChk()}" />
			<s:hidden name='sharePrivateField' value='%{#_action.getSharePrivateField()}' />

			<s:hidden name="editMode" value="%{true}"></s:hidden>
		</s:form>

		<div id="main" class="hide-item-thumbnails lazy-load-thumbnails">
			<s:action name="xpedxHeader" executeResult="true" namespace="/common" />

			<s:if test='!#guestUser'>
				<s:action name="xpedxShiptoHeader" executeResult="true" namespace="/common" />
			</s:if>

			<div class="container content-container" style="min-height: 535px;">
				<!-- breadcrumb -->
				<s:url action='home.action' namespace='/home' id='urlHome' includeParams='none' />
				<s:url id='urlMIL' namespace='/myItems' action='MyItemsList.action' includeParams="get" escapeAmp="false">
					<s:param name="filterByAccChk" value="%{#_action.getFilterByAccChk()}" />
					<s:param name="filterByShipToChk" value="%{#_action.getFilterByShipToChk()}" />
					<s:param name="filterByMyListChk" value="%{#_action.getFilterByMyListChk()}" />
					<s:param name="filterByAccSel" value="%{#_action.getFilterByAccSel()}" />
					<s:param name="filterByShipToSel" value="%{#_action.getFilterByShipToSel()}" />
				</s:url>

				<div id="mid-col-mil">
					<h5 align="center">
						<b><font color="red"><s:property value="ajaxLineStatusCodeMsg" /></font></b>
					</h5>
				</div>
				<div id="errorMessageDiv"></div>
				<div id="infoMessage">
					<s:if test=' "" != duplicateInfoMsg '>
						<s:property value="duplicateInfoMsg" />
					</s:if>
				</div>

				<s:set name="xpedxItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_ITEM_LABEL" />
				<s:set name="customerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUSTOMER_ITEM_LABEL" />
				<s:set name="manufacturerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MANUFACTURER_ITEM_LABEL" />
				<s:set name="mpcItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MPC_ITEM_LABEL" />

				<s:hidden id="mandatoryFieldCheckFlag_mil-edit" name="mandatoryFieldCheckFlag_mil-edit" value="%{false}"></s:hidden>
				
				<s:if test="%{editMode}">
					<p class="return-link">
						<a href="javascript:cancelChanges();">&lsaquo; Return to List</a>
					</p>
					<h1>Edit My Items List:&nbsp;<span><s:property value="listName" /></span></h1>
				</s:if>
				<s:else>
					<h1><s:text name='MSG.SWC.MIL.DETL.GENERIC.PGTITLE' />:&nbsp;<span><s:property value="listName" /></span></h1>
				</s:else>
				
				<div id="printButton" class="print-ico-xpedx underlink">
					<a href="javascript:window.print()">
						<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif"
								width="16" height="15" alt="Print Page" />
						Print Page
					</a>
				</div>
				
				<div>
					<span class="grey" style="width: 421px; word-wrap: break-word; float: left;">
						<s:property value="listDesc" />
					</span>
				</div>
				<div class="clear"></div>

				<s:if test="%{editMode != true && errorMsg != null && errorMsg != ''}">
					<s:if test="%{errorMsg == 'ITEM_REPLACE_ERROR'}">
						<h5 align="center">
							<b><font color="red">The selected replacement item could not be replaced in the list.</font></b>
						</h5>
						<br />
					</s:if>
					<s:elseif test="%{errorMsg == 'ITEM_ADD_ERROR'}">
						<h5 align="center">
							<b><font color="red">The selected replacement item could not be added to the list.</font></b>
						</h5>
						<br />
					</s:elseif>
					<s:elseif test="%{errorMsg == 'ITEM_SAVE_ERROR'}">
						<h5 align="center">
							<b><font color="red">The changes could not be saved.</font></b>
						</h5>
						<br />
					</s:elseif>
					<s:else>
						<h3>The import must be in csv format, please try another import file.</h3>
						<br />
					</s:else>
				</s:if>

				<div class="mid-altwrap">
					<s:if test='editMode != true'>
						<div class="view-hide-images">
							<a id="toggleview" class="viewbtn"></a>
						</div>

						<div class="button-container"> <%-- read-only top --%>
							<s:if test='itemCount > 0'>
								<s:if
									test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
									<input name="button" type="button"
											class="btn-add-items-qty-to-cart btn-gradient floatright addmarginleft10"
											value="Add Items with Qty to Cart" />
								</s:if>
								<s:else>
									<input name="button" type="button"
											class="btn-add-items-qty-to-cart btn-gradient floatright addmarginleft10"
											value="Add Items with Qty to Order" />
								</s:else>
							</s:if>

							<s:if test="%{canEditItem}">
								<input name="button" type="button"
									class="btn-neutral floatright addmarginleft10"
									value="Edit This List"
									onclick="document.getElementById('formEditMode').submit(); return false;" />
							</s:if>

							<input name="button" type="button"
									class="btn-neutral floatright addmarginleft10"
									value="Export This List" onclick="exportList(); return false;" />
						</div> <%-- / button-container --%>
					</s:if>
					<s:else> <%-- editMode --%>
						<div id="mil-edit" class="mil-edit" style="width: 100%">
							<div class="button-container"> <%-- edit mode top --%>
								<input name="button" type="button" class="btn-gradient floatright addmarginleft10" value="Save Updates" onclick="saveAllItemsNew('mil-edit', ['quick-add']); return false;" />
								
								<input name="button" type="button" class="btn-import-items btn-neutral floatright addmarginleft10" value="Import List" />
								
								<s:if test="%{canShare || (#isEstUser && (#shareAdminOnlyFlg=='' || #shareAdminOnlyFlg=='N'))}">
									<input name="button" type="button" class="btn-share-list btn-neutral floatright  addmarginleft10" value="Share List" />
								</s:if>
							</div>
							<div class="clearfix"></div>
							
							<div class="mil-edit-forms">
								<s:set name='isPrivateList' value="%{#_action.isFilterByMyListChk()}" />
								<s:set name='isSelectedList' value="%{#_action.isFilterBySelectedListChk()}" />
								<s:set name='isFilterByAll' value="%{#_action.isFilterByAllChk()}" />
								<s:set name='sharePrivate' value="%{#_action.getSharePrivateField()}" />
								<s:if test="(#isPrivateList)||(#isUserAdmin && #isSelectedList)">
									<s:set name='disableListNameAndDesc' value="%{false}" />
								</s:if>
								<s:else>
									<s:set name='disableListNameAndDesc' value="%{true}" />
								</s:else>
								<s:if test="#sharePrivate != '' && #sharePrivate != null">
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
								
								<label>Name:</label>
								<input id="listName" class="x-input text addheight" size="40" maxlength="35" title="Name"
										<s:property value="%{#disableListNameAndDesc ? 'disabled=\'disabled\'' : ''}" />
										onkeyup="listNameCheck(this, 'mil-edit');"
										onmouseover="listNameCheck(this, 'mil-edit');"
										value="<s:property value="listName"/>" />
							</div> <%-- / mil-edit-forms --%>
							
							<div class="mil-edit-forms-desc">
								<label>Description:</label>
								<input type="text" class="x-input addheight" id="listDesc" title="Description"
										<s:property value="%{#disableListNameAndDesc ? 'disabled=\'disabled\'' : ''}" />
										onkeyup="restrictTextareaMaxLength(this,200);" size="90"
										value="<s:property value="listDesc"/>" />
							</div> <%-- / mil-edit-forms-desc --%>
						</div> <%-- / mil-edit --%>
						<div class="clearfix"></div>
						
						<s:set name="showLinePo" value="#customerPONoFlag != null && !#customerPONoFlag.equals('')" />
						<s:set name="showLineAcct" value="#jobIdFlag != null && !#jobIdFlag.equals('')" />
						
						<form name="QuickAddForm" id="QuickAddForm" onsubmit="validateItems(); return false;">
							<div id="quick-add">
								<s:div cssClass="%{(#showLinePo || #showLineAcct) ? 'mil-qa-list-po' : 'mil-qa-list-wrap'}">
									<div id="mil-qa-trigger">
										<div id="view-qa-wrap" class="arrows-down"></div>
									</div>
									
									<div id="mil-qa-content" style="display: none;">
										<div class="mil-qa-button-wrap">
											<input class="btn-reset-items btn-neutral addmarginleft15  addmarginright10" value="Clear" type="button" />
											<input class="btn-gradient" type="submit" value="Add to My Items List" />
										</div>
										
										<s:div cssClass="%{(#showLinePo || #showLineAcct) ? 'mil-add-item' : 'mil-add-item-wrap'}">
											<h3 class="qa-subhead">Quick Add List</h3>
											<div class="mil-pa-select-item addpadbottom20 ">
												Select Item Type
												<select id="qaItemType" name="qaItemType" style="width:135px;">
													<s:iterator value="skuTypeList" id="skuType">
														<option value="<s:property value="%{#skuType.key}"/>">
															<s:property value="%{#skuType.value}"/>
														</option>
													</s:iterator>
												</select>
											</div>
											<div class="qa-listheader ">
												<div class="label-item">Item</div>
												<div class="label-qty">Qty</div>
												<s:if test="%{#showLinePo}">
													<div class="label-po">
														<s:property value='#customerPONoFlag' />
													</div>
												</s:if>
												<s:if test="%{#showLineAcct}">
													<div class="label-account">
														<s:property value='#jobIdFlag' />
													</div>
												</s:if>
											</div>
											
											<s:set name="numRows" value="%{200}" />
											<s:iterator value="%{new int[#numRows]}" status="itemline">
												<s:div id='%{"qa-listrow_" + #itemline.count}' cssStyle='%{#itemline.count > 5 ? "display:none;" : ""}' cssClass="qa-listrow">
													<div class="qa-error-icon" style="visibility: hidden">
														<input type="image" id="errorIcon_<s:property value='#itemline.count'/>" class="errorIcon" 
																src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_red_x<s:property value='#wcUtil.xpedxBuildKey' />.png"  />
													</div>
													<div class="label-item">
														<input type="text" maxlength="27" size="15"
																id="enteredProductIDs_<s:property value='#itemline.count'/>"
																name="enteredProductIDs" class="inputfloat input-item"
																onfocus="showQuickAddRow(<s:property value='%{#itemline.count + 1}'/>)" />
													</div>
													<div class="label-qty">
														<input maxlength="7" size="8" type="text"
																id="enteredQuantities_<s:property value='#itemline.count'/>"
																name="enteredQuantities" class="inputfloat input-qty"
																onkeyup="return isValidQuantityRemoveAlpha(this,event)" />
													</div>
													<s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
														<div class="label-po">
															<input maxlength="22" size="15" type="text"
																	name="enteredPONos" value="" class="inputfloat input-po"
																	id="enteredPONos_<s:property value='#itemline.count'/>" />
														</div>
											
													</s:if>
													<s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>
														<div class="label-account">
															<input maxlength="24" size="20" type="text"
																	id="enteredJobIDs_<s:property value='#itemline.count'/>"
																	name="enteredJobIDs" class="inputfloat input-account" />
														</div>
													</s:if>
													
													<div class="error producterrorLine" style="display: none;" id="producterrorLine_<s:property value='#itemline.count'/>">
													</div>
			
													<%-- These inputs are required for the backend to process, not visible in UI --%>
													<s:hidden cssClass="input-uom" name="enteredUOMs" value="" /> <%-- populated by javascript before submission --%>
													<s:hidden cssClass="input-itemType" name="enteredItemTypes" value="" /> <%-- populated by javascript before submission --%>
													<s:hidden name="enteredProductDescs" value="" /> <%-- always empty --%>
													<s:hidden name="quickAddOrderMultiple" value="1" /> <%-- always 1 (quick add ignores order multiple) --%>
												</s:div> <%-- / qa-listrow --%>
											</s:iterator>
											
											<div class="mil-qa-button-wrap">
												<input class="btn-reset-items btn-neutral addmarginleft15  addmarginright10" value="Clear" type="button" />
												<input class="btn-gradient" type="submit" value="Add to My Items List" />
											</div>
	
											<hr class="qa-horiz" />
										</s:div> <%-- / mil-add-item OR mil-add-item-wrap --%>
										
										<div class="mil-qa-copy-paste">
											<h3 class="qa-subhead">Copy and Paste <span>(replaces content in the Quick Add List)</span></h3>
	
											<div class="mil-copytext-wrap">
												<div class="mil-qa-copy-instructions">
													<p class="qa-psmall">Paste or type the xpedx item numbers or customer item numbers in the following format:</p>
													<p class="qa-plarge addmargintop12">
														<strong>Item Number,Quantity</strong>
													</p>
													<p class="qa-psmall addmargintop10">Examples:</p>
													<p class="qa-plarge"><strong>50052121,12</strong> (item with quantity)</p>
													<p class="qa-plarge"><strong>50052121</strong> (item without quantity)</p>
												</div>
											</div> <%-- / mil-copytext-wrap --%>
	
											<textarea name="items" id="copypaste-text" class="mil-qa-copypaste" rows="5"></textarea>
											<div class="qa-itemlimit">Item Limit 200</div>
											<div class="qa-button-wrap">
												<input id="btn-add-to-list" class="btn-gradient floatright" value="Add to Quick Add List" type="button" />
												<input id="btn-reset-copy-paste" class="btn-neutral floatright addmarginright10" value="Clear" type="button" />
											</div>
											<div id="copypaste-error" class="error floatleft" style="display: none;"></div>
										</div> <%-- / mil-qa-copy-paste --%>
									</div> <%-- / close mil-qa-content --%>
								</s:div> <%-- / mil-qa-list-po OR mil-qa-list-wrap --%>
							</div> <%-- / quick-add --%>
						</form>
						
						<s:form id="ReloadFormForQuickAdd" namespace="/myItems" action="MyItemsDetails" method="get">
							<s:hidden name="listKey" value="%{listKey}" />
							<s:hidden name="listName" value="%{listName}" />
							<s:hidden name="listDesc" value="%{listDesc}" />
							<s:hidden name="itemCount" value="%{itemCount}" cssClass="itemCount" />
							<s:hidden name="editMode" value="true" />
						</s:form>
										
						<div class="clear"></div>
						<br />
						<s:set name="shareAdminOnlyFlg" value="%{#_action.getShareAdminOnly()}" />
						
						<div class="view-hide-images addpadtop10">
							<a id="toggleview" class="viewbtn"></a>
						</div>
					</s:else> <%-- end if-else editMode --%>
	
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
	
						<ul style="float: center; text-align: center; background-color: white !important;">
							<li>
								<div class="clearall"></div>
	
								<div class="error" id="errorMsgForMandatoryFields_mil-edit" style="display: none;"></div>
								<div class="error" id="errorMsgTop" style="display: none;"></div>
	
								<s:if test="%{errorMsg == 'InvalidImport'}">
									<script type="text/javascript">
										document.getElementById("errorMsgTop").innerHTML = "The import file must be in .csv format." ;
										document.getElementById("errorMsgTop").style.display = "inline"; 
									</script>
								</s:if>
								<s:if test="%{errorMsg == 'ItemsOverLoad'}">
									<div class="error">
										<%--   Your list may contain a maximum of 200 items. Please delete some items and try again. --%>
										<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGT200' />
									</div>
								</s:if>
								<s:if test="%{errorMsg == 'InvalidFormat'}">
									<div class="error">
										<%--   An unexpected error occured (eg, a row had too few columns) --%>
										The import file is not in the correct file layout. Download the
										sample file for an example of the correct layout and try again.
									</div>
								</s:if>
							</li>
						</ul>
						
						<s:if test="editMode">
							<div class="notice mil-count-selected mil-count-selected-top" style="display:none;">
								Placeholder text
							</div>
							<div class="clearfix"></div>
						</s:if>
						
						<div class="graybar">
							<input id="selAll1" class="forselected-input toggleAllSelected" type="checkbox" />
							<s:if test="editMode != true">
								<div>
									Select All<a href="#" class="indent20px paaLink">Show Price &amp; Availablity for Selected Items</a>
								</div>
							</s:if>
							<s:else> <%-- editMode --%>
								<div>
									Select All<a href="#" class="indent20px removeItemsLink">Remove Selected Items</a>
								</div>
							</s:else>
						</div>
						
						<s:set name="baseUOMs" value="#_action.getBaseUOMmap()" />
						<s:set name="itemIdCustomerUomMap" value="#_action.getItemAndCustomerUomHashMap()" />
						
						<s:if test='XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems").size() == 0'>
							<div class="addpad20 mil-list-empty-message">
								There are no items in this list.
							</div>
						</s:if>
						<s:else> <%-- 1+ items --%>
							<s:set name="webtrendsItemTypeMap" value="%{#_action.getItemTypeMap()}" />
							<s:iterator status="status" id="item" value='XMLUtils.getElements(#outDoc2, "XPEDXMyItemsItems")'>
								<s:set name='id' value='#item.getAttribute("MyItemsKey")' />
								<s:set name='name' value='#item.getAttribute("Name")' />
		
								<s:set name='desc2' value='#item.getAttribute("Desc")' />
								<s:set name='desc' value='#item.getAttribute("Desc")' />
								<s:set name='qty1' value='#item.getAttribute("Qty")' />
								<s:set name='qty' value='%{#strUtil.replace(#qty1, ".00", "")}' />
								<s:set name='jobId' value='#item.getAttribute("JobId")' />
								<s:set name='listKey' value='#item.getAttribute("MyItemsListKey")' />
								<s:set name='itemId' value='#item.getAttribute("ItemId")+"" ' />
								<s:set name='desc1' value="%{#itemDescMap.get(#itemId)}" />
								<s:set name='itemType' value='#item.getAttribute("ItemType")' />
								<s:set name='itemTypeLabel' value="%{#xpedxItemLabel + '+:'}" />
								<s:set name='showItemType' value='%{true}' />
								<s:set name='itemUomId' value='#item.getAttribute("UomId")' />
								<s:set name='customerUOM' value='#itemIdCustomerUomMap.get(#itemId)' />
								<s:set name="itemUOMsMap" value='itemIdConVUOMMap.get(#itemId)' />
								<s:set name="itemBaseUom" value='#baseUOMs.get(#itemId)' />
		
								<s:if test='%{#customerUOM==#itemBaseUom}'>
									<s:set name='customerUomWithoutM' value='%{#customerUOM.substring(2, #customerUOM.length())}' />
									<s:set name="baseUOMDesc" value="#customerUomWithoutM" />
								</s:if>
								<s:else>
									<s:set name="baseUOMDesc" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#itemBaseUom)" />
								</s:else>
		
								<s:set name="YFSItmePrimaryInfo" value='descriptionMap.get(#item.getAttribute("ItemId"))' />
								<s:set name="YFSItmeExtn" value='masterItemExtnMap.get(#item.getAttribute("ItemId"))' />
								<s:set name='certFlag' value='#YFSItmeExtn.getAttribute("ExtnCert")' />
								<s:set name='desc' value='#YFSItmePrimaryInfo.getAttribute("Description")' />
								<s:set name='name' value='#YFSItmePrimaryInfo.getAttribute("ShortDescription")' />
								<s:if test='%{#webtrendsItemTypeMap!=null}'>
									<s:set name="webtrendsItemType" value='%{#webtrendsItemTypeMap.get(#itemId)}' />
								</s:if>
								<s:else>
									<s:set name="webtrendsItemType" value='' />
								</s:else>
		
		
								<s:set name='itemId1' value='#item.getAttribute("ItemId")' />
		
								<s:set name='itemOrder' value='#item.getAttribute("ItemOrder")' />
								<s:hidden name='itemOrder' id="ItemOrder11" value='%{#item.getAttribute("ItemOrder")}' />
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
		
								<s:url id='deleteListLink' action='MyItemsDetailsDelete.action'>
									<s:param name="key" value="#id" />
									<s:param name="listKey" value="%{listKey}" />
									<s:param name="listName" value="%{listName}" />
									<s:param name="listDesc" value="%{listDesc}" />
								</s:url>
								<s:set name='uomList' value='itemIdsUOMsDescMap.get(#itemId1)' />
		
								<s:url id='itemDetailsLink' namespace="/catalog" action='itemDetails.action' includeParams='none' escapeAmp="false">
									<s:param name="itemID" value="#itemId" />
									<s:param name="sfId" value="#parameters.sfId" />
									<s:param name="unitOfMeasure" value="#itemBaseUom" />
								</s:url>
		
								<s:div cssClass="mil-wrap-condensed-container mil-only %{#status.last ? 'last' : ''}">
									<div
										<s:if test='%{#status.count == 1}'>class="mil-wrap-condensed"</s:if>
										<s:elseif test="!#status.last" >class="mil-wrap-condensed-mid"</s:elseif>
										<s:else>class="mil-wrap-condensed-bot"</s:else>>
		
										<!-- begin image / checkbox   -->
										<div class="mil-checkbox-wrap">
											<s:if test='editMode == true'>
												<s:checkbox name="itemKeys" fieldValue="%{#id}" cssClass="milCheckbox" />
											</s:if>
											<s:else>
												<s:checkbox name="checkItemKeys" fieldValue="%{#id}" cssClass="milCheckbox" />
											</s:else>
											
											<%-- TODO: add escape=false to this s:property (href's query string is messed up without it) --%>
												<div class="imagewrap relative">
													<s:if test="#wcUtil.isCoreItemByExtn(#YFSItmeExtn)">
														<div class="core-item"></div>
													</s:if>
													<a href='<s:property value="%{itemDetailsLink}" />'>
													<img class="item-thumbnail"
															data-src="<s:url value='%{#itemImagesMap.get(#itemId)}' includeParams='none' />"
															width="150" height="150" alt="" /></a>
												</div>
											<s:hidden name="keys" value="%{#id}" />
										</div>
										<!-- end image / checkbox   -->
		
										<!-- begin description  -->
										<s:hidden name="itemsName" value="%{#name}" />
										<s:hidden name="names" value="%{#name}" />
										<s:hidden name="enteredProductIDs" id="enteredProductIDs_%{#id}" value="%{#itemId}" />
										<s:hidden name="itemIds" value="%{#itemId}" />
		
										<div class="mil-desc-wrap">
											<div class="mil-wrap-condensed-desc desc-hide-images" style="height: auto;">
												<s:if test="%{#itemType != 99}">
													<a class="short-description" href='<s:property value="%{itemDetailsLink}" />'>
														<s:property value="#name" />
													</a>
												</s:if>
												<ul class="prodlist">
													<s:if test="%{#itemType != 99}">
														<a href='<s:property value="%{itemDetailsLink}" />'>
															<s:property value="#desc" escape="false" />
														</a>
													</s:if>
													<s:hidden name="itemsDesc" value="%{#desc}" />
													<s:hidden name="descs" value="%{#desc}" />
												</ul>
												<s:if test="%{#showItemType}">
													<p>
														<b>
															<s:property value="wCContext.storefrontId" />
															<s:property value="#xpedxItemLabel" />:
															<s:property value="#itemId" />
														</b>
														<s:if test='#certFlag=="Y"'>
															<img class="green-e-logo" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/catalog/green-e-logo_small.png" />
														</s:if>
													</p>
		
													<s:if test='skuMap != null && skuMap.size() > 0'>
														<s:set name='itemSkuMap' value='%{skuMap.get(#itemId)}' />
														<s:set name='mfgItemVal' value='%{#itemSkuMap.get(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MFG_ITEM_NUMBER)}' />
														<s:set name='partItemVal' value='%{#itemSkuMap.get(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUST_PART_NUMBER)}' />
													</s:if>
													<s:if test='mfgItemFlag != null && mfgItemFlag=="Y"'>
														<p>
															<s:property value="#manufacturerItemLabel" />:
															<s:property value='#mfgItemVal' />
														</p>
													</s:if>
													<s:if test='customerItemFlag != null && customerItemFlag=="Y"'>
														<p>
															<s:property value="#customerItemLabel" />:
															<s:property value='#partItemVal' />
														</p>
													</s:if>
												</s:if>
												<s:if test='editMode == true'>	
													<input class="btn-neutral addmargintop10" type="button" value="Remove from List" name="button" onclick="removeItem('<s:property value="#id"/>');"/>
												</s:if>	
											</div>
											
											<s:if test='(xpedxItemIDUOMToComplementaryListMap.containsKey(#itemIDUOM))'>
												<p class="mil-replaced">
													<a class='modal red'
															href='javascript:showXPEDXComplimentaryItems("<s:property value="#itemIDUOM"/>", "<s:property value="#orderLineKey"/>", "<s:property  value="#orderLine.getAttribute('OrderedQty')"/>");'>
														Complimentary
													</a>
												</p>
												<br />
											</s:if>
											<s:if test='(xpedxItemIDUOMToAlternativeListMap.containsKey(#itemIDUOM))'>
												<p class="mil-replaced">
													<a class='modal red'
															href='javascript:showXPEDXComplimentaryItems("<s:property value="#itemIDUOM"/>", "<s:property value="#orderLineKey"/>", "<s:property  value="#orderLine.getAttribute('OrderedQty')"/>");'>
														Alternate
													</a>
												</p>
												<br />
											</s:if>
											
										</div> <%-- / mil-desc-wrap --%>
		
		
										<div class="mil-action-list-wrap">
											<table class="mil-config" width="380" border="0" cellspacing="0" cellpadding="0">
												<s:if test='editMode == true'>
													<tr>
														<td width="182" align="right"></td>
														<td width="53" align="right" colspan="2">
															<label style="text-align: right;">Sequence:</label>
															<s:select cssClass="xpedx_select_sm" cssStyle="width: 50px;"
																	name="orders" list="itemValue" value='%{itemOrder2}'
																	onfocus="populate(this.id);"
																	onchange="populateValue(this,this.id);"
																	id="itemOrder_%{#itemOrder2}" headerKey='%{itemOrder2}'
																	headerValue='%{itemOrder2}' emptyOption="false"
																	theme="simple" />
														</td>
													</tr>
												</s:if>
												<tr>
													<td width="182" align="right">
														<span style="text-align:right;">Qty:</span>
													</td>
													<td width="53">
														<%-- Qty --%>
														<s:hidden name="itemQty" value="%{#qty}" />
														<s:hidden id="enteredQuantities_%{#id}" name="enteredQuantities" value="%{#qty}" />
														<s:hidden id="custUOM_%{#id}" name="custUOM" value="%{#customerUOM}" />
														<s:hidden name='initialQTY_%{#id}' id='initialQTY_%{#id}' value='%{#qty}' />
														
														<%-- UOM & Custom Fields --%>
														<s:if test="%{#itemType != '99.00'}">
															<s:textfield title="QTY" cssClass="x-input"
																	cssStyle="width:51px;" name="qtys" id="qtys_%{#id}"
																	maxlength="7" tabindex="1" value="%{#qty}"
																	onkeyup="isValidQuantityRemoveAlpha(this,event);isValidQuantity(this);updateHidden(this,'%{#id}');setFocus(this,event);"
																	theme="simple" />
															<s:hidden name='QTY_%{#id}' id='QTY_%{#id}' value='%{#qty}' />
															<s:hidden id="enteredUOMs_%{#id}" name="enteredUOMs" value="%{#itemUomId}" />
															<s:hidden id="itemBaseUOM_%{#id}" name="itemBaseUOM" value="%{#itemBaseUom}" />
															<s:hidden name='UOM_%{#id}' id='UOM_%{#id}' value="%{#itemUomId}" />
														</s:if>
														<s:else> <%-- itemType == '99.00' --%>
															<s:textfield title="QTY" cssClass="x-input"
																	cssStyle="width:51px;" name="qtys" id="qtys_%{#id}"
																	tabindex="1" value="%{#qty}"
																	onkeyup="isValidQuantityRemoveAlpha(this,event);updateHidden(this,'%{#id}');isValidQuantity(this);setFocus(this,event);"
																	theme="simple" readonly="true" />
															<s:hidden name='QTY_%{#id}' id='QTY_%{#id}' value='%{#qty}' />
														</s:else>
													</td>
													<td width="145">
														<%-- UOM & Custom Fields --%>
														<s:if test="%{#itemType != '99.00'}">
															<s:if test="#uomList != null">
																<s:select cssClass="xpedx_select_sm"
																		cssStyle="width:140px;" name="uoms" id="uoms_%{#id}"
																		list="#uomList" listKey="key" listValue="value"
																		value='itemUomId'
																		onchange="updateHidden(this,'%{#id}',0,'%{#_action.getJsonStringForMap(#itemUOMsMap)}');"
																		theme="simple" />
																<s:hidden name='initialUOM_key_%{#id}' id='initialUOM_key_%{#id}' value='%{#itemUomId}' />
																<s:set name="itemUomIdDesc" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#itemUomId)" />
																<s:hidden name='UOM_desc_%{#id}' id='UOM_desc_%{#id}' value="%{#itemUomIdDesc}" />
															</s:if>
															<s:hidden name='UOM_%{#id}' id='UOM_%{#id}' value="%{#itemUomId}" />
														</s:if>
														<s:else> <%-- itemType == '99.00' --%>
															<s:textfield cssClass="x-input" cssStyle="width:140px;"
																	name="uoms" value="%{#itemUomId}"
																	onchange="updateHidden(this,'%{#id}');"
																	theme="simple" readonly="true" />
															<s:hidden name='UOM_%{#id}' id='UOM_%{#id}' value=' ' />
														</s:else>
													</td>
												</tr>
		
												<s:set name="mulVal" value='itemOrderMultipleMap.get(#itemId1)' />
												<s:set name="erroMsg" value='%{erroMsg}' />
												<s:set name="itemIdUOMsMap" value='itemIdConVUOMMap.get(#itemId1)' />
												<s:iterator value='itemIdUOMsMap'>
													<s:set name='currentUomConvFact' value='value' />
													<s:hidden name='convF_%{#currentUomId}' id="convF_%{#currentUomId}" value="%{#currentUomConvFact}" />
												</s:iterator>
		
												<s:set name="defaultConvF" value='#itemIdUOMsMap.get(#itemUomId)' />
												<s:hidden name="orderLineOrderMultiple" id="orderLineOrderMultiple_%{#id}" value="%{#mulVal}" />
												<s:hidden name="orderLineItemIDs" id="orderLineItemIDs_%{#id}" value='%{#itemId}' />
												<s:hidden name="UOMconversion" id="UOMconversion_%{#id}" value="%{#defaultConvF}" />
		
												<s:hidden name='customerFieldsSize_%{#id}' id='customerFieldsSize_%{#id}' value='%{#_action.getCustomerFieldsMap().size()}' />
												<s:iterator value='customerFieldsMap' status='custFieldStatus'>
													<s:set name='FieldLabel' value='key' />
													<s:set name='FieldValue' value='value' />
		
													<s:set name='customKey' value='%{customerFieldsDBMap.get(#FieldLabel)}' />
													<s:set name='CustomFieldValue' value="%{' '}" />
		
													<s:if test="%{#item.getAttribute(#customKey)!=null && #item.getAttribute(#customKey)!=''}">
														<s:set name='CustomFieldValue' value='%{#item.getAttribute(#customKey)}' />
													</s:if>
													<tr>
														<td align="right">
															<label style="text-align: right;">
																<s:property value="%{#FieldValue}" />:
															</label>
														</td>
														<td colspan="2">
															<%-- Creating text field with name as the Customer field name --%>
		
															<%-- BB: Need to add an if statement here, to determine which cdf this is. one has a max of 22, the other 24. --%>
															<s:if test="%{#FieldLabel == 'CustLineAccNo'}">
																<s:textfield cssStyle="width:202px;" cssClass="x-input"
																	maxlength="24" name='customField%{#FieldLabel}s'
																	id="customField%{#FieldLabel}s" size='10'
																	value="%{@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getReplacedValue(#CustomFieldValue)}"
																	title="%{#FieldValue}"
																	onchange="updateHidden(this,'%{#id}','%{#custFieldStatus.count}');" />
																<s:hidden
																	name='customerField_%{#custFieldStatus.count}_%{#id}'
																	id='customerField_%{#custFieldStatus.count}_%{#id}'
																	value='%{"Extn"+#FieldLabel+"@"+#CustomFieldValue}' />
																<s:hidden id="entered%{#FieldLabel}_%{#id}"
																	name="entered%{#FieldLabel}" value="%{#CustomFieldValue}" />
															</s:if>
															<s:elseif test="%{#FieldLabel == 'CustomerPONo'}">
																<s:textfield cssStyle="width:202px;" cssClass="x-input"
																	maxlength="22" name='customField%{#FieldLabel}s'
																	id="customField%{#FieldLabel}s" size='10'
																	value="%{@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getReplacedValue(#CustomFieldValue)}"
																	title="%{#FieldValue}"
																	onchange="updateHidden(this,'%{#id}','%{#custFieldStatus.count}');" />
																<s:hidden
																	name='customerField_%{#custFieldStatus.count}_%{#id}'
																	id='customerField_%{#custFieldStatus.count}_%{#id}'
																	value='%{"Extn"+#FieldLabel+"@"+#CustomFieldValue}' />
																<s:hidden id="entered%{#FieldLabel}_%{#id}"
																	name="entered%{#FieldLabel}" value="%{#CustomFieldValue}" />
															</s:elseif>
															<s:else>
																<s:textfield cssStyle="width:202px;" cssClass="x-input"
																	maxlength="25" name='customField%{#FieldLabel}s'
																	id="customField%{#FieldLabel}s" size='10'
																	value="%{@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getReplacedValue(#CustomFieldValue)}"
																	title="%{#FieldValue}"
																	onchange="updateHidden(this,'%{#id}','%{#custFieldStatus.count}');" />
																<s:hidden
																	name='customerField_%{#custFieldStatus.count}_%{#id}'
																	id='customerField_%{#custFieldStatus.count}_%{#id}'
																	value='%{"Extn"+#FieldLabel+"@"+#CustomFieldValue}' />
																<s:hidden id="entered%{#FieldLabel}_%{#id}"
																	name="entered%{#FieldLabel}" value="%{#CustomFieldValue}" />
															</s:else>
														</td>
													</tr>
												</s:iterator>
		
											</table>
											<s:set name="baseUOM" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#baseUOMs.get(#itemId))"></s:set>
											<s:hidden name="baseUOM" id="baseUOM_%{#id}" value="%{#baseUOM}" />
											<s:set name="baseUOMCode" value="#baseUOMs.get(#itemId)"></s:set>
											<s:hidden name="baseUOMCode" id="baseUOMCode_%{#id}" value="%{#baseUOMCode}" />
											<s:set name="displayInventoryIndicator" value="displayInventoryCheckForItemsMap.get(#itemId)"></s:set>
											<s:hidden name="displayInventoryIndicatorvalue" value="#displayInventoryIndicator"></s:hidden>
											<s:if test='editMode != true'>
												<div class="cart-btn-wrap addmarginbottom10">
													<div class="mil-avail-link">
														<a id="PAAClick_<s:property value="#id"/>" href="javascript:writeMetaTag('DCSext.w_x_sc_count,DCSext.w_x_sc_itemtype','1,' + '<s:property value="#webtrendsItemType"/>');checkAvailability('<s:property value="#itemId"/>','<s:property value="#id"/>')">
															Show Price &amp; Availability
														</a>
													</div>
													<div class="mil-addcart-btn">
														<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
															<input name="button" type="button"  class="btn-gradient floatright" value="Add to Cart"
																	onclick="myAddItemToCart('<s:property value="#itemId"/>','<s:property value="#id"/>')" />
														</s:if>
														<s:else>
															<input name="button" type="button"  class="btn-gradient floatright" value="Add to Order"
																	onclick="myAddItemToCart('<s:property value="#itemId"/>','<s:property value="#id"/>')" />
														</s:else>
													</div>
												</div>
	
												<s:hidden name="isEditOrder" id="isEditOrder" value="%{#isEditOrderHeaderKey}" />
												
												<s:if test='%{#mulVal >"1" && #mulVal !=null}'>
												
													<div  id="errorDiv_qtys_<s:property value='%{#id}' />"><p class="notice addmarginbottom10">
														<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' />
														<s:property value="%{#xpedxUtilBean.formatQuantityForCommas(#mulVal)}" />
														<s:property value="#baseUOMDesc"></s:property></p>
													</div>
													<s:hidden name="hiddenUOMOrdMul_%{#id}"
															id="hiddenUOMOrdMul_%{#id}"
															value="%{@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#baseUOMs.get(#itemId))}"></s:hidden>
													<s:hidden name="hiddenQuantityOrdMul_%{#id}"
															id="hiddenQuantityOrdMul_%{#id}"
															value='%{#xpedxUtilBean.formatQuantityForCommas(#mulVal)}'></s:hidden>
													<s:hidden name="hiddenId" id="hiddenId" value="%{#id}" />
												</s:if>
												<s:else>
													<div class="notice addmarginbottom10" id="errorDiv_qtys_<s:property value='%{#id}' />" style="display: none; float: right;"></div>
												</s:else>
												
												<s:if test='%{#erroMsg !=null && #erroMsg !=""}'>
													<div class="error" style="display: none;" id="errorDiv_qtys_<s:property value='%{#id}' />" style="color:red"></div>
												</s:if>
												<s:else>
													<div class="error" style="display: none;" id="errorDiv_qtys_<s:property value='%{#id}' />" style="color:red"></div>
												</s:else>
												<div class="clearfix"></div>
		
											</s:if>
											<s:else> <%-- end editMode --%>
												<s:if test='%{#mulVal >"1" && #mulVal !=null}'>
													<div 
															id="errorDiv_qtys_<s:property value='%{#id}' />"><p class="notice addmarginbottom10">
															<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' />
														<s:property value="%{#xpedxUtilBean.formatQuantityForCommas(#mulVal)}"></s:property>
														<s:property value="#baseUOMDesc"></s:property></p>
													</div>
												</s:if>
												<s:else>
													<div class="notice addmarginbottom10" id="errorDiv_qtys_<s:property value='%{#id}' />" style="display: none; float: right;"></div>
												</s:else>
											</s:else>
											<s:if test='%{#displayInventoryIndicator=="I"}'>
												<div class='non-stock-item mil-nonstock-adjust'><div class='stock-icon'><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/icon-stock.png" width="25" height="25"/>
												</div>Not a Stocked Item<div class='contact'> Contact Customer Service to confirm pricing and any additional charges</div></div>
											</s:if>
											<s:if test='%{#displayInventoryIndicator=="M"}'>
												<div class='non-stock-item mil-nonstock-adjust'><div class='stock-icon'><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/icon-manufacturing.png" width="25" height="25"/>
												</div>Item Ships Directly from Mfr<div class='contact'> Contact Customer Service to confirm pricing and any additional charges</div></div>
											</s:if>
											<s:if test='%{#displayInventoryIndicator=="W"}'>
														
											</s:if>
											
											<s:if test='editMode != true'>
													<s:if test="(xpedxItemIDUOMToReplacementListMap.containsKey(#itemId) && xpedxItemIDUOMToReplacementListMap.get(#itemId) != null)">
														<div class="replacement-item width-315 mil-replacement-adjust"><p>
															This item will be replaced once inventory is depleted.&nbsp;<span class="inline float-right"><img
																alt="To replace or add item, click the Edit This List button."
																title="To replace or add item, click the Edit This List button."
																height="12" border="0" width="12"
																src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"/></span></p>
														</div>
													</s:if>
												</s:if>
												<s:else> <%-- editMode --%>
													<%-- Show Replacement link only in Edit mode --%>
													<s:if test="(xpedxItemIDUOMToReplacementListMap.containsKey(#itemId) && xpedxItemIDUOMToReplacementListMap.get(#itemId) != null)">
														<div class="replacement-item width-315 mil-edit-replacement-adjust">
															<a href="#linkToReplacement" class="modal red"
																	onclick='showXPEDXReplacementItems("<s:property value="#itemId"/>", "<s:property value="#id"/>", "<s:property value="#qty"/>");'>
																This item will be replaced once inventory is depleted.
															</a>
														</div>
													</s:if>
												</s:else>
		
										</div> <%-- / mil-action-list-wrap --%>
									</div> <%-- / mil-wrap-condensed or mil-wrap-condensed-mid or mil-wrap-condensed-bot --%>
									
									<div
										<s:if test='%{#status.count == 1}'>class="show-hide-wrap"</s:if>
										<s:else>class="show-hide-wrap"  style="background-color:#fafafa;"</s:else>>
										<%-- Do not remove the following variables as it is used in the included jsp--%>
										<s:set name="itemID" value='#itemId' />
										<s:set name="itemKEY" value='#id' />
										<s:if test='#itemOrder == null'>
											<s:set name="jsonKey" value='%{#itemId}' />
										</s:if>
										<s:else>
											<s:set name="jsonKey" value='%{#itemId+"_"+#itemOrder}' />
										</s:else>
										<div id="availabilityRow_<s:property value='#id'/>">
											<!-- end prefs  -->
											<s:if test="%{pnaHoverMap.containsKey(#jsonKey)}">
												<s:include value="../MyItems/XPEDXMyItemsDetailsItemAvailability.jsp"></s:include>
											</s:if>
										</div>
									</div>
									<div class="clearfix"></div>
								</s:div> <%-- / mil-wrap-condensed-container --%>
							</s:iterator>
						</s:else> <%-- / if-else empty item list --%>
	
						<div id="priceDiv" style="display: none;"></div>
					</s:form>
	
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
										<td><s:text name="Availability"></s:text></td>
									</tr>
								</table>
								<div id="availibility-grid"></div>
								<ul id="tool-bar" class="tool-bar-bottom">
									<li><a class="grey-ui-btn"
										href="javascript:checkUpdateAvailability();"> <span>Update
												Availability</span>
									</a></li>
								</ul>
								<s:hidden name='#action.name' id='xx' value='xx' />
								<s:hidden name='#action.namespace' value='/xx' />
							</s:form>
						</div>
					</div>
	
					<div class="clear"></div>
				
					<s:if test='editMode != true'>
						<div class="graybar">
							<input id="selAll1" class="forselected-input toggleAllSelected" type="checkbox" />
							<div>
								Select All<a href="#" class="indent20px paaLink">Show Price &amp; Availablity for Selected Items</a>
							</div>
						</div>
	
						<div class="view-hide-images">
							<a id="toggleview" class="viewbtn"></a>
						</div>
						
						<div class="button-container addpadtop10"> <%-- read-only bottom --%>
							<s:if test='itemCount > 0'>
								<s:if
									test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
									<input name="button" type="button"
											class="btn-add-items-qty-to-cart btn-gradient floatright addmarginleft10"
											value="Add Items with Qty to Cart" />
								</s:if>
								<s:else>
									<input name="button" type="button"
											class="btn-add-items-qty-to-cart btn-gradient floatright addmarginleft10"
											value="Add Items with Qty to Order" />
								</s:else>
							</s:if>
	
							<s:if test="%{canEditItem}">
								<input name="button" type="button"
										class="btn-neutral floatright addmarginleft10"
										value="Edit This List"
										onclick="document.getElementById('formEditMode').submit(); return false;" />
							</s:if>
	
							<input name="button" type="button"
									class="btn-neutral floatright addmarginleft10"
									value="Export This List" onclick="exportList(); return false;" />
						</div> <%-- / button-container --%>
					</s:if>
					<s:else> <%-- editMode --%>
						<div class="graybar">
							<input id="selAll1" class="forselected-input toggleAllSelected" type="checkbox" />
							<s:if test="editMode != true">
								<div>
									Select All<a href="#" class="indent20px paaLink">Show Price &amp; Availablity for Selected Items</a>
								</div>
							</s:if>
							<s:else> <%-- editMode --%>
								<div>
									Select All<a href="#" class="indent20px removeItemsLink">Remove Selected Items</a>
								</div>
							</s:else>
						</div>
						
						<div class="notice mil-count-selected mil-count-selected-bottom" style="display:none;">
							Placeholder text
						</div>
						<div class="clearfix"></div>
						
						<div class="view-hide-images addpadtop10">
							<a id="toggleview" class="viewbtn"></a>
						</div>
						
						<div class="button-container addpadtop10"> <%-- edit mode bottom --%>
							<input name="button" type="button" class="btn-gradient floatright addmarginleft10" value="Save Updates" onclick="saveAllItemsNew('mil-edit', ['quick-add']); return false;" />
							
							<input name="button" type="button" class="btn-import-items btn-neutral floatright addmarginleft10" value="Import List" />
							
							<s:if test="%{canShare || (#isEstUser && (#shareAdminOnlyFlg=='' || #shareAdminOnlyFlg=='N'))}">
								<input name="button" type="button" class="btn-share-list btn-neutral floatright  addmarginleft10" value="Share List" />
							</s:if>
						</div>
					</s:else>
	
					<div class="clearall"></div>
	
					<ul>
						<li style="text-align: center;">
	
							<div class="error" id="errorMsgBottom" style="display: none;"></div>
	
							<s:if
								test="%{errorMsg == 'InvalidImport'}">
	
								<script type="text/javascript">
									document.getElementById("errorMsgBottom").innerHTML = "The import file must be in .csv format." ;
									document.getElementById("errorMsgBottom").style.display = "inline"; 
								</script>
							</s:if>
						</li>
					</ul>
				</div> <%-- / mid-altwrap --%>
				<div class="clearall"></div>

				<script type="text/javascript">
					var milFileImportMsg = [];
					
					<s:if test="%{errorMsg!=null && errorMsg!= '' && errorMsg.indexOf('ROW_PROCESSING_ERROR')>-1}">
						<s:set name='errIndex' value='%{errorMsg.indexOf("@")}' />
						<s:set name='rowNums' value='%{errorMsg.substring(#errIndex +1, errorMsg.length())}' />

						var msg = 'Row(s) <s:property value="#rowNums" /> failed to import. The supplier part number(s) are not valid.';
						msg = msg.replace(/\-/g, ', ');
						milFileImportMsg.push(msg);
					</s:if>
					<s:if test='%{#parameters.errorMsgRowsMissingItemId != null && #parameters.errorMsgRowsMissingItemId.length == 1 && #parameters.errorMsgRowsMissingItemId[0] != ""}'>
						var msg = 'Row(s) <s:property value="%{#parameters.errorMsgRowsMissingItemId[0]}" /> failed to import. The supplier part number(s) are missing.';
						msg = msg.replace(/\-/g, ', ');
						milFileImportMsg.push(msg);
					</s:if>
				
					if (milFileImportMsg.length > 0) {
						displayImportErrorMessage(milFileImportMsg.join('<br/><br/>'));
					}
				</script>

				<br />
				<s:if test='editMode != true'>
					<!-- START Carousel -->

					<s:if test='xpedxYouMightConsiderItems.size() > 0'>
						<div class="mil-cart-bg mil" style="margin-top: 0px;">
							<div id="cross-sell" class="float-left">
								<span class="consider-text"> You might also consider...</span>
								<ul id="footer-carousel-left" class="jcarousel-skin-xpedx">
									<!-- Begin - Changes made by Mitesh for JIRA 3186 -->
									<s:if test='xpedxYouMightConsiderItems.size() < 4'>
										<div disabled="disabled"
											class="jcarousel-prev jcarousel-prev-hide-horizontal"></div>
										<div disabled="disabled"
											class="jcarousel-next jcarousel-next-hide-horizontal"></div>

									</s:if>
									<s:if test='xpedxYouMightConsiderItems.size() > 0'>
										<s:iterator value='xpedxYouMightConsiderItems' id='reltItem'
											status='iStatus'>

											<s:set name="itemAssetList"
												value='#xutil.getElementsByAttribute(#reltItem,"AssetList/Asset","Type","ITEM_IMAGE_1" )' />
											<s:if
												test='#itemAssetList != null && #itemAssetList.size() > 0 '>
												<s:set name="itemAsset" value='#itemAssetList[0]' />
												<s:set name='imageLocation'
													value="#xutil.getAttribute(#itemAsset, 'ContentLocation')" />
												<s:set name='imageId'
													value="#xutil.getAttribute(#itemAsset, 'ContentID')" />
												<s:set name='imageLabel'
													value="#xutil.getAttribute(#itemAsset, 'Label')" />
												<s:set name='imageURL'
													value="#imageLocation + '/' + #imageId " />
												<s:if test='%{#imageURL=="/"}'>
													<s:set name='imageURL'
														value='%{"/xpedx/images/INF_150x150.jpg"}' />
												</s:if>

												<s:set name='primaryInfo'
													value='XMLUtils.getChildElement(#reltItem, "PrimaryInformation")' />
												<s:set name='shortDesc'
													value='#primaryInfo.getAttribute("ShortDescription")' />
												<li><s:a
														href="javascript:processDetail('%{#reltItem.getAttribute('ItemID')}', '%{#reltItem.getAttribute('UnitOfMeasure')}')">
														<img src="<s:url value='%{#imageURL}' includeParams='none' />"
															title='<s:property value="%{#reltItem.getAttribute('ItemID')}"/>'
															width="91" height="94"
															alt="<s:text name='%{#imageMainLabel}'/>" />
														<!-- <b><s:property value="%{#reltItem.getAttribute('ItemID')}"/></b> -->
														<br />
														<!-- Added span for Jira 3931 -->
														<span class="short-description"> <s:property
																value="%{#shortDesc}" />
														</span>
														<br />
														<br />
														<br />
													</s:a></li>

											</s:if>
											<s:else>
												<s:set name='imageIdBlank'
													value='%{"/xpedx/images/INF_150x150.jpg"}' />
												<s:set name='primaryInfo'
													value='XMLUtils.getChildElement(#reltItem, "PrimaryInformation")' />
												<s:set name='shortDesc'
													value='#primaryInfo.getAttribute("ShortDescription")' />
												<li><s:a
														href="javascript:processDetail('%{#reltItem.getAttribute('ItemID')}', '%{#reltItem.getAttribute('UnitOfMeasure')}')">
														<img src="<s:url value='%{#imageIdBlank}'/>"
															title='<s:property value="%{#reltItem.getAttribute('ItemID')}"/>'
															width="91" height="94" alt="" />
														<!-- <b><s:property value="%{#reltItem.getAttribute('ItemID')}"/></b> -->
														<br />
														<!-- Added span for Jira 3931 -->
														<span class="short-description"> <s:property
																value="%{#shortDesc}" />
														</span>
														<br />
														<br />
														<br />
													</s:a></li>
											</s:else>
										</s:iterator>
									</s:if>
								</ul>
							</div> <%-- / cross-sell --%>
						</div>
					</s:if>
					<!-- END carousel -->
				</s:if>

				<s:set name='lastModifiedDateString' value="getLastModifiedDateToDisplay()" />
				<s:set name='lastModifiedUserId' value="lastModifiedUserId" />
				<s:set name='modifiedBy' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getLoginUserName(#lastModifiedUserId)' />
				<div class="last-modified-div">
					Last modified by
					<s:property value="%{#_action.getLastModifiedUserId()}" />
					on
					<s:property value="#lastModifiedDateString" />
				</div>
			</div> <%-- / container --%>
		</div> <%-- / main  --%>

		<s:action name="xpedxFooter" executeResult="true" namespace="/common" />

	</div> <%-- / main-container --%>
	<div class="loading-wrap"  style="display:none;">
        <div class="load-modal" ></div>
    </div> 

	<div style="display: none;">

		<!-- Light Box -->
		<div style="height: 202px; width: 600px; overflow: auto;">
			<s:if test='editMode == true'>
				<!-- START: XPEDX Panel for Replacement items -->
				<s:set name='tabIndex' value='3001' />
				<s:iterator value='xpedxItemIDUOMToReplacementListMap'>
					<s:set name='altItemList' value='value' />

					<div id="replacement_<s:property value='key'/>"
						class="xpedx-light-box">
						<h1>
							Replacement Item(s) for
							<s:property value="wCContext.storefrontId" />
							Item #:
							<s:property value='key' />
						</h1>
						
						<!-- Light Box -->
						<s:div cssStyle="height: %{#altItemList.size() > 1 ? '202px' : '250px'}; width: 580px; overflow: auto; border: 1px solid #CCC; border-radius: 6px;">
							<s:if test="#altItemList.size() > 0">
								<input type="hidden" id="rListSize_<s:property value='key'/>" value="<s:property value='#altItemList.size()'/>" />
							</s:if>
							<s:iterator value='#altItemList' id='altItem' status='iStatus'>
								<s:div cssClass="mil-wrap-condensed-container %{#iStatus.last ? 'last' : ''}">
									<div class="mil-wrap-condensed" style="min-height: 240px;">
		
										<s:set name='uId' value='%{key + "_" +#altItem.getAttribute("ItemID")}' />
		
										<!-- begin image / checkbox   -->
										<div class="mil-checkbox-wrap">
											<s:set name='altItemIDUOM' value='#_action.getIDUOM(#altItem.getAttribute("ItemID"), #altItem.getAttribute("UnitOfMeasure"))' />
											<s:set name='altItemPrimaryInfo' value='#util.getElement(#altItem, "PrimaryInformation")' />
											<s:set name='name' value='%{#altItemPrimaryInfo.getAttribute("ShortDescription")}' />
											<s:set name='rItemID' value='%{#altItem.getAttribute("ItemID")}' />
											<s:set name='rdesc1' value="%{#itemDescMap.get(rItemID)}" />
											<s:set name='rdesc' value='%{#altItemPrimaryInfo.getAttribute("Description")}' />
											<s:url id='pImg' value='%{#_action.getImagePath(#altItemPrimaryInfo)}' />
		
											<s:set name='ritemUomId' value='#altItem.getAttribute("UnitOfMeasure")' />
											<s:set name='ritemType' value='#altItem.getAttribute("ItemType")' />
		
											<s:url id='ritemDetailsLink' namespace="/catalog"
													action='itemDetails.action' includeParams='none'
													escapeAmp="false">
												<s:param name="itemID" value="#rItemID" />
												<s:param name="sfId" value="#parameters.sfId" />
												<s:param name="unitOfMeasure" value="#ritemUomId" />
											</s:url>
											<s:if test="#altItemList.size() == 1">
												<input name="relatedItems" type="radio" checked="true" />
												<input type="hidden" id="hUId_<s:property value='key'/>" value='<s:property value="#uId" />' />
											</s:if>
											<s:else>
												<input name="relatedItems"
													onclick="setUId('<s:property value="#uId" />');"
													type="radio" />
											</s:else>
											<div class="mil-question-mark">
												<a href='<s:property value="%{itemDetailsLink}" />'> <img
													src="<s:property value='%{#pImg}' />" width="150"
													height="150" alt="" />
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
											<s:if test="%{#repItemUOMStatus.first}">
												<s:set name="repItemUOM" value="key" />
											</s:if>
										</s:iterator>
										<s:hidden id="replacement_%{uId}_uom" name="replacement_%{uId}_uom" value="%{#repItemUOM}" />
		
										<!-- begin description  -->
										<div class="mil-desc-wrap">
											<div class="mil-wrap-condensed-desc replacement-items"
												style="height: auto; max-height: 59px;">
												<s:if test="%{#ritemType != 99}">
													<a href='<s:property value="%{ritemDetailsLink}" />'>
														<span class="full-description-replacement-model">
															<s:property value="#name" />
														</span>
													</a>
												</s:if>
											</div>
											<div class="mil-attr-wrap">
												<ul class="mil-desc-attribute-list prodlist">
													<a href='<s:property value="%{ritemDetailsLink}" />'>
														<s:property value='#rdesc' escape='false' />
													</a>
												</ul>
		
												<p class="mil-attr-storefront-item-id">
													<s:property value="wCContext.storefrontId" />
													Item #:
													<s:property value="#rItemID" />
												</p>
												<s:if test='skuMap!=null && skuMap.size()>0'>
													<s:set name='rItemSkuMap' value='%{skuMap.get(#rItemID)}' />
													<s:set name='rMfgItemVal'
														value='%{#rItemSkuMap.get(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MFG_ITEM_NUMBER)}' />
													<s:set name='rPartItemVal'
														value='%{#rItemSkuMap.get(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUST_PART_NUMBER)}' />
												</s:if>
												<s:if test='mfgItemFlag != null && mfgItemFlag=="Y"'>
													<p>
														<s:property value="#manufacturerItemLabel" />:
														<s:property value='#rMfgItemVal' />
													</p>
												</s:if>
												<s:if test='customerItemFlag != null && customerItemFlag=="Y"'>
													<p>
														<s:property value="#customerItemLabel" />:
														<s:property value='#rPartItemVal' />
													</p>
												</s:if>
		
											</div> <%-- / mil-attr-wrap --%>
										</div> <%-- / mil-desc-wrap --%>
									</div> <%-- / mil-wrap-condensed --%>
								</s:div> <%-- / mil-wrap-condensed-container --%>
							</s:iterator>
						</s:div> <%-- / wrapper for lightbox --%>
					</div> <%-- / replacement_ --%>
				</s:iterator>
			</s:if>
		</div> <%-- / wrapper for lightbox --%>
	<div style="display: none;">
		<div id="deleteItemDiv" class="xpedx-light-box">
			<h1> <s:text name='Delete Item From My Items List' /></h1>
			<s:form id="itemIdForRemoveFromListForm" method="post" name="itemIdForRemoveFromListForm" onsubmit="return false;">
				<input id= "itemKeyForRemoveFromList" name = "itemKeys" type="hidden" value=""/>
				<input id= "listKeyForRemove" name = "listKey" type="hidden" value=""/>
				<input id= "editModeForRemove" name = "editMode" type="hidden" value="true"/>
				<input id= "listNameForRemove" name = "listName" type="hidden" value=""/>
				<input id= "listDescForRemove" name = "listDesc" type="hidden" value=""/>
			</s:form>
				<p class="addmargintop0"><s:text name='Are you sure want to delete this item from your My Items List?' /></p>
					<ul id="tool-bar" class="tool-bar-bottom float-right">
						<li>
							<a class="btn-neutral" href="javascript:$.fancybox.close();">
								<span>Cancel</span>
							</a>
						</li>
						<li class="float-right">
							<a class="btn-gradient" href="javascript:fancyBoxCloseAndDelItem();">
								<span>Yes</span>
							</a>
						</li>
					</ul>
		</div>
	</div>
		
		<s:form action="addComplementaryItemToCart" namespace="/order" method="POST" name="addReplacementItemToCartForm" id="addReplacementItemToCartForm">
			<div id="replacementItems" style="height: 380px; display: none;">
	
				<s:hidden name='#action.name' id='validationActionName'
					value='addReplacementItemToCart' />
				<s:hidden name='#action.namespace' value='/order' />
	
				<div id="replacementItemBody" class="xpedx-light-box" />
			</div>
			
			<div class="button-container addpadtop15"> <%-- replacement modal --%>
				<input class="btn-gradient floatright addmarginright10" type="submit" value="Replace" onclick="replacementReplaceInList(selReplacementId);" />
				<input class="btn-gradient floatright addmarginright10" type="submit" value="Add" onclick="replacementAddToList(selReplacementId);" />
				<input class="btn-neutral floatright addmarginright10" type="submit" value="Close" onclick="$.fancybox.close();" />
			</div>
		</s:form>
		
		<s:form id="formRIAddToList" action="XPEDXMyItemsDetailsCreate" method="post">
			<s:hidden name="listKey" value="%{listKey}"></s:hidden>
			<s:hidden name="listName" value="%{listName}"></s:hidden>
			<s:hidden name="listDesc" value="%{listDesc}" />
			<s:hidden name="itemCount" value="%{itemCount}"></s:hidden>
			<s:hidden name="editMode" value="%{true}"></s:hidden>
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
		</s:form>
		
		<s:form id="formRIReplaceInList" action="MyItemsDetailsChange" method="post">
			<s:hidden name="listKey" value="%{listKey}"></s:hidden>
			<s:hidden name="listName" value="%{listName}"></s:hidden>
			<s:hidden name="listDesc" value="%{listDesc}" />
			<s:hidden name="itemCount" value="%{itemCount}"></s:hidden>
			<s:hidden name="editMode" value="%{true}"></s:hidden>
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
		</s:form>
	</div> <%-- / wrapper for lightbox --%>

	<div class="hp-ad">
	</div>

	<script type="text/javascript">
		function updateValidation(){
			$(".numeric").numeric();	
		}
		updateValidation();
	</script>

	<!-- Web Trends tag start -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<!-- Web Trends tag end  -->
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<!-- Lightbox/Modal Window -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>
	
	<!-- page specific JS includes -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/MyItems/XPEDXMyItemsDetails<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/MyItems/XPEDXMyItemsDetails-QuickAdd<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/MyItems/XPEDXAddToCartAndAvailability<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numberformatter-1.1.0<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.blockUI<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<script type="text/javascript">
		$('.mil-wrap-condensed-container').hover(
			function() { $(this).addClass('green-background'); }, 
			function() { $(this).removeClass('green-background'); }
		);
	</script>
</body>
</html>
