package io.macgyver.metrics.statsd;

import io.macgyver.metrics.MetricRecorder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatsD implements MetricRecorder {

	static Logger logger = LoggerFactory.getLogger(StatsD.class);
	String host;
	int port;
	ExecutorService sender;
	BlockingQueue<Runnable> linkedBlockingQueue;
	AtomicReference<DatagramSocket> clientSocket = new AtomicReference<>();

	public static class RejectionHandler implements RejectedExecutionHandler {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			logger.warn("rejected send");

		}

	}

	public StatsD(String host, int port, String prefix) {
		this.host = host;
		this.port = port;

		linkedBlockingQueue = new LinkedBlockingDeque<Runnable>(50000);
		sender = new ThreadPoolExecutor(2, 2, 5, TimeUnit.SECONDS,
				linkedBlockingQueue, new RejectionHandler());

	}

	public void ensure() {
		try {
			if (clientSocket.get() == null) {
				DatagramSocket s = new DatagramSocket();

				s.connect(new InetSocketAddress(this.host, port));
				clientSocket.set(s);
			}
		} catch (SocketException e) {
			logger.warn(e.toString());
		}

	}

	@Override
	public void record(String name, long value) {

		sender.execute(new GaugeSender(name, value));

	}

	public class GaugeSender implements Runnable {

		String key;
		long val;

		GaugeSender(String name, long val) {
			this.key = name;
			this.val = val;
		}

		@Override
		public void run() {
			try {
				ensure();
				String payload = null;

				payload = String.format("%s:%d|g", key, val);

				final byte[] data = payload.getBytes();
				final DatagramPacket sendPacket = new DatagramPacket(data,
						data.length);
				clientSocket.get().send(sendPacket);
			} catch (IOException e) {
				logger.warn("problem sending: {}",e.toString());
			}
		}

	}

}
