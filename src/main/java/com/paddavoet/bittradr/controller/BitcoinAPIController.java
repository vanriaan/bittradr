package com.paddavoet.bittradr.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.paddavoet.bittradr.entity.BalanceHistoryEntity;
import com.paddavoet.bittradr.entity.PastTradeEntity;
import com.paddavoet.bittradr.entity.WalletBalanceEntity;
import com.paddavoet.bittradr.profit.calculator.Profit;
import com.paddavoet.bittradr.service.ProfitCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.paddavoet.bittradr.integration.request.bitfinex.Order;
import com.paddavoet.bittradr.service.MarketService;

@RestController
@RequestMapping("/api/bitcoin")
public class BitcoinAPIController {

	@Autowired
	private MarketService marketService;

	@Autowired
	private ProfitCalculateService profitCalculateService;

	@RequestMapping(value = "/currentPrice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public String currentPrice(Model model) {

    	BigDecimal currentPrice = marketService.getCurrentBitcoinPrice();
    	return currentPrice.toPlainString();
	}
	
	@RequestMapping(value = "/orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public List<Order> orders(Model model) {
    	List<Order> orders = marketService.getOrders();
		return orders;
	}

	@RequestMapping(value = "/tradeHistory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public List<PastTradeEntity> tradeHistory() {
		List<PastTradeEntity> tradeHistory = marketService.getTradeHistory();
		return tradeHistory;
	}
	@RequestMapping(value = "/walletBalances", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public List<WalletBalanceEntity> walletBalances() {
		List<WalletBalanceEntity> walletBalances = marketService.getWalletBalances();
		return walletBalances;
	}

	@RequestMapping(value = "/balanceHistory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public List<BalanceHistoryEntity> getBalanceHistory(@QueryParam("currency") String currency) {
		List<BalanceHistoryEntity> walletBalances = marketService.getBalanceHistory(currency);
		Collections.reverse(walletBalances);
		List<BalanceHistoryEntity> walletBalancesFinal = new ArrayList<>();
		for (BalanceHistoryEntity entity : walletBalances) {
			if (entity.getCurrency().equalsIgnoreCase("BTC") && entity.getBalance().compareTo(new BigDecimal("0.1")) > 0
					|| entity.getCurrency().equalsIgnoreCase("USD") && entity.getBalance().compareTo(new BigDecimal("1000")) > 0) {
				System.out.println(entity.getCurrency() + " - " + entity.getTimestamp() + " - " + entity.getBalance());
				walletBalancesFinal.add(entity);
			}
		}

		return walletBalancesFinal;
	}

	@RequestMapping(value = "/feePercentage/{buySell}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public String feePercentage(Model model, @PathVariable("buySell") String buySell) {
		BigDecimal buyFeePercentage = marketService.getBuySellFee(true);
		BigDecimal sellFeePercentage = marketService.getBuySellFee(false);

		return "Buy fee: " + buyFeePercentage + " Sell fee: " + sellFeePercentage;
	}

	@RequestMapping(value = "/calculateProfit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	Profit calulateProfit(@QueryParam("price") BigDecimal price){
		boolean buying = profitCalculateService.isBuying();
		String type = "Selling ";
		if (buying) {
			type = "Buying ";
		}

		return profitCalculateService.calculateProfit(buying, price);
	}

}
