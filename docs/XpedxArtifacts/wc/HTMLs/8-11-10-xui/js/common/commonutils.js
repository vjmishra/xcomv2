//This file contains common utils, adding page specific utils should be avoided


//This function is used for Image viewer
function loadItemImages(divId,url,productID, UOM)
{
    Ext.Ajax.request({
        url: url,
         params: {
   			itemID: productID,
   	    	unitOfMeasure: UOM
           },
        method: 'GET',
        success: function (response, request){
            var myDiv = document.getElementById(divId);
           
            myDiv.innerHTML = response.responseText;
            //DialogPanel.show('modalDialogPanel2');
            svg_classhandlers_decoratePage();
        },
        failure: function (response, request){
            var myDiv = document.getElementById(divId);
            myDiv.innerHTML = response.responseText;
            //DialogPanel.show('modalDialogPanel2');
            svg_classhandlers_decoratePage();
        }
    });
  }