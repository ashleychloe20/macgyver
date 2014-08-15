package io.macgyver.neo4j.rest.impl;

import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.atomic.AtomicReference;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SslTrust {

	public static HostnameVerifier withoutHostnameVerification() {
		HostnameVerifier verifier = new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				return true;
			}
		};
		return verifier;
	}

	static AtomicReference<SSLContext> trustAllContext = new AtomicReference<>();

	public static synchronized SSLContext withoutCertificateValidation() {
		try {
			SSLContext sslContext = trustAllContext.get();
			if (sslContext != null) {
				return sslContext;
			}
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

				@Override
				public X509Certificate[] getAcceptedIssuers() {

					return null;
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {

				}

				@Override
				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {

				}
			} };
			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts,
					new java.security.SecureRandom());
			trustAllContext.set(sslContext);
			return sslContext;
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

	}
}
