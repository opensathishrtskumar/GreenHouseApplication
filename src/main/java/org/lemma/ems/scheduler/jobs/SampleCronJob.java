package org.lemma.ems.scheduler.jobs;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author RTS Sathish Kumar
 *
 *         Dependencies will be injected
 *
 */
public class SampleCronJob extends QuartzJobBean implements InterruptableJob {

	private static final Logger logger = LoggerFactory.getLogger(SampleCronJob.class);


	@Autowired
	Environment environmment;

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		JobKey key = jobExecutionContext.getJobDetail().getKey();
		logger.debug("executeInternal {}", environmment);

	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		logger.debug("interrupt Stopping thread... ");
	}

}