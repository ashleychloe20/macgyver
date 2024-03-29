package io.macgyver.xson.impl;

import io.macgyver.xson.Xson;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonConverter implements TreeModelConverter {

	@SuppressWarnings("unchecked")
	@Override
	public <X> X convertObject(Object input, Class<X> output) {

		try {

			if (input == null) {
				return null;
			}
			X x = (X) new ObjectMapper().reader().readTree(input.toString());
			return x;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
