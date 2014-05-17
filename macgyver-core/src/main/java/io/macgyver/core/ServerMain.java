package io.macgyver.core;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import io.macgyver.core.mapdb.BootstrapMapDB;

import org.mapdb.DB;
import org.mapdb.TxBlock;
import org.mapdb.TxRollbackException;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

/**
 * Simple wrapper to start server.
 * 
 * @author rschoening
 * 
 */

@Configuration
@ComponentScan(basePackages = { "io.macgyver.config",
		"io.macgyver.plugin.config", "io.macgyver.core.config" })
@EnableAutoConfiguration
public class ServerMain {

	static Logger logger = org.slf4j.LoggerFactory.getLogger(ServerMain.class);

	public static void main(String[] args) throws Exception {

		printBanner();

		BootstrapMapDB bootstrap = BootstrapMapDB.getInstance();
		bootstrap.init();

		TxBlock b = new TxBlock() {

			@Override
			public void tx(DB db) throws TxRollbackException {

			}
		};
		bootstrap.getTxMaker().get().execute(b);
		SpringApplication app = new SpringApplication(ServerMain.class);
		app.setShowBanner(false);
		ConfigurableApplicationContext ctx = app.run(args);

		Environment env = ctx.getEnvironment();

		logger.info("Spring environment: {}", env);

	}

	public static void printBanner() {

		// Spring boot doesn't support alternate banner until 1.1.x
		String bannerText = "\n";
		try {
			URL url = ServerMain.class.getResource("/banner.txt");
			if (url != null) {
				try (InputStreamReader reader = new InputStreamReader(
						url.openStream(), Charsets.UTF_8)) {
					String text = CharStreams.toString(reader);

					bannerText += text;
				}
			}

			url = null;
			url = ServerMain.class
					.getResource("/macgyver-core-revision.properties");
			if (url != null) {
				Properties p = new Properties();
				try (InputStream x = url.openStream()) {
					p.load(x);
				}

				bannerText += String.format("\n\n                      (v%s rev:%s)\n",
								p.getProperty("version"),
								p.getProperty("gitShortCommitId"));
			}

		} catch (Exception e) {
			logger.warn("could not load banner");
		}
		System.out.println(bannerText);
	}
}
