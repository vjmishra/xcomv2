 

 /**
  		Product Comparison related java script functions
 **/
  function showDifferences()
  {
		document.prodCompare.showAttributesWithDifferentValuesOnly.value ="Y";
		document.prodCompare.submit();
  }

  function showAll()
  {
		document.prodCompare.showAttributesWithDifferentValuesOnly.value ="N";
		document.prodCompare.submit();
  }

function processCompare(compareURL)
{
	/*var checkBoxes = getFormCheckBoxes();
	var seleced_products = "";
	var selected = "false";
	for( var i = 0; i < checkBoxes.length; i++ )
	{
		if(checkBoxes[i].checked)
		{
			selected = "true";
		}
	}

	if(selected == "false")
	{
		//TODO: Localization/validation
		alert("No product selected for comparison.");
		return;
	}*/
	document.productListForm.action=compareURL;
	document.productListForm.submit();
}

function getFormCheckBoxes()
{
	var form_field;
	var result = new Array();
	if(document.productListForm)
	{
		for( var i = 0; i < document.productListForm.elements.length; i++ )
		{
			form_field = document.productListForm.elements[i];
			if( form_field.type == "checkbox" )
				result[result.length] = form_field;
		}
	}
	return result;
}

 /**
  		End of Product Comparison related java script functions
 **/

Ext.onReady(function() {
	Ext.get("pageSize").on("change", function(e, el, o){
		var size = document.getElementById("pageSize");
		var url = document.getElementById("pageSizeURL").href;
		location.href=url + "&pageSize=" + size.value;
	}, this, { preventDefault: true } );

	Ext.get("sortField").on("change", function(e, el, o){
		var field = document.getElementById("sortField");
		var url = document.getElementById("sortFieldsURL").href;
		location.href=url + "&sortField=" + field.value;
	}, this, { preventDefault: true } );
});

/*
 * Functions for add to cart/order operation
 */
function listAddToCart(url, productID, UOM, quantity, headerKey, returnURL,flowID,currency)
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
			    	flowID:flowID,
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
        },
        failure: function (response, request){
            var myDiv = document.getElementById("ajax-body-2");
            myDiv.innerHTML = response.responseText;
            DialogPanel.show('modalDialogPanel2');
            svg_classhandlers_decoratePage();
        }
    });
}

/*
 * Variables and functions for displaying bubble of extended product description
 */
/*
var hoverWaitTime=1000;
var timer1 = 0;
var bubbleURL=null;
var bubbleDisp=null;
Ext.onReady(function() {
	var imgs = Ext.query('.prodImg');
    for (var i = 0; i < imgs.length; i++) {
    	Ext.get(imgs[i]).on('mouseover',
    	function(){
    		var tmp = this.id.split("_");
    		var idx = tmp[tmp.length-1];
    		bubbleURL = document.getElementById("extDescURL_"+idx).href;
    		bubbleDisp = document.getElementById("extDescDiv_"+idx);
    		//showExtDescription(url, disp);
    		timer1 = setTimeout("showExtDescription();", 1000);
    	});
    	Ext.get(imgs[i]).on('mouseout',function(){
    		hideExtDescription();
    	});
    }
});

function hideExtDescription()
{
	clearTimeout(timer1);

	if(bubbleDisp)
	{
		bubbleDisp.innerHTML = "";
		bubbleDisp.style.display='none';
	}
}

function showExtDescription()
{
	Ext.Ajax.request({
    	// for testing only
        url: bubbleURL,
        // end testing
        method: 'GET',
        success: function (response, request){
			hideExtDescription();
			bubbleDisp.innerHTML = response.responseText;
			bubbleDisp.style.display='inline';
        	SVGAnnotator.mkRoundedBackground(".bubble", {
        		curve:10,
        	    borderWidth:1,
                borderColor: '#ccc',
        	    outsideOpacity:0
        	});
        },
        failure: function (response, request){
            var myDiv = document.getElementById("ajax-body-3");
            myDiv.innerHTML = response.responseText;
            DialogPanel.show('modalDialogPanel3');
            svg_classhandlers_decoratePage();
        }
    });
}
*/

/*
 * End: Variables and functions for displaying bubble of extended product description
 */

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
				DialogPanel.show('product_availability');
                el.dom.innerHTML = response.responseText;
    		}
         }
     });
}

var bubbleURL=null;
var bubbleDisp=null;

Ext.onReady(function(){
	var imgs = Ext.query('.prodImg');
	var qvlTitle = document.getElementById("quickViewLaunchTitle").innerHTML;
    for (var i = 0; i < imgs.length; i++) {
		var tmp = imgs[i].id.split("_");
		var idx = tmp[tmp.length-1];
		bubbleURL = document.getElementById("quickViewURL_"+idx).href;
		qvTitle = document.getElementById("quickViewTitle_"+idx).innerHTML;
		new QuickView(imgs[i],
			{ 
            	url: bubbleURL,
            	callback: svg_classhandlers_decoratePage
			}, 
			{
	            title: qvlTitle,
	            quickViewWindowConf:{
                animateFromAnchor:false,
	                windowConf:{
	            		title: qvTitle,
	                    width: 750,
	                    //height: 400
                        autoHeight: true
	                }
            }
        });
    }
});

/**********************************************************
 Functions from itemdetails.js for use in itemQuickView
 *********************************************************/
function openAvailabilityLightBox(actionURL) {

	var isFromValid = swc_validateForm("productDetailForm");
	if(isFromValid){
		requiredQty = document.productDetailForm.qtyBox.value;
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
					DialogPanel.show('product_availability');
					el.dom.innerHTML = response.responseText;
				}
			 }
		 });
	 }
  }

function openProdInfoWindow(url)
{
	   window.open(url,"_blank","toolbar=no, location=yes, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width=800, height=600");
}

function addToCart(url, productID, UOM,headerKey, returnURL,quantity, validateFlag)
{
	var isFromValid = true;
	if(validateFlag)
		isFromValid = swc_validateForm("productDetailForm");
if(isFromValid){
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
        	if(headerKey == "")
     	{
	            refreshMiniCartLink();	
				showMiniCartWindow();
         }
         else
         {
         	document.location.href = returnURL;
         }
            
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

function itemDetailAddToCart(productID, UOM, headerKey, returnURL)
{
    var myDiv = document.getElementById("ajax-body-1");
    var url = document.getElementById("addToCartURL").value;
    var qty = document.getElementsByName("qtyBox")[0];
    addToCart(url, productID, UOM,headerKey,returnURL, qty.value, true);
}

