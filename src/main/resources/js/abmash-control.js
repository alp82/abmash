(function(abmash) {
	var queryElementsFound;
	var queryLimit;
	
	var tempElement = null;
	var tempData = null;

	// public functions
	
	abmash.foo = function() {
		return foo;
	}
	
	abmash.setTempElement = function(element) {
		tempElement = element;
	}
	
	abmash.getTempElement = function() {
		return tempElement;
	}
	
	abmash.setTempData = function(data) {
		tempData = data;
	}
	
	abmash.getTempData = function() {
		return tempData;
	}
	
	abmash.processJqueryCommands = function(elements, jQueryCommands) {
		var response = {};
		jQuery.each(elements, function(id, element) {
			abmash.setTempElement(element);
			var elementData = {};
//			alert(element);
			jQuery.each(jQueryCommands, function(type, jQueryCommand) {
//				alert(element + " - " + type + "(" + jQueryCommand + ")");
				jQuery.globalEval("abmash.setTempData(" + jQueryCommand + ");");
				elementData[type] = abmash.getTempData();
//				alert(element + " - " + type + "(" + jQueryCommand + "): " + elementData[type]);
//				alert(element + " - " + id + "(" + type + "): " + jQueryCommand);
			});
			response[id] = elementData;
		});
		return response;
	}
	
	/*
	abmash.query = function(queries, rootElements, limit) {
		var response = [];
		var elementsFound = 0;
		// TODO how do multiple root elements work?
		abmash.setTempElement(rootElements.length > 0 ? rootElements.join(',') : document.body);
		jQuery.each(jQuery.parseJSON(queries), function() {
			var condition = this;
			var conditionResult = {};
			conditionResult.type = condition.type;
			conditionResult.elementType = condition.elementType;
			conditionResult.group = [];
			// TODO condition weight?
			jQuery.each(condition.selectorGroups, function() {
				var groupResult = querySelectorGroup(this);
				elementsFound += groupResult.result.length;
				if(groupResult.result.length > 0) conditionResult.group.push(groupResult);
			});
			// continue with fallback selector groups if nothing was found
			if(elementsFound == 0) {
				jQuery.each(condition.fallbackSelectorGroups, function() {
					var groupResult = querySelectorGroup(this);
					elementsFound += groupResult.result.length;
					if(groupResult.result.length > 0) conditionResult.group.push(groupResult);
				});
			}
			response.push(conditionResult);
		});
		return response;
	}
	*/
	
	abmash.query = function(conditions, rootElements, limit) {
		var result = null;
		queryLimit = limit;
		queryElementsFound = 0;
		// TODO how do multiple root elements work?
		abmash.setTempElement(rootElements.length > 0 ? rootElements.join(',') : document.body);
		jQuery.each(jQuery.parseJSON(conditions), function() {
			var conditionResult = queryCondition(this);
			if(result == null) {
				result = conditionResult;
			} else if(conditionResult.length > 0) {
				// intersect the results with the previous condition because all conditions are ANDed
				result = jQuery.map(result, function(a) { return jQuery.inArray(a, conditionResult) < 0 ? null : a;})
			}
			// TODO process all conditions again with fallbacks only? could produce unexpected results...
			if(result.length == 0) return false;
		});
		return result.unique();
	}

	// private functions
	
	function queryCondition(condition) {
		var conditionResult = [];
		// TODO condition weight?
		jQuery.each(condition.selectorGroups, function() {
			var groupResult = querySelectorGroup(this);
			if(groupResult.length > 0) conditionResult = conditionResult.concat(groupResult);
			if(queryLimitReached()) return false;
		});
		// continue with fallback selector groups if nothing was found
		if(queryElementsFound == 0) {
			jQuery.each(condition.fallbackSelectorGroups, function() {
				var groupResult = querySelectorGroup(this);
				if(groupResult.length > 0) conditionResult = conditionResult.concat(groupResult);
				if(queryLimitReached()) return false;
			});
		}
		return conditionResult;
	}
	
	function querySelectorGroup(selectorGroup) {
		var groupResult = [];
		var groupLimit = selectorGroup.limit;
		jQuery.each(selectorGroup.selectors, function() {
			var selector = this;
			// process jquery command
			jQuery.globalEval("abmash.setTempData(" + selector.command + ");");
			var queryResult = abmash.getTempData();
//			alert(queryResult.toSource());
			jQuery.each(queryResult, function() {
			    var element = this;
				groupResult.push(element);
				queryElementsFound++;
				if(queryLimitReached() || groupLimit > 0 && groupResult.length >= groupLimit) return false;
			});
			if(queryLimitReached() || groupLimit > 0 && groupResult.length >= groupLimit) return false;
		});
		return groupResult;
	}
	
	function queryLimitReached() {
		return queryLimit > 0 && queryElementsFound >= queryLimit;
	}
	
	/*
	function querySelectorGroup(group) {
		var selectorGroup = group;
		var groupResult = {};
		groupResult.weight = selectorGroup.weight;
		groupResult.result = [];
		//conditionResult.groupResult = [];
		jQuery.each(selectorGroup.selectors, function() {
			var selector = this;
			jQuery.globalEval("abmash.setTempData(" + selector.command + ");");
			var queryResult = abmash.getTempData();
			jQuery.each(queryResult, function() {
			    var element = this;
				groupResult.result.push({type: selector.type, weight: selector.weight, element: element});
			});
			//if(elementsFound >= limit) break;
		});
		return groupResult;
		if(groupResult.result.length > 0) conditionResult.group.push(groupResult);
	}
	*/

})(window.abmash = window.abmash || {});

//jQuery(document).ready(function(jQuery) {
//	abmash.foo();
//});