package com.paddavoet.bittradr.market;

import java.math.BigDecimal;

public class TradeVelocity {
	
	private TradeValueDirection direction;
	private BigDecimal magnitude;
	private BigDecimal tradeValue;
	
	public TradeValueDirection getDirection() {
		return direction;
	}

	public void setDirection(TradeValueDirection direction) {
		this.direction = direction;
	}

	public BigDecimal getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(BigDecimal magnitude) {
		this.magnitude = magnitude;
	}

	public BigDecimal getTradeValue() {
		return tradeValue;
	}

	public void setTradeValue(BigDecimal tradeValue) {
		this.tradeValue = tradeValue;
	}

}
