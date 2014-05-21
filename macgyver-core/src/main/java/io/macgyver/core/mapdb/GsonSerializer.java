package io.macgyver.core.mapdb;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.mapdb.Serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GsonSerializer implements Serializer<JsonObject>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(DataOutput out, JsonObject value) throws IOException {
		if (value!=null) {
			out.writeUTF(value.toString());
		}
		;
	}

	@Override
	public JsonObject deserialize(DataInput in, int available) throws IOException {
		String x = in.readUTF();
		
		JsonObject n = new Gson().fromJson(x, JsonObject.class);
		return n;
	}

	@Override
	public int fixedSize() {
		// TODO Auto-generated method stub
		return -1;
	}

}
