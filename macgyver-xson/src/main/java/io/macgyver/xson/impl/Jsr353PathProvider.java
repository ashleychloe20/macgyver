package io.macgyver.xson.impl;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class Jsr353PathProvider implements JsonPathProvider {

	
	@Override
	public <T> T path(Object element, String path) {

		return path(element, path, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T path(Object element, String path, Object defaultVal) {
		try {
		

			Object val = JsonPath.read(element.toString(), path);
			if (val == null) {
				return (T) defaultVal;
			}

			if (val instanceof Number) {
				return (T) val;
			} else if (val instanceof String) {
				return (T) val;
			} else if (val instanceof Boolean) {
				return (T) val;
			} else if (val instanceof JSONObject) {
				return (T) Json.createReader(new StringReader(val.toString())).readObject();
			} else if (val instanceof JSONArray) {
				return (T) Json.createReader(new StringReader(val.toString())).readArray();
			}
			throw new IllegalArgumentException("unsupported type: "
					+ val.getClass());
		} catch (PathNotFoundException e) {
			return (T) defaultVal;
		}
	}
	
	public boolean supports(Object x) {
		if (x instanceof JsonObject || x instanceof JsonArray) {
			return true;
		}
		
		return false;
	}
}
