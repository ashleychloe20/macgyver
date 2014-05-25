package io.macgyver.core.titan;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tinkerpop.blueprints.Vertex;

public class SimpleVertexSerializer extends JsonSerializer<Vertex> {

	@Override
	public void serialize(Vertex value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		jgen.writeStartObject();
		
		jgen.writeObjectFieldStart("props");
		for (String key: value.getPropertyKeys()) {
			jgen.writeObjectField(key, value.getProperty(key));
		}
		jgen.writeEndObject();
		jgen.writeEndObject();

	}

	public void add(ObjectMapper m) {
		SimpleModule module = new SimpleModule();
		module.addSerializer(Vertex.class, this);
		m.registerModule(module);
	}
}
