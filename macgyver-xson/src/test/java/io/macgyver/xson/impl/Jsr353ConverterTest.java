/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.xson.impl;

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
