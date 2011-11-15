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
