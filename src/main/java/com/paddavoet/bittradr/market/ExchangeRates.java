package com.paddavoet.bittradr.market;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paddavoet.bittradr.integration.response.bitfinex.QueryMarketResponse;

public class ExchangeRates {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRates.class);
	
	private BigDecimal bitcoinUSDPrice = BigDecimal.ZERO;
	
	public void updateRates(QueryMarketResponse response) {
		LOGGER.info("updating rates with: " + response.getRawResponse());
		
		setBitcoinUSDPrice(response.getLastPrice());
	}
	
	private void setBitcoinUSDPrice(BigDecimal price) {
		this.bitcoinUSDPrice = price;
		LOGGER.info("BitCoin USD price is now: " + this.bitcoinUSDPrice);
	}
	
	public BigDecimal getBitcoinUSDPrice() {
		return this.bitcoinUSDPrice;
	}
}
