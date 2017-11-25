package com.paddavoet.bittradr.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.paddavoet.bittradr.entity.PastTradeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.paddavoet.bittradr.integration.request.bitfinex.Order;
import com.paddavoet.bittradr.service.MarketService;

@RestController
@RequestMapping("/api/bitcoin")
public class BitcoinAPIController {

	@Autowired
	private MarketService marketService;

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
//		String userEmail = ((LinkedHashMap<String, String>) ((OAuth2Authentication) principal).getUserAuthentication().getDetails()).get("email");
		List<PastTradeEntity> tradeHistory = marketService.getTradeHistory();
		return tradeHistory;
	}

	@RequestMapping(value = "/feePercentage/{buySell}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public String feePercentage(Model model, @PathVariable("buySell") String buySell) {
		BigDecimal buyFeePercentage = marketService.getBuySellFee(true);
		BigDecimal sellFeePercentage = marketService.getBuySellFee(false);

		return "Buy fee: " + buyFeePercentage + " Sell fee: " + sellFeePercentage;
	}

}
