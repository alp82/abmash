(function(abmash) {
	var tempElement = null;
	var tempData = null;

	// private functions
	
//	function foo() {
//	}
	
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
	
	abmash.query = function(queries, rootElements) {
		var response = {};
		// TODO how do multiple root elements work?
		abmash.setTempElement(rootElements.length > 0 ? rootElements.join(',') : document.body);
		jQuery.each(jQuery.parseJSON(queries), function() {
			condition = this;
			//condition.type
			response[condition.type] = {}
			jQuery.each(condition.selectorGroups, function() {
				selectorGroup = this;
				//selectorGroup.type
				response[condition.type][selectorGroup.type] = {}
				jQuery.each(selectorGroup.selectors, function() {
					selector = this;
					//selector.type
//					alert(abmash.getTempElement());
//					alert(selector.command);
//					jQuery.globalEval("abmash.setTempData(" + selector.command + ");");
					abmash.setTempElement('*');
					abmash.setTempData(jQuery(abmash.getTempElement()).find("*:contains['Bilder']"));
					var queryResult = abmash.getTempData();
//					alert(queryResult);
					if(queryResult) {
						alert(selector.type);
//						response[condition.type][selectorGroup.type][selector.type] = queryResult;
					}
				});
			});
		});
		return response;
	}

})(window.abmash = window.abmash || {});

//jQuery(document).ready(function(jQuery) {
//	abmash.foo();
//});