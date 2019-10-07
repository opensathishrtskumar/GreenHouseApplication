package org.lemma.ems.scheduler.base;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author RTS Sathish  Kumar
 *
 */
public abstract class AbstractJob implements Job {

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		preProcessing(arg0);
		work(arg0);
		postProcessing(arg0);
	}

	/**
	 * @param arg0
	 * @throws JobExecutionException
	 */
	protected abstract void preProcessing(JobExecutionContext arg0) throws JobExecutionException;

	/**
	 * @param arg0
	 * @throws JobExecutionException
	 */
	protected abstract void work(JobExecutionContext arg0) throws JobExecutionException;

	/**
	 * @param arg0
	 * @throws JobExecutionException
	 */
	protected abstract void postProcessing(JobExecutionContext arg0) throws JobExecutionException;
}
