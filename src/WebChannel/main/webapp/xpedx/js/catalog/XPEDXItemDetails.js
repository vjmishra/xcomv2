var defaultUOM;
var currentAadd2ItemList = null;
var currentAadd2ItemListIndex = null;

$(document).ready(function() {
	var itemId = $('#itemID').val();
	$('#Qty_' + itemId).focus();
	addSpecialInst();
});

$(document).ready(function() {
	//Add to Wish List 
	$("#dlgAddToListLink").fancybox({
		'onStart' 	: function(){
			showPrivateList();
		},
		'onClosed':function() {
			var radio = document.addItemToList.itemListRadio;
			if(radio != null)
			{
				for(var i = 0; i < radio.length; i++)
				{
					radio[i].checked = false;
				}
			}
			currentAadd2ItemList = null;
			currentAadd2ItemListIndex = null;
		},
		'autoDimensions'	: false,
		'width' 			: 380,
		'height' 			: 290  
	});
});

$(document).ready(function() {
	$("#printButton").click(function () {
		$('.specs-tab').show();
		$('.specs-tab-plus').css('background-position', 'bottom left');
		// Print the page
		window.print();
	});
	
	// item spec expand/collapse buttons
	$("#expandAll").click(function () {
		$('.specs-tab').slideDown("fast");
		$('.specs-tab-plus').css('background-position', 'bottom left');
	});

	$("#collapseAll").click(function () {
		$('.specs-tab').slideUp("fast");
		$('.specs-tab-plus').css('background-position', 'top left');
	});

	$('.specs-trigger').click(function() {
		$(this).next('.specs-tab').slideToggle(400, function() {
			var $tab = $(this);
			var $plus = $tab.prev('.specs-trigger').find('.specs-tab-plus');
			if ($tab.is(':visible')) {
				$plus.css('background-position', 'bottom left');
			} else {
				$plus.css('background-position', 'top left');
			}
		});
	});
});

$(document).ready(function() {
	if ($('#isGuestUser').val() == 'false') {
		// get price and availability on load
		updateUOMFields();
		var requestedUom = $('#selectedUOM').val();
		var baseUom = $('#unitOfMeasure').val();
		defaultUOM = $('#selectedUOM').val();

		getPriceAndAvailabilityForItems({modal:false, items:[$('#itemID').val()], success:successCallback_PriceAndAvailability});
	}
});

function addSpecialInst(){
	var itemId = $('#itemID').val();
	var qtyBox = $('#Qty_' + itemId);
	if(qtyBox.val() !=null && qtyBox.val() != "")
	{
	$instructionlink = $('.special-text');
	$btn = $instructionlink.find('.instructions-link');
	if ($btn.hasClass('gray')) {
		$btn.removeClass('gray');
		$btn.addClass('active-link');
		$btn.addClass('pointer');
	} 
	return false;
	} else {
		$('#instructions-link').addClass("gray");
	}
}

function specialInstBox(){
	var itemId = $('#itemID').val();
	var qtyBox = $('#Qty_' + itemId);
	if(qtyBox.val() !=null && qtyBox.val() != "")
	{
		$("#instructions-content").slideToggle("slow");
	}
}
function successCallback_PriceAndAvailability(data) {
	var html = [];

	var listPriceUnits = $('[id^=listPriceUnit_]');
	var listPriceCosts = $('[id^=listPriceCost_]');
	if (listPriceUnits.length > 0) {

		html.push('		<div class="list-price addpadbottom10">');
		html.push('			<h4>List Price</h4>');
		html.push('			<div class="list-wrap">');
		for (var i = 0, len = listPriceUnits.length; i < len; i++) {
			var unit = listPriceUnits[i].value;
			var cost = listPriceCosts[i].value;
			html.push('			<div class="pa-row">');
			html.push('				<div class="col-1">', unit, ':</div>');
			html.push('				<div class="col-2">', cost, '</div>');
			html.push('			</div>');
		}
		html.push('			</div>'); // end list-wrap
		html.push('		</div>'); // end list-price
	}

	if ($('#isSalesRep').val() == 'true') {
		var item = data.priceAndAvailability.items[0];

		html.push('		<div class="list-price addpadbottom10">');
		html.push('			<h4>');
		html.push('				Cost (' , item.costCurrencyCode, ')');
		html.push('			</h4>');
		html.push('			<div class="cost-wrap">');
		html.push('				<div class="pa-row">');
		html.push('					<div class="col-1">');
		html.push('						<a id="show-hide" onclick="javascript:return showCost(this);" href="#">[Show]</a>');
		html.push('					</div>');
		html.push('					<div id="cost" class="col-2" style="display: none;">');
		var formattedCost = parseFloat(item.itemCost).toFixed(5) + "";
		if (item.itemCost.match(/^[0\.]+$/)) {
			html.push('					$' , formattedCost);
		} else {
			html.push('					$' , formattedCost , ' / ' , item.pricingUOM.substring(2));
		}
		html.push('					</div>');
		html.push('				</div>');
		html.push('			</div>');
		html.push('		</div>'); // list-price
	}

	if (html.length > 0) {
		var itemId = $('#itemID').val();
		$('#availabilty_' + itemId).append(html.join(''));
	}
}

