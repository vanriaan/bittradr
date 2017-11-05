package com.paddavoet.bittradr.market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paddavoet.bittradr.integration.responses.bitfinex.QueryMarketResponse;

public class ExchangeRates {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRates.class);
	
	private static double BITCOIN_USD_PRICE = 102205.77;
	
	public static void updateRates(QueryMarketResponse response) {
		LOGGER.info("updating rates with: " + response.getRawResponse());
		
		setBitcoinUSDPrice(response.getLastPrice());
	}
	
	private static void setBitcoinUSDPrice(double price) {
		LOGGER.info("Set BitCoin USD price to: " + price);
		
		ExchangeRates.BITCOIN_USD_PRICE = price;
	}
	
	public static double getBitcoinUSDPrice() {
		return ExchangeRates.BITCOIN_USD_PRICE;
	}
}
