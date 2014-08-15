package io.macgyver.plugin.cmdb;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface CheckInProcessor {

	public boolean checkAuth(HttpServletRequest r);
	public void decorate(ObjectNode data);
	
	public ObjectNode process(HttpServletRequest r) throws IOException;
}
