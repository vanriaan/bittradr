package com.paddavoet.bittradr.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.paddavoet.bittradr.service.MarketService;

@SpringBootApplication
@ComponentScan("com.paddavoet.bittradr.service")
@ComponentScan("com.paddavoet.bittradr.component")
@ComponentScan("com.paddavoet.bittradr.controller")
public class Application {
	
	@Autowired
	private MarketService marketService;
	
	public static void main(String[] args) throws Exception {    	
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        
        ApplicationConfig.initialise(context);
    }
}
