/* This file is sepcific to any methods related to Payment Information page in User Profile.
 * 
 */

/**
 * @author vbandhu
 * 
 */

/*Variables to save whether the Spending Limit panel and the Proxy approver panel should be expanded on page load or not.
 *           i)On save of Payment Information; Spending limit and Proxy approver panels should be expanded
*/
var expSplPnl = false;
var expPxyPnl = false;

/**
 * onReady function. Does the following tasks:
 *  1. expands and collpases the panels based on the use case. Uses slider1 function defined in profile.js
 *  2. Applies SVG after expand/collpase of panels. Calls svg_classhandlers_decoratePage method in theme-javascript 
 * 
 */

Ext.onReady(function() {
    expPnls();
    if(expSplPnl == "false"){
        var expSplimitPnl = false;
		var spLmtElem = document.getElementById("spendingLimitTable");
		if(spLmtElem)
        document.getElementById("spendingLimitTable").style.display = "none";
    }
    else{
        var expSplimitPnl = true;
    }
    if(expPxyPnl == "false"){
        var expProxyPnl = false;
		var prxyElem = document.getElementById("proxyTable");
        if(prxyElem)
        document.getElementById("proxyTable").style.display = "none";
    }
    else{
        var expProxyPnl = true;
    }
    var accountElem = Ext.get('toggleAccounts');
    if(accountElem)
        slider1('AccountInfoTable','toggleAccounts','sliderIn','sliderOut', true);
    var ccElem = Ext.get('toggleCreditCard');
    if(ccElem)
        slider1('CreditCardInfoTable','toggleCreditCard','sliderIn','sliderOut', true);
    var spendLimitElem = Ext.get('toggleSpendingLimit');
    if(spendLimitElem)
        slider1('spendingLimitTable','toggleSpendingLimit','sliderIn','sliderOut', expSplimitPnl);
    var proxyElem = Ext.get('toggleMyProxy');
    if(proxyElem)
        slider1('proxyTable','toggleMyProxy','sliderIn','sliderOut', expProxyPnl);
    svg_classhandlers_decoratePage();
});

/**
 * Variable which has the function for form-submit or show the confirmation message popup for Delete payment 
 *
 */

var  callbackIfOK = function (orgs){
    if(orgs){
         var formObj = document.getElementById(orgs[0]);
         if(formObj){
               formObj.action=orgs[1];
               formObj.submit();
           }else {
            DialogPanel.show("modalDialogMessagePanel");
           }
      }
}

/**
 * Method called on onClick of the Delete button in the Payment Information page of User profile to delete selected payment methods.
 * Checks if at least one check-box is selected (validateCheckBox method in profile.js).
 * Opens confirmation message pop-up (confirmMessageLightBox method in profile.js)
 *      
 * @param confirmMsg --> confirmation message to display
 * 
 */

function deletePaymentmethod(confirmMsg, deleteactionURL, confirmJSPURL){

    if(validateCheckBox("myAccount1", "deletePayment")){
        var arr = new Array();
        arr[0]= "myAccount1";
        arr[1]= deleteactionURL;
        var val = confirmMessageLightBox("ajax-body-message", "modalDialogMessagePanel", confirmJSPURL,confirmMsg,callbackIfOK ,arr, "manageSpendingLimit");
    }
    else{
        document.getElementById("actionName").value = "manageSpendingLimit";
    }
}

/**
 * Method called on onClick of the Add new button in the Payment Information page of User profile to adding new payment methods; or opening any 
 * existing payment method in edit payment light box (lightbox mehod in profile.js)
 *       
 * 
 */

function addPaymentmethodLightBox(urls)
{
     lightBox("ajax-body-1", "modalDialogPanel2", urls, "showPaymentType()","");

}

/**
 * Method for expand collapse links in Payment Information page in User profile.
 * 
 */

function expandOrCollapse(){
    var showImgClassVar1 = "sliderIn";
    var showImgClassVar2 = "sliderIn";
    //var showImgClassVar3 = "sliderIn";
    var imgDiv1 = document.getElementById("toggleAccounts");
    var imgDiv2 = document.getElementById("toggleCreditCard");
    //var imgDiv3 = document.getElementById("toggleOther");
    var imgDiv4 = document.getElementById("toggleSpendingLimit");
    var imgDiv5 = document.getElementById("toggleMyProxy");
    if(imgDiv1)
        showImgClassVar1 = imgDiv1.className;
    if(imgDiv2)
        showImgClassVar2 = imgDiv2.className;
    //if(imgDiv3)
      //  showImgClassVar3 = imgDiv3.className;
    if(imgDiv4)
        showImgClassVar4 = imgDiv4.className;
    if(imgDiv5)
        showImgClassVar5 = imgDiv5.className;
    var toExpandAll = true;
     if(!((showImgClassVar1 == null || showImgClassVar1=="" || showImgClassVar1.match("sliderIn")) && (showImgClassVar2 == null || showImgClassVar2==""|| showImgClassVar2.match("sliderIn"))))
     {
         toExpandAll = true;
     }
     else if(imgDiv4){
         if (imgDiv5){
             if(!((showImgClassVar4 == null || showImgClassVar4=="" || showImgClassVar4.match("sliderIn")) && (showImgClassVar5 == null || showImgClassVar5==""|| showImgClassVar5.match("sliderIn"))))
             {
                 toExpandAll = true;
             }
             else{
                 toExpandAll = false;
             }
         }
         else if(!((showImgClassVar4 == null || showImgClassVar4=="" || showImgClassVar4.match("sliderIn")))){
             toExpandAll = true;
         }
         else{
             toExpandAll = false;
         }
         
     }
     else{
         toExpandAll = false;
     }
     if(toExpandAll){
         slider2('AccountInfoTable','toggleAccounts','sliderIn','sliderOut', true);
         slider2('CreditCardInfoTable','toggleCreditCard','sliderIn','sliderOut', true);
       if(imgDiv4){
           slider2('spendingLimitTable','toggleSpendingLimit','sliderIn','sliderOut', true);
       }
       if(imgDiv5){
           slider2('proxyTable','toggleMyProxy','sliderIn','sliderOut', true);
       }
     }else{
         slider2('AccountInfoTable','toggleAccounts','sliderIn','sliderOut', false);
         slider2('CreditCardInfoTable','toggleCreditCard','sliderIn','sliderOut', false);
       if(imgDiv4){
           slider2('spendingLimitTable','toggleSpendingLimit','sliderIn','sliderOut', false);
       }
       if(imgDiv5){
           slider2('proxyTable','toggleMyProxy','sliderIn','sliderOut', false);
       }
     }
     svg_classhandlers_decoratePage();
  }
