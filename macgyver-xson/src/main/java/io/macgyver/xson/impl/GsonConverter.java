package io.macgyver.xson.impl;

import io.macgyver.xson.Xson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class GsonConverter implements TreeModelConverter {

	Gson gson = new Gson();

	@SuppressWarnings("unchecked")
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
