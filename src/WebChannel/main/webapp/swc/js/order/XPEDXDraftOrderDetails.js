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
	if(validateOrderMultiple() == false)
	{
		return;
	}
    var otherForm = document.QuickAddForm;
    clearErrorMessages(otherForm);
    clearErrorLabels(otherForm);

    document.getElementById("validationActionName").value = "saveCartDetails";
    if(swc_validateForm("OrderDetailsForm") == false)
    {
        return;
    }
    document.OrderDetailsForm.orderName.value = document.getElementById('cartName_new').value;
    document.OrderDetailsForm.orderDesc.value = document.getElementById('cartDesc_new').value; 
    document.OrderDetailsForm.isComingFromCheckout.value = "true";
    document.OrderDetailsForm.action = document.getElementById('checkoutURL');
    document.OrderDetailsForm.submit();
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
					else if(ordMul != 0 || totalQty ==0)
					{
						isError = true;
						//Added for 3098
						document.getElementById(divId).innerHTML ="Please order in units of " + orderMultiple +" "+baseUOM[i].value;
						document.getElementById(divId).style.display = "inline-block";
						document.getElementById(divId).style.marginLeft = "20px";
						document.getElementById(divId).setAttribute("class", "error");
						document.getElementById(divId).setAttribute("align", "center");
						noError = false;
					}
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

function resetQuantityErrorForQuckAdd()
{
	for(var i=0 ; i < QuickAddElems.length ; i++)
	 {
		var divId='errorQty_'+QuickAddElems[i].sku +i;
		var divVal=document.getElementById(divId);
		if(divVal != null)
			divVal.innerHTML='';
	}
}

function update()
{
	document.OrderDetailsForm.isComingFromCheckout.value = "false";
	if(document.getElementById('cartName_new').value.trim() == "")
	{
		//commented for 3098
		//alert("Cart name can't be blank, please add a valid name for cart.");
		document.getElementById("errorMsgTop").innerHTML = "Name is required." ;
        	document.getElementById("errorMsgTop").style.display = "inline";
        
        	document.getElementById("errorMsgBottom").innerHTML = "Name is required." ;
        	document.getElementById("errorMsgBottom").style.display = "inline";
		//3098
		
		return;
	}
	var orderLinesCount = document.OrderDetailsForm.OrderLinesCount.value;
	if(orderLinesCount > 0)
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
	    document.OrderDetailsForm.orderName.value = document.getElementById('cartName_new').value;
	    document.OrderDetailsForm.orderDesc.value = document.getElementById('cartDesc_new').value;
	    document.OrderDetailsForm.modifyOrderLines.value = "true";
	    document.OrderDetailsForm.action = document.getElementById('updateURL');
	    document.OrderDetailsForm.submit();
	}
	else
	{
		document.OrderDetailsForm.orderName.value = document.getElementById('cartName_new').value;
		document.OrderDetailsForm.orderDesc.value = document.getElementById('cartDesc_new').value;
		document.OrderDetailsForm.zeroOrderLines.value = "true";
		document.OrderDetailsForm.action = document.getElementById('updateNoLinesURL');
		document.OrderDetailsForm.submit();
	}
}

function validateOrderMultiple()
{
	 resetQuantityErrorMessage();
	var arrQty = new Array();
	var arrUOM = new Array();
	var arrItemID = new Array();
	var arrOrdMul = new Array();
	var baseUOM = new Array();
	var retVal=true;
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
		if(ordMul != 0 && zeroError == false)
		{			
			if(divIdError != null){
				divIdError.innerHTML ="Please order in units of " +addComma(arrOrdMul[i].value) +" "+baseUOM[i].value;
				divIdError.style.display = "inline-block"; 
				divIdError.setAttribute("class", "error");
			}
			retVal=false;
			noError=false;
		}
		if(arrOrdMul[i].value > 1 && noError==true) {
			if(divIdError != null){
				divIdError.innerHTML ="Must be ordered in units of " +addComma(arrOrdMul[i].value) +" "+baseUOM[i].value;
				divIdError.style.display = "inline-block"; 
				divIdError.setAttribute("class", "notice");	
			}
		}
	}
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
    var otherForm = document.QuickAddForm;
    clearErrorMessages(otherForm);
    clearErrorLabels(otherForm);

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

var QuickAddElems = new Array();
var deleteStringFromForm = "";
var addStringFromForm = "";
var custPOFlag = false;
var jobidFlag = false;

