function checkOut()
{
    var otherForm = document.QuickAddForm;
    clearErrorMessages(otherForm);
    clearErrorLabels(otherForm);

    document.getElementById("validationActionName").value = "saveCartDetails";
    if(swc_validateForm("OrderDetailsForm") == false)
    {
        return;
    }

    document.OrderDetailsForm.action = document.getElementById('checkoutURL');
    document.OrderDetailsForm.submit();
}

function addProductsToOrder()
{
    if(QuickAddElems.length > 0)
    {
        document.QuickAddForm.action = document.getElementById('addProductsToOrderURL');
        document.QuickAddForm.submit();
    }
}

function update()
{
	if(validateOrderMultiple() == false)
	{
		return;
	}
	var otherForm = document.QuickAddForm;
    clearErrorMessages(otherForm);
    clearErrorLabels(otherForm);

    document.getElementById("validationActionName").value = "draftOrderModifyLineItems";
    if(swc_validateForm("OrderDetailsForm") == false)
    {
        return;
    }
    document.OrderDetailsForm.action = document.getElementById('updateURL');
    document.OrderDetailsForm.submit();
}

function validateOrderMultiple()
{
	var arrQty = new Array();
	var arrUOM = new Array();
	var arrItemID = new Array();
	var arrOrdMul = new Array();
	arrQty = document.getElementsByName("orderLineQuantities");
	arrUOM = document.getElementsByName("itemUOMsSelect");
	arrItemID = document.getElementsByName("orderLineItemIDs");
	arrOrdMul =  document.getElementsByName("orderLineOrderMultiple");
	
	for(var i = 0; i < arrItemID.length; i++)
	{
		var totalQty = arrUOM[i].value * arrQty[i].value;
		var ordMul = totalQty % arrOrdMul[i].value;
		if(ordMul != 0)
		{
			alert("Order Quantity for " + arrItemID[i].value + " must be a multiple of " + arrOrdMul[i].value);
			return false;
		}
	}
	return true;
}

function setUOMValue(elementId)
{
	var element = document.getElementById(elementId);
	var uomvalue = element.options[element.selectedIndex].text;
	var lineKey = elementId.substring(15);
	document.getElementById("itemUOMs_" + lineKey).value = uomvalue;
}

function removeItems()
{
    var otherForm = document.QuickAddForm;
    clearErrorMessages(otherForm);
    clearErrorLabels(otherForm);

    document.getElementById("validationActionName").value = "draftOrderDeleteLineItems";
    if(swc_validateForm("OrderDetailsForm") == false)
    {
        return;
    }

    document.OrderDetailsForm.action = document.getElementById('removeItemsURL');
    document.OrderDetailsForm.submit();

    return;
}

function trim(src) {
    return src.replace(/^\s+|\s+$/g,"");
}

var QuickAddElems = new Array();
var deleteStringFromForm = "";
var addStringFromForm = "";

function addProductToQuickAddList(element)
{
	var theForm = element.form;
    var otherForm = document.OrderDetailsForm;
    clearErrorMessages(otherForm);
    clearErrorLabels(otherForm);

    var sku = trim(theForm.qaProductID.value);
    var quantity = trim(theForm.qaQuantity.value);
    var jobId = trim(theForm.qaJobID.value);
    var itemType = trim(theForm.qaItemType.value);
    
    if(sku == "")
    {
        alertString = theForm.localizedMissingProductIDMessage.value;
        alert(alertString);
        return;
    }
    if(quantity == "")
    {
        quantity = 1;
        theForm.qaQuantity.value = quantity;
    }
    
    document.getElementById("validationActionNameQA").value = "draftOrderAddOrderLines";
    if(swc_validateForm("QuickAddForm") == false)
    {
        theForm.qaQuantity.focus();
        return;
    }

    QuickAddElems[QuickAddElems.length] =
    {
            sku: sku,
            quantity: quantity,
            jobId: jobId,
            itemType: itemType,
            isEntitled: "true"
    }

    theForm.qaProductID.value = "";
    theForm.qaQuantity.value = "";
    theForm.qaJobID.value = "";
    theForm.qaItemType.value = "Xpedx #";
    
    // Kludge to get localized string from the form for use in the HTML
    // generated by redrawQuickAddList.  It counts on the fact that the
    // first time redrawQuickAddList will be called on the page is for
    // the add.  If it were to be called beforehand, there would be no
    // deleteStringFromForm set.
    if(deleteStringFromForm == "")
    {
        deleteStringFromForm = theForm.localizedDeleteLabel.value;
    }
    if(addStringFromForm == "")
    {
        addStringFromForm = theForm.localizedAddToCartLabel.value;
    }
    
    //redrawQuickAddList(element);
    validateItems();
    theForm.qaProductID.focus();

    return false;
}

