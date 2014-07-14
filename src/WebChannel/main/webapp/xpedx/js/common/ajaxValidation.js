function addErrorForRepeatingElements(form, errorData){
    var errorStrings = errorData.errorStrings;
    var resetValues = errorData.resetValueStrings;
    if(errorStrings.length>1){
        for(var i=0; i<errorStrings.length; i++){
            if(errorStrings[i] != null && errorStrings[i] != ""){
                addError(form.elements[errorData.fieldName][i], errorStrings[i]);
            }
            if(resetValues!=null)
                if(resetValues[i]!=null){
                    form.elements[errorData.fieldName][i].value = resetValues[i];
                }
        }
    }
    else if(errorStrings.length=1){
        if(errorStrings[0] != null && errorStrings[0] != ""){
            addError(form.elements[errorData.fieldName], errorStrings[0]);
        }
        if(resetValues!=null)
            if(resetValues[0]!=null){
                form.elements[errorData.fieldName].value = resetValues[0];
            }
    }
}


function swc_validateForm(formName, resetErrors) {
        var errors = false;
        form = document.getElementById(formName);
        if( (resetErrors === undefined) || (resetErrors == true) )
        {
            clearErrorMessages(form);
            clearErrorLabels(form);
        }
        dojo.io.bind({
            url :ajaxValidationURL, // URL for ajaxValidateJson is defined in head.tag using s:url tag to remove the error of hard-coded context-root
            load : function(type, data, evt) {
                if (data.count > 0) {
                    var i;
                    var errorCount = 0;
                    for(i = 0; i < data.count; i++) {
                        if(data.list[i].errorMesage != null){
                            if((form.elements[data.list[i].fieldName])!=null){
                                if(isFieldValidforAddError(form.elements[data.list[i].fieldName])){
                                    errorCount = errorCount + 1;
                                    addError(form.elements[data.list[i].fieldName], data.list[i].errorMesage);
                                }
                            }
                            else if((document.getElementById(data.list[i].fieldName))!= null){
                                if(isFieldValidforAddError(document.getElementById(data.list[i].fieldName))){
                                    errorCount = errorCount + 1;
                                    addError(document.getElementById(data.list[i].fieldName), data.list[i].errorMesage);
                                }
                            }
                        }
                        if(data.list[i].resetValue!=null){
                            form.elements[data.list[i].fieldName].value = data.list[i].resetValue;
                        }
                        else if(data.list[i].errorStrings != null){
                            errorCount = errorCount + 1;
                            addErrorForRepeatingElements(form, data.list[i]);
                        }
                    }
                    if(errorCount>0)
                        errors = true;
                    try{
                    svg_classhandlers_decoratePage();
                    }catch (err){
                        alert("There is an error in Svg-apply: "+err.message);
                    }
                }
            },
            mimetype : "text/json",
            sync : true,
            //form: "myForm",
            //content: { test: ["test1", "test2"] }
            formNode: form,
            method :"POST"
        });
        return !errors;
    }

function isFieldValidforAddError(field){
    var ctrlDiv = field.parentNode; // wwctrl_ div or span
    var enclosingDiv = ctrlDiv.parentNode; // wwgrp_ div
    if (!ctrlDiv || (ctrlDiv.nodeName != "DIV" && ctrlDiv.nodeName != "SPAN") || !enclosingDiv || enclosingDiv.nodeName != "DIV") {
        return false;
    }
    else{
        return true;
    }
}

String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}

var mandatoryFieldList = [	{"id": "userName", "display": "Username", "type": "text"},
                       		{"id": "listName", "display": "Name", "type": "text"},
                       		{"id": "firstName", "display": "First Name", "type": "text"},
                       		{"id": "lastName", "display": "Last Name", "type": "text"},
                       		{"id": "emailId", "display": "Email Address", "type": "text"},
                       		{"id": "confirmEmailId", "display": "Confirm Email Address", "type": "text"},
                       		{"id": "userpassword", "display": "Password", "type": "password"},
                       		{"id": "confirmpassword", "display": "Confirm Password", "type": "password"},
                       		{"id": "secretAnswer", "display": "Secret Answer", "type": "password"},
                       		{"id": "confirmAnswer", "display": "Confirm Answer", "type": "password"},
                       		{"id": "prodId", "display": "Item #", "type": "text"}
                       	 ];
var mandatoryFieldErrorList = [
                     		{"id": "List Name", "display": "Name is required."},
                     		{"id": "Name", "display": "Name is required."},
                     		{"id": "Item #", "display": "Item number is required."}
                     	 ];

