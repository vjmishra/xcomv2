/* This file is sepcific to any methods related to common payment-light box.
 * 
 */

/**
 * @author vbandhu
 * 
 */


//Array-variable to store key-value pair as Payment-type-->PaymentTypeGroup
var typeGroupArray = new Array();
//String to store the id of the field name on which focus should be there (to put focus on 1st editable field in the payment light box)
var focusElement = "";
//String to save the action for the form-submit for saving payment method
var submitActionURL = "";

/**
 * Sets the style:display attribute of the fields to "none" or ""; based on the payment-type selected from the drop-down
 * 
 * @param ptype --> PaymentType selected
 * @param visibility --> "none" or ""
 * 
 * 
 */

function setPaymentTableStyle(ptype, visibility){
    var tableRowElems = document.getElementsByTagName("tr");//set style on the elements by tagName "tr" as the all the fields are table rows
    for(i=0; i<tableRowElems.length; i++){
        if(tableRowElems[i].id==ptype){
            tableRowElems[i].style.display=visibility;
        }
    }
}

/**
 * Method call for onChange of PaymentType field (drop-down). This hides or displays the fields according to the payment-type selected.
 * Also, sets the focus on the 1st editable field. 
 * And also sets the value of the hidden parameter "paymentType" to the selection value (Payment type field in the UI is out of the main form).
 * 
 */

function showPaymentType(){
    populatePaymentTypeGroups();//method to prepare the array typeGroupArray
    if(document.getElementById("paymentTypeList") != null){
        var pmtType = document.getElementById("paymentTypeList").value;
        var custPaymentKey = document.managePaymentMtd.customerPmtKey.value;
        hidePaymentTableStyleForDivs("CREDIT_CARD");
        hidePaymentTableStyleForDivs("CUSTOMER_ACCOUNT");
        hidePaymentTableStyleForDivs("OTHER");
        var isNewPayment = false;
        if(custPaymentKey == null || custPaymentKey == ""){
            isNewPayment = true;
        }
        if(pmtType != null){
            var pmtTypeGroup = typeGroupArray[pmtType];
             if(pmtTypeGroup == "CREDIT_CARD"){
                 setPaymentTableStyleForDivs("CREDIT_CARD");
                 if(isNewPayment == false){
                         focusElement = "creditCardName";
                 }else if(isNewPayment == true){
                     focusElement = "paymentTypeList";
                 }
             }else if(pmtTypeGroup == "CUSTOMER_ACCOUNT"){
                 setPaymentTableStyleForDivs("CUSTOMER_ACCOUNT");
                 if(isNewPayment == false){
                         focusElement = "accountLimitCurrency";
                 }else if(isNewPayment == true){
                     focusElement = "paymentTypeList";
                 }
             }else if(pmtTypeGroup == "OTHER") {
                 setPaymentTableStyleForDivs("OTHER");
             }
        }
    }
    svg_classhandlers_decoratePage();
    document.getElementById("paymentType").value = document.getElementById("paymentTypeList").value;
    if(focusElement!=null && focusElement!=""){
        var elemFocus = Ext.get(focusElement);
        if(elemFocus)
            elemFocus.focus.defer(500, elemFocus);
    }
}

/**
 * Method called on onClick of the Save/Done button in the editable payment light box.
 * Performs the following tasks:
 * 
 *  1. Does validation of UI-Fields
 *  2. If validation successful, checks if Payment type is of Group "CREDIT_CARD";
 *      a)if "CREDIT_CARD"; 
 *          i) if new pamyent method; calls getTokenForCC method (padss.js) for tokenization
 *          ii) if not new payment method; submits form
 *      b)if not "CREDIT_CARD"; submits the form
 *      
 * @param actionURL --> action for form-submit
 */

function submitPaymentmethod(actionURL){
    submitActionURL = actionURL;
    var pmtType = document.getElementById("paymentType").value;
    var pmtTypeGroup = typeGroupArray[pmtType];
    var isCCNumberEntered = true;
    if(pmtTypeGroup == "CREDIT_CARD"){
        if(document.managePaymentMtd.isExistingPaymentMtd.value == 'false'  && document.managePaymentMtd.tokenizationRqd.value == 'true'){
            isCCNumberEntered = isCardNumberEntered('ccTokenGen', "ccTokenizeFaildiv");
        }
    }
    if(swc_validateForm("managePaymentMtd") && isCCNumberEntered == true){
        if(pmtTypeGroup == "CREDIT_CARD"){
            if(document.managePaymentMtd.isExistingPaymentMtd.value == 'false'){//check if adding new or editing existing payment method
                if(document.managePaymentMtd.tokenizationRqd.value == 'true'){
                    getTokenForCC('ccTokenGen', "saveCreditCardAsCustomerPaymentmethod()", pmtType);
                }
                else if(document.managePaymentMtd.tokenizationRqd.value == 'false'){
                        document.managePaymentMtd.action.value = actionURL;
                        document.managePaymentMtd.submit();
                    }
            }
            else{
                document.managePaymentMtd.action.value = actionURL;
                document.managePaymentMtd.submit();
            }
        }
        else{
            document.managePaymentMtd.action.value = actionURL;
            document.managePaymentMtd.submit();
        }
    }
    
}

