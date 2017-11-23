package com.paddavoet.bittradr.service.impl;

import com.paddavoet.bittradr.service.MarketService;
import com.paddavoet.bittradr.service.ProfitCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;

@Service
public class ProfitCalculateServiceImpl implements ProfitCalculateService {

    @Autowired
    private MarketService marketService;

    private BigDecimal BUY_FEE_PERCENTAGE = new BigDecimal(0.002);
    private BigDecimal SELL_FEE_PERCENTAGE = new BigDecimal(0.002);

    private BigDecimal BUY_VALUE = new BigDecimal(1580.12198);
    private BigDecimal BUY_PRICE = new BigDecimal(8200);
    private BigDecimal SELL_PRICE = new BigDecimal(8300);

    @Override
    public BigDecimal calculateProfit() {
        System.out.println(marketService.getCurrentBitcoinPrice());

        System.out.println("------------------BUY------------------");
        BigDecimal buyAmt = BUY_VALUE.divide(BUY_PRICE);
        BigDecimal buyFee = BUY_VALUE.multiply(BUY_FEE_PERCENTAGE);
        System.out.println("USD: " + BUY_VALUE);
        System.out.println("BTC: " + buyAmt);
        System.out.println("Fee: " + buyFee);
        System.out.println();

        System.out.println("------------------SELL-----------------");
        BigDecimal sellValue = buyAmt.multiply(SELL_PRICE);
        BigDecimal sellFee = sellValue.multiply(SELL_FEE_PERCENTAGE);
        System.out.println("USD: " + sellValue);
        System.out.println("BTC: " + buyAmt);
        System.out.println("Fee: " + sellFee);
        System.out.println();

        System.out.println("-----------------PROFIT----------------");
        BigDecimal profit = sellValue.subtract(BUY_VALUE).subtract(buyFee).subtract(sellFee);
        System.out.println(profit);
        System.out.println("---------------------------------------");
//        return profit;
        throw new NotImplementedException();
    }
}
