package io.macgyver.plugin.cloud.vsphere;

import io.macgyver.core.MacGyverException;
import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import java.io.IOException;
import java.net.URL;

import org.springframework.stereotype.Component;

import com.vmware.vim25.mo.ServiceInstance;

@Component
public class VSphereFactory extends BasicServiceFactory<ServiceInstance> {


	public static final String CERTIFICATE_VERIFICATION_DEFAULT="true";
	public VSphereFactory() {
		super("vsphere");
	}

	@Override
	protected ServiceInstance doCreateInstance(ServiceDefinition def) {
		try {
		logger.info("connecting to vcenter at: {}", def.getProperties().getProperty("url"));
		ServiceInstance si = new ServiceInstance(new URL(
				def.getProperties().getProperty("url")), def.getProperties().getProperty("username"),
				def.getProperties().getProperty("password"), Boolean.parseBoolean(def.getProperties().getProperty(CERTIFICATE_VERIFICATION_ENABLED,
						CERTIFICATE_VERIFICATION_DEFAULT)));

		return si;
		}
		catch (IOException e) {
			throw new MacGyverException(e);
		}
		
	}



}
