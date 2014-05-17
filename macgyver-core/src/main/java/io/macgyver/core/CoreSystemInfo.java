package io.macgyver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.io.Closer;

public class CoreSystemInfo {

	Logger logger = LoggerFactory.getLogger(CoreSystemInfo.class);

	@Autowired
	org.springframework.context.ApplicationContext applicationContext;

	
	AtomicReference<Map<String, String>> dataRef = new AtomicReference<>(
			Collections.unmodifiableMap((new HashMap<String,String>())));

	public Map<String, String> getData() {
		return dataRef.get();
	}

	@PostConstruct
	public void populateRevisionInfo() {

		Map<String, String> m = (Map<String, String>) Collections
				.unmodifiableMap(loadRevisionInfo());
		dataRef.set(m);

	}

	protected Map<String, String> loadRevisionInfo() {
		Closer c = Closer.create();
		try {

			Resource resource = applicationContext
					.getResource("classpath:macgyver-core-revision.properties");
			if (resource.exists()) {

				InputStream is = resource.getInputStream();
				c.register(is);
				if (is != null) {
					Properties p = new Properties();
					p.load(is);
					Map<String,String> m = Maps.newHashMap();
					for (Map.Entry<Object, Object> entry: p.entrySet()) {
						m.put(entry.getKey().toString(), entry.getValue().toString());
					}
					return m;
				}

			}

		} catch (Exception e) {
			logger.warn("could not load revision info", e);
		} finally {
			try {
				c.close();
			} catch (IOException e) {
				logger.warn("", e);
			}
		}
		return Maps.newHashMap();
	}
}
