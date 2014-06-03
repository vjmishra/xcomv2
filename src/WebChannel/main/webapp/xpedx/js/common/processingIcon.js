/*-----------Processing Icon Methods------------*/

function showProcessingIcon(){
			$(".loading-wrap").css('display','block');
			$(".loading-icon").css('display','block');
			$("body").css("overflow", "hidden");
		}

function hideProcessingIcon(){
			$(".loading-wrap").css('display','none');
			$(".loading-icon").css('display','none');
			$("body").css("overflow", "auto");
		}