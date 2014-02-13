package io.macgyver.email;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.macgyver.config.SmtpSessionFactoryBean;

import org.junit.Test;
import org.springframework.context.annotation.Bean;


public class SmtpSessionFactoryTest {

	
	@Test
	public void testMail() {
		SmtpSessionFactoryBean f = new SmtpSessionFactoryBean();
	/*	f.setHost("smtp.gmail.com");
		f.setPort(587);
		f.setAuthEnabled(true);
		f.setStartTlsEnabled(true);
		f.setUsername("xxxx@xxxx.com");
		f.setPassword("xxxxx");
		*/
	

	}
}
