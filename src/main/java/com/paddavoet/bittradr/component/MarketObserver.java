package com.paddavoet.bittradr.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.paddavoet.bittradr.integration.responses.bitfinex.QueryMarketResponse;
import com.paddavoet.bittradr.market.ExchangeRates;
import com.paddavoet.bittradr.market.TradeValueDirection;
import com.paddavoet.bittradr.market.TradeVelocity;

/**
 *
 * @author Riaan
 *
 */
@Component
public class MarketObserver {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MarketObserver.class);
	
	private ExchangeRates exchangeRates = new ExchangeRates();
	
	private List<TradeVelocity> velocities = new ArrayList<>();
	
	public void updateVelocities(QueryMarketResponse response) {
		TradeVelocity velocity = new TradeVelocity();
		
		velocity.setTradeValue(response.getLastPrice());

		if (getVelocities().size() == 0) {
			LOGGER.debug("creating initial velocity with value {}", velocity.getTradeValue());
			velocity.setDirection(TradeValueDirection.UNCHANGED);
			velocity.setMagnitude(BigDecimal.ZERO);
		} else {
			LOGGER.debug("creating another velocity");
			TradeVelocity previous = getVelocities().get(getVelocities().size() -1);
			
			BigDecimal previousValue = previous.getTradeValue();
			LOGGER.debug("previous value {}", previousValue);

			BigDecimal currentValue = response.getLastPrice();
			LOGGER.debug("current value {}", currentValue);

			BigDecimal absoluteDifference = previousValue.subtract(currentValue).abs();
			LOGGER.debug("abs difference {}", absoluteDifference);
			
			if (BigDecimal.ZERO.compareTo(absoluteDifference) != 0) {
				BigDecimal percentageChanged = (absoluteDifference.divide(previousValue, 5, RoundingMode.HALF_UP)); 
				BigDecimal magnitude = percentageChanged.multiply(new BigDecimal(100.00000));
				
				velocity.setMagnitude(magnitude);

				if (previousValue.compareTo(currentValue) == -1) {
					velocity.setDirection(TradeValueDirection.UP);
				} else {
					velocity.setDirection(TradeValueDirection.DOWN);
				}
				
			} else {
				velocity.setMagnitude(BigDecimal.ZERO);
				velocity.setDirection(TradeValueDirection.UNCHANGED);
			}

			LOGGER.info("Added new Velocity. Direction [{}] and magnitude [{}]", velocity.getDirection(), velocity.getMagnitude());
		}
		
		getVelocities().add(velocity);
	}
	
	public ExchangeRates getExchangeRates() {
		return exchangeRates;
	}

	public List<TradeVelocity> getVelocities() {
		return velocities;
	}

	public void setVelocities(List<TradeVelocity> velocities) {
		this.velocities = velocities;
	}
}
