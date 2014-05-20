package io.macgyver.jsondb.impl.mapdb;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.mapdb.DB;
import org.mapdb.TxBlock;
import org.mapdb.TxRollbackException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import io.macgyver.jsondb.JsonDbCollection;
import io.macgyver.jsondb.JsonDbCursor;
import io.macgyver.jsondb.JsonDbException;

public class CollectionImpl implements JsonDbCollection {

	ObjectMapper mapper = new ObjectMapper();
	JsonDbImpl db;
	String name;

	public CollectionImpl(JsonDbImpl db, String name) {
		super();
		this.db = db;
		this.name = name;
	}

	JsonDbImpl getJsonDbImpl() {
		return db;
	}

	public String getName() {
		return name;
	}

	
	@Override
	public void remove(final ObjectNode n) {
		
			String id = n.path("_id").asText();
			if (Strings.isNullOrEmpty(id)) {
				id = UUID.randomUUID().toString();
				n.put("_id", id);
			}
			final String key = id;
			TxBlock b = new TxBlock() {

				@Override
				public void tx(DB db) throws TxRollbackException {
			
						Map<String, byte[]> map = db.get(name);
						if (map == null) {
							map = db.createTreeMap(name).make();
						}
						map.remove(key);
						
					
				}
			};
			db.txmaker.execute(b);

		
	
	}
	
	@Override
	public void remove(final String id) {
		
			
			final String key = id;
			TxBlock b = new TxBlock() {

				@Override
				public void tx(DB db) throws TxRollbackException {
			
					try {
						Map<String, byte[]> map = db.get(name);
						if (map == null) {
							map = db.createTreeMap(name).make();
						}
						
						byte [] data = map.get(key);
						
						if (data!=null) {
							ObjectNode obj = (ObjectNode) mapper.readTree(data);
							map.remove(obj.path("_id").asText());
						}
						
					}
					catch (JsonProcessingException e) {
						throw new JsonDbException(e);	
					}
					catch (IOException e) {
						throw new JsonDbException(e);
					}
					
				}
			};
			db.txmaker.execute(b);

		
	
	}
	
	@Override
	public void save(final ObjectNode n) {
		
			String id = n.path("_id").asText();
			if (Strings.isNullOrEmpty(id)) {
				id = UUID.randomUUID().toString();
				n.put("_id", id);
			}
			final String key = id;
			TxBlock b = new TxBlock() {

				@Override
				public void tx(DB db) throws TxRollbackException {
					try {
						Map<String, byte[]> map = db.get(name);
						if (map == null) {
							map = db.createTreeMap(name).make();
						}
				
						byte[] data = mapper.writeValueAsBytes(n);
						map.put(key, data);
					} catch (JsonProcessingException e) {
						throw new JsonDbException(e);
					}
				}
			};
			db.txmaker.execute(b);

		
	
	}

	@Override
	public ObjectNode findOne(final String id) {
		
	
		final AtomicReference<ObjectNode> ref = new AtomicReference<>();
		TxBlock b = new TxBlock() {

			@Override
			public void tx(DB db) throws TxRollbackException {
				try {
					Map<String, byte[]> map = db.get(name);
					if (map == null) {
						map = db.createTreeMap(name).make();
					}
					
					byte [] data = map.get(id);
					
					if (data!=null) {
						ObjectNode obj = (ObjectNode) mapper.readTree(data);
						ref.set(obj);
					}
					
				} catch (JsonProcessingException e) {
					throw new JsonDbException(e);
				}
				catch (IOException e) {
					throw new JsonDbException(e);
				}
			}
		};
		db.txmaker.execute(b);
		ObjectNode n = ref.get();
		
		return n;
	
	}

	@Override
	public JsonDbCursor find() {

		final AtomicReference<List<ObjectNode>> ref = new AtomicReference<>();
		TxBlock b = new TxBlock() {

			@Override
			public void tx(DB db) throws TxRollbackException {
				try {
					Map<String, byte[]> map = db.get(name);
					if (map == null) {
						map = db.createTreeMap(name).make();
					}
					Collection<byte[]> vals = map.values();
					
					List<ObjectNode> list = Lists.newArrayList();
					
					for (byte [] byteData: vals) {
						ObjectNode obj = (ObjectNode) mapper.readTree(byteData);
						list.add(obj);
					}
					ref.set(list);
				
					
				} catch (JsonProcessingException e) {
					throw new JsonDbException(e);
				}
				catch (IOException e) {
					throw new JsonDbException(e);
				}
			}
		};
		db.txmaker.execute(b);
		
		return new CursorImpl(ref.get());
		
	
	}
}
