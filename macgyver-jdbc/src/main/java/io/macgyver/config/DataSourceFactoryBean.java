package io.macgyver.config;

import io.macgyver.core.ServiceFactoryBean;

import javax.sql.DataSource;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

public class DataSourceFactoryBean extends ServiceFactoryBean<DataSource> {

	public DataSourceFactoryBean() {
		super(DataSource.class);
	
	}

	@Override
	public DataSource createObject() throws Exception {
		BoneCPConfig cp = new BoneCPConfig(getProperties());
		return new BoneCPDataSource(cp);
	}

}
