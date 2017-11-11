package com.paddavoet.bittradr.service;

import java.math.BigDecimal;

import com.paddavoet.bittradr.integration.responses.bitfinex.QueryMarketResponse;

public interface MarketService {
	
	BigDecimal getCurrentBitcoinPrice();
	
	void handleQueryMarketResponse(QueryMarketResponse response);
	
}
