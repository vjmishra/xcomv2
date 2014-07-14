function showXPEDXComplimentaryItems(itemIDUOM, orderLineKey, quantity)
{
	var source = document.getElementById("complimentary_" + itemIDUOM);
    var destination = document.getElementById("complimentaryItemBody");
    destination.innerHTML = source.innerHTML;
    
    document.addComplementaryItemForm.OrderLineKey.value = orderLineKey;
	DialogPanel.show("complimentaryItems");
    svg_classhandlers_decoratePage();

  	var initialFocus = Ext.get("complimentary_" + itemIDUOM + "_first");
    initialFocus.focus.defer(1000, initialFocus);
}

function addXPEDXComplmentaryItemToCart()
{
	//get the select item, quantity and UOM from the form and set it in the parameters.
   	clearErrorMessages(document.addComplementaryItemForm);
    clearErrorLabels(document.addComplementaryItemForm);

    if(swc_validateForm("addComplementaryItemForm") == false)
    {
        return;
    }
    var selectedItemID = getCheckedValue(document.forms['addComplementaryItemForm'].elements['complimentaryItemID']);
    if(selectedItemID == ''){
    	alert('Please select an item to add');
    	return;
    }
    document.addComplementaryItemForm.ProductID.value = selectedItemID;
    var selectedQuantity = (document.getElementById("complimentaryQuantity_" + selectedItemID)).value;
    if(selectedQuantity == ''){
    	alert('Please enter the quantity to add');
    	return;
    }
    document.addComplementaryItemForm.Quantity.value = selectedQuantity; 
	//TODO: Change the UOM logic
    //document.addComplementaryItemForm.ProductUOM.value = 'EACH';
    document.addComplementaryItemForm.action = "addComplementaryItemToCart.action";
    document.addComplementaryItemForm.submit();
}

function replaceXPEDXComplmentaryItemToCart(){
	//get the select item, quantity and UOM from the form and set it in the parameters.
   	clearErrorMessages(document.addComplementaryItemForm);
    clearErrorLabels(document.addComplementaryItemForm);

    if(swc_validateForm("addComplementaryItemForm") == false)
    {
        return;
    }
    var selectedItemID = getCheckedValue(document.forms['addComplementaryItemForm'].elements['complimentaryItemID']);
    if(selectedItemID == ''){
    	alert('Please select an item to replace');
    	return;
    }
    document.addComplementaryItemForm.ProductID.value = selectedItemID;
    var selectedQuantity = (document.getElementById("complimentaryQuantity_" + selectedItemID)).value;
    if(selectedQuantity == ''){
    	alert('Please enter the quantity to replace');
    	return;
    }
    document.addComplementaryItemForm.Quantity.value = selectedQuantity; 
	//TODO: Change the UOM logic
    //document.addComplementaryItemForm.ProductUOM.value = 'EACH';
    document.addComplementaryItemForm.action = "addAlternativeItemToCart.action";
    document.addComplementaryItemForm.submit();
}

function showXPEDXAlternateItems(itemIDUOM, orderLineKey, quantity)
{
	var source = document.getElementById("alternatives_" + itemIDUOM);
    var destination = document.getElementById("alternativeItemBody");
    destination.innerHTML = source.innerHTML;
    
    document.addAlternativeItemForm.OrderLineKey.value = orderLineKey;
    document.addAlternativeItemForm.Quantity.value = quantity;
	DialogPanel.show("alternativeItems");
    svg_classhandlers_decoratePage();

  	var initialFocus = Ext.get("alternatives_" + itemIDUOM + "_first");
    initialFocus.focus.defer(1000, initialFocus);
}

function addXPEDXAlternativeItem()
{
	//get the select item, quantity and UOM from the form and set it in the parameters.
   	clearErrorMessages(document.addAlternativeItemForm);
    clearErrorLabels(document.addAlternativeItemForm);
    
    if(swc_validateForm("addAlternativeItemForm") == false)
    {
        return;
    }
    var selectedItemID = getCheckedValue(document.forms['addAlternativeItemForm'].elements['alternativeItemIDUOM']);
    document.addAlternativeItemForm.ProductID.value = selectedItemID;
    var selectedQuantity = (document.getElementById("alternativeQuantity_" + selectedItemID)).value;
    document.addAlternativeItemForm.Quantity.value = selectedQuantity; 
	//TODO: Change the UOM logic
    //document.addAlternativeItemForm.ProductUOM.value = 'EACH';
    document.addAlternativeItemForm.action = "addComplementaryItemToCart.action";
    document.addAlternativeItemForm.submit();
}

