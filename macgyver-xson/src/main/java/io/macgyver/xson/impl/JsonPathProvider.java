package io.macgyver.xson.impl;

public  interface JsonPathProvider {
	boolean supports(Object val);

	<T> T path(Object element, String path);

	<T> T path(Object element, String path, Object defaultVal);
}
