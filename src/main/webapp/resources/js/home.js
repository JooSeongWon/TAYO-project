'use strict'

var m = matchMedia("screen and (min-width: 1600px)");

	const mHtml = $("html");
	var page = 1;
	
	window.addEventListener("wheel", function(e){
		e.preventDefault();
	},{passive : false});
	
	
	$(window).on("wheel", function(e) {
		if(mHtml.is(":animated")) return;
		if(e.originalEvent.deltaY > 0) {
			if(page == 3) return;
			page++;
		} else if(e.originalEvent.deltaY < 0) {
			if(page == 1) return;
			page--;
		}
		var posTop =(page-1) * $(window).height();
		mHtml.animate({scrollTop : posTop});
	})

