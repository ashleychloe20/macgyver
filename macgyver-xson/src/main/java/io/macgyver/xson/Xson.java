package io.macgyver.xson;

import io.macgyver.xson.impl.GsonConverter;
import io.macgyver.xson.impl.GsonPathProvider;
import io.macgyver.xson.impl.JacksonConverter;
import io.macgyver.xson.impl.JacksonPathProvider;
import io.macgyver.xson.impl.Jsr353Converter;
import io.macgyver.xson.impl.Jsr353PathProvider;

import java.io.IOException;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class Xson {

	public static interface Converter {
		public <X> X convertObject(Object input, Class<X> output);

	}
	public  static interface JsonPathProvider {
		boolean supports(Object val);
		<T> T path(Object element, String path);
		<T> T path(Object element, String path, Object defaultVal);
	}
	public static Xson instance = new Xson();

	private Xson() {
		registerConverters();
	}

	Map<Class<? extends Object>, Converter> converters = Maps
			.newConcurrentMap();
	Map<Class<? extends Object>, JsonPathProvider> pathProviders = Maps
			.newConcurrentMap();

	public static <T> T eval(Object source, String path) {
		
		return lookupPathProvider(source).path(source, path);
	}

	public static <T> T eval(Object source, String path, Object defaultVal) {
		Object val = eval(source, path);
		if (val == null) {
			return (T) defaultVal;
		}
		return (T) val;
	}


	private static JsonPathProvider lookupPathProvider(Object source) {
		JsonPathProvider pp = instance.pathProviders.get(source.getClass());
		
		if (pp == null) {
			for (JsonPathProvider p: instance.pathProviders.values()) {
				if (p.supports(source)) {
					// add the class -> provider mapping
					instance.pathProviders.put(source.getClass(), p);
					return p;
				}
		
			}
			
		}
		
		if (pp==null) {
			throw new IllegalArgumentException("no JsonPathProvider for: "
					+ source.getClass().getName());
		}
		return pp;
	}

	



	@SuppressWarnings("unchecked")
	protected static <T> T read(JsonNode element, String path) {
		try {
			String s = element.toString();

			Object val = JsonPath.read(s, path);

			if (val == null) {
				return (T) val;
			} else if (val instanceof Number) {
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
			throw new IllegalArgumentException("invalid type: "
					+ val.getClass());
		} catch (PathNotFoundException e) {
			return null;
		} catch (JsonProcessingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	protected static <T> T read(JsonElement element, String path) {
		try {
			String s = element.toString();

			Object val = JsonPath.read(s, path);

			if (val == null) {
				return (T) val;
			} else if (val instanceof Number) {
				return (T) val;
			} else if (val instanceof String) {
				return (T) val;
			} else if (val instanceof Boolean) {
				return (T) val;
			} else if (val instanceof JSONObject) {
				return (T) new Gson()
						.fromJson(val.toString(), JsonObject.class);
			} else if (val instanceof JSONArray) {
				return (T) new Gson().fromJson(val.toString(), JsonArray.class);
			}
			throw new IllegalArgumentException("invalid type: "
					+ val.getClass());
		} catch (PathNotFoundException e) {
			return null;
		}
	}

	public static <T> T convert(Object input, Class<T> output) {
		Preconditions.checkNotNull(output);
		Converter c = instance.findConverter(output);
		if (c == null) {
			throw new IllegalArgumentException("target not supported: "
					+ output);
		}
		if (input != null
				&& (output.equals(input.getClass()) || output
						.isAssignableFrom(input.getClass()))) {
			return (T) input;
		}
		return (T) c.convertObject(input, output);

	}

	protected Converter findConverter(Class<? extends Object> target) {
		return converters.get(target);
	}

	protected JsonPathProvider findPathProvider(Class<? extends Object> target) {
		return pathProviders.get(target);
	}

	public void registerConverters() {
		converters.put(JsonElement.class, new GsonConverter());
		converters.put(JsonArray.class, new GsonConverter());
		converters.put(JsonObject.class, new GsonConverter());
		converters.put(ObjectNode.class, new JacksonConverter());
		converters.put(ArrayNode.class, new JacksonConverter());
		converters.put(javax.json.JsonObject.class, new Jsr353Converter());
		converters.put(javax.json.JsonArray.class, new Jsr353Converter());
		converters.put(javax.json.JsonStructure.class, new Jsr353Converter());
		converters.put(JsonNode.class, new JacksonConverter());
		converters.put(BaseJsonNode.class, new JacksonConverter());

		pathProviders.put(ObjectNode.class, new JacksonPathProvider());
		pathProviders.put(ArrayNode.class, new JacksonPathProvider());
		pathProviders.put(JsonArray.class, new GsonPathProvider());
		pathProviders.put(JsonObject.class, new GsonPathProvider());
		pathProviders.put(javax.json.JsonArray.class, new Jsr353PathProvider());
		pathProviders.put(javax.json.JsonObject.class, new Jsr353PathProvider());
	}
}
