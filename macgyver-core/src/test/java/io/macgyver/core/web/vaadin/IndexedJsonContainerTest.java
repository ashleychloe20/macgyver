package io.macgyver.core.web.vaadin;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class IndexedJsonContainerTest {

	ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void testExtractProperty() {
		IndexedJsonContainer c = new IndexedJsonContainer();
		ObjectNode n = mapper.createObjectNode();
		n.put("name", "Jerry");
		Assert.assertEquals("Jerry",c.extractPropertyValue(n, "name"));
		
		
		c.addJsonPathPropertyExtractor("firstName", "$.name");
		
		Assert.assertEquals("Jerry",c.extractPropertyValue(n, "firstName"));
	}
}
