$(document).ready(function() {
	var customerContactId = $('#customerContactId').val();	
	var includeShoppingForAndDefaultShipTo = $('#includeShoppingForAndDefaultShipTo').val();
	var shipToURL = $('#applytargetURL').attr('href');
	showShiptos(customerContactId,shipToURL,includeShoppingForAndDefaultShipTo,'',applyShipToChanges,'');
});