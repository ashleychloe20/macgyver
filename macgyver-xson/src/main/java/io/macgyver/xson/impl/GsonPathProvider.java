package io.macgyver.xson.impl;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class GsonPathProvider implements JsonPathProvider {

	Gson gson = new Gson();

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
				return (T) gson.fromJson(val.toString(), JsonObject.class);
			} else if (val.getClass().equals(JSONArray.class)) {
				
				return (T) gson.fromJson(val.toString(), JsonArray.class);
			}
			throw new IllegalArgumentException("unsupported type: "
					+ val.getClass());
		} catch (PathNotFoundException e) {
			return (T) defaultVal;
		}
		catch (RuntimeException e) {
			return (T) defaultVal;
		}
	}
	public boolean supports(Object x) {
		return false;
	}
}
