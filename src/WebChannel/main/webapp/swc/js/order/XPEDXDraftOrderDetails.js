function checkOut()
{
	//Added for JIRA 3523
	var entitleErrorMessage;
	if(document.getElementById("entileErrorMessade")!=null){
		entitleErrorMessage=document.getElementById("entileErrorMessade").innerHTML;
		if(entitleErrorMessage !=null && entitleErrorMessage.trim().length>0){
			return;
		}
	}
	document.OrderDetailsForm.modifyOrderLines.value = "false";
	//JIRA 3488 Start
	//Added for JIRA 3958
	var maxOrderAmtMsg = null; 
	if(document.getElementById("maxOrderErrorMessage") != null && document.getElementById("maxOrderErrorMessage") != "" && document.getElementById("maxOrderErrorMessage")!= 'undefined' ){
		maxOrderAmtMsg=document.getElementById("maxOrderErrorMessage").innerHTML;
	}
	if(maxOrderAmtMsg !=null && maxOrderAmtMsg.trim().length>0){
		return;
	}
	//JIRA 3488 End
	/*if(validateOrderMultiple() == false)
	{
		return;
	}*/
	if(validateQty() == false)
	{
		return;
	}
  /*  var otherForm = document.QuickAddForm;
    clearErrorMessages(otherForm);
    clearErrorLabels(otherForm);*/

    document.getElementById("validationActionName").value = "saveCartDetails";
    if(swc_validateForm("OrderDetailsForm") == false)
    {
        return;
    }
    if(document.getElementById('cartName_new')!=null && document.getElementById('cartName_new').value.trim() != "")
	{
    	document.OrderDetailsForm.orderName.value = document.getElementById('cartName_new').value;
	}
    document.OrderDetailsForm.orderDesc.value = document.getElementById('cartDesc_new').value; 
    document.OrderDetailsForm.isComingFromCheckout.value = "true";
    document.OrderDetailsForm.action = document.getElementById('checkoutURL');   

    //Added for EB-4754 - where the hidden uom field is not same as selected uom
	var orderLinesCount = document.OrderDetailsForm.OrderLinesCount.value;
	var retVal=true;
	if(orderLinesCount!=null && orderLinesCount==1){
		var itemSelUom;
		var lineKey;
		var itemUomHidden;		
		
		lineKey = document.getElementById("OrderDetailsForm").elements["orderLineKeys"];
		itemSelUom = document.getElementById("itemUOMsSelect_" + lineKey.value).value;
		itemUomHidden = document.getElementById("itemUOMs_" + lineKey.value).value;
		if (itemSelUom!=null && itemSelUom != itemUomHidden ){
			document.getElementById("itemUOMs_" + lineKey.value).value = itemSelUom;
		}
	}
	else{
		var itemSelUom = new Array();
		var lineKey = new Array();
		var itemUomHidden;		
		lineKey = document.getElementById("OrderDetailsForm").elements["orderLineKeys"];

		for(var i = 0; i < orderLinesCount; i++)
		{	
			itemUomHidden = document.getElementById("itemUOMs_" + lineKey[i].value).value;
			itemSelUom = document.getElementById("itemUOMsSelect_" + lineKey[i].value).value;
			if (itemSelUom != itemUomHidden ){
				document.getElementById("itemUOMs_" + lineKey[i].value).value = itemSelUom;
			}
		}
	
	}//End of EB-4754
    document.OrderDetailsForm.submit();
}
function validateQty(){
	var orderLinesCount = document.OrderDetailsForm.OrderLinesCount.value;
	var retVal=true;
	if(orderLinesCount== 0 || orderLinesCount =='' || orderLinesCount == undefined){
			return retVal; 
		}
	if(orderLinesCount==1){
		var arrQty ;		
		var arrItemID ;	
		var itemSelUom ;//EB-3840
		var conv = '';//EB-3840
		arrQty = document.getElementById("OrderDetailsForm").elements["orderLineQuantities"];		
		arrItemID = document.getElementById("OrderDetailsForm").elements["orderLineItemIDs"];
		itemSelUom = document.getElementById("OrderDetailsForm").elements["itemUOMsSelect"];//EB-3840
	
		var divId='errorDiv_'+ arrQty.id;
		var divIdError=document.getElementById(divId);
		var qtyElement = document.getElementById(arrQty.id);
		
		if(arrQty.value == '' || arrQty.value ==0)
		{		
			if(divIdError != null){
				divIdError.innerHTML='Please enter a valid quantity and try again.';
				divIdError.setAttribute("class", "error");
			}
			retVal=false; 
		}
		
		//EB-3840 Ordered quantity is greater than 7 digits validation.
		var DisplayUomItem =document.getElementsByName("DisplayUomItem_"+arrItemID.value);
		
		for(var j = 0; j < DisplayUomItem.length; j++)
		{
		if(itemSelUom.value == DisplayUomItem[j].id){
		 conv = DisplayUomItem[j].value;
		 break;
		}
		}
		if(conv != '' && conv != undefined){//EB-3651
			var part = conv.split("(");
			if(part.length >1)
				{
			var convFac = part[1].split(")");
			var totalQty = arrQty.value * convFac[0];
					if(totalQty.toString().length > 7) 
						{
						if(divIdError != null){
							divIdError.innerHTML='Order QTY too large, please enter a valid QTY.';
							divIdError.setAttribute("class", "error");
						}
					retVal=false;
						}
				}
			}
		//EB-3840 ends here
		
		
	}else{//if more than one item
		var arrQty = new Array();		
		var arrItemID = new Array();
		var itemSelUom = new Array();//EB-3840
		arrQty = document.getElementById("OrderDetailsForm").elements["orderLineQuantities"];		
		arrItemID = document.getElementById("OrderDetailsForm").elements["orderLineItemIDs"];
		itemSelUom = document.getElementById("OrderDetailsForm").elements["itemUOMsSelect"];		//EB-3840
	
		for(var i = 0; i < arrItemID.length; i++)
		{			
			var divId='errorDiv_'+ arrQty[i].id;
			var divIdError=document.getElementById(divId);
			var qtyElement = document.getElementById(arrQty[i].id);
			if(arrQty[i].value == '' || arrQty[i].value ==0)
			{		
				if(divIdError != null){
					divIdError.innerHTML='Please enter a valid quantity and try again.';
					divIdError.setAttribute("class", "error");
				}
				retVal=false;			
			}else
			{
				qtyElement.style.borderColor = "";
			} 
			//EB-3840 Ordered quantity is greater than 7 digits validation.
		var DisplayUomItem =document.getElementsByName("DisplayUomItem_"+arrItemID[i].value);
		var conv ;
		
		for(var j = 0; j < DisplayUomItem.length; j++)
		{
		if(itemSelUom[i].value == DisplayUomItem[j].id){
		 conv = DisplayUomItem[j].value;
		 break;
		}
		}
		if(conv != '' && conv != undefined){//EB-3651
			var part = conv.split("(");
			if(part.length >1)
				{
			var convFac = part[1].split(")");
			var totalQty = arrQty[i].value * convFac[0];
					if(totalQty.toString().length > 7) 
						{
						if(divIdError != null){
							divIdError.innerHTML='Order QTY too large, please enter a valid QTY.';
							divIdError.setAttribute("class", "error");
						}
					retVal=false;
						}
				}
			}
			
			//EB-3840 ends here
		}
	}//end of else
	return retVal;
}

