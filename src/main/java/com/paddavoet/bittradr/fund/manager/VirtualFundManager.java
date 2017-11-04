package com.paddavoet.bittradr.fund.manager;

import com.paddavoet.bittradr.fund.BitcoinFund;
import com.paddavoet.bittradr.fund.Fund;

public class VirtualFundManager implements FundManager {
	
	static Fund CASH_FUND;
	static BitcoinFund BITCOIN_FUND;
	
	@Override
	public Fund getCashFund() {
		return CASH_FUND;
	}

	@Override
	public BitcoinFund getBitcoinFund() {
		return BITCOIN_FUND;
	}
}
