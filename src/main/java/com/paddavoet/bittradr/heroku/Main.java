package com.paddavoet.bittradr.heroku;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paddavoet.bittradr.integration.BitFinExAPI;
import com.paddavoet.bittradr.market.jobs.QueryMarketStateJob;

/**
 * This class launches the web application in an embedded Jetty container. This
 * is the entry point to your application. The Java command that is used for
 * launching should fire this main method.
 */
public class Main {

	public static BitFinExAPI BIT_FIN_EX_API = new BitFinExAPI();

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	private static Scheduler SCHEDULER;

	public static void main(String[] args) throws Exception {
		initialise();

		// The port that we should run on can be set into an environment variable
		// Look for that variable and default to 8080 if it isn't there.
		String webPort = System.getenv("PORT");
		if (webPort == null || webPort.isEmpty()) {
			webPort = "8080";
		}

		final Server server = new Server(Integer.valueOf(webPort));
		final WebAppContext root = new WebAppContext();

		root.setContextPath("/");
		// Parent loader priority is a class loader setting that Jetty accepts.
		// By default Jetty will behave like most web containers in that it will
		// allow your application to replace non-server libraries that are part of the
		// container. Setting parent loader priority to true changes this behavior.
		// Read more here: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
		root.setParentLoaderPriority(true);

		final String webappDirLocation = "src/main/webapp/";
		root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
		root.setResourceBase(webappDirLocation);

		server.setHandler(root);

		server.start();
		server.join();
	}

	public static void initialise() {
		
		try {
			SCHEDULER = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			LOGGER.error("Initialization error with Scheduler: ", e);
		}
		
		if (SCHEDULER != null) {
			try {
				initializeScheduledJobs();				
			} catch (SchedulerException se) {
				LOGGER.error("Error initializing Jobs: ", se);
			}
		} else {
			LOGGER.warn("Did not schedule the jobs, as the scheduler is null. Check logs for possible scheduler initialization errors?");
		}
	}

	private static void initializeScheduledJobs() throws SchedulerException {
		
		// define the job and tie it to our MyJob class
		JobDetail job = newJob(QueryMarketStateJob.class).withIdentity("queryMarketState", "group1").build();

		// Trigger the job to run now, and then repeat every 40 seconds
		Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startNow()
				.withSchedule(simpleSchedule().withIntervalInSeconds(60).repeatForever()).build();

		// Tell quartz to schedule the job using our trigger
		SCHEDULER.scheduleJob(job, trigger);
	}
}