function addProductsToOrder()
{
	//for jira 3253 resetQuantityErrorForQuckAdd();
    if(QuickAddElems.length > 0)
    {
     	 var enteredQuants;
		 var enteredUoms; 
		 var enteredUomsConFact = new Array();
		 var availUomsConFact = new Array();
		 var baseUOM = new Array();
		 var entereditems;
		 var uomConvFac; 
		 var selectedUomConvFac;
		 var selectedUomConvFacFromStr;
		 var selectedUomFromStr;
		 var orderMultiple;// only one..refine to set it only once.
		 var isError = false;
		 var noError = true;
		 var selectedUOM = new Array();
		 selectedUOM= document.getElementsByName("enteredUOMs");
		 baseUOM = document.getElementsByName("quickAddBaseUOMs");
		 for(var i=0 ; i < QuickAddElems.length ; i++)
		 {
			 noError = true; 
			orderMultiple = encodeForHTML(QuickAddElems[i].orderMultiple);
			if(orderMultiple!=undefined && orderMultiple > 1 && orderMultiple.replace(/^\s*|\s*$/g,"") !='' && orderMultiple != null && orderMultiple !=0){
			var enteredUOM = selectedUOM[i].value;
			enteredUOM = enteredUOM.split(" ");
			enteredQuants = encodeForHTML(QuickAddElems[i].quantity);
			entereditems = encodeForHTML(QuickAddElems[i].sku);
			enteredUoms = enteredUOM[0];
			uomConvFac = encodeForHTML(QuickAddElems[i].itemUomAndConvString);
			}
			else {
				enteredQuants = encodeForHTML(QuickAddElems[i].quantity);
				entereditems = encodeForHTML(QuickAddElems[i].sku);
				enteredUoms = encodeForHTML(QuickAddElems[i].uom);
				uomConvFac = encodeForHTML(QuickAddElems[i].itemUomAndConvString);
			}
			if(enteredUoms){
				if(uomConvFac!=null)enteredUomsConFact = uomConvFac.split("|");
				for(var j=0; j<enteredUomsConFact.length; j++){
					availUomsConFact = enteredUomsConFact[j].split("-");
					selectedUomConvFacFromStr = availUomsConFact[1];
					selectedUomFromStr = availUomsConFact[0];
					if(enteredUoms.trim() == selectedUomFromStr.trim()){
						selectedUomConvFac = selectedUomConvFacFromStr;
						break;
					}
				}
				enteredQuants = ReplaceAll(enteredQuants,",","");
				var divId="errorQty_"+entereditems+i;
				var divIdError=document.getElementById(divId);
				//alert("entered item is: " + entereditems +" enteredUoms:" +enteredUoms + "  enteredQuants:" +enteredQuants+ " orderMultiple:" +orderMultiple + " selectedUomConvFacFromStr:"+selectedUomConvFacFromStr + "  selectedUomConvFac:"+selectedUomConvFac);
				if(selectedUomConvFac!=undefined && selectedUomConvFac!=null){
				
					if(orderMultiple == undefined || orderMultiple.replace(/^\s*|\s*$/g,"") =='' || orderMultiple == null || orderMultiple ==0)
					{
						orderMultiple=1;
					}
					var totalQty = selectedUomConvFac * enteredQuants;
					var ordMul = totalQty % orderMultiple; 
					
					if(enteredQuants == '' || enteredQuants=='0'){
						//3098
						document.getElementById(divId).innerHTML ='Please enter a valid quantity and try again.';
						//3098
						document.getElementById(divId).style.display = "inline-block";
						document.getElementById(divId).style.marginLeft = "20px";
						document.getElementById(divId).setAttribute("class", "error");
						document.getElementById(divId).setAttribute("align", "center");
						isError = true;
						noError = false;
					}					
				/*	else if(ordMul != 0 || totalQty ==0)
					{
						isError = true;
						//Added for 3098
						document.getElementById(divId).innerHTML ="Please order in units of " + orderMultiple +" "+baseUOM[i].value;
						document.getElementById(divId).style.display = "inline-block";
						document.getElementById(divId).style.marginLeft = "20px";
						document.getElementById(divId).setAttribute("class", "error");
						document.getElementById(divId).setAttribute("align", "center");
						noError = false;
					} XB 214 removing the Order Multiple Validation in Quick add & add to cart */
					if(orderMultiple > 1 && noError == true) {
						document.getElementById(divId).innerHTML ="Must be ordered in units of " + orderMultiple +" "+baseUOM[i].value;
						document.getElementById(divId).style.display = "inline-block";
						document.getElementById(divId).setAttribute("class", "notice");
						document.getElementById(divId).setAttribute("align", "center");
					}
				}
			}else{
				//alert(" not validating order multiple ");
				continue;
			}
		 }
		 if(!isError){	// no error, then submit to add the products to the cart
			 for(var i=0 ; i < QuickAddElems.length ; i++)
			 {
				var orderMultiple1 = encodeForHTML(QuickAddElems[i].orderMultiple);
				if(orderMultiple1 == undefined || orderMultiple1.replace(/^\s*|\s*$/g,"") =='' || orderMultiple1 == null || orderMultiple1 ==0)
				{
					orderMultiple1=1;
				}//Added createHiddenField method on the fly for ordermultiple for Jira 3481
				
				createHiddenField("QuickAddForm","quickAddOrderMultiple",orderMultiple1);
			 }
			 document.QuickAddForm.action = document.getElementById('addProductsToOrderURL');
			 var form= Ext.get("QuickAddForm");
			 addCSRFToken(form.dom, 'form');
			 form.dom.submit();
		 }
		 else{
			 return false;
		 }
    }
}
//Added createHiddenField method on the fly for ordermultiple for Jira 3481
function createHiddenField(formName,hidName,value)
{
var doc = document;
var f = doc.getElementById(formName);
//modified the method defination for jira 3677 - quick add - add to cart IE8
var input = doc.createElement("input");
input.setAttribute("type", "hidden");
input.setAttribute("name",hidName);
input.setAttribute("value",value);
f.appendChild(input);
//end of jira 3677
}


