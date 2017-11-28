package com.paddavoet.bittradr.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.paddavoet.bittradr.service.MarketService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private MarketService marketService;

	@RequestMapping("/home")
    String home(Model model) {
        //return "Current USD Price for 1 Bitcoin is: $" + MarketObserver.EXCHANGE_RATES.getBitcoinUSDPrice();
    	BigDecimal currentPrice = marketService.getCurrentBitcoinPrice();
    	model.addAttribute("currentPrice", currentPrice);
    	return "home";
    }


}
