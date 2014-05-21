package io.macgyver.xson;

import io.macgyver.xson.Xson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Assert;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.CharStreams;

public class JacksonPathTest extends AbstractXsonPathTest {

	public void loadSampleData() throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream(
				"json/sample.json");
		Assert.assertNotNull("must be able to load sample data", is);
		String s = CharStreams.toString(new InputStreamReader(is));

		object = Xson.convert(s, ObjectNode.class);
	}

}
