package io.macgyver.web.jetty;

import io.macgyver.core.Kernel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.glassfish.jersey.message.internal.MediaTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closer;

public class StaticResourceServlet extends HttpServlet {

	Logger logger = LoggerFactory.getLogger(getClass());
	MimeTypes mimeTypes;

	boolean isExcluded(String path) {
		if (path == null) {
			return true;
		}
		if (path.endsWith(".rythm")) {
		//	return true;
		}
		if (path.startsWith("/views")) {
			return true;
		}
		return false;	
	}
	boolean isExcluded(HttpServletRequest req) {
		return isExcluded(req.getRequestURI());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ResourceCollection c = new ResourceCollection();

		String uri = req.getRequestURI();
		InputStream input = null;
		String resourcePath = uri;

		Closer closer = Closer.create();

		boolean found = false;

		try {
			if (isExcluded(req)) {
				resp.sendError(404);
				return;
			} else {
				URL urx = null;
				File f = new File(new File(Kernel.getInstance()
						.getExtensionDir(), "web"), uri);
				if (!f.isDirectory() && f.exists()) {
					urx = f.toURI().toURL();

				} else {
					urx = getClass().getClassLoader().getResource(resourcePath);
				}
				
				if (urx != null) {
					Optional<String> contentType = chooseContentType(uri);
					if (contentType.isPresent()) {
						resp.setContentType(contentType.get());
					}
					input = urx.openStream();
					if (input != null) {
						found = true;
						if (logger.isDebugEnabled()) {
							logger.debug("serving {} from {}", uri, urx);
						}
						closer.register(closer);

						OutputStream output = resp.getOutputStream();

						ByteStreams.copy(input, output);
						return;
					}

				}
			}

		} finally {
			if (!found) {
				logger.debug("404 {}", uri);
				resp.setStatus(404);
			}
			closer.close();
		}

	}

	Optional<String> chooseContentType(String path) {
		try {

			String contentType = mimeTypes.getMimeByExtension(path);
			return Optional.fromNullable(contentType);
		} catch (RuntimeException e) {
			return Optional.absent();
		}

	}

	@Override
	public void init() throws ServletException {

		super.init();

		this.mimeTypes = Kernel.getInstance().getApplicationContext()
				.getBean("io.macgyver.web.MimeTypes", MimeTypes.class);
		Preconditions.checkNotNull(mimeTypes);
	}

}
