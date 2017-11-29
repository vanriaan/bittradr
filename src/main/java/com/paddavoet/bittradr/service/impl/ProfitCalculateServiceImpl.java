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
        BigDecimal exchangeFee = walletAmount.multiply(BUY_FEE_MULTIPLIER);
        BigDecimal exchangePayout = walletAmount.subtract(exchangeFee);
        exchangePayout = exchangePayout.multiply(currentBtcPrice);


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
                BigDecimal.ZERO,
                BigDecimal.ZERO
            );
    }

    private void calculateProfitSellingBTC(BigDecimal currentBtcPrice) {
        profit = null;

        //Payout if selling
        BigDecimal walletAmount = marketService.getBtcExchangeBalance(); //How much bitcoin in my wallet
        BigDecimal walletValue = walletAmount.multiply(currentBtcPrice); //How much USD that's worth
        BigDecimal exchangeFee = walletValue.multiply(SELL_FEE_MULTIPLIER); //How much I'll lose when selling
        BigDecimal exchangePayout = walletValue.subtract(exchangeFee); //How much I'll get in USD when selling

        //Profit made after last buy
        PastTradeEntity lastBuy = marketService.getLastBuy();
        BigDecimal lastBuyPrice = lastBuy.getPrice(); //What was the price I bought what I have at
        BigDecimal lastBuyAmount = lastBuy.getAmount(); //How much bitcoin I bought (Same as walletAmount)
        BigDecimal lastBuyPayout = lastBuyPrice.multiply(lastBuyAmount); //How much USD was spent to buy it (without fee)
        BigDecimal lastBuyFee = lastBuyPayout.divide(new BigDecimal(99.9), 10 , BigDecimal.ROUND_HALF_UP); //What was the fee buying
        BigDecimal profitAmount = exchangePayout.subtract(lastBuyPayout.add(lastBuyFee)); //How much profit from USD->BTC->USD

        //At what price do I need to buy BTC so my BTC value is the same as what I have now (subtracting fees)
        //walletAmount - trade to USD fee - trade to btc fee
        BigDecimal tempOne = walletValue.subtract(exchangeFee); //Subtract sell fee
        BigDecimal buyAgainFee = tempOne.multiply(BUY_FEE_MULTIPLIER); //Subtract buy fee
        BigDecimal tempTwo = tempOne.subtract(buyAgainFee);
        BigDecimal buyPrice = currentBtcPrice;
        BigDecimal buyAmount = tempTwo.divide(buyPrice, 10, BigDecimal.ROUND_HALF_DOWN);//How much btc does that buy me
        BigDecimal buyAtBuyPriceDifference = buyAmount.subtract(walletAmount);//How much do I lost when buying at the same price again

        //Reduce buy price until breaking equal
        while (buyAtBuyPriceDifference.compareTo(BigDecimal.ZERO) == -1) {
            //While losing money, drop BTC price by $1
            buyPrice = buyPrice.subtract(BigDecimal.ONE);
            buyAmount = tempTwo.divide(buyPrice, 10, BigDecimal.ROUND_HALF_DOWN);
            buyAtBuyPriceDifference = buyAmount.subtract(walletAmount);
        }

        profit = new Profit(
                false,
                currentBtcPrice,
                walletAmount,
                walletValue,
                exchangeFee,
                exchangePayout,
                lastBuyPrice,
                lastBuyAmount,
                lastBuyPayout,
                profitAmount,
                buyAmount,
                buyPrice
        );
    }
}