function update()
{
	document.OrderDetailsForm.isComingFromCheckout.value = "false";
	
	if(document.getElementById('cartName_new')!=null && document.getElementById('cartName_new').value.trim() == "")
	{
		$('#errorMsgTop,#errorMsgBottom').each(function(){
			$(this).find('p').text("Name is required.");
			$(this).show();
		});
		return;
	}
	var orderLinesCount = document.OrderDetailsForm.OrderLinesCount.value;
	if(orderLinesCount > 0)
	{
		if(validateQty() == false)
		{
			return;
		}
	/*	var otherForm = document.QuickAddForm;
	    clearErrorMessages(otherForm);
	    clearErrorLabels(otherForm);*/
	
	    document.getElementById("validationActionName").value = "draftOrderModifyLineItems";
	    if(swc_validateForm("OrderDetailsForm") == false)
	    {
	        return;
	    }
	    if(document.getElementById('cartName_new')!=null && document.getElementById('cartName_new').value.trim() != "")
		{
	    	document.OrderDetailsForm.orderName.value = document.getElementById('cartName_new').value;
		}
	    document.OrderDetailsForm.orderDesc.value = document.getElementById('cartDesc_new').value;
	    document.OrderDetailsForm.modifyOrderLines.value = "true";
	    document.OrderDetailsForm.action = document.getElementById('updateURL');
	    document.OrderDetailsForm.submit();
	}
	else
	{
		if(document.getElementById('cartName_new')!=null && document.getElementById('cartName_new').value.trim() == "")
		{
			document.OrderDetailsForm.orderName.value = document.getElementById('cartName_new').value;			
		}
		document.OrderDetailsForm.orderDesc.value = document.getElementById('cartDesc_new').value;
		document.OrderDetailsForm.zeroOrderLines.value = "true";
		document.OrderDetailsForm.action = document.getElementById('updateNoLinesURL');
		document.OrderDetailsForm.submit();
	}
}

