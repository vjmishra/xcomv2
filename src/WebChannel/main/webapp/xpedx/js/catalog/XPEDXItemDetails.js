var defaultUOM;

$(document).ready(function() {
	$('#qtyBox').focus();
});

$(document).ready(function() {
	updateUOMFields();
	var requestedUom = $('#selectedUOM').val();
	var baseUom = $('#unitOfMeasure').val();
	defaultUOM = $('#selectedUOM').val();

	callPnA(baseUom);
	//getPriceAndAvailabilityForItems([$('#itemID').val()]);
});

function callPnA(requestedUom) {
	var itemId = $('#itemID').val();
	var Quantity = $('#qtyBox').val();
	var baseUom = $('#unitOfMeasure').val();
	var prodMweight = $('#prodMweight').val();
	var pricingUOMConvFactor = $('#pricingUOMConvFactor').val();
	pandaByAjax(itemId, requestedUom, Quantity, baseUom, prodMweight, pricingUOMConvFactor);
}

function callPnAfromLink(requestedUom) { // TODO why do we have separate implementation for 'FromLink'
	var itemId = $('#itemID').val();
	var Quantity = $('#qtyBox').val();
	var baseUom = $('#unitOfMeasure').val();
	var prodMweight = $('#prodMweight').val();
	var pricingUOMConvFactor = $('#pricingUOMConvFactor').val();
	pandaByAjaxFromLink(itemId, requestedUom, Quantity, baseUom, prodMweight, pricingUOMConvFactor);
}

function updateUOMFields() {
	var uomvalue = $('#itemUOMsSelect').val();
	$('#selectedUOM').val(uomvalue);
	$('#uomConvFactor').val($('#convF_' + uomvalue).val());
}
var myMask;

function updatePandAfromLink() {
	/*Web Trends tag start*/
	if (document.getElementById("webtrendItemType") != null) {
		var itemType = document.getElementById("webtrendItemType").value;
		writeMetaTag("DCSext.w_x_itemtype", itemType);
	}
	var errorMessageDiv = document.getElementById("errorMessageDiv");
	if (errorMessageDiv != null && errorMessageDiv != undefined)
		errorMessageDiv.innerHTML = '';

	var waitMsg = Ext.Msg.wait("Processing...");
	myMask = new Ext.LoadMask(Ext.getBody(), {
		msg: waitMsg
	});
	myMask.show();
	try {

		var UOMelement = document.getElementById("itemUOMsSelect");
		var uomvalue = UOMelement.options[UOMelement.selectedIndex].value;
		callPnAfromLink(uomvalue);
	} catch (err) {
		var errorMessageDiv = document.getElementById("errorMessageDiv");
		if (errorMessageDiv != null && errorMessageDiv != undefined)
			errorMessageDiv.innerHTML = '<h5 align="center"><b><font color="red">Could not get the pricing details for this Particular Item at the moment. Please try again Later</font></b></h5>';
		Ext.Msg.hide();
		myMask.hide();
	}
}

