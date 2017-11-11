package com.paddavoet.bittradr.fund;

import java.math.BigDecimal;

public class VirtualFund implements Fund {
	
	private Currency currency;
	private BigDecimal amount;
	
	public VirtualFund(Currency currency, BigDecimal amount) {
		this.currency = currency;
		this.amount = amount;
	}
	
	@Override
	public Currency getCurrency() {
		return currency;
	}

	@Override
	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
