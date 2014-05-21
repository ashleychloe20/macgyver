package io.macgyver.xson;

import io.macgyver.xson.Xson;

import javax.json.JsonStructure;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Jsr353ConverterTest extends AbstractConverterTest {

	String jsonText = "{\"name\":\"Jerry\"}";
	String jsonArrayText = "[{\"name\":\"Jerry\"}]";

	@Test
	public void testObject() {
		ObjectNode a = Xson.convert(jsonText, ObjectNode.class);
		JsonNode a2 = Xson.convert(jsonText, JsonNode.class);

		a2 = Xson.convert(a, JsonNode.class);
	}

	@Test
	public void testNull() {
		org.junit.Assert.assertNull(Xson.convert(null, JsonNode.class));
	}

	@Test
	public void testJsonObject() {
		ObjectNode x = Xson.convert(jsonText, ObjectNode.class);

	}

	@Test(expected = ClassCastException.class)
	public void testInvalidTarget() {
		ArrayNode x = Xson.convert(jsonText, ArrayNode.class);

	}

	@Test
	public void testIdentityArray() {
		ArrayNode x = Xson.convert(
				Xson.convert(jsonArrayText, ArrayNode.class), ArrayNode.class);

	}

	@Override
	public Class<? extends Object> getAbstractNodeClass() {
		return JsonStructure.class;
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
