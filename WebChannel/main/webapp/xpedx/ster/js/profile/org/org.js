/* This file is sepcific to any methods related to all pages in Org Profile. This file is not used for Corporate Information
 * tab of Organization Profile.
 * 
 */

/**
 * @author sshailaja
 * 
 */


/*Variables to save whether the Address panel should be expanded on page load or not.
 * Use case: i) On save/edit, delete or duplicate of address; address panel should be expanded
*/
var expAddPnl = false;

/**
 * onReady function. Does the following tasks:
 *  1. expands and collpases the panels based on the use case. Uses slider1 function defined in profile.js
 *  2. Applies SVG after expand/collpase of panels. Calls svg_classhandlers_decoratePage method in theme-javascript 
 * 
 */

Ext.onReady(function() {
    expPanel();
    if(expAddPnl == "false"){
        var expAddPanel = false;
        document.getElementById("addressBookTable").style.display = "none";
    }
    else{
        var expAddPanel = true;
    }
    var genInfoElem = Ext.get('generalInfoTable');
    if(genInfoElem)
        slider1('generalInfoTable','toggleGeneralInfo','sliderIn','sliderOut', true);
    var addBookElem = Ext.get('addressBookTable');
    if(addBookElem)
        slider1('addressBookTable','toggleAddressBook','sliderIn','sliderOut', expAddPanel);
    svg_classhandlers_decoratePage();
});

<!-- General Information Section Scripts Starts  -->

/**
 * Method called on onClick of the Save/Done button in the editable address light box in org-profile.
 * Calls the validateAddress function (profile.js) by passing the appropriate parameters. The validateAddress function calls the
 * user-exit to validate the address-info and then submits the form to save the address
 *      
 * @param formObj --> form element
 * @param validateURL --> URL for action which calls the user exit for address validation
 */

function saveAddressInfo(formObj,validateURL, actionURL){
	 validateAddress(formObj,actionURL,validateURL, "verifyaddr",'svg_classhandlers_decoratePage()',swc_validateForm(formObj.name));
}

/**
 * Variable which has the function for form-submit or show the confirmation message popup for Delete/duplicate address, or payment methods
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
 * Method called on onClick of the Duplicate button in the General Information page of Org profile to duplicate selected addresses.
 * Checks if at least one check-box is selected (validateCheckBox method in profile.js).
 * Opens confirmation message pop-up (confirmMessageLightBox method in profile.js)
 *      
 * @param confirmMsg --> confirmation message to display
 * 
 */

function duplicateAddress(confirmMsg, duplicateactionURL, confirmJSPURL)
{

    if(validateCheckBox("updateCustOrg","duplicateCustomerAddress")){
        var arr = new Array();
        arr[0]= "updateCustOrg";
        arr[1]= duplicateactionURL;
    	var val = confirmMessageLightBox("ajax-body-message", "modalDialogMessagePanel", confirmJSPURL, confirmMsg,callbackIfOK ,arr, "deleteCustomerAddress");
    }
    else{
        document.getElementById("actionName").value = "deleteCustomerAddress";
    }
}

/**
 * Method called on onClick of the Delete button in the General Information page of Org profile to delete selected addresses.
 * Checks if at least one check-box is selected (validateCheckBox method in profile.js).
 * Opens confirmation message pop-up (confirmMessageLightBox method in profile.js)
 *      
 * @param confirmMsg --> confirmation message to display
 * 
 */

function deleteAddress(confirmMsg, deleteactionURL, confirmJSPURL)
{

    if(validateCheckBox("updateCustOrg","deleteCustomerAddress")){
      	var arr = new Array();
        arr[0]= "updateCustOrg";
        arr[1]= deleteactionURL;
    	var val = confirmMessageLightBox("ajax-body-message", "modalDialogMessagePanel", confirmJSPURL,confirmMsg,callbackIfOK ,arr, "");
    }
    else{
        document.getElementById("actionName").value = "deleteCustomerAddress";
    }

}

