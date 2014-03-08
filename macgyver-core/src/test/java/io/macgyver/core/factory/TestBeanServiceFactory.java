package io.macgyver.core.factory;

import java.util.Properties;

import io.macgyver.core.TestBean;

public class TestBeanServiceFactory extends ServiceFactory<TestBean> {

	public TestBeanServiceFactory() {
		super("testService");
	}
	@Override
	protected TestBean createObjecct(Properties props) {
		System.out.println("creating testbean with props: "+props);
		return new TestBean();
	}

}
