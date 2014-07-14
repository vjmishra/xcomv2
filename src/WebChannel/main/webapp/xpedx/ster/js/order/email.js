var focusDelay = 300;
    function emailLightBox(actionURL,dialogPanelId) {
         if(dialogPanelId == null){
             dialogPanelId = "emailDialogPanel";
         }
           Ext.get('ajax-body-1').load({
                url :  actionURL,
                method: 'POST',
                callback: function(el,success,response){
                    svg_classhandlers_decoratePage();
                    DialogPanel.show(dialogPanelId);
                    var focusElement = Ext.get('toAddress');
                    if(focusElement != null){
                     focusElement.focus(focusDelay);
                    }
                    if(!success){
                       var theDiv = document.getElementById("ajax-body-1");
                       theDiv.innerHTML = response.responseText;
                       DialogPanel.show(dialogPanelId);
                    }

                }
            });
    }

function sendEmail(emailAction) {
  var validationResult = swc_validateForm("emailForm");
  if(validationResult){
   Ext.Ajax.request({
          url : emailAction,
      form : 'emailForm',
          method: 'post',
          // if we get a successful response, parse the data then render the panel
          success: function ( response, request ) {
              var theDiv = document.getElementById("divEmail");
              theDiv.innerHTML = response.responseText;
              svg_classhandlers_decoratePage();
              var focusElement = Ext.get('closeLink');
              if(focusElement != null){
                 focusElement.focus(focusDelay);
               }
          return true;
          },
          failure: function ( response, request ) {
              var theDiv = document.getElementById("divEmail");
              theDiv.innerHTML = response.responseText;
              return false;
          }
      });
  }
}

   function download(actionName)
    {
        // Download Cart window has no actions out of it so we don't need a handle to close it
        var windowReference = window.open("", "ExportWindow", "directories=no,toolbar=no,localtion=no,menubar=no,scrollbars=yes,resizable=yes,width=1000,height=700");
         document.downloadForm.target = "ExportWindow";
        document.downloadForm.action = actionName;
        document.downloadForm.submit();
        windowReference.print();
        setTimeout('document.downloadForm.target = ""', 1000);
        setTimeout('document.downloadForm.action = ""', 1000);
    }
