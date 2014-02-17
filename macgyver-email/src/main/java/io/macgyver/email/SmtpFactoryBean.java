package io.macgyver.email;

import io.macgyver.core.CollaboratorRegistrationCallback;
import io.macgyver.core.ServiceFactoryBean;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class SmtpFactoryBean extends ServiceFactoryBean<Session> {

	public SmtpFactoryBean() {
		super(Session.class);
	}

	public Session createObject() {
		String host = getProperties().getProperty("host");
		boolean authEnabled = Boolean.parseBoolean(getProperties().getProperty(
				"auth"));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(host),
				"host must be set");

		Properties props = new Properties();

		props.put("mail.smtp.auth", getProperties()
				.getProperty("auth", "false"));
		props.put("mail.smtp.starttls.enable",
				getProperties().getProperty("starttls.enable", "false"));
		props.put("mail.smtp.host", getProperties().getProperty("host"));
		props.put("mail.smtp.port", getProperties().getProperty("port","25"));

		if (authEnabled) {
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(getProperties()
									.getProperty("username"), getProperties()
									.getProperty("password"));
						}
					});

			return session;
		} else {
			Session session = Session.getInstance(props);
			return session;
		}

	}

	@Override
	public CollaboratorRegistrationCallback getCollaboratorRegistrationCallback() {
		CollaboratorRegistrationCallback cb = new CollaboratorRegistrationCallback() {

			@Override
			public void registerCollaborators(RegistrationDetail detail) {
				String beanName = detail.getPrimaryBeanName()+"Client";
				BeanDefinition b = BeanDefinitionBuilder.rootBeanDefinition(SmtpClient.class).addConstructorArgReference(detail.getPrimaryBeanName()).getBeanDefinition();
				
				detail.getRegistry().registerBeanDefinition(beanName, b);
			}

		};

		return cb;
	}

}
