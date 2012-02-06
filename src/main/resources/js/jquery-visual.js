jQuery.expr[':'].above = function(node, stackIndex, properties) {
    return inDirection(node, "above", properties[3]);
};

jQuery.expr[':'].below = function(node, stackIndex, properties) {
    return inDirection(node, "below", properties[3]);
};

jQuery.expr[':'].leftTo = function(node, stackIndex, properties) {
    return inDirection(node, "leftTo", properties[3]);
};

jQuery.expr[':'].rightTo = function(node, stackIndex, properties) {
    return inDirection(node, "rightTo", properties[3]);
};

inDirection = function(node, direction, selector) {
    var topSource = jQuery(selector).offset().top;
    var leftSource = jQuery(selector).offset().left;
    var bottomSource = topSource + jQuery(selector).height();
    var rightSource = leftSource + jQuery(selector).width();

    var topTarget = jQuery(node).offset().top;
    var leftTarget = jQuery(node).offset().left;
    var bottomTarget = topTarget + jQuery(node).height();
    var rightTarget = leftTarget + jQuery(node).width();

    if(direction == "above") {
        return bottomSource > bottomTarget && leftSource <= rightTarget && rightSource >= leftTarget;
    }

    if(direction == "below") {
        return topSource < topTarget && leftSource <= rightTarget && rightSource >= leftTarget;
    }

    if(direction == "leftTo") {
        return rightSource > rightTarget && topSource <= bottomTarget && bottomSource >= topTarget;
    }

    if(direction == "rightTo") {
        return leftSource < leftTarget && topSource <= bottomTarget && bottomSource >= topTarget;
    }

    return false;
};
