function equipment_showAttributes() {
	var e = document.getElementById("quality");
	var qualityNumber = Math.max(0, e.selectedIndex - 2);
	// Show attributes up to quality
	for(var i=0; i<qualityNumber; i++) {
		var style = document.getElementById("attribute"+i).style;
		style.display = null;
	}
	// Hide remaining attributes
	for(var i=qualityNumber; i<5; i++) {
		var style = document.getElementById("attribute"+i).style;
		style.display = "none";
	}
	
	if(e.selectedIndex <= 0) {
		var style = document.getElementById("baseAttribute").style;
		style.display = "none";
	} else {
		var style = document.getElementById("baseAttribute").style;
		style.display = null;
	}
	if(e.selectedIndex <= 1) {
		var style = document.getElementById("defenceAttribute").style;
		style.display = "none";
	} else {
		var style = document.getElementById("defenceAttribute").style;
		style.display = null;
	}
	if(e.selectedIndex <= 6) {
		style = document.getElementById("ancientAttribute").style;
		style.display = "none";
	} else {
		var style = document.getElementById("ancientAttribute").style;
		style.display = null;
	}
}