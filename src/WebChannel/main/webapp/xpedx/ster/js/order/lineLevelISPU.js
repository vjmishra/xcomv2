	  function updatePage()
	  {
			document.PickupStoreForm.update.value= "true";			
			document.PickupStoreForm.zipCode.value= document.LineLevelISPU.zipCode.value;
			document.PickupStoreForm.country.value= document.LineLevelISPU.country.value;
			if(document.LineLevelISPU.searchDistance){
				document.PickupStoreForm.searchDistance.value= document.LineLevelISPU.searchDistance.value;
			}
			document.PickupStoreForm.submit();	
	  }

	  function removeErrorMsg(){
			var errorMsg = document.getElementById("errorMsg");
			errorMsg.innerHTML = "";
			svg_classhandlers_decoratePage();
	  }	  
	  
	  function showStoreInfo(thisVal, linekey) {
			removeErrorMsg();
			removeOriginalValues(linekey);
			sv=thisVal.options[thisVal.selectedIndex].value;
			var selectedDOM = Ext.DomQuery.selectNode("#selectedShipNode_"+linekey);
			selectedDOM.value = sv;
			if (sv==""){
				var destDivNm = "StoreInfoDestDiv_"+linekey;
				var destDivObj = document.getElementById(destDivNm);
				destDivObj.innerHTML = "";
				var pickupDate = document.getElementById("pickupDates_"+linekey);
				pickupDate.value = "";
			} else {				
				var divID = linekey+"_"+sv;
				var pickupDateDivObj = document.getElementById(divID);
				var pickupDateStr = document.getElementById("Pickup_Date").innerHTML+pickupDateDivObj.innerHTML;
				var dateDisplayStr ='<span class="left">' + pickupDateStr + '</span><br>';
				var destDivNm = "StoreInfoDestDiv_"+linekey;
				var srcDivNm = "StoreInfoSrcDiv_"+sv;
				var destDivObj = document.getElementById(destDivNm);
				var srcDivObj = document.getElementById(srcDivNm);
				destDivObj.innerHTML = srcDivObj.innerHTML+dateDisplayStr;
				var pickupDate = document.getElementById("pickupDates_"+linekey);
				pickupDate.value = pickupDateDivObj.className;
		   }
		   svg_classhandlers_decoratePage();
		}

	    function removeOriginalValues(linekey) {
			var shipNode = document.getElementById("originalShipNodes_"+linekey);
			var reqShipDate = document.getElementById("originalPickupDates_"+linekey);
			shipNode.value = "";
			reqShipDate.value = "";			
		}
		
		

		function goNext(){
			var selectedShipNodes = document.PickupStoreForm.selectedShipNode;			
			if(noOfLines != 1){
				for(var index = 0; index < selectedShipNodes.length ; index++){
					var shipNode = selectedShipNodes[index].value;
					var lineKey = selectedShipNodes[index].className;
					var sDelMethod = Ext.DomQuery.selectNode(".deliveryMethod_"+lineKey);
					//var sDelMethod = delMethod[index];
					var selectedDelMethod = sDelMethod.options[sDelMethod.selectedIndex].value;
					if(selectedDelMethod == ""){
						var noDelMethodSelected = document.getElementById("No_delivery_method_selected");
						var errorMsg = document.getElementById("errorMsg");
						errorMsg.innerHTML = noDelMethodSelected.innerHTML;
						svg_classhandlers_decoratePage();
						return;
					}
					if(selectedDelMethod == "SHP"){
						continue;
					}
					if(selectedDelMethod == "PICK" && shipNode == ""){
						var selectAStore = document.getElementById("select_a_store");
						var errorMsg = document.getElementById("errorMsg");
						errorMsg.innerHTML = selectAStore.innerHTML;
						svg_classhandlers_decoratePage();
						return;
					}
				}
				document.PickupStoreForm.submit();
			}else{
				var delMethod = Ext.DomQuery.selectNode(".deliverymethodSelectOption");
				var selectedDelMethod = delMethod.options[delMethod.selectedIndex].value;
				var shipNode = selectedShipNodes.value;								
				if(selectedDelMethod == ""){
						var noDelMethodSelected = document.getElementById("No_delivery_method_selected");
						var errorMsg = document.getElementById("errorMsg");
						errorMsg.innerHTML = noDelMethodSelected.innerHTML;
						svg_classhandlers_decoratePage();
						return;
				}
				if(shipNode == "" && selectedDelMethod == "PICK"){
					var selectAStore = document.getElementById("select_a_store");
					var errorMsg = document.getElementById("errorMsg");
					errorMsg.innerHTML = selectAStore.innerHTML;
					svg_classhandlers_decoratePage();
					return;					
				}
				document.PickupStoreForm.submit();				
			}				
		}


		function moveLines(thisVal,lineKey){
			removeErrorMsg();
			var deliveryMethod=thisVal.options[thisVal.selectedIndex].value;
			if(deliveryMethod==""){
				changeDeliveryMethod(deliveryMethod,lineKey);
				svg_classhandlers_decoratePage();
				return;
			}			
			var groupID = document.getElementById("groupID_"+lineKey);
			var targetContainer = "";
			if(groupID.value == "PICKUP" ){
				if(deliveryMethod=="PICK"){
					svg_classhandlers_decoratePage();
					return;
				}
				if(noOfPickupLines == 1){
					var errorMsg = document.getElementById("cannot_move_all_lines_to_shipping");
					var errorDiv = document.getElementById("errorMsg");
					errorDiv.innerHTML = errorMsg.innerHTML;
					resetDeliveryMethod(thisVal);					
					svg_classhandlers_decoratePage();
					return;
				}
				if(noOfPickupLines == noOfLines){
					generateEmptyTable();					
				}
				targetContainer = "otherItemContainer";
				hideShipNodeSelection(lineKey,groupID);
			}else if(groupID.value == "OTHERS" ){
				if(deliveryMethod != "PICK"){
					svg_classhandlers_decoratePage();
					return;
				}
				targetContainer = "pickupItemContainer";
				makeShipNodeSelectionVisible(lineKey,groupID);
			}else{
				svg_classhandlers_decoratePage();
				return;
			}			
			changeDeliveryMethod(deliveryMethod,lineKey);
			toggleLine(thisVal,lineKey,targetContainer);			
			changesBasedOnGroupID(groupID,lineKey);			
			setDropDown();
			svg_classhandlers_decoratePage();			
		}

		function resetDeliveryMethod(thisVal){
			var pickupOptionIndex;
			for(var index = 0; index < thisVal.options.length ; index++){
				if(thisVal.options[index].value =="PICK"){
					pickupOptionIndex = index;
					break;
				}
			}
			thisVal.selectedIndex=pickupOptionIndex;
		}
		
		function toggleLine(thisVal,lineKey,targetContainer){
			var itemRow = Ext.DomQuery.selectNode(".lineItem_"+lineKey);
			var tableContainer = Ext.DomQuery.selectNode("."+targetContainer);
			var newdivSection = document.createElement('div');
			var newdivClassName = ".lineItem_"+lineKey;
			newdivSection.className = newdivClassName;
			newdivSection.appendChild(itemRow);
			tableContainer.innerHTML = tableContainer.innerHTML + newdivSection.innerHTML;
			itemRow.innerHTML = "";
			itemRow.parentNode.removeChild(itemRow);
		}

		function changeDeliveryMethod(deliveryMethod,lineKey){
			var selectedDOM = Ext.DomQuery.selectNode("#selectedDeliveryMethod_"+lineKey);
			selectedDOM.value = deliveryMethod;
		}

		function setDropDown(){
			selectShipNodeDropDown();
			selectDeliveryMethodDropDown();
			setGroupID();
		}
		
		function setGroupID(){
			var pickupContainer = Ext.DomQuery.selectNode(".pickupItemContainer");
			var groupIDs = Ext.DomQuery.select(".groupID",pickupContainer);
			for(var index = 0; index < groupIDs.length ; index++){
				groupIDs[index].value = "PICKUP";						
			}
			var otherContainer = Ext.DomQuery.selectNode(".otherItemContainer");
			var groupIDs = Ext.DomQuery.select(".groupID",otherContainer);
			for(var index = 0; index < groupIDs.length ; index++){
				groupIDs[index].value = "OTHERS";						
			}			
		}

		function selectShipNodeDropDown(){
			var selectedShipNodes = document.PickupStoreForm.selectedShipNode;
			if(noOfLines == 1){
				var shipNodeDOM = selectedShipNodes;
				var lineKey = shipNodeDOM.className;
				var shipNode = shipNodeDOM.value;
				var dropDownDOM = Ext.DomQuery.selectNode(".shipNode_"+lineKey);
				dropDownDOM.value = shipNode;
			}else{
				for(var index = 0; index < selectedShipNodes.length ; index++){
					var shipNodeDOM = selectedShipNodes[index];
					var lineKey = shipNodeDOM.className;
					var shipNode = shipNodeDOM.value;
					var dropDownDOM = Ext.DomQuery.selectNode(".shipNode_"+lineKey);
					dropDownDOM.value = shipNode;					
				}
			}
		}

		function selectDeliveryMethodDropDown(){
			var selectedDelMethods = document.PickupStoreForm.selectedDeliveryMethod;
			if(noOfLines == 1){
				var delMethodDOM = selectedDelMethods;
				var lineKey = delMethodDOM.className;
				var delMethod = delMethodDOM.value;
				var dropDownDOM = Ext.DomQuery.selectNode(".deliveryMethod_"+lineKey);
				dropDownDOM.value = delMethod;
			}else{
				for(var index = 0; index < selectedDelMethods.length ; index++){
					var delMethodDOM = selectedDelMethods[index];
					var lineKey = delMethodDOM.className;
					var delMethod = delMethodDOM.value;
					var dropDownDOM = Ext.DomQuery.selectNode(".deliveryMethod_"+lineKey);
					dropDownDOM.value = delMethod;					
				}
			}
		}

		function changesBasedOnGroupID(groupID,lineKey){
			if(groupID.value == "PICKUP" ){
				groupID.value = "OTHERS";
				noOfPickupLines = noOfPickupLines - 1;
				//make ship node blank, when shift from pickup to others.
				var selectedDOM = Ext.DomQuery.selectNode("#selectedShipNode_"+lineKey);
				selectedDOM.value = "";
			}else{
				groupID.value = "PICKUP";
				noOfPickupLines = noOfPickupLines + 1;
				if(noOfPickupLines == noOfLines){
					hideEmptyTable();
				}
			}
		}
		
		function hideEmptyTable(groupID){
			var REInvisibleClass = new RegExp('\\bmake-invisible\\b', 'gi');
			var hiddenContainer = Ext.DomQuery.selectNode(".hiddenOtherItemContainer");
			var otherItemMainContainer = Ext.DomQuery.selectNode(".otherItemMainContainer");
			
			var otherItemContainer = Ext.DomQuery.selectNode(".otherItemMainContainer > .otherItemContainer");
			
			var otherItemTableHeader = Ext.DomQuery.selectNode(".listTableHeader",otherItemContainer);
			
			if(!otherItemContainer.className.match(REInvisibleClass)){
				otherItemContainer.className += ' ' + 'make-invisible';				
			}
			if(otherItemTableHeader != null){
				otherItemTableHeader.className = otherItemTableHeader.className.replace('listTableHeader', 'listTableHeaderX');	
			}
			hiddenContainer.innerHTML = otherItemMainContainer.innerHTML;
			otherItemMainContainer.innerHTML = "";
		}

		function generateEmptyTable(){
			var REInvisibleClass = new RegExp('\\bmake-invisible\\b', 'gi');
			var hiddenContainer = Ext.DomQuery.selectNode(".hiddenOtherItemContainer");
			var otherItemMainContainer = Ext.DomQuery.selectNode(".otherItemMainContainer");
			var hiddenOtherItemContainer = Ext.DomQuery.selectNode(".hiddenOtherItemContainer > .otherItemContainer");
			var hiddenOtherItemTableHeader = Ext.DomQuery.selectNode(".listTableHeaderX",hiddenOtherItemContainer);
			if(hiddenOtherItemContainer.className.match(REInvisibleClass)){
				hiddenOtherItemContainer.className = hiddenOtherItemContainer.className.replace(REInvisibleClass, "");
			}
			if(hiddenOtherItemTableHeader != null){
				hiddenOtherItemTableHeader.className = hiddenOtherItemTableHeader.className.replace('listTableHeaderX', 'listTableHeader');	
			}
			otherItemMainContainer.innerHTML = hiddenContainer.innerHTML;
			hiddenContainer.innerHTML = "";
		}

		function hideShipNodeSelection(lineKey,groupID){
			var selectedShipNodes = Ext.DomQuery.selectNode("#shipNodeList_"+lineKey);
			var selectedShipAddress = Ext.DomQuery.selectNode("#StoreInfoDestDiv_"+lineKey);
			selectedShipAddress.innerHTML = "";
			selectedShipNodes.className = "make-invisible";			
		}

		function makeShipNodeSelectionVisible(lineKey,groupID){
			var selectedShipNodes = Ext.DomQuery.selectNode("#shipNodeList_"+lineKey);
			if(selectedShipNodes.className.match("make-invisible")){
				selectedShipNodes.className = selectedShipNodes.className.replace("make-invisible", "");
			}
			
		}

		Ext.onReady(function() {
			javascript:SVGAnnotator.make3dBarBackground('#navigateSingleCheckout');
			removeErrorMsg();
		});