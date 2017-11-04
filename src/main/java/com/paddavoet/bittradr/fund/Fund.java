package com.paddavoet.bittradr.fund;

public interface Fund {
	Currency getCurrency();
	double getAmount();
	void setAmount(double amount);
}
