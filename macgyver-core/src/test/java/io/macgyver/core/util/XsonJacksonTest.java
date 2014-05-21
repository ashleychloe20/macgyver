package io.macgyver.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.minidev.json.JSONObject;

import org.bouncycastle.util.Strings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;

public class XsonJacksonTest {


	ObjectNode jacksonObject;
	
	@Before
	public void loadData() throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("json/sample.json");
		
		String s = CharStreams.toString(new InputStreamReader(is));
		 
		 jacksonObject = Xson.convert(s, ObjectNode.class);
	}
	
	
	@Test
	public void testNoMatch() {
		Assert.assertNull(Xson.path(jacksonObject, "$.idx"));
		Assert.assertNull(Xson.path(jacksonObject, "$.idx[3]"));
	}
	

	
	@Test
	public void testInt() {
		Assert.assertEquals(100,Xson.path(jacksonObject, "$.id"));
	}

	@Test
	public void testString() {
		Assert.assertEquals("donut",Xson.path(jacksonObject, "$.type"));
	}

	@Test
	public void testBoolean() {
		Assert.assertEquals(true,Xson.path(jacksonObject, "$.available"));
	}

	@Test
	public void testDouble() {
		Assert.assertEquals(new Double(0.55),Xson.path(jacksonObject, "$.ppu"));
	}
	
	@Test
	public void testArray() {
		ArrayNode arr = Xson.path(jacksonObject, "$.batters.batter");
		Assert.assertEquals("1001",arr.get(0).get("id").asText());
		
		
		ObjectNode x = Xson.path(jacksonObject, "$.batters.batter[2]");
		
		Assert.assertEquals("1003",x.get("id").asText());
		
		
		Assert.assertEquals("1003",Xson.path(jacksonObject,"$.batters.batter[2].id"));
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
