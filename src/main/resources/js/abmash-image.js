	// !!!!!!!!!!!!!! TODO !!!!!!!!!!!!!
//	public Color getClosestNamedColor() {
//		SortedSet<Entry<ColorName, Double>> distances = getDistancesToNamedColors();
//		return distances.first().getKey().getColor();
//	}
//	
	// !!!!!!!!!!!!!! TODO !!!!!!!!!!!!!
//	public SortedSet<Entry<ColorName, Double>> getDistancesToNamedColors() {
//		Map<ColorName, Double> unsortedDistances = new TreeMap<ColorName, Double>();
//		
//		ColorName[] colorNames = ColorName.values();
//		for (ColorName color: colorNames) {
//			double averageDistance = getAverageDistance(color.getColor());
//			unsortedDistances.put(color, averageDistance);
//		}
//		
//		return DataTypeConversion.entriesSortedByValues(unsortedDistances);
//	}
	
(function(jQuery) {
	
	// TODO sorting!
//	return resultElements ? resultElements.sort(sortByCloseness) : resultElements;
//    function sortByCloseness(firstElement, secondElement) {
//    	var firstDistance = calculateDistance(firstElement);
//    	var secondDistance = calculateDistance(secondElement);
//		
//		return firstDistance <= secondDistance ? -1 : 1;
//	}
    
	var filterIsColor = function(color, tolerance, dominance, node) {
		var result = abmash.filterColor("is", node, color, tolerance);
		return result ? node : false;
	};

	var filterHasColor = function(color, tolerance, dominance, node) {
		var result = abmash.filterColor("has", node, color, tolerance, dominance);
		return result ? node : false;
	};

//	jQuery.filterHasColor = function(color, tolerance, dominance) {
//		return jQuery(filterHasColor(color, tolerance, dominance, jQuery('*')));
//	}

	jQuery.fn.filterIsColor = function(color, tolerance) {
		var nodes = [];

		this.each(function() {
			node = filterIsColor(color, tolerance, this);
			if(node) nodes.push(node);
		});
		
		return this.pushStack(nodes, "filterIsColor", color, tolerance);
	};
	
	jQuery.fn.filterHasColor = function(options) {
		var nodes = [];

//		alert(options.toSource());
		
		var color = options.color;
		var tolerance = options.tolerance;
		var dominance = options.dominance;

		this.each(function() {
//			abmash.highlight(this);
//			alert(color.toSource());
//			alert(tolerance);
//			alert(dominance);
//			alert(abmash.filterColor("get", this, color, tolerance, dominance));
//			alert(abmash.filterColor("has", this, color, tolerance, dominance));
			node = filterHasColor(color, tolerance, dominance, this);
			if(node) nodes.push(node);
		});
//		abmash.highlight(nodes);
		
		return this.pushStack(nodes, "filterHasColor", options);
	};
	
})(jQuery);

