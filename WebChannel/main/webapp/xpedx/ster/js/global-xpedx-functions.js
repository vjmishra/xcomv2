$(document).ready(function() {
	$("#varous1").fancybox();
	$("#various2").fancybox();
	$("#various3").fancybox();
	$("#various4").fancybox();
	$("#various98").fancybox();
	$("#various99").fancybox();
	$("#import-lightbox").fancybox();
	$(".modal").fancybox();
	$(document).pngFix();
	$('.jq-shorten').shorten();
	
	/* $('#tree').checkboxTree();
	$('#collapseAllButtonsTree').checkboxTree({
			collapseAllButton: 'Collapse all',
			expandAllButton: 'Expand all'
	}); */
	
	//for div alt row zebra stripes
	$("div.mil-wrap-condensed-container:odd").css("background-color", "#fafafa");
	$("div.mil-wrap-condensed-container:even").css("background-color", "#fff");
	
	$(".btn-slide").click(function(){
		$("#panel").slideToggle("slow");
		$(this).toggleClass("slide"); return false;
	});
});
/*  roundy corners for IE */ 
	/* IE only */
//	DD_roundies.addRule('.mil-wrap-condensed', '6px 6px 0px 0px');
//	DD_roundies.addRule('.mil-wrap-condensed-bot', '0px 0px 6px 6px');

function expandPriceAndAvailability (el)
{
	jQuery(el).parents("dd").find('.price-and-availability').toggle();
	return false;
}

function showCost (el)
{
	if(el.innerHTML == '[Show]')
	  el.innerHTML = '[Hide]';
	else
	  el.innerHTML = '[Show]';
	  
	jQuery('#cost').toggle();
	
	return false;
}

function toggleCost(id)
{
   var e = document.getElementById(id);
   if(e.innerHTML = 'N/A')
	  e.style.display = 'none';
   else
	  e.style.display = 'block';

}
function restrictTextareaMaxLength(Object, maxLen){
 var txtTextArea = Object.value;
 
 if( txtTextArea.length > maxLen ) { 
 alert('Field cannot exceed '+ maxLen + ' characters');
 var txtTruckTextArea = txtTextArea.substring(0, maxLen);
 Object.value = txtTruckTextArea;
 return false;
 }
 
 return true;
 
}
