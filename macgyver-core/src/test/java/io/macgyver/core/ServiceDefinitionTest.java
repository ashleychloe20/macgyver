package io.macgyver.core;

import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceFactory;
import io.macgyver.core.service.ServiceRegistry;

import java.util.Properties;
import java.util.Set;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class ServiceDefinitionTest {

	ServiceFactory<Boolean> testServiceFactory = new ServiceFactory<Boolean>("test") {

		@Override
		protected Boolean doCreateInstance(ServiceDefinition def) {
			// TODO Auto-generated method stub
			return Boolean.TRUE;
		}

		@Override
		protected void doCreateCollaboratorInstances(
				ServiceRegistry registry,
				ServiceDefinition primaryDefinition, Boolean primaryBean) {
		}

		@Override
		public void doCreateCollaboratorDefinitions(
				Set<ServiceDefinition> defSet, ServiceDefinition def) {
			
		}
	};
	
	
	@Test
	public void testCreateCollab() {
		Properties p = new Properties();
		p.put("a", "1");
		ServiceDefinition def = new ServiceDefinition("foo", "foo", p, testServiceFactory);
		
		assertSame(testServiceFactory,def.getServiceFactory());
		
		ServiceDefinition cDef = def.createCollaboratorDefintiion("Bar");
		assertTrue(def.isLazyInit());
		assertEquals("fooBar", cDef.getName());
		assertEquals("foo",cDef.getPrimaryName());
		assertEquals(def.getProperties().getProperty("a"),cDef.getProperties().getProperty("a"));
		assertSame(def.getServiceFactory(),cDef.getServiceFactory());
		
		assertTrue(cDef.isCollaborator());
		assertFalse(def.isCollaborator());
	}
	@Test
	public void testDeepProperties() {
		Properties props = new Properties();
		props.put("a", "1");
		
		
		ServiceDefinition def = new ServiceDefinition("test", "test", props, testServiceFactory);	
		def.getProperties().put("b", "2");
		Assert.assertEquals("1",def.getProperties().getProperty("a"));
		Assert.assertEquals("2", def.getProperties().getProperty("b"));
		Assert.assertFalse(props.containsKey("b"));
		
		props.put("c", "3");
		Assert.assertFalse(def.getProperties().containsKey("c"));
		
	}
}
