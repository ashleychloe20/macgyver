package io.macgyver.core;

import io.macgyver.core.ServiceFactoryBean;
import io.macgyver.core.crypto.Crypto;

import org.springframework.beans.factory.annotation.Autowired;

public class TestServiceFactoryBean extends ServiceFactoryBean<TestBean> {

	@Autowired
	Crypto crypto;

	public TestServiceFactoryBean() {
		super(TestBean.class);
	}

	@Override
	public TestBean createObject() throws Exception {
		TestBean tb = new TestBean();
		
		return tb;
	}



}
