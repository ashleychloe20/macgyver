package io.macgyver.metrics.statsd;

import io.macgyver.test.MacgyverIntegrationTest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatsDTest extends MacgyverIntegrationTest{

	static Logger logger = LoggerFactory.getLogger(StatsDTest.class);
	static volatile boolean running = true;
	public static DatagramSocket socket;
	Thread consumer;

	static ConcurrentLinkedQueue<String> recvList = new java.util.concurrent.ConcurrentLinkedQueue<String>();

	public static class ConsumerRunnable implements Runnable {

		@Override
		public void run() {
			DatagramPacket p = new DatagramPacket(new byte[1024], 1024);
			while (running) {
				try {
					socket.receive(p);
					String val = new String(p.getData(), p.getOffset(),
							p.getLength());
					recvList.add(val);
				} catch (IOException e) {
					logger.info("", e);
				}
			}

		}

	}

	@BeforeClass
	public static void setup()  {
		try {
			socket = new DatagramSocket(new InetSocketAddress("localhost", 0));

			Thread t = new Thread(new ConsumerRunnable());
			t.setDaemon(true);
			t.start();

		} catch (Exception e) {
			logger.warn("skipping UDP tests because of failure to set up socket",e);
			Assume.assumeTrue(false);
		}
	}

	@Test
	public void testStatsD() throws Exception {
		StatsD sd = new StatsD("localhost", socket.getLocalPort());

		for (int i = 0; i < 10000; i++) {
			sd.record("metricName", i);

		}

		long sleepDelay = 500;
		Thread.sleep(sleepDelay);
		String val = recvList.peek();
		logger.info("sample received message: " + val);
		Assert.assertTrue(val.startsWith("metricName:"));
		logger.info("after {} ms, recv'd {} datagrams", sleepDelay,
				recvList.size());
	}
	
	@Test
	public void testConfig() {
		Assert.assertNotNull(applicationContext.getBean("testStatsD"));
	}
}
