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

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GsonConverterTest extends AbstractConverterTest {

	@Test
	public void testGson() {
		JsonObject a = Xson.convert(jsonText, JsonObject.class);
		JsonElement a2 = Xson.convert(jsonText, JsonElement.class);

		a2 = Xson.convert(a, JsonElement.class);
	}

	@Test
	public void testNull() {
		org.junit.Assert.assertNull(Xson.convert(null, JsonElement.class));
	}

	@Test
	public void testJsonObject() {
		JsonObject x = Xson.convert(jsonText, JsonObject.class);

	}

	@Test(expected = ClassCastException.class)
	public void testInvalidTarget() {
		Xson.convert(jsonText, JsonArray.class);

	}

	@Test
	public void testIdentityArray() {
		JsonArray x = Xson.convert(
				Xson.convert(jsonArrayText, JsonArray.class), JsonArray.class);

		Assert.assertNotNull(x);
	}

	@Override
	public Class<?> getAbstractNodeClass() {
		return JsonElement.class;
	}

	@Override
	public Class<?> getObjectNodeClass() {
		return JsonObject.class;
	}

	@Override
	public Class<?> getArrayNodeClass() {
		return JsonArray.class;
	}
}
