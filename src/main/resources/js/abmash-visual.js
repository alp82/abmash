(function(jQuery) {
	jQuery.fn.dimension = function() {
		return {width: this.outerWidth(), height: this.outerHeight()};
	};
	
//	jQuery.fn.collidesWith = function(target) {
//		var src=jQuery(this);
//		var x1=src.offset().left,y1=src.offset().top,w=src.outerWidth(),h=src.outerHeight();
//		var xp1=target.offset().left,yp1=target.offset().top,wp=target.outerWidth(),hp=target.outerHeight();
//		var x2=x1+w,y2=y1+h,xp2=xp1+wp,yp2=yp1+hp;
//		if(xp1>=x1 && xp1<=x2 ) {
//			if(yp1>=y1 && yp1<=y2) {
//				return true;
//			}
//			else if(y1>=yp1 && y1<=yp2) {
//				return true;
//			}
//		}
//		else if(x1>=xp1 && x1<=xp2) {
//			if(yp1>=y1 && yp1<=y2) {
//				return true;
//			}
//			else if(y1>=yp1 && y1<=yp2) {
//				return true;
//			}
//		}
//		return false;
//	};
	
	jQuery.fn.elementWithinElement = function(elemPossiblyCovering) {
		var elemPossiblyCovered = jQuery(this).get(0);
		elemPossiblyCovering = jQuery(elemPossiblyCovering).get(0);
		
		var top = elemPossiblyCovered.offsetTop;
		var left = elemPossiblyCovered.offsetLeft;
		var width = elemPossiblyCovered.offsetWidth;
		var height = elemPossiblyCovered.offsetHeight;

		while (elemPossiblyCovered.offsetParent) {
			elemPossiblyCovered = elemPossiblyCovered.offsetParent;
			top += elemPossiblyCovered.offsetTop;
			left += elemPossiblyCovered.offsetLeft;
		}

		return (top >= elemPossiblyCovering.offsetTop
				&& left >= elemPossiblyCovering.offsetLeft
				&& (top + height) <= (elemPossiblyCovering.offsetTop + elemPossiblyCovering.offsetHeight) && (left + width) <= (elemPossiblyCovering.offsetLeft + elemPossiblyCovering.offsetWidth));
	};

})(jQuery); 

