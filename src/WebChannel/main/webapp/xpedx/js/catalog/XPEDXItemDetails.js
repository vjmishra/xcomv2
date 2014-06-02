var defaultUOM;

$(document).ready(function() {
	$('[id^=Qty_]').focus();
});

$(document).ready(function() {
	// init carousel
	$('.liquid-carousel').liquidcarousel({height:170, duration:700, hidearrows:false});
	$('.associated-items-wrap').show();
});

$(document).ready(function() {
	// get price and availability on load
	updateUOMFields();
	var requestedUom = $('#selectedUOM').val();
	var baseUom = $('#unitOfMeasure').val();
	defaultUOM = $('#selectedUOM').val();
	
	getPriceAndAvailabilityForItems({modal:false, items:[$('#itemID').val()], success:successCallback_PriceAndAvailability});
});

function successCallback_PriceAndAvailability(data) {
	if ($('#isSalesRep').val() == 'true') {
		var item = data.priceAndAvailability.items[0];
		
		var html = [];
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
	
		var itemId = $('#itemID').val();
		$('#availabilty_' + itemId).append(html.join(''));
	}
}

function updateUOMFields() {
	var uomvalue = $('[id^=itemUomList_]').val();
	$('#selectedUOM').val(uomvalue);
	$('#uomConvFactor').val($('#convF_' + uomvalue).val());
}

var myMask;
function addItemToCart()
{	//added for jira 3974
	var waitMsg = Ext.Msg.wait("Processing...");
	myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
	myMask.show();
	var Qty = $('[id^=Qty_]').val();
	//Quantity validation
	if(Qty =='' || Qty=='0')
	{
		$('[id^=Qty_]').css('border-color', '#ff0000').focus();
		
		var errorMsgForQty = $('[id^=errorMsgForQty_]').get(0);
		errorMsgForQty.innerHTML  = "Please enter a valid quantity and try again.";
  		errorMsgForQty.style.display = "inline-block"; 
  		errorMsgForQty.setAttribute("class", "error");
		document.getElementById("Qty_Check_Flag").value = true;
		Ext.Msg.hide();
	    	myMask.hide();
	    return;
	}
	var validationSuccess = validateOrderMultiple();
	
	document.getElementById("Qty_Check_Flag").value = false;

	if(validationSuccess){
		var ItemId=document.getElementById("itemId").value;
		var UOM = $('[id^=itemUomList_]').val();
		if(document.getElementById("Job")!=null) {
			var Job=document.getElementById("Job").value;
		}
		if(document.getElementById("Customer")!=null) {
			var customer=document.getElementById("Customer").value;
		}
		
		var customerPO = "";
		if(document.getElementById("customerPONo") != null && document.getElementById("customerPONo") != undefined) {
			customerPO=document.getElementById("customerPONo").value; 
		}
		listAddToCartItem($('#addToCartURL'), ItemId, UOM, Qty, Job, customer, customerPO, '');
	}
}

function resetQuantityErrorMessage() {
	var divId = 'errorMsgForQty';
	var divVal = document.getElementById(divId);
	divVal.innerHTML = '';
	$('[id^=Qty_]').css('border-color', '');
}

function validateOrderMultiple() {
	resetQuantityErrorMessage();
	var Qty = $('[id^=Qty_]').get(0);
	if (Qty.value == "" || Qty.value == null || Qty.value == '0') {}
	var UOM = $('[id^=itemUomList_]').get(0);
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
		var myMessageDiv = document.getElementById("errorMsgForQty");
		if (OrdMultiple.value > 1) {
			if (priceCheck == true) {
				myMessageDiv.innerHTML = "Must be ordered in units of " + addComma(OrdMultiple.value) + " " + $('#baseUOMDesc').val();
				myMessageDiv.style.display = "inline-block";
				myMessageDiv.setAttribute("class", "notice");
			} else {
				myMessageDiv.innerHTML = "";
			}
		}
	}
	return true;
}

function qtyInputCheck(component) {
	var qtyCheckFlag = $('#Qty_Check_Flag').val();
	if (qtyCheckFlag == "true") {
		if (component.value == "") {
			component.style.borderColor = "#FF0000";
			document.getElementById('errorMsgForQty').style.display = "inline-block";
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
