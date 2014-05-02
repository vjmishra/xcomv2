/**
 * This snippet of code emulates the HTML5 'placeholder' attribute, which we can't use because IE9 doesn't support it.
 * Usage: <input type="text" data-watermark="My placeholder text" />
 */
$(document).ready(function() {
	var inputs = $(":input[data-watermark]");
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
});
