$(document).ready(function() {
	initMegaMenu();
});

/*
 * Wraps the megamenu plugin initialization, for re-usability elsewhere (see getMegaMenu function).
 */
function initMegaMenu() {
	$('#megamenu').megamenu({
		effect: 'show'
	});
	
	$('.mega-cat-2').click(function() {
		$this = $(this);
		var contentId = $this.attr('id') + '-content';
		var cat1Id = $this.attr('data-cat1Id');
		var $megacat3 = $('#megacat3-for-cat1-' + cat1Id);
		
		$megacat3.fadeOut(0);
		$megacat3.get(0).innerHTML = $('#' + contentId).get(0).innerHTML;
		$megacat3.fadeIn(200);
		
		return false;
	});
}

/*
 * Opens the brand page for the given cat2 button clicked. 'self' parameter is the button clicked.
 */
function openBrandPage(self) {
	$self = $(self);
	
	var path = $self.attr('data-path');
	var cat1name = $self.attr('data-cat1name');
	var cat2name = $self.attr('data-cat2name');
	
	var brandsURL = $('#baseBrandsURL').val();
	brandsURL += '&path=' + encodeURIComponent(path);
	brandsURL += '&cat1name=' + encodeURIComponent(cat1name);
	brandsURL += '&cat2name=' + encodeURIComponent(cat2name);
	window.location.href = brandsURL;
	
	return false;
}

/*
 * Loads the megamenu asynchronously via ajax call.
 */
function getMegaMenu() {
	var url = $('#megaMenuAjaxURL').val();
	$.ajax({
		url: url,
		dataType: 'html',
		success: function(data) {
			$('#megamenu li:first').append(data);
			initMegaMenu();
		},
		failure: function() {
			// retry
			getMegaMenu();
		}
	});
}
