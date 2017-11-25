package com.paddavoet.bittradr;

import com.paddavoet.bittradr.application.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.paddavoet.bittradr.service.MarketService;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
@ComponentScan("com.paddavoet.bittradr.service")
@ComponentScan("com.paddavoet.bittradr.component")
@ComponentScan("com.paddavoet.bittradr.controller")
@EnableOAuth2Sso
public class Application {
	
	@Autowired
	private MarketService marketService;
	
	public static void main(String[] args) throws Exception {    	
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        
        ApplicationConfig.initialise(context);
    }


}
