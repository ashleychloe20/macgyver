package io.macgyver.core.config;

import io.macgyver.core.ScriptHookManager;
import io.macgyver.core.auth.GrantedAuthoritiesTranslatorChain;
import io.macgyver.core.auth.GrantedAuthoritiesTranslatorScriptHook;
import io.macgyver.core.auth.InternalAuthenticationProvider;
import io.macgyver.core.auth.LogOnlyAccessDecisionVoter;
import io.macgyver.core.auth.MacGyverAccessDecisionManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Configuration
@EnableWebMvcSecurity
// @EnableWebSecurity
public class CoreSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	InternalAuthenticationProvider internalAuthenticationProvider;

	@Autowired
	ScriptHookManager hookScriptManager;

	@Override
	public void configure(WebSecurity web) throws Exception {

		/*
		 * Map<String, Object> map = Maps.newHashMap(); map.put("webSecurity",
		 * web); hookScriptManager.invokeHook("configureWebSecurity", map);
		 */
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable();

		httpSecurity.authorizeRequests()

		.antMatchers("/login", "/public/**", "/resources/**", "/webjars/**")
				.permitAll().and().authorizeRequests().and()
				.authorizeRequests().antMatchers("/**").hasAnyRole("MACGYVER_USER","MACGYVER_ADMIN").and().

				formLogin().loginPage("/login").failureUrl("/login")
				.defaultSuccessUrl("/").and().logout().permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {

		auth.authenticationProvider(internalAuthenticationProvider);

		Map<String, Object> map = Maps.newHashMap();
		map.put("authBuilder", auth);
		hookScriptManager.invokeHook("configureAuthProviders", map);
	}

	@Autowired
	public void registerSharedAuthentication(AuthenticationManagerBuilder auth)
			throws Exception {

		auth.authenticationProvider(internalAuthenticationProvider);

		Map<String, Object> map = Maps.newHashMap();
		map.put("authBuilder", auth);
		hookScriptManager.invokeHook("configureAuthProviders", map);
	}

	@Configuration
	@Order(92)
	public static class ApiWebSecurityConfigurationAdapter extends
			WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().antMatcher("/api/**").authorizeRequests()
					.anyRequest().authenticated().and().httpBasic();
		}
	}

	@Bean
	List<AccessDecisionVoter> macgyverAccessDecisionVoterList() {
		List<AccessDecisionVoter> x = Lists.newCopyOnWriteArrayList();
		x.add(new LogOnlyAccessDecisionVoter());
		x.add(new RoleVoter());
		
		return x;
	}

	@Bean
	AccessDecisionManager macgyverAccessDecisionManager() {
		
		List<AccessDecisionVoter> list = macgyverAccessDecisionVoterList();
		
		return new MacGyverAccessDecisionManager(list);
	}

	@Configuration
	@Order(91)
	public static class ApiPublicWebSecurityConfigurationAdapter extends
			WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/public/**").anonymous();

		}
	}

	@Bean
	public GrantedAuthoritiesTranslatorChain macGrantedAuthoritiesTranslatorChain() {
		GrantedAuthoritiesTranslatorChain chain = new GrantedAuthoritiesTranslatorChain();
		chain.addTranslator(macGrantedAuthoritiesTranslatorScriptHook());
		return chain;
	}
	@Bean
	public GrantedAuthoritiesTranslatorScriptHook macGrantedAuthoritiesTranslatorScriptHook() {
		return new GrantedAuthoritiesTranslatorScriptHook();
	}
	
	@Bean
	public InternalAuthenticationProvider macInternalAuthenticationProvider() {
		InternalAuthenticationProvider p = new InternalAuthenticationProvider();
		p.setAuthoritiesMapper(macGrantedAuthoritiesTranslatorChain());
		return p;
	}
}