package com.paddavoet.bittradr.profit.calculator;

import java.math.BigDecimal;

public class Profit {
    private boolean buying;
    private BigDecimal buySellUsdAmt;
    private BigDecimal lastBoughtSold;
    private BigDecimal buyingSellingAmt;
    private BigDecimal btcFee;
    private BigDecimal usdFee;
    private BigDecimal btcProfit;
    private BigDecimal usdProfit;

    public boolean isBuying() {
        return buying;
    }

    public void setBuying(boolean buying) {
        this.buying = buying;
    }

    public BigDecimal getBuySellUsdAmt() {
        return buySellUsdAmt;
    }

    public void setBuySellUsdAmt(BigDecimal buySellUsdAmt) {
        this.buySellUsdAmt = buySellUsdAmt;
    }

    public BigDecimal getLastBoughtSold() {
        return lastBoughtSold;
    }

    public void setLastBoughtSold(BigDecimal lastBoughtSold) {
        this.lastBoughtSold = lastBoughtSold;
    }

    public BigDecimal getBuyingSellingAmt() {
        return buyingSellingAmt;
    }

    public void setBuyingSellingAmt(BigDecimal buyingSellingAmt) {
        this.buyingSellingAmt = buyingSellingAmt;
    }

    public BigDecimal getBtcFee() {
        return btcFee;
    }

    public void setBtcFee(BigDecimal btcFee) {
        this.btcFee = btcFee;
    }

    public BigDecimal getUsdFee() {
        return usdFee;
    }

    public void setUsdFee(BigDecimal usdFee) {
        this.usdFee = usdFee;
    }

    public BigDecimal getBtcProfit() {
        return btcProfit;
    }

    public void setBtcProfit(BigDecimal btcProfit) {
        this.btcProfit = btcProfit;
    }

    public BigDecimal getUsdProfit() {
        return usdProfit;
    }

    public void setUsdProfit(BigDecimal usdProfit) {
        this.usdProfit = usdProfit;
    }
}
