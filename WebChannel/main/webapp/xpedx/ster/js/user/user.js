/* This file is sepcific to any methods related to General Information page in User Profile.
 * 
 */

/**
 * @author vbandhu
 * 
 */

/*Variables to save whether the Phone panel and the Address panel should be expanded on page load or not.
 * Use case: i) On save/edit, delete or duplicate of address; address panel should be expanded
 *           ii)On save of General Information; Phone panel should be expanded
*/
var expAddPnl = false;
var expPhnPnl = false;

/**
 * onReady function. Does the following tasks:
 *  1. expands and collpases the panels based on the use case. Uses slider1 function defined in profile.js
 *  2. Applies SVG after expand/collpase of panels. Calls svg_classhandlers_decoratePage method in theme-javascript 
 * 
 */

Ext.onReady(function() {
    xyz();//method initialized inside <script> block in editablePayment.jsp to prepare the array expAddPnl, expPhnPnl
    if(expAddPnl == "false"){
        var expAddPanel = false;
        document.getElementById("addressBookTable").style.display = "none";
    }
    else{
        var expAddPanel = true;
    }
    if(expPhnPnl == "false"){
        var expPhonePnl = false;
        document.getElementById("phoneBookTable").style.display = "none";
    }
    else{
        var expPhonePnl = true;
    }
    slider1('generalInfoTable','toggleUserInfo','sliderIn','sliderOut', true);
    slider1('addressBookTable','toggleAddBook','sliderIn','sliderOut', expAddPanel);
    slider1('phoneBookTable','togglePhoneBook','sliderIn','sliderOut', expPhonePnl);
    var adminListElem = Ext.get('toggleAdminList');
    if(adminListElem)
        slider1('adminListTable','toggleAdminList','sliderIn','sliderOut', false);
    svg_classhandlers_decoratePage();
});


/**
 * Method called on onClick of the Save/Done button in the editable address light box in user-profile.
 * Calls the validateAddress function (profile.js) by passing the appropriate parameters. The validateAddress function calls the
 * user-exit to validate the address-info and then submits the form to save the address
 *      
 * @param formObj --> form element
 * @param validateURL --> URL for action which calls the user exit for address validation
 */

function saveAddressInfo(formObj,validateURL, submitActionURL){
    validateAddress(formObj,submitActionURL,validateURL, "verifyaddr",'svg_classhandlers_decoratePage()',swc_validateForm(formObj.name));
}
function saveAsDefaultAddressInfo(formObj,submitActionURL){
    if(formObj){
               formObj.action=submitActionURL;
               formObj.submit();
           } 
}
/**
 * Variable which has the function for form-submit or show the confirmation message popup for Delete/duplicate address 
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
 * Method called on onClick of the Duplicate button in the General Information page of User profile to duplicate selected addresses.
 * Checks if at least one check-box is selected (validateCheckBox method in profile.js).
 * Opens confirmation message pop-up (confirmMessageLightBox method in profile.js)
 *      
 * @param confirmMsg --> confirmation message to display
 * 
 */

function duplicateAddress(confirmMsg, duplicateactionURL, confirmJSPURL)
{
   if(validateCheckBox("myAccount", "duplicateAddress")){
       var arr = new Array();
       arr[0]= "myAccount";
       arr[1]= duplicateactionURL;

       var val = confirmMessageLightBox("ajax-body-message", "modalDialogMessagePanel", confirmJSPURL, confirmMsg,callbackIfOK ,arr, "saveUserInfo");
    }
   else{
       document.getElementById("actionName").value = "saveUserInfo";
   }
}

/**
 * Method called on onClick of the Delete button in the General Information page of User profile to delete selected addresses.
 * Checks if at least one check-box is selected (validateCheckBox method in profile.js).
 * Opens confirmation message pop-up (confirmMessageLightBox method in profile.js)
 *      
 * @param confirmMsg --> confirmation message to display
 * 
 */

