function preventDefault(e) { e.preventDefault(); };

function allowDrop(ev) {
    ev.preventDefault();
}

/**
 * Start event for dragging a character
 * Stores all the data required at the drop location about the drag location
 * 
 * @param ev			DOM event
 * @param charId		Character id
 * @param srcSpanId		Span the character was dragged from
 * @param srcFrame		Panel the character was dragged from
 * @param srcUrl		URL of the panel the character was dragged from
 * @param contextPath	Context root path for site (e.g /dungeon)
 */
function dragCharacter(ev, charId, srcSpanId, srcFrame, srcUrl, contextPath) {
	var content = {
    		"type": "character",
    	    "charId": charId,
    	    "srcSpanId": srcSpanId,
    	    "srcFrame": srcFrame,
    	    "srcUrl": srcUrl,
    	    "contextPath": contextPath	
    };
    
    ev.dataTransfer.setData("text", JSON.stringify(content));
}

/**
 * Drop event for character
 * Actions the drop, validating that it is a valid location for the drop
 * 
 * @param ev			DOM event
 * @param destination	Panel containing the span the character was dropping in
 * @returns {Boolean}	Success
 */
function dropCharacter(ev, destination) {
	var json = JSON.parse(ev.dataTransfer.getData("text"));
	if(json["type"] == 'character') {
	    ev.preventDefault();
	    if(destination == 'dungeonsFrame') {
	    	// Make sure we have the outer node with an id
			var target = ev.target;
			while(target && !target.id) target = target.parentNode;
			if(!target) { return false }
			// Set the character ID for the party
		    $("#"+target.id).siblings("input").val(json["charId"]);
		    // Copy the character frame
		    var srcSpanHtml = $('#'+json["srcSpanId"]).html();
		    $("#"+target.id).html(srcSpanHtml);
		    return true;
		}
	}
}

/**
 * Start event for dragging a equipment
 * Stores all the data required at the drop location about the drag location
 * 
 * @param ev				DOM event
 * @param itemId			Id of equipment
 * @param itemValue	Salvage value of equipment
 * @param srcSpanId			Span id of item being dropped
 * @param contextPath		Context root path for site (e.g /dungeon)
 */
function dragEquipment(ev, itemId, itemValue, srcSpanId, contextPath) {
	var content = {
    		"type": "equipment",
    	    "itemId": itemId,
    	    "itemValue": itemValue,
    	    "srcSpanId": srcSpanId,
    	    "contextPath": contextPath	
    };
    
    ev.dataTransfer.setData("text", JSON.stringify(content));
}

/**
 * JSON callback for dropping equipment on a character
 * Make the JSON call back and process the response, making all changes to the DOM to match the response
 * 
 * @param ev				DOM event
 * @param charDetailsUrl	URL for character details of character
 * @param jsonUrl			URL to JSON callback
 * @param srcSpanId			Span id of item being dropped
 * @param contextPath		Context root path for site (e.g /dungeon)
 */
function jsonEquipmentCharDetailsDrop(ev, charDetailsUrl, jsonUrl, srcSpanId, contextPath, showCompare) {
	increasePendingRequests('jsonEquipmentCharDetailsDrop');
	debug("jsonEquipmentCharDetailsDrop: "+jsonUrl);
    $.post(jsonUrl,
		// DATA: none		
		// Success function
		function( data ) {
    		debug("jsonEquipmentCharDetailsDrop Got response");
	    	if(data) {
	    		debug("jsonEquipmentCharDetailsDrop Got Success response");
		    	// Make sure we have the outer node with an id
				var target = ev.target;
				while(target && !target.id) target = target.parentNode;
				if(!target) { return false }

				stashSlotId = -1;
				if(data.reloadStashSlotId >= 0 && data.reloadDungeon) {
					stashSlotId = data.reloadStashSlotId;
					//	Put item in char slot in stash slot
					moveSpanIdToStash(target.id, data.reloadStashSlotId, contextPath);
				}
				// No need to reload the stash, it's been dealt with
				data.reloadStashSlotId = -1;

				swapSpanHtml(srcSpanId, target.id);
				if(showCompare) swapSpanCompareHtml(srcSpanId, target.id, showCompare);
	    		swapSpanOnDragStart(srcSpanId, target.id);
	    		checkForEmptyCharSlot(target.id);
	    		swapSpanId(srcSpanId, target.id, data.reloadDungeon, contextPath);
	    		
	    		// No need to reload dungeon, we would have modified it already
	    		data.reloadDungeon = false; 
	    		data.reloadCharacterDetailsSummary = data.reloadCharacterDetails;
	    		data.reloadCharacterDetails = false;
	    		reloadPanels(data, contextPath);
	    		if(stashSlotId >= 0) {
	    			// We put an item from character in empty stash slot, so set the compare
	    			// We want to pass these by reference
	    			var results = {validSlots: '', stashFrameId: ''};
	    			getStashValidSlotsAndFrameId(stashSlotId, results);
	    			if(showCompare && results.validSlots != '' && results.stashFrameId != '') {
	    				if(results.empty) {
	    			        setUpCompare(results.validSlots,'lb_charslot_', 'emptyStash'+results.stashFrameId);
	    				} else {
	    			        setUpCompare(results.validSlots,'lb_charslot_', 'equipmentstashFrame'+results.stashFrameId);
	    			    }
	    			} // else something went wrong, so no compare for this item
	    		}
	    	}
    		decreasePendingRequests('jsonEquipmentCharDetailsDrop');
		}
		);
}

