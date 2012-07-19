function showLocations() {
	var url = document.getElementById("showLocationsUrl").value;
	var customerId = document.getElementById("customerId").value;
	var sapCustomerID=document.getElementById("sapCustomerID").value;
	if(sapCustomerID == null || sapCustomerID == undefined || sapCustomerID.length == 0 )
		sapCustomerID=customerId;
	var organizationCode = document.getElementById("organizationCode").value;
	if(url!=null) {
		Ext.Msg.wait("Getting the Locations.... Please Wait..."); 
		Ext.Ajax.request({
		    url: url,
		    params: {
				shownCustomerId: sapCustomerID,
				organizationCode: organizationCode,
				},
		    method: 'POST',
			success: function (response, request){
				var responseText = response.responseText;
				if(responseText.indexOf("Error")>-1)
				{
					alert("Error Getting the Locations. Please try again later");
					Ext.MessageBox.hide();
					$.fancybox.close();
				}
				else
				{
					Ext.MessageBox.hide(); 
					
					document.getElementById('showLocationsDiv').innerHTML = responseText;
					
				     /*$('#collapseAllButtonsTree').checkboxTree({
		            collapseAllButton: '',
		            expandAllButton: ''
				     });*/
				     
				     /*	<!-- This not recognizing the for IE -->	 
				      var x = document.getElementById('showLocationsDiv').getElementsByTagName("script");   
					   for( var i=0; i < x.length; i++) {  
						 eval(x[i].text);  
					   } */ 
				}	
			},
			failure: function (response, request){
				alert("Error Getting the Locations. Please try again later");
				Ext.MessageBox.hide();
				$.fancybox.close();
			},
		});
	}
}
function submitForm() {
	document.getElementById("locatoinsForm").submit();
}