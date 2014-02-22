package io.macgyver.metrics.graphite;

import java.util.Map;

import io.macgyver.metrics.TSV;
import io.macgyver.test.MacgyverIntegrationTest;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

public class HostedGrphiteTest extends MacgyverIntegrationTest{

	@Autowired
	ClientConfig clientConfig;
	
	@Test
	public void testHostedGraphite() throws Exception {
		HostedGraphite g = applicationContext.getBean("testHostedGraphite",
				HostedGraphite.class);
		g.record("macgyver.abc", 123);
		Assert.assertNotNull(g);

		Thread.sleep(2000);
	}
	
	@Test
	public void testQuery()  throws Exception {
		
		// http://graphite.readthedocs.org/en/latest/render_api.html
		
		HostedGraphite hg = new HostedGraphite(clientConfig);
		applicationContext.getAutowireCapableBeanFactory().initializeBean(hg, null);
		hg.setGraphiteQueryBaseUrl("https://www.hostedgraphite.com/adce91cd/e2ae8701-4e67-4dc4-b456-c8dfd75681ac/graphite");
		System.out.println(hg.queryTimeSeries("lc.db.LCGG.n800.gglag.R_LCGG","-1d",null));
		
		
		System.out.println(hg.queryMostRecentValue("lc.db.LCGG.n800.gglag.R_LCGG"));
	}
}
