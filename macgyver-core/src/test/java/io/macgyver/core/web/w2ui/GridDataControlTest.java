package io.macgyver.core.web.w2ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.util.HttpServletRequestDelegate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class GridDataControlTest {

	@Test
	public void testIt() {

		MockHttpServletRequest r = new MockHttpServletRequest();

		GridDataControl c = new GridDataControl(r);

		ObjectNode n = c.process();

		assertEquals("success", n.path("status").asText());
		assertEquals(0, n.path("total").asInt());
		assertTrue(n.path("records").isArray());
		assertTrue(n.withArray("records").size() == 0);
	}

	GridDataControl createTestControl(HttpServletRequest r) {
	

		GridDataControl c = new GridDataControl(r);

		ObjectNode n0 = new ObjectMapper().createObjectNode();
		ObjectNode n1 = new ObjectMapper().createObjectNode();
		ObjectNode n2 = new ObjectMapper().createObjectNode();

		n0.put("name", "Jerry");
		c.addRow(n0);
		c.addRow(n1);
		n1.put("name", "Bob");
		c.addRow(n2);
		n2.put("name", "Phil");
		return c;
	}

	
	@Test
	public void testFilterDefaultCaseSensitivity() {

		MockHttpServletRequest r = new MockHttpServletRequest();
		r.setParameter("search[0][value]","JER");
		GridDataControl c = createTestControl(r);
	
		
		ObjectNode n = c.process();
		Assert.assertEquals(1,n.path("total").asInt());	
		Assert.assertEquals("Jerry",n.withArray("records").get(0).get("name").asText());
	}
	
	@Test
	public void testDefaultSort() {

		GridDataControl c = createTestControl(new MockHttpServletRequest());
		c.setDefaultSort("name");
		
		ObjectNode n = c.process();

		assertEquals("success", n.path("status").asText());
		assertEquals(3, n.path("total").asInt());
		assertTrue(n.path("records").isArray());
		assertTrue(n.withArray("records").size() == 3);


		assertEquals("Bob", n.withArray("records").get(0).get("name").asText());
		assertEquals("Jerry", n.withArray("records").get(1).get("name").asText());
		assertEquals("Phil", n.withArray("records").get(2).get("name").asText());
	}
	
	
	@Test
	public void testValues() {

		GridDataControl c = createTestControl(new MockHttpServletRequest());
		ObjectNode n = c.process();

		assertEquals("success", n.path("status").asText());
		assertEquals(3, n.path("total").asInt());
		assertTrue(n.path("records").isArray());
		assertTrue(n.withArray("records").size() == 3);

		ArrayNode recs = n.withArray("records");
		for (int i = 0; i < recs.size(); i++) {
			assertEquals(i + 1, recs.get(i).get("recid").asInt());
		}

		assertEquals("Jerry", recs.get(0).get("name").asText());
	}
}
