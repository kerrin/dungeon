// True to not use the debug panel
LOG_LEVEL_NONE = 0;
LOG_LEVEL_ERROR = 1;
LOG_LEVEL_DEBUG = 2;
LOG_LEVEL_TRACE = 3;
var debugLevel = LOG_LEVEL_NONE;

var pendingRequests = 0;
var wrongOrderPendingRequests = 0;

function stackTrace() {
    var err = new Error();
    return err.stack;
}

/**
 * Add a debug statement if the debug span exists
 * 
 * @param text
 */
function trace(text) {
	if(debugLevel >= LOG_LEVEL_TRACE) $("#debugLogsSpan").prepend(text+"<br/>");
}

/**
 * Add a debug statement if the debug span exists
 * 
 * @param text
 */
function debug(text) {
	if(debugLevel >= LOG_LEVEL_DEBUG) $("#debugLogsSpan").prepend(text+"<br/>");
}

/**
 * Add an error statement if the debug span exists
 * 
 * @param text
 */
function error(text) {
	if(debugLevel >= LOG_LEVEL_ERROR) $("#debugLogsSpan").prepend("<font color='red'>"+text+"</font><br/>");
}

var pendingRequestsList = {};
function debugPendingRequests(increase, from, pendingRequests) {
	if(debugLevel < LOG_LEVEL_DEBUG) return;
	var debugText = pendingRequests;
	for(var key in pendingRequestsList) {
		debugText += "<br/>" + key;
		if(pendingRequestsList[key] != 1) {
			debugText += " => " + pendingRequestsList[key];
		}
	}
	$("#debugPendingLoadsSpan").html(debugText);
	debug((increase?"+":"-")+from+" set requests to "+pendingRequests);
}

function decreasePendingRequests(from) {
	if(!(from in pendingRequestsList)) {
		pendingRequestsList[from] = 0;
	}
	if(pendingRequestsList[from] > 0) {
		pendingRequests--;
	} else {
		//debug(stackTrace());
		trace("Decrease for "+from+" not in pending");
		wrongOrderPendingRequests++;
	}
	pendingRequestsList[from]--;
	if(pendingRequestsList[from] == 0) {
		delete pendingRequestsList[from];
	}
	debugPendingRequests(false, from, pendingRequests);
}

function increasePendingRequests(from) {
	if(from in pendingRequestsList) {
		//debug(stackTrace());
		trace("Increase for "+from+" already pending");
	} else {
		pendingRequestsList[from] = 0;
	}
	pendingRequestsList[from]++;
	if(wrongOrderPendingRequests > 0) {
		wrongOrderPendingRequests--;
		debug("Increase for previous outof order decrease found");
	} else {
		pendingRequests++;
	}
	debugPendingRequests(true, from, pendingRequests);
}

function isTouchDevice() {
  return 'ontouchstart' in window        // works on most browsers 
      || navigator.maxTouchPoints;       // works on IE10/11 and Surface
}

function isFirefox() {
  return navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
}

/**
 * Load the web page returned by the URL in to the span id
 * 
 * @param spanId	Span to load content in to
 * @param url		URL to get the content to put in the span
 */
function loadSpan(spanId, url) {
	debug("loadSpan("+spanId+","+url+")");
	increasePendingRequests("loadSpan("+url+")");
	$("#"+spanId).load(url, function() {
		decreasePendingRequests("loadSpan("+url+")");
	});
}

/**
 * Load the web page returned by the URL in to the span id
 * 
 * @param spanId	Span to load content in to
 * @param url		URL to get the content to put in the span
 */
function loadSpansInOrder(spanId1, url1, spanId2, url2) {
	debug("loadSpansInOrder("+spanId1+","+url1+","+spanId2+","+url2+")");
	$("#"+spanId1).load(url1, function() {
		loadSpan(spanId2, url2);
    });
}

function parseDate(str) {
  var v=str.split(' ');
  return new Date(Date.parse(v[1]+" "+v[2]+", "+v[5]+" "+v[3]+" UTC"));
}

function closeError() {
	$("#errorPopUp").remove();
}

function hideDivById(divName) {
	$("#"+divName).hide("slow");
}

function hideDivByIdFast(divName) {
	$("#"+divName).hide();
}

function showDivById(divName) {
	$("#"+divName).show("slow");
}

