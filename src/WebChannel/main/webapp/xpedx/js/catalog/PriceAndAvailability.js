/**
 * @param modal If true, displays processing bar during ajax call.
 * @param items array containing item ids
 * @param qtys array containing quantities
 * @param uoms array containing units of measure
 */
function getPriceAndAvailabilityForItems(modal, items, qtys, uoms) {
	if (modal) {
		var waitMsg = Ext.Msg.wait("Processing...");
		myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
		myMask.show();
	}
	var url = $('#getPriceAndAvailabilityForItemsURL').val();
	
	for (var i = 0, len = items.length; i < len; i++) {
		$('#availabilty_' + items[i]).hide().get(0).innerHTML = '';
	}
	
	var origQty = [];
	if (!qtys) {
		qtys = [];
		for (var i = 0, len = items.length; i < len; i++) {
			var qty = $('#Qty_' + items[i]).val().trim();
			qtys.push(qty);
			origQty.push(qty);
		}
	}
	
	if (!uoms) {
		uoms = [];
		for (var i = 0, len = items.length; i < len; i++) {
			var uom = $('#itemUomList_' + items[i]).val();
			uoms.push(uom);
		}
	}
	
	// if qty is blank, set qty and uom to default for order multiple
	for (var i = 0, len = qtys.length; i < len; i++) {
		if (qtys[i].trim().length == 0) {
			qtys[i] = $('#orderMultiple_' + items[i]).val();
			
			var baseUom = $('#baseUOMItem_' + items[i]).val();
			if (uoms[i] != baseUom) {
				// adjust order multiple quantity for the requested uom
				var uomConvFactor = $('#convF_' + uoms[i]).val();
				if (qtys[i] % uomConvFactor == 0) {
					qtys[i] = (qtys[i] / uomConvFactor) + '';
				} else {
					qtys[i] = '1';
				}
			}
		}
	}
	
	$.ajax({
		url: url,
		dataType: 'json',
		data: {
			'items': items.join('*'),
			'qtys': qtys.join('*'),
			'uoms': uoms.join('*')
		},
		complete: function() {
			if (modal) {
				Ext.Msg.hide();
				myMask.hide();
			}
		},
		success: function(data) {
			var pna = data.priceAndAvailability;
			var pricingInfo = data.pricingInfo;
			
			if (!pna || pna.transactionStatus != 'P') {
				alert('Real time Price & Availability is not available at this time. Please contact Customer Service.');
				return;
			}
			
			for (var i = 0, len = pna.items.length; i < len; i++) {
				var pnaItem = pna.items[i];
				
				var $divItemAvailability = $('#availabilty_' + pnaItem.legacyProductCode);
				if ($divItemAvailability.length == 0) {
					if (console) { console.log('Element not found: #availabilty_' + pnaItem.legacyProductCode); }
					continue;
				}
				
				var lineErrorMessage = data.lineErrorMessages[pnaItem.legacyProductCode];
				if (lineErrorMessage && lineErrorMessage.trim().length > 0) {
					var html = [];
					html.push('		<div class="pa-wrap">');
					html.push('			<h5 align="center"><b><font color="red">', htmlEncode(lineErrorMessage), '</font></b></h5>');
					html.push('		</div>');
					$divItemAvailability.show().get(0).innerHTML = html.join('');
					continue;
				}
				
				// order multiple messaging
				$divErrorMsgForQty = $('#errorMsgForQty_' + pnaItem.legacyProductCode);
				var isOrderMultipleError = pnaItem.orderMultipleErrorFromMax == 'true' && pnaItem.requestedQty;
				var cssClass = isOrderMultipleError ? 'error' : 'notice';
				cssClass += ' pnaOrderMultipleMessage';
				var html = [];
				html.push('<div class="', cssClass, '">Must be ordered in units of ', pnaItem.orderMultipleQty, ' ', data.uomDescriptions[pnaItem.orderMultipleUOM], '</div>'); // TODO remove inline styles
				$divErrorMsgForQty.show().get(0).innerHTML = html.join('');
				$('#Qty_' + pnaItem.legacyProductCode).css('border-color', isOrderMultipleError ? 'red' : '');
				if (isOrderMultipleError) {
					continue;
				}
				
				var pricingForItem = pricingInfo[pnaItem.legacyProductCode];
				
				var showBracket = false;
				var showPricing = false;
				if (data.userHasViewPricesRole) {
					var isPaperCategory = pricingForItem.categoryPath.indexOf('/MasterCatalog/300057') != -1;
					showBracket = pricingForItem.isBracketPricing == "true" && pnaItem.brackets.length > 0 && isPaperCategory; // old code also had: && #_action.getValidateOMForMultipleItems() == "true"
					showPricing = pricingForItem.displayPriceForUoms.length > 0;
				}
				
				var columnMode;
				if (showBracket && showPricing) {
					columnMode = 'three-col';
				} else if (showBracket || showPricing) {
					columnMode = 'two-col';
				} else {
					columnMode = 'one-col';
				}
				
				var pnaAvail = calculateAvailability(pnaItem.warehouseLocationList);
				
				var html = [];
				html.push('		<div class="pa-wrap ', columnMode, '">');
				html.push('			<div class="pa-avail">');
				html.push('				<h4>Availability</h4>');
				html.push('				<div class="avail-wrap">');
				
				var requestedQty = parseFloat(pnaItem.requestedQty);
				
				if (origQty[i].trim().length > 0) {
					// only display 'ready to ship' message if user requested a qty (omit message if user left qty field blank)
					if (pnaAvail['total'] == 0) {
						html.push('			<div class="pa-row pa-status ready-not-available-color">Not Available</div>');
					} else if (requestedQty <= pnaAvail['0']) {
						html.push('			<div class="pa-row pa-status ready-immediate-color">Ready to Ship</div>');
					} else if (requestedQty <= pnaAvail['1']) {
						html.push('			<div class="pa-row pa-status ready-next-day-color">Ready to Ship Next Day</div>');
					} else if (requestedQty <= pnaAvail['total']) {
						html.push('			<div class="pa-row pa-status ready-two-day-color">Ready to Ship Two Plus Days</div>');
					} else if (pnaAvail['total'] > 0 && requestedQty > pnaAvail['total']) {
						html.push('			<div class="pa-row pa-status ready-partial-available-color">Partial Quantity Available</div>');
					}
				}
				
				html.push('					<div class="pa-row">');
				html.push('						<div class="col-1 bold">Next Day:</div>');
				html.push('						<div class="col-2 bold">', numberWithCommas(pnaAvail['1']), '</div>');
				html.push('					</div>');
				html.push('					<div class="pa-row">');
				html.push('						<div class="col-1">2+ Days:</div>');
				html.push('						<div class="col-2">', numberWithCommas(pnaAvail['2']), '</div>');
				html.push('					</div>');
				html.push('					<div class="pa-row">');
				html.push('						<div class="col-1">Total Available:</div>');
				html.push('						<div class="col-2">', numberWithCommas(pnaAvail['total']), '</div>');
				html.push('						<div class="col-3">', data.uomDescriptions[pnaItem.requestedQtyUOM], '</div>');
				html.push('					</div>');
				html.push('					<div class="pa-row pa-location">', numberWithCommas(pnaAvail['0']), ' ', data.uomDescriptions[pnaItem.requestedQtyUOM], ' available today at ', data.divisionName, '</div>');
				html.push('				</div>'); // close avail-wrap
				html.push('			</div>'); // close pa-avail
				
				if (showBracket) {
					html.push('		<div class="pa-bracket">');
					html.push('			<h4> Bracket Pricing (', pricingForItem.priceCurrencyCode, ') </h4>');
					html.push('			<div class="bracket-wrap">');
					
					for (var j = 0, lenj = pricingForItem.bracketsPricingList.length; j < lenj; j++) {
						var bracket = pricingForItem.bracketsPricingList[j];
						
						var formattedPrice = parseFloat(bracket.bracketPrice).toFixed(5) + "";
						formattedPrice = numberWithCommas(formattedPrice);
						
						var formattedUom = bracket.bracketUOM;
						if (formattedUom.indexOf('M_') == 0) {
							formattedUom = formattedUom.substring(2);
						}
						
						// do NOT use bracket.brackUOM here - use 1st displayPriceForUoms's bracketUOM. this is usually 'Thousand' but can be other such as 'Hundred' or 'Box', etc.
						html.push('			<div class="pa-row">');
						html.push('				<div class="col-1">', bracket.bracketQTY, ' ', formattedUom, '</div>');
						html.push('				<div class="col-2">-&nbsp;', formattedPrice, ' / ', pricingForItem.displayPriceForUoms[0].bracketUOM, '</div>');
						html.push('			</div>');
					}
					html.push('			</div>'); // close bracket-wrap
					html.push('		</div>'); // close pa-bracket
				}
				
				if (showPricing) {
					html.push('		<div class="pa-price">');
					html.push('			<h4 class="pa-last"> Price (', pricingForItem.priceCurrencyCode, ') </h4>');
					html.push('			<div class="price-wrap">');
					
					for (var j = 0, lenj = pricingForItem.displayPriceForUoms.length; j < lenj; j++) {
						var displayPriceForUom = pricingForItem.displayPriceForUoms[j];
						var isZero = displayPriceForUom.bracketPrice.indexOf('$0.') != -1;
						if (j < lenj - 1) {
							// bracket price
							html.push('		<div class="pa-row">');
							html.push('			<div class="col-1 bold">', (j == 0 ? 'Price:' : '&nbsp;'), '</div>');
							html.push('			<div class="col-2">');
							if (isZero) {
								html.push(			'<span class="pa-price-tbd-color">Call for price</span>');
							} else {
								html.push(			displayPriceForUom.bracketPrice, ' / ', displayPriceForUom.bracketUOM);
							}
							html.push('			</div>'); // close col-2 (bracket price)
							html.push('		</div>'); // close pa-row (bracket price)
						} else {
							// last row is extended price
							html.push('		<div class="pa-row">');
							html.push('			<div class="col-1 bold">Extended Price:</div>');
							html.push('			<div class="col-2">');
							if (isZero) {
								html.push(			'<span class="pa-price-tbd-color">To be determined</span>');
							} else {
								html.push(			displayPriceForUom.bracketPrice);
							}
							html.push('			</div>'); // close pa-row (extended price)
							html.push('		</div>'); // close col-2 (extended price)
						}
					}
					html.push('			</div>'); // close pa-price
					html.push('		</div>'); // close pa-wrap
				}
				
				$divItemAvailability.show().get(0).innerHTML = html.join('');
			}
		},
		failure: function(jqXHR, textStatus) {
			if (console) { console.log('ajax failure:\njqXHR:\n' , jqXHR , '\ntextStatus:\n' , textStatus); }
		}
	});
}

