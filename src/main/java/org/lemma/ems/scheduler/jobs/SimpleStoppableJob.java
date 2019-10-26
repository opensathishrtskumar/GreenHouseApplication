package org.lemma.ems.scheduler.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author RTS Sathish Kumar
 *
 */
public abstract class SimpleStoppableJob extends QuartzJobBean {

	/**
	 * Holds reference of Thread which executes
	 */
	private Thread currentThread;

	/**
	 * 
	 */
	protected abstract void executeStoppableJob(JobExecutionContext context) throws JobExecutionException;

	/**
	 * @return
	 */
	public Thread getCurrentThread() {
		return this.currentThread;
	};

	/**
	 * 
	 */
	public void interruptStoppable() {
		getCurrentThread().interrupt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.
	 * quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// Hold current interruptable Thread reference
		this.currentThread = Thread.currentThread();

		executeStoppableJob(context);
	}
}
