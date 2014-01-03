<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<%
  		request.setAttribute("isMergedCSSJS","true");
  	  %>
<s:set name="CurrentCustomerId"
	value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />
<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
<s:set name='currentCartInContextOHK' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("OrderHeaderInContext")'/>
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name="isEstUser" value='%{#xpedxCustomerContactInfoBean.isEstimator()}' />
<s:url id='uomDescriptionURL' namespace="/common" action='getUOMDescription' />
<s:hidden name="uomDescriptionURL" value='%{#uomDescriptionURL}'/>

<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean" id="xpedxUtilBean" />
<s:bean	name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils' id='xutil' />
<!-- Added for JIRA 1402 Starts -->
<s:set name='ItemSize' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("listOfItemsSize")'/>
<!-- Added for JIRA 1402 Ends-->
<%-- 
<s:if test="#currentCartInContextOHK == null ">
	<s:set name='currentCartInContextOHK' value='@com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper@getCartInContextOrderHeaderKey(wCContext)'/>
</s:if>
--%>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils' id='xpedxSCXmlUtil' />
<s:set name='outDoc2' value='%{outDoc.documentElement}' />
<script type="text/javascript">
	var isUserAdmin = <s:property value="#isUserAdmin"/>;
	var isEstUser = <s:property value="#isEstUser"/>;

</script>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->


<!-- carousel scripts js   -->

<s:set name="customerPONoFlag" value='%{customerFieldsMap.get("CustomerPONo")}'></s:set>
<s:set name="jobIdFlag" value='%{customerFieldsMap.get("CustLineAccNo")}'></s:set>
<s:set name="custPONo" value='%{customerFieldsMap.get("CustomerPONo")}'></s:set>

<!-- begin styles. These should be the only three styles. -->

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<!-- end styles -->

