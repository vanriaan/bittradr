package com.paddavoet.bittradr.component;

import com.paddavoet.bittradr.integration.response.bitfinex.QueryMarketResponse;
import com.paddavoet.bittradr.market.TradeValueDirection;
import com.paddavoet.bittradr.market.TradeVelocity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test the velocity updates for UP, DOWN and UNCHANGED
 * @author Deathrid3r747
 */
@RunWith(MockitoJUnitRunner.class)
public class MarketObserverTest {
    private MarketObserver marketObserver;

    @Mock
    private QueryMarketResponse queryMarketResponse;
    private BigDecimal priceOne = new BigDecimal(6543.21);
    private BigDecimal priceTwo = new BigDecimal(7654.32);
    private ArrayList<TradeVelocity> tradeVelocities;

    @Before
    public void setUp() throws Exception {
        marketObserver = new MarketObserver();
        tradeVelocities = new ArrayList<>();
        marketObserver.setVelocities(tradeVelocities);
    }

    @Test
    public void updateVelocitiesFirstVelocity() throws Exception {
        when(queryMarketResponse.getLastPrice()).thenReturn(priceOne);
        marketObserver.updateVelocities(queryMarketResponse);
        assertEquals(marketObserver.getVelocities().size(), 1);
        assertEquals(marketObserver.getVelocities().get(0).getTradeValue(), priceOne);
        assertEquals(marketObserver.getVelocities().get(0).getDirection(), TradeValueDirection.UNCHANGED);
    }

    @Test
    public void updateVelocitiesPriceUp() throws Exception {
        when(queryMarketResponse.getLastPrice()).thenReturn(priceOne).thenReturn(priceTwo);
        marketObserver.updateVelocities(queryMarketResponse);
        marketObserver.updateVelocities(queryMarketResponse);
        assertEquals(marketObserver.getVelocities().size(), 2);
        assertEquals(marketObserver.getVelocities().get(0).getTradeValue(), priceOne);
        assertEquals(marketObserver.getVelocities().get(1).getTradeValue(), priceTwo);
        assertEquals(marketObserver.getVelocities().get(1).getDirection(), TradeValueDirection.UP);
    }

    @Test
    public void updateVelocitiesPriceDown() throws Exception {
        when(queryMarketResponse.getLastPrice()).thenReturn(priceTwo).thenReturn(priceOne);
        marketObserver.updateVelocities(queryMarketResponse);
        marketObserver.updateVelocities(queryMarketResponse);
        assertEquals(marketObserver.getVelocities().size(), 2);
        assertEquals(marketObserver.getVelocities().get(0).getTradeValue(), priceTwo);
        assertEquals(marketObserver.getVelocities().get(1).getTradeValue(), priceOne);
        assertEquals(marketObserver.getVelocities().get(1).getDirection(), TradeValueDirection.DOWN);
    }

    @Test
    public void updateVelocitiesPriceUnchanged() throws Exception {
        when(queryMarketResponse.getLastPrice()).thenReturn(priceOne).thenReturn(priceOne);
        marketObserver.updateVelocities(queryMarketResponse);
        marketObserver.updateVelocities(queryMarketResponse);
        assertEquals(marketObserver.getVelocities().size(), 2);
        assertEquals(marketObserver.getVelocities().get(0).getTradeValue(), priceOne);
        assertEquals(marketObserver.getVelocities().get(1).getTradeValue(), priceOne);
        assertEquals(marketObserver.getVelocities().get(1).getDirection(), TradeValueDirection.UNCHANGED);
    }
}
