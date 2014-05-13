package io.macgyver.config;

import java.util.Map;

import io.macgyver.core.HookScriptManager;
import io.macgyver.core.web.auth.InternalAuthenticationProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import com.google.common.collect.Maps;

@Configuration
@EnableWebMvcSecurity
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	InternalAuthenticationProvider internalAuthenticationProvider;
	
	@Autowired
	HookScriptManager hookScriptManager;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();
    	
    	AuthenticationManager x;
    	
    	
        http
            .authorizeRequests()
                .antMatchers("/login","/resources/**", "/webjars/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/**").authenticated();
        http
            .formLogin()
                .loginPage("/login").failureUrl("/login").defaultSuccessUrl("/")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    	auth.authenticationProvider(internalAuthenticationProvider);
    	
    	Map<String,Object> map = Maps.newHashMap();
    	map.put("auth", auth);
    	hookScriptManager.invokeHook("authBuilder",map);
    }
    
    
    
    @Autowired
    public void registerSharedAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    	
    	auth.authenticationProvider(internalAuthenticationProvider);
    	
    	Map<String,Object> map = Maps.newHashMap();
    	map.put("auth", auth);
    	hookScriptManager.invokeHook("authBuilder",map);
    }
}