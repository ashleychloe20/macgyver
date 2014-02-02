package io.macgyver.core;

import io.macgyver.core.crypto.Crypto;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import groovy.util.ConfigObject;

import com.google.common.base.Optional;

public abstract class ConfigStore {

	@Autowired
	Crypto crypto;

	Logger logger = LoggerFactory.getLogger(ConfigStore.class);


	protected abstract Optional<ConfigObject> getRootConfigObject();



	public JsonObject getRootConfig() {
		JsonObjectBuilder b = Json.createObjectBuilder();
		Optional<ConfigObject> co = getRootConfigObject();
	
		convertConfigObjectTreeToJsonObject(co.get(), b);
		return b.build();

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
			}
			else if (valObj instanceof Integer) {
				logger.warn("xxx");
				builder.add(entry.getKey().toString(), ((Integer) valObj).intValue());
			} 
			else if (valObj instanceof Long) {
				logger.warn("xxx");
				builder.add(entry.getKey().toString(), ((Long) valObj).longValue());
			} 
			else if (valObj instanceof Boolean) {
				builder.add(entry.getKey().toString(), ((Boolean) valObj).booleanValue());
			}
			else {
				logger.warn("unhandled type: " + valObj.getClass());
			}
		}
	}
}
