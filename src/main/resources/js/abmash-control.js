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

})(window.abmash = window.abmash || {});

//jQuery(document).ready(function(jQuery) {
//	abmash.foo();
//});