package io.macgyver.jdbc;

import io.macgyver.core.ServiceFactory;
import io.macgyver.core.ServiceNotFoundException;

import java.util.Properties;

import javax.json.JsonObject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.jolbox.bonecp.BoneCPDataSource;

public class DataSourceFactory extends ServiceFactory<DataSource> {

	public DataSourceFactory() {
		super("datasources");
	}

	public DataSource create(String name, JsonObject cfg) {

		try {
			Properties p = toProperties(cfg);

			BoneCPDataSource ds = new BoneCPDataSource();
			ds.setDefaultAutoCommit(true);
			ds.setProperties(p);

			return ds;
		} catch (io.macgyver.core.ServiceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceNotFoundException(e.toString());
		}

	}

	public JdbcTemplate jdbcTemplate(String name) {
		return newJdbcTemplate(name);
	}

	public JdbcTemplate newJdbcTemplate(String name) {
		return new JdbcTemplate(get(name));
	}
}
