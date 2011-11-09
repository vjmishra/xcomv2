function validateAddress(formObj, actionURL,validateURL,verifyaddrDiv,callbackMethod, submitFormOnSuccess){
  var objArray = new Array("AddressID", "AddressLine1", "AddressLine2", "City", "Company", "Country", "DayPhone", "EMailID", "EveningPhone",
                "FirstName", "LastName", "MiddleName", "PersonInfoKey", "SameAsShipping", "State", "Title", "ZipCode");
  var paramStr ="";
  if(submitFormOnSuccess === undefined){
      submitFormOnSuccess = true;
  }
  for(i=0; i<objArray.length; i++)
  {
    var ele = eval(formObj[objArray[i]]);
    if(ele)
      paramStr += "&"+objArray[i]+"="+ele.value;
  }
  if(paramStr.length > 1)
    paramStr = paramStr.substring(1);
    Ext.Ajax.request({
        url : validateURL,
        params : paramStr,
        method: 'GET',
        // if we get a successful response, parse the data then render the panel
        success: function ( response, request ) {
          theDiv = getErrorDiv(verifyaddrDiv);
          if(theDiv)
              theDiv.innerHTML = response.responseText;
            if(document.getElementById("resultStatus")){
             var resp =  document.getElementById("resultStatus").value;
             if ("VERIFIED"== resp ||"UE_MISSING"== resp || "AVS_DOWN"== resp ){
                if(submitFormOnSuccess == true){
                    formObj.action=actionURL;
                    formObj.submit();
                }
                return true;
                }
             else{
                return false;
              }
        }
        return true;
        },
        failure: function ( response, request ) {
            errorText = response.responseText;
            if(errorText.toLowerCase().indexOf("<html") > 0)
            {
                alternateMessage = document.getElementById("ErrorValidatingAddress");
                if(alternateMessage != null)
                {
                    errorText = alternateMessage.value;
                }
            }
            
            theDiv = getErrorDiv(verifyaddrDiv);
            if(theDiv != null)
            {
              theDiv.innerHTML = errorText;
            }
            else
            {
                alert(errorText);
            }
            return false;
        },
    callback:function(){
      if(callbackMethod){
        eval(callbackMethod);
      }
    }
    });
}

function getErrorDiv(verifyaddrDiv)
{
    theDiv = document.getElementById(verifyaddrDiv);
    if(theDiv == null)
    {
      theDiv = document.getElementById("verifyaddr");
    }

    return theDiv;
}

function validateAddressForPayment(formObj, actionUrl, validateURL,callBackFunction,verifyaddrDiv){
  var result = false;
  var objArray = new Array("AddressID", "AddressLine1", "AddressLine2", "City", "Company", "Country", "DayPhone", "EMailID",
                "FirstName", "LastName", "MiddleName", "PersonInfoKey",  "State", "ZipCode", "IsCommercialAddress");
  var paramStr ="";
  for(i=0; i<objArray.length; i++)
  {
    var ele = eval(formObj[objArray[i]]);
    if(ele)
      paramStr += "&"+objArray[i]+"="+ele.value;
  }
  if(paramStr.length > 1)
    paramStr = paramStr.substring(1);

    Ext.Ajax.request({
        url : validateURL,
        params : paramStr,
        method: 'GET',
        // if we get a successful response, parse the data then render the panel
        success: function ( response, request ) {
            var theDiv = document.getElementById(verifyaddrDiv);
            if(theDiv == null)
              theDiv = document.getElementById("verifyaddr");
          if(theDiv)
              theDiv.innerHTML = response.responseText;
            if(document.getElementById("resultStatus")){
             var resp =  document.getElementById("resultStatus").value;
             if ("VERIFIED"== resp ||"UE_MISSING"== resp || "AVS_DOWN"== resp ){
                callBackFunction();
                return true;
                }
             else{
               return false;
             }
        }

        return true;
        },
        failure: function ( response, request ) {
            alert('Failed: ' + response.responseText);
            return false;
        }
    });
}