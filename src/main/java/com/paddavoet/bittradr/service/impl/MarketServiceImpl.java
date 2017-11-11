package com.paddavoet.bittradr.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paddavoet.bittradr.component.MarketObserver;
import com.paddavoet.bittradr.integration.responses.bitfinex.QueryMarketResponse;
import com.paddavoet.bittradr.service.MarketService;

@Service
public class MarketServiceImpl implements MarketService{
	
	@Autowired
	private MarketObserver marketObserver;

	/*
	 * returns the price in USD
	 */
	public BigDecimal getCurrentBitcoinPrice() {
		return marketObserver.getExchangeRates().getBitcoinUSDPrice();
	}
	
	public void handleQueryMarketResponse(QueryMarketResponse response) {
		marketObserver.getExchangeRates().updateRates(response);
		marketObserver.updateVelocities(response);
	}
}
