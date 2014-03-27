/**
 * Add behavior to up/down arrows for scrolling quickly to top/bottom.
 * Assumes the primary div on the page has the id 'main'.
 */
$(function() {
	var $elem = $('#main');
	
	$('#nav_up').fadeIn('slow');
	$('#nav_down').fadeIn('slow');  
	
	$(window).bind('scrollstart', function(){
		$('#nav_up,#nav_down').stop().animate({'opacity':'0.2'});
	});
	$(window).bind('scrollstop', function(){
		$('#nav_up,#nav_down').stop().animate({'opacity':'1'});
	});
	
	$('#nav_down').click(
		function (e) {
			$('html, body').animate({scrollTop: $elem.height()}, "fast");
		}
	);
	$('#nav_up').click(
		function (e) {
			$('html, body').animate({scrollTop: '0px'}, "fast");
		}
	);
});
