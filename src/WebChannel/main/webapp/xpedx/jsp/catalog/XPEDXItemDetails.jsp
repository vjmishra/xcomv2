<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%
  		request.setAttribute("isMergedCSSJS","true");
  	  %>
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
    This is to avoid a defect in Struts that's creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts OGNL statements. --%>
<s:set name='_action' value='[0]' />
<s:bean
	name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils'
	id='xutil' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<s:set name='scuicontext' value="uiContext" />
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:bean
	name='com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils'
	id='utilMIL' />
<s:bean
	name='com.sterlingcommerce.webchannel.catalog.utils.CatalogUtilBean'
	id='catalogUtil' />
<s:set name="itemListElem" value="itemListElem" />
<s:set name="isStocked" value="isStocked" />
<s:set name="orderMultiple" value="orderMultiple" />
<s:set name='showCurrencySymbol' value='true' />

<s:set name="isUserAdmin"
	value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
<s:set name='currentCartInContextOHK' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("OrderHeaderInContext")'/>
	<%--
<s:set name='currentCartInContextOHK' value='@com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper@getCartInContextOrderHeaderKey(wCContext)'/>
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<swc:html isXhtml="true" > 

<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<!-- begin styles. These should be the only three styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/xpedx-header.css" />
<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS.css" />
<link rel="stylesheet" type="text/css"
               href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE.css" />
<![endif]-->
<!--  End Styles -->

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->
<!-- Facy Box (Lightbox/Modal Window -->
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>-->
<!-- Page Calls -->

<!-- END head-calls.php -->
<title><s:property value="wCContext.storefrontId" /> - <s:property value="wCContext.storefrontId" /> Product Details</title>

<s:set name='myParam' value='{"itemID"}' />
<s:url action='navigate.action' namespace='/catalog' id='myUrl' />
<s:url id='addToCartURL' namespace='/order' action='addToCart'
	includeParams="none" />
<s:url id='imgViewerURL' namespace='/catalog' action='itemImageViewer' />
<s:set name='appFlowContext' value='#session.FlowContext' />
<s:set name='isFlowInContext'
	value='#util.isFlowInContext(#appFlowContext)' />
<s:set name='orderHeaderKey' value='%{#appFlowContext.key}' />
<s:set name='returnURL' value='%{#appFlowContext.returnURL}' />
<s:set name='flowID' value='%{#appFlowContext.type}' />
<s:set name='orderCurrency' value='%{#appFlowContext.currency}' />
<s:url id='punchOutURLOrderChange' namespace='/order'
	action='configPunchOut'>
	<s:param name='orderHeaderKey' value='%{#appFlowContext.key}' />
	<s:param name='currency' value='%{#appFlowContext.currency}' />
	<s:param name='flowID' value='%{#appFlowContext.type}' />
</s:url>
<s:url id="xpedxItemDetailsPandA" namespace="/catalog" action="xpedxItemDetailsPandA">
</s:url>
<s:set name='isProcurementUser' value='wCContext.isProcurementUser()' />
<s:set name='isProcurementInspectMode'
	value='#util.isProcurementInspectMode(wCContext)' />
<s:set name='isReadOnly' value='#isProcurementInspectMode' />
<s:hidden name="catagory" id="catagory" value="%{#_action.getCatagory()}" />
<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>
</head>
<!-- END swc:head -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header.js"></script>
<script>
var priceCheck;
function pandaByAjax(itemId,reqUom,Qty,baseUom,prodMweight,pricingUOMConvFactor) {
	if(itemId == null || itemId == "null" || itemId == "") {
		return;
	}
	if(reqUom == null || reqUom == "null" || reqUom == "") {
		reqUom = document.getElementById("selectedUOM").value;
	}
	if(Qty == null || Qty == "null" || Qty == "") {
		Qty = "1";
	}
	priceCheck = true;
	var Category = document.getElementById("catagory").value;
	var url = '<s:property value="#xpedxItemDetailsPandA"/>';
	var validationSuccess = validateOrderMultiple();
	Ext.Ajax.request({
       	url:url,
        params: {
			itemID 	: itemId,
	       	requestedUOM : reqUom,
	       	pnaRequestedQty	: Qty,
	       	baseUOM	: baseUom,
	       	prodMweight : prodMweight,
	       	pricingUOMConvFactor : pricingUOMConvFactor,
	       	validateOrderMul : validationSuccess,
	       	Category : Category
		},      	
	   	success: function (response, request){
			document.getElementById("priceAndAvailabilityAjax").innerHTML = response.responseText;
			setPandAData();
			Ext.Msg.hide();
		}
	});
}
</script>
<script type="text/javascript">
	$(function() {
		// Activate the tabs
		$("#tabs").tabs({
			collapsible: false
		});
		
		// Truncate the sell text if needed
		var sellText = $('#sell-full').text().trim();
		$('#sell-truncated').text(sellText).shorten({noblock: true, tooltip: false, tail: '', width: ($('#sell-truncated').parent().width() * 0.85)});
		var sellTextTruncLength = $('#sell-truncated').text().length;
		if (sellTextTruncLength < sellText.length)
		{
			$('#sell-full').text(sellText.substr($('#sell-truncated').text().length));
			$('#sell-expand').click(function(){
				$('#sell-full').toggle();
				
				if ($('#sell-full').is(':visible'))
				{
					$(this).text(' [Collapse]');
				}
				else
				{
					$(this).html('&hellip; [Expand]');
				}
				
				return false;
			});
			$('#sell-expand').show();
		}
		
		// Select availability tab on MP&A click
		$('#my-price-link').click(function(){ $('a[href=#tabs-1]').click(); return false;});
		
		// Truncate promo text
		$('.promo-txt p').each(function(){
				$(this).shorten({width: ($(this).width() * 1.8)});
		});
	});
</script>
<!-- copied javascript from old page -->

<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
<s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />

<script type="text/javascript">
var isUserAdmin = <s:property value="#isUserAdmin"/>;
</script>

<script type="text/javascript">
	var currentAadd2ItemList = null;
	var currentAadd2ItemListIndex = null;
	
	
	$(document).ready(function() {
		$(document).pngFix();
		//Add to Wish List 
		$("#dlgAddToListLink").fancybox({
			'onStart' 	: function(){
				showPrivateList();
			},
			'onClosed':function() {
				var radio = document.addItemToList.itemListRadio;
				if(radio!=null)
				{
			  		for(var i=0;i<radio.length;i++)
			  		{
				  		radio[i].checked= false;
			  		}
				}
				currentAadd2ItemList = null;
				currentAadd2ItemListIndex = null;
			},
			'autoDimensions'	: false,
			'width' 			: 380,
			'height' 			: 262  
		});

		$("#dlgShareListLink").fancybox({
			'onStart' 	: function(){
				if (isUserAdmin){
					//Calling AJAX function to fetch 'Ship-To' locations only when user is an Admin
					showShareList('<s:property value="#CurrentCustomerId"/>');
				}
				Ext.get("smilTitle").dom.innerHTML = "New My Items List";  
			},
			'onClosed':function() {
				document.XPEDXMyItemsDetailsChangeShareList.listName.value = "";
				document.XPEDXMyItemsDetailsChangeShareList.listDesc.value = "";
				//document.getElementById("itemListSelect").value="-1";
				var radio = document.addItemToList.itemListRadio;
				if(radio!=null)
				{
			  		for(var i=0;i<radio.length;i++)
			  		{
				  		radio[i].checked= false;
			  		}
				}
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
					//Check Shared radio button
					radioBtns[1].checked = true;
					//Display Ship To Locations
					div.style.display = "block";
				}
				shareSelectAll(false);
				currentAadd2ItemList = null;
				currentAadd2ItemListIndex = null;
			},
			'autoDimensions'	: false,
			'width' 			: 600,
			'height' 			: 500  
		});	
	});
	
	$(function() {
		$("#tabs").tabs({
			collapsible: false
		});
	});


	function showShareList(customerId, showRoot, clFromListId){
		//Populate fields
		var divMainId 	= "divMainShareList";
		var divMain 	= document.getElementById(divMainId);
		
		//Check for show root
		if (showRoot == undefined ){
			//showRoot = true;
		}
		
		getShareList(customerId, divMainId, showRoot);
			
		if (clFromListId != undefined || clFromListId != null){
			var x = Ext.get("clFromListId");
			x.dom.value = clFromListId;
			
			try{ console.log("From list id: " + clFromListId); }catch(ee){} ;
		}
	}

	function getShareList(customerId, divId, showRoot){
	    //Init vars
	    
	    <s:url id='getShareList' includeParams='none' action='XPEDXMyItemsDetailsGetShareList'/>
	    
	    if (showRoot == null){ showRoot = false; }

	    var isCustomerSelected = false;
	    //Replace all '-' with '_'
	    var controlId = customerId.replace(/-/g, '_');
	    //Get the current checkbox selection of the customer
		   if(document.getElementById('customerPaths_'+controlId)!=null){
	        isCustomerSelected = document.getElementById('customerIds_'+controlId).checked;
		   }
	    
		var url = "<s:property value='#getShareList'/>";
	    url = ReplaceAll(url,"&amp;",'&');
	    //Show the waiting box
	    var x = document.getElementById(divId);
	    x.innerHTML = "Loading data... please wait!";
	    
	    
	    //Execute the call
	    document.body.style.cursor = 'wait';
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
	                setAndExecute(divId, response.responseText, isCustomerSelected);
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
	}




