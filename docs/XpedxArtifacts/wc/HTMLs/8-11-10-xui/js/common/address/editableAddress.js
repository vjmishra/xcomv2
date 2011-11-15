function showStatesForCountry(currentForm, ajaxURL, disabled)
{
    var country = currentForm.Country.value;
    var state = currentForm.State.value;

    Ext.Ajax.request({
        url : ajaxURL,
        params : {
            currentState : state,
            currentCountry: country,
            disabled: disabled
        },
        method: 'GET',
        // if we get a successful response, parse the data then render the panel
        success: function ( response, request ) {
            var theDiv = document.getElementById("editableAddressStateDiv");
            theDiv.innerHTML = response.responseText;
        },
        failure: function ( response, request ) {
            alert('Failed: ' + response.responseText);
        }
    });

}
