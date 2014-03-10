package io.macgyver.jdbc;

import io.macgyver.core.Kernel;
import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.test.MacgyverIntegrationTest;

import java.sql.SQLException;

import javax.sql.DataSource;

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
	ServiceRegistry registry;

	@Test
	public void testApplicationContextIntegrity() {
		Assert.assertNotNull(applicationContext);
		Assert.assertSame(Kernel.getInstance(), kernel);
		Assert.assertSame(Kernel.getInstance(),
				applicationContext.getBean("macgyverKernel"));

	}

	@Test
	public void testAutoRegistration() throws SQLException {


		JdbcTemplate t = (JdbcTemplate) registry.get("testdsTemplate");
		Assert.assertNotNull(t);
		Assert.assertSame(t,registry.get("testdsTemplate"));
		
		DataSource ds = (DataSource) registry.get("testds");
		Assert.assertNotNull(ds);
		
			DataSource ds1 = (DataSource) registry.get("testds");
			DataSource ds2 = (DataSource) registry.get("testds");
			Assert.assertNotNull(ds1);
			Assert.assertSame(ds1,ds2);
			Assert.assertSame(ds, t.getDataSource());
	}

}
