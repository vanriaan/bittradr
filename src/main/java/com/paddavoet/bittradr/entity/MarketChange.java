package com.paddavoet.bittradr.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.paddavoet.bittradr.integration.response.bitfinex.QueryMarketResponse;

@Document(collection = "MarketChange")
public class MarketChange {
	@Id
	private LocalDateTime time;
	
	private QueryMarketResponse marketResponse;
	private TradeVelocity velocity;
	
	public MarketChange(QueryMarketResponse marketResponse, TradeVelocity velocity) {
		this.marketResponse = marketResponse;
		this.velocity = velocity;
		this.time = velocity.getTime();
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public QueryMarketResponse getMarketResponse() {
		return marketResponse;
	}

	public void setMarketResponse(QueryMarketResponse marketResponse) {
		this.marketResponse = marketResponse;
	}

	public TradeVelocity getVelocity() {
		return velocity;
	}

	public void setVelocity(TradeVelocity velocity) {
		this.velocity = velocity;
	}

}