/**
 * Drop event for dropping equipment on a character
 * 
 * @param ev				DOM event
 * @param charDetailsUrl	URL for character details of character
 * @param jsonUrl			URL to JSON callback
 * @param slotId			Character Slot id for item (e.g. enum id for chest)
 */
function dropEquipmentCharDetails(ev, charDetailsUrl, jsonUrl, slotId, showCompare) {
	var json = JSON.parse(ev.dataTransfer.getData("text"));
	if(json["type"] == 'equipment') {
	    ev.preventDefault();
	    
	    jsonEquipmentCharDetailsDrop(ev, charDetailsUrl, 
	    		jsonUrl+json["itemId"]+"/slot/"+slotId, 
	    		json["srcSpanId"], 
	    		json["contextPath"],
	    		showCompare);
	}
}

/**
 * JSON callback for dropping equipment on enchant
 * Make the JSON call back and process the response, making all changes to the DOM to match the response
 * 
 * @param jsonUrl		URL to JSON callback
 * @param contextPath	Context root path for site (e.g /dungeon)
 * @param itemId	Equipment id for item being dropped
 */
function jsonEnchant(jsonUrl, contextPath, itemId) {
	increasePendingRequests('jsonEnchant');
	$.post(jsonUrl,
		// DATA
		// Success function
		function( data ) {
			if(data) {
				loadSpan("outerSpan_enchantFrame", contextPath+"/play/enchant/"+itemId);
				loadSpan("summaryFrame", contextPath+"/play/summary?hardcore="+(hardcore?"1":"0")+"&ironborn="+(ironborn?"1":"0"));
			}
			decreasePendingRequests('jsonEnchant');
		}
		);
}

/**
 * Open the enchant window for the equipment
 * 
 * @param ev		DOM event
 * @param jsonUrl	URL to JSON callback
 */
function dropEquipmentEnchant(ev, jsonUrl) {
	var json = JSON.parse(ev.dataTransfer.getData("text"));
	if(json["type"] == 'equipment') {
	    ev.preventDefault();
	    var outerSpanEnchantFrame = $('#outerSpan_enchantFrame');
	    outerSpanEnchantFrame.load(json["contextPath"]+"/play/enchant/"+json["itemId"]);
	    //outerSpanEnchantFrame.css('pointer-events', 'all');
	    //outerSpanEnchantFrame.css('display', 'block');
	}
}

/**
 * JSON callback for dropping equipment on a stash slot
 * Make the JSON call back and process the response, making all changes to the DOM to match the response
 * 
 * @param ev			DOM event
 * @param jsonUrl		URL to JSON callback
 * @param itemId	Equipment id for item being dropped
 * @param srcSpanId		Span id of item being dropped
 * @param contextPath	Context root path for site (e.g /dungeon)
 */
