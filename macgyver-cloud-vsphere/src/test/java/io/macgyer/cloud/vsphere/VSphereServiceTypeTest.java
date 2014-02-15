package io.macgyer.cloud.vsphere;

import org.junit.Assert;
import org.junit.Test;

import io.macgyver.cloud.vsphere.VSphereFactoryBean;
import io.macgyver.core.ServiceFactoryClassFinder;

public class VSphereServiceTypeTest {

	
	@Test
	public void checkServiceTypes() throws ClassNotFoundException {
		ServiceFactoryClassFinder locator = new ServiceFactoryClassFinder();
		Assert.assertSame(VSphereFactoryBean.class,locator.forServiceType("vsphere"));
	}
}
