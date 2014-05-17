package io.macgyver.core.config;

import io.macgyver.core.HookScriptManager;
import io.macgyver.core.web.auth.InternalAuthenticationProvider;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import com.google.common.collect.Maps;

@Configuration
@EnableWebMvcSecurity
// @EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	InternalAuthenticationProvider internalAuthenticationProvider;

	@Autowired
	HookScriptManager hookScriptManager;

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

		.antMatchers("/login","/public/**", "/resources/**", "/webjars/**").permitAll()
				.and().authorizeRequests().and().authorizeRequests()
				.antMatchers("/**").authenticated().and().

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
			http.csrf().disable().antMatcher("/api/**").authorizeRequests().anyRequest()
					.authenticated().and().httpBasic();
		}
	}

	@Configuration
	@Order(91)
	public static class ApiPublicWebSecurityConfigurationAdapter extends
			WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/public/**").anonymous();

		}
	}

}