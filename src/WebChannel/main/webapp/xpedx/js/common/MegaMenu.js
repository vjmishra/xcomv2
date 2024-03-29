$(document).ready(function() {
	if ($('#megaMenuAjaxURL').length == 0) {
		// page loaded with megamenu dom, so initialize it
		initMegaMenu();
	} else {
		// page loaded without megamenu dom, so fetch it via ajax
		getMegaMenu();
	}
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
		
		// change the active cat 2
		$this.parent('.cat-2-wrap').find('.mega-cat-2').removeClass('mega-cat-2-active');
		$this.addClass('mega-cat-2-active');
		
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
			var isLoadingMessageVisible = $('#megaMenuLoadingMessage').is(':visible');
			
			$('#megaMenuLoadingMessage').remove();
			$('#megamenu li:first').append(data);
			initMegaMenu();
			
			if (isLoadingMessageVisible) {
				// need to force a mouseenter event to trigger mega menu to show. this is necessary because we changed the DOM while menu was being hovered.
				$('#megamenu-top-catalog').mouseenter();
			}
		},
		failure: function() {
			// retry
			getMegaMenu();
		}
	});
}
