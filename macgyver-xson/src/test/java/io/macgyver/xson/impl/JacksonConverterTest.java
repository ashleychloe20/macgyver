package io.macgyver.xson.impl;

import io.macgyver.xson.Xson;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonConverterTest extends AbstractConverterTest {

	@Test
	public void testObject() {
		JsonObject a = Xson.convert(jsonText, JsonObject.class);
		JsonStructure a2 = Xson.convert(jsonText, JsonStructure.class);

		a2 = Xson.convert(a, JsonStructure.class);
	}

	@Test
	public void testNull() {
		org.junit.Assert.assertNull(Xson.convert(null, JsonStructure.class));
	}

	@Test
	public void testJsonObject() {
		Xson.convert(jsonText, JsonObject.class);

	}



	@Test
	public void testIdentityArray() {
		JsonArray x = Xson.convert(
				Xson.convert(jsonArrayText, JsonArray.class), JsonArray.class);
		Assert.assertNotNull(x);
	}

	@Override
	public Class<? extends Object> getAbstractNodeClass() {
		return JsonNode.class;
	}

	@Override
	public Class<? extends Object> getObjectNodeClass() {
		return ObjectNode.class;
	}

	@Override
	public Class<? extends Object> getArrayNodeClass() {
		return ArrayNode.class;
	}
}