function pandaByAjaxFromLink(itemId, reqUom, Qty, baseUom, prodMweight, pricingUOMConvFactor, isOrderData) {
	if (isOrderData == undefined || isOrderData == null) {
		isOrderData = 'false';
	}

	if (itemId == null || itemId == "null" || itemId == "") {
		Ext.Msg.hide();
		myMask.hide();
		return;
	}
	if (reqUom == null || reqUom == "null" || reqUom == "") {
		reqUom = $('#selectedUOM').val();
	}
	var Qty = $('#qtyBox').val();
	var qtyTextBox = Qty;

	if (Qty == '') {
		var uomConvFactor;
		var orderMul = $('#OrderMultiple').val();
		var conversionFactor = $('#uomConvFactor').val();
		if (Qty == null || Qty == "null" || Qty == "") {
			reqUom = $('#selectedUOM').val();
			if (orderMul != null && orderMul.value != 0 && uomConvFactor != 0 && conversionFactor != null) {
				if (uomConvFactor == 1) {
					Qty = orderMul;
				} else if (uomConvFactor <= orderMul.value) {
					if ((orderMul.value % uomConvFactor) == 0) {
						Qty = orderMul.value / uomConvFactor;
					} else {
						Qty = 1;
					}
				} else { //if conversionFactor is greater than OrderMul irrespective of the moduloOf(conversionFactor,OrderMul) is a whole number / decimal result we set the Qty to 1
					Qty = 1;
				}
			}
		}
	}
	var itemAvailDiv = document.getElementById("tabs-1");
	if (Qty == '0') {

		document.getElementById("qtyBox").style.borderColor = "#FF0000";
		document.getElementById("qtyBox").focus();
		document.getElementById("errorMsgForQty").innerHTML = "Please enter a valid quantity and try again.";
		document.getElementById("errorMsgForQty").style.display = "inline-block";
		document.getElementById("errorMsgForQty").setAttribute("class", "error");
		document.getElementById("Qty_Check_Flag").value = true;
		document.getElementById("qtyBox").value = "";
		itemAvailDiv.style.display = "none";
		Ext.Msg.hide();
		myMask.hide();
		return;
	}
	priceCheck = true;
	var Category = $('#catagory').val();
	var customerUom = $('#custUOM').val()
	var url = $('#xpedxItemDetailsPandAURLid').val();
	var validationSuccess = validateOrderMultiple();
	if (validationSuccess == false) {
		Ext.Msg.hide();
		myMask.hide();
		return;
	} else {
		Ext.Ajax.request({
			url: url,
			params: {
				itemID: itemId,
				requestedUOM: reqUom,
				pnaRequestedQty: Qty,
				qtyTextBox: qtyTextBox,
				baseUOM: baseUom,
				prodMweight: prodMweight,
				pricingUOMConvFactor: pricingUOMConvFactor,
				validateOrderMul: validationSuccess,
				Category: Category,
				isOrderData: isOrderData,
				customerUOM: customerUom
			},
			success: function (response, request) {
				if (response.responseText.indexOf('Sign In</span></a>') != -1 && response.responseText.indexOf('signId') != -1) {
					window.location.reload(true);
					Ext.Msg.hide();
					myMask.hide();
					return;
				}
				document.getElementById("priceAndAvailabilityAjax").innerHTML = response.responseText;
				Ext.Msg.hide();
				myMask.hide();
				//-- Web Trends tag start --
				var responseText = response.responseText;
				writeWebtrendTag(responseText);
				//-- Web Trends tag end --
			},
			failure: function (response, request) {
				Ext.Msg.hide();
				myMask.hide();
				var errorMessageDiv = document.getElementById("errorMessageDiv");
				if (errorMessageDiv != null && errorMessageDiv != undefined)
					errorMessageDiv.innerHTML = '<h5 align="center"><b><font color="red">Could not get the pricing details for this Particular Item at the moment. Please try again Later</font></b></h5>';
			}
		});
	}
}

function pandaByAjax(itemId, reqUom, Qty, baseUom, prodMweight, pricingUOMConvFactor) {
	if (itemId == null || itemId == "null" || itemId == "") {
		return;
	}
	if (reqUom == null || reqUom == "null" || reqUom == "") {
		reqUom = $('#selectedUOM').val();
	}
	// added for  Jira 2101 to get the PnA results based on the selected UOM on page load
	var uomConvFactor;
	var orderMul = $('#OrderMultiple').val();
	var conversionFactor = document.getElementById("uomConvFactor");
	if (conversionFactor != null && conversionFactor != undefined) {
		uomConvFactor = document.getElementById("uomConvFactor").value;
	}
	var qtyTextBox = Qty;
	if (Qty == null || Qty == "null" || Qty == "") {
		reqUom = $('#selectedUOM').val();
		if (orderMul != null && orderMul.value != 0 && uomConvFactor != 0 && conversionFactor != null) {
			if (uomConvFactor == 1) {
				Qty = orderMul;
			} else if (uomConvFactor <= orderMul.value) {
				if ((orderMul.value % uomConvFactor) == 0) {
					Qty = orderMul.value / uomConvFactor;
				} else {
					Qty = 1;
				}
			} else { //if conversionFactor is greater than OrderMul irrespective of the moduloOf(conversionFactor,OrderMul) is a whole number / decimal result we set the Qty to 1
				Qty = 1;
			}
		}
	}
	priceCheck = true;
	var Category = $('#catagory').val();
	var customerUom = $('#custUOM').val()
	var url = $('#xpedxItemDetailsPandAURLid').val();
	var validationSuccess = validateOrderMultiple();
	Ext.Ajax.request({
		url: url,
		params: {
			itemID: itemId,
			requestedUOM: reqUom,
			pnaRequestedQty: Qty,
			qtyTextBox: qtyTextBox,
			baseUOM: baseUom,
			prodMweight: prodMweight,
			pricingUOMConvFactor: pricingUOMConvFactor,
			validateOrderMul: validationSuccess,
			Category: Category,
			customerUOM: customerUom
		},
		success: function (response, request) {
			document.getElementById("priceAndAvailabilityAjax").innerHTML = response.responseText;
			Ext.Msg.hide();
			//-- Web Trends tag start --
			var responseText = response.responseText;
			writeWebtrendTag(responseText);
			//-- Web Trends tag end --
		}
	});
}

function resetQuantityErrorMessage() {
	var divId = 'errorMsgForQty';
	var divVal = document.getElementById(divId);
	divVal.innerHTML = '';
	document.getElementById("qtyBox").style.bordercolor = "";
}

function validateOrderMultiple() {
	resetQuantityErrorMessage();
	var Qty = document.getElementById("qtyBox");
	if (Qty.value == "" || Qty.value == null || Qty.value == '0') {}
	var UOM = document.getElementById("itemUOMsSelect");
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
