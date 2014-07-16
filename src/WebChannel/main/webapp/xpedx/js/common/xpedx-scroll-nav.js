$(document).ready(function() {
	var refreshScrollButtons = function() {
		var showScroll = $("body").height() >= $(window).height();
		
		var $main = $('#main');
		if ($main.length > 0) {
			if (($main.offset().left + $main.width()) <= 1150) {
				// hide scrollbars when content becomes narrow enough that scrollbars would overlay content
				showScroll = false;
			}
		}
		
		if (showScroll) {
			$("#scroll-up-down").show();
		} else {
			$("#scroll-up-down").hide();
		}
	};
	
	refreshScrollButtons();
	setInterval(refreshScrollButtons, 1000);
});