function updateUOMFields() {
	var itemId = $('#itemID').val();
	var uomvalue = $('#itemUomList_' + itemId).val();
	$('#selectedUOM').val(uomvalue);
	$('#uomConvFactor').val($('#convF_' + uomvalue).val());
}

function addItemToCart() {
	var itemId = $('#itemID').val();

	showProcessingIcon();
	var Qty = $('#Qty_' + itemId).val();
	//Quantity validation
	if(Qty =='' || Qty=='0')
	{
		$('#Qty_' + itemId).css('border-color', '#ff0000').focus();

		var errorMsgForQty = $('#errorMsgForQty_' + itemId).get(0);
		errorMsgForQty.innerHTML  = "Please enter a valid quantity and try again.";
		errorMsgForQty.style.display = "inline-block"; 
		errorMsgForQty.setAttribute("class", "error pnaOrderMultipleMessage");
		document.getElementById("Qty_Check_Flag").value = true;
		hideProcessingIcon();
		return;
	}
	var validationSuccess = validateOrderMultiple();

	document.getElementById("Qty_Check_Flag").value = false;

	if(validationSuccess){
		var UOM = $('#itemUomList_' + itemId).val();
		if(document.getElementById("custLineAccNo")!=null) {
			var Job=document.getElementById("custLineAccNo").value;
		}
		if(document.getElementById("Customer")!=null) {
			var customer=document.getElementById("Customer").value;
		}
		var specialInstructions =  document.getElementById('enteredInstructionsText').value;
		var customerPO = "";
		if(document.getElementById("customerPONo") != null && document.getElementById("customerPONo") != undefined) {
			customerPO=document.getElementById("customerPONo").value; 
		}
		listAddToCartItem($('#addToCartURL').val(), itemId, UOM, Qty, Job, customer, customerPO, specialInstructions, '');
	}
} // end addItemToCart

