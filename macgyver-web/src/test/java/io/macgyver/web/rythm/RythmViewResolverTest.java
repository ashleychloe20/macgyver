package io.macgyver.web.rythm;

import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class RythmViewResolverTest extends MacgyverIntegrationTest {


	@Test
	public void testX() throws Exception {
		RythmViewResolver r = new RythmViewResolver();
		Assert.assertNotNull(r);
		
		AbstractUrlBasedView v = r.buildView("templateFromFile.ryhtm");
		Assert.assertEquals("templateFromFile.ryhtm",v.getUrl());
	}
	
	
	@Test
	public void testViewNotFound() throws Exception {
		RythmViewResolver r = new RythmViewResolver();
		Assert.assertNotNull(r);
		
		AbstractUrlBasedView v = r.buildView("someViewThatIsNotFound.ryhtm");
		Assert.assertEquals("someViewThatIsNotFound.ryhtm",v.getUrl());
	}
}
