var saveOrderPaymentAction = null;
var focusDelay = 100; /* in millisecond. */
var typeGroupArray = new Array();
var paymentBookArrayName = new Array();
var cvvStringFromForm = "";
var resultAddressVerification = true;
  Ext.onReady(function() {
   var focusElement;
   var multiplePayment = Ext.get('multiplePayment').getValue(false);

   saveOrderPaymentAction = document.getElementById('saveOrderPaymentActionUrlLinkId')
   if(multiplePayment=='true'){
     focusElement = Ext.get('updateButtonId');
   }else{
        var oldPaymentEdit = Ext.get('isOldPaymentEdit').getValue(false);
        if(oldPaymentEdit == 'true'){
          focusElement = Ext.get('creditCardEditLink');
          if(focusElement == null){
            focusElement = Ext.get('useAddionalPaymentId');
          }
        }else{
          focusElement = Ext.get('rdPayment')
        }
   }
   if(focusElement != null){
     focusElement.focus(focusDelay);
   }


  })

  /* This function would be used on the light box used in the multi non gift payment page
     for setting the focus. This function would be called
     after  svg_classhandlers_decoratePage() function.
   */
  function setFocus(paymentType, newPayment){
   var focusElement;
   var paymentTypeGroup =  typeGroupArray[paymentType];
   if(paymentTypeGroup == 'CREDIT_CARD'){
       if(newPayment){
          focusElement = Ext.get('rdPayment')
       }else{
         focusElement = Ext.get('creditCardExpMonth');
       }
   }else if (paymentTypeGroup == 'CUSTOMER_ACCOUNT'){
        focusElement = Ext.get('customerAccountOnPB');
   }
   if(focusElement != null){
     focusElement.focus(focusDelay);
   }
  }
  function showCVVHelp(){
   Ext.Ajax.request({
                url :   document.getElementById('GetCVVNumberHelp').href,
                method: 'GET',
              success: function ( response, request ) {
              var theDiv = document.getElementById("cvvHelpDiv");
              theDiv.innerHTML = response.responseText;
          return true;
          },
          failure: function ( response, request ) {
               var errDiv = document.getElementById("cvvHelpDiv");
              errDiv.innerHTML = response.responseText;
              return false;
          }
         });
         DialogPanel.show('cvvHelpDialog');

  }

applySVG = function() {
}

