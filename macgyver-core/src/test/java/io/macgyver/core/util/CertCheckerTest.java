package io.macgyver.core.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CertCheckerTest {

	Logger logger = LoggerFactory.getLogger(CertCheckerTest.class);

	@Test
	public void testX() throws IOException, GeneralSecurityException {
		CertChecker c = new CertChecker();

		logger.info(new ObjectMapper().writerWithDefaultPrettyPrinter()
				.writeValueAsString(
						c.checkCertificates("https://www.google.com")));
	}

}
