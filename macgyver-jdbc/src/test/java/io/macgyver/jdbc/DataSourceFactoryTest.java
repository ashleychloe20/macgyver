package io.macgyver.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.macgyver.core.ServiceNotFoundException;
import io.macgyver.test.MacgyverIntegrationTest;

public class DataSourceFactoryTest extends MacgyverIntegrationTest {

	@Autowired
	DataSourceFactory dataSourceFactory;
	
	@Test
	public void testX() {
		Assert.assertNotNull(dataSourceFactory);
		
	
	}
	
	@Test(expected=ServiceNotFoundException.class)
	public void testDataSourceNotFound() {
		dataSourceFactory.get("notfound");
		
	}
	
	@Test()
	public void testDS() throws SQLException {
		DataSource s = dataSourceFactory.get("testds");
		Assert.assertNotNull(s);
		
		Connection c = s.getConnection();
		Assert.assertNotNull(c);
		c.close();
		
	}
}
