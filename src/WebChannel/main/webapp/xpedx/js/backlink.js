/**
 * JQuery for the back link for tools
 */

$(document).ready(function(){
	$(".back-resources").bind("click",function(){
		window.history.back();
	});
});