/* This function is to be use for setting message type on single non gift payment method. */
    function setTaskOnLoad(availableSinglePaymentMethod) {
        if(availableSinglePaymentMethod){
            setMessageType('update');
        }else{
            setPaymentBookArray();
            setMessageType('None');
        }
     }
  /* This function is to be use for populating CVV no. field for selected
     credit card on payment book.
  */
    function insertCVVNoFieldOnPaymentForCreditCard(valIndex)
    {
        var paymentType =paymentBookArrayName[valIndex].paymentType;
        var paymentTypeGroup =  typeGroupArray[paymentType]
        var row = (document.getElementById('PaymentBookTable')).rows;
        if(paymentTypeGroup == 'CREDIT_CARD'){
            if(cvvStringFromForm ==""){
              cvvStringFromForm = document.paymentCommonFormLocalizedData.localizedCVVLabel.value
            }

            hideVisibilityOfDiv("creditCardEdit");
            if(document.getElementById("creditCardEdit2"))
              hideVisibilityOfDiv("creditCardEdit2");

            var indexOfCvvNo = row[valIndex].cells.length -1;
            var cvvLinkTabIndex = Ext.get('paymentBookCvvLinkTabIndex').getValue(false);
            var cvvTabIndex = Ext.get('paymentBookCvvTabIndex').getValue(false);
            row[valIndex].cells[indexOfCvvNo].innerHTML = '<a id="cvvLinkOnPB" tabindex="'+cvvLinkTabIndex+'" href="javascript:showCVVHelp(this.id)">'+cvvStringFromForm+'</a><br><input type="text" id="paymentBookCvvNo" size="3" name="paymentBookCvvNo" value="" tabindex="'+cvvTabIndex+'"/>';
            removeCVVNoField();
            document.getElementById('oldIndex').value = valIndex;
         }else if(paymentTypeGroup=='CUSTOMER_ACCOUNT'){
            removeCVVNoField();
            document.getElementById('oldIndex').value = '';
         }
    }

   /*
     Function use to remove CVV no. text box from Payment book.
   */
   function removeCVVNoField() {
      var oldIndex = document.getElementById('oldIndex').value;
      if(oldIndex != '' ){
          var row = (document.getElementById('PaymentBookTable')).rows;
          var indexOfCvvNo = row[oldIndex].cells.length -1;
          row[oldIndex].cells[indexOfCvvNo].innerHTML = '';
      }
   }

   function setInputFieldFromPaymentArray(formName, fieldName, paymentFieldValue)
   {
       eval("document." + formName + "." + fieldName + ".value =paymentFieldValue");
   }

    /* Function use to copy payment method from payment book(payment method available on
       customer profile). */

  function copyPaymentBookPaymentMethodToOrder(index,paymentArrayName,formName)
  {
       var payment = paymentArrayName[index];
       var paymentTypeGroup =  typeGroupArray[payment.paymentType];

       setInputFieldFromPaymentArray(formName, "paymentType",payment.paymentType);
       setInputFieldFromPaymentArray(formName, "customerPaymentKey", payment.customerPaymentKey);
       if(paymentTypeGroup == 'CREDIT_CARD'){
         setInputFieldFromPaymentArray(formName, "creditCardType",payment.creditCardType);
         setInputFieldFromPaymentArray(formName, "displayCreditCardNo", payment.displayCreditCardNo);
       }else if(paymentTypeGroup == 'CUSTOMER_ACCOUNT'){
          setInputFieldFromPaymentArray(formName, "displayCustomerAccountNo", payment.displayCustomerAccountNo);
       }


  }
   /*
     Function use to call remove payment method from an order.
   */
    var removePayment = function (arrInput) {
        document.paymentCommonForm.messageType.value="RemovePayment";
        document.paymentCommonForm.paymentKey.value = arrInput[0];
        document.paymentCommonForm.suspendPayment.value = arrInput[1];
        document.paymentCommonForm.submit();
        DialogPanel.show('deletePaymentDialog');
   }

   /*
     Function use to call suspend/unsupend payment method from an order.
   */

   function suspendOrUnspendPaymentMethod(paymentKey,suspendPaymentFlag){
        document.paymentCommonForm.messageType.value="SuspendOrUnsuspendPayment";
        document.paymentCommonForm.paymentKey.value = paymentKey;
        document.paymentCommonForm.suspendPayment.value = suspendPaymentFlag;
        document.paymentCommonForm.submit();
   }

   /* Function use for removing payment method from an order. */
   function confirmDeletePayment(paymentKey,suspendPaymentFlag){
       DialogPanel.show('deletePaymentDialog');
       Ext.get('Confirm_Yes').focus(focusDelay);
       var paramArray = [paymentKey,suspendPaymentFlag];
       var divId= document.getElementById('deletePaymentDialog');
       var elm = divId.getElementsByTagName("div");
       var elms = divId.getElementsByTagName("*");
       for(var i = 0, maxI = elms.length; i < maxI; ++i) {
           var elm = elms[i];
           if(elm.type)
           {
                if(elm.name == "yes"){
                    elm.onclick= function(){
                    removePayment(paramArray);
                    };
             }
           }
      }
    }

    /*
      Fucntioo use to set the select index value on payment order book
      Selected payment would be copied on an order.
    */
   function setSelectedPaymentIndexOnPB(indexVal,boolInsertCvvNoField){
     document.getElementById("SelectedPaymentIndexOnPB").value = indexVal;
     if(boolInsertCvvNoField){
         insertCVVNoFieldOnPaymentForCreditCard(indexVal);
     }
     setMessageType('AddPaymentFromPB');
     hideVisibilityOfDiv("creditCardEdit");
     svg_classhandlers_decoratePage();
   }

    /*  function use to call update payment method flow.  */
    function updatePaymentMethod(){
       saveOrUpdatePayment(true);
    }

    function setMessageType(mType){
       document.getElementById("messageType").value = mType;
    }

   /*  Function use to save or update payment method on an order. */
    function saveOrUpdatePayment(useMorePayment)
    {
      var validationResult = true;
       var isCCNumberEntered = true;
       var task = document.paymentMethodForm.messageType.value;
       var paymentApplied = document.paymentMethodForm.paymentApplied.value;
       var pmtType = document.paymentMethodForm.paymentType.value;
       var paymentTypeGroup = typeGroupArray[pmtType];
       if(task=='AddGiftPayment'){
         document.paymentMethodForm.messageType.value = 'None';
         task='None';
       }
       if((task == 'None' && useMorePayment)) {
           return;
       }else if((task == 'None' && paymentApplied == 'true')) {
           document.paymentMethodForm.action =saveOrderPaymentAction;
           document.paymentMethodForm.submit();
           return;
       }else if (task == 'AddPaymentFromPB') {
           var index = document.getElementById("SelectedPaymentIndexOnPB").value;
           document.paymentMethodForm.paymentOnPB.value = true;
           var paymentType = "";
           paymentType  = paymentBookArrayName[index].paymentType;
           document.paymentMethodForm.paymentType.value = paymentType;
           paymentTypeGroup = typeGroupArray[paymentType];
           if(paymentTypeGroup == 'CREDIT_CARD'){
               var cvvOnPB = document.getElementById('paymentBookCvvNo').value;
               if(cvvOnPB != ""){
                    document.paymentMethodForm.creditCVVNo.value = cvvOnPB
               }
           }
           copyPaymentBookPaymentMethodToOrder(index, paymentBookArrayName, 'paymentMethodForm');
           document.paymentMethodForm.messageType.value = 'AddOrEditPayment';
        }
        document.paymentMethodForm.action =saveOrderPaymentAction;
        document.paymentMethodForm.useMorePayment.value =  useMorePayment;
        if(useMorePayment){
             document.paymentMethodForm.paymentApplied.value =  false;
        }
        if(task != "update"){
            document.paymentMethodAddOrEditForm.action =saveOrderPaymentAction;
            if(paymentTypeGroup == 'CREDIT_CARD'){
                document.paymentMethodAddOrEditForm.paymentOnPB.value = document.paymentMethodForm.paymentOnPB.value;
                document.paymentMethodAddOrEditForm.paymentType.value = pmtType;
                if(task == 'AddPaymentFromPB'){
                    document.paymentMethodAddOrEditForm.paymentType.value = document.paymentMethodForm.paymentType.value;
                }
                var isPaymentOnPB = document.paymentMethodForm.paymentOnPB.value;
                var isOldPayment = document.paymentMethodForm.isOldPaymentEdit.value;
                if(isOldPayment == "true"){
                    document.paymentMethodAddOrEditForm.paymentKey.value = document.paymentMethodForm.paymentKey.value;
                    document.paymentMethodAddOrEditForm.displayCreditCardNo.value = document.paymentMethodForm.displayCreditCardNo.value;
                }
                if(isPaymentOnPB == "false" && isOldPayment == "false"){
                    if(document.paymentMethodAddOrEditForm.tokenizationRqd.value == 'false'){
                        document.paymentMethodForm.creditCardToken.value = document.paymentMethodAddOrEditForm.creditCardToken.value;
                    }
                }
                validationResult = swc_validateForm("paymentMethodAddOrEditForm");
            }
        }
        else{
            document.paymentMethodForm.action =saveOrderPaymentAction;
            validationResult = swc_validateForm("paymentMethodForm");
        }
        var isCCNumberEntered = true;
        if(task != "update"){
            var isPaymentOnPB = document.paymentMethodForm.paymentOnPB.value;
            var isOldPayment = document.paymentMethodForm.isOldPaymentEdit.value;
            if(isPaymentOnPB == "false" && isOldPayment == "false"){
                if(paymentTypeGroup == 'CREDIT_CARD'){
                    if(document.paymentMethodAddOrEditForm.tokenizationRqd.value == 'true'){
                        isCCNumberEntered = isCardNumberEntered('ccTokenGen', "ccTokenizeFaildiv");
                    }
                }
            }
        }

        if(validationResult && isCCNumberEntered == true){
            if(task == "update"){
                document.paymentMethodForm.submit();
                return;
            }
            var isPaymentOnPB = document.paymentMethodForm.paymentOnPB.value;
            var isOldPayment = document.paymentMethodForm.isOldPaymentEdit.value;
            if(isPaymentOnPB == "false" && isOldPayment == "false"){
                if(paymentTypeGroup == 'CREDIT_CARD'){
                    if(document.paymentMethodAddOrEditForm.tokenizationRqd.value == 'true'){
                        getTokenForCC("ccTokenGen", "saveAddOrEditPaymentAfterTokenization()", pmtType);
                    }
                    else{
                        saveAddOrEditPaymentAfterTokenization();
                    }
                }
            }
            else if(isPaymentOnPB == "false" && isOldPayment == "true")
                saveAddOrEditPaymentAfterTokenization();
            else
                document.paymentMethodForm.submit();
        }

     }

    function saveAddOrEditPaymentAfterTokenization(){
        var sourceForm = document.paymentMethodAddOrEditForm;
        var destinationForm = document.paymentMethodForm;
        var isOldPayment = document.paymentMethodForm.isOldPaymentEdit.value;
        var pmtType = document.paymentMethodForm.paymentType.value;
        var paymentTypeGroup = typeGroupArray[pmtType];
        if(paymentTypeGroup == 'CREDIT_CARD'){
            if(isOldPayment == "false"){
                destinationForm.creditCardType.value = sourceForm.creditCardType.value;
                if(document.paymentMethodAddOrEditForm.tokenizationRqd.value == 'true'){
                    if(handlePostTokenization("ccTokenGen", "paymentMethodForm", "creditCardToken", "creditCardDisplayValue", "ccTokenizeFaildiv")){
                        destinationForm.creditCardType.value = sourceForm.creditCardType.value;
                        destinationForm.creditCardExpMonth.value = sourceForm.creditCardExpMonth.value;
                        destinationForm.creditCardExpYear.value = sourceForm.creditCardExpYear.value;
                        destinationForm.creditCVVNo.value = sourceForm.creditCVVNo.value;
                        destinationForm.creditCardName.value = sourceForm.creditCardName.value;
                        destinationForm.paymentReference1.value = sourceForm.paymentReference1.value;
                        copyCreditCardBillToAddress(sourceForm,destinationForm);
                        if(document.paymentMethodAddOrEditForm.tokenizationRqd.value == 'true'){
                            destinationForm.messageType.value = 'AddOrEditPayment';
                            destinationForm.action = saveOrderPaymentAction;
                            destinationForm.isTokenized.value = true;
                            if(swc_validateForm("paymentMethodForm")){
                                svg_classhandlers_decoratePage();
                            }
                            else{
                                destinationForm.isTokenized.value = false;
                                svg_classhandlers_decoratePage();
                                return;
                            }
                        }
                    }
                    else{
                        svg_classhandlers_decoratePage();
                        return;
                    }
                }
            }
            else{
                destinationForm.creditCardToken.value = sourceForm.creditCardToken.value;
                destinationForm.creditCardDisplayValue.value = sourceForm.creditCardDisplayValue.value;
            }
        }
        destinationForm.creditCardType.value = sourceForm.creditCardType.value;
        destinationForm.creditCardExpMonth.value = sourceForm.creditCardExpMonth.value;
        destinationForm.creditCardExpYear.value = sourceForm.creditCardExpYear.value;
        destinationForm.creditCVVNo.value = sourceForm.creditCVVNo.value;
        destinationForm.creditCardName.value = sourceForm.creditCardName.value;
        destinationForm.paymentReference1.value = sourceForm.paymentReference1.value;
        copyCreditCardBillToAddress(sourceForm,destinationForm);
        excutePaymentForAddOrUpdate();
        svg_classhandlers_decoratePage();
    }

   /*   Function use to update max charge limit of other payment method when other payment method is edited.  */
    function updateMaxChargeLimit(value) {
       document.paymentMethodForm.maxChargeLimit.value = value;
    }

    function finishPayment(){
       document.paymentMethodForm.paymentApplied.value = true;
    }

    /* This function toggle the view on single non gift payment flow.  */
    function showSinglePaymentMethod(task,pmtType){
        var paymentTypeGroup =  typeGroupArray[pmtType];
        hideVisibilityOfDiv("creditCardEdit");
        if(document.getElementById("creditCardEdit2"))
            hideVisibilityOfDiv("creditCardEdit2");
        setMessageType('AddOrEditPayment');
        document.paymentMethodForm.paymentOnPB.value = false;
        if(task == 'None'){
            removeCVVNoField();
            document.getElementById('oldIndex').value = '';
            setMessageType(task);
            applySVG();
            return;
        }else  if(task == 'New'){
            if(pmtType == null ){
               pmtType = document.getElementById("paymentTypeNew").value;
               paymentTypeGroup =  typeGroupArray[pmtType];
               document.getElementById('oldIndex').value = '';
            }
        }else if(task == "Edit")
        {
            hideVisibilityOfDiv("paymentMethodOnOrder");
            if(paymentTypeGroup == "CREDIT_CARD"){
                var cvvOnPaymentList = document.getElementById('creditCardCvvNo').value;
                if(cvvOnPaymentList != ""){
                    document.paymentMethodForm.creditCVVNo.value=cvvOnPaymentList;
                 }
            }
         }
         if(pmtType != null){
             document.paymentMethodForm.paymentType.value=pmtType;

             if(paymentTypeGroup == "CREDIT_CARD"){
                   showVisibilityOfDiv("creditCardEdit");
                 if(document.getElementById("creditCardEdit2"))
                     showVisibilityOfDiv("creditCardEdit2");
             } else if(paymentTypeGroup=="OTHER"){
             }
         }
         //applySVG();
         svg_classhandlers_decoratePage();
     }


    /*     This function is to be use for adding gift payment method on an order.  */
    function addGiftPayment(orderHeaderKey,svcNoIndex, paymentType,chargeSeqence,maxChargeLimit){
     var paymentTypeGroup =  typeGroupArray[paymentType];
     var indexedField = "svcNo_"+svcNoIndex;
     if(document.paymentMethodFormGift.tokenizationRqd.value == 'true'){
         var frameId = "svcTokenGen_"+svcNoIndex;
     }

     if(paymentTypeGroup == "STORED_VALUE_CARD"){
        document.paymentMethodFormGift.messageType.value = 'AddGiftPayment';
        document.paymentMethodFormGift.giftPaymentIndex.value = svcNoIndex;
        document.paymentMethodFormGift.giftPaymentPaymentType.value = paymentType;
        document.paymentMethodFormGift.giftPaymentOrderHeaderKey.value = orderHeaderKey;
        document.paymentMethodFormGift.giftPaymentChargeSequence.value = chargeSeqence;
        document.paymentMethodFormGift.giftMaxChargeLimit.value = maxChargeLimit;
        document.paymentMethodFormGift.action =saveOrderPaymentAction;
        var validationResult = swc_validateForm("paymentMethodFormGift");
        if(validationResult && document.paymentMethodFormGift.tokenizationRqd.value == 'true'){
            getTokenForCC(frameId, "addNewGiftPaymentAfterTokenization()", paymentType);
        }
        else if(validationResult && document.paymentMethodFormGift.tokenizationRqd.value == 'false'){
            addNewGiftPaymentAfterTokenization();
        }
     }
   }


    /*     This function is to be use for adding gift payment method on an order.  */
    function addNewGiftPaymentAfterTokenization(){
        var svcNoIndex = document.paymentMethodFormGift.giftPaymentIndex.value;
        if(document.paymentMethodFormGift.tokenizationRqd.value == 'true'){
            var frameId = "svcTokenGen_"+svcNoIndex;
            var errorDivId = "ccTokenizeFaildiv_"+svcNoIndex;
            var postTokenizationSuccss = handlePostTokenization(frameId, "paymentMethodFormGift", "cardToken", "cardDisplayValue", errorDivId);
        }
        else{
            eval("document.paymentMethodFormGift.cardToken.value = document.paymentMethodFormGift.svcNo_"+svcNoIndex+".value");
        }

        if(document.paymentMethodFormGift.tokenizationRqd.value == 'false' || postTokenizationSuccss == true){
            var validationResult = true;
            if(document.paymentMethodFormGift.tokenizationRqd.value == 'true'){
                document.paymentMethodFormGift.isTokenized.value = true;
                validationResult = swc_validateForm("paymentMethodFormGift");//duplicate payment method check
            }
            if(validationResult){
                var paymentType = document.paymentMethodFormGift.giftPaymentPaymentType.value;
                var orderHeaderKey = document.paymentMethodFormGift.giftPaymentOrderHeaderKey.value;
                  Ext.Ajax.request({
                    url :document.getElementById('VerifyStoreValueCardPayment').href ,
                    params: { 'orderHeaderKey': orderHeaderKey, 'paymentType':paymentType,'svcNo':document.paymentMethodFormGift.cardToken.value},
                    method: 'POST',
                     // if we get a successful response, parse the data then render the panel
                    success: function ( response, request ) {
                    var theDiv = document.getElementById("verifyStoreValueCardResult");
                    if(theDiv)
                        theDiv.innerHTML = response.responseText;
                    if(document.getElementById("resultVerfifySVC")){
                        var resp =  document.getElementById("resultVerfifySVC").value;
                        if ("PASS" == resp){
                            document.paymentMethodFormGift.action = saveOrderPaymentAction;
                            document.paymentMethodFormGift.submit();
                            return true;
                        } else
                            if ("UE_IMPLEMENTION_NOT_FOUND" == resp || "ZERO_VALUE_CARD" == resp || "UE_EXCEPTION" == resp){
                            DialogPanel.show('storeValueCardPaymentErrorDialog');
                            Ext.get('Ok_Button').focus(focusDelay);
                            return false;
                        }
                        else{
                            return false;
                        }
                     }
                    return true;
                   },
                   failure: function ( response, request ) {
                      var theDiv = document.getElementById("verifyStoreValueCardResult");
                      theDiv.innerHTML = response.responseText;
                       return false;
                   }
                });
            }
            else{
                document.paymentMethodFormGift.isTokenized.value = false;
            }
        }
     }

    /* Callback function to be called from add new payment method flow. */
   var showAddNewPayment = function (paymentType) {
      var paymentTypeGroup =  typeGroupArray[paymentType];
       if(paymentTypeGroup == 'CREDIT_CARD'){
           hideVisibilityOfDiv("creditCardEdit");
           hideVisibilityOfDiv("creditCardEdit2");
       } else if(paymentTypeGroup == 'CUSTOMER_ACCOUNT'){
           document.paymentMethodForm.messageType.value = 'AddPaymentFromPB';
       }else if(paymentTypeGroup =='OTHER'){
       }
       DialogPanel.show('paymentDialog');
       svg_classhandlers_decoratePage();
       setFocus(paymentType,true);
  }
  /* Callback function to be called from edit payment method flow. */
   var showEditPayment = function(paymentType){
      var paymentTypeGroup =  typeGroupArray[paymentType];
       if(paymentTypeGroup == 'CREDIT_CARD'){
           var cvvOnPaymentList = document.getElementById(document.paymentMethodForm.paymentKey.value).value;
           if(cvvOnPaymentList != ""){
               document.paymentMethodForm.creditCVVNo.value = cvvOnPaymentList;
           }
           showVisibilityOfDiv("creditCardEdit");
           showVisibilityOfDiv("creditCardEdit2");
           } else if(paymentTypeGroup =='OTHER'){
           }
           document.paymentMethodForm.messageType.value = 'AddOrEditPayment';
           DialogPanel.show('paymentDialog');
           svg_classhandlers_decoratePage();
           setFocus(paymentType,false);
    }

   /* This function is to be use for opening light box flow for add or edit of payment method. */
    function lightBox(callBackMethod,orderHeaderKey,paymentKey,paymentType,isEdit) {
     var paymentTypeGroup =  typeGroupArray[paymentType];
        Ext.Ajax.request({
            url : document.getElementById('GetPaymentMethodOnOrderURL').href,
            params: { orderHeaderKey: orderHeaderKey, paymentKey: paymentKey,paymentType:paymentType,paymentMethodEditable:isEdit,paymentBookForSingleNonGiftPayment:'false'},
            method: 'POST',
            scripts: true,
            success: function(response, options){
                 var theDiv = document.getElementById('Get-Payment-Body-For-AddOr-Edit');
                 theDiv.innerHTML = response.responseText;

                 if(!isEdit){
                  if(paymentTypeGroup=='CREDIT_CARD'){
                      getPaymentBook('PaymentBookCreditCard',document.getElementById('paymentBookUrl').href,paymentType);
                  }else if( paymentTypeGroup=='CUSTOMER_ACCOUNT'){
                      getPaymentBook('PaymentBookCustomerAccount',document.getElementById('paymentBookUrl').href,paymentType);
                  }

                 }
                if(paymentTypeGroup=='CREDIT_CARD'){
                     getAddressBook(paymentType,'AddressBookForAddOrEditCreditCardAddress',document.getElementById('addressBookURL').href,isEdit)
                 }

                 callBackMethod(paymentType);
                 return true;
             },
             failure: function ( response, request ) {
                var theDiv = document.getElementById('Get-Payment-Body-For-AddOr-Edit');
                theDiv.innerHTML = response.responseText;
                DialogPanel.show('paymentDialog');
                svg_classhandlers_decoratePage();
                return false;
             }
          });
      }

    /* This function is to be use for adding new payment method to an order for multi payment flow.*/
    function addPaymentMethod(paymentType,maxChargeLimit,orderHeaderKey){
        document.paymentMethodForm.messageType.value = 'update';
        var validationResult = swc_validateForm("paymentMethodForm");
        if(validationResult){
          document.paymentMethodForm.paymentKey.value = "";
          document.paymentMethodForm.paymentType.value = paymentType;
          document.paymentMethodForm.maxChargeLimit.value = maxChargeLimit;
          document.paymentMethodForm.messageType.value = 'AddOrEditPayment';
          var paymentKey = '';
          lightBox(showAddNewPayment,orderHeaderKey,paymentKey,paymentType,false);
       }

    }

  /*  This function is to be use for edit existing payment method on an order for multi payment flow. */
    function editPaymentMethod(paymentType,orderHeaderKey,paymentKey)
    {
        document.paymentMethodForm.messageType.value = 'update';
        var validationResult = swc_validateForm("paymentMethodForm");
        if(validationResult){
            document.paymentMethodForm.paymentType.value = paymentType;
            document.paymentMethodForm.paymentKey.value = paymentKey;
            lightBox(showEditPayment,orderHeaderKey,paymentKey,paymentType,true);
        }
    }

    function saveOrUpdatePaymentMethod(validateURL){
      var paymentType = document.paymentMethodAddOrEditForm.paymentType.value;
      var paymentTypeGroup =  typeGroupArray[paymentType];
      var formName = 'paymentMethodAddOrEditForm';
      if(paymentTypeGroup == 'CREDIT_CARD'){
         var task = document.getElementById("messageType").value;
         if (task == 'AddPaymentFromPB'){
             var index = document.getElementById("SelectedPaymentIndexOnPB").value;
             document.paymentMethodAddOrEditForm.paymentOnPB.value = true;
             var cvvOnPB = document.getElementById('paymentBookCvvNo').value;
             if(cvvOnPB != ""){
                     eval("document.paymentMethodAddOrEditForm.creditCVVNo.value="+cvvOnPB);
             }
             copyPaymentBookPaymentMethodToOrder(index, paymentBookArrayName, formName);
             addOrUpdatePaymentMethod();
             }else{
                 document.paymentMethodAddOrEditForm.paymentOnPB.value = false;
                 validateAddressForPayment(document.paymentMethodAddOrEditForm,'',validateURL,addOrUpdatePaymentMethod);
             }


      }else if(paymentTypeGroup == 'CUSTOMER_ACCOUNT'){
           var customerAccountOnPB = document.getElementById('customerAccountOnPB');
           var index = customerAccountOnPB.options[customerAccountOnPB.selectedIndex].value;
           copyPaymentBookPaymentMethodToOrder(index, paymentBookArrayName, formName);
           addOrUpdatePaymentMethod();
      }
    }

     function addOrUpdatePaymentMethod(){
       var paymentType = document.paymentMethodAddOrEditForm.paymentType.value;
       var paymentTypeGroup =  typeGroupArray[paymentType];
       if(paymentTypeGroup == 'CREDIT_CARD'){
           var newPayment = document.paymentMethodAddOrEditForm.newPayment.value;
           if(newPayment=='true'){
               if(document.paymentMethodAddOrEditForm.tokenizationRqd.value == 'false'){
                   document.paymentMethodForm.creditCardToken.value = document.paymentMethodAddOrEditForm.creditCardToken.value
               }
           }
       }

       var validationResult = swc_validateForm("paymentMethodAddOrEditForm");
       var editLink = isEditAddressLinkClicked('paymentMethodAddOrEditForm');
       if(!validationResult && editLink =='false' && paymentTypeGroup == 'CREDIT_CARD'){
           toggleVisibility('editableBillToAddressDiv');
       }
       var isCCNumberEntered = true;
       if(typeGroupArray[document.paymentMethodAddOrEditForm.paymentType.value] == "CREDIT_CARD"){
           if(document.paymentMethodAddOrEditForm.newPayment.value == 'true'  && document.paymentMethodAddOrEditForm.paymentOnPB.value == 'false'){
               if(document.paymentMethodAddOrEditForm.tokenizationRqd.value == 'true'){
                   isCCNumberEntered = isCardNumberEntered('ccTokenGen', "ccTokenizeFaildiv");
               }
           }
       }
       if(validationResult && isCCNumberEntered == true){
           var sourceForm = document.paymentMethodAddOrEditForm;
           var destinationForm = document.paymentMethodForm;
           var paymentType = sourceForm.paymentType.value;
           var paymentTypeGroup =  typeGroupArray[paymentType];
            if(paymentTypeGroup == 'CREDIT_CARD'){
            var newPayment = sourceForm.newPayment.value;
                if(newPayment=='true'){
                var paymentOnPB = sourceForm.paymentOnPB.value;
                 if(paymentOnPB=='true'){
                   destinationForm.paymentOnPB.value = sourceForm.paymentOnPB.value;
                   destinationForm.customerPaymentKey.value = sourceForm.customerPaymentKey.value;
                 }else{
                     if(document.paymentMethodAddOrEditForm.tokenizationRqd.value == 'true'){
                         getTokenForCC("ccTokenGen", "addOrUpdatePaymentMethodAfterTokenization()", paymentType);
                         return;
                     }
                     else{
                         addOrUpdatePaymentMethodAfterTokenization();
                         return;
                     }

                  }
                }
                destinationForm.creditCardExpMonth.value = sourceForm.creditCardExpMonth.value;
                destinationForm.creditCardExpYear.value = sourceForm.creditCardExpYear.value;
                destinationForm.creditCVVNo.value = sourceForm.creditCVVNo.value;
                destinationForm.creditCardName.value = sourceForm.creditCardName.value;
                destinationForm.paymentReference1.value = sourceForm.paymentReference1.value;
                copyCreditCardBillToAddress(sourceForm,destinationForm);
           }else if(paymentTypeGroup == 'CUSTOMER_ACCOUNT'){
                destinationForm.customerPaymentKey.value = sourceForm.customerPaymentKey.value;
            }else if(paymentTypeGroup =='OTHER'){
               destinationForm.paymentReference1.value = sourceForm.paymentReference1.value;
               destinationForm.paymentReference2.value = sourceForm.paymentReference2.value;
               destinationForm.paymentReference3.value = sourceForm.paymentReference3.value;
           }
           excutePaymentForAddOrUpdate();
       }
       svg_classhandlers_decoratePage();
     }

     function addOrUpdatePaymentMethodAfterTokenization(){

         var sourceForm = document.paymentMethodAddOrEditForm;
         var destinationForm = document.paymentMethodForm;
         destinationForm.creditCardType.value = sourceForm.creditCardType.value;
         if(document.paymentMethodAddOrEditForm.tokenizationRqd.value == 'true'){
             var postTokenizationSuccess = handlePostTokenization("ccTokenGen", "paymentMethodForm", "creditCardToken", "creditCardDisplayValue", "ccTokenizeFaildiv");
         }
         if(document.paymentMethodAddOrEditForm.tokenizationRqd.value == 'false' || postTokenizationSuccess == true){
             destinationForm.creditCardType.value = sourceForm.creditCardType.value;
             destinationForm.creditCardExpMonth.value = sourceForm.creditCardExpMonth.value;
             destinationForm.creditCardExpYear.value = sourceForm.creditCardExpYear.value;
             destinationForm.creditCVVNo.value = sourceForm.creditCVVNo.value;
             destinationForm.creditCardName.value = sourceForm.creditCardName.value;
             destinationForm.paymentReference1.value = sourceForm.paymentReference1.value;
             copyCreditCardBillToAddress(sourceForm,destinationForm);
             if(document.paymentMethodAddOrEditForm.tokenizationRqd.value == 'true'){
                 destinationForm.messageType.value = 'AddOrEditPayment';
                 destinationForm.action = saveOrderPaymentAction;
                 destinationForm.isTokenized.value = true;
                 if(swc_validateForm("paymentMethodForm")){
                     excutePaymentForAddOrUpdate();
                     svg_classhandlers_decoratePage();
                     return;
                 }
                 else{
                     destinationForm.isTokenized.value = false;
                     return;
                 }
             }
             excutePaymentForAddOrUpdate();
         }
         svg_classhandlers_decoratePage();
     }

  /* This function use as callback function in case of verify address successful case to add or update credit card payment.
     This function also use to add customer account payment and add credit card payment from payment book.   */
   function excutePaymentForAddOrUpdate(){
       document.paymentMethodForm.messageType.value = 'AddOrEditPayment';
       document.paymentMethodForm.action = saveOrderPaymentAction;
       document.paymentMethodForm.submit();
       var div = document.getElementById('paymentDialog');
       if(div)
           DialogPanel.hide('paymentDialog');
   }

   /* This function is to be use for showing new credit card flow. */
   function showCreditCardNew(){
        removeCVVNoField();
        document.getElementById('oldIndex').value = '';
        document.paymentMethodForm.messageType.value = 'AddOrEditPayment';
        document.paymentMethodForm.paymentOnPB.value = false;
        document.paymentMethodAddOrEditForm.creditCVVNo.value = '';
        showVisibilityOfDiv("creditCardEdit");
        showVisibilityOfDiv("creditCardEdit2");
        svg_classhandlers_decoratePage();
   }

   function multiPaymentContinue(){
       document.paymentMethodForm.messageType.value = 'update';
       document.paymentMethodForm.paymentApplied.value = true;
       document.paymentMethodForm.action =saveOrderPaymentAction;
       var validationResult = swc_validateForm("paymentMethodForm");
       if(validationResult){
         document.paymentMethodForm.submit();
       }
   }

   function continueCredtCardAddress(validateURL){
       validateAddressForPayment(document.paymentMethodAddressForm,'',validateURL,closeAddressDiaglogPanel)
   }
   closeAddressDiaglogPanel =  function (){
       var validationResult = swc_validateForm("paymentMethodAddressForm");
       var editLink = isEditAddressLinkClicked('paymentMethodAddressForm')
       if(!validationResult && editLink == 'false'){
           toggleVisibility('editableBillToAddressDiv');
       }
       else if(validationResult){
          copyCreditCardBillToAddress(document.paymentMethodAddressForm,document.paymentMethodForm);
          copyCreditCardBillToAddress(document.paymentMethodAddressForm,document.paymentMethodAddOrEditForm);
          DialogPanel.hide('creditCardDialog');
       }
   }
   function getAddressBook(paymentType,bodyId, jspUrl,newPayment) {
        Ext.get(bodyId).load({
            url : jspUrl ,
            method: 'POST',
            scripts: true,
            callback: function(el,success,response){
                 if(success){
                     svg_classhandlers_decoratePage();
                     var z = Ext.DomQuery.selectNode("#ShowMoreAddresses");
                     Ext.EventManager.addListener(z,'click',svg_classhandlers_decoratePage,this,{delay: 250});
                     setFocus(paymentType,newPayment);
                 }else{
                  el.dom.innerHTML = response.responseText;
                  svg_classhandlers_decoratePage();
                 }

            }
        });
    }
    function getPaymentBook(bodyId, jspUrl,paymentType) {
        Ext.get(bodyId).load({
            url : jspUrl ,
            params: {paymentType:paymentType,paymentBookForSingleNonGiftPayment:false},
            method: 'POST',
            scripts: true,
            callback: function(el,success,response){
                 if(success){
                   setPaymentBookArray();
                   svg_classhandlers_decoratePage();
                   setFocus(paymentType,true);
                 }else{
                   el.dom.innerHTML = response.responseText;
                   svg_classhandlers_decoratePage();
                 }

            }
        });
    }

      function isCommercialAddress(formName){
      var radioButton = eval("document."+formName+".IsCommercialAddress");

        for (var i=0; i < radioButton.length; i++)
        {
         var idCommercialAddr = formName+"_IsCommercialAddressY";
         if (radioButton[i].checked  && radioButton[i].id==idCommercialAddr)
         {
            return "Y"
         }
       }
       return "N";
  }

      function copyCreditCardBillToAddress(sourceForm, destinationForm){
            destinationForm.AddressID.value= sourceForm.AddressID.value;
            destinationForm.IsCommercialAddress.value= isCommercialAddress(sourceForm.id);
            destinationForm.Title.value= sourceForm.Title.value;
            destinationForm.FirstName.value= sourceForm.FirstName.value;
            destinationForm.MiddleName.value= sourceForm.MiddleName.value;
            destinationForm.LastName.value= sourceForm.LastName.value;
            destinationForm.Company.value= sourceForm.Company.value;
            destinationForm.AddressLine1.value= sourceForm.AddressLine1.value;
            destinationForm.AddressLine2.value= sourceForm.AddressLine2.value;
            destinationForm.AddressLine3.value= sourceForm.AddressLine3.value;
            destinationForm.AddressLine4.value= sourceForm.AddressLine4.value;
            destinationForm.AddressLine5.value= sourceForm.AddressLine5.value;
            destinationForm.AddressLine6.value= sourceForm.AddressLine6.value;
            destinationForm.City.value= sourceForm.City.value;
            destinationForm.State.value= sourceForm.State.value;
            destinationForm.Country.value= sourceForm.Country.value;
            destinationForm.ZipCode.value= sourceForm.ZipCode.value;
            destinationForm.DayPhone.value= sourceForm.DayPhone.value;
            destinationForm.EMailID.value= sourceForm.EMailID.value;
            destinationForm.AlternateEmailID.value= sourceForm.AlternateEmailID.value;
            destinationForm.Beeper.value= sourceForm.Beeper.value;
            destinationForm.DayFaxNo.value= sourceForm.DayFaxNo.value;
            destinationForm.Department.value= sourceForm.Department.value;
            destinationForm.ErrorTxt.value= sourceForm.ErrorTxt.value;
            destinationForm.EveningFaxNo.value= sourceForm.EveningFaxNo.value;
            destinationForm.EveningPhone.value= sourceForm.EveningPhone.value;
            destinationForm.HttpUrl.value= sourceForm.HttpUrl.value;
            destinationForm.IsAddressVerified.value= sourceForm.IsAddressVerified.value;
            destinationForm.JobTitle.value= sourceForm.JobTitle.value;
            destinationForm.Latitude.value= sourceForm.Latitude.value;
            destinationForm.Longitude.value= sourceForm.Longitude.value;
            destinationForm.MobilePhone.value= sourceForm.MobilePhone.value;
            destinationForm.PersonID.value= sourceForm.PersonID.value;
            destinationForm.PreferredShipAddress.value= sourceForm.PreferredShipAddress.value;
            destinationForm.TaxGeoCode.value= sourceForm.TaxGeoCode.value;
            destinationForm.Suffix.value= sourceForm.Suffix.value;
            destinationForm.UseCount.value= sourceForm.UseCount.value;
            destinationForm.VerificationStatus.value= sourceForm.VerificationStatus.value;
            destinationForm.saveToAddressBook.value= sourceForm.saveToAddressBook.value;
            if(sourceForm.saveToAddressBook.checked){
                destinationForm.saveAddressToCustomerAddressBook.value="Y";
            }else{
                destinationForm.saveAddressToCustomerAddressBook.value="N";
            }
   }
   function setUpdateFlag(){
      document.paymentMethodForm.updateFlag.value = true;
   }
   function setEditAddressLinkClicked(formName){
      form = eval("document."+formName);
      form.editAddressLinkClicked.value=true;
   }
   function isEditAddressLinkClicked(formName){
         form = eval("document."+formName);
         return form.editAddressLinkClicked.value;
   }

   function showDialogPanelWithFocus(dialogPanelId,focusId){
       DialogPanel.show(dialogPanelId);
       var focusComp = Ext.get(focusId);
       if(focusComp != null){
         focusComp.focus(focusDelay);
       }
    }
   function setPaymentBookArray(){
      paymentBookArrayName = getPaymentBookArray();
   }
	function hideVisibilityOfDiv(divID)
    {
        var divElement = Ext.get(divID);
        if(divElement != null)
        {
            divElement.replaceClass("visible-data","hidden-data");
        }
    }
    function showVisibilityOfDiv(divID)
    {
        var divElement = Ext.get(divID);
        if(divElement != null)
        {
            divElement.replaceClass("hidden-data","visible-data");        }
    }
    function showEditAddress(formName)
    {
      showVisibilityOfDiv('editableBillToAddressDiv');
      hideVisibilityOfDiv('readOnlyBillToAddressDiv');
      setEditAddressLinkClicked(formName);
    }
