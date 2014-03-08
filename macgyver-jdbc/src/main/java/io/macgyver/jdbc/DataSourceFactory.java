package io.macgyver.jdbc;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.serializer.ExceptionSerializer;
import com.google.common.base.Throwables;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

import io.macgyver.core.CollaboratorRegistrationCallback;
import io.macgyver.core.CollaboratorRegistrationCallback.RegistrationDetail;
import io.macgyver.core.MacGyverException;
import io.macgyver.core.factory.ServiceFactory;

public class DataSourceFactory extends ServiceFactory<DataSource> {

	public DataSourceFactory() {
		super("dataSource");
	}
	@Override
	protected DataSource createObjecct(Properties props) {
		try {
		Properties p = new Properties();
		// set some defaults
		p.put("defaultAutoCommit", "true");

		props.putAll(props);

		BoneCPConfig cp = new BoneCPConfig(p);

		return new BoneCPDataSource(cp);
		}
		catch (Exception e) {
			throw new MacGyverException(e);
		}
	}

	/*
	@Override
	public CollaboratorRegistrationCallback getCollaboratorRegistrationCallback() {
		CollaboratorRegistrationCallback cb = new CollaboratorRegistrationCallback() {

			@Override
			public void registerCollaborators(RegistrationDetail detail) {

				String name = detail.getPrimaryBeanName() + "Template";
				BeanDefinition bd = BeanDefinitionBuilder
						.rootBeanDefinition(JdbcTemplate.class)
						.addConstructorArgReference(detail.getPrimaryBeanName())
						.getBeanDefinition();
				detail.getRegistry().registerBeanDefinition(name, bd);

			}
		};

		return cb;
	}
	*/
}
