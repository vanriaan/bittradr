package com.paddavoet.bittradr.service;

import com.paddavoet.bittradr.profit.calculator.Profit;

import java.math.BigDecimal;

public interface ProfitCalculateService {
    boolean isBuying();

    Profit calculateProfit(boolean buying, BigDecimal price);
}
