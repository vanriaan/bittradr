package com.paddavoet.bittradr.fund;

public class BitcoinFund implements Fund {
	
	private double amount;
	
	@Override
	public Currency getCurrency() {
		return Currency.BITCOIN;
	}

	@Override
	public double getAmount() {
		return amount;
	}

	@Override
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double addBitcoin(double amount)
	{
		this.amount += amount;
		return this.amount;
	}

}
