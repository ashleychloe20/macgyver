package io.macgyver.core.mapdb;

import io.macgyver.test.MacGyverIntegrationTest;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.TxMaker;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MapDBTest {

	static TxMaker memoryTx = null;

	@BeforeClass
	public static void setup() {
		memoryTx = DBMaker.newMemoryDB().makeTxMaker();
		Assert.assertTrue(memoryTx.makeTx().getCatalog().isEmpty());
	}

	@AfterClass
	public static void after() {
		memoryTx.close();
	}

	@Test
	public void testX() {
		DB db = memoryTx.makeTx();
		String name = UUID.randomUUID().toString();
		
		
		JsonObject n = new JsonObject();
		n.addProperty("name", "jerry");
		Map<String, JsonElement> x = db.createTreeMap(name).valueSerializer(new GsonSerializer()).make();
		x.put("a", n);
		db.commit();
		
		db = memoryTx.makeTx();

		Map<String,String> x1 = db.get(name);
		Object val = x1.get("a");
		
	
		Assert.assertEquals(n, val);
		

		
	}

	@Test
	public void test() throws IOException {

		TxMaker tx = memoryTx;
		DB db = tx.makeTx();
		Map<String, String> m = db.getHashMap("test");
		String val = UUID.randomUUID().toString();
		m.put("a", val);
		db.commit();

		db = tx.makeTx();
		m = db.getHashMap("test");
		Assert.assertEquals(val, m.get("a"));
		db.commit();

		db = tx.makeTx();
		m = db.get(UUID.randomUUID().toString());
		Assert.assertNull(m);
		db.commit();

		db = tx.makeTx();
		String name = UUID.randomUUID().toString();
		m = db.getTreeMap(name);
		Assert.assertNotNull(m);

		db.commit();
	}
}
