    var focusDelay = 100; /* in millisecond. */
	function getLightBoxWithText(panelId) {
        svg_classhandlers_decoratePage();
		DialogPanel.show(panelId);
		var focusElement = Ext.get('shipComplete_0');
		if(focusElement != null){
            focusElement.focus(focusDelay);
		}
	}


	function updateFillQty(orderedQty,index){
		if(document.saveOrderLineShippingOption.fillQuantity.length){
			if(document.saveOrderLineShippingOption.shipComplete[index].checked){
				document.saveOrderLineShippingOption.fillQuantity[index].value = orderedQty;
			}else{
				document.saveOrderLineShippingOption.fillQuantity[index].value = 0;
			}
		}else{
			if(document.saveOrderLineShippingOption.shipComplete.checked){
				document.saveOrderLineShippingOption.fillQuantity.value = orderedQty;
			}else{
				document.saveOrderLineShippingOption.fillQuantity.value = 0;
			}

		}

	}

	function update(){
			document.saveOrderShippingOption.operation.value = "update";
			//document.saveOrderShippingOption.submit();
	}

  Ext.onReady(function() {
   var focusElement = Ext.get('optimizationType01')
   if(focusElement != null){
     focusElement.focus(focusDelay);
   }
  })