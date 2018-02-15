package com.paddavoet.bittradr.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.paddavoet.bittradr.entity.BalanceHistoryEntity;
import com.paddavoet.bittradr.entity.MarketChange;
import com.paddavoet.bittradr.entity.PastTradeEntity;
import com.paddavoet.bittradr.entity.TradeVelocity;
import com.paddavoet.bittradr.entity.WalletBalanceEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paddavoet.bittradr.application.ApplicationConfig;
import com.paddavoet.bittradr.component.MarketObserver;
import com.paddavoet.bittradr.component.publisher.MarketChangePublisher;
import com.paddavoet.bittradr.integration.request.bitfinex.Order;
import com.paddavoet.bittradr.integration.response.bitfinex.QueryMarketResponse;
import com.paddavoet.bittradr.service.MarketService;

@Service
public class MarketServiceImpl implements MarketService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MarketObserver marketObserver;
	
	@Autowired
	private MarketChangePublisher publisher;

	/*
	 * returns the price in USD
	 */
	public BigDecimal getCurrentBitcoinPrice() {
		return marketObserver.getExchangeRates().getBitcoinUSDPrice();
	}

	public void handleQueryMarketResponse(QueryMarketResponse response) {
		marketObserver.getExchangeRates().updateRates(response);
		
		TradeVelocity velocity = marketObserver.updateVelocities(response);
		
		createAndPublishMarketChangeEvent(response, velocity);
	}

	@Override
	public List<Order> getOrders() {
		return ApplicationConfig.BIT_FIN_EX_API.getOrders();
	}

	@Override
	public List<PastTradeEntity> getTradeHistory() {
		return ApplicationConfig.BIT_FIN_EX_API.getTradeHistory();
	}

	@Override
	public List<WalletBalanceEntity> getWalletBalances() {
		return ApplicationConfig.BIT_FIN_EX_API.getWalletBalances();
	}

	@Override
	public List<BalanceHistoryEntity> getBalanceHistory(String currency) {
		return ApplicationConfig.BIT_FIN_EX_API.getBalanceHistory(currency);
	}

	@Override
	public BigDecimal getBtcExchangeBalance() {
		BigDecimal value = BigDecimal.ZERO;
		for (WalletBalanceEntity wallet : ApplicationConfig.BIT_FIN_EX_API.getWalletBalances()) {
			if (wallet.getCurrency().equalsIgnoreCase("btc") && wallet.getType().equalsIgnoreCase("exchange")) {
				value = wallet.getAmount();
			}
		}
		return value;
	}

	@Override
	public BigDecimal getUsdExchangeBalance() {
		BigDecimal value = BigDecimal.ZERO;
		for (WalletBalanceEntity wallet : ApplicationConfig.BIT_FIN_EX_API.getWalletBalances()) {
			if (wallet.getCurrency().equalsIgnoreCase("usd") && wallet.getType().equalsIgnoreCase("exchange")) {
				value = wallet.getAmount();
			}
		}
		return value;
	}

	public PastTradeEntity getLastBuy() {
		PastTradeEntity lastBuy = new PastTradeEntity();
		lastBuy.setTimestamp("0");
		for (PastTradeEntity trade : getTradeHistory()) {
			if (trade.getType().equalsIgnoreCase("Buy")) {
				if (Double.parseDouble(trade.getTimestamp()) > Double.parseDouble(lastBuy.getTimestamp())) {
					lastBuy = trade;
				}
			}
		}
		return lastBuy;
	}

	public PastTradeEntity getLastSell() {
		PastTradeEntity lastSell = new PastTradeEntity();
		lastSell.setTimestamp("0");
		for (PastTradeEntity trade : getTradeHistory()) {
			if (trade.getType().equalsIgnoreCase("Sell")) {
				if (Double.parseDouble(trade.getTimestamp()) > Double.parseDouble(lastSell.getTimestamp())) {
					lastSell = trade;
				}
			}
		}
		return lastSell;

	}

	@Override
	public PastTradeEntity getLastTransaction() {
		PastTradeEntity lastTrade = new PastTradeEntity();
		lastTrade.setTimestamp("0");
		for (PastTradeEntity trade : getTradeHistory()) {
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

		for (PastTradeEntity trade : getTradeHistory()) {
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

	@Override
	public void createAndPublishMarketChangeEvent(QueryMarketResponse marketResponse, TradeVelocity velocity) {
		MarketChange marketChange = new MarketChange(marketResponse, velocity);
		
		try {
			
			publisher.publishChange(marketChange);
			log.info("published market_change event");
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
