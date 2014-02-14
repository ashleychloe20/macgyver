package io.macgyver.config;

import io.macgyver.core.CollaboratorRegistrationCallback;
import io.macgyver.core.ServiceFactoryBean;

import javax.sql.DataSource;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.base.Optional;
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

	@Override
	public Optional<CollaboratorRegistrationCallback> getCollaboratorRegistrationCallback() {
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

		return Optional.of(cb);
	}

}
