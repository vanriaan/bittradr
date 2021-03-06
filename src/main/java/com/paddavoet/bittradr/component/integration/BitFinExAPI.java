package com.paddavoet.bittradr.component.integration;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paddavoet.bittradr.application.GlobalProperties;
import com.paddavoet.bittradr.entity.BalanceHistoryEntity;
import com.paddavoet.bittradr.entity.PastTradeEntity;
import com.paddavoet.bittradr.entity.WalletBalanceEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.paddavoet.bittradr.integration.request.bitfinex.Order;
import com.paddavoet.bittradr.integration.response.bitfinex.QueryMarketResponse;

@Component
public class BitFinExAPI {
	private static final Logger LOGGER = LoggerFactory.getLogger(BitFinExAPI.class);
	private static final String TRADING_PAIR = "BTCUSD";

	private static final ObjectMapper mapper = new ObjectMapper();

	private String API_KEY;
	private String API_SECRET;
	private long nonce = System.currentTimeMillis();

	private static final String ALGORITHM_HMACSHA384 = "HmacSHA384";

	public static final String API_ROOT = "https://api.bitfinex.com/v1";
	public static final String API_RESOURCE_TICKER = "pubticker/";
	public static final String API_RESOURCE_ORDERS = "orders";
	public static final String API_RESOURCE_PAST_TRADES = "mytrades";
	public static final String API_RESOURCE_BALANCE_HISTORY = "history";
	public static final String API_RESOURCE_WALLET_BALANCES = "balances";
	public static final String API_RESOURCE_BITCOIN_USD = "btcusd";

	private Client jaxrsClient = ClientBuilder.newClient();

	public BitFinExAPI() {
		this.API_KEY = GlobalProperties.BIT_FIN_EX_API_KEY;
		this.API_SECRET = GlobalProperties.BIT_FIN_EX_API_SECRET;
	}
	
	public QueryMarketResponse queryMarket() {

		JSONObject jsonResponse = new JSONObject(
				jaxrsClient.target(API_ROOT).path(API_RESOURCE_TICKER + API_RESOURCE_BITCOIN_USD)
						.request(MediaType.APPLICATION_JSON).get(String.class));

		QueryMarketResponse response = new QueryMarketResponse(jsonResponse);
		return response;
	}

	public List<Order> getOrders() {
		List<Order> orders = new ArrayList<>();
		
		if (StringUtils.isEmpty(API_KEY) || StringUtils.isEmpty(API_SECRET)) {
			LOGGER.error(
					"This is an Authenticated API call, API Key and API Secret are required. None were found in configuration!");
		}

		JSONObject jsonResponse = doApiCall(API_RESOURCE_ORDERS, "POST", "orders");

		if (jsonResponse != null) {
			LOGGER.info(jsonResponse.toString());
			JSONArray jsonOrders = jsonResponse.getJSONArray("orders");
			
			if (jsonOrders.length() > 0) {
				for(int i = 0; i < jsonOrders.length(); i++)
				{
					Order order = new Order(jsonOrders.getJSONObject(i));
					orders.add(order);
				}
			}
		}

		return orders;
	}

	/**
	 * Get the trade history for the current account
	 * @return List<PastTradeEntity>
	 */
	public List<PastTradeEntity> getTradeHistory() {
		List<PastTradeEntity> pastTradeEntities = new ArrayList<>();

		if (StringUtils.isEmpty(API_KEY) || StringUtils.isEmpty(API_SECRET)) {
			LOGGER.error(
					"This is an Authenticated API call, API Key and API Secret are required. None were found in configuration!");
		}

		JSONObject jsonResponse = doApiCall(API_RESOURCE_PAST_TRADES, "POST", "myTrades");

		if (jsonResponse != null) {
//			LOGGER.info(jsonResponse.toString());
			JSONArray jsonOrders = jsonResponse.getJSONArray("myTrades");

			if (jsonOrders.length() > 0) {
				for(int i = 0; i < jsonOrders.length(); i++)
				{
					try {
						PastTradeEntity pastTradeEntity = mapper.readValue(jsonOrders.getJSONObject(i).toString(), PastTradeEntity.class);
						pastTradeEntities.add(pastTradeEntity);
					} catch (IOException e) {
						LOGGER.error("Unable to unmarshall " + jsonOrders.getJSONObject(i).toString(), e);
					}
				}
			}
		}

		return pastTradeEntities;
	}

	/**
	 * Get the trade history for the current account
	 * @return List<PastTradeEntity>
	 */
	public List<WalletBalanceEntity> getWalletBalances() {
		List<WalletBalanceEntity> walletBalances = new ArrayList<>();

		if (StringUtils.isEmpty(API_KEY) || StringUtils.isEmpty(API_SECRET)) {
			LOGGER.error(
					"This is an Authenticated API call, API Key and API Secret are required. None were found in configuration!");
		}

		JSONObject jsonResponse = doApiCall(API_RESOURCE_WALLET_BALANCES, "POST", "balances");

		if (jsonResponse != null) {
			LOGGER.info(jsonResponse.toString());
			JSONArray jsonBalances = jsonResponse.getJSONArray("balances");

			if (jsonBalances.length() > 0) {
				for(int i = 0; i < jsonBalances.length(); i++)
				{
					System.out.println(jsonBalances.getJSONObject(i));
					try {
						WalletBalanceEntity walletBalanceEntity = mapper.readValue(jsonBalances.getJSONObject(i).toString(), WalletBalanceEntity.class);
						walletBalances.add(walletBalanceEntity);
					} catch (IOException e) {
						LOGGER.error("Unable to unmarshall " + jsonBalances.getJSONObject(i).toString(), e);
					}
				}
			}
		}

		return walletBalances;
	}