function addProductToQuickAddList(element)
{
	//alert("-LP11- Shopping Cart addProductToQuickAddList ");
	var theForm = element.form;
    var otherForm = document.OrderDetailsForm;
    clearErrorMessages(otherForm);
    clearErrorLabels(otherForm);

    var sku = trim(theForm.qaProductID.value);
    var quantity = trim(theForm.qaQuantity.value);
    var jobId = "";
    if(theForm.qaJobID != null){
    	jobId = trim(theForm.qaJobID.value);
    	jobidFlag = true;
    }
    var itemType = trim(theForm.qaItemType.value);
    var itemTypeText = itemType;
    
    var itemTypeSelElem = theForm.qaItemType;
    if(itemTypeSelElem!=null){
    	itemType = itemTypeSelElem.options[itemTypeSelElem.selectedIndex].value;
    	itemTypeText = itemTypeSelElem.options[itemTypeSelElem.selectedIndex].text;
    }
    
    var uomArray = new Array();
    var purchaseOrder = "";
    if(theForm.purchaseOrder != null){
    	purchaseOrder = theForm.purchaseOrder.value;
    	custPOFlag = true;
    }
    if(sku == "")
    {
        //Added to fix 3098
    	//alertString = theForm.localizedMissingProductIDMessage.value;
        //alert(alertString);
    	document.getElementById("errorMsgItemBottom").innerHTML = "Please enter a valid Item #." ;
        document.getElementById("errorMsgItemBottom").style.display = "inline";
        return;
    }
    else
    	{
        document.getElementById("errorMsgItemBottom").innerHTML = "";
        document.getElementById("errorMsgItemBottom").style.display = "none";
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
            purchaseOrder: purchaseOrder,
            itemType: itemType,
            itemTypeText: itemTypeText,
            itemDesc: "",
            uom: "",
            uomList: uomArray,
            isValidated: "false",
            isEntitled: "true",
            orderMultiple:"",
            itemUomAndConvString:"",
            //Added selectedUOM for Jira 3862
            selectedUOM:""
    }

    theForm.qaProductID.value = "";
    theForm.qaQuantity.value = "";
    if(theForm.qaJobID != null){
    	theForm.qaJobID.value = "";
    }
    if(theForm.purchaseOrder != null){
    	theForm.purchaseOrder.value = "";
    }
    theForm.qaItemType.value = "1";
    
    // Kludge to get localized string from the form for use in the HTML
    // generated by redrawQuickAddList. It counts on the fact that the
    // first time redrawQuickAddList will be called on the page is for
    // the add. If it were to be called beforehand, there would be no
    // deleteStringFromForm set.
    if(deleteStringFromForm == "")
    {
        deleteStringFromForm = theForm.localizedDeleteLabel.value;
    }
    if(addStringFromForm == "")
    {
        addStringFromForm = theForm.localizedAddToCartLabel.value;
    }
    redrawQuickAddList(element);
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
	if (source == "" || source == null){
		return source;
	}
    return source.toString().replace(/&/g,"&amp;").replace(/"/g,"&quot;").replace(/</g,"&lt;").replace(/>/g,"&gt;"); 
} 

function redrawQuickAddList()
{	
	var tabIndex = 210;
	var showAddtoCartBtn = false;
    var code = '<table cellspacing="0" cellpadding="0" id="QuickAddTable" width="96%">';
    var jobValue;
    addToCartDiv = document.getElementById("addProdsToOrder");
    addToCartDiv.style.display = 'none';
    if(document.QuickAddForm.jobIdValue != undefined && document.QuickAddForm.jobIdValue.value !=null )	
    	 jobValue = document.QuickAddForm.jobIdValue.value;
    
    if(QuickAddElems.length > 0){
    	
    	code += '<thead><tr>';
    	code += '<th class="del-col">&nbsp;</th>';
    	code += '<th class="first-col-header col-header type-col">Item Type</th>';
    	code += '<th class="col-header item-col">Item #</th>';
    	code += '<th class="col-header qty-col">Qty</th>';
    	code += '<th class="col-header uom-col">UOM</th>';
    	if(jobidFlag == true && jobValue!= null)
    	code += '<th class="last-col-header col-header job-col">'+jobValue+'</th>';
    	if(custPOFlag)
    		code += '<th class="last-col-header col-header job-col">Line PO #</th>';
    	code += '<!--  <th class="col-header qty-col">Valid Product</th> -->';
    	code += '<!--  <th class="last-col-header col-header job-col">Add as Special Item</th> -->';
    	code += '</tr></thead>';
    	code += '<tbody>';
    }    
    	
    for(var i=0 ; i < QuickAddElems.length ; i++)
    {
    	var fieldId = "div_" + "QickAddItem_" + i;
    	showAddtoCartBtn = true;
    	addToCartDiv.style.display = 'inline';
    	if(QuickAddElems[i].isValidated == "false")
    	{
    		code += '<tr id="' + encodeForHTML(fieldId) + '">';
    		code += '<td class="wing-col-item">';
		    code += '<a href="#" onclick="javascript:removeProductFromQuickAddList(' + i + ');" title="Remove" tabindex="' + tabIndex++ + '"><img src="/swc/xpedx/images/icons/12x12_red_x.png"/></a>';
		    code += '</td>';
		    
	    	code += '<td class="col-item" colspan="4">';
	    	code += '<span>Validating item ' + encodeForHTML(QuickAddElems[i].sku) + ' Processing... </span>';
	    	code += '</td>';
	    	code += '</tr>';
	    	
	    	showAddtoCartBtn = false;
    	}
    	else
    	{
		    	if(QuickAddElems[i].itemTypeText == "Special Item")
		    	{
		    		code += '<tr id="' + encodeForHTML(fieldId) + '">';
		    	}
		    	else if(QuickAddElems[i].isEntitled == "false")
		    	{
		    		code += '<tr id="' + encodeForHTML(fieldId) + '" style="display: none;" >';
		    	}
		    	else
		    	{
		    		code += '<tr id="' + encodeForHTML(fieldId) + '">';
		    	}
		    	var divIdErrorQty="errorQty_"+QuickAddElems[i].sku+i;
		    	//var divIdErrorQty0="errorQty0_"+QuickAddElems[i].sku+i;
		    	code += '<td class="wing-col-item">';
			    code += '<a href="#" onclick="javascript:removeProductFromQuickAddList(' + i + ');" title="Remove" tabindex="' + tabIndex++ + '"><img src="/swc/xpedx/images/icons/12x12_red_x.png" /></a>';
			    code += '</td>';
			    code += '<td class="col-item">';
			    code +=	'<div id="ItemTypesText_' + i + '">'
		        code += encodeForHTML(QuickAddElems[i].itemTypeText);
			    code += '</div>'
		        code += '<input type="hidden" name="enteredItemTypes" id="enteredItemTypes_' + i + '" value="' + encodeForHTML(QuickAddElems[i].itemType) + '"/>';
			    code += '<input type="hidden" name="orderLineOrderMultiples" id="orderLineOrderMultiple_' + i + '" value="' + encodeForHTML(QuickAddElems[i].orderMultiple) + '"/>';
			    code += '<input type="hidden" name="itemUomAndConvStrings" id="itemUomAndConvStrings_' + i + '" value="' + encodeForHTML(QuickAddElems[i].itemUomAndConvString) + '"/>';
		        code += '</td>';
		        code += '<td class="col-item"><p style="width:55px; word-wrap:break-word;">';
		        code += encodeForHTML(QuickAddElems[i].sku);
		        code += '</P><input type="hidden" name="enteredProductIDs" id="enteredProductIDs_' + i + '" value="' + encodeForHTML(QuickAddElems[i].sku) + '"/>';
		        
		        code += '</td>';
		        code += '<td class="col-item">';
		        code +='<input type="hidden" name="quickAddBaseUOMs" value='+convertToUOMDescription(encodeForHTML(QuickAddElems[i].uom)) +' />';
		// code += encodeForHTML(QuickAddElems[i].quantity);
		        if(QuickAddElems[i].itemTypeText == "Special Item"){
		        	code += '<input type="text" disabled="disabled" value="1" name="enteredQuantities" id="enteredQuantities_' + i + '" onchange="javascript:isValidQuantity(this);" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" onblur="javascript:updateQuickAddElement(\'Qty\','+ i +');"  />';
		        	code += '<input type="hidden" value="1" name="enteredQuantities" id="enteredQuantities_' + i + '" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" onchange="javascript:isValidQuantity(this);" onblur="javascript:updateQuickAddElement(\'Qty\','+ i +');"  />';
		        }else{
		        	code += '<input type="text" name="enteredQuantities" id="enteredQuantities_' + i + '" value="' + encodeForHTML(QuickAddElems[i].quantity) + '" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" onchange="javascript:isValidQuantity(this);" onblur="javascript:updateQuickAddElement(\'Qty\','+ i +');" />';
		        	
		        }
		        code += '</td>';
		        if(QuickAddElems[i].isEntitled == "false")
		        {
			        code += '<td class="col-item">'; 
			        if(QuickAddElems[i].itemTypeText == "Special Item")
			    	{
			        	code += '<input type="text" value="EACH" disabled = "true" name="enteredUOMsUI" id="enteredUOMsUI_' + i + '" onchange="javascript:updateQuickAddElement(\'UOM\','+ i +')" />';
			        	code += '<input type="hidden" name="enteredUOMsUI" id="enteredUOMsUI_' + i + '" value="' + encodeForHTML(QuickAddElems[i].uom) + '" onchange="javascript:updateQuickAddElement(\'UOM\','+ i +')" />';
			    	}else{
			    		//code += '<input type="text" name="enteredUOMsUI" id="enteredUOMsUI_' + i + '" value="' + encodeForHTML(QuickAddElems[i].uom) + '" onchange="javascript:updateQuickAddElement(\'UOM\','+ i +')" />';
			    		code += '<input type="hidden" name="enteredUOMs" id="enteredUOMs_' + i + '" value="' + encodeForHTML(QuickAddElems[i].uom) + '" />';
			    	}
			        code += '</td>';
		        }
		        else
		        {
		        	//Start - Code added to fix XNGTP 2964					
		        	var msapExtnUseOrderMulUOMFlag = document.getElementById('msapOrderMulUOMFlag').value;
		        	if((msapExtnUseOrderMulUOMFlag!=null && msapExtnUseOrderMulUOMFlag == 'Y')&&(QuickAddElems[i].orderMultiple >"1" && QuickAddElems[i].orderMultiple != null))
		        	 
					  {
		        		
		        	var uomValues = QuickAddElems[i].uomList;
			        	var _uomCodes = QuickAddElems[i].uomCodes;
			        	code += '<td class="col-item">'; 
					    code += '<select name="enteredUOMsList" id="enteredUOMsList_' + i + '" onchange="javascript:updateQuickAddElement(\'UOMList\','+ i +')" >';
					    var storeUOM = 0;
						var minSelUOM;
				    	var maxSelUOM;
				    	var minFractUOM = 0.00;
				    	var maxFractUOM = 0.00;
				    	var defaultSelUOM;
				    	var _oUomCode;
				    	var _oUomDescription;
				    	var firstIndex;
				    	var lastIndex; 
				    	var orderMultipleValue = QuickAddElems[i].orderMultiple;
			        	 for(var oUomidx =0; oUomidx < uomValues.length; oUomidx++){
				    		 _oUomCode = uomValues[oUomidx];  
							 _oUomDescription=convertToUOMDescription(_oUomCode);
							 firstIndex = uomValues[oUomidx].indexOf('(');
					    	 lastIndex = uomValues[oUomidx].indexOf(')');
					    	var convFactor;
					    	var oFraction = 0.00;
					    	if(lastIndex>0 && firstIndex>0){
					    		 convFactor = uomValues[oUomidx].substring(firstIndex+1,lastIndex);
				    	 	} else {
				    	 		convFactor = 1;
				    	 	}
					    	if(convFactor == orderMultipleValue)
						     {
					    	 	minSelUOM = maxSelUOM = _oUomCode;
					    		minFractUOM = maxFractUOM = 1;
					    		break;
						     } else {
						    	 oFraction = convFactor/orderMultipleValue;
						    	 if(oFraction <= 1 && oFraction > minFractUOM) {
						    		 minFractUOM = oFraction;
						    		 minSelUOM = _oUomCode;
						    			
							     } else if(oFraction > 1 && (oFraction < maxFractUOM || maxFractUOM == 0.00)){
							    	 
							    	 maxFractUOM = oFraction;
							    	 maxSelUOM = _oUomCode;
							    	 
							     }
						     }
					    	
				    	}
				    	if(minFractUOM == 1){
				    		 defaultSelUOM = minSelUOM;
				    			
				    		  
				    	} else if(maxFractUOM > 0){
					    		defaultSelUOM = maxSelUOM;						    	
					    		
					    } else if(minFractUOM != null){
				    		defaultSelUOM = minSelUOM;
				    		
				    	}
				    	
			       			    			   
		        	//Passing selUOM as selcted - Done For Jira 3841/3862
				    var selUOM= QuickAddElems[i].selectedUOM;
				    for(var uomidx =0; uomidx < uomValues.length; uomidx++)
				    {
				    	var _uomCode=encodeForHTML(uomValues[uomidx]);
				    	//customizeUOM This var is used to customize the UOM(like M_SHT_C(36) to M_SHT) - Done for Jira 3841
				    	var customizeUOM= uomValues[uomidx];
				    	var testArray = new Array();
				    	testArray = customizeUOM.split("(");
				    	//fvar is the final var storing customize UOM- Done for Jira 3841
				    	var fvar=testArray[0];
				    	firstIndex = uomValues[uomidx].indexOf('(');
				    	lastIndex = uomValues[uomidx].indexOf(')');
				    	var _uomDescription;
				    	if(firstIndex != -1 && lastIndex != -1 && lastIndex> firstIndex) 
				    	{
				    		_uomDescription = convertToUOMDescription(_uomCode.substring(0,firstIndex))+uomValues[uomidx].substring(firstIndex,lastIndex)+')';
				    	} else {
				    		_uomDescription = convertToUOMDescription(_uomCode);
				    	}
				    	//Condn added to check if selected UOM is equal to any alernate UOMs - Done for Jira 3841
				    	if(selUOM!='' && selUOM == fvar){
				    		code += '<option value="' + encodeForHTML(uomValues[uomidx]) + '" selected="yes">' + _uomDescription + '</option>'
				    	}
				    	//else we are doing defaulting of UOMs as it is.
				    	else if(defaultSelUOM.trim() == uomValues[uomidx] && selUOM=='')
				    	{
				    		if(firstIndex!= -1) {	
				    			code += '<option value="' + encodeForHTML(uomValues[uomidx].substring(0,firstIndex)) + '" selected="yes">' + _uomDescription + '</option>'
				    		}
				    		else {
				    			code += '<option value="' + encodeForHTML(uomValues[uomidx])+ '" selected="yes">' + _uomDescription + '</option>'
				    		}
				    	} 
				    	else
				    	{
				    		if(firstIndex!= -1) {	
				    			code += '<option value="' + encodeForHTML(uomValues[uomidx].substring(0,firstIndex)) +'" >' + _uomDescription + '</option>'
				    		}
				    		else {
				    			code += '<option value="' + encodeForHTML(uomValues[uomidx])+ '" >' + _uomDescription + '</option>'
				    		}
				    	} 
				    	 
				    }
				   
			        	
					    }			  
					  				  
					  else{	
						  var uomValues = QuickAddElems[i].uomList;
				        	var _uomCodes = QuickAddElems[i].uomCodes;
				        	code += '<td class="col-item">'; 
						    code += '<select name="enteredUOMsList" id="enteredUOMsList_' + i + '" onchange="javascript:updateQuickAddElement(\'UOMList\','+ i +')" >';
						  //********
						    //Passing selUOM as selcted - Done For Jira 3841/3862
						    var selUOM= QuickAddElems[i].selectedUOM;
						   // alert("selUOM="+selUOM);
						    for(var uomidx =0; uomidx < uomValues.length; uomidx++)
						    {
						    	var _uomCode=encodeForHTML(uomValues[uomidx]);
						    	//******
						    	//customizeUOM This var is used to customize the UOM(like M_SHT_C(36) to M_SHT) - Done for Jira 3841
						    	var customizeUOM= uomValues[uomidx];
						    	var testArray = new Array();
						    	testArray = customizeUOM.split("(");
						    	//fvar is the final var storing customize UOM- Done for Jira 3841
						    	var fvar=testArray[0];
						    	//alert("fvar"+fvar);
						    	//******
						    	var _uomDescription=convertToUOMDescription(_uomCode);
						    	var firstIndex = uomValues[uomidx].indexOf('(');
						    	var lastIndex = uomValues[uomidx].indexOf(')');
						    	if(firstIndex != -1 && lastIndex != -1 && lastIndex> firstIndex) 
						    	{
						    		_uomDescription = convertToUOMDescription(_uomCode.substring(0,firstIndex))+uomValues[uomidx].substring(firstIndex,lastIndex)+')';
						    	}
						    	//*****************
						    	//Condn added to check if selected UOM is equal to any alernate UOMs - Done for Jira 3841
						    	if(selUOM!='' && selUOM == fvar){
						    		code += '<option value="' + encodeForHTML(uomValues[uomidx]) + '" selected="yes">' + _uomDescription + '</option>'
						    	}
						    	//else we are doing defaulting of UOMs as it is.
						    	
						    	//*************
						    	else if(_uomCodes[uomidx].trim() == QuickAddElems[i].uom.trim() && selUOM=='')
						    	{
						    		if(firstIndex!= -1) {				    			
						    			code += '<option value="' + encodeForHTML(uomValues[uomidx].substring(0,firstIndex)) + '" selected="yes">' + _uomDescription + '</option>'
						    		}
						    		else {
						    			code += '<option value="' + encodeForHTML(uomValues[uomidx])+ '" selected="yes">' + _uomDescription + '</option>'
						    		}
						    	}
						    	else
						    	{
						    		if(firstIndex!= -1) {				    			
						    			code += '<option value="' + encodeForHTML(uomValues[uomidx].substring(0,firstIndex)) +'" >' + _uomDescription + '</option>'
						    		}
						    		else {
						    			code += '<option value="' + encodeForHTML(uomValues[uomidx])+ '" >' + _uomDescription + '</option>'
						    		}
						    	}    
						    }
						
					  }
		        	var createNewHiddenField=true;
		        	if(defaultSelUOM == undefined && selUOM==''){
		        		code += '<input type="hidden" name="enteredUOMs" id="enteredUOMs_' + i + '" value="' + encodeForHTML(QuickAddElems[i].uom) + '" />';
		        		createNewHiddenField=false;
		        	}
		        	else if(!selUOM==''){
			        	var selectedUOMQty = selUOM.split(" ");
			        	var selectedUOMs;
						if(selectedUOMQty.length == 2){
							selectedUOMs =selectedUOMQty[0];
						}
						else{
							selectedUOMs = selectedUOMQty;
						}
			        	
		        	}
		        	else{
		        	var selectedUOMQty = defaultSelUOM.split(" ");
		        	var selectedUOMs;
					if(selectedUOMQty.length == 2){
						selectedUOMs =selectedUOMQty[0];
					}
					else{
						selectedUOMs = selectedUOMQty;
					}
		        	}
					
				    	 code += '</select>';
				    	 //Added selUOM condn.If UOM is not changed,pass default UOM,else pass selUOM. - Jira 3841
				    	 if(defaultSelUOM != undefined && selUOM=='' && createNewHiddenField){
				    		 code += '<input type="hidden" name="enteredUOMs" id="enteredUOMs_' + i + '" value="' + selectedUOMs + '" />';	
				    		 
				    	 }
				    	 else if(createNewHiddenField){
				    		 code += '<input type="hidden" name="enteredUOMs" id="enteredUOMs_' + i + '" value="' + selUOM + '" />'; 
				    	 }
				    	code += '</td>';

				    	
				    	
		        }//End - Code added to fix XNGTP 2964					
	        	if(document.QuickAddForm.jobIdValue != undefined && document.QuickAddForm.jobIdValue.value !=null )
		            	 jobValue = document.QuickAddForm.jobIdValue.value;
		        
		        if(jobidFlag == true && jobValue !=null){
			        code += '<td class="col-item">'; 
			        //code += encodeForHTML(QuickAddElems[i].jobId);
			        code += '<input type="text" name="enteredJobIDs" maxlength="25" id="enteredJobIDs_' + i + '" value="' + encodeForHTML(QuickAddElems[i].jobId) + '" onchange="javascript:updateQuickAddElement(\'JobId\','+ i +')"/>';
			        code += '</td>';
		        }
		        if(custPOFlag){
		        	code += '<td class="col-item">';
		        	code += '<input type="text" name="enteredPONos" maxlength="25" id="enteredPONos_' + i + '" value="' + encodeForHTML(QuickAddElems[i].purchaseOrder) + '" onchange="javascript:updateQuickAddElement(\'PO\','+ i +')"/>';
		        	code += '</td>';
		        }
		        var itemDescription = "enteredProductDescs_" + i;
		        code += '<input type="hidden" name="enteredProductDescs" id="' + encodeForHTML(itemDescription) + '" value="' + encodeForHTML(QuickAddElems[i].itemDesc) + '"/>';
		        
		        if(QuickAddElems[i].itemTypeText == "Special Item")
		        {
		        	var checkedfieldId = "enteredSpecialItems_" + i;
		        	code += '<input type="hidden" name="enteredSpecialItems" id="' + encodeForHTML(checkedfieldId) + '" value="' + encodeForHTML(QuickAddElems[i].sku) + '"/>';		        	
		        }
		        else if(QuickAddElems[i].isEntitled == "false")
		        {
		        	var checkedfieldId = "enteredSpecialItems_" + i;
		        	
			        // code += '<td style="display:none;">';
			        code += '<input type="hidden" name="enteredSpecialItems" id="' + encodeForHTML(checkedfieldId) + '" value=""/>';
			       // code += '</td>';
		        }
		        
		        code += '</tr>';
		        if (QuickAddElems[i].itemTypeText != "Special Item" && QuickAddElems[i].isEntitled == "false"){
		        	code += '<tr class="error-row">';
				    code += '<td colspan="6">';
			        code += '<div id="'+divIdErrorQty+'"></div>';
			        code += '</td>';
			        code += '</tr>';
		        }
		        else if(QuickAddElems[i].orderMultiple >"1" && QuickAddElems[i].orderMultiple != null){
		        code += '<tr class="error-row">';
		        code += '<td colspan="6">';
		        code += '<div align="center" class="notice" id="'+divIdErrorQty+'" style="display : inline;">Must be ordered in units of '+ addComma(QuickAddElems[i].orderMultiple) +'&nbsp;'+convertToUOMDescription(encodeForHTML(QuickAddElems[i].uom))+'</div>';
		        code += '</td>';
		        code += '</tr>';
		        }
		        else{
		        code += '<tr class="error-row">';
			    code += '<td colspan="6">';
		        code += '<div id="'+divIdErrorQty+'"></div>';
		        code += '</td>';
		        code += '</tr>';
		        }
		    	if(QuickAddElems[i].itemTypeText != "Special Item" && QuickAddElems[i].isEntitled == "false")
		    	{
		    		var specialId = "div_" + "enteredSpecialItems_" + i;
		    		var specialDivId = "divSpecialItemContent_" + i;
		    		var divFieldId= "div" + i;
		    		
		    		code += '<tr id="' + encodeForHTML(specialId) + '">';
		    		
		    		code += '<td class="wing-col-item">';
				    code += '<a href="#" class="del-quick-add" onclick="javascript:removeProductFromQuickAddList(' + i + ');" title="Remove" tabindex="' + tabIndex++ + '"><img src="/swc/xpedx/images/icons/12x12_red_x.png" /></a>';
				    code += '</td>';
				    
				  //Start Jira 3098
			    	code += '<td class="col-item" style="color:red" colspan="3" >';
			    	// TODO -FXD- : make the following line colspan '3' instead of two if the other customer defined fields are shown.
			    	// ie. stretch into three columns if there are three, or two if there are two
			    	code += 'Invalid item #. Please review and try again or contact Customer Service.</td>';
			    	//End Jira 3098
			    	//code += '<a href="javascript:showSpecialItem(\'' + encodeForHTML(specialDivId) + '\'); ">add it as an Special item</a> </span>';
			    	
			    	
			    	code += '<div id="' + encodeForHTML(specialDivId) + '" style="display: none;">';
			    	code += '<div class="xpedx-light-box" id="inline1" style="height: 305px; width: 550px;>';
			    	code += '  <span class="margin-top-none">';
			    	code += '	<h2>Add Non-Catalog Item</h2>';
			    	code += '  	<br>';
			    	code += '	<strong>HINT:</strong>';
			    	code += ' Use this form to add items you can\'t find in the online catalog, or would like to special order.';
			    	code += ' Items added here may be deleted from your shopping cart at any time</span>';
			    	code += '  <br>';
			    	code += '		<div>';
			    	code += '         	<p><span id="catalog-descr">Description:</span>';
			    	code += '			<textarea class="margin-left-10" id="siUIDesc_' + encodeForHTML(divFieldId) + '" rows="3" cols="50" onload="javascript:captureSpecialItemValues(this)" onchange="javascript:captureSpecialItemValues(this)"></textarea>';
			    	code += '         	</p>';
			    	code += '		</div>';
			    	
			      	//code += '              		<input type="hidden" title="UOM" onload="javascript:captureSpecialItemValues(this)" onchange="javascript:captureSpecialItemValues(this)" class="input-label" id="siUIUom_'+ encodeForHTML(divFieldId) +'" value="'+ encodeForHTML(QuickAddElems[i].uom) + '" />';
			      	//code += '              		<input type="hidden" title="QTY" onload="javascript:captureSpecialItemValues(this)" onchange="javascript:captureSpecialItemValues(this)" class="input-label" id="siUIJobId_'+ encodeForHTML(divFieldId) +'" value="'+ encodeForHTML(QuickAddElems[i].quantity) + '" />';
			    	
			    	code += '		<div>';
			    	code += '			<div class="add-non-catalog-labels"><span class="add-non-catalog-qty-label">Qty:</span>';
			    	code += '           		<input type="text" disabled="true" value="1" class="input-label add-non-catalog-qty-input" tabindex="12" onload="javascript:captureSpecialItemValues(this)" onchange="javascript:captureSpecialItemValues(this)" title="QTY" id="siUIQty_'+ encodeForHTML(divFieldId) +'" value="'+ encodeForHTML(QuickAddElems[i].quantity) + '" />'; 
			    	code += '              	<span class="bold">UOM:</span>';
			    	code += ' 					<input type="text" disabled="true" value="EACH" maxlength="16" class="input-label add-non-catalog-UOM-input" tabindex="13" style="text-align: left;" onload="javascript:captureSpecialItemValues(this)" onchange="javascript:captureSpecialItemValues(this)" title="UOM" id="siUIUom_'+ encodeForHTML(divFieldId) +'" value="'+ encodeForHTML(QuickAddElems[i].uom) + '" />';
			      	//code += '              		<input type="hidden" title="Job#" onload="javascript:captureSpecialItemValues(this)" onchange="javascript:captureSpecialItemValues(this)" class="input-label" id="siUIJobId_'+ encodeForHTML(divFieldId) +'" value="'+ encodeForHTML(QuickAddElems[i].jobId) + '" />';
			      	code += ' 			   </div>';
			    	code += ' 		</div>';
			      	code += '     	<ul id="tool-bar" class="tool-bar-bottom-right">';
			    	code += '     		<li>';
			    	code += '         		<a href="javascript:closeSpecialUIbox();" class="grey-ui-btn button-margin">';
			    	code += '				<span>Cancel</span>';
			    	code += '         	    </a>';
			    	code += '			</li>';
			    	code += '      		<li>';
			    	code += '    	     	<a href="javascript:addSpecialItem('+ i +');" class="green-ui-btn">';
			    	code += '             	<span>Add to Cart</span>';
			    	code += '				</a>';
			    	code += '			</li>';
			    	code += '	</ul>';			
			    	code += '</div>';
			    	code += '</div> ';			    	
			    	code += '</td>';			    	
			    	code += '</tr>';
		    	}
    	}
    }
    code += '</tbody>';
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
	var char1 = '\n';
	var itemLines = itemsString.split(char1);
	
	for(var i=0;i < itemLines.length; i++)
	{
    	addToCartDiv = document.getElementById("addProdsToOrder");
    	addToCartDiv.style.display = 'inline';
    	
		var itemQty = null;
		var itemSku = null;
		var jobId = "";
		var uomArray = new Array();
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
		
		if(itemSku == null || itemQty == null)
		{
			alert("Discarding this Line: not a valid format, please add again " + itemLines[i]);
		}
		else
		{
			QuickAddElems[QuickAddElems.length] =
		    {
		            sku: itemSku,
		            quantity: itemQty,
		            itemType: "1",
		            itemTypeText: "xpedx Item #",
		            itemDesc: "",
		            uom: "",
		            uomList: uomArray,
		            jobId: jobId,
		            isValidated: "false",
		            isEntitled: "true"
		    }
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
    
	redrawQuickAddList();
    validateItems();
    closeCopyPasteDialog();
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

function validateItems()
{
	var url = document.getElementById('productValidateURL');
	var itemsString = "";
	var itemTypesString = "";
	var itemsValidationIndex = new Array();
	var len = QuickAddElems.length;
	for(var index=0; index < len; index++)
    {
		var itemId = QuickAddElems[index].sku;
		var itemType = QuickAddElems[index].itemType;
		var isItemValidated = QuickAddElems[index].isValidated;
		
		if(isItemValidated == "false")
		{
			itemsString = itemsString + itemId + "*";
			itemTypesString = itemTypesString + itemType + "*";
			itemsValidationIndex.push(index);
			/*
			 * if(index != (len-1)) { itemsString = itemsString + itemId + "*";
			 * itemTypesString = itemTypesString + itemType + "*"; } else {
			 * itemsString = itemsString + itemId ; itemTypesString =
			 * itemTypesString + itemType; }
			 */
		}
    }
	
	if(itemsString != "")
	{
		itemsString = itemsString.substr(0,(itemsString.length-1));
		itemTypesString = itemTypesString.substr(0,(itemTypesString.length-1));
		
		Ext.Ajax.request({   
	        url: url,   
	        params: {itemList:itemsString, itemTypeList:itemTypesString}, // in
																			// XML
																			// format
	        success:  function (response, request){
	        	var res = response.responseText;
	        	var itemValid = res.split(",");
	        	var noOfItems = itemsValidationIndex.length;
	        	for(var index=0; index < noOfItems; index++)
	            {
	        		var itemValidandUomList =  trim(itemValid[index]).split("*");
	        		var itemEntitled = trim(itemValidandUomList[0]);
	        		var itemUOMArray = new Array();
	        		var itemUOMConvArray = new Array();
	        		var itemUomAndConvString = null;
	        		var itemUOMCodeArray = new Array();
	        		if(itemEntitled == "true")
	        		{
		        		var itemUomList = trim(itemValidandUomList[1]).split("!");
		        		for(var uomIndex = 0; uomIndex < itemUomList.length ; uomIndex++)
		        		{
		        			var itemUOMConvFactor = trim(itemUomList[uomIndex]).split(":");
		        			var itemUOM = trim(itemUOMConvFactor[0]);
		        			itemUOMCodeArray[uomIndex] = itemUOM;
		        			var itemUomConvfAndMultiple = itemUOMConvFactor[1].split("|");
		        			var itemUomConvf = trim(itemUomConvfAndMultiple[0]);
		        			var itemOrderMultiple = trim(itemUomConvfAndMultiple[1]);
		        			itemUomAndConvString = itemUomAndConvString+"|"+itemUOM+"-"+itemUomConvf;
		        			if(itemUomConvf == "1"){
		        				itemUOMArray = itemUOMArray.concat(itemUOM);
		        			}
		        			else{
		        				itemUOMArray = itemUOMArray.concat(itemUOM + " (" + itemUomConvf + ")");
		        			}
		        			itemUOMConvArray = itemUOMConvArray.concat(itemUomConvf);
		        		}
	        		}
	        		var QAindex = itemsValidationIndex[index];
	        		var entitled = itemEntitled;
	        	// alert("valide items : valid? " + entitled );
	        		QuickAddElems[QAindex].isEntitled = entitled;
	        		QuickAddElems[QAindex].isValidated = "true";
	        		QuickAddElems[QAindex].uomList = itemUOMArray;
	        		QuickAddElems[QAindex].uomCodes = itemUOMCodeArray;
	        		QuickAddElems[QAindex].orderMultiple = itemOrderMultiple;
	        		QuickAddElems[QAindex].itemUomAndConvString = itemUomAndConvString;
	        		if(itemUOMArray.length > 0)
	        		{
	        			var firstIndex = itemUOMArray[0].indexOf('(');
	        			if(firstIndex != -1) 
	        			{
	        				QuickAddElems[QAindex].uom = itemUOMArray[0].substring(0,firstIndex);
	        			}
	        			else
	        			{
	        				QuickAddElems[QAindex].uom = itemUOMArray[0];
	        			}
	        		}
	            }
	        	redrawQuickAddList();
	        },   
	        failure: function (response, request){
	        	 var noOfItems = QuickAddElems.length;
	        	 for(var index=0; index < noOfItems; index++)
	        	 {
	        		 var isItemValidated = QuickAddElems[index].isValidated;
	        		 if(isItemValidated == "false")
	        		 {
	        			 alert('Failed to validate item ' + QuickAddElems[index].sku + '. Please try again later.');
	        			 removeProductFromQuickAddList(index);
	        		 }
	        	 }
	        }
	    });
	}
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