function validateOrderMultiple()
{
var retVal=true;
	 resetQuantityErrorMessage();
	var orderLinesCount = document.OrderDetailsForm.OrderLinesCount.value;
	//alert(orderLinesCount);
	//If there is only one item 
	if(orderLinesCount==1){		
		var arrQty ;
		var arrUOM;
		var arrItemID ;
		var arrOrdMul;
		var baseUOM ;
		var zeroError=false;
		var noError = true;
	
		arrQty = document.getElementById("OrderDetailsForm").elements["orderLineQuantities"];
		arrUOM = document.getElementById("OrderDetailsForm").elements["UOMconversion"];
		//arrItemID = document.getElementsByName("orderLineItemIDs");
		arrItemID = document.getElementById("OrderDetailsForm").elements["orderLineItemIDs"];
		arrOrdMul =  document.getElementById("OrderDetailsForm").elements["orderLineOrderMultiple"];
		baseUOM = document.getElementById("OrderDetailsForm").elements["BaseUOMs"];
		var divId='errorDiv_'+	arrQty.id;
		var divIdError=document.getElementById(divId);
		var qtyElement =  document.getElementById(arrQty.id);
		//alert("arrQty.value="+arrQty.value);
		if(arrQty.value == '' || arrQty.value ==0)
		{
			//qtyElement.style.borderColor = "#FF0000"
			if(divIdError != null){
				divIdError.innerHTML='Please enter a valid quantity and try again.';
				divIdError.setAttribute("class", "error");
			}
			retVal=false;
			zeroError=true;
			noError=false;
		}else
		{
			qtyElement.style.borderColor = "";
		}
		if(arrOrdMul.value == 0 || arrOrdMul.value == null || arrOrdMul.value == undefined)
		{
			arrOrdMul.value=1;
		}
		
		var totalQty = arrUOM.value * arrQty.value;		
		var ordMul = totalQty % arrOrdMul.value;
	//	alert("ordMul="+ordMul);
	//	alert("zeroError="+zeroError);
	//	alert("divIdError="+divIdError);
		if(ordMul=="NaN"|| ordMul==NaN)
			{
			    ordMul= 0
			}		
	/*	if(ordMul != 0 && zeroError == false)
		{			
			if(divIdError != null){
				divIdError.innerHTML ="Please order in units of " +addComma(arrOrdMul.value) +" "+baseUOM.value;
				divIdError.style.display = "inline-block"; 
				divIdError.setAttribute("class", "error");
			}
			retVal=false;
			noError=false;
		} XB 214 removing the Order Multiple Validation in cart page */
	//	alert("arrOrdMul.value="+arrOrdMul.value);
		if(arrOrdMul.value > 1 && noError==true) {
			if(divIdError != null){
				divIdError.innerHTML ="Must be ordered in units of " +addComma(arrOrdMul.value) +" "+baseUOM.value;
				divIdError.style.display = "inline-block"; 
				divIdError.setAttribute("class", "notice");	
			}
		}
	}
	//else if more than one item
	else{
		var arrQty = new Array();
		var arrUOM = new Array();
		var arrItemID = new Array();
		var arrOrdMul = new Array();
		var baseUOM = new Array();		
		arrQty = document.getElementById("OrderDetailsForm").elements["orderLineQuantities"];
		arrUOM = document.getElementById("OrderDetailsForm").elements["UOMconversion"];
		//arrItemID = document.getElementsByName("orderLineItemIDs");
		arrItemID = document.getElementById("OrderDetailsForm").elements["orderLineItemIDs"];
		arrOrdMul =  document.getElementById("OrderDetailsForm").elements["orderLineOrderMultiple"];
		baseUOM = document.getElementsByName("BaseUOMs");
	for(var i = 0; i < arrItemID.length; i++)
	{
		var zeroError=false;
		var noError = true;
		var divId='errorDiv_'+	arrQty[i].id;
		var divIdError=document.getElementById(divId);
		var qtyElement =  document.getElementById(arrQty[i].id);
		if(arrQty[i].value == '' || arrQty[i].value ==0)
		{
			//qtyElement.style.borderColor = "#FF0000"
			if(divIdError != null){
				divIdError.innerHTML='Please enter a valid quantity and try again.';
				divIdError.setAttribute("class", "error");
			}
			retVal=false;
			zeroError=true;
			noError=false;
		}else
		{
			qtyElement.style.borderColor = "";
		}
		if(arrOrdMul[i].value == 0 || arrOrdMul[i].value == null || arrOrdMul[i].value == undefined)
		{
			arrOrdMul[i].value=1;
		}
		
		var totalQty = arrUOM[i].value * arrQty[i].value;		
		var ordMul = totalQty % arrOrdMul[i].value;
		if(ordMul=="NaN"|| ordMul==NaN)
			{
			    ordMul= 0
			}		
	/*	if(ordMul != 0 && zeroError == false)
		{			
			if(divIdError != null){
				divIdError.innerHTML ="Please order in units of " +addComma(arrOrdMul[i].value) +" "+baseUOM[i].value;
				divIdError.style.display = "inline-block"; 
				divIdError.setAttribute("class", "error");
			}
			retVal=false;
			noError=false;
		} XB 214 removing the Order Multiple Validation in Quick add & add to cart */
		if(arrOrdMul[i].value > 1 && noError==true) {
			if(divIdError != null){
				divIdError.innerHTML ="Must be ordered in units of " +addComma(arrOrdMul[i].value) +" "+baseUOM[i].value;
				divIdError.style.display = "inline-block"; 
				divIdError.setAttribute("class", "notice");	
			}
		}
	}
}//end of else
	return retVal;
}

