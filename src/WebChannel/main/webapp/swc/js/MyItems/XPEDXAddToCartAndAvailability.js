	function updateHidden(component,uid,fieldCount) {
		var name = component.name.trim();
		var value = component.value.trim();
		if(name.indexOf('qtys')>-1)
		{
			document.getElementById('QTY_'+uid).value=value;
			document.getElementById('enteredQuantities_'+uid).value=value;
		}
		if(name.indexOf('uoms')>-1)
		{
			document.getElementById('UOM_'+uid).value=value;
			document.getElementById('enteredUOMs_'+uid).value=value;
			document.getElementById('UOMconversion_'+uid).value= document.getElementById("convF_"+value).value;
		}
		if(name.indexOf('customField')>-1)
		{
			var custFieldName = name.substring(name.indexOf('customField')+11, name.length - 1);
			document.getElementById('customerField_'+fieldCount+"_"+uid).value= "Extn" + custFieldName + "@"+value;
			document.getElementById('entered'+custFieldName+'_'+uid).value= value;
			
		}
	}
	
	function updateHidden(component,uid,fieldCount,jsonMap)
	{
		var name = component.name.trim();
		var value = component.value.trim();
		if(name.indexOf('qtys')>-1)
		{
			document.getElementById('QTY_'+uid).value=value;
			document.getElementById('enteredQuantities_'+uid).value=value;
		}
		if(name.indexOf('uoms')>-1)
		{
			var obj = jQuery.parseJSON(jsonMap);
			var conversionFactor =  obj[value];
			document.getElementById('UOM_'+uid).value=value;
			document.getElementById('enteredUOMs_'+uid).value=value;
			document.getElementById('UOMconversion_'+uid).value=conversionFactor;
		}
		if(name.indexOf('customField')>-1)
		{
			var custFieldName = name.substring(name.indexOf('customField')+11, name.length - 1);
			document.getElementById('customerField_'+fieldCount+"_"+uid).value= "Extn" + custFieldName + "@"+value;
			document.getElementById('entered'+custFieldName+'_'+uid).value= value;
			
		}
	}
	var myMask;
	function addItemToCart(itemId,uid) {
		//added for jira 3974
		var waitMsg = Ext.Msg.wait("Processing...");
		myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
		myMask.show();

		 //commented for jira 3253 resetQuantityError(uid);
		//reset the Qty box- Jira 3197- MIL Messaging
		document.getElementById('qtys_'+uid).style.borderColor="";
		 if(validateOrderMultiple(true,uid) == false)
		 {
		 	Ext.Msg.hide();
		    myMask.hide();
			return;
		 }
		var qty = document.getElementById('QTY_'+uid).value;
		var uom = document.getElementById('UOM_'+uid).value;
		var itemType = document.getElementById('entereditemTypeList_'+uid).value;
		var customerFieldSize = document.getElementById('customerFieldsSize_'+uid).value
		var customerFields="";
		//var selCart = document.getElementById("draftOrders");
		var draftOrder;
		if(customerFieldSize!=null && customerFieldSize>0) {
			for(var i=1; i<=customerFieldSize; i++) {
				var temp = 'customerField_'+i+"_"+uid;
				var field = document.getElementById(temp).value;
				customerFields= customerFields+","+field;
			}
		}
		try{
			 //selCart = selCart.options[selCart.selectedIndex].value;
			var selCart = document.getElementById('currentCartInContextOHKVal').value;
			if(selCart!=null && selCart!="")
				 draftOrder = 'Y';
			 if(selCart=="") {
				 selCart = "_CREATE_NEW_";
				 draftOrder="N";
			 }
		 }catch(ee){
			 selCart = "_CREATE_NEW_";
			 draftOrder="N";
		 }
		 var url = document.getElementById('updatePandAURL').value;
		 if(url!=null) {
			 
			//Ext.Msg.wait("Adding Item "+itemId+"to cart...Please wait!"); 
			Ext.Ajax.request({
			    url: url,
			    params: {
					requestedItemID: itemId,
					requestedQty: qty,
					requestedUOM: uom,
					requestedItemType: itemType,
					requestedCustomerFields: customerFields,
					requestedOrderHeaderKey: selCart,
					draft: draftOrder
			    },
			    method: 'POST',
				success: function (response, request){
					var responseText = response.responseText;
		        	var draftErr = response.responseText;
		            var draftErrDiv = document.getElementById("errorMessageDiv");
		            if(draftErr.indexOf("This cart has already been submitted, please refer to the Order Management page to review the order.") >-1)
		        {			refreshWithNextOrNewCartInContext();
		                    draftErrDiv.innerHTML = "<h5 align='left'><b><font color=red>" + response.responseText + "</font></b></h5>";
		                    Ext.Msg.hide();
		                	myMask.hide();
		        	}
		            else if(draftErr.indexOf("Item has been added to your cart. Please review the cart to update the item with a valid quantity.") >-1)
			        {
		            	var divVal=document.getElementById('errorDiv_qtys_'+uid);        
		            	divVal.innerHTML = "Item has been added to your cart. Please review the cart to update the item with a valid quantity.";
		            	divVal.style.display = "inline-block"; 
						divVal.setAttribute("style", "margin-right:5px;float:right;");
						divVal.setAttribute("class", "error");
			                    Ext.Msg.hide();
			                	myMask.hide();
			        }
					else if(responseText.indexOf("Error")>-1)
					{
						refreshMiniCartLink();
						Ext.Msg.hide();
						myMask.hide();
						alert("Error Adding the Item to the cart. Please try again later");
					}
					else
					{
						refreshMiniCartLink();
						//Succesfully Added to Cart Info message for jira 3253
						var divId = 'errorDiv_qtys_'+uid;
						var divVal=document.getElementById('errorDiv_qtys_'+uid);
						//Start- fix for 3105
						if(document.getElementById('isEditOrder')!=null && document.getElementById('isEditOrder').value!=null && document.getElementById('isEditOrder').value!='')
						{
								divVal.innerHTML = "Item has been added to order." ;
						}
						else
						{
							divVal.innerHTML = "Item has been added to cart." ;
						}

						// commented for 3105
						//divVal.innerHTML ="Item successfully added to cart";
					   	//"<s:text name='MSG.SWC.CART.ADDTOCART.SUCCESS.ITEMADDEDINFO' />" ;
						divVal.style.display = "inline-block"; 
						divVal.setAttribute("style", "margin-right:5px;float:right;");
						divVal.setAttribute("class", "success");
				       		 //end of addition 3253
						Ext.Msg.hide();
						myMask.hide();
						//JQuery Popup: 3 sec popup dispaly. 
						// commented for 3105
						//$.jqDialog.notify("Item successfully added to cart", 3);
					/*	if(document.getElementById('isEditOrder')!=null && document.getElementById('isEditOrder').value!=null && document.getElementById('isEditOrder').value!='')
						{
							$.jqDialog.notify("Item successfully added to order", 3);
						}
						else{
							$.jqDialog.notify("Item successfully added to cart", 3);
						} commented for jira 3974*/
						
						//End- fix for 3105
						//Ext.MessageBox.hide(); 
						//alert("Successfully added item "+itemId+" with quantity "+qty+" to the cart");
						//-- Web Trends tag start --
						var tag = "WT.si_n,WT.tx_cartid,WT.si_x,DCSext.w_x_ord_ac";
						var content = "ShoppingCart," + selCart + ",2,1";
						writeMetaTag(tag,content,4);
						//-- Web Trends tag end --
					}	
				},
				failure: function (response, request){
					refreshMiniCartLink();
				    //Ext.MessageBox.hide(); 
					Ext.Msg.hide();
					myMask.hide();
				    alert("Error Adding the Item to the cart. Please try again later");
				}
			});

		 }
	}

	//-- Web Trends tag start --
	function writeWebtrendTag(responseText){
		var variable1 = 'meta name=\"DCSext.w_x_sc\"' ;
		var variable2 = 'meta name=\"DCSext.w_x_scr\"' ;
		var variable3 = "content=";
		var variable4 = "content=";
		var try21 = responseText.indexOf(variable1);
		var try211 = responseText.indexOf(variable3);
		var try22 = responseText.indexOf(variable2);
		var try222 = responseText.lastIndexOf(variable4);
		var try3 = responseText.substring(try21+10,try211-1);
		
		var try4 = responseText.substring(try22+10,try222-1);
		
		var try5 = responseText.indexOf(try3);
		var try51 = responseText.substring(try5+try3.length);
		
		var test1 = try51.indexOf("content=");
		var test11 = try51.indexOf("></meta");
		var test111 = try51.substring(test1+8,test11);		
		
		var test2 = try51.lastIndexOf("content=");
		var test22 = try51.lastIndexOf("></meta");
		var test222 = try51.substring(test2+8,test22);		
		
		tag = "DCSext.w_x_sc,DCSext.w_x_scr";
		content = test111 + ","+test222;	
		
		content = ReplaceAll(content,"'","");
		content= ReplaceAll(content,'"','');
		
		writeMetaTag(tag,content,2);
		
	}
	//-- Web Trends tag end --
	var myMask;
	function displayAvailability(itemId,qty,uom,myItemsKey,url,validateOM) {
		//added for jira 3974
		var waitMsg = Ext.Msg.wait("Processing...");
		myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
		myMask.show();
		if(itemId == null || itemId =="") {
			alert("Item ID cannot be null to make a PnA call");
		}
		else{
			//Commented for 3475
			//Ext.Msg.wait("Processing..."); 
			Ext.Ajax.request({
	            url: url,
	            params: {
					pnaItemId: itemId,
					pnaRequestedQty: qty,
					pnaRequestedUOM: uom,
					myItemsKey:myItemsKey,
					validateOM:validateOM
	            },
	            method: 'POST',
	            success: function (response, request){
	            	var responseText = response.responseText;
	            	if(responseText.indexOf("Unable to get Price and Availability.Please contact system admin")>-1)
	            	{
						document.getElementById('availabilityRow_'+myItemsKey).innerHTML='';
						document.getElementById('availabilityRow_'+myItemsKey).innerHTML=responseText;
						availabilityRow.style.display = '';
						//Ext.MessageBox.hide(); 
	            	}
	            	else
	            	{
	            		document.body.style.cursor = 'default';
	            		//alert(responseText);
	            		var availabilityRow = document.getElementById('availabilityRow_'+myItemsKey);
	            		availabilityRow.innerHTML='';
	            		availabilityRow.innerHTML=responseText;
	            		availabilityRow.style.display = '';
	            		
	            		// start of XB 214 BR1
		            	var sourceOrderMulError = document.getElementById("errorDiv_qtys_"+myItemsKey);
		            	var orderMultipleQtyFromSrc = document.getElementById("orderMultipleQtyFromSrc_"+myItemsKey);
		            	if(orderMultipleQtyFromSrc != null ){
		            	var orderMultipleQtyFromSrc1 = document.getElementById("orderMultipleQtyFromSrc_"+myItemsKey).value;
		            	var orderMultipleQtyUom = orderMultipleQtyFromSrc1.split("|");
		            	var orderMultipleQty = orderMultipleQtyUom[0];
		            	var orderMultipleUom = orderMultipleQtyUom[1];
		            	var omError = orderMultipleQtyUom[2];
		            	//alert("orderMultipleQty"+orderMultipleQty+"orderMultipleUom :"+orderMultipleUom+"OMError: "+omError)

		            	if(omError == 'true')
		            	{
		            		//alert("orderMultipleQty : "+orderMultipleQty+"omError : "+omError);
		            		sourceOrderMulError.innerHTML = "Must be ordered in units of " + addComma(orderMultipleQty) +" "+orderMultipleUom;
		            		sourceOrderMulError.style.display = "inline-block"; 
		            		sourceOrderMulError.setAttribute("class", "notice");
		            	}
		            	}
		            	//End of BR1 XB 214
		            	
	            		Ext.Msg.hide();
				myMask.hide();
	            		//-- Web Trends tag start --
	            		writeWebtrendTag(responseText);
	            		//-- Web Trends tag end --
	            		
	            		//Ext.MessageBox.hide(); 
	            	}	
	            },
	        	failure: function (response, request){
	        		var responseText = response.responseText;
	                document.body.style.cursor = 'default';
	                Ext.MessageBox.hide();
	                document.getElementById('availabilityRow_'+myItemsKey).innerHTML='';
	                if(responseText!=null && responseText!=undefined)
	                	document.getElementById('availabilityRow_'+myItemsKey).innerHTML=responseText;
	                else
	                	document.getElementById('availabilityRow_'+myItemsKey).innerHTML='<tbody>'+
	        		'<tr ><td width="100%"><h5 align="center"><b><font color="red">Your request could not be completed at this time, please try again.</font></b></h5>'+
	        		'</td></tr>'+
	        		'<tr style="border-bottom: 1px solid rgb(204, 204, 204);">'+
	        		'<td></td></tr></tbody>';
					//document.getElementById('availabilityRow_'+myItemsKey).innerHTML=responseText;
					document.getElementById('availabilityRow_'+myItemsKey).style.display = '';
					Ext.Msg.hide();
					myMask.hide();
	                //alert('Failure');
	            },
	        });
		}			
	}

