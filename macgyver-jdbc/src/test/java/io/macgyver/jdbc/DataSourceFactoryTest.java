package io.macgyver.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import io.macgyver.core.Kernel;
import io.macgyver.core.factory.ServiceInstanceRegistry;
import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class DataSourceFactoryTest extends MacgyverIntegrationTest {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	Kernel kernel;

	@Autowired
	ServiceInstanceRegistry registry;

	@Test
	public void testApplicationContextIntegrity() {
		Assert.assertNotNull(applicationContext);
		Assert.assertSame(Kernel.getInstance(), kernel);
		Assert.assertSame(Kernel.getInstance(),
				applicationContext.getBean("macgyverKernel"));

	}

	@Test
	public void testAutoRegistration() throws SQLException {

		DataSource ds1 = (DataSource) registry.get("testds");
		DataSource ds2 = (DataSource) registry.get("testds");
		Assert.assertNotNull(ds1);
		Assert.assertSame(ds1,ds2);

	}

}
