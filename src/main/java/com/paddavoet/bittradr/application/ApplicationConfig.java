package com.paddavoet.bittradr.application;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.paddavoet.bittradr.component.integration.BitFinExAPI;
import com.paddavoet.bittradr.market.jobs.QueryMarketStateJob;
import com.paddavoet.bittradr.trader.quartz.AutowiringSpringBeanJobFactory;

/**
 * Intended to be the configuration class for the App
 * 
 * @author Riaan
 *
 */
@Configuration
public class ApplicationConfig {
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);

	public static BitFinExAPI BIT_FIN_EX_API;

	private static Scheduler SCHEDULER;

	private static boolean initialized;
	
	private static Properties CONFIG = new Properties();
	private static InputStream PROP_INPUT_STREAM = null;
	
	public static void initialise(ConfigurableApplicationContext appContext) {
		readPropertiesFile();
		
		BIT_FIN_EX_API = new BitFinExAPI(CONFIG.getProperty("api.bitfinex.apikey"), CONFIG.getProperty("api.bitfinex.apisecret"));
		
		try {
			SCHEDULER = StdSchedulerFactory.getDefaultScheduler();

			// and start it off
			SCHEDULER.start();

			if (SCHEDULER != null) {
				initializeScheduledJobs(appContext);
				ApplicationConfig.setInitialized(true);
			} else {
				LOG.warn("Did not schedule the jobs, as the scheduler is null. Check logs for possible scheduler initialization errors?");
			}
		} catch (SchedulerException e) {
			LOG.error("Initialization error with Scheduler: ", e);
		}
	}

	private static void readPropertiesFile() {
		try {
			PROP_INPUT_STREAM = ApplicationConfig.class.getClassLoader().getResourceAsStream("config.properties");

			// load a properties file
			CONFIG.load(PROP_INPUT_STREAM);
		} catch (IOException ex) {
			LOG.error("Exception occured whilst trying to read properties file: " + ex.getMessage());
		} finally {
			if (PROP_INPUT_STREAM != null) {
				try {
					PROP_INPUT_STREAM.close();
				} catch (IOException e) {
					LOG.error("Exception occured whilst trying to close the properties file InputStream: " + e.getMessage());
				}
			}
		}
	}

	public static boolean isInitialized() {
		return initialized;
	}

	public static void setInitialized(boolean initialized) {
		ApplicationConfig.initialized = initialized;
	}

	private static void initializeScheduledJobs(ConfigurableApplicationContext appContext) throws SchedulerException {

		
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
	    jobFactory.setApplicationContext(appContext);
	    SCHEDULER.setJobFactory(jobFactory);
	    
		// define the job and tie it to our MyJob class
		JobDetail job = newJob(QueryMarketStateJob.class).withIdentity("queryMarketState", "group1").build();

		// Tell quartz to schedule the job using our trigger
		if (!SCHEDULER.checkExists(job.getKey())) {
			LOG.info("Job does NOT exist for key {}, scheduling the job now.", job.getKey());

			// Trigger the job to run now, and then repeat every 60 seconds
			Trigger trigger = newTrigger().withIdentity("trigger1", "group1").forJob(job).startNow()
					.withSchedule(simpleSchedule().withIntervalInSeconds(60).repeatForever()).build();

			SCHEDULER.scheduleJob(job, trigger);
		} else {
			LOG.info("Job already exists for key {}, scheduling the existing job", job.getKey());

			// Trigger the job to run now, and then repeat every 60 seconds
			Trigger newTrigger = newTrigger().withIdentity("trigger_new", "group1").forJob(job).startNow()
					.withSchedule(simpleSchedule().withIntervalInSeconds(60).repeatForever()).build();

			List<? extends Trigger> oldTriggers = SCHEDULER.getTriggersOfJob(job.getKey());

			for (Trigger oldTrigger : oldTriggers) {
				SCHEDULER.rescheduleJob(oldTrigger.getKey(), newTrigger);
			}
		}
	}
}
