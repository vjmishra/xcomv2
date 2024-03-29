/**
 * This is main java script function for change/select ship-to. Automatically populate below html code.
 * @param titleOfModal Title of Modal
 * @param customerContactId user id
 * @param getAssignedShipToURL. this url for getting the shiptos
 * @param includeShoppingForAndDefaultShipTo : this is flag to show Preferred Ship-to/shopping for.
 * @param cancelShipToChanges Cancel function, send null if not needed cancel functionality.
 * @param applyShipToChanges Apply function, send null if not needed Apply functionality.
 * @param selectShipToChanges Select function, send null if not needed select functionality.
 * @returns
 */
function showShiptos(titleOfModal, customerContactId, getAssignedShipToURL, includeShoppingForAndDefaultShipTo, cancelShipToChanges, applyShipToChanges, selectShipToChanges, applyNoShioTo) {
	function isEmpty(str) {
		return (!str || 0 === str.trim().length);
	}

function reset() {
		$('#ship-container').get(0).innerHTML = '';
	}

	reset();
	showProcessingIcon();
	var url = getAssignedShipToURL;
	$.ajax({
		type : 'POST',
		proccessData : false,
		url : url,
		dataType : 'json',
		data : {
			'customerContactId' : customerContactId
			,'includeShoppingForAndDefaultShipTo':includeShoppingForAndDefaultShipTo
		},
		success : function(data) {

			if (data.shipToList) {	
				var defaultShipToCustomer;
				var defaultShipToAddressList;
				var shoppingForShipToCustomer;
				var shoppingForAddressList; 					
				var $shipTosContainerDiv = $('#ship-container');
				var html = [];
				if (titleOfModal){
					html.push('		<h1>',	titleOfModal,	'</h1>');
				}
				html.push('			<input type="hidden" name="userIdForSelectOrChangeShipTo" id="userIdForSelectOrChangeShipTo" value="', customerContactId, '"/>');

				if(includeShoppingForAndDefaultShipTo=='true'){
					if (data.defaultShipToCustomer) {
						defaultShipToCustomer = data.defaultShipToCustomer;
						defaultShipToAddressList = data.defaultShipToCustomer.addressList;
					}
					if (data.shoppingForShipToCustomer) {
						shoppingForShipToCustomer = data.shoppingForShipToCustomer;
						shoppingForAddressList = data.shoppingForShipToCustomer.addressList;
					}
					if (data.shoppingForShipToCustomer && data.defaultShipToCustomer) {
						html.push('		 <div class="ship-current-wrap">');					
						html.push('			<h3>Currently Shopping for:</h3>');

						html.push('			 <p>');	
						html.push(					shoppingForShipToCustomer.customerID.replace("-M-XX-S",""), '<br/>');
						html.push(					shoppingForShipToCustomer.organizationName,'<br/>');
						if(!isEmpty(shoppingForShipToCustomer.locationID)){
							html.push(				shoppingForShipToCustomer.locationID, '<br/>');
						}
						for (var i = 0, len = shoppingForAddressList.length; i < len; i++) {
							if(!isEmpty(shoppingForAddressList[i])){
								html.push(					shoppingForAddressList[i], '<br/>');
							}
						}					
						html.push(				shoppingForShipToCustomer.city, ', ', shoppingForShipToCustomer.state, ' ', shoppingForShipToCustomer.zipCode, ' ', shoppingForShipToCustomer.country, '<br/>');
						html.push('			</p>');
						html.push('		 </div>');

						html.push('		 <div class="ship-preferred-wrap">');
						html.push('			<h3>Preferred Ship-To:</h3>');
						if (data.defaultShipToCustomer) {
							html.push('			 <p>');
							html.push(				defaultShipToCustomer.customerID.replace("-M-XX-S",""), '<br/>');	
							html.push(				defaultShipToCustomer.organizationName, '<br/>');
							if(!isEmpty(defaultShipToCustomer.locationID)){
								html.push(					defaultShipToCustomer.locationID, '<br/>');
							}
							for (var j = 0, len = defaultShipToAddressList.length; j < len; j++) {
								if(!isEmpty(defaultShipToAddressList[j])){
									html.push(		defaultShipToAddressList[j], '<br/>');
								}
							}
							html.push(				defaultShipToCustomer.city, ', ', defaultShipToCustomer.state, ' ', defaultShipToCustomer.zipCode, ' ', defaultShipToCustomer.country, '<br/>');
							html.push('			 </p>');
						}
						html.push('			 <div class="preferred-select">');
						html.push('		     	<input type="checkbox" name="setAsDefault" class="change-preferred-ship-to" id="setAsDefault"');
						if (data.defaultShipToCustomer && "30" === data.defaultShipToCustomer.customerStatus) {
							html.push('				checked', ' ', 'disabled');						
						}
						html.push('				/>')
						html.push('		     	<div>Change Preferred Ship-To to Selected</div>');
						html.push('		     </div>');						
						html.push('		 </div>');
					}else{
						html.push('			 <div class="preferred-select" style="display:none">');
						html.push('		     	<input type="checkbox" name="setAsDefault" class="change-preferred-ship-to" id="setAsDefault" checked');
						html.push('				/>')
						html.push('		     	<div>Change Preferred Ship-To to Selected</div>');
						html.push('		     </div>');
					}					
				}
				html.push('		    <div class="clearfix"></div>');	
				html.push('			<div class="ship-search-wrap">');
				if (data.defaultShipToCustomer && "30" === data.defaultShipToCustomer.customerStatus) {
					html.push('				<div><h3>Please select an active ship-to as your default.<h3></div>');
				}
				if (((!data.defaultShipToCustomer) || (data.defaultShipToCustomer && "30" !== data.defaultShipToCustomer.customerStatus)) && (!selectShipToChanges)) {					
					html.push('				<div class="notice float-right addmarginbottom10 shipToErrTxt" >Changing the Ship-To could impact pricing on orders.</div>');
				}else{
					html.push('				<div class="float-right addmarginbottom10 shipToErrTxt" ></div>');	
				}
				html.push('				<div class="clearfix"></div>');	
				html.push('				<div class="ship-button-wrap">');				
				if (applyShipToChanges && typeof(applyShipToChanges) === "function") { 				
					html.push('				<input class="btn-gradient floatright" type="submit" value="Apply" name="applyShipToButton"/>');			
				}
				if (selectShipToChanges && typeof(selectShipToChanges) === "function") {  
					html.push('				<input class="btn-gradient floatright" type="submit" value="Select" name="selectShipToButton"/>');
				}
				if (cancelShipToChanges && typeof(cancelShipToChanges) === "function") { 
					html.push('				<input type="submit" value="Cancel" class="btn-neutral floatright margin-right-10" name="cancelShipToButton" />');
				}
				html.push('				</div>');


				html.push('				<div id="header" class="ship-search-input-wrap"></div>');
				html.push('				<div id="ship-list-wrap"></div>');
				html.push('				<hr class="divider-line addmargintop10"/>');

				html.push('				<div class="ship-button-wrap">');

				if (applyShipToChanges && typeof(applyShipToChanges) === "function") {					
					html.push('					<input class="btn-gradient floatright" type="submit" value="Apply" name="applyShipToButton"/>');
				}
				if (selectShipToChanges && typeof(selectShipToChanges) === "function") {  
					html.push('					<input class="btn-gradient floatright" type="submit" value="Select" name="selectShipToButton"/>');
				}
				if (cancelShipToChanges && typeof(cancelShipToChanges) === "function") { 
					html.push('					<input class="btn-neutral floatright margin-right-10" type="submit" value="Cancel" name="cancelShipToButton"/>');
				}
				html.push('					</div>');
				html.push('					<div class="clearfix"></div>');
				if (((!data.defaultShipToCustomer) || (data.defaultShipToCustomer && "30" !== data.defaultShipToCustomer.customerStatus)) && (!selectShipToChanges)) {					
					html.push('			<div class="notice float-right addmargintop10 shipToErrTxt">Changing the Ship-To could impact pricing on orders.</div>');
				}
				else{
					html.push('			<div class="float-right addmargintop10 shipToErrTxt"></div>');
				}
				html.push('					<div class="clearfix"></div>');
				html.push('				</div>');
				$shipTosContainerDiv.get(0).innerHTML = html.join('');

				/*	Add Apply, select and cancel button click events  */

				if (applyShipToChanges && typeof(applyShipToChanges) === "function") {
					$("input[name='applyShipToButton']").click(applyShipToChanges);
				}
				if (selectShipToChanges && typeof(selectShipToChanges) === "function") {
					$("input[name='selectShipToButton']").click(selectShipToChanges);
				}
				if (cancelShipToChanges && typeof(cancelShipToChanges) === "function") {
					$("input[name='cancelShipToButton']").click(cancelShipToChanges);
				}

				// create search input box in header div
				var form = $("<form>").attr({"class":"filterform", "action":"#"}),
				input = $("<input>").attr({"class":"filterinput", "type":"text", "data-watermark":"Search"});
				$(form).append(input).append('<span class="resetlink">Reset</span>').appendTo($("#header"));
				do_watermark('.filterinput'); //add water mark
				// re-create ship-to list when user performs filter operation
				$('.filterform').submit(function() {
					var filter = $(this).find('.filterinput').val();
					displaySearchResults(filter);
					return false;
				});
				$('.resetlink').click(function() {
					$("input[class='filterinput']").val('');
					displaySearchResults('');
					do_watermark('.filterinput');
					return false;
				});
				function displaySearchResults(filter) {
					var shipToList = [];
					if (filter) {							
						for (var i = 0, len = data.shipToList.length; i < len; i++) {
							var shipTo = data.shipToList[i];
							if (shipTo.shipToDisplayString.toLowerCase().indexOf(filter.toLowerCase()) != -1) {
								shipToList.push(shipTo);
							}
						}

					} else {
						// no filter, so use full list
						shipToList = data.shipToList;
					}
					var shipToSelected=$("input[class='shipto-input']:checked").val();//ship To Selected before search


					var $shipListWrapDiv = $("#ship-list-wrap");
					var html = [];
					html.push('					<ul id="list">');
					for (var k = 0, len = shipToList.length; k < len; k++) {
						var shipTo = shipToList[k];
						var shipToCustomerId = shipTo.shipToCustomerID;
						var selectedShipToID = "selectedShipTo_"+shipToCustomerId; 
						var shipToMultiRowDisplayID = "shipToMultiRowDisplay_"+shipToCustomerId;
						html.push('					<li>');
						html.push('						<a class="underlink;">');
						html.push('							<input id="', selectedShipToID, '" type="radio" class="shipto-input" ' , (shipToSelected == shipToCustomerId ? 'checked' : ''), ' value="', shipTo.shipToCustomerID, '" name="selectedShipTo" />');
						html.push('							<label for="', selectedShipToID, '">', shipTo.shipToDisplayString, '</label>');
						html.push('						</a>');
						html.push('						<input type="hidden" name="shipToMultiRowDisplay" id="',shipToMultiRowDisplayID,'" value="',shipTo.shipToMultiRowDisplay,'"/>');
						html.push('					</li>');
					}
					html.push('					</ul>');
					$shipListWrapDiv.get(0).innerHTML = html.join('');
				} // end displaySearchResults

				displaySearchResults(''); //to show all ship-tos at the initial load

			} else if (applyNoShioTo && typeof(applyNoShioTo) === "function") {
				applyNoShioTo();
			}
		},
		error: function(resp, textStatus, xhr) {
			var $shipTosContainerDiv = $('#ship-container');
			$shipTosContainerDiv.get(0).innerHTML = '<div class="error">Currently unable get assigned Ship-Tos. Please try again later</div>';
			if (console) { console.log('showShiptos ajax error: resp = ', resp, '   textStatus = ', textStatus, '   xhr = ', xhr); }
		},
		complete: function(){			
			hideProcessingIcon();
		}
	});

}
