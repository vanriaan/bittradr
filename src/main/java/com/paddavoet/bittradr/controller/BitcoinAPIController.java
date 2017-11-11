package com.paddavoet.bittradr.controller;

import java.math.BigDecimal;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}
