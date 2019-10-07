package org.lemma.ems.scheduler.base;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

/**
 * @author RTS Sathish  Kumar
 *
 */
public final class SchedulerUtil {
	
	/**
	 * 
	 */
	private SchedulerUtil() {
		// Auto-generated constructor stub
	}
	
	
	/**
	 * @param triggerName
	 * @param groupName
	 * @param cronExpression
	 * @return
	 */
	public static Trigger createTrigger(String triggerName, String groupName, String cronExpression) {
		return TriggerBuilder.newTrigger().withIdentity(triggerName, groupName)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
	}

	/**
	 * @param jobName
	 * @param groupName
	 * @param jobImplementation
	 * @return
	 */
	public static JobDetail createJob(String jobName, String groupName, Class<? extends Job> jobImplementation) {
		return JobBuilder.newJob(jobImplementation).withIdentity(jobName, groupName).build();
	}
}
