/* This file is sepcific to any methods related to PA-DSS calls. It contains methods which can be accesssed at page level
 * in the application to perform tasks related to tokenization of CC-number and SVC-number
*/

/**
 * @author vbandhu
 * 
 */

/**
 * Generic method called on by individual payment pages in application for tokenization of CC-number and SVC-number
 * Performs the following tasks:
 * 
 *  1. PA-dss form submit using Ajax call to PADSS server
 *  2. Attach the response text to iframe.innerhtml
 *  3. pass the flow to the callbackMethod
 *  
 * On failed tokenization call; shows alert. The behavior may change to show proper UI-message
 *      
 * @param frameNameId --> id of the iframe
 * @param callbackMethod --> java-script method to call after successful tokenization response
 * @param paymentType --> Need to pass this to the SSSDCS server
 */

function getTokenForCC(frameNameId, callbackMethod, paymentType) {
    var frameName = frameNameId;
    var enteredValue = window.frames[frameName].document.ssdcs.ssdcsDataToTokenize.value;
    window.frames[frameName].document.ssdcs.ssdcsDataTypeDetail.value = paymentType;
       Ext.Ajax.request({
          url : padssURL,
          form : window.frames[frameName].document.ssdcs, 
          method: 'post',
              // if we get a successful response, parse the data then render the panel
              success: function ( response, request ) {
                       theDiv = document.getElementById(frameName);
                       if(theDiv)
                           window.frames[frameName].document.body.innerHTML = response.responseText;
                       window.frames[frameName].document.ssdcs.ssdcsDataToTokenize.value = enteredValue;
                       eval(callbackMethod); /*the response from the padss server which includes the ResultCode, tokenized value, display value
                       is attached to the frame. callbackMethod can perform additional validations before form-submit by reading the elements in the
                       frame attached from the response from the padss server. Also, add additional error messages in the UI, if the tokenization 
                       failed based on the ResultCode and ResultDescription.
                       */
                      },
              failure: function ( response, request ) {
                  alert(errorMsgArray['ALERT_TOKENIZATION_FAILURE'] + response.responseText);
              }
          });
}

/**
 * Generic method called on by the individual page-level callbackMethod parametrized in the above function.
 * 
 *  1. Checks if tokenization failed:
 *      i) if Failed, attaches the proper error message to the errorDisplayElem, and returns false
 *      ii) if passed, sets the token value and the displayValue in the form formName
 *      and returns true;
 *  
 * On failed tokenization call; shows alert. The behavior may change to show proper UI-message
 *      
 * @param frameNameId --> id of the iframe
 * @param formName --> form into which the token and DisplayValue needs to be put
 * @param tokenElemId --> hidden param name to store the token vlaue
 * @param displayValElemId --> hidden param name to store the Display vlaue
 * @param errorDisplayElem --> hidden div to add UI-Error messages
 */

function handlePostTokenization(frameNameId, formName, tokenElemId, displayValElemId, errorDisplayElem){
    var result = window.frames[frameNameId].document.ssdcs.ssdcsResultCode.value;
    form = document.getElementById(formName);
    clearErrorMessages(form);
    clearErrorLabels(form);
    // now delete the paragraphsToDelete
    var divs = document.getElementsByTagName("div");
    var paragraphsToDelete = new Array();
    for(var i = 0; i < divs.length; i++) {
        var p = divs[i];
        if (p.getAttribute("errorFor")) {
            if(p.getAttribute("errorFor") == errorDisplayElem){
                paragraphsToDelete.push(p);
            }
        }
    }
    // now delete the paragraphsToDelete
    for (var i = 0; i < paragraphsToDelete.length; i++) {
        var r = paragraphsToDelete[i];
        var parent = r.parentNode;
        parent.removeChild(r);
    }
    if(result == "SUCCESS"){
        var enteredValue = window.frames[frameNameId].document.ssdcs.ssdcsDataToTokenize.value;
        if(enteredValue == null || enteredValue == ""){
            addError(document.getElementById(errorDisplayElem), errorMsgArray['BLANK_DATA']);
            return false;
        }
        var dataType = window.frames[frameNameId].document.ssdcs.ssdcsDataType.value;
        if(dataType == "ssdcsCreditCardNumber"){
            var cardType = evaluateCardType(frameNameId);
            var selectedcardType = form.elements["creditCardType"].value
            if(cardType != "" && cardType != selectedcardType){
                //exception case where Computed type is CART_BLANCHE but selected card is DINERS_CLUB as the latter has taken over the former
                if(selectedcardType=="DINERS_CLUB" && cardType=="CARTE_BLANCHE"){
                    
                }
                else{
                    addError(document.getElementById(errorDisplayElem), errorMsgArray['INVALID_CARD_FOR_TYPE']);
                    return false;
                }
            }
        }
        form.elements[tokenElemId].value = window.frames[frameNameId].document.ssdcs.ssdcsToken.value;
        form.elements[displayValElemId].value = window.frames[frameNameId].document.ssdcs.ssdcsDisplayValue.value;
        return true;
    }
    else if(result == "FAIL"){
        var failReason = window.frames[frameNameId].document.ssdcs.ssdcsFailReason.value;
        if(failReason == "INVALID_SESSION"){
            addError(document.getElementById(errorDisplayElem), errorMsgArray['INVALID_SESSION']);
            reloadIframeWithNewAccessToken(frameNameId);
        }
        else if(failReason == "INVALID_DATA"){
            addError(document.getElementById(errorDisplayElem), errorMsgArray['INVALID_DATA']);
        }
        else if(failReason == "TOKENIZATION_FAILED"){
            addError(document.getElementById(errorDisplayElem), errorMsgArray['TOKENIZATION_FAILED'] + window.frames[frameNameId].document.ssdcs.ssdcsResultDescription.value);
        }
        else{
            addError(document.getElementById(errorDisplayElem), errorMsgArray['OTHER_REASON_TOKENIZATION_FAILED'] + window.frames[frameNameId].document.ssdcs.ssdcsFailReason.value);
        }
        return false;
    }
    
}

