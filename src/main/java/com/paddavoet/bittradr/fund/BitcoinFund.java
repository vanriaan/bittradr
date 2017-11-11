package com.paddavoet.bittradr.fund;

import java.math.BigDecimal;

public class BitcoinFund implements Fund {
	
	private BigDecimal amount;
	
	@Override
	public Currency getCurrency() {
		return Currency.BITCOIN;
	}

	@Override
	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal addBitcoin(BigDecimal amount)
	{
		this.amount.add(amount);
		return this.amount;
	}

}
