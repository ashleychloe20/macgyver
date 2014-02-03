package io.macgyver.core;

import io.macgyver.core.crypto.Crypto;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonWriter;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import groovy.util.ConfigObject;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

public abstract class ConfigStore {

	@Autowired
	Crypto crypto;

	Logger logger = LoggerFactory.getLogger(ConfigStore.class);

	protected abstract Optional<ConfigObject> getRootConfigObject();

	public JsonObject getRootConfig() {
		JsonObjectBuilder b = Json.createObjectBuilder();
		Optional<ConfigObject> co = getRootConfigObject();

		convertConfigObjectTreeToJsonObject(co.get(), b);
		JsonObject encryptedJson = b.build();

		return decrypt(encryptedJson);


	}

	public void convertConfigObjectTreeToJsonObject(ConfigObject source,
			JsonObjectBuilder builder) {
		for (Object x : source.entrySet()) {

			Map.Entry entry = (Map.Entry) x;
			Object valObj = entry.getValue();

			if (valObj instanceof ConfigObject) {
				JsonObjectBuilder childBuilder = Json.createObjectBuilder();

				convertConfigObjectTreeToJsonObject((ConfigObject) valObj,
						childBuilder);
				builder.add(entry.getKey().toString(), childBuilder.build());

			} else if (valObj instanceof String) {
				builder.add(entry.getKey().toString(), valObj.toString());
			} else if (valObj instanceof Integer) {
				builder.add(entry.getKey().toString(),
						((Integer) valObj).intValue());
			} else if (valObj instanceof Long) {
				builder.add(entry.getKey().toString(),
						((Long) valObj).longValue());
			} else if (valObj instanceof Boolean) {
				builder.add(entry.getKey().toString(),
						((Boolean) valObj).booleanValue());
			} else {
				logger.warn("unhandled type: " + valObj.getClass());
			}
		}
	}

	private void decrypt(com.google.gson.JsonObject x) {
		Gson g = new Gson();
		for (Entry<String, JsonElement> t : x.entrySet()) {
			JsonElement val = t.getValue();

			if (val == null) {

			} else if (val instanceof JsonPrimitive) {

				JsonPrimitive jx = (JsonPrimitive) val;
				if (jx.isString()) {
					String input = jx.getAsString();
					String output = crypto.decryptStringWithPassThrough(input);
					JsonPrimitive jxxx = new JsonPrimitive(output);
					
					t.setValue(jxxx);

				}
			} else if (val instanceof com.google.gson.JsonObject) {
				decrypt((com.google.gson.JsonObject) val);
			}

			// won't recurse into arrays

		}

	}

	private JsonObject decrypt(JsonObject source) {
		StringWriter sw = new StringWriter();
		JsonWriter jw = Json.createWriter(sw);
		jw.writeObject(source);
		jw.close();

		Gson g = new Gson();
		com.google.gson.JsonObject jo = g.fromJson(sw.toString(),
				com.google.gson.JsonObject.class);
		decrypt(jo);

		javax.json.JsonReader p = Json.createReader(new StringReader(g
				.toJson(jo)));
		return p.readObject();

	}
}
