var focusDelay = 100;
function showCreateCartDialog()
{
       DialogPanel.show('createNewCartPanel');
       svg_classhandlers_decoratePage();
}


function changeParameterAndValidateForm(formObj,actionName)
{
    document.getElementById("validationActionName").value = actionName;
    if(swc_validateForm("dol") != false)
    {
       if(actionName=="draftOrderDelete"){
		  formObj.action = "draftOrderDelete.action";
	   }
	   formObj.submit();
	}
}
function confirmDeleteDraftOrder(formObj,actionName){
    document.getElementById("validationActionName").value = actionName;
     if(swc_validateForm("dol") != false){
        DialogPanel.show('deleteCartDialog');
     }
 }