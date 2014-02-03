package io.macgyver.elb.a10;

import io.macgyver.elb.ELBException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ListIterator;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class A10Client {

	String username;
	String password;
	String url;
	
	String token;
	
	public A10Client(String url, String username, String password) {
		this.url = url;
		this.username=username;
		this.password=password;
	}
	
	
	
	public void authenticate() {
		WebTarget wt = newWebTarget();
	
		
		Form f = new Form().param("username", username).param("password", password).param("format", "json").param("method", "authenticate");
		Response resp = wt.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		
		JsonObject obj = resp.readEntity(JsonObject.class);
		
		String sid = obj.getString("session_id");
		if (sid==null) {
			throw new ELBException("authentication failed");
		}
		this.token = sid;
		
	}
	
	public void getAllSLB() {
	WebTarget wt = newWebTarget();
	
		
		Form f = new Form().param("session_id", token).param("format", "json").param("method", "slb.service_group.getAll");
		Response resp = wt.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		
		JsonObject obj = resp.readEntity(JsonObject.class);
		JsonArray arr = obj.getJsonArray("service_group_list");
		ListIterator<JsonValue> t = arr.listIterator();
		while (t.hasNext()) {
			t.next();
		}
		
	}
	
	protected WebTarget newWebTarget() {
		try {
		 HostnameVerifier verifier = new HostnameVerifier() {
				
				@Override
				public boolean verify(String hostname, SSLSession session) {
					// TODO Auto-generated method stub
					return true;
				}
			};
			
			 TrustManager[] trustAllCerts = new TrustManager[]{
				        new X509TrustManager() {
							
							@Override
							public X509Certificate[] getAcceptedIssuers() {
								// TODO Auto-generated method stub
								return null;
							}
							
							@Override
							public void checkServerTrusted(X509Certificate[] arg0, String arg1)
									throws CertificateException {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void checkClientTrusted(X509Certificate[] arg0, String arg1)
									throws CertificateException {
								// TODO Auto-generated method stub
								
							}
						}
				    };
			SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());

	
		
		Client client = ClientBuilder.newBuilder().hostnameVerifier(verifier).sslContext(sc).build();

		WebTarget t = client.target(url);
		return t;
		}
		catch (KeyManagementException e) {
			
			throw new ELBException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new ELBException(e);
		}
	}
}
