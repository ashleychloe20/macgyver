package io.macgyver.test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;



@ContextConfiguration(loader=AnnotationConfigContextLoader.class,  classes=TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class MacgyverIntegrationTest extends AbstractJUnit4SpringContextTests {

	protected Logger logger = LoggerFactory.getLogger(getClass());
}
