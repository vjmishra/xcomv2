/*
 * This method is a copy of XPEDXDraftOrderDetails.js/addProductToQuickAddList with quick hacks for eb-1999, which was a quick hack for performance.
 * I did NOT remove that function since it may have broken other pages.
 * When this page is fully rewritten as part of the larger epic (eb-4366) then this should be cleaned up.
 */
function quickAdd_addProductToQuickAddList(element) {
	var theForm = element.form;
	
	var sku = trim(theForm.qaProductID.value);
	var quantity = trim(theForm.qaQuantity.value);
	var jobId = "";
	if (theForm.qaJobID != null) {
		jobId = trim(theForm.qaJobID.value);
		jobidFlag = true;
	}
	var itemType = trim(theForm.qaItemType.value);
	var itemTypeText = itemType;

	var itemTypeSelElem = theForm.qaItemType;
	if (itemTypeSelElem != null) {
		itemType = itemTypeSelElem.options[itemTypeSelElem.selectedIndex].value;
		itemTypeText = itemTypeSelElem.options[itemTypeSelElem.selectedIndex].text;
	}
	
	var uomArray = new Array();
	var CustomUomArray = new Array();
	var purchaseOrder = "";
	if (theForm.purchaseOrder != null) {
		purchaseOrder = theForm.purchaseOrder.value;
		custPOFlag = true;
	}
	if (sku == "") {
		setErrorMessage('Please enter a valid Item #.')
		return;
	} else {
		clearErrorMessage();
	}
	if (quantity == "") {
		quantity = 1;
		theForm.qaQuantity.value = quantity;
	}
	
	document.getElementById("validationActionNameQA").value = "draftOrderAddOrderLines";
	if (swc_validateForm("QuickAddForm") == false) {
		theForm.qaQuantity.focus();
		return;
	}
	
	QuickAddElems[QuickAddElems.length] = {
		sku: sku,
		quantity: quantity,
		jobId: jobId,
		purchaseOrder: purchaseOrder,
		itemType: itemType,
		itemTypeText: itemTypeText,
		itemDesc: "",
		uom: "",
		uomList: uomArray,
		isValidated: "false",
		isEntitled: "true",
		orderMultiple: "",
		itemUomAndConvString: "",
		//Added selectedUOM for Jira 3862
		selectedUOM: "",
		customUOM: CustomUomArray
	};
	
	theForm.qaProductID.value = "";
	theForm.qaQuantity.value = "";
	if (theForm.qaJobID != null) {
		theForm.qaJobID.value = "";
	}
	if (theForm.purchaseOrder != null) {
		theForm.purchaseOrder.value = "";
	}
	
	// Kludge to get localized string from the form for use in the HTML
	// generated by quickAdd_redrawQuickAddList. It counts on the fact that the
	// first time quickAdd_redrawQuickAddList will be called on the page is for
	// the add. If it were to be called beforehand, there would be no
	// deleteStringFromForm set.
	if (deleteStringFromForm == "") {
		deleteStringFromForm = theForm.localizedDeleteLabel.value;
	}
	if (addStringFromForm == "") {
		addStringFromForm = theForm.localizedAddToCartLabel.value;
	}
	quickAdd_redrawQuickAddList(element);
	quickAdd_validateItems();
	theForm.qaProductID.focus();

	return false;
} // end function quickAdd_addProductToQuickAddList


/*
 * This method is a copy of XPEDXDraftOrderDetails.js/validateItems with quick hacks for eb-1999, which was a quick hack for performance.
 * I did NOT remove that function since it may have broken other pages.
 * When this page is fully rewritten as part of the larger epic (eb-4366) then this should be cleaned up.
 */
