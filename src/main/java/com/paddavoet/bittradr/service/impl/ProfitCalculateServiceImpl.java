package com.paddavoet.bittradr.service.impl;

import com.paddavoet.bittradr.entity.PastTradeEntity;
import com.paddavoet.bittradr.profit.calculator.Profit;
import com.paddavoet.bittradr.service.MarketService;
import com.paddavoet.bittradr.service.ProfitCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProfitCalculateServiceImpl implements ProfitCalculateService {

    @Autowired
    private MarketService marketService;

    private BigDecimal BUY_FEE_MULTIPLIER = new BigDecimal(0.001);
    private BigDecimal SELL_FEE_MULTIPLIER = new BigDecimal(0.001);

    private BigDecimal currentBtcPrice;
    private Profit profit;

    @Override
    public boolean isBuying() {
        return marketService.getLastTransaction().getType().equalsIgnoreCase("sell");
    }

    @Override
    public Profit calculateProfit(boolean buying, BigDecimal price) {
        currentBtcPrice = marketService.getCurrentBitcoinPrice();

        BigDecimal profitPrice = currentBtcPrice;
        if (null != price) {
            profitPrice = price;
        }

        if (buying) {
            calculateProfitBuyingBTC(profitPrice);
        } else {
            calculateProfitSellingBTC(profitPrice);
        }

        return profit;
    }

    private void calculateProfitBuyingBTC(BigDecimal currentBtcPrice) {
        profit = null;

        //Payout if buying
        BigDecimal walletValue = marketService.getUsdExchangeBalance();
        BigDecimal walletAmount = walletValue.divide(currentBtcPrice, 10, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal exchangeFee = walletValue.multiply(BUY_FEE_MULTIPLIER);
        BigDecimal exchangePayout = walletValue.subtract(exchangeFee);

        //Profit made after last sell
        PastTradeEntity lastSell = marketService.getLastSell();
        BigDecimal lastSellPrice = lastSell.getPrice();
        BigDecimal lastSellAmount = lastSell.getAmount();
        BigDecimal lastSellPayout = lastSellPrice.multiply(lastSellAmount);
        BigDecimal profitAmount = exchangePayout.subtract(lastSellPayout);

        //When to sell again to break equal
        BigDecimal breakEqualSellFactor = exchangePayout.divide(walletValue, 10, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal breakEqualSellPrice = currentBtcPrice.multiply(breakEqualSellFactor);
        //TODO: fee

        profit = new Profit(
                true,
                currentBtcPrice,
                walletAmount,
                walletValue,
                exchangeFee,
                exchangePayout,
                lastSellPrice,
                lastSellAmount,
                lastSellPayout,
                profitAmount,
                breakEqualSellFactor,
                breakEqualSellPrice
            );
    }

    private void calculateProfitSellingBTC(BigDecimal currentBtcPrice) {
        profit = null;

        //Payout if selling
        BigDecimal walletAmount = marketService.getBtcExchangeBalance();
        BigDecimal walletValue = walletAmount.multiply(currentBtcPrice);
        BigDecimal exchangeFee = walletValue.multiply(SELL_FEE_MULTIPLIER);
        BigDecimal exchangePayout = walletValue.subtract(exchangeFee);

        //Profit made after last buy
        PastTradeEntity lastBuy = marketService.getLastBuy();
        BigDecimal lastBuyPrice = lastBuy.getPrice();
        BigDecimal lastBuyAmount = lastBuy.getAmount();
        BigDecimal lastBuyPayout = lastBuyPrice.multiply(walletAmount);
        BigDecimal profitAmount = exchangePayout.subtract(lastBuyPayout);

        //When to buy again to break equal
        BigDecimal breakEqualBuyFactor = exchangePayout.divide(walletValue, 10, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal breakEqualBuyPrice = currentBtcPrice.multiply(breakEqualBuyFactor);
//        BigDecimal breakEqualOnePercent = currentBtcPrice.divide(new BigDecimal(99.9));
//        BigDecimal breakEqualBuyValue = breakEqualOnePercent.multiply(new BigDecimal(100.0));
//        BigDecimal breakEqualBuyPrice = breakEqualBuyValue.multiply(breakEqualBuyFactor);

        //TODO: fee

        profit = new Profit(
                true,
                currentBtcPrice,
                walletAmount,
                walletValue,
                exchangeFee,
                exchangePayout,
                lastBuyPrice,
                lastBuyAmount,
                lastBuyPayout,
                profitAmount,
                breakEqualBuyFactor,
                breakEqualBuyPrice
        );
    }
}
