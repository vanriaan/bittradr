package com.paddavoet.bittradr.component.rulesengine;

import com.paddavoet.bittradr.entity.MarketChange;

import reactor.bus.Event;

public abstract class BaseMarketRule implements MarketRule {
	TradeAction tradeAction = TradeAction.UNDETERMINED;
	
	protected abstract void calculateTradeAction(MarketChange marketChange);
	
	@Override
	public void accept(Event<MarketChange> event) {
		MarketChange marketChange = event.getData();
		
		calculateTradeAction(marketChange);
	}
	
	public TradeAction getTradeAction() {
		return tradeAction;
	}
}
