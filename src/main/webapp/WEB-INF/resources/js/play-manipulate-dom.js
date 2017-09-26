// debugPendingRequests(false, '***Initialisation***', pendingRequests);
var runningZombieCheck = 0;
var waitingZombies = 0;
var initialiseCompareReverseLock = 0;

/**
 * Initialise the panels
 * 
 * @param contextPath	Context root path for site (e.g /dungeon)
 * @param dungeonId		Dungeon details to load in to dungeon panel (if any)
 * @param characterId	Character details to load in to character details panel (if any)
 */
function initPlay(contextPath, dungeonId, characterId) {
	debug("initPlay");
	httpGet(contextPath+"/play/init?touchScreen="+(isTouchDevice()?'true':'false'));
	/*
	if(isTouchDevice()) {
		// Stop the annoying touch move as it interferes with dragging
		document.addEventListener('touchmove', preventDefault, false);
	}
	*/
	var typeParams = "?hardcore="+(hardcore?"1":"0")+"&ironborn="+(ironborn?"1":"0");
	if(dungeonId > 0) {
		loadSpan("dungeonsFrame", contextPath+"/play/dungeon/"+dungeonId+typeParams+"&fullPage=true");
	} else {
		loadSpan("dungeonsFrame", contextPath+"/play/dungeon"+typeParams);
	}
	loadSpan("charactersFrame", contextPath+"/play/character"+typeParams);
	if(characterId > 0) {
		loadSpan("characterDetailsFrame", contextPath+"/play/character/"+characterId+typeParams);
		loadSpan("characterDetailsSummaryFrame", contextPath+"/play/character/"+characterId+"/summary"+typeParams);
	} else {
		loadSpan("characterDetailsFrame", contextPath+"/play/character/create"+typeParams);
		$("#characterDetailsSummaryFrame").empty();
	}
	loadSpan("enchantFrame", contextPath+"/play/enchant"+typeParams);
	loadSpan("stashFrame", contextPath+"/play/stash"+typeParams);
	loadSpan("salvageFrame", contextPath+"/play/salvage"+typeParams);
	initSummary(contextPath, typeParams);
}

/**
 * Initialise the summary panel
 * 
 * @param contextPath	Context root path for site (e.g /dungeon)
 * @param typeParams	String containing View Type parameters (e.g. '?hardcore=false&ironborn=true')
 */
function initSummary(contextPath, typeParams) {
	debug("initSummary");
	loadSpan("summaryFrame", contextPath+"/play/summary"+typeParams);
}

/**
 * Pop Up the dungeon-pedia
 * 
 * @param contextPath	Context root path for site (e.g /dungeon)
 */
function createDungeonPedia(contextPath) {
	$("#outerSpan_dungeonpediaFrame").empty();
	$("#outerSpan_dungeonpediaFrame").append("<div class='dungeonpediaContentFrame' style='pointer-events: all;'></div>");
	$(".dungeonpediaContentFrame").load(contextPath+"/dungeonpedia");
	/*
	if(isTouchDevice()) {
		$("body").delegate('span','touchmove',function(e){
		    e.stopPropagation();
		});
	}
	*/
}

/**
 * Close the dungeon-pedia panel
 */
function closeDungeonPedia() {
	$("#outerSpan_dungeonpediaFrame").empty();
	/*
	if(isTouchDevice()) {
		$("body").delegate('span','touchmove',function(e){
			e.preventDefault();
		});
	}
	*/
}

/**
 * Close the dungeon-pedia panel
 */
function closeMessages(contextPath, typeParams) {
	$("#outerSpan_messagesFrame").empty();
	$("#outerSpan_messagesFrame").hide();
	$.get(contextPath+"/play/closeMessages"+typeParams,
		// DATA: none		
		// Success function
		function( data ) {
		}
	);
}

/**
 * Make a HTTP request to the web server
 * 
 * @param url	URL to request
 * @returns		The response
 */
function httpGet(url)
{
	debug("httpGet:"+url);
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", url, false ); // false for synchronous request
    xmlHttp.send( null );
    return xmlHttp.responseText;
}

function showMessages(contextPath, typeParams) {
	showMessagesWait(contextPath, 0, typeParams);
}

function showMessagesWait(contextPath, count, typeParams) {
	if(pendingRequests == 0) {
		showMessagesDo(contextPath, typeParams);
	} else if(count < 1000) {
		setTimeout(function() {
			showMessagesWait(contextPath, ++count, typeParams);
        }, 100); // wait 100 ms
	}
}

