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

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractConverterTest {

	String jsonText = "{\"name\":\"Jerry\"}";
	String jsonArrayText = "[{\"name\":\"Jerry\"}]";


	public abstract Class<?> getAbstractNodeClass();


	public abstract Class<?> getObjectNodeClass();


	public abstract Class<?> getArrayNodeClass();

	
	// ******************
	// Abstract node tests
	// ******************
	@Test
	public void testNullToAbstractNode() {
		org.junit.Assert.assertNull(Xson.convert(null, getAbstractNodeClass()));
	}

	@Test
	public void testStringToAbstractNode() {
		Object val = Xson.convert(jsonText, getAbstractNodeClass());
		org.junit.Assert.assertTrue(getAbstractNodeClass().isAssignableFrom(
				val.getClass()));
	}
	// ******************
	// Array node tests
	// ******************
	@Test
	public void testNullToArrayNode() {
		org.junit.Assert.assertNull(Xson.convert(null, getArrayNodeClass()));
	}
	@Test
	public void testStringToArrayNode() {
		Object val = Xson.convert(jsonArrayText, getArrayNodeClass());
		org.junit.Assert.assertTrue(getArrayNodeClass().isAssignableFrom(
				val.getClass()));
	}
	
	

	// ******************
	// Object node tests
	// ******************
	@Test
	public void testStringToObjectNode() {
		Object val = Xson.convert(jsonText, getObjectNodeClass());
		org.junit.Assert.assertTrue(getObjectNodeClass().isAssignableFrom(
				val.getClass()));
	}
	@Test
	public void testNullToObjectNode() {
		org.junit.Assert.assertNull(Xson.convert(null, getObjectNodeClass()));
	}
	@Test
	public void testJsonpToObjectNode() {
		JsonObject source = Json.createObjectBuilder().add("name", "hello").build();
		
		Object val = Xson.convert(source, getObjectNodeClass());
		
		Assert.assertEquals(val.getClass(),getObjectNodeClass());
		
		JsonObject result = Xson.convert(val, JsonObject.class);
		
		Assert.assertEquals(source.getString("name"), result.getString("name"));
		
	}
}