var mandatoryFieldListHL = [
                     		{"id": "listNameHL", "display": "List Name", "type": "text"}
                     	 ];

function searchInArray(arr, obj){
	for(var i=0; i<arr.length; i++){
		if (arr[i].id == obj) return arr[i].display;
	}
	return false;
}

function isChildInIgnoreList(ch, ignoreList){
	for(var i=0; i<ignoreList.length; i++){
		if (ignoreList[i] == ch) return true;
	}
	return false;
}

function mandatoryFieldValidation(docDivId, ignoreDivIds){
	var returnErrorMsg = "";
	document.getElementById("mandatoryFieldCheckFlag_"+docDivId).value = "true";
	var invalidMandatoryFieldCount = 0;
	var childElements = document.getElementById(docDivId).childNodes;
	for (var j = 0; j < childElements.length; j++){
		if(childElements[j].id != undefined){
			if(!isChildInIgnoreList(childElements[j].id, ignoreDivIds)){
				var elemsToValidate = childElements[j].getElementsByTagName('input');
				for(var i=0; i< elemsToValidate.length; i++)
				{	
					var fieldDisplay = searchInArray(mandatoryFieldList, elemsToValidate[i].id); 
					if(fieldDisplay != false){
						var fieldValue = elemsToValidate[i].value.trim();
						if(fieldValue == undefined || fieldValue == null || fieldValue == ""){
							if(invalidMandatoryFieldCount!=0){
								returnErrorMsg = returnErrorMsg + ",";
							}	
							elemsToValidate[i].value = "";
							elemsToValidate[i].style.borderColor="#FF0000";
							returnErrorMsg = returnErrorMsg + fieldDisplay;
							invalidMandatoryFieldCount++;
						}
						else
							elemsToValidate[i].style.borderColor="";
					}
				}
			}
		}
	}
	if(returnErrorMsg!=""){
		var fieldErrorDisplay = searchInArray(mandatoryFieldErrorList, returnErrorMsg); 
		if(fieldErrorDisplay != false){
			document.getElementById("errorMsgForMandatoryFields_"+docDivId).innerHTML = fieldErrorDisplay;
		}
		else{
			returnErrorMsg = returnErrorMsg;
			document.getElementById("errorMsgForMandatoryFields_"+docDivId).innerHTML = "Required fields missing. Please review and try again.";
		}
		
		document.getElementById("errorMsgForMandatoryFields_"+docDivId).style.display = "inline";
	}
	else{
		document.getElementById("errorMsgForMandatoryFields_"+docDivId).style.display = "none";
		document.getElementById("mandatoryFieldCheckFlag_"+docDivId).value = "false";
	}
	return returnErrorMsg;
}

function mandatoryFieldValidationHL(){
	var returnErrorMsgHL = "";
	mandatoryFieldCheckFlagHL = true;
	var elemsToValidateHL = document.getElementsByTagName('input');
	var invalidMandatoryFieldCountHL = 0;
	for(var i=0; i< elemsToValidateHL.length; i++)
	{	
		var fieldDisplay = searchInArray(mandatoryFieldListHL, elemsToValidateHL[i].id); 
		if(fieldDisplay != false){
			var fieldValue = elemsToValidateHL[i].value.trim();
			if(fieldValue == undefined || fieldValue == null || fieldValue == ""){
				if(invalidMandatoryFieldCountHL!=0){
					returnErrorMsg = returnErrorMsg + ",";
				}
				elemsToValidateHL[i].value = "";
				elemsToValidateHL[i].style.borderColor="#FF0000";
				returnErrorMsgHL = returnErrorMsgHL + fieldDisplay;
				invalidMandatoryFieldCountHL++;
			}
			else
				elemsToValidateHL[i].style.borderColor="";

		}
	}
	if(returnErrorMsgHL!=""){
		
		var fieldErrorDisplay = searchInArray(mandatoryFieldErrorList, returnErrorMsgHL); 
		if(fieldErrorDisplay != false)
			document.getElementById("errorMsgForMandatoryFieldsHL").innerHTML = fieldErrorDisplay;
		else {
			returnErrorMsgHL = returnErrorMsgHL + ".";
			document.getElementById("errorMsgForMandatoryFieldsHL").innerHTML = "Mandatory Fields missing : "+returnErrorMsgHL;
		}
		document.getElementById("errorMsgForMandatoryFieldsHL").style.display = "inline";
	}
	return returnErrorMsgHL;
}