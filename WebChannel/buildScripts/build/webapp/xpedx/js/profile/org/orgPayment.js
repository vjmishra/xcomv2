/**
 * onReady function. Does the following tasks:
 *  1. expands and collpases the panels based on the use case. Uses slider1 function defined in profile.js
 *  2. Applies SVG after expand/collpase of panels. Calls svg_classhandlers_decoratePage method in theme-javascript 
 * 
 */

var expTaxPnl = false;

function expandPaymentPanels() {
    expTaxPnl = expPanel();
    var expTaxPanel = false;
    if(expTaxPnl == 'false'){
        expTaxPanel = false;
        document.getElementById("TaxInfoTable").style.display = "none";
    }
    else{
        expTaxPanel = true;
    }
        
    var accountElem = Ext.get('toggleAccounts');
    if(accountElem)
        slider1('AccountInfoTable','toggleAccounts','sliderIn','sliderOut', true);
    var ccElem = Ext.get('toggleCreditCard');
    if(ccElem)
        slider1('CreditCardInfoTable','toggleCreditCard','sliderIn','sliderOut', true);
    var taxInfoElem = Ext.get('toggleTax');
    if(taxInfoElem)
        slider1('TaxInfoTable','toggleTax','sliderIn','sliderOut', expTaxPanel);
    svg_classhandlers_decoratePage();
}

/**
 * Method for expand collapse links in Payment Information page in Org profile.
 * 
 */

function expandOrCollapsePaymentPanels(){
    var showImgClassVar1 = "sliderIn";
    var showImgClassVar2 = "sliderIn";
    //var showImgClassVar3 = "sliderIn";
    var imgDiv1 = document.getElementById("toggleAccounts");
    var imgDiv2 = document.getElementById("toggleCreditCard");
    //var imgDiv3 = document.getElementById("toggleOther");
    var imgDiv4 = document.getElementById("toggleTax");
    if(imgDiv1)
        showImgClassVar1 = imgDiv1.className;
    if(imgDiv2)
        showImgClassVar2 = imgDiv2.className;
    //if(imgDiv3)
      //  showImgClassVar3 = imgDiv3.className;
    if(imgDiv4)
        showImgClassVar4 = imgDiv4.className;
     if(!((showImgClassVar1 == null || showImgClassVar1=="" || showImgClassVar1.match("sliderIn")) && (showImgClassVar2 == null || showImgClassVar2==""|| showImgClassVar2.match("sliderIn")) && (showImgClassVar4 == null || showImgClassVar4==""|| showImgClassVar4.match("sliderIn"))))
     {
         slider2('AccountInfoTable','toggleAccounts','sliderIn','sliderOut', true);
         slider2('CreditCardInfoTable','toggleCreditCard','sliderIn','sliderOut', true);
       if(imgDiv4){
           slider2('TaxInfoTable','toggleTax','sliderIn','sliderOut', true);
       }
     }else{
         slider2('AccountInfoTable','toggleAccounts','sliderIn','sliderOut', false);
         slider2('CreditCardInfoTable','toggleCreditCard','sliderIn','sliderOut', false);
       if(imgDiv4){
           slider2('TaxInfoTable','toggleTax','sliderIn','sliderOut', false);
       }
     }
     svg_classhandlers_decoratePage();
  }
