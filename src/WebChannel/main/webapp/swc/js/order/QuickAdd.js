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
function clearAll(){
	var rows = $('.qa-listrow:visible');
	for (var rowId = 1, len = rows.length; rowId <= len; rowId++){
		$('#enteredProductIDs_'+rowId).val('');
		$('#enteredQuantities_'+rowId).val('');
		$('#enteredPONos_'+rowId).val('');
		$('#enteredJobIDs_'+rowId).val('');
		if (rowId > 5){
			var rowDiv="qa-listrow_" + rowId;
			$('#' + rowDiv).hide();
		}
	}
}
function clearErrorRow(rowId){
	$('#errorIcon_'+ rowId).click(function(){
		$('#enteredProductIDs_'+rowId).val('');
		$('#enteredQuantities_'+rowId).val('');
		$('#enteredPONos_'+rowId).val('');
		$('#enteredJobIDs_'+rowId).val('');
		clearErrorMessage(rowId);
		
		return false;
	});
}
function clearErrorMessage(rowId){
	$('#producterrorLine_' + rowId).hide();
	document.getElementById("errorIcon_" + rowId).style.visibility="hidden";
}

/*
 * Manage error messages
 */
function setItemErrorMessage(rowId, errorMessage) {
	$('#producterrorLine_' + rowId).show().get(0).innerHTML = errorMessage;
	document.getElementById("errorIcon_" + rowId).style.visibility="visible";
	clearErrorRow(rowId);
}
function clearItemErrorMessages() {
	var errorDivs = $('.producterrorLine');
	for (var i = 0, len = errorDivs.length; i < len; i++) {
		errorDivs[i].innerHTML = '';
		errorDivs[i].style.display = 'none';
		
	}
	
}

/*
 * Performs validation on all rows with item and/or quantity (empty rows are ignored):
 * 1. Validates that each row has both item and quantity.
 * 2. Validates that each quantity has been entered as an integer.
 * 3. Validates that each item exists (id exists, is entitled, etc).
 */
function validateItems() {
	showProcessingIcon();
	clearItemErrorMessages();
	
	
	var itemsToValidate = []; // holds objects
	var rowIdsForItem = {}; // key=item, value=list of rowId (since multiple rows could have the same item #)
	var errorMessageForRowId = {}; // key=rowId, value=error message
	
	var rows = $('.qa-listrow:visible');
	var hasErrors = false;
	for (var rowId = 1, len = rows.length; rowId <= len; rowId++) {
		var $row = $(rows[rowId - 1]);
		var itemId = $row.find('.input-item').val();
		var qty = $row.find('.input-qty').val();
		var po = $row.find('.input-po').val();
		var account = $row.find('.input-account').val();
		
		clearErrorMessage(rowId);
		itemId = itemId ? itemId.trim() : '';
		qty = qty ? qty.trim() : '';
		po = po ? po.trim() : '';
		account = account ? account.trim() : '';
		
		if (!itemId && !qty) {
			// ignore blank links
			continue;
		}
		
		if (!itemId) {
			errorMessageForRowId[rowId] = 'Please enter a valid item # and try again.';
			//clearErrorRow(rowId);
			hasErrors = true;
			
			
		} else if (!qty || !isInt(qty) || parseInt(qty) < 1) {
			errorMessageForRowId[rowId] = 'Please enter a valid quantity and try again.';
			hasErrors = true;
		} else {
			itemsToValidate.push({
				'id': itemId,
				'qty': qty,
				'po': po,
				'account': account
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
		hideProcessingIcon();
		return;
		
	} else {
		var itemType = $('#qaItemType').val();
		var url = $('#ajaxAddItemsToCartURL').attr('href');
		
		var items = itemsToValidate[0].id;
		var qtys = itemsToValidate[0].qty;
		var pos = itemsToValidate[0].po;
		var accounts = itemsToValidate[0].account;
		for (var i = 1, len = itemsToValidate.length; i < len; i++) {
			items += "*" + itemsToValidate[i].id;
			qtys += "*" + itemsToValidate[i].qty;
			pos += "*" + itemsToValidate[i].po;
			accounts += "*" + itemsToValidate[i].account;
		}
		
		$.ajax({
			type: 'POST'
			,proccessData: false	
			,url: url
			,dataType: 'json'
			,data: {
					'isEditOrder': $('#isEditOrder').val()
					,'orderHeaderKey': $('#orderHeaderKey').val()
					,'itemType': itemType
					,'items': items
					,'qtys': qtys
					,'pos': pos
					,'accounts': accounts
				}
			,success: function(data) {
				if (data.allItemsValid) {
					var quickAddUrl = $('#quickAddURL').attr('href');
					window.location.href = quickAddUrl;
					
				} else if (data.unexpectedError) {
					setItemErrorMessage(1, 'Unexpected error. Please try again.');
					hideProcessingIcon();
					if (console) { console.log('Unexpected error while adding items to cart: ' + data.unexpectedError); }
					
				} else {
					for (itemId in data.itemValidFlags) {
						var validItem = data.itemValidFlags[itemId];
						if (!validItem) {
							var invalidRowIds = rowIdsForItem[itemId];
							for (var i = 0, len = invalidRowIds.length; i < len; i++) {
								setItemErrorMessage(invalidRowIds[i], 'Invalid Item # <item>. Please review and try again.');
							}
						}
					}
					hideProcessingIcon();
				}
			}
			,error: function(resp, textStatus, xhr) {
				// TODO how to recover from this? definitely stop the processing bar. ideally also show error message
				hideProcessingIcon();
			}
		});
	}
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


