package com.paddavoet.bittradr.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.paddavoet.bittradr.repository.TradeVelocityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paddavoet.bittradr.integration.response.bitfinex.QueryMarketResponse;
import com.paddavoet.bittradr.market.ExchangeRates;
import com.paddavoet.bittradr.market.TradeValueDirection;
import com.paddavoet.bittradr.component.publisher.MarketChangePublisher;
import com.paddavoet.bittradr.entity.MarketChange;
import com.paddavoet.bittradr.entity.TradeVelocity;

/**
 *
 * @author Riaan
 *
 */
@Component
public class MarketObserver {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TradeVelocityRepository tradeVelocityRepository;
	
	private ExchangeRates exchangeRates = new ExchangeRates();
	
	private List<TradeVelocity> velocities = new ArrayList<>();
	
	/**
	 * Takes the {@link QueryMarketResponse} and creates a {@link TradeVelocity} entity to represent the velocity of the change.
	 * @param response
	 * @return returns the velocity created.
	 */
	public TradeVelocity updateVelocities(QueryMarketResponse response) {
		TradeVelocity velocity = new TradeVelocity();
		
		velocity.setTradeValue(response.getLastPrice());

		if (getVelocities().size() == 0) {
			log.debug("creating initial velocity with value {}", velocity.getTradeValue());
			velocity.setDirection(TradeValueDirection.UNCHANGED);
			velocity.setMagnitude(BigDecimal.ZERO);
		} else {
			log.debug("creating another velocity");
			TradeVelocity previous = getVelocities().get(getVelocities().size() -1);
			
			BigDecimal previousValue = previous.getTradeValue();
			log.debug("previous value {}", previousValue);

			BigDecimal currentValue = response.getLastPrice();
			log.debug("current value {}", currentValue);

			BigDecimal absoluteDifference = previousValue.subtract(currentValue).abs();
			log.debug("abs difference {}", absoluteDifference);
			
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

			log.info("Added new Velocity. Direction [{}] and magnitude [{}]", velocity.getDirection(), velocity.getMagnitude());
		}

		if (tradeVelocityRepository != null) {
			tradeVelocityRepository.save(velocity);
		}
		getVelocities().add(velocity);
		
		return velocity;
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
