/**
 * @param items array containing item ids
 * @param qtys array containing quantities
 * @param uoms array containing units of measure
 */
function getPriceAndAvailabilityForItems(items, qtys, uoms) {
	var waitMsg = Ext.Msg.wait("Processing...");
	myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
	myMask.show();
	var url = $('#getPriceAndAvailabilityForItemsURL').val();
	
	if (!qtys) {
		qtys = [];
		for (var i = 0, len = items.length; i < len; i++) {
			var qty = $('#Qty_' + items[i]).val();
			if (qty.trim().length == 0) {
				qty = '1';
			}
			qtys.push(qty);
		}
	}
	
	if (!uoms) {
		uoms = [];
		for (var i = 0, len = items.length; i < len; i++) {
			var uom = $('#itemUomList_' + items[i]).val();
			uoms.push(uom);
		}
	}
	
	$.ajax({
		url: url,
		dataType: 'json',
		data: {
			'stockCheckAll': true,
			'validateOM': true,
			'items': items.join('*'),
			'qtys': qtys.join('*'),
			'uoms': uoms.join('*')
		},
		complete: function() {
			Ext.Msg.hide();
			myMask.hide();
		},
		success: function(data) {
			var pna = data.priceAndAvailability;
			var pricingInfo = data.pricingInfo;
			
			if (!pna) {
				console.log('Failed to retrieve price and availability');
				return;
			}
			// pna.transactionStatus != 'P' is error condition
			
			for (var i = 0, len = pna.items.length; i < len; i++) {
				var pnaItem = pna.items[i];
				
				var $divItemAvailability = $('#availabilty_' + pnaItem.legacyProductCode);
				if ($divItemAvailability.length == 0) {
					console.log('Element not found: #availabilty_' + pnaItem.legacyProductCode);
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
				
				var pnaAvail = calculateAvailability(pnaItem);
				
				var html = [];
				html.push('		<div class="pa-wrap ', columnMode, '">');
				html.push('			<div class="pa-avail">');
				html.push('				<h4>Availability</h4>');
				html.push('				<div class="avail-wrap">');
				
				var requestedQty = parseFloat(pnaItem.requestedQty);
				
				if (pnaAvail['total'] == 0) {
					html.push('				<div class="pa-row pa-status ready-not-available-color">Not Available</div>');
				} else if (requestedQty <= pnaAvail['0']) {
					html.push('				<div class="pa-row pa-status ready-immediate-color">Ready to Ship</div>');
				} else if (requestedQty <= pnaAvail['1']) {
					html.push('				<div class="pa-row pa-status ready-next-day-color">Ready to Ship Next Day</div>');
				} else if (requestedQty <= pnaAvail['total']) {
					html.push('				<div class="pa-row pa-status ready-two-day-color">Ready to Ship Two Plus Days</div>');
				} else if (pnaAvail['total'] > 0 && requestedQty > pnaAvail['total']) {
					html.push('				<div class="pa-row pa-status ready-partial-available-color">Partial Quantity Available</div>');
				} else {
					html.push('				<div class="pa-row pa-status ready-not-available-color"></div>');
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
						html.push('			<div class="pa-row">');
						html.push('				<div class="col-1">', bracket.bracketQTY, ' ', bracket.bracketUOM, '</div>');
						html.push('				<div class="col-2">-&nbsp;', formattedPrice, ' / Thousand</div>'); // TODO format with 5 decimals
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
			console.log('ajax failure:\njqXHR:\n' , jqXHR , '\ntextStatus:\n' , textStatus);
		}
	});
}

/**
 * @param pnaItem
 */
function calculateAvailability(pnaItem) {
	var qtys = {
			'0': 0,
			'1': 0,
			'2': 0,
			'total': 0
	};
	
	for (var i = 0, len = pnaItem.warehouseLocationList.length; i < len; i++) {
		var warehouse = pnaItem.warehouseLocationList[i];
		var numQty = parseFloat(warehouse.availableQty);
		qtys[warehouse.numberOfDays] += numQty;
		qtys['total'] += numQty;
	}
	
	qtys['1'] += qtys['0']; // next day total includes two day
	
	// round to 1 decimal place if necessary
	if ((qtys['0'] + '').indexOf('.') != -1) {
		qtys['0'] = qtys['0'].toFixed(1);
	}
	if ((qtys['1'] + '').indexOf('.') != -1) {
		qtys['1'] = qtys['1'].toFixed(1);
	}
	if ((qtys['2'] + '').indexOf('.') != -1) {
		qtys['2'] = qtys['2'].toFixed(1);
	}
	if ((qtys['total'] + '').indexOf('.') != -1) {
		qtys['total'] = qtys['total'].toFixed(1);
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
