package com.paddavoet.bittradr.service.impl;

import com.paddavoet.bittradr.fund.Currency;
import com.paddavoet.bittradr.integration.request.bitfinex.PastTrade;
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

    private BigDecimal BUY_FEE_PERCENTAGE = new BigDecimal(0.00200);
    private BigDecimal SELL_FEE_PERCENTAGE = new BigDecimal(0.00050);

    private BigDecimal currentBtcPrice;

    @Override
    public boolean isBuying() {
        return marketService.getLastTransaction().getType().equalsIgnoreCase("sell");
    }

    @Override
    public BigDecimal calculateProfit(boolean buying) {
        currentBtcPrice = marketService.getCurrentBitcoinPrice();
        if (buying) {
            return calculateProfitBuyingBTC();
        }
        return calculateProfitSellingBTC();
    }

    private BigDecimal calculateProfitBuyingBTC() {
        PastTrade lastSell = marketService.getLastSell();

        //Last sold BTC
        BigDecimal btcLastSold = lastSell.getAmount();
        //How much BTC will that buy me now
        BigDecimal lastSoldUsdAmount = btcLastSold.multiply(lastSell.getPrice());
        BigDecimal buyNowBtcValue = lastSoldUsdAmount.divide(currentBtcPrice, 5, RoundingMode.HALF_DOWN);

        BigDecimal btcProfitWithoutFee = buyNowBtcValue.subtract(btcLastSold);
        BigDecimal buyFeeBtc = btcLastSold.multiply(BUY_FEE_PERCENTAGE);
        BigDecimal profitBtc = btcProfitWithoutFee.subtract(buyFeeBtc);
        BigDecimal profitUsd = profitBtc.multiply(currentBtcPrice);

        System.out.println("------------------BUY BTC-----------------");
        System.out.println("Buying BTC for $ " + lastSoldUsdAmount);
        System.out.println("BTC sold: " + btcLastSold);
        System.out.println("BTC buying: " + buyNowBtcValue);
        System.out.println("BTC fee: " + buyFeeBtc);
        System.out.println("BTC profit: " + profitBtc);
        System.out.println("USD profit: " + profitUsd);
        System.out.println();

        return profitUsd;
    }

    private BigDecimal calculateProfitSellingBTC() {
        PastTrade lastBuy = marketService.getLastBuy();

        //Last bought BTC
        BigDecimal btcLastBought = lastBuy.getAmount();
        //How much USD will that get me now
        BigDecimal lastBoughtUsdAmount = btcLastBought.multiply(lastBuy.getPrice());

        BigDecimal sellNowUsdValue = btcLastBought.multiply(currentBtcPrice);

        BigDecimal usdProfitWithoutFee = sellNowUsdValue.subtract(lastBoughtUsdAmount);
        BigDecimal sellFeeUsd = lastBoughtUsdAmount.multiply(SELL_FEE_PERCENTAGE);
        BigDecimal profitUsd = usdProfitWithoutFee.subtract(sellFeeUsd);

        System.out.println("------------------BUY BTC-----------------");
        System.out.println("Selling BTC for $ " + lastBoughtUsdAmount);
        System.out.println("USD bought: " + lastBoughtUsdAmount);
        System.out.println("USD selling: " + sellNowUsdValue);
        System.out.println("USD fee: " + sellFeeUsd);
        System.out.println("USD profit: " + profitUsd);
        System.out.println();

        return profitUsd;
    }
}
