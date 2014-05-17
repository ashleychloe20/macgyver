package io.macgyver.core.util;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonConverter implements Xson.Converter {

	@Override
	public <X> X convertObject(Object input, Class<X> output) {

		try {
			String val = null;
			if (input == null) {
				return null;
			}
			return (X) new ObjectMapper().reader().readTree(input.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
