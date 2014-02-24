package io.macgyver.cloud.vsphere;

import io.macgyver.core.MacGyverException;

import java.io.IOException;
import java.net.URL;

import javax.naming.ConfigurationException;

import com.vmware.vim25.mo.ServiceInstance;

public class ServiceInstanceFactory {

	ServiceInstance serviceInstance = null;

	private String url;
	private String username;
	private String password;
	private boolean ignoreCert = true;

	public synchronized ServiceInstance getServiceInstance() {
		if (serviceInstance == null) {
			try {
			ServiceInstance si = new ServiceInstance(new URL(url), username,
					password, ignoreCert);
			this.serviceInstance = si;
			}
			catch (IOException e) {
				throw new MacGyverException(e);
			}
		}
		return serviceInstance;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isIgnoreCert() {
		return ignoreCert;
	}

	public void setIgnoreCert(boolean ignoreCert) {
		this.ignoreCert = ignoreCert;
	}

}
