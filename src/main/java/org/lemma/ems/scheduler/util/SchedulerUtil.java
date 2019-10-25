package org.lemma.ems.scheduler.util;

import java.util.Date;

import org.lemma.ems.base.dao.dto.SchedulesDTO;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author RTS Sathish Kumar
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

	/**
	 * @param schedule
	 * @param jobDetail
	 * @return
	 */
	public static Trigger createTrigger(SchedulesDTO schedule, JobDetail jobDetail) {
		return JobUtil.createCronTrigger(schedule.getJobKey() + schedule.getGroupKey(), new Date(),
				schedule.getCronExpression(), jobDetail, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
	}

	/**
	 * @param schedule
	 * @param forName
	 * @param context
	 * @return
	 */
	public static JobDetail createJob(SchedulesDTO schedule, Class<QuartzJobBean> forName, ApplicationContext context) {
		return JobUtil.createJob(forName, false, context, schedule.getJobKey(), schedule.getGroupKey());
	}
	
}
