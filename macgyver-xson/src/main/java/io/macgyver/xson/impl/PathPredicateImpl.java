package io.macgyver.xson.impl;

import io.macgyver.xson.JsonPathPredicate;
import io.macgyver.xson.Xson;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Predicate;

public class PathPredicateImpl implements JsonPathPredicate<Object> {

	String path;

	public PathPredicateImpl(String jsonPath) {
		this.path = jsonPath;
	}

	@Override
	public boolean apply(Object input) {
		Object val = Xson.eval(input, path, null);
		if (val == null) {
			return false;
		}
		if (val instanceof JsonNode) {
			if (((JsonNode) val).isArray()) {
				return ((JsonNode) val).size() > 0;
			} else {
				return true;
			}
		}
		return false;

	}

}
