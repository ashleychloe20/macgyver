package io.macgyver.xson.impl;

import java.io.IOException;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class JacksonPathProvider implements JsonPathProvider {

	@Override
	public <T> T path(Object element, String path) {

		return path(element, path, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T path(Object element, String path, Object defaultVal) {
		try {
			JsonNode node = (JsonNode) element;
			String valToEval = element.toString();
		
			System.out.println("EVAL: "+valToEval);
			Object val = JsonPath.read(valToEval, path);
		
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
				return (T) new ObjectMapper().readTree(val.toString());
			} else if (val instanceof JSONArray) {
				return (T) new ObjectMapper().readTree(val.toString());
			}
			throw new IllegalArgumentException("unsupported type: "
					+ val.getClass());
		} catch (PathNotFoundException e) {
			return (T) defaultVal;
		} catch (JsonProcessingException e) {
			return (T) defaultVal;
		} catch (IOException e) {
			return (T) defaultVal;
		} catch (RuntimeException e) {
			return (T) defaultVal;
		}
	}
	public boolean supports(Object x) {
		return false;
	}
}
