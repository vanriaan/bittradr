package com.paddavoet.bittradr.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paddavoet.bittradr.application.ApplicationConfig;
import com.paddavoet.bittradr.component.MarketObserver;
import com.paddavoet.bittradr.integration.request.bitfinex.Order;
import com.paddavoet.bittradr.integration.response.bitfinex.QueryMarketResponse;
import com.paddavoet.bittradr.service.MarketService;

@Service
public class MarketServiceImpl implements MarketService {

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

	@Override
	public List<Order> getOrders() {
		return ApplicationConfig.BIT_FIN_EX_API.getOrders();
	}

	@Override
	public String getTradeHistory() {
		//TODO: Convert to List of objects
		return ApplicationConfig.BIT_FIN_EX_API.getTradeHistory().toString();
	}
}
