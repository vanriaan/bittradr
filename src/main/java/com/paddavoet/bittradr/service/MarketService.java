package com.paddavoet.bittradr.service;

import java.math.BigDecimal;
import java.util.List;

import com.paddavoet.bittradr.integration.request.bitfinex.Order;
import com.paddavoet.bittradr.entity.PastTradeEntity;
import com.paddavoet.bittradr.integration.response.bitfinex.QueryMarketResponse;

public interface MarketService {
	
	BigDecimal getCurrentBitcoinPrice();
	
	void handleQueryMarketResponse(QueryMarketResponse response);
	
	List<Order> getOrders();

	List<PastTradeEntity> getTradeHistory();

	PastTradeEntity getLastBuy();

	PastTradeEntity getLastSell();

	PastTradeEntity getLastTransaction();

	BigDecimal getBuySellFee(boolean buy);
}