function quickAdd_validateItems() {
	var url = document.getElementById('productValidateURL');
	var itemsString = "";
	var itemTypesString = "";
	var itemsValidationIndex = new Array();
	var len = QuickAddElems.length;
	for (var index = 0; index < len; index++) {
		var itemId = QuickAddElems[index].sku;
		var itemType = QuickAddElems[index].itemType;
		var isItemValidated = QuickAddElems[index].isValidated;

		if (isItemValidated == "false") {
			itemsString = itemsString + itemId + "*";
			itemTypesString = itemTypesString + itemType + "*";
			itemsValidationIndex.push(index);
		}
	}

	if (itemsString != "") {
		itemsString = itemsString.substr(0, (itemsString.length - 1));
		itemTypesString = itemTypesString.substr(0, (itemTypesString.length - 1));

		Ext.Ajax.request({
			url: url,
			params: {
				itemList: itemsString,
				itemTypeList: itemTypesString
			}, 
			success: function (response, request) {
				var res = response.responseText;
				var itemValid = res.split(",");
				var noOfItems = itemsValidationIndex.length;
				for (var index = 0; index < noOfItems; index++) {
					var itemValidandUomList = trim(itemValid[index]).split("*");
					var itemEntitled = trim(itemValidandUomList[0]);
					var itemUOMArray = new Array();
					var itemUOMConvArray = new Array();
					var itemUomAndConvString = null;
					var itemUOMCodeArray = new Array();
					var itemCustomUOMArray = new Array();
					if (itemEntitled == "true") {
						var itemUomList = trim(itemValidandUomList[1]).split("!");
						for (var uomIndex = 0; uomIndex < itemUomList.length; uomIndex++) {
							var itemUOMConvFactor = trim(itemUomList[uomIndex]).split(":");
							var itemUOMAndCustomUomInfo = trim(itemUOMConvFactor[0]).split("-");

							var itemUOM = trim(itemUOMAndCustomUomInfo[0]);
							itemUOMCodeArray[uomIndex] = itemUOM;

							var itemCustomUOM = trim(itemUOMAndCustomUomInfo[1]);
							itemCustomUOMArray[uomIndex] = itemCustomUOM;

							var itemUomConvfAndMultiple = itemUOMConvFactor[1].split("|");
							var itemUomConvf = trim(itemUomConvfAndMultiple[0]);
							var itemOrderMultiple = trim(itemUomConvfAndMultiple[1]);
							itemUomAndConvString = itemUomAndConvString + "|" + itemUOM + "-" + itemUomConvf;
							if (itemUomConvf == "1") {
								itemUOMArray = itemUOMArray.concat(itemUOM);
							} else {
								itemUOMArray = itemUOMArray.concat(itemUOM + " (" + itemUomConvf + ")");
							}
							itemUOMConvArray = itemUOMConvArray.concat(itemUomConvf);
						}
					}
					var QAindex = itemsValidationIndex[index];
					var entitled = itemEntitled;
					QuickAddElems[QAindex].isEntitled = entitled;
					QuickAddElems[QAindex].isValidated = "true";
					QuickAddElems[QAindex].uomList = itemUOMArray;
					QuickAddElems[QAindex].uomCodes = itemUOMCodeArray;
					QuickAddElems[QAindex].orderMultiple = itemOrderMultiple;
					QuickAddElems[QAindex].itemUomAndConvString = itemUomAndConvString;
					QuickAddElems[QAindex].customUOM = itemCustomUOMArray;
					if (itemUOMArray.length > 0) {
						var firstIndex = itemUOMArray[0].indexOf('(');
						if (firstIndex != -1) {
							QuickAddElems[QAindex].uom = itemUOMArray[0].substring(0, firstIndex);
						} else {
							QuickAddElems[QAindex].uom = itemUOMArray[0];
						}
					}
				}
				quickAdd_redrawQuickAddList();
			},
			failure: function (response, request) {
				var noOfItems = QuickAddElems.length;
				for (var index = 0; index < noOfItems; index++) {
					var isItemValidated = QuickAddElems[index].isValidated;
					if (isItemValidated == "false") {
						alert('Failed to validate item ' + QuickAddElems[index].sku + '. Please try again later.');
						removeProductFromQuickAddList(index);
					}
				}
			}
		});
	}
}


/*
 * This method is a copy of XPEDXDraftOrderDetails.js/addProductsToOrder with quick hacks for eb-1999, which was a quick hack for performance.
 * I did NOT remove that function since it may have broken other pages.
 * When this page is fully rewritten as part of the larger epic (eb-4366) then this should be cleaned up.
 */
function quickAdd_addProductsToOrder() {
	if (QuickAddElems.length > 0) {
		var enteredQuants;
		var enteredUoms;
		var enteredUomsConFact = new Array();
		var availUomsConFact = new Array();
		var baseUOM = new Array();
		var entereditems;
		var uomConvFac;
		var selectedUomConvFac;
		var selectedUomConvFacFromStr;
		var selectedUomFromStr;
		var orderMultiple; // only one..refine to set it only once.
		var isError = false;
		var noError = true;
		var selectedUOM = new Array();
		selectedUOM = document.getElementsByName("enteredUOMs");
		baseUOM = document.getElementsByName("quickAddBaseUOMs");
		for (var i = 0; i < QuickAddElems.length; i++) {
			noError = true;
			orderMultiple = encodeForHTML(QuickAddElems[i].orderMultiple);
			if (orderMultiple != undefined && orderMultiple > 1 && orderMultiple.replace(/^\s*|\s*$/g, "") != '' && orderMultiple != null && orderMultiple != 0) {
				var enteredUOM = selectedUOM[i].value;
				enteredUOM = enteredUOM.split(" ");
				enteredQuants = encodeForHTML(QuickAddElems[i].quantity);
				entereditems = encodeForHTML(QuickAddElems[i].sku);
				enteredUoms = enteredUOM[0];
				uomConvFac = encodeForHTML(QuickAddElems[i].itemUomAndConvString);
			} else {
				enteredQuants = encodeForHTML(QuickAddElems[i].quantity);
				entereditems = encodeForHTML(QuickAddElems[i].sku);
				enteredUoms = encodeForHTML(QuickAddElems[i].uom);
				uomConvFac = encodeForHTML(QuickAddElems[i].itemUomAndConvString);
			}
			if (enteredUoms) {
				if (uomConvFac != null) enteredUomsConFact = uomConvFac.split("|");
				for (var j = 0; j < enteredUomsConFact.length; j++) {
					availUomsConFact = enteredUomsConFact[j].split("-");
					selectedUomConvFacFromStr = availUomsConFact[1];
					selectedUomFromStr = availUomsConFact[0];
					if (enteredUoms.trim() == selectedUomFromStr.trim()) {
						selectedUomConvFac = selectedUomConvFacFromStr;
						break;
					}
				}
				enteredQuants = ReplaceAll(enteredQuants, ",", "");
				
				var divId = "errorQty_" + entereditems + i;
				var divError = document.getElementById(divId);
				if (selectedUomConvFac != undefined && selectedUomConvFac != null) {
					if (orderMultiple == undefined || orderMultiple.replace(/^\s*|\s*$/g, "") == '' || orderMultiple == null || orderMultiple == 0) {
						orderMultiple = 1;
					}
					var totalQty = selectedUomConvFac * enteredQuants;
					var ordMul = totalQty % orderMultiple;

					if (enteredQuants == '' || enteredQuants == '0') {
						divError.innerHTML = 'Please enter a valid quantity and try again.';
						divError.style.display = "inline-block";
						divError.style.marginLeft = "20px";
						divError.setAttribute("class", "error");
						divError.setAttribute("align", "center");
						isError = true;
						noError = false;
					}
					if (orderMultiple > 1 && noError == true) {
						divError.innerHTML = "Must be ordered in units of " + orderMultiple + " " + baseUOM[i].value;
						divError.style.display = "inline-block";
						divError.setAttribute("class", "notice qa-standalone-notice");
						divError.setAttribute("align", "center");
					}
				}
			} else {
				continue;
			}
		}
		if (!isError) { // no error, then submit to add the products to the cart
			for (var i = 0; i < QuickAddElems.length; i++) {
				var orderMultiple1 = encodeForHTML(QuickAddElems[i].orderMultiple);
				if (orderMultiple1 == undefined || orderMultiple1.replace(/^\s*|\s*$/g, "") == '' || orderMultiple1 == null || orderMultiple1 == 0) {
					orderMultiple1 = 1;
				}

				createHiddenField("QuickAddForm", "quickAddOrderMultiple", orderMultiple1);
			}
			document.QuickAddForm.action = document.getElementById('addProductsToOrderURL');
			var form = Ext.get("QuickAddForm");
			addCSRFToken(form.dom, 'form');
			form.dom.submit();
		} else {
			return false;
		}
	}
} // end function quickAdd_addProductsToOrder


