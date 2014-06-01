package io.macgyver.jdbc;

import io.macgyver.core.MacGyverException;
import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceFactory;
import io.macgyver.core.service.ServiceRegistry;

import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.base.Throwables;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

public class DataSourceFactory extends ServiceFactory<DataSource> {

	public DataSourceFactory() {
		super("dataSource");
	}

	@Override
	protected DataSource doCreateInstance(ServiceDefinition def) {
		try {
			Properties p = new Properties();
			// set some defaults
			p.put("defaultAutoCommit", Boolean.TRUE.toString());
			p.put("lazyInit", Boolean.TRUE.toString());
			p.putAll(def.getProperties());

			BoneCPConfig cp = new BoneCPConfig(p);

			BoneCPDataSource ds = new BoneCPDataSource(cp);

			
			return ds;
		} catch (Exception e) {
			Throwables.propagateIfPossible(e, MacGyverException.class);
			throw new MacGyverException(e);
		}
	}





	

	@Override
	protected void doCreateCollaboratorInstances(
			ServiceRegistry registry,
			ServiceDefinition primaryDefinition, DataSource primaryBean) {
		JdbcTemplate t = new JdbcTemplate(primaryBean, true);
		registry.registerCollaborator(primaryDefinition.getPrimaryName()+"Template", t);
	}

	@Override
	public void doCreateCollaboratorDefinitions(Set<ServiceDefinition> defSet,
			ServiceDefinition primary) {
		ServiceDefinition def = new ServiceDefinition(primary.getName()+"Template", primary.getName(), primary.getProperties(), this);
		
		defSet.add(def);
		
	}

	
}