//Added for Jira 3920 - Merging create a new list and adding item into one call for Item detail page
	function addItemToListnew(form){
	
		var idx = currentAadd2ItemListIndex;

			var quantityValue = Ext.util.Format.trim(Ext.get('qtyBox').dom.value);	    			
			form.dom.orderLineItemNames.value 	= document.OrderDetailsForm.orderLineItemNames.value;
			form.dom.orderLineItemDesc.value 	= document.OrderDetailsForm.orderLineItemDesc.value;
			
			if (document.getElementById("itemUOMsSelect") == null){
				form.dom.itemUOMs.value = "EACH";
			} else {
				form.dom.itemUOMs.value 	= document.getElementById("itemUOMsSelect").value;
					
			}
			
			if (document.getElementById("qtyBox")){
				form.dom.orderLineQuantities.value = quantityValue;
			}

			//Job#
			if (document.getElementById("Job")!=null){
				form.dom.orderLineCustLineAccNo.value = document.getElementById("Job").value;
			}

			if(document.getElementById("customerPONo")!=null) 
			{
				form.dom.customerLinePONo.value=document.getElementById("customerPONo").value;
			}
			
	       
	        var itemCountValOfSelList = "0";//= document.getElementById("itemCount_"+currentAadd2ItemList.value);
	        
	        form.dom.orderLineItemOrders.value = Number(itemCountValOfSelList.value) + 1;
		       
		        var itemId = document.OrderDetailsForm.orderLineItemIDs.value;  
		        form.dom.orderLineItemIDs.value = itemId;
		        form.dom.fromItemDetail.value=true;
	   
	}
	   	
	function submitNewlistAddItem(xForm){
		//Validate the form
		try{
			if (xForm == undefined || xForm == null || xForm == ""){
				xForm = "XPEDXMyItemsDetailsChangeShareList";
			}
		}catch(er){
			xForm = "XPEDXMyItemsDetailsChangeShareList";
		}
		var form = Ext.get(xForm);
		
		//Validate form
		try{
			var val = form.dom.listName.value;
			if (val.trim() == ""){
				alert("Name is required. Please enter a name for this list.");
				return;
			}
		}catch(err){
		}
		
		var dataParams = new Object();
		
		for (i=0; i < form.dom.elements.length; i++){
			var item = form.dom.elements[i];
			if (item.name != ""){
				dataParams[item.name] = item.value;
			}
		}
		try{ console.debug("params" , dataParams); }catch(e){}

		//adding the already seleted as hidden checkboxes to the form 
		createHiddenInputsForSelectedCustomers(xForm);
		clearTheArrays();// clearing the arrays
		
		//form.dom.submit();
	    //Submit the data via ajax
	    
	    //Init vars
	    <s:url id='XPEDXMyItemsDetailsChangeShareListURL' includeParams='none' action='XPEDXMyItemsDetailsChangeShareListForItemDetail' namespace="/xpedx/myItems" escapeAmp="false" />

		var url = "<s:property value='#XPEDXMyItemsDetailsChangeShareListURL'/>";
	    url = ReplaceAll(url,"&amp;",'&');
	    
	    //Execute the call
	    document.body.style.cursor = 'wait';
	   	    
	    addItemToListnew(form);
	    $.fancybox.close();
	  //Added For Jira 2903
	  //Commented for 3475
      //Ext.Msg.wait("Processing..."); 

	    Ext.Ajax.request({
	      url: url,
	      form: xForm,
	      method: 'POST',
	      success: function (response, request){
	    	 document.body.style.cursor = 'wait';	    	
	          var myMessageDiv = document.getElementById("errorMsgForQty");	            
	            myMessageDiv.innerHTML = "Item has been added to the selected list." ;	            
	            myMessageDiv.style.display = "inline-block"; 
	            myMessageDiv.setAttribute("class", "success");	           
		  		/*Web Trends tag start*/ 
		  		writeMetaTag("DCSext.w_x_list_additem","1");
		  		/*Web Trends tag end*/
		  		 document.body.style.cursor = 'default';
	         
	      },
	      failure: function (response, request){
	          document.body.style.cursor = 'default';
	          Ext.Msg.hide();
	          
	          //alert("Error creating new list. Please try again later.");  //JIRA3920                                    
	      }
	  });
	  document.body.style.cursor = 'default';
	}

	function createRadioElement( name, id, checked, value, label ) 
	{     	
			var radioInput;     
			try {   
					radioInput = document.createElement('input');         
					radioInput.setAttribute('type', 'radio');         
					radioInput.setAttribute('name', name); 
					radioInput.setAttribute('id', id);
					radioInput.setAttribute('value', value);        
					if ( checked ) 
					{             
						radioInput.setAttribute('checked', 'checked');         
					}     
				         
				} 
				catch( err ) 
				{         
					radioInput = document.createElement('input');         
					radioInput.setAttribute('type', 'radio');         
					radioInput.setAttribute('name', name);         
					if ( checked ) 
					{             
						radioInput.setAttribute('checked', 'checked');         
					}     
				}      
			return radioInput; 
		} 
		
	//This function fetches the private my item lists in the form of radio buttons
	function showPrivateList(){
		//Populate fields
		var divMainId 	= "divMainPrivateList";
		var divMain 	= document.getElementById(divMainId);
		getMyItemsList(divMainId);
	}

	function getMyItemsList(divId){

		//Init vars
	   
	    <s:url id='getMyItemsList' includeParams="none" namespace="/xpedx/myItems" action='XPEDXMyItemsList'/> 
	    
	    var url = '<s:property value="#getMyItemsList"/>'+'&ShareList=ShareList';
	    url = ReplaceAll(url,"&amp;",'&');
	    //Show the waiting box
	    var x = document.getElementById(divId);
	    x.innerHTML = "Loading data... please wait!";
	    
	    //Execute the call
	    document.body.style.cursor = 'wait';
	    if(true){
	          Ext.Ajax.request({
	            url: url,
	            params: {
	        	  		  filterByMyListChk: true,
	        	  		  //Parameter to fetch the private lists in the form of radio buttons
	        	  		  displayAsRadioButton: true,
	            },
	            method: 'POST',
	            success: function (response, request){
	                document.body.style.cursor = 'default';
	                setAndExecute(divId, response.responseText);
	            },
	            failure: function (response, request){
	                var x = document.getElementById(divId);
	                x.innerHTML = "";
	                alert('Failed to fecth the my items list');
	                document.body.style.cursor = 'default';                                                  
	            }
	        });     
	    }
		document.body.style.cursor = 'default';
	}

	$(document).ready(function() {
		
		$("#areqsample").fancybox(
			{
		  	'autoDimensions'	: false,
			'width'         	: 800,
			'height'        	: 585
		});
	});
	
	
	function showRequestSampleDiv() {
		// Display the fancy box
		$.fancybox(
			Ext.get("RequestSampleDiv").dom.innerHTML,
			{
	        	'autoDimensions'	: false,
				'width'         	: 800,
				'height'        	: 585
			}
		);
	}
	
	function hideSharedListForm(){
		document.getElementById("dynamiccontent").style.display = "none";
	}
	
	function showSharedListForm(){
		var dlgForm 		= document.getElementById("dynamiccontent");
		if (dlgForm){
			dlgForm.style.display = "block";
		}
	}



</script>



<script type="text/javascript">
function addItemToCart(data)
{	
	
	var Qty=document.getElementById("qtyBox").value;
	//Quantity validation
	if(Qty =='' || Qty=='0')
	{
		document.getElementById("qtyBox").style.borderColor="#FF0000";
		document.getElementById("qtyBox").focus();
		document.getElementById("errorMsgForQty").innerHTML  = "Please enter a valid quantity and try again.";
  		document.getElementById("errorMsgForQty").style.display = "inline-block"; 
  		 document.getElementById("errorMsgForQty").setAttribute("class", "error");
		document.getElementById("Qty_Check_Flag").value = true;
		document.getElementById("qtyBox").value = "";
	    return;
	}
	var validationSuccess = validateOrderMultiple();
	
	document.getElementById("Qty_Check_Flag").value = false;

	if(validationSuccess){
		var ItemId=document.getElementById("itemId").value;
		var UOM=document.getElementById("itemUOMsSelect").value;
		if(document.getElementById("Job")!=null)
			var Job=document.getElementById("Job").value;
		if(document.getElementById("Customer")!=null)
			var customer=document.getElementById("Customer").value;
		//var UOM = document.getElementById("itemUOMsSelect");
		
		var customerPO = "";
		if(document.getElementById("customerPONo")!=null
				&&  document.getElementById("customerPONo")!=undefined){
			customerPO=document.getElementById("customerPONo").value; 
		}
		listAddToCartItem('<s:property value="#addToCartURL" escape='false'/>',ItemId,UOM,Qty,Job,customer,customerPO,'');
		
	}
	
}
function resetQuantityErrorMessage()
{
		var divId='errorMsgForQty';
		var divVal=document.getElementById(divId);
		divVal.innerHTML='';

}
function validateOrderMultiple() {
	resetQuantityErrorMessage();
	var Qty = document.getElementById("qtyBox");
	if(Qty.value == "" || Qty.value == null || Qty.value == '0'){
		Qty.value = 1;
	}
	var UOM = document.getElementById("itemUOMsSelect");
	var OrdMultiple = document.getElementById("OrderMultiple");
	var uomCF = 1;
	if(OrdMultiple != null && OrdMultiple!=undefined && OrdMultiple.value != 1){
		var selectedUOM = UOM.options[UOM.selectedIndex].text;
		var selectedUOMQty = selectedUOM.split(" ");
			if(selectedUOMQty.length == 2){
				var splitUOMQty = selectedUOMQty[1];
				var splitUOMQty1 = splitUOMQty.split("(");
				var splitUOMQty2 = splitUOMQty1[1];
				splitUOMQty2 = splitUOMQty2.split(")");
				var uomConvFactor = splitUOMQty2[0];
					if(uomConvFactor!=null && uomConvFactor!=undefined){
						uomCF = uomConvFactor;
				}
		}
		else{
			var selectedUOM = document.getElementById("selectedUOM");
			var uomConvFactor = document.getElementById("uomConvFactor");
				if(uomConvFactor!=null && uomConvFactor!=undefined){
					uomCF = uomConvFactor.value;
			}
		}
	}
	else{
			var selectedUOM = document.getElementById("selectedUOM");
			var uomConvFactor = document.getElementById("uomConvFactor");
				if(uomConvFactor!=null && uomConvFactor!=undefined){
					uomCF = uomConvFactor.value;
			}
		}
	
	var totalQty =  uomCF * Qty.value;
	//alert("Qty.value:"+Qty.value+" UOM:"+UOM.value+ " selectedUOM:"+selectedUOM.value+" uomConvFactor:"+uomConvFactor.value+" OrdMultiple:"+OrdMultiple.value +" uomCF:"+uomCF+ " totalQty:"+totalQty);
	if(OrdMultiple!=null && OrdMultiple!=undefined && OrdMultiple.value!=0){
		var ordMul = totalQty % OrdMultiple.value;
		var myMessageDiv = document.getElementById("errorMsgForQty");
		if (ordMul != 0) {
			//alert("-LP22-Order Quantity must be a multiple of " + OrdMultiple.value);
			if (priceCheck == true){
				      myMessageDiv.innerHTML = "<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(OrdMultiple.value) + " <s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#_action.getBaseUOM())'></s:property>";	            
	           		      myMessageDiv.style.display = "inline-block"; 
	            		      myMessageDiv.setAttribute("class", "error");
			}
			else{	   
            myMessageDiv.innerHTML = " <s:text name='MSG.SWC.CART.ADDTOCART.ERROR.NEWORDRMULTIPLES' /> " + addComma(OrdMultiple.value) + " <s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#_action.getBaseUOM())'></s:property>";	            
            myMessageDiv.style.display = "inline-block"; 
            myMessageDiv.setAttribute("class", "error");
			}
			return false;
		}
		else if (OrdMultiple.value > 1){
			if (priceCheck == true){
			      myMessageDiv.innerHTML = "<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(OrdMultiple.value) + " <s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#_action.getBaseUOM())'></s:property>";	            
         		      myMessageDiv.style.display ="inline-block";
          		      myMessageDiv.setAttribute("class", "notice");
			}
			else{
				myMessageDiv.innerHTML = "";
			}
		}
	}
	return true;
}

function listAddToCartItem(url, productID, UOM, quantity,Job,customer,customerPO)
{
    //Ext.Msg.wait("Adding item to cart...Please wait!");
    xpedx_working_start();
    setTimeout(xpedx_working_stop, 3000);
    var baseUOM;
    if(document.getElementById("baseUnitOfMeasure")!=null
			&&  document.getElementById("baseUnitOfMeasure")!=undefined){
    	baseUOM=document.getElementById("baseUnitOfMeasure").value; 
	} 
	
   	Ext.Ajax.request({
    	// for testing only
        url: url,
        params: {
			productID: productID,
	    	productUOM: UOM,
	    	baseUnitOfMeasure:baseUOM,
	    	quantity: quantity,
	    	reqJobId: Job,
	    	customerPONo:customerPO,
	    	reqCustomer:customer
	     },
        // end testing
        method: 'GET',
        success: function (response, request){
              
           // DialogPanel.toggleDialogVisibility('addToCart');	  
         //-- WebTrends tag start --
         //var selCart = document.getElementById("draftOrders");
         //selCart = selCart.options[selCart.selectedIndex].value;
         var selCart = '<s:property value="#currentCartInContextOHK" />';
         var tag="";
         var content = "";
         var itemType = document.getElementById("pritemType");
        
         
             if(itemType!=null && itemType!=undefined && itemType.value!='') {
            	 
					if(itemType = 'R'){
							tag = "DCSext.w_x_item_repl_ac";
						}
					else if(itemType = 'A'){
							tag="DCSext.w_x_item_alt_ac";
						}
					else if(itemType = 'Cr'){
						tag="DCSext.w_x_item_crosssell_ac";
					}
					else if(itemType = 'U'){
						tag="DCSext.w_x_item_upsell_ac";
					}
					content="1";
					writeMetaTag(tag,content);	
              } else {   
			 	tag = "WT.si_n,WT.tx_cartid,WT.si_x,DCSext.w_x_ord_ac";
			 	content = "ShoppingCart," + selCart + ",2,1";			
				writeMetaTag(tag,content,4);
              }
			//-- WebTrends tag end -
		    Ext.MessageBox.hide(); 
		    refreshMiniCartLink();
            //var myDiv = document.getElementById("ajax-body-1");	            
            //myDiv.innerHTML = 'The product has been successfully added to the cart';	            
           // DialogPanel.show('modalDialogPanel1');	            
           // svg_classhandlers_decoratePage();
           
			
             var myMessageDiv = document.getElementById("errorMsgForQty");
             if(document.getElementById('isEditOrder')!=null && document.getElementById('isEditOrder').value!=null && document.getElementById('isEditOrder').value!='')
            	 myMessageDiv.innerHTML = "Item has been added to order." ;
			 else
				 myMessageDiv.innerHTML = "Item has been added to cart." ;	            
             myMessageDiv.style.display = "inline-block"; 
             myMessageDiv.setAttribute("class", "success");
		    
			 
        },
        failure: function (response, request){
			Ext.MessageBox.hide(); 
            //var myDiv = document.getElementById("ajax-body-1");
            //myDiv.innerHTML = response.responseText;
           // DialogPanel.show('modalDialogPanel1');
           // svg_classhandlers_decoratePage();
             }
    });		
}

