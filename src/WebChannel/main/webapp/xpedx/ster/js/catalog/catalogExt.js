	Ext.BLANK_IMAGE_URL = "<s:url value='' + XPEDXWCUtils_STATIC_FILE_LOCATION + '/ext/resources/images/default/s.gif'/>";

Ext.onReady(function() {
	
	if( Ext.get("pageSize") != null ){
		Ext.get("pageSize").on("change", function(e, el, o){
	
			var size = document.getElementById("pageSize");
	
			var url = document.getElementById("pageSizeURL").href;
	
			location.href=url + "&pageSize=" + size.value;
	
		}, this, { preventDefault: true } );
	}



	if( Ext.get("sortField") != null){
		Ext.get("sortField").on("change", function(e, el, o){
	
			var field = document.getElementById("sortField");
	
			var url = document.getElementById("sortFieldsURL").href;
	
			location.href=url + "&sortField=" + field.value;
	
		}, this, { preventDefault: true } );
	}

});



/*

 * Functions for add to cart operation

 */

function listAddToCart(url, productID, UOM, quantity, headerKey,returnURL,flowID,currency)

{

	if(flowID == "2" && currency != "")

	{

		Ext.Ajax.request({

	    	// for testing only

	        url: url,

	        params: {

				productID: productID,

		    	productUOM: UOM,

		    	quantity: quantity,

		    	orderHeaderKey: headerKey,

		    	flowID: flowID,

		    	currency:currency

	        },

	        // end testing

	        method: 'GET',

	        success: function (response, request){



	            	document.location.href = returnURL;

	            //var myDiv = document.getElementById("ajax-body-1");

	            //myDiv.innerHTML = response.responseText;

	            //DialogPanel.toggleDialogVisibility('modalDialogPanel1');

	        },

	        failure: function (response, request){

	            var myDiv = document.getElementById("ajax-body-1");

	            myDiv.innerHTML = response.responseText;

	            DialogPanel.show('modalDialogPanel1');

	            svg_classhandlers_decoratePage();

	        }

	    });

    }

    else

    {

    			Ext.Ajax.request({

	    	// for testing only

	        url: url,

	        params: {

				productID: productID,

		    	productUOM: UOM,

		    	quantity: quantity,

		    	orderHeaderKey: headerKey



	        },

	        // end testing

	        method: 'GET',

	        success: function (response, request){

            refreshMiniCartLink();

            showMiniCartWindow();

	

	            //var myDiv = document.getElementById("ajax-body-1");

	            //myDiv.innerHTML = response.responseText;

	            //DialogPanel.toggleDialogVisibility('modalDialogPanel1');

	        },

	        failure: function (response, request){

	            var myDiv = document.getElementById("ajax-body-1");

	            myDiv.innerHTML = response.responseText;

	            DialogPanel.show('modalDialogPanel1');

	            svg_classhandlers_decoratePage();

	        }

	    });

    }

}



/*

 * Show lightbox for superseding product

 */

function showSupersede(url)

{

	 Ext.Ajax.request({

    	// for testing only

        url: url,

        // end testing

        method: 'GET',

        success: function (response, request){

            var myDiv = document.getElementById("ajax-body-2");

            myDiv.innerHTML = response.responseText;

            DialogPanel.show('modalDialogPanel2');

            svg_classhandlers_decoratePage();

            var el = Ext.get('detailURL1');

            el.focus.defer(100, el);

        },

        failure: function (response, request){

			var myDiv = document.getElementById("ajax-body-2");

            myDiv.innerHTML = response.responseText;

            DialogPanel.show('modalDialogPanel2');

            svg_classhandlers_decoratePage();

        }

    });

}



/**********************************************************

 Functions from itemdetails.js for use in itemQuickView

 *********************************************************/

function openAvailabilityLightBox(actionURL) {

	var isFromValid = swc_validateForm("productDetailForm");

	if(isFromValid){

		if(document.productDetailForm.qtyBox === undefined)

		{

			requiredQty = 1;

		}

		else

		{

			requiredQty = document.productDetailForm.qtyBox.value;

		}

		actionURL = actionURL+'&requiredQty='+requiredQty;

		Ext.get('ajax-prodAvailability').load({

			 url :  actionURL,

			 method: 'GET',

			 callback:function(el, success, response){

				if(success)

				{

					DialogPanel.show('product_availability');

					svg_classhandlers_decoratePage();

				}

				else

				{

					// el.dom.innerHTML = response.responseText;

					 

					var myDiv = document.getElementById("ajax-prodAvailability");

					myDiv.innerHTML = response.responseText;

					DialogPanel.show('product_availability');

				}

			 }

		 });

	}

  }



function openProdInfoWindow(url)

{

       window.open(url,"_blank","toolbar=no, location=yes, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width=800, height=600");

}



function addToCart(url, productID, UOM,headerKey, returnURL,quantity, validateFlag,flowID,currency)

{

    var isFromValid = true;

    if(validateFlag)

        isFromValid = swc_validateForm("productDetailForm");

if(isFromValid){



	if(flowID == "2" && currency != "")

		{

		    Ext.Ajax.request({

		        // for testing only

		        url: url,

		        params: {

		            productID: productID,

		            productUOM: UOM,

		            quantity: quantity,

		            orderHeaderKey: headerKey,

		            flowID: flowID,

			    	currency:currency

		        },

		        // end testing

		        method: 'GET',

		        success: function (response, request){



		            document.location.href = returnURL;

		            //var myDiv = document.getElementById("ajax-body-1");

		            //myDiv.innerHTML = response.responseText;

		            //DialogPanel.toggleDialogVisibility('modalDialogPanel1');

		        },

		        failure: function (response, request){

		            var myDiv = document.getElementById("ajax-body-1");

		            myDiv.innerHTML = response.responseText;

		            DialogPanel.show('modalDialogPanel1');

		            svg_classhandlers_decoratePage();

		        }

		    });

    	}

    	else

    	{

    			Ext.Ajax.request({

		        // for testing only

		        url: url,

		        params: {

		            productID: productID,

		            productUOM: UOM,

		            quantity: quantity,

		            orderHeaderKey: headerKey

		        },

		        // end testing

		        method: 'GET',

		        success: function (response, request){

		            refreshMiniCartLink();

	            	showMiniCartWindow();  

		            //var myDiv = document.getElementById("ajax-body-1");

		            //myDiv.innerHTML = response.responseText;

		            //DialogPanel.toggleDialogVisibility('modalDialogPanel1');

		        },

		        failure: function (response, request){

		            var myDiv = document.getElementById("ajax-body-1");

		            myDiv.innerHTML = response.responseText;

		            DialogPanel.show('modalDialogPanel1');

		            svg_classhandlers_decoratePage();

		        }

		    });

    			

    	}

    }

}



function itemDetailAddToCart(productID, UOM, headerKey, returnURL,flowID,currency)

{

    var myDiv = document.getElementById("ajax-body-1");

    var url = document.getElementById("addToCartURL").value;

    var qty = document.getElementsByName("qtyBox")[0];

    addToCart(url, productID, UOM,headerKey,returnURL, qty.value, true,flowID,currency);

}



/*

 * Availability light box

 */

function listOpenAvailabilityLightBox(actionURL)

{

    Ext.get('ajax-prodAvailability').load({

         url :  actionURL,

         method: 'POST',

         callback:function(el, success, response){

			if(success)

			{

		        DialogPanel.show('product_availability');

		        svg_classhandlers_decoratePage();

			}

			else

			{

	            el.dom.innerHTML = response.responseText;

				DialogPanel.show('product_availability');

		        svg_classhandlers_decoratePage();

			}

         }

     });

}



