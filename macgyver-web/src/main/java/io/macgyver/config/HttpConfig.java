package io.macgyver.config;

import io.macgyver.http.jetty.JettyServer;
import io.macgyver.http.shiro.DelegatingAuthorizingRealm;
import io.macgyver.http.shiro.StaticAuthorizingRealm;
import io.macgyver.http.spark.AuthSparklet;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class HttpConfig {

	int httpPort = 8080;

	@Bean
	public JettyServer jettyServer() {
		return new JettyServer(8080);
		
	}

	@Bean
	public AuthSparklet adminSparklet() {
		return new AuthSparklet();
	}

    @Bean(name = "defaultWebSecurityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        
        dwsm.setRealm(delegatingAuthorizingRealm());
        return dwsm;
    }


    @Bean(name = "shiroFilter")
    @DependsOn(value="defaultWebSecurityManager")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean sf = new ShiroFilterFactoryBean();
        sf.setSecurityManager(defaultWebSecurityManager());

        return sf;
    }
    
    @Bean
    public DelegatingAuthorizingRealm delegatingAuthorizingRealm() {
    	return new DelegatingAuthorizingRealm();
    }
    
	@Bean
	public StaticAuthorizingRealm staticAuthorizingRealm() {
		return new StaticAuthorizingRealm();
	}
}