function showMessagesDo(contextPath, typeParams) {
	if($(".messagesContentFrame").length == 0) {
		increasePendingRequests("showMessages");
		$("#outerSpan_messagesFrame").append("<div id='messagesContentFrame' class='messagesContentFrame'><div>");
		$(".messagesContentFrame").load(contextPath+"/play/displayMessages"+typeParams);
		$("#outerSpan_messagesFrame").show();
		decreasePendingRequests("showMessages");
	}
}

/**
 * Reloads the page, adding the character and dungeon ids to the URL request
 * 
 * @param url          The URL to reload as
 */
function reloadPage(url) {
	//debug("reloadPage("+url+")");
	var finalUrl = url+'&charId='+charId+'&dungeonId='+dungeonId;
	window.location.replace(finalUrl);
}

/**
 * Check if all the equipment is gone, then reload the dungeon panel if we can
 * 
 * @param contextPath	Context root path for site (e.g /dungeon)
 */
function reloadDungeonPanel(contextPath) {
	if(dungeonId > 0 && $("[id^=equipmentdungeonsFrame]").length < 1) {
		loadSpan('dungeonsFrame',contextPath+'/play/dungeon/'+dungeonId);
	}
}

/**
 * Check if all the equipment is gone, then reload the dungeon panel if we can
 * 
 * @param contextPath	Context root path for site (e.g /dungeon)
 */
function reloadMessagesPanel(contextPath, typeParams) {
	if($("[id^=equipmentmessagesContentFrame]").length < 1) {
		loadSpan('messagesContentFrame',contextPath+'/play/displayMessages'+typeParams);
	}
}

/**
 * Reload the character panel
 * 
 * @param contextPath	Context root path for site (e.g /dungeon)
 */
function reloadCharacterDetailsPanel(contextPath) {
	if(charId > 0) {
		loadSpan('characterDetailsFrame',contextPath+'/play/character/'+charId);
	}
}

/**
 * Reload the character details summary panel
 * 
 * @param contextPath	Context root path for site (e.g /dungeon)
 */
function reloadCharacterDetailsSummaryPanel(contextPath) {
	if(charId > 0) {
		loadSpan('characterDetailsSummaryFrame',contextPath+'/play/character/'+charId+'/summary');
	}
}

/**
 * Swap the inner html of the two spans
 * 
 * @param spanId1
 * @param spanId2
 */
function swapSpanHtml(spanId1, spanId2) {
	var html1 = $("#"+spanId1).html();
	var html2 = $("#"+spanId2).html();
	$("#"+spanId1).html(html2);
	$("#"+spanId2).html(html1);
	
	// Make sure the drop location equipment details has been rendered
	$("#"+spanId2).qtip('toggle', true);
	
	// Set the detail light boxes to swap
	html1 = $("#lb_"+spanId1).html();
	html2 = $("#lb_"+spanId2).html();
	$("#lb_"+spanId1).html(html2);
	$("#lb_"+spanId2).html(html1);
}

/**
 * Initialise all the compare popups once they exist in the DOM for each char slot listed for all the char slots
 * 
 * @param spanId			Span id of the equipment to put in the compare popups
 * @param charSlotNames		Char slots to initialise the compares of
 */
function initialiseCompareWaitAll(spanId, charSlotNames) {
	debug("initialiseCompareWaitAll("+spanId+","+charSlotNames+")");
	var charSlotNamesList = charSlotNames.split(' ');
	charSlotNamesList.forEach(function(charSlotName) {
		initialiseCompareWait(spanId, charSlotName, 0)
	});
}

/**
 * Initialise all the compare popups once they exist in the DOM for the char slot listed
 * 
 * @param spanId			Span id of the equipment to put in the compare popups
 * @param charSlotNames		Char slots to initialise the compares of
 * @param count				How many times we have waited and then checked for the span to exist
 */
function initialiseCompareWait(spanId, charSlotName, count) {
	debug("initialiseCompareWait("+spanId+","+charSlotName+","+count+")");
	if($('#lb_'+spanId).length > 0) {
		initialiseCompareInner(spanId, charSlotName);
	} else {
		if(count < 1000) {
			setTimeout(function() {
				initialiseCompareWait(spanId, charSlotName, ++count);
	        }, 100); // wait 100 ms
		} else {
			decreasePendingRequests("setUpEquipmentPopups");
		}
	}
}