</script>
<script>
function checkModalRadio()
{
 //alert("Fired!!");
  if(document.getElementById("radio3").checked)
  {	
			document.getElementById("divs1").style.display = "none";
			document.getElementById("divs2").style.display = "none";
			document.getElementById("divs3").style.display = "none";
  }
}
 
 //-- Form Validation 
function SubmitActionWithValidation() 
  {	
	    var phoneField = document.dataform.rphone;
	    var emailField = document.dataform.rmail;
	    var contactField = document.dataform.rcontact2;
	    var quantityField = document.dataform.Quantity;
	    //alert ( "Phone -- " + document.getElementById("rphone").value  + Alt :" , Alt : -- " + phoneField.value  +" , Length-- " + phoneField.value.trim().length );
	    
	    var fedExServiceProviderNumberField = document.dataform.dataform_serviceProviderNumber_FedEx; 
	    var upsServiceProviderNumberField = document.dataform.dataform_serviceProviderNumber_UPS; 
	  
  		var errorDiv = document.getElementById("errorMsgForRequestSample");
		var errorDivMessage = "Please enter required field(s) : ";
		var returnval = true;
			  
		errorDiv.innerHTML = "";
	    errorDiv.style.display = "none";
	    if (phoneField.value.trim().length == 0)
	    {
	        errorDivMessage= errorDivMessage + "-Phone ";
	        phoneField.style.borderColor="#FF0000";
	        errorDiv.style.display = 'inline-block';
	        returnval = false;
	    }
	    
	    if (emailField.value.trim().length == 0)
	    {
	        errorDivMessage= errorDivMessage + "-Email Address ";
	        emailField.style.borderColor="#FF0000";
	        errorDiv.style.display = 'inline-block';
	        returnval = false;
	    }
	    
	    if (contactField.value.trim().length == 0)
	    {
	        errorDivMessage= errorDivMessage + "-Attention ";
	        contactField.style.borderColor="#FF0000";
	        errorDiv.style.display = 'inline-block';
	        returnval = false;
	    }
	    
	    if (quantityField.value.trim().length == 0)
	    {
	        errorDivMessage= errorDivMessage + "-Qty ";
	        quantityField.style.borderColor="#FF0000";
	        errorDiv.style.display = 'inline-block';
	        returnval = false;
	    }
	    
	    
	    if ( (document.getElementById('dataform_FedEx').checked==true) || (document.getElementById('dataform_UPS').checked==true) ) {
	    	
			//if ( (fedExServiceProviderNumberField.value.trim().length == 0) && (upsServiceProviderNumberField.value.trim().length == 0)) 
			//{
			//	alert("Please enter Tracking number for FedEx/UPS ");
			//} 
		    	
			if(( fedExServiceProviderNumberField.value.trim().length ==0 ) && ( upsServiceProviderNumberField.value.length ==0 )) 
			 {
		        errorDivMessage= errorDivMessage + "-FedEx/UPS Number ";
		        fedExServiceProviderNumberField.style.borderColor="#FF0000";
		        errorDiv.style.display = 'inline-block';
		        returnval = false;
		        
		        //errorDivMessage= errorDivMessage + "-FedEx/UPS Number ";
		        upsServiceProviderNumberField.style.borderColor="#FF0000";
		        errorDiv.style.display = 'inline-block';
		        returnval = false;
			 }else{
				 fedExServiceProviderNumberField.style.borderColor="";
				 upsServiceProviderNumberField.style.borderColor="";
			}
	    }
	   
	    if(returnval == true )
	    {	
	    //added for jira 2971
	    	$(document).ready(function(){
	    		$("#confirmSampleRequest").fancybox({
	    			'autoDimensions'	: false,
	    			'width' 			: 200,
	    			'height' 			: 150  
	    		}).trigger('click');
	    	    
	    	});
	    
	    	//end of jira 2971
	    	
	    	SubmitAction(); 
	    }else {
	    	errorDiv.innerHTML = errorDivMessage;
	    }
	    
  }
  

</script>




<!-- copied javascript from old page ends.. -->

<body class="ext-gecko ext-gecko3">

<s:if test='%{#_action.getWCContext().isGuestUser() == true}'>
	<s:include value='XPEDXAnonItemDetails.jsp' />
</s:if>
<s:else>
<div id="main-container">
	<div id="main">
<!-- begin header --> 
	<s:action name="xpedxHeader" executeResult="true" namespace="/common" >
		<s:param name='shipToBanner' value="%{'true'}" />
	</s:action> 
