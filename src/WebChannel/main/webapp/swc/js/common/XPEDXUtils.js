//The below function is used to change the sequencing in a list

function setNewPriority(idx, currIdx, currentSelElem) {

	var currValue = null;
	idx = parseInt(idx);
	currIdx = parseInt(currIdx);
	var currentSelElemName = currentSelElem.name;
	if (!currentSelElem) {
		currentSelElem = null;
	}

	var selectElems = document.getElementsByName(currentSelElemName);
	if (selectElems == null)
		return;
	for ( var i = 0; i < selectElems.length; i++) {
		var currElem = selectElems[i];
		currValue = parseInt(currElem.value);
		if (currentSelElem == currElem) {
			currentSelElem.value = idx;
			continue;
		}

		if (idx < currIdx) {
			if (currValue >= idx && currValue < currIdx) {
				currElem.selectedIndex = currElem.selectedIndex + 1;
			}
		} else {
			if (currValue > currIdx && currValue <= idx) {
				currElem.selectedIndex = currElem.selectedIndex - 1;
			}
		}
	}

}

function onChangeItemOrder(currentElem, currentSeqNo, newSeqNo) {
	setNewPriority(newSeqNo, currentSeqNo, currentElem);
	//Remove the focus from the select element
	currentElem.blur();
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
		 alert("Please select a replacement item.");
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
	//Passing the qty as blank
	//form.dom.qty.value 		= qty;
	form.dom.uomId.value 	= uom;
	form.dom.order.value 	= parseInt(order, 10) + 1;
	
	//Submit the form
	form.dom.submit();
	
	Ext.Msg.wait('Saving your selection... Please wait.');
}

function replacementReplaceInList(uId) {
	
	if (uId == ""){
		alert("Please select a replacement item.");
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
	//Passing the qty as blank
	//form.dom.qty.value 		= qty;
	form.dom.uom.value 	= uom;
	form.dom.submit();
	
	Ext.Msg.wait('Saving your selection... Please wait.');
}

function addComma(strOriginal){	 
	 
	 var data = "";	 
	 var i,j;
	 j=0;
	 var len = strOriginal.length;

	 for (i=len; i>=0 ; i-- )
	 { 
	   data = data + (strOriginal.charAt(i)) ;
	   var m = j+1;
	   var l = m%4;
	   
		if( l == 0 && i>0) {					
			data = data + ',' ;
			j=j+1;
		}
		j=j+1;
	 } 
	return reverseString(data);
	  
 }

function reverseString(my_str){

	var reverseStr = "";

	var i=my_str.length;
	i=i-1;

	for (var x = i; x >=0; x--)
	{
	reverseStr= reverseStr + my_str.charAt(x);

	}
	return reverseStr;

 }