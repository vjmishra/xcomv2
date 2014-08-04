$(document).ready(function() {

	function getSalesRepCustomers(allowRetry) {
		showProcessingIcon();
		var selectCustomerBaseURL = $('#selectCustomerBaseURL').val();
		var getSalesRepCustomerURL = $('#getSalesRepCustomerURL').val();
		var url = getSalesRepCustomerURL;

		$.ajax({
			type : 'GET',
			url : url,
			dataType : 'json',
			timeout: allowRetry ? 10000 : 60000, // retry after 10 seconds
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

					} else {
						// no filter, so use full list
						customerList = data.customerList;
					}

					if (customerList.length > 0) {
						html.push('	    <table id="mil-list-new" class="salespro-accounts">');
						html.push(' 		<thead>');
						html.push(' 			<tr>');
						html.push(' 				<th width="50%">Customer Name</th>');
						html.push(' 				<th width="25%"><p>Customer Number</p></th>');
						html.push(' 				<th width="25%">&nbsp;</th>');
						html.push(' 			</tr>');
						html.push(' 		</thead>');
						html.push(' 		<tbody>');

						for (var k = 0, len = customerList.length; k < len; k++) {
							var sRCustomer = customerList[k];
							var customerNo = sRCustomer.customerNo;
							var customerName = sRCustomer.customerName; 

							html.push('    		 <tr>');
							html.push('    		 	<td>');
							html.push('					<p class="customer">' , customerName , '</p>');
							html.push('				</td>');
							html.push('    		 	<td valign="top">' , customerNo , '</td>');
							html.push('    		 	<td valign="top">');
							html.push('    				<a href="' , selectCustomerBaseURL , '&selectedCustomer=' , customerNo , '">Select</a>');
							html.push('				</td>');
							html.push('    		 </tr>');
						}

						html.push(' 		</tbody>');
						html.push(' 	</table>');

					} else {
						// no results, so display empty result message
						html.push('<div class="alert alert-warning">No results found.</div>');
					}

					$customerListDiv.get(0).innerHTML = html.join('');
					hideProcessingIcon();
					$('.content-container').show();
				}

				displaySearchResults('');
				do_watermark(':input[data-watermark]');

			},
			error: function(resp, textStatus, xhr) {
				if (allowRetry && textStatus == 'timeout') {
					// retry - per eb-6895 we have observed that sometimes the api call stalls and refetching the data usually works
					getSalesRepCustomers(false);
				} else {
					if (console) { console.log('ajax error: resp = ', resp, '   textStatus = ', textStatus, '   xhr = ', xhr); }
					alert('Unable to retrieve customer list.\nPlease try again later.');
					window.location.href = $('#salesRepLoginURL').val();
				}
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
	}
	
	// initial fetch allows retry
	getSalesRepCustomers(true);
	
});