/**
 * The function that actually finds the compare spans and sets them to the passed spanId content
 * 
 * @param spanId			Span id of the equipment to mouse over and then put in the compare popups
 * @param charSlotNames		Char slots to initialise the compares of
 */
function initialiseCompareInner(spanId, charSlotId) {
	debug("initialiseCompareInner("+spanId+","+charSlotId+")");
	// Get the character equipment pop up for this equipments char slot
	var spanIdCompare = $('.charEquipment_'+charSlotId);
	
	// Check it exists
	if(spanIdCompare == null || spanIdCompare.length < 1) return true; // same as continue in a sensible language
	
	// Create the compare
	var lb_popup = spanIdCompare.attr('id');
	if(lb_popup == null) return true;
	if(lb_popup.match("lb_")) lb_popup = lb_popup.substring(3);
	swapSpanCompareHtml(spanId, lb_popup, true);
	decreasePendingRequests("initialiseCompare");
}

/**
 * Loops over the char slots and finds the compare spans and sets them to the passed spanId content
 * 
 * @param spanId			Span id of the equipment to mouse over and then put in the compare popups
 * @param charSlotNames		Char slots to initialise the compares of
 */
function initialiseCompare(spanId, charSlotIds) {
	debug("initialiseCompare("+spanId+","+charSlotIds+")");
	var charSlotNamesList = charSlotNames.split(' ');
	charSlotNamesList.forEach(function(charSlotId) {
		initialiseCompareInner(spanId, charSlotId);
	});
}

/**
 * Find the equipment slot on the character and set the spanId to it
 * 
 * @param replaceSpanId		Span to replace
 * @param charSlotIds		Ids of the char slots to replace
 */
function initialiseCompareReverse(replaceSpanIdStart, replaceSpanIdEnd, charSlotIds) {
	initialiseCompareReverseLock++;
	debug("initialiseCompareReverse(" + replaceSpanIdStart + "," + replaceSpanIdEnd + ",[" + charSlotIds + "])");
	// Get the span to replace by it's id
	charSlotIds.forEach(function(charSlotId) {
		var charslotClass = "";
		var compareSpan = $('[id^='+replaceSpanIdStart + charSlotId + replaceSpanIdEnd+"]");
		if(compareSpan == null) {
			error(replaceSpanIdStart + charSlotId + replaceSpanIdEnd + " not found");
			return;
		}
		compareSpan.empty();
		
		// Get the character equipment pop up for this equipments char slot from it's class name
		var spanIdComparedTo = $('.charEquipment_charslot_'+charSlotId);
		
		// Check it exists
		if(spanIdComparedTo != null && spanIdComparedTo.length >= 1) {
			trace('Copying html from '+spanIdComparedTo.attr('id')+" to "+compareSpan.attr('id'));
			trace('Size before: '+compareSpan.html().length);
			// Add the new compare
			compareSpan.append(spanIdComparedTo.html());
			trace('Size after: '+compareSpan.html().length);

			charslotClass = "charslot_"+charSlotId;
		} else {
			trace('Class charEquipment_charslot_'+charSlotId+" not found");
		}
		trace('Added class '+charslotClass+" to " + compareSpan.attr('id'));
		compareSpan.attr('class', charslotClass);
	});
	initialiseCompareReverseLock--;
}

/**
 * Initialise all the compare popups from the span id once they exist in the DOM for the char slot listed
 * 
 * @param replaceSpanId		Span to replace
 * @param charSlotIds		Ids of the char slots to replace
 * @param count				How many times we have waited and then checked for the span to exist
 */
function initialiseCompareReverseWait(replaceSpanIdStart, replaceSpanIdEnd, charSlotIds, count) {
	if(pendingRequests < 1 && initialiseCompareReverseLock < 1) {
		var spanCount = 0;
		charSlotIds.forEach(function(charSlotId) {
			if($('#'+replaceSpanIdStart+charSlotId+replaceSpanIdEnd).length > 0) {
				spanCount++;
				trace("Found "+replaceSpanIdStart+charSlotId+replaceSpanIdEnd);
			}
		});
		if(spanCount >= charSlotIds.length) {
			initialiseCompareReverse(replaceSpanIdStart, replaceSpanIdEnd, charSlotIds);
		} else {
			if(count < 1000) {
				setTimeout(function() {
					initialiseCompareReverseWait(replaceSpanIdStart, replaceSpanIdEnd, charSlotIds, ++count);
		        }, 100); // wait 100 ms
			}
		}
	} else {
		if(count < 1000) {
			setTimeout(function() {
				initialiseCompareReverseWait(replaceSpanIdStart, replaceSpanIdEnd, charSlotIds, ++count);
	        }, 100); // wait 100 ms
		}
	}
}

