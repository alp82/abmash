(function(jQuery) {
	jQuery.fn.dimension = function() {
		return [this.outerWidth(), this.outerHeight()];
	}
})(jQuery);

jQuery.expr[':'].hasLabel = function(node, stackIndex, properties) {
	return jQuery().elementIsInDirection("hasLabel", node, properties[3]);
}

jQuery.expr[':'].above = function(node, stackIndex, properties) {
    return jQuery().elementIsInDirection("above", node, properties[3]);
};

jQuery.expr[':'].below = function(node, stackIndex, properties) {
    return jQuery().elementIsInDirection("below", node, properties[3]);
};

jQuery.expr[':'].leftTo = function(node, stackIndex, properties) {
    return jQuery().elementIsInDirection("leftTo", node, properties[3]);
};

jQuery.expr[':'].rightTo = function(node, stackIndex, properties) {
    return jQuery().elementIsInDirection("rightTo", node, properties[3]);
};

jQuery.expr[':'].aboveAll = function(node, stackIndex, properties) {
    return jQuery().elementIsInDirection("above", node, properties[3], {
    	directionHasToMatchAllTargets: true,
    });
};

jQuery.expr[':'].belowAll = function(node, stackIndex, properties) {
    return jQuery().elementIsInDirection("below", node, properties[3], {
    	directionHasToMatchAllTargets: true,
    });
};

jQuery.expr[':'].leftToAll = function(node, stackIndex, properties) {
    return jQuery().elementIsInDirection("leftTo", node, properties[3], {
    	directionHasToMatchAllTargets: true,
    });
};

jQuery.expr[':'].rightToAll = function(node, stackIndex, properties) {
    return jQuery().elementIsInDirection("rightTo", node, properties[3], {
    	directionHasToMatchAllTargets: true,
    });
};


(function(jQuery) {
	// source and target coordinates
	var topSource;
    var leftSource;
    var bottomSource;
    var rightSource;
	var topTarget;
    var leftTarget;
    var bottomTarget;
    var rightTarget;
	
	jQuery.elementIsInDirection = function(direction, source, targets, options) {
		topSource = jQuery(source).offset().top;
	    leftSource = jQuery(source).offset().left;
	    bottomSource = topSource + jQuery(source).height();
	    rightSource = leftSource + jQuery(source).width();
	    
	    // default options
	    options = jQuery.extend({
	    	directionHasToMatchAllTargets: false,
	    }, options);
	    
	    jQuery.each(function() {
	    	var target = this;
	    	topTarget = jQuery(target).offset().top;
	        leftTarget = jQuery(target).offset().left;
	        bottomTarget = topTarget + jQuery(target).height();
	        rightTarget = leftTarget + jQuery(target).width();
	        
	        if(direction == "hasLabel") {
	        	var hasLabel = isBelow() || isRightTo();
	        	if(options.directionHasToMatchAllTargets && !hasLabel) {
	        		return false;
	        	} else if(hasLabel) {
	        		return true;
	        	}
	        }
	        
	        if(direction == "above") {
	        	var isAbove = isAbove() && isInHorizontalBounds();
	        	if(options.directionHasToMatchAllTargets && !isAbove) {
	        		return false;
	        	} else if(isAbove) {
	        		return true;
	        	}
	        }

	        if(direction == "below") {
	            var isBelow = isBelow() && isInHorizontalBounds();
	        	if(options.directionHasToMatchAllTargets && !isBelow) {
	        		return false;
	        	} else if(isBelow) {
	        		return true;
	        	}
	        }

	        if(direction == "leftTo") {
	        	var isLeftTo = isLeftTo() && isInVerticalBounds();
	        	if(options.directionHasToMatchAllTargets && !isLeftTo) {
	        		return false;
	        	} else if(isLeftTo) {
	        		return true;
	        	}
	        }

	        if(direction == "rightTo") {
	        	var isRightTo = isRightTo() && isInVerticalBounds();
	        	if(options.directionHasToMatchAllTargets && !isRightTo) {
	        		return false;
	        	} else if(isRightTo) {
	        		return true;
	        	}
	        }
	    });

	    return false;
	};
	
	function isAbove() {
		return bottomSource <= topTarget;
	}
	
	function isBelow() {
		return topSource >= bottomTarget;
	}
	
	function isLeftTo() {
		return rightSource <= leftTarget;
	}
	
	function isRightTo() {
		return leftSource >= rightTarget;
	}
	
	function isInHorizontalBounds() {
		return !isLeftTo() && !isRightTo();
	}
	
	function isInVerticalBounds() {
		return !isAbove() && !isBelow();
	}
	
})(jQuery);
