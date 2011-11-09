// JavaScript Document

function validatePrompt (Ctrl, PromptStr) {
	alert (PromptStr)
	if (eform.caliper.value == "") {
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
		if (!testValue(form.caliper,"Caliper")) return false;
		CalculatePPI(form.caliper.value);
		return true;

	}

	function CalculatePPI(caliper) {
			document.eform.Answer.value = formatnumber((2 / caliper),0);
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
	
	

function formatPhone(AddingText,objTxtBox)
{
    y=objTxtBox.value;
    abc=/-/gi;
    rep=y.replace(abc,0)
    if (isNaN(rep))
    {
        ab=/(a*)(b*)(c*)(d*)(e*)(f*)(g*)(h*)(i*)(j*)(k*)(l*)(m*)(n*)(o*)(p*)(q*)(r*)(s*)(t*)(u*)(v*)(w*)(x*)(y*)(z*)/gi;
        re=y.replace(ab,"")
        objTxtBox.value="";
        strMsg=AddingText+" Should be numeric";
        intCheckError=1;
        alert(strMsg);
        objTxtBox.focus();
    }
}

function clearr()
{
document.getElementById("txtstran").value="";
}