/**
 * Initialise all the compare popups from the span id once they exist in the DOM for the char slot listed
 * 
 * @param validSlotIds		All the slot ids that are valid
 * @param replaceSpanId		Span to replace
 */
function setUpCompare(validSlotIds, replaceSpanIdStart, replaceSpanIdEnd) {
	trace("setUpCompare("+validSlotIds + "," + replaceSpanIdStart + "," + replaceSpanIdEnd + ")");
	// Loop through valid slot ids, 
	// copying the character equipment in class charEquipment_charslot_<slotId> to the spanId
	var charSlotIdsList = $.parseJSON(validSlotIds);
	
	initialiseCompareReverseWait(replaceSpanIdStart, replaceSpanIdEnd, charSlotIdsList, 0);
}

/**
 * Remove all compare pop ups
 */
function clearCompares() {
	debug("clearCompares");
	
	$('[class^="charslot_"]').html("");
}

/**
 * Swap the compare pop-up spans inner html and classes
 * 
 * @param spanId1	Span to swap
 * @param spanId2	Span to swap
 */
function swapSpanCompareHtml(spanId1, spanId2, showCompare) {
	debug("swapSpanCompareHtml("+spanId1+"," + spanId2 + ","+(showCompare?'true':'false')+")");
	if(showCompare) return;
	var charSlotNamesJQ;
	var html;
	if(spanId1 == null || spanId2 == null) return;
	
	var lbCharslot1 = $("[id ^=lb_charslot_][id $="+spanId1+']');
	var lbCharslot2 = $("[id ^=lb_charslot_][id $="+spanId2+']');
	var lbCharslotClass1 = lbCharslot1.attr('class');
	var lbCharslotClass2 = lbCharslot2.attr('class');
	
	if(spanId1.indexOf('equipmentcharacterDetailsFrame') >= 0 || 
			spanId1.indexOf('emptyequipmentcharacterDetailsFrame') >= 0) {
		var lb_span1 = $("#lb_"+spanId1);
		// Don't swap if the equipment slot was empty
		var lb_span_class1 = lb_span1.attr('class');
		if(lb_span_class1 != null && lb_span_class1.indexOf('charslot_none') < 0) {
			debug("Span 1 Character Equipment");
			html = lb_span1.html();
			var popUp = $("#lb_"+spanId2);
			if(popUp) {
				debug("Span 2 is popup");
				var siblings = popUp.siblings("[class^=charslot]");
				charSlotNamesJQ = siblings.attr('class');
				lbCharslotClass1 = lb_span_class1.substr(lb_span_class1.indexOf('charslot_'));
			}
		}
	}
	// Try span 2 then
	if(html == null || html == '&nbsp;' || charSlotNamesJQ == null) {
		if(spanId2.indexOf('equipmentcharacterDetailsFrame') >= 0 || 
				spanId2.indexOf('emptyequipmentcharacterDetailsFrame') >= 0) {
			debug("Span 2 Character Equipment");
			var lb_span2 = $("#lb_"+spanId2);
			// Don't swap if the equipment slot was empty
			var lb_span_class2 = lb_span2.attr('class');
			if(lb_span_class2 != null && lb_span_class2.indexOf('charslot_none') < 0) {
				var html2 = lb_span2.html();
				// check if this one (spanId2) is better than spanId1
				if(html == null || // No HTML yet
						// or html was empty and pop up char slots set
						(html == '&nbsp;' && charSlotNamesJQ != null && html2 != null)) {
					debug("Found better html");
					html = html2;
					var popUp = $("#lb_"+spanId1);
					if(popUp) {
						debug("Span 1 is popup");
						var siblings = popUp.siblings("[class^=charslot]");
						charSlotNamesJQ = siblings.attr('class');
						lbCharslotClass2 = lb_span_class2.substr(lb_span_class2.indexOf('charslot_'));
					}
				}
			}
		}
	}
	
	debug("Html:"+(html==null?'null':html.length));
	debug("charSlotNamesJQ: "+(charSlotNamesJQ==null?"null":charSlotNamesJQ));
	if(html != null && charSlotNamesJQ != null) {
		debug("Swapping "+ spanId1 + " and " + spanId2);
		var charSlotNames = charSlotNamesJQ.split(' ');
		charSlotNames.forEach(function(charSlotName) {
			$("."+charSlotName).html(html);
			$("."+charSlotName).css('border', '1px solid green');
		});
		
		
		$("[id ^=lb_charslot_][id $="+spanId1+"]").attr('class', lbCharslotClass2);
		$("[id ^=lb_charslot_][id $="+spanId2+"]").attr('class', lbCharslotClass1);
	} else {
		// Not involving the character equipment changing, so just swap the compare frames
		if(spanId1.indexOf('equipmentstashFrame') >= 0 || 
				spanId1.indexOf('emptyStash') >= 0 || 
				spanId2.indexOf('equipmentstashFrame') >= 0 || 
				spanId2.indexOf('emptyStash') >= 0) {
			debug("Swapping compare only");
			var html1 = lbCharslot1.html();
			var html2 = lbCharslot2.html();
			
			if(html2 == null) {
				$("[id ^=lb_charslot_][id $="+spanId1+"]").html('');
			} else {
				$("[id ^=lb_charslot_][id $="+spanId1+"]").html(html2);
			}
			if(html1 == null) {
				$("[id ^=lb_charslot_][id $="+spanId2+"]").html('');
			} else {
				$("[id ^=lb_charslot_][id $="+spanId2+"]").html(html1);
			}
			
			$("[id ^=lb_charslot_][id $="+spanId1+"]").attr('class', lbCharslotClass2);
			$("[id ^=lb_charslot_][id $="+spanId2+"]").attr('class', lbCharslotClass1);
		} else {
			debug("No swap");
		}
	}
}

