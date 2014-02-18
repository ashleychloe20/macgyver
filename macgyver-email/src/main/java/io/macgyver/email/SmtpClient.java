package io.macgyver.email;

import io.macgyver.core.MacGyverException;

import java.net.InetAddress;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class SmtpClient {

	Logger logger = LoggerFactory.getLogger(SmtpClient.class);
	Session session;

	public SmtpClient() {

	}

	public SmtpClient(Session session) {
		this.session = session;
	}



	public void sendMail(String from, String to, String subject, String body) {
		List<String> recipientList = Lists.newArrayList();
		recipientList.add(to);
		sendMail(from, recipientList, subject, body);
	}

	public void sendMail(String from, List<String> to, String subject,
			String body) {
		try {
			 
			logger.debug("sending email from:{} to:{}",from,to);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			List<InternetAddress> addressList = Lists.newArrayList();
			for (String addr: to) {
				InternetAddress[] address = InternetAddress.parse(addr);
				if (address!=null) {
					for (InternetAddress x: address) {
						addressList.add(x);
					
					}
				}
			}
			message.setRecipients(Message.RecipientType.TO,addressList.toArray(new InternetAddress[0]));
			message.setSubject(subject);
			message.setText(body);
			
 
			Transport.send(message);
 
			
 
		} catch (MessagingException e) {
			throw new MailException(e);
		}
	}
}
