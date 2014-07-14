function showMoreAddresses(tableID, numbInitialRows)
{
    var table = document.getElementById(tableID);
    var tableLength = table.rows.length;
    table.rows[numbInitialRows].style.display = "none";
    for(i=numbInitialRows + 1; i < tableLength; i++)
    {
        table.rows[i].style.display = "";
    }
}

function useAddress(index, addressArrayName, formName, divName, callBackFunctionForUseAddress, skipChangeEvents)
{
    if(formName != '')
    {
        copyArrayDataToForm(index, addressArrayName, formName);
        
        if(eval("document." + formName + ".isAddressDirty") != null)
        {
            eval("document." + formName + ".isAddressDirty.value = 'true'");
        }

        if(!skipChangeEvents)
        {
            eval("document." + formName + ".Country.onchange()");
        }
    }

    if(divName != '')
    {
        var targetElem = document.getElementById(divName);
        var sourceElem = document.getElementById('addressText' + index);
        targetElem.innerHTML = sourceElem.innerHTML; 
    }
    
    if(callBackFunctionForUseAddress != '')
    {
        eval(callBackFunctionForUseAddress + "(" + index + ")");
    }
}

function copyArrayDataToForm(index, addressArrayName, formName)
{
    setInputFieldFromAddressArray(formName, "AddressID", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "AddressLine1", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "AddressLine2", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "AddressLine3", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "AddressLine4", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "AddressLine5", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "AddressLine6", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "AlternateEmailID", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "Beeper", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "City", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "Company", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "Country", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "DayFaxNo", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "DayPhone", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "Department", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "EMailID", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "ErrorTxt", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "EveningFaxNo", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "EveningPhone", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "FirstName", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "HttpUrl", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "IsAddressVerified", index, addressArrayName);
    selectRadioButtonByValue(formName, "IsCommercialAddress", "N", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "JobTitle", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "LastName", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "Latitude", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "Longitude", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "MiddleName", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "MobilePhone", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "OtherPhone", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "PersonID", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "PreferredShipAddress", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "State", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "Suffix", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "TaxGeoCode", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "Title", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "UseCount", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "VerificationStatus", index, addressArrayName);
    setInputFieldFromAddressArray(formName, "ZipCode", index, addressArrayName);
}

function setInputFieldFromAddressArray(formName, fieldName, index, addressArrayName)
{
    eval("document." + formName + "." + fieldName + ".value = " + addressArrayName + "[" + index + "]." + fieldName);
}

function selectRadioButtonByValue(formName, fieldName, defaultValue, index, addressArrayName)
{
    var searchValue = defaultValue;
    var targetValue = eval(addressArrayName + "[" + index + "]." + fieldName);
            
    if(targetValue != "")
    {
        searchValue = targetValue;
    }
    
    var radioButton = eval("document." + formName + "." + fieldName);
    for (var i=0; i < radioButton.length; i++)
    {
        if (radioButton[i].value == searchValue)
        {
            radioButton[i].checked = true;
            return;
        }
    }
}

