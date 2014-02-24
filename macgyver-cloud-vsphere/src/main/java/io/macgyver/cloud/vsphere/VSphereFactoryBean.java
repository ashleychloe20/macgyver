package io.macgyver.cloud.vsphere;

import io.macgyver.core.MacGyverException;
import io.macgyver.core.ServiceFactoryBean;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import com.vmware.vim25.mo.ServiceInstance;

public class VSphereFactoryBean extends
		ServiceFactoryBean<ServiceInstanceFactory> {

	public VSphereFactoryBean() {
		super(ServiceInstanceFactory.class);
	}

	@Override
	public ServiceInstanceFactory createObject() {

		ServiceInstanceFactory f = new ServiceInstanceFactory();
		Properties p = getProperties();
		f.setUrl(p.getProperty("url"));
		f.setUsername(p.getProperty("username"));
		f.setPassword(p.getProperty("password"));
		f.setIgnoreCert(Boolean.parseBoolean(p
				.getProperty("ignoreCert", "true")));

		return f;

	}

}
