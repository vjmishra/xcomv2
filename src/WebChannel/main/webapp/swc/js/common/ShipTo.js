//create jquery expression ':Contains' for use with ship-to search plugin
jQuery.expr[':'].Contains = function(a,i,m){
	return (a.textContent || a.innerText || "").toUpperCase().indexOf(m[3].toUpperCase())>=0;
};

function showShiptos(customerContactId,shipToURL,includeShoppingForAndDefaultShipTo, cancelShipToChanges, applyShipToChanges, selectSaveShipToChanges) {
	var url = $('#getAssignedShipToCustomersURL').attr('href');
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
				var shipToList = data.shipToList;
				var defaultShipToCustomer;
				var defaultShipToAddressList;
				var shoppingForShipToCustomer;
				var shoppingForAddressList; 					
				var $ShipTosContainerDiv = $('#ship-container');
				var html = [];
				html.push('			<input type="hidden" name="userIdForSelectOrChangeShipTo" id="userIdForSelectOrChangeShipTo" value="',customerContactId,'"/>');
				html.push('			<input type="hidden" name="shipToURL" id="shipToURL" value="',shipToURL,'"/>');
				if(includeShoppingForAndDefaultShipTo=='true'){
					defaultShipToCustomer = data.defaultShipToCustomer;
					defaultShipToAddressList = data.defaultShipToCustomer.addressList;
					shoppingForShipToCustomer = data.shoppingForShipToCustomer;
					shoppingForAddressList = data.shoppingForShipToCustomer.addressList;	
					html.push('		 <div class="ship-current-wrap">');					
					html.push('			<h3>Currently Shopping for:</h3>');
					html.push('			 <p>');	
					html.push(					shoppingForShipToCustomer.customerID.replace("-M-XX-S",""),'<br/>');
					html.push(					shoppingForShipToCustomer.organizationName,'<br/>');
					if(!isEmpty(shoppingForShipToCustomer.locationID)){
						html.push(					shoppingForShipToCustomer.locationID,'<br/>');
					}
					for (var i = 0, len = shoppingForAddressList.length; i < len; i++) {
						if(!isEmpty(shoppingForAddressList[i])){
							html.push(					shoppingForAddressList[i],'<br/>');
						}
					}
					html.push(				shoppingForShipToCustomer.city,', ', shoppingForShipToCustomer.state,' ', shoppingForShipToCustomer.zipCode, ' ', shoppingForShipToCustomer.country,'<br/>');
					html.push('			 </p>');
					html.push('		 </div>');

					html.push('		 <div class="ship-preferred-wrap">');
					html.push('			<h3>Preferred Ship-To:</h3>');
					html.push('			 <p>');
					html.push(					defaultShipToCustomer.customerID.replace("-M-XX-S",""),'<br/>');	
					html.push(					defaultShipToCustomer.organizationName,'<br/>');
					if(!isEmpty(shoppingForShipToCustomer.locationID)){
						html.push(					shoppingForShipToCustomer.locationID,'<br/>');
					}
					for (var j = 0, len = defaultShipToAddressList.length; j < len; j++) {
						if(!isEmpty(defaultShipToAddressList[j])){
							html.push(					defaultShipToAddressList[j],'<br/>');
						}
					}
					html.push(				defaultShipToCustomer.city, ', ', defaultShipToCustomer.state,' ', defaultShipToCustomer.zipCode,' ', defaultShipToCustomer.country,'<br/>');
					html.push('			 </p>');
					html.push('			 <div class="preferred-select">');
					html.push('		     	<input type="checkbox" name="setAsDefault" class="change-preferred-ship-to" id="setAsDefault">');
					html.push('		     	<div>Change Preferred Ship-To to Selected</div>');
					html.push('		     </div>');
					html.push('		 </div>');
					html.push('		 <div class="clearfix"></div>');						
				}
				html.push('		 <hr class="divider-line addmargintop10">');
				html.push('		<div class="ship-search-wrap">');
				html.push('		 <div class="notice float-right shipToErrTxt" >Changing the Ship-To could impact pricing on orders.</div>');
				html.push('		 <div class="clearfix"></div>');
				html.push('		 <div class="float-right clearboth  addmargintop10">');
				if (applyShipToChanges && typeof(applyShipToChanges) === "function") {  
					html.push('			<input class="btn-gradient floatright" type="submit" value="Apply" onclick="javascript:applyShipToChanges();">');
				}
				if (selectSaveShipToChanges && typeof(selectSaveShipToChanges) === "function") {  
					html.push('			<input class="btn-gradient floatright" type="submit" value="Select" onclick="javascript:selectSaveShipToChanges();">');
				}
				if (cancelShipToChanges && typeof(cancelShipToChanges) === "function") { 
					html.push('			<input type="submit" value="Cancel" class="btn-neutral floatright margin-right-10" onclick="javascript:$.fancybox.close();" >');
				}
				html.push('		</div>');

				html.push('						 <div id="header"></div>')				
				html.push('								<ul id="list">');
				for (var k = 0, len = shipToList.length; k < len; k++) {
					var shipTo = shipToList[k];
					var shipToCustomerId = shipTo.shipToCustomerID;
					var selectedShipToID = "selectedShipTo_"+shipToCustomerId; 
					html.push('								<li> <a href="javascript:return false;">');
					html.push('    							 	<input id="',selectedShipToID,'" type="radio" value=',shipTo.shipToCustomerID,' name="selectedShipTo" />');
					html.push('									 	<label for="',selectedShipToID,'">',shipTo.shipToDisplayString, '</label>');
					html.push('									 </a>');
					html.push('								</li>');
				}
				html.push('								</ul>');
				html.push('								 <hr class="divider-line addmargintop10">');
				html.push('								 <div class="float-right clearboth  addmargintop10">');
				if (applyShipToChanges && typeof(applyShipToChanges) === "function") {  
					html.push('									<input class="btn-gradient floatright" type="submit" value="Apply" onclick="javascript:applyShipToChanges();">');
				}
				if (selectSaveShipToChanges && typeof(selectSaveShipToChanges) === "function") {  
					html.push('									<input class="btn-gradient floatright" type="submit" value="Select" onclick="javascript:selectSaveShipToChanges();">');
				}
				if (cancelShipToChanges && typeof(cancelShipToChanges) === "function") { 
					html.push('									<input class="btn-neutral floatright margin-right-10" type="submit" value="Cancel" onclick="javascript:$.fancybox.close();">');
				}
				html.push('								</div>');
				html.push('								<div class="clearfix"></div>');
				html.push('								<div class="notice float-right addmargintop10 shipToErrTxt">Changing the Ship-To could impact pricing on orders.</div>');
				html.push('								<div class="clearfix"></div>');
				html.push('							</div>');
				$ShipTosContainerDiv.show().get(0).innerHTML = html.join('');

				// create search box
				var form = $("<form>").attr({"class":"filterform","action":"#"}),
				input = $("<input>").attr({"class":"filterinput","type":"text"});
				$(form).append(input).appendTo($("#header"));

				$(input)
				.change( function () {
					var filter = $(this).val();
					if(filter) {
						$('#list').find("a:not(:Contains(" + filter + "))").parent().slideUp();
						$('#list').find("a:Contains(" + filter + ")").parent().slideDown();
					} else {
						$('#list').find("li").slideDown();
					}
					return false;
				})
				.keyup( function () {
					$(this).change();
				});
			}
		},
		error : function(resp, textStatus, xhr) {
		}
	});
}


function applyShipToChanges(){	

	if (!$("input[name='selectedShipTo']:checked").val()) {
		$('.shipToErrTxt').removeClass("notice").addClass("error");		
		$('.shipToErrTxt').show().get(0).innerHTML = "Please select a Ship-To Location.";
		$('.shipToErrTxt').show().get(1).innerHTML = "Please select a Ship-To Location.";
		return false;
	}
	var url = $('input[name=shipToURL]').val();
	if($('#setAsDefault').attr('checked')){
		url = url+"&setSelectedAsDefault=true";
	}	
	window.location.href = url+"&selectedCurrentCustomer="+$("input[name='selectedShipTo']:checked").val();
}

function cancelShipToChanges(){
	$.fancybox.close();
}

function selectSaveShipToChanges()    
{  
	//TODO need to implement actuval functionality
	$.fancybox.close();

}
function isEmpty(str) {
	return (!str || 0 === str.trim().length);
}