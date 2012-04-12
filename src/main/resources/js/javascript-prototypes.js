String.prototype.trim = function() { return (this.replace(/^[\s\xA0]+/, "").replace(/[\s\xA0]+$/, "")); };

String.prototype.contains = function(str) { return this.indexOf(str) != -1; };

String.prototype.containsWord = function(str) { return new RegExp('\\b' + str + '\\b').test(this); };

String.prototype.startsWith = function(str) { return this.match("^" + str) == str; };

String.prototype.endsWith = function(str) { return this.match(str + "$") == str; };

Array.prototype.unique = function() {
    var a = this.concat();
    for(var i=0; i<a.length; ++i) {
        for(var j=i+1; j<a.length; ++j) {
            if(a[i] === a[j])
                a.splice(j, 1);
        }
    }

    return a;
};


// Only add setZeroTimeout to the window object, and hide everything
// else in a closure.
(function() {
    var timeouts = [];
    var messageName = "zero-timeout-message";

    // Like setTimeout, but only takes a function argument.  There's
    // no time argument (always zero) and no arguments (you have to
    // use a closure).
    function setZeroTimeout(fn) {
        timeouts.push(fn);
        window.postMessage(messageName, "*");
    }

    function handleMessage(event) {
        if (event.source == window && event.data == messageName) {
            event.stopPropagation();
            if (timeouts.length > 0) {
                var fn = timeouts.shift();
                fn();
            }
        }
    }

    window.addEventListener("message", handleMessage, true);

    // Add the one thing we want added to the window object.
    window.setZeroTimeout = setZeroTimeout;
})();

// alert handling
//document.hasAlert = false;
//window.alert = function(msg) {
//	document.hasAlert = true; document.lastAlert = msg;
//};
//
//function checkForWindowAlert() {
//	var alertText = "";
//	if(document.hasAlert) {
//		document.hasAlert = false;
//		alertText = document.lastAlert;
//	}
//	return alertText;
//}
	
// error handling
/*
window.jsErrors = [];
window.onerror = function(errorMessage) {
  window.jsErrors[window.jsErrors.length] = errorMessage;
}
window.onerror = function(msg, url, linenumber) {
	var error = document.createAttribute("javaScriptErrorMessage");
	error.nodeValue = msg + "\n  at line number " + linenumber + " (URL: " + url + ")";
	document.getElementsByTagName("body")[0].setAttributeNode(error);
};
*/