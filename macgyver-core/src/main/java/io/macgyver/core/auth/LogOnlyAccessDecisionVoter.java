package io.macgyver.core.auth;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

public class LogOnlyAccessDecisionVoter implements AccessDecisionVoter<Object> {

	Logger logger = LoggerFactory.getLogger(LogOnlyAccessDecisionVoter.class);

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		if (logger.isDebugEnabled()) {
			logger.debug("auth: {}, object: {}, attrs: {}", authentication,
					object, attributes);
		}
		return AccessDecisionVoter.ACCESS_ABSTAIN;
	}

}
