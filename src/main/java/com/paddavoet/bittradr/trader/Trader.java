package com.paddavoet.bittradr.trader;

public interface Trader {
	void buy();
	void sell();
	TraderStatus getStatus();
}
