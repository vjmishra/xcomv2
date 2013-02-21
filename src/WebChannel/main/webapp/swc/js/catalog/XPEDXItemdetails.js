
function itemDetailValidateAndAddToCart(itemId) {
	if (validateOrderMultiple() == false) {
		return;
	}

	var UOMelement = document.getElementById("itemUOMsSelect");
	var uomvalue = UOMelement.options[UOMelement.selectedIndex].text;
	itemDetailAddToCart(itemId, uomvalue, '', '');
}

/*function validateOrderMultiple() {
	var Qty = document.getElementById("qtyBox");
	var UOM = document.getElementById("itemUOMsSelect");
	var OrdMultiple = document.getElementById("OrderMultiple");
	var totalQty = UOM.value * Qty.value;
	var ordMul = totalQty % OrdMultiple.value;
	if (ordMul != 0) {
		alert("Order Quantity must be a multiple of " + OrdMultiple.value);
		return false;
	}
	return true;
}*/
var myMask;
function updatePandAfromLink(){
	/*Web Trends tag start*/ 
	if(document.getElementById("webtrendItemType")!=null){
		var itemType=document.getElementById("webtrendItemType").value;	
		writeMetaTag("DCSext.w_x_itemtype",itemType);
	}	
		/*Web Trends tag end*/
	//added for jira 3974
	var waitMsg = Ext.Msg.wait("Processing...");
	myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
	myMask.show();
	var UOMelement = document.getElementById("itemUOMsSelect");
	var uomvalue = UOMelement.options[UOMelement.selectedIndex].value;	
	callPnAfromLink(uomvalue);
	//Ext.Msg.hide();
	//myMask.hide();
}
function updatePandA() {
	/*var UOMelement = document.getElementById("itemUOMsSelect");
	var uomvalue = UOMelement.options[UOMelement.selectedIndex].value;
	var uomvalue1 = UOMelement.options[UOMelement.selectedIndex].value;
	document.getElementById("requestedUOM").value = uomvalue;
	if (document.getElementById("qtyBox").value == null
			|| document.getElementById("qtyBox").value == "") {
		//alert("Enter Qty Please");
		//return;
	}
	document.getElementById("requestedQty").value = document
			.getElementById("qtyBox").value;
	document.productDetailForm.action = document
			.getElementById('updatePandAURL');
	//Added For Jira 2903
	Ext.Msg.wait("Processing..."); 
	document.productDetailForm.submit();*/
	//added for jira 3253
	var UOMelement = document.getElementById("itemUOMsSelect");
	var uomvalue = UOMelement.options[UOMelement.selectedIndex].value;
	Ext.Msg.wait("Processing...");
	callPnA(uomvalue); 
}

function updateUOMFields() {
	var UOMelement = document.getElementById("itemUOMsSelect");
	var uomvalue = UOMelement.options[UOMelement.selectedIndex].value;
	//These fields are used for order multiple check
	document.getElementById("selectedUOM").value = uomvalue;
	document.getElementById('uomConvFactor').value= document.getElementById("convF_"+uomvalue).value;
}