function listAddToCartItem(url, productID, UOM, quantity, Job, customer, customerPO, specialInstructions) {
	var itemId = $('#itemID').val();
	showProcessingIcon();
	var baseUOM;
	
	if (document.getElementById("baseUnitOfMeasure") != null && document.getElementById("baseUnitOfMeasure") != undefined) {
		baseUOM = document.getElementById("baseUnitOfMeasure").value;
	}
	Ext.Ajax.request({
		url: url,
		params: {
			productID: productID,
			productUOM: UOM,
			baseUnitOfMeasure: baseUOM,
			quantity: quantity,
			reqJobId: Job,
			customerPONo: customerPO,
			reqCustomer: customer,
			enteredInstructionsText: specialInstructions
		},
		method: 'GET',
		success: function (response, request) {
			if (response.responseText.indexOf('Sign In</span></a>') != -1 && response.responseText.indexOf('signId') != -1) {
				window.location.reload(true);
				hideProcessingIcon();
				return;
			}

			var draftErr = response.responseText;
			var myMessageDiv = document.getElementById("errorMsgForQty_" + itemId);
			var draftErrDiv = document.getElementById("errorMessageDiv");

			var enteredQty = document.getElementById("Qty_" + itemId).value;
			var uomList = document.getElementById('itemUomList_' + itemId);
			var selectedUom = uomList.options[uomList.selectedIndex].value;
			var selectedUomText = uomList.options[uomList.selectedIndex].text;
			var index = selectedUomText.indexOf("(");
			if (index > -1) {
				selectedUomText = selectedUomText.substring(0, index);
			}

			if (draftErr.indexOf("This cart has already been submitted, please refer to the Order Management page to review the order.") > -1) {
				refreshWithNextOrNewCartInContext();
				draftErrDiv.innerHTML = "<h5 align='center'><b><font color=red>" + response.responseText + "</font></b></h5>";
			} else if (draftErr.indexOf("We were unable to add some items to your cart as there was an invalid quantity in your list. Please correct the qty and try again.") > -1) {
				document.getElementById("Qty_" + itemId).style.borderColor = "#FF0000";
				document.getElementById("Qty_" + itemId).focus();
				document.getElementById("errorMsgForQty_" + itemId).innerHTML = "Please enter a valid quantity and try again.";
				document.getElementById("errorMsgForQty_" + itemId).style.display = "inline-block";
				document.getElementById("errorMsgForQty_" + itemId).setAttribute("class", "error pnaOrderMultipleMessage");
				document.getElementById("Qty_Check_Flag").value = true;
				hideProcessingIcon();
				return;
			} else if (draftErr.indexOf("Exception While Applying cheanges .Order Update was finished before you update") > -1) {
				var orderHeaderKey = document.getElementById("editOrderHeaderKey").value;
				var orderdetailsURL = document.getElementById('orderdetailsURLId').value + '&isErrorMessage=Y&orderHeaderKey=' + orderHeaderKey;
				orderdetailsURL = ReplaceAll(orderdetailsURL, "&amp;", '&');
				window.location = orderdetailsURL;
			} else if (draftErr.indexOf(productID) !== -1) {
				refreshMiniCartLink();
				myMessageDiv.innerHTML = enteredQty + " " + selectedUomText + " has been added to your cart. Please review the cart to update the item with a valid quantity."; //"Item has been added to your cart. Please review the cart to update the item with a valid quantity." ;//add for EB 40
				myMessageDiv.setAttribute("class", "error pnaOrderMultipleMessage");
				myMessageDiv.style.display = "inline-block";

				document.getElementById("Qty_" + itemId).value = "";
				var UOMelement = document.getElementById("itemUomList_" + itemId);
				if (UOMelement != "" && UOMelement != null && defaultUOM != "") {
					UOMelement.value = defaultUOM;
				} else {
					UOMelement.value = uomList.options[0].value;
				}
				updateUOMFields();
			}
			else {
				var selCart = $('#currentCartInContextOHK').val();
				var tag = "";
				var content = "";
				var itemType = document.getElementById("pritemType");

				if (itemType != null && itemType != undefined && itemType.value != '') {

					if (itemType = 'R') {
						tag = "DCSext.w_x_item_repl_ac";
					} else if (itemType = 'A') {
						tag = "DCSext.w_x_item_alt_ac";
					} else if (itemType = 'Cr') {
						tag = "DCSext.w_x_item_crosssell_ac";
					} else if (itemType = 'U') {
						tag = "DCSext.w_x_item_upsell_ac";
					}
					content = "1";
					writeMetaTag(tag, content);
				} else {
					tag = "WT.si_n,WT.tx_cartid,WT.si_x,DCSext.w_x_ord_ac";
					content = "ShoppingCart," + selCart + ",2,1";
					writeMetaTag(tag, content, 4);
				}

				refreshMiniCartLink();
				if (document.getElementById('isEditOrder') != null && document.getElementById('isEditOrder').value != null && document.getElementById('isEditOrder').value != '') {
					myMessageDiv.innerHTML = "Item has been added to order.";
				} else {
					myMessageDiv.innerHTML = enteredQty + " " + selectedUomText + " has been added to your cart.";
				}

				myMessageDiv.style.display = "inline-block";
				myMessageDiv.setAttribute("class", "success pnaOrderMultipleMessage");
				//On successful addition to cart clear the Qty field & restore the default UOM - EB 40
				document.getElementById("Qty_" + itemId).value = "";
				var UOMelement = document.getElementById("itemUomList_" + itemId);
				if (UOMelement != "" && UOMelement != null && defaultUOM != "") {
					UOMelement.value = defaultUOM;
				} else {
					UOMelement.value = uomList.options[0].value;
				}
				updateUOMFields();
			}
			hideProcessingIcon();
		},
		failure: function (response, request) {
			Ext.MessageBox.hide();

			var myMessageDiv = document.getElementById("errorMsgForQty_" + itemId);
			if (document.getElementById('isEditOrder') != null && document.getElementById('isEditOrder').value != null && document.getElementById('isEditOrder').value != '')
				myMessageDiv.innerHTML = "Error in adding item to the order.";
			else
				myMessageDiv.innerHTML = "Error in adding item to the cart.";
			myMessageDiv.style.display = "inline-block";
			myMessageDiv.setAttribute("class", "error pnaOrderMultipleMessage");
			refreshMiniCartLink();
			hideProcessingIcon();
		}
	});
} // end listAddToCartItem

