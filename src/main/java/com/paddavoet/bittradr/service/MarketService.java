package com.paddavoet.bittradr.service;

import java.math.BigDecimal;
import java.util.List;

import com.paddavoet.bittradr.integration.request.bitfinex.Order;
import com.paddavoet.bittradr.entity.PastTrade;
import com.paddavoet.bittradr.integration.response.bitfinex.QueryMarketResponse;

public interface MarketService {
	
	BigDecimal getCurrentBitcoinPrice();
	
	void handleQueryMarketResponse(QueryMarketResponse response);
	
	List<Order> getOrders();

	List<PastTrade> getTradeHistory();

	PastTrade getLastBuy();

	PastTrade getLastSell();

	PastTrade getLastTransaction();

	BigDecimal getBuySellFee(boolean buy);
}
