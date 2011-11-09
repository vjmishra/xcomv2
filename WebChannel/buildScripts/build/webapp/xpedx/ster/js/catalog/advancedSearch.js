function openAttributePickerLightBox(actionURL, selectFieldId)
{
    Ext.Ajax.request({
		url:  actionURL,
		method : "POST",
		success : function(response, options){
			eval(response.responseText);
		},
		params : {"selectFieldId" : selectFieldId}
	});
	/*Ext.get('ajax-attributePicker').load({
         url :  actionURL,
         method: 'GET',
         callback: function(el,success){
	        DialogPanel.toggleDialogVisibility('attribute_Picker');
	        svg_classhandlers_decoratePage();
             if(!success)
                 alert("failure");
         }
     });*/
}
function resetPage(selectbox_1,selectbox_2,selectbox_3, formName){
			var i;
			/*if(document.getElementById(selectbox_1)){
			for(i=document.getElementById(selectbox_1).options.length-1;i>=0;i--){
					document.getElementById(selectbox_1).remove(i);
				}}
			if(document.getElementById(selectbox_2)){
			for(i=document.getElementById(selectbox_2).options.length-1;i>=0;i--){
					document.getElementById(selectbox_2).remove(i);
			}}
			if(document.getElementById(selectbox_3)){
			for(i=document.getElementById(selectbox_3).options.length-1;i>=0;i--){
					document.getElementById(selectbox_3).remove(i);
			}} */
			document.advancedSearch_form.reset();
			document.advancedSearch_form.searchTerm_1.value="";
			document.advancedSearch_form.searchTerm_2.value="";
			document.advancedSearch_form.searchTerm_3.value="";
			document.advancedSearch_form.priceRange_From.value="";
			document.advancedSearch_form.priceRange_To.value="";
			document.getElementById('indexField_1').options[0].selected= 'true';
			document.getElementById('indexField_2').options[0].selected= 'true';
			document.getElementById('indexField_3').options[0].selected= 'true';
			if( document.getElementById('path') != null ){
				document.getElementById('path').options[0].selected= 'true';
			}

			var errors = false;
	        form = document.getElementById(formName);
   			form.isSupersededIncluded.checked= false;
	        clearErrorMessages(form);
            clearErrorLabels(form);

}

/* function selectAll(selectbox_1, selectbox_2, selectbox_3,selectAll) {
			// have we been passed an ID
			var attributeString_1 = "";
			var attributeString_2 = "";
			var attributeString_3 = "";
			 if ( (typeof selectbox_1 == "string") && (typeof selectbox_2 == "string") && (typeof selectbox_3 == "string") ) {
			//if (typeof selectbox_1 == "string"){
				selectbox_1 = document.getElementById(selectbox_1);
				 selectbox_2 = document.getElementById(selectbox_2);
				 selectbox_3 = document.getElementById(selectbox_3);
			}
			if (selectbox_1.type == "select-multiple") {
				for (var i = 0; i < selectbox_1.options.length; i++) {
					selectbox_1.options[i].selected = selectAll;
					if(attributeString_1.length == 0 ){
						attributeString_1 = selectbox_1.options[i].value;
					}
					else{
						attributeString_1=attributeString_1 + ";" + selectbox_1.options[i].value;
					}
				}
				document.advancedSearch_form.productFeature_1.value=attributeString_1;

			}
			if (selectbox_2.type == "select-multiple") {
				for (var i = 0; i < selectbox_2.options.length; i++) {
					selectbox_2.options[i].selected = selectAll;
					if(attributeString_2.length == 0 ){
						attributeString_2 = selectbox_2.options[i].value;
					}
					else{
						attributeString_2=attributeString_2 + ";" + selectbox_2.options[i].value;
					}

				}
				document.advancedSearch_form.productFeature_2.value=attributeString_2;

			}
			if (selectbox_3.type == "select-multiple") {
				for (var i = 0; i < selectbox_3.options.length; i++) {
					selectbox_3.options[i].selected = selectAll;
					if(attributeString_3.length == 0 ){
						attributeString_3 = selectbox_3.options[i].value;
					}
					else{
						attributeString_3=attributeString_3 + ";" + selectbox_3.options[i].value;
					}
				}
				document.advancedSearch_form.productFeature_3.value=attributeString_3;

			}
		} */

		/*
		 function resetPage(selectbox_1,selectbox_2,selectbox_3){
			var i;
			for(i=document.getElementById(selectbox_1).options.length-1;i>=0;i--){
					document.getElementById(selectbox_1).remove(i);
				}
			for(i=document.getElementById(selectbox_2).options.length-1;i>=0;i--){
					document.getElementById(selectbox_2).remove(i);
				}
			for(i=document.getElementById(selectbox_3).options.length-1;i>=0;i--){
					document.getElementById(selectbox_3).remove(i);
				}
			document.advancedSearch_form.reset();
		} */

		function performOperation(selectbox_1,selectbox_2,selectbox_3){
		/*selectAll(selectbox_1, selectbox_2, selectbox_3, true); */
			//alert("inside perform operation");
		}

		function getLightBoxWithText(panelId) {
			DialogPanel.show(panelId);
		}

		function removeOptions(selectbox){

		//alert("inside remove options");
			var i;
			for(i=document.getElementById(selectbox).options.length-1;i>=0;i--){
				if(document.getElementById(selectbox).options[i].selected)
				{
				 document.getElementById(selectbox).remove(i);
				}
			}
		}

    function openSearchTipsLightBox(url){
	     javascript:window.open(url, 'searchTips','top=0, left=250, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');
	    }