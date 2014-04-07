
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
 * Source: http://stackoverflow.com/questions/16941386/validate-a-string-is-non-negative-whole-number-in-javascript
 */
function isInt(n) {
	if (n) {
		var intRegex = /^\d+$/;
		return intRegex.test(n);
	} else {
		return false;
	}
}


/*
 * Manage error messages
 */
function setItemErrorMessage(rowId, errorMessage) {
	$('#producterrorLine_' + rowId).show().get(0).innerHTML = errorMessage;
}
function clearItemErrorMessages() {
	var errorDivs = $('.producterrorLine');
	for (var i = 0, len = errorDivs.length; i < len; i++) {
		errorDivs[i].innerHTML = '';
		errorDivs[i].style.display = 'none';
	}
}


/*
 * Manage processing bar, which must remain active during item validation
 */
function showProcessingBar() {
	var waitMsg = Ext.Msg.wait("Processing...");
	myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
	myMask.show();
}
function hideProcessingBar() {
	Ext.Msg.hide();
	myMask.hide();
}


/*
 * Performs validation on all rows with item and/or quantity (empty rows are ignored):
 * 1. Validates that each row has both item and quantity.
 * 2. Validates that each quantity has been entered as an integer.
 * 3. Validates that each item exists (id exists, is entitled, etc).
 */
function validateItems() {
	showProcessingBar();
	clearItemErrorMessages();
	
	var itemsToValidate = []; // holds objects
	var rowIdsForItem = {}; // key=item, value=list of rowId (since multiple rows could have the same item #)
	var errorMessageForRowId = {}; // key=rowId, value=error message
	
	var rows = $('.qa-listrow:visible');
	var hasErrors = false;
	for (var rowId = 1, len = rows.length; rowId <= len; rowId++) {
		var $row = $(rows[rowId - 1]);
		var itemId = $row.find('.input-item').val().trim();
		var qty = $row.find('.input-qty').val().trim();
		
		if (!itemId && !qty) {
			// ignore blank links
			continue;
		}
		
		if (!itemId) {
			errorMessageForRowId[rowId] = 'Please enter a valid item # and try again.';
			hasErrors = true;
		} else if (!qty || !isInt(qty) || parseInt(qty) < 1) {
			errorMessageForRowId[rowId] = 'Please enter a valid quantity and try again.';
			hasErrors = true;
		} else {
			itemsToValidate.push({
				'id': itemId,
				'qty': qty
			});
			
			var bucket = rowIdsForItem[itemId];
			if (!bucket) {
				// lazy-load list
				bucket = [];
				rowIdsForItem[itemId] = bucket;
			}
			bucket.push(rowId);
		}
	}
	
	if (!hasErrors && itemsToValidate.length == 0) {
		// if form is completely blank, then treat as missing data on first row
		errorMessageForRowId[1] = 'Please enter a valid item # and try again.';
		hasErrors = true;
	}
	
	if (hasErrors) {
		for (var rowId in errorMessageForRowId) {
			var errorMessage = errorMessageForRowId[rowId];
			setItemErrorMessage(rowId, errorMessage);
		}
		hideProcessingBar();
		return;
		
	} else {
		var itemType = $('#qaItemType').val();
		var url = $('#ajaxValidateItemsURL').attr('href');
		url += '&itemType=' + itemType;
		for (var i = 0, len = itemsToValidate.length; i < len; i++) {
			url += '&itemIds=' + encodeURIComponent(itemsToValidate[i].id);
		}
		$.ajax({
			type: 'GET'
			,url: url
			,dataType: 'json'
			,success: function(data) {
				if (data.hasItemErrors) {
					for (itemId in data.itemValidFlags) {
						var validItem = data.itemValidFlags[itemId];
						if (!validItem) {
							var invalidRowIds = rowIdsForItem[itemId];
							for (var i = 0, len = invalidRowIds.length; i < len; i++) {
								setItemErrorMessage(invalidRowIds[i], 'Invalid Item # <item>. Please review and try again.');
							}
						}
					}
					hideProcessingBar();
					
				} else {
					quickAdd_addProductsToOrder(itemsToValidate, data.itemUoms, itemType);
				}
			}
			,error: function(resp, textStatus, xhr) {
				// TODO how to recover from this? definitely stop the processing bar. ideally also show error message
				hideProcessingBar();
			}
		});
	}
}


/*
 * Adds the given items to the cart. Parameters:
 * items: An array of objects, each with the properties: id, qty
 * itemUoms: A hash in the form: key=itemId, value=uom
 * itemType: 1=xpedx item #, 2=Customer Part #
 */
function quickAdd_addProductsToOrder(items, itemUoms, itemType) {
	var $form = $("#QuickAddForm");
	$form.attr('action', $('#addProductsToOrderURL').attr('href'));
	
	// by design, the quick add ui doesn't collect all the input necessary for XPEDXDraftOrderAddOrderLinesAction.java/execute, so we must inject some extra data
	var htmlExtraInputs = [];
	var rows = $('.qa-listrow:visible');
	for (var rowId = 1, len = rows.length; rowId <= len; rowId++) {
		var $row = $(rows[rowId - 1]);
		var itemId = $row.find('.input-item').val().trim();
		var uom = itemUoms[itemId];
		
		$row.find('.input-uom').val(uom ? uom : '');
		$row.find('.input-itemType').val(itemType);
	}
	$form.append(htmlExtraInputs.join(''));
	
	addCSRFToken($form.get(0), 'form');
	$form.submit();
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


/*
 * Shows the specified quick add row.
 */
function showQuickAddRow(rowId){
	if (rowId > 200) {
		return;
	}
	var rowDiv="qa-listrow_" + rowId;
	$('#' + rowDiv).show();
}


$(document).ready(function() {
	$('#copyPaste').fancybox({
		onComplete: function() {
			$('#dlgCopyAndPasteText').focus();
		},
		onClosed: function() {
			$.fancybox.close();
			
			$('#dlgCopyAndPasteText').val('');
			
			// clear copy and paste error message
			$errorMsgCopyBottom = $('#errorMsgCopyBottom');
			$errorMsgCopyBottom.innerHTML = '';
			$errorMsgCopyBottom.hide();
		}
	});
	
	$('#btn-reset-copy-paste').click(function() {
		$('#copypaste-text').val('');
	});
	
	$('#btn-add-to-quick-list').click(function() {
		$('#copypaste-error').hide().get(0).innerHTML = '';
		
		// split data into lines, ignoring blank rows
		var lines = $('#copypaste-text').val().match(/[^\r\n]+/g);
		if (lines.length > 200) {
			
			$('#copypaste-error').show().get(0).innerHTML = 'You cannot add more than 200 items to the list.<br/>You have entered ' + lines.length + ' items.';
			return;
		}
		
		for (var i = 0, len = lines.length; i < len; i++) {
			var line = lines[i];
			var rowId = i + 1;
			
			var tokens = line.split(/\s*,\s*/);
			
			// overwrite each value - use empty for any values not provided
			var $row = $('#qa-listrow_' + rowId);
			$row.find('.input-item').val(tokens[0] ? tokens[0] : '');
			$row.find('.input-qty').val(tokens[1] ? tokens[1] : '');
			$row.find('.input-po').val(tokens[2] ? tokens[2] : '');
			$row.find('.input-account').val(tokens[3] ? tokens[3] : '');
			showQuickAddRow(rowId + 1);
		}
		
		// clear the textarea
		$('#copypaste-text').val('');
	});
});
