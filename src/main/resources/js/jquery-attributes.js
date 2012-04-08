jQuery.fn.extend({
	getAttributeNames: function (options) {
		var options = jQuery.extend({
		}, options);
		
		var node = this;
		
		var el = node.get(0); 
		var arr = [];
		for (var i=0, attrs=el.attributes, l=attrs.length; i<l; i++) {
			arr.push(attrs.item(i).nodeName);
		};
		
		return arr;
	},
	
	getAttributes: function (options) {
		var options = jQuery.extend({
		}, options);
		
		var node = this;
		
		var el = node.get(0); 
		var arr = {};
		for (var i=0, attrs=el.attributes, l=attrs.length; i<l; i++) {
			arr[attrs.item(i).nodeName] = attrs.item(i).nodeValue;
		};
		
		return arr;
	},
});
