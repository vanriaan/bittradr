package com.paddavoet.bittradr.service;

import java.math.BigDecimal;

public interface ProfitCalculateService {
    boolean isBuying();

    BigDecimal calculateProfit(boolean buying);
}
