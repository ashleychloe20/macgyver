package io.macgyver.core.auth;

import java.util.Collection;
import java.util.Map;

import io.macgyver.core.ScriptHookManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.collect.Maps;

public class GrantedAuthoritiesTranslatorScriptHook extends
		GrantedAuthoritiesTranslator {
	@Autowired
	ScriptHookManager hookScriptManager;

	@Override
	protected void translate(Collection<? extends GrantedAuthority> source,
			Collection<? extends GrantedAuthority> target) {
	
		Map<String,Object> m = Maps.newConcurrentMap();
		m.put("source",source);
		m.put("target",target);
		
		hookScriptManager.invokeHook("translateGrantedAuthorities", m);
	
	}
	
	
}