/*
 * This method is a copy of XPEDXDraftOrderDetails.js/quickAdd_redrawQuickAddList with quick hacks for eb-1999, which was a quick hack for performance.
 * I did NOT remove that function since it may have broken other pages.
 * When this page is fully rewritten as part of the larger epic (eb-4366) then this should be cleaned up.
 */
function quickAdd_redrawQuickAddList() {
	var tabIndex = 210;
	var showAddtoCartBtn = false;
	var code = '<table cellspacing="0" cellpadding="0" id="QuickAddTable" width="96%">';
	var jobValue;
	var customerPONoValue;
	addToCartDiv = document.getElementById("addProdsToOrder");
	addToCartDiv.style.display = 'none';
	if (document.QuickAddForm.jobIdValue != undefined && document.QuickAddForm.jobIdValue.value != null)
		jobValue = document.QuickAddForm.jobIdValue.value;
	if (document.QuickAddForm.customerPONoValue != undefined && document.QuickAddForm.customerPONoValue.value != null) {
		customerPONoValue = document.QuickAddForm.customerPONoValue.value;
	}

	if (QuickAddElems.length > 0) {
		code += '<thead><tr>';
		code += '<th width="18" class="del-col">&nbsp;</th>';
		code += '<th width="176" class="first-col-header col-header type-col">Item Type</th>';
		code += '<th width="59" class="col-header item-col">Item #</th>';
		code += '<th width="87" class="col-header qty-col">Qty</th>';
		code += '<th width="141" class="col-header uom-col">UOM</th>';

		if (custPOFlag == true && customerPONoValue != null) {
			code += '<th width="241" class="last-col-header col-header job-col">' + customerPONoValue + '</th>';
		}
		if (jobidFlag == true && jobValue != null) {
			code += '<th width="176" class="last-col-header col-header job-col">' + jobValue + '</th>';
		}

		code += '</tr></thead>';
		code += '<tbody>';
	}

	for (var i = 0; i < QuickAddElems.length; i++) {
		var fieldId = "div_" + "QickAddItem_" + i;
		showAddtoCartBtn = true;
		addToCartDiv.style.display = 'inline';
		if (QuickAddElems[i].isValidated == "false") {
			code += '<tr id="' + encodeForHTML(fieldId) + '">';
			code += '<td class="wing-col-item">';
			code += '<a href="#" onclick="removeProductFromQuickAddList(' + i + '); return false;" title="Remove" tabindex="' + tabIndex++ + '"><img src="/swc/xpedx/images/icons/12x12_red_x.png"/></a>';
			code += '</td>';

			code += '<td class="col-item" colspan="4">';
			code += '<span>Validating item ' + encodeForHTML(QuickAddElems[i].sku) + ' Processing... </span>';
			code += '</td>';
			code += '</tr>';

			showAddtoCartBtn = false;
		} else {
			if (QuickAddElems[i].itemTypeText == "Special Item") {
				code += '<tr id="' + encodeForHTML(fieldId) + '">';
			} else if (QuickAddElems[i].isEntitled == "false") {
				code += '<tr id="' + encodeForHTML(fieldId) + '" style="display: none;" >';
			} else {
				code += '<tr id="' + encodeForHTML(fieldId) + '">';
			}
			var divIdErrorQty = "errorQty_" + QuickAddElems[i].sku + i;
			code += '<td class="wing-col-item">';
			code += '<a href="#" onclick="javascript:removeProductFromQuickAddList(' + i + '); return false;" title="Remove" tabindex="' + tabIndex++ + '"><img src="/swc/xpedx/images/icons/12x12_red_x.png" /></a>';
			code += '</td>';
			code += '<td class="col-item">';
			code += '<div id="ItemTypesText_' + i + '">'
			code += encodeForHTML(QuickAddElems[i].itemTypeText);
			code += '</div>'
			code += '<input type="hidden" name="enteredItemTypes" id="enteredItemTypes_' + i + '" value="' + encodeForHTML(QuickAddElems[i].itemType) + '"/>';
			code += '<input type="hidden" name="orderLineOrderMultiples" id="orderLineOrderMultiple_' + i + '" value="' + encodeForHTML(QuickAddElems[i].orderMultiple) + '"/>';
			code += '<input type="hidden" name="itemUomAndConvStrings" id="itemUomAndConvStrings_' + i + '" value="' + encodeForHTML(QuickAddElems[i].itemUomAndConvString) + '"/>';
			code += '</td>';
			code += '<td class="col-item"><p style="width:55px; word-wrap:break-word;">';
			code += encodeForHTML(QuickAddElems[i].sku);
			code += '</P><input type="hidden" name="enteredProductIDs" id="enteredProductIDs_' + i + '" value="' + encodeForHTML(QuickAddElems[i].sku) + '"/>';
			code += '</td>';
			
			code += '<td class="col-item">';
			code += '<input type="hidden" name="quickAddBaseUOMs" value=' + quickAdd_convertToUOMDescription(encodeForHTML(QuickAddElems[i].uom)) + ' />';
			if (QuickAddElems[i].itemTypeText == "Special Item") {
				code += '<input type="text" disabled="disabled" value="1" name="enteredQuantities" id="enteredQuantities_' + i + '" onchange="javascript:isValidQuantity(this);" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" onblur="javascript:updateQuickAddElement(\'Qty\',' + i + ');"  />';
				code += '<input type="hidden" value="1" name="enteredQuantities" id="enteredQuantities_' + i + '" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" onchange="javascript:isValidQuantity(this);" onblur="javascript:updateQuickAddElement(\'Qty\',' + i + ');"  />';
			} else {
				code += '<input type="text" name="enteredQuantities" id="enteredQuantities_' + i + '" value="' + encodeForHTML(QuickAddElems[i].quantity) + '" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" onchange="javascript:isValidQuantity(this);" onblur="javascript:updateQuickAddElement(\'Qty\',' + i + ');" />';
			}
			code += '</td>';
			
			if (QuickAddElems[i].isEntitled == "false") {
				code += '<td class="col-item">';
				if (QuickAddElems[i].itemTypeText == "Special Item") {
					code += '<input type="text" value="EACH" disabled = "true" name="enteredUOMsUI" id="enteredUOMsUI_' + i + '" onchange="javascript:updateQuickAddElement(\'UOM\',' + i + ')" />';
					code += '<input type="hidden" name="enteredUOMsUI" id="enteredUOMsUI_' + i + '" value="' + encodeForHTML(QuickAddElems[i].uom) + '" onchange="javascript:updateQuickAddElement(\'UOM\',' + i + ')" />';
				} else {
					code += '<input type="hidden" name="enteredUOMs" id="enteredUOMs_' + i + '" value="' + encodeForHTML(QuickAddElems[i].uom) + '" />';
				}
				code += '</td>';
				
			} else {
				var msapExtnUseOrderMulUOMFlag = document.getElementById('msapOrderMulUOMFlag').value;
				if ((msapExtnUseOrderMulUOMFlag != null && msapExtnUseOrderMulUOMFlag == 'Y') && (QuickAddElems[i].orderMultiple > "1" && QuickAddElems[i].orderMultiple != null)) {
					var uomValues = QuickAddElems[i].uomList;
					var _uomCodes = QuickAddElems[i].uomCodes;
					var customUomFlagValues = QuickAddElems[i].customUOM;
					code += '<td class="col-item">';
					code += '<select name="enteredUOMsList" id="enteredUOMsList_' + i + '" onchange="javascript:updateQuickAddElement(\'UOMList\',' + i + ')" >';
					var storeUOM = 0;
					var minSelUOM;
					var maxSelUOM;
					var minFractUOM = 0.00;
					var maxFractUOM = 0.00;
					var defaultSelUOM;
					var _oUomCode;
					var _oUomDescription;
					var firstIndex;
					var lastIndex;
					var customUom;
					var orderMultipleValue = QuickAddElems[i].orderMultiple;
					
					for (var oUomidx = 0; oUomidx < uomValues.length; oUomidx++) {
						_oUomCode = uomValues[oUomidx];
						if (customUomFlagValues[oUomidx] == "Y") {
							customUom = _oUomCode;
						}
						_oUomDescription = quickAdd_convertToUOMDescription(_oUomCode);
						firstIndex = uomValues[oUomidx].indexOf('(');
						lastIndex = uomValues[oUomidx].indexOf(')');
						var convFactor;
						var oFraction = 0.00;
						if (lastIndex > 0 && firstIndex > 0) {
							convFactor = uomValues[oUomidx].substring(firstIndex + 1, lastIndex);
						} else {
							convFactor = 1;
						}
						if (convFactor == orderMultipleValue) {
							minSelUOM = maxSelUOM = _oUomCode;
							minFractUOM = maxFractUOM = 1;
							break;
						} else {
							oFraction = convFactor / orderMultipleValue;
							if (oFraction <= 1 && oFraction > minFractUOM) {
								minFractUOM = oFraction;
								minSelUOM = _oUomCode;

							} else if (oFraction > 1 && (oFraction < maxFractUOM || maxFractUOM == 0.00)) {
								maxFractUOM = oFraction;
								maxSelUOM = _oUomCode;
							}
						}
					} // end foreach uomValues
					
					if (customUom != null) {
						defaultSelUOM = customUom;
					} else {
						if (minFractUOM == 1) {
							defaultSelUOM = minSelUOM;
						} else if (maxFractUOM > 0) {
							defaultSelUOM = maxSelUOM;
						} else if (minFractUOM != null) {
							defaultSelUOM = minSelUOM;
						}
					}

					//Passing selUOM as selcted - Done For Jira 3841/3862
					var selUOM = QuickAddElems[i].selectedUOM;
					for (var uomidx = 0; uomidx < uomValues.length; uomidx++) {
						var _uomCode = encodeForHTML(uomValues[uomidx]);
						//customizeUOM This var is used to customize the UOM(like M_SHT_C(36) to M_SHT) - Done for Jira 3841
						var customizeUOM = uomValues[uomidx];
						var testArray = new Array();
						testArray = customizeUOM.split("(");
						//fvar is the final var storing customize UOM- Done for Jira 3841
						var fvar = testArray[0];
						firstIndex = uomValues[uomidx].indexOf('(');
						lastIndex = uomValues[uomidx].indexOf(')');
						var _uomDescription;
						if (firstIndex != -1 && lastIndex != -1 && lastIndex > firstIndex) {
							_uomDescription = quickAdd_convertToUOMDescription(_uomCode.substring(0, firstIndex)) + uomValues[uomidx].substring(firstIndex, lastIndex) + ')';
						} else {
							_uomDescription = quickAdd_convertToUOMDescription(_uomCode);
						}
						//Condn added to check if selected UOM is equal to any alernate UOMs - Done for Jira 3841
						if (selUOM != '' && selUOM == fvar) {
							code += '<option value="' + encodeForHTML(uomValues[uomidx]) + '" selected="yes">' + _uomDescription + '</option>'
						}
						//else we are doing defaulting of UOMs as it is.
						else if (defaultSelUOM.trim() == uomValues[uomidx] && selUOM == '') {
							if (firstIndex != -1) {
								code += '<option value="' + encodeForHTML(uomValues[uomidx].substring(0, firstIndex)) + '" selected="yes">' + _uomDescription + '</option>'
							} else {
								code += '<option value="' + encodeForHTML(uomValues[uomidx]) + '" selected="yes">' + _uomDescription + '</option>'
							}
						} else {
							if (firstIndex != -1) {
								code += '<option value="' + encodeForHTML(uomValues[uomidx].substring(0, firstIndex)) + '" >' + _uomDescription + '</option>'
							} else {
								code += '<option value="' + encodeForHTML(uomValues[uomidx]) + '" >' + _uomDescription + '</option>'
							}
						}
					} // end foreach uomValues

				} else {
					var defaultSelUOM;
					if ((msapExtnUseOrderMulUOMFlag != null && msapExtnUseOrderMulUOMFlag == 'Y')) {
						defaultSelUOM = undefined;
					}
					
					var uomValues = QuickAddElems[i].uomList;
					var _uomCodes = QuickAddElems[i].uomCodes;
					code += '<td class="col-item">';
					code += '<select name="enteredUOMsList" id="enteredUOMsList_' + i + '" onchange="javascript:updateQuickAddElement(\'UOMList\',' + i + ')" >';
					
					//Passing selUOM as selcted - Done For Jira 3841/3862
					var selUOM = QuickAddElems[i].selectedUOM;
					var customUomFlagValues = QuickAddElems[i].customUOM;
					var customUom;
					for (var oUomidx = 0; oUomidx < uomValues.length; oUomidx++) {
						_oUomCode = uomValues[oUomidx];
						if (customUomFlagValues[oUomidx] == "Y") {
							customUom = _oUomCode;
							defaultSelUOM = _oUomCode;
						}
					}
					
					for (var uomidx = 0; uomidx < uomValues.length; uomidx++) {
						var _uomCode = encodeForHTML(uomValues[uomidx]);
						//customizeUOM This var is used to customize the UOM(like M_SHT_C(36) to M_SHT) - Done for Jira 3841
						var customizeUOM = uomValues[uomidx];
						var testArray = new Array();
						testArray = customizeUOM.split("(");
						//fvar is the final var storing customize UOM- Done for Jira 3841
						var fvar = testArray[0];
						var _uomDescription = quickAdd_convertToUOMDescription(_uomCode);
						var firstIndex = uomValues[uomidx].indexOf('(');
						var lastIndex = uomValues[uomidx].indexOf(')');
						if (firstIndex != -1 && lastIndex != -1 && lastIndex > firstIndex) {
							_uomDescription = quickAdd_convertToUOMDescription(_uomCode.substring(0, firstIndex)) + uomValues[uomidx].substring(firstIndex, lastIndex) + ')';
						}
						//Condn added to check if selected UOM is equal to any alernate UOMs - Done for Jira 3841
						if (selUOM != '' && selUOM == fvar) {
							code += '<option value="' + encodeForHTML(uomValues[uomidx]) + '" selected="yes">' + _uomDescription + '</option>'
						}
						//else we are doing defaulting of UOMs as it is.
						else if (defaultSelUOM != undefined && _uomCodes[uomidx].trim() == defaultSelUOM.trim() && selUOM == '') {
							if (firstIndex != -1) {
								code += '<option value="' + encodeForHTML(uomValues[uomidx].substring(0, firstIndex)) + '" selected="yes">' + _uomDescription + '</option>'
							} else {
								code += '<option value="' + encodeForHTML(uomValues[uomidx]) + '" selected="yes">' + _uomDescription + '</option>'
							}
						} else {
							if (firstIndex != -1) {
								code += '<option value="' + encodeForHTML(uomValues[uomidx].substring(0, firstIndex)) + '" >' + _uomDescription + '</option>'
							} else {
								code += '<option value="' + encodeForHTML(uomValues[uomidx]) + '" >' + _uomDescription + '</option>'
							}
						}
					}
				}
				
				var createNewHiddenField = true;
				if (defaultSelUOM == undefined && selUOM == '') {
					code += '<input type="hidden" name="enteredUOMs" id="enteredUOMs_' + i + '" value="' + encodeForHTML(QuickAddElems[i].uom) + '" />';
					createNewHiddenField = false;
				} else if (!selUOM == '') {
					var selectedUOMQty = selUOM.split(" ");
					var selectedUOMs;
					if (selectedUOMQty.length == 2) {
						selectedUOMs = selectedUOMQty[0];
					} else {
						selectedUOMs = selectedUOMQty;
					}

				} else {
					var selectedUOMQty = defaultSelUOM.split(" ");
					var selectedUOMs;
					if (selectedUOMQty.length == 2) {
						selectedUOMs = selectedUOMQty[0];
					} else {
						selectedUOMs = selectedUOMQty;
					}
				}

				code += '</select>';
				//Added selUOM condn.If UOM is not changed,pass default UOM,else pass selUOM. - Jira 3841
				if (defaultSelUOM != undefined && selUOM == '' && createNewHiddenField) {
					code += '<input type="hidden" name="enteredUOMs" id="enteredUOMs_' + i + '" value="' + selectedUOMs + '" />';

				} else if (createNewHiddenField) {
					code += '<input type="hidden" name="enteredUOMs" id="enteredUOMs_' + i + '" value="' + selUOM + '" />';
				}
				code += '</td>';
			}
			
			if (document.QuickAddForm.jobIdValue != undefined && document.QuickAddForm.jobIdValue.value != null) {
				jobValue = document.QuickAddForm.jobIdValue.value;
			}
			
			if (custPOFlag) {
				code += '<td class="col-item">';
				code += '<input type="text" name="enteredPONos" maxlength="22" id="enteredPONos_' + i + '" value="' + encodeForHTML(QuickAddElems[i].purchaseOrder) + '" onchange="javascript:updateQuickAddElement(\'PO\',' + i + ')"/>';
				code += '</td>';
			}
			if (jobidFlag == true && jobValue != null) {
				code += '<td class="col-item">';
				code += '<input type="text" name="enteredJobIDs" maxlength="24" id="enteredJobIDs_' + i + '" value="' + encodeForHTML(QuickAddElems[i].jobId) + '" onchange="javascript:updateQuickAddElement(\'JobId\',' + i + ')"/>';
				code += '</td>';
			}

			var itemDescription = "enteredProductDescs_" + i;
			code += '<input type="hidden" name="enteredProductDescs" id="' + encodeForHTML(itemDescription) + '" value="' + encodeForHTML(QuickAddElems[i].itemDesc) + '"/>';

			if (QuickAddElems[i].itemTypeText == "Special Item") {
				var checkedfieldId = "enteredSpecialItems_" + i;
				code += '<input type="hidden" name="enteredSpecialItems" id="' + encodeForHTML(checkedfieldId) + '" value="' + encodeForHTML(QuickAddElems[i].sku) + '"/>';
			} else if (QuickAddElems[i].isEntitled == "false") {
				var checkedfieldId = "enteredSpecialItems_" + i;
				code += '<input type="hidden" name="enteredSpecialItems" id="' + encodeForHTML(checkedfieldId) + '" value=""/>';
			}
			code += '</tr>';
			
			if (QuickAddElems[i].itemTypeText != "Special Item" && QuickAddElems[i].isEntitled == "false") {
				code += '<tr class="error-row">';
				code += '<td colspan="4">';
				code += '<div id="' + divIdErrorQty + '"></div>';
				code += '</td>';
				code += '</tr>';
			} else if (QuickAddElems[i].orderMultiple > "1" && QuickAddElems[i].orderMultiple != null) {
				code += '<tr class="error-row">';
				code += '<td colspan="4">';
				code += '<div align="center" class="notice qa-standalone-notice" id="' + divIdErrorQty + '" style="display : inline;">Must be ordered in units of ' + addComma(QuickAddElems[i].orderMultiple) + '&nbsp;' + quickAdd_convertToUOMDescription(encodeForHTML(QuickAddElems[i].uom)) + '</div>';
				code += '</td>';
				code += '</tr>';
			} else {
				code += '<tr class="error-row">';
				code += '<td colspan="4">';
				code += '<div id="' + divIdErrorQty + '"></div>';
				code += '</td>';
				code += '</tr>';
			}
			if (QuickAddElems[i].itemTypeText != "Special Item" && QuickAddElems[i].isEntitled == "false") {
				var specialId = "div_" + "enteredSpecialItems_" + i;
				var specialDivId = "divSpecialItemContent_" + i;
				var divFieldId = "div" + i;

				code += '<tr id="' + encodeForHTML(specialId) + '">';

				code += '<td class="wing-col-item">';
				code += '<a href="#" class="del-quick-add" onclick="javascript:removeProductFromQuickAddList(' + i + '); return false;" title="Remove" tabindex="' + tabIndex++ + '"><img src="/swc/xpedx/images/icons/12x12_red_x.png" /></a>';
				code += '</td>';

				code += '<td class="col-item" style="color:red" colspan="3" >';
				// TODO -FXD- : make the following line colspan '3' instead of two if the other customer defined fields are shown.
				// ie. stretch into three columns if there are three, or two if there are two
				code += 'Invalid item # ' + encodeForHTML(QuickAddElems[i].sku) + '. Please review and try again or contact Customer Service.</td>';

				code += '<div id="' + encodeForHTML(specialDivId) + '" style="display: none;">';
				code += '<div class="xpedx-light-box" id="inline1" style="height: 305px; width: 550px;>';
				code += '  <span class="margin-top-none">';
				code += '	<h2>Add Non-Catalog Item</h2>';
				code += '  	<br>';
				code += '	<strong>HINT:</strong>';
				code += ' Use this form to add items you can\'t find in the online catalog, or would like to special order.';
				code += ' Items added here may be deleted from your shopping cart at any time</span>';
				code += '  <br>';
				code += '		<div>';
				code += '         	<p><span id="catalog-descr">Description:</span>';
				code += '			<textarea class="margin-left-10" id="siUIDesc_' + encodeForHTML(divFieldId) + '" rows="3" cols="50" onload="javascript:captureSpecialItemValues(this)" onchange="javascript:captureSpecialItemValues(this)"></textarea>';
				code += '         	</p>';
				code += '		</div>';
				code += '		<div>';
				code += '			<div class="add-non-catalog-labels"><span class="add-non-catalog-qty-label">Qty:</span>';
				code += '           		<input type="text" disabled="true" value="1" class="input-label add-non-catalog-qty-input" tabindex="12" onload="javascript:captureSpecialItemValues(this)" onchange="javascript:captureSpecialItemValues(this)" title="QTY" id="siUIQty_' + encodeForHTML(divFieldId) + '" value="' + encodeForHTML(QuickAddElems[i].quantity) + '" />';
				code += '              	<span class="bold">UOM:</span>';
				code += ' 					<input type="text" disabled="true" value="EACH" maxlength="16" class="input-label add-non-catalog-UOM-input" tabindex="13" style="text-align: left;" onload="javascript:captureSpecialItemValues(this)" onchange="javascript:captureSpecialItemValues(this)" title="UOM" id="siUIUom_' + encodeForHTML(divFieldId) + '" value="' + encodeForHTML(QuickAddElems[i].uom) + '" />';
				code += ' 			   </div>';
				code += ' 		</div>';
				code += '     	<ul id="tool-bar" class="tool-bar-bottom-right">';
				code += '     		<li>';
				code += '         		<a href="javascript:closeSpecialUIbox();" class="grey-ui-btn button-margin">';
				code += '				<span>Cancel</span>';
				code += '         	    </a>';
				code += '			</li>';
				code += '      		<li>';
				code += '    	     	<a href="javascript:addSpecialItem(' + i + ');" class="green-ui-btn">';
				code += '             	<span>Add to Cart</span>';
				code += '				</a>';
				code += '			</li>';
				code += '	</ul>';
				code += '</div>';
				code += '</div> ';
				code += '</td>';
				code += '</tr>';
			}
		}
	}
	code += '</tbody>';
	code += '</table>';

	theDiv = document.getElementById("QuickAddList");
	theDiv.innerHTML = code;

	svg_classhandlers_decoratePage();
} // end function function quickAdd_redrawQuickAddList


