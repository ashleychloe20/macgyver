package io.macgyver.http.jetty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.resource.ResourceCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closer;

public class StatcResourceServlet extends HttpServlet {

	Logger logger = LoggerFactory.getLogger(getClass());

	boolean isExcluded(HttpServletRequest req) {
		String path = req.getRequestURI();
		if (path == null) {
			return true;
		}
		if (path.endsWith(".ftl")) {
			return true;
		}
		return false;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ResourceCollection c = new ResourceCollection();

		String uri = req.getRequestURI();
		InputStream input = null;
		String resourcePath = "io/macgyver/web" + uri;

		Closer closer = Closer.create();

		boolean found = false;

		try {
			if (isExcluded(req)) {
				// do not serve
			} else {
				URL urx = getClass().getClassLoader().getResource(resourcePath);
				if (urx != null) {
					input = urx.openStream();
					if (input != null) {
						found = true;
						logger.debug("serving: {}", urx);
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

}
