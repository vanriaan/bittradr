package com.paddavoet.bittradr;

import static reactor.bus.selector.Selectors.$;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.paddavoet.bittradr.application.ApplicationConfig;
import com.paddavoet.bittradr.component.rulesengine.MarketRule;
import com.paddavoet.bittradr.service.MarketService;

import reactor.Environment;
import reactor.bus.EventBus;

@SpringBootApplication
@ComponentScan("com.paddavoet.bittradr.service")
@ComponentScan("com.paddavoet.bittradr.component")
@ComponentScan("com.paddavoet.bittradr.controller")
@EnableOAuth2Sso
public class Application implements CommandLineRunner {
	
	@Autowired
	private MarketService marketService;
	
	@Autowired
	private EventBus eventBus;
	
	@Autowired
	List<MarketRule> marketRules;
	
	@Bean
    Environment env() {
        return Environment.initializeIfEmpty()
                          .assignErrorJournal();
    }
	
	@Bean
    EventBus createEventBus(Environment env) {
	    return EventBus.create(env, Environment.THREAD_POOL);
    }
	
	@Override
	public void run(String... args) throws Exception {
		for (MarketRule rule : marketRules) {
			eventBus.on($("market_change"), rule);			
		}
	}
	
	public static void main(String[] args) throws Exception {    
		
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        
        ApplicationConfig.initialise(context);
    }


}
