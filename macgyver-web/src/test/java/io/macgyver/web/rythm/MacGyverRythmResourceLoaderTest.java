package io.macgyver.web.rythm;

import org.junit.Assert;
import org.junit.Test;
import org.rythmengine.RythmEngine;
import org.rythmengine.resource.ITemplateResource;
import org.rythmengine.template.ITemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import io.macgyver.test.MacgyverIntegrationTest;

public class MacGyverRythmResourceLoaderTest extends MacgyverIntegrationTest {


	@Autowired
	RythmEngine rythmEngine;
	

	
	@Test
	public void testLoader() {
		MacGyverRythmResourceLoader loader = new MacGyverRythmResourceLoader();
		
		String root = loader.getResourceLoaderRoot();
		Assert.assertEquals("/", root);
		
		Assert.assertNull(loader.load("notfound.rythm"));
		
		Assert.assertNotNull(loader.load("templateFromClasspath.rythm"));
		Assert.assertNotNull(loader.load("subdir/templateFromClasspathSubdir.rythm"));
		
		Assert.assertNotNull(loader.load("templateFromFile.rythm"));
		Assert.assertNotNull(loader.load("subdir/templateFromFileSubdir.rythm"));
		
		ITemplateResource r = loader.load("subdir/templateFromFileSubdir.rythm");
		Assert.assertTrue(r.asTemplateContent().contains("from sub dir"));
		
	}
	
	@Test
	public void testPrecedence() {
		MacGyverRythmResourceLoader loader = new MacGyverRythmResourceLoader();
		ITemplateResource r = loader.load("precedenceTest.rythm");
		
		Assert.assertTrue(r.asTemplateContent().contains("from file"));
	}
	
	
}
