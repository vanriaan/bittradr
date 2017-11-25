package com.paddavoet.bittradr.market.jobs;

import com.paddavoet.bittradr.application.ApplicationConfig;
import com.paddavoet.bittradr.integration.response.bitfinex.QueryMarketResponse;
import com.paddavoet.bittradr.service.MarketService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncTradeHistoryJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(SyncTradeHistoryJob.class);

	@Autowired
	private MarketService marketService;

	public SyncTradeHistoryJob() {
	}

	public SyncTradeHistoryJob(MarketService marketService) {
		this.marketService = marketService;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		//TODO: Get accounts


//		LOGGER.debug("About to call BitFinEx's API to sync history for account");
		
//		QueryMarketResponse response = ApplicationConfig.BIT_FIN_EX_API.getTradeHistory();
		
//		LOGGER.debug("API Call returned with: " + response.getRawResponse());
		
//		marketService.handleQueryMarketResponse(response);
	}

}
