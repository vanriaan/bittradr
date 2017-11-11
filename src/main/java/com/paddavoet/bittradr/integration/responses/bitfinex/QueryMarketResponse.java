package com.paddavoet.bittradr.integration.responses.bitfinex;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryMarketResponse {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryMarketResponse.class);
	
	private BigDecimal lastPrice;
	private static final String LAST_PRICE_JSON_KEY = "last_price";
	private String rawResponse;
	
	// Example response{"mid":"7345.4","bid":"7345.0","ask":"7345.8","last_price":"7345.8","low":"6930.1","high":"7419.0","volume":"49201.27359462","timestamp":"1509819907.68831"}
	public QueryMarketResponse(JSONObject response) {
		rawResponse = response.toString();
		try {
			lastPrice = response.optBigDecimal(LAST_PRICE_JSON_KEY, new BigDecimal(-1)).setScale(5, RoundingMode.HALF_UP);
		} catch (JSONException je) {
			LOGGER.error("Could not get the BigDecimal value from the JSON response");
		}
	}
	
	public BigDecimal getLastPrice() {
		return lastPrice;
	}
	
	public String getRawResponse() {
		return rawResponse;
	}
}
