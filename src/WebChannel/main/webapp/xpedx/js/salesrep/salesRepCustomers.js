$(document).ready(function() {
	showProcessingIcon();
	$('.content-container').hide();
	var getselectedCustURL = $('#selectedCustURL').val();
	var getSalesRepCustomerURL=$('#getSalesRepCustomerURL').val();
	var url = getSalesRepCustomerURL;
	$.ajax({
		type : 'GET',
		url : url,
		dataType : 'json',
		data : {

		},
		success : function(data) {
			var $customerListDiv = $('#listOfCustomers');

			$('.filterform').submit(function() {
				var filter = $('#searchTerm').val();
				displaySearchResults(filter);
				return false;
			});
			$('.resetlink').click(function() {
				$("input[class='filterinput']").val('');
				displaySearchResults('');
				return false;
			});

			function displaySearchResults(filter) {
				var customerList = [];
				var html = [];
				if (filter) {	
					for (var i = 0, len = data.customerList.length; i < len; i++) {
						var customer = data.customerList[i];
						if (customer.customerName.toLowerCase().indexOf(filter.toLowerCase()) != -1
								|| customer.customerNo.toLowerCase().indexOf(filter.toLowerCase()) != -1) {
							customerList.push(customer);
						}
					}

				} 
				else {
					// no filter, so use full list
					customerList = data.customerList;
				}

				html.push('	    <table id="mil-list-new" class="salespro-accounts">');
				html.push(' 		<thead>');

				html.push(' 			<tr>');
				html.push(' 				<th width="50%">Customer Name</th>');
				html.push(' 				<th width="25%"><p>Customer Number</p></th>');
				html.push(' 				<th width="25%">&nbsp;</th>');
				html.push(' 			</tr>');

				html.push(' 		</thead>');
				html.push(' 		<body>');

				for (var k = 0, len = customerList.length; k < len; k++) {
					var sRCustomer = customerList[k];
					var customerNo = sRCustomer.customerNo;
					var customerName = sRCustomer.customerName; 

					html.push('    		 <tr>');
					html.push('    		 	<td>');
					html.push('					<p class="customer">',customerName,'</p>');
					html.push('				</td>');
					html.push('    		 	<td valign="top">',customerNo,'</td>');
					html.push('    		 	<td valign="top">');
					html.push('    			<a href="' , getselectedCustURL , '&selectedCustomer=' , customerNo , '">Select</a></td>');
					html.push('    		 </tr>');
				}

				html.push(' 		</body>');
				html.push(' 	</table>');

				$customerListDiv.get(0).innerHTML = html.join('');
				hideProcessingIcon();
				$('.content-container').show();
			}

			displaySearchResults('');
			do_watermark(':input[data-watermark]');

		},
		error: function(resp, textStatus, xhr) {
			if (console) { console.log('ajax error: resp = ', resp, '   textStatus = ', textStatus, '   xhr = ', xhr); }
		}
		
});
	
	function do_watermark(selector) {
		var inputs = $(selector);
		for (var i = 0, len = inputs.length; i < len; i++) {
			var $input = $(inputs[i]);

			$input.val($input.attr("data-watermark"));
			$input.addClass('input-watermark-color');
			$input.focus(function() {
				var $this = $(this);
				if ($this.val() == $this.attr("data-watermark")) {
					$this.val('');
				}
				$this.removeClass('input-watermark-color');
			});
			$input.blur(function() {
				var $this = $(this);
				if ($this.val() == '') {
					$this.val($this.attr("data-watermark"));
					$this.addClass('input-watermark-color');
				}
			});
		}	
	}

	if ($("body").height() < $(window).height()) {
	    	$("#scroll-up-down").hide();
	      }
	
});
