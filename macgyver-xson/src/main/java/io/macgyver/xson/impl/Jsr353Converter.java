package io.macgyver.xson.impl;

import io.macgyver.xson.Xson;

import java.io.StringReader;

import javax.json.Json;

public class Jsr353Converter implements TreeModelConverter {

	@Override
	public <X> X convertObject(Object input, Class<X> output) {

		if (input == null) {
			return null;
		}
		return (X) Json.createReader(new StringReader(input.toString())).read();

	}
}
