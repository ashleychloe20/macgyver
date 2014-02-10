package io.macgyver.email;

import io.macgyver.core.ServiceFactory;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class SmtpSessionFactory  extends ServiceFactory<Session>{

	public SmtpSessionFactory() {
	
	}

	boolean authEnabled;
	boolean startTlsEnabled=false;
	String host;
	int port;
	String username;
	String password;
	
	public boolean isAuthEnabled() {
		return authEnabled;
	}

	public void setAuthEnabled(boolean authEnabled) {
		this.authEnabled = authEnabled;
	}

	public boolean isStartTlsEnabled() {
		return startTlsEnabled;
	}

	public void setStartTlsEnabled(boolean startTlsEnabled) {
		this.startTlsEnabled = startTlsEnabled;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Session create() {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(host),"host must be set");
		
		Properties props = new Properties();
		
		
		props.put("mail.smtp.auth", Boolean.toString(authEnabled));
		props.put("mail.smtp.starttls.enable", Boolean.toString(startTlsEnabled));
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", Integer.toString(port));
		
		if (authEnabled) {
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
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
