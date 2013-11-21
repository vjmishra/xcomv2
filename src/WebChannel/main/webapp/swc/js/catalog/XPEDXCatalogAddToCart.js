var myMask;
function addItemToCart(itemId) {
	var qty;
	//added for jira 3974
	var waitMsg = Ext.Msg.wait("Processing...");
	myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
	myMask.show();
	//Ext.Msg.wait("Processing...");
	if(Ext.util.Format.trim(document.getElementById('Qty_'+itemId).value)!='' && Ext.util.Format.trim(document.getElementById('Qty_'+itemId).value)!=null)
		var qty = Number(Ext.util.Format.trim(document.getElementById('Qty_'+itemId).value));
	else
		var qty = Ext.util.Format.trim(document.getElementById('Qty_'+itemId).value);
		if(qty =='' || qty=='0')
		{	
			document.getElementById('Qty_'+itemId).style.borderColor="#FF0000";
			document.getElementById('Qty_'+itemId).focus();
			document.getElementById('errorMsgForQty_'+itemId).innerHTML = "Please enter a valid quantity and try again." ;
			document.getElementById('errorMsgForQty_'+itemId).style.display = "inline"; 
			document.getElementById('errorMsgForQty_'+itemId).setAttribute("class", "error");
			document.getElementById('errorMsgForQty_'+itemId).setAttribute("style", "margin-right:5px;float:right;");
			document.getElementById('Qty_Check_Flag_'+itemId).value = true;
			//document.getElementById('Qty_'+itemId).value = ""; commented for EB 41,42,43 - dont clear Qty on failure to add to cart
			Ext.Msg.hide();
			myMask.hide();
			return false;
		}
		var uomList = document.getElementById('itemUomList_'+itemId);
		var defaultUom = document.getElementById('defaultUOMCode_'+itemId);//Added for EB 41,42,43
		var selectedUom = uomList.options[uomList.selectedIndex].value;
		var selectedUomText = uomList.options[uomList.selectedIndex].text;
		var index = selectedUomText.indexOf("(");
		if(index > -1)
			selectedUomText = selectedUomText.substring(0,index);
		//var selCart = document.getElementById("draftOrders");
		var itemType = "" ;
		var customerFields = "";
		//Added for EB 3372
		var Job="";
		if(document.getElementById('Job_'+itemId)!=null)
			 Job=document.getElementById('Job_'+itemId).value;
		
		var customerPONo = "";
		if(document.getElementById('customerPONo_'+itemId)!=null &&  document.getElementById('customerPONo_'+itemId)!=undefined){
			customerPONo=document.getElementById('customerPONo_'+itemId).value; 
		}
		//End of EB 3372
		var draftOrder;
		if(validateOrderMultiple(itemId) == false)
		{
			Ext.Msg.hide();
			myMask.hide();
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
						requestedJobId: Job,
				    	        requestedCustomerLinePONo: customerPONo,
						requestedOrderHeaderKey: selCart,
						draft: draftOrder
				    },
				    method: 'POST',
					success: function (response, request){
						var responseText = response.responseText;
						//Added for EB 560
		    	   			if(response.responseText.indexOf('Sign In</span></a>') != -1 && response.responseText.indexOf('signId') != -1){
		    	   				window.location.reload(true);
		    	   				Ext.Msg.hide();
		    					myMask.hide();
		    					return;
		    	   			}
		    				//End of EB 560
						if(responseText.indexOf("This cart has already been submitted, please refer to the Order Management page to review the order.") >-1){
							refreshWithNextOrNewCartInContext();
							Ext.Msg.hide();
							myMask.hide();
							alert("This cart has already been submitted, please refer to the Order Management page to review the order.");
							return false;
						}	
						 else if(responseText.indexOf("We were unable to add some items to your cart as there was an invalid quantity in your list. Please correct the qty and try again.") >-1)
							 {
							   	document.getElementById('Qty_'+itemId).style.borderColor="#FF0000";
								document.getElementById('Qty_'+itemId).focus();
								document.getElementById('errorMsgForQty_'+itemId).innerHTML = "Please enter a valid quantity and try again." ;
								document.getElementById('errorMsgForQty_'+itemId).style.display = "inline"; 
								document.getElementById('errorMsgForQty_'+itemId).setAttribute("class", "error");
								document.getElementById('errorMsgForQty_'+itemId).setAttribute("style", "margin-right:5px;float:right;");
								document.getElementById('Qty_Check_Flag_'+itemId).value = true;
								//document.getElementById('Qty_'+itemId).value = ""; Commented for EB 41
								Ext.Msg.hide();
								myMask.hide();
								return false;
							 }
						 else if(responseText.indexOf("Exception While Applying cheanges .Order Update was finished before you update") >-1)
			             {
							 var orderHeaderKey=document.getElementById("editOrderHeaderKey").value;
				        	 var orderdetailsURL=document.getElementById('orderdetailsURLId').value+'&isErrorMessage=Y&orderHeaderKey='+orderHeaderKey;				        	 
				        	 orderdetailsURL = ReplaceAll(orderdetailsURL,"&amp;",'&');
				        	 window.location=orderdetailsURL;//"orderDetail.action?sfId=<s:property value="wCContext.storefrontId" />&orderHeaderKey=<s:property value="#orderHeaderKey" />&scFlag=Y";
			             }
						else if(responseText.indexOf("has been added to your cart. Please review the cart to update the item with a valid quantity.") >-1)
				        {
							refreshMiniCartLink();
							if(document.getElementById('isEditOrder')!=null && document.getElementById('isEditOrder').value!=null && document.getElementById('isEditOrder').value!=''){
								document.getElementById('errorMsgForQty_'+itemId).innerHTML ="Item has been added to your order. Please review the order to update the item with a valid quantity.";
								document.getElementById('errorMsgForQty_'+itemId).setAttribute("class", "error");	
							}else{
								document.getElementById('errorMsgForQty_'+itemId).innerHTML = qty+" "+selectedUomText+" has been added to your cart. Please review the cart to update the item with a valid quantity.";
								document.getElementById('errorMsgForQty_'+itemId).setAttribute("class", "error");
							}
							document.getElementById('errorMsgForQty_'+itemId).style.display = "inline"; 
					                document.getElementById('errorMsgForQty_'+itemId).setAttribute("style", "margin-right:5px;float:right;");	
					        	//Added for EB 41
					        	document.getElementById('Qty_'+itemId).value = "";
					        	if(defaultUom != null && defaultUom.value !=''){
					        		document.getElementById('itemUomList_'+itemId).value = defaultUom.value;
					        	}
					        	else{
					        		document.getElementById('itemUomList_'+itemId).value = uomList.options[0].value;
					        	}
							//ENd of EB 41
							Ext.Msg.hide();
							myMask.hide();
							return true;
				        }
						else if(responseText.indexOf("Error")>-1)
						{
							refreshMiniCartLink();
							Ext.Msg.hide();
							myMask.hide();
							alert("Error Adding the Item to the cart. Please try again later");
							return false;
						}
						else
						{
							refreshMiniCartLink();
							//JQuery Popup: 3 sec popup dispaly. 							
							/*if(document.getElementById('isEditOrder')!=null && document.getElementById('isEditOrder').value!=null && document.getElementById('isEditOrder').value!='')
								$.jqDialog.notify("Item successfully added to order", 3);
							else
								$.jqDialog.notify("Item successfully added to cart", 3);
							Ext.MessageBox.hide(); */
							//alert("Successfully added item "+itemId+" with quantity "+qty+" and Unit of Measure "+selectedUomText+" to the cart");
							document.getElementById('Qty_Check_Flag_'+itemId).value = false;
							//Succesfully Added to Cart Info message
							/*Start- Jira 3104 */
							if(document.getElementById('isEditOrder')!=null && document.getElementById('isEditOrder').value!=null && document.getElementById('isEditOrder').value!='')
						      		document.getElementById('errorMsgForQty_'+itemId).innerHTML = "Item has been added to order." ;
							else
								document.getElementById('errorMsgForQty_'+itemId).innerHTML = qty+" "+selectedUomText+" has been added to cart." ;
						    /*End- Jira 3104 */
						      		//"<s:text name='MSG.SWC.CART.ADDTOCART.SUCCESS.ITEMADDEDINFO' />" ;
					           	document.getElementById('errorMsgForQty_'+itemId).style.display = "inline"; 
					           	document.getElementById('errorMsgForQty_'+itemId).setAttribute("style", "margin-right:5px;float:right;");
					           	document.getElementById('errorMsgForQty_'+itemId).setAttribute("class", "success");
					         	//Added for EB 41
						        document.getElementById('Qty_'+itemId).value = "";
						        if(defaultUom != null && defaultUom.value !='' ){
						        	document.getElementById('itemUomList_'+itemId).value = defaultUom.value;
						        }
						        else {
					        		document.getElementById('itemUomList_'+itemId).value = uomList.options[0].value;
						        }
							Ext.Msg.hide();
							myMask.hide();
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
					    //Ext.MessageBox.hide(); 
					    refreshMiniCartLink();
					    	Ext.Msg.hide();
						myMask.hide();
						alert("Error Adding the Item to the cart. Please try again later");
					    return false;
					},
				});
	
			
			 }
		}
	}
