	  function updateTaxExemptFlag(){
			if(document.saveOrderBillToAddress.taxExemption.checked){
				document.saveOrderBillToAddress.taxExemptionFlag.value = "Y";
			}else{
				document.saveOrderBillToAddress.taxExemptionFlag.value = "N";
			}
	  }

    function validateAndSaveInfo(formObj, actionURL, validateURL, verifyaddrDiv)
    {
        var realActionURL = "";
        if( (formObj.isAddressDirty == null) || (formObj.isAddressDirty.value == 'false') )
        {
            document.getElementById("actionName").value = "saveOrderBillToWithoutAddress";
            realActionURL = document.getElementById('saveOrderBillToWithoutAddressURL');
            formObj.action = realActionURL;
        }
        else
        {           
            document.getElementById("actionName").value = "saveOrderBillToAddress";
            realActionURL = document.getElementById('saveOrderBillToAddressURL');
            formObj.action = realActionURL;
        }
		return validateAndSaveAddressInfo(formObj, realActionURL, validateURL, verifyaddrDiv, true);		        
    }