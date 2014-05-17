package io.macgyver.core;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class CheckBeanNamingConvention extends CoreIntegrationTestCase {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	org.springframework.context.ApplicationContext ctx;

	@Test
	public void testNaming() {
		Map<String, Object> beans = ctx.getBeansOfType(Object.class);

		for (Map.Entry<String, Object> entry : beans.entrySet()) {

			@SuppressWarnings("rawtypes")
			Class c = entry.getValue().getClass();
			
			if (c.getPackage().getName().startsWith("io.macgyver")) {
				check(entry.getKey(), c);
			
			}

		}
	}
	
	@SuppressWarnings("rawtypes")
	public void check(String name, Class clazz) {
		if (name.contains("Config") || name.equals("testGroovyBean")) {
			
		}
		else if (!name.startsWith("mac")) {
			Assert.fail("bean naming violation: "+name+" for "+clazz);
		}
		else if (name.toLowerCase().startsWith("macgyver")) {
			Assert.fail("bean naming violation: "+name+" for "+clazz);
		}
	}
}
