package io.macgyver.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GsonConverter implements Xson.Converter {

	Gson gson = new Gson();

	@Override
	public <X> X convertObject(Object input, Class<X> output) {

		if (input instanceof String) {
			return gson.fromJson((String) input, output);

		} else if (input instanceof JsonElement) {
			return (X) input;
		} else {
			if (input == null) {
				return gson.fromJson((String) null, output);
			} else {
				return gson.fromJson(input.toString(), output);
			}
		}

	}

}
