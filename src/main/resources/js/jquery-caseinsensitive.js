jQuery.fn.extend({

	textMatch: function (compareType, text, options) {
		var options = jQuery.extend({}, options);
		
		var nodes = [];

//		alert(text + ", " + compareType.toUpperCase());
		this.each(function() {
			var node = jQuery(this).get(0);
			if(jQuery.inArray(node.tagName.toLowerCase(), ['html', 'head', 'script', 'meta', 'link']) < 0 &&
				compareStrings((node.innerText || node.textContent || "").toLowerCase(), text.toLowerCase(), compareType.toUpperCase())) {
//				abmash.highlight(this);
				nodes.push(this);
			}
		});

		return jQuery(nodes);
	},
	
	attrMatch: function (compareType, attributeName, text, options) {
		var options = jQuery.extend({}, options);
		
		var nodes = [];

		this.each(function() {
			var node = jQuery(this).get(0);
			if(jQuery.inArray(node.tagName.toLowerCase(), ['html', 'head', 'script', 'meta', 'link']) < 0) {
				var textFound = false;
			    jQuery.each(jQuery(node.attributes), function(index) {
			        var attr = node.attributes[index];
			        if (attributeName == "*" || attributeName == attr.name.toLowerCase()) {
			            if (compareStrings(attr.value.toLowerCase(), text, compareType)) {
			            	textFound = true;
			            }
			        }
			    });
			    
			    if(textFound) {
			    	nodes.push(this);
			    }
			}
		});

		return jQuery(nodes);
	},
});

jQuery.extend(jQuery.expr[':'], {
	
	textMatch: function(node, index, match) {
	    var args = match[3].split(',').map(function(arg) {
	        return arg.replace(/^\\s*[\"']|[\"']\\s*$/g, '');
	    });

	    var compareType = jQuery.trim(args[0]).toUpperCase();
	    var text = jQuery.trim(args[1]).toLowerCase();
	    var options = jQuery.trim(args[2]);
	    
        return jQuery(node).textMatch(compareType, text, options).length > 0;
    },
    
	attrMatch: function(node, index, match) {
	    var args = match[3].split(',').map(function(arg) {
	        return arg.replace(/^\\s*[\"']|[\"']\\s*$/g, '');
	    });

	    var compareType = jQuery.trim(args[0]).toUpperCase();
	    var attributeName = jQuery.trim(args[1]).toLowerCase();
	    var text = jQuery.trim(args[2]).replace(/[\"']/g, '').toLowerCase();
	    var options = jQuery.trim(args[3]);
	    
        return jQuery(node).attrMatch(compareType, attributeName, text, options).length > 0;
    },
    
	// example for email adresses
	// jQuery('p:regex([A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4})')
	regex: function (node, index, match) {
		var element = jQuery(node);
		var r = new RegExp(match[3], 'i');
	    return r.test(element.text());
	},
    
});

compareStrings = function(str1, str2, type) {
    switch (type) {
    case "EXISTS":
        return str1.length > 0;
    case "EXACT":
        return str1 == str2;
    case "WORD":
        return str1.containsWord(str2);
    case "STARTSWITH":
        return str1.startsWith(str2);
    case "ENDSWITH":
        return str1.endsWith(str2);
    case "CONTAINS":
        return str1.contains(str2);
    default:
        return false;
    }
};

//levenshteinDistance = function(u, v) {
//	var m = u.length;
//	var n = v.length;
//	var D = [];
//	for(var i = 0; i <= m; i++) {
//		D.push([]);
//		for(var j = 0; j <= n; j++) {
//			D[i][j] = 0;
//		}
//	}
//	for(var i = 1; i <= m; i++) {
//		for(var j = 1; j <= n; j++) {
//			if (j == 0) {
//				D[i][j] = i;
//			} else if (i == 0) {
//				D[i][j] = j;
//			} else {
//				D[i][j] = [D[i-1][j-1] + (u[i-1] != v[j-1]), D[i][j-1] + 1, D[i-1][j] + 1].sort()[0];
//			}
//		}
//	}
//	return D[m][n];
//};