package com.paddavoet.bittradr.integration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.paddavoet.bittradr.integration.responses.bitfinex.QueryMarketResponse;

public class BitFinExAPI {
	public static final String API_ROOT = "https://api.bitfinex.com/v1";
	public static final String API_RESOURCE_TICKER = "pubticker/";
	public static final String API_RESOURCE_BITCOIN_USD = "btcusd";
	
	public static final String ENDPOINT_TICKER_BITCOIN_USD = "https://api.bitfinex.com/v1/pubticker/btcusd";
	
	private Client jaxrsClient = ClientBuilder.newClient(); 
	
		
	public QueryMarketResponse queryMarket() {
		
		JSONObject jsonResponse = new JSONObject(jaxrsClient.target(API_ROOT)
	            .path(API_RESOURCE_TICKER + API_RESOURCE_BITCOIN_USD)
	            .request(MediaType.APPLICATION_JSON)
	            .get(String.class));
		
		QueryMarketResponse response = new QueryMarketResponse(jsonResponse); 
		return 	response;
	}
}