function replaceXPEDXAlternateItemToCart(){
	    //get the select item, quantity and UOM from the form and set it in the parameters.
   	clearErrorMessages(document.addAlternativeItemForm);
    clearErrorLabels(document.addAlternativeItemForm);
	if(swc_validateForm("addAlternativeItemForm") == false)
    {
        return;
    }
    var selectedItemID = getCheckedValue(document.forms['addAlternativeItemForm'].elements['alternativeItemIDUOM']);
    document.addAlternativeItemForm.ProductID.value = selectedItemID;
    var selectedQuantity = (document.getElementById("alternativeQuantity_" + selectedItemID)).value;
    document.addAlternativeItemForm.Quantity.value = selectedQuantity; 
	//TODO: Change the UOM logic
    //document.addAlternativeItemForm.ProductUOM.value = 'EACH';
    document.addAlternativeItemForm.action = "addAlternativeItemToCart.action";
    document.addAlternativeItemForm.submit();
}

/*function showXPEDXReplacementItems(itemIDUOM, orderLineKey, quantity)
{
	var source = document.getElementById("replacement_" + itemIDUOM);
    var destination = document.getElementById("replacementItemBody");
    destination.innerHTML = source.innerHTML;
    
    document.addReplacementItemToCartForm.OrderLineKey.value = orderLineKey;
    document.addReplacementItemToCartForm.Quantity.value = quantity;
	DialogPanel.show("replacementItems");
    svg_classhandlers_decoratePage();

  	var initialFocus = Ext.get("replacement_" + itemIDUOM + "_first");
    initialFocus.focus.defer(1000, initialFocus);
}
*/

function addXPEDXReplacementItem()
{
	//get the select item, quantity and UOM from the form and set it in the parameters.
   	clearErrorMessages(document.addReplacementItemToCartForm);
    clearErrorLabels(document.addReplacementItemToCartForm);
    if(swc_validateForm("addReplacementItemToCartForm") == false)
    {
        return;
    }
    var selectedItemID = getCheckedValue(document.forms['addReplacementItemToCartForm'].elements['replacementItemID']);
    document.addReplacementItemToCartForm.ProductID.value = selectedItemID;
    var selectedQuantity = (document.getElementById("replacementQuantity_" + selectedItemID)).value;
    document.addReplacementItemToCartForm.Quantity.value = selectedQuantity; 
    //TODO: Change the UOM logic
    //document.addAlternativeItemForm.ProductUOM.value = 'EACH';
    document.addReplacementItemToCartForm.action = "addComplementaryItemToCart.action";
    document.addReplacementItemToCartForm.submit();
}


function replaceXPEDXReplacementItemToCart(){
	    //get the select item, quantity and UOM from the form and set it in the parameters.
   	clearErrorMessages(document.addReplacementItemToCartForm);
    clearErrorLabels(document.addReplacementItemToCartForm);
	if(swc_validateForm("addReplacementItemToCartForm") == false)
    {
        return;
    }
    var selectedItemID = getCheckedValue(document.forms['addReplacementItemToCartForm'].elements['replacementItemID']);
    document.addReplacementItemToCartForm.ProductID.value = selectedItemID;
    var selectedQuantity = (document.getElementById("replacementQuantity_" + selectedItemID)).value;
    document.addReplacementItemToCartForm.Quantity.value = selectedQuantity; 
	//TODO: Change the UOM logic
    //document.addAlternativeItemForm.ProductUOM.value = 'EACH';
    document.addReplacementItemToCartForm.action = "addAlternativeItemToCart.action";
    document.addReplacementItemToCartForm.submit();
}

