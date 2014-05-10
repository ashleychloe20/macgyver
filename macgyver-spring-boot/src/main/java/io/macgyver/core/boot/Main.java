package io.macgyver.core.boot;

import java.util.Arrays;

import io.macgyver.config.MyConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ComponentScan(basePackageClasses={Main.class,MyConfig.class})
@EnableAutoConfiguration
public class Main {


	
    public static void main(String[] args) {
    	
        SpringApplication.run(Main.class, args);
        
    }
    
}

@RestController
class GreetingController {
	@Autowired 
	ApplicationContext ctx;
	
    @RequestMapping("/hello/{name}")
    String hello(@PathVariable String name) {
    	System.out.println();
    	System.out.println( Arrays.asList(ctx.getBeanDefinitionNames()));
        return "Hello, " + name + "!";
    }
}