function jsonStashDrop(ev, jsonUrl, itemId, srcSpanId, contextPath, equipment, showCompare) {
	increasePendingRequests('jsonStashDrop');
	debug(jsonUrl);
    $.post(jsonUrl,
		// DATA
		{
    		equipment: equipment,
			itemId: itemId
		},
		// Success function
		function( data ) {
			if(data) {
				// Make sure we have the outer node with an id
				var target = ev.target;
				while(target && !target.id) target = target.parentNode;
				if(!target) { return false }
				
				swapSpanHtml(srcSpanId, target.id);
	    		if(showCompare) swapSpanCompareHtml(srcSpanId, target.id, showCompare);
	    		swapSpanOnDragStart(srcSpanId, target.id);
	    		checkForEmptyCharSlot(srcSpanId);
	    		swapSpanId(srcSpanId, target.id, data.reloadDungeon, contextPath);
	    		data.reloadDungeon = false; // No need to reload dungeon, we would have modified it already
	    		data.reloadStashSlotId = -1; // No need to reload dungeon, we would have modified it already
	    		reloadPanels(data, contextPath);
			}
			decreasePendingRequests('jsonStashDrop');
		}
		);
}

/**
 * Drop event for equipment being dropped in a stash slot
 * 
 * @param ev			DOM event
 * @param jsonUrl		URL to JSON callback
 */
function dropItemStash(ev, jsonUrl, showCompare) {
	var json = JSON.parse(ev.dataTransfer.getData("text"));
	if(json["type"] == 'equipment') {
	    ev.preventDefault();
	    jsonStashDrop(ev, jsonUrl, json["itemId"], json["srcSpanId"], json["contextPath"], true, showCompare);
	} else if(json["type"] == 'boostItem') {
	    ev.preventDefault();
	    jsonStashDrop(ev, jsonUrl, json["itemId"], json["srcSpanId"], json["contextPath"], false, showCompare);
	}
}

/**
 * JSON callback for dropping equipment on salvage
 * Make the JSON call back and process the response, making all changes to the DOM to match the response
 * 
 * @param ev			DOM event
 * @param jsonUrl		URL to JSON callback
 * @param srcSpanId		Span id of item being dropped
 * @param contextPath	Context root path for site (e.g /dungeon)
 */
function jsonSalvageDrop(ev, jsonUrl, srcSpanId, contextPath) {
	increasePendingRequests('jsonSalvageDrop');
	$.post(jsonUrl,
		// DATA
		// Success function
		function( data ) {
			if(data) {
				clearSpan(srcSpanId, contextPath);
				loadSpan("summaryFrame", contextPath+"/play/summary?hardcore="+(hardcore?"1":"0")+"&ironborn="+(ironborn?"1":"0"));
			}
			decreasePendingRequests('jsonSalvageDrop');
		}
		);
}

/**
 * Drop event for equipment being dropped on salvage
 * 
 * @param ev		DOM event
 * @param jsonUrl	URL to JSON callback
 */
function dropEquipmentSalvage(ev, jsonUrl) {
	var json = JSON.parse(ev.dataTransfer.getData("text"));
	if(json["type"] == 'equipment') {
	    ev.preventDefault();
	    if(confirm("Are you sure you want to salvage that item for "+json["itemValue"]+" dungeon tokens?")) {
		    jsonSalvageDrop(ev, jsonUrl+json["itemId"]+"/salvage", json["srcSpanId"], json["contextPath"]);
		}
	} else if(json["type"] == 'boostItem') {
	    ev.preventDefault();
	    if(confirm("Are you sure you want to salvage that item for "+json["itemValue"]+" dungeon tokens?")) {
		    jsonSalvageDrop(ev, jsonUrl+json["itemId"]+"/salvageBoostItem", json["srcSpanId"], json["contextPath"]);
		}
	}
}

//  =============== Boost Items ===============

/**
 * Start event for dragging a boostItem
 * Stores all the data required at the drop location about the drag location
 * 
 * @param ev				DOM event
 * @param itemId			Id of Boost Item
 * @param itemValue			Salvage value of Boost Item
 * @param srcSpanId			Span id of item being dropped
 * @param contextPath		Context root path for site (e.g /dungeon)
 */
function dragBoostItem(ev, itemId, itemValue, srcSpanId, contextPath) {
	debug("dragBoostItem");
	var content = {
    		"type": "boostItem",
    	    "itemId": itemId,
    	    "itemValue": itemValue,
    	    "srcSpanId": srcSpanId,
    	    "contextPath": contextPath	
    };
	debug(JSON.stringify(content));
    ev.dataTransfer.setData("text", JSON.stringify(content));
}