$(document).ready(function() {
	getPriceAndAvailabilityForItems(['2263242']);
//	getPriceAndAvailabilityForItems(['5531440']);
});

/**
 * @param items array containing item ids
 * @param qtys array containing quantities
 * @param uoms array containing units of measure
 */
function getPriceAndAvailabilityForItems(items, qtys, uoms) {
	var url = $('#getPriceAndAvailabilityForItemsURL').val();
	
	clearPriceAndAvailability(items);
	
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
		success: function(data) {
			console.log('data = ' , data);
			var pna = data.priceAndAvailability;
			var pricingInfo = data.pricingInfo;
			
			if (!pna) {
				console.log('Failed to retrieve price and availability');
				return;
			}
			// pna.transactionStatus != 'P' is error condition
			
			for (var i = 0, len = pna.items.length; i < len; i++) {
				var pnaItem = pna.items[i];
				console.log('pnaItem = ' , pnaItem);
				
				var $divItemAvailability = $('#availabilty_' + pnaItem.legacyProductCode);
				if ($divItemAvailability.length == 0) {
					console.log('div not found: #availabilty_' + pnaItem.legacyProductCode);
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
				
				// pricingInfo is null when user lacks permission to see prices
				var pricingForItem = pricingInfo ? pricingInfo[pnaItem.legacyProductCode] : null;
				var showBracket = pricingInfo ? (pnaItem.brackets.length > 0) : false; // %{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y" && #category.trim().equals("Paper") && #_action.getValidateOMForMultipleItems() == "true" && #isBracketPricing == "true"}
				var showPricing = pricingInfo ? (pricingForItem.displayPriceForUoms.length > 0) : false; // %{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y" && #displayPriceForUoms.size() > 0}
				console.log('pricingForItem = ' , pricingForItem);
				
				var columnMode;
				if (showBracket && showPricing) {
					columnMode = 'three-col';
				} else if (showBracket || showPricing) {
					columnMode = 'two-col';
				} else {
					columnMode = 'one-col';
				}
				
				var pnaAvail = calculateAvailability(pnaItem);
				console.log('pnaAvail = ' , pnaAvail);
				
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
					var availableBalance = requestedQty - pnaAvail['total']; // TODO where does this message display?
					console.log('availableBalance = ' , availableBalance);
				} else {
					html.push('				<div class="pa-row pa-status ready-not-available-color"></div>');
				}
				
				html.push('					<div class="pa-row">');
				html.push('						<div class="col-1 bold">Next Day:</div>');
				html.push('						<div class="col-2 bold">', numberWithCommas(pnaAvail['1']), '</div>');
				html.push('					</div>');
				html.push('					<div class="pa-row">');
				html.push('						<div class="col-1">2+ Days:</div>');
				html.push('						<div class="col-2">', pnaAvail['2']), '</div>');
				html.push('					</div>');
				html.push('					<div class="pa-row">');
				html.push('						<div class="col-1">Total Available:</div>');
				html.push('						<div class="col-2">', pnaAvail['total']), '</div>');
				html.push('						<div class="col-3">', data.uomDescriptions[pnaItem.requestedQtyUOM], '</div>');
				html.push('					</div>');
				html.push('					<div class="pa-row pa-location">', pnaAvail['0']), ' ', data.uomDescriptions[pnaItem.requestedQtyUOM], ' available today at ', data.divisionName, '</div>');
				html.push('				</div>'); // close avail-wrap
				html.push('			</div>'); // close pa-avail
				
				
				if (showBracket) {
					html.push('			<div class="pa-bracket">');
					html.push('				<h4> Bracket Pricing (', pricingForItem.priceCurrencyCode, ') </h4>');
					html.push('				<div class="bracket-wrap">');
					html.push('					<div class="pa-row">');
					html.push('						<div class="col-1">40 M CTN</div>');
					html.push('						<div class="col-2">-&nbsp;$12.40000 / Thousand</div>');
					html.push('					</div>');
					html.push('					<div class="pa-row">');
					html.push('						<div class="col-1">10 M CTN</div>');
					html.push('						<div class="col-2">-&nbsp;$13.05000 / Thousand</div>');
					html.push('					</div>');
					html.push('					<div class="pa-row">');
					html.push('						<div class="col-1">1 M CTN</div>');
					html.push('						<div class="col-2">-&nbsp;$12.40000 / Thousand</div>');
					html.push('					</div>');
					html.push('					<div class="pa-row">');
					html.push('						<div class="col-1">1 M CTN</div>');
					html.push('						<div class="col-2">-&nbsp;$13.05000 / Thousand</div>');
					html.push('					</div>');
					html.push('				</div>');
					html.push('			</div>'); // close pa-bracket
				}
				
				if (showPricing) {
					html.push('			<div class="pa-price">');
					html.push('				<h4 class="pa-last"> Price (', pricingForItem.priceCurrencyCode, ') </h4>');
					html.push('				<div class="price-wrap">');
					
					for (var j = 0, lenj = pricingForItem.displayPriceForUoms.length; j < lenj; j++) {
						var displayPriceForUom = pricingForItem.displayPriceForUoms[j];
						if (displayPriceForUom.bracketPrice.indexOf('$0.') != -1) {
							// skip zero price
							continue;
						}
						if (j < lenj - 1) {
							// bracket price
							html.push('			<div class="pa-row">');
							html.push('				<div class="col-1 bold">', (j == 0 ? 'Price:' : '&nbsp;'), '</div>');
							html.push('				<div class="col-2">', displayPriceForUom.bracketPrice, ' / ', displayPriceForUom.bracketUOM, '</div>');
							html.push('			</div>');
						} else {
							// last row is extended price
							html.push('			<div class="pa-row">');
							html.push('				<div class="col-1 bold">Extended Price:</div>');
							html.push('				<div class="col-2">', displayPriceForUom.bracketPrice, '</div>');
							html.push('			</div>');
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
 * @param items array containing item ids
 * @returns
 */
function clearPriceAndAvailability(items) {
	var selectorList = [];
	for (var i = 0, len = items.length; i < len; i++) {
		selectorList.push('#availabilty_' + items[i]);
	}
	
	var itemAvailabilityDivs = $(selectorList.join(','));
	for (var i = 0, len = itemAvailabilityDivs.length; i < len; i++) {
		itemAvailabilityDivs[i].innerHTML = '';
	}
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
function numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}
