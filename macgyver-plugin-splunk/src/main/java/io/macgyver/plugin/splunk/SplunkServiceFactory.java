package io.macgyver.plugin.splunk;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import java.util.Properties;

import com.splunk.Service;
import com.splunk.ServiceArgs;

public class SplunkServiceFactory extends BasicServiceFactory<Service>{

	public SplunkServiceFactory() {
		super("Splunk");
		
	}

	@Override
	protected Service doCreateInstance(ServiceDefinition def) {
		
		
		Properties p = def.getProperties();
		
		ServiceArgs args = new ServiceArgs();
		String username = p.getProperty("username");
		String password = p.getProperty("password");
		String host = p.getProperty("host");
		String port = p.getProperty("port","8089");
		String scheme = p.getProperty("scheme");
		String token = p.getProperty("token");
		
		if (username!=null) {
			args.setUsername(username);
		}
		if (password!=null) {
			args.setPassword(password);
		}
		if (host!=null) {
			args.setHost(host);
		}
		if (port!=null) {
			args.setPort(Integer.parseInt(port));
		}
		if (token!=null) {
			args.setToken(token);
		}
		if (scheme!=null) {
			args.setScheme(scheme);
		}
		
		Service service = Service.connect(args);
		return service;
	}



}
