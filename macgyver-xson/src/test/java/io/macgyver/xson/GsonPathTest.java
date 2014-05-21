package io.macgyver.xson;

import io.macgyver.xson.Xson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.common.io.CharStreams;
import com.google.gson.JsonObject;

public class GsonPathTest extends AbstractXsonPathTest {

	public void loadSampleData()  throws IOException{
		InputStream is = getClass().getClassLoader().getResourceAsStream("json/sample.json");
		
		String s = CharStreams.toString(new InputStreamReader(is));
		 
		 object = Xson.convert(s, JsonObject.class);
	}

}
