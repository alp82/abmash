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
