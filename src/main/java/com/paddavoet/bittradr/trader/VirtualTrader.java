package com.paddavoet.bittradr.trader;

import com.paddavoet.bittradr.fund.manager.FundManager;
import com.paddavoet.bittradr.market.ExchangeRates;

public class VirtualTrader implements Trader {

	private TraderStatus status = TraderStatus.OBSERVING;
	private FundManager fundManager;
	
	@Override
	public void buy() {
		if (fundManager.getCashFund().getAmount() > 0) {
			
			status = TraderStatus.BUYING;
			double spending = fundManager.getCashFund().getAmount();
			double bitcoinBought = spending/ExchangeRates.getBitcoinUSDPrice();
			
			fundManager.getBitcoinFund().addBitcoin(bitcoinBought);
			fundManager.getCashFund().setAmount(0);

		}
		status = TraderStatus.OBSERVING;
	}

	@Override
	public void sell() {
		if (fundManager.getBitcoinFund().getAmount() > 0) {
			
			status = TraderStatus.SELLING;
			
			double selling = fundManager.getBitcoinFund().getAmount();
			double bitcoinSoldFor = selling*ExchangeRates.getBitcoinUSDPrice();
			
			fundManager.getBitcoinFund().setAmount(0);
			fundManager.getCashFund().setAmount(fundManager.getCashFund().getAmount() + bitcoinSoldFor);
		}
		status = TraderStatus.OBSERVING;
	}

	@Override
	public TraderStatus getStatus() {
		return status;
	}

}
