package io.macgyver.core.util;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Xson {

	public static interface Converter {
		public <X> X convertObject(Object input, Class<X> output);

	}

	public static Xson instance = new Xson();

	Xson() {
		registerConverters();
	}

	Map<Class, Converter> converters = Maps.newConcurrentMap();

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

	public Converter findConverter(Class target) {
		return converters.get(target);
	}

	public void registerConverters() {
		converters.put(JsonElement.class, new GsonConverter());
		converters.put(JsonArray.class, new GsonConverter());
		converters.put(JsonObject.class, new GsonConverter());
		converters.put(ObjectNode.class, new JacksonConverter());
		converters.put(ArrayNode.class, new JacksonConverter());
		converters.put(javax.json.JsonObject.class, new Jsr353Converter());
		converters.put(javax.json.JsonArray.class, new Jsr353Converter());
		converters
				.put(javax.json.JsonStructure.class, new Jsr353Converter());
		converters.put(JsonNode.class, new JacksonConverter());
		converters.put(BaseJsonNode.class, new JacksonConverter());
	}
}
