package com.paddavoet.bittradr.market.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paddavoet.bittradr.application.ApplicationConfig;
import com.paddavoet.bittradr.integration.response.bitfinex.QueryMarketResponse;
import com.paddavoet.bittradr.service.MarketService;

@Component
public class QueryMarketStateJob implements Job {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryMarketStateJob.class);
	
	@Autowired
	private MarketService marketService;
	
	public QueryMarketStateJob() {
	}
	
	public QueryMarketStateJob(MarketService marketService) {
		this.marketService = marketService;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		LOGGER.debug("About to call BitFinEx's API for the 'market state'...");
		
		QueryMarketResponse response = ApplicationConfig.BIT_FIN_EX_API.queryMarket();
		
		LOGGER.debug("API Call returned with: " + response.getRawResponse());
		
		marketService.handleQueryMarketResponse(response);
	}

}
