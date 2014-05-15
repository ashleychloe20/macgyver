package io.macgyver.core.mapdb;

import io.macgyver.core.CoreIntegrationTestCase;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.TxMaker;
import org.springframework.beans.factory.annotation.Autowired;

public class MapDBTest extends CoreIntegrationTestCase {

	@Autowired(required=true)
	TxMaker txMaker;
	
	
	@Test
	public void test() throws IOException {
		TxMaker tx = DBMaker.newFileDB(File.createTempFile("test", "db")).deleteFilesAfterClose().makeTxMaker();
		DB db = tx.makeTx();
		Map<String,String>m = db.getHashMap("test");
		String val = UUID.randomUUID().toString();
		m.put("a", val);
		db.commit();
		
		db = tx.makeTx();
		m = db.getHashMap("test");
		Assert.assertEquals(val,m.get("a"));
		db.commit();
		
		
	}
}