function deleteAddress(confirmMsg, deleteactionURL, confirmJSPURL)
{
   if(validateCheckBox("myAccount", "deleteAddress")){
       var arr = new Array();
       arr[0]= "myAccount";
       arr[1]= deleteactionURL;
           var val = confirmMessageLightBox("ajax-body-message", "modalDialogMessagePanel", confirmJSPURL,confirmMsg,callbackIfOK ,arr, "saveUserInfo");
    }
   else{
       document.getElementById("actionName").value = "saveUserInfo";
   }
}

/**
 * Method called on onClick of the Add new button in the General Information page of User profile to adding new address; or opening any 
 * existing address in edit address light box (lightbox mehod in profile.js)
 *       
 * 
 */

function openAddressLightBox(bodyId,panelId,input){
    lightBox(bodyId,panelId,input,"", "editableAddress.AddressID");
}

/**
 * Method for expand collapse links in General Information page in User profile.
 * 
 */

function expandOrCollapse(){
   var showImgClassVar1 = "sliderIn";
   var showImgClassVar2 = "sliderIn";
   var showImgClassVar3 = "sliderIn";
   var imgDiv1 = document.getElementById("toggleUserInfo");
   var imgDiv2 = document.getElementById("toggleAddBook");
   var imgDiv3 = document.getElementById("togglePhoneBook");
   var imgDiv4 = document.getElementById("toggleAdminList");
   if(imgDiv1)
       showImgClassVar1 = imgDiv1.className;
   if(imgDiv2)
       showImgClassVar2 = imgDiv2.className;
   if(imgDiv3)
       showImgClassVar3 = imgDiv3.className;
   if(imgDiv4)
       showImgClassVar4 = imgDiv4.className;
   var toExpandAll = true;
    if(!((showImgClassVar1 == null || showImgClassVar1=="" || showImgClassVar1.match("sliderIn")) && (showImgClassVar2 == null || showImgClassVar2==""|| showImgClassVar2.match("sliderIn"))&& (showImgClassVar3 == null || showImgClassVar3=="" || showImgClassVar3.match("sliderIn"))))
    {
        toExpandAll = true;
    }
    else if(imgDiv4){
            if(!((showImgClassVar4 == null || showImgClassVar4=="" || showImgClassVar4.match("sliderIn"))))
            {
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
        slider2('generalInfoTable','toggleUserInfo','sliderIn','sliderOut', true);
        slider2('addressBookTable','toggleAddBook','sliderIn','sliderOut', true);
        slider2('phoneBookTable','togglePhoneBook','sliderIn','sliderOut', true);
      if(imgDiv4){
          slider2('adminListTable','toggleAdminList','sliderIn','sliderOut', true);
      }
    }else{
        slider2('generalInfoTable','toggleUserInfo','sliderIn','sliderOut', false);
        slider2('addressBookTable','toggleAddBook','sliderIn','sliderOut', false);
        slider2('phoneBookTable','togglePhoneBook','sliderIn','sliderOut', false);
      if(imgDiv4){
          slider2('adminListTable','toggleAdminList','sliderIn','sliderOut', false);
      }
    }
    svg_classhandlers_decoratePage();
 }

function expPanelBeforeSave(){
    slider2('generalInfoTable','toggleUserInfo','sliderIn','sliderOut', true);
    slider2('phoneBookTable','togglePhoneBook','sliderIn','sliderOut', true);
    return true;
}

function resetPassword(actionURL){
    Ext.Ajax.request({
        url :  actionURL,
        method: 'POST',
        scripts: true,
        success: function ( response, request ){
            theDiv = document.getElementById("resetPwd");
            if(theDiv){
                theDiv.innerHTML = response.responseText;
            }
            if(document.getElementById("resultStatus")){
               var resp =  document.getElementById("resultStatus").value;
               if ("success"== resp){
                   DialogPanel.show("modalDialogMessagePanelForResetPasswordSuccess");
               }
               else{
                   DialogPanel.show("modalDialogMessagePanelForResetPasswordFailure");
               }
          }
            theDiv.innerHTML = "";
        },
        failure: function ( response, request ) {
            DialogPanel.show("modalDialogMessagePanelForResetPasswordFailure");
        }
    });
}