/**
 * Call back method after Tokenization of CreditCardNumber. Performs the following tasks:
 *  1. Validates the Credit Card type
 *  2. Adds the proper UI error message based on the response after tokenization
 *  3. Submits the form if no error
 */

function saveCreditCardAsCustomerPaymentmethod(){
    if(handlePostTokenization("ccTokenGen", "managePaymentMtd", "cardToken", "cardDisplayValue", "ccTokenizeFaildiv")){
        document.managePaymentMtd.isTokenized.value = true;
        if(swc_validateForm("managePaymentMtd")){//duplicate payment method check
            document.managePaymentMtd.action.value = submitActionURL;
            document.managePaymentMtd.submit();
        }
        else{
            document.managePaymentMtd.isTokenized.value = false;
        }
    }
}

/**
 * Method call for onChange of Country field (drop-down) to show the states.
 * 
 * @param currentForm --> form-element
 * @param ajaxURL --> URL for action which gets the states
 * @param disabled --> attribute value for "disabled" for the field
 * 
*/

function showStatesForCountry(currentForm, ajaxURL, disabled)
{
    var country = currentForm.country.value;
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


function populatePaymentTypeGroups(){
    typeGroupArray = new Array();
    var paymentTypeGroupAttrib = document.getElementById("paymentTypeGroupList").value;
    var nameValuePairs = paymentTypeGroupAttrib.split(";");
    for (i=0; i<nameValuePairs.length; i++) {
        var nameAndValue = nameValuePairs[i].split(":");
        var paymentType = nameAndValue[0];
        var paymentTypeGroup = nameAndValue[1];
        typeGroupArray[paymentType] = paymentTypeGroup;
    }
}

function setPaymentTableStyleForDivs(pmtTypeGroup){
    
    if(pmtTypeGroup == "CREDIT_CARD"){
        var divElement = Ext.get("commoncreditCardInfoTable");
        if(divElement != null)
        {
            divElement.replaceClass("hidden-div-for-payment","visible-div-for-payment"); 
        }
        divElement = Ext.get("commoncreditCardInfoTable2");
        if(divElement != null)
        {
            divElement.replaceClass("hidden-div-for-payment","visible-div-for-payment"); 
        }
    }
    else if(pmtTypeGroup == "CUSTOMER_ACCOUNT"){
        divElement = Ext.get("commonaccountInfoTable");
        if(divElement != null)
        {
            divElement.replaceClass("hidden-div-for-payment","visible-div-for-payment"); 
        }
    }
    else if(pmtTypeGroup == "OTHER"){
        divElement = Ext.get("commonotherInfoTable");
        if(divElement != null)
        {
            divElement.replaceClass("hidden-div-for-payment","visible-div-for-payment"); 
        }
    }
    
}

function hidePaymentTableStyleForDivs(pmtTypeGroup){
    
    if(pmtTypeGroup == "CREDIT_CARD"){
        var divElement = Ext.get("commoncreditCardInfoTable");
        if(divElement != null)
        {
            divElement.replaceClass("visible-div-for-payment", "hidden-div-for-payment"); 
        }
        divElement = Ext.get("commoncreditCardInfoTable2");
        if(divElement != null)
        {
            divElement.replaceClass("visible-div-for-payment", "hidden-div-for-payment"); 
        }
    }
    else if(pmtTypeGroup == "CUSTOMER_ACCOUNT"){
        divElement = Ext.get("commonaccountInfoTable");
        if(divElement != null)
        {
            divElement.replaceClass("visible-div-for-payment", "hidden-div-for-payment"); 
        }
    }
    else if(pmtTypeGroup == "OTHER"){
        divElement = Ext.get("commonotherInfoTable");
        if(divElement != null)
        {
            divElement.replaceClass("visible-div-for-payment", "hidden-div-for-payment");  
        }
    }
    
}