(function(abmash) {
	var queryElementsFound = 0;
	var queryLimit = 0;
	var queryData = {};
	var elementWeight = {};
	var elementWeightHistory = {};
	

	// public functions
	
	abmash.highlight = function(elements) {
		var bgColors = {};
		elements = jQuery.unique(jQuery(elements).get());
		
		jQuery.each(elements, function() {
			var element = jQuery(this);
			var path = element.getPath();
			var bgColor = element.css('background-color');
			bgColors[path] = bgColor;
			element.css('background-color', 'yellow');
		});
		
		alert(elements);
		
		jQuery.each(elements, function() {
			var element = jQuery(this);
			var path = element.getPath();
			element.css('background-color', bgColors[path]);
		});
	};
	
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
	
	abmash.query = function(jsonPredicates/*, closenessConditions, colorConditions, rootElements, elementsToFilter, referenceElements, labelElements, limit*/) {
		// query, remove duplicaates and sort afterwards
		var result = abmash.parsePredicates(JSON.parse(jsonPredicates), 'AND');
		result = jQuery.unique(result).sort(sortByWeight);
		
		var elements = [];
		jQuery.each(result, function() {
			resultElement = this;
			jQueryElement = jQuery(resultElement);
			
			var element = {
				element: resultElement,
				tag: resultElement.tagName.toLowerCase(),
				text: jQueryElement.text(),
				sourceText: jQueryElement.html(),
				attributeNames: jQueryElement.getAttributeNames(),
				attributes: jQueryElement.getAttributes(),
				uniqueSelector: jQueryElement.getPath(),
				location: jQueryElement.offset(),
				size: jQueryElement.dimension(),
			};
			elements.push(element);
		});
		
		return elements;
	};
	
	abmash.parsePredicates = function(predicates, booleanType) {
		var result = [];

		// process jQueryLists
		var firstAnd = true;
		var hasJQueryPredicates = false;
		jQuery.each(predicates, function() {
			var predicate = this;
			
			if(predicate.jQueryList) {
				var jQueryListResult = abmash.parseJQueryList(predicate.jQueryList, booleanType);
				result = abmash.mergeResult(result, jQueryListResult, booleanType, firstAnd);
				firstAnd = false;
				hasJQueryPredicates = true;
				
//				alert(jQueryList.toSource());
//				alert(booleanType);
//				alert(relationalResult);
//				abmash.highlight(jQueryListResult);
//				alert(result);
			}
		});
		
		// process boolean predicates
		jQuery.each(predicates, function() {
			var predicate = this;
			
			if(predicate.isBoolean) {
//				var booleanType = predicate.type;
				var predicateResult = abmash.parsePredicates(predicate.predicates, predicate.type);
				result = abmash.mergeResult(result, predicateResult, booleanType, firstAnd);
				firstAnd = false;
			}
			
		});
		
		if(!hasJQueryPredicates && result.length == 0) {
			result = jQuery('*:visible').get();
		}
		
		// process recursive predicates (direction and color)
		jQuery.each(predicates, function() {
			var predicate = this;
			var predicateResult = null;
			
			if(predicate.isDirection) {
//				var directionType = predicate.type;
				// TODO use weight of direction result elements
				var directionResult = abmash.parsePredicates(predicate.predicates, 'OR');
				
				switch(predicate.type) {
				case "ABOVE":
					predicateResult = jQuery(result).above(directionResult).get();
					break;
				case "BELOW":
					predicateResult = jQuery(result).below(directionResult).get();
					break;
				case "LEFTOF":
					predicateResult = jQuery(result).leftOf(directionResult).get();
					break;
				case "RIGHTOF":
					predicateResult = jQuery(result).rightOf(directionResult).get();
					break;
					
				default:
					alert("WRONG CLOSENESS TYPE: " + predicate.type);
					break;
				}
			} else if(predicate.isColor) {
				var color = predicate.color;
				var tolerance = predicate.tolerance;
				var dominance = predicate.dominance;
				predicateResult = jQuery(result).filterHasColor(color, tolerance, dominance).get();
			}
			
			if(predicateResult != null) {
				result = abmash.mergeResult(result, predicateResult, booleanType, firstAnd);
				firstAnd = false;
			}
		});
		
		return result;
	};
	
	abmash.mergeResult = function(result, newResult, booleanType, firstAnd) {
		switch (booleanType) {
		case "OR":
			// merge results
			result = result.concat(newResult);
			break;
			
		case "NOT":
			// remove not result from query result
			var allElements = jQuery('body, body *').get();
			var notResult = jQuery.map(allElements, function(element) { return jQuery.inArray(element, newResult) < 0 ? element : null;});
			newResult = notResult;
			// do not break here, the not result will be treated like an AND expression
			
		case "AND":
			// intersect results
			if(firstAnd) {
//				abmash.highlight(result);
//				abmash.highlight(newResult);
				result = result.concat(newResult);
			} else {
//				abmash.highlight(result);
//				abmash.highlight(newResult);
				result = jQuery.map(result, function(element) { return jQuery.inArray(element, newResult) < 0 ? null : element;});
//				abmash.highlight(result);
			}
			break;
			
		default:
			alert("WRONG BOOLEAN TYPE: " + booleanType);
			break;
		};
		
		return result;
	};

	abmash.parseJQueryList = function(jQueryList, booleanType) {
		var result = [];

		jQuery.each(jQueryList, function() {
			var JQuery = this;
//			alert(JQuery.toSource());
			var jQueryResult = abmash.parseJQuery(JQuery, booleanType);
//			abmash.highlight(jQueryResult);
			result = result.concat(jQueryResult);
		});
			
		return result;
	};
	
	abmash.parseJQuery = function(JQuery, booleanType) {
		var result = [];
		var jQueryCommandList = [];
		
		var selector = JQuery.selector;
		var weight = JQuery.weight;
		var jQueryCommands = JQuery.commands;
		
		jQuery.each(jQueryCommands, function() {
			var jQueryCommand = this;
			// the command list will be evaluated with JQuery after all commands of this predicate are processed
			jQueryCommandList.push(jQueryCommand.method + "(" + jQueryCommand.selector + ")");
		});
		
		var jQueryEval = "jQuery(" + selector + ")";
		jQueryEval += jQueryCommandList.length > 0 ? "." + jQueryCommandList.join(".") : "";
//		alert(jQueryEval);
		result = result.concat(abmash.evaluateJQuery(jQueryEval, weight));
//		abmash.highlight(result);
		
		return result;
	};
	
	abmash.evaluateJQuery = function(jQueryEval, weight) {
		var result = [];
		
		jQuery.globalEval("abmash.setData('commandResult', " + jQueryEval + ");");
		var commandResult = abmash.getData('commandResult');
		jQuery.each(jQuery(commandResult), function() {
		    var element = jQuery(this);
		    // add element if it is visible
		    // TODO :visible optional?

		    if(element.filter(':visible').length > 0 && element.parents('head').length == 0 && element.parents('html').length > 0) {
		    	result.push(element.get(0));
		    	// store priority for element based on selector weight
		    	abmash.setElementWeight(element.get(0), weight);
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
