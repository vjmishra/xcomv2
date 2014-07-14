	if (!Array.prototype.indexOf)
	{
	  Array.prototype.indexOf = function(elt)
	  {
	    var len = this.length;
	
	    var from = Number(arguments[1]) || 0;
	    from = (from < 0)
	         ? Math.ceil(from)
	         : Math.floor(from);
	    if (from < 0)
	      from += len;
	
	    for (; from < len; from++)
	    {
	      if (from in this &&
	          this[from] === elt)
	        return from;
	    }
	    return -1;
	  };
	}
					
	indexOfFormEl = function(el, elt) {
	  if (!el){
	  	return -1;
	  }
	  var len = el.length;
	
	  var from = Number(arguments[1]) || 0;
	  from = (from < 0)
	       ? Math.ceil(from)
	       : Math.floor(from);
	  if (from < 0)
	    from += len;
	
	  if (len == undefined){
	  	if (el.value === elt){
	  		return 0;
	  	} else {
	  		return -1;
	  	}
	  }

	  for (; from < len; from++)
	  {
	    if (from in el &&
	        el[from].value === elt)
	      return from;
	  }
	  return -1;
	};

	function showForm(formId){
		//var dlgForm 		= document.getElementById("dlg" + formId);
		var dlgForm 		= Ext.get("dlg" + formId);
		
		if (dlgForm){
			//dlgForm.dom.style.display = "block";
			dlgForm.show();
		}
	}
	
	function hideForm(formId){
		//var dlgForm 		= document.getElementById("dlg" + formId);
		var dlgForm 		= Ext.get("dlg" + formId);
		
		if (dlgForm){
			//dlgForm.dom.style.display = "none";
			dlgForm.hide();
		}
	}
	
	function editForm(formId, key, name, desc, qty, jobId, order){
		//Populate fields
		var editForm 	= document.getElementById("formEdit");
		
		editForm.key.value 			= key;
		editForm.name.value 		= name;
		editForm.desc.value 		= desc;
		editForm.qty.value 			= qty;
		editForm.jobId.value 		= jobId;
		editForm.order.value 		= order;

		showForm(formId);
	}
	
	var isShareListLoaded = false;
	
	function showShareList(customerId, showRoot){
		//Populate fields
		var divMainId = "divMainShareList";
		var divMain 	= document.getElementById(divMainId);
		
		//Load the list if it has not been loaded before.
		//if (!isShareListLoaded){
			getShareList(customerId,"", divMainId, showRoot);
			isShareListLoaded = true;
		//}
		
		try{ console.log("showShareList: Customer ID, ShowRoot ", customerId, showRoot); }catch(e){}
	}
	
	function ReplaceAll(Source,stringToFind,stringToReplace){
	  var temp = Source;
	    var index = temp.indexOf(stringToFind);
	        while(index != -1){
	            temp = temp.replace(stringToFind,stringToReplace);
	            index = temp.indexOf(stringToFind);
			}
	        return temp;
	}
	
	function getAllPandA(all){
		try{
			var formItemIds 	= document.getElementById("formItemIds");
			var prodIds			= formItemIds.elements["enteredProductIDs"];
			var prodUoms		= formItemIds.elements["uoms"];
			var prodSels		= formItemIds.elements["itemIds"];
			
			if (all == undefined){ all = true; }
					
			for (var i=0;i<prodIds.length;i++) {
				var prodId 	= prodIds[i].value;
				var prodUom = prodUoms[i].value;
				var prodSel = prodSels[i].checked;
				
				var divAvail = "divAvail_" + prodId;
				var divPrice = "divPrice_" + prodId;
				//Debug
				//alert("Prod Id: " + prodId + ", UOM: " + prodUom + ", Selected: " + prodSel);
				
				if (all == true || (all == false && prodSel == true) ){
					getAvail(prodId, prodUom, divAvail);
					getPrice(prodId, prodUom, divPrice);
				}
			}
		}catch(err){
			alert("Error in getAllPandA : " + err);
		}
	}
	


	//qaTable
	
	function qaDeleteRow(id) {
		var i = document.getElementById(id).rowIndex;
		document.getElementById('qaTable').deleteRow(i);
		return;
	}
