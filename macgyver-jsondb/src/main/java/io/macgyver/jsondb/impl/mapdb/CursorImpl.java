package io.macgyver.jsondb.impl.mapdb;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.macgyver.jsondb.JsonDbCursor;

public class CursorImpl implements JsonDbCursor {


	List<ObjectNode> list;
	
	public CursorImpl(List<ObjectNode> list) {
		this.list = list;
	}
	@Override
	public Iterator<ObjectNode> iterator() {
		return list.iterator();
	}

	public int size() {
		return list.size();
	}
}
