package com.paddavoet.bittradr.market.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paddavoet.bittradr.heroku.Main;
import com.paddavoet.bittradr.integration.responses.bitfinex.QueryMarketResponse;
import com.paddavoet.bittradr.market.ExchangeRates;

public class QueryMarketStateJob implements Job {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryMarketStateJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("About to call BitFinEx's API for the 'market state'...");
		
		QueryMarketResponse response = Main.BIT_FIN_EX_API.queryMarket();
		
		LOGGER.info("API Call returned with: " + response.getRawResponse());
		
		ExchangeRates.updateRates(response);
	}

}