function qaAddItem(jobId, qty, itemId, itemType, purchaseOrder, itemTypeText){
	//Validate the data
		try{
			if (mandatoryFieldValidation('quick-add', []) != "")
				return;
		}catch(err){
		}
		
		//Inser the row
		var qaTable = document.getElementById('qaTable');
		var max 	= qaTable.rows.length;
		
		var row 	= qaTable.insertRow(max);
		
		var rowId	= "qaRow_" + max;
		row.id 		= rowId;
		
		var cAction		= row.insertCell(0);
		var cItemType	= row.insertCell(1);
		//var cItemId		= row.insertCell(2);
		//var cQty		= row.insertCell(3);
		//var cUOM		= row.insertCell(4);
		//var cJobid		= row.insertCell(5);
		//var cPO			= row.insertCell(6);
		
		var divId 		= "qaItem_" + max;
		
		//cItemType.colSpan 		= 5;
		cItemType.innerHTML 	= '<div id="'+divId+'"> </div> ';
		cAction.className		= "wing-col-item";
		cAction.align 			= "center";
		cAction.innerHTML 		= '<a class="del-quick-add" href="javascript:void(0);" onclick="javascript: qaDeleteRow(\''+rowId+'\');"><input type="image" src="' + XPEDXWCUtils_STATIC_FILE_LOCATION + '/xpedx/images/icons/12x12_red_x.png" /></a>';
		
		qaValidateItem(divId, jobId, qty, itemId, itemType, purchaseOrder, itemTypeText);
		
		
		//Clearing form selection
		document.getElementById("prodId").value= '';
		document.getElementById("qty").value= '';
		if(document.getElementById("jobId") != null)
		{
			document.getElementById("jobId").value= '';
		}
		if(document.getElementById("purchaseOrder") != null)
		{
			document.getElementById("purchaseOrder").value= '';
		}
		
	
	}
	
	function qaAddItem1(form){
		//Validate the data
		//Added For Jira 3197
		document.getElementById('errorMsgFor_QL').innerHTML = "";
		document.getElementById("errorMsgFor_QL").style.display = "none";
		// Code Fix For Jira 3197
		try{
			if (mandatoryFieldValidation('quick-add', []) != "")
				return;
		}catch(err){
		}
		
		var qty = form.qty.value;
		var prodId = form.prodId.value;
		var itemType =  form.itemType.value;
		var itemTypeText = form.itemType.options[form.itemType.selectedIndex].text;
		var jobId = '';
		var purchaseOrder = '';
		if(document.getElementById('jobId') != undefined){
			jobId = form.jobId.value;
		}
		if(document.getElementById('purchaseOrder') != undefined){
			purchaseOrder = form.purchaseOrder.value;
		}
		qaAddItem(jobId, qty, prodId, itemType, purchaseOrder, itemTypeText);
		//document.quickaddselector.itemType.selectedIndex = 0;
		//document.quickaddselector.prodId.value = '';
		//document.quickaddselector.qty.value = 1;
		//if(document.quickaddselector.purchaseOrder) {
		//	document.quickaddselector.purchaseOrder.value = '';
		//}
		//if(document.quickaddselector.jobId) {
		//	document.quickaddselector.jobId.value = '';
		//}
	}				
	
	function getShareList2(customerId, divId, depthLevel){
           //Init vars
           var url = "";
           
           //Execute the call
           document.body.style.cursor = 'wait';
           if(true){
           	document.body.style.cursor = 'default';
               var x = document.getElementById(divId);
               depthLevel++;
               //alert("depthLevel: " + depthLevel);
               var tmp1 = Math.random();
               var padding_left = 15 * depthLevel;
               var res = " " +
               	"<div style=\"padding-left: 15px; position: relative; display: block;\"  id='divShareList_"+tmp1+"_base'\"> Ship To for "+tmp1+" <br>" +
               		"<input type=\"button\" value=\"+\" onclick=\"getShareList2('1234', 'divShareList_"+tmp1+"', '"+depthLevel+"')\"/> " +
               		"<input type=\"checkbox\" name=\"customer_path\" value=\""+tmp1+1+"\"> Customer "+tmp1+1+"<br>" +
               		"<div id='divShareList_"+tmp1+"'\"> Second layer  TEXT HERE<br> </div>" +
               	"</div>" +	
               " ";
               x.innerHTML = res;
           
           }
       	document.body.style.cursor = 'default';

	}

	function showShareListChilds(customerId, divId){
		var childDiv 	= document.getElementById(divId);
		
		if (!childDiv) { alert("There is no div " + divId + "."); return; }
		if (childDiv.childNodes.length > 0){
			//Get and open the list of children
			getShareList(cutomerId, divId);
		} else {
			//Just open the list of children
		}
	}
	
	function updateShareListChild(customerId, controlId){
		var sslCustomerId 	= document.getElementById("customerIds_" + controlId);
		var sslCustomerPath = document.getElementById("customerPaths_" + controlId);
		var sslForm 	= document.getElementById("XPEDXMyItemsDetailsChangeShareList");
		var sslData		= sslForm.sslNames;
		
		try{
			//console.log("Looking for customerId " + customerId + " in ", sslData);	
		}catch(err){}
		var idx = indexOfFormEl(sslData, customerId); 
		if (idx > -1){
			try{
				console.log("Found " + customerId +", at loc: " + idx);
			}catch(err){}
			sslCustomerId.checked	= true;
			sslCustomerPath.checked = true;
			selectNode(controlId, true);
		}
	}
	
	function setAndExecute(divId, innerHTML) {  
	   var div = document.getElementById(divId);  
	   div.innerHTML = innerHTML;  
	   var x = div.getElementsByTagName("script");   
	   for( var i=0; i < x.length; i++) {  
	     eval(x[i].text);  
	   }  
	}
	
	function setAndExecuteForShareList(divId, innerHTML, isCustomerSelected) {  
		   var div = document.getElementById(divId);  
		   div.innerHTML = innerHTML;
		   saveShareListForChild(divId, isCustomerSelected);
		   var x = div.getElementsByTagName("script");   
		   for( var i=0; i < x.length; i++) {  
		     eval(x[i].text);  
		   }  
		}
	
	function saveShareListForChild(divId, isCustomerSelected){
		   var allChildElements = $("div[id="+divId + "] :checkbox");
        for(var j=0;j<allChildElements.length;j++)
			{
				var currentCB = allChildElements[j];
				currentCB.checked = isCustomerSelected;
			}
	}
	
	function showXPEDXReplacementItems(itemIDUOM, orderLineKey, order) {
		var source 		= document.getElementById("replacement_" + itemIDUOM);
	    var destination = document.getElementById("replacementItemBody");
	    
	    //Fill in data
	    destination.innerHTML = source.innerHTML;
	  
	  	//Add the key into the change form
	  	var form = Ext.get("formRIReplaceInList");
	  	form.dom.key.value = orderLineKey;
	    
	  	//Display the fancy box
		$.fancybox(
			Ext.get("replacementItems").dom.innerHTML,
			{
	        	'autoDimensions'	: true,
				'width'         	: '1035px',
				'height'        	: 'auto'
			}
		);	  	
	}
	
	function showSpecialItem(divId) {
	  	
	  	//Display the facy box
		var data = Ext.get(divId).dom.innerHTML;
		Ext.get(divId).dom.innerHTML = "";
		$.fancybox(
			data,
			{
	        	'autoDimensions'	: true,
				'width'         	: 350,
				'height'        	: 'auto',
				'onClosed'			: function() {
				    Ext.get(divId).dom.innerHTML = data;
				}				
			}
		);
	}
	
	
	function replacementAddToList(uId) {
		if (uId == ""){
			Ext.Msg.alert('Error', 'Please select a replacement item.');
			return;
		}
		
		//Get the form
		var form = Ext.get("formRIAddToList");
		
		//Get the rest of the data
		var itemid 	= Ext.get("replacement_" + uId + "_itemid").dom.value;
		var name 	= Ext.get("replacement_" + uId + "_name").dom.value;
		var desc 	= Ext.get("replacement_" + uId + "_desc").dom.value;
		
		var uom 	= document.getElementById("replacement_" + uId + "_uom").value;
		var order 	= parseInt(form.dom.itemCount.value,10);
	
		//Transfer the values
		form.dom.itemId.value 	= itemid;
		form.dom.name.value 	= name;
		form.dom.desc.value 	= desc;
		//form.dom.qty.value 		= qty;
		form.dom.uomId.value 	= uom;
		form.dom.order.value 	= parseInt(order, 10) + 1;
	
		
		//Submit the form
		form.dom.submit();
		
		Ext.Msg.wait('Saving your selection... Please wait.');
	}

	function replacementReplaceInList(uId) {
		
		if (uId == ""){
			Ext.Msg.alert('Error', 'Please select a replacement item.');
			return;
		}
		
		//Get the form
		var form = Ext.get("formRIReplaceInList");
		
		//Get the rest of the data
		var itemid 	= Ext.get("replacement_" + uId + "_itemid").dom.value;
		var name 	= Ext.get("replacement_" + uId + "_name").dom.value;
		var desc 	= Ext.get("replacement_" + uId + "_desc").dom.value;
		
		var uom 	= document.getElementById("replacement_" + uId + "_uom").value;
		//Transfer the values
		form.dom.itemId.value 	= itemid;
		form.dom.name.value 	= name;
		form.dom.desc.value 	= desc;
		//form.dom.qty.value 		= qty;
		form.dom.uom.value 	= uom;
		
		form.dom.submit();
		
		Ext.Msg.wait('Saving your selection... Please wait.');
	}
	
	function updateUOM(uId, uom ) {
		var elemId = "replacement_"+uId+"_uom_modified";
		//Setting the _modified suffix element since dom binding is not happening in js
		document.getElementById(elemId).value = uom;
	}
	
	/*function updateQTY(uId, qty){
		if(qty=='' || qty=='0' || (!checkForValidQuantity(qty))){
			Ext.Msg.alert('Error', 'Please select a valid quantity.');
			return false;
		}
		var elemId = "replacement_"+uId+"_qty_modified";
		//Setting the _modified suffix element since dom binding is not happening in js
		document.getElementById(elemId).value = qty;
	}*/
	
	function updateQTY(uId, component){
		var qty = component.value; 
		if(!isValidQuantity(component)){
			Ext.Msg.alert('Error', 'Please select a valid quantity.');
			component.value = qty;
			return false;
		}
		qty = component.value;
		var elemId = "replacement_"+uId+"_qty_modified";
		//Setting the _modified suffix element since dom binding is not happening in js
		document.getElementById(elemId).value = qty;
	}
	
	
	
	