package com.paddavoet.bittradr.entity;

import com.paddavoet.bittradr.market.TradeValueDirection;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "TradeVelocity")
public class TradeVelocity {

	@Id
	private LocalDateTime time;
	private TradeValueDirection direction;
	private BigDecimal magnitude;
	private BigDecimal tradeValue;

	public TradeVelocity() {
		time = LocalDateTime.now();
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

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