(function(abmash) {
	
	var imageDataSegments = "";
    
    jQuery.extend(abmash, {
 
    	buildImageDataForPageScreenshot: function(imageData) {
			imageDataSegments += imageData;
    	},
    	
    	updatePageScreenshot: function(width, height) {
    		// (re)create canvas
			if(jQuery('canvas#abmashCurrentPageScreenshot').length > 0) {
				jQuery('canvas#abmashCurrentPageScreenshot').remove();
			}
			jQuery(document.body).append('<canvas id="abmashCurrentPageScreenshot"></canvas>');
	 		jQuery('canvas#abmashCurrentPageScreenshot').hide();
    		
			var canvas = jQuery('canvas#abmashCurrentPageScreenshot');
			var ctx = canvas.get(0).getContext("2d");
			canvas.attr('width', width);
			canvas.attr('height', height);
			
			var image = new Image();
			image.onload = function() {
				ctx.drawImage(image, 0, 0);
			};
			image.src = imageDataSegments;
			imageDataSegments = "";
    	},
    	
    	filterColor: function(type, element, color, tolerance, dominance) {
        	// image canvas
        	var canvas = jQuery('canvas#abmashCurrentPageScreenshot');
        	var ctx = canvas.get(0).getContext("2d");
        	
        	// element coordinates
        	var offset = jQuery(element).offset();
        	var dimension = jQuery(element).dimension();
        	var left = Math.min(Math.max(offset.left, 0), canvas.attr("width"));
        	var top = Math.min(Math.max(offset.top, 0), canvas.attr("height"));
        	var width = Math.max(Math.min(dimension.width, canvas.attr("width") - left), 0);
        	var height = Math.max(Math.min(dimension.height, canvas.attr("height") - top), 0);

        	// crop image and get its data
        	if(width == 0 || height == 0) return false;
    		var imgdata = ctx.getImageData(left, top, width, height);
    		var img = imgdata.data;
    		
    		var result = false;
    		
    		if(type == 'is') {
    			result = isColor(img, color, tolerance);
    		} else if(type == 'has') {
    			result = hasColor(img, color, tolerance, dominance);
    		} else if(type == 'get') {
    			result = getDominanceOfColor(img, color, tolerance);
    		}

    		return result;
    	}
    	
    });
    
	function isColor(img, color, tolerance) {
		if(img && img.length < 4) return false;
		var data = analyzeImage(img, color);
		return data.normalizedDistance <= tolerance;
	}
    
    function hasColor(img, color, tolerance, dominance) {
		if(img && img.length < 4) return false;
		return getDominanceOfColor(img, color, tolerance) >= dominance;
	}
    
    function getDominanceOfColor(img, color, tolerance) {
		if(img && img.length < 4) return false;
    	var data = analyzeImage(img, color);
    	
//    	alert(data.toSource());
    	
    	var sumMatchingPixels = 0;
		for(var i = 0, n = img.length; i < n-3; i += 4) {
			var pixelColor = {
				red: img[i],
				green: img[i+1],
				blue: img[i+2],
			};
			var distance = colorDistance(pixelColor, color);
			var normalizedDistance = data.maxDistance > 0 ? distance / data.maxDistance : 0;
			if(normalizedDistance <= tolerance) sumMatchingPixels++;
//			alert(pixelColor.toSource());
//			alert(color.toSource());
//			alert(data.maxDistance);
//			alert(distance);
//			alert(normalizedDistance);
//			alert(tolerance);
//			alert(normalizedDistance <= tolerance);
		}
		
//		alert("sumMatchingPixels: " + sumMatchingPixels);
//		alert("data.imageSize: " + data.imageSize);
//		alert(sumMatchingPixels / data.imageSize);
		
		return sumMatchingPixels / data.imageSize;
    }
    
	function analyzeImage(img, color) {
		var minDistance = 9007199254740992;
		var maxDistance = 0.0;
		var totalDistance = 0.0;
		
		var sumR = 0;
		var sumG = 0;
		var sumB = 0;
		
		for(var i = 0, n = img.length; i < n-3; i += 4) {
			var pixelColor = {
				red: img[i],
				green: img[i+1],
				blue: img[i+2],
			};
			
			sumR += pixelColor.red;
			sumG += pixelColor.green;
			sumB += pixelColor.blue;
			
			var distance = colorDistance(pixelColor, color);
			minDistance = Math.min(minDistance, distance);
			maxDistance = Math.max(maxDistance, distance);
			totalDistance += distance;
		}
		
		var imageSize = img.length / 4;
		var averageDistance = totalDistance / imageSize;
		var averageColor = { red: sumR / imageSize, green: sumG / imageSize, blue: sumB / imageSize };
		
		return {
			imageSize: imageSize,
			averageColor: averageColor,
			minDistance: minDistance,
			maxDistance: maxDistance,
			averageDistance: averageDistance,
			normalizedDistance: maxDistance > 0 ? averageDistance / maxDistance : 0,
		};
	}
    
    function colorDistance(c1, c2) {
//    	alert(c1.toSource());
//    	alert(c2.toSource());
		var rmean = (c1.red + c2.red) / 2;
		var r = c1.red - c2.red;
		var g = c1.green - c2.green;
		var b = c1.blue - c2.blue;
		var weightR = 2.0 + rmean / 256;
		var weightG = 4.0;
		var weightB = 2.0 + (255 - rmean) / 256;
		return Math.sqrt(weightR * r * r + weightG * g * g + weightB * b * b);
	}
    	
	
})(window.abmash = window.abmash || {});
