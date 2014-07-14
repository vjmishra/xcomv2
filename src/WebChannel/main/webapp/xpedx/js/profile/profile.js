/* This file contains common java-script methods used across the Profile module
 * 
 */

/**
 * @author sshailaja
 * 
 */

/**
 * Method for expand collapse links in pages in Profile module.
 * 
 */

function slider2(elementId, imgElementId, showImgClass, hideImgClass, shouldDisplay){
    var slideMeElement = Ext.get(elementId);
    var imgElement = Ext.get(imgElementId);
    if(slideMeElement){
        var upArrow=Ext.get(showImgClass);
        var downArrow=Ext.get(hideImgClass);	
        if (!shouldDisplay){
            slideMeElement.slideOut('t', {
                easing: 'easeOut',
                remove: false,
                useDisplay: true,
                callback: function(){
                    svg_classhandlers_decoratePage();
                    imgElement.removeClass(showImgClass);
                    imgElement.addClass(hideImgClass);
                }
            });
        }
        else {
            slideMeElement.slideIn('t', {
                easing: 'easeIn',
                callback: function(){
                    svg_classhandlers_decoratePage()
                    imgElement.removeClass(hideImgClass);
                    imgElement.addClass(showImgClass);
                }
            });
        }
    }
}

/**
 * Method for expand collapse panels called from Ext.onReady in pages in Profile module.
 * 
 */

function slider1(elementId, imgElementId, showImgClass, hideImgClass, isDisplayed){
    // define slide operation
    var slideText = function(elementId, imgElementId, showImgClass, hideImgClass){
        var slideMeElement = Ext.get(elementId);
        var imgElement = Ext.get(imgElementId);
        if(slideMeElement){
            var upArrow=Ext.get(showImgClass);
            var downArrow=Ext.get(hideImgClass);
            if (slideMeElement.isVisible()){
                slideMeElement.slideOut('t', {
                    easing: 'easeOut',
                    remove: false,
                    useDisplay: true,
                    callback: function(){
                        svg_classhandlers_decoratePage();
                        imgElement.removeClass(showImgClass);
                        imgElement.addClass(hideImgClass);
                    }
                });
            }
            else {
                slideMeElement.slideIn('t', {
                    easing: 'easeIn',
                    callback: function(){
                        svg_classhandlers_decoratePage();
                        imgElement.removeClass(hideImgClass);
                        imgElement.addClass(showImgClass);
                    }
                });
            }
        }
    }

    // set up initial style according to the state of the content element
    var imgElement = Ext.get(imgElementId);
    if (isDisplayed==undefined) {
        isDisplayed = upArrow.isDisplayed();
    }
    if(imgElement !=null)
    {
		    if (isDisplayed) {
		        imgElement.removeClass(hideImgClass);
		        imgElement.addClass(showImgClass);
		    }
		    else {
		        imgElement.removeClass(showImgClass);
		        imgElement.addClass(hideImgClass);
		    }
    }

    // Set up event handler
    var anchorElem = "anchor"+imgElementId;
    if(Ext.get(anchorElem) != null)
    Ext.get(anchorElem).on('click',function(e,t){
        slideText(elementId, imgElementId, showImgClass, hideImgClass);
    });
}


/**
 * Opens confirmation message pop-ups for address/payment delete/duplicate in pages in Profile module.
 * 
 */

 function confirmMessageLightBox(bodyId, panelId,actionURL, msgCode, callbackIfOK ,paramArr, revertActionName) {

	   Ext.get(bodyId).load({
	        url :  actionURL,
	       evalScripts: true,
	        method: 'GET',
	        callback:function(e1, success, response){
	            if(success){   
	                populateConfirmJSP(bodyId,msgCode,callbackIfOK,paramArr, revertActionName);
	            }
	            else{
	                el.dom.innerHTML = response.responseText;
	            }
                DialogPanel.show(panelId);
                try{
                svg_classhandlers_decoratePage();
                }catch (err){

                }
	    	}
	    });
}


/**
 * This is a common method to open an address/payment lightbox in pages in Profile module.
 */

function lightBox(bodyId, panelId,actionURL,callBackMethod,elemToFocus) {

	   Ext.Ajax.request({
	        url :  actionURL,
	        method: 'GET',
	        scripts: true,
	        success: function ( response, request ){
    	       theDiv = document.getElementById(bodyId);
               if(theDiv)
                    theDiv.innerHTML = response.responseText;
			   var elemFocus = Ext.get(elemToFocus);
               if(elemFocus)
                    elemFocus.focus.defer(1000, elemFocus);
                DialogPanel.show(panelId);
                try{
                    svg_classhandlers_decoratePage.defer(1200,this);
                    }catch (err){

                    }
                  if(callBackMethod != null && callBackMethod != ""){
	        		eval(callBackMethod);
	        	}
                  
	    	},
	    	failure: function ( response, request ) {
	    	   theDiv = document.getElementById(bodyId);
               if(theDiv){
                    theDiv.innerHTML = response.responseText;
               }
               DialogPanel.show(panelId);
               try{
                   svg_classhandlers_decoratePage.defer(1200,this);
               }catch (err){

               }    
	    	}
	    });

}

/**
 * This is a common method for confirmation message popup. It submits form on confirmation or closes the popup when confirmation denied.
 */

function populateConfirmJSP( bodyId,msg,callbackIfOK, paramArray, revertActionName){
	var divId= document.getElementById(bodyId);

	var elms = divId.getElementsByTagName("*");
      for(var i = 0, maxI = elms.length; i < maxI; ++i) {
        var elm = elms[i];

	     if(elm.id == "confirmMsg"){

	         if(msg && msg != 'undefined')
	         elm.innerHTML= msg;

	     }
         if(elm.type)
         {
            if(elm.name == "yes"){
               if(!paramArray){
                        paramArray =[];
                   }
	           elm.onclick= function(){
				callbackIfOK(paramArray);
				} ;

            }
            else if(elm.name == "no"){
                var actionNameparam = document.getElementById("actionName");
                if(actionNameparam){
                    document.getElementById("actionName").value = revertActionName;
                }
            }
        }
      }
}

/** Validation for check-box selection to check if at least one check-box is selected .
 * 
 */

function validateCheckBox(formName, actionName){
    document.getElementById("actionName").value = actionName;
    return swc_validateForm(formName);
}


function isAtleastOneChecked(formName,fieldName )
{
  var isChecked = "false";
  var fld = "document."+formName+"."+fieldName;

  if(eval(fld) && eval(fld).value && eval(fld).checked)
  {
    isChecked = "true";
  }
  else{
    var objs = eval(fld);
    for(i=0;i<objs.length; i++){
     if(objs[i].checked)
     {
       isChecked = "true";
       break;
     }
    }
  }
  return isChecked;
}

function disableInputs(formObj, hasAccess){

  if(typeof hasAccessTo != 'undefined' && hasAccessTo != 'TRUE' && formObj ){
  var inputs = formObj.getElementsByTagName("input");
  var dropdowns = formObj.getElementsByTagName("select");

     for(i=0; i<inputs.length; i++){
        inputs[i].disabled= true;
     }
      for(i=0; i<dropdowns.length; i++){
        dropdowns[i].disabled= true;
     }
 }

}