/**
 * @param warehouseLocationList
 * @return Returns an object with properties: '0', '1', '2', 'total'.
 *         The value for each property is the quantity (as a float) for the corresponding number of days: 0=Immediate, 1=Next Day, 2=2+ Days, total=Total
 */
function calculateAvailability(warehouseLocationList) {
	var qtys = {
			'0': 0,
			'1': 0,
			'2': 0,
			'total': 0
	};
	
	for (var i = 0, len = warehouseLocationList.length; i < len; i++) {
		var warehouse = warehouseLocationList[i];
		var numQty = parseFloat(warehouse.availableQty);
		
		var numDays = warehouse.numberOfDays;
		if (parseInt(numDays) > 2) {
			// treat 3 day qty as 2+ day qty
			numDays = '2';
		}
		
		qtys[numDays] += numQty;
		qtys['total'] += numQty;
	}
	
	// next day includes immediate
	qtys['1'] += qtys['0'];
	
	// workaround for inexactness of JavaScript floating point math - we must explicitly round after adding
	for (var i = 0, len = qtys.length; i < len; i++) {
		qtys[i] = parseFloat(qtys[i].toFixed(5));
	}
	
	return qtys;
}

/**
 * @param value
 * @returns
 * @author http://stackoverflow.com/questions/1219860/html-encoding-in-javascript-jquery
 */
function htmlEncode(value){
	//create a in-memory div, set it's inner text(which jQuery automatically encodes)
	//then grab the encoded contents back out.  The div never exists on the page.
	return $('<div/>').text(value).html();
}

/**
 * Formats number with commas.
 * @author http://stackoverflow.com/questions/2901102/how-to-print-a-number-with-commas-as-thousands-separators-in-javascript
 */
function numberWithCommas(num) {
    var parts = num.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}