function getCheckedValue(radioObj) {
	if(!radioObj)
		return "";
	var radioLength = radioObj.length;
	if(radioLength == undefined)
		if(radioObj.checked)
			return radioObj.value;
		else
			return "";
	for(var i = 0; i < radioLength; i++) {
		if(radioObj[i].checked) {
			return radioObj[i].value;
		}
	}
	return "";
}
<!-- eb 1131-->
function showXPEDXReplacementItems(repItemID, orderLineKey, order) {
	
  	var rsize=document.getElementById("rListSize_"+repItemID).value;
  	if(rsize == "1"){
  	  selReplacementId = document.getElementById("hUId_"+repItemID).value;
  	}
  	else{
  	selReplacementId = "";
  	}
	var source 		= document.getElementById("replacement_" + repItemID);
    var destination = document.getElementById("replacementItemBody");
    
    //Fill in data
    destination.innerHTML = source.innerHTML;
  	
  	//Add the key into the change form
  	var form = Ext.get("formRIReplaceInList");
  	form.dom.key.value = orderLineKey;
  	
  	//Display the facy box
	$.fancybox(
		Ext.get("replacementItems").dom.innerHTML,
		{
        	'autoDimensions'	: true,
			'width'         	: 350,
			'height'        	: 'auto'
		}
	);	  	
}

function replacementAddToList(uId) {
	if (uId == ""){
		 alert("Please select an item first.");
		return;
	}
	
	//Get the form
	var form = Ext.get("formRIAddToList");
	
	//Get the rest of the data
	var itemid 	= Ext.get("replacement_" + uId + "_itemid").dom.value;
	var name 	= Ext.get("replacement_" + uId + "_name").dom.value;
	var desc 	= Ext.get("replacement_" + uId + "_desc").dom.value;
	var uom 	= document.getElementById("replacement_" + uId + "_uom").value;
	
	

	//Transfer the values
	form.dom.itemId.value 	= itemid;
	form.dom.name.value 	= name;
	form.dom.desc.value 	= desc;
	form.dom.qty.value 		= "1";
	form.dom.uomId.value 	= uom;
	
	//Submit the form
	form.dom.submit();
	
	Ext.Msg.wait('Saving your selection... Please wait.');
}

function replacementReplaceInList(uId) {

	if (uId == ""){
		alert("Please select an item first."); /*Modified code For Jira 2922*/
		//Ext.Msg.alert('Error', 'Please select an item first.');
		return;
	}
	
	//Get the form
	var form = Ext.get("formRIReplaceInList");
	
	//Get the rest of the data
	var name 	= Ext.get("replacement_" + uId + "_name").dom.value;
	var desc 	= Ext.get("replacement_" + uId + "_desc").dom.value;
	var itemid 	= Ext.get("replacement_" + uId + "_itemid").dom.value;
	
	var uom 	= document.getElementById("replacement_" + uId + "_uom").value;

	
	//var order 	= parseInt(form.dom.itemCount.value,10);
	
	//Transfer the values
	form.dom.itemId.value 	= itemid;
	form.dom.name.value 	= name;
	form.dom.desc.value 	= desc;
	form.dom.qty.value 		= "1";
	form.dom.uomId.value 	= uom;
	//form.dom.order.value 	= parseInt(order, 10) + 1;
	
	//Submit the form
	form.dom.submit();
	
	Ext.Msg.wait('Saving your selection... Please wait.');
}

function updateUOM(uId, uom ) {
	var elemId = "replacement_"+uId+"_uom_modified";
	//Setting the _modified suffix element since dom binding is not happening in js
	document.getElementById(elemId).value = uom;
}
/*
function updateQTY(uId, qty){
	if(qty=='' || qty=='0'){
		Ext.Msg.alert('Error', 'Please select a valid quantity.');
		return false;
	}
	var elemId = "replacement_"+uId+"_qty_modified";
	//Setting the _modified suffix element since dom binding is not happening in js
	document.getElementById("replacement_"+uId+"_qty_modified").value = qty;
}
*/
function updateQTY(uId, component){
	var qty = component.value; 
	if(!isValidQuantity(component)){
		Ext.Msg.alert('Error', 'Please select a valid quantity.');
		component.value = qty;
		return false;
	}
	qty = component.value;
	var qtyNum =  Number(qty);
	if(qtyNum=='' || qtyNum=='0'){
		Ext.Msg.alert('Error', 'Please select a valid quantity.');
		return false;
	}
	var elemId = "replacement_"+uId+"_qty_modified";
	//Setting the _modified suffix element since dom binding is not happening in js
	document.getElementById(elemId).value = qty;
}

function setOtherFields(uId) {
	var form = Ext.get("formRIReplaceInList");
	document.getElementById("replacement_" + uId + "_qty_modified").value = document.getElementById("replacement_" + uId + "_qty").value;
	document.getElementById("replacement_" + uId + "_uom_modified").value = document.getElementById("replacement_" + uId + "_uom").value;
	
}


