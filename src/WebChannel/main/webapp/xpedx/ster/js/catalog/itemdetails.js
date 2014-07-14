Ext.onReady(function() {

	var o = tabify("Content", 300);// Show contents in tabs

	svg_classhandlers_decoratePage();// Apply SVG for rounded corner, gradient...etc.
	if(o)
	{
		// bind SVG function to "showTab" event so each time the tab redraws, the SVG
		// applies
		o.addListener("showTab", svg_classhandlers_decoratePage);
	}
});

var selectionArray = new Array();
var attrArray = new Array();
var completeProductList = new Array();
//var productList = new Array();
var newcompleteProductList = new Array();
var valueProdBitMapArray = new Array();
var idx = 0;
/*
function openPrintItemPage(actionURL)
{
	var windowReference = window.open("", "ExportWindow", "directories=no,toolbar=no,localtion=no,menubar=no,scrollbars=yes,resizable=yes,width=1000,height=700");
         document.productDetailForm.target = "ExportWindow";
        document.productDetailForm.action = actionURL;
        document.productDetailForm.submit();
       // window.print();
       //var t=setTimeout("window.print()",2000);
*/
	/*
    Ext.Ajax.request({
		//url:  actionURL,
		//method : "POST",
		//success : function(response, options){
			// showPrintWindow();
			//eval(response.responseText);
		}
	});


}*/


 function openVariantAvailabilityLightBox(id, uom) {
	   
      var requiredQty = 1;
	  var availabilityURL = document.getElementById("availabilityURL").value;
      availabilityURL = availabilityURL+'&itemID='+id+'&unitOfMeasure='+uom+'&requiredQty='+requiredQty;
      Ext.get('ajax-prodAvailability').load({
           url :  availabilityURL,
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
				     var myDiv = document.getElementById("ajax-prodAvailability");
		             myDiv.innerHTML = response.responseText;
				}
           }
       });
     
 }
	function openPrintItemPage(actionURL,itemID,UOM,value)
	{
		var windowReference = window.open("", "ExportWindow", "directories=no,toolbar=no,localtion=no,menubar=no,scrollbars=yes,resizable=yes,width=1000,height=700");
	         document.printPage.target = "ExportWindow";
	
	        document.printPage.submit();
	       // window.print();
	       //var t=setTimeout("window.print()",2000);
	}
	
	function findItem(attrValue,attrName)
	{
		// reset selection UI
		resetAllOptionUI();
	
		if(selectionArray[attrName] != attrName)
		{
			for (var m in attrArray[selectionArray[attrName]])
			{
				if(attrArray[selectionArray[attrName]].hasOwnProperty(m))
				{
					//Not sure why # being used to read the resource ID
					//image = Ext.DomQuery.selectNode("#"+attrArray[selectionArray[attrName]][m]);
					image = document.getElementById(attrArray[selectionArray[attrName]][m]);
					//setOpacity(image,200);
					setCompatible(image, true);
				}
			}
		}

		for (var m in attrArray[attrValue])
		{
			if(attrArray[attrValue].hasOwnProperty(m))
			{
				//Not sure why # being used to read the resource ID
				//image = Ext.DomQuery.selectNode("#"+attrArray[attrValue][m]);
				image = document.getElementById(attrArray[attrValue][m]);
				//setOpacity(image,50);
				setCompatible(image, false);
			}
		}
	
		selectionArray[attrName] = attrValue;
		// set selection UI	
		for (var t in selectionArray) {
			if(selectionArray.hasOwnProperty(t)){
				if(selectionArray[t] != t)
				{
					setUISelected(selectionArray[t]);
				}
			}
		}
		// set UI for currently selected
		setUICurrentSelected(attrValue);

		selectionOfItem();
		svg_classhandlers_decoratePage();
	}

	function selectionOfItem() {
		var productList = new Array();
		var i = 0;
		for (var t in selectionArray) {
			if(selectionArray.hasOwnProperty(t)){
				if(selectionArray[t] != t){
					var m = selectionArray[t];
					productList[i] = valueProdBitMapArray[m];
					i++;
				}
			}
		}
		var mergedList = new Array();
		mergedList = merge(productList);
		
		checkAndDisplayResult(mergedList);
	}

	function checkAndDisplayResult( mergedList ){
		var z = 0;
		for(i = 0 ; i < totalChildItem ; i++){
			z = z + mergedList[i];
		}
		if(z == 1){
			var index = getFinalProductIndex(mergedList);
			var product = completeProductList[index];
			lit = '<a href="'+product.childDetailURL +'"> <img src="' +product.childImageURL + '" width=200 height=180/></a>';

			lit =  lit + '<p> <B>Item ID: </B><a href="'+product.childDetailURL +'">' +product.ID + '</a><BR><B>Unit Price: </B>' + product.unitPrice +' <BR> ';

				for (var m in product.attrNameList) {
					if(product.attrNameList.hasOwnProperty(m)){
						//lit = lit + product.attrNameList[m] + ' : ' + product.attrValueList[product.attrNameList[m]] + ' <BR> ';
						lit = lit + '<B>'+product.attrDescList[m] + ' :</B> ' + product.attrValueList[product.attrNameList[m]] + ' <BR> ';
					}
				}
			//lit = lit + '<BR><input type="submit" value="Add to cart" class="submitBtnBg2" />'+'</p>';
			if(null != product.unitPrice && (product.unitPrice).length >0){
			  lit = lit + '<BR><p><input id="openVariantAvailabilityLightBox" type="submit" value="Check availability" class="submitBtnBg1" onclick="javascript:openVariantAvailabilityLightBox(\'' + product.ID + '\',\'' + product.UOM + '\');return false;"/></p>';
				if(!product.headerKey || product.headerKey == "" || product.headerKey == "undefined")
				{
					lit = lit + '<BR><input id="addToCart" type="submit" value="Add To Cart" class="submitBtnBg2" onclick="javascript:variantAddToCart(\'' + product.ID + '\',\'' + product.UOM + '\',\'' + product.headerKey + '\',\'' + product.returnURL + '\');return false;"/>';
				}
				else
				{
					lit = lit + '<BR><input id="addToCart" type="submit" value="Add To Order" class="submitBtnBg2" onclick="javascript:variantAddToCart(\'' + product.ID + '\',\'' + product.UOM + '\',\'' + product.headerKey + '\',\'' + product.returnURL + '\',\'' + product.flowID + '\',\'' + product.currency + '\');return false;"/>';
				}
			}
			lit = lit + '</p>';
			Ext.DomQuery.selectNode("#childProduct").innerHTML=lit;
		}


		if(z == 0){
			Ext.DomQuery.selectNode("#childProduct").innerHTML= '<p> No Item is selected </p>';
		}
	}

	function getFinalProductIndex( mergedList ){
		for(i = 0 ; i < totalChildItem ; i++){
			if( mergedList[i] == 1){
			   return i;
			}
		}

	}

	function merge( productList ){
		var numberOfValuesSelected = productList.length;
		var mergedProdList = new Array();
		for (  i = 0 ; i < totalChildItem ; i++) {
			mergedProdList[i] = 1;
			for (  m = 0; m < numberOfValuesSelected ; m ++) {
				if(productList[m][i] == 0){
				   mergedProdList[i] = 0;
		   		}
			}
		}
		return mergedProdList;
	}

  function setOpacity(obj, opacity) {
       opacity = (opacity == 100)?99.999:opacity;

	   // IE/Win
	   obj.style.filter = "alpha(opacity:"+opacity+")";

	   // Safari<1.2, Konqueror
	   obj.style.KHTMLOpacity = opacity/100;

	   // Older Mozilla and Firefox
	   obj.style.MozOpacity = opacity/100;

	   // Safari 1.2, newer Firefox and Mozilla, CSS3
	   obj.style.opacity = opacity/100;
   }

  function product(ID,UOM,unitPrice,attrNameList,attrValueList,attrDescList,childImageURL,childDetailURL,headerKey,returnURL,flowID,currency)
  {
		this.ID=ID;
		this.UOM=UOM;
		this.unitPrice=unitPrice;
		this.attrNameList = attrNameList;
		this.attrValueList = attrValueList;
		this.attrDescList = attrDescList;
		this.childImageURL = childImageURL;
		this.childDetailURL = childDetailURL;
		this.headerKey = headerKey;
		this.returnURL = returnURL;
		this.flowID = flowID;
		this.currency = currency;
  }


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
				     DialogPanel.show('product_availability');
				     var myDiv = document.getElementById("ajax-prodAvailability");
		             myDiv.innerHTML = response.responseText;
				}
           }
       });
    }
 }

 function openProdInfoWindow(url)
 {
	   window.open(url,"_blank","toolbar=no, location=yes, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width=800, height=600");
 }

 function openProdInfoLightBox(panelId) {
      DialogPanel.show(panelId);
      svg_classhandlers_decoratePage();
 }

 function addToCart(url, productID, UOM,headerKey, returnURL,quantity, validateFlag, flowID,currency)
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

   function variantAddToCart(productID, UOM, headerKey, returnURL, flowID,currency)
   {
       var myDiv = document.getElementById("ajax-body-1");
       var url = document.getElementById("addToCartURL").value;
       addToCart(url, productID, UOM,headerKey,returnURL,'',false,flowID,currency);
   }

   function setUISelected(id)
    {
	  	var elem = Ext.get(id);	  	
	  	if(elem)
	  	{
	    	var parent = elem.findParent(".variantOption");
	    	if(parent)
	    	{
    			parent = Ext.get(parent);
    			parent.removeClass("selectedVariantOption");
    			parent.removeClass("currentSelectedVariantOption");
    			parent.removeClass("nonSelectedVariantOption");
    			parent.addClass("selectedVariantOption");
	    	}
	  	}
    }

    function setUICurrentSelected(id)
    {
	  	var elem = Ext.get(id);	  	
	  	if(elem)
	  	{
	    	var parent = elem.findParent(".variantOption");
	    	if(parent)
	    	{
    			parent = Ext.get(parent);
    			parent.removeClass("selectedVariantOption");
    			parent.removeClass("currentSelectedVariantOption");
    			parent.removeClass("nonSelectedVariantOption");
    			parent.addClass("currentSelectedVariantOption");
	    	}
	  	}
    }

    function resetAllOptionUI()
    {
    	var options = Ext.query('.variantOption');
        for (var i = 0; i < options.length; i++) 
        {
        	Ext.get(options[i]).removeClass("compatibleVariantOption");
        	Ext.get(options[i]).removeClass("nonCompatibleVariantOption");
        	Ext.get(options[i]).removeClass("selectedVariantOption");
        	Ext.get(options[i]).removeClass("nonSelectedVariantOption");
        	Ext.get(options[i]).removeClass("currentSelectedVariantOption");
        	//Ext.get(options[i]).addClass("nonSelectedVariantOption");
        }
    }

    function setCompatible(obj, compatibility)
    {
    	var elem = Ext.get(obj);
    	if(elem)
    	{
	    	var parent = elem.findParent(".variantOption");
	    	if(parent)
	    	{
    			parent = Ext.get(parent);
    			parent.removeClass("compatibleVariantOption");
    			parent.removeClass("nonCompatibleVariantOption");
	    		if(compatibility)
	    		{
	    			parent.addClass("compatibleVariantOption")
	    		}
	    		else
	    		{
	    			parent.addClass("nonCompatibleVariantOption")
	    		}
	    	}
    	}
    }

   function resetVariantTabSelection(){

		resetAllOptionUI();
		for (var t in selectionArray) {
			if(selectionArray.hasOwnProperty(t)){
				if(selectionArray[t] != t){
					selectionArray[t] = t;
 				}
			}
		}
		Ext.DomQuery.selectNode("#childProduct").innerHTML= '<p> No Item is selected </p>';

	}

    
    var focusDelay = 300;
    function openItemEmailLightBox(actionURL,dialogPanelId) {
        Ext.get('email-lightbox').load({
             url :  actionURL,
             method: 'GET',
             callback: function(el,success){
                 if(success){
	                 if(dialogPanelId == null){
	                     dialogPanelId = "emailDialogPanel";
	                 }
	                 svg_classhandlers_decoratePage();
	                 DialogPanel.show(dialogPanelId);
	                 var focusElement = Ext.get('toAddress');
	                    if(focusElement != null){
	                     focusElement.focus(focusDelay);
	                  }
                  }else{
                    var theDiv = document.getElementById("email-lightbox");
 	                theDiv.innerHTML = response.responseText; 
                 }
             }
         });
    }

 	function sendEmail(eurl) {
 		var validationResult = swc_validateForm("emailForm");
		
 		if(validationResult){
 			Ext.Ajax.request({
 				url : eurl,
	 	  form : 'emailForm',
	 	      method: 'POST',
 	      // if we get a successful response, parse the data then render the panel
 	      success: function ( response, request ) {
 	          var theDiv = document.getElementById("email-lightbox");
 	          theDiv.innerHTML = response.responseText; 
 	          svg_classhandlers_decoratePage();
 	          var focusElement = Ext.get('closeLink');
              if(focusElement != null){
                 focusElement.focus(focusDelay);
               }
 	      return true;
 	      },
 	      failure: function ( response, request ) {
 	          DialogPanel.show	('emailDialogPanel');
              var theDiv = document.getElementById("email-lightbox");
 	          theDiv.innerHTML = response.responseText; 
 	          return false;
 	      }
 	  });
 	}
 	}

    
    