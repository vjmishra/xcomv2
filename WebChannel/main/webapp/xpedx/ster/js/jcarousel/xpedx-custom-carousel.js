// JavaScript Document

Ext.onReady(function () {

       jQuery('.jcarousel-skin-xpedx').jcarousel({
		   wrap:'circular'
	   });
	   
	   jQuery('.jcarousel-skin-xpedx li a:first').each(function(){
		   var url = $(this).attr("href");
		   $(this).after('<a href="' + url + '"></a>');

	   });

});