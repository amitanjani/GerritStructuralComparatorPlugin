structuralDiff.NavigateChanges = (function () {
	
	"use strict";
	
	var w,
		marker,
		counter,
		changes,
		offset,
		topoffset,
		className;
	
	return {
		gotoNextChange: function () {
			counter = navigateModule.getCounter();
			changes = navigateModule.getTotalChange();
			if (counter < changes) {
				counter = navigateModule.incrementCounter();
				if (counter === changes) {
					navigateModule.resetCounter();
				}
				marker = '#Marker' + counter;
			}
			offset = $(marker).offset();
			topoffset = offset.top;
			className =  $('#diffoutput').attr('class');
			if (className === "vScroll") {
				w = $(".vScroll");
				w.scrollTop(topoffset - w.height() / 2);
			} else {
				w = $(".vScrollFullScreen");
				w.scrollTop(topoffset - w.height() / 2);
			}
		},
		
		gotoPreviousChange: function () {
			counter = navigateModule.getCounter();
			changes = navigateModule.getTotalChange();
			if (counter >= 0) {
				counter = navigateModule.decrementCounter();
				if (counter === -1) {
					counter = navigateModule.updateCounter();
				}
				marker = '#Marker' + counter;
			}
			offset = $(marker).offset();
			topoffset = offset.top;
			className = $('#diffoutput').attr('class');
			if (className === "vScroll") {
				w = $(".vScroll");
				w.scrollTop(topoffset - w.height() / 2);
			} else {
				w = $(".vScrollFullScreen");
				w.scrollTop(topoffset - w.height() / 2);
			}
		}
	};
})();

var navigateModule = (function () {
	var changes = 0;
	var counter = 0;
	return {
		incrementCounter: function () {
			return counter++;
		},
		
		decrementCounter: function () {
			return --counter;
		},
		
		getCounter: function () {
			return counter;
		},
		
		resetCounter: function () {
			counter = 0;
		},
		
		updateCounter: function () {
			counter = changes - 1;
			return counter;
		},
		
		incrementChanges: function () {
			changes++;
		},
		
		resetChanges: function () {
			changes = 0;
		},
		
		getTotalChange: function () {
			return changes;
		} 
	}; 
})();

$(document).jkey('f6', function() {
	structuralDiff.NavigateChanges.gotoNextChange();
});

$(document).jkey('f7', function() {
	structuralDiff.NavigateChanges.gotoPreviousChange();
});

$(document).ready(function() {
	$(".hide_Rows").hide();
	$(".showDetails").click(function() {
    	$(".hide_Rows").toggle();
    	$(".showDetails").toggle();
    });
	
	navigateModule.resetCounter();
	
	$("#markerNext").click(function() {
		structuralDiff.NavigateChanges.gotoNextChange();
	});

	$("#markerPrev").click(function() {
		structuralDiff.NavigateChanges.gotoPreviousChange();
	});
});