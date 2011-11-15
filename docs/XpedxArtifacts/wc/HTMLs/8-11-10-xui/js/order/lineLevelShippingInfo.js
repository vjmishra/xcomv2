/*
 * getOrderLineAddress method gets Order line Ship to address and display in
 * lightbox.
 * orderlinekey: Primary key of the line for which shipTo Address needs to be fetched.
 * stateDropDownURL: URL of the action which populates state drop down list.
 * returnURL: URL of the action used for "Back" and "Continue" button. It is used for single-step check-out flow.
 */
function getOrderLineAddress(actionURL,orderlinekey,stateDropDownURL,isDraftOrder,returnURL)
{

	Ext.get("dynamic-body").load({
	        url :  actionURL,
			params : {
				orderLineKey : orderlinekey,
				returnURL : returnURL,
				draft : isDraftOrder
			},
	        method: 'POST',
	        scripts: true,
	        callback:function(el, success, response){
			   if(!success){
			       el.dom.innerHTML = response.responseText;
			   }
               DialogPanel.show("addressLightBox");
               svg_classhandlers_decoratePage();
			   if(success){
			       showStatesForCountry(document.saveOrderLineShipToAddress,stateDropDownURL);
			   }
	    	}
	    });
}

/*
 * saveAddressInfo method is used for submitting user entered address through lightbox.
 * It validates address before submitting address.
 *
 */
function saveAddressInfo(formObj,actionURL,validateURL,verifyaddrDiv){
    formObj.isAddressDirty.value='true';
    isValid = swc_validateForm(formObj.name);
    isValid &= validateAddress(formObj, actionURL, validateURL, verifyaddrDiv, 'svg_classhandlers_decoratePage()', isValid);
}

/*
 * getAddressBook method gets Address book for an Order line.
 *
 */
function getAddressBook(actionURL,bodyId, panelId,orderLineKey,orderHeaderKey,isDraftOrder,returnURL) {
	   order_line_key = 0;
	   order_line_key = orderLineKey;
			Ext.get(bodyId).load({
				url : actionURL,
				params : {
					orderLineKey : orderLineKey,
					orderHeaderKey : orderHeaderKey,
					returnURL : returnURL,
					draftOrderFalg : isDraftOrder
				},
				method: 'POST',
				scripts: true,
				callback: function(el,success, response){
					 if(success){
							DialogPanel.show(panelId);
							svg_classhandlers_decoratePage();
							var z = Ext.DomQuery.selectNode("#ShowMoreAddresses");
							if(z != null){
								Ext.EventManager.addListener(z,'click',svg_classhandlers_decoratePage,this,{delay: 250});
							}
					 }else{	
							DialogPanel.show(panelId);
							svg_classhandlers_decoratePage();
							el.dom.innerHTML = response.responseText;					
					 }
					 
				}
		   });

}

/*
 * getAddressBook method is used for submitting address select from Address book(on click of "Use this Address") lightbox.
 *
 */
function formSubmit() {
    document.saveOrderLineShipToAddress_1.submit();
}
