package io.macgyver.core.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class GrantedAuthoritiesTranslatorChain extends GrantedAuthoritiesTranslator {

	List<GrantedAuthoritiesTranslator> mapperList = Lists.newCopyOnWriteArrayList();
	
	
	
	public void addTranslator(GrantedAuthoritiesTranslator t) {
		Preconditions.checkNotNull(t);
		mapperList.add(t);
	}

	protected void translate(Collection<? extends GrantedAuthority> source, Collection<? extends GrantedAuthority> target) {
	
		for (GrantedAuthoritiesTranslator t: mapperList) {
			t.translate(source,target);
		}
			
	}
}