/*
 * This method is a copy of XPEDXDraftOrderDetails.js/convertToUOMDescription with quick hacks for eb-1999, which was a quick hack for performance.
 * I did NOT remove that function since it may have broken other pages.
 * When this page is fully rewritten as part of the larger epic (eb-4366) then this should be cleaned up.
 */
var globalCachedUomDescriptions = {};
function quickAdd_convertToUOMDescription(uomCode) {
	if (!globalCachedUomDescriptions[uomCode]) {
		var url = document.getElementById("uomDescriptionURL").value;
		var req = null;
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		} else {
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}
		url = url + '&uomCode=' + uomCode;
		req.open('POST', url, false);
		req.send(null);
		if (req.status == 200) {
			globalCachedUomDescriptions[uomCode] = req.responseText;
			return req.responseText;
		} else {
			// error case
			return uomCode;
		}
	}
	return globalCachedUomDescriptions[uomCode];
}


function quickAddCopyAndPaste(data) {
	var prodcutValidateUrl = document.getElementById('productValidateURL').href;
	document.getElementById('productValidateURL').href = document.getElementById('productListValidateURL').href;
	var itemLineFlag = "false";
	var itemsString = data;
	var char = '\n';
	var itemLines = itemsString.split(char);
	var checklength = itemLines.length; //jira 4128
	if (itemsString == "") {
		document.getElementById("errorMsgCopyBottom").innerHTML = "Valid string is required. See instructions above.";
		document.getElementById("errorMsgCopyBottom").style.display = "inline";
		return false;
	} else {
		document.getElementById("errorMsgCopyBottom").innerHTML = "";
		document.getElementById("errorMsgCopyBottom").style.display = "none";
	}

	for (var i = 0; i < itemLines.length; i++) {
		var itemQty = null;
		var itemSku = null;
		var jobId = "";
		var itemLine = itemLines[i].split('\t');
		if (i == itemLines.length - 1) {
			if (itemLine == "") {
				break;
			}
		}
		if (checklength > 20) {
			itemLineFlag = "true";
			alert("The list has exceeded the maximum of 20 items. Please remove some items and try again.");
			break;
		}

		if (itemLine.length > 1) {
			itemQty = itemLine[0];
			itemSku = itemLine[1];
		}
		itemLine = itemLines[i].split(',');
		if (itemLine.length > 1) {
			itemQty = itemLine[0];
			itemSku = itemLine[1];
			if (itemSku == "" && itemQty == "") {
				itemLineFlag = "true";
				document.getElementById("errorMsgCopyBottom").innerHTML = "Valid string is required. See instructions above.";
				document.getElementById("errorMsgCopyBottom").style.display = "inline";
				return false;
			}
		} else {
			itemLineFlag = "true";
			document.getElementById("errorMsgCopyBottom").innerHTML = "Valid string is required. See instructions above.";
			document.getElementById("errorMsgCopyBottom").style.display = "inline";
			return false;
		}
	}
	
	if (itemLineFlag == "false") {
		for (var i = 0; i < itemLines.length; i++) {
			var itemQty = null;
			var itemSku = null;
			var jobId = "";
			var itemLine = itemLines[i].split('\t');

			if (itemLine.length > 1) {
				itemQty = itemLine[0];
				itemSku = itemLine[1];
			}
			itemLine = itemLines[i].split(',');

			if (itemLine.length > 1) {
				itemQty = itemLine[0];
				itemSku = itemLine[1];
			}
			if ((i + 1) == itemLines.length && itemLineFlag == "false") {
				$.fancybox.close();
				Ext.get('dlgCopyAndPasteText').dom.value = '';
			}
			
			itemSku = Ext.util.Format.trim(itemSku);
			itemQty = Ext.util.Format.trim(itemQty);
			
			if (itemSku != null && itemSku != "null") {
				document.getElementById("qaProductID").value = itemSku;
				document.getElementById("qaQuantity").value = itemQty;
				
				var theForm = document.getElementById('quickAddButton').form;
				
				var sku = trim(theForm.qaProductID.value);
				var quantity = trim(theForm.qaQuantity.value);
				var jobId = "";
				if (theForm.qaJobID != null) {
					jobId = trim(theForm.qaJobID.value);
					jobidFlag = true;
				}
				var itemType = trim(theForm.qaItemType.value);
				var itemTypeText = itemType;
				
				var itemTypeSelElem = theForm.qaItemType;
				if (itemTypeSelElem != null) {
					itemType = itemTypeSelElem.options[itemTypeSelElem.selectedIndex].value;
					itemTypeText = itemTypeSelElem.options[itemTypeSelElem.selectedIndex].text;
				}
				
				var uomArray = new Array();
				var CustomUomArray = new Array();
				var purchaseOrder = "";
				if (theForm.purchaseOrder != null) {
					purchaseOrder = theForm.purchaseOrder.value;
					custPOFlag = true;
				}
				if (sku == "") {
					setErrorMessage('Please enter a valid Item #.');
					return;
				} else {
					clearErrorMessage();
				}
				if (quantity == "") {
					quantity = 1;
					theForm.qaQuantity.value = quantity;
				}
				
				document.getElementById("validationActionNameQA").value = "draftOrderAddOrderLines";
				if (swc_validateForm("QuickAddForm") == false) {
					theForm.qaQuantity.focus();
					return;
				}
				
				QuickAddElems[QuickAddElems.length] = {
					sku: sku,
					quantity: quantity,
					jobId: jobId,
					purchaseOrder: purchaseOrder,
					itemType: itemType,
					itemTypeText: itemTypeText,
					itemDesc: "",
					uom: "",
					uomList: uomArray,
					isValidated: "false",
					isEntitled: "true",
					orderMultiple: "",
					itemUomAndConvString: "",
					//Added selectedUOM for Jira 3862
					selectedUOM: "",
					customUOM: CustomUomArray
				};
				
				theForm.qaProductID.value = "";
				theForm.qaQuantity.value = "";
				if (theForm.qaJobID != null) {
					theForm.qaJobID.value = "";
				}
				if (theForm.purchaseOrder != null) {
					theForm.purchaseOrder.value = "";
				}
				
				// Kludge to get localized string from the form for use in the HTML
				// generated by quickAdd_redrawQuickAddList. It counts on the fact that the
				// first time quickAdd_redrawQuickAddList will be called on the page is for
				// the add. If it were to be called beforehand, there would be no
				// deleteStringFromForm set.
				if (deleteStringFromForm == "") {
					deleteStringFromForm = theForm.localizedDeleteLabel.value;
				}
				if (addStringFromForm == "") {
					addStringFromForm = theForm.localizedAddToCartLabel.value;
				}
			} // end if itemSku not null
		} // end for itemLines
	} // end if itemLineFlag is 'false'
	
	quickAdd_validateItems();
	document.getElementById('productValidateURL').href = prodcutValidateUrl;
} // end function quickAddCopyAndPaste


function setErrorMessage(message) {
	if (message) {
		var $errorMsgItemBottom = $('#errorMsgItemBottom');
		$errorMsgItemBottom.html(message);
		$errorMsgItemBottom.show();
	} else {
		clearErrorMessage();
	}
}
function clearErrorMessage() {
	var $errorMsgItemBottom = $('#errorMsgItemBottom');
	$errorMsgItemBottom.hide();
	$errorMsgItemBottom.html('');
}


$(document).ready(function() {
	$('#copyPaste').fancybox({
		onClosed: function() {
			$.fancybox.close();
			
			$('#dlgCopyAndPasteText').val('');
			
			// clear copy and paste error message
			$errorMsgCopyBottom = $('#errorMsgCopyBottom');
			$errorMsgCopyBottom.innerHTML = '';
			$errorMsgCopyBottom.hide();
		}
	});
});