function resetQuantityErrorMessage() {
	var itemId = $('#itemID').val();
	$('#errorMsgForQty_' + itemId).get(0).innerHTML = '';
	$('#Qty_' + itemId).css('border-color', '');
}

var priceCheck;
function validateOrderMultiple() {
	var itemId = $('#itemID').val();
	resetQuantityErrorMessage();
	var Qty = $('#Qty_' + itemId).get(0);
	if (Qty.value == "" || Qty.value == null || Qty.value == '0') {}
	var UOM = $('#itemUomList_' + itemId).get(0);
	var OrdMultiple = document.getElementById("OrderMultiple");
	var uomCF = 1;
	if (OrdMultiple != null && OrdMultiple != undefined && OrdMultiple.value != 1) {
		var selectedUOM = UOM.options[UOM.selectedIndex].text;
		var selectedUOMQty = selectedUOM.split(" ");
		if (selectedUOMQty.length == 2) {
			var splitUOMQty = selectedUOMQty[1];
			var splitUOMQty1 = splitUOMQty.split("(");
			var splitUOMQty2 = splitUOMQty1[1];
			splitUOMQty2 = splitUOMQty2.split(")");
			var uomConvFactor = splitUOMQty2[0];
			if (uomConvFactor != null && uomConvFactor != undefined) {
				uomCF = uomConvFactor;
			}
		} else {
			var selectedUOM = document.getElementById("selectedUOM");
			var uomConvFactor = document.getElementById("uomConvFactor");
			if (uomConvFactor != null && uomConvFactor != undefined) {
				uomCF = uomConvFactor.value;
			}
		}
	} else {
		var selectedUOM = document.getElementById("selectedUOM");
		var uomConvFactor = document.getElementById("uomConvFactor");
		if (uomConvFactor != null && uomConvFactor != undefined) {
			uomCF = uomConvFactor.value;
		}
	}

	var totalQty = uomCF * Qty.value;
	if (OrdMultiple != null && OrdMultiple != undefined && OrdMultiple.value != 0) {
		var ordMul = totalQty % OrdMultiple.value;
		var myMessageDiv = $('#errorMsgForQty_' + itemId).get(0);
		if (OrdMultiple.value > 1) {
			if (priceCheck == true) {
				myMessageDiv.innerHTML = "Must be ordered in units of " + addComma(OrdMultiple.value) + " " + $('#baseUOMDesc').val();
				myMessageDiv.style.display = "inline-block";
				myMessageDiv.setAttribute("class", "notice pnaOrderMultipleMessage");
			} else {
				myMessageDiv.innerHTML = "";
			}
		}
	}
	return true;
}

function qtyInputCheck(component) {
	var itemId = $('#itemID').val();
	var qtyCheckFlag = $('#Qty_Check_Flag').val();
	if (qtyCheckFlag == "true") {
		if (component.value == "") {
			component.style.borderColor = "#FF0000";
			$('#errorMsgForQty_' + itemId).css('display', 'inline-block');
		} else {
			component.style.borderColor = "";
		}
	}
}

