package io.macgyver.xson;

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
