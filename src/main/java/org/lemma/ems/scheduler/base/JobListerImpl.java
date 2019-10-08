package org.lemma.ems.scheduler.base;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JobListerImpl implements JobListener {

	private static final Logger logger = LoggerFactory.getLogger(JobListerImpl.class);

	@Override
	public String getName() {
		return "EMSJobListener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		logger.trace("Jobs to be executed {} on {}", context.getJobDetail().getJobClass(), System.currentTimeMillis());
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		logger.trace("Job execution vetoed {} on {}", context.getJobDetail().getJobClass(), System.currentTimeMillis());
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		logger.trace("Job executed {} on {}, exception {}", context.getJobDetail().getJobClass(),
				System.currentTimeMillis(), jobException);
	}

}