function resetQuantityErrorMessage()
{
	var arrQty = new Array();
	arrQty = document.getElementsByName("orderLineQuantities");
	for(var i = 0; i < arrQty.length; i++)
	{
		var divId='errorDiv_'+	arrQty[i].id;
		var divVal=document.getElementById(divId);
		if(divVal != null){
			divVal.innerHTML='';
		}
	}
}

function setUOMValue(elementId,jsonMap)
{
	var obj = jQuery.parseJSON(jsonMap);
	var element = document.getElementById(elementId);
	var uomvalue = element.options[element.selectedIndex].value;
	var lineKey = elementId.substring(15);
	document.getElementById("itemUOMs_" + lineKey).value = uomvalue;
	var conversionFactor =  obj[uomvalue];
	document.getElementById("UOMconversion_" + lineKey).value = conversionFactor;
}

function removeItems()
{
/*    var otherForm = document.QuickAddForm;
    clearErrorMessages(otherForm);
    clearErrorLabels(otherForm);*/

    document.getElementById("validationActionName").value = "draftOrderDeleteLineItems";
    if(swc_validateForm("OrderDetailsForm") == false)
    {
        return;
    }
    document.OrderDetailsForm.action = document.getElementById('removeItemsURL');
    if(isAnyItemDeletable()){
    	document.OrderDetailsForm.submit();
    }
    else{
    	alert('No item is available to delete/Item is not deletable');
    }
    return;
}
function isAnyItemDeletable()
{
	var items = document.getElementsByName("selectedLineItem");
	var flag=false;
	for (var i = 0, n = items.length; i < n; ++i)
	{
		if (items[i].type == 'checkbox' && !items[i].disabled)
		{
			flag=true;
			break;
		}
	}
	return flag;
}