function showDivByIdFast(divName) {
	$("#"+divName).show();
}

function hideDivByClass(divName) {
	$("."+divName).hide("slow");
}

function hideDivByClassFast(divName) {
	$("."+divName).hide();
}

function showDivByClass(divName) {
	$("."+divName).show("slow");
}

function showDivByClassFast(divName) {
	$("."+divName).show();
}

function showOnlyDivByClass(divName, start, end, showIndex) {
	for (i = start; i <= end; i++) {
		if(i == showIndex) {
			$("."+divName+i).show();
		} else {
			$("."+divName+i).hide();
		}
	}
}

function touchMe(elementId, selectId, selectFunc, modifyFunc) {
	var thisElement = document.getElementById(elementId);
	thisElement.addEventListener('touchend',function(event) {
		selectFunc(selectId);
		if(typeof(modifyFunc) !== 'undefined') {
			var selectElement = document.getElementById(selectId);
			modifyFunc(selectElement);
		}
		},false);
	
}

function toggleCheckboxById(selectId) {
	var newState = !$('#' + selectId).prop('checked');
	$('#' + selectId).prop('checked', newState);
}

function selectRadioButtonById(selectId) {
	$('#' + selectId).prop('checked', true);
}

function registerShowDiv(linkDivName, showDivName, oArg) {
	$("#"+linkDivName).bind('click', function (event) {
	    if(typeof(oArg.absLeft) === 'undefined') {
	    	oArg.absLeft = event.pageX;
	    }
	    if(typeof(oArg.offsetLeft) === 'undefined') {
	    	oArg.offsetLeft = 0;
	    }
	    $("#"+showDivName).css('left',oArg.absLeft+oArg.offsetLeft);
	    if(typeof(oArg.absTop) === 'undefined') {
	    	oArg.absTop = event.pageY;
	    }
	    if(typeof(oArg.offsetTop) === 'undefined') {
	    	oArg.offsetTop = 0;
	    }
	    $("#"+showDivName).css('top',oArg.absTop+oArg.offsetTop);	    
	    $("#"+showDivName).css('display','inline');     
	    $("#"+showDivName).css("position", "absolute");
	    $("#"+showDivName).show("slow");
	});
}



//Countdown timer code
function getTimeRemaining(endTime){
var total = Date.parse(endTime) - new Date();
var seconds = Math.floor( (total/1000) % 60 );
var minutes = Math.floor( (total/1000/60) % 60 );
var hours = Math.floor( (total/(1000*60*60)));
return {
 'total': total,
 'hours': hours,
 'minutes': minutes,
 'seconds': seconds
};
}

function initializeClock(id, endtime, doneFrame, doneUrl, doneSpanId){
	var clock = document.getElementById(id);
	var hoursSpan = clock.querySelector('.hours');
	var minutesSpan = clock.querySelector('.minutes');
	var minutesPlusOneSpan = clock.querySelector('.minutesPlusOne');
	var secondsSpan = clock.querySelector('.seconds');
	function updateClock(){
		var t = getTimeRemaining(endtime);
		if(t.total<=0){
			clearInterval(timeinterval);
			clock.innerHTML = '<font color="green">Done</font>';
			if(doneFrame != undefined && doneUrl != undefined) {
				loadSpan(doneFrame, doneUrl);
			} else {
				if(doneSpanId != undefined && doneSpanId > 0) {
					hideDivByIdFast("pending_"+ doneSpanId);
					showDivByIdFast("finished_"+ doneSpanId);
				}
			}
		} else {
		    hoursSpan.innerHTML = ('0' + t.hours).slice(-2);
		    minutesSpan.innerHTML = ('0' + t.minutes).slice(-2);
		    secondsSpan.innerHTML = ('0' + t.seconds).slice(-2);
		    if(minutesPlusOneSpan) minutesPlusOneSpan.innerHTML = parseInt(('0' + (t.minutes+(t.hours*60))).slice(-2))+1;
		}
	}
	// Check if the time is already expired
	var t = getTimeRemaining(endtime);
	if(t.total<=0){
		clearInterval(timeinterval);
		clock.innerHTML = '<font color="green">Done</font>';
	} else {
		updateClock(); // run function once at first to avoid delay
		var timeinterval = setInterval(updateClock,1000);
	}
}