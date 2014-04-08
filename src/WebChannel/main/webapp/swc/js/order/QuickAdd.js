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
		var url = $('#ajaxAddItemsToCartURL').attr('href');
//		url += '&itemType=' + itemType;
//		for (var i = 0, len = itemsToValidate.length; i < len; i++) {
//			url += '&itemIds=' + encodeURIComponent(itemsToValidate[i].id);
//		}
		
		var itemIdsList = itemsToValidate[0].id;
		for (var i = 1, len = itemsToValidate.length; i < len; i++) {
			itemIdsList += "*" + itemsToValidate[i].id;
		}
		$.ajax({
			type: 'POST'
			,url: url
			,dataType: 'json'
			,data: { 'itemType': itemType, 'itemIds': itemIdsList }
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
					addProductsToOrder(itemsToValidate, data.itemUoms, itemType);
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
function addProductsToOrder(items, itemUoms, itemType) {
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
	$('#btn-reset-copy-paste').click(function() {
		$('#copypaste-text').val('');
	});
	
	$('#btn-add-to-list').click(function() {
		$('#copypaste-error').hide().get(0).innerHTML = '';
		
		// split data into lines, ignoring blank rows
		var lines = $('#copypaste-text').val().match(/[^\r\n]+/g);
		if (lines.length == 0) {
			return;
		}
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
		
		for (var rowId = lines.length + 1; rowId <= 200; rowId++) {
			$('#qa-listrow_' + rowId + ' input').val('');
		}
		
		// clear the textarea
		$('#copypaste-text').val('');
	});
});
