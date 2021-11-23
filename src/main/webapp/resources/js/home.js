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
			if(page == 2) return;
			page++;
		} else if(e.originalEvent.deltaY < 0) {
			if(page == 1) return;
			page--;
		}
		var posTop =(page-1) * $(window).height();
		mHtml.animate({scrollTop : posTop});
	})
	


//function createObserver() {
//	let observer; 
//	
//	let options = {
//			root: document.body,
//			rootMargin: '0px',
//			threshold: buildThresholdList()
//	}
//	
//	observer = new IntersectionObserver(handleIntersect, options);
//	observer.observe(body)
//}
//
//
//window.addEventListener("load" , (event) => {
//	
//	createObserver();
//	
//},false);
//
//function buildThresholdList() {
//	  let thresholds = [];
//	  let numSteps = 20;
//
//	  for (let i=1.0; i<=numSteps; i++) {
//	    let ratio = i/numSteps;
//	    thresholds.push(ratio);
//	  }
//
//	  thresholds.push(0);
//	  return thresholds;
//}
//
//function handleIntersect(entries, observer) {
//	  entries.forEach((entry) => {
//	    if (entry.intersectionRatio > prevRatio) {
//	      entry.target.style.backgroundColor = increasingColor.replace("ratio", entry.intersectionRatio);
//	    } else {
//	      entry.target.style.backgroundColor = decreasingColor.replace("ratio", entry.intersectionRatio);
//	    }
//
//	    prevRatio = entry.intersectionRatio;
//	  });
//}