/**
 * Reload the iframe with new Access token if token expired at the time
 * of tokenization.
 * 
 * @param frameNameId --> id of the iframe
 *  
 */

function reloadIframeWithNewAccessToken(frameNameId){
    Ext.Ajax.request({
        url : newTokenURL,
        params : {none:"none"},
        method: 'post',
            // if we get a successful response, parse the data then render the panel
            success: function ( response, request ) {
                    var newAccessTokenDiv = document.getElementById("statusResp");
                    if(newAccessTokenDiv) {
                        newAccessTokenDiv.innerHTML = response.responseText;
                    }
                    var newAccessToken = document.getElementById("newAccessTokenVal").value;
                    newAccessTokenDiv.innerHTML = "";
                     window.frames[frameNameId].document.ssdcs.ssdcsAuthenticationToken.value = newAccessToken;
                    },
            failure: function ( response, request ) {
                alert(errorMsgArray['ALERT_IFRAME_RELOAD_FAILURE'] + response.responseText);
            }
        });
}


/**
 * Returns the CreditCardType parsing the hidden parameter "ssdcsAdditionalResultData"
 * from the ssdcs form. Assumption is that the parameter "ssdcsAdditionalResultData" is 
 * a string of list of NAME:VALUE pair of the format "ZZZ:YYY;AAA:BBB;DDD:UUU".
 * 
 * @param frameNameId --> id of the iframe
 *  
 */

function evaluateCardType(frameNameId){
    var additionalResultData = window.frames[frameNameId].document.ssdcs.ssdcsAdditionalResultData.value;
    var cardType= "";
    var nameValuePairs = additionalResultData.split(";");
    for (i=0; i<nameValuePairs.length; i++) {
        var nameAndValue = nameValuePairs[i].split(":");
        if (nameAndValue[0]=="CardType")
            cardType = nameAndValue[1];
    }
    return cardType;
}


/**
 * Dialog message to be shown on page load when tokenization is required and Payment capture server is 
 * not available.
 * 
 * @param isTokenizationReqd 
 * @param isPaymentServerAvailable
 *  
 */

function showSSDCSServerUnavailable(isTokenizationReqd, isPaymentServerAvailable){
    if(isTokenizationReqd == true && isPaymentServerAvailable == false){
        var cmp = Ext.getCmp("modalDialogMessagePanelForssdcsUnavailable");
        if(cmp!=null){
            DialogPanel.show("modalDialogMessagePanelForssdcsUnavailable");
        }
    }
}

/**
 * Check if card number was entered or not. If not then add error message "Required field"
 * in the errorDisplayElem
 * 
 * @param frameNameId 
 * @param errorDisplayElem
 *  
 */

function isCardNumberEntered(frameNameId, errorDisplayElem){
 // now delete the paragraphsToDelete
    var divs = document.getElementsByTagName("div");
    var paragraphsToDelete = new Array();
    for(var i = 0; i < divs.length; i++) {
        var p = divs[i];
        if (p.getAttribute("errorFor")) {
            if(p.getAttribute("errorFor") == errorDisplayElem){
                paragraphsToDelete.push(p);
            }
        }
    }
    // now delete the paragraphsToDelete
    for (var i = 0; i < paragraphsToDelete.length; i++) {
        var r = paragraphsToDelete[i];
        var parent = r.parentNode;
        parent.removeChild(r);
    }
    var frameName = frameNameId;
    var enteredValue = window.frames[frameName].document.ssdcs.ssdcsDataToTokenize.value;
    if(enteredValue == null || enteredValue == ""){
        addError(document.getElementById(errorDisplayElem), errorMsgArray['REQUIRED_FIELD']);
        return false;
    }
    else
        return true;
}
