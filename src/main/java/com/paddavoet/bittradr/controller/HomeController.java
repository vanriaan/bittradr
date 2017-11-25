package com.paddavoet.bittradr.controller;

import java.math.BigDecimal;
import java.security.Principal;

import com.paddavoet.bittradr.service.ProfitCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.paddavoet.bittradr.application.ApplicationConfig;
import com.paddavoet.bittradr.service.MarketService;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.QueryParam;
import javax.xml.ws.Response;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private MarketService marketService;

	@Autowired
	private ProfitCalculateService profitCalculateService;
	
    @RequestMapping("/home")
    String home(Model model) {
        //return "Current USD Price for 1 Bitcoin is: $" + MarketObserver.EXCHANGE_RATES.getBitcoinUSDPrice();
    	BigDecimal currentPrice = marketService.getCurrentBitcoinPrice();
    	model.addAttribute("currentPrice", currentPrice);
    	return "home";
    }

	@RequestMapping("/calculateProfit")
	@ResponseBody
	String calulateProfit(Model model){
    	boolean buying = profitCalculateService.isBuying();
    	String type = "Selling ";
    	if (buying) {
    		type = "Buying ";
		}

		return type + profitCalculateService.calculateProfit(buying).toPlainString();
	}
}
