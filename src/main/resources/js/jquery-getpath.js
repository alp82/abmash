jQuery.fn.extend({
	getPath: function (options) {
		var options = jQuery.extend({
			generalize: false,
			generalizeEachLevel: false,
		}, options);
		var generalizedAmount = 1;
		
		var path, node = this;
		while (node.length) {
			var realNode = node[0], name = realNode.localName;
			if (!name) break;
			name = name.toLowerCase();
			
			// support for namespaced tag names
			name = name.replace(":", "\\:");

			var parent = node.parent();

			var sameTagSiblings = parent.children(name);
			if (sameTagSiblings.length > 1) { 
				var generalize = false;
				if(options.generalize) {
					if(generalizeEachLevel && generalizedAmount < options.generalize ||
					!generalizeEachLevel && generalizedAmount == options.generalize	) {
						generalizedAmount++;
					}
				} else {
					allSiblings = parent.children();
					var index = allSiblings.index(realNode) + 1;
					if (index > 1) {
						name += ':nth-child(' + index + ')';
					}
				}
			}

			path = name + (path ? '>' + path : '');
			node = parent;
		}

		return path;
	}
});

jQuery.fn.distinctDescendants = function() {
    var nodes = [];
    var parents = [];

    // First, copy over all matched elements to nodes and remove duplicates.
    jQuery(this).each(function(index, element) {
    	if(jQuery.inArray(element, nodes) < 0) {
    		nodes.push(element);
    	}
    });
   

    // Then, for each of these nodes, check if it is parent to some element.
    for (var i=0; i<nodes.length; i++) {
        var nodeToCheck = nodes[i];
        jQuery(this).each(function(index, element) {

            // Skip self comparisons.
            if (element == nodeToCheck) {
                return;
            }

            // Use .tagName to allow .find() to work properly.
            if((jQuery(nodeToCheck).find(element.tagName).length > 0)) {
                if (parents.indexOf(nodeToCheck) < 0) {
                    parents.push(nodeToCheck);
                }
            }
        });
    }
    
    // Finally, construct the result.
    var result = [];
    for (var i=0; i<nodes.length; i++) {
        var nodeToCheck = nodes[i];
        
        // add to result if the element is a leaf node or has different contents than its child
        if (parents.indexOf(nodeToCheck) < 0) {
        	// the element is a leaf node
            result.push(nodeToCheck);
        } else {
            // check if parent has different text than its children
        	var isDifferent = true;
            jQuery(nodeToCheck).children().each(function() {
            	var child = jQuery(this);
            	// the node is different if its text differs and the child is in the initial result set
            	if(jQuery.trim(child.text()) == jQuery.trim(jQuery(nodeToCheck).text()) && nodes.indexOf(child.get(0)) >= 0) {
            		isDifferent = false;
            	}
            });
            if(isDifferent) {
            	result.push(nodeToCheck);
            }
        }
    }

    return jQuery(result);
};