function trim(src) {
	if (src == "" || src == null){
		return src;
	}
    return src.replace(/^\s+|\s+$/g,"");
}

function encodeForHTML(source)
{
	if (source == "" || source == null){
		return source;
	}
    return source.toString().replace(/&/g,"&amp;").replace(/"/g,"&quot;").replace(/</g,"&lt;").replace(/>/g,"&gt;"); 
} 
function closeCopyPasteDialog()
{
	document.getElementById("items").value = "";
	DialogPanel.hide("copyPasteDialog");
}

function showCopyPastePanel()
{
	//-- Web Trends tag start --
	writeMetaTag("WT.ti,DCSext.w_x_ord_quickadd_cp", "1,Copy and Paste",2);	
	//-- Web Trends tag end --
	DialogPanel.show("copyPasteDialog");
    svg_classhandlers_decoratePage();

    var initialFocus = Ext.get("items");
    
    if(initialFocus != null)
    {
        initialFocus.focus.defer(1000, initialFocus);
    }
}

function editNotes(element)
{
	var note = document.getElementById("orderLineNote_" + element);
	var edit = document.getElementById("editNote_" + element);
	var save = document.getElementById("saveNote_" + element);
	var instructionText = document.getElementById("instructionText_" + element);
	instructionText.style.visibility = "hidden";
	note.style.visibility = "visible";
	save.style.visibility = "visible";
	edit.style.visibility = "hidden";
}

function addNotes(element)
{	
	var note = document.getElementById("orderLineNote_" + element);
	var add = document.getElementById("addNote_" + element);
	var save = document.getElementById("saveNote_" + element);
	note.style.visibility = "visible";
	save.style.visibility = "visible";
	add.style.visibility = "hidden";
}

function saveNotes(element)
{
	document.getElementById("orderLineKeyForNote").value = element;
	document.OrderDetailsForm.action = document.getElementById('updateNotesURL');
	var arrNotes = new Array();        
	arrNotes = document.getElementsByName("orderLineNote");
	for(var i = 0; i < arrNotes.length; i++)
	{
		arrNotes[i].disabled = false;
	}
    document.OrderDetailsForm.submit();
}


function showEditHeader()
{
    DialogPanel.show("editHeaderDialog");
    svg_classhandlers_decoratePage();

    var initialFocus = Ext.get("cartName");
    if(initialFocus == null)
    {
        initialFocus = Ext.get("selectedCurrency");
    }
    if(initialFocus != null)
    {
        initialFocus.focus.defer(1000, initialFocus);
    }
}

function showAllComplementaryItems(orderLineKey)
{
    for(var index=0; ; index++)
    {
        var element = Ext.get("comp" + orderLineKey + "-" + index);
        if(element == null)
        {
            break;
        }

        element.setDisplayed("");
    }

    var element = Ext.get("comp" + orderLineKey + "-more");
    element.setDisplayed("none");

    svg_classhandlers_decoratePage();
}

function addComplmentaryItemToCart(itemID, uom)
{
    document.addComplementaryItemForm.ProductID.value = itemID;
    document.addComplementaryItemForm.ProductUOM.value = uom;
    document.addComplementaryItemForm.submit();
}

function showAlternativeItems(itemIDUOM, orderLineKey, quantity)
{
    clearErrorMessages(document.addAlternativeItemForm);
    clearErrorLabels(document.addAlternativeItemForm);

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

function addAlternativeItem()
{
    clearErrorMessages(document.addAlternativeItemForm);
    clearErrorLabels(document.addAlternativeItemForm);

    if(swc_validateForm("addAlternativeItemForm") == false)
    {
        return;
    }
    document.addAlternativeItemForm.submit();
}

function configPunchIn(orderLineKey, returnURL)
{
    document.configPunchInForm.OrderLineKey.value = orderLineKey;
    document.configPunchInForm.ReturnURL.value = returnURL;
    document.configPunchInForm.submit();
}

Ext.onReady(function() {
    var initialFocus = document.getElementById("qaProductID");
    if(initialFocus == null)
    {
        initialFocus = document.getElementById("checkout1");
    }
    
    if(initialFocus != null)
    {
        try
        {
         initialFocus.focus();
        }
        catch(err){
        initialFocus = document.getElementById("checkout1"); 
       // initialFocus.focus();       
        }
    }
});
function showXPEDXAddSpecialItem(){
    		DialogPanel.show("addXPEDXSpecialItem");
		    svg_classhandlers_decoratePage();
}

function trim(str)
{
	if (str == "" || str == null){
		return str;
	}
	//alert(" Str: " + str);
	var trimmed = str.replace(/^\s+|\s+$/g, '') ;
	return trimmed;
}

var siUIDesc;
var siUIQty;
var siUIJobId;
var siUIUom;

function showSpecialItem(divId) {
	
	var divIndex = divId.charAt((divId.length - 1));
	siUIDesc = "";
	siUIQty = QuickAddElems[divIndex].quantity;
	siUIJobId = QuickAddElems[divIndex].jobId;
	siUIUom = "";
	
	// Display the fancy box
	$.fancybox(
		Ext.get(divId).dom.innerHTML,
		{
        	'autoDimensions'	: true,
			'width'         	: 570,
			'height'        	: 325
		}
	);
}

function updateQuickAddElement(eleName, eleId)
{
	if(eleName=="Qty")
	{
		var siQty = Ext.get("enteredQuantities_" + eleId);
		QuickAddElems[eleId].quantity = siQty.getValue();
	}
	else if(eleName=="JobId")
	{
		var siJobId = Ext.get("enteredJobIDs_" + eleId);
		QuickAddElems[eleId].jobId = siJobId.getValue();
	}
	else if(eleName=="UOM")
	{
		var siUOM = Ext.get("enteredUOMsUI_" + eleId);
		var siHiddenUOM = Ext.get("enteredUOMs_" + eleId);
		QuickAddElems[eleId].uom = siUOM.getValue();
		siHiddenUOM.dom.value = siUOM.getValue();
	}
	else if(eleName=="UOMList")
	{
		var siUOM = Ext.get("enteredUOMsList_" + eleId);
		var siHiddenUOM = Ext.get("enteredUOMs_" + eleId);
		QuickAddElems[eleId].selectedUOM = siUOM.getValue();
		siHiddenUOM.dom.value = siUOM.getValue();
	}
	else if(eleName=="PO")
	{
		var siPO = Ext.get("enteredPONos_" + eleId);
		QuickAddElems[eleId].purchaseOrder = siPO.getValue();
	}	
}


function captureSpecialItemValues(elementName)
{
	var elementId=elementName.id;
	if(elementId.indexOf("siUIDesc_")!=-1){
		siUIDesc=elementName.value;
	}else if(elementId.indexOf("siUIQty_")!=-1){
		siUIQty=elementName.value;
	}else if(elementId.indexOf("siUIJobId_")!=-1){
		siUIJobId=elementName.value;
	}else if(elementId.indexOf("siUIUom_")!=-1){
		siUIUom=elementName.value;
	}
}

function setSpecialItemValues(id) {
	siUIQty = document.getElementById('siUIQty_'+id).value;
	siUIUom = document.getElementById('siUIUom_'+id).value;
}

function addSpecialItem(divId)
{
	var divFieldId= "div" + divId;
	setSpecialItemValues(divFieldId);
	// Get all the values
	if (siUIDesc.length > 200){
		alert(" The description you have entered is longer than the maximum limit 200. Please reenter.");
		return;
	}
	QuickAddElems[divId].quantity = siUIQty;
	QuickAddElems[divId].itemTypeText ="Special Item";
	QuickAddElems[divId].jobId = siUIJobId;
	QuickAddElems[divId].itemDesc = siUIDesc;
	QuickAddElems[divId].uom = siUIUom;

// var siName = Ext.get("siName_" + divId);
	var siDesc 			= Ext.get("enteredProductDescs_" + divId);
	var siQty 			= Ext.get("enteredQuantities_" + divId);
	var siUom 			= Ext.get("enteredUOMs_" + divId);
	var siUomUI 		= Ext.get("enteredUOMsUI_" + divId);
	var siJobId 		= Ext.get("enteredJobIDs_" 		+ divId);
	var siItemId 		= Ext.get("enteredProductIDs_" 	+ divId);
	var siItemType 		= Ext.get("enteredItemTypes_" 	+ divId);
	var siItemTypeText 	= Ext.get("ItemTypesText_" + divId);
	var specialItem 	= Ext.get("enteredSpecialItems_" + divId);
	
	
	// Transfer the values for the records
	// Id is already transfer by the jsp page
// siName.dom.value = siItemId.getValue();
	siDesc.dom.value = siUIDesc;
	siQty.dom.value = siUIQty;
	siUomUI.dom.value = siUIUom;
	siUom.dom.value = siUIUom;
	if (siUIJobId != ''){
		siJobId.dom.value = siUIJobId;
	}
// siItemType.dom.value = "99";
	specialItem.dom.value  	= siItemId.getValue();
	siItemTypeText.update("Special Item") ;
	siUIDesc="";
	siUIQty="";
	siUIJobId="";
	siUIUom="";
	// Show the current record
	var el = Ext.get("div_" + "QickAddItem_" + divId);
	el.setVisibilityMode(Ext.Element.DISPLAY);
	el.show();
	
	// hide the message
	var el = Ext.get("div_" + "enteredSpecialItems_" + divId);
	el.setVisibilityMode(Ext.Element.DISPLAY);
	el.hide();

	// Hide the window
	$.fancybox.close();
	
}

function closeSpecialUIbox()
{
	siUIDesc="";
	siUIQty="";
	siUIJobId="";
	siUIUom="";
	
	// Hide the window
	$.fancybox.close();
}

function convertToUOMDescription(uomCode)
{
    var url=document.getElementById("uomDescriptionURL").value;
    var req=null;
    if (window.XMLHttpRequest){
    	req = new XMLHttpRequest();
    }else{
    	req = new ActiveXObject("Microsoft.XMLHTTP");
    }
    url=url+'&uomCode='+uomCode;
    req.open('POST', url, false);     
    req.send(null);
    if(req.status == 200){
      return req.responseText;
    }else{
    	return uomCode;
    }
}