package io.macgyver.config;

import io.macgyver.core.ServiceFactoryBean;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class SmtpSessionFactoryBean  extends ServiceFactoryBean<Session>{

	public SmtpSessionFactoryBean() {
		super(Session.class);
	}


	public Session createObject() {
		String host = getProperties().getProperty("host");
		boolean authEnabled = Boolean.parseBoolean(getProperties().getProperty("auth"));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(host),"host must be set");
		
		Properties props = new Properties();
		
		
		props.put("mail.smtp.auth", getProperties().getProperty("auth", "false"));
		props.put("mail.smtp.starttls.enable", getProperties().getProperty("starttls.enable","false"));
		props.put("mail.smtp.host", getProperties().getProperty("host"));
		props.put("mail.smtp.port", getProperties().getProperty("port"));
		
		if (authEnabled) {
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(getProperties().getProperty("username"), getProperties().getProperty("password"));
			}
		  });
		
		return session;
		}
		else {
			Session session = Session.getInstance(props);
			return session;
		}
		
		
 
	}

}
