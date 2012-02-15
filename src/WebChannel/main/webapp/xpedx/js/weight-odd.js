// JavaScript Document

function validatePrompt (Ctrl, PromptStr) {
			alert (PromptStr)
			/*if (ver == "n3") {
				Ctrl.focus();*/
			//Modified For Jira 3109
			if (document.eform.Msheets.value == ""){
				document.getElementById("Msheets").style.borderColor="#FF0000";
				Ctrl.focus();
			}
			if (document.eform.NumSheets.value == ""){
				document.getElementById("NumSheets").style.borderColor="#FF0000";
				Ctrl.focus();
			}return;
		}
//Added for Jira 3109
function resetPromptForWOdd () {
	document.getElementById("Msheets").style.borderColor="";	
	document.getElementById("NumSheets").style.borderColor="";	
	
}
		function testBlank(objField, FieldName) {
			var strField = new String(objField.value);
			//Added resetPromptForWOdd()for Jira 3109
			resetPromptForWOdd ();
			if (objField.value == "") {
				validatePrompt (objField, "Required fields missing. Please review and try again.")
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
			if (!testValue(form.Msheets,"Weight Per M Sheet")) return false;
			if (!testValue(form.NumSheets,"Number of Sheets")) return false;
			CalculateOddNumber(form.Msheets.value,form.NumSheets.value);
			return true;
		}

		function CalculateOddNumber(MSheets,NumSheets) {
			var OddNumber =(MSheets * NumSheets) / 1000;
			document.eform.Answer.value = formatnumber(OddNumber,2);
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