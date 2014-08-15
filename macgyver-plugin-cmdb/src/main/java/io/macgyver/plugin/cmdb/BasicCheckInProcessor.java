package io.macgyver.plugin.cmdb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BasicCheckInProcessor implements CheckInProcessor {

	ObjectMapper mapper = new ObjectMapper();

	@Override
	public void decorate(ObjectNode request) {

	}

	@Override
	public boolean checkAuth(HttpServletRequest r) {
		return true;
	}

	@Override
	public ObjectNode process(HttpServletRequest request) throws IOException{

		ObjectNode data = mapper.createObjectNode();
		if (request.getMethod().equalsIgnoreCase("GET")) {
			
			Enumeration<String> t = request.getParameterNames();
			while (t.hasMoreElements()) {
				String key = t.nextElement();
				String val = request.getParameter(key);
				data.put(key, val);
			}
		}
		else if (request.getMethod().equalsIgnoreCase("PUT") || request.getMethod().equalsIgnoreCase("POST")) {
			if (request.getContentType().contains("json")) {
				try (InputStream is = request.getInputStream()) {
					data = (ObjectNode) mapper.readTree(is);
				}
				
			}
		}
		return data;
	}

}