/**
 * Swap the span ids and reload the dungeon panel if needed
 * 
 * @param spanId1				Span to swap
 * @param spanId2				Span to swap
 * @param dungeonPanelReload	If this is a dungeon item, so we may need to reload the dungeon panel 
 * @param contextPath			Context root path for site (e.g /dungeon)
 */
function swapSpanId(spanId1, spanId2, dungeonPanelReload, contextPath) {
	var regexGetNumber = new RegExp('([0-9]+)$');
	
	var matches = spanId1.match(regexGetNumber);
	if(matches == null) return;
	var replace1 = matches[1];
	var regexReplace1 = new RegExp(replace1, "g");
	
	matches = spanId2.match(regexGetNumber);
	if(matches == null) return;
	var replace2 = matches[1];
	var regexReplace2 = new RegExp(replace2, "g");
	
	var attr1 = $("#"+spanId1).attr('id');
	attr1 = attr1.replace(regexReplace1, replace2);
	var attr2 = $("#"+spanId2).attr('id');
	attr2 = attr2.replace(regexReplace2, replace1);
	
	// Need to give the span a temporary id, else the next swap will match it and put it back
	$("#"+spanId1).attr('id', "swap_"+attr1);
	// Now swap the other id
	$("#"+spanId2).attr('id', attr2);
	// Now we can set the original id to the new one
	$("#swap_"+attr1).attr('id', attr1);
	
	// Try the light box spans
	$("#lb_"+spanId1).attr('id', "lb_swap_"+attr1);
	$("#lb_"+spanId2).attr('id', "lb_"+attr2);
	$("#lb_swap_"+attr1).attr('id', "lb_"+attr1);
	
	// Try the light box compare spans
	$("[id ^=lb_charslot_][id $="+spanId1+"]").attr('id', "lb_swap_charslot_"+attr1);
	// TODO: Fix this, the new ids need a char slot
	class2 = $("[id ^=lb_charslot_][id $="+spanId2+"]").attr('class');
	$("[id ^=lb_charslot_][id $="+spanId2+"]").attr('id', "lb_"+class2+attr2);
	class1 = $("#lb_swap_charslot_"+attr1).attr('class');
	$("#lb_swap_charslot_"+attr1).attr('id', "lb_charslot_"+class1+attr1);
	
	// Now check for removing the span
	spanId1 = attr1;
	attr1 = $("#"+spanId1).attr('remove');
	if(attr1 != null || dungeonPanelReload) {
		$("#"+spanId1).remove();
		reloadDungeonPanel(contextPath);
	}
	spanId2 = attr2;
	attr2 = $("#"+spanId2).attr('remove');
	if(attr2 != null) {
		$("#"+spanId2).remove();
		reloadDungeonPanel(contextPath);
	}
}

/**
 * Swap the ondragstart functions between spans, updating ids as required 
 * 
 * @param spanId1		Span to swap ondragstart
 * @param spanId2		Span to swap ondragstart
 */
