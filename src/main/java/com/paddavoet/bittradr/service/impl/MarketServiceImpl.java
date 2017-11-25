package com.paddavoet.bittradr.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.paddavoet.bittradr.integration.request.bitfinex.PastTrade;
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
	public List<PastTrade> getTradeHistory() {
		return ApplicationConfig.BIT_FIN_EX_API.getTradeHistory();
	}

	public PastTrade getLastBuy() {
		PastTrade lastBuy = new PastTrade();
		lastBuy.setTimestamp("0");
		for (PastTrade trade : getTradeHistory()) {
			if (trade.getType().equalsIgnoreCase("Buy")) {
				if (Double.parseDouble(trade.getTimestamp()) > Double.parseDouble(lastBuy.getTimestamp())) {
					lastBuy = trade;
				}
			}
		}
		return lastBuy;
	}

	public PastTrade getLastSell() {
		PastTrade lastSell = new PastTrade();
		lastSell.setTimestamp("0");
		for (PastTrade trade : getTradeHistory()) {
			if (trade.getType().equalsIgnoreCase("Sell")) {
				if (Double.parseDouble(trade.getTimestamp()) > Double.parseDouble(lastSell.getTimestamp())) {
					lastSell = trade;
				}
			}
		}
		return lastSell;

	}

	@Override
	public PastTrade getLastTransaction() {
		PastTrade lastTrade = new PastTrade();
		lastTrade.setTimestamp("0");
		for (PastTrade trade : getTradeHistory()) {
			if (Double.parseDouble(trade.getTimestamp()) > Double.parseDouble(lastTrade.getTimestamp())) {
				lastTrade = trade;
			}
		}
		return lastTrade;
	}

	@Override
	public BigDecimal getBuySellFee(boolean buy) {
		BigDecimal sellFeePercentageTotal = BigDecimal.ZERO;
		BigDecimal buyFeePercentageTotal = BigDecimal.ZERO;
		int buyCount = 0;
		int sellCount = 0;
		BigDecimal averageFee;

		for (PastTrade trade : getTradeHistory()) {
			BigDecimal value = trade.getAmount().multiply(trade.getPrice());
			BigDecimal feePercentage = trade.getFee().divide(value, 5, BigDecimal.ROUND_HALF_DOWN);

			if (trade.getType().equalsIgnoreCase("buy")) {
				buyCount++;
				buyFeePercentageTotal = buyFeePercentageTotal.add(feePercentage);
			} else {
				sellCount++;
				sellFeePercentageTotal = sellFeePercentageTotal.add(feePercentage);
			}
		}

		if (buy) {
			averageFee = sellFeePercentageTotal.divide(new BigDecimal(buyCount), 5, BigDecimal.ROUND_HALF_DOWN);
		} else {
			averageFee = buyFeePercentageTotal.divide(new BigDecimal(sellCount), 5, BigDecimal.ROUND_HALF_DOWN);
		}

		return averageFee.abs();
	}
}