function goBack() {
	var backPageUrl = $('#backPageUrl').val();
	if (backPageUrl) {
		window.location.href = backPageUrl;
	} else {
		window.history.back();
	}
}

function addItemToWishList() {
	$("#dlgAddToListLink").trigger('click');
}

function showPrivateList(){
	//Populate fields
	var divMainId 	= "divMainPrivateList";
	var divMain 	= document.getElementById(divMainId);
	getMyItemsList(divMainId);
}

function getMyItemsList(divId) {
	//Init vars
	var url = $('#getMyItemsListURL').val() + '&ShareList=ShareList';

	document.getElementById(divId).innerHTML = "Loading data... please wait!";

	//Execute the call
	Ext.Ajax.request({
		url: url,
		params: {
			filterByMyListChk: true,
			//Parameter to fetch the private lists in the form of radio buttons
			displayAsRadioButton: true,
		},
		method: 'POST',
		success: function (response, request){
			//Added for EB 560
			if(response.responseText.indexOf('List Img') != -1)
			{
				var addItemListButtonVar = document.getElementById("addItemListButton");
				if(addItemListButtonVar != null && addItemListButtonVar != undefined)
				{
					addItemListButtonVar.style.display="block";
				}
			}

			if(response.responseText.indexOf('Sign In</span></a>') != -1 && response.responseText.indexOf('signId') != -1){
				window.location.reload(true);
				return;
			}
			//End of EB 560
			setAndExecute(divId, response.responseText);
		},
		failure: function (response, request){
			var x = document.getElementById(divId);
			x.innerHTML = "";
			alert('Failed to fecth the my items list');
			document.body.style.cursor = 'default';                                                  
		}
	});     
	document.body.style.cursor = 'default';
} // end getMyItemsList

function addItemsToList() {
	var itemId = $('#itemID').val();
	var idx = currentAadd2ItemListIndex;

	//if idx is not set, no list is selected
	if (idx != null) {
		showProcessingIcon();

		var quantityValue = Ext.util.Format.trim(Ext.get("Qty_" + itemId).dom.value);

		document.OrderDetailsForm.orderLineItemNames.value = unescape(document.OrderDetailsForm.orderLineItemNames.value);
		document.OrderDetailsForm.orderLineItemDesc.value = unescape(document.OrderDetailsForm.orderLineItemDesc.value);

		if (document.getElementById("itemUomList_" + itemId) == null) {
			document.OrderDetailsForm.itemUOMs.value = "EACH";
		} else {
			document.OrderDetailsForm.itemUOMs.value = document.getElementById("itemUomList_" + itemId).value;
		}

		if (document.getElementById("Qty_" + itemId)) {
			document.OrderDetailsForm.orderLineQuantities.value = quantityValue;
		}

		//Job#
		if (document.getElementById("custLineAccNo") != null) {
			document.OrderDetailsForm.orderLineCustLineAccNo.value = document.getElementById("custLineAccNo").value;
		}

		if (document.getElementById("customerPONo") != null) {
			document.OrderDetailsForm.customerLinePONo.value = document.getElementById("customerPONo").value;
		}
		document.getElementById("listKey").value = currentAadd2ItemList.value;

		var url = $('#addItemURL').val();

		var itemCountValOfSelList = document.getElementById("itemCount_" + currentAadd2ItemList.value);
		document.OrderDetailsForm.orderLineItemOrders.value = Number(itemCountValOfSelList.value) + 1;
		if (itemCountValOfSelList.value < 200) {
			document.body.style.cursor = 'wait';
			var orderDetailsForm = Ext.get("OrderDetailsForm");
			orderDetailsForm.dom.listKey.value = currentAadd2ItemList.value;
			var itemId = $('#itemID').val();
			Ext.Ajax.request({
				url: url,
				form: 'OrderDetailsForm',
				method: 'POST',
				success: function (response, request) {
					document.body.style.cursor = 'default';
					// Removal of MIL dropdown list from header for performance improvement
					itemCountValOfSelList.value = itemCountValOfSelList.value + 1;

					var myMessageDiv = document.getElementById("errorMsgForQty_" + itemId);
					myMessageDiv.innerHTML = "Item has been added to the selected list.";
					myMessageDiv.style.display = "inline-block";
					myMessageDiv.setAttribute("class", "success pnaOrderMultipleMessage");
					hideProcessingIcon();
					/*Web Trends tag start*/
					writeMetaTag("DCSext.w_x_list_additem", "1");
					/*Web Trends tag end*/
				},
				failure: function (response, request) {
					document.body.style.cursor = 'default';
					var myMessageDiv = document.getElementById("errorMsgForQty_" + itemId);
					myMessageDiv.innerHTML = "No lists have been created. Please create new list.";
					myMessageDiv.style.display = "inline-block";
					myMessageDiv.setAttribute("class", "notice pnaOrderMultipleMessage");
					hideProcessingIcon();
				}
			});
		} else {
			alert("Maximum number of element in a list can only be 200..\n Please try again with removing some items or create a new list.");
		}

		document.body.style.cursor = 'default';
		$.fancybox.close();
	} else {
		hideProcessingIcon();
		alert('Please select a list or create a new list.');

		document.body.style.cursor = 'default';
		return;
	}
} // end addItemsToList

