package com.paddavoet.bittradr.integration.responses.bitfinex;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

public class QueryMarketResponse {
	
	private float lastPrice;
	private static final String LAST_PRICE_JSON_KEY = "last_price";
	private String rawResponse;
	
	// Example response{"mid":"7345.4","bid":"7345.0","ask":"7345.8","last_price":"7345.8","low":"6930.1","high":"7419.0","volume":"49201.27359462","timestamp":"1509819907.68831"}
	public QueryMarketResponse(JSONObject response) {
		rawResponse = response.toString();
		String last_price = response.getString(LAST_PRICE_JSON_KEY);
				
		if (StringUtils.isNotEmpty(last_price)) {
			lastPrice = Float.parseFloat(last_price);
		}
	}
	
	public float getLastPrice() {
		return lastPrice;
	}
	
	public String getRawResponse() {
		return rawResponse;
	}
}
