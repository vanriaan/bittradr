package com.paddavoet.bittradr.fund;

import java.math.BigDecimal;

public interface Fund {
	Currency getCurrency();
	BigDecimal getAmount();
	void setAmount(BigDecimal amount);
}