// Checking the availability the way it is done on catalog page with the pop up and all requires the below functions
	function checkUpdateAvailability() 
	{
		var ItemId=document.getElementById("ITemId").value;
		var UOM=document.getElementById("UOM").value;	
		if(document.getElementById("Qty").value!=null)
		{
			var qtyEle = document.getElementById("Qty");
			ItemId=ItemId+'&Qty='+document.getElementById("Qty").value+'&UOM='+UOM;
		}
		openAddToCart(ItemId);
			
	}
	
	function populateDivGrid1(myData)
	{	
		var obj = eval("(" + myData + ")");
		var availStore = new Ext.data.JsonStore({
		   proxy: new Ext.data.HttpProxy({  
			    method:'POST',  
			    url: '/swc/catalog/ajaxAvailabilityJson.action?sfId=xpedx',
			    headers: {'Content-type': 'application/json'}
			}),		
			data   : obj,					
			root: 'availList',
			fields:['availability','availValue']		
	    });
		var grid = new Ext.grid.GridPanel({
	        width:550,
	        height:200,
	        layout:'fit',
	        title:'',
	        store: availStore,
	        trackMouseOver:false,
	        disableSelection:true,
	        loadMask: true,
	
	        // grid columns
	        columns:[{
	            id: 'tableUOM', 
	            header: '',
	            dataIndex: 'availability',
	            width: 130,
	            height:80,
	            sortable: false
	        },{
	            header: '',
	            dataIndex: 'availValue',
	            width: 130,
	            height:80,
	            sortable: false
	        }]
	    });//grid			
	    Ext.fly('availibility-grid').dom.innerHTML='';
		availStore.loadData(obj);	
		grid.render('availibility-grid');
		document.body.style.cursor = 'default';
	}
	
	function populateAddToCartDlg(url) {
		if(url == null) {
			var url = availabilityURL;
		}
		Ext.Ajax.request({
            url: url,
            params: {
				pnaItemId: selectedItemId,
            },
            method: 'POST',
            success: function (response, request){
            	var responseText = response.responseText;
            	if(responseText.indexOf("Unable to get Price and Availability.Please contact system admin")>-1)
            	{
					document.getElementById('availibility-grid_error').innerHTML='';
					document.getElementById('availibility-grid_error').innerHTML=responseText;
            	}
            	else
            	{
            		var variable1="{\"availList\":";
            		var startIndexof=responseText.indexOf(variable1);
            		Ext.fly('availibility-grid').dom.innerHTML='';
            		if(startIndexof > 0){
            			var jsonString=responseText.substring(startIndexof,responseText.length);
            			variable1="}]}";
            			startIndexof=jsonString.indexOf(variable1)+variable1.length;
            			jsonString=jsonString.substring(0,startIndexof);
            			populateDivGrid1(jsonString);
            		}
            		document.body.style.cursor = 'default';
            		document.getElementById('add-to-cart-grid').innerHTML='';
					document.getElementById('add-to-cart-grid').innerHTML=responseText;
            	}	
            },
        	failure: function (response, request){
                document.body.style.cursor = 'default';
                alert('Failure');
            },
        });
	}
	
	function clearAddToCartDiv() {
		document.getElementById('add-to-cart-grid').innerHTML='';
		document.getElementById('availibility-grid').innerHTML='';
	}
	
	function openDialog()
	{
	 	openAddToCart1(document.getElementById("ITemId").value)
		javascript:DialogPanel.show('bracketPricingDialog');
	}
	
	function handleBracketPriceSuccess(responseObject){
		var responseText = responseObject.responseText;
		Ext.fly('Price_Grid').dom.innerHTML='';
		Ext.fly('Price_Grid').dom.innerHTML=responseText;
	}
	
	function handleBracketPricefailure(){
		alert('fail');
	}
	
	function openAddToCart1(val){
		Ext.onReady(function(){
			Ext.Ajax.request({
			   url: '/swc/catalog/ajaxAvailabilityJson_Price.action?sfId=xpedx&pnaItemId='+val,
			   success: handleBracketPriceSuccess,
			   failure: handleBracketPricefailure
			});
						
		});
	}
	
	function isValidQuantity(component) {
	    //valid size if 10,3(7 whole and 3 after digits)
	    /*var quantity = component.value.trim();
	    var qtyLen = quantity.length;
	    var validVals = "0123456789.";
	    var isValid=true;
	    var char;
	    for (i = 0; i < qtyLen && isValid == true; i++) {
	       char = quantity.charAt(i); 
	       if (validVals.indexOf(char) == -1) 
	       {
	          isValid = false;
	       }
	 	}
	 	if (!isValid){
	        component.value = "";
	        return false;
	 	}

	    var qtyDecPointAt = quantity.indexOf(".");
	    var wholenum = quantity.substr(0,qtyDecPointAt);
		if (qtyDecPointAt == -1){
			wholenum = quantity;
		}
	    if (wholenum.length > 7){
	        var val = quantity.substr(0,7);
			if (qtyDecPointAt != -1){
				val = val + quantity.substr(qtyDecPointAt,qtyLen);
			}
			quantity = val;
	        component.value = quantity;
	    }
	    if (qtyDecPointAt != -1 && qtyDecPointAt < qtyLen - 4) {
	        quantity = quantity.substr(0,qtyDecPointAt + 4);
	        component.value = quantity;
	        return false;
	    }

	    return true;
	    */
		//Fix for Qty not being decimal
    	var quantity = component.value.trim();
        var qtyLen = quantity.length;
        var validVals = "0123456789";
        var isValid=true;
        var char;
        for (i = 0; i < qtyLen && isValid == true; i++) {
           char = quantity.charAt(i); 
           if (validVals.indexOf(char) == -1) 
           {
              isValid = false;
           }
     	}
     	if (!isValid){
            component.value = "";
            return false;
     	}

        if (quantity.length > 7){
            var val = quantity.substr(0,7);
    		quantity = val;
            component.value = quantity;
        }
        return true;
	}
	
	function openData(data)
	{	var ITemId=document.getElementById("ITemId").value;
		var Qty=document.getElementById("Qty").value;
		var UOM=document.getElementById("UOM").value;
		var Job=document.getElementById("Job").value;
		var customer=document.getElementById("customer").value;
		
		listAddToCartItem(addToCartURL,ITemId,UOM,Qty,Job,customer,'');
	}
	
	function listAddToCartItem(url, productID, UOM, quantity,Job,customer) {
	      
	   	Ext.Ajax.request({
	    	// for testing only
	        url: url,
	        params: {
				productID: productID,
		    	productUOM: UOM,
		    	quantity: quantity,
		    	reqJobId: Job,
		    	reqCustomer:customer
		     },
	        // end testing
	        method: 'GET',
	        success: function (response, request){
	            $.fancybox.close();
	            if(quantity == "")
	            	quantity ='1';
	            alert('The Item '+productID+" with Quantity "+quantity+" has been added successfully to your cart");
	        },
	        failure: function (response, request){
	        	$.fancybox.close();
	            alert("Error adding Item to Cart Please try again later...");	           
	          }
	    });		
	}
	