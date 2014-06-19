package io.macgyver.core.web.navigation;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MenuTest {

	@Test
	public void testMenuJson() {
		Menu m = new Menu();
	
		Assert.assertEquals("root",m.getRootObjectNode().path("id").asText());
		Assert.assertEquals(ArrayNode.class,m.getRootObjectNode().get("items").getClass());
		m.addMenuItem("a", "/x/y/z", "XYZ");
	
		ObjectNode xa = (ObjectNode) m.getRootObjectNode().path("items").get(0);
		Assert.assertEquals("a",xa.get("id").asText());

	}

}
