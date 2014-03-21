$(document).ready(function () {
	$('#megamenu').megamenu({
		effect: 'show'
	});

	$('.mega-cat-2').click(function () {
		$this = $(this);
		var contentId = $this.attr('id') + '-content';
		var cat1Id = $this.attr('data-cat1Id');
		var $megacat3 = $('#megacat3-for-cat1-' + cat1Id);
		
		$megacat3.fadeOut(0);
		$megacat3.get(0).innerHTML = $('#' + contentId).get(0).innerHTML;
		$megacat3.fadeIn(200);
		
		return false;
	});
	
});

function openBrandPage(self) {
	$self = $(self);
	
	var path = $self.attr('data-path');
	var cat1name = $self.attr('data-cat1name');
	var cat2name = $self.attr('data-cat2name');
	
	alert('TODO: open brands page: path=' + path + ', cat1name=' + cat1name + ', cat2name=' + cat2name);
//	var brandsURL = $('#brandsURL');
//	brandsURL += '&path=' + encodeURIComponent(path);
//	brandsURL += '&cat1name=' + encodeURIComponent(cat1name);
//	brandsURL += '&cat2name=' + encodeURIComponent(cat2name);
//	window.location.href = brandsURL;
	
	return false;
}