<!-- For the number formatting -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MIL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<s:include value="../order/XPEDXRefreshMiniCart.jsp"/>
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
	var divId;
	var isGlobal;
	var errorflag;
	var addToCartFlag;
	var validAddtoCartItemsFlag  = new Array();
	function hideSharedListFormIfPrivate() {
		var theForm=document.getElementById("XPEDXMyItemsDetailsChangeShareList");
		if(theForm!=null){
			var radioBtns = theForm.sharePermissionLevel;
			var div = document.getElementById("dynamiccontent");
			if(radioBtns[0].checked) {
				div.style.display = "none";
			}
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
		
		/* Begin Short desc. shortener */
		$('.short-description').each(function() {
			var html = $(this).html();
			var shortHTML = html.substring(0, 90);
			if( html.length > shortHTML.length )
			{
				$(this).html(shortHTML);
				$(this).append('...');	
				$(this).attr('title', html );
			}
		});
		
		/* Begin long desc. shortener */
		$('.prodlist ul li, #prodlist ul li').each(function() {
			var html = $(this).html();
			var shortHTML = html.substring(0, 30);
			if( html.length > shortHTML.length )
			{
				$(this).html(shortHTML);
				$(this).append('...');	
				$(this).attr('title', html );
			}
		});
		
		
		
		$("#dlgShareListLink1").fancybox({
			'onStart' 	: function(){
			if (isUserAdmin || isEstUser){			
				//Calling AJAX function to fetch 'Ship-To' locations only when user is an Admin
				showShareList('<s:property value="#CurrentCustomerId"/>', true);
				hideSharedListFormIfPrivate();
				}
			},
			'onClosed':function() {
				var theForm=document.getElementById("XPEDXMyItemsDetailsChangeShareList");
				theForm.shareAdminOnly.checked=false;
				var radioBtns = theForm.sharePermissionLevel;
				var div = document.getElementById("dynamiccontent");
				if(!isUserAdmin &&  !isEstUser)
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
			if (isUserAdmin || isEstUser){
				//Calling AJAX function to fetch 'Ship-To' locations only when user is an Admin
				showShareList('<s:property value="#CurrentCustomerId"/>', true);
				hideSharedListFormIfPrivate();
				}
			},
			'onClosed':function() {
				var theForm=document.getElementById("XPEDXMyItemsDetailsChangeShareList");
				theForm.shareAdminOnly.checked=false;
				var radioBtns = theForm.sharePermissionLevel;
				var div = document.getElementById("dynamiccontent");
				if(!isUserAdmin && !isEstUser)
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
     var priceCheck;
     var myMask;
     	function checkAvailability(itemId,myItemsKey) {
     		priceCheck = true;
     		addToCartFlag = false;
     		//added for jira 3974
     		var waitMsg = Ext.Msg.wait("Processing...");
     		myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
     		myMask.show();
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
            		Ext.Msg.hide();
            		myMask.hide();
         		}
     		}
     		else{
        		Ext.Msg.hide();
        		myMask.hide();
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
			javascript:updateSelectedPAA( );
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
		var myMask;
		//Resets the Messages and calls the actual javascript function
		function myAddItemToCart(itemId, id){
			//added for jira 3974
			var waitMsg = Ext.Msg.wait("Processing...");
			myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
			myMask.show();
			//Added isGlobal for Jira 3770
			isGlobal = true;
			addToCartFlag = true;
			//Clear previous messages if any
			clearPreviousDisplayMsg();
			javascript:addItemToCart(itemId, id );
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
					//Code Fix For Jira 3197
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
    					var divVal=document.getElementById(divId);    					
    					if(response.responseText.indexOf(addedItems[i].value+"_"+arrQty[i].value+"_"+arrUOM[i].value) !== -1){
    						divVal.innerHTML = "Item has been added to your cart. Please review the cart to update the item with a valid quantity." ;
    						divVal.setAttribute("class", "error");
    					}
    					else{
    						divVal.innerHTML = "Item has been added to cart." ;
    						 divVal.setAttribute("class", "success");
    					}
						  divVal.style.display = "inline-block"; 
						  divVal.setAttribute("style", "margin-right:5px;float:right;");
						 
    					
    				}
     	   }
		}
     	   }
		var addItemsWithQty;
		var myMask;
		function addToCart(){
			//added for jira 3974
			var waitMsg = Ext.Msg.wait("Processing...");
			myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
			myMask.show();
			addItemsWithQty = true;
			isGlobal = false;
			clearPreviousDisplayMsg();
			
			resetQuantityError();
			 if( validateOrderMultipleFromAddQtyToCart(false,null) == false)
			 {	
				 //Added displayMsgHdrLevelForLineLevelError() here for displaying error msg when Add Item with Qty To cart
				 //displayMsgHdrLevelForLineLevelError ();
				 if(addToCartFlag == false){
				 Ext.Msg.hide();
			         myMask.hide();
			         return;
				 }
					
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
	            //Commented for 3475
				//Ext.Msg.wait("Processing...");      
				//Ext.Msg.wait("Adding items to cart...Please wait!");
	                     //xpedx_working_start();
                         //setTimeout(xpedx_working_stop, 3000);
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
		                    Ext.Msg.hide();
		                	myMask.hide();
		       			 }
	  		     	    else if(draftErr.indexOf("We were unable to add some items to your cart as there was an invalid quantity in your list. Please correct the qty and try again.") >-1)
	   		     		 {			
		            	    refreshWithNextOrNewCartInContext();
		                    draftErrDiv.innerHTML = "<h5 align='center'><b><font color=red>" + response.responseText + "</font></b></h5>";
		                    Ext.Msg.hide();
		                	myMask.hide();
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
	                	    Ext.Msg.hide();
				    		myMask.hide();
	   		         	}
	                   },
	                   failure: function (response, request){
	                	   setMsgOnAddItemsWithQtyToCart(response);
	                	   Ext.Msg.hide();
			   	   		   myMask.hide();
	                	}
	               });    
	                } 
   	                
				//END - Submit the form via ajax
				
				
			} else {
				Ext.Msg.hide();
	            		myMask.hide();
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
					Ext.Msg.hide();
					myMask.hide();
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
						Ext.Msg.hide();
						myMask.hide();
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
		/*		if(ordMul!= 0 && addItemsWithQty != true)
				{
					//divVal.innerHTML="You must order in units of "+ arrOrdMul[i].value+", please review your entry and try again.";
					//divVal.innerHTML="<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + arrOrdMul[i].value +" "+baseUOM[i].value ;
					//added for jira 3253
					if (priceCheck == true){
						divVal.setAttribute("class", "error");
						divVal.style.display = 'block';
						}
					else {
					divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul.value) +" "+baseUOM.value ;
					divVal.setAttribute("class", "error");
					divVal.style.display = 'block';
					document.getElementById(arrQty.id).style.borderColor="#FF0000";
					document.getElementById("errorMsgTop").innerHTML = "An error has occured with one or more of your items. Please review the list and try again." ;
		            document.getElementById("errorMsgTop").style.display = "inline";
					
		            document.getElementById("errorMsgBottom").innerHTML = "An error has occured with one or more of your items. Please review the list and try again." ;
		            document.getElementById("errorMsgBottom").style.display = "inline";
		            uomCheck = true;
		            Ext.Msg.hide();
			    myMask.hide();
					}
					errorflag= false; validAddtoCartItemsFlag[0]=false;
					isAddToCart=false;
				}
				
				else Commented for XB 214 BR4 to remove the validation of requested Qty before PnA response */
				
				if(addItemsWithQty == true){
					addToCartFlag = true;
					/* Commented for Order multiple CR - CR2if(ordMul!= 0){
						divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul.value) +" "+baseUOM.value ;
						divVal.setAttribute("class", "error");
						divVal.style.display = 'block';
						document.getElementById(arrQty.id).style.borderColor="#FF0000";
						document.getElementById("errorMsgTop").innerHTML = "An error has occured with one or more of your items. Please review the list and try again." ;
			            document.getElementById("errorMsgTop").style.display = "inline";
						
			            document.getElementById("errorMsgBottom").innerHTML = "An error has occured with one or more of your items. Please review the list and try again." ;
			            document.getElementById("errorMsgBottom").style.display = "inline";
			            uomCheck = true;
			            errorflag= false; validAddtoCartItemsFlag[0]=false;
						isAddToCart=false;
						Ext.Msg.hide();
						myMask.hide();
					}
					else{*/
						validAddtoCartItemsFlag[0]=true;
					//}
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
				
				//Changed to || if((quantity == '0' || quantity== '' ) && isOnlyOneItem == true) JIRA 3197
				//alert("isGlobal==="+ isGlobal);
				if(isGlobal == true){
					
					if(quantity == '0' || quantity== '' ){
							//Display Generic Message at Header level first then Update Line Level message.
						
						/* divVal.innerHTML='Qty Should be greater than 0'; */
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
						
						//alert("WE are in if block of quantity == '0'");
						//divVal.innerHTML="You must order in units of "+ arrOrdMul[i].value+", please review your entry and try again.";
						divVal.innerHTML= " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul[i].value) + " "+baseUOM[i].value;
						divVal.setAttribute("class", "notice");
						divVal.style.display = 'block';
						//document.getElementById(arrQty[i].id).style.borderColor="#FF0000";
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
								/*document.getElementById("errorMsgTop").innerHTML = "Item # "+unEntitledItemIDs+" is currently not valid. Please delete it from your list and contact Customer Service.";
						        document.getElementById("errorMsgTop").style.display = "inline";
									
						        document.getElementById("errorMsgBottom").innerHTML = "Item # "+unEntitledItemIDs+" is currently not valid. Please delete it from your list and contact Customer Service.";
						        document.getElementById("errorMsgBottom").style.display = "inline";
								errorflag= false;*/
					}
					}
					//end of XB 224
					var totalQty = arrUOM[i].value * quantity;
					if(arrOrdMul[i].value == undefined || arrOrdMul[i].value.replace(/^\s*|\s*$/g,"") =='' || arrOrdMul[i].value == null)
					{
						arrOrdMul[i].value=1;
					}
					var ordMul = totalQty % arrOrdMul[i].value;
					isQuantityZero = false;
			/*		if(ordMul!= 0 && addItemsWithQty != true)
					{
						//divVal.innerHTML="You must order in units of "+ arrOrdMul[i].value+", please review your entry and try again.";
						//divVal.innerHTML="<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + arrOrdMul[i].value +" "+baseUOM[i].value ;
						//added for jira 3253
						if (priceCheck == true){
							divVal.setAttribute("class", "error");
							divVal.style.display = 'block';
							}
						else {
						divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul[i].value) +" "+baseUOM[i].value ;
						divVal.setAttribute("class", "error");
						divVal.style.display = 'block';
						document.getElementById(arrQty[i].id).style.borderColor="#FF0000";
						document.getElementById("errorMsgTop").innerHTML = "An error has occured with one or more of your items. Please review the list and try again." ;
			            document.getElementById("errorMsgTop").style.display = "inline";
						
			            document.getElementById("errorMsgBottom").innerHTML = "An error has occured with one or more of your items. Please review the list and try again." ;
			            document.getElementById("errorMsgBottom").style.display = "inline";
			            uomCheck = true;
			            Ext.Msg.hide();
				    	myMask.hide();
						}
						errorflag= false; validAddtoCartItemsFlag[i]=false;
						isAddToCart=false;
					}
					
					else Commented for XB 214 BR4 to remove the validation of requested Qty against the order multiple before PnA response*/
					
					if(addItemsWithQty == true){
						addToCartFlag = true;
						/* Commented for Order Multiple CR-2 if(ordMul!= 0){
							divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul[i].value) +" "+baseUOM[i].value ;
							divVal.setAttribute("class", "error");
							divVal.style.display = 'block';
							document.getElementById(arrQty[i].id).style.borderColor="#FF0000";
							document.getElementById("errorMsgTop").innerHTML = "An error has occured with one or more of your items. Please review the list and try again." ;
				            document.getElementById("errorMsgTop").style.display = "inline";
							
				            document.getElementById("errorMsgBottom").innerHTML = "An error has occured with one or more of your items. Please review the list and try again." ;
				            document.getElementById("errorMsgBottom").style.display = "inline";
				            uomCheck = true;
				            errorflag= false; validAddtoCartItemsFlag[i]=false;
							isAddToCart=false;
							Ext.Msg.hide();
					    		myMask.hide();
						}
						else{*/
							validAddtoCartItemsFlag[i]=true;
						//}
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
				Ext.Msg.hide();
    			myMask.hide();
			}
			return errorflag;
			
		}
// Function End - Jira 3770
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
			//added for XB-224
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
				//End of XB 224

				if (priceCheck == true && (addToCartFlag == false || addToCartFlag == undefined)){
					// added for XB 214 BR2
					if(quantity == ''){
						if(arrOrdMul[i].value!=null || arrOrdMul[i].value!='')
							quantity = arrOrdMul[i].value; 
					}
				}
				
				//Changed to || if((quantity == '0' || quantity== '' ) && isOnlyOneItem == true) JIRA 3197
				//Added For Jira 3770
				if(isGlobal == true){
					
					if(quantity == '0' || quantity== '' ){
							//Display Generic Message at Header level first then Update Line Level message.
						
						/* divVal.innerHTML='Qty Should be greater than 0'; */
							divVal.innerHTML="<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGTZERO' />";
							divVal.setAttribute("class", "error");
							divVal.style.display = 'block';
							document.getElementById(arrQty[i].id).style.borderColor="#FF0000";
							//Ctrl.focus();
							errorflag= false;
						}
					isQuantityZero = false;
				}
				//End Fix For Jira 3770
				//modified error msg & added error msg at header level for XB 214 BR4
				if((quantity == '0') )
				{
					if((arrOrdMul[i].value!=null || arrOrdMul[i].value!='') && arrOrdMul[i].value>1)
					{
						//divVal.innerHTML="You must order in units of "+ arrOrdMul[i].value+", please review your entry and try again.";
						divVal.innerHTML= " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul[i].value) + " "+baseUOM[i].value;
						divVal.style.display = 'block';
					}
					//Display Generic Message at Header level first then Update Line Level message.
					
					/* divVal.innerHTML='Qty Should be greater than 0'; */
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
						//Ctrl.focus();
					
				} //end of changes to XB 214
				else if(quantity>0){
					var totalQty = arrUOM[i].value * quantity;
					if(arrOrdMul[i].value == undefined || arrOrdMul[i].value.replace(/^\s*|\s*$/g,"") =='' || arrOrdMul[i].value == null)
					{
						arrOrdMul[i].value=1;
					}
					var ordMul = totalQty % arrOrdMul[i].value;
					isQuantityZero = false;
					/* Commented for Order multiple CR2if(ordMul!= 0)
					{
						//divVal.innerHTML="You must order in units of "+ arrOrdMul[i].value+", please review your entry and try again.";
						//divVal.innerHTML="<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + arrOrdMul[i].value +" "+baseUOM[i].value ;
						//added for jira 3253
						if (priceCheck == true && addToCartFlag != true){
							divVal.setAttribute("class", "error");
							divVal.style.display = 'block';
							}
						else {
						divVal.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(arrOrdMul[i].value) +" "+baseUOM[i].value ;
						divVal.setAttribute("class", "error");
						divVal.style.display = 'block';
						document.getElementById(arrQty[i].id).style.borderColor="#FF0000";
						document.getElementById("errorMsgTop").innerHTML = "An error has occured with one or more of your items. Please review the list and try again." ;
			            document.getElementById("errorMsgTop").style.display = "inline";
						
			            document.getElementById("errorMsgBottom").innerHTML = "An error has occured with one or more of your items. Please review the list and try again." ;
			            document.getElementById("errorMsgBottom").style.display = "inline";
			            uomCheck = true;
			            Ext.Msg.hide();
				    myMask.hide();
						}
						errorflag= false;
					}
					else*/ if (arrOrdMul[i].value > 1 && priceCheck == true && errorMsgFlag == false){
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
				//errorflag= false;
				//Ext.Msg.hide();
            	//myMask.hide();
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
			//Ext.get('dlgCopyAndPasteText').dom.value = '';
			//$.fancybox.close();
			
			//console.debug("data: ", data);
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
			//var w = Ext.WindowMgr.get("dlgCopyAndPaste");
			//w.hide();
		}
		
		function exportList(){
			//Wait for 5 sec and them take the dialog off 
			var task = new Ext.util.DelayedTask(function(){
			    Ext.MessageBox.hide();
			});
			task.delay(5000);
			//document.getElementById('formItemIds').command.value = 'export_list'; 
			//document.getElementById('formItemIds').submit();
			
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
			//Added For 3197 - No items with quantity defined. Please review the list and try again.
			//var msgGenericForLineLevelErrors = "<s:text name='MSG.SWC.MIL.GENHDRLEVELMSG.ERROR.LINELEVELERROS' />";
			document.getElementById("errorMsgTop").innerHTML = "No items with quantity defined. Please review the list and try again." ;
            document.getElementById("errorMsgTop").style.display = "inline";
            
            document.getElementById("errorMsgBottom").innerHTML = "No items with quantity defined. Please review the list and try again." ;
            document.getElementById("errorMsgBottom").style.display = "inline";
			
		}
		
		var myMask;
		function updateSelectedPAA(){
			//added for jira 3974
			var waitMsg = Ext.Msg.wait("Processing...");
			myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
			myMask.show();
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
	                     	Ext.Msg.hide();
				        	myMask.hide();
				      //-- Web Trends tag start --
						//alert(responseText);
			            writeWebtrendTag(responseText);
			            //-- Web Trends tag end --
	                   },

	                   failure: function (response, request){						
						  Ext.Msg.hide();
				          	  myMask.hide();
						  alert("Your request could not be completed at this time, please try again.");	                   }
	               });     
		}if(cnt <=0 ){
				
				//alert("Please select at least one item for Price Check : " + cnt );
				//var msgSelectItemFirst = "You have not selected any items for Price Check. Please select an item and try again";
				var msgSelectItemFirst = "<s:text name='MSG.SWC.MIL.NOITEMSELECT.ERROR.SELECTFORPNA' />";
				document.getElementById("errorMsgTop").innerHTML = msgSelectItemFirst ;
                		document.getElementById("errorMsgTop").style.display = "inline";

				document.getElementById("errorMsgBottom").innerHTML = msgSelectItemFirst ;
                		document.getElementById("errorMsgBottom").style.display = "inline";
                		Ext.Msg.hide();
	            		myMask.hide();
				
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
			// Begin - Changes made by Mitesh Parikh for 2422 JIRA
			<s:if test='editMode != true'>			
				<s:set name="itemDtlBackPageURL" value="%{itemDtlBackPageURL}" scope="session"/>
			</s:if>
			// End - Changes made by Mitesh Parikh for 2422 JIRA
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
            <!-- Added for JIRA 1402 Starts-->
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
    </script>
    <!-- Added for JIRA 1402 Ends--> 

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
<s:url id='checkAvailabilityURL' action='getItemAvailabilty' namespace="/myItems" />
<input type="hidden" value="<s:property value='%{checkAvailabilityURL}'/>" id="checkAvailabilityURLHidden"/>
<s:url id='addToCartURL' namespace='/order' action='addToCart' />
<s:url id='addToCartURLId' namespace="/myItems" action='addMyItemToCart' />
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
	style="width: 400px; height: 350px;">
<h2>Copy and Paste</h2>
<%-- <p>Copy and Paste the quantities and <s:property value="wCContext.storefrontId" /> item #'s from your file. --%>
<!-- Enter one item per line:<br /> -->
<!-- Qty. [Tab or Comma] Item#</p> -->
<p>Copy and paste or type the quantities and <s:property value="wCContext.storefrontId" />  item numbers or customer item numbers from your file in the following format: quantity,item number (no spaces). <br/>Example:12,5002121 </p>
<p>To enter items without quantities, copy and paste or type a comma followed by the item number (no spaces).<br/> Example: ,5002121  <br />
</p>
<br />
<form id="form1" name="form1" method="post" action=""><textarea
	name="dlgCopyAndPasteText" id="dlgCopyAndPasteText" cols="48" rows="5"></textarea>
<ul id="tool-bar" class="tool-bar-bottom" style="float:right";>
	<li><a class="grey-ui-btn" href="javascript:$.fancybox.close();"
		onclick="Ext.get('dlgCopyAndPasteText').dom.value = '';Ext.get('errorMsgCopyBottom').dom.innerHTML='';Ext.get('errorMsgCopyBottom').dom.style.display='none'"><span>Cancel</span></a></li>
	<li style="float: right;"><a href="javascript: quickAddCopyAndPaste( document.form1.dlgCopyAndPasteText.value);" class="green-ui-btn" style="margin-left:5px;"><span>Add to Quick List</span></a></li>
	
	
</ul>
</form>
</br></br></br><div class="error" id="errorMsgCopyBottom" style="display:none;position:relative;left:130px" ></div>
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
	
        <div class="container" style="min-height: 535px;">
            <!-- breadcrumb -->
			<s:url action='home.action' namespace='/home' id='urlHome'
				includeParams='none' /> <s:url id='urlMIL' namespace='/myItems'
				action='MyItemsList.action' includeParams="get" escapeAmp="false">
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
			<div id ="errorMessageDiv"> </div>

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
				<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif" width="16" height="15" alt="Print Page" />Print Page</span></a> </div>
		    
 		<%--             
 			<s:if test='editMode != true'>	 	
				<div id="breadcumbs-list-name" > <span class="page-title"> My Items List:</span> &nbsp; <span><s:property value="listName" /> </span>
				<a href="javascript:window.print()"><span class="print-ico-xpedx underlink" style="font-weight:normal; font-size:12px;">
				<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon.gif" width="16" height="15" alt="Print Page" />Print Page</span></a> </div>
			</s:if>
			<s:else>
				<div id="breadcumbs-list-name"><span class="page-title"> Edit My Items List: </span> &nbsp; <span><s:property value="listName" /> </span>
				<a href="javascript:window.print()"><span class="print-ico-xpedx underlink" style="font-weight:normal; font-size:12px;">
				<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon.gif" width="16" height="15" alt="Print Page" />Print Page</span></a> </div>
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
                        <div class="smallBody">
						
						<img style="margin-top:5px; padding-right:5px;display: inline;" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/mil/ad-arrow<s:property value='#wcUtil.xpedxBuildKey' />.gif" width="7" height="4" alt="" />advertisement</div>
						<!-- Added for EB-1714 Display a Saalfeld advertisement image on MIL Starts -->
							 <s:set name='storefrontId' value="wCContext.storefrontId" />
							<s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storefrontId)}'>
							 <img width="468" height="60" border="0" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/ad_placeholders/xpedx_468x60<s:property value='#wcUtil.xpedxBuildKey' />.jpg"/>
							 </s:if>
							<s:elseif test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>
							 <img width="468" height="60" border="0" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/images/SD_468x60<s:property value='#wcUtil.xpedxBuildKey' />.jpg"/>
	                        </s:elseif>
                   		<!-- EB-1714 END -->     
                        <div class="clear"></div>
                        <!-- Ad Juggler Tag Starts -->
                     <%--    <s:set name='ad_keyword' value='' />						
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
				
				<!--aj_server : https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/  -->
				
		<s:if test='%{#ad_keyword != null}' >
			<s:if test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118165'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:if>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CANADA_STORE_FRONT}' >
								<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118201'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@BULKLEY_DUNTON_STORE_FRONT}' >
									<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118189'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif >
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115717'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:else>
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115717'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:else>			
		</s:if>	
		<s:else>
			<s:if test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118165'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:if>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CANADA_STORE_FRONT}' >
								<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118201'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@BULKLEY_DUNTON_STORE_FRONT}' >
									<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118189'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif >
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115717'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:else>
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115717'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:else>			
		</s:else>
			<script type="text/javascript" language="JavaScript" src="https://img.hadj7.adjuggler.net/banners/ajtg.js"></script> --%>
			<!-- Ad Juggler Tag Ends -->
