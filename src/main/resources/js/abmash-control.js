(function(abmash) {
	var queryElementsFound = 0;
	var queryLimit = 0;
	var queryData = {};
	var elementWeight = {};
	var elementInIframe = {};
	var referenceElements = {};

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
	
	abmash.deleteData = function(key) {
		delete queryData[key];
	};
	
	// TODO for subqueries (color, closeness)
	abmash.setElementWeight = function(element, weight) {
		elementWeight[jQuery(element).getPath()] = Math.max(weight, abmash.getElementWeight(element));
	};

	abmash.getElementWeight = function(element, options) {
//		if(document.title.contains("Select a Car")) {
//			alert("element: " + element);
//			alert("options: " + options);
//			alert("path: " + jQuery(element).getPath());
//			alert("type: " + (typeof elementWeight[jQuery(element).getPath()]));
//		}		

		options = jQuery.extend({
		}, options);
		
		if(typeof elementWeight[jQuery(element).getPath()] != "undefined") return elementWeight[jQuery(element).getPath()];
		return 1;
	};
	
	abmash.query = function(jsonPredicates, refElements) {
		// prepare global variables
		elementWeight = {};
		elementInIframe = {};
		referenceElements = refElements;
		abmash.deleteData('elementsForFilteringQuery');
		abmash.deleteData('weightForDirectionQuery');
		abmash.deleteData('subQueryLevel');

		// query, remove duplicates and sort afterwards
		var result = abmash.parsePredicates(JSON.parse(jsonPredicates), 'AND');
		result = jQuery(result).distinctDescendants().get();
		result = jQuery.unique(result).sort(sortByWeight);
		
		var elements = [];
		jQuery.each(result, function() {
			resultElement = this;
			jQueryElement = jQuery(resultElement);
			
			// return all element relevant data
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
			
			// check if element is in iframe and return that information
			if(typeof elementInIframe[jQueryElement.getPath()] != "undefined") {
				element = jQuery.extend(element, { iframe: elementInIframe[jQueryElement.getPath()] });
			}
			
//			if(abmash.getData('test')) {
//				abmash.highlight(resultElement);
//				alert(abmash.getElementWeight(resultElement));
//			}
			
			// finally add the information to the result set
			elements.push(element);
		});
		
		return elements;
	};
	
	abmash.parsePredicates = function(predicates, booleanType) {
		var result = [];
		// TODO only if necessary (no "normal" jquerylist)
		abmash.setData('elementsForFilteringQuery', jQuery('*:visible:not(html,head,head *)'));
		
		// process element predicates
		var firstAnd = true;
		jQuery.each(predicates, function() {
			var predicate = this;
			
			if(predicate.referenceId) {
				result = abmash.mergeResult(result, referenceElements[predicate.referenceId], booleanType, firstAnd);
				firstAnd = false;
			}
		});

		// process jQueryLists
		var hasJQueryPredicates = false;
		jQuery.each(predicates, function() {
			var predicate = this;
			
			if(predicate.jQueryList) {
				var jQueryListResult = abmash.parseJQueryList(predicate.jQueryList, booleanType);
				result = abmash.mergeResult(result, jQueryListResult, booleanType, firstAnd);
				abmash.setData('elementsForFilteringQuery', jQuery(result));
				firstAnd = false;
				hasJQueryPredicates = true;
				
//				if(abmash.getData('test') && abmash.getData('subQueryLevel') && abmash.getData('subQueryLevel') > 0) {
//					abmash.highlight(jQueryListResult);
//					alert(booleanType);
//					alert(firstAnd);
//					abmash.highlight(result);
//				}
				
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
				var predicateResult = abmash.parsePredicates(predicate.predicates, predicate.type);
				result = abmash.mergeResult(result, predicateResult, booleanType, firstAnd);
				firstAnd = false;
			}
			
		});
		
//		if(abmash.getData('test')) {
//			alert(predicates.toSource());
//			abmash.highlight(result);
//			if(predicates[0].referenceId) {
//				alert(predicates[0].referenceId);
//				abmash.highlight(referenceElements[predicates[0].referenceId]);
//			}
//			abmash.highlight(abmash.getData('elementsForFilteringQuery'));
//		}

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
			//var allElements = jQuery('body, body *').get();
			var allElements = abmash.getData('elementsForFilteringQuery');
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
		
		abmash.setData('elementsForFilteringQuery', jQuery(result));
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
		
		var subQueryId = null;
		
		abmash.setData('weightForDirectionQuery', weight);
		
		jQuery.each(jQueryCommands, function() {
			var jQueryCommand = this;
			// the command list will be evaluated with JQuery after all commands of this predicate are processed
			if(jQueryCommand.predicates) {
				subQueryId = "subQuery" + (new Date().getTime()) + ((Math.random() * 900) + 100);
				
				// temporarily save intermediate result
				var tempElements = abmash.getData('elementsForFilteringQuery');
				var tempWeight = abmash.getData('weightForDirectionQuery');
				
				// process subquery predicates
//				if(abmash.getData('test')) alert(jQueryCommand.predicates.toSource());
//				alert(jQueryCommand.predicates.toSource());
				abmash.setData('subQueryLevel', abmash.getData('subQueryLevel') ? abmash.getData('subQueryLevel') + 1 : 1);
				abmash.setData(subQueryId, abmash.parsePredicates(jQueryCommand.predicates, 'AND'));
//				abmash.highlight(abmash.getData('subQuery' + subQueryId));
//				if(abmash.getData('test')) abmash.highlight(abmash.getData('subQuery' + subQueryId));
				jQueryCommandList.push(jQueryCommand.method + "(abmash.getData('" + subQueryId + "')," + jQueryCommand.selector + ")");
				
				// restore temporarily saved intermediate result
				abmash.setData('elementsForFilteringQuery', tempElements);
				abmash.setData('weightForDirectionQuery', tempWeight);
				abmash.setData('subQueryLevel', abmash.getData('subQueryLevel') - 1);
			} else {
				jQueryCommandList.push(jQueryCommand.method + "(" + jQueryCommand.selector + ")");
			}
		});
		
		var jQueryEval = "jQuery(" + selector + ")";
		jQueryEval += jQueryCommandList.length > 0 ? "." + jQueryCommandList.join(".") : "";
//		if(abmash.getData('test')) alert(jQueryEval);
//		alert(jQueryEval);
		result = result.concat(abmash.evaluateJQuery(jQueryEval, weight, subQueryId));
		abmash.deleteData(subQueryId);
//		abmash.highlight(result);
//		if(abmash.getData('test')) abmash.highlight(result);
		
		return result;
	};
	
	abmash.evaluateJQuery = function(jQueryEval, weight, subQueryId) {
		var result = [];
		
//		jQuery.globalEval("abmash.setData('commandResult', " + jQueryEval + ");");
		abmash.eval("abmash.setData('jQueryResult', " + jQueryEval + ");");
		var jQueryResult = abmash.getData('jQueryResult');
		// add element if it is visible
		// TODO optionally with :visible? but does not work with closeTo command (return no result, i dont know why)
		jQuery.each(jQuery(jQueryResult)/*.distinctDescendants()/*.filter(':visible:not(html,head,head *)')*/, function() {
		    var element = jQuery(this);
		    
		    // check if element is located in iframe
		    jQuery('iframe').each(function() {
		    	try {
			    	if(jQuery(this).contents().find(element.get(0).tagName).length > 0){
			    		var iframe = jQuery(this);
			    		elementInIframe[element.getPath()] = iframe.get(0);
			    		return false;
			    	}
		    	} catch(e) {
		    		// iframe is on another domain and cannot be accessed due to same origin policy
		    	}
		    });
		    
			result.push(element.get(0));
			
	    	// store priority for element based on selector weight
			// TODO only if this is not a subquery
//			if(subQueryId != null) {
				abmash.setElementWeight(element.get(0), weight);
//			}
	    	
	    	// increment element count
//		    queryElementsFound++;
//		    if(groupLimit > 0 && groupResult.length >= groupLimit) return false;
		});
//		alert(jQueryEval);
//		abmash.highlight(jQueryResult);
//		abmash.highlight(result);
		return result;
	};
	
	abmash.eval = function(evalString) {
		try {
		  eval(evalString);
		} catch(e) {
		  var err = e.constructor('Eval Error: ' + e.message + '\nScript: ' + evalString);
		  // +3 because `err` has the line number of the `eval` line plus two.
		  err.lineNumber = e.lineNumber - err.lineNumber + 3;
		  alert(err);
		  throw err;
		}
	}
	
    function sortByWeight(firstElement, secondElement) {
    	var firstWeight = abmash.getElementWeight(firstElement);
    	var secondWeight = abmash.getElementWeight(secondElement);
		return secondWeight - firstWeight;
	}
    
})(window.abmash = window.abmash || {});
