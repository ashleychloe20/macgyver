package io.macgyver.config;

import io.macgyver.core.ServiceFactoryBean;
import io.macgyver.core.TestBean;


public class TestServiceFactory extends ServiceFactoryBean<TestBean> {
	


	
	public TestServiceFactory() {
		super(TestBean.class);
	}

	@Override
	public TestBean getObject() throws Exception {
		return new TestBean();
	}
	
	

}
