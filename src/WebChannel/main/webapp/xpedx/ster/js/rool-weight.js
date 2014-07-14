// JavaScript Document

function validateForm() {
	var form=document.eform;
	var myindex=form.factor.selectedIndex;

	if (document.eform.rDiameter.value == "")
		{
		alert("Enter Roll Diameter (inches).");
		document.eform.rDiameter.focus();
		return (false);
		}

	if (isNaN(document.eform.rDiameter.value))
		{
		alert("Roll Diameter must be a number.");
		document.eform.rDiameter.focus();
		return (false);
		}

	if (document.eform.cDiameter.value == "")
		{
		alert("Enter Core Diameter (inches).");
		document.eform.cDiameter.focus();
		return (false);
		}

	if (isNaN(document.eform.cDiameter.value))
		{
		alert("Core Diameter must be a number.");

		document.eform.cDiameter.focus();
		return (false);
		}

	if (document.eform.rWidth.value == "")
		{
		alert("Enter Roll Width (inches).");
		document.eform.rWidth.focus();
		return (false);
		}

	if (isNaN(document.eform.rWidth.value))
		{
		alert("Roll Width must be a number.");
		document.eform.rWidth.focus();
		return (false);
		}

	if (form.factor.options[myindex].value == "")
		{
		alert("Select Factor.");
		document.eform.factor.focus();
		return (false);
		}
	CalculateRollEstimation(form.rDiameter.value,form.cDiameter.value,form.rWidth.value,form.factor.options[myindex].value);
	return true;
}

function CalculateRollEstimation(rDiameter,cDiameter,rWidth,factor) {
	var RollEstimation = (rDiameter * rDiameter) - (cDiameter * cDiameter);
	RollEstimation = RollEstimation * rWidth * factor;
	document.eform.Answer.value = formatnumber(RollEstimation,0);
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