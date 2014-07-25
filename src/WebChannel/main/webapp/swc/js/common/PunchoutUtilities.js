function encryptUserIdAndPassword() {
	var url = $('#punchoutCreateDataParamURL').val();
	
	var showError = function(message) {
		$('#ajax-output').get(0).innerHTML = '<span style="color:red;">' + message + '</span>';
	};
	
	$('#ajax-output').get(0).innerHTML = '';
	
	showProcessingIcon();
	$.ajax({
		url: url,
		type : 'POST',
		dataType: 'json',
		data: {
			userId: $('#userId').val(),
			password: $('#password').val()
		},
		success: function(data) {
			console.log('data = ' , data);
			if (data.url) {
				var html = [];
				html.push('<div>This is the URL to give to the customer. Their procurement system will append an extra "hook_url".</div>');
				html.push('<input type="text" id="punchoutUrl" value="' , data.url , '" style="width:99%;" />');
				$('#ajax-output').get(0).innerHTML = html.join('');
			} else {
				showError('Unable to generate URL');
			}
		},
		failure: function (response, request) {
			// TODO display error
			if (console) { console.log('response = ' , response); }
			showError('Error generating URL: ' + response);
		},
		complete: function() {
			hideProcessingIcon();
		}
	});
}
