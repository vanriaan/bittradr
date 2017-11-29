package com.paddavoet.bittradr.profit.calculator;

import java.math.BigDecimal;

public class Profit {
    private boolean buying;
    private BigDecimal currentPrice;
    private BigDecimal walletAmount;
    private BigDecimal walletValue;
    private BigDecimal exchangeFee;
    private BigDecimal exchangePayout;
    private BigDecimal lastPrice;
    private BigDecimal lastAmount;
    private BigDecimal lastPayout;
    private BigDecimal profitAmount;
    private BigDecimal breakEqualAmount;
    private BigDecimal breakEqualPrice;

    public Profit(boolean buying, BigDecimal currentPrice, BigDecimal walletAmount, BigDecimal walletValue, BigDecimal exchangeFee, BigDecimal exchangePayout, BigDecimal lastPrice, BigDecimal lastAmount, BigDecimal lastPayout, BigDecimal profitAmount, BigDecimal breakEqualAmount, BigDecimal breakEqualPrice) {
        this.buying = buying;
        this.currentPrice = currentPrice;
        this.walletAmount = walletAmount;
        this.walletValue = walletValue;
        this.exchangeFee = exchangeFee;
        this.exchangePayout = exchangePayout;
        this.lastPrice = lastPrice;
        this.lastAmount = lastAmount;
        this.lastPayout = lastPayout;
        this.profitAmount = profitAmount;
        this.breakEqualAmount = breakEqualAmount;
        this.breakEqualPrice = breakEqualPrice;
    }

    public boolean isBuying() {
        return buying;
    }

    public void setBuying(boolean buying) {
        this.buying = buying;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(BigDecimal walletAmount) {
        this.walletAmount = walletAmount;
    }

    public BigDecimal getWalletValue() {
        return walletValue;
    }

    public void setWalletValue(BigDecimal walletValue) {
        this.walletValue = walletValue;
    }

    public BigDecimal getExchangeFee() {
        return exchangeFee;
    }

    public void setExchangeFee(BigDecimal exchangeFee) {
        this.exchangeFee = exchangeFee;
    }

    public BigDecimal getExchangePayout() {
        return exchangePayout;
    }

    public void setExchangePayout(BigDecimal exchangePayout) {
        this.exchangePayout = exchangePayout;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public BigDecimal getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(BigDecimal lastAmount) {
        this.lastAmount = lastAmount;
    }

    public BigDecimal getLastPayout() {
        return lastPayout;
    }

    public void setLastPayout(BigDecimal lastPayout) {
        this.lastPayout = lastPayout;
    }

    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }

    public BigDecimal getBreakEqualAmount() {
        return breakEqualAmount;
    }

    public void setBreakEqualAmount(BigDecimal breakEqualAmount) {
        this.breakEqualAmount = breakEqualAmount;
    }

    public BigDecimal getBreakEqualPrice() {
        return breakEqualPrice;
    }

    public void setBreakEqualPrice(BigDecimal breakEqualPrice) {
        this.breakEqualPrice = breakEqualPrice;
    }
}
