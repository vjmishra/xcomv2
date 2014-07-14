     function invalidateSessionAndSubmit(invalidateSessionURL){       
            Ext.Ajax.request({
				url : invalidateSessionURL,
				method: 'GET',       
				success: function ( response, request ) {
					document.punchOutForm.submit();       
				},
				failure: function ( response, request ) {
					document.punchOutForm.submit(); 
				}    
	        });
     }   