package com.paddavoet.bittradr.trader;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.paddavoet.bittradr.fund.manager.FundManager;
import com.paddavoet.bittradr.service.MarketService;

@Component
public class VirtualTrader implements Trader {

	private TraderStatus status = TraderStatus.OBSERVING;
	private FundManager fundManager;
	private MarketService marketService;
	
	public VirtualTrader(MarketService marketService) {
		this.marketService = marketService;
	}
	
	@Override
	public void buy() {
		if (fundManager.getCashFund().getAmount().doubleValue() > 0) {
			
			status = TraderStatus.BUYING;
			BigDecimal spending = fundManager.getCashFund().getAmount();
			BigDecimal bitcoinBought = spending.divide(marketService.getCurrentBitcoinPrice());
			
			fundManager.getBitcoinFund().addBitcoin(bitcoinBought);
			fundManager.getCashFund().setAmount(BigDecimal.ZERO);

		}
		status = TraderStatus.OBSERVING;
	}

	@Override
	public void sell() {
		if (fundManager.getBitcoinFund().getAmount().doubleValue() > 0) {
			
			status = TraderStatus.SELLING;
			
			BigDecimal selling = fundManager.getBitcoinFund().getAmount();
			BigDecimal bitcoinSoldFor = selling.multiply(marketService.getCurrentBitcoinPrice());
			
			fundManager.getBitcoinFund().setAmount(BigDecimal.ZERO);
			
			fundManager.getCashFund().setAmount(fundManager.getCashFund().getAmount().add(bitcoinSoldFor));
		}
		status = TraderStatus.OBSERVING;
	}

	@Override
	public TraderStatus getStatus() {
		return status;
	}

}
