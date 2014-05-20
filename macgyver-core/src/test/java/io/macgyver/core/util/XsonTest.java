package io.macgyver.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;

public class XsonTest {

	JsonObject obj;
	
	@Before
	public void loadData() throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("json/sample.json");
		String x = CharStreams.toString(new InputStreamReader(is));
		 obj = new Gson().fromJson(x, JsonObject.class);
	}
	
	
	@Test
	public void testNoMatch() {
		Assert.assertNull(Xson.path(obj, "$.idx"));
		Assert.assertNull(Xson.path(obj, "$.idx[3]"));
	}
	
	@Test
	public void testInt() {
		Assert.assertEquals(100,Xson.path(obj, "$.id"));
	}
	
	@Test
	public void testString() {
		Assert.assertEquals("donut",Xson.path(obj, "$.type"));
	}
	@Test
	public void testBoolean() {
		Assert.assertEquals(true,Xson.path(obj, "$.available"));
	}
	@Test
	public void testDouble() {
		Assert.assertEquals(new Double(0.55),Xson.path(obj, "$.ppu"));
	}
	
	@Test
	public void testArray() {
		JsonArray arr = Xson.path(obj, "$.batters.batter");
		Assert.assertEquals("1001",arr.get(0).getAsJsonObject().get("id").getAsString());
		
		
		JsonObject x = Xson.path(obj, "$.batters.batter[2]");
		
		Assert.assertEquals("1003",x.get("id").getAsString());
		
		
		Assert.assertEquals("1003",Xson.path(obj,"$.batters.batter[2].id"));
	}
	/*
{
    "id": 100,
    "type": "donut",
    "name": "Cake",
    "available": true,
    "ppu": 0.55,
    "batters":
    {
        "batter":
                [
                    {
                        "id": "1001",
                        "type": "Regular"
                    },
                    {
                        "id": "1002",
                        "type": "Chocolate"
                    },
                    {
                        "id": "1003",
                        "type": "Blueberry"
                    },
                    {
                        "id": "1004",
                        "type": "Devil's Food"
                    }
                ]
    },
    "toppings": ["Glazed", "Sugar", "Chocolate", "Maple"]
}
	 */
}