/**
 * Method called on opening any existing address in edit address light box (lightbox mehod in profile.js)
 *       
 * 
 */

function openAddressLightBox(bodyId,panelId,input){
 	lightBox(bodyId,panelId,input,"updateAddresspopup()","editableAddress.AddressID");
}

/**
 * Callback method on opening address-light box in edit mode for existing addressses to disable the fields if edit not allowed
 *       
 * 
 */

function updateAddresspopup(){
  disableInputs(document.updateCustAddress,hasAccessTo );
}

/**
 * Method called on click of Add new button in General Information page in org profile for adding new address to
 * open the address light box (lightbox mehod in profile.js)
 *       
 * 
 */
function addAddressLightBox(urls)
{
	 lightBox("ajax-body-1","modalDialogPanel2",urls, "svg_classhandlers_decoratePage()", "editableAddress.AddressID");
}

<!-- General Information Section Scripts Ends -->

<!-- Payment Section Script Starts -->

/**
 * Method called on onClick of the Add new button in the Payment Information page of Org profile to adding new payment methods in 
 * payment light box (lightbox mehod in profile.js)
 *       
 * 
 */

function addPaymentmethodLightBox(urls)
{
	 lightBox("ajax-body-1", "modalDialogPanel2", urls, "showPaymentType()","");
}

/**
 * Method called on opening any existing payment method in edit address light box (lightbox mehod in profile.js)
 *       
 * 
 */

function openPaymentmethodLightBox(bodyId, panelId, input){

	lightBox(bodyId,panelId,input, "showPaymentType()","");

}
/**
 * Method called on onClick of the Delete button in the Payment Information page of Org profile to delete selected payment methods.
 * Checks if at least one check-box is selected (validateCheckBox method in profile.js).
 * Opens confirmation message pop-up (confirmMessageLightBox method in profile.js)
 *      
 * @param confirmMsg --> confirmation message to display
 * 
 */
function deletePaymentmethod(confirmMsg, deleteactionURL, confirmJSPURL)
{

    if(validateCheckBox("paymentinfo","deleteCustomerPaymentMethod")){
    	var arr = new Array();
        arr[0]= "paymentinfo";
        arr[1]= deleteactionURL;
    	var val = confirmMessageLightBox("ajax-body-message", "modalDialogMessagePanel", confirmJSPURL,confirmMsg,callbackIfOK ,arr, "updateCustomerPaymentInfo");
    }


}

<!-- Payment Section Script ENDS -->


/**
 * Method for expand collapse links in General Information page in Org profile.
 * 
 */

function expandOrCollapse(){
    var showImgClassVar1 = "sliderIn";
    var showImgClassVar2 = "sliderIn";
    var imgDiv1 = document.getElementById("toggleGeneralInfo");
    var imgDiv2 = document.getElementById("toggleAddressBook");
    if(imgDiv1)
        showImgClassVar1 = imgDiv1.className;
    if(imgDiv2)
        showImgClassVar2 = imgDiv2.className;
     if(!((showImgClassVar1 == null || showImgClassVar1=="" || showImgClassVar1.match("sliderIn")) && (showImgClassVar2 == null || showImgClassVar2==""|| showImgClassVar2.match("sliderIn"))))
     {
         slider2('generalInfoTable','toggleGeneralInfo','sliderIn','sliderOut', true);
         slider2('addressBookTable','toggleAddressBook','sliderIn','sliderOut', true);
     }else{
         slider2('generalInfoTable','toggleGeneralInfo','sliderIn','sliderOut', false);
         slider2('addressBookTable','toggleAddressBook','sliderIn','sliderOut', false);
     }
    svg_classhandlers_decoratePage();
  }

function submitGenInfoAfterValidation(){
    if(swc_validateForm("getCustOrg") == false)
    {
        return;
    }

    document.getCustOrg.submit();
}

