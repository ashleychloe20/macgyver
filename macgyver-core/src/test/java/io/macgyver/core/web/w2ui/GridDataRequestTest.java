package io.macgyver.core.web.w2ui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
public class GridDataRequestTest {

	
	@Test
	public void testDefaults() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		GridDataRequest gdr = new GridDataRequest(request);
		
		assertEquals(100,gdr.getLimit());
		assertEquals(0,gdr.getOffset());
		
		List<Sort> sortOrder = gdr.getSortOrder();
		assertTrue(sortOrder.isEmpty());
		
		assertNull(gdr.getCommand());

	}
	
	
	@Test
	public void testSpecifications() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("limit", "50");
		request.setParameter("offset", "20");
		GridDataRequest gdr = new GridDataRequest(request);
		
		assertEquals(50,gdr.getLimit());
		assertEquals(20,gdr.getOffset());
		
		List<Sort> sortOrder = gdr.getSortOrder();
		assertTrue(sortOrder.isEmpty());
		
		assertNull(gdr.getCommand());

	}
}