function removeProductFromQuickAddList(index)
{
    QuickAddElems.splice(index, 1);
  
    redrawQuickAddList();
}

function encodeForHTML(source)
{
    return source.toString().replace(/&/g,"&amp;").replace(/"/g,"&quot;").replace(/</g,"&lt;").replace(/>/g,"&gt;"); 
} 

function redrawQuickAddList()
{
	var tabIndex = 210;
    var code = '<table cellspacing="0" cellpadding="0" id="QuickAddTable">';
    	code += '<thead> <tr>';
    	code += '<th class="del-col">&nbsp;</th>';
    	code += '<th class="first-col-header col-header type-col">Item Type</th>';
    	code += '<th class="col-header item-col">Item #</th>';
    	code += '<th class="col-header qty-col">Quantity</th>';
    	code += '<!--  <th class="col-header uom-col">UOM</th> -->';
    	code += '<th class="last-col-header col-header job-col">Job#</th>';
    	code += '<th class="last-col-header col-header job-col">Valid Product</th>';
    	code += '<th class="last-col-header col-header job-col">Add as Special Item</th>';
    	code += '</tr>    		</thead>';
    for(var i=0 ; i < QuickAddElems.length ; i++)
    {
    	code += '<tr>';
    	code += '<td align="left" width="10%" class="wing-col-item">';
	    code += '<a href="#" class="del-quick-add" onclick="javascript:removeProductFromQuickAddList(' + i + ');" tabindex="' + tabIndex++ + '">x</a>';
	    code += '</td>';
	    code += '<td align="left" width="20%">';
        code += encodeForHTML(QuickAddElems[i].itemType);
        code += '<input type="hidden" name="enteredItemTypes" value="' + encodeForHTML(QuickAddElems[i].itemType) + '"/>';
        code += '</td>';
        code += '</td>';
        code += '<td align="left" width="15%">';
        code += encodeForHTML(QuickAddElems[i].sku);
        code += '<input type="hidden" name="enteredProductIDs" value="' + encodeForHTML(QuickAddElems[i].sku) + '"/>';
        code += '</td>';
        code += '<td align="right" width="20%">';
        code += encodeForHTML(QuickAddElems[i].quantity);
        code += '<input type="hidden" name="enteredQuantities" value="' + encodeForHTML(QuickAddElems[i].quantity) + '"/>';
        code += '</td>';
        code += '<td align="right" width="10%">'; 
        code += encodeForHTML(QuickAddElems[i].jobId); 
        code += '<input type="hidden" name="enteredJobIDs" value="' + encodeForHTML(QuickAddElems[i].jobId) + '"/>';
        if(QuickAddElems[i].isEntitled == "false")
        {
        	var fieldId = "enteredSpecialItems_" + i;
        	code += '<td align="right" width="15%">';
	        code += 'Product Invalid/Not Entitled';
	        code += '</td>';
	        
	        code += '<td align="right" width="20%">';
	        code += '<input type="checkbox" name="enteredSpecialItems" id="' + encodeForHTML(fieldId) + '" value="' + encodeForHTML(QuickAddElems[i].sku) + '"/>';
	        code += '</td>';
        }
	       
    }
    code += '<tr>';
    if(QuickAddElems.length > 0)
    {	
        code += '<td align="right" colspan="5">';
        code += '<input type="image" onclick="javascript:addProductsToOrder()" name="addProdsToOrder"  class="add-to-cart-btn" src="'+ XPEDXWCUtils_STATIC_FILE_LOCATION +'/xpedx/images/theme/theme-1/quick-add/addtocart.png" tabIndex="' + tabIndex++ + '"/>';
        code += '</td>';
        code += '</tr>';
    }
    code += '</table>';

    theDiv = document.getElementById("QuickAddList");
    theDiv.innerHTML = code;

    svg_classhandlers_decoratePage();
}

function addItemsToQuickAddList()
{
	var itemsString = document.getElementById("items").value;
	if(itemsString==''){
		alert('Enter valid string');
		return false;
	}
	var char = '\n';
	var itemLines = itemsString.split(char);
	
	for(var i=0;i < itemLines.length; i++)
	{
		var itemQty = null;
		var itemSku = null;
		var jobId = "";
		var itemLine = itemLines[i].split('\t');
		if(itemLine.length > 1 )
		{
			itemQty = itemLine[0];
			itemSku = itemLine[1];
		}
		itemLine = itemLines[i].split(',');
		if(itemLine.length > 1 )
		{
			itemQty = itemLine[0];
			itemSku = itemLine[1];
		}
		
		itemSku = trim(itemSku);
		itemQty = trim(itemQty);
		
		QuickAddElems[QuickAddElems.length] =
	    {
	            sku: itemSku,
	            quantity: itemQty,
	            itemType: "Xpedx #",
	            jobId: jobId,
	            isEntitled: "true"
	    }
	}	
	if(deleteStringFromForm == "")
    {
        deleteStringFromForm = document.QuickAddForm.localizedDeleteLabel.value;
    }
    if(addStringFromForm == "")
    {
        addStringFromForm = document.QuickAddForm.localizedAddToCartLabel.value;
    }
    
	//redrawQuickAddList();
    validateItems();
	document.getElementById("items").value = "";
	DialogPanel.hide("copyPasteDialog");
}

function showCopyPastePanel()
{
	DialogPanel.show("copyPasteDialog");
    svg_classhandlers_decoratePage();

    var initialFocus = Ext.get("items");
    
    if(initialFocus != null)
    {
        initialFocus.focus.defer(1000, initialFocus);
    }
}

/*function editNotes(element)
{
	var note = document.getElementById("orderLineNote_" + element);
	var edit = document.getElementById("editNote_" + element);
	var save = document.getElementById("saveNote_" + element);
	note.disabled = false;
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
} */

function addItemsToList()
{
	document.OrderDetailsForm.action = document.getElementById('addItemsToListURL');
	document.getElementById("listKey").value = document.getElementById("itemListSelect").value;
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
        initialFocus = document.getElementById("qaQuantity");
    }
    
    
    if(initialFocus != null)
    {
        initialFocus.focus();
    }
});
function showXPEDXAddSpecialItem(){
    		DialogPanel.show("addXPEDXSpecialItem");
		    svg_classhandlers_decoratePage();
}