<!-- // header end -->
        
		<div class="container">
		<s:set name="emailDialogTitle" scope="page"
			value="#_action.getText('Email_Title')" />

				<s:if test="%{null != #xutil.getChildElement(#itemListElem, 'Item')}">
			<s:set name="itemElem"
				value='#xutil.getChildElement(#itemListElem,"Item")' />
			<s:set name="itemElemExtn"
				value='#xutil.getChildElement(#itemElem,"Extn")' />
			<s:set name='certFlag'
				value="#xutil.getAttribute(#itemElemExtn, 'ExtnCert')" />
			<s:set name="prodMweight" value="%{#_action.getProdMweight()}"/>
			<s:set name="pricingUOMConvFactor" value="%{#_action.getPricingUOMConvFactor()}"/>
			<s:set name='isSuperseded'
				value='#xutil.getAttribute(#itemElem,"IsItemSuperseded")' />
			<s:set name="primaryInfoElem"
				value='#xutil.getChildElement(#itemElem,"PrimaryInformation")' />
			<s:set name='isValid'
				value='#xutil.getAttribute(#primaryInfoElem,"IsValid")' />
			<s:set name="computedPrice"
				value='#xutil.getChildElement(#itemElem,"ComputedPrice")' />
			<s:set name="availabilityEle"
				value='#xutil.getChildElement(#itemElem,"Availability")' />
			<s:set name='imageLocation'
				value="#xutil.getAttribute(#primaryInfoElem, 'ImageLocation')" />
			<s:set name='imageId'
				value="#xutil.getAttribute(#primaryInfoElem, 'ImageID')" />
			<s:set name='imageLabel'
				value="#xutil.getAttribute(#primaryInfoElem, 'ImageLabel')" />
			<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
			<s:if test='%{#imageURL=="/"}'>
				<s:set name='imageURL' value='%{"/xpedx/images/INF_150x150.jpg"}' />
			</s:if>
			<s:set name="itemAssets"
				value='#xutil.getChildElement(#itemElem,"AssetList")' />
			<!--<s:set name="itemLargeImages" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_LRG_1")' />-->
			<s:set name="hasItemLargeImages"
				value='#catalogUtil.hasAssetsDefined(#itemAssets,"ITEM_IMAGE_LRG_1")' />
			<s:set name="itemThumnailImages"
				value='#catalogUtil.getAssetList(#itemAssets,"ITEM_THUMBNAIL_IMAGE1")' />
			<s:set name='pImg'
				value='%{#imageLocation+"/"+#primaryInfoElem.getAttribute("ImageID")}' />
			<s:if test='%{#pImg=="/"}'>
				<s:set name='pImg' value='%{"/xpedx/images/INF_150x150.jpg"}' />
			</s:if>
			<s:set name="itemDataSheets"
				value='#catalogUtil.getAssetList(#itemAssets,"ITEM_DATA_SHEET")' />
			<s:set name="itemMainImages"
				value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_1")' />
			<s:set name='minQty'
				value="#xutil.getAttribute(#primaryInfoElem, 'MinOrderQuantity')" />
			<s:set name='maxQty'
				value="#xutil.getAttribute(#primaryInfoElem, 'MaxOrderQuantity')" />
			<s:set name='kitCode'
				value='#xutil.getAttribute(#primaryInfoElem,"KitCode")' />
			<s:set name='isModelItem'
				value='#primaryInfoElem.getAttribute("IsModelItem")' />
			<s:set name='isPreConfigured'
				value='#primaryInfoElem.getAttribute("IsPreConfigured")' />
			<s:set name='isModelItem'
				value='#primaryInfoElem.getAttribute("IsModelItem")' />
			<s:set name='isConfigurable'
				value='#primaryInfoElem.getAttribute("IsConfigurable")' />
			<s:set name='itemID' value='#xutil.getAttribute(#itemElem,"ItemID")' />
			<s:set name='unitOfMeasure'
				value='#xutil.getAttribute(#itemElem,"UnitOfMeasure")' />
			<s:set name="itemElem1"
				value='#xutil.getChildElement(#itemListElem,"Item")' />
			<s:set name='itemID' value='#xutil.getAttribute(#itemElem1,"ItemID")' />
			<s:set name='unitOfMeasure'
				value='#xutil.getAttribute(#itemElem1,"UnitOfMeasure")' />
			<s:set name='configurationKey'
				value='#xutil.getAttribute(#primaryInfoElem,"ConfigurationKey")' />
			<s:set name='isConfigurableBundle' value='%{"N"}' />
			<!-- added for jira 2084 -->
			<s:set name='manufacturer' value='#xutil.getAttribute(#primaryInfoElem,"ManufacturerName")' />
			
			<s:if test='%{#kitCode == "BUNDLE" && #isConfigurable=="Y" }'>
				<s:set name='isConfigurableBundle' value='%{"Y"}' />
			</s:if>
			<s:set name='ManufactureSKU'
				value='#xutil.getAttribute(#primaryInfoElem,"ManufacturerItem")' />
		</s:if>
		
	<!-- If condition... and form things starts..-->
			<s:if test="%{null != #xutil.getChildElement(#itemListElem, 'Item')}">

			<!-- icons for print & email 
		    <div class="iconBox" id="icons">
		      <ul class="icons">
						<s:if test="%{null != #xutil.getChildElement(#itemListElem, 'Item')}">
							<s:set name="itemElem1" value='#xutil.getChildElement(#itemListElem,"Item")' />
							<s:set name='itemID' value='#xutil.getAttribute(#itemElem1,"ItemID")' />
							<s:set name='unitOfMeasure' value='#xutil.getAttribute(#itemElem1,"UnitOfMeasure")' />
							<s:set name='isSuperseded' value='#xutil.getAttribute(#itemElem,"IsItemSuperseded")' />
							<s:set name='isModelItem' value='#primaryInfoElem.getAttribute("IsModelItem")' />
							<s:set name='isValid' value='#primaryInfoElem.getAttribute("IsValid")' />
						</s:if>
		        <li>
		          <s:a cssClass="printIcon" href="javascript:openPrintItemPage()" tabindex="101">
							  <s:text name="Print.page" />
						  </s:a>
		        </li>
		        <s:url id='itemEmailURL' includeParams="none" escapeAmp="false" namespace='/catalog' value='emailItemDetails.action?itemID=%{#itemID}&unitOfMeasure=%{#unitOfMeasure}&messageType=ComposeMail'/>
		        <li>
						  <s:a cssClass="emailIcon" href="javascript:openItemEmailLightBox('%{#itemEmailURL}');" tabindex="103">
							  <s:text name="Email.page" />
						  </s:a>
		        </li>
		      </ul>
		      <div class="itemVariationInstr" >
						<s:if test='%{#isModelItem=="Y" && #isValid == "Y"}'>
							<s:text name="Choose.an.item.from.the.variation.tab.below" />
						</s:if>
			  </div>
		      <div class="clearBoth">&nbsp;</div>
		    </div> -->

			<!-- main content -->
			<s:form name="printPage" id="printPage" action="itemDetails">
				<s:hidden id="isPrintPage" name="isPrintPage" value="%{true}" />
				<s:hidden id="itemID" name="itemID" value="%{#itemID}" />
				<s:hidden id="unitOfMeasure" name="unitOfMeasure"
					value="%{#unitOfMeasure}" />
			</s:form>

			<s:form name="OrderDetailsForm" id="OrderDetailsForm"
				namespace="/order" action="xpedxAddItemsToList">
				<s:hidden name="orderHeaderKey" value='%{#orderHeaderKey}' />
				<s:hidden name="draft" value="%{#draftOrderFlag}" />
				<s:hidden name='Currency' value='%{#currencyCode}' />
				<s:hidden name='mode' value='%{#mode}' />
				<s:hidden name='fullBackURL' value='%{#returnURL}' />
				<s:hidden name="orderLineKeyForNote" id="orderLineKeyForNote"
					value="" />

				<s:hidden name="listKey" id="listKey" value="" />
				<s:hidden name="selectedLineItem" value="1" />
				<s:hidden name="orderLineKeys" value="1" />
				<s:hidden name="orderLineItemOrders" value="" />

				<s:hidden name="orderLineItemIDs" value="%{#itemID}" />

				<s:set name="shortDesc"
					value='%{#utilMIL.formatEscapeCharacters(#xutil.getAttribute(#primaryInfoElem, "ShortDescription"))}' />
				<s:set name="longDesc"
					value='%{#utilMIL.formatEscapeCharacters(#xutil.getAttribute(#primaryInfoElem, "Description"))}' />

				<s:hidden name="orderLineItemNames" value='%{#shortDesc}' />
				<s:hidden name="orderLineItemDesc" value='%{#longDesc}' />

				<s:hidden name="orderLineQuantities"
					value="%{#xutil.getAttribute(#primaryInfoElem, 'MinOrderQuantity')}" />
				<s:hidden name="orderLineCustLineAccNo" value=" " />
				<s:hidden name="itemUOMs" value=" " />
				<s:hidden name="sendToItemDetails" value="true" />

				<s:hidden name="itemID" value="%{#itemID}" />
				<s:hidden name="unitOfMeasure" value="%{#parameters.unitOfMeasure}" />
				<s:hidden name="customerLinePONo" value="" />
			</s:form>
			
	<!-- END of new MIL - PN -->
	</s:if>
	<!-- Hemantha -->
	<s:include value="../modals/XPEDXSelectWishListModal.jsp"></s:include>
	<!-- Hemantha -->


		
	<!--- begin breadcrumbs header--->
		<div id="breadcrumbs-list-name" class="breadcrumbs-no-float">
			<!-- Begin - Changes made by Mitesh Parikh for 2422 JIRA -->	
			<script type="text/javascript">
			function getbackPageUrl(){				
				var backPageUrl = document.getElementById("backPageUrl").value;
				window.location.href=backPageUrl;
			}
			</script>
			<s:if test= '%{#_action.getGoBackFlag() == "true"}'>
			<a href="#"  onclick="javascript:window.history.back();">Back</a> / <span class="page-title"><s:property value='%{#itemID}' /></span>
			</s:if>
			<s:else>
				<a href="javascript:getbackPageUrl();">Back</a> / <span class="page-title"><s:property value='%{#itemID}' /></span>
			</s:else>
			<% 
			HttpServletRequest httpRequest = (HttpServletRequest) request; 
			String referer1 = httpRequest.getHeader("referer");
			if(httpRequest.getSession().getAttribute("itemDtlBackPageURL")==null)
			{
				httpRequest.getSession().setAttribute("itemDtlBackPageURL",referer1);				
			} 
						
			%>	
			<s:hidden id='backPageUrl' name='backPageUrl' value="%{#session.itemDtlBackPageURL.substring(#session.itemDtlBackPageURL.indexOf('/swc'))}" />
			
			<!-- End - Changes made by Mitesh Parikh for 2422 JIRA -->
			
			<a href="javascript:window.print()"><span class="print-ico-xpedx underlink" style="margin-right: 15px;margin-top: 0px;"> <img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon.gif" width="16"
			height="15" alt="Print Page" />Print Page</span>
			</a>
		</div>				
		<!--- clearall puts stuff below 'floating' items. nonbreaking space is for IE--->				
		<div class="clearall">&nbsp;</div>
	<!--- end breadcrumbs header --->
	<s:form name="productDetailForm" id="productDetailForm"
		onsubmit="return false;	">
		<s:hidden name="#action.namespace" value="/catalog" />
		<s:hidden id="actionName" name="#action.name" value="itemDetails" />
		<s:hidden id="itemID" name="itemID" value="%{#itemID}" />
		<s:hidden id="unitOfMeasure" name="unitOfMeasure"
			value="%{#unitOfMeasure}" />
		<!-- addToCartURL to be retrieved from javascript -->

		<input type="hidden" id="addToCartURL"
			value="<s:property value='#addToCartURL'/>" />
		<s:hidden id="maxQty" name="maxQty" value="%{#maxQty}" />
		<!-- Webtrend tag starts -->
		<s:hidden id="pritemType" name="pritemType" value="%{prItemtype}" />
		<!-- Webtrend tag ends -->
		
        <s:hidden name='baseUnitOfMeasure' id='baseUnitOfMeasure'
				value='%{#unitOfMeasure}' />        
	<div class="prod_detail">
			<!-- -FXD1-4  change for change location of error and location --> <h5><b><font color="red"><s:property
			value="ajaxLineStatusCodeMsg" /></font></b></h5> 
			<!-- for Jira 2885 -->
			  <div id="errorMessageDiv"></div>
			<!-- End of Jira 2885 -->
		<div class="prod_detail_top">&nbsp;</div>
		<div class="prod_detail_rpt">
			<!-- right -->
		<div class="right-prod-detail">
		<div class="prod_desc">
			<s:url id='punchOutURL'
			namespace='/order' action='configPunchOut' /> <s:set name="itemElem"
			value='#xutil.getChildElement(#itemListElem,"Item")' /> <s:set
			name='isSuperseded'
			value='#xutil.getAttribute(#itemElem,"IsItemSuperseded")' /> <s:set
			name="primaryInfoElem"
			value='#xutil.getChildElement(#itemElem,"PrimaryInformation")' /> <s:set
			name="isValid"
			value='#xutil.getAttribute(#primaryInfoElem,"IsValid")' /> <s:set
			name="computedPrice"
			value='#xutil.getChildElement(#itemElem,"ComputedPrice")' /> <s:set
			name="availabilityEle"
			value='#xutil.getChildElement(#itemElem,"Availability")' /> <s:set
			name='imageLocation'
			value="#xutil.getAttribute(#primaryInfoElem, 'ImageLocation')" /> <s:set
			name='imageId'
			value="#xutil.getAttribute(#primaryInfoElem, 'ImageID')" /> <s:set
			name='imageLabel'
			value="#xutil.getAttribute(#primaryInfoElem, 'ImageLabel')" /> <s:set
			name='imageURL' value="#imageLocation + '/' + #imageId " />
		<s:if test='%{#imageURL=="/"}'>
				<s:set name='imageURL' value='%{"/xpedx/images/INF_150x150.jpg"}' />
		</s:if>
		<s:set name="itemAssets"
			value='#xutil.getChildElement(#itemElem,"AssetList")' /> <!--<s:set name="itemLargeImages" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_LRG_1")' />-->
		<s:set name="hasItemLargeImages"
			value='#catalogUtil.hasAssetsDefined(#itemAssets,"ITEM_IMAGE_LRG_1")' />
		<s:set name="itemThumnailImages"
			value='#catalogUtil.getAssetList(#itemAssets,"ITEM_THUMBNAIL_IMAGE1")' />
		<s:set name="itemDataSheets"
			value='#catalogUtil.getAssetList(#itemAssets,"ITEM_DATA_SHEET")' />
		<s:set name="itemMainImages"
			value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_1")' /> <s:set
			name='minQty'
			value="#xutil.getAttribute(#primaryInfoElem, 'MinOrderQuantity')" />
		<s:set name='maxQty'
			value="#xutil.getAttribute(#primaryInfoElem, 'MaxOrderQuantity')" />
		<s:set name='kitCode'
			value='#xutil.getAttribute(#primaryInfoElem,"KitCode")' /> <s:set
			name='isModelItem'
			value='#primaryInfoElem.getAttribute("IsModelItem")' /> <s:set
			name='isPreConfigured'
			value='#primaryInfoElem.getAttribute("IsPreConfigured")' /> <s:set
			name='isModelItem'
			value='#primaryInfoElem.getAttribute("IsModelItem")' /> <s:set
			name='isConfigurable'
			value='#primaryInfoElem.getAttribute("IsConfigurable")' /> <s:set
			name='itemID' value='#xutil.getAttribute(#itemElem,"ItemID")' /> <s:set
			name='unitOfMeasure'
			value='#xutil.getAttribute(#itemElem,"UnitOfMeasure")' /> <s:set
			name='currency' value='#xutil.getAttribute(#itemListElem,"Currency")' />
		<s:if test='%{#kitCode == "BUNDLE" }'>
			<s:set name='price'
				value='#xutil.getAttribute(#computedPrice,"BundleTotal")' />
		</s:if><s:else>
			<s:set name='price'
				value='#xutil.getAttribute(#computedPrice,"UnitPrice")' />
		</s:else> <s:if test="displayPriceForUoms.size() > 0">
			<s:set name='price' value='%{displayPriceForUoms.get(2)}' />

			<s:set name='formattedUnitprice'
				value='#xpedxutil.formatPriceWithCurrencySymbol(#scuicontext,#currency,#price,#showCurrencySymbol)' />
			<!-- 
						<tr>
							<td>
	                            <span class="headerProductName"><s:property value='#xutil.getAttribute(#primaryInfoElem,"ShortDescription")' /></span>
	                        </td>
							<td align="right" width="35%">
								<h2 class="itemPrice">
									<s:text name="Price" />: <s:property value='#formattedUnitprice' />
	                            </h2>
							</td>
						</tr>  -->
		</s:if> <!-- </table>  -->	
		<s:set name="xpedxItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_ITEM_LABEL"/>
					<s:set name="customerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUSTOMER_ITEM_LABEL"/>
					<s:set name="manufacturerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MANUFACTURER_ITEM_LABEL"/>
					<s:set name="mpcItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MPC_ITEM_LABEL"/>
		
					
					<p class="short-description"><s:property
			value='#xutil.getAttribute(#primaryInfoElem,"ShortDescription")' /></p>
					<p>&nbsp;</p>                  
					<ul class="bullet_pts" >
						<s:property value='#xutil.getAttribute(#primaryInfoElem,"Description")' escape="false"/>
					</ul>
					<br/>
					<p>					
					<ul class="bullet_pts" style="padding-left: 0px;">
						<s:property value='#xutil.getAttribute(#itemElemExtn,"ExtnSellText")' escape="false"/>
					</ul>	  
					</p>					
					<br/>
					<div>
						<s:property value="wCContext.storefrontId" /> <s:property value="#xpedxItemLabel" />: <s:property value='%{#itemID}' />
							<s:if test='certFlag=="Y"'>
								<img border="none"  src="/swc/xpedx/images/catalog/green-e-logo_small.png" alt="" />
							</s:if>
					</div>					
			<s:if test='custSKU!= ""'>
				<p style="margin-top:8px;">
					<s:if test='%{custSKU == "1"}' >
						<s:property value="#customerItemLabel" />: <s:property value='custPartNumber' />
					</s:if>
					<s:elseif test='%{custSKU == "2" }'>
						<s:property value="#manufacturerItemLabel" />: <s:property value='ManufacturerPartNumber' />
					</s:elseif>
					<s:elseif test='%{custSKU == "3" }'>
						<s:property value="#mpcItemLabel" />: <s:property value='MPC' />
					</s:elseif>
				</p>				
			</s:if>
			
			<br/>
					<div class="red bold"> 
    <s:if test="(replacementAssociatedItems!=null && replacementAssociatedItems.size() > 0)">
    <!-- Web Trends tag start -->  
     <meta name="DCSext.w_x_item_repl_p" content="1" />
     <!-- Web Trends tag end -->  
    			This item has been replaced. Select item: 
				<s:iterator value='replacementAssociatedItems' id='replacementItem'
						status="count" >											
				<s:set name="promoItemPrimInfoElem"
					value='#xutil.getChildElement(#replacementItem,"PrimaryInformation")' />
				<s:set name="promoItemComputedPrice"
					value='#xutil.getChildElement(#replacementItem,"ComputedPrice")' />
				<s:set name="itemAssetList"
					value='#xutil.getElementsByAttribute(#replacementItem,"AssetList/Asset","Type","ITEM_IMAGE_1" )' />

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
						<!-- <img src="<s:url value='%{#imageURL}' includeParams='none' />"
										alt="<s:text name='%{#imageLabel}'/>" width="52" height="50" align="left" /> -->
		        	</s:if>
					<s:url id='detailURLFromPromoProd' namespace='/catalog'
							action='itemDetails.action'>
							<s:param name='itemID'>
								<s:property value='#xutil.getAttribute(#replacementItem,"ItemID")' />
							</s:param>
							<s:param name='unitOfMeasure'>
								<s:property
									value='#xutil.getAttribute(#replacementItem,"UnitOfMeasure")' />
							</s:param>
					</s:url><s:if test='#count.index != 0'><font color="black">,&nbsp;</font></s:if><s:a href="%{detailURLFromPromoProd}" tabindex="%{#ipTabIndex}" ><s:property value='#xutil.getAttribute(#replacementItem,"ItemID")' /></s:a>
						 
	    	</s:iterator>
		</s:if>
		<s:if test='#itemID==null || #itemID==""' >
		You are not entitled to view this item.
		</s:if>
		</div>				
					<br/>

	<%-- Adding link for Replacement Items Ends --%>

					
				</div>
			</div>
			<!--end right prod detail -->
			<!-- right side image -->
			<div class="prod-detail-img">
			
			<s:if
			test="#itemMainImages != null && #itemMainImages.size() > 0">
			<s:set name='imageMainLocation'
				value="#xutil.getAttribute(#itemMainImages[0], 'ContentLocation')" />
			<s:set name='imageMainId'
				value="#xutil.getAttribute(#itemMainImages[0], 'ContentID')" />
				<s:hidden name="hdn_imageMainId" value="%{#imageMainId}" />
			<s:set name='imageMainLabel'
				value="#xutil.getAttribute(#itemMainImages[0], 'Label')" />
			<!--Removing "/"  from value="#imageMainLocation + "/" + #imageMainId " />-->
			<s:set name='imageMainURL'
				value="#imageMainLocation + #imageMainId " />
			<s:if test='%{#imageMainURL=="/"}'>
				<s:set name='imageMainURL' value='%{"/xpedx/images/INF_150x150.jpg"}' />
			</s:if>
			<img src="<s:url value='%{#imageMainURL}' includeParams='none'/>"
				width="150" height="150" id="productImg1"
				alt="<s:text name='%{#imageMainLabel}'/>" />
		</s:if> <s:else>
			<img src="<s:url value='%{#pImg}'/>" width="150" height="150" 
				id="productImg1" alt="<s:text name='%{#pImg}'/>"/>
		</s:else> <s:if
			test='%{#hasItemLargeImages != null && #hasItemLargeImages == "true"}'>
			<script type="text/javascript"
				src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/component/lightbox.js"></script>
			<script type="text/javascript"
				src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/commonutils.js"></script>
			<script>
        	Ext.ux.Lightbox.register('a.imageViewer', true); // true to show them as a set
        	</script>
			<div id="itemImagesViwer"></div>
		</s:if>
			
		</div>
			<!--<div class="clearall">&nbsp; </div>-->
			<!-- end right side image -->
			<!-- begin lower half -->
		<s:if test='#itemID!=null && #itemID!=""' >	
			<!-- BEGIN prod-config -->
			<div class="prod-config">
			<s:set name="addToCartDisabled"
			value="%{''}" /> <s:if
			test='%{(#catalogUtil.hasAccessToAddtoCart(#primaryInfoElem,#formattedUnitprice))=="Y" || updateAvailability}'>
			<s:set name="addToCartDisabled" value="%{''}" />
				</s:if> 
			<s:if test='!#isReadOnly && !(#_action.getWCContext().isGuestUser() == true)'>
			<s:if test="%{#addToCartDisabled ==''}">
			
			<s:hidden id="Qty_Check_Flag" name="Qty_Check_Flag" value="false"/>
				<label class="input-label" for="QTY">Qty:</label>
				<s:if test='#minQty != null && #minQty > 0'>
					<s:set name='dispMinQty' value="#minQty" />
				</s:if>
				<s:else>
					<s:set name='dispMinQty' value='1' />
				</s:else>
				<s:hidden name="requestedQty" id="requestedQty"
					value="%{#util.formatQuantity(wCContext, #dispMinQty)}" />
				<s:set name="requestedQty"  value="%{#_action.getRequestedQty()}" />
				<s:textfield name='qtyBox' id="qtyBox"
					cssClass="input-label x-input" size="7" maxlength="7"
					value="%{#_action.getRequestedQty()}"
					disabled='%{(#addToCartDisabled == "disabled")?"true":"false"}'
					tabindex="260" theme="simple" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);"
					onchange="javascript:isValidQuantity(this); javascript:qtyInputCheck(this);" onmouseover="javascript:qtyInputCheck(this);">
				</s:textfield>

				
				<%-- 2964 start<s:if test="itemUOMsMap != null && itemUOMsMap.size() > 0">
				 --%>
				 <s:if test="itemIdConVUOMMap != null && itemIdConVUOMMap.size() > 0">
				
					<s:set name="mulVal" value='itemOrderMultipleMap[#itemID]' />
					
					<s:hidden name="OrderMultiple" id="OrderMultiple"
						value="%{#mulVal}" />
					<s:set name="itemuomMap" value='#_action.getDisplayItemUOMsMap()' />
					<s:select name="itemUOMsSelect" id="itemUOMsSelect" onchange='javascript:updateUOMFields()'
						cssClass="qty_selector"
						list="itemuomMap" listKey="key"
						listValue="value"
						value='%{#_action.getRequestedDefaultUOM()}' disabled="%{#isReadOnly}" 
						tabindex="%{#tabIndex}" theme="simple" />
					<s:hidden name="requestedUOM" id="requestedUOM" value="" />
					<s:hidden name="uomId" id="uomId" value="" />
					<s:set name="requestedUOM"  value="%{#_action.getRequestedUOM()}" />
					<%-- <s:hidden name="selectedUOM" value="%{#requestedUOM}" id="selectedUOM" /> 3253--%>
					<s:hidden name="selectedUOM" value="%{#_action.getRequestedDefaultUOM()}" id="selectedUOM" />
					<%-- 2964 Start<s:set name="convFac" value='itemUOMsMap[#requestedUOM]' />
					 --%>
					 <s:set name="convFac" value='itemIdConVUOMMap[#requestedUOM]' />
					
					<s:hidden name="uomConvFactor" id="uomConvFactor" value="%{#convFac}" /> <!--  TODO : Purma -LP3- Conversion factor space -->
					
					<%-- 2964 statt<s:iterator value='itemUOMsMap'>
					 --%><s:iterator value='itemIdConVUOMMap'>
					
							<s:set name='currentUomId' value='key' />
							<s:set name='currentUomConvFact' value='value' />
							<s:hidden name='convF_%{#currentUomId}' id="convF_%{#currentUomId}" value="%{#currentUomConvFact}" />
					</s:iterator>
					
					<s:url id='updatePandAURLid' namespace='/catalog'
						action='itemDetails' />
					<s:a id='updatePandAURL' href='%{#updatePandAURLid}' />
				</s:if>
				
				<br />
				<s:if test=' (isCustomerLinAcc == "Y") '>
					<!-- Job Number represents Customer Line Account # -->
					<div class="jobNum line-spacing">
						<label class="left35"><s:property value='custLineAccNoLabel' />:</label>
						<input name="Job" class="input-details x-input"  id="Job" tabindex="12" title="JobNumber" maxlength="24"/>
					</div>
				</s:if>
				<input type="hidden" name="Customer" class="input-details x-input" id="Customer"
					tabindex="12" title="CustomerNumber" />

			
				<s:if test=' (isCustomerPO == "Y") '>
				<div class="linePO line-spacing">
					<s:property value='customerPOLabel' />:
					<s:textfield name='customerPONo' theme="simple"
						cssClass="input-details x-input" id="customerPONo" value="" title="CustomerNumber"
						tabindex="%{#tabIndex}" maxlength="22"/>					
					
				</div>
				</s:if>	

					<s:if test='!#isProcurementUser'>
					<s:set name='cartUser'
						value='#_action.getWCContext().getEffectiveUserId()' />
					<s:set name='cartUserRem'
						value='#_action.getWCContext().getRememberedUserId()' />
					<!--
                                           <p>
                                           <s:if test='%{#isFlowInContext == false}'>
                                               <s:if test='%{#_action.getWCContext().isGuestUser() == true && (#cartUserRem == null || #cartUserRem.length() == 0)  && #isModelItem != "Y"}'>
                                               </s:if>
                                               <s:else>
                                                   <s:text name='Cart_Belong_To'>
                                                       <s:param value='cartUser'/>
                                                   </s:text>
                                               </s:else>
                                           </s:if>
                                           </p>
                                           -->
				</s:if>

				</s:if>
			</s:if>
				<div class="orderbtns">
					<a href="javascript:updatePandA()">My Price & Availability</a>
				<input type="hidden" name="isEditOrder" id="isEditOrder" value="<s:property value='#isEditOrderHeaderKey'/>"/>	
				<s:if test='%{#isFlowInContext == true}'>
					<s:a
						href="javascript:itemDetailAddToCart('%{#itemID}', '%{#unitOfMeasure}', '%{#appFlowContext.key}','%{#appFlowContext.returnURL}','%{#appFlowContext.type}','%{#appFlowContext.currency}');"
						cssClass="orange-ui-btn" id="addToOrder" tabindex="261"
						theme="simple">
						<span> <s:text name='Add_to_order' /></span>
					</s:a>
				</s:if>
				<s:else>
					<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
						<s:a href="javascript:addItemToCart();" theme="simple"
							cssClass="orange-ui-btn" id="addToCart" tabindex="261">
							<span><s:text name='Add_to_cart' /></span>
						</s:a>
					</s:if>
					<s:else>
						<s:a href="javascript:addItemToCart();" theme="simple"
							cssClass="orange-ui-btn" id="addToCart" tabindex="261">
							<span><s:text name='Add_to_order' /></span>
						</s:a>
					</s:else>
				</s:else>
					<br/><br/>
					<div class="rebel_btn">
						<a class="grey-ui-btn" href="javascript:addItemToWishList();"><span>Add to My Items</span></a>
					</div>

					
				</div>
				
				<!--  DEFINE DIV TAG FOR MESSAGES -->		
				<br/>
				<div class="error" id="errorMsgForQty" style="display : none"> &nbsp; <br/></div>
				
				
				<br/>
				<%-- 2964 Start<s:if test="itemUOMsMap != null && itemUOMsMap.size() > 0">
				 --%><s:if test="itemIdConVUOMMap != null && itemIdConVUOMMap.size() > 0">
				
					<s:set name="mulVal" value='itemOrderMultipleMap[#itemID]' />
					<s:set name="requestedUOM"  value="%{#_action.getRequestedUOM()}" />
					<%--<s:hidden name="selectedUOM" value="%{#requestedUOM}" id="selectedUOM" />  --%>
					<s:hidden name="selectedUOM" value="%{#_action.getRequestedDefaultUOM()}" id="selectedUOM" />
					<s:hidden name="OrderMultiple" id="OrderMultiple"
						value="%{#mulVal}" />
						
				
				<s:if test='%{#mulVal >"1" && #mulVal !=null}'>		
				
				<script>
					var myMessageDiv = document.getElementById("errorMsgForQty");	            
		            myMessageDiv.innerHTML = "<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value='%{#xpedxutil.formatQuantityForCommas(#mulVal)}'></s:property> <s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#_action.getBaseUOM())'></s:property>";	            
		            myMessageDiv.style.display = "inline-block"; 
		            myMessageDiv.setAttribute("class", "notice");
				</script>
				
				
				<%-- <div class="temp_UOM" id="errorMsgForQty" style="display : inline-block"><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#mulVal}"></s:property> <s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#_action.getBaseUOM())"></s:property></div><br/> --%>
				
				<%-- 
				<div class="notice" id="errorMsgForQty" style="display : inline-block"><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#mulVal}"></s:property> <s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#_action.getBaseUOM())"></s:property></div><br/>
			 --%>	
			 	</s:if>
				</s:if>	
				
				<!-- <div class="error" id="errorMsgForQty" style="display : none">Please enter a quantity greater than 0.<br/></div> -->
				<%-- <div class="error" id="errorMsgForQty" style="display : none"><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGTZERO' /><br/></div> --%>
				<br />
		<script type="text/javascript">
		
		function qtyInputCheck(component){
			var qtyCheckFlag = document.getElementById("Qty_Check_Flag").value;
			if(qtyCheckFlag=="true"){
				if(component.value==""){
            		component.style.borderColor="#FF0000";
	            	document.getElementById('errorMsgForQty').style.display = "inline-block";
    	    	}
        		else{
            		component.style.borderColor="";
            		document.getElementById('errorMsgForQty').style.display = "none";
	    	    }
    		}
    	}	
		
		
		//This function is called when user clicks on 'Add to Wish List' button
		
		function qtyInputCheck(component){
			var qtyCheckFlag = document.getElementById("Qty_Check_Flag").value;
			if(qtyCheckFlag=="true"){
				if(component.value==""){
            		component.style.borderColor="#FF0000";
	            	document.getElementById('errorMsgForQty').style.display = "inline-block";
    	    	}
        		else{
            		component.style.borderColor="";
            		// commented for jira 3253 document.getElementById('errorMsgForQty').style.display = "none";
	    	    }
    		}
    	}	
		
		function addItemToWishList()
		{
			/*
			if(Ext.util.Format.trim(Ext.get('qtyBox').dom.value)!='' && Ext.util.Format.trim(Ext.get('qtyBox').dom.value)!=null)
				quantityValue = Number(Ext.util.Format.trim(Ext.get('qtyBox').dom.value));
			else
				quantityValue = Ext.util.Format.trim(Ext.get('qtyBox').dom.value);
			//Qty validation is happening on keyup pf the qtyBox field, 
			//the below ones are the only invalid ones
			if(quantityValue =='' || quantityValue=='0')
			{
				document.getElementById("qtyBox").style.borderColor="#FF0000";
				document.getElementById("qtyBox").focus();
				document.getElementById("errorMsgForQty").style.display = "inline-block";
				document.getElementById("Qty_Check_Flag").value = true;
				document.getElementById("qtyBox").value = "";
			    return;
			}*/
			$("#dlgAddToListLink").trigger('click');
			/*
			document.getElementById("Qty_Check_Flag").value = false;
			document.getElementById("errorMsgForQty").style.display = "none";
			*/
			//document.getElementById("qtyBox").value = "";
		}
		
		function setAndExecute(divId, innerHTML, isCustomerSelected) { 
			   var div = document.getElementById(divId);  
			   div.innerHTML = innerHTML; 
			   if(isCustomerSelected!=null) 
			   saveShareListForChild(divId, isCustomerSelected);
			   var x = div.getElementsByTagName("script");   
			   for( var i=0; i < x.length; i++) { 
					eval(x[i].text);
				}
			}

		function saveShareListForChild(divId, isCustomerSelected){
			   var allChildElements = $("div[id="+divId + "] :checkbox");
		    for(var j=0;j<allChildElements.length;j++)
				{
					var currentCB = allChildElements[j];
					currentCB.checked = isCustomerSelected;
				}
		}
		
		function createNewWishList()
		{
			$("#dlgShareListLink").trigger('click');
		}
		
		//This function is called to add the item in an existing list/new list from Select a WishList window.
		function addItemsToList(){
			//alert('currentAadd2ItemListIndex '+currentAadd2ItemListIndex);
			//alert('currentAadd2ItemList '+currentAadd2ItemList);
			var idx = currentAadd2ItemListIndex;

			//if idx is not set, no list is selected
			if(idx!=null){
				var quantityValue = Ext.util.Format.trim(Ext.get('qtyBox').dom.value);
		    	/*
		    	Quantity validation is happening on click of 'Add to Wish List' button
		    	if(quantityValue =='' || quantityValue=='0' || quantityValue=='0.0')
				{
				    alert('Enter a valid quantity');
				    document.getElementById("qtyBox").focus();
				    document.body.style.cursor = 'default';
				    var radio = document.addItemToList.itemListRadio;
					if(radio!=null)
					{
				  		for(var i=0;i<radio.length;i++)
				  		{
					  		radio[i].checked= false;
				  		}
					}
				    return;
				}*/

				//Ext.Msg.wait("Adding item to list... Please wait.");
				xpedx_working_start();
                setTimeout(xpedx_working_stop, 4000);
				
				document.OrderDetailsForm.orderLineItemNames.value 	= unescape(document.OrderDetailsForm.orderLineItemNames.value);
				document.OrderDetailsForm.orderLineItemDesc.value 	= unescape(document.OrderDetailsForm.orderLineItemDesc.value);
				
				if (document.getElementById("itemUOMsSelect") == null){
					document.OrderDetailsForm.itemUOMs.value = "EACH";
				} else {
					document.OrderDetailsForm.itemUOMs.value 	= document.getElementById("itemUOMsSelect").value;
						
				}
				
				if (document.getElementById("qtyBox")){
					document.OrderDetailsForm.orderLineQuantities.value = quantityValue;
				}

				//Job#
				if (document.getElementById("Job")!=null){
					document.OrderDetailsForm.orderLineCustLineAccNo.value = document.getElementById("Job").value;
				}

				if(document.getElementById("customerPONo")!=null) 
				{
					document.OrderDetailsForm.customerLinePONo.value=document.getElementById("customerPONo").value;
				}
				document.getElementById("listKey").value	= currentAadd2ItemList.value;
			    //document.OrderDetailsForm.submit();
		       
		        <s:url id='AddItemURL' includeParams='none' escapeAmp="false" namespace="/order" action="xpedxAddItemsToList" />
	        
	        	var url = "<s:property value='#AddItemURL'/>";
		        url = ReplaceAll(url,"&amp;",'&');

		        var itemCountValOfSelList = document.getElementById("itemCount_"+currentAadd2ItemList.value);
		        //alert('Item count of selected list is '+itemCountValOfSelList.value);
		        document.OrderDetailsForm.orderLineItemOrders.value = Number(itemCountValOfSelList.value) + 1;
		        if(itemCountValOfSelList.value<200){
					//Execute the call
		        	document.body.style.cursor = 'wait';
		        	var orderDetailsForm = Ext.get("OrderDetailsForm");
			        orderDetailsForm.dom.listKey.value = currentAadd2ItemList.value;
			        var itemId = document.OrderDetailsForm.orderLineItemIDs.value;
		        	Ext.Ajax.request({
		          		url: url,
		          		form: 'OrderDetailsForm',
		          		method: 'POST',
		          		success: function (response, request){
		              		document.body.style.cursor = 'default';
		              		Ext.Msg.hide();
					  		//reloadMenu();
							// Removal of MIL dropdown list from header for performance improvement
					  		itemCountValOfSelList.value = itemCountValOfSelList.value + 1;
					  		//alert("Successfully added item "+itemId+ " to the selected list.");
					  		
					  		var myMessageDiv = document.getElementById("errorMsgForQty");	            
				            myMessageDiv.innerHTML = "Item has been added to the selected list." ;	            
				            myMessageDiv.style.display = "inline-block"; 
				            myMessageDiv.setAttribute("class", "success");

					  		
					  		/*Web Trends tag start*/ 
					  		writeMetaTag("DCSext.w_x_list_additem","1");
					  		/*Web Trends tag end*/

		          		},
		          		failure: function (response, request){
		              		document.body.style.cursor = 'default';
		              		Ext.Msg.hide();
		              		//alert("Error adding item to the list. Please try again later.");
		              		var myMessageDiv = document.getElementById("errorMsgForQty");
		              		//Start fix for 3104
				            myMessageDiv.innerHTML = "No lists have been created. Please create new list." ;	
		              		//End fix for 3104
				            myMessageDiv.style.display = "inline-block"; 
				            myMessageDiv.setAttribute("class", "notice");

		          		}
		       		});
		        }
		        else{
		      		alert("Maximum number of element in a list can only be 200..\n Please try again with removing some items or create a new list.");
		      		Ext.Msg.hide();
		        }
		        Ext.Msg.hide();
		        document.body.style.cursor = 'default';
		        $.fancybox.close();
				}
		    	else{
		    		//alert('Please select a Wish List to add the item');
		    		/*Start- Jira 3104  */
		    		
		    		alert('Please select a list or create a new list.');
		    		
		    		/*End- Jira 3104  */
		    		/*
		    		var myMessageDiv = document.getElementById("errorMsgForQty");	  
					myMessageDiv.innerHTML = "Please select a Wish List to add the item." ;	            
				    myMessageDiv.style.display = "inline-block"; 
				    myMessageDiv.setAttribute("class", "notice"); */
				            
		    		document.body.style.cursor = 'default';
		    		return;
		    	}
			}
					/* Commenting the addItemsToList function used by dropdown
					function addItemsToList(idx){
						if (idx == 1){
							$("#dlgShareListLink").trigger('click');
					    } else if (idx > 1){
					    	var quantityValue = Ext.util.Format.trim(Ext.get('qtyBox').dom.value);
					    	if(quantityValue =='' || quantityValue=='0' || quantityValue=='0.0')
							{
							    alert('Enter a valid quantity');
							    document.getElementById("qtyBox").focus();
							    document.body.style.cursor = 'default';
						  		document.getElementById("itemListSelect").selectedIndex = 0;
							    return;
							}

							//Ext.Msg.wait("Adding item to list... Please wait.");
							xpedx_working_start();
                            setTimeout(xpedx_working_stop, 3000);
							
							document.OrderDetailsForm.orderLineItemNames.value 	= unescape(document.OrderDetailsForm.orderLineItemNames.value);
							document.OrderDetailsForm.orderLineItemDesc.value 	= unescape(document.OrderDetailsForm.orderLineItemDesc.value);
							
							if (document.getElementById("itemUOMsSelect") == null){
								document.OrderDetailsForm.itemUOMs.value = "EACH";
							} else {
								document.OrderDetailsForm.itemUOMs.value 	= document.getElementById("itemUOMsSelect").value;
									
							}
							
							if (document.getElementById("qtyBox")){
								document.OrderDetailsForm.orderLineQuantities.value = quantityValue;
							}

							//Job#
							if (document.getElementById("Job")!=null){
								document.OrderDetailsForm.orderLineCustLineAccNo.value = document.getElementById("Job").value;
							}
							 
							document.getElementById("listKey").value	= document.getElementById("itemListSelect").value;
						    //document.OrderDetailsForm.submit();
					       
					        <s:url id='AddItemURL' includeParams='none' escapeAmp="false" namespace="/order" action="xpedxAddItemsToList" />
					        
					        var url = "<s:property value='#AddItemURL'/>";
					        url = ReplaceAll(url,"&amp;",'&');
					        
					        if(idx > itemCountList.length){
								setArrayValue(currentAadd2ItemList.value,0);
							}

					        var reqIndex;
					        for(i=0 ; i < itemCountList.length ; i++)
					        {
						        if(itemCountList[i].listKeyId==currentAadd2ItemList.value){
							        reqIndex = i;
							        break;
						        }
					        }

					        document.OrderDetailsForm.orderLineItemOrders.value = Number(itemCountList[reqIndex].itemCount)+1;
					        if(itemCountList[reqIndex].itemCount<200){
								//Execute the call
					        	document.body.style.cursor = 'wait';
					        	var orderDetailsForm = Ext.get("OrderDetailsForm");
						        orderDetailsForm.dom.listKey.value=document.getElementById("itemListSelect").value;
						        
					        	Ext.Ajax.request({
					          		url: url,
					          		form: 'OrderDetailsForm',
					          		method: 'POST',
					          		success: function (response, request){
					              		document.body.style.cursor = 'default';
					              		Ext.Msg.hide();
								  		reloadMenu();
								  		itemCountList[reqIndex].itemCount++;
					          		},
					          		failure: function (response, request){
					              		document.body.style.cursor = 'default';
					              		Ext.Msg.hide();
					              		alert("Error adding item to the list. Please try again later.");
					          		}
					       		});
					        }
					        else{
			              		alert("Maximum number of element in a list can only be 200..\n Please try again with removing some items or create a new list.");
			              		Ext.Msg.hide();
					        }
					        document.body.style.cursor = 'default';
						  	document.getElementById("itemListSelect").selectedIndex = 0;
					    }
					}
					*/
				 </script> <!-- END - Adding the MIL dropdown - PN -->

		
		<s:set name="isSalesRep" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute('IS_SALES_REP')}"/>
				 
				<!-- table to properly format the pricing per the mockup -->
				<div class="bottom_right">				
					<s:if test='%{#isStocked !="Y"}'>
						<p id="milltext">Mill / Mfg. Item - Additional charges may apply</p>
					</s:if>
					<br/>
					<br/>
						<div id="displayPricesDiv"></div>					
					<br/>
					<br/>
				</div>
				<!-- end table and pricing info -->
			</div>
			<!-- END prod-config -->
		<!-- TABS -->
		<div class="avail-grid">
			<div id="tabs"class="ui-tabs ui-widget ui-widget-content ui-corner-all">
				<ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					<li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="#tabs-1">Availability</a></li>
					<li class="ui-state-default ui-corner-top"><a href="#tabs-2" onclick="javascript:blockDiv();">Specifications</a></li>
				</ul>
				
				<p class="tablinks">&nbsp;
			 	<%-- Commented for JIRA 3288	--%>	 	
			 	<%-- <s:set name="canRequestProductSample" value="#session.showSampleRequest" />  --%>
		  	 	<%-- <s:if test='#canRequestProductSample=="Y"'> --%>
 			 		<%-- 	<a id="areqsample" href="#RequestSampleDiv" onclick="javascript: writeMetaTag('WT.ti', '	');">  	
					<s:text name='Request Sample' /> </a>&nbsp;&nbsp; --%>
		   		<%-- </s:if>  --%>
				<s:iterator value="msdsLinkMap" id="msdsMap" status="status" >
					<s:set name="link" value="value" />
					<s:set name="desc" value="key" />	
					<a class="slightly_left" href="<s:property value='#link'/>" target="_blank">MSDS</a>
				</p>
				</s:iterator>
				<!-- tab1 -->
				<div id="tabs-1" class="ie_floatleft">
					<%-- This will be filled by ajax as the P and A call happens on page load as Ajax --%>
				</div>
				<!-- end tab1 -->
                                    
				<!-- tab2 -->
		<div id="tabs-2" style="display: none" ><s:set name="certImage"
			value="#_action.getCertImagePath()" /> 
		<s:if
			test="%{null != #certImage}">
			<img src="#certImage" class="green-e-prod" align="right" />
		</s:if> <s:set name="itemAttributeGroupTypeList"
			value='#xutil.getChildElement(#itemElem,"ItemAttributeGroupTypeList")' />		
		<!-- <div id="tabs-2" class="ie_tabsfix" >	 -->
		<table id="prod-details-tbl" border="0" cellspacing="0"
			cellpadding="0" style="overflow: auto;">
			<tr class="detail-head-prod-bg ui-corner-all">
				<td class="tblhead-white int-deets2" style="border-top-left-radius: 5px;border-top-right-radius: 0;">Specification</td>
				<td class="tblhead-white" style="border-top-left-radius: 0px;border-top-right-radius: 5px;">Details</td>
			</tr>
			<s:iterator
				value='#xutil.getChildren(#itemAttributeGroupTypeList, "ItemAttributeGroupType")'
				id='itemAttributeGroupType'>
				<s:set name='classificationPurposeCode'
					value='#xutil.getAttribute(#itemAttributeGroupType,"ClassificationPurposeCode")' />
				<s:if test="%{#classificationPurposeCode=='SPECIFICATION'}">
					<s:set name="itemAttributeGroupList"
						value='#xutil.getChildElement(#itemAttributeGroupType,"ItemAttributeGroupList")' />
					<s:set name="itemAttrGrp"
						value='#xutil.getChildElement(#itemAttributeGroupList,"ItemAttributeGroup")' />
					<s:if test="#itemAttrGrp!= null">
						<s:set name="counter" value="0" />
						<s:iterator
							value='#xutil.getChildren(#itemAttributeGroupList, "ItemAttributeGroup")'
							id='itemAttributeGroup'>
							<s:set name="itemAttributeList"
								value='#xutil.getChildElement(#itemAttributeGroup,"ItemAttributeList")' />
								<s:set name='tableRowCount' value="%{0}" />
							<s:iterator status="idx"
								value='#xutil.getChildren(#itemAttributeList, "ItemAttribute")'
								id='itemAttribute'>
								<s:set name="assignedValueList_new"
									value='#xutil.getChildElement(#itemAttribute,"AssignedValueList")' />
								<s:set name="assignedValue_new"
									value='#xutil.getChildElement(#assignedValueList_new,"AssignedValue")' />
								<s:if test='%{(#xutil.getAttribute(#assignedValue_new,"Value") != "") && (#xutil.getAttribute(#assignedValue_new,"Value") != null)}' >
									<s:if test="#tableRowCount%2  == 0">
										<tr class="int-deets">
									</s:if>
									<s:else>
										<tr class="odd int-deets">
									</s:else>
									<td><s:property
										value='#xutil.getAttribute(#xutil.getChildElement(#itemAttribute,"Attribute"),"ShortDescription")' /></td>
									<td class="divider"><s:set name="assignedValueList"
										value='#xutil.getChildElement(#itemAttribute,"AssignedValueList")' />
									<s:set name="attribute"
										value='#xutil.getChildElement(#itemAttribute,"Attribute")' />
									<s:set name="dataType"
										value='#xutil.getAttribute(#attribute,"DataType")' /> <s:set
										name="derivedFrom" value='' /> 
									
									<s:if
										test="%{null != #xutil.getAttribute(#attribute,'AllowMultipleValues')}">
										<s:set name='allowMultiVals'
											value='%{#xutil.getAttribute(#attribute,"AllowMultipleValues")}' />
									</s:if> <s:if
										test="%{null != #xutil.getAttribute(#attribute,'IsAllowedValueDefined')}">
										<s:set name='valueDefined'
											value='%{#xutil.getAttribute(#attribute,"IsAllowedValueDefined")}' />
									</s:if> <s:if
										test="%{null != #xutil.getAttribute(#attribute,'DerivedFromAttributeKey')}">
										<s:set name='derivedFrom'
											value='%{#xutil.getAttribute(#attribute,"DerivedFromAttributeKey")}' />
									</s:if> <s:iterator
										value='#xutil.getChildren(#assignedValueList, "AssignedValue")'
										id='assignedValue'>
										<!-- Jira 2634, Adding To Fetch Asset of Attr -->
										<s:set name="Value" value='#xutil.getAttribute(#assignedValue,"Value")' />
										<s:hidden name="hdn_Value" value="%{#Value}" />
										<s:if test='%{"" != #derivedFrom}'>
											<s:property
												value='#xutil.getAttribute(#assignedValue,"Value")' />
											<s:property
												value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
										</s:if>
										<!-- Jira 2634 - Check if Attribute has Asset -->
										<s:elseif test="%{#dataType=='TEXT'}">
										<s:set name="found" value="false" />
										<!-- Adding Iterator assetLinkMap for Jira 2634 -->
										<s:iterator value="assetLinkMap" id="assetMap" status="status" >
											<s:set name="link" value="value" />
											<s:set name="assetId" value="key" />										
											<s:hidden name="hdn_test" value="%{#assetId}"  />
											<s:if test='%{#assetId == #Value}'>
												<a href="<s:property value='#link'/>" target="_blank"><s:property value='#xutil.getAttribute(#assignedValue,"Value")'/></a>
												<s:property	value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
												<s:set name="found" value="true" />
											</s:if>
										</s:iterator>
										<s:if test="%{#found == false}"> 
											<s:property value='#xutil.getAttribute(#assignedValue,"Value")'/>
											<s:property	value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
										</s:if>
											
										</s:elseif>
										<s:elseif test="%{#dataType=='BOOLEAN'}">
											<s:property
												value='#xutil.getAttribute(#assignedValue,"Value")' />
											<s:property
												value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
										</s:elseif>
										<s:elseif test="%{#dataType=='INTEGER'}">
											<s:if
												test='%{(#valueDefined == "Y" && allowMultiVals == "Y") || (#derivedFrom != null && #derivedFrom.length() > 0)}'>
												<s:property
													value='#xutil.getAttribute(#assignedValue,"Value")' />
											</s:if>
											<s:else>
												<s:property
													value='#xutil.getAttribute(#assignedValue,"IntegerValue")' />
											</s:else>
											<s:property
												value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
										</s:elseif>
										<s:elseif test="%{#dataType=='DECIMAL'}">
											<s:if
												test='%{(#valueDefined == "Y" && allowMultiVals == "Y") || (#derivedFrom != null && #derivedFrom.length() > 0)}'>
												<s:property
													value='#xutil.getAttribute(#assignedValue,"Value")' />
											</s:if>
											<s:else>
												<s:property
													value='#xutil.getAttribute(#assignedValue,"DoubleValue")' />
											</s:else>
											<s:property
												value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
										</s:elseif>
									</s:iterator></td></tr>
									<s:set name='tableRowCount' value="#tableRowCount+1" />
								</s:if>
							</s:iterator>
						</s:iterator>
					</s:if>
				</s:if>
			</s:iterator>
		</table>
		</div><!-- end tab2 
			</div>-->
			
			<div class="clearall">&nbsp;</div>
		 <!--</div>   end tabs -->
	</div>
	</s:if>
	
		<s:url id='addToItemListURLid' namespace='/xpedx/myItems'
			action='XPEDXMyItemsDetailsCreate' /> <s:a id='addToItemListURL'
			href='%{#addToItemListURLid}' /> <s:hidden id="itemId" name="itemId"
			value="%{#itemID}" /> <s:hidden id="itemType" name="itemType"
			value="99" /> <s:hidden id="qty" name="qty" value="" /> <s:hidden
			id="listKey" name="listKey" value="" /> <s:hidden id="listName"
			name="listName" value="" /> <s:hidden id="name" name="name"
			value='%{#xutil.getAttribute(#primaryInfoElem,"ShortDescription")}' />
		<s:hidden id="desc" name="desc"
			value='%{#xutil.getAttribute(#primaryInfoElem,"Description")}' />
	
        </div>
        </div>
				<!-- end Tabs -->
                <!-- end main left column -->
		</s:form>
		
		<div class="prod_detail_bot">&nbsp;</div>
		 <!-- begin right column -->
		
		<!-- end right column -->
		</div>     
		<s:include value="XPEDXItemPromotions.jsp"></s:include>     
        </div>
    </div>
    <!-- // container end -->
