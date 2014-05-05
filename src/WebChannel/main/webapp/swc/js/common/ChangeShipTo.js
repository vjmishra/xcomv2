$(document).ready(function() {
	var customerContactId = $('#customerContactId').val();	
	var includeShoppingForAndDefaultShipTo = $('#includeShoppingForAndDefaultShipTo').val();
	var shipToURL = $('#applytargetURL').val();	
	var applyShipToChanges = function applyShipToChanges(){	

		if (!$("input[name='selectedShipTo']:checked").val()) {
			$('.shipToErrTxt').removeClass("notice").addClass("error");		
			$('.shipToErrTxt').text("Please select a Ship-To Location.");		
			return false;
		}
		var url = $('input[id=shipToURL]').val();
		if($('#setAsDefault').attr('checked')){
			url = url+"&setSelectedAsDefault=true";
		}	
		window.location.href = url+"&selectedCurrentCustomer="+$("input[name='selectedShipTo']:checked").val();
	};
	showShiptos(null,	customerContactId,	shipToURL,	includeShoppingForAndDefaultShipTo,	null,	applyShipToChanges,	null);
});