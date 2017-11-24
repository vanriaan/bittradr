package com.paddavoet.bittradr.integration.request.bitfinex;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paddavoet.bittradr.fund.Currency;

import java.math.BigDecimal;

public class PastTrade {
    private BigDecimal amount;
    @JsonProperty("fee_amount")
    private BigDecimal fee;
    private BigDecimal price;
    @JsonProperty("fee_currency")
    private Currency feeCurrency;
    private String type;
    @JsonProperty("order_id")
    private long orderId;
    private long tid;
    private String timestamp;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getFeeCurrency() {
        return feeCurrency;
    }

    public void setFeeCurrency(Currency feeCurrency) {
        this.feeCurrency = feeCurrency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