<!-- BEGIN footer -->
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
<!-- END footer -->


<swc:dialogPanel title="Product Availability" isModal="true"
	id="product_availability">
	<div id="ajax-prodAvailability"></div>
</swc:dialogPanel>
<swc:dialogPanel title='Add To Cart Result' isModal="true"
	id="modalDialogPanel1">
	<div class="dialog-body" id="ajax-body-1"></div>
</swc:dialogPanel>

<!-- end div for light box -->
<swc:dialogPanel title="Bracket Pricing" isModal="true"
	id="bracketPricingDialog" cssClass="my-class">
	<div>
	<table>
		<tr>
			<td>Currency Code</td>
			<td><s:property value="priceCurrencyCode" /></td>
		</tr>
		<tr>
			<td width="30%">UOM</td>
			<td width="30%">QTY</td>
			<td width="30%">PRICE</td>
		</tr>
		<s:iterator value='bracketsPricingList' id='bracket'
			status='bracketStatus'>
			<tr>
				<s:set name="bracketPriceForUOM" value="bracketPrice" />
				<s:set name='formattedBracketpriceForUom'
					value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#bracketPriceForUOM,#showCurrencySymbol)' />
				<s:set name="bracketUOMDesc" value="bracketUOM" />
				<s:set name='formattedbracketUOM'
					value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#bracketUOMDesc)' />
				<td><s:property value="%{#formattedbracketUOM}" /></td>
				<td><s:property value="bracketQTY" /></td>
				<td><s:property value='%{#formattedBracketpriceForUom}' /></td>
			</tr>
		</s:iterator>
	</table>
	</div>