	/**
	 * Get the trade history for the current account
	 * @return List<Object>
	 */
	public List<BalanceHistoryEntity> getBalanceHistory(String currency) {
		String arrayWrapper = "history";
		List<BalanceHistoryEntity> histories = new ArrayList<>();

		if (StringUtils.isEmpty(API_KEY) || StringUtils.isEmpty(API_SECRET)) {
			LOGGER.error(
					"This is an Authenticated API call, API Key and API Secret are required. None were found in configuration!");
		}
		Map<String, String> customHeaderValues = new HashMap<>();
		customHeaderValues.put("currency", currency);

		JSONObject jsonResponse = doApiCall(API_RESOURCE_BALANCE_HISTORY, "POST", arrayWrapper, customHeaderValues);

		if (jsonResponse != null) {
//			LOGGER.info(jsonResponse.toString());
			JSONArray jsonBalances = jsonResponse.getJSONArray(arrayWrapper);

			if (jsonBalances.length() > 0) {
				for(int i = 0; i < jsonBalances.length(); i++)
				{
					System.out.println(jsonBalances.getJSONObject(i));
					try {
						BalanceHistoryEntity walletBalanceEntity = mapper.readValue(jsonBalances.getJSONObject(i).toString(), BalanceHistoryEntity.class);
						histories.add(walletBalanceEntity);
					} catch (IOException e) {
						LOGGER.error("Unable to unmarshall " + jsonBalances.getJSONObject(i).toString(), e);
					}
				}
			}
		}

		return histories;
	}


	/**
	 * Called if an IOException occurred during URL call to avoid error handling duplication
	 * @param conn
	 * @param e
	 */
	private void ioExceptionOccurred(HttpURLConnection conn, IOException e) {
		String errMsg = e.getLocalizedMessage();

		if (conn != null) {
            try {
                errMsg += " -> " + convertStreamToString(conn.getErrorStream());
                LOGGER.error(errMsg, e);
            } catch (IOException e1) {
                errMsg += " Error on reading error-stream. -> " + e1.getLocalizedMessage();
                LOGGER.error(errMsg, e);
            }
        } else {
            LOGGER.error(e.getMessage());
        }
	}

	/**
	 * This will take the {@link HttpURLConnection} and add the necessary headers for an authenticated API call to BitFinEx. The urlPath parameter is required, as it is used in the encoded payload generation.
	 * 
	 * @param conn the {@link HttpURLConnection} to add the authentication headers to.
	 * @param urlPath the relative path on the BitFinex API that you are accessing. This is required, as it is used in the payload generation which is added as one of the authentication headers. A simple example of what the relative urlPath would look like is: '/v1/orders'
	 * @param customHeaderValues a map of any custom values to send through in the header
	 */
	private void addAuthenticationHeaders(HttpURLConnection conn, String urlPath, Map<String, String> customHeaderValues) {
		JSONObject jo = new JSONObject();
		jo.put("request", urlPath);
		jo.put("nonce", Long.toString(getNonce()));
		jo.put("symbol", TRADING_PAIR);
		if (null != customHeaderValues) {
			for (Map.Entry<String, String> custemHeaderValue : customHeaderValues.entrySet()) {
				jo.put(custemHeaderValue.getKey(), custemHeaderValue.getValue());
			}
		}


		// API v1
		String payload = jo.toString();
		String payload_base64 = Base64.getEncoder().encodeToString(payload.getBytes());

		String payload_sha384hmac = hmacDigest(payload_base64, API_SECRET, ALGORITHM_HMACSHA384);

		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.addRequestProperty("X-BFX-APIKEY", API_KEY);
		conn.addRequestProperty("X-BFX-PAYLOAD", payload_base64);
		conn.addRequestProperty("X-BFX-SIGNATURE", payload_sha384hmac);
	}
	
	private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

	private static String hmacDigest(String msg, String keyString, String algo) {
		String digest = null;
		try {
			SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
			Mac mac = Mac.getInstance(algo);
			mac.init(key);

			byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

			StringBuffer hash = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			digest = hash.toString();
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage());
		} catch (InvalidKeyException e) {
			LOGGER.error(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		}
		return digest;
	}

	private long getNonce() {
		return ++nonce;
	}

	private JSONObject doApiCall(String apiPath, String method, String arrayWrapper) {
		return doApiCall(apiPath, method, arrayWrapper, null);
	}

	private JSONObject doApiCall(String apiPath, String method, String arrayWrapper, Map<String, String> customHeaderValues) {
		JSONObject jsonResponse = null;
		String urlPath = "/v1/" + apiPath;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(API_ROOT + "/" + apiPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);

			conn.setDoOutput(true);
			conn.setDoInput(true);

			addAuthenticationHeaders(conn, urlPath, customHeaderValues);

			// read the response
			InputStream in = new BufferedInputStream(conn.getInputStream());
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder("{\"" + arrayWrapper + "\":");

			String inputStr;
			while ((inputStr = streamReader.readLine()) != null) {
				responseStrBuilder.append(inputStr);
			}

			jsonResponse = new JSONObject(responseStrBuilder.append("}").toString());

		} catch (MalformedURLException e) {
			LOGGER.error(e.getMessage());
		} catch (ProtocolException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			ioExceptionOccurred(conn, e);
		} catch (JSONException e) {
			String msg = "Error on setting up the connection to server";
			LOGGER.error(msg, e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return jsonResponse;
	}

}
