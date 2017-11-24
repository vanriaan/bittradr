package com.paddavoet.bittradr.fund;

public enum Currency {
	BTC("BTC", "BC"),
	ZAR("ZAR", "R"),
	USD("USD", "$");
	
	private String code;
	private String symbol;
	
	private Currency(String code, String symbol) {
		this.code = code;
		this.symbol = symbol;
	}

	public String getCode() {
		return code;
	}

	public String getSymbol() {
		return symbol;
	}
	
	
}