</swc:dialogPanel>
<div id="priceAndAvailabilityAjax" style="display: none" ></div>

<!-- Request Sample Light Box -->

<div style="display: none;">
<div  class="xpedx-light-box"  id="RequestSampleDiv">
<script language="text/javascript">
// 	used in the request sample modal
	function Toggle(thediv) {
	document.getElementById("divs1").style.display = "none";
	document.getElementById("divs2").style.display = "none";
	document.getElementById("divs3").style.display = "none";
	document.getElementById(thediv).style.display = "block";
	}

function tbTest_focus(e,o){
if(o.firstTime){return}
o.firstTime=true
o.value=""
}
function tbTests_focus(e,o){ 
if(o.firstTime){return}
o.firstTime=true
o.value=""
}

</script>

<script type="text/javascript" 
	src='<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric.js'> </script>
<script type="text/javascript" 
	src='<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.maskedinput-1.3.js'></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.phone-numeric').numeric(false); 
		$("#rphone").mask("999 999-9999");
	});
</script>
<!-- added for jira 2971 -->
<a id="confirmSampleRequest" href="#submitSampleRequestDiv"></a>
<!-- end of jira 2971 -->
<form id="dataform" name="dataform">
<div id="inline1" class="xpedx-light-box">


<h2 class="color000"><span class="no-margin">Request Sample</span></h2> 