function setPandAData() {
	var ajaxAvailDiv = document.getElementById("jsonAvalabilityDiv");
	var errorValue = document.getElementById("lineStatusCodeMsg");
	var errorVal=document.getElementById("pnaErrorStatusMsg");
	var itemAvailDiv = document.getElementById("tabs-1");
	var displayPricesDiv = document.getElementById("displayPricesDiv");
	var pricedDiv = document.getElementById("pricesDiv");
	var OrdMultiple = document.getElementById("OrderMultiple");
	
	var errorMsgDiv = document.getElementById("errorMessageDiv"); //Added for Fix of Jira 2885

	if(ajaxAvailDiv!=null && itemAvailDiv!=null) {
		itemAvailDiv.innerHTML = ajaxAvailDiv.innerHTML;
	}
	document.getElementById("jsonAvalabilityDiv").innerHTML = "";
	if(displayPricesDiv!=null && pricedDiv!=null) {
		displayPricesDiv.innerHTML = pricedDiv.innerHTML;
	}
	document.getElementById("pricesDiv").innerHTML = "";
	
	//added for jira 2885
	if(errorValue.value != null && errorValue.value != ""){
		errorMsgDiv.innerHTML = "<h5 align='center'><b><font color=red>" + errorValue.value + "</font></b></h5>";
	}
	
	if(errorVal.value != null && errorVal.value != ""){
		errorMsgDiv.innerHTML = "<h5 align='center'><b><font color=red>" + errorVal.value + "</font></b></h5>";
	}
	//XB 214 BR1
	var qty = document.getElementById("qtyBox");
	var sourceOrderMulError = document.getElementById("errorMsgForQty");
	var OrderMultipleQtyFromSrc = document.getElementById("OrderMultipleQtyFromSrc");
	if(OrderMultipleQtyFromSrc != null && OrderMultipleQtyFromSrc.value != ''){
	var OrderMultipleQtyFromSrc1 = OrderMultipleQtyFromSrc.value;
	var OrderMultipleQtyUom = OrderMultipleQtyFromSrc1.split("|");
	var orderMultipleQty = OrderMultipleQtyUom[0];
	var OrderMultipleUom = OrderMultipleQtyUom[1];
	var omError = OrderMultipleQtyUom[2];	
	if(omError == 'true' && qty.value > 0)//omError == 'true' && qty.value > 0 )
	{
		sourceOrderMulError.innerHTML = "Must be ordered in units of " + addComma(orderMultipleQty) +" "+OrderMultipleUom;
		sourceOrderMulError.style.display = "inline-block"; 
		sourceOrderMulError.setAttribute("class", "error");
		displayPricesDiv.style.display = "none"; 
		itemAvailDiv.style.display = "none"; 
		qty.style.borderColor="#FF0000";
	}
	else if(omError == 'true')
	{
		sourceOrderMulError.innerHTML = "Must be ordered in units of " + addComma(orderMultipleQty) +" "+OrderMultipleUom;
		sourceOrderMulError.style.display = "inline-block"; 
		sourceOrderMulError.setAttribute("class", "notice");
		displayPricesDiv.style.display = "none"; 
		itemAvailDiv.style.display = "none"; 
		qty.style.borderColor="";
	}
	else if(orderMultipleQty != null)
	{
		sourceOrderMulError.innerHTML = "Must be ordered in units of " + addComma(orderMultipleQty) +" "+OrderMultipleUom;
		sourceOrderMulError.style.display = "inline-block"; 
		sourceOrderMulError.setAttribute("class", "notice");
		qty.style.borderColor="";
	}
	else
	{
		displayPricesDiv.style.display = "block"; 
		itemAvailDiv.style.display = "block"; 
		qty.style.borderColor="";
	}
	}
	
	//if(displayPricesDiv!=null && pricedDiv!=null) 
	if(errorValue.value == null && errorValue.value == "") {
		errorMsgDiv.innerHTML= "";
	}
	document.getElementById("lineStatusCodeMsg").value = "";
	document.getElementById("qtyBox").style.borderColor="";	
}

