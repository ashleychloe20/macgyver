package io.macgyver.core.scheduler;

import io.macgyver.core.scheduler.AutoScheduler;

import java.io.File;
import java.io.IOException;

import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public class AutoSchedulerTest {

	@Test
	public void testCrontabScanner() throws IOException {

		File f = File.createTempFile("junit", "junit");
		String cronExpression = "* * * * * ?";
		String expected = "{\"cron\":\""+cronExpression+"\"}";
		String data = "blah\nblah\n//   #@Schedule    " + expected + "   \n\nblah";
		Files.write(data, f, Charsets.UTF_8);

		LineProcessor<Optional<JsonObject>> lp = new AutoScheduler.CrontabLineProcessor();

		Optional<JsonObject> x = Files.readLines(f, Charsets.UTF_8, lp);

		Assert.assertTrue(x.isPresent());
		org.junit.Assert.assertEquals(cronExpression, x.get().getString("cron"));
	}
}
