package com.paddavoet.bittradr.component.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paddavoet.bittradr.entity.MarketChange;

import reactor.bus.Event;
import reactor.bus.EventBus;

@Service
public class MarketChangePublisher {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	EventBus eventBus;
	
	public void publishChange(MarketChange marketChange) throws InterruptedException {

		eventBus.notify("market_change", Event.wrap(marketChange));
		
		log.debug("published event to 'market_change' queue");
	}
}
