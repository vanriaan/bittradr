package com.paddavoet.bittradr.fund.manager;

import com.paddavoet.bittradr.fund.BitcoinFund;
import com.paddavoet.bittradr.fund.Fund;

public interface FundManager {
	Fund getCashFund();
	BitcoinFund getBitcoinFund();
}