<fieldset class="feild-service padding5" >
<legend>Shipping Information</legend>

<!-- <div class="clearview">&nbsp;</div> -->
<div id="requestservice">
<table width="100%" class="form-service-light" border="0" cellspacing="0" cellpadding="0" >

		<tr>
		<td valign="top"><div class="form-service-light">
  			<table width="100%" border="0" cellspacing="0" cellpadding="0">
  			<tr valign="">
  			<td width="15%" style="padding-left:3px;"><span class="red">*&nbsp;</span>Attention:</td>
			<td width="33%"><input name="rcontact2" type="text" class="x-input width-250px" id="rcontact2"  maxlength="30" /></td>
	      	<td colspan="2"><div class=" margin-15 padding-bottom5">To expedite the shipment of this request, please provide your FedEx or UPS number.</div> <label>
	      	                               <input  value="radio" type="radio" class="margin-right" name="Toggledivs" id="dataform_FedEx" onclick="Toggle('divs1');"  />FedEx </label>
       		 	<label class="margin-left7"><input value="radio" type="radio" class="margin-right" name="Toggledivs" id="dataform_UPS" onclick="Toggle('divs2');" />UPS </label>
        		<label class="margin-left7"><input value="radio" type="radio" class="margin-right" name="Toggledivs" id="dataform_None" onclick="Toggle('divs3');" checked="checked" />None</label>
				 	<div id="divs1"  class=" float-right  text-align:left;" style=" margin-right: -6px; margin-top: 0px;"> 
						<input type="text" class="x-input width-160px margin-left7" style="margin-top:0px; margin-right:0px; margin-bottom:0px;" name=tbTest id="dataform_serviceProviderNumber_FedEx" type=text onfocus='tbTest_focus(event,this)'[color=blue] value="" name="input5" maxlength="15" />
					</div>
	  				<div id="divs2" class=" float-right  textAlignLeft text-left" style=" margin-right: -6px; margin-top: 0px;"> <input type="text" class="x-input width-160px margin-left7" style="margin-top:0px; margin-right:0px; margin-bottom:0px;" name=tbTests id="dataform_serviceProviderNumber_UPS" type=text onfocus='tbTests_focus(event,this)'[color=blue] value="" maxlength="15"  /></div>
  					<div id="divs3"  class=" "> </div>
			</td>
		</tr>
		<tr>
      		<td nowrap>&nbsp;Address Line1:</td>
      		<td><s:textfield name="ShippingAddress1" id="ShippingAddress1" readonly="true" cssClass="x-input width-250px"  maxlength="35"/></td>
      		<td width="15%" rowspan="3"  valign="top"><div class="">&nbsp;Notes:</div></td>
		    <td width="33%" rowspan="3" valign="top" >
		    <textarea name="Notes" id="Notes" cols="45" rows="5" onkeyup="javascript:restrictTextareaMaxLength(this,250);" class="x-input width-230px paddingtop" ></textarea></td>
    	</tr>
	   <tr>
	      <td nowrap>&nbsp;Address Line2:</td>
    	  <td><s:textfield name="ShippingAddress2" id="ShippingAddress2" readonly="true" cssClass="x-input width-250px" id="raddress2" maxlength="35" /></td>
      </tr>
    <tr>
      <td nowrap>&nbsp;Address Line3:</td>
      <td valign=""><s:textfield name="ShippingAddress3" id="ShippingAddress3" readonly="true"  cssClass="x-input width-250px" id="raddress3" maxlength="35"/></td>
      </tr>
     
    <tr>
      <td>&nbsp;City:</td>
      <td><s:textfield name="CityRequest" readonly="true" cssClass="x-input width-160px" id="rcity" maxlength="20"  /></td>
      <td>&nbsp;State / &nbsp;Province:</td>
      <td><input name="StateRequest" id="rState" type="text" class="x-input width-160px" maxlength="20" /></td>

      </tr>
    <tr>
      <td>&nbsp;Postal Code:</td>
      <td><s:textfield name="ZipRequest" readonly="true" cssClass="x-input width-160px" id="rzipcode" onkeyup="formatPhone('Postal Code',request_form.rzipcode);" maxlength="10" />
      </td>
      <td>&nbsp;Country:</td>
      <td>
      <input name="rCountry" id="rCountry" type="text" class="x-input width-160px" id="rCountry" maxlength="20" />
	<%--

	<select name="select" id="select" class="x-input margin-left3" >
        <option>- Select Country -</option>
         <option value="USA" selected>USA</option>
      </select>
      --%>
      </td>
      </tr>
    <tr>
      <td style="padding-left:3px;"><span class="red">*&nbsp;</span>Phone:</td>
      <td><input name="rphone" id="rphone" type="text" cssClass="x-input phone-numeric width-160px"  maxlength="10"  /></td>
      <td nowrap style="padding-left:3px;"><span class="red">*&nbsp;</span>Email Address:</td>
      <td><input name="rmail" id="rmail" type="text" class="x-input width-160px" id="rmail" maxlength="150" /></td>

      </tr>
  </table>
