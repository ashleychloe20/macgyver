package io.macgyver.xson;

import io.macgyver.xson.Xson;

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

public abstract class AbstractXsonPathTest {


	Object object;
	
	@Before
	public void loadData() throws IOException {
		loadSampleData();
	}
	
	public abstract void loadSampleData() throws IOException;
	

	
	
	@Test
	public void testNoMatch() {
		Assert.assertNull(Xson.eval(object, "$.idx"));
		Assert.assertNull(Xson.eval(object, "$.idx[3]"));
	}
	

	
	@Test
	public void testInt() {
		Assert.assertEquals(100,Xson.eval(object, "$.id"));
	}

	@Test
	public void testString() {
		Assert.assertEquals("donut",Xson.eval(object, "$.type"));
	}

	@Test
	public void testBoolean() {
		Assert.assertEquals(true,Xson.eval(object, "$.available"));
	}

	@Test
	public void testDouble() {
		Assert.assertEquals(new Double(0.55),Xson.eval(object, "$.ppu"));
	}
	
	@Test
	public void testArray() {
		Assert.assertNotNull(object);
	
		Object x = Xson.eval(object, "$.batters.batter");
		Assert.assertNotNull(x);
		
		ArrayNode n = Xson.convert(x, ArrayNode.class);
		Assert.assertTrue(4==n.size());
		
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