function addItemToList() {
	if (validateOrderMultiple() == false) {
		return;
	}

	var Qty = document.getElementById("qtyBox");
	var ItemListelement = document.getElementById("itemListSelect");
	var listname = ItemListelement.options[ItemListelement.selectedIndex].text;
	var UOMelement = document.getElementById("itemUOMsSelect");
	var uomvalue = UOMelement.options[UOMelement.selectedIndex].text;

	document.getElementById("uomId").value = uomvalue;
	document.getElementById("qty").value = Qty.value;
	document.getElementById("listKey").value = ItemListelement.value;
	document.getElementById("listName").value = listname;
	document.productDetailForm.action = document
			.getElementById('addToItemListURL');
	document.productDetailForm.submit();
}
function RequestSample() {
	DialogPanel.show('showSampleReqDialog');
}
function editField(data) {

	if (data == "Edit1") {

		document.getElementById("RequestedDeliveryDate").readOnly = false;
		document.getElementById("RequestedDeliveryDate").style.background = '#dedede';
		document.getElementById("CustomerJobTitle").readOnly = false;
		document.getElementById("CustomerJobTitle").style.background = '#dedede';
		document.getElementById("Notes").readOnly = false;
		document.getElementById("Notes").style.background = '#dedede';

	} else if (data == "Edit2") {

		document.getElementById("AccountNumber").readOnly = false;
		document.getElementById("AccountNumber").style.background = '#dedede';
		document.getElementById("CustomerName").readOnly = false;
		document.getElementById("CustomerName").style.background = '#dedede';
		document.getElementById("CustomerContactName").readOnly = false;
		document.getElementById("CustomerContactName").style.background = '#dedede';
		document.getElementById("ContactPhoneNumber").readOnly = false;
		document.getElementById("ContactPhoneNumber").style.background = '#dedede';
		document.getElementById("ShippingAddress1").readOnly = false;
		document.getElementById("ShippingAddress1").style.background = '#dedede';
		document.getElementById("ShippingAddress2").readOnly = false;
		document.getElementById("ShippingAddress2").style.background = '#dedede';
		document.getElementById("ShippingAddress3").readOnly = false;
		document.getElementById("ShippingAddress3").style.background = '#dedede';
		document.getElementById("CityRequest").readOnly = false;
		document.getElementById("CityRequest").style.background = '#dedede';
		document.getElementById("StateRequest").readOnly = false;
		document.getElementById("StateRequest").style.background = '#dedede';
		document.getElementById("ZipRequest").readOnly = false;
		document.getElementById("ZipRequest").style.background = '#dedede';
	} else if (data == "Edit3") {

		document.getElementById("DivisionNumber").readOnly = false;
		document.getElementById("DivisionNumber").style.background = '#dedede';
		document.getElementById("SalesRepresentative1").readOnly = false;
		document.getElementById("SalesRepresentative1").style.background = '#dedede';
	} else {

		document.getElementById("Manufacturer").readOnly = false;
		document.getElementById("Manufacturer").style.background = '#dedede';
		document.getElementById("ManufacturerPartNumber").readOnly = false;
		document.getElementById("ManufacturerPartNumber").style.background = '#dedede';
		document.getElementById("Itemdescription").readOnly = false;
		document.getElementById("Itemdescription").style.background = '#dedede';
		document.getElementById("Quantity").readOnly = false;
		document.getElementById("Quantity").style.background = '#dedede';

		document.getElementById("UnitofMeasure1").readOnly = false;
		document.getElementById("UnitofMeasure1").style.background = '#dedede';
	}
}

function SubmitAction() {
	var url = "itemDetailsEmail.action?Notes="+ document.getElementById("Notes").value 
			+ "&ContactPhoneNumber=" + document.getElementById("rphone").value
			+ "&ShippingAddress1=" + document.getElementById("ShippingAddress1").value
			+ "&ShippingAddress2=" + document.getElementById("raddress2").value
			+ "&ShippingAddress3=" + document.getElementById("raddress3").value
			+ "&CityRequest=" + document.getElementById("rcity").value
			+ "&StateRequest=" + document.getElementById("rState").value
			+ "&ZipRequest=" + document.getElementById("rzipcode").value			
			+ "&MPC="+ document.getElementById("MPC").value
			+ "&Manufacturer=" + document.getElementById("Manufacturer").value
			+ "&ManufacturerPartNumber=" + document.getElementById("itemID").value
			+ "&Itemdescription=" + document.getElementById("Itemdescription").value 
			+ "&Quantity=" + document.getElementById("Quantity").value;

	SendEmail(url);
	$.fancybox.close();

}

function SendEmail(url) {
	if (window.XMLHttpRequest) {
		// Non-IE browsers
		req = new XMLHttpRequest();
		req.onreadystatechange = processStateChange;
		try {
			req.open("GET", url, true);
		} catch (e) {
			alert(e);
		}
		req.send(null);
	} else if (window.ActiveXObject) { // IE

		req = new ActiveXObject("Microsoft.XMLHTTP");
		if (req) {
			req.onreadystatechange = processStateChange;
			req.open("GET", url, true);
			req.send();
		}
	}
}
function processStateChange() {
	if (req.readyState == 4) { // Complete
		if (req.status == 200) { // OK response
			var data = req.responseText;
			//alert(data);
		} else {
			alert("Problem: " + req.statusText);
		}
	}
}
