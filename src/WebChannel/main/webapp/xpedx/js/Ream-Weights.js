 
function validatePrompt (Ctrl, PromptStr) {
	alert (PromptStr)
	/*if (ver == "n3") {
		Ctrl.focus();
	}*/
	if (document.eform.hSize.value == "") {
		document.getElementById("hSize").style.borderColor="#FF0000";
		Ctrl.focus();
	}
	if (document.eform.wSize.value == "") {
		document.getElementById("wSize").style.borderColor="#FF0000";
		Ctrl.focus();
	}
	if (document.eform.bWeight.value == "") {
		document.getElementById("bWeight").style.borderColor="#FF0000";
		Ctrl.focus();
	}
	if (document.eform.hArea.value == "") {
		document.getElementById("hArea").style.borderColor="#FF0000";
		Ctrl.focus();
	}
	if (document.eform.wArea.value == "") {
		document.getElementById("wArea").style.borderColor="#FF0000";
		Ctrl.focus();
	}
	/*if (document.eform.hSize.value == "") {
		document.getElementById("hSize").style.borderColor="#FF0000";
		Ctrl.focus();
	}*/
	return;
}
//Added for Jira 3109
function resetPromptForReamW () {
document.getElementById("hSize").style.borderColor=""; 
document.getElementById("wSize").style.borderColor=""; 
document.getElementById("bWeight").style.borderColor="";
document.getElementById("hArea").style.borderColor="";
document.getElementById("wArea").style.borderColor="";

}



function testBlank(objField, FieldName) {
	var strField = new String(objField.value);
	//Added resetPromptForReamW()for Jira 3109
	resetPromptForReamW ();
	if (objField.value == "") {
		/*Start- Jira 3109 */
		validatePrompt (objField, "Required fields missing. Please review and try again.")
		/*End- Jira 3109*/
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
	if (!testValue(form.hSize,"Given Size (Length)") ) return false; 
	if (!testValue(form.wSize,"Given Size (Width)")) return false;
	if (!testValue(form.bWeight,"Basis Weight")) return false;
	if (!testValue(form.hArea,"Basic Size (Length)")) return false;
	if (!testValue(form.wArea,"Basic Size (Width)")) return false;
	CalculateReamWeight(form.hSize.value,form.wSize.value,form.bWeight.value,form.hArea.value,form.wArea.value);
	return true;
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

function CalculateReamWeight(hSize,wSize,bWeight,hArea,wArea) {
	var sheetsize = (hSize * wSize)
	var reamsize = ((hSize * wSize) * bWeight) / (hArea*wArea)
	if (sheetsize> 863) {
		reamsize = formatnumber(reamsize,0)
	}
	if (sheetsize < 864) {
		reamsize = formatnumber(reamsize,1)
	}
	if (sheetsize < 336) {
		reamsize = formatnumber(reamsize,2)
	}

	document.eform.Answer.value=reamsize;
}

 