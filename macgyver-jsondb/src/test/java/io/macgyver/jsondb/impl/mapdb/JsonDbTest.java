package io.macgyver.jsondb.impl.mapdb;

import io.macgyver.jsondb.JsonDb;
import io.macgyver.jsondb.JsonDbCallback;
import io.macgyver.jsondb.JsonDbCursor;
import io.macgyver.jsondb.JsonDbTemplate;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mapdb.DBMaker;
import org.mapdb.TxMaker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;

public class JsonDbTest {

	static JsonDbImpl dbi = null;

	static JsonDbTemplate template;
	
	@BeforeClass
	public static void setup() {
		TxMaker txm = DBMaker.newMemoryDB().makeTxMaker();
		dbi = new JsonDbImpl(txm);
		
		template = new JsonDbTemplate(dbi);
	}


	@Test
	public void testInsert() {
		ObjectMapper m = new ObjectMapper();
		
		for (int i=0; i<1000; i++) {
			ObjectNode n = m.createObjectNode();
			n.put("name", "My Name_"+i);

			template.save("mycollection", n);
		}
		
		JsonDbCallback<Integer> cb = new JsonDbCallback<Integer>() {

			@Override
			public Integer execute(JsonDb db) {
				JsonDbCursor cursor= db.getCollection("mycollection").find();
				
				return cursor.size();
			}
		};
		Assert.assertEquals(new Integer(1000),template.execute(cb));
		
	}
	@Test
	public void testTemplate() {
		
		ObjectMapper m = new ObjectMapper();
		ObjectNode n = m.createObjectNode();
		n.put("name", "jerry");
		
		template.save("test", n);
		

		
		n.put("foo", "bar");
		
		template.save("test",n);
		
		
		final Optional<ObjectNode> other = template.findOneById("test", n.get("_id").asText());
	
		Assert.assertNotNull(other);
		
		Assert.assertEquals(other.get().get("_id"),n.get("_id"));
		

		
		JsonDbCallback<Object> cb = new JsonDbCallback<Object>() {

			@Override
			public Object execute(JsonDb db) {
				
				db.getCollection("test").remove(other.get().path("_id").asText());
				return Boolean.TRUE;
			}
		};
		template.execute(cb);
		
		Assert.assertFalse(template.findOneById("test", other.get().get("_id").asText()).isPresent());
	}
	
}