function swapSpanOnDragStart(spanId1, spanId2) {
	var regexGetSpanIdNumber = new RegExp('^([^0-9]+)([0-9]+)$');
	var matchesSpanId1 = spanId1.match(regexGetSpanIdNumber);
	if(matchesSpanId1 == null) return;
	
	var matchesSpanId2 = spanId2.match(regexGetSpanIdNumber);
	if(matchesSpanId2 == null) return;
	
	var attr1 = $("#"+spanId1).attr('ondragstart');
	var attr2 = $("#"+spanId2).attr('ondragstart');
	// Neither have a ondrag start, so no action required
	if(attr1 == null && attr2 == null) return;

	// Both have ondragstart, so swap them
	if(attr1 != null && attr2 != null) {
		// Swap the span Ids
		var replaceSpanId1 = matchesSpanId1[2];
		if(replaceSpanId1 == null) return;
		var regexReplaceSpanId1 = new RegExp(replaceSpanId1, "g");

		var replaceSpanId2 = matchesSpanId2[2];
		if(replaceSpanId2 == null) return;
		var regexReplaceSpanId2 = new RegExp(replaceSpanId2, "g");

		attr1 = attr1.replace(regexReplaceSpanId1, replaceSpanId2);	
		attr2 = attr2.replace(regexReplaceSpanId2, replaceSpanId1);

		// Swap the equipment values
		var regexGetItemValueNumber = new RegExp('^([^\,]+\,){2}(\s*[0-9]+\s*\,)');
		var matchesItemValue1 = attr1.match(regexGetItemValueNumber);
		if(matchesItemValue1 == null) return;
		
		var matchesItemValue2 = attr2.match(regexGetItemValueNumber);
		if(matchesItemValue2 == null) return;
		
		var replaceItemValue1 = matchesItemValue1[2];
		if(replaceItemValue1 == null) return;
		var regexReplaceItemValue1 = new RegExp('\,'+replaceItemValue1);

		var replaceItemValue2 = matchesItemValue2[2];
		if(replaceItemValue2 == null) return;
		var regexReplaceItemValue2 = new RegExp('\,'+replaceItemValue2);
		
		attr1 = attr1.replace(regexReplaceItemValue1, ','+replaceItemValue2);	
		attr2 = attr2.replace(regexReplaceItemValue2, ','+replaceItemValue1);
		
		// Swap the function names (in case one is equipment and the other is a boost)
		var regexGetFunction = new RegExp('^drag(Equipment|BoostItem)');
		var matchesFunction1 = attr1.match(regexGetFunction);
		if(matchesFunction1 == null) {
			console.log('Attribute 1 not a function: '+attr1);
			return;
		}
		
		var matchesFunction2 = attr2.match(regexGetFunction);
		if(matchesFunction2 == null) {
			console.log('Attribute 2 not a function: '+attr2);
			return;
		}
		
		var replaceFunction1 = matchesFunction1[0]; // The whole string matched
		if(replaceFunction1 == null) return;
		var regexReplaceFunction1 = new RegExp(replaceFunction1, "g");

		var replaceFunction2 = matchesFunction2[0]; // The whole string matched
		if(replaceFunction2 == null) return;
		var regexReplaceFunction2 = new RegExp(replaceFunction2, "g");

		console.log('Swapping '+replaceFunction1 + " and " + replaceFunction2);
		
		attr1 = attr1.replace(regexReplaceFunction1, replaceFunction2);	
		attr2 = attr2.replace(regexReplaceFunction2, replaceFunction1);
		
		// Do the actual swap
		$("#"+spanId1).attr('ondragstart', attr1);
		$("#"+spanId2).attr('ondragstart', attr2);
	} else {
		// One or both don't have an ondragstart
		// So move the ondragstart (if there is one) to the other span
		var replace1 = matchesSpanId1[1];
		if(replace1 == null) return;
		var regexReplace1 = new RegExp(replace1, "g");
		var replace2 = matchesSpanId2[1];
		if(replace2 == null) return;
		var regexReplace2 = new RegExp(replace2, "g");
		var regexDungeon = new RegExp("^equipmentdungeonsFrame[0-9]+$");
		var regexMessage = new RegExp("^equipmentmessagesContentFrame[0-9]+$");
		
		if(attr1 == null) {
			attr2 = attr2.replace(regexReplace2, replace1);
			
			$("#"+spanId1).attr('ondragstart', attr2).attr('draggable', 'true');
			if(spanId2.match(regexDungeon) || spanId2.match(regexMessage)) {
				$("#"+spanId2).attr('remove', true);
			} else {
				$("#"+spanId2).removeAttr('ondragstart').removeAttr('draggable');
			}
		} else {
			attr1 = attr1.replace(regexReplace1, replace2);
			if(spanId1.match(regexDungeon) || spanId1.match(regexMessage)) {
				$("#"+spanId1).attr('remove', true);
			} else {				
				$("#"+spanId1).removeAttr('ondragstart').removeAttr('draggable');
			}
			$("#"+spanId2).attr('ondragstart', attr1).attr('draggable', 'true');
		}
	}
}