function submitNewlistAddItem(xForm) {
	var itemId = $('#itemID').val();
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

	for (i = 0; i < form.dom.elements.length; i++){
		var item = form.dom.elements[i];
		if (item.name != ""){
			dataParams[item.name] = item.value;
		}
	}
	try{ console.debug("params" , dataParams); }catch(e){}

	//adding the already seleted as hidden checkboxes to the form 
	createHiddenInputsForSelectedCustomers(xForm);
	clearTheArrays();// clearing the arrays

	//Submit the data via ajax

	//Init vars
	var url = $('#myItemsDetailsChangeShareListURL').val();

	document.body.style.cursor = 'wait';

	addItemToListnew(form);
	$.fancybox.close();

	Ext.Ajax.request({
		url: url,
		form: xForm,
		method: 'POST',
		success: function (response, request){
			document.body.style.cursor = 'wait';	    	
			var myMessageDiv = document.getElementById("errorMsgForQty_" + itemId);	            
			myMessageDiv.innerHTML = "Item has been added to the selected list." ;	            
			myMessageDiv.style.display = "inline-block"; 
			myMessageDiv.setAttribute("class", "success pnaOrderMultipleMessage");	           
			/*Web Trends tag start*/ 
			writeMetaTag("DCSext.w_x_list_additem","1");
			/*Web Trends tag end*/
			document.body.style.cursor = 'default';

		},
		failure: function (response, request){
			document.body.style.cursor = 'default';
			Ext.Msg.hide();
		}
	});
	document.body.style.cursor = 'default';
}

function addItemToListnew(form){
	var itemId = $('#itemID').val();

	var idx = currentAadd2ItemListIndex;

	var quantityValue = Ext.util.Format.trim(Ext.get('Qty_' + itemId).dom.value);	    			
	form.dom.orderLineItemNames.value 	= document.OrderDetailsForm.orderLineItemNames.value;
	form.dom.orderLineItemDesc.value 	= document.OrderDetailsForm.orderLineItemDesc.value;

	if (document.getElementById("itemUomList_" + itemId) == null){
		form.dom.itemUOMs.value = "EACH";
	} else {
		form.dom.itemUOMs.value 	= document.getElementById("itemUomList_" + itemId).value;
	}

	if (quantityValue){
		form.dom.orderLineQuantities.value = quantityValue;
	}

	//Job#
	if (document.getElementById("custLineAccNo")!=null){
		form.dom.orderLineCustLineAccNo.value = document.getElementById("custLineAccNo").value;
	}

	if(document.getElementById("customerPONo")!=null) {
		form.dom.customerLinePONo.value=document.getElementById("customerPONo").value;
	}

	var itemCountValOfSelList = "0";

	form.dom.orderLineItemOrders.value = Number(itemCountValOfSelList.value) + 1;

	form.dom.orderLineItemIDs.value = itemId;
	form.dom.fromItemDetail.value = true;
}

$(document).ready(function() {
	
	$("#registerId").click(function(){
		var url = $('#myregisterId').val();
		window.location.href = url;
		return false;
	});
});

