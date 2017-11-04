package com.paddavoet.bittradr.fund;

public class VirtualFund implements Fund {
	
	private Currency currency;
	private double amount;
	
	public VirtualFund(Currency currency, Float amount) {
		this.currency = currency;
		this.amount = amount;
	}
	
	@Override
	public Currency getCurrency() {
		return currency;
	}

	@Override
	public double getAmount() {
		return amount;
	}

	@Override
	public void setAmount(double amount) {
		this.amount = amount;
	}

}