/**
 * Empty the span and associated pop-ups, char slots, etc
 * 
 * @param spanId		Span to clear
 * @param contextPath	Context root path for site (e.g /dungeon)
 */
function clearSpan(spanId, charSlotId, contextPath) {
	debug("clearSpan("+spanId+","+contextPath+")");
	$("#"+spanId).empty();
	$("#lb_"+spanId).empty();
	$("[id ^=lb_charslot_][id $="+spanId+"]").empty();
	$("[id ^=lb_charslot_][id $="+spanId+"]").attr('class','charslot_none'+spanId); // Needs to be unique
	// If it was in the dungeon panel, it may need to be reloaded
	var regexDungeon = new RegExp("^equipmentdungeonsFrame[0-9]+$");
	if(spanId.match(regexDungeon)) {
		$("#"+spanId).remove();
		reloadDungeonPanel(contextPath);
	}
}

/**
 * Removes ALL spans that contain the matching part in their id
 * 
 * @param matchIdPart	id part to remove
 */
function removeQtips(matchIdPart) {
	debug("removeQtips("+matchIdPart+") found "+$("[id*="+matchIdPart+"]").length);
	$("[id*="+matchIdPart+"]").remove();	
}

/**
 * Remove qtips no long referenced by spans as pop ups
 */
function removeZombiedQtips() {
	debug("removeZombiedQtips");
	// No point in queueing multiple
	if(waitingZombies < 1) {
		waitingZombies++;
		setTimeout(function() {
			waitingZombies--;
			removeZombiedQtipsWait(0);
	    }, 1000); // wait 1 second, so page is likely loaded
	}
}

function removeZombiedQtipsWait(count){
    if(document.readyState == 'complete' && pendingRequests == 0 && runningZombieCheck == 0) {
        removeZombiedQtipsDo();
    } else {
    	if(count < 1000) {
			setTimeout(function() {
				removeZombiedQtipsWait(++count);
	        }, 100); // wait 100 ms
		}
    }
}

function removeZombiedQtipsDo() {
	debug("removeZombiedQtipsDo");
	runningZombieCheck++;
	$(".qtip").each(function() {
		$(this).addClass("qtipRemove");
		trace("Added qtipRemove to "+$(this).attr('id'));		
	});
	debug("Added qtipRemove classes");
	setTimeout(function() {
		// After a brief pause to let the browser catch up, remove flag from qtips with references
		$("[aria-describedby*='qtip-']").each(function(){
			var foundQtipId = $(this).attr('aria-describedby');
			trace("saving qtip '"+foundQtipId+"' ("+$("#"+foundQtipId+":first").attr('class')+")");
			$("#"+foundQtipId+".qtipRemove").removeClass("qtipRemove");
		});
		debug("Removed qtipRemove classes");
		setTimeout(function() {
			// After a brief pause to let the browser catch up, actually remove stuff
			debug("Removing '"+$(".qtipRemove").size()+" zombies");
			$(".qtipRemove").each(function(){
				trace("Removing qtip '"+$(this).attr('id'));
			});
			$(".qtipRemove").remove();
			setTimeout(function() {
				// After a brief pause to let the browser catch up, unlock
				debug("removeZombiedQtipsDo unlock");
				runningZombieCheck--;
		    }, 1000);
	    }, 1000); // wait 1000 ms
		
	}, 1000); // wait 1000 ms
}

/**
 * If the Char Slot is empty, make sure the slot type icon is visible
 * Otherwise make sure it isn't visible
 * 
 * @param spanId	Character Slot span
 */
function checkForEmptyCharSlot(spanId) {
	if(!$.trim( $("#"+spanId).html() ).length) {
		$("#"+spanId).removeClass("charSlotEquipment");
		$("#"+spanId).addClass("charSlotEmptyEquipment");		
	} else {
		$("#"+spanId).removeClass("charSlotEmptyEquipment");
		$("#"+spanId).addClass("charSlotEquipment");		
	}
}