function validateItems()
{
	var url = document.getElementById('productValidateURL');
	var itemsString = "";
	var itemTypesString = ""
	var len = QuickAddElems.length;
	for(var index=0; index < len; index++)
    {
		var itemId = QuickAddElems[index].sku;
		var itemType = QuickAddElems[index].itemType;
		if(index != (len-1))
		{
			itemsString = itemsString + itemId + "*";
			itemTypesString = itemTypesString + itemType + "*";
		}
		else
		{
			itemsString =  itemsString + itemId ;
			itemTypesString = itemTypesString + itemType;
		}
    }
	
	Ext.Ajax.request({   
        url: url,   
        params: {itemList:itemsString, itemTypeList:itemTypesString}, //in XML format   
        success:  function (response, request){
        	var res = response.responseText;
        	var itemValid = res.split(",");
        	var noOfItems = QuickAddElems.length;
        	for(var index=0; index < noOfItems; index++)
            {
        		var entitled = trim(itemValid[index]);
        		QuickAddElems[index].isEntitled = entitled;
            }
        	
        	redrawQuickAddList();
        },   
        failure: function (response, request){
        	alert("failure");
        }
    });
}

function trim(str)
{
	var trimmed = str.replace(/^\s+|\s+$/g, '') ;
	return trimmed;
}
