package io.macgyver.jsondb.impl.mapdb;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.mapdb.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;



public class JacksonSerializer implements Serializer<ObjectNode>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public void serialize(DataOutput out, ObjectNode value) throws IOException {
		if (value!=null) {
			out.write(mapper.writeValueAsBytes(value));
		}
		;
	}

	@Override
	public ObjectNode deserialize(DataInput in, int available) throws IOException {
		byte [] x = new byte [available];
		in.readFully(x);
		ObjectNode n = mapper.readValue(x,ObjectNode.class);
	
		return n;
	}

	@Override
	public int fixedSize() {
		// TODO Auto-generated method stub
		return -1;
	}

}