/**
 * Reload the src span detailed in the transfer data
 * 
 * @param dataTransfer	Transfer data containing src frame and URL
 */
function reloadSrcFrame(dataTransfer) {
	var srcFrame = dataTransfer.getData("srcFrame");
    var srcUrl = dataTransfer.getData("srcUrl");
    if(srcFrame == undefined) {
    	error("Reload called with null srcFrame");
    	return;
    }
    if(srcUrl == undefined ){
    	error("Reload called with null srcUrl");
    	return;
    }
    loadSpan(srcFrame,srcUrl);
}

/**
 * Check if any of the panels need a full reload and reload any that do
 * 
 * @param reloadPanels		List of panels to reload
 * @param contextPath		Context root path for site (e.g /dungeon)
 */
function reloadPanels(reloadPanels, contextPath) {
	var typeParam = "?hardcore="+(hardcore?"1":"0")+"&ironborn="+(ironborn?"1":"0");
	if(reloadPanels.reloadSummary) loadSpan("summaryFrame", contextPath+"/play/summary" + typeParam);
	if(reloadPanels.reloadStashSlotId >= 0) loadSpan('stashFrame',contextPath+'/play/stash' + typeParam);
	if(reloadPanels.reloadDungeons && dungeonId > 0) {
		loadSpan('dungeonsFrame',contextPath+'/play/dungeon/'+dungeonId);
	}
	if(reloadPanels.reloadCharacters) {
		loadSpan('charactersFrame',contextPath+'/play/characters');
	}
	if(reloadPanels.reloadCharacterDetails) {
		reloadCharacterDetailsPanel(contextPath);
		reloadPanels.reloadCharacterDetailsSummary = true;
	}
	if(reloadPanels.reloadCharacterDetailsSummary) reloadCharacterDetailsSummaryPanel(contextPath);
	if(reloadPanels.reloadMessages) reloadMessagesPanel(contextPath, typeParams);
}

/**
 * Move the spans inner html to the stash slot
 * 
 * @param srcSpanId		Span to move
 * @param stashSlotId	Stash slot span to put moved span to 
 * @param contextPath	Context root path for site (e.g /dungeon)
 */
function moveSpanIdToStash(srcSpanId, stashSlotId, contextPath) {
	var targetSpan = $("#stashSlot"+stashSlotId+" > .itemDrop");
	if(targetSpan.length < 1) return;
	var targetId = targetSpan.attr('id');
	swapSpanHtml(srcSpanId, targetId);
	swapSpanCompareHtml(srcSpanId, targetId, true);
	swapSpanOnDragStart(srcSpanId, targetId);
	checkForEmptyCharSlot(targetId);
	swapSpanId(srcSpanId, targetId, false, contextPath);
}

/**
 * Find the span id and list of valid slots for the stash slot, also flags if the stash slot was empty
 * 
 * @param stashSlotId	Stash slot to find span id of
 * @param results		Map of result data
 * 							stashFrameId	The span id of the stash slot
 * 							validSlots		JSON list of valid slot ids
 * 							empty			If the stash slot was empty
 */
function getStashValidSlotsAndFrameId(stashSlotId, results) {
	var stashItemSpan = $("#stashSlot"+stashSlotId+" > .itemDrop");
	if(stashItemSpan.length < 1) return;
	var stashFrameSpanId = stashItemSpan.attr('id');
	var regex1 = /^[^\d]+([\d]+)$/;
	results.stashFrameId = stashFrameSpanId.match(regex1)[1];
	var compareSpan = $("#lb_charslot_emptyStash"+results.stashFrameId);
	if(compareSpan.length < 1) {
	    compareSpan = $("[id^=lb_charslot_][id$=equipmentstashFrame"+results.stashFrameId+"]");
	    results.empty = false;
	} else {
		results.empty = true;
	}
	if(compareSpan.length < 1) return;
	var compareClasses = compareSpan.attr('class');
	var regex2 = /charslot_([\d]+)/g;
	var match = regex2.exec(compareClasses);
	results.validSlots = "[";
	var seperator = "";
	while (match != null) {
		validSlot = match[1];
		results.validSlots += seperator + validSlot;
		seperator = ",";
	    match = regex2.exec(compareClasses);
	}
	results.validSlots += "]";
}