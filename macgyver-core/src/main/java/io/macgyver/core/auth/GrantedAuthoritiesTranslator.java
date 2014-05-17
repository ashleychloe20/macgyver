package io.macgyver.core.auth;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.media.jai.operator.TranslateDescriptor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public  abstract class GrantedAuthoritiesTranslator implements GrantedAuthoritiesMapper {

	@Override
	public final Collection<? extends GrantedAuthority> mapAuthorities(
			Collection<? extends GrantedAuthority> authorities) {
		Collection<? extends GrantedAuthority> target = Lists.newArrayList();
		translate(Collections.unmodifiableCollection(authorities), target);
		return target;
	}

	protected abstract void translate(Collection<? extends GrantedAuthority> source,
			Collection<? extends GrantedAuthority> target) ;
}
