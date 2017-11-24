package com.paddavoet.bittradr.service.impl;

import com.paddavoet.bittradr.fund.Currency;
import com.paddavoet.bittradr.service.MarketService;
import com.paddavoet.bittradr.service.ProfitCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ProfitCalculateServiceImpl implements ProfitCalculateService {

    @Autowired
    private MarketService marketService;

    private BigDecimal BUY_FEE_PERCENTAGE = new BigDecimal(0.002);
    private BigDecimal SELL_FEE_PERCENTAGE = new BigDecimal(0.001);

    private BigDecimal BUY_VALUE = new BigDecimal(1589.45);
    private BigDecimal BUY_PRICE = new BigDecimal(8300);

    private BigDecimal currentBtcPrice;

    @Override
    public BigDecimal calculateProfit(boolean buying) {
        currentBtcPrice = marketService.getCurrentBitcoinPrice();
        if (buying) {
            return calculateProfitBuyingBTC();
        }
        return calculateProfitSellingBTC();
    }

    private BigDecimal calculateProfitBuyingBTC() {
        //BTC value before selling - BTC value now buying - fee
        BigDecimal btcLastSold = new BigDecimal(0.19);
        BigDecimal buyUsdAmount = new BigDecimal(1588.43);

        BigDecimal buyBtcAmt = buyUsdAmount.divide(currentBtcPrice, 6, RoundingMode.HALF_UP);
        BigDecimal buyFeeBtc = buyUsdAmount.multiply(BUY_FEE_PERCENTAGE).divide(currentBtcPrice, 6, RoundingMode.HALF_UP);
        BigDecimal profitBtc = buyBtcAmt.subtract(buyFeeBtc).subtract(btcLastSold);
        BigDecimal profitUsd = profitBtc.multiply(currentBtcPrice);
        System.out.println("------------------SELL-----------------");
        System.out.println("Buying BTC for $ " + buyUsdAmount);
        System.out.println("BTC sold: " + btcLastSold);
        System.out.println("BTC value: " + buyBtcAmt);
        System.out.println("BTC fee: " + buyFeeBtc);
        System.out.println("BTC profit: " + profitBtc);
        System.out.println("USD profit: " + profitUsd);
        System.out.println();

        return profitUsd;
    }

    private BigDecimal calculateProfitSellingBTC() {
        System.out.println("------------------SELL BTC-----------------");
//        System.out.println("Selling BTC " + sellBtcAmount);
//        BigDecimal sellUsdAmount = sellBtcAmount.multiply(currentBtcPrice);
//        System.out.println("USD Value: " + sellUsdAmount);
//        BigDecimal buyFeeUsd = sellUsdAmount.multiply(SELL_FEE_PERCENTAGE);
//        System.out.println("USD fee: " + buyFeeUsd);

        return BigDecimal.ZERO;
    }
}
