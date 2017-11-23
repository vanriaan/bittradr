package com.paddavoet.bittradr.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.paddavoet.bittradr.integration.request.bitfinex.Order;
import com.paddavoet.bittradr.service.MarketService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
	public String tradeHistory(Model model) {
		String tradeHistory = marketService.getTradeHistory();
		return tradeHistory;
	}

}