var priceCheck;
var myMask;
	function displayAvailability(itemId) {
		//added for jira 3974
		var waitMsg = Ext.Msg.wait("Processing...");
		myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
		myMask.show();
		
		priceCheck = true;
		var validateOM;
		if(validateOrderMultiple(itemId) == false)
		{
			validateOM =  false;
			Ext.Msg.hide();
			myMask.hide();
		}
		else{
			validateOM =  true;
		}

		//alert("priceCheck = "+priceCheck);
		if(itemId == null || itemId =="") {
			Ext.Msg.hide();
			myMask.hide();
			alert("Item ID cannot be null to make a PnA call");
		}
		else if(validateOM == true){
			var qty = document.getElementById('Qty_'+itemId).value;
			var qtyTextBox=qty;
			var custUOM = document.getElementById('custUOM_'+itemId).value;
			var uomList = document.getElementById('itemUomList_'+itemId);
			var baseUOM = document.getElementById('baseUOMs_'+itemId).value;
			if(uomList!=null && uomList.options.length>0)
			{
			var selectedUom = uomList.options[uomList.selectedIndex].value;
			var url = document.getElementById('checkAvailabilityURLId').value;
			// added for  EB 2034 to get the PnA results based on the selected UOM on click of PnA Link
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
			var orderMul = document.getElementById('orderMultiple_'+itemId);
			//Added if block for jira 3922
			if(qty == null || qty == "null" || qty == "") {
				
				if(orderMul != null && orderMul.value != 0 && selectedUomConv != 0 ){
						if(selectedUomConv == 1){
							qty = document.getElementById('orderMultiple_'+itemId).value;
						}
				else if(selectedUomConv <= orderMul.value){
							if((orderMul.value % selectedUomConv) == 0){
								qty = orderMul.value / selectedUomConv;
							}
							else{
								qty = 1;
							}
						}
				else{//if conversionFactor is greater than OrderMul irrespective of the moduloOf(conversionFactor,OrderMul) is a whole number / decimal result we set the Qty to 1
						qty = 1;
				    }
				}
				//End of EB 2034
				/*commented for EB 2034
				 * var qty = document.getElementById('orderMultiple_'+itemId).value;
				   var selectedUom = document.getElementById('baseUOMItem_'+itemId).value;
				 */
			}
			Ext.Ajax.request({
	            url: url,
	            params: {
					pnaItemId: itemId,
					pnaRequestedQty: qty,
					qtyTextBox: qtyTextBox,
					pnaRequestedUOM: selectedUom,
					validateOM : validateOM,
					pnaReqCustomerUOM : custUOM,
	            },
	            method: 'GET',
	            success: function (response, request){
	            	var responseText = response.responseText;
	            	//Added for EB 560
	    	   	if(response.responseText.indexOf('Sign In</span></a>') != -1 && response.responseText.indexOf('signId') != -1){
	    	   		window.location.reload(true);
	    	   		Ext.Msg.hide();
	    			myMask.hide();
	    			return;
	    	   	}
	    		//End of EB 560
	            	if(responseText.indexOf("Unable to get Price and Availability.Please contact system admin")>-1)
	            	{
						document.getElementById('availabilty_'+itemId).innerHTML='';
						document.getElementById('availabilty_'+itemId).innerHTML=responseText;
						availabilityRow.style.display = '';
						Ext.Msg.hide();
						myMask.hide();
						//document.getElementById('addtocart_'+itemId).focus();
	            	}
	            	else
	            	{
	            		document.body.style.cursor = 'default';
	            		var availabilityRow = document.getElementById('availabilty_'+itemId);
	            		availabilityRow.innerHTML='';
	            		availabilityRow.innerHTML=responseText;
	            		availabilityRow.style.display = 'inline';
	            		
	            		// start of XB 214 BR1
	            		var qty = document.getElementById("Qty_"+itemId);
		            	var sourceOrderMulError = document.getElementById("errorMsgForQty_"+itemId);
		            	
		            	var orderMultipleQtyFromSrc = document.getElementById("orderMultipleQtyFromSrc_"+itemId);
		            	if(orderMultipleQtyFromSrc != null && orderMultipleQtyFromSrc.value != ''){
		            	var orderMultipleQtyFromSrc1 = document.getElementById("orderMultipleQtyFromSrc_"+itemId).value;
		            	var orderMultipleQtyUom = orderMultipleQtyFromSrc1.split("|");
		            	var orderMultipleQty = orderMultipleQtyUom[0];
		            	var orderMultipleUom = orderMultipleQtyUom[1];
		            	
		            	
		            	//Added for EB-439 - start
		            	var customerUOM = document.getElementById("reqCustomerUOM");
		            	if(customerUOM!=null && customerUOM!=undefined ){
		            		var custuom = customerUOM.value;
		            		if(custuom!=null && custuom == orderMultipleUom){
		            			orderMultipleUom = custuom.substr(2);
		            		}
		            	}//Added for EB-439 - start
		            	var omError = orderMultipleQtyUom[2];		            			
		            	if(omError == 'true' && (qty.value >0 || qty.value == "") ){
				            sourceOrderMulError.innerHTML = "Must be ordered in units of " + addComma(orderMultipleQty) +" "+orderMultipleUom;
				            sourceOrderMulError.style.display = "inline"; 
				            sourceOrderMulError.setAttribute("class", "error");
				            availabilityRow.style.display = "none"; 
				            qty.style.borderColor="#FF0000";
				    		qty.focus();		            
				        }
		            	else if(omError == 'true')
		            	{	
		            		sourceOrderMulError.innerHTML = "Must be ordered in units of " + addComma(orderMultipleQty) +" "+orderMultipleUom;
		            		sourceOrderMulError.style.display = "inline"; 
		            		sourceOrderMulError.setAttribute("class", "notice");
		            		availabilityRow.style.display = "none"; 
		            		qty.style.borderColor="";
		            		
		            	} 
		            	else if(orderMultipleQty != null && orderMultipleQty != 0)
		            	{	
		            		sourceOrderMulError.innerHTML = "Must be ordered in units of " + addComma(orderMultipleQty) +" "+orderMultipleUom;
		            		sourceOrderMulError.style.display = "inline"; 
		            		sourceOrderMulError.setAttribute("class", "notice");
		            		availabilityRow.style.display = "block"; 
		            		qty.style.borderColor="";
		            		
		            	} 
		            	else{
		            		availabilityRow.style.display = "block"; 
		            		qty.style.borderColor="";
		            	}
		            	}
		            	//End of BR1 XB 214
	            		Ext.Msg.hide();
	            		myMask.hide();
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
	                Ext.Msg.hide();
	            	myMask.hide();
	               
	            },
	        });
			}
			else{
				Ext.Msg.hide();
				myMask.hide();
				alert("Item UOM cannot be null to make a PnA call");
			}
		}
		//document.getElementById('addtocart_'+itemId).focus();
	}
	function resetQuantityErrorMessage(itemId)
	{
			var divId='errorMsgForQty_'+itemId;
			var divVal=document.getElementById(divId);
			divVal.innerHTML='';
	
	}
	function validateOrderMultiple(itemId) {
		resetQuantityErrorMessage(itemId);
		//var orderMultiple = getOrderMultiple(itemId);
		var orderMultiple = document.getElementById('orderMultiple_'+itemId).value;
		
		if(orderMultiple == "") {
			alert("Failed getting the order multiple for the Item. Please try adding the item later again..");
			return false;
		}
		var uomList = document.getElementById('itemUomList_'+itemId);
		var baseUOM = document.getElementById('baseUOMs_'+itemId).value;
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
		//XB 214 BR2 
		if(qty =="")// || qty == 0 )
		{
			//Changed the qty to ordermultiple for Jira 3922. pnA wasnt coming when qty is bank and orderMul flag is N.
			//qty=1;
			var qty = document.getElementById('orderMultiple_'+itemId).value;
			//alert("QTY in vaidateOrderMul===="+ qty);
		}
		//XB 214 BR4
		if( qty == 0 )
		{
			document.getElementById('Qty_'+itemId).style.borderColor="#FF0000";
			document.getElementById('Qty_'+itemId).focus();
			
			document.getElementById('errorMsgForQty_'+itemId).innerHTML = "Please enter a valid quantity and try again.";
			document.getElementById('errorMsgForQty_'+itemId).style.display = "inline-block"; 
			document.getElementById('errorMsgForQty_'+itemId).setAttribute("class", "error");
			document.getElementById('errorMsgForQty_'+itemId).setAttribute("style", "margin-right:5px;float:right;");	
			return false;
		}
		//End of XB 214
		var totalQty = selectedUomConv * qty;
		var ordMul = totalQty % orderMultiple;
		document.getElementById('Qty_'+itemId).style.borderColor="";
	/*	if(ordMul != 0 || totalQty==0)
		{		
			if(priceCheck == true){
				document.getElementById('errorMsgForQty_'+itemId).innerHTML = "Must be ordered in units of " + addComma(orderMultiple) + " " + baseUOM;
				document.getElementById('errorMsgForQty_'+itemId).style.display = "inline-block"; 
				document.getElementById('errorMsgForQty_'+itemId).setAttribute("class", "error");
				document.getElementById('errorMsgForQty_'+itemId).setAttribute("style", "margin-right:5px;float:right;");
			}
			else{
			document.getElementById('errorMsgForQty_'+itemId).innerHTML = "Please order in units of " + addComma(orderMultiple) + " " + baseUOM;
			document.getElementById('errorMsgForQty_'+itemId).style.display = "inline-block"; 
			document.getElementById('errorMsgForQty_'+itemId).setAttribute("class", "error");
			document.getElementById('errorMsgForQty_'+itemId).setAttribute("style", "margin-right:5px;float:right;");
			}
			return false;
		} Commented for XB 214 BR4 to remove the validation of requested Qty against the order multiple before PnA response
		else */ 
		if(orderMultiple > 1){
			if(priceCheck == true){
				document.getElementById('errorMsgForQty_'+itemId).innerHTML = "Must be ordered in units of " + addComma(orderMultiple) + " " + baseUOM;
				document.getElementById('errorMsgForQty_'+itemId).style.display = "inline-block"; 
				document.getElementById('errorMsgForQty_'+itemId).setAttribute("class", "notice");
				document.getElementById('errorMsgForQty_'+itemId).setAttribute("style", "margin-right:5px;float:right;");
			}
			else{
				document.getElementById('errorMsgForQty_'+itemId).innerHTML = "";
			}
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
            	//commented for jira 3253 document.getElementById('errorMsgForQty_'+itemId).style.display = "none";
    	    }
    	}
    }
 