</div>
</td>
</tr>

</table></div>
</fieldset>

<div class="clearview">&nbsp;</div>
            <table width="100%" id="bottom-table" border="0" align="left" cellpadding="0" cellspacing="0" class="background-none">
            <tbody>
             <tr class="table-header-bar" id="none">

            <td width="16%" class="padding9 table-header-bar-left padding8"><span class="white"> Mfg. Item # </span></td>
            <td width="16%" class="padding9 padding8 left-border"><span class="white">Manufacturer</span></td>
            <td width="16%" class="padding9 padding8 left-border"><span class="white">Item #</span></td>
            <td width="43%" class="padding9 padding8 left-border"><span class="white">Description</span></td>
            <td width="9%" class="padding9-right table-header-bar-right padding8 left-border"><span class="red">*</span><span class="white">Qty</span></td></tr>

            
            <tr>
            <td class="padding9 left-border"> <s:property value='MPC' /></td>
            <s:hidden id="MPC" name="MPC" value='%{#_action.getMPC()}' />
           	<!-- commented for jira 2084
		<td class="padding9 left-border"> <s:property value="ManufacturerPartNumber" /></td>
            <s:hidden id="Manufacturer" name="Manufacturer" value='%{#_action.getManufacturerPartNumber()}' />  -->
        	<!-- added for jira 2084 -->
            <td class="padding9 left-border"> <s:property value='manufacturer' /></td>	
            <s:hidden id="Manufacturer" name="Manufacturer" value='%{#_action.getManufacturer()}' /> 
            <!-- end of jira 2084 -->
            <td class="padding9 left-border"> <s:property value='%{#itemID}' />
            	<s:if test='certFlag=="Y"'>
					<img border="none"  src="/swc/xpedx/images/catalog/green-e-logo_small.png" alt="" style="margin-left:0px; display: inline;"/>
				</s:if>
            </td>
            <s:hidden id="Itemdescription" name="Itemdescription" value='%{#xutil.getAttribute(#primaryInfoElem,"ShortDescription")}' />
            <td class="padding9 left-border"> <s:property value='#xutil.getAttribute(#primaryInfoElem,"ShortDescription")' /></td>				
            <td class="padding9 left-border right-border"><s:textfield name="Quantity" id="Quantity" cssClass="x-input width55" id="Quantity" maxlength="7" /></td></tr>
            </tbody>
            </table>         
<div id="table-bottom-bar" class=" width993 clear-both">
<div id="table-bottom-bar-L"></div>
<div id="table-bottom-bar-R"></div></div>  
<center><div class="error" id="errorMsgForRequestSample" style="display : none"></div></center>
<div class="clearview">&nbsp;</div>


<div class="form-service-light" > 
           <table width="100%" border="0" cellspacing="0" cellpadding="0" align="right">
            <tr>
            <td width="69%">&nbsp;</td>
            <td> 
				<ul id="tool-bar" class="tool-bar-bottom" >
				 <li class=""><a class="grey-ui-btn" href="javascript:$.fancybox.close()"><span>Cancel</span></a></li>
				 <li style="float: right;"><a class="orange-ui-btn" href="javascript:SubmitActionWithValidation();"><span>Submit Request</span></a></li>
				</ul>
            </td>
            </tr> 
            </table> 
</div>
<s:hidden id="Email1" name="Email1" /> <s:hidden id="Email2"
	name="Email2" /></form>
</div>
<!-- End LightBox -->
</div>
<swc:dialogPanel title='${emailDialogTitle}' isModal="true"
	id="emailDialogPanel">
	<div class="dialog-body" id="email-lightbox"></div>
</swc:dialogPanel>
<!-- // main end -->
<s:if
	test='%{#hasItemLargeImages != null && #hasItemLargeImages == "true"}'>
	<script type="text/javascript">
Ext.onReady(function(){
	loadItemImages("itemImagesViwer", "<s:property value='#imgViewerURL'/>","<s:property value='#itemID'/>","<s:property value='#unitOfMeasure'/>");
});


</script>
</s:if>

<script type="text/javascript">
	$(document).ready(function() {
		//added for jira 3253
		updateUOMFields();
		var requestedUom = document.getElementById("selectedUOM").value;
		callPnA(requestedUom);
	});

function callPnA(requestedUom)
{
	var itemId = '<s:property value="#itemID" />';
	var Quantity = document.getElementById("qtyBox").value;
	var baseUom = '<s:property value="#unitOfMeasure" />';
	var prodMweight = '<s:property value="#prodMweight" />';
	var pricingUOMConvFactor = '<s:property value="#pricingUOMConvFactor" />';
	pandaByAjax(itemId,requestedUom,Quantity, baseUom, prodMweight, pricingUOMConvFactor);
}

function blockDiv()
{
	var divId=document.getElementById("tabs-2");
	divId.style.display="block";
}
</script>
<!-- added for jira 2971 --> 
<div style="display: none;">
	<div id="submitSampleRequestDiv">
		 <h2> <s:text name='MSG.SWC.MISC.CONFIRMATION.GENERIC.PGTITLE'></s:text></h2> 		
		<br /><hr /><br></br>
		
		 <p> <s:text name='MSG.SWC.MISC.SAMPLEREQUESTCONFIRMATION.GENERIC.GENINFO'></s:text></p><br></br> <br></br> 
			<ul id="tool-bar" class="tool-bar-bottom">
		<!-- 	<li>
				<a class="grey-ui-btn" href="javascript:$.fancybox.close();">
					<span>No</span>
				</a>
			</li>  -->
			<li class="float-right">
				<a class="green-ui-btn" href="javascript:$.fancybox.close();">
					<span>Ok</span>
				</a>
			</li>
			</ul>
	</div>
</div>        
<!-- end of jira 2971 -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>-->

<!-- carousel scripts js   -->

<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/modals/checkboxtree/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>

-->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>
--><!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions.js"></script>
-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/catalog/XPEDXItemdetails.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.blockUI.js"></script>

<!-- Web Trends tag start -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag.js"></script>
<!-- Web Trends tag end  -->
</body>
</s:else>
</swc:html>
