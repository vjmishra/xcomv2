	 Ext.onReady(function() {
				var z = Ext.DomQuery.selectNode("#ShowMoreAddresses");
				if(z != null){
					Ext.EventManager.addListener(z,'click',svgRoundedBackground,this,{delay: 500});
				}
	  });

	  function svgRoundedBackground() {
          javascript:svg_classhandlers_decoratePage()
	  }

	  function toggleAddressBlock(formName) {
			toggleVisibilityOfDiv('editableAddressDiv');
			toggleVisibilityOfDiv('readOnlyAddressDiv');
			if(eval("document." + formName + ".isAddressDirty") != null)
			{
				eval("document." + formName + ".isAddressDirty.value = 'true'");
			}
			svgRoundedBackground();
	  }

	function toggleVisibilityOfDiv(divID)
    {
        var divElement = document.getElementById(divID);
        if(divElement.className != "hidden-data")
        {
            divElement.className = "hidden-data";
        }
        else
        {
            divElement.className = "visible-data";
        }
    }
	
	function saveAddressInfo(formObj, actionURL, validateURL, verifyaddrDiv)
	{
	    validateAndSaveAddressInfo(formObj, actionURL, validateURL, verifyaddrDiv, false);		
	}

	function validateAndSaveAddressInfo(formObj, actionURL, validateURL, verifyaddrDiv, isBillToPage)
	{
	    if( (formObj.isAddressDirty == null) || (formObj.isAddressDirty.value == 'false') )
	    {
	        if(isBillToPage && !swc_validateForm(formObj.name)){
				return false;
			}
			formObj.submit();
			return true;
						
	    }
	    else
	    {           
	        isValid = swc_validateForm(formObj.name);
			isValid &= validateAddress(formObj, actionURL, validateURL, verifyaddrDiv, 'svgRoundedBackground()', isValid);			
	    }
	}