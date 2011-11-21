function addItemToCart(itemId) {
	var qty;
	if(Ext.util.Format.trim(document.getElementById('Qty_'+itemId).value)!='' && Ext.util.Format.trim(document.getElementById('Qty_'+itemId).value)!=null)
		var qty = Number(Ext.util.Format.trim(document.getElementById('Qty_'+itemId).value));
	else
		var qty = Ext.util.Format.trim(document.getElementById('Qty_'+itemId).value);
		if(qty =='' || qty=='0')
		{	
			document.getElementById('Qty_'+itemId).style.borderColor="#FF0000";
			document.getElementById('Qty_'+itemId).focus();
			document.getElementById('errorMsgForQty_'+itemId).style.display = "inline";
			document.getElementById('Qty_Check_Flag_'+itemId).value = true;
			document.getElementById('Qty_'+itemId).value = "";
			return false;
		}
		var uomList = document.getElementById('itemUomList_'+itemId);
		var selectedUom = uomList.options[uomList.selectedIndex].value;
		var selectedUomText = uomList.options[uomList.selectedIndex].text;
		var index = selectedUomText.indexOf("(");
		if(index > -1)
			selectedUomText = selectedUomText.substring(0,index);
		//var selCart = document.getElementById("draftOrders");
		var itemType = "" ;
		var customerFields = "";
		var draftOrder;
		if(validateOrderMultiple(itemId) == false)
		{
			return false;
		}
		else
		{
			try{
				 //selCart = selCart.options[selCart.selectedIndex].value;
				var selCart = document.getElementById('currentCartInContextOHKVal').value; 
				 if(selCart!=null && selCart!="")
					 draftOrder = 'Y';
				 if(selCart=="") {
					 selCart = "_CREATE_NEW_";
					 draftOrder="Y";
				 }
			 }catch(ee){
				 selCart = "_CREATE_NEW_";
				 draftOrder="Y";
			 }
			 var url = document.getElementById('addItemToCartURL').value;
				
			 if(url!=null) {
				//Ext.Msg.wait("Adding Item "+itemId+"to cart...Please wait!"); 
				Ext.Ajax.request({
				    url: url,
				    params: {
						requestedItemID: itemId,
						requestedQty: qty,
						requestedUOM: selectedUom,
						requestedItemType: itemType,
						requestedCustomerFields: customerFields,
						requestedOrderHeaderKey: selCart,
						draft: draftOrder
				    },
				    method: 'POST',
					success: function (response, request){
						var responseText = response.responseText;
						if(responseText.indexOf("Error")>-1)
						{
							alert("Error Adding the Item to the cart. Please try again later");
							Ext.MessageBox.hide(); 
							return false;
						}
						else
						{
							refreshMiniCartLink();
							//JQuery Popup: 3 sec popup dispaly. 
							$.jqDialog.notify("Item successfully added to cart", 3);
							Ext.MessageBox.hide(); 
							//alert("Successfully added item "+itemId+" with quantity "+qty+" and Unit of Measure "+selectedUomText+" to the cart");
							document.getElementById('Qty_Check_Flag_'+itemId).value = false;
							//Succesfully Added to Cart Info message
						      document.getElementById('errorMsgForQty_'+itemId).innerHTML = "Successfully added to cart" ;
						      		//"<s:text name='MSG.SWC.CART.ADDTOCART.SUCCESS.ITEMADDEDINFO' />" ;
					           document.getElementById('errorMsgForQty_'+itemId).style.display = "inline"; 
					           document.getElementById('errorMsgForQty_'+itemId).setAttribute("class", "success");
						
							
							document.getElementById('Qty_'+itemId).value = "";
							 //-- Web Trends tag start --
							 //var selCart = document.getElementById("draftOrders");
							 //selCart = selCart.options[selCart.selectedIndex].value;         
							 var selCart = document.getElementById('currentCartInContextOHKVal').value; 
							 var tag = "WT.si_n,WT.tx_cartid,WT.si_x,DCSext.w_x_ord_ac";
							 var content = "ShoppingCart," + selCart + ",2,1";			
							 writeMetaTag(tag,content,4);
							//-- Web Trends tag end -
							return true;
						}	
					},
					failure: function (response, request){
					    Ext.MessageBox.hide(); 
					    alert("Error Adding the Item to the cart. Please try again later");
					    return false;
					},
				});
	
			
			 }
		}
	}

	function displayAvailability(itemId) {
		if(itemId == null || itemId =="") {
			alert("Item ID cannot be null to make a PnA call");
		}
		else{
			var qty = document.getElementById('Qty_'+itemId).value;
			var uomList = document.getElementById('itemUomList_'+itemId);
			if(uomList!=null && uomList.options.length>0)
			{
			var selectedUom = uomList.options[uomList.selectedIndex].value;
			var url = document.getElementById('checkAvailabilityURLId').value;
			/*Ext.Msg.wait("Getting Avalability for item "+itemId+"...Please wait!"); */
			Ext.Msg.wait("<s:text name='MSG.SWC.GENERIC.PROCESSING' />" ); 
			Ext.Ajax.request({
	            url: url,
	            params: {
					pnaItemId: itemId,
					pnaRequestedQty: qty,
					pnaRequestedUOM: selectedUom,
	            },
	            method: 'GET',
	            success: function (response, request){
	            	var responseText = response.responseText;
	            	if(responseText.indexOf("Unable to get Price and Availability.Please contact system admin")>-1)
	            	{
						document.getElementById('availabilty_'+itemId).innerHTML='';
						document.getElementById('availabilty_'+itemId).innerHTML=responseText;
						availabilityRow.style.display = '';
						Ext.MessageBox.hide();
						//document.getElementById('addtocart_'+itemId).focus();
	            	}
	            	else
	            	{
	            		document.body.style.cursor = 'default';
	            		var availabilityRow = document.getElementById('availabilty_'+itemId);
	            		availabilityRow.innerHTML='';
	            		availabilityRow.innerHTML=responseText;
	            		availabilityRow.style.display = 'inline';
	            		Ext.MessageBox.hide(); 
	            		//document.getElementById('addtocart_'+itemId).focus();
	            	}	
	            },
	        	failure: function (response, request){
	            	var responseText = response.responseText;
	                document.body.style.cursor = 'default';
	                Ext.MessageBox.hide();
	                document.getElementById('availabilty_'+itemId).innerHTML='';
	                document.getElementById('availabilty_'+itemId).innerHTML=responseText;
	                document.getElementById('availabilty_'+itemId).style.display = '';
	                alert('Failure');
	            },
	        });
			}
			else{
				alert("Item UOM cannot be null to make a PnA call");
			}
		}
		//document.getElementById('addtocart_'+itemId).focus();
	}
	
	function validateOrderMultiple(itemId) {
		var orderMultiple = getOrderMultiple(itemId);
		if(orderMultiple == "") {
			alert("Failed getting the order multiple for the Item. Please try adding the item later again..");
			return false;
		}
		var uomList = document.getElementById('itemUomList_'+itemId);
		var selectedUom = uomList.options[uomList.selectedIndex].value;
		var qty = document.getElementById('Qty_'+itemId).value;
		var selectedUomConv;
		var itemUomsString = getItemUoms(itemId);
		var uomAndConv = itemUomsString.split("|");
		if(uomAndConv.length>0) {
			for(var i=1; i<uomAndConv.length; i++) {
				var tmpUomAndConv = uomAndConv[i].split(":");
				if(tmpUomAndConv[0] == selectedUom) {
					selectedUomConv = tmpUomAndConv[1];
				}
			}
		}
		var totalQty = selectedUomConv * qty;
		var ordMul = totalQty % orderMultiple;
		if(ordMul != 0 || totalQty==0)
		{
			alert("Order Quantity for " + itemId + " must be a multiple of " + orderMultiple);
			return false;
		}
		return true;
	}
	
	function getOrderMultiple(itemId) {
	    var url=document.getElementById("orderMultipleURL").value;
	    var req=null;
	    if (window.XMLHttpRequest){
	    	req = new XMLHttpRequest();
	    }else{
	    	req = new ActiveXObject("Microsoft.XMLHTTP");
	    }
	    url=url+'&itemId='+itemId;
	    req.open('POST', url, false);     
	    req.send(null);
	    if(req.status == 200){
	      return req.responseText;
	    }else{
	    	return "";
	    }
	}
	
	function getItemUoms(itemId) {
	    var url=document.getElementById("getItemUomsURL").value;
	    var req=null;
	    if (window.XMLHttpRequest){
	    	req = new XMLHttpRequest();
	    }else{
	    	req = new ActiveXObject("Microsoft.XMLHTTP");
	    }
	    url=url+'&itemId='+itemId;
	    req.open('POST', url, false);     
	    req.send(null);
	    if(req.status == 200){
	      return req.responseText;
	    }else{
	    	return "";
	    }
	}
	
	function qtyInputCheck(component, itemId){
		var qtyCheckFlag = document.getElementById("Qty_Check_Flag_"+itemId).value;
		if(qtyCheckFlag=="true"){
			if(component.value==""){
            	component.style.borderColor="#FF0000";
            	document.getElementById('errorMsgForQty_'+itemId).style.display = "inline";
        	}
        	else{
            	component.style.borderColor="";
            	document.getElementById('errorMsgForQty_'+itemId).style.display = "none";
    	    }
    	}
    }
