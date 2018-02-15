package com.paddavoet.bittradr.component.rulesengine;

import org.springframework.stereotype.Service;

import com.paddavoet.bittradr.entity.MarketChange;

import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public interface MarketRule extends Consumer<Event<MarketChange>> {
	TradeAction getTradeAction();
}
