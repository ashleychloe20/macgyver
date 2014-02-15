package io.macgyver.cloud.vsphere;

import io.macgyver.core.MacGyverException;
import io.macgyver.core.ServiceFactoryBean;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;

import com.vmware.vim25.mo.ServiceInstance;

public class VSphereFactoryBean extends ServiceFactoryBean<ServiceInstance> {


	public VSphereFactoryBean() {
		super(ServiceInstance.class);
	}

	@Override
	public ServiceInstance createObject() {

		try {

			ServiceInstance si = new ServiceInstance(new URL(getProperties().getProperty("url")), getProperties().getProperty("username"),
					getProperties().getProperty("password"), true);
			return si;
		} catch (MalformedURLException e) {
			throw new MacGyverException(e);
		} catch (IOException e) {
			throw new MacGyverException(e);
		}
	}


}
