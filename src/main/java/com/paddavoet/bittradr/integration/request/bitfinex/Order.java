package com.paddavoet.bittradr.integration.request.bitfinex;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paddavoet.bittradr.fund.Currency;
import com.paddavoet.bittradr.integration.request.bitfinex.enumeration.OrderStatus;
import com.paddavoet.bittradr.integration.request.bitfinex.enumeration.OrderType;
/**
 * 
 * 
 * @author Riaan
 *
 */
public class Order {
	private static final Logger LOG = LoggerFactory.getLogger(Order.class);
	
	private int id;
	private static final String ORDER_ID_JSON_KEY = "id"; 
	
	private int groupId;
	private int clientOrderId;
	private Currency currency;
	private int mtsCreate;
	private int mtsUpdate;
	
	private BigDecimal amount;
	private static final String ORDER_AMOUNT_JSON_KEY = "original_amount";
	
	private BigDecimal originalAmount;
	private OrderType type;
	private OrderStatus orderStatus;
	
	private BigDecimal price;
	private static final String ORDER_PRICE_JSON_KEY = "price";
	
	private BigDecimal averagePrice;
	private BigDecimal trailingPrice;
	private boolean notify;
	private boolean hidden;
	
	private String rawResponse;
	
	public Order(JSONObject json) {
		rawResponse = json.toString();
		
		try {
			id = json.getInt(ORDER_ID_JSON_KEY);
			amount = json.optBigDecimal(ORDER_AMOUNT_JSON_KEY, new BigDecimal(-1)).setScale(5, RoundingMode.HALF_UP);
			price = json.optBigDecimal(ORDER_PRICE_JSON_KEY, new BigDecimal(-1)).setScale(5, RoundingMode.HALF_UP);
		} catch (JSONException je) {
			LOG.error("Could not parse an Order from the json: {}", json.toString());
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getClientOrderId() {
		return clientOrderId;
	}
	public void setClientOrderId(int clientOrderId) {
		this.clientOrderId = clientOrderId;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public int getMtsCreate() {
		return mtsCreate;
	}
	public void setMtsCreate(int mtsCreate) {
		this.mtsCreate = mtsCreate;
	}
	public int getMtsUpdate() {
		return mtsUpdate;
	}
	public void setMtsUpdate(int mtsUpdate) {
		this.mtsUpdate = mtsUpdate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getOriginalAmount() {
		return originalAmount;
	}
	public void setOriginalAmount(BigDecimal originalAmount) {
		this.originalAmount = originalAmount;
	}
	public OrderType getType() {
		return type;
	}
	public void setType(OrderType type) {
		this.type = type;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}
	public BigDecimal getTrailingPrice() {
		return trailingPrice;
	}
	public void setTrailingPrice(BigDecimal trailingPrice) {
		this.trailingPrice = trailingPrice;
	}
	public boolean isNotify() {
		return notify;
	}
	public void setNotify(boolean notify) {
		this.notify = notify;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	
}
