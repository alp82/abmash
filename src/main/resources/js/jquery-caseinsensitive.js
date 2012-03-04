jQuery.expr[':'].equallyCaseInsensitive = function(node, stackIndex, properties) { 
  return jQuery.trim(jQuery(node).text().toLowerCase()) ===  jQuery.trim(properties[3].toLowerCase());
};

jQuery.expr[':'].containsCaseInsensitive = function(node, stackIndex, properties) { 
  return (node.textContent || node.innerText || "").toLowerCase().indexOf((properties[3] || "").toLowerCase()) >= 0; 
};

jQuery.expr[':'].attrCaseInsensitive = function(node, stackIndex, properties) {
    var args = properties[3].split(',').map(function(arg) {
        return arg.replace(/^\\s*[\"']|[\"']\\s*$/g, '');
    });

    var compareType = jQuery.trim(args[0]);
    var attributeName = jQuery.trim(args[1]).toLowerCase();
    var attrValueReference = jQuery.trim(args[2]).toLowerCase();

    var returnValue = false;
    jQuery.each(jQuery(jQuery(node).get(0).attributes), function(index) {
        var attr = jQuery(node).get(0).attributes[index];
        if (attributeName == "*" || attributeName == attr.name.toLowerCase()) {
            if (compareStrings(attr.value.toLowerCase(), attrValueReference, compareType)) {
                returnValue = true;
            }
        }
    });

    return returnValue;
};

compareStrings = function(str1, str2, type) {
    switch (type) {
    case "EXISTS":
        return str1.length > 0;
    case "EXACT":
        return str1 == str2;
    case "WORD":
        //  /\bSTR2\b/gi
        return str1 == str2;
    case "BEGINSWITH":
        return str1 == str2;
    case "ENDSWITH":
        return str1 == str2;
    case "CONTAINS":
        return str1.contains(str2);
    default:
        return false;
    }
};
