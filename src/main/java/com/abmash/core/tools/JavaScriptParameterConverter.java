package com.abmash.core.tools;

import java.util.Collection;
import java.util.Map;

import com.abmash.REMOVE.core.htmlquery.selector.JQuerySelector;
import com.abmash.api.HtmlElement;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

public class JavaScriptParameterConverter implements Function<Object, Object> {

	public Object apply(Object arg) {
		if(arg instanceof HtmlElement) arg = ((HtmlElement) arg).getSeleniumElement();
		if(arg instanceof JQuerySelector) arg = ((JQuerySelector) arg).getExpressionAsJQueryCommand();
		if (arg instanceof Collection<?>) {
			Collection<?> converted = (Collection<?>) arg;
			return Collections2.transform(converted, this);
		}
		if(arg instanceof Map<?, ?>) {
			Map<Object, Object> converted = Maps.newHashMapWithExpectedSize(((Map<?, ?>) arg).size());
			for (Map.Entry<?, ?> entry: ((Map<?, ?>) arg).entrySet()) {
				Object key = entry.getKey();
				converted.put(key, apply(entry.getValue()));
			}
			arg = converted;
		}
		
		return arg;
	}

}