(function(abmash) {
	// constants
	var directionType = {
		closeTo: 1,
		hasLabel: 5,
		hasLabelForSelect: 6,
		above: 10,
		below: 20,
		leftOf: 30,
		rightOf: 40,
	};
	var distanceType = {
		topLeft: 1,
		top: 2,
		topRight: 3,
		left: 4,
		center: 5,
		right: 6,
		bottomLeft: 7,
		bottom: 8,
		bottomRight: 9,
		average: 10,
	};
	var calculationType = {
		min: 1,
		average: 2,
	};

	// closeness query
	var options = {};
    
    jQuery.fn.extend({
    	closeTo: function(referenceElements) {
    		return abmash.elementsInDirection({
		    	sources: this,
		    	targets: jQuery(referenceElements),
		    	direction: directionType.closeTo,
		    	distance: distanceType.center,
		    	calculation: calculationType.min,
		    	inBounds: false,
		    	directionHasToMatchAllTargets: false,
		    });
    	},
    	hasLabel: function(referenceElements) {
    		return abmash.elementsInDirection({
		    	sources: this,
		    	targets: jQuery(referenceElements),
		    	direction: directionType.hasLabel,
		    	distance: distanceType.topLeft,
		    	calculation: calculationType.min,
		    	inBounds: false,
		    	directionHasToMatchAllTargets: false,
		    });
    	},
    	hasLabelForSelect: function(referenceElements) {
    		return abmash.elementsInDirection({
		    	sources: this,
		    	targets: jQuery(referenceElements),
		    	direction: directionType.hasLabelForSelect,
		    	distance: distanceType.center,
		    	calculation: calculationType.min,
		    	inBounds: false,
		    	directionHasToMatchAllTargets: false,
		    });
    	},
    	above: function(referenceElements) {
    		return abmash.elementsInDirection({
		    	sources: this,
		    	targets: jQuery(referenceElements),
		    	direction: directionType.above,
		    	distance: distanceType.bottomLeft,
		    	calculation: calculationType.min,
		    	inBounds: false,
		    	directionHasToMatchAllTargets: false,
		    });
    	},
    	below: function(referenceElements) {
    		return abmash.elementsInDirection({
		    	sources: this,
		    	targets: jQuery(referenceElements),
		    	direction: directionType.below,
		    	distance: distanceType.topLeft,
		    	calculation: calculationType.min,
		    	inBounds: false,
		    	directionHasToMatchAllTargets: false,
		    });
    	},
    	leftOf: function(referenceElements) {
    		return abmash.elementsInDirection({
		    	sources: this,
		    	targets: jQuery(referenceElements),
		    	direction: directionType.leftOf,
		    	distance: distanceType.right,
		    	calculation: calculationType.min,
		    	inBounds: false,
		    	directionHasToMatchAllTargets: false,
		    });
    	},
    	rightOf: function(referenceElements) {
    		return abmash.elementsInDirection({
		    	sources: this,
		    	targets: jQuery(referenceElements),
		    	direction: directionType.rightOf,
		    	distance: distanceType.left,
		    	calculation: calculationType.min,
		    	inBounds: false,
		    	directionHasToMatchAllTargets: false,
		    });
    	},
    	aboveAll: function(referenceElements) {
    		return abmash.elementsInDirection({
		    	sources: this,
		    	targets: jQuery(referenceElements),
		    	direction: directionType.above,
		    	distance: distanceType.bottomLeft,
		    	calculation: calculationType.average,
		    	inBounds: false,
		    	directionHasToMatchAllTargets: true,
    	    });
    	},
    	belowAll: function(referenceElements) {
    		return abmash.elementsInDirection({
		    	sources: this,
		    	targets: jQuery(referenceElements),
		    	direction: directionType.below,
		    	distance: distanceType.topLeft,
		    	calculation: calculationType.average,
		    	inBounds: false,
		    	directionHasToMatchAllTargets: true,
    	    });
    	},
    	leftOfAll: function(referenceElements) {
    		return abmash.elementsInDirection({
		    	sources: this,
		    	targets: jQuery(referenceElements),
		    	direction: directionType.leftOf,
		    	distance: distanceType.right,
		    	calculation: calculationType.average,
		    	inBounds: false,
		    	directionHasToMatchAllTargets: true,
    	    });
    	},
    	rightOfAll: function(referenceElements) {
    		return abmash.elementsInDirection({
		    	sources: this,
		    	targets: jQuery(referenceElements),
		    	direction: directionType.rightOf,
		    	distance: distanceType.left,
		    	calculation: calculationType.average,
		    	inBounds: false,
		    	directionHasToMatchAllTargets: true,
    	    });
    	},
    });
    
    jQuery.extend(abmash, {
    	elementsInDirection: function(closenessOptions) {
			var resultElements = [];
			
    		// use default options if not specified
		    options = jQuery.extend({
		    	sources: jQuery(),
		    	targets: jQuery(),
		    	direction: directionType.hasLabel,
		    	distance: distanceType.average,
		    	calculation: calculationType.min,
		    	inBounds: false,
		    	directionHasToMatchAllTargets: false,
		    }, closenessOptions);
		    
			// TODO remove
			if(abmash.getData('debug2')) {
				alert(options.sources.get());
				alert(options.targets.get());
				alert(options.direction);
			}	

		    
//			jQuery(options.sources).css('background-color', 'green');
			jQuery.each(options.sources, function() {
				var source = jQuery(this);
			    if(source.get().length > 0 && abmash.checkElementLocations(source)) {
			    	resultElements.push(source.get(0));
			    }
			});
			
//			jQuery(resultElements).css('background-color', 'yellow');
			// now return the sorted result set
			return resultElements.length > 0 ? resultElements.sort(sortByCloseness) : [];
    	},
		
    	checkElementLocations: function(source) {
		    var result;
		    
		    // closeTo is only about ordered results, no filter is applied
		    if(options.direction == directionType.closeTo) {
		    	return true;
		    }
		    
		    // the html document's root element cannot be taken for closeness calculations
		    if(source.get(0) == document) {
		    	return false;
		    }
		    
		    if(options.directionHasToMatchAllTargets) {
		    	result = true;
		    } else {
		    	result = false;
		    }

		    jQuery.each(options.targets, function() {
			    var target = jQuery(this);
			    var coords = calculateCoordinates(source, target);
				
				if(options.direction == directionType.hasLabel) {
					var hasLabel = abmash.isBelow(coords) || abmash.isRightTo(coords);
					if(options.directionHasToMatchAllTargets && !hasLabel) {
						result = false;
						return false;
					} else if(hasLabel) {
						result = true;
						return false;
					}
				}
				
				if(options.direction == directionType.hasLabelForSelect) {
					var hasLabel = abmash.isLeftTo(coords);
					if(options.directionHasToMatchAllTargets && !hasLabel) {
						result = false;
						return false;
					} else if(hasLabel) {
						result = true;
						return false;
					}
				}
				
				if(options.direction == directionType.above) {
					var isAbove = abmash.isAbove(coords) && (!options.inBounds || abmash.isInHorizontalBounds(coords));
					if(options.directionHasToMatchAllTargets && !isAbove) {
						result = false;
						return false;
					} else if(isAbove) {
						result = true;
						return false;
					}
				}
				
				if(options.direction == directionType.below) {
					var isBelow = abmash.isBelow(coords) && (!options.inBounds || abmash.isInHorizontalBounds(coords));
					if(options.directionHasToMatchAllTargets && !isBelow) {
						result = false;
						return false;
					} else if(isBelow) {
						result = true;
						return false;
					}
				}
				
				if(options.direction == directionType.leftOf) {
					var isLeftOf = abmash.isLeftOf(coords) && (!options.inBounds || abmash.isInVerticalBounds(coords));
					if(options.directionHasToMatchAllTargets && !isLeftOf) {
						result = false;
						return false;
					} else if(isLeftOf) {
						result = true;
						return false;
					}
				}
				
				if(options.direction == directionType.rightTo) {
					var isRightOf = abmash.isRightOf(coords) && (!options.inBounds || abmash.isInVerticalBounds(coords));
					if(options.directionHasToMatchAllTargets && !isRightOf) {
						result = false;
						return false;
					} else if(isRightOf) {
						result = true;
						return false;
					}
				}
			});
		    
		    return result;
		},
		
		isAbove: function(coords) {
			return coords.bottomSource < coords.topTarget;
		},
		
		isBelow: function(coords) {
			return coords.topSource > coords.bottomTarget;
		},
		
		isLeftOf: function(coords) {
			return coords.rightSource < coords.leftTarget;
		},
		
		isRightOf: function(coords) {
			return coords.leftSource > coords.rightTarget;
		},
		
		isInHorizontalBounds: function(coords) {
			return !isLeftOf(coords) && !isRightOf(coords);
		},
		
		isInVerticalBounds: function(coords) {
			return !isAbove(coords) && !isBelow(coords);
		},
		
    });
    
    function sortByCloseness(firstElement, secondElement) {
    	// TODO collision needed?
    	if(jQuery(firstElement).elementWithinElement(secondElement)) {
    		return 1;
    	}
    	if(jQuery(secondElement).elementWithinElement(firstElement)) {
    		return -1;
    	}
    	
    	var firstDistance = calculateDistance(firstElement);
    	var secondDistance = calculateDistance(secondElement);
		
		return firstDistance - secondDistance;
	}
    
    function calculateDistance(element) {
    	// zero or maxint depending on closeness matching 
    	var distance = options.directionHasToMatchAllTargets ? 0 : 9007199254740992;
    	
		var totalWeight = 0; 
    	jQuery.each(options.targets, function() {
		    var target = jQuery(this);
		    var dist = getDistance(element, target);
		    // TODO higher weight for first targets
			var weight = getWeight(target);
			totalWeight += weight;
			
			if(options.directionHasToMatchAllTargets) {
				distance += dist * weight;
			} else {
				distance = Math.min(distance, dist * weight);
			}
    	});
    	
    	// total distance
    	distance /= totalWeight;
    	distance = options.directionHasToMatchAllTargets ? distance / jQuery(options.targets).get().length : distance;
    	
    	// distance gets higher for bigger elements
    	distance *= Math.sqrt(jQuery(element).dimension().width * jQuery(element).dimension().height);
    	
    	// distance is correlated to element weight
    	distance /= abmash.getElementWeight(element);
    	
    	return distance;
    }
    
    function getDistance(source, target) {
		// get coordinates
		var coords = calculateCoordinates(source, target);
		
		// calculate euclidean distances
		var distanceTopLeft = euclideanDistance(coords.diffLeft, coords.bottomSource - coords.topTarget);
		var distanceTop = euclideanDistance(coords.diffCenterX, coords.bottomSource - coords.topTarget);
		var distanceTopRight = euclideanDistance(coords.diffRight, coords.bottomSource - coords.topTarget);
		var distanceLeft = euclideanDistance(coords.rightSource - coords.leftTarget, coords.diffCenterY);
		var distanceCenter = euclideanDistance(coords.diffCenterX, coords.diffCenterY);
		var distanceRight = euclideanDistance(coords.leftSource - coords.rightTarget, coords.diffCenterY);
		var distanceBottomLeft = euclideanDistance(coords.diffLeft, coords.topSource - coords.bottomTarget);
		var distanceBottom = euclideanDistance(coords.diffCenterX, coords.topSource - coords.bottomTarget);
		var distanceBottomRight = euclideanDistance(coords.diffRight, coords.topSource - coords.bottomTarget);
		
		if(options.distance == distanceType.topLeft) return distanceTopLeft;
		else if(options.distance == distanceType.top) return distanceTop;
		else if(options.distance == distanceType.topRight) return distanceTopRight;
		else if(options.distance == distanceType.left) return distanceLeft;
		else if(options.distance == distanceType.center) return distanceCenter;
		else if(options.distance == distanceType.right) return distanceRight;
		else if(options.distance == distanceType.bottomLeft) return distanceBottomLeft;
		else if(options.distance == distanceType.bottom) return distanceBottom;
		else if(options.distance == distanceType.bottomRight) return distanceBottomRight;
		// TODO if TYPABLE then distanceTopLeft
		else return (distanceTopLeft + distanceCenter + distanceBottomRight) / 3;
		
		// TODO this should throw an error
		return;
	}
    
    function getWeight(target) {
    	// TODO weights
//		for (ElementType elementType: element.getTypes()) {
//			switch (elementType) {
//			case TYPABLE:
//			case CHOOSABLE:
//			case DATEPICKER:
//				if (element.getTagName().equals("label")) {
//					weight *= 3;
//				}
//				// TODO more weight on exact matches
//			case IMAGE:
//				// TODO font-weight 400 as constant. research if 400 is default everywhere
//				String fontWeight;
//				if ((fontWeight = element.getCssValue("font-weight")) != null) {
//					weight *= Double.parseDouble(fontWeight) / 400;
//				}
//				if (element.getTagName().equals("strong")) {
//					weight *= 2.0;
//				}
//				if (element.getTagName().equals("li")) {
//					weight *= 1.5;
//				}
//				break;
//			default:
//				break;
//			}		
//		}
    	return 1;
    }
    
    function calculateCoordinates(source, target) {
		source = jQuery(source);
		target = jQuery(target);
		
		coords = {
			topSource: source.offset().top,
		    leftSource: source.offset().left,
			topTarget: target.offset().top,
			leftTarget: target.offset().left,
		};
		
		coords = jQuery.extend(coords, {
			bottomSource: coords.topSource + source.height(),
		    rightSource: coords.leftSource + source.width(),
			bottomTarget: coords.topTarget + target.height(),
			rightTarget: coords.leftTarget + target.width(),
		});
		
		coords = jQuery.extend(coords, {
			centerXSource: coords.leftSource + (coords.rightSource - coords.leftSource) / 2,
			centerYSource: coords.topSource + (coords.bottomSource - coords.topSource) / 2,
			centerXTarget: coords.leftTarget + (coords.rightTarget - coords.leftTarget) / 2,
			centerYTarget: coords.topTarget + (coords.bottomTarget - coords.topTarget) / 2,
		});
		
		coords = jQuery.extend(coords, {
			diffLeft: coords.leftTarget - coords.leftSource,
			diffRight: coords.rightTarget - coords.rightSource,
			diffCenterX: coords.centerXTarget - coords.centerXSource,
			diffCenterY: coords.centerYTarget - coords.centerYSource,
		});
		
		return coords;
	}
    
    function euclideanDistance(x, y) {
    	return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
	
})(window.abmash = window.abmash || {});