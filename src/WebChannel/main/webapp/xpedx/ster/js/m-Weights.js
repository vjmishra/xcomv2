function validatePrompt (Ctrl, PromptStr) {
	alert (PromptStr)
	/*if (ver == "n3") {
		Ctrl.focus();*/
	if (eform.hSize.value == ""){
		Ctrl.focus();
	}
	if (eform.wsize == ""){
		Ctrl.focus();
	}
	if (eform.bWeight.value == ""){
		Ctrl.focus();
	}
	if (eform.hArea.value == ""){
		Ctrl.focus();
	}
	if (eform.wArea.value == ""){
		Ctrl.focus();
	}
	return;
}

function testBlank(objField, FieldName) {
	var strField = new String(objField.value);
	if (objField.value == "") {
		validatePrompt (objField, "\""+FieldName+"\" cannot be blank.")
		return (false);
	} else {
		return (true);
	}
}

function testNumber(objField, FieldName) {
	var numField = objField.value;
	
	if (isNaN(numField)) {
		validatePrompt (objField, "\""+FieldName+"\" must be a valid number.")
		return (false);
	}
	
	numField = parseFloat(numField);

	if (numField> 0) {
		return (true);
	} else {
		validatePrompt (objField, "\""+FieldName+"\" must be a number greater than 0.")
		return (false);
	}
}

function testValue(objField, FieldName) {
	if (!testBlank(objField,FieldName)) return false;
	if (!testNumber(objField,FieldName)) return false;
	return true;
}

function validateForm() {
	var form=document.eform;
	if (!testValue(form.hSize,"Given Size (height)")) return false;
	if (!testValue(form.wSize,"Given Size (width)")) return false;
	if (!testValue(form.bWeight,"Basis Weight")) return false;
	if (!testValue(form.hArea,"Basic Size (height)")) return false;
	if (!testValue(form.wArea,"Basic Size (width)")) return false;
	CalculateReamWeight(form.hSize.value,form.wSize.value,form.bWeight.value,form.hArea.value,form.wArea.value)
	return true;
}

function CalculateReamWeight(hSize,wSize,bWeight,hArea,wArea) {
	var sheetsize = (hSize * wSize);
	var reamsize = ((hSize * wSize) * bWeight) / (hArea*wArea);
	if (sheetsize> 863) {
		reamsize = formatnumber(reamsize,0);
	}
	if (sheetsize < 864) {
		reamsize = formatnumber(reamsize,1);
	}
	if (sheetsize < 336) {
		reamsize = formatnumber(reamsize,2);
	}
	reamsize = (reamsize * 2);
	document.eform.Answer.value = reamsize;
}

function formatnumber(val,places) {
    var power = 1;
    if (places> 0) {
		power = Math.pow(10,places);
    }
    // now we round the val to X decimal places.
    var roundedNum = (Math.round(val * power)/power) + "";

	// check the result for decimals
    var decpos = roundedNum.indexOf('.');

    if (decpos>= 0) {
	    var decDiff = places-(roundedNum.length-(decpos+1));
	} else {
		var decDiff = places;
		if (places> 0) {
			roundedNum += ".";
		}
	}

	// add missing zeros to satisify the decimal request.
	for (var x = 0; x < decDiff; x++) {
		roundedNum += "0";
	}

    return roundedNum;
}