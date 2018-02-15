package com.paddavoet.bittradr.component.rulesengine;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.paddavoet.bittradr.entity.MarketChange;
import com.paddavoet.bittradr.entity.TradeVelocity;
import com.paddavoet.bittradr.market.TradeValueDirection;

@Component
public class VelocityRule extends BaseMarketRule implements MarketRule {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void calculateTradeAction(MarketChange marketChange) {
		if (marketChange.getVelocity() != null) {
			TradeVelocity velocity = marketChange.getVelocity();
			
			BigDecimal magnitude = velocity.getMagnitude();
			
			BigDecimal buyThreshhold = new BigDecimal(0.6);
			
			if (magnitude.compareTo(buyThreshhold) >= 0) {
				if (velocity.getDirection() == TradeValueDirection.UP) {
					tradeAction = TradeAction.BUY;
				} else if (velocity.getDirection() == TradeValueDirection.DOWN) {
					tradeAction = TradeAction.SELL;
				}
			}
			else if (magnitude.compareTo(buyThreshhold) < 0) {
				tradeAction = TradeAction.HODL;
			}
			
			log.info("VelocityRule: Magnitude is [" + magnitude +"] TradeAction is... " + tradeAction);
		}
		
	}
	
}
