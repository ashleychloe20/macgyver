package io.macgyver.cloud.vsphere;

import io.macgyver.core.MacGyverException;
import io.macgyver.core.ServiceFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.vmware.vim25.mo.ServiceInstance;

public class VSphereClientFactory extends ServiceFactory<ServiceInstance> {

	String url;
	String username;
	String password;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public ServiceInstance create() {

		try {

			ServiceInstance si = new ServiceInstance(new URL(url), username,
					password, true);
			return si;
		} catch (MalformedURLException e) {
			throw new MacGyverException(e);
		} catch (IOException e) {
			throw new MacGyverException(e);
		}
	}

	public VSphereQueryService createQueryService() {
		VSphereQueryService q = new VSphereQueryService();
		q.setVSphereClientFactory(this);
		return q;

	}
}