</div>
</s:if>
		 	<s:if test='editMode != true'> 
                <div>
					 <span class="grey" style="width:421px; word-wrap:break-word; float:left;"><s:property value="listDesc" /></span>
               	</div><!-- Close mil-edit -->
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
                    <div id="quick-add" class="quick-add float-right ">
                        <div class="clear">&nbsp;</div>
						<s:hidden id="mandatoryFieldCheckFlag_quick-add" name="mandatoryFieldCheckFlag_quick-add" value="%{false}"></s:hidden>
                        <h2 style="float: left; margin-top:5px;">Quick Add</h2>
                        <s:set name='storefrontId' value="wCContext.storefrontId" />
						<s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storefrontId)}'>
                        <p class="quick-add-aux-links underlink" style="color: orange;margin-top:5px; margin-right:5px;"> <a id="various2" class="modal" href="#dlgCopyAndPaste" onclick="javascript: writeMetaTag('DCSext.w_x_ord_quickadd_cp', '1');">Copy and Paste</a></p>
						</s:if>
						<s:elseif test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>
						<p class="quick-add-aux-links underlink" style="color:  #006a3a;margin-top:5px; margin-right:5px;"> <a id="various2" class="modal" href="#dlgCopyAndPaste" onclick="javascript: writeMetaTag('DCSext.w_x_ord_quickadd_cp', '1');">Copy and Paste</a></p>
						</s:elseif>
                        <div class="clear">&nbsp;</div>
                        <div class="quick-add-form-top">&nbsp;</div>
                        <div class="quick-add-form quick-add-form-mil">
							<form class="form selector" id="quick-add-selector" name="quickaddselector">
							<br/>
							
                                <ul class="hvv">
                                    <li>
                                        <label>Item Type</label>
										<s:select name="itemType" cssStyle="width:100px; margin-left: 0px;" headerKey="1" list="skuTypeList" listKey="key" listValue="value"/>
                                    </li>
                                    <li>
                                        <label>Item #</label>
										<p class="p-word-wrap"><s:textfield maxlength="27" cssStyle="width:50px;" cssClass="text x-input" name="prodId" onkeyup="javascript:listNameCheck(this, 'quick-add');"	onmouseover="javascript:listNameCheck(this, 'quick-add');"></s:textfield></p>
                                    </li>
                                    <li>
                                        <label>Qty</label>
										<s:textfield maxlength="7" cssStyle="width:63px;" cssClass="qty-field text x-input" name="qty" value="" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);"></s:textfield>
                                    </li>
                                    <!-- This condition check is also applied to the kind of css file that's been included. Refer in this page above in the <head> tag. -->
									<s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
										<li>
											 <label><s:property value="#custPONo" />:</label>
											 <!--Blank space removed 3693  -->
											<s:textfield maxlength="22"  cssStyle="width:154px;" cssClass="text x-input" name="purchaseOrder" value=""></s:textfield>
										</li>
									</s:if>
                                    <s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>
	                                    <li>
	                                        <!-- label>Job Number:</label>  -->
	                                        <label><s:property value="#jobIdFlag" />:</label>
	                                        <!--Blank space removed 3693  -->
											<s:textfield maxlength="24" cssStyle="width:154px;" cssClass="text x-input" name="jobId" value=""></s:textfield>
	                                    </li>
									</s:if>
									<li class="nomarginright">
										<label>&nbsp;</label>
										<s:set name='storefrontId' value="wCContext.storefrontId" />
										<s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storefrontId)}'>
											<input type="image" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/quick-add/addtoquicklist.png"
											onclick="$('#prodId').focus();qaAddItem1(this.form); return false;" />
										</s:if>
										<s:elseif test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>
										<input type="image"	src="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/images/buttons/addtoquicklist green.png"
											onclick="$('#prodId').focus();qaAddItem1(this.form); return false;" />
										</s:elseif>
									</li>

                                </ul>
                            </form>

							<s:form id="formAdd2List" cssClass="form quick-add-to-cart-form" cssStyle="padding-right:21px;"
								action="MyItemsDetailsQuickAdd" method="post">
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
                                            <s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
                                            	<th class="col-header col-header job-col"><s:property value="#custPONo" /></th>
                                            </s:if>
											<s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>
												<th class="last-col-header col-header po-col"><!-- Job Number  --><s:property value="#jobIdFlag" /></th>
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
                              <!-- <input id="btnQLAdd2Cart" type="image" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/quick-add/addtolist.png" 
								class="add-to-cart-btn" onclick="add2List();return false;" /> -->
								<a href="#" id="btnQLAdd2Cart" class="orange-ui-btn add-to-cart-btn" onclick="add2List();return false;"><span>Add to My Items List</span></a>
                            </s:form> <!-- CODE_END Quick Add - PN -->
							
                            <div class="clear">&nbsp;</div>
                            <div class="quick-add-form-bot">
                            <!-- Added For Jira 3197 -->
                            <center><div class="error" id="errorMsgFor_QL" style="display : none"/>Click the Add to Quick List button once you have entered an item number.</div> </center>
                             <!-- Code Fix For Jira 3197 -->
                            </div>
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
							<textarea class="x-input" id="listDesc" disabled="disabled" title="Description" rows="2" onkeyup="javascript:restrictTextareaMaxLength(this,250);" style="width:220px; height: 92px; word-wrap:break-word;"><s:property
							value="listDesc"   /></textarea>
						</s:if><s:else>
							<textarea class="x-input" id="listDesc" title="Description" rows="2" onkeyup="javascript:restrictTextareaMaxLength(this,250);" style="width:220px; height: 92px; word-wrap:break-word; "><s:property
							value="listDesc"  /></textarea>
						</s:else>
                    </div>
                    <div class="clear">&nbsp;</div>
                </div>
                
                <!-- Close mil-edit -->
				<div class="clear"></div>
                <br />
                <s:set name="shareAdminOnlyFlg" value="%{#_action.getShareAdminOnly()}" />
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
                    <s:else>								
						<s:if test='#shareAdminOnlyFlg=="" || #shareAdminOnlyFlg=="N"'>									
								<s:if test="#isEstUser">
											<li><a class="grey-ui-btn " href="#dlgShareList" id="dlgShareListLink2" ><span>Share List</span></a></li>
								</s:if>
						</s:if>
					</s:else>
                    <li><a href="#dlgImportForm" id="various5"  class="grey-ui-btn"><span>Import Items</span></a></li>
                </ul>

                <ul id="tool-bar float-right" class="tool-bar-bottom" style="float:right; padding-top:5px; width:152px;">
                    <li><a style="margin-left:5px;" class="green-ui-btn float-right-imp" href="javascript:saveAllItemsNew('mil-edit', ['quick-add']);"><span>Save</span></a></li>
                    <li><a class="grey-ui-btn float-right-imp" href="javascript:cancelChanges();"><span>Cancel</span></a></li>
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
    	
			 	    <div class="error" id="errorMsgForMandatoryFields_mil-edit" style="display:none;" ></div>  
			 		<div class="error" id="errorMsgTop" style="display:none;" ></div> 
			 		
				<%-- Added For Jira 3197 - InvalidImport condition --%>
				<s:if test="%{errorMsg == 'InvalidImport'}">
							
					<script type="text/javascript">
								document.getElementById("errorMsgTop").innerHTML = "The import file must be in .csv format." ;
					           	document.getElementById("errorMsgTop").style.display = "inline"; 
					            
					</script>
				</s:if>
				<s:if test="%{errorMsg == 'ItemsOverLoad'}">
							<div class="error">
							      <!--   Your list may contain a maximum of 200 items. Please delete some items and try again. -->
							         <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGT200' />
							</div>
						</s:if>
					<br/>
                 </li> 
               </ul>
               
               
               
               
               <s:set name="baseUOMs" value="#_action.getBaseUOMmap()" />
               <s:set name="itemIdCustomerUomMap" value="#_action.getItemAndCustomerUomHashMap()" />
               
             	<s:set name="webtrendsItemTypeMap" value="%{#_action.getItemTypeMap()}" />
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
					<s:set name='customerUOM' value='#itemIdCustomerUomMap.get(#itemId)' />
					<s:set name="itemUOMsMap" value='itemIdConVUOMMap.get(#itemId)' />
					<s:set name="itemBaseUom"  value='#baseUOMs.get(#itemId)' />
					
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
					<s:if test='%{#webtrendsItemTypeMap!=null}' >
						<s:set name="webtrendsItemType" value='%{#webtrendsItemTypeMap.get(#itemId)}' />
					</s:if>
					<s:else>
						<s:set name="webtrendsItemType" value='' />
					</s:else>
					
					<%-- <s:if test='%{#itemUOMsMap!=null}' >
						<s:iterator value='#itemUOMsMap' status='uomIndex'>
							<s:if test="%{#uomIndex.index == 0}" >
								<s:set name='itemBaseUom' value='key' />
							</s:if>
						</s:iterator>
					</s:if> --%>
						
					
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
					<s:set name='uomList'
						value='itemIdsUOMsDescMap.get(#itemId1)' />

					<s:url id='itemDetailsLink' namespace="/catalog"
						action='itemDetails.action' includeParams='none' escapeAmp="false">
						<s:param name="itemID" value="#itemId" />
						<s:param name="sfId" value="#parameters.sfId" />
						<s:param name="unitOfMeasure" value="#itemBaseUom" />
					</s:url>


    				<s:if test="#status.last" >
                   		<div class="mil-wrap-condensed-container mil-only last"  onmouseover="$(this).addClass('green-background');" onmouseleave="$(this).removeClass('green-background');" >    
           			</s:if>
           			<s:else>
           				<div class="mil-wrap-condensed-container mil-only"  onmouseover="$(this).addClass('green-background');" onmouseleave="$(this).removeClass('green-background');" >  
           			</s:else>
           
    
           
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
                            <div class="mil-wrap-condensed-desc" style="height:auto;">  
								<s:if test="%{#itemType != 99}">
									<a class="short-description" href='<s:property value="%{itemDetailsLink}" />'>
									<s:property value="#name" />
									</a>
								</s:if>					
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
									<p ><b><s:property value="wCContext.storefrontId" /> <s:property value="#xpedxItemLabel" />: <s:property value="#itemId" /></b>			
										<s:if test='#certFlag=="Y"'>
											<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/catalog/green-e-logo_small.png" style="margin-left:0px; display: inline;">
										</s:if>
									</p>
									
										<%-- JIRA xb-805 & xb-840 Code Changes Begin --%>								
									<s:if test='skuMap!=null && skuMap.size()>0'>
										<s:set name='itemSkuMap' value='%{skuMap.get(#itemId)}'/>
										<s:set name='mfgItemVal' value='%{#itemSkuMap.get(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MFG_ITEM_NUMBER)}'/>
										<s:set name='partItemVal' value='%{#itemSkuMap.get(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUST_PART_NUMBER)}'/>
									</s:if>
										 	<s:if test='mfgItemFlag != null && mfgItemFlag=="Y"'> 
											<p class="fields-padding">
												<s:property value="#manufacturerItemLabel" />: <s:property value='#mfgItemVal' /></p>
											 </s:if>
											<s:if test='customerItemFlag != null && customerItemFlag=="Y"'>
											<p class="fields-padding">
												<s:property value="#customerItemLabel" />: <s:property value='#partItemVal' /></p>
											</s:if>			
									<%-- JIRA xb-805 & xb-840 Code Changes End --%>	
					
								</s:if>
							<%-- JIRA 356 Code Changes Begin --%>	
							<div class="red fields-padding">
	                            <s:if test="%{#itemType != '99.00'}">
									<s:set name="isStocked" value="inventoryCheckForItemsMap.get(#itemId)"></s:set>
									<s:if test="#isStocked !=null">
										<s:if test='%{#isStocked !="Y"}'>
												<p>Mill / Mfg. Item - Additional charges may apply</p>
										</s:if>
									</s:if> 
									<s:else>
										<p>Mill / Mfg. Item - Additional charges may apply</p>
									</s:else>
								</s:if>
						
							<s:if test='editMode == true'>
							<%-- Show Replacement link only in Edit mode --%>
									<s:if test="(xpedxItemIDUOMToReplacementListMap.containsKey(#itemId) && xpedxItemIDUOMToReplacementListMap.get(#itemId) != null)">
										<p class="replacementtext"><a href="#linkToReplacement" class="modal red" onclick='javascript:showXPEDXReplacementItems("<s:property value="#itemId"/>", "<s:property value="#id"/>", "<s:property value="#qty"/>");'>This item has been replaced</a></p>
									</s:if>
								</s:if>
								<s:else>
								  <s:if test="(xpedxItemIDUOMToReplacementListMap.containsKey(#itemId) && xpedxItemIDUOMToReplacementListMap.get(#itemId) != null)">
									<p class="replacementtext">This item has been replaced&nbsp;<img
					alt="To replace or add item, click the Edit This List button."
					title="To replace or add item, click the Edit This List button."
					height="12" border="0" width="12"
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px; float: right;" />
									 </p>
									</s:if>
								</s:else>
							</div>
							<%-- JIRA 356 Code Changes End --%>	
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

                            <table class="mil-config" width="380" border="0" cellspacing="0" cellpadding="0">
                                <s:if test='editMode == true'>
									<tr>

										<td align="right"></td>
										<td align="right"><label style="text-align: right;">Sequence: </label>
										
										<%--Hemantha
										<s:textfield name="orders" value="%{#itemOrder2}" id="itemOrder_%{#itemOrder2}" maxlength="3" 
											onkeyup="javascript:isValidQuantity(this);resetQuantityError('%{#id}');" 
											cssStyle="width: 50px;" theme="simple"/>
										--%>
										<!-- Modified for JIRA 1402 Starts-->			    
								         <s:select cssClass="xpedx_select_sm" cssStyle="width: 50px;" name="orders"
											list="itemValue" value='%{itemOrder2}' onfocus="populate(this.id);" 
											onchange="populateValue(this,this.id);"
											id="itemOrder_%{#itemOrder2}" headerKey='%{itemOrder2}' headerValue='%{itemOrder2}' emptyOption="false" theme="simple"/>
										<!-- Modified for JIRA 1402 End-->
										</td>
									</tr>
								</s:if>
								<tr>
                                    <td align="right" width="112"><label style="text-align:right;">Qty:</label></td>
                                    <td width="142" colspan="2" align="left">                                    
										<!-- Qty --> <s:hidden
											name="itemQty" value="%{#qty}" /> <s:hidden
											id="enteredQuantities_%{#id}" name="enteredQuantities" value="%{#qty}" /> 
											<s:hidden id="custUOM_%{#id}" name="custUOM" value="%{#customerUOM}" />
											<!-- UOM & Custom Fields -->
											<s:if test="%{#itemType != '99.00'}">
												<s:textfield
												title="QTY" cssClass="x-input" cssStyle="width:51px;" name="qtys" id="qtys_%{#id}"  maxlength="7" tabindex="1"
												value="%{#qty}" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);isValidQuantity(this);updateHidden(this,'%{#id}');setFocus(this,event);" theme="simple"></s:textfield>
												<s:hidden name='QTY_%{#id}' id='QTY_%{#id}' value='%{#qty}'/>
												<s:hidden id="enteredUOMs_%{#id}" name="enteredUOMs" value="%{#itemUomId}" />
												<s:hidden id="itemBaseUOM_%{#id}" name="itemBaseUOM" value="%{#itemBaseUom}" />
													<s:if test="#uomList!=null" >
													<s:select cssClass="xpedx_select_sm" cssStyle="width:140px;" name="uoms" list="#uomList"
													listKey="key"
													listValue="value" 
													value='itemUomId' onchange="javascript:updateHidden(this,'%{#id}',0,'%{#_action.getJsonStringForMap(#itemUOMsMap)}');" theme="simple"/>
													</s:if>
												<s:hidden name='UOM_%{#id}' id='UOM_%{#id}' value="%{#itemUomId}"/>
											</s:if> <s:else>
												<s:textfield
												title="QTY" cssClass="x-input" cssStyle="width:51px;" name="qtys" id="qtys_%{#id}" tabindex="1"
												value="%{#qty}" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);updateHidden(this,'%{#id}');isValidQuantity(this);setFocus(this,event);" theme="simple" readonly="true"></s:textfield>
												<s:hidden name='QTY_%{#id}' id='QTY_%{#id}' value='%{#qty}'/>
												<s:textfield cssClass="x-input" cssStyle="width:140px;" name="uoms" value="%{#itemUomId}" onchange="javascript:updateHidden(this,'%{#id}');" theme="simple" readonly="true"/>
												<s:hidden name='UOM_%{#id}' id='UOM_%{#id}' value=' '/>
											</s:else> 
											<%-- <div id="errorDiv_qtys_<s:property value='%{#id}' />" style="color:red;"> 
											</div> --%>
                                        </td>
                                </tr>
								
								<s:set name="mulVal" value='itemOrderMultipleMap.get(#itemId1)' />
									<s:set name="erroMsg" value='%{erroMsg}'/>
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
											<td align="right" width="169"><label style="text-align:right;"><s:property value="%{#FieldValue}" />:</label></td>
											<td>
												<%-- Creating text field with name as the Customer field name --%>
												
												<%-- BB: Need to add an if statement here, to determine which cdf this is. one has a max of 22, the other 24. --%>
												<%--Start JIRA 3693  --%>
												<%--Start JIRA 449  --%>
												<s:if test="%{#FieldLabel == 'CustLineAccNo'}">
												<s:textfield cssStyle="width:198px;" cssClass="x-input" maxlength="24"
													name='customField%{#FieldLabel}s' id="customField%{#FieldLabel}s"
													size='10' value="%{@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getReplacedValue(#CustomFieldValue)}" 
													title="%{#FieldValue}" onchange="javascript:updateHidden(this,'%{#id}','%{#custFieldStatus.count}');"/>
												<s:hidden name='customerField_%{#custFieldStatus.count}_%{#id}' id='customerField_%{#custFieldStatus.count}_%{#id}' value='%{"Extn"+#FieldLabel+"@"+#CustomFieldValue}'/>
												<s:hidden id="entered%{#FieldLabel}_%{#id}" name="entered%{#FieldLabel}" value="%{#CustomFieldValue}" /></s:if>
												<s:elseif test="%{#FieldLabel == 'CustomerPONo'}">
												<s:textfield cssStyle="width:198px;" cssClass="x-input" maxlength="22"
													name='customField%{#FieldLabel}s' id="customField%{#FieldLabel}s"
													size='10' value="%{@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getReplacedValue(#CustomFieldValue)}" 
													title="%{#FieldValue}" onchange="javascript:updateHidden(this,'%{#id}','%{#custFieldStatus.count}');"/>
												<s:hidden name='customerField_%{#custFieldStatus.count}_%{#id}' id='customerField_%{#custFieldStatus.count}_%{#id}' value='%{"Extn"+#FieldLabel+"@"+#CustomFieldValue}'/>
												<s:hidden id="entered%{#FieldLabel}_%{#id}" name="entered%{#FieldLabel}" value="%{#CustomFieldValue}" /></s:elseif>
												<s:else>
												<s:textfield cssStyle="width:198px;" cssClass="x-input" maxlength="25"
													name='customField%{#FieldLabel}s' id="customField%{#FieldLabel}s"
													size='10' value="%{@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getReplacedValue(#CustomFieldValue)}" 
													title="%{#FieldValue}" onchange="javascript:updateHidden(this,'%{#id}','%{#custFieldStatus.count}');"/>
												<s:hidden name='customerField_%{#custFieldStatus.count}_%{#id}' id='customerField_%{#custFieldStatus.count}_%{#id}' value='%{"Extn"+#FieldLabel+"@"+#CustomFieldValue}'/>
												<s:hidden id="entered%{#FieldLabel}_%{#id}" name="entered%{#FieldLabel}" value="%{#CustomFieldValue}" /></s:else>
												<%--End JIRA 3693 --%>
											</td>
										</tr>
									</s:iterator>
                                
                            </table>
                            <!--  TODO FXD2-11 Display error message  -->
                            <s:set name="baseUOM" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#baseUOMs.get(#itemId))"></s:set>
	                        <s:hidden name="baseUOM" id="baseUOM_%{#id}" value="%{#baseUOM}"/>
	                         <s:set name="baseUOMCode" value="#baseUOMs.get(#itemId)"></s:set>
	                        <s:hidden name="baseUOMCode" id="baseUOMCode_%{#id}" value="%{#baseUOMCode}"/>
                            <div class="clear"></div>
							<s:if test='editMode != true'>
							<s:hidden name="isEditOrder" id="isEditOrder" value="%{#isEditOrderHeaderKey}"/>
							    <ul style="float: right; width: 281px;" class="tool-bar-bottom" id="tool-bar">
                                <li style="float: left; display: block; position: absolute; right: 127px; margin-right: 8px;"><a id="PAAClick_<s:property value="#id"/>" href="javascript:writeMetaTag('DCSext.w_x_sc_count,DCSext.w_x_sc_itemtype','1,' + '<s:property value="#webtrendsItemType"/>');checkAvailability('<s:property value="#itemId"/>','<s:property value="#id"/>')" 
                                 style="margin-left: 25px;"> 
								<span class="mil-mpna">My Price &amp; Availability&nbsp;&nbsp;</span></a></li>
                                <%-- <li style="margin-left: 72px;"><a class="orange-ui-btn" href="javascript:addItemToCart('<s:property value="#itemId"/>','<s:property value="#id"/>')"><span>Add to Cart</span></a></li> --%>
                                <s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
                                	<li style="margin-left: 170px;"><a class="orange-ui-btn" href="javascript:myAddItemToCart('<s:property value="#itemId"/>','<s:property value="#id"/>')"><span>Add to Cart</span></a></li>
                                </s:if>
                                <s:else>
                                	<li style="margin-left: 165px;"><a class="orange-ui-btn" href="javascript:myAddItemToCart('<s:property value="#itemId"/>','<s:property value="#id"/>')"><span>Add to Order</span></a></li>
                                </s:else> 
                                 <s:if test='%{#mulVal >"1" && #mulVal !=null}'> 
	                               <li style="float: right; display: block; margin-right: 2px; margin-top: 3px; width: 275px;"> 
	                               <div class="notice" id="errorDiv_qtys_<s:property value='%{#id}' />" style="display : inline; float: right;">
	                               		<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#xpedxUtilBean.formatQuantityForCommas(#mulVal)}"></s:property>&nbsp; 
	                               		<s:property value="#baseUOMDesc"></s:property>
	                               	</div>
	                               	<%--Added below hiddens for Jira 3197- MIL Messaging --%>
	                               	<s:hidden name="hiddenUOMOrdMul_%{#id}"  id="hiddenUOMOrdMul_%{#id}" value="%{@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#baseUOMs.get(#itemId))}"></s:hidden>
	                              	<s:hidden name="hiddenQuantityOrdMul_%{#id}" id="hiddenQuantityOrdMul_%{#id}" value='%{#xpedxUtilBean.formatQuantityForCommas(#mulVal)}'></s:hidden>
	                              	<s:hidden name="hiddenId" id="hiddenId" value="%{#id}" />
	                              	
	                               </li>
                                </s:if>
                                <s:else>
                                <li style="float: right; display: block; margin-right: 2px; margin-top: 3px; width: 275px;"> 
	                               <div class="notice" id="errorDiv_qtys_<s:property value='%{#id}' />" style="display : inline; float: right;">
	                               </div>
								</li>
                                </s:else>
                                <s:if test='%{#erroMsg !=null && #erroMsg !=""}'>  
                                <li style="float: right; display: block; margin-right: 10px; width: 550px; margin-top: 5px;">
		                            <div class="error" style="display:none;" id="errorDiv_qtys_<s:property value='%{#id}' />" style="color:red"></div>
		                    	</li>
		                    	</s:if>
		                    	<s:else><li style="float: right; display: block; margin-right: 10px; width: 200px; margin-top: 5px;">
		                            <div class="error" style="display:none;" id="errorDiv_qtys_<s:property value='%{#id}' />" style="color:red"></div>
		                    	</li>
		                    	</s:else>
		                    	<br/> 
		
		                     </ul>
                            <div class="clearall"> &nbsp;</div>

                            
							</s:if>
							<s:else>
							<s:if test='%{#mulVal >"1" && #mulVal !=null}'> 
	                               <li style="float: right; display: block; margin-right: 2px; margin-top: 3px; width: 275px;"> 
	                               <div class="notice" id="errorDiv_qtys_<s:property value='%{#id}' />" style="display : inline; float: right;">
	                               		<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#xpedxUtilBean.formatQuantityForCommas(#mulVal)}"></s:property>&nbsp; 
	                               		<s:property value="#baseUOMDesc"></s:property>
	                               	</div>                               	
	                              	
	                               </li>
                                </s:if>
							</s:else>
							 
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
						 <div id="availabilityRow_<s:property value='#id'/>"  <s:if test='%{pnaHoverMap.containsKey(#jsonKey)}'></s:if><s:else>style="display:block; background-color:#fafafa;"</s:else> >   
                            <!-- end prefs  -->
                             <s:if test="%{pnaHoverMap.containsKey(#jsonKey)}">
	                        	<s:include value="../MyItems/XPEDXMyItemsDetailsItemAvailability.jsp"></s:include>
                        	</s:if>
                        </div>
                      
                    </div>


				
                </div>
                </s:iterator>
						<div id="priceDiv" style="display: none;">
                    </div>                
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
					<div class="clear"></div><br/>
                    <s:if test='editMode != true'>
						
						<fieldset class="mil-non-edit-field">
							<legend>For Selected Items:</legend>
							<input class="forselected-input" type="checkbox" id="selAll2"/>
							<%-- <a class="grey-ui-btn float-left" href="javascript:updateSelectedPAA()"><span>Update My Price &amp; Availability</span></a> --%>
							<a class="grey-ui-btn float-left" href="javascript:javascript:writeMetaTag('DCSext.w_x_sc_itemtype','<s:property value="#webtrendsItemType"/>');myUpdateSelectedPAA()"><span>Update My Price &amp; Availability</span></a>
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
								<s:else>																
									<s:if test='#shareAdminOnlyFlg=="" || #shareAdminOnlyFlg=="N"'>																			
										<s:if test="#isEstUser">
											<li><a class="grey-ui-btn " href="#dlgShareList" id="dlgShareListLink2" ><span>Share List</span></a></li>
										</s:if>
									</s:if>
								</s:else>
								<li><a href="#dlgImportForm" id="various5"  class="grey-ui-btn"><span>Import Items</span></a></li>
							</ul>
	
							<ul id="tool-bar float-right" class="tool-bar-bottom" style="float:right; padding-top:5px; width:152px; ">
								<li><a  style="margin-left:5px;" class="green-ui-btn float-right-imp" href="javascript:saveAllItemsNew('mil-edit', ['quick-add']);"><span>Save</span></a></li>
								<li><a class="grey-ui-btn float-right-imp" href="javascript:cancelChanges();"><span>Cancel</span></a></li>																
							</ul>
						</s:if>

					</s:else>
			
                </div>
                            <div class="clearall"></div>
            <ul>
			<li style="text-align: center;">
                            
 
 
 			<div class="error" id="errorMsgBottom" style="display:none;" ></div> 
 
            <%-- Added For Jira 3197 - InvalidImport condition --%>
				<s:if test="%{errorMsg == 'InvalidImport'}">
							
					<script type="text/javascript">
								document.getElementById("errorMsgBottom").innerHTML = "The import file must be in .csv format." ;
					            document.getElementById("errorMsgBottom").style.display = "inline"; 
					
					</script>
				</s:if>
            </li>
			</ul>
            <div class="clearall"></div>
               
            <s:if test="%{errorMsg!=null && errorMsg!= '' && errorMsg.indexOf('ROW_PROCESSING_ERROR')>-1}">
						<s:set name='errIndex' value='%{errorMsg.indexOf("@")}' />
						<s:set name='rowNums' value='%{errorMsg.substring(#errIndex +1, errorMsg.length())}' />
						<%-- set rowNums for Jira 3197 - MIL Messaging, Added space in message--%>
						<s:set name='rowNums' value='%{#rowNums.replace(",",", ")}' />
						<br />
					
						<script type="text/javascript">
						//Modified For Jira 3197 - milFileImportMsg
						var milFileImportMsg = "Row(s) "+ '<s:property value="#rowNums" />' + " failed to import.";
						//var milFileImportMsg = "<s:text name='MSG.SWC.ITEM.LISTIMPORT.ERROR.NUMROWSFAILED' /> " + '<s:property value="#rowNums" />' ;
						importItems(milFileImportMsg);
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
	                    <!-- Begin - Changes made by Mitesh for JIRA 3186 -->	
	                    <s:if test='xpedxYouMightConsiderItems.size() < 4'>
							<div disabled="disabled" class="jcarousel-prev jcarousel-prev-hide-horizontal"></div> 
				    		<div disabled="disabled" class="jcarousel-next jcarousel-next-hide-horizontal"></div>
		   			 		
			    		</s:if>
			    		<!-- End - Added by Mitesh Parikh for JIRA#3186  -->						    		
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
											<s:set name='imageURL' value='%{"/xpedx/images/INF_150x150.jpg"}' />
										</s:if>					
	 										
										<s:set name='primaryInfo' value='XMLUtils.getChildElement(#reltItem, "PrimaryInformation")'/>
										<s:set name='shortDesc' value='#primaryInfo.getAttribute("ShortDescription")'/>
										<li> 
										    <s:a href="javascript:processDetail('%{#reltItem.getAttribute('ItemID')}', '%{#reltItem.getAttribute('UnitOfMeasure')}')"> 
										    	<img src="<s:url value='%{#imageURL}' includeParams='none' />" title='<s:property value="%{#reltItem.getAttribute('ItemID')}"/>' width="91" height="94" alt="<s:text name='%{#imageMainLabel}'/>" /> <!-- <b><s:property value="%{#reltItem.getAttribute('ItemID')}"/></b> --><br />
												<!-- Added span for Jira 3931 -->
												<span class="short-description">
													<s:property value="%{#shortDesc}"/>
												</span>
												<br />
												<br />
												<br />
											</s:a> 
										</li>
	
									</s:if> 
									<s:else>
										<s:set name='imageIdBlank' value='%{"/xpedx/images/INF_150x150.jpg"}' />									
										<s:set name='primaryInfo' value='XMLUtils.getChildElement(#reltItem, "PrimaryInformation")'/>
										<s:set name='shortDesc' value='#primaryInfo.getAttribute("ShortDescription")'/>
										<li> 
										    <s:a href="javascript:processDetail('%{#reltItem.getAttribute('ItemID')}', '%{#reltItem.getAttribute('UnitOfMeasure')}')"> 
										    	<img src="<s:url value='%{#imageIdBlank}'/>" title='<s:property value="%{#reltItem.getAttribute('ItemID')}"/>' width="91" height="94" alt="" /> <!-- <b><s:property value="%{#reltItem.getAttribute('ItemID')}"/></b> --><br />
												<!-- Added span for Jira 3931 -->
												<span class="short-description">
													<s:property value="%{#shortDesc}"/>
												</span>
												<br />
												<br />
												<br />
											</s:a> 
										</li>
									</s:else>								
							</s:iterator>						
						</s:if>
						<!-- End - Changes made by Mitesh for JIRA 3186 -->
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
		 <div class="last-modified-div">
		 		Last modified by <s:property value= "%{#_action.getLastModifiedUserId()}"/> on <s:property value="#lastModifiedDateString"/> 
		 </div> 
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
   		<script type="text/javascript">
			Ext.onReady(function(){		
	       
				
			//
				
				
				
				});
		</script> 
		<s:if test="#altItemList.size() > 0">
			 <input type="hidden" id="rListSize_<s:property value='key'/>" value=<s:property value='#altItemList.size()'/> />  
		</s:if> 
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
						
						
						<s:set name='ritemUomId' value='#altItem.getAttribute("UnitOfMeasure")' />
						<s:set name='ritemType' value='#altItem.getAttribute("ItemType")' />
						
						<s:url id='ritemDetailsLink' namespace="/catalog"
							action='itemDetails.action' includeParams='none' escapeAmp="false">
							<s:param name="itemID" value="#rItemID" />
							<s:param name="sfId" value="#parameters.sfId" />
							<s:param name="unitOfMeasure" value="#ritemUomId" />
						</s:url>
						<!-- Checked in for JIRA XBT-335  -->
						<s:if test="#altItemList.size() == 1">
                                                <input name="relatedItems" type="radio" checked="true"/>
                                                 <input type="hidden" id="hUId_<s:property value='key'/>" value='<s:property value="#uId" />' />                 
                       </s:if>	
						<s:else>
						<input name="relatedItems" 
								onclick="javascript:setUId('<s:property value="#uId" />');" type="radio" />
						</s:else>
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
								<span class="short-description"><s:property value="#name" /></span>
								</a>	
							</s:if>
							</div>
                    <div class="mil-attr-wrap">
                        <ul class="mil-desc-attribute-list prodlist">                        
						<a href='<s:property value="%{ritemDetailsLink}" />'>	 <s:property value='#rdesc' escape='false'/> </a>
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
</s:form> <s:form id="formRIReplaceInList" action="MyItemsDetailsChange"
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
	
	
	


<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>
-->
<!-- Web Trends tag start -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Web Trends tag end  -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/modals/checkboxtree/jquery.checkboxtree.js"></script>-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>
--><!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions.js"></script>

--><!-- Lightbox/Modal Window --> 
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>-->
<!-- page specific JS includes -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/MyItems/XPEDXMyItemsDetails<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/MyItems/XPEDXAddToCartAndAvailability<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/ajaxValidation.js"></script>
-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numberformatter-1.1.0<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.blockUI<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
</body>
</html>