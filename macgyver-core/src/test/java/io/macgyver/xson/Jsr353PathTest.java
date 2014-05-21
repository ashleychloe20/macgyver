package io.macgyver.xson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.json.JsonObject;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.CharStreams;

public class Jsr353PathTest extends AbstractXsonPathTest {

	public void loadSampleData()  throws IOException{
		InputStream is = getClass().getClassLoader().getResourceAsStream("json/sample.json");
		
		String s = CharStreams.toString(new InputStreamReader(is));
		 
		 object = Xson.convert(s, JsonObject.class);
	}

}
