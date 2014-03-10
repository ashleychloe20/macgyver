package io.macgyver.web.rythm;

import io.macgyver.core.Kernel;

import java.io.File;

import org.rythmengine.RythmEngine;
import org.rythmengine.resource.ITemplateResource;
import org.rythmengine.resource.ResourceLoaderBase;
import org.rythmengine.resource.TemplateResourceBase;
import org.rythmengine.utils.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MacGyverRythmResourceLoader extends ResourceLoaderBase {

	Logger logger = LoggerFactory.getLogger(MacGyverRythmResourceLoader.class);

	public MacGyverRythmResourceLoader() {
		super();

	}

	static RythmEngine macgyverRythmEngine;

	public static void setRhythmEngine(RythmEngine engine) {
		// this is kind of a hack
		macgyverRythmEngine = engine;
	}

	@Override
	public ITemplateResource load(String path) {

		File templateDir = new File(Kernel.getInstance().getExtensionDir(),
				"web");

		

		final File finalTemplate = new File(convertDotsToSlash(new File(templateDir,path).getAbsolutePath()));
		logger.info("looking for: {}", finalTemplate);
		if (finalTemplate.exists()) {
			return new TemplateResourceBase() {

			
				private static final long serialVersionUID = -8139914562941211801L;

				public boolean isValid() {
					return null != finalTemplate
							&& !finalTemplate.isDirectory()
							&& finalTemplate.canRead();
				}

				@Override
				public Object getKey() {
					return finalTemplate.getAbsolutePath();
				}

				@Override
				protected String reload() {
					return IO.readContentAsString(finalTemplate);
				}

				@Override
				protected long lastModified() {

					return finalTemplate.lastModified();
				}

				@Override
				protected long defCheckInterval() {
					// TODO Auto-generated method stub
					return 5 * 1000;
				}
			};

		}
		
		path = stripLeadingSlash((path));
		logger.info("searching classpath for " + path);
		CustomClasspathTemplateResource ctr = new CustomClasspathTemplateResource(
				"web/" + stripLeadingSlash(path), this);
		if (ctr.exists()) {
			return ctr;
		} else {
			logger.info("did not locate template for: ",path);
		}

		return null;
	}

	@Override
	public String getResourceLoaderRoot() {
		return "/";
	}

	@Override
	public RythmEngine getEngine() {
		RythmEngine parentEngine = super.getEngine();
		if (parentEngine != null) {
			return parentEngine;
		}
		return macgyverRythmEngine;
	}

	public static String convertDotsToSlash(String input) {
		int idx = input.lastIndexOf(".");
		if (idx>0) {
			return input.substring(0,idx).replace(".", "/")+input.substring(idx);
		}
		else {
			return input;
		}
	}
	public static String stripLeadingSlash(String input) {
		while (input.startsWith("/")) {
			input = input.substring(1);
		}
		return input;
	}
}
