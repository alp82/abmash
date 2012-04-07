(function(abmash) {
	var queryElementsFound = 0;
	var queryLimit = 0;
	var queryData = {};
	var elementWeight = {};
	var elementWeightHistory = {};
	
	// public functions
	
	abmash.setData = function(key, value) {
		queryData[key] = value;
	};
	
	abmash.getData = function(key) {
		return queryData[key];
	};
	
	abmash.setElementWeight = function(element, weight) {
		elementWeight[jQuery(element).getPath()] = Math.max(weight, abmash.getElementWeight(element, { useHistory: false }));
	};

	abmash.getElementWeight = function(element, options) {
//		if(document.title.contains("Select a Car")) {
//			alert("element: " + element);
//			alert("options: " + options);
//			alert("path: " + jQuery(element).getPath());
//			alert("type: " + (typeof elementWeight[jQuery(element).getPath()]));
//		}		

		options = jQuery.extend({
			useHistory: true,
		}, options);
		
		if(typeof elementWeight[jQuery(element).getPath()] != "undefined") return elementWeight[jQuery(element).getPath()];
		if(options.useHistory && typeof elementWeightHistory[jQuery(element).getPath()] != "undefined") return elementWeightHistory[jQuery(element).getPath()];
		return 1;
	};
	
	abmash.processJqueryCommands = function(elements, jQueryCommands) {
		var response = {};
		jQuery.each(elements, function(id, element) {
			abmash.setData('queryElements', element);
			var elementData = {};
			jQuery.each(jQueryCommands, function(type, jQueryCommand) {
				jQuery.globalEval("abmash.setData('queryResults', " + jQueryCommand + ");");
				elementData[type] = abmash.getData('queryResults');
			});
			response[id] = elementData;
		});
		return response;
	};
	
	abmash.query = function(jsonJQueryList/*, closenessConditions, colorConditions, rootElements, elementsToFilter, referenceElements, labelElements, limit*/) {
		var result = abmash.queryJQueryList(JSON.parse(jsonJQueryList), null);
		return result;
	};
	
	abmash.queryJQueryList = function(jQueryList, relationalResult) {
		var result = [];

		jQuery.each(jQueryList, function() {
			var jQuery = this;
			//TODO jQuery.weight
			result = result.concat(abmash.queryJQueryCommands(jQuery.commands, relationalResult));
		});
		
		return result;
	};
	
	abmash.queryJQueryCommands = function(jQueryCommands, relationalResult) {
		var result = [];
		
		var firstAnd = true;
		
		jQuery.each(jQueryCommands, function() {
			var jQueryCommand = this;

			if(jQueryCommand.boolean) {
				booleanResult = result.concat(abmash.queryJQueryList(jQueryCommand.jQueryList, result));
				switch (jQueryCommand.type) {
				case "OR":
					// merge results
					result = result.concat(booleanResult);
					break;
					
				case "NOT":
					// remove not result from query result
					result = jQuery.map(booleanResult, function(a) { return jQuery.inArray(a, result) < 0 ? a : null;});
					break;
					
				case "AND":
					// intersect results
					if(firstAnd) {
						result = result.concat(booleanResult);
						firstAnd = false;
					} else {
						result = jQuery.map(booleanResult, function(a) { return jQuery.inArray(a, result) < 0 ? null : a;});
					}
					break;
					
				default:
					alert("WRONG BOOLEAN TYPE: " + jQueryCommand.type);
				}
			} else {
				result = result.concat(abmash.queryExecuteJQueryCommand(jQueryCommand, relationalResult));
			}
		});
		
		return result;
	};
	
	abmash.queryExecuteJQueryCommand = function(jQueryCommand, relationalResult) {
		var result = [];
		
		var jQueryEval = "jQuery" + (relationalResult != null && false ? "(" + relationalResult + ")" : "");
		jQueryEval += "." + jQueryCommand.method + "(" + jQueryCommand.selector + ")";

		jQuery.globalEval("abmash.setData('commandResult', " + jQueryEval + ");");
		var commandResult = abmash.getData('commandResult');
		jQuery.each(jQuery(commandResult), function() {
		    var element = jQuery(this);
		    // add element if it is visible
		    // TODO :visible optional?

		    if(element.filter(':visible').length > 0 && element.parents('head').length == 0 && element.parents('html').length > 0) {
		    	result.push(element.get(0));
		    	// store priority for element based on selector weight
		    	abmash.setElementWeight(element.get(0), jQueryCommand.weight);
		    	// increment element count
//		    	queryElementsFound++;
//		    	if(groupLimit > 0 && groupResult.length >= groupLimit) return false;
		    }
		});
		
		return result;
	};
	
	abmash.queryOld = function(conditions, closenessConditions, colorConditions, rootElements, elementsToFilter, referenceElements, labelElements, limit) {
		queryLimit = limit;
		queryElementsFound = 0;
		elementWeightHistory = jQuery.extend(elementWeightHistory, elementWeight);
		elementWeight = {};
		// TODO how do multiple root elements work?
		abmash.setData('queryElements', rootElements.length > 0 ? rootElements.join(',') : document);
		
		// prepare results
		var results = [];
		
		// TODO remove
		if(elementsToFilter.length > 0) {
			abmash.setData('debug1', true);
		}
		
		// normal conditions (element selectors)
		if(JSON.parse(conditions).length > 0) {
			results = queryConditions(conditions, referenceElements, labelElements, elementsToFilter);
			if(results.length == 0) return [];
		} else if(elementsToFilter.length > 0) {
			results = elementsToFilter;
		}

		// closeness conditions
		if(JSON.parse(closenessConditions).length > 0) {
			abmash.setData('queryElements', results);
			// TODO remove
			if(abmash.getData('debug1')) {
				abmash.setData('debug2', true);
			}		
			results = queryConditions(closenessConditions, referenceElements, labelElements, []);
			if(results.length == 0) return [];
		}
		
		
		// color conditions
		if(JSON.parse(colorConditions).length > 0) {
			abmash.setData('queryElements', results);
			results = queryConditions(colorConditions, referenceElements, labelElements, []);
			if(results.length == 0) return [];
		}
		
//		results = results.sort(sortResults);
				
//		jQuery(results).css('background-color', 'purple');
		return results;
	};

	// private functions
	
	function queryConditions(conditions, referenceElements, labelElements, preResult) {
		var conditionsResult = preResult.length > 0 ? preResult : null;

		jQuery.each(JSON.parse(conditions), function() {
			var conditionResult = queryCondition(this, referenceElements, labelElements);
			if(conditionsResult == null) {
				conditionsResult = conditionResult;
			} else if(conditionResult.length > 0) {
				// intersect the results with the previous condition because all conditions are ANDed
				conditionsResult = jQuery.map(conditionsResult, function(a) { return jQuery.inArray(a, conditionResult) < 0 ? null : a;});
			}
			
			// TODO process all conditions again with fallbacks only? could produce unexpected results...
			if(conditionsResult.length == 0) return false;
		});

		return conditionsResult == null ? [] : conditionsResult.unique();
	}
	
	function queryCondition(condition, referenceElements, labelElements) {
		var conditionResult = [];
		// TODO condition weight?
		conditionResult = querySelectorGroups(condition.selectorGroups, referenceElements, conditionResult);

		// continue with fallback selector groups if nothing was found
		if(queryElementsFound == 0) {
			conditionResult = querySelectorGroups(condition.fallbackSelectorGroups, referenceElements, conditionResult);
		}
		
		// search for closest labels if needed
		if(condition.labelId) {
			var additionalElementsForLabels = querySelectorGroups(condition.labelSelectorGroups, referenceElements, conditionResult);
			var totalElementsForLabels = jQuery(conditionResult).add(additionalElementsForLabels);
			var labelResult = [];
			if(condition.labelType == "input") {
				labelResult = totalElementsForLabels.hasLabel(jQuery(labelElements[condition.labelId]));
			} else if(condition.labelType == "select") {
				labelResult = totalElementsForLabels.hasLabelForSelect(jQuery(labelElements[condition.labelId]));
			}
			
			// apply element weights for labels
			jQuery.each(jQuery(labelResult), function() {
				abmash.setElementWeight(this, abmash.getElementWeight(jQuery(labelElements[condition.labelId])));
			});
			
			conditionResult = labelResult.concat(conditionResult).unique();
		}

		// sort results by their weight
		conditionResult = conditionResult.sort(sortByWeight);
		
		// remove duplicates
		conditionResult = conditionResult.unique();
		
		// if this was a closeness or color condition, further narrow down the results for potentially following similar queries
		if(condition.isClosenessCondition || condition.isColorCondition) {
			abmash.setData('queryElements', conditionResult);
		}

		return conditionResult;
	}
	
	function querySelectorGroups(groups, referenceElements, conditionResult) {
		jQuery.each(groups, function() {
			var groupResult = querySelectorGroup(this, referenceElements);
			if(groupResult.length > 0) conditionResult = conditionResult.concat(groupResult);
			// TODO query limit needed? (cannot return here)
//			if(queryLimitReached()) return false;
		});
		
		return conditionResult;
	}
	
	function querySelectorGroup(selectorGroup, referenceElements) {
		var groupResult = [];
		var groupLimit = selectorGroup.limit;
		if(selectorGroup.referenceId) {
			abmash.setData('referenceElements', jQuery(referenceElements[selectorGroup.referenceId]));
		}
		jQuery.each(selectorGroup.selectors, function() {
			var selector = this;

			// process jquery command
			jQuery.globalEval("abmash.setData('queryResults', " + selector.command + ");");
			var queryResult = abmash.getData('queryResults');
			jQuery.each(jQuery(queryResult), function() {
			    var element = jQuery(this);
			    // add element if it is visible
			    // TODO :visible optional?

			    if(element.filter(':visible').length > 0 && element.parents('head').length == 0 && element.parents('html').length > 0) {
			    	groupResult.push(element.get(0));
			    	// store priority for element based on selector weight
			    	abmash.setElementWeight(element.get(0), selector.weight);
			    	// increment element count
			    	queryElementsFound++;
			    	if(groupLimit > 0 && groupResult.length >= groupLimit) return false;
			    }
			});
			if(groupLimit > 0 && groupResult.length >= groupLimit) return false;
		});
		return groupResult;
	}
	
	function queryLimitReached() {
		return queryLimit > 0 && queryElementsFound >= queryLimit;
	}
	
    function sortByWeight(firstElement, secondElement) {
    	var firstWeight = abmash.getElementWeight(firstElement);
    	var secondWeight = abmash.getElementWeight(secondElement);
		return secondWeight - firstWeight;
	}
    
})(window.abmash = window.abmash || {});
